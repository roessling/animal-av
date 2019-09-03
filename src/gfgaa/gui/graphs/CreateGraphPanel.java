package gfgaa.gui.graphs;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.GraphScriptPanel;
import gfgaa.gui.GraphScriptWriter;
import gfgaa.gui.MessageHandler;
import gfgaa.gui.components.FileChooserGSF;
import gfgaa.gui.components.IntegerTextFieldEx;
import gfgaa.gui.components.SComponent;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.graphs.basic.Uppercorner;
import gfgaa.gui.others.PanelManager;
import gfgaa.gui.parser.GraphScriptParser;
import gfgaa.gui.parser.ParserUnit;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Panel class<br>
 * This panel is used to change the graph attributes, the colors of the graphs
 * representation and the typ of the graph. It also contains buttons to save,
 * load, transfer and to create an empty graph.
 * 
 * @author S. Kulessa
 * @version 0.97c
 */
public abstract class CreateGraphPanel extends SPanel {

  /**
   * 
   */
  private static final long serialVersionUID = -4134589278462198483L;

  /** Reference to the projects mainclass. */
  public GraphAlgController mainclass;

  /**
   * Panel component - Used to select whether the graph should be directed or
   * not.
   */
  public JRadioButton    gerichtet;

  /**
   * Panel component - Used to select whether the graph should be directed or
   * not.
   */
  protected JRadioButton    ungerichtet;

  /**
   * Panel component - Used to select whether the graph should be weighted or
   * not.
   */
  public JRadioButton    gewichtet;

  /**
   * Panel component - Used to select whether the graph should be weighted or
   * not.
   */
  protected JRadioButton    ungewichtet;

  /** Panel component - Used to describe the graphTypChooser button. */
  private JLabel            typLabel;

  /** Panel component - Used to open a dialog to change the graphtyp. */
  // private JButton graphTypChooser;
  /** Panel component - Used to select the diameter of the nodes. */
  private JLabel            labRadius;

  /**
   * Panel component - Used to select the radius of the nodes in the graphical
   * representation of the graph.
   */
  JComboBox<String>         radBox;

  /** Panel component - Used to create an empty graph. */
  protected JButton         clearGraph;

  /** Panel component - Used to transfer a graph into graphscript. */
  private JButton           transferGraph;

  /** Panel component - Used to save a graphscript file. */
  protected JButton         saveFile;

  /** Panel component - Used to load a graphscript file. */
  private JButton           loadFileButton;
  // bouchra
  /** Panel component - Label. */
  private JLabel            graphObenLinksLab;

  /**
   * Panel component - TextField to enter the position of the matrix components
   * upper left edge on the x-axis.
   */
  IntegerTextFieldEx        graphObenLinksX;

  /**
   * Panel component - TextField to enter the position of the upper left edge on
   * the y-axis of the matrix component.
   */
  IntegerTextFieldEx        graphObenLinksY;

  // ComboBox fuer startKnoten und zielknoten
  private JComboBox         nodeBox;
  JComboBox<String>         startNodeBox;
  JComboBox<String>         zielNodeBox;
  private JLabel            startNodeLab;
  private JLabel            zielNodeLab;

  /**
   * (constructor)<br>
   * Creates the panel object.
   * 
   * @param mainclass
   *          Reference to the projects mainclass
   */
  public CreateGraphPanel(final GraphAlgController mainclass) {
    super();

    this.mainclass = mainclass;
    this.mainclass.addPanel(PanelManager.PANEL_CREATEGRAPH, this);

    setLayout(null);

    // add(createGraphTypChooser());
    // add(createColorChooserButton());

    add(createTransferButton());
    add(createSaveButton());
    add(createLoadButton());
    createGraphCoordinats();

    createRadioButtons1(this);
    createRadioButtons2(this);
    createRadiusComboBox(this);
    createStartZielGraph();

    this.addAncestorListener(new AncestorListener() {

      public void ancestorAdded(final AncestorEvent e) {
        refreshPanelComponents();
      }

      public void ancestorMoved(final AncestorEvent e) {
      }

      public void ancestorRemoved(final AncestorEvent e) {
      }
    });
  }

  protected abstract JButton createClearButton();

  /*
   * +PANEL+CONSTRUCTION+METHODS+++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /*
   * private JButton createGraphTypChooser() { graphTypChooser = new JButton();
   * graphTypChooser.setSize(200, 25);
   * 
   * add(new SComponent(graphTypChooser, new String[]
   * {"Anderen Graphentyp wählen", "Select the typ of graphs"}, new String[]
   * {"Zeigt eine Auswahl der zu Verfügung stehenden Graphentypen an.",
   * "Shows the different graph types."}));
   * 
   * graphTypChooser.addActionListener(new ActionListener() { public void
   * actionPerformed(final ActionEvent e) {
   * mainclass.getGraphDatabase().showGraphTypDialog(); } });
   * 
   * return graphTypChooser; }
   */

  /**
   * (Panel construction method)<br>
   * Creates a button to open the colorchooser dialog.
   * 
   * @return Colorchooser button
   */
  /*
   * private JButton createColorChooserButton() { colorChooser = new JButton();
   * colorChooser.setBounds(70, 230, 150, 25);
   * 
   * add(new SComponent(colorChooser, new String[] { "Farbeinstellungen",
   * "Color Settings" }, new String[] {
   * "Zeigt die Farbeinstellungen der Graphen" + " Darstellungen an.",
   * "Shows the color settings of the graph." }));
   * 
   * colorChooser.addActionListener(new ActionListener() {
   * 
   * public void actionPerformed(final ActionEvent e) { new
   * ColorChooser(mainclass).setVisible(true); } });
   * 
   * return colorChooser; }
   */

  /**
   * (Panel construction method)<br>
   * Creates a button to load a graphscript file.
   * 
   * @return Load button
   */
  private JButton createLoadButton() {
    loadFileButton = new JButton();
    loadFileButton.setBounds(45, 370, 200, 25);
    add(new SComponent(loadFileButton, new String[] { "Vorlage laden",
        "Load Graph" }, new String[] {
        "Lädt einen neuen Graphen aus" + " einer Datei.",
        "Loads a new Graph from a File." }));

    loadFileButton.addActionListener(new ActionListener() {

      public void actionPerformed(final ActionEvent e) {
        loadFile();
      }
    });
    return loadFileButton;
  }

  /**
   * (Panel construction method)<br>
   * Creates the components to controll whether the graph is directed or not.
   * 
   * @param cont
   *          Panels container
   */
  private void createRadioButtons1(final Container cont) {
    typLabel = new JLabel();
    typLabel.setSize(130, 20);
    typLabel.setFont(new Font("Serif", Font.BOLD, 18));
    add(new SComponent(typLabel,
        new String[] { "Art des Graphen", "Graph Type" }/*
                                                         * , new int[][] {{70,
                                                         * 20, 150, 20}, {80,
                                                         * 20, 130, 20}}
                                                         */));
    cont.add(typLabel);

    /*-*/

    gerichtet = new JRadioButton();
    gerichtet.setBounds(90, 50, 100, 20);
    gerichtet.setSelected(true);
    add(new SComponent(gerichtet, new String[] { "Gerichtet", "Directed" }));

    /*-*/

    ungerichtet = new JRadioButton();
    ungerichtet.setBounds(90, 70, 100, 20);
    ungerichtet.setSelected(false);
    add(new SComponent(ungerichtet, new String[] { "Ungerichtet",
        "Not Directed" }));

    /*-*/

    ActionListener toggleListener = new ActionListener() {

      public void actionPerformed(final ActionEvent e) {

        if (ungerichtet == e.getSource()) {
          boolean sel = gerichtet.isSelected();

          ungerichtet.setSelected(!sel);
          gerichtet.setSelected(sel);

          if (sel) {
            if (mainclass
                .showUserMessage(MessageHandler.MESSAGE_DATA_MAY_BE_LOST)) {
              ungerichtet.setSelected(sel);
              gerichtet.setSelected(!sel);
            }
          } else {
            ungerichtet.setSelected(sel);
            gerichtet.setSelected(!sel);
          }
        } else if (gerichtet == e.getSource()) {

          boolean sel = ungerichtet.isSelected();

          gerichtet.setSelected(!sel);
          ungerichtet.setSelected(sel);

          if (!sel) {
            if (mainclass
                .showUserMessage(MessageHandler.MESSAGE_DATA_MAY_BE_LOST)) {
              gerichtet.setSelected(sel);
              ungerichtet.setSelected(!sel);
            }
          } else {
            gerichtet.setSelected(sel);
            ungerichtet.setSelected(!sel);
          }
        }
        mainclass.getGraph().setDirected(gerichtet.isSelected());
        mainclass.repaint();
      }
    };

    gerichtet.addActionListener(toggleListener);
    ungerichtet.addActionListener(toggleListener);

    cont.add(gerichtet);
    cont.add(ungerichtet);
  }

  /**
   * (Panel construction method)<br>
   * Creates the components to controll whether the graph is weighted or not.
   * 
   * @param cont
   *          Panels container
   */
  private void createRadioButtons2(final Container cont) {

    gewichtet = new JRadioButton();
    gewichtet.setBounds(90, 100, 100, 20);
    gewichtet.setSelected(true);

    add(new SComponent(gewichtet, new String[] { "Gewichtet", "Weighted" }));

    /*-*/

    ungewichtet = new JRadioButton();
    ungewichtet.setBounds(90, 120, 120, 20);
    ungewichtet.setSelected(false);

    add(new SComponent(ungewichtet, new String[] { "Ungewichtet",
        "Not weighted" }));
    /*-*/

    ActionListener toggleListener = new ActionListener() {
      public void actionPerformed(final ActionEvent e) {

        if (gewichtet == e.getSource()) {
          boolean sel = ungewichtet.isSelected();
          gewichtet.setSelected(sel);
          ungewichtet.setSelected(!sel);
        } else if (ungewichtet == e.getSource()) {
          boolean sel = gewichtet.isSelected();
          ungewichtet.setSelected(sel);
          gewichtet.setSelected(!sel);
        }
        mainclass.getGraph().setWeighted(gewichtet.isSelected());
        mainclass.repaint();
      }
    };

    gewichtet.addActionListener(toggleListener);
    ungewichtet.addActionListener(toggleListener);

    cont.add(gewichtet);
    cont.add(ungewichtet);
  }

  // bouchra
  public JComboBox createNodeBox(final int id) {
    nodeBox = new JComboBox();
    nodeBox.setFont(new Font("Serif", Font.BOLD, 16));

    // Positionieren der ComboBox
    if (id == 1) {
      nodeBox.setBounds(200, 185, 44, 25);
    } else {
      nodeBox.setBounds(200, 215, 44, 25);
    }

    // Ergänzen der Item Liste
    nodeBox.addItem(" ");

    AbstractGraph graph = mainclass.getGraph();
    int anz = 0;
    if (graph != null) {
      char sign = 'A';
      for (int i = 0; i < 26; i++, sign++) {
        if (graph.containsTag("" + sign)) {

          nodeBox.addItem(new Character(sign));
          anz++;
        }

      }
    }

    // Disabeln falls keine Knoten vorhanden
    if (anz == 0) {
      nodeBox.setEnabled(false);
    }

    nodeBox.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {

        // Auslesen der Tag Informationen
        if (startNodeBox != null) {
          char cStart = startNodeBox.getSelectedItem().toString().charAt(0);

          // Laden der zugehörigen Knoten

          AbstractGraph aktuGraph = mainclass.getGraph();
          AbstractNode strt = aktuGraph.getNode(cStart);
          aktuGraph.setStartNode(strt);

        }

      }
    });

    nodeBox.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {

        // Auslesen der Tag Informationen
        if (zielNodeBox != null) {
          char cZiel = zielNodeBox.getSelectedItem().toString().charAt(0);

          // Laden der zugehörigen Knoten
          AbstractGraph aktuGraph = mainclass.getGraph();
          AbstractNode end = aktuGraph.getNode(cZiel);
          aktuGraph.setTargetNode(end);

        }
      }
    });

    this.add(nodeBox);
    return nodeBox;
  }

  public void createStartZielGraph() {

    // Start Knoten Label
    startNodeLab = new JLabel();
    startNodeLab.setBounds(30, 185, 150, 25);

    this.add(new SComponent(startNodeLab, new String[] { "Startknoten",
        "Start Node" }));
    this.add(startNodeLab);

    // Erzeugt eine ComboBox mit dessen Hilfe man die Kennzeichnung
    // eines Knotens auswählen kann

    startNodeBox = createNodeBox(1);
    // Intitialisieren der StartNodeBox mit dem aktuellen Startknoten

    final AbstractGraph aktuGraph = mainclass.getGraph();
    if (aktuGraph.getStartNode() != null) {
      startNodeBox.setSelectedItem(aktuGraph.getStartNode().getTag());
    }

    // Ziel Knoten Label
    zielNodeLab = new JLabel();
    zielNodeLab.setBounds(30, 215, 150, 25);

    this.add(new SComponent(zielNodeLab, new String[] { "Zielknoten",
        "End Node" }));
    this.add(zielNodeLab);

    // Erzeugt eine ComboBox mit dessen Hilfe man die Kennzeichnung
    // eines Knotens auswählen kann

    zielNodeBox = createNodeBox(2);

    // Intitialisieren der StartNodeBox mit dem aktuellen Zielknoten

    final AbstractGraph aktuGraph1 = mainclass.getGraph();

    if (aktuGraph1.getTargetNode() != null) {
      zielNodeBox.setSelectedItem(aktuGraph1.getTargetNode().getTag());
    }

  }

  /**
   * (Panel construction method)<br>
   * Creates a combobox to select the diameter of the nodes.
   * 
   * @param cont
   *          Panels container
   */
  private void createRadiusComboBox(final Container cont) {
    labRadius = new JLabel();
    labRadius.setBounds(30, 200, 150, 25);
    add(new SComponent(labRadius, new String[] { "Durchmesser der Knoten",
        "Node diameter" }/*
                          * , new int[][] {{30, 180, 150, 25}, {60, 180, 120,
                          * 25}}
                          */));
    add(labRadius);

    /*-*/

    radBox = new JComboBox();
    radBox.setBounds(205, 200, 50, 25);

    for (int i = 30; i < 61; i++) {
      radBox.addItem("" + i);
    }

    radBox.setSelectedIndex(10);
    radBox.addActionListener(new ActionListener() {

      public void actionPerformed(final ActionEvent e) {
        int radius = Integer.parseInt(radBox.getSelectedItem().toString());

        AbstractGraph loadedgraph = mainclass.getGraph();
        if (loadedgraph != null) {
          loadedgraph.setDiameter(radius);
          mainclass.repaint();
        }
      }
    });

    cont.add(radBox);
  }

  /**
   * (Panel construction method)<br>
   * Creates a button to save a graphscript file.
   * 
   * @return Save button
   */
  private JButton createSaveButton() {
    saveFile = new JButton();
    saveFile.setBounds(45, 400, 200, 25);
    add(new SComponent(saveFile, new String[] { "Vorlage speichern",
        "Save Graph" }, new String[] { "Speichert den aktuellen Graphen ab.",
        "Saves the current used graph." }));

    saveFile.addActionListener(new ActionListener() {

      public void actionPerformed(final ActionEvent e) {
        saveFile();
      }
    });
    return saveFile;
  }

  /**
   * (Panel construction method)<br>
   * Creates a button that allows the transfer of the graph into graphscript.
   * 
   * @return Transfer button
   */
  private JButton createTransferButton() {
    transferGraph = new JButton();
    transferGraph.setBounds(45, 310, 200, 25);
    add(new SComponent(transferGraph, new String[] { "Graphen transferieren",
        "Transfer Graph" }, new String[] {
        "überführt den Graphen in die" + " GraphScript Notation.",
        "Translates the graph into the" + " graphscript notation." }));

    transferGraph.addActionListener(new ActionListener() {

      public void actionPerformed(final ActionEvent e) {
        if (mainclass.showUserMessage(MessageHandler.MESSAGE_DATA_MAY_BE_LOST)) {

          // mainclass.getGraphDatabase().transfer();

          mainclass.getGraphDatabase().transfer();

        }
      }
    });
    return transferGraph;
  }

  // Bouchra
  private void createGraphCoordinats() {

    // Beschriftungs Label
    graphObenLinksLab = new JLabel();
    graphObenLinksLab.setBounds(45, 118, 200, 25);

    add(new SComponent(graphObenLinksLab, new String[] { "Graph Koordinaten",
        "Graph Coordinates" }));
    this.add(graphObenLinksLab);

    // Eingabe Feld für die x Koordinate der oberen linke Ecke
    graphObenLinksX = new IntegerTextFieldEx(0, 1024);
    graphObenLinksX.setBounds(175, 120, 40, 25);
    graphObenLinksX.setEnabled(true);
    this.add(graphObenLinksX);

    // Eingabe Feld für die y Koordinate der oberen linke Ecke
    graphObenLinksY = new IntegerTextFieldEx(0, 1024);
    graphObenLinksY.setBounds(225, 120, 40, 25);
    graphObenLinksY.setEnabled(true);
    this.add(graphObenLinksY);

    // Madieha: Integertextfelder mit den richtiger Werten initialisieren
    final AbstractGraph g = mainclass.getGraph();
    if (g.getCorner() != null) {
      graphObenLinksX.setText("" + g.getCorner().x);
      graphObenLinksY.setText("" + g.getCorner().y);
    }

    FocusAdapter FA = new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        int ObenLinksX = graphObenLinksX.getValue().intValue();
        int ObenLinksY = graphObenLinksY.getValue().intValue();

        Uppercorner rect = new Uppercorner(ObenLinksX, ObenLinksY);
        g.setCorner(rect);
      }
    };
    graphObenLinksX.addFocusListener(FA);
    graphObenLinksY.addFocusListener(FA);

  }

  /*
   * +DISPLAY+METHODS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  public void paint(Graphics g) {
    Dimension size = this.getSize();

    int[] pos = new int[4];
    pos[0] = (size.width - 150) / 2;
    pos[1] = (size.width - 200) / 2;
    pos[2] = (size.width - 100) / 2;

    if (mainclass.getLanguageSettings() == LANGUAGE_GERMAN) {
      pos[3] = (size.width - 225) / 2;

      typLabel.setBounds((size.width - 130) / 2, 20, 130, 25);
      labRadius.setBounds(pos[3], 250, 150, 25);
      radBox.setLocation(pos[3] + 170, 250);
    } else {
      pos[3] = (size.width - 175) / 2;

      typLabel.setBounds((size.width - 100) / 2, 20, 100, 25);
      labRadius.setBounds(pos[3], 250, 160, 25);
      radBox.setLocation(pos[3] + 148, 250);
    }

    gerichtet.setLocation(pos[2], 50);
    ungerichtet.setLocation(pos[2], 70);
    gewichtet.setLocation(pos[2], 100);
    ungewichtet.setLocation(pos[2], 120);

    graphObenLinksLab.setLocation(pos[3], 156);
    graphObenLinksX.setLocation(pos[3] + 145, 156);
    graphObenLinksY.setLocation(pos[3] + 185, 156);

    // colorChooser.setLocation(pos[0], 286);
    transferGraph.setLocation(pos[1], 336);
    clearGraph.setLocation(pos[1], 366);
    loadFileButton.setLocation(pos[1], 396);
    saveFile.setLocation(pos[1], 426);

    super.paint(g);
  }

  /**
   * (internal display method)<br>
   * Updates the components used to describe the attributes of the graph.
   */
  private void refreshGraphComboBoxes() {
    AbstractGraph g = mainclass.getGraph();

    boolean flag = g.isDirected();
    gerichtet.setSelected(flag);
    ungerichtet.setSelected(!flag);

    flag = g.isWeighted();
    gewichtet.setSelected(flag);
    ungewichtet.setSelected(!flag);

    // createStartZielGraph();
    // Erneut die ComboBoxen zur Start Knoten Tag Wahl
    Object select = startNodeBox.getSelectedItem();
    this.remove(startNodeBox);
    startNodeBox = createNodeBox(1);
    startNodeBox.setSelectedItem(select);

    // Erneuert die ComboBoxen zur Ziel Knoten Tag Wahl
    select = zielNodeBox.getSelectedItem();
    this.remove(zielNodeBox);
    zielNodeBox = createNodeBox(2);
    zielNodeBox.setSelectedItem(select);

  }

  /**
   * (internal display method)<br>
   * Updates the panels components.
   */
  public void refreshPanelComponents() {
    // Madieha
    // refreshgraphCoordinats();
    refreshGraphComboBoxes();
    refreshTransferSaveButton();

    AbstractGraph graph = mainclass.getGraph();
    if (graph != null) {
      radBox.setSelectedItem("" + graph.getDiameter());
    }

  }

  /**
   * (internal display method)<br>
   * Updates to controll the en and disabling of the transfer and save buttons.
   */
  public void refreshTransferSaveButton() {
    boolean flag = (mainclass.getGraph().getNumberOfNodes() > 0);
    transferGraph.setEnabled(flag);
    saveFile.setEnabled(flag);
  }

  /*
   * +INTERNAL+USED+METHODS++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * (internal method)<br>
   * Saves a graphscript file.
   */
  public void saveFile() {

    FileChooserGSF fc = new FileChooserGSF();
    if (fc.showSaveDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {

      String filename = fc.getSelectedFile().getPath();
      if (filename.indexOf('.') == -1) {
        filename += ".gsf";
      }

      GraphScriptWriter writer = new GraphScriptWriter(filename);
      if (writer.startWriting(mainclass.getGraphDatabase().getSelectedEntry()) != 0) {

        mainclass.showUserMessage(MessageHandler.MESSAGE_ERROR_WRITING_FILE);
      }
    }
  }

  /**
   * (interal method)<br>
   * Loads a graphscript file.
   */
  public void loadFile() {
    if (mainclass.showUserMessage(MessageHandler.MESSAGE_DATA_MAY_BE_LOST)) {

      final FileChooserGSF fc = new FileChooserGSF();
      if (fc.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {

        final GraphScriptParser parser = new GraphScriptParser(
            mainclass.getGraphDatabase());

        if (parser.parse(fc.getSelectedFile().getPath()) == ParserUnit.STATE_FINISHED_PARSING) {

          mainclass.getGraphDatabase().setGraph(parser.getParsedGraph());

          ArrayList<String> eData = parser.getErrorMessages();
          if ((eData.size() > 1)
              && (mainclass
                  .showUserMessage(MessageHandler.MESSAGE_PARSER_ERRORS_GOTO_PREVIEW))) {

            SPanel pan = mainclass.getPanel(PanelManager.PANEL_GRAPHSCRIPT);

            ((GraphScriptPanel) pan).loadFile(fc.getSelectedFile().getPath());
            ((GraphScriptPanel) pan).parsePanelContent(1);
          }

          refreshPanelComponents();
          ((KantenPanelInterface) mainclass.getPanel(PanelManager.PANEL_KANTEN))
              .refreshPanelComponents();

          // mainclass.getModel().recreateAnimation();
          // mainclass.updateAlgoPanel();
          mainclass.repaint();
        } else {
          mainclass.showUserMessage(MessageHandler.MESSAGE_CANT_READ_FILE);
        }
      }
    }
  }
}
