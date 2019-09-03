package algoanim.primitives.generators.vhdl;


/**
 * <code>NotGateGenerator</code> offers methods to request the included 
 * Language object to
 * append NOT gate-related script code lines to the output.
 * It is designed to be included by a <code>NotGate</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>NotGate</code> primitive has to 
 * implement its own
 * <code>NotGateGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 0.2 20110218
 */
public interface NotGateGenerator extends VHDLElementGenerator {
}
