/*
 * Created on 22.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;

/**
 * @author ek
 */
public class Chart extends AnimalObject {

	TextBox[][] elements;

	int columns;

	int rows;

	public Chart(AnimalScriptWriter aWriter, EKNode aPosition, int pcolumns,
			int prows) {
		super(aWriter);
		name = "chart" + instance_index;
		columns = pcolumns;
		rows = prows;
		elements = new TextBox[rows][columns];
		int i1, i2;
		boolean firstrow = true;
		for (i1 = rows - 1; i1 >= 0; i1--) {
			TextBox t;
			if (firstrow) {
				t = new TextBox(aWriter, aPosition, " ");
				firstrow = false;
			} else
				t = new TextBox(aWriter, elements[i1 + 1][0]
						.createOffset(0, 0, NW), " ");
			elements[i1][0] = t;
			for (i2 = 1; i2 < columns; i2++)
				elements[i1][i2] = new TextBox(aWriter, elements[i1][i2 - 1]
						.createOffset(0, 0, SE), " ");
		}
		setDepth(depth); // evtl ueberfluessig. um Tiefe auch fuer unterelemente zu
											// uebernehmen
	}

	public void setDepth(int aDepth) {
		this.depth = aDepth;
		int i1, i2;
		for (i1 = 0; i1 < rows; i1++)
			for (i2 = 0; i2 < columns; i2++)
				elements[i1][i2].setDepth(depth);
	}

	public TextBox getElementAt(int x, int y) {
		return elements[y][x];
	}

	public void setText(int x, int y, String text) {
		elements[y][x].getText().setValue(text);
	}

	public void setFillColorRect(int x, int y, int w, int h, EKColor c) {
		int i1, i2;
		for (i1 = x; i1 < x + w; i1++)
			for (i2 = y; i2 < y + h; i2++)
				elements[i2][i1].getRectangle().setFillColor(c);
	}

	/**
	 * Sets each cell with one char from the given String.
	 * 
	 * @param x
	 * @param y
	 * @param chars
	 */
	public void setCharsHorizontaly(int x, int y, String chars) {
		int i;
		int width = chars.length();
		for (i = 0; i < width; i++) {
			// TODO Check if this change is OK
			// Text t;
			if (i + x >= columns)
				break;
			// was: t =
			getElementAt(i + x, y).setText("" + chars.charAt(i));
		}
	}

	/**
	 * Sets each cell with one char from the given String.
	 * 
	 * @param x
	 * @param y
	 * @param chars
	 */
	public void setCharsVerticaly(int x, int y, String chars) {
		int i;
		int width = chars.length();
		for (i = 0; i < width; i++) {
			// TODO Check if this change is OK
			// Text t;
			if (i + y >= rows)
				break;
			// was: t =
			getElementAt(x, y + i).setText("" + chars.charAt(i));
		}
	}

	/*
	 * deprecated: setCharsHorizontaly instead public void setChars(int x, int y,
	 * String chars) { int i; int width = chars.length(); for(i = 0; i < width;
	 * i++) { Text t; if(i+x >= columns) break; t = getElementAt(i+x,y).setText("" +
	 * chars.charAt(i)); } }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see animalobjects.AnimalObject#register()
	 */
	public void register() {
		super.register();
		int i1, i2;
		for (i1 = rows - 1; i1 >= 0; i1--)
			for (i2 = 0; i2 < columns; i2++)
				elements[i1][i2].register();
		out.print("group \"");
		out.print(name);
		for (i1 = rows - 1; i1 >= 0; i1--)
			for (i2 = 0; i2 < columns; i2++) {
				String s = elements[i1][i2].name;
				out.print("\" \"");
				out.print(s);
				out.print("\" \"");
			}
		out.println();

	}

}
