/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.eclipse.preferences;

public class ArchitectureDetector {
	/**
	 * Prints system properties to standard out.
	 * <ul>
	 * <li>os.arch</li>
	 * </ul>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print(System.getProperty("os.arch")); //$NON-NLS-1$
	}
}