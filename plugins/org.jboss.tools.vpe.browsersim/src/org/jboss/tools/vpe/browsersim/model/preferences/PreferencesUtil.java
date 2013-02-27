package org.jboss.tools.vpe.browsersim.model.preferences;

import org.jboss.tools.vpe.browsersim.ui.BrowserSim;

public class PreferencesUtil {
	public static final String SEPARATOR = System.getProperty("file.separator");
	public static final String USER_HOME = System.getProperty("user.home");
	
	private static final String STANDALONE_PREFERENCES_FOLDER = ".browsersim";
	private static final String USER_PREFERENCES_FOLDER = "org.jboss.tools.vpe.browsersim";
	
	protected static final String getConfigFolderPath() {
		return BrowserSim.isStandalone	? USER_HOME + SEPARATOR + STANDALONE_PREFERENCES_FOLDER : USER_PREFERENCES_FOLDER;
	}
	
	protected static final String encode(String string) {
		return string.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t");
	}

	protected static final String decode(String string) {
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
