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
package org.jboss.tools.vpe.browsersim.ui.skin.android;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.ui.skin.DeviceComposite;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.AppleIPhone3ResizableSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.ButtonDescriptor;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
@SuppressWarnings("nls")
public class AndroidResizableSkin  extends AppleIPhone3ResizableSkin {
	public static final String ANDROID_SKIN_ID = "Android";
	private static final Point NORMAL_SKREEN_SIZE = new Point(240, 1);
	private static final Point NORMAL_SKIN_SIZE = new Point(339, 156);
	public static final int[] VISIBLE_REGION_VERTICAL =   {24, 0, 314, 0, 315, 1, 318, 1, 319, 2, 320, 2, 321, 3, 322,
		3, 323, 4, 324, 4, 325, 5, 326, 5, 329, 8, 330, 8, 331, 9, 331, 10, 334, 13, 334, 14, 335, 15, 335, 16, 336, 17,
		336, 18, 337, 19, 337, 21, 338, 22, 338, 162, 337, 163, 337, 165, 336, 166, 336, 168, 335, 169, 335, 170, 333,
		172, 333, 173, 327, 179, 326, 179, 324, 181, 323, 181, 322, 182, 321, 182, 320, 183, 318, 183, 317, 184, 316,
		184, 315, 185, 23, 185, 22, 184, 21, 184, 20, 183, 18, 183, 17, 182, 16, 182, 15, 181, 14, 181, 12, 179, 11,
		179, 5, 173, 5, 172, 3, 170, 3, 169, 2, 168, 2, 166, 1, 165, 1, 163, 0, 162, 0, 22, 1, 21, 1, 19, 2, 18, 2, 17,
		3, 16, 3, 15, 4, 14, 4, 13, 7, 10, 7, 9, 8, 8, 9, 8, 12, 5, 13, 5, 14, 4, 15, 4, 16, 3, 17, 3, 18, 2, 20, 2, 21,
		1, 23, 1};
	private static final Point VERTICAL_BORDERS_SIZE = new Point(NORMAL_SKIN_SIZE.x - NORMAL_SKREEN_SIZE.x, NORMAL_SKIN_SIZE.y - NORMAL_SKREEN_SIZE.y);
	private static final Point HORIZONTAL_BORDERS_SIZE = new Point(VERTICAL_BORDERS_SIZE.y, VERTICAL_BORDERS_SIZE.x);
	private static final AndroidSkinDescriptor VERTICAL_DESCRIPTOR;
	static {
		String bd = "android/droid/vertical/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 3, 1, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "03.png"), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "14.png", 1, 1, SWT.VERTICAL), iOsDescriptor,                                                                                                new ImageDescriptor(bd + "16.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "17.png"), new ImageDescriptor(bd + "18.png"), new ImageDescriptor(bd + "19.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "20.png"), new ImageDescriptor(bd + "21.png")
			};
		int bodyGridSize = 5;
		int cornersSize = 32;
		
		String bd2 = "android/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 61);
			formData.bottom = new FormAttachment(100, -41);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(33, 12);
			formData.bottom = new FormAttachment(100, -41);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward.png", bd2 + "forward-selected.png");
		}
		ButtonDescriptor homeButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(67, -45);
			formData.bottom = new FormAttachment(100, -41);
			homeButtonDescriptor = new ButtonDescriptor(formData, bd2 + "home.png", bd2 + "home.png", bd2 + "home-selected.png");
		}
		ButtonDescriptor refreshButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(100, -61 - 28);
			formData.bottom = new FormAttachment(100, -41);
			refreshButtonDescriptor = new ButtonDescriptor(formData, bd2 + "refresh.png", bd2 + "refresh.png", bd2 + "refresh-selected.png");
		}
		VERTICAL_DESCRIPTOR = new AndroidSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, cornersSize,
				backButtonDescriptor, forwardButtonDescriptor, homeButtonDescriptor, refreshButtonDescriptor);
	}
	
	@Override
	protected Point getBordersSize(boolean vertical) {
		Point bordersSize = vertical ? VERTICAL_BORDERS_SIZE : HORIZONTAL_BORDERS_SIZE;
		return bordersSize;
	}

	@Override
	protected DeviceComposite createDeviceComposite(Composite parent, boolean vertical) {
		AndroidSkinDescriptor skinDescriptor;
		if (vertical) {
			skinDescriptor = VERTICAL_DESCRIPTOR;
		} else {
			skinDescriptor = VERTICAL_DESCRIPTOR;
		}
		return new AndroidComposite(parent, skinDescriptor);
	}
	
	@Override
	protected int[] getNormalRegion(boolean vertical) {
		return VISIBLE_REGION_VERTICAL;
	}
}
