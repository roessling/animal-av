package generators.maths.gerschgorin.coordinatesystem;

import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Polygon;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The grid of a coordinate system.
 * @author Jannis Weil, Hendrik Wuerz
 */
class Grid {

    /**
     * The width of a axis description text.
     */
    static final int TEXT_WIDTH = 20;

    /**
     * The height of an axis description text.
     */
    static final int TEXT_HEIGHT = 20;

    /**
     * The concrete language object used for creating output
     */
    private Language lang;

    /**
     * The used configuration to draw the grid.
     */
    private CoordinateSystemConfig config;

    /**
     * The polygon for the axis.
     */
    private Polygon polygon;

    /**
     * All axis description texts.
     */
    private List<GridText> gridTexts;

    /**
     * Default constructor
     *
     * @param lang The concrete language object used for creating output
     */
    Grid(Language lang, CoordinateSystemConfig config) {
        this.lang = lang;
        this.config = config;

        draw();
    }

    /**
     * Draws this grid based on the current configuration.
     */
    private void draw() {

        // The nodes of the grid
        Node[] nodes = new Node[] {
                new Coordinates(config.getTopLeftX(), config.getTopLeftY()),
                new Coordinates(config.getTopLeftX(), config.getTopLeftY() + config.getHeight()),
                new Coordinates(config.getTopLeftX() + config.getWidth(), config.getTopLeftY() + config.getHeight())
        };
        try {
            polygon = lang.newPolygon(addReverse(nodes), config.getPrefix() + "grid", null);
        } catch (NotEnoughNodesException e) {
            e.printStackTrace();
        }

        // Generate text elements on the axis of this grid
        gridTexts = Arrays.stream(GridTextType.values())
                .map(type -> new GridText(lang, type, config))
                .collect(Collectors.toList());
    }

    /**
     * Moves this grid to the position specified in the passed configuration.
     * @param config The new coordinate system configuration, containing the position.
     * @param duration The duration of the movement animation.
     */
    void moveTo(CoordinateSystemConfig config, Timing duration) {
        Node position = new Coordinates(config.getTopLeftX(), config.getTopLeftY());
        polygon.moveTo("C", "translate", position, Timing.INSTANTEOUS, duration);
        gridTexts.forEach(gridText -> gridText.applyNewConfig(config, duration));
    }

    /**
     * Adds the reverse of the passed array behind it (without first and last element)
     * Example: addReverse([A, B, C, D, E]) == [A, B, C, D, E, D, C, B]
     * Use this for drawing lines, using the animal polygon type.
     * (Normally the figure is destroyed by the last line from end back to start)
     * @param nodes The nodes of the figure
     * @return The nodes added with their reverse
     */
    private Node[] addReverse(Node[] nodes) {
        if(nodes.length < 3) return nodes;
        Node[] result = new Node[nodes.length * 2 - 2];
        System.arraycopy(nodes, 0, result, 0, nodes.length);
        int counter = 0; // counter for the current array index in result
        for(int i = nodes.length - 2; i > 0; i--) {
            result[nodes.length + counter] = nodes[i];
        }
        return result;
    }
}
