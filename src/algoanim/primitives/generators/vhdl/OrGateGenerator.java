package algoanim.primitives.generators.vhdl;


/**
 * <code>OrGateGenerator</code> offers methods to request the included 
 * Language object to
 * append OR gate-related script code lines to the output.
 * It is designed to be included by a <code>OrGate</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>OrGate</code> primitive has to 
 * implement its own
 * <code>OrGateGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface OrGateGenerator extends VHDLElementGenerator {
}
