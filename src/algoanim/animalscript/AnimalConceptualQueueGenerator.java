package algoanim.animalscript;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import algoanim.primitives.ConceptualQueue;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.ConceptualQueueGenerator;
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
 * @see algoanim.primitives.generators.ConceptualQueueGenerator
 * @author Dima Vronskyi
 */
public class AnimalConceptualQueueGenerator<T> extends AnimalGenerator
		implements ConceptualQueueGenerator<T> {

	private static int count = 1;

	private Node upperLeft;

	private LinkedList<Primitive> rectangles, texts;

	private TextProperties tp;

	private RectProperties rp;

	private int tailX = -1, ulx, uly, fontSize, heightOffset;

	private Text tailText, frontText;

	private Rect tailRect, frontRect;

	private Group rectanglesToMove, textsToMove;

	private Polyline pLine;

	private Object fontObject;
	
	private boolean hidden, hiddenProp, hiddenPLine, highlightedTailRect, highlightedTailText, 
					alternateFilled, odd;
	
	private static final int WIDTH_OFFSET = 2;
	
	public AnimalConceptualQueueGenerator(Language aLang) {
		super(aLang);
	}

	public void create(ConceptualQueue<T> cq) {

		if (this.isNameUsed(cq.getName()) || cq.getName() == "") {
			cq.setName("ConceptualQueue" + AnimalConceptualQueueGenerator.count);
			AnimalConceptualQueueGenerator.count++;
		}

		upperLeft = cq.getUpperLeft();
		if (upperLeft instanceof Coordinates) {
			ulx = ((Coordinates) upperLeft).getX();
			uly = ((Coordinates) upperLeft).getY();
		} // TODO Offset?
		
		Integer queueDepth = (Integer) cq.getProperties().get(AnimationPropertiesKeys.
				DEPTH_PROPERTY);
		tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, cq.getProperties().get(
				AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		if (queueDepth.compareTo(Integer.MAX_VALUE) < 0)
			tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, queueDepth);
		else
			tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, queueDepth - 1);
		tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		fontObject = cq.getProperties().get(AnimationPropertiesKeys.FONT_PROPERTY);
		fontSize = ((Font) fontObject).getSize();
		heightOffset = fontSize / 2;
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, fontObject);
		tp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);

		PolylineProperties plp = new PolylineProperties();
		plp.set(AnimationPropertiesKeys.COLOR_PROPERTY, cq.getProperties().get(
				AnimationPropertiesKeys.COLOR_PROPERTY));
		plp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
		plp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, cq.getProperties().get(
				AnimationPropertiesKeys.DEPTH_PROPERTY));
		hiddenProp = (Boolean) cq.getProperties().get(AnimationPropertiesKeys.HIDDEN_PROPERTY);
		hidden = cq.getDisplayOptions() instanceof Hidden;
		plp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);

		Node[] vertices = new Node[2];
		vertices[0] = new Coordinates(ulx, uly + heightOffset + heightOffset/2);
		vertices[1] = new Coordinates(ulx + 40, uly + heightOffset + heightOffset/2);
		lang.newPolyline(vertices, "", cq.getDisplayOptions(), plp);
		
		Node[] vertices2 = new Node[2];
		vertices2[0] = new Coordinates(ulx + 40, uly + heightOffset + heightOffset/2);
		vertices2[1] = new Coordinates(ulx + 80, uly + heightOffset + heightOffset/2);
		pLine = lang.newPolyline(vertices2, "", cq.getDisplayOptions(), plp);
		pLine.hide();
		hiddenPLine = true;
		
		rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, cq.getProperties().get(
				AnimationPropertiesKeys.COLOR_PROPERTY));
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, (Integer) tp
				.get(AnimationPropertiesKeys.DEPTH_PROPERTY) + 1);
		alternateFilled = (Boolean) cq.getProperties().get(
				AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, hiddenProp);
		
		rectangles = new LinkedList<Primitive>();
		texts = new LinkedList<Primitive>();

		List<T> content = cq.getInitContent();
		if (content != null) {
			ListIterator<T> li = content.listIterator();
			while (li.hasNext())
				enqueue(cq, li.next(), null, null);
		}
	}

	public void dequeue(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		frontText.hide(delay);
		frontRect.hide(delay);
		texts.removeFirst();
		rectangles.removeFirst();
		String text = frontText.getText();
		int width = Animal.getStringWidth(text, (Font) fontObject) + 2*WIDTH_OFFSET;
		pLine.moveBy("translate", -width, 0, delay, duration);
		if (!texts.isEmpty()) {
			frontRect = (Rect) rectangles.getFirst();
			frontText = (Text) texts.getFirst();
			textsToMove = lang.newGroup(texts, "");
			rectanglesToMove = lang.newGroup(rectangles, "");
			textsToMove.moveBy("translate", -width, 0, delay, duration);
			rectanglesToMove.moveBy("translate", -width, 0, delay, duration);
			tailX -= width;
		}
		else {
			frontRect = null;
			frontText = null;
			tailX = -1;
			pLine.hide(delay);
			hiddenPLine = true;
		}
	}

	public void enqueue(ConceptualQueue<T> cq, T elem, Timing delay, Timing duration) {

		String text = elem.toString();
		int width = Animal.getStringWidth(text, (Font) fontObject) + 2*WIDTH_OFFSET;
		
		if (tailText != null && highlightedTailText)
			unhighlightTailElem(cq, delay, duration);
		if (tailRect != null && highlightedTailRect)
			unhighlightTailCell(cq, delay, duration);
		
		if (tailX == -1)
			tailX = ulx + 40;
		
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, odd && alternateFilled ? 
				cq.getProperties().get(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY) :
					Color.WHITE);
		
		tailText = lang.newText(new Coordinates(tailX + width / 2, uly),
				text, "", hidden ? new Hidden() : delay, tp);
		tailRect = lang.newRect(new Coordinates(tailX, uly), new Coordinates(
				tailX + width, uly + fontSize + heightOffset), "", 
				hidden ? new Hidden() : delay, rp);
		
		pLine.moveBy("translate", width, 0, delay, duration);
		if (hiddenPLine && !hidden && !hiddenProp) {
			pLine.show(delay);
			hiddenPLine = false;
		}
		
		tailX += width;
		
		if (frontText == null)
			frontText = tailText;
		if (frontRect == null)
			frontRect = tailRect;

		odd = !odd;
		texts.add(tailText);
		rectangles.add(tailRect);
	}

	public void front(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		highlightFrontCell(cq, delay, duration);
		highlightFrontElem(cq, delay, duration);
	}

	public void highlightFrontCell(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		frontRect.changeColor("fillColor", (Color) cq.getProperties().get(
				AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), delay, duration);
	}

	public void highlightFrontElem(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		frontText.changeColor("color", (Color) cq.getProperties().get(
				AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), delay, duration);
	}

	public void highlightTailCell(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		tailRect.changeColor("fillColor", (Color) cq.getProperties().get(
				AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY), delay, duration);
		highlightedTailRect = true;
	}

	public void highlightTailElem(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		tailText.changeColor("color", (Color) cq.getProperties().get(
				AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), delay, duration);
		highlightedTailText = true;
	}

	public void isEmpty(ConceptualQueue<T> cq, Timing delay, Timing duration) {
		// does nothing yet:)
	}

	public void tail(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		highlightTailCell(cq, delay, duration);
		highlightTailElem(cq, delay, duration);
	}

	public void unhighlightFrontCell(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		frontRect.changeColor("fillColor", alternateFilled && !odd ?
				(Color) rp.get(AnimationPropertiesKeys.FILL_PROPERTY) : Color.WHITE,
				delay, duration);
	}

	public void unhighlightFrontElem(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		frontText.changeColor("color", (Color) tp.get(AnimationPropertiesKeys.COLOR_PROPERTY),
				delay, duration);
	}

	public void unhighlightTailCell(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		tailRect.changeColor("fillColor", alternateFilled && !odd ?
				(Color) rp.get(AnimationPropertiesKeys.FILL_PROPERTY) : Color.WHITE,
				delay, duration);
		highlightedTailRect = false;
	}

	public void unhighlightTailElem(ConceptualQueue<T> cq, Timing delay, Timing duration) {

		tailText.changeColor("color", (Color) tp.get(AnimationPropertiesKeys.COLOR_PROPERTY),
				delay, duration);
		highlightedTailText = false;
	}
}
