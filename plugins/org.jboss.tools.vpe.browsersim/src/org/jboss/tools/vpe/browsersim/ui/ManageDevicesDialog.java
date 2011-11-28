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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.DevicesList;
import org.jboss.tools.vpe.browsersim.model.DevicesListStorage;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ManageDevicesDialog extends Dialog {

	protected DevicesList oldDevicesList;
	protected List<Device> devices;
	protected int selectedDeviceIndex;
	protected Shell shell;
	private Table table;
	private DevicesList resultDevicesList;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @param oldDevicesList 
	 */
	public ManageDevicesDialog(Shell parent, int style, DevicesList oldDevicesList) {
		super(parent, style);
		setText(Messages.ManageDevicesDialog_DEVICES);
		this.oldDevicesList = oldDevicesList;
		this.devices = new ArrayList<Device>(oldDevicesList.getDevices());
		this.selectedDeviceIndex = oldDevicesList.getSelectedDeviceIndex();
	} 

	/**
	 * Open the dialog.
	 * @return the newDevicesList
	 */
	public DevicesList open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return resultDevicesList;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectedDeviceIndex = ((Table)e.widget).getSelectionIndex();
			}
		});
		
		TableColumn tableColumnName = new TableColumn(table, SWT.NONE);
		tableColumnName.setWidth(100);
		tableColumnName.setText(Messages.ManageDevicesDialog_NAME);
		
		TableColumn tableColumnWidth = new TableColumn(table, SWT.NONE);
		tableColumnWidth.setWidth(100);
		tableColumnWidth.setText(Messages.ManageDevicesDialog_WIDTH);
		
		TableColumn tableColumnHeight = new TableColumn(table, SWT.NONE);
		tableColumnHeight.setWidth(100);
		tableColumnHeight.setText(Messages.ManageDevicesDialog_HEIGHT);
		
		TableColumn tableColumnUseragent = new TableColumn(table, SWT.NONE);
		tableColumnUseragent.setWidth(100);
		tableColumnUseragent.setText(Messages.ManageDevicesDialog_USER_AGENT);
		
		Composite compositeControls = new Composite(shell, SWT.NONE);
		compositeControls.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		compositeControls.setLayout(new FillLayout(SWT.VERTICAL));
		
		Button buttonAdd = new Button(compositeControls, SWT.NONE);
		buttonAdd.setText(Messages.ManageDevicesDialog_ADD);
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device newDevice = new EditDeviceDialog(shell,  SWT.APPLICATION_MODAL | SWT.SHELL_TRIM,
						new Device(Messages.ManageDevicesDialog_NEW_DEVICE, 480, 800, Messages.ManageDevicesDialog_NEW_USER_AGENT)).open();
				if (newDevice != null) {
					devices.add(newDevice);
					selectedDeviceIndex = devices.size() - 1;
					updateDevices();
				}
			}
		});
		
		Button buttonEdit = new Button(compositeControls, SWT.NONE);
		buttonEdit.setText(Messages.ManageDevicesDialog_EDIT);
		buttonEdit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device newDevice = new EditDeviceDialog(shell,  SWT.APPLICATION_MODAL | SWT.SHELL_TRIM,
						devices.get(selectedDeviceIndex)).open();
				if (newDevice != null) {
					devices.remove(selectedDeviceIndex);
					devices.add(selectedDeviceIndex, newDevice);
					updateDevices();
				}
			}
		});
		
		Button buttonRemove = new Button(compositeControls, SWT.NONE);
		buttonRemove.setText(Messages.ManageDevicesDialog_REMOVE);
		buttonRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (devices.size() > 1) {
					devices.remove(selectedDeviceIndex);
					if (selectedDeviceIndex >= devices.size()) {
						selectedDeviceIndex = devices.size() - 1;
					}
					updateDevices();
				}
			}
		});
		
		Button buttonReset = new Button(compositeControls, SWT.NONE);
		buttonReset.setText(Messages.ManageDevicesDialog_REVERT_ALL);
		
		buttonReset.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				devices  = new ArrayList<Device>(oldDevicesList.getDevices());
				selectedDeviceIndex = oldDevicesList.getSelectedDeviceIndex();
				updateDevices();
			}
		});

		new Label(compositeControls, SWT.NONE);
		
		Button buttonLoadDefaults = new Button(compositeControls, SWT.NONE);
		buttonLoadDefaults.setText(Messages.ManageDevicesDialog_LOAD_DEFAULTS);
		buttonLoadDefaults.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DevicesList defaultDevicesList = DevicesListStorage.loadDefaultDevicesList();
				devices = defaultDevicesList.getDevices();
				selectedDeviceIndex = defaultDevicesList.getSelectedDeviceIndex();
				updateDevices();
			}
		});
		
		Composite compositeOkCancel = new Composite(shell, SWT.NONE);
		compositeOkCancel.setLayout(new FillLayout(SWT.HORIZONTAL));
		compositeOkCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		
		Button buttonOk = new Button(compositeOkCancel, SWT.NONE);
		buttonOk.setText(Messages.ManageDevicesDialog_OK);
		shell.setDefaultButton(buttonOk);
		buttonOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resultDevicesList = new DevicesList(devices, selectedDeviceIndex);
				shell.close();
			}
		});
		
		Button buttonCancel = new Button(compositeOkCancel, SWT.NONE);
		buttonCancel.setText(Messages.ManageDevicesDialog_CANCEL);
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resultDevicesList = null;
				shell.close();
			}
		});
		

		updateDevices();
	}
	
	public void updateDevices() {
		table.removeAll();
		for (Device device : devices) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] {
					device.getName(), 
					device.getWidth() == Device.DEFAULT_SIZE ? Messages.ManageDevicesDialog_DEFAULT : String.valueOf(device.getWidth()),
					device.getHeight() == Device.DEFAULT_SIZE ? Messages.ManageDevicesDialog_DEFAULT : String.valueOf(device.getHeight()),
					device.getUserAgent() == null ? Messages.ManageDevicesDialog_DEFAULT : device.getUserAgent()
			});
		}
		table.setSelection(selectedDeviceIndex);
	}
}
