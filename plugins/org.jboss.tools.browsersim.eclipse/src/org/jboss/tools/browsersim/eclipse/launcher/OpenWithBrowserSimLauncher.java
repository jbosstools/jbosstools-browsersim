/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.eclipse.launcher;

import org.eclipse.osgi.service.runnable.ParameterizedRunnable;

public class OpenWithBrowserSimLauncher implements ParameterizedRunnable {

	@Override
	public Object run(Object context) throws Exception {
		BrowserSimLauncher.launchBrowserSim((String)context);
		return null;
	}

}
