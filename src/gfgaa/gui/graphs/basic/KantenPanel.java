package gfgaa.gui.graphs.basic;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.MessageHandler;
import gfgaa.gui.components.IntegerTextFieldEx;
import gfgaa.gui.components.SComponent;
import gfgaa.gui.components.SPanel;
import gfgaa.gui.graphs.AbstractEdge;
import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.graphs.AbstractNode;
import gfgaa.gui.graphs.KantenPanelInterface;
import gfgaa.gui.others.PanelManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/** Panel class - TabedPanel of the EditGraphPanel<br>
  * This panel allows the user to create new or remove old
  * nodes and edges. It also contains the possibility to move
  * nodes or changing the weight of an edge.
  *
  * @author S. Kulessa
  * @version 0.92b
  */
public final class KantenPanel extends SPanel implements KantenPanelInterface {

    /**
	 * 
	 */
	//private static final long serialVersionUID = 2283100811872159078L;

	/**
   * 
   */
  private static final long serialVersionUID = 2283100811872159078L;

  /** Reference to the projects mainclass. */
    GraphAlgController mainclass;

    /** Button to create nodes or to move them. */
    private JButton createNodeButton;

    /** Button to remove a node. */
    private JButton removeNodeButton;

    /** ComboBox to choose the tag of a node. */
    JComboBox nodeTagBox;

    /** TextField to enter the x position of a node. */
    IntegerTextFieldEx xPos;

    /** TextField to enter the y position of a node. */
    IntegerTextFieldEx yPos;

    /** Button to create edges or to set the weight of an edge. */
    private JButton createEdgeButton;

    /** Button to remove an edge. */
    private JButton removeEdgeButton;

    /** ComboBox to choose the start node. */
    JComboBox startNodeBox;

    /** ComboBox to choose the end node. */
    JComboBox endNodeBox;

    /** Weight Label. */
    private JLabel weightEdgeLab;

    /** TextField to enter the weight of an edge. */
    IntegerTextFieldEx weightEdgeField;

    private JComboBox nodeBox;
    private JLabel workAtEdgesLab;
    private JLabel startNodeLab;
    private JLabel workAtNodeLab;
    private JLabel nodeTagLab;
    private JLabel positionLab;
    private JLabel endNodeLab;

    /** (constructor)<br>
      * Construct the panel and places the components.
      *
      * @param mainclass        Reference to the projects mainclass
      */
    public KantenPanel(final GraphAlgController mainclass) {
        setLayout(null);
        this.mainclass = mainclass;
        this.mainclass.addPanel(PanelManager.PANEL_KANTEN, this);

        createEditNodes();
        createEditEdges();

        changeLanguageSettings(mainclass.getLanguageSettings());

        this.addAncestorListener(new AncestorListener() {

            /** Beim Öffnen des Panels werden die angezeigten
              * Daten und Komponenten aktualisiert.
              *
              * @param e    AncestorEvent
              */
            public void ancestorAdded(final AncestorEvent e) {
                refreshPanelComponents();
            }

            public void ancestorMoved(final AncestorEvent e) {
            }

            public void ancestorRemoved(final AncestorEvent e) {
            }
        });
    }

    /** (panel construction method)
      * Creates NodeComboBox which contains exisiting node tags only.
      *
      * @param id       Start | End
      * @return         Created ComboBox
      */
    private JComboBox createNodeBox(final int id) {
        nodeBox = new JComboBox();
        nodeBox.setFont(new Font("Serif", Font.BOLD, 16));

        // Positionieren der ComboBox
        if (id == 1) {
            nodeBox.setBounds(213, 280, 44, 25);
        } else {
            nodeBox.setBounds(213, 310, 44, 25);
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

        this.add(nodeBox);
        return nodeBox;
    }

    /** (panel construction method)<br>
      * Creates the components used to create and edit nodes.
      */
    private void createEditNodes() {

        // Überschriften Label
        workAtNodeLab = new JLabel();
        workAtNodeLab.setFont(new Font("Serif", Font.BOLD, 18));

        this.add(new SComponent(workAtNodeLab,
                           new String[] {"Knoten bearbeiten",
                                         "Edit Nodes"}/*,
                           new int[][] {{65, 20, 150, 25},
                                        {105, 20, 100, 25}}*/));
        this.add(workAtNodeLab);

        // Kennzeichnungs Label
        nodeTagLab = new JLabel();
        nodeTagLab.setBounds(20, 70, 170, 25);

        this.add(new SComponent(nodeTagLab,
                           new String[] {"Kennzeichnung des Knotens",
                                         "Node Tag"}));
        this.add(nodeTagLab);

        // Positionierungs Label
        positionLab = new JLabel();
        positionLab.setBounds(20, 100, 170, 25);

        this.add(new SComponent(positionLab,
                           new String[] {"Position des Knotens",
                                         "Node Position"}));
        this.add(positionLab);

        // TextFeld zur Eingabe der x Koordinate
        xPos = new IntegerTextFieldEx(30, 420);
        xPos.setBounds(203, 100, 30, 25);
        xPos.setText("210");

        this.add(new SComponent(xPos,
                           new String[] {"x Koordinate",
                                         "x Coordinate"}));
        this.add(xPos);

        // TextFeld zur Eingabe der y Koordinate
        yPos = new IntegerTextFieldEx(30, 420);
        yPos.setBounds(237, 100, 30, 25);
        yPos.setText("210");

        this.add(new SComponent(yPos,
                           new String[] {"y Koordinate",
                                         "y Coordinate"}));
        this.add(yPos);

        // ComboBox zur Auswahl des Kennzeichnens eines Knotens
        nodeTagBox = new JComboBox();
        nodeTagBox.setBounds(213, 70, 44, 25);
        nodeTagBox.setFont(new Font("Serif", Font.BOLD, 16));

        char sign = 'A';

        nodeTagBox.addItem(" ");
        for (int i = 0; i < 26; i++) {
            nodeTagBox.addItem(new Character(sign++));
        }

        nodeTagBox.setSelectedIndex(0);
        nodeTagBox.addActionListener(new ActionListener() {

            /** ActionListener der die Events des Buttons kontrolliert
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {
                enableNodeButtons();
            }
        });
        this.add(nodeTagBox);

        // Button der die Erschaffung und Positionierung
        // von Knoten behandelt
        createNodeButton = new JButton();
        createNodeButton.setBounds(10, 180, 132, 25);
        createNodeButton.setEnabled(false);

        createNodeButton.addActionListener(new ActionListener() {

            /** ActionListener der die Events des Buttons kontrolliert
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {

                // Auslesen der Tag Informationen
                AbstractGraph aktuGraph = mainclass.getGraph();
                String tag = nodeTagBox.getSelectedItem().toString();

                // Auslesen der Koordinaten
                int xpos = xPos.getValue().intValue();
                int ypos = yPos.getValue().intValue();

                // Prüfen ob Knoten bereits existiert
                if (!aktuGraph.containsTag(tag)) {

                    // Neuen Knoten erschaffen
                    aktuGraph.addNode(new Node(tag.charAt(0), xpos, ypos));
                    refreshPanelComponents();

                    nodeTagBox.setSelectedIndex(0);
                } else {

                    // Knoten repositionieren
                    aktuGraph.getNode(tag.charAt(0)).moveTo(xpos, ypos);
                }

                mainclass.repaint();
            }
        });
        this.add(createNodeButton);

        // Button dessen Druck das Löschen von Knoten behandelt
        removeNodeButton = new JButton();
        removeNodeButton.setBounds(142, 180, 132, 25);
        removeNodeButton.setEnabled(false);

        this.add(new SComponent(removeNodeButton,
                           new String[] {"Knoten entfernen",
                                         "Remove Node"},
                           new String[] {"Entfernt den Knoten mit der"
                                         + " ausgewählten Kennzeichnung.",
                                         "Removes the node with the choosen"
                                         + " tag."}));

        removeNodeButton.addActionListener(new ActionListener() {

            /** ActionListener der die Events des Buttons kontrolliert
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {

                // Auslesen der Tag Information
                AbstractGraph aktuGraph = mainclass.getGraph();
                String tag = nodeTagBox.getSelectedItem().toString();

                if (mainclass.showUserMessage(
                        MessageHandler.MESSAGE_SURE_TO_REMOVE_NODE)) {

                    // Entfernen des Knotens
                    aktuGraph.removeNode(tag.charAt(0));

                    // Aktualisieren der Panel Komponenten
                    refreshPanelComponents();

                    // Rücksetzen der Eingabe Komponenten
                    nodeTagBox.setSelectedIndex(0);

                    // Neuzeichnen der Komponenten
                    mainclass.repaint();
                }
            }
        });
        this.add(removeNodeButton);
    }

    /** (panel construction method)<br>
      * Constructs the components used to create and edit edges.
      */
    private void createEditEdges() {

        // Überschriften Label
        workAtEdgesLab = new JLabel();
        workAtEdgesLab.setFont(new Font("Serif", Font.BOLD, 18));

        this.add(new SComponent(workAtEdgesLab,
                           new String[] {"Kanten bearbeiten",
                                         "Edit Edges"}/*,
                           new int[][] {{70, 230, 150, 25},
                                        {105, 230, 100, 25}}*/));
        this.add(workAtEdgesLab);

        // Start Knoten Label
        startNodeLab = new JLabel();
        startNodeLab.setBounds(20, 280, 200, 25);

        this.add(new SComponent(startNodeLab,
                           new String[] {"Startknoten der Kante",
                                         "Start Node Tag"}));
        this.add(startNodeLab);

        // Erzeugt eine ComboBox mit dessen Hilfe man die Kennzeichnung
        // eines Knotens auswählen kann
        startNodeBox = createNodeBox(1);

        // Ziel Knoten Label
        endNodeLab = new JLabel();
        endNodeLab.setBounds(20, 310, 200, 25);

        this.add(new SComponent(endNodeLab,
                           new String[] {"Zielknoten der Kante",
                                         "End Node Tag"}));
        this.add(endNodeLab);

        // Erzeugt eine ComboBox mit dessen Hilfe man die Kennzeichnung
        // eines Knotens auswählen kann
        endNodeBox = createNodeBox(2);

        // Gewichts Label
        weightEdgeLab = new JLabel();
        weightEdgeLab.setBounds(20, 340, 200, 25);

        this.add(new SComponent(weightEdgeLab,
                           new String[] {"Gewicht der Kante",
                                         "Weight of the Edge"}));
        this.add(weightEdgeLab);

        // TextFeld zur Eingabe des Gewichts der Kante
        weightEdgeField = new IntegerTextFieldEx(1, 99);
        weightEdgeField.setBounds(223, 340, 25, 25);
        weightEdgeField.setText("1");

        this.add(new SComponent(weightEdgeField,
                           new String[] {"Gewicht der Kante",
                                         "Weight of the Edge" }));
        this.add(weightEdgeField);

        // Erzeugt einen Button durch dessen Druck die Kante erzeugt wird
        // oder das Gewicht der Kante angepasst wird
        createEdgeButton = new JButton();
        createEdgeButton.setBounds(10, 420, 132, 25);
        createEdgeButton.setEnabled(false);

        createEdgeButton.addActionListener(new ActionListener() {

            /** ActionListener der die Events des Buttons kontrolliert
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {

                // Auslesen der Tag Informationen
                char cStart = startNodeBox.getSelectedItem()
                                  .toString().charAt(0);
                char cZiel = endNodeBox.getSelectedItem()
                                  .toString().charAt(0);

                // Laden der zugehörigen Knoten
                AbstractGraph aktuGraph = mainclass.getGraph();
                AbstractNode strt = aktuGraph.getNode(cStart);
                AbstractNode end = aktuGraph.getNode(cZiel);

                // Lesen der Gewichts Information
                int weight = weightEdgeField.getValue().intValue();

                if (aktuGraph.isDirected()) {
                    // Art des Events behandeln
                    if (!aktuGraph.containsTag(cStart + "->" + cZiel)) {

                        // Neue Kante erzeugen
                        AbstractEdge edge = new Edge(strt, end, weight);

                        strt.addEdge(edge);
                        end.addAgainstEdge(edge);

                        // Rücksetzen der Eingabe Komponenten
                        weightEdgeField.setText("1");
                        startNodeBox.setSelectedIndex(0);
                        endNodeBox.setSelectedIndex(0);

                    } else {

                        // Gewicht der Kante anpassen
                        strt.getEdgeTo(end).setWeight(weight);
                    }
                } else {
                    if ((aktuGraph.containsTag(cStart + "->" + cZiel))
                       || (aktuGraph.containsTag(cZiel + "->" + cStart))) {

                        if (cStart > cZiel) {
                            end.getEdgeTo(strt).setWeight(weight);
                        } else {
                            strt.getEdgeTo(end).setWeight(weight);
                        }
                    } else {
                        Edge edge;
                        if (cStart > cZiel) {
                            edge = new Edge(end, strt, weight);

                            end.addEdge(edge);
                            strt.addAgainstEdge(edge);
                        } else {
                            edge = new Edge(strt, end, weight);

                            strt.addEdge(edge);
                            end.addAgainstEdge(edge);
                        }

                        // Rücksetzen der Eingabe Komponenten
                        weightEdgeField.setText("1");
                        startNodeBox.setSelectedIndex(0);
                        endNodeBox.setSelectedIndex(0);
                    }
                }
                mainclass.repaint();
            }
        });
        this.add(createEdgeButton);


        // Erzeugt einen Button durch dessen Druck die ausgewählte Kante
        // entfernt werden kann
        removeEdgeButton = new JButton();
        removeEdgeButton.setBounds(142, 420, 132, 25);
        removeEdgeButton.setEnabled(false);

        this.add(new SComponent(removeEdgeButton,
                           new String[] {"Kante entfernen",
                                         "Remove Edge"},
                           new String[] {"Löscht die Kante die vom"
                                         + " ausgewählten Start zum Ziel geht.",
                                         "Removes the edge which points from"
                                         + " the choosen start to the choosen"
                                         + " target."}));

        removeEdgeButton.addActionListener(new ActionListener() {

            /** ActionListener der die Events des Buttons kontrolliert
              *
              * @param e        ActionEvent
              */
            public void actionPerformed(final ActionEvent e) {

                // Auslesen der Tag Informationen
                String sStart = startNodeBox.getSelectedItem().toString();
                String sZiel = endNodeBox.getSelectedItem().toString();

                // Sicherheitsabfrage
                if (mainclass.showUserMessage(
                        MessageHandler.MESSAGE_SURE_TO_REMOVE_EDGE)) {

                    // Laden der zugehürigen Knoten
                    AbstractGraph aktuGraph = mainclass.getGraph();
                    AbstractNode strt = aktuGraph.getNode(sStart.charAt(0));
                    AbstractNode end = aktuGraph.getNode(sZiel.charAt(0));

                    // Entfernen der Kante
                    if (aktuGraph.isDirected()) {
                        aktuGraph.removeEdge(strt, end);
                    } else {
                        if (sStart.charAt(0) > sZiel.charAt(0)) {
                            aktuGraph.removeEdge(end, strt);
                        } else {
                            aktuGraph.removeEdge(strt, end);
                        }
                    }

                    // Rücksetzen der Eingabe Komponenten
                    weightEdgeField.setText("1");
                    startNodeBox.setSelectedIndex(0);
                    endNodeBox.setSelectedIndex(0);

                    mainclass.repaint();
                }
            }
        });
        this.add(removeEdgeButton);
    }

    /** (panel button handler method)<br>
      * En/Disables the Buttons that controll actions
      * to create/remove and move nodes.
      */
    void enableNodeButtons() {
        AbstractGraph aktuGraph = mainclass.getGraph();

        // Gültiges Kennzeichen ausgewählt
        if (nodeTagBox.getSelectedIndex() == 0) {

            // Disabeln des Erschaffungs Buttons
            createNodeButton.setEnabled(false);

            // Texte des Buttons zurücksetzen
            if (LANGUAGE_GERMAN == mainclass.getLanguageSettings()) {
                createNodeButton.setText("Knoten erzeugen");
                createNodeButton.setToolTipText(
                        "Erzeugt einen Knoten mit der ausgewählten Kennung"
                        + " an der angegebenen Position.");
            } else {
                createNodeButton.setText("Create Node");
                createNodeButton.setToolTipText(
                        "Creates a node with the choosen tag at the"
                        + " specified position.");
            }

            // Standard Position einsetzen
            xPos.setText("210");
            yPos.setText("210");

            // Disabeln des remove Buttons
            removeNodeButton.setEnabled(false);
            return;
        }

        String sTag = nodeTagBox.getSelectedItem().toString();
        boolean flag = aktuGraph.containsTag(sTag);
        if (flag) {
            // Aktualisieren der Position des Knotens
            AbstractNode node = aktuGraph.getNode(sTag.charAt(0));
            xPos.setText("" + node.getXPos());
            yPos.setText("" + node.getYPos());

            // Texte des Buttons anpassen
            if (LANGUAGE_GERMAN == mainclass.getLanguageSettings()) {
                createNodeButton.setText("Knoten bewegen");
                createNodeButton.setToolTipText("Bewegt den ausgewählten"
                        + " Knoten zur entsprechenden Position.");
            } else {
                createNodeButton.setText("Move Node");
                createNodeButton.setToolTipText("Moves the selected node to"
                                                + " the specified position.");
            }
        } else {

            // Standard Position einsetzen
            xPos.setText("210");
            yPos.setText("210");

            // Texte des Buttons anpassen
            if (LANGUAGE_GERMAN == mainclass.getLanguageSettings()) {
                createNodeButton.setText("Knoten erzeugen");
                createNodeButton.setToolTipText("Erzeugt einen Knoten"
                        + " mit der ausgewählten Kennung an der"
                        + " angegebenen Position.");
            } else {
                createNodeButton.setText("Create Node");
                createNodeButton.setToolTipText(
                        "Creates a node with the choosen tag at the"
                        + " specified position.");
            }
        }

        createNodeButton.setEnabled(flag
                                    || (!flag && (aktuGraph.getNumberOfNodes()
                                                  < aktuGraph.maxsize())));
        removeNodeButton.setEnabled(flag);
    }

    /** (panel button handler method)<br>
      * En/Disables the Buttons that controll actions
      * to create/remove and reweight edges.
      */
    void enableEdgeButtons() {
        if ((startNodeBox.getSelectedIndex() == 0)
                || (endNodeBox.getSelectedIndex() == 0)) {

            // Disabelns des Create Buttons
            createEdgeButton.setEnabled(false);

            // Anpassen der ButtonTexte
            if (LANGUAGE_GERMAN == mainclass.getLanguageSettings()) {
                createEdgeButton.setText("Kante erzeugen");
                createEdgeButton.setToolTipText("Erzeugt eine neue"
                        + " Kante mit entsprechendem Gewicht"
                        + " vom Start- zum Zielknoten.");
            } else {
                createEdgeButton.setText("Create Edge");
                createEdgeButton.setToolTipText("Creates a new edge from"
                        + " the specified start to the specified end"
                        + " with the given weight.");
            }

            // Disabeln des Remove Buttons
            removeEdgeButton.setEnabled(false);

            // Standard Wert setzen
            weightEdgeField.setText("1");
            return;
        }

        // Tag der Kante laden
        String sTag = startNodeBox.getSelectedItem().toString() + "->"
                    + endNodeBox.getSelectedItem().toString();
        String vTag = endNodeBox.getSelectedItem().toString() + "->"
                    + startNodeBox.getSelectedItem().toString();

        AbstractGraph aktuGraph = mainclass.getGraph();
        boolean flag = (aktuGraph.containsTag(sTag)
                       || (!aktuGraph.isDirected()
                           && aktuGraph.containsTag(vTag)));

        if (flag && aktuGraph.isWeighted()) {

            if (aktuGraph.containsTag(sTag)) {
                weightEdgeField.setText(""
                     + aktuGraph.getNode(sTag.charAt(0)).getEdgeTo(
                            aktuGraph.getNode(sTag.charAt(3))).getWeight());
            } else {
                weightEdgeField.setText(""
                        + aktuGraph.getNode(vTag.charAt(0)).getEdgeTo(
                               aktuGraph.getNode(vTag.charAt(3))).getWeight());
            }

            // Anpassen des ButtonTextes
            if (LANGUAGE_GERMAN == mainclass.getLanguageSettings()) {
                createEdgeButton.setText("Gewicht ändern");
                createEdgeButton.setToolTipText("");
            } else {
                createEdgeButton.setText("Set Weight");
                createEdgeButton.setToolTipText("");
            }
        } else {

            // Anpassen des ButtonTextes
            if (LANGUAGE_GERMAN == mainclass.getLanguageSettings()) {
                createEdgeButton.setText("Kante erzeugen");
                createEdgeButton.setToolTipText("Erzeugt eine neue"
                        + " Kante mit entsprechendem Gewicht"
                        + " vom Start- zum Zielknoten.");
            } else {
                createEdgeButton.setText("Create Edge");
                createEdgeButton.setToolTipText("Creates a new edge from"
                        + " the specified start to the specified end"
                        + " with the given weight.");
            }
        }

        createEdgeButton.setEnabled(!flag
                                    || (flag && aktuGraph.isWeighted()));
        removeEdgeButton.setEnabled(flag);
    }

    /** (panel display method)<br>
      * Refreshes the current components.
      */
    public void refreshPanelComponents() {

        // Erneut die ComboBoxen zur Start Knoten Tag Wahl
        Object select = startNodeBox.getSelectedItem();
        this.remove(startNodeBox);
        startNodeBox = createNodeBox(1);
        startNodeBox.setSelectedItem(select);

        // Erneuert die ComboBoxen zur Ziel Knoten Tag Wahl
        select = endNodeBox.getSelectedItem();
        this.remove(endNodeBox);
        endNodeBox = createNodeBox(2);
        endNodeBox.setSelectedItem(select);

        // En/Disabeln der Buttons
        enableEdgeButtons();
        enableNodeButtons();

        // Zuweisen des En/Disable Kanten Buttons Listeners
        ActionListener combolistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                enableEdgeButtons();
            }
        };
        startNodeBox.addActionListener(combolistener);
        endNodeBox.addActionListener(combolistener);
    }

    /** (Panel language method)<br>
      * This method can change the languageSettings of all
      * SComponents in this SPanel.
      *
      * @param languageFlag     Language Id
      */
    public void changeLanguageSettings(final int languageFlag) {
        super.changeLanguageSettings(languageFlag);
        enableNodeButtons();
        enableEdgeButtons();
    }

    /** (panel display method)<br>
      * Draws the components of the panel.
      *
      * @param g        Graphical component of this panel
      */
    public void paint(final Graphics g) {
        Dimension size = this.getSize();

        int[] pos = new int[5];
        pos[0] = (size.width - 266) / 2;
        pos[1] = pos[0] + 134;

        pos[3] = (size.width - 240) / 2;
        pos[4] = pos[3] + 190;

        if (mainclass.getLanguageSettings() == LANGUAGE_GERMAN) {
            pos[2] = (size.width - 145) / 2;

            workAtNodeLab.setBounds(pos[2], 20, 145, 25);
            workAtEdgesLab.setBounds(pos[2], 230, 145, 25);
        } else {
            pos[2] = (size.width - 90) / 2;

            workAtNodeLab.setBounds(pos[2], 20, 90, 25);
            workAtEdgesLab.setBounds(pos[2], 230, 90, 25);
        }

        nodeTagLab.setLocation(pos[3], 70);
        nodeTagBox.setLocation(pos[4], 70);

        positionLab.setLocation(pos[3], 100);
        xPos.setLocation(pos[3] + 180, 100);
        yPos.setLocation(pos[3] + 212, 100);

        startNodeLab.setLocation(pos[3], 280);
        startNodeBox.setLocation(pos[4], 280);

        endNodeLab.setLocation(pos[3], 310);
        endNodeBox.setLocation(pos[4], 310);

        weightEdgeLab.setLocation(pos[3], 340);
        weightEdgeField.setLocation(pos[3] + 200, 340);

        createNodeButton.setLocation(pos[0], 180);
        removeNodeButton.setLocation(pos[1], 180);

        createEdgeButton.setLocation(pos[0], 420);
        removeEdgeButton.setLocation(pos[1], 420);

        Image offImage = createImage(size.width, size.height);
        Graphics offGraphics = offImage.getGraphics();

        boolean vis = mainclass.getGraph().isWeighted();
        weightEdgeLab.setVisible(vis);
        weightEdgeField.setVisible(vis);

        super.printComponents(offGraphics);

        offGraphics.setColor(Color.BLACK);
        offGraphics.drawLine(0, 220, size.width, 220);

        g.drawImage(offImage, 0, 0, this);
    }

    /** (internal display method) <br>
      * The PreviewPanel use this methode to aktualize the
      * node position fields when a node had been dragged.
      *
      * @param node     Node that had been moved
      */
    public void refreshNodePosition(final AbstractNode node) {
        if (node.getTag() == nodeTagBox.getSelectedItem()
                                       .toString().charAt(0)) {
            xPos.setText("" + node.getXPos());
            yPos.setText("" + node.getYPos());
        }
    }
}
