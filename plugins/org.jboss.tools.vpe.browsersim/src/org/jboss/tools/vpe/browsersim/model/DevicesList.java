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

/**
 * An unmodifiable list of {@link Device}s, with modifiable {@link #selectedDeviceIndex}.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class DevicesList extends Observable {
	private List<Device> devices;
	private int selectedDeviceIndex;

	public DevicesList(List<Device> devices, int selectedDeviceIndex) {
		this.devices = devices;
		this.selectedDeviceIndex = selectedDeviceIndex;
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
}
