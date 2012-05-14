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
package org.jboss.tools.vpe.browsersim.ui.skin;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserSimBrowserFactory;
import org.jboss.tools.vpe.browsersim.ui.ControlHandler;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public interface BrowserSimSkin {
	void setBrowserFactory(IBrowserSimBrowserFactory browserFactory);
	void createControls(Display display);
	BrowserSimBrowser getBrowser();
	Shell getShell();
	Menu getMenuBar();
	void setControlHandler(ControlHandler controlHandler);
	
	void locationChanged(String newLocation, boolean backEnabled, boolean forwardEnabled);
	void pageTitleChanged(String newTitle);
	void progressChanged(int percents); // -1 for completed
	void statusTextChanged(String newStatusText);
	void setOrientationAndSize(int orientation, Point browserSize, ResizableSkinSizeAdvisor sizeAdvisor);
	void setAddressBarVisible(boolean visible);
	void setContextMenu(Menu contextMenu);
}
