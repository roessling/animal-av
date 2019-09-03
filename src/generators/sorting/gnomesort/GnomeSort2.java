package generators.sorting.gnomesort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class GnomeSort2 implements Generator {
	
	//Globale Variablen
	private Language lang;
	private ArrayMarkerProperties amj;
	private ArrayMarkerProperties ami;
	private SourceCodeProperties scProps;
	private ArrayProperties arrayProps;
	private int[] original;
	private SourceCode sc;
	private TextProperties textProps;
	private RectProperties rect;
	private Text header;
	private SourceCode desc;
	private IntArray array;
	private ArrayMarker i;
	private ArrayMarker j;
	private TextProperties lastProps;
	private SourceCodeProperties questionProps;
	private SourceCodeProperties answerProps;
	private Variables vars;

	//Initialisierung von verschiedenen Properties und von AnimalScript
	public void init() {
		lang = new AnimalScript("GnomeSort 2.0", "Tabea Born, Yasmin Krahofer",
				800, 600);
		lang.setStepMode(true);
		
		questionProps = new SourceCodeProperties();
		questionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 17));
		questionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		answerProps = new SourceCodeProperties();
		answerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 17));
		answerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		
		lastProps = new TextProperties();
		lastProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		lastProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		lastProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 15));
		
		vars = lang.newVariables();
		vars.declare("int", "varLoop");
		vars.declare("int", "varSwap");
		vars.declare("int", "varI");
		vars.declare("int", "varI-1");

	}

	//Hier werden die Werte, die der Benutzer eingestellt hat in den Quellcode übernommen
	//danach wird über sort(original) der eigentliche Algorithmus aufgerufen
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		amj = (ArrayMarkerProperties) props.getPropertiesByName("amj");
		ami = (ArrayMarkerProperties) props.getPropertiesByName("ami");
		scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		original = (int[]) primitives.get("original");

		sort(original);

		return lang.toString();
	}

	//Methoden für die Konfigurationsmaske und den Generator
	public String getName() {
		return "GnomeSort 2.0";
	}

	//Methoden für die Konfigurationsmaske und den Generator
	public String getAlgorithmName() {
		return "Gnome Sort";
	}

	//Methoden für die Konfigurationsmaske und den Generator
	public String getAnimationAuthor() {
		return "Tabea Born, Yasmin Krahofer";
	}
	
	//Methoden für die Konfigurationsmaske und den Generator
	public String getDescription() {
		return "Man stelle sich einen Zwerg(Gnome) vor, der Blument&ouml;pfe von links nach rechts der Gr&ouml;&szlig;e nach sortieren m&ouml;chte. Da die Blument&ouml;pfe aber gr&ouml;&szlig;er sind als der Zwerg, stellt er sich immer zwischen zwei T&ouml;pfe und vergleicht diese miteinander. Stimmt die Reihenfolge, so macht er einen Schritt nach rechts. Stimmt die Reihenfolge aber nicht, dann vertauscht er die T&ouml;pfe und geht einen Schritt nach links. Dies wiederholt er so oft, bis es am ganz rechts stehenden Blumentopf angekommen ist."
				+ "\n"
				+ "\n"
				+ "Der Algorithmus hat im Worst-Case eine Komplexit&auml;t von O(n^2) und im Best-Case eine Komplexit&auml;t von O(n)."
				+ "\n" + "Quelle: http://de.wikipedia.org/wiki/Gnomesort";
	}

	//Methoden für die Konfigurationsmaske und den Generator
	public String getCodeExample() {
		return "for ( int i = 1; i < array.length; ) {  " + "\n"
				+ "     if ( array[i - 1] <= array[i] ) {    " + "\n"
				+ "          ++i;    " + "\n" + "     } else {    " + "\n"
				+ "          int tempVal = array[i];    " + "\n"
				+ "          array[i] = array[i - 1];    " + "\n"
				+ "          array[i - 1] = tempVal;    " + "\n"
				+ "          --i;    " + "\n" + "          if ( i == 0 ) {    "
				+ "\n" + "               i = 1;     " + "\n"
				+ "          }    " + "\n" + "     }    " + "\n" + "}  ";
	}

	//Methoden für die Konfigurationsmaske und den Generator
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	//Methoden für die Konfigurationsmaske und den Generator
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	//Methoden für die Konfigurationsmaske und den Generator
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	//Methoden für die Konfigurationsmaske und den Generator
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
	
	//Warnhinweis für den Benutzer
	//Damit es zu keinen Missverständnissen kommt
	public void warning() {

		SourceCodeProperties warningProps = new SourceCodeProperties();
		warningProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		warningProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 17));

		SourceCode warning = lang.newSourceCode(new Coordinates(20, 90),
				"warning", null, warningProps);

		warning.addCodeLine("Hinweis:", null, 0, null);
		warning.addCodeLine("", null, 0, null);
		warning.addCodeLine(
				"Im Algorithmus werden zwei ArrayMarker verwendet.", null, 0,
				null);
		warning.addCodeLine(
				"('i' und 'i-1') Dabei kann es vorkommen, dass der", null, 0,
				null);
		warning.addCodeLine(
				"Array Marker 'i-1' zwei Positionen von 'i' entfernt", null, 0,
				null);
		warning.addCodeLine(
				"ist. Dies soll lediglich der Benutzerfreundlichkeit", null, 0,
				null);
		warning.addCodeLine(
				"dienen und kann von euch bedenkenlos übergangen werden.",
				null, 0, null);
		warning.addCodeLine(
				"Der Zeiger 'i' wird sofort im nächsten Schritt angepasst.",
				null, 0, null);
		lang.nextStep();
		warning.hide(); //Der Warnhinweis wird im nächsten Schritt wieder ausgeblendet
	}
	
	//Die erste Frage für den Benutzer mit Antwort
	//nicht mehr als MultipleChoiceQuestion, da diese nicht implementiert wurde
	//vom Veranstalter...
	public void getFirstQuestion(){
		desc.hide();
		SourceCode question = lang.newSourceCode(new Coordinates (450,200),"firstQ", null, questionProps);
		question.addCodeLine("Frage: Welche Komplexitaet hat der GnomeSort im worst-case", null,0,null);
		question.addCodeLine("a) O(n)", null, 1, null);
		question.addCodeLine("b) O(n^3)", null, 1, null);
		question.addCodeLine("c) O(n^2)", null, 1, null);
		question.addCodeLine("d) O(n log n)", null, 1, null);
		lang.nextStep("Antwort");
		
		SourceCode answer = lang.newSourceCode(new Coordinates (450,350),"firstA", null, answerProps);
		answer.addCodeLine("Antwort: Die Antwort (c) ist richtig. Im schlimmsten Fall",null,0,null);
		answer.addCodeLine("ist das Array 'rueckwaerts' sortiert. Der Algorithmus wuerde", null, 0, null);
		answer.addCodeLine("dann genau n*(n-1)-mal durchlaufen. Das ist aequivalent", null, 0, null);
		answer.addCodeLine("zu n^2-n und hat damit die Komplexitaet O(n^2).", null, 0, null);
		lang.nextStep("Start des Algorithmus");
		desc.show();
		question.hide();
		answer.hide();
	}
	
	//Die zweite Frage für den Benutzer
	public void getSecondQuestion(){
		desc.hide();
		SourceCode question = lang.newSourceCode(new Coordinates (450,200),"firstQ", null, questionProps);
		question.addCodeLine("Frage: In welchem Fall hat der GnomeSort eine Komplexitaet von O(n)?", null,0,null);
		lang.nextStep("Antwort");
		
		SourceCode answer = lang.newSourceCode(new Coordinates (450,250),"firstA", null, answerProps);
		answer.addCodeLine("Antwort: Der GnomeSort hat genau dann eine Komplexitaet von O(n),",null,0,null);
		answer.addCodeLine("wenn es sich um ein sortiertes Array handelt.", null, 0, null);
		lang.nextStep();
		desc.show();
		question.hide();
		answer.hide();
	}
	
	//Ein bisschen Trivia für den Benutzer
	//gefunden im englischen Wikipedia-Artikel
	//http://en.wikipedia.org/wiki/Gnome_sort
	public void getNice2Know(){
		SourceCode known = lang.newSourceCode(new Coordinates(20, 130),
				"Schon gewusst", null, questionProps);
		known.addCodeLine("Schon gewusst: ", null, 0, null);
		known.addCodeLine("", null, 0, null);
		known.addCodeLine(
				"Der GnomeSort wurde im Jahr 2000 von Hamid Sarbazi-Azad",
				null, 0, null);
		known.addCodeLine("vorgeschlagen und hiess damals 'StupidSort'.", null,
				0, null);
		known.addCodeLine("Spaeter wurde er dann von Dick Grune beschrieben",
				null, 0, null);
		known.addCodeLine("und in GnomeSort umbenannt.", null, 0, null);

		lang.nextStep();
		known.hide();
		desc.hide();
	}

	//Der eigentliche Quellcode
	//Hier wird das eingegebene Array mit dem GnomeSort sortiert
	public void sort(int[] inputArray) {

		//anzeigen der einzelnen Elemente (Überschrift und Beschreibung)
		array = lang.newIntArray(new Coordinates(30, 150), inputArray, "array",
				null, arrayProps);	//Das Array wird hier schon initialisiert aber verdeckt, damit die Beschreibung variabel daran angepasst werden kann 
		array.hide();
		showHeader();
		showDescription();
		lang.nextStep();
		
		warning(); //Hier wird der Warnhinweis eingeblendet
		
		int loops = 0; //Anzahl der Iterationen und Vergleiche
		int swaps = 0; //Anzahl der Vertauschungen
		
		//Anzeige von loops und swaps für den Benutzer
		Text loop = lang.newText(new Offset(40, -40, array,
				AnimalScript.DIRECTION_E),
				"Anzahl der Iterationen und Vergleiche: " + loops, "loop",
				null, lastProps); 
		Text swap = lang.newText(new Offset(40, -10, array,
				AnimalScript.DIRECTION_E), "Anzahl der Vertauschungen: "
				+ swaps, "swap", null, lastProps);

		Timing defaultTiming = new TicksTiming(30); 
		
		array.show(); //Anzeigen des Arrays
		showSourceCode(); //Anzeigen des SourceCodes
		lang.nextStep();

		//ArrayMarker werden eingeblendet
		i = lang.newArrayMarker(array, 1, "i", null, ami);
		j = lang.newArrayMarker(array, 0, "j", null, amj);
		lang.nextStep("1.Frage");
		
		//Variablen werden auf die richtigen Werte gesetzt
		vars.set("varI", Integer.toString(i.getPosition()));
		vars.set("varI-1", Integer.toString(j.getPosition()));
		
		
		getFirstQuestion(); //Die erste Frage wird aufgerufen
		
		//eigentlicher Algorithmus
		for (; i.getPosition() < array.getLength();) {

			loops++; //Erhöhe die Anzahl der loops und ändere den angezeigten Text
			loop.setText("Anzahl der Iterationen und Vergleiche: " + loops,
					null, null);
			vars.set("varLoop", Integer.toString(loops)); //Anzahl der Iterationen im Variablen-Fenster
			
			
			//Highlighten der verschiedenen Elemente (bzw.unhighlighten)
			sc.highlight(0);
			sc.unhighlight(2);
			sc.unhighlight(8);
			sc.unhighlight(9);
			lang.nextStep(loops + ". Iteration");

			sc.unhighlight(0);
			sc.highlight(1);
			lang.nextStep("Vergleiche die Array-Felder miteiander");

			//Das Feld i wird mit dem Feld i-1 verglichen. 
			//Ist der Wert von i > als der Wert von i-1
			//werden die Zeigenr incrementiert und es passiert nichts
			if (array.getData(j.getPosition()) <= array
					.getData(i.getPosition())) {
				sc.toggleHighlight(1, 2);
				i.increment(null, defaultTiming);
				vars.set("varI", Integer.toString(i.getPosition())); //erhöhen der Variablen i
				j.increment(null, defaultTiming);
				vars.set("varI-1", Integer.toString(j.getPosition())); //erhöhen der ariablen i-1
				lang.nextStep("Die Werte des Arrays sind in der richtigen Reihenfolge");
			} else { //Sind die Werte nicht in der richtigen Reihenfolge, werden sie vertauscht und die Zeiger werden dekrementiert
				sc.toggleHighlight(1, 3);
				lang.nextStep("Die Werte des Arrays befinden sich nicht in der richtigen Reihenfolge");

				sc.toggleHighlight(3, 4);
				sc.highlight(5);
				sc.highlight(6);
				swaps++; //Die Anzahl der Vertauschungen wird inkrementiert und angezeigt
				array.swap(i.getPosition(), j.getPosition(), null,
						defaultTiming);
				swap.setText("Anzahl der Vertauschungen: " + swaps, null, null);
				vars.set("varSwap", Integer.toString(swaps)); //Anzahl der Vertauschungen im Variablen-Fenster
				lang.nextStep("Vertausche die Einträge. Anzahl der Vertauschungen: "
						+ swaps);

				sc.unhighlight(4);
				sc.unhighlight(5);
				sc.toggleHighlight(6, 7);
				
				/*Grund für den Warnhinweis:
				 * würde man hier die Schritte j.decrement(null, defaultTiming) und i.decrement(null, defaultTiming)
				 * einfach hintereinander ausführen lassen, dann würde der Zeiger j dekrementiert, i aber nicht (zumindest nicht sichtbar)
				 * Um es für den Benutzer lesbarer zu machen, ist also lang.nextStep() eingefügt worden, damit beide Zeiger
				 * zu jeder Zeit die richtigen Positionen haben
				*/
				
				//Abfangen der ArrayIndexOutOfBoundsException - ist i-1 = 0, wird dieser Schritt übersprungen
				if (j.getPosition() > 0) {
					j.decrement(null, defaultTiming);
					vars.set("varI-1", Integer.toString(j.getPosition())); //verringern der Variable i-1
					lang.nextStep("Dekrementiere 'i-1', wenn i > 0 ist");
				}

				//Hier wird dann der Zeiger i dekrementiert
				i.decrement(null, defaultTiming);
				vars.set("varI", Integer.toString(i.getPosition())); //verringern der Variable i
				lang.nextStep("Dekrementiere 'i'");

				//Befindet sich der Zeiger (leider nicht sichtbar) auf der Position 0, wird er hier zurück auf 1 gesetzt
				//(Daher meiner Meinung nach nicht tragisch für den Benutzer)
				sc.toggleHighlight(7, 8);
				if (i.getPosition() == 0) {
					lang.nextStep("Setze i = 1, wenn i = 0");
					sc.toggleHighlight(8, 9);
					i.move(1, null, defaultTiming);
					vars.set("varI", Integer.toString(i.getPosition())); //die Variable i wird auf 1 gesetzt
				}
				lang.nextStep();
			}
		}
		sc.unhighlight(2);
		sc.unhighlight(9);
		
		getSecondQuestion(); //Anzeigen der zweiten Frage

		lang.nextStep("Ende des Algorithmus und Auswertung"); //Ende des Algorithmus
		loop.hide();
		swap.hide();

		showLast(loops, swaps);  //eigentliche Abschlussfolie
		showGnome(); //Abschiedsfolie

	}

	//Abschlussfolie wird eingeblendet und danach kommt ein bisschen Trivia
	public void showLast(int loops, int swaps) {

		sc.hide();
		array.hide();
		i.hide();
		j.hide();

		lastProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 20));

		//Die Anzahl der Iterationen, Vergleiche und Vertauschungen werden angezeigt
		Text loop = lang.newText(new Coordinates(20, 130),
				"Anzahl der Iterationen und Vergleiche: " + loops, "loop",
				null, lastProps);
		Text swap = lang
				.newText(new Coordinates(20, 160),
						"Anzahl der Vertauschungen: " + swaps, "swap", null,
						lastProps);

		lang.nextStep();
		loop.hide();
		swap.hide();

		getNice2Know(); //ein bisschen Trivia 

	}

	// Abschiedsfolie, damit der Algorithmus nicht ganz so trocken endet...
	public void showGnome() {
		SourceCodeProperties gnomeProps = new SourceCodeProperties();
		gnomeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		gnomeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 12));

		SourceCode gnome = lang.newSourceCode(new Coordinates(20, 90), "gnome",
				null, gnomeProps);
		gnome.addCodeLine("_______$$$$$$", null, 0, null);
		gnome.addCodeLine("_______$$____$$", null, 0, null);
		gnome.addCodeLine("_____$$$$$____$", null, 0, null);
		gnome.addCodeLine("_____$$_$$____$$", null, 0, null);
		gnome.addCodeLine("_______$$______$", null, 0, null);
		gnome.addCodeLine("_______$_______$$", null, 0, null);
		gnome.addCodeLine("______$_________$$", null, 0, null);
		gnome.addCodeLine("______$__________$", null, 0, null);
		gnome.addCodeLine("_____$$$$$$$$$$$$$$", null, 0, null);
		gnome.addCodeLine("_____$$$$$$$___$$$$$", null, 0, null);
		gnome.addCodeLine("____$$$_$$_$$$$$$$$$$$", null, 0, null);
		gnome.addCodeLine("___$$$$$$__o__o__$$$$$", null, 0, null);
		gnome.addCodeLine("___$$$$$$_________$$$", null, 0, null);
		gnome.addCodeLine("____$$$__$$$$__$$$_$", null, 0, null);
		gnome.addCodeLine("__$$$_$___$$$$$$$$$$", null, 0, null);
		gnome.addCodeLine("_$$____$_____$$$$$__$", null, 0, null);
		gnome.addCodeLine("$$______$$__________$$", null, 0, null);
		gnome.addCodeLine("$___$$$$$$$$$$___$$$$$$$$$$$$$$$$", null, 0, null);
		gnome.addCodeLine("$$$___$_$_$$$$$$$$$$$$$$$$$__$$$$$$$$", null, 0,
				null);
		gnome.addCodeLine("__$$$$$_$_____$$$$_________$$$$$$$$$$$$", null, 0,
				null);
		gnome.addCodeLine("____$$$$$$$$$_$$__$$$$$_________$_____$", null, 0,
				null);
		gnome.addCodeLine("____$$_____$$$$$$$__$$$$$$_____$$_$$$$$", null, 0,
				null);
		gnome.addCodeLine("___$$$$$$$$$$$$$$$$$$$$$$$$__$$$$$_$$$$", null, 0,
				null);
		gnome.addCodeLine("___$$$$_____$$$$$$$$$$$$___$$$$$$__$$_$$", null, 0,
				null);
		gnome.addCodeLine("__$$$$$$____$____$$_$$$$$$$$___$$$$_$$_$$", null, 0,
				null);
		gnome.addCodeLine("_$$__$$$$__$$_____$_____$$$$$$$__$$$_$__$", null, 0,
				null);
		gnome.addCodeLine("_$___$$_$$$_$_____$$________$$$$$$$__$__$", null, 0,
				null);
		gnome.addCodeLine("_$$$$$$$$$$$$$_$$$$$__________$$_____$_$$", null, 0,
				null);
		gnome.addCodeLine("__$$_____$__$$$$$_$$___________$$___$__$", null, 0,
				null);
		gnome.addCodeLine("___$$$$$$$_____$$$$$___________$$$$$__$$", null, 0,
				null);
		gnome.addCodeLine("_________________________________$$$$$$", null, 0,
				null);
		gnome.addCodeLine("---> gefunden auf http://snart.cc <---", null, 0,
				null);

		SourceCodeProperties ende = new SourceCodeProperties();
		ende.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		ende.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.BOLD, 20));

		SourceCode bye = lang.newSourceCode(new Coordinates(500, 300), "gnome",
				null, ende);
		bye.addCodeLine("Wenn ihr moechtet, koennt ihr euch jetzt auch noch",
				null, 0, null);
		bye.addCodeLine("persoenlich bei unserem 'Gnome' bedanken ;)", null, 0,
				null);
		bye.addCodeLine("Wir hoffen, es hat euch Spass gemacht!", null, 0, null);
	}

	
	//Der SourceCode wird unterdem Array angezeigt, damit der Benutzer ihn nachvollziehen kann
	//Dabei wird immer die entsprechende Zeile gehighlightet
	public void showSourceCode() {

		sc = lang.newSourceCode(new Coordinates(10, 200), "sourceCode", null,
				scProps);
		sc.addCodeLine("for ( int i = 1; i < array.length; ) {", null, 0, null);
		sc.addCodeLine("if ( array[i - 1] <= array[i] ) {", null, 1, null);
		sc.addCodeLine("++i;", null, 2, null);
		sc.addCodeLine("} else {", null, 1, null);
		sc.addCodeLine("int tempVal = array[i];", null, 2, null);
		sc.addCodeLine("array[i] = array[i - 1];", null, 2, null);
		sc.addCodeLine("array[i - 1] = tempVal;", null, 2, null);
		sc.addCodeLine("--i;", null, 2, null);
		sc.addCodeLine("if ( i == 0 ) {", null, 2, null);
		sc.addCodeLine("i = 1; ", null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
	}

	//Die Überschrift des GnomeSorts, die auch die ganze Animation lang stehen bleibt
	public void showHeader() {
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
		textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.BOLD, 24));
		header = lang.newText(new Coordinates(20, 30), "GnomeSort", "header",
				null, textProps);

		rect = new RectProperties();
		rect.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rect.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rect.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, header, AnimalScript.DIRECTION_SE),
				"HeaderBack", null, rect);
	}

	//Die Beschreibung des Codes in einer einfachen Form
	public void showDescription() {
		SourceCodeProperties description = new SourceCodeProperties();
		description.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		description.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 15));

		desc = lang.newSourceCode(new Offset(470, -80, array,
				AnimalScript.DIRECTION_E), "description", null, description);

		desc.addCodeLine("Ueber den Algorithmus:", null, 0, null);
		desc.addCodeLine("", null, 0, null);
		desc.addCodeLine(
				"Man stelle sich einen Zwerg(Gnome) vor, der Blumentoepfe von links nach rechts der Groesse",
				null, 1, null);
		desc.addCodeLine(
				"nach sortieren moechte. Da die Blumentoepfe aber groesser sind als der Zwerg, stellt er sich immer",
				null, 1, null);
		desc.addCodeLine(
				"zwischen zwei Toepfe und vergleicht diese miteinander. Stimmt die Reihenfolge, so macht er",
				null, 1, null);
		desc.addCodeLine(
				"einen Schritt nach rechts. Stimmt die Reihenfolge aber nicht, dann vertauscht er die Toepfe",
				null, 1, null);
		desc.addCodeLine(
				"und geht einen Schritt nach links. Dies wiederholt er so oft, bis es am ganz rechts stehenden",
				null, 1, null);
		desc.addCodeLine("Blumentopf angekommen ist.", null, 1, null);
		desc.addCodeLine("", null, 0, null);
		desc.addCodeLine("", null, 0, null);
		desc.addCodeLine("Laufzeitanalyse:", null, 0, null);
		desc.addCodeLine("", null, 0, null);
		desc.addCodeLine(
				"Der Algorithmus hat im Worst-Case eine Komplexitaet von O(n^2) und im Best-Case eine ",
				null, 1, null);
		desc.addCodeLine("Komplexitaet von O(n).", null, 1, null);
		desc.addCodeLine("", null, 0, null);
		desc.addCodeLine("Quelle: http://de.wikipedia.org/wiki/Gnomesort",
				null, 0, null);
	}
	
}