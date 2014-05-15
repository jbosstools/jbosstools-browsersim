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
 *******************************************************************************/
package org.jboss.tools.vpe.browsersim.util;

import org.eclipse.swt.internal.Platform;

@SuppressWarnings("restriction")
public class LinuxUtil {
	public static int VERSION(int major, int minor, int micro) {
		return (major << 16) + (minor << 8) + micro;
	}
	
	/** @method flags=const */
	public static final native int _gtk_major_version();

	public static final int gtk_major_version() {
		Platform.lock.lock();
		try {
			return _gtk_major_version();
		} finally {
			Platform.lock.unlock();
		}
	}

	/** @method flags=const */
	public static final native int _gtk_minor_version();

	public static final int gtk_minor_version() {
		Platform.lock.lock();
		try {
			return _gtk_minor_version();
		} finally {
			Platform.lock.unlock();
		}
	}

	/** @method flags=const */
	public static final native int _gtk_micro_version();

	public static final int gtk_micro_version() {
		Platform.lock.lock();
		try {
			return _gtk_micro_version();
		} finally {
			Platform.lock.unlock();
		}
	}
	
	/** @method flags=dynamic */
	public static final native int _webkit_major_version();

	public static final int webkit_major_version() {
		Platform.lock.lock();
		try {
			return _webkit_major_version();
		} finally {
			Platform.lock.unlock();
		}
	}

	/** @method flags=dynamic */
	public static final native int _webkit_get_major_version();

	public static final int webkit_get_major_version() {
		Platform.lock.lock();
		try {
			return _webkit_get_major_version();
		} finally {
			Platform.lock.unlock();
		}
	}

	/** @method flags=dynamic */
	public static final native int _webkit_micro_version();

	public static final int webkit_micro_version() {
		Platform.lock.lock();
		try {
			return _webkit_micro_version();
		} finally {
			Platform.lock.unlock();
		}
	}

	/** @method flags=dynamic */
	public static final native int _webkit_get_micro_version();

	public static final int webkit_get_micro_version() {
		Platform.lock.lock();
		try {
			return _webkit_get_micro_version();
		} finally {
			Platform.lock.unlock();
		}
	}

	/** @method flags=dynamic */
	public static final native int _webkit_minor_version();

	public static final int webkit_minor_version() {
		Platform.lock.lock();
		try {
			return _webkit_minor_version();
		} finally {
			Platform.lock.unlock();
		}
	}

	/** @method flags=dynamic */
	public static final native int _webkit_get_minor_version();

	public static final int webkit_get_minor_version() {
		Platform.lock.lock();
		try {
			return _webkit_get_minor_version();
		} finally {
			Platform.lock.unlock();
		}
	}
}
