package generators.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Stack;

import algoanim.animalscript.AnimalGroupGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Tower extends Stack<Integer> {

  private Language lang;
  // private Group tower;
  private Text     textObj;

  private Text     fromText, toText;

  private String   label;
  private int      index;           // 0, 1 oder 2 ( wichtig für gerade und
                                     // ungerade )

  public Tower(Language l, String lbl, int ind, Color tc) {
    super();
    lang = l;
    label = lbl;
    index = ind;

    createTower(tc);
  }

  public int getIndex() {
    return this.index;
  }

  public String getLabel() {
    return label;
  }

  public int nextClockwise() {
    return (index + 1) % 3;
  }

  public int nextCounterClockwise() {
    return (index + 2) % 3;
  }

  private void createTower(Color towerColor) {
//    int l = 10;
    int w = 20;
    int m = 5;

    int X = 640, Y = 420;

    int xs = X, ys = Y, xp;

    if (label.equals("B"))
      xs = X + 120 + 40;
    else if (label.equals("C"))
      xs = X + (120 + 40) * 2;

    xp = (xs + 120 - xs - m) / 2 + xs;

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, towerColor);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, towerColor);

    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    Rect rh = lang.newRect(new Coordinates(xs, ys), new Coordinates(xs + 120,
        ys + w), label, null, rectProps);
    Rect rv = lang.newRect(new Coordinates(xp, ys - 165), new Coordinates(xp
        + m, ys), label, null, rectProps);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 50));

    textObj = lang.newText(new Offset(45, 20, rh, AnimalScript.DIRECTION_SW),
        label, label, null, textProps);

    LinkedList<Primitive> prms = new LinkedList<Primitive>();
    prms.add(rh);
    prms.add(rv);
    prms.add(textObj);

    new Group(new AnimalGroupGenerator(lang), prms, null);

    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    fromText = lang.newText(new Offset(-45, -70, rv, AnimalScript.DIRECTION_N),
        "from", "from", null, textProps);
    fromText.hide();

    toText = lang.newText(new Offset(-15, -70, rv, AnimalScript.DIRECTION_N),
        "to", "to", null, textProps);
    toText.hide();
  }

  public void changeLabel(String newLbl) {
    textObj.setText(newLbl, null, null);
  }

  public void hideFrom() {
    fromText.hide();
  }

  public void showFrom() {
    fromText.show();
  }

  public void hideTo() {
    toText.hide();
  }

  public void showTo() {
    toText.show();
  }
}
