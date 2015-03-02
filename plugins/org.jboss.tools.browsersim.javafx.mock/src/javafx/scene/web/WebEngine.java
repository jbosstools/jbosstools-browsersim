package javafx.scene.web;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.util.Callback;

import com.sun.javafx.scene.web.Debugger;

public class WebEngine {
	public final Worker<Void> getLoadWorker() { 
		return null;
	}

	public Debugger impl_getDebugger() {
		return null;
	}

	public String getLocation() {
		return null;
	}

	public final ReadOnlyStringProperty titleProperty() {
		return null;
	}

	public void setOnAlert(EventHandler<WebEvent<String>> eventHandler) { }

	public void setConfirmHandler(Callback<String, Boolean> callback) { }

	public void setPromptHandler(Callback<PromptData, String> callback) { }

	public void setCreatePopupHandler(Callback<PopupFeatures, WebEngine> callback) { }

	public void setOnVisibilityChanged(EventHandler<WebEvent<Boolean>> eventHandler) { }

	public Object executeScript(String string) { return null; }

	public WebHistory getHistory() { return null; }

	public void reload() { }

	public void load(String location) { } 
}
