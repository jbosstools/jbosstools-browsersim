package org.jboss.tools.browsersim.ui.model;

import org.jboss.tools.browsersim.ui.Messages;

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
