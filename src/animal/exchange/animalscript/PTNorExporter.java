package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTNor;

/**This class create a animal-script String from a nor-gate GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTNorExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a nor-gate GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for nor-gate
	 * @return a String for animal-script of nor-gate
	 **/
	public String getExportString(PTGraphicObject ptgo) {
    return getCommonGateExportString((PTNor)ptgo, "relnor");
	}
}