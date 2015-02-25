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
package org.jboss.tools.browsersim.util;

import java.io.InputStream;

import org.eclipse.swt.widgets.Widget;

/**
 * A cache of images. All created images are disposed automatically
 * together with given {@link #disposable}.  
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSimImageList extends ImageList {

	public BrowserSimImageList(Widget disposable) {
		super(disposable);
	}

	@Override
	public InputStream getResourceAsStream(String location) {
		return BrowserSimResourcesUtil.getResourceAsStream(location);
	}

}
