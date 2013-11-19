package javafx.concurrent;

import javafx.beans.property.ReadOnlyDoubleProperty;

public interface Worker<V> {
	public ReadOnlyDoubleProperty progressProperty(); 
}
