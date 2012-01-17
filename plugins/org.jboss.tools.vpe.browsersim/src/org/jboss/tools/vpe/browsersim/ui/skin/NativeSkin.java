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
package org.jboss.tools.vpe.browsersim.ui.skin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserSimBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.Device;
import org.jboss.tools.vpe.browsersim.ui.ControlHandler;
import org.jboss.tools.vpe.browsersim.ui.Messages;
import org.jboss.tools.vpe.browsersim.util.ResourcesUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class NativeSkin implements BrowserSimSkin {
	private ProgressBar progressBar;
	private Label statusLabel;
	private Text locationText;
	private Shell shell;
	private BrowserSimBrowser browser;
	private Menu menuBar;
	private ControlHandler controlHandler;
	private ToolItem itemBack;
	private ToolItem itemForward;
	private ToolItem itemStop;
	private ToolItem itemRefresh;
	private ToolItem itemGo;
	private ToolItem itemRotateCounterclockwise;
	private ToolItem itemRotateClockwise;
	private Composite skinComposite;
	private IBrowserSimBrowserFactory browserFactory;
	
	public NativeSkin() {
	}

	@Override
	public void setBrowserFactory(IBrowserSimBrowserFactory browserFactory) {
		this.browserFactory = browserFactory;
	};
	
	@Override
	public void createControls(Display display) {
		shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL | SWT.HORIZONTAL));
		
		menuBar = display.getMenuBar();
		if (menuBar == null) {
			menuBar = new Menu(shell, SWT.BAR);
			shell.setMenuBar(menuBar);
		}
		
		skinComposite = new Composite(shell, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		skinComposite.setLayout(gridLayout);
		
		ToolBar toolbar = createControlBar();
		GridData toolbarData = new GridData();
		toolbarData.horizontalSpan = 3;
		toolbar.setLayoutData(toolbarData);

		Label labelAddress = new Label(skinComposite, SWT.NONE);
		labelAddress.setText(Messages.BrowserSim_ADDRESS);
		
		locationText = new Text(skinComposite, SWT.BORDER);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		data.widthHint = 0;
		locationText.setLayoutData(data);
		
		browser = browserFactory.createBrowser(skinComposite, SWT.NONE);
		GridData browserData = new GridData();
		browserData.horizontalAlignment = GridData.FILL;
		browserData.verticalAlignment = GridData.FILL;
		browserData.horizontalSpan = 3;
		browserData.grabExcessHorizontalSpace = true;
		browserData.grabExcessVerticalSpace = true;
		browser.setLayoutData(browserData);
		
		statusLabel = new Label(skinComposite, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.widthHint = 0;
		statusLabel.setLayoutData(data);

		progressBar = new ProgressBar(skinComposite, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		progressBar.setLayoutData(data);
		
		addControlListeners();

		shell.open();
	}
		
	private void addControlListeners() {
		locationText.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				controlHandler.goToAddress(locationText.getText());
			}
		});
		
		itemBack.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				controlHandler.goBack();
			}
		});
		
		itemForward.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				controlHandler.goForward();
			}
		});
		
		itemStop.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				controlHandler.stop();
			}
		});
		
		itemRefresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				controlHandler.refresh();
			}
		});
		
		itemRotateCounterclockwise.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				controlHandler.rotate(true);
			}
		});
		
		itemRotateClockwise.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				controlHandler.rotate(false);
			}
		});
		
		itemGo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				controlHandler.goToAddress(locationText.getText());
			}
		});
	}
	
	@Override
	public void setControlHandler(ControlHandler controlHandler) {
		this.controlHandler = controlHandler;
	}
	
	private ToolBar createControlBar() {
		ToolBar toolbar = new ToolBar(skinComposite, SWT.NONE);
		
		itemBack = new ToolItem(toolbar, SWT.PUSH);
//		itemBack.setText("Back");
		itemForward = new ToolItem(toolbar, SWT.PUSH);
//		itemForward.setText("Forward");
		itemStop = new ToolItem(toolbar, SWT.PUSH);
//		itemStop.setText("Stop");
		itemRefresh = new ToolItem(toolbar, SWT.PUSH);
//		itemRefresh.setText("Refresh");
		itemGo = new ToolItem(toolbar, SWT.PUSH);
//		itemGo.setText("Go");
		itemRotateCounterclockwise = new ToolItem(toolbar, SWT.PUSH);
//		itemGo.setText("Rotate Counterclockwise");
		itemRotateClockwise = new ToolItem(toolbar, SWT.PUSH);
//		itemGo.setText("Rotate Clockwise");
		
		Display display = skinComposite.getDisplay();
		final Image imageBack = new Image(display, ResourcesUtil.getResourceAsStream("native_skin/nav_backward.gif")); //$NON-NLS-1$
		final Image imageForward = new Image(display, ResourcesUtil.getResourceAsStream("native_skin/nav_forward.gif")); //$NON-NLS-1$
		final Image imageStop = new Image(display, ResourcesUtil.getResourceAsStream("native_skin/nav_stop.gif")); //$NON-NLS-1$
		final Image imageRefresh = new Image(display, ResourcesUtil.getResourceAsStream("native_skin/nav_refresh.gif")); //$NON-NLS-1$
		final Image imageGo = new Image(display, ResourcesUtil.getResourceAsStream("native_skin/nav_go.gif")); //$NON-NLS-1$
		final Image imageRotateClockwise = new Image(display, ResourcesUtil.getResourceAsStream("native_skin/rotate_clockwise.png")); //$NON-NLS-1$
		final Image imageRotateCounterclockwise = new Image(display, ResourcesUtil.getResourceAsStream("native_skin/rotate_counterclockwise.png")); //$NON-NLS-1$		
		
		itemBack.setImage(imageBack);
		itemForward.setImage(imageForward);
		itemStop.setImage(imageStop);
		itemRefresh.setImage(imageRefresh);
		itemGo.setImage(imageGo);
		itemRotateClockwise.setImage(imageRotateClockwise);
		itemRotateCounterclockwise.setImage(imageRotateCounterclockwise);
		
		skinComposite.addDisposeListener(new DisposeListener() {
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
	
	public void setBrowserSize(int width, int height) {
		GridData data = (GridData) browser.getLayoutData();
		
		Rectangle clientArea = getMonitorClientArea();
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
	
	private Rectangle getMonitorClientArea() {
		Rectangle clientArea = skinComposite.getMonitor().getClientArea();

		/* on Linux returned monitor client area may be bigger
		 * than the monitor bounds when multiple monitors are used.
		 * The following code fixes this */
		Rectangle bounds = skinComposite.getMonitor().getBounds();
		clientArea.width = Math.min(clientArea.width, bounds.width);
		clientArea.height = Math.min(clientArea.height, bounds.height);
		
		return clientArea;
	}
	
	@Override
	public BrowserSimBrowser getBrowser() {
		return browser;
	}
	
	@Override
	public Shell getShell() {
		return shell;
	}
	
	@Override
	public Menu getMenuBar() {
		return menuBar; 
	}

	@Override
	public void locationChanged(String location, boolean backEnabled, boolean forwardEnabled) {
		locationText.setText(location);
		itemBack.setEnabled(backEnabled);
		itemForward.setEnabled(forwardEnabled);
	}

	@Override
	public void progressChanged(int percents) {
		if (percents > 0) {
			progressBar.setEnabled(true);
			progressBar.setSelection(percents);
		} else {
			progressBar.setSelection(0);
			progressBar.setEnabled(false);
		}
		
	}

	@Override
	public void statusTextChanged(String statusText) {
		statusLabel.setText(statusText);
	}

	@Override
	public void setOrientation(int orientation) {
		// do nothing, only browser size should change it should
		// be done by calling setBrowserSize(int width, int height)
	}
}
