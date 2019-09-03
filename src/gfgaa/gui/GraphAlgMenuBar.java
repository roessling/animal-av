package gfgaa.gui;

import gfgaa.gui.graphs.CreateGraphPanel;
import gfgaa.gui.others.LanguageInterface;
import gfgaa.gui.others.PanelManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/** MenuBar of the GUI.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public final class GraphAlgMenuBar extends JMenuBar
                                   implements LanguageInterface {

    /**
   * 
   */
  private static final long serialVersionUID = 631583197506199373L;

    /** Dialog class<br>
      * Authors dialog - giving information about me and the program.
      *
      * @author S. Kulessa
      * @version 0.97c
      */
    private final class MyDia extends JDialog {

        /**
       * 
       */
      private static final long serialVersionUID = -6720591781211991375L;

        /** Panel class<br>
          * Panel of authors dialog.
          *
          * @author S. Kulessa
          * @version 0.97c
          */
        private final class MyPanel extends JPanel {

          /**
           * 
           */
          private static final long serialVersionUID = -2245165579751107497L;

            /** (constructor)<br>
              * Creates a new panel containing information about
              * me and the program.
              */
            public MyPanel() {
                this.setLayout(null);
                this.setSize(200, 200);

                JButton myBut = new JButton("Close");
                myBut.setBounds(60, 140, 80, 25);

                myBut.addActionListener(new ActionListener() {

                    /** Schließt das Fenster
                      *
                      * @param e    ActionEvent
                      */
                    public void actionPerformed(final ActionEvent e) {
                        close();
                    }
                });
                this.add(myBut);
            }

            /** (panel display method)<br>
              * Draws the components of the panel.
              *
              * @param g    Graphical component of this panel
              */
            public void paint(final Graphics g) {
                int panWidth = this.getSize().width;
                Image offImage = createImage(panWidth,
                                             this.getSize().height);
                Graphics offGraphics = offImage.getGraphics();
                super.paint(offGraphics);

                // Texte
                String[] text = {mainclass.getLanguageSettings()
                                           == LANGUAGE_GERMAN
                                 ? "Ein Programm von" : "A program from",
                                 "Simon Kulessa",
                                 "TU Darmstadt",
                                 "Fachbereich Informatik",
                                 "S.Kulessa@gmx.de",
                                 "Version 0.97c - 10.04.2007"};

                // Größen und y-Positionen
                int[] sizes = {12, 20, 16, 16, 14, 12};
                int[] ycord = {20, 40, 65, 80, 105, 130};

                // Zeichnen der Schriftzüge
                offGraphics.setColor(Color.BLACK);
                for (int i = 0; i < 6; i++) {
                    Font serifBold = new Font("Serif",
                                              Font.BOLD,
                                              sizes[i]);
                    Rectangle2D sis = serifBold.getStringBounds(text[i],
                                      new FontRenderContext(
                                              new AffineTransform(),
                                              false, false));

                    offGraphics.setFont(serifBold);
                    offGraphics.drawString(text[i],
                                           (int) (panWidth
                                                  - sis.getWidth()) / 2,
                                           ycord[i]);
                }

                g.drawImage(offImage, 0, 0, this);
            }
        }

        /** (Constructor)<br>
          * Creates an author dialog.
          */
        public MyDia() {
            super(mainclass.getGUI(), true);

            String[] title = new String[] {"Autor", "Author"};
            this.setTitle(title[mainclass.getLanguageSettings()]);

            this.setSize(200, 200);
            this.setResizable(false);

            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((d.width - getSize().width) / 2,
                        (d.height - getSize().height) / 2);

            this.setContentPane(new MyPanel());
        }

        /** (internal displays method)<br>
          * Sets the panel visible state to invisible.
          */
        public void close() {
            this.setVisible(false);
            this.dispose();
        }
    }

    /** Reference to the projects mainclass. */
    GraphAlgController mainclass;

    /** (constructor)<br>
      * Creates the MenuBar component of the GUI.
      *
      * @param mainclass    Reference to the projects mainclass
      */
    public GraphAlgMenuBar(final GraphAlgController mainclass) {

      // Erzeugen der Panel Componenten
      this.mainclass = mainclass;
      this.add(createSystemMenu());
      this.add(createPanelMenu());
      this.add(createSetupMenu());
      //this.add(createAnimationMenu());
      this.add(createAboutMenu());

      // Laden der aktuellen Spracheinstellungen
      this.changeLanguageSettings(mainclass.getLanguageSettings());
    }

    /** (internal language method)<br>
      * Changes the language settings.
      *
      * @param languageFlag     Internal language identification
      */
    public void changeLanguageSettings(final int languageFlag) {
        String[][] text = new String[2][6];

        // System Menü Einträge
        text[0][0] = "Datei";
        text[0][1] = "Vorlage laden ...";
        text[0][2] = "Vorlage speichern ...";
        text[0][3] = "Graphen transferieren";
        text[0][4] = "Beenden";

        text[1][0] = "File";
        text[1][1] = "Load graph";
        text[1][2] = "Save graph";
        text[1][3] = "Transfer graph";
        text[1][4] = "Exit";

        this.system.setText(text[languageFlag][0]);
        this.sysLoadGraph.setText(text[languageFlag][1]);
        this.sysSaveGraph.setText(text[languageFlag][2]);
        this.sysTransfer.setText(text[languageFlag][3]);
        this.sysExit.setText(text[languageFlag][4]);

        // Ansichts Menü Einträge
        text[0][0] = "Ansicht";
        text[0][1] = "Eigenschaften des Graphen";
        text[0][2] = "Graphen bearbeiten";
        text[0][3] = "Adjazenz Matrix";
        text[0][4] = "Graph Script";
        text[0][5] = "Graphenalgorithmen";

        text[1][0] = "View";
        text[1][1] = "Graph attributes";
        text[1][2] = "Edit Graph";
        text[1][3] = "Adjacencymatrix";
        text[1][4] = "Graphscript";
        text[1][5] = "Graphalgorithms";

        this.panels.setText(text[languageFlag][0]);
        this.panAttribute.setText(text[languageFlag][1]);
        this.panGraphen.setText(text[languageFlag][2]);
        this.panMatrix.setText(text[languageFlag][3]);
        //this.panSkript.setText(text[languageFlag][4]);
        //this.panAlgos.setText(text[languageFlag][5]);

        // Einstellungs Menü Einträge
        text[0][0] = "Sprache";
        text[1][0] = "Language";

        this.language.setText(text[languageFlag][0]);

        text[0][0] = "PopUp Meldungen";
        text[0][1] = "Warnungen";
        text[0][2] = "Parser Meldungen";
        text[0][3] = "Fehlermeldungen";

        text[1][0] = "Popup messages";
        text[1][1] = "Warnings";
        text[1][2] = "Parser messages";
        text[1][3] = "Error messages";

        this.popups.setText(text[languageFlag][0]);
        this.setHints.setText(text[languageFlag][1]);
        this.setWarnings.setText(text[languageFlag][2]);
        this.setErrors.setText(text[languageFlag][3]);

        text[0][0] = "Einstellungen";
        text[0][1] = "Farbeinstellungen";
        text[0][2] = "Graphtypeinstellungen";

        text[1][0] = "Options";
        text[1][1] = "Color settings";
        text[1][2] = "Graphtyp settings";

        this.setup.setText(text[languageFlag][0]);
        this.setColors.setText(text[languageFlag][1]);
        this.setGraphTyp.setText(text[languageFlag][2]);

        /*//Animations Menü Einträge
        text[0][0] = "Algorithmus wählen";
        text[0][1] = "Komponenten aktivieren";

        text[1][0] = "Choose Algorithm";
        text[1][1] = "Activate Components";

        //this.algorithmus.setText(text[languageFlag][0]);
        //this.algoComponents.setText(text[languageFlag][1]);

        text[0][0] = "Animation erzeugen";
        text[0][1] = "Animation löschen";
        text[0][2] = "Animation Code ansehen";
        text[0][3] = "Animation Code speichern";

        text[1][0] = "Generate animation";
        text[1][1] = "Delete animation";
        text[1][2] = "View animation code";
        text[1][3] = "Save animation code";

        this.animGenerate.setText(text[languageFlag][0]);
        this.animDelete.setText(text[languageFlag][1]);
        this.animView.setText(text[languageFlag][2]);
        this.animSave.setText(text[languageFlag][3]);
*/
        // Hilfe Menü Einträge
        text[0][0] = "Hilfe";
        text[0][1] = "Autor";

        text[1][0] = "Help";
        text[1][1] = "Author";

        this.help.setText(text[languageFlag][0]);
        /*this.helpLog.setText("Changelog");*/
        this.helpAbout.setText(text[languageFlag][1]);

        // Mnemonics Einstellungen
        char[][] keys = new char[][] {{'D', 'F'}, {'A', 'V'}, {'E', 'O'},
                                      {'S', 'L'}, {'F', 'C'}, {'N', 'A'},
                                      {'W', 'C'}, {'K', 'A'}, {'E', 'G'},
                                      {'L', 'D'}, {'A', 'V'}, {'G', 'G'}};

        this.system.setMnemonic(keys[0][languageFlag]);
        this.panels.setMnemonic(keys[1][languageFlag]);
        this.setup.setMnemonic(keys[2][languageFlag]);
        this.language.setMnemonic(keys[3][languageFlag]);
        this.setColors.setMnemonic(keys[4][languageFlag]);
       /* this.animation.setMnemonic(keys[5][languageFlag]);
        this.algorithmus.setMnemonic(keys[6][languageFlag]);
        this.algoComponents.setMnemonic(keys[7][languageFlag]);
        this.animGenerate.setMnemonic(keys[8][languageFlag]);
        this.animDelete.setMnemonic(keys[9][languageFlag]);
        this.animView.setMnemonic(keys[10][languageFlag]);*/
        this.setGraphTyp.setMnemonic(keys[11][languageFlag]);

        // Ausrichten der deutschen Mnemonics Markierungen
        if (languageFlag == LANGUAGE_GERMAN) {
            this.sysLoadGraph.setDisplayedMnemonicIndex(8);
            //this.algoComponents.setDisplayedMnemonicIndex(13);
            //this.animView.setDisplayedMnemonicIndex(15);
        }
    }

/*+SYSTEM+MENU+ENTRYS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Reference to the system menu. */
    private JMenu system;

    /** Reference to system menu - save graph. */
    JMenuItem sysSaveGraph;

    /** Reference to system menu - load graph. */
    private JMenuItem sysLoadGraph;

    /** Reference to system menu - transfer graph. */
    JMenuItem sysTransfer;

    /** Reference to system menu - quit program. */
    private JMenuItem sysExit;

    /** (menu construction method)<br>
      * Creates the system menu.
      *
      * @return     System menu
      */
    private JMenu createSystemMenu() {
        this.system = new JMenu();

        this.system.addMenuListener(new MenuListener() {
            public void menuSelected(final MenuEvent e) {
                boolean flag = ((mainclass.getGraph() != null)
                               && (mainclass.getGraph()
                                            .getNumberOfNodes() > 0));

                sysSaveGraph.setEnabled(flag);
                sysTransfer.setEnabled(flag);
            }

            public void menuCanceled(final MenuEvent e) {
            }

            public void menuDeselected(final MenuEvent e) {
            }
        });

        // Laden einer Graph File
        this.sysLoadGraph = new JMenuItem();
        this.sysLoadGraph.setMnemonic('L');
        this.sysLoadGraph.addActionListener(new ActionListener() {

            /** System Menü Eintrag der für das Laden eines Graphen
              * zuständig ist.
              *
              * @param e      ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                ((CreateGraphPanel) mainclass.getPanel(
                		PanelManager.PANEL_CREATEGRAPH)).loadFile();
            }
        });

        // Speichern einer Graph File
        this.sysSaveGraph = new JMenuItem();
        this.sysSaveGraph.setMnemonic('S');
        this.sysSaveGraph.addActionListener(new ActionListener() {

            /** System Menü Eintrag der für das Speichern eines Graphen
              * zuständig ist.
              *
              * @param e    ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                ((CreateGraphPanel) mainclass.getPanel(
                		PanelManager.PANEL_CREATEGRAPH)).saveFile();
            }
        });

        // Transferieren eines Graphens in GraphScript Notation
        this.sysTransfer = new JMenuItem();
        this.sysTransfer.setMnemonic('T');
        this.sysTransfer.addActionListener(new ActionListener() {

            /** System Menü Eintrag der f+r das Speichern eines Graphen
              * zuständig ist.
              *
              * @param e      ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                if (mainclass.showUserMessage(
                                MessageHandler.MESSAGE_DATA_MAY_BE_LOST)) {

                    mainclass.getGraphDatabase().transfer();
                }
            }
        });

        // Beenden des Programms
        this.sysExit = new JMenuItem();
        this.sysExit.setMnemonic('E');
        this.sysExit.setAccelerator(KeyStroke.getKeyStroke(
                                    KeyEvent.VK_Q, ActionEvent.ALT_MASK));

        sysExit.addActionListener(new ActionListener() {

            /** Beendert die Anwendung.
              *
              * @param e    ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                if (mainclass.showUserMessage(
                                    MessageHandler.MESSAGE_SURE_TO_EXIT)) {
                    System.exit(0);
                }
            }
        });

        // Erstellen der Menü Einträge
        this.system.add(this.sysLoadGraph);
        this.system.add(this.sysSaveGraph);
        this.system.addSeparator();
        this.system.add(this.sysTransfer);
        this.system.addSeparator();
        this.system.add(this.sysExit);

        return this.system;
    }

/*+VIEW+MENU+ENTRYS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Reference to the view menu. */
    private JMenu panels;

    /** Reference to view menu - algorithms. */
//    private JMenuItem panAlgos;

    /** Reference to view menu - graph attributes. */
    private JMenuItem panAttribute;

    /** Reference to view menu - kanten panel. */
    private JMenuItem panGraphen;

    /** Reference to view menu - matrix panel. */
    private JMenuItem panMatrix;

    /** Reference to view menu - graph script panel. */
//    private JMenuItem panSkript;

    /** (menu construction method)<br>
      * Creates the view menu.
      *
      * @return     View menu
      */
    private JMenu createPanelMenu() {
        this.panels = new JMenu();

        // Ansicht Preview Panel + CreateGraph Panel
        this.panAttribute = new JMenuItem();
        this.panAttribute.setMnemonic(KeyEvent.VK_1);
        this.panAttribute.setAccelerator(KeyStroke.getKeyStroke(
                                         KeyEvent.VK_1, ActionEvent.ALT_MASK));

        this.panAttribute.addActionListener(new ActionListener() {

            /** Macht die entsprechenden Panels sichtbar.
              *
              * @param e    ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                mainclass.setTopLevelPanel(new int[] {0, 0});
            }
        });

        // Ansicht Preview Panel + Kanten Panel
        this.panGraphen = new JMenuItem();
        this.panGraphen.setMnemonic(KeyEvent.VK_2);
        this.panGraphen.setAccelerator(KeyStroke.getKeyStroke(
                                       KeyEvent.VK_2, ActionEvent.ALT_MASK));
        this.panGraphen.addActionListener(new ActionListener() {

            /** Macht die entsprechenden Panels sichtbar.
              *
              * @param e    ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                mainclass.setTopLevelPanel(new int[] {1});
            }
        });

        // Ansicht Preview Panel + Matrix Panel
        this.panMatrix = new JMenuItem();
        this.panMatrix.setMnemonic(KeyEvent.VK_3);
        this.panMatrix.setAccelerator(KeyStroke.getKeyStroke(
                                       KeyEvent.VK_3, ActionEvent.ALT_MASK));
        this.panMatrix.addActionListener(new ActionListener() {

            /** Macht die entsprechenden Panels sichtbar
              *
              * @param e    ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                mainclass.setTopLevelPanel(new int[] {2});
            }
        });

        /*// Ansicht GraphScript Panel
        this.panSkript = new JMenuItem();
        this.panSkript = new JMenuItem();
        this.panSkript.setMnemonic(KeyEvent.VK_4);
        this.panSkript.setAccelerator(KeyStroke.getKeyStroke(
                                       KeyEvent.VK_4, ActionEvent.ALT_MASK));
        this.panSkript.addActionListener(new ActionListener() {

            *//** Macht die entsprechenden Panels sichtbar.
              *
              * @param e    ActionEvent
              *//*
            public void actionPerformed(final ActionEvent e) {
                mainclass.setTopLevelPanel(new int[] {1});
            }
        });*/

       /* // Ansicht Animations Panel
        this.panAlgos = new JMenuItem();
        this.panAlgos.setMnemonic(KeyEvent.VK_5);
        this.panAlgos.setAccelerator(KeyStroke.getKeyStroke(
                                     KeyEvent.VK_5, ActionEvent.ALT_MASK));
        this.panAlgos.addActionListener(new ActionListener() {

            *//** Macht die entsprechenden Panels sichtbar.
              *
              * @param e    ActionEvent
              *//*
            public void actionPerformed(final ActionEvent e) {
                mainclass.setTopLevelPanel(new int[] {2});
            }
        });*/

        // Erstellen der Menü Einträge
        this.panels.add(this.panAttribute);
        this.panels.add(this.panGraphen);
        this.panels.add(this.panMatrix);
        //this.panels.addSeparator();
        //this.panels.add(this.panSkript);
        //this.panels.addSeparator();
        //this.panels.add(this.panAlgos);

        return this.panels;
    }

/*+VIEW+SETUP+MENU++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Reference to the setup menu. */
    private JMenu setup;

    /** Reference to the setup - language menu. */
    private JMenu language;

    /** Reference to the setup - transfer menu. */
    private JMenu transfer;

    /** Reference to the setup - popups menu. */
    private JMenu popups;

    /** Reference to setup - popup - error messages. */
    JCheckBoxMenuItem setErrors;

    /** Reference to setup - popup - hint messages. */
    JCheckBoxMenuItem setHints;

    /** Reference to setup - popup - warning messages. */
    JCheckBoxMenuItem setWarnings;

    /** Reference to setup - color settings. */
    private JMenuItem setColors;

    /** Reference to setup - graphtyp settings. */
    private JMenuItem setGraphTyp;

    /** (menu construction method)<br>
      * Creates the submenu to select the GUI's language.
      *
      * @return     Language submenu
      */
    private JMenu createLanguageMenu() {
        this.language = new JMenu();
        final JRadioButtonMenuItem langGerman
                            = new JRadioButtonMenuItem("Deutsch");
        final JRadioButtonMenuItem langEnglish
                            = new JRadioButtonMenuItem("English");

        boolean langFlag = (mainclass.getLanguageSettings() == LANGUAGE_GERMAN);
        langGerman.setSelected(langFlag);
        langEnglish.setSelected(!langFlag);

        language.add(langGerman);
        language.add(langEnglish);

        ActionListener languageListener = new ActionListener() {

            /** Ändert die ausgewählte Sprache.
              *
              * @param e    ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                if (e.getSource() == langEnglish) {
                    setLanguage(langGerman.isSelected());
                } else if (e.getSource() == langGerman) {
                    setLanguage(!langEnglish.isSelected());
                }
            }

            /** Setzt den Status der Auswahl Componenten und
              * ruft die Methoden zur Anpassung der SprachEinstellungen
              * auf.
              *
              * @param sel      selectionFlag
              */
            private void setLanguage(final boolean sel) {
                langEnglish.setSelected(sel);
                langGerman.setSelected(!sel);

                if (langEnglish.isSelected()) {
                    mainclass.changeLanguageSettings(LANGUAGE_ENGLISH);
                } else {
                    mainclass.changeLanguageSettings(LANGUAGE_GERMAN);
                }
            }
        };

        langEnglish.addActionListener(languageListener);
        langGerman.addActionListener(languageListener);
        return language;
    }

    /** (menu construction method)<br>
      * Creates the submenu to select the transfer mode.
      *
      * @return     Transfer submenu
      */
    private JMenu createTransferMenu() {
        this.transfer = new JMenu("Transfer");
        this.transfer.setMnemonic('T');

        final JRadioButtonMenuItem saveAsGraph
                            = new JRadioButtonMenuItem("Graph Mode");
        final JRadioButtonMenuItem saveAsMatrix
                            = new JRadioButtonMenuItem("Matrix Mode");

        saveAsGraph.setSelected(true);
        saveAsMatrix.setSelected(false);

        transfer.add(saveAsGraph);
        transfer.add(saveAsMatrix);

        ActionListener transferListener = new ActionListener() {

            /** Ändert den Transfer Modus.
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                if (e.getSource() == saveAsGraph) {
                    setTransferMode(saveAsMatrix.isSelected());
                } else if (e.getSource() == saveAsMatrix) {
                    setTransferMode(!saveAsGraph.isSelected());
                }
            }

            /** Setzt den Status der Auswahl Componenten und
              * passt den Transfer Modus an.
              *
              * @param sel      selectionFlag
              */
            private void setTransferMode(final boolean sel) {
                saveAsGraph.setSelected(sel);
                saveAsMatrix.setSelected(!sel);

                if (sel) {
                    mainclass.setTransferMode(
                            GraphScriptWriter.WRITE_AS_GRAPH);
                } else {
                    mainclass.setTransferMode(
                            GraphScriptWriter.WRITE_AS_MATRIX);
                }
            }
        };

        saveAsGraph.addActionListener(transferListener);
        saveAsMatrix.addActionListener(transferListener);

        return transfer;
    }

    /** (menu construction method)<br>
      * Creates the submenu to en- and disable popup messages.
      *
      * @return     Popup submenu
      */
    private JMenu createPopUpMenu() {
        this.popups = new JMenu();
        this.popups.setMnemonic('P');

        this.setHints = new JCheckBoxMenuItem("Warnungen", true);
        this.setHints.addActionListener(new ActionListener() {

            /** Aktiviert oder deaktiviert die Hinweis Meldungen.
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                mainclass.setHintsEnabeld(0, setHints.isSelected());
            }
        });

        this.setWarnings = new JCheckBoxMenuItem("Parser Meldungen", true);
        this.setWarnings.addActionListener(new ActionListener() {

            /** Aktiviert oder deaktiviert die Parser Fehler Meldungen.
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                mainclass.setHintsEnabeld(1, setWarnings.isSelected());
            }
        });

        this.setErrors = new JCheckBoxMenuItem("Fehlermeldungen", true);
        this.setErrors.addActionListener(new ActionListener() {

            /** Aktiviert oder deaktiviert die Fehler Meldungen.
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                mainclass.setHintsEnabeld(2, setErrors.isSelected());
            }
        });

        // Erstellend er Menü Einträge
        this.popups.add(this.setHints);
        this.popups.add(this.setWarnings);
        this.popups.add(this.setErrors);

        return popups;
    }

    /** (menu construction method)<br>
      * Creates the setup menu.
      *
      * @return     Setup menu
      */
    private JMenu createSetupMenu() {
        this.setup = new JMenu();

        // Erzeugt die UnterSetup Eintr?gen
        this.setup.add(createLanguageMenu());
        this.setup.add(createPopUpMenu());
        this.setup.add(createTransferMenu());

        // Typ Einsetllungen des Graphen
        this.setGraphTyp = new JMenuItem();
        this.setGraphTyp.setMnemonic(KeyEvent.VK_6);
        this.setGraphTyp.setAccelerator(KeyStroke.getKeyStroke(
                                       KeyEvent.VK_6, ActionEvent.ALT_MASK));

        this.setGraphTyp.addActionListener(new ActionListener() {

            /** öffnet den ColorChooser Dialog.
              *
              * @param e    ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                mainclass.getGraphDatabase().showGraphTypDialog();
            }
        });

        this.setup.add(setGraphTyp);

        // Farbeinstellungen des Graphen
        this.setColors = new JMenuItem();
        this.setColors.setMnemonic(KeyEvent.VK_7);
        this.setColors.setAccelerator(KeyStroke.getKeyStroke(
                                      KeyEvent.VK_7, ActionEvent.ALT_MASK));
        this.setColors.addActionListener(new ActionListener() {

            /** öffnet den ColorChooser Dialog.
              *
              * @param e    ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                new ColorChooser(mainclass).setVisible(true);
            }
        });

        this.setup.add(setColors);

        return this.setup;
    }

/*+ANIMATION+MENU+ENTRYS++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Reference to the animation - algorithm menu. */
   /* private JMenu algorithmus;

    *//** Reference to the animation - subcomponents menu. *//*
    private JMenu algoComponents;

    *//** Reference to animation - subcomponents - graph component. *//*
    private JMenuItem algoCompGraph;

    *//** Reference to animation - subcomponents - matrix component. *//*
    private JMenuItem algoCompMatrix;

    *//** Reference to the animation menu. *//*
    private JMenu animation;

    *//** Reference to animation - delete animation components. *//*
    private JMenuItem animDelete;

    *//** Reference to animation - generate animation components. *//*
    private JMenuItem animGenerate;

    *//** Reference to animation - save animation code. *//*
    private JMenuItem animSave;

    *//** Reference to animation - view animation code. *//*
    private JMenuItem animView;

    *//** (menu construcion method)<br>
      * Creates the algorithm menu.
      *
      * @return     Algorithm menu
      *//*
    private JMenu createAlgoMenu() {
        this.algorithmus = new JMenu();
        return this.algorithmus;
    }

    *//** (menu construction method)<br>
      * Creates the algo component menu.
      *
      * @return     Algo component menu
      *//*
    private JMenu createAlgoComponentMenu() {
        this.algoComponents = new JMenu();

        // Graph Komponente
        this.algoCompGraph = new JCheckBoxMenuItem("Graph");
        this.algoCompGraph.setSelected(true);

        this.algoCompGraph.addActionListener(new ActionListener() {

            *//** Aktiviert/Deaktiviert die Graph Animations Komponente.
              *
              * @param e    ActionEvent
              *//*
            public void actionPerformed(final ActionEvent e) {
                if (algoCompGraph.isSelected()) {
                    mainclass.getModel()
                             .setGraphComponentState(GAModel
                                                     .COMPONENT_INVALID);
                } else {
                    mainclass.getModel()
                             .setGraphComponentState(GAModel
                                                     .COMPONENT_DISABLED);
                }
            }
        });

        // Matrix Komponente
        this.algoCompMatrix = new JCheckBoxMenuItem("Matrix");
        this.algoCompMatrix.setSelected(true);

        this.algoCompMatrix.addActionListener(new ActionListener() {

            *//** Aktiviert/Deaktiviert die Matrix Animations Komponente.
              *
              * @param e    ActionEvent
              *//*
            public void actionPerformed(final ActionEvent e) {
                if (algoCompMatrix.isSelected()) {
                    mainclass.getModel()
                             .setMatrixComponentState(GAModel
                                                      .COMPONENT_INVALID);
                } else {
                    mainclass.getModel()
                             .setMatrixComponentState(GAModel
                                                      .COMPONENT_DISABLED);
                }
            }
        });

        // Erzeugt die Menü Einträge
        this.algoComponents.add(this.algoCompGraph);
        this.algoComponents.addSeparator();
        this.algoComponents.add(this.algoCompMatrix);
        this.algoComponents.addSeparator();

        return this.algoComponents;
    }

    *//** (menu construction method)<br>
      * Creates the animation menu.
      *
      * @return     Animation menu
      *//*
    private JMenu createAnimationMenu() {
        this.animation = new JMenu("Animation");

        this.animation.addMenuListener(new MenuListener() {
            public void menuSelected(final MenuEvent e) {
                boolean graphFlag = ((mainclass.getGraph() != null)
                        && (mainclass.getGraph().getNumberOfNodes() > 0));

                GAModel gam = mainclass.getModel();

                int[] flag = new int[] {gam.getGraphComponentState(),
                                        gam.getMatrixComponentState(),
                                        gam.getAlgorithmComponentState()};

                boolean genFlag = false;
                int i = 0;
                do {
                    genFlag = (flag[i++] == GAModel.COMPONENT_VALID);
                } while (!genFlag && i < 3);

                animDelete.setEnabled(genFlag);
                animSave.setEnabled(genFlag);

                i = 0;
                genFlag = false;
                while (!genFlag && i < algorithmus.getItemCount()) {
                    JRadioButtonMenuItem jrbe =
                         (JRadioButtonMenuItem) algorithmus.getItem(i++);
                    genFlag = jrbe.isSelected();
                }

                animGenerate.setEnabled(graphFlag && genFlag);
            }

            public void menuCanceled(final MenuEvent e) {
            }

            public void menuDeselected(final MenuEvent e) {
            }
        });

        // Animation genierieren
        this.animGenerate = new JMenuItem();
        this.animGenerate.addActionListener(new ActionListener() {

            *//** Generiert alle ausgew?hlten Animations Komponenten.
              *
              * @param e    ActionEvent
              *//*
            public void actionPerformed(final ActionEvent e) {
                mainclass.getModel().generateAllAnimations();
            }
        });

        // Animation löschen
        this.animDelete = new JMenuItem();
        this.animDelete.addActionListener(new ActionListener() {

            *//** Löscht alle bereits genierten Komponenten.
              *
              * @param e    ActionEvent
              *//*
            public void actionPerformed(final ActionEvent e) {
                mainclass.getModel().deleteAllAnimations();
            }
        });

        // Animations Code ansehen
        this.animView = new JMenuItem();
        this.animView.addActionListener(new ActionListener() {

            *//** Öffnet ein Fenster indem man den generierten Animations Code
              * anschauen kann.
              *
              * @param e    ActionEvent
              *//*
            public void actionPerformed(final ActionEvent e) {
                //mainclass.getModel().showCodePreview();
            }
        });

        // Animations Code speichern
        this.animSave = new JMenuItem();
        this.animSave.setMnemonic('S');
        this.animSave.addActionListener(new ActionListener() {

            *//** Speichert den generierten AnimationsCode in eine
              * ausgewählte Datei.
              *
              * @param e    ActionEvent
              *//*
            public void actionPerformed(final ActionEvent e) {
               // mainclass.getModel().saveAnimationCode();
            }
        });

        // Erstellt die Menü Einträge
        this.animation.add(createAlgoMenu());
        this.animation.addSeparator();
        this.animation.add(createAlgoComponentMenu());
        this.animation.addSeparator();
        this.animation.add(this.animGenerate);
        this.animation.add(this.animDelete);
        this.animation.addSeparator();
        this.animation.add(this.animView);
        this.animation.add(this.animSave);

        return this.animation;
    }*/

/*+ABOUT+MENU+ENTRYS++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** Reference to the help menu. */
    private JMenu help;

    /** Reference to help - about. */
    private JMenuItem helpAbout;

    //private JMenuItem helpLog;

    /** (menu construction method)<br>
      * Creates the about menu.
      *
      * @return     About menu
      */
    private JMenu createAboutMenu() {
        this.help = new JMenu();
        this.help.setMnemonic('H');

        /*
        this.helpLog = new JMenuItem();
        this.helpLog.addActionListener(new ActionListener() {

            /** ?ffnet das Panel mit den Programm Informationen
              *
              * @param e        ActionEvent
              *
            public void actionPerformed(final ActionEvent e) {
                try {
                    new LogFileDialog(this.getClass().getResource("LogFiles/log.html")).setVisible(true);
                } catch (IOException ioe) {
                    mainclass.showUserMessage(-1);
                }
            }
        });*/

        this.helpAbout = new JMenuItem();
        this.helpAbout.setAccelerator(KeyStroke.getKeyStroke(
                                      KeyEvent.VK_8, ActionEvent.ALT_MASK));
        this.helpAbout.addActionListener(new ActionListener() {

            /** Öffnet das Panel mit den Programm Informationen
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                MyDia autor = new MyDia();
                autor.setVisible(true);
            }
        });

        /*this.help.add(helpLog);
        this.system.addSeparator();*/
        this.help.add(helpAbout);

        return this.help;
    }

/*+INTERNAL+CONTROLL+METHODS++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Adds the given component menu items to the menubar.
      *
      * @param compItems    Array with JCheckBoxMenuItems
      */
    /*protected void addAlgorithmusComponentMenuEntrys(
                                    final JCheckBoxMenuItem[] compItems) {

        for (int i = 0; i < compItems.length; i++) {
            this.algoComponents.add(compItems[i]);
        }
    }*/

    /** (internal data method)<br>
      * Creates the algorithm menu entries.
      *
      * @param db       Algorithm database
      */
    /*protected void createAlgorithmMenu(final AlgoDataBase db) {

        // Entfernen aller vorhandenen Algorithmus Eintr?ge
        int size = algorithmus.getItemCount();
        for (int i = size; i > 0;) {
            algorithmus.remove(--i);
        }

        size = db.size();
        for (int i = 0; i < size; i++) {
            JRadioButtonMenuItem algoItem =
                new JRadioButtonMenuItem(db.get(i).getTitle());

            final int pos = i;

            algoItem.addActionListener(new ActionListener() {

                *//** ActionListener der die Auswahl der Algorithmen ?ber
                  * das Men? steuert
                  *
                  * @param e      ActionEvent
                  *//*
                public void actionPerformed(final ActionEvent e) {
                    JRadioButtonMenuItem jrbmi =
                                (JRadioButtonMenuItem) e.getSource();

                    // Abw?hlen nicht m?glich
                    if (!jrbmi.isSelected()) {
                        jrbmi.setSelected(true);
                        return;

                    // Setzen des Choosers im GraphAlgoPanel auf den Algorithmus
                    } else {
                        mainclass.getModel().selectAlgorithmus(pos);
                    }

                    // Abw?hlen der anderen Algorithmen Eintr?ge
                    for (int i = 0; i < algorithmus.getItemCount(); i++) {
                        JRadioButtonMenuItem jrbe =
                            (JRadioButtonMenuItem) algorithmus.getItem(i);
                        if (jrbmi != jrbe) {
                            jrbe.setSelected(false);
                        }
                    }
                }
            });

            algorithmus.add(algoItem);
        }
    }

    *//** (internal data method)<br>
      * Removes all not standard component menu items from the menubar.
      *//*
    protected void removeAlgorithmusComponentMenuEntrys() {
        for (int i = 4; i < algoComponents.getItemCount();) {
            algoComponents.remove(i);
        }
    }

    *//** (internal data method)<br>
      * Method to select/unselect the specified graph algorithm.
      *
      * @param pos          Id of the graph algorithm that
      *                     should be selected
      *//*
    protected void selectAlgorithmMenuEntry(final int pos) {
        for (int i = 0; i < algorithmus.getItemCount(); i++) {
            JRadioButtonMenuItem jrbe =
                (JRadioButtonMenuItem) algorithmus.getItem(i);
            jrbe.setSelected(i == pos - 1);
        }
    }

    *//** (internal data method)<br>
      * Method to en/disable the graph menu item.
      *
      * @param enabled      Enabled flag
      *//*
    protected void setGraphMenuItemEnabled(final boolean enabled) {
        algoCompGraph.setSelected(enabled);
    }

    *//** (internal data method)<br>
      * Method to en/disable the matrix menu item.
      *
      * @param enabled      Enabled flag
      *//*
    protected void setMatrixMenuItemEnabled(final boolean enabled) {
        algoCompMatrix.setSelected(enabled);
    }*/
}
