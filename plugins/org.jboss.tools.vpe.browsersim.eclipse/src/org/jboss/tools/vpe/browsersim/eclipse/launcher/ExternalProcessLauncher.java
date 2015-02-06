/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.eclipse.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.BrowserSimLogger;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.eclipse.Activator;
import org.jboss.tools.vpe.browsersim.eclipse.Messages;
import org.jboss.tools.vpe.browsersim.eclipse.dialog.BrowserSimErrorDialog;
import org.jboss.tools.vpe.browsersim.eclipse.preferences.BrowserSimPreferencesPage;
import org.jboss.tools.vpe.browsersim.eclipse.preferences.PreferencesUtil;
import org.jboss.tools.vpe.browsersim.util.ManifestUtil;
import org.osgi.framework.Bundle;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Ilya Buziuk (ibuziuk)
 */
public class ExternalProcessLauncher {
	private static String PATH_SEPARATOR = System.getProperty("path.separator"); //$NON-NLS-1$
	private static final String CONFIGURATION = "-configuration"; //$NON-NLS-1$
	private static final String SWT_GTK3 = "SWT_GTK3"; //$NON-NLS-1$
	private static final String OFF = "0"; //$NON-NLS-1$
	private static final String ON = "1"; //$NON-NLS-1$
	
	
	public static void launchAsExternalProcess(List<String> bundles, List<String> resourcesBundles, List<String> jettyBundles,
			final List<ExternalProcessCallback> callbacks, String className, List<String> parameters, final String programName, IVMInstall jvm) {
		try {			
			String classPath = getClassPathString(bundles, resourcesBundles, jettyBundles);			
			String jreContainerPath = JavaRuntime.newJREContainerPath(jvm).toPortableString();
			if (jreContainerPath != null) {
				List<String> commandElements = new ArrayList<String>();
				
				// RT-35868 and JBIDE-18404 - need to handle CORS headers correctly
				commandElements.add("-Dsun.net.http.allowRestrictedHeaders=true"); //$NON-NLS-1$
				
				if (Platform.OS_MACOSX.equals(Platform.getOS())) {
					commandElements.add("-XstartOnFirstThread"); //$NON-NLS-1$
				}

				addConfigurationFolderParameter(parameters);

				ILaunch launch = launch(programName, classPath, className, parameters, jreContainerPath, commandElements);
				ProcessCallBacks(launch, callbacks);
			} else {
				showErrorDialog(programName);
			}
		} catch (IOException e) {
			Activator.logError(e.getMessage(), e);
		} catch (CoreException e) {
			Activator.logError(e.getMessage(), e);
		} catch (URISyntaxException e) {
			Activator.logError(e.getMessage(), e);
		}
	}
	
	// JBIDE-17718 Need to save preferences in the folder with configuration info 
	private static List<String> addConfigurationFolderParameter(List<String> parameters) throws URISyntaxException, IOException {
		parameters.add(CONFIGURATION);
		parameters.add(PreferencesUtil.getBrowserSimConfigFolderPath());
		return parameters;
	}
	
	private static void ProcessCallBacks(ILaunch launch, final List<ExternalProcessCallback> callbacks) {
		IProcess[] processes = launch.getProcesses();
		if (processes.length > 0) {
			IProcess process = processes[0];
			IStreamMonitor outputStreamMonitor = process.getStreamsProxy().getOutputStreamMonitor();
			outputStreamMonitor.addListener(new IStreamListener() {

				@Override
				public void streamAppended(String message, IStreamMonitor monitor) {
					for (ExternalProcessCallback callback : callbacks) {
						if (message.startsWith(callback.getCallbackId())) {
							try {
								callback.call(message, null);
							} catch (IOException e) {
								Activator.logError(e.getMessage(), e);
							}
						}
					}

				}
			});
		} else {
			Activator.logError("Unable to get launch process", new Throwable()); //$NON-NLS-1$
		}
	}


	public static boolean isGTK2() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		if (IPreferenceStore.TRUE.equals(store.getString(BrowserSimPreferencesPage.BROWSERSIM_GTK_2))){
			return true;
		}
		return false;
	}
	
	private static void setUpGtkEnvironmentalVariable(ILaunchConfigurationWorkingCopy workingCopy) {
		Map<String, String> environment = new HashMap<String, String>();
		if (isGTK2()) {
			environment.put(SWT_GTK3, OFF);
		} else {
			environment.put(SWT_GTK3, ON);
		}
		workingCopy.setAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES,environment);
	}

	public static void showErrorDialog(final String programName) {
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				Shell shell = Display.getDefault().getActiveShell();
				if (shell == null) {
					shell = Display.getDefault().getShells()[0]; // Hot fix for gtk3 
				}
				BrowserSimErrorDialog e = new BrowserSimErrorDialog(shell, Messages.ExternalProcessLauncher_ERROR, shell.getDisplay().getSystemImage(SWT.ICON_ERROR),
						programName, MessageDialog.ERROR, new String[] {IDialogConstants.OK_LABEL}, 0); 
				e.open();
			}
		});
	}
	
	private static String getClassPathString(List<String> bundles, List<String> resourcesBundles, List<String> jettyBundles) throws IOException {
		List<Bundle> classPathBundles = new ArrayList<Bundle>();
		for (String bundleName : bundles) {
			Bundle bundle = Platform.getBundle(bundleName);
			if (bundle != null) {
				classPathBundles.add(bundle);
			}
		}
		
		String jettyVersion = ManifestUtil.getJettyVersion();
		String jettyMajorVersion = jettyVersion.substring(0, jettyVersion.indexOf(".")); //$NON-NLS-1$
		for (String bundleName : jettyBundles) {
			Bundle[] jettys = Platform.getBundles(bundleName, null);
			for (Bundle jetty : jettys) {				
				if (jetty.getVersion().toString().startsWith(jettyMajorVersion)) {
					classPathBundles.add(jetty);
				}
			}
		}
		
		StringBuilder classPath = new StringBuilder();
		if (classPathBundles.size() > 0) {
			for (int i = 0; i < classPathBundles.size() - 1; i++) {
				classPath.append(getBundleLocation(classPathBundles.get(i)));
				classPath.append(PATH_SEPARATOR);
			}
			classPath.append(getBundleLocation(classPathBundles.get(classPathBundles.size() - 1)));
		}	
		
		for (String bundleName : resourcesBundles) {
			Bundle bundle = Platform.getBundle(bundleName);
			if (bundle != null) {
				classPath.append(getResource(bundle));
			}
		}
		
		return classPath.toString();
	}
	
	public static List<String> getClassPathMementos(String classPathString) {
		 String[] classPathArray = classPathString.split(PATH_SEPARATOR);
		 List<String> classpathMementos = new ArrayList<String>();
		 for (int i = 0; i < classPathArray.length; i++) {
		     IRuntimeClasspathEntry cpEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(classPathArray[i]));
		     cpEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
		     try {
		         classpathMementos.add(cpEntry.getMemento());
		     } catch (CoreException e) {
		         BrowserSimLogger.logError(e.getMessage(), e);
		     }
		 }
		 return classpathMementos;
	}
			
	public static String getBundleLocation(Bundle bundle) throws IOException {
		try {
			File bundleLocation = FileLocator.getBundleFile(bundle);
			
			if (bundleLocation.isDirectory()) {
				File binDirectory = new File(bundleLocation, "bin"); //$NON-NLS-1$
				if (binDirectory.isDirectory()) {
					bundleLocation = binDirectory;
				}
			}
	
			return bundleLocation.getCanonicalPath();
		} catch (IOException e) {
			throw new IOException(Messages.ExternalProcessLauncher_NO_BUNDLE + bundle.getSymbolicName(), e);
		}
	}
	
	private static String getResource(Bundle bundle) throws IOException {
		StringBuilder result = new StringBuilder();
		
		//URL[] res = FileLocator.findEntries(bundle, new Path("plugins"));
		String location = FileLocator.getBundleFile(bundle).getCanonicalPath(); 
		File resources = new File(location + "/plugins"); //$NON-NLS-1$
		if (resources.exists()) {
			for(File resource : resources.listFiles()) {
				result.append(PATH_SEPARATOR);
				result.append(resource.getCanonicalPath());
			}
		}
		return result.toString();
	}
	
	
	
	private static ILaunch launch(String programmName, String classPath, String className, List<String> parameters, String jvmPath, List<String> commandElements) throws CoreException {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, programmName);		
		setWorkingCopyAttributes(workingCopy, classPath, className, parameters, jvmPath, commandElements);				
		return workingCopy.launch(ILaunchManager.RUN_MODE, null);
	}


	private static void setWorkingCopyAttributes(ILaunchConfigurationWorkingCopy workingCopy, String classPath,
			String className, List<String> parameters, String jreContainerPath, List<String> commandElements) {
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, getClassPathMementos(classPath));
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_JRE_CONTAINER_PATH, jreContainerPath);
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, PreferencesUtil.argumentsListToString(commandElements));
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, PreferencesUtil.argumentsListToString(parameters));
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, className);

		if (PlatformUtil.OS_LINUX.equals(PlatformUtil.getOs())) {
			setUpGtkEnvironmentalVariable(workingCopy);
		}
	}
}