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
package org.jboss.tools.browsersim.util;

import org.jboss.tools.browsersim.BrowserSimArgs;
import org.w3c.dom.Node;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 * @author Ilya Buziuk (ibuziuk)
 * 
 */

public class PreferencesUtil {
	public static final String SEPARATOR = System.getProperty("file.separator"); //$NON-NLS-1$
	public static final String USER_HOME = System.getProperty("user.home"); //$NON-NLS-1$
	private static final String STANDALONE_PREFERENCES_FOLDER = ".browsersim"; //$NON-NLS-1$
	
	private PreferencesUtil() {
	}
	
	public static final String getConfigFolderPath() {
		String path = null;
		if (BrowserSimArgs.standalone) {
			path = USER_HOME + SEPARATOR + STANDALONE_PREFERENCES_FOLDER;
		} else {
			path = BrowserSimArgs.cofigurationFolder;
		}
		return path;
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}
	
	public static boolean isNullOrEmpty(Node node) {
		return node == null || node.getTextContent().trim().isEmpty();
	}

}
