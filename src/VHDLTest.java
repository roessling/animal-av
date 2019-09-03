import java.awt.Color;
import java.util.List;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.VHDLLanguage;
import algoanim.primitives.vhdl.VHDLElement;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.primitives.vhdl.VHDLPinType;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.VHDLWireProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

/**
 * @author Dr. Guido R&ouml;&szlig;ling <roessling@acm.org>
 * @version 1.0 2007-05-30
 *
 */
public class VHDLTest {
		
	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height
		VHDLLanguage lang = new AnimalScript("Quicksort Animation", "Guido Rößling", 640, 480);
		lang.setStepMode(true);
    Vector<VHDLPin> pins = new Vector<VHDLPin>(10);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "a", '0'));
    VHDLPin newIn = new VHDLPin(VHDLPinType.INPUT, "b", '1');
    pins.add(newIn);
    VHDLPin out1 = new VHDLPin(VHDLPinType.OUTPUT, "a", '0');
    pins.add(out1);
    
    VHDLElement elem = lang.newAndGate(new Coordinates(20, 100), 80, 360, "myAnd", pins, null);  
    lang.nextStep();
    elem.hide();

    elem = lang.newNAndGate(new Coordinates(20, 100), 80, 360, "myNAnd", pins, null);  
    lang.nextStep();
    elem.hide();

    elem = lang.newNorGate(new Coordinates(20, 100), 80, 360, "myNOR", pins, null);  
    lang.nextStep();
    elem.hide();

    elem = lang.newNotGate(new Coordinates(20, 100), 80, 360, "myNOT", pins, null);  
    lang.nextStep();
    elem.hide();

    elem = lang.newOrGate(new Coordinates(20, 100), 80, 360, "myOr", pins, null);  
    lang.nextStep();
    elem.hide();

    elem = lang.newXNorGate(new Coordinates(20, 100), 80, 360, "myXNORd", pins, null);  
    lang.nextStep();
    elem.hide();

    elem = lang.newXOrGate(new Coordinates(20, 100), 80, 360, "myXor", pins, null);  
    lang.nextStep();
    elem.hide();


    elem = lang.newTFlipflop(new Coordinates(20, 100), 80, 360, "myT", pins, null);  
    lang.nextStep();
    elem.hide();

    VHDLPin newOut = new VHDLPin(VHDLPinType.OUTPUT, "B", 'X');
    
    pins.add(newOut);
    elem = lang.newDFlipflop(new Coordinates(20, 100), 80, 360, "myD", pins, null);  
    lang.nextStep();
    elem.hide();

    elem = lang.newJKFlipflop(new Coordinates(20, 100), 80, 360, "myJK", pins, null);  
    lang.nextStep();
    elem.hide();

    elem = lang.newRSFlipflop(new Coordinates(20, 100), 80, 360, "myRS", pins, null);  
    lang.nextStep();
    elem.hide();

    pins.remove(newIn);
    VHDLPin control = new VHDLPin(VHDLPinType.CONTROL, "S0", '1');
    pins.add(control);
//    pins.add(new VHDLPin(VHDLPinType.CONTROL, "S1", '1'));
    elem = lang.newDemultiplexer(new Coordinates(20, 100), 80, 360, "myDemux", pins, null);  
    lang.nextStep();
    elem.hide();

    pins.remove(newOut);
    pins.remove(control);
    pins.remove(out1);
    pins.add(newIn);
    pins.add(new VHDLPin(VHDLPinType.INPUT, "c", '1'));
    pins.add(new VHDLPin(VHDLPinType.INPUT, "d", '1'));
    pins.add(control);
    pins.add(new VHDLPin(VHDLPinType.CONTROL, "S1", '0'));
    pins.add(out1);
    elem = lang.newMultiplexer(new Coordinates(20, 100), 80, 360, "myMux", pins, null);  
    lang.nextStep();
    elem.hide();
    
    VHDLWireProperties wireProps = new VHDLWireProperties();
    wireProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    wireProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    wireProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
    List<Node> nodes = new Vector<Node>(10, 3);
    nodes.add(new Coordinates(10, 30));
    nodes.add(new Coordinates(50, 30));
    nodes.add(new Coordinates(50, 80));
    nodes.add(new Coordinates(80, 80));
//    VHDLWire wire =
        lang.newWire(nodes, 0, "WireA", null, wireProps);
    lang.nextStep();

    System.out.println(lang);
	}
}
