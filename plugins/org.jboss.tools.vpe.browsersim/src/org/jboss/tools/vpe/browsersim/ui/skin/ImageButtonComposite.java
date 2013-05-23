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
package org.jboss.tools.vpe.browsersim.ui.skin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.ButtonDescriptor;
import org.jboss.tools.vpe.browsersim.util.BrowserSimImageList;


public class ImageButtonComposite extends Composite {

	private MouseTrackListener mouseTrackListener;
	private Image enabledImage;
	private Image disabledImage;
	private Image selectedImage;
	protected boolean enabled;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ImageButtonComposite(Composite parent, Image enabledImage, Image disabledImage, Image selectedImage) {
		super(parent, SWT.NONE);
		this.enabledImage = enabledImage;
		this.disabledImage = disabledImage;
		this.selectedImage = selectedImage;
		setEnabled(true);
		addMouseTrackListener(getMouseTrackListener());
	}
	
	public ImageButtonComposite(Composite parent, BrowserSimImageList imageList, ButtonDescriptor buttonDescriptor) {
		this( parent, imageList.getImage(buttonDescriptor.getEnabledImageName()),
				imageList.getImage(buttonDescriptor.getDisabledImageName()),
				imageList.getImage(buttonDescriptor.getSelectedImageName()) );

		FormData formData = buttonDescriptor.getFormData();
		FormData actualFormData = new FormData();
		actualFormData.left = formData.left;
		actualFormData.top = formData.top;
		actualFormData.right = formData.right;
		actualFormData.bottom = formData.bottom;
		
		Rectangle size = enabledImage.getBounds();
		actualFormData.width = size.width;
		actualFormData.height = size.height;
		
		this.setLayoutData(actualFormData);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			setBackgroundImage(enabledImage);
		} else {
			setBackgroundImage(disabledImage);
		}
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private MouseTrackListener getMouseTrackListener() {
		if (mouseTrackListener == null) {
			mouseTrackListener = new MouseTrackListener() {
				@Override
				public void mouseHover(MouseEvent e) {
				}
				
				@Override
				public void mouseExit(MouseEvent e) {
					if (enabled) {
						setBackgroundImage(enabledImage);
					}
				}
				
				@Override
				public void mouseEnter(MouseEvent e) {
					if (enabled) {
						setBackgroundImage(selectedImage);
					}
				}
			};
		}
		return mouseTrackListener;
	}
}
