/*
 * Created on 08.04.2005 by T. Ackermann
 */

package generators.framework;

import generators.framework.components.ArrayInputPanel;
import generators.framework.components.ColorChooserComboBox;
import generators.framework.components.DoubleTextField;
import generators.framework.components.EnumerationChooserComboBox;
import generators.framework.components.FontChooserComboBox;
import generators.framework.components.IntegerTextField;
import generators.framework.components.MatrixInputPanel;
import generators.framework.components.StringArrayInputPanel;
import generators.framework.properties.tree.PropertiesTree;
import generators.framework.properties.tree.PropertiesTreeModel;
import generators.framework.properties.tree.PropertiesTreeNode;
import generators.framework.properties.tree.PropertiesTreePane;
import gfgaa.gui.GraphScriptPanel;
import gfgaa.gui.others.PanelManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import algoanim.primitives.Graph;
import algoanim.properties.CallMethodProperties;


/**
 * This Panel shows AnimationProperties and allows the user to change them.
 * 
 * @author T. Ackermann
 */
public class PropertiesPanel extends JSplitPane implements
		TreeSelectionListener, ActionListener {

	/** stores the initial (and minimal) width of the Tree */
	private static final int MIN_TREE_WIDTH = 200;

	/** we store this because PropertiesPanel is serializable */
	private static final long serialVersionUID = 3762254145129952565L;

	/** are the design mode controls shown? */
	boolean designMode = false;

	/** stores the currently selected Node */
	PropertiesTreeNode selectedNode = null;

	/** stores the selected Folder String */
	String selectedDisplay = "";

	/** stores the TreePane at the left */
	private PropertiesTreePane treePane;

	/** stores the Tree */
	private PropertiesTree tree;

	/** stores the inner Panel at the right */
	private JPanel panelRight;

	/** stores the bordered label at the upper right */
	private JLabel labelHead;

	/** stores the Panel for labelGeneral */
	private JPanel lineGeneral;

	/** stores the Label for the General Text */
	private JLabel labelGeneral;

	/** stores the Panel for checkIsEditable */
	private JPanel lineIsEditable;

	/** stores the CheckBox for the isEditable Value */
	private JCheckBox checkIsEditable;

	/** stores the Panel for textLabel */
	private JPanel lineLabel;

  /** stores the TextField for the label Value */
  private JTextField textLabel;
	
	/** stores the TextField for the description Value */
	private JPanel textDescription;

	/** stores the Panel for value */
	private JPanel lineValue;

	/** stores the Component that allows entering the value */
	private JComponent objValue;
	
	/** stores the Component that allows entering the value */
	private JComponent objDescription;

	/** stores the setToDefault-Button */
	private JButton buttonSetToDefault;

	/** stores the Panel for the setToDefault-Button */
	private JPanel lineSetToDefault;

	/** stores the Listener for called Methods */
	private transient PropertiesPanelListener listener = null;

	/**
	 * Constructor creates a new PropertiesPanel-Object. This constructor should
	 * be used by the GeneratorGUI because Design Mode is disabled. Later loadURI
	 * should be called.
	 */
	public PropertiesPanel() {
		super();
		tree = new PropertiesTree();
		
		setFinalMode();
		init();
	}

	/**
	 * Constructor creates a new PropertiesPanel-Object. This constructor should
	 * be used by the GeneratorGUI because Design Mode is disabled and the tree is
	 * constructed based on the given XML file.
	 * 
	 * @param uri
	 *          The Name of the XML-Ressource-File.
	 * @throws IllegalArgumentException
	 *           if something goes wrong.
	 */
	public PropertiesPanel(String uri) throws IllegalArgumentException {
		super();
		tree = new PropertiesTree();
		setWorkingMode();
		loadURI(uri);
		init();
	}

	/**
	 * Constructor creates a new PropertiesPanel-Object. This constructor should
	 * be used by the PropertiesGUI because Design Mode is enabled.
	 * 
	 * @param newTree
	 *          Pointer to an existing PropertiesTree.
	 */
	public PropertiesPanel(PropertiesTree newTree) {
		super();
		PropertiesTree concreteTree = newTree;
		if (concreteTree == null)
		  concreteTree = new PropertiesTree();
		tree = concreteTree;
		setWorkingMode();
		init();
	}

	/**
	 * init initializes all the GUI controls.
	 */
	private void init() {

		// init SplitPane
		panelRight = new JPanel();
		panelRight.setLayout(new BoxLayout(panelRight,
				BoxLayout.PAGE_AXIS));
		panelRight.add(Box.createRigidArea(new Dimension(0, 10)));

		// labelHead
		labelHead = new JLabel();
		labelHead.setOpaque(true);
		labelHead.setBackground(Color.white);
		labelHead.setBorder(BorderFactory.createLineBorder(Color.black));
		labelHead.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

		JPanel line = new JPanel();
		line.setLayout(new BoxLayout(line, BoxLayout.LINE_AXIS));
		line.add(Box.createRigidArea(new Dimension(10, 0)));
		line.add(labelHead);
		line.add(Box.createRigidArea(new Dimension(10, 0)));
		panelRight.add(line);
		panelRight.add(Box.createRigidArea(new Dimension(0, 24)));

		// labelGeneral
		labelGeneral = new JLabel();
		if (designMode)
			labelGeneral
					.setText("<html>Please select a Property Item or a"
							+ " Primitive and<br />change its value to the value that should"
							+ " be used as<br />the default value.<br /><br />You can also "
							+ "make a Property Item invisible to the users<br />by unchecking the upper "
							+ "checkbox.<br /><br />The Label stores the text that "
							+ "describes<br />the Property Item.</html>");
		else
			labelGeneral.setText("<html>Please select a Property at "
					+ "the left and change its value.</html>");

		labelGeneral.setMaximumSize(new Dimension(Integer.MAX_VALUE,
				labelGeneral.getMaximumSize().height));
		lineGeneral = new JPanel();
		lineGeneral.setLayout(new BoxLayout(lineGeneral,
				BoxLayout.LINE_AXIS));
		lineGeneral.add(Box.createRigidArea(new Dimension(10, 0)));
		lineGeneral.add(labelGeneral);
		lineGeneral.add(Box.createRigidArea(new Dimension(10, 0)));
		panelRight.add(lineGeneral);

		if (designMode) {
			// checkIsEditable
			checkIsEditable = new JCheckBox(
					"This value is editable by the user", true);
			checkIsEditable.setMaximumSize(new Dimension(Integer.MAX_VALUE,
					checkIsEditable.getMaximumSize().height));
			lineIsEditable = new JPanel();
			lineIsEditable.setLayout(new BoxLayout(lineIsEditable,
					BoxLayout.LINE_AXIS));
			lineIsEditable.add(Box.createRigidArea(new Dimension(10, 0)));
			lineIsEditable.add(checkIsEditable);
			lineIsEditable.add(Box.createRigidArea(new Dimension(10, 0)));
			lineIsEditable.setVisible(false);
			panelRight.add(lineIsEditable);
			panelRight.add(Box.createRigidArea(new Dimension(0, 8)));

			// textLabel
			textLabel = new JTextField();
			textLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Math.max(
					textLabel.getMinimumSize().height, 25)));
			lineLabel = new JPanel();
			lineLabel.setLayout(new BoxLayout(lineLabel,
					BoxLayout.LINE_AXIS));
			lineLabel.add(Box.createRigidArea(new Dimension(10, 0)));
			lineLabel.add(new JLabel("Label:"));
			lineLabel.add(Box.createRigidArea(new Dimension(10, 0)));
			lineLabel.add(textLabel);
			lineLabel.add(Box.createRigidArea(new Dimension(10, 0)));
			lineLabel.setVisible(false);
			panelRight.add(lineLabel);
			panelRight.add(Box.createRigidArea(new Dimension(0, 16)));

			textDescription = new JPanel();
			textDescription.setLayout(new BoxLayout(textDescription, BoxLayout.LINE_AXIS));
			textDescription.add(Box.createRigidArea(new Dimension(10, 0)));
			textDescription.add(new JLabel("Description:"));
			textDescription.add(Box.createRigidArea(new Dimension(10, 0)));
			textDescription.setVisible(false);
			panelRight.add(textDescription);
			panelRight.add(Box.createRigidArea(new Dimension(0, 32)));
		}

		// lineValue
		lineValue = new JPanel();
		lineValue.setLayout(new BoxLayout(lineValue, BoxLayout.LINE_AXIS));
		lineValue.add(Box.createRigidArea(new Dimension(10, 0)));
		lineValue.add(new JLabel("Value:"));
		lineValue.add(Box.createRigidArea(new Dimension(10, 0)));
		lineValue.setVisible(false);
		panelRight.add(lineValue);
		panelRight.add(Box.createRigidArea(new Dimension(0, 32)));
    
		// setToDefault
		buttonSetToDefault = new JButton(
				"Set this property to its default value");
		buttonSetToDefault.setActionCommand("setToDefault");
		buttonSetToDefault.addActionListener(this);
		buttonSetToDefault.setMaximumSize(new Dimension(Integer.MAX_VALUE,
				buttonSetToDefault.getMaximumSize().height));
		lineSetToDefault = new JPanel();
		lineSetToDefault.setLayout(new BoxLayout(lineSetToDefault,
				BoxLayout.LINE_AXIS));
		lineSetToDefault.add(Box.createRigidArea(new Dimension(10, 0)));
		lineSetToDefault.add(buttonSetToDefault);
		lineSetToDefault.add(Box.createRigidArea(new Dimension(10, 0)));
		lineSetToDefault.setVisible(false);
		panelRight.add(lineSetToDefault);
		panelRight.add(Box.createRigidArea(new Dimension(0, 8)));

		JScrollPane scrollRight = new JScrollPane(panelRight);

		treePane = new PropertiesTreePane(tree);
		setLeftComponent(treePane);
		setRightComponent(scrollRight);
		setDividerLocation(MIN_TREE_WIDTH);

		tree.addTreeSelectionListener(this);
	}

	/**
	 * loadURI opens the given TreeModel. This method should be used by the
	 * GeneratorGUI because the tree is constructed based on the given
	 * XML-Ressource-File.
	 * 
	 * @param uri
	 *          The Name of the XML-Ressource-File.
	 * @throws IllegalArgumentException
	 *           if something goes wrong.
	 */
	public void loadURI(String uri) throws IllegalArgumentException {
		setFinalMode();
		((PropertiesTreeModel) tree.getModel()).loadFromXMLFile(uri, true);
		tree.expandAllFolders();
		tree.updateUI();
	}

	/**
	 * updateRightPanelGeneral updates the right Panel when a Type is selected. A
	 * general information is displayed for this type.
	 */
	protected void updateRightPanelGeneral() {
		lineGeneral.setVisible(true);
		if (designMode) {
			lineIsEditable.setVisible(false);
			lineLabel.setVisible(false);
		}
		lineValue.setVisible(false);
		textDescription.setVisible(false);
		lineSetToDefault.setVisible(false);
	}

	/**
	 * updateRightPanelItem updates the right Panel when an Item is selected in
	 * the Tree. The user is allowed to change the value of this Property.
	 */
	private void updateRightPanelItem() {
		if (selectedNode == null)
			return;

		boolean bCallMethod = false;
		if (selectedNode.isProperty()
				|| selectedNode.getAnimationProperties() instanceof CallMethodProperties) {
			bCallMethod = true;
			// create a fake item-node to work with...
			PropertiesTreeNode n = new PropertiesTreeNode("", "methodName");
			n.setParent(selectedNode);
			selectedNode = n;
		}

		if (selectedNode == null || !selectedNode.isItem())
			return;

		// get Parent-Node
		PropertiesTreeNode p = (PropertiesTreeNode) selectedNode.getParent();
		if (!p.isProperty())
			return;

		if (designMode) {
			textLabel.setText(p.getAnimationProperties().getLabel(
					selectedNode.getName()));
			checkIsEditable.setSelected(p.getAnimationProperties()
					.getIsEditable(selectedNode.getName()));
			checkIsEditable.setVisible(!bCallMethod);
		}

		// update the value Panel
		if (objValue != null) {
			lineValue.remove(objValue);
			lineValue.remove(lineValue.getComponentCount() - 1);
			objValue = null;
		}
		if (objDescription != null) {
			textDescription.remove(objDescription);
			textDescription.remove(textDescription.getComponentCount() - 1);
			objDescription = null;
		}

		if (!designMode && bCallMethod) {
			// selectedPropertyName has to be "methodName"
			objValue = new JButton(p.getAnimationProperties().getLabel(
					"methodName"));
			((JButton) objValue).setActionCommand("call:"
					+ p.getAnimationProperties().get("methodName"));
			((JButton) objValue).addActionListener(this);
		} else {
				// Madieha :Aktualisieren der Graphproperties directed und weighted
		      Graph g = PropertiesGUI.getGraphFromScriptFile();
		      if (g != null && selectedNode.getName().equals("weighted")) {
		        p.getAnimationProperties().set("weighted",
		            g.getProperties().get("weighted"));
		      }
		      if (g != null && selectedNode.getName().equals("directed")) {
		        p.getAnimationProperties().set("directed",
		            g.getProperties().get("directed"));
		      }
	      objValue = getEditorForProperty(p.getAnimationProperties().get(
		          selectedNode.getName()),true);
	      if(selectedNode.getDescription()!=null){
		      objDescription = getEditorForProperty(selectedNode.getDescription(), false);
	      }
		}

		if(objValue!=null){
			int oldPrefferedHeight = objValue.getPreferredSize().height;
			int minHeight = Math.max(oldPrefferedHeight, 25);
			int minWidth = objValue.getPreferredSize().width;
			objValue.setMinimumSize(new Dimension(minWidth, minHeight));
			objValue.setPreferredSize(new Dimension(minWidth, minHeight));
			objValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, minHeight));
			lineValue.updateUI();
			lineValue.add(objValue);
			lineValue.add(Box.createRigidArea(new Dimension(10, 0)));
		}

		if(objDescription!=null){
			int oldPrefferedHeight = objDescription.getPreferredSize().height;
			int minHeight = Math.max(oldPrefferedHeight, 25);
			int minWidth = objDescription.getPreferredSize().width;
			objDescription.setMinimumSize(new Dimension(minWidth, minHeight));
			objDescription.setPreferredSize(new Dimension(minWidth, minHeight));
			objDescription.setMaximumSize(new Dimension(Integer.MAX_VALUE, minHeight));
			textDescription.updateUI();
			textDescription.add(objDescription);
			textDescription.add(Box.createRigidArea(new Dimension(10, 0)));
		}

		lineGeneral.setVisible(false);
		if (designMode) {
			lineIsEditable.setVisible(true);
			lineLabel.setVisible(true);
		}
		lineValue.setVisible(true);
		textDescription.setVisible(false);
		lineSetToDefault.setVisible(!bCallMethod);
	}

	/**
	 * updateRightPanelPrimitive updates the right Panel when a Primitive is
	 * selected in the Tree. The user is allowed to change the value of this
	 * Primitive.
	 */
	private void updateRightPanelPrimitive() {
		if (selectedNode == null || !selectedNode.isPrimitive())
			return;

		if (designMode) {
			lineIsEditable.setVisible(false);
			lineLabel.setVisible(false);
		}

		// update the value Panel
		if (objValue != null) {
			lineValue.remove(objValue);
			lineValue.remove(lineValue.getComponentCount() - 1);
			objValue = null;
		}
		if (objDescription != null) {
			textDescription.remove(objDescription);
			textDescription.remove(textDescription.getComponentCount() - 1);
			objDescription = null;
		}

		objValue = getEditorForProperty(selectedNode.getValue(), true);
	    objDescription = getEditorForProperty(selectedNode.getDescription(), false);
	    

		if(objValue!=null){// TODO
			int oldPrefferedHeight = objValue.getPreferredSize().height;
			int minHeight = Math.max(oldPrefferedHeight, 25);
			int minWidth = objValue.getPreferredSize().width;
			
			objValue.setMinimumSize(new Dimension(minWidth, minHeight));
      if(objValue instanceof JTextArea) {
        JTextArea jta = (JTextArea) objValue;
        int lines = jta.getLineCount();
        objValue.setPreferredSize(new Dimension(minWidth, minHeight/lines*5));
        objValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, minHeight*5));
      } else {
        objValue.setPreferredSize(new Dimension(minWidth, minHeight));
        objValue.setMaximumSize(new Dimension(Integer.MAX_VALUE, minHeight));
      }
			lineValue.updateUI();
			lineValue.add(objValue);
			lineValue.add(Box.createRigidArea(new Dimension(10, 0)));
		}

		if(objDescription!=null){
			int oldPrefferedHeight = objDescription.getPreferredSize().height;
			int minHeight = Math.max(oldPrefferedHeight, 25);
			int minWidth = objDescription.getPreferredSize().width;
			objDescription.setMinimumSize(new Dimension(minWidth, minHeight));
			objDescription.setPreferredSize(new Dimension(minWidth, minHeight));
			objDescription.setMaximumSize(new Dimension(Integer.MAX_VALUE, minHeight));
			textDescription.updateUI();
			textDescription.add(objDescription);
			textDescription.add(Box.createRigidArea(new Dimension(10, 0)));
		}

		lineGeneral.setVisible(false);
		if (designMode) {
			lineIsEditable.setVisible(false);
			lineLabel.setVisible(false);
		}
		lineValue.setVisible(true);
		textDescription.setVisible(true);
		lineSetToDefault.setVisible(false);
	}

	/**
	 * getSelectedFolder returns a String showing the given path.
	 * 
	 * @param p
	 *          A path.
	 * @return A String showing the given path.
	 */
	private String getSelectedFolder(TreePath p) {
		if (p == null || p.getPathCount() < 1)
			return "* ";
		StringBuilder buf = new StringBuilder(127);
		Object[] os = p.getPath();
		int len = p.getPathCount();
		PropertiesTreeNode n;
		buf.append("/ ");
		for (int i = 1; i < len; i++) {
			if (!(os[i] instanceof PropertiesTreeNode))
				continue;
			n = (PropertiesTreeNode) os[i];

			if (n.isFolder()) {
				buf.append(n.getLabel());
				if (i < len - 1)
					buf.append(" / ");
			}
			if (n.isPrimitive()) {
				buf.append(n.getName());
			}
			if (n.isProperty()) {
				buf.append(n.getAnimationProperties().get("name"));
				if (i < len - 1)
					buf.append(" / ");
			}
			if (n.isItem()) {
				if (designMode)
					buf.append(n.getName());
				else
					buf.append(n.getLabel());
			}
		}
		return buf.toString();
	}

	/**
	 * getTree returns the PropertiesTree.
	 * 
	 * @return The PropertiesTree.
	 */
	public PropertiesTree getTree() {
		return tree;
	}

	/**
	 * reInsertTree sets the viewport of the TreePane to the tree to make sure the
	 * tree is displayed.
	 */
	public void reInsertTree() {
		treePane.reInsertTree();
	}

	/**
	 * areThereElements returns true, if AnimationProperties are in the tree.
	 * 
	 * @return true, if AnimationProperties are in the tree.
	 */
	public boolean areThereElements() {
		return (((PropertiesTreeModel) tree.getModel()).getElementsCount() > 0);
	}

	// ****************************************************************************
	// The two methods below should be changed, when new Types are added
	// to the AnimationProperties!!!
	// ****************************************************************************

	/**
	 * getEditorForProperty returns an Editor for the given Class of the Object.
	 * 
	 * @param obj
	 *          The Object that we want an Editor for.
	 * @return An Editor for the given Object Class.
	 */
	@SuppressWarnings("unchecked")
  private JComponent getEditorForProperty(Object obj, boolean value) {
		if (obj == null)
			return new JLabel("There is no registered Editor for a null-Object");
    if (obj instanceof String) {
      if(value) {
        JTextArea textLabelArea = new JTextArea((String) obj);
        textLabelArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, Math.max(
            textLabelArea.getMinimumSize().height*5, 25)));
        return textLabelArea;
      } else {
        return new JTextField((String) obj);
      }
    }
//    if (obj instanceof String)
//      return new JTextField((String) obj);
		if (obj instanceof Integer)
			return new IntegerTextField((Integer) obj);
		if (obj instanceof Boolean)
			return new JCheckBox("(Checked: TRUE, Unchecked: FALSE)", ((Boolean) obj)
					.booleanValue());
		if (obj instanceof Double)
			return new DoubleTextField((Double) obj);
		if (obj instanceof int[])
			return new ArrayInputPanel((int[]) obj);
		if (obj instanceof String[])
			return new StringArrayInputPanel((String[]) obj);
		if (obj instanceof int[][])
			return new MatrixInputPanel((int[][]) obj);
		if (obj instanceof String[][])
			return new MatrixInputPanel((String[][]) obj);
		if (obj instanceof Color)
			return new ColorChooserComboBox((Color) obj);
		if (obj instanceof Font)
			return new FontChooserComboBox((Font) obj);
        
		if (obj instanceof Vector)
		  return new EnumerationChooserComboBox((Vector<String>) obj);
         
    // Madieha
    if (obj instanceof GraphScriptPanel) {
      return PropertiesGUI.mainclass.getPanel(PanelManager.PANEL_GRAPHSCRIPT);
    }

		return new JLabel("There is no registered Editor for "
				+ obj.getClass().getName());
	}

	/**
	 * getValueOfEditorByType returns the current value based on the type of the
	 * old value. We need this type because we don't know the class of editor
	 * before.
	 * 
	 * @param obj
	 *          The old value (used for determining the type).
	 * @return The currently entered value.
	 */
  private Object getValueOfEditorByType(Object obj) {
		if (obj == null)
			return null;
    if (obj instanceof String)
      return ((JTextArea) objValue).getText();
//    if (obj instanceof String)
//      return ((JTextField) objValue).getText();

		if (obj instanceof Integer)
			return ((IntegerTextField) objValue).getValue();

		if (obj instanceof Boolean)
			return Boolean.valueOf(((JCheckBox) objValue).isSelected());

		if (obj instanceof Double)
			return ((DoubleTextField) objValue).getValue();

		if (obj instanceof int[])
			return ((ArrayInputPanel) objValue).tblElements.getIntValues();
		
		if (obj instanceof String[])
			return ((StringArrayInputPanel) objValue).tblElements.getStringValues();

    if (obj instanceof double[][])
      return ((MatrixInputPanel) objValue).tblElements.getDoubleMatrixValues();

    if (obj instanceof int[][])
			return ((MatrixInputPanel) objValue).tblElements.getIntMatrixValues();

		if (obj instanceof String[][])
			return ((MatrixInputPanel) objValue).tblElements.getStringMatrixValues();

		if (obj instanceof Color)
			return ((ColorChooserComboBox) objValue).getColorSelected();

		if (obj instanceof Font)
			return ((FontChooserComboBox) objValue).getFontSelected();
        
		if (obj instanceof Vector)
			return ((EnumerationChooserComboBox) objValue).getElementSelectedAsString();
		
		  if(obj instanceof GraphScriptPanel){
	        	return ((GraphScriptPanel)PropertiesGUI.mainclass.getPanel(PanelManager.PANEL_GRAPHSCRIPT));
	        	}

		return null;
	}

	/**
	 * updateCurrentPropertyValues updates the values (label, isEditable and the
	 * selected value) of the currently selected PropertyType.
	 */
	public void updateCurrentPropertyValues() {
		if (selectedNode == null || selectedNode.isFolder()
				|| selectedNode.isProperty())
			return;

		if (selectedNode.isPrimitive()) {
			Object oldValue = selectedNode.getValue();
			if (oldValue != null)
				selectedNode.setValue(getValueOfEditorByType(oldValue));
			if (objDescription != null) {
        selectedNode.setDescription(((JTextField) objDescription).getText());
			}
      return;
		}

		if (selectedNode.isItem()) {
			// getParent
			PropertiesTreeNode p = (PropertiesTreeNode) selectedNode.getParent();
			if (p == null || !p.isProperty())
				return;

			if (designMode) {
				p.getAnimationProperties().setIsEditable(selectedNode.getName(),
						checkIsEditable.isSelected());
				try {
					p.getAnimationProperties().setLabel(selectedNode.getName(),
							textLabel.getText());
				} catch (IllegalArgumentException e) {
					System.err.println(e.getLocalizedMessage());
				}
			}

			if (p.getAnimationProperties() instanceof CallMethodProperties)
				return;

			Object oldValue = p.getAnimationProperties().get(
					selectedNode.getName());
			if (oldValue == null)
				return;

			try {
				p.getAnimationProperties().set(selectedNode.getName(),
						getValueOfEditorByType(oldValue));

			} catch (IllegalArgumentException e) {
				System.err.println(e.getLocalizedMessage());
			}
		}
	}

	/**
	 * setBuildMode displays the Tree in BuildMode. Nodes can be dragged around
	 * and the root is visible. Items are not shown.
	 */
	public void setBuildMode() {
		tree.setBuildMode();
		designMode = true;
	}

	/**
	 * setWorkingMode displays the Tree in WorkingMode. Nodes are not editable but
	 * items and also hidden items are shown. Root is not visible.
	 */
	public void setWorkingMode() {
		tree.setWorkingMode();
		designMode = true;
	}

	/**
	 * setFinalMode displays the Tree in FinalMode. This is the way the Tree is
	 * presented to the end-user in the GeneratorGUI. Hidden items are not shown
	 * and the Tree is not editable.
	 */
	public void setFinalMode() {
		tree.setFinalMode();
		designMode = false;
	}

	// ****************************************************************************
	// TreeSelectionListener
	// ****************************************************************************

    protected void clearSelection() {
      selectedNode = null;
      tree.clearSelection();
      selectedDisplay = null;
      labelHead.setText("");
    }
    
	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		if (e == null)
			return;

		if (selectedNode != null
				&& (selectedNode.isItem() || selectedNode.isPrimitive())) {
			updateCurrentPropertyValues();
		}

		PropertiesTreeNode n = (PropertiesTreeNode) tree
				.getLastSelectedPathComponent();
		if (n == null || n == tree.getModel().getRoot())
			return;
		selectedNode = n;
		selectedDisplay = getSelectedFolder(tree.getSelectionPath());
		labelHead.setText("   " + selectedDisplay);

		if (n.isFolder()) {
			updateRightPanelGeneral();
			return;
		}

		if (n.isProperty()) {
			if (n.getAnimationProperties() instanceof CallMethodProperties) {
				updateRightPanelItem();
				return;
			}

			updateRightPanelGeneral();
			return;
		}

		if (n.isPrimitive()) {
			updateRightPanelPrimitive();
			return;
		}

		if (n.isItem()) {
			updateRightPanelItem();
			return;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e == null)
			return;

		if (e.getActionCommand().equalsIgnoreCase("setToDefault")) {
			if (selectedNode == null || !selectedNode.isItem())
				return;

			// get Parent
			PropertiesTreeNode p = (PropertiesTreeNode) selectedNode.getParent();
			if (!p.isProperty())
				return;

			if (!designMode
					&& p.getAnimationProperties() instanceof CallMethodProperties)
				return;

			p.getAnimationProperties().set(
					selectedNode.getName(),
					p.getAnimationProperties().getDefault(selectedNode.getName())
							.get());
			updateRightPanelItem();
		}

		if (e.getActionCommand().length() > 5) {
			if (e.getActionCommand().substring(0, 4).equalsIgnoreCase("call")) {
				String method = e.getActionCommand().substring(5);
				if (listener != null) {
					listener.callMethod(method);
				}
				return;
			}
		}
	}

	/**
	 * setListener registers a Listener that is called, when the user chooses to
	 * call a specific Generator method (for example to show a Frame)
	 * 
	 * @param newListener
	 *          the new Listener that should be registered.
	 */
	public void setListener(PropertiesPanelListener newListener) {
		listener = newListener;
	}
}
