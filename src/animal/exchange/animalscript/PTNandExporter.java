
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTNand;

/**This class create a animal-script String from a nand-gate GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTNandExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a nand-gate GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for nand-gate
	 * @return a String for animal-script of nand-gate
	 **/
	public String getExportString(PTGraphicObject ptgo) {
    return getCommonGateExportString((PTNand)ptgo, "relnand");
	}
}