package org.jboss.tools.vpe.browsersim.ui.skin;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

public interface DeviceComposite {

	public abstract ImageButtonComposite getBackButtonComposite();

	public abstract ImageButtonComposite getForwardButtonComposite();

	public abstract Composite getBrowserContainer();

	public abstract void setNavBarCompositeVisible(boolean visible);

	public abstract boolean isNavBarCompositeVisible();

	public abstract ImageButtonComposite getStopButtonComposite();

	public abstract ImageButtonComposite getRefreshButtonComposite();

	public abstract Text getUrlText();

	public abstract ProgressBar getProgressBar();

	public abstract StyledText getPageTitleStyledText();

	public abstract void addListener(int eventType, Listener listener);

	public abstract void removeListener(int eventType, Listener listener);

	public abstract void setMenu(Menu menu);

	ImageButtonComposite getHomeButtonComposite();

	public abstract Menu getMenu();

	public abstract void dispose();

}
