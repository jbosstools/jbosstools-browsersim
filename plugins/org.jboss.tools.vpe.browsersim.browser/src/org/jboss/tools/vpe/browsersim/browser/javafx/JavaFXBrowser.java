package org.jboss.tools.vpe.browsersim.browser.javafx;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swt.FXCanvas;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.PromptData;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.jboss.tools.vpe.browsersim.browser.ExtendedCloseWindowListener;
import org.jboss.tools.vpe.browsersim.browser.ExtendedOpenWindowListener;
import org.jboss.tools.vpe.browsersim.browser.ExtendedVisibilityWindowListener;
import org.jboss.tools.vpe.browsersim.browser.ExtendedWindowEvent;
import org.jboss.tools.vpe.browsersim.browser.IBrowser;
import org.jboss.tools.vpe.browsersim.browser.IBrowserFunction;
import org.jboss.tools.vpe.browsersim.browser.IDisposable;

import com.sun.javafx.scene.web.Debugger;

public class JavaFXBrowser extends FXCanvas implements IBrowser {
	private WebView webView;
	private List<ExtendedCloseWindowListener> closeWindowListeners = new ArrayList<ExtendedCloseWindowListener>();
	private List<LocationListener> locationListeners = new ArrayList<LocationListener>();
	private List<TitleListener> titleListeners = new ArrayList<TitleListener>();
	private List<StatusTextListener> statusTextListeners = new ArrayList<StatusTextListener>();
	private List<ExtendedOpenWindowListener> openWindowListeners = new ArrayList<ExtendedOpenWindowListener>();
	private List<ProgressListener> progressListeners = new ArrayList<ProgressListener>();
	private List<ExtendedVisibilityWindowListener> visibilityWindowListeners = new ArrayList<ExtendedVisibilityWindowListener>();
	
	public JavaFXBrowser(Composite parent) {
		super(parent, SWT.NONE);
		webView = new WebView();

		this.setScene(new Scene(webView));
		
		Debugger debugger = getEngine().impl_getDebugger();

		debugger.setEnabled(true);
		debugger.sendMessage("{\"id\" : -1, \"method\" : \"Network.enable\"}");
		getEngine().getLoadWorker().progressProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				if (oldValue.doubleValue() == 0.0 && newValue.doubleValue() > 0.0) {
					LocationEvent event = new LocationEvent(JavaFXBrowser.this);
					event.widget = JavaFXBrowser.this;
					event.location = getEngine().getLocation();
					event.top = true; // XXX
					for (LocationListener locationListener: locationListeners) {
						locationListener.changed(event);
					}
				}
			}
		});
		
		getEngine().getLoadWorker().progressProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
				ProgressEvent progressEvent = new ProgressEvent(JavaFXBrowser.this);
//				double maximumValue = webView.getEngine().getLoadWorker().getTotalWork();
				double maximumValue = 1.0;
				
				progressEvent.total = 100;
				if (maximumValue > 0.0 && newValue.doubleValue() >= maximumValue) {
					progressEvent.current = progressEvent.total;
					for (ProgressListener progressListener : progressListeners) {
						progressListener.completed(progressEvent);
					}
				} else {
					if (maximumValue <= 0.0) {
						progressEvent.current = progressEvent.total / 2;
					} else {// maximumValue undefined
						progressEvent.current = (int) (newValue.doubleValue() / maximumValue * progressEvent.total);
					}
					for (ProgressListener progressListener : progressListeners) {
						progressListener.changed(progressEvent);
					}
				}
			}
		});
		
		getEngine().titleProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldState, String newState) {
				TitleEvent event = new TitleEvent(JavaFXBrowser.this);
				event.widget = JavaFXBrowser.this;
				event.title = newState != null ? newState : "";
				for (TitleListener titleListener : titleListeners) {
					titleListener.changed(event);
				}
			}
		});
		
		getEngine().setOnAlert(new EventHandler<WebEvent<String>>() {
			@Override
			public void handle(WebEvent<String> event) {
				MessageBox messageBox = new MessageBox(getShell());
				messageBox.setMessage(event.getData());
				messageBox.open();
			}
		});
		
		getEngine().setConfirmHandler(new Callback<String, Boolean>() {
			@Override
			public Boolean call(String message) {
				MessageBox messageBox = new MessageBox(getShell());
				messageBox.setMessage(message);
				return messageBox.open() == SWT.OK;
			}
		});
		
		getEngine().setPromptHandler(new Callback<PromptData, String>() {
			
			@Override
			public String call(PromptData param) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		getEngine().setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {
			@Override
			public WebEngine call(PopupFeatures popupFeatures) {// XXX: use popupFeatures
				ExtendedWindowEvent event = new ExtendedWindowEvent(JavaFXBrowser.this);
				event.widget = JavaFXBrowser.this;
				// TODO: set event.display, etc.
				
				for (ExtendedOpenWindowListener openWindowListener : openWindowListeners) {
					openWindowListener.open(event);
				}
				
				if (event.browser instanceof JavaFXBrowser && !event.browser.isDisposed()) {
					final JavaFXBrowser popupWebViewBrowser = (JavaFXBrowser) event.browser;
					return popupWebViewBrowser.getEngine();
				}
				return null;
			}
		});
		
		getEngine().setOnVisibilityChanged(new EventHandler<WebEvent<Boolean>>() {
			@Override
			public void handle(WebEvent<Boolean> event) {
				boolean shown = event.getData();
				ExtendedWindowEvent extendedWindowEvent = new ExtendedWindowEvent(JavaFXBrowser.this);
				extendedWindowEvent.widget = JavaFXBrowser.this;
				// TODO: set event.display, etc.
				if (shown) {
					// window.open() is called
					for (ExtendedVisibilityWindowListener visibilityWindowListener : visibilityWindowListeners) {
						visibilityWindowListener.show(extendedWindowEvent);
					}
				} else {
					// window.close() is called
					for (ExtendedCloseWindowListener closeWindowListener : closeWindowListeners) {
						if (!isDisposed()) {
							closeWindowListener.close(extendedWindowEvent);
						}
					}
				}
			}
		});
	}
	
	@Override
	public void addCloseWindowListener(ExtendedCloseWindowListener closeWindowListener) {
		closeWindowListeners.add(closeWindowListener);
	}

	@Override
	public void removeCloseWindowListener(ExtendedCloseWindowListener closeWindowListener) {
		closeWindowListeners.remove(closeWindowListener);
	}

	@Override
	public void addLocationListener(final LocationListener locationListener) {
		locationListeners.add(locationListener);
	}

	@Override
	public void removeLocationListener(LocationListener locationListener) {
		locationListeners.remove(locationListener);
	}

	@Override
	public void addOpenWindowListener(ExtendedOpenWindowListener openWindowListener) {
		openWindowListeners.add(openWindowListener);
	}

	@Override
	public void removeOpenWindowListener(ExtendedOpenWindowListener listener) {
		openWindowListeners.remove(listener);
	}

	@Override
	public void addProgressListener(ProgressListener progressListener) {
		progressListeners.add(progressListener);
	}
	
	@Override
	public void removeProgressListener(ProgressListener progressListener) {
		progressListeners.remove(progressListener);
	}

	@Override
	public void addStatusTextListener(StatusTextListener statusTextListener) {
		statusTextListeners.add(statusTextListener);
	}

	@Override
	public void removeStatusTextListener(StatusTextListener statusTextListener) {
		statusTextListeners.remove(statusTextListeners);
	}

	@Override
	public void addTitleListener(TitleListener titleListener) {
		titleListeners.add(titleListener);
	}
	
	@Override
	public void removeTitleListener(TitleListener titleListener) {
		titleListeners.remove(titleListener);
	}
	
	@Override
	public void addVisibilityWindowListener(ExtendedVisibilityWindowListener listener) {
		visibilityWindowListeners.add(listener);		
	}

	@Override
	public void removeVisibilityWindowListener(ExtendedVisibilityWindowListener listener) {
		visibilityWindowListeners.remove(listener);
	}

	@Override
	public boolean back() {
		boolean success = isBackEnabled();
		if (success) {
			getEngine().getHistory().go(-1);
		}
		return success;
	}

	@Override
	public boolean forward() {
		boolean success = isForwardEnabled();
		if (success) {
			getEngine().getHistory().go(1);
		}
		return success;
	}

	@Override
	public void refresh() {
		getEngine().reload();
	}

	@Override
	public void stop() {
		execute("window.stop()");
	}

	@Override
	public Object evaluate(String script) {
		return getEngine().executeScript("(function(){" + script + "}())");
	}

	@Override
	public boolean execute(String string) {
		try {
			getEngine().executeScript(string);
			return true;
		} catch (JSException e) {
			return false;
		}
	}

	@Override
	public IDisposable registerBrowserFunction(final String name, final IBrowserFunction iBrowserFunction) {
		JSObject window = (JSObject) evaluate("return window");
		
		final String id = "__webViewProxy_" + name;
		window.setMember(id, new JavaFXBrowserFunctionProxy(iBrowserFunction));
		evaluate("window['" + name + "'] = function(){return window['" + id + "'].func(arguments)}");
		
		return new IDisposable() {
			@Override
			public void dispose() {
				evaluate("delete window['" + name + "']; delete window['" + id + "']");
			}
			
			@Override
			public boolean isDisposed() {
				return (Boolean) evaluate("return window['" + name + "'] === undefined && window['" + id + "'] === undefined");
			}
		}; 
	}

	public Debugger getDebugger() {
		return getEngine().impl_getDebugger();
	}

	@Override
	public String getText() {
		String doctypeScript = 
			"var node = document.doctype;" +
			"var doctypeText = \"<!DOCTYPE \"" +
			         "+ node.name" + 
			         "+ (node.publicId ? ' PUBLIC \"' + node.publicId + '\"' : '')" +
			         "+ (!node.publicId && node.systemId ? ' SYSTEM' : '')" + 
			         "+ (node.systemId ? ' \"' + node.systemId + '\"' : '')" +
			         "+ '>';" +
			 "return doctypeText";
		String doctypeText = (String) evaluate(doctypeScript);
		String innerHtml = (String) evaluate("return window.document.documentElement.innerHTML");
		return doctypeText + '\n' + innerHtml;
	}

	@Override
	public String getUrl() {
		return getEngine().getLocation();
	}

	@Override
	public boolean isBackEnabled() {
		return getEngine().getHistory().getCurrentIndex() > 0;
	}

	@Override
	public boolean isForwardEnabled() {
		return getEngine().getHistory().getCurrentIndex() + 1 < getEngine().getHistory().getEntries().size();  
	}

	@Override
	public void setUserAgent(String userAgent) {
		if (userAgent == null) {
			userAgent = ""; // empty string means 'default value' for DevTools
		}
		String escapedUserAgent = userAgent
				.replace("\\", "\\\\")
				.replace("\"", "\\\"");
		getDebugger().sendMessage("{\"id\" : -1, \"method\" : \"Network.setUserAgentOverride\","
				+ "\"params\" : { "
				+ 		"\"userAgent\" : \""+ escapedUserAgent +"\""
				+ "}}");
	}

	@Override
	public boolean setUrl(String location) {
		location = location.trim();
		if (!location.contains(":")) {
			location = "http://" + location;
		}
		getEngine().load(location);

		return true; //XXX
	}
	
	protected WebEngine getEngine() {
		return webView.getEngine();
	}
}
