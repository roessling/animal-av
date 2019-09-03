package animal.vhdl.vhdlscript;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import animal.exchange.animalscript.PTGraphicObjectExporter;
import animal.exchange.animalscript.PTPolylineExporter;
import animal.exchange.animalscript.PTRectangleExporter;
import animal.exchange.animalscript.PTTextExporter;
import animal.graphics.PTPolyline;
import animal.graphics.PTText;
import animal.misc.ColorChoice;
import animal.vhdl.analyzer.VHDLImporter;
import animal.vhdl.graphics.PTVHDLElement;
import animal.vhdl.graphics.PTWire;

public class ScriptGenerator {

  private static final String CRLF = System.getProperty("line.separator");
  static String[] codeSource;

  /**
   * @param fileName
   *          the file name of the original VHDL quelle code
   * @param elements
   * @return the generated string
   */
  public static String getScriptFrom(String fileName,
      ArrayList<PTVHDLElement> elements) {


    VHDLImporter imp = new VHDLImporter();
    imp.importVHDLFrom(fileName);
    codeSource = imp.getCodeLine("codeSource");
    //
    ArrayList<PTVHDLElement> real = new ArrayList<PTVHDLElement>();
    //

    //
    String asuFileName = "";
    //
    for (PTVHDLElement ele : elements) {
      if (ele.getEntityType() == 0)
        asuFileName = ele.getObjectName();
      if (ele.getEntityType() == 2) {
        real.add(ele);
      }
    }

    AnimationPlacement pl = new AnimationPlacement(real);

    // File myFile = new File("VHDL" + File.separator + "VHDLasu"
    // + File.separator + asuFileName + ".asu");
    // File myFile = new File("example" + File.separator +asuFileName + ".asu");
//    File myFile = new File(asuFileName + ".asu");
//    FileWriter fw;
//    try {
//      fw = new FileWriter(myFile);
//
//      BufferedWriter out = new BufferedWriter(fw);
      
      StringBuilder sb = new StringBuilder(16000);
      
      // script title
      sb.append("%Animal 2656*574").append(CRLF);
      // code group
      int codeX = pl.getBlackBox().getEndNode().x + 50;

      sb.append("{").append(CRLF);
      sb.append("codeGroup \"codeSource\" at (").append(codeX);
      sb.append(",60) color black highlightColor red").append(CRLF);
      for (String codeLine : codeSource) {
        sb.append(codeLine).append(CRLF);
      }
      sb.append("}").append(CRLF);
      // black box
      PTRectangleExporter goe = new PTRectangleExporter();
      sb.append("{").append(CRLF);
      sb.append(goe.getExportString(pl.getBlackBox())).append(CRLF);
      PTText bt = new PTText();
      bt.setText(asuFileName);
      int btx = pl.getBlackBox().getLocation().x;
      int bty = pl.getBlackBox().getLocation().y - bt.getBoundingBox().height;
      bt.setLocation(new Point(btx, bty));
      PTTextExporter bte = new PTTextExporter();
      sb.append(bte.getExportString(bt)).append(CRLF);

      ArrayList<PTPolyline> blackPins = pl.getBlackPins();
      PTPolylineExporter bpe = new PTPolylineExporter();
      for (PTPolyline bp : blackPins) {
        sb.append(bpe.getExportString(bp)).append(CRLF);
      }

      PTVHDLElement bEle = elements.get(0);
      for (int i = bEle.getCodeLineNumberBegin(); 
        i <= bEle.getCodeLineNumberEnd(); i++) {
        sb.append("highlightCode on  \"codeSource\" line ");
        sb.append(i).append(CRLF);
      }
      sb.append(CRLF).append("}").append(CRLF);

      try {
        sb.append(CRLF).append(toScript(pl.getOriginalData(), pl.getWires()));
        sb.append(CRLF);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();

      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

      // wire between white box and black box
      sb.append("{").append(CRLF);
      for (PTWire w : pl.getBlackWires()) {
        sb.append(w.toScript()).append(CRLF);
      }
      for (int i = 0; i < codeSource.length - 1; i++) {
        sb.append("unhighlightCode on  \"codeSource\" line ");
        sb.append(i).append(CRLF);
      }
      sb.append("highlightCode on  \"codeSource\" line ");
      sb.append((codeSource.length - 1)).append(CRLF);
      sb.append("}").append(CRLF);

//      out.close();
//    } catch (IOException e1) {
//      e1.printStackTrace();
//    }
//    return "example" + File.separator + asuFileName + ".asu";
    // return "VHDL" + File.separator + "VHDLasu" + File.separator
    // + asuFileName + ".asu";
      try {
        // File myFile = new File("VHDL" + File.separator + "VHDLasu"
        // + File.separator + asuFileName + ".asu");
        // File myFile = new File("example" + File.separator +asuFileName + ".asu");
        File myFile = new File(asuFileName + ".asu");
        FileWriter fw;
        fw = new FileWriter(myFile);
        BufferedWriter out = new BufferedWriter(fw);
        out.write(sb.toString());
        out.close();
        fw.close();
      }
      catch(IOException e) {
         System.err.println("exception in writing file " + asuFileName + ".asu");
      }
      return asuFileName + ".asu";
//      return sb.toString();
  }
/*
  public static String getScriptFrom(String fileName,
      ArrayList<PTVHDLElement> elements) {


    VHDLImporter imp = new VHDLImporter();
    imp.importVHDLFrom(fileName);
    codeSource = imp.getCodeLine("codeSource");
    //
    ArrayList<PTVHDLElement> real = new ArrayList<PTVHDLElement>();
    //

    //
    String asuFileName = "";
    //
    for (PTVHDLElement ele : elements) {
      if (ele.getEntityType() == 0)
        asuFileName = ele.getObjectName();
      if (ele.getEntityType() == 2) {
        real.add(ele);
      }
    }

    AnimationPlacement pl = new AnimationPlacement(real);

    // File myFile = new File("VHDL" + File.separator + "VHDLasu"
    // + File.separator + asuFileName + ".asu");
    // File myFile = new File("example" + File.separator +asuFileName + ".asu");
    File myFile = new File(asuFileName + ".asu");
    FileWriter fw;
    try {
      fw = new FileWriter(myFile);

      BufferedWriter out = new BufferedWriter(fw);
      
      // script title
      out.write("%Animal 2656*574" + CRLF);
      // code group
      int codeX = pl.getBlackBox().getEndNode().x + 50;

      out.write("{" + CRLF
          + "codeGroup \"codeSource\" at (" + codeX
          + ",60) color black highlightColor red"
          + CRLF);
      for (String codeLine : codeSource) {
        out.write(codeLine + CRLF);
      }
      out.write("}" + CRLF);
      // black box
      PTRectangleExporter goe = new PTRectangleExporter();
      out.write("{" + CRLF
          + goe.getExportString(pl.getBlackBox())
          + CRLF);
      PTText bt = new PTText();
      bt.setText(asuFileName);
      int btx = pl.getBlackBox().getLocation().x;
      int bty = pl.getBlackBox().getLocation().y - bt.getBoundingBox().height;
      bt.setLocation(new Point(btx, bty));
      PTTextExporter bte = new PTTextExporter();
      out.write(bte.getExportString(bt) + CRLF);

      ArrayList<PTPolyline> blackPins = pl.getBlackPins();
      PTPolylineExporter bpe = new PTPolylineExporter();
      for (PTPolyline bp : blackPins) {
        out.write(bpe.getExportString(bp)
            + CRLF);
      }

      PTVHDLElement bEle = elements.get(0);
      for (int i = bEle.getCodeLineNumberBegin(); i <= bEle
          .getCodeLineNumberEnd(); i++) {
        out.write("highlightCode on  \"codeSource\" line " + i
            + CRLF);
      }
      out.write(CRLF + "}"
          + CRLF);

      try {
        out.write(CRLF
            + toScript(pl.getOriginalData(), pl.getWires())
            + CRLF);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();

      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

      // wire between white box and black box
      out.write("{" + CRLF);
      for (PTWire w : pl.getBlackWires()) {
        out.write(w.toScript() + CRLF);
      }
      for (int i = 0; i < codeSource.length - 1; i++) {
        out.write("unhighlightCode on  \"codeSource\" line " + i
            + CRLF);
      }
      out.write("highlightCode on  \"codeSource\" line "
          + (codeSource.length - 1) + CRLF);
      out.write("}" + CRLF);

      out.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return "example" + File.separator + asuFileName + ".asu";
    // return "VHDL" + File.separator + "VHDLasu" + File.separator
    // + asuFileName + ".asu";

  }
*/
  public static String toScript(PTVHDLElement[] originalData,
      ArrayList<PTWire> wires) throws ClassNotFoundException,
      InstantiationException, IllegalAccessException {
    // String vhdlScript = "";
    StringBuilder script = new StringBuilder(18000);
    ArrayList<PTVHDLElement> generatedEle = new ArrayList<PTVHDLElement>(
        originalData.length);

    PTVHDLElement oEle = null;
    for (PTVHDLElement ele : originalData) {
      script.append("{").append(CRLF);

      if (oEle != null) {
        script.append(CRLF).append("color \"");
        script.append(oEle.getObjectName()).append("\" type \"fillColor\" ");
        script.append(ColorChoice.getColorName(oEle.getDefaultFillColor()));
        script.append(" ").append(CRLF);

        script.append("color \"").append(oEle.getObjectName());
        script.append("\" type \"color\" ");
        script.append(ColorChoice.getColorName(oEle.getDefaultColor())).append(" ");
        script.append(CRLF);

      }
      oEle = ele;
      String className = ele.getClass().getName();
      int lastComma = className.lastIndexOf(".");
      String eleName = className.substring(lastComma);
      String eleEditorName = eleName + "Exporter";
      @SuppressWarnings("rawtypes")
      Class eleEditor = Class.forName("animal.exchange.animalscript"
          + eleEditorName);
      PTGraphicObjectExporter editor = (PTGraphicObjectExporter) eleEditor
          .newInstance();
      script.append(editor.getExportString(ele)).append(
          CRLF);

      // code

      for (int i = 0; i < codeSource.length; i++) {
        if (i < ele.getCodeLineNumberBegin()
            || i > ele.getCodeLineNumberEnd()) {
          script.append("un");
        }
        script.append("highlightCode on  \"codeSource\" line ").append(i);
        script.append(CRLF);
//        if (i >= ele.getCodeLineNumberBegin()
//            && i <= ele.getCodeLineNumberEnd()) {
//          script.append("highlightCode on  \"codeSource\" line ").append(i);
//          script.append(crLf);
//        } else {
//          script.append("unhighlightCode on  \"codeSource\" line ").append(i);
//          script.append(crLf);
//        }
      }
      script.append(CRLF).append("}");
      script.append(CRLF); 

      generatedEle.add(ele);
      for (PTWire w : wires) {
        if (ele == w.getEndElement()
            && generatedEle.contains(w.getStartElement())) {
          script.append("{").append(CRLF);
          script.append(w.toScript()).append(CRLF); 
          script.append("}").append(CRLF);
        }
      }

    }
    if (oEle != null) {
      script.append("{").append(CRLF).append("color \"");
      script.append(oEle.getObjectName());
      script.append("\" type \"fillColor\" ");
      script.append(ColorChoice.getColorName(oEle.getDefaultFillColor()));
      script.append(" ").append(CRLF);

      script.append("color \"").append(oEle.getObjectName());
      script.append("\" type \"color\" ");
      script.append(ColorChoice.getColorName(oEle.getDefaultColor()));
      script.append(" ").append(CRLF);

      script.append("}").append(CRLF);
    }
    return script.toString();
  }
}
