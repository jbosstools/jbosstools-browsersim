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
package org.jboss.tools.vpe.browsersim.ui;

import org.jboss.tools.vpe.browsersim.browser.IBrowser;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSimControlHandler implements ControlHandler {
	protected IBrowser browser;
	protected SpecificPreferences specificPreferences;
	protected String homeUrl;

	public BrowserSimControlHandler(IBrowser browser, String homeUrl, SpecificPreferences specificPreferences) {
		this.browser = browser;
		this.specificPreferences = specificPreferences;
		this.homeUrl = homeUrl;
	}

	@Override
	public void goBack() {
		browser.back();
		browser.setFocus();
	}

	@Override
	public void goForward() {
		browser.forward();
		browser.setFocus();
	}

	@Override
	public void goHome() {
		browser.setUrl(homeUrl);
		browser.setFocus();
	}

	@Override
	public void goToAddress(String address) {
		browser.setUrl(address);
		browser.setFocus();
	}

	@Override
	public void showContextMenu() {
		// TODO Auto-generated method stub
	}

	@Override
	public void rotate(boolean counterclockwise) {
		int orientationAngle = specificPreferences.getOrientationAngle();
		if (counterclockwise) {
			orientationAngle+= 90;
		} else {
			orientationAngle-= 90;
		}
		
		// normalize angle to be in [-90; 180]
		orientationAngle = ((orientationAngle - 180) % 360) + 180;
		orientationAngle = ((orientationAngle + 90) % 360) - 90;
		
		specificPreferences.setOrientationAngle(orientationAngle);
		specificPreferences.notifyObservers(false);
	}

	@Override
	public void stop() {
		browser.stop();
		browser.setFocus();
	}

	@Override
	public void refresh() {
		browser.refresh();
		browser.setFocus();
	}
}
