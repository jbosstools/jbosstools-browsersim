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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.DeviceOrientation;
import org.jboss.tools.vpe.browsersim.model.DevicesList;
import org.jboss.tools.vpe.browsersim.model.DevicesListHolder;
import org.jboss.tools.vpe.browsersim.model.DevicesListStorage;
import org.jboss.tools.vpe.browsersim.model.SkinMap;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ResizableSkinSizeAdvisor;
import org.jboss.tools.vpe.browsersim.util.ManifestUtil;
import org.jboss.tools.vpe.browsersim.util.ResourcesUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSim {
	public static final String BROWSERSIM_PLUGIN_ID = "org.jboss.tools.vpe.browsersim"; //$NON-NLS-1$
	private static final String DEFAULT_URL = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
	private static final String[] BROWSERSIM_ICONS = {"icons/browsersim_16px.png", "icons/browsersim_32px.png", "icons/browsersim_64px.png", "icons/browsersim_128px.png", "icons/browsersim_256px.png", }; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
	private static int BROWSERSIM_ICON_32PX_INDEX = 2;
	private static final String BROWSERSIM_CLASS_NAME = "org.jboss.tools.vpe.browsersim.ui.BrowserSim"; //$NON-NLS-1$
	/** @see org.jboss.tools.vpe.browsersim.eclipse.callbacks.OpenFileCallback */
	private static final String OPEN_FILE_COMMAND = BROWSERSIM_CLASS_NAME + ".command.openFile:"; //$NON-NLS-1$
	/** @see org.jboss.tools.vpe.browsersim.eclipse.callbacks.ViewSourceCallback */
	private static final String VIEW_SOURCE_COMMAND = BROWSERSIM_CLASS_NAME + ".command.viewSource:"; //$NON-NLS-1$
	
	private Display display;
	private String homeUrl;
	private DevicesListHolder devicesListHolder;
	private DeviceOrientation deviceOrientation;
	private BrowserSimSkin skin;
	private ControlHandler controlHandler;
	private Image[] icons;
	private ResizableSkinSizeAdvisor sizeAdvisor;

	public static void main(String[] args) {
		//CocoaUIEnhancer handles connection between the About, Preferences and Quit menus in MAC OS X
		CocoaUIEnhancer cocoaUIEnhancer = null;
		if (PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			cocoaUIEnhancer = new CocoaUIEnhancer(Messages.BrowserSim_BROWSER_SIM);
			cocoaUIEnhancer.initializeMacOSMenuBar();
		}
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
		

		// set event handlers for Mac OS X Menu-bar
		if (cocoaUIEnhancer != null) {
			browserSim.addMacOsMenuApplicationHandler(cocoaUIEnhancer);
		}


		while (browserSim.skin!= null && browserSim.skin.getShell() != null && !browserSim.skin.getShell().isDisposed()) {//XXX
			if (!display.readAndDispatch())
				display.sleep();
		}
		browserSim.dispose();
		display.dispose();
	}

	public BrowserSim(Display display, String homeUrl) {
		this.display = display;
		this.homeUrl = homeUrl;
		
		this.icons = new Image[BROWSERSIM_ICONS.length];
		for (int i = 0; i < BROWSERSIM_ICONS.length; i++) {
			String iconLocation = BROWSERSIM_ICONS[i];
			icons[i] = new Image(display, ResourcesUtil.getResourceAsStream(iconLocation));
		}
		
	}
	
	private void dispose() {
		for (Image icon : icons) {
			icon.dispose();
		}
		icons = null;
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
			e.printStackTrace();
			ExceptionNotifier.showWebKitLoadError(new Shell(display), e);
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
		setShellAttibutes();
		
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

		browser.addLocationListener(new LocationAdapter() {
			public void changed(LocationEvent event) {
				initOrientation(deviceOrientation.getOrientationAngle());
			}
		});

		//JBIDE-12191 - custom scrollbars work satisfactorily on windows only
		if (PlatformUtil.OS_WIN32.equals(PlatformUtil.getOs())) {
			browser.addLocationListener(new LocationAdapter() {
				@Override
				public void changed(LocationEvent event) {
					Browser browser = (Browser) event.widget;
					setCustomScrollbarStyles(browser);
				}
				
				@SuppressWarnings("nls")
				private void setCustomScrollbarStyles(Browser browser) {
					browser.execute(
						"if (window._browserSim_customScrollBarStylesSetter === undefined) {"
							+"window._browserSim_customScrollBarStylesSetter = function () {"
							+	"document.removeEventListener('DOMSubtreeModified', window._browserSim_customScrollBarStylesSetter, false);"
							+	"var head = document.head;"
							+	"var style = document.createElement('style');"
							+	"style.type = 'text/css';"
							+	"style.id='browserSimStyles';"
							+	"head.appendChild(style);"
							+	"style.innerText='"
							// The following two rules fix a problem with showing scrollbars in Google Mail and similar,
							// but autohiding of navigation bar stops to work with it. That is why they are commented.
							//+	"html {"
							//+		"overflow: hidden;"
							//+	"}"
							//+	"body {"
							//+		"position: absolute;"
							//+		"top: 0px;"
							//+		"left: 0px;"
							//+		"bottom: 0px;"
							//+		"right: 0px;"
							//+		"margin: 0px;"
							//+		"overflow-y: auto;"
							//+		"overflow-x: auto;"
							//+	"}"
							+		"::-webkit-scrollbar {"
							+			"width: 5px;"
							+			"height: 5px;"
							+		"}"
							+		"::-webkit-scrollbar-thumb {"
							+			"background: rgba(0,0,0,0.4); "
							+		"}"
							+		"::-webkit-scrollbar-corner, ::-webkit-scrollbar-thumb:window-inactive {"
							+			"background: rgba(0,0,0,0.0);"
							+		"};"
							+	"';"
							+"};"
							+ "document.addEventListener('DOMSubtreeModified', window._browserSim_customScrollBarStylesSetter, false);"
						+ "}"
					);
				}
			});
		};
		
		browser.addLocationListener(new LocationListener() {
			private BrowserFunction scrollListener = null;
			@SuppressWarnings("nls")
			public void changed(LocationEvent event) {
				if (scrollListener != null) {
					scrollListener.dispose();
				}
				scrollListener = new BrowserFunction(((Browser)event.widget), "_browserSim_scrollListener") {
					public Object function(Object[] arguments) {
						double pageYOffset = (Double) arguments[0];
						if (pageYOffset > 0.0) {
							display.asyncExec(new Runnable() {
								public void run() {
									if (skin != null && skin.getShell() != null && !skin.getShell().isDisposed()) {
										skin.setAddressBarVisible(false);
									}
								}
							});
						}
						return null;
					}
				};
				
				Browser browser = (Browser)event.widget;
				browser.execute(
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
		
		browser.addTitleListener(new TitleListener() {
			@Override
			public void changed(TitleEvent event) {
				skin.pageTitleChanged(event.title);
			}
		});
		shell.open();
	}
	
	private void setShellAttibutes() {
		Shell shell = skin.getShell();
		if (shell != null) {
			shell.setImages(icons);
			shell.setText(Messages.BrowserSim_BROWSER_SIM);
		}
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
				addUseSkinsItem(contextMenu);
				addPreferencesItem(contextMenu);
					
				new MenuItem(contextMenu, SWT.BAR);
				addTurnMenuItems(contextMenu);

				new MenuItem(contextMenu, SWT.BAR);
				addFileMenuItems(contextMenu);
				
				new MenuItem(contextMenu, SWT.BAR);
				addAboutItem(contextMenu);
				
				new MenuItem(contextMenu, SWT.BAR);
				addExitItem(contextMenu);
			}
		});
	}

	private void createMenusForMenuBar(Menu appMenuBar) {
		Menu file = createDropDownMenu(appMenuBar, Messages.BrowserSim_FILE);
		addFileMenuItems(file);
		
		// If Platform is Mac OS X, application will have no duplicated menu items (Exit/Quit BrowserSim)
		if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			addExitItem(file);
		}
		
		Menu devicesMenu = createDropDownMenu(appMenuBar, Messages.BrowserSim_DEVICES);
		devicesMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu devicesMenu = (Menu)e.widget;
				for (MenuItem item : devicesMenu.getItems()) {
					item.dispose();
				}
				
				addDevicesMenuItems(devicesMenu);
				addUseSkinsItem(devicesMenu);
				
				// If Platform is Mac OS X, application will have no duplicated menu items (Preferences)
				if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
					addPreferencesItem(devicesMenu);
				}

				new MenuItem(devicesMenu, SWT.BAR);
				addTurnMenuItems(devicesMenu);
			}
		});
		
		// If Platform is Mac OS X, application will have no duplicated menu items (About) 
		if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			Menu help = createDropDownMenu(appMenuBar, Messages.BrowserSim_HELP);
			addAboutItem(help);
		}
	}

	private void addDevicesMenuItems(final Menu devicesMenu) {
		addDevicesListForMenu(devicesListHolder.getDevicesList(), devicesMenu);
	}

	private void addFileMenuItems(Menu menu) {
		addOpenInDefaultBrowserItem(menu);
		addViewSourceItem(menu);
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
		MenuItem openInDefaultBrowser = new MenuItem(menu, SWT.PUSH);
		openInDefaultBrowser.setText(Messages.BrowserSim_OPEN_IN_DEFAULT_BROWSER);
		openInDefaultBrowser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
					URL url;
					try {
						url = new URL(skin.getBrowser().getUrl());// validate URL (to do not open 'about:blank' and similar)
						Program.launch(url.toString());
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
						ExceptionNotifier.showErrorMessage(skin.getShell(), Messages.BrowserSim_COULD_NOT_OPEN_DEFAULT_BROWSER + e1.getMessage());
					}
			}
		});
	}

	public void addViewSourceItem(Menu menu) {
		MenuItem openInDefaultBrowser = new MenuItem(menu, SWT.PUSH);
		openInDefaultBrowser.setText(Messages.BrowserSim_VIEW_PAGE_SOURCE);
		openInDefaultBrowser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (skin.getBrowser().getUrl().startsWith("file:")) { //$NON-NLS-1$
					URI uri = null;
					try {
						uri = new URI(skin.getBrowser().getUrl());
						File sourceFile = new File(uri);
						System.out.println(OPEN_FILE_COMMAND + sourceFile.getAbsolutePath()); // send command to Eclipse
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				} else {
					System.out.println(VIEW_SOURCE_COMMAND + skin.getBrowser().getUrl()); // send command to Eclipse
					String source = skin.getBrowser().getText();
					String base64Source = DatatypeConverter.printBase64Binary(source.getBytes());
					System.out.println(base64Source);
				}
			}
		});
	}
	
	public void addAboutItem(Menu menu) {
		MenuItem about = new MenuItem(menu, SWT.PUSH);
		about.setText(Messages.BrowserSim_ABOUT);
		about.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				showAboutDialog(e.display.getActiveShell());
			}

		});
	}
	
	public void addPreferencesItem(Menu menu){
		MenuItem preferences = new MenuItem(menu, SWT.PUSH);
		preferences.setText(Messages.BrowserSim_PREFERENCES);
		preferences.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DevicesList newDevicesList = new ManageDevicesDialog(e.display.getActiveShell(),
						SWT.APPLICATION_MODAL | SWT.SHELL_TRIM,
						devicesListHolder.getDevicesList()).open();
				if (newDevicesList != null) {
					devicesListHolder.setDevicesList(newDevicesList);
					devicesListHolder.notifyObservers();
				}
		
			}
		});
	}
	
	public void addUseSkinsItem(Menu menu){
		MenuItem useSkinsMenuItem = new MenuItem(menu, SWT.CHECK);
		useSkinsMenuItem.setText(Messages.BrowserSim_USE_SKINS);
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
	
	public void addExitItem(Menu menu){
		MenuItem exit = new MenuItem(menu, SWT.PUSH);
		exit.setText(Messages.BrowserSim_EXIT);
		exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				display.dispose();
			};
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
		Point size = getSizeInDesktopPixels(device);
		skin.setOrientationAndSize(	deviceOrientation.getOrientationAngle(), size,
				getSizeAdvisor());
		fixShellLocation(clientArea);
		deviceOrientation.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				int orientationAngle = ((DeviceOrientation) o).getOrientationAngle();

				Point size = getSizeInDesktopPixels(device);
				int minSize = Math.min(size.x, size.y);
				int maxSize = Math.max(size.x, size.y);
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

	@SuppressWarnings("nls")
	private void initOrientation(int orientation) {
		skin.getBrowser().execute("window.onorientationchange = null;"
				+ "window.orientation = " + orientation + ";");
	}
	
	@SuppressWarnings("nls")
	private void fireOrientationChangeEvent(int orientation, Point browserSize) {
		Rectangle clientArea = getMonitorClientArea();
		skin.setOrientationAndSize(orientation, browserSize, getSizeAdvisor());
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
	
	/**
	 * See JBIDE-11896	BrowserSim: pixel ratio problem.
	 * 
	 * On many mobile devices like iPhone 4 1 CSS pixel = 2 device pixels.
	 */
	protected Point getSizeInDesktopPixels(Device device) {
		double pixelRatio = device.getPixelRatio();
		if (device.getPixelRatio() == 0.0) {
			pixelRatio = 1.0;
			new RuntimeException("Pixel Ratio is 0.0").printStackTrace();
		}
		int width = (int) Math.round(device.getWidth() / pixelRatio);
		int height = (int) Math.round(device.getHeight() / pixelRatio);
		return new Point(width, height);
	}
	
	class ControlHandlerImpl implements ControlHandler {
		private Browser browser;
		
		public ControlHandlerImpl(Browser browser) {
			this.browser = browser;
		}

		@Override
		public void goBack() {
			browser.back();
			browser.setFocus();
		}

		@Override
		public void goForward() {
			browser.forward();
			browser.setFocus();
		}

		@Override
		public void goHome() {
			browser.setUrl(homeUrl);
			browser.setFocus();
		}

		@Override
		public void goToAddress(String address) {
			browser.setUrl(address);
			browser.setFocus();
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
			browser.setFocus();
		}

		@Override
		public void refresh() {
			browser.refresh();
			browser.setFocus();
		}
	}
	
	public void showAboutDialog(Shell shell){
		String message;
		String version = ManifestUtil.getManifestVersion();
		if (version != null) {
			message = MessageFormat.format(Messages.BrowserSim_ABOUT_MESSAGE,ManifestUtil.getManifestVersion());
		} else {
			message = MessageFormat.format(Messages.BrowserSim_ABOUT_MESSAGE, ""); //$NON-NLS-1$
		}
		new MessageBoxWithLinks(shell,
				message,
				icons[BROWSERSIM_ICON_32PX_INDEX],
				Messages.BrowserSim_ABOUT).open();	
    }
	
	
	private ResizableSkinSizeAdvisor getSizeAdvisor() {
		if (sizeAdvisor == null) {
			sizeAdvisor = new ResizableSkinSizeAdvisor() {
				@Override
				public Point checkWindowSize(int orientation, Point prefferedSize,
						Point prefferedShellSize) {
					DevicesList devicesList = devicesListHolder.getDevicesList();
					Rectangle clientArea = getMonitorClientArea();

					boolean truncateWindow = false;
					if (devicesList.getTruncateWindow() == null) {
						if (prefferedShellSize.x > clientArea.width || prefferedShellSize.y > clientArea.height) { 
							String deviceName = devicesList.getDevices().get(devicesList.getSelectedDeviceIndex()).getName();
							
							SizeWarningDialog dialog = new SizeWarningDialog(skin.getShell(), new Point(clientArea.width, clientArea.height),
									prefferedShellSize, deviceName,
									orientation == DeviceOrientation.PORTRAIT || orientation == DeviceOrientation.PORTRAIT_INVERTED);
							dialog.open();
							
							truncateWindow = dialog.getTruncateWindow();
							if (dialog.getRememberDecision()) {
								devicesList.setTruncateWindow(truncateWindow);
							}
						}
					} else {
						truncateWindow = devicesList.getTruncateWindow();
					}
					
					Point size = new Point(prefferedShellSize.x, prefferedShellSize.y);
					if (truncateWindow) {
						size.x = Math.min(prefferedShellSize.x, clientArea.width);
						size.y = Math.min(prefferedShellSize.y, clientArea.height);
					}
					
					return size;
				}
			};
		}
		
		return sizeAdvisor;
	}
	
	/**
	 * @return skin shell instance or new shell if there are no skin shell. Never returns {@code null}
	 */
	private Shell getParentShell() {
		Shell shell;
		if (skin != null && skin.getShell() != null) {
			shell = skin.getShell();
		} else {
			shell = new Shell();
		}
		return shell;
	}

	
	private void addMacOsMenuApplicationHandler(CocoaUIEnhancer enhancer) {
		
		Listener quitListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				// Quit Listener has no implementation, cause quit event is handled by  controlHandler, 
				// but it must be in CocoaUIEnchancer released by EPL  
			}
		};
		
		Runnable macAboutAction = new Runnable() {
			@Override
			public void run() {
				Shell shell = getParentShell();
				
				showAboutDialog(shell);
			}
		};

		Runnable macPreferencesAction = new Runnable() {
			@Override
			public void run() {
				Shell shell = getParentShell();

				DevicesList newDevicesList = new ManageDevicesDialog(shell,
						SWT.APPLICATION_MODAL | SWT.SHELL_TRIM,
						devicesListHolder.getDevicesList()).open();
				if (newDevicesList != null) {
					devicesListHolder.setDevicesList(newDevicesList);
					devicesListHolder.notifyObservers();
				}

			}
		};
        
		enhancer.hookApplicationMenu(display, quitListener, macAboutAction, macPreferencesAction);
	}
}
