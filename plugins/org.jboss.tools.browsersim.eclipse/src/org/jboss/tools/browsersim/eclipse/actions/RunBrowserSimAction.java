/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.eclipse.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.tools.browsersim.eclipse.launcher.BrowserSimLauncher;
import org.jboss.tools.browsersim.eclipse.util.CommandUtil;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class RunBrowserSimAction implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {
		String url = CommandUtil.guessUrl();
		BrowserSimLauncher.launchBrowserSim(url);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {		
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}
}