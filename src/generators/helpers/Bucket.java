package generators.helpers;

import generators.sorting.helpers.AbstractGenerator;

import java.util.Vector;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;

public class Bucket {
	private Text label;
	private Vector<Zahl> zahlen;

	public Bucket(String label, Node coords, Language lang) {
		this.label = lang.newText(coords, label, "bucket_label_" + label, null,
				AbstractGenerator.BOLD);
		this.empty();
	}

	public void add(Zahl z) {
		z.moveTo(label, zahlen.size());
		this.zahlen.add(z);
	}

	public void empty() {
		this.zahlen = new Vector<Zahl>();
	}

	public Vector<Zahl> getZahlen() {
		return this.zahlen;
	}
}
