/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.internal.Library;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.jboss.tools.browsersim.browser.IBrowser;
import org.jboss.tools.browsersim.browser.PlatformUtil;
import org.jboss.tools.browsersim.browser.javafx.JavaFXBrowser;
import org.jboss.tools.browsersim.ui.BrowserSimLogger;
import org.jboss.tools.browsersim.ui.MessageBoxWithLinks;
import org.jboss.tools.browsersim.ui.MessageBoxWithLinksForDebugger;
import org.jboss.tools.browsersim.ui.Messages;
import org.jboss.tools.browsersim.ui.model.Device;
import org.jboss.tools.browsersim.ui.model.SkinMap;
import org.jboss.tools.browsersim.ui.skin.BrowserSimSkin;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
@SuppressWarnings("restriction")
public class BrowserSimUtil {
	private static final String java7u51 = "1.7.0_51"; //$NON-NLS-1$
	private static final String java8 = "1.8.0"; //$NON-NLS-1$
	private static final String[] BROWSERSIM_ICONS = {"icons/browsersim_16px.png", "icons/browsersim_32px.png", "icons/browsersim_64px.png", "icons/browsersim_128px.png", "icons/browsersim_256px.png", }; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
	private static final String SWT_GTK3 = "SWT_GTK3"; //$NON-NLS-1$
	private static final String DISABLED = "0"; //$NON-NLS-1$

	private static void fixShellLocation(Shell shell) {
		Rectangle allClientArea = shell.getMonitor().getClientArea();
		
		Point shellLocation = shell.getLocation();
		Point shellSize = shell.getSize();
		int bottomOverlap = shellLocation.y + shellSize.y - (allClientArea.y + allClientArea.height);
		if (bottomOverlap > 0) {
			if (shellLocation.y > bottomOverlap) {
				shellLocation.y -= bottomOverlap;
			} else {
				shellLocation.y = allClientArea.y;
			}
		}

		int rightOverlap = shellLocation.x + shellSize.x - (allClientArea.x + allClientArea.width);
		if (rightOverlap > 0) {
			if (shellLocation.x > rightOverlap) {
				shellLocation.x -= rightOverlap;
			} else {
				shellLocation.x = allClientArea.x;
			}
		}

		int leftOverlap = shellLocation.x - allClientArea.x;
		if (leftOverlap < 0) {
			if (shellLocation.x < leftOverlap) {
				shellLocation.x -= leftOverlap;
			} else {
				shellLocation.x = allClientArea.x;
			}
		}
		
		int topOverlap = shellLocation.y - allClientArea.y;
		if (topOverlap < 0) {
			if (shellLocation.y < topOverlap) {
				shellLocation.y -= topOverlap;
			} else {
				shellLocation.y = allClientArea.y;
			}
		}
		
		shell.setLocation(shellLocation);
	}
	
	public static Rectangle getMonitorClientArea(Shell shell) {
		Rectangle clientArea = shell.getMonitor().getClientArea();

		/* on Linux returned monitor client area may be bigger
		 * than the monitor bounds when multiple monitors are used.
		 * The following code fixes this */
		Rectangle bounds = shell.getMonitor().getBounds();
		for(Monitor monitor : Display.getDefault().getMonitors()) {
			if(monitor.getBounds().intersects(shell.getBounds())) {
				bounds = monitor.getBounds();
			}
		}
		clientArea.width = Math.min(clientArea.width, bounds.width);
		clientArea.height = Math.min(clientArea.height, bounds.height);

		return clientArea;
	}
	
	/**
	 * See JBIDE-11896	BrowserSim: pixel ratio problem.
	 * 
	 * On many mobile devices like iPhone 4 1 CSS pixel = 2 device pixels.
	 */
	public static Point getSizeInDesktopPixels(Device device) {
		double pixelRatio = device.getPixelRatio();
		if (device.getPixelRatio() == 0.0) {
			pixelRatio = 1.0;
			RuntimeException e = new RuntimeException(Messages.BrowserSim_ZERO_PIXEL_RATIO);
			BrowserSimLogger.logError(e.getMessage(), e);
		}
		int width = (int) Math.round(device.getWidth() / pixelRatio);
		int height = (int) Math.round(device.getHeight() / pixelRatio);
		return new Point(width, height);
	}

	public static Class<? extends BrowserSimSkin> getSkinClass(Device device, boolean useSkins) {
		return SkinMap.getInstance().getSkinClass(useSkins ? device.getSkinId() : null);
	}
	

	public static void showAboutDialog(Shell shell, String message, Image icon) {
		new MessageBoxWithLinks(shell, message, icon, Messages.BrowserSim_ABOUT_HEADER).open();
	}
	
	public static void showDevToolsDialog(Shell shell, String message, String url, Image icon) {
		new MessageBoxWithLinksForDebugger(shell, message, url, icon, Messages.BrowserSim_DEV_TOOLS_HEADER).open();
	}
	
	public static void addDisposeListener(Widget widget, final Resource disposable) {
		widget.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				disposable.dispose();
			}
		});
	}
	
	public static Shell getParentShell(BrowserSimSkin skin) {
		return PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs()) ? null : skin.getShell().getParent().getShell();
	}
	
	/**
	 * Sets the location of the shell.
	 * 
	 * @param shell - shell.
	 * @param shellSize - size of the shell. Must not be <code>null</code>.
	 * @param location - location of shell's left top corner. If location is <code>null</code>,
	 * shell location will stay as it is(sets automatically by SWT). 
	 */
	public static void setShellLocation(Shell shell, Point shellSize, Point location) {
		if (location != null && shellSize != null) {
			Rectangle browserSimArea = new Rectangle(location.x, location.y, shellSize.x, shellSize.y);
			if (shell.getDisplay().getClientArea().intersects(browserSimArea)) {
				shell.setLocation(location);
			}
			fixShellLocation(shell);
		}
	}
	
	public static void setCustomScrollbarStylesForWindows(IBrowser browser) {
		if (PlatformUtil.OS_WIN32.equals(PlatformUtil.getOs()) && !(browser instanceof JavaFXBrowser)) {
			browser.addLocationListener(new LocationAdapter() {
				@SuppressWarnings("nls")
				@Override
				public void changed(LocationEvent event) {
					IBrowser browser = (IBrowser) event.widget;
					if (browser != null) {
						browser.execute(
							"if (window._browserSim_customScrollBarStylesSetter === undefined) {"
								+"window._browserSim_customScrollBarStylesSetter = function () {"
								+	"document.removeEventListener('DOMSubtreeModified', window._browserSim_customScrollBarStylesSetter, false);"
								+	"var head = document.head;"
								+	"var style = document.createElement('style');"
								+	"style.type = 'text/css';"
								+	"style.id='browserSimStyles';"
								+	"head.appendChild(style);"
								+	"style.innerText='"
								// The following two rules fix a problem with showing scrollbars in Google Mail and similar,
								// but autohiding of navigation bar stops to work with it. That is why they are commented.
								//+	"html {"
								//+		"overflow: hidden;"
								//+	"}"
								//+	"body {"
								//+		"position: absolute;"
								//+		"top: 0px;"
								//+		"left: 0px;"
								//+		"bottom: 0px;"
								//+		"right: 0px;"
								//+		"margin: 0px;"
								//+		"overflow-y: auto;"
								//+		"overflow-x: auto;"
								//+	"}"
								+		"::-webkit-scrollbar {"
								+			"width: 5px;"
								+			"height: 5px;"
								+		"}"
								+		"::-webkit-scrollbar-thumb {"
								+			"background: rgba(0,0,0,0.4); "
								+		"}"
								+		"::-webkit-scrollbar-corner, ::-webkit-scrollbar-thumb:window-inactive {"
								+			"background: rgba(0,0,0,0.0);"
								+		"};"
								+	"';"
								+"};"
								+ "document.addEventListener('DOMSubtreeModified', window._browserSim_customScrollBarStylesSetter, false);"
							+ "}"
						);
					}
				}
			});
		};
	}
	
	/**Checks if current JVM has JavaFX libraries and loads it into runtime.
	 * 
	 * @return true if JavaFX is available for current JVM
	 */
	public static boolean loadJavaFX() {
		String javaHome = System.getProperty("java.home"); //$NON-NLS-1$
		return loadJavaFX9(javaHome) || loadJavaFX8(javaHome) || loadJavaFX7(javaHome);
	}
	
	private static boolean loadJavaFX7(String javaHome) {
		File jfxrt7 = new File(javaHome + File.separator + "lib" + File.separator + "jfxrt.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		if (jfxrt7.exists()) {
			loadJar(jfxrt7);
			return true;
		}
		return false;
	}
	
	private static boolean loadJavaFX8(String javaHome) {
		File jfxrt8 = new File(javaHome + File.separator + "lib" + File.separator + "ext" + File.separator + "jfxrt.jar"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		File jfxswt8 = new File(javaHome + File.separator + "lib" + File.separator + "jfxswt.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		if (jfxrt8.exists() && jfxswt8.exists()) {
			loadJar(jfxrt8);
			loadJar(jfxswt8);
			return true;
		}
		return false;
	}
	
	private static boolean loadJavaFX9(String javaHome) {
		File jfxrt9 = new File(javaHome + File.separator + "lib" + File.separator + "jfxrt.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		File jfxswt9 = new File(javaHome + File.separator + "lib" + File.separator + "jfxswt.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		if (jfxrt9.exists() && jfxswt9.exists()) {
			loadJar(jfxrt9);
			loadJar(jfxswt9);
			return true;
		}
		return false;
	}
	
	/**Extracts jar file with mocks to temporal folder and load it to classpath.
	 * This method should be used only for Standalone BrowserSim.
	 * 
	 * @param tempDir - directory to extract jar file
	 * @param jarName - path to jar file
	 */
	public static void loadMock(String tempDir, String jarName) {
	    InputStream inputStream = null;
	    try {
			inputStream = BrowserSimUtil.class.getClassLoader().getResourceAsStream(jarName);
			Path tmpFile = Paths.get(tempDir, jarName);
			Files.copy(inputStream, tmpFile);
			loadJar(tmpFile.toFile());          
	    } catch (FileNotFoundException e) {
            BrowserSimLogger.logError(e.getMessage(), e);
        } catch (IOException e) {
            BrowserSimLogger.logError(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    BrowserSimLogger.logError(e.getMessage(), e);
                }
            }
        }
        
    }
	
	public static boolean isJavaFxAvailable() {
		String javaHome = System.getProperty("java.home"); //$NON-NLS-1$
		return isJavaFxAvailable(javaHome);
	}
	
	public static boolean isJavaFxAvailable(String javaHome) {
		File jfxrt7 = new File(javaHome + File.separator
				+ "lib" + File.separator + "jfxrt.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		File jfxrt8 = new File(javaHome + File.separator
				+ "lib" + File.separator + "ext" + File.separator + "jfxrt.jar"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		File jfxswt8 = new File(javaHome + File.separator
				+ "lib" + File.separator + "jfxswt.jar"); //$NON-NLS-1$ //$NON-NLS-2$
	
		return jfxrt7.exists() || (jfxrt8.exists() && jfxswt8.exists());
	}
	
	public static boolean isJavaFx7Available() {
		String javaHome = System.getProperty("java.home"); //$NON-NLS-1$
		return new File(javaHome + File.separator + "lib" + File.separator + "jfxrt.jar").exists(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public static boolean isJavaFx8Available() {
		String javaHome = System.getProperty("java.home"); //$NON-NLS-1$
		File jfxrt8 = new File(javaHome + File.separator
				+ "lib" + File.separator + "ext" + File.separator + "jfxrt.jar"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		File jfxswt8 = new File(javaHome + File.separator
				+ "lib" + File.separator + "jfxswt.jar"); //$NON-NLS-1$ //$NON-NLS-2$
	
		return jfxrt8.exists() && jfxswt8.exists();
	}
	
	private static void loadJar(File file) {
		try {
			URL u = file.toURI().toURL();

			URLClassLoader sysloader = (URLClassLoader) ClassLoader
					.getSystemClassLoader();
			@SuppressWarnings("rawtypes")
			Class sysclass = URLClassLoader.class;
			@SuppressWarnings("unchecked")
			Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class }); //$NON-NLS-1$
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
		} catch (Throwable t) {
			BrowserSimLogger.logError("Unable to add " + file.getName() + " to classpath.", t); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * We need to do this to make switcher work properly.
	 * We need to load both SWT.WEBKIT and JavaFx WebKit to make switcher working properly.
	 * 
	 * After Java7u51 loading SWT.WEBKIT before JavaFX webkit causes an error.
	 * For Java7u45 and earlier loading JavaFX Webkit before SWT Webkit causes a SWT Error.
	 * 
	 * @see JBIDE-16405
	 * @see https://javafx-jira.kenai.com/browse/RT-35480
	 */
	@SuppressWarnings("unused")
	public static void loadWebkitLibraries() {
		Shell tempShell = new Shell();
		String javaVersion = System.getProperty("java.version"); //$NON-NLS-1$
		if (java7u51.compareTo(javaVersion) > 0) {
			Browser tempSWTBrowser = new Browser(tempShell, SWT.WEBKIT);
			JavaFXBrowser tempJavaFXBrowser = new JavaFXBrowser(tempShell);
			tempSWTBrowser.dispose();
		} else {
			JavaFXBrowser tempJavaFXBrowser = new JavaFXBrowser(tempShell);
			Browser tempSWTBrowser = new Browser(tempShell, SWT.WEBKIT);
			tempSWTBrowser.dispose();
		}
	}
	
	/**
	 * Setting SWT_GTK3 option is relevant only on Linux
	 * @return <code>true</code> if running against GTK 2 libs
	 * @see JBIDE-16732
	 */
	public static boolean isRunningAgainstGTK2() { 
		Map<String, String> env = System.getenv();
		String gtk3 = env.get(SWT_GTK3);
		if (gtk3 != null && gtk3.equals(DISABLED)) {
			return true;
		}

		return false;
	}
	
	private static Image[] initImages(Shell shell) {
		BrowserSimImageList imageList = new BrowserSimImageList(shell);
		Image[] icons = new Image[BROWSERSIM_ICONS.length];
		for (int i = 0; i < BROWSERSIM_ICONS.length; i++) {
			icons[i] = imageList.getImage(BROWSERSIM_ICONS[i]);
		}
		
		return icons;
	}

	public static void setShellAttributes(Shell shell) {
		Image[] icons = initImages(shell);
		shell.setImages(icons);
		shell.setText(Messages.BrowserSim_BROWSER_SIM);
	}
	
	public static boolean isWebkitAvailable() {
		String os = PlatformUtil.getOs();
		if (PlatformUtil.OS_WIN32.equals(os)) {
			return isWindowsSwtWebkitInstalled();
		} else if (PlatformUtil.OS_MACOSX.equals(os)){
			return true;
		} else if (PlatformUtil.OS_LINUX.equals(os)) {
			return isLinuxWebkitInstalled();
		}
		return false;
	}
	
	private static boolean isWindowsSwtWebkitInstalled() {
		/**
		 * Workaround for Java7u51 and higher. Loading SWT.WEBKIT libraries before JavaFX webkit causes an error. 
		 * @see https://javafx-jira.kenai.com/browse/RT-35480
		 */
		String javaVersion = System.getProperty("java.version"); //$NON-NLS-1$
		
		//if 7u51 <= java version < 8 load javafx first
		if (0 >= java7u51.compareTo(javaVersion) && java8.compareTo(javaVersion) >0) {
			@SuppressWarnings("unused")
			JavaFXBrowser tempJavaFXBrowser = new JavaFXBrowser(new Shell());
		}
		
		if (PlatformUtil.ARCH_X64.equals(PlatformUtil.getArch())) {
			//64Bit Safari does not exists
			return false;
		}
		//due to last changes Safari is needed to run BrowerSim (against QuickTime)
		//to avoid JVM crash we need to check Safari existnce before creating a browser.(JBIDE-13044).
		//If an exception is thrown during org.eclipse.swt.browser.WebKit.readInstallDir() invocation,
		//this means that SWT internal API is changed and we just log it to the console.
		try {
			Method method = Class.forName("org.eclipse.swt.browser.WebKit").getDeclaredMethod("readInstallDir", String.class); //$NON-NLS-1$ //$NON-NLS-2$
			method.setAccessible(true);
			String AASDirectory = (String) method.invoke(null, "SOFTWARE\\Apple Computer, Inc.\\Safari");//$NON-NLS-1$
			
			if (AASDirectory != null) {
				AASDirectory += "\\Apple Application Support"; //$NON-NLS-1$
				if (new File(AASDirectory).exists()) {
					return true;
				}
			}
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean isLinuxWebkitInstalled() {
		try {
			Library.loadLibrary("swt-webkit"); //$NON-NLS-1$

			String webkit2 = System.getenv("SWT_WEBKIT2"); //$NON-NLS-1$
			
			boolean WEBKIT2 = webkit2 != null && webkit2.equals("1") && isGTK3(); //$NON-NLS-1$
			// TODO webkit_check_version() should take care of the following,
			// but for some
			// reason this symbol is missing from the latest build. If it is
			// present in
			// Linux distro-provided builds then replace the following with this
			// call.
			int major, minor, micro;
			if (WEBKIT2) {
				major = LinuxUtil.webkit_get_major_version();
				minor = LinuxUtil.webkit_get_minor_version();
				micro = LinuxUtil.webkit_get_micro_version();
			} else {
				major = LinuxUtil.webkit_major_version();
				minor = LinuxUtil.webkit_minor_version();
				micro = LinuxUtil.webkit_micro_version();
			}
			final int[] MIN_VERSION = {1, 2, 0};
			
			return major > MIN_VERSION[0] ||
			(major == MIN_VERSION[0] && minor > MIN_VERSION[1]) ||
			(major == MIN_VERSION[0] && minor == MIN_VERSION[1] && micro >= MIN_VERSION[2]);
		} catch (Throwable e) {
		    BrowserSimLogger.logError(e.getMessage(), e);
            return false;
		}
	}
	
	/**
	 * Detects GTK3.
	 * 
	 * @return <code>true</code> if browsersim is runned under GTK3, <code>false</code> if browsersim runned under GTK2 and if OS is not Linux 
	 */
	public static boolean isGTK3() {
        if (PlatformUtil.OS_LINUX.equals(PlatformUtil.getOs())) {            
            try {
                int GTK_VERSION = LinuxUtil.VERSION(LinuxUtil.gtk_major_version(), LinuxUtil.gtk_minor_version(), LinuxUtil.gtk_micro_version());
                return GTK_VERSION >= LinuxUtil.VERSION(3, 0, 0);
            } catch (Exception e) {
                BrowserSimLogger.logError(e.getMessage(), e);
                return false;
            }
        } else {
            return false;
        }
	}
}
