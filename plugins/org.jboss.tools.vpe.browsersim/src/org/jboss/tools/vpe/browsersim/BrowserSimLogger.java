/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim;

import java.io.PrintStream;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSimLogger {
	private static final String LOG_COMMAND = "org.jboss.tools.vpe.browsersim.command.log:"; //$NON-NLS-1$
	private static final String LOG_END_SRING = "org.jboss.tools.vpe.browsersim.command.log.end"; //$NON-NLS-1$
	
	public static void logError(String message, Throwable throwable) {
		logError(message, throwable, BrowserSimRunner.PLUGIN_ID);
	}
	
	public static void logError(String message, Throwable throwable, String pluginId) {
		if (BrowserSimArgs.standalone) {
			logError(System.err, message, throwable, pluginId);
		} else {
			System.out.print(LOG_COMMAND);
			logError(System.out, message, throwable, pluginId);
			System.out.println(LOG_END_SRING);
		}
	}

	private static void logError(PrintStream printStream, String message,
			Throwable throwable, String pluginId) {
		printStream.println(pluginId);
		if (message != null) {
			printStream.println(message);
		}
		if (throwable != null) {
			printStream.println("Stack trace:");
			throwable.printStackTrace(printStream);
		}
	}
}
