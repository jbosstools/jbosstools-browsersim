package javafx.beans.value;

import javafx.beans.Observable;

public interface ObservableValue<T> extends Observable {
	void addListener(ChangeListener<? super T> listener);
}
