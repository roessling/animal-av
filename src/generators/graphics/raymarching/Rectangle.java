package generators.graphics.raymarching;

import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;

import java.awt.*;

public class Rectangle extends Shape {

    Vector2D center;
    Vector2D dimension;
    double angle;
    boolean fill;
    Rect element;

    public Rectangle(Vector2D center, Vector2D dimensions, double angle, Color color, boolean fill) {
        this.center = center;
        this.dimension = dimensions;
        this.angle = angle;
        this.fill = fill;
        this.color = color;
    }

    public void place(Language lang){
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,color);
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,fill);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,color);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY,3);

        element = lang.newRect(center.add(-1,dimension).toNode(),center.add(dimension).toNode(),"",null,rectProps);
        element.rotate(center.toNode(), (int) (angle),null,null);
    }

    public void hide(){
        element.hide();
    }

    public double signedDistance(Vector2D p) {
        Vector2D pt = p.add(-1,center).rotate(Math.toRadians(angle));
        Vector2D d = pt.abs().add(-1,dimension);
        return d.max(new Vector2D(0.0,0.0)).length() + Math.min(Math.max(d.x,d.y),0.0);
    }
}
