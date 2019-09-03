package generators.misc.helpers;

import generators.misc.devs.model.Event;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class DEVSAlgorithm {

	Queue<Float> exp = new LinkedList<Float>();
	Queue<Float> norm = new LinkedList<Float>();

	// list is needed because of sortable attribute
	List<Event> L = new LinkedList<Event>();

	public void init(Event initialEvent) {
		float[] exp = new float[] { 0.57f, 2.73f, 1.36f, 0.72f, 0.23f, 0.08f,
				1.21f, 0.35f, 2.81f, 0.19f, 0.88f };
		for (float f : exp) {
			this.exp.add(f);
		}
		float[] norm = new float[] { 1.89f, 2.02f, 2.56f, 3.84f, 2.00f, 2.28f,
				3.32f, 3.56f, 2.97f, 2.67f, 2.70f };
		for (float f : norm) {
			this.norm.add(f);
		}
		L.add(initialEvent);
	}

	protected void run(float tmax) {
		boolean busy = false;
		float t = 0;
		int N = 0;
		while (t < tmax) {
			Event event = L.get(0);
			L.remove(0);
			t = (float) event.getTime();
			switch (event.getType()) {
			case 'A':
				L.add(new Event(t + exp.poll(), 'A'));
				if (busy) {
					N++;
				} else {
					busy = true;
					L.add(new Event(t + norm.poll(), 'D'));
				}
				break;
			case 'D':
				if (N == 0) {
					busy = false;
				} else {
					N--;
					L.add(new Event(t + norm.poll(), 'D'));
				}
				break;
			}
			Collections.sort(L);
		}
	}

	public static void main(String[] args) {
		float tmax = 10.00f;
		DEVSAlgorithm devs = new DEVSAlgorithm();
		devs.init(new Event(00.50f, 'A'));
		devs.run(tmax);
	}

}
