package gfgaa.gui.others;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.GraphScriptPanel;
import gfgaa.gui.components.SComponent;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.exceptions.GraphTypeException;
import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.graphs.CreateGraphPanel;
import gfgaa.gui.graphs.GraphEntry;
import gfgaa.gui.graphs.KantenPanelInterface;
import gfgaa.gui.graphs.MatrixPanelInterface;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * Database class<br>
 * This class represents a database that saves all graphtyps. The Graph Database
 * is used for the GUI's communication with the diffrent graphtyps.
 * 
 * @author S. Kulessa
 * @version 0.95
 */
public final class GraphDataBase {

  /**
   * Graphtyp Chooser Dialog<br>
   * Opens a Dialog to choose the graphtyp.
   * 
   * @author S. Kulessa
   * @version 0.95
   */
  private class GraphTypDialog extends JDialog implements LanguageInterface {

    /**
       * 
       */
    private static final long serialVersionUID = -4992726681533122100L;

    /**
     * Panel class<br>
     * Panel of the Graphtyp Dialog.
     * 
     * @author S. Kulessa
     * @version 0.95
     */
    private final class GraphTypPanel extends SPanel {

      /**
           * 
           */
      private static final long serialVersionUID = -2911161207553048185L;

      /** JButton to create an empty graph. */
      private JButton           create;

      /** JButton to close this window. */
      private JButton           exit;

      /** JComboBox to select the graphtyp. */
      JComboBox<GraphEntry>     typChooser;

      /** JScrollPane to display the discription of the graphtyps. */
      JScrollPane               typDescr;

      /**
       * (constructor)<br>
       * Creates the panel of the graphtyp dialog.
       */
      public GraphTypPanel() {
        setLayout(null);

        createTypChooserComponents();

        add(createResetButton());
        add(createReturnButton());

        this.changeLanguageSettings(mainclass.getLanguageSettings());
      }

      /**
       * (panel display method)<br>
       * Paints the panel and his components.
       * 
       * @param g
       *          Graphical component of this panel
       */
      public void paint(final Graphics g) {
        Dimension size = this.getSize();

        int[] pos = new int[5];
        pos[0] = (size.width - 210) / 2;
        pos[1] = (size.width - 275) / 2;
        pos[2] = pos[1] + 175;

        pos[3] = (size.height - 275) / 2;
        pos[4] = pos[3] + 250;

        typDescr.setLocation((size.width - 300) / 2, pos[3] + 40);
        typChooser.setLocation((size.width - 250) / 2, pos[3]);

        create.setLocation(pos[0], pos[4]);
        exit.setLocation(pos[0] + 110, pos[4]);

        super.paint(g);
      }

      /**
       * (panel construction method)<br>
       * Creates the components to select a graphtyp and to shows informations
       * about them.
       */
      private void createTypChooserComponents() {
        final JEditorPane typContent = new JEditorPane();
        typContent.setEditable(false);

        typDescr = new JScrollPane(typContent);
        typDescr.setSize(300, 190);

        typDescr.setBorder(BorderFactory.createEtchedBorder());
        typDescr
            .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        add(typDescr);

        typChooser = new JComboBox<GraphEntry>();
        typChooser.setSize(250, 25);

        int size = data.size();
        for (int i = 0; i < size; i++) {
          typChooser.addItem(data.get(i));
        }

        typChooser.addActionListener(new ActionListener() {
          public void actionPerformed(final ActionEvent e) {

            typContent.setText(((GraphEntry) typChooser.getSelectedItem())
                .getDescription());

            javax.swing.SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                typDescr.getVerticalScrollBar().setValue(0);
              }
            });
          }
        });

        typChooser.setSelectedItem(selected);
        add(typChooser);
      }

      /**
       * (panel construction method)<br>
       * Creates the reset button.
       * 
       * @return Reset button
       */
      private JButton createResetButton() {
        create = new JButton();
        create.setBounds(110, 260, 100, 25);

        add(new SComponent(create, new String[] { "Erzeugen", "Create" },
            new String[] {
                "Erzeugt einen leeren" + " Graphen des ausgewählten"
                    + " Graphentypus.",
                "Creates an empty graph of" + " the selected typ." }));

        create.addActionListener(new ActionListener() {

          /**
           * Lädt die gesicherten Einstellungen in das Panel und zeichnet den
           * Graphen wieder mit den ursprünglichen Einstellungen.
           * 
           * @param e
           *          ActionEvent
           */
          public void actionPerformed(final ActionEvent e) {

            selected = (GraphEntry) typChooser.getSelectedItem();
            selected.createGraph();

            mainclass.getPanel(PanelManager.PANEL_EDITGRAPH)
                .refreshPanelComponents();
            mainclass.repaint();
          }
        });

        return create;
      }

      /**
       * (panel construction method)<br>
       * Creates the return button.
       * 
       * @return Return button
       */
      private JButton createReturnButton() {
        exit = new JButton();
        exit.setBounds(215, 260, 100, 25);

        add(new SComponent(exit, new String[] { "Schließen", "Close" },
            new String[] { "Schließt das Fenster.", "Closes the window." }));

        exit.addActionListener(new ActionListener() {

          /**
           * Schließt den Dialog.
           * 
           * @param e
           *          ActionEvent
           */
          public void actionPerformed(final ActionEvent e) {
            close();
          }
        });

        return exit;
      }

      /**
       * @deprecated
       */
      public void refreshPanelComponents() {
      }
    }

    /**
     * (Constructor)<br>
     * Creates a new GraphTypDialog.
     */
    public GraphTypDialog() {
      super(mainclass.getGUI(), true);

      if (mainclass.getLanguageSettings() == LANGUAGE_GERMAN) {
        setTitle("Graphtypus Einstellungen");
      } else {
        setTitle("Graphtyp Settings");
      }

      this.setSize(330, 320);
      this.getContentPane().setLayout(null);
      this.setResizable(false);

      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation((d.width - getSize().width) / 2,
          (d.height - getSize().height) / 2);

      this.setContentPane(new GraphTypPanel());
    }

    /**
     * (internal display method)<br>
     * Sets the dialogs visible state to false.
     */
    void close() {
      this.setVisible(false);
    }
  }

  /** Reference to the projects mainclass. */
  GraphAlgController                   mainclass;

  /** Reference to the currently selected GraphEntry. */
  GraphEntry                           selected;

  /** List containing all known GraphAlgorithm. */
  ArrayList<GraphEntry>                data;

  private HashMap<Integer, GraphEntry> link;

  /**
   * (constructor)<br>
   * Creates the graph database.
   * 
   * @param mainclass
   *          Reference to the projects mainclass
   */
  public GraphDataBase(final GraphAlgController mainclass) {
    this.mainclass = mainclass;

    data = new ArrayList<GraphEntry>();
    link = new HashMap<Integer, GraphEntry>();
  }

  public void addGraphType(final GraphEntry entry) throws GraphTypeException {

    ArrayList<String> eMessage = new ArrayList<String>();

    if (!(entry.createCreateGraphPanel() instanceof CreateGraphPanel)) {
      eMessage.add("Error #01 -> The CreateGraphPanel must be an"
          + " instance of the CreateGraphPanel.");
    }

    if (!(entry.createKantenPanel() instanceof KantenPanelInterface)) {
      eMessage.add("Error #02 -> The CreateGraphPanel must implement"
          + " the KantenPanelInterface.");
    }

    if (!(entry.createMatrixPanel() instanceof MatrixPanelInterface)) {
      eMessage.add("Error #03 -> The CreateGraphPanel must implement"
          + " the MatrixPanelInterface.");
    }

    // Prüfung ob Fehlermeldungen gespeichert wurden
    if (!eMessage.isEmpty()) {
      throw new GraphTypeException(entry, eMessage);
    }

    addGraph(entry);
  }

  public void addGraph(final GraphEntry entry) {

    if (link.containsKey(entry.getTyp())) {
      throw new IllegalArgumentException("GraphTyp is already contained.");
    }
    link.put(entry.getTyp(), entry);

    String refTitle = entry.getTitle();

    int size = data.size();

    for (int i = 0; i < size; i++) {
      String title = get(i).getTitle();
      if (refTitle.compareTo(title) < 0) {
        data.add(i, entry);
        return;
      }
    }

    data.add(entry);
  }

  /**
   * (language method)<br>
   * Changes the LanguageSetting of the database and sorts the database in
   * lexicographical order.
   */
  public void changeLanguageSettings() {

    GraphEntry entry;
    ArrayList<GraphEntry> newData = new ArrayList<GraphEntry>();

    boolean added;
    String title, refTitle;
    int i, j, size = data.size();

    for (i = 0; i < size; i++) {
      entry = get(i);

      refTitle = entry.getTitle();
      added = false;

      for (j = 0; j < i && !added; j++) {
        title = get(j).getTitle();
        if (refTitle.compareTo(title) < 0) {
          newData.add(j, entry);
          added = true;
        }
      }

      if (!added) {
        newData.add(entry);
      }
    }

    this.data = newData;
  }

  /**
   * (data method)<br>
   * Returns the AbstractGraph Reference at the given position in the database.
   * 
   * @param pos
   *          Index
   * @return AbstractGraph Reference
   */
  public GraphEntry get(final int pos) {
    return (GraphEntry) data.get(pos);
  }

  /**
   * (info method)<br>
   * Returns the position of the given Graph in the database.
   * 
   * @param entry
   *          Graph
   * @return Index in the database<br>
   *         -1 if the Graph is unknown
   */
  public int getIndex(final GraphEntry entry) {
    int size = size();
    for (int i = 0; i < size; i++) {
      if (get(i) == entry) {
        return i;
      }
    }
    return -1;
  }

  /**
   * (info method)<br>
   * Returns the number of containend GraphAlgorithms.
   * 
   * @return Size of the GraphAlgorithm database
   */
  public int size() {
    return data.size();
  }

  /**
   * (internal info method)<br>
   * Transfers the currently used Graph into GraphScriptNotation and sends this
   * data to the graphscript panel.
   */
  public void transfer() {
    GraphScriptPanel s = (GraphScriptPanel) mainclass
        .getPanel(PanelManager.PANEL_GRAPHSCRIPT);

    s.showGraphData(selected.transfer().toString());
  }

  /**
   * (internal data method)<br>
   * The database saves the given GraphEntry as the currently selected
   * GraphEntry.
   * 
   * @param entry
   *          GraphEntry object
   */
  public void selectEntry(final GraphEntry entry) {
    this.selected = entry;
  }

  public int getIndexOf(final String title) {

    GraphEntry entry;
    int size = data.size();

    for (int i = 0; i < size; i++) {
      entry = (GraphEntry) data.get(i);
      if (entry.getTitle().equals(title)) {
        return i;
      }
    }

    return 0;
  }

  /**
   * (internal data method)<br>
   * Adds the given graph to the selected GraphEntry object. Updates the
   * EditGraphPanel afterwards.
   * 
   * @param graph
   *          Graph
   */
  public void setGraph(final AbstractGraph graph) {
    GraphEntry entry = (GraphEntry) link.get(graph.getGraphTyp());
    // System.out.println(entry);
    this.selected = entry;
    entry.setGraph(graph);

    this.mainclass.getPanel(PanelManager.PANEL_EDITGRAPH)
        .refreshPanelComponents();
  }

  /**
   * (internal info method)<br>
   * Returns the currently used graph.
   * 
   * @return The currently used graph
   */
  public AbstractGraph getSelectedGraph() {
    if (this.selected != null) {
      return this.selected.getGraph();
    }
    return null;
  }

  /**
   * (internal info method)<br>
   * Returns the ID of the currently used GraphEntry.
   * 
   * @return ID of the currently used GraphEntry
   */
  public Integer getGraphTyp() {
    return this.selected.getTyp();
  }

  /**
   * (internal info method)<br>
   * Returns the currently used GraphEntry.
   * 
   * @return The currently used GraphEntry
   */
  public GraphEntry getSelectedEntry() {
    return this.selected;
  }

  /**
   * (internal info method)<br>
   * Returns the referenced GraphEntry object.
   * 
   * 
   * @param index
   *          Index of the Graph Entry in the database
   * 
   * @return The GraphEntry object at the specified index
   */
  public GraphEntry getEntry(final int index) {
    return (GraphEntry) data.get(index);
  }

  /**
   * (panel display method)<br>
   * Opens the GraphTypDialog.
   */
  public void showGraphTypDialog() {
    new GraphTypDialog().setVisible(true);
  }

  /**
   * (internal data method)<br>
   * Initializes the graph database - have to be called after adding the
   * GraphEntry for BasicGraphs.
   */
  public void initialize() {
    this.selected = (GraphEntry) link.get(AbstractGraph.GRAPHTYP_BASIC);
  }
}
