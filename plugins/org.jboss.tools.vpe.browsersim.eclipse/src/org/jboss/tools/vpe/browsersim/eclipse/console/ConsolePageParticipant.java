/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.eclipse.console;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;
import org.jboss.tools.vpe.browsersim.eclipse.Activator;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class ConsolePageParticipant implements IConsolePageParticipant {
	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class arg) {
		return null;
	}

	@Override
	public void activated() {
	}

	@Override
	public void deactivated() {
	}

	@Override
	public void dispose() {
		Activator.getDefault().removeViewerWithPageParticipant(this);
	}

	@Override
	public void init(IPageBookViewPage page, IConsole console) {
		if (page.getControl() instanceof StyledText) {
			StyledText viewer = (StyledText) page.getControl();
			ConsoleStyleListener styleListeneristener = new ConsoleStyleListener();
			viewer.addLineStyleListener(styleListeneristener);
			Activator.getDefault().addViewer(viewer, this);
		}
	}

}
