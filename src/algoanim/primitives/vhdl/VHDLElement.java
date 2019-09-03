package algoanim.primitives.vhdl;

import java.util.List;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.vhdl.VHDLElementGenerator;
import algoanim.properties.VHDLElementProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

public abstract class VHDLElement extends Primitive {
  protected VHDLElementGenerator generator = null;

  protected int height = 0;
  
  protected List<VHDLPin> pins = null;
  
  protected VHDLElementProperties properties = null;

  protected int width = 0;

  protected Node upperLeft = null;


  /**
   * Instantiates the <code>Square</code> and calls the create() method of the
   * associated <code>SquareGenerator</code>.
   * 
   * @param sg
   *          the appropriate code <code>Generator</code>.
   * @param upperLeftCorner
   *          the upper left corner of this <code>Square</code>.
   * @param theWidth
   *          the width of this <code>Square</code>.
   * @param name
   *          the name of this <code>Square</code>.
   * @param display
   *          [optional] the <code>DisplayOptions</code> of this
   *          <code>Square</code>.
   * @param sp
   *          [optional] the properties of this <code>Square</code>.
   * @throws IllegalArgumentException
   */
  public VHDLElement(VHDLElementGenerator sg, Node upperLeftCorner, int theWidth,
      int theHeight, String name, List<VHDLPin> definedPins,
      DisplayOptions display, VHDLElementProperties sp)
      throws IllegalArgumentException {
    super(sg, display);

    upperLeft = upperLeftCorner;
    width = theWidth;
    height = theHeight;
    generator = sg;
    pins = definedPins;
    properties = sp;
    setName(name);
  }


  /**
   * Returns the height of this <code>AND gate</code>.
   * 
   * @return the height of this <code>AND gate</code>.
   */
  public int getHeight() {
    return width;
  }

  /**
   * Returns the properties of this <code>AND gate</code>.
   * 
   * @return the properties of this <code>AND gate</code>.
   */
  public VHDLElementProperties getProperties() {
    return properties;
  }

  /**
   * Returns the upper left corner of this <code>AND gate</code>.
   * 
   * @return the upper left corner of this <code>AND gate</code>.
   */
  public Node getUpperLeft() {
    return upperLeft;
  }

  /**
   * Returns the width of this <code>AND gate</code>.
   * 
   * @return the width of this <code>AND gate</code>.
   */
  public int getWidth() {
    return width;
  }

  /**
   * @see algoanim.primitives.Primitive#setName(java.lang.String)
   */
  public void setName(String newName) {
    properties.setName(newName);
    super.setName(newName);
  }
  
  public List<VHDLPin> getPins() {
    return pins;
  }
}