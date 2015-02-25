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
package org.jboss.tools.browsersim.ui;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public interface ControlHandler {
	void goBack();
	void goForward();
	void goHome();
	void goToAddress(String address);
	void showContextMenu();
	void rotate(boolean counterclockwise);
	void stop();
	void refresh();
}
