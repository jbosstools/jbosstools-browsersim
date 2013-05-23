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
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.graphics.Point;
import org.jboss.tools.vpe.browsersim.util.PreferencesUtil;
import org.jboss.tools.vpe.browsersim.util.BrowserSimResourcesUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class SpecificPreferencesStorage implements PreferencesStorage{
	private static final String PREFERENCES_ORIENTATION_ANGLE = "orientationAngle";
	private static final String PREFERENCES_LOCATION_Y = "y";
	private static final String PREFERENCES_LOCATION_X = "x";
	private static final String PREFERENCES_LOCATION = "location";
	private static final String PREFERENCES_USE_SKINS = "useSkins";
	private static final String PREFERENCES_LIVE_RELOAD = "enableLiveReload";
	private static final String PREFERENCES_SELECTED_DEVICE = "selectedDeviceId";
	private static final String PREFERENCES_VERSION = "version";

	
	private static final String SPECIFIC_PREFERENCES_FILE = "specificPreferences.xml";
	private static final String DEFAULT_SPECIFIC_PREFERENCES_RESOURCE = "config/specificPreferences.xml";

	private static final int CURRENT_CONFIG_VERSION = 11;

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
			specificPreferences = load(BrowserSimResourcesUtil.getResourceAsStream(DEFAULT_SPECIFIC_PREFERENCES_RESOURCE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (specificPreferences == null) {
			specificPreferences = new SpecificPreferences(null, true, false, 0, null);
		}

		return specificPreferences;
	}
	
	private SpecificPreferences load(InputStream is) throws IOException {
		int configVersion = 0;
		String selectedDeviceId = null;
		int orientationAngle = 0;
		Point currentlocation = null;
		boolean useSkins = true;
		boolean enableLiveReload = false;

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(is);

			// optional, but recommended
			// see http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			document.getDocumentElement().normalize();

			configVersion = Integer.parseInt(document.getDocumentElement().getAttribute(PREFERENCES_VERSION));
			if (configVersion == CURRENT_CONFIG_VERSION) {
				Node node = document.getElementsByTagName(PREFERENCES_SELECTED_DEVICE).item(0);
				if (!PreferencesUtil.isNullOrEmpty(node)) {
					selectedDeviceId = node.getTextContent();
				}
				
				node = document.getElementsByTagName(PREFERENCES_USE_SKINS).item(0);
				if (!PreferencesUtil.isNullOrEmpty(node)) {
					useSkins = Boolean.parseBoolean(node.getTextContent());
				}
				
				node = document.getElementsByTagName(PREFERENCES_LIVE_RELOAD).item(0);
				if (!PreferencesUtil.isNullOrEmpty(node)) {
					enableLiveReload = Boolean.parseBoolean(node.getTextContent());
				}
				
				node = document.getElementsByTagName(PREFERENCES_ORIENTATION_ANGLE).item(0);
				if (!PreferencesUtil.isNullOrEmpty(node)) {
					orientationAngle = Integer.parseInt(node.getTextContent());
				}
				
				node = document.getElementsByTagName(PREFERENCES_LOCATION).item(0);
				if (!PreferencesUtil.isNullOrEmpty(node) && node.getNodeType() == Node.ELEMENT_NODE) {
					Element location = (Element) node;
					Node x = location.getElementsByTagName(PREFERENCES_LOCATION_X).item(0);
					Node y = location.getElementsByTagName(PREFERENCES_LOCATION_Y).item(0);
					
					if (!PreferencesUtil.isNullOrEmpty(x) && !PreferencesUtil.isNullOrEmpty(y)) {
						currentlocation = new Point(Integer.parseInt(x.getTextContent()), Integer.parseInt(y
								.getTextContent()));
					}
				}
				return new SpecificPreferences(selectedDeviceId, useSkins, enableLiveReload, orientationAngle, currentlocation);
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void save(SpecificPreferences sp, File file) throws IOException {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement("browserSim");
			rootElement.setAttribute(PREFERENCES_VERSION, String.valueOf(CURRENT_CONFIG_VERSION));
			doc.appendChild(rootElement);

			Element selectedDevice = doc.createElement(PREFERENCES_SELECTED_DEVICE);
			selectedDevice.setTextContent(String.valueOf(sp.getSelectedDeviceId()));
			rootElement.appendChild(selectedDevice);

			Element location = doc.createElement(PREFERENCES_LOCATION);
			Element locationX = doc.createElement(PREFERENCES_LOCATION_X);
			Element locationY = doc.createElement(PREFERENCES_LOCATION_Y);
			Point currentLocation = sp.getLocation();
			locationX.setTextContent(String.valueOf(currentLocation.x));
			locationY.setTextContent(String.valueOf(currentLocation.y));
			location.appendChild(locationX);
			location.appendChild(locationY);
			rootElement.appendChild(location);
			
			Element orientationAngle = doc.createElement(PREFERENCES_ORIENTATION_ANGLE);
			orientationAngle.setTextContent(String.valueOf(sp.getOrientationAngle()));
			rootElement.appendChild(orientationAngle);
			
			Element useSkins = doc.createElement(PREFERENCES_USE_SKINS);
			useSkins.setTextContent(String.valueOf(sp.getUseSkins()));
			rootElement.appendChild(useSkins);
			
			Element enableLiveReload = doc.createElement(PREFERENCES_LIVE_RELOAD);
			enableLiveReload.setTextContent(String.valueOf(sp.isEnableLiveReload()));
			rootElement.appendChild(enableLiveReload);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.newTransformer().transform(new DOMSource(doc), new StreamResult(file));

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
