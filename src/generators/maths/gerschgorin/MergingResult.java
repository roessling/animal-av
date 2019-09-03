package generators.maths.gerschgorin;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Offset;
import generators.maths.gerschgorin.coordinatesystem.Circle;
import generators.maths.gerschgorin.coordinatesystem.CoordinateSystem;

import java.util.LinkedList;

/**
 * The result of merging the circles from the original matrix with the circles resulting from the transposed matrix.
 * @author Jannis Weil, Hendrik Wuerz
 */
class MergingResult {

    /**
     * All text nodes in correct order which print status information about the merging.
     */
    private LinkedList<Text> mergingInfos = new LinkedList<>();

    /**
     * The coordinate system containing the merged circles.
     */
    private CoordinateSystem coordinateSystem;

    /**
     * All merged circles in the coordinate system.
     */
    private Circle[] circles;

    /**
     * Initializes the merging info output texts. All further texts are placed below this one.
     * @param text The animal text node containing the info.
     * @throws RuntimeException If the list was already initialized.
     */
    void initMergingInfo(Text text) {
        if(!mergingInfos.isEmpty()) {
            throw new RuntimeException("You can not initializes a non empty list of merging infos.");
        }
        mergingInfos.add(text);
    }

    /**
     * Prints a text below the last merging info text.
     * There has to be a printed text. If there is none, an NoSuchElementException will be thrown.
     * @param info The new status text containing merging information.
     * @throws java.util.NoSuchElementException If no text was already added manual. (Needed for position)
     */
    void addMergingInfo(Language lang, String info) {
        Text text = lang.newText(new Offset(0, 0, mergingInfos.getLast(), "SW"),
                info, "mergingInfo" + mergingInfos.size(), null);
        mergingInfos.add(text);
    }

    /**
     * Get all registered merging infos.
     * @return The animal objects of the merging infos in correct order.
     */
    LinkedList<Text> getMergingInfos() {
        return mergingInfos;
    }

    CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    Circle[] getCircles() {
        return circles;
    }

    void setCircles(Circle[] circles) {
        this.circles = circles;
    }
}
