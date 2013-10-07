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
package org.jboss.tools.vpe.browsersim.ui.menu;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.BrowserSimArgs;
import org.jboss.tools.vpe.browsersim.BrowserSimLogger;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.model.preferences.CommonPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;
import org.jboss.tools.vpe.browsersim.ui.BrowserSimSourceViewer;
import org.jboss.tools.vpe.browsersim.ui.ExceptionNotifier;
import org.jboss.tools.vpe.browsersim.ui.ManageDevicesDialog;
import org.jboss.tools.vpe.browsersim.ui.Messages;
import org.jboss.tools.vpe.browsersim.ui.PreferencesWrapper;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */

public class FileMenuCreator {
	/** @see org.jboss.tools.vpe.browsersim.eclipse.callbacks.OpenFileCallback */
	protected static final String OPEN_FILE_COMMAND = "org.jboss.tools.vpe.browsersim.command.openFile:"; //$NON-NLS-1$
	/** @see org.jboss.tools.vpe.browsersim.eclipse.callbacks.ViewSourceCallback */
	private static final String VIEW_SOURCE_COMMAND = "org.jboss.tools.vpe.browsersim.command.viewSource:"; //$NON-NLS-1$

	public void addItems(final Menu menu, final BrowserSimSkin skin, final CommonPreferences commonPreferences, final SpecificPreferences specificPreferences) {
		addOpenInDefaultBrowserItem(menu, skin);
		addViewSourceItem(menu, skin);

		// If Platform is Mac OS X, application will have no duplicated menu items (Preferences)
		if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			addPreferencesItem(menu, commonPreferences, specificPreferences);
		}
	}
	
	private void addOpenInDefaultBrowserItem(final Menu menu, final BrowserSimSkin skin) {
		MenuItem openInDefaultBrowser = new MenuItem(menu, SWT.PUSH);
		openInDefaultBrowser.setText(Messages.BrowserSim_OPEN_IN_DEFAULT_BROWSER);
		openInDefaultBrowser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				URL url;
				try {
					url = new URL(skin.getBrowser().getUrl());// validate URL (to do not open 'about:blank' and similar)
					Program.launch(url.toString());
				} catch (MalformedURLException e) {
					BrowserSimLogger.logError(e.getMessage(), e);
					ExceptionNotifier.showErrorMessage(skin.getShell(), Messages.BrowserSim_COULD_NOT_OPEN_DEFAULT_BROWSER + e.getMessage());
				}
			}
		});
	}
	
	private void addViewSourceItem(final Menu menu, final BrowserSimSkin skin) {
		MenuItem openInDefaultBrowser = new MenuItem(menu, SWT.PUSH);
		openInDefaultBrowser.setText(Messages.BrowserSim_VIEW_PAGE_SOURCE);
		openInDefaultBrowser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (BrowserSimArgs.standalone) {
					viewSource(skin);
				} else {
					if (skin.getBrowser().getUrl().startsWith("file:")) { //$NON-NLS-1$
						openFile(skin.getBrowser().getUrl());
					} else {
						viewServerSource(skin.getBrowser());
					}
				}
			}
		});
	}
	
	private void viewSource(BrowserSimSkin skin) {
		final BrowserSimSourceViewer sourceViewer = new BrowserSimSourceViewer(BrowserSimUtil.getParentShell(skin));
		sourceViewer.setText(skin.getBrowser().getText());
		sourceViewer.open();
		
		skin.getShell().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				Shell sourceShell = sourceViewer.getShell();
				if (!sourceShell.isDisposed()) {
					sourceShell.dispose();
				}
			}
		});		
	}
	
	protected void openFile(String path) {		
		URI uri = null;
		try {
			uri = new URI(path);
			File sourceFile = new File(uri);
			System.out.println(OPEN_FILE_COMMAND + sourceFile.getAbsolutePath()); // send command to Eclipse
		} catch (URISyntaxException e) {
			BrowserSimLogger.logError(e.getMessage(), e);
		}
	}
	
	protected void viewServerSource(Browser browser) {
		System.out.println(VIEW_SOURCE_COMMAND + browser.getUrl()); // send command to Eclipse
		String source = browser.getText();
		String base64Source = DatatypeConverter.printBase64Binary(source.getBytes());
		System.out.println(base64Source);
	}
	
	private void addPreferencesItem(Menu menu, final CommonPreferences commonPreferences, final SpecificPreferences specificPreferences) {
		MenuItem preferences = new MenuItem(menu, SWT.PUSH);
		preferences.setText(Messages.BrowserSim_PREFERENCES);
		preferences.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PreferencesWrapper pw = new ManageDevicesDialog(Display.getDefault().getActiveShell(), SWT.APPLICATION_MODAL
						| SWT.SHELL_TRIM, commonPreferences, specificPreferences).open();
				if (pw != null) {
					commonPreferences.copyProperties(pw.getCommonPreferences());
					specificPreferences.copyProperties(pw.getSpecificPreferences());
					commonPreferences.notifyObservers();
					specificPreferences.notifyObservers();
				}
			}
		});
	}
}
