/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
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

import javax.servlet.ServletContext;

import org.eclipse.jetty.websocket.WebSocket;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Ilya Buziuk (ibuziuk)
 */
public class DevToolsWebSocket implements WebSocket.OnTextMessage {

	private Connection connection;
	private ServletContext context;

	public DevToolsWebSocket(ServletContext context) {
		this.context = context;
	}
	
	@Override
	public void onOpen(Connection connection) {
		this.connection = connection;
		if (context.getAttribute("org.jboss.tools.browsersim.ui.devtools.DevToolsWebSocket") != null) {
			connection.close();
			System.out.println("Another client is already connected. Connection refused.");
		} else {
			context.setAttribute("org.jboss.tools.browsersim.ui.devtools.DevToolsWebSocket", this);
			System.out.println("Client connected.");
		}
	}

	@Override
	public void onClose(int closeCode, String message) {
		DevToolsWebSocket mainSocket = (DevToolsWebSocket) context.getAttribute("org.jboss.tools.browsersim.ui.devtools.DevToolsWebSocket");
		if (mainSocket == this) {
			context.removeAttribute("org.jboss.tools.browsersim.ui.devtools.DevToolsWebSocket");
			System.out.println("Client disconnected.");
		}
	}

	@Override
	public void onMessage(String data) {
		DevToolsDebuggerServer.sendMessageToBrowser(data);
	}

	public void sendMessage(String data) throws IOException  {
		connection.sendMessage(data);
	}
}