package generators.graphics.raymarching;

import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.properties.PolylineProperties;
import algoanim.util.MsTiming;
import algoanim.util.Node;

public class Ray {

    Vector2D start;
    Vector2D end;
    PolylineProperties properties;
    Polyline element;

    public Ray(Vector2D start, Vector2D end, PolylineProperties properties) {
        this.end = end;
        this.start = start;
        this.properties = properties;
    }

    public void place(Language lang) {
        element = lang.newPolyline(new Node[]{start.toNode(), end.toNode()},"",null,properties);
    }

    public void hide() {
        element.hide();
    }

    public void moveEnd(Vector2D target, int duration) {
        element.moveBy("translate #2",(int)Math.round(target.x-end.x),(int)Math.round(target.y-end.y),null, new MsTiming(duration));
        end = target;
    }
}
