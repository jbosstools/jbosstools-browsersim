/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.browsersim.browser.IBrowser;
import org.jboss.tools.browsersim.ui.Messages;

public final class LiveReloadUtil {
	
	 private LiveReloadUtil() {
	 }
	 
	 public static void injectScript(IBrowser browser, int port, boolean isJavaFX) {
		 if (browser != null && !browser.isDisposed()) {
			 String script = generateScript(port, isJavaFX);
			 browser.execute(script);
		 }
	 }
	 
	public static boolean isLivereloadAvailable(int port) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			URL url = new URL("http://localhost:" + port + "/livereload.js"); //$NON-NLS-1$ //$NON-NLS-2$
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(1000);
			con.setRequestMethod("HEAD"); //$NON-NLS-1$
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (IOException e) {
			return false;
		}
	}
	
	public static void showLivereloadError(String message, Shell shell) {
		MessageBox warning = new MessageBox(shell, SWT.ICON_WARNING);
		warning.setText(Messages.WARNING);
		warning.setMessage(message);
		warning.open();
	}
	 
	 private static String generateScript(int port, boolean isJavaFX) {
			StringBuilder script = new StringBuilder();
		 	String injection = "var e = document.createElement('script');" + //$NON-NLS-1$
						       "e.type = 'text/javascript';" + //$NON-NLS-1$
						       "e.async = 'true';" + //$NON-NLS-1$
						       "e.src = 'http://localhost:" + port + "/livereload.js';" + //$NON-NLS-1$ //$NON-NLS-2$
						       "document.head.appendChild(e);"; //$NON-NLS-1$
		 	
		 	if (isJavaFX) {
		 		script.append("if (!window.LiveReload) {"). //$NON-NLS-1$
		 			   append(injection).
		 			   append("}");  //$NON-NLS-1$ 
		 	} else {
		 		script.append("if (!window.LiveReload) {"). //$NON-NLS-1$
		 		       append("window.addEventListener('load', function() {"). //$NON-NLS-1$
		 		       append(injection).
		 		       append("});"). //$NON-NLS-1$
		 		       append("}"); //$NON-NLS-1$
		 	}
		 	
		 	return script.toString();
	 }

}
