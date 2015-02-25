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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.browsersim.ui.skin.DeviceComposite;
import org.jboss.tools.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.browsersim.ui.skin.ResizableSkin;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
@SuppressWarnings("nls")
public class AppleIPadMiniResizableSkin extends ResizableSkin {
	public static final String IPAD_MINI_SKIN_ID = "iPad mini";
	private static final int[] VISIBLE_REGION_VERTICAL = { 37, 0, 341, 0, 342, 1, 346, 1, 347, 2, 349, 2, 350, 3, 352,
		3, 353, 4, 354, 4, 355, 5, 356, 5, 358, 7, 359, 7, 361, 9, 362, 9, 368, 15, 368, 16, 371, 19, 371, 20, 372,
		21, 372, 22, 373, 23, 373, 24, 374, 25, 374, 26, 375, 27, 375, 29, 376, 30, 376, 33, 377, 34, 377, 576,
		376, 577, 376, 579, 375, 580, 375, 582, 374, 583, 374, 585, 373, 586, 373, 587, 371, 589, 371, 590, 369,
		592, 369, 593, 361, 601, 360, 601, 358, 603, 357, 603, 355, 605, 353, 605, 352, 606, 351, 606, 350, 607,
		348, 607, 347, 608, 344, 608, 343, 609, 34, 609, 33, 608, 30, 608, 29, 607, 27, 607, 26, 606, 25, 606, 24,
		605, 23, 605, 22, 604, 21, 604, 20, 603, 19, 603, 16, 600, 15, 600, 9, 594, 9, 593, 7, 591, 7, 590, 5, 588,
		5, 587, 4, 586, 4, 585, 3, 584, 3, 582, 2, 581, 2, 579, 1, 578, 1, 574, 0, 573, 0, 36, 1, 35, 1, 31, 2, 30,
		2, 28, 3, 27, 3, 26, 4, 25, 4, 24, 5, 23, 5, 22, 6, 21, 6, 20, 7, 19, 7, 18, 12, 13, 12, 12, 13, 12, 17, 8,
		18, 8, 20, 6, 21, 6, 22, 5, 23, 5, 24, 4, 25, 4, 26, 3, 28, 3, 29, 2, 31, 2, 32, 1, 36, 1 };
	private static final int[] VISIBLE_REGION_HORIZONTAL = { 34, 0, 576, 0, 577, 1, 579, 1, 580, 2, 582, 2, 583, 3,
		585, 3, 586, 4, 587, 4, 589, 6, 590, 6, 592, 8, 593, 8, 601, 16, 601, 17, 603, 19, 603, 20, 605, 22, 605,
		24, 606, 25, 606, 26, 607, 27, 607, 29, 608, 30, 608, 33, 609, 34, 609, 343, 608, 344, 608, 347, 607, 348,
		607, 350, 606, 351, 606, 352, 605, 353, 605, 354, 604, 355, 604, 356, 603, 357, 603, 358, 600, 361, 600,
		362, 594, 368, 593, 368, 591, 370, 590, 370, 588, 372, 587, 372, 586, 373, 585, 373, 584, 374, 582, 374,
		581, 375, 579, 375, 578, 376, 574, 376, 573, 377, 36, 377, 35, 376, 31, 376, 30, 375, 28, 375, 27, 374, 26,
		374, 25, 373, 24, 373, 23, 372, 22, 372, 21, 371, 20, 371, 19, 370, 18, 370, 13, 365, 12, 365, 12, 364, 8,
		360, 8, 359, 6, 357, 6, 356, 5, 355, 5, 354, 4, 353, 4, 352, 3, 351, 3, 349, 2, 348, 2, 346, 1, 345, 1,
		341, 0, 340, 0, 36, 1, 35, 1, 31, 2, 30, 2, 28, 3, 27, 3, 25, 4, 24, 4, 23, 5, 22, 5, 21, 7, 19, 7, 18, 9,
		16, 9, 15, 15, 9, 16, 9, 19, 6, 20, 6, 21, 5, 22, 5, 23, 4, 24, 4, 25, 3, 26, 3, 27, 2, 29, 2, 30, 1, 33, 1 };
	private static final Point NORMAL_SCREEN_SIZE = new Point(318, 450);
	private static final Point NORMAL_SKIN_SIZE = new Point(378, 610);
	
	private static final int CORNERS_SIZE = 40;		
	
	private static final IPhoneSkinDescriptor VERTICAL_DESCRIPTOR;
	static {
		String bd = "ios/ipad/mini/vertical/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 5, 3, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "08.png"), iOsDescriptor,                                                                                                                                                                                                                  new ImageDescriptor(bd + "09.png"),
				new ImageDescriptor(bd + "10.png", 1, 1, SWT.VERTICAL),                                                                                                                                                                                                             new ImageDescriptor(bd + "11.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "12.png"), 																																																								new ImageDescriptor(bd + "13.png"),
				new ImageDescriptor(bd + "14.png"), new ImageDescriptor(bd + "15.png"), new ImageDescriptor(bd + "16.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "17.png"), new ImageDescriptor(bd + "18.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "19.png"), new ImageDescriptor(bd + "20.png"),
			};
		int bodyGridSize = 7;
		
		String bd2 = "ios/ipad/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 45);
			formData.bottom = new FormAttachment(0, 135);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 86);
			formData.bottom = new FormAttachment(0, 135);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		VERTICAL_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}
	private static final IPhoneSkinDescriptor HORIZONTAL_DESCRIPTOR;
	static {
		String bd = "ios/ipad/mini/horizontal/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 3, 5, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"),  new ImageDescriptor(bd + "05.png"),
				new ImageDescriptor(bd + "06.png"), iOsDescriptor,                                                                                new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "08.png", 1, 1, SWT.VERTICAL),                                                                           new ImageDescriptor(bd + "09.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "10.png"),                                                                                               new ImageDescriptor(bd + "11.png"),
				new ImageDescriptor(bd + "12.png", 1, 1, SWT.VERTICAL),                                                                           new ImageDescriptor(bd + "13.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "14.png"),                                                                                               new ImageDescriptor(bd + "15.png"),
				new ImageDescriptor(bd + "16.png"), new ImageDescriptor(bd + "17.png"), new ImageDescriptor(bd + "18.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "19.png"),  new ImageDescriptor(bd + "20.png")
		};
		int bodyGridSize = 5;
		String bd2 = "ios/ipad/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 95);
			formData.bottom = new FormAttachment(0, 85);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 136);
			formData.bottom = new FormAttachment(0, 85);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		HORIZONTAL_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}
	
	public AppleIPadMiniResizableSkin() {
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
		return new AppleIPadComposite(parent, skinDescriptor);
	}

	@Override
	public boolean automaticallyHideAddressBar() {
		return false;
	}

}
