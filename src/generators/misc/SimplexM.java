/*
 * SimplexM.java
 * Florian Klotzsch, Neda Mesbah, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class SimplexM implements Generator {
	private Language lang;
	private int[] ergebnisseNebenbedingungen;
	private TextProperties titelEigenschaften;
	private String[] vergleichoperatoren;
	private int[] zielfunktion;
	private MatrixProperties matrixEigenschaften;
	private int[][] nebenbedingungen;
	private ArrayMarkerProperties pfeileEigenschaften;
	private SourceCodeProperties textEigenschaften;

	private double[] f;
	private double[][] nb;
	private String[] vergleich;
	private double[] b;
	private double[][] schlupfvariablen;
	private int[] zulaessigeBasisloesung;
	private double[][] simplexTableau;
	private int pivotspalte;
	private int pivotzeile;
	private double[][] mTableau;
	private double[][] kuenstlicheVariablen;
	private int anzahlKuenstlicheVariablen;
	private double[] m;
	private SourceCode system;
	private StringMatrix tableau;
	private StringMatrix matrixRechnung;
	private SourceCode vorgehen;
	private ArrayMarker am;
	private ArrayPrimitive arPrim;
	private SourceCodeProperties scProps;
	private TextProperties subtitleProps;
	private Text title;
	private SourceCode hilfetext;
	private SourceCode normal;
	private int anzahldurchläufe;

	public void init() {
		lang = new AnimalScript(
				"Primaler Simplex-Algorithmus mit der Erweiterung der M-Methode [DE]",
				"Florian Klotzsch, Neda Mesbah", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		ergebnisseNebenbedingungen = (int[]) primitives
				.get("ergebnisseNebenbedingungen");
		titelEigenschaften = (TextProperties) props
				.getPropertiesByName("titelEigenschaften");
		vergleichoperatoren = (String[]) primitives.get("vergleichoperatoren");
		zielfunktion = (int[]) primitives.get("zielfunktion");
		matrixEigenschaften = (MatrixProperties) props
				.getPropertiesByName("matrixEigenschaften");
		nebenbedingungen = (int[][]) primitives.get("nebenbedingungen");
		pfeileEigenschaften = (ArrayMarkerProperties) props
				.getPropertiesByName("pfeileEigenschaften");
		textEigenschaften = (SourceCodeProperties) props
				.getPropertiesByName("textEigenschaften");

		b = new double[ergebnisseNebenbedingungen.length];
		f = new double[zielfunktion.length];
		vergleich = new String[vergleichoperatoren.length];
		nb = new double[nebenbedingungen[0].length][nebenbedingungen.length];

		for (int i = 0; i < ergebnisseNebenbedingungen.length; i++) {
			b[i] = (double) ergebnisseNebenbedingungen[i];
		}

		for (int i = 0; i < zielfunktion.length; i++) {
			f[i] = (double) zielfunktion[i];
		}

		for (int i = 0; i < nebenbedingungen.length; i++) {
			for (int j = 0; j < nebenbedingungen[0].length; j++) {
				nb[j][i] = (double) nebenbedingungen[i][j];
			}
		}

		try {
			simplex();
		} catch (Exception e) {
		}

		return lang.toString();
	}

	public String getName() {
		return "Primaler Simplex-Algorithmus mit der Erweiterung der M-Methode [DE]";
	}

	public String getAlgorithmName() {
		return "Primaler Simplex mit M-Methode";
	}

	public String getAnimationAuthor() {
		return "Florian Klotzsch, Neda Mesbah";
	}

	public String getDescription() {
		return "Der Simplex-Algorihmus ist ein Optimierungsalgorithmus für die Lösung linearer"
				+ "\n"
				+ "Gleichungssysteme. Dieser Algorithmus wird beispielweise in der Produktionsplanung"
				+ "\n"
				+ "angewendet. Für ein bestimmtes Maximierungsproblem wird dabei, unter Angabe"
				+ "\n"
				+ "von Nebenbedingungen (in Form von Gleichungen und Ungleichungen), iterativ die"
				+ "\n"
				+ "optimale Lösung bestimmt."
				+ "\n"
				+ "(Die Minimierung eines Funktionswertes wird mit Hilfe des Dualen-Simplex gelöst,"
				+ "\n"
				+ "der hier jedoch nicht behandelt wird.)"
				+ "\n"
				+ "\n"
				+ "Zur Lösung des Optimierungsproblems muss eine zulässige Basislösung"
				+ "\n"
				+ "für das lineare Gleichungssystem bekannt sein. Diese Einschränkung kann durch"
				+ "\n"
				+ "die M-Methode umgangen werden. Die M-Methode berechnet irgendeine zulässige"
				+ "\n"
				+ "Basislösung. Mit Hilfe dieser kann man letztlich den primalen Simplex anwenden."
				+ "\n"
				+ "\n"
				+ "Input:"
				+ "\n"
				+ "Zum Durchführen des Simplex-Algorithmus müssen die Nebenbedingungen sowie die Zielfunktion angegeben werden."
				+ "\n"
				+ "Diese werden durch folgende Primitive übergeben:"
				+ "\n"
				+ "\n"
				+ "nebenbedingungen: Hier stehen die Koeffizienten der Variablen in den Nebenbedingungen."
				+ "\n"
				+ "In der ersten Spalte stehen die Koeffizienten für x_1, in der zweiten für x_2. usw."
				+ "\n"
				+ "\n"
				+ "ergebnisseNebenbedingungen: Hier stehen die Ergebnisse der Nebenbedingungen, "
				+ "\n"
				+ "beginnend ab dem Ergebnis der ersten Nebenbedingung"
				+ "\n"
				+ "\n"
				+ "vergleichsoperatoren: Hier werden die Vergleichsoperatoren der Nebenbedingungen angegeben."
				+ "\n"
				+ "Zur Auswahl stehen dabei groesser (für >=), gleich (für =) und kleiner (für kleinergleich Zeichen)."
				+ "\n"
				+ "Es ist wichtig, dass die hier angegebenen Schreibweisen eingehalten werden,"
				+ "\n"
				+ "sonst kann der Algorithmus nicht ausgeführt werden."
				+ "\n"
				+ "\n"
				+ "zielfunktion: Hier stehen die Koeffizienten der Variablen in der Zielfunktion."
				+ "\n"
				+ "\n"
				+ "Die voreingestellten Werte würden bspw. zu folgender Zielfunktion und zugehörigen Nebenbedingungen führen:"
				+ "\n"
				+ "\n"
				+ "Zielfunktion:"
				+ "\n"
				+ "F = 10x_1 + 20x_2"
				+ "\n"
				+ "Nebenbedingungen:"
				+ "\n"
				+ "I.   1x_1 + 1x_2 kleinergleich 100"
				+ "\n"
				+ "II.  6x_1 + 9x_2 kleinergleich 720"
				+ "\n"
				+ "III. 0x_1 + 1x_2 kleinergleich 60";
	}

	public String getCodeExample() {
		return "Vorgehensweise des Simplex-Verfahrens:"
				+ "\n"
				+ "\n"
				+ "1. Simplex-Tableau aufstellen (siehe unten)"
				+ "\n"
				+ "2. Pivot Element bestimmen"
				+ "\n"
				+ "	I. Wahl der Pivot-Spalte"
				+ "\n"
				+ "		a. Keine negativen Eintragungen in der F-Zeile: aktuelle Basislösung ist optimal"
				+ "\n"
				+ "			--> Ende des Verfahrens (Schnittpunkt aus F-Zeile und b-Spalte entspricht optimalem Zielwert)"
				+ "\n"
				+ "		b. Negative Eintragungen in der F-Zeile: Auswahl der Variable (entspricht Pivot-Spalte) mit der kleinsten Eintragung "
				+ "\n"
				+ "	II. Wahl der Pivot-Zeile"
				+ "\n"
				+ "		a. Alle Koeffizienten in der Pivot Spalte sind kleiner oder gleich 0: unbeschränktes Modell (keine optimale Lösung)"
				+ "\n"
				+ "			--> Abbruch des Verfahrens"
				+ "\n"
				+ "		b. Ansonsten wähle die Zeile als Pivotzeile, mit dem kleinsten Quotienten aus dem Wert der b-Spalte und dem Wert aus"
				+ "\n"
				+ "		    der Pivot-Spalte (muss größer als 0 sind)"
				+ "\n"
				+ "	III. Pivotspalte und Pivotzeile ergeben das Pivotelement"
				+ "\n"
				+ "3. Berechnung der neuen Basislösung, Transformtion des Tableaus"
				+ "\n"
				+ "	I. Tausche die bisherige Basisvariable in der Pivot-Zeile durch die Variable der Pivot-Spalte"
				+ "\n"
				+ "	II. Schaffe unter der neuen Basisvariable einen Einheitsvektor durch Anwenden linearer Transformationen"
				+ "\n"
				+ "4. Fahre fort mit 2. Schritt"
				+ "\n"
				+ "\n"
				+ "Vorgehensweise zur Überführung des Optimierungsproblems in das Simplex-Tableau:"
				+ "\n"
				+ "\n"
				+ "1. Überführen des Systems in Normalform"
				+ "\n"
				+ "	I. Ungleichungen (in den Nebenbedingungen) werden durch Hinzufügen einer zusätzlichen Variable (Schlupfvariable) in Gleichungen überführt"
				+ "\n"
				+ "2. Ermitteln einer zulässigen Basislösung"
				+ "\n"
				+ "	I. Wenn diese nicht durch die Einheitsmartix unter den Schlupfvariablen gegeben ist"
				+ "\n"
				+ "		--> Durchführen der M-Methode (siehe unten)"
				+ "\n"
				+ "3. Überführen in die kanonische Form --> Überführung des Systems in Matrixschreibweise"
				+ "\n"
				+ "	I. Normalform muss gegeben sein"
				+ "\n"
				+ "	II. Die Ergebnisse der Nebenbedingungen müssen positiv sein"
				+ "\n"
				+ "		--> falls nicht gegeben: Durchführen der M-Methode"
				+ "\n"
				+ "	III. In der Matrix der Nebenbedingungen muss eine Einheitsmatrix enthalten sein"
				+ "\n"
				+ "	IV. Matrix der Nebenbedingungen enthält für jede Variable (inkl. Schlupfvariablen) eine Spalte"
				+ "\n"
				+ "	V. Matrix der Nebenbedingungen enthält für jede Nebenbedingung eine Zeile"
				+ "\n"
				+ "		--> entspricht auch der Anzahl an Schlupfvariablen"
				+ "\n"
				+ "4. Simplex-Tableau aufstellen"
				+ "\n"
				+ "	I. Die Werte der Matrix der Nebenbedingungen werden in das Tableau überführt"
				+ "\n"
				+ "	II. Hinzufügen einer Spalte b am rechten Ende, die die Ergebnisse der Nebenbedingungen enthält"
				+ "\n"
				+ "	III. Hinzufügen einer Spalte BV am linken Ende, die die Basisvariablen der zulässigen Basislösung enthält"
				+ "\n"
				+ "	IV. Hinzufügen einer Zeile F am unteren Ende, die die jeweiligen Koeffizienten der Variablen aus der Zielfunktion enthält"
				+ "\n"
				+ "	"
				+ "\n"
				+ "	"
				+ "\n"
				+ "Vorgehensweise der M-Methode:"
				+ "\n"
				+ "1. Gleichungen, die keine Schlupfariable enthalten oder in denen die Schlupfvariable einen negativen Koeffizienten hat, werden um künstliche Variable ergänzt"
				+ "\n"
				+ "2. Umformung der Zielfunktion"
				+ "\n"
				+ "	I. Ergänzen der Zielfunktion, um künstliche Variablen mit hohem Koeffizient M"
				+ "\n"
				+ "	II. Ersetzen der künstlichen Variablen durch entsprechende Gleichungen"
				+ "\n"
				+ "	III. Bilde zweite Zielfunktion für alle M-Terme"
				+ "\n"
				+ "3. Anwenden des primalen Simplex"
				+ "\n"
				+ "4. Entferne die künstlichen Variablen sobald sie die Basis verlassen"
				+ "\n"
				+ "	I. Zulässige Lösung erreicht, sobald alle künstlichen Variablen entfernt sind"
				+ "\n"
				+ "		--> weiter mit primalen Simplex bis optimale Lösung gefunden wurde";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * double[] f - Werte für die Variablen in der Zielfunktion double[] nb -
	 * Werte für die Variablen der Nebenbedingung String[] vergleich -
	 * Verlgeichsoperator der Nebenbedingung double[] b - Ergebnis der
	 * Nebenbedingung
	 * 
	 * @throws Exception
	 */
	public void simplex() throws Exception {

		// Initialsierung

		anzahldurchläufe = 0;

		TextProperties errorProps = new TextProperties();
		errorProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 12));
		errorProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

		// Überprüfung der korrekten Übermittlung
		if (b.length == vergleich.length && f.length == nb.length) {
			for (int i = 0; i < nb.length; i++) {
				if (nb[i].length != vergleich.length) {

					lang.newText(
							new Coordinates(10, 10),
							"Fehler: Für die Nebenbedinungen sind nicht die Variablenwerte angegeben",
							"excpNB", null, errorProps);
					throw new Exception(
							"Für die Nebenbedinungen sind nicht die Variablenwerte angegeben");
				}
			}

			for (int i = 0; i < vergleichoperatoren.length; i++) {
				switch (vergleichoperatoren[i]) {
				case "kleiner":
					vergleich[i] = "<=";
					break;

				case "groesser":
					vergleich[i] = ">=";
					break;

				case "gleich":
					vergleich[i] = "=";
					break;

				default:

					lang.newText(new Offset(0, 10, system,
							AnimalScript.DIRECTION_SW),
							"Fehler: Unbekannter Operator angegeben",
							"excpOperator", null, errorProps);
					throw new Exception("Unbekannter Operator angegeben");
				}
			}

			TextProperties titleProps = titelEigenschaften;
			titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					Font.SANS_SERIF, Font.BOLD, 16));
			title = lang.newText(new Coordinates(10, 10),
					"Simplex-Algorithmus", "title", null, titleProps);

			// first, set the visual properties for the source code
			scProps = textEigenschaften;
			// scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
			// Color.BLUE);
			// scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
			// Font.SANS_SERIF, Font.PLAIN, 12));
			//
			// scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
			// Color.RED);
			// scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

			SourceCode beschreibung = lang.newSourceCode(new Offset(0, 10,
					title, AnimalScript.DIRECTION_SW), "beschreibung", null,
					scProps);

			beschreibung
					.addCodeLine(
							"Der Simplex-Algorihmus ist ein Optimierungsalgorithmus für die Lösung linearer Gleichungssysteme.",
							null, 0, null);
			beschreibung
					.addCodeLine(
							"Dieser Algorithmus wird beispielweise in der Produktionsplanung angewendet. Für ein bestimmtes Maximierungsproblem wird dabei,",
							null, 0, null);
			beschreibung
					.addCodeLine(
							"unter Angabe von Nebenbedingungen (in Form von Gleichungen und Ungleichungen), iterativ die optimale Lösung bestimmt.",
							null, 0, null);
			beschreibung
					.addCodeLine(
							"(Die Minimierung eines Funktionswertes wird mit Hilfe des Dualen-Simplex gelöst, der hier jedoch nicht behandelt wird.)",
							null, 0, null);
			beschreibung.addCodeLine("", null, 0, null);
			beschreibung
					.addCodeLine(
							"Zur Lösung des Optimierungsproblems muss eine zulässige Basislösung für das lineare Gleichungssystem bekannt sein.",
							null, 0, null);
			beschreibung
					.addCodeLine(
							"Diese Einschränkung kann durch die M-Methode umgangen werden. Die M-Methode berechnet irgendeine zulässige Basislösung.",
							null, 0, null);
			beschreibung
					.addCodeLine(
							"Mit Hilfe dieser kann man letztlich den primalen Simplex anwenden.",
							null, 0, null);

			vorgehen = lang.newSourceCode(new Offset(0, 10, beschreibung,
					AnimalScript.DIRECTION_SW), "vorgehen", null, scProps);

			vorgehen.addCodeLine("1. Simplex-Tableau aufstellen", null, 0, null);
			vorgehen.addCodeLine("2. Pivot Element bestimmen", null, 0, null);
			vorgehen.addCodeLine("I. Wahl der Pivot-Spalte", null, 1, null);
			vorgehen.addCodeLine(
					"a. Keine negativen Eintragungen in der F-Zeile: aktuelle Basislösung ist optimal --> Ende des Verfahrens",
					null, 2, null);
			vorgehen.addCodeLine(
					"b. Negative Eintragungen in der F-Zeile: Auswahl der Variable (entspricht Pivot-Spalte) mit der kleinsten Eintragung ",
					null, 2, null);
			vorgehen.addCodeLine("II. Wahl der Pivot-Zeile", null, 1, null);
			vorgehen.addCodeLine(
					"a. Alle Koeffizienten in der Pivot Spalte sind kleiner oder gleich 0: unbeschränktes Modell (keine optimale Lösung) --> Abbruch des Verfahrens",
					null, 2, null);
			vorgehen.addCodeLine(
					"b. Ansonsten wähle die Zeile als Pivotzeile, mit dem kleinsten Quotienten aus dem Wert der b-Spalte und dem Wert aus der Pivot-Spalte (muss größer als 0 sind)",
					null, 2, null);
			vorgehen.addCodeLine(
					"III. Pivotspalte und Pivotzeile ergeben das Pivotelement",
					null, 1, null);
			vorgehen.addCodeLine(
					"3. Berechnung der neuen Basislösung, Transformtion des Tableaus",
					null, 0, null);
			vorgehen.addCodeLine(
					"I. Tausche die bisherige Basisvariable in der Pivot-Zeile durch die Variable der Pivot-Spalte",
					null, 1, null);
			vorgehen.addCodeLine(
					"II. Schaffe unter der neuen Basisvariable einen Einheitsvektor durch Anwenden linearer Transformationen",
					null, 1, null);
			vorgehen.addCodeLine("4. Fahre fort mit 2. Schritt", null, 0, null);

			lang.nextStep("Stelle Gleichungssystem auf");

			beschreibung.hide();
			vorgehen.moveTo(null, "translate", new Offset(0, 10, title,
					AnimalScript.DIRECTION_SW), null, new MsTiming(100));

			// now, create the source code entity
			system = lang.newSourceCode(new Offset(0, -80, vorgehen,
					AnimalScript.DIRECTION_SW), "system", null, scProps);

			StringBuffer sb = new StringBuffer("Maximiere F = ");
			for (int i = 0; i < f.length; i++) {
				if (f[i] >= 0 && i > 0) {
					sb.append("+" + f[i] + "x" + (i + 1));
				} else {
					sb.append(f[i] + "x" + (i + 1));
				}
			}

			String zielfunktion = sb.toString();
			system.addCodeLine("Zielfunktion: ", null, 0, null);
			system.addCodeLine(zielfunktion, null, 1, null);
			system.addCodeLine("Unter folgenden Nebenbedinungen:", null, 0,
					null);
			String[] nebenbed = new String[vergleich.length];
			for (int i = 0; i < vergleich.length; i++) {
				sb = new StringBuffer();
				for (int j = 0; j < nb.length; j++) {
					if (nb[j][i] > 0 && j > 0) {
						sb.append("+" + nb[j][i] + "x" + (j + 1));
					} else {
						sb.append(nb[j][i] + "x" + (j + 1));
					}
				}

				nebenbed[i] = sb.toString();
				system.addCodeLine(nebenbed[i], null, 1, null);
				system.addCodeElement(vergleich[i] + " " + b[i], null, 1, null);
			}

			vorgehen.highlight(0, 0, true, new TicksTiming(50), null);

			lang.nextStep("Überführe Gleichungssystem in Normalform");

			vorgehen.hide();
			subtitleProps = new TextProperties();
			subtitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					Font.SANS_SERIF, Font.ITALIC, 14));

			Text subTitleTableau = lang.newText(new Offset(0, 10, title,
					AnimalScript.DIRECTION_SW), "Simplex-Tableau aufstellen",
					"subTitleTableau", null, subtitleProps);

			SourceCode vorgehenSimplexTableau = lang.newSourceCode(new Offset(
					0, 2, subTitleTableau, AnimalScript.DIRECTION_SW),
					"vorgehenSimplexTableau", null, scProps);

			vorgehenSimplexTableau.addCodeLine(
					"1. Überführen des Systems in Normalform", null, 0, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"I. Ungleichungen (in den Nebenbedingungen) werden durch Hinzufügen einer zusätzlichen Variable (Schlupfvariable) in Gleichungen überführt",
							null, 1, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"2. Überführen in die kanonische Form --> Überführung des Systems in Matrixschreibweise ",
							null, 0, null);
			vorgehenSimplexTableau.addCodeLine(
					"I. Normalform muss gegeben sein", null, 1, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"II. Die Ergebnisse der Nebenbedingungen müssen positiv sein --> falls nicht gegeben: Durchführen der M-Methode",
							null, 1, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"III. In der Matrix der Nebenbedingungen muss eine Einheitsmatrix enthalten sein",
							null, 1, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"IV. Matrix der Nebenbedingungen enthält für jede Variable (inkl. Schlupfvariablen) eine Spalte",
							null, 1, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"V. Matrix der Nebenbedingungen enthält für jede Nebenbedingung eine Zeile --> entspricht auch der Anzahl an Schlupfvariablen",
							null, 1, null);
			vorgehenSimplexTableau.addCodeLine(
					"3. Ermitteln einer zulässigen Basislösung", null, 0, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"I. Wenn diese nicht durch die Einheitsmartix unter den Schlupfvariablen gegeben ist --> Durchführen der M-Methode",
							null, 1, null);
			vorgehenSimplexTableau.addCodeLine("4. Simplex-Tableau aufstellen",
					null, 0, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"I. Die Werte der Matrix der Nebenbedingungen werden in das Tableau überführt",
							null, 1, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"II. Hinzufügen einer Spalte b am rechten Ende, die die Ergebnisse der Nebenbedingungen enthält",
							null, 1, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"III. Hinzufügen einer Spalte BV am linken Ende, die die Basisvariablen der zulässigen Basislösung enthält",
							null, 1, null);
			vorgehenSimplexTableau
					.addCodeLine(
							"IV. Hinzufügen einer Zeile F am unteren Ende, die die jeweiligen Koeffizienten der Variablen aus der Zielfunktion enthält",
							null, 1, null);

			vorgehenSimplexTableau.highlight(0, 0, true, null, null);

			// now, create the sourcecode entity
			normal = lang.newSourceCode(new Offset(20, -12, system,
					AnimalScript.DIRECTION_NE), "normal", null, scProps);
			normal.addCodeLine("Zielfunktion: ", null, 0, null);
			normal.addCodeLine(zielfunktion, null, 1, null);
			normal.addCodeLine("Unter folgenden Nebenbedinungen:", null, 0,
					null);

			// Überführen in die Normalform und Einführung der Schlupfvariable
			schlupfvariablen = new double[vergleich.length][vergleich.length];
			String[] gleichungssystem = new String[vergleich.length];
			for (int i = 0; i < vergleich.length; i++) {
				switch (vergleich[i]) {
				case "=":
					gleichungssystem[i] = nebenbed[i] + "+0.0x"
							+ (nb.length + i + 1) + " = " + b[i];

					schlupfvariablen[i][i] = 0;
					break;
				case "<=":
					gleichungssystem[i] = nebenbed[i] + "+1.0x"
							+ (nb.length + i + 1) + " = " + b[i];

					schlupfvariablen[i][i] = 1;
					break;
				case ">=":
					gleichungssystem[i] = nebenbed[i] + "-1.0x"
							+ (nb.length + i + 1) + " = " + b[i];

					schlupfvariablen[i][i] = -1;
					break;
				// default:
				//
				//
				// lang.newText(new Offset(0, 10, system,
				// AnimalScript.DIRECTION_SW),
				// "Fehler: Unbekannter Operator angegeben",
				// "excpOperator", null, errorProps);
				// throw new Exception("Unbekannter Operator angegeben");
				}

				normal.addCodeLine(gleichungssystem[i], null, 1, null);

				if (i > 0) {
					normal.unhighlight(i + 2);
				}
				normal.highlight(i + 3);
				lang.nextStep();

			}

			normal.unhighlight(vergleich.length + 2);
			// Überprüfung der Existenz der Einheitsmatrix (Teil der kanonischen
			// Form)
			boolean exist_zulaessigeBasisloesung = true;

			vorgehenSimplexTableau.unhighlight(0, 0, true);
			vorgehenSimplexTableau.highlight(2, 0, true);

			vorgehenSimplexTableau.highlight(8, 0, true);

			hilfetext = lang.newSourceCode(new Offset(30 + (20 * (nb.length
					+ (vergleich.length * 2) + 2)), -12, normal,
					AnimalScript.DIRECTION_NE), "hilfeText", null, scProps);

			hilfetext
					.addCodeLine(
							"Hinweis: Die Basislösung stellt am Ende des Algorithmus die Lösung dar.",
							null, 0, null);
			hilfetext
					.addCodeLine(
							"Am Anfang ist die Basislösung maximal zur Nebenbedingung und am Ende stellt sie die optimale Lösung dar.",
							null, 1, null);
			hilfetext.addCodeLine("", null, 0, null);
			hilfetext.highlight(0, 0, true);
			hilfetext.highlight(1, 0, true);
			for (int i = 0; i < schlupfvariablen.length; i++) {
				for (int j = 0; j < schlupfvariablen[i].length; j++) {
					if (i != j) {
						schlupfvariablen[i][j] = 0;
					} else if (schlupfvariablen[i][j] == -1 && b[j] <= 0) {
						sb = new StringBuffer();
						for (int j2 = 0; j2 < nb.length; j2++) {
							nb[j2][j] = nb[j2][j] * (-1);
							if (nb[j2][j] > 0 && j2 > 0) {
								sb.append("+" + nb[j2][j] + "x" + (j2 + 1));
							} else {
								sb.append(nb[j2][j] + "x" + (j2 + 1));
							}
						}
						b[j] = b[j] * (-1);
						schlupfvariablen[i][j] = 1;
						sb.append("+1.0x" + (nb.length + i + 1) + " = " + b[j]);
						gleichungssystem[j] = sb.toString();

						normal.hide();
						normal = lang.newSourceCode(new Offset(20, -12, system,
								AnimalScript.DIRECTION_NE), "normal", null,
								scProps);
						normal.addCodeLine("Zielfunktion: ", null, 0, null);
						normal.addCodeLine(zielfunktion, null, 1, null);
						normal.addCodeLine("Unter folgenden Nebenbedinungen:",
								null, 0, null);
						for (int j2 = 0; j2 < vergleich.length; j2++) {
							normal.addCodeLine(gleichungssystem[j2], null, 1,
									null);
						}

						normal.highlight(j + 3);
						TextProperties textProps = new TextProperties();
						textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
								new Font(Font.SANS_SERIF, Font.PLAIN, 12));
						int pos = 0;
						if (j > 1) {
							pos++;
						}
						Text normRechnung = lang.newText(new Offset(5,
								(j + 4 + pos) * 12, normal,
								AnimalScript.DIRECTION_NE), "| * (-1)",
								"rechnung", null, textProps);
						lang.nextStep();

						normRechnung.hide();
						normal.unhighlight(j + 3);

					} else if (schlupfvariablen[i][j] != 1) {
						// normal.highlight(j);
						exist_zulaessigeBasisloesung = false;
					}
				}
			}

			// Ermittle zulässige Basisvariable
			zulaessigeBasisloesung = new int[schlupfvariablen.length];
			for (int i = 0; i < zulaessigeBasisloesung.length; i++) {
				zulaessigeBasisloesung[i] = nb.length + i;
			}

			hilfetext
					.addCodeLine(
							"Sind die Ergebnisse der Nebenbedingungen positiv und bilden die Schlupfvariablen eine Einheitsmatrix?",
							null, 0, new TicksTiming(90));

			// Überprüfung der nichtnegativen Seite (Teil der kanonischen Form)
			boolean fuelleNachMmethode = false;
			if (exist_zulaessigeBasisloesung) {

				boolean temp = true;
				for (int i = 0; i < b.length; i++) {

					if (b[i] < 0) {
						temp = false;
						hilfetext.addCodeLine(
								"Nein! Durchführen der M-Methode.", null, 1,
								new TicksTiming(150));
						hilfetext.addCodeLine("", null, 0, null);
						lang.nextStep("Starte M-Methode");
						hilfetext.unhighlight(0);
						hilfetext.unhighlight(1);
						vorgehenSimplexTableau.hide();
						subTitleTableau.hide();
						mMethode();
						vorgehenSimplexTableau.show();
						subTitleTableau.show();
						fuelleNachMmethode = true;
						break;
					}
				}

				if (temp) {
					hilfetext.addCodeLine("Ja!", null, 1, new TicksTiming(150));
					hilfetext.addCodeLine("", null, 0, null);
					lang.nextStep("Fülle Simplex Tableau");
					hilfetext.unhighlight(0);
					hilfetext.unhighlight(1);
				}

			} else {
				hilfetext.addCodeLine("Nein! Durchführen der M-Methode.", null,
						1, new TicksTiming(150));
				hilfetext.addCodeLine("", null, 0, null);
				lang.nextStep("Starte M-Methode");
				hilfetext.unhighlight(0);
				hilfetext.unhighlight(1);
				vorgehenSimplexTableau.hide();
				subTitleTableau.hide();
				mMethode();
				vorgehenSimplexTableau.show();
				subTitleTableau.show();
				fuelleNachMmethode = true;
			}

			vorgehenSimplexTableau.unhighlight(2);
			vorgehenSimplexTableau.unhighlight(8);

			vorgehenSimplexTableau.highlight(10, 0, true);

			// Fülle Simplex Tableau
			if (!fuelleNachMmethode) {
				fuelleTabelle(true);

				vorgehenSimplexTableau.highlight(11, 0, true);

				for (int i = 1; i < simplexTableau.length; i++) {
					for (int j = 1; j < simplexTableau[0].length; j++) {
						tableau.highlightCell(j, i, null, null);
					}
				}

				lang.nextStep();

				vorgehenSimplexTableau.unhighlight(11);
				vorgehenSimplexTableau.highlight(12, 0, true);

				for (int i = 1; i < simplexTableau.length; i++) {
					for (int j = 1; j < simplexTableau[0].length; j++) {
						tableau.unhighlightCell(j, i, null, null);
					}
				}

				for (int i = 1; i <= b.length; i++) {
					tableau.highlightCell(i, simplexTableau.length, null, null);
				}

				lang.nextStep();

				vorgehenSimplexTableau.unhighlight(12);
				vorgehenSimplexTableau.highlight(13, 0, true);

				for (int i = 1; i <= b.length; i++) {
					tableau.unhighlightCell(i, simplexTableau.length, null,
							null);
				}

				for (int i = 1; i <= zulaessigeBasisloesung.length; i++) {
					tableau.highlightCell(i, 0, null, null);
				}

				lang.nextStep();

				vorgehenSimplexTableau.unhighlight(13);
				vorgehenSimplexTableau.highlight(14, 0, true);

				for (int i = 1; i <= zulaessigeBasisloesung.length; i++) {
					tableau.unhighlightCell(i, 0, null, null);
				}

				for (int i = 1; i <= simplexTableau.length; i++) {
					tableau.highlightCell(simplexTableau[0].length, i, null,
							null);
				}

				anzahldurchläufe++;

				lang.nextStep("Simplex-Tableau ist gefüllt und Start des 1. Durchgangs zur Bestimmung der Pivotzeile und -spalte");

				for (int i = 1; i <= simplexTableau.length; i++) {
					tableau.unhighlightCell(simplexTableau[0].length, i, null,
							null);
				}

			}

			vorgehenSimplexTableau.hide();
			subTitleTableau.hide();
			vorgehen.show();
			vorgehen.unhighlight(0);

			vorgehen.highlight(1, 0, true);

			// Durchführen des Simplex-Verfahren

			TwoValueCounter counter = lang.newCounter(tableau);
			CounterProperties cp = new CounterProperties();
			cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);

			TwoValueView view = lang.newCounterView(counter, new Coordinates(
					360, 20), cp, true, true);

			boolean is_optimaleLoesung = true;
			anzahldurchläufe++;

			do {
				// Überprüfen, ob die optimale Lösung betrachtet wird
				is_optimaleLoesung = true; // wiederholen, wegen den Abläufe
											// nach der ersten Runde
				for (int i = 0; i < simplexTableau.length - 1; i++) {
					if (simplexTableau[i][simplexTableau[0].length - 1] < 0) {
						is_optimaleLoesung = false;
					}
				}

				vorgehen.highlight(2, 0, true);
				vorgehen.highlight(3, 0, true);
				hilfetext
						.addCodeLine("Ist Basislösung optimal?", null, 0, null);
				// Wahl der Pivotspalte
				if (!is_optimaleLoesung) {
					hilfetext.addCodeLine("Nein! Wähle Pivotspalte.", null, 1,
							new TicksTiming(50));
					lang.nextStep();
					vorgehen.unhighlight(3);
					vorgehen.highlight(4, 0, true);
					bestimmePivotspalte(simplexTableau);

					vorgehen.unhighlight(4);
					vorgehen.unhighlight(2);
					vorgehen.highlight(5, 0, true);
					vorgehen.highlight(6, 0, true);

					hilfetext.addCodeLine("Wenn möglich wähle Pivotzeile.",
							null, 1, null);

					lang.nextStep();

					// Überprüfe, ob es überhaupt eine Lösung gibt
					if (is_LGloesbar(simplexTableau, 1)) {

						hilfetext.addCodeLine("Ist möglich.", null, 2, null);
						hilfetext.addCodeLine("", null, 0, null);

						vorgehen.unhighlight(6);
						vorgehen.highlight(7, 0, true);

						// Wahl der Privotzeile
						bestimmePivotzeile(simplexTableau, 1);

						for (int i = 0; i <= simplexTableau[0].length; i++) {
							tableau.unhighlightCell(i, pivotspalte + 1, null,
									null);
						}

						for (int i = 0; i <= simplexTableau.length; i++) {
							tableau.unhighlightCell(pivotzeile + 1, i, null,
									null);
						}

						tableau.highlightCell(pivotzeile + 1, pivotspalte + 1,
								null, null);

						vorgehen.unhighlight(5);
						vorgehen.unhighlight(7);
						vorgehen.highlight(8, 0, true);

						hilfetext
								.addCodeLine(
										"Im nächsten Schritt wird die Basislösung getauscht.",
										null, 0, null);
						hilfetext.addCodeLine("", null, 0, null);

						lang.nextStep("Anfang des Basistauschs");

						vorgehen.unhighlight(1);
						vorgehen.unhighlight(8);
						vorgehen.highlight(9, 0, true);
						vorgehen.highlight(10, 0, true);

						// Tausche Basis aus
						tauscheBasis(simplexTableau);

						tableau.unhighlightCell(pivotzeile + 1,
								pivotspalte + 1, null, null);

						vorgehen.unhighlight(9);
						vorgehen.unhighlight(11);

						vorgehen.highlight(12, 0, true);

						lang.nextStep("Ende des Basistauschs und Start des "
								+ anzahldurchläufe
								+ ". Durchgangs zur Bestimmung der Pivotzeile und -spalte");
						anzahldurchläufe++;

						vorgehen.unhighlight(12);

					} else {
						hilfetext
								.addCodeLine(
										"Nicht möglich und somit auch nicht lösbar - LG hat keinen Wert über 0 nur in Pivotspalte",
										"exceptionLG", 1, new TicksTiming(50));
						hilfetext.highlight("exceptionLG");

						throw new Exception(
								"Nicht lösbar - LG hat keinen Wert über 0 nur in Pivotspalte");
					}

				}

			} while (!is_optimaleLoesung);

			hilfetext.addCodeLine("Ja! Das Verfahren ist beendet.", null, 1,
					new TicksTiming(90));
			lang.nextStep("Die optimale Lösung gefunden (Ende des Verfahrens)");
			hilfetext.addCodeLine("", null, 0, null);
			hilfetext.addCodeLine("Das Ergebnis lautet: ", null, 0, null);
			LinkedList<String> ergebnisse = new LinkedList<String>();

			// Ergebnis auslesen
			hilfetext
					.addCodeLine(
							"F-Wert: "
									+ String.valueOf(Math
											.round(simplexTableau[simplexTableau.length - 1][simplexTableau[0].length - 1] * 100) / 100.0),
							"f-wert", 1, null);
			hilfetext.highlight("f-wert");
			tableau.highlightCell(tableau.getNrRows() - 2,
					tableau.getNrCols() - 1, null, null);
			tableau.highlightCell(tableau.getNrRows() - 2, 0, null, null);

			ergebnisse
					.add("F-Wert: "
							+ String.valueOf(Math
									.round(simplexTableau[simplexTableau.length - 1][simplexTableau[0].length - 1] * 100) / 100.0));
			
			for (int i = 0; i < zulaessigeBasisloesung.length; i++) {
				if (zulaessigeBasisloesung[i] < nb.length) {
					hilfetext.addCodeLine("Variable x"
							+ (zulaessigeBasisloesung[i] + 1)
							+ " hat den Wert "
							+ String.valueOf(Math
									.round(simplexTableau[simplexTableau.length - 1][i] * 100) / 100.0),
							"ergebnis" + i, 1, null);
					hilfetext.highlight("ergebnis" + i);
					tableau.highlightCell(i + 1, tableau.getNrCols() - 1, null,
							null);
					tableau.highlightCell(i + 1, 0, null, null);

					ergebnisse.add("Variable x"
							+ (zulaessigeBasisloesung[i] + 1)
							+ " hat den Wert "
							+ String.valueOf(Math
									.round(simplexTableau[simplexTableau.length - 1][i] * 100) / 100.0));

				}
			}

			lang.nextStep("Ausblick des Verfahrens");

			fazitEinblenden(ergebnisse);

		} else {
			lang.newText(new Coordinates(10, 10),
					"Fehler: Eingabewerte stimmmen nicht überein!",
					"excpEingabewerte", null, errorProps);
			lang.newText(
					new Coordinates(10, 20),
					"(Entweder kommt nicht jede Variable sowohl in der Zielfunktion als auch in den Nebenbedingungen vor oder die Anzahl an Ergebnissen der Nebenbedingungen stimmt nicht mit der Anzahl an Vergleichsoperatoren überein.)",
					"excpGrund", null, errorProps);
			throw new Exception("Eingabewerte stimmmen nicht überein");
		}
	}

	private void mMethode() throws Exception {
		Text subTitleTableau = lang.newText(new Offset(0, 10, title,
				AnimalScript.DIRECTION_SW), "M-Methode", "subTitleMMethode",
				null, subtitleProps);

		SourceCode vorgehenMMethode = lang.newSourceCode(new Offset(0, 2,
				subTitleTableau, AnimalScript.DIRECTION_SW),
				"vorgehenMMethode", null, scProps);

		vorgehenMMethode
				.addCodeLine(
						"1. Gleichungen, die keine Schlupfariable enthalten oder in denen die Schlupfvariable einen negativen Koeffizient hat, werden um künstliche Variable ergänzt",
						null, 0, null);
		vorgehenMMethode.addCodeLine("2. Umformung der Zielfunktion", null, 0,
				null);
		vorgehenMMethode
				.addCodeLine(
						"I. Ergänzen der Zielfunktion, um künstliche Variablen mit hohem Koeffizient M",
						null, 1, null);
		vorgehenMMethode
				.addCodeLine(
						"II. Ersetzen der künstlichen Variablen durch entsprechende Gleichungen",
						null, 1, null);
		vorgehenMMethode.addCodeLine(
				"III. Bilde zweite Zielfunktion für alle M-Terme", null, 1,
				null);
		vorgehenMMethode.addCodeLine("3. Anwenden des primalen Simplex", null,
				0, null);
		vorgehenMMethode.addCodeLine("I. Wahl der Pivot-Spalte", null, 1, null);
		vorgehenMMethode
				.addCodeLine(
						"a. Alle künstlichen Variablen aus der Basislösung entfernt: aktuelle Basislösung ist zulässig --> Ende des Verfahrens",
						null, 2, null);
		vorgehenMMethode
				.addCodeLine(
						"b. Negative Eintragungen in der M-Zeile (Ansonsten Abbruch): Auswahl der Variable (entspricht Pivot-Spalte) mit der kleinsten Eintragung",
						null, 2, null);
		vorgehenMMethode.addCodeLine("II. Wahl der Pivot-Zeile", null, 1, null);
		vorgehenMMethode
				.addCodeLine(
						"a. Alle Koeffizienten in der Pivot Spalte sind kleiner oder gleich 0: unbeschränktes Modell (keine zulässige Lösung berechenbar) --> Abbruch des Verfahrens",
						null, 2, null);
		vorgehenMMethode
				.addCodeLine(
						"b. Ansonsten wähle die Zeile als Pivotzeile, mit dem kleinsten Quotienten aus dem Wert der b-Spalte und dem Wert aus der Pivot-Spalte (muss größer als 0 sind)",
						null, 2, null);
		vorgehenMMethode
				.addCodeLine(
						"III. Berechnung der neuen Basislösung, Transformtion des Tableaus",
						null, 1, null);
		vorgehenMMethode.addCodeLine("IV. Fahre fort mit 2. Schritt", null, 1,
				null);
		vorgehenMMethode.addCodeLine(
				"4. Entferne die künstlichen Variablen und M-Zeile", null, 0,
				null);

		// Anzahl an künstlichen Variablen bestimmen
		anzahlKuenstlicheVariablen = 0;
		int[] betroffeneNB = new int[vergleich.length];
		for (int i = 0; i < schlupfvariablen.length; i++) {
			for (int j = 0; j < schlupfvariablen[0].length; j++) {
				if (i == j && (schlupfvariablen[i][j] != 1 || b[j] < 0)) {
					anzahlKuenstlicheVariablen++;
					betroffeneNB[j] = 1;
				} else if (i == j) {
					betroffeneNB[j] = 0;
				}
			}
		}

		// Künstliche Variablen einführen
		kuenstlicheVariablen = new double[anzahlKuenstlicheVariablen][vergleich.length];

		for (int i = 0; i < kuenstlicheVariablen.length; i++) {
			for (int j = 0; j < kuenstlicheVariablen[0].length; j++) {
				kuenstlicheVariablen[i][j] = 0;
			}
		}

		int StelleKuenstlicheVariable = 0;
		for (int i = 0; i < betroffeneNB.length; i++) {
			if (betroffeneNB[i] != 0) {
				kuenstlicheVariablen[StelleKuenstlicheVariable][i] = 1;
				StelleKuenstlicheVariable++;
			}
		}

		// Umformung der Zielfunktion und Bestimmung der zulässigen Lösung der
		// M-Methode
		m = new double[nb.length + schlupfvariablen.length
				+ anzahlKuenstlicheVariablen + 1];

		for (int i = 0; i < m.length; i++) {
			m[i] = 0;

		}

		for (int i = 0; i < f.length; i++) {
			for (int j = 0; j < betroffeneNB.length; j++) {
				if (betroffeneNB[j] != 0) {
					m[i] = m[i] + nb[i][j];
				}
			}
		}

		int temp2 = 0;
		double value = 0;
		for (int j = 0; j < betroffeneNB.length; j++) {
			if (betroffeneNB[j] != 0) {
				m[f.length + j] = schlupfvariablen[j][j];
				value = value - b[j];
				zulaessigeBasisloesung[j] = nb.length + schlupfvariablen.length
						+ temp2;
				temp2++;
			} else {
				zulaessigeBasisloesung[j] = nb.length + j;
			}
		}

		m[m.length - 1] = value;

		// Füllen Starttableau der M-Methode

		fuelleTabelle(false);

		vorgehenMMethode.highlight(0, 0, true);

		hilfetext
				.addCodeLine(
						"Füge bei jeder Gleichung, die vor den Schlupfvariablen nicht den Koeffizienten 1.0 hat eine weitere Variable y mit dem Koeffizienten 1.0 ein .",
						null, 0, null);
		hilfetext.addCodeLine("", null, 0, null);

		for (int i = 1; i <= anzahlKuenstlicheVariablen; i++) {
			for (int j = 1; j <= vergleich.length; j++) {
				tableau.highlightCell(j, i + nb.length + vergleich.length,
						null, null);
			}

		}

		lang.nextStep();

		for (int i = 1; i <= anzahlKuenstlicheVariablen; i++) {
			for (int j = 1; j <= vergleich.length; j++) {
				tableau.unhighlightCell(j, i + nb.length + vergleich.length,
						null, null);
			}

		}

		vorgehenMMethode.unhighlight(0);
		vorgehenMMethode.highlight(1, 0, true);

		for (int i = 1; i <= mTableau.length; i++) {
			tableau.highlightCell(mTableau[0].length, i, null, null);
		}

		lang.nextStep();

		for (int i = 1; i <= mTableau.length; i++) {
			tableau.unhighlightCell(mTableau[0].length, i, null, null);
		}

		vorgehenMMethode.unhighlight(1);

		vorgehenMMethode.highlight(5, 0, true);

		hilfetext
				.addCodeLine(
						"Rest der Tabelle ist genauso wie das Simplex Tableau gefüllt.",
						null, 0, null);
		hilfetext
				.addCodeLine(
						"Die Werte der Matrix der Nebenbedingungen werden in das Tableau überführt.",
						null, 1, null);

		for (int i = 1; i <= nb.length + schlupfvariablen.length; i++) {
			for (int j = 1; j <= vergleich.length; j++) {
				tableau.highlightCell(j, i, null, null);
			}
		}

		lang.nextStep();

		for (int i = 1; i <= nb.length + schlupfvariablen.length; i++) {
			for (int j = 1; j <= vergleich.length; j++) {
				tableau.unhighlightCell(j, i, null, null);
			}
		}

		hilfetext
				.addCodeLine(
						"Hinzufügen einer Spalte b am rechten Ende, die die Ergebnisse der Nebenbedingungen enthält.",
						null, 1, null);

		for (int i = 1; i <= vergleich.length; i++) {
			tableau.highlightCell(i, mTableau.length, null, null);
		}

		lang.nextStep();

		for (int i = 1; i <= vergleich.length; i++) {
			tableau.unhighlightCell(i, mTableau.length, null, null);
		}

		hilfetext
				.addCodeLine(
						"Hinzufügen einer Spalte BV am linken Ende, die die Basisvariablen der zulässigen Basislösung enthält.",
						null, 1, null);

		for (int i = 1; i <= vergleich.length; i++) {
			tableau.highlightCell(i, 0, null, null);
		}

		lang.nextStep();

		for (int i = 1; i <= vergleich.length; i++) {
			tableau.unhighlightCell(i, 0, null, null);
		}

		hilfetext
				.addCodeLine(
						"Hinzufügen einer Zeile F am unteren Ende, die die jeweiligen Koeffizienten der Variablen aus der Zielfunktion enthält.",
						null, 1, null);
		hilfetext.addCodeLine("", null, 0, null);

		for (int i = 1; i <= mTableau.length; i++) {
			tableau.highlightCell(mTableau[0].length - 1, i, null, null);
		}

		lang.nextStep("M-Tableau ist aufgestellt und Start des 1. Durchgangs zur Bestimmung der Pivotzeile und -spalte");

		for (int i = 1; i <= mTableau.length; i++) {
			tableau.unhighlightCell(mTableau[0].length - 1, i, null, null);
		}

		int anzahlDurchlaufeM = 2;

		// Durchführen der M-Methode
		boolean is_zulaessigeLoesung = true;

		do {
			// Überprüfen, ob die zulässige Lösung betrachtet wird
			is_zulaessigeLoesung = true; // wiederholen, wegen den Abläufe
											// nach der ersten Runde
			for (int i = 0; i < zulaessigeBasisloesung.length; i++) {
				if (zulaessigeBasisloesung[i] >= (nb.length + schlupfvariablen.length)) {
					is_zulaessigeLoesung = false;
				}
			}

			boolean pivotElementwhaelbar = false;
			for (int i = 0; i < mTableau.length - 1; i++) {
				if (mTableau[i][mTableau[i].length - 1] < 0) {
					pivotElementwhaelbar = true;
				}
			}

			vorgehenMMethode.highlight(6, 0, true);
			vorgehenMMethode.highlight(7, 0, true);
			hilfetext.addCodeLine("Ist die Lösung eine zulässige Basislösung?",
					null, 0, null);

			// Wahl der Pivotspalte
			if (!is_zulaessigeLoesung) {
				if (pivotElementwhaelbar) {

					hilfetext.addCodeLine("Nein! Wähle Pivotspalte.", null, 1,
							new TicksTiming(50));
					lang.nextStep();
					vorgehenMMethode.unhighlight(7);
					vorgehenMMethode.highlight(8, 0, true);
					bestimmePivotspalte(mTableau);

					vorgehenMMethode.unhighlight(8);
					vorgehenMMethode.unhighlight(6);
					vorgehenMMethode.highlight(9, 0, true);
					vorgehenMMethode.highlight(10, 0, true);

					hilfetext.addCodeLine("Wenn möglich wähle Pivotzeile.",
							null, 1, null);

					lang.nextStep();

					// Überprüfe, ob es überhaupt eine Lösung gibt
					if (is_LGloesbar(mTableau, 2)) {

						hilfetext.addCodeLine("Ist möglich.", null, 2, null);
						hilfetext.addCodeLine("", null, 0, null);

						vorgehenMMethode.unhighlight(10);
						vorgehenMMethode.highlight(11, 0, true);

						// Wahl der Privotzeile
						bestimmePivotzeile(mTableau, 2);

						for (int i = 0; i <= mTableau[0].length; i++) {
							tableau.unhighlightCell(i, pivotspalte + 1, null,
									null);
						}

						for (int i = 0; i <= mTableau.length; i++) {
							tableau.unhighlightCell(pivotzeile + 1, i, null,
									null);
						}

						tableau.highlightCell(pivotzeile + 1, pivotspalte + 1,
								null, null);

						hilfetext
								.addCodeLine(
										"Im nächsten Schritt wird die Basislösung getauscht.",
										null, 0, null);
						hilfetext.addCodeLine("", null, 0, null);

						lang.nextStep("Anfang des Basistauschs");

						vorgehenMMethode.unhighlight(9);
						vorgehenMMethode.unhighlight(11);
						vorgehenMMethode.highlight(12, 0, true);

						// Tausche Basis aus
						tauscheBasis(mTableau);

						vorgehen.unhighlight(11);

						tableau.unhighlightCell(pivotzeile + 1,
								pivotspalte + 1, null, null);

						vorgehenMMethode.unhighlight(12);

						vorgehenMMethode.highlight(13, 0, true);

						lang.nextStep("Ende des Basistauschs und Start des "
								+ anzahlDurchlaufeM
								+ ". Durchgangs zur Bestimmung der Pivotzeile und -spalte");
						anzahlDurchlaufeM++;

						vorgehenMMethode.unhighlight(13);

					} else {
						hilfetext
								.addCodeLine(
										"Nicht möglich und somit nicht lösbar - LG hat keinen Wert über 0 nur in Pivotspalte",
										"exceptionLG", 1, new TicksTiming(50));
						hilfetext.highlight("exceptionLG");
						throw new Exception("M-Methode: Nicht lösbar");
					}

				} else {
					hilfetext.addCodeLine("", null, 0, null);

					hilfetext
							.addCodeLine(
									"Nicht lösbar - LG hat keinen Wert unter 0 nur in M-Zeile",
									"exceptionLG", 1, new TicksTiming(50));
					hilfetext.highlight("exceptionLG");
					throw new Exception("Pivot Element nicht wählbar");
				}
			}
		} while (!is_zulaessigeLoesung);

		lang.nextStep();
		// Ergebnis auslesen
		hilfetext
				.addCodeLine(
						"Ja. Die M-Methode ist beendet. Überführen in ein gültiges Simplex-Tableau.",
						null, 1, null);
		hilfetext.addCodeLine("", null, 0, null);
		vorgehenMMethode.unhighlight(5);
		vorgehenMMethode.unhighlight(6);
		vorgehenMMethode.unhighlight(7);
		vorgehenMMethode.highlight(14, 0, true);

		lang.nextStep("Ende M-Methode: Die zulässige Lösung ist gefunden --> Überführung ins Simplex Tableau");

		simplexTableau = new double[nb.length + schlupfvariablen.length + 1][vergleich.length + 1];

		for (int i = 0; i < simplexTableau.length; i++) {
			for (int j = 0; j < simplexTableau[0].length; j++) {
				simplexTableau[i][j] = mTableau[i][j];
			}
		}

		for (int i = 0; i < simplexTableau[0].length; i++) {
			simplexTableau[simplexTableau.length - 1][i] = mTableau[mTableau.length - 1][i];
		}

		tableau.hide();

		String[][] matrix = new String[simplexTableau[0].length + 2][simplexTableau.length + 1];

		for (int i = 0; i < matrix.length - 1; i++) {
			if (i == 0) {
				for (int j = 1; j < matrix[0].length - 1; j++) {
					matrix[0][j] = "x" + String.valueOf(j);
				}

			} else {
				for (int j = 1; j < matrix[0].length; j++) {
					matrix[i][j] = String.valueOf(Math
							.round(simplexTableau[j - 1][i - 1] * 100) / 100.0);
				}
			}
		}

		for (int i = 0; i < matrix[0].length; i++) {
			matrix[matrix.length - 1][i] = "";
		}

		matrix[0][0] = "";
		matrix[0][matrix[0].length - 1] = "B";
		matrix[matrix.length - 2][0] = "F";

		for (int i = 0; i < zulaessigeBasisloesung.length; i++) {
			matrix[i + 1][0] = "x"
					+ String.valueOf(zulaessigeBasisloesung[i] + 1);
		}

		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);

		String[] aZeiger = new String[simplexTableau.length + 1];

		for (int i = 0; i < aZeiger.length; i++) {
			aZeiger[i] = "          ";
		}

		arPrim = lang
				.newStringArray(new Offset(0, 40, system,
						AnimalScript.DIRECTION_SW), aZeiger, "arPrim", null,
						arrayProps);

		MatrixProperties maProps = matrixEigenschaften;
		// maProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
		// Color.YELLOW);
		// maProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		// Font.SANS_SERIF, Font.PLAIN, 12));

		// maProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
		// Color.RED);
		// maProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
		// Color.BLACK);
		maProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");

		tableau = lang.newStringMatrix(new Offset(0, 70, system,
				AnimalScript.DIRECTION_SW), matrix, "matrix", null, maProps);
		String[][] re = new String[matrix.length - 1][1];

		for (int i = 0; i < re.length; i++) {
			re[i][0] = "";
		}
		MatrixProperties maReProps = matrixEigenschaften;
		// maReProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
		// Color.YELLOW);
		// maReProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		// Font.SANS_SERIF, Font.PLAIN, 12));
		//
		// maReProps
		// .set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		// maReProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
		// Color.BLACK);
		maReProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");

		matrixRechnung = lang.newStringMatrix(new Offset(
				25 * simplexTableau.length, 50, tableau,
				AnimalScript.DIRECTION_NE), re, "matrixrechnung", null,
				maReProps);

		hilfetext.addCodeLine(
				"Nun ist ein gültiges Simplex-Tableau entstanden.", null, 0,
				null);
		hilfetext.addCodeLine("", null, 0, null);

		lang.nextStep();

		vorgehenMMethode.hide();
		subTitleTableau.hide();

	}

	private void bestimmePivotspalte(double[][] array) {
		pivotspalte = 0;
		for (int i = 0; i < array.length - 1; i++) {
			if (array[i][array[0].length - 1] < array[pivotspalte][array[0].length - 1]) {
				pivotspalte = i;
			}
		}
		for (int i = 0; i <= array[0].length; i++) {
			tableau.highlightCell(i, pivotspalte + 1, null, null);
		}

		lang.nextStep();
	}

	private void bestimmePivotzeile(double[][] array, int letzteZeile) {
		pivotzeile = -1;
		double pivot_quotient_b_a = -1;
		do {
			pivotzeile++;
			if (array[pivotspalte][pivotzeile] != 0) {
				pivot_quotient_b_a = array[array.length - 1][pivotzeile]
						/ array[pivotspalte][pivotzeile];
				matrixRechnung
						.put(pivotzeile,
								0,
								String.valueOf(Math
										.round(array[array.length - 1][pivotzeile] * 100) / 100.0)
										+ " / "
										+ String.valueOf(Math
												.round(array[pivotspalte][pivotzeile] * 100) / 100.0)
										+ " = "
										+ String.valueOf(Math
												.round(pivot_quotient_b_a * 100) / 100.0),
								null, null);
			} else {
				matrixRechnung.put(pivotzeile, 0, "0", null, null);
			}
		} while (array[pivotspalte][pivotzeile] <= 0);
		for (int i = pivotzeile + 1; i < array[0].length - letzteZeile; i++) {
			if (array[pivotspalte][i] != 0) {

				double quotient_b_a = array[array.length - 1][i]
						/ array[pivotspalte][i];

				matrixRechnung
						.put(i,
								0,
								String.valueOf(Math
										.round(array[array.length - 1][i] * 100) / 100.0)
										+ " / "
										+ String.valueOf(Math
												.round(array[pivotspalte][i] * 100) / 100.0)
										+ " = "
										+ String.valueOf(Math
												.round(quotient_b_a * 100) / 100.0),
								null, null);

				if (array[pivotspalte][i] > 0) {
					if (quotient_b_a >= 0 & quotient_b_a < pivot_quotient_b_a) {
						pivotzeile = i;
						pivot_quotient_b_a = quotient_b_a;
					}
				}
			} else {
				matrixRechnung.put(i, 0, "0", null, null);
			}
		}

		for (int i = 0; i <= array.length; i++) {
			tableau.highlightCell(pivotzeile + 1, i, null, null);
		}

		lang.nextStep();

		for (int i = 0; i < array[0].length; i++) {
			matrixRechnung.put(i, 0, "", null, null);
		}

	}

	private void fuelleTabelle(boolean is_primalSimplex) {
		int fZeile = 1;
		double[][] array;
		String[][] matrix;
		if (is_primalSimplex) {
			array = new double[nb.length + schlupfvariablen.length + 1][vergleich.length + 1];
			matrix = new String[vergleich.length + 3][nb.length
					+ schlupfvariablen.length + 2];
			simplexTableau = array;
		} else {
			array = new double[nb.length + schlupfvariablen.length
					+ anzahlKuenstlicheVariablen + 1][vergleich.length + 2];
			matrix = new String[vergleich.length + 4][nb.length
					+ schlupfvariablen.length + anzahlKuenstlicheVariablen + 2];
			mTableau = array;

			fZeile = 2;

			for (int i = 0; i < kuenstlicheVariablen.length; i++) {
				for (int j = 0; j < kuenstlicheVariablen[i].length; j++) {
					array[i + nb.length + schlupfvariablen.length][j] = kuenstlicheVariablen[i][j];
				}

				matrix[0][i + nb.length + schlupfvariablen.length + 1] = "y"
						+ String.valueOf(i + 1);
			}

			for (int i = 0; i < m.length - 1; i++) {
				array[i][array[0].length - 1] = m[i] * (-1);
			}

			array[m.length - 1][array[0].length - 1] = m[m.length - 1];
			matrix[matrix.length - 2][0] = "M";

		}

		for (int i = 0; i < f.length; i++) {
			f[i] = f[i] * (-1);
		}

		for (int i = 0; i < nb.length; i++) {
			for (int j = 0; j < nb[i].length; j++) {
				array[i][j] = nb[i][j];
			}

			matrix[0][i + 1] = "x" + String.valueOf(i + 1);
		}

		for (int i = 0; i < schlupfvariablen.length; i++) {
			for (int j = 0; j < schlupfvariablen[i].length; j++) {
				array[i + nb.length][j] = schlupfvariablen[i][j];
			}

			matrix[0][i + nb.length + 1] = "x"
					+ String.valueOf(i + nb.length + 1);
		}

		for (int i = 0; i < b.length; i++) {
			array[array.length - 1][i] = b[i];
		}

		for (int i = 0; i < f.length; i++) {
			array[i][array[0].length - fZeile] = f[i];
		}

		for (int i = f.length; i < array.length; i++) {
			array[i][array[0].length - fZeile] = 0;
		}

		matrix[0][0] = "";
		for (int i = 0; i < zulaessigeBasisloesung.length; i++) {
			if (zulaessigeBasisloesung[i] >= nb.length
					+ schlupfvariablen.length) {
				matrix[i + 1][0] = "y"
						+ String.valueOf(zulaessigeBasisloesung[i] + 1
								- nb.length - schlupfvariablen.length);
			} else {
				matrix[i + 1][0] = "x"
						+ String.valueOf(zulaessigeBasisloesung[i] + 1);
			}
		}
		matrix[0][matrix[0].length - 1] = "B";
		matrix[matrix.length - fZeile - 1][0] = "F";

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				matrix[j + 1][i + 1] = String.valueOf(Math
						.round(array[i][j] * 100) / 100.0);
			}
		}

		for (int i = 0; i < matrix[0].length; i++) {
			matrix[matrix.length - 1][i] = "";
		}

		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);

		// arrayProps
		// .set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY,
		// Color.WHITE);
		// arrayProps
		// .set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 10);
		// arrayProps
		// .set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 30);

		String[] aZeiger = new String[array.length + 1];

		for (int i = 0; i < aZeiger.length; i++) {
			aZeiger[i] = "          ";
		}

		arPrim = lang
				.newStringArray(new Offset(0, 40, system,
						AnimalScript.DIRECTION_SW), aZeiger, "arPrim", null,
						arrayProps);

		MatrixProperties maProps = matrixEigenschaften;
		// maProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
		// Color.YELLOW);
		// maProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		// Font.SANS_SERIF, Font.PLAIN, 12));
		//
		// maProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
		// Color.RED);
		// maProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
		// Color.BLACK);
		maProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");

		tableau = lang.newStringMatrix(new Offset(0, 70, system,
				AnimalScript.DIRECTION_SW), matrix, "matrix", null, maProps);
		String[][] re = new String[matrix.length - 1][1];

		for (int i = 0; i < re.length; i++) {
			re[i][0] = "";
		}
		MatrixProperties maReProps = matrixEigenschaften;
		// maReProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
		// Color.YELLOW);
		// maReProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		// Font.SANS_SERIF, Font.PLAIN, 12));
		//
		// maReProps
		// .set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		// maReProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
		// Color.BLACK);
		maReProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");

		matrixRechnung = lang.newStringMatrix(new Offset(25 * array.length, 50,
				tableau, AnimalScript.DIRECTION_NE), re, "matrixrechnung",
				null, maReProps);

		lang.nextStep();

	}

	private boolean is_LGloesbar(double[][] array, int relevanteZeilen) {
		boolean loesbarerLG = false;
		for (int i = 0; i < array[0].length - relevanteZeilen; i++) {
			if (array[pivotspalte][i] > 0) {
				loesbarerLG = true;
			}
		}
		return loesbarerLG;
	}

	private void tauscheBasis(double[][] array) {

		zulaessigeBasisloesung[pivotzeile] = pivotspalte;

		ArrayMarkerProperties amProp = pfeileEigenschaften;

		am = lang.newArrayMarker(arPrim, 0, "zeiger", null, amProp);
		tableau.put(pivotzeile + 1, 0, tableau.getElement(0, pivotspalte + 1),
				null, null);
		double umrechnungsfaktor = 0;

		lang.nextStep();

		am.hide();

		umrechnungsfaktor = 1 / array[pivotspalte][pivotzeile];

		matrixRechnung
				.put(pivotzeile,
						0,
						"* "
								+ String.valueOf(Math
										.round(umrechnungsfaktor * 100) / 100.0),
						null, null);

		lang.nextStep();

		for (int j = 0; j < array.length; j++) {
			am = lang.newArrayMarker(arPrim, j + 1, "zeiger" + j, null, amProp);
			array[j][pivotzeile] = umrechnungsfaktor * array[j][pivotzeile];
			tableau.put(pivotzeile + 1, j + 1, String.valueOf(Math
					.round(array[j][pivotzeile] * 100) / 100.0), null, null);
			lang.nextStep();
			am.hide();
		}

		matrixRechnung.put(pivotzeile, 0, "", null, null);

		vorgehen.unhighlight(10);
		vorgehen.highlight(11, 0, true);

		for (int i = 0; i < array[0].length; i++) {
			if (i != pivotzeile) {
				am.hide();
				umrechnungsfaktor = array[pivotspalte][i]
						/ array[pivotspalte][pivotzeile];

				matrixRechnung
						.put(i,
								0,
								"- "
										+ (pivotzeile + 1)
										+ ".Zeile * "
										+ String.valueOf(Math
												.round(umrechnungsfaktor * 100) / 100.0),
								null, null);
				lang.nextStep();

				for (int j = 0; j < array.length; j++) {
					am.hide();
					array[j][i] = array[j][i]
							- (umrechnungsfaktor * array[j][pivotzeile]);
					tableau.put(i + 1, j + 1, String.valueOf(Math
							.round(array[j][i] * 100) / 100.0), null, null);

					am = lang.newArrayMarker(arPrim, j + 1, "zeiger" + i + j,
							null, amProp);

					lang.nextStep();
				}

				matrixRechnung.put(i, 0, "", null, null);

			}
		}

		am.hide();

	}

	private void fazitEinblenden(LinkedList<String> ergebnisse) {

		Text subTitleTableau = lang.newText(new Offset(0, 10, title,
				AnimalScript.DIRECTION_SW), "Ende und Ausblick des Verfahrens",
				"subTitleFazit", null, subtitleProps);

		SourceCode fazit = lang.newSourceCode(new Offset(0, 2, subTitleTableau,
				AnimalScript.DIRECTION_SW), "fazit", null, scProps);

		fazit.addCodeLine("Das vorliegende Beispiel wurde in "
				+ (anzahldurchläufe - 1)
				+ " Schritten (ohne M-Methode) gelöst.", null, 0, null);
		fazit.addCodeLine("", null, 0, null);
		fazit.addCodeLine("Die Lösung lautet:", null, 0, null);

		for (Iterator<String> iterator = ergebnisse.iterator(); iterator
				.hasNext();) {
			String string = (String) iterator.next();
			fazit.addCodeLine(string, null, 1, null);

		}

		fazit.addCodeLine("", null, 0, null);
		fazit.addCodeLine(
				"Im schlimmsten Fall kann der Algorithmus eine exponentielle Laufzeit annehmen. So ist das ",
				null, 0, null);
		fazit.addCodeLine(
				"Verfahren in der Theorie langsamer als etwa Innere-Punkte-Verfahren. In der Praxis hat sich",
				null, 0, null);
		fazit.addCodeLine(
				"jedoch gezeigt, dass dieses meist schneller abläuft.", null,
				0, null);
		fazit.addCodeLine("", null, 0, null);
		fazit.addCodeLine(
				"Zudem hat das Verfahren den Vorteil, dass es bei kleineren Änderungen, wie neuen ",
				null, 0, null);
		fazit.addCodeLine(
				"Nebenbedingungen, nicht von vorne beginnen muss, sondern von der letzten gefundenen ",
				null, 0, null);
		fazit.addCodeLine("Lösung beginnen kann.", null, 0, null);
		fazit.addCodeLine("", null, 0, null);
		fazit.addCodeLine(
				"Zusätzlich kann man mit einigen Verbesserungen die Laufzeit des Verfahrens verbessern. Dies ",
				null, 0, null);
		fazit.addCodeLine(
				"geschieht etwa durch verbesserte Regeln zur Auswahl des Pivotelements, der Pivotspalte oder der Pivotzeile.",
				null, 0, null);
		fazit.addCodeLine("", null, 0, null);
		fazit.addCodeLine(
				"Außerdem kann mit dem Dualen Simplex-Verfahren die optimale Duallösung des zum linearen ",
				null, 0, null);
		fazit.addCodeLine(
				"Problem zugehörigen dualen Problems bestimmt werden. Eine weitere Verbesserung stellt die hier ",
				null, 0, null);
		fazit.addCodeLine(
				"zusätzlich implementierte M-Methode dar,  die das Lösen des linearen Problems erlaubt, falls in den ",
				null, 0, null);
		fazit.addCodeLine(
				"Nebenbedingungen größer gleich und gleich Relationen existieren.",
				null, 0, null);
		fazit.addCodeLine("", null, 0, null);
		fazit.addCodeLine("(Quelle: Wikipedia)", null, 0, null);

		system.moveTo(null, "translate", new Offset(10, 0, fazit,
				AnimalScript.DIRECTION_NE), null, new MsTiming(100));

		vorgehen.hide();
		normal.hide();
		hilfetext.hide();
		tableau.hide();

	}

}