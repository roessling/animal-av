/*
 * Created on 29.08.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.editor.graphics.meta;

import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import animal.editor.Editor;
import animal.graphics.meta.OrientedPrimitive;
import animal.main.Animation;
import animal.misc.EditableObject;
import animal.misc.XProperties;

/**
 * abstract editor for oriented arc based shapes
 * 
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 2.5 2008-06-23
 */
public abstract class OrientedArrowablePrimitiveEditor extends ArrowablePrimitiveEditor {
  /**
	 * 
	 */
	private static final long serialVersionUID = 6199592133277102205L;
	protected JCheckBox clockwise;
	
  protected void buildGUI() {
    initializeLayoutComponents();
    
    // add basic color choice
    createGenericColorSetting("OpenObjectEditor.outline", KeyEvent.VK_C);

    // options for forward/backward arrows
    insertSeparator("LineEditor.propertiesBL", cp, generator);

    JLabel arrowLabel = generator.generateJLabel("ArrowableShapeEditor.arrowLabel");
    cp.add(arrowLabel, Editor.LAYOUT_PARAGRAPH_GAP);

    fwArrow = generator.generateJCheckBox("ArrowableShapeEditor.fwArrow", null, this);
    fwArrow.addItemListener(this);
    bwArrow = generator.generateJCheckBox("ArrowableShapeEditor.bwArrow", null, this);
    bwArrow.addItemListener(this);
    cp.add(fwArrow, Editor.LAYOUT_PARAGRAPH_GAP);
    cp.add(bwArrow, Editor.LAYOUT_WRAP);

    JLabel clockwiseLabel = generator.generateJLabel("ArcBasedShapeEditor.orientation");
    cp.add(clockwiseLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    
    // add orientation
    clockwise = generator.generateJCheckBox("ArcBasedShapeEditor.clockwise",
        null, this);
    clockwise.addItemListener(this);
    cp.add(clockwise, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);

    // finish with the standard editors
    // for primitives: depth and object name, plus OK/apply/cancel
    finishEditor(cp);
  }

  public void getProperties(XProperties props) {
    super.getProperties(props);
    String baseKey = getBasicType();
    props.put(baseKey + ".clockwise", clockwise.isSelected());
  }
  
  public void setProperties(XProperties props) {
    super.setProperties(props);
    String baseKey = getBasicType();
    clockwise.setSelected(props.getBoolProperty(baseKey + ".clockwise"));
  }

  protected void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    OrientedPrimitive p = (OrientedPrimitive) eo;
    clockwise.setSelected(p.isClockwise());
  }


  protected void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    OrientedPrimitive p = (OrientedPrimitive)eo;
    p.setClockwise(clockwise.isSelected());
  }
  
  /**
   * enable or disable some CheckBoxes according to whether the polyline is
   * closed or not
   */
  public void itemStateChanged(ItemEvent e) {
    super.itemStateChanged(e);
    OrientedPrimitive p = (OrientedPrimitive)getCurrentObject();
    if (e.getSource() == clockwise)
      p.setClockwise(clockwise.isSelected());

    Animation.get().doChange();
    repaintNow();
  } // itemStateChanged
}
