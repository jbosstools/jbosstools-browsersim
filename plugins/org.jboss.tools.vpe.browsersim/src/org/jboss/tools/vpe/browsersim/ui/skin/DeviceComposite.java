package org.jboss.tools.vpe.browsersim.ui.skin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

public abstract class DeviceComposite extends Composite {
	protected Composite bodyComposite;

	public DeviceComposite(Composite parent, int style) {
		super(parent, style);
	}

	public abstract ImageButtonComposite getBackButtonComposite();

	public abstract ImageButtonComposite getForwardButtonComposite();

	public abstract Composite getBrowserContainer();

	public abstract void setNavBarCompositeVisible(boolean visible);

	public abstract boolean isNavBarCompositeVisible();

	public abstract ImageButtonComposite getStopButtonComposite();

	public abstract ImageButtonComposite getRefreshButtonComposite();

	public abstract Text getUrlText();

	public abstract ProgressBar getProgressBar();

	public abstract StyledText getPageTitleStyledText();

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.browsersim.ui.skin.ios.PhoneComposite#addListener(int, org.eclipse.swt.widgets.Listener)
	 */
	@Override
	public void addListener(int eventType, Listener listener) {
		super.addListener(eventType, listener);
		switch (eventType) {
		case SWT.MouseDown:
		case SWT.MouseUp:
		case SWT.MouseMove:
			bodyComposite.addListener(eventType, listener);
			for (Control child : bodyComposite.getChildren()) {
				child.addListener(eventType, listener);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.browsersim.ui.skin.ios.PhoneComposite#removeListener(int, org.eclipse.swt.widgets.Listener)
	 */
	@Override
	public void removeListener(int eventType, Listener listener) {
		super.removeListener(eventType, listener);
		switch (eventType) {
		case SWT.MouseDown:
		case SWT.MouseUp:
		case SWT.MouseMove:
			bodyComposite.removeListener(eventType, listener);
			for (Control child :bodyComposite.getChildren()) {
				child.removeListener(eventType, listener);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.browsersim.ui.skin.ios.PhoneComposite#setMenu(org.eclipse.swt.widgets.Menu)
	 */
	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		bodyComposite.setMenu(menu);
		for (Control child :bodyComposite.getChildren()) {
			child.setMenu(menu);
		}
	}

	public abstract ImageButtonComposite getHomeButtonComposite();
}
