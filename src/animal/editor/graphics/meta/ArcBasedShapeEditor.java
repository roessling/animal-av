/*
 * Created on 18.07.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.editor.graphics.meta;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import translator.TranslatableGUIElement;
import animal.graphics.PTSquare;
import animal.misc.ColorChoice;
import animal.misc.ColorChooserAction;

/**
 * support for arc-based shapes
 * @author roessling
 * @version 2.5 2008-06-23
 */
public abstract class ArcBasedShapeEditor extends GraphicEditor {
  /**
	 * 
	 */
	private static final long serialVersionUID = 4033981355799037393L;

	protected JCheckBox filledCB;

  protected ColorChooserAction fillColorChooser;

  protected ExtendedActionButton fillColorChooserButton;

//  protected ColorChooserAction colorChooser; 
  
  public Box createCommonElements(TranslatableGUIElement generator) {
    return createColorBox(generator);
  }

  public Box createColorBox(TranslatableGUIElement generator) {
    Box colorBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        "GenericEditor.colorBL");

    // first row: polyline / polygon color
    Box firstRowBox = new Box(BoxLayout.LINE_AXIS);
    firstRowBox.add(generator.generateJLabel("GenericEditor.colorLabel")); // color
    Color initialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTSquare) getCurrentObject(false)).getColor();
    colorChooser = createColorChooser("OpenObjectEditor.outline", initialColor);
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
    fillColorChooserButton = new ExtendedActionButton(fillColorChooser,
        KeyEvent.VK_F);
    secondRowBox.add(fillColorChooserButton);
    colorBox.add(secondRowBox);
    addBox(colorBox);
    return colorBox;
  }
} // ArcBasedShapeEditor
