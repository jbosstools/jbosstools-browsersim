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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.preferences.CommonPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;
import org.jboss.tools.vpe.browsersim.ui.CocoaUIEnhancer;
import org.jboss.tools.vpe.browsersim.ui.ControlHandler;
import org.jboss.tools.vpe.browsersim.ui.ManageDevicesDialog;
import org.jboss.tools.vpe.browsersim.ui.Messages;
import org.jboss.tools.vpe.browsersim.ui.PreferencesWrapper;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.util.BrowserSimImageList;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;
import org.jboss.tools.vpe.browsersim.util.ManifestUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */

public class BrowserSimMenuCreator {
	private static final String ABOUT_ICON = "icons/browsersim_32px.png"; //$NON-NLS-1$
	
	private BrowserSimSkin skin;
	private static CommonPreferences commonPreferences;
	private SpecificPreferences specificPreferences;
	private ControlHandler controlHandler;
	private String homeUrl;
	
	public BrowserSimMenuCreator(BrowserSimSkin skin, CommonPreferences cp, SpecificPreferences sp,
			ControlHandler controlHandler, String homeUrl) {
		this.skin = skin;
		commonPreferences = cp;
		this.specificPreferences = sp;
		this.controlHandler = controlHandler;
		this.homeUrl = homeUrl;
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
		CocoaUIEnhancer cocoaUIEnhancer = CocoaUIEnhancer.getInstance();
		if (cocoaUIEnhancer != null) {
			addMacOsMenuApplicationHandler(cocoaUIEnhancer);
		}
	}

	public void createMenuItemsForContextMenu(Menu contextMenu) {
		contextMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu contextMenu = (Menu) e.widget;
				for (MenuItem item : contextMenu.getItems()) {
					item.dispose();
				}

				addUseSkinsItem(contextMenu);
				addDevicesListForMenu(contextMenu);
				
				new MenuItem(contextMenu, SWT.BAR);
				addTurnMenuItems(contextMenu, controlHandler);

				new MenuItem(contextMenu, SWT.BAR);
				addToolsItems(contextMenu, skin, commonPreferences, specificPreferences, homeUrl);

				new MenuItem(contextMenu, SWT.BAR);
				addFileItemsToContextMenu(contextMenu, skin, commonPreferences, specificPreferences);
				
				// JBIDE-16581 Need to remove 'About' from the context menu
				if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
					new MenuItem(contextMenu, SWT.BAR);
					addAboutItem(contextMenu);
				}

				new MenuItem(contextMenu, SWT.BAR);
				addExitItem(contextMenu, skin.getShell());
			}
		});
	}

	private void createMenusForMenuBar(Menu appMenuBar) {
		Menu file = createDropDownMenu(appMenuBar, Messages.BrowserSim_FILE);
		addFileItemsToMenuBar(file, skin, commonPreferences, specificPreferences);

		// If Platform is Mac OS X, application will have no duplicated menu items (Exit/Quit BrowserSim)
		if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			addExitItem(file, skin.getShell());
		}

		Menu devicesMenu = createDropDownMenu(appMenuBar, Messages.BrowserSim_DEVICE);
		devicesMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu devicesMenu = (Menu) e.widget;
				for (MenuItem item : devicesMenu.getItems()) {
					item.dispose();
				}

				addTurnMenuItems(devicesMenu, controlHandler);

				new MenuItem(devicesMenu, SWT.BAR);
				addUseSkinsItem(devicesMenu);
				addDevicesListForMenu(devicesMenu);
			}
		});

		Menu toolsMenu = createDropDownMenu(appMenuBar, Messages.BrowserSim_TOOLS);
		toolsMenu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent e) {
				Menu toolsMenu = (Menu) e.widget;
				for (MenuItem item : toolsMenu.getItems()) {
					item.dispose();
				}
				
				addToolsItems(toolsMenu, skin, commonPreferences, specificPreferences, homeUrl);
			}
		});

		// If Platform is Mac OS X, application will have no duplicated menu items (About)
		if (!PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			Menu help = createDropDownMenu(appMenuBar, Messages.BrowserSim_HELP);
			addAboutItem(help);
		}
	}
	
	protected void addToolsItems(Menu contextMenu, BrowserSimSkin skin, CommonPreferences commonPreferences,
			SpecificPreferences specificPreferences, String homeUrl) {
		ToolsMenuCreator.addDebugItem(contextMenu, skin, commonPreferences.getWeinreScriptUrl(), commonPreferences.getWeinreClientUrl(), specificPreferences.isJavaFx());
		ToolsMenuCreator.addScreenshotMenuItem(contextMenu, skin, commonPreferences);
		ToolsMenuCreator.addSyncronizedWindowItem(contextMenu, skin, commonPreferences.getDevices(),
				specificPreferences.getUseSkins(), specificPreferences.isEnableLiveReload(),
				specificPreferences.getLiveReloadPort(), specificPreferences.isEnableTouchEvents(), specificPreferences.getOrientationAngle(), homeUrl, specificPreferences.isJavaFx());
		ToolsMenuCreator.addLiveReloadItem(contextMenu, specificPreferences);
		ToolsMenuCreator.addTouchEventsItem(contextMenu, specificPreferences);
	}

	protected void addFileItemsToContextMenu(final Menu menu, final BrowserSimSkin skin, final CommonPreferences commonPreferences, final SpecificPreferences specificPreferences) {
		new FileMenuCreator().addItemsToContextMenu(menu, skin, commonPreferences, specificPreferences);
	}
	
	protected void addFileItemsToMenuBar(final Menu menu, final BrowserSimSkin skin, final CommonPreferences commonPreferences, final SpecificPreferences specificPreferences) {
		new FileMenuCreator().addItemsToMenuBar(menu, skin, commonPreferences, specificPreferences);
	}
	
	private Menu createDropDownMenu(Menu menuBar, String name) {
		MenuItem menuItem = new MenuItem(menuBar, SWT.CASCADE);
		menuItem.setText(name);
		Menu dropdown = new Menu(menuBar);
		menuItem.setMenu(dropdown);
		return dropdown;
	}

	private void addDevicesListForMenu(Menu devicesMenu) {
		MenuItem skins = new MenuItem(devicesMenu, SWT.CASCADE);
		skins.setText(Messages.BrowserSim_SKIN);
		Menu subMenu = new Menu(skins);
		for (Device device : commonPreferences.getDevices().values()) {
			MenuItem deviceMenuItem = new MenuItem(subMenu, SWT.RADIO);
			deviceMenuItem.setText(device.getName());
			deviceMenuItem.setData(device.getId());
			if (device.getId().equals(specificPreferences.getSelectedDeviceId())) {
				deviceMenuItem.setSelection(true);
			}

			deviceMenuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					MenuItem menuItem = (MenuItem) e.widget;
					if (menuItem.getSelection()) {
						Device selected = commonPreferences.getDevices().get(menuItem.getData());
						specificPreferences.setSelectedDeviceId(selected.getId());
						specificPreferences.notifyObservers();
					}
				};
			});
		}
		skins.setMenu(subMenu);
	}

	private void addUseSkinsItem(Menu menu) {
		MenuItem useSkinsMenuItem = new MenuItem(menu, SWT.CHECK);
		useSkinsMenuItem.setText(Messages.BrowserSim_USE_SKINS);
		useSkinsMenuItem.setSelection(specificPreferences.getUseSkins());
		useSkinsMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MenuItem menuItem = (MenuItem) e.widget;
				specificPreferences.setUseSkins(menuItem.getSelection());
				specificPreferences.notifyObservers();
			}
		});
	}
		
	private void addTurnMenuItems(Menu menu, final ControlHandler controlHandler) {
		MenuItem turnLeft = new MenuItem(menu, SWT.PUSH);
		turnLeft.setText(Messages.BrowserSim_ROTATE_LEFT);
		turnLeft.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controlHandler.rotate(true);
			}
		});

		MenuItem turnRight = new MenuItem(menu, SWT.PUSH);
		turnRight.setText(Messages.BrowserSim_ROTATE_RIGHT);
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
				Shell parentShell = e.display.getActiveShell();
				if (parentShell == null) {
					parentShell = e.display.getShells()[0]; // Hot fix for gtk3
				}				
				showAboutDialog(parentShell);
			}
		});
	}

	private void addExitItem(Menu menu, final Shell shell) {
		MenuItem close = new MenuItem(menu, SWT.PUSH);
		close.setText(Messages.BrowserSim_CLOSE);
		close.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		
		final List<BrowserSim> instances = new ArrayList<BrowserSim>(BrowserSim.getInstances());
		if (instances.size() > 1) {
			MenuItem closeOther = new MenuItem(menu, SWT.PUSH);
			closeOther.setText(Messages.BrowserSim_CLOSE_OTHER);
			closeOther.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					for (BrowserSim browserSim : instances) {
						Shell source = ((MenuItem)e.widget).getParent().getShell();
						if (!source.equals(browserSim.getBrowser().getShell())) {
							browserSim.getBrowser().getShell().close();	
						}
					}
				}
			});

			MenuItem closeAll = new MenuItem(menu, SWT.PUSH);
			closeAll.setText(Messages.BrowserSim_CLOSE_ALL);
			closeAll.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					for (BrowserSim browserSim : instances) {
						browserSim.getBrowser().getShell().close();	
					}
				}
			});
		}
	}

	private void addMacOsMenuApplicationHandler(CocoaUIEnhancer cocoaUIEnhancer) {
		if (cocoaUIEnhancer != null) {
			cocoaUIEnhancer.setQuitAction(new Runnable() {
				@Override
				public void run() {
					//skin's shell has close listener which saves preferences
					skin.getShell().close();
				}
			});

			cocoaUIEnhancer.setAboutAction(new Runnable() {
				@Override
				public void run() {
					showAboutDialog(getParentShell());
				}
			});

			cocoaUIEnhancer.setPreferencesAction(new Runnable() {
				@Override
				public void run() {
					Shell shell = getParentShell();
					PreferencesWrapper pw = openDialog(shell, commonPreferences, specificPreferences, skin.getBrowser().getUrl());
					if (pw != null) {
						commonPreferences.copyProperties(pw.getCommonPreferences());
						specificPreferences.copyProperties(pw.getSpecificPreferences());
						commonPreferences.notifyObservers();
						specificPreferences.notifyObservers();
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
	
	private void showAboutDialog(Shell shell) {
		String message = ""; //$NON-NLS-1$
		String version = ManifestUtil.getManifestVersion(BrowserSim.class);
		if (version != null) {
			message = MessageFormat.format(Messages.BrowserSim_ABOUT_BROWSERSIM_MESSAGE, ManifestUtil.getManifestVersion(BrowserSim.class));
		} else {
			message = MessageFormat.format(Messages.BrowserSim_ABOUT_BROWSERSIM_MESSAGE, ""); //$NON-NLS-1$
		}
		BrowserSimImageList imageList = new BrowserSimImageList(shell);
		BrowserSimUtil.showAboutDialog(shell, message, imageList.getImage(ABOUT_ICON));
	}
	
	protected PreferencesWrapper openDialog(Shell parentShell, CommonPreferences commonPreferences,
			SpecificPreferences specificPreferences, String currentUrl) {
		return new ManageDevicesDialog(parentShell, SWT.APPLICATION_MODAL
				| SWT.SHELL_TRIM, commonPreferences, specificPreferences, currentUrl).open();
	}
}