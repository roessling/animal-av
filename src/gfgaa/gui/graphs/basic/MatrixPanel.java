package gfgaa.gui.graphs.basic;

import gfgaa.gui.EditGraphPanel;
import gfgaa.gui.GraphAlgController;
import gfgaa.gui.MessageHandler;
import gfgaa.gui.components.IntegerTextFieldEx;
import gfgaa.gui.components.SComponent;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.graphs.AbstractEdge;
import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.graphs.AbstractNode;
import gfgaa.gui.graphs.MatrixPanelInterface;
import gfgaa.gui.others.PanelManager;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/** This Panel shows the Adjacency Matrix of the Graph and
  * allows the user to add new edges, remove old edges or
  * to change the weight of edges.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public final class MatrixPanel extends SPanel
                               implements MatrixPanelInterface {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1138675616899090047L;

	/** Reference to the projects mainclass. */
    final GraphAlgController mainclass;

    /** Number of nodes the graph contains. */
    int anzKnoten;

    /** Matrix Input Fields Array. */
    IntegerTextFieldEx[][] weightFields;

    /** Tags from the nodes in alphabetic order. */
    char[] cTab;

    /** The panels headline label. */
    private JLabel sup;

    /** Button to resize the window. */
    JButton sizeButton;

    /** Button to applay the matrix settings to the graph. */
    private JButton applyChanges;

    /** Button to reset the matrix data. */
    private JButton reset;

    /** (Constructor)<br>
      * Constructs the MatrixPanel and all his components.
      *
      * @param mainclass            Reference to projects mainclass
      */
    public MatrixPanel(final GraphAlgController mainclass) {
        this.mainclass = mainclass;
        this.mainclass.addPanel(PanelManager.PANEL_MATRIX, this);

        this.anzKnoten = mainclass.getGraph().maxsize();
        this.weightFields = new IntegerTextFieldEx[anzKnoten]
                                                  [anzKnoten];
        this.cTab = new char[anzKnoten];

        this.setLayout(null);

        sup = new JLabel();
        sup.setFont(new Font("Serif", Font.BOLD, 18));
        sup.setBounds(90, 0, 150, 25);

        add(new SComponent(sup,
                           new String[] {"Adjazenzmatrix",
                                         "Adjacencymatrix"}));

        add(sup);
        add(createResetButton());
        add(createApplyButton());
        add(createSizeButton());

        for (int a = 0; a < anzKnoten; a++) {
            for (int b = 0; b < anzKnoten; b++) {
                this.weightFields[a][b] = new IntegerTextFieldEx(0, 99);
                this.add(weightFields[a][b]);
            }
        }
        this.setMatrixVisibility();

        this.addAncestorListener(new AncestorListener() {

            public void ancestorMoved(final AncestorEvent e) {
            }

            /** Wenn das Panel sichtbar wird, wird der Split Balken
              * bewegbar.
              *
              * @param e    AncestorEvent
              */
            public void ancestorAdded(final AncestorEvent e) {
                EditGraphPanel sp = (EditGraphPanel) mainclass
                                                .getPanel(PanelManager
                                                            .PANEL_EDITGRAPH);
                sp.setStaticSplitEnabled(true);
                refreshPanelComponents();
            }

            /** Wenn das Panel wieder in den Hintergrund geht, wird der
              * Split Balken disabelt und das Fenster auf Standard Größe
              * gesetzt.
              *
              * @param e    AncestorEvent
              */
            public void ancestorRemoved(final AncestorEvent e) {
                EditGraphPanel sp = (EditGraphPanel) mainclass
                                                .getPanel(PanelManager
                                                            .PANEL_EDITGRAPH);
                sizeButton.setText("<<");

                String[] text = new String[]
                                    {"Ändert die Größe des Matrix Panels.",
                                     "Changes the size of the matrix panel."};
                sizeButton.setToolTipText(
                            text[mainclass.getLanguageSettings()]);

                sp.resetSplit();
                sp.setStaticSplitEnabled(false);
            }
        });

        changeLanguageSettings(mainclass.getLanguageSettings());
    }

    /** (panel construction method)<br>
      * Creates the apply button.
      *
      * @return     Apply button
      */
    private JButton createApplyButton() {
        applyChanges = new JButton();
        applyChanges.setBounds(15, 420, 100, 25);

        add(new SComponent(applyChanges,
                new String[] {"Anpassen",
                              "Apply"},
                new String[] {"Passt den Graphen anhand der Matrix-Daten an.",
                              "Applys the matrix settings to the graph."}));

        applyChanges.addActionListener(new ActionListener() {

            /** Behandelt den Button Druck und vergleicht die Matrix
              * Einstellungen mit den Kanten des Graphens
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                if (mainclass.showUserMessage(MessageHandler
                                                 .MESSAGE_DATA_MAY_BE_LOST)) {


                    AbstractGraph graph = mainclass.getGraph();
                    AbstractNode nodeA, nodeB;
                    AbstractEdge edgeAB;
                    int a, b, val;

                    for (a = 0; a < anzKnoten; a++) {

                        nodeB = graph.getNode(cTab[a]);
                        for (b = 0; b < anzKnoten; b++) {

                            val = weightFields[a][b].getValue().intValue();
                            nodeA = graph.getNode(cTab[b]);

                            edgeAB = nodeA.getEdgeTo(nodeB);
                            // Wenn eine Kante exisitiert ...
                            if (edgeAB != null) {
                                if (val > 0) {
                                    // ... setze Gewicht der Kante neu
                                    edgeAB.setWeight(val);
                                } else {
                                    // ... entferne vorhanden Kante
                                    graph.removeEdge(edgeAB);
                                }
                            } else if (val > 0) {

                                // Wenn keine Kante exisitiert erschaffe
                                // eine neue Kante
                                edgeAB = new Edge(nodeA, nodeB, val);
                                nodeA.addEdge(edgeAB);
                                nodeB.addAgainstEdge(edgeAB);
                            }
                        }
                    }
                    mainclass.repaint();
                }
            }
        });

        return applyChanges;
    }

    /** (panel construction method)<br>
      * Creates the reset button.
      *
      * @return     Reset button
      */
    private JButton createResetButton() {
        reset = new JButton();
        reset.setBounds(147, 420, 100, 25);

        add(new SComponent(reset,
                new String[] {"Reset",
                              "Reset"},
                new String[] {"Lädt die aktuellen Matrix-Daten erneut.",
                              "Resets the matrix settings."}));

        reset.addActionListener(new ActionListener() {

            /** Lädt die aktuellen Kantenwerte in die Matrix ein
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                if (mainclass.showUserMessage(MessageHandler
                                                 .MESSAGE_DATA_MAY_BE_LOST)) {
                    setCurrentWeights();
                }
            }
        });

        return reset;
    }

    /** (panel construction method)<br>
      * Creates the size button.
      *
      * @return     Size button
      */
    private JButton createSizeButton() {
        sizeButton = new JButton("<<");
        sizeButton.setBounds(15, 420, 50, 25);

        sizeButton.addActionListener(new ActionListener() {

            /** Behandelt den Druck des Resize Buttons und minimiert
              * oder maximiert das Panel
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                EditGraphPanel sp = (EditGraphPanel) mainclass
                                         .getPanel(PanelManager
                                                       .PANEL_EDITGRAPH);
                if (sizeButton.getText().equals("<<")) {
                    sizeButton.setText(">>");
                    sp.setMaxSplit();
                } else {
                    sizeButton.setText("<<");
                    sp.resetSplit();
                }
            }
        });

        return sizeButton;
    }

   /** (panel display method)<br>
     * Paints the panel and his components.
     *
     * @param g        Graphical component of this panel
     */
   public void paint(final Graphics g) {
       setMatrixVisibility();
       Image offImage = createImage(this.getSize().width,
                                    this.getSize().height);
       Graphics offGraphics = offImage.getGraphics();

       AbstractGraph aktuGraph = mainclass.getGraph();
       offGraphics.setFont(new Font("Serif", Font.BOLD, 15));

       int size = getSize().width / 2;
       int xpos = size - 10 * anzKnoten + 10;
       int xpos2 = size - 10 * anzKnoten - 10;

       // Setzt die Locations der Button und der Überschrift
       sup.setLocation(size - 60, 0);
       reset.setLocation(size + 29, 420);
       applyChanges.setLocation(size - 75, 420);
       sizeButton.setLocation(size - 129, 420);

       AbstractNode node;
       // Malt die Matrix Zeilen und Spalten Beschriftungen neu
       for (int a = 0; a < aktuGraph.getNumberOfNodes(); a++) {
           node = aktuGraph.getNode(a);
           cTab[a] = node.getTag();

           offGraphics.drawString("" + node.getTag(), xpos + 20 * a, 40);
           offGraphics.drawString("" + node.getTag(), xpos2, 60 + 20 * a);
       }

       super.printComponents(offGraphics);
       g.drawImage(offImage, 0, 0, this);
   }

   /** (panel display method)<br>
     * Refreshs the data of the components of the panel.
     */
   public void refreshPanelComponents() {
       AbstractGraph aktuGraph = mainclass.getGraph();

       AbstractNode node;
       for (int a = 0; a < aktuGraph.getNumberOfNodes(); a++) {
           node = aktuGraph.getNode(a);
           cTab[a] = node.getTag();
       }

       setMatrixVisibility();
       setCurrentWeights();

       String[] text = new String[]
                           {"Ändert die Größe des Matrix Panels.",
                            "Changes the size of the matrix panel."};
       sizeButton.setToolTipText(
                       text[mainclass.getLanguageSettings()]);
   }

   /** (panel display method)<br>
     * Sets the current Weights into the matrix.
     */
   void setCurrentWeights() {
       AbstractGraph currentGraph = mainclass.getGraph();
       AbstractNode node;
       AbstractEdge edge;
       int a, b, anz;
       char cKennung;

       for (a = 0; a < anzKnoten; a++) {
           for (b = 0; b < anzKnoten; b++) {
               weightFields[b][a].setText("0");
           }

           node = currentGraph.getNode(a);
           anz = node.getNumberOfEdges();
           for (b = 0; b < anz; b++) {
               edge = node.getEdge(b);

               cKennung = edge.getTarget().getTag();
               weightFields[getNodeTabPosition(cKennung)][a]
                               .setText("" + edge.getWeight());
           }
       }
   }

   /** (panel display method)<br>
     * Controlls the Locations and Visibility of the matrix TextFields.
     */
   private void setMatrixVisibility() {
       anzKnoten = mainclass.getGraph().getNumberOfNodes();

       int xpos = (getSize().width / 2) - 10 * (anzKnoten) + 8;

       AbstractGraph graph = mainclass.getGraph();

       boolean gerichtet = graph.isDirected();
       int size = graph.maxsize();

       for (int a = 0; a < size; a++) {
           for (int b = 0; b < size; b++) {
               weightFields[a][b].setBounds(xpos + a * 20,
                                            45 + b * 20,
                                            20, 20);
               weightFields[a][b].setVisible(b < anzKnoten
                                             && a < anzKnoten
                                             && (gerichtet
                                             || (!gerichtet && b <= a)));
           }
       }
    }

    /** (internal data method)<br>
      * This method deliver the adjacency matrix to the projects mainclass.
      *
      * @return             The adjacencymatrix of the graph
      */
    public int[][] getAdjacencyMatrix() {
        refreshPanelComponents();

        int anz = mainclass.getGraph().getNumberOfNodes();
        int[][] matrix = new int[anz][anz];

        for (int i = 0; i < anz; i++) {
            for (int j = 0; j < anz; j++) {
                matrix[i][j] = weightFields[i][j].getValue().intValue();
            }
        }

        return matrix;
    }

    /** (internal info method)<br>
      * Returns the position of the given tag in the cTab array.
      *
      * @param cTag         Given tag
      * @return             Position in the cTab array
      */
    private int getNodeTabPosition(final char cTag) {
        for (int a = 0; a < anzKnoten; a++) {
            if (cTab[a] == cTag) {
                return a;
            }
        }
        return -1;
    }
}
