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
package org.jboss.tools.browsersim.ui.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class Device {
	public static final int DEFAULT_SIZE = -1;
	public static final DecimalFormat PIXEL_RAIO_FORMAT = new DecimalFormat("0.###"); //$NON-NLS-1$
	static {
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
		formatSymbols.setDecimalSeparator('.');
		PIXEL_RAIO_FORMAT.setDecimalFormatSymbols(formatSymbols);
	}
	
	private String id;
	
	private String name;
	private int width;
	private int height;
	private double pixelRatio;
	private String userAgent;
	private String skinId;

	public Device(String name, int width, int height, double pixelRatio, String userAgent, String skinId) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.pixelRatio = pixelRatio;
		this.userAgent = userAgent;
		this.skinId = skinId;
	}

	public Device(String id, String name, int width, int height, double pixelRatio, String userAgent, String skinId) {
		this(name, width, height, pixelRatio, userAgent, skinId);
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getName() {
		return name;
	}

	public String getSkinId() {
		return skinId;
	}

	public double getPixelRatio() {
		return pixelRatio;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(pixelRatio);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((skinId == null) ? 0 : skinId.hashCode());
		result = prime * result + ((userAgent == null) ? 0 : userAgent.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (!id.equals(other.id)) {
			return false;
		} if (height != other.height)
			return false;
		if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(pixelRatio) != Double.doubleToLongBits(other.pixelRatio))
			return false;
		if (skinId == null) {
			if (other.skinId != null)
				return false;
		} else if (!skinId.equals(other.skinId))
			return false;
		if (userAgent == null) {
			if (other.userAgent != null)
				return false;
		} else if (!userAgent.equals(other.userAgent))
			return false;
		if (width != other.width)
			return false;
		return true;
	}
}
