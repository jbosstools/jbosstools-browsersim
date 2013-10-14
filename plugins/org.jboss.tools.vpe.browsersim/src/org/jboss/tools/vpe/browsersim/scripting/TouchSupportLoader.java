package org.jboss.tools.vpe.browsersim.scripting;

import org.eclipse.swt.browser.Browser;
import org.jboss.tools.vpe.browsersim.util.BrowserSimResourcesUtil;

public class TouchSupportLoader {
	public static void initTouchEvents(final Browser browser) {
		String phantomLimb = BrowserSimResourcesUtil.getResourceAsString("javascript/phantom-limb.js");
		browser.execute(
				"if (!window._limbLoaded) {"
					+ "window.addEventListener('DOMContentLoaded', function () {"
						+ phantomLimb
					+ "});"
				+ "}"
				+ "window._limbLoaded = true;");
		
	}
}
