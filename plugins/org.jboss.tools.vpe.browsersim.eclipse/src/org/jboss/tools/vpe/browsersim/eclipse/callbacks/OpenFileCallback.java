/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.eclipse.callbacks;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.vpe.browsersim.eclipse.Activator;
import org.jboss.tools.vpe.browsersim.eclipse.Messages;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.ExternalProcessCallback;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.TransparentReader;


/**
 * Handler for the BrowserSim commands printed to the console in the following form:
 * <pre>org.jboss.tools.vpe.browsersim.ui.BrowserSim.command.openFile:file:///path/to/file</code>
 * 
 * @author Yahor Radtsevich (yradtsevich)
 * @author Ilya Buziuk (ibuziuk)
 */
public class OpenFileCallback implements ExternalProcessCallback {
	private static final String OPEN_FILE_COMMAND = "org.jboss.tools.vpe.browsersim.command.openFile:"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.browsersim.eclipse.callbacks.BrowserSimCallback#getCallbackId()
	 */
	@Override
	public String getCallbackId() {
		return OPEN_FILE_COMMAND;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.browsersim.eclipse.callbacks.BrowserSimCallback#callback(java.io.InputStreamReader)
	 */
	@Override
	public void call(final String lastString, TransparentReader reader) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				String stringToParse = lastString;
				stringToParse = stringToParse.trim();
				
				String fileNameToOpen = stringToParse.substring(OPEN_FILE_COMMAND.length());
				File fileToOpen = new File(fileNameToOpen);

				if (fileToOpen.exists() && fileToOpen.isFile()) {
					IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					IWorkbenchPage page = window != null ? window.getActivePage() : null;

					if (page != null) {
						try {
							IDE.openEditorOnFileStore(page, fileStore);
						} catch (PartInitException e) {
							Activator.logError(e.getMessage(), e);
						}
					} else {
						Exception e = new Exception(Messages.Callback_CANNOT_OBTAIN_PAGE);
						Activator.logError(e.getMessage(), e);
					}
				} else {
					FileNotFoundException e = new FileNotFoundException(MessageFormat.format(Messages.OpenFileCallback_CANNOT_OPEN_FILE, fileNameToOpen));
					Activator.logError(e.getMessage(), e);
				}
			}
		});
	}
}