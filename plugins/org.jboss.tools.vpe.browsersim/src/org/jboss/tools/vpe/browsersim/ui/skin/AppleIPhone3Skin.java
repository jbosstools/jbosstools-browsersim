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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Button;
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
import org.jboss.tools.vpe.browsersim.ui.ControlHandler;
import org.jboss.tools.vpe.browsersim.util.ResourcesUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class AppleIPhone3Skin implements BrowserSimSkin {
	private static final Point FORWARD_LOCATION = new Point(115, 571);
	private static final Rectangle BROWSER_RECTANGLE = new Rectangle(33, 206, 320, 355);
	private static final Rectangle URL_RECTANGLE = new Rectangle(45, 178, 245, 17);
	private static final int URL_BACKGROUND_COLOR = SWT.COLOR_WHITE;
	private static final Rectangle PROGRESS_BAR_RECTANGLE = new Rectangle(40, 200, 277, 5);
	private static final Point STOP_LOCATION = new Point(294, 178);
	private static final Point REFRESH_LOCATION = new Point(323, 173);
	private static final Point BACK_LOCATION = new Point(52, 571);
	
	private static final int[] VISIBLE_REGION = {
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
	}; 

	private IBrowserSimBrowserFactory browserFactory;
	private Shell shell;
	private BrowserSimBrowser browser;
	private Display display;
	private ControlHandler controlHandler;
	private Text locationText;
	private ProgressBar progressBar;

	@Override
	public void setBrowserFactory(IBrowserSimBrowserFactory browserFactory) {
		this.browserFactory = browserFactory;
	}

	@Override
	public void createControls(Display display) {
		this.display = display;
		shell = new Shell(display, SWT.NO_TRIM | SWT.NO_BACKGROUND);
		Region region = new Region();
		region.add(VISIBLE_REGION);
		shell.setRegion(region);
		final Image shellImage = new Image(display, ResourcesUtil.getResourceAsStream("iphone_skin/iPhone3.png"));
		final Image backImage = new Image(display, ResourcesUtil.getResourceAsStream("iphone_skin/back.png"));
		final Image backSelectedImage = new Image(display, ResourcesUtil.getResourceAsStream("iphone_skin/back-selected.png"));
		final Image forwardImage = new Image(display, ResourcesUtil.getResourceAsStream("iphone_skin/forward.png"));
		final Image forwardSelectedImage = new Image(display, ResourcesUtil.getResourceAsStream("iphone_skin/forward-selected.png"));
		final Image refreshImage = new Image(display, ResourcesUtil.getResourceAsStream("iphone_skin/refresh.png"));
		final Image refreshSelectedImage = new Image(display, ResourcesUtil.getResourceAsStream("iphone_skin/refresh-selected.png"));
		final Image stopImage = new Image(display, ResourcesUtil.getResourceAsStream("iphone_skin/stop.png"));
		final Image stopSelectedImage = new Image(display, ResourcesUtil.getResourceAsStream("iphone_skin/stop-selected.png"));
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				shellImage.dispose();
				backImage.dispose();
				backSelectedImage.dispose();
				forwardImage.dispose();
				forwardSelectedImage.dispose();
				refreshImage.dispose();
				refreshSelectedImage.dispose();
				stopImage.dispose();
				stopSelectedImage.dispose();
			}
		});

		PhoneButton backButton = new PhoneButton(shell, backImage, backSelectedImage);
		backButton.setLocation(BACK_LOCATION);
		backButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controlHandler.goBack();
			};
		});

		PhoneButton forwardButton = new PhoneButton(shell, forwardImage, forwardSelectedImage);
		forwardButton.setLocation(FORWARD_LOCATION);
		forwardButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controlHandler.goForward();
			}
		});
		
		PhoneButton refreshButton = new PhoneButton(shell, refreshImage, refreshSelectedImage);
		refreshButton.setLocation(REFRESH_LOCATION);
		refreshButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controlHandler.refresh();
			}
		});
		PhoneButton stopButton = new PhoneButton(shell, stopImage, stopSelectedImage);
		stopButton.setLocation(STOP_LOCATION);
		stopButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				controlHandler.stop();
			}
		});

		Listener l = new Listener() {
			Point origin;

			public void handleEvent(Event e) {
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
		};

		shell.addListener(SWT.MouseDown, l);
		shell.addListener(SWT.MouseUp, l);
		shell.addListener(SWT.MouseMove, l);
		// define a region
		Rectangle imageSize = shellImage.getBounds();
		// shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setSize(imageSize.width, imageSize.height);
		// shell.setBackgroundImage(image);
		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle bounds = shellImage.getBounds();
				Point size = shell.getSize();
				e.gc.drawImage(shellImage, 0, 0, bounds.width, bounds.height, 0, 0,
						size.x, size.y);
			}
		});
		
		locationText = new Text(shell, SWT.NONE);
		locationText.setBackground(display.getSystemColor(URL_BACKGROUND_COLOR));
		locationText.setBounds(URL_RECTANGLE);
		
		progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setBounds(PROGRESS_BAR_RECTANGLE);
		
		browser = browserFactory.createBrowser(shell, SWT.NONE);
		browser.setBounds(BROWSER_RECTANGLE);

		locationText.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				controlHandler.goToAddress(locationText.getText());
			}
		});

		shell.open();
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
		}
	}

	@Override
	public void statusTextChanged(String newStatusText) {
		// TODO Auto-generated method stub
	}
}

class PhoneButton extends Button {
	private Image enabledImage;
	private Image selectedImage;
	
	public PhoneButton(Composite parent, Image enabledImage, Image selectedImage) {
		super(parent, SWT.FLAT);
		this.enabledImage = enabledImage;
		this.selectedImage = selectedImage;
		
		Rectangle imageBounds = enabledImage.getBounds();
		setSize(imageBounds.width, imageBounds.height);
		setImage(enabledImage);
		
		this.addMouseTrackListener(new MouseTrackListener() {
			@Override
			public void mouseHover(MouseEvent e) {
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				setImage(PhoneButton.this.enabledImage);				
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				if (PhoneButton.this.selectedImage != null) {
					setImage(PhoneButton.this.selectedImage);
				}
			}
		});
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		setVisible(enabled);
	}
	
	@Override
	protected void checkSubclass() {
	}
}
