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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Behaves like MessageBox with styles SWT.OK and SWT.ICON_ERROR, but allows
 * HTML links is messages.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class MessageBoxWithLinks extends CustomMessageBox {
	private String message;
	private String header;

	public MessageBoxWithLinks(Shell parent, String message, Image icon,
			String header) {
		super(parent, icon);
		this.message = message;
		this.header = header;
	}

	@Override
	protected void createWidgets() {
		super.createWidgets();
		Link link = new Link(getMessageComposite(), SWT.WRAP);
		link.setText(message);
		link.setBackground(getMessageCompositeBackground());
		link.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Program.launch(event.text);
			}
		});
		
		addButtons();
		getShell().setText(header);
		getShell().pack();
	}
	
	protected void addButtons() {
		Button ok = new Button(getButtonsComposite(), SWT.PUSH);
		ok.setText(Messages.ExceptionNotifier_OK);
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().close();
			}
		});
		GridData okLayoutData = new GridData(SWT.END, SWT.CENTER, true, true);
		okLayoutData.widthHint = 88;
		ok.setLayoutData(okLayoutData);
		getShell().setDefaultButton(ok);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setHeader(String header) {
		this.header = header;
	}
}
