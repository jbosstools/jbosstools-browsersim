/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.NativeSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.AppleIPhone3ResizableSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.AppleIPhone4ResizableSkin;

public class SkinMap {
	public static final  Class<? extends BrowserSimSkin> DEFAULT_SKIN_CLASS = NativeSkin.class;
	private static SkinMap instance;
	private Map<String, Class<? extends BrowserSimSkin>> map;
	
	private SkinMap(){
		map = new HashMap<String, Class<? extends BrowserSimSkin>>();
		
		map.put(AppleIPhone3ResizableSkin.IPHONE3_SKIN_ID, AppleIPhone3ResizableSkin.class);
		map.put(AppleIPhone4ResizableSkin.IPHONE4_SKIN_ID, AppleIPhone4ResizableSkin.class);
//		map.put(AndroidResizableSkin.ANDROID_SKIN_ID, AndroidResizableSkin.class);
	}
	
	public static SkinMap getInstance() {
		if (instance == null) {
			instance = new SkinMap();
		}
		return instance;
	}
	
	public Class<? extends BrowserSimSkin> getSkinClass(String skinId) {
		Class<? extends BrowserSimSkin> skinClass = map.get(skinId);
		if (skinClass == null) {
			skinClass = DEFAULT_SKIN_CLASS;
		}
		return skinClass;
	}
	
	public Set<String> getSkinIds() {
		return map.keySet();
	}
}
