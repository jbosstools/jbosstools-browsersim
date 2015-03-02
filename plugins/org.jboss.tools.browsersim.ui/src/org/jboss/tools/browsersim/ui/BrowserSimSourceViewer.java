/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Simple text viewer to show page source.
 *
 * @author Konstantin Marmalyukov (kmarmalyukov)
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSimSourceViewer {
	private Text text;
	private Shell shell;

	public BrowserSimSourceViewer(Shell parent) {
		shell = new Shell(parent, SWT.SHELL_TRIM);
	    shell.setLayout(new FillLayout());
	    			    
	    text = new Text(shell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL
	    		| SWT.FULL_SELECTION | SWT.READ_ONLY);
	    text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
	    
	    Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);
		MenuItem editItem = new MenuItem (bar, SWT.CASCADE);
		editItem.setText (Messages.BrowserSimSourceViewer_EDIT);
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		editItem.setMenu (submenu);

		MenuItem copyAllItem = new MenuItem (submenu, SWT.PUSH);
		copyAllItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				text.selectAll();
			}
		});
		copyAllItem.setText (Messages.BrowserSimSourceViewer_SELECT_ALL);
		copyAllItem.setAccelerator (SWT.MOD1 + 'A');
		
		MenuItem copyItem = new MenuItem (submenu, SWT.PUSH);
		copyItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				text.copy();
			}
		});
		copyItem.setText (Messages.BrowserSimSourceViewer_COPY);
		copyItem.setAccelerator (SWT.MOD1 + 'C');
		
		
		// Note that as long as your application has not overridden 
		// the global accelerators for copy, paste, and cut 
		//(CTRL+C or CTRL+INSERT, CTRL+V or SHIFT+INSERT, and CTRL+X or SHIFT+DELETE)
		// these behaviours are already available by default.
		// If your application overrides these accelerators,
		// you will need to call Text.copy(), Text.paste() and Text.cut()
		// from the Selection callback for the accelerator when the 
		// text widget has focus.
	}
	
	public void setText(String textString) {
	    text.setText(textString);
	}
	
	public void open() {
	    shell.open();
	}

	public Shell getShell() {
		return shell;
	}
}
