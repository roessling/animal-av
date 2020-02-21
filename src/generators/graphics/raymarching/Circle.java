package generators.graphics.raymarching;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.util.MsTiming;

import java.awt.*;

public class Circle extends Shape{
    Vector2D center;
    double radius;
    boolean fill;
    algoanim.primitives.Circle element;

    public Circle(Vector2D center, double radius, Color color, boolean fill){
        this.center = center;
        this.radius = radius;
        this.fill = fill;
        this.color = color;
    }

    public void place(Language lang){
        CircleProperties circleProps = new CircleProperties();
        circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,color);
        circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,fill);
        circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY,color);
        circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY,3);
        element = lang.newCircle(center.toNode(),(int)radius,"",null,circleProps);
    }

    public void hide(){
        element.hide();
    }

    public void growRadius(double target,int duration){
        element.moveBy("translateRadius", (int)(target),0,null, new MsTiming(duration));
        radius = target;
    }

    public double signedDistance(Vector2D p){
        return p.add(-1,center).length() - radius;
    }
}
