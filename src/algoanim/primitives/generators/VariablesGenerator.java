package algoanim.primitives.generators;

import animal.variables.VariableRoles;

public interface VariablesGenerator extends GeneratorInterface {
	public static final String DECLARE = "declare";
	public static final String SET = "update";
	public static final String DISCARD = "discard";
	public static final String EVAL = "eval";

	public void declare(String key);
	public void declare(String key, String value);
  public void declare(String key, String value, VariableRoles role);
  public void update(String key, String value);
	public void update(String key, String value, VariableRoles newRole);
	public void discard(String key);
}
