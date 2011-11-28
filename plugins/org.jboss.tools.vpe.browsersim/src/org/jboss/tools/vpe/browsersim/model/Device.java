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

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class Device {
	public static final int DEFAULT_SIZE = -1;
	
	private String name;
	private int width;
	private int height;
	private String userAgent;

	public Device(String name, int width, int height, String userAgent) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.userAgent = userAgent;
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
}
