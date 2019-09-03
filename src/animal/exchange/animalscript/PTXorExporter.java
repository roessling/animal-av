
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTXor;

/**This class create a animal-script String from a xor-gate GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTXorExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a xor-gate GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for xor-gate
	 * @return a String for animal-script of xor-gate
	 **/
	public String getExportString(PTGraphicObject ptgo) {
    return getCommonGateExportString((PTXor)ptgo, "relxor");
	}
}