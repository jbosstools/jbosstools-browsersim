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

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.jboss.tools.vpe.browsersim.util.BrowserSimResourcesUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class WebSqlLoader {
	
	
	private static BrowserFunction loadPureJsWebSqlFunction;

	public static void initWebSql(final Browser browser) {
		if (loadPureJsWebSqlFunction != null && !loadPureJsWebSqlFunction.isDisposed()) {
			loadPureJsWebSqlFunction.dispose();
		}
		
		loadPureJsWebSqlFunction = new BrowserFunction(browser, "loadPureJsWebSql") {
			@Override
			public Object function(Object[] arguments) {
				super.function(arguments);
				
				String purejswebsql = BrowserSimResourcesUtil.getResourceAsString("javascript/purejswebsql.js");
				browser.execute(purejswebsql);

				String sql = BrowserSimResourcesUtil.getResourceAsString("javascript/sql.js");
				browser.execute(sql);
				return null;
			}
		};
		
		browser.execute(
			"(function() {" +
				"if (!window.purejsOpenDatabase) {" +
					"var origOpenDatabase = window.openDatabase;" +
					"window.openDatabase = function() {" +
						"try {" +
							"var result = origOpenDatabase.apply(this, arguments);" +
							"window.openDatabase = origOpenDatabase;" + // always use origOpenDatabase
							"return result;" +
						"} catch (e) {" +
							"if (e.code === 18) {" +
								"loadPureJsWebSql();" +
								"window.openDatabase = purejsOpenDatabase;" + // always use purejsOpenDatabase
								"return window.openDatabase.apply(this, arguments);" +
							"} else {" +
								"throw e;" +
							"}" +
						"}" +
					"};" +
				"}" +
			"})();");
	}
}
