/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui.devtools;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletContext;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.jboss.tools.browsersim.ui.BrowserSimLogger;
import org.jboss.tools.browsersim.ui.Messages;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Ilya Buziuk (ibuziuk)
 */
public class DevToolsWebSocket implements WebSocketListener {
	public static final String WEB_SOCKET_ATTR_NAME = "org.jboss.tools.browsersim.ui.devtools.DevToolsWebSocket"; //$NON-NLS-1$
	private Session session;
	private ServletContext context;

	public DevToolsWebSocket() {
		this.context = DevToolsDebuggerServer.getServletContext();
	}

	@Override
	public void onWebSocketConnect(Session session) {
		this.session = session;
		if (context.getAttribute(WEB_SOCKET_ATTR_NAME) != null) {
			session.close();
			System.out.println(Messages.DevTools_CONNECTION_REFUSED);
		} else {
			context.setAttribute(WEB_SOCKET_ATTR_NAME, this);
			System.out.println(Messages.DevTools_CLIENT_CONNECTED);
		}
	}

	@Override
	public void onWebSocketClose(int closeCode, String message) {
		DevToolsWebSocket mainSocket = (DevToolsWebSocket) context.getAttribute(WEB_SOCKET_ATTR_NAME);
		if (mainSocket == this) {
			context.removeAttribute(WEB_SOCKET_ATTR_NAME);
			System.out.println(Messages.DevTools_CLIENT_DISCONNECTED);
		}
	}

	public void sendMessage(String data) throws IOException {
		RemoteEndpoint remote = session.getRemote();
		remote.sendString(data);
	}

	@Override
	public void onWebSocketText(String data) {
		DevToolsDebuggerServer.sendMessageToBrowser(data);
	}
	
	@Override
	public void onWebSocketError(Throwable t) {
		String errorMessage = t.getMessage();
		System.out.println(MessageFormat.format(Messages.DevTools_WEBSOCKET_ERROR, errorMessage));
		BrowserSimLogger.logError(errorMessage, t);
	}

	@Override
	public void onWebSocketBinary(byte[] arg0, int arg1, int arg2) {
	}

}