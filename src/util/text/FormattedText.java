package util.text;

import java.awt.Color;
import java.awt.Font;
import java.util.Stack;
import java.util.Vector;

import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Point;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.CircleProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PropertiesBuilder;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * The class <tt>FormattedText</tt> contains the functionality to insert
 * formatted text in AnimalScript.
 * @author Christian Ritter
 *
 */
public class FormattedText {

	public final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private String name;
	private Language lang;
	private TextProperties props;
	private Node pos;
	private Stack<EnumFormat> enumStack;
	private Vector<Text> fragmentList;
	private Vector<Primitive> itemList;
	private double wsFactor;
	private double tabFactor;
	private double itemFactor;
	private double parDistFactor;
	private double lineDistFactor;
	private int firstElemInLine;
	private int tabDist;
	private boolean stepMode;
	private boolean utf8;
	
	
	/**
	 * The enum <tt>ItemForm</tt> contains possible forms of bullets.<br /><br />
	 * The following forms are supported:<br />
	 * <tt>NONE</tt> - no bullet<br />
	 * <tt>SQUARE</tt> - a filled square<br />
	 * <tt>SQUARELINE</tt> - an unfilled square<br />
	 * <tt>CIRCLE</tt> - a circle<br />
	 * <tt>CIRCLELINE</tt> - an unfilled circle<br />
	 * <tt>TRIANGLE</tt> - a triangle<br />
	 * <tt>TRIANGLELINE</tt> - an unfilled triangle
	 * @author Christian Ritter
	 *
	 */
	public enum ItemForm {
		NONE,
		SQUARE,
		SQUARELINE,
		CIRCLE,
		CIRCLELINE,
		TRIANGLE,
		TRIANGLELINE
	};
	
	/**
	 * Constructor for class <tt>FormattedText</tt>.
	 * @param name - A name for labelling the text fragments which will be added.
	 * Be careful: If you are going to define offset relative to the formatted text, you cannot use this name.
	 * Instead of it, you must add an index number, so that you get the label of an text fragment.
	 * @param lang - The <tt>Language</tt>-object to which the formatted text should be added.
	 * @param pos - The position of the upper left corner of the formatted text.
	 * <p>There are more properties of the formatted text which you can set. If you want to do so, use another constructor
	 * or set them by using a setter function.</p>
	 */
	public FormattedText(String name, Language lang, Node pos) {
		init(name, lang, pos, new TextProperties());
	}
	
	/**
	 * Constructor for class <tt>FormattedText</tt>.
	 * @param name - A name for labelling the text fragments which will be added.
	 * Be careful: If you are going to define offset relative to the formatted text, you cannot use this name.
	 * Instead of it, you must add an index number, so that you get the label of an text fragment.
	 * @param lang - The <tt>Language</tt>-object to which the formatted text should be added.
	 * @param pos - The position of the upper left corner of the formatted text.
	 * @param props - The <tt>TextProperties</tt> which should be used for the formatted text.
	 * @param stepMode - If this parameter is set true, each paragraph will be shown step by step.
	 * @param lineDistFactor - The ratio between font size and distance between two lines which are in one paragraph.
	 * @param parDistFactor - The ratio between font size and distance between two paragaphs.
	 * <p>There are more properties of the formatted text which you can set. If you want to do so, use another constructor
	 * or set them by using a setter function.</p>
	 */
	public FormattedText(String name, Language lang, Node pos, TextProperties props, boolean stepMode, double lineDistFactor, double parDistFactor) {
		init(name, lang, pos, props);
		this.lineDistFactor = lineDistFactor;
		this.parDistFactor = parDistFactor;
		this.stepMode = stepMode;
	}
	
	/**
	 * Constructor for class <tt>FormattedText</tt>.
	 * @param name - A name for labelling the text fragments which will be added.
	 * Be careful: If you are going to define offset relative to the formatted text, you cannot use this name.
	 * Instead of it, you must add an index number, so that you get the label of a text fragment.
	 * @param lang - The <tt>Language</tt>-object to which the formatted text should be added.
	 * @param pos - The position of the upper left corner of the formatted text.
	 * @param props - The <tt>TextProperties</tt> which should be used for the formatted text.
	 * @param stepMode - If this parameter is set true, each paragraph will be shown step by step.
	 * @param useUtf8 - If this parameter is set true, the added text can contain UTF-8 symbols.
	 * @param lineDistFactor - The ratio between font size and distance between two lines which are in one paragraph.
	 * @param parDistFactor - The ratio between font size and distance between two paragaphs.
	 * @param tabFactor - The ratio between font size and tabulator distance. The tabulator distance is used by
	 * defining lists.
	 * @param wsFactor - The ratio between font size and whitespace distance. The whitespace distance is ued by
	 * adding more than one text fragment in one line.
	 * @param itemFactor - The ratio between font size and bullet size. 
	 */
	public FormattedText(String name, Language lang, Node pos, TextProperties props, boolean stepMode, boolean useUtf8,
		                 double lineDistFactor, double parDistFactor, double tabFactor, double wsFactor, double itemFactor) {
		init(name, lang, pos, props);
		this.lineDistFactor = lineDistFactor;
		this.parDistFactor = parDistFactor;
		this.tabFactor = tabFactor;
		this.wsFactor = wsFactor;
		this.itemFactor = itemFactor;
		this.stepMode = stepMode;
		this.utf8 = useUtf8;
	}
	
	private void init(String name, Language lang, Node pos, TextProperties props) {
		this.name = name;
		this.lang = lang;
		this.pos = pos;
		this.props = (props == null) ? new TextProperties() : PropertiesBuilder.createTextProperties(props);
		this.props.set("centered", false);
		wsFactor = 0.3;
		tabFactor = 2;
		itemFactor = 0.4;
		lineDistFactor = 0.3;
		parDistFactor = 0.6;
		tabDist = 0;
		enumStack = new Stack<EnumFormat>();
		fragmentList = new Vector<Text>();
		itemList = new Vector<Primitive>();
		firstElemInLine = -1;
		stepMode = false;
		utf8 = true;
		Point p = lang.newPoint(pos, name + "_ref", null, new PointProperties());
		p.hide();
	}
	
	/**
	 * @return The given name of the instance.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The <tt>Language</tt>-object to which the formatted text will be added.
	 */
	public Language getLang() {
		return lang;
	}
	
	/**
	 * Adds a text fragment.
	 * @param text - The content of the new text fragment.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text add(String text) {
		return add(text, null, null, null, null, null, false);
	}
	
	/**
	 * Adds a text fragment.
	 * @param text - The content of the new text fragment.
	 * @param monospaced - Decides, whether the text should be written in monospace letters.
	 * If this parameter is <tt>null</tt>, the font family will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text add(String text, Boolean monospaced) {
		return add(text, null, null, monospaced, null, null, false);
	}
	
	/**
	 * Adds a text fragment.
	 * @param text - The content of the new text fragment.
	 * @param bold - Decides, whether the text should be written in bold letters.
	 * If this parameter is <tt>null</tt>, the font weight will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param italic - Decides, whether the text should be written in italic letters.
	 * If this parameter is <tt>null</tt>, the font style will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text add(String text, Boolean bold, Boolean italic) {
		return add(text, bold, italic, null, null, null, false);
	}
	
	/**
	 * Adds a text fragment.
	 * @param text - The content of the new text fragment.
	 * @param color - The text color.
	 * If this parameter is <tt>null</tt>, the font color will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text add(String text, Color color) {
		return add(text, null, null, null, color, null, false);
	}
	
	/**
	 * Adds a text fragment.
	 * @param text - The content of the new text fragment.
	 * @param wsDist - The distance from left.
	 * If this parameter is <tt>null</tt>, the font color will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param absolute - If this is true, the distance from left is taken from the line start.
	 * Otherwise, the distance is taken from the right corner of the text fragment which was added last.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text add(String text, int wsDist, boolean absolute) {
		return add(text, null, null, null, null, wsDist, absolute);
	}
	
	/**
	 * Adds a text fragment.
	 * @param text - The content of the new text fragment.
	 * @param bold - Decides, whether the text should be written in bold letters.
	 * If this parameter is <tt>null</tt>, the font weight will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param italic - Decides, whether the text should be written in italic letters.
	 * If this parameter is <tt>null</tt>, the font style will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param monospaced - Decides, whether the text should be written in monospace letters.
	 * If this parameter is <tt>null</tt>, the font family will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param color - The text color.
	 * If this parameter is <tt>null</tt>, the font color will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param wsDist - The distance from left.
	 * If this parameter is <tt>null</tt>, the font color will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param absolute - If this is true, the distance from left is taken from the line start.
	 * Otherwise, the distance is taken from the right corner of the text fragment which was added last.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text add(String text, Boolean bold, Boolean italic, Boolean monospaced, Color color, Integer wsDist, boolean absolute) {
		TextProperties props = changeProp(this.props, bold, italic, monospaced, color, null);
		Font font = (Font) props.getItem("font").get();
		int myWsDist = (wsDist == null) ? resize(font.getSize(), wsFactor) : wsDist.intValue();
		Offset offset = absolute ? new Offset(myWsDist, 0, fragmentList.get(firstElemInLine).getName(), "NW") :
			                       new Offset(myWsDist, 0, fragmentList.lastElement().getName(), "NE");
		Text result = lang.newText(offset, replaceUmlauts(text), name + fragmentList.size(), null, props);
		fragmentList.add(result);
		return result;
	}
	
	/**
	 * Adds a text fragment in a new line.
	 * Inside an enumarte, no bullet will be added.
	 * @param text - The content of the new text fragment.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addInNextLine(String text) {
		return addLine(text, false, null, null, null, null, null, null);
	}
	
	/**
	 * Adds a text fragment in a new line.
	 * Inside an enumarte, no bullet will be added.
	 * @param text - The content of the new text fragment.
	 * @param monospaced - Decides, whether the text should be written in monospace letters.
	 * If this parameter is <tt>null</tt>, the font family will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addInNextLine(String text, Boolean monospaced) {
		return addLine(text, false, null, null, monospaced, null, null, null);
	}
	
	/**
	 * Adds a text fragment in a new line.
	 * Inside an enumarte, no bullet will be added.
	 * @param text - The content of the new text fragment.
	 * @param bold - Decides, whether the text should be written in bold letters.
	 * If this parameter is <tt>null</tt>, the font weight will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param italic - Decides, whether the text should be written in italic letters.
	 * If this parameter is <tt>null</tt>, the font style will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addInNextLine(String text, Boolean bold, Boolean italic) {
		return addLine(text, false, bold, italic, null, null, null, null);
	}
	
	/**
	 * Adds a text fragment in a new line.
	 * Inside an enumarte, no bullet will be added.
	 * @param text - The content of the new text fragment.
	 * @param color - The text color.
	 * If this parameter is <tt>null</tt>, the font color will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addInNextLine(String text, Color color) {
		return addLine(text, false, null, null, null, color, null, null);
	}
	
	public Text addInNextLine(String text, Boolean bold, Boolean italic, Boolean monospaced, Color color) {
		return addLine(text, false, bold, italic, monospaced, color, null, null);
	}
	
	/**
	 * Adds a text fragment in a new line. A new paragraph will be started.
	 * Inside an enumarte, a bullet will be added.
	 * @param text - The content of the new text fragment.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addAsNewPar(String text) {
		return addLine(text, true, null, null, null, null, null, null);
	}
	
	/**
	 * Adds a text fragment in a new line. A new paragraph will be started.
	 * Inside an enumarte, a bullet will be added.
	 * @param text - The content of the new text fragment.
	 * @param size - The font size.
	 * If this parameter is <tt>null</tt>, the font size will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param lineDistFactor - The ratio between font size and distance between this line and the previous line.
	 * This distance is used only here. To change the line distance globally, use
	 * <tt>setLineDistance(int dist)</tt>.
	 * If this parameter is <tt>null</tt> the global line distance is used.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addAsNewPar(String text, Integer size, Double lineDistFactor) {
		return addLine(text, true, null, null, null, null, size, lineDistFactor);
	}
	
	/**
	 * Adds a text fragment in a new line. A new paragraph will be started.
	 * Inside an enumarte, a bullet will be added.
	 * @param text - The content of the new text fragment.
	 * @param monospaced - Decides, whether the text should be written in monospace letters.
	 * If this parameter is <tt>null</tt>, the font family will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addAsNewPar(String text, Boolean monospaced) {
		return addLine(text, true, null, null, monospaced, null, null, null);
	}
	
	/**
	 * Adds a text fragment in a new line. A new paragraph will be started.
	 * Inside an enumarte, a bullet will be added.
	 * @param text - The content of the new text fragment.
	 * @param bold - Decides, whether the text should be written in bold letters.
	 * If this parameter is <tt>null</tt>, the font weight will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param italic - Decides, whether the text should be written in italic letters.
	 * If this parameter is <tt>null</tt>, the font style will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addAsNewPar(String text, Boolean bold, Boolean italic) {
		return addLine(text, true, bold, italic, null, null, null, null);
	}
	
	/**
	 * Adds a text fragment in a new line.
	 * @param text - The content of the new text fragment.
	 * @param bold - Decides, whether the text should be written in bold letters.
	 * If this parameter is <tt>null</tt>, the font weight will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param italic - Decides, whether the text should be written in italic letters.
	 * If this parameter is <tt>null</tt>, the font style will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param monospaced - Decides, whether the text should be written in monospace letters.
	 * If this parameter is <tt>null</tt>, the font family will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param size - The font size.
	 * If this parameter is <tt>null</tt>, the font size will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param lineDistFactor - The ratio between font size and distance between this line and the previous line.
	 * This distance is used only here. To change the line distance globally, use
	 * <tt>setParagraphDistance(int dist)</tt>.
	 * If this parameter is <tt>null</tt> the global line distance is used.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addAsNewPar(String text, Boolean bold, Boolean italic, Boolean monospaced, Integer size, Double lineDistFactor) {
		return addLine(text, true, bold, italic, monospaced, null, size, lineDistFactor);
	}
	
	/**
	 * Adds a text fragment in a new line.
	 * @param text - The content of the new text fragment.
	 * @param props - The font style.
	 * @param lineDistFactor - The ratio between font size and distance between this line and the previous line.
	 * This distance is used only here. To change the line distance globally, use
	 * <tt>setParagraphDistance(int dist)</tt>.
	 * If this parameter is <tt>null</tt> the global line distance is used.
	 * @return The <tt>Text</tt>-object of the text fragment which was created and added.
	 */
	public Text addAsNewPar(String text, Double lineDistFactor, TextProperties props) {
		TextProperties tProps = PropertiesBuilder.createTextProperties(props);
		tProps.set("centered", false);
		return addLine(text, true, tProps, lineDistFactor);
	}
	
	private Text addLine(String text, boolean newPar, Boolean bold, Boolean italic, Boolean monospaced, Color color, Integer size, Double lineDistFactor) {
		TextProperties props = changeProp(this.props, bold, italic, monospaced, color, size);
		return addLine(text, newPar, props, lineDistFactor);
	}
	
	private Text addLine(String text, boolean newPar, TextProperties props, Double lineDistFactor) {
		TextProperties tProps = PropertiesBuilder.createTextProperties(props);
		tProps.set("centered", false);
		if (newPar && stepMode) lang.nextStep();
		Font font = (Font) tProps.get("font");
		int lineDist = (lineDistFactor == null) ? 
			(newPar ? resize(font.getSize(), this.parDistFactor) : resize(font.getSize(), this.lineDistFactor)) :
			resize(font.getSize(), lineDistFactor);
		if (!enumStack.isEmpty() && lineDistFactor == null && newPar) {
			EnumFormat format = enumStack.peek();
			if (format.index > 0) lineDist = resize(font.getSize(), format.innerDistFactor);
			else if (enumStack.size() > 1) {
				lineDist = resize(font.getSize(), enumStack.get(enumStack.size() - 2).innerDistFactor);
			}
		}
		Offset offset;
		if (firstElemInLine == -1) offset = new Offset(tabDist, 0, name + "_ref", "C");
		else offset = new Offset(tabDist, lineDist, fragmentList.get(firstElemInLine).getName(), "SW");
		Text result = lang.newText(offset, replaceUmlauts(text), name + fragmentList.size(), null, tProps);
		firstElemInLine = fragmentList.size();
		fragmentList.add(result);
		tabDist = 0;
		if (!enumStack.isEmpty() && newPar) {	
			Primitive item = addItem(enumStack.lastElement());
			itemList.add(item);
		}
		return result;
	}
	
	private Primitive addItem(EnumFormat format) {
		
		int i = itemList.size();
		int j = fragmentList.size() - 1;
		Font font = (Font) format.props.getItem("font").get();
		int size = resize(font.getSize(), format.sizeFactor);
		int halfsize = resize(font.getSize(), 0.5 * format.sizeFactor);
		CircleProperties cprop = new CircleProperties();
		RectProperties rprop = new RectProperties();
		PolygonProperties pprop = new PolygonProperties();
		cprop.set("color", format.color);
		rprop.set("color", format.color);
		pprop.set("color", format.color);
		format.index++;
		
		Node[] nodes = new Node[3];
		nodes[0] = new Offset(0 - format.indent, 0 - halfsize, name + j, "W");
		nodes[1] = new Offset(size - format.indent, 0, name + j, "W");
		nodes[2] = new Offset(0 - format.indent, halfsize, name + j, "W");

		/*
		 * dfischer: this once was a switch with much less code through elegant
     * falling through. Then Animal came and said it should be Java 1.6 compatible.
		 */
		if (format.form.equals("square")) {
      rprop.set("filled", true);
      rprop.set("fillColor", format.color);
      return lang.newRect(nodes[0], new Offset(size - format.indent, halfsize, name + j, "W"), name + "Item" + i, null, rprop);
		} else if (format.form.equals("squareline")) {
      return lang.newRect(nodes[0], new Offset(size - format.indent, halfsize, name + j, "W"), name + "Item" + i, null, rprop);
    } else if (format.form.equals("circle")) {
      cprop.set("filled", true);
      cprop.set("fillColor", format.color);
      return lang.newCircle(new Offset(resize(halfsize, 0.5) - format.indent + 1, 0, name + j, "W"),
          halfsize, name + "Item" + i, null, cprop);
    } else if (format.form.equals("circleline")) {
      return lang.newCircle(new Offset(resize(halfsize, 0.5) - format.indent + 1, 0, name + j, "W"),
          halfsize, name + "Item" + i, null, cprop);
    } else if (format.form.equals("triangle")) {
      pprop.set("filled", true);
      pprop.set("fillColor", format.color);
      try {
        return lang.newPolygon(nodes, name + "Item" + i, null, pprop);
      }
      catch (NotEnoughNodesException e) {
        return null;
      }
    } else if (format.form.equals("triangleline")) {
      try {
        return lang.newPolygon(nodes, name + "Item" + i, null, pprop);
      }
      catch (NotEnoughNodesException e) {
        return null;
      }
    } else {
      String form = format.form;
      NumberConverter num = new NumberConverter(format.index);
      String roman = num.toRoman();
      String alpha = num.toAlpha();
      form = form.replace("none", "");
      form = form.replaceFirst("1", "" + format.index);
      form = form.replaceFirst("i", roman.toLowerCase());
      form = form.replaceFirst("I", roman);
      form = form.replaceFirst("a", alpha.toLowerCase());
      form = form.replaceFirst("A", alpha);
      return lang.newText(new Offset(0 - format.indent, 0, name + j, "NW"), form, name + "Item" + i, null, format.props);
    }
	}
	
	/**
	 * Starts a new itemization.
	 * @param form The bullet form. If this parameter is <tt>null</tt>, a filled circle is used.
	 * @see ItemForm
	 */
	public void beginItemize(ItemForm form) {
		beginItemize(form, null, null, null, null, null);
	}
	
	/**
	 * Starts a new itemization.
	 * @param form The bullet form. If this parameter is <tt>null</tt>, a filled circle is used.
	 * @param indent - The indention of the itemization. This parameter sets the indention only for this list.
	 * If <tt>indent</tt> is <tt>null</tt>, the indention is calculated from the <tt>tabFactor</tt>.
	 * For changing the <tt>tabFactor</tt> globally, use <tt>setTabulatorFactor(int factor)</tt>.
	 * @param color - The color of the bullets. If this parameter is <tt>null</tt>,
	 * the font color is used.
	 * @see ItemForm
	 */
	public void beginItemize(ItemForm form, Integer indent, Color color) {
		beginItemize(form, indent, color, null, null, null);
	}
	
	/**
	 * Starts a new itemization.
	 * @param form The bullet form. If this parameter is <tt>null</tt>, a filled circle is used.
	 * @param indent - The indention of the itemization. This parameter sets the indention only for this list.
	 * If <tt>indent</tt> is <tt>null</tt>, the indention is calculated from the <tt>tabFactor</tt>.
	 * For changing the <tt>tabFactor</tt> globally, use <tt>setTabulatorFactor(int factor)</tt>.
	 * @param color - The color of the bullets. If this parameter is <tt>null</tt>,
	 * the font color is used.
	 * @param sizeFactor - The ratio between the font size and the size of the bullet. If this parameter
	 * is <tt>null</tt>, the global <tt>itemFactor</tt> is used. For changing the <tt>itemFactor</tt>,
	 * use <tt>setItemFactor(int factor)</tt>.
	 * @param innerDistFactor - The line distance inside the itemization. If this parameter is <tt>null</tt>,
	 * the normal line distance (not the paragraph distance) is used.
	 * @see ItemForm
	 */
	public void beginItemize(ItemForm form, Integer indent, Color color, Double sizeFactor, Double innerDistFactor) {
		beginItemize(form, indent, color, sizeFactor, innerDistFactor, null);
	}
	
	/**
	 * Starts a new itemization.
	 * @param form The bullet form. If this parameter is <tt>null</tt>, a filled circle is used.
	 * @param indent - The indention of the itemization. This parameter sets the indention only for this list.
	 * If <tt>indent</tt> is <tt>null</tt>, the indention is calculated from the <tt>tabFactor</tt>.
	 * For changing the <tt>tabFactor</tt> globally, use <tt>setTabulatorFactor(int factor)</tt>.
	 * @param color - The color of the bullets. If this parameter is <tt>null</tt>,
	 * the font color is used.
	 * @param sizeFactor - The ratio between the font size and the size of the bullet. If this parameter
	 * is <tt>null</tt>, the global <tt>itemFactor</tt> is used. For changing the <tt>itemFactor</tt>,
	 * use <tt>setItemFactor(int factor)</tt>.
	 * @param innerDistFactor - The line distance inside the itemization. If this parameter is <tt>null</tt>,
	 * the normal line distance (not the paragraph distance) is used.
	 * @param preIndent - The indention for the bullets. If this parameter is <tt>null</tt>, no pre-indention will be used.
	 * @see ItemForm
	 */
	public void beginItemize(ItemForm form, Integer indent, Color color, Double sizeFactor, Double innerDistFactor, Integer preIndent) {
		ItemForm effectiveForm = (form == null) ? ItemForm.CIRCLE : form;
		beginList(effectiveForm.toString().toLowerCase(), indent, color, null, null, sizeFactor, innerDistFactor, preIndent);
	}

	private void beginList(String form, Integer indent, Color color, Boolean bold, Boolean italic, Double sizeFactor,
			               Double innerDistFactor, Integer preIndent) {
		TextProperties props = changeProp(this.props, bold, italic, null, color, null);
		Font font = (Font) props.getItem("font").get();
		int myIndent = (indent == null) ? resize(font.getSize(), tabFactor) : indent.intValue();
		Color myColor = (color == null) ? (Color) props.getItem("color").get() : color;
		double mySizeFactor = (sizeFactor == null) ? itemFactor : sizeFactor.doubleValue();
		double myInnerDistFactor = (innerDistFactor == null) ? lineDistFactor : innerDistFactor.doubleValue();
		int myPreIndent = (preIndent == null) ? 0 : preIndent.intValue();
		tabDist += (myPreIndent + myIndent);
		EnumFormat format = new EnumFormat(form, myIndent, mySizeFactor, props, myColor, myInnerDistFactor, myPreIndent);
		enumStack.push(format);
	}
	
	/**
	 * Ends the previous itemization.
	 */
	public void endItemize() {
		EnumFormat format = enumStack.pop();
		tabDist -= format.indent + format.preIndent;
	}
	
	/**
	 * Starts a new enumerate.
	 * @param form - A pattern for the enumerate. For example "(1.)" defines an
	 * enumerate with (1.), (2.), (3.), ... etc.<br />
	 * Use "1" for Arabic numbers, "I" for Roman numbers, "i" for lower Roman numbers,
	 * "A" for alphabetic enumerate in captial letters and "a" if you wish small letters.
	 * If this paramter is <tt>null</tt>, the enumerate style is "1.".
	 */
	public void beginEnumerate(String form) {
		beginEnumerate(form, null, null, null, null, null, null);
	}
	
	/**
	 * Starts a new enumerate.
	 * @param form - A pattern for the enumerate. For example "(1.)" defines an
	 * enumerate with (1.), (2.), (3.), ...
	 * Use "1" for Arabic numbers, "I" for Roman numbers, "i" for lower Roman numbers,
	 * "A" for alphabetic enumerate in captial letters and "a" if you wish small letters.
	 * If this paramter is <tt>null</tt>, the enumerate style is "1.".
	 * @param indent - The indention of the itemization. This parameter sets the indention only for this list.
	 * If <tt>indent</tt> is <tt>null</tt>, the indention is calculated from the <tt>tabFactor</tt>.
	 * For changing the <tt>tabFactor</tt> globally, use <tt>setTabulatorFactor(int factor)</tt>.
	 * @param color - The color of the bullets. If this parameter is <tt>null</tt>,
	 * the font color is used.
	 * @param bold - Decides, whether the enumeration text should be written in bold letters.
	 * If this parameter is <tt>null</tt>, the font weight will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 */
	public void beginEnumerate(String form, Integer indent, Color color, Boolean bold) {
		beginEnumerate(form, indent, color, bold, null, null, null);
	}
	
	/**
	 * Starts a new enumerate.
	 * @param form - A pattern for the enumerate. For example "(1.)" defines an
	 * enumerate with (1.), (2.), (3.), ...
	 * Use "1" for Arabic numbers, "I" for Roman numbers, "i" for lower Roman numbers,
	 * "A" for alphabetic enumerate in captial letters and "a" if you wish small letters.
	 * If this paramter is <tt>null</tt>, the enumerate style is "1.".
	 * @param indent - The indention of the itemization. This parameter sets the indention only for this list.
	 * If <tt>indent</tt> is <tt>null</tt>, the indention is calculated from the <tt>tabFactor</tt>.
	 * For changing the <tt>tabFactor</tt> globally, use <tt>setTabulatorFactor(int factor)</tt>.
	 * @param color - The color of the bullets. If this parameter is <tt>null</tt>,
	 * the font color is used.
	 * @param bold - Decides, whether the enumeration text should be written in bold letters.
	 * If this parameter is <tt>null</tt>, the font weight will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param innerDistFactor - The line distance inside the enumerate. If this parameter is <tt>null</tt>,
	 * the normal line distance (not the paragraph distance) is used.
	 */
	public void beginEnumerate(String form, Integer indent, Color color, Boolean bold, Double innerDistFactor) {
		beginEnumerate(form, indent, color, bold, null, innerDistFactor, null);
	}
	
	/**
	 * Starts a new enumerate.
	 * @param form - A pattern for the enumerate. For example "(1.)" defines an
	 * enumerate with (1.), (2.), (3.), ...
	 * Use "1" for Arabic numbers, "I" for Roman numbers, "i" for lower Roman numbers,
	 * "A" for alphabetic enumerate in captial letters and "a" if you wish small letters.
	 * If this paramter is <tt>null</tt>, the enumerate style is "1.".
	 * @param indent - The indention of the itemization. This parameter sets the indention only for this list.
	 * If <tt>indent</tt> is <tt>null</tt>, the indention is calculated from the <tt>tabFactor</tt>.
	 * For changing the <tt>tabFactor</tt> globally, use <tt>setTabulatorFactor(int factor)</tt>.
	 * @param color - The color of the bullets. If this parameter is <tt>null</tt>,
	 * the font color is used.
	 * @param bold - Decides, whether the enumeration text should be written in bold letters.
	 * If this parameter is <tt>null</tt>, the font weight will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param italic - Decides, whether the enumeration text should be written in italic letters.
	 * If this parameter is <tt>null</tt>, the font weight will be chosen from the given
	 * <tt>TextProperties</tt>-object.
	 * @param innerDistFactor - The line distance inside the enumerate. If this parameter is <tt>null</tt>,
	 * the normal line distance (not the paragraph distance) is used.
	 * @param preIndent - The indention for the bullets. If this parameter is <tt>null</tt>, no pre-indention will be used.
	 */
	public void beginEnumerate(String form, Integer indent, Color color, Boolean bold, Boolean italic, Double innerDistFactor, Integer preIndent) {
		String theForm = (form == null) ? "1." : form ;
		beginList(theForm, indent, color, bold, italic, null, innerDistFactor, preIndent);
	}
	
	/**
	 * Ends the previous enumerate.
	 */
	public void endEnumerate() {
		endItemize();
	}
	
	/**
	 * Setter for:
	 * @param index - The number which will be set in front of the next enumeration index.
	 */
	public void setEnumerateIndex(int index) {
		if (index <= 0) throw new IllegalArgumentException("You are not allowed to set a negative enumeration index.");
		enumStack.lastElement().index = index - 1;
	}

	/**
	 * @return The text style that is used for this formatted text.
	 */
	public TextProperties getTextProperties() {
		return props;
	}

	/**
	 * Setter for:
	 * @param props - The text style that is used for this formatted text.
	 */
	public void setTextProperties(TextProperties props) {
		this.props = PropertiesBuilder.createTextProperties(props);
		this.props.set("centered", false);
	}
	
	/**
	 * @return The position of the upper left corner of the formatted text.
	 */
	public Node getPosition() {
		return pos;
	}
	
	/**
	 * @return The ratio between font size and whitespace distance. The whitespace distance is ued by
	 * adding more than one text fragment in one line.
	 */
	public double getWhitespaceFactor() {
		return wsFactor;
	}
	
	/**
	 * Setter for:
	 * @param wsFactor - The ratio between font size and whitespace distance. The whitespace distance is used by
	 * adding more than one text fragment in one line.
	 */
	public void setWhitespaceFactor(double wsFactor) {
		this.wsFactor = wsFactor;
	}
	
	/**
	 * @return The ratio between font size and tabulator distance. The tabulator distance is used by
	 * defining lists.
	 */
	public double getTabulatorFactor() {
		return tabFactor;
	}
	
	/**
	 * Setter for:
	 * @param tabFactor - The ratio between font size and tabulator distance. The tabulator distance is used by
	 * defining lists.
	 */
	public void setTabulatorFactor(double tabFactor) {
		this.tabFactor = tabFactor;
	}
	
	/**
	 * @return The ratio between font size and bullet size.
	 */
	public double getItemFactor() {
		return itemFactor;
	}
	
	/**
	 * Setter for:
	 * @param itemFactor - The ratio between font size and bullet size.
	 */
	public void setItemFactor(double itemFactor) {
		this.itemFactor = itemFactor;
	}
	
	/**
	 * @return The ratio between font size and distance between two paragaphs.
	 */
	public double getParagraphDist() {
		return parDistFactor;
	}
	
	/**
	 * Setter for:
	 * @param parDistFactor - The ratio between font size and distance between two paragaphs.
	 */
	public void setParagraphDist(double parDistFactor) {
		this.parDistFactor = parDistFactor;
	}
	
	/**
	 * @return The ratio between font size and distance between two lines which are in one paragraph.
	 */
	public double getLineDist() {
		return lineDistFactor;
	}
	
	/**
	 * Setter for:
	 * @param lineDistFactor - The ratio between font size and distance between two lines which are in one paragraph.
	 */
	public void setLineDist(double lineDistFactor) {
		this.lineDistFactor = lineDistFactor;
	}
	
	/**
	 * @return The primitive which contains the last added bullet resp. the last added enumeration number.
	 */
	public Primitive getLastItemPrimitive() {
		return itemList.lastElement();
	}
	
	/**
	 * @return If this parameter is true, each paragraph will be shown step by step.
	 */
	public boolean getStepMode() {
		return stepMode;
	}
	
	/**
	 * @param stepMode - If this parameter is set true, each paragraph will be shown step by step.
	 */
	public void setStepMode(boolean stepMode) {
		this.stepMode = stepMode;
	}
	
	/**
	 * @return If this parameter is true, the added text is used in utf8-encoding.
	 */
	public boolean isUTF8() {
		return utf8;
	}
	
	/**
	 * @param useUTF8 - If this parameter is true, the added text is used in utf8-encoding.
	 */
	public void setEncoding(boolean useUTF8) {
		this.utf8 = useUTF8;
	}
	
	/**
	 * Hides all text fragments (and bullets) of the formatted text.
	 */
	public void hide() {
		for (Primitive prim : fragmentList) prim.hide();
		for (Primitive prim : itemList) prim.hide();
	}
	
	/**
	 * Hides all text fragments (and bullets) of the formatted text.
	 * @param delay - The delay before hiding.
	 */
	public void hide(Timing delay) {
		for (Primitive prim : fragmentList) prim.hide(delay);
		for (Primitive prim : itemList) prim.hide(delay);
	}
	
	/**
	 * Shows all text fragments (and bullets) of the formatted text.
	 */
	public void show() {
		for (Primitive prim : fragmentList) prim.show();
		for (Primitive prim : itemList) prim.show();
	}
	
	/**
	 * Shows all text fragments (and bullets) of the formatted text.
	 * @param delay - The delay before showing.
	 */
	public void show(Timing delay) {
		for (Primitive prim : fragmentList) prim.show(delay);
		for (Primitive prim : itemList) prim.show(delay);
	}
	
	/**
	 * Changes the color of all text fragments (and bullets) of the formatted text.
	 * @param color - The new color.
	 * @param delay - The delay before changing color.
	 * @param duration - The duration of changing color.
	 */
	public void changeColor(Color color, Timing delay, Timing duration) {
		for (Primitive prim : fragmentList) prim.changeColor(null, color, delay, duration);
		for (Primitive prim : itemList) prim.changeColor(null, color, delay, duration);
	}
	
	/**
	 * Moves all text fragments (and bullets) of the formatted text.
	 * @param dx - The movement interval in right direction.
	 * @param dy - The movement interval down.
	 * @param delay - The delay before moving.
	 * @param duration - The duration of moving.
	 */
	public void moveBy(int dx, int dy, Timing delay, Timing duration) {
		for (Primitive prim : fragmentList) prim.moveBy(null, dx, dy, delay, duration);
		for (Primitive prim : itemList) prim.moveBy(null, dx, dy, delay, duration);
	}
	
	private String replaceUmlauts(String text) {
		return utf8 ? text : text.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss");
	}
	
	private TextProperties changeProp(final TextProperties prop, Boolean bold, Boolean italic, Boolean monospaced, Color color, Integer size) {
		TextProperties newProp = new TextProperties();
		for (String key : prop.getAllPropertyNames()) {
			newProp.set(key, prop.get(key));
		}
		Font font = (Font) this.props.getItem("font").get();
		String family = (monospaced == null) ? font.getFamily() : (monospaced ? "Monospaced" : "SansSerif");
		boolean isBold = (bold == null) ? font.isBold() : bold.booleanValue();
		boolean isItalic = (italic == null) ? font.isItalic() : italic.booleanValue();
		int fontSize = (size == null) ? font.getSize() : size.intValue();
		Color myColor = (color == null) ? (Color) props.getItem("color").get() : color;
		int style = Font.PLAIN;
		if (isBold) style += Font.BOLD;
		if (isItalic) style += Font.ITALIC;
		newProp.set("font", new Font(family, style, fontSize));
		newProp.set("color", myColor);
		return newProp;
	}
	
	private int resize(int size, double factor) {
		return (int) Math.round(size * factor);
	}
	
}
