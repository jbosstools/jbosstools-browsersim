/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.js.log;

import org.eclipse.swt.browser.BrowserFunction;
import org.jboss.tools.browsersim.browser.IBrowser;
import org.jboss.tools.browsersim.browser.IBrowserFunction;

/**
 * {@link BrowserFunction} for processing main js console methods (log, error,warn, info) </br> 
 *  and logging relevant messages to the eclipse console
 * 
 * @author Ilya Buziuk (ibuziuk)
 */
public class JsLogFunction implements IBrowserFunction {
	private static final String JS_LOG_PREFIX = "!JavaScript"; //$NON-NLS-1$
	private MessageType type;

	public JsLogFunction(IBrowser browser, MessageType type) {
		this.type = type;
	}

	@Override
	public Object function(Object[] arguments) {
		addTypeInfo(arguments, type);
		if (arguments != null && arguments.length >= 1) {
			StringBuffer message = new StringBuffer();
			
			for (Object argument : arguments) {
				message.append(argument.toString() + " "); //$NON-NLS-1$
			}
			JsConsoleLogger.log(message.toString(), type);
		}
		return null;
	}

	public void addTypeInfo(Object[] arguments, MessageType type) {
		if (type != null && arguments != null && arguments.length >= 1) {
			arguments[0] = JS_LOG_PREFIX + " " + type.toString() + ": " + arguments[0]; //$NON-NLS-1$ //$NON-NLS-2$
		} 
	}
	
}