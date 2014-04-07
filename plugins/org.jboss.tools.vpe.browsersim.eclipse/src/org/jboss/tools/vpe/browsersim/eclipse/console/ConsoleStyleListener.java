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
package org.jboss.tools.vpe.browsersim.eclipse.console;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Display;

/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class ConsoleStyleListener implements LineStyleListener {
	private static final String JS_WARN_PREFIX = "!JavaScript WARN:"; //$NON-NLS-1$
	private static final String JS_INFO_PREFIX = "!JavaScript INFO:"; //$NON-NLS-1$

	@Override
	public void lineGetStyle(LineStyleEvent event) {
		String message = event.lineText;
		if (event == null || message == null || message.length() == 0) {
			return;
		}

		List<StyleRange> styles = new ArrayList<StyleRange>();

		if (message.startsWith(JS_INFO_PREFIX)) {
			styles.add(new StyleRange(event.lineOffset, message.length(), Display.getDefault().getSystemColor(SWT.COLOR_BLUE), null));
			setStyles(event, styles);
		} else if (message.startsWith(JS_WARN_PREFIX)) {
			styles.add(new StyleRange(event.lineOffset, message.length(), Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW), null));
			setStyles(event, styles);
		}
	}

	private void setStyles(LineStyleEvent event, List<StyleRange> styles) {
		event.styles = styles.toArray(new StyleRange[0]);
	}

}