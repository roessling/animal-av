package animal.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import translator.AnimalTranslator;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.AnimalFrame;
import animal.main.Animation;

/**
 * the window that displays all Animators in sequential order, allows to insert
 * and delete Animators, Steps etc.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 13.07.1998
 */
public class VariableView extends AnimalFrame implements TableModelListener {
	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = 6000473356719369903L;

	/** current Animation */
	private Animation anim;

	/** the List that contains all AnimatorInfos */
	// private JButton cancelButton;
	private JScrollPane scrollPane;
	private JTable table;
	private VariableTableModel tableModel = null;
  private Font               defaultFont      = new Font("Dialog", 0, 14);
  private JButton            cancelButton;

	public VariableView(Animal animalInstance, Animation anim) {
		super(animalInstance, AnimalConfiguration.getDefaultConfiguration()
				.getProperties());
		this.anim = anim;
		init();
	}

	/**
	 * initializes the HiddenObjectList by adding the button panel, the
	 * ObjectPanel and the List.
	 */
	public void init() {
		super.init();

		if (anim == null)
			anim = Animal.get().getAnimation();
		tableModel = new VariableTableModel();
		table = new JTable(tableModel);
		// TODO Java 6?
		table.setFillsViewportHeight(true);

		table.getModel().addTableModelListener(this);

		scrollPane = new JScrollPane(table);

		JPanel buttonPanel = new JPanel();
    cancelButton = AnimalTranslator.getGUIBuilder()
				.generateActionButton(
						"cancel",
						AnimalTranslator.getGUIBuilder().generateAction(
								"cancel", null, this, null, true));
		buttonPanel.add(cancelButton);

		workContainer().removeAll();
		workContainer().setLayout(new BorderLayout(0, 0));
    workContainer().add(BorderLayout.CENTER, scrollPane);
		workContainer().add(BorderLayout.SOUTH, buttonPanel);

		setTitle("Variable List");
		setSize(400, 400);
		setLocation(0, 275);
		setVisible(false);
	}

	public void setStep(int step) {
		tableModel.setStep(step);
	}

	public void tableChanged(TableModelEvent e) {
		scrollPane.repaint();
	}

	public void cancelOperation() {
		this.setVisible(false);
	}

	// public void repaint()
	// {
	// tableModel.setStep(0);//updateDataForStep();
	// System.out.println("repainting");
	// }

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
        dim.setSize(dim.getWidth() + 40, dim.getHeight() + 40);
      }

    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
      if (dim.getWidth() > 200) {
        dim.setSize(dim.getWidth() - 40, dim.getHeight() - 40);
      }



    }

    if (cancelButton != null) {
      cancelButton.setFont(defaultFont);
    }

    if (table != null) {
      UIManager.put("Table.font", defaultFont);
      table.getTableHeader().setFont(defaultFont);

    }
    this.setSize(dim);

  }

}
