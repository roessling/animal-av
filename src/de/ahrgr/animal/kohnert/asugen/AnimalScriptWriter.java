/*
 * Created on 24.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import de.ahrgr.animal.kohnert.asugen.property.Property;

/**
 * This is the main object for an animal animation. It specifies where and how
 * to generate the script. Each graphical Object will use this object to
 * generate its script.
 * 
 * @author ek
 */
public class AnimalScriptWriter {

	protected PrintWriter out;

	protected boolean block_opened = false;

	protected int default_depth = 0;

	protected LinkedList<AnimalObject> registerList = new LinkedList<AnimalObject>();

	protected ArrayList<AnimalObject> registeredObjects = new ArrayList<AnimalObject>();

	protected AnimalObject objToRegister = null;

	// sollen createXXX() Methoden die Objekte sofort registrieren?
	protected boolean auto_register = true;

	/**
	 * Creates the AnimalScriptWriter
	 * 
	 * @param outWriter
	 *          where to write the script
	 */
	public AnimalScriptWriter(PrintWriter outWriter) {
		out = outWriter;
		out.println("%Animal 2");
	}

	/**
	 * Generates a Script File with embedded Generator Properties.
	 * 
	 * @param outWriter
	 *          where to write the script
	 * @param properties
	 *          the properties to write into the file
	 * @param generatorClass
	 *          the name of the class generating the script
	 */
	public AnimalScriptWriter(PrintWriter outWriter, Property[] properties,
			String generatorClass) {
		this(outWriter);
		writeProperties(properties, generatorClass);
	}

	/**
	 * Writes a list of properties into the script
	 * 
	 * @param properties
	 * @param generatorClass
	 */
	public void writeProperties(Property[] properties, String generatorClass) {
		out.println("#!!!EMBEDDED_PROPERTIES");
		out.println("#generator = \"" + generatorClass + "\"");
		int i;
		for (i = 0; i < properties.length; i++) {
			out.println("#" + properties[i].toString());
		}
		out.println("#!!!END_EMBEDDED_PROPERTIES");
	}

	/**
	 * Starts a block if there is none yet.
	 */
	public void startBlock() {
		if (!block_opened) {
			if (objToRegister != null)
				objToRegister.register();
			block_opened = true;
			out.println("{");
		}
	}

	/**
	 * closes the current block
	 */
	public void endBlock() {
		if (!block_opened)
			return;
		AnimalObject o;
		while (!registerList.isEmpty()) {
			o = registerList.removeFirst();
			o.register();
		}
		out.println("}");
		block_opened = false;
	}

	/**
	 * Closes any open block and starts a new one.
	 * 
	 */
	public void startNewBlock() {
		endBlock(); // falls noch ein Block offen: beenden
		startBlock(); // dann den neuen Block anfangen
	}

	/**
	 * Adds a label with the given namen at the current frame
	 * 
	 * @param label
	 *          the label to add
	 */
	public void addLabel(String label) {
		out.println("label \"" + label + "\"");
	}

	/**
	 * 
	 * @return the destination of the script
	 */
	public PrintWriter getOut() {
		return out;
	}

	/**
	 * Sets the given object to be auto registered. The scriptwriter will try to
	 * find a good time to create the script.
	 * 
	 * @param obj
	 */
	protected void autoRegister(AnimalObject obj) {
		if (block_opened) {
			// mehrere Objekte koennen auf einmal registriert werden
			registerList.add(obj);
		} else {
			// immer das letzte Objekt registrieren
			if (objToRegister != null)
				objToRegister.register();
			objToRegister = obj;
		}
	}

	/**
	 * Sets all Animal Objects to invisible
	 * 
	 */
	public void clear() {
		startBlock();
		Iterator<AnimalObject> i = registeredObjects.iterator();
		while (i.hasNext()) {
			AnimalObject o = i.next();
			o.setHidden(true);
		}
	}

	/**
	 * Should be called to close the script
	 */
	public void close() {
		endBlock();
		if (objToRegister != null)
			objToRegister.register();
	}

	/**
	 * Creates an absolute Node
	 * 
	 * @param x
	 *          the x coordinate
	 * @param y
	 *          the y coordinate
	 * @return the Node instance
	 */
	public EKNode abs(int x, int y) {
		return new AbsoluteNode(this, x, y);
	}

	/**
	 * Creates a Text Object
	 * 
	 * @param position
	 *          the objects position
	 * @param text
	 *          the text
	 * @return generated Text Object
	 */
	public Text createText(EKNode position, String text) {
		Text t = new Text(this, position, text);
		if (auto_register)
			autoRegister(t); // t.register();
		return t;
	}

	/**
	 * Creates a line from n1 to n2
	 * 
	 * @param n1
	 *          the start node
	 * @param n2
	 *          the end node
	 * @return generated Line
	 */
	public PolyLine createLine(EKNode n1, EKNode n2) {
		PolyLine p = new PolyLine(this);
		p.addNode(n1);
		p.addNode(n2);
		if (auto_register)
			autoRegister(p); // p.register();
		return p;
	}

	/**
	 * Creates a rectangle with the given top left and bottom right points
	 * 
	 * @param n1
	 *          the top left
	 * @param n2
	 *          the bottom right
	 * @return the generated Rectangle
	 */
	public Rectangle createRectangle(EKNode n1, EKNode n2) {
		Rectangle r = new Rectangle(this, n1, n2);
		if (auto_register)
			autoRegister(r);
		return r;
	}

	/**
	 * Creates a new CodeGroup
	 * 
	 * @param position
	 *          the position
	 * @return the generated CodeGroup
	 */
	public CodeGroup createCodeGroup(EKNode position) {
		CodeGroup g = new CodeGroup(this, position);
		if (auto_register)
			autoRegister(g); // g.register();
		return g;
	}

	/**
	 * Creates a new Circle
	 * 
	 * @param position
	 *          the center of the circle
	 * @param radius
	 *          the radius of the circle
	 * @return the generated Circle
	 */
	public Circle createCircle(EKNode position, int radius) {
		Circle c = new Circle(this, position, radius);
		if (auto_register)
			autoRegister(c); // c.register();
		return c;
	}

}
