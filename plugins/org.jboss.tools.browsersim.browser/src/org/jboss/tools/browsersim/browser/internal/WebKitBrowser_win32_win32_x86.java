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
package org.jboss.tools.browsersim.browser.internal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.browsersim.browser.AbstractWebKitBrowser;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
@SuppressWarnings("restriction")
public class WebKitBrowser_win32_win32_x86 extends AbstractWebKitBrowser {
	public WebKitBrowser_win32_win32_x86(Composite parent, int style) {
		super(parent, style);
	}
	
	protected void setUserAgentImpl(String userAgent) {
		try {
			int userAgentValue;
			
			if (userAgent != null) {
				char[] data = (userAgent + '\0').toCharArray ();
				
				//userAgentValue = org.eclipse.swt.internal.ole.win32.COM.SysAllocString(data);
				userAgentValue = invokeCOM_SysAllocString(data);
			} else {
				userAgentValue = 0;
			}
			
			Field webBrowserField = Browser.class.getDeclaredField("webBrowser"); //$NON-NLS-1$
			webBrowserField.setAccessible(true);
			Object webKit = webBrowserField.get(this);

			Field webViewField = webKit.getClass().getDeclaredField("webView"); //$NON-NLS-1$
			webViewField.setAccessible(true);
			Object webView = webViewField.get(webKit);

			Method setCustomUserAgentMethod = webView.getClass().getDeclaredMethod("setCustomUserAgent", int.class); //$NON-NLS-1$
			setCustomUserAgentMethod.invoke(webView, userAgentValue);
			
			if (userAgent != null) {
				//org.eclipse.swt.internal.ole.win32.COM.SysFreeString(userAgentValue);
				invokeCOM_SysFreeString(userAgentValue);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Invokes {@link org.eclipse.swt.internal.ole.win32.COM#SysAllocString(char[])}. 
	 */
	private int /*long*/ invokeCOM_SysAllocString(char [] sz) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> COM = Browser.class.getClassLoader().loadClass("org.eclipse.swt.internal.ole.win32.COM"); //$NON-NLS-1$
		Method COM_SysAllocString = COM.getDeclaredMethod("SysAllocString", char[].class); //$NON-NLS-1$
		return (Integer) COM_SysAllocString.invoke(null, sz);
	}
	
	/**
	 * Invokes {@link org.eclipse.swt.internal.ole.win32.COM#SysFreeString(int)}. 
	 */
	private void invokeCOM_SysFreeString(int /*long*/ bstr) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> COM = Browser.class.getClassLoader().loadClass("org.eclipse.swt.internal.ole.win32.COM"); //$NON-NLS-1$
		Method COM_SysFreeString = COM.getDeclaredMethod("SysFreeString", int.class /*long.class*/); //$NON-NLS-1$
		COM_SysFreeString.invoke(null, bstr);
	}
}
