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
package org.jboss.tools.vpe.browsersim.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.jboss.tools.vpe.browsersim.eclipse.util.BrowserSimLauncher;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 */
public class RunBrowserSimHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public RunBrowserSimHandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		BrowserSimLauncher.launchBrowserSim(null);			
		//Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		return null;
	}
}
