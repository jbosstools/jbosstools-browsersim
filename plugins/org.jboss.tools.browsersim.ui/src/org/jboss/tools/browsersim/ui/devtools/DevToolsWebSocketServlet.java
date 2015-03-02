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

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Ilya Buziuk (ibuziuk)
 */
public class DevToolsWebSocketServlet extends WebSocketServlet {

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		return new DevToolsWebSocket(getServletContext());
	}
}