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
package org.jboss.tools.browsersim.ui.model.preferences;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public interface PreferencesStorage {
	static final String PREFERENCES_BROWSERSIM = "browserSim"; //$NON-NLS-1$
	
	public void save(Object o);
	
	public Object load(String configFolder);
	
	public Object loadDefault();
}
