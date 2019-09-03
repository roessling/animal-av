/*
 * roundrobin.java
 * Anja Kirchhöfer, Ben Kohr, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArcProperties;
import algoanim.properties.ListElementProperties;
import algoanim.properties.SourceCodeProperties;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.roundrobin.RoundRobinGenerator;

public class RoundRobin {
	private Language lang;

	// true -> Englisch, false -> Deutsch
	private boolean language;

	// true -> Pseudocode, false -> Java
	private boolean codeLanguage;

	private Color processColor1;

	private Color processColor2;

	private Color processColor3;

	private Color processColor4;

	private Color processColor5;

	private Color processColor6;

	private int processArrayLength;

	private int colorEffectDuration;

	private Color sCColor;

	private Color sCHighlightColor;

	private int sCFontSize;

	private boolean sCBold;

	private boolean sCItalic;

	private String sCFont;

	private int k;

	private boolean infinityMode;

	private int exampleNumber;

	private String[][] processes;

	private int probGeneral;

	private int probSpecific;



	public void init() {
		lang = new AnimalScript("Round-Robin-Scheduling", "Anja Kirchhöfer, Ben Kohr", 800, 600);
	}


	public RoundRobin(boolean language, boolean codeLanguage) {
		this.language = language;
		this.codeLanguage = codeLanguage;
	}


	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		processColor1 = (Color) ((ArcProperties) props.getPropertiesByName((language) ? "Color of the 1. process" : "Farbe des 1. Prozesses"))
				.get("fillColor");
		processColor2 = (Color) ((ArcProperties) props.getPropertiesByName((language) ? "Color of the 2. process" : "Farbe des 2. Prozesses"))
				.get("fillColor");
		processColor3 = (Color) ((ArcProperties) props.getPropertiesByName((language) ? "Color of the 3. process" : "Farbe des 3. Prozesses"))
				.get("fillColor");
		processColor4 = (Color) ((ArcProperties) props.getPropertiesByName((language) ? "Color of the 4. process" : "Farbe des 4. Prozesses"))
				.get("fillColor");
		processColor5 = (Color) ((ArcProperties) props.getPropertiesByName((language) ? "Color of the 5. process" : "Farbe des 5. Prozesses"))
				.get("fillColor");
		processColor6 = (Color) ((ArcProperties) props.getPropertiesByName((language) ? "Color of the 6. process" : "Farbe des 6. Prozesses"))
				.get("fillColor");

		processArrayLength = (int) ((ListElementProperties) props
				.getPropertiesByName((language) ? "Length of the process array" : "Länge des Prozess-Arrays")).get("position");
		colorEffectDuration = (int) ((ListElementProperties) props
				.getPropertiesByName((language) ? "Duration of the color effect" : "Länge des Farb-Effekts")).get("position");

		k = (Integer) primitives.get((language) ? "Time slice (k)" : "Zeitscheibe (k)");
		infinityMode = (Boolean) primitives.get((language) ? "Infinity mode" : "Unendlichkeits-Modus");
		exampleNumber = (Integer) primitives.get((language) ? "Example number" : "Beispielnummer");

		sCColor = (Color) ((SourceCodeProperties) props.getPropertiesByName((language) ? "Code properties" : "Code-Einstellungen")).get("color");
		sCHighlightColor = (Color) ((SourceCodeProperties) props.getPropertiesByName((language) ? "Code properties" : "Code-Einstellungen"))
				.get("highlightColor");
		sCFontSize = (int) ((SourceCodeProperties) props.getPropertiesByName((language) ? "Code properties" : "Code-Einstellungen")).get("size");
		sCBold = (boolean) ((SourceCodeProperties) props.getPropertiesByName((language) ? "Code properties" : "Code-Einstellungen")).get("bold");
		sCItalic = (boolean) ((SourceCodeProperties) props.getPropertiesByName((language) ? "Code properties" : "Code-Einstellungen")).get("italic");
		sCFont = ((Font) ((SourceCodeProperties) props.getPropertiesByName((language) ? "Code properties" : "Code-Einstellungen")).get("font"))
				.getName();

		processes = (String[][]) primitives.get((language) ? "Custom processes" : "Eigene Prozesse");

		probGeneral = (Integer) primitives.get((language) ? "Probability for general questions" : "Wahrscheinlichkeit für allgemeine Fragen");
		probSpecific = (Integer) primitives.get((language) ? "Probability for specific questions" : "Wahrscheinlichkeit für spezifische Fragen");

		RoundRobinGenerator roundRobin;
		try {

			roundRobin = new RoundRobinGenerator(lang, language, codeLanguage, k, exampleNumber, infinityMode, processes, processArrayLength,
					colorEffectDuration, processColor1, processColor2, processColor3, processColor4, processColor5, processColor6, sCColor,
					sCHighlightColor, sCFontSize, sCBold, sCItalic, sCFont, probGeneral, probSpecific);

			roundRobin.execute(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lang.toString();
	}


	public String getName() {
		if (language) {
			return "Round Robin Scheduling";
		} else {
			return "Round-Robin-Scheduling";
		}

	}


	public String getAlgorithmName() {
		if (language) {
			return "Round Robin Scheduling";
		} else {
			return "Round-Robin-Scheduling";
		}
	}


	public String getAnimationAuthor() {
		return "Anja Kirchhöfer, Ben Kohr";
	}


	public String getDescription() {

		if (language) {
			return "Round Robin Scheduling is a scheduling technique which grants each process a fixed time slide" + "\n"
					+ "on the processor. Processes are executed after each other in their given time slice, then the" + "\n"
					+ "next process is to be executed." + "\n" + "" + "\n"
					+ "Please note: This generated animations are adjusted for a resolution of 1366 x 768 pixels. If the animations" + "\n"
					+ "are very small or too large to be completely displayed on the screen and your PC has a different resolution," + "\n"
					+ "consider to temporarily switch to the above resolution. Or instead, you can adjust the scaling in the" + "\n"
					+ "Animal animation window. If your PC has a Full HD resolution (1920 x 1080), a scaling of 150% should" + "\n"
					+ "yield good results.";
		} else {
			return "Der Round-Robin-Algorithmus ist eine Scheduling-Technik, bei der jedem Prozess eine Zeitscheibe" + "\n"
					+ "fester Länge zusteht. Prozesse werden nacheinander für die zur Verfügung stehende Zeit bearbeitet," + "\n"
					+ "danach kommt der nächste Prozess an die Reihe." + "\n" + "" + "\n"
					+ "Anmerkung: Die generierten Animationen sind für eine Auflösung von 1366 x 768 Pixeln zugeschnitten. Falls" + "\n"
					+ "sie sehr klein oder zu groß für die Darstellung auf dem Bildschirm sind und dein PC eine andere" + "\n"
					+ "Auflösung hat, kann es helfen, vorübergehend zu der obigen Auflösung zu wechseln. Stattdessen kannst du" + "\n"
					+ "auch die Skalierung im Animationsfenster von Animal anpassen. Wenn dein PC eine Full-HD-Auflösung" + "\n"
					+ "(1920 x 1080) hat, sollte eine Skalierung von 150% gute Ergebnisse liefern.";

		}


	}


	public String getCodeExample() {

		if (language && codeLanguage) {
			return "procedure 'Round Robin Scheduling'" + "\n" + "\n" + "        do forever {" + "\n" + "                if (queue is empty)	"
					+ "\n" + "                        wait 1 time unit;	" + "\n" + "                else" + "\n"
					+ "                        slice <- k;" + "\n" + "                        first <- first process in queue;		" + "\n"
					+ "                        while (slice > 0 && first is not finished) { 		" + "\n"
					+ "                                execute first for 1 time unit;" + "\n" + "                                decrement slice;"
					+ "\n" + "                        }" + "\n" + "                        if (first is finished)" + "\n"
					+ "                                remove first from the queue;" + "\n" + "                        else" + "\n"
					+ "                                rotate the queue;" + "\n" + "        }";

		} else if (language && !codeLanguage) {
			return "public roundRobin(List<Process> queue) {" + "\n" + "\n" + "        while (true) {" + "\n"
					+ "                if (queue.isEmpty()) {	" + "\n" + "                        wait(1);	" + "\n" + "                } else {"
					+ "\n" + "                        int slice = k;" + "\n" + "                        Process first = queue.getFirst();		"
					+ "\n" + "                        while (slice > 0 || !first.finished()) { 		" + "\n"
					+ "                                execute(first, 1);" + "\n" + "                                slice = slice - 1;" + "\n"
					+ "                        }" + "\n" + "                        if (first.finished()) {" + "\n"
					+ "                                remove(queue, first);" + "\n" + "                        } else {" + "\n"
					+ "                                rotate(queue);" + "\n" + "                        }" + "\n" + "                }		" + "\n"
					+ "        }" + "\n" + "} ";

		} else if (!language && codeLanguage) {
			return "Prozedur Round-Robin-Scheduling" + "\n" + "\n" + "        tue für immer {" + "\n"
					+ "                falls (Warteschlange ist leer)	" + "\n" + "                        warte eine Zeiteinheit lang;	" + "\n"
					+ "                sonst" + "\n" + "                        zeitscheibe <- k;" + "\n"
					+ "                        erster <- erster Prozess in der Warteschlange;			" + "\n"
					+ "                        solange (zeitscheibe > 0 && !(erster beendet)) { 		" + "\n"
					+ "                                führe erster eine Zeiteinheit lang aus;" + "\n"
					+ "                                dekrementiere zeitscheibe;" + "\n" + "                        }" + "\n"
					+ "                        falls (erster beendet)" + "\n"
					+ "                                entferne erster aus der Warteschlange;" + "\n" + "                        sonst" + "\n"
					+ "                                rotiere die Prozesse in der Warteschlange;" + "\n" + "        }" + "\n";
		} else {
			return "public void roundRobin(Warteschlange ws) {" + "\n" + "\n" + "        while (true) {" + "\n"
					+ "                if (ws.istLeer()) {	" + "\n" + "                        warte(1);	" + "\n" + "                } else {"
					+ "\n" + "                        int zeitscheibe = k;" + "\n"
					+ "                        Prozess erster = ws.ersterProzess();		" + "\n"
					+ "                        while (zeitscheibe > 0 && !erster.beendet()) { 		" + "\n"
					+ "                                fuehreAus(erster, 1);" + "\n"
					+ "                                zeitscheibe = zeitscheibe - 1;" + "\n" + "                        }" + "\n"
					+ "                        if (erster.beendet()) {" + "\n" + "                                enferne(ws, erster);" + "\n"
					+ "                        } else {" + "\n" + "                                rotiere(ws);" + "\n" + "                        }"
					+ "\n" + "                }		" + "\n" + "        }" + "\n" + "} ";
		}
	}


	public String getFileExtension() {
		return "asu";
	}


	public Locale getContentLocale() {
		if (language) {
			return Locale.ENGLISH;
		} else {
			return Locale.GERMAN;
		}
	}


	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}


	public String getOutputLanguage() {
		if (codeLanguage) {
			return Generator.PSEUDO_CODE_OUTPUT;
		} else {
			return Generator.JAVA_OUTPUT;
		}
	}


	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {

		int processArrayLength = (int) ((ListElementProperties) props
				.getPropertiesByName((language) ? "Length of the process array" : "Länge des Prozess-Arrays")).get("position");
		int k = (Integer) primitives.get((language) ? "Time slice (k)" : "Zeitscheibe (k)");
		int exampleNumber = (Integer) primitives.get((language) ? "Example number" : "Beispielnummer");
		String[][] processes = (String[][]) primitives.get((language) ? "Custom processes" : "Eigene Prozesse");
		int probGeneral = (Integer) primitives.get((language) ? "Probability for general questions" : "Wahrscheinlichkeit für allgemeine Fragen");
		int probSpecific = (Integer) primitives.get((language) ? "Probability for specific questions" : "Wahrscheinlichkeit für spezifische Fragen");


		// ------------

		if (processArrayLength < 9) {
			return false;
		}

		if (k < 0) {
			return false;
		}

		if (exampleNumber < 1 || exampleNumber > 4) {
			return false;
		}

		if (probGeneral < 0 || probGeneral > 100) {
			return false;
		}

		if (probSpecific < 0 || probSpecific > 100) {
			return false;
		}

		if (exampleNumber == 4) {

			if (processes.length > 6 || processes[0].length > 3) {
				return false;
			}

			LinkedList<String> names = new LinkedList<String>();
			for (int i = 0; i < processes.length; i++) {

				if (processes[i][0].isEmpty() || processes[i][1].isEmpty() || processes[i][2].isEmpty()) {
					return false;
				}

				if (!processes[i][1].matches("[0-9]+") || !processes[i][2].matches("[0-9]+")) {
					return false;
				}

				if (names.contains(processes[i][0])) {
					return false;
				} else {
					names.add(processes[i][0]);
				}
			}

		}

		return true;
	}

}