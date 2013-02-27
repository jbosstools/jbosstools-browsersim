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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Point;
import org.jboss.tools.vpe.browsersim.util.ResourcesUtil;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class SpecificPreferencesStorage implements PreferencesStorage{
	private static final String SPECIFIC_PREFERENCES_FILE = "specificPreferences.cfg";
	private static final String DEFAULT_SPECIFIC_PREFERENCES_RESOURCE = "config/specificPreferences.cfg";

	private static final int CURRENT_CONFIG_VERSION = 9;

	public static SpecificPreferencesStorage INSTANCE = new SpecificPreferencesStorage();
	
	@Override
	public void save(Object o) {
		File configFolder = new File(PreferencesUtil.getConfigFolderPath());
		configFolder.mkdir();
		File configFile = new File(configFolder, SPECIFIC_PREFERENCES_FILE);
		
		try {
			save((SpecificPreferences) o, configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object load() {
		String folder = PreferencesUtil.getConfigFolderPath();
		File customConfigFile = new File(folder + PreferencesUtil.SEPARATOR + SPECIFIC_PREFERENCES_FILE);
		SpecificPreferences specificPreferences = null;
		if (customConfigFile.exists()) {
			try {
				specificPreferences = load(new FileInputStream(customConfigFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return specificPreferences;
	}
	
	@Override
	public SpecificPreferences loadDefault() {
		SpecificPreferences specificPreferences = null;
		try {
			specificPreferences = load(ResourcesUtil.getResourceAsStream(DEFAULT_SPECIFIC_PREFERENCES_RESOURCE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (specificPreferences == null) {
			specificPreferences = new SpecificPreferences(0, true, null);
		}

		return specificPreferences;
	}
	
	private SpecificPreferences load(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		int selectedDeviceIndex = 0;
		Point location = null;
		boolean useSkins = true;
		try {
			String nextLine;
			int configVersion = 0;
			if ((nextLine = reader.readLine()) != null) {
				Pattern pattern = Pattern.compile("ConfigVersion=([0-9]+)");
				Matcher matcher = pattern.matcher(nextLine);
				if (matcher.matches()) {
					configVersion = Integer.parseInt(matcher.group(1));
				}
			}
			
			if (configVersion == CURRENT_CONFIG_VERSION) {
				if ((nextLine = reader.readLine()) != null) {
					Pattern pattern = Pattern.compile("Location=(\\-?[0-9]+) (\\-?[0-9]+)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						location = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
					}
				}
				
				if ((nextLine = reader.readLine()) != null) {
					Pattern pattern = Pattern.compile("SelectedDeviceIndex=([0-9]+)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						selectedDeviceIndex = Integer.parseInt(matcher.group(1));
					}
				}
				
				if ((nextLine = reader.readLine()) != null) {
					Pattern pattern = Pattern.compile("UseSkins=(true|false)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						useSkins = Boolean.parseBoolean(matcher.group(1));
					}
				}
			}	
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		
		return new SpecificPreferences(selectedDeviceIndex, useSkins, location);
	}

	private void save(SpecificPreferences sp, File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		writer.write("ConfigVersion=" + String.valueOf(CURRENT_CONFIG_VERSION) + "\n");
		Point location = sp.getLocation();
		writer.write("Location=" + location.x + " " + location.y + "\n");
		writer.write("SelectedDeviceIndex=" + String.valueOf(sp.getSelectedDeviceIndex()) + "\n");
		writer.write("UseSkins=" + String.valueOf(sp.getUseSkins()) + "\n");
		
		writer.close();
	}
}
