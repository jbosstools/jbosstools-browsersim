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
package org.jboss.tools.browsersim.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
	public static Object call(String className, String methodName)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = Class.forName(className);
		Object o = clazz.newInstance();
		Method method = clazz.getDeclaredMethod(methodName);
		return method.invoke(o);
	}
}
