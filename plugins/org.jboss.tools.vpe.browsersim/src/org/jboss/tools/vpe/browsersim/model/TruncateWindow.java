package org.jboss.tools.vpe.browsersim.model;
/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public enum TruncateWindow {
	ALWAYS_TRUNCATE("Always truncate"),
	NEVER_TRUNCATE("Never truncate"),
	PROMPT("Prompt");
	
	private String message;

	private TruncateWindow(String value) {
		this.message = value;
	}
	
	public String getMessage() {
		return message;
	}
}
