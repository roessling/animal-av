
/*
 * mooresAlgorithm.java
 * Niklas St�hr, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class MooresAlgorithm implements ValidatingGenerator {
	private Language lang;
	//private ArrayProperties arrayProps;
	private MatrixProperties matrixProps;
	private Integer textSize;
	private Integer sourceCodeTextSize;
	private TextProperties textProps;
	private SourceCodeProperties sourceCodeProps;

	public void init() {
		lang = new AnimalScript("Moores Algorithmus", "Niklas St�hr", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		//arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		int[][] Jobs = (int[][]) primitives.get("jobMatrix");
		matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
		textSize = (Integer) primitives.get("textSize");
		sourceCodeTextSize = (Integer) primitives.get("sourceCodeTextSize");
		textProps = (TextProperties) props.getPropertiesByName("textProps");
		sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
		lang.setStepMode(true);
		sort(Jobs);
		return lang.toString();
	}

	private void sort(int[][] jobs) {
		int[][] initJobs = jobs;
		// create Textproperties
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, textSize));
		textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

		// create Title
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, textSize + 6));
		titleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		Text title = lang.newText(new Coordinates(20, 20), "Moores Algorithmus", "Title", null, titleProps);

		// create Background element:
		RectProperties boxProps = new RectProperties();
		boxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		boxProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		boxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		Rect box = lang.newRect(new Offset(-5, -5, title, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, title, AnimalScript.DIRECTION_SE), "box", null);

		// DO INTRO SLIDE
		String[] IntroStrings = { "Moores Algorithmus ist ein Algorithmus zur Ablaufplanung von Jobs.",
				"Er bestimmt fuer eine gegebene Sequenz von Jobs, die optimale Reihenfolge der Bearbeitung.",
				"Der Algorithmus minimiert dabei die Anzahl der verspaeteten Jobs.",
				"Dies ist insbesondere in der Praxis relevant, da haeufig Strafzahlungen pro verspaetetem Auftrag anfallen.",
				"Unter dieser Erklaerung ist, die in diesem Beispiel gegebene, Matrix mit den zu planenden Jobs zu sehen.",
				"In der ersten Spalte findet sich die Jobnummer, dann der Termin in Spalte 2 und in Spalte 3 die Bearbeitungszeit.",
				"In der letzten Spalte findet sich der Termin der Fertigstellung des Jobs, wenn diese in der Reihenfolge von Zeile 1 nach unten bearbeitet werden.",
				String.format("In dem ausgewaehlten Beispiel gibt es demnach %s Jobs, die zu planen sind.",
						initJobs[0].length),
				" ", "Moores Algorithmus berechnet zuerst die Fertigsstellungstermine fuer die initiale Reihenfolge.",
				"Danach werden die Job aufsteigend nach ihrem Fertigstellungstermins sortiert.",
				"Nun sucht der Algorithmus den ersten verspaeteten Job, d.h. Spalte 2 ist kleiner als Spalte 4.",
				"In der Sequenz des bis zu dem ersten verspaeteten Job, wird im Anschluss der Auftrag mit der hoechsten Bearbeitungszeit (Spalte 3) entfernt.",
				"Nun wird wieder der erste verspaetete Job gesucht, dies wiederholt sich bis kein verspaeteter Job mehr gefunden werden kann.",
				"Die finale Reihenfolge ensteht, indem die entfernten Jobs an die uebrig gebliebenen Jobs angehaengt werden."

		};
		Text[] introExpl = new Text[IntroStrings.length];
		for (int i = 0; i < IntroStrings.length; i++) {
			Offset pos = null;
			if (i == 0) {
				pos = new Offset(0, 20, box, AnimalScript.DIRECTION_SW);
			} else {
				pos = new Offset(0, 0, introExpl[i - 1], AnimalScript.DIRECTION_SW);
			}
			introExpl[i] = lang.newText(pos, IntroStrings[i], "Title", null, textProps);
		}

		// CREATE JOBMATRIX
		MatrixProperties matProps = matrixProps;
		// now, create the IntMatrix object, linked to the properties
		IntMatrix jobMat = lang.newIntMatrix(
				new Offset(0, 20, introExpl[IntroStrings.length - 1], AnimalScript.DIRECTION_SW), initJobs, "jobMat",
				null, matProps);

		lang.nextStep("Start");

		// HIDE INTRO
		for (Text text : introExpl) {
			text.hide();
		}

		jobMat.hide();

		// SHOW NEW JOBMATRIX
		Text jobMatTitle = lang.newText(new Offset(0, 20, title, AnimalScript.DIRECTION_SW), "Jobmatrix:", "matTitle",
				null, textProps);
		jobMat = lang.newIntMatrix(new Offset(0, 5, jobMatTitle, AnimalScript.DIRECTION_SW), initJobs, "jobMat", null,
				matProps);

		// CREATE SOURCE CODE
		SourceCodeProperties scProps = sourceCodeProps;
		// scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, sourceCodeTextSize));
		// scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		// scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc = lang.newSourceCode(new Offset(50, 0, jobMat, AnimalScript.DIRECTION_NE), "sourceCode", null,
				scProps);

		// ADD LINES TO SOURCE CODE
		sc.addCodeLine("1.a Sortiere die Jobs nach ihrem Termin. (Spalte 2)", null, 0, null); // 0
		sc.addCodeLine("1.b Berechne die Fertigstellungszeitpunkte der Jobs (Spalte 4).", null, 0, null); // 1
		sc.addCodeLine("2. Finde den ersten verspaeteten Job [i]. Existiert keiner, gehe zu Schritt 4.", null, 0, null); // 2
		sc.addCodeLine("3. Entferne den Job mit der groessten Bearbeitungszeit aus [0] ... [i]. Gehe zu 2.", null, 0,
				null); // 3
		sc.addCodeLine("4. Fuege die entfernten Auftraege an das Ende an.", null, 0, null); // 4

		lang.nextStep("1a. Schritt");

		// Sort jobs by ascending order of the second row, which are the due dates --
		// jobs[i][1]
		sc.highlight(0);
		lang.nextStep();
		int[][] sortedJobs = initJobs.clone();
		Arrays.sort(sortedJobs, new Comparator<int[]>() {
			@Override
			public int compare(int[] d1, int[] d2) {
				int d1Date = d1[1];
				int d2Date = d2[1];
				return d1Date - d2Date;
			}
		});
		// PRESENT SORTED JOBMATRIX
		jobMat.hide();
		jobMat = lang.newIntMatrix(jobMat.getUpperLeft(), sortedJobs, "jobMat", null, matProps);
		highlightColumn(jobMat, 1, true);
		lang.nextStep("1b. Schritt");
		highlightColumn(jobMat, 1, false);
		sc.unhighlight(0);

		// CALCULATE FINISH TIMES
		sc.highlight(1);
		lang.nextStep();
		highlightColumn(jobMat, 3, true);
		calculateFinishDates(jobMat);
		lang.nextStep("2. Schritt");
		highlightColumn(jobMat, 3, false);
		sc.unhighlight(1);

		// as long as there are late jobs in the sequence
		int rJcounter = 0;
		int[][] removed = null;
		// ADD TITLE FOR REMOVED JOB MATRIX
		Text removedTitle = null;
		IntMatrix removedMat = null;

		while (true) {
			sc.highlight(2);
			lang.nextStep();
			// Find first late job in the sequence
			int late = -1;
			for (int i = 0; i < jobMat.getNrRows(); i++) {
				jobMat.highlightCell(i, 1, null, null);
				jobMat.highlightCell(i, 3, null, null);
				String compTxt = String.format("%s < %s  -> ", jobMat.getElement(i, 1), jobMat.getElement(i, 3));
				if (jobMat.getElement(i, 1) < jobMat.getElement(i, 3)) {
					compTxt += " verspaetet.";
				} else {
					compTxt += " puenktlich.";
				}
				Text comp = lang.newText(new Offset(0, 20, sc, AnimalScript.DIRECTION_SW), compTxt, "comp", null,
						textProps);
				lang.nextStep();
				if (jobMat.getElement(i, 3) > jobMat.getElement(i, 1)) {
					late = i;					
					comp.hide();
					break;
				}
				jobMat.unhighlightCell(i, 1, null, null);
				jobMat.unhighlightCell(i, 3, null, null);
				comp.hide();
			}
			if (late == -1) {
				// no late jobs, stop the alg
				break;
			}
			highlightRow(jobMat, late);

			lang.nextStep("3. Schritt");
			sc.unhighlight(2);
			sc.highlight(3);

			// remove job with highest working time (job[i][2]) from 0...i with i as the
			// position of late.
			int workT = 0;
			int jobWithHighestWorkT = 0;
			// first we have to find the job with the longest time requirement
			for (int i = 0; i <= late; i++) {
				if (jobMat.getElement(i, 2) > workT) {
					workT = jobMat.getElement(i, 2);
					jobWithHighestWorkT = i;
				}
			}
			unhighlightRow(jobMat, late);
			highlightRow(jobMat, jobWithHighestWorkT);
			// then we remove this job from the sequence.
			int[][] oldremoved = null;
			if (removed != null) {
				oldremoved = removed;
			}
			removed = new int[rJcounter + 1][jobMat.getNrCols()];
			if (oldremoved != null) {
				for (int i = 0; i < oldremoved.length; i++) {
					removed[i] = oldremoved[i];
				}
			}
			int[][] newArray = new int[jobMat.getNrRows() - 1][jobMat.getNrCols()];
			int c = 0;
			for (int i = 0; i < jobMat.getNrRows(); i++) {
				if (!(i == jobWithHighestWorkT)) {
					for (int j = 0; j < jobMat.getNrCols(); j++) {
						newArray[c][j] = jobMat.getElement(i, j);
					}
					c++;
				} else {
					for (int j = 0; j < jobMat.getNrCols(); j++) {
						if (j != jobMat.getNrCols() - 1) {
							removed[rJcounter][j] = jobMat.getElement(i, j);
						} else {
							removed[rJcounter][j] = 0;
						}
					}
				}
			}

			// display new jobs sequence with removed job
			lang.nextStep();

			unhighlightRow(jobMat, jobWithHighestWorkT);
			unhighlightRow(jobMat, late);

			jobMat.hide();
			jobMat = lang.newIntMatrix(new Offset(0, 5, jobMatTitle, AnimalScript.DIRECTION_SW), newArray, "jobMat",
					null, matProps);
			if (removedTitle != null) {
				removedTitle.hide();
			}
			
			removedTitle = lang.newText(new Offset(0, 20, jobMat, AnimalScript.DIRECTION_SW), "Entfernte Jobs:",
					"R1Title", null, textProps);
			if (removedMat != null) {
				removedMat.hide();
			}			
			removedMat = lang.newIntMatrix(new Offset(0, 5, removedTitle, AnimalScript.DIRECTION_SW), removed, "r1",
					null, matProps);

			// and calculate the finish dates for the new sequence.
			calculateFinishDates(jobMat);
			lang.nextStep();
			sc.unhighlight(3);
			rJcounter++;
		}
		
		// Calculate the final sequence by adding the removedJobs at the end.
		lang.nextStep("4. Schritt");
		sc.unhighlight(2);
		sc.highlight(4);
		lang.nextStep();
		int[][] finalSequence = new int[jobs.length][4];
		for (int i = 0; i < finalSequence.length; i++) {
			if (i < jobMat.getNrRows()) {
				for (int j = 0; j < jobMat.getNrCols(); j++) {
					finalSequence[i][j] = jobMat.getElement(i, j);
				}
			} else {
				for (int j = 0; j < jobMat.getNrCols(); j++) {
					finalSequence[i][j] = removed[i - jobMat.getNrRows()][j];
				}
			}
		}
		jobMat.hide();
		jobMat = lang.newIntMatrix(new Offset(0, 5, jobMatTitle, AnimalScript.DIRECTION_SW), finalSequence, "jobMat",
				null, matProps);
		if (removedMat != null) {
			removedMat.hide();
			removedTitle.hide();
		}
		
		// calculate finish dates for the final sequence.
		calculateFinishDates(jobMat);

		// CREATE OUTRO
		lang.nextStep("Outro");
		ArrayList<Primitive> keep = new ArrayList<Primitive>();
		keep.add(title);
		keep.add(box);
		keep.add(jobMat);
		lang.hideAllPrimitivesExcept(keep);

		String finSeqString = "";
		for (int i = 0; i < jobMat.getNrRows(); i++) {
			finSeqString += jobMat.getElement(i, 0);
			if (i < jobMat.getNrRows() - 1) {
				finSeqString += ", ";
			}
		}

		// SHOW INTIAL JOBS IN OUTRO
		Text initJobsTitle = lang.newText(new Offset(0, 20, box, AnimalScript.DIRECTION_SW), "Initiale Jobmatrix: ",
				"initMatTitle", null, textProps);
		IntMatrix initMat = lang.newIntMatrix(new Offset(0, 5, initJobsTitle, AnimalScript.DIRECTION_SW), initJobs,
				"InitMat", null, matProps);
		calculateFinishDates(initMat);
		Text finMatTitle = lang.newText(new Coordinates(0, 0), "Finale Jobmatrix: ", "finMatTitle", null, textProps);
		finMatTitle.moveTo(AnimalScript.DIRECTION_SW, "translate",
				new Offset(75, -25, initMat, AnimalScript.DIRECTION_NE), null, null);
		jobMat.moveTo(AnimalScript.DIRECTION_NW, "translate", new Offset(75, 0, initMat, AnimalScript.DIRECTION_NE),
				null, null);

		PolylineProperties arrows = new PolylineProperties();
		arrows.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		Node[] nodes = { new Offset(10, 10, initMat, AnimalScript.DIRECTION_NE),
				new Offset(55, 10, initMat, AnimalScript.DIRECTION_NE) };
		Polyline arrow = lang.newPolyline(nodes, "move", null, arrows);

		int initLate = 0;
		for (int i = 0; i < initMat.getNrRows(); i++) {
			if (initMat.getElement(i, 1) < initMat.getElement(i, 3)) {
				initLate += 1;
				highlightRow(initMat, i);
				for (int j = 0; j < initMat.getNrCols(); j++) {
					//
				}
			}
		}

		for (int i = 0; i < jobMat.getNrRows(); i++) {
			if (jobMat.getElement(i, 1) < jobMat.getElement(i, 3)) {
				highlightRow(jobMat, i);
			}
		}
		String[] conclStrings = {
				String.format("Die finale Sequenz enthaelt die Jobs in der Reihenfolge (%s).", finSeqString),
				"Die verspaeteten Jobs sind jeweils farbig hervorgehoben.",
				String.format("In der urspruenglichen Reihenfolge sind %s Jobs verspaetet.", initLate),
				String.format("Es wurden %s Jobs entfernt, diese verbleiben verspaetet.", rJcounter),

		};
		Text[] conclExpl = new Text[conclStrings.length];
		for (int i = 0; i < conclStrings.length; i++) {
			Offset pos = null;
			if (i == 0) {
				pos = new Offset(0, 20, initMat, AnimalScript.DIRECTION_SW);
			} else {
				pos = new Offset(0, 0, conclExpl[i - 1], AnimalScript.DIRECTION_SW);
			}
			conclExpl[i] = lang.newText(pos, conclStrings[i], "conclStr", null, textProps);
		}
	}

	private void highlightColumn(IntMatrix jobMat, int col, boolean b) {
		for (int i = 0; i < jobMat.getNrRows(); i++) {
			if (b) {
				jobMat.highlightCell(i, col, null, null);
			} else {
				jobMat.unhighlightCell(i, col, null, null);
			}
		}

	}

	private void highlightBorder(IntMatrix jobMat, int late, boolean b) {
		MatrixProperties props = jobMat.getProperties();
		Color highborcolor = (Color) props.get(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY);
		Color borcolor = (Color) props.get(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY);
		for (int i = 0; i < jobMat.getNrCols(); i++) {
			if (b) {
				jobMat.setGridBorderColor(late, i, highborcolor, null, null);
			} else {
				jobMat.setGridBorderColor(late, i, borcolor, null, null);
			}
		}

	}

	private static IntMatrix calculateFinishDates(IntMatrix jobMat) {
		int fdate = 0;
		for (int i = 0; i < jobMat.getNrRows(); i++) {
			fdate += jobMat.getElement(i, 2);
			jobMat.put(i, 3, fdate, null, null);
		}
		return jobMat;
	}

	private static void highlightRow(IntMatrix jobMat, int row) {
		for (int i = 0; i < jobMat.getNrCols(); i++) {
			jobMat.highlightCell(row, i, null, null);
		}
	}

	private static void unhighlightRow(IntMatrix jobMat, int row) {
		for (int i = 0; i < jobMat.getNrCols(); i++) {
			jobMat.unhighlightCell(row, i, null, null);
		}
	}

	public String getName() {
		return "Moores Algorithmus - Ablaufplanung";
	}

	public String getAlgorithmName() {
		return "mooresAlgorithm";
	}

	public String getAnimationAuthor() {
		return "Niklas Stoehr";
	}

	public String getDescription() {
		return "Moores Algorithmus f�r die Ablaufplanung ist ein Algorithmus, der eine gegebene Menge an Jobs in eine Reihenfolge bringt. "
				+ "Diese, durch den Algorithmus gefundene Reihenfolge f�r das Abarbeiten der Jobs, minimiert dabei die Anzahl versp�teter Jobs. "
				+ "Jeder Job hat deswegen grunds�tzlich eine Bearbeitungszeit und einen Termin. "
				+ "Der Zeitpunkt an dem Job i fertiggestellt wird, ist deshalb die Summe aus der Bearbeitungszeit aller Jobs, "
				+ "die vor Job i in der Reihenfolge vorkommen und die Bearbeitungszeit von Job i. "
				+ "Ein Job ist dann versp�tet, wenn sein Fertigstellungszeitpunkt sp�ter als sein Termin ist. " + "\n"
				+ "In dieser Beispielimplementation wird f�r eine beliebige Menge an Jobs, die durch Jobnummer, Termin und Bearbeitungszeit definiert sind, "
				+ "eine Reihenfolge ermittelt, die die Anzahl versp�teter Auftr�ge minimiert. "
				+ "Dieses Ziel ist in der Ablaufplanung insbesondere deshalb von Interesse, weil Strafzahlungen h�ufig pauschal pro versp�tetem Job anfallen. ";
	}

	public String getCodeExample() {
		return "F�r eine gegebene Reihenfolge von n Jobs gehe wie folgt vor: \n"
				+ "1.a Sortiere die Jobs nach ihrem Termin. \n"
				+ "1.b Berechne die Fertigstellungszeitpunkte der Jobs.\n"
				+ "2. Finde den ersten verspaeteten Job [i]. Existiert keiner, gehe zu Schritt 4.\n"
				+ "3. Entferne den Job mit der groessten Bearbeitungszeit aus [0] ... [i]. Gehe zu 2.\n"
				+ "4. Fuege die entfernten Auftraege an das Ende an.";
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

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
			int[][] jobs = (int[][]) primitives.get("jobMatrix");
			if (jobs[0].length != 4) {
				return false;
			}
		return true;
	}
}