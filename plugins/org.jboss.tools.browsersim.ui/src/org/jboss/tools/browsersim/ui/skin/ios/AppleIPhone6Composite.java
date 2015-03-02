/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui.skin.ios;
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
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.browsersim.ui.skin.DeviceComposite;
import org.jboss.tools.browsersim.ui.skin.ImageButtonComposite;
import org.jboss.tools.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.browsersim.ui.util.BrowserSimImageList;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class AppleIPhone6Composite extends DeviceComposite {
	private BrowserSimImageList imageList;
	private Composite iOsCompositeContainer;
	private Composite browserContainer;
	private IOS8NavBarComposite navBarComposite;
	private ImageButtonComposite forwardButtonComposite;
	private ImageButtonComposite backButtonComposite;
	private int cornersSize;

	/**
	 * Create the composite.
	 * @param parent
	 * @param skinDescriptor 
	 * @param style
	 */
	public AppleIPhone6Composite(Composite parent, IPhoneSkinDescriptor skinDescriptor) {
		super(parent, SWT.NONE);
		
		imageList = new BrowserSimImageList(this);
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
		IOS8TimeComposite timeComposite = new IOS8TimeComposite(iOsCompositeContainer, imageList);
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
		timeComposite.setLayoutData(gridData);
		
		navBarComposite = new IOS8NavBarComposite(iOsCompositeContainer, imageList);
		gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
		navBarComposite.setLayoutData(gridData);

		browserContainer = new Composite(iOsCompositeContainer, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		browserContainer.setLayoutData(gridData);
		
		cornersSize = skinDescriptor.getCornersSize();

		timeComposite.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					setNavBarCompositeVisible(!isNavBarCompositeVisible());
				}
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#getBackButtonComposite()
	 */
	@Override
	public ImageButtonComposite getBackButtonComposite() {
		return backButtonComposite;
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#getForwardButtonComposite()
	 */
	@Override
	public ImageButtonComposite getForwardButtonComposite() {
		return forwardButtonComposite;
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#getBrowserContainer()
	 */
	@Override
	public Composite getBrowserContainer() {
		return browserContainer;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#setNavBarCompositeVisible(boolean)
	 */
	@Override
	public void setNavBarCompositeVisible(boolean visible) {
		GridData gridData = (GridData) navBarComposite.getLayoutData();
		if (visible) {
			gridData.heightHint = SWT.DEFAULT;
		} else {
			gridData.heightHint = 0;
		}
		iOsCompositeContainer.layout();
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#isNavBarCompositeVisible()
	 */
	@Override
	public boolean isNavBarCompositeVisible() {
		GridData gridData = (GridData) navBarComposite.getLayoutData();
		return gridData.heightHint != 0;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#getStopButtonComposite()
	 */
	@Override
	public ImageButtonComposite getStopButtonComposite() {
		return navBarComposite.getStopButtonComposite();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#getRefreshButtonComposite()
	 */
	@Override
	public ImageButtonComposite getRefreshButtonComposite() {
		return navBarComposite.getRefreshButtonComposite();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#getUrlText()
	 */
	@Override
	public Text getUrlText() {
		return navBarComposite.getUrlText();
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#getProgressBar()
	 */
	@Override
	public ProgressBar getProgressBar() {
		return navBarComposite.getProgressBar();
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.browsersim.ui.skin.ios.PhoneComposite#getPageTitleStyledText()
	 */
	@Override
	public StyledText getPageTitleStyledText() {
		return navBarComposite.getPageTitleStyledText();
	}
	
	@Override
	public ImageButtonComposite getHomeButtonComposite() {
		return null;
	}

	@Override
	protected int getCornersSize() {
		return cornersSize;
	}
}
