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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.vpe.browsersim.ui.ControlHandler;

public abstract class ResizableSkin implements BrowserSimSkin {
	protected DeviceComposite deviceComposite;
	protected ControlHandler controlHandler;
	
	//protected Point location;
	
	protected void bindDeviceCompositeControls() {
		deviceComposite.getBackButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.goBack();
				}
			}
		});
		if (deviceComposite.getForwardButtonComposite() != null) {
			deviceComposite.getForwardButtonComposite().addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					if (e.button == 1) {
						controlHandler.goForward();
					}
				}
			});
		}
		deviceComposite.getStopButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.stop();
				}
			}
		});
		deviceComposite.getRefreshButtonComposite().addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					controlHandler.refresh();
				}
			}
		});
		if (deviceComposite.getHomeButtonComposite() != null) {
			deviceComposite.getHomeButtonComposite().addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					if (e.button == 1) {
						controlHandler.goHome();
					}
				}
			});
		}
		deviceComposite.getUrlText().addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				controlHandler.goToAddress(((Text)e.widget).getText());
			}
		});
		TextSelectionUtil.addSelectTextOnFocusListener(deviceComposite.getUrlText());
		
		Listener moveListener = new Listener() {
			Point origin;
			Point shellOrigin;

			public void handleEvent(Event e) {
				if (e.widget instanceof Composite && ((e.stateMask & SWT.BUTTON1) != 0 || e.button == 1)) { // left mouse Composite
					Composite composite = (Composite) e.widget;
					Shell shell = composite.getShell();
					
					switch (e.type) {
					case SWT.MouseDown:
						origin = e.display.map(shell, null, e.x, e.y);
						shellOrigin = shell.getLocation();
						break;
					case SWT.MouseUp:
						origin = null;
						break;
					case SWT.MouseMove:
						if (origin != null) {
							Point p = e.display.map(shell, null, e.x, e.y);
							Point location = new Point(shellOrigin.x + p.x - origin.x, shellOrigin.y + p.y - origin.y);
							shell.setLocation(location.x, location.y);
						}
						break;
					}
				}
			}
		};
		deviceComposite.addListener(SWT.MouseDown, moveListener);
		deviceComposite.addListener(SWT.MouseUp, moveListener);
		deviceComposite.addListener(SWT.MouseMove, moveListener);
		
		final ImageList imageList = new ImageList(deviceComposite);
		Listener rotationHotSpotListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				Control contol = event.display.getCursorControl();
				Point displayPoint = event.display.getCursorLocation();
				Image rotateImage = imageList.getImage("cursors/rotate.gif"); //$NON-NLS-1$
				if (deviceComposite.isDeviceCorner(displayPoint) && deviceComposite.isDeviceBody(contol)) {
					deviceComposite.setCursor(new Cursor(Display.getCurrent(), rotateImage.getImageData(), 0, 0));
				} else {
					deviceComposite.setCursor(null);					
				}
			}
		};
		deviceComposite.addListener(SWT.MouseMove, rotationHotSpotListener); 
		deviceComposite.addListener(SWT.MouseExit, rotationHotSpotListener);
		
		Listener rotationHotSpotClickListener  = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.button == 1) {
					Control contol = event.display.getCursorControl();
					Point displayPoint = event.display.getCursorLocation();
					if (deviceComposite.isDeviceCorner(displayPoint) && deviceComposite.isDeviceBody(contol)) {
						controlHandler.rotate(false);
					}
				}
			}
		};
		
		deviceComposite.addListener(SWT.MouseDown, rotationHotSpotClickListener);
	}
}
