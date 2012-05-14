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

class SizeWarningDialog extends CustomMessageBox {
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
		setText("Device size will be truncated");
	}
	
	@Override
	protected void createWidgets() {
		super.createWidgets();
		
		Composite messageRow = new Composite(getMessageComposite(), SWT.NONE);
		messageRow.setLayout(new RowLayout(SWT.VERTICAL));
		messageRow.setBackground(getMessageCompositeBackground());
		
		Label message = new Label(messageRow, SWT.WRAP);
		String messageText;
		if (vertical) {
			messageText = MessageFormat.format("Your desktop size ({0}x{1} pixels) is smaller than what {2} needs in vertical layout ({3}x{4}).\n" +
					"Device size will be truncated to fit your desktop.",
					actualSize.x, actualSize.y, deviceName, requiredSize.x, requiredSize.y);			
		} else {
			messageText = MessageFormat.format("Your desktop size ({0}x{1} pixels) is smaller than what {2} needs in horizontal layout ({3}x{4}).\n" +
					"Device size will be truncated to fit your desktop.",
					actualSize.x, actualSize.y, deviceName, requiredSize.x, requiredSize.y);			
		}
		message.setText(messageText);
		message.setBackground(getMessageCompositeBackground());
		
		Button rememberDecisionCheckbox = new Button(messageRow, SWT.CHECK);
		rememberDecisionCheckbox.setText("Remember my decision (this can be changed in the Preferences dialog later).");
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
		okButton.setText("Yes (recommended)");
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				truncateWindow = true;
				getShell().close();
			}
		});
		
		Button noButton = new Button(buttonRow, SWT.PUSH);
		noButton.setText("No, show me actual size");
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