package animal.editor.graphics.meta;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import translator.TranslatableGUIElement;
import animal.editor.Editor;
import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.gui.DrawCanvas;
import animal.main.Animation;
import animal.misc.ColorChoice;
import animal.misc.ColorChooserAction;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * Editor for GraphicObjects.
 * 
 * @see animal.graphics.PTGraphicObject
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public abstract class GraphicEditor extends Editor 
implements PropertyChangeListener {
  protected Container cp = null;
  protected TranslatableGUIElement generator = null;
  protected ColorChooserAction colorChooser;


  /**
	 * 
	 */
	private static final long serialVersionUID = 633413558545294186L;

	public GraphicEditor() {
    this(true);
  }

	public GraphicEditor(boolean boxMode) {
	  super(AnimalMainWindow.getWindowCoordinator().getDrawWindow(false), boxMode);
	  buildGUI();
	}

  protected abstract void buildGUI();

  /**
   * the number of points needed for the construction of a graphic object.
   * 
   * @return -1, if the object needs arbitrarily many points (e.g. a polyline)
   *         <p>
   *         Otherwise, the number of points needed.
   */
  public abstract int pointsNeeded();

  /**
   * insert/set a point of the graphic object. This method is called by <a
   * href=ObjectPanel.html>ObjectPanel</a>, when the mouse is dragged or moved.
   * <code>num</code> is increased every time the mousebutton is pressed.
   * 
   * @param num
   *          the number of the point to be set/changed, starting with -1.
   * @param p
   *          the coordinates of the point to be set/changed
   */
  public abstract boolean nextPoint(int num, Point p);

  /**
   * force a new paint, e.g. if parameters have changed
   */
  public void repaintNow() {
    AnimalMainWindow.getWindowCoordinator().getDrawWindow(true).getDrawCanvas()
        .repaintAll();
  }

  /**
   * returns the distance of the object to the point p, i.e. the length of a
   * shortest line linking the object with p
   */
  public abstract int getMinDist(PTGraphicObject go, Point p);

  /**
   * returns the Edit points of the given graphic object. The type of the given
   * graphic object must correspond to the type of the editor, which is(at least
   * should be :) ) the case, if this method is called by
   * <code>go.getEditor().getEditPoints(go)</code>, <em>go</em> being the
   * graphic object. Must be implemented by subclasses to return the specific
   * Editpoints of the graphic object implemented, i.e. a PolylineEditor must
   * return the Editpoints of a Polyline and can rely on the parameter passed
   * being a Polyline(but must not(!) use PTPolyline as a parameter, but cast
   * the given EditableObject to a PTPolyline).
   */
  public abstract EditPoint[] getEditPoints(PTGraphicObject go);

  /**
   * returns a new ColorChooser
   * @return the concrete ColorChooserAction
   */
  public ColorChooserAction createColorChooser(String translatedColorName, Color initialColor) {
    return createColorChooser("color", "GenericEditor.chooseColor",
        translatedColorName, initialColor);
  }
   /**
   * returns a new ColorChooser
   * @return the concrete ColorChooserAction
   */
  public ColorChooserAction createColorChooser(String colorName,
      String translatedColorName, Color initialColor) {
    return createColorChooser(colorName, "GenericEditor.chooseColor",
        translatedColorName, initialColor);
  }
  
  /**
   * returns a new ColorChooser
   * @return the concrete ColorChooserAction
   */
  public ColorChooserAction createColorChooser(String colorName, String promptMessage,
      String translatedColorName, Color initialColor) {
    ColorChooserAction colorChooserAction = new ColorChooserAction(this, 
        ColorChoice.getColorName(initialColor), colorName, 
        AnimalTranslator.translateMessage(promptMessage, 
              new Object[] { AnimalTranslator.translateMessage(translatedColorName) }),
              initialColor);
    return colorChooserAction;
  }

  public abstract String getBasicType();

  /**
   * paints this editor's GraphicObject.
   */
  public void paintObject(Graphics g) {
    ((PTGraphicObject) getCurrentObject()).paint(g);
  }
  
  protected void initializeLayoutComponents() {
    // basic setup: central panel, MigLayout, on a scroll pane if needed
    if (cp == null)
      cp = setupLayout();

    if (generator == null)
      generator = AnimalTranslator.getGUIBuilder();  
  }
  
  protected void createGenericColorSetting(String colorTypeLabel, int shortCutKey) {
    initializeLayoutComponents();
    // ===========================
    // create area for color input
    // ===========================
    insertSeparator("GenericEditor.colorBL", cp, generator);

    // create the color label
    JLabel colorLabel = generator.generateJLabel("GenericEditor.colorLabel");
    cp.add(colorLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    PTGraphicObject ptgo = (PTGraphicObject)getCurrentObject(false);
    // add color chooser
    Color initialColor = (ptgo == null) ? Color.black : ptgo.getColor();
    
    colorChooser = createColorChooser("color", "GenericEditor.chooseColor", 
        colorTypeLabel, initialColor);
    ExtendedActionButton button = new ExtendedActionButton(colorChooser,
        shortCutKey);
    cp.add(button, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }


  protected void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    if (eo instanceof PTGraphicObject) {
      PTGraphicObject go = (PTGraphicObject) eo;
      go.setDepth(Integer.valueOf((String) depthBox.getSelectedItem())
          .intValue());
      go.setObjectName(objectNameField.getText());
      go.setColor(colorChooser.getColor());
    }
  }
  
  protected void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    if (eo instanceof PTGraphicObject) {
      PTGraphicObject p = (PTGraphicObject) eo;
      if (p.getDepth() > 16 && p.getDepth() != Integer.MAX_VALUE)
        depthBox.addItem(String.valueOf(p.getDepth()));
      depthBox.addItem(String.valueOf(Integer.MAX_VALUE));
      depthBox.setSelectedItem(String.valueOf(p.getDepth()));
      objectNameField.setText(p.getObjectName());
      colorChooser.setColor(p.getColor());
    }
  }

  public void getProperties(XProperties props) {
    String baseKey = getBasicType();
    props.put(baseKey + ".color", colorChooser.getColor());
    props.put(baseKey + ".depth", depthBox.getSelectedItem());
  }
  
  public void setProperties(XProperties props) {
    String baseKey = getBasicType();
    colorChooser.setColor(props.getColorProperty(baseKey
        + ".color", Color.black));
    depthBox.setSelectedItem(props.getProperty(baseKey
        + ".depth", "16"));
  }
  
  public void propertyChange(PropertyChangeEvent event) {
    PTGraphicObject poly = (PTGraphicObject) getCurrentObject();
    String eventName = event.getPropertyName();
    if ("color".equals(eventName))
      poly.setColor((Color) event.getNewValue());
    if (!event.getOldValue().equals(event.getNewValue())) {
      repaintNow();
      if (Animation.get() != null)
        Animation.get().doChange();
    }
  }
  
  /**
   * returns a message describing how to create a new object in the DrawCanvas.
   * This message is displayed in DrawWindow's statusline whenever this editor
   * is active.
   */
  public String getStatusLineMsg() {
    return AnimalTranslator.translateMessage(
        getBasicType() + "Editor.statusLine",
        new Object[] { DrawCanvas.translateDrawButton(),
            DrawCanvas.translateFinishButton(),
            DrawCanvas.translateCancelButton() });
  }
}
