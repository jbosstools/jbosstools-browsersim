/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
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
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ExceptionNotifier {
	
	/**
	 * Should be used to notify user about WebKit-loading errors
	 */
	public static void showWebKitLoadError(Shell parentShell, SWTError error) {
		String os = PlatformUtil.getOs();
		String arch = PlatformUtil.getArch();
		String message;
		if (PlatformUtil.OS_WIN32.equals(os) && !PlatformUtil.ARCH_X86.equals(arch)) {// Eclipse 64-bit on Windows
			message = Messages.ExceptionNotifier_ONLY_32_BIT_ECLIPSE_IS_SUPPORTED_ON_WINDOWS;
		} else if (PlatformUtil.OS_WIN32.equals(os) && PlatformUtil.ARCH_X86.equals(arch) // Eclipse 32-bit on Windows and
				&& error.getMessage() != null											  // Apple Application Support is not installed
				&& error.getMessage().contains("Safari must be installed to use a SWT.WEBKIT-style Browser")) {
			message = Messages.ExceptionNotifier_APPLE_APPLICATION_SUPPORT_IS_NOT_FOUND;
		} else if (PlatformUtil.OS_LINUX.equals(os) && error.getMessage() != null) {    // Linux - probably WebKitGTK is not installed
			message = MessageFormat.format(Messages.ExceptionNotifier_BROWSERSIM_IS_FAILED_TO_START_ON_LINUX, error.getMessage());
		} else {																	  // everything else
			message = MessageFormat.format(Messages.ExceptionNotifier_BROWSERSIM_IS_FAILED_TO_START, error.getMessage());
		}
		showErrorMessageWithLinks(parentShell, message);
	}
	
	public static void showErrorMessage(Shell shell, String message) {
		System.err.println(message);
		
		MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
		messageBox.setText(Messages.BrowserSim_ERROR);
		messageBox.setMessage(message);
		messageBox.open();
	}
	
	public static void showErrorMessageWithLinks(Shell shell, String message) {
		System.err.println(message);
		
		ErrorMessageBoxWithLinks messageBox = new ErrorMessageBoxWithLinks(shell);
		messageBox.setText(Messages.BrowserSim_ERROR);
		messageBox.setMessage(message);
		messageBox.open();
	}
}

/**
 * Behaves like MessageBox with styles SWT.OK and SWT.ICON_ERROR, but allows HTML links is messages. 
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
class ErrorMessageBoxWithLinks extends Dialog {
	private Shell shell;
	private String message;

	public ErrorMessageBoxWithLinks(Shell parent) {
		super(parent, SWT.NONE);
		message = "";
	}

	public void open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		GridLayout shellLayout = new GridLayout(1, true);
		shellLayout.marginHeight = 0;
		shellLayout.marginWidth = 0;
		shellLayout.verticalSpacing = 0;  
		shell.setLayout(shellLayout);
		shell.setText(getText());
		
		Color whiteColor = getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE);
		Composite messageComposite = new Composite(shell, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true); 
		messageComposite.setLayoutData(gridData);
		RowLayout messageCompositeLayout = new RowLayout(SWT.HORIZONTAL);
		messageCompositeLayout.marginHeight = 22;
		messageCompositeLayout.marginWidth = 22;
		messageCompositeLayout.spacing = 10;
		messageCompositeLayout.center = true;
		messageComposite.setLayout(messageCompositeLayout);
		messageComposite.setBackground(whiteColor);
		
		Image errorImage = shell.getDisplay().getSystemImage(SWT.ICON_ERROR);
		Label imageLabel = new Label(messageComposite, SWT.NONE);
		imageLabel.setImage(errorImage);
		imageLabel.setBackground(whiteColor);
		Link link = new Link(messageComposite, SWT.WRAP);
		link.setText(message);
		link.setBackground(whiteColor);
		link.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
				Program.launch(event.text);
			}
		});
		
		Composite okComposite = new Composite(shell, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		okComposite.setLayoutData(gridData);
		GridLayout okCompositeLayout = new GridLayout(1, false);
		okCompositeLayout.marginHeight = 10;
		okCompositeLayout.marginWidth = 10;
		okComposite.setLayout(okCompositeLayout);
		
		Button ok = new Button(okComposite, SWT.PUSH);
		ok.setText("OK");
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		GridData okLayoutData = new GridData(SWT.END, SWT.CENTER, true, true);
		okLayoutData.widthHint = 88;
		ok.setLayoutData(okLayoutData);
		shell.setDefaultButton(ok);

		
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
