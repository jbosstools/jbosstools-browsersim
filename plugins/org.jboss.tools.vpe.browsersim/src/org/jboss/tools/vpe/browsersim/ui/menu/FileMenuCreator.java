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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;
import org.jboss.tools.vpe.browsersim.ui.BrowserSimSourceViewer;
import org.jboss.tools.vpe.browsersim.ui.ExceptionNotifier;
import org.jboss.tools.vpe.browsersim.ui.Messages;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */

public class FileMenuCreator {
	/** @see org.jboss.tools.vpe.browsersim.eclipse.callbacks.OpenFileCallback */
	private static final String OPEN_FILE_COMMAND = "org.jboss.tools.vpe.browsersim.command.openFile:"; //$NON-NLS-1$
	/** @see org.jboss.tools.vpe.browsersim.eclipse.callbacks.ViewSourceCallback */
	private static final String VIEW_SOURCE_COMMAND = "org.jboss.tools.vpe.browsersim.command.viewSource:"; //$NON-NLS-1$

	public static void addItems(final Menu menu, final BrowserSimSkin skin) {
		addOpenInDefaultBrowserItem(menu, skin);
		addViewSourceItem(menu, skin);
	}
	
	private static void addOpenInDefaultBrowserItem(final Menu menu, final BrowserSimSkin skin) {
		MenuItem openInDefaultBrowser = new MenuItem(menu, SWT.PUSH);
		openInDefaultBrowser.setText(Messages.BrowserSim_OPEN_IN_DEFAULT_BROWSER);
		openInDefaultBrowser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				URL url;
				try {
					url = new URL(skin.getBrowser().getUrl());// validate URL (to do not open 'about:blank' and similar)
					Program.launch(url.toString());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
					ExceptionNotifier.showErrorMessage(skin.getShell(), Messages.BrowserSim_COULD_NOT_OPEN_DEFAULT_BROWSER + e1.getMessage());
				}
			}
		});
	}
	
	private static void addViewSourceItem(final Menu menu, final BrowserSimSkin skin) {
		MenuItem openInDefaultBrowser = new MenuItem(menu, SWT.PUSH);
		openInDefaultBrowser.setText(Messages.BrowserSim_VIEW_PAGE_SOURCE);
		openInDefaultBrowser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (BrowserSim.isStandalone) {
					BrowserSimSourceViewer sourceViewer = new BrowserSimSourceViewer();
					sourceViewer.setText(skin.getBrowser().getText());
					sourceViewer.open();
				} else {
					if (skin.getBrowser().getUrl().startsWith("file:")) { //$NON-NLS-1$
						URI uri = null;
						try {
							uri = new URI(skin.getBrowser().getUrl());
							File sourceFile = new File(uri);
							System.out.println(OPEN_FILE_COMMAND + sourceFile.getAbsolutePath()); // send command to Eclipse
						} catch (URISyntaxException e1) {
							e1.printStackTrace();
						}
					} else {
						System.out.println(VIEW_SOURCE_COMMAND + skin.getBrowser().getUrl()); // send command to Eclipse
						String source = skin.getBrowser().getText();
						String base64Source = DatatypeConverter.printBase64Binary(source.getBytes());
						System.out.println(base64Source);
					}
				}
			}
		});
	}
}
