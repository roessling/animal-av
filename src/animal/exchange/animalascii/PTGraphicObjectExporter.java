package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Point;
import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.meta.FillablePrimitive;

public class PTGraphicObjectExporter implements Exporter {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param pw
   *          the PrintWriter to write to
   */
  public void exportTo(PrintWriter pw, PTGraphicObject ptgo) {
    pw.println("Undefined primitive: " + getClass().getName() + " for " + ptgo);
  }

  /**
   * exports a given color with the prefix "color"
   * 
   * @param pw
   *          the PrintWriter to write to
   * @param color
   *          the color to export
   */
  public void exportColor(PrintWriter pw, Color color) {
    exportColor(pw, color, "color");
  }

  /**
   * exports a given color with the prefix <em>key</em>
   * 
   * @param pw
   *          the PrintWriter to write to
   * @param color
   *          the color to export
   * @param key
   *          the prefix to use before the color export
   */
  public void exportColor(PrintWriter pw, Color color, String key) {
    // write this color
    pw.print(key);
    pw.print(" (");
    pw.print(color.getRed());
    pw.print(",");
    pw.print(color.getGreen());
    pw.print(",");
    pw.print(color.getBlue());
    pw.print(") ");
  }

  /**
   * exports common "end" attributes for polygonal shapes:
   * 
   * <ol>
   * <li>the shape's depth</li>
   * <li>the shape's object name (if any)</li>
   * </ol>
   * 
   * @param pw
   *          the PrintWriter on which to write
   * @param shape
   *          the shape to export
   */
  public void exportCommonEndAttributesTo(PrintWriter pw, PTGraphicObject shape) {
    pw.print("depth ");
    pw.print(shape.getDepth());

    if (shape.getObjectName() != null) {
      pw.print(" name \"");
      pw.print(shape.getObjectName());
      pw.println("\"");
    }
  }

  /**
   * exports common "start" attributes for polygonal shapes:
   * 
   * <ol>
   * <li>the file version of the class,</li>
   * <li>the shape's numeric ID</li>
   * </ol>
   * 
   * @param pw
   *          the PrintWriter on which to write
   * @param shape
   *          the shape to export
   */
  public void exportCommonStartAttributesTo(PrintWriter pw,
      PTGraphicObject shape) {
    // write out the information of the super object
    pw.print(shape.getFileVersion());
    pw.print(" object ");
    pw.print(shape.getNum(false));
    pw.print(" ");
    pw.print(shape.getType());
    pw.print(' ');
  }

  /**
   * exports the "fill" attributes for closed arc-based shapes:
   * 
   * <ol>
   * <li>whether the shape is filled,</li>
   * <li>the shape's fill color (only if it is filled!),</li>
   * </ol>
   * 
   * @param pw
   *          the PrintWriter on which to write
   * @param shape
   *          the shape to export
   */
  public void exportFillSettings(PrintWriter pw, FillablePrimitive shape) {
    if (shape.isFilled()) {
      pw.print("filled ");

      // export the fill color
      exportColor(pw, shape.getFillColor(), "fillColor");
    }
  }

  /**
   * exports a single node
   * 
   * @param pw
   *          the PrintWriter on which to write
   * @param node the point to export
   */
  public void exportNode(PrintWriter pw, Point node) {
    pw.print("(");
    pw.print(node.x);
    pw.print(",");
    pw.print(node.y);
    pw.print(") ");
  }

  /**
   * exports a single node
   * 
   * @param pw
   *          the PrintWriter on which to write
   * @param node the PTPoint to export
   */
  public void exportNode(PrintWriter pw, PTPoint node) {
    if (node != null)
      exportNode(pw, node.toPoint());
  }

  /**
   * exports a single node
   * 
   * @param pw
   *          the PrintWriter on which to write
   * @param node the Point to export
   * @param key
   *          the prefix to use before the exported node
   */
  public void exportNode(PrintWriter pw, Point node, String key) {
    pw.print(key);
    pw.print(' ');
    exportNode(pw, node);
  }

  /**
   * exports a single node
   * 
   * @param pw
   *          the PrintWriter on which to write
   * @param node the PTPoint to export
   * @param key
   *          the prefix to use before the exported node
   */
  public void exportNode(PrintWriter pw, PTPoint node, String key) {
    pw.print(key);
    pw.print(' ');
    exportNode(pw, node);
  }
}
