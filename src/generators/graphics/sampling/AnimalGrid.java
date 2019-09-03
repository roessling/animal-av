package generators.graphics.sampling;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Point;
import algoanim.primitives.Primitive;
import algoanim.primitives.Square;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.SquareProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author simon
 */
@SuppressWarnings("static-access")
public class AnimalGrid {

    private final Language lang;
    private int width;
    private int height;
    private int squareSize;
    private Square[][] grid;
    private Square markedSquare;
    private final Point basePoint;
    private final Hashtable<String, Circle> points = new Hashtable<>();
    private final Hashtable<String, Circle> temporaryPoints = new Hashtable<>();
    private final Hashtable<String, Circle> temporaryOrbits = new Hashtable<>();
    private final Hashtable<String, Circle> orbits = new Hashtable<>();
    public static final int SCALING = 4; // Multiples of 2 are good
    public static Color MARK_COLOR = Color.YELLOW;
    public static Color FILL_COLOR = Color.GRAY;
    public static Color POINT_COLOR = Color.BLUE;
    public static Color POINT_PROCESSED_COLOR = Color.BLACK;
    public static Color POINT_NOT_OK_COLOR = Color.RED;
    public static Color POINT_OK_COLOR = Color.GREEN;
    
    private static final AnimationPropertiesKeys APK = new AnimationPropertiesKeys() {
    };
    
    public static void setColor(SquareProperties fillProp, SquareProperties markProp){
        FILL_COLOR = (Color) fillProp.get(APK.FILL_PROPERTY);
        MARK_COLOR = (Color) markProp.get(APK.FILL_PROPERTY);
        
//        POINT_COLOR = 
//        POINT_PROCESSED_COLOR
//        POINT_NOT_OK_COLOR
//        POINT_OK_COLOR
//        APK.COLOR_PROPERTY
    }

    public AnimalGrid(Language l) {
        lang = l;
        //baseCoordinates = new Coordinates(25, 25);
        /* FIXME: Workaround because animalscript Locations are not available
         * and "Offset based on an instance of algoanim.util.Offset not
         * supported"!
         */
        basePoint = lang.newPoint(new Coordinates(25, 25), "ReferencePoint", null, new PointProperties());
    }
    
    public AnimalGrid(Language l, Node node){
        lang = l;
        basePoint = lang.newPoint(node, "ReferencePoint", null, new PointProperties());
        
    }
    
    public void nextStep() {
        lang.nextStep();
    }

    public String cellID(int posX, int posY) {
        return String.format("Square[%d][%d]", posX / squareSize, posY / squareSize);
    }

    public Node lowerRight(){
        int right = grid.length - 1;
        int lower = grid[right].length - 1;
        Square square = grid[right][lower];
        return new Offset(0, 0, square, "SE");
    }
    
    public Node lowerLeft(){
        int left = 0;
        int lower = grid[left].length - 1;
        Square square = grid[left][lower];
        return new Offset(0, 0, square, "SW");
    }
    
    public Node upperRight(){
        int right = grid.length - 1;
        int upper = 0;
        Square square = grid[right][upper];
        return new Offset(0, 0, square, "NE");
    }
    

    public void createGrid(int squareSize, int width, int height) {
        this.squareSize = squareSize;
        this.width = width;
        this.height = height;
        grid = new Square[width][height];
        int scaledSize = squareSize * SCALING;
        SquareProperties squareProp = new SquareProperties();
        squareProp.set(APK.FILLED_PROPERTY, true);
        squareProp.set(APK.FILL_PROPERTY, FILL_COLOR);
        squareProp.set(APK.DEPTH_PROPERTY, 25);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Offset offset = new Offset(i * scaledSize, j * scaledSize, basePoint, "C");
                String squareID = String.format("Square[%d][%d]", i, j);
                Square square = lang.newSquare(offset, scaledSize, squareID, null, squareProp);
                grid[i][j] = square;
            }
        }
    }

    public void markCell(String squareID) {
        if (markedSquare != null) {
            unmarkCell(markedSquare);
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Square square = grid[i][j];
                if (square.getName().equals(squareID)) {
                    markCell(square);
                }
            }
        }
    }

    public void markCell(Square square) {
        markedSquare = square;
        square.changeColor("fillColor", MARK_COLOR, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    }

    public void unmarkCell(Square square) {
        square.changeColor("fillColor", FILL_COLOR, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    }
    
    public void markPointOk(int xCoordinate, int yCoordinate) {
        String circleID = String.format("Point[%d][%d]", xCoordinate, yCoordinate);
        Circle c = points.get(circleID);
        if (c==null)
            return;
        c.changeColor("fillColor", POINT_OK_COLOR, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    }
    
    public void markPointNotOk(int xCoordinate, int yCoordinate) {
        String circleID = String.format("Point[%d][%d]", xCoordinate, yCoordinate);
        Circle c = points.get(circleID);
        if (c==null)
            return;
        c.changeColor("fillColor", POINT_NOT_OK_COLOR, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        c.changeColor("color", POINT_NOT_OK_COLOR, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    }
    
    public void markPointAndOrbit(int xCoordinate, int yCoordinate, int radius, Color color) {
        String circleID = String.format("Point[%d][%d]", xCoordinate, yCoordinate);
        Circle c = points.get(circleID);
        if (c!=null) {
            c.changeColor("fillColor", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            c.changeColor("color", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
        
        circleID = String.format("Point[%d][%d][%d]", xCoordinate, yCoordinate, radius);
        c = orbits.get(circleID);
        if (c!=null) {
            c.changeColor("color", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
    }
    
    public void markOrbit(int xCoordinate, int yCoordinate, int radius, Color color) {
        String circleID = String.format("Point[%d][%d][%d]", xCoordinate, yCoordinate, radius);
        Circle c = orbits.get(circleID);
        if (c!=null) {
            c.changeColor("color", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
    }
    
    public void markPoint(int xCoordinate, int yCoordinate, Color color) {
        String circleID = String.format("Point[%d][%d]", xCoordinate, yCoordinate);
        
        Circle c = points.get(circleID);
        if (c!=null) {
            c.changeColor("fillColor", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            c.changeColor("color", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
    }
    
    public void markTemporaryPoint(int xCoordinate, int yCoordinate, Color color) {
        String circleID = String.format("Point[%d][%d]", xCoordinate, yCoordinate);
        
        Circle c = temporaryPoints.get(circleID);
        if (c!=null) {
            c.changeColor("fillColor", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            c.changeColor("color", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
    }
    
    public void markTemporaryOrbit(int xCoordinate, int yCoordinate, int distance, Color color) {
        String circleID = String.format("Point[%d][%d][%d]", xCoordinate, yCoordinate, distance);
        
        Circle c = temporaryOrbits.get(circleID);
        if (c!=null) {
            c.changeColor("color", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
    }
    
    public void markPoint(Circle circle) {
        circle.changeColor("fillColor", MARK_COLOR, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        circle.changeColor("color", MARK_COLOR, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    }

    public void placeCircle(int xCoordinate, int yCoordinate) {
        placeCircle(basePoint, xCoordinate, yCoordinate);
    }
    
    public void placeCircle(int xCoordinate, int yCoordinate, Color color) {
        placeCircle(basePoint, xCoordinate, yCoordinate, color, false);
    }
    
    public void placeTemporaryPoint(int xCoordinate, int yCoordinate, Color color) {
        placeCircle(basePoint, xCoordinate, yCoordinate, color, true);
    }

    public void placeTemporaryOrbit(int xCoordinate, int yCoordinate, int distance, Color color) {
        placeCircleUnfilled(basePoint, xCoordinate, yCoordinate, distance, color, true);
    }
    
    public void placeCircle(Primitive basePoint, int xCoordinate, int yCoordinate, Color color, boolean temporary) {
        int scaledX = xCoordinate * SCALING;
        int scaledY = yCoordinate * SCALING;
        int radius = SCALING / 2;
        CircleProperties circleProp = new CircleProperties();
        circleProp.set(APK.COLOR_PROPERTY, color);
        circleProp.set(APK.DEPTH_PROPERTY, 1);
        circleProp.set(APK.FILL_PROPERTY, color);
        circleProp.set(APK.FILLED_PROPERTY, true);

        Offset offset = new Offset(scaledX, scaledY, basePoint, "SE");
        String circleID = String.format("Point[%d][%d]", xCoordinate, yCoordinate);
        if (temporary) {
            temporaryPoints.put(circleID, lang.newCircle(offset, radius, circleID, null, circleProp) );
        } else {
            points.put(circleID, lang.newCircle(offset, radius, circleID, null, circleProp) );
        }
    }
    
    public void placeCircle(Primitive basePoint, int xCoordinate, int yCoordinate) {
        placeCircle(basePoint, xCoordinate, yCoordinate, POINT_COLOR, false);
    }
    
    public void placeCircleUnfilled(int xCoordinate, int yCoordinate, int radius) {
        placeCircleUnfilled(basePoint, xCoordinate, yCoordinate, radius);
    }
    
    public void placeCircleUnfilled(int xCoordinate, int yCoordinate, int radius, Color color) {
        placeCircleUnfilled(basePoint, xCoordinate, yCoordinate, radius, color, false);
    }
    
    public void placeCircleUnfilled(Primitive basePoint, int xCoordinate, int yCoordinate, int radius) {
        placeCircleUnfilled(basePoint, xCoordinate, yCoordinate, radius, POINT_COLOR, false);
    }
    
    public void placeCircleUnfilled(Primitive basePoint, int xCoordinate, int yCoordinate, int radius, Color color, boolean temporary) {
        int scaledX = xCoordinate * SCALING;
        int scaledY = yCoordinate * SCALING;
        int scaledRadius = radius*SCALING;
        CircleProperties circleProp = new CircleProperties();
        circleProp.set(APK.COLOR_PROPERTY, color);
        circleProp.set(APK.DEPTH_PROPERTY, 1);
        circleProp.set(APK.FILL_PROPERTY, color);
        circleProp.set(APK.FILLED_PROPERTY, false);

        Offset offset = new Offset(scaledX, scaledY, basePoint, "C");
        String circleID = String.format("Point[%d][%d][%d]", xCoordinate, yCoordinate, radius);
        if (temporary) {
            temporaryOrbits.put(circleID, lang.newCircle(offset, scaledRadius, circleID, null, circleProp) );
        } else {
            orbits.put(circleID, lang.newCircle(offset, scaledRadius, circleID, null, circleProp) );
        }
        
    }
   

    public void removeOrbit(int xCoordinate, int yCoordinate, int radius) {
        String circleID = String.format("Point[%d][%d][%d]", xCoordinate, yCoordinate, radius);
        Circle c = orbits.remove(circleID);
        if (c==null)
            return;
        c.hide();
    }
    
    public void removeAllOrbits() {
        for (Map.Entry<String, Circle> entry : orbits.entrySet()) {
            Circle c = entry.getValue();
            c.hide();
            orbits.remove(entry.getKey());
        }
    }
    
    public void removeTemporaryPoint(int xCoordinate, int yCoordinate) {
        String circleID = String.format("Point[%d][%d]", xCoordinate, yCoordinate);
        Circle c = temporaryPoints.remove(circleID);
        if (c==null)
            return;
        c.hide();
    }
    
    public void removeTemporaryOrbit(int xCoordinate, int yCoordinate, int distance) {
        String circleID = String.format("Point[%d][%d][%d]", xCoordinate, yCoordinate, distance);
        Circle c = temporaryOrbits.remove(circleID);
        if (c==null)
            return;
        c.hide();
    }
    
    public void removePoint(int xCoordinate, int yCoordinate) {
        String circleID = String.format("Point[%d][%d]", xCoordinate, yCoordinate);
        Circle c = points.remove(circleID);
        if (c==null)
            return;
        c.hide();
    }
    
    public void hide(){
        for(Square[] line : grid){
            for(Square square: line){
                square.hide();
            }
        }
        for(Circle point : points.values()){
            point.hide();
        }
        for(Circle circle : temporaryOrbits.values()){
            circle.hide();
        }
        for(Circle circle : temporaryPoints.values()){
            circle.hide();
        }
        for(Circle circle : orbits.values()){
            circle.hide();
        }
    }

}
