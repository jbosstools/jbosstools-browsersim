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
package org.jboss.tools.vpe.browsersim.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.jboss.tools.vpe.browsersim.ui.BrowserSim;

/**
 * @author Ilya Buizuk (ibuizuk)
 */
public class ManifestUtil {

	private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName"; //$NON-NLS-1$
	private static final String BUNDLE_VERSION = "Bundle-Version"; //$NON-NLS-1$

	public static String getManifestVersion() {
		URLClassLoader classLoader = (URLClassLoader) BrowserSim.class.getClassLoader();
		String version = null;

		try {
	        // If Manifest Version is in jar
			Enumeration<URL> manifestUrls = classLoader.getResources(JarFile.MANIFEST_NAME);
			while (manifestUrls.hasMoreElements()) {
				URL manifestUrl = (URL) manifestUrls.nextElement();
				InputStream inputStream = manifestUrl.openStream();
				if (inputStream != null) {
					Manifest manifest = new Manifest(inputStream);
					String manifestVersion = getManifestVersion(manifest);
					if (!PreferencesUtil.isNullOrEmpty(manifestVersion)) {
						version = manifestVersion; 
					}
				}
			}

			if (version == null) {
				// If Manifest Version is not in jar
				URL browserSimBaseUrl = BrowserSim.class.getClassLoader().getResource("."); //$NON-NLS-1$
				if (browserSimBaseUrl != null && "file".equals(browserSimBaseUrl.getProtocol())) { //$NON-NLS-1$
					File binDir = new File(browserSimBaseUrl.getFile());
					File browsersimDir = binDir.getParentFile();
					File manifestFile = new File(browsersimDir, JarFile.MANIFEST_NAME);
					FileInputStream inputStream = new FileInputStream(manifestFile);
					Manifest manifestFromFile = new Manifest(inputStream);
					version = getManifestVersion(manifestFromFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			version = null;
		}
	
		return version;
	}

	private static String getManifestVersion(Manifest manifest) {
		Attributes mainAttributes = manifest.getMainAttributes();
		String version = null;
		String bundleId = mainAttributes.getValue(BUNDLE_SYMBOLIC_NAME);
		if (bundleId != null && bundleId.startsWith(BrowserSim.BROWSERSIM_PLUGIN_ID)) {
			version = mainAttributes.getValue(BUNDLE_VERSION);
		}
		return version;
	}

}
