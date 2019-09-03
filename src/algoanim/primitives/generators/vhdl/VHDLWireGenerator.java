package algoanim.primitives.generators.vhdl;

import algoanim.primitives.generators.GeneratorInterface;
import algoanim.primitives.vhdl.VHDLWire;


/**
 * <code>AndGateGenerator</code> offers methods to request the included 
 * Language object to
 * append AND gate-related script code lines to the output.
 * It is designed to be included by a <code>AndGate</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>AndGate</code> primitive has to 
 * implement its own
 * <code>AndGateGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 0.2 20110218
 */
public interface VHDLWireGenerator extends GeneratorInterface {
  /**
   * Creates the originating script code for a given <code>AND gate</code>,
   * due to the fact that before a primitive can be worked with it has to be 
   * defined and made known to the script language.
   * 
   * @param wire the <code>wire</code> for which the initiate script code
   * shall be created. 
   */
    public void create(VHDLWire wire);

}
