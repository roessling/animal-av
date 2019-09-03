package algoanim.primitives.generators.vhdl;


/**
 * <code>NAndGateGenerator</code> offers methods to request the included 
 * Language object to
 * append NAND gate-related script code lines to the output.
 * It is designed to be included by a <code>NAnd</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>NAnd</code> primitive has to 
 * implement its own
 * <code>NAndGenerator</code>, which is then responsible for creating
 * proper script code.  
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 0.2 20110218
 */
public interface NAndGateGenerator extends VHDLElementGenerator {
}
