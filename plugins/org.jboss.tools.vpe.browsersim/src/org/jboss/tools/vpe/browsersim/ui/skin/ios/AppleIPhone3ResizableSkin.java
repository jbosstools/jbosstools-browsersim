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
public class AppleIPhone3ResizableSkin extends ResizableSkin {
	public static final String IPHONE3_SKIN_ID = "iPhone 3";

	private static final int[] VISIBLE_REGION_VERTICAL = {
		62, 3, 257, 3, 258, 2, 258, 0, 320, 0, 320, 2, 321, 3, 330, 3, 331, 4, 334, 4, 335, 5, 338, 5, 339, 6, 341, 6,
		342, 7, 344, 7, 345, 8, 347, 8, 348, 9, 349, 9, 350, 10, 351, 10, 353, 12, 354, 12, 355, 13, 356, 13, 359, 16,
		360, 16, 368, 24, 368, 25, 371, 28, 371, 29, 373, 31, 373, 32, 374, 33, 374, 34, 375, 35, 375, 36, 376, 37,
		376, 38, 377, 39, 377, 41, 378, 42, 378, 43, 379, 44, 379, 46, 380, 47, 380, 50, 381, 51, 381, 54, 382, 55, 382,
		60, 383, 61, 383, 668, 382, 669, 382, 673, 381, 674, 381, 678, 380, 679, 380, 682, 379, 683, 379, 685, 378, 686, 378,
		687, 377, 688, 377, 690, 376, 691, 376, 692, 375, 693, 375, 694, 374, 695, 374, 696, 373, 697, 373, 698, 371, 700, 371,
		701, 368, 704, 368, 705, 360, 713, 359, 713, 356, 716, 355, 716, 354, 717, 353, 717, 351, 719, 350, 719, 349, 720, 348,
		720, 347, 721, 345, 721, 344, 722, 341, 722, 340, 723, 337, 723, 336, 724, 334, 724, 333, 725, 329, 725, 328, 726, 59,
		726, 58, 725, 54, 725, 53, 724, 47, 724, 46, 723, 44, 723, 43, 722, 41, 722, 40, 721, 39, 721, 38, 720, 37, 720, 36, 719,
		35, 719, 34, 718, 33, 718, 31, 716, 30, 716, 28, 714, 27, 714, 17, 704, 17, 703, 14, 700, 14, 699, 13, 698, 13, 697, 12,
		696, 12, 695, 11, 694, 11, 693, 10, 692, 10, 691, 9, 690, 9, 689, 8, 688, 8, 686, 7, 685, 7, 683, 6, 682, 6, 680, 5, 679,
		5, 676, 4, 675, 4, 670, 3, 669, 3, 660, 2, 659, 2, 258, 1, 257, 1, 256, 0, 255, 0, 220, 1, 219, 1, 212, 2, 211, 2, 177, 1,
		176, 1, 169, 0, 168, 0, 132, 2, 130, 2, 108, 3, 107, 2, 106, 1, 106, 1, 74, 2, 73, 2, 67, 3, 66, 3, 59, 4, 58, 4, 54, 5, 53,
		5, 50, 6, 49, 6, 47, 7, 46, 7, 44, 8, 43, 8, 41, 9, 40, 9, 39, 10, 38, 10, 37, 11, 36, 11, 35, 12, 34, 12, 33, 13, 32, 13, 31,
		14, 30, 14, 29, 17, 26, 17, 25, 26, 16, 27, 16, 30, 13, 31, 13, 33, 11, 34, 11, 35, 10, 36, 10, 37, 9, 38, 9, 39, 8, 41, 8,
		42, 7, 43, 7, 44, 6, 47, 6, 48, 5, 50, 5, 51, 4, 61, 4
	};
	private static final int[] VISIBLE_REGION_HORIZONTAL = {
		100, 0, 668, 0, 669, 1, 673, 1, 674, 2, 678, 2, 679, 3, 682, 3, 683, 4, 685, 4, 686, 5, 687, 5, 688, 6, 690, 6, 691, 7, 692, 7, 693, 8,
		694, 8, 695, 9, 696, 9, 697, 10, 698, 10, 700, 12, 701, 12, 704, 15, 705, 15, 713, 23, 713, 24, 716, 27, 716, 28, 717, 29, 717,
		30, 719, 32, 719, 33, 720, 34, 720, 35, 721, 36, 721, 38, 722, 39, 722, 42, 723, 43, 723, 46, 724, 47, 724, 49, 725, 50, 725, 54,
		726, 55, 726, 324, 725, 325, 725, 329, 724, 330, 724, 336, 723, 337, 723, 339, 722, 340, 722, 342, 721, 343, 721, 344, 720, 345,
		720, 346, 719, 347, 719, 348, 718, 349, 718, 350, 716, 352, 716, 353, 714, 355, 714, 356, 704, 366, 703, 366, 700, 369, 699, 369,
		698, 370, 697, 370, 696, 371, 695, 371, 694, 372, 693, 372, 692, 373, 691, 373, 690, 374, 689, 374, 688, 375, 686, 375, 685, 376,
		683, 376, 682, 377, 680, 377, 679, 378, 676, 378, 675, 379, 670, 379, 669, 380, 660, 380, 659, 381, 258, 381, 257, 382, 256, 382,
		255, 383, 220, 383, 219, 382, 212, 382, 211, 381, 177, 381, 176, 382, 169, 382, 168, 383, 132, 383, 130, 381, 108, 381, 107, 380,
		106, 381, 106, 382, 74, 382, 73, 381, 67, 381, 66, 380, 59, 380, 58, 379, 54, 379, 53, 378, 50, 378, 49, 377, 47, 377, 46, 376,
		44, 376, 43, 375, 41, 375, 40, 374, 39, 374, 38, 373, 37, 373, 36, 372, 35, 372, 34, 371, 33, 371, 32, 370, 31, 370, 30, 369, 29,
		369, 26, 366, 25, 366, 16, 357, 16, 356, 13, 353, 13, 352, 11, 350, 11, 349, 10, 348, 10, 347, 9, 346, 9, 345, 8, 344, 8, 342, 7,
		341, 7, 340, 6, 339, 6, 336, 5, 335, 5, 333, 4, 332, 4, 322, 3, 321, 3, 126, 2, 125, 0, 125, 0, 63, 2, 63, 3, 62, 3, 53, 4, 52,
		4, 49, 5, 48, 5, 45, 6, 44, 6, 42, 7, 41, 7, 39, 8, 38, 8, 36, 9, 35, 9, 34, 10, 33, 10, 32, 12, 30, 12, 29, 13, 28, 13, 27, 16,
		24, 16, 23, 24, 15, 25, 15, 28, 12, 29, 12, 31, 10, 32, 10, 33, 9, 34, 9, 35, 8, 36, 8, 37, 7, 38, 7, 39, 6, 41, 6, 42, 5, 43, 5,
		44, 4, 46, 4, 47, 3, 50, 3, 51, 2, 54, 2, 55, 1, 60, 1, 61, 0
	};
	private static final Point NORMAL_SCREEN_SIZE = new Point(320, 480);
	private static final Point NORMAL_SKIN_SIZE = new Point(384, 727);
			
	private static final int CORNERS_SIZE = 58;
	
	private static final IPhoneSkinDescriptor VERTICAL_IPHONE3_DESCRIPTOR;
	static {
		String bd = "ios/iphone3/vertical/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 5, 3, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "08.png"), iOsDescriptor,                                                                                                                                                                                                                  new ImageDescriptor(bd + "14.png"),
				new ImageDescriptor(bd + "21.png", 1, 1, SWT.VERTICAL),                                                                                                                                                                                                             new ImageDescriptor(bd + "22.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "23.png"),                                                                                                                                                                                                                                 new ImageDescriptor(bd + "24.png"),
				new ImageDescriptor(bd + "25.png"), new ImageDescriptor(bd + "26.png"), new ImageDescriptor(bd + "27.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "28.png"), new ImageDescriptor(bd + "29.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "30.png"), new ImageDescriptor(bd + "31.png"),
			};
		int bodyGridSize = 7;
		
		String bd2 = "ios/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 52);
			formData.bottom = new FormAttachment(100, -132);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 115);
			formData.bottom = new FormAttachment(100, -132);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		VERTICAL_IPHONE3_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}
	private static final IPhoneSkinDescriptor HORIZONTAL_IPHONE3_DESCRIPTOR;
	static {
		String bd = "ios/iphone3/horizontal/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 5, 5, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "08.png"), iOsDescriptor,                                                                                                                                                                                                                  new ImageDescriptor(bd + "10.png"),
				new ImageDescriptor(bd + "11.png", 1, 1, SWT.VERTICAL),                                                                                                                                                                                                             new ImageDescriptor(bd + "12.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "13.png"),                                                                                                                                                                                                                                 new ImageDescriptor(bd + "14.png"),
				new ImageDescriptor(bd + "15.png", 1, 1, SWT.VERTICAL),                                                                                                                                                                                                             new ImageDescriptor(bd + "16.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "17.png"),                                                                                                                                                                                                                                 new ImageDescriptor(bd + "18.png"),
				new ImageDescriptor(bd + "19.png"), new ImageDescriptor(bd + "20.png"), new ImageDescriptor(bd + "21.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "22.png"), new ImageDescriptor(bd + "23.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "24.png"), new ImageDescriptor(bd + "25.png"),
		};
		int bodyGridSize = 7;
		String bd2 = "ios/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 145);
			formData.bottom = new FormAttachment(100, -36);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 247);
			formData.bottom = new FormAttachment(100, -36);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		HORIZONTAL_IPHONE3_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}

	public AppleIPhone3ResizableSkin() {
		super(VISIBLE_REGION_HORIZONTAL, VISIBLE_REGION_VERTICAL, NORMAL_SCREEN_SIZE, NORMAL_SKIN_SIZE);
	}

	@Override
	protected DeviceComposite createDeviceComposite(Composite parent, boolean vertical) {
		IPhoneSkinDescriptor skinDescriptor;
		if (vertical) {
			skinDescriptor = VERTICAL_IPHONE3_DESCRIPTOR;
		} else {
			skinDescriptor = HORIZONTAL_IPHONE3_DESCRIPTOR;
		}
		
		return new AppleIPhoneComposite(parent, skinDescriptor);
	}

	@Override
	public boolean automaticallyHideAddressBar() {
		return true;
	}
	
	
}
