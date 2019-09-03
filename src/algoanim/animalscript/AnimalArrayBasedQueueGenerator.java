package algoanim.animalscript;

import java.awt.Color;
import java.util.List;

import algoanim.primitives.ArrayBasedQueue;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.ArrayBasedQueueGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

public class AnimalArrayBasedQueueGenerator<T> extends AnimalGenerator implements
		ArrayBasedQueueGenerator<T> {
	
	private static int count = 1;
	private Node upperLeft;
	private ArrayProperties ap;
	private StringArray array;
	private String [] data;
	private int head = -1, tail = -1, capacity;
	private ArrayMarker head_am, tail_am, ht_am;
	private boolean hiddenProp, tail_am_hidden, highlightedFrontElem, highlightedFrontCell,
					highlightedTailElem, highlightedTailCell;

	public AnimalArrayBasedQueueGenerator(Language aLang) {
		super(aLang);
	}

	public void create(ArrayBasedQueue<T> abq) {
		if (this.isNameUsed(abq.getName()) || abq.getName() == "") {
			abq.setName("ArrayBasedQueue" + AnimalArrayBasedQueueGenerator.count);
			AnimalArrayBasedQueueGenerator.count++;
		}
		
		upperLeft = abq.getUpperLeft();
		
		ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				abq.getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY));
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				abq.getProperties().get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				abq.getProperties().get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
		ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				abq.getProperties().get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		ap.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
				abq.getProperties().get(AnimationPropertiesKeys.DEPTH_PROPERTY));
		hiddenProp = (Boolean) abq.getProperties().get(AnimationPropertiesKeys.HIDDEN_PROPERTY);
		ap.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		
		capacity = abq.getCapacity();
		data = new String[capacity];
		for (int i = 0; i < capacity; i++)
			data[i] = "";
		
		
		List<T> content = abq.getInitContent();
		if (content != null) {
			if (content.size() > abq.getCapacity())
				throw new ArrayIndexOutOfBoundsException("The initial content of the" +
						" ArrayBasedQueue has " + content.size() + " elements, but it may" +
						" not have more elements as the capacity of the ArrayBasedQueue" +
						" = " + abq.getCapacity());
			for (int i = 0; i < content.size(); i++) {
				data[i] = content.get(i).toString();
				tail++;
			}
		}
		
		ArrayDisplayOptions ado = (ArrayDisplayOptions) abq.getDisplayOptions();
		array = lang.newStringArray(upperLeft, data, "", ado, ap);
		ArrayMarkerProperties tail_amp = new ArrayMarkerProperties();
		tail_amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "tail");
		tail_amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, ap.get(
				AnimationPropertiesKeys.COLOR_PROPERTY));
		tail_amp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, ap.get(
				AnimationPropertiesKeys.DEPTH_PROPERTY));
		tail_amp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		
		ArrayMarkerProperties head_amp = new ArrayMarkerProperties();
		head_amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "head");
		head_amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, ap.get(
				AnimationPropertiesKeys.COLOR_PROPERTY));
		head_amp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, ap.get(
				AnimationPropertiesKeys.DEPTH_PROPERTY));
		head_amp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		
		ArrayMarkerProperties ht_amp = new ArrayMarkerProperties();
		ht_amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "head, tail");
		ht_amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, ap.get(
				AnimationPropertiesKeys.COLOR_PROPERTY));
		ht_amp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, ap.get(
				AnimationPropertiesKeys.DEPTH_PROPERTY));
		ht_amp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		
		head_am = lang.newArrayMarker(array, 0, "head", ado == null ? null : ado.getOffset(),
				head_amp);
		ht_am = lang.newArrayMarker(array, 0, "head_tail", ado == null ? null : ado.getOffset(),
				ht_amp);
		
		if (tail == -1) {
			tail_am = lang.newArrayMarker(array, 0, "tail", ado == null ? null : ado.getOffset(),
					tail_amp);
			tail_am.hide();
			tail_am_hidden = true;
			head_am.hide();
			ht_am.hide();
		}
		else {
			head = 0;
			if (tail == head)
				head_am.hide();
			else {
				ht_am.hide();
				tail_am = lang.newArrayMarker(array, tail, "tail", ado == null ?
						null : ado.getOffset(), tail_amp);
			}
		}
	}

	public void dequeue(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		int newHead = -1;
		
		if (highlightedFrontCell)
			unhighlightFrontCell(abq, delay, duration);
		if (highlightedFrontElem)
			unhighlightFrontElem(abq, delay, duration);

		if (head == tail) {
			ht_am.hide(delay);
			tail = -1;
		}
		else {
			newHead = (head + 1) % capacity;
			lang.nextStep(0);
			head_am.move(newHead, delay, duration);
			if (newHead == tail) {
				tail_am.hide(delay);
				tail_am_hidden = true;
				head_am.hide(delay);
				lang.nextStep(0);
				ht_am.move(newHead, delay, duration);
				if (!hiddenProp)
					ht_am.show();
			}
		}
		array.put(head, "", delay, duration);
		head = newHead;
	}

	public void enqueue(ArrayBasedQueue<T> abq, T elem, Timing delay, Timing duration) {
		if (highlightedTailCell)
			unhighlightTailCell(abq, delay, duration);
		if (highlightedTailElem)
			unhighlightTailElem(abq, delay, duration);
		
		if (tail == -1) {
			head = 0;
			lang.nextStep(0);
			ht_am.move(0, delay, duration);
			if (!hiddenProp)
				ht_am.show(delay);
		}
		else 
			if (tail == head) {
				ht_am.hide(delay);
				lang.nextStep(0);
				head_am.move(head, delay, duration);
				if (!hiddenProp)
					head_am.show(delay);
			}
		
		tail = (tail + 1) % capacity;
		
		lang.nextStep(0);
		tail_am.move(tail, delay, duration);
		
		array.put(tail, elem.toString(), delay, duration);
		if (tail != head && tail_am_hidden && !hiddenProp) {
			tail_am.show(delay);
			tail_am_hidden = false;
		}
	}

	public void front(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		highlightFrontCell(abq, delay, duration);
		highlightFrontElem(abq, delay, duration);
	}

	public void highlightFrontCell(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		array.highlightCell(head, delay, duration);
		highlightedFrontCell = true;
	}

	public void highlightFrontElem(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		array.highlightElem(head, delay, duration);
		highlightedFrontElem = true;
	}

	public void highlightTailCell(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		array.highlightCell(tail, delay, duration);
		highlightedTailCell = true;
	}

	public void highlightTailElem(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		array.highlightElem(tail, delay, duration);
		highlightedTailElem = true;
	}

	public void isEmpty(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		// does nothing yet:)
	}

	public void isFull(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		// does nothing yet:)
	}

	public void tail(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		highlightTailCell(abq, delay, duration);
		highlightTailElem(abq, delay, duration);
	}

	public void unhighlightFrontCell(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		array.unhighlightCell(head, delay, duration);
		highlightedFrontCell = false;
	}

	public void unhighlightFrontElem(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		array.unhighlightElem(head, delay, duration);
		highlightedFrontElem = false;
	}

	public void unhighlightTailCell(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		array.unhighlightCell(tail, delay, duration);
		highlightedTailCell = false;
	}

	public void unhighlightTailElem(ArrayBasedQueue<T> abq, Timing delay, Timing duration) {
		array.unhighlightElem(tail, delay, duration);
		highlightedTailElem = false;
	}
}
