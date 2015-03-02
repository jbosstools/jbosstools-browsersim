package netscape.javascript;

public abstract class JSObject {
	public abstract Object getMember(String name) throws JSException;
	public abstract Object getSlot(int index) throws JSException;
	public void setMember(String id, Object object) { }
}
