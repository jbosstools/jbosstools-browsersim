/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.ui;

import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.BrowserSimLogger;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ExceptionNotifier {
	/**
	 * Should be used to notify user about WebKit-loading errors
	 */
	public static void showBrowserSimLoadError(Shell parentShell, SWTError error, String appName) {
		String message;
		message = MessageFormat.format(Messages.ExceptionNotifier_BROWSERSIM_IS_FAILED_TO_START, error.getMessage());
		showErrorMessageWithLinks(parentShell, message, error);
	}

	public static void showErrorMessage(Shell shell, String message) {
		BrowserSimLogger.logError(message, null);

		MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
		messageBox.setText(Messages.BrowserSim_ERROR);
		messageBox.setMessage(message);
		messageBox.open();
	}

	private static void showErrorMessageWithLinks(Shell shell, String message, Throwable throwable) {
		BrowserSimLogger.logError(message, throwable);

		MessageBoxWithLinks messageBox = new MessageBoxWithLinks(shell,
				message, shell.getDisplay().getSystemImage(SWT.ICON_ERROR),
				Messages.BrowserSim_ERROR);
		messageBox.open();
	}
}



