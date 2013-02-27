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
import org.jboss.tools.vpe.browsersim.model.TruncateWindow;
import org.jboss.tools.vpe.browsersim.model.preferences.CommonPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;
import org.jboss.tools.vpe.browsersim.ui.SizeWarningDialog;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

public class ResizableSkinSizeAdvisorImpl implements ResizableSkinSizeAdvisor{
	private CommonPreferences commonPreferences;
	private SpecificPreferences specificPreferences;
	private Shell shell;
	
	public ResizableSkinSizeAdvisorImpl(CommonPreferences cp, SpecificPreferences sp, Shell shell) {
		super();
		this.commonPreferences = cp;
		this.specificPreferences = sp;
		this.shell = shell;
	}

	@Override
	public Point checkWindowSize(int orientation, Point prefferedSize, Point prefferedShellSize) {
		Rectangle clientArea = BrowserSimUtil.getMonitorClientArea(shell.getMonitor());

		TruncateWindow truncateWindow = null;
		if (commonPreferences.getTruncateWindow() == TruncateWindow.PROMPT) {
			if (prefferedShellSize.x > clientArea.width || prefferedShellSize.y > clientArea.height) { 
				String deviceName = commonPreferences.getDevices().get(specificPreferences.getSelectedDeviceIndex()).getName();
				
				SizeWarningDialog dialog = new SizeWarningDialog(shell, new Point(clientArea.width, clientArea.height),
						prefferedShellSize, deviceName,
						orientation == DeviceOrientation.PORTRAIT || orientation == DeviceOrientation.PORTRAIT_INVERTED);
				dialog.open();

				truncateWindow = dialog.getTruncateWindow();
				if (dialog.getRememberDecision()) {
					commonPreferences.setTruncateWindow(truncateWindow);
				}
			}
		} else {
			truncateWindow = commonPreferences.getTruncateWindow();
		}

		Point size = new Point(prefferedShellSize.x, prefferedShellSize.y);
		if (TruncateWindow.ALWAYS_TRUNCATE.equals(truncateWindow)) {
			size.x = Math.min(prefferedShellSize.x, clientArea.width);
			size.y = Math.min(prefferedShellSize.y, clientArea.height);
		}

		return size;
	}
}
