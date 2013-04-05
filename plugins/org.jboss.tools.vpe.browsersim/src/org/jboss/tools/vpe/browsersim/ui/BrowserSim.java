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
package org.jboss.tools.vpe.browsersim.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.preferences.CommonPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.CommonPreferencesStorage;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferencesStorage;
import org.jboss.tools.vpe.browsersim.ui.debug.firebug.FireBugLiteLoader;
import org.jboss.tools.vpe.browsersim.ui.events.ExitListener;
import org.jboss.tools.vpe.browsersim.ui.events.SkinChangeEvent;
import org.jboss.tools.vpe.browsersim.ui.events.SkinChangeListener;
import org.jboss.tools.vpe.browsersim.ui.menu.BrowserSimMenuCreator;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ResizableSkinSizeAdvisor;
import org.jboss.tools.vpe.browsersim.ui.skin.ResizableSkinSizeAdvisorImpl;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;
import org.jboss.tools.vpe.browsersim.util.ImageList;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public class BrowserSim {
	public static final String BROWSERSIM_PLUGIN_ID = "org.jboss.tools.vpe.browsersim"; //$NON-NLS-1$
	private static final String[] BROWSERSIM_ICONS = {"icons/browsersim_16px.png", "icons/browsersim_32px.png", "icons/browsersim_64px.png", "icons/browsersim_128px.png", "icons/browsersim_256px.png", }; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
	
	public static boolean isStandalone;
	private static List<BrowserSim> instances;
	private String homeUrl;
	private static CommonPreferences commonPreferences;
	private SpecificPreferences specificPreferences;
	private ResizableSkinSizeAdvisor resizableSkinSizeAdvisor;
	private BrowserSimSkin skin;
	private ControlHandler controlHandler;
	private ImageList imageList;
	private Image[] icons;
	private Point currentLocation;
	private ProgressListener progressListener;
	private Observer commonPreferencesObserver;
	private List<SkinChangeListener> skinChangeListenerList = new ArrayList<SkinChangeListener>();
	private List<ExitListener> exitListenerList = new ArrayList<ExitListener>();
	
	static {
		instances = new ArrayList<BrowserSim>();
	}

	public BrowserSim(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	
	public void open() {
		commonPreferences = (CommonPreferences) CommonPreferencesStorage.INSTANCE.load();
		if (commonPreferences == null) {
			commonPreferences = (CommonPreferences) CommonPreferencesStorage.INSTANCE.loadDefault();
		}
		
		SpecificPreferences sp = (SpecificPreferences) SpecificPreferencesStorage.INSTANCE.load();
		if (sp == null) {
			sp = (SpecificPreferences) SpecificPreferencesStorage.INSTANCE.loadDefault();
		}
		
		open(sp, null);
	}

	public void open(SpecificPreferences sp, String url) {
		if (url == null) {
			url = homeUrl;
		}
		
		specificPreferences = sp;
		initObservers();
		Device defaultDevice = commonPreferences.getDevices().get(specificPreferences.getSelectedDeviceId()); 
		if (defaultDevice == null) {
			System.out.println("Could not find selected device in devices list");
			String id;
			try {
				id = commonPreferences.getDevices().keySet().iterator().next();
			} catch (NoSuchElementException e) {
				commonPreferences = CommonPreferencesStorage.INSTANCE.loadDefault();
				id = commonPreferences.getDevices().keySet().iterator().next();
			}
			specificPreferences.setSelectedDeviceId(id);
			defaultDevice = commonPreferences.getDevices().get(id);
		}
		
		initSkin(BrowserSimUtil.getSkinClass(defaultDevice, specificPreferences.getUseSkins()), specificPreferences.getLocation());
		setSelectedDevice();
		controlHandler.goToAddress(url);
		
		instances.add(BrowserSim.this);
		skin.getShell().open();
	}
	
	private void initSkin(Class<? extends BrowserSimSkin> skinClass, Point location) {
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
		
		Display display = Display.getDefault();
		
		try {
			skin.createControls(display, location);
			currentLocation = location;
		} catch (SWTError e) {
			e.printStackTrace();
			ExceptionNotifier.showWebKitLoadError(new Shell(display), e);
			display.dispose();
			return;
		}
		
		final Shell shell = skin.getShell();
		resizableSkinSizeAdvisor = new ResizableSkinSizeAdvisorImpl(commonPreferences, specificPreferences, shell);
		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlMoved(ControlEvent e) {
				currentLocation = shell.getLocation();
				super.controlMoved(e);
			}
		});
		
		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				instances.remove(BrowserSim.this);
				specificPreferences.setLocation(currentLocation);
				SpecificPreferencesStorage.INSTANCE.save(specificPreferences);
				if(instances.isEmpty()) {
					CommonPreferencesStorage.INSTANCE.save(commonPreferences);
				}
				commonPreferences.deleteObserver(commonPreferencesObserver);
			}
		});
		
		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				for (ExitListener e : exitListenerList) {
					e.exit();
				}
			}
		});

		final BrowserSimBrowser browser = getBrowser();
		controlHandler = createControlHandler(browser, homeUrl, specificPreferences);
		final BrowserSimMenuCreator menuCreator = createMenuCreator(skin, commonPreferences, specificPreferences, controlHandler, homeUrl);
		
		shell.addShellListener(new ShellListener() {
			@Override
			public void shellIconified(ShellEvent e) {
			}
			
			@Override
			public void shellDeiconified(ShellEvent e) {
			}
			
			@Override
			public void shellDeactivated(ShellEvent e) {
			}
			
			@Override
			public void shellClosed(ShellEvent e) {
			}
			
			@Override
			public void shellActivated(ShellEvent e) {
				//adding menu on activation to make it working properly on every sync window
				menuCreator.addMenuBar();
			}
		});
		menuCreator.addMenuBar();
		setShellAttibutes();
		
		skin.setControlHandler(controlHandler);
		
		Menu contextMenu = new Menu(shell);
		skin.setContextMenu(contextMenu);
		menuCreator.createMenuItemsForContextMenu(contextMenu);

		progressListener = new ProgressListener() {
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
		};
		browser.addProgressListener(progressListener);

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
				initOrientation(specificPreferences.getOrientationAngle());
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

		browser.addOpenWindowListener(new OpenWindowListener() {
			public void open(WindowEvent event) {
				if (FireBugLiteLoader.isFireBugPopUp(event)) {
					FireBugLiteLoader.processFireBugPopUp(event);
				} else {
					event.browser = browser;
				}
			}
		});

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
							skin.getShell().getDisplay().asyncExec(new Runnable() {
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

		browser.addLocationListener(new LocationAdapter() {
			@Override
			public void changed(LocationEvent event) {
				if (getBrowser().equals(Display.getDefault().getFocusControl()) && event.top) {
					for (BrowserSim bs : instances) {
						if (bs.skin != skin) {
							bs.skin.getBrowser().setUrl(event.location);
						}
					}
				}
			}
		});

		browser.addTitleListener(new TitleListener() {
			@Override
			public void changed(TitleEvent event) {
				skin.pageTitleChanged(event.title);
			}
		});
		fireSkinChangeEvent();
	}
	
	private void initImages() {
		imageList = new ImageList(skin.getShell());
		this.icons = new Image[BROWSERSIM_ICONS.length];
		for (int i = 0; i < BROWSERSIM_ICONS.length; i++) {
			icons[i] = imageList.getImage(BROWSERSIM_ICONS[i]);
		}
	}
	
	private void setShellAttibutes() {
		Shell shell = skin.getShell();
		if (shell != null) {
			initImages();
			shell.setImages(icons);
			shell.setText(Messages.BrowserSim_BROWSER_SIM);
		}
	}

	private void initObservers() {
		commonPreferencesObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				setSelectedDeviceAsync();
			}
		};
		commonPreferences.addObserver(commonPreferencesObserver);
		specificPreferences.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				setSelectedDeviceAsync();
			}
		});
	}
	
	private boolean deviceUpdateRequired = false;
	private void setSelectedDeviceAsync() {
		deviceUpdateRequired = true;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (deviceUpdateRequired) {
					setSelectedDevice();
					deviceUpdateRequired = false;
				}
			}
		});
	}

	private void setSelectedDevice() {
		final Device device = commonPreferences.getDevices().get(specificPreferences.getSelectedDeviceId());
		if (device == null) {
			skin.getShell().close();
		} else {
			Class<? extends BrowserSimSkin> newSkinClass = BrowserSimUtil.getSkinClass(device, specificPreferences.getUseSkins());
			String oldSkinUrl = null;
			if (newSkinClass != skin.getClass()) {
				oldSkinUrl = skin.getBrowser().getUrl();
				Point currentLocation = skin.getShell().getLocation();
				skin.getBrowser().removeProgressListener(progressListener);
				skin.getBrowser().getShell().dispose();
				initSkin(newSkinClass, currentLocation);
			}
			setOrientation(specificPreferences.getOrientationAngle(), device);
	
			skin.getBrowser().setDefaultUserAgent(device.getUserAgent());
	
			if (oldSkinUrl != null) {
				skin.getBrowser().setUrl(oldSkinUrl); // skin (and browser instance) is changed
			} else {
				skin.getBrowser().refresh(); // only user agent and size of the browser is changed
			}
	
			skin.getShell().open();
		}
	}

	@SuppressWarnings("nls")
	private void initOrientation(int orientation) {
		getBrowser().execute("window.onorientationchange = null;" + "window.orientation = " + orientation + ";");
	}

	@SuppressWarnings("nls")
	private void setOrientation(int orientationAngle, Device device) {
		Point size = BrowserSimUtil.getSizeInDesktopPixels(device);
		Point browserSize;
		if (orientationAngle == SpecificPreferences.ORIENTATION_LANDSCAPE
				|| orientationAngle == SpecificPreferences.ORIENTATION_LANDSCAPE_INVERTED) {
			browserSize = new Point(size.y, size.x);
		} else {
			browserSize = size;
		}
		
		Rectangle clientArea = BrowserSimUtil.getMonitorClientArea(skin.getShell().getMonitor());
		skin.setOrientationAndSize(orientationAngle, browserSize, resizableSkinSizeAdvisor);
		BrowserSimUtil.fixShellLocation(skin.getShell(), clientArea);
		getBrowser().execute("window.orientation = " + orientationAngle + ";"
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

	public BrowserSimBrowser getBrowser() {
		return skin != null ? skin.getBrowser() : null;
	}
	
	
	public void addSkinChangeListener(SkinChangeListener listener) {
		skinChangeListenerList.add(listener);
	}
	
	public void addExitListener(ExitListener listener){
		exitListenerList.add(listener);
	}
	
	public void fireSkinChangeEvent() {
		SkinChangeEvent event = new SkinChangeEvent(this, skin);
		for (SkinChangeListener listener : skinChangeListenerList) {
			listener.skinChanged(event);
		}
	}

	/**
	 * {@link ControlHandler} factory method.
	 * 
	 * Override this method if you need a custom {@link ControlHandler}
	 */
	protected ControlHandler createControlHandler(BrowserSimBrowser browser, String homeUrl, SpecificPreferences specificPreferences) {
		return new BrowserSimControlHandler(browser, homeUrl, specificPreferences);
	}
	
	/**
	 * {@link BrowserSimMenuCreator} factory method.
	 * 
	 * Override this method if you need a custom {@link BrowserSimMenuCreator}
	 */
	protected BrowserSimMenuCreator createMenuCreator(BrowserSimSkin skin, CommonPreferences commonPreferences, SpecificPreferences specificPreferences, ControlHandler controlHandler, String homeUrl) {
		return new BrowserSimMenuCreator(skin, commonPreferences, specificPreferences, controlHandler, homeUrl);
	}	

}
