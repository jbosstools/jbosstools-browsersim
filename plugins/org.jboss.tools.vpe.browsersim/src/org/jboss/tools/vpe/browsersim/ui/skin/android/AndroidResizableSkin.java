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
		int[] normalRegion = new int[8];
		normalRegion[0] = 0;
		normalRegion[1] = 0;
		
		normalRegion[2] = 339;
		normalRegion[3] = 0;
		
		normalRegion[4] = 339;
		normalRegion[5] = 186;
		
		normalRegion[6] = 0;
		normalRegion[7] = 186;
		
		return normalRegion;
	}
}
