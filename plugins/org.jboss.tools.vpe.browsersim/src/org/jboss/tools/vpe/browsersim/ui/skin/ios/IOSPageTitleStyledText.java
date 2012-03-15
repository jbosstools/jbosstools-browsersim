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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.browsersim.ui.skin.ImageList;

public class IOSPageTitleStyledText extends StyledText {
	private final String BD = "ios/";
	private Font font;
	private Color foregroundColor;
	private static boolean IS_OSX = SWT.getPlatform() == "cocoa";
	
	public IOSPageTitleStyledText(Composite parent, ImageList imageList) {
		super(parent, (IS_OSX ? SWT.MULTI : SWT.SINGLE) | SWT.WRAP);
		setBackgroundImage(imageList.getImage(BD + "page-title-background.png"));
		setEditable(false);
		setEnabled(false);
		setMargins(16, IS_OSX ? 7 : 6, 16, 0);
		setAlignment(SWT.CENTER);
		setLineSpacing(27);
		font = createFont(this.getDisplay(), SWT.BOLD);
		setFont(font);
		foregroundColor = new Color(this.getDisplay(), 64, 64, 64);
		setForeground(foregroundColor);
	}
	
	static Font createFont(Device device, int style) {
		Font font;
		if(SWT.getPlatform() == "win32") {
			font = new Font(device, "Arial", 9, style);	
		} else if (SWT.getPlatform() == "gtk") {
			font = new Font(device, "Helvetica", 9, style);		
		} else if (SWT.getPlatform() == "cocoa") {
			font = new Font(device, "Helvetica", 12, style);
		} else {
			font = new Font(device, "Verdana", 9, style);
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
