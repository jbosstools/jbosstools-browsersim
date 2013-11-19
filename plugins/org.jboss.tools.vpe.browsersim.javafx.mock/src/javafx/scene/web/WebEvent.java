package javafx.scene.web;

import javafx.event.Event;

final public class WebEvent<T> extends Event {

	/**
	 * @deprecated this constructor does not exist in JavaFX
	 */
	@Deprecated
	public WebEvent(Object source) {
		super(source);
	}
	
	public T getData() { return null;}
}
