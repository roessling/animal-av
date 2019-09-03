package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Vincent Riesop und Nils Mäser
 *
 */
public class BB84 implements ValidatingGenerator {
	
    /**
     * The concrete language object used for creating output
     */
    private Language lang;
    
    private double subsetpercentage;
    private int subsetlength;
    
    private int[] alicebitvalues;
    private int[] alicebases;
    private int[] alicesubset;
    private int[] bobsbitvalues;
    private int[] bobsbases;
    private int[] bobssubset;
    private int[] evesbitvalues;
    //private int[] evesSendbitvalues; //Eve sends exactly the same what she received!!
    private int[] evesbases;
    private int countsamebases;
    
    private Text[] alicebitvaluesText;
	private Text[] alicebasesText;
	private Text[] alicesubsetText;
	private Text[] bobsbitvaluesText;
	private Text[] bobsbasesText;
	private Text[] bobssubsetText;
	private Text[] samebasesText;
    private Text[] evesRecbitvaluesText;
	private Text[] evesRecbasesText;
	private Text[] evesSendbitvaluesText;
	private Text[] evesSendbasesText;
	
	/** Alice bitvalues description */
	private Text abvtext;
	/** Alice bases description */
	private Text abstext;
	/** Alice subset description */
	private Text asstext;
	/** Bobs bitvalues description */
	private Text bbvtext;
	/** Bobs bases description */
	private Text bbstext;
	/** Bobs subset description */
	private Text bsstext;
	/** Eves received bitvalues description */
	private Text erbvtext;
	/** Eves received bases description */
	private Text erbstext;
	/** Eves sent bitvalues description */
	private Text esbvtext;
	/** Eves sent bases description */
	private Text esbstext;
	/** Same bases description */
	private Text sbstext;
	
	private int veryImportantConstant = Integer.valueOf(AnimationPropertiesKeys.The_Answer_to_Life_the_Universe_and_Everything);
	
	private Rect filterMaschine11;
	private Text filterMaschineName11;
	private Rect filterMaschine12;
	private Text filterMaschineName12;
	private Rect filterMaschine21;
	private Text filterMaschineName21;
	private Rect filterMaschine22;
	private Text filterMaschineName22;
    
    private TextProperties   normtextprop  = new TextProperties();
    private TextProperties   smalltextprop  = new TextProperties();
    private TextProperties   normtextdepth10prop  = new TextProperties();
    private TextProperties   normtextdepth12prop  = new TextProperties();
    private TextProperties   normtextdepth13prop  = new TextProperties();
    private TextProperties   headlineprop  = new TextProperties();
    private TextProperties   boldtextprop  = new TextProperties();
    private TextProperties   bigtextprop   = new TextProperties();
    private TextProperties   whitetextprop = new TextProperties();
    private TextProperties   monotextprop  = new TextProperties();
    private TextProperties   smallertextprop  = new TextProperties();

    private TextProperties   checktextprop   = new TextProperties();
    private TextProperties   falsetextprop = new TextProperties();
    
    private CircleProperties borderedcircle    = new CircleProperties();
    private CircleProperties head_prop    = new CircleProperties();
    
    private SquareProperties borderedsquare = new SquareProperties();
    private SquareProperties borderedGreensquare = new SquareProperties();
    private SquareProperties borderedRedsquare = new SquareProperties();
    private RectProperties whiteoverlay = new RectProperties();
    private RectProperties filter_prop = new RectProperties();
    private RectProperties qchannel_prop = new RectProperties();
    private RectProperties cchannel_prop = new RectProperties();
    private PolylineProperties qcline_prop = new PolylineProperties();
    private PolylineProperties ccline_prop = new PolylineProperties();
    private PolylineProperties arrow_prop = new PolylineProperties();
    
    private Color highlightColor;
	
	
    private static final String DESCRIPTION = 
    "Heutige kryptographische Verfahren sind nicht hundertprozentig sicher. "
    +"Angreifer können unter Umständen den Datenverkehr von zwei Parteien "
	+"abhören und den Schlüssel zum Entschlüsseln der Daten herausfinden. Ein "
	+"vollkommen sicherer Datenaustausch ließe sich beispielsweise mit einem "
	+"symmetrischen Verschlüsselungsverfahren implementieren, bei dem die "
	+"Kommunikationspartner sich auf einen Schlüssel einigen, der mithilfe des "
	+"Quantenschlüsselaustauschs übertragen wird. BB84 ist ein solches Protokoll, "
	+"mit dem Schlüssel sicher erstellt werden können. Es beruht auf den Gesetzen "
	+"der Quantenmechanik und seine Sicherheit lässt sich beweisen.\n\n\n"
	+"Begriffe:\n\n"
	+"• Qubits sind Photonen, die durch ihre Polarisierung Bits repräsentieren.\n"
	+"• Polarisierung: Die Qubits werden auf 4 verschiedene Weisen polarisiert. "
	+"Vertikal |, horizontal -, rechtsdiagonal / und linksdiagonal \\ \n"
	+"• Quantenkanal: Der Quantenkanal ist ein Lichtwellenleiter, über den ein "
	+"Sender Qubits zu einem Empfänger schickt.\n"
	+"• +-Basis und x-Basis: Wenn ein Qubit vertikal | oder horizontal - "
	+"polarisiert ist, wurde es auf +-Basis erzeugt. Ist es rechtsdiagonal / oder "
	+"linksdiagonal \\ polarisiert, wurde es auf x-Basis erzeugt.\n"
	+"• Filter: Der Empfänger von Qubits benutzt einen Filter, um herauszufinden, "
	+"welche Polarisation diese haben. Es gibt sowohl einen Filter auf +-Basis, "
	+"als auch einen auf x-Basis. Mit einem Filter auf +-Basis kann man vertikale "
	+"| und horizontale - Polarisierungen klar erkennen und den Bitwerten 0 oder 1 "
	+"zuordnen. Die anderen Polarisationen können nicht klar erkannt werden, und "
	+"ergeben deshalb zufällig 0 oder 1. Bei der x-Basis ist es genau andersherum.\n"
	+"• Alice, Eve und Bob: Alice und Bob bezeichnen die Kommunikationspartner, die "
	+"einen geheimen Schlüssel erstellen wollen. Eve ist eine dritte Partie, die "
	+"den Schlüsselaustausch belauscht.";
	
    private static final String SOURCE_CODE = 
     "1. Alice sendet zufällige Qubits mit zufälliger Basis über den Quantenkanal an Bob.\n"
    +"   -> Bob wählt zufällig Filter aus, um die ankommenden Qubits zu lesen.\n"
    +"2. Bob sendet seine Wahl über einen klassischen, authentifizierten Kanal an Alice.\n"
    +"3. Alice sendet Bob, welche der Filter er korrekt gewählt hat.\n"
    +"4. Beide senden sich gegenseitig eine vorher abgesprochene Untermenge der Bits, "
    +"für die sie die gleiche Basis verwendet haben.\n"
    +"5. Falls die Untermengen übereinstimmen, können die restlichen Bits als Schlüssel "
    +"verwendet werden.";
    
    private Text[] text;
    private int textlinec;
    private int nextParagraph;
    /**
     * Add a line of text below the headline. Max. 8 lines
     * @param line the textline
     * @param bold use the bold text prop
     */
    private void addText(String line, TextProperties prop) {

    	Node coords = (textlinec == 0) ?
    		(Node) new Coordinates(40, 85) : 
    		(Node) new Offset(0, 20*nextParagraph, text[textlinec-1], AnimalScript.DIRECTION_NW);
    		
    	//"text"+textlinec shall be continuous
    	text[textlinec] = lang.newText(coords, line, "", null, prop);
    	textlinec = (++textlinec)%31;
    	nextParagraph = 1;
    }
    /**
     * Hide all the text
     */
    private void clearText() {

    	textlinec = 0;
    	for (Text line : text) {
    		if(line != null)
    			line.hide();
		}
    }
    
    
    @SuppressWarnings("unused")
	private void bb84animation() {
    	
    	Text headline = lang.newText(new Coordinates(40, 34),
    			"BB84 - Quantenschlüsselaustausch",
    			"headline", null, headlineprop);
    			
		//FIXME Sometimes, texts are positioned wrong with AnimalScript. This random
		//empty text helps. Search for "hideme" in this document to find empty texts.
    	Text hideme = lang.newText(new Coordinates(0, 0), "", "", null, normtextprop);
    	addText("Heutige kryptographische Verfahren sind nicht hundertprozentig sicher. Angreifer können", normtextprop);
    	addText("unter Umständen den Datenverkehr von zwei Parteien abhören und den Schlüssel zum", normtextprop);
    	addText("Entschlüsseln der Daten herausfinden. Ein vollkommen sicherer Datenaustausch ließe sich", normtextprop);
    	addText("beispielsweise mit einem symmetrischen Verschlüsselungsverfahren implementieren, bei", normtextprop);
    	addText("dem die Kommunikationspartner sich auf einen Schlüssel einigen, der mithilfe des", normtextprop);
    	addText("Quantenschlüsselaustauschs übertragen wird. BB84 ist ein solches Protokoll, mit dem", normtextprop);
    	addText("Schlüssel sicher erstellt werden können. Es beruht auf den Gesetzen der Quantenmechanik", normtextprop);
    	addText("und seine Sicherheit lässt sich beweisen.", normtextprop);

    	lang.nextStep("Einleitung");
    	hideme.hide();
    	nextParagraph = 3;

    	addText("Begriffe", boldtextprop);
    	addText("• Qubits sind Photonen, die durch ihre Polarisierung Bits repräsentieren.", normtextprop);
    	
    	lang.nextStep();
    	addText("• Polarisierung: Die Qubits werden auf 4 verschiedene Weisen polarisiert. Vertikal |, horizontal -,", normtextprop);
    	addText("rechtsdiagonal / und linksdiagonal \\\\ ", normtextprop);
    	lang.nextStep();
    	addText("• Quantenkanal: Der Quantenkanal ist ein Lichtwellenleiter, über den ein Sender Qubits zu", normtextprop);
    	addText("einem Empfänger schickt.", normtextprop);
    	lang.nextStep();
    	addText("• +-Basis und x-Basis: Wenn ein Qubit vertikal | oder horizontal - polarisiert ist, wurde es auf", normtextprop);
    	addText("+-Basis erzeugt. Ist es rechtsdiagonal / oder linksdiagonal \\\\ polarisiert, wurde es auf x-Basis", normtextprop);
    	addText("erzeugt.", normtextprop);
    	lang.nextStep();
    	addText("• Filter: Der Empfänger von Qubits benutzt einen Filter, um herauszufinden, welche Polarisation", normtextprop);
    	addText("diese haben. Es gibt sowohl einen Filter auf +-Basis, als auch einen auf x-Basis. Mit einem", normtextprop);
    	addText("Filter auf +-Basis kann man vertikale | und horizontale - Polarisierungen klar erkennen und den", normtextprop);
    	addText("Bitwerten 0 oder 1 zuordnen. Die anderen Polarisationen können nicht klar erkannt werden,", normtextprop);
    	addText("und ergeben deshalb zufällig 0 oder 1. Bei der x-Basis ist es genau andersherum.", normtextprop);
    	lang.nextStep();
    	addText("• Alice, Eve und Bob: Alice und Bob bezeichnen die Kommunikationspartner. Eve ist eine dritte", normtextprop);
    	addText("Partei, die den Schlüsselaustausch belauscht.", normtextprop);

    	lang.nextStep();
    	
    	
    	MultipleSelectionQuestionModel base = new MultipleSelectionQuestionModel("base");
    	base.setPrompt("Welche_Qubit-Polarisationen_werden_von_einem_Filter_auf_x-Basis_klar_erkannt?");
    	base.addAnswer("/", 1, "/__ist_korrekt._");
    	base.addAnswer("-", 0, "-_ist_falsch._");
    	base.addAnswer("\\", 1, "\\__ist_korrekt._");
    	base.addAnswer("|", 0, "|_ist_falsch._");
    	lang.addMSQuestion(base);
    	
    	clearText();
    	addText("In den folgenden Beispielen sind Qubits, die vertikal oder horizontal polarisiert sind, lila", normtextprop);
    	addText("eingefärbt. Rechtsdiagonal und linksdiagonal polarisierte Qubits sind blau eingefärbt.", normtextprop);
    	addText("Die Repräsentation der Qubits in Bits ist wie folgt gewählt:", normtextprop);

    	Qubit q1 = new Qubit(lang, 55, 170, 0, 0);
    	Qubit q2 = new Qubit(lang, 95, 170, 1, 0);
    	Qubit q3 = new Qubit(lang, 135, 170, 0, 1);
    	Qubit q4 = new Qubit(lang, 175, 170, 1, 1);
    	lang.nextStep();

    	Text exampletext = lang.newText(new Coordinates(40,245), "Beispiele zur Übertragung von Qubits", "exampletext", null, boldtextprop);

    	
    	//Alice1
    	Circle aliceHead = lang.newCircle(new Coordinates(70, 300), 20, "aliceHead", null, head_prop);
		Polyline aliceBody = lang.newPolyline(getPolyLineFor(70, 320, 70, 380), "aliceBody", null);
		Polyline aliceRightHand = lang.newPolyline(getPolyLineFor(70, 330, 90, 350), "aliceRightHand", null);
		Polyline aliceLeftHand = lang.newPolyline(getPolyLineFor(70, 330, 50, 350), "aliceLeftHand", null);
		Polyline aliceLeftFoot = lang.newPolyline(getPolyLineFor(70, 380, 50, 400), "aliceLeftFoot", null);
		Polyline aliceRightFoot = lang.newPolyline(getPolyLineFor(70, 380, 90, 400), "aliceRightFoot", null);

		
    	//Bob2
    	Circle bobHead = lang.newCircle(new Coordinates(650,300), 20, "bobHead", null, head_prop);
		Polyline bobBody = lang.newPolyline(getPolyLineFor(650, 320, 650, 380), "bobBody", null);
		Polyline bobRightHand = lang.newPolyline(getPolyLineFor(650, 330, 670, 350), "bobRightHand", null);
		Polyline bobLeftHand = lang.newPolyline(getPolyLineFor(650, 330, 630, 350), "bobLeftHand", null);
		Polyline bobLeftFoot = lang.newPolyline(getPolyLineFor(650, 380, 670, 400), "bobLeftFoot", null);
		Polyline bobRightFoot = lang.newPolyline(getPolyLineFor(650, 380, 630, 400), "bobRightFoot", null);

		Rect quantenKanal1 = lang.newRect(new Coordinates(130,325), new Coordinates(570,345), "64", null, qchannel_prop);
		Polyline quantenKanal1_top = lang.newPolyline(getPolyLineFor(120,325,580,325), "qchannel1top", null, qcline_prop);  
		Polyline quantenKanal1_bot = lang.newPolyline(getPolyLineFor(120,345,580,345), "qchannel1bot", null, qcline_prop);  
    	Text quantenKanalName1 = lang.newText(new Coordinates(300,360), "Quantenkanal", "quantenKanalName", null, smalltextprop);
	
    	Text aliceName1 = lang.newText(new Offset(-17, 100, aliceHead, AnimalScript.DIRECTION_C), "Alice", "AliceName", null, normtextprop);
    	Text bobName = lang.newText(new Offset(-13, 100, bobHead, AnimalScript.DIRECTION_C), "Bob", "bobName", null, normtextprop);

    	Text leftdiagonalPolQubitName = lang.newText(new Offset(70, 0, aliceHead, AnimalScript.DIRECTION_C), "linksdiagonal polarisiertes Qubit", "verticalPolQubitName", null, smalltextprop);
    	Text filterName = lang.newText(new Offset(-180, -10, bobHead, AnimalScript.DIRECTION_C), "Filter auf x-Basis", "filterName", null, normtextprop);

    	Polyline fwArrow = lang.newPolyline(getPolyLineFor(130,385,220,385), "fwArrow", null, arrow_prop);    	
    	
    	//Filter Maschine 1
		filterMaschine11 = lang.newRect(new Coordinates(590,315), new Coordinates(620,355), "filterMaschine11", null, filter_prop);
		filterMaschineName11 =  lang.newText(new Offset(-4, -10, filterMaschine11, AnimalScript.DIRECTION_C), "x", "filterMaschineName11", null, normtextdepth10prop);
		

		
		
    	//Alice2
    	Circle aliceHead2 = lang.newCircle(new Coordinates(70,475), 20, "aliceHead2", null, head_prop);
		Polyline aliceBody2 = lang.newPolyline(getPolyLineFor(70,495, 70,555), "aliceBody2", null);
		Polyline aliceRightHand2 = lang.newPolyline(getPolyLineFor(70, 505, 90, 525), "aliceRightHand2", null);
		Polyline aliceLeftHand2 = lang.newPolyline(getPolyLineFor(70, 505, 50, 525), "aliceLeftHand2", null);
		Polyline aliceLeftFoot2 = lang.newPolyline(getPolyLineFor(70, 555, 50, 575), "aliceLeftFoot2", null);
		Polyline aliceRightFoot2 = lang.newPolyline(getPolyLineFor(70, 555, 90, 575), "aliceRightFoot2", null);
    	Text aliceName2 = lang.newText(new Offset(-17, 100, aliceHead2, AnimalScript.DIRECTION_C), "Alice", "AliceName2", null, normtextprop);

    	
    	//Bob2
    	Circle bobHead2 = lang.newCircle(new Coordinates(650,475), 20, "bobHead2", null, head_prop);
		Polyline bobBody2 = lang.newPolyline(getPolyLineFor(650, 495, 650, 555), "bobBody2", null);
		Polyline bobRightHand2 = lang.newPolyline(getPolyLineFor(650, 505, 670, 525), "bobRightHand2", null);
		Polyline bobLeftHand2 = lang.newPolyline(getPolyLineFor(650, 505, 630, 525), "bobLeftHand2", null);
		Polyline bobLeftFoot2 = lang.newPolyline(getPolyLineFor(650, 555, 670, 575), "bobLeftFoot2", null);
		Polyline bobRightFoot2 = lang.newPolyline(getPolyLineFor(650, 555, 630, 575), "bobRightFoot2", null);
    	Text bobName2 = lang.newText(new Offset(-13, 100, bobHead2, AnimalScript.DIRECTION_C), "Bob", "bobName", null, normtextprop);

    
    	Text verticalPolQubitName = lang.newText(new Offset(70, 0, aliceHead2, AnimalScript.DIRECTION_C), "vertikal polarisiertes Qubit", "horizontalPolQubitName", null, smalltextprop);
    	Text quantenKanalName2 = lang.newText(new Coordinates(300,533), "Quantenkanal", "quantenKanalName", null, smalltextprop);

    	Rect quantenKanal2 = lang.newRect(new Coordinates(130,500), new Coordinates(130+440,500+20), "64", null, qchannel_prop);
		Polyline quantenKanal2_top = lang.newPolyline(getPolyLineFor(120,500,580,500), "qchannel2top", null, qcline_prop);  
		Polyline quantenKanal2_bot = lang.newPolyline(getPolyLineFor(120,520,580,520), "qchannel2bot", null, qcline_prop);  

		filterMaschine21 = lang.newRect(new Coordinates(590,490), new Coordinates(590+30,490+40), "filterMaschine21",null, filter_prop);
		filterMaschineName21 =  lang.newText(new Offset(-4, -10, filterMaschine21, AnimalScript.DIRECTION_C), "x", "filterMaschineName", null, normtextdepth10prop);

    	Polyline fwArrow2 = lang.newPolyline(getPolyLineFor(130,560,220,560), "fwArrow", null, arrow_prop);  
    	Text filterName2 = lang.newText(new Offset(-180, -10, bobHead2, AnimalScript.DIRECTION_C), "Filter auf x-Basis", "filterName", null, normtextprop);

		
		Qubit movingQuibit1 = new Qubit(lang, 122, 335, 1, 1);

		Qubit movingQuibit2 = new Qubit(lang, 122, 510, 0, 0);

		// In diesem Schritt werden die Qubits bewegt
		lang.nextStep();
		
		leftdiagonalPolQubitName.hide();
		verticalPolQubitName.hide();
		filterName.hide();
		filterName2.hide();
		Text check =  lang.newText(new Coordinates(570, 400), "✓", "check", null, checktextprop);
		Text falsee =  lang.newText(new Coordinates(570, 540), "✗", "check", null, falsetextprop);

		
		hideme =  lang.newText(new Coordinates(380,273), "", "", null, smalltextprop); //random text so the next line is positioned correctly
		Text result1_1 =  lang.newText(new Coordinates(380,273), "Der Filter auf x-Basis kann das", "", null, smalltextprop);
		Text result1_2 =  lang.newText(new Coordinates(380,288), "linksdiagonal polarisierte Qubit klar", "", null, smalltextprop);
		Text result1_3 =  lang.newText(new Coordinates(380,303), "erkennen und ordnet es 1 zu!", "", null, smalltextprop);
		Text result2_1 =  lang.newText(new Coordinates(360,443), "Das vertikal polarisierte Qubit kann vom", "", null, smalltextprop);
		Text result2_2 =  lang.newText(new Coordinates(360,458), "Filter auf x-Basis nicht klar erkannt", "", null, smalltextprop);
		Text result2_3 =  lang.newText(new Coordinates(360,473), "werden. Es wird zufällig 0 oder 1.", "", null, smalltextprop);
		
		movingQuibit1.moveQubit(460, 0, 0, 345);
    	
		movingQuibit2.moveQubit(460, 0, 0, 345);
		
		
    	lang.nextStep();
    	hideme.hide();
    	q1.hide();
    	q2.hide();
    	q3.hide();
    	q4.hide();
    	movingQuibit1.hide();
    	movingQuibit2.hide();
    	check.hide();
    	falsee.hide();
    	result2_1.hide();
    	result2_2.hide();
    	result2_3.hide();
    	result1_1.hide();
    	result1_2.hide();
    	result1_3.hide();
    	//Alice2
    	aliceHead2.hide();
		aliceBody2.hide();
		aliceRightHand2.hide();
		aliceLeftHand2.hide();
		aliceLeftFoot2.hide();
		aliceRightFoot2.hide();
    	aliceName2.hide();
    	//Bob2
    	bobHead2.hide();
		bobBody2.hide();
		bobRightHand2.hide();
		bobLeftHand2.hide();
		bobLeftFoot2.hide();
		bobRightFoot2.hide();
    	bobName2.hide();
    	quantenKanal2.hide();
    	quantenKanal2_top.hide();
    	quantenKanal2_bot.hide();
    	quantenKanalName2.hide();
    	filterMaschine21.hide();
    	filterMaschineName21.hide();
    	fwArrow2.hide();
    	clearText();
    	veryImportantConstant = veryImportantConstant * 1;

		filterMaschine12 = lang.newRect(new Coordinates(590,355), new Coordinates(620,395), "filterMaschine12", null, filter_prop);
		filterMaschineName12 =  lang.newText(new Offset(-4, -8, filterMaschine12, AnimalScript.DIRECTION_C), "+", "filterMaschineName12", null, normtextdepth10prop);
		
    	quantenKanalName1.moveBy("translate", 0, -60, new MsTiming(0), new MsTiming(300));
    	
    	headline.setText("BB84 - Das Protokoll", new MsTiming(0), new MsTiming(0));
    	addText("Die Erzeugung des gemeinsamen, geheimen Schlüssels startet wie folgt:", normtextprop);
    	addText("Zunächst schickt Alice eine ausreichende Anzahl an Qubits über den Quantenkanal an Bob.", normtextprop);
    	addText("Es ist wichtig, dass die Basis der Qubits zufällig abgewechselt wird.", normtextprop);
    	addText("Bob wählt zum Auswerten jedes Qubits zufällig einen der zwei Filter auf +- oder x-Basis", normtextprop);
    	addText("aus, um die ankommenden Qubits auszuwerten.", normtextprop);

    	exampletext.setText("Beispiel:", new MsTiming(0), new MsTiming(0));
    	
    	nextParagraph = 14;
    	addText("Bob kann immer nur einen der beiden Filter zum Lesen eines Qubits auswählen, da die", normtextprop);
    	addText("Qubits beim Lesen zerstört werden.", normtextprop);
    	addText("Zu diesem Zeitpunkt kann er nicht feststellen, ob er den richtigen Filter gewählt hat!", normtextprop);
    	

    	abvtext = lang.newText(new Coordinates(40,511), "Alice sendet:", "", null, monotextprop);
    	abstext     = lang.newText(new Coordinates(64,528), "auf Basis:", "", null, monotextprop);
    	bbvtext   = lang.newText(new Coordinates(40,543), "Bob empfängt:", "", null, monotextprop);
    	bbstext       = lang.newText(new Coordinates(55,558), "mit Filter:", "", null, monotextprop);

    	Rect overlayalice = lang.newRect(new Coordinates(0,280), new Coordinates(0+70,280+100), "", null, whiteoverlay);
    	Rect overlaybob = lang.newRect(new Coordinates(650,280), new Coordinates(650+70,280+100), "", null, whiteoverlay);
    	
    	lang.nextStep("Das Protokoll");
    	
    	//---------------------------send Qubits----------------------------
    	
    	sendQubits(false);

		filterMaschine11    .hide();
		filterMaschineName11.hide();
		filterMaschine12    .hide();
		filterMaschineName12.hide();
		
		//move filter down in case it's up
		if(bobsbases[bobsbases.length-1] == 0) {
			filterMaschine11    .moveBy("translate", 0, 40, new MsTiming(0), new MsTiming(0));
			filterMaschineName11.moveBy("translate", 0, 40, new MsTiming(0), new MsTiming(0));
			filterMaschine12    .moveBy("translate", 0, 40, new MsTiming(0), new MsTiming(0));
			filterMaschineName12.moveBy("translate", 0, 40, new MsTiming(0), new MsTiming(0));
		}
			
		
		bobsbitvaluesText[alicebitvalues.length-1].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
		bobsbasesText[alicebitvalues.length-1].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));

		clearText();
		addText("Auf diese Weise erzeugt Bob eine Binärabfolge. Er hat sich gemerkt, in welcher", normtextprop);
		addText("Reihenfolge er die Filter verwendet hat, bzw. welche Basen er angenommen hat.", normtextprop);
		addText("Dies teilt er Alice über eine klassische, authentifizierte Leitung mit.", normtextprop);
		
		fwArrow.rotate(new Coordinates(340,326), 180, new MsTiming(0), new MsTiming(300));
		quantenKanal1    .moveBy("translate", 0, 40, new MsTiming(0), new MsTiming(300));
		quantenKanal1_top.moveBy("translate", 0, 40, new MsTiming(0), new MsTiming(300));
		quantenKanal1_bot.moveBy("translate", 0, 40, new MsTiming(0), new MsTiming(300));
		quantenKanalName1 .moveBy("translate", 0, 100, new MsTiming(0), new MsTiming(300));
		
		Rect classicalChannel = lang.newRect(new Coordinates(130,295), new Coordinates(570,315), "cchannel", new MsTiming(300), cchannel_prop);
		Polyline classicalChannel_top = lang.newPolyline(getPolyLineFor(120,295,580,295), "cchanneltop", new MsTiming(300), ccline_prop);  
		Polyline classicalChannel_bot = lang.newPolyline(getPolyLineFor(120,315,580,315), "cchannelbot", new MsTiming(300), ccline_prop);  
    	Text classicalChannelName = lang.newText(new Coordinates(290,328), "Klassische Leitung", "cchannelName", new MsTiming(300), smalltextprop);

		lang.nextStep();
		
		sendBasesBack();
	
		
		clearText();
		addText("Alice hat sich ebenfalls die Reihenfolge der Basen gemerkt, mit der sie die Qubits", normtextprop);
		addText("verschickt hat. Sie überprüft also, welche Filter Bob korrekt gewählt hat, und schickt", normtextprop);
		addText("ihm eine Rückmeldung darüber.", normtextprop);
		addText("Anschließend wissen beide, welche Qubits auf jeden Fall bei beiden gleich sind.", normtextprop);
		
		fwArrow.rotate(new Coordinates(350,270), 180, new MsTiming(0), new MsTiming(300));

    	hideme = lang.newText(new Coordinates(31,573), "", "", null, monotextprop);
    	sbstext = lang.newText(new Coordinates(31,573), "gleiche Basis:", "", null, monotextprop);

    	abvtext.changeColor("color", Color.LIGHT_GRAY, new MsTiming(45), new MsTiming(0));
    	bbvtext.changeColor("color", Color.LIGHT_GRAY, new MsTiming(45), new MsTiming(0));
    	for(int i = 0; i < alicebitvalues.length; i++) {
    		alicebitvaluesText[i].changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    		bobsbitvaluesText[i] .changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    	}

		lang.nextStep();
		
		hideme.hide();
		
    	sendBaseFeedback(true);
    	
    	clearText();
    	addText("Anschließend tauschen Alice und Bob eine bestimmte Untermenge an Bits aus, bei ", normtextprop);
    	addText("denen sie die gleichen Basen gewählt hatten. Auf diese Weise können sie feststellen, ", normtextprop);
    	addText("ob sie bei der Kommunikation belauscht wurden. (Dazu später mehr.) ", normtextprop);
    	addText("Stimmen ihre Untermengen überein, wurden sie höchstwahrscheinlich nicht belauscht. ", normtextprop);
    	addText("Sie benutzen die restlichen Bits für den Schlüssel. ", normtextprop);
    	addText("Sind jedoch einige Bits unterschiedlich, ist womöglich ein Angreifer schuld daran. Sie ", normtextprop);
    	addText("beginnen von neu.", normtextprop);
		
		fwArrow.rotate(new Coordinates(350,270), 180, new MsTiming(0), new MsTiming(300));

    	abvtext.changeColor("color", Color.BLACK, new MsTiming(45), new MsTiming(0));
    	bbvtext.changeColor("color", Color.BLACK, new MsTiming(45), new MsTiming(0));
		abvtext.setText("Alice Bitfolge:", new MsTiming(0), new MsTiming(300));
		bbvtext  .setText("Bobs Bitfolge:", new MsTiming(0), new MsTiming(300));
		abvtext.moveBy("translate", -16, 0, new MsTiming(0), new MsTiming(300));
		bbvtext  .moveBy("translate", -9, 0, new MsTiming(0), new MsTiming(300));
		abstext.changeColor("color", Color.LIGHT_GRAY, new MsTiming(45), new MsTiming(0));
		bbstext  .changeColor("color", Color.LIGHT_GRAY, new MsTiming(45), new MsTiming(0));
    	for(int i = 0; i < alicebitvalues.length; i++) {
    		alicebitvaluesText[i].changeColor("color", Color.BLACK, new MsTiming(45*i), new MsTiming(45));
    		bobsbitvaluesText[i] .changeColor("color", Color.BLACK, new MsTiming(45*i), new MsTiming(45));
    		alicebasesText[i].changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    		bobsbasesText[i] .changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    	}

		bsstext = lang.newText(new Coordinates(39,586), "Bobs Unterm.:", "bobssubsettext", new MsTiming(300), monotextprop);
    	asstext = lang.newText(new Coordinates(31,603), "Alice Unterm.:", "alicesubsettext", new MsTiming(300), monotextprop);

		lang.nextStep();
		
		subsetlength = (int) Math.round(countsamebases*subsetpercentage);
		
		sendSubset(false);
		
		fwArrow.rotate(new Coordinates(350,270), 180, new MsTiming(0), new MsTiming(300));
		lang.nextStep();
							
		sendSubset(true);
		
		clearText();
		addText("Die beiden Untermengen stimmen überein. Alice und Bob können nun die restlichen", normtextprop);
		addText("Bits als Schlüssel verwenden! Der Schlüssel lautet:", normtextprop);
		
		String key = "";
		int j = 0;
		for(int i = 0; i < alicebitvalues.length; i++) {

    		if(alicebases[i] == bobsbases[i] && j >= subsetlength) {
    			key += String.valueOf(alicebitvalues[i]);
    			continue;
    		}
    		else if(alicebases[i] == bobsbases[i])
    			j++;
    		alicebitvaluesText[i].changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    		bobsbitvaluesText[i] .changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
		}
		
		Text finalkey = lang.newText(new Coordinates(40,145), (key != "")?key:"Leider keine Bits übrig.", "finalkey", null, bigtextprop);
		fwArrow.hide();
		
		
		//------------------------------------------Lauschangriff-----------------------------------------
    	
		
		lang.nextStep();
    	for(int i = 0; i < alicebitvalues.length; i++) {
        	alicebitvaluesText[i].hide();
        	alicebasesText[i].hide();
        	bobsbitvaluesText[i].hide();
        	bobsbasesText[i].hide();
        	samebasesText[i].hide();
    	}
    	for(int i = 0; i < subsetlength; i++) {
        	alicesubsetText[i].hide();
        	bobssubsetText[i].hide();
    	}
		abstext.hide();
		abvtext.hide();
		bbstext.hide();
		bbvtext.hide();
		sbstext.hide();
		asstext.hide();
		bsstext.hide();
    	
    	headline.setText("BB84 - Lauschangriff", new MsTiming(0), new MsTiming(0));
    	clearText();
    	addText("Wie bereits angedeutet, können Alice und Bob feststellen, ob ihre Verbindung", normtextprop);
    	addText("belauscht wurde.", normtextprop);
    	addText("Angenommen, eine dritte Partei - Eve - hätte ganz am Anfang die Übertragung der", normtextprop);
    	addText("Qubits belauscht. Dann müsste Eve genau wie Bob ebenfalls Filter verwenden, um", normtextprop);
    	addText("die Qubits in Bits umzuwandeln.", normtextprop);
    	
    	fwArrow = lang.newPolyline(getPolyLineFor(130,385,220,385), "fwArrow", new MsTiming(300), arrow_prop); 
		quantenKanal1    .moveBy("translate", 0, -40, new MsTiming(0), new MsTiming(300));
		quantenKanal1_top.moveBy("translate", 0, -40, new MsTiming(0), new MsTiming(300));
		quantenKanal1_bot.moveBy("translate", 0, -40, new MsTiming(0), new MsTiming(300));
		quantenKanalName1 .moveBy("translate", 0, -100, new MsTiming(0), new MsTiming(300));
    	
		filterMaschine11.show(new MsTiming(300));
		filterMaschineName11.show(new MsTiming(300));
		filterMaschine12.show(new MsTiming(300));
		filterMaschineName12.show(new MsTiming(300));
		
		classicalChannel.hide();
		classicalChannel_bot.hide();
		classicalChannel_top.hide();
		classicalChannelName.hide();
		finalkey.hide();
		
		lang.nextStep("Lauschangriff");

    	addText("Beim Lesen zerstört sie jedoch die Qubits. Sie kann lediglich ihre eigenen Messungen", normtextprop);
    	addText("an Bob weiter schicken, und hoffen, dass es nicht auffällt.", normtextprop);
		
    	abvtext = lang.newText(new Coordinates(40,480), "Alice sendet:", "", null, monotextprop);
    	abstext = lang.newText(new Coordinates(64,496), "auf Basis:", "", null, monotextprop);
    	erbvtext = lang.newText(new Coordinates(40,510), "Eve empfängt:", "", null, monotextprop);
    	erbstext = lang.newText(new Coordinates(56,525), "mit Filter:", "", null, monotextprop);
    	esbvtext = lang.newText(new Coordinates(56,555), "Eve sendet:", "", null, monotextprop);
    	esbstext = lang.newText(new Coordinates(64,570), "auf Basis:", "", null, monotextprop);
    	bbvtext = lang.newText(new Coordinates(40,585), "Bob empfängt:", "", null, monotextprop);
    	bbstext = lang.newText(new Coordinates(56,600), "mit Filter:", "", null, monotextprop);
    	
    	quantenKanal1.hide();
    	quantenKanal1_bot.hide();
    	quantenKanal1_top.hide();
    	
		quantenKanal1     = lang.newRect(new Coordinates(130,325), new Coordinates(130+160,325+20), "64", null, qchannel_prop);
		quantenKanal1_top = lang.newPolyline(getPolyLineFor(120,325,300,325), "qchannel1top", null, qcline_prop);
		quantenKanal1_bot = lang.newPolyline(getPolyLineFor(120,345,300,345), "qchannel1bot", null, qcline_prop);
		
		quantenKanal2     = lang.newRect(new Coordinates(410,325), new Coordinates(410+160,345), "64", null, qchannel_prop);
		quantenKanal2_top = lang.newPolyline(getPolyLineFor(400,325,580,325), "qchannel2top", null, qcline_prop);
		quantenKanal2_bot = lang.newPolyline(getPolyLineFor(400,345,580,345), "qchannel2bot", null, qcline_prop);
		
    	quantenKanalName2 = lang.newText(new Coordinates(300,303), "Quantenkanal", "quantenKanalName", null, smalltextprop);
    	quantenKanalName2.moveBy("translate", 150, 0, new MsTiming(0), new MsTiming(300));
    	quantenKanalName1.moveBy("translate", -130, 0, new MsTiming(0), new MsTiming(300));

    	Rect tmpqquantenK     = lang.newRect(new Coordinates(290,325), new Coordinates(290+160,345), "64", null, qchannel_prop);
    	Polyline tmpqquaK_top = lang.newPolyline(getPolyLineFor(280,325,460,325), "qchannel2top", null, qcline_prop);
    	Polyline tmpqquaK_bot = lang.newPolyline(getPolyLineFor(280,345,460,345), "qchannel2bot", null, qcline_prop);

    	MsTiming ticks200 = new MsTiming(300);
    	tmpqquantenK.moveBy("translate", 120, 0, new MsTiming(0), ticks200);
    	tmpqquaK_top.moveBy("translate", 120, 0, new MsTiming(0), ticks200);
    	tmpqquaK_bot.moveBy("translate", 120, 0, new MsTiming(0), ticks200);
    	tmpqquantenK.hide(new MsTiming(300));
    	tmpqquaK_top.hide(new MsTiming(300));
    	tmpqquaK_bot.hide(new MsTiming(300));
    	
    	//Eve
    	Circle eveHead        = lang.newCircle (new Coordinates(370, 300), 20, "eveHead", ticks200, head_prop);
		Polyline eveBody      = lang.newPolyline(getPolyLineFor(370, 320, 370, 380), "eveBody", ticks200);
		Polyline eveRightHand = lang.newPolyline(getPolyLineFor(370, 330, 390, 350), "eveRightHand", ticks200);
		Polyline eveLeftHand  = lang.newPolyline(getPolyLineFor(370, 330, 350, 350), "eveLeftHand", ticks200);
		Polyline eveLeftFoot  = lang.newPolyline(getPolyLineFor(370, 380, 350, 400), "eveLeftFoot", ticks200);
		Polyline eveRightFoot = lang.newPolyline(getPolyLineFor(370, 380, 390, 400), "eveRightFoot", ticks200);
    	Text eveName          = lang.newText(new Offset(-12, 100, eveHead, AnimalScript.DIRECTION_C), "Eve", "eveName", ticks200, normtextprop);
    	Rect overlayeve = lang.newRect(new Coordinates(330,280), new Coordinates(330+40,280+100), "", ticks200, whiteoverlay);

		filterMaschine21 = lang.newRect(new Coordinates(310,315), new Coordinates(340,355), "filterMaschine21", ticks200, filter_prop);
		filterMaschineName21 =  lang.newText(new Offset(-4, -10, filterMaschine21, AnimalScript.DIRECTION_C), "x", "filterMaschineName21", ticks200, normtextdepth10prop);
		filterMaschine22 = lang.newRect(new Coordinates(310,355), new Coordinates(340,395), "filterMaschine22", ticks200, filter_prop);
		filterMaschineName22 =  lang.newText(new Offset(-4, -8, filterMaschine22, AnimalScript.DIRECTION_C), "+", "filterMaschineName22", ticks200, normtextdepth10prop);
    	lang.nextStep();
    	
    	//----------------------------send Qubits again!-------------------------------
    	
    	sendQubits(true);
    	
		filterMaschine11    .hide();
		filterMaschineName11.hide();
		filterMaschine12    .hide();
		filterMaschineName12.hide();
		filterMaschine21    .hide();
		filterMaschineName21.hide();
		filterMaschine22    .hide();
		filterMaschineName22.hide();
		
		erbvtext.hide();
		erbstext.hide();
		esbvtext.hide();
		esbstext.hide();

		bobsbitvaluesText[alicebitvalues.length-1].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
		bobsbasesText[alicebitvalues.length-1].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));

		clearText();
		addText("Wie zuvor tauschen Alice und Bob nun die benutzen Filter aus.", normtextprop);
		
		fwArrow.rotate(new Coordinates(340,326), 180, new MsTiming(0), ticks200);
		quantenKanal1    .moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		quantenKanal1_top.moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		quantenKanal1_bot.moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		quantenKanalName1.moveBy("translate", 0, 100, new MsTiming(0), ticks200);
		quantenKanal2    .moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		quantenKanal2_top.moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		quantenKanal2_bot.moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		quantenKanalName2.moveBy("translate", 0, 100, new MsTiming(0), ticks200);
		
    	eveHead     .moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		eveBody     .moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		eveRightHand.moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		eveLeftHand .moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		eveLeftFoot .moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		eveRightFoot.moveBy("translate", 0, 40, new MsTiming(0), ticks200);
    	eveName     .moveBy("translate", 0, 40, new MsTiming(0), ticks200);
    	overlayeve  .moveBy("translate", 0, 40, new MsTiming(0), ticks200);
		
		classicalChannel.show(ticks200);
		classicalChannel_top.show(ticks200);
		classicalChannel_bot.show(ticks200);
		classicalChannelName.moveBy("translate", -100, 0, new MsTiming(0), new MsTiming(0));
    	classicalChannelName.show(ticks200);
    	
    	for(int i = 0; i < alicebitvalues.length; i++) {
    		evesRecbitvaluesText[i].hide();
    		evesRecbasesText[i].hide();
    		evesSendbitvaluesText[i].hide();
    		evesSendbasesText[i].hide();
    		alicebitvaluesText[i].moveBy("translate", 0, 32, new MsTiming(0), ticks200);
    		alicebasesText[i].moveBy("translate", 0, 32, new MsTiming(0), ticks200);
    		bobsbitvaluesText[i].moveBy("translate", 0, -42, new MsTiming(0), ticks200);
    		bobsbasesText[i].moveBy("translate", 0, -42, new MsTiming(0), ticks200);
    	}
    	abvtext.moveBy("translate", 0, 32, new MsTiming(0), ticks200);
    	abstext.moveBy("translate", 0, 32, new MsTiming(0), ticks200);
    	bbvtext.moveBy("translate", 0, -42, new MsTiming(0), ticks200);
    	bbstext.moveBy("translate", 0, -42, new MsTiming(0), ticks200);

		lang.nextStep();
		
		sendBasesBack();
		

		fwArrow.rotate(new Coordinates(350,270), 180, new MsTiming(0), new MsTiming(300));

    	sbstext.show();

    	abvtext.changeColor("color", Color.LIGHT_GRAY, new MsTiming(45), new MsTiming(0));
    	bbvtext.changeColor("color", Color.LIGHT_GRAY, new MsTiming(45), new MsTiming(0));
    	for(int i = 0; i < alicebitvalues.length; i++) {
    		alicebitvaluesText[i].changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    		bobsbitvaluesText[i] .changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    	}

		lang.nextStep();
		
    	sendBaseFeedback(false);
    	
    	clearText();
    	addText("Anschließend wird eine Untermenge der Bits mit gleicher Basis ausgetauscht.", normtextprop);
		
		fwArrow.rotate(new Coordinates(350,270), 180, new MsTiming(0), new MsTiming(300));

    	abvtext.changeColor("color", Color.BLACK, new MsTiming(45), new MsTiming(0));
    	bbvtext.changeColor("color", Color.BLACK, new MsTiming(45), new MsTiming(0));
		abvtext.setText("Alice Bitfolge:", new MsTiming(0), new MsTiming(300));
		bbvtext  .setText("Bobs Bitfolge:", new MsTiming(0), new MsTiming(300));
		abvtext.moveBy("translate", -16, 0, new MsTiming(0), new MsTiming(300));
		bbvtext  .moveBy("translate", -9, 0, new MsTiming(0), new MsTiming(300));
		abstext.changeColor("color", Color.LIGHT_GRAY, new MsTiming(45), new MsTiming(0));
		bbstext  .changeColor("color", Color.LIGHT_GRAY, new MsTiming(45), new MsTiming(0));
    	for(int i = 0; i < alicebitvalues.length; i++) {
    		alicebitvaluesText[i].changeColor("color", Color.BLACK, new MsTiming(45*i), new MsTiming(45));
    		bobsbitvaluesText[i] .changeColor("color", Color.BLACK, new MsTiming(45*i), new MsTiming(45));
    		alicebasesText[i].changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    		bobsbasesText[i] .changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    	}
    	
		bsstext.show(ticks200);
    	asstext.show(ticks200);

		lang.nextStep();
		
		sendSubset(false);
		
		fwArrow.rotate(new Coordinates(350,270), 180, new MsTiming(0), new MsTiming(300));
		lang.nextStep();
							
		sendSubset(true);
		
		clearText();
		
		boolean evescoverisblown = false;
		boolean aliceandbobhavediffkeys = false;
		key = "";
		j = 0;
		for(int i = 0; i < alicebitvalues.length; i++) {

    		if(alicebases[i] == bobsbases[i] && j >= subsetlength) {
    			if(alicebitvalues[i] != bobsbitvalues[i])
    				aliceandbobhavediffkeys = true;
    			continue;
    		}
    		else if(alicebases[i] == bobsbases[i]) {
    			if(alicebitvalues[i] != bobsbitvalues[i])
    				evescoverisblown = true;
    			j++;
    		}
    		alicebitvaluesText[i].changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
    		bobsbitvaluesText[i] .changeColor("color", Color.LIGHT_GRAY, new MsTiming(45*i), new MsTiming(45));
		}
		
		if(evescoverisblown) {
			addText("Die Untermengen stimmen nicht überein! Alice und Bob können die restlichen Bits", normtextprop);
			addText("also nicht als Schlüssel verwenden!", normtextprop);
			nextParagraph = 2;
			addText("Sie starten das Protokoll von vorne.", bigtextprop);

	    	MultipleChoiceQuestionModel evequestion = new MultipleChoiceQuestionModel("eve");
	    	evequestion.setPrompt("Wieso_ist_Eve_bei_ihrem_Lauschangriff_aufgeflogen?");
	    	evequestion.addAnswer("Weil_die_restlichen_Bits_übereinstimmen.", 0, "Falsch._Alice_und_Bob_erkennen_Eve_daran,_dass_die_ausgetauschten_Untermengen_verschieden_sind.");
	    	evequestion.addAnswer("Weil_die_ausgetauschten_Untermengen_übereinstimmen.", 0, "Falsch._Alice_und_Bob_erkennen_Eve_daran,_dass_die_Untermengen_NICHT_übereinstimmen.");
	    	evequestion.addAnswer("Weil_die_ausgetauschten_Untermengen_nicht_übereinstimmen.", 1, "Korrekt!");
	    	lang.addMCQuestion(evequestion);
		}
		else {
			addText("Eve konnte in diesem Beispiel den Schlüsselaustausch unbemerkt belauschen, da", normtextprop);
			addText("Alice und Bob zufällig die gleichen Untermengen hatten.", normtextprop);
			if(aliceandbobhavediffkeys) {
				addText("Wie man an den restlichen Bits sehen kann, haben Alice und Bob sogar verschiedene", normtextprop);
				addText("Schlüssel!", normtextprop);
			}
			nextParagraph = 2;
			addText("Um dieses Problem zu vermeiden, sollte man einen längeren Schlüssel benutzen.", normtextprop);
			
	    	MultipleChoiceQuestionModel evequestion = new MultipleChoiceQuestionModel("eve");
	    	evequestion.setPrompt("Wieso_ist_Eve__in_diesem_Beispiel_bei_ihrem_Lauschangriff_nicht_aufgeflogen?");
	    	evequestion.addAnswer("Weil_die_restlichen_Bits_übereinstimmen.", 0, "Falsch._Eve_bleibt_unentdeckt,_da_Bob_zufällig_die_gleiche_Untermenge_wie_Alice_hat._Die_restlichen_Bits_werden_als_Schlüssel_verwendet.");
	    	evequestion.addAnswer("Weil_die_ausgetauschten_Untermengen_zufällig_übereinstimmen.", 1, "Korrekt!");
	    	evequestion.addAnswer("Weil_die_ausgetauschten_Untermengen_nicht_übereinstimmen.", 0, "Falsch._Eve_bleibt_unentdeckt,_da_Bob_zufällig_die_gleiche_Untermenge_wie_Alice_hat.");
	    	lang.addMCQuestion(evequestion);
		}
		
		fwArrow.hide();
		
		
		lang.nextStep();
		
		headline.setText("BB84 - Zusammenfassung", new MsTiming(0), new MsTiming(0));
		
		exampletext.hide();
		abvtext.hide();
		abstext.hide();
		asstext.hide();
		bbvtext.hide();
		bbstext.hide();
		bsstext.hide();
		sbstext.hide();
    	for(int i = 0; i < alicebitvalues.length; i++) {
        	alicebitvaluesText[i].hide();
        	alicebasesText[i].hide();
        	bobsbitvaluesText[i].hide();
        	bobsbasesText[i].hide();
        	samebasesText[i].hide();
    	}
    	for(int i = 0; i < subsetlength; i++) {
        	alicesubsetText[i].hide();
        	bobssubsetText[i].hide();
    	}
		
    	//Alice
    	aliceHead.hide();
		aliceBody.hide();
		aliceRightHand.hide();
		aliceLeftHand.hide();
		aliceLeftFoot.hide();
		aliceRightFoot.hide();
    	aliceName1.hide();
    	//Bob
    	bobHead.hide();
		bobBody.hide();
		bobRightHand.hide();
		bobLeftHand.hide();
		bobLeftFoot.hide();
		bobRightFoot.hide();
    	bobName.hide();
    	//Eve
    	eveHead.hide();
		eveBody.hide();
		eveRightHand.hide();
		eveLeftHand.hide();
		eveLeftFoot.hide();
		eveRightFoot.hide();
    	eveName.hide();
    	quantenKanal1.hide();
    	quantenKanal1_top.hide();
    	quantenKanal1_bot.hide();
    	quantenKanalName1.hide();
    	quantenKanal2.hide();
    	quantenKanal2_top.hide();
    	quantenKanal2_bot.hide();
    	quantenKanalName2.hide();
    	classicalChannel.hide();
    	classicalChannel_top.hide();
    	classicalChannel_bot.hide();
    	classicalChannelName.hide();
    	
		
		clearText();
		addText("Algorithmus:", boldtextprop);
		addText("1. Alice sendet zufällige Qubits mit zufälliger Basis über den Quantenkanal an Bob.", normtextprop);
		addText("    -> Bob wählt zufällig Filter aus, um die ankommenden Qubits zu lesen.", normtextprop);
		addText("2. Bob sendet seine Wahl über einen klassischen, authentifizierten Kanal an Alice.", normtextprop);
		addText("3. Alice sendet Bob, welche der Filter er korrekt gewählt hat.", normtextprop);
		addText("4. Beide senden sich gegenseitig eine vorher abgesprochene Untermenge der Bits, für", normtextprop);
		addText("    die sie die gleiche Basis verwendet haben.", normtextprop);
		addText("5. Falls die Untermengen übereinstimmen, können die restlichen Bits als Schlüssel", normtextprop);
		addText("    verwendet werden.", normtextprop);
		
		nextParagraph = 2;
		addText("Warum ist Eve aufgeflogen?", boldtextprop);
		addText("Wenn Bob einen Filter wählt, dann besteht eine 50%-ige Wahrscheinlichkeit, dass er einen", normtextprop);
		addText("Filter mit der gleichen Basis wie Alice wählt. Zusätzlich sind von den Bits, bei denen", normtextprop);
		addText("Alice und Bob verschiedene Basen gewählt haben, 50% zufällig gleich. Somit sind im Schnitt", normtextprop);
		addText("75% aller Bits von Alice und Bob gleich.", normtextprop);
		addText("Das gleiche gilt für Eve. Sie hat ebenfalls 75% gleiche Bits wie Alice. Wenn Sie jetzt", normtextprop);
		addText("aber ihre Messungen an Bob weiter schickt, hat Bob am Ende weniger als 75% gleiche Bits", normtextprop);
		addText("wie Alice.", normtextprop);
		addText("Wenn Alice und Bob die Untermengen austauschen, bemerken sie das, und können Eve entlarven.", normtextprop);

		nextParagraph = 2;
		addText("Anmerkungen", boldtextprop);
		addText("Es gibt verschiedene Ausführungen des BB84-Protokolls. Ebenfalls kann sich Eve anders", normtextprop);
		addText("verhalten. Auch wird nicht jedes Qubit immer korrekt erkannt, wenn die gleichen Basen", normtextprop);
		addText("gewählt wurden. Um das Protokoll aber möglichst einfach zu visualisieren, haben wir", normtextprop);
		addText("die gezeigte Ausführung gewählt.", normtextprop);
		addText("Leider ist das Verfahren nicht mehr 100%-ig sicher. Mittlerweile ist es schon mehreren", normtextprop);
		addText("Wissenschaftlern gelungen, verschlüsselte Nachrichten abzuhören.", normtextprop);

		lang.nextStep("Zusammenfassung");
    }
    
    private void sendQubits(boolean isinterc) {

    	Qubit active;
    	Qubit toEve = null;
    	Qubit toBob = null;
    	for(int i = 0; i < alicebitvalues.length; i++) {
    		
			Text hideme = lang.newText(new Offset(10 + i*8, 0, abvtext, AnimalScript.DIRECTION_NE), "", "", null, monotextprop);
			alicebitvaluesText[i] = lang.newText(new Offset(10 + i*8, 0, abvtext, AnimalScript.DIRECTION_NE), String.valueOf(alicebitvalues[i]), "", null, monotextprop);
			alicebitvaluesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
			alicebasesText[i] = lang.newText(new Offset(10 + i*8, 0, abstext, AnimalScript.DIRECTION_NE), (alicebases[i]==0)?"+":"x", "", null, monotextprop);
			alicebasesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
			
			active = new Qubit(lang, 47,336, alicebitvalues[i], alicebases[i]);
			active.moveQubit(75, 0, (i==0)?0:(isinterc?15:285), 60);

			lang.nextStep();
			hideme.hide();
			
			
			if(isinterc) {
				
				if(toBob != null)
					toBob.hide();

				evesbitvalues[i] = alicebitvalues[i];
				if(alicebases[i] != evesbases[i])
					evesbitvalues[i] = Math.round((float) Math.random());

				alicebitvaluesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
				alicebasesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
				
				evesRecbitvaluesText[i] = lang.newText(new Offset(10 + i*8, 0, erbvtext, AnimalScript.DIRECTION_NE), String.valueOf(evesbitvalues[i]), "", null, monotextprop);
				evesRecbitvaluesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
				evesRecbasesText[i] = lang.newText(new Offset(10 + i*8, 0, erbstext, AnimalScript.DIRECTION_NE), (evesbases[i]==0)?"+":"x", "", null, monotextprop);
				evesRecbasesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));

				if(i>0) {
					bobsbitvaluesText[i-1].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
					bobsbasesText[i-1].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
				}
				
				if(i==0 && 1 != evesbases[i] || i>0 && evesbases[i-1] != evesbases[i]) {
					
					int dy = (((i==0)?0:evesbases[i-1]) < evesbases[i]) ? 40 : -40;
					filterMaschine21    .moveBy("translate", 0, dy, new MsTiming(0), new MsTiming(75));
					filterMaschineName21.moveBy("translate", 0, dy, new MsTiming(0), new MsTiming(75));
					filterMaschine22    .moveBy("translate", 0, dy, new MsTiming(0), new MsTiming(75));
					filterMaschineName22.moveBy("translate", 0, dy, new MsTiming(0), new MsTiming(75));
				}
				
				toEve = active;
				toEve.moveQubit(180, 0, 0, 75);
				
				lang.nextStep();
				
				toEve.hide();
				
				evesRecbitvaluesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
				evesRecbasesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
				
	    		
				evesSendbitvaluesText[i] = lang.newText(new Offset(10 + i*8, 0, esbvtext, AnimalScript.DIRECTION_NE), String.valueOf(evesbitvalues[i]), "", null, monotextprop);
				evesSendbitvaluesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
				evesSendbasesText[i] = lang.newText(new Offset(10 + i*8, 0, esbstext, AnimalScript.DIRECTION_NE), (evesbases[i]==0)?"+":"x", "", null, monotextprop);
				evesSendbasesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));

				active = new Qubit(lang, 347,336, evesbitvalues[i], evesbases[i]);
				active.moveQubit(55, 0, (i==0)?0:15, 60);

				lang.nextStep();
				
				bobsbitvalues[i] = evesbitvalues[i];
				if(evesbases[i] != bobsbases[i])
					bobsbitvalues[i] = Math.round((float) Math.random());
				
				evesSendbitvaluesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
				evesSendbasesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
			}
			
			else {
				
				if((i == 2 || i == 4) && (i+1) < alicebitvalues.length) {

			    	MultipleChoiceQuestionModel qubit = new MultipleChoiceQuestionModel("qubit"+i);
			    	qubit.setPrompt("Welchen Bitwert wird Bob im nächsten Schritt empfangen, wenn er den Filter auf "+((bobsbases[i+1]==0)?"+":"x")+"-Basis wählt?");
			    	boolean na = alicebases[i+1]==bobsbases[i+1] && alicebitvalues[i+1] == 1;
			    	qubit.addAnswer("1", na?1:0, na?"Korrekt!":((evesbases[i+1]!=bobsbases[i+1])?"Leider falsch, da sie dann unterschiedliche Basen haben würden.":"Leider falsch, da Bob den korrekten Bitwert empfangen würde, wenn er die gleiche Basis wählen würde."));
			    	
			    	na = alicebases[i+1]==bobsbases[i+1] && alicebitvalues[i+1] == 0;
			    	qubit.addAnswer("0", na?1:0, na?"Korrekt!":((alicebases[i+1]!=bobsbases[i+1])?"Leider falsch, da sie dann unterschiedliche Basen haben würden.":"Leider falsch, da Bob den korrekten Bitwert empfangen würde, wenn er die gleiche Basis wählen würde."));
			    	
			    	na = alicebases[i+1]!=bobsbases[i+1];
			    	qubit.addAnswer("zufällig 0 oder 1", na?0:1, na?"Korrekt!":"Leider falsch, da Alice und Eve die selben Basen haben würden.");
			    	lang.addMCQuestion(qubit);
				}
				
				bobsbitvalues[i] = alicebitvalues[i];
				if(alicebases[i] != bobsbases[i])
					bobsbitvalues[i] = Math.round((float) Math.random());
			
				alicebitvaluesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
				alicebasesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
			}
			
			
			
			
			
			
			bobsbitvaluesText[i] = lang.newText(new Offset(10 + i*8, 0, bbvtext, AnimalScript.DIRECTION_NE), String.valueOf(bobsbitvalues[i]), "", null, monotextprop);
			bobsbitvaluesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
			bobsbasesText[i] = lang.newText(new Offset(10 + i*8, 0, bbstext, AnimalScript.DIRECTION_NE), (bobsbases[i]==0)?"+":"x", "", null, monotextprop);
			bobsbasesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));

			if(i>0) {
				bobsbitvaluesText[i-1].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
				bobsbasesText[i-1].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
			}
			
			if(i==0 && 1 != bobsbases[i] || i>0 && bobsbases[i-1] != bobsbases[i]) {
				
				int dy = (((i==0)?0:bobsbases[i-1]) < bobsbases[i]) ? 40 : -40;
				filterMaschine11    .moveBy("translate", 0, dy, new MsTiming(0), new MsTiming(isinterc?75:300));
				filterMaschineName11.moveBy("translate", 0, dy, new MsTiming(0), new MsTiming(isinterc?75:300));
				filterMaschine12    .moveBy("translate", 0, dy, new MsTiming(0), new MsTiming(isinterc?75:300));
				filterMaschineName12.moveBy("translate", 0, dy, new MsTiming(0), new MsTiming(isinterc?75:300));
			}
			
			Qubit hide = toBob;
			toBob = active;
			toBob.moveQubit(isinterc?180:460, 0, 0, isinterc?75:345);
			if(hide != null)
				hide.hide();
		}
		lang.nextStep();

		if(toBob != null)
			toBob.hide();
    }
    
    private void sendBasesBack() {

    	//borderedsquare.set(AnimationPropertiesKeys.FILL_PROPERTY,  Color.WHITE);
    	
    	Square active;
    	Text   activeName;
    	Square last = null;
    	Text   lastName = null;
    	for(int i = 0; i < alicebitvalues.length; i++) {

			bobsbasesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
			
			active = lang.newSquare(new Coordinates(660,290), 30, "base"+i, null, borderedsquare);
			activeName = lang.newText(new Offset(-2, -8, active, AnimalScript.DIRECTION_C), bobsbasesText[i].getText(), "basetext"+i, null, normtextdepth13prop);
			active    .moveBy("translate", -100, 0, new MsTiming((i==0)?0:285), new MsTiming(60));
			activeName.moveBy("translate", -100, 0, new MsTiming((i==0)?0:285), new MsTiming(60));

			lang.nextStep();
			
			bobsbasesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
			
			Square hide = last;
			Text hideName = lastName;
			last = active;
			lastName = activeName;
			last    .moveBy("translate", -480, 0, new MsTiming(0), new MsTiming(345));
			lastName.moveBy("translate", -480, 0, new MsTiming(0), new MsTiming(345));
			if(hide != null) {
				hide.hide();
				hideName.hide();
			}
		}
		lang.nextStep();
		if(last != null) {
	    	last.hide();
	    	lastName.hide();
		}
    }
	
    private void sendBaseFeedback(boolean showquestions) {

    	Square active;
    	Text   activeName;
    	Square last = null;
    	Text   lastName = null;
    	for(int i = 0; i < alicebitvalues.length; i++) {

			String feedback;
			String feedbackt;
			SquareProperties prop;
			if(bobsbases[i] == alicebases[i]) {
				prop = borderedGreensquare;
				feedback = "✔";
				feedbackt = "✔";
				countsamebases++;
			}
			else {
				prop = borderedRedsquare;
				feedback = "✘";
				feedbackt = " ";
			}

    		Text hideme = lang.newText(new Offset(10 + i*8, 3, sbstext, AnimalScript.DIRECTION_NE), "", "feedbackz"+i, null, smallertextprop);
    		samebasesText[i] = lang.newText(new Offset(10 + i*8, 3, sbstext, AnimalScript.DIRECTION_NE), feedbackt, "feedback"+i, null, smallertextprop);

			bobsbasesText [i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
			alicebasesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
			samebasesText [i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));

			if(showquestions && (i == 2 || i == 4) && (i+1) < alicebitvalues.length) {

		    	MultipleChoiceQuestionModel feedbackquestion = new MultipleChoiceQuestionModel("basefeedback"+i);
		    	feedbackquestion.setPrompt("Welches_Feedback_sendet_Alice_in_diesem_Schritt_an_Bob?");
		    	boolean na = alicebases[i+1]==bobsbases[i+1];
		    	feedbackquestion.addAnswer("✔", na?1:0, na?"Korrekt!":"Leider_falsch,_da_Alice_und_Bob_in_diesem_Schritt_verschiedene_Basen_haben.");
		    	
		    	feedbackquestion.addAnswer("✘", !na?1:0, !na?"Korrekt!":"Leider_falsch,_da_Alice_und_Bob_in_diesem_Schritt_die_gleichen_Basen_haben.");
		    	
		    	lang.addMCQuestion(feedbackquestion);
				lang.nextStep();
			}

			active = lang.newSquare(new Coordinates(30,290), 30, "feedback"+i, null, prop);
			activeName = lang.newText(new Offset(-6, -6, active, AnimalScript.DIRECTION_C), feedback, "feedbacktext"+i, null, normtextdepth13prop);
			active    .moveBy("translate", 80, 0, new MsTiming((i==0)?0:285), new MsTiming(60));
			activeName.moveBy("translate", 80, 0, new MsTiming((i==0)?0:285), new MsTiming(60));


			lang.nextStep();
			
			hideme.hide();
			
			bobsbasesText [i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
			alicebasesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
			samebasesText [i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
			
			Square hide = last;
			Text hideName = lastName;
			last = active;
			lastName = activeName;
			last    .moveBy("translate", 500, 0, new MsTiming(0), new MsTiming(345));
			lastName.moveBy("translate", 500, 0, new MsTiming(0), new MsTiming(345));
			if(hide != null) {
				hide.hide();
				hideName.hide();
			}
		}
		lang.nextStep();
		if(last != null) {
	    	last.hide();
	    	lastName.hide();
		}
    }

    private void sendSubset(boolean alicetobob) {
    	
    	Circle active;
    	Text   activeName;
    	Circle last = null;
    	Text   lastName = null;
    	int j = 0;
    	for(int i = 0; i < alicebitvalues.length; ++i) {
    		
    		if(j >= subsetlength)
    			break;
    		if(alicebases[i] != bobsbases[i])
    			continue;
    		
    		Text hideme = null;
    		if(alicetobob) {
    			alicesubset[j] = alicebitvalues[i];
    			hideme = lang.newText(new Offset(10 + i*8, 0, asstext, AnimalScript.DIRECTION_NE), "", "alicesubsettmp"+j, null, monotextprop);
    			alicesubsetText[j] = lang.newText(new Offset(10 + i*8, 0, asstext, AnimalScript.DIRECTION_NE), String.valueOf(alicesubset[j]), "alicesubset"+j, null, monotextprop);
    			alicesubsetText[j]   .changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
    			alicebitvaluesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
    		}
    		if(!alicetobob) {
    			bobssubset[j] = bobsbitvalues[i];
    			hideme = lang.newText(new Offset(10 + i*8, 0, bsstext, AnimalScript.DIRECTION_NE), "", "bobssubsettmp"+j, null, monotextprop);
    			bobssubsetText[j] = lang.newText(new Offset(10 + i*8, 0, bsstext, AnimalScript.DIRECTION_NE), String.valueOf(bobssubset[j]), "bobssubset"+j, null, monotextprop);
    			bobssubsetText[j]   .changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
    			bobsbitvaluesText[i].changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
    		}
			samebasesText[i]     .changeColor("color", highlightColor, new MsTiming(0), new MsTiming(0));
			
			active = lang.newCircle(new Coordinates((alicetobob?40:677),306), 15, "subset"+i, null, borderedcircle);
			activeName = lang.newText(new Offset(-4, -6, active, AnimalScript.DIRECTION_C), String.valueOf(bobsbitvalues[i]), "basetext"+i, null, normtextdepth13prop);
			active    .moveBy("translate", (alicetobob?80:-100), 0, new MsTiming((j==0)?0:285), new MsTiming(60));
			activeName.moveBy("translate", (alicetobob?80:-100), 0, new MsTiming((j==0)?0:285), new MsTiming(60));

			lang.nextStep();
			hideme.hide();

    		if(alicetobob) {
    			alicesubsetText[j]   .changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
    			alicebitvaluesText[i].changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
    		}
    		if(!alicetobob) {
				bobssubsetText[j]    .changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
	    		bobsbitvaluesText[i] .changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
    		}
			samebasesText[i]     .changeColor("color", Color.BLACK, new MsTiming(0), new MsTiming(0));
			
			Circle hide = last;
			Text hideName = lastName;
			last = active;
			lastName = activeName;
			last    .moveBy("translate", (alicetobob?500:-480), 0, new MsTiming(0), new MsTiming(345));
			lastName.moveBy("translate", (alicetobob?500:-480), 0, new MsTiming(0), new MsTiming(345));
			if(hide != null) {
				hide.hide();
				hideName.hide();
			}
    		j++;
		}
		lang.nextStep();
		if(last != null) {
	    	last.hide();
	    	lastName.hide();
		}
    }
	private Node[] getPolyLineFor(int x1, int y1, int x2, int y2){

    	Node[] vertices = {new Coordinates(x1, y1), new Coordinates(x2, y2)};

		return vertices;
	}
	

    public String getName() {
        return "BB84 Protokoll [DE]";
    }
	
    public String getDescription() {
	return DESCRIPTION;
    }
	
    public String getCodeExample() {
	return SOURCE_CODE;
    }
    
	

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		TextProperties h = (TextProperties)  props.getPropertiesByName("highlightColor");
        Qubit.qubit_prop = (CircleProperties)props.getPropertiesByName("qubit_+");
        Qubit.qubitXprop = (CircleProperties)props.getPropertiesByName("qubit_x");
        borderedcircle   = (CircleProperties)props.getPropertiesByName("bitMessage");
        qchannel_prop    = (RectProperties)  props.getPropertiesByName("quantumChannel");
        cchannel_prop    = (RectProperties)  props.getPropertiesByName("classicalChannel");
        filter_prop      = (RectProperties)  props.getPropertiesByName("filter");
        borderedsquare   = (SquareProperties)props.getPropertiesByName("baseMessage");
        borderedGreensquare = (SquareProperties)props.getPropertiesByName("baseYesMessage");
        borderedRedsquare   = (SquareProperties)props.getPropertiesByName("baseNoMessage");

        qcline_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, qchannel_prop.get(AnimationPropertiesKeys.COLOR_PROPERTY));
        ccline_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, cchannel_prop.get(AnimationPropertiesKeys.COLOR_PROPERTY));
        qchannel_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, qchannel_prop.get(AnimationPropertiesKeys.FILL_PROPERTY));
        cchannel_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, cchannel_prop.get(AnimationPropertiesKeys.FILL_PROPERTY));

        highlightColor = (Color) h.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        
		subsetpercentage = (double)primitives.get("propotionOfCheckBits");
        alicebitvalues = (int[])primitives.get("aliceBits");
		
        //this is now unnecessary because of the validating generator
        /*subsetpercentage = Math.abs(subsetpercentage%1);
        
        for(int i = 0; i < alicebitvalues.length; i++)
        	alicebitvalues[i] = (alicebitvalues[i]%2==0) ? 0 : 1;*/
		
	    alicebases        = new int[alicebitvalues.length];
	    alicesubset       = new int[alicebitvalues.length];
	    bobsbitvalues     = new int[alicebitvalues.length];
	    bobsbases         = new int[alicebitvalues.length];
	    bobssubset        = new int[alicebitvalues.length];
	    evesbitvalues     = new int[alicebitvalues.length];
	    evesbases         = new int[alicebitvalues.length];
	    
		for(int i = 0; i < alicebitvalues.length; i++) {
			alicebases[i] = Math.round((float) Math.random());
			bobsbases[i]  = Math.round((float) Math.random());
			evesbases[i]  = Math.round((float) Math.random());
		}
		

	    alicebitvaluesText    = new Text[alicebitvalues.length];
		alicebasesText        = new Text[alicebitvalues.length];
		alicesubsetText       = new Text[alicebitvalues.length];
		bobsbitvaluesText     = new Text[alicebitvalues.length];
		bobsbasesText         = new Text[alicebitvalues.length];
		bobssubsetText        = new Text[alicebitvalues.length];
		samebasesText         = new Text[alicebitvalues.length];
	    evesRecbitvaluesText  = new Text[alicebitvalues.length];
		evesRecbasesText      = new Text[alicebitvalues.length];
		evesSendbitvaluesText = new Text[alicebitvalues.length];
		evesSendbasesText     = new Text[alicebitvalues.length];
		
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		bb84animation();
		lang.finalizeGeneration();
		
		//important: set color back
        qchannel_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, qcline_prop.get(AnimationPropertiesKeys.COLOR_PROPERTY));
        cchannel_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, ccline_prop.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		return lang.toString();
	}

	@Override
    public String getAlgorithmName() {
        return "BB84";
    }

	@Override
    public String getAnimationAuthor() {
        return "Vincent Riesop und Nils Mäser";
    }

	@Override
	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

	@Override
    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		
		double subsp = (double)primitives.get("propotionOfCheckBits");
		if(subsp < 0.0 || subsp > 1.0)
			throw new IllegalArgumentException("Ungültiger Wert für 'propotionOfCheckBits': " + Double.toString(subsp) + "\n" +
					"'propotionOfCheckBits' muss eine Dezimalzahl zwischen 0.0 und 1.0 sein.\n" +
					"Alice und Bob schicken sich im Verlauf des Protokolls eine Untermenge " +
					"der Bits, bei denen sie die gleiche Basis gewählt haben, um mögliche " +
					"Lauschangriffe aufzudecken.\nMit dem Wert 'propotionOfCheckBits' lässt " +
					"sich der Anteil dieser Untermenge bestimmen. ");

		int[] aliceb = (int[])primitives.get("aliceBits");
        
        for(int i = 0; i < aliceb.length; i++) {
        	if(aliceb[i] != 0 && aliceb[i] != 1)
    			throw new IllegalArgumentException("Ungültiger Wert in 'aliceBits': " + Integer.toString(aliceb[i]) + "\n" +
    					"'aliceBits' ist eine Menge von Bitwerten, die nur die Werte 0 und 1 annehmen dürfen.");
        }
		return true;
	}

	@Override
	public void init() {
		
		lang = new AnimalScript("BB84 Protokoll [DE]", "Vincent Riesop und Nils Mäser", 800, 640);
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
		
	    text = new Text[31];
	    textlinec = 0;
	    nextParagraph = 1;
	    countsamebases = 0;


    	normtextprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
    			Font.PLAIN, 16));
    	smalltextprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
    			Font.PLAIN, 14));
    	headlineprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 
    			Font.BOLD, 24));
    	boldtextprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 
    			Font.BOLD, 16));
    	bigtextprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 
    			Font.PLAIN, 24));
    	whitetextprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
    			Font.PLAIN, 16));
    	whitetextprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    	whitetextprop.set(AnimationPropertiesKeys.DEPTH_PROPERTY,  13);
    	monotextprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
    			Font.PLAIN, 14));
    	smallertextprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
    			Font.PLAIN, 10));
    	
    	Qubit.whitetextprop = whitetextprop;
    	Qubit.normtextprop = normtextdepth13prop;
    	
    	

    	normtextdepth10prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
    			Font.PLAIN, 16));
    	normtextdepth10prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY,  10);
    	normtextdepth12prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
    			Font.PLAIN, 16));
    	normtextdepth12prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY,  12);
    	normtextdepth13prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
    			Font.PLAIN, 16));
    	normtextdepth13prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY,  13);

    	
    	checktextprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 
    			Font.PLAIN, 50));
    	checktextprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    	falsetextprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 
    			Font.PLAIN, 50));
    	falsetextprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    	
      	
    	head_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    	head_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY,  Boolean.TRUE);
    	head_prop.set(AnimationPropertiesKeys.FILL_PROPERTY,  Color.WHITE);
    	head_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY,  11);
    	
    	whiteoverlay.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    	whiteoverlay.set(AnimationPropertiesKeys.FILLED_PROPERTY,  Boolean.TRUE);
    	whiteoverlay.set(AnimationPropertiesKeys.FILL_PROPERTY,  Color.WHITE);
    	whiteoverlay.set(AnimationPropertiesKeys.DEPTH_PROPERTY,  12);

    	qcline_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY,  15);
    	ccline_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY,  15);
    	
    	arrow_prop.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
    	
	}

	private static class Qubit {
	
		public static TextProperties   normtextprop  = new TextProperties();
	    public static TextProperties   whitetextprop = new TextProperties();
	
		private Language lang;
		
	    public static CircleProperties qubitXprop    = new CircleProperties();
	    public static CircleProperties qubit_prop    = new CircleProperties();
	    
	    private Circle qubit;
	    Text qubit_value;
	    Text qubit_bitvalue;
	    
	    /**
	     * 
	     * @param lang
	     * @param x
	     * @param y
	     * @param bitvalue
	     * @param base
	     */
	    
	    Qubit(Language lang, int x, int y, int bitvalue, int base) {
	    	
	    	this.lang = lang;
	    	
			qubit = this.lang.newCircle(new Coordinates(x, y), 15, "qubit", null, (base == 0) ? qubitXprop : qubit_prop);
			
			int ofx = -3;
			int ofy = -9;
			String value = "\\\\";
			if(bitvalue == 0 && base == 0) {
				ofx = -1;
				ofy = -10;
				value = "|";
			}
			else if(bitvalue == 1 && base == 0) {
				ofx = -1;
				ofy = -10;
				value = "-";
			}
			else if(bitvalue == 0 && base == 1) {
				ofx = -2;
				value = "/";
			}
			qubit_value = this.lang.newText(new Offset(ofx, ofy, qubit, AnimalScript.DIRECTION_C), value, "qubit_value", null, whitetextprop);
			qubit_bitvalue = this.lang.newText(new Offset(-4, 20, qubit, AnimalScript.DIRECTION_C), String.valueOf(bitvalue), "qubit_bitvalue", null, normtextprop);
	
	    }
		
		
	    public void hide() {
	    	qubit.hide();
	    	qubit_bitvalue.hide();
	    	qubit_value.hide();
	    }
		
	
	    public void moveQubit(int dx, int dy, int delay, int ticks){
	
			qubit.moveBy("translate", dx, dy, new MsTiming(delay), new MsTiming(ticks));
			qubit_value.moveBy("translate", dx, dy, new MsTiming(delay), new MsTiming(ticks));
			qubit_bitvalue.moveBy("translate", dx, dy, new MsTiming(delay), new MsTiming(ticks));
	    }
	}

}
