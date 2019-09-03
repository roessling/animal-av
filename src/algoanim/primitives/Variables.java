package algoanim.primitives;

import java.util.HashMap;
import java.util.Map.Entry;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.generators.VariablesGenerator;
import algoanim.util.DisplayOptions;
import algoanim.variables.Variable;
import algoanim.variables.VariableContext;

public class Variables extends CountablePrimitive {
	protected VariablesGenerator gen;
	protected VariableContext vars;
	protected HashMap<String, String> list;

	public Variables(VariablesGenerator gen, DisplayOptions display) {
		super(gen, display);
		this.gen = gen;

		vars = new VariableContext();
		list = vars.listContext();
	}

	public void setGlobal(String key) {
		vars.setGlobal(key);
		this.updateView();
	}

	public void openContext() {
		vars = vars.contextOpen();
		this.updateView();
	}

	public void closeContext() {
		vars = vars.contextClose();
		this.updateView();
	}

	public void declare(String type, String key) {
		vars.defineKey(type, key);
		this.updateView();
	}

	public void declare(String type, String key, String value) {
		vars.defineKey(type, key, value);
		this.updateView();
	}

	public void declare(String type, String key, String value, String role) {
		vars.defineKey(type, key, value);
		vars.setRole(key, role);
		this.updateView();
	}

	public void setRole(String key, String value) {
		vars.setRole(key, value);
		this.updateView();
	}

	public void set(String key, String value) {
		vars.setValue(key, value);
		this.updateView();
		notifyObservers(PrimitiveEnum.set);
	}

	public String get(String key) {
		notifyObservers(PrimitiveEnum.get);
		return vars.getString(key);
	}

	public Variable getVariable(String key) {
		notifyObservers(PrimitiveEnum.get);
		return vars.getVariable(key);
	}

	public void discard(String key) {
		vars.deleteKey(key);
		this.updateView();
	}

	protected void updateView() {
		HashMap<String, String> newList = vars.listContext();

		// alte Liste durchgehen
		for (String key : list.keySet()) {
			// alle keys entfernen, die nicht in der neuen auftauchen
			if (!newList.containsKey(key))
				gen.discard(key);
		}

		// neue liste durchgehen
		for (Entry<String, String> entry : newList.entrySet()) {
			// fehlende keys anlegen
			if (!list.containsKey(entry.getKey())) {
			  String s = entry.getKey();
			  Variable v = vars.getVariable(s);
				gen.declare(entry.getKey(), entry.getValue(), v.getRole());
			}
			else
			// geänderte keys updaten
			if (list.get(entry.getKey()) != entry.getValue())
				gen.update(entry.getKey(), entry.getValue(), vars.getVariable(entry.getKey()).getRole());
		}

		// neue liste speichern
		list = newList;
	}
}
