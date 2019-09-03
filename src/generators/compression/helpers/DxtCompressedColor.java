package generators.compression.helpers;

import java.awt.Color;

public class DxtCompressedColor {
    public final static double UNCOMPRESSED_MAX = 255;

    private final int rSize;
    private final int gSize;
    private final int bSize;

    private final int rMax;
    private final int gMax;
    private final int bMax;

    private int r = 0;
    private int g = 0;
    private int b = 0;
    private int a = 0;

    public DxtCompressedColor(Color color) {
        this(Dxt.R_SIZE, Dxt.G_SIZE, Dxt.B_SIZE, color);
    }

    public DxtCompressedColor(int rSize, int gSize, int bSize, Color color) {
        this.rSize = rSize;
        this.gSize = gSize;
        this.bSize = bSize;

        this.rMax = (0xFFFFFFFF >>> (32 - rSize));
        this.gMax = (0xFFFFFFFF >>> (32 - gSize));
        this.bMax = (0xFFFFFFFF >>> (32 - bSize));

        setColor(color);
    }

    public void setColor(Color color) {
        r = (int) ((color.getRed() / UNCOMPRESSED_MAX) * rMax);
        g = (int) ((color.getGreen() / UNCOMPRESSED_MAX) * gMax);
        b = (int) ((color.getBlue() / UNCOMPRESSED_MAX) * bMax);
        a = color.getAlpha();
    }

    public Color getColor() {
        return new Color(
                (int) (((double) r / rMax) * UNCOMPRESSED_MAX),
                (int) (((double) g / gMax) * UNCOMPRESSED_MAX),
                (int) (((double) b / bMax) * UNCOMPRESSED_MAX),
                a
        );
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    @Override
    public String toString() {
        return String.format("%" + rSize + "s,%" + gSize + "s,%" + bSize + "s", Integer.toBinaryString(r), Integer.toBinaryString(g), Integer.toBinaryString(b))
                .replace(' ', '0');
    }
}
