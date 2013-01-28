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
package org.jboss.tools.vpe.browsersim.model;

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

import org.eclipse.swt.graphics.Point;
import org.jboss.tools.vpe.browsersim.util.ResourcesUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
@SuppressWarnings("nls")
public class DevicesListStorage {
	
	public static final String SEPARATOR = "/";
	private static final String DEFAULT_PREFERENCES_RESOURCE = "config/devices.cfg";
	private static final String USER_HOME = System.getProperty("user.home");
	private static final String STANDALONE_PREFERENCES_FOLDER = ".browsersim";
	private static final String USER_PREFERENCES_FOLDER = "org.jboss.tools.vpe.browsersim";
	private static final String PREFERENCES_FILE = "devices.cfg";
	private static final int CURRENT_CONFIG_VERSION = 7;

	public static void saveUserDefinedDevicesList(DevicesList devicesList, Point location, boolean isStandalone) {
		File configFolder = new File(getConfigFolderPath(isStandalone));
		configFolder.mkdir();
		File configFile = new File(configFolder, PREFERENCES_FILE);
		
		try {
			saveDevicesList(devicesList, configFile, location);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DevicesList loadUserDefinedDevicesList(boolean isStandalone) {
		String folder = getConfigFolderPath(isStandalone);
		File customConfigFile = new File(folder + SEPARATOR + PREFERENCES_FILE);
		DevicesList devicesList = null;
		if (customConfigFile.exists()) {
			try {
				devicesList = loadDevicesList(customConfigFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return devicesList;
	}
	
	public static DevicesList loadDefaultDevicesList() {
		DevicesList devicesList = null;
		try {
			devicesList = loadDevicesList(ResourcesUtil.getResourceAsStream(
					DEFAULT_PREFERENCES_RESOURCE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (devicesList == null) {
			Device device = new Device("Default", 1024, 768, 1.0, null, null);
			List<Device> devices = new ArrayList<Device>();
			devices.add(device);
			devicesList = new DevicesList(devices, 0, true, null, null);
		}

		return devicesList;
	}

	public static String getConfigFolderPath(boolean isStandalone) {
		return isStandalone	? USER_HOME + SEPARATOR + STANDALONE_PREFERENCES_FOLDER : USER_PREFERENCES_FOLDER;
	}
	
	private static void saveDevicesList(DevicesList devicesList, File file, Point location) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		writer.write("ConfigVersion=" + String.valueOf(CURRENT_CONFIG_VERSION) + "\n");
		writer.write("Location=" + location.x + " " + location.y + "\n");
		writer.write("SelectedDeviceIndex=" + String.valueOf(devicesList.getSelectedDeviceIndex()) + "\n");
		writer.write("UseSkins=" + String.valueOf(devicesList.getUseSkins()) + "\n");
		Boolean truncateWindow = devicesList.getTruncateWindow();
		String truncateWindowString = truncateWindow == null ? "" : truncateWindow.toString();
		writer.write("TruncateWindow=" + truncateWindowString + "\n");
		
		for (Device device : devicesList.getDevices()) {
			writer.write( encode(device.getName() ));
			writer.write('\t');
			writer.write(encode( String.valueOf(device.getWidth()) ));
			writer.write('\t');
			writer.write(encode( String.valueOf(device.getHeight()) ));
			writer.write('\t');
			writer.write(encode( Device.PIXEL_RAIO_FORMAT.format(device.getPixelRatio()) ));
			writer.write('\t');
			if (device.getUserAgent() != null) {
				writer.write( encode(device.getUserAgent() ));
			}
			writer.write('\t');
			if (device.getSkinId() != null) {
				writer.write( encode(device.getSkinId() ));
			}
			writer.write('\n');
		}
		
		writer.close();
	}

	private static DevicesList loadDevicesList(File file) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		return loadDevicesList(inputStream);
	}

	private static DevicesList loadDevicesList(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		List<Device> devices = null;
		int selectedDeviceIndex = 0;
		Point location = null;
		boolean useSkins = true;
		Boolean truncateWindow = true;
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
				
				if ((nextLine = reader.readLine()) != null) {
					Pattern pattern = Pattern.compile("TruncateWindow=(true|false|)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						if ( "".equals(matcher.group(1)) ) {
							truncateWindow = null;
						} else {							
							truncateWindow = Boolean.parseBoolean(matcher.group(1));
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
								decode(deviceMatcher.group(1)),
								Integer.parseInt(deviceMatcher.group(2)),
								Integer.parseInt(deviceMatcher.group(3)),
								pixelRatio,
								deviceMatcher.group(5) != null 
								? decode(deviceMatcher.group(5))
										: null,
										deviceMatcher.group(6) != null 
										? decode(deviceMatcher.group(6))
												: null
								));
					}
				}
			}	
		} finally {
			reader.close();
		}
		
		if (devices == null || devices.size() <= selectedDeviceIndex) {
			return null;
		} else { 
			return new DevicesList(devices, selectedDeviceIndex, useSkins, truncateWindow, location);
		}
	}

	private static String encode(String string) {
		return string.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t");
	}

	private static String decode(String string) {
		StringBuilder result = new StringBuilder();
			
		int i = 0;
		while (i < string.length() - 1) {
			char c0 = string.charAt(i);
			if (c0 == '\\') {
				char c1 = string.charAt(i + 1);
				switch (c1) {
				case '\\':
					result.append('\\');
					i++;
					break;
				case 'n':
					result.append('\n');
					i++;
					break;
				case 't':
					result.append('\t');
					i++;
					break;
				default:
					result.append(c0);
					break;
				}
			} else {
				result.append(c0);
			}
			i++;
		}
	
		if (i < string.length()) {
			result.append(string.charAt(i));
		}
				
		return result.toString();		
	}
}
