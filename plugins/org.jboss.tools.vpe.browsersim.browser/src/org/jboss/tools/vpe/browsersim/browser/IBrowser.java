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

import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public interface IBrowser {
	void addCloseWindowListener(ExtendedCloseWindowListener closeWindowListener);
	void removeCloseWindowListener(ExtendedCloseWindowListener closeWindowListener);
	void addDisposeListener(DisposeListener disposeListener); 
	void removeDisposeListener(DisposeListener disposeListener); 
	void addLocationListener(LocationListener locationListener);
	void removeLocationListener(LocationListener liveReloadLocationAdapter);
	void addOpenWindowListener(ExtendedOpenWindowListener listener);
	void removeOpenWindowListener(ExtendedOpenWindowListener listener);
	void addProgressListener(ProgressListener progressListener);
	void removeProgressListener(ProgressListener progressListener);
	void addStatusTextListener(StatusTextListener statusTextListener);
	void removeStatusTextListener(StatusTextListener statusTextListener);
	void addTitleListener(TitleListener titleListener);
	void removeTitleListener(TitleListener titleListener);
	void addVisibilityWindowListener (ExtendedVisibilityWindowListener listener);
	void removeVisibilityWindowListener (ExtendedVisibilityWindowListener listener);
	
	boolean back();
	boolean forward();
	void refresh();
	void stop();
	
	Object evaluate(String script); 
	boolean execute(String string);
	
	void dispose();
	boolean forceFocus();
	IDisposable registerBrowserFunction(String name, IBrowserFunction iBrowserFunction);
	
	Object getLayoutData();
	Shell getShell();
	Composite getParent();
	String getText();
	String getUrl();
	boolean isBackEnabled();
	boolean isDisposed();
	boolean isForwardEnabled();
	
	void setUserAgent(String userAgent);
	boolean setFocus();
	void setLayoutData(Object layoutData);
	boolean setParent(Composite browserContainer);
	boolean setUrl(String location);
}
