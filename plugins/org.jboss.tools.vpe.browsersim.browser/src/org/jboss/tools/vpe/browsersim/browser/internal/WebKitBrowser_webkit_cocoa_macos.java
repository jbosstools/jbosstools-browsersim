/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.browser.internal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.browser.AbstractWebKitBrowser;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class WebKitBrowser_webkit_cocoa_macos extends AbstractWebKitBrowser {
	
	public WebKitBrowser_webkit_cocoa_macos(Composite parent, int style) {
		super(parent, style);
	}

	protected void setCustomUserAgent(String userAgent) {
		try {
			Field webBrowserField = Browser.class.getDeclaredField("webBrowser"); //$NON-NLS-1$
			webBrowserField.setAccessible(true);
			Object webKit = webBrowserField.get(this);
	
			Field webViewField = webKit.getClass().getDeclaredField("webView"); //$NON-NLS-1$
			webViewField.setAccessible(true);
			Object webView = webViewField.get(webKit);
			Class<?> NSString = Browser.class.getClassLoader().loadClass("org.eclipse.swt.internal.cocoa.NSString"); //$NON-NLS-1$
			Method setCustomUserAgent = webView.getClass().getDeclaredMethod("setCustomUserAgent", NSString); //$NON-NLS-1$
			
			if (userAgent == null) {
				setCustomUserAgent.invoke(webView, (Object) null);
			} else {
				Method NSString_stringWith = NSString.getDeclaredMethod("stringWith", String.class); //$NON-NLS-1$
				//setCustomUserAgent.invoke(webView, org.eclipse.swt.internal.cocoa.NSString.stringWith(userAgent));
				setCustomUserAgent.invoke(webView, NSString_stringWith.invoke(null, userAgent));
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
