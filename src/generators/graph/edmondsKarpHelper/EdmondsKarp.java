package generators.graph.edmondsKarpHelper;

import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import javafx.geometry.Point2D;
import translator.Translator;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.LinkedList;

public class EdmondsKarp {

    private Language lang;

    private Coordinates upperLeft;

    private Graph graph;

    private int numNodes;

    private int startNode;

    private int targetNode;

    private Text[][] labels;

    private int[][] adj;

    private int[][] u;

    private int[][] flow;

    private int[] path;

    private SourceCode theCode;

    private Text annotationText1;

    private Text annotationText2;

    private int pathNumber;

    private final String DESCRIPTION;/* =   "Der Algorithmus von Edmonds und Karp ist ein Graphalgorithmus, der den maximalen Fluss in einem Netzwerk berechnet.\n"
                                                + "Ein Netzwerk oder auch Flussgraph ist ein Graph dessen Kanten individuelle Kapazitäten vorweisen. Dies kann man\n"
                                                + "sich als Rohrsystem von einer Quelle (s) zu einem Ziel (t) vorstellen, wobei jedes Rohr unterschiedlich dick sein\n"
                                                + "kann. Ein Fluss weist jeder Kante in dem Graph einen Wert im Intervall von 0 bis zur jeweiligen Kapazität zu. Der\n"
                                                + "Fluss bestimmt also, wieviel Wasser durch jedes Rohr fließt. Der Flusswert ist die Summe der Flüsse, die aus der\n"
                                                + "Quelle ausgehen. Der maximale Fluss eines Graphen ist der Fluss, dessen Flusswert maximal unter allen Flüssen ist\n"
                                                + "und die Flusserhaltungsbedingung erfüllt. Diese Bedingung besagt, dass in einen Knoten genauso viel Fluss eingeht\n"
                                                + "wie auch aus dem Knoten ausgeht mit Ausnahme der Quelle und des Ziels. In einem Knoten des Rohrsystems kann auch\n"
                                                + "nicht mehr Wasser ausströmen als hereinströmt. Ein flusserhöhender Pfad von der Quelle zum Ziel ist ein s-t-Pfad\n"
                                                + "dessen Fluss unter Berücksichtigung der Flusserhaltungsbedingung erhöht werden kann. Ein Fluss ist daher auch\n"
                                                + "maximal, wenn es keinen flusserhöhenden Pfad mehr in dem Graph gibt.\n"
                                                + "Wichtig ist noch der Begriff des residualen Netzwerks. Zu einem Netzwerk gehört genau ein residuales Netzwerk.\n"
                                                + "In diesem Netzwerk existiert eine Kante zwischen von Knoten a zu Knoten b, wenn die residuale Kapazität zwischen\n"
                                                + "diesen Knoten größer als 0 ist. Die residuale Kapazität ist die Summe des Flusses der Kante von b zu a und der\n"
                                                + "Differenz der Kapazität und des Flusses der Kante von a zu b\n"
                                                + "(siehe https://wiki.algo.informatik.tu-darmstadt.de/Basic_flow_definitions#Residual_network).\n\n"
                                                + "Die letzte Bedingung machen sich der Algorithmus von Ford und Fulkerson wie auch der von Edmonds und Karp zunutze.\n"
                                                + "Ähnlich wie der Algorithmus von Ford und Fulkerson sucht auch der Algorithmus von Edmonds und Karp einen\n"
                                                + "flusserhöhenden Pfad, bis kein weiterer existiert. Der Algorithmus von Edmonds und Karp findet stets einen Pfad\n"
                                                + "mit minimaler Anzahl an Kanten, weshalb konsequent eine Breitensuche zum Finden eines Pfades verwendet wird.\n"
                                                + "Entlang eines solchen Pfades wird dann der Fluss um die minimale Differenz der Kapazitäten und der Flüsse einer\n"
                                                + "jeden Kante auf diesem Pfad erhöht.";*/

    private final String SUMMARY;/* =   "Wie in der Ausführung des Algorithmus zu sehen war, wurde in jeder Iteration des Algorithmus ein flusserhöhender\n"
                                            + "Pfad mit minimaler Anzahl an Kanten betrachtet. Dies impliziert, dass die kleinste Anzahl an Kanten auf einem\n"
                                            + "flusserhöhenden Pfad nach spätestens m Iterationen echt monoton steigt, wobei m die Anzahl der Kanten ist.\n"
                                            + "Die kleinste Anzahl an Kanten auf einem Pfad von s nach t kann nicht größer als n - 1 sein, wobei n die Anzahl\n"
                                            + "der Knoten ist. Damit ist die gesamte Anzahl an durchgeführten Iterationen in O(nm). In jeder Iteration wird\n"
                                            + "im schlimmsten Fall jede Kante des Graphen einmal betrachtet, um den Fluss entsprechend zu erhöhen. Damit ist\n"
                                            + "die asymptotitsche Komplexität des Algorithmus von Edmonds und Karp in O(nmm).\n\n"
                                            + "Damit hat der Algorithmus einen Vorteil gegebenüber des Algorithmus von Ford und Fulkerson, dessen Komplexität\n"
                                            + "vom maximalen Flusswert abhängt. Für große Flüsse eignet sich daher der Algorithmus von Edmonds und Karp besser,\n"
                                            + "da dieser lediglich von der Architektur des Graphen abhängt.";*/

    private Translator translator;

    public EdmondsKarp(Language lang) {
        int[][] adj = new int[][]{
                {0, 1, 0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0},
                {1, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 0, 1, 1, 0},
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0}
        };

        int[][] u = new int[][] {
                {0, 3, 0, 3, 0, 0, 0},
                {0, 0, 4, 0, 0, 0, 0},
                {3, 0, 0, 1, 2, 0, 0},
                {0, 0, 0, 0, 2, 6, 0},
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 9},
                {0, 0, 0, 0, 0, 0, 0}
        };

        int startNode = 0;
        int targetNode = 6;

        upperLeft = new Coordinates(100, 40);

        Node[] nodes = new Node[]{  new Offset(0, 100, upperLeft, null),
                new Offset(0, 400, upperLeft, null),
                new Offset(100, 250, upperLeft, null),
                new Offset(200, 100, upperLeft, null),
                new Offset(200, 400, upperLeft, null),
                new Offset(300, 100, upperLeft, null),
                new Offset(300, 400, upperLeft, null),
        };

        DESCRIPTION = translator.translateMessage("descriptionGlobal");

        SUMMARY = translator.translateMessage("summaryGlobal");

        initAnimal(lang, adj, u, startNode, targetNode, nodes);
    }

    public EdmondsKarp(Language lang, int[][] adj, int[][] u, int startNode, int targetNode, Coordinates[] nodes, Translator translator) {
        upperLeft = new Coordinates(100, 40);

        this.translator = translator;

        Node[] positions = new Node[nodes.length];

        for(int i = 0; i < nodes.length; i++)
            positions[i] = new Offset(nodes[i].getX(), nodes[i].getY(), upperLeft, null);

        DESCRIPTION = translator.translateMessage("descriptionGlobal");

        SUMMARY = translator.translateMessage("summaryGlobal");

        initAnimal(lang, adj, u, startNode, targetNode, positions);
    }

    private int residualCapacity(int from, int to) {
        int residualFlow = 0;
        if(adj[from][to] == 1) {
            residualFlow += u[from][to] - flow[from][to];
            if(adj[to][from] == 1)
                residualFlow += flow[to][from];
        } else if(adj[to][from] == 1)
            residualFlow += flow[to][from];

        return residualFlow;
    }

    private void addResidualFlow(int from, int to, int epsilon) {
        if(adj[from][to] == 1) {
            if(adj[to][from] == 0)
                flow[from][to] += epsilon;
            else {
                flow[to][from] -= Math.max(0, flow[from][to] + epsilon - u[from][to]);
                flow[from][to] = Math.min(u[from][to], flow[from][to] + epsilon);
            }
        } else {
            flow[to][from] -= epsilon;
        }
    }

    boolean hasAugmentingPath() {
        path = new int[numNodes];
        boolean[] marked = new boolean[numNodes];

        LinkedList<Integer> q = new LinkedList<>();
        q.addLast(startNode);

        while(!q.isEmpty() && !marked[targetNode]) {
            int v = q.removeFirst();

            for(int to = 0; to < numNodes; to++) {
                if(residualCapacity(v, to) > 0) {
                    if(!marked[to]) {
                        path[to] = v;
                        marked[to] = true;
                        q.addLast(to);
                    }
                }
            }
        }

        return marked[targetNode];
    }

    void nextStep() {
        for(int v = targetNode; v != startNode; v = path[v]) {
            if(adj[path[v]][v] != 0) {
                graph.setEdgeHighlightPolyColor(path[v], v, Color.BLUE, null, null);
                graph.highlightEdge(path[v], v, null, null);
            } else {
                graph.setEdgeHighlightPolyColor(v, path[v], Color.BLUE, null, null);
                graph.highlightEdge(v, path[v], null, null);
            }
        }

        lang.nextStep(translator.translateMessage("flowAugmentingPathNR") + " " + pathNumber);

        for(int v = targetNode; v != startNode; v = path[v]) {
            if(adj[path[v]][v] != 0)
                graph.setEdgeHighlightPolyColor(path[v], v, Color.ORANGE, null, null);
            else
                graph.setEdgeHighlightPolyColor(v, path[v], Color.ORANGE, null, null);
        }

        theCode.unhighlight(1);
        theCode.unhighlight(8);
        theCode.highlight(2);
        annotationText1.show();

        lang.nextStep();

        theCode.unhighlight(2);
        annotationText1.hide();

        int minFlow = Integer.MAX_VALUE;
        //int minCapacityNode = startNode;
        for(int v = targetNode; v != startNode; v = path[v]) {//Search the minimum residual capacity
            theCode.highlight(4);

            graph.setEdgeHighlightPolyColor(path[v], v, Color.BLUE, null, null);
            if(adj[v][path[v]] != 0) {
                graph.setEdgeHighlightPolyColor(v, path[v], Color.BLUE, null, null);
                graph.highlightEdge(v, path[v], null, null);
            }

            lang.nextStep();

            theCode.unhighlight(4);
            theCode.highlight(5);

            /*if(Math.min(minFlow, residualCapacity(path[v], v)) < minFlow)
                minCapacityNode = v;*/

            int previousFlow = minFlow;
            minFlow = Math.min(minFlow, residualCapacity(path[v], v));

            annotationText2.setText("residualCapacity = min(" + (previousFlow == Integer.MAX_VALUE ? "INFINITY" : Integer.toString(previousFlow)) + ", " + residualCapacity(path[v], v) + ") = " + minFlow, null, null);
            annotationText2.show();

            lang.nextStep();

            graph.setEdgeHighlightPolyColor(path[v], v, Color.ORANGE, null, null);
            if(adj[v][path[v]] != 0) {
                graph.setEdgeHighlightPolyColor(v, path[v], Color.BLACK, null, null);
                if(adj[path[v]][v] == 0)
                    graph.setEdgeHighlightPolyColor(v, path[v], Color.ORANGE, null, null);
            }

            theCode.unhighlight(5);
        }

        for(int v = targetNode; v != startNode; v = path[v]) { //Augment the flow on the path up to saturation
            theCode.highlight(6);

            graph.setEdgeHighlightPolyColor(path[v], v, Color.BLUE, null, null);
            if(adj[v][path[v]] != 0)
                graph.setEdgeHighlightPolyColor(v, path[v], Color.BLUE, null, null);

            lang.nextStep();

            theCode.unhighlight(6);
            theCode.highlight(7);

            addResidualFlow(path[v], v, minFlow);
            if(adj[path[v]][v] != 0) {
                labels[path[v]][v].hide();
                printLabel(path[v], v);
                labels[path[v]][v].changeColor(null, Color.BLUE, null, null);
            }
            else {
                labels[v][path[v]].hide();
                printLabel(v, path[v]);
                labels[v][path[v]].changeColor(null, Color.BLUE, null, null);
            }

            lang.nextStep();

            theCode.unhighlight(7);

            graph.setEdgeHighlightPolyColor(path[v], v, Color.ORANGE, null, null);
            if(adj[v][path[v]] != 0) {
                graph.setEdgeHighlightPolyColor(v, path[v], Color.BLACK, null, null);
                if(adj[path[v]][v] == 0)
                    graph.setEdgeHighlightPolyColor(v, path[v], Color.ORANGE, null, null);
            }

            if(adj[path[v]][v] != 0)
                labels[path[v]][v].changeColor(null, Color.BLACK, null, null);
            else
                labels[v][path[v]].changeColor(null, Color.BLACK, null, null);
        }

        for(int v = targetNode; v != startNode; v = path[v]) {
            if(adj[path[v]][v] != 0) {
                labels[path[v]][v].changeColor(null, Color.BLACK, null, null);
                graph.unhighlightEdge(path[v], v, null, null);
            } else {
                labels[v][path[v]].changeColor(null, Color.BLACK, null, null);
                graph.unhighlightEdge(v, path[v], null, null);
            }
        }

        theCode.highlight(8);
        annotationText2.setText("", null, null);
        annotationText2.hide();
    }

    private void initAnimal(Language lang, int[][] adj, int[][] u, int startNode, int targetNode, Node[] nodes) {
        this.lang = lang;

        pathNumber = 0;

        lang.setStepMode(true);

        this.adj = adj;
        this.u = u;

        this.startNode = startNode;
        this.targetNode = targetNode;

        TextProperties titleProperties = new TextProperties();
        titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
        Text title = lang.newText(new Offset(0, 0, upperLeft, null), "Edmonds-Karp", "Title", null, titleProperties);

        RectProperties titleBoxProperties = new RectProperties();
        titleBoxProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        titleBoxProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        titleBoxProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);

        lang.newRect(new Offset(-10, -10, title, null), new Offset(175, 30, title, null), "TitleBox", null, titleBoxProperties);

        SourceCode description = this.lang.newSourceCode(new Offset(-10, 100, title, null), "Description", null, new SourceCodeProperties());
        Arrays.asList(DESCRIPTION.split("\n")).forEach(s -> description.addCodeLine(s, null, 0, null));

        lang.nextStep(translator.translateMessage("introduction"));
        description.hide();

        SourceCodeProperties codeProperties = new SourceCodeProperties();
        codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 15));
        codeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);

        theCode = lang.newSourceCode(new Offset(475, 100, upperLeft, null), "Sourcecode", null, codeProperties);
        theCode.addCodeLine("edmondsKarp(G, s, t):", null, 0, null);
        theCode.addCodeLine("path = breadthFirstSearch(G, s, t)", null, 1, null);
        theCode.addCodeLine("while(path != null):", null, 1, null);
        theCode.addCodeLine("residualCapacity = INFINITY", null, 2, null);
        theCode.addCodeLine("for(edge in path):", null, 2, null);
        theCode.addCodeLine("residualCapacity = min(residualCapacity, edge.getResidualCapacity())", null, 3, null);
        theCode.addCodeLine("for(edge in path):", null, 2, null);
        theCode.addCodeLine("edge.addResidualFlow(residualCapacity)", null, 3, null);
        theCode.addCodeLine("path = breadthFirstSearch(G, s, t)", null, 2, null);
        theCode.hide();

        TextProperties annotationTextProperties = new TextProperties();
        annotationTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 15));

        annotationText1 = lang.newText(new Offset(620, 39, theCode, null), "true", "AnnotationText1", null, annotationTextProperties);
        annotationText1.hide();

        annotationText2 = lang.newText(new Offset(620, 95, theCode, null), "residualCapacity = min(INFINITY, 0) = 0", "AnnotationText2", null, annotationTextProperties);
        annotationText2.hide();

        numNodes = adj.length;

        labels = new Text[numNodes][numNodes];

        flow = new int[numNodes][numNodes];

        String[] labels = new String[numNodes];
        for(int i = 0; i < numNodes; i++)
            labels[i] = Integer.toString(i);

        GraphProperties graphProperties = new GraphProperties();
        graphProperties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);

        graph = lang.newGraph("Graph", adj, nodes, labels, null, graphProperties);

        for(Node node: nodes)
            graph.setNodeRadius(node, 15, null, null);

        graph.setNodeHighlightFillColor(startNode, Color.BLUE, null, null);
        graph.setNodeHighlightFillColor(targetNode, Color.GREEN, null, null);
        graph.setNodeLabel(startNode, "s", null, null);
        graph.setNodeLabel(targetNode, "t", null, null);
        graph.highlightNode(startNode, null, null);
        graph.highlightNode(targetNode, null, null);

        for(int from = 0; from < numNodes; from++)
            for(int to = 0; to < numNodes; to++) {
                printLabel(from, to);
                graph.setEdgeHighlightPolyColor(from, to, Color.ORANGE, null, null);
            }

        theCode.show();

        lang.nextStep();

        theCode.highlight(1);
        while(hasAugmentingPath()) {
            pathNumber++;
            nextStep();
        }

        lang.nextStep();

        theCode.highlight(2);
        theCode.unhighlight(1);
        theCode.unhighlight(8);
        annotationText1.setText("false", null, null);
        annotationText1.show();

        lang.nextStep();

        theCode.hide();
        annotationText1.hide();

        SourceCode summary = lang.newSourceCode(new Offset(0, 0, theCode, null), "Summary", null, new SourceCodeProperties());
        String summaryString = translator.translateMessage("summaryString1") + " " + pathNumber + " " + translator.translateMessage("summaryString2") + " " + this.flowValue() + ".";

        for(int node = 0; node < adj.length; node++) {
            if(adj[startNode][node] != 0)
                this.labels[startNode][node].changeColor(null, Color.BLUE, null, null);
            if(adj[node][startNode] != 0)
                this.labels[node][startNode].changeColor(null, Color.BLUE, null, null);
            if(adj[node][targetNode] != 0)
                this.labels[node][targetNode].changeColor(null, Color.BLUE, null, null);
            if(adj[targetNode][node] != 0)
                this.labels[targetNode][node].changeColor(null, Color.BLUE, null, null);
        }

        Arrays.asList(summaryString.split("\n")).forEach(s -> summary.addCodeLine(s, null, 0, null));

        lang.nextStep(translator.translateMessage("summaryTitle"));

        graph.hide();
        summary.hide();
        for(int from = 0; from < numNodes; from++) {
            for (int to = 0; to < numNodes; to++) {
                if (adj[from][to] > 0) {
                    this.labels[from][to].hide();
                }
            }
        }

        SourceCode outline = this.lang.newSourceCode(new Offset(-10, 100, title, null), "Description", null, new SourceCodeProperties());
        Arrays.asList(SUMMARY.split("\n")).forEach(s -> outline.addCodeLine(s, null, 0, null));

        lang.nextStep(translator.translateMessage("outline"));
    }

    private void printLabel(int from, int to) {
        Offset p1 = (Offset) graph.getNode(from);
        Offset p2 = (Offset) graph.getNode(to);
        Point2D s = new Point2D(p1.getX(), p1.getY());
        Point2D e = new Point2D(p2.getX(), p2.getY());

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        AffineTransform aftr = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(aftr, true, true);

        Point2D l = e.subtract(s); //Direction
        Point2D m = s.midpoint(e); //Middle

        TextProperties labelProperties = new TextProperties();
        labelProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

        if(adj[from][to] > 0) {
            Point2D n;
            String label = flow[from][to] + "/" + u[from][to];
            if(l.getX() >= 0 && l.getY() <= 0) //First Quarter: upper left
                n = new Point2D(-l.getY(), l.getX()).normalize().multiply(10);
            else if(l.getX() < 0 && l.getY() < 0) //Second Quarter: down left
                n = new Point2D(-l.getY(), l.getX()).normalize().multiply(10).subtract(0, font.getStringBounds(label, frc).getHeight());
            else if(l.getX() < 0 && l.getY() >= 0) //Third Quarter: down right
                n = new Point2D(-l.getY(), l.getX()).normalize().multiply(10).subtract(font.getStringBounds(label, frc).getWidth(), font.getStringBounds(label, frc).getHeight());
            else
                n = new Point2D(-l.getY(), l.getX()).normalize().multiply(10).subtract(font.getStringBounds(label, frc).getWidth(), 0).add(0, font.getStringBounds(label, frc).getHeight());
            Point2D c = m.add(n);
            labels[from][to] = lang.newText(new Offset((int) c.getX(), (int) c.getY(), upperLeft, ""), label, "", null, labelProperties);
            //lang.nextStep();
        }
    }

    private int flowValue() {
        int output = 0;
        for(int i = 0; i < adj.length; i++) {
            if(adj[startNode][i] != 0)
                output += flow[startNode][i];

            if(adj[i][startNode] != 0)
                output -= flow[i][startNode];
        }
        return output;
    }

    private static double getHeight(Coordinates p1, Coordinates p2) {
        Point2D s = new Point2D(p1.getX(), p1.getY());
        Point2D e = new Point2D(p2.getX(), p2.getY());
        return 25 * (1 / (1 + Math.exp((s.distance(e) - 275) / 55))) + 15;
    }

    @SuppressWarnings("unused")
    private Circle calcCircle(Coordinates p1, Coordinates p2) {
        double h = EdmondsKarp.getHeight(p1, p2);

        Point2D s = new Point2D(p1.getX(), p1.getY());
        Point2D e = new Point2D(p2.getX(), p2.getY());

        lang.newPolyline(new Node[]{p1, p2}, "l", null);

        Point2D l = e.subtract(s);
        Point2D m = s.midpoint(e);

        lang.newCircle(new Coordinates((int) m.getX(), (int) m.getY()), 3, "Middle", null);

        Point2D n = new Point2D(-l.getY(), l.getX()).normalize().multiply(h);

        Point2D c = m.add(n);

        lang.newCircle(new Coordinates((int) c.getX(), (int) c.getY()), 3, "H", null);

        lang.newPolyline(new Node[]{new Coordinates((int) m.getX(), (int) m.getY()), new Coordinates((int) c.getX(), (int) c.getY())}, "h", null);

        double x1 = s.getX(), y1 = s.getY(), x2 = c.getX(), y2 = c.getY(), x3 = e.getX(), y3 = e.getY();

        double C = ((x1 * x1 + y1 * y1 - x3 * x3 - y3 * y3) * (x1 - x2) + (-x1 * x1 - y1 * y1 + x2 * x2 + y2 * y2) * (x1 - x3)) /
                ((-y1 + y2) * (x1 - x3) + (y1 - y3) * (x1 - x2));

        double B = (-C * y1 + C * y2 + x1 * x1 + y1 * y1 - x2 * x2 - y2 * y2) / (x1 - x2);

        System.out.println((int) B);

        double A = B * x1 + C * y1 - x1 * x1 - y1 * y1;

        int xM = (int) B / 2, yM = (int) C / 2;

        int r = (int) Math.sqrt(xM * xM + yM * yM - A);

        return this.lang.newCircle(new Coordinates(xM, yM), r, "TheCircle", null, new CircleProperties());
    }
}
