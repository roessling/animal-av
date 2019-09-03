package generators.helpers;

import java.awt.Color;
import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;

public class MultilineText implements RelativeText {

  private final ArrayList<RelativeText> children       = new ArrayList<RelativeText>();
  private final TextGenerator           tg;
  private final TextProperties          tp;
  private final int                     lineSpace;
  private final String                  name;
  private final String                  text;
  private final static String           TABSPACE       = "    ";

  private int                           activeChildren = 0;

  private Color                         color;

  public MultilineText(TextGenerator tg, Node upperLeftCorner, String theText,
      String name, TextProperties tp, int lineSpace) {
    this.tg = tg;
    this.name = name;
    this.tp = tp;
    this.lineSpace = lineSpace;

    this.text = theText;

    this.children.add(new RelativeTextImpl(this.tg, new OffsetCoords(
        upperLeftCorner, 0, 0), theText, this.name + ":" + 0, null, this.tp));

    this.setText(theText, null, null);
  }

  @Override
  public void moveBy(String moveType, int dx, int dy, Timing delay,
      Timing duration) {

    int x = this.getX();
    int y = this.getY();

    for (int i = 0; i < this.activeChildren; i++) {
      RelativeText rt = this.children.get(i);

      rt.moveBy(moveType, (x - rt.getX()) + dx,
          ((y + i * this.lineSpace) - rt.getY()) + dy, delay, duration);
    }
  }

  @Override
  public void changeColor(String colorType, Color newColor, Timing t, Timing d) {
    for (RelativeText rt : this.children) {
      rt.changeColor(colorType, newColor, t, d);
    }

    this.color = newColor;
  }

  @Override
  public void setText(String newText, Timing delay, Timing duration) {
    String newText2 = newText;
    newText2 = newText2.replaceAll("\\$T", TABSPACE);
    String[] strings = newText2.split("\\$N");

    for (int i = this.children.size(); i < strings.length; i++) {
      RelativeText rt = new RelativeTextImpl(this.tg, new OffsetCoords(
          this.getUpperLeft(), 0, i * this.lineSpace), "", this.name + ":" + i,
          null, this.tp);
      this.children.add(rt);

      if ((this.color != null) && !this.tp.get("color").equals(this.color)) {
        rt.changeColor(AnimalScript.COLORCHANGE_COLOR, this.color, null, null);
      }
    }

    for (int i = 0; i < strings.length; i++) {
      this.children.get(i).setText(strings[i], delay, duration);
    }

    for (int i = strings.length; i < this.activeChildren; i++) {
      this.children.get(i).hide();
    }

    this.activeChildren = strings.length;
  }

  public void normalizeText(Timing delay, Timing duration) {
    this.setText(this.text, delay, duration);
  }

  public void rotateText(Timing delay, Timing duration) {
    StringBuilder sb = new StringBuilder();
    for (char c : this.text.toCharArray()) {
      sb.append(c).append("$N");

    }

    this.setText(sb.toString(), delay, duration);
  }

  public int getHeight() {
    return this.activeChildren * this.lineSpace - this.lineSpace;
  }

  @Override
  public Node getUpperLeft() {
    return this.children.get(0).getUpperLeft();
  }

  @Override
  public int getWidth() {
    int max = 0;

    for (int i = 0; i < this.activeChildren; i++) {

      if (this.children.get(i).getWidth() > max) {
        max = this.children.get(i).getWidth();
      }
    }

    return max;
  }

  @Override
  public int getX() {
    return this.children.get(0).getX();
  }

  @Override
  public int getY() {
    return this.children.get(0).getY();
  }

  @Override
  public void moveTo(String direction, String moveType, Node target,
      Timing delay, Timing duration) {
    this.moveTo(target, delay, duration);
  }

  @Override
  public void moveTo(Node target, Timing delay, Timing duration) {

    int x = ((Coordinates) target).getX() - this.getX();
    int y = ((Coordinates) target).getY() - this.getY();

    this.moveBy("translate", x, y, delay, duration);
  }

  @Override
  public void hide() {
    this.hide(null);
  }

  public void show() {
    for (RelativeText child : this.children) {
      child.show();
    }
  }

  public void show(Timing delay) {
    for (RelativeText child : this.children) {
      child.show(delay);
    }
  }

  @Override
  public String getText() {
    StringBuilder sb = new StringBuilder();
    for (RelativeText child : this.children) {
      sb.append(child.getText());
    }
    return sb.toString();
  }

  @Override
  public void hide(Timing t) {
    for (RelativeText child : this.children) {
      child.hide(t);
    }
  }

  @Override
  public void setX(int x) {
  }

  @Override
  public void setY(int y) {
  }

  @Override
  public void moveVia(String direction, String moveType, Primitive via,
      Timing delay, Timing duration) {
    for (int i = 0; i < this.children.size(); i++) {
      RelativeText rt = this.children.get(i);

      rt.moveVia(direction, moveType, via, delay, duration);
    }
  }

  @Override
  public TextProperties getProperties() {
    return this.tp;
  }
}