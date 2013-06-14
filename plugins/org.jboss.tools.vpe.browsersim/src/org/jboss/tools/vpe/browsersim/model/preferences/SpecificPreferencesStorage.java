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

import org.jboss.tools.vpe.browsersim.util.PreferencesUtil;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public abstract class SpecificPreferencesStorage implements PreferencesStorage{
	protected static final String PREFERENCES_ORIENTATION_ANGLE = "orientationAngle";
	protected static final String PREFERENCES_LOCATION_Y = "y";
	protected static final String PREFERENCES_LOCATION_X = "x";
	protected static final String PREFERENCES_LOCATION = "location";
	protected static final String PREFERENCES_USE_SKINS = "useSkins";
	protected static final String PREFERENCES_LIVE_RELOAD = "enableLiveReload";
	protected static final String PREFERENCES_SELECTED_DEVICE = "selectedDeviceId";
	protected static final String PREFERENCES_VERSION = "version";

	@Override
	public void save(Object o) {
		File configFolder = new File(PreferencesUtil.getConfigFolderPath());
		configFolder.mkdir();
		File configFile = new File(configFolder, getFileName());
		
		save((SpecificPreferences) o, configFile);
	}

	@Override
	public Object load() {
		String folder = PreferencesUtil.getConfigFolderPath();
		File customConfigFile = new File(folder + PreferencesUtil.SEPARATOR + getFileName());
		SpecificPreferences specificPreferences = null;
		if (customConfigFile.exists()) {
			try {
				specificPreferences = load(new FileInputStream(customConfigFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
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
