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
package org.jboss.tools.vpe.browsersim;

import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;
import org.jboss.tools.vpe.browsersim.ui.CocoaUIEnhancer;
import org.jboss.tools.vpe.browsersim.ui.Messages;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */

public class BrowserSimRunner {
	public static final String NOT_STANDALONE = "-not-standalone"; //$NON-NLS-1$
	public static final String ABOUT_BLANK = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
	
	public static void main(String[] args) {
		if (PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			CocoaUIEnhancer.initializeMacOSMenuBar(Messages.BrowserSim_BROWSER_SIM);
		}
		BrowserSimArgs browserSimArgs = BrowserSimArgs.parseArgs(args);
		BrowserSim.isStandalone = browserSimArgs.isStandalone();
		
		String path = browserSimArgs.getPath();
		String url;
		if (path != null) {
			try {
				new URI(path); // validate URL
				url = path;
			} catch (URISyntaxException e) {
				url = ABOUT_BLANK;
			}
		} else {
			url = ABOUT_BLANK;
		}
		
		BrowserSim browserSim = new BrowserSim(url);
		browserSim.open();

		Display display = Display.getDefault();
		while (!display.isDisposed() && display.getShells().length > 0) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
