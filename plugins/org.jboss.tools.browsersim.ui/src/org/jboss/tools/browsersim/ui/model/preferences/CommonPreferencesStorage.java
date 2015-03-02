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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.tools.browsersim.ui.BrowserSimLogger;
import org.jboss.tools.browsersim.ui.model.Device;
import org.jboss.tools.browsersim.ui.model.TruncateWindow;
import org.jboss.tools.browsersim.ui.util.BrowserSimResourcesUtil;
import org.jboss.tools.browsersim.ui.util.PreferencesUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class CommonPreferencesStorage implements PreferencesStorage{
	private static final String PREFERENCES_ID = "id"; //$NON-NLS-1$
	private static final String PREFERENCES_DEVICE = "device"; //$NON-NLS-1$
	private static final String PREFERENCES_DEVICE_HEIGHT = "height"; //$NON-NLS-1$
	private static final String PREFERENCES_DEVICE_WIDTH = "width"; //$NON-NLS-1$
	private static final String PREFERENCES_DEVICE_NAME = "name"; //$NON-NLS-1$
	private static final String PREFERENCES_DEVICE_SKIN = "skin"; //$NON-NLS-1$
	private static final String PREFERENCES_DEVICE_USER_AGENT = "userAgent"; //$NON-NLS-1$
	private static final String PREFERENCES_DEVICE_PIXEL_RATIO = "pixelRatio"; //$NON-NLS-1$
	private static final String PREFERENCES_DEVICES = "devices"; //$NON-NLS-1$
	private static final String PREFERENCES_WEINRE_CLIENT_URL = "clientUrl"; //$NON-NLS-1$
	private static final String PREFERENCES_WEINRE_SCRIPT_URL = "scriptUrl"; //$NON-NLS-1$
	private static final String PREFERENCES_WEINRE = "weinre"; //$NON-NLS-1$
	private static final String PREFERENCES_SCREENSHOTS_FOLDER = "screenshotsFolder"; //$NON-NLS-1$
	private static final String PREFERENCES_TRUNCATE_WINDOW = "truncateWindow"; //$NON-NLS-1$
	private static final String PREFERENCES_VERSION = "version"; //$NON-NLS-1$

	
	private static final String DEFAULT_COMMON_PREFERENCES_RESOURCE = "config/commonPreferences.xml"; //$NON-NLS-1$
	private static final String COMMON_PREFERENCES_FILE = "commonPreferences.xml"; //$NON-NLS-1$
	
	private static final String DEFAULT_WEINRE_SCRIPT_URL = "http://debug.build.phonegap.com/target/target-script-min.js"; //$NON-NLS-1$
	private static final String DEFAULT_WEINRE_CLIENT_URL = "http://debug.build.phonegap.com/client/"; //$NON-NLS-1$
	
	private static final int CURRENT_CONFIG_VERSION = 13;
	
	public static final CommonPreferencesStorage INSTANCE = new CommonPreferencesStorage();
	
	@Override
	public void save(Object o) {
		File configFolder = new File(PreferencesUtil.getConfigFolderPath());
		configFolder.mkdir();
		File configFile = new File(configFolder, COMMON_PREFERENCES_FILE);
		
		try {
			saveCommonPreferences((CommonPreferences) o, configFile);
		} catch (IOException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		}
	}
	
	@Override
	public Object load(String configFolder) {
		File customConfigFile = new File(configFolder + PreferencesUtil.SEPARATOR + COMMON_PREFERENCES_FILE);
		CommonPreferences commonPreferences = null;
		if (customConfigFile.exists()) {
			try {
				commonPreferences = load(new FileInputStream(customConfigFile));
			} catch (IOException e) {
				BrowserSimLogger.logError(e.getMessage(), e);
			}
		}
		
		return commonPreferences;
	}

	@Override
	public CommonPreferences loadDefault(){
		CommonPreferences commonPreferences = load(BrowserSimResourcesUtil.getResourceAsStream(DEFAULT_COMMON_PREFERENCES_RESOURCE));
		if (commonPreferences == null) {
			Device device = new Device(UUID.randomUUID().toString(), "Default", 1024, 768, 1.0, null, null); //$NON-NLS-1$
			Map<String, Device> devices = new LinkedHashMap<String, Device>();
			
			devices.put(device.getId(), device);
			commonPreferences = new CommonPreferences(devices, TruncateWindow.PROMPT, getDefaultScreenshotsFolderPath(),
					getDefaultWeinreScriptUrl(), getDefaultWeinreClientUrl());
		}

		return commonPreferences;
	}
	
	private CommonPreferences load(InputStream is){
		Map<String, Device> devices = null;
		TruncateWindow truncateWindow = TruncateWindow.PROMPT;
		String screenshotsFolder = getDefaultScreenshotsFolderPath();
		String weinreScriptUrl = getDefaultWeinreScriptUrl();
		String weinreClientUrl = getDefaultWeinreClientUrl();

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(is);

			// optional, but recommended
			// see http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			document.getDocumentElement().normalize();

			int configVersion = Integer.parseInt(document.getDocumentElement().getAttribute(PREFERENCES_VERSION));
			if (configVersion == CURRENT_CONFIG_VERSION) {
				Node node = document.getElementsByTagName(PREFERENCES_TRUNCATE_WINDOW).item(0);
				if (!PreferencesUtil.isNullOrEmpty(node)) {
					truncateWindow = TruncateWindow.valueOf(node.getTextContent());
				}
				
				node = document.getElementsByTagName(PREFERENCES_SCREENSHOTS_FOLDER).item(0);
				if (!PreferencesUtil.isNullOrEmpty(node)) {
					screenshotsFolder = node.getTextContent();
				}
				
				node = document.getElementsByTagName(PREFERENCES_WEINRE).item(0);
				if (!PreferencesUtil.isNullOrEmpty(node) && node.getNodeType() == Node.ELEMENT_NODE) {
					Element weinre = (Element) node;
					Node scriptUrl = weinre.getElementsByTagName(PREFERENCES_WEINRE_SCRIPT_URL).item(0);
					Node clientUrl = weinre.getElementsByTagName(PREFERENCES_WEINRE_CLIENT_URL).item(0);
					
					if (!PreferencesUtil.isNullOrEmpty(scriptUrl)) {
						weinreScriptUrl = scriptUrl.getTextContent();
					}
					if (!PreferencesUtil.isNullOrEmpty(clientUrl)) {
						weinreClientUrl = clientUrl.getTextContent();
					}
				}
				
				node = document.getElementsByTagName(PREFERENCES_DEVICES).item(0);
				if (!PreferencesUtil.isNullOrEmpty(node) && node.hasChildNodes()) {
					NodeList devicesList = node.getChildNodes();
					devices = new LinkedHashMap<String, Device>();
					for (int i = 0; i < devicesList.getLength();i++) {
						Node item = devicesList.item(i);
						if (!PreferencesUtil.isNullOrEmpty(item) && item.getNodeType() == Node.ELEMENT_NODE) {
							Element device = (Element) item;
							
							double pixelRatio;
							try {
								pixelRatio = Device.PIXEL_RAIO_FORMAT.parse(
										device.getElementsByTagName(PREFERENCES_DEVICE_PIXEL_RATIO).item(0)
												.getTextContent()).doubleValue();
							} catch (ParseException e) {
								pixelRatio = 1.0;
								BrowserSimLogger.logError(e.getMessage(), e);
							}
							String id = device.getAttribute(PREFERENCES_ID); 
							if (id.isEmpty()) {
								id = UUID.randomUUID().toString();
							}
							String userAgent = device.getElementsByTagName(PREFERENCES_DEVICE_USER_AGENT).item(0).getTextContent();
							String skin = device.getElementsByTagName(PREFERENCES_DEVICE_SKIN).item(0).getTextContent(); 
							devices.put(id, new Device(id, device.getElementsByTagName(PREFERENCES_DEVICE_NAME).item(0).getTextContent(),
									Integer.parseInt(device.getElementsByTagName(PREFERENCES_DEVICE_WIDTH).item(0).getTextContent()),
									Integer.parseInt(device.getElementsByTagName(PREFERENCES_DEVICE_HEIGHT).item(0).getTextContent()),
									pixelRatio,
									PreferencesUtil.isNullOrEmpty(userAgent) ? null : userAgent,
									PreferencesUtil.isNullOrEmpty(skin) ? null : skin));
						}
					}
				}
				return new CommonPreferences(devices, truncateWindow, screenshotsFolder, weinreScriptUrl, weinreClientUrl);
			}
		} catch (ParserConfigurationException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		} catch (SAXException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		} catch (FactoryConfigurationError e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		} catch (IOException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		} catch (RuntimeException e) {
			//catched to avoid exceptions like NPE, NFE, etc
			BrowserSimLogger.logError(e.getMessage(), e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				BrowserSimLogger.logError(e.getMessage(), e);
			}
		}
		
		return null;
	}

	private void saveCommonPreferences(CommonPreferences cp, File file) throws IOException {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement(PREFERENCES_BROWSERSIM);
			rootElement.setAttribute(PREFERENCES_VERSION, String.valueOf(CURRENT_CONFIG_VERSION));
			doc.appendChild(rootElement);

			Element truncateWindow = doc.createElement(PREFERENCES_TRUNCATE_WINDOW);
			truncateWindow.setTextContent(String.valueOf(cp.getTruncateWindow()));
			rootElement.appendChild(truncateWindow);

			Element screenshotsFolder = doc.createElement(PREFERENCES_SCREENSHOTS_FOLDER);
			screenshotsFolder.setTextContent(cp.getScreenshotsFolder());
			rootElement.appendChild(screenshotsFolder);
			
			Element weinre = doc.createElement(PREFERENCES_WEINRE);
			Element weinreScriptUrl = doc.createElement(PREFERENCES_WEINRE_SCRIPT_URL);
			Element weinreClientUrl = doc.createElement(PREFERENCES_WEINRE_CLIENT_URL);
			weinreScriptUrl.setTextContent(cp.getWeinreScriptUrl());
			weinreClientUrl.setTextContent(cp.getWeinreClientUrl());
			weinre.appendChild(weinreScriptUrl);
			weinre.appendChild(weinreClientUrl);
			rootElement.appendChild(weinre);
			
			Element devices = doc.createElement(PREFERENCES_DEVICES);
			for (String id : cp.getDevices().keySet()) {
				Device device = cp.getDevices().get(id);
				Element deviceElement = doc.createElement(PREFERENCES_DEVICE);
				deviceElement.setAttribute(PREFERENCES_ID, id);
				
				Element name = doc.createElement(PREFERENCES_DEVICE_NAME);
				name.setTextContent(device.getName());
				deviceElement.appendChild(name);
				
				Element width = doc.createElement(PREFERENCES_DEVICE_WIDTH);
				width.setTextContent(String.valueOf(device.getWidth()));
				deviceElement.appendChild(width);
				
				Element height = doc.createElement(PREFERENCES_DEVICE_HEIGHT);
				height.setTextContent(String.valueOf(device.getHeight()));
				deviceElement.appendChild(height);
				
				Element pixelRatio = doc.createElement(PREFERENCES_DEVICE_PIXEL_RATIO);
				pixelRatio.setTextContent(String.valueOf(device.getPixelRatio()));
				deviceElement.appendChild(pixelRatio);
				
				Element userAgent = doc.createElement(PREFERENCES_DEVICE_USER_AGENT);
				userAgent.setTextContent(device.getUserAgent());
				deviceElement.appendChild(userAgent);
				
				Element skin = doc.createElement(PREFERENCES_DEVICE_SKIN);
				skin.setTextContent(device.getSkinId());
				deviceElement.appendChild(skin);
				
				devices.appendChild(deviceElement);
			}
			rootElement.appendChild(devices);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.newTransformer().transform(new DOMSource(doc), new StreamResult(file));

		} catch (ParserConfigurationException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		} catch (TransformerException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		}
	}
	
	private static String getDefaultScreenshotsFolderPath() {
		return ""; //$NON-NLS-1$
	}
	
	private static String getDefaultWeinreScriptUrl() {
		return DEFAULT_WEINRE_SCRIPT_URL;	
	}
	
	private static String getDefaultWeinreClientUrl() {
		return DEFAULT_WEINRE_CLIENT_URL;
	}
}
