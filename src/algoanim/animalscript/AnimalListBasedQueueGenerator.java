package algoanim.animalscript;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Group;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.ListElement;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.ListBasedQueueGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ListElementProperties;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnimalListBasedQueueGenerator<T> extends AnimalGenerator implements
		ListBasedQueueGenerator<T> {

	private static int count = 1;
	private Node upperLeft;
	private ListElementProperties lp;
	private ListElement head, tail;
	private Rect emptyQueue;
	private boolean hidden, hiddenProp, emptyHidden, highlightedTailCell, highlightedTailElem;
	private LinkedList<Primitive> list;
	private final static int DISTANCE = 70;
	int ulx, uly;
	
	public AnimalListBasedQueueGenerator(Language aLang) {
		super(aLang);
	}

	public void create(ListBasedQueue<T> lbq) {
		
		if (this.isNameUsed(lbq.getName()) || lbq.getName() == "") {
			lbq.setName("ListBasedQueue" + AnimalListBasedQueueGenerator.count);
			AnimalListBasedQueueGenerator.count++;
		}
		
		upperLeft = lbq.getUpperLeft();
		if (upperLeft instanceof Coordinates) {
			ulx = ((Coordinates) upperLeft).getX();
			uly = ((Coordinates) upperLeft).getY();
		} //TODO: Offset?
		
		Object queueColor = lbq.getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		hiddenProp = (Boolean) lbq.getProperties().get(AnimationPropertiesKeys.HIDDEN_PROPERTY);
		hidden = lbq.getDisplayOptions() instanceof Hidden;
		
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, queueColor);
		emptyQueue = lang.newRect(upperLeft, new Coordinates(ulx + 20, uly + 40),
				"", lbq.getDisplayOptions(), rp);
		
		lp = new ListElementProperties();
		
		lp.set(AnimationPropertiesKeys.COLOR_PROPERTY, queueColor);
		lp.set(AnimationPropertiesKeys.POINTERAREACOLOR_PROPERTY, queueColor);
		lp.set(AnimationPropertiesKeys.POSITION_PROPERTY, 
				AnimationPropertiesKeys.LIST_POSITION_BOTTOM);
		lp.set(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY,
				lbq.getProperties().get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		lp.set(AnimationPropertiesKeys.BOXFILLCOLOR_PROPERTY, Color.WHITE);
		lp.set(AnimationPropertiesKeys.POINTERAREAFILLCOLOR_PROPERTY, Color.WHITE);
		lp.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
				lbq.getProperties().get(AnimationPropertiesKeys.DEPTH_PROPERTY));
		lp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		
		list = new LinkedList<Primitive>();
		
		List<T> content = lbq.getInitContent();
		if (content != null) {
			ListIterator<T> li = content.listIterator();
			//lang.nextStep(0);
			while (li.hasNext())
				enqueue(lbq, li.next(), null, null);
		}
	}

	public void dequeue(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		head.hide(delay);
		list.removeFirst();
		if (!list.isEmpty()) {
			head = (ListElement) list.getFirst();
			Group newQueue = lang.newGroup(list, "");
			try {
			  if(autoSteps) lang.nextStep(0);
				newQueue.moveTo(AnimalScript.DIRECTION_NW, "translate", upperLeft, delay, duration);
			} catch (IllegalDirectionException e) {
				e.printStackTrace();
			}
		}
		else {
			if (!hidden && !hiddenProp) {
				emptyQueue.show(delay);
				emptyHidden = false;
			}
		}
	}

	public void enqueue(ListBasedQueue<T> lbq, T elem, Timing delay, Timing duration) {
		if (!emptyHidden) {
			emptyQueue.hide(delay);
			emptyHidden = true;
		}
		
		if (tail != null && highlightedTailElem)
			unhighlightTailElem(lbq, delay, duration);
		if (tail != null && highlightedTailCell)
			unhighlightTailCell(lbq, delay, duration);

		String text = elem.toString();
		lp.set(AnimationPropertiesKeys.TEXT_PROPERTY, text);
		//int tailTextWidth = Animal.getStringWidth(text, new Font("SansSerif", Font.PLAIN, 16));
		
		/*if (!list.isEmpty()) {
			Group formerStack = lang.newGroup(list, "");
			formerStack.moveBy("translate", topTextWidth + distance, 0, delay, duration);
			lang.nextStep(0);
		}	*/
       /* LinkedList<Object> locs = null;
        if (top != null) {
          locs = new LinkedList<Object>();
          locs.add(top);
        }*/
		
        Node newTailPosition = (tail == null) ? upperLeft :
        	new Offset(DISTANCE, 0, tail, AnimalScript.DIRECTION_NE);
		
        if(autoSteps) lang.nextStep(0);
		ListElement le = lang.newListElement(newTailPosition, 1, null, null, "", 
				hidden ? new Hidden() : null, lp);
		
		if (tail != null) {
		  if(autoSteps) lang.nextStep(0);
			tail.link(le, 1, delay, duration);
		}
		else	
			head = le;
		tail = le;
		list.add(le);
	}
	
	private boolean autoSteps = true;
	public void setAutoSteps(boolean autoSteps) {
	  this.autoSteps = autoSteps;
	}
  
  public void hide() {
    hidden = true;
    for(Primitive p : list) {
      p.hide(new TicksTiming(0));
    }
    emptyQueue.hide(new TicksTiming(0));
  }
  
  public void show() {
    hidden = true;
    for(Primitive p : list) {
      p.show(new TicksTiming(0));
    }
    if(list.isEmpty()) {
      emptyQueue.show(new TicksTiming(0));
    }
  }

	public void front(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		highlightFrontCell(lbq, delay, duration);
		highlightFrontElem(lbq, delay, duration);
	}

	public void highlightFrontCell(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		head.changeColor("fillColor", (Color) lbq.getProperties().get(
				AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), delay, duration);
		head.changeColor("pointer background color", (Color) lbq.getProperties().get(
				AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), delay, duration);
	}

	public void highlightFrontElem(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		head.changeColor("textcolor", (Color) lbq.getProperties().get(
				AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), delay,
				duration);
	}

	public void highlightTailCell(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		tail.changeColor("fillColor", (Color) lbq.getProperties().get(
				AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), delay, duration);
		tail.changeColor("pointer background color", (Color) lbq.getProperties().get(
				AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), delay, duration);
		highlightedTailCell = true;
	}

	public void highlightTailElem(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		tail.changeColor("textcolor", (Color) lbq.getProperties().get(
				AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), delay,
				duration);
		highlightedTailElem = true;
	}

	public void isEmpty(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		// does nothing yet:)
	}

	public void tail(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		highlightTailCell(lbq, delay, duration);
		highlightTailElem(lbq, delay, duration);
	}

	public void unhighlightFrontCell(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		head.changeColor("fillColor", Color.WHITE, delay, duration);
		head.changeColor("pointer background color", Color.WHITE, delay, duration);
	}

	public void unhighlightFrontElem(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		head.changeColor("textcolor", (Color) lp.get(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY),
				delay, duration);
	}

	public void unhighlightTailCell(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		tail.changeColor("fillColor", Color.WHITE, delay, duration);
		tail.changeColor("pointer background color", Color.WHITE, delay, duration);
		highlightedTailCell = false;
	}

	public void unhighlightTailElem(ListBasedQueue<T> lbq, Timing delay, Timing duration) {
		tail.changeColor("textcolor", (Color) lp.get(AnimationPropertiesKeys.TEXTCOLOR_PROPERTY),
				delay, duration);
		highlightedTailElem = true;
	}
}
