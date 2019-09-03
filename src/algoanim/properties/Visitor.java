package algoanim.properties;

import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.items.DoublePropertyItem;
import algoanim.properties.items.EnumerationPropertyItem;
import algoanim.properties.items.FontPropertyItem;
import algoanim.properties.items.IntegerPropertyItem;
import algoanim.properties.items.StringPropertyItem;

/**
 * A class wishing to visit a AnimationProperties or AnimationPropertyItems has
 * to implement this interface.
 * 
 * @author Jens Pfau, Guido R&ouml;&szlig;ling 
 * @version 1.1 20080609
 */
public interface Visitor {

  /**
   * Visit a BooleanPropertyItem.
   * 
   * @param bpi
   *          the BooleanPropertyItem which is visited
   */
  public void visit(BooleanPropertyItem bpi);

  /**
   * Visit a ColorPropertyItem.
   * 
   * @param cpi
   *          the ColorPropertyItem which is visited
   */
  public void visit(ColorPropertyItem cpi);

  /**
   * Visit a DoublePropertyItem.
   * 
   * @param dpi
   *          the DoublePropertyItem which is visited
   */
  public void visit(DoublePropertyItem dpi);

  /**
   * Visit a FontPropertyItem.
   * 
   * @param fpi
   *          the FontPropertyItem which is visited
   */
  public void visit(FontPropertyItem fpi);

  /**
   * Visit a IntegerPropertyItem.
   * 
   * @param ipi
   *          the IntegerPropertyItem which is visited
   */
  public void visit(IntegerPropertyItem ipi);

  /**
   * Visit a StringPropertyItem.
   * 
   * @param spi
   *          the StringPropertyItem which is visited
   */
  public void visit(StringPropertyItem spi);

  /**
   * Visit an EnumerationPropertyItem
   * 
   * @param epi
   *          the EnumerationPropertyItem which is visited
   */
  public void visit(EnumerationPropertyItem epi);
}
