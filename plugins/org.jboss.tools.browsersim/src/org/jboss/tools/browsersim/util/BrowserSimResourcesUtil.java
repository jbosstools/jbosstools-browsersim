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

import java.io.File;
import java.io.InputStream;

import org.jboss.tools.browsersim.ui.BrowserSim;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSimResourcesUtil {

	private static final String RESOURCES_ROOT_FOLDER = "/org/jboss/tools/browsersim/resources/"; //$NON-NLS-1$

	public static InputStream getResourceAsStream(String name) {
		return ResourcesUtil.getResourceAsStream(RESOURCES_ROOT_FOLDER, name);
	}
	
	public static String getResourceAsString(String name) {
		return ResourcesUtil.getResourceAsString(RESOURCES_ROOT_FOLDER, name);
	}
	
	public static File getResourceAsFile(String name) {
		if (name.startsWith("/")) { //$NON-NLS-1$
			return new File(BrowserSim.class.getResource(name).getPath());
		} else {
			return new File(BrowserSim.class.getResource(RESOURCES_ROOT_FOLDER + name).getPath());
		}
	}
}
