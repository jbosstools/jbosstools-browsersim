/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.browsersim.browser.PlatformUtil;
import org.jboss.tools.browsersim.ui.launch.BrowserSimArgs;
import org.jboss.tools.browsersim.ui.model.Device;
import org.jboss.tools.browsersim.ui.model.TruncateWindow;
import org.jboss.tools.browsersim.ui.model.preferences.BrowserSimSpecificPreferences;
import org.jboss.tools.browsersim.ui.model.preferences.BrowserSimSpecificPreferencesStorage;
import org.jboss.tools.browsersim.ui.model.preferences.CommonPreferences;
import org.jboss.tools.browsersim.ui.model.preferences.CommonPreferencesStorage;
import org.jboss.tools.browsersim.ui.model.preferences.SpecificPreferences;
import org.jboss.tools.browsersim.ui.model.preferences.SpecificPreferencesStorage;
import org.jboss.tools.browsersim.ui.util.BrowserSimUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ManageDevicesDialog extends Dialog {
	/** @see org.jboss.tools.browsersim.eclipse.callbacks.RestartCallback */
	private static final String BROWSERSIM_RESTART_COMMAND = "org.jboss.tools.browsersim.command.restart:"; //$NON-NLS-1$
	
	private String currentUrl;
	protected CommonPreferences oldCommonPreferences;
	protected SpecificPreferences oldSpecificPreferences;
	protected Map<String, Device> devices;
	protected String checkedDeviceId;
	protected String selectedDeviceId;
	protected Shell shell;
	protected Table table;
	protected CommonPreferences newCommonPreferences;
	protected SpecificPreferences newSpecificPreferences;
	protected boolean useSkins;
	protected boolean enableLiveReload;
	protected int liveReloadPort;
	protected boolean enableTouchEvents;
	protected boolean isJavaFx;
	protected TruncateWindow truncateWindow;
	protected Button askBeforeTruncateRadio;
	protected Button alwaysTruncateRadio;
	protected Button neverTruncateRadio;
	protected Button javaFXBrowserRadio;
	protected Button swtBrowserRadio;
	protected Button useSkinsCheckbox;
	protected Button liveReloadCheckBox;
	protected Button touchEventsCheckBox;
	protected Label liveReloadPortLabel;
	protected Text liveReloadPortText;
	protected Text screenshotsPath;
	protected Text weinreScriptUrlText;
	protected Text weinreClientUrlText;	

	protected Button buttonEdit;
	protected Button buttonRemove;
	
	protected Composite settingsComposite;
	
	private Label livereloadErrorLabel;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @param oldDevicesList 
	 */
	public ManageDevicesDialog(Shell parent, int style, CommonPreferences oldCommonPreferences, SpecificPreferences oldSpecificPreferences, String currentUrl) {
		super(parent, style);
		setText(Messages.ManageDevicesDialog_PREFERENCES);
		this.oldCommonPreferences = oldCommonPreferences;
		this.oldSpecificPreferences = oldSpecificPreferences;
		
		this.devices = new LinkedHashMap<String, Device>(oldCommonPreferences.getDevices());
		this.checkedDeviceId = oldSpecificPreferences.getSelectedDeviceId();
		this.selectedDeviceId = oldSpecificPreferences.getSelectedDeviceId();
		this.useSkins = oldSpecificPreferences.getUseSkins();
		this.enableLiveReload = oldSpecificPreferences.isEnableLiveReload();
		this.liveReloadPort = oldSpecificPreferences.getLiveReloadPort();
		this.enableTouchEvents = oldSpecificPreferences.isEnableTouchEvents();
		this.truncateWindow = oldCommonPreferences.getTruncateWindow();
		this.isJavaFx = oldSpecificPreferences.isJavaFx();
		this.currentUrl = currentUrl;
	} 
	
	/**
	 * Open the dialog.
	 * @return the newDevicesList
	 */
	public PreferencesWrapper open() {
		createContents();
		updateDevices();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (newCommonPreferences == null || newSpecificPreferences == null) {
			return null;
		} else {
			return new PreferencesWrapper(newCommonPreferences, newSpecificPreferences);
		}
	}

	/**
	 * Create contents of the dialog.
	 */
	protected void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(800, 555);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		final TabFolder tabFolder = new TabFolder (shell, SWT.FILL);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Rectangle clientArea = shell.getClientArea ();
		tabFolder.setLocation (clientArea.x, clientArea.y);

		TabItem devicesTab = new TabItem(tabFolder, SWT.NONE);
		devicesTab.setText(Messages.ManageDevicesDialog_TABS_DEVICES);
		
		Composite devicesComposite = new Composite(tabFolder, SWT.NONE);
		devicesComposite.setLayout(new GridLayout());

		Group devicesGroup = new Group(devicesComposite, SWT.NONE);
		devicesGroup.setText(Messages.ManageDevicesDialog_DEVICES);
		devicesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		devicesGroup.setLayout(new GridLayout(2, false));

		table = new Table(devicesGroup, SWT.CHECK |SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (e.detail == SWT.CHECK) {
					checkedDeviceId = e.item.getData().toString();
					for (TableItem item : table.getItems()) {
						item.setChecked(false);
					}
					((TableItem)e.item).setChecked(true);
				}  else {
					selectedDeviceId = e.item.getData().toString();
				}
			}
		});

		TableColumn tableColumnName = new TableColumn(table, SWT.NONE);
		tableColumnName.setWidth(175);
		tableColumnName.setText(Messages.ManageDevicesDialog_NAME);

		TableColumn tableColumnWidth = new TableColumn(table, SWT.NONE);
		tableColumnWidth.setWidth(75);
		tableColumnWidth.setText(Messages.ManageDevicesDialog_WIDTH);

		TableColumn tableColumnHeight = new TableColumn(table, SWT.NONE);
		tableColumnHeight.setWidth(75);
		tableColumnHeight.setText(Messages.ManageDevicesDialog_HEIGHT);

		TableColumn tableColumnPixelRatio = new TableColumn(table, SWT.NONE);
		tableColumnPixelRatio.setWidth(75);
		tableColumnPixelRatio.setText(Messages.ManageDevicesDialog_PIXEL_RATIO);

		TableColumn tableColumnUseragent = new TableColumn(table, SWT.NONE);
		tableColumnUseragent.setWidth(150);
		tableColumnUseragent.setText(Messages.ManageDevicesDialog_USER_AGENT);

		TableColumn tableColumnSkin = new TableColumn(table, SWT.NONE);
		tableColumnSkin.setWidth(75);
		tableColumnSkin.setText(Messages.ManageDevicesDialog_SKIN);

		Composite compositeControls = new Composite(devicesGroup, SWT.NONE);
		compositeControls.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		compositeControls.setLayout(new FillLayout(SWT.VERTICAL));

		Button buttonAdd = new Button(compositeControls, SWT.NONE);
		buttonAdd.setSize(88, SWT.DEFAULT);
		buttonAdd.setText(Messages.ManageDevicesDialog_ADD);
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device newDevice = new AddDeviceDialog(shell, SWT.APPLICATION_MODAL | SWT.SHELL_TRIM, devices.get(selectedDeviceId)).open();
				if (newDevice != null) {
					String id = UUID.randomUUID().toString();
					newDevice.setId(id);
					devices.put(id, newDevice);
					table.setSelection(table.getItemCount());
					selectedDeviceId = id;
					updateDevices();
				}
			}
		});

		buttonEdit = new Button(compositeControls, SWT.NONE);
		buttonEdit.setText(Messages.ManageDevicesDialog_EDIT);
		buttonEdit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Device selected = devices.get(selectedDeviceId);
				Device newDevice = new EditDeviceDialog(shell, SWT.APPLICATION_MODAL | SWT.SHELL_TRIM, selected).open();
				if (newDevice != null) {
					newDevice.setId(selected.getId());
					devices.put(newDevice.getId(), newDevice);
					updateDevices();
				}
			}
		});

		buttonRemove = new Button(compositeControls, SWT.NONE);
		buttonRemove.setText(Messages.ManageDevicesDialog_REMOVE);
		buttonRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (devices.size() > 1) {
					boolean checkedRemoved = checkedDeviceId.equals(selectedDeviceId);
					if (checkedRemoved) {
						int nextSelection = table.getSelectionIndex() + 1;
						if (nextSelection == table.getItemCount()) {
							// last element selected, then element before last must
							// be selected after deletion
							nextSelection = table.getItemCount() - 2;
						}
						devices.remove(checkedDeviceId);
						checkedDeviceId = table.getItem(nextSelection).getData().toString();
					} else {
						devices.remove(table.getItem(table.getSelectionIndex()).getData());
					}
					updateDevices();
				}
			}
		});

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				buttonEdit.setEnabled(true);
				buttonRemove.setEnabled(true);
			}
		});
		
		Button buttonReset = new Button(compositeControls, SWT.NONE);
		buttonReset.setText(Messages.ManageDevicesDialog_REVERT_ALL);

		buttonReset.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				devices = new LinkedHashMap<String, Device>(oldCommonPreferences.getDevices());
				checkedDeviceId = oldSpecificPreferences.getSelectedDeviceId();
				updateDevices();
			}
		});

		Group useSkinsGroup = new Group(devicesComposite, SWT.NONE);
		useSkinsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		useSkinsGroup.setLayout(new RowLayout(SWT.VERTICAL));
		useSkinsGroup.setText(Messages.ManageDevicesDialog_SKINS_OPTIONS);
		useSkinsCheckbox = new Button(useSkinsGroup, SWT.CHECK);
		useSkinsCheckbox.setText(Messages.ManageDevicesDialog_USE_SKINS);
		useSkinsCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button checkbox = (Button) e.widget;
				useSkins = checkbox.getSelection();
			}
		});
		
		Group truncateWindowGroup = new Group(devicesComposite, SWT.NONE);
		truncateWindowGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		truncateWindowGroup.setText(Messages.ManageDevicesDialog_TRUNCATE_THE_DEVICE_WINDOW);
		truncateWindowGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		
		alwaysTruncateRadio = new Button(truncateWindowGroup, SWT.RADIO);
		alwaysTruncateRadio.setText(TruncateWindow.ALWAYS_TRUNCATE.getMessage());
		alwaysTruncateRadio.setData(TruncateWindow.ALWAYS_TRUNCATE);
		
		neverTruncateRadio = new Button(truncateWindowGroup, SWT.RADIO);
		neverTruncateRadio.setText(TruncateWindow.NEVER_TRUNCATE.getMessage());
		neverTruncateRadio.setData(TruncateWindow.NEVER_TRUNCATE);

		askBeforeTruncateRadio = new Button(truncateWindowGroup, SWT.RADIO);
		askBeforeTruncateRadio.setText(TruncateWindow.PROMPT.getMessage());
		askBeforeTruncateRadio.setData(TruncateWindow.PROMPT);
		
		SelectionListener truncateSelectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button radio = (Button) e.widget;
				if (radio.getSelection()) {
					truncateWindow = (TruncateWindow) radio.getData();
				}
			}
		};
		
		alwaysTruncateRadio.addSelectionListener(truncateSelectionListener);
		neverTruncateRadio.addSelectionListener(truncateSelectionListener);
		askBeforeTruncateRadio.addSelectionListener(truncateSelectionListener);
		
		devicesTab.setControl(devicesComposite);
		
		TabItem settingsTab = new TabItem(tabFolder, SWT.NONE);
		settingsTab.setText(Messages.ManageDevicesDialog_TABS_SETTINGS);
		
		settingsComposite = new Composite(tabFolder, SWT.NONE);
		settingsComposite.setLayout(new GridLayout());
		
		final Group liveReloadGroup = new Group(settingsComposite, SWT.NONE);
		liveReloadGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		liveReloadGroup.setLayout(new GridLayout(2, false));
		liveReloadGroup.setText(Messages.ManageDevicesDialog_LIVE_RELOAD_OPTIONS);
		liveReloadCheckBox = new Button(liveReloadGroup, SWT.CHECK);
		liveReloadCheckBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		liveReloadCheckBox.setText(Messages.ManageDevicesDialog_ENABLE_LIVE_RELOAD);
		
		final Composite liveReloadPortComposite = new Composite(liveReloadGroup, SWT.NONE);
		liveReloadPortComposite.setLayout(new GridLayout(2, false));
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
		data.horizontalIndent = 15;
		liveReloadPortComposite.setLayoutData(data);
		liveReloadPortLabel = new Label(liveReloadPortComposite, SWT.NONE);
		liveReloadPortLabel.setText(Messages.ManageDevicesDialog_LIVE_RELOAD_PORT);
		liveReloadPortText = new Text(liveReloadPortComposite, SWT.BORDER);
		liveReloadPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		liveReloadPortText.setText(Integer.toString(liveReloadPort));
		liveReloadPortText.setTextLimit(5);
		liveReloadPortText.addVerifyListener(new VerifyDigitsListener());
		liveReloadPortText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if ("".equals(liveReloadPortText.getText())) { //$NON-NLS-1$
					liveReloadPortText.setText(Integer.toString(SpecificPreferencesStorage.DEFAULT_LIVE_RELOAD_PORT));
				}
			}
		});
		
		liveReloadCheckBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button checkbox = (Button) e.widget;
				enableLiveReload = checkbox.getSelection();
				enableLiveReloadPort(enableLiveReload);
			}
		});
		
		livereloadErrorLabel = new Label(liveReloadGroup, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd.horizontalSpan = 2;
        livereloadErrorLabel.setLayoutData(gd);
        livereloadErrorLabel.setText(Messages.ManageDevicesDialog_LIVE_RELOAD_UNAVAILABLE);
        livereloadErrorLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		
		disableLivereloadForJavaFx7(liveReloadGroup);
		
		Group touchEventsGroup = new Group(settingsComposite, SWT.NONE);
		touchEventsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		touchEventsGroup.setText(Messages.ManageDevicesDialog_TOUCH_EVENTS_OPTIONS);
		touchEventsGroup.setLayout(new GridLayout(2, false));
		touchEventsCheckBox = new Button(touchEventsGroup, SWT.CHECK);
		touchEventsCheckBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		touchEventsCheckBox.setText(Messages.ManageDevicesDialog_SIMULATE_TOUCH_EVENTS);
		
		Group browserTypeGroup = new Group(settingsComposite, SWT.NONE);
		browserTypeGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		browserTypeGroup.setText(Messages.ManageDevicesDialog_BROWSER_ENGINE);
		browserTypeGroup.setLayout(new RowLayout(SWT.HORIZONTAL));

		javaFXBrowserRadio = new Button(browserTypeGroup, SWT.RADIO);
		javaFXBrowserRadio.setText(Messages.ManageDevicesDialog_BROWSER_TYPE_JAVAFX);

		swtBrowserRadio = new Button(browserTypeGroup, SWT.RADIO);
		swtBrowserRadio.setText(Messages.ManageDevicesDialog_BROWSER_TYPE_SWT);

		SelectionListener browserTypeSelectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isJavaFx = javaFXBrowserRadio.equals((Button) e.widget);
				disableLivereloadForJavaFx7(liveReloadGroup);
			}
		};

		javaFXBrowserRadio.addSelectionListener(browserTypeSelectionListener);
		swtBrowserRadio.addSelectionListener(browserTypeSelectionListener);

		disableWebEngineSwitcherIfJavaFxNotAvailable(javaFXBrowserRadio, browserTypeGroup);
		disableWebEngineSwitcherIfWebKitNotAvailable(swtBrowserRadio, browserTypeGroup);
		
		Group screnshotGroup = new Group(settingsComposite, SWT.NONE);
		screnshotGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		screnshotGroup.setText(Messages.ManageDevicesDialog_SCREENSHOTS);
		screnshotGroup.setLayout(new GridLayout(3, false));
		
		Label screenshotsLabel = new Label(screnshotGroup, SWT.NONE);
		screenshotsLabel.setText(Messages.ManageDevicesDialog_LOCATION);		
		screenshotsPath = new Text(screnshotGroup, SWT.BORDER);
		screenshotsPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		screenshotsPath.setText(oldCommonPreferences.getScreenshotsFolder());
		
		Button selectFolder = new Button(screnshotGroup, SWT.PUSH);
		selectFolder.setText(Messages.ManageDevicesDialog_BROWSE);
		selectFolder.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				DirectoryDialog dd = new DirectoryDialog(shell);
				dd.setText(Messages.ManageDevicesDialog_SELECT_FOLDER);
				dd.setFilterPath(screenshotsPath.getText());
				
				String selectedFolder = dd.open(); 
				if(selectedFolder != null) {
					screenshotsPath.setText(selectedFolder);
				}
			}
		});
		
		Group weinreGroup = new Group(settingsComposite, SWT.NONE);
		weinreGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		weinreGroup.setText(Messages.ManageDevicesDialog_WEINRE);
		weinreGroup.setLayout(new GridLayout(2, false));
		
		Label weinreScriptUrlLabel = new Label(weinreGroup, SWT.NONE);
		weinreScriptUrlLabel.setText(Messages.ManageDevicesDialog_WEINRE_SCRIPT_URL);
		weinreScriptUrlText = new Text(weinreGroup, SWT.BORDER);
		weinreScriptUrlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		weinreScriptUrlText.setText(oldCommonPreferences.getWeinreScriptUrl());
		
		Label weinreClientUrlLabel = new Label(weinreGroup, SWT.NONE);
		weinreClientUrlLabel.setText(Messages.ManageDevicesDialog_WEINRE_CLIENT_URL);
		weinreClientUrlText = new Text(weinreGroup, SWT.BORDER);
		weinreClientUrlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		weinreClientUrlText.setText(oldCommonPreferences.getWeinreClientUrl());
		
		settingsTab.setControl(settingsComposite);
		tabFolder.pack();
		
		Composite compositeOkCancel = new Composite(shell, SWT.NONE);
		compositeOkCancel.setLayout(new GridLayout(2, true));
		compositeOkCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		
		new Label(compositeOkCancel, SWT.NONE);
		
		Button buttonLoadDefaults = new Button(compositeOkCancel, SWT.NONE);
		buttonLoadDefaults.setText(Messages.ManageDevicesDialog_LOAD_DEFAULTS);
		buttonLoadDefaults.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				loadDefaultPreferences();
			}
		});
		
		
		Button buttonOk = new Button(compositeOkCancel, SWT.NONE);
		buttonOk.setText(Messages.ManageDevicesDialog_OK);
		buttonOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		shell.setDefaultButton(buttonOk);
		buttonOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				performOK();
			}
		});
		
		Button buttonCancel = new Button(compositeOkCancel, SWT.NONE);
		buttonCancel.setText(Messages.ManageDevicesDialog_CANCEL);
		buttonCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				performCancel();
			}
		});
	}
	
	private void restart() {
		newCommonPreferences = new CommonPreferences(devices, truncateWindow, screenshotsPath.getText(), weinreScriptUrlText.getText(), weinreClientUrlText.getText());
		newSpecificPreferences = create(selectedDeviceId, useSkins, enableLiveReload, getLiveReloadPort(), touchEventsCheckBox.getSelection(), isJavaFx);
		getSpecificPreferencesStorage().save(newSpecificPreferences);
		CommonPreferencesStorage.INSTANCE.save(newCommonPreferences);
		Display.getDefault().dispose();						
		sendRestartCommand();
	}
	
	private void disableWebEngineSwitcherIfJavaFxNotAvailable(Button javaFXBrowserRadio, Group browserTypeGroup) {
		String message = ""; //$NON-NLS-1$
		// For now JavaFx is compiled against gtk 2 only, no support for gtk 3 on Linux - https://bugs.eclipse.org/bugs/show_bug.cgi?id=420182
		if (PlatformUtil.OS_LINUX.equals(PlatformUtil.getOs()) && !BrowserSimUtil.isRunningAgainstGTK2()) {
			message = Messages.ManageDevicesDialog_BROWSER_ENGINE_WARNING_ON_LINUX;
		} else if (!BrowserSimUtil.isJavaFxAvailable()) {
			message = Messages.ManageDevicesDialog_BROWSER_ENGINE_WARNING;
	    }	
		
		if (!message.isEmpty()) {
			disableSwitcher(javaFXBrowserRadio, browserTypeGroup, message);
		}
	}
	
	private void disableWebEngineSwitcherIfWebKitNotAvailable(Button swtBrowserRadio, Group browserTypeGroup) {
		String message = ""; //$NON-NLS-1$
		// BrowserSim needs Safari installed on Windows
		if (!BrowserSimUtil.isWebkitAvailable()) {
			String os = PlatformUtil.getOs();
			if (PlatformUtil.OS_WIN32.equals(os)) {
				if (PlatformUtil.ARCH_X64.equals(PlatformUtil.getArch())) {
					message = Messages.ManageDevicesDialog_BROWSER_ENGINE_WEBKIT_WARNING_ON_WINDOWS64;
				} else {
					message = Messages.ManageDevicesDialog_BROWSER_ENGINE_WEBKIT_WARNING_ON_WINDOWS;
				}
			} else if (PlatformUtil.OS_LINUX.equals(os)) {
				message = Messages.ManageDevicesDialog_BROWSER_ENGINE_WEBKIT_WARNING_ON_LINUX;
			}
		}
		if (!message.isEmpty()) {
			disableSwitcher(swtBrowserRadio, browserTypeGroup, message);
		}
	}
	
	private void disableSwitcher(Button javaFXBrowserRadio, Group browserTypeGroup, String message) {
		javaFXBrowserRadio.setEnabled(false);
		Label label = new Label(browserTypeGroup, SWT.NONE);
		label.setText(message);
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
	}
	
	private void disableLivereloadForJavaFx7(Composite parent) {
		if (isJavaFx && BrowserSimUtil.isJavaFx7Available()) {
			liveReloadCheckBox.setSelection(false);
			liveReloadCheckBox.setEnabled(false);
			livereloadErrorLabel.setVisible(true);
		} else {
			liveReloadCheckBox.setEnabled(true);
			livereloadErrorLabel.setVisible(false);
		}
	}
	
	public void updateDevices() {
		table.removeAll();
		List<Device> values = new ArrayList<Device>(devices.values()); 
		for (int i = 0; i < values.size(); i++) {
			Device device = values.get(i);
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setData(device.getId());
			tableItem.setText(new String[] {
					device.getName(), 
					device.getWidth() == Device.DEFAULT_SIZE ? Messages.ManageDevicesDialog_DEFAULT : String.valueOf(device.getWidth()),
					device.getHeight() == Device.DEFAULT_SIZE ? Messages.ManageDevicesDialog_DEFAULT : String.valueOf(device.getHeight()),
					Device.PIXEL_RAIO_FORMAT.format(device.getPixelRatio()),
					device.getUserAgent() == null ? Messages.ManageDevicesDialog_DEFAULT : device.getUserAgent(),
					device.getSkinId() == null ?  Messages.ManageDevicesDialog_NONE : device.getSkinId()
			});
			if (device.getId().equals(checkedDeviceId)) {
				tableItem.setChecked(true);
			}
			if (device.getId().equals(selectedDeviceId)) {
				table.setSelection(tableItem);
			}
		}
		
		if (table.getSelectionIndex() < 0) {
			buttonEdit.setEnabled(false);
			buttonRemove.setEnabled(false);			
		}
		
		useSkinsCheckbox.setSelection(useSkins);
		liveReloadCheckBox.setSelection(enableLiveReload);
		enableLiveReloadPort(enableLiveReload);
		touchEventsCheckBox.setSelection(enableTouchEvents);

		askBeforeTruncateRadio.setSelection(TruncateWindow.PROMPT.equals(truncateWindow));
		alwaysTruncateRadio.setSelection(TruncateWindow.ALWAYS_TRUNCATE.equals(truncateWindow));
		neverTruncateRadio.setSelection(TruncateWindow.NEVER_TRUNCATE.equals(truncateWindow));
		
		javaFXBrowserRadio.setSelection(isJavaFx);
		swtBrowserRadio.setSelection(!isJavaFx);
	}
	
	private void performOK() {
		if (isJavaFx != oldSpecificPreferences.isJavaFx()) {
			int result = showEngineSwitchConfirmationDialog();
			if (result == SWT.OK) {
				restart();
			}
		} else {
			newCommonPreferences = new CommonPreferences(devices, truncateWindow, screenshotsPath.getText(), weinreScriptUrlText.getText(), weinreClientUrlText.getText());
			newSpecificPreferences = create(checkedDeviceId, useSkins, enableLiveReload, getLiveReloadPort(), touchEventsCheckBox.getSelection(), isJavaFx);
			shell.close();
		}
	}
	
	private void performCancel() {
		newCommonPreferences = null;
		newSpecificPreferences = null;
		shell.close();
	}
	
	protected void loadDefaultPreferences() {
		CommonPreferences cp = CommonPreferencesStorage.INSTANCE.loadDefault();
		SpecificPreferences sp = getSpecificPreferencesStorage().loadDefault();
		devices = cp.getDevices();
		checkedDeviceId = sp.getSelectedDeviceId();
		selectedDeviceId = sp.getSelectedDeviceId();
		useSkins = sp.getUseSkins();
		enableLiveReload = sp.isEnableLiveReload();
		liveReloadPortText.setText(Integer.toString(sp.getLiveReloadPort()));
		enableTouchEvents = sp.isEnableTouchEvents();
		truncateWindow = cp.getTruncateWindow();
		isJavaFx = sp.isJavaFx();
		screenshotsPath.setText(cp.getScreenshotsFolder());
		weinreScriptUrlText.setText(cp.getWeinreScriptUrl());
		weinreClientUrlText.setText(cp.getWeinreClientUrl());
		updateDevices();
	}
		
	private void enableLiveReloadPort(boolean enableLiveReload) {
		liveReloadPortLabel.setEnabled(enableLiveReload);
		liveReloadPortText.setEnabled(enableLiveReload);
	}
	
	private int getLiveReloadPort() {
		String port = liveReloadPortText.getText();
		return port.isEmpty() ? SpecificPreferencesStorage.DEFAULT_LIVE_RELOAD_PORT : Integer.parseInt(port);
	}
	
	private int showEngineSwitchConfirmationDialog() {
		MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION	| SWT.OK | SWT.CANCEL);
		dialog.setText("Change web engine"); //$NON-NLS-1$
		dialog.setMessage("Changing browser engine requires restart. Do you want to restart BrowserSim now?"); //$NON-NLS-1$
		return dialog.open();
	}
	
	/**
	 * {@link SpecificPreferencesStorage} factory method.
	 * 
	 * Override this method if you need a custom {@link SpecificPreferencesStorage}
	 */
	protected SpecificPreferencesStorage getSpecificPreferencesStorage() {
		return BrowserSimSpecificPreferencesStorage.INSTANCE;
	}
	
	protected SpecificPreferences create(String selectedDeviceId, boolean useSkins, boolean enableLiveReload, int liveReloadPort, boolean enableTouchEvents, boolean isJavaFx) {
		return new BrowserSimSpecificPreferences(selectedDeviceId, useSkins, enableLiveReload, liveReloadPort, enableTouchEvents,
				oldSpecificPreferences.getOrientationAngle(), getParent().getLocation(), isJavaFx);
	}

	/**
	 * Send restart command to Eclipse or send a message to user to do a restart manually
	 */
	protected void sendRestartCommand() {
		if (!BrowserSimArgs.standalone) {
			System.out.println(BROWSERSIM_RESTART_COMMAND + currentUrl);
		} else {
			System.out.println(Messages.ManageDevicesDialog_STANDALONE_RESTART);
		}
	}
}
