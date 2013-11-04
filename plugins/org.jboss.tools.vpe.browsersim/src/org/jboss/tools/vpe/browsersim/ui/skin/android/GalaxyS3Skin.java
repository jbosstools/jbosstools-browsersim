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
public class GalaxyS3Skin  extends ResizableSkin {
	public static final String ANDROID_SKIN_ID = "Galaxy S III";

	private static final int[] VISIBLE_REGION_VERTICAL = { 146, 0, 280, 0, 281, 1, 302, 1, 303, 2, 318, 2, 319, 3, 330,
		3, 331, 4, 339, 4, 340, 5, 347, 5, 348, 6, 353, 6, 354, 7, 357, 7, 358, 8, 361, 8, 362, 9, 364, 9, 365, 10,
		367, 10, 368, 11, 370, 11, 371, 12, 373, 12, 374, 13, 375, 13, 376, 14, 378, 14, 379, 15, 380, 15, 381, 16,
		382, 16, 383, 17, 384, 17, 385, 18, 386, 18, 388, 20, 389, 20, 391, 22, 392, 22, 394, 24, 395, 24, 404, 33,
		404, 34, 407, 37, 407, 38, 409, 40, 409, 41, 410, 42, 410, 43, 412, 45, 412, 46, 413, 47, 413, 49, 414, 50,
		414, 51, 415, 52, 415, 54, 416, 55, 416, 57, 417, 58, 417, 61, 418, 62, 418, 65, 419, 66, 419, 71, 420, 72,
		420, 79, 421, 80, 421, 90, 422, 91, 422, 115, 423, 116, 423, 199, 424, 200, 425, 200, 427, 202, 427, 259,
		425, 261, 425, 429, 424, 430, 424, 466, 423, 467, 423, 478, 422, 479, 422, 486, 421, 487, 421, 492, 420,
		493, 420, 497, 419, 498, 419, 501, 418, 502, 418, 504, 417, 505, 417, 507, 416, 508, 416, 510, 415, 511,
		415, 513, 414, 514, 414, 515, 413, 516, 413, 517, 412, 518, 412, 519, 411, 520, 411, 521, 410, 522, 410,
		523, 409, 524, 409, 525, 407, 527, 407, 528, 405, 530, 405, 531, 401, 535, 401, 536, 394, 543, 393, 543,
		390, 546, 389, 546, 387, 548, 386, 548, 384, 550, 383, 550, 381, 552, 380, 552, 379, 553, 378, 553, 377,
		554, 376, 554, 375, 555, 374, 555, 373, 556, 371, 556, 370, 557, 369, 557, 368, 558, 366, 558, 365, 559,
		364, 559, 363, 560, 361, 560, 360, 561, 358, 561, 357, 562, 354, 562, 353, 563, 350, 563, 349, 564, 346,
		564, 345, 565, 341, 565, 340, 566, 333, 566, 332, 567, 325, 567, 324, 568, 313, 568, 312, 569, 297, 569,
		296, 570, 279, 570, 278, 571, 146, 571, 145, 570, 127, 570, 126, 569, 113, 569, 112, 568, 104, 568, 103,
		567, 95, 567, 94, 566, 89, 566, 88, 565, 83, 565, 82, 564, 78, 564, 77, 563, 74, 563, 73, 562, 71, 562, 70,
		561, 68, 561, 67, 560, 65, 560, 64, 559, 63, 559, 62, 558, 60, 558, 59, 557, 58, 557, 57, 556, 56, 556, 55,
		555, 54, 555, 53, 554, 52, 554, 50, 552, 49, 552, 48, 551, 47, 551, 45, 549, 44, 549, 42, 547, 41, 547, 37,
		543, 36, 543, 27, 534, 27, 533, 24, 530, 24, 529, 22, 527, 22, 526, 21, 525, 21, 524, 19, 522, 19, 521, 18,
		520, 18, 519, 17, 518, 17, 517, 16, 516, 16, 514, 15, 513, 15, 512, 14, 511, 14, 509, 13, 508, 13, 507, 12,
		506, 12, 503, 11, 502, 11, 500, 10, 499, 10, 496, 9, 495, 9, 490, 8, 489, 8, 483, 7, 482, 7, 472, 6, 471,
		6, 446, 5, 445, 5, 401, 4, 400, 4, 286, 3, 285, 2, 285, 1, 284, 1, 283, 0, 282, 0, 169, 1, 168, 1, 167, 2,
		166, 3, 166, 4, 165, 4, 164, 5, 164, 6, 163, 6, 92, 7, 91, 7, 78, 8, 77, 8, 70, 9, 69, 9, 64, 10, 63, 10,
		60, 11, 59, 11, 57, 12, 56, 12, 54, 13, 53, 13, 51, 14, 50, 14, 49, 15, 48, 15, 47, 16, 46, 16, 45, 17, 44,
		17, 43, 19, 41, 19, 40, 20, 39, 20, 38, 23, 35, 23, 34, 33, 24, 34, 24, 36, 22, 37, 22, 39, 20, 40, 20, 42,
		18, 43, 18, 44, 17, 45, 17, 46, 16, 47, 16, 48, 15, 49, 15, 50, 14, 51, 14, 52, 13, 54, 13, 55, 12, 56, 12,
		57, 11, 59, 11, 60, 10, 63, 10, 64, 9, 66, 9, 67, 8, 70, 8, 71, 7, 75, 7, 76, 6, 82, 6, 83, 5, 89, 5, 90,
		4, 98, 4, 99, 3, 110, 3, 111, 2, 124, 2, 125, 1, 145, 1 };
	private static final int[] VISIBLE_REGION_HORIZONTAL = { 202, 0, 259, 0, 261, 2, 429, 2, 430, 3, 466, 3, 467, 4,
		478, 4, 479, 5, 486, 5, 487, 6, 492, 6, 493, 7, 497, 7, 498, 8, 501, 8, 502, 9, 504, 9, 505, 10, 507, 10,
		508, 11, 510, 11, 511, 12, 513, 12, 514, 13, 515, 13, 516, 14, 517, 14, 518, 15, 519, 15, 520, 16, 521, 16,
		522, 17, 523, 17, 524, 18, 525, 18, 527, 20, 528, 20, 530, 22, 531, 22, 535, 26, 536, 26, 543, 33, 543, 34,
		546, 37, 546, 38, 548, 40, 548, 41, 550, 43, 550, 44, 552, 46, 552, 47, 553, 48, 553, 49, 554, 50, 554, 51,
		555, 52, 555, 53, 556, 54, 556, 56, 557, 57, 557, 58, 558, 59, 558, 61, 559, 62, 559, 63, 560, 64, 560, 66,
		561, 67, 561, 69, 562, 70, 562, 73, 563, 74, 563, 77, 564, 78, 564, 81, 565, 82, 565, 86, 566, 87, 566, 94,
		567, 95, 567, 102, 568, 103, 568, 114, 569, 115, 569, 130, 570, 131, 570, 148, 571, 149, 571, 281, 570,
		282, 570, 300, 569, 301, 569, 314, 568, 315, 568, 323, 567, 324, 567, 332, 566, 333, 566, 338, 565, 339,
		565, 344, 564, 345, 564, 349, 563, 350, 563, 353, 562, 354, 562, 356, 561, 357, 561, 359, 560, 360, 560,
		362, 559, 363, 559, 364, 558, 365, 558, 367, 557, 368, 557, 369, 556, 370, 556, 371, 555, 372, 555, 373,
		554, 374, 554, 375, 552, 377, 552, 378, 551, 379, 551, 380, 549, 382, 549, 383, 547, 385, 547, 386, 543,
		390, 543, 391, 534, 400, 533, 400, 530, 403, 529, 403, 527, 405, 526, 405, 525, 406, 524, 406, 522, 408,
		521, 408, 520, 409, 519, 409, 518, 410, 517, 410, 516, 411, 514, 411, 513, 412, 512, 412, 511, 413, 509,
		413, 508, 414, 507, 414, 506, 415, 503, 415, 502, 416, 500, 416, 499, 417, 496, 417, 495, 418, 490, 418,
		489, 419, 483, 419, 482, 420, 472, 420, 471, 421, 446, 421, 445, 422, 286, 422, 285, 423, 285, 425, 284,
		426, 283, 426, 282, 427, 169, 427, 169, 426, 168, 425, 166, 425, 166, 424, 165, 423, 164, 423, 164, 422,
		163, 421, 92, 421, 91, 420, 78, 420, 77, 419, 70, 419, 69, 418, 64, 418, 63, 417, 60, 417, 59, 416, 57,
		416, 56, 415, 54, 415, 53, 414, 51, 414, 50, 413, 49, 413, 48, 412, 47, 412, 46, 411, 45, 411, 44, 410, 43,
		410, 41, 408, 40, 408, 39, 407, 38, 407, 35, 404, 34, 404, 24, 394, 24, 393, 22, 391, 22, 390, 20, 388, 20,
		387, 18, 385, 18, 384, 17, 383, 17, 382, 16, 381, 16, 380, 15, 379, 15, 378, 14, 377, 14, 376, 13, 375, 13,
		373, 12, 372, 12, 371, 11, 370, 11, 368, 10, 367, 10, 364, 9, 363, 9, 361, 8, 360, 8, 357, 7, 356, 7, 352,
		6, 351, 6, 345, 5, 344, 5, 338, 4, 337, 4, 329, 3, 328, 3, 317, 2, 316, 2, 303, 1, 302, 1, 282, 0, 281, 0,
		147, 1, 146, 1, 125, 2, 124, 2, 109, 3, 108, 3, 97, 4, 96, 4, 88, 5, 87, 5, 80, 6, 79, 6, 74, 7, 73, 7, 70,
		8, 69, 8, 66, 9, 65, 9, 63, 10, 62, 10, 60, 11, 59, 11, 57, 12, 56, 12, 54, 13, 53, 13, 52, 14, 51, 14, 49,
		15, 48, 15, 47, 16, 46, 16, 45, 17, 44, 17, 43, 18, 42, 18, 41, 20, 39, 20, 38, 22, 36, 22, 35, 24, 33, 24,
		32, 33, 23, 34, 23, 37, 20, 38, 20, 40, 18, 41, 18, 42, 17, 43, 17, 45, 15, 46, 15, 47, 14, 49, 14, 50, 13,
		51, 13, 52, 12, 54, 12, 55, 11, 57, 11, 58, 10, 61, 10, 62, 9, 65, 9, 66, 8, 71, 8, 72, 7, 79, 7, 80, 6,
		90, 6, 91, 5, 115, 5, 116, 4, 199, 4, 200, 3, 200, 2 };
	private static final Point NORMAL_SCREEN_SIZE = new Point(360, 398);
	private static final Point NORMAL_SKIN_SIZE = new Point(428, 572);
	
	private static final int CORNERS_SIZE = 60;
	private static final AndroidSkinDescriptor VERTICAL_DESCRIPTOR;
	static {
		String bd = "android/galaxyS3/vertical/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 3, 3, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png"), 
				new ImageDescriptor(bd + "06.png"), iOsDescriptor, new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "08.png", 1, 1, SWT.VERTICAL), new ImageDescriptor(bd + "09.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "10.png"), new ImageDescriptor(bd + "11.png"),
				new ImageDescriptor(bd + "12.png"), new ImageDescriptor(bd + "13.png"), new ImageDescriptor(bd + "14.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "15.png"),  new ImageDescriptor(bd + "16.png")
			};
		int bodyGridSize = 5;
		
		String bd2 = "android/galaxyS3/buttons/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(100, -110);
			formData.bottom = new FormAttachment(100, -45);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor homeButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(50, -45);
			formData.bottom = new FormAttachment(100, -30);
			homeButtonDescriptor = new ButtonDescriptor(formData, bd2 + "home.png", bd2 + "home.png", bd2 + "home-selected.png");
		}
		ButtonDescriptor refreshButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 90);
			formData.bottom = new FormAttachment(100, -38);
			refreshButtonDescriptor = new ButtonDescriptor(formData, bd2 + "refresh.png", bd2 + "refresh.png", bd2 + "refresh-selected.png");
		}
		VERTICAL_DESCRIPTOR = new AndroidSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE,
				backButtonDescriptor, null, homeButtonDescriptor, refreshButtonDescriptor);
	}
	
	private static final AndroidSkinDescriptor HORIZONTAL_DESCRIPTOR;
	static {
		String bd = "android/galaxyS3/horizontal/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 3, 3, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png"),
				new ImageDescriptor(bd + "06.png"), iOsDescriptor, new ImageDescriptor(bd + "07.png"), 
				new ImageDescriptor(bd + "08.png", 1, 1, SWT.VERTICAL), new ImageDescriptor(bd + "09.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "10.png"), new ImageDescriptor(bd + "11.png"),
				new ImageDescriptor(bd + "12.png"), new ImageDescriptor(bd + "13.png"), new ImageDescriptor(bd + "14.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "15.png"), new ImageDescriptor(bd + "16.png")	
		};
		int bodyGridSize = 5;
		String bd2 = "android/galaxyS3/buttons/";
		
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.right = new FormAttachment(100, -40);
			formData.bottom = new FormAttachment(0, 110);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back-horizontal.png", bd2 + "back-horizontal-disabled.png", bd2 + "back-horizontal-selected.png");
		}
		
		ButtonDescriptor homeButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.right = new FormAttachment(100, -30);
			formData.bottom = new FormAttachment(50, 45 );
			homeButtonDescriptor = new ButtonDescriptor(formData, bd2 + "home-horizontal.png", bd2 + "home-horizontal.png", bd2 + "home-horizontal-selected.png");
		}
		ButtonDescriptor refreshButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.right = new FormAttachment(100, -38);
			formData.bottom = new FormAttachment(100, -90);
			refreshButtonDescriptor = new ButtonDescriptor(formData, bd2 + "refresh-horizontal.png", bd2 + "refresh-horizontal.png", bd2 + "refresh-horizontal-selected.png");
		}

		HORIZONTAL_DESCRIPTOR = new AndroidSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE,
				backButtonDescriptor, null, homeButtonDescriptor, refreshButtonDescriptor);
	}

	public GalaxyS3Skin() {
		super(VISIBLE_REGION_HORIZONTAL, VISIBLE_REGION_VERTICAL, NORMAL_SCREEN_SIZE, NORMAL_SKIN_SIZE);
	}

	@Override
	protected DeviceComposite createDeviceComposite(Composite parent, boolean vertical) {
		return new AndroidComposite(parent, vertical ? VERTICAL_DESCRIPTOR : HORIZONTAL_DESCRIPTOR);
	}

	@Override
	public boolean automaticallyHideAddressBar() {
		return true;
	}
}
