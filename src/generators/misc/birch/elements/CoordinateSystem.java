package generators.misc.birch.elements;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.misc.birch.OffsetHelper;

public class CoordinateSystem {
    private Language lang;
    private Node upperLeft;
    private int width, height;
    private int xMax, yMax;
    private int xMin, yMin;
    /**
     * Size of one step in the coordinate system in virtual coordinates. Only relevant for markers on axis!
     */
    private int xStepSize, yStepSize;

    private Rect boundingBox;
    private int paddingLeft = 20, paddingBottom = 20, paddingTop = 5, paddingRight = 5;
    /**
     * Size of a step of one in absolute coordinates
     */
    private float realXStepLength, realYStepLength;

    private CircleProperties pointProperties;
    private EllipseProperties circleProperties;
    private PolylineProperties lineProperties;
    private TextProperties textProperties;

    /**
     * @param lang
     * @param upperLeft
     * @param width     in Pixel/ Koordinatenpunkte
     * @param height    in Pixel/ Koordinatenpunkte
     * @param xMax      > xMin
     * @param yMax      > yMin
     * @param xMin      >= 0
     * @param yMin      >= 0
     */
    public CoordinateSystem(Language lang, Node upperLeft, int width, int height, int xMax, int yMax, int xMin, int yMin, int xStepSize, int yStepSize) {
        this.lang = lang;
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
        this.xMax = xMax;
        this.xMin = xMin;
        this.yMax = yMax;
        this.yMin = yMin;
        this.xStepSize = xStepSize;
        this.yStepSize = yStepSize;

        initializeProperties();
        drawBasics();
    }

    public CoordinateSystem(Language lang, Node upperLeft, int size, int axisMax, int axisMin, int stepSize) {
        this(lang, upperLeft, size, size, axisMax, axisMax, axisMin, axisMin, stepSize, stepSize);
    }

    public Rect getBoundingBox() {
        return boundingBox;
    }

    private void initializeProperties() {
        pointProperties = new CircleProperties();
        pointProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

        lineProperties = new PolylineProperties();
        circleProperties = new EllipseProperties();
        textProperties = new TextProperties();
    }

    /**
     * Calculate an offset from the coordinate systems origin in absolute coordinates
     *
     * @param xOffset as absolute coordinates
     * @param yOffset as absolute coordinates
     * @return offset from origin
     */
    private Node createOffsetFromOrigin(int xOffset, int yOffset) {
        return OffsetHelper.offsetOf(upperLeft, paddingLeft + xOffset, height - paddingBottom + yOffset);
    }

    /**
     * Calculate an offset for virtual coordinates
     *
     * @param x as virtual coordinate
     * @param y as virtual coordinate
     * @return offset in the system
     */
    private Node createOffsetForCoordinates(float x, float y) {
        return createOffsetFromOrigin((int) ((x - xMin) * realXStepLength), (int) ((y - yMin) * -realYStepLength));
    }

    private void drawBasics() {
        /* Draw bounding box */
        RectProperties boundingBoxProps = new RectProperties();
        boundingBoxProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
        boundingBox = lang.newRect(upperLeft, OffsetHelper.offsetOf(upperLeft, width, height),
                "boundingBox", null, boundingBoxProps);

        /* Draw x- and y-axis */
        PolylineProperties axisProperties = new PolylineProperties();
        axisProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        axisProperties.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);

        lang.newPolyline(new Node[]{
                OffsetHelper.offsetOf(upperLeft, paddingLeft, paddingTop),
                createOffsetFromOrigin(0, 0),
                OffsetHelper.offsetOf(upperLeft, width - paddingRight, height - paddingBottom),
        }, "axis", null, axisProperties);

        /* Draw x- and y-markers */
        PolylineProperties markerProperties = new PolylineProperties();
        int innerMarkerLength = 3, outerMarkerLength = 5;

        int paddingFromArrow = 25;

        int numberOfXSteps = (xMax - xMin) / xStepSize;
        realXStepLength = (width - paddingLeft - paddingRight - paddingFromArrow) / (float) numberOfXSteps / xStepSize;
        for (int i = 0; i <= numberOfXSteps; i++) {
            Polyline marker = lang.newPolyline(new Node[]{
                    createOffsetFromOrigin((int) (i * xStepSize * realXStepLength), -innerMarkerLength),
                    createOffsetFromOrigin((int) (i * xStepSize * realXStepLength), outerMarkerLength)
            }, "xStep", null, markerProperties);

            String markerText = "" + (xMin + i * xStepSize);
            lang.newText(new Offset(-markerText.length() * 4, 0, marker, AnimalScript.DIRECTION_S),
                    markerText, "xMarker", null);
        }

        int numberOfYSteps = (yMax - yMin) / yStepSize;
        realYStepLength = (height - paddingTop - paddingBottom - paddingFromArrow) / (float) numberOfYSteps / yStepSize;
        for (int i = 0; i <= numberOfYSteps; i++) {
            Polyline marker = lang.newPolyline(new Node[]{
                    createOffsetFromOrigin(innerMarkerLength, (int) (-i * yStepSize * realYStepLength)),
                    createOffsetFromOrigin(-outerMarkerLength, (int) (-i * yStepSize * realYStepLength))
            }, "yStep", null, markerProperties);

            String markerText = "" + (yMin + i * yStepSize);
            lang.newText(new Offset(-6 - (markerText.length() * 6), -7, marker, AnimalScript.DIRECTION_S),
                    markerText, "yMarker", null);
        }
    }

    public Circle drawPoint(float x, float y) {
        return lang.newCircle(createOffsetForCoordinates(x, y), 2, "point", null, pointProperties);
    }

    public Polyline drawLine(float x1, float y1, float x2, float y2) {
        return lang.newPolyline(new Node[]{createOffsetForCoordinates(x1, y1), createOffsetForCoordinates(x2, y2)},
                "line", null, lineProperties);
    }

    public Ellipse drawCircle(float x, float y, float radius) {
        return lang.newEllipse(createOffsetForCoordinates(x, y), new Coordinates((int) (radius * realXStepLength),
                (int) (radius * realYStepLength)), "circle", null, circleProperties);
    }

    public Text drawText(Circle reference, String text) {
        Offset coordinate = (Offset) (reference.getCenter());
        return drawText(coordinate.getX(), coordinate.getY(), text);
    }

    public Text drawText(Polyline reference, String text) {
        Node[] nodes = reference.getNodes();
        Offset node1 = (Offset) nodes[0];
        Offset node2 = (Offset) nodes[1];
        return drawText((node1.getX() + node2.getX()) / 2, (node1.getY() + node2.getY()) / 2, text);
    }

    public Text drawText(Ellipse reference, String text) {
        Offset center = (Offset) reference.getCenter();
        Coordinates radius = (Coordinates) reference.getRadius();
        return drawText(center.getX() + radius.getX() * 3 / 4, center.getY() + radius.getY() * 3 / 4, text);
    }

    private Text drawText(int x, int y, String text) {
        return lang.newText(OffsetHelper.offsetOf(upperLeft, x, y), text, "text", null, textProperties);
    }
}

