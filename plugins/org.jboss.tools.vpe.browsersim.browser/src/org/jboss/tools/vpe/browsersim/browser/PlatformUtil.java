/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.browser;

import org.eclipse.swt.SWT;

/**
 * @author "Yahor Radtsevich (yradtsevich)"
 */
@SuppressWarnings("nls")
public class PlatformUtil {

	public static final String ARCH_X86 = "x86";
	public static final String OS_WIN32 = "win32";
	public static final String OS_LINUX = "linux";
	public static final String OS_MACOSX = "macosx";
	public static final String CURRENT_PLATFORM;
	static {
		String os = getOs();
		String ws = getWs();
		String arch = getArch();

		if (OS_MACOSX.equals(os) && ARCH_X86.equals(arch)) {
			CURRENT_PLATFORM = ws + '.' + os; // special case for MacOSX x86 (its SWT bundle has name org.eclipse.swt.cocoa.macosx)
		} else {			
			CURRENT_PLATFORM = ws + '.' + os + '.' + arch;
		}
	}

	/*
	 * Copy of org.eclipse.swt.internal.Library.arch() 
	 */
	public static String getArch() {
		String osArch = System.getProperty("os.arch"); //$NON-NLS-1$
		if (osArch.equals ("i386") || osArch.equals ("i686")) return ARCH_X86; //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
		if (osArch.equals ("amd64")) return "x86_64"; //$NON-NLS-1$ $NON-NLS-2$
		if (osArch.equals ("IA64N")) return "ia64_32"; //$NON-NLS-1$ $NON-NLS-2$
		if (osArch.equals ("IA64W")) return "ia64"; //$NON-NLS-1$ $NON-NLS-2$
		return osArch;
	}

	/*
	 * Copy of org.eclipse.swt.internal.Library.os()
	 */
	public static String getOs() {
		String osName = System.getProperty("os.name"); //$NON-NLS-1$
		if (osName.equals ("Linux")) return OS_LINUX; //$NON-NLS-1$ $NON-NLS-2$
		if (osName.equals ("AIX")) return "aix"; //$NON-NLS-1$ $NON-NLS-2$
		if (osName.equals ("Solaris") || osName.equals ("SunOS")) return "solaris"; //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
		if (osName.equals ("HP-UX")) return "hpux"; //$NON-NLS-1$ $NON-NLS-2$
		if (osName.equals ("Mac OS X")) return OS_MACOSX; //$NON-NLS-1$ $NON-NLS-2$
		if (osName.startsWith ("Win")) return OS_WIN32; //$NON-NLS-1$ $NON-NLS-2$
		return osName;
	}
	
	private static String getWs() {
		return SWT.getPlatform();
	}
}
