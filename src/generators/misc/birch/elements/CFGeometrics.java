package generators.misc.birch.elements;

import algoanim.primitives.Circle;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Text;
import generators.misc.birch.ClusterFeature;
import generators.misc.birch.Vector;

import java.awt.*;

public class CFGeometrics {
    private Circle center;
    private Text text;
    private Ellipse radius;

    public CFGeometrics(Circle center, Text text, Ellipse radius) {
        this.center = center;
        this.text = text;
        this.radius = radius;
    }

    public Circle getCenter() {
        return center;
    }

    public Text getText() {
        return text;
    }

    public Ellipse getRadius() {
        return radius;
    }

    public void showBasics(){
        text.show();
        center.show();
    }

    public void showRadius(){
        radius.show();
    }

    public void changeColorBasics(Color color){
        center.changeColor(null, color, null,null);
        text.changeColor(null, color,null,null);
        center.changeColor("fillColor", color, null,null);
    }

    public void hideBasics(){
        text.hide();
        center.hide();
    }

    public void hideRadius() {
        radius.hide();
    }

    public static CFGeometrics create(CoordinateSystem coordinateSystem, ClusterFeature clusterFeature) {
        Vector centroid = clusterFeature.getCentroid();

        Circle center = coordinateSystem.drawPoint(centroid.getX(), centroid.getY());
        center.hide();

        Text text = coordinateSystem.drawText(center, clusterFeature.getName());
        text.hide();

        Ellipse radius = coordinateSystem.drawCircle(centroid.getX(), centroid.getY(), clusterFeature.getRadius());
        radius.hide();

        return new CFGeometrics(center, text, radius);
    }
}
