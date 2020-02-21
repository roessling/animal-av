package generators.misc.birch.elements;

import algoanim.primitives.Polyline;
import algoanim.primitives.Text;

import java.awt.*;

public class NamedLine {
    private Polyline line;
    private Text text;

    public NamedLine(Polyline line, Text text) {
        this.line = line;
        this.text = text;
    }

    public void show() {
        line.show();
        text.show();
    }

    public void hide() {
        line.hide();
        text.hide();
    }

    public Polyline getLine() {
        return line;
    }

    public Text getText() {
        return text;
    }

    public void changeColor(Color color){
        line.changeColor(null,color,null,null);
        text.changeColor(null,color,null,null);
    }

    public static NamedLine create(CoordinateSystem coordinateSystem, float x1, float y1, float x2, float y2, String name) {
        Polyline line = coordinateSystem.drawLine(x1, y1, x2, y2);
        Text text = coordinateSystem.drawText(line, name);
        return new NamedLine(line, text);
    }
}
