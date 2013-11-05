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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public abstract class BrowserSimBrowser extends Browser implements IBrowser {
	private Map<ExtendedCloseWindowListener, CloseWindowListener> closeWindowListenerMap =
			new HashMap<ExtendedCloseWindowListener, CloseWindowListener>();
	private Map<ExtendedOpenWindowListener, OpenWindowListener> openWindowListenerMap =
			new HashMap<ExtendedOpenWindowListener, OpenWindowListener>();
	private Map<ExtendedVisibilityWindowListener, VisibilityWindowListener> visibilityWindowListenerMap =
			new HashMap<ExtendedVisibilityWindowListener, VisibilityWindowListener>();
	
	
	public BrowserSimBrowser(Composite parent, int style) {
		super(parent, style);
	}

	public abstract void setUserAgent(String defaultUserAgent);

	@Override
	protected void checkSubclass() {
	}
	
	@Override
	public void addCloseWindowListener(final ExtendedCloseWindowListener extendedListener) {
		CloseWindowListener listener = new CloseWindowListener() {
			@Override
			public void close(WindowEvent event) {
				ExtendedWindowEvent extendedEvent = new ExtendedWindowEvent(event); 
				
				extendedListener.close(extendedEvent);
				if (extendedEvent.browser instanceof Browser) {
					event.browser = (Browser) extendedEvent.browser;
				}
			}
		};
		addCloseWindowListener(listener);
		closeWindowListenerMap.put(extendedListener, listener);
	}
	
	@Override
	public void removeCloseWindowListener(ExtendedCloseWindowListener extendedListener) {
		CloseWindowListener listener = closeWindowListenerMap.remove(extendedListener);
		if (listener != null) {
			removeCloseWindowListener(listener);
		}
	}

	@Override
	public void addOpenWindowListener(final ExtendedOpenWindowListener extendedListener) {
		OpenWindowListener listener = new OpenWindowListener() {
			@Override
			public void open(WindowEvent event) {
				ExtendedWindowEvent extendedEvent = new ExtendedWindowEvent(event); 
				
				extendedListener.open(extendedEvent);
				if (extendedEvent.browser instanceof Browser) {
					event.browser = (Browser) extendedEvent.browser;
				}
			}
		};
		addOpenWindowListener(listener);
		openWindowListenerMap.put(extendedListener, listener);
	}
	
	@Override
	public void removeOpenWindowListener(ExtendedOpenWindowListener extendedListener) {
		OpenWindowListener listener = openWindowListenerMap.remove(extendedListener);
		if (listener != null) {
			removeOpenWindowListener(listener);
		}
	}
	
	@Override
	public void addVisibilityWindowListener(final ExtendedVisibilityWindowListener extendedListener) {
		VisibilityWindowListener listener = new VisibilityWindowListener() {
			@Override
			public void show(WindowEvent event) {
				ExtendedWindowEvent extendedEvent = new ExtendedWindowEvent(event);
				extendedListener.show(extendedEvent);
			}

			@Override
			public void hide(WindowEvent event) {
				ExtendedWindowEvent extendedEvent = new ExtendedWindowEvent(event);
				extendedListener.hide(extendedEvent);
			}
		};
		addVisibilityWindowListener(listener);
		visibilityWindowListenerMap.put(extendedListener, listener);
	}
	
	@Override
	public void removeVisibilityWindowListener(ExtendedVisibilityWindowListener extendedListener) {
		VisibilityWindowListener listener = visibilityWindowListenerMap.remove(extendedListener);
		if (listener != null) {
			removeVisibilityWindowListener(listener);
		}
	}
}
