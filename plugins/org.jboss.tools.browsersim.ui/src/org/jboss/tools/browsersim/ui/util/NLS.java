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
package org.jboss.tools.browsersim.ui.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.jboss.tools.browsersim.ui.BrowserSimLogger;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class NLS {
	private static final int MOD_EXPECTED = Modifier.PUBLIC | Modifier.STATIC;

	private NLS(){};
	
	public static void initializeMessages(Class<?> clazz) {
		String bundleName = clazz.getName().toLowerCase();
		initializeMessages(bundleName, clazz);
	}

	public static void initializeMessages(String bundleName, Class<?> clazz) {
		ResourceBundle resourceBundle  = ResourceBundle.getBundle(bundleName);
		
		try {
			for (Field field : clazz.getDeclaredFields()) {
				// if it is a public static uninitialized String field
				if ((field.getModifiers() & MOD_EXPECTED) == MOD_EXPECTED
						&& field.getType() == String.class
						&& field.get(null) == null) {
					try {
						field.set(null, resourceBundle.getString(field.getName()));
					} catch (MissingResourceException e) {
						field.set(null, '!' + field.getName() + '!');
					}
				}
			}
		}  catch (IllegalAccessException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		}
	}
}
