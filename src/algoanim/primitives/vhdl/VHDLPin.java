package algoanim.primitives.vhdl;

public class VHDLPin {
  public final static char VALUE_NOT_DEFINED = '_';
  protected VHDLPinType pinType;
  
  protected String name;
  
  protected char value = VHDLPin.VALUE_NOT_DEFINED;
  
  public VHDLPin(VHDLPinType type, String pinName, char pinValue) {
    pinType = type;
    name = pinName;
    value = pinValue;
  }
  
  /**
   * @return the pinType
   */
  public VHDLPinType getPinType() {
    return pinType;
  }

  /**
   * @param pinType the pinType to set
   */
  public void setPinType(VHDLPinType pinType) {
    this.pinType = pinType;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the value
   */
  public char getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(char value) {
    this.value = value;
  }
}
