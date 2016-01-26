/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.eclipse.handlers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.IServerModule;

/**
 * 
 * @author Konstantin Marmalyukov
 *
 */
public class OpenInWebBrowserWithBrowsersimEnablementTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		final IServerModule appModule = retrieveServerModuleFromSelectedElement(receiver);
		if(appModule == null) {
			return false;
		}
		final IServer appServer = appModule.getServer();
		boolean res =  appServer != null && appServer.getServerState() == IServer.STATE_STARTED;
		return res;
	}

	/**
	 * Returns the {@link IServerModule} from the selection if this selection's
	 * first element is an {@link IServerModule}, null otherwise.
	 * 
	 * 
	 * @param selection
	 * @return the {@link IServerModule} from the selection if this selection's
	 *         first element is an {@link IServerModule}, null otherwise.
	 */
	private static IServerModule retrieveServerModuleFromSelectedElement(final Object selection) {
		if (selection instanceof IStructuredSelection) {
			final Object selectedObject = ((IStructuredSelection) selection).getFirstElement();
			if (selectedObject instanceof IServerModule) {
				final IServerModule selectedModule = (IServerModule) selectedObject;
				return selectedModule;
			}
		}
		return null;
	}
}
