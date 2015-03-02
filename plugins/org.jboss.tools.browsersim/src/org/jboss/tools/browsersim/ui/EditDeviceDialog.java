/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui;

import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.browsersim.model.Device;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public class EditDeviceDialog extends DeviceDialog {

	public EditDeviceDialog(Shell parent, int style, Device initialDevice) {
		super(parent, style, initialDevice);
	}

	@Override
	protected String getHeaderText() {
		return Messages.EditDeviceDialog_EDIT_DEVICE;
	}

	@Override
	protected String getDefaultStatusText() {
		return Messages.EditDeviceDialog_EDIT_DEVICE_STATUS;
	}

}
