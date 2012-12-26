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
package org.jboss.tools.vpe.browsersim.model;

import java.util.List;
import java.util.Observable;

import org.eclipse.swt.graphics.Point;

/**
 * An unmodifiable list of {@link Device}s, with modifiable {@link #selectedDeviceIndex}.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class DevicesList extends Observable {
	private List<Device> devices;
	private int selectedDeviceIndex;
	private boolean useSkins;
	private Boolean truncateWindow;
	private Point location;

	public DevicesList(List<Device> devices, int selectedDeviceIndex, boolean useSkins, Boolean truncateWindow, 
			Point location) {
		this.devices = devices;
		this.selectedDeviceIndex = selectedDeviceIndex;
		this.useSkins = useSkins;
		this.truncateWindow = truncateWindow;
		this.location = location;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public int getSelectedDeviceIndex() {
		return selectedDeviceIndex;
	}

	public void setSelectedDeviceIndex(int currentDeviceIndex) {
		if (this.selectedDeviceIndex != currentDeviceIndex) {
			this.selectedDeviceIndex = currentDeviceIndex;
			setChanged();
		}
	}

	public boolean getUseSkins() {
		return useSkins;
	}

	public void setUseSkins(boolean useSkins) {
		if (this.useSkins != useSkins) {
			this.useSkins = useSkins;
			setChanged();
		}
	}

	public Boolean getTruncateWindow() {
		return truncateWindow;
	}

	public void setTruncateWindow(Boolean truncateWindow) {
		if ( (this.truncateWindow != null && !this.truncateWindow.equals(truncateWindow))
				|| (this.truncateWindow == null && truncateWindow != null)) {
			this.truncateWindow = truncateWindow;
			setChanged();
		}
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
}
