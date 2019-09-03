package animal.exchange;

import java.awt.Point;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

import translator.AnimalTranslator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArcProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CircleSegProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.ListElementProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.animator.Animator;
import animal.animator.ColorChanger;
import animal.animator.Move;
import animal.animator.Rotate;
import animal.animator.SetText;
import animal.animator.TimedAnimator;
import animal.animator.TimedShow;
import animal.graphics.PTArc;
import animal.graphics.PTBoxPointer;
import animal.graphics.PTCircle;
import animal.graphics.PTClosedCircleSegment;
import animal.graphics.PTClosedEllipseSegment;
import animal.graphics.PTEllipse;
import animal.graphics.PTGraph;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTLine;
import animal.graphics.PTOpenCircleSegment;
import animal.graphics.PTOpenEllipseSegment;
import animal.graphics.PTPoint;
import animal.graphics.PTPolygon;
import animal.graphics.PTPolyline;
import animal.graphics.PTRectangle;
import animal.graphics.PTSquare;
import animal.graphics.PTText;
import animal.graphics.PTTriangle;
import animal.graphics.meta.ArrowablePrimitive;
import animal.graphics.meta.FillablePrimitive;
import animal.graphics.meta.OrientedPrimitive;
import animal.graphics.meta.PTArray;
import animal.graphics.meta.PTMatrix;
import animal.main.Animal;
import animal.main.AnimationListEntry;
import animal.main.Link;
import animal.misc.MessageDisplay;

/**
 * This class exports an animation or parts thereof as an AnimalScript file.
 * 
 * @author Guido R&ouml;&szlig;ling ( <A
 *         HREF="mailto:roessling@acm.org">roessling@acm.org </a>)
 * @version 1.1 2000-09-27
 */
public class AlgoAnimExporter extends AnimationExporter {
  /**
   * The Animal object used for retrieving the images to export
   */
  protected Animal                            animal;        // = Animal.get();

  // /**
  // * The current file extension
  // */
  // private String extension = null;
  private Language                            lang;

  protected HashMap<Integer, Primitive>       oidToPrimitive;
  protected HashMap<Integer, PTGraphicObject> oidToObject;

  //
  // /**
  // * Determine if file is compressed
  // */
  // private boolean isCompressed = true;
  private  StringBuilder exportErrors = null;

  /**
   * Export the animation to a file of the given name. Note that you must set
   * the animation by calling <code>setAnimation</code> <strong>before </STRON>
   * you call this method.
   * 
   * Use <code>exportAnimationTo(System.out)</code> instead if you want the
   * output to be given on the terminal.
   * 
   * @param fileName
   *          the name of the output file to export to.
   * @return true if the operation was successful
   * @see #setAnimation(animal.main.Animation)
   */
  public boolean exportAnimationTo(String fileName) {
    OutputStream oStream = null;
    filename = fileName;

    String fileExtension = getDefaultExtension();

    if (!filename.endsWith(fileExtension)) {
      filename += ("." + fileExtension);
    }

    try {
      oStream = new BufferedOutputStream(new FileOutputStream(filename));
      //
      // if (isCompressed) {
      // oStream = new GZIPOutputStream(oStream);
      // }
    } catch (FileNotFoundException fileNotFoundException) {
      MessageDisplay.errorMsg(fileNotFoundException.getMessage(),
          MessageDisplay.RUN_ERROR);
    }

    if (oStream == null) {
      return false;
    }

    return exportAnimationTo(oStream);
  }

  /**
   * Export the animation to the OutputStream passed. Note that you must set the
   * animation by calling <code>setAnimation</code> <strong>before </strong> you
   * call this method.
   * 
   * Use <code>exportAnimationTo(System.out)</code> instead if you want the
   * output to be given on the terminal.
   * 
   * @param oStream
   *          the OutputStream to export to.
   * @return true if the operation was successful
   * @see #setAnimation(animal.main.Animation)
   */
  public boolean exportAnimationTo(OutputStream oStream) {
    if (animal == null) {
      animal = Animal.get();
      animal.getEditors();
    }
    
    exportErrors = new StringBuilder(6144);

    String title = animationToExport.getTitle();
    if (title == null)
      title = "<Untitled>";
    String animAuthor = animationToExport.getAuthor();
    if (animAuthor == null)
      animAuthor = "<Anonymous>";

    // Rectangle animationBBox = animationToExport.determineVisualizationSize();
    // int width = animationBBox.width, height = animationBBox.height;
    lang = new AnimalScript(title, animAuthor, animationToExport.getWidth(),
        animationToExport.getHeight());
    lang.setStepMode(true);
    // width, height);

 
    AnimationListEntry[] localinfo;
    int oidID = -1, pos = 0;
    Vector<PTGraphicObject> allGraphicObjects = animationToExport
        .getGraphicObjects();
    oidToObject = new HashMap<Integer, PTGraphicObject>(
        allGraphicObjects.size() + 89);
    oidToPrimitive = new HashMap<Integer, Primitive>(
        allGraphicObjects.size() + 89);

    for (PTGraphicObject ptgo : allGraphicObjects) {
      if (ptgo != null) {
        oidID = ptgo.getNum(false);
        if (oidToObject.containsKey(Integer.valueOf(oidID)))
          exportErrors.append("Object with OID " + oidID
              + " already present for " + ptgo.toString() +"\n");
        else
          oidToObject.put(oidID, ptgo);
      } else
        exportErrors.append("ptgo at position " + pos + " undefined.\n");
      pos++;
    }
    // AnimatorExporter.setGraphicObjects(allGraphicObjects);
    localinfo = animationToExport.getAnimatorList();

    if (localinfo != null) { // no content...
      // loop over all animators...
      for (AnimationListEntry entry : localinfo) {
        if (entry.mode == AnimationListEntry.ANIMATOR) {
          exportAnimator(lang, entry.animator);
        } else if (entry.mode == AnimationListEntry.STEP) {
          exportStep(lang, entry.link);
        }
      }
    }

    MessageDisplay.message("exportStatusLog",
        new String[] { exportErrors.toString() });

    // open a writer...
    PrintWriter writer = new PrintWriter(oStream);
    // ... then create the animation contents...
    String exportedContent = lang.toString();
    // ... dump them ...
    writer.print(exportedContent);
    // ... and close the writer.
    writer.close();
    System.err.println(exportErrors.toString());
    exportErrors = null;
    localinfo = null;
    return true;
  }

  private void exportAnimator(Language lang, Animator animator) {
    int[] oids = animator.getObjectNums();
    // Vector<PTGraphicObject> ptgos = new Vector<PTGraphicObject>(oids.length);
    // for (int oid : oids) {
    // if (oid >= 1 && oidToObject.containsKey(oid))
    // ptgos.add(oidToObject.get(oid));
    // else
    // System.err.println("oid " + oid +" not associated with an object.");
    // }
    if (animator instanceof TimedAnimator) {
      TimedAnimator ta = (TimedAnimator) animator;
      Timing delay = getDelayFrom(ta);
      Timing duration = getDurationFrom(ta);

      if (animator instanceof TimedShow)
        exportTimedShow(lang, (TimedShow)animator, oids, delay, duration);
      else if (animator instanceof Move)
        exportMove(lang, (Move)animator, oids, delay, duration);
      else if (animator instanceof ColorChanger)
        exportColorChange(lang, (ColorChanger)animator, oids, delay, duration);
      else if (animator instanceof Rotate)
        exportRotate(lang, (Rotate)animator, oids, delay, duration);
      else if (animator instanceof SetText)
        exportSetText(lang, (SetText)animator, oids, delay, duration);
      else
        exportErrors.append("sorry, unknown animator " + animator.toString() +"\n");
    }
  }

  private void exportSetText(Language lang2, SetText animator, int[] oids,
      Timing delay, Timing duration) {
//    PTGraphicObject ptgo = oidToObject.get(animator.getCenterNum());
//    Node c = new Coordinates(ptgo.getLocation().x, ptgo.getLocation().y);
    for (int oid : oids) {
      Primitive associatedObject = getPrimitiveFor(oid, true);
//      associatedObject.rotate(c, animator.getDegrees(), delay, duration);
      if (associatedObject instanceof Text)
        ((Text)associatedObject).setText(animator.getValue(), delay, duration);
      else
        exportErrors.append("exportSetText cannot yet handle "+associatedObject.getClass().getName() +"\n");
    }
  }

  private void exportRotate(Language lang2, Rotate animator, int[] oids,
      Timing delay, Timing duration) {
    PTGraphicObject ptgo = oidToObject.get(animator.getCenterNum());
    Node c = new Coordinates(ptgo.getLocation().x, ptgo.getLocation().y);
    for (int oid : oids) {
      Primitive associatedObject = getPrimitiveFor(oid, true);
      associatedObject.rotate(c, animator.getDegrees(), delay, duration);
    }
  }

  private void exportColorChange(Language lang2, ColorChanger animator,
      int[] oids, Timing delay, Timing duration) {
    for (int oid : oids) {
      Primitive associatedObject = getPrimitiveFor(oid, true);
      associatedObject.changeColor(animator.getMethod(), animator.getColor(),
          delay, duration);
    }
  }

  private void exportTimedShow(Language lang, TimedShow animator, int[] oids,
      Timing delay, Timing duration) {
    boolean isShow = animator.isShow();

    for (int oid : oids) {
      if (!isShow) {
        Primitive associatedObject = getPrimitiveFor(oid, true);
        associatedObject.hide(delay);
      } else {
        if (oidToPrimitive.containsKey(oid))
          oidToPrimitive.get(oid).show(delay);// show again
        else
          getPrimitiveFor(oid, true);
      }
    }
  }

  private void exportMove(Language lang, Move animator, int[] oids,
      Timing delay, Timing duration) {
    Primitive moveBase = getPrimitiveFor(animator.getMoveBaseNum(), false);
    for (int oid : oids) {
      // retrieve and export move base
      Primitive associatedObject = getPrimitiveFor(oid, true);
      associatedObject.moveVia(null, animator.getMethod(), moveBase, delay,
          duration);
    }
  }

  private Timing createTiming(int amount, boolean useTicks) {
    Timing t = null;
    if (amount > 0) {
      if (useTicks)
        t = new TicksTiming(amount);
      else
        t = new MsTiming(amount);
    }
    return t;
  }

  private Timing getDelayFrom(TimedAnimator animator) {
    return createTiming(animator.getOffset(), animator.isUnitIsTicks());
  }

  private Timing getDurationFrom(TimedAnimator animator) {
    return createTiming(animator.getDuration(), animator.isUnitIsTicks());
  }

  private Primitive getPrimitiveFor(int oid, boolean isVisible) {
    Integer val = Integer.valueOf(oid);
    if (oidToPrimitive.containsKey(val))
      return oidToPrimitive.get(val);
    // unknown, create it!
    // PTGraphicObject ptgo = oidToObject.get(val);
    return exportPTGraphicObject(oid, isVisible);
  }

  private Primitive exportPTGraphicObject(int oid, boolean isVisible) {
    PTGraphicObject ptgo = oidToObject.get(oid);
    if (ptgo == null)
      return lang.newText(new Coordinates(0, 0), "ERROR", "ERROR", null, null);
    Point location = ptgo.getLocation();
    Node start = new Coordinates(location.x, location.y);
    Primitive targetPrimitive = null;
    String name = ptgo.getClass().getName().substring(ptgo.getClass().getName().lastIndexOf('.')+1)
        + String.valueOf(ptgo.getNum(false));
    AnimationProperties props = null;

    TicksTiming t = new TicksTiming(0);
    props = insertProperties(ptgo);
    props.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, !isVisible);
    if (ptgo instanceof PTText) {
//      props = insertProperties(ptgo);
      PTText textObject = (PTText) ptgo;
      targetPrimitive = lang.newText(start, textObject.getText(), name, t,
          (TextProperties) props);
    } else if (ptgo instanceof PTRectangle) {
//      props = insertProperties(ptgo);
      PTRectangle rect = (PTRectangle) ptgo;
      targetPrimitive = lang.newRect(
          start,
          new Coordinates(location.x + rect.getWidth(), location.y
              + rect.getHeight()), name, t, (RectProperties)props);
    } else if (ptgo instanceof PTPolyline) {
//      props = insertProperties(ptgo);
      PTPolyline line = (PTPolyline) ptgo;
      Node[] vertices = new Node[line.getNodeCount()];
      int pos = 0;
      for (PTPoint p : line.getNodes())
        vertices[pos++] = new Coordinates(p.getX(), p.getY());
      targetPrimitive = lang.newPolyline(vertices, name,t, (PolylineProperties)props);
    } else {
      exportErrors.append("add me@exportPTGO: " + ptgo.getClass().getName() +"\n");
      targetPrimitive = lang.newSquare(new Coordinates(100, 100), 20, name, t);
    }
    // add primitive
    oidToPrimitive.put(oid, targetPrimitive);
    // return animation
    return targetPrimitive;
  }

  private AnimationProperties insertProperties(PTGraphicObject ptgo) {
    AnimationProperties props = null;
    if (ptgo instanceof PTArc) {
      props = new ArcProperties();
    } else if (ptgo instanceof PTArray) {
      props = new ArrayProperties();
      PTArray array = (PTArray)ptgo;
      props.set(AnimationPropertiesKeys.FILL_PROPERTY, array.getBGColor());
      props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      props.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, array.getElemHighlightColor());
      props.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, array.getHighlightColor());
      props.set(AnimationPropertiesKeys.FONT_PROPERTY, array.getFont());
      props.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, false);
      props.set(AnimationPropertiesKeys.CASCADED_PROPERTY, false);
    } else if (ptgo instanceof PTBoxPointer) {
      props = new ListElementProperties();
      PTBoxPointer ptb = (PTBoxPointer)ptgo;
      props.set(AnimationPropertiesKeys.TEXT_PROPERTY, ptb.getText());
      props.set(AnimationPropertiesKeys.POSITION_PROPERTY, ptb.getPointerPosition());
      props.set(AnimationPropertiesKeys.BOXFILLCOLOR_PROPERTY, ptb.getFillColor());
      //TODO
//      props.set(AnimationPropertiesKeys.POINTERAREACOLOR_PROPERTY, ptb.get
//      props.set(AnimationPropertiesKeys.POINTERAREAFILLCOLOR_PROPERTY, new ColorPropertyItem());
      props.set(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY, ptb.getTextComponent().getColor());
    } else if (ptgo instanceof PTCircle) {
      props = new CircleProperties();
    } else if (ptgo instanceof PTClosedCircleSegment) {
      props = new CircleSegProperties();
      PTClosedCircleSegment ccs = (PTClosedCircleSegment)ptgo;
      props.set(AnimationPropertiesKeys.ANGLE_PROPERTY, ccs.getTotalAngle());
      props.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, ccs.getStartAngle());
      props.set(AnimationPropertiesKeys.CLOSED_PROPERTY, true);
      props.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
      props.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
    } else if (ptgo instanceof PTClosedEllipseSegment) {
      props = new EllipseProperties();
      PTClosedEllipseSegment ces = (PTClosedEllipseSegment)ptgo;
      props.set(AnimationPropertiesKeys.ANGLE_PROPERTY, ces.getTotalAngle());
      props.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, ces.getStartAngle());
      props.set(AnimationPropertiesKeys.CLOSED_PROPERTY, true);
      props.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
      props.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
    } else if (ptgo instanceof PTEllipse) {
      props = new EllipseProperties();
      props.set(AnimationPropertiesKeys.CLOSED_PROPERTY, true);
      props.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
      props.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
    } else if (ptgo instanceof PTGraph) {
      props = new GraphProperties();
    } else if (ptgo instanceof PTLine) {
      props = new PolylineProperties();
//    } else if (ptgo instanceof PTNode) {
//      props = new NodeProperties();
    } else if (ptgo instanceof PTMatrix) {
      props = new MatrixProperties();
    } else if (ptgo instanceof PTOpenCircleSegment) {
      props = new CircleSegProperties();
      PTOpenCircleSegment ocs = (PTOpenCircleSegment)ptgo;
      props.set(AnimationPropertiesKeys.ANGLE_PROPERTY, ocs.getTotalAngle());
      props.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, ocs.getStartAngle());
      props.set(AnimationPropertiesKeys.CLOSED_PROPERTY, false);
      props.set(AnimationPropertiesKeys.FWARROW_PROPERTY, ocs.hasFWArrow());
      props.set(AnimationPropertiesKeys.BWARROW_PROPERTY, ocs.hasBWArrow());
    } else if (ptgo instanceof PTOpenEllipseSegment) {
      props = new EllipseProperties();
      PTOpenEllipseSegment oes = (PTOpenEllipseSegment)ptgo;
      props.set(AnimationPropertiesKeys.ANGLE_PROPERTY, oes.getTotalAngle());
      props.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, oes.getStartAngle());
      props.set(AnimationPropertiesKeys.CLOSED_PROPERTY, false);
      props.set(AnimationPropertiesKeys.FWARROW_PROPERTY, oes.hasFWArrow());
      props.set(AnimationPropertiesKeys.BWARROW_PROPERTY, oes.hasBWArrow());
    } else if (ptgo instanceof PTPoint) {
      props = new PointProperties();
    } else if (ptgo instanceof PTPolygon) {
      props = new PolygonProperties();
    } else if (ptgo instanceof PTPolyline) {
      props = new PolylineProperties();
    } else if (ptgo instanceof PTRectangle) {
      props = new RectProperties();
    } else if (ptgo instanceof PTSquare) {
      props = new SquareProperties();
    } else if (ptgo instanceof PTText) {
      props = new TextProperties();
      PTText text = (PTText)ptgo;
      props.set(AnimationPropertiesKeys.FONT_PROPERTY, text.getFont());
    } else if (ptgo instanceof PTTriangle) {
      props = new TriangleProperties();
    }
    if (props != null) {
      // generic properties
      props.set(AnimationPropertiesKeys.COLOR_PROPERTY, ptgo.getColor());
      props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, ptgo.getDepth());
      
      // properties for fillable primitives
      if (ptgo instanceof FillablePrimitive) {
        FillablePrimitive fp = (FillablePrimitive)ptgo;
        props.set(AnimationPropertiesKeys.FILLED_PROPERTY, fp.isFilled());
        props.set(AnimationPropertiesKeys.FILL_PROPERTY, fp.getFillColor());      
      }
      
      // properties for oriented primitives
      if (ptgo instanceof OrientedPrimitive) {
        OrientedPrimitive op = (OrientedPrimitive)ptgo;
        props.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, op.isClockwise());      
        props.set(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY, !op.isClockwise());
      }

      // properties for fillable primitives
      if (ptgo instanceof ArrowablePrimitive) {
        ArrowablePrimitive ap = (ArrowablePrimitive)ptgo;
        props.set(AnimationPropertiesKeys.BWARROW_PROPERTY, ap.hasBWArrow());      
        props.set(AnimationPropertiesKeys.FWARROW_PROPERTY, ap.hasFWArrow());
      }
    }
    return props;
  }

  private void exportStep(Language lang, Link currentLink) {
    if (currentLink == null)
      return;
    String optionalLabel = currentLink.getLinkLabel();
    int optionalDelay = currentLink.getDurationInTicks();
    if (optionalDelay > 0)
      lang.nextStep(optionalDelay, optionalLabel);
    else
      lang.nextStep(optionalLabel);
  }

  /**
   * Return the default extension for this output type
   * 
   * @return a string determining the output extension tag for this format.
   */
  public String getDefaultExtension() {
    return "asu2";
    // return (isCompressed) ? "asc" : "asu";
  }

  /**
   * Return a short description of this output type
   * 
   * @return a string describing this format.
   * @since 1.1
   */
  public String getFormatDescription() {
    return AnimalTranslator.translateMessage("animalScriptFormat");
    // return AnimalTranslator.translateMessage((isCompressed)
    // ? "animalScriptFormatGzip" : "animalScriptFormat");
  }

  /**
   * Return the MIME type for this output type
   * 
   * @return the MIME type of this format.
   * @since 1.1
   */
  public String getMIMEType() {
    // if (isCompressed) {
    // return "animation/animal-ascii-compressed";
    // }
    return "animation/animalscript";
  }

  /**
   * Set the format name requested and adjust the export capabilities
   * 
   * @param format
   *          the name of the actual format requested
   */
  public void init(String format) {
    super.init(format);
    // extension = format.substring(format.indexOf('/') + 1);
    // isCompressed = extension.endsWith("compressed");
    exportCapabilities = EXPORT_STATIC_SNAPSHOT | EXPORT_DYNAMIC_STEP
        | EXPORT_FULL_ANIMATION;
  }

  /**
   * Use this method to provide a short(single line) description of the exporter
   * 
   * @return a String describing this exporter
   */
  public String toString() {
    return AnimalTranslator.translateMessage("animalScriptExportDescription");
  }
}
