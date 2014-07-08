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
package org.jboss.tools.vpe.browsersim.eclipse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.tools.usage.event.UsageEventType;
import org.jboss.tools.usage.event.UsageReporter;
import org.jboss.tools.vpe.browsersim.eclipse.preferences.PreferencesUtil;
import org.jboss.tools.vpe.browsersim.model.preferences.BrowserSimSpecificPreferencesStorage;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferences;
import org.jboss.tools.vpe.browsersim.model.preferences.SpecificPreferencesStorage;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author "Yahor Radtsevich (yradtsevich)"
 * @author Ilya Buziuk (ibuziuk)
 */
public class Activator extends AbstractUIPlugin {
	
	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.vpe.browsersim.eclipse"; //$NON-NLS-1$

	private static final String BROWSERSIM_ACTION = "browsersim"; //$NON-NLS-1$
	private static final String JAVA_FX_LABEL = "javafx"; //$NON-NLS-1$
	private static final String WEBKIT_LABEL = "webkit"; //$NON-NLS-1$

	private Map<StyledText, IConsolePageParticipant> viewers = new HashMap<StyledText, IConsolePageParticipant>();

	// The shared instance
	private static Activator plugin;

	private UsageEventType launchEventType;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		launchEventType = new UsageEventType(this, BROWSERSIM_ACTION, Messages.UsageEventTypeLaunchLabelDescription, UsageEventType.HOW_MANY_TIMES_VALUE_DESCRIPTION);
		UsageReporter.getInstance().registerEvent(launchEventType);
	}

	public void countLaunchEvent() {
		try {
			String label = getEngineName(BrowserSimSpecificPreferencesStorage.INSTANCE);
			UsageReporter.getInstance().countEvent(launchEventType.event(label));
		} catch (URISyntaxException e) {
			Activator.logError(e.getMessage(), e);
		} catch (IOException e) {
			Activator.logError(e.getMessage(), e);
		}
	}

	public static String getEngineName(SpecificPreferencesStorage storage) throws URISyntaxException, IOException {
		String configFolder = PreferencesUtil.getBrowserSimConfigFolderPath();
		SpecificPreferences sp = (SpecificPreferences) storage.load(configFolder);
		if (sp == null) {
			sp = (SpecificPreferences) storage.loadDefault();
		}
		return sp.isJavaFx()?JAVA_FX_LABEL:WEBKIT_LABEL;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public static void logError(String message, Throwable throwable) {
		logError(message, throwable, PLUGIN_ID);
	}
	
	public static void logError(String message, Throwable throwable, String pluginId) {
		getDefault().getLog().log(new Status(IStatus.ERROR, pluginId, message, throwable));
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Return a <code>java.io.File</code> object that corresponds to the specified
	 * <code>IPath</code> in the plug-in directory.
	 * 
	 * @param path the path to look for in the launching bundle
	 * @return the {@link File} from the bundle or <code>null</code>
	 */
	public static File getFileInPlugin(IPath path) {
		try {
			URL installURL =
				new URL(getDefault().getBundle().getEntry("/"), path.toString()); //$NON-NLS-1$
			URL localURL = FileLocator.toFileURL(installURL);
			return new File(localURL.getFile());
		} catch (IOException ioe) {
			return null;
		}
	}
	
	public void addViewer(StyledText viewer, IConsolePageParticipant participant) {
		viewers.put(viewer, participant);
	}

	public void removeViewerWithPageParticipant(IConsolePageParticipant participant) {
		Set<StyledText> toRemove = new HashSet<StyledText>();

		for (StyledText viewer : viewers.keySet()) {
			if (viewers.get(viewer) == participant)
				toRemove.add(viewer);
		}

		for (StyledText viewer : toRemove)
			viewers.remove(viewer);
	}
	
}