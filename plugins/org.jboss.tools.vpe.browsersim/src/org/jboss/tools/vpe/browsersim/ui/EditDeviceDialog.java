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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.SkinMap;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class EditDeviceDialog extends Dialog {
	protected Device resultDevice;
	protected Device initialDevice;
	protected Shell shell;
	private Text textName;
	private Text textWidth;
	private Text textHeight;
	private Text textPixelRatio;
	private Text textUserAgent;
	private Button checkButtonWidth;
	private Button checkButtonHeight;
	private Button checkButtonUserAgent;
	private Combo comboSkin;
	private List<String> skinIds;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EditDeviceDialog(Shell parent, int style, Device initialDevice) {
		super(parent, style);
		setText(Messages.EditDeviceDialog_MANAGE_DEVICES);
		this.initialDevice = initialDevice;
	}

	/**
	 * Open the dialog.
	 * @return the newDevicesList
	 */
	public Device open() {
		createContents();
		shell.open();
		shell.layout();
		shell.setSize(shell.computeSize(300, SWT.DEFAULT));
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		return resultDevice;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX);
		shell.setSize(450, 300);
		shell.setText(Messages.EditDeviceDialog_EDIT_DEVICE);
		shell.setLayout(new GridLayout(2, false));
		
		Label labelName = new Label(shell, SWT.NONE);
		labelName.setText(Messages.EditDeviceDialog_NAME);
		
		textName = new Text(shell, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textName.addFocusListener(new FocusGainedTextListener());
		textName.setText(initialDevice.getName());

		checkButtonWidth = new Button(shell, SWT.CHECK);
		checkButtonWidth.setText(Messages.EditDeviceDialog_WIDTH);
		checkButtonWidth.setSelection(initialDevice.getWidth() != Device.DEFAULT_SIZE);
		
		textWidth = new Text(shell, SWT.BORDER);
		textWidth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textWidth.setTextLimit(4);
		textWidth.addVerifyListener(new VerifyDigitsListener());
		textWidth.addFocusListener(new FocusLostDigitsListener());
		textWidth.addFocusListener(new FocusGainedTextListener());
		if (initialDevice.getWidth() != Device.DEFAULT_SIZE) {
			textWidth.setText(String.valueOf(initialDevice.getWidth()));
		}
		attachCheckBoxToText(checkButtonWidth, textWidth);
		
		checkButtonHeight = new Button(shell, SWT.CHECK);
		checkButtonHeight.setText(Messages.EditDeviceDialog_HEIGHT);
		checkButtonHeight.setSelection(initialDevice.getHeight() != Device.DEFAULT_SIZE);
		
		textHeight = new Text(shell, SWT.BORDER);
		textHeight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textHeight.setTextLimit(4);
		textHeight.addVerifyListener(new VerifyDigitsListener());
		textHeight.addFocusListener(new FocusLostDigitsListener());
		textHeight.addFocusListener(new FocusGainedTextListener());
		if (initialDevice.getHeight() != Device.DEFAULT_SIZE) {
			textHeight.setText(String.valueOf(initialDevice.getHeight()));
		}
		attachCheckBoxToText(checkButtonHeight, textHeight);
		
		Label labelPixelRatio = new Label(shell, SWT.NONE);
		labelPixelRatio.setText(Messages.EditDeviceDialog_PIXEL_RATIO);
		textPixelRatio = new Text(shell, SWT.BORDER);
		textPixelRatio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPixelRatio.setTextLimit(5);
		textPixelRatio.addVerifyListener(new VerifyFloatListener());

		textPixelRatio.setText(Device.PIXEL_RAIO_FORMAT.format(initialDevice.getPixelRatio()));
		
		checkButtonUserAgent = new Button(shell, SWT.CHECK);
		checkButtonUserAgent.setText(Messages.EditDeviceDialog_USER_AGENT);
		checkButtonUserAgent.setSelection(initialDevice.getUserAgent() != null);
		
		textUserAgent = new Text(shell, SWT.BORDER);
		textUserAgent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textUserAgent.addFocusListener(new FocusGainedTextListener());
		if (initialDevice.getUserAgent() != null) {
			textUserAgent.setText(initialDevice.getUserAgent());
		}
		
		attachCheckBoxToText(checkButtonUserAgent, textUserAgent);
		
		Label labelSkin = new Label(shell, SWT.NONE);
		labelSkin.setText(Messages.EditDeviceDialog_SKIN);
		
		comboSkin = new Combo (shell, SWT.READ_ONLY);
		comboSkin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		skinIds = new ArrayList<String>(SkinMap.getInstance().getSkinIds());
		Collections.sort(skinIds, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});		
		skinIds.add(0, Messages.EditDeviceDialog_NONE);
		comboSkin.setItems(skinIds.toArray(new String[0]));
		comboSkin.setText(initialDevice.getSkinId() == null ? Messages.EditDeviceDialog_NONE : initialDevice.getSkinId());
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 2, 1));
		
		Button buttonOk = new Button(composite, SWT.NONE);
		buttonOk.setText(Messages.EditDeviceDialog_OK);
		buttonOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				double pixelRatio;
				try {
					pixelRatio = Device.PIXEL_RAIO_FORMAT.parse(textPixelRatio.getText()).doubleValue();
				} catch (ParseException e1) {
					pixelRatio = 1.0;
				}
				
				resultDevice = new Device(textName.getText(),
						checkButtonWidth.getSelection() ? Integer.valueOf("0" + textWidth.getText()) : Device.DEFAULT_SIZE, //$NON-NLS-1$
						checkButtonHeight.getSelection() ? Integer.valueOf("0" + textHeight.getText()) : Device.DEFAULT_SIZE, //$NON-NLS-1$
						pixelRatio,
						checkButtonUserAgent.getSelection() ? textUserAgent.getText() : null,
						comboSkin.getSelectionIndex() == 0 ? null : skinIds.get(comboSkin.getSelectionIndex()));
				shell.close();
			}
		});
		shell.setDefaultButton(buttonOk);
		
		Button buttonCancel = new Button(composite, SWT.NONE);
		buttonCancel.setText(Messages.EditDeviceDialog_CANCEL);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resultDevice = null;
				shell.close();
			}
		});
	}

	private void attachCheckBoxToText(Button checkBox, final Text text) {
		if (checkBox.getSelection()) {
			text.setEnabled(true);
		} else {
			text.setEnabled(false);
		}
		checkBox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button)e.widget).getSelection()) {
					text.setEnabled(true);
				} else {
					text.setEnabled(false);
				}
			}
		});
	}
}

final class VerifyFloatListener implements VerifyListener {
	public void verifyText(VerifyEvent e) {
		for (char c : e.text.toCharArray()) {
			if (!('0' <= c && c <= '9') && c != ',' && c != '.') {
				e.doit = false;
				return;
			}
		}
		
		Text text = (Text) e.widget;
		String oldText = text.getText();
		String newText = oldText.substring(0, e.start) + e.text + oldText.substring(e.end);

		int delimiterCounter = 0;
		for (char c : newText.toCharArray()) {
			if (c == ',' || c == '.') {
				delimiterCounter++;
			}
		}
		if (delimiterCounter > 1) {
			e.doit = false;
			return;
		}
		
		e.text = e.text.replace(',', '.');
	}
}

final class VerifyDigitsListener implements VerifyListener {
	public void verifyText(VerifyEvent e) {
		for (char c : e.text.toCharArray()) {
			if (!('0' <= c && c <= '9')) {
				e.doit = false;
				return;
			}
		}
	}
}

final class FocusLostDigitsListener extends FocusAdapter {
	public void focusLost(FocusEvent e) {
		Text text = ((Text) e.widget);
		if (text.getText().trim().isEmpty()) {
			text.setText("0"); //$NON-NLS-1$
		}
	}
}

final class FocusGainedTextListener extends FocusAdapter {
	public void focusGained(FocusEvent e) {
		Text text = ((Text) e.widget);
		text.setSelection(0, text.getText().length());
	}
}
