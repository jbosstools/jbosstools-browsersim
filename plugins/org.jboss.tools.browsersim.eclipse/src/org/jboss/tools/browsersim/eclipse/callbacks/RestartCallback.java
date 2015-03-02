package org.jboss.tools.browsersim.eclipse.callbacks;

import java.io.IOException;

import org.jboss.tools.browsersim.eclipse.launcher.BrowserSimLauncher;
import org.jboss.tools.browsersim.eclipse.launcher.ExternalProcessCallback;
import org.jboss.tools.browsersim.eclipse.launcher.TransparentReader;

public class RestartCallback implements ExternalProcessCallback{
	private static final String RESTART_COMMAND = "org.jboss.tools.browsersim.command.restart:"; //$NON-NLS-1$
	
	@Override
	public String getCallbackId() {
		return RESTART_COMMAND;
	}

	@Override
	public void call(String lastString, TransparentReader reader) throws IOException {
		final String url = lastString.substring(RESTART_COMMAND.length());
		BrowserSimLauncher.launchBrowserSim(url);
	}

}
