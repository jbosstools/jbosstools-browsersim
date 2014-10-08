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
package org.jboss.tools.vpe.browsersim.eclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.tools.vpe.browsersim.eclipse.Activator;

public class BrowserSimPreferencesInitializer extends AbstractPreferenceInitializer{
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(BrowserSimPreferencesPage.BROWSERSIM_JVM_AUTOMATICALLY, IPreferenceStore.TRUE);
		store.setDefault(BrowserSimPreferencesPage.BROWSERSIM_GTK_2, PreferencesUtil.requiresGTK3() ? IPreferenceStore.FALSE : IPreferenceStore.TRUE);
	}
}
