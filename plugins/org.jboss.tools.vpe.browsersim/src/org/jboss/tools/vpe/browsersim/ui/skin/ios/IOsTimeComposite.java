package org.jboss.tools.vpe.browsersim.ui.skin.ios;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.ui.skin.AbstractTimeComposite;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageList;


public class IOsTimeComposite extends AbstractTimeComposite {
	private static final String BD = "ios/";
	private static final ImageDescriptor[] BODY_DESCRIPTOR = {
		new ImageDescriptor(BD + "time-1.png"), new ImageDescriptor(BD + "time-2.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(BD + "time-3.png"), new ImageDescriptor(BD + "time-4.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(BD + "time-5.png")
	};
	
	public IOsTimeComposite(Composite parent, ImageList imageList) {
		super(parent, imageList);
		System.out.println("ss");
	}

	protected ImageDescriptor[] getBodyDescriptor() {
		return BODY_DESCRIPTOR;
	}
}
