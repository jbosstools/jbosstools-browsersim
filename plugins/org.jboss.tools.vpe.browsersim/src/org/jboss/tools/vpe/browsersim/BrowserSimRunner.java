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
import java.text.MessageFormat;

import org.eclipse.jetty.server.Server;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.browser.javafx.JavaFXBrowser;
import org.jboss.tools.vpe.browsersim.devtools.DevToolsDebuggerServer;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;
import org.jboss.tools.vpe.browsersim.ui.CocoaUIEnhancer;
import org.jboss.tools.vpe.browsersim.ui.ExceptionNotifier;
import org.jboss.tools.vpe.browsersim.ui.Messages;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */

public class BrowserSimRunner {
	public static final String PLUGIN_ID = "org.jboss.tools.vpe.browsersim"; //$NON-NLS-1$
		
	public static final String NOT_STANDALONE = "-not-standalone"; //$NON-NLS-1$
	public static final String ABOUT_BLANK = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
	
	private static boolean isJavaFxAvailable;
	private static boolean isWebKitAvailable;
	static {
		if (PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			CocoaUIEnhancer.initializeMacOSMenuBar(Messages.BrowserSim_BROWSER_SIM);
		}
	}
	
	static { 
		String platform = PlatformUtil.getOs();
		isJavaFxAvailable = false;
		
		boolean isLinux = PlatformUtil.OS_LINUX.equals(platform);

		// Trying to load javaFx libs except Linux GTK3 case
		if (!(isLinux && !BrowserSimUtil.isRunningAgainstGTK2())) {
			isJavaFxAvailable = BrowserSimUtil.loadJavaFX();
		}
		
		isWebKitAvailable = BrowserSimUtil.isWebkitAvailable();
	}
	
	public static void main(String[] args) {
		Display display = null;
		try {
			if (!isJavaFxAvailable && !isWebKitAvailable) {
				String errorMessage = ""; //$NON-NLS-1$
				String os = PlatformUtil.getOs();
				if (PlatformUtil.OS_LINUX.equals(os)) {
					errorMessage = MessageFormat.format(
					        BrowserSimUtil.isGTK3() ? Messages.BrowserSim_NO_WEB_ENGINES_LINUX_GTK3 : Messages.BrowserSim_NO_WEB_ENGINES_LINUX,
							Messages.BrowserSim_BROWSER_SIM);
				} else if(PlatformUtil.OS_WIN32.equals(os)) {
					errorMessage = MessageFormat.format(
							Messages.BrowserSim_NO_WEB_ENGINES_WINDOWS,
							Messages.BrowserSim_BROWSER_SIM);
				}
				throw new SWTError(errorMessage);
			}
			
			BrowserSimArgs browserSimArgs = BrowserSimArgs.parseArgs(args);
			
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
			browserSim.open(isJavaFxAvailable, isWebKitAvailable);
			
            if (browserSim.getBrowser() instanceof JavaFXBrowser&& !Server.STARTED.equals(DevToolsDebuggerServer.getServerState())) {
                DevToolsDebuggerServer.startDebugServer(((JavaFXBrowser)browserSim.getBrowser()).getDebugger());
            }

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
