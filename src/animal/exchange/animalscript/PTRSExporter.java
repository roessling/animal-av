/**
 * 
 */
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTRS;

/**This class create a animal-script String from a RS-FlipFlop GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTRSExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a RS-FlipFlop GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for RS-FlipFlop
	 * @return a String for animal-script of RS-FlipFlop
	 **/
	public String getExportString(PTGraphicObject ptgo) {
	  return getCommonFlipFlopExportString((PTRS)ptgo, "relrs", true, true);
	}
}