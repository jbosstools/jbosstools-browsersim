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
package org.jboss.tools.browsersim.ui.model.preferences;

import org.eclipse.swt.graphics.Point;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public class BrowserSimSpecificPreferences extends SpecificPreferences{

	public BrowserSimSpecificPreferences(String selectedDeviceId, boolean useSkins, boolean enableLiveReload,
			int liveReloadPort, boolean enableTouchEvents, int orientationAngle, Point location, boolean isJavaFx) {
		super(selectedDeviceId, useSkins, enableLiveReload, liveReloadPort, enableTouchEvents, orientationAngle, location, isJavaFx);
	}

}
