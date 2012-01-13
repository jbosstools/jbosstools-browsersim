/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
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
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserSimBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.DeviceOrientation;
import org.jboss.tools.vpe.browsersim.ui.ControlHandler;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class AppleIPhone3Skin implements BrowserSimSkin {
	private static final String[] SHELL_IMAGE = {"iphone_skin/iPhone3.png", "iphone_skin/iPhone3-horizontal.png"};
	private static final String[] ADDRESS_BAR_IMAGE = {"iphone_skin/address-bar.png", "iphone_skin/address-bar-horizontal.png"};
	private static final String[] SHOW_ADDRESS_BAR_IMAGE = {"iphone_skin/show-address-bar.png", "iphone_skin/show-address-bar-horizontal.png"};
	private static final int VERTICAL = 0;
	private static final int HORIZONTAL = 1;
	private static final Point[] FORWARD_LOCATION = {new Point(115, 571), new Point(248, 324)};
	private static final Rectangle[] BROWSER_RECTANGLE = {new Rectangle(33, 206, 320, 355), new Rectangle(125, 111, 480, 208)};
	private static final Rectangle[] BROWSER_RECTANGLE_MAXIMIZED = {new Rectangle(33, 145, 320, 416), new Rectangle(125, 50, 480, 269)};
	private static final int URL_BACKGROUND_COLOR = SWT.COLOR_WHITE;
	private static final Point[] BACK_LOCATION = {new Point(52, 571), new Point(145, 324)};
	private static final Point[] SHOW_ADDRESS_BAR_LOCATION = {new Point(33, 126), new Point(125, 31)};
	
	private static final Point[] ADDRESS_BAR_LOCATION = {new Point(33, 145), new Point(125, 50)};

	// the following four coordinates are related to ADDRESS_BAR_LOCATION
	private static final Rectangle[] URL_RECTANGLE = {new Rectangle(12, 33, 245, 17), new Rectangle(11, 33, 401, 17)};
	private static final Rectangle[] PROGRESS_BAR_RECTANGLE = {new Rectangle(7, 55, 277, 5), new Rectangle(7, 55, 430, 5)};
	private static final Point[] REFRESH_LOCATION = {new Point(290, 28), new Point(447, 28)};
	private static final Point[] STOP_LOCATION = {new Point(261, 33), new Point(412, 33)};
	
	private static final int[][] VISIBLE_REGION = {{
		62, 3, 257, 3, 258, 2, 258, 0, 320, 0, 320, 2, 321, 3, 330, 3, 331, 4, 334, 4, 335, 5, 338, 5, 339, 6, 341, 6,
		342, 7, 344, 7, 345, 8, 347, 8, 348, 9, 349, 9, 350, 10, 351, 10, 353, 12, 354, 12, 355, 13, 356, 13, 359, 16,
		360, 16, 368, 24, 368, 25, 371, 28, 371, 29, 373, 31, 373, 32, 374, 33, 374, 34, 375, 35, 375, 36, 376, 37,
		376, 38, 377, 39, 377, 41, 378, 42, 378, 43, 379, 44, 379, 46, 380, 47, 380, 50, 381, 51, 381, 54, 382, 55, 382,
		60, 383, 61, 383, 668, 382, 669, 382, 673, 381, 674, 381, 678, 380, 679, 380, 682, 379, 683, 379, 685, 378, 686, 378,
		687, 377, 688, 377, 690, 376, 691, 376, 692, 375, 693, 375, 694, 374, 695, 374, 696, 373, 697, 373, 698, 371, 700, 371,
		701, 368, 704, 368, 705, 360, 713, 359, 713, 356, 716, 355, 716, 354, 717, 353, 717, 351, 719, 350, 719, 349, 720, 348,
		720, 347, 721, 345, 721, 344, 722, 341, 722, 340, 723, 337, 723, 336, 724, 334, 724, 333, 725, 329, 725, 328, 726, 59,
		726, 58, 725, 54, 725, 53, 724, 47, 724, 46, 723, 44, 723, 43, 722, 41, 722, 40, 721, 39, 721, 38, 720, 37, 720, 36, 719,
		35, 719, 34, 718, 33, 718, 31, 716, 30, 716, 28, 714, 27, 714, 17, 704, 17, 703, 14, 700, 14, 699, 13, 698, 13, 697, 12,
		696, 12, 695, 11, 694, 11, 693, 10, 692, 10, 691, 9, 690, 9, 689, 8, 688, 8, 686, 7, 685, 7, 683, 6, 682, 6, 680, 5, 679,
		5, 676, 4, 675, 4, 670, 3, 669, 3, 660, 2, 659, 2, 258, 1, 257, 1, 256, 0, 255, 0, 220, 1, 219, 1, 212, 2, 211, 2, 177, 1,
		176, 1, 169, 0, 168, 0, 132, 2, 130, 2, 108, 3, 107, 2, 106, 1, 106, 1, 74, 2, 73, 2, 67, 3, 66, 3, 59, 4, 58, 4, 54, 5, 53,
		5, 50, 6, 49, 6, 47, 7, 46, 7, 44, 8, 43, 8, 41, 9, 40, 9, 39, 10, 38, 10, 37, 11, 36, 11, 35, 12, 34, 12, 33, 13, 32, 13, 31,
		14, 30, 14, 29, 17, 26, 17, 25, 26, 16, 27, 16, 30, 13, 31, 13, 33, 11, 34, 11, 35, 10, 36, 10, 37, 9, 38, 9, 39, 8, 41, 8,
		42, 7, 43, 7, 44, 6, 47, 6, 48, 5, 50, 5, 51, 4, 61, 4
	},{
		100, 0, 668, 0, 669, 1, 673, 1, 674, 2, 678, 2, 679, 3, 682, 3, 683, 4, 685, 4, 686, 5, 687, 5, 688, 6, 690, 6, 691, 7, 692, 7, 693, 8,
		694, 8, 695, 9, 696, 9, 697, 10, 698, 10, 700, 12, 701, 12, 704, 15, 705, 15, 713, 23, 713, 24, 716, 27, 716, 28, 717, 29, 717,
		30, 719, 32, 719, 33, 720, 34, 720, 35, 721, 36, 721, 38, 722, 39, 722, 42, 723, 43, 723, 46, 724, 47, 724, 49, 725, 50, 725, 54,
		726, 55, 726, 324, 725, 325, 725, 329, 724, 330, 724, 336, 723, 337, 723, 339, 722, 340, 722, 342, 721, 343, 721, 344, 720, 345,
		720, 346, 719, 347, 719, 348, 718, 349, 718, 350, 716, 352, 716, 353, 714, 355, 714, 356, 704, 366, 703, 366, 700, 369, 699, 369,
		698, 370, 697, 370, 696, 371, 695, 371, 694, 372, 693, 372, 692, 373, 691, 373, 690, 374, 689, 374, 688, 375, 686, 375, 685, 376,
		683, 376, 682, 377, 680, 377, 679, 378, 676, 378, 675, 379, 670, 379, 669, 380, 660, 380, 659, 381, 258, 381, 257, 382, 256, 382,
		255, 383, 220, 383, 219, 382, 212, 382, 211, 381, 177, 381, 176, 382, 169, 382, 168, 383, 132, 383, 130, 381, 108, 381, 107, 380,
		106, 381, 106, 382, 74, 382, 73, 381, 67, 381, 66, 380, 59, 380, 58, 379, 54, 379, 53, 378, 50, 378, 49, 377, 47, 377, 46, 376,
		44, 376, 43, 375, 41, 375, 40, 374, 39, 374, 38, 373, 37, 373, 36, 372, 35, 372, 34, 371, 33, 371, 32, 370, 31, 370, 30, 369, 29,
		369, 26, 366, 25, 366, 16, 357, 16, 356, 13, 353, 13, 352, 11, 350, 11, 349, 10, 348, 10, 347, 9, 346, 9, 345, 8, 344, 8, 342, 7,
		341, 7, 340, 6, 339, 6, 336, 5, 335, 5, 333, 4, 332, 4, 322, 3, 321, 3, 126, 2, 125, 0, 125, 0, 63, 2, 63, 3, 62, 3, 53, 4, 52,
		4, 49, 5, 48, 5, 45, 6, 44, 6, 42, 7, 41, 7, 39, 8, 38, 8, 36, 9, 35, 9, 34, 10, 33, 10, 32, 12, 30, 12, 29, 13, 28, 13, 27, 16,
		24, 16, 23, 24, 15, 25, 15, 28, 12, 29, 12, 31, 10, 32, 10, 33, 9, 34, 9, 35, 8, 36, 8, 37, 7, 38, 7, 39, 6, 41, 6, 42, 5, 43, 5,
		44, 4, 46, 4, 47, 3, 50, 3, 51, 2, 54, 2, 55, 1, 60, 1, 61, 0
	}};
	
	

	private IBrowserSimBrowserFactory browserFactory;
	private Shell shell;
	private BrowserSimBrowser browser;
	private Display display;
	private ControlHandler controlHandler;
	private Text locationText;
	private ProgressBar progressBar;
	private int orientation;
	private CompositeImageDecorator backCompositeDecorator;
	private CompositeImageDecorator forwardCompositeDecorator;
	private CompositeImageDecorator refreshCompositeDecorator;
	private CompositeImageDecorator stopCompositeDecorator;
	private Image shellImage;
	private Region shellRegion;
	private CompositeImageDecorator showAddressBarCompositeDecorator;
	private boolean addressBarVisible = true;
	private int currentOrientationIndex;
	private CompositeImageDecorator addressBarDecorator;
	private ImageList imageList;
	private BrowserFunction scrollListener = null;

	@Override
	public void setBrowserFactory(IBrowserSimBrowserFactory browserFactory) {
		this.browserFactory = browserFactory;
	}

	@Override
	public void createControls(Display display) {
		this.display = display;
		shell = new Shell(display, SWT.NO_TRIM | SWT.NO_BACKGROUND);
		imageList = new ImageList(shell);

		Image backImage = imageList.getImage("iphone_skin/back.png");
		Image backSelectedImage = imageList.getImage("iphone_skin/back-selected.png");
		Image forwardImage = imageList.getImage("iphone_skin/forward.png");
		Image forwardSelectedImage = imageList.getImage("iphone_skin/forward-selected.png");
		Image refreshImage = imageList.getImage("iphone_skin/refresh.png");
		Image refreshSelectedImage = imageList.getImage("iphone_skin/refresh-selected.png");
		Image stopImage = imageList.getImage("iphone_skin/stop.png");
		Image stopSelectedImage = imageList.getImage("iphone_skin/stop-selected.png");

		Composite backComposite = new Composite(shell, SWT.NONE);
		backComposite.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.goBack();
				}
			};
		});
		backCompositeDecorator = new CompositeImageDecorator(backComposite);
		backCompositeDecorator.setImages(backImage, backSelectedImage);
		backCompositeDecorator.setLocations(BACK_LOCATION[VERTICAL], BACK_LOCATION[HORIZONTAL]);

		Composite forwardComposite = new Composite(shell, SWT.NONE);
		forwardComposite.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.goForward();
				}
			}
		});
		forwardCompositeDecorator = new CompositeImageDecorator(forwardComposite);
		forwardCompositeDecorator.setImages(forwardImage, forwardSelectedImage);
		forwardCompositeDecorator.setLocations(FORWARD_LOCATION[VERTICAL], FORWARD_LOCATION[HORIZONTAL]);
		
		Composite addressBar = new Composite(shell, SWT.NONE);
		addressBarDecorator = new CompositeImageDecorator(addressBar);
		addressBarDecorator.setImages(imageList.getImage(ADDRESS_BAR_IMAGE[VERTICAL]), imageList.getImage(ADDRESS_BAR_IMAGE[VERTICAL]),
				imageList.getImage(ADDRESS_BAR_IMAGE[HORIZONTAL]), imageList.getImage(ADDRESS_BAR_IMAGE[HORIZONTAL]));
		addressBarDecorator.setLocations(ADDRESS_BAR_LOCATION[VERTICAL], ADDRESS_BAR_LOCATION[HORIZONTAL]);
		
		locationText = new Text(addressBar, SWT.NONE);
		locationText.setBackground(display.getSystemColor(URL_BACKGROUND_COLOR));
		
		progressBar = new ProgressBar(addressBar, SWT.NONE);
		progressBar.setVisible(false);

		
		Composite refreshComposite = new Composite(addressBar, SWT.NONE);
		refreshComposite.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.refresh();
				}
			}
		});
		refreshCompositeDecorator = new CompositeImageDecorator(refreshComposite);
		refreshCompositeDecorator.setImages(refreshImage, refreshSelectedImage);
		refreshCompositeDecorator.setLocations(REFRESH_LOCATION[VERTICAL], REFRESH_LOCATION[HORIZONTAL]);
		
		Composite stopComposite = new Composite(addressBar, SWT.NONE);
		stopCompositeDecorator = new CompositeImageDecorator(stopComposite);
		stopCompositeDecorator.setImages(stopImage, stopSelectedImage);
		stopCompositeDecorator.setLocations(STOP_LOCATION[VERTICAL], STOP_LOCATION[HORIZONTAL]);
		stopComposite.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.stop();
				}
			}
		});
		
		Composite showAddressBarComposite = new Composite(shell, SWT.NONE);
		showAddressBarComposite.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					setAddressBarVisible(!addressBarVisible);
				}
			}
		});
		showAddressBarCompositeDecorator = new CompositeImageDecorator(showAddressBarComposite);
		Image showAddressBarVerticalImage = imageList.getImage(SHOW_ADDRESS_BAR_IMAGE[VERTICAL]);
		Image showAddressBarHorizontalImage = imageList.getImage(SHOW_ADDRESS_BAR_IMAGE[HORIZONTAL]);
		showAddressBarCompositeDecorator.setImages(showAddressBarVerticalImage, showAddressBarVerticalImage, showAddressBarHorizontalImage, showAddressBarHorizontalImage);
		showAddressBarCompositeDecorator.setLocations(SHOW_ADDRESS_BAR_LOCATION[VERTICAL], SHOW_ADDRESS_BAR_LOCATION[HORIZONTAL]);

		Listener l = new Listener() {
			Point origin;

			public void handleEvent(Event e) {
				if ((e.stateMask & SWT.BUTTON1) != 0 || e.button == 1) { // left mouse Composite
					switch (e.type) {
					case SWT.MouseDown:
						origin = new Point(e.x, e.y);
						break;
					case SWT.MouseUp:
						origin = null;
						break;
					case SWT.MouseMove:
						if (origin != null) {
							Point p = AppleIPhone3Skin.this.display.map(shell, null, e.x, e.y);
							shell.setLocation(p.x - origin.x, p.y - origin.y);
						}
						break;
					}
				}
			}
		};

		shell.addListener(SWT.MouseDown, l);
		shell.addListener(SWT.MouseUp, l);
		shell.addListener(SWT.MouseMove, l);
		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle bounds = shellImage.getBounds();
				Point size = shell.getSize();
				e.gc.drawImage(shellImage, 0, 0, bounds.width, bounds.height, 0, 0,
						size.x, size.y);
			}
		});
		
//		Label showAddressBarLabel = new Label(shell, SWT.NONE);
//		showAddressBarLabel.setBounds(new Rectangle(33, 126, 320, 18));
		browser = browserFactory.createBrowser(shell, SWT.NONE);
		browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				if (scrollListener != null) {
					scrollListener.dispose();
				}
				scrollListener = new BrowserFunction(browser, "_browserSim_scrollListener") {
					public Object function(Object[] arguments) {
						double pageYOffset = (Double) arguments[VERTICAL];
						if (pageYOffset > 0.0) {
							setAddressBarVisible(false);
						}
						return null;
					}
				};
				
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
				setAddressBarVisible(true);
			}
		});

		locationText.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				controlHandler.goToAddress(locationText.getText());
			}
		});
		
		currentOrientationIndex = AppleIPhone3Skin.VERTICAL;
		setWidgetsBounds(currentOrientationIndex);
		shell.open();
	}
	
	private void setAddressBarVisible(boolean visible) {
		Rectangle browserBounds;
		if (visible) {
			browserBounds = BROWSER_RECTANGLE[currentOrientationIndex];
		} else {
			browserBounds = BROWSER_RECTANGLE_MAXIMIZED[currentOrientationIndex];
		}
		browser.setBounds(browserBounds);
		
		addressBarDecorator.setVisible(visible);
		addressBarVisible = visible;
	}

	private void setWidgetsBounds(int orientationIndex) {
		boolean vertical = orientationIndex == VERTICAL;
		addressBarDecorator.setVertical(vertical);
		backCompositeDecorator.setVertical(vertical);
		forwardCompositeDecorator.setVertical(vertical);
		refreshCompositeDecorator.setVertical(vertical);
		stopCompositeDecorator.setVertical(vertical);
		showAddressBarCompositeDecorator.setVertical(vertical);
		
		locationText.setBounds(URL_RECTANGLE[orientationIndex]);
		progressBar.setBounds(PROGRESS_BAR_RECTANGLE[orientationIndex]);
		browser.setBounds(BROWSER_RECTANGLE[orientationIndex]);
		setAddressBarVisible(addressBarVisible);

		if (shellRegion != null) {
			shellRegion.dispose();
		}
		shellRegion = new Region();		
		shellRegion.add(VISIBLE_REGION[orientationIndex]);
		shell.setRegion(shellRegion);
		shellImage = imageList.getImage(SHELL_IMAGE[orientationIndex]);
		// define a region
		Rectangle imageSize = shellImage.getBounds();
		// shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setSize(imageSize.width, imageSize.height);
		// shell.setBackgroundImage(image);
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
		return display.getMenuBar();
	}

	@Override
	public void setControlHandler(ControlHandler controlHandler) {
		this.controlHandler = controlHandler;
	}

	@Override
	public void setBrowserSize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void locationChanged(String location) {
		locationText.setText(location);
	}

	@Override
	public void progressChanged(int percents) {
		if (percents > 0) {
			progressBar.setVisible(true);
			progressBar.setSelection(percents);
		} else {
			progressBar.setVisible(false);
			progressBar.setSelection(0);
		}
	}

	@Override
	public void statusTextChanged(String newStatusText) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setOrientation(int orientation) {
		if ((this.orientation == DeviceOrientation.PORTRAIT || this.orientation == DeviceOrientation.PORTRAIT_INVERTED) &&
				(orientation == DeviceOrientation.LANDSCAPE || orientation == DeviceOrientation.LANDSCAPE_INVERTED)) {
			//changed from portrait to landscape
			currentOrientationIndex = HORIZONTAL;
		} else if ((this.orientation == DeviceOrientation.LANDSCAPE || this.orientation == DeviceOrientation.LANDSCAPE_INVERTED) &&
					(orientation == DeviceOrientation.PORTRAIT || orientation == DeviceOrientation.PORTRAIT_INVERTED)) {
			//changed from landscape to portrait
			currentOrientationIndex = VERTICAL;
		}
		setWidgetsBounds(currentOrientationIndex);
		
		this.orientation = orientation;
	}
}

