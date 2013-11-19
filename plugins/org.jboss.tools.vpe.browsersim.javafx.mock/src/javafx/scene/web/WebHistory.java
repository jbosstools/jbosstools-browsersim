package javafx.scene.web;

import javafx.collections.ObservableList;

public class WebHistory {
	public void go(int offset) throws IndexOutOfBoundsException { }

	public int getCurrentIndex() { return 0; }

	public ObservableList<Entry> getEntries() {
		return null;
	}
	
	public final class Entry { }
}
