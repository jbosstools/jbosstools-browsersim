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
package org.jboss.tools.browsersim.ui.skin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#addListener(int, org.eclipse.swt.widgets.Listener)
	 */
	@Override
	public void addListener(int eventType, Listener listener) {
		super.addListener(eventType, listener);
		switch (eventType) {
		case SWT.MouseDown:
		case SWT.MouseUp:
		case SWT.MouseMove:
		case SWT.MouseExit:
			bodyComposite.addListener(eventType, listener);
			for (Control child : bodyComposite.getChildren()) {
				child.addListener(eventType, listener);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#removeListener(int, org.eclipse.swt.widgets.Listener)
	 */
	@Override
	public void removeListener(int eventType, Listener listener) {
		super.removeListener(eventType, listener);
		switch (eventType) {
		case SWT.MouseDown:
		case SWT.MouseUp:
		case SWT.MouseMove:
		case SWT.MouseExit:
			bodyComposite.removeListener(eventType, listener);
			for (Control child :bodyComposite.getChildren()) {
				child.removeListener(eventType, listener);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#setMenu(org.eclipse.swt.widgets.Menu)
	 */
	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		bodyComposite.setMenu(menu);
		for (Control child :bodyComposite.getChildren()) {
			child.setMenu(menu);
		}
	}
	
	public boolean isDeviceBody(Control contol) {
		// XXX: simple implementation, there are some cases when this won't work
		if (contol instanceof Composite) {
			Composite composite = (Composite) contol;
			if (composite.getParent() == bodyComposite && composite.getBackgroundImage() != null) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isDeviceCorner(Point displayPoint) {
		Point bodyPoint = bodyComposite.toControl(displayPoint);
		Point bodySize = bodyComposite.getSize();
		int cornersSize = getCornersSize();
		Rectangle leftTopCorner = new Rectangle(0, 0, cornersSize, cornersSize);
		Rectangle rightTopCorner = new Rectangle(bodySize.x - cornersSize, 0, cornersSize, cornersSize);
		Rectangle leftBottomCorner = new Rectangle(0, bodySize.y - cornersSize, cornersSize, cornersSize);
		Rectangle rightBottomCorner = new Rectangle(bodySize.x - cornersSize, bodySize.y - cornersSize, cornersSize, cornersSize);		
		
		return leftTopCorner.contains(bodyPoint) || rightTopCorner.contains(bodyPoint)
				|| leftBottomCorner.contains(bodyPoint) || rightBottomCorner.contains(bodyPoint); 
	}
	
	protected abstract int getCornersSize();
	public abstract ImageButtonComposite getHomeButtonComposite();
}
