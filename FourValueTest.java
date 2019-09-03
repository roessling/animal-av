import java.util.LinkedList;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.FourValueCounter;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.generators.Language;
import algoanim.properties.CounterProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.TicksTiming;

public class FourValueTest {

	private Language lang;
	private DisplayOptions disOp = new TicksTiming(0);
	private ListBasedQueue<String> listBasedQueue;
	private FourValueCounter counter;
//	private FourValueView view;

	public static void main(String[] args) {
		Language l = new AnimalScript("Lineare Suche Animation",
				"Axel Heimann", 1280, 720);
		FourValueTest fwt = new FourValueTest(l);

		fwt.init();
		fwt.enqueue();
		fwt.dequeue();
		fwt.enqueue();
		fwt.enqueue();
		fwt.dequeue();
		fwt.dequeue();
		fwt.dequeue();
		System.out.println(fwt.lang.toString());
	}

	public FourValueTest(Language l) {
		lang = l;
		lang.setStepMode(true);
	}

	public void init() {
		lang = new AnimalScript("Lineare Suche Animation", "Axel Heimann",
				1280, 720);
		lang.setStepMode(true);
		LinkedList<String> content = new LinkedList<String>();
		content.add("1");
		content.add("2");
		content.add("3");
		content.add("4");
		listBasedQueue = lang.newListBasedQueue(new Coordinates(100, 100),
				content, "ListBasedQueue", disOp);

		counter = lang.newCounter(listBasedQueue);
		CounterProperties cp = new CounterProperties();
		String[] valueNames = { "longString", "veryLongString", "shortString",
				"tinyString" };
//		view = 
		lang.newCounterView(counter, new Coordinates(10, 10), cp, true, true, valueNames);
	}

	private void enqueue() {
		listBasedQueue.enqueue("42");
		lang.nextStep();
	}

	private void dequeue() {
		listBasedQueue.dequeue();
		lang.nextStep();
	}

}
