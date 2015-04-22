/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.jboss.tools.browsersim.eclipse.launcher.BrowserSimLauncher;
import org.jboss.tools.browsersim.eclipse.util.CommandUtil;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class RunBrowserSimHandler extends AbstractHandler {
	private static final String LAUNCH_URL = "url"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String launchUrl = event.getParameter(LAUNCH_URL);
		launchUrl = (launchUrl != null) ? launchUrl : CommandUtil.guessUrl();
		BrowserSimLauncher.launchBrowserSim(launchUrl);
		return null;
	}
	
}
