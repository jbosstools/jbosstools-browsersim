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
package org.jboss.tools.vpe.browsersim.ui.skin.android;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.vpe.browsersim.ui.AdressBarText;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageButtonComposite;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.vpe.browsersim.util.BrowserSimImageList;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
@SuppressWarnings("nls")
public class AndroidNavBarComposite extends Composite {
	private final String BD = "android/";
	private final ImageDescriptor[] BODY_DESCRIPTOR = {
		new ImageDescriptor(BD + "nav-1.png"), new ImageDescriptor(BD + "nav-2.png", 1, 1, SWT.HORIZONTAL), new ImageDescriptor(BD + "nav-3.png")
	};
	private ImageButtonComposite stopButtonComposite;
	private Text urlText;
	private ProgressBar progressBar;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AndroidNavBarComposite(Composite parent, BrowserSimImageList imageList) {
		super(parent, SWT.NONE);

		this.setLayout(new FormLayout());
		
		urlText = new AdressBarText(this, SWT.SINGLE);
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 17);
		formData.right = new FormAttachment(100, -55);
		formData.top = new FormAttachment(0, 22 - AdressBarText.adressBarUrlOffset);
		formData.height = 16;
		urlText.setLayoutData(formData);
		
		stopButtonComposite = new ImageButtonComposite(this, imageList.getImage(BD + "stop.png"), imageList.getImage(BD + "stop.png"), imageList.getImage(BD + "stop-selected.png"));
		formData = new FormData();
		Rectangle stopImageBounds = imageList.getImage(BD + "stop.png").getBounds();
		formData.right = new FormAttachment(100, -12);
		formData.bottom = new FormAttachment(100, -11);
		formData.width = stopImageBounds.width;
		formData.height = stopImageBounds.height;
		stopButtonComposite.setLayoutData(formData);
		
		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setVisible(false);
		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 2);
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

	public Text getUrlText() {
		return urlText;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
