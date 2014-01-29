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
package org.jboss.tools.vpe.browsersim.js.log;

import java.io.PrintStream;

import org.jboss.tools.vpe.browsersim.BrowserSimArgs;
import org.jboss.tools.vpe.browsersim.BrowserSimRunner;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class JsConsoleLogger {
	private static final String JS_CONSOLE_COMMAND = "org.jboss.tools.vpe.browsersim.command.console.log:"; //$NON-NLS-1$
	private static final String JS_CONSOLE_END_SRING = "org.jboss.tools.vpe.browsersim.command.console.log.end"; //$NON-NLS-1$

	private JsConsoleLogger() {
	}

	public static void log(String message) {
		log(message, BrowserSimRunner.PLUGIN_ID);
	}

	private static void log(String message, String pluginId) {
		if (BrowserSimArgs.standalone) {
			log(System.out, message, pluginId);
		} else {
			System.out.print(JS_CONSOLE_COMMAND);
			log(System.out, message, pluginId);
			System.out.println(JS_CONSOLE_END_SRING);
		}
	}

	private static void log(PrintStream printStream, String message, String pluginId) {
		printStream.println(pluginId);
		if (message != null) {
			printStream.println(message);
		}
	}

}
