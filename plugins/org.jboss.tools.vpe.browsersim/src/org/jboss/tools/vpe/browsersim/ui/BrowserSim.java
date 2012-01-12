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
import java.util.List;
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
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
public class BrowserSim {
	private static final String DEFAULT_URL = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
	private static final Class<? extends BrowserSimSkin> DEFAULT_SKIN_CLASS = NativeSkin.class;
	private Display display;
	private String homeUrl;
	private DevicesListHolder devicesListHolder;
	private DeviceOrientation deviceOrientation;
	private BrowserSimSkin skin;
//	private BrowserSimBrowser browser;
	private ControlHandler controlHandler;

	public static void main(String[] args) {
		String homeUrl;
		if (args.length > 0) {
			String lastArg = args[args.length - 1];
			try {
				new URI(lastArg); // validate URL
				homeUrl = lastArg;
			} catch (URISyntaxException e) {
				homeUrl = DEFAULT_URL;
			}
		} else {
			homeUrl = DEFAULT_URL;
		}

		
		DevicesList devicesList = DevicesListStorage.loadUserDefinedDevicesList();
		if (devicesList == null) {
			devicesList = DevicesListStorage.loadDefaultDevicesList();
		}
		Device defaultDevice = devicesList.getDevices().get(devicesList.getSelectedDeviceIndex());
		Display display = new Display();
		BrowserSim browserSim = new BrowserSim(display, homeUrl);
		browserSim.initSkin(defaultDevice);
		browserSim.initDevicesListHolder();
		browserSim.devicesListHolder.setDevicesList(devicesList);
		browserSim.devicesListHolder.notifyObservers();
		
		while (browserSim.skin!= null && browserSim.skin.getShell() != null && !browserSim.skin.getShell().isDisposed()) {//XXX
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public BrowserSim(Display display, String homeUrl) {
		this.display = display;
		this.homeUrl = homeUrl;
	}
	
	public void initSkin(Device device) {
		Class<? extends BrowserSimSkin> skinClass = device.getSkinClass();
		if (skinClass == null) {
			skinClass = DEFAULT_SKIN_CLASS;
		}
		
		try {
			skin = skinClass.newInstance();//new AppleIPhone3Skin();//new NativeSkin();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		skin.setBrowserFactory(new WebKitBrowserFactory());
		try {
			skin.createControls(display);
		} catch (SWTError e) {
			System.err.println(Messages.BrowserSim_COULD_NOT_INSTANTIATE_WEBKIT_BROWSER + e.getMessage());
			
			MessageBox messageBox = new MessageBox(new Shell(display), SWT.OK | SWT.ICON_ERROR);
			messageBox.setText("Error");
			messageBox.setMessage(Messages.BrowserSim_COULD_NOT_INSTANTIATE_WEBKIT_BROWSER + e.getMessage());
			messageBox.open();
			
			display.dispose();
			return;
		}
		
		Shell shell = skin.getShell();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (devicesListHolder != null) {
					DevicesListStorage.saveUserDefinedDevicesList(devicesListHolder.getDevicesList());
				}
			}
		});
		shell.setText(Messages.BrowserSim_BROWSER_SIM);
		
		BrowserSimBrowser browser = skin.getBrowser();
		controlHandler = new ControlHandlerImpl(browser);
		skin.setControlHandler(controlHandler);
		
		Menu appMenuBar = skin.getMenuBar();
		if (appMenuBar != null) {
			createMenusForMenuBar(appMenuBar);
		}
		
		Menu contextMenu = new Menu(shell);
		shell.setMenu(contextMenu);
		createMenuItemsForContextMenu(contextMenu);
		
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

		browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				initOrientation(deviceOrientation.getOrientationAngle());
			}
			public void changing(LocationEvent event) {
			}
		});
	}

	private void initDevicesListHolder() {
		devicesListHolder = new DevicesListHolder();
		devicesListHolder.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				DevicesListHolder devicesManager = (DevicesListHolder) o;
				DevicesList devicesList = devicesManager.getDevicesList();
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
	}

	private void createMenuItemsForContextMenu(Menu contextMenu) {
		contextMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu contextMenu = (Menu) e.widget;
				for (MenuItem item : contextMenu.getItems()) {
					item.dispose();
				}
				
				addDevicesMenuItems(contextMenu);
				new MenuItem(contextMenu, SWT.BAR);
				addTurnMenuItems(contextMenu);
				new MenuItem(contextMenu, SWT.BAR);
				addFileMenuItems(contextMenu);
			}
		});
	}

	private void createMenusForMenuBar(Menu appMenuBar) {
		Menu file = createDropDownMenu(appMenuBar, Messages.BrowserSim_FILE);
		addFileMenuItems(file);
		
		Menu devicesMenu = createDropDownMenu(appMenuBar, Messages.BrowserSim_DEVICES);
		devicesMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu devicesMenu = (Menu)e.widget;
				for (MenuItem item : devicesMenu.getItems()) {
					item.dispose();
				}
				addDevicesMenuItems(devicesMenu);
				new MenuItem(devicesMenu, SWT.BAR);
				addTurnMenuItems(devicesMenu);
			}
		});
	}

	private void addDevicesMenuItems(final Menu devicesMenu) {
		addDevicesListForMenu(devicesListHolder.getDevicesList(), devicesMenu);
		
		MenuItem manageDevicesMenuItem = new MenuItem(devicesMenu, SWT.PUSH);
		manageDevicesMenuItem.setText(Messages.BrowserSim_MORE);
		manageDevicesMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DevicesList newDevicesList = new ManageDevicesDialog(e.display.getActiveShell(), SWT.APPLICATION_MODAL | SWT.SHELL_TRIM,
						devicesListHolder.getDevicesList()).open();
				if (newDevicesList != null) {
					devicesListHolder.setDevicesList(newDevicesList);
					devicesListHolder.notifyObservers();
				}
			}
		});
	}

	private void addFileMenuItems(Menu file) {
		MenuItem exit = new MenuItem(file, SWT.PUSH);
		exit.setText(Messages.BrowserSim_EXIT);
		exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				display.dispose();
			};
		});
	}
	
	private void addTurnMenuItems(Menu menu) {
		MenuItem turnLeft = new MenuItem(menu, SWT.PUSH);
		turnLeft.setText(Messages.BrowserSim_TURN_LEFT);
		turnLeft.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controlHandler.rotate(true);
			}
		});
		
		MenuItem turnRight = new MenuItem(menu, SWT.PUSH);
		turnRight.setText(Messages.BrowserSim_TURN_RIGHT);
		turnRight.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controlHandler.rotate(false);
			}
		});
	}

	private void addDevicesListForMenu(final DevicesList devicesList, Menu devicesMenu) {
		List<Device> devices = devicesList.getDevices();
		for (int i = 0; i < devices.size(); i++) {
			Device device = devices.get(i);
			MenuItem deviceMenuItem = new MenuItem(devicesMenu, SWT.RADIO);
			deviceMenuItem.setText(device.getName());
			deviceMenuItem.setData(device);
			if (i == devicesList.getSelectedDeviceIndex()) {
				deviceMenuItem.setSelection(true);
			}

			deviceMenuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					MenuItem menuItem = (MenuItem)e.widget;
					if (menuItem.getSelection()) {
						int selectedDeviceIndex = devicesList.getDevices().indexOf(menuItem.getData());
						if (selectedDeviceIndex < 0) {
							selectedDeviceIndex = 0;
						}
						devicesList.setSelectedDeviceIndex(selectedDeviceIndex);
						devicesList.notifyObservers();
					}
				};
			});
		}
	}

	private Menu createDropDownMenu(Menu menuBar, String name) {
		MenuItem menuItem = new MenuItem(menuBar, SWT.CASCADE);
		menuItem.setText(name);
		Menu dropdown = new Menu(menuBar);
		menuItem.setMenu(dropdown);
		return dropdown;
	}
	
	public void setDevice(final Device device) {
		String oldSkinUrl = null;
		if (device.getSkinClass() != skin.getClass() 
				|| (device.getSkinClass() == null && skin.getClass() != DEFAULT_SKIN_CLASS)) {
			oldSkinUrl = skin.getBrowser().getUrl();
			skin.getBrowser().getShell().dispose();//XXX
			initSkin(device);
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

		skin.getBrowser().setDefaultUserAgent(device.getUserAgent());
		
		if (oldSkinUrl != null) {
			skin.getBrowser().setUrl(oldSkinUrl);
		} else {
			skin.getBrowser().setUrl(homeUrl);
		}
	}

	private void initOrientation(int orientation) {
		skin.getBrowser().execute("window.onorientationchange = null;"
				+ "window.orientation = " + orientation + ";");
	}
	
	private void fireOrientationChangeEvent(int orientation) {
		skin.setOrientation(orientation);
		skin.getBrowser().execute("window.orientation = " + orientation + ";"
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
			browser.setUrl(homeUrl);
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
