package animal.misc;

import java.util.Hashtable;

import javax.swing.Action;
import javax.swing.JTextField;

public class TextUtilities {
	private static Hashtable<String, Action> actions = null;

	static {
		JTextField textField = new JTextField();
		Action[] textFieldActions = textField.getActions();
		actions = new Hashtable<String, Action>(textFieldActions.length << 1 + 7);
		for (int i = 0; i < textFieldActions.length; i++) {
			Action action = textFieldActions[i];
			actions.put("JTextField." + action.getValue(Action.NAME), action);
		}
	}

	public static Action findTextFieldAction(String key) {
		return findAction(key, "JTextField");
	}

	public static Action findAction(String key, String baseType) {
		return actions.get(baseType + "." + key);
	}

}
