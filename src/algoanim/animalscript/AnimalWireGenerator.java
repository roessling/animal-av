package algoanim.animalscript;

import java.util.List;

import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.vhdl.VHDLWireGenerator;
import algoanim.primitives.vhdl.VHDLWire;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.VHDLWireProperties;
import algoanim.util.Node;

/**
 * @see algoanim.primitives.generators.SquareGenerator
 * @author Guido Roessling
 * @version 0.2 20110218
 */
public class AnimalWireGenerator extends AnimalGenerator 
implements VHDLWireGenerator {

  /**
   * @param aLang
   *          the associated <code>Language</code> object.
   */
  public AnimalWireGenerator(Language aLang) {
    super(aLang);
  }


  @Override
  public void create(VHDLWire wire) {
    // Check Name, if used already, create a new one silently
    if (this.isNameUsed(wire.getName()) || wire.getName().equals("")) {
      wire.setName("wire" + AnimalVHDLElementGenerator.count);
      AnimalVHDLElementGenerator.count++;
    }
    lang.addItem(wire);

    StringBuilder sb = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);

    // create element type and name
    sb.append("wire \"").append(wire.getName())
    .append("\" ");
    
    // generate all nodes for this primitive
    List<Node> nodes = wire.getNodes(); 
    for (Node node: nodes) {
      sb.append(AnimalGenerator.makeNodeDef(node)).append(' ');
    }

    // generate speed, if any
    if (wire.getDisplaySpeed() > 0)
      sb.append("speed ").append(wire.getDisplaySpeed()).append(' ');
    
    VHDLWireProperties props = wire.getProperties();

    // add color information
    addColorOption(props, AnimationPropertiesKeys.COLOR_PROPERTY,
        "color ", sb);

    // check if the wire has a forward arrow
    addBooleanOption(props, AnimationPropertiesKeys.FWARROW_PROPERTY,
        " fwArrow", sb);
    // check if the wire has a forward arrow
    addBooleanOption(props, AnimationPropertiesKeys.BWARROW_PROPERTY,
        " bwArrow ", sb);

    sb.append(AnimalGenerator.makeDisplayOptionsDef(wire.getDisplayOptions(),
        props));
    lang.addLine(sb);
  }
}
