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
import org.jboss.tools.vpe.browsersim.ui.skin.ResizableSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.ButtonDescriptor;

@SuppressWarnings("nls")
public class GalaxyNote2Skin  extends ResizableSkin {
	public static final String ANDROID_SKIN_ID = "Galaxy Note II";
	
	private static final int[] VISIBLE_REGION_VERTICAL = { 60, 0, 206, 0, 207, 1, 216, 1, 217, 2, 223, 2, 224, 3, 227,
			3, 228, 4, 231, 4, 232, 5, 233, 5, 234, 6, 236, 6, 238, 8, 239, 8, 239, 9, 240, 10, 242, 10, 243, 11, 244,
			11, 248, 15, 249, 15, 253, 19, 253, 20, 257, 24, 257, 25, 258, 26, 258, 27, 260, 29, 260, 30, 261, 31, 261,
			33, 262, 34, 262, 35, 263, 36, 263, 38, 264, 39, 264, 42, 265, 43, 265, 47, 266, 48, 266, 392, 265, 393,
			265, 400, 264, 401, 264, 405, 263, 406, 263, 409, 262, 410, 262, 412, 261, 413, 261, 415, 260, 416, 260,
			418, 259, 419, 259, 420, 258, 421, 258, 422, 257, 423, 257, 424, 255, 426, 255, 427, 253, 429, 253, 430,
			242, 441, 241, 441, 239, 443, 238, 443, 237, 444, 236, 444, 235, 445, 234, 445, 233, 446, 232, 446, 231,
			447, 230, 447, 229, 448, 227, 448, 226, 449, 224, 449, 223, 450, 219, 450, 218, 451, 214, 451, 213, 452,
			53, 452, 52, 451, 48, 451, 47, 450, 43, 450, 42, 449, 40, 449, 39, 448, 37, 448, 36, 447, 35, 447, 34, 446,
			33, 446, 32, 445, 31, 445, 30, 444, 29, 444, 28, 443, 27, 443, 25, 441, 24, 441, 13, 430, 13, 429, 11, 427,
			11, 426, 9, 424, 9, 423, 8, 422, 8, 421, 7, 420, 7, 419, 6, 418, 6, 416, 5, 415, 5, 413, 4, 412, 4, 410, 3,
			409, 3, 406, 2, 405, 2, 401, 1, 400, 1, 393, 0, 392, 0, 48, 1, 47, 1, 43, 2, 42, 2, 39, 3, 38, 3, 36, 4,
			35, 4, 34, 5, 33, 5, 31, 6, 30, 6, 29, 8, 27, 8, 26, 9, 25, 9, 24, 13, 20, 13, 19, 17, 15, 18, 15, 22, 11,
			23, 11, 24, 10, 26, 10, 27, 9, 27, 8, 28, 8, 30, 6, 32, 6, 33, 5, 34, 5, 35, 4, 38, 4, 39, 3, 42, 3, 43, 2,
			49, 2, 50, 1, 59, 1 };
	private static final int[] VISIBLE_REGION_HORIZONTAL = { 48, 0, 392, 0, 393, 1, 400, 1, 401, 2, 405, 2, 406, 3,
		409, 3, 410, 4, 412, 4, 413, 5, 415, 5, 416, 6, 418, 6, 419, 7, 420, 7, 421, 8, 422, 8, 423, 9, 424, 9,
		426, 11, 427, 11, 429, 13, 430, 13, 441, 24, 441, 25, 443, 27, 443, 28, 444, 29, 444, 30, 445, 31, 445, 32,
		446, 33, 446, 34, 447, 35, 447, 36, 448, 37, 448, 39, 449, 40, 449, 42, 450, 43, 450, 47, 451, 48, 451, 52,
		452, 53, 452, 213, 451, 214, 451, 218, 450, 219, 450, 223, 449, 224, 449, 226, 448, 227, 448, 229, 447,
		230, 447, 231, 446, 232, 446, 233, 445, 234, 445, 235, 444, 236, 444, 237, 443, 238, 443, 239, 441, 241,
		441, 242, 430, 253, 429, 253, 427, 255, 426, 255, 424, 257, 423, 257, 422, 258, 421, 258, 420, 259, 419,
		259, 418, 260, 416, 260, 415, 261, 413, 261, 412, 262, 410, 262, 409, 263, 406, 263, 405, 264, 401, 264,
		400, 265, 393, 265, 392, 266, 48, 266, 47, 265, 43, 265, 42, 264, 39, 264, 38, 263, 36, 263, 35, 262, 34,
		262, 33, 261, 31, 261, 30, 260, 29, 260, 27, 258, 26, 258, 25, 257, 24, 257, 20, 253, 19, 253, 15, 249, 15,
		248, 11, 244, 11, 243, 10, 242, 10, 240, 9, 239, 8, 239, 8, 238, 6, 236, 6, 234, 5, 233, 5, 232, 4, 231, 4,
		228, 3, 227, 3, 224, 2, 223, 2, 217, 1, 216, 1, 207, 0, 206, 0, 60, 1, 59, 1, 50, 2, 49, 2, 43, 3, 42, 3,
		39, 4, 38, 4, 35, 5, 34, 5, 33, 6, 32, 6, 30, 8, 28, 8, 27, 9, 27, 10, 26, 10, 24, 11, 23, 11, 22, 15, 18,
		15, 17, 19, 13, 20, 13, 24, 9, 25, 9, 26, 8, 27, 8, 29, 6, 30, 6, 31, 5, 33, 5, 34, 4, 35, 4, 36, 3, 38, 3,
		39, 2, 42, 2, 43, 1, 47, 1 };
	private static final Point NORMAL_SCREEN_SIZE = new Point(213, 285);
	private static final Point NORMAL_SKIN_SIZE = new Point(267, 453);
	
	private static final int CORNERS_SIZE = 32;
	private static final AndroidSkinDescriptor VERTICAL_DESCRIPTOR;
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
	
	public GalaxyNote2Skin() {
		super(VISIBLE_REGION_HORIZONTAL, VISIBLE_REGION_VERTICAL, NORMAL_SCREEN_SIZE, NORMAL_SKIN_SIZE);
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
}
