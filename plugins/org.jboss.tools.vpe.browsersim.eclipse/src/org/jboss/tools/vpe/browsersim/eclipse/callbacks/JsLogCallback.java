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
package org.jboss.tools.vpe.browsersim.eclipse.callbacks;

import java.io.IOException;

import org.jboss.tools.vpe.browsersim.eclipse.Activator;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.ExternalProcessCallback;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.TransparentReader;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class JsLogCallback implements ExternalProcessCallback {
	private static final String CONSOLE_NAME = "BrowserSim / CordovaSim console log"; //$NON-NLS-1$
	private static final String JS_CONSOLE_COMMAND = "org.jboss.tools.vpe.browsersim.command.console.log:"; //$NON-NLS-1$
	private static final String JS_CONSOLE_END_SRING = "org.jboss.tools.vpe.browsersim.command.console.log.end"; //$NON-NLS-1$

	@Override
	public String getCallbackId() {
		return JS_CONSOLE_COMMAND;
	}

	@Override
	public void call(String lastString, TransparentReader reader) throws IOException {
		StringBuffer logMessage = new StringBuffer();
		String nextLine;
		while ((nextLine = reader.readLine(false)) != null && !nextLine.startsWith(JS_CONSOLE_END_SRING)) {
			logMessage.append(nextLine);
			logMessage.append(' ');
		}

		Activator.logMessage(logMessage.toString(), CONSOLE_NAME);
	}

}
