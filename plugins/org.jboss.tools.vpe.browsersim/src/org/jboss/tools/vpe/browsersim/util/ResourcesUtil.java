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
package org.jboss.tools.vpe.browsersim.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jboss.tools.vpe.browsersim.ui.BrowserSim;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ResourcesUtil {
	public static InputStream getResourceAsStream(String resourcesRootFolder, String name) {
		if (name.startsWith("/")) { //$NON-NLS-1$
			return BrowserSim.class.getResourceAsStream(name);
		} else {
			return BrowserSim.class.getResourceAsStream(resourcesRootFolder + name);
		}
	}
	
	public static String getResourceAsString(String resourcesRootFolder, String name) {
		InputStream input = getResourceAsStream(resourcesRootFolder, name);
		InputStreamReader reader = new InputStreamReader(input);
		BufferedReader bufferedReader = new BufferedReader(reader);

		StringBuilder stringBuilder = new StringBuilder();
		String read;
		try {
			try {
				while ((read = bufferedReader.readLine()) != null) {
					stringBuilder.append(read);
					stringBuilder.append('\n');
				}
			} finally {
				bufferedReader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}
}
