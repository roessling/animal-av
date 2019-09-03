package generators.maths.fixpointinteration;

import java.util.ArrayList;

import algoanim.primitives.generators.Language;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;

public class Table {
	private final Utility util;

	private final ArrayList<Column> columns = new ArrayList<>();

	private final int posX;
	private final int posY;

	private int currentY;

	private int leftMargin = 10;
	private int rowHeight = 20;
	private PolylineProperties lineProperties = new PolylineProperties();
	private TextProperties textProperties = new TextProperties();
	
	

	public Table(Language l, int posX, int posY) {
		util = new Utility(l);
		this.posX = posX;
		this.posY = posY;
		currentY = posY;
	}

	public void drawHeader() {
		int currentX = posX;
		for (Column c : columns) {
			util.drawText(c.getCaption(), currentX + leftMargin, posY, textProperties);
			util.drawLine(currentX, posY, currentX, posY + rowHeight, lineProperties);

			currentX += c.getWidth();
		}
		util.drawLine(currentX, posY, currentX, posY + rowHeight, lineProperties);
		util.drawLine(posX, posY + rowHeight, currentX, posY + rowHeight, lineProperties);
		currentY += rowHeight;
	}

	public void addColumn(String caption, int width) {
		columns.add(new Column(caption, width));
	}

	public void addRow(String... values) {
		if (values.length != columns.size()) {
			throw new IllegalArgumentException(
					"Number of values has to match number of columns. Given: "
							+ values.length + ". Current number of rows: "
							+ columns.size());
		}
		int currentX = posX;
		for (int i = 0; i < values.length; i++) {
			util.drawText(values[i], currentX + leftMargin, currentY, textProperties);
			util.drawLine(currentX, currentY, currentX, currentY + rowHeight, lineProperties);
			currentX += columns.get(i).getWidth();
		}
		util.drawLine(currentX, currentY, currentX, currentY + rowHeight, lineProperties);
		currentY += rowHeight;
	}
	
	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}

	public void setLineProperties(PolylineProperties lineProperties) {
		this.lineProperties = lineProperties;
	}

	public void setTextProperties(TextProperties textProperties) {
		this.textProperties = textProperties;
	}

}
