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
package org.jboss.tools.browsersim.model.preferences;

import java.util.Map;
import java.util.Observable;

import org.jboss.tools.browsersim.model.Device;
import org.jboss.tools.browsersim.model.TruncateWindow;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class CommonPreferences extends Observable {
	private Map<String, Device> devices;
	private TruncateWindow truncateWindow;
	private String screenshotsFolder;
	private String weinreScriptUrl;
	private String weinreClientUrl;

	public CommonPreferences(Map<String, Device> devices, TruncateWindow truncateWindow, String screenshotsFolder,
			String weinreScriptUrl, String weinreClientUrl) {
		this.devices = devices;
		this.truncateWindow = truncateWindow;
		this.screenshotsFolder = screenshotsFolder;
		this.weinreScriptUrl = weinreScriptUrl;
		this.weinreClientUrl = weinreClientUrl;
	}

	public Map<String, Device> getDevices() {
		return devices;
	}

	public void setDevices(Map<String, Device> devices) {
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
