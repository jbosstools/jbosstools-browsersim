package org.jboss.tools.vpe.browsersim.eclipse;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.jboss.tools.vpe.browsersim.ui.BrowserSim;

public class BrowsersimApplication implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		BrowserSim.main(new String[] {});
		return IApplication.EXIT_OK;
	}

	public void stop() {
	}

}
