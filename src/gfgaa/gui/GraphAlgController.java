package gfgaa.gui;

import gfgaa.gui.components.ColorChooserComboBox;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.exceptions.GraphTypeException;
import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.graphs.GraphEntry;
import gfgaa.gui.graphs.MatrixPanelInterface;
import gfgaa.gui.graphs.basic.BasicGraphEntry;
import gfgaa.gui.others.GraphDataBase;
import gfgaa.gui.others.LanguageInterface;
import gfgaa.gui.others.PanelManager;

import java.util.HashMap;

/**
 * Hauptklasse des Projektes Enthält alle wichtige Daten und Referenzen auf die
 * einzelnen Panels zwecks Kommunikation der Panels untereinander.
 * 
 * @author S. Kulessa
 * @version 0.92
 */
public final class GraphAlgController implements LanguageInterface {

  /**
   * Internal array position of the MainPanel. public static final int
   * PANEL_MAIN = 0;
   * 
   * /** Internal array position of the EditGraphPanel. public static final int
   * PANEL_EDITGRAPH = 1;
   * 
   * /** Internal array position of the PreviewPanel. public static final int
   * PANEL_PREVIEW = 2;
   * 
   * /** Internal array position of the CreateGraphPanel. public static final
   * int PANEL_CREATEGRAPH = 3;
   * 
   * /** Internal array position of the KantenPanel. public static final int
   * PANEL_KANTEN = 4;
   * 
   * /** Internal array position of the MatrixPanel. public static final int
   * PANEL_MATRIX = 5;
   * 
   * /** Internal array position of the GraphScriptPanel. public static final
   * int PANEL_GRAPHSCRIPT = 6;
   * 
   * /** Internal array position of the ParserPanel. public static final int
   * PANEL_PARSER = 7;
   * 
   * /** Internal array position of the BNFDescriptionPanel. public static final
   * int PANEL_BNFDESCRIPTION = 8;
   */

  /** Reference to the GraphAlgorithm Database. */

  /** Reference to the Graph Database. */
  private GraphDataBase  gb;

  private PanelManager   pm;

  /** Reference to the model component. */
  // private GAModel graphAlgoModel;
  /** Reference to the GUI. */
  private GraphAlgGUI    gui;

  /** Reference to the GraphDrawer object. */
  private GraphDrawer    drawer;

  /** Reference to the MessageHandler object. */
  private MessageHandler messages;

  /**
   * Reference to every used panel. private SPanel[] allMyPanels = new
   * SPanel[9];
   */

  /**
   * (constructor)<br>
   * Creates and shows the Graphical User Interface and initializes every
   * necessary setting.
   */
  public GraphAlgController() {
    new GraphAlgController(this.languageFlag);
  }

  /**
   * (constructor)<br>
   * Creates and shows the Graphical User Interface and initializes every
   * necessary setting.
   * 
   * @param languageFlag
   *          Language settings constant
   * 
   */
  public GraphAlgController(final int languageFlag) {

    this.languageFlag = languageFlag;
    // this.loadedGraph = new Graph(true, true);

    this.messages = new MessageHandler(this);

    // Initialisierung der Hinweise Flags
    this.hintsEnabled = new boolean[4];
    for (int i = 0; i < 4; i++) {
      this.hintsEnabled[i] = true;
    }

    this.pm = new PanelManager();

    // Initialisierung der Graph Datenbank
    this.gb = new GraphDataBase(this);
    this.gb.addGraph(new BasicGraphEntry(this));
    this.gb.initialize();

    // Initialisierung der Algorithmen Datenbank

    this.drawer = new GraphDrawer();
    this.gui = new GraphAlgGUI(this);
  }

  /* +DISPLAY+METHODS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

  /**
   * (internal recursive display method)<br>
   * Repaints every panel of the GUI.
   */
  public void repaint() {
    if (gui != null) {
      gui.repaint();
    }
  }

  /**
   * (internal recursive display method)<br>
   * Set the specified sub panels on top.
   * 
   * @param panelId
   *          Internal panel identification
   */
  protected void setTopLevelPanel(final int[] panelId) {
    gui.setTopLevelPanel(panelId);
  }

  /**
   * (internal display method)<br>
   * Sets the GUI visible state to visible or invisible.
   * 
   * @param isVisible
   *          Whether the GUI will become visible or not
   */
  public void setVisible(final boolean isVisible) {
    this.gui.setVisible(isVisible);
  }

  /* +INTERNAL+METHODS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

  /**
   * (internal data method)<br>
   * Adds the speciefied panel to the main class.
   * 
   * @param pos
   *          Internal panel identification
   * @param panel
   *          New panel
   */
  public void addPanel(final int pos, final SPanel panel) {
    this.pm.addPanel(pos, panel);
  }

  /**
   * (internal data method)<br>
   * Sets the model of the graphalgo panelgroup.
   * 
   * @param graphAlgoModel
   *          Reference to the model
   */
  /*
   * public void setModel(final GAModel graphAlgoModel) { this.graphAlgoModel =
   * graphAlgoModel; }
   */

  /**
   * (internal data method)<br>
   * Returns the Algorithm Database.
   * 
   * @return Algorithm Database
   */

  /**
   * (internal data method)<br>
   * Returns the Graph Database.
   * 
   * @return Graph Database
   */
  public GraphDataBase getGraphDatabase() {
    return this.gb;
  }

  /**
   * (internal data method)<br>
   * Returns the current used Graph.
   * 
   * @return Current used Graph
   */
  public AbstractGraph getGraph() {
    return gb.getSelectedGraph();
  }

  public Integer getGraphTyp() {
    return gb.getGraphTyp();
  }

  /**
   * (internal data method)<br>
   * Returns the Object which draws the Graph.
   * 
   * @return GraphDrawer object
   */
  protected GraphDrawer getGraphDrawer() {
    return drawer;
  }

  /**
   * (internal data method)<br>
   * Returns the specified panel.
   * 
   * @param pos
   *          Internal panel identification
   * @return Specified panel
   */
  public SPanel getPanel(final int pos) {
    return this.pm.getPanel(pos);
  }

  /**
   * (internal data method)<br>
   * Returns the model of the graphalgo panelgroup.
   * 
   * @return Reference to the model
   */
  /*
   * public GAModel getModel() { return this.graphAlgoModel; }
   */
  /**
   * (internal data method)<br>
   * Set the reference of the new graph.
   * 
   * @param newGraph
   *          New graph object
   * 
   *          public void setGraphEntry(final AbstractGraph newGraph) {
   *          gb.setGraph(newGraph); }
   */

  /**
   * (internal display method)<br>
   * Displays a dialog with the speciefied messageId.
   * 
   * @param messageId
   *          Internal message identification
   * @return Answer of the user
   */
  public boolean showUserMessage(final int messageId) {
    messages.setMessage(messageId);
    if (hintsEnabled[messages.getMessageType()]) {
      messages.setMessageVisible();
      return messages.getAnswer();
    }
    return true;
  }

  /* +INTERNAL+MENU+COMMUNICATION+METHODS++++++++++++++++++++++++++++++++++++++ */

  /** Specification of the different hint types. */
  private boolean[] hintsEnabled;

  /** Current language settings. */
  private int       languageFlag = 0;

  /** Current transfer settings. */
  private int       transferMode = 0;

  /**
   * (internal info method)<br>
   * Returns the current used transfer mode.
   * 
   * @return Current transfer mode
   */
  public int getTransferMode() {
    return this.transferMode;
  }

  /**
   * (internal data method)<br>
   * Sets the transfer mode for GraphScript transfer and save operations.
   * 
   * @param transferMode
   *          Internal transfer mode identification
   */
  protected void setTransferMode(final int transferMode) {
    this.transferMode = transferMode;
  }

  /**
   * (internal info method)<br>
   * Returns the current language settings.
   * 
   * @return Current language settings
   */
  public int getLanguageSettings() {
    return languageFlag;
  }

  /**
   * (internal recursive display method)<br>
   * Changes the language settings of every panel and their components.
   * 
   * @param languageFlag
   *          New language settings
   */
  protected void changeLanguageSettings(final int languageFlag) {
    if (this.languageFlag != languageFlag) {
      this.languageFlag = languageFlag;
      gui.changeLanguageSettings(this.languageFlag);

      gb.changeLanguageSettings();
    }
  }

  /**
   * (internal info method)<br>
   * Returns if the specified hint class is en- or disabeld.
   * 
   * @param pos
   *          Internal hint identification
   * @return Wheter the hint-class is enabeld or not
   */
  protected boolean getHintsEnabeld(final int pos) {
    return this.hintsEnabled[pos];
  }

  /**
   * (internal info method)<br>
   * En-/disabels the specified hints.
   * 
   * @param pos
   *          Internal hint identification
   * @param enabled
   *          Wheter the hint-class should be enabeld or disabeld
   */
  protected void setHintsEnabeld(final int pos, final boolean enabled) {
    this.hintsEnabled[pos] = enabled;
  }

  /* +ADD+GRAPHALGORITHM+METHODS+++++++++++++++++++++++++++++++++++++++++++++++ */

  public void addGraph(final GraphEntry entry) throws GraphTypeException {
    this.gb.addGraphType(entry);
  }

  /**
   * (<b>public method</b>)<br>
   * Adds the given algorithm component to the GUI.
   * 
   * @param algo
   *          GraphAlgorithm that should be added
   * @throws GraphAlgoException
   *           If the GraphAlgorithm can not be added
   */

  /**
   * (<b>public method</b>)<br>
   * Initializes/Updates the AlgorithmChooser ComboBox and the Algorithm Menu
   * Entriys.
   */
  /*
   * public void initAlgorithmComponents() {
   * 
   * // Erzeugt die Algorithmus Einträge im Menü
   * this.getGUI().createAlgorithmMenu(db);
   * 
   * // Erzeugt die ComboBox Einträge im Menü // Setzt den ursprünglichen Index
   * sowohl im Menü als auch // in der ComboBox - Reihenfolge beachten!
   * this.graphAlgoModel.createAlgorithmChooser(this.db); }
   */
  /**
   * (internal error method)<br>
   * Displays an error message if eFlag is TRUE.
   * 
   * @param eFlag
   *          Error flag
   */
  public void setAlgoExcepFlag(final boolean eFlag) {
    if (eFlag) {
      this.showUserMessage(MessageHandler.MESSAGE_CANT_ADD_ALGORITHM);
    }
  }

  /* +PUBLIC+METHODS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

  /**
   * (<b>public method</b>)<br>
   * Returns the GraphicalUserInterface.
   * 
   * @return Reference to the GUI object
   */
  public GraphAlgGUI getGUI() {
    return gui;
  }

  /**
   * (<b>public method</b>)<br>
   * Returns the adjacencymatrix of the current used graph.
   * 
   * @return Adjacencymatrix
   */
  public int[][] getAdjacencyMatrix() {
    return ((MatrixPanelInterface) this.pm.getPanel(PanelManager.PANEL_MATRIX))
        .getAdjacencyMatrix();
  }

  /**
   * (<b>public method</b>)<br>
   * Returns the colorsettings used for the graph animation. (look at
   * ColorChooser for the array identifier constants)
   * 
   * @return Graph animation color settings
   */
  public String[] getGraphColorSettings() {
    java.awt.Color[] graphColors = this.drawer.getColorSettings();

    int l = graphColors.length;
    String[] gracol = new String[l];
    for (int i = 0; i < l; i++) {
      gracol[i] = ColorChooserComboBox.getStringForColor(graphColors[i]);
    }
    return gracol;
  }

  public HashMap getWeightLabelSet() {
    return this.drawer.getWeightLabelSet();
  }

  /**
   * (<b>public method</b>)<br>
   * Returns the colorsettings used for the matrix animation. (look at
   * AnimalMatrixGenerator for the array identifier constants)
   * 
   * @return Matrix animation color settings
   */
  /*
   * public String[] getMatrixColorSettings() { return
   * this.graphAlgoModel.getMatrixColorSettings(); }
   */

  /**
   * (internal data method)<br>
   * Sets the state of the algorithm component to outdated.
   */
  /*
   * public void setAlgorithmComponentOutdated() {
   * this.graphAlgoModel.setAlgorithmComponentState(GAModel
   * .COMPONENT_OUTDATED); }
   */

  /**
   * (internal info method)<br>
   * Returns the state of the algorithm component.
   * 
   * @return State ot the algorihm component
   */
  /*
   * public int getAlgorithmComponentState() { return
   * this.graphAlgoModel.getAlgorithmComponentState(); }
   *//**
   * (<b>public method</b>)<br>
   * Updates the GraphAlgorithm Panel.
   */
  /*
   * public void updateAlgoPanel() { this.graphAlgoModel.update(); }
   */
}
