package org.jboss.tools.browsersim.browser.javafx;

import org.jboss.tools.browsersim.browser.IBrowserFunction;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

public class JavaFXBrowserFunctionProxy {
	private IBrowserFunction browserFunction;
	
    public JavaFXBrowserFunctionProxy(IBrowserFunction browserFunction) {
		this.browserFunction = browserFunction;
	}

	public Object func(JSObject arguments) {
		int length = (Integer) arguments.getMember("length");
		Object[] argumentsArray = new Object[length];
		for (int i = 0; i < length; i++) {
			Object argument = arguments.getSlot(i);
			if (argument instanceof Integer) {
				// JavaFX WebView may pass Integer for numbers, but SWT Browser
				// always passes Double - make this to be uniform
				argumentsArray[i] = ((Integer) argument).doubleValue();
			} else if ("undefined".equals(argument)) {
				// JavaFX WebView passes "undefined" for JS undefined, but SWT Browser passes null
				argumentsArray[i] = null;
			} else {
				argumentsArray[i] = arguments.getSlot(i);
			}
		}
		
		try {
			return browserFunction.function(argumentsArray);
		} catch (Exception e) {
			return new JSException(e.getMessage());
		}
    }
}
