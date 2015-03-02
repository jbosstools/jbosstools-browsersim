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
package org.jboss.tools.browsersim.ui.scripting;

import org.jboss.tools.browsersim.browser.IBrowser;
import org.jboss.tools.browsersim.ui.util.BrowserSimResourcesUtil;

public class TouchSupportLoader {
	public static void initTouchEvents(final IBrowser browser) {
		String phantomLimb = BrowserSimResourcesUtil.getResourceAsString("javascript/phantom-limb.js"); //$NON-NLS-1$
		browser.execute(
				"if (!window._limbLoaded) {" //$NON-NLS-1$
					+ "window.addEventListener('DOMContentLoaded', function () {" //$NON-NLS-1$
						+ phantomLimb
					+ "});" //$NON-NLS-1$
				+ "}" //$NON-NLS-1$
				+ "window._limbLoaded = true;"); //$NON-NLS-1$
		
	}
}
