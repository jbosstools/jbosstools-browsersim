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
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ExceptionNotifier {
	/**
	 * Should be used to notify user about WebKit-loading errors
	 */
	public static void showBrowserSimLoadError(Shell parentShell, SWTError error, String appName) {
		String os = PlatformUtil.getOs();
		String arch = PlatformUtil.getArch();
		String message;
		if (PlatformUtil.OS_WIN32.equals(os) && !PlatformUtil.ARCH_X86.equals(arch)) {// Eclipse 64-bit on Windows
			message = Messages.ExceptionNotifier_ONLY_32_BIT_ECLIPSE_IS_SUPPORTED_ON_WINDOWS;
		} else if (PlatformUtil.OS_WIN32.equals(os) && PlatformUtil.ARCH_X86.equals(arch) // Eclipse 32-bit on Windows and
				&& error.getMessage() != null											  // Safari is not installed
				&& error.getMessage().contains("Safari must be installed to use a SWT.WEBKIT-style IBrowser")) { //$NON-NLS-1$
			message = MessageFormat.format(Messages.ExceptionNotifier_APPLE_APPLICATION_SUPPORT_IS_NOT_FOUND, appName);
		} else if (PlatformUtil.OS_WIN32.equals(os) && PlatformUtil.ARCH_X86.equals(arch) // Eclipse 32-bit on Windows and
				&& error.getMessage() != null											  // Apple Application Support is not installed and
				&& error.getMessage().contains("No more handles [Failed to load the swt-webkit library]") // Safari libs cannot be loaded and //$NON-NLS-1$
				&& "1.7".equals(System.getProperty("java.specification.version"))) { //Java version is 1.7 //$NON-NLS-1$ //$NON-NLS-2$
			// see JBIDE-12467: Unable to Run Browsersim in Windows7 64b + JDK7 32b
			message = Messages.ExceptionNotifier_JRE_1_7_IS_NOT_SUPPORTED_ON_WINDOWS;
		} else if (PlatformUtil.OS_LINUX.equals(os) && error.getMessage() != null) {    // Linux - probably WebKitGTK is not installed
			message = MessageFormat.format(Messages.ExceptionNotifier_BROWSERSIM_IS_FAILED_TO_START_ON_LINUX, error.getMessage());
		} else {																	  // everything else
			message = MessageFormat.format(Messages.ExceptionNotifier_BROWSERSIM_IS_FAILED_TO_START, error.getMessage());
		}
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



