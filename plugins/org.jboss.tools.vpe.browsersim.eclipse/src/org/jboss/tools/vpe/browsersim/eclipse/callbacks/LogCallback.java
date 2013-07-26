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
package org.jboss.tools.vpe.browsersim.eclipse.callbacks;

import java.io.IOException;

import org.jboss.tools.vpe.browsersim.eclipse.Activator;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.ExternalProcessCallback;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.TransparentReader;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class LogCallback implements ExternalProcessCallback {
	private static final String LOG_COMMAND = "org.jboss.tools.vpe.browsersim.command.log:"; //$NON-NLS-1$
	private static final String LOG_END_SRING = "org.jboss.tools.vpe.browsersim.command.log.end"; //$NON-NLS-1$

	@Override
	public String getCallbackId() {
		return LOG_COMMAND;
	}

	@Override
	public void call(final String lastString, TransparentReader reader) throws IOException {
		StringBuffer logMessage = new StringBuffer();
		String pluginId = lastString.substring(LOG_COMMAND.length());
		String nextLine;
		while ((nextLine = reader.readLine(false)) != null && !nextLine.startsWith(LOG_END_SRING)) {
			logMessage.append(nextLine);
			logMessage.append('\n');
		}
		
		Activator.logError(logMessage.toString(), null, pluginId);
	}

}
