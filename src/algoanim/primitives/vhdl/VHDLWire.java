package algoanim.primitives.vhdl;

import java.util.List;
import java.util.Vector;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.vhdl.VHDLWireGenerator;
import algoanim.properties.VHDLWireProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * Represents a wire defined by a sequence of nodes.
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 0.2 20110302
 */
public class VHDLWire extends Primitive {

  private VHDLWireGenerator generator;

  private List<Node> nodes;

  private VHDLWireProperties properties;

  private int speed;

	/**
	 * Instantiates the <code>VHDLWire</code> and calls the create() method of the
	 * associated <code>VHDLElementGenerator</code>.
	 * 
	 * @param sg
	 *          the appropriate code <code>Generator</code>.
	 * @param wireNodes
	 *          the nodes of this <code>VHDL element</code>.
	 * @param displaySpeed
	 *          the speed of displaying this <code>VHDL element</code>.
	 * @param name
	 *          the name of this <code>VHDL element</code>.
	 * @param display
	 *          [optional] the <code>DisplayOptions</code> of this
	 *          <code>VHDL element</code>.
	 * @param sp
	 *          [optional] the properties of this <code>VHDL element</code>.
	 * @throws IllegalArgumentException
	 */
	public VHDLWire(VHDLWireGenerator sg, List<Node> wireNodes,
      int displaySpeed, String name, DisplayOptions display, VHDLWireProperties sp)
			throws IllegalArgumentException {
		super(sg, display);
		generator = sg;
		nodes = wireNodes;
		speed = displaySpeed;
		setName(name);
		properties = sp;
    generator.create(this);
	}
	
	public List<Node> getNodes() {
	  if (nodes == null)
	    return new Vector<Node>();
	  return nodes;
	}
	
	public int getDisplaySpeed() {
	  return speed;
	}
	
	public VHDLWireProperties getProperties() {
	  return properties;
	}
}
