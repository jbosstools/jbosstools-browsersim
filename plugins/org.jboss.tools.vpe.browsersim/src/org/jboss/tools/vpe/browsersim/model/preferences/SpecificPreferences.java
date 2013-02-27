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

import java.util.Observable;

import org.eclipse.swt.graphics.Point;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class SpecificPreferences extends Observable {
	private int selectedDeviceIndex;
	private boolean useSkins;
	private Point location;

	public SpecificPreferences(int selectedDeviceIndex, boolean useSkins, Point location) {
		this.selectedDeviceIndex = selectedDeviceIndex;
		this.useSkins = useSkins;
		this.location = location;
	}

	public int getSelectedDeviceIndex() {
		return selectedDeviceIndex;
	}

	public void setSelectedDeviceIndex(int selectedDeviceIndex) {
		if (this.selectedDeviceIndex != selectedDeviceIndex) {
			this.selectedDeviceIndex = selectedDeviceIndex;
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

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
			this.location = location;
	}

	public void copyProperties(SpecificPreferences sp) {
		setSelectedDeviceIndex(sp.getSelectedDeviceIndex());
		setUseSkins(sp.getUseSkins());
		setLocation(sp.getLocation());
	}
}
