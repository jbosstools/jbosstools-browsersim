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
package org.jboss.tools.browsersim.ui.events;

import java.util.EventObject;

import org.jboss.tools.browsersim.ui.BrowserSim;
import org.jboss.tools.browsersim.ui.skin.BrowserSimSkin;

public class SkinChangeEvent extends EventObject {
	private static final long serialVersionUID = 2734715406829337324L;
	
	private BrowserSimSkin newSkin;

	public SkinChangeEvent(BrowserSim source, BrowserSimSkin newSkin) {
		super(source);
		this.newSkin = newSkin;
	}

	@Override
	public BrowserSim getSource() {
		return (BrowserSim) super.getSource();
	}

	public BrowserSimSkin getNewSkin() {
		return newSkin;
	}
}
