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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.DeviceOrientation;
import org.jboss.tools.vpe.browsersim.model.DevicesList;
import org.jboss.tools.vpe.browsersim.model.DevicesListHolder;
import org.jboss.tools.vpe.browsersim.model.DevicesListStorage;
import org.jboss.tools.vpe.browsersim.model.SkinMap;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSim {
	private static final String DEFAULT_URL = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
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
		browserSim.initSkin(getSkinClass(defaultDevice, devicesList.getUseSkins()));
		browserSim.initDevicesListHolder();
		browserSim.devicesListHolder.setDevicesList(devicesList);
		browserSim.devicesListHolder.notifyObservers();
		browserSim.controlHandler.goHome();
		
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
	
	public void initSkin(Class<? extends BrowserSimSkin> skinClass) {
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
			showErrorMessage(new Shell(display), Messages.BrowserSim_COULD_NOT_INSTANTIATE_WEBKIT_BROWSER + e.getMessage());
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
			//delete all existing items, if any (necessary for systems like MacOS
			// that have system (non-shell) menu bar), to do not create duplicate items while changing skins
			for (MenuItem item : appMenuBar.getItems()) {
				item.dispose();
			}
			
			createMenusForMenuBar(appMenuBar);
		}
		
		Menu contextMenu = new Menu(shell);
		skin.setContextMenu(contextMenu);
		createMenuItemsForContextMenu(contextMenu);
		
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				int ratio;
				if (event.current == event.total || event.total == 0) {
					ratio = -1;
				} else {
					ratio = event.current * 100 / event.total;
				}
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
					BrowserSimBrowser browser = (BrowserSimBrowser) event.widget;
					skin.locationChanged(event.location, browser.isBackEnabled(), browser.isForwardEnabled());
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
		
		browser.addLocationListener(new LocationListener() {
			private BrowserFunction scrollListener = null;
			public void changed(LocationEvent event) {
				if (scrollListener != null) {
					scrollListener.dispose();
				}
				scrollListener = new BrowserFunction(((Browser)event.widget), "_browserSim_scrollListener") {
					public Object function(Object[] arguments) {
						double pageYOffset = (Double) arguments[0];
						if (pageYOffset > 0.0) {
							skin.setAddressBarVisible(false);
						}
						return null;
					}
				};
				
				((Browser)event.widget).execute(
						"(function() {" +
							"var scrollListener = function(e){" +
								"window._browserSim_scrollListener(window.pageYOffset)" +
							"};" +
							"window.addEventListener('scroll', scrollListener);" +
							"window.addEventListener('beforeunload', function(e){" +
								"window.removeEventListener('scroll', scrollListener);" +
								"delete window._browserSim_scrollListener;" +
							"})" +
						"})();");
			}
			public void changing(LocationEvent event) {
				skin.setAddressBarVisible(true);
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
					setSelectedDevice(devicesList);
				}
				devicesList.addObserver(new Observer() {
					public void update(Observable o, Object arg) {
						setSelectedDevice((DevicesList)o);
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
		
		MenuItem useSkinsMenuItem = new MenuItem(devicesMenu, SWT.CHECK);
		useSkinsMenuItem.setText("Use Skins");
		useSkinsMenuItem.setSelection(devicesListHolder.getDevicesList().getUseSkins());
		useSkinsMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MenuItem menuItem = (MenuItem) e.widget; 
				DevicesList devicesList = devicesListHolder.getDevicesList();
				devicesList.setUseSkins(menuItem.getSelection());
				devicesList.notifyObservers();
			}
		});
	}

	private void addFileMenuItems(Menu menu) {
		addOpenInDefaultBrowserItem(menu);
		
		MenuItem exit = new MenuItem(menu, SWT.PUSH);
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

	public void addOpenInDefaultBrowserItem(Menu menu) {
		if (Desktop.isDesktopSupported()) {
			if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				MenuItem openInDefaultBrowser = new MenuItem(menu, SWT.PUSH);
				openInDefaultBrowser.setText(Messages.BrowserSim_OPEN_IN_DEFAULT_BROWSER);
				openInDefaultBrowser.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						try {
							URL url = new URL(skin.getBrowser().getUrl()); // validate URL (to do not open 'about:blank' and similar)
							Desktop.getDesktop().browse(url.toURI());
						} catch (IOException e1) {
							showErrorMessage(skin.getShell(), Messages.BrowserSim_COULD_NOT_OPEN_DEFAULT_BROWSER + e1.getMessage());
						} catch (URISyntaxException e2) {
							showErrorMessage(skin.getShell(), Messages.BrowserSim_COULD_NOT_OPEN_DEFAULT_BROWSER + e2.getMessage());
						} 
					}
				});
			}
		}
	}
	
	private void showErrorMessage(Shell shell, String message) {
		System.err.println(message);
		
		MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
		messageBox.setText(Messages.BrowserSim_ERROR);
		messageBox.setMessage(message);
		messageBox.open();
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
	
	public void setSelectedDevice(DevicesList devicesList) {
		final Device device = devicesList.getDevices().get(devicesList.getSelectedDeviceIndex());
		Class<? extends BrowserSimSkin> newSkinClass = getSkinClass(device, devicesList.getUseSkins());
		String oldSkinUrl = null;
		if (newSkinClass != skin.getClass()) {
			oldSkinUrl = skin.getBrowser().getUrl();
			skin.getBrowser().getShell().dispose();//XXX
			initSkin(newSkinClass);
		}
		
		deviceOrientation = new DeviceOrientation(device.getWidth() < device.getHeight()
					? DeviceOrientation.PORTRAIT
					: DeviceOrientation.LANDSCAPE);
		Rectangle clientArea = getMonitorClientArea();
		skin.setOrientationAndSize(new Point(clientArea.width, clientArea.height), 
				deviceOrientation.getOrientationAngle(),
				new Point(device.getWidth(), device.getHeight()));
		fixShellLocation(clientArea);
		deviceOrientation.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				int orientationAngle = ((DeviceOrientation) o).getOrientationAngle();
				
				int minSize = Math.min(device.getWidth(), device.getHeight());
				int maxSize = Math.max(device.getWidth(), device.getHeight());
				Point browserSize;
				if (orientationAngle == DeviceOrientation.LANDSCAPE
						|| orientationAngle == DeviceOrientation.LANDSCAPE_INVERTED) {
					browserSize = new Point(maxSize, minSize);
				} else {
					browserSize = new Point(minSize, maxSize);
				}
				
				fireOrientationChangeEvent(orientationAngle, browserSize);
			}
		});

		skin.getBrowser().setDefaultUserAgent(device.getUserAgent());
		
		if (oldSkinUrl != null) {
			skin.getBrowser().setUrl(oldSkinUrl); // skin (and browser instance) is changed
		} else {
			skin.getBrowser().refresh(); // only user agent and size of the browser is changed
		}
	}
	
	private static Class<? extends BrowserSimSkin> getSkinClass(Device device, boolean useSkins) {
		return SkinMap.getInstance().getSkinClass(useSkins ? device.getSkinId() : null);
	}

	private void initOrientation(int orientation) {
		skin.getBrowser().execute("window.onorientationchange = null;"
				+ "window.orientation = " + orientation + ";");
	}
	
	private void fireOrientationChangeEvent(int orientation, Point browserSize) {
		Rectangle clientArea = getMonitorClientArea();
		skin.setOrientationAndSize(new Point(clientArea.width, clientArea.height), orientation, browserSize);
		fixShellLocation(clientArea);
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
	
	private void fixShellLocation(Rectangle clientArea) {
		Shell shell = skin.getShell();
		Point shellLocation = shell.getLocation();
		Point shellSize = shell.getSize();
		int bottomOverlap = shellLocation.y + shellSize.y - (clientArea.y + clientArea.height);
		if (bottomOverlap > 0) {
			if (shellLocation.y > bottomOverlap) {
				shellLocation.y -= bottomOverlap;
			} else {
				shellLocation.y = 0;
			}
		}
		
		int rightOverlap = shellLocation.x + shellSize.x - (clientArea.x + clientArea.width);
		if (rightOverlap > 0) {
			if (shellLocation.x > rightOverlap) {
				shellLocation.x -= rightOverlap;
			} else {
				shellLocation.x = 0;
			}
		}
		
		shell.setLocation(shellLocation);
	}
	
	private Rectangle getMonitorClientArea() {
		Monitor monitor = skin.getShell().getMonitor();
		Rectangle clientArea = monitor.getClientArea();

		/* on Linux returned monitor client area may be bigger
		 * than the monitor bounds when multiple monitors are used.
		 * The following code fixes this */
		Rectangle bounds = monitor.getBounds();
		clientArea.width = Math.min(clientArea.width, bounds.width);
		clientArea.height = Math.min(clientArea.height, bounds.height);
		
		return clientArea;
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
