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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * Allows to render composites using provided images.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
class CompositeImageDecorator extends ControlImageDecorator<Composite> {

	public CompositeImageDecorator(Composite control) {
		super(control);
	}

	@Override
	protected void setImage(Image image) {
		control.setBackgroundImage(image);
	}
}