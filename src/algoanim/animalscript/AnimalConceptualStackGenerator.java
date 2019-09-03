package algoanim.animalscript;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import algoanim.primitives.ConceptualStack;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.ConceptualStackGenerator;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.main.Animal;

/**
 * @see algoanim.primitives.generators.ConceptualStackGenerator
 * @author Dima Vronskyi
 */
public class AnimalConceptualStackGenerator<T> extends AnimalGenerator
		implements ConceptualStackGenerator<T> {

	private static int count = 1;

	private Node upperLeft;

	private LinkedList<Primitive> rectangles, texts;

	private TextProperties tp;

	private RectProperties rp;

	private int maxWidth = 0;

	private int ulx, uly, fontSize, heightOffset;

	private Text topText;

	private Rect topRect;

	private Group rectanglesToMove, textsToMove;

	private Polyline pLine;

	private Object fontObject;

	private boolean hidden, hiddenProp, highlightedTopText, highlightedTopRect,
			alternateFilled, odd;

	private final static int WIDTH_OFFSET = 2;

	/**
	 * @param aLang
	 *            the associated <code>Language</code> object.
	 */
	public AnimalConceptualStackGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.ConceptualStackGenerator
	 *      #create(algoanim.primitives.ConceptualStack)
	 */
	public void create(ConceptualStack<T> cs) {
		if (this.isNameUsed(cs.getName()) || cs.getName() == "") {
			cs
					.setName("ConceptualStack"
							+ AnimalConceptualStackGenerator.count);
			AnimalConceptualStackGenerator.count++;
		}

		lang.addItem(cs);

		upperLeft = cs.getUpperLeft();
		if (upperLeft instanceof Coordinates) {
			ulx = ((Coordinates) upperLeft).getX();
			uly = ((Coordinates) upperLeft).getY();
		} // TODO Offset? absolute coordinates?

		PolylineProperties plp = new PolylineProperties();
		plp.set(AnimationPropertiesKeys.COLOR_PROPERTY, cs.getProperties().get(
				AnimationPropertiesKeys.COLOR_PROPERTY));
		Integer stackDepth = (Integer) cs.getProperties().get(
				AnimationPropertiesKeys.DEPTH_PROPERTY);
		if (stackDepth.compareTo(Integer.MAX_VALUE) < 0)
			plp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, stackDepth);
		else
			plp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, stackDepth - 1);
		hiddenProp = (Boolean) cs.getProperties().get(
				AnimationPropertiesKeys.HIDDEN_PROPERTY);
		hidden = cs.getDisplayOptions() instanceof Hidden;
		plp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);

		Node[] vertices = new Node[6];
		vertices[0] = upperLeft;
		vertices[1] = new Coordinates(ulx + 20, uly);
		vertices[2] = new Coordinates(ulx + 20, uly + 10);
		vertices[3] = new Coordinates(ulx + 40, uly + 10);
		vertices[4] = new Coordinates(ulx + 40, uly);
		vertices[5] = new Coordinates(ulx + 60, uly);
		pLine = lang.newPolyline(vertices, "", cs.getDisplayOptions(), plp);

		tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, cs.getProperties().get(
				AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, plp
				.get(AnimationPropertiesKeys.DEPTH_PROPERTY));
		tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		fontObject = cs.getProperties().get(
				AnimationPropertiesKeys.FONT_PROPERTY);
		fontSize = ((Font) fontObject).getSize();
		heightOffset = fontSize / 2;
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, fontObject);
		tp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);

		rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, cs.getProperties().get(
				AnimationPropertiesKeys.DIVIDINGLINE_COLOR_PROPERTY));
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, (Integer) tp
				.get(AnimationPropertiesKeys.DEPTH_PROPERTY) + 1);
		alternateFilled = (Boolean) cs.getProperties().get(
				AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);

		rectangles = new LinkedList<Primitive>();
		texts = new LinkedList<Primitive>();

		List<T> content = cs.getInitContent();
		if (content != null) {
			ListIterator<T> li = content.listIterator();
			while (li.hasNext())
				push(cs, li.next(), null, null);
		}
	}

	public void pop(ConceptualStack<T> cs, Timing delay, Timing duration) {
		topText.hide(delay);
		topRect.hide(delay);
		rectangles.removeLast();
		texts.removeLast();
		odd = !odd;
		String text = topText.getText();
		int width = Animal.getStringWidth(text, (Font) fontObject) + 2
				* WIDTH_OFFSET;
		if (!texts.isEmpty()) {
			topRect = (Rect) rectangles.getLast();
			topText = (Text) texts.getLast();
			// Group newStack = lang.newGroup(primitives, "");
			textsToMove = lang.newGroup(texts, "");
			rectanglesToMove = lang.newGroup(rectangles, "");
			textsToMove.moveBy("translate", 0, -(fontSize + heightOffset),
					delay, duration);
			rectanglesToMove.moveBy("translate", 0, -(fontSize + heightOffset),
					delay, duration);
			pLine.moveBy("translateNodes 3 4", 0, -(fontSize + heightOffset),
					delay, duration);
			if (width == maxWidth)
				narrowStack(delay, duration);
		} else {
			pLine.moveBy("translateNodes 3 4", 0,
					10 - (fontSize + heightOffset), delay, duration);
			pLine.moveBy("translateNodes 4 5 6", 20 - maxWidth, 0, delay,
					duration);
			maxWidth = 0;
		}
	}

	private void narrowStack(Timing delay, Timing duration) {
		int newMaxWidth = 0;
		Iterator<Primitive> it = texts.iterator();
		while (it.hasNext()) {
			int width = Animal.getStringWidth(((Text) it.next()).getText(),
					(Font) fontObject)
					+ 2 * WIDTH_OFFSET;
			if (width > newMaxWidth)
				newMaxWidth = width;
		}
		rectanglesToMove.moveBy("translate #2", newMaxWidth - maxWidth, 0,
				delay, duration);
		textsToMove.moveBy("translate", (newMaxWidth - maxWidth) / 2, 0, delay,
				duration);
		pLine.moveBy("translateNodes 4 5 6", newMaxWidth - maxWidth, 0, delay,
				duration);
		maxWidth = newMaxWidth;
	}

	public void push(ConceptualStack<T> cs, T elem, Timing delay,
			Timing duration) {
		String text = elem.toString();
		int width = Animal.getStringWidth(text, (Font) fontObject) + 2
				* WIDTH_OFFSET;

		if (!texts.isEmpty()) {
			textsToMove = lang.newGroup(texts, "");
			rectanglesToMove = lang.newGroup(rectangles, "");

			textsToMove.moveBy("translate", 0, fontSize + heightOffset, delay,
					duration);
			rectanglesToMove.moveBy("translate", 0, fontSize + heightOffset,
					delay, duration);
			pLine.moveBy("translateNodes 3 4", 0, fontSize + heightOffset,
					delay, duration);

			if (maxWidth < width)
				expandStack(width, delay, duration);

		} else {
			maxWidth = width;
			pLine.moveBy("translateNodes 4 5 6", maxWidth - 20, 0, delay,
					duration);
			pLine.moveBy("translateNodes 3 4", 0, fontSize + heightOffset - 10,
					delay, duration);
		}

		if (topText != null && highlightedTopText)
			unhighlightTopElem(cs, delay, duration);
		if (topRect != null && highlightedTopRect)
			unhighlightTopCell(cs, delay, duration);

		rp.set(AnimationPropertiesKeys.FILL_PROPERTY,
				odd && alternateFilled ? cs.getProperties().get(
						AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY)
						: Color.WHITE);

		topText = lang.newText(new Coordinates(ulx + 20 + maxWidth / 2, uly),
				text, "", hidden ? new Hidden() : delay, tp);
		topRect = lang.newRect(new Coordinates(ulx + 20, uly), new Coordinates(
				ulx + 20 + maxWidth, uly + fontSize + heightOffset), "",
				hidden ? new Hidden() : delay, rp);

		odd = !odd;
		texts.add(topText);
		rectangles.add(topRect);
	}

	private void expandStack(int width, Timing delay, Timing duration) {
		rectanglesToMove.moveBy("translate #2", width - maxWidth, 0, delay,
				duration);
		textsToMove.moveBy("translate", (width - maxWidth) / 2, 0, delay,
				duration);
		pLine.moveBy("translateNodes 4 5 6", width - maxWidth, 0, delay,
				duration);
		maxWidth = width;
	}

	public void top(ConceptualStack<T> cs, Timing delay, Timing duration) {
		highlightTopCell(cs, delay, duration);
		highlightTopElem(cs, delay, duration);
	}

	public void isEmpty(ConceptualStack<T> cs, Timing delay, Timing duration) {
		// does nothing yet:)
	}

	public void highlightTopCell(ConceptualStack<T> cs, Timing delay,
			Timing duration) {
		topRect.changeColor("fillColor", (Color) cs.getProperties().get(
				AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), delay,
				duration);
		highlightedTopRect = true;
	}

	public void highlightTopElem(ConceptualStack<T> cs, Timing delay,
			Timing duration) {
		topText.changeColor("color", (Color) cs.getProperties().get(
				AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), delay,
				duration);
		highlightedTopText = true;
	}

	public void unhighlightTopCell(ConceptualStack<T> cs, Timing delay,
			Timing duration) {
		topRect.changeColor("fillColor", alternateFilled && !odd ? (Color) rp
				.get(AnimationPropertiesKeys.FILL_PROPERTY) : Color.WHITE,
				delay, duration);
		highlightedTopRect = false;
	}

	public void unhighlightTopElem(ConceptualStack<T> cs, Timing delay,
			Timing duration) {
		topText.changeColor("color", (Color) tp
				.get(AnimationPropertiesKeys.COLOR_PROPERTY), delay, duration);
		highlightedTopText = false;
	}

}