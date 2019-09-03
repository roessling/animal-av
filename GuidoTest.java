import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ListElement;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ListElementProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;


public class GuidoTest {

	
	/*public static <T> ConceptualStack<T> newConceptualStack(T dummy,
			Coordinates coords, String name, Timing timing, StackProperties sp) {
		// ConceptualStack
		ConceptualStackGenerator<T> csg = new AnimalConceptualStackGenerator<T>(l);
		ConceptualStack<T> result = new ConceptualStack<T>(csg, coords, name, timing, sp);
		return result;
	}
	public static <T> ConceptualStack<T> newConceptualStack(ConceptualStackGenerator<T> csg,
			Coordinates coords, String name, Timing timing, StackProperties sp) {
		ConceptualStack<T> result = new ConceptualStack<T>(csg, coords, name, timing, sp);
		return result;
	}*/
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Language l = new AnimalScript("ConceptualStack Animation", "Dima Vronskyi", 640, 480);
		l.setStepMode(true);
        ListElementProperties lep = new ListElementProperties();
        lep.set(AnimationPropertiesKeys.POSITION_PROPERTY, AnimationPropertiesKeys.LIST_POSITION_BOTTOM);
        lep.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
        lep.set(AnimationPropertiesKeys.BOXFILLCOLOR_PROPERTY, Color.blue);
        lep.set(AnimationPropertiesKeys.POINTERAREACOLOR_PROPERTY, Color.green);
        lep.set(AnimationPropertiesKeys.POINTERAREAFILLCOLOR_PROPERTY, Color.yellow);
        lep.set(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY, Color.cyan);
        lep.set(AnimationPropertiesKeys.TEXT_PROPERTY, "ctx");
        ListElement le = l.newListElement(new Coordinates(10,10), 3, null, null, "le1", null, lep);
        l.nextStep();
        le = l.newListElement(new Coordinates(100, 100), 2, null, le, "le2", new TicksTiming(5), lep);
 /*       Rect rect = l.newRect(new Coordinates(10, 10), new Coordinates(10,20), "myRect", new MsTiming(20));
        l.nextStep();
        rect = l.newRect(new Coordinates(10, 110), new Coordinates(10,120), "myRect2", new Hidden());
        l.nextStep();
        RectProperties rp = new RectProperties();
        rp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
        rect = l.newRect(new Coordinates(10, 210), new Coordinates(10,220), "myRect2", new Hidden(), rp);
        l.nextStep();
        rect = l.newRect(new Coordinates(10, 210), new Coordinates(10,220), "myRect2", new TicksTiming(25), rp);
        l.nextStep();
    	StackProperties sp = new StackProperties();
		LinkedList<String> content = null;
		
		sp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		sp.set(AnimationPropertiesKeys.DIVIDINGLINE_COLOR_PROPERTY, Color.ORANGE);
		sp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		sp.set(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY, true);
		sp.set(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY, Color.GREEN);
		sp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		sp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
		//sp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
//		ConceptualStack<Integer> cs = new ConceptualStack<Integer>(csg, new Coordinates(10,20),"CStack",
//				null, sp);
		content = new LinkedList<String>();
		content.add("10");
		content.add("20");
		content.add("30");
		
		Timing defaultTiming = new TicksTiming(50);
		
		// ConceptualStack
		ConceptualStack<String> cs = l.newConceptualStack(new Coordinates(10, 20), content,
				"CStack", null, sp);
//		ConceptualStack<Integer> cs2 = newConceptualStack(csg, new Coordinates(10, 20), "CStack",
//				null, sp);
		l.nextStep();
		cs.push("1l", null, null);
		l.nextStep();
		cs.top(null, null);
		l.nextStep();
		cs.push("j2Q", defaultTiming, defaultTiming);
		l.nextStep();
		cs.highlightTopCell(defaultTiming, defaultTiming);
		l.nextStep();
		cs.push("32gh", null, null);
		l.nextStep();
		cs.push("1563756745", null, null);
		l.nextStep();
		cs.push("21", null, null);
		l.nextStep();
		cs.top(defaultTiming, new TicksTiming(100));
		l.nextStep();
		cs.push("32bQqIj", null, null);
		l.nextStep();
		cs.pop(defaultTiming, defaultTiming);
		l.nextStep();
		cs.pop(null, null);
		l.nextStep();
		cs.push("7", null, null);
		l.nextStep();
		cs.top(null, null);
		l.nextStep();
		cs.pop(null, null);
		l.nextStep();
		cs.pop(null, null);
		l.nextStep();
		cs.pop(null, null);
		l.nextStep();
		cs.pop(null, null);
		l.nextStep();
		cs.pop(null, null);
		l.nextStep();
		cs.push("7", null, null);
		l.nextStep();
		cs.top(null, null);
		l.nextStep();
		cs.push("17", null, null);
//		l.nextStep();
//		cs.pop(null, null);*/
		/*VisualStack cs1 = new ConceptualStack<Integer>(csg, new Coordinates(250,20),"CStack",
				null, sp);*/
		
		// ArrayBasedStack
/*		//sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		//sp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ArrayDisplayOptions ado = new ArrayDisplayOptions(defaultTiming, null, false);
		ArrayBasedStack<Integer> abs = l.newArrayBasedStack(new Coordinates(10,70), content,
				"AbStack", null, sp, 6);
		l.nextStep();
		abs.pop(null, null);
		l.nextStep();
		abs.pop(null, null);
		l.nextStep();
		abs.pop(null, null);
		l.nextStep();

		abs.push(11, null, null);
		l.nextStep();
		abs.push(12, defaultTiming, defaultTiming);
		l.nextStep();
		abs.highlightTopElem(null, null);
		l.nextStep();
		abs.push(13, null, null);
		l.nextStep();
		abs.pop(defaultTiming, defaultTiming);
		l.nextStep();
		abs.highlightTopCell(null, null);;
		l.nextStep();
		abs.pop(null, null);
		l.nextStep();
		abs.top(null, null);
		l.nextStep();
		abs.pop(null, null);*/
		
		// ListBasedStack
/*		ListBasedStack<Integer> lbs = l.newListBasedStack(new Coordinates(10,70), content, "LbStack",
				null, sp);
		l.nextStep();
		lbs.push(1, null, null);
		l.nextStep();
		lbs.push(658742, null, null);
		l.nextStep();
		lbs.push(378765937, null, null);
		l.nextStep();
		lbs.pop(null, null);
		l.nextStep();
		lbs.pop(null, null);
		l.nextStep();
		lbs.pop(null, null);*/
		
        // ListElement le = l.newListElement(new Coordinates(10, 10), 1, null, null, "l", null);
		System.out.println(l);
		
	}

}