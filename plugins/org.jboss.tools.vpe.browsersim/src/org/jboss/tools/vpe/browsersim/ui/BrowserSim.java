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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.DeviceOrientation;
import org.jboss.tools.vpe.browsersim.model.DevicesList;
import org.jboss.tools.vpe.browsersim.model.DevicesListHolder;
import org.jboss.tools.vpe.browsersim.model.DevicesListStorage;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.NativeSkin;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSim implements Runnable {
	private static final String DEFAULT_URL = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
	private BrowserSimBrowser browser;
	private Display display;
	private Shell shell;
	private String initialUrl;
	private Menu devicesMenu;
	private DevicesListHolder devicesListHolder;
	private DeviceOrientation deviceOrientation;
	private BrowserSimSkin skin;

	public static void main(String[] args) {
		String initialUrl;
		if (args.length > 0) {
			String lastArg = args[args.length - 1];
			try {
				new URI(lastArg); // validate URL
				initialUrl = lastArg;
			} catch (URISyntaxException e) {
				initialUrl = DEFAULT_URL;
			}
		} else {
			initialUrl = DEFAULT_URL;
		}

		new BrowserSim(new Display(), initialUrl).run();
	}

	public BrowserSim(Display display, String initialUrl) {
		this.display = display;
		this.initialUrl = initialUrl;
	}
	
	@Override
	public void run() {
		skin = new NativeSkin();//new AppleIPhone3Skin();
		skin.setBrowserFactory(new WebKitBrowserFactory());

		try {
			skin.createControls(display);
		} catch (SWTError e) {
			System.out.println(Messages.BrowserSim_COULD_NOT_INSTANTIATE_WEBKIT_BROWSER + e.getMessage());
			
			MessageBox messageBox = new MessageBox(new Shell(display), SWT.OK | SWT.ICON_ERROR);
			messageBox.setText("Error");
			messageBox.setMessage(Messages.BrowserSim_COULD_NOT_INSTANTIATE_WEBKIT_BROWSER + e.getMessage());
			messageBox.open();
			
			display.dispose();
			return;
		}
		
		shell = skin.getShell();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (devicesListHolder != null) {
					DevicesListStorage.saveUserDefinedDevicesList(devicesListHolder.getDevicesList());
				}
			}
		});
		shell.setText(Messages.BrowserSim_BROWSER_SIM);
		
		browser = skin.getBrowser();
		
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				if (event.total == 0) return;                            
				int ratio = event.current * 100 / event.total;
				skin.progressChanged(ratio);
			}
			public void completed(ProgressEvent event) {
				skin.progressChanged(-1);
			}
		});
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				skin.statusTextChanged(event.text);
			}
		});
		browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				if (event.top) {
					skin.locationChanged(event.location);
				}
			}
			public void changing(LocationEvent event) {
			}
		});

		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		browser.setLayoutData(data);

		browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				initOrientation(deviceOrientation.getOrientationAngle());
			}
			public void changing(LocationEvent event) {
			}
		});
		
		skin.setControlHandler(new ControlHandlerImpl(browser));
		
		devicesListHolder = new DevicesListHolder();
		Menu appMenuBar = skin.getMenuBar();
		if (appMenuBar != null) {
			fillMenuBar(appMenuBar);
		}
		
		DevicesList devicesList = DevicesListStorage.loadUserDefinedDevicesList();
		if (devicesList == null) {
			devicesList = DevicesListStorage.loadDefaultDevicesList();
		}
		
		devicesListHolder.setDevicesList(devicesList);
		devicesListHolder.notifyObservers();

		if (devicesList != null && devicesList.getDevices() != null 
				&& devicesList.getSelectedDeviceIndex() < devicesList.getDevices().size()) {
			setDevice(devicesList.getDevices().get(devicesList.getSelectedDeviceIndex()));
		}

		browser.setUrl(initialUrl);
		
		while (!this.shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public void fillMenuBar(Menu appMenuBar) {
		Menu file = createDropDownMenu(appMenuBar, Messages.BrowserSim_FILE);
		MenuItem exit = new MenuItem(file, SWT.PUSH);
		exit.setText(Messages.BrowserSim_EXIT);
		exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				display.dispose();
			};
		});
		
		devicesMenu = createDropDownMenu(appMenuBar, Messages.BrowserSim_DEVICES);
		devicesListHolder.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				DevicesListHolder devicesManager = (DevicesListHolder) o;
				DevicesList devicesList = devicesManager.getDevicesList();
				setDevicesListForMenu(devicesList);
				if (devicesList.getSelectedDeviceIndex() < devicesList.getDevices().size()) {
					setDevice(devicesList.getDevices().get(devicesList.getSelectedDeviceIndex()));
				}
				devicesList.addObserver(new Observer() {
					public void update(Observable o, Object arg) {
						setDevice(((DevicesList)o).getDevices().get(((DevicesList)o).getSelectedDeviceIndex()));
					}
				});
			}
		});

		new MenuItem(devicesMenu, SWT.BAR);
		MenuItem manageDevicesMenuItem = new MenuItem(devicesMenu, SWT.PUSH);
		manageDevicesMenuItem.setText(Messages.BrowserSim_MORE);
		manageDevicesMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DevicesList newDevicesList = new ManageDevicesDialog(shell, SWT.APPLICATION_MODAL | SWT.SHELL_TRIM,
						devicesListHolder.getDevicesList()).open();
				if (newDevicesList != null) {
					devicesListHolder.setDevicesList(newDevicesList);
					devicesListHolder.notifyObservers();
				}
			}
		});
	}

	private void setDevicesListForMenu(DevicesList devicesList) {
		for (MenuItem item : devicesMenu.getItems()) {
			if (item.getData() instanceof Device) {
				item.dispose();
			}
		}
		
		int currentIndex = 0;
		for (Device device : devicesList.getDevices()) {
			MenuItem deviceMenuItem = new MenuItem(devicesMenu, SWT.RADIO, currentIndex);
			deviceMenuItem.setText(device.getName());
			deviceMenuItem.setData(device);
			deviceMenuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					MenuItem menuItem = (MenuItem)e.widget;
					if (menuItem.getSelection()) {
						DevicesList devicesList = devicesListHolder.getDevicesList();
						int selectedDeviceIndex = devicesList.getDevices().indexOf(menuItem.getData());
						if (selectedDeviceIndex < 0) {
							selectedDeviceIndex = 0;
						}
						devicesList.setSelectedDeviceIndex(selectedDeviceIndex);
						devicesList.notifyObservers();
					}
				};
			});
			
			currentIndex++;
		}
	}

	private Menu createDropDownMenu(Menu menuBar, String name) {
		MenuItem manuItem = new MenuItem(menuBar, SWT.CASCADE);
		manuItem.setText(name);
		Menu dropdown = new Menu(menuBar);
		manuItem.setMenu(dropdown);
		return dropdown;
	}
	
	public void setDevice(final Device device) {
		if (devicesMenu != null) {
			for (MenuItem menuItem : devicesMenu.getItems()) {
				if (menuItem.getData() instanceof Device) {
					menuItem.setSelection(menuItem.getData() == device);				
				}
			}
		}
		
		skin.setBrowserSize(device.getWidth(), device.getHeight());
		deviceOrientation = new DeviceOrientation(device.getWidth() < device.getHeight() 
					? DeviceOrientation.PORTRAIT
					: DeviceOrientation.LANDSCAPE);
		deviceOrientation.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				int orientationAngle = ((DeviceOrientation) o).getOrientationAngle();
				
				int minSize = Math.min(device.getWidth(), device.getHeight());
				int maxSize = Math.max(device.getWidth(), device.getHeight());
				
				if (orientationAngle == DeviceOrientation.LANDSCAPE
						|| orientationAngle == DeviceOrientation.LANDSCAPE_INVERTED) {
					skin.setBrowserSize(maxSize, minSize);					
				} else {
					skin.setBrowserSize(minSize, maxSize);										
				}
				
				fireOrientationChangeEvent(orientationAngle);
			}
		});

		browser.setDefaultUserAgent(device.getUserAgent());
		browser.refresh();
	}
	
	private void initOrientation(int orientation) {
		browser.execute("window.onorientationchange = null;"
				+ "window.orientation = " + orientation + ";");		
	}
	
	private void fireOrientationChangeEvent(int orientation) {
		browser.execute("window.orientation = " + orientation + ";"
				+ "(function(){"
				+ 		"var event = document.createEvent('Event');"
				+ 		"event.initEvent('orientationchange', false, false);" // http://jsbin.com/azefow/6   https://developer.mozilla.org/en/DOM/document.createEvent
				+ 		"window.dispatchEvent(event);"
				+ 		"if (typeof window.onorientationchange === 'function') {"
				+			"window.onorientationchange(event);"
				+ 		"}"
				+	"})();"
		);
	}
	
	protected void rotateDevice(boolean counterclockwise) {
		deviceOrientation.turnDevice(counterclockwise);
		deviceOrientation.notifyObservers();
	}
	
	class ControlHandlerImpl implements ControlHandler {
		private Browser browser;
		
		public ControlHandlerImpl(Browser browser) {
			this.browser = browser;
		}

		@Override
		public void goBack() {
			browser.back();
		}

		@Override
		public void goForward() {
			browser.forward();
		}

		@Override
		public void goHome() {
			browser.setUrl("about:blank");//XXX
		}

		@Override
		public void goToAddress(String address) {
			browser.setUrl(address);
		}

		@Override
		public void showContextMenu() {
			// TODO Auto-generated method stub//XXX
		}

		@Override
		public void rotate(boolean counterclockwise) {
			BrowserSim.this.rotateDevice(counterclockwise);
		}

		@Override
		public void stop() {
			browser.stop();
		}

		@Override
		public void refresh() {
			browser.refresh();
		}
	}
}
