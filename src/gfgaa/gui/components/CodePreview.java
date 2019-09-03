package gfgaa.gui.components;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.others.ProgressDialog;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/** This JDialog shows the generated animalscript code.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public class CodePreview extends JDialog {

    /**
   * 
   */
  private static final long serialVersionUID = -6414656080255443396L;

    /** Panel class<br>
      * The ContentPane of the CodePreview Dialog.
      *
      * @author S. Kulessa
      * @version 0.92
      */
    private class CodePreviewPanel extends SPanel {

        /**
       * 
       */
      private static final long serialVersionUID = 367392582025882490L;

        /** The InformationDialog of the CodePreview Dialog.
          *
          * @author S. Kulessa
          * @version 0.92
          */
        private class InfoDialog extends JDialog {

            /**
           * 
           */
          private static final long serialVersionUID = -7849160552388209578L;

            /** Panel class<br>
              * The Panel of the InformationDialog.
              *
              * @author S. Kulessa
              * @version 0.92
              */
            private class InfoPanel extends SPanel {

              /**
               * 
               */
              private static final long serialVersionUID = -8859557441105896511L;
                private JLabel vecSizeLabel;
                private JLabel pageSizeLabel;
                private JLabel currentPageLabel;

                private JEditorPane vecSize;
                private IntegerTextFieldEx pageSize;
                private IntegerTextFieldEx currentPage;
                private IntegerTextFieldEx maxPage;

                private JButton apply;
                private JButton close;

                public InfoPanel() {
                    this.setLayout(null);

                    createVecSize();
                    createPageSize();
                    createCurrentPage();
                    createButtons();

                    this.changeLanguageSettings(
                            mainclass.getLanguageSettings());
                }

                private void createVecSize() {
                    vecSizeLabel = new JLabel();
                    vecSizeLabel.setBounds(20, 20, 130, 25);

                    this.add(new SComponent(vecSizeLabel,
                                            new String[] {"Datenmenge:",
                                                          "Amount of data:"}));
                    this.add(vecSizeLabel);

                    vecSize = new JEditorPane();
                    vecSize.setBorder(BorderFactory.createEtchedBorder());
                    vecSize.setBounds(150, 20, 90, 25);
                    vecSize.setEditable(false);

                    String ent = " Einträge";
                    if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
                        ent = " entries";
                    }

                    vecSize.setText(sCode.size() + ent);
                    this.add(vecSize);
                }

                private void createPageSize() {
                    pageSizeLabel = new JLabel();
                    pageSizeLabel.setBounds(20, 50, 130, 25);

                    this.add(new SComponent(
                                pageSizeLabel,
                                new String[] {"Aktuelle Seitengröße:",
                                              "Current pagesize:"}));
                    this.add(pageSizeLabel);

                    pageSize = new IntegerTextFieldEx(1000, 50000);
                    pageSize.setBorder(BorderFactory.createEtchedBorder());
                    pageSize.setBounds(150, 50, 90, 25);
                    pageSize.setText("" + pagesize);
                    this.add(pageSize);
                }

                private void createCurrentPage() {
                    currentPageLabel = new JLabel();
                    currentPageLabel.setBounds(20, 80, 130, 25);
                    this.add(new SComponent(currentPageLabel,
                                            new String[] {"Aktuelle Seite:",
                                                          "Current page"}));
                    this.add(currentPageLabel);

                    maxpage = sCode.size() / pagesize + 1;

                    currentPage = new IntegerTextFieldEx(1, maxpage);
                    currentPage.setBounds(150, 80, 40, 25);
                    currentPage.setText("" + pagenumber);
                    this.add(currentPage);

                    maxPage = new IntegerTextFieldEx();
                    maxPage.setBounds(200, 80, 40, 25);
                    maxPage.setEditable(false);
                    maxPage.setText("" + maxpage);
                    this.add(maxPage);
                }

                private void createButtons() {
                    apply = new JButton();
                    apply.setBounds(20, 115, 105, 25);
                    apply.addActionListener(new ActionListener() {
                        public void actionPerformed(final ActionEvent e) {
                            int newPageSize = pageSize.getValue().intValue();
                            if (pagesize != newPageSize) {
                                pagesize = newPageSize;

                                startIndex = (startIndex / pagesize) * pagesize;
                                endIndex = startIndex + pagesize;

                                loadScriptCode();

                                pagenumber = (startIndex / pagesize) + 1;
                            } else {
                                int newPage = currentPage.getValue().intValue();
                                if (newPage != pagenumber) {
                                    startIndex = pagesize * (newPage - 1);
                                    endIndex = startIndex + pagesize;
                                    loadScriptCode();
                                    pagenumber = newPage;
                                }
                            }
                            refreshPanelComponents();
                        }
                    });

                    this.add(new SComponent(apply,
                                            new String[] {"Anpassen",
                                                          "Apply"}));
                    this.add(apply);

                    close = new JButton("Close");
                    close.setBounds(135, 115, 105, 25);
                    close.addActionListener(new ActionListener() {
                       public void actionPerformed(final ActionEvent e) {
                           close();
                       }
                    });
                    this.add(new SComponent(close,
                                            new String[] {"Schließen",
                                                          "Close"}));
                    this.add(close);
                }

                public void refreshPanelComponents() {
                    pageSize.setText("" + pagesize);

                    maxpage = sCode.size() / pagesize + 1;
                    maxPage.setText("" + maxpage);

                    currentPage.setMinMaxValues(1, maxpage);
                    currentPage.setText("" + pagenumber);
                }

                public void paint(final Graphics g) {
                    Dimension size = this.getSize();

                    int[] pos = new int[6];
                    pos[0] = (size.width - 220) / 2;
                    pos[1] = pos[0] + 130;

                    pos[2] = (size.height - 120) / 2;
                    pos[3] = pos[2] + 30;
                    pos[4] = pos[2] + 60;
                    pos[5] = pos[2] + 95;

                    vecSizeLabel.setLocation(pos[0], pos[2]);
                    pageSizeLabel.setLocation(pos[0], pos[3]);
                    currentPageLabel.setLocation(pos[0], pos[4]);

                    vecSize.setLocation(pos[1], pos[2]);
                    pageSize.setLocation(pos[1], pos[3]);
                    currentPage.setLocation(pos[1], pos[4]);
                    maxPage.setLocation(pos[0] + 180, pos[4]);

                    apply.setLocation(pos[0], pos[5]);
                    close.setLocation(pos[0] + 115, pos[5]);

                    super.paint(g);
                }
            }

            public InfoDialog() {
                // Setzt die Eigenschaften des Dialogs
                super(cpv, false);

                String title = "Informationen";
                if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
                    title = "Informations";
                }
                this.setTitle(title);

                this.setSize(250, 170);
                this.setResizable(false);

                // Setzt das Fenster in die Mitte des Bildschirms
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                this.setLocation((d.width - getSize().width) / 2,
                                 (d.height - getSize().height) / 2);

                this.setContentPane(new InfoPanel());
            }

            /** (internal displays method)<br>
              * Sets the dialog visible state to false.
              */
            private void close() {
                this.setVisible(false);
                infoDialog = null;
                this.dispose();
            }

            public void refreshPanelComponents() {
                ((SPanel) this.getContentPane()).refreshPanelComponents();
            }
        }

         class LoadingThread extends Thread {

            /** Reference to the ProgressDialog */
            private ProgressDialog pd;

            /** Stats the loading operation
              */
            public void run() {
                int size = sCode.size();
                if (endIndex > size) {
                    endIndex = size;
                }

                int pageIndex = endIndex % pagesize;
                if (pageIndex == 0) {
                    pageIndex = pagesize;
                }

               // codePane.setText("");

                int oldVal = -1;
                for (int k = startIndex, i = 0; k < endIndex; k++, i++) {
                    //System.out.println(k + " writing " + i);

                    int newVal = (int) ((((i + 1.) / pageIndex) * 100.));
                    if (oldVal != newVal) {
                        pd.setProgress(newVal);
                    }

                   /* try {
                        codePane.insertRegularString(sCode.get(k) + "\n");
                    } catch (BadLocationException ble) {
                        System.out
                                .println("Internal Error - BLE@GraphAlgoPanel");
                        ble.printStackTrace();
                    }*/
                }
                pd.close();
            }

            /** (internal data method) <br>
              * Assigns a ProgressDialog to this Thread
              *
              * @param pd           ProgressDialog
              */
            protected void setProgressDialog(final ProgressDialog pd) {
                this.pd = pd;
            }
        }

        /** JButton to close the dialog */
        private JButton close;

        /** JButton to get informations about the preview */
        private JButton info;

        /** JButton to load the next page */
        private JButton next;

        /** JButton to load the previous page */
        private JButton previous;

        /** Scroll Component for the Syntax Pane */
        private JScrollPane scrollPane;

        /** JEditorPane to display the preview */
        //private SyntaxPane codePane;

        /** Temporary variable - Contains the current size of a page */
        private int pagesize = 10000;

        /** Temporary variable - Contains the number of the current
          *                      shown page */
        private int pagenumber = 1;

        /** Temporary variable - Contains the maximum numbers of pages */
        private int maxpage = 1;

        /** Start index of the current displayed page */
        private int startIndex = 0;

        /** End index of the current displayed page */
        private int endIndex = pagesize;

        /** (constructor)<br>
          * Creates a new CodePreviewPanel.
          */
        public CodePreviewPanel() {
            this.setLayout(null);

           // createCodePane();
            createButtons();

            refreshPanelComponents();
            loadScriptCode();

            this.changeLanguageSettings(mainclass.getLanguageSettings());
        }

        /** (panel construction method)<br>
          * Creates the buttons of this panel.
          */
        private void createButtons() {
            info = new JButton();
            info.setBounds(0, 0, 149, 25);
            info.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    if (infoDialog == null) {
                        infoDialog = new InfoDialog();
                    }
                    infoDialog.setVisible(true);
                }
            });

            this.add(new SComponent(info,
                                    new String[] {"Einstellungen",
                                                  "Settings"}));

            this.add(info);

            previous = new JButton();
            previous.setBounds(150, 0, 149, 25);

            previous.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    if (startIndex > 0) {
                        startIndex -= pagesize;
                        endIndex = startIndex + pagesize;
                        loadScriptCode();
                        pagenumber--;
                    }
                    if (infoDialog != null) {
                        infoDialog.refreshPanelComponents();
                    }
                }
            });

            this.add(new SComponent(previous,
                                    new String[] {"Vorherige Seite",
                                                  "Previous Page"}));
            this.add(previous);

            next = new JButton();
            next.setBounds(300, 0, 149, 25);

            next.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    if (endIndex < sCode.size()) {
                        startIndex += pagesize;
                        endIndex = startIndex + pagesize;
                        loadScriptCode();
                        pagenumber++;
                    }
                    if (infoDialog != null) {
                        infoDialog.refreshPanelComponents();
                    }
                }
            });

            this.add(new SComponent(next,
                                    new String[] {"Nächste Seite",
                                                  "Next Page"}));
            this.add(next);

            close = new JButton();
            close.setBounds(450, 0, 144, 25);

            close.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    close();
                }
            });

            this.add(new SComponent(close,
                                    new String[] {"Schließen",
                                                  "Close"}));
            this.add(close);
        }

        /** (panel construction method)<br>
          * Creates the JEditorPane to display the preview.
          */
      /*  private void createCodePane() {
            // Erzeugt ein Scrollbares Text Panels
            codePane = new SyntaxPane(mainclass);
            codePane.setEditable(true);
            this.add(codePane);

            // Füllen des JEditorPanels mit dem AnimalScript Code
            scrollPane = new JScrollPane(codePane);
            scrollPane.setBounds(0, 26, 594, 450);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane
                                                  .VERTICAL_SCROLLBAR_ALWAYS);
            this.add(scrollPane);
        }*/

        /** (internal display method)<br>
          * Loads the current page into the JEditorPane.
          */
        private void loadScriptCode() {
            LoadingThread load = new LoadingThread();
            ProgressDialog pd = new ProgressDialog(load, mainclass.getGUI());

            load.setProgressDialog(pd);
            pd.setVisible(true);

            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    scrollPane.getVerticalScrollBar().setValue(0);
                }
            });

            refreshPanelComponents();
            repaint();
        }

        /** (panel display method)<br>
          * Draws the components of the panel.
          *
          * @param g         Graphical component of this panel
          */
        public void paint(final Graphics g) {
            update();
            super.paint(g);
        }

        /** (panel control method)<br>
          * Controls the dis- and enabling of the panel buttons.
          */
        public void refreshPanelComponents() {
            previous.setEnabled(startIndex > 0);
            next.setEnabled(endIndex < sCode.size());
        }

        /** (panel display method)<br>
          * Controls the positions of the panel buttons in the panel.
          */
        public void update() {
            int width = this.getSize().width;
            int w = (width - 2) / 4;

            info.setBounds(0, 0, w, 25);
            previous.setBounds(w + 1, 0, w, 25);
            next.setBounds(2 * w + 2, 0, w, 25);
            close.setBounds(3 * w + 3, 0, width - 3 * w - 4, 25);

            scrollPane.setBounds(0, 26, width, this.getHeight() - 26);
        }
    }

    /** Reference to the projects mainclass */
    private GraphAlgController mainclass;

    private CodePreview.CodePreviewPanel.InfoDialog infoDialog;

    /** Vector containing the animalscript code */
    private Vector sCode;

    private CodePreview cpv;

    /** (constructor) <br>
      * Creates and opens the dialog.
      *
      * @param sCode        Vector containing the AnimalScript code
      * @param mainclass    Reference on the projects main class
      */
    public CodePreview(final Vector sCode,
                       final GraphAlgController mainclass) {
        // Setzt die Eigenschaften des Dialogs
        super(mainclass.getGUI(), true);
        this.cpv = this;

        this.mainclass = mainclass;
        this.setTitle("Code Preview");

        this.sCode = sCode;
        this.setSize(600, 500);
        this.setResizable(false);

        // Setzt das Fenster in die Mitte des Bildschirms
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((d.width - getSize().width) / 2,
                         (d.height - getSize().height) / 2);

        // Liefert den Container des Fensters
        this.setContentPane(new CodePreviewPanel());
        this.setVisible(true);
    }

    /** (internal displays method)<br>
      * Sets the dialog visible state to false.
      */
     void close() {
        if (infoDialog != null) {
            infoDialog.setVisible(false);
        }
        this.setVisible(false);
        this.dispose();
    }
}
