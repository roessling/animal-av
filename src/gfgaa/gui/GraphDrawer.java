package gfgaa.gui;

import gfgaa.gui.graphs.AbstractEdge;
import gfgaa.gui.graphs.AbstractGraph;
import gfgaa.gui.graphs.AbstractNode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Vector;

/** Class to draw the nodes and the edges of the graph.
  *
  * @author S. Kulessa
  * @version 0.97
  */
public final class GraphDrawer {

    /** Class to save the position of the self-edges.
      *
      * @author S. Kulessa
      * @version 0.97
      */
    private final class Eigenkante {

        /** Tag of the self-edge. */
        private String tag;

        /** Starting angle of the start position of the self-edge. */
        private int winkel;

        /** (Constructor)<br>
          * Creates a self-edge object.
          *
          * @param tag      Tag of the self-edge
          * @param winkel   Starting angle
          */
        public Eigenkante(final String tag, final int winkel) {
            this.tag = tag;
            this.winkel = winkel;
        }

        /** (internal data method)<br>
          * Returns the tag of the self-edge.
          *
          * @return         Tag of the self-edge
          */
        public String getTag() {
            return this.tag;
        }

        /** (internal data method)<br>
          * Returns the starting angle of the self-edge.
          *
          * @return         Starting angle
          */
        public int getWinkel() {
            return this.winkel;
        }

        /** (internal data method)<br>
          * Saves the new starting angle of the self-edge.
          *
          * @param winkel   New starting angle
          */
        public void setWinkel(final int winkel) {
            this.winkel = winkel;
        }
    }

    /** Color settings. */
    private Color[] colorSettings;

    /** Self-edge vector. */
    private Vector<Eigenkante> data;

    /** Second-edge flag. */
    private int edgeId;

    /** Self-edge flag. */
    private boolean eigenkante;

    /** Last used graphical component. */
    private Graphics g;

    /** Last used start point. */
    private Point nStart;

    /** Last used end point. */
    private Point nZiel;

    /** Last used tag size. */
    private int tagsize;

    /** Last used x position of a tag. */
    private int tagXPos;

    /** Last used y position of a tag. */
    private int tagYPos;

    /** Last used x position of a weight. */
    private int weightXPos;

    /** Last used y position of a weight. */
    private int weightYPos;

    /** Last used angle of a self-edge. */
    private int winkel;

    /** Last used weight Label. */
    private String weightLabel;

    private HashMap weightLabelSet;

    /** (constructor)<br>
      * Creates the GraphDrawer object.
      */
    public GraphDrawer() {
        data = new Vector<Eigenkante>(1, 1);
        colorSettings = new Color[] {Color.LIGHT_GRAY, Color.BLACK,
                                     Color.BLACK, Color.BLACK, Color.RED,
                                     Color.BLACK, Color.WHITE, Color.BLACK,
                                     Color.BLUE, Color.RED, Color.BLACK};
    }

    /** (internal data method)<br>
      * Applies a graphical component to the GraphDrawer object.
      *
      * @param g        Graphical component
      */
    protected void setGraphics(final Graphics g) {
        this.g = g;
    }

    /** (internal data method)<br>
      * Adds a new self-edge if no entry exists and
      * returns the used starting angle.
      *
      * @param tag      Self-edge tag
      * @return         Starting angle
      */
    private int aktualizeEigenkante(final String tag) {
        Eigenkante ek = getEigenkante(tag);
        if (ek == null) {
            data.add(new Eigenkante(tag, 0));
            return 0;
        } else {
            return ek.getWinkel();
        }
    }

    /** (internal data method)<br>
      * Returns the given self-edge.
      *
      * @param tag      Tag of a self-edge
      * @return         Self-edge
      */
    private Eigenkante getEigenkante(final String tag) {
        int anz = data.size();
        for (int i = 0; i < anz; i++) {
            Eigenkante ek = (Eigenkante) data.get(i);
            if (ek.getTag().equals(tag)) {
                return ek;
            }
        }
        return null;
    }

    /** (internal data method)<br>
      * Returns the used starting angle of the specified self-edge.
      *
      * @param tag      Tag of a self-edge
      * @return         Starting angle
      */
    protected int getEigenkanteWinkel(final String tag) {
        Eigenkante ek = getEigenkante(tag);
        if (ek != null) {
            return ek.getWinkel();
        }
        return 0;
    }

    /** (internal data method)<br>
      * Sets the angle of the given self-edge.
      *
      * @param tag      Tag of a self-edge
      * @param winkel   Starting angle
      */
    protected void setEigenkanteWinkel(final String tag, final int winkel) {
        Eigenkante ek = getEigenkante(tag);
        if (ek != null) {
            ek.setWinkel((winkel > 360) ? 0 : winkel);
        }
    }

    /** (internal data method)<br>
      * Updates the color settings.
      *
      * @param colorSettings        New color settings
      */
    protected void setColorSettings(final Color[] colorSettings) {
        this.colorSettings = colorSettings;
    }

/*+Mathe+Funktionen+++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Returns the bigger value and if the biggest value is zero
      * it returns one.
      *
      * @param valueA   First value
      * @param valueB   Second value
      * @return         Biggest value, but at least 1.0
      */
    private double maximum(final double valueA, final double valueB) {
        double maximum = valueB;
        if (valueA > valueB) {
            maximum = valueA;
        }

        if (maximum == 0.) {
            maximum = 1.;
        }
        return maximum;
    }

    /** (internal data method)<br>
      * Rotates a two dimension vector with the given angle in the plane.
      *
      * @param vektor   Two dimensional vector
      * @param winkel   Angle
      * @return         Rotated vector
      */
    public static double[] drehung(final double[] vektor, final double winkel) {
        return new double[] {
            Math.cos(winkel) * vektor[0] - Math.sin(winkel) * vektor[1],
            Math.sin(winkel) * vektor[0] + Math.cos(winkel) * vektor[1]};
    }

    /** (internal data method)<br>
      * Returns the vectors intersection point with the circle.
      *
      * @param vektor   Two dimensional vector
      * @param radius   Diameter of the circle
      * @return         Vectors intersection point with the circle
      */
    public static int[] getKreisposition(final double[] vektor,
                                         final double radius) {
        double[] cpos = new double[2];
        do {
            cpos[0] += vektor[0];
            cpos[1] += vektor[1];
        } while (zweinorm(cpos) < radius);

        return new int[] {(int) cpos[0], (int) cpos[1]};
    }

    /** (internal data method)<br>
      * Returns the length of a n-dimensional vector.
      *
      * @param vektor   vector
      * @return         length of the vector
      */
    public static double zweinorm(final double[] vektor) {
        double sum = 0.;
        for (int i = 0; i < vektor.length; i++) {
            sum += vektor[i] * vektor[i];
        }
        return Math.sqrt(sum);
    }

/*+DRAWING+METHODS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal drawing method)<br>
      * Draws the given self-edge on the graphic g.
      *
      * @param g        Graphical component
      * @param edge     Edge that should be drawn
      * @param radius   Radius of the circles
      * @param arrow    Whether the graph is directed or not
      * @param weight   Whether the graph is weighted or not
      */
    private void drawOwnEdge(final Graphics g, final AbstractEdge edge,
                             final int radius, final boolean arrow,
                             final boolean weight) {

        AbstractNode start = edge.getSource();

        winkel = aktualizeEigenkante(edge.getTag());
        double[] npos = drehung(new double[] {radius, -radius},
                                              winkel * Math.PI / 180);

        nStart = new Point((int) npos[0] + start.getXPos(), (int) npos[1]
                + start.getYPos());

        g.drawArc(nStart.x - radius, nStart.y - radius, 2 * radius, 2 * radius,
                -winkel - ((arrow) ? 60 : 90), ((arrow) ? 240 : 270));

        // Zeichne Pfeilspitze
        if (arrow) {
            double[] vektor = drehung(new double[] {0, -radius},
                    (winkel + 90) * Math.PI / 180);

            nZiel = new Point(start.getXPos() + (int) vektor[0], start
                    .getYPos()
                    + (int) vektor[1]);

            // Normierung nach der unendlich Norm
            double xk = Math.abs(vektor[0]);
            double yk = Math.abs(vektor[1]);
            if ((xk > yk) && (xk != 0)) {
                vektor[0] /= xk;
                vektor[1] /= xk;
            } else if (yk != 0) {
                vektor[0] /= yk;
                vektor[1] /= yk;
            }

            // Berechnen der Pfeilspitzenfläche
            int[] cpos = getKreisposition(vektor, 12.);
            npos = drehung(new double[] {cpos[0], cpos[1]},
                                         -22.5 * Math.PI / 180.);
            Point mid = new Point(nZiel.x + (int) npos[0], nZiel.y
                    + (int) npos[1]);

            cpos = getKreisposition(drehung(vektor, -Math.PI / 6. - 22.5
                    * Math.PI / 180.), 12.);
            Point leftWing = new Point(mid.x + cpos[0], mid.y + cpos[1]);

            cpos = getKreisposition(drehung(vektor, Math.PI / 6. - 22.5
                    * Math.PI / 180.), 12.);
            Point rightWing = new Point(mid.x + cpos[0], mid.y + cpos[1]);

            int[] x = new int[] {nZiel.x, leftWing.x, mid.x, rightWing.x};
            int[] y = new int[] {nZiel.y, leftWing.y, mid.y, rightWing.y};

            g.setColor(colorSettings[ColorChooser.COLOR_EDGE_TOP]);
            g.fillPolygon(x, y, 4);
        }

        // Gewicht der Kante zeichnen
        if (weight) {
            double[] vektor = drehung(new double[] {0, -radius * 5 / 3},
                    (winkel + 45) * Math.PI / 180);

            Point pos = new Point(start.getXPos() + (int) vektor[0], start
                    .getYPos()
                    + (int) vektor[1]);

            g.setColor(colorSettings[ColorChooser.COLOR_EDGE_WEIGHT]);
            Font serif = new Font("Serif", Font.BOLD, 15);

            String sTag = "" + edge.getWeight();
            Rectangle2D sis = serif.getStringBounds(sTag,
                    new FontRenderContext(new AffineTransform(), false, false));

            g.setFont(serif);

            weightXPos = pos.x;
            weightYPos = pos.y + (int) sis.getHeight() / 4;

            g.drawString(sTag, pos.x - (int) sis.getWidth() / 2, weightYPos);
        }
    }

    public boolean drawResidualEdge(final Graphics g,
                                    final AbstractNode start,
                                    final AbstractNode ziel,
                                    final AbstractEdge edge,
                                    final int radius) {

        this.g = g;

        // Exisitiert zweite Kante ?
        boolean secondEdge = (edge.getSource().getTag() != '+'
                             && edge.getTarget().getTag() != '-');

        if (secondEdge) {
            if (edge.getSource() == start) {
                edgeId = 1;
            } else {
                edgeId = 2;
            }
        } else {
            if (edge.getSource() != start) {
                return false;
            }
            edgeId = 0;
        }

        // Berechnen der Abstände
        int distX = start.getXPos() - ziel.getXPos();
        int distY = start.getYPos() - ziel.getYPos();

        if ((distX > -radius) && (distX < radius)
           && (distY > -radius) && (distY < radius)) {
            return false;
        }

        // Berechnen des Richtungsvektors
        double maximum = maximum(Math.abs(distX), Math.abs(distY));
        double[] vektor = new double[] {(distX / maximum), (distY / maximum)};

        // Berechnen der Kreisposition
        int[] cpos = getKreisposition(vektor, radius);

        if (secondEdge) {
            double[] npos1 = drehung(new double[] {cpos[0], cpos[1]},
                                     -Math.PI / 12.);
            double[] npos2 = drehung(new double[] {cpos[0], cpos[1]},
                                     Math.PI / 12.);

            nStart = new Point(start.getXPos() - (int) npos1[0],
                               start.getYPos() - (int) npos1[1]);
            nZiel = new Point(ziel.getXPos() + (int) npos2[0],
                              ziel.getYPos() + (int) npos2[1]);
        } else {
            // Berechne Start und Endpunkt der Linie
            nStart = new Point(start.getXPos() - cpos[0],
                               start.getYPos() - cpos[1]);
            nZiel = new Point(ziel.getXPos() + cpos[0],
                              ziel.getYPos() + cpos[1]);
        }

        // Zeichne Linie
        g.setColor(colorSettings[ColorChooser.COLOR_EDGE_FLOW + (edgeId % 2)]);
        g.drawLine(nStart.x, nStart.y, nZiel.x, nZiel.y);

        // Zeichne Pfeilspitze
        cpos = getKreisposition(vektor, 12.);
        Point mid = new Point(nZiel.x + cpos[0], nZiel.y + cpos[1]);

        cpos = getKreisposition(drehung(vektor, -Math.PI / 6.), 12.);
        Point leftWing = new Point(mid.x + cpos[0], mid.y + cpos[1]);

        cpos = getKreisposition(drehung(vektor, Math.PI / 6.), 12.);
        Point rightWing = new Point(mid.x + cpos[0], mid.y + cpos[1]);

        int[] x = new int[] {nZiel.x, leftWing.x, mid.x, rightWing.x};
        int[] y = new int[] {nZiel.y, leftWing.y, mid.y, rightWing.y};

        g.setColor(colorSettings[ColorChooser.COLOR_EDGE_RTOP]);
        g.fillPolygon(x, y, 4);

        // Gewicht der Kante malen
        drawWeight(edge, distX, distY, vektor, edgeId);

        return true;
    }

    /** (internal drawing method)<br>
      * Draws the weight of the given edge.
      *
      * @param edge         Related Edge
      * @param distX        
      * @param distY        
      * @param vektor       Vector
      * @param directionId  
      */
    private void drawWeight(final AbstractEdge edge,
                            final int distX, final int distY,
                            final double[] vektor, final int directionId) {

        double maximum = maximum(Math.abs(distX * 0.30),
                                 Math.abs(distY * 0.30));

        Point pos = new Point((int) (nZiel.x + maximum * vektor[0]),
              (int) (nZiel.y + maximum * vektor[1]));

        int[] cpos = getKreisposition(drehung(vektor, Math.PI / 2.), 10.);
        Point posA = new Point(pos.x + cpos[0],
                               pos.y + cpos[1]);

        g.setColor(colorSettings[ColorChooser.COLOR_EDGE_WEIGHT]);
        Font serif = new Font("Serif", Font.BOLD, 15);

        switch (directionId) {
            case 0: weightLabel = "*";
                break;
            /*case 1: weightLabel = "" + ((ResidualEdge) edge).getCapacity();
                break;
            case 2: weightLabel = "" + ((ResidualEdge) edge).getFlow();
                break;
*/            default:
                weightLabel = "" + edge.getWeight();
        }

        Rectangle2D sis = serif.getStringBounds(weightLabel,
                            new FontRenderContext(new AffineTransform(),
                                                  false, false));
        g.setFont(serif);

        weightXPos = posA.x;
        weightYPos = posA.y + (int) sis.getHeight() / 4;

        g.drawString(weightLabel, posA.x - (int) sis.getWidth() / 2,
                     weightYPos);
    }

    /** (internal drawing method)<br>
      * Draws the given edge on the graphic g.
      *
      * @param g        Graphical component
      * @param edge     Edge that should be drawn
      * @param radius   Radius of the circles
      * @param arrow    Whether the graph is directed or not
      * @param weight   Whether the graph is weighted or not
      */
    public void drawEdge(final Graphics g, final AbstractEdge edge,
                         final int radius, final boolean arrow,
                         final boolean weight) {
        this.g = g;

        AbstractNode start = edge.getSource();
        AbstractNode ziel = edge.getTarget();

        char startTag = start.getTag();
        char zielTag = ziel.getTag();

        AbstractGraph graph = start.getGraph();

        // Exisitiert zweite Kante ?
        this.edgeId = 0;
        boolean secondEdge = false;

        if (graph.containsTag(zielTag + "->" + startTag)) {
            secondEdge = true;
            if (startTag > zielTag) {
                this.edgeId = 1;
            }
        }

        // Prüfung ob Kante Eigenkante ist
        this.eigenkante = (start == ziel);
        if (this.eigenkante) {
            drawOwnEdge(g, edge, radius, arrow, weight);
            return;
        }

        // Berechnen der Abstände
        int distX = start.getXPos() - ziel.getXPos();
        int distY = start.getYPos() - ziel.getYPos();

        if ((distX > -radius) && (distX < radius)
           && (distY > -radius) && (distY < radius)) {
            return;
        }

        // Berechnen des Richtungsvektors
        double maximum = maximum(Math.abs(distX), Math.abs(distY));
        double[] vektor = new double[] {(distX / maximum), (distY / maximum)};

        // Berechnen der Kreisposition
        int[] cpos = getKreisposition(vektor, radius);

        if (secondEdge) {
            double[] npos1 = drehung(new double[] {cpos[0], cpos[1]},
                                     -Math.PI / 12.);
            double[] npos2 = drehung(new double[] {cpos[0], cpos[1]},
                                     Math.PI / 12.);

            nStart = new Point(start.getXPos() - (int) npos1[0],
                               start.getYPos() - (int) npos1[1]);
            nZiel = new Point(ziel.getXPos() + (int) npos2[0],
                              ziel.getYPos() + (int) npos2[1]);
        } else {
            // Berechne Start und Endpunkt der Linie
            nStart = new Point(start.getXPos() - cpos[0],
                               start.getYPos() - cpos[1]);
            nZiel = new Point(ziel.getXPos() + cpos[0],
                              ziel.getYPos() + cpos[1]);
        }

        // Zeichne Linie
        g.setColor(colorSettings[ColorChooser.COLOR_EDGE_ONE + edgeId]);
        g.drawLine(nStart.x, nStart.y, nZiel.x, nZiel.y);

        // Zeichne Pfeilspitze
        if (arrow) {
            cpos = getKreisposition(vektor, 12.);
            Point mid = new Point(nZiel.x + cpos[0], nZiel.y + cpos[1]);

            cpos = getKreisposition(drehung(vektor, -Math.PI / 6.), 12.);
            Point leftWing = new Point(mid.x + cpos[0], mid.y + cpos[1]);

            cpos = getKreisposition(drehung(vektor, Math.PI / 6.), 12.);
            Point rightWing = new Point(mid.x + cpos[0], mid.y + cpos[1]);

            int[] x = new int[] {nZiel.x, leftWing.x, mid.x, rightWing.x};
            int[] y = new int[] {nZiel.y, leftWing.y, mid.y, rightWing.y};

            g.setColor(colorSettings[ColorChooser.COLOR_EDGE_TOP]);
            g.fillPolygon(x, y, 4);
        }

        // Gewicht der Kante malen
        if (weight) {
            drawWeight(edge, distX, distY, vektor, -1);
        }
    }

    /** (internal drawing method)<br>
      * Draws a circle on the graphic g with the given radius which represents
      * the specified node of the graph.
      *
      * @param g        Grapical component
      * @param node     Node which should be drawn
      * @param radius   Node radius
      */
    public void drawNodes(final Graphics g, final AbstractNode node,
                          final int radius) {
        this.g = g;

        tagsize = radius - 5;
        Font serifBold = new Font("Serif", Font.BOLD, tagsize);
        g.setFont(serifBold);

        g.setColor(colorSettings[ColorChooser.COLOR_NODE_BACKGROUND]);
        g.fillOval(node.getXPos() - radius / 2,
                   node.getYPos() - radius / 2,
                   radius, radius);

        String sTag = "" + node.getTag();
        Rectangle2D sis = serifBold.getStringBounds(sTag,
                              new FontRenderContext(new AffineTransform(),
                                                    false, false));

        g.setColor(colorSettings[ColorChooser.COLOR_NODE_TAG]);

        /* XPosition weist einen Unterschied zwischen GETK und Animal auf */
        tagXPos = node.getXPos();
        tagYPos = node.getYPos() + (int) sis.getHeight() / 4;

        g.drawString(sTag, node.getXPos() - (int) sis.getWidth() / 2, tagYPos);

        g.setColor(colorSettings[ColorChooser.COLOR_NODE_BORDER]);
        g.drawOval(node.getXPos() - radius / 2,
                   node.getYPos() - radius / 2,
                   radius, radius);
    }

/*+PUBLIC+GET+METHODS+++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal data method)<br>
      * Returns the current color settings.
      *
      * @return     Color settings
      */
    public Color[] getColorSettings() {
        return this.colorSettings;
    }

    /** (internal data method)<br>
      * Returns the x position of the last drawn end of the edge.
      *
      * @return     X-Axis postion of the end of the edge
      */
    public int getEdgeEndXPos() {
        return this.nZiel.x;
    }

    /** (internal data method)<br>
      * Returns the y position of the last drawn end of the edge.
      *
      * @return     y-Axis postion of the end of the edge
      */
    public int getEdgeEndYPos() {
        return this.nZiel.y;
    }

    /** (internal data method)<br>
      * Returns the x position of the last drawn start of the edge.
      *
      * @return     X-Axis postion of the start of the edge
      */
    public int getEdgeStartXPos() {
        return this.nStart.x;
    }

    /** (internal data method)<br>
      * Returns the y position of the last drawn start of the edge.
      *
      * @return     Y-Axis postion of the start of the edge
      */
    public int getEdgeStartYPos() {
        return this.nStart.y;
    }

    /** (internal data method)<br>
      * Returns the last used graphic component.
      *
      * @return     Graphical component
      */
    public Graphics getGraphics() {
        return this.g;
    }

    /** (internal data method)<br>
      * Returns the last used angle of a self-edge.
      *
      * @return     Angle of the self-edge
      */
    public int getStartAngle() {
        return winkel;
    }

    /** (internal data method)<br>
      * Returns the last used tag size.
      *
      * @return     Tag size
      */
    public int getTagSize() {
        return this.tagsize;
    }

    /** (internal data method)<br>
      * Returns the last used x position of a tag.
      *
      * @return     X-Axis position of a tag
      */
    public int getTagXPos() {
        return this.tagXPos;
    }

    /** (internal data method)<br>
      * Returns the last used y position of a tag.
      *
      * @return     Y-Axis position of a tag
      */
    public int getTagYPos() {
        return this.tagYPos;
    }

    /** (internal data method)<br>
      * Returns the last used x postion of a weight.
      *
      * @return     X-Axis position of a weight
      */
    public int getWeightXPos() {
        return this.weightXPos;
    }

    /** (internal data method)<br>
      * Returns the last used y postion of a weight.
      *
      * @return     Y-Axis position of a weight
      */
    public int getWeightYPos() {
        return this.weightYPos;
    }

    /** (internal data method)<br>
      * Returns whether the last drawn edge is a self-edge or not.
      *
      * @return     Self-edge flag
      */
    public boolean isEigenkante() {
        return this.eigenkante;
    }

    /** (internal data method)<br>
      * Returns whether a similar edge to the last drawn edge exists or not.
      *
      * @return     Second-edge flag
      */
    public boolean isSecondEdge() {
        return (this.edgeId == 0);
    }

    /** (internal data method)<br>
      * Returns the weight label.
      *
      * @return        Weight label
      */
    public String getWeightLabel() {
        return this.weightLabel;
    }

    /** (internal data method)<br>
      * Returns the edge id.
      *
      * @return        Edge id
      */
    public int getEdgeId() {
        return this.edgeId;
    }

    /** (internal data method)<br>
      * Sets the current used weight label set.
      *
      * @param weightLabelSet        HashMap containing all known edge tags
      */
    public void setWeightLabelSet(final HashMap weightLabelSet) {
        this.weightLabelSet = weightLabelSet;
    }

    /** (internal data method)<br>
      * Returns the used weight label set.
      *
      * @return    Weight label set
      */
    public HashMap getWeightLabelSet() {
        return this.weightLabelSet;
    }
}
