/*
 * Jitter.java
 * Patrick Weber, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

import algoanim.properties.SourceCodeProperties;

public class Jitter implements Generator, ValidatingGenerator {
	// public static int ArraySize = 100;
	public static int NoOfStud;

	private static Language lang;
	private static double Radius_R;
	private static String[] IN_stringArray;
	private static CircleProperties DotInCS;
	private static TextProperties actDot;
	private static SourceCodeProperties sourceCodeP;

	public static TextProperties titleP;
	public static TextProperties descriptionP;

	public static Student[] gradeList;
	public static Student[] gradeList_Before;
	public static Student[] gradeList_After;

	public static Text label1;
	public static Text title;
	public static Text description;
	public static Text dataPoint;

	public static SourceCode mysc;
	public static SourceCode motivation;
	public static SourceCode Conclusion;

	static int xo = 100;
	static int yo = 400;
	static int size = 300;
	static int step = 50;

	static Coordinates origin = new Coordinates(xo, yo);
	static Coordinates eox = new Coordinates(xo + size, yo);
	static Coordinates eoy = new Coordinates(xo, yo - size);

	public static Circle[] points;

	static DecimalFormat f = new DecimalFormat("0.###");

	public static double[][] randoms;

	static Color DotInCSFC;

	public void init() {
		lang = new AnimalScript("Jitter", "Patrick Weber", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		Radius_R = (double) primitives.get("Radius_R");
		IN_stringArray = (String[]) primitives.get("IN_stringArray");
		DotInCS = (CircleProperties) props.getPropertiesByName("DotInCS");
		actDot = (TextProperties) props.getPropertiesByName("actDot");
		sourceCodeP = (SourceCodeProperties) props.getPropertiesByName("sourceCodeP");

		NoOfStud = IN_stringArray.length;

		gradeList = new Student[NoOfStud];
		gradeList_Before = new Student[NoOfStud];
		gradeList_After = new Student[NoOfStud];

		points = new Circle[NoOfStud];

		DotInCSFC = (Color) DotInCS.get(AnimationPropertiesKeys.FILL_PROPERTY);

		// Einlesen des eingegebenen Arrays
		for (int i = 0; i < IN_stringArray.length; i++) {
			double gradeA = 1.0, gradeB = 1.0;
			// Leerzeichen entfernen
			IN_stringArray[i].replaceAll("\\s+", "");
			gradeA = Double.parseDouble(IN_stringArray[i].substring(1, 4));
			gradeB = Double.parseDouble(IN_stringArray[i].substring(5, 8));
			gradeList[i] = new Student(gradeA, gradeB);
		}

		lang.setStepMode(true); // Schrittmodus aktivieren
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		showIntro();

		firstdrawCS(gradeList);

		copyArray(gradeList, gradeList_Before);

		jitterArray(gradeList);

		copyArray(gradeList, gradeList_After);

		redrawCS(gradeList);

		showOutro();

		lang.finalizeGeneration();

		// try {
		// return new String(lang.toString().getBytes("UTF-8"), "ISO-8859-1");
		// //Umlaute-Fix
		// } catch (UnsupportedEncodingException e) {
		// return "Error";
		// }
		return lang.toString();
	}

	public void showOutro() {
		MultipleChoiceQuestionModel distribution2 = new MultipleChoiceQuestionModel("dist2");
		distribution2.setPrompt(
				"Nochmal zur Eingangsfrage: Gibt es denn nun mehr gute oder mehr schlechte Studenten im Jahrgang?");
		distribution2.addAnswer("Gute Studenten", 1, "Jetzt ist es auch im Diagramm sichtbar.");
		distribution2.addAnswer("Schlechte Studenten", 0, "Versuche es noch einmal!");
		distribution2.setNumberOfTries(1);
		lang.addMCQuestion(distribution2);

		lang.nextStep();

		lang.hideAllPrimitives();
		title.show();

		String concl = "Jitter verrauscht die Daten bewusst und erzielt damit zweierlei:\n"
				+ "1. Es werden Ergebnisse verfälscht. Die Punkte werden nämlich nicht\n"
				+ "auf ihre eigentlichen Werte abgebildet, sondern auf davon leicht ab-\n"
				+ "weichende. Dieser Nachteil bringt aber folgenden Vorteil.\n"
				+ "2. Overplotting, also die Abbildung mehrerer Datenpunkte auf nur einen\n"
				+ "visuellen (Bildschirm-)Punkt wird vermieden oder zumindest reduziert.\n"
				+ "Damit wird nun erkenntlich, in welchem Bereich sich viele Datenpunkte\n"
				+ "- im Beispiel Studenten - tummeln und in welchem weniger viele.";

		Conclusion = lang.newSourceCode(new Coordinates(50, 60), "Conclusion", null);
		Conclusion.addMultilineCode(concl, "0", null);

		lang.nextStep("Fazit");
	}

	public void showIntro() {
		titleP = new TextProperties();
		titleP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		title = lang.newText(new Coordinates(400, 10), "JITTER", "Title", null, titleP);

		String motiv = "Eine Möglichkeit, zweidimensionale Daten zu visualisieren, ist die Anordnung dieser Daten "
				+ "\n" + "in einem Koordinatensystem. Sind die zugrundeliegenden Datenwerte allerdings diskret, wie"
				+ "\n"
				+ "beispielsweise Noten an der TU Darmstadt, so kommt es bereits bei relativ kleinen Datenmengen\n"
				+ "zum Overplotting. Das bedeutet, dass zwei verschiedene Daten, mit (zufällig) denselben\n"
				+ "Datenwerten im Koordinatensystem auf einen Punkt abgebildet werden." + "\n"
				+ "Es ist nicht sofort ersichtlich, hinter welchem Punkt in der Abbildung sich also nur ein Datum"
				+ "\n" + "befindet und hinter welchem sich mehrere Daten befinden. Um dem entgegenzutreten werden die"
				+ "\n"
				+ "diskreten Datenwerte nun absichtlich verrauscht, indem eine kleine, zufällige, positive oder negative"
				+ "\n" + "Abweichung beigefügt wird." + "\n"
				+ "Neben ursprünglich diskreten Datenwerten eignen sich auch künstlich diskretisierte, bspw. \n"
				+ "gerundetete Datenwerte gut um mittels Jitter Overplotting zu vermeiden. Vorraussetzung ist aber, dass \n"
				+ "genug Raum um die Datenpunkte ist damit es durch Jitter nicht zum erneuten Overplotting kommt.";

		motivation = lang.newSourceCode(new Coordinates(50, 60), "mymotiv", null);
		motivation.addMultilineCode(motiv, "0", null);

		lang.nextStep("Motivation");
		motivation.hide();

		descriptionP = new TextProperties();
		descriptionP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		descriptionP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		description = lang.newText(new Coordinates(50, 60), "", "Description", null, descriptionP);

		lang.nextStep("Einleitung");

		addSC();

		// lang.nextStep();

		mysc.highlight(0);
		description.setText("Nacheinander werden alle Datenpunkte abgearbeitet.", null, null);
		lang.nextStep();
		mysc.unhighlight(0);

		mysc.highlight(1);
		description.setText("Mittels eines (Pseudo-)Zufallszahlengenerators wird eine Zufallszahl bestimmt.", null,
				null);
		lang.nextStep();
		mysc.unhighlight(1);

		mysc.highlight(2);
		description.setText("Diese Zufallszahl wird auf den Bereich [-R;+R] normiert.", null, null);
		lang.nextStep();
		mysc.unhighlight(2);

		mysc.highlight(3);
		description.setText("Der normierte Wert wird zum Wert der x-Koordinaten addiert.", null, null);
		lang.nextStep();
		mysc.unhighlight(3);

		mysc.highlight(4);
		description.setText("Zufallszahlerzeugung, Normierung und Addition werden auch für den y-Wert ausgeführt.",
				null, null);
		lang.nextStep();
		mysc.unhighlight(4);

		mysc.highlight(5);
		description.setText("Im Koordinatensystem wird der entsprechende Punkt an die neuen Koordinaten bewegt.", null,
				null);
		lang.nextStep();
		mysc.unhighlight(5);
		description.hide();

	}

	public static void firstdrawCS(Student[] IN_Studs) {
		label1 = lang.newText(new Coordinates(20, 30), "Aktueller Punkt: -", "MyLabel", null, actDot);

		Coordinates uL = new Coordinates(origin.getX() - 30, origin.getY() - size - 10);
		Coordinates lR = new Coordinates(origin.getX() + size + 10, origin.getY() + 30);

		// Properties f�r das Rect erstellen
		RectProperties RectP = new RectProperties();
		RectP.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		RectP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		lang.newRect(uL, lR, null, null, RectP);

		Coordinates[] NxAxis = { origin, eox };
		Coordinates[] NyAxis = { origin, eoy };

		Polyline xaxis = lang.newPolyline(NxAxis, "x-Achse", null);
		Polyline yaxis = lang.newPolyline(NyAxis, "y-Achse", null);

		// Achsenbeschriftung
		for (int i = 0; i <= size; i++) {
			if (i % step == 0) {
				// x-Achse beschriften
				Coordinates temp1 = new Coordinates(origin.getX() + i, origin.getY() - 5);
				Coordinates temp2 = new Coordinates(origin.getX() + i, origin.getY() + 5);
				Coordinates[] temp3 = { temp1, temp2 };
				lang.newPolyline(temp3, null, null);

				Coordinates temp4 = new Coordinates(origin.getX() + (i - 3), origin.getY() + 10);

				lang.newText(temp4, "" + (i / step), null, null);

				// y-Achse beschriften
				temp1 = new Coordinates(origin.getX() + 5, origin.getY() - i);
				temp2 = new Coordinates(origin.getX() - 5, origin.getY() - i);
				Coordinates[] temp5 = { temp1, temp2 };
				lang.newPolyline(temp5, null, null);

				temp4 = new Coordinates(origin.getX() - 15, origin.getY() - (i + 8));

				lang.newText(temp4, "" + (i / step), null, null);

			}

		}
		lang.nextStep();

		// Punkte einzeichnen � als kleine Kreise
		for (int i = 0; i < IN_Studs.length; i++) {

			Coordinates currentPoint = new Coordinates(origin.getX() + (int) (IN_Studs[i].gradeICS1 * step),
					origin.getY() - (int) (IN_Studs[i].gradeICS2 * step));

			// CircleProperties currentPointP = new CircleProperties();
			// currentPointP.set(AnimationPropertiesKeys.FILL_PROPERTY,
			// Color.GRAY);
			// currentPointP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

			points[i] = lang.newCircle(currentPoint, 3, "Punkt " + (i + 1), null, DotInCS);

		}
		lang.nextStep("Erste Iteration");

		MultipleSelectionQuestionModel distribution = new MultipleSelectionQuestionModel("dist");
		distribution.setPrompt("Gibt es mehr gute oder schlechte Studenten im Jahrgang?");
		distribution.addAnswer("Gute Studenten", 2, "Du hast den richtigen Riecher.");
		distribution.addAnswer("weder/noch", 1, "Diese Schlussfolgerung kann man auch aus dem Diagramm ableiten.");
		distribution.addAnswer("Schlechte Studenten", 1,
				"Diese Schlussfolgerung kann man auch aus dem Diagramm ableiten.");
		distribution.setNumberOfTries(1);
		lang.addMSQuestion(distribution);
		lang.nextStep();
	}

	public static void redrawCS(Student[] IN_Studs) {

		dataPoint = lang.newText(new Coordinates(origin.getX() + size + 50, origin.getY() - size - 50), "", "dataPoint",
				null);

		// Punkte neu zeichnen � als kleine Kreise
		for (int i = 0; i < IN_Studs.length; i++) {

			Coordinates targetPoint = new Coordinates(origin.getX() + (int) (IN_Studs[i].gradeICS1 * step),
					origin.getY() - (int) (IN_Studs[i].gradeICS2 * step));

			label1.setText("Aktueller Punkt: ( " + gradeList_Before[i].toString() + " ) " + "- > ( "
					+ IN_Studs[i].toString() + " )", null, null);
			mysc.highlight(0);
			// points[i].show();
			dataPoint.setText("Nächster Datenpunkt: ( " + gradeList_Before[i].toString() + " ) ", null, null);

			points[i] = lang.newCircle(points[i].getCenter(), points[i].getRadius(), points[i].getName(), null,
					DotInCS);
			points[i].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW, null, null);

			lang.nextStep();
			mysc.unhighlight(0);

			mysc.highlight(1);
			dataPoint.setText("Zufallszahl: " + f.format(randoms[i][0]), null, null);
			lang.nextStep();
			mysc.unhighlight(1);

			mysc.highlight(2);
			dataPoint.setText("(" + f.format(randoms[i][0]) + " - 0.5) * " + Radius_R + " = "
					+ f.format((randoms[i][0] - 0.5) * Radius_R), null, null);
			lang.nextStep();
			mysc.unhighlight(2);

			mysc.highlight(3);
			dataPoint.setText(f.format(gradeList_Before[i].gradeICS1) + " + "
					+ f.format((randoms[i][0] - 0.5) * Radius_R) + " = " + f.format(gradeList_After[i].gradeICS1), null,
					null);
			lang.nextStep();
			mysc.unhighlight(3);

			// repeat for y-Value
			mysc.highlight(4);
			dataPoint.setText("Zufallszahl: " + f.format(randoms[i][1]), null, null);
			lang.nextStep();
			dataPoint.setText("(" + f.format(randoms[i][1]) + " - 0.5) * " + Radius_R + " = "
					+ f.format((randoms[i][1] - 0.5) * Radius_R), null, null);
			lang.nextStep();
			dataPoint.setText(f.format(gradeList_Before[i].gradeICS2) + " + "
					+ f.format((randoms[i][1] - 0.5) * Radius_R) + " = " + f.format(gradeList_After[i].gradeICS2), null,
					null);
			lang.nextStep();
			mysc.unhighlight(4);

			mysc.highlight(5);
			dataPoint.setText("", null, null);
			lang.nextStep();

			points[i].moveTo(AnimalScript.DIRECTION_C, "translate", targetPoint, null, null);

			lang.nextStep(); // n�chster Animationsschritt: Highlight entfernen
			mysc.unhighlight(5);

			points[i].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, DotInCSFC, null, null);

		}

	}

	public static void addSC() {

		mysc = lang.newSourceCode(new Coordinates(origin.getX() + size + 50, origin.getY() - size), "sc", null,
				sourceCodeP);
		mysc.addCodeLine("Wähle den nächsten Datenpunkt aus.", "", 0, null);
		mysc.addCodeLine("Erzeuge eine Zufallszahl zwischen 0 und 1.", "", 0, null);
		mysc.addCodeLine("Subtrahiere 0.5 und multipliziere mit dem Radius R.", "", 0, null);
		mysc.addCodeLine("Addiere diesen Wert zum alten x-Wert.", "", 0, null);
		mysc.addCodeLine("Wiederhole das Ganze für den y-Wert.", "", 0, null);
		mysc.addCodeLine("Verrücke den Datenpunkt entsprechend der neuen Koordinaten.", "", 0, null);
	}

	static void copyArray(Student[] IN_Stud, Student[] OUT_Stud) {
		for (int i = 0; i < IN_Stud.length; i++) {
			OUT_Stud[i] = IN_Stud[i];
		}
	}

	static void jitterArray(Student[] IN_Array) {
		randoms = new double[IN_Array.length][2];
		for (int i = 0; i < IN_Array.length; i++) {
			randoms[i][0] = Math.random();
			randoms[i][1] = Math.random();
			IN_Array[i] = new Student((IN_Array[i].gradeICS1 + (randoms[i][0] - 0.5) * Radius_R),
					(IN_Array[i].gradeICS2 + ((randoms[i][1] - 0.5) * Radius_R)));
		}
	}

	static double jitter(double IN_d) {
		return (IN_d + ((Math.random() - 0.5) * Radius_R));

	}

	public String getName() {
		return "Jitter";
	}

	public String getAlgorithmName() {
		return "JITTER";
	}

	public String getAnimationAuthor() {
		return "Patrick Weber";
	}

	public String getDescription() {
		return "Eine Möglichkeit, zweidimensionale Daten zu visualisieren, ist die Anordnung dieser Daten " + "\n"
				+ "in einem Koordinatensystem. Sind die zugrundeliegenden Datenwerte allerdings diskret, wie" + "\n"
				+ "beispielsweise Noten an der TU Darmstadt, so kommt es bereits bei relativ kleinen Datenmengen\n"
				+ "zum Overplotting. Das bedeutet, dass zwei verschiedene Daten, mit (zufällig) denselben\n"
				+ "Datenwerten im Koordinatensystem auf einen Punkt abgebildet werden." + "\n"
				+ "Es ist nicht sofort ersichtlich, hinter welchem Punkt in der Abbildung sich also nur ein Datum"
				+ "\n" + "befindet und hinter welchem sich mehrere Daten befinden. Um dem entgegenzutreten werden die"
				+ "\n"
				+ "diskreten Datenwerte nun absichtlich verrauscht, indem eine kleine, zufällige positive oder negative"
				+ "\n" + "Abweichung beigefügt wird.";
	}

	public String getCodeExample() {
		return "Wähle den nächsten Datenpunkt aus." + "\n" + "Erzeuge eine Zufallszahl zwischen 0 und 1." + "\n"
				+ "Subtrahiere 0.5 und multipliziere mit dem Radius R." + "\n" + "Addiere diesen Wert zum alten x-Wert."
				+ "\n" + "Wiederhole das Ganze für den y-Wert." + "\n"
				+ "Verrücke den Datenpunkt entsprechend der neuen Koordinaten.";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		// Radius_R gets checked by ANIMAL
		// Prüfen der Array-Eingaben in IN_stringArray
		try {
			IN_stringArray = (String[]) primitives.get("IN_stringArray");
			for (int i = 0; i < IN_stringArray.length; i++) {
				IN_stringArray[i].replaceAll("\\s+", "");
				if (!IN_stringArray[i]
						.matches("\\(([1-3]\\.[037]|(4\\.0)|(5\\.0))\\|([1-3]\\.[037]|(4\\.0)|(5\\.0))\\)"))
					return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}