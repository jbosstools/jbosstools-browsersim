/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.browsersim.browser.PlatformUtil;
import org.jboss.tools.browsersim.util.BrowserSimUtil;

public class AdressBarText extends Text {
	private Font font;
	public static final int adressBarUrlOffset = (SWT.getPlatform().equals("gtk")) ? 2 : 0; //XXX fixing url centering in the adress bar under linux  //$NON-NLS-1$
	public AdressBarText(Composite parent, int style) {
		super(parent, style);
		font = createFont(this.getDisplay(), SWT.NORMAL);
		setFont(font);
		BrowserSimUtil.addDisposeListener(this, font);
	}

	public static Font createFont(Device device, int style) {
		Font font;
		if (SWT.getPlatform().equals(PlatformUtil.OS_WIN32)) {
			font = new Font(device, "Arial", 10, style); //$NON-NLS-1$
		} else if (SWT.getPlatform().equals("gtk")) { //$NON-NLS-1$
			font = new Font(device, "Helvetica", 10, style); //$NON-NLS-1$
		} else if (SWT.getPlatform().equals("cocoa")) { //$NON-NLS-1$
			font = new Font(device, "Helvetica", 12, style); //$NON-NLS-1$
		} else {
			font = new Font(device, "Verdana", 9, style); //$NON-NLS-1$
		}

		return font;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
