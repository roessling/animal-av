package generators.graphics.raymarching;

import algoanim.util.Coordinates;
import algoanim.util.Node;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Coordinates cord) {
        x = cord.getX();
        y = cord.getY();
    }

    public Node toNode() {
        return new Coordinates((int)x,(int)y);
    }

    public Vector2D add(double scale, Vector2D v){
        return new Vector2D(x+scale*v.x,y+scale*v.y);
    }
    public Vector2D add(Vector2D v){
        return new Vector2D(x+v.x,y+v.y);
    }

    public Vector2D scale(double scale) {
        return new Vector2D(x*scale,y*scale);
    }

    public double scalarMult(Vector2D v) {
        return x*v.x+y*v.y;
    }

    public double length(){
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize(){
        return scale(1/length());
    }

    public Vector2D rotate(double angle) {
        return new Vector2D(x * Math.cos(angle) - y * Math.sin(angle),x * Math.sin(angle) + y * Math.cos(angle));
    }

    public Vector2D map(BiFunction<Double,Double,Double> function,Vector2D v){
        return new Vector2D(function.apply(x,v.x),function.apply(y,v.y));
    }

    public Vector2D map(Function<Double,Double> function){
        return new Vector2D(function.apply(x),function.apply(y));
    }

    public Vector2D max(Vector2D v){
        return map(Math::max,v);
    }

    public Vector2D min(Vector2D v){
        return map(Math::min,v);
    }

    public Vector2D abs() {
        return map(Math::abs);
    }
}
