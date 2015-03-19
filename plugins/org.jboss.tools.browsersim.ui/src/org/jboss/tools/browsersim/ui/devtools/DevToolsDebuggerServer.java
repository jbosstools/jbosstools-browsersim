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
import javax.servlet.ServletContext;
import javafx.application.Platform;
import javafx.util.Callback;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.tools.browsersim.ui.launch.BrowserSimArgs;

import com.sun.javafx.scene.web.Debugger;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Ilya Buziuk (ibuziuk)
 */
public class DevToolsDebuggerServer {
	private static ServletContextHandler contextHandler;
	private static Debugger debugger;
	private static Server server;

	public static void startDebugServer(Debugger debugger) throws Exception {
		server = new Server(BrowserSimArgs.debuggerPort);
		debugger.setEnabled(true);

		// Basic application context (Handler Tree)
		contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextHandler.setContextPath("/"); //$NON-NLS-1$

		ServletHolder devToolsHolder = new ServletHolder(new DevToolsWebSocketServlet());
		contextHandler.addServlet(devToolsHolder, "/devtools/page/dtdb"); //$NON-NLS-1$

		String devToolsPath = DevToolsDebuggerServer.class.getClassLoader().getResource("inspector-front-end").toExternalForm(); //$NON-NLS-1$

		ServletHolder devToolsHome = new ServletHolder("devTools-home", DefaultServlet.class); //$NON-NLS-1$
		devToolsHome.setInitParameter("resourceBase", devToolsPath); //$NON-NLS-1$
		devToolsHome.setInitParameter("dirAllowed", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		contextHandler.addServlet(devToolsHome, "/"); //$NON-NLS-1$

		server.setHandler(contextHandler);
		server.start();

		DevToolsDebuggerServer.debugger = debugger;
		debugger.setMessageCallback(new Callback<String, Void>() {
			@Override
			public Void call(String data) {
				DevToolsWebSocket mainSocket = (DevToolsWebSocket) contextHandler.getServletContext().getAttribute(DevToolsWebSocket.WEB_SOCKET_ATTR_NAME);
				if (mainSocket != null) {
					try {
						mainSocket.sendMessage(data);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		});
	}

	public static void stopDebugServer() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
		}
	}

	public static void sendMessageToBrowser(final String data) {
		Platform.runLater(new Runnable() {// Display.asyncExec won't be successful here
			@Override
			public void run() {
				debugger.sendMessage(data);
			}
		});
	}

	public static String getServerState() {
		return server == null ? null : server.getState();
	}

	public static ServletContext getServletContext() {
		return (contextHandler != null) ? contextHandler.getServletContext() : null;
	}

}