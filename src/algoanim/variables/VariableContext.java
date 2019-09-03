package algoanim.variables;

import java.util.HashMap;

import animal.variables.VariableRoles;

public class VariableContext {
  /**
   * father and son are references to other contexts
   */
  protected VariableContext         father;
  protected VariableContext         son;

  /**
   * holds the key/value pairs of this context
   */
  private HashMap<String, Variable> variables;

  private static HashMap<String, VariableRoles> roles;
  
  static {
    roles = new HashMap<String, VariableRoles>(23);
    roles.put("unknown", VariableRoles.UNKNOWN);
    roles.put("stepper", VariableRoles.STEPPER);
    roles.put("temporary", VariableRoles.TEMPORARY);
    roles.put("organizer", VariableRoles.ORGANIZER);
    roles.put("fixed value", VariableRoles.FIXED_VALUE);
    roles.put("most-wanted holder", VariableRoles.MOST_WANTED_HOLDER);
    roles.put("most-recent holder", VariableRoles.MOST_RECENT_HOLDER);
    roles.put("one-way flag", VariableRoles.ONE_WAY_FLAG);
    roles.put("follower", VariableRoles.FOLLOWER);
    roles.put("gatherer", VariableRoles.GATHERER);
    roles.put("container", VariableRoles.CONTAINER);
    roles.put("walker", VariableRoles.WALKER);    
  }
  
  /**
   * constructor
   */
  public VariableContext() {
    father = null;
    son = null;
    variables = new HashMap<String, Variable>();
  }

  /**
   * constructor which references an existing context as a father
   * 
   * @param myFather
   */
  public VariableContext(VariableContext myFather) {
    this();
    father = myFather;
  }

  /**
	 * 
	 */
  public VariableContext dropSon() {
    son = null;
    return this;
  }

  /**
   * defines a variable for this context
   * 
   * @param key
   *          name for the variable to define
   */
  public void defineKey(String type, String key) {
    variables.put(key, VariableFactory.newVariable(type));
  }

  public void defineKey(String type, String key, String value) {
    this.defineKey(type, key);
    this.setValue(key, value);
  }

  /**
   * deletes a key
   */
  public void deleteKey(String key) {
    if (variables.containsKey(key))
      variables.remove(key);
    else if (father != null)
      father.deleteKey(key);
  }

  /**
   * returns the variable for a given key. if the variable is not found in the
   * actual context, search is continued in fathers context
   * 
   * @param key
   *          name of the variable
   * @return variable
   */
  public Variable getVariable(String key) {
    if (variables.containsKey(key))
      return variables.get(key);
    else {
      if (father != null)
        return father.getVariable(key);
      else
        return null;
    }
  }

  public Integer getInt(String key) {
    Variable v = getVariable(key);
    if (v != null)
      return getVariable(key).getValue(Integer.class);
    else
      return null;
  }

  public Float getFloat(String key) {
    Variable v = getVariable(key);
    if (v != null)
      return getVariable(key).getValue(Float.class);
    else
      return null;
  }

  public Boolean getBool(String key) {
    Variable v = getVariable(key);
    if (v != null)
      return getVariable(key).getValue(Boolean.class);
    else
      return null;
  }

  public String getString(String key) {
    Variable v = getVariable(key);
    if (v != null)
      return getVariable(key).getValue(String.class);
    else
      return null;
  }

  /**
   * gets a list of all current variables specific to this context
   * 
   * @return list of all variables
   */
  public HashMap<String, String> listContext() {
    HashMap<String, String> res = new HashMap<String, String>();

    for (String key : variables.keySet()) {
      res.put(key, variables.get(key).getValue(String.class));
    }

    if (father != null)
      res.putAll(father.listGlobal());

    return res;
  }

  public HashMap<String, String> listGlobal() {
    HashMap<String, String> res = new HashMap<String, String>();

    for (String key : variables.keySet()) {
      Variable var = variables.get(key);
      if (var.isGlobal())
        res.put(key, var.getValue(String.class));
    }

    if (father != null)
      res.putAll(father.listGlobal());

    return res;
  }

  /**
   * gets a list of all current variables specific to this context
   * 
   * @return list of all variables
   */
  public HashMap<String, String> listAll() {
    if (father == null)
      return this.listContext();

    HashMap<String, String> res = father.listAll();

    for (String key : variables.keySet())
      res.put(key, variables.get(key).getValue(String.class));

    if (father != null)
      res.putAll(father.listGlobal());

    return res;
  }

  /**
   * sets the value for a given variable. if the variable is not found in the
   * actual context, search is continued in fathers context
   * 
   * @param key
   *          name of the variable
   * @param value the value to be set for the key
   */
  public void setValue(String key, String value) {
    if (variables.containsKey(key)) {
      variables.get(key).setValue(value);
    } else if (father != null) {
      father.setValue(key, value);
    } else
      System.err.println("variable not found: " + key);
  }
  
  /**
   * sets the value for a given variable. if the variable is not found in the
   * actual context, search is continued in fathers context
   * 
   * @param key
   *          name of the variable
   * @param value the value to be set for the key
   */
  public void setRole(String key, String value) {
    VariableRoles vRole = roles.get(value.toLowerCase());
    if (vRole == null)
      vRole = VariableRoles.UNKNOWN;
    if (variables.containsKey(key)) {
       variables.get(key).setRole(vRole);
    } else if (father != null) {
      father.setRole(key, value);
    } else
      System.err.println("variable not found: " + key);
  }


  public void setGlobal(String key) {
    if (variables.containsKey(key))
      variables.get(key).setGlobal();
  }

  /**
   * opens a new context (e.g. a function, loop, etc.) and references to it as
   * son
   * 
   * @return the new context
   */
  public VariableContext contextOpen() {
    son = new VariableContext(this);
    return son;
  }

  /**
   * closes given contact and returns to father context
   * 
   * @return father context
   */
  public VariableContext contextClose() {
    return father.dropSon();
  }

  /**
   * @return the actual top-context
   */
  public VariableContext getContext() {
    if (son != null)
      return son.getContext();
    else
      return this;
  }

  /*
   * testbench
   */
  public static void main(String[] args) {
    VariableContext field = new VariableContext();

    field.defineKey("int", "global");
    field.setGlobal("global");
    field.setValue("global", "13");

    field.defineKey("int", "a");
    field.setValue("a", "5");

    field.defineKey("double", "b");
    field.setValue("b", "1.23456");

    field.defineKey("double", "b");
    field.setValue("b", "1.23456");

    System.out.println("erster Context: " + field.listAll());

    field = field.contextOpen();
    field.setValue("a", "3");
    field.defineKey("double", "b");
    field.setValue("b", "6.54321");
    field.defineKey("int", "c");
    field.setValue("c", "6.54321");
    field.setValue("global", "19");

    System.out.println("zweiter Context: " + field.listAll());

    field = field.contextClose();

    System.out.println("zurueck zum ersten: " + field.listAll());
  }
}