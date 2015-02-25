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
package org.jboss.tools.browsersim.ui.skin;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.browsersim.browser.IBrowser;
import org.jboss.tools.browsersim.browser.IBrowserSimBrowserFactory;
import org.jboss.tools.browsersim.ui.ControlHandler;
import org.jboss.tools.browsersim.ui.skin.ios.AppleIPadMiniResizableSkin;
import org.jboss.tools.browsersim.ui.skin.ios.AppleIPadResizableSkin;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public interface BrowserSimSkin {
	void setBrowserFactory(IBrowserSimBrowserFactory browserFactory);
	void createControls(Display display, Point location, Shell parentShell, boolean javaFx);
	IBrowser getBrowser();
	Shell getShell();
	Menu getMenuBar();
	Point getMinimalScreenSize();
	void setControlHandler(ControlHandler controlHandler);
	
	void locationChanged(String newLocation, boolean backEnabled, boolean forwardEnabled);
	void pageTitleChanged(String newTitle);
	void progressChanged(int percents); // -1 for completed
	void statusTextChanged(String newStatusText);
	void setOrientationAndLocationAndSize(int orientation, Point location, Point browserSize, ResizableSkinSizeAdvisor sizeAdvisor);
	void setAddressBarVisible(boolean visible);
	void setContextMenu(Menu contextMenu);
	
	/**
	 * Indicates whether address bar should be hidden automatically.  
	 * {@link NativeSkin}, {@link AppleIPadResizableSkin} and {@link AppleIPadMiniResizableSkin} 
	 * should not hide the adressBar on scroll, whereas other skins should. 
	 * 
	 * @return <tt>true</tt> if address bar should be hidden automatically
	 */
	boolean automaticallyHideAddressBar();
}
