package algoanim.properties;

import algoanim.properties.items.StringPropertyItem;

/**
 * This special Properties-Object is used to make calls to a specific method of
 * the Generator. The method that should be called is stored (as a String) in
 * "methodName".
 * 
 * @author T. Ackermann
 * @see algoanim.properties.AnimationProperties
 */
public class CallMethodProperties extends AnimationProperties {

  /**
   * Generates an unnamed <code>CallMethodProperties</code> object.
   */
  public CallMethodProperties() {
    this("unnamed CallMethod property");
  }

  /**
   * Generates a named <code>CallMethodProperties</code> object.
   * 
   * @param name
   *          the name for this <code>CallMethodProperties</code>.
   */
  public CallMethodProperties(String name) {
    super(name);
    fillHashMap();
  }

  /**
   * @see algoanim.properties.AnimationProperties#fillHashMap()
   */
  @Override
  protected void addTypeSpecificValues() {
//    super.fillHashMap();

    // Create the Hashmap with new PropertyItems
    // We don't have to care about some appropriate default values, because they
    // are set in the PropertyItem classes themselves
    data.put(AnimationPropertiesKeys.METHOD_NAME, new StringPropertyItem());
    //
    // // enter all additional values
    // fillAdditional();
  }
}
