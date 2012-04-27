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
package org.jboss.tools.vpe.browsersim.ui;

import org.jboss.tools.vpe.browsersim.util.NLS;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class Messages {
	private static final String BUNDLE_NAME = Messages.class.getName().toString().toLowerCase();
	public static String BrowserSim_ADDRESS;
	public static String BrowserSim_BROWSER_SIM;
	public static String BrowserSim_COULD_NOT_OPEN_DEFAULT_BROWSER;
	public static String BrowserSim_DEVICES;
	public static String BrowserSim_ERROR;
	public static String BrowserSim_EXIT;
	public static String BrowserSim_FILE;
	public static String BrowserSim_MORE;
	public static String BrowserSim_OPEN_IN_DEFAULT_BROWSER;
	public static String BrowserSim_TURN_LEFT;
	public static String BrowserSim_TURN_RIGHT;
	public static String BrowserSim_VIEW_PAGE_SOURCE;
	public static String EditDeviceDialog_CANCEL;
	public static String EditDeviceDialog_EDIT_DEVICE;
	public static String EditDeviceDialog_HEIGHT;
	public static String EditDeviceDialog_MANAGE_DEVICES;
	public static String EditDeviceDialog_NAME;
	public static String EditDeviceDialog_NONE;
	public static String EditDeviceDialog_OK;
	public static String EditDeviceDialog_SKIN;
	public static String EditDeviceDialog_USER_AGENT;
	public static String EditDeviceDialog_WIDTH;
	public static String ExceptionNotifier_APPLE_APPLICATION_SUPPORT_IS_NOT_FOUND;
	public static String ExceptionNotifier_BROWSERSIM_IS_FAILED_TO_START;
	public static String ExceptionNotifier_ONLY_32_BIT_ECLIPSE_IS_SUPPORTED_ON_WINDOWS;
	public static String ExceptionNotifier_BROWSERSIM_IS_FAILED_TO_START_ON_LINUX;
	public static String ManageDevicesDialog_ADD;
	public static String ManageDevicesDialog_CANCEL;
	public static String ManageDevicesDialog_DEFAULT;
	public static String ManageDevicesDialog_DEVICES;
	public static String ManageDevicesDialog_EDIT;
	public static String ManageDevicesDialog_HEIGHT;
	public static String ManageDevicesDialog_NAME;
	public static String ManageDevicesDialog_NONE;
	public static String ManageDevicesDialog_NEW_DEVICE;
	public static String ManageDevicesDialog_NEW_USER_AGENT;
	public static String ManageDevicesDialog_OK;
	public static String ManageDevicesDialog_REMOVE;
	public static String ManageDevicesDialog_REVERT_ALL;
	public static String ManageDevicesDialog_SKIN;
	public static String ManageDevicesDialog_LOAD_DEFAULTS;
	public static String ManageDevicesDialog_USER_AGENT;
	public static String ManageDevicesDialog_WIDTH;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
