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
package org.jboss.tools.vpe.browsersim.model.preferences;

import java.util.List;
import java.util.Observable;

import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.TruncateWindow;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class CommonPreferences extends Observable {
	private List<Device> devices;
	private TruncateWindow truncateWindow;
	private String screenshotsFolder;
	private String weinreScriptUrl;
	private String weinreClientUrl;

	public CommonPreferences(List<Device> devices, TruncateWindow truncateWindow, String screenshotsFolder,
			String weinreScriptUrl, String weinreClientUrl) {
		this.devices = devices;
		this.truncateWindow = truncateWindow;
		this.screenshotsFolder = screenshotsFolder;
		this.weinreScriptUrl = weinreScriptUrl;
		this.weinreClientUrl = weinreClientUrl;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		if (!this.devices.equals(devices)) {
			this.devices = devices;
			setChanged();
		}
	}

	public TruncateWindow getTruncateWindow() {
		return truncateWindow;
	}

	public void setTruncateWindow(TruncateWindow truncateWindow) {
		if ((this.truncateWindow != null && !this.truncateWindow.equals(truncateWindow))
				|| (this.truncateWindow == null && truncateWindow != null)) {
			this.truncateWindow = truncateWindow;
			setChanged();
		}
	}

	public String getScreenshotsFolder() {
		return screenshotsFolder;
	}

	public void setScreenshotsFolder(String screenshotsFolder) {
		if (!this.screenshotsFolder.equals(screenshotsFolder)) {
			this.screenshotsFolder = screenshotsFolder;
			setChanged();
		}
	}

	public String getWeinreScriptUrl() {
		return weinreScriptUrl;
	}

	public void setWeinreScriptUrl(String weinreScriptUrl) {
		if (!this.weinreScriptUrl.equals(weinreScriptUrl)) {
			this.weinreScriptUrl = weinreScriptUrl;
			setChanged();
		}
	}

	public String getWeinreClientUrl() {
		return weinreClientUrl;
	}

	public void setWeinreClientUrl(String weinreClientUrl) {
		if (!this.weinreClientUrl.equals(weinreClientUrl)) {
			this.weinreClientUrl = weinreClientUrl;
			setChanged();
		}
	}
	
	public void copyProperties(CommonPreferences cp) {
		setDevices(cp.getDevices());
		setScreenshotsFolder(cp.getScreenshotsFolder());
		setTruncateWindow(cp.getTruncateWindow());
		setWeinreClientUrl(cp.getWeinreClientUrl());
		setWeinreScriptUrl(cp.getWeinreScriptUrl());
	}
}
