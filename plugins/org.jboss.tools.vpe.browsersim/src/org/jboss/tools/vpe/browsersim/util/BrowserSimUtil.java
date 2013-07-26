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
package org.jboss.tools.vpe.browsersim.util;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.jboss.tools.vpe.browsersim.BrowserSimLogger;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.SkinMap;
import org.jboss.tools.vpe.browsersim.ui.MessageBoxWithLinks;
import org.jboss.tools.vpe.browsersim.ui.Messages;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */

public class BrowserSimUtil {
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
}
