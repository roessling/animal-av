/*
 * PoissonDiskSampling.java
 * Simon Braunstein, Sascha Kunz, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.sampling.AnimalGrid;
import generators.graphics.sampling.AnimalValue;
import generators.graphics.sampling.Utils;
import static generators.graphics.sampling.Utils.buildOffset;
import static generators.graphics.sampling.Utils.removeHTML;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@SuppressWarnings("UseOfObsoleteCollectionType")
public class PoissonDiskSampling extends SamplingGenerator {

    Hashtable<String, SourceCode> sourceCodes;
    ArrayList<String> sourceCodeNames = new ArrayList<>();
    ArrayList<String> sourceCodeStubNames = new ArrayList<>();
    Hashtable<String, String> sourceCodeStrings = new Hashtable<>();

    String scMain;
    String scGenPoints;
    String scPointOutOfArea;
    String scPointTooClose;
    String scCalcDistance;

    String scMainStub;
    String scGenPointsStub;
    String scPointOutOfAreaStub;
    String scPointTooCloseStub;
    String scCalcDistanceStub;
    AnimalGrid animalGrid;
    AnimalValue[] values;
    AnimalValue[] lables;
    int detailedSteps;
    int counterDetailedSteps;
    boolean showStep;
    boolean askQuestions;
    int askCountTrue = 1;
    int askCountFalse = 1;
    int pointsGenerated = 0;
    int pointsDiscarded = 0;
    ArrayList<Integer> pointsGeneratedPerPoint = new ArrayList<>();
    ArrayList<Integer> pointsDiscardedPerPoint = new ArrayList<>();

    String[] labelValues = new String[]{
        "grid length:",
        "distance:",
        "k:",
        "",
        "Function poissonDisk",
        "cellLength:",
        "cellCount:",
        "referencePoint:",
        "newPoint:",
        "processingList.size():",
        "outputList.size():",
        "i (0<=i<k):",
        "",
        "Function generatePointInAnnulus",
        "angle:",
        "radius:",
        "x:",
        "y:",
        "",
        "Function checkIfPointOutOfArea",
        "p.x:",
        "p.y:",
        "",
        "Function checkIfPointTooClose",
        "cellX:",
        "cellY:",
        "i (cellX-2<=i<=cellX+2):",
        "j (cellY-2<=j<=cellY+2):",
        "cellPoint:",
        "",
        "Function calcDistance",
        "pointDistance:"
    };
    DecimalFormat df = new DecimalFormat("#.00");

    public PoissonDiskSampling() {
        algoName = "Poisson Disk Sampling";
        algoAuthors = "Simon Braunstein, Sascha Kunz";
        algoConclusion = "Verwandte Algorithmen Jittered Grid Sampling <br/>"
                + "<br/>Komplexitätsklasse O(n)";
        algoDescription = "<br/>Poisson Disk Sampling ist ein Sampling-Algorithmus, um Punkte derart auf einer Fl&auml;che zu verteilen,<br/>"
                + "dass der Abstand zweier Punkte eine angegebene Gr&ouml;&szlig;e nicht unterschreitet.<br/>"
                + "Dies kann in der Bildverarbeitung beim Anti-Aliasing zur Verminderung von Alias-Effekten genutzt werden.<br/>"
                + "Es ist aber auch n&uuml;tzlich, um eine nat&uuml;rlich wirkende Positionierung nachzubilden.<br/>"
                + "Als Beispiel seien hier B&auml;ume in einem Wald genannt.<br/>"
                + "Dabei liefert es bessere Ergebnisse als eine komplett zuf&auml;llig gew&auml;hlte Verteilung"
                + "<br/>und wirkt nat&uuml;rlicher als das verwandte Jittered Grid Sampling.<br/>"
                + "<br/>Funktionsweise:<br/>"
                
                + "<br/>Auf der Fl&auml;che wird ein Punkt zuf&auml;llig positioniert."
                + "<br/>Zwischen dem ein- und zweifachen gew&uuml;nschten Mindestabstand um diesen Startpunkt"
                + "<br/>werden nun k Punkte generiert. F&uuml;r jeden wird gepr&uuml;ft, ob sich dieser Punkt"
                + "<br/>innerhalb der Grenzen der Fl&auml;che liegt und ob es Nachbarpunkte gibt, deren Mindestabstand"
                + "<br/>unterschritten werden w&uuml;rde. Dazu wird die Fl&auml;che in Teilfl&auml;chen eingeteilt,"
                + "<br/>deren Gr&ouml;&szlig;e vom Mindestabstand abh&auml;ngt. Zu jedem generierten Punkt wird "
                + "<br/>gespeichert, auf welcher Teilfl&auml;che er sich befindet. So kann performant nachgeschlagen"
                + "<br/>werden, ob es Nachbarn gibt, deren Abstand es zu pr&uuml;fen gilt."
                + "<br/>F&uuml;r jeden generierten Punkt, der keinen Abstandskonflikt hat, werden widerum im ein- bis" 
                + "<br/>zweifachen Abstand k Punkte generiert. Wurden alle Punkte traversiert, ohne dass neue Punkte"
                + "<br/>ohne Konflikt gefunden werden k&ouml;nnen, endet der Algorithmus."
                
                
                
                
                ;
        SOURCE_CODE = "\npublic ArrayList<Point> poissonDisk(int gridLength, int distance, int k) {"
                + "\n\tint cellLength = (int) (distance / Math.sqrt(2));"
                + "\n\tArrayList<Point> processingList = new ArrayList<>();"
                + "\n\tArrayList<Point> outputList = new ArrayList<>();"
                + "\n\tint cellCount = (int)Math.ceil((double)gridLength/cellLength);"
                + "\n\tPoint[][] gridCellOccupation = new Point[cellCount][cellCount];"
                + "\n\tint startX = rand.nextInt(gridLength);"
                + "\n\tint startY = rand.nextInt(gridLength);"
                + "\n\tPoint startPoint = new Point(startX, startY);"
                + "\n\tint cellX = startX / cellLength;"
                + "\n\tint cellY = startY / cellLength;"
                + "\n\tgridCellOccupation[cellX][cellY] = startPoint;"
                + "\n\tprocessingList.add(startPoint);"
                + "\n\toutputList.add(startPoint);"
                + "\n\t"
                + "\n\twhile (!processingList.isEmpty()) {"
                + "\n\t\tPoint referencePoint = processingList.remove( rand.nextInt(processingList.size()) );"
                + "\n\t\t"
                + "\n\t\tfor (int i=0; i<k; i++) {"
                + "\n\t\t\tPoint newPoint = generatePointInAnnulus(referencePoint, distance);"
                + "\n\t\t\t"
                + "\n\t\t\tif (checkIfPointOutOfArea(newPoint, gridLength)"
                + "\n\t\t\t\t|| checkIfPointTooClose(newPoint, gridCellOccupation, cellCount, cellLength, distance))"
                + "\n\t\t\t\tcontinue;"
                + "\n\t\t\t"
                + "\n\t\t\toutputList.add(newPoint);"
                + "\n\t\t\tprocessingList.add(newPoint);"
                + "\n\t\t\tgridCellOccupation[(int)newPoint.x/cellLength][(int)newPoint.y/cellLength] = newPoint;"
                + "\n\t\t}"
                + "\n\t}"
                + "\n\treturn outputList;"
                + "\n}"
                + "\n"
                + "\nprivate Point generatePointInAnnulus(Point referencePoint, int distance) {"
                + "\n\tdouble angle = rand.nextDouble() * 2 * Math.PI;"
                + "\n\tdouble radius = (rand.nextDouble()+1) * distance;"
                + "\n\t"
                + "\n\tdouble x = referencePoint.x + radius * Math.cos(angle);"
                + "\n\tdouble y = referencePoint.y + radius * Math.sin(angle);"
                + "\n\t"
                + "\n\treturn new Point( (int)x, (int)y );"
                + "\n}\n"
                + "\nprivate boolean checkIfPointOutOfArea(Point p, int gridLength) {"
                + "\n\treturn (p.x<0 || p.x >= gridLength || p.y<0 || p.y >= gridLength);"
                + "\n}\n"
                + "\nprivate boolean checkIfPointTooClose(Point newPoint, Point[][] gridCellOccupation, int cellCount, int cellLength, int dist) {"
                + "\n\tint cellX = ((int)newPoint.x/cellLength);"
                + "\n\tint cellY = ((int)newPoint.y/cellLength);"
                + "\n\t"
                + "\n\tfor (int i=Math.max(0, cellX -2); i<=Math.min(cellCount-1, cellX +2); i++) {"
                + "\n\t\tfor (int j=Math.max(0, cellY -2); j<=Math.min(cellCount-1, cellY +2); j++) {"
                + "\n\t\t\tPoint cellPoint = gridCellOccupation[i][j];"
                + "\n\t\t\tif (cellPoint != null && calcDistance(cellPoint, newPoint) < dist) {"
                + "\n\t\t\t\treturn true;"
                + "\n\t\t\t}"
                + "\n\t\t}"
                + "\n\t}"
                + "\n\treturn false;"
                + "\n}"
                + "\n"
                + "\nprivate double calcDistance(Point a, Point b) {"
                + "\n\tdouble pointDistance = Math.sqrt( Math.pow(a.x-b.x, 2) + Math.pow(a.y-b.y, 2) );"
                + "\n\treturn pointDistance;"
                + "\n\t"
                + "\n}";

        scMainStub = "public ArrayList<Point> poissonDisk(int gridLength, int distance, int k) { ... }";
        scMain = "public ArrayList<Point> poissonDisk(int gridLength, int distance, int k) {"
                + "\n\tint cellLength = (int) (distance / Math.sqrt(2));"
                + "\n\tArrayList<Point> processingList = new ArrayList<>();"
                + "\n\tArrayList<Point> outputList = new ArrayList<>();"
                + "\n\tint cellCount = (int)Math.ceil((double)gridLength/cellLength);"
                + "\n\tPoint[][] gridCellOccupation = new Point[cellCount][cellCount];"
                + "\n\tint startX = rand.nextInt(gridLength);"
                + "\n\tint startY = rand.nextInt(gridLength);"
                + "\n\tPoint startPoint = new Point(startX, startY);"
                + "\n\tint cellX = startX / cellLength;"
                + "\n\tint cellY = startY / cellLength;"
                + "\n\tgridCellOccupation[cellX][cellY] = startPoint;"
                + "\n\tprocessingList.add(startPoint);"
                + "\n\toutputList.add(startPoint);"
                + "\n\t"
                + "\n\twhile (!processingList.isEmpty()) {"
                + "\n\t\tPoint referencePoint = processingList.remove( rand.nextInt(processingList.size()) );"
                + "\n\t\t"
                + "\n\t\tfor (int i=0; i<k; i++) {"
                + "\n\t\t\tPoint newPoint = generatePointInAnnulus(referencePoint, distance);"
                + "\n\t\t\t"
                + "\n\t\t\tif (checkIfPointOutOfArea(newPoint, gridLength)"
                + "\n\t\t\t\t|| checkIfPointTooClose(newPoint, gridCellOccupation, cellCount, cellLength, distance))"
                + "\n\t\t\t\tcontinue;"
                + "\n\t\t\t"
                + "\n\t\t\toutputList.add(newPoint);"
                + "\n\t\t\tprocessingList.add(newPoint);"
                + "\n\t\t\tgridCellOccupation[(int)newPoint.x/cellLength][(int)newPoint.y/cellLength] = newPoint;"
                + "\n\t\t}"
                + "\n\t}"
                + "\n\treturn outputList;"
                + "\n}";
        scGenPointsStub = "private Point generatePointInAnnulus(Point referencePoint, int distance) { ... }";
        scGenPoints = "private Point generatePointInAnnulus(Point referencePoint, int distance) {"
                + "\n\tdouble angle = rand.nextDouble() * 2 * Math.PI;"
                + "\n\tdouble radius = (rand.nextDouble()+1) * distance;"
                + "\n\t"
                + "\n\tdouble x = referencePoint.x + radius * Math.cos(angle);"
                + "\n\tdouble y = referencePoint.y + radius * Math.sin(angle);"
                + "\n\t"
                + "\n\treturn new Point( (int)x, (int)y );"
                + "\n}";

        scPointOutOfAreaStub = "private boolean checkIfPointOutOfArea(Point p, int gridLength) { ... }";
        scPointOutOfArea = "private boolean checkIfPointOutOfArea(Point p, int gridLength) {"
                + "\n\treturn (p.x<0 || p.x >= gridLength || p.y<0 || p.y >= gridLength);"
                + "\n}";

        scPointTooCloseStub = "private boolean checkIfPointTooClose(Point newPoint, Point[][] gridCellOccupation, int cellCount, int cellLength, int dist) { ... }";
        scPointTooClose = "private boolean checkIfPointTooClose(Point newPoint, Point[][] gridCellOccupation, int cellCount, int cellLength, int dist) {"
                + "\n\tint cellX = ((int)newPoint.x/cellLength);"
                + "\n\tint cellY = ((int)newPoint.y/cellLength);"
                + "\n\t"
                + "\n\tfor (int i=Math.max(0, cellX -2); i<=Math.min(cellCount-1, cellX +2); i++) {"
                + "\n\t\tfor (int j=Math.max(0, cellY -2); j<=Math.min(cellCount-1, cellY +2); j++) {"
                + "\n\t\t\tPoint cellPoint = gridCellOccupation[i][j];"
                + "\n\t\t\tif (cellPoint != null && calcDistance(cellPoint, newPoint) < dist) {"
                + "\n\t\t\t\treturn true;"
                + "\n\t\t\t}"
                + "\n\t\t}"
                + "\n\t}"
                + "\n\treturn false;"
                + "\n}";

        scCalcDistanceStub = "private double calcDistance(Point a, Point b) { ... }";
        scCalcDistance = "private double calcDistance(Point a, Point b) {"
                + "\n\tdouble pointDistance = Math.sqrt( Math.pow(a.x-b.x, 2) + Math.pow(a.y-b.y, 2) );"
                + "\n\treturn pointDistance;"
                + "\n}";

        scMain = scMain + "\n\n" + scGenPointsStub + "\n\n" + scPointOutOfAreaStub + "\n\n" + scPointTooCloseStub + "\n\n" + scCalcDistanceStub;
        scGenPoints = scMainStub + "\n\n" + scGenPoints + "\n\n" + scPointOutOfAreaStub + "\n\n" + scPointTooCloseStub + "\n\n" + scCalcDistanceStub;
        scPointOutOfArea = scMainStub + "\n\n" + scGenPointsStub + "\n\n" + scPointOutOfArea + "\n\n" + scPointTooCloseStub + "\n\n" + scCalcDistanceStub;
        scPointTooClose = scMainStub + "\n\n" + scGenPointsStub + "\n\n" + scPointOutOfAreaStub + "\n\n" + scPointTooClose + "\n\n" + scCalcDistanceStub;
        scCalcDistance = scMainStub + "\n\n" + scGenPointsStub + "\n\n" + scPointOutOfAreaStub + "\n\n" + scPointTooCloseStub + "\n\n" + scCalcDistance;

    }

    static final Logger log = Logger.getAnonymousLogger();
    private Random rand = new Random(11729l);

    private ArrayList<Point> poissonDisk(AnimalGrid grid, AnimalValue[] values, int gridLength, int distance, int k) {

        showSourceCodeOnly("scMain");
        highlight(0, "Poisson Disk Sampling Algorithm started");
        int cellLength = (int) (distance / Math.sqrt(2));
        setAnimalValue("cellLength", cellLength);
        setAnimalValue("k", k);
        toggleHighlight(0, 1);

        ArrayList<Point> processingList = new ArrayList<>();
        ArrayList<Point> outputList = new ArrayList<>();
        setAnimalValue("processingList.size()", processingList.size());
        setAnimalValue("outputList.size()", outputList.size());

        int cellCount = (int) Math.ceil((double) gridLength / cellLength);
        setAnimalValue("cellCount", cellCount);
        toggleHighlight(1, new int[]{2, 3, 4});
        Point[][] gridCellOccupation = new Point[cellCount][cellCount];

        // defining the starting point
        int startX = rand.nextInt(gridLength);
        int startY = rand.nextInt(gridLength);
        Point startPoint = new Point(startX, startY);

        setAnimalValue("referencePoint", startPoint);
        toggleHighlight(new int[]{2,3,4}, new int[]{5,6,7,8}, "Startpunkt");
     
        int cellX = startX / cellLength;
        int cellY = startY / cellLength;

        gridCellOccupation[cellX][cellY] = startPoint;
        toggleHighlight(new int[]{5,6,7,8}, new int[]{9,10,11});
        
        //place startpoint and highlight        
        grid.placeCircle((int)startPoint.x, (int)startPoint.y, AnimalGrid.POINT_COLOR);
        grid.placeCircleUnfilled((int)startPoint.x, (int)startPoint.y, distance, AnimalGrid.POINT_COLOR);
        
        processingList.add(startPoint);
        setAnimalValue("processingList.size()", processingList.size());

        toggleHighlight(new int[]{9, 10, 11}, new int[]{12});
        outputList.add(startPoint);
        pointsGenerated++;
        setAnimalValue("outputList.size()", outputList.size());
        toggleHighlight(12, 13);

        int counter = 0;
        while (!processingList.isEmpty()) {
            toggleHighlight(13, 15);
            counter++;

            //choose random point from processing list and remove it from processing list
            Point referencePoint = processingList.remove(rand.nextInt(processingList.size()));
            setAnimalValue("processingList.size()", processingList.size());

            setAnimalValue("referencePoint", referencePoint);
        
            
            //mark reference point yellow
            grid.markPoint((int)referencePoint.x, (int)referencePoint.y, AnimalGrid.MARK_COLOR);
            grid.removeOrbit((int)referencePoint.x, (int)referencePoint.y, distance);
            grid.placeCircleUnfilled((int)referencePoint.x, (int)referencePoint.y, distance, AnimalGrid.MARK_COLOR);
            grid.placeCircleUnfilled((int)referencePoint.x, (int)referencePoint.y, 2*distance, AnimalGrid.MARK_COLOR);
            toggleHighlight(new int[]{15}, new int[]{16}, "Neuen Referenzpunkt gewählt");
            counterDetailedSteps = detailedSteps;
            
            int counterPointsGeneratedPerPoint = 0;
            int counterPointsDiscardedPerPoint = 0;

            //generate k (=30) random points in annulus
            for (int i = 0; i < k; i++) {
                showStep = (counterDetailedSteps > 0);
                if (showStep) {
                    counterDetailedSteps--;
                }

                setAnimalValue("i (0<=i<k)", i);
                toggleHighlightWithoutStep(new int[]{16, 23, 26, 27}, new int[]{});
                if (showStep) toggleHighlight(new int[]{}, new int[]{18,19});

                Point newPoint = generatePointInAnnulus(referencePoint, distance);
                if (showStep) {
                    setAnimalValue("newPoint", newPoint);
                    setAnimalValue("angle", "");
                    setAnimalValue("radius", "");
                    setAnimalValue("x", "");
                    setAnimalValue("y", "");
                }
                
                grid.placeTemporaryPoint((int) newPoint.x, (int) newPoint.y, AnimalGrid.POINT_COLOR);
                grid.placeTemporaryOrbit((int) newPoint.x, (int) newPoint.y, distance, AnimalGrid.POINT_COLOR);
                if (showStep) {
                    showSourceCodeOnly("scMain");
                    toggleHighlight(new int[]{19}, new int[]{21});
                }
                
                //check if point is out of grid
                //check (per gridCellOccupation) if point is too close
                if (checkIfPointOutOfArea(newPoint, gridLength) || checkIfPointTooClose(newPoint, gridCellOccupation, cellCount, cellLength, distance)) {
                    
                    if (showStep) {
                        askPointPlaced(false);
                        showSourceCodeOnly("scMain");
                    }

                    //TODO mark point red
                    grid.markTemporaryPoint((int) newPoint.x, (int) newPoint.y, AnimalGrid.POINT_NOT_OK_COLOR);
                    grid.markTemporaryOrbit((int) newPoint.x, (int) newPoint.y, distance, AnimalGrid.POINT_NOT_OK_COLOR);

                    if (showStep) {
                        toggleHighlight(new int[]{}, new int[]{23});
                    }

                    grid.removeTemporaryPoint((int) newPoint.x, (int) newPoint.y);
                    grid.removeTemporaryOrbit((int) newPoint.x, (int) newPoint.y, distance);
                    pointsDiscarded++;
                    counterPointsDiscardedPerPoint++;
                    continue;
                }
                askPointPlaced(true);

                if (showStep) {
                    showSourceCodeOnly("scMain");
                }
                //TODO mark point green

                //if there's none, add to the output list, processing list and grid
                outputList.add(newPoint);
                if (showStep) {
                    setAnimalValue("outputList.size()", outputList.size());
                }
                if (showStep) {
                    toggleHighlight(new int[]{}, new int[]{25});
                }

                processingList.add(newPoint);
                gridCellOccupation[(int) newPoint.x / cellLength][(int) newPoint.y / cellLength] = newPoint;

                if (showStep) {
                    setAnimalValue("processingList.size()", processingList.size());
                }
                if (showStep) {
                    toggleHighlight(new int[]{25}, new int[]{26, 27});
                }

                grid.placeCircle((int) newPoint.x, (int) newPoint.y, AnimalGrid.POINT_OK_COLOR);
                grid.removeTemporaryOrbit((int) newPoint.x, (int) newPoint.y, distance);
                pointsGenerated++;
                counterPointsGeneratedPerPoint++;
            }
            //TODO mark referencePoint black
            grid.markPoint((int) referencePoint.x, (int) referencePoint.y, AnimalGrid.POINT_PROCESSED_COLOR);
            grid.removeOrbit((int) referencePoint.x, (int) referencePoint.y, distance);
            grid.removeOrbit((int) referencePoint.x, (int) referencePoint.y, 2 * distance);
            
            pointsDiscardedPerPoint.add(counterPointsDiscardedPerPoint);
            pointsGeneratedPerPoint.add(counterPointsGeneratedPerPoint);
            
        }
        
        showSourceCodeForce("scMain");
        toggleHighlight(new int[]{25}, new int[]{30});
        

        grid.removeAllOrbits();
        toggleHighlightWithoutStep(new int[]{30}, new int[]{});
        lang.nextStep("Grafik ohne Kreise");

        System.out.println("counter: " + counter);
        System.out.println("outputList size: " + outputList.size());
        return outputList;
    }

    private Point generatePointInAnnulus(Point referencePoint, int distance) {
        if (showStep) showSourceCodeOnly("scGenPoints");
        if (showStep) toggleHighlight(new int[]{}, new int[]{2}, "Generiere neuen Punkt im Annulus");
        
        double angle = rand.nextDouble() * 2 * Math.PI;
        double radius = (rand.nextDouble()+1) * distance;
        setAnimalValue("angle", String.valueOf(df.format(angle)));
        setAnimalValue("radius", String.valueOf(df.format(radius)));
        
        if (showStep) toggleHighlight(new int[]{2}, new int[]{3,4});
        
        double x = referencePoint.x + radius * Math.cos(angle);
        double y = referencePoint.y + radius * Math.sin(angle);
        
        if (showStep) setAnimalValue("x", (int)(new Point( (int)x, (int)y ).x));
        if (showStep) setAnimalValue("y", (int)(new Point( (int)x, (int)y ).y));
        
        if (showStep) toggleHighlight(new int[]{2,3,4}, new int[]{6,7, 9});
        
        return new Point( (int)x, (int)y );
    }

    private boolean checkIfPointTooClose(Point newPoint, Point[][] gridCellOccupation, int cellCount, int cellLength, int dist) {
        if (showStep) showSourceCodeOnly("scMain");
        if (showStep) toggleHighlight(new int[]{}, new int[]{22});
        
        if (showStep) showSourceCodeOnly("scPointTooClose");
        if (showStep) toggleHighlight(new int[]{}, new int[]{6}, "Prüfung, ob Punkt zu nah");
        int cellX = ((int)newPoint.x/cellLength);
        int cellY = ((int)newPoint.y/cellLength);
        setAnimalValue("cellX", cellX);
        setAnimalValue("cellY", cellY);
        if (showStep) {
            toggleHighlight(new int[]{6}, new int[]{7, 8});
        }

        for (int i=Math.max(0, cellX -2); i<=Math.min(cellCount-1, cellX +2); i++) {
            if (showStep) showSourceCodeOnly("scPointTooClose");
            if (showStep) setAnimalValue("i (cellX-2<=i<=cellX+2)", i);
            toggleHighlightWithoutStep(new int[]{7,8}, new int[]{});
            
            for (int j=Math.max(0, cellY -2); j<=Math.min(cellCount-1, cellY +2); j++) {
                if (showStep) showSourceCodeOnly("scPointTooClose");
                if (showStep) setAnimalValue("j (cellY-2<=j<=cellY+2)", j);
                
                Point cellPoint = gridCellOccupation[i][j];
                if (showStep) setAnimalValue("cellPoint", cellPoint);
                if (showStep) toggleHighlight(new int[]{13}, new int[]{10, 11, 12});
                if (showStep) toggleHighlight(new int[]{12}, new int[]{13});
                if (cellPoint != null && calcDistance(cellPoint, newPoint) < dist) {
                    if (showStep) showSourceCodeOnly("scPointTooClose");
                    if (showStep) toggleHighlight(new int[]{13}, new int[]{10,11,14});
                    return true;
                }
            }
        }
        if (showStep) showSourceCodeOnly("scPointTooClose");
        if (showStep) toggleHighlight(new int[]{10,11,13,14,12}, new int[]{18});
        return false;
    }

    private boolean checkIfPointOutOfArea(Point p, int gridLength) {
        if (showStep) showSourceCodeOnly("scPointOutOfArea");
        if (showStep) setAnimalValue("p.x", (int)p.x);
        if (showStep) setAnimalValue("p.y", (int)(p.y));
        
        if (showStep) toggleHighlight(new int[]{}, new int[]{4,5}, "Prüfung, ob Punkt im Grid liegt");
        
        if (showStep) setAnimalValue("p.x", "");
        if (showStep) setAnimalValue("p.y", "");
        
        return (p.x<0 || p.x >= gridLength || p.y<0 || p.y >= gridLength);
    }

    private double calcDistance(Point a, Point b) {
        if (showStep) showSourceCodeOnly("scCalcDistance");
        double pointDistance = Math.sqrt( Math.pow(a.x-b.x, 2) + Math.pow(a.y-b.y, 2) );
        if (showStep) setAnimalValue("pointDistance", String.valueOf( df.format(pointDistance)) );
        if (showStep) toggleHighlight(new int[]{}, new int[]{8,9,10}, "Berechnung der Distanz zweier Punkte");
        if (showStep) setAnimalValue("pointDistance", "");
        return pointDistance;
        
    }

    @Override
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        askQuestions = (Boolean) primitives.get("askQuestions");
        detailedSteps = (Integer) primitives.get("detailedSteps");
        int gridLength = (Integer) primitives.get("gridLength");
        int distance = (Integer) primitives.get("distance");
        int k = (Integer) primitives.get("k");
        int seed = (Integer) primitives.get("seed");
        rand = new Random(seed);

        setupProperties(props, primitives);

        System.out.println("placing algo name: " + algoName);
        Node topicNode = placeTopic(algoName);

        //startfolie
        String[] descriptionArray = algoDescription.split("<br/>");
        ArrayList<Text> descriptionTextNodes = new ArrayList<>();

        String refPoint = "topicText";
        for (int i = 0, len = descriptionArray.length; i < len; i++) {
            String newRefPoint = String.format("descriptionText[%d]", i);
            descriptionTextNodes.add(lang.newText(new Offset(0, 0, refPoint, "SW"), removeHTML(descriptionArray[i]), newRefPoint, null, textProp));
            refPoint = newRefPoint;
        }

        lang.nextStep("Topic");

        for (Text t : descriptionTextNodes) {
            t.hide();
        }

        int cellLength = (int) (distance / Math.sqrt(2));
        int cellCount = (int) Math.ceil((double) gridLength / cellLength);

        animalGrid = new AnimalGrid(lang, buildOffset(lang, 0, 2, topicNode, "SW"));
        animalGrid.createGrid(cellLength, cellCount, cellCount);

        sourceCodeNames.add("scMain");
        sourceCodeNames.add("scGenPoints");
        sourceCodeNames.add("scPointOutOfArea");
        sourceCodeNames.add("scPointTooClose");
        sourceCodeNames.add("scCalcDistance");

        sourceCodeStubNames.add("scMainStub");
        sourceCodeStubNames.add("scGenPointsStub");
        sourceCodeStubNames.add("scPointOutOfAreaStub");
        sourceCodeStubNames.add("scPointTooCloseStub");
        sourceCodeStubNames.add("scCalcDistanceStub");

        sourceCodeStrings.put("scMain", scMain);
        sourceCodeStrings.put("scGenPoints", scGenPoints);
        sourceCodeStrings.put("scPointOutOfArea", scPointOutOfArea);
        sourceCodeStrings.put("scPointTooClose", scPointTooClose);
        sourceCodeStrings.put("scCalcDistance", scCalcDistance);

        sourceCodeStrings.put("scMainStub", scMainStub);
        sourceCodeStrings.put("scGenPointsStub", scGenPointsStub);
        sourceCodeStrings.put("scPointOutOfAreaStub", scPointOutOfAreaStub);
        sourceCodeStrings.put("scPointTooCloseStub", scPointTooCloseStub);
        sourceCodeStrings.put("scCalcDistanceStub", scCalcDistanceStub);

        sourceCodes = new Hashtable();

        showSourceCodeOnly("scMain");

        lables = createValues(labelValues, buildOffset(lang, 0, 10, animalGrid.lowerLeft(), "SE"));
        values = createValues(new String[labelValues.length], buildOffset(lang, 200, 10, animalGrid.lowerLeft(), "SE"));
        
        emptyAnimalValues();
        
        setAnimalValue("grid length", gridLength);
        setAnimalValue("distance", distance);

        List<java.awt.Point> points = convert(poissonDisk(animalGrid, values, gridLength, distance, k));

        generateSummary(points);

        lang.finalizeGeneration();
        return lang.toString();
    }

    public void generateSummary(List<java.awt.Point> points) {
        lang.hideAllPrimitives();

        Node topicNode = placeTopic(algoName);
        
        //summary folie
        algoConclusion += "<br/><br/>Statistik:<br/>"
                + "<br/>Mindestdistanz zweier Punkte: %f"
                + "<br/>Durschnittlische Mindestdistanz: %f"
                + "<br/>Insgesamt generierte und akzeptierte Punkte: " + pointsGenerated;
        algoConclusion += "<br/>Insgesamt verworfene Punkte: " + pointsDiscarded;
        
        algoConclusion += "<br/><br/>Punkt Nr.   Generiert   Verworfen<br/>";
        
        algoConclusion = String.format(algoConclusion, calcMinDistance(points), calcAvgMinDistance(points));
        
        for (int i=0; i<pointsGeneratedPerPoint.size(); i++) {
            String nr = String.valueOf(((i+1) <10 )? ("0"+(i+1)): (i+1));
            algoConclusion += "<br/>"+nr+".                 "+pointsGeneratedPerPoint.get(i)+"                 "+pointsDiscardedPerPoint.get(i);
        }
        

        
        String[] summaryArray = algoConclusion.split("<br/>");
        ArrayList<Text> summaryTextNodes = new ArrayList<>();
        
        String refPoint = "topicText";
        for (int i = 0, len = summaryArray.length; i < len; i++) {
            String newRefPoint = String.format("summaryText[%d]", i);
            summaryTextNodes.add(lang.newText(new Offset(0, 5, refPoint, "SW"), summaryArray[i], newRefPoint, null, textProp));
            //lang.newRect(new Offset(-2, -2, "topicText", "NW"), new Offset(2, 2, "topicText", "SW"), "topicRect", null, textProp);
            refPoint = newRefPoint;
        }
        
        lang.nextStep("Abschluss");
    }

    public void setAnimalValue(String name, String value) {
        for (int i = 0; i < labelValues.length; i++) {
            String s = labelValues[i];
            if (s.equals(name + ":")) {
                values[i].setValue(value);
                
                return;
            }
        }
    }

    public void setAnimalValue(String name, int value) {
        for (int i = 0; i < labelValues.length; i++) {
            String s = labelValues[i];
            if (s.equals(name + ":")) {
                values[i].setValue(String.valueOf(value));
                return;
            }
        }
    }

    private void setAnimalValue(String name, Point p) {
        for (int i = 0; i < labelValues.length; i++) {
            String s = labelValues[i];
            if (s.equals(name + ":")) {
                if (p == null) {
                    values[i].setValue("null");
                } else {
                    values[i].setValue("x: " + (int)(p.x) + ", y: " + (int)(p.y));
                }
                return;
            }
        }
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer apc, Hashtable<String, Object> hshtbl) throws IllegalArgumentException {
        return true;
    }
    
    private void showSourceCodeOnly(String functionName) {
        if (actualSourceCode!=null && actualSourceCode.getName().equals(functionName)) {
            //actualSourceCode = null;
            return;
        }
        
        for (int i = 0; i<sourceCodeNames.size(); i++) {
            String scName = sourceCodeNames.get(i);

            SourceCode sc = sourceCodes.get(scName);
            if (sc!=null) {
                sc.hide();
            }
            
            sc = sourceCodes.get(scName+"Stub");
            if (sc!=null) {
                sc.hide();
            }
        }

        if (functionName.length() == 0) {
            actualSourceCode = null;
            return;
        }
        SourceCode sc = generateSourceCode(buildOffset(lang, 10, 0, animalGrid.upperRight(), "SW"), sourceCodeStrings.get(functionName), functionName);
        sourceCodes.put(functionName, sc);
        actualSourceCode = sc;
    }
    
    private void showSourceCodeForce(String functionName) {
        for (int i = 0; i<sourceCodeNames.size(); i++) {
            String scName = sourceCodeNames.get(i);

            SourceCode sc = sourceCodes.get(scName);
            if (sc!=null) {
                sc.hide();
            }
            
            sc = sourceCodes.get(scName+"Stub");
            if (sc!=null) {
                sc.hide();
            }
        }

        if (functionName.length() == 0) {
            actualSourceCode = null;
            return;
        }
        SourceCode sc = generateSourceCode(buildOffset(lang, 10, 0, animalGrid.upperRight(), "SW"), sourceCodeStrings.get(functionName), functionName);
        sourceCodes.put(functionName, sc);
        actualSourceCode = sc;
    }

    private void emptyAnimalValues() {
        for (int i=0; i<labelValues.length; i++) {
            values[i].setValue("");
        }
    }

    private void askPointPlaced(boolean correctAnswer) {
        String question = "Wird der nächste Punkt akzeptiert?";
        if (askQuestions) {
            if (correctAnswer) {
                if (askCountTrue-- > 0) {
                    lang.addMCQuestion(Utils.buildMultipleChoiceQuestion(question, "ja", "richtig", "nein", "falsch"));
                }
            } else {
                if (askCountFalse-- > 0) {
                    lang.addMCQuestion(Utils.buildMultipleChoiceQuestion(question, "nein", "richtig", "ja", "falsch"));
                }
            }
        }
    }
    
    private List<java.awt.Point> convert(List<Point> points){
        List<java.awt.Point> ret = new ArrayList<>();
        for(Point p : points){
            ret.add(new java.awt.Point((int)p.x, (int)p.y));
        }
        return ret;
    }

}

