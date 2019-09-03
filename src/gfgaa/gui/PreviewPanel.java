package gfgaa.gui;

import gfgaa.gui.components.SPanel;
import gfgaa.gui.graphs.AbstractEdge;
import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.graphs.AbstractNode;
import gfgaa.gui.graphs.KantenPanelInterface;
import gfgaa.gui.others.JarFileLoader;
import gfgaa.gui.others.PanelManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;

/** Panel class<br>
  * Panel that contains a painting of the current graph.
  *
  * @author S. Kulessa
  * @version 0.97
  */
public final class PreviewPanel extends SPanel {

    /**
   * 
   */
  private static final long serialVersionUID = -3883620705468561538L;

    /** Reference to the projects mainclass. */
    private GraphAlgController mainclass;

    /** Reference to the project GraphDrawer class. */
    GraphDrawer drawer;

    /** Reference to the moving node. */
    AbstractNode moving;

    /** (constructor)<br>
      * Constructs the panel and this components.
      *
      * @param mainclass        Referene to the projects mainclass
      */
    public PreviewPanel(final GraphAlgController mainclass) {
        this.mainclass = mainclass;
        this.mainclass.addPanel(PanelManager.PANEL_PREVIEW, this);

        this.drawer = mainclass.getGraphDrawer();

        this.setPreferredSize(new Dimension(450, 500));
        this.setMinimumSize(new Dimension(335, 500));

        this.setLayout(null);

        String path = mainclass.getClass().getResource(
                            "MessageHandler.class").toString();
        path = path.substring(0, path.length() - 20);

        logo = new JLabel(JarFileLoader.loadImage(path + "Icons/logo.png"));
        logo.setBounds(0, 0, 450, 450);
        this.add(logo);

        this.addMouseListener(new MouseListener() {

            public void mouseEntered(final MouseEvent e) {
            }

            public void mouseExited(final MouseEvent e) {
            }

            public void mouseClicked(final MouseEvent e) {
            }

            /** Prüft welcher Knoten mit der Maus Position übereinstimmt.
              *
              * @param e        MouseEvent
              * @return         Knoten auf den geklickt wurde oder NULL
              */
            private AbstractNode isWithinCircle(final MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                AbstractGraph loadedgraph = mainclass.getGraph();
                if (loadedgraph != null) {
                    int i = 0, radius = loadedgraph.getRadius();

                    for (; i < loadedgraph.getNumberOfNodes(); i++) {
                        AbstractNode c = loadedgraph.getNode(i);

                        if ((x > c.getXPos() - radius)
                           && (x < c.getXPos() + radius)
                           && (y > c.getYPos() - radius)
                           && (y < c.getYPos() + radius)) {
                            return c;
                        }
                    }
                }
                return null;
            }

            /** Parameter der gesetzt wird wenn die Maustaste
              * losgelassen wird. */
            boolean kicked;

            /** Bewegt die Eigenkante um den angeklickten Knoten.
              * (TODO: Sehr kostenintensive Endlosschleife ;) )
              *
              * @param sTag     Tag des Knotens um den die
              *                 Eigenkante gedreht wird
              */
            void drawAround(final String sTag) {
                int winkel = drawer.getEigenkanteWinkel(sTag);
                drawer.setEigenkanteWinkel(sTag, winkel + 5);
                repaint();

                javax.swing.SwingUtilities.invokeLater(new Runnable() {

                    /** Verlangsamen der Endloschschleife und dadurch
                      * Verhinderung eines StackOverflowErrors.
                      */
                    public void run() {
                        // Solange Maustaste gedrückt ist
                        if (!kicked) {
                            // Rekursiver Aufruf
                            drawAround(sTag);
                        }
                    }
                });
            }

            /** Bei einem Druck einer Maustaste wird geprüft um welche
              * Maustate es sich handelt, um dann entweder den Knoten zu
              * bewegen oder die Eigenkante zu drehen.
              *
              * @param e        MouseEvent
              */
            public void mousePressed(final MouseEvent e) {
                AbstractNode obj = isWithinCircle(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    moving = obj;
                } else if (obj != null) {
                    String sTag = obj.getTag() + "->" + obj.getTag();
                    if (mainclass.getGraph().containsTag(sTag)) {
                        kicked = false;
                        drawAround(sTag);
                    }
                }
            }

            /** Registriert wenn die Maustaste losgelassen wird -
              * d.h. entweder das Ende einer Drag & Drop Operation
              * oder das Ende einer Eigenkanten Drehung.
              *
              * @param e        MouseEvent
              */
            public void mouseReleased(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int[] pos = getMousePosition(e);

                    if (moving != null && pos != null) {
                        moving.moveTo(pos[0], pos[1]);

                        ((KantenPanelInterface) mainclass
                                    .getPanel(PanelManager.PANEL_KANTEN))
                                    .refreshNodePosition(moving);

                        moving = null;
                        repaint();
                    }
                } else {
                    kicked = true;
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseMoved(final MouseEvent e) {
            }

            /** Registriert wenn die Maus mit einer gedrückten
              * Maustaste bewegt wird.
              *
              * @param e        MouseEvent
              */
            public void mouseDragged(final MouseEvent e) {
                int[] pos = getMousePosition(e);

                if (moving != null && pos != null) {
                    moving.moveTo(pos[0], pos[1]);
                    ((KantenPanelInterface) mainclass
                            .getPanel(PanelManager.PANEL_KANTEN))
                            .refreshNodePosition(moving);
                }

                repaint();
            }
        });
    }

    /** Panel component - Used to display the projects logo. */
    private JLabel logo;

    /** (internal display method)<br>
      * Paints the panel and his components.
      *
      * @param g        Graphical component of this panel
      */
    public void paint(final Graphics g) {
        Image offImage = createImage(this.getWidth(), this.getHeight());
        Graphics offGraphics = offImage.getGraphics();

        logo.setVisible(true);
        drawer.setGraphics(offGraphics);
        super.printComponents(offGraphics);

        AbstractGraph loadedGraph = mainclass.getGraph();
        if (loadedGraph != null && loadedGraph.getNumberOfNodes() > 0) {

            AbstractNode c;

            Color[] colors = drawer.getColorSettings();
            offGraphics.setColor(colors[0]);
            offGraphics.fillRect(0, 0, 450, 450);

            int anzKanten, i, j, radius = loadedGraph.getRadius();
            int size = loadedGraph.getNumberOfNodes();

            if (loadedGraph.getGraphTyp() == AbstractGraph.GRAPHTYP_RESIDUAL) {

                /*ResidualNode node;
                ResidualEdge arc;

                for (i = 0; i < size; i++) {
                    node = (ResidualNode) loadedGraph.getNode(i);

                    anzKanten = node.getNumberOfEdges();
                    for (j = 0; j < anzKanten; j++) {
                        arc = (ResidualEdge) node.getEdge(j);

                        offGraphics.setColor(colors[1]);
                        drawer.drawResidualEdge(offGraphics,
                                                node, arc.getOtherEnd(node),
                                                arc, radius);
                    }

                    anzKanten = node.getNumberOfAgainstEdges();
                    for (j = 0; j < anzKanten; j++) {
                        arc = (ResidualEdge) node.getAgainstEdge(j);

                        offGraphics.setColor(colors[1]);
                        drawer.drawResidualEdge(offGraphics,
                                                node, arc.getOtherEnd(node),
                                                arc, radius);
                    }
                }*/

            } else {

                AbstractEdge k;

                for (i = 0; i < size; i++) {
                    c = (AbstractNode) loadedGraph.getNode(i);

                    anzKanten = c.getNumberOfEdges();
                    for (j = 0; j < anzKanten; j++) {
                        k = c.getEdge(j);

                        offGraphics.setColor(colors[1]);
                        drawer.drawEdge(offGraphics,
                                        k, radius,
                                        loadedGraph.isDirected(),
                                        loadedGraph.isWeighted());
                    }
                }
            }

            radius = loadedGraph.getDiameter();
            for (i = 0; i < size; i++) {
                c = (AbstractNode) loadedGraph.getNode(i);
                drawer.drawNodes(offGraphics, c, radius);
            }
        }

        g.drawImage(offImage, 0, 0, this);
    }

    /** empty method - not in use.
      *
      * @see SPanel#refreshPanelComponents
      * @deprecated
      */
    public void refreshPanelComponents() {
    }

/*+INTERNAL+METHODS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal info method)<br>
      * Returns the mouse position in the PreviewPanel. The position will
      * be a two dimensional array with values of the interval [30, 420].
      * [0] represents the x axis position and [1] represents the y axis
      * position.
      *
      * @param e       MouseEvent
      * @return        Mouse position or NULL if the graph is empty
      */
    int[] getMousePosition(final MouseEvent e) {
        int[] pos = new int[] {e.getX(), e.getY()};

        if (mainclass.getGraph() != null) {
            if (pos[0] < 30) {
                pos[0] = 30;
            } else if (pos[0] > 420) {
                pos[0] = 420;
            }

            if (pos[1] < 30) {
                pos[1] = 30;
            } else if (pos[1] > 420) {
                pos[1] = 420;
            }

            return pos;
        }
        return null;
    }
}
