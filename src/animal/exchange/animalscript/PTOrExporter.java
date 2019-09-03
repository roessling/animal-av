
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTOr;

/**This class create a animal-script String from a or-gate GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTOrExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a or-gate GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for or-gate
	 * @return a String for animal-script of or-gate
	 **/
	public String getExportString(PTGraphicObject ptgo) {
    return getCommonGateExportString((PTOr)ptgo, "relor");
	}
}