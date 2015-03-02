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
package org.jboss.tools.browsersim;

import java.io.PrintStream;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Ilya Buziuk (ibuziuk)
 */
public class BrowserSimLogger {
	
	private BrowserSimLogger() {
	}
		
	public static void logError(String message, Throwable throwable) {
		if (BrowserSimArgs.standalone) {
			logError(System.err, message, throwable);
		} else {
			logError(System.out, message, throwable);
		}
	}

	private static void logError(PrintStream printStream, String message, Throwable throwable) {
		if (message != null) {
			printStream.println(message);
		}
		if (throwable != null) {
			printStream.println("Stack trace:"); //$NON-NLS-1$
			throwable.printStackTrace(printStream);
		}
	}
}