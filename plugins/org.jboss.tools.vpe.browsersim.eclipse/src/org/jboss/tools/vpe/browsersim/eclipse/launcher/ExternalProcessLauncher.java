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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.vpe.browsersim.eclipse.Activator;
import org.jboss.tools.vpe.browsersim.eclipse.dialog.BrowserSimErrorDialog;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.internal.ExternalProcessPostShutdownDestroyer;
import org.jboss.tools.vpe.browsersim.eclipse.preferences.BrowserSimPreferencesPage;
import org.jboss.tools.vpe.browsersim.eclipse.preferences.PreferencesUtil;
import org.osgi.framework.Bundle;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ExternalProcessLauncher {
	private static String PATH_SEPARATOR = System.getProperty("path.separator"); //$NON-NLS-1$
	
	public static void launchAsExternalProcess(List<String> bundles, List<String> resourcesBundles,
			final List<ExternalProcessCallback> callbacks, String className, List<String> parameters) {
		try {			
			String classPath = getClassPathString(bundles, resourcesBundles);
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			String jvmPath = "";
			if (IPreferenceStore.FALSE.equals(store.getString(BrowserSimPreferencesPage.BROWSERSIM_JVM_AUTOMATICALLY))) {
				// path to browserSim jvm is located in preferences
				String jvmId = store.getString(BrowserSimPreferencesPage.BROWSERSIM_JVM_ID);
				jvmPath = PreferencesUtil.getJVMPath(jvmId);
			} else {
				// detect jvm automatically
				List<IVMInstall> jvms = PreferencesUtil.getSuitableJvms();
				if (!jvms.isEmpty()) {
					jvmPath = jvms.get(0).getInstallLocation().getAbsolutePath();
				}
			}
			
			if (!jvmPath.isEmpty()) {
				String javaCommand = jvmPath + "/bin/java"; //$NON-NLS-1$ //$NON-NLS-2$
				//String javaCommand = System.getProperty("java.home") + "/bin/java"; //$NON-NLS-1$ //$NON-NLS-2$
				
				// This is a workaround for JDK 7: JBIDE-12467 Unable to Run Browsersim in Windows7 64b + JRE7 32b
				// On Windows and Java 7 the 'java.home' variable always points to JRE, but 'eclipse.vm' may point to JDK,
				// if it is specified explicitly in the eclipse.ini.
				boolean isJava1_7 = "1.7".equals(System.getProperty("java.specification.version")); //$NON-NLS-1$ //$NON-NLS-2$
				if (Platform.OS_WIN32.equals(Platform.getOS()) && isJava1_7) {
					String eclipseVm = System.getProperty("eclipse.vm");
					if (eclipseVm != null) {
						if (eclipseVm.endsWith("java") || eclipseVm.endsWith("java.exe") 
								|| eclipseVm.endsWith("javaw") || eclipseVm.endsWith("javaw.exe")) {
							javaCommand = eclipseVm;
						}
					}
				}
				
				List<String> commandElements = new ArrayList<String>();
				commandElements.add(javaCommand);
				
				if (Platform.OS_MACOSX.equals(Platform.getOS())) {
					commandElements.add("-XstartOnFirstThread"); //$NON-NLS-1$
					if (Platform.ARCH_X86.equals(Platform.getOSArch())) {
						commandElements.add("-d32"); //$NON-NLS-1$
					}
				}
				
				commandElements.add("-cp"); //$NON-NLS-1$
				commandElements.add(classPath);
				commandElements.add(className);
				
				//optional parameters
				for (String parameter : parameters) {
					commandElements.add(parameter);
				}
				
				ProcessBuilder processBuilder = new ProcessBuilder(commandElements);
				processBuilder.directory(ConfigurationScope.INSTANCE.getLocation().toFile());
				
				Process browserSimProcess = processBuilder.start();
				final IWorkbenchListener browserSimPostShutDownDestroyer = new ExternalProcessPostShutdownDestroyer(browserSimProcess);
				PlatformUI.getWorkbench().addWorkbenchListener(browserSimPostShutDownDestroyer);
				
				final InputStreamReader errorReader = new InputStreamReader(browserSimProcess.getErrorStream());
				final Reader inputReader = new InputStreamReader(browserSimProcess.getInputStream());
				new Thread() {
					public void run() {
						try {
							TransparentReader transparentReader = new TransparentReader(inputReader, System.out);
							String nextLine;
							while ((nextLine = transparentReader.readLine(true)) != null) {
								for (ExternalProcessCallback callback : callbacks) { 
									if (nextLine.startsWith(callback.getCallbackId())) {
										callback.call(nextLine, transparentReader);
									}
								}
							}
						} catch (IOException e) {
							Activator.logError(e.getMessage(), e);
						}  finally {
							PlatformUI.getWorkbench().removeWorkbenchListener(browserSimPostShutDownDestroyer);
						}
					};
				}.start();
				new Thread() {
					public void run() {
						int nextCharInt;
						try {
							while ((nextCharInt = errorReader.read()) >= 0) {
								System.err.print((char) nextCharInt);
							}
						} catch (IOException e) {
							Activator.logError(e.getMessage(), e);
						}
					};
				}.start();
			} else {
				Shell shell = Display.getDefault().getActiveShell();
				BrowserSimErrorDialog e = new BrowserSimErrorDialog(shell, "Error", shell.getDisplay().getSystemImage(SWT.ICON_ERROR),
						"BrowserSim is failed to start", MessageDialog.ERROR, new String[] {"OK"}, 0); 
				e.open();
			}
		} catch (IOException e) {
			Activator.logError(e.getMessage(), e);
		}		
	}
	
	private static String getClassPathString(List<String> bundles, List<String> resourcesBundles) throws IOException {
		List<Bundle> classPathBundles = new ArrayList<Bundle>();
		for (String bundleName : bundles) {
			Bundle bundle = Platform.getBundle(bundleName);
			if (bundle != null) {
				classPathBundles.add(bundle);
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
			throw new IOException("Cannot resolve the path to bundle: " + bundle.getSymbolicName(), e);
		}
	}
	
	private static String getResource(Bundle bundle) throws IOException {
		StringBuilder result = new StringBuilder();
		
		//URL[] res = FileLocator.findEntries(bundle, new Path("plugins"));
		String location = FileLocator.getBundleFile(bundle).getCanonicalPath(); 
		File resources = new File(location + "/plugins");
		if (resources.exists()) {
			for(File resource : resources.listFiles()) {
				result.append(PATH_SEPARATOR);
				result.append(resource.getCanonicalPath());
			}
		}
		return result.toString();
	}
}
