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
package org.jboss.tools.browsersim.ui.skin.android;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.browsersim.ui.skin.AbstractTimeComposite;
import org.jboss.tools.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.browsersim.ui.util.BrowserSimImageList;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
@SuppressWarnings("nls")
public class AndroidTimeComposite extends AbstractTimeComposite {
	private static final String BD = "android/";
	private static final ImageDescriptor[] BODY_DESCRIPTOR = {
		new ImageDescriptor(BD + "time-1.png"), new ImageDescriptor(BD + "time-2.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(BD + "time-3.png")
	};

	public AndroidTimeComposite(Composite parent, BrowserSimImageList imageList) {
		super(parent, imageList);
	}

	protected ImageDescriptor[] getBodyDescriptor() {
		return BODY_DESCRIPTOR;
	}
}
