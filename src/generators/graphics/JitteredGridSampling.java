/*
 * JitteredGridSampling.java
 * Simon Braunstein, Sascha Kunz, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.graphics.sampling.AnimalGrid;
import generators.graphics.sampling.AnimalValue;
import generators.graphics.sampling.Utils;
import java.util.ArrayList;
import java.util.Random;
import static generators.graphics.sampling.Utils.buildOffset;
import static generators.graphics.sampling.Utils.removeHTML;
import java.util.List;

@SuppressWarnings("override, static-access")
public class JitteredGridSampling extends SamplingGenerator {

    public JitteredGridSampling() {
        algoName = "Jittered Grid Sampling";
        algoAuthors = "Simon Braunstein, Sascha Kunz";
        algoDescription = "Jittered Grid ist ein Sampling-Algorithmus, um Punkte auf einer Fl&auml;che zu verteilen.<br/>"
                + "Dies kann in der Bildverarbeitung beim Anti-Aliasing zur Verminderung von Alias-Effekten genutzt werden.<br/>"
                + "Es ist aber auch n&uuml;tzlich, um eine nat&uuml;rlich wirkende Positionierung nachzubilden.<br/>"
                + "Als Beispiel seien hier B&auml;ume in einem Wald genannt.<br/>"
                + "Dabei liefert es bessere Ergebnisse als eine komplett zuf&auml;llige Verteilung,<br/>"
                + "wirkt jedoch unnat&uuml;rlicher als das verwandte Poisson-Disk-Sampling, da nicht garantiert ist,"
                + "<br/>dass zwischen zwei Punkten in aneinanderliegenden Teilfl&auml;chen ein Mindestabstand eingehalten wird.<br/>"
                + "<br/>Funktionsweise:<br/>Die Fl&auml;che wird je nach gew&uuml;nschter Dichte in <i>n</i> Zeilen und Spalten aufgeteilt."
                + "<br/>Innerhalb jeder dieser Teilfl&auml;chen wird nun ein Punkt zuf&auml;llig positioniert.";
        SOURCE_CODE = " public ArrayList<Point> jitteredGrid(int gridLength, int density) {" // 0
                // 1
                + "\n\t Random rand = new Random(%d);"
                // 2
                + "\n\t int cellLength = gridLength / density;"
                // 3
                + "\n\t ArrayList<Point> outputList = new ArrayList<Point>();"
                // 4
                + "\n\t for (int i=0; i < density; i++) {"
                // 5
                + "\n\t\t for (int j=0; j < density; j++) {"
                // 6
                + "\n\t\t\t double newX = (rand.nextDouble() + i) * cellLength;"
                // 7
                + "\n\t\t\t double newY = (rand.nextDouble() + j) * cellLength;"
                // 8
                + "\n\t\t\t Point newPoint = new Point((int)newX, (int)newY);"
                // 9
                + "\n\t\t\t outputList.add(newPoint);"
                // 10
                + "\n\t\t }"
                // 11
                + "\n\t }"
                // 12
                + "\n\t return outputList;"
                // 13
                + "\n }";
        algoConclusion = "Verwandte Algorithmen Poisson Disk Sampling <br/>"
                + "<br/>Komplexitätsklasse O(n)"
                + "<br/>Mindestdistanz zweier Punkte: %f"
                + "<br/>Durschnittlische Mindestdistanz: %f";
    }

    private boolean askQuestions;
    private int gridLength;
    private int density;
    private int seed;
    private Random rand;

    // see tocGuard()
    private int tocCount = 4;

    @Override
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        askQuestions = (Boolean) primitives.get("askQuestions");
        gridLength = (Integer) primitives.get("gridLength");
        density = (Integer) primitives.get("density");
        seed = (Integer) primitives.get("seed");
        rand = new Random(seed);
        
        setupProperties(props, primitives);

        Node topicNode = placeTopic("Jittered Grid Sampling");

        //startfolie
        String[] descriptionArray = algoDescription.split("<br/>");
        ArrayList<Text> descriptionTextNodes = new ArrayList<>();

        String refPointTopic = "topicText";
        descriptionTextNodes = placeText("descriptionText", refPointTopic, descriptionArray);

        lang.nextStep("Beschreibung");

        for (Text t : descriptionTextNodes) {
            t.hide();
        }

        int squareSize = gridLength / density;
        AnimalGrid animalGrid = new AnimalGrid(lang, buildOffset(lang, 0, 2, topicNode, "SW"));
        animalGrid.createGrid(squareSize, density, density);

        String sourceCode = String.format(SOURCE_CODE, seed);
        SourceCode sc = generateSourceCode(animalGrid.upperRight(), sourceCode);

        String[] labelValues = new String[]{
            "gridLength:",
            "density:",
            "cellLength:",
            "i:",
            "j:",
            "newX:",
            "newY:",
            "newPoint:"
        };

        // Ask question
        if (askQuestions) {
            String question = String.format("Wie viele Zellen hat ein Gitter mit der Kantenlänge %d und der Dichte %d?", gridLength, density);
            String answer = String.format("%d", density * density);
            lang.addFIBQuestion(Utils.buildFillInQuestion(question, answer, "richtig"));
        }

        // Labels and Values
        AnimalValue[] lables = createValues(labelValues, buildOffset(lang, 0, 10, animalGrid.lowerLeft(), "SW"));
        AnimalValue[] values = createValues(new String[labelValues.length], buildOffset(lang, 100, 10, animalGrid.lowerLeft(), "SW"));
        values[0].setValue(gridLength);
        values[1].setValue(density);
        // Algorithmus
        lang.nextStep("Algorithmus");
        List<java.awt.Point> points = jitteredGrid(sc, animalGrid, values, gridLength, density);

        // Hide all
        animalGrid.hide();
        sc.hide();
        for (AnimalValue label : lables) {
            label.hide();
        }
        for (AnimalValue value : values) {
            value.hide();
        }
        algoConclusion = String.format(algoConclusion, calcMinDistance(points), calcAvgMinDistance(points));
        placeText("conclusion", refPointTopic, algoConclusion.split("<br/>"));
        nextStep("Abschluss");

        lang.finalizeGeneration();
        return lang.toString();
    }

    /**
     * Jittered Grid Sampling for evenly distributing points on a plane.
     * Supports only square shaped grids. Attention: The Density is not actually
     * a straight density, but the <b>reciprocal<b> of density. So it is not
     * expressed in 1/P, but in P.
     *
     * @param sc Animal source code helper
     * @param grid Animal grid helper
     * @param values Animal value helper
     * @param gridLength Length of the edge of a square shaped grid
     * @param density Reciprocal of the Density of the grid
     * @return
     */
    private ArrayList<java.awt.Point> jitteredGrid(SourceCode sc, AnimalGrid grid, AnimalValue[] values, int gridLength, int density) {
        highlight(sc, 1, "Zufallsgenerator initialisieren");
        int cellLength = gridLength / density;
        values[2].setValue(cellLength);
        toggleHighlight(sc, 1, 2, "Kantenlänge der Gitterzellen ermitteln");
        ArrayList<java.awt.Point> outputList = new ArrayList<>();
        toggleHighlight(sc, 2, 3, "Ausgabeliste initialisieren");
        toggleHighlight(sc, 3, 4);
        highlight(sc, 5, "Schleife betreten");
        for (int i = 0; i < density; i++) {
            values[3].setValue(i);
            for (int j = 0; j < density; j++) {
                values[4].setValue(j);
                double newX = (rand.nextDouble() + i) * cellLength;
                double newY = (rand.nextDouble() + j) * cellLength;
                java.awt.Point newPoint = new java.awt.Point((int) newX, (int) newY);
                outputList.add(newPoint);
                // Animal select cell, then draw point
                values[5].setValue("" + newX);
                toggleHighlight(sc, 9, 6, tocGuard("X-Koordinate ermitteln")); // newX
                values[6].setValue("" + newY);
                toggleHighlight(sc, 6, 7, tocGuard("Y-Koordinate ermitteln")); //newY
                values[7].setValue(String.format("Point(%d, %d)", newPoint.x, newPoint.y));
                grid.markCell(grid.cellID(newPoint.x, newPoint.y));
                toggleHighlight(sc, 7, 8, tocGuard("Punkt erzeugen"));
                grid.placeCircle(newPoint.x, newPoint.y);
                toggleHighlight(sc, 8, 9, tocGuard("Punkt an Ausgabeliste anhängen"));
            }
        }
        sc.unhighlight(9);
        sc.unhighlight(5);
        toggleHighlight(sc, 4, 12);
        return outputList;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer apc, Hashtable<String, Object> hshtbl) throws IllegalArgumentException {
        return true;
    }

    private String tocGuard(String label) {
        return tocCount-- > 0 ? label : null;
    }
    
    private ArrayList<Text> placeText(String id, String refPoint, String[] text){
        ArrayList<Text> textNodes = new ArrayList<>();
        for (int i = 0, len = text.length; i < len; i++) {
            String newRefPoint = String.format("%s[%d]", id, i);
            textNodes.add(lang.newText(new Offset(0, 2, refPoint, "SW"), removeHTML(text[i]), newRefPoint, null, textProp));
            //lang.newRect(new Offset(-2, -2, "topicText", "NW"), new Offset(2, 2, "topicText", "SW"), "topicRect", null, textProp);
            refPoint = newRefPoint;
        }
        return textNodes;
    }
    
}
