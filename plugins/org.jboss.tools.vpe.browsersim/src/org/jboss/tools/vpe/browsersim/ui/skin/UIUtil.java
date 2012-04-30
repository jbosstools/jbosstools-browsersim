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

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Text;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class UIUtil {
	public static void addSelectTextOnFocusListener(final Text targetText) {
		targetText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				e.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						if (!targetText.isDisposed()) {
							targetText.selectAll();
						}
					}
				});
			}
		});
	}
}
