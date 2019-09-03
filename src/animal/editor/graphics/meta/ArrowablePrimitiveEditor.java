package animal.editor.graphics.meta;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import animal.editor.Editor;
import animal.graphics.meta.ArrowablePrimitive;
import animal.main.Animation;
import animal.misc.EditableObject;
import animal.misc.XProperties;

public abstract class ArrowablePrimitiveEditor extends GraphicEditor 
implements ActionListener, ItemListener {

  protected JCheckBox bwArrow;

  protected JCheckBox fwArrow;

  public ArrowablePrimitiveEditor() {
    this(false);
  }

  public ArrowablePrimitiveEditor(boolean displayMode) {
    super(displayMode);
  }
  
  @Override
  protected void buildGUI() {
    
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
    
    // finish with the standard editors
    // for primitives: depth and object name, plus OK/apply/cancel
    finishEditor(cp);
  }

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    ArrowablePrimitive p = (ArrowablePrimitive) getCurrentObject();

    if (p != null) {
      if (Animation.get() != null)
        Animation.get().doChange();
      repaintNow();
    }
  }
  
  protected void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    ArrowablePrimitive p = (ArrowablePrimitive) eo;
    p.setBWArrow(bwArrow.isSelected());
    p.setFWArrow(fwArrow.isSelected());
  }

  protected void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    ArrowablePrimitive p = (ArrowablePrimitive) eo;
    bwArrow.setSelected(p.hasBWArrow());
    fwArrow.setSelected(p.hasFWArrow());
  }

  public void getProperties(XProperties props) {
    super.getProperties(props);
    String key = getBasicType();
    props.put(key + ".bwArrow", bwArrow.isSelected());
    props.put(key + ".fwArrow", fwArrow.isSelected());
  }
  
  public void setProperties(XProperties props) {
    super.setProperties(props);
    String key = getBasicType();
    bwArrow.setSelected(props.getBoolProperty(key + ".bwArrow"));
    fwArrow.setSelected(props.getBoolProperty(key + ".fwArrow"));
  }

  /**
   * enable or disable some CheckBoxes according to whether the polyline is
   * closed or not
   */
  public void itemStateChanged(ItemEvent e) {
    ArrowablePrimitive p = (ArrowablePrimitive)getCurrentObject();
    if (p == null)
      return;
    if (e.getSource() == fwArrow) {
        p.setFWArrow(fwArrow.isSelected());
    }

    if (e.getSource() == bwArrow) {
        p.setBWArrow(bwArrow.isSelected());
    }
    Animation.get().doChange();
    repaintNow();
  } // itemStateChanged
}
