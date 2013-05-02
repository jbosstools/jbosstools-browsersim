package org.jboss.tools.vpe.browsersim.eclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.tools.vpe.browsersim.eclipse.Activator;

public class BrowserSimPreferencesInitializer extends AbstractPreferenceInitializer{
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(BrowserSimPreferencesPage.BROWSERSIM_JVM_AUTOMATICALLY, IPreferenceStore.TRUE);
	}
}
