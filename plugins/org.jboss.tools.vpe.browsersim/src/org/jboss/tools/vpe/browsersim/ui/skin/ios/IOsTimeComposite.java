package org.jboss.tools.vpe.browsersim.ui.skin.ios;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageList;


public class IOsTimeComposite extends Composite {
	private final String BD = "ios/";
	private final ImageDescriptor[] BODY_DESCRIPTOR = {
		new ImageDescriptor(BD + "time-1.png"), new ImageDescriptor(BD + "time-2.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(BD + "time-3.png"), new ImageDescriptor(BD + "time-4.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(BD + "time-5.png")
	};

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public IOsTimeComposite(Composite parent, ImageList imageList) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(5, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		for (ImageDescriptor descriptor : BODY_DESCRIPTOR) {
			descriptor.createWidget(this, imageList);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	@Override
	public void addListener(int eventType, Listener listener) {
		super.addListener(eventType, listener);
		switch (eventType) {
		case SWT.MouseDown:
		case SWT.MouseUp:
		case SWT.MouseMove:
			for (Control child : getChildren()) {
				child.addListener(eventType, listener);
			}
		}
	}
	
	@Override
	public void removeListener(int eventType, Listener listener) {
		super.removeListener(eventType, listener);
		switch (eventType) {
		case SWT.MouseDown:
		case SWT.MouseUp:
		case SWT.MouseMove:
			for (Control child : getChildren()) {
				child.removeListener(eventType, listener);
			}
		}
	}
}
