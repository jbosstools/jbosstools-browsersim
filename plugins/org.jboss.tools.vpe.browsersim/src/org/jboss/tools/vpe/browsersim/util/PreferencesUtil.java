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
package org.jboss.tools.vpe.browsersim.util;

import org.jboss.tools.vpe.browsersim.BrowserSimArgs;
import org.w3c.dom.Node;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class PreferencesUtil {
	public static final String SEPARATOR = System.getProperty("file.separator");
	public static final String USER_HOME = System.getProperty("user.home");
	
	private static final String STANDALONE_PREFERENCES_FOLDER = ".browsersim";
	private static final String USER_PREFERENCES_FOLDER = "org.jboss.tools.vpe.browsersim";
	
	public static final String getConfigFolderPath() {
		return BrowserSimArgs.standalone ? USER_HOME + SEPARATOR + STANDALONE_PREFERENCES_FOLDER : USER_PREFERENCES_FOLDER;
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}
	
	public static boolean isNullOrEmpty(Node node) {
		return node == null || node.getTextContent().trim().isEmpty();
	}
}
