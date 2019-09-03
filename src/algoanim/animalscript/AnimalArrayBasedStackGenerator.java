package algoanim.animalscript;

import java.awt.Color;
import java.util.List;

import algoanim.primitives.ArrayBasedStack;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.ArrayBasedStackGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * @author Dima Vronskyi
 *
 */
public class AnimalArrayBasedStackGenerator<T> extends AnimalGenerator implements ArrayBasedStackGenerator<T> {

	private static int count = 1;
	private Node upperLeft;
	private ArrayProperties ap;
	private StringArray array;
	private String [] data;
	private int top = -1, capacity;
	private ArrayMarker am;
	private boolean hiddenProp, am_hidden, highlightedTopElem, highlightedTopCell;
	
	
	public AnimalArrayBasedStackGenerator(Language aLang) {
		super(aLang);
	}


	public void create(ArrayBasedStack<T> abs) {
		if (this.isNameUsed(abs.getName()) || abs.getName() == "") {
			abs.setName("ArrayBasedStack" + AnimalArrayBasedStackGenerator.count);
			AnimalArrayBasedStackGenerator.count++;
		}
		
		upperLeft = abs.getUpperLeft();
		
		ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				abs.getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY));
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				abs.getProperties().get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				abs.getProperties().get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
		ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				abs.getProperties().get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		ap.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
				abs.getProperties().get(AnimationPropertiesKeys.DEPTH_PROPERTY));
		hiddenProp = (Boolean) abs.getProperties().get(AnimationPropertiesKeys.HIDDEN_PROPERTY);
		ap.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		
		capacity = abs.getCapacity();
		data = new String[capacity];
		for (int i = 0; i < capacity; i++)
			data[i] = "";
		
		
		List<T> content = abs.getInitContent();
		if (content != null) {
			if (content.size() > abs.getCapacity())
				throw new ArrayIndexOutOfBoundsException("The initial content of the" +
						" ArrayBasedStack has " + content.size() + " elements, but it may" +
								" not have more elements as the capacity of the ArrayBasedStack" +
								" = " + abs.getCapacity());
/*			content.toArray(data);
			data[content.size()] = "";
			top = content.size() - 1;*/
			//T [] contentArray = (T[]) content.toArray();
			for (int i = 0; i < content.size(); i++) {
				data[i] = content.get(i).toString();
				top++;
			}
			/*ListIterator<T> li = content.listIterator();
			while (li.hasNext()) {
				push(abs, li.next(), null, null);
				//GR
				lang.nextStep();
				lang.addLine("echo bounds: \"StringArray1[0]\"");
			}*/
		}
		
		ArrayDisplayOptions ado = (ArrayDisplayOptions) abs.getDisplayOptions();
		array = lang.newStringArray(upperLeft, data, "", ado, ap);
		ArrayMarkerProperties amp = new ArrayMarkerProperties();
		amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "top");
		amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, ap.get(
				AnimationPropertiesKeys.COLOR_PROPERTY));
		amp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, ap.get(
				AnimationPropertiesKeys.DEPTH_PROPERTY));
		amp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		if (top == -1) {
			am = lang.newArrayMarker(array, 0, "top", ado == null ? null : ado.getOffset(), amp);
			am.hide();
			am_hidden = true;
		}
		else
			am = lang.newArrayMarker(array, top, "top", ado == null ? null : ado.getOffset(), amp);
	}

	public void isFull(ArrayBasedStack<T> abs, Timing delay, Timing duration) {
		// does nothing yet:)
	}

	public void pop(ArrayBasedStack<T> abs, Timing delay, Timing duration) {
		if (highlightedTopCell)
			unhighlightTopCell(abs, delay, duration);
		if (highlightedTopElem)
			unhighlightTopElem(abs, delay, duration);

		top--;
		if (top == -1) {
			am.hide(delay);
			am_hidden = true;
		}
		else {
			lang.nextStep(0);
			am.move(top, delay, duration);
		}
		array.put(top+1, "", delay, duration);
	}

	public void push(ArrayBasedStack<T> abs, T elem, Timing delay, Timing duration) {
		if (highlightedTopCell)
			unhighlightTopCell(abs, delay, duration);
		if (highlightedTopElem)
			unhighlightTopElem(abs, delay, duration);
		top++;
		//lang.addLine("echo bounds: \"StringArray1[0]\"");
		lang.nextStep(0); //TODO
		am.move(top, delay, duration);
		//lang.addLine("echo bounds: \"StringArray1[0]\"");
		array.put(top, elem.toString(), delay, duration);
		if (am_hidden && !hiddenProp) {
			am.show(delay);
			am_hidden = false;
		}
	}

	public void top(ArrayBasedStack<T> abs, Timing delay, Timing duration) {
		highlightTopCell(abs, delay, duration);
		highlightTopElem(abs, delay, duration);
	}


	public void isEmpty(ArrayBasedStack<T> abs, Timing delay, Timing duration) {
		// does nothing yet:)
	}


	public void highlightTopCell(ArrayBasedStack<T> abs, Timing delay, Timing duration) {
		array.highlightCell(top, delay, duration);
		highlightedTopCell = true;
	}


	public void highlightTopElem(ArrayBasedStack<T> abs, Timing delay, Timing duration) {
		array.highlightElem(top, delay, duration);
		highlightedTopElem = true;
	}


	public void unhighlightTopCell(ArrayBasedStack<T> abs, Timing delay, Timing duration) {
		array.unhighlightCell(top, delay, duration);
		highlightedTopCell = false;
	}


	public void unhighlightTopElem(ArrayBasedStack<T> abs, Timing delay, Timing duration) {
		array.unhighlightElem(top, delay, duration);
		highlightedTopElem = false;
	}
}
