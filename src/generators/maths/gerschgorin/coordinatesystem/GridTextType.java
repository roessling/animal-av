package generators.maths.gerschgorin.coordinatesystem;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;

import java.util.function.Function;

import static generators.maths.gerschgorin.coordinatesystem.Grid.TEXT_HEIGHT;
import static generators.maths.gerschgorin.coordinatesystem.Grid.TEXT_WIDTH;

/**
 * The different types of a grid axis description.
 * @author Jannis Weil, Hendrik Wuerz
 */
public enum GridTextType {

    /**
     * Text to print the maximum value on the y-axis.
     */
    VERTICAL_TOP(
            (CoordinateSystemConfig config) -> new Coordinates(config.getTopLeftX() - TEXT_WIDTH, config.getTopLeftY()),
            (CoordinateSystemConfig config) -> String.valueOf((int) (config.getStepsPerPixel() * config.getHeight() / 2)),
            (CoordinateSystemConfig config) -> config.getPrefix() + "coordinateSystemText1"
    ),

    /**
     * Point of origin text on the y-axis
     */
    VERTICAL_MIDDLE(
            (CoordinateSystemConfig config) -> new Coordinates(config.getTopLeftX() - TEXT_WIDTH, config.getTopLeftY() + config.getHeight() / 2 - TEXT_HEIGHT / 2),
            (CoordinateSystemConfig config) -> "0",
            (CoordinateSystemConfig config) -> config.getPrefix() + "coordinateSystemText2"
    ),

    /**
     * Text to print the minimum value on the y-axis.
     */
    VERTICAL_BOTTOM(
            (CoordinateSystemConfig config) -> new Coordinates(config.getTopLeftX() - TEXT_WIDTH, config.getTopLeftY() + config.getHeight() - TEXT_HEIGHT),
            (CoordinateSystemConfig config) -> String.valueOf((int) (config.getStepsPerPixel() * config.getHeight() / -2)),
            (CoordinateSystemConfig config) -> config.getPrefix() + "coordinateSystemText3"
    ),

    /**
     * Text to print the minimum value on the x-axis.
     */
    HORIZONTAL_LEFT(
            (CoordinateSystemConfig config) -> new Coordinates(config.getTopLeftX(), config.getTopLeftY() + config.getHeight()),
            (CoordinateSystemConfig config) -> "0",
            (CoordinateSystemConfig config) -> config.getPrefix() + "coordinateSystemText4"
    ),

    /**
     * Text to print the middle value on the x-axis.
     */
    HORIZONTAL_MIDDLE(
            (CoordinateSystemConfig config) -> new Coordinates(config.getTopLeftX() + config.getWidth() / 2, config.getTopLeftY() + config.getHeight()),
            (CoordinateSystemConfig config) -> String.valueOf((int) (config.getStepsPerPixel() * config.getWidth() / 2)),
            (CoordinateSystemConfig config) -> config.getPrefix() + "coordinateSystemText5"
    ),

    /**
     * Text to print the maximum value on the x-axis.
     */
    HORIZONTAL_RIGHT(
            (CoordinateSystemConfig config) -> new Coordinates(config.getTopLeftX() + config.getWidth(), config.getTopLeftY() + config.getHeight()),
            (CoordinateSystemConfig config) -> String.valueOf((int) (config.getStepsPerPixel() * config.getWidth())),
            (CoordinateSystemConfig config) -> config.getPrefix() + "coordinateSystemText6"
    );

    /**
     * Function to calculate the position of the text
     */
    private Function<CoordinateSystemConfig, Coordinates> coordinates;

    /**
     * Function to calculate the displayed text
     */
    private Function<CoordinateSystemConfig, String> text;

    /**
     * Function to calculate the unique name of the animal element
     */
    private Function<CoordinateSystemConfig, String> name;

    /**
     * Private enum constructor to store the calculation functions.
     *
     * @param coordinates Function to calculate the position of the text
     * @param text        Function to calculate the displayed text
     * @param name        Function to calculate the unique name of the animal element
     */
    GridTextType(Function<CoordinateSystemConfig, Coordinates> coordinates,
                 Function<CoordinateSystemConfig, String> text,
                 Function<CoordinateSystemConfig, String> name) {
        this.coordinates = coordinates;
        this.text = text;
        this.name = name;
    }

    /**
     * Get the pixel coordinates of this text assuming the passed configuration.
     *
     * @param config The current coordinate system configuration.
     * @return The pixel coordinates of this text.
     */
    public Coordinates getCoordinates(CoordinateSystemConfig config) {
        return coordinates.apply(config);
    }

    /**
     * Generates a new animal text node based on the passed configuration.
     *
     * @param lang   The animal language where the node should be added.
     * @param config The configuration of the coordinate system where the text should be displayed.
     * @return The animal text element.
     */
    public Text generateTextNode(Language lang, CoordinateSystemConfig config) {
        return lang.newText(coordinates.apply(config), text.apply(config), name.apply(config), null);
    }

}
