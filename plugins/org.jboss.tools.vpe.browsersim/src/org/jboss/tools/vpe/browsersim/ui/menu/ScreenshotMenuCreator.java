/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.ui.menu;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.ui.Messages;
import org.jboss.tools.vpe.browsersim.util.PreferencesUtil;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class ScreenshotMenuCreator {
	private static final String EXTENSION = ".png";
	private static ImageData data;
	
	public static Menu createScreenshotsMenu(final Menu parent, final Display display, final Shell shell,
			final String defaultFolder) {
		Menu screenshotsMenu = new Menu(parent);
		
		MenuItem saveItem = new MenuItem(screenshotsMenu, SWT.PUSH);
		saveItem.setText(Messages.Screenshots_Save);
		saveItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						Image image = takeScreenshot(display, shell);
						saveImage(image, defaultFolder);
						image.dispose();
					}
				});
				
			};
		});

		MenuItem saveAsItem = new MenuItem(screenshotsMenu, SWT.PUSH);
		saveAsItem.setText(Messages.Screenshots_SaveAs);
		saveAsItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final String selectedPath = saveAs(shell);
				if (selectedPath != null) {
					display.asyncExec(new Runnable() {
						public void run() {
							File selected = new File(selectedPath);
							Image image = takeScreenshot(display, shell);
							saveImage(image, selected.getParentFile().getAbsolutePath(), selected.getName());
							image.dispose();
						}
					});
				}				
			};
		});
		
		MenuItem copyItem = new MenuItem(screenshotsMenu, SWT.PUSH);
		copyItem.setText(Messages.Screenshots_CopyToClipboard);
		copyItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						Image image = takeScreenshot(display, shell);
						//on Linux SWT dnd canot copy image to clipboard, that's why we need to do it using AWT
						if (PlatformUtil.OS_LINUX.equals(PlatformUtil.getOs())) {
							java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							java.awt.Image awtImage = convertToAWT(image.getImageData());
							ImageSelection selection = new ImageSelection(awtImage);
				            clipboard.setContents(selection, null);
				            awtImage.flush();
						} else {
							Clipboard cl = new Clipboard(display);
							cl.setContents(new Object[] {image.getImageData()}, new Transfer[] {ImageTransfer.getInstance()});
							cl.dispose();					
						}				
						image.dispose();
					}
				});
			};
		});
		
		return screenshotsMenu;
	}
	
	private static Image takeScreenshot(final Display display, final Shell parent) {
		/* Take the screen shot */
		GC gc = new GC(parent);
		Image image = new Image(display, parent.getClientArea());
		gc.copyArea(image, 0, 0);
		GC gcImage = new GC(image);
		gcImage.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gcImage.setAdvanced(true);
		Region region = parent.getRegion();

		data = image.getImageData();
		if (region != null) {
			int height = image.getBounds().height;
			int width = image.getBounds().width;
			byte[] alphaData = new byte[height * width];
			int currentPosition = 0;
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (!region.contains(j, i)) {
						gcImage.drawPoint(j, i);
						alphaData[currentPosition] = 0;
					} else {
						alphaData[currentPosition] = (byte) 255;
					}
					currentPosition++;
				}

			}
			data.alphaData = alphaData;
		}

		image.dispose();
		gcImage.dispose();
		gc.dispose();

		return new Image(display, data);
	}
	
	private static String saveAs(Shell parent) {
		FileDialog fd = new FileDialog(parent, SWT.SAVE);
		fd.setText(Messages.Screenshots_SaveAs);
		String[] filterExt = {EXTENSION};
		fd.setFilterExtensions(filterExt);
		fd.setFileName(getDefaultFilename());
		return fd.open();
	}
	
	private static void saveImage(Image image, String defaultScreenshotFolder) {
		File f = new File(defaultScreenshotFolder);
		f.mkdirs();

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(defaultScreenshotFolder + PreferencesUtil.SEPARATOR + getDefaultFilename(), SWT.IMAGE_PNG);
		
		image.dispose();
	}
	
	private static void saveImage(Image image, String folder, String fileName) {
		File f = new File(folder);
		f.mkdirs();
		
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(f.getAbsolutePath() + PreferencesUtil.SEPARATOR + fileName, SWT.IMAGE_PNG);
		
		image.dispose();
	}
	
	private static String getDefaultFilename() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");  
		return df.format(new Date()) + EXTENSION;
	}
	
	private static BufferedImage convertToAWT(ImageData data) {
		ColorModel colorModel = null;
		PaletteData palette = data.palette;
		colorModel = ColorModel.getRGBdefault();
		BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(
				data.width, data.height), false, null);
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				int pixel = data.getPixel(x, y);
				RGB rgb = palette.getRGB(pixel);
				byte alpha = (byte) data.getAlpha(x, y);
				bufferedImage.setRGB(x, y, alpha <<24 | rgb.red << 16 | rgb.green << 8 | rgb.blue);
			}
		}
		return bufferedImage;
	}
	
	private static class ImageSelection implements Transferable {
		private java.awt.Image theImage;
		
		public ImageSelection(java.awt.Image image) {
			theImage = image;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.equals(DataFlavor.imageFlavor);
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (flavor.equals(DataFlavor.imageFlavor)) {
				return theImage;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}
	}
}
