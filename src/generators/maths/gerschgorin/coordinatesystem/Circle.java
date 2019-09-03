package generators.maths.gerschgorin.coordinatesystem;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.CircleProperties;
import algoanim.util.*;

import java.awt.*;

/**
 * A drawable gerschgorin circle.
 * @author Jannis Weil, Hendrik Wuerz
 */
public class Circle {

    private Language lang;
    private CoordinateSystemConfig config;
    private double x;
    private double y;
    private double radius;
    private String name;
    private boolean isVisible;
    private Color fillColor = Color.LIGHT_GRAY;

    // The radius of a circle used to draw a single dot
    private static final double DOT_RADIUS = 0.02;

    // Used for unique names if multiple circles are generated
    private int generatedElementCounter;

    // An identifier which can be set from outside to specific which circle is stored in this object
    private int identifier = -1;

    // The matrix which contains the radius of the circle
    private DoubleMatrix radiusMatrix;
    private int radiusMatrixRow = -1;

    private algoanim.primitives.Circle animalCircle;

    Circle(Language lang, CoordinateSystemConfig config, double x, double y, double radius, String name) {
        this(lang, config, x, y, radius, name, true);
    }

    Circle(Language lang, CoordinateSystemConfig config, double x, double y, double radius, String name, boolean isVisible) {
        this.lang = lang;
        this.config = config;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.name = name;
        this.isVisible = isVisible;

        generatedElementCounter = 0;

        draw();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    /**
     * Sets the reference to the radius matrix to be able to highlight the cell later
     * @param radiusMatrix The animal object for the matrix
     * @param radiusMatrixRow The row in the matrix
     */
    public void setRadiusMatrix(DoubleMatrix radiusMatrix, int radiusMatrixRow) {
        this.radiusMatrix = radiusMatrix;
        this.radiusMatrixRow = radiusMatrixRow;
    }

    /**
     * Get the real position of this circle in global animal coordinates
     * @return The real (= global) position
     */
    public Node getRealPosition() {
        return animalCircle.getCenter();
    }

    private void draw() {
        CircleProperties circleProperties = new CircleProperties(config.getPrefix() + name + generatedElementCounter + "Properties");
        circleProperties.set("fillColor", fillColor);
        circleProperties.set("filled", true);

        Hidden hidden = isVisible ? null : new Hidden();

        animalCircle = lang.newCircle(
                config.toPixel(x, y),
                Math.max(1, config.toPixel(Math.max(DOT_RADIUS, radius))),
                config.getPrefix() + name + generatedElementCounter,
                hidden,
                circleProperties);
    }

    public double getRadius() {
        return radius;
    }

    /**
     * Changes the used coordinate system to the passed config
     * Moves this circle to the new position.
     * Animates the movement with the passed duration.
     * @param config The new config on which positions are calculate
     * @param duration Duration of the movement animation
     */
    public void moveTo(CoordinateSystemConfig config, Timing duration) {
        this.config = config;
        moveTo(x, y, duration);
    }

    /**
     * Moves this circle to the new position.
     * Animates the movement with the passed duration.
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @param duration Duration of the movement animation
     */
    public void moveTo(double x, double y, Timing duration) {
        this.x = x;
        this.y = y;

        // calculate destination
        Coordinates realPosition = config.toPixel(x, y);
        int realRadius = config.toPixel(radius);
        realPosition = new Coordinates(realPosition.getX() - realRadius, realPosition.getY() - realRadius);

        // Attention: The direction seams not to work correctly. (No changes for different parameters)
        // At the moment the calculation of realPosition works for the behaviour of animal.
        // This might change in future.
        // Valid directions
        // NW | N | NE | W | C | E | SW | S | SE
        animalCircle.moveTo("C", "translate", realPosition, Timing.INSTANTEOUS, duration);
        //repaint();
    }

    /**
     * Changes the radius of the circle to the passed value
     * @param radius The new radius
     */
    public void resize(double radius, Timing delay) {
        this.radius = radius;
        repaint(delay);
    }

    /**
     * Filles the circle with the passed color
     * @param color The new fill color
     */
    public void fillColor(Color color) {
        fillColor = color;
        animalCircle.changeColor("fillColor", color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    }

    /**
     * Hides the circle after the passed delay
     * @param delay Delay before hiding
     */
    public void hide(Timing delay) {
        isVisible = false;
        animalCircle.hide(delay);
    }

    /**
     * Shows the circle after the passed delay
     * @param delay Delay before display
     */
    public void show(Timing delay) {
        isVisible = true;
        animalCircle.show(delay);
    }

    /**
     * Duplicates
     */
    public Circle duplicate() {
        Circle duplicate = new Circle(lang, config, x, y, radius, name + generatedElementCounter, isVisible);
        duplicate.fillColor(fillColor);
        duplicate.setIdentifier(identifier);
        duplicate.setRadiusMatrix(radiusMatrix, radiusMatrixRow);
        return duplicate;
    }

    /**
     * removes the current circle and creates a new one based on the current settings
     */
    private void repaint(Timing delay) {
        hide(delay);
        draw();
        show(delay);
    }

    /**
     * Checks whether this circle touches the passed circle
     * @param other The other circle which should be checked for collision
     * @return true if this touches the other, false otherwise
     */
    public boolean touches(Circle other) {
        double distance = Math.sqrt(
                Math.pow(getX() - other.getX(), 2) +
                        Math.pow(getY() - other.getY(), 2)
        );
        return distance <= getRadius() + other.getRadius();
    }

    /**
     * Removes the highlighting from the radius matrix.
     */
    public void unHighlightRadius() {
        highlightRadius(Color.white);
    }

    /**
     * Highlights the radius matrix with the passed color.
     * @param color The color which should be used for highlighting
     */
    public void highlightRadius(Color color) {
        if(radiusMatrix != null && radiusMatrixRow >= 0) {
            radiusMatrix.setGridFillColor(radiusMatrixRow, 0, color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        } else {
            throw new RuntimeException("setRadiusMatrix must be called before this function can be used");
        }
    }
}
