package algoanim.variables;

import java.util.Vector;

import animal.variables.VariableRoles;

public abstract class Variable {
  private VariableTypes            type;
  private Boolean                  isGlobal  = false;
  private VariableRoles role;

  private Vector<VariableObserver> observers = new Vector<VariableObserver>();

  public Variable(VariableTypes type) {
    this.type = type;
  }

  public VariableTypes getType() {
    return this.type;
  }

  public String getRoleString() {
    return animal.variables.Variable.getRoleString(role);
  }

  public VariableRoles getRole() {
    return role;
  }

  public void setGlobal() {
    isGlobal = true;
  }

  public Boolean isGlobal() {
    return isGlobal;
  }

  /**
   * abstract setValue functions
   */
  public abstract void setValue(Variable value);

  public abstract void setValue(Boolean value);

  public abstract void setValue(Byte value);

  public abstract void setValue(Double value);

  public abstract void setValue(Float value);

  public abstract void setValue(Integer value);

  public abstract void setValue(Long value);

  public abstract void setValue(Short value);

  public abstract void setValue(String value);
  
  
  public Class<?> getAssociatedClass() {
    return type.getAssociatedClass();
  }

  /**
   * generic getValue method
   * 
   * @param type
   *          the element
   * @return the returned value
   */
  public abstract <T> T getValue(Class<T> type);

  protected void setError(String value) {
    System.err.println("Unable to set '" + value 
        + "', old value persists; role is " +role);
  }


  public void setRole(VariableRoles varRole) {
    role = varRole;
  }
  
  public abstract String toString();

  // Observer Methods
  protected void update() {
    for (VariableObserver obs : observers)
      obs.update();
  }

  public void addObserver(VariableObserver obs) {
    observers.add(obs);
  }

  public void removeObserver(VariableObserver obs) {
    observers.remove(obs);
  }
}
