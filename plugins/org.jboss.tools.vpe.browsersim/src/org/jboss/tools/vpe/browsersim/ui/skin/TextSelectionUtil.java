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

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class TextSelectionUtil {
	public static void addSelectTextOnFocusListener(final Text targetText) {
		SelectTextListener selectTextListener;
		if (PlatformUtil.OS_MACOSX.equals(PlatformUtil.getOs())) {
			selectTextListener = new OsXSelectTextListenerImpl();
		} else {
			selectTextListener = new SelectTextListenerImpl();
		}
		
		targetText.addFocusListener(selectTextListener);
		targetText.addMouseListener(selectTextListener);
	}
}

interface SelectTextListener extends FocusListener, MouseListener {
}

class SelectTextListenerImpl extends MouseAdapter implements SelectTextListener {

	@Override
	public void focusGained(FocusEvent e) {
		asyncSelectAllText((Text) e.widget, e.display);
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		asyncClearSelectionText((Text) e.widget, e.display);
	};
	
	protected void asyncSelectAllText(final Text text, Display display) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!text.isDisposed()) {
					text.selectAll();
				}
			}
		});
	}
	
	protected void asyncClearSelectionText(final Text text, Display display) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!text.isDisposed()) {
					text.clearSelection();
				}
			}
		});
	}
}

class OsXSelectTextListenerImpl extends SelectTextListenerImpl {
	private boolean focusJustGained = false;

	@Override
	public void mouseUp(MouseEvent e) {
		if (focusJustGained) {
			asyncSelectAllText((Text) e.widget, e.display);
			focusJustGained = false;
		}
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		focusJustGained = true;
	}

	@Override
	public void focusLost(FocusEvent e) {
		focusJustGained = false;
	}
}
