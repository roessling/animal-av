package generators.maths.grid;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JTextField;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;

public class GridProperty {

	  public static final String GRID_COLOR = "GRID_COLOR";
	  public static final String GRID_BG_COLOR = "GRID_BG_COLOR";
	  public static final String GRID_FILLED = "GRID_FILLED";
	  
	  public static final String TEXT_SIZE = "TEXT_SIZE";
	  public static final String TEXT_FONT = "TEXT_FONT";
	  public static final String TEXT_COLOR = "TEXT_COLOR";
	  
	  public static final String BORDER = "BORDER";
	  public static final String BORDER_COLOR = "BORDER_COLOR";
	  	  
	  public static final String CAPTION_TEXT_SIZE = "CAPTION_TEXT_SIZE";
	  public static final String CAPTION_FONT = "CAPTION_FONT";
	  public static final String CAPTION_COLOR = "CAPTION_COLOR";
	  public static final String CAPTION_OFFSET_LEFT = "CAPTION_OFFSET_LEFT";
	  public static final String CAPTION_OFFSET_RIGHT = "CAPTION_OFFSET_RIGHT";
	  public static final String CAPTION_OFFSET_TOP = "CAPTION_OFFSET_TOP";
	  public static final String CAPTION_OFFSET_BOTTOM = "CAPTION_OFFSET_BOTTOM";
	  
	  private HashMap<String,Object> properties;

	  public GridProperty() {
		  properties = new HashMap<String, Object>();
	  }
	  
	  public void set(String key, boolean object) {
		  properties.put(key, object);
	  }
	  
	  public void set(String key, String object) {
		  properties.put(key, object);
	  }
	  
	  public void set(String key, int object) {
		  properties.put(key, object);
	  }
	  public void set(String key, Object object) {
		  properties.put(key, object);
	  }
	  
	  public HashMap<String, Object> getProperties() {
		  return properties;
	  }
	  
	  public boolean getBorder() {
		  if (properties.containsKey(BORDER)) {
			  return (Boolean) properties.get(BORDER);
		  } else {
			  return false;
		  }
	  }
	  
	  public Color getBorderColor() {
		  if (properties.containsKey(BORDER_COLOR)) {
			  return (Color) properties.get(BORDER_COLOR);
		  } else {
			  return Color.black;
		  } 
	  }
	  
	  public SquareProperties getCellProperty() {
		  SquareProperties sp = new SquareProperties();
		  
		  Color gridColor;
		  Color gridBGColor;
		  boolean gridFilled;
		  
		  if (properties.containsKey(GRID_COLOR)) {
			  gridColor = (Color) properties.get(GRID_COLOR);
		  } else {
			  gridColor = Color.black;
		  }
		  
		  if (properties.containsKey(GRID_BG_COLOR)) {
			  gridBGColor = (Color) properties.get(GRID_BG_COLOR);
		  } else {
			  gridBGColor = Color.white;
		  }
		  
		  if (properties.containsKey(GRID_FILLED)) {
			  gridFilled = (Boolean) properties.get(GRID_FILLED);
		  } else {
			  gridFilled = true;
		  }
		  
		  sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, gridFilled);
		  sp.set(AnimationPropertiesKeys.FILL_PROPERTY, gridBGColor);
		  sp.set(AnimationPropertiesKeys.COLOR_PROPERTY, gridColor);
		  //sp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, false);
		  
		  return sp;
	  }
	  
	  public TextProperties getTextProperty(int width, int height, String text) {
		  TextProperties tp = new TextProperties();
		  
		  int TextSize;
		  Color TextColor;
		  String TextFont;
		  		  
		  if (properties.containsKey(TEXT_COLOR)) {
			  TextColor = (Color) properties.get(TEXT_COLOR);
		  } else {
			  TextColor = Color.BLACK;
		  }
		  
		  if (properties.containsKey(TEXT_FONT)) {
			  TextFont = (String) properties.get(TEXT_FONT);
		  } else {
			  TextFont = "Arial";
		  }
		  
		  if (properties.containsKey(TEXT_SIZE)) {
			  TextSize = (Integer) properties.get(TEXT_SIZE);
		  } else {
			  TextSize = calculateFontSize(TextFont, text, width, height);
		  }
		  
		  tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(TextFont, Font.PLAIN, TextSize));
		  tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, TextColor);
		  //tp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, false);
		  
		  return tp;
	  }
	  
	  public TextProperties getCaptionProperty(int width, int height, String text) {
		  TextProperties tp = new TextProperties();
		  
		  int TextSize;
		  Color TextColor;
		  String TextFont;
		  	  
		  if (properties.containsKey(CAPTION_COLOR)) {
			  TextColor = (Color) properties.get(CAPTION_COLOR);
		  } else {
			  TextColor = Color.BLACK;
		  }
		  
		  if (properties.containsKey(CAPTION_FONT)) {
			  TextFont = (String) properties.get(CAPTION_FONT);
		  } else {
			  TextFont = "Arial";
		  }
		  
		  if (properties.containsKey(CAPTION_TEXT_SIZE)) {
			  TextSize = (Integer) properties.get(CAPTION_TEXT_SIZE);
		  } else {
			  TextSize = calculateFontSize(TextFont, text, width, height);
		  }
		  
		  tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(TextFont, Font.PLAIN, TextSize));
		  tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, TextColor);
		  //tp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, false);
		  
		  return tp;
	  }
	  
	  public Point getCaptionOffsetLeft() {
		  if (properties.containsKey(CAPTION_OFFSET_LEFT)) {
			  return (Point) properties.get(CAPTION_OFFSET_LEFT);
		  } else {
			  return new Point(0, 0);
		  }
	  }
	  
	  public Point getCaptionOffsetRight() {
		  if (properties.containsKey(CAPTION_OFFSET_RIGHT)) {
			  return (Point) properties.get(CAPTION_OFFSET_RIGHT);
		  } else {
			  return new Point(0, 0);
		  }
	  }
	  
	  public Point getCaptionOffsetTop() {
		  if (properties.containsKey(CAPTION_OFFSET_TOP)) {
			  return (Point) properties.get(CAPTION_OFFSET_TOP);
		  } else {
			  return new Point(0, 0);
		  }
	  }
	  
	  public Point getCaptionOffsetBottom() {
		  if (properties.containsKey(CAPTION_OFFSET_BOTTOM)) {
			  return (Point) properties.get(CAPTION_OFFSET_BOTTOM);
		  } else {
			  return new Point(0, 0);
		  }
	  }
	  
	  
	  public int calculateFontSize(String fontName, String text, int width, int height) {
			
			JTextField tf = new JTextField();			
			FontMetrics metrics = tf.getFontMetrics(new Font(fontName, Font.PLAIN, 10));
			float heightFactor = height/(float)metrics.getHeight();
			float widthFactor = width/(float)metrics.stringWidth(text);
				
			int result = (int) Math.floor(10*Math.min(heightFactor, widthFactor)) - 2;
			return result;			
		}
}
