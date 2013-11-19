package javafx.embed.swt;

import javafx.scene.Scene;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class FXCanvas extends Canvas {
	public FXCanvas(Composite parent, int style) {
		super(parent, style);
	}
	
	public void setScene(final Scene newScene) { }
}
