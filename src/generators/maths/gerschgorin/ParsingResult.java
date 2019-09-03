package generators.maths.gerschgorin;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Text;
import generators.maths.gerschgorin.coordinatesystem.Circle;
import generators.maths.gerschgorin.coordinatesystem.CoordinateSystem;

/**
 * The result of parsing a matrix.
 * @author Jannis Weil, Hendrik Wuerz
 */
class ParsingResult {

    /**
     * The circles resulting from parsing a matrix.
     */
    private Circle[] circles;

    /**
     * The parsed matrix.
     */
    private Matrix matrix;

    /**
     * The animal object used to display the radius of the circles.
     */
    private DoubleMatrix radiusMatrix;

    /**
     * The caption over the matrix with the radius values.
     */
    private Text radiusMatrixText;

    /**
     * The used coordinate system to draw the circles.
     */
    private CoordinateSystem coordinateSystem;

    /**
     * Creates a new wrapper around all created elements while parsing a matrix.
     * @param circles The circles resulting from parsing a matrix.
     * @param matrix The parsed matrix.
     * @param radiusMatrix The animal object used to display the radius of the circles.
     * @param radiusMatrixText The caption over the matrix with the radius values.
     * @param coordinateSystem The used coordinate system to draw the circles.
     */
    ParsingResult(Circle[] circles, Matrix matrix, DoubleMatrix radiusMatrix, Text radiusMatrixText, CoordinateSystem coordinateSystem) {
        this.circles = circles;
        this.matrix = matrix;
        this.radiusMatrix = radiusMatrix;
        this.radiusMatrixText = radiusMatrixText;
        this.coordinateSystem = coordinateSystem;
    }

    Circle[] getCircles() {
        return circles;
    }

    Matrix getMatrix() {
        return matrix;
    }

    DoubleMatrix getRadiusMatrix() {
        return radiusMatrix;
    }

    Text getRadiusMatrixText() {
        return radiusMatrixText;
    }

    CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }
}
