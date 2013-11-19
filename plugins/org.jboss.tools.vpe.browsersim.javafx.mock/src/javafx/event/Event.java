package javafx.event;

import java.util.EventObject;

public class Event extends EventObject implements Cloneable {

	/**
	 * @deprecated the constructor does not exist in JavaFX
	 */
	@Deprecated
	public Event(Object source) {
		super(source);
	}

}
