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

import java.util.Observable;


/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class DevicesListHolder extends Observable {
	private DevicesList devicesList;
	
	public DevicesListHolder() {
	}
		
	public DevicesList getDevicesList() {
		return devicesList;
	}

	public void setDevicesList(DevicesList devicesList) {
		if (this.devicesList != devicesList) {
			this.devicesList = devicesList;
			setChanged();
		}
	}
}
