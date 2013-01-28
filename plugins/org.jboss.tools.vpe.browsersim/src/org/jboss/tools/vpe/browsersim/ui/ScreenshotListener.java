package org.jboss.tools.vpe.browsersim.ui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.model.DevicesListStorage;

public class ScreenshotListener implements Listener {
	private static final String EXTENSION = ".png";
	private Display display;
	private Shell parent;
	private boolean isStandalone;
	
	private String folderPath;
	private String imgName;

	private Shell popup;
	private final Color white;

	public ScreenshotListener(Display display, Shell shell, boolean isStandalone) {
		this.display = display;
		this.parent = shell;
		this.isStandalone = isStandalone;		
		
		this.white = display.getSystemColor(SWT.COLOR_WHITE);
	}

	@Override
	public void handleEvent(Event event) {
		popup = new Shell(parent, SWT.SHELL_TRIM);
		popup.setBackground(white);
		popup.setLayout(new GridLayout(1, true));
		popup.setText(Messages.ScreenshotDialog_Screenshot);
		popup.setSize(400, 400);
		
		this.folderPath = DevicesListStorage.getConfigFolderPath(isStandalone) + DevicesListStorage.SEPARATOR + "screenshots";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");  
		this.imgName = df.format(new Date()) + EXTENSION;
		
		final Image image = takeScreenshot();

		popup.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event e) {
				image.dispose();
			}
		});

		addImageComposite(image);
		addButtons(image);

		popup.open();
	}

	private Image takeScreenshot() {
		/* Take the screen shot */
		GC gc = new GC(parent);
		Image image = new Image(display, parent.getClientArea());
		gc.copyArea(image, 0, 0);
		GC gcImage = new GC(image);
		gcImage.setForeground(white);
		gcImage.setAdvanced(true);
		Region region = parent.getRegion();
		
		ImageData data = image.getImageData();
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

	private void addImageComposite(final Image image) {
		final Composite sc = new Composite(popup, SWT.NONE);
		sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sc.setBackground(white);
		sc.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle imageBounds = image.getBounds();
				Rectangle compositeBounds = sc.getBounds();

				double compressX = (double) imageBounds.height / compositeBounds.height;
				double compressY = (double) imageBounds.width / compositeBounds.width;
				double compress = Math.max(compressX, compressY);
				compress = Math.max(compress, 1.0);

				Point compressedSize = new Point((int) (imageBounds.height / compress), (int) (imageBounds.width / compress));

				e.gc.drawImage(image, 0, 0, imageBounds.width, imageBounds.height,
						(compositeBounds.width - compressedSize.y) / 2,
						(compositeBounds.height - compressedSize.x) / 2, compressedSize.y, compressedSize.x);
			}
		});
	}

	private void addButtons(final Image image) {
		Composite buttonsComposite = createButtonsComposite();
		
		addButton(buttonsComposite, Messages.ScreenshotDialog_Save, new Listener() {
			@Override
			public void handleEvent(Event event) {
				saveImage(image, folderPath, imgName);
				popup.dispose();
			}
		});

		addButton(buttonsComposite, Messages.ScreenshotDialog_SaveAndOpen, new Listener() {
			@Override
			public void handleEvent(Event event) {
				saveImage(image, folderPath, imgName);

				popup.dispose();
				Program.launch(folderPath + DevicesListStorage.SEPARATOR + imgName);
			}
		});

		addButton(buttonsComposite, Messages.ScreenshotDialog_SaveAs, new Listener() {
			@Override
			public void handleEvent(Event event) {
				String selectedPath = saveAs();
				if (selectedPath != null) {
					File selected = new File(selectedPath);
					saveImage(image, selected.getParentFile().getAbsolutePath(), selected.getName());
					
					popup.dispose();
				}
			}
		});

		addButton(buttonsComposite, Messages.ScreenshotDialog_SaveAsAndOpen, new Listener() {
			@Override
			public void handleEvent(Event event) {
				String selectedPath = saveAs();
				if (selectedPath != null) {
					File selected = new File(selectedPath);
					saveImage(image, selected.getParentFile().getAbsolutePath(), selected.getName());
					
					popup.dispose();
					
					Program.launch(selectedPath);
				}
				
			}
		});

		addButton(buttonsComposite, Messages.ScreenshotDialog_CopyPath, new Listener() {
			@Override
			public void handleEvent(Event event) {
				saveImage(image, folderPath, imgName);

				Clipboard cb = new Clipboard(display);
				String textData = folderPath + DevicesListStorage.SEPARATOR + imgName;
				cb.setContents(new Object[] { textData }, new Transfer[] { TextTransfer.getInstance() });

				cb.dispose();
				popup.dispose();
			}
		});

		addButton(buttonsComposite,Messages.ScreenshotDialog_CopyImage, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Clipboard cl = new Clipboard(display);
				cl.setContents(new Object[] { image.getImageData() }, new Transfer[] { ImageTransfer.getInstance() });

				cl.dispose();
				popup.dispose();
			}
		});
	}

	private Composite createButtonsComposite() {
		Composite c = new Composite(popup, SWT.NONE);
		c.setLayout(new GridLayout(2, true));
		c.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, true, false));
		c.setBackground(white);
		return c;
	}

	private String saveAs() {
		FileDialog fd = new FileDialog(popup, SWT.SAVE);
		fd.setText(Messages.ScreenshotDialog_SaveAs);
		String[] filterExt = {EXTENSION};
		fd.setFilterExtensions(filterExt);
		fd.setFileName(imgName);
		return fd.open();
	}
	
	private void addButton(final Composite c, final String title, final Listener listener) {
		Button b = new Button(c, SWT.PUSH);
		b.setText(title);
		b.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		b.addListener(SWT.Selection, listener);
	}

	private void saveImage(Image image, String path, String fileName) {
		File f = new File(path);
		f.mkdirs();
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(path + DevicesListStorage.SEPARATOR + fileName, SWT.IMAGE_PNG);
	}
	
}
