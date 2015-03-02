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
package org.jboss.tools.browsersim.ui.skin;
import org.eclipse.swt.SWT;

public class WidgetDescriptor {
	protected int horizontalSpan;
	protected int verticalSpan;
	protected int stretching;

	public WidgetDescriptor(int horizontalSpan, int verticalSpan, int stretching) {
		this.verticalSpan = verticalSpan;
		this.horizontalSpan = horizontalSpan;
		this.stretching = stretching;
	}
	
	public WidgetDescriptor(int horizontalSpan, int verticalSpan) {
		this(horizontalSpan, verticalSpan, SWT.NONE);
	}
	public WidgetDescriptor() {
		this(1, 1);
	}
	
	public int getHorizontalSpan() {
		return horizontalSpan;
	}
	public int getVerticalSpan() {
		return verticalSpan;
	}
	public int getStretching() {
		return stretching;
	}
}
