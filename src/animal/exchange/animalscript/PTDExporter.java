/**
 * 
 */
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTD;

/**This class create a animal-script String from a D-FlipFlop GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTDExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a D-FlipFlop GraphicObject
	 * 
	 * @param ptgo PTGraphicObject for D-FlipFlop
	 * @return a String for animal-script of D-FlipFlop
	 **/
	public String getExportString(PTGraphicObject ptgo) {
	  return getCommonFlipFlopExportString((PTD)ptgo, "reld", false, false);
	}
}