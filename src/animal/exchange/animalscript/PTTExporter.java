/**
 * 
 */
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTT;

/**This class create a animal-script String from a T-FlipFlop GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTTExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a T-FlipFlop GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for T-FlipFlop
	 * @return a String for animal-script of T-FlipFlop
	 **/
	public String getExportString(PTGraphicObject ptgo) {
	  return getCommonFlipFlopExportString((PTT) ptgo, "relt", false, false);
	}
}