package animal.exchange.xaal;

import java.io.PrintWriter;
import java.util.HashMap;

import animal.graphics.PTCircle;
import animal.graphics.PTGraphicObject;

public class PTCircleExporter extends PTGraphicObjectExporter {
   public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    // write out the information of the super object
    PTCircle shape = (PTCircle) ptgo;

    // export shared attributes I (file version, id)
    exportCommonStartAttributesTo(pw, shape, "circle");

    // export centger
    exportNode(pw, shape.getCenter(), "center");

    pw.print("    <radius x=\"");
    pw.print(shape.getRadius());
    pw.print("\" y=\"");
    pw.print(shape.getRadius());
    pw.println("\"/>");
    
    // export color
//    exportColor(pw, shape.getColor(), "color");

    // export fill settings (filled?, fillColor)
//    exportFillSettings(pw, shape);

    // export shared attributes II (depth, name)
    exportCommonEndAttributesTo(pw, shape, "circle");
  }

  @Override
  public void gatherGraphicSettingsTo(PTGraphicObject ptgo, HashMap<String, String> properties) {
    // TODO Auto-generated method stub
    
  }
}
