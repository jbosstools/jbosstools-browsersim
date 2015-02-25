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


/**
 * @author Ilya Buziuk (ibuziuk)
 */
public class JsConsoleLogger {
	
	private JsConsoleLogger() {
	}

	public static void log(String message, MessageType type) {
		if (MessageType.ERROR.equals(type)) {
			System.err.println(message);
		} else {
			System.out.println(message);
		}
	}
	
}