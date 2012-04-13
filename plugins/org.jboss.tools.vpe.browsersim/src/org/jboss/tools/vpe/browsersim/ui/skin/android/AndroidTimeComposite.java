package org.jboss.tools.vpe.browsersim.ui.skin.android;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.ui.skin.AbstractTimeComposite;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageList;

public class AndroidTimeComposite extends AbstractTimeComposite {
	private static final String BD = "android/";
	private static final ImageDescriptor[] BODY_DESCRIPTOR = {
		new ImageDescriptor(BD + "time-1.png"), new ImageDescriptor(BD + "time-2.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(BD + "time-3.png")
	};

	public AndroidTimeComposite(Composite parent, ImageList imageList) {
		super(parent, imageList);
	}

	protected ImageDescriptor[] getBodyDescriptor() {
		return BODY_DESCRIPTOR;
	}
}
