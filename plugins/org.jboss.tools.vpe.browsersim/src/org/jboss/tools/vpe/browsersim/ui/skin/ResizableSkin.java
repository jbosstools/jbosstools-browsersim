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
package org.jboss.tools.vpe.browsersim.ui.skin;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.vpe.browsersim.browser.IBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserSimBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;
import org.jboss.tools.vpe.browsersim.ui.ControlHandler;
import org.jboss.tools.vpe.browsersim.util.BrowserSimImageList;
import org.jboss.tools.vpe.browsersim.util.BrowserSimUtil;

public abstract class ResizableSkin implements BrowserSimSkin {
	protected DeviceComposite deviceComposite;
	protected ControlHandler controlHandler;
	
	private IBrowserSimBrowserFactory browserFactory;
	private Display display;
	private Shell shell;
	private IBrowser browser;
	private boolean vertical;
	
	private int[] visibleRegionHorizontal;
	private int[] visibleRegionVertical;
	private Point horizontalBorderSize;
	private Point verticalBorderSize;
	private Point maxScreenSize;
	private Composite innerBrowserContainer;

	public ResizableSkin(int[] visibleRegionHorizontal, int[] visibleRegionVertical, Point normalScreenSize,
			Point normalSkinSize) {
		super();
		this.visibleRegionHorizontal = visibleRegionHorizontal;
		this.visibleRegionVertical = visibleRegionVertical;
		this.maxScreenSize = normalScreenSize;
		this.verticalBorderSize = new Point(normalSkinSize.x - normalScreenSize.x, normalSkinSize.y - normalScreenSize.y);
		this.horizontalBorderSize = new Point(verticalBorderSize.y, verticalBorderSize.x);
	}
	
	@Override
	public void createControls(Display display, Point location, Shell parentShell) {
		this.display = display;
		if (parentShell == null) {
			shell = new Shell(display, SWT.NO_TRIM | SWT.NO_BACKGROUND);
		} else {
			shell = new Shell(parentShell,  SWT.NO_TRIM | SWT.NO_BACKGROUND );
		}
		shell.setLayout(new FillLayout());

//		if (location != null && display.getClientArea().contains(location)) {
//			shell.setLocation(location);
//		}
		
		vertical = true;
		deviceComposite = createDeviceComposite(shell, vertical);
		
		bindDeviceCompositeControls();
		Composite browserContainer = deviceComposite.getBrowserContainer();
		browserContainer.setLayout(new FillLayout());
		innerBrowserContainer = new Composite(browserContainer, SWT.NONE);
		innerBrowserContainer.setLayout(new FillLayout());
		browser = browserFactory.createBrowser(innerBrowserContainer, SWT.NONE);
		
		shell.setSize(/*shell.computeSize(SWT.DEFAULT, SWT.DEFAULT)*/ 384, 727);
		setShellRegion();		
	}
	
	protected void bindDeviceCompositeControls() {
		deviceComposite.getBackButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.goBack();
				}
			}
		});
		if (deviceComposite.getForwardButtonComposite() != null) {
			deviceComposite.getForwardButtonComposite().addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					if (e.button == 1) {
						controlHandler.goForward();
					}
				}
			});
		}
		deviceComposite.getStopButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.stop();
				}
			}
		});
		deviceComposite.getRefreshButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.refresh();
				}
			}
		});
		if (deviceComposite.getHomeButtonComposite() != null) {
			deviceComposite.getHomeButtonComposite().addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					if (e.button == 1) {
						controlHandler.goHome();
					}
				}
			});
		}
		deviceComposite.getUrlText().addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				controlHandler.goToAddress(((Text)e.widget).getText());
			}
		});
		TextSelectionUtil.addSelectTextOnFocusListener(deviceComposite.getUrlText());
		
		Listener moveListener = new Listener() {
			Point origin;
			Point shellOrigin;

			public void handleEvent(Event e) {
				if (e.widget instanceof Composite && ((e.stateMask & SWT.BUTTON1) != 0 || e.button == 1)) { // left mouse Composite
					Composite composite = (Composite) e.widget;
					Shell shell = composite.getShell();
					
					switch (e.type) {
					case SWT.MouseDown:
						origin = e.display.map(shell, null, e.x, e.y);
						shellOrigin = shell.getLocation();
						break;
					case SWT.MouseUp:
						origin = null;
						break;
					case SWT.MouseMove:
						if (origin != null) {
							Point p = e.display.map(shell, null, e.x, e.y);
							Point location = new Point(shellOrigin.x + p.x - origin.x, shellOrigin.y + p.y - origin.y);
							shell.setLocation(location.x, location.y);
						}
						break;
					}
				}
			}
		};
		deviceComposite.addListener(SWT.MouseDown, moveListener);
		deviceComposite.addListener(SWT.MouseUp, moveListener);
		deviceComposite.addListener(SWT.MouseMove, moveListener);
		
		final BrowserSimImageList imageList = new BrowserSimImageList(deviceComposite);
		Listener rotationHotSpotListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				Control contol = event.display.getCursorControl();
				Point displayPoint = event.display.getCursorLocation();
				Image rotateImage = imageList.getImage("cursors/rotate.gif"); //$NON-NLS-1$
				if (deviceComposite.isDeviceCorner(displayPoint) && deviceComposite.isDeviceBody(contol)) {
					deviceComposite.setCursor(new Cursor(Display.getCurrent(), rotateImage.getImageData(), 0, 0));
					BrowserSimUtil.addDisposeListener(deviceComposite, deviceComposite.getCursor());
				} else {
					deviceComposite.setCursor(null);					
				}
			}
		};
		deviceComposite.addListener(SWT.MouseMove, rotationHotSpotListener); 
		deviceComposite.addListener(SWT.MouseExit, rotationHotSpotListener);
		
		Listener rotationHotSpotClickListener  = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.button == 1) {
					Control contol = event.display.getCursorControl();
					Point displayPoint = event.display.getCursorLocation();
					if (deviceComposite.isDeviceCorner(displayPoint) && deviceComposite.isDeviceBody(contol)) {
						controlHandler.rotate(false);
					}
				}
			}
		};
		
		deviceComposite.addListener(SWT.MouseDown, rotationHotSpotClickListener);
	}
	
	private void setShellRegion() {
		int[] normalRegion = getNormalRegion(vertical);

		Point normalRegionSize = new Point(0, 0);		
		for (int i = 0; i < normalRegion.length; i += 2) {
			if (normalRegionSize.x < normalRegion[i]) {
				normalRegionSize.x = normalRegion[i];
			}
			if (normalRegionSize.y < normalRegion[i + 1]) {
				normalRegionSize.y = normalRegion[i + 1];
			}
		}
		
		Point normalRegionCenter = new Point(normalRegionSize.x / 2, normalRegionSize.y / 2);
		Point shellSize = shell.getSize();
		Point regionIncrement = new Point(shellSize.x - normalRegionSize.x, shellSize.y - normalRegionSize.y);
		
		int[] shellRegion = Arrays.copyOf(normalRegion, normalRegion.length);
		for (int i = 0; i < shellRegion.length; i += 2) {
			if (shellRegion[i] > normalRegionCenter.x) {
				shellRegion[i] += regionIncrement.x;
			}
			if (shellRegion[i + 1] > normalRegionCenter.y) {
				shellRegion[i + 1] += regionIncrement.y;
			}
		}
		final Region region = new Region();
		region.add(shellRegion);
		shell.setRegion(region);
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				region.dispose();
			}
		});
		////////////////////////////////
//		shell.setRegion(null);
//		display.asyncExec(new Runnable() {
//			
//			@Override
//			public void run() {
//				Region region = new Region();		
////		shellRegion.add(AppleIPhone3Skin.VISIBLE_REGION[0]);
//				GC gc = new GC(shell);
//				final Image screenshot = new Image(display, shell.getBounds());
//				gc.copyArea(screenshot, 0, 0);
//				gc.dispose();
//				ImageData imageData = screenshot.getImageData();
//				region.add(0, 0, imageData.width, imageData.height);
//				int firstPixel = imageData.getPixel(0, 0);
//				for (int i = 0; i < imageData.height; i++) {
//					for (int j = 0; j < imageData.width; j++) {
//						if (imageData.getPixel(j, i) == firstPixel) {
//							region.subtract(j, i, 1, 1);
//						}
//					}
//				}
//				screenshot.dispose();
//				shell.setRegion(region);
//				region.dispose();
//			}
//		});
	}
	
	@Override
	public void setOrientationAndLocationAndSize(int orientation, Point location, Point browserSize, ResizableSkinSizeAdvisor sizeAdvisor) {
		vertical = (orientation == SpecificPreferences.ORIENTATION_PORTRAIT || orientation == SpecificPreferences.ORIENTATION_PORTRAIT_INVERTED);
		String urlTextText = deviceComposite.getUrlText().getText();
		String pageTitle = deviceComposite.getPageTitleStyledText() != null ? deviceComposite.getPageTitleStyledText().getText() : ""; //$NON-NLS-1$
		boolean backEnabled = deviceComposite.getBackButtonComposite().getEnabled();
		boolean forwardEnabled = false;
		if (deviceComposite.getForwardButtonComposite() != null) {
			forwardEnabled = deviceComposite.getForwardButtonComposite().getEnabled();
		}
		boolean navBarVisible = deviceComposite.isNavBarCompositeVisible();
		Menu contextMenu = deviceComposite.getMenu();
		deviceComposite.setMenu(null);
		
		DeviceComposite oldDeviceComposite = deviceComposite;
		deviceComposite = createDeviceComposite(shell, vertical);
		bindDeviceCompositeControls();
		Composite browserContainer = deviceComposite.getBrowserContainer();
		browserContainer.setLayout(new FillLayout());
		innerBrowserContainer.setParent(browserContainer);
		oldDeviceComposite.dispose();
		Point bordersSize = getBordersSize(vertical);
		int shellWidthHint;
		if (browserSize.x == SWT.DEFAULT) {
			shellWidthHint = SWT.DEFAULT;
		} else {
			shellWidthHint = bordersSize.x + browserSize.x;
		}
		int shellHeightHint;
		if (browserSize.y == SWT.DEFAULT) {
			shellHeightHint = SWT.DEFAULT;
		} else {
			shellHeightHint = bordersSize.y + browserSize.y;
		}

		Point prefferedShellSize = shell.computeSize(shellWidthHint, shellHeightHint);
		Point shellSize = sizeAdvisor.checkWindowSize(orientation, browserSize, prefferedShellSize);
		shell.setSize(shellSize);
		BrowserSimUtil.setShellLocation(shell, shellSize, location);
		
		shell.layout(true);
		setShellRegion();
		
		deviceComposite.getUrlText().setText(urlTextText);
		if (deviceComposite.getPageTitleStyledText() != null) {
			deviceComposite.getPageTitleStyledText().setText(pageTitle);
		}
		deviceComposite.getBackButtonComposite().setEnabled(backEnabled);
		if (deviceComposite.getForwardButtonComposite() != null) {
			deviceComposite.getForwardButtonComposite().setEnabled(forwardEnabled);
		}
		deviceComposite.setNavBarCompositeVisible(navBarVisible);
		deviceComposite.setMenu(contextMenu);
	}
	
	
	
	@Override
	public void statusTextChanged(String newStatusText) {
		// not supported		
	}
	
	@Override
	public void progressChanged(int percents) {
		ProgressBar progressBar = deviceComposite.getProgressBar();
		if (percents > 0) {
			progressBar.setVisible(true);
			progressBar.setSelection(percents);
		} else {
			progressBar.setVisible(false);
			progressBar.setSelection(0);
		}
	}
	
	@Override
	public void locationChanged(String newLocation, boolean backEnabled,
			boolean forwardEnabled) {
		deviceComposite.getUrlText().setText(newLocation);
		deviceComposite.getBackButtonComposite().setEnabled(backEnabled);
		if (deviceComposite.getForwardButtonComposite() != null) {
			deviceComposite.getForwardButtonComposite().setEnabled(forwardEnabled);
		}
	}
	
	@Override
	public void pageTitleChanged(String newTitle) {
		if (deviceComposite.getPageTitleStyledText() != null) {
			deviceComposite.getPageTitleStyledText().setText(newTitle);
		}
	}

	private Point getBordersSize(boolean vertical) {
		return vertical ? verticalBorderSize : horizontalBorderSize;
	}
	
	protected abstract DeviceComposite createDeviceComposite(Composite parent, boolean vertical);
		
	private int[] getNormalRegion(boolean vertical) {
		return vertical ? visibleRegionVertical : visibleRegionHorizontal;
	}
	
	@Override
	public Point getMinimalScreenSize() {
		return maxScreenSize;
	}
	
	@Override
	public IBrowser getBrowser() {
		return browser;
	}

	@Override
	public Shell getShell() {
		return shell;
	}

	@Override
	public Menu getMenuBar() {
		return display.getMenuBar();
	}

	@Override
	public void setControlHandler(ControlHandler controlHandler) {
		this.controlHandler = controlHandler;
	}
	
	@Override
	public void setAddressBarVisible(boolean visible) {
		deviceComposite.setNavBarCompositeVisible(visible);
	}

	@Override
	public void setContextMenu(Menu contextMenu) {
		deviceComposite.setMenu(contextMenu);
	}
	
	@Override
	public void setBrowserFactory(IBrowserSimBrowserFactory browserFactory) {
		this.browserFactory = browserFactory;		
	}
}
