package animal.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import translator.AnimalTranslator;
import animal.graphics.PTGraphicObject;

public class IndexedContentChooserListSupport extends IndexedContentChooser {
  // TODO beim Objektwechsel die Liste loeschen, oder fuer jede Objektart eine
  // Liste verwalten
  public static final String                                    ADD_BUTTON_LABEL    = "addItem";

  public static final String                                    DELETE_BUTTON_LABEL = "deleteItem";

  public static final String                                    EDIT_BUTTON_LABEL   = "editItem";

  public static final String                                    OK_BUTTON_LABEL     = "ok";

  public static final String                                    CANCEL_BUTTON_LABEL = "cancel";

  public static final String                                    BOX_LABEL           = "IndexedContenChooser.selectObjects";

  private AbstractButton                                        addButton,
      deleteButton, editButton;

  private JList<String>                                         indexList;

  private AbstractButton                                        okButton,
      cancelButton;

  private Hashtable<String, Vector<Integer>>                    indicesTable        = new Hashtable<String, Vector<Integer>>();

  @SuppressWarnings("unused")
  private boolean                                               editMode;

  private Hashtable<String, DefaultListModel<String>>           listModels          = new Hashtable<String, DefaultListModel<String>>();

  private Hashtable<String, Hashtable<String, Vector<Integer>>> indexTables         = new Hashtable<String, Hashtable<String, Vector<Integer>>>();

  public IndexedContentChooserListSupport() {
    this(BOX_LABEL);
  }

  public IndexedContentChooserListSupport(String boxLabel) {
    super(boxLabel);
    // create Editrow
    this.getContentBox().add(createEditRow());
    // create List
    this.getContentBox().add(createList());
    // create OKCancel
    this.getContentBox().add(createOkCancelRow());
    //
    this.editMode = false;
    // initListModels
  }

  public void setData(PTGraphicObject[] objects, Vector<String> methods) {
    super.setData(objects, methods);
    createListModels();
  }

  /**
   * creates list models for the different kinds of objects
   */
  private void createListModels() {
    this.listModels.clear();
    this.indexTables.clear();
    for (IndexableObjectInformation info : this.indexableObjects.values()) {
      listModels.put(info.getIdentifier(), new DefaultListModel<String>());
      indexTables.put(info.getIdentifier(),
          new Hashtable<String, Vector<Integer>>());
    }
    String selectedobject = this.getSelectedKindOfObject();
    if (selectedobject != null) {
      this.indexList.setModel(listModels.get(selectedobject));
      this.indicesTable = indexTables.get(selectedobject);
    }
  }

  private Component createEditRow() {
    Box buttonBox = new Box(BoxLayout.LINE_AXIS);
    buttonBox.add(addButton = AnimalTranslator.getGUIBuilder().generateJButton(
        ADD_BUTTON_LABEL, null, false, this));
    buttonBox.add(deleteButton = AnimalTranslator.getGUIBuilder()
        .generateJButton(DELETE_BUTTON_LABEL, null, false, this));
    buttonBox.add(editButton = AnimalTranslator.getGUIBuilder()
        .generateJButton(EDIT_BUTTON_LABEL, null, false, this));
    return buttonBox;
  }

  private Component createList() {
    indexList = new JList<String>();
    indexList.setVisibleRowCount(5);
    JScrollPane scp = new JScrollPane(indexList);
    return scp;
  }

  private Component createOkCancelRow() {
    Box buttonBox = new Box(BoxLayout.LINE_AXIS);
    buttonBox.add(okButton = AnimalTranslator.getGUIBuilder().generateJButton(
        OK_BUTTON_LABEL, null, false, this));
    okButton.setVisible(false);
    buttonBox.add(cancelButton = AnimalTranslator.getGUIBuilder()
        .generateJButton(CANCEL_BUTTON_LABEL, null, false, this));
    cancelButton.setVisible(false);
    return buttonBox;
  }

  public void actionPerformed(ActionEvent e) {

    super.actionPerformed(e);
    if (e.getSource() == okButton) {
      handleOKEvent();
    } else if (e.getSource() == cancelButton) {
      handleCancelEvent();
    } else if (e.getSource() == addButton) {
      addIndicesToList(-1, this.getSelectedIndices());
    } else if (this.indexList.getSelectedValue() != null) {
      if (e.getSource() == deleteButton) {
        deleteIndicesFromList();
      } else if (e.getSource() == editButton) {
        editFirstSelectedObject();
      }
    } else if (e.getSource() == kindOfObjectCB) {
      if (!this.listModels.containsKey(getSelectedKindOfObject()))
        this.listModels.put(this.getSelectedKindOfObject(),
            new DefaultListModel<String>());
      this.indexList.setModel(this.listModels.get(this
          .getSelectedKindOfObject()));
      this.indicesTable = this.indexTables.get(getSelectedKindOfObject());
    }

  }

  private void handleOKEvent() {
    if (!this.indexList.getSelectedValue().toString()
        .equals(this.getTextualIndexRepresentation(this.getSelectedIndices()))) {
      // save position of selectedObject in List
      int selectedObjectListIndex = this.indexList.getSelectedIndex();
      // get int representation of selected Indices

      // TODO seems to have no function at all...
      // Vector<Integer> intRep =
      // this.indicesTable.get(indexList.getSelectedValue().toString());
      // deleteSelectedIndex from List and Table
      this.indicesTable.remove(this.getSelectedStrings().get(0));
      deleteStringsFromList(this.getSelectedStrings());
      // get selected Indices from dimensionComboBoxes
      this.addIndicesToList(selectedObjectListIndex, this.getSelectedIndices());
      // enable old indices
      // this.enableIndices(intRep);
      // hide OK- and Cancel-button
      hideOKCancel();
      // enable GUI
      enableGUIAfterEdit();
    } else
      // Wenn die Indices gleich geblieben sind entspricht die Semantik einem
      // Cancel
      handleCancelEvent();
    this.editMode = false;
  }

  private void handleCancelEvent() {
    // disable first selected Object as if it was added for the first time
    disableIndicesOfFirstSelectedObject();
    // hide OK- and Cancel-button
    hideOKCancel();
    // enable GUI
    enableGUIAfterEdit();
    this.editMode = false;
  }

  private void enableGUIAfterEdit() {
    this.addButton.setEnabled(true);
    this.deleteButton.setEnabled(true);
    this.editButton.setEnabled(true);
    this.indexList.setEnabled(true);
    this.methodCB.setEnabled(true);
    this.kindOfObjectCB.setEnabled(true);
  }

  private void hideOKCancel() {
    this.okButton.setVisible(false);
    this.cancelButton.setVisible(false);
  }

  private void disableIndicesOfFirstSelectedObject() {
    Object selectedObject = this.indexList.getSelectedValue();
    if (selectedObject != null)
      if (this.indicesTable.containsKey(selectedObject.toString())) {
        Vector<Integer> intIndices = this.indicesTable.get(selectedObject
            .toString());
        disableIndices(intIndices);
      }
  }

  private void editFirstSelectedObject() {
    // Set edit mode
    this.editMode = true;
    // disable other Objects
    disableGUIForEdit();
    // select only first Object
    selectSelectedObjectWithSmallestIndex();
    // enable first selected Object so it appears in the selection combo boxes
    enableFirstSelectedObject();
    // show OK-Cancel-buttons
    showOKCancel();
  }

  private void showOKCancel() {
    this.okButton.setVisible(true);
    this.cancelButton.setVisible(true);
  }

  private void selectSelectedObjectWithSmallestIndex() {
    Object firstSelectedValue = this.indexList.getSelectedValue();
    indexList.getSelectionModel().clearSelection();
    this.indexList.setSelectedValue(firstSelectedValue, true);
  }

  private void enableFirstSelectedObject() {
    Object selectedObject = this.indexList.getSelectedValue();
    if (selectedObject != null)
      if (this.indicesTable.containsKey(selectedObject.toString())) {
        Vector<Integer> intIndices = this.indicesTable.get(selectedObject
            .toString());
        enableIndices(intIndices);
      }
  }

  private void disableGUIForEdit() {
    this.addButton.setEnabled(false);
    this.deleteButton.setEnabled(false);
    this.editButton.setEnabled(false);
    this.indexList.setEnabled(false);
    this.methodCB.setEnabled(false);
    this.kindOfObjectCB.setEnabled(false);
  }

  private void deleteIndicesFromList() {
    Vector<String> selectedStrings = getSelectedStrings();
    Vector<Integer> intIndices;
    for (int i = 0; i < selectedStrings.size(); ++i) {
      if (this.indicesTable.containsKey(selectedStrings.get(i))) {
        intIndices = this.indicesTable.get(selectedStrings.get(i));
        this.indicesTable.remove(selectedStrings.get(i));
        enableIndices(intIndices);
      }
    }
    deleteStringsFromList(selectedStrings);
  }

  private void deleteStringsFromList(Vector<String> selectedStrings) {
    ListModel<String> model = this.indexList.getModel();
    if (model instanceof DefaultListModel) {
      DefaultListModel<String> defModel = (DefaultListModel<String>) model;
      for (int i = 0; i < selectedStrings.size(); ++i)
        defModel.removeElement(selectedStrings.get(i));
    }

  }

  private void enableIndices(Vector<Integer> indices) {
    IndexableObjectInformation tmpInfo = this
        .getObjectInformation(this.kindOfObjectCB.getSelectedItem().toString());
    // CB suchen die "all" selektiert.
    Vector<Integer> chosenIndices = searchFirstSelectionOfAll(tmpInfo);
    // indices in den dimensionTree einfuegen
    tmpInfo.getPrimaryDimension().enableIndexEntries(indices);
    // Fuer die nachfolgenden subdimensionen die Modelle neu anfordern.
    updateDimensionBoxModels(chosenIndices);
  }

  // TODO check, have commented these as no one calls them...
  // private void setEditMode(boolean isInEditMode) {
  // editMode = isInEditMode;
  // }
  //
  // private boolean inEditMode() {
  // return editMode;
  // }
  private Vector<String> getSelectedStrings() {
    Vector<String> selectedStrings = new Vector<String>();
    List<String> selectedObjects = this.indexList.getSelectedValuesList();
    for (String s : selectedObjects)
      selectedStrings.add(s.toString());
    return selectedStrings;
  }

  /**
   * Adds indices selected in the index boxes to the list and disables them in
   * the index boxes
   * 
   * @param index
   * @param indices
   */
  private void addIndicesToList(int index, Vector<Integer> indices) {
    String textualIndexRepresentation = getTextualIndexRepresentation(indices);
    if (textualIndexRepresentation != null
        && !this.indicesTable.containsKey(textualIndexRepresentation)) {
      if (index >= 0 && index < this.indexList.getModel().getSize())
        this.indexList.setModel(addToModel(this.indexList.getModel(),
            textualIndexRepresentation, index));
      else
        this.indexList.setModel(addToModel(this.indexList.getModel(),
            textualIndexRepresentation, this.indexList.getModel().getSize()));
      disableIndices(indices);
      this.indicesTable.put(textualIndexRepresentation, indices);
    }
  }

  private void disableIndices(Vector<Integer> indices) {
    IndexableObjectInformation tmpInfo = this
        .getObjectInformation(this.kindOfObjectCB.getSelectedItem().toString());
    // CB suchen die "all" selektiert.
    Vector<Integer> chosenIndices = searchFirstSelectionOfAll(tmpInfo);
    // indices aus dem dimensionTree entfernen
    tmpInfo.getPrimaryDimension().disableIndexEntries(indices);
    // Fuer die nachfolgenden subdimensionen die Modelle neu anfordern.
    updateDimensionBoxModels(chosenIndices);
  }

  private Vector<Integer> searchFirstSelectionOfAll(
      IndexableObjectInformation tmpInfo) {
    int index = 0;
    Vector<Integer> chosenIndices = new Vector<Integer>();
    String translatedAll = AnimalTranslator
        .translateMessage(DimensionData.DIMENSION_INDEX_ALL);
    while (index < tmpInfo.getDimensionCount()
        && !translatedAll.equals((String) this.dimensionCBs.get(index)
            .getSelectedItem())) {
      chosenIndices.add(getSelectedIntValue(index));
      ++index;
    }
    return chosenIndices;
  }

  private ListModel<String> addToModel(ListModel<String> model, String value,
      int index) {
    if (!(model instanceof DefaultListModel)/* model.getSize() < 1 */) {
      DefaultListModel<String> defModel = new DefaultListModel<String>();
      defModel.addElement(value);
      // add the new Model to the list models for the appropriate kind of object
      this.listModels.put(this.getSelectedKindOfObject(), defModel);
      return defModel;
    } else if (model instanceof DefaultListModel) {
      DefaultListModel<String> defModel = (DefaultListModel<String>) model;
      if (index < model.getSize() && index >= 0) {
        defModel.add(index, value);
      } else {
        defModel.addElement(value);
      }
      return defModel;
    }
    return model;
  }

  private String getTextualIndexRepresentation(Vector<Integer> indices) {
    if (indices != null) {
      String returnValue = "[";
      for (int i = 0; i < indices.size(); ++i) {
        if (indices.get(i) != null)
          returnValue += indices.get(i) + ",";
        else
          return null;
      }
      if (returnValue.length() > 1)
        returnValue = returnValue.substring(0, returnValue.length() - 1);
      returnValue += "]";
      return returnValue;
    }
    return null;
  }

  public String getChosenIndexTuples() {
    String returnValue = "{";
    for (Iterator<String> it = this.indicesTable.keySet().iterator(); it
        .hasNext();)
      returnValue += it.next();
    return returnValue + "}";
  }

  /**
   * Adds indices to the List and disables their selection in the ComboBoxes
   * 
   * @param values
   */
  public void addIndicesToList(Vector<Vector<Integer>> values) {
    IndexableObjectInformation info = this
        .getObjectInformation(this.kindOfObjectCB.getSelectedItem().toString());
    int indexCount = info.getDimensionCount();
    // clear List
    this.indexList.setModel(new DefaultListModel<String>());
    // add index tuples one after the other
    for (Vector<Integer> indexTuple : values) {
      if (indexTuple.size() == indexCount)
        addIndicesToList(-1, indexTuple);
    }
  }

  public boolean setSelectedKindOfObject(String string) {
    String translatedString = AnimalTranslator.translateMessage(string);
    this.kindOfObjectCB.setSelectedItem(translatedString);
    if (this.kindOfObjectCB.getSelectedItem() != null
        && translatedString.equals(this.kindOfObjectCB.getSelectedItem()))
      return true;
    return false;
  }

  public boolean setSelectedMethod(String string) {
    String translatedString = AnimalTranslator.translateMessage(string);
    this.methodCB.setSelectedItem(translatedString);
    if (this.methodCB.getSelectedItem() != null
        && translatedString.equals(this.methodCB.getSelectedItem()))
      return true;
    return false;
  }

}
