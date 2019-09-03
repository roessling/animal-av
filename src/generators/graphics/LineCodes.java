package generators.graphics;

import java.awt.Color;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

/**
 * class to draw the codes if a line will be intersected
 * @author Jens Krüger
 *
 */
public class LineCodes {
	
	// finals
		private final static int UP    = 0b1000;
		private final static int DOWN  = 0b0100;
		private final static int RIGHT = 0b0010;
		private final static int LEFT  = 0b0001;
	
	// positions
		private int x;
		private int y;
		private static final int HEIGHT = 30;
		private static final int WIDTH = 10;
		private static final int TEXT_WIDTH = 30;
	
	// fields
		private Text[][] codes;
		private TextProperties properties;
		private TextProperties highlightProperties;
		private Color normalColor;
		private Color highlightColor;
		private Text a, b, s;
	

	/**
	 * draws the initialization of linecodes 
	 * @param lang the generator
	 * @param upperLeft upper left edge
	 * @param properties properties for text
 	 * @param hightlightProperties color for highlighting text
	 */
	public LineCodes (Language lang, Coordinates upperLeft,TextProperties properties, TextProperties hightlightProperties) {
		
		// initialize x, y, prop, highlightColor
			this.x = upperLeft.getX();
			this.y = upperLeft.getY();
			this.properties = properties;
			this.highlightProperties = hightlightProperties;
			this.normalColor = (Color)properties.get(AnimationPropertiesKeys.COLOR_PROPERTY);
			this.highlightColor = (Color)hightlightProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY);
					
		// draw A, B, S
			TextProperties propLeft = new TextProperties();
			propLeft.set(AnimationPropertiesKeys.FONT_PROPERTY, properties.get(AnimationPropertiesKeys.FONT_PROPERTY));
			propLeft.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
			Text bugfix = lang.newText(new Coordinates(x, y), 			"A = ", "DUMM", null, propLeft);
			bugfix.hide();
			a = lang.newText(new Coordinates(x, y),          "A = ", "A = ", null, propLeft);
			b = lang.newText(new Coordinates(x, y+HEIGHT),   "B = ", "B = ", null, propLeft);
			s = lang.newText(new Coordinates(x, y+2*HEIGHT), "S = ", "S = ", null, propLeft);
			
		// initialize codes
			codes = new Text[3][4];
			for (int i = 0; i < codes.length; i++) {
				for (int j = 0; j < codes[0].length; j++) {
					codes[i][j] = lang.newText(new Coordinates(x+TEXT_WIDTH+j*WIDTH, y+i*HEIGHT), "", "codes" + i + j, null, properties);
				}
			}
	}
	
	/**
	 * init line code with zeros
	 * @param line
	 */
	public void initLineCode (int line) {
		for (int j = 0; j < codes[line].length; j++) {
			codes[line][j].setText("0", null, null);
		}
	}
	
	
	/**
	 * updates the codes of A and B
	 * @param newCodes
	 */
	public void updateAB (int[] newCodes) {
		for (int i = 0; i < newCodes.length; i++) {
			if ((newCodes[i] & UP)    == UP)    codes[i][0].setText("1", null, null);
			if ((newCodes[i] & DOWN)  == DOWN)  codes[i][1].setText("1", null, null);
			if ((newCodes[i] & RIGHT) == RIGHT) codes[i][2].setText("1", null, null);
			if ((newCodes[i] & LEFT)  == LEFT)  codes[i][3].setText("1", null, null);
		}
	}
	
	/**
	 * updates the code of S
	 * @param newCode
	 */
	public void updateS (int newCode) {
		char[] newCodeChar = String.format("%"+Integer.toString(4)+"s",Integer.toBinaryString(newCode)).replace(" ","0").toCharArray();
		for (int j = 0; j < codes[2].length; j++)
			codes[2][j].setText(String.valueOf(newCodeChar[j]), null, null);
	}
	
	/**
	 * resets the code lines
	 */
	public void reset () {
		for (int i = 0; i < codes.length; i++)
			for (int j = 0; j < codes[0].length; j++)
				codes[i][j].setText("", null, null);
	}
	
	
	/**
	 * highlights the element at {@param x,@param y}
	 */
	public void highlight (int x, int y) {
		codes[x][y].changeColor("", highlightColor, null, null);
	}
	
	
	/**
	 * unhighlights the element at {@param x,@param y}
	 */
	public void unhighlight (int x, int y) {
		codes[x][y].changeColor("", normalColor, null, null);
	}
	
	/**
	 * hides it complete
	 */
	public void hide() {
		a.hide();
		b.hide();
		s.hide();
		for (int i = 0; i < codes.length; i++) {
			for (int j = 0; j < codes[0].length; j++) {
				codes[i][j].hide();
			}
		}
	}
	
	
	
}
