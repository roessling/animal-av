package generators.maths.gerschgorin.coordinatesystem;

import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import algoanim.util.Timing;

import java.util.LinkedList;

/**
 * A coordinate system to visualize the gerschgorin circles.
 * @author Jannis Weil, Hendrik Wuerz
 */
public class CoordinateSystem {

    /**
     * The concrete language object used for creating output.
     */
    private Language lang;

    /**
     * The configuration containing infos for the object position.
     */
    private CoordinateSystemConfig config;

    /**
     * The grid of this coordinate system.
     */
    private Grid grid;

    /**
     * All circles in this coordinate system.
     */
    private LinkedList<Circle> circles = new LinkedList<>();

    /**
     * Default constructor
     *
     * @param lang The concrete language object used for creating output
     * @param config The config used to draw this coordinate system (sizes, position, ranges)
     */
    public CoordinateSystem(Language lang, CoordinateSystemConfig config) {
        this.lang = lang;
        this.config = config;
        this.grid = new Grid(lang, config);
    }

    public Circle drawCircle(double x, double y, double radius, String name) {
        return drawCircle(x, y, radius, name, true);
    }

    public Circle drawCircle(double x, double y, double radius, String name, boolean isVisible) {
        Circle circle = new Circle(lang, config, x, y, radius, name, isVisible);
        circles.add(circle);
        return circle;
    }

    /**
     * Moves the coordinate system and all of its components to the new position.
     * @param coordinates The new position where the coordinate system should be placed.
     * @param duration The duration of the movement animation
     */
    public void moveTo(Coordinates coordinates, Timing duration) {
        config.setPosition(coordinates);

        // Move grid and circles
        grid.moveTo(config, duration);
        circles.forEach(circle -> circle.moveTo(config, duration));
    }
}
