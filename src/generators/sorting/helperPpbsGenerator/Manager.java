package generators.sorting.helperPpbsGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import translator.TranslatableGUIElement;

public class Manager {
	
	public static Language lang;
	public static TranslatableGUIElement builder;
	
	private static List<Object> clearList = new ArrayList<>();
	
	private static int upLeftX = Integer.MAX_VALUE;
	private static int upLeftY = Integer.MAX_VALUE;
	
	private static Text pivotText;
	private static Text resultText;
	private static Text iterationText;
	
	private static final int offsetX = 10;
	
	private static Text m1Text;
	private static final int m1YOffset = 10;
	
	private static Text m2Text;
	private static final int m2YOffset = 25;
	
	private static Text i1Text;
	private static final int i1YOffset = 40;
	
	private static Text i2Text;
	private static final int i2YOffset = 55;
	
	private static Text i3Text;
	private static final int i3YOffset = 70;
	
	
	// highlight array cells
	public static void highlightCell(IntArray array, int idx, Color highlightColor) {
		array.setHighlightFillColor(idx, highlightColor, null, null);
		array.highlightCell(idx, null, null);
	}
	
	public static void hideArrayMarker(ArrayMarker marker) {
		marker.hide();
	}
	
	
	public static void addToClearList(Object o) {
		clearList.add(o);
	}
	
	public static void clear() {
		for(Object o : clearList) {
			if(o instanceof Text) {
				((Text)o).hide();
			}
			else if(o instanceof SourceCode) {
				((SourceCode)o).hide();
			}
			else if(o instanceof IntArray) {
				((IntArray)o).hide();
			}
			else if(o instanceof ArrayMarker) {
				((ArrayMarker)o).hide();
			}
			else if(o instanceof Rect) {
				((Rect)o).hide();
			}
		}
		
		clearList.clear();
	}
	
	
	// ====================== makeXXX ===========================
	
	
	public static Text makeText(String text, int x, int y, TextProperties textProps) {
		return new Text(new AnimalTextGenerator(lang), new Coordinates(x, y), text, "text", null, textProps);
	}
	
	public static Rect makeBox(int upperLeftX, int upperLeftY, int lowerRightX, int lowerRightY, RectProperties rectProps) {
		upLeftX = upperLeftX;
		upLeftY = upperLeftY;
		Rect box = new Rect(new AnimalRectGenerator(lang), new Coordinates(upperLeftX, upperLeftY), new Coordinates(lowerRightX, lowerRightY), "rect", null, rectProps);
		addToClearList(box);
		return box;
	}
	
	public static void makeTitle() {
		RectProperties titleBoxProps = new RectProperties();
		titleBoxProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleBoxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		titleBoxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		new Rect(new AnimalRectGenerator(lang), new Coordinates(20, 50), new Coordinates(300, 90), "titleBox", null, titleBoxProps);
		
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		new Text(new AnimalTextGenerator(lang), new Coordinates(40, 60), "Pivot Partitioning by Scanning", "title", null, titleProps);
	}
	
	// pivot and iteration and result texts
	
	public static void makePivotText(int pivot, int x, int y, TextProperties pivotTextProps) {
		pivotText = makeText("pivot = " + pivot, x, y, pivotTextProps);
		addToClearList(pivotText);
	}
	
	public static void makeIterationText(int iteration, int x, int y, TextProperties iterationTextProps) {
		if(iterationText != null)
			iterationText.hide();
		iterationText = makeText(builder.generateJLabel("iter").getText() + " = " + iteration, x, y, iterationTextProps);
		addToClearList(iterationText);
	}
	
	public static void makeResultText(int[] result, int x, int y, TextProperties resultTextProps) {
		resultText = makeText(builder.generateJLabel("res").getText() + " = [m1, m2] = [" + result[0] + ", " + result[1] + "]", x, y, resultTextProps);
		addToClearList(resultText);
	}
	
	// write values of indices into the box
	
	public static void makeM1Text(int m1, TextProperties textProps) {
		if(m1Text != null)
			m1Text.hide();
		
		m1Text = makeBoxText(m1Text, "m1 = " + m1, m1YOffset, textProps);
		addToClearList(m1Text);
	}
	
	public static void makeM2Text(int m2, TextProperties textProps) {
		if(m2Text != null)
			m2Text.hide();
		
		m2Text = makeBoxText(m2Text, "m2 = " + m2, m2YOffset, textProps);
		addToClearList(m2Text);
	}
	
	public static void makeI1Text(int i1, TextProperties textProps) {
		if(i1Text != null)
			i1Text.hide();
		
		i1Text = makeBoxText(i1Text, "i1 = " + i1, i1YOffset, textProps);
		addToClearList(i1Text);
	}
	
	public static void makeI2Text(int i2, TextProperties textProps) {
		if(i2Text != null)
			i2Text.hide();
		
		i2Text = makeBoxText(i2Text, "i2 = " + i2, i2YOffset, textProps);
		addToClearList(i2Text);
	}
	
	public static void makeI3Text(int i3, TextProperties textProps) {
		if(i3Text != null)
			i3Text.hide();
		
		i3Text = makeBoxText(i3Text, "i3 = " + i3, i3YOffset, textProps);
		addToClearList(i3Text);
	}
	
	private static Text makeBoxText(Text text, String txt, int offsetY, TextProperties textProps) {
		if(text != null) 
			text.hide();
		
		int x = upLeftX + offsetX;
		int y = upLeftY + offsetY;
		return new Text(new AnimalTextGenerator(lang), new Coordinates(x, y), txt, "boxText", null, textProps);
	}
	
	
	// intro and outro texts
	
	public static void showIntroText(TextProperties textProps) {
		Text introText = makeText(builder.generateJLabel("in0").getText(), 50, 140, textProps);
		introText.changeColor("", Color.BLUE, null, null);
		introText.setFont(new Font("Monospaced", Font.BOLD, 20), null, null);
		addToClearList(introText);
		
		addToClearList(makeText(builder.generateJLabel("in1").getText(), 50, 170, textProps));
		addToClearList(makeText(builder.generateJLabel("in2").getText(), 50, 190, textProps));
		addToClearList(makeText(builder.generateJLabel("in3").getText(), 50, 210, textProps));
		addToClearList(makeText(builder.generateJLabel("in4").getText(), 50, 230, textProps));
		addToClearList(makeText(builder.generateJLabel("in5").getText(), 50, 250, textProps));
		addToClearList(makeText(builder.generateJLabel("in6").getText(), 50, 300, textProps));
		addToClearList(makeText("  " + builder.generateJLabel("in7").getText(), 50, 320, textProps));
		addToClearList(makeText("  " + builder.generateJLabel("in8").getText(), 50, 340, textProps));
		addToClearList(makeText("  " + builder.generateJLabel("in9").getText(), 50, 360, textProps));
	}
	
	public static void showOutroText(TextProperties textProps) {
		Text outroText = makeText(builder.generateJLabel("out0").getText(), 50, 140, textProps);
		outroText.changeColor("", Color.BLUE, null, null);
		outroText.setFont(new Font("Monospaced", Font.BOLD, 20), null, null);
		addToClearList(outroText);
		
		addToClearList(makeText(builder.generateJLabel("out1").getText(), 50, 170, textProps));
		Text algoLink = makeText("    " + builder.generateJLabel("out2").getText(), 50, 200, textProps);
		Color c = (Color)algoLink.getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY);
		algoLink.changeColor("", c == Color.BLUE ? Color.ORANGE : Color.BLUE, null, null);
		addToClearList(algoLink);
		
		addToClearList(makeText(builder.generateJLabel("out3").getText(), 50, 250, textProps));
		Text nablaLink = makeText("    " + builder.generateJLabel("out4").getText(), 50, 280, textProps);
		nablaLink.changeColor("", c == Color.BLUE ? Color.ORANGE : Color.BLUE, null, null);
		addToClearList(nablaLink);
		
		
		addToClearList(makeText(builder.generateJLabel("out5").getText(), 50, 350, textProps));
	}
}
