/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc. - new implementation based on reflection
 *******************************************************************************/
package org.jboss.tools.browsersim.util;

/**
 * 
 * @author Konstantin Marmalyukov (kmarmaliykov)
 *
 */

public class LinuxUtil {
	public static int VERSION(int major, int minor, int micro) {
		return (major << 16) + (minor << 8) + micro;
	}
	
	public static final int gtk_major_version() throws Exception {
		return (Integer)ReflectionUtil.call("org.eclipse.swt.internal.gtk.OS", "gtk_major_version"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static final int gtk_minor_version() throws Exception {
		return (Integer)ReflectionUtil.call("org.eclipse.swt.internal.gtk.OS", "gtk_minor_version"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static final int gtk_micro_version() throws Exception {
		return (Integer)ReflectionUtil.call("org.eclipse.swt.internal.gtk.OS", "gtk_micro_version"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public static final int webkit_major_version() throws Exception {
		return (Integer)ReflectionUtil.call("org.eclipse.swt.internal.webkit.WebKitGTK", "webkit_major_version"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static final int webkit_get_major_version() throws Exception {
		return (Integer)ReflectionUtil.call("org.eclipse.swt.internal.webkit.WebKitGTK", "webkit_get_major_version"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static final int webkit_micro_version() throws Exception {
		return (Integer)ReflectionUtil.call("org.eclipse.swt.internal.webkit.WebKitGTK", "webkit_micro_version"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static final int webkit_get_micro_version() throws Exception {
		return (Integer)ReflectionUtil.call("org.eclipse.swt.internal.webkit.WebKitGTK", "webkit_get_micro_version"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static final int webkit_minor_version() throws Exception {
		return (Integer)ReflectionUtil.call("org.eclipse.swt.internal.webkit.WebKitGTK", "webkit_minor_version"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static final int webkit_get_minor_version() throws Exception {
		return (Integer)ReflectionUtil.call("org.eclipse.swt.internal.webkit.WebKitGTK", "webkit_get_minor_version"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
