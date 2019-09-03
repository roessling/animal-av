package algoanim.animalscript;

import java.awt.Color;
import java.awt.Font;
import java.util.Vector;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Square;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Generator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;
import algoanim.util.Timing;

/**
 * This class implements functionality which is shared by all AnimalScript
 * generators. Especially this applies to operations that can be performed on
 * all primitives.
 * 
 * @author Stephan Mehlhase
 */
public abstract class AnimalGenerator extends Generator {

  /**
   * Provides the given <code>Language</code> object to the Generator.
   * 
   * @param aLang
   *          the related Language object
   */
  public AnimalGenerator(Language aLang) {
    super(aLang);
  }

  /**
   * Creates the definition of a <code>Node</code> in AnimalScript.
   * 
   * @param n
   *          the node for which the code shall be created.
   * @return the code.
   */
  public static String makeNodeDef(Node n) {
    StringBuilder sb = new StringBuilder(40);
    if (n instanceof Offset) {
      Offset o = (Offset) n;
      int mode = o.getReferenceMode();
      if (mode == Offset.PRIMITIVE_REFERENCE || mode == Offset.ID_REFERENCE) {
        sb.append("offset (").append(o.getX()).append(", ").append(o.getY());
        sb.append(") from \"");
        if (mode == Offset.PRIMITIVE_REFERENCE)
          sb.append(o.getRef().getName());
        else
          sb.append(o.getBaseID());
        sb.append("\" ").append(o.getDirection());
      } else if (o.getReferenceMode() == Offset.NODE_REFERENCE) {
        Node baseNode = o.getNode();
        if (baseNode instanceof Coordinates) {
          Coordinates c = (Coordinates) baseNode;
          sb.append('(').append(o.getX() + c.getX()).append(", ");
          sb.append(o.getY() + c.getY()).append(')');
        } else { // offset from node...
          sb.append('(').append(o.getX()).append(", ").append(o.getY());
          sb.append(')');
          System.err.println("Offset based on an instance of "
              + baseNode.getClass().getName() + " not supported");
        }
      }
    } else if (n instanceof Coordinates) {
      Coordinates c = (Coordinates) n;
      sb.append('(').append(c.getX()).append(", ");
      sb.append(c.getY()).append(')');
    } else if (n instanceof OffsetFromLastPosition) {
      OffsetFromLastPosition oflp = (OffsetFromLastPosition)n;
      sb.append("move (").append(oflp.getX()).append(", ");
      sb.append(oflp.getY()).append(')');      
    } else {
      // nothing to be done here
    }
    return sb.toString();
  }

  /**
   * Creates a color definition in AnimalScript for the given values of red,
   * green and blue. All values must be between 0 and 255. If not, the values
   * are calculated by modulo operation to fit.
   * 
   * @param r
   *          the red part of the color (must between 0 - 255)
   * @param g
   *          the green part of the color (must between 0 - 255)
   * @param b
   *          the blue part of the color (must between 0 - 255)
   * @return the String description for the color passed in
   */
  public static String makeColorDef(int r, int g, int b) {
    StringBuilder sb = new StringBuilder(40);
    sb.append('(').append(r % 256).append(", ");
    sb.append(g % 256).append(", ").append(b % 256).append(')');
    return sb.toString();
  }

  /**
   * Creates a color definition in AnimalScript for the given values of red,
   * green and blue. All values must be between 0 and 255. If not, the values
   * are calculated by modulo operation to fit.
   * 
   * @param aColor
   *          the color to be converted to a String
   * @return the String description for the color passed in
   */
  public static String makeColorDef(Color aColor) {
    if (aColor == null)
      return "(0, 0, 0)";
    return makeColorDef(aColor.getRed(), aColor.getGreen(), aColor.getBlue());
  }

  /**
   * Creates the AnimalScript represantation of a <code>Timing</code>.
   * 
   * @param delay
   *          the <code>Timing</code> to handle.
   * @return the string representation of the Timing.
   */
  public static String makeOffsetTimingDef(Timing delay) {
    if (delay == null)
      return "";
    StringBuilder sb = new StringBuilder(30);
    sb.append(" after ").append(delay.getDelay()).append(" ");
    sb.append(delay.getUnit());
    return sb.toString();
  }

  /**
   * Creates the AnimalScript representation for a hidden object
   * 
   * @param props
   *          the properties item
   * @return the string representation of the Timing.
   */
  public static String makeHiddenDef(AnimationProperties props) {
    boolean isHidden = false;
    if (props != null)
      isHidden = ((Boolean) props.get(AnimationPropertiesKeys.HIDDEN_PROPERTY))
          .booleanValue();
    return (isHidden) ? " hidden" : "";
  }

  /**
   * Creates the AnimalScript code for a duration <code>Timing</code>.
   * 
   * @param duration
   *          the <code>Timing</code> for which the code shall be created.
   * @return the string representation of the Timing.
   */
  public static String makeDurationTimingDef(Timing duration) {
    if (duration == null)
      return "";
    StringBuilder sb = new StringBuilder(30);
    sb.append(" within ").append(duration.getDelay());
    sb.append(" ").append(duration.getUnit());
    return sb.toString();
  }

  /**
   * Creates the AnimalScript code for a <code>DisplayOptions</code> object.
   * 
   * @param d
   *          the <code>DisplayOptions</code> for which the code shall be
   *          created.
   * @return the string representation of the <code>DisplayOptions</code>.
   */
  public static String makeDisplayOptionsDef(DisplayOptions d) {
    if (d instanceof Hidden) {
      return " hidden";
    } else if (d instanceof Timing) {
      return makeOffsetTimingDef((Timing) d);
    } else {
      return "";
    }
  }

  /**
   * Creates the AnimalScript code for a <code>DisplayOptions</code> object.
   * 
   * @param d
   *          the <code>DisplayOptions</code> for which the code shall be
   *          created.
   * @return the string representation of the <code>DisplayOptions</code>.
   */
  public static String makeDisplayOptionsDef(DisplayOptions d,
      AnimationProperties props) {
    StringBuilder sb = new StringBuilder(32);
    if (d instanceof Hidden) {
      sb.append(" hidden");
    } else if (d instanceof Timing) {
      sb.append(makeOffsetTimingDef((Timing) d));
    }
    if (!(d instanceof Hidden))
      sb.append(AnimalGenerator.makeHiddenDef(props));
    return sb.toString();
  }

  /**
   * @see algoanim.primitives.generators.GeneratorInterface#exchange(algoanim.primitives.Primitive,
   *      algoanim.primitives.Primitive)
   */
  public void exchange(Primitive p, Primitive q) {
    StringBuilder sb = new StringBuilder(30);
    sb.append("exchange \"").append(p.getName()).append("\" \"");
    sb.append(q.getName()).append("\"");
    lang.addLine(sb);
  }

  /**
   * @see algoanim.primitives.generators.GeneratorInterface
   *      #hide(algoanim.primitives.Primitive, algoanim.util.Timing)
   */
  public void hide(Primitive q, Timing t) {
    if (t == null)
      lang.hideInThisStep.addElement(q.getName());
    else {
      StringBuilder sb = new StringBuilder(30);
      sb.append("hide \"").append(q.getName()).append("\" ");
      sb.append(AnimalGenerator.makeOffsetTimingDef(t));
      lang.addLine(sb);
    }
  }

  /**
   * @see algoanim.primitives.generators.GeneratorInterface
   *      #rotate(algoanim.primitives.Primitive, algoanim.primitives.Primitive,
   *      int, algoanim.util.Timing, algoanim.util.Timing)
   */
  public void rotate(Primitive p, Primitive around, int degrees, Timing t,
      Timing d) {
    // Well not defined for every Type in AnimalScript
    // This is not the most beautiful method, but in our structure Rect,
    // Triangle and Square are not a subtyp of Polygon

    if (p instanceof Polygon || p instanceof Rect || p instanceof Triangle
        || p instanceof Square) {
      StringBuilder sb = new StringBuilder(30);
      sb.append("rotate \"").append(p.getName()).append("\" around \"");
      sb.append(around.getName() + "\" degrees ").append(degrees % 360);
      sb.append(AnimalGenerator.makeOffsetTimingDef(t));
      sb.append(" ").append(AnimalGenerator.makeDurationTimingDef(d));
      lang.addLine(sb);
    }
  }

  /**
   * @see algoanim.primitives.generators.GeneratorInterface
   *      #rotate(algoanim.primitives.Primitive, algoanim.util.Node, int,
   *      algoanim.util.Timing, algoanim.util.Timing)
   */
  public void rotate(Primitive p, Node center, int degrees, Timing t, Timing d) {
    // Well not defined for every Type in AnimalScript
    if (p instanceof Polyline || p instanceof Square || p instanceof Rect
        || p instanceof Triangle || p instanceof Polygon) {
      StringBuilder def = new StringBuilder("rotate \"");
      def.append(p.getName());
      def.append("\" center ");
      def.append(AnimalGenerator.makeNodeDef(center));
      def.append(" ");

      if (degrees != 0) {
        def.append(" degrees ");
        def.append(degrees % 360);
      }

      def.append(AnimalGenerator.makeOffsetTimingDef(t));
      def.append(AnimalGenerator.makeDurationTimingDef(d));
      lang.addLine(def);
    }
  }

  /**
   * @see algoanim.primitives.generators.GeneratorInterface
   *      #show(algoanim.primitives.Primitive, algoanim.util.Timing)
   */
  public void show(Primitive p, Timing t) {
    StringBuilder sb = new StringBuilder(30);
    sb.append("show \"").append(p.getName()).append("\" ");
    sb.append(AnimalGenerator.makeOffsetTimingDef(t));
    lang.addLine(sb);
  }

  /**
   * @see algoanim.primitives.generators.GeneratorInterface
   *      #moveVia(algoanim.primitives.Primitive, java.lang.String,
   *      java.lang.String, algoanim.primitives.Primitive, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void moveVia(Primitive elem, String direction, String moveType,
      Primitive via, Timing delay, Timing d) throws IllegalDirectionException {
    if (!this.lang.isValidDirection(direction))
      throw new IllegalDirectionException(direction);
    StringBuilder sb = new StringBuilder(30);

    sb.append("move \"").append(elem.getName()).append("\"");
    if (direction != null) {
      sb.append(" corner ").append(direction);
    }
    if (moveType != null) {
      sb.append(" type \"").append(moveType).append("\"");
    }
    sb.append(" via \"").append(via.getName()).append("\" ");
    sb.append(AnimalGenerator.makeOffsetTimingDef(delay)).append(" ");
    sb.append(AnimalGenerator.makeDurationTimingDef(d));
    lang.addLine(sb);
  }

  /**
   * @see algoanim.primitives.generators.GeneratorInterface
   *      #moveTo(algoanim.primitives.Primitive, java.lang.String,
   *      java.lang.String, algoanim.util.Node, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void moveTo(Primitive p, String direction, String moveType,
      Node target, Timing delay, Timing duration)
      throws IllegalDirectionException {
    if (!this.lang.isValidDirection(direction))
      throw new IllegalDirectionException(direction);
    StringBuilder sb = new StringBuilder(30);

    sb.append("move \"").append(p.getName()).append("\"");
    if (direction != null) {
      sb.append(" corner ").append(direction);
    }
    if (moveType != null) {
      sb.append(" type \"").append(moveType).append("\"");
    }
    sb.append(" to ").append(AnimalGenerator.makeNodeDef(target));
    sb.append(" ").append(AnimalGenerator.makeOffsetTimingDef(delay));
    sb.append(" ").append(AnimalGenerator.makeDurationTimingDef(duration));
    lang.addLine(sb);
  }

  /**
   * @see algoanim.primitives.generators.GeneratorInterface
   *      #moveBy(algoanim.primitives.Primitive, java.lang.String, int, int,
   *      algoanim.util.Timing, algoanim.util.Timing)
   */
  public void moveBy(Primitive p, String moveType, int dx, int dy,
      Timing delay, Timing duration) {
    StringBuilder sb = new StringBuilder();
    sb.append("move \"").append(p.getName()).append("\"");
    if (moveType != null) {
      sb.append(" type \"").append(moveType).append("\"");
    }
    sb.append(" along line (0, 0) (").append(dx).append(", ").append(dy);
    sb.append(") ").append(AnimalGenerator.makeOffsetTimingDef(delay));
    sb.append(" ").append(AnimalGenerator.makeDurationTimingDef(duration));
    lang.addLine(sb);
  }

  protected boolean addIntOption(AnimationProperties ap, String key,
      String entry, StringBuilder builder) {
    Integer anInt = (Integer) ap.get(key);
    boolean conditionMet = anInt != null;
    if (conditionMet)
      builder.append(entry).append(anInt.intValue());
    return conditionMet;
  }

  protected boolean addIntProperty(AnimationProperties ap, String key, StringBuilder builder) {
    Integer anInt = (Integer) ap.get(key);
    boolean conditionMet = anInt != null;
    if (conditionMet)
      builder.append(' ').append(key).append(' ').append(anInt.intValue());
    return conditionMet;
  }

  protected boolean addBooleanOption(AnimationProperties ap, String key,
      String entry, StringBuilder builder) {
    Boolean aBool = (Boolean) ap.get(key);
    boolean conditionMet = aBool != null && aBool.booleanValue();
    if (conditionMet)
      builder.append(entry);
    return conditionMet;
  }
  protected boolean addBooleanSwitch(AnimationProperties ap, String key,
      String ifTrue, String otherwise, StringBuilder builder) {
    Boolean aBool = (Boolean) ap.get(key);
    boolean conditionMet = aBool != null && aBool.booleanValue();
    if (conditionMet)
      builder.append(ifTrue);
    else
      builder.append(otherwise);
    return conditionMet;
  }

  protected void addWithTiming(StringBuilder sb, Timing delay,
      Timing duration) {
    sb.append(' ').append(AnimalGenerator.makeOffsetTimingDef(delay));
    sb.append(' ').append(AnimalGenerator.makeDurationTimingDef(duration));
    lang.addLine(sb);

  }
  protected boolean addColorOption(AnimationProperties ap, StringBuilder builder) {
    return addColorOption(ap, AnimationPropertiesKeys.COLOR_PROPERTY,
        " color ", builder);
  }

  protected boolean addColorOption(AnimationProperties ap, String key,
      String entry, StringBuilder builder) {
    Color aColor = (Color) ap.get(key);
    boolean conditionMet = aColor != null;
    if (conditionMet)
      builder.append(entry).append(AnimalGenerator.makeColorDef(aColor));
    return conditionMet;
  }
  protected void addFontOption(AnimationProperties ap, String key,
      StringBuilder builder) {
    addFontOption(ap, key, " font ", builder);
  }

  protected void addFontOption(AnimationProperties ap, String key,
      String tag, StringBuilder builder) {
    Font f = (Font)ap.get(key);
    if (f != null) {
      //   [font <fontNames>] [size fontSize] [bold] [italic]
      builder.append(" font ").append(f.getName()).append(" size ");
      builder.append(f.getSize());
      if (f.isBold())
        builder.append(" bold");
      if (f.isItalic())
        builder.append(" italic");
    }
  }

  protected void addAlignOption(AnimationProperties ap, String key,
      String tag, StringBuilder builder) {
    @SuppressWarnings("unchecked")
	String value = (String)((Vector<String>)ap.get(key)).firstElement();
    if (value != null) {
      builder.append(" align ").append(value);
    }
  }

  /**
   * @see algoanim.primitives.generators.GeneratorInterface
   *      #changeColor(algoanim.primitives.Primitive, java.lang.String,
   *      java.awt.Color, algoanim.util.Timing, algoanim.util.Timing)
   */
  public void changeColor(Primitive elem, String colorType, Color newColor,
      Timing delay, Timing d) {
    StringBuilder sb = new StringBuilder(30);

    sb.append("color \"").append(elem.getName()).append("\"");
    if (colorType != null && colorType.length() > 0) {
      sb.append(" type \"").append(colorType).append("\"");
    }
    sb.append(" ");
    sb.append(AnimalGenerator.makeColorDef(newColor.getRed(),
        newColor.getGreen(), newColor.getBlue()));
    sb.append(" ").append(AnimalGenerator.makeOffsetTimingDef(delay));
    sb.append(" ").append(AnimalGenerator.makeDurationTimingDef(d));
    lang.addLine(sb);
  }
}
