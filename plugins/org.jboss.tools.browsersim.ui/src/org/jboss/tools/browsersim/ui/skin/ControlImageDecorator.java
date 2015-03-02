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
package org.jboss.tools.browsersim.ui.skin;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * Abstract class that allows to render concrete controls using provided images.
 * A typical usage (anyway a subclass should be used):
 * <pre><code>  Button myButton = new Button(shell, SWT.FLAT);
 *  backButton.addSelectionListener(new SelectionAdapter() {
 *		public void widgetSelected(SelectionEvent e) {
 *			doMyAction();
 *		};
 *  });
 *  ButtonImageDecorator myButtonDecorator = new ButtonImageDecorator(myButton);
 *  myButtonDecorator.setImages(imageForVerticalLayout, imageForVerticalLayoutSelected,
 *			imageForHorizontalLayout, imageForHorizontalLayoutSelected);
 *  myButtonDecorator.setLocations(locationForVerticalLayout, locationForHorizontalLayout);
 *  myButtonDecorator.setVertical(true);
 * </code></pre>
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
abstract class ControlImageDecorator<T extends Control> {
	protected T control;
	private Point verticalOrientationLocation;
	private Point horizontalOrientationLocation;
	
	private Image verticalOrientationEnabledImage;
	private Image verticalOrientationSelectedImage;
	private Image horizontalOrientationEnabledImage;
	private Image horizontalOrientationSelectedImage;
	
	private boolean vertical = true;
	
	private MouseTrackListener mouseTrackListener;
	
	public ControlImageDecorator(T control) {
		this.control = control;
	}
	public void setLocations(Point verticalOrientationLocation, Point horizontalOrientationLocation) {
		this.verticalOrientationLocation = verticalOrientationLocation;
		this.horizontalOrientationLocation = horizontalOrientationLocation;
	}
	
	/**
	 * All params must be not null.
	 */
	public void setImages(Image verticalOrientationEnabledImage,
			Image verticalOrientationSelectedImage,
			Image horizontalOrientationEnabledImage,
			Image horizontalOrientationSelectedImage) {
		this.verticalOrientationEnabledImage = verticalOrientationEnabledImage;
		this.verticalOrientationSelectedImage = verticalOrientationSelectedImage;
		this.horizontalOrientationEnabledImage = horizontalOrientationEnabledImage;
		this.horizontalOrientationSelectedImage = horizontalOrientationSelectedImage;
		
		if (mouseTrackListener != null) {
			control.removeMouseTrackListener(mouseTrackListener);
			mouseTrackListener = null;
		}
		if (verticalOrientationEnabledImage != verticalOrientationSelectedImage 
				|| horizontalOrientationEnabledImage != horizontalOrientationSelectedImage) {
			control.addMouseTrackListener(getMouseTrackListener());
		}
	}
	
	public void setImages(Image enabledImage, Image selectedImage) {
		setImages(enabledImage, selectedImage, enabledImage, selectedImage);
	}
	
	public void setImages(Image image) {
		setImages(image, image);
	}
	
	private MouseTrackListener getMouseTrackListener() {
		if (mouseTrackListener == null) {
			mouseTrackListener = new MouseTrackListener() {
				@Override
				public void mouseHover(MouseEvent e) {
				}
				
				@Override
				public void mouseExit(MouseEvent e) {
					setSelected(false);
				}
				
				@Override
				public void mouseEnter(MouseEvent e) {
					setSelected(true);
				}
			};
		}
		return mouseTrackListener;
	}
	
	/**
	 * Changes image of the control
	 */
	private void setSelected(boolean selected) {
		if (selected) {
			if (vertical) {
				setImageCommon(verticalOrientationSelectedImage);
			} else {
				setImageCommon(horizontalOrientationSelectedImage);
			}
		} else {
			if (vertical) {
				setImageCommon(verticalOrientationEnabledImage);
			} else {
				setImageCommon(horizontalOrientationEnabledImage);
			}
		}
	}
	
	/**
	 * Changes image of the control and its position
	 */
	public void setVertical(boolean vertical) {
		this.vertical = vertical;
		if (vertical) {
			control.setLocation(verticalOrientationLocation);
			setImageCommon(verticalOrientationEnabledImage);
		} else {
			control.setLocation(horizontalOrientationLocation);
			setImageCommon(horizontalOrientationEnabledImage);
		}
		
		setSelected(false);
	}
	
	public void setVisible(boolean enabled) {
		control.setVisible(enabled);
	}
	
	public boolean isVisible() {
		return control.isVisible();
	}
	
	private void setImageCommon(Image image) {
		Rectangle imageBounds = image.getBounds();
		control.setSize(imageBounds.width, imageBounds.height);
		setImage(image);
	}
	
	protected abstract void setImage(Image image);
}