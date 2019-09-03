package algoanim.primitives.generators;

import java.util.List;
import java.util.Vector;

import algoanim.primitives.vhdl.AndGate;
import algoanim.primitives.vhdl.DFlipflop;
import algoanim.primitives.vhdl.Demultiplexer;
import algoanim.primitives.vhdl.JKFlipflop;
import algoanim.primitives.vhdl.Multiplexer;
import algoanim.primitives.vhdl.NAndGate;
import algoanim.primitives.vhdl.NorGate;
import algoanim.primitives.vhdl.NotGate;
import algoanim.primitives.vhdl.OrGate;
import algoanim.primitives.vhdl.RSFlipflop;
import algoanim.primitives.vhdl.TFlipflop;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.primitives.vhdl.VHDLPinType;
import algoanim.primitives.vhdl.VHDLWire;
import algoanim.primitives.vhdl.XNorGate;
import algoanim.primitives.vhdl.XOrGate;
import algoanim.properties.VHDLElementProperties;
import algoanim.properties.VHDLWireProperties;
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
public abstract class VHDLLanguage extends Language {

	public VHDLLanguage(String title, String author, int x, int y) {
	 super(title, author, x, y);
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
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>AndGate</code>.
   */
  public AndGate newAndGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    return newAndGate(upperLeft, width, height, name, pins, display, 
        new VHDLElementProperties());
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
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>AndGate</code>.
   */
  public abstract AndGate newAndGate(Node upperLeft, int width, int height, 
      String name, List<VHDLPin> pins,
      DisplayOptions display, VHDLElementProperties properties);
 
  /**
   * Creates a new <code>AndGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the element
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param inA the value for the first input gate, usually '0' or '1'
   * @param inB the value for the second input gate, usually '0' or '1'
   * @param outC the value for the output gate, usually '0' or '1'
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>AndGate</code>.
   */
  public AndGate newAndGate(Node upperLeft, int width, int height, 
      String name, char inA, char inB, char outC,
      DisplayOptions display, VHDLElementProperties properties) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(3);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "A", inA));
    pins.add(new VHDLPin(VHDLPinType.INPUT, "B", inB));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "C", outC));
    return newAndGate(upperLeft, width, height, name, pins, 
        display, properties);
  }

  /**
   * Creates a new <code>NAndGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>NAndGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>AndGate</code>.
   */
  public NAndGate newNAndGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    return newNAndGate(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
  }

  /**
   * Creates a new <code>NAndGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>NAndGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>NAndGate</code>.
   */
  public abstract NAndGate newNAndGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);

  /**
   * Creates a new <code>NAndGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the element
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param inA the value for the first input gate, usually '0' or '1'
   * @param inB the value for the second input gate, usually '0' or '1'
   * @param outC the value for the output gate, usually '0' or '1'
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>NAndGate</code>.
   */
  public NAndGate newNAndGate(Node upperLeft, int width, int height, 
      String name, char inA, char inB, char outC,
      DisplayOptions display, VHDLElementProperties properties) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(3);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "A", inA));
    pins.add(new VHDLPin(VHDLPinType.INPUT, "B", inB));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "C", outC));
    return newNAndGate(upperLeft, width, height, name, pins, 
        display, properties);
  }

  
  
  /**
   * Creates a new <code>NorGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>NorGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>NorGate</code>.
   */
  public NorGate newNorGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    NorGate norGate = newNorGate(upperLeft, width, height, name, pins, display, 
        new VHDLElementProperties());
    return norGate;
  }

  /**
   * Creates a new <code>NorGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>NorGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>NorGate</code>.
   */
  public abstract NorGate newNorGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);

  /**
   * Creates a new <code>NorGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the element
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param inA the value for the first input gate, usually '0' or '1'
   * @param inB the value for the second input gate, usually '0' or '1'
   * @param outC the value for the output gate, usually '0' or '1'
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>NorGate</code>.
   */
  public NorGate newNorGate(Node upperLeft, int width, int height, 
      String name, char inA, char inB, char outC,
      DisplayOptions display, VHDLElementProperties properties) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(3);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "A", inA));
    pins.add(new VHDLPin(VHDLPinType.INPUT, "B", inB));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "C", outC));
    return newNorGate(upperLeft, width, height, name, pins, 
        display, properties);
  }

  
  /**
   * Creates a new <code>NotGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>NotGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>AndGate</code>.
   */
  public NotGate newNotGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    NotGate notGate = newNotGate(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return notGate;
  }

  /**
   * Creates a new <code>NotGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>NotGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>NotGate</code>.
   */
  public abstract NotGate newNotGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);

  /**
   * Creates a new <code>NotGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the element
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param inA the value for the first input gate, usually '0' or '1'
   * @param outB the value for the output gate, usually '0' or '1'
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>NotGate</code>.
   */
  public NotGate newNotGate(Node upperLeft, int width, int height, 
      String name, char inA, char outB,
      DisplayOptions display, VHDLElementProperties properties) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(3);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "A", inA));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "B", outB));
    return newNotGate(upperLeft, width, height, name, pins, 
        display, properties);
  }

  
  
  /**
   * Creates a new <code>OrGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>OrGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>OrGate</code>.
   */
  public OrGate newOrGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    OrGate orGate = newOrGate(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return orGate;
  }

  /**
   * Creates a new <code>OrGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>OrGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>OrGate</code>.
   */
  public abstract OrGate newOrGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);

  /**
   * Creates a new <code>OrGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the element
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param inA the value for the first input gate, usually '0' or '1'
   * @param inB the value for the second input gate, usually '0' or '1'
   * @param outC the value for the output gate, usually '0' or '1'
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>OrGate</code>.
   */
  public OrGate newOrGate(Node upperLeft, int width, int height, 
      String name, char inA, char inB, char outC,
      DisplayOptions display, VHDLElementProperties properties) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(3);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "A", inA));
    pins.add(new VHDLPin(VHDLPinType.INPUT, "B", inB));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "C", outC));
    return newOrGate(upperLeft, width, height, name, pins, 
        display, properties);
  }

  
  /**
   * Creates a new <code>XNorGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>XNorGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>AndGate</code>.
   */
  public XNorGate newXNorGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    XNorGate xNorGate = newXNorGate(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return xNorGate;
  }

  /**
   * Creates a new <code>XNorGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>XNorGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>AndGate</code>.
   */
  public abstract XNorGate newXNorGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);

  /**
   * Creates a new <code>XNorGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the element
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param inA the value for the first input gate, usually '0' or '1'
   * @param inB the value for the second input gate, usually '0' or '1'
   * @param outC the value for the output gate, usually '0' or '1'
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>XNorGate</code>.
   */
  public XNorGate newXNorGate(Node upperLeft, int width, int height, 
      String name, char inA, char inB, char outC,
      DisplayOptions display, VHDLElementProperties properties) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(3);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "A", inA));
    pins.add(new VHDLPin(VHDLPinType.INPUT, "B", inB));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "C", outC));
    return newXNorGate(upperLeft, width, height, name, pins, 
        display, properties);
  }

  
  /**
   * Creates a new <code>XOrGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>XOrGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>XOrGate</code>.
   */
  public XOrGate newXOrGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    XOrGate xOrGate = newXOrGate(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return xOrGate;
  }

  /**
   * Creates a new <code>XOrGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>XOrGate</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>XOrGate</code>.
   */
  public abstract XOrGate newXOrGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);

  
  /**
   * Creates a new <code>XOrGate</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the element
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param inA the value for the first input gate, usually '0' or '1'
   * @param inB the value for the second input gate, usually '0' or '1'
   * @param outC the value for the output gate, usually '0' or '1'
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>XOrGate</code>.
   */
  public XOrGate newXOrGate(Node upperLeft, int width, int height, 
      String name, char inA, char inB, char outC,
      DisplayOptions display, VHDLElementProperties properties) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(3);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "A", inA));
    pins.add(new VHDLPin(VHDLPinType.INPUT, "B", inB));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "C", outC));
    return newXOrGate(upperLeft, width, height, name, pins, 
        display, properties);
  }

  /**
   * Creates a new <code>DFlipflop</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>DFlipflop</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>DFlipflop</code>.
   */
  public DFlipflop newDFlipflop(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    DFlipflop dFlipflop = newDFlipflop(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return dFlipflop;
  }

  /**
   * Creates a new <code>DFlipflop</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>DFlipflop</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>DFlipflop</code>.
   */
  public abstract DFlipflop newDFlipflop(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);

  
  /**
   * Creates a new <code>JKFlipflop</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>JKFlipflop</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>JKFlipflop</code>.
   */
  public JKFlipflop newJKFlipflop(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    JKFlipflop jkFlipflop = newJKFlipflop(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return jkFlipflop;
  }

  /**
   * Creates a new <code>JKFlipflop</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>JKFlipflop</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>JKFlipflop</code>.
   */
  public abstract JKFlipflop newJKFlipflop(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);
  
  /**
   * Creates a new <code>RSFlipflop</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>RSFlipflop</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>RSFlipflop</code>.
   */
  public RSFlipflop newRSFlipflop(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    RSFlipflop rsFlipflop = newRSFlipflop(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return rsFlipflop;
  }

  /**
   * Creates a new <code>RSFlipflop</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>RSFlipflop</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>RSFlipflop</code>.
   */
  public abstract RSFlipflop newRSFlipflop(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);
  
  /**
   * Creates a new <code>TFlipflop</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>TFlipflop</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>TFlipflop</code>.
   */
  public TFlipflop newTFlipflop(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    TFlipflop tFlipflop = newTFlipflop(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return tFlipflop;
  }

  /**
   * Creates a new <code>TFlipflop</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>TFlipflop</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>TFlipflop</code>.
   */
  public abstract TFlipflop newTFlipflop(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);
  
  /**
   * Creates a new <code>Demultiplexer</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>Demultiplexer</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Demultiplexer</code>.
   */
  public Demultiplexer newDemultiplexer(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    Demultiplexer demux = newDemultiplexer(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return demux;
  }

  /**
   * Creates a new <code>Demultiplexer</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>Demultiplexer</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>Demultiplexer</code>.
   */
  public abstract Demultiplexer newDemultiplexer(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);

  /**
   * Creates a new <code>Demultiplexer</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the element
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param inA the value for the first input gate, usually '0' or '1'
   * @param outB the value for the first output gate, usually '0' or '1'
   * @param outC the value for the second output gate, usually '0' or '1'
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>Demultiplexer</code>.
   */
  public Demultiplexer newDemultiplexer(Node upperLeft, int width, int height, 
      String name, char inA, char outB, char outC,
      DisplayOptions display, VHDLElementProperties properties) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(3);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "A", inA));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "B", outB));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "C", outC));
    return newDemultiplexer(upperLeft, width, height, name, pins, 
        display, properties);
  }

  
  /**
   * Creates a new <code>Multiplexer</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>Multiplexer</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Multiplexer</code>.
   */
  public Multiplexer newMultiplexer(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display) {
    Multiplexer mux = newMultiplexer(upperLeft, width, height, name, pins, display,
        new VHDLElementProperties());
    return mux;
  }
  

  /**
   * Creates a new <code>Demultiplexer</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the element
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param inA the value for the first input gate, usually '0' or '1'
   * @param inB the value for the second input gate, usually '0' or '1'
   * @param outC the value for the output gate, usually '0' or '1'
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>Multiplexer</code>.
   */
  public Multiplexer newMultiplexer(Node upperLeft, int width, int height, 
      String name, char inA, char inB, char outC,
      DisplayOptions display, VHDLElementProperties properties) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(3);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "A", inA));
    pins.add(new VHDLPin(VHDLPinType.INPUT, "B", inB));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "C", outC));
    return newMultiplexer(upperLeft, width, height, name, pins, 
        display, properties);
  }
  
  /**
   * Creates a new <code>Multiplexer</code> object.
   * 
   * @param upperLeft
   *          the upper left coordinates of the <code>Multiplexer</code>
   * @param width
   *          the width of the gate element
   * @param height
   *          the height of the gate element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          element. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param pins the pins used for this gate
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @param properties the graphical properties of this element
   * @return a <code>Multiplexer</code>.
   */
  public abstract Multiplexer newMultiplexer(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display, VHDLElementProperties properties);
 
  /**
   * Creates a new <code>wire</code> object.
   * 
   * @param nodes
   *          the nodes of this <code>wire</code>
   * @param speed
   *          the display speed of this <code>wire</code> element
   * @param name
   *          [optional] an arbitrary name which uniquely identifies this
   *          <code>Multiplexer</code>. If you don't want to set this, set this
   *          parameter to "null". The name is then created automatically.
   * @param display
   *          [optional] a subclass of <code>DisplayOptions</code> which
   *          describes additional display options like a hidden flag or
   *          timings.
   * @return a <code>Multiplexer</code>.
   */
  public abstract VHDLWire newWire(List<Node> nodes, int speed,
      String name, DisplayOptions display,
      VHDLWireProperties properties);


}
