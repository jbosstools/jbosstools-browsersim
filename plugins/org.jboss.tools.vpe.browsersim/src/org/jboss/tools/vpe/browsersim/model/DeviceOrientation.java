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
 * Holds device orientation and can be turned.
 */
public class DeviceOrientation extends Observable {
	public static final int PORTRAIT = 0;
	public static final int LANDSCAPE = 90;
	public static final int PORTRAIT_INVERTED = 180;
	public static final int LANDSCAPE_INVERTED = -90;
	
	private int orientationAngle;
	
	public DeviceOrientation(int orientationAngle) {
		this.orientationAngle = orientationAngle;
	}
	
	public void turnDevice(boolean counterclockwise) {
		if (counterclockwise) {
			orientationAngle+= 90;
		} else {
			orientationAngle-= 90;
		}
		
		// normalize angle to be in [-90; 180]
		orientationAngle = ((orientationAngle - 180) % 360) + 180;
		orientationAngle = ((orientationAngle + 90) % 360) - 90;
		
		this.setChanged();
	}

	public int getOrientationAngle() {
		return orientationAngle;
	}
}
