package org.jboss.tools.vpe.browsersim.ui.skin.ios;

import org.eclipse.swt.layout.FormData;

public class ButtonDescriptor {
	private String enabledImageName;
	private String disabledImageName;
	private String selectedImageName;
	
	/** the width and height fields are ignored*/
	private FormData formData;
	
	/** the width and height fields of formData are ignored*/
	public ButtonDescriptor(FormData formData, String enabledImageName,
			String disabledImageName, String selectedImageName) {
		this.formData = formData;
		this.enabledImageName = enabledImageName;
		this.disabledImageName = disabledImageName;
		this.selectedImageName = selectedImageName;
	}
	
	public String getEnabledImageName() {
		return enabledImageName;
	}
	public String getDisabledImageName() {
		return disabledImageName;
	}
	public String getSelectedImageName() {
		return selectedImageName;
	}
	public FormData getFormData() {
		return formData;
	}
}
