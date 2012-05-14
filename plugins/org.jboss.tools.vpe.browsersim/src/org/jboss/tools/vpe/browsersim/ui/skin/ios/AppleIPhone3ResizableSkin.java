package org.jboss.tools.vpe.browsersim.ui.skin.ios;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.BrowserSimBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserSimBrowserFactory;
import org.jboss.tools.vpe.browsersim.model.DeviceOrientation;
import org.jboss.tools.vpe.browsersim.ui.ControlHandler;
import org.jboss.tools.vpe.browsersim.ui.skin.AppleIPhone3Skin;
import org.jboss.tools.vpe.browsersim.ui.skin.DeviceComposite;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.vpe.browsersim.ui.skin.ResizableSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ResizableSkinSizeAdvisor;

public class AppleIPhone3ResizableSkin extends ResizableSkin {
	private static final Point NORMAL_SKREEN_SIZE = new Point(320, 480);
	private static final Point NORMAL_SKIN_SIZE = new Point(384, 727);
	private static final Point VERTICAL_BORDERS_SIZE = new Point(NORMAL_SKIN_SIZE.x - NORMAL_SKREEN_SIZE.x, NORMAL_SKIN_SIZE.y - NORMAL_SKREEN_SIZE.y);
	private static final Point HORIZONTAL_BORDERS_SIZE = new Point(VERTICAL_BORDERS_SIZE.y, VERTICAL_BORDERS_SIZE.x);
	private static final IPhoneSkinDescriptor VERTICAL_IPHONE3_DESCRIPTOR;
	private static final int CORNERS_SIZE = 58;
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
		VERTICAL_IPHONE3_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
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
		HORIZONTAL_IPHONE3_DESCRIPTOR = new IPhoneSkinDescriptor(bodyGridSize, bodyGridImageDescriptors, iOsDescriptor, CORNERS_SIZE, backButtonDescriptor, forwardButtonDescriptor);
	}

	private IBrowserSimBrowserFactory browserFactory;
	private boolean vertical;
	private Display display;
	private Shell shell;
	private BrowserSimBrowser browser;

	@Override
	public void setBrowserFactory(IBrowserSimBrowserFactory browserFactory) {
		this.browserFactory = browserFactory;		
	}

	@Override
	public void createControls(Display display) {
		this.display = display;
		shell = new Shell(display, SWT.NO_TRIM | SWT.NO_BACKGROUND);
		shell.setLayout(new FillLayout());
		
		deviceComposite = new AppleIPhoneComposite(shell, VERTICAL_IPHONE3_DESCRIPTOR);
		vertical = true;
		bindDeviceCompositeControls();
		Composite browserContainer = deviceComposite.getBrowserContainer();
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
		deviceComposite.getUrlText().setText(newLocation);
		deviceComposite.getBackButtonComposite().setEnabled(backEnabled);
		deviceComposite.getForwardButtonComposite().setEnabled(forwardEnabled);
	}
	

	@Override
	public void pageTitleChanged(String newTitle) {
		if (deviceComposite.getPageTitleStyledText() != null) {
			deviceComposite.getPageTitleStyledText().setText(newTitle);
		}
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
	public void statusTextChanged(String newStatusText) {
		// not supported		
	}

	@Override
	public void setOrientationAndSize(int orientation, Point browserSize, ResizableSkinSizeAdvisor sizeAdvisor) {
		vertical = (orientation == DeviceOrientation.PORTRAIT || orientation == DeviceOrientation.PORTRAIT_INVERTED);
		String urlTextText = deviceComposite.getUrlText().getText();
		String pageTitle = deviceComposite.getPageTitleStyledText() != null ? deviceComposite.getPageTitleStyledText().getText() : "";
		boolean backEnabled = deviceComposite.getBackButtonComposite().getEnabled();
		boolean forwardEnabled = deviceComposite.getForwardButtonComposite().getEnabled();
		boolean navBarVisible = deviceComposite.isNavBarCompositeVisible();
		Menu contextMenu = deviceComposite.getMenu();
		deviceComposite.setMenu(null);
		
		DeviceComposite oldDeviceComposite = deviceComposite;
		deviceComposite = createDeviceComposite(shell, vertical);
		bindDeviceCompositeControls();
		Composite browserContainer = deviceComposite.getBrowserContainer();
		browserContainer.setLayout(new FillLayout());
		browser.setParent(browserContainer);
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
		shell.layout(true);
		setShellRegion();
		
		deviceComposite.getUrlText().setText(urlTextText);
		if (deviceComposite.getPageTitleStyledText() != null) {
			deviceComposite.getPageTitleStyledText().setText(pageTitle);
		}
		deviceComposite.getBackButtonComposite().setEnabled(backEnabled);
		deviceComposite.getForwardButtonComposite().setEnabled(forwardEnabled);
		deviceComposite.setNavBarCompositeVisible(navBarVisible);
		deviceComposite.setMenu(contextMenu);
	}

	protected Point getBordersSize(boolean vertical) {
		Point bordersSize = vertical ? VERTICAL_BORDERS_SIZE : HORIZONTAL_BORDERS_SIZE;
		return bordersSize;
	}

	protected DeviceComposite createDeviceComposite(Composite parent, boolean vertical) {
		IPhoneSkinDescriptor skinDescriptor;
		if (vertical) {
			skinDescriptor = VERTICAL_IPHONE3_DESCRIPTOR;
		} else {
			skinDescriptor = HORIZONTAL_IPHONE3_DESCRIPTOR;
		}
		
		return new AppleIPhoneComposite(parent, skinDescriptor);
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
		deviceComposite.setNavBarCompositeVisible(visible);
	}

	@Override
	public void setContextMenu(Menu contextMenu) {
		deviceComposite.setMenu(contextMenu);
	}
}
