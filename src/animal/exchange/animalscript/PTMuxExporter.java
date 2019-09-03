
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTMux;

/**This class create a animal-script String from a Mux GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTMuxExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a Mux GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for Mux
	 * @return a String for animal-script of Mux
	 **/
	public String getExportString(PTGraphicObject ptgo) {
    return getCommonMultiplexExportString((PTMux)ptgo, "relmux");
	}
}