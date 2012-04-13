package org.jboss.tools.vpe.browsersim.ui.skin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

public abstract class AbstractTimeComposite extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractTimeComposite(Composite parent, ImageList imageList) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(5, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		for (ImageDescriptor descriptor : getBodyDescriptor()) {
			descriptor.createWidget(this, imageList);
		}
	}

	protected abstract ImageDescriptor[] getBodyDescriptor();

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
