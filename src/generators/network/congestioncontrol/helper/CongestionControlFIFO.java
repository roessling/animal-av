/*
 * AbstractCongestionFIFO.java
 * Felix Gail, Torben Carstens, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network.congestioncontrol.helper;

import algoanim.primitives.Rect;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

import java.awt.*;
import java.util.LinkedList;

public class CongestionControlFIFO {
    private static final float WARNING_FILL_POINT = 0.8f;
    private static final Color WARNING_COLOR = Color.ORANGE;
    private static final Color FULL_COLOR = Color.RED;
    private MatrixProperties properties;
    private Coordinates lowerRight;
    private Rect highlightRect;
    private int cellHeight;
    private LinkedList<CongestionControlPacket> inner;
    private StringMatrix matrix;
    private Coordinates upperLeft;
    private Language language;
    private int capacity;
    static int HEIGHT = 400;
    static int WIDTH = 70;

    CongestionControlFIFO(int capacity, Language language, Coordinates upperLeft) {
        inner = new LinkedList<>();
        this.capacity = capacity;
        this.language = language;
        this.upperLeft = upperLeft;
        cellHeight = HEIGHT / this.capacity;
        lowerRight = new Coordinates(upperLeft.getX() + WIDTH, upperLeft.getY() + capacity * cellHeight);

        Coordinates upperLeftRect = new Coordinates(upperLeft.getX() - 20, upperLeft.getY() - 15);
        // Cell is approx 25px high
        Coordinates lowerRight = new Coordinates(upperLeftRect.getX() + WIDTH + 52, upperLeft.getY() + (25 * capacity) + 20);
        RectProperties errorProperties = new RectProperties("error_properties");
        errorProperties.set(
                AnimationPropertiesKeys.FILL_PROPERTY,
                Color.WHITE
        );
        errorProperties.set(
                AnimationPropertiesKeys.FILLED_PROPERTY,
                true
        );
        errorProperties.set(
                AnimationPropertiesKeys.COLOR_PROPERTY,
                Color.DARK_GRAY
        );
        errorProperties.set(
                AnimationPropertiesKeys.DEPTH_PROPERTY,
                2
        );
        properties = new MatrixProperties("fifo_properties");
        highlightRect = language.newRect(upperLeftRect, lowerRight, "error_rectangle", null, errorProperties);
        generateMatrix();
        update();
    }

    private void generateMatrix() {
        properties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        properties.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
        properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        properties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        properties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        properties.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, Color.BLACK);
        properties.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, Color.BLACK);
        String[][] items = new String[capacity][1];
        for (int i = 0; i < capacity; i++) {
            items[i][0] = "";
        }

        matrix = language.newStringMatrix(upperLeft, items, "fifo", new MsTiming(0), properties);
    }

    public void update() {
        fillEmpty();
        fillOccupied();
        float filledPercent = ((float) size()) / capacity;
        if (filledPercent >= WARNING_FILL_POINT) {
            if (size() == capacity) {
                showFull();
            } else {
                showWarning();
            }
        } else {
            unhighlightAll();
        }
    }

    private void showWarning() {
        updateErrorRectangleColor(WARNING_COLOR);
    }

    private void showFull() {
        updateErrorRectangleColor(FULL_COLOR);
    }

    private void unhighlightAll() {
        updateErrorRectangleColor(Color.WHITE);
    }

    private void updateErrorRectangleColor(Color color) {
        highlightRect.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, color, null, null);
    }

    private void fillEmpty() {
        for (int row = 0; row < capacity - size(); row++) {
            matrix.setGridHighlightFillColor(row, 0, Color.WHITE, null, null);
            matrix.unhighlightCell(row, 0, null, null);
            matrix.put(row, 0, String.format("%18s", " "), null, null);
        }
    }

    private void fillOccupied() {
        int row = capacity - 1;
        for (CongestionControlPacket packet : inner) {
            matrix.setGridHighlightFillColor(row, 0, packet.getColor(), null, null);
            matrix.highlightCell(row, 0, null, null);
            matrix.put(row, 0, String.format("%1$7s%2$2d%1$7s", " ", packet.getId()), null, null);
            row--;
        }
    }

    public void enqueue(CongestionControlPacket element) throws CapacityReachedException {
        if (size() >= capacity) {
            throw new CapacityReachedException();
        }

        inner.add(element);
    }

    CongestionControlPacket dequeue() throws IndexOutOfBoundsException {
        return inner.remove(0);
    }

    CongestionControlPacket drop(int index) {
        return inner.remove(index);
    }

    public boolean isFull() {
        return size() == capacity;
    }

    public int size() {
        return inner.size();
    }

    public boolean isEmpty() {
        return inner.isEmpty();
    }
}

class CapacityReachedException extends RuntimeException {

}
