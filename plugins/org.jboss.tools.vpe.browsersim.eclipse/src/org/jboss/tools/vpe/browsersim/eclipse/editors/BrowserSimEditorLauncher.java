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

package org.jboss.tools.vpe.browsersim.eclipse.editors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorLauncher;
import org.jboss.tools.vpe.browsersim.eclipse.util.BrowserSimLauncher;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 */
public class BrowserSimEditorLauncher implements IEditorLauncher {
	public static final String EDITOR_ID = "org.jboss.tools.vpe.browsersim.eclipse.editors.BrowserSimLauncher"; //$NON-NLS-1$

	@Override
	public void open(IPath file) {
		BrowserSimLauncher.launchBrowserSim(file.toFile().toURI().toASCIIString()); // XXX: NPE
	}
}
