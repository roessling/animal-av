package animal.editor;

import java.util.Vector;

public abstract class SpecialSelector {
	protected Editor parentEditor;
	protected String baseMethodName;
	protected boolean allowMultipleSelection = false;
	protected String effectiveMethodName;
	protected Vector<MethodSelectionListener> listeners = 
		new Vector<MethodSelectionListener>(20);
	
	public SpecialSelector(Editor callingEditor, String methodBaseName,
			boolean enableMultipleMode) {
		setParentEditor(callingEditor);
		setBaseMethodName(methodBaseName);
		setMultipleSelectionMode(enableMultipleMode);
	}
	
	public void addMethodSelectionListener(MethodSelectionListener listener) {
		if (listener != null && !listeners.contains(listener))
			listeners.add(listener);
	}
	
	public void removeMethodSelectionListener(MethodSelectionListener listener) {		
		if (listener != null && listeners.contains(listener))
			listeners.remove(listener);
	}
	
	public void setParentEditor(Editor callingEditor) {
		parentEditor = callingEditor;
	}
	
	public void setBaseMethodName(String methodBaseName) {
		baseMethodName = methodBaseName;
	}
	
	public void setMultipleSelectionMode(boolean enableMultipleMode) {
		allowMultipleSelection = enableMultipleMode;
	}
	
	public String getEffectiveMethodName() {
		return effectiveMethodName;
	}
	
	protected void notifyListeners(String newMethodName) {
		for (MethodSelectionListener listener : listeners) {
			listener.addMethodSelection(newMethodName);
			listener.selectMethodSelection(newMethodName);
		}
	}
}
