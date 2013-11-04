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
 * @author Yahor Radtsevich (yradtsevich)
 */
@SuppressWarnings("nls")
public class AppleIPhone4ResizableSkin extends ResizableSkin {
	public static final String IPHONE4_SKIN_ID = "iPhone 4";
	
	private static final int[] VISIBLE_REGION_VERTICAL = {40, 0, 254, 0, 255, 1, 261, 1, 262, 2, 265, 2, 266, 3, 267, 3,
		268, 4, 270, 4, 271, 5, 272, 5, 274, 7, 275, 7, 278, 10, 279, 10, 282, 13, 282, 14, 285, 17, 285, 18, 287, 20,
		287, 21, 288, 22, 288, 23, 289, 24, 289, 26, 290, 27, 290, 29, 291, 30, 291, 398, 290, 399, 290, 401, 289, 402,
		289, 404, 288, 405, 288, 406, 287, 407, 287, 408, 285, 410, 285, 411, 283, 413, 283, 414, 278, 419, 277, 419,
		275, 421, 274, 421, 272, 423, 271, 423, 270, 424, 269, 424, 268, 425, 266, 425, 265, 426, 263, 426, 262, 427,
		257, 427, 256, 428, 38, 428, 37, 427, 33, 427, 32, 426, 30, 426, 29, 425, 27, 425, 26, 424, 25, 424, 24,
		423, 23, 423, 22, 422, 21, 422, 18, 419, 17, 419, 11, 413, 11, 412, 9, 410, 9, 409, 8, 408, 8, 407, 7, 406, 7,
		405, 6, 404, 6, 403, 5, 402, 5, 400, 4, 399, 4, 395, 3, 394, 3, 200, 2, 199, 0,	199, 0, 179, 2, 179, 3, 178, 3,
		153, 2, 152, 0, 152, 0, 132, 2, 132, 3, 131, 3, 94, 2, 93, 1, 93, 0, 92, 0,	62, 1, 61, 2, 61, 3, 60, 3, 35, 4,
		34, 4, 30, 5, 29, 5, 26, 6, 25, 6, 24, 7, 23, 7, 22, 8, 21, 8, 20, 9, 19, 9, 18, 12, 15, 12, 14, 17, 9, 18, 9,
		20, 7, 21, 7, 23, 5, 24, 5, 25, 4, 26, 4, 27, 3, 29, 3, 30, 2, 33, 2, 34, 1, 39, 1}; 
	private static final int[] VISIBLE_REGION_HORIZONTAL =  {30, 0, 398, 0, 399, 1, 401, 1, 402, 2, 404, 2, 405, 3,
		406, 3, 407, 4, 408, 4, 410, 6, 411, 6, 413, 8, 414, 8, 419, 13, 419, 14, 421, 16, 421, 17, 423, 19, 423, 20,
		424, 21, 424, 22, 425, 23, 425, 25, 426, 26, 426, 28, 427, 29, 427, 34, 428, 35, 428, 253, 427, 254, 427, 258,
		426, 259, 426, 261, 425, 262, 425, 264, 424, 265, 424, 266, 423, 267, 423, 268, 422, 269, 422, 270, 419, 273,
		419, 274, 413, 280, 412, 280, 410, 282, 409, 282, 408, 283, 407, 283, 406, 284, 405, 284, 404, 285, 403, 285,
		402, 286, 400, 286, 399, 287, 395, 287, 394, 288, 200, 288, 199, 289, 199, 291, 179, 291, 179, 289, 178, 288,
		153, 288, 152, 289, 152, 291, 132, 291, 132, 289, 131, 288, 94, 288, 93, 289, 93, 290, 92, 291, 62, 291, 61,
		290, 61, 289, 60, 288, 35, 288, 34, 287, 30, 287, 29, 286, 26, 286, 25, 285, 24, 285, 23, 284, 22, 284, 21,
		283, 20, 283, 19, 282, 18, 282, 15, 279, 14, 279, 9, 274, 9, 273, 7, 271, 7, 270, 5, 268, 5, 267, 4, 266, 4,
		265, 3, 264, 3, 262, 2, 261, 2, 258, 1, 257, 1, 252, 0, 251, 0, 37, 1, 36, 1, 30, 2, 29, 2, 26, 3, 25, 3, 24, 4,
		23, 4, 21, 5, 20, 5, 19, 7, 17, 7, 16, 10, 13, 10, 12, 13, 9, 14, 9, 17, 6, 18, 6, 20, 4, 21, 4, 22, 3, 23, 3,
		24, 2, 26, 2, 27, 1, 29, 1};
	private static final Point NORMAL_SCREEN_SIZE = new Point(236, 198);
	private static final Point NORMAL_SKIN_SIZE = new Point(292, 429);
	
	private static final int CORNERS_SIZE = 40;
	
	private static final IPhoneSkinDescriptor VERTICAL_DESCRIPTOR;
	static {
		String bd = "ios/iphone4/vertical/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 5, 2, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "03.png"), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "08.png"), new ImageDescriptor(bd + "09.png"),
				new ImageDescriptor(bd + "11.png"), iOsDescriptor,                                                                                                                                                                                                                  new ImageDescriptor(bd + "13.png"),
				new ImageDescriptor(bd + "14.png", 1, 1, SWT.VERTICAL),                                                                                                                                                                                                             new ImageDescriptor(bd + "16.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "17.png"), new ImageDescriptor(bd + "18.png"), new ImageDescriptor(bd + "19.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "20.png"), new ImageDescriptor(bd + "21.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "22.png"), new ImageDescriptor(bd + "23.png"),
			};
		int bodyGridSize = 7;
		
		String bd2 = "ios/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 52);
			formData.bottom = new FormAttachment(100, -124);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(15, 52);
			formData.bottom = new FormAttachment(100, -124);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		VERTICAL_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}
	private static final IPhoneSkinDescriptor HORIZONTAL_DESCRIPTOR;
	static {
		String bd = "ios/iphone4/horizontal/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 2, 5, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "03.png"), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"),
				new ImageDescriptor(bd + "08.png"), iOsDescriptor,                                                                                new ImageDescriptor(bd + "10.png"),
				new ImageDescriptor(bd + "11.png", 1, 1, SWT.VERTICAL),                                                                           new ImageDescriptor(bd + "12.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "13.png"),                                                                                               new ImageDescriptor(bd + "14.png"),
				new ImageDescriptor(bd + "15.png", 1, 1, SWT.VERTICAL),                                                                           new ImageDescriptor(bd + "16.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "17.png"),                                                                                               new ImageDescriptor(bd + "18.png"),
				new ImageDescriptor(bd + "19.png"), new ImageDescriptor(bd + "20.png"), new ImageDescriptor(bd + "21.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "22.png")
		};
		int bodyGridSize = 4;
		String bd2 = "ios/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 145);
			formData.bottom = new FormAttachment(100, -33);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(15, 145);
			formData.bottom = new FormAttachment(100, -33);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		HORIZONTAL_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}

	public AppleIPhone4ResizableSkin() {
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
		return new AppleIPhoneComposite(parent, skinDescriptor);
	}

	@Override
	public boolean automaticallyHideAddressBar() {
		return true;
	}
}
