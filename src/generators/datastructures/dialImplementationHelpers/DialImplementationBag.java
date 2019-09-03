package generators.datastructures.dialImplementationHelpers;

import algoanim.primitives.Arc;
import algoanim.primitives.Circle;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;

@SuppressWarnings("unused")
public class DialImplementationBag {

    private Circle dialCircle;

    private Text text;

    private StringArray array;

    private Offset center;

    public DialImplementationBag(Circle dialCircle, Text text, StringArray array) {
        this.dialCircle = dialCircle;
        this.text = text;
        this.array = array;
        this.center = (Offset) dialCircle.getCenter();
    }

    Circle getOuterCircle() {
        return this.dialCircle;
    }

    Text getText() {
        return this.text;
    }

    StringArray getArray() {
        return this.array;
    }

    Offset getCenter() {
        return this.center;
    }

    void changeText(int number, int key, int duration) {
        this.text.setText(Integer.toString(number) + " * ", new MsTiming(duration), null);
        this.array.put(0, key != -1 ? Integer.toString(key) : " ", new MsTiming(duration), null);
    }

    void hide() {
        this.array.hide();
        this.text.hide();
        this.dialCircle.hide();
    }

    int moveVia(Arc arc) {
        int angle = (int) arc.getProperties().get(AnimationPropertiesKeys.ANGLE_PROPERTY);
        Coordinates turnCenter = (Coordinates) arc.getCenter();
        /*if(this.center.getX() > 0)
            angle = 360 - angle;*/
        //System.out.println((int) Math.round((this.center.getX() - turnCenter.getX()) * Math.cos(Math.toRadians(angle)) - (this.center.getY() - turnCenter.getY()) * Math.sin(Math.toRadians(angle))));
        int x = (int) Math.round(this.center.getX() * Math.cos(Math.toRadians(angle)) - this.center.getY() * Math.sin(Math.toRadians(angle)));
        int y = (int) Math.round(this.center.getX() * Math.sin(Math.toRadians(angle)) + this.center.getY() * Math.cos(Math.toRadians(angle)));
        this.center = new Offset(x, y, this.center.getRef(), null);
        int duration = 4000 / 360 * angle;
        this.dialCircle.moveVia(null, null, arc, Timing.FAST, new MsTiming(duration));
        this.text.moveVia(null, null, arc, Timing.FAST, new MsTiming(duration));
        this.array.moveVia(null, null, arc, Timing.FAST, new MsTiming(duration));

        return duration + 250;
    }

    int moveVia(Arc arc, int timeOffset) {
        int angle = (int) arc.getProperties().get(AnimationPropertiesKeys.ANGLE_PROPERTY);
        Coordinates turnCenter = (Coordinates) arc.getCenter();
        /*if(this.center.getX() > 0)
            angle = 360 - angle;*/
        //System.out.println((int) Math.round((this.center.getX() - turnCenter.getX()) * Math.cos(Math.toRadians(angle)) - (this.center.getY() - turnCenter.getY()) * Math.sin(Math.toRadians(angle))));
        int x = (int) Math.round(this.center.getX() * Math.cos(Math.toRadians(angle)) - this.center.getY() * Math.sin(Math.toRadians(angle)));
        int y = (int) Math.round(this.center.getX() * Math.sin(Math.toRadians(angle)) + this.center.getY() * Math.cos(Math.toRadians(angle)));
        this.center = new Offset(x, y, this.center.getRef(), null);
        int duration = 4000 / 360 * angle;
        this.dialCircle.moveVia(null, null, arc, new MsTiming(timeOffset + 250), new MsTiming(duration));
        this.text.moveVia(null, null, arc, new MsTiming(timeOffset + 250), new MsTiming(duration));
        this.array.moveVia(null, null, arc, new MsTiming(timeOffset + 250), new MsTiming(duration));

        return duration + 250 + timeOffset;
    }
}
