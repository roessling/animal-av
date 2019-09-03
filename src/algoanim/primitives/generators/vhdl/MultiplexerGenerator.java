package algoanim.primitives.generators.vhdl;


/**
 * <code>MultiplexerGenerator</code> offers methods to request the included 
 * Language object to
 * append multiplexer-related script code lines to the output.
 * It is designed to be included by a <code>Multiplexer</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>Multiplexer</code> primitive has to 
 * implement its own
 * <code>MultiplexerGenerator</code>, which is then responsible for creating 
 * proper script code.  
 * 
 * @author Guido Roessling (roessling@acm.org)
 * @version 0.2 20110218
 */
public interface MultiplexerGenerator extends VHDLElementGenerator {
}
