
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTJK;

/**This class create a animal-script String from a JK-FlipFlop GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTJKExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a JK-FlipFlop GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for JK-FlipFlop
	 * @return a String for animal-script of JK-FlipFlop
	 **/
	public String getExportString(PTGraphicObject ptgo) {
	 return getCommonFlipFlopExportString((PTJK)ptgo, "reljk", true, true);
	}
}
