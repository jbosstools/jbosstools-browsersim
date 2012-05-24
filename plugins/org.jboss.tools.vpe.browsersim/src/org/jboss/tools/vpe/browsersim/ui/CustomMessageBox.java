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
package org.jboss.tools.vpe.browsersim.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
class CustomMessageBox extends Dialog {
	private Shell shell;
	private Composite messageComposite;
	private Composite buttonsComposite;
	private Image icon;
	
	public CustomMessageBox(Shell parent, Image icon) {
		super(parent, SWT.NONE);
		this.icon = icon;
	}
	
	public void open() {
		createWidgets();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	protected void createWidgets() {
		shell = new Shell(getParent(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		GridLayout shellLayout = new GridLayout(1, true);
		shellLayout.marginHeight = 0;
		shellLayout.marginWidth = 0;
		shellLayout.verticalSpacing = 0;  
		shell.setLayout(shellLayout);
		shell.setText(getText());
		
		messageComposite = new Composite(shell, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true); 
		messageComposite.setLayoutData(gridData);
		RowLayout messageCompositeLayout = new RowLayout(SWT.HORIZONTAL);
		messageCompositeLayout.marginHeight = 22;
		messageCompositeLayout.marginWidth = 22;
		messageCompositeLayout.spacing = 10;
		messageCompositeLayout.center = true;
		messageComposite.setLayout(messageCompositeLayout);
		messageComposite.setBackground(getMessageCompositeBackground());
		
		if (icon != null) {
			Label iconLabel = new Label(messageComposite, SWT.NONE);
			iconLabel.setImage(icon);
			iconLabel.setBackground(getMessageCompositeBackground());
		}
		
		buttonsComposite = new Composite(shell, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		buttonsComposite.setLayoutData(gridData);
		GridLayout buttonsCompositeLayout = new GridLayout(1, false);
		buttonsCompositeLayout.marginHeight = 10;
		buttonsCompositeLayout.marginWidth = 10;
		buttonsComposite.setLayout(buttonsCompositeLayout);
	}
	
	protected Composite getMessageComposite() {
		return messageComposite;
	}
	
	protected Composite getButtonsComposite() {
		return buttonsComposite;
	}
	
	protected Color getMessageCompositeBackground() {
		 return getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE);
	}

	protected Shell getShell() {
		return shell;
	}
}