package generators.graphics;

import java.awt.Color;
import java.util.Vector;

import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

/**
 * This is a workaround for integer matrices 
 * because the native support in ANIMAL has too many bugs
 * 
 *  This workaround works with rects and strings.
 *  The column width depends on the biggest input. 
 *  
 *  it supports MatrixProperties except the following properties, which will be ignored:
 *  
 *  
 * @author Jens Krüger
 *
 */
public class StringMatrixWA {
	
	// statics
	private final static int ROW_HEIGHT = 25;
	private final static int SIMPLE_COLUMN_WIDTH = 25;
	private static int COLUMN_WIDTH;
	public final static int TEXT_OFFSET = 4;
	private int mCounter;

	// fields
	private String[][] matrix;
	private Language lang;
	private Coordinates upperLeft;
	private Rect[][] rects;
	private Text[][] texts;
	private RectProperties rectProperties;
	private RectProperties highlightedRectProperties;
	private RectProperties afterHighlithingRectProperties;
	private TextProperties textProperties;
	int hCounter = 0;
	
	
	public StringMatrixWA(String[][] matrix) {
		this.matrix = matrix;
	}
	
	/**
	 * 
	 * @param lang
	 * @param upperLeft
	 * @param matrix
	 * @param matrixProperties
	 * @param mCounter
	 * @return
	 */
	public StringMatrixWA drawMatrix (Language lang, Node upperLeft, MatrixProperties matrixProperties, int mCounter) {
		
		// initialize the needed fields
			this.lang = lang;
			this.upperLeft = (Coordinates)upperLeft;
			this.mCounter = mCounter;
		
		// initialize internal matrices
			rects = new Rect[matrix.length][matrix[0].length];
			texts = new Text[matrix.length][matrix[0].length];
		
		// create rectProperties
			rectProperties = new RectProperties();
			rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, matrixProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY));
			rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
			rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, matrixProperties.get(AnimationPropertiesKeys.FILL_PROPERTY));
			rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			
		// create highlightedRectProperties
			highlightedRectProperties = new RectProperties();
			highlightedRectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, matrixProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY));
			highlightedRectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			highlightedRectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, matrixProperties.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
			highlightedRectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		// create textProperties
			textProperties = new TextProperties();
			textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, matrixProperties.get(AnimationPropertiesKeys.FONT_PROPERTY));
			textProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
			textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		
		// set the column width
			COLUMN_WIDTH = 2*SIMPLE_COLUMN_WIDTH;
			
		// ceateCells
			for (int i = 0; i < matrix.length; i++) { // rows
				for (int j = 0; j < matrix[0].length; j++) { // columns
					createCell(matrix[i][j], i, j);
				}
			}
	
		return this;
	}
	
	private void createCell (String value, int i, int j) {
		
		// create rectangle
		Coordinates upperLeft = new Coordinates(this.upperLeft.getX() + j * COLUMN_WIDTH, this.upperLeft.getY() + i * ROW_HEIGHT);
		Coordinates lowerRight = new Coordinates(upperLeft.getX() + COLUMN_WIDTH, upperLeft.getY() + ROW_HEIGHT);
		rects[i][j] = lang.newRect(upperLeft, lowerRight, "StrMWA"+"rect"+mCounter+i+j, null, rectProperties);
		
		// create text
		
		// Bugfix
		if (i == 0 && j == 0) {
			TextProperties textBugFixProperties = new TextProperties();
			textBugFixProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
			Text bugFix = lang.newText(new Coordinates(upperLeft.getX() + COLUMN_WIDTH /2, upperLeft.getY() + TEXT_OFFSET), "A", "StrMWA"+"text"+"BUGFIX"+mCounter+i+j, null, textBugFixProperties);
			bugFix.hide();
		}
		
		texts[i][j] = lang.newText(new Coordinates(upperLeft.getX() + COLUMN_WIDTH /2, upperLeft.getY() + TEXT_OFFSET), value, "StrMWA"+"text"+mCounter+i+j, null, textProperties);
	}
	
	/**
	 * highlight the cells in the specified space
	 */
	public void highlightCellSpace (int left, int right, int up, int down) {
		for (int i = up; i <= down; i++) { // rows
			for (int j = left; j <= right; j++) { // columns
				rects[i][j].hide();
				rects[i][j] = lang.newRect(rects[i][j].getUpperLeft(), rects[i][j].getLowerRight(), rects[i][j].getName() + hCounter, null, highlightedRectProperties);
			}
		}
		hCounter++;
	}
	  
	/**
	 * unhighlight the cells in the specified space
	 */
	public void unhighlightCellSpace (int left, int right, int up, int down) {
		for (int i = up; i <= down; i++) { // rows
			for (int j = left; j <= right; j++) { // columns
				rects[i][j].hide();
				rects[i][j] = lang.newRect(rects[i][j].getUpperLeft(), rects[i][j].getLowerRight(), rects[i][j].getName() + hCounter, null, rectProperties);
			}
		}
	}
	
	public void hide() {
		for (int i = 0; i < rects.length; i++) {
			for (int j = 0; j < rects[0].length; j++) {
				rects[i][j].hide();
				texts[i][j].hide();
			}
		}
	}
	
	/**
	 * 
	 * @return all primitives which represent the matrix
	 */
	public Vector<Primitive> getPrimitives() {
		Vector<Primitive> primitives = new Vector<Primitive>();
		for (int i = 0; i < rects.length; i++) {
			for (int j = 0; j < rects[0].length; j++) {
				primitives.add(rects[i][j]);
				primitives.add(texts[i][j]);
			}
		}
		return primitives;
	}
	
	public Coordinates getUpperLeft() {
		return upperLeft;
	}
	
	public String[][] getMatrix() {
		return matrix;
	}
	
}
