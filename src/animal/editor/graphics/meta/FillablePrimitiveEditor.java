/*
 * Created on 18.07.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.editor.graphics.meta;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import translator.TranslatableGUIElement;
import animal.editor.Editor;
import animal.graphics.PTSquare;
import animal.graphics.meta.FillablePrimitive;
import animal.main.Animation;
import animal.misc.ColorChoice;
import animal.misc.ColorChooserAction;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/** 
 * Abstract editor for fillable primitives
 * 
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 1.1 2008-06-23
 */
public abstract class FillablePrimitiveEditor extends GraphicEditor 
implements ActionListener, ItemListener, PropertyChangeListener {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 8380963078395327260L;

	protected JCheckBox filledCB;

  protected ColorChooserAction fillColorChooser;

//  protected ExtendedActionButton fillColorChooserButton;

  public Box createCommonElements(TranslatableGUIElement generator) {
    return createColorBox(generator);
  }
  
  protected void buildGUI() {
    initializeLayoutComponents();
    
    // add basic color choice
    createGenericColorSetting("OpenObjectEditor.outline", KeyEvent.VK_C);
    
    // create the elements for entering color and fill color
    createFillColorChoice("GenericEditor.fillColor", KeyEvent.VK_F);

    // add fill option
    filledCB = generator.generateJCheckBox("GenericEditor.filled", null, this);
    filledCB.addItemListener(this);
    cp.add(filledCB, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);

    // finish with the standard editors
    // for primitives: depth and object name, plus OK/apply/cancel
    finishEditor(cp);
  }

  public void createFillColorChoice(String key, int keyCode) {
    createFillColorChoice(key, keyCode, Editor.LAYOUT_PARAGRAPH_GAP);
  }
  public void createFillColorChoice(String key, int keyCode, String layout) {

    // create the fill color label + chooser
    JLabel fillColorLabel = generator.generateJLabel("GenericEditor.fillColorLabel");
    cp.add(fillColorLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    FillablePrimitive ptgo = (FillablePrimitive)getCurrentObject(false);
    Color initialColor = (ptgo == null) ? Color.black : ptgo.getFillColor();
    fillColorChooser = createColorChooser("fillColor", "GenericEditor.chooseColor",
        key, initialColor);
    ExtendedActionButton fillColorChooserButton = new ExtendedActionButton(fillColorChooser,
        keyCode);
    cp.add(fillColorChooserButton, layout);
  }

  public Box createColorBox(TranslatableGUIElement generator) {
    Box colorBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        "GenericEditor.colorBL");

    // first row: polyline / polygon color
    Box firstRowBox = new Box(BoxLayout.LINE_AXIS);
    firstRowBox.add(generator.generateJLabel("GenericEditor.colorLabel")); // color
    Color initialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTSquare) getCurrentObject(false)).getColor();
    colorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "color", AnimalTranslator
        .translateMessage("GenericEditor.chooseColor",
            new Object[] { AnimalTranslator
                .translateMessage("OpenObjectEditor.outline") }), initialColor);
    firstRowBox.add(new ExtendedActionButton(colorChooser, KeyEvent.VK_C));
    colorBox.add(firstRowBox);

    // second row: polygon fill color
    Box secondRowBox = new Box(BoxLayout.LINE_AXIS);
    secondRowBox.add(generator.generateJLabel("GenericEditor.fillColorLabel"));
    initialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTSquare) getCurrentObject(false)).getFillColor();
    fillColorChooser = new ColorChooserAction(this, 
        ColorChoice.getColorName(initialColor), "fillColor", 
        AnimalTranslator.translateMessage("GenericEditor.chooseColor",
          new Object[] { 
            AnimalTranslator.translateMessage("GenericEditor.fillColor") }),
          initialColor);
    ExtendedActionButton fillColorChooserButton = new ExtendedActionButton(fillColorChooser,
        KeyEvent.VK_F);
    secondRowBox.add(fillColorChooserButton);
    colorBox.add(secondRowBox);
    addBox(colorBox);
    return colorBox;
  }
  
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    FillablePrimitive p = (FillablePrimitive)getCurrentObject();

    if (p != null) {
      if (Animation.get() != null)
        Animation.get().doChange();
      repaintNow();
    }
  }
 
  protected void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
   
    // add fillable properties
    FillablePrimitive p = (FillablePrimitive)eo;
    filledCB.setEnabled(true);
    filledCB.setSelected(p.isFilled());
    fillColorChooser.setColor(p.getFillColor());
  }

  @Override
  public void getProperties(XProperties props) {
    super.getProperties(props);
    String baseKey = getBasicType();
    props.put(baseKey + ".filled", filledCB.isSelected());
    props.put(baseKey + ".fillColor", fillColorChooser.getColor());
  }

  public void setProperties(XProperties props) {
    super.setProperties(props);
    String baseKey = getBasicType();
    filledCB.setSelected(props.getBoolProperty(baseKey
        + ".filled"));
    fillColorChooser.setColor(props.getColorProperty(baseKey
        + ".fillColor", Color.black));
  }
  
  protected void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    FillablePrimitive p = (FillablePrimitive) eo;
    p.setFilled(filledCB.isSelected());
    p.setFillColor(fillColorChooser.getColor());
  }

  /**
   * enable or disable some CheckBoxes according to whether the polyline is
   * closed or not
   */
  public void itemStateChanged(ItemEvent e) {
    FillablePrimitive p = (FillablePrimitive)getCurrentObject();

    if (e.getSource() == filledCB) {
      if (p != null)
        p.setFilled(filledCB.isSelected());
    }

    Animation.get().doChange();
    repaintNow();
  } // itemStateChanged

  public void propertyChange(PropertyChangeEvent event) {
    super.propertyChange(event);
    FillablePrimitive poly = (FillablePrimitive)getCurrentObject();
    String eventName = event.getPropertyName();
    if ("fillColor".equals(eventName))
      poly.setFillColor((Color) event.getNewValue());
    if (!event.getOldValue().equals(event.getNewValue())) {
      repaintNow();
      if (Animation.get() != null)
        Animation.get().doChange();
    }
  }
} // FillablePrimitiveEditor
