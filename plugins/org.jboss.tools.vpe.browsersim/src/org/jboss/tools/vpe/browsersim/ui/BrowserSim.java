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
package org.jboss.tools.vpe.browsersim.ui;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;

import org.eclipse.jetty.server.Server;
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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.BrowserSimArgs;
import org.jboss.tools.vpe.browsersim.BrowserSimLogger;
import org.jboss.tools.vpe.browsersim.browser.ExtendedOpenWindowListener;
import org.jboss.tools.vpe.browsersim.browser.ExtendedWindowEvent;
import org.jboss.tools.vpe.browsersim.browser.IBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserFunction;
import org.jboss.tools.vpe.browsersim.browser.IDisposable;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;
import org.jboss.tools.vpe.browsersim.browser.javafx.JavaFXBrowser;
import org.jboss.tools.vpe.browsersim.devtools.DevToolsDebuggerServer;
import org.jboss.tools.vpe.browsersim.js.log.ConsoleLogConstants;
import org.jboss.tools.vpe.browsersim.js.log.JsLogFunction;
import org.jboss.tools.vpe.browsersim.js.log.MessageType;
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
import org.jboss.tools.vpe.browsersim.util.PreferencesUtil;
import org.jboss.tools.vpe.browsersim.util.ReflectionUtil;

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
			commonPreferences = (CommonPreferences) CommonPreferencesStorage.INSTANCE.load(PreferencesUtil.getConfigFolderPath());
			if (commonPreferences == null) {
				commonPreferences = (CommonPreferences) CommonPreferencesStorage.INSTANCE.loadDefault();
			}
		}
	}
	
	public BrowserSim(String homeUrl, Shell parent) {
		this.homeUrl = homeUrl;
		parentShell = parent;
	}
	
	public void open(boolean isJavaFxAvailable, boolean isWebKitAvailable) {
		SpecificPreferences sp = (SpecificPreferences) getSpecificPreferencesStorage().load(PreferencesUtil.getConfigFolderPath());
		if (sp == null) {
			sp = (SpecificPreferences) getSpecificPreferencesStorage().loadDefault();
		}
		
		if (BrowserSimArgs.standalone) {
			sp.setJavaFx(false);
		} else {
			if (!isWebKitAvailable) {
				if (isJavaFxAvailable) {
					sp.setJavaFx(true);
				}
			}
			if (!isJavaFxAvailable) {
				sp.setJavaFx(false);
			}
		}

		open(sp, null);
	}

	public void open(SpecificPreferences sp, String url) {
		if (url == null) {
			url = homeUrl;
		}
		
		specificPreferences = sp;
		enableLivereloadIfAvailable();
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
		
		skin.createControls(display, location, parentShell, specificPreferences.isJavaFx());
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
		
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				try {
					if (!BrowserSimArgs.standalone) {
						ReflectionUtil.call("org.jboss.tools.vpe.browsersim.devtools.DevToolsDebuggerServer", "stopDebugServer"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} catch (Exception e) {
					BrowserSimLogger.logError(e.getMessage(), e);
				}
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
		BrowserSimUtil.setShellAttributes(shell);
		
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
					FireBugLiteLoader.processFireBugPopUp(event, skin, specificPreferences.isJavaFx());
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
		
		overrideJsConsole(browser); // JBIDE-15932		
	}
	
	// JBIDE-15932 need to display console logs especially during startup
	private void overrideJsConsole(final IBrowser browser) {	
		browser.addLocationListener(new LocationAdapter() {  
			@Override
			public void changed(LocationEvent e) {
				if (browser instanceof JavaFXBrowser && BrowserSimUtil.isJavaFx8Available()) {
					 Platform.runLater(new Runnable() {
						@Override
						public void run() {
							createLogFunctions(browser); 
							overrideJsLogFunctions(browser);
						}
					});
				} else {
					createLogFunctions(browser); 
					overrideJsLogFunctions(browser);
				}
			}
		});
	}

	private void createLogFunctions(final IBrowser browser) {
		if (browser != null && !browser.isDisposed()) { 
			browser.registerBrowserFunction(ConsoleLogConstants.BROSERSIM_CONSOLE_LOG, new JsLogFunction(browser,  MessageType.LOG)); 
			browser.registerBrowserFunction(ConsoleLogConstants.BROSERSIM_CONSOLE_INFO, new JsLogFunction(browser,  MessageType.INFO));  
			browser.registerBrowserFunction(ConsoleLogConstants.BROSERSIM_CONSOLE_WARN, new JsLogFunction(browser,  MessageType.WARN));  
			browser.registerBrowserFunction(ConsoleLogConstants.BROSERSIM_CONSOLE_ERROR, new JsLogFunction(browser,  MessageType.ERROR));  
		}
	}
	
	private void overrideJsLogFunctions(final IBrowser browser) {
		if (browser != null && !browser.isDisposed()) {
			browser.execute("(function() {" //$NON-NLS-1$
					+ "if (window.console) {" //$NON-NLS-1$
					//  Adding BrowserFunction to the process of logging
					+ 	"var originalConsole = window.console;" //$NON-NLS-1$
					+ 	"console = {" //$NON-NLS-1$
					+		"log: function(message) {" //$NON-NLS-1$
					+			"originalConsole.log(message);" //$NON-NLS-1$
					+			ConsoleLogConstants.BROSERSIM_CONSOLE_LOG + "(message);" //$NON-NLS-1$
					+		"}," //$NON-NLS-1$
					+		"info: function(message) {" //$NON-NLS-1$
					+			"originalConsole.info(message);" //$NON-NLS-1$
					+			ConsoleLogConstants.BROSERSIM_CONSOLE_INFO + "(message);" //$NON-NLS-1$
					+		"},"  //$NON-NLS-1$
					+		"warn: function(message) {" //$NON-NLS-1$
					+			"originalConsole.warn(message);" //$NON-NLS-1$
					+			ConsoleLogConstants.BROSERSIM_CONSOLE_WARN + "(message);" //$NON-NLS-1$
					+		"},"   //$NON-NLS-1$
					+		"error: function(message) {" //$NON-NLS-1$
					+			"originalConsole.error(message);" //$NON-NLS-1$
					+			ConsoleLogConstants.BROSERSIM_CONSOLE_ERROR + "(message);" //$NON-NLS-1$
					+		"},"   //$NON-NLS-1$
					+		"debug: function(message) {" //$NON-NLS-1$ 
					+			"console.log(message);" //$NON-NLS-1$ do the same as for 'console.log'
					+		"}"   //$NON-NLS-1$
					+ 	"};" //$NON-NLS-1$
	
					// Overriding window.onerror 
					+	"window.onerror = function(msg, url, lineNumber) {" //$NON-NLS-1$
					+		"console.error(msg + ' on line ' + lineNumber + ' for ' + url);"  //$NON-NLS-1$
					+	"}" //$NON-NLS-1$
					+ "}" //$NON-NLS-1$
			+ "})();");	 //$NON-NLS-1$
		}
	}

	private void enableLivereloadIfAvailable() {
	    if (isLivereloadAvailable()) {
            specificPreferences.setEnableLiveReload(true); 
        } else {
            specificPreferences.setEnableLiveReload(false);
        }
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
			final Class<? extends BrowserSimSkin> newSkinClass = BrowserSimUtil.getSkinClass(device, specificPreferences.getUseSkins());
			boolean needToChangeSkin = newSkinClass != skin.getClass(); 
													
			boolean needToChangeEngine = (skin.getBrowser() instanceof JavaFXBrowser && !specificPreferences.isJavaFx())
									  || (!(skin.getBrowser() instanceof JavaFXBrowser) && specificPreferences.isJavaFx());
			if (needToChangeSkin || needToChangeEngine) {
				final String oldSkinUrl = skin.getBrowser().getUrl();
				final Point currentLocation = skin.getShell().getLocation();
				skin.getBrowser().removeProgressListener(progressListener);
				skin.getBrowser().getShell().dispose();
				
				if (specificPreferences.isJavaFx()) {
					Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							changeSkinOrEngine(newSkinClass, device, currentLocation, oldSkinUrl);
						}
					});
				} else {
					changeSkinOrEngine(newSkinClass, device, currentLocation, oldSkinUrl);
				}
				
			} else {
				setOrientation(specificPreferences.getOrientationAngle(), device);
				skin.getBrowser().setUserAgent(device.getUserAgent());
				
				processLiveReload(specificPreferences.isEnableLiveReload());
				processTouchEvents(specificPreferences.isEnableTouchEvents());
				
				if (!Boolean.FALSE.equals(refreshRequired)) {
					getBrowser().refresh(); // only user agent and size of the browser is changed and orientation is not changed
				}
				
				skin.getShell().open();
			}

		} 
	} 
	
	private void changeSkinOrEngine(Class<? extends BrowserSimSkin> newSkinClass, Device device,  Point currentLocation, String oldSkinUrl) {
		initSkin(newSkinClass, currentLocation, parentShell);
		fireSkinChangeEvent();
		
		if (skin.getBrowser() instanceof JavaFXBrowser && !Server.STARTED.equals(DevToolsDebuggerServer.getServerState())) {
            try {
				DevToolsDebuggerServer.startDebugServer(((JavaFXBrowser)skin.getBrowser()).getDebugger());
			} catch (Exception e) {
				BrowserSimLogger.logError(e.getMessage(), e);
			}
        }
		
		setOrientation(specificPreferences.getOrientationAngle(), device);
		skin.getBrowser().setUserAgent(device.getUserAgent());
		
		processLiveReload(specificPreferences.isEnableLiveReload());
		processTouchEvents(specificPreferences.isEnableTouchEvents());
		
		if (oldSkinUrl != null && isUrlResettingNeededAfterSkinChange()) {
			skin.getBrowser().setUrl(oldSkinUrl); // skin (and browser instance) is changed
		}
		
		skin.getShell().open();
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
		processLiveReload(specificPreferences.isEnableLiveReload());
	}
	
	private void processLiveReload(boolean isLiveReloadEnabled) {
		if (isLiveReloadEnabled) {
			if (specificPreferences.isJavaFx() && BrowserSimUtil.isJavaFx7Available()) {
				showLivereloadError(Messages.ManageDevicesDialog_LIVE_RELOAD_UNAVAILABLE);
				specificPreferences.setEnableLiveReload(false);
			} else if (isLivereloadAvailable()) {
				if (liveReloadLocationAdapter == null) {
					initLiveReloadLocationAdapter();
				}
				skin.getBrowser().addLocationListener(liveReloadLocationAdapter);
			} else {
				showLivereloadError(Messages.BrowserSim_LIVERELOAD_WARNING);
				specificPreferences.setEnableLiveReload(false);
			}
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
			public void changed(final LocationEvent event) {
				if (isLivereloadAvailable()) {
					if (specificPreferences.isJavaFx()) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								processLiveReloadEvent(event);
							}
						});
					} else {
						processLiveReloadEvent(event);
					}
				} else {
					showLivereloadError(Messages.BrowserSim_LIVERELOAD_WARNING);
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
			con.setConnectTimeout(1000);
			con.setRequestMethod("HEAD"); //$NON-NLS-1$
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}

	private void showLivereloadError(String message) {
		MessageBox warning = new MessageBox(skin.getShell(), SWT.ICON_WARNING);
		warning.setText(Messages.WARNING);
		warning.setMessage(message);
		warning.open();
	}
	
	private void processLiveReloadEvent(final LocationEvent event) {
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
	}
	
	private void initTouchEventsLocationAdapter() {
		touchEventsLocationAdapter = new LocationAdapter() {
			@Override
			public void changed(final LocationEvent event) {
				if (specificPreferences.isJavaFx()) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							TouchSupportLoader.initTouchEvents((IBrowser) event.widget);
						}
					});
				} else {
					TouchSupportLoader.initTouchEvents((IBrowser) event.widget);
				}
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
