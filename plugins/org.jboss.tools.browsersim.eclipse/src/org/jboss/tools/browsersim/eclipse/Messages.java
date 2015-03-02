package org.jboss.tools.browsersim.eclipse;

import org.eclipse.osgi.util.NLS;

/**
 * @author kmarmaliykov
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.browsersim.eclipse.messages"; //$NON-NLS-1$
	public static String Callback_CANNOT_OBTAIN_PAGE;
	public static String OpenFileCallback_CANNOT_OPEN_FILE;
	public static String BrowserSim;
	public static String BrowserSim_LIBRARY_DETECTION;
	public static String BrowserSim_LIBRARY_DETECTION_FAILURE;
	public static String ExternalProcessLauncher_ERROR;
	public static String ExternalProcessLauncher_NO_BUNDLE;
	public static String BrowserSimErrorDialog_ERROR_MESSAGE_COMMON;
	public static String BrowserSimErrorDialog_ERROR_MESSAGE_HEADER;
	public static String BrowserSimPreferencesPage_DETECT_AUTOMATICALLY;
	public static String BrowserSimPreferencesPage_GTK_2;
	public static String BrowserSimPreferencesPage_GTK_3;
	public static String BrowserSimPreferencesPage_JRE_LINK;
	public static String BrowserSimPreferencesPage_REQUIREMENTS;
	public static String BrowserSimPreferencesPage_SELECT;
	public static String BrowserSimPreferencesPage_SELECT_GTK;
	public static String BrowserSimPreferencesPage_SELECT_JRE;
	public static String UsageEventTypeLaunchLabelDescription;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
