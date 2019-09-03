package animal.editor.graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import animal.editor.Editor;
import animal.editor.graphics.meta.AbstractTextEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;
import animal.graphics.meta.ImmediateTextContainer;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * The Editor for Text.
 * 
 * @see animal.graphics.PTText
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class TextEditor extends AbstractTextEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -1180513717753983571L;

//  private boolean isItalic, isBold;

  /**
   * The Editor's constructor. Writes the components into a Panel and adds this
   * Panel to the Editor, then adds the Buttons.
   */
  public TextEditor() {
    super();
  }

  protected void buildGUI() {
//    // basic setup: central panel, MigLayout, on a scroll pane if needed
//    Container cp = setupLayout();
//    
//    // retrieve GUI translator
//    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    PTText go = (PTText)getCurrentObject(false);
    Color c = (go == null) ? Color.BLACK : go.getColor();
    createTextComponentChooser(c, "color");
    
    // ugly but important - make sure "colorChooser" is set!
    // NOTE: this is only for "straight" color objects, not for those
    // using embedded texts
    colorChooser = textColorChooser;
    
    if (go != null)
      textField.setText(go.getText());
    
    // finish with the standard editors
    // for primitives: depth and object name, plus OK/apply/cancel
    finishEditor(cp);
  }

  /**
   * how many points do we need to specify a Text?
   * 
   * @see #nextPoint
   */
  public int pointsNeeded() {
    return 2;
  }

  /**
   * sets one of the Text's points. <br>
   * 1st and 2nd: set Location(by this, the Text is not displayed before the
   * mouse is clicked once)<br>
   */
  public boolean nextPoint(int num, Point p) {
    PTText t = (PTText) getCurrentObject();
    switch (num) {
    case 1:
    case 2:
      t.setLocation(p);
      break;
    }
    return true;
  } // nextPoint;

  /**
   * returns the minimal distance from point <i>p</i> to the Text. IMPORTANT:
   * use <code>PTGraphicObject</code> as first parameter and cast it to a
   * PTText inside the method. Otherwise this method won't be called.
   */
  public int getMinDist(PTGraphicObject go, Point p) {
    return MSMath.dist(p, go.getBoundingBox());
  }

  /**
   * returns the EditPoints of the Text. Again, the parameter has to be of type
   * <b>PTGraphicObject</b>. A Text only has one MovePoint(its upper left
   * corner) and one ChangePoint(its tip).
   */
  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTText t = (PTText) go;
    Rectangle r = t.getBoundingBox();
    return new EditPoint[] { // only move points
    new EditPoint(-1, t.getLocation()), // at the baseline
        new EditPoint(-2, new Point(r.x, r.y)), // upper left
        new EditPoint(-3, new Point(r.x + r.width, r.y)), // upper right
        new EditPoint(-4, new Point(r.x, r.y + r.height)), // lower left
        new EditPoint(-5, new Point(r.x + r.width, r.y + r.height)),
    // lower right
    };
  } // getEditPoints

  /**
   * creates a new Text and uses the attributes of this Editor as default
   * values.
   */
  public EditableObject createObject() {
    PTText t = new PTText();
    storeAttributesInto(t);
    return t;
  }

  /**
   * creates a secondary Editor for the given <b>EditableObject</b> and copies
   * all of the object's attributes into the components. We can rely on this
   * object always being a <b>PTText</b>.
   */
  public Editor getSecondaryEditor(EditableObject go) {
    TextEditor result = new TextEditor();
    result.extractAttributesFrom(go);
    return result;
  }
  
  /**
   * applies the Editor's settings to the Text by setting all of Texts
   * attributes according to the components. Parameter must be of type
   * <b>EditableObject</b> and be casted inside the method.
   */
  protected void storeAttributesInto(EditableObject eo) {
    // don't forget to store the parent's attributes!
    super.storeAttributesInto(eo);
    PTGraphicObject ptgo = (PTGraphicObject) eo; // just a shortcut
    ptgo.setColor(textColorChooser.getColor());
    ImmediateTextContainer itc = (ImmediateTextContainer)eo;
    itc.setText(textField.getText());
//    System.err.println("Store into... " +itc + ".." +textField.getText() + " //" +itc.getText());
    itc.setFont(storeFont());
  }

  /**
   * makes the Editor reflect the Text's attributes by setting all components
   * according to the attributes. Parameter must be of type <b>EditableObject</b>,
   * otherwise there may be problems with calls to super that can be difficult
   * to debug.
   */
  protected void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    PTText t = (PTText) eo;
    textColorChooser.setColor(t.getColor());
    textField.setText(t.getText());
    //    System.err.println("extracting from..." +t + ".."+ t.getText() + " // " +textField.getText());
    extractFont(t.getFont());
  }

  
  public String getBasicType() {
    return PTText.TEXT_TYPE;
  }
} // TextEditor
