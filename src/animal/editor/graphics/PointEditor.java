package animal.editor.graphics;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

import animal.editor.Editor;
import animal.editor.graphics.meta.GraphicEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * The Editor for a PTPoint.
 * 
 * @see animal.graphics.PTPoint
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class PointEditor extends GraphicEditor implements
    PropertyChangeListener {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long  serialVersionUID = 2295451210977833754L;
  
//  private ColorChooserAction colorChooser;

  public PointEditor() {
    super(false);
  }
  
  protected void buildGUI() {
    createGenericColorSetting("Point", KeyEvent.VK_C);

    // finish with the standard editors
    // for primitives: depth and object name, plus OK/apply/cancel
    finishEditor(cp);
  }

  public int pointsNeeded() {
    return 1;
  }

  public boolean nextPoint(int num, Point p) {
    if (num == 1)
      ((PTPoint) getCurrentObject()).set(p);
    return true;
  }

  public int getMinDist(PTGraphicObject go, Point p) {
    return MSMath.dist(((PTPoint) go).toPoint(), p);
  }

  public EditPoint[] getEditPoints(PTGraphicObject go) {
    return new EditPoint[] { new EditPoint(-1, ((PTPoint) go).toPoint()) };
  }

  public EditableObject createObject() {
    PTPoint result = new PTPoint();
    storeAttributesInto(result);
    return result;
  }

  public Editor getSecondaryEditor(EditableObject e) {
    PointEditor result = new PointEditor();
    result.extractAttributesFrom(e);
    return result;
  }

  public String getBasicType() {
    return PTPoint.POINT_TYPE;
  }
} // PointEditor
