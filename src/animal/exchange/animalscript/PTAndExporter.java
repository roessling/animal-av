
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTAnd;

/**This class create a animal-script String from a and-gate GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTAndExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a and-gate GraphicObject
	 * 
	 * @param ptgo PTGraphicObject for and-gate
	 * @return a String for animal-script of and-gate
	 **/
	public String getExportString(PTGraphicObject ptgo) {
	  return getCommonGateExportString((PTAnd)ptgo, "reland");
	}
}