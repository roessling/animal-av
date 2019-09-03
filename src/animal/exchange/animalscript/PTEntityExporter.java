
package animal.exchange.animalscript;

import animal.graphics.PTGraphicObject;
import animal.vhdl.graphics.PTEntity;

/**This class create a animal-script String from a Entity GraphicObject  
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class PTEntityExporter extends AbstractVHDLExporter {
	/**create a animal-script String from a Entity GraphicObject
	 * 
	 * @param ptgo a PTGraphicObject for Entity
	 * @return a String for animal-script of Entity
	 **/
	public String getExportString(PTGraphicObject ptgo) {
		// write out the information of the super object
    if (getExportStatus(ptgo))
      return "# previously exported: '" + ptgo.getNum(false) + "/"
          + ptgo.getObjectName();
    
    PTEntity entity = (PTEntity)ptgo;
	  StringBuilder sb = startExport(entity, "relentity", true, true);
	  
	  // export in-/output pins
	  exportPins(entity.getInoutPins(), "inoutput", true, sb);
    
	  // export control pins
    exportPins(entity.getControlPins(), "control", true, sb);

//    StringBuilder sb2 = new StringBuilder(200);
//
//		//inoutput port
//		for (int i = 0; i < shape.getInoutPins().size(); i++) {
//			sb.append(" inoutput " + shape.getInoutPins().get(i).getPinName());
//			if (shape.getInoutPins().get(i).getPinValue() != ' ')
//				sb.append(" value " + shape.getInoutPins().get(i).getPinValue());
//		}
//		//control port
//		for (int i = 0; i < shape.getControlPins().size(); i++) {
//			sb.append(" control " + shape.getControlPins().get(i).getPinName());
//			if (shape.getControlPins().get(i).getPinValue() != ' ')
//				sb.append(" value " + shape.getControlPins().get(i).getPinValue());
//		}
		sb.append(" name \"").append(entity.getElementSymbol().getText()).append("\"");

		// finish with color etc.
		finishExport(entity, sb);
		return sb.toString();
	}
}