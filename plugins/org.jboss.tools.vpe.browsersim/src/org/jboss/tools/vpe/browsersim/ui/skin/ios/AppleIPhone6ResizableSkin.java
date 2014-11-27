/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.ui.skin.ios;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.ui.skin.DeviceComposite;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.vpe.browsersim.ui.skin.ResizableSkin;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
@SuppressWarnings("nls")
public class AppleIPhone6ResizableSkin extends ResizableSkin {
	public static final String IPHONE6_SKIN_ID = "iPhone 6";

	private static final int[] VISIBLE_REGION_VERTICAL = { 66, 0, 207, 0, 208, 1, 214, 1, 215, 2, 218, 2, 219, 3, 222,
			3, 223, 4, 225, 4, 226, 5, 227, 5, 228, 6, 230, 6, 231, 7, 232, 7, 233, 8, 234, 8, 236, 10, 237, 10, 238,
			11, 239, 11, 241, 13, 242, 13, 245, 16, 246, 16, 255, 25, 255, 26, 258, 29, 258, 30, 260, 32, 260, 33, 262,
			35, 262, 36, 263, 37, 263, 38, 264, 39, 264, 40, 265, 41, 265, 42, 266, 43, 266, 44, 267, 45, 267, 47, 268,
			48, 268, 50, 269, 51, 269, 55, 270, 56, 270, 59, 271, 60, 271, 159, 272, 160, 273, 160, 273, 197, 272, 197,
			271, 198, 271, 458, 270, 459, 270, 461, 269, 462, 269, 466, 268, 467, 268, 469, 267, 470, 267, 472, 266,
			473, 266, 474, 265, 475, 265, 476, 264, 477, 264, 478, 263, 479, 263, 480, 262, 481, 262, 482, 261, 483,
			261, 484, 259, 486, 259, 487, 256, 490, 256, 491, 246, 501, 245, 501, 242, 504, 241, 504, 239, 506, 238,
			506, 236, 508, 235, 508, 234, 509, 233, 509, 232, 510, 231, 510, 230, 511, 229, 511, 228, 512, 226, 512,
			225, 513, 223, 513, 222, 514, 220, 514, 219, 515, 216, 515, 215, 516, 211, 516, 210, 517, 63, 517, 62, 516,
			58, 516, 57, 515, 54, 515, 53, 514, 51, 514, 50, 513, 48, 513, 47, 512, 46, 512, 45, 511, 44, 511, 43, 510,
			41, 510, 39, 508, 38, 508, 37, 507, 36, 507, 35, 506, 34, 506, 31, 503, 30, 503, 26, 499, 25, 499, 19, 493,
			19, 492, 15, 488, 15, 487, 13, 485, 13, 484, 12, 483, 12, 482, 10, 480, 10, 479, 9, 478, 9, 477, 8, 476, 8,
			474, 7, 473, 7, 472, 6, 471, 6, 469, 5, 468, 5, 466, 4, 465, 4, 460, 3, 459, 3, 248, 2, 247, 0, 247, 0,
			208, 2, 208, 3, 207, 3, 200, 2, 199, 0, 199, 0, 160, 2, 160, 3, 159, 3, 138, 2, 137, 0, 137, 0, 112, 2,
			112, 3, 111, 3, 58, 4, 57, 4, 52, 5, 51, 5, 49, 6, 48, 6, 46, 7, 45, 7, 44, 8, 43, 8, 42, 9, 41, 9, 40, 10,
			39, 10, 38, 11, 37, 11, 36, 12, 35, 12, 34, 14, 32, 14, 31, 16, 29, 16, 28, 21, 23, 21, 22, 24, 19, 25, 19,
			30, 14, 31, 14, 33, 12, 34, 12, 36, 10, 37, 10, 38, 9, 39, 9, 40, 8, 41, 8, 42, 7, 43, 7, 44, 6, 45, 6, 46,
			5, 48, 5, 49, 4, 51, 4, 52, 3, 54, 3, 55, 2, 58, 2, 59, 1, 65, 1 };
	private static final int[] VISIBLE_REGION_HORIZONTAL = { 160, 0, 197, 0, 197, 1, 198, 2, 458, 2, 459, 3, 461, 3,
			462, 4, 466, 4, 467, 5, 469, 5, 470, 6, 472, 6, 473, 7, 474, 7, 475, 8, 476, 8, 477, 9, 478, 9, 479, 10,
			480, 10, 481, 11, 482, 11, 483, 12, 484, 12, 486, 14, 487, 14, 490, 17, 491, 17, 501, 27, 501, 28, 504, 31,
			504, 32, 506, 34, 506, 35, 508, 37, 508, 38, 509, 39, 509, 40, 510, 41, 510, 42, 511, 43, 511, 44, 512, 45,
			512, 47, 513, 48, 513, 50, 514, 51, 514, 53, 515, 54, 515, 57, 516, 58, 516, 62, 517, 63, 517, 210, 516,
			211, 516, 215, 515, 216, 515, 219, 514, 220, 514, 222, 513, 223, 513, 225, 512, 226, 512, 227, 511, 228,
			511, 229, 510, 230, 510, 232, 508, 234, 508, 235, 507, 236, 507, 237, 506, 238, 506, 239, 503, 242, 503,
			243, 499, 247, 499, 248, 493, 254, 492, 254, 488, 258, 487, 258, 485, 260, 484, 260, 483, 261, 482, 261,
			480, 263, 479, 263, 478, 264, 477, 264, 476, 265, 474, 265, 473, 266, 472, 266, 471, 267, 469, 267, 468,
			268, 466, 268, 465, 269, 460, 269, 459, 270, 248, 270, 247, 271, 247, 273, 208, 273, 208, 271, 207, 270,
			200, 270, 199, 271, 199, 273, 160, 273, 160, 271, 159, 270, 138, 270, 137, 271, 137, 273, 112, 273, 112,
			271, 111, 270, 58, 270, 57, 269, 52, 269, 51, 268, 49, 268, 48, 267, 46, 267, 45, 266, 44, 266, 43, 265,
			42, 265, 41, 264, 40, 264, 39, 263, 38, 263, 37, 262, 36, 262, 35, 261, 34, 261, 32, 259, 31, 259, 29, 257,
			28, 257, 23, 252, 22, 252, 19, 249, 19, 248, 14, 243, 14, 242, 12, 240, 12, 239, 10, 237, 10, 236, 9, 235,
			9, 234, 8, 233, 8, 232, 7, 231, 7, 230, 6, 229, 6, 228, 5, 227, 5, 225, 4, 224, 4, 222, 3, 221, 3, 219, 2,
			218, 2, 215, 1, 214, 1, 208, 0, 207, 0, 66, 1, 65, 1, 59, 2, 58, 2, 55, 3, 54, 3, 51, 4, 50, 4, 48, 5, 47,
			5, 46, 6, 45, 6, 43, 7, 42, 7, 41, 8, 40, 8, 39, 10, 37, 10, 36, 11, 35, 11, 34, 13, 32, 13, 31, 16, 28,
			16, 27, 25, 18, 26, 18, 29, 15, 30, 15, 32, 13, 33, 13, 35, 11, 36, 11, 37, 10, 38, 10, 39, 9, 40, 9, 41,
			8, 42, 8, 43, 7, 44, 7, 45, 6, 47, 6, 48, 5, 50, 5, 51, 4, 55, 4, 56, 3, 59, 3, 60, 2, 159, 2, 160, 1 };
	private static final Point NORMAL_SCREEN_SIZE = new Point(220, 292);
	private static final Point NORMAL_SKIN_SIZE = new Point(274, 518);

	private static final int CORNERS_SIZE = 40;

	private static final IPhoneSkinDescriptor VERTICAL_DESCRIPTOR;
	static {
		String bd = "ios/iphone6/vertical/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 5, 2, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = { new ImageDescriptor(bd + "01.png"),
				new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL),
				new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL),
				new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "08.png"), iOsDescriptor, new ImageDescriptor(bd + "09.png"),
				new ImageDescriptor(bd + "10.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "11.png", 1, 1, SWT.VERTICAL), new ImageDescriptor(bd + "12.png"),
				new ImageDescriptor(bd + "13.png"), new ImageDescriptor(bd + "14.png", 1, 1, SWT.HORIZONTAL),
				new ImageDescriptor(bd + "15.png"), new ImageDescriptor(bd + "16.png", 1, 1, SWT.HORIZONTAL),
				new ImageDescriptor(bd + "17.png"), new ImageDescriptor(bd + "18.png"), };
		int bodyGridSize = 7;

		String bd2 = "ios/iphone6/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 52);
			formData.bottom = new FormAttachment(100, -130);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2
					+ "back-selected.png");
		}

		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(15, 52);
			formData.bottom = new FormAttachment(100, -130);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png",
					bd2 + "forward-selected.png");
		}
		VERTICAL_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor,
				CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}
	private static final IPhoneSkinDescriptor HORIZONTAL_DESCRIPTOR;
	static {
		String bd = "ios/iphone6/horizontal/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 2, 5, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = { new ImageDescriptor(bd + "01.png"),
				new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL),
				new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png"), iOsDescriptor,
				new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "08.png", 1, 1, SWT.VERTICAL), new ImageDescriptor(bd + "09.png"),
				new ImageDescriptor(bd + "10.png"), new ImageDescriptor(bd + "11.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "12.png", 1, 1, SWT.VERTICAL), new ImageDescriptor(bd + "13.png"),
				new ImageDescriptor(bd + "14.png"), new ImageDescriptor(bd + "15.png"),
				new ImageDescriptor(bd + "16.png"), new ImageDescriptor(bd + "17.png", 1, 1, SWT.HORIZONTAL),
				new ImageDescriptor(bd + "18.png") };
		int bodyGridSize = 4;
		String bd2 = "ios/iphone6/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 145);
			formData.bottom = new FormAttachment(100, -39);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2
					+ "back-selected.png");
		}

		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(15, 145);
			formData.bottom = new FormAttachment(100, -39);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png",
					bd2 + "forward-selected.png");
		}
		HORIZONTAL_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor,
				CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}

	public AppleIPhone6ResizableSkin() {
		super(VISIBLE_REGION_HORIZONTAL, VISIBLE_REGION_VERTICAL, NORMAL_SCREEN_SIZE, NORMAL_SKIN_SIZE);
	}

	@Override
	protected DeviceComposite createDeviceComposite(Composite parent, boolean vertical) {
		IPhoneSkinDescriptor skinDescriptor;
		if (vertical) {
			skinDescriptor = VERTICAL_DESCRIPTOR;
		} else {
			skinDescriptor = HORIZONTAL_DESCRIPTOR;
		}
		return new AppleIPhone6Composite(parent, skinDescriptor);
	}

	@Override
	public boolean automaticallyHideAddressBar() {
		return true;
	}
}
