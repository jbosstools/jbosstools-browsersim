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
package org.jboss.tools.browsersim.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.jboss.tools.browsersim.ui.BrowserSim;
import org.jboss.tools.browsersim.ui.BrowserSimLogger;

/**
 * @author Ilya Buizuk (ibuizuk)
 */
public class ManifestUtil {

	private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName"; //$NON-NLS-1$
	private static final String BUNDLE_VERSION = "Bundle-Version"; //$NON-NLS-1$
	private static final String REQUIRE_BUNDLE = "Require-Bundle"; //$NON-NLS-1$
	private static final String REQUIRE_BUNDLE_VERSION = "bundle-version"; //$NON-NLS-1$
	private static final String JETTY = "jetty"; //$NON-NLS-1$

	public static String getManifestVersion(Class<?> clazz) {
		String manifestVersion = getManifestVersion(getManifestFromJar(clazz));
		if (!PreferencesUtil.isNullOrEmpty(manifestVersion)) {
			return manifestVersion;
		}

		return  getManifestVersion(getManifestFromFile(clazz));
	}
	
	public static Manifest getManifest(Class<?> clazz) {
		Manifest fromJar = getManifestFromJar(clazz);
		return fromJar != null ? fromJar : getManifestFromFile(clazz);
	}
	
	public static String getJettyVersion() {
		Manifest manifest = getManifest(BrowserSim.class);
		String requireBundle = manifest.getMainAttributes().getValue(REQUIRE_BUNDLE);
		String[] requiredBundles = requireBundle.split(","); //$NON-NLS-1$
		for (String bundle : requiredBundles) {
			if (bundle.contains(JETTY)) { //org.eclipse.jetty.server;bundle-version="8.1.14"
				String[] jettyBundle = bundle.split(";"); //$NON-NLS-1$
				for (String jettyBundlePart : jettyBundle) {
					if(jettyBundlePart.startsWith(REQUIRE_BUNDLE_VERSION)) { // bundle-version="8.1.14"
						return jettyBundlePart.substring(REQUIRE_BUNDLE_VERSION.length() + 2, jettyBundlePart.length() - 1);
					}
				}
			}
		}
		return null;
	}

	private static String getManifestVersion(Manifest manifest) {
		Attributes mainAttributes = manifest.getMainAttributes();
		String bundleId = mainAttributes.getValue(BUNDLE_SYMBOLIC_NAME);
		if (bundleId != null && bundleId.startsWith(BrowserSim.BROWSERSIM_PLUGIN_ID)) {
			return mainAttributes.getValue(BUNDLE_VERSION);
		}
		return null;
	}

	private static Manifest getManifestFromJar(Class<?> clazz) {
		try {
			Enumeration<URL> manifestUrls = clazz.getClassLoader().getResources(JarFile.MANIFEST_NAME);
			while (manifestUrls.hasMoreElements()) {
				URL manifestUrl = manifestUrls.nextElement();
				InputStream inputStream = manifestUrl.openStream();
				if (inputStream != null) {
					return new Manifest(inputStream);
				}
			}
		} catch (IOException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		}
		return null;
	}
	
	private static Manifest getManifestFromFile(Class<?> clazz) {
		try {
			URL browserSimBaseUrl = BrowserSim.class.getClassLoader().getResource("."); //$NON-NLS-1$
			if (browserSimBaseUrl != null && "file".equals(browserSimBaseUrl.getProtocol())) { //$NON-NLS-1$
				File binDir = new File(browserSimBaseUrl.getFile());
				File browsersimDir = binDir.getParentFile();
				File manifestFile = new File(browsersimDir, JarFile.MANIFEST_NAME);
				FileInputStream inputStream = new FileInputStream(manifestFile);
				return new Manifest(inputStream);
			}
		} catch (IOException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		}
		return null;
	}
}
