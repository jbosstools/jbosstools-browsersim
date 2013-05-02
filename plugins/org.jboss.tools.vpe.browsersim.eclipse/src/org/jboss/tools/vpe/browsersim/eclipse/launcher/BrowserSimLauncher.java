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
	public static final List<String> REQUIRED_BUNDLES = Arrays.asList(
		"org.jboss.tools.vpe.browsersim",
		"org.jboss.tools.vpe.browsersim.browser",
		"org.eclipse.swt"
	);
	public static final List<String> OPTIONAL_BUNDLES = Arrays.asList(	
		// org.eclipse.swt plugin may contain this fragment in itself - that is why it is optional. See JBIDE-11923
		"org.eclipse.swt." + PlatformUtil.CURRENT_PLATFORM 
	);
	//if you change this parameter, see also @org.jbosstools.browsersim.ui.BrowserSim
	public static final String NOT_STANDALONE = "-not-standalone"; //$NON-NLS-1$
	

	public static void launchBrowserSim(String initialUrl) {
		List<String> parameters = new ArrayList<String>();
		
		parameters.add(NOT_STANDALONE);
		if (initialUrl != null) {
			parameters.add(initialUrl);
		}
		
		ExternalProcessLauncher.launchAsExternalProcess(REQUIRED_BUNDLES, OPTIONAL_BUNDLES,
				BROWSERSIM_CALLBACKS, BROWSERSIM_CLASS_NAME, parameters);
	}
}
