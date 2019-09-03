
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTNot;

/**This class create a animal-script String from a not-gate GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTNotExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a not-gate GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for not-gate
	 * @return a String for animal-script of not-gate
	 **/
	public String getExportString(PTGraphicObject ptgo) {
    return getCommonGateExportString((PTNot)ptgo, "relnot");
	}
}