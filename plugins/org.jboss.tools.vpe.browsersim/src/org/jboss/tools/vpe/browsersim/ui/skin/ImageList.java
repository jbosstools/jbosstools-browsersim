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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;
import org.jboss.tools.vpe.browsersim.util.ResourcesUtil;

/**
 * A cache of images. All created images are disposed automatically
 * together with given {@link #disposable}.  
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ImageList {
	private Widget disposable;
	private Map<String, Image> imageMap = new HashMap<String, Image>();
	
	public ImageList(Widget disposable) {
		this.disposable = disposable;
		disposable.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		});
	}

	public Image getImage(String location) {
		Image image = imageMap.get(location);
		if (image == null) {
			image = new Image(disposable.getDisplay(), ResourcesUtil.getResourceAsStream(location));
			imageMap.put(location, image);
		}
		
		return image;
	}
	
	private void dispose() {
		for (Image image : imageMap.values()) {
			image.dispose();
		}
		imageMap.clear();
	}
}
