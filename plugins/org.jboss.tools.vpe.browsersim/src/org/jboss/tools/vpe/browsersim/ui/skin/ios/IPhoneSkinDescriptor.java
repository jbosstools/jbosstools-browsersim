package org.jboss.tools.vpe.browsersim.ui.skin.ios;

import org.jboss.tools.vpe.browsersim.ui.skin.ImageDescriptor;

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
