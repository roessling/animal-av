package generators.misc.hepersGlicko2;

import java.awt.Color;

import algoanim.primitives.StringMatrix;

/**
 * The Table class provides utility functions for
 * improving the usability of the StringMatrix. 
 * (used in Glicko2-Generator)
 * @author Maxim Kuznetsov
 *
 */
public class Glicko2Table {
	
	public Glicko2Table() {}
	
	public static void updateRow(StringMatrix target, int row, String... data) {
		updateRow(target, row, Color.YELLOW, data);
	}
	
	public static void updateRow(StringMatrix target, int row, Color color, String... data) {
		for (int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				target.put(row, i, data[i], null, null);
				highlightCell(target, row, i, color);
			}
		}
	}
	
	public static void highlightRow(StringMatrix target, int row) {
		target.highlightCellColumnRange(row, 0, target.getNrCols() - 1, null, null);
	}
	
	public static void highlightRow(StringMatrix target, int row, Color color) {
		for (int i = 0; i < target.getNrCols(); i++) {
			target.setGridHighlightFillColor(row, i, color, null, null);
		}
		target.highlightCellColumnRange(row, 0, target.getNrCols() - 1, null, null);
	}
	
	public static void unhighlightRow(StringMatrix target, int row) {
		for(int i = 0; i < target.getNrCols(); i++) {
			target.unhighlightCell(row, i, null, null);
			target.setGridHighlightFillColor(row, i, Color.YELLOW, null, null); //reset color to default
		}
	}
	
	public static void highlightCell(StringMatrix target, int row, int column, Color color) {
		target.setGridHighlightFillColor(row, column, color, null, null);
		target.highlightCell(row, column, null, null);
	}

}
