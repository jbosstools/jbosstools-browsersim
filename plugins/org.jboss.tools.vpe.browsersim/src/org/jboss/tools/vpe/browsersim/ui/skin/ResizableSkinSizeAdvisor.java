package org.jboss.tools.vpe.browsersim.ui.skin;

import org.eclipse.swt.graphics.Point;

public interface ResizableSkinSizeAdvisor {
	public Point checkWindowSize(int orientation, Point prefferedSize, Point prefferedShellSize); 
}
