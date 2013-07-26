package org.jboss.tools.vpe.browsersim.model;

import org.jboss.tools.vpe.browsersim.ui.Messages;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public enum TruncateWindow {
	ALWAYS_TRUNCATE(Messages.ManageDevicesDialog_ALWAYS_TRUNCATE),
	NEVER_TRUNCATE(Messages.ManageDevicesDialog_NEVER_TRUNCATE),
	PROMPT(Messages.ManageDevicesDialog_PROMPT);
	
	private String message;

	private TruncateWindow(String value) {
		this.message = value;
	}
	
	public String getMessage() {
		return message;
	}
}
