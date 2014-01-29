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
package org.jboss.tools.vpe.browsersim.eclipse.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.jboss.tools.vpe.browsersim.BrowserSimLogger;
import org.jboss.tools.vpe.browsersim.js.log.MessageType;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class ConsoleUtil {
	
	/**
	 * @param  name console name
	 * @return the {@link MessageConsole} with a given name or creates a new one if it cannot be found
	 */
	public static MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}
	
	
	/**
	 * Reveals the Console View and asks it to display a particular console instance
	 * 
	 * @param  the {@link MessageConsole} instance
	 */
	public static void show (final MessageConsole console) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				String id = IConsoleConstants.ID_CONSOLE_VIEW;
				IConsoleView view = null;
				try {
					view = (IConsoleView) page.showView(id);
				} catch (PartInitException e) {
					BrowserSimLogger.logError(e.getMessage(), e);
				}
				if (view != null) {
					view.display(console);
				}
			}
		});
	}
	
	/**
	 * Colors the console output depending on the message type {@link MessageType}
	 * 
	 * @param  out {@link MessageConsoleStream} instance
	 * @param  message {@link String} message
	 */
	public static void setConsoleColor(final MessageConsoleStream out, final String message) {
		final Display display = Display.getDefault();
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if (message != null) {
					if (message.startsWith(MessageType.INFO.toString())) {
						out.setColor(display.getSystemColor(SWT.COLOR_BLUE)); 
					} else if (message.startsWith(MessageType.WARN.toString())) {
						out.setColor(display.getSystemColor(SWT.COLOR_DARK_YELLOW)); 
					} else if (message.startsWith(MessageType.ERROR.toString())) {
						out.setColor(display.getSystemColor(SWT.COLOR_RED)); 
					}
				}	
			}
		});
	}
	
}
