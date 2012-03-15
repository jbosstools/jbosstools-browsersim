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
package org.jboss.tools.vpe.browsersim.ui.skin.ios;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageList;

public class IOSPageTitleStyledText extends StyledText {
	private final String BD = "ios/";
	private Font font;
	private Color foregroundColor;
	
	public IOSPageTitleStyledText(Composite parent, ImageList imageList) {
		super(parent, SWT.SINGLE | SWT.WRAP);
		setBackgroundImage(imageList.getImage(BD + "page-title-background.png"));
		setEditable(false);
		setEnabled(false);
		setMargins(16, 6, 16, 0);
		setAlignment(SWT.CENTER);
		font = createFont(this.getDisplay(), 9, SWT.BOLD);
		setFont(font);
		foregroundColor = new Color(this.getDisplay(), 64, 64, 64);
		setForeground(foregroundColor);
	}
	
	static Font createFont(Device device, int FONT_SIZE, int style) {
		Font font;
		if(SWT.getPlatform() == "win32") {
			font = new Font(device, "Arial", FONT_SIZE, style);	
		} else if (SWT.getPlatform() == "motif") {
			font = new Font(device, "Times", FONT_SIZE, style);		
		} else if (SWT.getPlatform() == "gtk") {
			font = new Font(device, "Baekmuk Batang", FONT_SIZE, style);		
		} else if (SWT.getPlatform() == "cocoa") {
			font = new Font(device, "Helvetica", FONT_SIZE, style);
		} else { // photon
			font = new Font(device, "Verdana", FONT_SIZE, style);
		}
		
		return font;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
		foregroundColor.dispose();
	}
}
