package generators.cryptography.feistel;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;

/**
 * @author Moritz Kulessa <mori.k@web.de>
 * @version 1.0
 * @since 2014-07-01
 */
public class Painter {

	private Language lang;

	private TextProperties defaultTextProperties;
	private TextProperties boldTextProperties;
	private TextProperties headerProperties;
	private PolylineProperties polylineProperties;
	private SourceCodeProperties sourceCodeProperties;
	private MatrixProperties tableProperties;

	private CircleProperties circleProperties;
	private RectProperties rectProperties;
	private PolylineProperties straightlineProperties;

	private Color defaultTextColor;
	private Color defaultLineColor;
	private Color highlightedColor;

	private Text[] description;

	private Text currentBlock;

	private Text ki;
	private Text li;
	private Text ri;
	private Text li1;
	private Text ri1;
	private Text f;
	private Text currentChiff;
	private Text chiffM;

	private Polyline l_path;
	private Polyline r_path1;
	private Polyline r_path2;
	private Polyline r_path3;
	private Polyline r_path4;
	private Polyline r_path5;
	private Polyline key_path;
	private Primitive[] xor;
	private Primitive[] fBlock;

	private LinkedList<Primitive> highlightedText;
	private LinkedList<Primitive> highlightedLine;

	private SourceCode sc;

	public Painter(Language lang, AnimationPropertiesContainer props) {
		this.lang = lang;
		this.initProperties(props);
		this.highlightedText = new LinkedList<Primitive>();
		this.highlightedLine = new LinkedList<Primitive>();
		this.description = null;
	}

	private void initProperties(AnimationPropertiesContainer props) {
		highlightedColor = Color.red;
		defaultTextColor = (Color) props.getPropertiesByName("defaultText")
				.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		defaultLineColor = (Color) props.getPropertiesByName("polyline").get(
				AnimationPropertiesKeys.COLOR_PROPERTY);

		polylineProperties = (PolylineProperties) props
				.getPropertiesByName("polyline");
		polylineProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		straightlineProperties = new PolylineProperties();
		straightlineProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				defaultLineColor);
		circleProperties = new CircleProperties();
		circleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				defaultLineColor);
		rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				defaultLineColor);

		defaultTextProperties = (TextProperties) props
				.getPropertiesByName("defaultText");

		boldTextProperties = (TextProperties) props
				.getPropertiesByName("boldText");
		Font textFont = (Font) props.getPropertiesByName("boldText").get(
				AnimationPropertiesKeys.FONT_PROPERTY);
		Font boldFont = textFont.deriveFont(Font.BOLD);
		boldTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, boldFont);

		headerProperties = (TextProperties) props
				.getPropertiesByName("headlineText");
		textFont = (Font) props.getPropertiesByName("headlineText").get(
				AnimationPropertiesKeys.FONT_PROPERTY);
		boldFont = textFont.deriveFont(Font.BOLD);
		Font headFont = textFont.deriveFont((float) 20.0);
		headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, headFont);

		tableProperties = (MatrixProperties) props
				.getPropertiesByName("matrix");

		sourceCodeProperties = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");
	}

	public void paintStartScreen() {
		paintHeadline("Feistel-Chiffre: Verschlüsselung");
		paintIntroduction();
	}

	public void paintWorkScreen(String msg, String keyFunction,
			String fFunction, int numberOfRounds) {
		removeIntroduction();
		paintInitializationValues(msg, keyFunction, fFunction, numberOfRounds);
		paintVisualization();
		paintSourceCode();
		paintProtocol(numberOfRounds);
	}

	public void paintEndScreen() {
		removeAll();
		paintHeadline("Feistel-Chiffre: Entschlüsselung");
		paintOutro();
		paintDecryptVisualization();
	}

	public void removeAll() {
		lang.addLine("hideAll");
		lang.addLine("hide \"table\"");
	}

	private void paintHeadline(String name) {

		lang.newText(new Coordinates(30, 40), name, "header", null,
				headerProperties);

		RectProperties hRectProperties = new RectProperties();
		hRectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		hRectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		hRectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.LIGHT_GRAY);
		lang.newRect(new Offset(-10, -5, "header", "NW"), new Offset(10, 5,
				"header", "SE"), "hRect", null, hRectProperties);
	}

	private void paintIntroduction() {

		description = new Text[15];

		String descriptionString0 = "Die Feistel-Chiffre ist ein Chriffriersystem, bei dem der";
		String descriptionString1 = "Klartext blockweise verschlüsselt wird. Die Klartextblöcke";
		String descriptionString2 = "werden vor der Verschlüsselung erst nochmal in eine linke";
		String descriptionString3 = "und eine rechte Blockhälfte aufgeteilt. Die neu berechneten";
		String descriptionString4 = "Blockhälfte werden nach einer Runde wie folgt bestimmt:";
		String descriptionString5 = "";
		String descriptionString6 = "Li+1 = Ri";
		String descriptionString7 = "Ri+1 = F(Ri,Ki) xor Li		(F = nicht lineare Funktion)";
		String descriptionString8 = "";
		String descriptionString9 = "Dieser Algorithmus wird n Runden lang wiederholt, bis man";
		String descriptionString10 = "schließlich das Chiffrat des Blockes erhält, indem man einfach";
		String descriptionString11 = "den letzten berechneten linken Blockteil mit dem letzten";
		String descriptionString12 = "berechneten rechten Blockteil konkateniert.";
		String descriptionString13 = "Dies wird solange wiederholt, bis alle Klartextblöcke";
		String descriptionString14 = "verschlüsselt sind.";

		description[0] = lang
				.newText(new Offset(20, 40, "hRect", "SW"), descriptionString0,
						"description0", null, defaultTextProperties);
		description[1] = lang
				.newText(new Offset(0, 10, "description0", "SW"),
						descriptionString1, "description1", null,
						defaultTextProperties);
		description[2] = lang
				.newText(new Offset(0, 10, "description1", "SW"),
						descriptionString2, "description2", null,
						defaultTextProperties);
		description[3] = lang
				.newText(new Offset(0, 10, "description2", "SW"),
						descriptionString3, "description3", null,
						defaultTextProperties);
		description[4] = lang
				.newText(new Offset(0, 10, "description3", "SW"),
						descriptionString4, "description4", null,
						defaultTextProperties);
		description[5] = lang
				.newText(new Offset(0, 10, "description4", "SW"),
						descriptionString5, "description5", null,
						defaultTextProperties);
		description[6] = lang
				.newText(new Offset(0, 10, "description5", "SW"),
						descriptionString6, "description6", null,
						defaultTextProperties);
		description[7] = lang
				.newText(new Offset(0, 10, "description6", "SW"),
						descriptionString7, "description7", null,
						defaultTextProperties);
		description[8] = lang
				.newText(new Offset(0, 10, "description7", "SW"),
						descriptionString8, "description8", null,
						defaultTextProperties);
		description[9] = lang
				.newText(new Offset(0, 10, "description8", "SW"),
						descriptionString9, "description9", null,
						defaultTextProperties);
		description[10] = lang.newText(new Offset(0, 10, "description9", "SW"),
				descriptionString10, "description10", null,
				defaultTextProperties);
		description[11] = lang.newText(
				new Offset(0, 10, "description10", "SW"), descriptionString11,
				"description11", null, defaultTextProperties);
		description[12] = lang.newText(
				new Offset(0, 10, "description11", "SW"), descriptionString12,
				"description12", null, defaultTextProperties);
		description[13] = lang.newText(
				new Offset(0, 10, "description12", "SW"), descriptionString13,
				"description13", null, defaultTextProperties);
		description[14] = lang.newText(
				new Offset(0, 10, "description13", "SW"), descriptionString14,
				"description14", null, defaultTextProperties);
	}

	private void paintOutro() {
		String descriptionString0 = "Um Feistel-Chiffren wieder zu entschlüsseln, wendet";
		String descriptionString1 = "man einfach die Umkehrung der Formel zur Verschlüsselung";
		String descriptionString2 = "an:";
		String descriptionString3 = "";
		String descriptionString4 = "Ri = Li+1";
		String descriptionString5 = "Li = Ri+1 xor F(Li+1,Ki)";
		String descriptionString6 = "";
		String descriptionString7 = "Wie man sieht, muss für die Funktion F keine Umkehrfunktion";
		String descriptionString8 = "gebildet werden.";

		lang.newText(new Offset(20, 40, "hRect", "SW"), descriptionString0,
				"description0", null, defaultTextProperties);
		lang.newText(new Offset(0, 10, "description0", "SW"),
				descriptionString1, "description1", null, defaultTextProperties);
		lang.newText(new Offset(0, 10, "description1", "SW"),
				descriptionString2, "description2", null, defaultTextProperties);
		lang.newText(new Offset(0, 10, "description2", "SW"),
				descriptionString3, "description3", null, defaultTextProperties);
		lang.newText(new Offset(0, 10, "description3", "SW"),
				descriptionString4, "description4", null, defaultTextProperties);
		lang.newText(new Offset(0, 10, "description4", "SW"),
				descriptionString5, "description5", null, defaultTextProperties);
		lang.newText(new Offset(0, 10, "description5", "SW"),
				descriptionString6, "description6", null, defaultTextProperties);
		lang.newText(new Offset(0, 10, "description6", "SW"),
				descriptionString7, "description7", null, defaultTextProperties);
		lang.newText(new Offset(0, 10, "description7", "SW"),
				descriptionString8, "description8", null, defaultTextProperties);

	}

	private void removeIntroduction() {
		if (description != null) {
			for (Text desc : description) {
				desc.hide();
			}
		}
	}

	private void paintInitializationValues(String msg, String keyFunction,
			String fFunction, int numberOfRounds) {
		lang.newText(new Offset(0, 15, "hRect", "SW"), "Nachricht:",
				"msg_label", null, boldTextProperties);
		String m = "m = " + msg;
		lang.newText(new Offset(0, 0, "msg_label", "SW"), m, "m", null,
				defaultTextProperties);

		lang.newText(new Offset(0, 5, "m", "SW"), "Anzahl der Runden:",
				"rounds_label", null, boldTextProperties);
		String n = "n = " + numberOfRounds;
		lang.newText(new Offset(0, 0, "rounds_label", "SW"), n, "n", null,
				defaultTextProperties);

		lang.newText(new Offset(0, 5, "n", "SW"), "Verschlüsselungsfunktion:",
				"fFunction_label", null, boldTextProperties);
		String f = "f(r,k) = " + fFunction;
		lang.newText(new Offset(0, 0, "fFunction_label", "SW"), f, "f(x)",
				null, defaultTextProperties);

		lang.newText(new Offset(0, 5, "f(x)", "SW"), "Schlüsselfunktion:",
				"keyFunction_label", null, boldTextProperties);
		String k = "k(k) = " + keyFunction;
		lang.newText(new Offset(0, 0, "keyFunction_label", "SW"), k, "k(x)",
				null, defaultTextProperties);

	}

	private void paintDecryptVisualization() {

		lang.newText(new Offset(250, 20, "hRect", "SE"), "Li+1", "li+1", null,
				defaultTextProperties);
		lang.newText(new Offset(170, 0, "li+1", "Nw"), "Ri+1", "ri+1", null,
				defaultTextProperties);

		// Li+1 path
		lang.newPolyline(new Node[] { new Offset(10, 25, "li+1", "SW"),
				new OffsetFromLastPosition(0, 100) }, "l+1_path1", null,
				polylineProperties);
		lang.newPolyline(new Node[] { new Offset(0, 50, "l+1_path1", "SW"),
				new OffsetFromLastPosition(0, 90) }, "l+1_path2", null,
				polylineProperties);
		lang.newPolyline(new Node[] { new Offset(0, 10, "l+1_path2", "SW"),
				new OffsetFromLastPosition(0, 100) }, "l+1_path3", null,
				polylineProperties);
		lang.newPolyline(new Node[] { new Offset(0, 50, "l+1_path1", "NW"),
				new OffsetFromLastPosition(85, 0),
				new OffsetFromLastPosition(0, 250),
				new OffsetFromLastPosition(85, 0),
				new OffsetFromLastPosition(0, 50) }, "l+1_path4", null,
				polylineProperties);

		// Ri+1 path
		lang.newPolyline(new Node[] { new Offset(10, 25, "ri+1", "SW"),
				new OffsetFromLastPosition(0, 250),
				new OffsetFromLastPosition(-160, 0) }, "ri+1_path", null,
				polylineProperties);

		lang.newText(new Offset(-10, 25, "l+1_path4", "SE"), "Ri", "ri", null,
				defaultTextProperties);
		lang.newText(new Offset(-10, 25, "l+1_path3", "SE"), "Li", "li", null,
				defaultTextProperties);

		// f block
		lang.newRect(new Offset(-25, 0, "l+1_path1", "SW"), new Offset(25, 50,
				"l+1_path1", "SW"), "new_f_block", null);
		lang.newText(new Offset(15, 15, "new_f_block", "NW"), "F(x)",
				"new_f_label", null, defaultTextProperties);

		// xor
		lang.newCircle(new Offset(0, 10, "l+1_path2", "SW"), 10, "new_xor_0",
				null, circleProperties);
		lang.newPolyline(new Node[] { new Offset(10, 0, "new_xor_0", "NW"),
				new Offset(10, 20, "new_xor_0", "NW") }, "new_xor_1", null,
				straightlineProperties);
		lang.newPolyline(new Node[] { new Offset(-10, 10, "new_xor_1", "NW"),
				new Offset(10, 10, "new_xor_1", "NW") }, "new_xor_2", null,
				straightlineProperties);

		// key path
		lang.newPolyline(new Node[] { new Offset(-75, 25, "new_f_block", "NW"),
				new OffsetFromLastPosition(75, 0), }, "new_key_path", null,
				polylineProperties);
		lang.newText(new Offset(25, -25, "new_key_path", "NW"), "Ki",
				"ki_label", null, defaultTextProperties);

	}

	private void paintVisualization() {
		l_path = lang.newPolyline(new Node[] {
				new Offset(50, 40, "k(x)", "SW"),
				new OffsetFromLastPosition(0, 250),
				new OffsetFromLastPosition(160, 0) }, "l_path", null,
				polylineProperties);

		r_path5 = lang.newPolyline(new Node[] {
				new Offset(220, 40, "k(x)", "SW"),
				new OffsetFromLastPosition(0, 50) }, "r_path5", null,
				straightlineProperties);

		r_path1 = lang.newPolyline(new Node[] {
				new Offset(0, 0, "r_path5", "SW"),
				new OffsetFromLastPosition(0, 50) }, "r_path1", null,
				polylineProperties);

		r_path2 = lang.newPolyline(new Node[] {
				new Offset(0, 50, "r_path1", "SW"),
				new OffsetFromLastPosition(0, 90) }, "r_path2", null,
				polylineProperties);

		r_path4 = lang.newPolyline(new Node[] {
				new Offset(0, 10, "r_path2", "SW"),
				new OffsetFromLastPosition(0, 100) }, "r_path4", null,
				polylineProperties);

		r_path3 = lang.newPolyline(new Node[] {
				new Offset(0, -50, "r_path1", "SW"),
				new OffsetFromLastPosition(-85, 0),
				new OffsetFromLastPosition(0, 250),
				new OffsetFromLastPosition(-85, 0),
				new OffsetFromLastPosition(0, 50) }, "r_path3", null,
				polylineProperties);

		// f(x) Block
		fBlock = new Primitive[2];

		fBlock[0] = lang.newRect(new Offset(-25, 0, "r_path1", "SW"),
				new Offset(25, 50, "r_path1", "SW"), "f_block", null);
		fBlock[1] = lang.newText(new Offset(15, 15, "f_block", "NW"), "F(x)",
				"f_label", null, defaultTextProperties);

		// key line
		key_path = lang.newPolyline(new Node[] {
				new Offset(75, 25, "f_block", "NE"),
				new OffsetFromLastPosition(-75, 0), }, "key_path", null,
				polylineProperties);

		// XOR
		xor = new Primitive[3];

		xor[0] = lang.newCircle(new Offset(0, 10, "r_path2", "SW"), 10,
				"xor_0", null, circleProperties);
		xor[1] = lang.newPolyline(new Node[] {
				new Offset(10, 0, "xor_0", "NW"),
				new Offset(10, 20, "xor_0", "NW") }, "xor_1", null,
				straightlineProperties);
		xor[2] = lang.newPolyline(new Node[] {
				new Offset(-10, 10, "xor_1", "NW"),
				new Offset(10, 10, "xor_1", "NW") }, "xor_2", null,
				straightlineProperties);

		// BEschriftung
		ki = lang.newText(new Offset(25, -25, "key_path", "NW"), "K0 = ",
				"ki_label", null, defaultTextProperties);

		li = lang.newText(new Offset(-50, -25, "l_path", "NW"), "L0 = ",
				"li_label", null, defaultTextProperties);

		ri = lang.newText(new Offset(-50, -25, "r_path5", "NW"), "R0 = ",
				"ri_label", null, defaultTextProperties);

		li1 = lang.newText(new Offset(-50, 15, "r_path3", "SW"), "L1 = ",
				"li+1_label", null, defaultTextProperties);

		ri1 = lang.newText(new Offset(-50, 15, "r_path4", "SW"), "R1 = ",
				"ri+1_label", null, defaultTextProperties);

		f = lang.newText(new Offset(-10, 15, "f_block", "SE"), "",
				"after_f_label", null, defaultTextProperties);

		// Trennlinie
		lang.newPolyline(new Node[] { new Offset(110, 10, "hRect", "SE"),
				new OffsetFromLastPosition(0, 595), }, "line", null,
				straightlineProperties);
	}

	private void paintSourceCode() {
		lang.newText(new Offset(20, 5, "line", "NE"), "Algorithmus:",
				"algorithm", null, boldTextProperties);
		sc = lang.newSourceCode(new Offset(30, 0, "algorithm", "SW"), "code",
				null, sourceCodeProperties);

		sc.addCodeLine("for each block do", null, 0, null);
		sc.addCodeLine("L0 = linker Blockteil;", null, 1, null);
		sc.addCodeLine("R0 = rechter Blockteil;", null, 1, null);
		sc.addCodeLine("for i = 0,...,n do", null, 1, null);
		sc.addCodeLine("Ki = (i==0) ? K0 : k(Ki);", null, 2, null);
		sc.addCodeLine("Li = Ri;", null, 2, null);
		sc.addCodeLine("Ri = f(Ri, ki) xor Li;", null, 2, null);
		sc.addCodeLine("end", null, 1, null);
		sc.addCodeLine("cj = Li ° Ri;", null, 1, null);
		sc.addCodeLine("end", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("c = c1 ° c2 ° ... ° cj; (° = Konkatenation)", null, 0,
				null);

	}

	private void paintProtocol(int numberOfRounds) {

		Color tableCellHighlightColor = (Color) tableProperties
				.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
		String tableTextFont = ((Font) tableProperties
				.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName();

		Color tableTextColor = (Color) tableProperties
				.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		Color tableFillColor = (Color) tableProperties
				.get(AnimationPropertiesKeys.FILL_PROPERTY);

		// Protocol table
		lang.newText(new Offset(-30, 20, "code", "SW"), "Protokoll:",
				"protocol", null, boldTextProperties);

		currentBlock = lang
				.newText(new Offset(0, 10, "protocol", "SW"),
						"Momentaner Block = ", "current_m", null,
						defaultTextProperties);

		String tableDecl = "grid \"table\" offset (0,10) from \"current_m\" SW lines "
				+ (numberOfRounds + 2)
				+ " columns 3 style table cellWidth 125 cellHeight 20 borderColor white ";
		tableDecl += "highlightFillColor (" + tableCellHighlightColor.getRed()
				+ ", " + tableCellHighlightColor.getGreen() + ", "
				+ tableCellHighlightColor.getBlue() + ") ";
		tableDecl += "font " + tableTextFont + " size 12";
		lang.addLine(tableDecl);
		String tableInit = "setGridColor \"table[][]\" color white ";
		tableInit += "textColor (" + tableTextColor.getRed() + ", "
				+ tableTextColor.getGreen() + ", " + tableTextColor.getBlue()
				+ ") ";
		tableInit += "fillColor (" + tableFillColor.getRed() + ", "
				+ tableFillColor.getGreen() + ", " + tableFillColor.getBlue()
				+ ")";
		lang.addLine(tableInit);
		lang.addLine("setGridValue \"table[][]\" \"\"");
		lang.addLine("setGridValue \"table[0][0]\" \"Runde i\"");
		lang.addLine("setGridValue \"table[0][1]\" \"Li\"");
		lang.addLine("setGridValue \"table[0][2]\" \"Ri\"");

		for (int i = 0; i < (numberOfRounds + 1); i++) {
			lang.addLine("setGridValue \"table[" + (i + 1) + "][0]\" \"" + i
					+ "\"");
		}

		currentChiff = lang.newText(new Offset(0, 20, "table", "SW"),
				"Chiffrat vom momentanen Block = ", "current_chiff", null,
				defaultTextProperties);

		chiffM = lang.newText(new Offset(0, 20, "current_chiff", "SW"),
				"Chiffrat von m = ", "chiff_m", null, defaultTextProperties);

		// Trennlinie gegebenfalls um weitere linie erweitern
		lang.newPolyline(new Node[] { new Offset(-20, 00, "chiff_m", "SW"),
				new Offset(110, 605, "hRect", "SE") }, "line2", null,
				straightlineProperties);

	}

	public void setKi(int round, String value) {
		ki.setText("K" + round + " = " + value, null, null);
		highlight(ki, true);
	}

	public void setF(int round, String value) {
		f.setText(value, null, null);
		highlight(f, true);
	}

	public void setLi(int round, String value) {
		li.setText("L" + round + " = " + value, null, null);
		highlight(li, true);
	}

	public void setRi(int round, String value) {
		ri.setText("R" + round + " = " + value, null, null);
		highlight(ri, true);
	}

	public void setLi1(int round, String value) {
		li1.setText("L" + round + " = " + value, null, null);
		highlight(li1, true);
	}

	public void setRi1(int round, String value) {
		ri1.setText("R" + round + " = " + value, null, null);
		highlight(ri1, true);
	}

	public void setCurrentChiff(String value) {
		currentChiff.setText("Chiffrat vom momentanen Block = " + value, null,
				null);
		highlight(currentChiff, true);
	}

	public void setChiffM(String value) {
		chiffM.setText("Chiffrat von m = " + value, null, null);
		highlight(chiffM, true);
	}

	public void setCurrentBlock(String value) {
		currentBlock.setText("Momentaner Block = " + value, null, null);
		highlight(currentBlock, true);
	}

	public void removeValues(int round) {
		li.setText("L" + round + " = ", null, null);
		ri.setText("R" + round + " = ", null, null);
		li1.setText("L" + (round + 1) + " = ", null, null);
		ri1.setText("R" + (round + 1) + " = ", null, null);
		ki.setText("K" + round + " = ", null, null);
		f.setText("", null, null);
	}

	public void highlightPathForF() {
		highlight(key_path, false);
		highlight(r_path1, false);
		highlight(r_path5, false);
	}

	public void highlightPathForXor() {
		highlight(l_path, false);
		highlight(r_path2, false);
	}

	public void highlightPathForResultR() {
		highlight(r_path4, false);
	}

	public void highlightPathForResultL() {
		highlight(r_path5, false);
		highlight(r_path3, false);
	}

	public void highlight(Primitive p, boolean isText) {
		p.changeColor("color", highlightedColor, null, null);
		if (isText) {
			highlightedText.add(p);
		} else {
			highlightedLine.add(p);
		}
	}

	public void removeHighlight() {
		removeHighlightLine();
		removeHighlightText();
	}

	public void removeHighlightLine() {
		while (!highlightedLine.isEmpty()) {
			Primitive p = highlightedLine.pop();
			p.changeColor("color", defaultLineColor, null, null);
		}
	}

	public void removeHighlightText() {
		while (!highlightedText.isEmpty()) {
			Primitive p = highlightedText.pop();
			p.changeColor("color", defaultTextColor, null, null);
		}
	}

	public void highlightF() {
		highlight(fBlock[0], false);
		highlight(fBlock[1], true);
	}

	public void highlightXor() {
		for (int i = 0; i < xor.length; i++) {
			highlight(xor[i], false);
		}
	}

	public void highlightSourceCode(int i) {
		sc.highlight(i);
	}

	public void unHighlightSourceCode(int i) {
		sc.unhighlight(i);
	}

	public void highlightSourceCode(int i, int j) {
		sc.toggleHighlight(i, j);
	}

	public void addProtocolL(int round, String value) {
		lang.addLine("highlightGridCell \"table[" + (round + 1) + "][1]\"");
		lang.addLine("setGridValue \"table[" + (round + 1) + "][1]\" \""
				+ value + "\"");
	}

	public void addProtocolR(int round, String value) {
		lang.addLine("highlightGridCell \"table[" + (round + 1) + "][2]\"");
		lang.addLine("setGridValue \"table[" + (round + 1) + "][2]\" \""
				+ value + "\"");
	}

	public void unhighlightProtocol(int round) {
		lang.addLine("unhighlightGridCell \"table[" + (round + 1) + "][1]\"");
		lang.addLine("unhighlightGridCell \"table[" + (round + 1) + "][2]\"");
	}

	public void removeValuesProtocol(int numberOfRounds) {
		for (int i = 0; i < (numberOfRounds + 1); i++) {
			lang.addLine("setGridValue \"table[" + (i + 1) + "][1]\" \"\"");
			lang.addLine("setGridValue \"table[" + (i + 1) + "][2]\" \"\"");
		}
	}

	public void removeTextCurrentChiff() {
		currentChiff.setText("Chiffrat vom momentanen Block = ", null, null);

	}

}
