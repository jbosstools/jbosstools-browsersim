/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
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
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 */
public class BrowserSimLauncher {
	public static void launchBrowserSim(String initialUrl) {
		String pathSeparator = System.getProperty("path.separator");
		try {
			String classPath = getBundleLocation("org.jboss.tools.vpe.browsersim")
					+ pathSeparator + getBundleLocation("org.jboss.tools.vpe.browsersim.browser")
					+ pathSeparator + getBundleLocation("org.eclipse.swt")
					+ pathSeparator + getBundleLocation("org.eclipse.swt." + PlatformUtil.CURRENT_PLATFORM);
			String javaCommand = System.getProperty("java.home") + "/bin/java";
			
			List<String> commandElements = new ArrayList<String>();
			commandElements.add(javaCommand);
			if (Platform.OS_MACOSX.equals(Platform.getOS())) {
				commandElements.add("-XstartOnFirstThread");
				if (Platform.ARCH_X86.equals(Platform.getOSArch())) {
					commandElements.add("-d32");
				}
			}
			
			commandElements.add("-cp");
			commandElements.add(classPath);
			commandElements.add("org.jboss.tools.vpe.browsersim.ui.BrowserSim");
			if (initialUrl != null) {
				commandElements.add(initialUrl);
			}

			ProcessBuilder processBuilder = new ProcessBuilder(commandElements);
			processBuilder.directory(ConfigurationScope.INSTANCE.getLocation().toFile());
			
			Process browserSimProcess = processBuilder.start();

			final InputStream errorStream = browserSimProcess.getErrorStream();
			final InputStream inputStream = browserSimProcess.getInputStream();
			new Thread() {
				public void run() {
					int nextByte;
					try {
						while ((nextByte = inputStream.read()) >= 0) {
							System.out.write(nextByte);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();
			new Thread() {
				public void run() {
					int nextByte;
					try {
						while ((nextByte = errorStream.read()) >= 0) {
							System.err.write(nextByte);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getBundleLocation(String symbolicName) throws IOException {
		String eclipseHome = new URL(System.getProperty("eclipse.home.location")).getFile();
		String locationId = Platform.getBundle(symbolicName).getLocation();

		File bundleLocation;
		if (locationId.startsWith("reference:file:")) {
			bundleLocation = new File(locationId.substring("reference:file:".length()));
		} else {
			bundleLocation = new File(locationId);
		}
		
		if (!bundleLocation.isAbsolute()) {
			bundleLocation = new File(eclipseHome, bundleLocation.getPath());
		}
		
		if (bundleLocation.isDirectory()) {
			File binDirectory = new File(bundleLocation, "bin");
			if (binDirectory.isDirectory()) {
				bundleLocation = binDirectory;
			}
		}

		return bundleLocation.getCanonicalPath();
	}
}
