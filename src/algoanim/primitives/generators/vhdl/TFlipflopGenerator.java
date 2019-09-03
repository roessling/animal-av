package algoanim.primitives.generators.vhdl;


/**
 * <code>TFlipflopGenerator</code> offers methods to request the included 
 * Language object to
 * append T flipflop-related script code lines to the output.
 * It is designed to be included by a <code>TFlipflop</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>TFlipflop</code> primitive has to 
 * implement its own
 * <code>TFlipflop</code>, which is then responsible for creating
 * proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface TFlipflopGenerator extends VHDLElementGenerator {
}
