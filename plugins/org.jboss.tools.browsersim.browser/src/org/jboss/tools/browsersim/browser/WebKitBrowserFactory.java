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
package org.jboss.tools.browsersim.browser;

import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.browsersim.browser.internal.WebKitBrowser_gtk_linux_x86;
import org.jboss.tools.browsersim.browser.internal.WebKitBrowser_gtk_linux_x86_64;
import org.jboss.tools.browsersim.browser.internal.WebKitBrowser_webkit_cocoa_macos;
import org.jboss.tools.browsersim.browser.internal.WebKitBrowser_win32_win32_x86;
import org.jboss.tools.browsersim.browser.javafx.JavaFXBrowser;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 */
public class WebKitBrowserFactory implements IBrowserSimBrowserFactory {
	@Override
	public IBrowser createBrowser(Composite parent, int style, boolean isJavaFx) {
		if (isJavaFx) {
			return new JavaFXBrowser(parent);
		}
		if (PlatformUtil.CURRENT_PLATFORM.equals("gtk.linux.x86")) { //$NON-NLS-1$
			return new WebKitBrowser_gtk_linux_x86(parent, style);
		} else if (PlatformUtil.CURRENT_PLATFORM.equals("gtk.linux.x86_64")) { //$NON-NLS-1$
			return new WebKitBrowser_gtk_linux_x86_64(parent, style);
		} else if (PlatformUtil.CURRENT_PLATFORM.startsWith("cocoa.macosx")) { //$NON-NLS-1$
			return new WebKitBrowser_webkit_cocoa_macos(parent, style);
		} else if (PlatformUtil.CURRENT_PLATFORM.equals("win32.win32.x86")) { //$NON-NLS-1$
			//will work fine because AAS existance checked before browser creation
			return new WebKitBrowser_win32_win32_x86(parent, style);
		}

		throw new SWTError("Unsupported Platform"); //$NON-NLS-1$
	}
}
