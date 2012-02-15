package org.jboss.tools.vpe.browsersim.ui.skin.ios;
import org.eclipse.swt.SWT;

class WidgetDescriptor {
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
