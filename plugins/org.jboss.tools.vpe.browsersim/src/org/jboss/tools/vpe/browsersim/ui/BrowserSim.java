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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jboss.tools.vpe.browsersim.browser.AbstractWebKitBrowser;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.DeviceOrientation;
import org.jboss.tools.vpe.browsersim.model.DevicesList;
import org.jboss.tools.vpe.browsersim.model.DevicesListHolder;
import org.jboss.tools.vpe.browsersim.model.DevicesListStorage;
import org.jboss.tools.vpe.browsersim.util.ResourcesUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class BrowserSim implements Runnable {
	private static final String DEFAULT_URL = "about:blank"; //"http://www.w3schools.com/js/tryit_view.asp?filename=try_nav_useragent"; //$NON-NLS-1$
	private AbstractWebKitBrowser browser;
	private Display display;
	private Shell shell;
	private Text locationText;
	private Label statusLabel;
	private ProgressBar progressBar;
	private String initialUrl;
	private Menu devicesMenu;
	private DevicesListHolder devicesListHolder;
	private DeviceOrientation deviceOrientation;

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
		shell = new Shell(display);
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (devicesListHolder != null) {
					DevicesListStorage.saveUserDefinedDevicesList(devicesListHolder.getDevicesList());
				}
			}
		});
		shell.setText(Messages.BrowserSim_BROWSER_SIM);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		Menu appMenuBar = display.getMenuBar();
		if (appMenuBar == null) {
			appMenuBar = new Menu(shell, SWT.BAR);
			shell.setMenuBar(appMenuBar);
		}

		ToolBar toolbar = createControlBar();		
		GridData data = new GridData();
		data.horizontalSpan = 3;
		toolbar.setLayoutData(data);

		Label labelAddress = new Label(shell, SWT.NONE);
		labelAddress.setText(Messages.BrowserSim_ADDRESS);
		
		locationText = new Text(shell, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		data.widthHint = 0;
		locationText.setLayoutData(data);

		try {
			browser = WebKitBrowserFactory.createWebKitBrowser(shell, SWT.NONE);
		} catch (SWTError e) {
			System.out.println(Messages.BrowserSim_COULD_NOT_INSTANTIATE_WEBKIT_BROWSER + e.getMessage());
			
			MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
			messageBox.setText("Error");
			messageBox.setMessage(Messages.BrowserSim_COULD_NOT_INSTANTIATE_WEBKIT_BROWSER + e.getMessage());
			messageBox.open();
			
			display.dispose();
			return;
		}

		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		browser.setLayoutData(data);

		statusLabel = new Label(shell, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.widthHint = 0;
		statusLabel.setLayoutData(data);

		progressBar = new ProgressBar(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		progressBar.setLayoutData(data);

		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
					if (event.total == 0) return;                            
					int ratio = event.current * 100 / event.total;
					progressBar.setSelection(ratio);
			}
			public void completed(ProgressEvent event) {
				progressBar.setSelection(0);
			}
		});
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				statusLabel.setText(event.text);	
			}
		});
		browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				if (event.top) {
					locationText.setText(event.location);
				}
				initOrientation(deviceOrientation.getOrientationAngle());
			}
			public void changing(LocationEvent event) {
			}
		});
		
		devicesListHolder = new DevicesListHolder();
		fillMenuBar(appMenuBar);
		
		locationText.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				browser.setUrl(locationText.getText());
			}
		});
		
		DevicesList devicesList = devicesListHolder.getDevicesList(); 
		if (devicesList != null && devicesList.getDevices() != null 
				&& devicesList.getSelectedDeviceIndex() < devicesList.getDevices().size()) {
			setDevice(devicesList.getDevices().get(devicesList.getSelectedDeviceIndex()));
		}
		shell.open();
		browser.setUrl(initialUrl);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public ToolBar createControlBar() {
		ToolBar toolbar = new ToolBar(shell, SWT.NONE);
		ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
//		itemBack.setText("Back");
		itemBack.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				browser.back();
			}
		});
		
		ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
//		itemForward.setText("Forward");
		itemForward.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				browser.forward();
			}
		});
		
		ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
//		itemStop.setText("Stop");
		itemStop.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				browser.stop();
			}
		});
		
		ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
//		itemRefresh.setText("Refresh");
		itemRefresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				browser.refresh();
			}
		});
		
		ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
//		itemGo.setText("Go");
		itemGo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				browser.setUrl(locationText.getText());
			}
		});
		
		ToolItem itemRotateCounterclockwise = new ToolItem(toolbar, SWT.PUSH);
//		itemGo.setText("Rotate Counterclockwise");
		itemRotateCounterclockwise.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				rotateDevice(true);
			}
		});
		
		ToolItem itemRotateClockwise = new ToolItem(toolbar, SWT.PUSH);
//		itemGo.setText("Rotate Clockwise");
		itemRotateClockwise.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				rotateDevice(false);
			}
		});
		
		final Image imageBack = new Image(display, ResourcesUtil.getResourceAsStream("/org/jboss/tools/vpe/browsersim/resources/icons/nav_backward.gif")); //$NON-NLS-1$
		final Image imageForward = new Image(display, ResourcesUtil.getResourceAsStream("/org/jboss/tools/vpe/browsersim/resources/icons/nav_forward.gif")); //$NON-NLS-1$
		final Image imageStop = new Image(display, ResourcesUtil.getResourceAsStream("/org/jboss/tools/vpe/browsersim/resources/icons/nav_stop.gif")); //$NON-NLS-1$
		final Image imageRefresh = new Image(display, ResourcesUtil.getResourceAsStream("/org/jboss/tools/vpe/browsersim/resources/icons/nav_refresh.gif")); //$NON-NLS-1$
		final Image imageGo = new Image(display, ResourcesUtil.getResourceAsStream("/org/jboss/tools/vpe/browsersim/resources/icons/nav_go.gif")); //$NON-NLS-1$
		final Image imageRotateClockwise = new Image(display, ResourcesUtil.getResourceAsStream("/org/jboss/tools/vpe/browsersim/resources/icons/rotate_clockwise.png")); //$NON-NLS-1$
		final Image imageRotateCounterclockwise = new Image(display, ResourcesUtil.getResourceAsStream("/org/jboss/tools/vpe/browsersim/resources/icons/rotate_counterclockwise.png")); //$NON-NLS-1$		
		
		itemBack.setImage(imageBack);
		itemForward.setImage(imageForward);
		itemStop.setImage(imageStop);
		itemRefresh.setImage(imageRefresh);
		itemGo.setImage(imageGo);
		itemRotateClockwise.setImage(imageRotateClockwise);
		itemRotateCounterclockwise.setImage(imageRotateCounterclockwise);
		
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				imageBack.dispose();
				imageForward.dispose();
				imageStop.dispose();
				imageRefresh.dispose();
				imageGo.dispose();
				imageRotateClockwise.dispose();
				imageRotateCounterclockwise.dispose();
			}
		});

		return toolbar;
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
		
		DevicesList devicesList = DevicesListStorage.loadUserDefinedDevicesList();
		if (devicesList == null) {
			devicesList = DevicesListStorage.loadDefaultDevicesList();
		}
		
		devicesListHolder.setDevicesList(devicesList);
		devicesListHolder.notifyObservers();
		
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
		for (MenuItem menuItem : devicesMenu.getItems()) {
			if (menuItem.getData() instanceof Device) {
				menuItem.setSelection(menuItem.getData() == device);				
			}
		}
		
		setBrowserSize(device.getWidth(), device.getHeight());
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
					setBrowserSize(maxSize, minSize);					
				} else {
					setBrowserSize(minSize, maxSize);										
				}
				
				fireOrientationChangeEvent(orientationAngle);
			}
		});

		browser.setDefaultUserAgent(device.getUserAgent());
		browser.refresh();
	}

	private void setBrowserSize(int width, int height) {
		GridData data = (GridData) browser.getLayoutData();
		
		Rectangle clientArea = display.getClientArea();
		int shellWidthHint = SWT.DEFAULT;
		if (width != Device.DEFAULT_SIZE) {
			data.widthHint = width;
		} else if (data.widthHint == SWT.DEFAULT) {
			shellWidthHint = clientArea.width;
		}
		int shellHeightHint = SWT.DEFAULT;
		if (height != Device.DEFAULT_SIZE) {
			data.heightHint =  height;
		} else if (data.heightHint == SWT.DEFAULT) {
			shellHeightHint = clientArea.height;
		}
		Point shellSize = shell.computeSize(shellWidthHint, shellHeightHint);
		shellSize.x = Math.min(shellSize.x, clientArea.width);
		shellSize.y = Math.min(shellSize.y, clientArea.height);
		shell.setSize(shellSize);
		
		Rectangle shellBounds = shell.getBounds();
		int bottomOverlap = shellBounds.y + shellBounds.height - (clientArea.y + clientArea.height);
		if (bottomOverlap > 0) {
			if (shellBounds.y > bottomOverlap) {
				shellBounds.y -= bottomOverlap;
			} else {
				shellBounds.y = 0;
			}
		}
		
		int rightOverlap = shellBounds.x + shellBounds.width - (clientArea.x + clientArea.width);
		if (rightOverlap > 0) {
			if (shellBounds.x > rightOverlap) {
				shellBounds.x -= rightOverlap;
			} else {
				shellBounds.x = 0;
			}
		}
		
		shell.setBounds(shellBounds);
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
}
