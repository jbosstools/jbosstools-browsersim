package org.jboss.tools.vpe.browsersim.ui.skin.android;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.AppleIPhone3ResizableSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.ButtonDescriptor;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.IPhoneSkinDescriptor;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.ImageDescriptor;

public class AndroidResizableSkin  extends AppleIPhone3ResizableSkin {
	private static final Point NORMAL_SKREEN_SIZE = new Point(240, 1);
	private static final Point NORMAL_SKIN_SIZE = new Point(339, 186);
	public static final int[] VISIBLE_REGION_VERTICAL = {20, 0, 318, 0, 319, 1, 321, 1, 322, 2, 323, 2, 325, 4, 326, 4,
		328, 6, 329, 6, 332, 9, 332, 10, 335, 13, 335, 14, 336, 15, 336, 17, 337, 18, 337, 20, 338, 21, 338, 163, 337,
		164, 337, 166, 336, 167, 336, 169, 335, 170, 335, 171, 333, 173, 333, 174, 327, 180, 326, 180, 325, 181, 324,
		181, 323, 182, 322, 182, 321, 183, 320, 183, 318, 185, 20, 185, 18, 183, 17, 183, 16, 182, 15, 182, 14, 181,
		13, 181, 12, 180, 11, 180, 5, 174, 5, 173, 3, 171, 3, 170, 2, 169, 2, 167, 1, 166, 1, 164, 0, 163, 0, 21, 1,
		20, 1, 18, 2, 17, 2, 15, 3, 14, 3, 13, 6, 10, 6, 9, 7, 9, 9, 7, 9, 6, 10, 6, 12, 4, 13, 4, 15, 2, 16, 2, 17, 1,
		19, 1};
	private static final Point VERTICAL_BORDERS_SIZE = new Point(NORMAL_SKIN_SIZE.x - NORMAL_SKREEN_SIZE.x, NORMAL_SKIN_SIZE.y - NORMAL_SKREEN_SIZE.y);
	private static final Point HORIZONTAL_BORDERS_SIZE = new Point(VERTICAL_BORDERS_SIZE.y, VERTICAL_BORDERS_SIZE.x);
	private static final IPhoneSkinDescriptor VERTICAL_IPHONE3_DESCRIPTOR;
	static {
		String bd = "android/droid/vertical/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 3, 1, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "03.png"), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "14.png", 1, 1, SWT.VERTICAL), iOsDescriptor,                                                                                                new ImageDescriptor(bd + "16.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "17.png"), new ImageDescriptor(bd + "18.png"), new ImageDescriptor(bd + "19.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "20.png"), new ImageDescriptor(bd + "21.png")
			};
		int bodyGridSize = 5;
		
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
		VERTICAL_IPHONE3_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, backButtonDescriptor, forwardButtonDescriptor);
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
		HORIZONTAL_IPHONE3_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, backButtonDescriptor, forwardButtonDescriptor);
	}
	
	@Override
	protected Point getBordersSize(boolean vertical) {
		Point bordersSize = vertical ? VERTICAL_BORDERS_SIZE : HORIZONTAL_BORDERS_SIZE;
		return bordersSize;
	}

	@Override
	protected IPhoneSkinDescriptor getSkinDescriptor(boolean vertical) {
		IPhoneSkinDescriptor skinDescriptor;
		if (vertical) {
			skinDescriptor = VERTICAL_IPHONE3_DESCRIPTOR;
		} else {
			skinDescriptor = HORIZONTAL_IPHONE3_DESCRIPTOR;
		}
		return skinDescriptor;
	}
	
	@Override
	protected int[] getNormalRegion(boolean vertical) {
		return VISIBLE_REGION_VERTICAL;
	}
}
