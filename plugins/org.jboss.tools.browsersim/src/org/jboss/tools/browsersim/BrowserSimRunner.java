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
package org.jboss.tools.browsersim;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;

import org.eclipse.jetty.server.Server;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.browsersim.browser.PlatformUtil;
import org.jboss.tools.browsersim.browser.javafx.JavaFXBrowser;
import org.jboss.tools.browsersim.devtools.DevToolsDebuggerServer;
import org.jboss.tools.browsersim.ui.BrowserSim;
import org.jboss.tools.browsersim.ui.CocoaUIEnhancer;
import org.jboss.tools.browsersim.ui.ExceptionNotifier;
import org.jboss.tools.browsersim.ui.Messages;
import org.jboss.tools.browsersim.util.BrowserSimUtil;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */

public class BrowserSimRunner {
	public static final String ABOUT_BLANK = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
	
	private static final String STANDALONE_MOCK_JAR = "javafx-mock.jar"; //$NON-NLS-1$
	
	private static boolean isJavaFxAvailable;
	private static boolean isWebKitAvailable;
	
	private static Path tempDir;
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
			
			if (!isJavaFxAvailable && BrowserSimArgs.standalone) {
				tempDir = Files.createTempDirectory("browsersim"); //$NON-NLS-1$
			    BrowserSimUtil.loadMock(tempDir.toString(), STANDALONE_MOCK_JAR);
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
			
			// do a cleanup
			if (BrowserSimArgs.standalone && tempDir != null) {
			    try {
			    	Files.walkFileTree(tempDir, new SimpleFileVisitor<Path>() {
			    		   @Override
			    		   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			    			   Files.delete(file);
			    			   return FileVisitResult.CONTINUE;
			    		   }

			    		   @Override
			    		   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			    			   Files.delete(dir);
			    			   return FileVisitResult.CONTINUE;
			    		   }

			    	   });
				} catch (IOException e) {
					BrowserSimLogger.logError(e.getMessage(), e);
				}
			}
		}
	}
}
