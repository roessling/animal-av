package generators.graphics.raymarching;

import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Polygon;
import algoanim.primitives.generators.Language;
import algoanim.properties.PolygonProperties;
import algoanim.util.Node;

import java.util.Arrays;

public class Camera {
    Vector2D position;
    double angle;
    PolygonProperties properties;
    Polygon element;
    Vector2D[] points = {new Vector2D(-2,1),new Vector2D(0,1)
            ,new Vector2D(1,2),new Vector2D(1,-2)
            ,new Vector2D(0,-1),new Vector2D(-2,-1)};
    double scale = 10;

    public Camera (Vector2D pos, double angle, PolygonProperties properties){
        position = pos;
        this.angle = angle;
        this.properties = properties;
    }

    public void place(Language lang) {
        Node[] nodes = Arrays.stream(points).map(v -> v.scale(scale).rotate(Math.toRadians(angle)).add(position).toNode()).toArray(Node[]::new);
        try {
            element = lang.newPolygon(nodes,"",null,properties);
        }catch (NotEnoughNodesException e){
            System.err.println("Camera does not have enough nodes, that's impossible but ok");
        }
    }

    public void hide(){
        element.hide();
    }
}
