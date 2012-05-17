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
}
