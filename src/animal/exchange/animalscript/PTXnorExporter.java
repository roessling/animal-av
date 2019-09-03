package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTXnor;

/**This class create a animal-script String from a xnor-gate GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTXnorExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a xnor-gate GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for xnor-gate
	 * @return a String for animal-script of xnor-gate
	 **/
	public String getExportString(PTGraphicObject ptgo) {
    return getCommonGateExportString((PTXnor)ptgo, "relxnor");
	}
}