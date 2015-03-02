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
package org.jboss.tools.browsersim.ui;

import org.jboss.tools.browsersim.ui.model.preferences.CommonPreferences;
import org.jboss.tools.browsersim.ui.model.preferences.SpecificPreferences;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class PreferencesWrapper {
	private CommonPreferences commonPreferences;
	private SpecificPreferences specificPreferences;
	
	public PreferencesWrapper(CommonPreferences commonPreferences, SpecificPreferences specificPreferences) {
		this.commonPreferences = commonPreferences;
		this.specificPreferences = specificPreferences;
	}

	public CommonPreferences getCommonPreferences() {
		return commonPreferences;
	}

	public SpecificPreferences getSpecificPreferences() {
		return specificPreferences;
	}
}
