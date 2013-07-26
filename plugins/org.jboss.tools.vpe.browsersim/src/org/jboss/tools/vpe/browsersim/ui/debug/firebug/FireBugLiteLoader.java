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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 */
public class FireBugLiteLoader {
	private static final String FIREBUG_LITE_JS_URL = "https://getfirebug.com/firebug-lite.js"; //$NON-NLS-1$
	
	public static void startFireBugOpening(Browser browser) {
		browser.execute("window._fireBugLiteLoading = true;"); //$NON-NLS-1$
		Object fireBugScriptLoaded = browser.evaluate("return !!window.Firebug"); //$NON-NLS-1$
		if (!Boolean.TRUE.equals(fireBugScriptLoaded)) {
			browser.execute(
				"(function() {" + //$NON-NLS-1$
				   "var initializeFireBug = function() {" + //$NON-NLS-1$
						  /* Cordova's InAppBrowser API overrides window.open function, so we have to restore it
						   * (see RippleInjector.java and JBIDE-14625) */
						  "var cordovaOpen = window.open;" + //$NON-NLS-1$
						  "window.open = window._bsOriginalWindowOpen || window.open;" + //$NON-NLS-1$
						  "window.Firebug.chrome.toggle(true, true);" + //$NON-NLS-1$
						  "window.open = cordovaOpen;" + //$NON-NLS-1$
					"};" + //$NON-NLS-1$
					"var e = document.createElement('script');" + //$NON-NLS-1$
					"e.type = 'text/javascript';" + //$NON-NLS-1$
					"e.src = '" + FIREBUG_LITE_JS_URL + "';" + //$NON-NLS-1$ //$NON-NLS-2$
					"e.addEventListener('load'," + //$NON-NLS-1$
						"function() {" + //$NON-NLS-1$
							/* XXX: Two timeouts because we need to run our initializeFireBug method
							 * AFTER two inner FireBug Lite timeouts */
							"setTimeout(function() {" + //$NON-NLS-1$
								"setTimeout(initializeFireBug, 0)" + //$NON-NLS-1$
							 "}, 0)" + //$NON-NLS-1$
						"}, false);" + //$NON-NLS-1$
					"document.head.appendChild(e);" + //$NON-NLS-1$
				"})()"); //$NON-NLS-1$
		} else {
			browser.execute(
					"var cordovaOpen = window.open;" + //$NON-NLS-1$
					"window.open = window._bsOriginalWindowOpen || window.open;" + //$NON-NLS-1$
					"window.Firebug.chrome.close();" +  // cannot open FireBug Lite twice without this line //$NON-NLS-1$ 
					"window.Firebug.chrome.toggle(true, true);" + //$NON-NLS-1$
					"window.open = cordovaOpen;" //$NON-NLS-1$
			);
		}
	}
	
	public static boolean isFireBugPopUp(WindowEvent openWindowEvent) {
		Browser parentBrowser = (Browser) openWindowEvent.widget;
		return Boolean.TRUE.equals(parentBrowser.evaluate("return !!window._fireBugLiteLoading")); //$NON-NLS-1$
	}
	
	public static void processFireBugPopUp(WindowEvent openWindowEvent, BrowserSimSkin skin) {
		final Browser parentBrowser = (Browser) openWindowEvent.widget;
		parentBrowser.execute("window._fireBugLiteLoading = false;"); //$NON-NLS-1$
		
		Shell shell = new Shell(BrowserSimUtil.getParentShell(skin), SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		
		final Browser fireBugBrowser = new Browser(shell, SWT.WEBKIT);
		openWindowEvent.browser = fireBugBrowser;
		
		fireBugBrowser.addVisibilityWindowListener(new VisibilityWindowListener() {
			@Override
			public void show(WindowEvent event) {
				Browser fblBrowser = (Browser)event.widget;
				final Shell shell = fblBrowser.getShell();
				Point location = event.location;
				if (location != null && Display.getDefault().getClientArea().intersects(
						new Rectangle(location.x, location.y, event.size.x, event.size.y))) {
					shell.setLocation(location);
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
		
		skin.getShell().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				if (!fireBugBrowser.isDisposed() && !fireBugBrowser.getShell().isDisposed()) {
					fireBugBrowser.getShell().dispose();
				}
			}
		});
		
		// yradtsevich: fix for JBIDE-13625 Browsersim closes unexpectively when closing Firebug Lite
		if (PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			shell.addShellListener(new ShellAdapter() {
				@Override
				public void shellClosed(ShellEvent e) {
					if (!parentBrowser.isDisposed()) {
						parentBrowser.execute("window.Firebug.chrome.close();"); //$NON-NLS-1$
					}
				}
			});
		} else {
			fireBugBrowser.addCloseWindowListener(new CloseWindowListener() {
				public void close(WindowEvent event) {
					Browser browser = (Browser)event.widget;
					Shell shell = browser.getShell();
					shell.close();
				}
			});
		}
	}
}
