package org.jboss.tools.browsersim.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;

public abstract class ImageList {
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
			image = new Image(disposable.getDisplay(), getResourceAsStream(location));
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
	
	public abstract InputStream getResourceAsStream(String location);
}
