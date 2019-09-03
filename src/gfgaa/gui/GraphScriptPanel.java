package gfgaa.gui;

import generators.framework.PropertiesGUI;
import generators.generatorframe.controller.GraphPanelListener;
import gfgaa.gui.components.SComponent;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.others.GraphDataBase;
import gfgaa.gui.others.PanelManager;
import gfgaa.gui.parser.GraphScriptParser;
import gfgaa.gui.parser.ParserUnit;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import algoanim.primitives.Graph;

/**
 * Panel class<br>
 * This panel contains the bnf of the GraphScript notation and a panel to
 * describe the graph in this notation and finally parse it.
 * 
 * @author S. Kulessa
 * @version 0.97
 */
public final class GraphScriptPanel extends SPanel {

  /** Maximum size of a file that can be loaded into the bnf input panel. */
  private static final int   FILE_MAXSIZE = 10000;

  /** Reference to the projects mainclass. */
  GraphAlgController mainclass;

  /**
   * Reference to the tabbed pane that contains the Parser- and the
   * BNFDescrPanel.
   */
  private JTabbedPane        outtab;

  /** Reference to the bnf input pane. */
  public JEditorPane         input;

  /** Reference to the ScrollPane which contains the bnf input pane. */
  JScrollPane        scroll;

  // private JButton save;
  private JButton            parse;
  // private JButton load;
  private JButton            edit;
  
  //------Nora Wester-----
  private GraphPanelListener listen;
  //----------------------

  /**
   * (internal parser method)<br>
   * Describes the current used graph in GraphScript Notation and loads this
   * description into the bnf input pane.
   * 
   * @param graphscript
   *          GraphScript Notation
   */
  public void showGraphData(final String graphscript) {
    input.setText(graphscript);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        scroll.getVerticalScrollBar().setValue(0);
      }
    });

    mainclass.setTopLevelPanel(new int[] { 2, 0 });
    ((ParserPanel) mainclass.getPanel(PanelManager.PANEL_PARSER))
        .displayMessage("** Transfer successfull");
  }

  JScrollPane test;

  /**
   * (Constructor)<br>
   * Creates a new GraphScript Panel Object.
   * 
   * @param mainclass
   *          Reference to the projects mainclass
   */
  public GraphScriptPanel(final GraphAlgController mainclass) {
    setLayout(null);
    this.setPreferredSize(new Dimension(750, 550));
    // this.setMinimumSize(new Dimension(335, 500));
    this.mainclass = mainclass;
    this.mainclass.addPanel(PanelManager.PANEL_GRAPHSCRIPT, this);

    add(createInputField());
    add(createOutputPanels());
    add(createParseButton());
    add(createEditButton());

    //---------Nora Wester-----
    listen = new GraphPanelListener();
    //-----------------------
    
    this.addAncestorListener(new AncestorListener() {

      /**
       * Zeigt die Syntax des aktuellen ausgewählten GraphenTypus.
       * 
       * @param e
       *          AncestorEvent
       */
      public void ancestorAdded(final AncestorEvent e) {

        GraphDataBase gb = mainclass.getGraphDatabase();

//        GraphEntry entry = 
        gb.getSelectedEntry();
        // graphTyp.setText(entry.getTitle());

        /*
         * syntax = entry.getBNFPane(); add(syntax);
         */
      }

      public void ancestorMoved(final AncestorEvent e) {
      }

      public void ancestorRemoved(final AncestorEvent e) {
      }
    });

    changeLanguageSettings(mainclass.getLanguageSettings());
  }

  /**
   * (panel construction method)<br>
   * Creates the load button.
   * 
   * @return Load button
   */
  /*
   * private JButton createLoadButton() { load = new JButton();
   * load.setBounds(400, 370, 124, 40); add(new SComponent(load, new String[]
   * {"Datei laden", "Load File"}, new String[]
   * {"Lädt eine Datei in das Eingabe Pane.",
   * "Loads a graph into the input pane."}));
   * 
   * load.addActionListener(new ActionListener() {
   *//**
   * öffnet einen FileChooser Dialog und lädt die angegebene Datei in das bnf
   * intput panel. Die maximale Größe einer Datei ist auf 10000 Zeichen
   * beschränkt.
   * 
   * @param e
   *          ActionEvent
   */
  /*
   * public void actionPerformed(final ActionEvent e) { if
   * (mainclass.showUserMessage( MessageHandler.MESSAGE_DATA_MAY_BE_LOST)) {
   * FileChooserGSF fc = new FileChooserGSF();
   * 
   * if (fc.showOpenDialog(new JPanel()) == FileChooserGSF.APPROVE_OPTION) {
   * 
   * loadFile(fc.getSelectedFile().getPath()); } } } }); return load; }
   */

  /**
   * (internal data method)<br>
   * Parses the content of the input panel.
   * 
   * @param idFlag
   *          Display flag
   */
  public void parsePanelContent(final int idFlag) {
    GraphScriptParser parser = new GraphScriptParser(mainclass
        .getGraphDatabase());
    boolean finished = true;

    if (parser.parse(input) == ParserUnit.STATE_FINISHED_PARSING) {
      mainclass.getGraphDatabase().setGraph(parser.getParsedGraph());
      //----------Nora Wester-----
      listen.graphSet(input.getText());
      //--------------------------
    } else {
      finished = false;
      mainclass.showUserMessage(MessageHandler.MESSAGE_SCRIPT_CANT_PARSED);

      return;
    }

    ArrayList<String> eData = parser.getErrorMessages();
    ((ParserPanel) mainclass.getPanel(PanelManager.PANEL_PARSER))
        .displayMessage(eData);

    if (idFlag == 0) {
      if ((eData.size() == 1)
          || ((eData.size() > 1) && (finished) && (mainclass
              .showUserMessage(MessageHandler.MESSAGE_PARSER_ERRORS_GOTO_INPUTPANEL)))) {
        mainclass.setTopLevelPanel(new int[] { 0, 2 });
      }
    } else if (idFlag == 1) {
      mainclass.setTopLevelPanel(new int[] { 1, 0 });
    }

    mainclass.repaint();
  }

  /**
   * (internal data method)<br>
   * Loads a file into the input panel.
   * 
   * @param path
   *          Path to the selected file
   */
  public void loadFile(final String path) {
    FileReader reader = null;
    try {
      reader = new FileReader(path);

      String datei = "";
      int anz = 0;
      int count = 0;
      do {
        char[] cBuf = new char[100];
        anz = reader.read(cBuf);
        count += anz;

        for (int i = 0; i < anz; i++) {
          datei += cBuf[i];
        }
      } while (anz != -1 && count < FILE_MAXSIZE);

      input.setText(datei);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          scroll.getVerticalScrollBar().setValue(0);
        }
      });

      if (count == FILE_MAXSIZE) {
        ((ParserPanel) mainclass.getPanel(PanelManager.PANEL_PARSER))
            .displayErrorMessage(1, path);
      } else {
        ((ParserPanel) mainclass.getPanel(PanelManager.PANEL_PARSER))
            .displayMessage("** Loading File was successfull");
      }
    } catch (FileNotFoundException fnfe) {
      ((ParserPanel) mainclass.getPanel(PanelManager.PANEL_PARSER))
          .displayErrorMessage(2, path);
      mainclass.showUserMessage(MessageHandler.MESSAGE_SCRIPT_CANT_PARSED);
    } catch (IOException ioe) {
      ((ParserPanel) mainclass.getPanel(PanelManager.PANEL_PARSER))
          .displayErrorMessage(3, path);
      mainclass.showUserMessage(MessageHandler.MESSAGE_SCRIPT_CANT_PARSED);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException ioe) {
          ((ParserPanel) mainclass.getPanel(7)).displayErrorMessage(4, path);
        }
      }
    }

    setTopLevelPanel(new int[] { 0 });
  }

  /**
   * (panel construction method)<br>
   * Creates the save button.
   * 
   * @return Save button
   */
  /*
   * private JButton createSaveButton() { save = new JButton();
   * save.setBounds(525, 370, 124, 40); add(new SComponent(save, new String[]
   * {"Datei speichern", "Save File"}, new String[] {"Speichert den Inhalt des"
   * + " Eingabe Panes ab.", "Saves the content of the" + " input pane."}));
   * 
   * save.addActionListener(new ActionListener() {
   *//**
   * Öffnet einen FileChooser Dialog und speichert den Inhalt des bnf input
   * Pane in die gewählte Datei.
   * 
   * @param e
   *          ActionEvent
   */
  /*
   * public void actionPerformed(final ActionEvent e) { FileChooserGSF fc = new
   * FileChooserGSF();
   * 
   * if (fc.showSaveDialog(new JPanel()) == FileChooserGSF.APPROVE_OPTION) {
   * 
   * String filename = fc.getSelectedFile().getPath(); if (filename.indexOf('.')
   * == -1) { filename += ".gsf"; }
   * 
   * GraphScriptWriter writer = new GraphScriptWriter(filename);
   * writer.startWriting(input);
   * 
   * mainclass.repaint(); } } }); return save; }
   */
  /**
   * (panel construction method)<br>
   * Creates the parse button.
   * 
   * @return Parse button
   */
  private JButton createEditButton() {
    edit = new JButton();

    edit.setBounds(595, 463, 105, 35);
    // edit.setLocation(650,470);

    add(new SComponent(edit, new String[] { "Edit Graph", "Edit Script" },
        new String[] { "Parst den Inhalt des Eingabe Pane.",
            "Parses the content of the" + " input pane." }));

    edit.addActionListener(new ActionListener() {

       Graph graph = null;
       /**
        * Bei einem Druck des Buttons wird versucht den Inhalt des bnf input Pane
        * in eine graphische Darstellung im PreviewPanel zu übersetzen.
        * 
        * @param e
        *          ActionEvent
        */
      public void actionPerformed(final ActionEvent e) {
        // Madieha
        graph = PropertiesGUI.getGraphFromScriptFile();
        if (graph != null) {
          parsePanelContent(0);
        }
        GraphAlgGUI test = mainclass.getGUI();
        test.setVisible(true);
      }

    });

    return edit;
  }

  private JButton createParseButton() {
    parse = new JButton();

    parse.setBounds(595, 415, 105, 35);

    add(new SComponent(parse, new String[] { "Skript parsen", "Parse Script" },
        new String[] { "Parst den Inhalt des Eingabe Pane.",
            "Parses the content of the" + " input pane." }));

    parse.addActionListener(new ActionListener() {

      /**
       * Bei einem Druck des Buttons wird versucht den Inhalt des bnf input Pane
       * in eine graphische Darstellung im PreviewPanel zu übersetzen.
       * 
       * @param e
       *          ActionEvent
       */
      public void actionPerformed(final ActionEvent e) {
        if (mainclass.showUserMessage(MessageHandler.MESSAGE_DATA_MAY_BE_LOST)) {
          parsePanelContent(0);
          // mainclass.getModel().recreateAnimation();
        }
      }
    });

    return parse;
  }

  /**
   * (panel construction method)<br>
   * Makes the bnf input pane scrollable.
   * 
   * @return Scrollable pane
   */
  private JScrollPane createInputField() {
    input = new JEditorPane();
    input.setText("%graphscript");
    scroll = new JScrollPane(input);
    scroll.setBounds(20, 15, 680, 395);

    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.setPreferredSize(new Dimension(400, 600));

    return scroll;
  }

  /**
   * (panel construction method)<br>
   * Creates the tabbed pane and add the subpanels to it.
   * 
   * @return Tabbed pane
   */
  private JTabbedPane createOutputPanels() {
    String[][] tabTitels = new String[][] { { "Parser" }, { "Parser" } };

    outtab = new JTabbedPane();
    outtab.setBounds(20, 415, 570, 105);
    outtab.setTabPlacement(SwingConstants.BOTTOM);
    setTabbedPane(outtab, tabTitels);

    SPanel outpars = new ParserPanel(mainclass);

    int flag = mainclass.getLanguageSettings();
    outtab.add(tabTitels[flag][0], outpars);

    add(outpars, false);

    return outtab;
  }

  /** Panel component - JButton to select the previous graphtyp. */
   JButton     left;

  /** Panel component - JButton to select the next graphtyp. */
   JButton     right;

  /** Panel component - JEditorPane to display the graphtyps name. */
   JEditorPane graphTyp;

  /**
   * (panel construction method)<br>
   * Creates the BNFChooser component.
   */
  /*
   * private void createBNFChooser() { left = new JButton("<<<");
   * left.setSize(55, 20);
   * 
   * left.addActionListener(new ActionListener() {
   *//**
   * Zeigt die Syntax des vorhergehenden Graphentypus in der Graphen Datenbank
   * an.
   * 
   * @param e
   *          ActionEvent
   */
  /*
   * public void actionPerformed(final ActionEvent e) { GraphDataBase gb =
   * mainclass.getGraphDatabase(); int index =
   * gb.getIndexOf(graphTyp.getText()); remove(syntax);
   * 
   * index--; if (index < 0) { index = gb.size() - 1; }
   * 
   * GraphEntry entry = gb.getEntry(index); graphTyp.setText(entry.getTitle());
   * 
   * syntax = entry.getBNFPane(); add(syntax);
   * 
   * mainclass.repaint(); } });
   * 
   * this.add(left);
   * 
   * right = new JButton(">>>"); right.setSize(55, 20);
   * 
   * right.addActionListener(new ActionListener() {
   *//**
   * Zeigt die Syntax des nachfolgenden Graphentypus in der Graphen Datenbank
   * an.
   * 
   * @param e
   *          ActionEvent
   */
  /*
   * public void actionPerformed(final ActionEvent e) { GraphDataBase gb =
   * mainclass.getGraphDatabase();
   * 
   * int index = gb.getIndexOf(graphTyp.getText()); remove(syntax);
   * 
   * index++; if (index >= gb.size()) { index = 0; }
   * 
   * GraphEntry entry = gb.getEntry(index); graphTyp.setText(entry.getTitle());
   * 
   * syntax = entry.getBNFPane(); add(syntax);
   * 
   * mainclass.repaint(); } });
   * 
   * this.add(right);
   * 
   * graphTyp = new JEditorPane();
   * graphTyp.setBorder(BorderFactory.createEtchedBorder());
   * graphTyp.setEditable(false); graphTyp.setSize(235, 21);
   * 
   * GraphEntry entry = mainclass.getGraphDatabase().getSelectedEntry();
   * graphTyp.setText(entry.getTitle());
   * 
   * syntax = entry.getBNFPane(); this.add(syntax);
   * 
   * this.add(graphTyp); }
   */

  /**
   * (panel display method)<br>
   * Draws the components of the panel.
   * 
   * @param g
   *          Graphical component of this panel
   */
  /*
   * public void paint(final Graphics g) { Dimension size = this.getSize();
   * int[] pos = new int[3]; pos[0] = (size.width - 758) / 2; pos[1] =
   * (size.height - 500) / 2; pos[2] = pos[1] + 400;//354;
   * 
   * scroll.setLocation(pos[0]-95, pos[1]); edit.setLocation(pos[0] + 510,
   * pos[2]);
   * 
   * parse.setLocation(pos[0] + 510, pos[2]+48);
   * 
   * outtab.setLocation(pos[0]-110, pos[1] + 400);
   * 
   * super.paint(g); }
   */
  /**
   * empty method - not in use.
   * @see SPanel#refreshPanelComponents
   * @deprecated
   */
  public void refreshPanelComponents() {

  }
}
