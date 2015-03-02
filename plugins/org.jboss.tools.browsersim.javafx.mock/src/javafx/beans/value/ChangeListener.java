package javafx.beans.value;

public interface ChangeListener<T> {
	void changed(ObservableValue<? extends T> observable, T oldValue, T newValue); 
}
