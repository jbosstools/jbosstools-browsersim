/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.eclipse.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.osgi.framework.internal.core.BundleFragment;
import org.eclipse.osgi.framework.internal.core.BundleHost;
import org.jboss.tools.vpe.browsersim.eclipse.Activator;
import org.jboss.tools.vpe.browsersim.eclipse.callbacks.BrowserSimCallback;
import org.jboss.tools.vpe.browsersim.eclipse.callbacks.OpenFileCallback;
import org.jboss.tools.vpe.browsersim.eclipse.callbacks.ViewSourceCallback;
import org.osgi.framework.Bundle;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 */
@SuppressWarnings("restriction")
public class BrowserSimLauncher {
	public static final String BROWSERSIM_CLASS_NAME = "org.jboss.tools.vpe.browsersim.ui.BrowserSim"; //$NON-NLS-1$
	private static final BrowserSimCallback[] BROWSERSIM_CALLBACKS = { new ViewSourceCallback(), new OpenFileCallback() };
	private static final String[] REQUIRED_PLUGINS = {
		"org.jboss.tools.vpe.browsersim",
		"org.jboss.tools.vpe.browsersim.browser",
		"org.eclipse.swt"};

	public static void launchBrowserSim(String initialUrl) {
		try {
			
			String classPath = getClassPathString();
			String javaCommand = System.getProperty("java.home") + "/bin/java"; //$NON-NLS-1$ //$NON-NLS-2$
			
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
			commandElements.add(BROWSERSIM_CLASS_NAME);
			if (initialUrl != null) {
				commandElements.add(initialUrl);
			}

			ProcessBuilder processBuilder = new ProcessBuilder(commandElements);
			processBuilder.directory(ConfigurationScope.INSTANCE.getLocation().toFile());
			
			Process browserSimProcess = processBuilder.start();

			final InputStreamReader errorReader = new InputStreamReader(browserSimProcess.getErrorStream());
			final Reader inputReader = new InputStreamReader(browserSimProcess.getInputStream());
			new Thread() {
				public void run() {
					try {
						TransparentReader transparentReader = new TransparentReader(inputReader, System.out);
						String nextLine;
						while ((nextLine = transparentReader.readLine(true)) != null) {
							for (BrowserSimCallback callback : BROWSERSIM_CALLBACKS) { 
								if (nextLine.startsWith(callback.getCallbackId())) {
									callback.call(nextLine, transparentReader);
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
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
						e.printStackTrace();
					}
				};
			}.start();
		} catch (IOException e) {
			Activator.logError(e.getMessage(), e);
		}
	}

	private static String getClassPathString() throws IOException {
		List<Bundle> classPathBundles = new ArrayList<Bundle>();
		for (String requiredPlugin : REQUIRED_PLUGINS) {
			classPathBundles.addAll(getBundleAndFragments(requiredPlugin));
		}
					
		String pathSeparator = System.getProperty("path.separator"); //$NON-NLS-1$
		StringBuilder classPath = new StringBuilder();
		if (classPathBundles.size() > 0) {
			for (int i = 0; i < classPathBundles.size() - 1; i++) {
				classPath.append(getBundleLocation(classPathBundles.get(i)));
				classPath.append(pathSeparator);
			}
			classPath.append(getBundleLocation(classPathBundles.get(classPathBundles.size() - 1)));
		}
		
		return classPath.toString();
	}
	
	private static List<Bundle> getBundleAndFragments(String symbolicName) throws IOException {
		List<Bundle> bundles = new ArrayList<Bundle>();
		Bundle bundle = Platform.getBundle(symbolicName);

		if (bundle == null) {
			throw new IOException("Cannot find bundle: " + symbolicName);
		}
		
		bundles.add(bundle);
		
		if (bundle instanceof BundleHost) {
			BundleFragment[] fragments = ((BundleHost) bundle).getFragments();
			if (fragments != null) {
				Collections.addAll(bundles, fragments);
			}				
		}
		
		return bundles;
	}
	
	private static String getBundleLocation(Bundle bundle) throws IOException {
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
}
