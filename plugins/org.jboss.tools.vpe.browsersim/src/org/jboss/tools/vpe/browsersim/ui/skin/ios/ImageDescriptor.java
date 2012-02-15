package org.jboss.tools.vpe.browsersim.ui.skin.ios;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageList;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.WidgetDescriptor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;


class ImageDescriptor extends WidgetDescriptor {
	protected String imageName;

	public ImageDescriptor(String imageName, int horizontalSpan, int verticalSpan, int stretching) {
		super(horizontalSpan, verticalSpan, stretching);
		this.imageName = imageName;
	}
	public ImageDescriptor(String imageName, int horizontalSpan, int verticalSpan) {
		this(imageName, horizontalSpan, verticalSpan, SWT.NONE);
	}
	public ImageDescriptor(String imageName) {
		this(imageName, 1, 1);
	}
	
	public Composite createWidget(Composite parent, ImageList imageList) {
		Composite composite = new Composite(parent, SWT.NONE);
		boolean fillHorizontal = (getStretching() & SWT.HORIZONTAL) != 0;
		boolean fillVertical = (getStretching() & SWT.VERTICAL) != 0;
		GridData gridData = new GridData(
				fillHorizontal ? SWT.FILL: SWT.CENTER,
				fillVertical ? SWT.FILL: SWT.CENTER,
				fillHorizontal, fillVertical,
				getHorizontalSpan(), getVerticalSpan());
		
		String imageName = getImageName();
		if (imageName != null) {
			Image image = imageList.getImage(imageName);
			composite.setBackgroundImage(image);
			Rectangle bounds = image.getBounds();
			gridData.widthHint = fillHorizontal ? SWT.DEFAULT : bounds.width;
			gridData.heightHint = fillVertical ? SWT.DEFAULT : bounds.height;
		} else {
			composite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		}
		
		composite.setLayoutData(gridData);
		
		return composite;
	}
	
	public String getImageName() {
		return imageName;
	}
}
