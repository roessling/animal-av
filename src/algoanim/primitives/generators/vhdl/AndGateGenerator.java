package algoanim.primitives.generators.vhdl;


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
public interface AndGateGenerator extends VHDLElementGenerator {
}
