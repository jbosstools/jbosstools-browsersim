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
package org.jboss.tools.vpe.browsersim.ui.debug.firebug;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 */
public class FireBugLiteLoader {
	private static final String FIREBUG_LITE_JS_URL = "https://getfirebug.com/firebug-lite.js"; //$NON-NLS-1$
	
	public static void startFireBugOpening(Browser browser) {
		browser.execute("window._fireBugLiteLoading = true;");
		Object fireBugScriptLoaded = browser.evaluate("return !!window.Firebug");
		if (!Boolean.TRUE.equals(fireBugScriptLoaded)) {
			browser.execute(
				"(function() {" +
					"var e = document.createElement('script');" +
					"e.type = 'text/javascript';" +
					"e.src = '" + FIREBUG_LITE_JS_URL + "#startInNewWindow';" +
					"document.head.appendChild(e);" +
				"})()");
		} else {
			browser.execute(
					"window.Firebug.chrome.close();" +  // cannot open FireBug Lite twice without this line 
					"window.Firebug.chrome.toggle(true, true);"
			);
		}
	}
	
	public static boolean isFireBugPopUp(WindowEvent openWindowEvent) {
		Browser parentBrowser = (Browser) openWindowEvent.widget;
		return Boolean.TRUE.equals(parentBrowser.evaluate("return !!window._fireBugLiteLoading"));
	}
	
	public static void processFireBugPopUp(WindowEvent openWindowEvent) {
		Browser parentBrowser = (Browser) openWindowEvent.widget;
		parentBrowser.execute("window._fireBugLiteLoading = false;");
		Shell shell = new Shell(parentBrowser.getDisplay());
		shell.setLayout(new FillLayout());
		
		Browser fireBugBrowser = new Browser(shell, SWT.WEBKIT);
		openWindowEvent.browser = fireBugBrowser;
		
		fireBugBrowser.addVisibilityWindowListener(new VisibilityWindowListener() {
			@Override
			public void show(WindowEvent event) {
				Browser fblBrowser = (Browser)event.widget;
				final Shell shell = fblBrowser.getShell();
				if (event.location != null) {
					shell.setLocation(event.location);
				}
				shell.open();
			}
			
			@Override
			public void hide(WindowEvent event) {
				Browser browser = (Browser)event.widget;
				Shell shell = browser.getShell();
				shell.setVisible(false);
			}
		});
		
		fireBugBrowser.addCloseWindowListener(new CloseWindowListener() {
			public void close(WindowEvent event) {
				Browser browser = (Browser)event.widget;
				Shell shell = browser.getShell();
				shell.close();
			}
		});
	}
}
