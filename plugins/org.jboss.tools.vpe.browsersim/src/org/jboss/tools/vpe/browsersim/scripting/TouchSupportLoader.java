package org.jboss.tools.vpe.browsersim.scripting;

import org.eclipse.swt.browser.Browser;
import org.jboss.tools.vpe.browsersim.util.BrowserSimResourcesUtil;

public class TouchSupportLoader {
	public static void initTouchEvents(final Browser browser) {
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
