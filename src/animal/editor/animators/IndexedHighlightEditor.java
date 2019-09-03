package animal.editor.animators;

import animal.animator.IndexedHighlight;
import animal.editor.Editor;
import animal.misc.EditableObject;

public class IndexedHighlightEditor extends IndexedTimedAnimatorEditor {

  /**
   * 
   */
  private static final long serialVersionUID = 6791880197682680850L;

  public IndexedHighlightEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    finishBoxes();
  }

  /**
   * Open a secondary IndexedSetTextEditor window
   * 
   * @param eo
   *          the object from which to take the current settings
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    IndexedHighlightEditor result = new IndexedHighlightEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  @Override
  public EditableObject createObject() {
    IndexedHighlight returnValue = new IndexedHighlight();
    storeAttributesInto(returnValue);
    return returnValue;
  }
}
