package algoanim.primitives.generators;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.InteractionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.generator.CounterFactory;
import algoanim.counter.generator.ViewFactory;
import algoanim.counter.model.FourValueCounter;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.FourValueView;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Arc;
import algoanim.primitives.ArrayBasedQueue;
import algoanim.primitives.ArrayBasedStack;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.Circle;
import algoanim.primitives.CircleSeg;
import algoanim.primitives.ConceptualQueue;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.CountablePrimitive;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Ellipse;
import algoanim.primitives.EllipseSeg;
import algoanim.primitives.Graph;
import algoanim.primitives.Group;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.ListBasedStack;
import algoanim.primitives.ListElement;
import algoanim.primitives.Point;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.Variables;
import algoanim.primitives.VisualQueue;
import algoanim.primitives.VisualStack;
import algoanim.primitives.vhdl.AndGate;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.properties.ArcProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CircleSegProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.EllipseSegProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.ListElementProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.QueueProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.StackProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.properties.VHDLElementProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * The abstract Language class defines the basic methods for all particular
 * languages like AnimalScript for example, which then itselves provide
 * functionality for output management, a name registry for primitives and
 * factory methods for all supported primitives.
 * 
 * @author Jens Pfau, Stephan Mehlhase, Dima Vronskyi
 */
public abstract class Language {

  public static final int                  INTERACTION_TYPE_NONE          = 128;
  public static final int                  INTERACTION_TYPE_JHAVE_TEXT    = 256;
  public static final int                  INTERACTION_TYPE_JHAVE_XML     = 512;
  public static final int                  INTERACTION_TYPE_AVINTERACTION = 1024;
  public static final int                  UNDEFINED_SIZE                 = 0;

  public HashMap<String, InteractionModel> interactiveElements            = new HashMap<String, InteractionModel>(
                                                                              89);
  /**
   * gather all primitives that are supposed to be hidden or shown in this step
   */
  public Vector<String>                    hideInThisStep                 = new Vector<String>(
                                                                              21,
                                                                              15);

  /**
   * gather all primitives that are supposed to be hidden or shown in this step
   */
  public Vector<String>                    showInThisStep                 = new Vector<String>(
                                                                              21,
                                                                              15);

  public Language(String title, String author, int x, int y) {
    // has to be done by subclasses...
  }
  
  public static Language getLanguageInstance(AnimationType type, String title, String author,
      int width, int height) {
    Language instance = null;
    switch(type) {
      // TODO insert hook for JSON
      case ANIMALSCRIPT:
      default:
        instance = new AnimalScript(title, author, width, height);
    }

    return instance;      
  }

  /**
   * Creates and returns a counter for an <code>countablePrimitive</code>
   * 
   * @param observedObject
   *          the <code>countablePrimitive</code> whose operations are counted
   * @return AbstractCounter the <code>AbstractCounter</code> which saves the
   *         counted values.
   */
  public FourValueCounter newCounter(VisualStack<?> observedObject) {
    CounterFactory cf = new CounterFactory();
    return cf.createCounter(observedObject);
  }

  /**
   * Creates and returns a counter for an <code>countablePrimitive</code>
   * 
   * @param observedObject
   *          the <code>countablePrimitive</code> whose operations are counted
   * @return AbstractCounter the <code>AbstractCounter</code> which saves the
   *         counted values.
   */
  public FourValueCounter newCounter(VisualQueue<?> observedObject) {
    CounterFactory cf = new CounterFactory();
    return cf.createCounter(observedObject);
  }
  
  /**
   * Creates and returns a counter for an <code>countablePrimitive</code>
   * 
   * @param observedObject
   *          the <code>countablePrimitive</code> whose operations are counted
   * @return AbstractCounter the <code>AbstractCounter</code> which saves the
   *         counted values.
   */
  public TwoValueCounter newCounter(CountablePrimitive observedObject) {
    CounterFactory cf = new CounterFactory();
    return cf.createCounter(observedObject);
  }

  /**
   * Creates an <code>TwoValueView</code> with standard text in front of
   * bar/number visualization on a specified <code>AbstractCounter</code>. Both
   * visualizations (bar/number) are used. The COunterProperties have default
   * values.
   * 
   * @param counter
   *          the <code>AbstractCounter</code> whose values are visualized
   * @param coord
   *          the Node representing the top left corner of the view
   */
  public TwoValueView newCounterView(TwoValueCounter counter, Node coord) {
    if (counter == null) {
      throw new IllegalArgumentException("No Counter Object.");
    }
    String[] valueNames = { "Assignments:", "Accesses:", "Enqueueings:",
        "Dequeueings:" };
    CounterProperties props = new CounterProperties();
    ViewFactory vf = new ViewFactory();
    return vf.createView(this, coord, counter, props, true, true, valueNames);

  }

  /**
   * Creates an <code>TwoValueView</code> with standard text in front of
   * bar/number visualization on a specified <code>AbstractCounter</code>. Both
   * visualizations (bar/number) are used.
   * 
   * @param counter
   *          the <code>AbstractCounter</code> whose values are visualized
   * @param coord
   *          the Node representing the top left corner of the view
   * @param counterProperties
   *          the <code>CounterProperties</code> which define colors etc. of the
   *          visualization
   */
  public TwoValueView newCounterView(TwoValueCounter counter, Node coord,
      CounterProperties counterProperties) {
    if (counter == null) {
      throw new IllegalArgumentException("No Counter Object.");
    }
    CounterProperties cp = (counterProperties != null) ? counterProperties
        : new CounterProperties();
    // GR FIX in case of "null", this would always raise a
    // NullPointerException ("null.equals...")!
    // if (counterProperties.equals(null)) {
    // counterProperties = new CounterProperties();
    // }
    String[] valueNames = { "Assignments:", "Accesses:", "Enqueueings:",
        "Dequeueings:" };
    ViewFactory vf = new ViewFactory();
    return vf.createView(this, coord, counter, cp, true, true, valueNames);
  }

  /**
   * Creates an <code>TwoValueView</code> with standard text in front of
   * bar/number visualization on a specified <code>AbstractCounter</code>
   * 
   * @param counter
   *          the <code>AbstractCounter</code> whose values are visualized
   * @param coord
   *          the Node representing the top left corner of the view
   * @param counterProperties
   *          the <code>CounterProperties</code> which define colors etc. of the
   *          visualization
   * @param number
   *          true -> Values visualized as number false -> Values not visualized
   *          as number
   * @param bar
   *          true -> Values visualized as bar false -> Values not visualized as
   *          bar
   */
  public TwoValueView newCounterView(TwoValueCounter counter, Node coord,
      CounterProperties counterProperties, boolean number, boolean bar) {
    if (counter == null) {
      throw new IllegalArgumentException("No Counter Object.");
    }
    CounterProperties cp = (counterProperties != null) ? counterProperties
        : new CounterProperties();
    // GR FIX in case of "null", this would always raise a
    // NullPointerException ("null.equals...")!
    // if (counterProperties.equals(null)) {
    // counterProperties = new CounterProperties();
    // }
    String[] valueNames = { "Assignments:", "Accesses:", "Enqueueings:",
        "Dequeueings:" };
    ViewFactory vf = new ViewFactory();
    return vf.createView(this, coord, counter, cp, bar, number, valueNames);
  }

  /**
   * Creates an <code>TwoValueView</code> on a specified
   * <code>AbstractCounter</code>
   * 
   * @param counter
   *          the <code>AbstractCounter</code> whose values are visualized
   * @param coord
   *          the Node representing the top left corner of the view
   * @param counterProperties
   *          the <code>CounterProperties</code> which define colors etc. of the
   *          visualization
   * @param number
   *          true -> Values visualized as number false -> Values not visualized
   *          as number
   * @param bar
   *          true -> Values visualized as bar false -> Values not visualized as
   *          bar
   * @param valueNames
   *          String Array to customize the names of the counted values (e.g.
   *          "Zuweisungen" instead of "assignments"). The Array must have 2 or
   *          4 entries dependent on the number of visualized values.
   */
  public TwoValueView newCounterView(TwoValueCounter counter, Node coord,
      CounterProperties counterProperties, boolean number, boolean bar,
      String[] valueNames) {
    if (counter == null) {
      throw new IllegalArgumentException("No Counter Object.");
    }
    String[] names = valueNames;
    CounterProperties cp = (counterProperties != null) ? counterProperties
        : new CounterProperties();
    // GR FIX in case of "null", this would always raise a
    // NullPointerException ("null.equals...")!
    // if (counterProperties.equals(null)) {
    // counterProperties = new CounterProperties();
    // }
    if (names == null) {
      names = new String[4];
      names[0] = "Assignments:";
      names[1] = "Accesses:";
      names[2] = "Enqueueings";
      names[3] = "Dequeueings";
    }
    ViewFactory vf = new ViewFactory();
    return vf.createView(this, coord, counter, cp, bar, number, names);
  }

  /**
   * Creates an <code>FourValueView</code> with standard text in front of
   * bar/number visualization on a specified <code>AbstractCounter</code>. Both
   * visualizations (bar/number) are used. The COunterProperties have default
   * values.
   * 
   * @param counter
   *          the <code>AbstractCounter</code> whose values are visualized
   * @param coord
   *          the Node representing the top left corner of the view
   */
  public FourValueView newCounterView(FourValueCounter counter, Node coord) {
    if (counter == null) {
      throw new IllegalArgumentException("No Counter Object.");
    }
    String[] valueNames = { "Assignments:", "Accesses:", "Enqueueings:",
        "Dequeueings:" };
    CounterProperties props = new CounterProperties();
    ViewFactory vf = new ViewFactory();
    return vf.createView(this, coord, counter, props, true, true, valueNames);

  }

  /**
   * Creates an <code>FourValueView</code> with standard text in front of
   * bar/number visualization on a specified <code>AbstractCounter</code>. Both
   * visualizations (bar/number) are used.
   * 
   * @param counter
   *          the <code>AbstractCounter</code> whose values are visualized
   * @param coord
   *          the Node representing the top left corner of the view
   * @param counterProperties
   *          the <code>CounterProperties</code> which define colors etc. of the
   *          visualization
   */
  public FourValueView newCounterView(FourValueCounter counter, Node coord,
      CounterProperties counterProperties) {
    if (counter == null) {
      throw new IllegalArgumentException("No Counter Object.");
    }
    CounterProperties cp = (counterProperties != null) ? counterProperties
        : new CounterProperties();
    // GR FIX in case of "null", this would always raise a
    // NullPointerException ("null.equals...")!
    // if (counterProperties.equals(null)) {
    // counterProperties = new CounterProperties();
    // }
    String[] valueNames = { "Assignments:", "Accesses:", "Enqueueings:",
        "Dequeueings:" };
    ViewFactory vf = new ViewFactory();
    return vf.createView(this, coord, counter, cp, true, true, valueNames);
  }

  /**
   * Creates an <code>FourValueView</code> with standard text in front of
   * bar/number visualization on a specified <code>AbstractCounter</code>
   * 
   * @param counter
   *          the <code>AbstractCounter</code> whose values are visualized
   * @param coord
   *          the Node representing the top left corner of the view
   * @param counterProperties
   *          the <code>CounterProperties</code> which define colors etc. of the
   *          visualization
   * @param number
   *          true -> Values visualized as number false -> Values not visualized
   *          as number
   * @param bar
   *          true -> Values visualized as bar false -> Values not visualized as
   *          bar
   */
  public FourValueView newCounterView(FourValueCounter counter, Node coord,
      CounterProperties counterProperties, boolean number, boolean bar) {
    if (counter == null) {
      throw new IllegalArgumentException("No Counter Object.");
    }
    CounterProperties cp = (counterProperties != null) ? counterProperties
        : new CounterProperties();
    // GR FIX in case of "null", this would always raise a
    // NullPointerException ("null.equals...")!
    // if (counterProperties.equals(null)) {
    // counterProperties = new CounterProperties();
    // }
    String[] valueNames = { "Assignments:", "Accesses:", "Enqueueings:",
        "Dequeueings:" };
    ViewFactory vf = new ViewFactory();
    return vf.createView(this, coord, counter, cp, bar, number, valueNames);
  }

  /**
   * Creates an <code>FourValueView</code> on a specified
   * <code>AbstractCounter</code>
   * 
   * @param counter
   *          the <code>AbstractCounter</code> whose values are visualized
   * @param coord
   *          the Node representing the top left corner of the view
   * @param counterProperties
   *          the <code>CounterProperties</code> which define colors etc. of the
   *          visualization
   * @param number
   *          true -> Values visualized as number false -> Values not visualized
   *          as number
   * @param bar
   *          true -> Values visualized as bar false -> Values not visualized as
   *          bar
   * @param valueNames
   *          String Array to customize the names of the counted values (e.g.
   *          "Zuweisungen" instead of "assignments"). The Array must have 2 or
   *          4 entries dependent on the number of visualized values.
   */
  public FourValueView newCounterView(FourValueCounter counter, Node coord,
      CounterProperties counterProperties, boolean number, boolean bar,
      String[] valueNames) {
    if (counter == null) {
      throw new IllegalArgumentException("No Counter Object.");
    }
    String[] names = valueNames;
    CounterProperties cp = (counterProperties != null) ? counterProperties
        : new CounterProperties();
    // GR FIX in case of "null", this would always raise a
    // NullPointerException ("null.equals...")!
    // if (counterProperties.equals(null)) {
    // counterProperties = new CounterProperties();
    // }
    if (names == null) {
      names = new String[4];
      names[0] = "Assignments:";
      names[1] = "Accesses:";
      names[2] = "Enqueueings";
      names[3] = "Dequeueings";
    }
    ViewFactory vf = new ViewFactory();
    return vf.createView(this, coord, counter, cp, bar, number, names);
  }
  
  /**
   * create an instance of this abstract object for a concrete back-end
   * This is the <strong>preferred way</strong> to start an animation
   * as of Animal version 2.3.34
   */
  public static Language createInstance(AlgoAnimBackend chosenBackend,
      String title, String author, int width, int height) {
    Language myInstance = null;
    switch(chosenBackend) {
      case ANIMALSCRIPT:
        myInstance = new AnimalScript(title, author, width, height);
        break;
      default:
        System.err.println("Unknown or unsupported backend, sorry... Reverting to AnimalScript");
        myInstance = new AnimalScript(title, author, width, height);
    }
    return myInstance;
  }

  /**
   * Adds another line at the end of the output buffer.
   * 
   * @param line
   *          the line to add.
   */
  public abstract void addLine(StringBuilder line);

  /**
   * Adds another line at the end of the output buffer.
   * 
   * @param line
   *          the line to add.
   */
  public void addLine(String line) {
    this.addLine(new StringBuilder(line));
  }

  /**
   * Adds another line at the end of the error buffer.
   * 
   * @param error
   *          the line to add.
   */
  public abstract void addError(StringBuilder error);

  /**
   * Adds another line at the end of the error buffer.
   * 
   * @param error
   *          the line to add.
   */
  public void addError(String error) {
    this.addError(new StringBuilder(error));
  }

  /**
   * Registers a newly created Primitive to the Language object.
   * 
   * @param p
   *          the primitive to register.
   */
  public abstract void addItem(Primitive p);

  /**
   * Writes the output to the given file.
   * 
   * @param fileName
   *          the target file.
   */
  public abstract void writeFile(String fileName);

  /**
   * Method to be called before the content is accessed or written
   */
  public abstract void finalizeGeneration();

  /**
   * Returns the generated animation code
   * 
   * @return the animation code
   */
  public abstract String getAnimationCode();

  /**
   * Gives the current animation step. This step is created by calling
   * <code>nextStep()</code>.
   * 
   * @return the current animation step.
   */
  public abstract int getStep();

  /**
   * Checks the internal primitive registry for the given name.
   * 
   * @param name
   *          the name to check.
   * @return if the given name is already in use.
   */
  public abstract boolean isNameUsed(String name);

  /**
   * The vector which is returned describes the valid direction statements
   * regarding this actual language object.
   * 
   * @return the directions which can be applied to move methods.
   */
  public abstract Vector<String> validDirections();

  /**
   * Evaluates whether a given String describes a valid direction for movement
   * operations.
   * 
   * @param direction
   *          the String to check.
   * @return whether the given String describes a valid direction.
   */
  public abstract boolean isValidDirection(String direction);

  /**
   * This method is used to enable or disable the step mode. If disabled, each
   * statement will occupy its own animation step. If enabled (mode=true), all
   * subsequent statements will share the same animation step until the method
   * <em>nextStep</em> is called. Statements grouped in the same step will be
   * executed in parallel, but of course respecting the optional timing (delay,
   * duration) specifiable for each statement.
   * 
   * @param mode
   *          whether to enable stepmode or not.
   * @see #nextStep()
   */
  public abstract void setStepMode(boolean mode);

  /**
   * If <em>setStepMode(true)</em> was called, calling <em>nextStep()</em> will
   * start the next step.
   * 
   * @see #setStepMode(boolean)
   */
  public void nextStep() {
    nextStep(-1, null);
  }

  /**
   * If <em>setStepMode(true)</em> was called, calling <em>nextStep(int)</em>
   * will start the next step after the delay specified by the input parameter.
   * 
   * @param delay
   *          the delay in ms to wait before the next step starts. Use
   *          <em>-1</em> to specify that the next step will <em>not</em> start
   *          automatically.
   * @see #setStepMode(boolean)
   */
  public void nextStep(int delay) {
    nextStep(delay, null);
  }

  /**
   * If <em>setStepMode(true)</em> was called, calling <em>nextStep(int)</em>
   * will start the next step after the delay specified by the input parameter.
   * 
   * @param label
   *          the label associated with the current step (may be null). If a
   *          label is given, it may be used as an anchor for navigating to the
   *          step.
   * @see #setStepMode(boolean)
   */
  public void nextStep(String label) {
    nextStep(-1, label);
  }

  /**
   * If <em>setStepMode(true)</em> was called, calling <em>nextStep(int)</em>
   * will start the next step after the delay specified by the input parameter.
   * 
   * @param delay
   *          the delay in ms to wait before the next step starts. Use
   *          <em>-1</em> to specify that the next step will <em>not</em> start
   *          automatically.
   * @param label
   *          the label associated with the current step (may be null). If a
   *          label is given, it may be used as an anchor for navigating to the
   *          step.
   * @see #setStepMode(boolean)
   */
  public abstract void nextStep(int delay, String label);

  /*
   * Every Language has to implement the following factory methods, which return
   * the proper primitives. If one of this types is not supported by a
   * particular Language, just return null.
   */

  /**
   * Creates a new <code>Point</code> object.
   * 
   * @param coords
   *          the <code>Point</code>s coordinates.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Point</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param pp
   *          [optional] user-defined properties to apply to this
   *          <code>Point</code>. If you don't want any user-defined properties
   *          to be set, then set this parameter to "null", default properties
   *          will be applied in that case.
   * @return a <code>Point</code>
   */
  public abstract Point newPoint(Node coords, String name,
      DisplayOptions display, PointProperties pp);

  /*
   * Arc Types
   */

  /**
   * @param center
   *          the center of the <code>Arc</code>.
   * @param radius
   *          the radius of the <code>Arc</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Arc</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return the new arc
   */
  public Arc newArc(Node center, Node radius, String name,
      DisplayOptions display) {
    return newArc(center, radius, name, display, new ArcProperties());
  }

  /**
   * @param center
   *          the center of the <code>Arc</code>.
   * @param radius
   *          the radius of the <code>Arc</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Arc</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param ap
   *          [optional] user-defined properties to apply to this
   *          <code>Arc</code>. If you don't want any user-defined properties to
   *          be set, then set this parameter to "null", default properties will
   *          be applied in that case.
   * @return the new arc
   */
  public abstract Arc newArc(Node center, Node radius, String name,
      DisplayOptions display, ArcProperties ap);

  /**
   * Creates a new <code>Circle</code> object.
   * 
   * @param center
   *          the center of the <code>Circle</code>.
   * @param radius
   *          the radius of the <code>Circle</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Circle</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Circle</code>.
   */
  public Circle newCircle(Node center, int radius, String name,
      DisplayOptions display) {
    return newCircle(center, radius, name, display, new CircleProperties());
  }

  /**
   * Creates a new <code>Circle</code> object.
   * 
   * @param center
   *          the center of the <code>Circle</code>.
   * @param radius
   *          the radius of the <code>Circle</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Circle</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param cp
   *          [optional] user-defined properties to apply to this
   *          <code>Circle</code>. If you don't want any user-defined properties
   *          to be set, then set this parameter to "null", default properties
   *          will be applied in that case.
   * @return a <code>Circle</code>.
   */
  public abstract Circle newCircle(Node center, int radius, String name,
      DisplayOptions display, CircleProperties cp);

  /**
   * Creates a new <code>CircleSeg</code> object.
   * 
   * @param center
   *          the center of the <code>CircleSeg</code>.
   * @param radius
   *          the radius of this <code>CircleSeg</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>CircleSeg</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param cp
   *          [optional] user-defined properties to apply to this
   *          <code>CircleSeg</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>CircleSeg</code>.
   */
  public abstract CircleSeg newCircleSeg(Node center, int radius, String name,
      DisplayOptions display, CircleSegProperties cp);

  /**
   * Creates a new <code>EllipseSeg</code> object.
   * 
   * @param center
   *          the center of the <code>EllipseSeg</code>.
   * @param radius
   *          the radius of this <code>EllipseSeg</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>EllipseSeg</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>CircleSeg</code>.
   */
  public EllipseSeg newEllipseSeg(Node center, Node radius, String name,
      DisplayOptions display) {
    return newEllipseSeg(center, radius, name, display,
        new EllipseSegProperties());
  }

  /**
   * Creates a new <code>EllipseSeg</code> object.
   * 
   * @param center
   *          the center of the <code>EllipseSeg</code>.
   * @param radius
   *          the radius of this <code>EllipseSeg</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>EllipseSeg</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param cp
   *          [optional] user-defined properties to apply to this
   *          <code>EllipseSeg</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>CircleSeg</code>.
   */
  public abstract EllipseSeg newEllipseSeg(Node center, Node radius,
      String name, DisplayOptions display, EllipseSegProperties cp);

  /**
   * Creates a new <code>Ellipse</code> object.
   * 
   * @param center
   *          the center of the <code>Ellipse</code>.
   * @param radius
   *          the radius of the <code>Ellipse</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Ellipse</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param ep
   *          [optional] user-defined properties to apply to this
   *          <code>Ellipse</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>Ellipse</code>.
   */
  public abstract Ellipse newEllipse(Node center, Node radius, String name,
      DisplayOptions display, EllipseProperties ep);

  /**
   * Creates a new <code>Ellipse</code> object.
   * 
   * @param center
   *          the center of the <code>Ellipse</code>.
   * @param radius
   *          the radius of the <code>Ellipse</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Ellipse</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Ellipse</code>.
   */
  public Ellipse newEllipse(Node center, Node radius, String name,
      DisplayOptions display) {
    return newEllipse(center, radius, name, display, new EllipseProperties());
  }

  /**
   * Creates a new <code>CircleSeg</code> object.
   * 
   * @param center
   *          the center of the <code>CircleSeg</code>.
   * @param radius
   *          the radius of this <code>CircleSeg</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>CircleSeg</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>CircleSeg</code>.
   */
  public CircleSeg newCircleSeg(Node center, int radius, String name,
      DisplayOptions display) {
    return newCircleSeg(center, radius, name, display,
        new CircleSegProperties());
  }

  /**
   * Creates a new <code>Ellipse</code> object.
   * 
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Ellipse</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *          the graph's adjacency matrix
   * @param graphNodes
   *          the nodes of the graph
   * @param labels
   *          the labels of the graph's nodes
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param props
   *          [optional] user-defined properties to apply to this
   *          <code>Graph</code>. If you don't want any user-defined properties
   *          to be set, then set this parameter to "null", default properties
   *          will be applied in that case.
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix,
      Node[] graphNodes, String[] labels, DisplayOptions display,
      GraphProperties props);

  public Graph addGraph(Graph aGraph) {
    return addGraph(aGraph, null, new GraphProperties());
  }

  public abstract Graph addGraph(Graph aGraph, DisplayOptions display,
      GraphProperties props);
  

  /**
   * Creates a new <code>Graph</code> object. Using Autodraw without nodes needed
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param labels
   *            the labels of the graph's nodes
   * @param display
   *            [optional] a subclass of <code>DisplayOptions</code> which
   *            describes additional display options like a hidden flag or
   *            timings.
   * @param props
   *            [optional] user-defined properties to apply to this
   *            <code>Graph</code>. If you don't want any user-defined properties
   *            to be set, then set this parameter to "null", default properties
   *            will be applied in that case.
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   *            the lower right y coordinate of the area for drawing the graph in
   * @param minEdgeLength
   *            the minimum edge length between two nodes
   * @param nodeRadius
   *            the radius of the nodes
   * @param loopIterations
   *            the iterations of the force directed node placement
   * @param postIterations
   *            the iterations for reducing crossing edge
   * @param raster
   *            specifies if nodes with similar x or y coordinate get the same x
   *            or y coordinate
   * @param xEpsilon
   *            the x epsilon in which two nodes get the same x coordinate
   * @param yEpsilon
   *            the y epsilon in which two nodes get the same y coordinate
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, DisplayOptions display,
      GraphProperties props, int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRight, int minEdgeLength,
      int nodeRadius, int loopIterations, int postIterations, boolean raster, int xEpsilon, int yEpsilon);
  
    /**
   * Creates a new <code>Graph</code> object. Using Autodraw without nodes needed
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param labels
   *            the labels of the graph's nodes
   * @param display
   *            [optional] a subclass of <code>DisplayOptions</code> which
   *            describes additional display options like a hidden flag or
   *            timings.
   * @param props
   *            [optional] user-defined properties to apply to this
   *            <code>Graph</code>. If you don't want any user-defined properties
   *            to be set, then set this parameter to "null", default properties
   *            will be applied in that case.
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   *            the lower right y coordinate of the area for drawing the graph in
   * @param minEdgeLength
   *            the minimum edge length between two nodes
   * @param nodeRadius
   *            the radius of the nodes
   * @param loopIterations
   *            the iterations of the force directed node placement
   * @param postIterations
   *            the iterations for reducing crossing edge
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, DisplayOptions display,
      GraphProperties props, int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRight, int minEdgeLength,
      int nodeRadius, int loopIterations, int postIterations);

  /**
   * Creates a new <code>Graph</code> object. Using Autodraw without nodes needed
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param labels
   *            the labels of the graph's nodes
   * @param display
   *            [optional] a subclass of <code>DisplayOptions</code> which
   *            describes additional display options like a hidden flag or
   *            timings.
   * @param props
   *            [optional] user-defined properties to apply to this
   *            <code>Graph</code>. If you don't want any user-defined properties
   *            to be set, then set this parameter to "null", default properties
   *            will be applied in that case.
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   * @param raster
   *            specifies if nodes with similar x or y coordinate get the same x
   *            or y coordinate
   * @param xEpsilon
   *            the x epsilon in which two nodes get the same x coordinate
   * @param yEpsilon
   *            the y epsilon in which two nodes get the same y coordinate
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, DisplayOptions display,
      GraphProperties props, int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRight, boolean raster,
      int xEpsilon, int yEpsilon);

  /**
   * Creates a new <code>Graph</code> object. Using Autodraw without nodes needed
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param labels
   *            the labels of the graph's nodes
   * @param display
   *            [optional] a subclass of <code>DisplayOptions</code> which
   *            describes additional display options like a hidden flag or
   *            timings.
   * @param props
   *            [optional] user-defined properties to apply to this
   *            <code>Graph</code>. If you don't want any user-defined properties
   *            to be set, then set this parameter to "null", default properties
   *            will be applied in that case.
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   *            the lower right y coordinate of the area for drawing the graph in
   * 
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, DisplayOptions display,
      GraphProperties props, int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRight);

  /**
   * Creates a new <code>Graph</code> object. Using Autodraw without nodes needed
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param labels
   *            the labels of the graph's nodes
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   *            the lower right y coordinate of the area for drawing the graph in
   * 
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, int xUpperLeft,
      int yUpperLeft, int xLowerRight, int yLowerRight);

  /**
   * Creates a new <code>Graph</code> object. Using Autodraw with the need of
   * nodes
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param graphNodes
   *            the nodes of the graph
   * @param labels
   *            the labels of the graph's nodes
   * @param display
   *            [optional] a subclass of <code>DisplayOptions</code> which
   *            describes additional display options like a hidden flag or
   *            timings.
   * @param props
   *            [optional] user-defined properties to apply to this
   *            <code>Graph</code>. If you don't want any user-defined properties
   *            to be set, then set this parameter to "null", default properties
   *            will be applied in that case.
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   *            the lower right y coordinate of the area for drawing the graph in
   * @param minEdgeLength
   *            the minimum edge length between two nodes
   * @param nodeRadius
   *            the radius of the nodes
   * @param loopIterations
   *            the iterations of the force directed node placement
   * @param postIterations
   *            the iterations for reducing crossing edge
   * @param raster
   *            specifies if nodes with similar x or y coordinate get the same x
   *            or y coordinate
   * @param xEpsilon
   *            the x epsilon in which two nodes get the same x coordinate
   * @param yEpsilon
   *            the y epsilon in which two nodes get the same y coordinate
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix,
      Node[] graphNodes, String[] labels, DisplayOptions display,
      GraphProperties props, int xUpperLeft, int yUpperLeft, int xLowerRight,
    int yLowerRight, int minEdgeLength, int nodeRadius, int loopIterations, int postIterations, boolean raster, int xEpsilon, int yEpsilon);
   
   
    /**
   * Creates a new <code>Graph</code> object. Using Autodraw with the need of
   * nodes
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param graphNodes
   *            the nodes of the graph
   * @param labels
   *            the labels of the graph's nodes
   * @param display
   *            [optional] a subclass of <code>DisplayOptions</code> which
   *            describes additional display options like a hidden flag or
   *            timings.
   * @param props
   *            [optional] user-defined properties to apply to this
   *            <code>Graph</code>. If you don't want any user-defined properties
   *            to be set, then set this parameter to "null", default properties
   *            will be applied in that case.
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   *            the lower right y coordinate of the area for drawing the graph in
   * @param minEdgeLength
   *            the minimum edge length between two nodes
   * @param nodeRadius
   *            the radius of the nodes
   * @param loopIterations
   *            the iterations of the force directed node placement
   * @param postIterations
   *            the iterations for reducing crossing edge
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix,
      Node[] graphNodes, String[] labels, DisplayOptions display,
      GraphProperties props, int xUpperLeft, int yUpperLeft, int xLowerRight,
    int yLowerRight, int minEdgeLength, int nodeRadius, int loopIterations, int postIterations);
  
  
    /**
   * Creates a new <code>Graph</code> object. Using Autodraw with the need of
   * nodes
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param graphNodes
   *            the nodes of the graph
   * @param labels
   *            the labels of the graph's nodes
   * @param display
   *            [optional] a subclass of <code>DisplayOptions</code> which
   *            describes additional display options like a hidden flag or
   *            timings.
   * @param props
   *            [optional] user-defined properties to apply to this
   *            <code>Graph</code>. If you don't want any user-defined properties
   *            to be set, then set this parameter to "null", default properties
   *            will be applied in that case.
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   *            the lower right y coordinate of the area for drawing the graph in
   * @param raster
   *            specifies if nodes with similar x or y coordinate get the same x
   *            or y coordinate
   * @param xEpsilon
   *            the x epsilon in which two nodes get the same x coordinate
   * @param yEpsilon
   *            the y epsilon in which two nodes get the same y coordinate
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix,
      Node[] graphNodes, String[] labels, DisplayOptions display,
      GraphProperties props, int xUpperLeft, int yUpperLeft, int xLowerRight,
    int yLowerRight, boolean raster, int xEpsilon, int yEpsilon);
  
  
  /**
   * Creates a new <code>Graph</code> object. Using Autodraw with the need of
   * nodes
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param graphNodes
   *            the nodes of the graph
   * @param labels
   *            the labels of the graph's nodes
   * @param display
   *            [optional] a subclass of <code>DisplayOptions</code> which
   *            describes additional display options like a hidden flag or
   *            timings.
   * @param props
   *            [optional] user-defined properties to apply to this
   *            <code>Graph</code>. If you don't want any user-defined properties
   *            to be set, then set this parameter to "null", default properties
   *            will be applied in that case.
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   *            the lower right y coordinate of the area for drawing the graph in
   * @return a <code>Graph</code>.
   */
 public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix,
     Node[] graphNodes, String[] labels, DisplayOptions display,
     GraphProperties props, int xUpperLeft, int yUpperLeft, int xLowerRight,
    int yLowerRights);
   
  /**
   * Creates a new <code>Graph</code> object. Using Autodraw with the need of
   * nodes
   * 
   * @param name
   *            [optional] an arbitrary name which uniquely identifies this
   *            <code>Graph</code>. If you don't want to set this, set this
   *            parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *            the graph's adjacency matrix
   * @param graphNodes
   *            the nodes of the graph
   * @param labels
   *            the labels of the graph's nodes
   * @param xUpperLeft
   *            the upper left x coordinate of the area for drawing the graph in
   * @param yUpperLeft
   *            the upper left y coordinate of the area for drawing the graph in
   * @param xLowerRight
   *            the lower right x coordinate of the area for drawing the graph in
   * @param yLowerRight
   *            the lower right y coordinate of the area for drawing the graph in
   * @return a <code>Graph</code>.
   */
  public abstract Graph newGraph(String name, int[][] graphAdjacencyMatrix, Node[] graphNodes, String[] labels,
      int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRights);
  

  /**
   * Creates a new <code>Ellipse</code> object.
   * 
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Ellipse</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param graphAdjacencyMatrix
   *          the graph's adjacency matrix
   * @param graphNodes
   *          the nodes of the graph
   * @param labels
   *          the labels of the graph's nodes
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Graph</code>.
   */
  public Graph newGraph(String name, int[][] graphAdjacencyMatrix,
      Node[] graphNodes, String[] labels, DisplayOptions display) {
    return newGraph(name, graphAdjacencyMatrix, graphNodes, labels, display,
        new GraphProperties());
  }

  /*
   * Polygon Types
   */
  /**
   * Creates a new <code>Polyline</code> object.
   * 
   * @param vertices
   *          the vertices of this <code>Polyline</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Polyline</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param pp
   *          [optional] user-defined properties to apply to this
   *          <code>Polyline</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>Polyline</code>.
   */
  public abstract Polyline newPolyline(Node[] vertices, String name,
      DisplayOptions display, PolylineProperties pp);

  /**
   * Creates a new <code>Polyline</code> object.
   * 
   * @param vertices
   *          the vertices of this <code>Polyline</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Polyline</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Polyline</code>.
   */
  public Polyline newPolyline(Node[] vertices, String name,
      DisplayOptions display) {
    return newPolyline(vertices, name, display, new PolylineProperties());
  }

  /**
   * Creates a new <code>Polygon</code> object.
   * 
   * @param vertices
   *          the vertices of this <code>Polygon</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Polygon</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param pp
   *          [optional] user-defined properties to apply to this
   *          <code>Polygon</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>Polygon</code>.
   * @throws NotEnoughNodesException
   *           Thrown if vertices contains less than 2 nodes.
   */
  public abstract Polygon newPolygon(Node[] vertices, String name,
      DisplayOptions display, PolygonProperties pp)
      throws NotEnoughNodesException;

  /**
   * Creates a new <code>Polygon</code> object.
   * 
   * @param vertices
   *          the vertices of this <code>Polygon</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Polygon</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Polygon</code>.
   * @throws NotEnoughNodesException
   *           Thrown if vertices contains less than 2 nodes.
   */
  public Polygon newPolygon(Node[] vertices, String name, DisplayOptions display)
      throws NotEnoughNodesException {
    return newPolygon(vertices, name, display, new PolygonProperties());
  }

  /**
   * Creates a new <code>Rect</code> object.
   * 
   * @param upperLeft
   *          the coordinates of the upper left corner of this <code>Rect</code>
   *          .
   * @param lowerRight
   *          the coordinates of the lower right corner of this
   *          <code>Rect</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Rect</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param rp
   *          [optional] user-defined properties to apply to this
   *          <code>Rect</code>. If you don't want any user-defined properties
   *          to be set, then set this parameter to "null", default properties
   *          will be applied in that case.
   * @return a <code>Rect</code>.
   */
  public abstract Rect newRect(Node upperLeft, Node lowerRight, String name,
      DisplayOptions display, RectProperties rp);

  /**
   * Creates a new <code>Rect</code> object.
   * 
   * @param upperLeft
   *          the coordinates of the upper left corner of this <code>Rect</code>
   *          .
   * @param lowerRight
   *          the coordinates of the lower right corner of this
   *          <code>Rect</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Rect</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Rect</code>.
   */
  public Rect newRect(Node upperLeft, Node lowerRight, String name,
      DisplayOptions display) {
    return newRect(upperLeft, lowerRight, name, display, new RectProperties());
  }

  /**
   * Creates a new <code>Square</code>.
   * 
   * @param upperLeft
   *          the coordinates of the upper left corner the <code>Square</code>.
   * @param width
   *          the length of the edges of the <code>Square</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Square</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param sp
   *          [optional] user-defined properties to apply to this
   *          <code>Square</code>. If you don't want any user-defined properties
   *          to be set, then set this parameter to "null", default properties
   *          will be applied in that case.
   * @return a <code>Square</code>.
   */
  public abstract Square newSquare(Node upperLeft, int width, String name,
      DisplayOptions display, SquareProperties sp);

  /**
   * Creates a new <code>Square</code>.
   * 
   * @param upperLeft
   *          the coordinates of the upper left corner the <code>Square</code>.
   * @param width
   *          the length of the edges of the <code>Square</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Square</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Square</code>.
   */
  public Square newSquare(Node upperLeft, int width, String name,
      DisplayOptions display) {
    return newSquare(upperLeft, width, name, display, new SquareProperties());
  }

  /**
   * Creates a new <code>Triangle</code> object.
   * 
   * @param x
   *          the coordinates of the first node of the <code>Triangle</code> .
   * @param y
   *          the coordinates of the second node of the <code>Triangle</code>.
   * @param z
   *          the coordinates of the third node of the <code>Triangle</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Triangle</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param tp
   *          [optional] user-defined properties to apply to this
   *          <code>Triangle</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>Triangle</code>.
   */
  public abstract Triangle newTriangle(Node x, Node y, Node z, String name,
      DisplayOptions display, TriangleProperties tp);

  /**
   * Creates a new <code>Variables</code> object.
   * 
   * @return <code>Variables</code>
   */
  public abstract Variables newVariables();

  /**
   * Creates a new <code>Triangle</code> object.
   * 
   * @param x
   *          the coordinates of the first node of the <code>Triangle</code> .
   * @param y
   *          the coordinates of the second node of the <code>Triangle</code>.
   * @param z
   *          the coordinates of the third node of the <code>Triangle</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Triangle</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Triangle</code>.
   */
  public Triangle newTriangle(Node x, Node y, Node z, String name,
      DisplayOptions display) {
    return newTriangle(x, y, z, name, display, new TriangleProperties());
  }

  /**
   * Creates a new <code>DoubleArray</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>DoubleArray</code>.
   * @param data
   *          the initial data of the <code>DoubleArray</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>DoubleArray</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param iap
   *          [optional] user-defined properties to apply to this
   *          <code>IntArray</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>IntArray</code>.
   */
  public abstract DoubleArray newDoubleArray(Node upperLeft, double[] data,
      String name, ArrayDisplayOptions display, ArrayProperties iap);

  /**
   * Creates a new <code>DoubleArray</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>DoubleArray</code>.
   * @param data
   *          the initial data of the <code>DoubleArray</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>DoubleArray</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>IntArray</code>.
   */
  public DoubleArray newDoubleArray(Node upperLeft, double[] data, String name,
      ArrayDisplayOptions display) {
    return newDoubleArray(upperLeft, data, name, display, new ArrayProperties());
  }

  /**
   * Creates a new <code>IntArray</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>IntArray</code>.
   * @param data
   *          the initial data of the <code>IntArray</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>IntArray</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param iap
   *          [optional] user-defined properties to apply to this
   *          <code>IntArray</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>IntArray</code>.
   */
  public abstract IntArray newIntArray(Node upperLeft, int[] data, String name,
      ArrayDisplayOptions display, ArrayProperties iap);

  /**
   * Creates a new <code>IntArray</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>IntArray</code>.
   * @param data
   *          the initial data of the <code>IntArray</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>IntArray</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>IntArray</code>.
   */
  public IntArray newIntArray(Node upperLeft, int[] data, String name,
      ArrayDisplayOptions display) {
    return newIntArray(upperLeft, data, name, display, new ArrayProperties());
  }

  /**
   * Creates a new <code>DoubleMatrix</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>DoubleMatrix</code>.
   * @param data
   *          the initial data of the <code>DoubleMatrix</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>DoubleMatrix</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param imp
   *          [optional] user-defined properties to apply to this
   *          <code>DoubleMatrix</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>DoubleMatrix</code>.
   */
  public abstract DoubleMatrix newDoubleMatrix(Node upperLeft, double[][] data,
      String name, DisplayOptions display, MatrixProperties imp);

  /**
   * Creates a new <code>IntMatrix</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>IntMatrix</code>.
   * @param data
   *          the initial data of the <code>IntMatrix</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>IntMatrix</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param imp
   *          [optional] user-defined properties to apply to this
   *          <code>IntMatrix</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>IntArray</code>.
   */
  public abstract IntMatrix newIntMatrix(Node upperLeft, int[][] data,
      String name, DisplayOptions display, MatrixProperties imp);

  /**
   * Creates a new <code>DoubleMatrix</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>DoubleMatrix</code>.
   * @param data
   *          the initial data of the <code>DoubleMatrix</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>DoubleMatrix</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>DoubleMatrix</code>.
   */
  public DoubleMatrix newDoubleMatrix(Node upperLeft, double[][] data,
      String name, DisplayOptions display) {
    return newDoubleMatrix(upperLeft, data, name, display,
        new MatrixProperties());
  }

  /**
   * Creates a new <code>IntMatrix</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>IntMatrix</code>.
   * @param data
   *          the initial data of the <code>IntMatrix</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>IntMatrix</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>IntArray</code>.
   */
  public IntMatrix newIntMatrix(Node upperLeft, int[][] data, String name,
      DisplayOptions display) {
    return newIntMatrix(upperLeft, data, name, display, new MatrixProperties());
  }

  /**
   * Creates a new <code>StringArray</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>StringArray</code>.
   * @param data
   *          the initial data of the <code>StringArray</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>StringArray</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param sap
   *          [optional] user-defined properties to apply to this
   *          <code>StringArray</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>StringArray</code>.
   */
  public abstract StringArray newStringArray(Node upperLeft, String[] data,
      String name, ArrayDisplayOptions display, ArrayProperties sap);

  /**
   * Creates a new <code>StringArray</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>StringArray</code>.
   * @param data
   *          the initial data of the <code>StringArray</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>StringArray</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>StringArray</code>.
   */
  public StringArray newStringArray(Node upperLeft, String[] data, String name,
      ArrayDisplayOptions display) {
    return newStringArray(upperLeft, data, name, display, new ArrayProperties());
  }

  /**
   * Creates a new <code>StringMatrix</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>StringMatrix</code>.
   * @param data
   *          the initial data of the <code>StringMatrix</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>StringMatrix</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param smp
   *          [optional] user-defined properties to apply to this
   *          <code>StringMatrix</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>StringMatrix</code>.
   */
  public abstract StringMatrix newStringMatrix(Node upperLeft, String[][] data,
      String name, DisplayOptions display, MatrixProperties smp);

  /**
   * Creates a new <code>StringMatrix</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>StringMatrix</code>.
   * @param data
   *          the initial data of the <code>StringMatrix</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>StringMatrix</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>StringMatrix</code>.
   */
  public StringMatrix newStringMatrix(Node upperLeft, String[][] data,
      String name, DisplayOptions display) {
    return newStringMatrix(upperLeft, data, name, display,
        new MatrixProperties());
  }

  /**
   * Creates a new <code>ArrayMarker</code> object.
   * 
   * @param a
   *          the array which this <code>ArrayMarker</code> belongs to.
   * @param index
   *          the current index at which this <code>ArrayMarker</code> is
   *          located.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ArrayMarker</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param amp
   *          [optional] user-defined properties to apply to this
   *          <code>ArrayMarker</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return an <code>ArrayMarker</code>.
   */
  public abstract ArrayMarker newArrayMarker(ArrayPrimitive a, int index,
      String name, DisplayOptions display, ArrayMarkerProperties amp);

  /**
   * Creates a new <code>ArrayMarker</code> object.
   * 
   * @param a
   *          the array which this <code>ArrayMarker</code> belongs to.
   * @param index
   *          the current index at which this <code>ArrayMarker</code> is
   *          located.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ArrayMarker</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return an <code>ArrayMarker</code>.
   */
  public ArrayMarker newArrayMarker(ArrayPrimitive a, int index, String name,
      DisplayOptions display) {
    return newArrayMarker(a, index, name, display, new ArrayMarkerProperties());
  }

  /**
   * Creates a new <code>ListElement</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ListElement</code>.
   * @param pointers
   *          the number of pointers between 0 and 255.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ListElement</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param lp
   *          [optional] user-defined properties to apply to this
   *          <code>ListElement</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>ListElement</code>.
   */
  public abstract ListElement newListElement(Node upperLeft, int pointers,
      LinkedList<Object> ptrLocations, ListElement prev, ListElement next,
      String name, DisplayOptions display, ListElementProperties lp);

  /**
   * Creates a new <code>ListElement</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ListElement</code>.
   * @param pointers
   *          the number of pointers between 0 and 255.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ListElement</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param lp
   *          [optional] user-defined properties to apply to this
   *          <code>ListElement</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>ListElement</code>.
   */
  public ListElement newListElement(Node upperLeft, int pointers,
      LinkedList<Object> ptrLocations, ListElement prev, String name,
      DisplayOptions display, ListElementProperties lp) {
    return newListElement(upperLeft, pointers, ptrLocations, prev, null, name,
        display, lp);
  }

  /**
   * Creates a new <code>ListElement</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ListElement</code>.
   * @param pointers
   *          the number of pointers between 0 and 255.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ListElement</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>ListElement</code>.
   */
  public ListElement newListElement(Node upperLeft, int pointers,
      LinkedList<Object> ptrLocations, ListElement prev, String name,
      DisplayOptions display) {
    return newListElement(upperLeft, pointers, ptrLocations, prev, null, name,
        display, new ListElementProperties());
  }

  /**
   * Creates a new <code>SourceCode</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of this <code>SourceCode</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>SourceCode</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param sp
   *          [optional] user-defined properties to apply to this
   *          <code>SourceCode</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>SourceCode</code>.
   */
  public abstract SourceCode newSourceCode(Node upperLeft, String name,
      DisplayOptions display, SourceCodeProperties sp);

  /**
   * Creates a new <code>SourceCode</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of this <code>SourceCode</code>.
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>SourceCode</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>SourceCode</code>.
   */
  public SourceCode newSourceCode(Node upperLeft, String name,
      DisplayOptions display) {
    return newSourceCode(upperLeft, name, display, new SourceCodeProperties());
  }

  /**
   * Creates a new <code>Text</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>Text</code>
   * @param text
   *          the content of the <code>Text</code> element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Text</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param tp
   *          [optional] user-defined properties to apply to this
   *          <code>Text</code>. If you don't want any user-defined properties
   *          to be set, then set this parameter to "null", default properties
   *          will be applied in that case.
   * @return a <code>Text</code>.
   */
  public abstract Text newText(Node upperLeft, String text, String name,
      DisplayOptions display, TextProperties tp);

  /**
   * Creates a new <code>Text</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>Text</code>
   * @param text
   *          the content of the <code>Text</code> element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Text</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Text</code>.
   */
  public Text newText(Node upperLeft, String text, String name,
      DisplayOptions display) {
    Text aText = newText(upperLeft, text, name, display, new TextProperties());
    return aText;
  }

  /**
   * Creates a new <code>AndGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>AndGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this parameter to
   *          "null". The name is then created automatically.
   * @param pins
   *          the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>AndGate</code>.
   */
  public AndGate newAndGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    AndGate andGate = newAndGate(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return andGate;
  }

  /**
   * Creates a new <code>AndGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>AndGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this parameter to
   *          "null". The name is then created automatically.
   * @param pins
   *          the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties
   *          the graphical properties of this element
   * @return a <code>AndGate</code>.
   */
  public abstract AndGate newAndGate(Node upperLeft, int width, int height,
      String name, List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties);

  /**
   * Creates a new <code>ConceptualStack</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ConceptualStack</code> .
   * @param content
   *          the initial content of the <code>ConceptualStack</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ConceptualStack</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ConceptualStack</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param sp
   *          [optional] user-defined properties to apply to this
   *          <code>ConceptualStack</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>ConceptualStack</code>.
   */
  public abstract <T> ConceptualStack<T> newConceptualStack(Node upperLeft,
      List<T> content, String name, DisplayOptions display, StackProperties sp);

  /**
   * Creates a new <code>ConceptualStack</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ConceptualStack</code> .
   * @param content
   *          the initial content of the <code>ConceptualStack</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ConceptualStack</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ConceptualStack</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>ConceptualStack</code>.
   */
  public <T> ConceptualStack<T> newConceptualStack(Node upperLeft,
      List<T> content, String name, DisplayOptions display) {
    return newConceptualStack(upperLeft, content, name, display,
        new StackProperties());
  }

  /**
   * Creates a new <code>ArrayBasedStack</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ArrayBasedStack</code> .
   * @param content
   *          the initial content of the <code>ConceptualStack</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ArrayBasedStack</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ArrayBasedStack</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param sp
   *          [optional] user-defined properties to apply to this
   *          <code>ArrayBasedStack</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @param capacity
   *          the capacity limit of this <code>ArrayBasedStack</code>; must be
   *          nonnegative.
   * @return an <code>ArrayBasedStack</code>.
   */
  public abstract <T> ArrayBasedStack<T> newArrayBasedStack(Node upperLeft,
      List<T> content, String name, DisplayOptions display, StackProperties sp,
      int capacity);

  /**
   * Creates a new <code>ArrayBasedStack</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ArrayBasedStack</code> .
   * @param content
   *          the initial content of the <code>ConceptualStack</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ArrayBasedStack</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ArrayBasedStack</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param capacity
   *          the capacity limit of this <code>ArrayBasedStack</code>; must be
   *          nonnegative.
   * @return an <code>ArrayBasedStack</code>.
   */
  public <T> ArrayBasedStack<T> newArrayBasedStack(Node upperLeft,
      List<T> content, String name, DisplayOptions display, int capacity) {
    return newArrayBasedStack(upperLeft, content, name, display,
        new StackProperties(), capacity);
  }

  /**
   * Creates a new <code>ListBasedStack</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ListBasedStack</code>.
   * @param content
   *          the initial content of the <code>ConceptualStack</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ListBasedStack</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ListBasedStack</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param sp
   *          [optional] user-defined properties to apply to this
   *          <code>ListBasedStack</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>ListBasedStack</code>.
   */
  public abstract <T> ListBasedStack<T> newListBasedStack(Node upperLeft,
      List<T> content, String name, DisplayOptions display, StackProperties sp);

  /**
   * Creates a new <code>ListBasedStack</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ListBasedStack</code>.
   * @param content
   *          the initial content of the <code>ConceptualStack</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ListBasedStack</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ListBasedStack</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>ListBasedStack</code>.
   */
  public <T> ListBasedStack<T> newListBasedStack(Node upperLeft,
      List<T> content, String name, DisplayOptions display) {
    return newListBasedStack(upperLeft, content, name, display,
        new StackProperties());
  }

  /**
   * Creates a new <code>ConceptualQueue</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ConceptualQueue</code> .
   * @param content
   *          the initial content of the <code>ConceptualQueue</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ConceptualQueue</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ConceptualQueue</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param qp
   *          [optional] user-defined properties to apply to this
   *          <code>ConceptualQueue</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>ConceptualQueue</code>.
   */
  public abstract <T> ConceptualQueue<T> newConceptualQueue(Node upperLeft,
      List<T> content, String name, DisplayOptions display, QueueProperties qp);

  /**
   * Creates a new <code>ConceptualQueue</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ConceptualQueue</code> .
   * @param content
   *          the initial content of the <code>ConceptualQueue</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ConceptualQueue</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ConceptualQueue</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>ConceptualQueue</code>.
   */
  public <T> ConceptualQueue<T> newConceptualQueue(Node upperLeft,
      List<T> content, String name, DisplayOptions display) {
    return newConceptualQueue(upperLeft, content, name, display,
        new QueueProperties());
  }

  /**
   * Creates a new <code>ArrayBasedQueue</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ArrayBasedQueue</code> .
   * @param content
   *          the initial content of the <code>ConceptualQueue</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ArrayBasedQueue</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ArrayBasedQueue</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param qp
   *          [optional] user-defined properties to apply to this
   *          <code>ArrayBasedQueue</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @param capacity
   *          the capacity limit of this <code>ArrayBasedQueue</code>; must be
   *          nonnegative.
   * @return an <code>ArrayBasedQueue</code>.
   */
  public abstract <T> ArrayBasedQueue<T> newArrayBasedQueue(Node upperLeft,
      List<T> content, String name, DisplayOptions display, QueueProperties qp,
      int capacity);

  /**
   * Creates a new <code>ArrayBasedQueue</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ArrayBasedQueue</code> .
   * @param content
   *          the initial content of the <code>ConceptualQueue</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ArrayBasedQueue</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ArrayBasedQueue</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param capacity
   *          the capacity limit of this <code>ArrayBasedQueue</code>; must be
   *          nonnegative.
   * @return an <code>ArrayBasedQueue</code>.
   */
  public <T> ArrayBasedQueue<T> newArrayBasedQueue(Node upperLeft,
      List<T> content, String name, DisplayOptions display, int capacity) {
    return newArrayBasedQueue(upperLeft, content, name, display,
        new QueueProperties(), capacity);
  }

  /**
   * Creates a new <code>ListBasedQueue</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ListBasedQueue</code>.
   * @param content
   *          the initial content of the <code>ConceptualQueue</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ListBasedQueue</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ListBasedQueue</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param qp
   *          [optional] user-defined properties to apply to this
   *          <code>ListBasedQueue</code>. If you don't want any user-defined
   *          properties to be set, then set this parameter to "null", default
   *          properties will be applied in that case.
   * @return a <code>ListBasedQueue</code>.
   */
  public abstract <T> ListBasedQueue<T> newListBasedQueue(Node upperLeft,
      List<T> content, String name, DisplayOptions display, QueueProperties qp);

  /**
   * Creates a new <code>ListBasedQueue</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>ListBasedQueue</code>.
   * @param content
   *          the initial content of the <code>ConceptualQueue</code>,
   *          consisting of the elements of the generic type T. If the initial
   *          <code>ListBasedQueue</code> has no elements, <code>content</code>
   *          can be null or an empty <code>List</code> .
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>ListBasedQueue</code>. If you don't want to set this, set
   *          this parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>ListBasedQueue</code>.
   */
  public <T> ListBasedQueue<T> newListBasedQueue(Node upperLeft,
      List<T> content, String name, DisplayOptions display) {
    return newListBasedQueue(upperLeft, content, name, display,
        new QueueProperties());
  }

  /**
   * Creates a new element <code>Group</code> with a list of contained
   * <code>Primitive</code>s and a distinct name.
   * 
   * @param primitives
   *          the contained <code>Primitive</code>s.
   * @param name
   *          the name of this <code>Group</code>.
   * @return a new <code>Group</code> nobject.
   */
  public abstract Group newGroup(LinkedList<Primitive> primitives, String name);

  public abstract void addDocumentationLink(HtmlDocumentationModel docuLink);

  public abstract void addTFQuestion(TrueFalseQuestionModel tfQuestion);

  public abstract void addFIBQuestion(FillInBlanksQuestionModel fibQuestion);

  public abstract void addMCQuestion(MultipleChoiceQuestionModel mcQuestion);

  public abstract void addMSQuestion(MultipleSelectionQuestionModel msQuestion);

  public abstract void addQuestionGroup(QuestionGroupModel group);

  public abstract void setInteractionType(int interactionTypeCode);

  public abstract void hideAllPrimitives();

  public abstract void hideAllPrimitivesExcept(Primitive keepThisOne);

  public abstract void hideAllPrimitivesExcept(List<Primitive> keepThese);
}
