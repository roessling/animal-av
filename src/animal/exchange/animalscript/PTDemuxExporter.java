
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTDemux;

/**This class create a animal-script String from a Mux GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTDemuxExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a Mux GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for Mux
	 * @return a String for animal-script of Mux
	 **/
	public String getExportString(PTGraphicObject ptgo) {
    return getCommonMultiplexExportString((PTDemux)ptgo, "reldemux");
	}
}