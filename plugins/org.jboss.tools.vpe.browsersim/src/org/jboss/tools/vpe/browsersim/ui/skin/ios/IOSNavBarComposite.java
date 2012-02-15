package org.jboss.tools.vpe.browsersim.ui.skin.ios;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageList;


public class IOSNavBarComposite extends Composite {
	private final String BD = "ios/";
	private final ImageDescriptor[] BODY_DESCRIPTOR = {
		new ImageDescriptor(BD + "nav-1.png"), new ImageDescriptor(BD + "nav-2.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(BD + "nav-3.png"), new ImageDescriptor(BD + "nav-4.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(BD + "nav-5.png")
	};
	private ImageButtonComposite stopButtonComposite;
	private ImageButtonComposite refreshButtonComposite;
	private Text urlText;
	private ProgressBar progressBar;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public IOSNavBarComposite(Composite parent, ImageList imageList) {
		super(parent, SWT.NONE);

		this.setLayout(new FormLayout());
		
		urlText = new Text(this, SWT.SINGLE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 12);
		formData.right = new FormAttachment(100, -62);
		formData.top = new FormAttachment(0, 33);
		formData.bottom = new FormAttachment(100, -8);
		urlText.setLayoutData(formData);
		
		refreshButtonComposite = new ImageButtonComposite(this, imageList.getImage(BD + "refresh.png"), imageList.getImage(BD + "refresh.png"), imageList.getImage(BD + "refresh-selected.png"));
		formData = new FormData();
		Rectangle refreshImageBounds = imageList.getImage(BD + "refresh.png").getBounds();
		formData.right = new FormAttachment(100, -8);
		formData.bottom = new FormAttachment(100, -7);
		formData.width = refreshImageBounds.width;
		formData.height = refreshImageBounds.height;
		refreshButtonComposite.setLayoutData(formData);

		stopButtonComposite = new ImageButtonComposite(this, imageList.getImage(BD + "stop.png"), imageList.getImage(BD + "stop.png"), imageList.getImage(BD + "stop-selected.png"));
		formData = new FormData();
		Rectangle stopImageBounds = imageList.getImage(BD + "stop.png").getBounds();
		formData.right = new FormAttachment(100, -42);
		formData.bottom = new FormAttachment(100, -11);
		formData.width = stopImageBounds.width;
		formData.height = stopImageBounds.height;
		stopButtonComposite.setLayoutData(formData);
		
		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setVisible(false);
		formData = new FormData();
		formData.left = new FormAttachment(0, 7);
		formData.right = new FormAttachment(100, -36);
		formData.bottom = new FormAttachment(100, -1);
		formData.height = 5;
		progressBar.setLayoutData(formData);
		
		Composite imagesComposite = new Composite(this, SWT.NONE);
		formData = new FormData();
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		formData.top = new FormAttachment(0);
		formData.bottom = new FormAttachment(100);
		imagesComposite.setLayoutData(formData);
		
		GridLayout gridLayout = new GridLayout(5, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		
		imagesComposite.setLayout(gridLayout);
		
		for (ImageDescriptor descriptor : BODY_DESCRIPTOR) {
			descriptor.createWidget(imagesComposite, imageList);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public ImageButtonComposite getStopButtonComposite() {
		return stopButtonComposite;
	}

	public ImageButtonComposite getRefreshButtonComposite() {
		return refreshButtonComposite;
	}

	public Text getUrlText() {
		return urlText;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}
}
