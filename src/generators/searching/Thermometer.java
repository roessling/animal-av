package generators.searching;

import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import translator.Translator;

import java.awt.*;
import java.util.ResourceBundle;

public class Thermometer {
    private Language language;
    private Coordinates center;
    private int width;
    private double initialTemperatur;

    private Rect tempRect;
    private int zeroY;
    private int maxY;
    private Text currentTempText;
    private Color fillColor;
    private Color borderColor;
    private Translator translator;

    Primitive[] allElements = new Primitive[10];

    public Thermometer(Language lang, Coordinates start, int width, double initialTemperatur, RectProperties props, Translator translator) {
        this.language = lang;
        this.center = start;
        this.width = width;
        this.initialTemperatur = initialTemperatur;
        this.fillColor = (Color) props.get(AnimationPropertiesKeys.FILL_PROPERTY);
        this.borderColor = (Color) props.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        this.translator = translator;

    }

    public void adjustTemperature(double newTemp) {
        double newTempDisplay = Math.exp(Math.exp(Math.pow(newTemp, 0.1)));
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fillColor);
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, fillColor);
        int newY = (int) (maxY + ((initialTemperatur - newTempDisplay) * ((zeroY - maxY) / initialTemperatur)));
        Rect newTempRect = language.newRect(new Coordinates((int) (center.getX() - (width * (3.0 / 5)) / 2), newY), new Coordinates((int) (center.getX() + (width * (3.0 / 5)) / 2), (int) (center.getY() - (width / 1.428))), "", null, rectProps);

        tempRect.changeColor("", Color.BLUE, null, null);
        tempRect.hide();
        currentTempText.setText(String.format(translator.translateMessage("thermometer.currentTemp"), newTemp), null, null);
        tempRect = newTempRect;
    }

    public void drawThermometer() {

        CircleProperties circleProps = new CircleProperties();
        circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fillColor);
        circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, fillColor);

        Circle circle = language.newCircle(center, width, "test", null, circleProps);
        allElements[0] = circle;

        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, fillColor);
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, fillColor);
        tempRect = language.newRect(new Coordinates((int) (center.getX() - (width * (3.0 / 5)) / 2), (int) (center.getY() - width * 4.4)), new Coordinates((int) (center.getX() + (width * (3.0 / 5)) / 2), (int) (center.getY() - (width / 1.428))), "", null, rectProps);
        allElements[1] = tempRect;

        maxY = (int) (center.getY() - width * 4.4);
        zeroY = center.getY() - width;

        PolylineProperties lineProps = new PolylineProperties();
        lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.borderColor);
        Node[] nodes = new Coordinates[2];
        nodes[0] = new Coordinates((int) (center.getX() - (width * (4.4 / 5)) / 2), (int) (center.getY() - width * 4.3));
        nodes[1] = new Coordinates((int) (center.getX() - (width * (4.4 / 5)) / 2), (int) (center.getY() - (width / 0.9)));
        Polyline line1 = language.newPolyline(nodes, "", null, lineProps);
        allElements[2] = line1;

        nodes[0] = new Coordinates((int) (center.getX() + (width * (4.4 / 5)) / 2), (int) (center.getY() - width * 4.3));
        nodes[1] = new Coordinates((int) (center.getX() + (width * (4.4 / 5)) / 2), (int) (center.getY() - (width / 0.9)));
        Polyline line2 = language.newPolyline(nodes, "", null, lineProps);
        allElements[3] = line2;

        ArcProperties circleSegProps = new ArcProperties();
        circleSegProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.borderColor);
        circleSegProps.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 180);

        int seqWith = (int) (width * (4.4 / 5) / 2);
        Arc seq = language.newArc(new Coordinates(center.getX(), (int) (center.getY() - width * 4.3)), new Coordinates(seqWith, seqWith), "test", null, circleSegProps);
        allElements[4] = seq;

        ArcProperties arcProps = new ArcProperties();
        arcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.borderColor);
        arcProps.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 316);
        arcProps.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 112);

        Arc arc = language.newArc(center, new Coordinates((int) (width * (6.0 / 5)), (int) (width * (6.0 / 5))), "test", null, arcProps);
        allElements[5] = arc;

        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced", Font.PLAIN, (int) (width / 3.0)));

        currentTempText = language.newText(new Coordinates(center.getX() - width, center.getY() + (int) (width * (6.0 / 5)) + 10),
                String.format(translator.translateMessage("thermometer.currentTemp"), initialTemperatur), "", null, textProps);

        allElements[6] = currentTempText;
    }

    public void hide() {
        for (Primitive elem : allElements) {
            if (elem != null) {
                elem.hide();
            }
        }
        tempRect.hide();
    }
}
