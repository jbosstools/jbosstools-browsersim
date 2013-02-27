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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.TruncateWindow;
import org.jboss.tools.vpe.browsersim.util.ResourcesUtil;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class CommonPreferencesStorage implements PreferencesStorage{
	private static final String DEFAULT_COMMON_PREFERENCES_RESOURCE = "config/commonPreferences.cfg";
	private static final String COMMON_PREFERENCES_FILE = "commonPreferences.cfg";
	private static final String DEFAULT_WEINRE_SCRIPT_URL = "http://debug.phonegap.com/target/target-script-min.js";
	private static final String DEFAULT_WEINRE_CLIENT_URL = "http://debug.phonegap.com/client/";
	
	private static final int CURRENT_CONFIG_VERSION = 9;
	
	public static final CommonPreferencesStorage INSTANCE = new CommonPreferencesStorage();
	
	@Override
	public void save(Object o) {
		File configFolder = new File(PreferencesUtil.getConfigFolderPath());
		configFolder.mkdir();
		File configFile = new File(configFolder, COMMON_PREFERENCES_FILE);
		
		try {
			saveCommonPreferences((CommonPreferences) o, configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object load() {
		String folder = PreferencesUtil.getConfigFolderPath();
		File customConfigFile = new File(folder + PreferencesUtil.SEPARATOR + COMMON_PREFERENCES_FILE);
		CommonPreferences commonPreferences = null;
		if (customConfigFile.exists()) {
			try {
				commonPreferences = load(new FileInputStream(customConfigFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return commonPreferences;
	}

	@Override
	public CommonPreferences loadDefault(){
		CommonPreferences commonPreferences = null;
		try {
			commonPreferences = load(ResourcesUtil.getResourceAsStream(DEFAULT_COMMON_PREFERENCES_RESOURCE));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (commonPreferences == null) {
			Device device = new Device("Default", 1024, 768, 1.0, null, null);
			List<Device> devices = new ArrayList<Device>();
			devices.add(device);
			commonPreferences = new CommonPreferences(devices, null, getDefaultScreenshotsFolderPath(),
					getDefaultWeinreScriptUrl(), getDefaultWeinreClientUrl());
		}

		return commonPreferences;
	}
	
	private CommonPreferences load(InputStream is) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		List<Device> devices = null;
		TruncateWindow truncateWindow = TruncateWindow.PROMPT;
		String screenshotsFolder = "";
		String weinreScriptUrl = "";
		String weinreClientUrl = "";
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
					Pattern pattern = Pattern.compile("TruncateWindow=(.*)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						if ( "".equals(matcher.group(1)) ) {
							truncateWindow = null;
						} else {							
							truncateWindow = TruncateWindow.valueOf(matcher.group(1));
						}
					}
				}
				
				if ((nextLine = reader.readLine()) != null) {
					Pattern pattern = Pattern.compile("ScreenshotsFolder=(.*)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						if ( "".equals(matcher.group(1)) ) {
							screenshotsFolder = getDefaultScreenshotsFolderPath();
						} else {							
							screenshotsFolder = matcher.group(1);
						}
					}
				}
				
				if ((nextLine = reader.readLine()) != null) {
					Pattern pattern = Pattern.compile("WeinreScriptUrl=(.*)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						if ( "".equals(matcher.group(1)) ) {
							weinreScriptUrl = getDefaultWeinreScriptUrl();
						} else {							
							weinreScriptUrl = matcher.group(1);
						}
					}
				}
				
				if ((nextLine = reader.readLine()) != null) {
					Pattern pattern = Pattern.compile("WeinreClientUrl=(.*)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						if ( "".equals(matcher.group(1)) ) {
							weinreClientUrl = getDefaultWeinreClientUrl();
						} else {							
							weinreClientUrl = matcher.group(1);
						}
					}
				}
				
				Pattern devicePattern = Pattern.compile("^(.*)\\t([0-9]+)\\t([0-9]+)\\t([0-9]*\\.?[0-9]*)\\t(.+)?\\t(.+)?$");
				
				devices = new ArrayList<Device>();
				while ((nextLine = reader.readLine()) != null) {
					Matcher deviceMatcher = devicePattern.matcher(nextLine);
					if (deviceMatcher.matches()) {
						double pixelRatio;
						try {
							pixelRatio = Device.PIXEL_RAIO_FORMAT.parse(deviceMatcher.group(4)).doubleValue();
						} catch (ParseException e) {
							pixelRatio = 1.0;
							e.printStackTrace();
						}

						devices.add(new Device(
								PreferencesUtil.decode(deviceMatcher.group(1)),
								Integer.parseInt(deviceMatcher.group(2)),
								Integer.parseInt(deviceMatcher.group(3)),
								pixelRatio,
								deviceMatcher.group(5) != null 
									? PreferencesUtil.decode(deviceMatcher.group(5))
									: null,
								deviceMatcher.group(6) != null 
									? PreferencesUtil.decode(deviceMatcher.group(6))
									: null
								));
					}
				}
			}	
		} finally {
			reader.close();
		}
		
		if (devices == null) {
			return null;
		} else { 
			return new CommonPreferences(devices, truncateWindow, screenshotsFolder, weinreScriptUrl, weinreClientUrl);
		}
	}

	private void saveCommonPreferences(CommonPreferences cp, File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		writer.write("ConfigVersion=" + String.valueOf(CURRENT_CONFIG_VERSION) + "\n");
		TruncateWindow truncateWindow = cp.getTruncateWindow();
		String truncateWindowString = truncateWindow == null ? "" : truncateWindow.toString();
		writer.write("TruncateWindow=" + truncateWindowString + "\n");
		writer.write("ScreenshotsFolder=" + cp.getScreenshotsFolder() + "\n");
		writer.write("WeinreScriptUrl=" + cp.getWeinreScriptUrl() + "\n");
		writer.write("WeinreClientUrl=" + cp.getWeinreClientUrl() + "\n");
		
		for (Device device : cp.getDevices()) {
			writer.write(PreferencesUtil.encode(device.getName() ));
			writer.write('\t');
			writer.write(PreferencesUtil.encode( String.valueOf(device.getWidth()) ));
			writer.write('\t');
			writer.write(PreferencesUtil.encode( String.valueOf(device.getHeight()) ));
			writer.write('\t');
			writer.write(PreferencesUtil.encode( Device.PIXEL_RAIO_FORMAT.format(device.getPixelRatio()) ));
			writer.write('\t');
			if (device.getUserAgent() != null) {
				writer.write(PreferencesUtil.encode(device.getUserAgent() ));
			}
			writer.write('\t');
			if (device.getSkinId() != null) {
				writer.write(PreferencesUtil.encode(device.getSkinId() ));
			}
			writer.write('\n');
		}
		
		writer.close();
	}
	
	private static String getDefaultScreenshotsFolderPath() {
		return PreferencesUtil.USER_HOME;
	}
	
	private static String getDefaultWeinreScriptUrl() {
		return DEFAULT_WEINRE_SCRIPT_URL;	
	}
	
	private static String getDefaultWeinreClientUrl() {
		return DEFAULT_WEINRE_CLIENT_URL;
	}
}
