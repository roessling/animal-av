/*
 * Created on 17.12.2004
 */
package de.ahrgr.animal.kohnert.asugen.petri;

import de.ahrgr.animal.kohnert.asugen.AnimalObject;
import de.ahrgr.animal.kohnert.asugen.AnimalScriptWriter;
import de.ahrgr.animal.kohnert.asugen.Circle;
import de.ahrgr.animal.kohnert.asugen.EKColor;
import de.ahrgr.animal.kohnert.asugen.EKNode;

/**
 * @author ek
 */
public class Stelle extends AnimalObject {

	protected int size;

	protected Circle circle;

	protected Circle marke;

	public Stelle(AnimalScriptWriter w, EKNode aPosition, int aSize) {
		super(w);
		size = aSize;
		position = aPosition;
		circle = new Circle(w, aPosition, size);
		marke = new Circle(w, aPosition, 10);
		marke.setFillColor(EKColor.BLACK);
		marke.setHidden(true);
	}

	public boolean getMarkeGesetzt() {
		return !marke.getHidden();
	}

	public void setMarke(boolean b) {
		marke.setHidden(!b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see animalobjects.AnimalObject#register()
	 */
	public void register() {
		if (registered)
			return;
		circle.register();
		marke.register();
		out.print("group");
		printQuoted(name);
		printQuoted(circle.getName());
		printQuoted(marke.getName());
		out.println();
	}

}
