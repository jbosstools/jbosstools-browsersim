package org.jboss.tools.vpe.browsersim.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.vpe.browsersim.ui.skin.BrowserSimSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.NativeSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.android.AndroidResizableSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.AppleIPhone3ResizableSkin;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.AppleIPhone4ResizableSkin;

public class SkinMap {
	public static final  Class<? extends BrowserSimSkin> DEFAULT_SKIN_CLASS = NativeSkin.class;
	private static SkinMap instance;
	private Map<String, Class<? extends BrowserSimSkin>> map;
	
	private SkinMap(){
		map = new HashMap<String, Class<? extends BrowserSimSkin>>();
		
		map.put("iPhone 3", AppleIPhone3ResizableSkin.class);
		map.put("iPhone 4", AppleIPhone4ResizableSkin.class);
		map.put("Android", AndroidResizableSkin.class);
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
