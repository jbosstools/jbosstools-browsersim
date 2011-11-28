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
package org.jboss.tools.vpe.browsersim.browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public abstract class AbstractWebKitBrowser extends Browser {

	private String defaultUserAgent;

	public AbstractWebKitBrowser(Composite parent, int style) {
		super(parent, style | SWT.WEBKIT);
	}

	@Override
	protected void checkSubclass() {
	}

	@Override
	public boolean setUrl(String url, String postData, String[] headers) {
		setCustomUserAgent(defaultUserAgent);
		boolean result = super.setUrl(url, postData, headers);
		setCustomUserAgent(defaultUserAgent);
		return result;
	}

	public void setDefaultUserAgent(String defaultUserAgent) {
		this.defaultUserAgent = defaultUserAgent;
		setCustomUserAgent(defaultUserAgent);
	}
	
	protected abstract void setCustomUserAgent(String userAgent);
}
