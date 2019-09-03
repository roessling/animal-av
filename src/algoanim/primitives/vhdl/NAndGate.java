package algoanim.primitives.vhdl;

import java.util.List;

import algoanim.primitives.generators.vhdl.VHDLElementGenerator;
import algoanim.properties.VHDLElementProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents a NAND gate defined by an upper left corner and its width.
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 0.2 20110218
 */
public class NAndGate extends VHDLElement {
	/**
	 * Instantiates the <code>NAndGate</code> and calls the create() method of the
	 * associated <code>VHDLElementGenerator</code>.
	 * 
	 * @param sg
	 *          the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner
	 *          the upper left corner of this <code>VHDL element</code>.
	 * @param theWidth
	 *          the width of this <code>VHDL element</code>.
   * @param theHeight the height of this <em>VHDL element</em>
	 * @param name
	 *          the name of this <code>VHDL element</code>.
   * @param definedPins the list of VHDL pins (in, out, control, or bidirectional)
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>VHDL element</code>.
	 * @param sp
	 *          [optional] the properties of this <code>VHDL element</code>.
	 * @throws IllegalArgumentException
	 */
	public NAndGate(VHDLElementGenerator sg, Node upperLeftCorner, int theWidth,
      int theHeight, String name, List<VHDLPin> definedPins,
      DisplayOptions display, VHDLElementProperties sp)
      throws IllegalArgumentException {
    super(sg, upperLeftCorner, theWidth, theHeight, name, definedPins, display, sp);
    generator.create(this);
	}
}
