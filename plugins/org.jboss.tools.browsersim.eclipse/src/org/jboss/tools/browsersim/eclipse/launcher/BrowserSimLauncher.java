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
package org.jboss.tools.browsersim.eclipse.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.tools.browsersim.browser.PlatformUtil;
import org.jboss.tools.browsersim.eclipse.Activator;
import org.jboss.tools.browsersim.eclipse.Messages;
import org.jboss.tools.browsersim.eclipse.callbacks.OpenFileCallback;
import org.jboss.tools.browsersim.eclipse.callbacks.RestartCallback;
import org.jboss.tools.browsersim.eclipse.callbacks.ViewSourceCallback;
import org.jboss.tools.browsersim.eclipse.preferences.BrowserSimPreferencesPage;
import org.jboss.tools.browsersim.eclipse.preferences.PreferencesUtil;
import org.jboss.tools.browsersim.util.BrowserSimUtil;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 * @author Ilya Buziuk (ibuziuk)
 */
public class BrowserSimLauncher {
	public static final String BROWSERSIM_CLASS_NAME = "org.jboss.tools.browsersim.BrowserSimRunner"; //$NON-NLS-1$
	public static final List<ExternalProcessCallback> BROWSERSIM_CALLBACKS = Arrays.asList(
		new ViewSourceCallback(),
		new OpenFileCallback(),
		new RestartCallback()
	);
	
	public static final List<String> RESOURCES_BUNDLES = Collections.<String>emptyList();
	
	public static final String NOT_STANDALONE = "-not-standalone"; //$NON-NLS-1$
	
	public static void launchBrowserSim(String initialUrl) {
		Activator.getDefault().countLaunchEvent();
		List<String> parameters = new ArrayList<String>();
		
		parameters.add(NOT_STANDALONE);
		if (initialUrl != null) {
			parameters.add(initialUrl);
		}
		
		IVMInstall jvm = getSelectedVM();
		if (jvm == null) {// no suitable vm
			ExternalProcessLauncher.showErrorDialog(Messages.BrowserSim);
		} else {
			String jvmPath = jvm.getInstallLocation().getAbsolutePath();
			String jrePath = jvm.getInstallLocation().getAbsolutePath() + File.separator + "jre"; //$NON-NLS-1$
			
			List<String> bundles = getBundles();
			
			if (!ExternalProcessLauncher.isGTK2()
					|| (!BrowserSimUtil.isJavaFxAvailable(jvmPath) && !BrowserSimUtil.isJavaFxAvailable(jrePath))) {
				bundles.add("org.jboss.tools.browsersim.javafx.mock"); //$NON-NLS-1$
			}
			
			ExternalProcessLauncher.launchAsExternalProcess(bundles, RESOURCES_BUNDLES, getJettyBundles(),
					BROWSERSIM_CALLBACKS, BROWSERSIM_CLASS_NAME, parameters, Messages.BrowserSim, jvm);
		}
	}
	
	public static List<String> getBundles() {
		List<String> bundles = new ArrayList<String>();
		bundles.add("org.jboss.tools.browsersim"); //$NON-NLS-1$
		bundles.add("org.jboss.tools.browsersim.browser"); //$NON-NLS-1$
		bundles.add("org.jboss.tools.browsersim.debugger");  //$NON-NLS-1$
		
		bundles.add("org.eclipse.swt"); //$NON-NLS-1$
		bundles.add("org.eclipse.swt."+ PlatformUtil.CURRENT_PLATFORM); //$NON-NLS-1$

		bundles.add("javax.servlet"); //$NON-NLS-1$
		return bundles;
	}
	
	public static List<String> getJettyBundles() {
		List<String> bundles = new ArrayList<String>();
		
		bundles.add("org.eclipse.jetty.server"); //$NON-NLS-1$
		bundles.add("org.eclipse.jetty.servlet"); //$NON-NLS-1$
		bundles.add("org.eclipse.jetty.websocket"); //$NON-NLS-1$
		bundles.add("org.eclipse.jetty.util"); //$NON-NLS-1$
		bundles.add("org.eclipse.jetty.http"); //$NON-NLS-1$
		bundles.add("org.eclipse.jetty.io"); //$NON-NLS-1$
		bundles.add("org.eclipse.jetty.security"); //$NON-NLS-1$
		bundles.add("org.eclipse.jetty.continuation"); //$NON-NLS-1$

		return bundles;
	}
	
	public static IVMInstall getSelectedVM() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		IVMInstall jvm = null;
		if (IPreferenceStore.FALSE.equals(store.getString(BrowserSimPreferencesPage.BROWSERSIM_JVM_AUTOMATICALLY))) {
			// path to browserSim jvm is located in preferences
			String jvmId = store.getString(BrowserSimPreferencesPage.BROWSERSIM_JVM_ID);
				jvm = PreferencesUtil.getJVM(jvmId);
		} else {
			// detect jvm automatically
			List<IVMInstall> jvms = PreferencesUtil.getSuitableJvms(1);
			if (!jvms.isEmpty()) {
				jvm = jvms.get(0);
			}
		}
		return jvm;
	}
}
