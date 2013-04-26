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
package org.jboss.tools.vpe.browsersim.eclipse.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.eclipse.callbacks.OpenFileCallback;
import org.jboss.tools.vpe.browsersim.eclipse.callbacks.ViewSourceCallback;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 */
public class BrowserSimLauncher {
	public static final String BROWSERSIM_CLASS_NAME = "org.jboss.tools.vpe.browsersim.BrowserSimRunner"; //$NON-NLS-1$
	public static final List<ExternalProcessCallback> BROWSERSIM_CALLBACKS = Arrays.asList(
		new ViewSourceCallback(),
		new OpenFileCallback()
	);
	public static final List<String> BUNDLES = getBundles();
	public static final List<String> RESOURCES_BUNDLES = Arrays.asList(
		"org.jboss.tools.vpe.browsersim.win32.win32.x86_64"
	);
	
	//if you change this parameter, see also @org.jbosstools.browsersim.ui.BrowserSim
	public static final String NOT_STANDALONE = "-not-standalone"; //$NON-NLS-1$
	

	public static void launchBrowserSim(String initialUrl) {
		List<String> parameters = new ArrayList<String>();
		
		parameters.add(NOT_STANDALONE);
		if (initialUrl != null) {
			parameters.add(initialUrl);
		}
		
		ExternalProcessLauncher.launchAsExternalProcess(BUNDLES, RESOURCES_BUNDLES,
				BROWSERSIM_CALLBACKS, BROWSERSIM_CLASS_NAME, parameters);
	}
	
	private static List<String> getBundles() {
		List<String> bundles = new ArrayList<String>();
		bundles.add("org.jboss.tools.vpe.browsersim");
		bundles.add("org.jboss.tools.vpe.browsersim.browser");
		
		//for Win64 we add swt from fragment which mached in resources
		if (!(PlatformUtil.OS_WIN32.equals(PlatformUtil.getOs())
				&& PlatformUtil.ARCH_X64.equals(PlatformUtil.getArch()))) {
			bundles.add("org.eclipse.swt");
			bundles.add("org.eclipse.swt."+ PlatformUtil.CURRENT_PLATFORM);
		}
		return bundles;
	}
}
