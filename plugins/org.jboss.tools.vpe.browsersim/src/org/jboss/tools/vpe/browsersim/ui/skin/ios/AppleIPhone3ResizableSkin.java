package org.jboss.tools.vpe.browsersim.ui.skin.ios;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
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
import org.jboss.tools.vpe.browsersim.ui.skin.AppleIPhone3Skin;
import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;

public class AppleIPhone3ResizableSkin implements BrowserSimSkin {
	private static final Point NORMAL_SKREEN_SIZE = new Point(320, 480);
	private static final Point NORMAL_SKIN_SIZE = new Point(384, 727);
	private static final Point VERTICAL_BORDERS_SIZE = new Point(NORMAL_SKIN_SIZE.x - NORMAL_SKREEN_SIZE.x, NORMAL_SKIN_SIZE.y - NORMAL_SKREEN_SIZE.y);
	private static final Point HORIZONTAL_BORDERS_SIZE = new Point(VERTICAL_BORDERS_SIZE.y, VERTICAL_BORDERS_SIZE.x);
	private static final IPhoneSkinDescriptor VERTICAL_IPHONE3_DESCRIPTOR;
	static {
		String bd = "ios/iphone3/vertical/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 5, 3, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "08.png"), iOsDescriptor,                                                                                                                                                                                                                  new ImageDescriptor(bd + "14.png"),
				new ImageDescriptor(bd + "21.png", 1, 1, SWT.VERTICAL),                                                                                                                                                                                                             new ImageDescriptor(bd + "22.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "23.png"),                                                                                                                                                                                                                                 new ImageDescriptor(bd + "24.png"),
				new ImageDescriptor(bd + "25.png"), new ImageDescriptor(bd + "26.png"), new ImageDescriptor(bd + "27.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "28.png"), new ImageDescriptor(bd + "29.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "30.png"), new ImageDescriptor(bd + "31.png"),
			};
		int bodyGridSize = 7;
		
		String bd2 = "ios/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 52);
			formData.bottom = new FormAttachment(100, -132);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 115);
			formData.bottom = new FormAttachment(100, -132);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		VERTICAL_IPHONE3_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, backButtonDescriptor, forwardButtonDescriptor);
	}
	private static final IPhoneSkinDescriptor HORIZONTAL_IPHONE3_DESCRIPTOR;
	static {
		String bd = "ios/iphone3/horizontal/";
		ImageDescriptor iOsDescriptor = new ImageDescriptor(null, 5, 5, SWT.VERTICAL | SWT.HORIZONTAL);
		ImageDescriptor[] bodyGridImageDescriptors = {
				new ImageDescriptor(bd + "01.png"), new ImageDescriptor(bd + "02.png"), new ImageDescriptor(bd + "03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "04.png"), new ImageDescriptor(bd + "05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "06.png"), new ImageDescriptor(bd + "07.png"),
				new ImageDescriptor(bd + "08.png"), iOsDescriptor,                                                                                                                                                                                                                  new ImageDescriptor(bd + "10.png"),
				new ImageDescriptor(bd + "11.png", 1, 1, SWT.VERTICAL),                                                                                                                                                                                                             new ImageDescriptor(bd + "12.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "13.png"),                                                                                                                                                                                                                                 new ImageDescriptor(bd + "14.png"),
				new ImageDescriptor(bd + "15.png", 1, 1, SWT.VERTICAL),                                                                                                                                                                                                             new ImageDescriptor(bd + "16.png", 1, 1, SWT.VERTICAL),
				new ImageDescriptor(bd + "17.png"),                                                                                                                                                                                                                                 new ImageDescriptor(bd + "18.png"),
				new ImageDescriptor(bd + "19.png"), new ImageDescriptor(bd + "20.png"), new ImageDescriptor(bd + "21.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "22.png"), new ImageDescriptor(bd + "23.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(bd + "24.png"), new ImageDescriptor(bd + "25.png"),
		};
		int bodyGridSize = 7;
		String bd2 = "ios/";
		ButtonDescriptor backButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 145);
			formData.bottom = new FormAttachment(100, -36);
			backButtonDescriptor = new ButtonDescriptor(formData, bd2 + "back.png", bd2 + "back-disabled.png", bd2 + "back-selected.png");
		}
		
		ButtonDescriptor forwardButtonDescriptor;
		{
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 247);
			formData.bottom = new FormAttachment(100, -36);
			forwardButtonDescriptor = new ButtonDescriptor(formData, bd2 + "forward.png", bd2 + "forward-disabled.png", bd2 + "forward-selected.png");
		}
		HORIZONTAL_IPHONE3_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, backButtonDescriptor, forwardButtonDescriptor);
	}

	private IBrowserSimBrowserFactory browserFactory;
	private boolean vertical;
	private Display display;
	private Shell shell;
	private BrowserSimBrowser browser;
	private ControlHandler controlHandler;
	private AppleIPhoneComposite iPhoneComposite;

	@Override
	public void setBrowserFactory(IBrowserSimBrowserFactory browserFactory) {
		this.browserFactory = browserFactory;		
	}

	@Override
	public void createControls(Display display) {
		this.display = display;
		shell = new Shell(display, SWT.NO_TRIM | SWT.NO_BACKGROUND);
		shell.setLayout(new FillLayout());
		
		iPhoneComposite = new AppleIPhoneComposite(shell, VERTICAL_IPHONE3_DESCRIPTOR);
		vertical = true;
		bindIPhoneCompositeControls();
		Composite browserContainer = iPhoneComposite.getBrowserContainer();
		browserContainer.setLayout(new FillLayout());
		browser = browserFactory.createBrowser(browserContainer, SWT.NONE);
		
		shell.setSize(/*shell.computeSize(SWT.DEFAULT, SWT.DEFAULT)*/ 384, 727);
		shell.open();
		setShellRegion();
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
		Region region = new Region();
		region.add(shellRegion);
		shell.setRegion(region);
		region.dispose();
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

	private void bindIPhoneCompositeControls() {
		iPhoneComposite.getBackButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.goBack();
				}
			}
		});
		iPhoneComposite.getForwardButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.goForward();
				}
			}
		});
		iPhoneComposite.getStopButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.stop();
				}
			}
		});
		iPhoneComposite.getRefreshButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.refresh();
				}
			}
		});
		iPhoneComposite.getUrlText().addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				controlHandler.goToAddress(((Text)e.widget).getText());
			}
		});
		
		
		Listener l = new Listener() {
			Point origin;
			Point shellOrigin;

			public void handleEvent(Event e) {
				if ((e.stateMask & SWT.BUTTON1) != 0 || e.button == 1) { // left mouse Composite
					switch (e.type) {
					case SWT.MouseDown:
						origin = AppleIPhone3ResizableSkin.this.display.map(shell, null, e.x, e.y);
						shellOrigin = shell.getLocation();
						break;
					case SWT.MouseUp:
						origin = null;
						break;
					case SWT.MouseMove:
						if (origin != null) {
							Point p = AppleIPhone3ResizableSkin.this.display.map(shell, null, e.x, e.y);
							shell.setLocation(shellOrigin.x + p.x - origin.x, shellOrigin.y + p.y - origin.y);
						}
						break;
					}
				}
			}
		};
		iPhoneComposite.addListener(SWT.MouseDown, l);
		iPhoneComposite.addListener(SWT.MouseUp, l);
		iPhoneComposite.addListener(SWT.MouseMove, l);
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
	public void locationChanged(String newLocation, boolean backEnabled,
			boolean forwardEnabled) {
		iPhoneComposite.getUrlText().setText(newLocation);
		iPhoneComposite.getBackButtonComposite().setEnabled(backEnabled);
		iPhoneComposite.getForwardButtonComposite().setEnabled(forwardEnabled);
	}
	

	@Override
	public void pageTitleChanged(String newTitle) {
		iPhoneComposite.getPageTitleStyledText().setText(newTitle);
	}

	@Override
	public void progressChanged(int percents) {
		ProgressBar progressBar = iPhoneComposite.getProgressBar();
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
		// not supported		
	}

	@Override
	public void setOrientationAndSize(Point maximumShellSize, int orientation, Point browserSize) {
		vertical = (orientation == DeviceOrientation.PORTRAIT || orientation == DeviceOrientation.PORTRAIT_INVERTED);
		String urlTextText = iPhoneComposite.getUrlText().getText();
		String pageTitle = iPhoneComposite.getPageTitleStyledText().getText();
		boolean backEnabled = iPhoneComposite.getBackButtonComposite().getEnabled();
		boolean forwardEnabled = iPhoneComposite.getForwardButtonComposite().getEnabled();
		boolean navBarVisible = iPhoneComposite.isNavBarCompositeVisible();
		Menu contextMenu = iPhoneComposite.getMenu();
		iPhoneComposite.setMenu(null);
		
		AppleIPhoneComposite oldIPhoneComposite = iPhoneComposite;
		IPhoneSkinDescriptor skinDescriptor = getSkinDescriptor(vertical);
		iPhoneComposite = new AppleIPhoneComposite(shell, skinDescriptor);
		bindIPhoneCompositeControls();
		Composite browserContainer = iPhoneComposite.getBrowserContainer();
		browserContainer.setLayout(new FillLayout());
		browser.setParent(browserContainer);
		oldIPhoneComposite.dispose();
		Point bordersSize = getBordersSize(vertical);
		int shellWidth;
		if (browserSize.x == SWT.DEFAULT) {
			shellWidth = SWT.DEFAULT;
		} else {
			shellWidth = Math.min(bordersSize.x + browserSize.x, maximumShellSize.x);
		}
		int shellHeight;
		if (browserSize.y == SWT.DEFAULT) {
			shellHeight = SWT.DEFAULT;
		} else {
			shellHeight = Math.min(bordersSize.y + browserSize.y, maximumShellSize.y);
		}

		shell.setSize(shell.computeSize(shellWidth, shellHeight));
		shell.layout(true);
		setShellRegion();
		
		iPhoneComposite.getUrlText().setText(urlTextText);
		iPhoneComposite.getPageTitleStyledText().setText(pageTitle);
		iPhoneComposite.getBackButtonComposite().setEnabled(backEnabled);
		iPhoneComposite.getForwardButtonComposite().setEnabled(forwardEnabled);
		iPhoneComposite.setNavBarCompositeVisible(navBarVisible);
		iPhoneComposite.setMenu(contextMenu);
	}

	protected Point getBordersSize(boolean vertical) {
		Point bordersSize = vertical ? VERTICAL_BORDERS_SIZE : HORIZONTAL_BORDERS_SIZE;
		return bordersSize;
	}

	protected IPhoneSkinDescriptor getSkinDescriptor(boolean vertical) {
		IPhoneSkinDescriptor skinDescriptor;
		if (vertical) {
			skinDescriptor = VERTICAL_IPHONE3_DESCRIPTOR;
		} else {
			skinDescriptor = HORIZONTAL_IPHONE3_DESCRIPTOR;
		}
		return skinDescriptor;
	}
	
	protected int[] getNormalRegion(boolean vertical) {
		int[] normalRegion;
		if (vertical) {
			normalRegion = AppleIPhone3Skin.VISIBLE_REGION[0];
		} else {
			normalRegion = AppleIPhone3Skin.VISIBLE_REGION[1];
		}
		return normalRegion;
	}
	
	@Override
	public void setAddressBarVisible(boolean visible) {
		iPhoneComposite.setNavBarCompositeVisible(visible);
	}

	@Override
	public void setContextMenu(Menu contextMenu) {
		iPhoneComposite.setMenu(contextMenu);
	}
}
