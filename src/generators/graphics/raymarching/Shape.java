package generators.graphics.raymarching;

import algoanim.primitives.generators.Language;

import java.awt.*;

public abstract class Shape {
    Color color;
    public abstract double signedDistance(Vector2D p);
    public abstract void place(Language lang);
    public abstract void hide();
}
