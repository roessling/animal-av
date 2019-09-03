/**
 * 
 */
package animal.exchange.animalscript;

import java.awt.Color;
import java.awt.Point;

import animal.graphics.PTGraphicObject;
import animal.misc.ColorChoice;
import animal.vhdl.graphics.PTWire;

/**This class create a animal-script String from a wire GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTWireExporter extends PTGraphicObjectExporter {
	/**create a animal-script String from a wire GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for wire
	 * @return a String for animal-script of wire
	 **/
	public String getExportString(PTGraphicObject ptgo) {
		// write out the information of the super object
    System.err.println("Wire... ");
    System.err.println(ptgo);
		PTWire shape=(PTWire)ptgo;
		StringBuilder sb = new StringBuilder(200);
		if (getExportStatus(shape))
			return "# previously exported: '" + shape.getNum(false) + "/"
					+ shape.getObjectName();
		//keyword "wire" express a wire in animal-script
		sb.append("wire \"").append(shape.getObjectName()).append("\"");
		for(int i=0;i<shape.getNodeCount();i++){
			Point node=shape.getNodeAsPoint(i);
			sb.append(" (").append((int) node.getX()).append(", ").append((int) node.getY()).append(") ");
		}
		
		sb.append(" speed ").append(shape.getWalkSpeed());		
		// write this object's information
		Color color = shape.getColor();
		sb.append(" color ").append(ColorChoice.getColorName(color));

		sb.append(" depth ").append(shape.getDepth());

		
		hasBeenExported.put(shape, shape.getObjectName());
		return sb.toString();
	}
}