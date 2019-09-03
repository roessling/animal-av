package generators.maths.gerschgorin.coordinatesystem;

import algoanim.util.Coordinates;

/**
 * All configuration data which are needed to draw a coordinate system.
 * @author Jannis Weil, Hendrik Wuerz
 */
public class CoordinateSystemConfig {

    private int topLeftX = 250; // pixel
    private int topLeftY = 20; // pixel
    private int width; // pixel
    private int height; // pixel
    private double maxX; // coordinate
    private double maxY; // coordinate
    private double stepsPerPixel;
    private String prefix;

    public CoordinateSystemConfig(double maxX, double maxY,
                                  int topLeftX, int topLeftY,
                                  int width, int height,
                                  String prefix) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.width = width;
        this.height = height;
        this.maxX = maxX;
        this.maxY = maxY;
        this.stepsPerPixel = calculateStepsPerPixel(width, height, maxX, maxY);
        this.prefix = prefix;
    }

    /**
     * Changes the position of the configuration.
     * Be careful when calling this function because using coordinate systems will not be updated automatically.
     *
     * @param coordinates The new coordinates of the top left corner
     */
    public void setPosition(Coordinates coordinates) {
        this.topLeftX = coordinates.getX();
        this.topLeftY = coordinates.getY();
    }

    private static double calculateStepsPerPixel(double width, double height, double maxX, double maxY) {
        // How many steps (one step is from one to two in the calculated coordinates of the circles) have to be
        // gone for one pixel. The scale of the X-Axis and the Y-Axis should be equal.
        // Use this for text calculation.
        double stepsPerPixelX = maxX / width;
        double stepsPerPixelY = maxY / (height / 2); // division by 2 because of the zero line in the middle
        return Math.max(stepsPerPixelX, stepsPerPixelY);
    }

    public int getTopLeftX() {
        return topLeftX;
    }

    public int getTopLeftY() {
        return topLeftY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getStepsPerPixel() {
        return stepsPerPixel;
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * Converts the passed x|y coordinates to pixel coordinates which can be used to place animal elements.
     *
     * @param x The x coordinate of the element.
     * @param y The y coordinate of the element.
     * @return Pixel coordinates in the global window.
     */
    public Coordinates toPixel(double x, double y) {
        return new Coordinates(
                (int) (topLeftX + x / stepsPerPixel),
                (int) (topLeftY + height / 2 - y / stepsPerPixel));
    }

    /**
     * Converts the passed length to pixels.
     *
     * @param length The length which should be mapped to pixels
     * @return The amount of pixels used to draw a line of the passed length.
     */
    public int toPixel(double length) {
        return (int) (length / stepsPerPixel);
    }
}
