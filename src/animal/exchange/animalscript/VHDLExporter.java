package animal.exchange.animalscript;

import java.util.ArrayList;


/**
 * This class insert all VHDLExporter class (for example
 * VHDL.animal.exchange.animalscript.PTAndExporter) to Hashtable objectExporters
 * 
 * @author Lu,Zheng
 * 
 *  apply to ANIMAL 3.0
 */
public class VHDLExporter extends AnimatorExporter {
  /**
   * insert exporter class to Hashtable
   * 
   * @param components
   *          a ArrayList include all exporter class for VHDL object
   */
  public static void importToObjectExporters(ArrayList<String> components) {
    String subName;
    for (String className : components) {
      subName = "";
      StringBuilder handlerName = new StringBuilder(
          "animal.vhdl.exchange.animalscript.");
      handlerName.append(className.substring(className.lastIndexOf('.') + 1));
      handlerName.append("Exporter");
      subName = handlerName.toString();
      PTGraphicObjectExporter handler = null;
      Class<?> c;
      try {
        c = Class.forName(subName);
        handler = (PTGraphicObjectExporter) c.newInstance();
      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InstantiationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      AnimatorExporter.objectExporters.put(className, handler);
    }
  }

}
