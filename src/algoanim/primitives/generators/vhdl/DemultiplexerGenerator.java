package algoanim.primitives.generators.vhdl;


/**
 * <code>DemultiplexerGenerator</code> offers methods to request the included 
 * Language object to
 * append demultiplexer-related script code lines to the output.
 * It is designed to be included by a <code>Demultiplexer</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Demultiplexer</code> primitive has to 
 * implement its own
 * <code>DemultiplexerGenerator</code>, which is then responsible to create 
 * proper script code.  
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 0.2 20110218
 */
public interface DemultiplexerGenerator extends VHDLElementGenerator {
}
