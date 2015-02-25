package org.jboss.tools.browsersim.ui;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

/**
 * @author kmarmaliykov
 */
final class VerifyDigitsListener implements VerifyListener {
	public void verifyText(VerifyEvent e) {
		for (char c : e.text.toCharArray()) {
			if (!('0' <= c && c <= '9')) {
				e.doit = false;
				return;
			}
		}
	}
}