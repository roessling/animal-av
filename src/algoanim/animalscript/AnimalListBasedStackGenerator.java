package algoanim.animalscript;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Group;
import algoanim.primitives.ListBasedStack;
import algoanim.primitives.ListElement;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.ListBasedStackGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ListElementProperties;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.main.Animal;

/**
 * @author Dima Vronskyi
 *
 */
public class AnimalListBasedStackGenerator<T> extends AnimalGenerator implements ListBasedStackGenerator<T> {

	private static int count = 1;
	private Node upperLeft;
	private ListElementProperties lp;
	private ListElement top;
	private Rect emptyStack;
	private boolean hidden, hiddenProp, emptyHidden, highlightedTopCell, highlightedTopElem;
	private LinkedList<Primitive> list;
	private final static int DISTANCE = 70;
	int ulx, uly;
	
	public AnimalListBasedStackGenerator(Language aLang) {
		super(aLang);
	}

	public void create(ListBasedStack<T> lbs) {
		if (this.isNameUsed(lbs.getName()) || lbs.getName() == "") {
			lbs.setName("ListBasedStack" + AnimalListBasedStackGenerator.count);
			AnimalListBasedStackGenerator.count++;
		}
		
		upperLeft = lbs.getUpperLeft();
		if (upperLeft instanceof Coordinates) {
			ulx = ((Coordinates) upperLeft).getX();
			uly = ((Coordinates) upperLeft).getY();
		} //TODO: Offset?
		
		Object stackColor = lbs.getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		hiddenProp = (Boolean) lbs.getProperties().get(AnimationPropertiesKeys.HIDDEN_PROPERTY);
		hidden = lbs.getDisplayOptions() instanceof Hidden;
		
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, stackColor);
		emptyStack = lang.newRect(upperLeft, new Coordinates(ulx + 20, uly + 40),
				"", lbs.getDisplayOptions(), rp);
		
		lp = new ListElementProperties();
		
		lp.set(AnimationPropertiesKeys.COLOR_PROPERTY, stackColor);
		lp.set(AnimationPropertiesKeys.POINTERAREACOLOR_PROPERTY, stackColor);
		lp.set(AnimationPropertiesKeys.POSITION_PROPERTY, 
				AnimationPropertiesKeys.LIST_POSITION_BOTTOM);
		lp.set(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY,
				lbs.getProperties().get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		lp.set(AnimationPropertiesKeys.BOXFILLCOLOR_PROPERTY, Color.WHITE);
				//lbs.getProperties().get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		lp.set(AnimationPropertiesKeys.POINTERAREAFILLCOLOR_PROPERTY, Color.WHITE);
		lp.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
				lbs.getProperties().get(AnimationPropertiesKeys.DEPTH_PROPERTY));
		lp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		
		list = new LinkedList<Primitive>();
		
		List<T> content = lbs.getInitContent();
		if (content != null) {
			ListIterator<T> li = content.listIterator();
			lang.nextStep(0);
			while (li.hasNext())
				push(lbs, li.next(), null, null);
		}
	}

	public void pop(ListBasedStack<T> lbs, Timing delay, Timing duration) {
		top.hide(delay);
		list.removeLast();
		if (!list.isEmpty()) {
			top = (ListElement) list.getLast();
			Group newStack = lang.newGroup(list, "");
			try {
				lang.nextStep(0);
				newStack.moveTo(AnimalScript.DIRECTION_NW, "translate", upperLeft, delay, duration);
			} catch (IllegalDirectionException e) {
				e.printStackTrace();
			}
		}
		else {
			if (!hidden && !hiddenProp) {
				emptyStack.show(delay);
				emptyHidden = false;
			}
		}
	}

	public void push(ListBasedStack<T> lbs, T elem, Timing delay, Timing duration) {
		
		if (!emptyHidden) {
			emptyStack.hide(delay);
			emptyHidden = true;
		}
		
		if (top != null && highlightedTopElem)
			unhighlightTopElem(lbs, delay, duration);
		if (top != null && highlightedTopCell)
			unhighlightTopCell(lbs, delay, duration);
	
		/*LinkedList<Object> ptrLoc = new LinkedList<Object>();
		if (top != null && ptrLoc.isEmpty())
			ptrLoc.add(top);*/

		String text = elem.toString();
		lp.set(AnimationPropertiesKeys.TEXT_PROPERTY, text);
		int topTextWidth = Animal.getStringWidth(text, new Font("SansSerif", Font.PLAIN, 16));
		
		if (!list.isEmpty()) {
			Group formerStack = lang.newGroup(list, "");
			formerStack.moveBy("translate", topTextWidth + DISTANCE, 0, delay, duration);
			lang.nextStep(0);
		}	
        /*LinkedList<Object> locs = null;
        if (top != null) {
          locs = new LinkedList<Object>();
          locs.add(top);
        }*/
       
        //lang.nextStep(0);
		/*ListElement le = lang.newListElement(upperLeft, 1, locs, null, "", 
				hidden ? new Hidden() : null, lp);*/
		
        lang.nextStep(0);
		ListElement le = lang.newListElement(upperLeft, 1, null, null, top, "", 
				hidden ? new Hidden() : null, lp);
		
//        ListElement le = lang.newListElement(upperLeft, 1, locs, null, "", lbs.getDisplayOptions(), lp);
		//lang.nextStep(0);
		/*if (top != null)
			le.link(top, 1, delay, duration);*/
		
		top = le;
		list.add(le);
	}

	public void top(ListBasedStack<T> lbs, Timing delay, Timing duration) {
		// TODO Auto-generated method stub
		highlightTopCell(lbs, delay, duration);
		highlightTopElem(lbs, delay, duration);
	}

	public void isEmpty(ListBasedStack<T> lbs, Timing delay, Timing duration) {
		// does nothing yet:)
	}
	
	public void highlightTopCell(ListBasedStack<T> lbs, Timing delay,
			Timing duration) {
		top.changeColor("fillColor", (Color) lbs.getProperties().get(
				AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), delay, duration);
		top.changeColor("pointer background color", (Color) lbs.getProperties().get(
				AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), delay, duration);
		highlightedTopCell = true;
	}

	public void highlightTopElem(ListBasedStack<T> lbs, Timing delay,
			Timing duration) {
		top.changeColor("textcolor", (Color) lbs.getProperties().get(
				AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), delay,
				duration);
		highlightedTopElem = true;
	}

	public void unhighlightTopCell(ListBasedStack<T> lbs, Timing delay,
			Timing duration) {
		top.changeColor("fillColor", Color.WHITE, delay, duration);
		top.changeColor("pointer background color", Color.WHITE, delay, duration);
		highlightedTopCell = false;
	}

	public void unhighlightTopElem(ListBasedStack<T> lbs, Timing delay,
			Timing duration) {
		top.changeColor("textcolor", (Color) lp.get(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY),
				delay, duration);
		highlightedTopElem = false;
	}

}
