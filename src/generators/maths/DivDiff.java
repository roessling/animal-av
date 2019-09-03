package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import java.awt.Font;

public class DivDiff implements Generator {

	// Innere Klasse Point zur besseren Behandlung von Punktangaben
	public class Point {

		private double x, y;

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public Point(double x, double y) {
			this.x = x;
			this.y = y;

		}

	}

	private int[] x_i;
	private int[] y_i;

	private Language l;
	private Text heading;
	private DecimalFormat df;
	private String schriftart;

	public void init() {
		l = new AnimalScript("DivDiff [DE]", "Jan Dillmann,Fabian Letzkus",
				800, 600);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		x_i = (int[]) primitives.get("x_i");
		y_i = (int[]) primitives.get("y_i");
		String rundung = (String) primitives.get("Number format");
		schriftart = (String) primitives.get("Font");
		
		df = new DecimalFormat(rundung);

		ArrayList<Point> set = new ArrayList<Point>();
		for (int i = 0; i < Math.max(x_i.length, y_i.length); i++) {
			set.add(new Point(x_i[i], y_i[i]));
		}
		l.setStepMode(true);

		if (createIntro(set))
			createScheme(set);

		return l.toString();
	}

	public String getName() {
		return "DivDiff [DE]";
	}

	public String getAlgorithmName() {
		return "Schema der dividierten Differenzen";
	}

	public String getAnimationAuthor() {
		return "Jan Dillmann, Fabian Letzkus";
	}

	public String getDescription() {
		return "H&auml;ufig liegen f&uuml;r einen funktionalen Zusammenhang y = f(x) nur eine begrenzte Zahl von Werten y_i = f(x_i) vor, man m&ouml;chte jedoch f(x) f&uuml;r ein beliebiges x n&auml;herungsweise berechnen, plotten etc."
				+ "\n"
				+ "Dies bezeichnet man als Interpolationsproblem, bei dem eine Ersatzfunktion als m&ouml;glichst genaue N&auml;herung zur unbekannten Funktion f(x) gesucht wird."
				+ "\n"
				+ "\n"
				+ "Ein m&ouml;glicher Ansatz daf&uuml;r ist die Newtonsche Interpolationsformel, deren Faktoren als dividierte Differenz zu den St&uuml;tzstellen x_0 ... x_n bezeichnet werden.";
	}

	public String getCodeExample() {
		return "Die Berechnung der dividierten Differenzen erfolgt dabei durch die Formel:"
				+ "\n"
				+ "\n"
				+ "[x_i]f = f(x_i)"
				+ "\n"
				+ "[x_i, ..., x_j]f = ([x_(i+1), ..., x_j]f - [x_i, ..., x_(j-1)]f) / (x_j-x_i)"
				+ "\n"
				+ "\n"
				+ "wobei y_i = f(x_i) die bekannten St&uuml;tzstellen sind.";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	// Hier folgt der Code der Berechnung
	private boolean createIntro(final List<Point> p) {
		this.heading = l.newText(new Coordinates(10, 10), getName(),
				"einleitung0", null);
		// this.heading.getProperties().set("font", new Font(Font.SANSSERIF));
		this.heading.setFont(new Font("SansSerif", Font.BOLD, 16), null, null); // .set("size",
																				// 16);
		l.newText(
				new Coordinates(10, 50),
				"In dieser Animation soll die Interpolation einer Funktion über die",
				"einleitung1", null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);
		l.newText(
				new Offset(0, 25, "einleitung1", AnimalScript.DIRECTION_NW),
				"Newton Basis gezeigt werden. Die Schwierigkeit liegt hierbei in der",
				"einleitung2", null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);
		l.newText(new Offset(0, 25, "einleitung2", AnimalScript.DIRECTION_NW),
				"Berechnung der dividierten Differenzen.", "einleitung3", null)
				.setFont(new Font(schriftart, Font.PLAIN, 16), null, null);
		l.newText(
				new Offset(0, 25, "einleitung3", AnimalScript.DIRECTION_NW),
				"Folgende " + p.size()
						+ " Stützstellen der gesuchten Funktion sind bekannt:",
				"einleitung4", null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);

		// Tabelle zeichnen
		int twidth = (p.size() + 1) * 50;

		l.newPolyline(new Coordinates[] { new Coordinates(5, 198),
				new Coordinates(5 + twidth, 198) }, "table_einleitung_h", null);
		l.newPolyline(new Coordinates[] { new Coordinates(30, 170),
				new Coordinates(30, 245) }, "table_einleitung_v", null);

		String lineI = "i      ", lineX = "x     ", lineY = "y     ";
		Iterator<Point> it = p.iterator();
		Point po = null;
		int i = 0;
		ArrayList<Double> checkForDoubleX = new ArrayList<Double>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		while (it.hasNext()) {
			po = it.next();
			if (checkForDoubleX.contains(po.getX())) {
				errorMessages.add("Es wurden 2 gleiche x-Werte gefunden.");
				errorMessages
						.add("Da die x-Werte innerhalb der Berechnung im Nenner"
								+ " von einander abgezogen werden müssen alle x-Werte unterschiedlich groß sein.");
				errorMessages
						.add("Eine Animation ist mit diesen Werten daher nicht möglich.");
				break;
			} else {
				checkForDoubleX.add(po.getX());
			}
			lineI = lineI.concat(i + "       ");
			lineX = lineX.concat(po.getX() + "    ");
			lineY = lineY.concat(po.getY() + "    ");
			i++;
		}
		lineY = lineY.concat(" = f(x)");

		if (errorMessages.size() != 0) {
			i = 0;
			l.addLine("hide \"table_einleitung_h\" \"table_einleitung_v\"");
			for (String msg : errorMessages) {
				l.newText(new Coordinates(10, 175 + 25 * i), "Fehler: " + msg,
						"fehler_" + i, null);
				i++;
			}
			return false;
		}

		l.newText(new Coordinates(10, 175), lineI, "einleitung5", null);
		l.newText(new Coordinates(10, 200), lineX, "einleitung6", null);
		l.newText(new Coordinates(10, 225), lineY, "einleitung7", null);

		//
		l.newText(
				new Coordinates(10, 300),
				"Die Berechnung der dividierten Differenzen erfolgt dabei durch folgende Formel",
				"einleitung8", null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);
		l.newText(new Offset(0, 25, "einleitung8", AnimalScript.DIRECTION_NW),
				"[xi]f = f(xi)", "einleitung9", null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);
		l.newText(
				new Offset(0, 25, "einleitung9", AnimalScript.DIRECTION_NW),
				"[xi, ..., xj]f = ([xi+1, ..., xj]f - [xi, ..., xj-1]f) / (xj-xi)",
				"einleitung10", null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);
		l.newText(
				new Offset(0, 25, "einleitung10", AnimalScript.DIRECTION_NW),
				"Anmerkung: Bei unterschiedlichen Arraygrößen für x_i und y_i wurde die kleinste Anzahl an möglichen Punkten angenommen!",
				"einleitung11", null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);
		l.nextStep("Einleitung");
		return true;
	}

	private void createScheme(final List<Point> points) {
		l.hideAllPrimitives();
		this.heading.show();

		Iterator<Point> it = points.iterator();
		Point p;
		ArrayList<Double> results = new ArrayList<Double>();
		if (it.hasNext()) {
			p = it.next();
			l.newText(new Coordinates(10, 50), "x0 = " + p.getX(), "x0", null);
			l.newText(new Offset(40, 0, "x0", AnimalScript.DIRECTION_NE),
					"y0 = " + p.getY(), "x0x0", null);
			results.add(p.getY());
		}
		int i = 0;
		while (it.hasNext()) {
			p = it.next();
			l.newText(new Offset(0, 70, "x" + i, AnimalScript.DIRECTION_NW),
					"x" + (i + 1) + " = " + p.getX(), "x" + (i + 1), null);
			l.newText(new Offset(0, 70, "x0x" + i, AnimalScript.DIRECTION_NW),
					"y" + (i + 1) + " = " + p.getY(), "x0x" + (i + 1), null);
			results.add(p.getY());
			i++;
		}

		int lineLength = 70 * (points.size() - 1);
		l.newPolyline(new Offset[] {
				new Offset(20, 0, "x0", AnimalScript.DIRECTION_NE),
				new Offset(20, lineLength, "x0", AnimalScript.DIRECTION_SE) },
				"seperator", null);

		l.newText(new Offset(0, 50, "x" + i, AnimalScript.DIRECTION_W),
				"[xi]f = f(xi)", "formulaA", null);
		l.newText(
				new Offset(0, 25, "formulaA", AnimalScript.DIRECTION_W),
				"[xi, ..., xj]f = ([xi+1, ..., xj]f - [xi, ..., xj-1]f) / (xj-xi)",
				"formulaB", null);

		// Berechnungen durchführen
		// Algorithmus siehe
		// http://www.emg.tu-bs.de/pdf/DMM/DMM08_Ausgleichsrechnung.pdf
		// Ergebnis: Matrix m mit folgenden Einträgen
		// k
		// |
		// j--y0
		// y1 x0x1
		// y2 x1x2 x0x2
		// y3 x2x3 x1x3 x0x3
		// ....
		double[][] m = new double[points.size()][points.size()];
		double[][] n = new double[points.size()][points.size()]; // m^T zur
																	// besseren
																	// Verarbeitung
																	// des
																	// Ergebnisses!
		for (int j = 0; j < points.size(); j++) { // Zeile
			for (int k = 0; k <= j; k++) { // Spalte
				if (k == 0) {
					m[j][k] = points.get(j).getY();
				} else {
					m[j][k] = (m[j][k - 1] - m[j - 1][k - 1])
							/ (points.get(j).getX() - points.get(j - k).getX());
				}
				n[k][j] = m[j][k];
			}
		}

		// Ergebnisse visualisieren
		/*
		 * i: #Punkte-1 Example: color "x0" "x1" "y0" "y1" blue
		 * 
		 * text "x0x1" "-2" offset (70,35) from "y0" NE color black font
		 * Monospaced size 14
		 * 
		 * line "a01" offset ( 10 , 2 ) from "y0" E offset ( -10 , -2 ) from
		 * "x0x1" W color black fwArrow line "a10" offset ( 10 , -2 ) from "y1"
		 * E offset ( -10 , 2 ) from "x0x1" W color black fwArrow
		 * 
		 * text "formel0"
		 * "[x0,x1]f = ([x1]f - [x0]f)/(x1-x0) = (0-1)/(0.5-0) = -2" at (10,425)
		 * color blue font Monospaced 14
		 */
		String baseNode = "x0x0";
		Offset o = new Offset(0, 25, "formulaB", AnimalScript.DIRECTION_W);
		int fc = 0;
		for (int j = 1; j < n.length; j++) {

			// Erster Teil
			l.newText(new Offset(70, 35, baseNode, AnimalScript.DIRECTION_NE),
					df.format(n[j][j]), "x" + j + "x1", null);
			l.newPolyline(new Offset[] {
					new Offset(10, 2, baseNode, AnimalScript.DIRECTION_E),
					new Offset(-10, -2, "x" + j + "x1",
							AnimalScript.DIRECTION_W) }, "a" + j + "a1", null);
			l.newPolyline(
					new Offset[] {
							new Offset(10, -2, "x" + (j - 1) + "x" + j,
									AnimalScript.DIRECTION_E),
							new Offset(-10, 2, "x" + j + "x1",
									AnimalScript.DIRECTION_W) }, "a" + 1 + "a"
							+ j, null);

			String calc = "(" + df.format(n[j - 1][j]);
			if (n[j - 1][j - 1] < 0)
				calc = calc.concat("+" + df.format((-1) * n[j - 1][j - 1])
						+ ")");
			else
				calc = calc.concat("-" + df.format(n[j - 1][j - 1]) + ")");
			calc = calc.concat("/(" + df.format(points.get(j).getX()));
			if (points.get(j - 1).getX() < 0)
				calc = calc.concat("+"
						+ df.format((-1) * points.get(j - 1).getX()) + ") = "
						+ df.format(n[j][j]));
			else
				calc = calc.concat("-" + df.format(points.get(j - 1).getX())
						+ ") = " + df.format(n[j][j]));

			l.newText(o, calc, "formula" + fc, null);
			fc++;

			baseNode = "x" + j + "x1";

			// Zweiter Teil
			for (int k = j + 1; k < n.length; k++) {
				l.nextStep();
				l.newText(
						new Offset(0, 70, baseNode, AnimalScript.DIRECTION_NW),
						df.format(n[j][k]), "x" + j + "x" + k, null);
				l.newPolyline(new Offset[] {
						new Offset(10, 2, "x" + (j - 1) + "x" + (k - 1),
								AnimalScript.DIRECTION_E),
						new Offset(-10, -2, "x" + j + "x" + k,
								AnimalScript.DIRECTION_W) }, "a" + j + "a" + k,
						null);
				l.newPolyline(new Offset[] {
						new Offset(10, -2, "x" + (j - 1) + "x" + k,
								AnimalScript.DIRECTION_E),
						new Offset(-10, 2, "x" + j + "x" + k,
								AnimalScript.DIRECTION_W) }, "a" + k + "a" + j,
						null);

				calc = "(" + df.format(n[j - 1][k]);
				if (n[j - 1][k - 1] < 0) {
					calc = calc.concat("+" + df.format((-1) * n[j - 1][k - 1])
							+ ")");
				} else {
					calc = calc.concat("-" + df.format(n[j - 1][k - 1]) + ")");
				}
				calc = calc.concat("/(" + df.format(points.get(j).getX()));
				if (points.get(j - 1).getX() < 0) {
					calc = calc.concat("+"
							+ df.format((-1) * points.get(j - 1).getX())
							+ ") = " + df.format(n[j][k]));
				} else {
					calc = calc.concat("-"
							+ df.format(points.get(j - 1).getX()) + ") = "
							+ df.format(n[j][k]));
				}
				l.newText(new Offset(0, 25, "formula" + (fc - 1),
						AnimalScript.DIRECTION_NW), calc, "formula" + fc, null);
				fc++;

				baseNode = "x" + j + "x" + k;

			}

			// Next Round..
			o = new Offset(0, 25, "formula" + (fc - 1),
					AnimalScript.DIRECTION_NW);
			baseNode = "x" + j + "x1";

			l.nextStep("Runde " + j);

		}

		// Zusammenfassung zeigen und Polynom aufbauen
		l.hideAllPrimitives();
		this.heading.show();
		l.newText(
				new Offset(0, 50, this.heading, AnimalScript.DIRECTION_W),
				"Mit dem Schema der dividierten Differenzen kann das Polynom einfach berechnet werden.",
				"berechnung1", null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);
		;
		l.newText(
				new Offset(0, 25, "berechnung1", AnimalScript.DIRECTION_W),
				"Dazu wird die Newtonsche Darstellung des Polynoms verwendet: f(x) = [x0]f + [x0,x1]f*(x-x0) + [x0,x1,x2]f*(x-x0)*(x-x1) + ...",
				"berechnung2", null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);
		;

		l.newText(new Offset(50, 50, "berechnung2", AnimalScript.DIRECTION_SW),
				"f(x)=" + n[0][0], "berechnung3", null);
		String mult = "";
		int step = 0;
		l.nextStep("Zusammenbau des Polynoms");
		for (i = 1; i < points.size(); i++) {
			if (n[i][i] != 0) {
				for (int j = 0; j <= i - 1; j++) {
					if (points.get(j).getX() < 0)
						mult += "(x+" + (-1) * points.get(j).getX() + ")";
					else if (points.get(j).getX() != 0)
						mult += "(x-" + points.get(j).getX() + ")";
					else
						mult += "x";
				}

				String element = "";
				if (n[i][i] >= 0) {
					element = "+" + df.format(n[i][i]);
				} else {
					element = df.format(n[i][i]);
				}

				l.newText(new Offset(10, 0, "berechnung" + (step + 3),
						AnimalScript.DIRECTION_NE), element + "*" + mult,
						"berechnung" + (step + 4), null);

				mult = "";
				step++;
				l.nextStep();
			}
		}

		l.newText(
				new Offset(0, 110, "berechnung2", AnimalScript.DIRECTION_W),
				"Der Aufwand beträgt O(n²).",
				"berechnung" + (i + 4), null).setFont(
				new Font(schriftart, Font.PLAIN, 16), null, null);
		;
	}
}
