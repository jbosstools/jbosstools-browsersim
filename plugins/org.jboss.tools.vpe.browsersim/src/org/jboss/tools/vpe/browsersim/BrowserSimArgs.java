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
package org.jboss.tools.vpe.browsersim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.tools.vpe.browsersim.ui.BrowserSim;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSimArgs {
	public static final String NOT_STANDALONE = "-not-standalone"; //$NON-NLS-1$
	private String path;
	private boolean standalone;
	
	private BrowserSimArgs(String path, boolean standalone) {
		this.path = path;
		this.standalone = standalone;
	}

	public static BrowserSimArgs parseArgs(String[] args) {
		List<String> params = new ArrayList<String>(Arrays.asList(args));
		boolean standalone = !params.contains(NOT_STANDALONE);
		if (!BrowserSim.isStandalone) {
			params.remove(NOT_STANDALONE);
		}
		
		String path;
		if (params.size() > 0) {
			path = params.get(params.size() - 1);
		} else {
			path = null;
		}
		
		return new BrowserSimArgs(path, standalone);
	}
	
	public String getPath() {
		return path;
	}
	public boolean isStandalone() {
		return standalone;
	}
}
