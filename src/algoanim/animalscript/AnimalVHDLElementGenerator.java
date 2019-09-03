package algoanim.animalscript;

import java.util.HashMap;

import algoanim.primitives.generators.Language;
import algoanim.primitives.vhdl.VHDLElement;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.primitives.vhdl.VHDLPinType;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.VHDLElementProperties;

/**
 * @see algoanim.primitives.generators.SquareGenerator
 * @author Guido Roessling
 * @version 0.3 20110221
 */
public abstract class AnimalVHDLElementGenerator extends AnimalGenerator {
  protected static int count = 1;

  public static HashMap<VHDLPinType, String> pinNames;
  
  static {
    pinNames = new HashMap<VHDLPinType, String>(5);
    pinNames.put(VHDLPinType.BIDIRECTIONAL, "inoutput");
    pinNames.put(VHDLPinType.CLOCK, "clk");
    pinNames.put(VHDLPinType.CLOCK_ENABLE, "ce");
    pinNames.put(VHDLPinType.CONTROL, "control");
    pinNames.put(VHDLPinType.INPUT, "input");
    pinNames.put(VHDLPinType.OUTPUT, "output");
    pinNames.put(VHDLPinType.RESET, "rd");
    pinNames.put(VHDLPinType.SET, "sd");
  }
  
  /**
   * @param aLang
   *          the associated <code>Language</code> object.
   */
  public AnimalVHDLElementGenerator(Language aLang) {
    super(aLang);
  }


//  @Override
  public void createRepresentationForGate(VHDLElement vhdlElement, String typeName) {
    // Check Name, if used already, create a new one silently
    if (this.isNameUsed(vhdlElement.getName()) || vhdlElement.getName().equals("")) {
      vhdlElement.setName(typeName +"Gate" + AnimalVHDLElementGenerator.count);
      AnimalVHDLElementGenerator.count++;
    }
    lang.addItem(vhdlElement);

    StringBuilder sb = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    
    // create element type and name
    sb.append(typeName).append(" \"").append(vhdlElement.getName())
    .append("\" ");

    // generate the start node of this element
    sb.append(AnimalGenerator.makeNodeDef(vhdlElement.getUpperLeft()));

    // add the width and height as a node
    sb.append(" (").append(vhdlElement.getWidth()).append(", ");
    sb.append(vhdlElement.getHeight()).append(") ");
    
    // store the list of pins
    for (VHDLPin currentPin : vhdlElement.getPins()) {
      // export pint type
      sb.append(pinNames.get(currentPin.getPinType()));
      // append the pin name, if it exists
      String pinName = currentPin.getName();
      if (pinName != null && pinName.length() > 0)
        sb.append(' ').append(pinName);
      
      // append the pin value, if it exists
      char pinValue = currentPin.getValue();
      if (pinValue != VHDLPin.VALUE_NOT_DEFINED)
        sb.append(" value ").append(pinValue).append(' ');
    }
    
    VHDLElementProperties props = vhdlElement.getProperties();

    // add color information
    addColorOption(props, AnimationPropertiesKeys.COLOR_PROPERTY,
        " color ", sb);
    
    // check if filled and if so, also add fill color
    if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY,
        " filled ", sb))
      addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY,
          " fillColor ", sb);
    
    sb.append(AnimalGenerator.makeDisplayOptionsDef(vhdlElement.getDisplayOptions(),
        props));
    lang.addLine(sb);
  }
  
  /*
//@Override
  public void createRepresentationForFlipflop(RSFlipflop vhdlElement, String typeName) {
    // Check Name, if used already, create a new one silently
    if (this.isNameUsed(vhdlElement.getName()) || vhdlElement.getName() == "") {
      vhdlElement.setName(typeName + "Flipflop" + AnimalVHDLElementGenerator.count);
      AnimalVHDLElementGenerator.count++;
    }
    lang.addItem(vhdlElement);

    StringBuilder sb = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
    // create element type and name
    sb.append(typeName).append(" \"").append(vhdlElement.getName())
    .append("\" (");

    // generate the start node of this element
    Node node = vhdlElement.getUpperLeft();
    sb.append(AnimalGenerator.makeNodeDef(vhdlElement.getUpperLeft()));

    // add the width and height as a node
    sb.append(" (").append(vhdlElement.getWidth()).append(", ");
    sb.append(vhdlElement.getHeight()).append(") ");
    
    // store the list of pins
    for (VHDLPin currentPin : vhdlElement.getPins()) {
      // export pint type
      sb.append(pinNames.get(currentPin.getPinType()));
      // append the pin name, if it exists
      String pinName = currentPin.getName();
      if (pinName != null && pinName.length() > 0)
        sb.append(' ').append(pinName);
      
      // append the pin value, if it exists
      char pinValue = currentPin.getValue();
      if (pinValue != VHDLPin.VALUE_NOT_DEFINED)
        sb.append(" value ").append(pinValue);
    }
    
    VHDLElementProperties props = vhdlElement.getProperties();

    // add color information
    addColorOption(props, AnimationPropertiesKeys.COLOR_PROPERTY,
        " color ", sb);
    
    // check if filled and if so, also add fill color
    if (addBooleanOption(props, AnimationPropertiesKeys.FILLED_PROPERTY,
        " filled ", sb))
      addColorOption(props, AnimationPropertiesKeys.FILL_PROPERTY,
          " fillColor ", sb);
    
    sb.append(AnimalGenerator.makeDisplayOptionsDef(vhdlElement.getDisplayOptions(),
        props));
    lang.addLine(sb);
  }
*/
}
