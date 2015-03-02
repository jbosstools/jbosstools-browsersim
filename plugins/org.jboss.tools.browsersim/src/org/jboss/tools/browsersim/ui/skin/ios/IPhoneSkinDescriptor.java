/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui.skin.ios;

import org.jboss.tools.browsersim.ui.skin.ImageDescriptor;

public class IPhoneSkinDescriptor {
	private int bodyGridSize;
	private ImageDescriptor[] bodyGridImageDescriptors;
	private ImageDescriptor osDescriptor;
	private int cornersSize;
	private ButtonDescriptor backButton;
	private ButtonDescriptor forwardButton;

	public IPhoneSkinDescriptor(int bodyGridSize,
			ImageDescriptor[] bodyGridImageDescriptors,
			ImageDescriptor iOSDescriptor, int cornersSize,
			ButtonDescriptor backButton, ButtonDescriptor forwardButton) {
		this.bodyGridSize = bodyGridSize;
		this.bodyGridImageDescriptors = bodyGridImageDescriptors;
		this.osDescriptor = iOSDescriptor;
		this.cornersSize = cornersSize;
		this.backButton = backButton;
		this.forwardButton = forwardButton;
	}

	public int getBodyGridSize() {
		return bodyGridSize;
	}

	public ImageDescriptor[] getBodyGridImageDescriptors() {
		return bodyGridImageDescriptors;
	}

	public ImageDescriptor getiOSDescriptor() {
		return osDescriptor;
	}
	
	public int getCornersSize() {
		return cornersSize;
	}

	public ButtonDescriptor getBackButton() {
		return backButton;
	}

	public ButtonDescriptor getForwardButton() {
		return forwardButton;
	}
}
