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
package org.jboss.tools.vpe.browsersim.model.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jboss.tools.vpe.browsersim.BrowserSimLogger;
import org.jboss.tools.vpe.browsersim.util.PreferencesUtil;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public abstract class SpecificPreferencesStorage implements PreferencesStorage{
	protected static final String PREFERENCES_ORIENTATION_ANGLE = "orientationAngle"; //$NON-NLS-1$
	protected static final String PREFERENCES_LOCATION_Y = "y"; //$NON-NLS-1$
	protected static final String PREFERENCES_LOCATION_X = "x"; //$NON-NLS-1$
	protected static final String PREFERENCES_LOCATION = "location"; //$NON-NLS-1$
	protected static final String PREFERENCES_USE_SKINS = "useSkins"; //$NON-NLS-1$
	protected static final String PREFERENCES_LIVE_RELOAD = "enableLiveReload"; //$NON-NLS-1$
	protected static final String PREFERENCES_LIVE_RELOAD_PORT = "liveReloadPort"; //$NON-NLS-1$
	protected static final String PREFERENCES_TOUCH_EVENTS = "enableTouchEvents"; //$NON-NLS-1$
	protected static final String PREFERENCES_SELECTED_DEVICE = "selectedDeviceId"; //$NON-NLS-1$
	protected static final String PREFERENCES_IS_JAVAFX = "javafx"; //$NON-NLS-1$
	protected static final String PREFERENCES_VERSION = "version"; //$NON-NLS-1$

	public static final int DEFAULT_LIVE_RELOAD_PORT = 35729;
	
	@Override
	public void save(Object o) {
		File configFolder = new File(PreferencesUtil.getConfigFolderPath());
		configFolder.mkdir();
		File configFile = new File(configFolder, getFileName());
		
		save((SpecificPreferences) o, configFile);
	}

	@Override
	public Object load(String configFolder) {
		File customConfigFile = new File(configFolder + PreferencesUtil.SEPARATOR + getFileName());
		SpecificPreferences specificPreferences = null;
		if (customConfigFile.exists()) {
			try {
				specificPreferences = load(new FileInputStream(customConfigFile));
			} catch (FileNotFoundException e) {
				BrowserSimLogger.logError(e.getMessage(), e);
			}
		}
		
		return specificPreferences;
	}
	
	@Override
	public SpecificPreferences loadDefault() {
		SpecificPreferences specificPreferences = null;
		specificPreferences = load(getDefaultPreferencesFileAsStream());
		
		if (specificPreferences == null) {
			specificPreferences = createBlankPreferences();
		}

		return specificPreferences;
	}
	
	protected abstract SpecificPreferencesStorage getInstance();
	
	protected abstract SpecificPreferences load(InputStream is);
	
	protected abstract SpecificPreferences createBlankPreferences();

	protected abstract void save(SpecificPreferences sp, File file);
	
	protected abstract String getFileName();
	
	protected abstract InputStream getDefaultPreferencesFileAsStream();
}
