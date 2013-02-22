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

import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class SizeWarningDialog extends CustomMessageBox {
	private Point actualSize;
	private Point requiredSize;
	private String deviceName;
	private boolean vertical;
	private boolean truncateWindow = false;
	private boolean rememberDecision = false;
	
	public SizeWarningDialog(Shell parent, Point actualSize, Point requiredSize, String deviceName, boolean vertical) {
		super(parent, parent.getDisplay().getSystemImage(SWT.ICON_INFORMATION));
		this.actualSize = actualSize;
		this.requiredSize = requiredSize;
		this.deviceName = deviceName;
		this.vertical = vertical;
		setText(Messages.SizeWarningDialog_DEVICE_SIZE_WILL_BE_TRUNCATED);
	}
	
	@Override
	protected void createWidgets() {
		super.createWidgets();
		
		Composite messageRow = new Composite(getMessageComposite(), SWT.NONE);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.spacing = 12;
		messageRow.setLayout(rowLayout);
		messageRow.setBackground(getMessageCompositeBackground());
		
		Label message = new Label(messageRow, SWT.WRAP);
		String messageText;
		if (vertical) {
			messageText = MessageFormat.format(Messages.SizeWarningDialog_DESKTOP_SIZE_TOO_SMALL_VERTICAL,
					actualSize.x, actualSize.y, deviceName, requiredSize.x, requiredSize.y);			
		} else {
			messageText = MessageFormat.format(Messages.SizeWarningDialog_DESKTOP_SIZE_TOO_SMALL_HORIZONTAL,
					actualSize.x, actualSize.y, deviceName, requiredSize.x, requiredSize.y);			
		}
		message.setText(messageText);
		message.setBackground(getMessageCompositeBackground());
		
		Button rememberDecisionCheckbox = new Button(messageRow, SWT.CHECK);
		rememberDecisionCheckbox.setText(Messages.SizeWarningDialog_REMEMBER_MY_DECISION);
		rememberDecisionCheckbox.setBackground(getMessageCompositeBackground());
		rememberDecisionCheckbox.setSelection(rememberDecision);
		rememberDecisionCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rememberDecision = ((Button)e.widget).getSelection();
			}
		});

		Composite buttonRow = new Composite(getButtonsComposite(), SWT.NONE);
		buttonRow.setLayout(new RowLayout(SWT.HORIZONTAL));
		GridData buttonRowLayoutData = new GridData(SWT.END, SWT.CENTER, true, true);
		buttonRow.setLayoutData(buttonRowLayoutData);
		
		Button okButton = new Button(buttonRow, SWT.PUSH);
		okButton.setText(Messages.SizeWarningDialog_OK);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				truncateWindow = true;
				getShell().close();
			}
		});
		
		Button noButton = new Button(buttonRow, SWT.PUSH);
		noButton.setText(Messages.SizeWarningDialog_CANCEL);
		noButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().close();
			}
		});
		
		getShell().setDefaultButton(okButton);
		getShell().pack();
	}
	
	public boolean getTruncateWindow() {
		return truncateWindow;
	}
	
	public boolean getRememberDecision() {
		return rememberDecision;
	}
}
