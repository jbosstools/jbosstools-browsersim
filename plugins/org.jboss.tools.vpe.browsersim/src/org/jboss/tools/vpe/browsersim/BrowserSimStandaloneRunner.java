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
package org.jboss.tools.vpe.browsersim;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;
import org.jboss.tools.vpe.browsersim.ui.CocoaUIEnhancer;
import org.jboss.tools.vpe.browsersim.ui.ExceptionNotifier;
import org.jboss.tools.vpe.browsersim.ui.Messages;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

public class BrowserSimStandaloneRunner {
	public static final String ABOUT_BLANK = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
	
	private static boolean isWebKitAvailable;
	
	static {
		if (PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			CocoaUIEnhancer.initializeMacOSMenuBar(Messages.BrowserSim_BROWSER_SIM);
		}
	}
	
	public static void main(String[] args) {
		Display display = null;
		BrowserSimArgs browserSimArgs = BrowserSimArgs.parseArgs(args);
		
		isWebKitAvailable = BrowserSimUtil.isWebkitAvailable();
		try {
			if (!isWebKitAvailable) {
				String errorMessage = ""; //$NON-NLS-1$
				String os = PlatformUtil.getOs();
				if (PlatformUtil.OS_LINUX.equals(os)) {
					errorMessage = Messages.BrowserSim_NO_WEB_ENGINES_LINUX_STANDALONE;
				} else if(PlatformUtil.OS_WIN32.equals(os)) {
					errorMessage = Messages.BrowserSim_NO_WEB_ENGINES_WINDOWS_STANDALONE;
				}
				throw new SWTError(errorMessage);
			}

			String path = browserSimArgs.getPath();
			String url;
			if (path != null) {
				try {
					new URI(path); // validate URL
					url = path;
				} catch (URISyntaxException e) {
					url = ABOUT_BLANK;
				}
			} else {
				url = ABOUT_BLANK;
			}
	
			Shell parent = null;
			if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
				parent = new Shell(Display.getDefault(), SWT.NO_TRIM);
				parent.setSize(0, 0);
				BrowserSimUtil.setShellAttributes(parent);
				parent.open();
			}

			BrowserSim browserSim = new BrowserSim(url, parent);
			browserSim.open(false, isWebKitAvailable);

			display = Display.getDefault();
			while (!display.isDisposed() && BrowserSim.getInstances().size() > 0) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (SWTError e) {
			ExceptionNotifier.showBrowserSimLoadError(new Shell(Display.getDefault()), e, Messages.BrowserSim_BROWSER_SIM);
		} catch (Throwable t) {
			BrowserSimLogger.logError(t.getMessage(), t);
		} finally {
			if (display != null) {
				display.dispose();
			}
		}
	}
}
