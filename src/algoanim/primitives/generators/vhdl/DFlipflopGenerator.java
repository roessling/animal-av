package algoanim.primitives.generators.vhdl;


/**
 * <code>DFlipflopGenerator</code> offers methods to request the included 
 * Language object to
 * append D-flipflop related script code lines to the output.
 * It is designed to be included by a <code>DFlipflop</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>DFlipflop</code> primitive has to 
 * implement its own
 * <code>DFlipflopGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 0.2 20110218
 */
public interface DFlipflopGenerator extends VHDLElementGenerator {
}
