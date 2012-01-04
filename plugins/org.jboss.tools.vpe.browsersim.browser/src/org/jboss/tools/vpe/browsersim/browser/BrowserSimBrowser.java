package org.jboss.tools.vpe.browsersim.browser;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

public abstract class BrowserSimBrowser extends Browser {

	public BrowserSimBrowser(Composite parent, int style) {
		super(parent, style);
	}

	public abstract void setDefaultUserAgent(String defaultUserAgent);

	@Override
	protected void checkSubclass() {
	}
}
