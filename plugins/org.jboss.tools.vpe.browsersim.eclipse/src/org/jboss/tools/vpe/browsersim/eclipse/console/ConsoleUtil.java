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
package org.jboss.tools.vpe.browsersim.eclipse.console;

import java.io.PrintStream;

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
	public static final String BROWSERSIM_CORDOVASIM_CONSOLE = "BrowserSim / CordovaSim console"; //$NON-NLS-1$
	private static final String JS_LOG_PREFIX = "!JavaScript "; //$NON-NLS-1$

	/**
	 * @return the BrowserSim / CordovaSim {@link MessageConsole}
	 */
	public static MessageConsole getConsole() {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (BROWSERSIM_CORDOVASIM_CONSOLE.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole console = new MessageConsole(BROWSERSIM_CORDOVASIM_CONSOLE, null);
		conMan.addConsoles(new IConsole[] { console });
		return console;
	}

	/**
	 * Reveals the Console View and asks it to display a particular console
	 * instance
	 * 
	 * @param console {@link MessageConsole}
	 */
	public static void show(final MessageConsole console) {
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

	public static PrintStream getConsoleOutputStream() {
		MessageConsoleStream stream = getConsole().newMessageStream();
		return new PrintStream(stream);
	}

	/**
	 * Colors the console output depending on the message type
	 * {@link MessageType}
	 * 
	 * @param type
	 *            {@link MessageType}
	 * @param stream
	 *            {@link MessageConsoleStream}
	 */
	public static void setColor(final MessageType type, final MessageConsoleStream stream) {
		final Display display = Display.getDefault();
		if (type != null) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					switch (type) {
					case INFO:
						stream.setColor(display.getSystemColor(SWT.COLOR_BLUE));
						break;
					case WARN:
						stream.setColor(display.getSystemColor(SWT.COLOR_DARK_YELLOW));
						break;
					case ERROR:
						stream.setColor(display.getSystemColor(SWT.COLOR_RED));
						break;
					default:
						break;
					}
				}
			});
		}
	}

	public static MessageType getMessageType(String message) {
		MessageType mt = null;
		if (message != null) {
			if (message.startsWith(MessageType.INFO.toString())) {
				mt = MessageType.INFO;
			} else if (message.startsWith(MessageType.WARN.toString())) {
				mt = MessageType.WARN;
			} else if (message.startsWith(MessageType.ERROR.toString())) {
				mt = MessageType.ERROR;
			}
		}

		return mt;
	}

	public static String addJsLogPrefix(String message) {
		return JS_LOG_PREFIX + " " + message; //$NON-NLS-1$
	}

	public static synchronized  void logException(Throwable e) {
		logException(e, false);
	}

	private static synchronized  void logException(Throwable e, boolean hasCause) {
		StringBuilder builder = new StringBuilder();
		if (hasCause) {
			builder.append("Caused by: "); //$NON-NLS-1$
		}

		builder.append(e.getClass().getName() + ": " + e.getMessage() + "\n"); //$NON-NLS-1$//$NON-NLS-2$
		StackTraceElement[] traces = e.getStackTrace();
		for (int i = 0; i < traces.length; i++) {
			builder.append("\t" + traces[i] + "\n"); //$NON-NLS-1$//$NON-NLS-2$
		}

		final String log = builder.toString();

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageConsoleStream messageStream = getConsole().newMessageStream();
				messageStream.print(log);
			}
		});

		Throwable cause = e.getCause();
		if (cause != null) {
			logException(e.getCause(), true);
		}
	}

}