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
package org.jboss.tools.browsersim.devtools;

import java.io.IOException;

import javafx.application.Platform;
import javafx.util.Callback;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jboss.tools.browsersim.BrowserSimArgs;

import com.sun.javafx.scene.web.Debugger;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Ilya Buziuk (ibuziuk)
 */
public class DevToolsDebuggerServer {
	private static Debugger debugger;
	private static Server server;
	
	public static void startDebugServer(Debugger debugger) throws Exception {
		server = new Server(BrowserSimArgs.debuggerPort);
		
		debugger.setEnabled(true);
    	
    	final ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    	servletHandler.addServlet(DevToolsWebSocketServlet.class, "/devtools/page/dtdb");
    	
    	ResourceHandler resourceHandler = new ResourceHandler();
    	resourceHandler.setDirectoriesListed(true);
    	String devToolsPath = DevToolsDebuggerServer.class.getClassLoader().getResource("inspector-front-end").toExternalForm(); 
    	resourceHandler.setResourceBase(devToolsPath);
    	
    	HandlerList handlerList = new HandlerList();
    	handlerList.setHandlers(new Handler[]{servletHandler, resourceHandler});
    	server.setHandler(handlerList);
        server.start();
        
		DevToolsDebuggerServer.debugger = debugger;
		debugger.setMessageCallback(new Callback<String, Void>() {
			@Override
			public Void call(String data) {
				DevToolsWebSocket mainSocket = (DevToolsWebSocket) servletHandler.getServletContext().getAttribute("org.jboss.tools.browsersim.devtools.DevToolsWebSocket");
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
	            @Override public void run() {
	            	debugger.sendMessage(data);
	            }
		 });
	}
	
	public static String getServerState() {
		return server == null ? null : server.getState();
	}
}