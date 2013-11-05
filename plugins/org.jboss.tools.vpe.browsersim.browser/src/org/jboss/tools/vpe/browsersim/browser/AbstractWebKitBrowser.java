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
package org.jboss.tools.vpe.browsersim.browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public abstract class AbstractWebKitBrowser extends BrowserSimBrowser {

	private String userAgent;

	public AbstractWebKitBrowser(Composite parent, int style) {
		super(parent, style | SWT.WEBKIT);
	}

	@Override
	public boolean setUrl(String url, String postData, String[] headers) {
		if (url != null && url.trim().isEmpty()) {
			/* If the url is empty, then just ignore it to avoid
			 * StringIndexOutOfBoundsException under Linux (JBIDE-11165)
			 * (in the case if url == null, then super.setUrl
			 * will throw an IllegalArgumentException, thus we do not need to handle this).*/
			return false;
		} else {
			setUserAgentImpl(userAgent);
			boolean result = super.setUrl(url, postData, headers); // setUrl resets user-agent
			setUserAgentImpl(userAgent);
			return result;
		}
	}

	@Override
	public void setUserAgent(String defaultUserAgent) {
		this.userAgent = defaultUserAgent;
		setUserAgentImpl(defaultUserAgent);
	}
	
	protected abstract void setUserAgentImpl(String userAgent);
	
	@Override
	public IDisposable registerBrowserFunction(String name, final IBrowserFunction iBrowserFunction) {
		final BrowserFunction function = new BrowserFunction(this, name) {
			@Override
			public Object function(Object[] arguments) {
				return iBrowserFunction.function(arguments);
			}
		}; 
		
		return new IDisposable() {
			@Override
			public boolean isDisposed() {
				return function.isDisposed();
			}
			
			@Override
			public void dispose() {
				function.dispose();
			}
		};
	}
}
