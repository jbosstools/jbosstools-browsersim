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
package org.jboss.tools.vpe.browsersim.ui.skin;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.model.DeviceOrientation;
import org.jboss.tools.vpe.browsersim.model.DevicesList;
import org.jboss.tools.vpe.browsersim.ui.SizeWarningDialog;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

public class ResizableSkinSizeAdvisorImpl implements ResizableSkinSizeAdvisor{
	private DevicesList devicesList;
	private Shell shell;
	
	public ResizableSkinSizeAdvisorImpl(DevicesList devicesList, Shell shell) {
		super();
		this.devicesList = devicesList;
		this.shell = shell;
	}

	@Override
	public Point checkWindowSize(int orientation, Point prefferedSize, Point prefferedShellSize) {
		Rectangle clientArea = BrowserSimUtil.getMonitorClientArea(shell.getMonitor());

		boolean truncateWindow = false;
		if (devicesList.getTruncateWindow() == null) {
			if (prefferedShellSize.x > clientArea.width || prefferedShellSize.y > clientArea.height) { 
				String deviceName = devicesList.getDevices().get(devicesList.getSelectedDeviceIndex()).getName();
				
				SizeWarningDialog dialog = new SizeWarningDialog(shell, new Point(clientArea.width, clientArea.height),
						prefferedShellSize, deviceName,
						orientation == DeviceOrientation.PORTRAIT || orientation == DeviceOrientation.PORTRAIT_INVERTED);
				dialog.open();

				truncateWindow = dialog.getTruncateWindow();
				if (dialog.getRememberDecision()) {
					devicesList.setTruncateWindow(truncateWindow);
				}
			}
		} else {
			truncateWindow = devicesList.getTruncateWindow();
		}

		Point size = new Point(prefferedShellSize.x, prefferedShellSize.y);
		if (truncateWindow) {
			size.x = Math.min(prefferedShellSize.x, clientArea.width);
			size.y = Math.min(prefferedShellSize.y, clientArea.height);
		}

		return size;
	}
}
