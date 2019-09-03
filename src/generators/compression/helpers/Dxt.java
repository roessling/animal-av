package generators.compression.helpers;

import algoanim.primitives.Circle;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;

import java.awt.Color;
import java.awt.Font;

import static algoanim.properties.AnimationPropertiesKeys.*;

public class Dxt {
    public final static int N = 4;
    public final static int NxN = N * N;
    public final static int PIXEL_SIZE = 60;
    public final static int TEXT_R_SIZE = 35;
    public final static int TEXT_G_SIZE = 42;
    public final static int TEXT_B_SIZE = 35;
    public final static int TEXT_CI_SIZE = 15;
    public final static int TEXT_AI_SIZE = 22;
    public final static int TEXT_A_SIZE = 57;
    public final static int R_SIZE = 5;
    public final static int G_SIZE = 6;
    public final static int B_SIZE = 5;

    public static void createTitleBox(Language lang, int x, int y, int width, String title) {
        TextProperties properties = new TextProperties();
        properties.set(FONT_PROPERTY, Font.decode("SansSerif Bold 24"));
        properties.set(DEPTH_PROPERTY, 0);
        lang.newText(new Coordinates(x + 10, y), title, "", null, properties);
        RectProperties rectProperties = new RectProperties();
        rectProperties.set(FILLED_PROPERTY, true);
        rectProperties.set(FILL_PROPERTY, new Color(140, 255, 255));
        rectProperties.set(DEPTH_PROPERTY, 1);
        lang.newRect(new Coordinates(x, y), new Coordinates(x + width, y + 34), "", null, rectProperties);
        RectProperties backgroundProperties = new RectProperties();
        backgroundProperties.set(FILLED_PROPERTY, true);
        backgroundProperties.set(FILL_PROPERTY, Color.GRAY);
        backgroundProperties.set(COLOR_PROPERTY, Color.GRAY);
        backgroundProperties.set(DEPTH_PROPERTY, 2);
        lang.newRect(new Coordinates(x + 5, y + 5), new Coordinates(x + width + 5, y + 39), "", null, backgroundProperties);
    }

    public static int checkColorRange(double color) {
        if (color < 0)
            return 0;
        if (color > 255)
            return 255;
        return (int) color;
    }

    public static IntMatrix createMatrix(Language lang, int x, int y, double[] data) {
        int[][] newData = new int[data.length][];
        for (int i = 0; i < data.length; i++)
            newData[i] = new int[]{(int) data[i]};
        return createMatrix(lang, x, y, newData);
    }

    public static IntMatrix createMatrix(Language lang, int x, int y, double[][] data) {
        int[][] newData = new int[data.length][];
        for (int i = 0; i < data.length; i++) {
            newData[i] = new int[data[i].length];
            for (int k = 0; k < data[i].length; k++) {
                newData[i][k] = (int) data[i][k];
            }
        }
        return createMatrix(lang, x, y, newData);
    }

    public static IntMatrix createMatrix(Language lang, int x, int y, int[][] data) {
        MatrixProperties properties = new MatrixProperties();
        properties.set(COLOR_PROPERTY, Color.DARK_GRAY);
        properties.set(GRID_STYLE_PROPERTY, "matrix");
        properties.set(GRID_ALIGN_PROPERTY, "right");
        return lang.newIntMatrix(new Coordinates(x, y), data, "", null, properties);
    }

    public static Circle createCircle(Language lang, int x, int y, int r, Color border, Color fill) {
        CircleProperties properties = new CircleProperties();
        properties.set(COLOR_PROPERTY, border);
        properties.set(FILL_PROPERTY, fill);
        properties.set(FILLED_PROPERTY, true);
        return lang.newCircle(new Coordinates(x, y), r, "", null, properties);
    }

    public static Square createSquare(Language lang, int x, int y, int size, Color border, Color fill) {
        SquareProperties properties = new SquareProperties();
        properties.set(COLOR_PROPERTY, border);
        properties.set(FILL_PROPERTY, fill);
        properties.set(FILLED_PROPERTY, true);
        return lang.newSquare(new Coordinates(x, y), size, "", null, properties);
    }

    public static Square createPixel(Language lang, int x, int y, int xOffset, int yOffset, Color color, Color displayColor) {
        int xLoc = xOffset + x * PIXEL_SIZE;
        int yLoc = yOffset + y * PIXEL_SIZE;
        int yTextOffset = 10;
        if (color.getAlpha() < 255)
            yTextOffset = 4;

        Square square = createSquare(lang, xLoc, yLoc, PIXEL_SIZE, displayColor, displayColor);
        Color contrastColor = getContrastColor(displayColor);
        createText(lang, xLoc + 20, yLoc + yTextOffset, String.format("%03d", color.getRed()), contrastColor);
        createText(lang, xLoc + 20, yLoc + yTextOffset + 12, String.format("%03d", color.getGreen()), contrastColor);
        createText(lang, xLoc + 20, yLoc + yTextOffset + 24, String.format("%03d", color.getBlue()), contrastColor);
        if (color.getAlpha() < 255)
            createText(lang, xLoc + 20, yLoc + yTextOffset + 36, String.format("%03d", color.getAlpha()), contrastColor);
        return square;
    }

    public static Text createText(Language lang, int x, int y, String text) {
        return createText(lang, x, y, text, Color.BLACK);
    }

    public static Text createText(Language lang, int x, int y, String text, Color color) {
        TextProperties properties = new TextProperties();
        properties.set(COLOR_PROPERTY, color);
        return lang.newText(new Coordinates(x, y), text, "", null, properties);
    }

    public static Color getContrastColor(Color color) {
        int yiq = (color.getRed() * 299 + color.getGreen() * 587 + color.getBlue() * 114) / 1000;
        if (yiq >= 128)
            return Color.BLACK;
        return Color.WHITE;
    }

    public static String formatBinary(int value, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    public static int findNearestColor(Color[] palette, Color color, boolean skipAlpha) {
        if (skipAlpha && color.getAlpha() < 255)
            return palette.length - 1;

        int shortestDistance = Integer.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < palette.length; i++) {
            int distance = getColorDistance(color, palette[i]);
            if (distance < shortestDistance) {
                index = i;
                shortestDistance = distance;
            }
        }

        return index;
    }

    public static int getColorDistance(Color color1, Color color2) {
        int redDiff = color2.getRed() - color1.getRed();
        int greenDiff = color2.getGreen() - color1.getGreen();
        int blueDiff = color2.getBlue() - color1.getBlue();

        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff;
    }

    public static int findNearestAlpha(int[] palette, Color color) {
        int shortestDistance = Integer.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < palette.length; i++) {
            int distance = Math.abs(color.getAlpha() - palette[i]);
            if (distance < shortestDistance) {
                index = i;
                shortestDistance = distance;
            }
        }

        return index;
    }

    /**
     * generates a random color
     *
     * @param alpha       if the color should have a random alpha
     * @param rangedAlpha if the alpha should be random in a range of 0% to 100% or just on or off. Only effective if alpha is set to true
     * @return the random color
     */
    public static Color randomColor(boolean alpha, boolean rangedAlpha) {
        if (alpha)
            return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), rangedAlpha ? (int) (Math.random() * 255) : Math.random() < 0.5 ? 0 : 255);
        else return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    public static Color premultiplyAlpha(Color c) {
        double alpha = c.getAlpha() / 255D;
        int back = 255 - c.getAlpha();
        return new Color((int) (c.getRed() * alpha) + back, (int) (c.getGreen() * alpha) + back, (int) (c.getBlue() * alpha) + back);
    }
}
