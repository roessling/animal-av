package generators.maths.buffon;

import java.awt.Color;
import java.util.Random;
import java.util.Vector;

import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.properties.PropertiesBuilder;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Needle {

  private Random         r;
  private Vector<Double> sizes;
  private Vector<Double> angles;
  private Vector<Node>   cuts;
  private double         centerOffset;
  private int            centerPos;
  private Color          color;
  private int            index;
  private int            subindex;

  class DoublePair {

    public double x = 0;
    public double y = 0;

  }

  public Needle(int[] data, Color color, int index) {
    this.color = color;
    this.index = index;
    subindex = 0;
    sizes = new Vector<Double>();
    angles = new Vector<Double>();
    cuts = new Vector<Node>();
    r = new Random(System.currentTimeMillis() * index + this.hashCode());
    double sizeSum = 0;
    double sizeHalfsum = 0;
    boolean centerFound = false;
    for (int i = 0; i < data.length; i += 2) {
      if (data[i] == 0)
        break;
      else if (data[i] > 0) {
        sizeSum += data[i];
        sizes.add((double) data[i]);
      } else
        throw new IllegalArgumentException(
            "The needle length data is not allowed to contain negative numbers!");
    }
    for (int i = 0; i < sizes.size(); i++) {
      double size = sizes.get(i);
      if (!centerFound) {
        sizeHalfsum += size;
        if (sizeHalfsum >= sizeSum / 2) {
          centerPos = i;
          centerOffset = sizeHalfsum - sizeSum / 2;
          centerFound = true;
        }
      }
      sizes.set(i, size / (sizeSum * 6));
    }
    centerOffset = centerOffset / (sizeSum * 6);
    for (int i = 0; i < sizes.size() - 1; i++) {
      angles.add((double) data[2 * i + 1]);
    }
  }

  public Polyline throwNeedle(Language a) {
    subindex += 1;
    cuts.clear();
    Node[] nodes = new Node[sizes.size() + 1];
    DoublePair[] pairs = new DoublePair[sizes.size() + 1];
    double origX = r.nextDouble();
    double origY = r.nextDouble();
    double angle = r.nextDouble() * 180;
    pairs[centerPos] = nextPos(origX, origY, sizes.get(centerPos)
        - centerOffset, angle);
    pairs[centerPos + 1] = nextPos(origX, origY, centerOffset, 180 + angle);
    for (int i = centerPos - 1; i >= 0; i--) {
      pairs[i] = nextPos(pairs[i + 1].x, pairs[i + 1].y, sizes.get(i), angle
          - angles.get(i));
    }
    for (int i = centerPos + 2; i <= sizes.size(); i++) {
      pairs[i] = nextPos(pairs[i - 1].x, pairs[i - 1].y, sizes.get(i - 1),
          angle + angles.get(i - 2) + 180);
    }
    for (int i = 0; i < pairs.length; i++) {
      nodes[i] = createNode(pairs[i]);
      if (i > 0) {
        if (Math.ceil(pairs[i - 1].x * 3) != Math.ceil(pairs[i].x * 3)) {
          DoublePair cut = new DoublePair();
          if (pairs[i - 1].x < pairs[i].x) {
            cut.x = Math.ceil(pairs[i - 1].x * 3) / 3;
            cut.y = pairs[i - 1].y
                + ((cut.x - pairs[i - 1].x) / (pairs[i].x - pairs[i - 1].x))
                * (pairs[i].y - pairs[i - 1].y);
          } else {
            cut.x = Math.ceil(pairs[i].x * 3) / 3;
            cut.y = pairs[i - 1].y
                + ((cut.x - pairs[i - 1].x) / (pairs[i].x - pairs[i - 1].x))
                * (pairs[i].y - pairs[i - 1].y);
          }
          cuts.add(createNode(cut));
        }
      }
    }
    return a.newPolyline(nodes, "needle" + index + "_" + subindex, null,
        PropertiesBuilder.createPolylineProperties(color));
  }

  public Vector<Node> getCuts() {
    return cuts;
  }

  private DoublePair nextPos(double posX, double posY, double length,
      double angle) {
    DoublePair result = new DoublePair();
    result.x = posX + length * Math.cos(angle * (Math.PI / 180));
    result.y = posY + length * Math.sin(-angle * (Math.PI / 180));
    return result;
  }

  private static Node createNode(DoublePair pos) {
    return new Offset((int) Math.round(428 * pos.x),
        (int) Math.round(443 * pos.y), "plainBorder", "NW");
  }

}
