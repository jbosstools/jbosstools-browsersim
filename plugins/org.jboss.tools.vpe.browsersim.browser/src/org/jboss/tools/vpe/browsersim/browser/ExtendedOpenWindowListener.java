/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc. and others.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.jboss.tools.vpe.browsersim.browser;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.internal.SWTEventListener;

/** 
 * This listener interface may be implemented in order to receive
 * a {@link ExtendedWindowEvent} notification when a new {@link Browser}
 * needs to be provided by the application.
 * 
 * @see Browser#addOpenWindowListener(OpenWindowListener)
 * @see Browser#removeOpenWindowListener(OpenWindowListener)
 * @see ExtendedCloseWindowListener
 * @see VisibilityWindowListener
 * 
 */
public interface ExtendedOpenWindowListener extends SWTEventListener {

/**
 * This method is called when a new window needs to be created.
 * <p>
 * A particular <code>Browser</code> can be passed to the event.browser
 * field to host the content of a new window.
 * <p>
 * A standalone system browser is used to host the new window
 * if the event.required field value is false and if the event.browser 
 * field is left <code>null</code>. The event.required field
 * is true on platforms that don't support a standalone system browser for
 * new window requests. 
 * <p>
 * The navigation is cancelled if the event.required field is set to
 * true and the event.browser field is left <code>null</code>.
 * <p>
 * <p>The following fields in the <code>ExtendedWindowEvent</code> apply:
 * <ul>
 * <li>(in/out) required true if the platform requires the user to provide a
 * <code>Browser</code> to handle the new window or false otherwise.
 * <li>(out) browser the new <code>Browser</code> that will host the 
 * content of the new window.
 * <li>(in) widget the <code>Browser</code> that is requesting to open a 
 * new window
 * </ul>
 * 
 * @param event the <code>ExtendedWindowEvent</code> that needs to be passed a new
 * <code>Browser</code> to handle the new window request
 * 
 */ 
public void open(ExtendedWindowEvent event);
}
