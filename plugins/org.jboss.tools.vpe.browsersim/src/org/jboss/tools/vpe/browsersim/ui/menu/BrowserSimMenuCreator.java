/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.ui.menu;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.DevicesList;
import org.jboss.tools.vpe.browsersim.model.DevicesListHolder;
import org.jboss.tools.vpe.browsersim.ui.CocoaUIEnhancer;
import org.jboss.tools.vpe.browsersim.ui.ControlHandler;
import org.jboss.tools.vpe.browsersim.ui.ManageDevicesDialog;
import org.jboss.tools.vpe.browsersim.ui.Messages;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */

public class BrowserSimMenuCreator {
	private BrowserSimSkin skin;
	private DevicesListHolder devicesListHolder;
	private ControlHandler controlHandler;
	private String homeUrl;
	private static CocoaUIEnhancer cocoaUIEnhancer;
	
	public BrowserSimMenuCreator(BrowserSimSkin skin, DevicesListHolder devicesListHolder,
			ControlHandler controlHandler, String homeUrl) {
		this.skin = skin;
		this.devicesListHolder = devicesListHolder;
		this.controlHandler = controlHandler;
		this.homeUrl = homeUrl;
	}
	
	/**
	 * must be called before display is created
	 */
	public static void initCocoaUIEnhancer() {
		// CocoaUIEnhancer handles connection between the About, Preferences and Quit menus in MAC OS X
		if (cocoaUIEnhancer == null && PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			cocoaUIEnhancer = new CocoaUIEnhancer(Messages.BrowserSim_BROWSER_SIM);
			cocoaUIEnhancer.initializeMacOSMenuBar();
		}
	}

	public void addMenuBar() {
		Menu appMenuBar = skin.getMenuBar();
		if (appMenuBar != null) {
			// delete all existing items, if any (necessary for systems like MacOS
			// that have system (non-shell) menu bar), to do not create duplicate items while changing skins
			for (MenuItem item : appMenuBar.getItems()) {
				item.dispose();
			}

			createMenusForMenuBar(appMenuBar);
		}
		
		// set event handlers for Mac OS X Menu-bar
		if (cocoaUIEnhancer != null) {
			addMacOsMenuApplicationHandler();
			cocoaUIEnhancer.hookApplicationMenu(skin.getShell().getDisplay());
		}
	}

	public void createMenuItemsForContextMenu(Menu contextMenu) {
		contextMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu contextMenu = (Menu) e.widget;
				for (MenuItem item : contextMenu.getItems()) {
					item.dispose();
				}

				addDevicesListForMenu(contextMenu, devicesListHolder.getDevicesList());
				addUseSkinsItem(contextMenu);
				addPreferencesItem(contextMenu);

				new MenuItem(contextMenu, SWT.BAR);
				addTurnMenuItems(contextMenu, controlHandler);

				new MenuItem(contextMenu, SWT.BAR);
				ToolsMenuCreator.addItems(contextMenu, skin, devicesListHolder, homeUrl);

				new MenuItem(contextMenu, SWT.BAR);
				FileMenuCreator.addItems(contextMenu, skin);

				new MenuItem(contextMenu, SWT.BAR);
				addAboutItem(contextMenu);

				new MenuItem(contextMenu, SWT.BAR);
				addExitItem(contextMenu, skin.getShell());
			}
		});
	}

	private void createMenusForMenuBar(Menu appMenuBar) {
		Menu file = createDropDownMenu(appMenuBar, Messages.BrowserSim_FILE);
		FileMenuCreator.addItems(file, skin);

		// If Platform is Mac OS X, application will have no duplicated menu items (Exit/Quit BrowserSim)
		if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			addExitItem(file, skin.getShell());
		}

		Menu devicesMenu = createDropDownMenu(appMenuBar, Messages.BrowserSim_DEVICES);
		devicesMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu devicesMenu = (Menu) e.widget;
				for (MenuItem item : devicesMenu.getItems()) {
					item.dispose();
				}

				addDevicesListForMenu(devicesMenu, devicesListHolder.getDevicesList());
				addUseSkinsItem(devicesMenu);
				
				// If Platform is Mac OS X, application will have no duplicated menu items (Preferences)
				if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
					addPreferencesItem(devicesMenu);
				}

				new MenuItem(devicesMenu, SWT.BAR);
				addTurnMenuItems(devicesMenu, controlHandler);
			}
		});

		Menu toolsMenu = createDropDownMenu(appMenuBar, Messages.BrowserSim_TOOLS);
		ToolsMenuCreator.addItems(toolsMenu, skin, devicesListHolder, homeUrl);

		// If Platform is Mac OS X, application will have no duplicated menu items (About)
		if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			Menu help = createDropDownMenu(appMenuBar, Messages.BrowserSim_HELP);
			addAboutItem(help);
		}
	}

	private Menu createDropDownMenu(Menu menuBar, String name) {
		MenuItem menuItem = new MenuItem(menuBar, SWT.CASCADE);
		menuItem.setText(name);
		Menu dropdown = new Menu(menuBar);
		menuItem.setMenu(dropdown);
		return dropdown;
	}

	private void addDevicesListForMenu(Menu devicesMenu, final DevicesList devicesList) {
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
					MenuItem menuItem = (MenuItem) e.widget;
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

	private void addUseSkinsItem(Menu menu) {
		MenuItem useSkinsMenuItem = new MenuItem(menu, SWT.CHECK);
		useSkinsMenuItem.setText(Messages.BrowserSim_USE_SKINS);
		useSkinsMenuItem.setSelection(devicesListHolder.getDevicesList().getUseSkins());
		useSkinsMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MenuItem menuItem = (MenuItem) e.widget;
				devicesListHolder.getDevicesList().setUseSkins(menuItem.getSelection());
				devicesListHolder.getDevicesList().notifyObservers();
			}
		});
	}

	private void addPreferencesItem(Menu menu) {
		MenuItem preferences = new MenuItem(menu, SWT.PUSH);
		preferences.setText(Messages.BrowserSim_PREFERENCES);
		preferences.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DevicesList newDevicesList = new ManageDevicesDialog(e.display.getActiveShell(), SWT.APPLICATION_MODAL
						| SWT.SHELL_TRIM, devicesListHolder.getDevicesList()).open();
				if (newDevicesList != null) {
					devicesListHolder.setDevicesList(newDevicesList);
					devicesListHolder.notifyObservers();
				}

			}
		});
	}

	private void addTurnMenuItems(Menu menu, final ControlHandler controlHandler) {
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

	private void addAboutItem(Menu menu) {
		MenuItem about = new MenuItem(menu, SWT.PUSH);
		about.setText(Messages.BrowserSim_ABOUT);
		about.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				BrowserSimUtil.showAboutDialog(e.display.getActiveShell());
			}
		});
	}

	private void addExitItem(Menu menu, final Shell shell) {
		MenuItem exit = new MenuItem(menu, SWT.PUSH);
		exit.setText(Messages.BrowserSim_EXIT);
		exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			};
		});
	}

	private void addMacOsMenuApplicationHandler() {
		if (cocoaUIEnhancer != null) {
			cocoaUIEnhancer.setQuitListener(new Listener() {
				@Override
				public void handleEvent(Event event) {
					// Quit Listener has no implementation, cause quit event is handled by controlHandler,
					// but it must be in CocoaUIEnchancer released by EPL
				}
			});

			cocoaUIEnhancer.setAboutAction(new Runnable() {
				@Override
				public void run() {
					BrowserSimUtil.showAboutDialog(getParentShell());
				}
			});

			cocoaUIEnhancer.setPreferencesAction(new Runnable() {
				@Override
				public void run() {
					Shell shell = getParentShell();

					DevicesList newDevicesList = new ManageDevicesDialog(shell, SWT.APPLICATION_MODAL | SWT.SHELL_TRIM,
							devicesListHolder.getDevicesList()).open();
					if (newDevicesList != null) {
						devicesListHolder.setDevicesList(newDevicesList);
						devicesListHolder.notifyObservers();
					}
				}
			});
		}
	}

	/**
	 * @return skin shell instance or new shell if there are no skin shell.
	 *         Never returns {@code null}
	 */
	private Shell getParentShell() {
		Display display = skin.getShell().getDisplay();
		if (display != null && display.getActiveShell() != null) {
			return display.getActiveShell();
		} else if (skin != null && skin.getShell() != null) {
			return skin.getShell();
		}
		return new Shell();
	}
}