package generators.maths.gerschgorin.coordinatesystem;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Timing;

/**
 * Wrapper around animal text. This will allow to draw the axis texts in a diagram and move them around if the
 * matrix configuration changes.
 * @author Jannis Weil, Hendrik Wuerz
 */
class GridText {

    /**
     * The type of this text. This defines the content and position.
     */
    private GridTextType type;

    /**
     * The animal text object which is generated based on the type and the coordinate system configuaration
     */
    private Text text;

    /**
     * Generates a new text node based on the parameters.
     *
     * @param lang   The animal language reference to add the text.
     * @param type   The type of this text node. Used for content and relative position.
     * @param config The configuration of the coordinate system for positioning.
     */
    GridText(Language lang, GridTextType type, CoordinateSystemConfig config) {
        this.type = type;
        this.text = type.generateTextNode(lang, config);
    }

    /**
     * Applies the passed configuration.
     * Moves the text within the passed duration to the new position which is calculated based on the configuration.
     *
     * @param config   The new configuration of the coordinate system.
     * @param duration The duration of the movement.
     */
    void applyNewConfig(CoordinateSystemConfig config, Timing duration) {
        text.moveTo("C", "translate", type.getCoordinates(config), Timing.INSTANTEOUS, duration);
    }
}
