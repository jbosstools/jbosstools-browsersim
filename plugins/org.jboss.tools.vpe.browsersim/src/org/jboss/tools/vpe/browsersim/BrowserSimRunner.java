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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */

public class BrowserSimRunner {
	private static final String NOT_STANDALONE = "-not-standalone"; //$NON-NLS-1$
	private static final String DEFAULT_URL = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
	
	public static void main(String[] args) {
		List<String> params = new ArrayList<String>(Arrays.asList(args));
		BrowserSim.isStandalone = !params.contains(NOT_STANDALONE);
		if (!BrowserSim.isStandalone) {
			params.remove(NOT_STANDALONE);
		}
		
		String homeUrl;
		if (params.size() > 0) {
			String lastArg = params.get(params.size() - 1);
			try {
				new URI(lastArg); // validate URL
				homeUrl = lastArg;
			} catch (URISyntaxException e) {
				homeUrl = DEFAULT_URL;
			}
		} else {
			homeUrl = DEFAULT_URL;
		}
		
		BrowserSim browserSim = new BrowserSim(homeUrl);		
		browserSim.open(homeUrl);

		Display display = BrowserSim.getDisplay();
		while (!display.isDisposed() && display.getShells().length > 0) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
