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
public class WebKitBrowser_gtk_linux_x86_64 extends AbstractWebKitBrowser {
	private static final byte[] USER_AGENT; // = WebKitGTK.user_agent
	static {
		int length = "user-agent".length(); //$NON-NLS-1$
		char [] chars = new char[length];
		"user-agent".getChars(0, length, chars, 0); //$NON-NLS-1$
		USER_AGENT = new byte[length + 1];
		for (int i = 0; i < length; i++) {
			USER_AGENT[i] = (byte) chars [i];
		}
	}
	
	public WebKitBrowser_gtk_linux_x86_64(Composite parent, int style) {
		super(parent, style);
	}

	protected void setCustomUserAgent(String userAgent) {
		try {
			long webView = getThis_webBrowser_webView();
			
			//long settings = WebKitGTK.webkit_web_view_get_settings(webView);
			long settings = invokeWebKitGTK_webkit_web_view_get_settings(webView);
			
			if (userAgent == null) {
				//org.eclipse.swt.internal.gtk.OS.g_object_set(settings, USER_AGENT, 0, 0);
				invokeOS_g_object_set(settings, USER_AGENT, 0, 0);
			} else {
				//byte[] bytes = org.eclipse.swt.internal.Converter.wcsToMbcs(null, userAgent, true);
				byte[] bytes = invokeConverter_wcsToMbcs(null, userAgent, true);
				
				//org.eclipse.swt.internal.gtk.OS.g_object_set(settings, USER_AGENT, bytes, 0);
				invokeOS_g_object_set(settings, USER_AGENT, bytes, 0);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	// this.webBrowser.webView
	private long getThis_webBrowser_webView() throws NoSuchFieldException,
			IllegalAccessException {
		Field webBrowserField = Browser.class.getDeclaredField("webBrowser"); //$NON-NLS-1$
		webBrowserField.setAccessible(true);
		Object webKit = webBrowserField.get(this);

		Field webViewField = webKit.getClass().getDeclaredField("webView"); //$NON-NLS-1$
		webViewField.setAccessible(true);
		long webView = (Long) webViewField.get(webKit);
		return webView;
	}
	
	//void org.eclipse.swt.internal.gtk.OS#g_object_set(long /*int*/ object, byte[] first_property_name, int data, long /*int*/ terminator)
	private void invokeOS_g_object_set(long /*int*/ object, byte[] first_property_name, int data, long /*int*/ terminator)
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Class<?> OS = Browser.class.getClassLoader().loadClass("org.eclipse.swt.internal.gtk.OS"); //$NON-NLS-1$
		Method OS_g_object_set = OS.getDeclaredMethod("g_object_set", long.class, byte[].class, int.class, long.class); //$NON-NLS-1$
		OS_g_object_set.invoke(null, object, first_property_name, data, terminator);
	}

	//void org.eclipse.swt.internal.gtk.OS#void g_object_set(long /*int*/ object, byte[] first_property_name, byte[] data, long /*int*/ terminator)
	private void invokeOS_g_object_set(long /*int*/ object, byte[] first_property_name, byte[] data, long /*int*/ terminator)
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Class<?> OS = Browser.class.getClassLoader().loadClass("org.eclipse.swt.internal.gtk.OS"); //$NON-NLS-1$
		Method OS_g_object_set = OS.getDeclaredMethod("g_object_set", long.class, byte[].class, byte[].class, long.class); //$NON-NLS-1$
		OS_g_object_set.invoke(null, object, first_property_name, data, terminator);
	}
	
	// public static byte [] org.eclipse.swt.internal.Converter#wcsToMbcs (String codePage, String string, boolean terminate)
	private byte [] invokeConverter_wcsToMbcs (String codePage, String string, boolean terminate)
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Class<?> Converter = Browser.class.getClassLoader().loadClass("org.eclipse.swt.internal.Converter"); //$NON-NLS-1$
		Method Converter_wcsToMbcs = Converter.getDeclaredMethod("wcsToMbcs", String.class, String.class, boolean.class); //$NON-NLS-1$
		return (byte[]) Converter_wcsToMbcs.invoke(null, codePage, string, terminate);
	}
	
	// org.eclipse.swt.internal.webkit.WebKitGTK#webkit_web_view_get_settings(webView);
	private long invokeWebKitGTK_webkit_web_view_get_settings(long /*int*/ webView)
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Class<?> webKitGTKClass = Browser.class.getClassLoader().loadClass("org.eclipse.swt.internal.webkit.WebKitGTK"); //$NON-NLS-1$
		Method webkit_web_view_get_settingsMethod = webKitGTKClass.getDeclaredMethod("webkit_web_view_get_settings", long.class); //$NON-NLS-1$
		return (Long) webkit_web_view_get_settingsMethod.invoke(null, webView);
	}
}
