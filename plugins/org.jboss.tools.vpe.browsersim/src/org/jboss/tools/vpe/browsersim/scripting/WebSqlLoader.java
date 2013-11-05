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
package org.jboss.tools.vpe.browsersim.scripting;

import org.jboss.tools.vpe.browsersim.browser.IBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserFunction;
import org.jboss.tools.vpe.browsersim.browser.IDisposable;
import org.jboss.tools.vpe.browsersim.util.BrowserSimResourcesUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class WebSqlLoader {
	
	
	private static IDisposable loadPureJsWebSqlFunction;

	public static void initWebSql(final IBrowser browser) {
		if (loadPureJsWebSqlFunction != null && !loadPureJsWebSqlFunction.isDisposed()) {
			loadPureJsWebSqlFunction.dispose();
		}
		
		loadPureJsWebSqlFunction = browser.registerBrowserFunction("loadPureJsWebSql", new IBrowserFunction() {
			@Override
			public Object function(Object[] arguments) {
				String purejswebsql = BrowserSimResourcesUtil.getResourceAsString("javascript/purejswebsql.js"); //$NON-NLS-1$
				browser.execute(purejswebsql);

				String sql = BrowserSimResourcesUtil.getResourceAsString("javascript/sql.js"); //$NON-NLS-1$
				browser.execute(sql);
				return null;
			}
		});
		
		browser.execute(
			"(function() {" + //$NON-NLS-1$
				"if (!window.purejsOpenDatabase) {" + //$NON-NLS-1$
					"var origOpenDatabase = window.openDatabase;" + //$NON-NLS-1$
					"window.openDatabase = function() {" + //$NON-NLS-1$
						"try {" + //$NON-NLS-1$
							"var result = origOpenDatabase.apply(this, arguments);" + //$NON-NLS-1$
							"window.openDatabase = origOpenDatabase;" + // always use origOpenDatabase //$NON-NLS-1$
							"return result;" + //$NON-NLS-1$
						"} catch (e) {" + //$NON-NLS-1$
							"if (e.code === 18) {" + //$NON-NLS-1$
								"loadPureJsWebSql();" + //$NON-NLS-1$
								"window.openDatabase = purejsOpenDatabase;" + // always use purejsOpenDatabase //$NON-NLS-1$
								"return window.openDatabase.apply(this, arguments);" + //$NON-NLS-1$
							"} else {" + //$NON-NLS-1$
								"throw e;" + //$NON-NLS-1$
							"}" + //$NON-NLS-1$
						"}" + //$NON-NLS-1$
					"};" + //$NON-NLS-1$
				"}" + //$NON-NLS-1$
			"})();"); //$NON-NLS-1$
	}
}
