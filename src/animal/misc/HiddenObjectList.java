package animal.misc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import translator.AnimalTranslator;
import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.AnimalFrame;
import animal.main.Animation;

/**
 * the window that displays all Animators in sequential order, allows to insert
 * and delete Animators, Steps etc.
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 13.07.1998
 */
public class HiddenObjectList extends AnimalFrame implements
		ListSelectionListener, ActionListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 6000473356719369903L;

	/** current Animation */
	private Animation animation;

	/** the List that contains all AnimatorInfos */
	// private JList objectList;
	private JTable objectTable;

	/** for determining need to update when Animation has changed */
	private int first = 0, last = 0;

	/** button to append a new step after the current step */
	private JButton cancelButton;

	private JScrollPane scrollPane;

  private Font              defaultFont      = new Font("Dialog", 0, 14);

	/**
	 * constructs the HiddenObjectList. Initialization is done with
	 * <code>init</code>.
	 * 
	 * @see #init
	 */
	public HiddenObjectList(Animal animalInstance, Animation anim) {
		super(animalInstance, AnimalConfiguration.getDefaultConfiguration()
				.getProperties());
		animation = anim;
		init();
	}

	/**
	 * initializes the HiddenObjectList by adding the button panel, the
	 * ObjectPanel and the List.
	 */
	public void init() {
		super.init();
		workContainer().setLayout(new BorderLayout(0, 0));
		buildTable();

		scrollPane = new JScrollPane(objectTable);

		JPanel buttonPanel = new JPanel();
		cancelButton = AnimalTranslator.getGUIBuilder().generateActionButton(
				"GenericEditor.cancel",
				AnimalTranslator.getGUIBuilder().generateAction("GenericEditor.cancel",
						null, this, null, true));
		buttonPanel.add(cancelButton);

		workContainer().add(BorderLayout.CENTER, scrollPane);
		workContainer().add(BorderLayout.SOUTH, buttonPanel);
		setTitle("Object List");
    setSize(680, 420);
		setVisible(true);
	}

	private static final String[] rowLabels = new String[] {
			AnimalTranslator.translateMessage("holID"),
			AnimalTranslator.translateMessage("holType"),
			AnimalTranslator.translateMessage("holVisible"),
			AnimalTranslator.translateMessage("holInfo") };

	private ObjectTableModel otm = null;

	public void setStep(int step) {
		if (otm == null)
			initializeTableModel();
		otm.setStep(step);
	}

	public void initializeTableModel() {
		otm = new ObjectTableModel();
		if (animation == null)
			animation = Animal.get().getAnimation();
		otm.setAnimation(animation);
		if (animation == null)
			otm.setObjects(new Vector<PTGraphicObject>());
		else
			otm.setObjects(animation.getGraphicObjects());
		objectTable = new JTable(otm);
	}

	public void buildTable() {
		setStep(AnimalMainWindow.getWindowCoordinator().getDrawWindow(false)
				.getStep());
		TableColumn column = null;
		for (int i = 0; i < rowLabels.length; i++) {
			column = objectTable.getColumnModel().getColumn(i);
			if (i == 0 || i == 2)
				column.setPreferredWidth(20);
			else if (i == 1)
				column.setPreferredWidth(100);
			else
				column.setPreferredWidth(300);
		}
	}

	/**
	 * reacts to double clicks on the list's items by showing the
	 * Animator's/Link's Editor. Reacts to buttons pressed by performing the
	 * appropriate commands.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton)
			setVisible(false);
	}

	/**
	 * initializes the list by calling all Objects' <code>.toString</code>-method.
	 * This is done when an Animator or Link has changed or is inserted or
	 * deleted.
	 * 
	 * @param getNewList
	 *          if true, the Animation is asked for a new list. This is needed if
	 *          an Animator or Link is inserted or deleted or a Link is changed(as
	 *          Link time needs a line of it's own).
	 */
	public void initList(boolean getNewList) {
		// do nothing
	}

	public int[] getObjectIDs() {
		/*
		 * Object[] selectedObjects = objectList.getSelectedValues(); if
		 * (selectedObjects == null || selectedObjects.length == 0) return new int[]
		 * { -1 }; int pos = 0, nr = selectedObjects.length; int[] selectedElements
		 * = new int[nr]; for (pos = 0; pos < nr; pos++) { if (selectedObjects[pos]
		 * instanceof String) { StringTokenizer stok = new
		 * StringTokenizer((String)selectedObjects[pos]); selectedElements[pos] =
		 * Integer.parseInt(stok.nextToken()); } else System.err.println(" no String
		 * at index " +pos); } return selectedElements;
		 */
		return null;
		// convert to String, use STokenizer, ....!
	}

	public void cancelOperation() {
		setVisible(false);
	}

	public void hideSelected() {
		int[] selected = getObjectIDs();
		System.err.print("hide ");
		for (int i = 0; i < selected.length; i++)
			System.err.print(selected[i] + " ");
	}

	// public void showSelected() {
	// int[] selected = getObjectIDs();
	// System.err.print("show ");
	// for (int i = 0; i < selected.length; i++)
	// System.err.print(selected[i] + " ");
	// }

	/**
	 * sets the AnimationOverviews step by selecting an appropriate line, i.e. a
	 * line that contains Link information or an Animator for this step. If an
	 * appropriate line was already selected, don't change anything.
	 */
	void setStep(int step, boolean setOtherWindows) {
		setStep(step);
	}

	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			first = e.getFirstIndex();
			last = e.getLastIndex();
			MessageDisplay.message("holChanged", new Object[] { e.toString(),
					Integer.valueOf(first), Integer.valueOf(last) });
		}
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {

    Dimension dim = this.getSize();

    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
      if (dim.getWidth() < 1000) {
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      }

    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
      if (dim.getWidth() > 200) {
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      }

    }

    if (cancelButton != null) {
      cancelButton.setFont(defaultFont);
    }

    if (objectTable != null) {
      UIManager.put("Table.font", defaultFont);
      objectTable.getTableHeader().setFont(defaultFont);

    }
    this.setSize(dim);

  }

}
