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

@SuppressWarnings("nls")
public class GalaxyNote2Skin  extends AppleIPhone3ResizableSkin {
	public static final String ANDROID_SKIN_ID = "Galaxy Note II";
	private static final Point NORMAL_SKREEN_SIZE = new Point(240, 1);
	private static final Point NORMAL_SKIN_SIZE = new Point(372, 281);
	public static final int[] VISIBLE_REGION_VERTICAL = { 68, 0, 225, 0, 226, 1, 231, 1, 232, 2, 236, 2, 237, 3, 239,
			3, 240, 4, 242, 4, 243, 5, 245, 5, 246, 6, 247, 6, 248, 7, 249, 7, 250, 8, 251, 8, 253, 10, 254, 10, 256,
			12, 257, 12, 264, 19, 264, 20, 267, 23, 267, 24, 269, 26, 269, 27, 270, 28, 270, 29, 271, 30, 271, 31, 272,
			32, 272, 34, 273, 35, 273, 37, 274, 38, 274, 40, 275, 41, 275, 45, 276, 46, 276, 53, 277, 54, 277, 84, 278,
			85, 278, 126, 277, 127, 277, 197, 278, 198, 278, 209, 279, 210, 279, 242, 280, 243, 280, 317, 279, 318,
			279, 323, 278, 324, 278, 327, 277, 328, 277, 330, 276, 331, 276, 333, 275, 334, 275, 335, 274, 336, 274,
			338, 273, 339, 273, 340, 271, 342, 271, 343, 269, 345, 269, 346, 266, 349, 266, 350, 260, 356, 259, 356,
			256, 359, 255, 359, 253, 361, 252, 361, 251, 362, 250, 362, 248, 364, 246, 364, 245, 365, 244, 365, 243,
			366, 242, 366, 241, 367, 238, 367, 237, 368, 234, 368, 233, 369, 223, 369, 222, 370, 161, 370, 160, 370,
			56, 370, 55, 370, 50, 370, 49, 369, 45, 369, 44, 368, 42, 368, 41, 367, 39, 367, 38, 366, 37, 366, 36, 365,
			34, 365, 33, 364, 32, 364, 30, 362, 29, 362, 28, 361, 27, 361, 24, 358, 23, 358, 15, 350, 15, 349, 12, 346,
			12, 345, 11, 344, 11, 343, 10, 342, 10, 341, 9, 340, 9, 339, 8, 338, 8, 337, 7, 336, 7, 335, 6, 334, 6,
			332, 5, 331, 5, 329, 4, 328, 4, 325, 3, 324, 3, 319, 2, 318, 2, 43, 3, 42, 3, 40, 4, 39, 4, 37, 5, 36, 5,
			34, 6, 33, 6, 32, 7, 31, 7, 30, 8, 29, 8, 28, 9, 27, 9, 26, 11, 24, 11, 23, 22, 12, 23, 12, 25, 10, 26, 10,
			28, 8, 29, 8, 30, 7, 31, 7, 32, 6, 33, 6, 34, 5, 36, 5, 37, 4, 40, 4, 41, 3, 44, 3, 45, 2, 51, 2, 52, 1,
			67, 1 };
	private static final int[] VISIBLE_REGION_HORIZONTAL = { 243, 0, 317, 0, 318, 1, 323, 1, 324, 2, 327, 2, 328, 3,
			330, 3, 331, 4, 333, 4, 334, 5, 335, 5, 336, 6, 338, 6, 339, 7, 340, 7, 342, 9, 343, 9, 345, 11, 346, 11,
			349, 14, 350, 14, 356, 20, 356, 21, 359, 24, 359, 25, 361, 27, 361, 28, 362, 29, 362, 30, 364, 32, 364, 34,
			365, 35, 365, 36, 366, 37, 366, 38, 367, 39, 367, 42, 368, 43, 368, 46, 369, 47, 369, 57, 370, 58, 370,
			119, 371, 120, 371, 224, 370, 225, 370, 230, 369, 231, 369, 235, 368, 236, 368, 238, 367, 239, 367, 241,
			366, 242, 366, 243, 365, 244, 365, 246, 364, 247, 364, 248, 362, 250, 362, 251, 361, 252, 361, 253, 358,
			256, 358, 257, 350, 265, 349, 265, 346, 268, 345, 268, 344, 269, 343, 269, 342, 270, 341, 270, 340, 271,
			339, 271, 338, 272, 337, 272, 336, 273, 335, 273, 334, 274, 332, 274, 331, 275, 329, 275, 328, 276, 325,
			276, 324, 277, 319, 277, 318, 278, 43, 278, 42, 277, 40, 277, 39, 276, 37, 276, 36, 275, 34, 275, 33, 274,
			32, 274, 31, 273, 30, 273, 29, 272, 28, 272, 27, 271, 26, 271, 24, 269, 23, 269, 12, 258, 12, 257, 10, 255,
			10, 254, 8, 252, 8, 251, 7, 250, 7, 249, 6, 248, 6, 247, 5, 246, 5, 244, 4, 243, 4, 240, 3, 239, 3, 236, 2,
			235, 2, 229, 1, 228, 1, 213, 0, 212, 0, 55, 1, 54, 1, 49, 2, 48, 2, 44, 3, 43, 3, 41, 4, 40, 4, 38, 5, 37,
			5, 35, 6, 34, 6, 33, 7, 32, 7, 31, 8, 30, 8, 29, 10, 27, 10, 26, 12, 24, 12, 23, 19, 16, 20, 16, 23, 13,
			24, 13, 26, 11, 27, 11, 28, 10, 29, 10, 30, 9, 31, 9, 32, 8, 34, 8, 35, 7, 37, 7, 38, 6, 40, 6, 41, 5, 45,
			5, 46, 4, 53, 4, 54, 3, 84, 3, 85, 2, 126, 2, 127, 3, 197, 3, 198, 2, 209, 2, 210, 1, 242, 1 };
	private static final Point VERTICAL_BORDERS_SIZE = new Point(NORMAL_SKIN_SIZE.x - NORMAL_SKREEN_SIZE.x, NORMAL_SKIN_SIZE.y - NORMAL_SKREEN_SIZE.y);
	private static final Point HORIZONTAL_BORDERS_SIZE = new Point(VERTICAL_BORDERS_SIZE.y, VERTICAL_BORDERS_SIZE.x);
	private static final AndroidSkinDescriptor VERTICAL_DESCRIPTOR;
	private static final int CORNERS_SIZE = 32;
	static {
		String bd = "android/galaxyNote2/vertical/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 5, 2, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "08.png"), iOsDescriptor, new ImageDescriptor(bd + "09.png"),
				new ImageDescriptor(bd + "10.png", 1, 1, SWT.VERTICAL), new ImageDescriptor(bd + "11.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "12.png"), new ImageDescriptor(bd + "13.png"), new ImageDescriptor(bd + "14.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "15.png"), new ImageDescriptor(bd + "16.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "17.png"), new ImageDescriptor(bd + "18.png"),
			};
		int bodyGridSize = 7;
		
		String bd2 = "android/galaxyNote2/buttons/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 61);
			formData.bottom = new FormAttachment(100, -30);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(33, 12);
			formData.bottom = new FormAttachment(100, -30);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		ButtonDescriptor homeButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(67, -45);
			formData.bottom = new FormAttachment(100, -30);
			homeButtonDescriptor = new ButtonDescriptor(formData, bd2 + "home.png", bd2 + "home.png", bd2 + "home-selected.png");
		}
		ButtonDescriptor refreshButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(100, -61 - 28);
			formData.bottom = new FormAttachment(100, -30);
			refreshButtonDescriptor = new ButtonDescriptor(formData, bd2 + "refresh.png", bd2 + "refresh.png", bd2 + "refresh-selected.png");
		}
		VERTICAL_DESCRIPTOR = new AndroidSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE,
				backButtonDescriptor, forwardButtonDescriptor, homeButtonDescriptor, refreshButtonDescriptor);
	}
	
	private static final AndroidSkinDescriptor HORIZONTAL_DESCRIPTOR;
	static {
		String bd = "android/galaxyNote2/horizontal/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 2, 5, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"),
				new ImageDescriptor(bd + "05.png"), iOsDescriptor, new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "08.png", 1, 1, SWT.VERTICAL), new ImageDescriptor(bd + "09.png"),
				new ImageDescriptor(bd + "10.png"), new ImageDescriptor(bd + "11.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "12.png", 1, 1, SWT.VERTICAL), new ImageDescriptor(bd + "13.png"), new ImageDescriptor(bd + "14.png"), new ImageDescriptor(bd + "15.png"), new ImageDescriptor(bd + "16.png"), new ImageDescriptor(bd + "17.png", 1, 1 , SWT.HORIZONTAL), new ImageDescriptor(bd + "18.png"),		
		};
		int bodyGridSize = 4;
		String bd2 = "android/galaxyNote2/buttons/";
		
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.right = new FormAttachment(100, -30);
			formData.bottom = new FormAttachment(100, -70);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back-horizontal.png", bd2 + "back-horizontal-disabled.png", bd2 + "back-horizontal-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.right = new FormAttachment(100, -30);
			formData.bottom = new FormAttachment(67, -12);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward-horizontal.png", bd2 + "forward-horizontal-disabled.png", bd2 + "forward-horizontal-selected.png");
		}
		ButtonDescriptor homeButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.right = new FormAttachment(100, -30);
			formData.bottom = new FormAttachment(33, 35 );
			homeButtonDescriptor = new ButtonDescriptor(formData, bd2 + "home-horizontal.png", bd2 + "home-horizontal.png", bd2 + "home-horizontal-selected.png");
		}
		ButtonDescriptor refreshButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.right = new FormAttachment(100, -30);
			formData.bottom = new FormAttachment(0, 90);
			refreshButtonDescriptor = new ButtonDescriptor(formData, bd2 + "refresh-horizontal.png", bd2 + "refresh-horizontal.png", bd2 + "refresh-horizontal-selected.png");
		}
	
		HORIZONTAL_DESCRIPTOR = new AndroidSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE,
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
			skinDescriptor = HORIZONTAL_DESCRIPTOR;
		}
		return new AndroidComposite(parent, skinDescriptor);
	}
	
	@Override
	protected int[] getNormalRegion(boolean vertical) {
		if (vertical) {
			return VISIBLE_REGION_VERTICAL;
		} else {
			return VISIBLE_REGION_HORIZONTAL;
		}
		
		//return VISIBLE_REGION_VERTICAL;
	}
}
