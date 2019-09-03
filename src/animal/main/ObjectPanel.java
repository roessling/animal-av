package animal.main;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import animal.animator.GraphicObjectSpecificAnimation;
import animal.editor.Editor;
import animal.editor.animators.AnimatorEditor;
import animal.editor.graphics.meta.GraphicEditor;
import animal.gui.DrawWindow;
import animal.misc.EditableObject;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

/**
 * ObjectPanel is a Panel that contains buttons for selecting the Editors,
 * either for GraphicObjects or for Animators. When a button is pressed, an
 * appropriate method is called * for GraphicObjects, a new primary Editor is
 * selected in DrawWindow * for Animators, a new AnimatorEditor is created and
 * shown
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.0 18.07.1998
 */
public class ObjectPanel extends JPanel implements ActionListener {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 9175523615770554449L;

	/**
	 * if true, this ObjectPanel contains GraphicEditors, otherwise it contains
	 * AnimatorEditors.
	 */
	private boolean graphicEditors;
	private JComboBox<String> comboBox;

	private Hashtable<String, JButton> editorButtonTable;

	/** a reference to the Animal window */
	private Animal animal;

	/**
	 * the DrawWindow the ObjectPanel is in. <b>null </b>, if this ObjectPanel
	 * is in AnimationOverview. one of drawWindow/animationOverview is set,
	 * according to <i>graphicEditors </i>
	 */
	private DrawWindow drawWindow;

	/**
	 * all buttons are in this buttonGroup. It is used to iterate these buttons.
	 */
	private ButtonGroup buttonGroup;

	private JToolBar toolBar;

	/**
	 * constructs an ObjectPanel.
	 * 
	 * @param animalInstance the instance of Animal to be used
	 * @param parent the AnimalFrame in which contents are placed
	 * @param props the XProperties describing the elements
	 * @param usesGraphicEditors if true, the panel contains the GraphicEditors,
	 * else the panel contains AnimatorEditors.
	 */
	public ObjectPanel(Animal animalInstance, AnimalFrame parent, XProperties props,
			boolean usesGraphicEditors) {
		super();
		animal = animalInstance;
		if (usesGraphicEditors)
			drawWindow = (DrawWindow) parent;
		graphicEditors = usesGraphicEditors;
		installEditors(animal);
		setProperties(props);
	}

	public JToolBar getToolBar() {
		return toolBar;
	}

	public void installEditors( Animal animalInstance) {
		// clear up the tool bar and button group
		if (comboBox == null) {
			comboBox = new JComboBox<String>();
			comboBox.addItem("Specific Operations");
			comboBox.addActionListener(this);
		}
		
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setFloatable(true);
			toolBar.getAccessibleContext().setAccessibleName(
					(graphicEditors) ? "Graphic primitives" : "Animators");
		} else
			toolBar.removeAll();

		// install button group and editor table
		if (buttonGroup == null)
			buttonGroup = new ButtonGroup();
		if (editorButtonTable == null)
			editorButtonTable = new Hashtable<String, JButton>(53);

		Editor editor = null;
		JButton button = null;
		Hashtable<String, Editor> editors = animal.getEditors();
		int nrEditors = editors.size();
		Vector<String> dsSpecifics = new Vector<String>(57);
		
		// iterate over all editors; retrieve numbered editor
		for (int i = 0; i < nrEditors; i++) {
			Enumeration<Editor> elems = editors.elements();
			while (elems.hasMoreElements()) {
				editor = elems.nextElement();
				if (editor != null) {
					String editorClassName = editor.getClass().getName();
					if (editor.getNum() == i && isValidEditor(editor)) {
						
						if (editor instanceof GraphicObjectSpecificAnimation) {
							String[] typeList = ((GraphicObjectSpecificAnimation)editor).getSupportedTypes();
							for (String element : typeList) {
								dsSpecifics.addElement(element +": " +editor.getName());
							}
						}
						else {
						if (editorButtonTable.containsKey(editorClassName)) {
							button = editorButtonTable.get(editorClassName);
							toolBar.add(button);
						} else {
							addButton(toolBar, editor.getName() + ".gif",
									editor.getName(), editorClassName, editor.getName());
						}
						//            toolBar.add(button);
					}}
//					}
				}
			}
		}
		if (dsSpecifics.size() > 0) {
			String[] dsElements = new String[dsSpecifics.size()]; 
			dsSpecifics.copyInto(dsElements);
			Arrays.sort(dsElements);
			String previousType = "-";
			for (String key: dsElements) {
				String localKey = key.substring(0, key.indexOf(':'));
				if (!previousType.equals(localKey)) {
					comboBox.addItem("=== " + localKey + " ===");
					previousType = localKey;
				}
				comboBox.addItem(key.substring(key.indexOf(':') + 2));
			}				
		}
		toolBar.add(comboBox);
	}

	public void addButton(JToolBar aToolBar, String iconName,
			String toolTipText, String className, String name) {
		JButton button = new JButton(animal.getImageIcon(iconName));
		button.setToolTipText(toolTipText);
		// ActionCommand is set to have a link between the
		// buttons and the Editors they display
		button.setActionCommand(name);
		button.addActionListener(this);
		buttonGroup.add(button);
		editorButtonTable.put(className, button);
		aToolBar.add(button);
	}

	/**
	 * sets the ObjectPanel's properties. As ObjectPanel itself has no
	 * properties, this only sets all Editor's properties.
	 */
	void setProperties(XProperties props) {
		Enumeration<Editor> e = animal.getEditors().elements(); 
		while (e.hasMoreElements()) {
			Editor ed = e.nextElement();
			// don't set the properties twice(once in the AW's ObjectPanel
			// and once in the AO's
			if (isValidEditor(ed)) {
				// create a dummy object for each Editor.
				// Thus, currentObject != null and a lot of null pointer checks
				// in the Editors are obsolete.
				ed.createObject();
				ed.setProperties(props);
			}
		}
		// do not set an editor but let the user choose
	}

	/**
	 * returns whether the Editor fits into this ObjectPanel, i.e if it is of
	 * correct type (AnimatorEditor for the ObjectPanel in AnimationOverview,
	 * GraphicEditor for the ObjectPanel in DrawWindow)
	 */
	boolean isValidEditor(Editor e) {
		return (graphicEditors && e instanceof GraphicEditor || !graphicEditors
				&& e instanceof AnimatorEditor);
	}

	/**
	 * returns the properties of all GraphicEditors. only GraphicEditors have
	 * primary editors. Animator- and LinkEditors save their properties whenever
	 * applying changes. So they needn't (and mustn't, because the nonexisting
	 * primary editors would overwrite the properties) be called for saving
	 * their properties.
	 */
	public void getProperties(XProperties props) {
		Enumeration<Editor> e = animal.getEditors().elements();
		while (e.hasMoreElements()) {
			Editor ed = e.nextElement();
			if (ed instanceof GraphicEditor)
				ed.getProperties(props);
		}
	}

	/**
	 * selects a new Editor by its name. If the Editor is a GraphicEditor, it's
	 * made the primary Editor used in DrawWindow, if it's an AnimatorEditor, a
	 * new Animator is created and its Editor displayed.
	 * 
	 * @param name
	 *            null: just close current Editor; other: new Editor to be used
	 */
	public void setCurrentEditor(String name) {
		boolean found = false;
		boolean thatsit; // the currently checked editor is the right one.
		if (name == null)
			return;
		Editor editor = animal.getEditor(name, false);
		// check all buttons whether their ActionCommand fits. If so,
		// select this button and leave the loop
		if (editor instanceof GraphicObjectSpecificAnimation)
			found = true; 
		else {
			Enumeration<AbstractButton> e = buttonGroup.getElements(); 
			while (e.hasMoreElements()&& !found) {
				JButton rb = (JButton) e.nextElement();
				thatsit = rb.getActionCommand().equals(name);
				rb.setSelected(thatsit);
				found = found | thatsit;
			}
		}

		if (!found || editor == null) {
			MessageDisplay.errorMsg("illegalEditor", name,
					MessageDisplay.CONFIG_ERROR);
		} // name == null
		else {
			if (graphicEditors) {
				editor.createObject();
				drawWindow.getDrawCanvas().setGraphicEditor(
						(GraphicEditor) editor);
			} else { // if the new Object is an Animator, always insert a
				//         secondary editor. Thus it is possible to create several
				//         new Animators at once and some problems with "apply" are
				//         avoided
				EditableObject a = editor.createObject();
				Editor se = a.getSecondaryEditor();
				se.setProperties(AnimalConfiguration.getDefaultConfiguration().getProperties());
				se.setVisible(true);
			}
		}
	} // setCurrentEditor

	/**
	 * reacts to button press by selecting an Editor.
	 */
	public void actionPerformed(java.awt.event.ActionEvent event) {
		Object object = event.getSource();
		if (object instanceof JButton) {
			setCurrentEditor(((JButton) object).getActionCommand());
		}
		else if (object instanceof JComboBox) {
			// determine selected element
			JComboBox<?> box = (JComboBox<?>) object;
			if (box.getSelectedIndex() > 0)
				setCurrentEditor((String)box.getSelectedItem());
			box.setSelectedIndex(0);
		}
	} // actionPerformed

	protected ButtonGroup getButtonGroup() {
		return buttonGroup;
	}
}
