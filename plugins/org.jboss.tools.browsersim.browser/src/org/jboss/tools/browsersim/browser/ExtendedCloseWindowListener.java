/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc. and others.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.jboss.tools.browsersim.browser;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.internal.SWTEventListener;

/**
 * This listener interface may be implemented in order to receive
 * a {@link ExtendedWindowEvent} notification when a {@link Browser} is 
 * about to be closed and when its host window should be closed
 * by the application.
 * 
 * @see Browser#addCloseWindowListener(ExtendedCloseWindowListener)
 * @see Browser#removeCloseWindowListener(ExtendedCloseWindowListener)
 * @see OpenWindowListener
 * @see VisibilityWindowListener
 * 
 */
public interface ExtendedCloseWindowListener extends SWTEventListener {

/**
 * This method is called when the window hosting a {@link Browser} should be closed.
 * Application would typically close the {@link org.eclipse.swt.widgets.Shell} that
 * hosts the <code>Browser</code>. The <code>Browser</code> is disposed after this
 * notification.
 *
 * <p>The following fields in the <code>ExtendedWindowEvent</code> apply:
 * <ul>
 * <li>(in) widget the <code>Browser</code> that is going to be disposed
 * </ul></p>
 *
 * @param event the <code>ExtendedWindowEvent</code> that specifies the <code>Browser</code>
 * that is going to be disposed
 * 
 * @see org.eclipse.swt.widgets.Shell#close()
 * 
 */ 
public void close(ExtendedWindowEvent event);
}
