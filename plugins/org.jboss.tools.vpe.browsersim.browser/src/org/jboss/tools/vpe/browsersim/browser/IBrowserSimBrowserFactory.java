package org.jboss.tools.vpe.browsersim.browser;

import org.eclipse.swt.widgets.Composite;

public interface IBrowserSimBrowserFactory {
	BrowserSimBrowser createBrowser(Composite parent, int style);
}