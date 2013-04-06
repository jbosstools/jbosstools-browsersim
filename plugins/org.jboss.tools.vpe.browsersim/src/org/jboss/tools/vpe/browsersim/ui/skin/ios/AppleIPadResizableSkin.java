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
 * @author Ilya Buziuk (ibuziuk)
 */
@SuppressWarnings("nls")
public class AppleIPadResizableSkin extends ResizableSkin {
	public static final String IPAD_SKIN_ID = "iPad";

	private static final int[] VISIBLE_REGION_VERTICAL = { 37, 0, 341, 0, 342, 1, 346, 1, 347, 2, 349, 2, 350, 3, 352,
		3, 353, 4, 354, 4, 355, 5, 356, 5, 358, 7, 359, 7, 361, 9, 362, 9, 368, 15, 368, 16, 371, 19, 371, 20, 372,
		21, 372, 22, 373, 23, 373, 24, 374, 25, 374, 26, 375, 27, 375, 29, 376, 30, 376, 33, 377, 34, 377, 574,
		376, 575, 376, 577, 375, 578, 375, 580, 374, 581, 374, 583, 373, 584, 373, 585, 371, 587, 371, 588, 369,
		590, 369, 591, 361, 599, 360, 599, 358, 601, 357, 601, 355, 603, 353, 603, 352, 604, 351, 604, 350, 605,
		348, 605, 347, 606, 344, 606, 343, 607, 34, 607, 33, 606, 30, 606, 29, 605, 27, 605, 26, 604, 25, 604, 24,
		603, 23, 603, 22, 602, 21, 602, 20, 601, 19, 601, 16, 598, 15, 598, 9, 592, 9, 591, 7, 589, 7, 588, 5, 586,
		5, 585, 4, 584, 4, 583, 3, 582, 3, 580, 2, 579, 2, 577, 1, 576, 1, 572, 0, 571, 0, 36, 1, 35, 1, 31, 2, 30,
		2, 28, 3, 27, 3, 26, 4, 25, 4, 24, 5, 23, 5, 22, 6, 21, 6, 20, 7, 19, 7, 18, 12, 13, 12, 12, 13, 12, 17, 8,
		18, 8, 20, 6, 21, 6, 22, 5, 23, 5, 24, 4, 25, 4, 26, 3, 28, 3, 29, 2, 31, 2, 32, 1, 36, 1 }; 
	private static final int[] VISIBLE_REGION_HORIZONTAL = new int[] { 34, 0, 574, 0, 575, 1, 577, 1, 578, 2, 580, 2, 581, 3,
		583, 3, 584, 4, 585, 4, 587, 6, 588, 6, 590, 8, 591, 8, 599, 16, 599, 17, 601, 19, 601, 20, 603, 22, 603,
		24, 604, 25, 604, 26, 605, 27, 605, 29, 606, 30, 606, 33, 607, 34, 607, 343, 606, 344, 606, 347, 605, 348,
		605, 350, 604, 351, 604, 352, 603, 353, 603, 354, 602, 355, 602, 356, 601, 357, 601, 358, 598, 361, 598,
		362, 592, 368, 591, 368, 589, 370, 588, 370, 586, 372, 585, 372, 584, 373, 583, 373, 582, 374, 580, 374,
		579, 375, 577, 375, 576, 376, 572, 376, 571, 377, 36, 377, 35, 376, 31, 376, 30, 375, 28, 375, 27, 374, 26,
		374, 25, 373, 24, 373, 23, 372, 22, 372, 21, 371, 20, 371, 19, 370, 18, 370, 13, 365, 12, 365, 12, 364, 8,
		360, 8, 359, 6, 357, 6, 356, 5, 355, 5, 354, 4, 353, 4, 352, 3, 351, 3, 349, 2, 348, 2, 346, 1, 345, 1,
		341, 0, 340, 0, 36, 1, 35, 1, 31, 2, 30, 2, 28, 3, 27, 3, 25, 4, 24, 4, 23, 5, 22, 5, 21, 7, 19, 7, 18, 9,
		16, 9, 15, 15, 9, 16, 9, 19, 6, 20, 6, 21, 5, 22, 5, 23, 4, 24, 4, 25, 3, 26, 3, 27, 2, 29, 2, 30, 1, 33, 1 };
	private static final Point NORMAL_SCREEN_SIZE = new Point(226, 448);
	private static final Point NORMAL_SKIN_SIZE = new Point(378, 608);
	
	private static final int CORNERS_SIZE = 40;
	
	private static final IPhoneSkinDescriptor VERTICAL_DESCRIPTOR;
	static {
		String bd = "ios/ipad/vertical/";
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
			formData.left = new FormAttachment(0, 90);
			formData.bottom = new FormAttachment(0, 135);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 131);
			formData.bottom = new FormAttachment(0, 135);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		VERTICAL_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}
	private static final IPhoneSkinDescriptor HORIZONTAL_DESCRIPTOR;
	static {
		String bd = "ios/ipad/horizontal/";
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
			formData.left = new FormAttachment(0, 92);
			formData.bottom = new FormAttachment(0, 131);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 133);
			formData.bottom = new FormAttachment(0, 131);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		HORIZONTAL_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
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
	
	public AppleIPadResizableSkin() {
		super(VISIBLE_REGION_HORIZONTAL, VISIBLE_REGION_VERTICAL, NORMAL_SCREEN_SIZE, NORMAL_SKIN_SIZE);
	}

	@Override
	public void setAddressBarVisible(boolean visible) {
		return;
	}
}
