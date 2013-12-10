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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.BrowserSimLogger;
import org.jboss.tools.vpe.browsersim.BrowserSimRunner;
import org.jboss.tools.vpe.browsersim.browser.ExtendedOpenWindowListener;
import org.jboss.tools.vpe.browsersim.browser.ExtendedWindowEvent;
import org.jboss.tools.vpe.browsersim.browser.IBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserFunction;
import org.jboss.tools.vpe.browsersim.browser.IDisposable;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.model.preferences.BrowserSimSpecificPreferencesStorage;
import org.jboss.tools.vpe.browsersim.model.preferences.CommonPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.CommonPreferencesStorage;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferencesStorage;
import org.jboss.tools.vpe.browsersim.scripting.TouchSupportLoader;
import org.jboss.tools.vpe.browsersim.scripting.WebSqlLoader;
import org.jboss.tools.vpe.browsersim.ui.debug.firebug.FireBugLiteLoader;
import org.jboss.tools.vpe.browsersim.ui.events.ExitListener;
import org.jboss.tools.vpe.browsersim.ui.events.SkinChangeEvent;
import org.jboss.tools.vpe.browsersim.ui.events.SkinChangeListener;
import org.jboss.tools.vpe.browsersim.ui.menu.BrowserSimMenuCreator;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ResizableSkinSizeAdvisor;
import org.jboss.tools.vpe.browsersim.ui.skin.ResizableSkinSizeAdvisorImpl;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public class BrowserSim {
	public static final String BROWSERSIM_PLUGIN_ID = "org.jboss.tools.vpe.browsersim"; //$NON-NLS-1$

	private static List<BrowserSim> instances;
	private Shell parentShell; // is needed for CordovaSim in order to have one icon in the taskbar JBIDE-14578 
	private String homeUrl;
	private static CommonPreferences commonPreferences;
	private SpecificPreferences specificPreferences;
	private ResizableSkinSizeAdvisor resizableSkinSizeAdvisor;
	protected BrowserSimSkin skin;
	private ControlHandler controlHandler;
	private Point currentLocation;
	private ProgressListener progressListener;
	private Observer commonPreferencesObserver;
	private LocationAdapter liveReloadLocationAdapter;
	private LocationAdapter touchEventsLocationAdapter;
	private List<SkinChangeListener> skinChangeListenerList = new ArrayList<SkinChangeListener>();
	private List<ExitListener> exitListenerList = new ArrayList<ExitListener>();
	
	static {
		instances = new ArrayList<BrowserSim>();
		if (commonPreferences == null) {
			commonPreferences = (CommonPreferences) CommonPreferencesStorage.INSTANCE.load();
			if (commonPreferences == null) {
				commonPreferences = (CommonPreferences) CommonPreferencesStorage.INSTANCE.loadDefault();
			}
		}
	}
	
	public BrowserSim(String homeUrl, Shell parent) {
		this.homeUrl = homeUrl;
		parentShell = parent;
	}
	
	public void open() {
		SpecificPreferences sp = (SpecificPreferences) getSpecificPreferencesStorage().load();
		if (sp == null) {
			sp = (SpecificPreferences) getSpecificPreferencesStorage().loadDefault();
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
			BrowserSimLogger.logError(Messages.BrowserSim_NO_SELECTED_DEVICE, new NullPointerException());
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
		
		initSkin(BrowserSimUtil.getSkinClass(defaultDevice, specificPreferences.getUseSkins()), specificPreferences.getLocation(), parentShell);
		setSelectedDevice(null);
		controlHandler.goToAddress(url);
		
		instances.add(BrowserSim.this);
		skin.getShell().open();
	}
	
	private void initSkin(Class<? extends BrowserSimSkin> skinClass, Point location, final Shell parentShell) {
		try {
			skin = skinClass.newInstance();//new AppleIPhone3Skin();//new NativeSkin();
		} catch (InstantiationException e1) {
			BrowserSimLogger.logError(e1.getMessage(), e1);
		} catch (IllegalAccessException e1) {
			BrowserSimLogger.logError(e1.getMessage(), e1);
		}

		skin.setBrowserFactory(new WebKitBrowserFactory());
		
		Display display = Display.getDefault();
		
		skin.createControls(display, location, parentShell);
		skin.setAddressBarVisible(isAddressBarVisibleByDefault());
		currentLocation = location;
		
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
				getSpecificPreferencesStorage().save(specificPreferences);
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

		final IBrowser browser = getBrowser();
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
		BrowserSimRunner.setShellAttributes(shell);
		
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
		
		browser.addLocationListener(createNavButtonsListener());

		browser.addLocationListener(new LocationAdapter() {
			public void changed(LocationEvent event) {
				initOrientation(specificPreferences.getOrientationAngle());
				WebSqlLoader.initWebSql(skin.getBrowser());
			}
		});

		//JBIDE-12191 - custom scrollbars work satisfactorily on windows only
		BrowserSimUtil.setCustomScrollbarStylesForWindows(browser);

		browser.addOpenWindowListener(new ExtendedOpenWindowListener() {
			
			@Override
			public void open(ExtendedWindowEvent event) {
				if (FireBugLiteLoader.isFireBugPopUp(event)) {
					FireBugLiteLoader.processFireBugPopUp(event, skin);
				} else {
					event.browser = browser;
				}
			}
		});

		browser.addLocationListener(new LocationListener() {
			private IDisposable scrollListener = null;

			public void changed(LocationEvent event) {
				if (scrollListener != null) {
					scrollListener.dispose();
				}
				scrollListener = ((IBrowser)event.widget).registerBrowserFunction("_browserSim_scrollListener", //$NON-NLS-1$
						new IBrowserFunction() {							
							@Override
							public Object function(Object[] arguments) {
								double pageYOffset = (Double) arguments[0];
								if (pageYOffset > 0.0) {
									skin.getShell().getDisplay().asyncExec(new Runnable() {
										public void run() {
											if (skin != null && skin.getShell() != null && !skin.getShell().isDisposed()) {
										if (skin.automaticallyHideAddressBar()) {
											skin.setAddressBarVisible(false);
										}
									}
								}
									});
								}
								return null;
					}
				});

				IBrowser browser = (IBrowser)event.widget;
				browser.execute(
								"(function() {" + //$NON-NLS-1$
									"var scrollListener = function(e){" + //$NON-NLS-1$
										"window._browserSim_scrollListener(window.pageYOffset)" + //$NON-NLS-1$
									"};" + //$NON-NLS-1$
									"window.addEventListener('scroll', scrollListener);" + //$NON-NLS-1$
									"window.addEventListener('beforeunload', function(e){" + //$NON-NLS-1$
										"window.removeEventListener('scroll', scrollListener);" + //$NON-NLS-1$
										"delete window._browserSim_scrollListener;" + //$NON-NLS-1$
									"})" + //$NON-NLS-1$
								"})();"); //$NON-NLS-1$

			}
			
			public void changing(LocationEvent event) {
				if (skin.automaticallyHideAddressBar() && isAddressBarVisibleByDefault()) {
					skin.setAddressBarVisible(true);
				}
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
	}
	
	private void initObservers() {
		commonPreferencesObserver = new Observer() {
			@Override
			public void update(Observable o, Object refreshRequired) {
				setSelectedDeviceAsync((Boolean) refreshRequired);
			}
		};
		commonPreferences.addObserver(commonPreferencesObserver);
		specificPreferences.addObserver(new Observer() {
			public void update(Observable o, Object refreshRequired) {
				setSelectedDeviceAsync((Boolean) refreshRequired);
			}
		});
		
	}
	
	private boolean deviceUpdateRequired = false;
	private void setSelectedDeviceAsync(final Boolean refreshRequired) {
		deviceUpdateRequired = true;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (deviceUpdateRequired) {
					setSelectedDevice(refreshRequired);
					deviceUpdateRequired = false;
				}
			}
		});
	}

	protected void setSelectedDevice(Boolean refreshRequired) {
		final Device device = commonPreferences.getDevices().get(specificPreferences.getSelectedDeviceId());
		if (device == null) {
			skin.getShell().close();
		} else {
			Class<? extends BrowserSimSkin> newSkinClass = BrowserSimUtil.getSkinClass(device, specificPreferences.getUseSkins());
			boolean needToChangeSkin = newSkinClass != skin.getClass();
			String oldSkinUrl = null;
			if (needToChangeSkin) {
				oldSkinUrl = skin.getBrowser().getUrl();
				Point currentLocation = skin.getShell().getLocation();
				skin.getBrowser().removeProgressListener(progressListener);
				skin.getBrowser().getShell().dispose();
				initSkin(newSkinClass, currentLocation, parentShell);
				fireSkinChangeEvent();
			}
			setOrientation(specificPreferences.getOrientationAngle(), device);
			skin.getBrowser().setUserAgent(device.getUserAgent());
			
			processLiveReload(specificPreferences.isEnableLiveReload());
			processTouchEvents(specificPreferences.isEnableTouchEvents());
			
			if (needToChangeSkin) {
				if (oldSkinUrl != null && isUrlResettingNeededAfterSkinChange()) {
					skin.getBrowser().setUrl(oldSkinUrl); // skin (and browser instance) is changed
				}
			} else {
				if (!Boolean.FALSE.equals(refreshRequired)) {
					getBrowser().refresh(); // only user agent and size of the browser is changed and orientation is not changed
				}
			}

	
			skin.getShell().open();
		} 
	} 
	
	protected boolean isUrlResettingNeededAfterSkinChange() {
		return true; // JBIDE-14636
	}

	public void reinitSkin() {
		final Device device = commonPreferences.getDevices().get(specificPreferences.getSelectedDeviceId());
		Class<? extends BrowserSimSkin> newSkinClass = BrowserSimUtil.getSkinClass(device, specificPreferences.getUseSkins());
		String oldSkinUrl = skin.getBrowser().getUrl();
		Point currentLocation = skin.getShell().getLocation();
		skin.getBrowser().removeProgressListener(progressListener);
		skin.getBrowser().getShell().dispose();
		initSkin(newSkinClass, currentLocation, parentShell);
		setOrientation(specificPreferences.getOrientationAngle(), device);
		skin.getBrowser().setUserAgent(device.getUserAgent());
		skin.getBrowser().setUrl(oldSkinUrl); 
		skin.getShell().open();
	}
	
	private void processLiveReload(boolean isLiveReloadEnabled) {
		if (isLiveReloadEnabled) {
			if (liveReloadLocationAdapter == null) {
				initLiveReloadLocationAdapter();
			}
			skin.getBrowser().addLocationListener(liveReloadLocationAdapter);
		} else {
			if (liveReloadLocationAdapter != null) {
				skin.getBrowser().removeLocationListener(liveReloadLocationAdapter);
			}
		}
	}
	
	private void processTouchEvents(boolean isTouchEventsEnabled) {
		if (isTouchEventsEnabled) {
			if (touchEventsLocationAdapter == null) {
				initTouchEventsLocationAdapter();
			}
			skin.getBrowser().addLocationListener(touchEventsLocationAdapter);
		} else if (touchEventsLocationAdapter != null) {
			skin.getBrowser().removeLocationListener(touchEventsLocationAdapter);
		}
	}
	
	private void initLiveReloadLocationAdapter() {
		liveReloadLocationAdapter = new LocationAdapter() {
			@Override
			public void changed(LocationEvent event) {
				if (isLivereloadAvailable()) {
					IBrowser browser = (IBrowser) event.widget;
					browser.execute("if (!window.LiveReload) {" + //$NON-NLS-1$
										"window.addEventListener('load', function(){" + //$NON-NLS-1$
											"var e = document.createElement('script');" + //$NON-NLS-1$
											"e.type = 'text/javascript';" + //$NON-NLS-1$
											"e.async = 'true';" + //$NON-NLS-1$
											"e.src = 'http://localhost:" + specificPreferences.getLiveReloadPort() + "/livereload.js';" + //$NON-NLS-1$ //$NON-NLS-2$
											"document.head.appendChild(e);" + //$NON-NLS-1$
										"});" + //$NON-NLS-1$
									"}"); //$NON-NLS-1$
				} else {
					MessageBox warning = new MessageBox(parentShell, SWT.ICON_WARNING);
					warning.setText(Messages.WARNING);
					warning.setMessage(Messages.BrowserSim_LIVERELOAD_WARNING);
					warning.open();
					
					skin.getBrowser().removeLocationListener(liveReloadLocationAdapter);
					specificPreferences.setEnableLiveReload(false);	
				}
			}
		};
	}
	
	private boolean isLivereloadAvailable() {
		try {
			HttpURLConnection.setFollowRedirects(false);
			URL url = new URL("http://localhost:" + specificPreferences.getLiveReloadPort() + "/livereload.js"); //$NON-NLS-1$ //$NON-NLS-2$
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("HEAD"); //$NON-NLS-1$
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}
	
	private void initTouchEventsLocationAdapter() {
		touchEventsLocationAdapter = new LocationAdapter() {
			@Override
			public void changed(LocationEvent event) {
				TouchSupportLoader.initTouchEvents((IBrowser) event.widget);
			}
		};
	}

	private void initOrientation(int orientation) {
		getBrowser().execute("window.onorientationchange = null;" + "window.orientation = " + orientation + ";"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void setOrientation(int orientationAngle, Device device) {
		Point size = BrowserSimUtil.getSizeInDesktopPixels(device);
		Point browserSize;
		if (orientationAngle == SpecificPreferences.ORIENTATION_LANDSCAPE
				|| orientationAngle == SpecificPreferences.ORIENTATION_LANDSCAPE_INVERTED) {
			browserSize = new Point(size.y, size.x);
		} else {
			browserSize = size;
		}

		skin.setOrientationAndLocationAndSize(orientationAngle, currentLocation, browserSize, resizableSkinSizeAdvisor);
		getBrowser().execute("window.orientation = " + orientationAngle + ";" //$NON-NLS-1$ //$NON-NLS-2$
				+ "(function(){" //$NON-NLS-1$
				+ 		"var event = document.createEvent('Event');" //$NON-NLS-1$
				+ 		"event.initEvent('orientationchange', false, false);" // http://jsbin.com/azefow/6   https://developer.mozilla.org/en/DOM/document.createEvent //$NON-NLS-1$
				+ 		"window.dispatchEvent(event);" //$NON-NLS-1$
				+ 		"if (typeof window.onorientationchange === 'function') {" //$NON-NLS-1$
				+			"window.onorientationchange(event);" //$NON-NLS-1$
				+ 		"}" //$NON-NLS-1$
				+	"})();" //$NON-NLS-1$
		);
	}

	public IBrowser getBrowser() {
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
	
	public Device getCurrentDevice() {
		return commonPreferences.getDevices().get(specificPreferences.getSelectedDeviceId());
	}
	
	public static List<BrowserSim> getInstances() {
		return instances;
	}
	
	protected boolean isAddressBarVisibleByDefault() {
		return true;
	}

	/**
	 * {@link ControlHandler} factory method.
	 * 
	 * Override this method if you need a custom {@link ControlHandler}
	 */
	protected ControlHandler createControlHandler(IBrowser browser, String homeUrl, SpecificPreferences specificPreferences) {
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
	/**
	 * {@link SpecificPreferencesStorage} factory method.
	 * 
	 * Override this method if you need a custom {@link SpecificPreferencesStorage}
	 */
	protected SpecificPreferencesStorage getSpecificPreferencesStorage() {
		return BrowserSimSpecificPreferencesStorage.INSTANCE;
	}
	public SpecificPreferences getSpecificPreferences() {
		return specificPreferences;
	}
	
	/**
	 * Factory method. Override this method if you need to change appearance of back and forward buttons on UI.
	 * 
	 * @return {@link LocationListener} which controls back and forward buttons on UI
	 */
	protected LocationListener createNavButtonsListener() {
		return new LocationAdapter() {
			public void changed(LocationEvent event) {
				if (event.top) {
					IBrowser browser = (IBrowser) event.widget;
					skin.locationChanged(event.location, browser.isBackEnabled(), browser.isForwardEnabled());
				}
			}
		};
	}
}
