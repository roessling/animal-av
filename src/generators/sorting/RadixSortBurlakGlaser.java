package generators.sorting;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayBasedQueue;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


/**
 * 
 * @author| Dmitrij Burlak | Christian Glaser | | Dima85@web.de
 *          |c_glaser@rbg.informatik.tu-darmstadt.de|
 */
public class RadixSortBurlakGlaser implements Generator {
	//MODUS debugmode = true = ON 

	private static final boolean debugMode = false;
	// ALGORITHMUS GRUNDLAGEN
	public int BASIS = 10;
	public int [] DATA;
	int MAXelem = 0;
	public Queue<Integer>[] stapel;
	public int durchlaufe = 0;
	// ANIMAL GRUNDLAGEN
	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;
	TextProperties tp;
	SourceCodeProperties scProps;
	TextProperties l1tp;
	ArrayProperties arrayProps;
	/*
	 * ################################################################
	 * KONSTRUKTOREN fÜr AUFGABE7 RADIXSORT AUF ANIMAL
	 * 
	 * ################################################################
	 */
	RadixSortBurlakGlaser() 
	{
		
	}

	
	RadixSortBurlakGlaser(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);

		for (int i = 0; i < DATA.length; i++) {
			MAXelem = MAXelem < DATA[i] ? DATA[i] : MAXelem;
		}
		durchlaufe = (int) Math.ceil(Math.log10(MAXelem));

		// Der Stapel
		stapel =  new Queue[BASIS];

		for (int i = 0; i < stapel.length; i++) {
			stapel[i] = new LinkedList<Integer>();
		}
	}


	 private static final String DESCRIPTION = 
			"RADIX - SORT : Radixsort besteht aus zwei Phasen, "+
		 	"die immer wieder abwechselnd durchgef&uuml;hrt werden. " +
	 		"Die Verteilungsphase dient dazu, die Daten auf Schubladen aufzuteilen,"+
			"w&auml;hrend in der Sammelphase die Daten aus diesen Schubladen wieder aufgesammelt werden. " +
			"Beide Phasen werden f&uuml;r jede Stelle der (zu sortierenden) Schl&uuml;ssel einmal durchgef&uuml;hrt.\n\n"+
			
			"Verteilungsphase \n" +
			"In dieser Phase werden die Daten in die vorhandenen Schubladen (Queue) aufgeteilt(auf den Stapel gestapelt) ," +
			"wobei f&uuml;r jedes Zeichen des zugrunde liegenden Alphabets, in unserem Fall die Ziffern von 0 - 9, " +
			"eine Schublade (Queue) zur Verf&uuml;gung steht. " +
			"In welches Fach der gerade betrachtete Schl&uuml;ssel gelegt wird, " +
			"haengt von dem Zeichen der gerade betrachteten Stelle im Schluessel ab. " +
			"So wird z.B. die Zahl 352 in die 3. Schublade (Queues) gelegt, " +
			"wenn gerade die dritte Stelle (von hinten) betrachtet wird.\n\n"+
			
			"Sammelphase\n" +
			"Nach der Aufteilung der Daten in Schubladen(Queues) in Phase 1 " +
			"werden die Daten wieder eingesammelt und in ein Array geschrieben. " +
			"Hierbei wird so vorgegangen, dass zuerst alle Daten aus der Schublade mit der niedrigsten Wertigkeit eingesammelt werden," +
			"wobei die Reihenfolge der darin befindlichen Elemente nicht ver&auml;ndert werden darf. " +
			"Danach werden die Elemente des n&auml;chst h&ouml;heren Faches eingesammelt und an die schon aufgesammelten Elemente angef&uuml;gt. " +
			"Dies f&uuml;hrt man fort bis alle F&auml;cher wieder geleert wurden.";
 
	 private static final String SOURCE_CODE = "" 
	 	+"for (int d = 0; d &lt; durchlaufe; d++) {\n\n"
	 	
		+"// PHASE1 : auf den Stapel nch der aktuellen ziffer sortiert \n"
		+" for (int i = 0; i &lt; DATA.length; i++) {\n"
		+"  tmp = DATA[i];\n"
		+"  QUEUE[numAt(tmp, d)].add(tmp);\n"
		+"  }\n\n"

		+"//  PHASE2 : schreibe die daten zurück ins DATA \n"
		+" for (int i = 0; i &lt; Stapel.length; i++) {\n"
		+"  while (!QUEUE[i].isEmpty()) {\n"
		+"   DATA[zaehler] = QUEUE[i].poll();\n"
		+"   zaehler++;\n"
		+"   }\n"
		+"  }\n"
		+" zaehler = 0;\n"
		+" };\n"
		+"\n\n\n\n"
		
		+"int numAt(int num, int pos) {\n"
		+"int tmp = (int) (num / Math.pow(10, pos));\n" +
		"return tmp % 10;\n" +
		"}";
	
	
	public String toString() {
		String vars = "";
		vars += BASIS + " BASIS  \n";
		vars += durchlaufe + " durchlaufe  \n";
		if (DATA != null)
		  for (int i = 0; i < DATA.length; i++)
		    vars += DATA[i] + " ";
		vars += " " + DATA + " DATA  \n";
		return vars;
	}

	/**
	 * 
	 * @param num
	 *            eine Zahl
	 * @param pos
	 *            Position pos der gewuenschten Ziffer
	 * @return eine ziffer aus der Zahl num an der Position pos
	 */

	int numAt(int num, int pos) {
		int tmp = (int) (num / Math.pow(10, pos));
		return tmp % 10;
	}
/**
 * radix sortiert ein Array von ints indem stellenweise alle Zahlen verglichen und einsortiert werden.
 * @param DATAArray 
 * @param codeSupport
 * @throws LineNotExistsException
 */
	void radix(IntArray DATAArray, SourceCode codeSupport)throws LineNotExistsException  {
		int tmp = 0;
		int zaehler = 0;
		
		Timing DURATION = new TicksTiming(60);
		// Highlight first line
		// Line, Column, use context colour?, display options, duration
		codeSupport.highlight(0, 0, false);
		lang.nextStep();

//		Array, current index, name, display options, properties
//		ArrayMarkerProperties arrayMProps = new ArrayMarkerProperties();
//		arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");   
//		arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
//		arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
//		
		Text [] TArr = new Text[10];
		ArrayBasedQueue<Integer> [] ABQArr = new ArrayBasedQueue[10];
		//TextProperties l1tp = new TextProperties();
		l1tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 15));
		lang.newText(new Coordinates(20,120), "QUEUES:", "stappelArrayText", null,tp);
		
		//stappel von stappeln in das die werte des arrays reinkommen
		int zeilen = 0;
		int spalten = 0;
		int abstand = 170;
		for(int i= 0 ;i < 10;i++){
				TArr[i]=lang.newText(new Coordinates(80+spalten*abstand,160 + zeilen*90), i+" :", "stappelArrayText"+i, null,l1tp);
      ABQArr[i] = lang.newArrayBasedQueue(new Coordinates(110 + spalten
          * abstand, 160 + zeilen * 90), (List<Integer>) stapel[i], "QUEUE"+i, null, DATA.length);
			zeilen++;
			if(zeilen % 5 == 0){spalten++;zeilen=0;}
		}
		lang.nextStep();
		
		l1tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		l1tp.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.BOLD, 20));
		lang.newText(new Coordinates(550, 70), "INFOS :: AKTUELLE :: INFOS ", "INFOText", null,tp);
		
		//max Data, gibt  das größte und dementsprechend das längste lement des DATA[] raus. 
		Text elemMax = lang.newText(new Coordinates(550, 120), "LENGTH(MAX(DATA))","InfoMaxData", null,l1tp);
		l1tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		Text elemTmp = lang.newText(new Coordinates(550, 170), " AKTUELLES ELEMENT = " +
																"KOMMT IN DIE " +
																"QUEUE :","InfoAktuelleElement", null,l1tp);
		lang.nextStep();
		
		codeSupport.toggleHighlight(0, 1);
		elemMax.setText("Anzahl der Ziffern in der groessten Zahl = "+durchlaufe+" = length("+MAXelem+")", null, DURATION);
		lang.nextStep();
		
		// durchlaufe = anzahl der stellen der groessten Zahl
		for (int d = 0; d < durchlaufe; d++) {
			if(d > 0)codeSupport.toggleHighlight(10, 1);
			elemMax.setText("VERTEILUNGS PHASE ", null, DURATION);
			// auf den stappel nch der aktuellen ziffer sortiert
			for (int i = 0; i < DATA.length; i++) {
				if(i > 0)codeSupport.toggleHighlight(4, 2); else codeSupport.toggleHighlight(1, 2); 
				tmp = DATA[i];
				DATAArray.put(i, 0, null, DURATION);
				elemTmp.setText("AKTUELLES ELEMENT = "+tmp+" KOMMT IN DIE QUEUE : "+numAt(tmp, d), null, DURATION);
				lang.nextStep();
				elemTmp.setText("DABEI WIRD NACH DER "+(durchlaufe-d)+". ZIFFER von "+tmp+" SORTIERT", null, DURATION);
				lang.nextStep();
				
				codeSupport.toggleHighlight(2, 3);
				stapel[numAt(tmp, d)].add(tmp);
				ABQArr[numAt(tmp, d)].enqueue(tmp, null, DURATION);
				lang.nextStep();
				
				codeSupport.toggleHighlight(3, 4);
				DATAArray.highlightCell(i, null, DURATION);
				lang.nextStep();
			
			}
			
			DATAArray.unhighlightCell(0, DATA.length-1, null, DURATION);
			elemMax.setText("VERTEILUNGS PHASE ZU ENDE", null, DURATION);
			lang.nextStep();
			elemMax.setText("EINSAMMEL PHASE:", null, DURATION);
			codeSupport.toggleHighlight(4, 6);
			lang.nextStep();
			
			// schreibe die daten zurück ins DATA
			for (int i = 0; i < stapel.length; i++) {
				codeSupport.toggleHighlight(6, 7);
				lang.nextStep();
				
				elemTmp.setText("WIR BETRACHTEN QUEUE " + i+" : DIESE IST " +(stapel[i].isEmpty()?"LEER":"NICHT LEER"), null, DURATION);
				if(stapel[i].isEmpty())codeSupport.toggleHighlight(7,9);
				lang.nextStep();
				
				while (!stapel[i].isEmpty()) {
					DATA[zaehler] = stapel[i].poll();
					ABQArr[i].dequeue(null, DURATION);
					elemTmp.setText("AKTUELLES ELEMENT = "+DATA[zaehler]+" KOMMT AUS DER QUEUE: " + i, null, DURATION);
					codeSupport.toggleHighlight(7,8);
					lang.nextStep();
					
					DATAArray.put(zaehler, DATA[zaehler], null, DURATION);
					DATAArray.highlightCell(zaehler, null, DURATION);
					lang.nextStep();
					
					zaehler++;
					if(stapel[i].isEmpty())codeSupport.toggleHighlight(8,9);
					lang.nextStep();
				}
				
				if(i<stapel.length-1)codeSupport.toggleHighlight(9, 6);
				//DATAArray.highlightCell(i, i+1, null, DURATION);
				
				lang.nextStep();
			}
			codeSupport.toggleHighlight(9,10);
			elemMax.setText("EINSAMMEL PHASE ZU ENDE", null, DURATION);
			lang.nextStep();
		
			zaehler = 0;
			DATAArray.unhighlightCell(0, DATA.length-1, null, DURATION);
			lang.nextStep();
		}
		codeSupport.toggleHighlight(10, 12);
		elemTmp.setText("GESAMMTLAUFZEIT : "+durchlaufe  +" *( "+ BASIS +" + "+ DATA.length+") = " + durchlaufe*(BASIS + DATA.length)+" OPERATIONEN",null,DURATION);
		elemMax.setText("GESAMMTLAUFZEIT : durchlaeufe  *(BIAS + DATA.length)", null, DURATION);
	}

	/*
	 * public static void printToFile(Aufgabe7 lang, File file){
	 * FileOutputStream out; PrintStream p; try { out = new
	 * FileOutputStream(file); p = new PrintStream(out);
	 * p.println(lang.toString()); p.close(); } catch (Exception e){
	 * System.out.println("Error writing to file"); } }
	 */

	public static void printToFile(Language lang, File file) {
		FileOutputStream out;
		PrintStream p;
		try {
			out = new FileOutputStream(file);
			p = new PrintStream(out);
			p.println(lang);
			p.close();
		} catch (Exception e) {
			System.out.println("Error writing to file");
		}
	}

	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		lang = new AnimalScript("RADIX-SORT", "Dmitrij Burlak, Christian Glaser", 600, 400);
		lang.setStepMode(true);
		DATA = (int[]) arg1.get("array");
		if(!debugMode)
		{
			tp = (TextProperties) arg0.getPropertiesByName("INFO");
			scProps = (SourceCodeProperties) arg0.getPropertiesByName("sourceCode");
			arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");
			l1tp= (TextProperties) arg0.getPropertiesByName("Lauftext1");
		}
		//#######################################################
		for (int i = 0; i < DATA.length; i++) {
			MAXelem = MAXelem < DATA[i] ? DATA[i] : MAXelem;
		}
		
		durchlaufe = (int) Math.ceil(Math.log10(MAXelem));

		// Der Stappel
		stapel =  new Queue[BASIS];

		for (int i = 0; i < stapel.length; i++) {
			stapel[i] = new LinkedList<Integer>();
		}

		//########################################################
		myInit();
		return lang.toString();
	}
	
	/*
	 * ################################################################ 
	 * Getter
	 * ################################################################
	 */
	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	@Override
	public String getAlgorithmName() {
		return "Radix-Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Dmitrij Burlak, Christian Glaser";
	}

	@Override
	public String getCodeExample() {
		return SOURCE_CODE;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return "Radix-Sort";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

    public void init() {
    }
	public void myInit() {
		//create TEXT
		
		if (debugMode)
		{
			tp  = new TextProperties();
			arrayProps = new ArrayProperties();
			scProps = new SourceCodeProperties();
			l1tp = new TextProperties();
		}
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 15));
		
		lang.newText(new Coordinates(20,20), "RADIX - SORT", "header", null,tp);
		
		// first, set the visual properties
	    
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
	    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
	        Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
	        Color.orange);
	    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
	        Color.yellow); 

	    
	    // now, create the IntArray object, linked to the properties
	    	lang.newText(new Coordinates(80,70), "DATA :", "DATAArrayText", null,tp);
	    	IntArray DATAArray = lang.newIntArray(new Coordinates(150, 70), DATA, "DATAArray", 
					null, arrayProps);
	    	
			// start a new step after the array was created
			lang.nextStep();
			
			Timing Duration = new TicksTiming(60);
			
			// Create SourceCode: coordinates, name, display options, 
			// default properties
			
			// first, set the visual properties for the source code
	    
	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
	        Font.BOLD, 15));

	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
	        Color.BLUE);   
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

	    SourceCode ansage= lang.newSourceCode(new Coordinates(80, 140), "Kurz Beschriebung" 
	    		, null, scProps);
	    ansage.addCodeLine("Der im Folgenden vorgestellte Algorithmus sortiert mit Zahlen gefuellte Liste.", null, 0, Duration);
	    ansage.addCodeLine("  ", null, 0, Duration);
	    ansage.addCodeLine("In der VERTEILUNGS-Phase werden die Zahlen, aus der Liste, nach der n-ten Stelle auf Warteschlangen(FIFO) verteilt.", null, 0, Duration);
		ansage.addCodeLine("In der EINSAMMEL-Phase werden die Zahlen wieder zurueck in die Zahlenliste geschrieben.", null, 0, Duration);
		ansage.addCodeLine("Nun sind die Zahlen nach der n-ten Stelle sortiert.", null, 0, Duration);
		ansage.addCodeLine("  ", null, 0, Duration);
		ansage.addCodeLine("Fuer den erfahrenen Betrachter dieser Presentation empfiehlt es sich,", null, 0, Duration);
		ansage.addCodeLine("den rechts unten eingeblendeten PseudoCode mit zu verfolgen.", null, 0, Duration);
		ansage.addCodeLine("Die einzelnen Vorgehensschritte werden im INFO-Fenstert erlaeutert.", null, 0, Duration);
		ansage.addCodeLine("  ", null, 0, Duration);
		ansage.addCodeLine("Viel Spass mit Radix Sort!", null, 0, Duration);
		lang.nextStep();
		
		ansage.hide(Duration);
		lang.nextStep();
	 // now, create the source code entity
		SourceCode sourceCode = lang.newSourceCode(new Coordinates(550, 300), "sourceCode",
        null, scProps);

		
		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sourceCode.addCodeLine("durchlaeufe = length( MAX(DATA) )", null, 0, null); //1
		sourceCode.addCodeLine("FOR d = 0 to durchlaeufe  ", null, 0, null); //2
		sourceCode.addCodeLine("     FOR i = 0 to DATA.length  //Verteilungs Phase", null, 1, null); //3
		sourceCode.addCodeLine("         QUEUES[cipherAt(DATA[i],d)] <- DATA[i]", null, 1, null); //4
		sourceCode.addCodeLine("     NEXT i++ ", null, 1, null); //5
		sourceCode.addCodeLine("                                                ", null, 1, null); //6
		sourceCode.addCodeLine("     FOR j = 0 to QUEUES.length  //Einsammel Phase", null, 1, null); //7
		sourceCode.addCodeLine("         WHILE QUEUES[j] not empty", null, 1, null); //8
		sourceCode.addCodeLine("         DO  DATA[] <- DEQUEUE(QUEUES[j])", null, 1, null); //9
		sourceCode.addCodeLine("     NEXT j++", null, 1, null); //10
		sourceCode.addCodeLine("NEXT d++                                        ", null, 1, null); //10
		sourceCode.addCodeLine("                                                ", null, 1, null); //11
		sourceCode.addCodeLine("FERTIG ! ! ! FERTIG ! ! ! FERTIG !  ! !", null, 1, null); //12
		
		/*
	void radix() {
		int tmp = 0;
		int zaehler = 0;
		// durchlaufe = anzahl der stellen der groessten Zahl
		for (int d = 0; d < durchlaufe; d++) {
			
			// auf den stappel nch der aktuellen ziffer sortiert
			for (int i = 0; i < DATA.length; i++) {
				tmp = DATA[i];
				stappel[numAt(tmp, d)].add(tmp);
			}

			// schreibe die daten zurückins DATA
			for (int i = 0; i < stappel.length; i++) {
				while (!stappel[i].isEmpty()) {
					DATA[zaehler] = stappel[i].poll();
					zaehler++;
				}
			}
			zaehler = 0;
		}
	}
		*/
		
		lang.nextStep();
		
		try {
			// Start quicksort
			radix(DATAArray, sourceCode);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
	}

}
