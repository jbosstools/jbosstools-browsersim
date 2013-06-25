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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
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
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.util.BrowserSimImageList;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public abstract class DeviceDialog extends Dialog {
	private static final String ERROR_IMAGE_PATH = "icons/error.gif";
	
	protected Device resultDevice;
	protected Device initialDevice;
	protected Shell shell;
	private Composite header;
	private Label statusImageLabel;
	private Text textStatus;
	private Text textName;
	private Text textWidth;
	private Text textHeight;
	private Text textPixelRatio;
	private Text textUserAgent;
	private Button checkButtonUserAgent;
	private Combo comboSkin;
	private Button buttonOk;
	private List<String> skinIds;
	
	private BrowserSimImageList imageList;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DeviceDialog(Shell parent, int style, Device initialDevice) {
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
		shell.setSize(shell.computeSize(330, SWT.DEFAULT));
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
		shell = new Shell(getParent(), getStyle());
		shell.setText(getHeaderText());
		GridLayout shellLayout = new GridLayout(1, false);
		shellLayout.marginWidth = 0;
		shellLayout.marginHeight = 0;
		shell.setLayout(shellLayout);

		imageList = new BrowserSimImageList(shell);
		
		header = new Composite(shell, SWT.NONE);
		header.setLayout(new GridLayout(2, false));
		header.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));
		header.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		
		textStatus = new Text(header, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		GC gc = new GC(textStatus);
		try
		{
		    gc.setFont(textStatus.getFont());
		    FontMetrics fm = gc.getFontMetrics();

		    /* Set the height to 2 rows of characters */
		    data.heightHint = 2 * fm.getHeight() + 4;//4 pixels is added because of MacOS non-standart behaviour
		}
		finally
		{
		    gc.dispose();
		}
		textStatus.setLayoutData(data);
		textStatus.setBackground(textStatus.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		textStatus.setText(getDefaultStatusText());

		Composite body = new Composite(shell, SWT.NONE);
		body.setLayout(new GridLayout(2, false));
		body.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label labelName = new Label(body, SWT.NONE);
		labelName.setText(Messages.EditDeviceDialog_NAME);
		
		textName = new Text(body, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textName.addFocusListener(new FocusGainedTextListener());
		textName.setText(initialDevice.getName());
		textName.setFocus();
		
		Label labelWidth = new Label(body, SWT.NONE);
		labelWidth.setText(Messages.EditDeviceDialog_WIDTH);
		
		textWidth = new Text(body, SWT.BORDER);
		textWidth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textWidth.setTextLimit(4);
		if (initialDevice.getWidth() != Device.DEFAULT_SIZE) {
			textWidth.setText(String.valueOf(initialDevice.getWidth()));
		}
		textWidth.addVerifyListener(new VerifyDigitsListener());
		textWidth.addModifyListener(new ModifySizeListener());
		textWidth.addFocusListener(new FocusGainedTextListener());
		
		Label labelHeight = new Label(body, SWT.NONE);
		labelHeight.setText(Messages.EditDeviceDialog_HEIGHT);
		
		textHeight = new Text(body, SWT.BORDER);
		textHeight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textHeight.setTextLimit(4);
		if (initialDevice.getHeight() != Device.DEFAULT_SIZE) {
			textHeight.setText(String.valueOf(initialDevice.getHeight()));
		}
		textHeight.addVerifyListener(new VerifyDigitsListener());
		textHeight.addModifyListener(new ModifySizeListener());
		textHeight.addFocusListener(new FocusGainedTextListener());
		
		Label labelPixelRatio = new Label(body, SWT.NONE);
		labelPixelRatio.setText(Messages.EditDeviceDialog_PIXEL_RATIO);
		textPixelRatio = new Text(body, SWT.BORDER);
		textPixelRatio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textPixelRatio.setTextLimit(5);
		textPixelRatio.setText(Device.PIXEL_RAIO_FORMAT.format(initialDevice.getPixelRatio()));
		textPixelRatio.addModifyListener(new ModifySizeListener());
		textPixelRatio.addVerifyListener(new VerifyFloatListener());
		
		checkButtonUserAgent = new Button(body, SWT.CHECK);
		checkButtonUserAgent.setText(Messages.EditDeviceDialog_USER_AGENT);
		checkButtonUserAgent.setSelection(initialDevice.getUserAgent() != null);
		
		textUserAgent = new Text(body, SWT.BORDER);
		textUserAgent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if (initialDevice.getUserAgent() != null) {
			textUserAgent.setText(initialDevice.getUserAgent());
		}
		textUserAgent.addFocusListener(new FocusGainedTextListener());
		
		attachCheckBoxToText(checkButtonUserAgent, textUserAgent);
		
		Label labelSkin = new Label(body, SWT.NONE);
		labelSkin.setText(Messages.EditDeviceDialog_SKIN);
		
		comboSkin = new Combo (body, SWT.READ_ONLY);
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
		comboSkin.addModifyListener(new ModifySizeListener());

		Composite buttonsComposite = new Composite(shell, SWT.NONE);
		buttonsComposite.setLayout(new GridLayout(2, true));
		buttonsComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		
		buttonOk = new Button(buttonsComposite, SWT.NONE);
		buttonOk.setText(Messages.EditDeviceDialog_OK);
		buttonOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		buttonOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				double pixelRatio;
				try {
					pixelRatio = Device.PIXEL_RAIO_FORMAT.parse(textPixelRatio.getText()).doubleValue();
				} catch (ParseException e1) {
					pixelRatio = 1.0;
				}
				
				resultDevice = new Device(textName.getText(),
						Integer.valueOf("0" + textWidth.getText()), //$NON-NLS-1$
						Integer.valueOf("0" + textHeight.getText()), //$NON-NLS-1$
						pixelRatio,
						checkButtonUserAgent.getSelection() ? textUserAgent.getText() : null,
						comboSkin.getSelectionIndex() == 0 ? null : skinIds.get(comboSkin.getSelectionIndex()));
				shell.close();
			}
		});
		shell.setDefaultButton(buttonOk);
		
		Button buttonCancel = new Button(buttonsComposite, SWT.NONE);
		buttonCancel.setText(Messages.EditDeviceDialog_CANCEL);
		buttonCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resultDevice = null;
				shell.close();
			}
		});
	}

	private void showStatusImageLabel() {
		if (statusImageLabel == null || statusImageLabel.isDisposed()) {
			statusImageLabel = new Label(header, SWT.NONE);
			statusImageLabel.setImage(imageList.getImage(ERROR_IMAGE_PATH));
			statusImageLabel.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			statusImageLabel.moveAbove(textStatus);
		}
	}
	
	private void hideStatusImageLabel() {
		if (statusImageLabel != null) {
			statusImageLabel.dispose();
		}
	}
	
	private void validate() {
		try {
			double pixelRatio = 0;
			try {
				pixelRatio = Device.PIXEL_RAIO_FORMAT.parse(textPixelRatio.getText()).doubleValue();
			} catch (ParseException e) {
				setMessage(Messages.EditDeviceDialog_PIXEL_RATIO_ERROR);
				buttonOk.setEnabled(false);
				return;
			}
			if (pixelRatio == 0.0) {
				setMessage(Messages.EditDeviceDialog_ZERO_PIXEL_RATIO_ERROR);
				buttonOk.setEnabled(false);
				return;
			}
			
			BrowserSimSkin skin = SkinMap.getInstance().getSkinClass(comboSkin.getText()).newInstance();

			Point minimalSize = skin.getMinimalScreenSize();
			Point effectiveMinimalSize = new Point((int) (minimalSize.x * pixelRatio), (int) (minimalSize.y * pixelRatio));

			int width = 0;
			try {
				width = Integer.parseInt(textWidth.getText());
			} catch (NumberFormatException e) {
				// occurs for empty focused field
				setMessage(Messages.EditDeviceDialog_EMPTY_WIDTH_ERROR);
				buttonOk.setEnabled(false);
				return;
			}
			int height = 0;
			try {
				height = Integer.parseInt(textHeight.getText());
			} catch (NumberFormatException e) {
				// occurs for empty focused field
				setMessage(Messages.EditDeviceDialog_EMPTY_HEIGHT_ERROR);
				buttonOk.setEnabled(false);
				return;
			}
			
			if (width < effectiveMinimalSize.x) {
				setMessage(MessageFormat.format(Messages.EditDeviceDialog_SMALL_WIDTH, effectiveMinimalSize.x, comboSkin.getText(), pixelRatio));
				buttonOk.setEnabled(false);
			} else if (height < effectiveMinimalSize.y) {
				setMessage(MessageFormat.format(Messages.EditDeviceDialog_SMALL_HEIGHT, effectiveMinimalSize.y, comboSkin.getText(), pixelRatio));
				buttonOk.setEnabled(false);
			} else {
				setMessage(null);
				buttonOk.setEnabled(true);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	private void setMessage(String message) {
		if (message == null) {
			textStatus.setText(getDefaultStatusText());
			hideStatusImageLabel();
		} else {
			textStatus.setText(message);
			showStatusImageLabel();
		}
		header.layout();
	}
	
	protected abstract String getHeaderText();
	
	protected abstract String getDefaultStatusText();
	
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
	
	final class ModifySizeListener implements ModifyListener{
		@Override
		public void modifyText(ModifyEvent e) {
			DeviceDialog.this.validate();
		}		
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

final class FocusGainedTextListener extends FocusAdapter {
	public void focusGained(FocusEvent e) {
		Text text = ((Text) e.widget);
		text.setSelection(0, text.getText().length());
	}
}
