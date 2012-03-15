package org.jboss.tools.vpe.browsersim.ui.skin.ios;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageList;


public class AppleIPhoneComposite extends Composite {
	private ImageList imageList;
	private Composite iOsCompositeContainer;
	
//	ImageDescriptor I_OS_DESCRIPTOR = new ImageDescriptor(null, 5, 3, SWT.VERTICAL | SWT.HORIZONTAL);
//	ImageDescriptor[] BODY_DESCRIPTOR = {
//		new ImageDescriptor("01.png"), new ImageDescriptor("02.png"), new ImageDescriptor("03.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor("04.png"), new ImageDescriptor("05.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor("06.png"), new ImageDescriptor("07.png"),
//		new ImageDescriptor("08.png"),                                I_OS_DESCRIPTOR,                                                                                                         new ImageDescriptor("14.png"),
//		new ImageDescriptor("21.png", 1, 1, SWT.VERTICAL),                                                                                                                                                                                    new ImageDescriptor("22.png", 1, 1, SWT.VERTICAL),
//		new ImageDescriptor("23.png"),                                                                                                                                                                                                        new ImageDescriptor("24.png"),
//		new ImageDescriptor("25.png"), new ImageDescriptor("26.png"), new ImageDescriptor("27.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor("28.png"), new ImageDescriptor("29.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor("30.png"), new ImageDescriptor("31.png"),
//	};

	private Composite browserContainer;
	private IOSNavBarComposite navBarComposite;
	private ImageButtonComposite forwardButtonComposite;
	private ImageButtonComposite backButtonComposite;

	private Composite bodyComposite;

	/**
	 * Create the composite.
	 * @param parent
	 * @param skinDescriptor 
	 * @param style
	 */
	public AppleIPhoneComposite(Composite parent, IPhoneSkinDescriptor skinDescriptor) {
		super(parent, SWT.NONE);
		
		imageList = new ImageList(this);
		setLayout(new FormLayout());
		
		backButtonComposite = new ImageButtonComposite(this, imageList, skinDescriptor.getBackButton());
		forwardButtonComposite = new ImageButtonComposite(this, imageList, skinDescriptor.getForwardButton());
		
		bodyComposite = new Composite(this, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0);
		formData.top = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		formData.bottom = new FormAttachment(100);
		bodyComposite.setLayoutData(formData);

		GridLayout gridLayout = new GridLayout(skinDescriptor.getBodyGridSize(), false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		bodyComposite.setLayout(gridLayout);
		
		for (ImageDescriptor descriptor : skinDescriptor.getBodyGridImageDescriptors()) {
			Composite composite = descriptor.createWidget(bodyComposite, imageList);
			if (descriptor == skinDescriptor.getiOSDescriptor()) {
				iOsCompositeContainer = composite;
			}
		}
		
		gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		iOsCompositeContainer.setLayout(gridLayout);
		IOsTimeComposite timeComposite = new IOsTimeComposite(iOsCompositeContainer, imageList);
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
		timeComposite.setLayoutData(gridData);
		
		navBarComposite = new IOSNavBarComposite(iOsCompositeContainer, imageList);
		gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
		navBarComposite.setLayoutData(gridData);

		browserContainer = new Composite(iOsCompositeContainer, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		browserContainer.setLayoutData(gridData);

		timeComposite.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					setNavBarCompositeVisible(!isNavBarCompositeVisible());
				}
			}
		});
	}
	
	public ImageButtonComposite getBackButtonComposite() {
		return backButtonComposite;
	}
	
	public ImageButtonComposite getForwardButtonComposite() {
		return forwardButtonComposite;
	}
	
	public Composite getBrowserContainer() {
		return browserContainer;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setNavBarCompositeVisible(boolean visible) {
		GridData gridData = (GridData) navBarComposite.getLayoutData();
		if (visible) {
			gridData.heightHint = SWT.DEFAULT;
		} else {
			gridData.heightHint = 0;
		}
		iOsCompositeContainer.layout();
	}
	
	public boolean isNavBarCompositeVisible() {
		GridData gridData = (GridData) navBarComposite.getLayoutData();
		return gridData.heightHint != 0;
	}

	public ImageButtonComposite getStopButtonComposite() {
		return navBarComposite.getStopButtonComposite();
	}

	public ImageButtonComposite getRefreshButtonComposite() {
		return navBarComposite.getRefreshButtonComposite();
	}

	public Text getUrlText() {
		return navBarComposite.getUrlText();
	}
	
	public ProgressBar getProgressBar() {
		return navBarComposite.getProgressBar();
	}
	
	public StyledText getPageTitleStyledText() {
		return navBarComposite.getPageTitleStyledText();
	}
	
	@Override
	public void addListener(int eventType, Listener listener) {
		super.addListener(eventType, listener);
		switch (eventType) {
		case SWT.MouseDown:
		case SWT.MouseUp:
		case SWT.MouseMove:
			bodyComposite.addListener(eventType, listener);
			for (Control child : bodyComposite.getChildren()) {
				child.addListener(eventType, listener);
			}
		}
	}
	
	@Override
	public void removeListener(int eventType, Listener listener) {
		super.removeListener(eventType, listener);
		switch (eventType) {
		case SWT.MouseDown:
		case SWT.MouseUp:
		case SWT.MouseMove:
			bodyComposite.removeListener(eventType, listener);
			for (Control child :bodyComposite.getChildren()) {
				child.removeListener(eventType, listener);
			}
		}
	}
	
	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		bodyComposite.setMenu(menu);
		for (Control child :bodyComposite.getChildren()) {
			child.setMenu(menu);
		}
	}
}
