package generators.maths.affine;

import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;


/**
 * @author Dr. Guido R&ouml;&szlig;ling <roessling@acm.org>
 * @version 1.0 2007-05-30
 *
 */
public class Translation_Inhalt {
	
    /**
     * The concrete language object used for creating output
     */
    private Language lang;
    
    //Hier sind alle Properties der Primitiven
    ArrayProperties arrayProps;
    SourceCodeProperties scProps;
    MatrixProperties matrixProps;
    TextProperties headerProps; 
    TextProperties normalTextProps;
    TextProperties rechenzeichenProps;
    TextProperties markierteRechenzeichenProps;
    TextProperties markierterNormalTextProps;
    MatrixProperties markiertMatrixProps;
    PolylineProperties pfeilProps;
    PolylineProperties achseProps;
    TriangleProperties dreieckProps;
    PolygonProperties polyProps;
    TextProperties achsenbeschriftung;
    PointProperties pointProps;
    
   
    /**
     * Default constructor
     * @param l the conrete language object used for creating output
     */
    public Translation_Inhalt(Language l) {
	// Store the language object
	lang = l;
	
	//initialisiere die properties dynamisch und setze sie
	arrayProps = new ArrayProperties();
	scProps = new SourceCodeProperties();
	matrixProps = new MatrixProperties();
	headerProps = new TextProperties();
	normalTextProps = new TextProperties();
	rechenzeichenProps = new TextProperties();
	markierterNormalTextProps = new TextProperties();
	markiertMatrixProps = new MatrixProperties();
	markierteRechenzeichenProps = new TextProperties();
	pfeilProps = new PolylineProperties();
	achseProps = new PolylineProperties();
	dreieckProps = new TriangleProperties();
	polyProps = new PolygonProperties();
	achsenbeschriftung = new TextProperties();
	pointProps = new PointProperties();
	
//	setzeProperties(arrayProps, scProps, matrixProps, headerProps, normalTextProps, rechenzeichenProps, markierterNormalTextProps, markiertMatrixProps, markierteRechenzeichenProps, pfeilProps, dreieckProps, polyProps);
	
	
	
	// This initializes the step mode. Each pair of subsequent steps has to
	// be divdided by a call of lang.nextStep();
	lang.setStepMode(true);
    }
	
    private static final String DESCRIPTION = "Team 11, das ist unsere Beschreibung: ....";
    
    private static final String SOURCE_CODE = "brauchen weir einen quellcode???";
	
    public void letzteFolie(int anzahlEcken) {
    	
    	lang.nextStep("Abschlussfolie");
    	
    	int koord_x = 20;
    	int koord_y = 40;
    	int zeilenabstand = 25;
    	int ergebnis = 18;
    	
    	lang.hideAllPrimitives();
    	
    	Text text_header = lang.newText(new Coordinates(koord_x, 13), "Affine Abbildungen: Translation", "trans", null, headerProps);
    	
    	
    	Text text_teil0 = lang.newText(new Coordinates(koord_x, koord_y),
    			"Die Anzahl der verwendeten Operationen berechnet sich wie folgt: ",
    			"text", null, normalTextProps);
    	
    	koord_y = koord_y + zeilenabstand + 6;
    	
    	Text text_teil1_1 = lang.newText(new Coordinates(koord_x +20, koord_y),
    			"(Anzahl der Rechenoperationen bei der Matrizenmultiplikation +",
    			"text", null, normalTextProps);
    	
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil1_2 = lang.newText(new Coordinates(koord_x +20, koord_y),
    			"Anzahl der Rechenoperationen bei der Vektoraddition) *",
    			"text", null, normalTextProps);
    	
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil1_3 = lang.newText(new Coordinates(koord_x +20, koord_y),
    			"Anzahl der Punkte des Polygons",
    			"text", null, normalTextProps);
    	
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil2_1 = lang.newText(new Coordinates(koord_x +20, koord_y),
    			"= (15 + 3) * " + anzahlEcken,
    			"text", null, normalTextProps);
    	
    	koord_y = koord_y + zeilenabstand;

    	ergebnis = ergebnis * anzahlEcken;
    	
    	Text text_teil3 = lang.newText(new Coordinates(koord_x, koord_y),
    			"Dies ergibt insgesamt " + ergebnis + " Rechenoperationen. ",
    			"text", null, normalTextProps);
    	
    	koord_y = koord_y + zeilenabstand;
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil4 = lang.newText(new Coordinates(koord_x, koord_y),
    			"Für weitere Affine Abbildungen siehe:",
    			"text", null, normalTextProps);
    	
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil4_1 = lang.newText(new Coordinates(koord_x +60, koord_y),
    			"- Affine Abbildungen: Scherung",
    			"text", null, normalTextProps);
    	
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil4_2 = lang.newText(new Coordinates(koord_x +60, koord_y),
    			"- Affine Abbildungen: Skalierung",
    			"text", null, normalTextProps);
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil4_3 = lang.newText(new Coordinates(koord_x +60, koord_y),
    			"- Affine Abbildungen: Rotation",
    			"text", null, normalTextProps);
    	
    }


    
    /**
     * Hier wird der Einstiegstext erzeugt, der allgemein beschreibt, um was es geht.
     * Unabhängig von den ausgewählten parametern muss dieser Teil immer ausgeführt werden am anfang
     */
    public void simulateAllgemein() {
    	
    	//dieser Block wird in jeder Methode lokal erneut deklariert.
    	int pixelbreite_text = 7;
    	int koord_x0 = 20;
    	int koord_y0 = 40;
    	int koord_x = 20;
    	int koord_y = 40;
    	int zeilenabstand = 25;
    	int einrueckabstand = 50;
    	
    	Text text_header = lang.newText(new Coordinates(koord_x, 13), "Affine Abbildungen", "header", null, headerProps);
    	lang.nextStep("Beschreibung");
    	
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil1_1 = lang.newText(new Coordinates(koord_x, koord_y),
    			"Die affine Abbildung, auch affine Transformation genannt,",
    			"text", null, normalTextProps);

		koord_y = koord_y + zeilenabstand;
    	
		Text text_teil1_2 = lang.newText(new Coordinates(koord_x, koord_y + 6),
    			"ist in der Mathematik eine Abbildung, bei der ",
    			"text", null, normalTextProps);	
		
		lang.nextStep();
		
		koord_y = koord_y + zeilenabstand +6;

		Text text_teilK_1 = lang.newText(new Coordinates(koord_x + einrueckabstand, koord_y),
    			" - Kollinearität: ",
    			"text", null, markierterNormalTextProps);
		
		Text text_teilK_2 = lang.newText(new Coordinates(koord_x + einrueckabstand + 160, koord_y),
    			"Geraden werden auf Geraden abgebildet.",
    			"text", null, normalTextProps);
		
		koord_y = koord_y + zeilenabstand;
		
    	lang.nextStep();
    	
    	text_teilK_1.hide();
    	
    	text_teilK_1 = lang.newText(text_teilK_1.getUpperLeft(),
    			" - Kollinearität: ",
    			"text", null, normalTextProps);
    	
		Text text_teilP_1 = lang.newText(new Coordinates(koord_x + einrueckabstand, koord_y),
    			" - Parallelität: ",
    			"text", null, markierterNormalTextProps);
		
		Text text_teilP_2 = lang.newText(new Coordinates(koord_x + einrueckabstand + 160, koord_y),
    			"Parallele Objekte bleiben parallel.",
    			"text", null, normalTextProps);
		
		koord_y = koord_y + zeilenabstand;
		
    	lang.nextStep();
    	
    	text_teilP_1.hide();
    	
		text_teilP_1 = lang.newText(text_teilP_1.getUpperLeft(),
    			" - Parallelität: ",
    			"text", null, normalTextProps);
    	
		Text text_teilT_1 = lang.newText(new Coordinates(koord_x + einrueckabstand, koord_y),
    			" - Teilverhältnisse: ",
    			"text", null, markierterNormalTextProps);
		
		Text text_teilT_2 = lang.newText(new Coordinates(koord_x + einrueckabstand + 160, koord_y),
    			"Verhältnisse von Längen, Oberflächen und Volumen bleiben erhalten.",
    			"text", null, normalTextProps);
		
		koord_y = koord_y + zeilenabstand;
		
    	lang.nextStep();
    	
    	text_teilT_1.hide();
    	
    	text_teilT_1 = lang.newText(text_teilT_1.getUpperLeft(),
    			" - Teilverhältnisse: ",
    			"text", null, normalTextProps);
    	
		Text text_teil1_3 = lang.newText(new Coordinates(koord_x, koord_y),
		"erhalten bleiben.",
		"text", null, normalTextProps);
	
    	lang.nextStep();
    	

    	Text text_teil6 = lang.newText(new Coordinates(koord_x, koord_y = koord_y + zeilenabstand),
    			"Affine Abbildungen setzen sich aus einer linearen Abbildung und einer Parallelverschiebung",
    			"text", null, normalTextProps);
    	Text text_teil7 = lang.newText(new Coordinates(koord_x, koord_y = koord_y + zeilenabstand),
    			"zusammen. Sie haben die folgende allgemeine Form:",
    			"text", null, normalTextProps);
    	
    	Text text_teil8_1 = lang.newText(new Coordinates(koord_x = koord_x + 2*einrueckabstand, koord_y + zeilenabstand),
    			"x' ", "text", null, normalTextProps);
    	Text text_teil8_2 = lang.newText(new Coordinates(koord_x = 5 + koord_x + pixelbreite_text*text_teil8_1.getText().length(), koord_y + zeilenabstand),
    			"= ", "text", null, normalTextProps);
    	Text text_teil8_3 = lang.newText(new Coordinates(koord_x = 5 + koord_x + pixelbreite_text*text_teil8_2.getText().length(), koord_y + zeilenabstand),
    			"A ", "text", null, normalTextProps);
    	Text text_teil8_4 = lang.newText(new Coordinates(koord_x = 5 + koord_x + pixelbreite_text*text_teil8_3.getText().length(), koord_y + zeilenabstand),
    			"* ", "text", null, normalTextProps);
    	Text text_teil8_5 = lang.newText(new Coordinates(koord_x = 5 + koord_x + pixelbreite_text*text_teil8_4.getText().length(), koord_y + zeilenabstand),
    			"x ", "text", null, normalTextProps);
    	Text text_teil8_6 = lang.newText(new Coordinates(koord_x = 5 + koord_x + pixelbreite_text*text_teil8_5.getText().length(), koord_y + zeilenabstand),
    			"+ ", "text", null, normalTextProps);
    	Text text_teil8_7 = lang.newText(new Coordinates(koord_x = 5 + koord_x + pixelbreite_text*text_teil8_6.getText().length(), koord_y = koord_y + zeilenabstand),
    			"t ", "text", null, normalTextProps);
    	koord_x = koord_x0;
    	
    	
    	lang.nextStep();
    	
    	
    	Text text_teil9_1 = lang.newText(new Coordinates(koord_x, koord_y + zeilenabstand),
    			"Dabei ist A die Abbildungsmatrix.", "text", null, normalTextProps);
    	text_teil8_3 = lang.newText(text_teil8_3.getUpperLeft(),
    			"A ", "text", null, markierterNormalTextProps);
    	
    	lang.nextStep();
    	
    	text_teil9_1.hide();
    	text_teil8_3 = lang.newText(text_teil8_3.getUpperLeft(),
    			"A ", "text", null, normalTextProps);
    	text_teil8_7 = lang.newText(text_teil8_7.getUpperLeft(),
    			"t ", "text", null, markierterNormalTextProps);
    	
    	Text text_teil9_2 = lang.newText(new Coordinates(koord_x, koord_y + zeilenabstand),
    			"Und t ist der Verschiebungsvektor.", "text", null, normalTextProps);
    	
    	lang.nextStep();
    	
    	text_teil8_7 = lang.newText(text_teil8_7.getUpperLeft(),
    			"t ", "text", null, normalTextProps);
    	text_teil9_2.hide();
    	
    	Text text_teil10 = lang.newText(new Coordinates(koord_x, koord_y = koord_y + zeilenabstand),
    			"Für den dreidimensionalen Raum sieht das Gerüst der affinen Abbildung folgendermaßen aus:",
    			"text", null, normalTextProps);
    	
    	lang.nextStep();
    	
    	
    	//Hier werden die matrizen und rechenzeichen eingefügt.
    	//Vielleiucht kann man die rechenzeichen ersetzen durch zeichnungen oder sowas.
    	String[][] beispielmatrix_1 = {{"x\'"},{"y\'"},{"z\'"}};
    	String[][] beispielmatrix_2 = {{"a00","a01","a02"},{"a10","a11","a12"},{"a20","a21","a22"}};
    	String[][] beispielmatrix_3 = {{"x"},{"y"},{"z"}};
    	String[][] beispielmatrix_4 = {{"x0"},{"y0"},{"z0"}};
    	
    	StringMatrix bspM_1 = lang.newStringMatrix(new Coordinates(koord_x+50, koord_y = koord_y + 2*zeilenabstand), beispielmatrix_1, "a", null, matrixProps);
    	StringMatrix bspM_2 = lang.newStringMatrix(new Coordinates(koord_x+150, koord_y), beispielmatrix_2, "b", null, matrixProps);
    	StringMatrix bspM_3 = lang.newStringMatrix(new Coordinates(koord_x+350, koord_y), beispielmatrix_3, "c", null, matrixProps);
    	StringMatrix bspM_4 = lang.newStringMatrix(new Coordinates(koord_x+500, koord_y), beispielmatrix_4, "d", null, matrixProps);
    	
    	int y_anpasskonstante = 40;
    	Text text_gleichheitszeichen = lang.newText(new Coordinates(koord_x+115, koord_y+y_anpasskonstante+10),
    			"=", "text", null, rechenzeichenProps);
		Text text_multiplikationszeichen = lang.newText(new Coordinates(koord_x+315, koord_y+y_anpasskonstante),
    			"*", "text", null, rechenzeichenProps);
		Text text_additionszeichen = lang.newText(new Coordinates(koord_x+445, koord_y+y_anpasskonstante),
    			"+", "text", null, rechenzeichenProps);
		
    	lang.nextStep("Translation");
    	
    	//Nun müssen alle Primitiven verschwinden
    	lang.hideAllPrimitives(); bspM_1.hide(); bspM_2.hide(); bspM_3.hide(); bspM_4.hide();

    }
    
   
    
    

    public void translation(int[][] urbilder, int[][] transVek) {
    	//dieser Block wird in jeder Methode lokal erneut deklariert.
    	
    	double[][] x_full = matrix_intToDouble(urbilder);
    	double[][] b = matrix_intToDouble(transVek);
    	
    	int pixelbreite_text = 7;
    	int koord_x = 20;
    	int koord_y = 40;
    	int koord_x0 = 20;
    	int zeilenabstand = 25;
    	int einrueckabstand = 50;
    	
    	
    	//ersten Punkt extrahieren
    	double[][] x = {{x_full[0][0]},{x_full[1][0]},{x_full[2][0]}};
    	
    	
    	
    	//Modifizierte Überschrift
    	Text text_header = lang.newText(new Coordinates(koord_x, 0), "Affine Abbildungen: Translation", "trans", null, headerProps);
    	    	Text text_teil1_1 = lang.newText(new Coordinates(koord_x, koord_y = koord_y + zeilenabstand),
    			"Bei der Translation, auch Verschiebung genannt, wird ein Objekt mittels eines Vektors",
    			"text", null, normalTextProps);
    	
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil1_2 = lang.newText(new Coordinates(koord_x, koord_y + 9),
    			"beliebig im Raum verschoben.",
    			"text", null, normalTextProps);	
    	
    	koord_y = koord_y + zeilenabstand +9;
    	
		lang.nextStep("Beginn der Rechnung");
		
		//Hier werden die matrizen und rechenzeichen eingefügt.
    	//Vielleiucht kann man die rechenzeichen ersetzen durch zeichnungen oder sowas.
    	String[][] beispielmatrix_1 = {{"x\'"},{"y\'"},{"z\'"}};
    	String[][] beispielmatrix_2 = {{"a00","a01","a02"},{"a10","a11","a12"},{"a20","a21","a22"}};
    	String[][] beispielmatrix_3 = {{"x"},{"y"},{"z"}};
    	String[][] beispielmatrix_4 = {{"x0"},{"y0"},{"z0"}};
    	
    	StringMatrix bspM_1 = lang.newStringMatrix(new Coordinates(koord_x+50, koord_y + 5*zeilenabstand), beispielmatrix_1, "a", null, matrixProps);
    	StringMatrix bspM_2 = lang.newStringMatrix(new Coordinates(koord_x+150, koord_y + 5*zeilenabstand), beispielmatrix_2, "b", null, matrixProps);
    	StringMatrix bspM_3 = lang.newStringMatrix(new Coordinates(koord_x+350, koord_y + 5*zeilenabstand), beispielmatrix_3, "c", null, matrixProps);
    	StringMatrix bspM_4 = lang.newStringMatrix(new Coordinates(koord_x+500, koord_y + 5*zeilenabstand), beispielmatrix_4, "d", null, matrixProps);
    	
    	//wir merken uns die Position der zu ersetzenden matrix
    	Coordinates pos_bspM_4 = new Coordinates(koord_x+500, koord_y + 5*zeilenabstand);
    	Coordinates pos_bspM_2 = new Coordinates(koord_x+150, koord_y + 5*zeilenabstand);
    	Coordinates pos_bspM_3 = new Coordinates(koord_x+350, koord_y + 5*zeilenabstand);
    	
    	int y_anpasskonstante = 40;
    	Text text_gleichheitszeichen = lang.newText(new Coordinates(koord_x+115, koord_y + 5*zeilenabstand+y_anpasskonstante+10),
    			"=", "text", null, rechenzeichenProps);
		Text text_multiplikationszeichen = lang.newText(new Coordinates(koord_x+315, koord_y + 5*zeilenabstand+y_anpasskonstante),
    			"*", "text", null, rechenzeichenProps);
		Text text_additionszeichen = lang.newText(new Coordinates(koord_x+445, koord_y + 5*zeilenabstand+y_anpasskonstante),
    			"+", "text", null, rechenzeichenProps);
		
		lang.nextStep();
		
		bspM_4.hide();
		bspM_4 = lang.newStringMatrix(pos_bspM_4, beispielmatrix_4, "c", null, markiertMatrixProps);

		Text text_teil3_1 = lang.newText(new Coordinates(koord_x, koord_y ),
    			"Dieser ",
    			"text", null, normalTextProps);
		Coordinates pos_text_teil3_1 = new Coordinates(koord_x, koord_y);
		
		Text text_teil3_3 = lang.newText(new Coordinates(koord_x +60, koord_y ),
    			"Verschiebungsvektor",
    			"text", null, markierterNormalTextProps);
		
		Text text_teil3_2 = lang.newText(new Coordinates(koord_x +225, koord_y ),
    			" setzt sich im dreidimensionalen Raum aus drei Parametern zusammen,",
    			"text", null, normalTextProps);
		
		koord_y = koord_y + zeilenabstand;
		
    	Text text_teil4_1 = lang.newText(new Coordinates(koord_x, koord_y ),
    			"die jeweils die Verschiebung anhand der x-, y- und der z-Achse angeben.",
    			"text", null, normalTextProps);
    	
    	lang.nextStep();
    	
    	 text_teil3_3.hide();
    	
    	 text_teil3_3 = lang.newText(new Coordinates(koord_x +60, koord_y - zeilenabstand),
     			"Verschiebungsvektor",
     			"text", null, normalTextProps);
 		
    	koord_y = koord_y + zeilenabstand;
    	
    	Text text_teil4_2 = lang.newText(new Coordinates(koord_x, koord_y),
    			"Zuerst werden die ", "text", null, normalTextProps);
       	Text text_teil4_3 = lang.newText(new Coordinates(koord_x + 150, koord_y),
    			"konkreten Werte ", "text", null, markierterNormalTextProps);
       	Text text_teil4_4 = lang.newText(new Coordinates(koord_x +286, koord_y),
    			"eingesetzt.", "text", null, normalTextProps);
    	
    	lang.nextStep();
    	
    	bspM_4.hide();
    	DoubleMatrix m_b = lang.newDoubleMatrix(pos_bspM_4 , b, "d", null, markiertMatrixProps);
    	
    	lang.nextStep();
    	
    	text_teil4_2.hide(); text_teil4_3.hide(); text_teil4_4.hide();
    	m_b.hide();
    	m_b = lang.newDoubleMatrix(pos_bspM_4 , b, "d", null, matrixProps);
    	
    	Text text_teil6_1 = lang.newText(new Coordinates(koord_x, koord_y),
    			"Nun werden die Werte für den",
    			"text", null, normalTextProps);
		Text text_teil6_2 = lang.newText(new Coordinates(koord_x = koord_x+235, koord_y),
    			" Ursprungsvektor",
    			"text", null, markierterNormalTextProps);
		Text text_teil6_3 = lang.newText(new Coordinates(koord_x+137, koord_y),
    			" eingesetzt.",
    			"text", null, normalTextProps);
		koord_x = koord_x0;
		
		bspM_3.hide();
		bspM_3 = lang.newStringMatrix(pos_bspM_3, beispielmatrix_3, "c", null, markiertMatrixProps);
		
		lang.nextStep();
		
		bspM_3.hide();
		DoubleMatrix m_urbild = lang.newDoubleMatrix(pos_bspM_3, x, "c", null, markiertMatrixProps);
		
		lang.nextStep();
		
		text_teil6_1.hide(); text_teil6_2.hide(); text_teil6_3.hide();
		bspM_3.hide();
		m_urbild.hide();
		m_urbild = lang.newDoubleMatrix(pos_bspM_3, x, "c", null, matrixProps);

		Text text_teil5_1 = lang.newText(new Coordinates(koord_x, koord_y),
    			"Die Abbildungsmatrix",
    			"text", null, markierterNormalTextProps);
		Text text_teil5_2 = lang.newText(new Coordinates(koord_x+173, koord_y),
    			" wird bei der Translation auf die Einheitsmatrix gesetzt.",
    			"text", null, normalTextProps);
		
		bspM_2.hide();
		bspM_2 = lang.newStringMatrix(pos_bspM_2, beispielmatrix_2, "b", null, markiertMatrixProps);
		
		lang.nextStep();
		
		bspM_2.hide();
		
		double[][] identitaet = {{1.0 , 0.0 , 0.0},{0.0 , 1.0 , 0.0},{0.0 , 0.0 , 1.0}};
		DoubleMatrix m_A = lang.newDoubleMatrix(pos_bspM_2, identitaet, "b", null, markiertMatrixProps);

		lang.nextStep();
		
		m_A.hide(); text_teil5_1.hide(); text_teil5_2.hide(); text_multiplikationszeichen.hide();
		m_A = lang.newDoubleMatrix(pos_bspM_2, identitaet, "b", null, matrixProps);

		Text text_teil9 = lang.newText(new Coordinates(koord_x, koord_y),
    			"Nun wird zuerst die ", "text", null, normalTextProps);
    	Text text_teil9_1 = lang.newText(new Coordinates(koord_x +160, koord_y),
    			"Matrix ", "text", null, markierterNormalTextProps);
    	Text text_teil9_2 = lang.newText(new Coordinates(koord_x +218, koord_y),
    			"mit dem ", "text", null, normalTextProps);
    	Text text_teil9_3 = lang.newText(new Coordinates(koord_x +288, koord_y),
    			"Vektor x ", "text", null, markierterNormalTextProps);
    	Text text_teil9_4 = lang.newText(new Coordinates(koord_x +360, koord_y),
    			"multipliziert.", "text", null, normalTextProps);

    	text_multiplikationszeichen = lang.newText(new Coordinates(koord_x+315, koord_y + 5*zeilenabstand +3),
    			"*", "text", null, markierteRechenzeichenProps);
	
		m_A.hide(); m_urbild.hide();
		m_A = lang.newDoubleMatrix(pos_bspM_2, identitaet, "b", null, markiertMatrixProps);
		m_urbild = lang.newDoubleMatrix(pos_bspM_3, x, "c", null, markiertMatrixProps);
		lang.nextStep(10);
		
		m_A.moveTo("N", "translate", new Coordinates(koord_x+150, koord_y + 2 *zeilenabstand +7), new MsTiming(10), new MsTiming(1000));
		text_multiplikationszeichen.moveTo("N", "translate", new Coordinates(koord_x+315, koord_y +4*zeilenabstand -3), new MsTiming(10), new MsTiming(1000));
		
		m_urbild.moveTo("N", "translate", new Coordinates(koord_x+350, koord_y + 2 *zeilenabstand +7), new MsTiming(10), new MsTiming(1000));
		
		lang.nextStep(10);
    	
    	text_multiplikationszeichen.hide(); m_A.hide(); m_urbild.hide();
		
    	m_urbild = lang.newDoubleMatrix(new Coordinates(koord_x+250, koord_y + 2 *zeilenabstand +7), x, "c", null, markiertMatrixProps);

		lang.nextStep();

		text_additionszeichen.hide(); m_b.hide(); text_teil9.hide(); text_teil9_1.hide(); text_teil9_2.hide(); text_teil9_3.hide(); text_teil9_4.hide();

    	m_b = lang.newDoubleMatrix(pos_bspM_4 , b, "d", null, markiertMatrixProps);
		
		text_additionszeichen = lang.newText(new Coordinates(koord_x+445, koord_y + 5*zeilenabstand +3),
    			"+", "text", null, markierteRechenzeichenProps);
	
		Text text_teil10 = lang.newText(new Coordinates(koord_x, koord_y - 13),
        		"Danach erfolgt eine Addition mit dem ", "text", null, normalTextProps);
    	Text text_teil10_1 = lang.newText(new Coordinates(koord_x +303, koord_y ),
        		"Verschiebungsvektor", "text", null, markierterNormalTextProps);
    	Text text_teil10_2 = lang.newText(new Coordinates(koord_x +465, koord_y ),
        		".", "text", null, normalTextProps);
    	
    	lang.nextStep(10);
		
    	m_urbild.moveTo("E", "translate", new Coordinates(koord_x+350,  koord_y + 2 *zeilenabstand +7), new MsTiming(10), new MsTiming(1000));
		
    	m_b.moveTo("N", "translate", new Coordinates(koord_x+500, koord_y + 2 *zeilenabstand +7), new MsTiming(10), new MsTiming(1000));
		text_additionszeichen.moveTo("N", "translate", new Coordinates(koord_x+445, koord_y +4*zeilenabstand -3), new MsTiming(10), new MsTiming(1000));
		
		lang.nextStep(10);
		
     	text_additionszeichen.hide(); m_b.hide(); m_urbild.hide();
     	
	
     	double[][] ergebnis = matrixAddition(x, b);
		DoubleMatrix m_erg = lang.newDoubleMatrix(new Coordinates(koord_x+420, koord_y + 2 *zeilenabstand +7), ergebnis, "c", null, markiertMatrixProps);
		
		lang.nextStep();

		text_teil10.hide(); text_teil10_1.hide(); text_teil10_2.hide();
 
     	Text text_teil11 = lang.newText(new Coordinates(koord_x, koord_y ),
    			"Man erhält den gewünschten Vektor x´.",
    			"text", null, normalTextProps);

     	m_erg.moveTo("E", "translate", new Coordinates(koord_x+420, koord_y + 2 *zeilenabstand +20), new MsTiming(10), new MsTiming(1000));
     	
     	m_erg.moveTo("E", "translate", new Coordinates(180, koord_y + 2 *zeilenabstand +11), new MsTiming(1000), new MsTiming(1000));
    	   
		lang.nextStep("Berechnung restlicher Punkte");
		
		
		
    	text_gleichheitszeichen.hide();
    	text_teil11.hide();
    	text_teil1_1.hide();
    	text_teil1_2.hide();
    	text_teil3_1.hide();
    	text_teil3_2.hide();
    	text_teil3_3.hide();
    	text_teil4_1.hide();
    	m_erg.hide(); bspM_1.hide();

    }
   
    
    public void translation_restlichePunkte_schnell(int[][] urbild_punkte, int[][] versch) {
    	
    	double[][] transVek = matrix_intToDouble(versch);
    	
    	int x = 100;
    	Text einleitung = lang.newText(new Coordinates(25, 40),
    			"Nun erfolgt die Berechnung aller weiteren Punkte im Schnelldurchgang.",
    			"scher", null, normalTextProps);
    	Text counter_visuell = lang.newText(new Coordinates(25, 55),
    			"",
    			"scher", null, normalTextProps);
    	
    	//Alle berechneten Punkte hier merken
    	double[][] bildpunkte = new double[urbild_punkte.length][urbild_punkte[0].length];
	
    	//restliche Punkte in der Schleife bearbeiten
    	for (int i = 0; urbild_punkte[0].length > i; i++) {
    		counter_visuell.hide();
    		int[][] aktueller_punkt = {{urbild_punkte[0][i]},{urbild_punkte[1][i]},{urbild_punkte[2][i]}};
    		counter_visuell = lang.newText(new Coordinates(25, 60),
        			"Punkt "+(i+1)+":",
        			"scher", null, normalTextProps);
    		double[][] temp_punkt = translation_schnell(aktueller_punkt, transVek);
    		bildpunkte[0][i] = temp_punkt[0][0];
    		bildpunkte[1][i] = temp_punkt[1][0];
    		bildpunkte[2][i] = temp_punkt[2][0];
    		
    	}
    	counter_visuell.hide();
    	
    	einleitung.hide();
    	
    	
    	simulateKoordSystem2D(urbild_punkte, bildpunkte);
    }
    
    public double[][] translation_schnell(int[][] urbild_punkte, double[][] transVek) {
    	//dieser Block wird in jeder Methode lokal erneut deklariert.
    	int koord_x = 20;
    	int koord_y = 80;
    	int zeilenabstand = 25;
    	int einrueckabstand = 50;
    	
    	//ersten Punkt extrahieren
    	double[][] x = {{urbild_punkte[0][0]},{urbild_punkte[1][0]},{urbild_punkte[2][0]}};
    	double[][] einheitsmatrix = {{1,0,0},{0,1,0},{0,0,1}};
    	String[][] urbildStrich = {{"x\'"},{"y\'"},{"z\'"}};
    	
    	StringMatrix m_bild = lang.newStringMatrix(new Coordinates(koord_x+50, koord_y + 3*zeilenabstand), urbildStrich, "a", null, matrixProps);
		DoubleMatrix m_A = lang.newDoubleMatrix(new Coordinates(koord_x+150, koord_y + 3*zeilenabstand), einheitsmatrix, "b", null, matrixProps);
		DoubleMatrix m_urbild = lang.newDoubleMatrix(new Coordinates(koord_x+450, koord_y + 3*zeilenabstand), x, "c", null, matrixProps);
		DoubleMatrix m_b = lang.newDoubleMatrix(new Coordinates(koord_x+600, koord_y + 3*zeilenabstand), transVek, "d", null, matrixProps);

		int y_anpasskonstante = 40;
    	Text text_gleichheitszeichen = lang.newText(new Coordinates(koord_x+115, koord_y+y_anpasskonstante+10+ 3*zeilenabstand),
    			"=", "text", null, rechenzeichenProps);
		Text text_multiplikationszeichen = lang.newText(new Coordinates(koord_x+415, koord_y+y_anpasskonstante+ 3*zeilenabstand),
    			"*", "text", null, rechenzeichenProps);
		Text text_additionszeichen = lang.newText(new Coordinates(koord_x+545, koord_y+y_anpasskonstante+ 3*zeilenabstand),
    			"+", "text", null, rechenzeichenProps);
		
		lang.nextStep();
		
		m_A.hide(); m_urbild.hide(); m_b.hide();
		text_multiplikationszeichen.hide(); text_additionszeichen.hide();
		

		double[][] resultat = new double[2][0];
		resultat = matrixRunden(matrixAddition(matrixMultiplikation(einheitsmatrix, x), transVek));
		

		
		DoubleMatrix resultat_visuell = lang.newDoubleMatrix(new Coordinates(koord_x+150, koord_y + 3*zeilenabstand), resultat, "b", null, matrixProps);	
		
		lang.nextStep();
		
		resultat_visuell.hide(); text_gleichheitszeichen.hide(); m_bild.hide();
		
		return resultat;
    	
    }
    
    public void setzePropertiesUser (	MatrixProperties matrix_normal, MatrixProperties matrix_markiert, PolygonProperties polygon,
			TextProperties text_header, TextProperties text_normal, TextProperties text_markiert,
			TextProperties textrechenzeichen_normal, TextProperties rechenzeichen_markiert,
			PolylineProperties pfeil, PolylineProperties achse) {


		matrixProps = matrix_normal;
		markiertMatrixProps = matrix_markiert;
		polyProps = polygon;
		headerProps = text_header;
		normalTextProps = text_normal;
		markierterNormalTextProps = text_markiert;
		rechenzeichenProps = textrechenzeichen_normal;
		markierteRechenzeichenProps = rechenzeichen_markiert;
		pfeilProps = pfeil;
		achseProps = achse;



    }
    
	public void simulateKoordSystem2D(int[][] urbildpunkte, double[][] bildpunkte) {
	    	
	    	//3D in 2D umwandeln
	    	double urbildpunkte_2D[][] = new double[2][urbildpunkte[0].length];
	    	double bildpunkte_2D[][] = new double[2][bildpunkte[0].length];
	    	
	    	//Hier werden die extremwerte für die Achsenskalierung gespeichert
	    	double x_min = 0;
	    	double x_max = 0;
	    	double y_min = 0;
	    	double y_max = 0;
	    	
	    	
	    	//Schleife 1: Diese Schleife erzeugt einen 2D-Array und findet die Extremwerte.
	    	for (int i=0 ; i < 2 ; i++) {
	    		for (int j=0; j < bildpunkte[0].length ; j++) {
	    			
	    			//2D erzeugen
	    			urbildpunkte_2D[i][j] = urbildpunkte[i][j];
	    			bildpunkte_2D[i][j] = bildpunkte[i][j];
	    			
	    			//Extremwerte setzen
	    			if (i == 0) {
	    				if (x_min > urbildpunkte_2D[i][j]) {x_min = urbildpunkte_2D[i][j];}
	    				if (x_max < urbildpunkte_2D[i][j]) {x_max = urbildpunkte_2D[i][j];}
	    				
	    				if (x_min > bildpunkte_2D[i][j]) {x_min = bildpunkte_2D[i][j];}
	    				if (x_max < bildpunkte_2D[i][j]) {x_max = bildpunkte_2D[i][j];}
	    			}
	    			else {
	    				if (y_min > urbildpunkte_2D[i][j]) {y_min = urbildpunkte_2D[i][j];}
	    				if (y_max < urbildpunkte_2D[i][j]) {y_max = urbildpunkte_2D[i][j];}
	    				
	    				if (y_min > bildpunkte_2D[i][j]) {y_min = bildpunkte_2D[i][j];}
	    				if (y_max < bildpunkte_2D[i][j]) {y_max = bildpunkte_2D[i][j];}
	    			}
	    		}	
	    	}//Schleife 1 ende
	    	
	 
	    	
	    	//Pixel-Werte setzen (das ist quasi ein Quadrat, in das die Achse reinpassen soll)
	    	int pixel_start_x = 40;
	    	int pixel_start_y = 100;
	    	int pixel_seitenlaenge = 400;
	    	int pixel_end_x = pixel_start_x + pixel_seitenlaenge;
	    	int pixel_end_y = pixel_start_y + pixel_seitenlaenge;
	    	int unterteilungen = 5;
	    	
	    	
	    	Text text_beschr = lang.newText(new Coordinates(25, 40),
	    			"Nun erfolgt die Simulation der Berechnung.",
	    			"scher", null, normalTextProps);
	    	
	    	Polygon poly_urbild = zeichneUrbilder (	urbildpunkte_2D, pixel_start_x, pixel_start_y, pixel_seitenlaenge,
		 			x_min, x_max, y_min, y_max);
	    	
	    	//Nur die vier Achsen zeichnen
	    	zeichneKoordinatensystem(
	    			x_min,  x_max,  y_min,  y_max, pixel_seitenlaenge,
	    			pixel_start_x,  pixel_start_y, unterteilungen	);
	    	
	    	//TODO
	    	
//	    	poly1.moveBy("translateNodes 1 2", 50, 50, new MsTiming(1000), new MsTiming(1000));
	    	
	    	lang.nextStep();
	    	
	    	//verschiebung vornehmen
	    	try {
	    		
	    		
	    		double distanz_wertebereich_x = Math.abs(x_max - x_min);
	    		double wertToPixel_x = (double)pixel_seitenlaenge / (double)distanz_wertebereich_x;
	    		
	    		double distanz_wertebereich_y = Math.abs(y_max - y_min);
	    		double wertToPixel_y = (double)pixel_seitenlaenge / (double)distanz_wertebereich_y;
	    		
	    		
	    		
	    		for(int i=0 ; i < urbildpunkte_2D[0].length ; i++) {
	    			int distanz_x = (int)Math.round(bildpunkte_2D[0][i] - urbildpunkte_2D[0][i]);
	    			int distanz_y = (int)Math.round(bildpunkte_2D[1][i] - urbildpunkte_2D[1][i]);
	    			
	    			poly_urbild.moveBy("translateNodes "+(i+1), (int)Math.round(wertToPixel_x*distanz_x), (int)Math.round(-distanz_y*wertToPixel_y), new MsTiming(1000), new MsTiming(1000));
	    		}
	    		
	    	} catch (Exception e) {e.printStackTrace();}
	    	
	    	
	    		
	}
	
	
	/**
	 * Liefert die Position in Pixel. Achtung: die 0 musss im Wertebereich enthalten sein.
	 * @param pixelbreite
	 * @param min
	 * @param max
	 * @param toInsert
	 * @return
	 */
	public int findePositionInPixel(int pixelbreite, double min, double max, double toInsert) {
		
		double distanz_wertToMin = Math.abs(min - toInsert);
		double distanz_wertebereich = Math.abs(max - min);
		double wertToPixel = (double)pixelbreite / (double)distanz_wertebereich;
		double position = distanz_wertToMin * wertToPixel;
		return (int) Math.round(position);
		
	}
	
	
	public Polygon zeichneUrbilder (
			double[][] urbildpunkte_2D, int pixel_start_x, int pixel_start_y, int pixel_seitenlaenge,
			double x_min, double x_max, double y_min, double y_max  ) {
	
		
		Node[] punkte = new Node[urbildpunkte_2D[0].length];
		
		for (int i=0 ; i<urbildpunkte_2D[0].length ; i++) {
			double grab_x = urbildpunkte_2D[0][i];
			int pix_x = findePositionInPixel(pixel_seitenlaenge, x_min, x_max, grab_x);
			pix_x = pix_x + pixel_start_x;
			
			double grab_y = urbildpunkte_2D[1][i];
			int pix_y = findePositionInPixel(pixel_seitenlaenge, y_min, y_max, grab_y);
			pix_y = (pixel_start_y+pixel_seitenlaenge)-pix_y;
			punkte[i] = new Coordinates(pix_x, pix_y);
			
			Node n1 = new Coordinates(pix_x,pix_y);
			
//			Point p1 = lang.newPoint(n1, "n1", null, pointProps);
			
		}
		
		//zeichne das polygon
		try {
			Polygon poly1 =    lang.newPolygon(punkte, "a", null, polyProps);
			return poly1;
			
		} catch(Exception e) {e.printStackTrace(); return null;}
		
		
		
       
    	
			
	}

	/**
	 * auslagerung aus der main
	 * @param pixelbreite
	 * @param min_x
	 * @param max_x
	 * @param min_y
	 * @param max_y
	 * @param bestimmtesKoord
	 * @param unterteilungen
	 */
	public void beschrifteKoordSystem(
			int pixelbreite, double min_x, double max_x, double min_y, double max_y,
			int[][] bestimmtesKoord, int unterteilungen, int x_start, int y_start) {
		
		
		//achse nach oben
		if (max_y != 0) {
			double schrittWert = max_y/unterteilungen;
			int achsenlaenge_oben = bestimmtesKoord[0][1] - bestimmtesKoord[1][1];
			int pixel_x = bestimmtesKoord[0][0];
			for(int i=1 ; i!=unterteilungen ; i++) {
				int pixel_y = findePositionInPixel(achsenlaenge_oben,  0, max_y, i*schrittWert);
				
				Node[] strichCoordsY = {
						new Coordinates(pixel_x+2, bestimmtesKoord[0][1] - pixel_y),
						new Coordinates(pixel_x-2, bestimmtesKoord[0][1] - pixel_y)	};
				Polyline strichAufY = lang.newPolyline(strichCoordsY, "strich", null);
				
				Text b2 = lang.newText(new Coordinates(pixel_x - 20, (bestimmtesKoord[0][1] - pixel_y) -8), ""+Math.round(schrittWert*i),
				"text", null, achsenbeschriftung);
				
			}//ende oben	
		}
		
		
		if (min_x != 0) {
			//Achse links
			double schrittWert2 = min_x/unterteilungen;
			int achsenlaenge_links = bestimmtesKoord[0][0] - bestimmtesKoord[2][0];
			int pixel_y2 = bestimmtesKoord[0][1];
			for(int i=1 ; i!=unterteilungen ; i++) {
				int pixel_x2 = findePositionInPixel(achsenlaenge_links,  0, min_x, i*schrittWert2);
				
				Node[] strichCoordsY = {
						new Coordinates(bestimmtesKoord[0][0] - pixel_x2, pixel_y2+2),
						new Coordinates(bestimmtesKoord[0][0] - pixel_x2, pixel_y2-2)	};
				Polyline strichAufY = lang.newPolyline(strichCoordsY, "strich", null);
				
				Text b2 = lang.newText(new Coordinates((bestimmtesKoord[0][0] - pixel_x2) -8, pixel_y2 + 15), ""+Math.round(schrittWert2*i),
				"text", null, achsenbeschriftung);
				
			}//ende links
		}
		
		if (min_y != 0) {
			//achse nach unten
			double schrittWert3 = min_y/unterteilungen;
			int achsenlaenge_unten = bestimmtesKoord[3][1] - bestimmtesKoord[0][1];
			int pixel_x3 = bestimmtesKoord[0][0];
			for(int i=1 ; i!=unterteilungen ; i++) {
				int pixel_y3 = findePositionInPixel(achsenlaenge_unten,  0, min_y, i*schrittWert3);
				
				Node[] strichCoordsY = {
						new Coordinates(pixel_x3+2, bestimmtesKoord[0][1] + pixel_y3),
						new Coordinates(pixel_x3-2, bestimmtesKoord[0][1] + pixel_y3)	};
				Polyline strichAufY = lang.newPolyline(strichCoordsY, "strich", null);
				
				Text b2 = lang.newText(new Coordinates(pixel_x3 + 10, (bestimmtesKoord[0][1] + pixel_y3) -8), ""+Math.round(schrittWert3*i),
				"text", null, achsenbeschriftung);
				
			}//ende unten
		}
		
		if (max_x != 0) {
			//Achse rechts
			double schrittWert4 = max_x/unterteilungen;
			int achsenlaenge_rechts = bestimmtesKoord[4][0] - bestimmtesKoord[0][0];
			int pixel_y4 = bestimmtesKoord[0][1];
			for(int i=1 ; i!=unterteilungen ; i++) {
				int pixel_x4 = findePositionInPixel(achsenlaenge_rechts,  0, max_x, i*schrittWert4);
				
				Node[] strichCoordsY = {
						new Coordinates(bestimmtesKoord[0][0] + pixel_x4, pixel_y4+2),
						new Coordinates(bestimmtesKoord[0][0] + pixel_x4, pixel_y4-2)	};
				Polyline strichAufY = lang.newPolyline(strichCoordsY, "strich", null);
				
				Text b2 = lang.newText(new Coordinates((bestimmtesKoord[0][0] + pixel_x4) -8, pixel_y4 - 15), ""+Math.round(schrittWert4*i),
				"text", null, achsenbeschriftung);
				
			}//ende rechts
		}
		
		
			
	}
	
	/**
	 * Diese methode soll lediglich die Code-Masse aus der Hauptmethode fernhalten.
	 * @param x_min
	 * @param x_max
	 * @param y_min
	 * @param y_max
	 * @param pixel_seitenlaenge
	 * @param pixel_start_x
	 * @param pixel_start_y
	 */
	public void zeichneKoordinatensystem(
			double x_min, double x_max, double y_min, double y_max,
			int	pixel_seitenlaenge, int pixel_start_x, int pixel_start_y, int unterteilungen) {
		//koordinaten festlegen und Achsen zeichnen
    	int [][] koordSyst = bestimmteKoordinatensystem (
    				x_min, x_max, y_min, y_max,
    				pixel_seitenlaenge, pixel_start_x, pixel_start_y	);
    	
    	Node[] achseOben = {
    			new Coordinates(koordSyst[0][0]	, koordSyst[0][1])	,
    			new Coordinates(koordSyst[1][0]	, koordSyst[1][1])	};
    	Node[] achseLinks = {
    			new Coordinates(koordSyst[0][0]	, koordSyst[0][1])	,
    			new Coordinates(koordSyst[2][0]	, koordSyst[2][1])	};
    	Node[] achseUnten = {
    			new Coordinates(koordSyst[0][0]	, koordSyst[0][1])	,
    			new Coordinates(koordSyst[3][0]	, koordSyst[3][1])	};
    	Node[] achseRechts = {
    			new Coordinates(koordSyst[0][0]	, koordSyst[0][1])	,
    			new Coordinates(koordSyst[4][0]	, koordSyst[4][1])	};
    	
    	Polyline AchseOben = lang.newPolyline(achseOben, "achseOben", null, pfeilProps);
    	Polyline AchseLinks = lang.newPolyline(achseLinks, "achseLinks", null, pfeilProps);
    	Polyline AchseUnten = lang.newPolyline(achseUnten, "achseUnten", null, pfeilProps);
    	Polyline AchseRechts = lang.newPolyline(achseRechts, "achseRechts", null, pfeilProps);
    	
    	
    	
    	
    	beschrifteKoordSystem(
    			pixel_seitenlaenge, x_min, x_max, y_min, y_max,
    			koordSyst, unterteilungen, pixel_start_x, pixel_start_y);
    	
	}
	
	/**
	 * Findet den Mittelpunkt zwischen zwei extremwerten in pixel. Die 0 muss innerhalb min und max liegen.
	 * @param pixelbreite
	 * @param min
	 * @param max
	 * @return
	 */
	public int findeMitteInPixel(int pixelbreite, double min, double max) {
		
		double distanz_wertebereich = Math.abs(max - min);
		double wertToPixel = (double)pixelbreite / (double)distanz_wertebereich;
		double position = Math.abs(min) * wertToPixel;
		return (int) Math.round(position);
		
		
	}
	/**
	 * Liefert alle wichtigen Pixel zum zeichnen des Koordinatensystems.
	 * Notation: int[0=Mitte, 1=Oben, 2=Links, 3=unten, 4=rechts][0=x / 1=y]
	 * @param x_min
	 * @param x_max
	 * @param y_min
	 * @param y_max
	 * @param pixelbreite
	 * @param quadratBeginn_x
	 * @param quadratBeginn_y
	 * @return
	 */
	public int[][] bestimmteKoordinatensystem (
			double x_min, double x_max, double y_min, double y_max,
			int pixelbreite, int quadratBeginn_x, int quadratBeginn_y	) {
		
		int[][] result = new int[5][2];
		
		//Mitte Finden
		double mitte_x = findeMitteInPixel(pixelbreite, x_min, x_max);
		result[0][0] = (int)mitte_x + quadratBeginn_x;
		double mitte_y = findeMitteInPixel(pixelbreite, y_min, y_max);
		result[0][1] =  (quadratBeginn_y+pixelbreite) - (int)mitte_y;
		//Oben finden
		int oben_x = result[0][0];
		result[1][0] =  oben_x;
		int oben_y = quadratBeginn_y;
		result[1][1] =  oben_y;
		
		//links finden
		int links_x = quadratBeginn_x;
		result[2][0] =  links_x;
		int links_y = result[0][1];
		result[2][1] =  links_y;
		
		//unten finden
		int unten_x = oben_x;
		result[3][0] = unten_x ;
		int unten_y = quadratBeginn_y + pixelbreite;
		result[3][1] =  unten_y;
		
		//unten finden
		int rechts_x = quadratBeginn_x + pixelbreite;
		result[4][0] = rechts_x ;
		int rechts_y = links_y;
		result[4][1] =  rechts_y;
		
		return result;
	}
	
    /**
     * Hier werden zentral alle Eigenschaften der Primitiven eingestellt.
     * Beliebig erweiterbar
     * @param arrayProps
     * @param scProps
     * @param matrixProps
     */
    public void setzeProperties () {
 
    	
    	//matrix
//    	matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
//    	matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
//    	matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
//      	matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
//      	matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
//      	matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);
      	
//      	markiertMatrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
//      	markiertMatrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
//      	markiertMatrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED);
//      	markiertMatrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
      	
//      sorgt dafür, dass die String matrix keine elemtne mehr anzeigt -> NIE BENUTZEN!
//    	matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        
      	//polyline
//    	pfeilProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
      	
      //polygon
//    polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
//    polyProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
//    polyProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      
    	//Text
    	 headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    		        Font.SANS_SERIF, Font.BOLD, 24));
    	 normalTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
 		        Font.SANS_SERIF, Font.PLAIN, 16));
    	 rechenzeichenProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    			Font.SANS_SERIF, Font.BOLD, 30));
    	 markierteRechenzeichenProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    			Font.SANS_SERIF, Font.BOLD, 30));
//    	 markierteRechenzeichenProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    	 markierterNormalTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    			Font.SANS_SERIF, Font.PLAIN, 16));
//    	 markierterNormalTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    	 
    	 achsenbeschriftung.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
  		        Font.SANS_SERIF, Font.PLAIN, 10));
    	 
    	
    }
    /**
     * Quelle: http://www.java-programmieren.com/matrix-multiplikation-java.php
     * verifiziert, es funktioniert.
     * @param A
     * @param b
     * @return
     */
    public double[][] matrixMultiplikation (double[][] A, double [][] b) {
    	
    	double[][] ergebnismatrix = null;

		if (A[0].length == b.length) {
			int zeilenm1 = A.length;
			int spaltenm1 = A[0].length;
			int spalenm2 = b[0].length;

			ergebnismatrix = new double[zeilenm1][spalenm2];

			for (int i = 0; i < zeilenm1; i++) {
				for (int j = 0; j < spalenm2; j++) {
					ergebnismatrix[i][j] = 0;
					for (int k = 0; k < spaltenm1; k++) {
					  ergebnismatrix[i][j] += A[i][k] * b[k][j];
					}
				}
			}
		} else {
			int zeilen = A.length;
			int spalten = A[0].length;

			ergebnismatrix = new double[zeilen][spalten];
			for (int i = 0; i < A.length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					ergebnismatrix[i][j] = 0;
				}
			}
		}
		return ergebnismatrix;
    	
    }
    
    /**
     * Rotiere um die Z-Achse um alpha Grad gegen den Uhrzeigersinn.
     * Funktionalität überprüft.
     * @param alpha diesen parameter darf der User einstellen
     * @return die rotationsmatrix A
     */
    public double[][] createRotationMatrix_Z(double alpha) {
    	//Vermeidung von Fehlern
    	
    	double[][] A = {{0,0,0},{0,0,0},{0,0,1}};
    	
    	if (A.length == A[0].length) {
    		
    		double[][] ergebnis = A.clone();
    		
    		ergebnis[0][0] = Math.cos(alpha);
    		ergebnis[0][1] = -Math.sin(alpha);
    		ergebnis[1][0] = Math.sin(alpha);
    		ergebnis[1][1] = Math.cos(alpha);
    		
    		return ergebnis;
    		
    	}
    	else return A;
    }
    
    /**
     * Rotiere um die Y-Achse um alpha Grad gegen den Uhrzeigersinn.
     * Funktionalität geprüft
     * @param alpha diesen parameter darf der User einstellen
     * @return die rotationsmatrix A
     */
    public double[][] createRotationMatrix_Y(double alpha) {
    	//Vermeidung von Fehlern
    	
    	double[][] A = {{0,0,0},{0,1,0},{0,0,0}};
    	
    	if (A.length == A[0].length) {
    		
    		double[][] ergebnis = A.clone();
    		
    		ergebnis[0][0] = Math.cos(alpha);
    		ergebnis[0][2] = Math.sin(alpha);
    		ergebnis[2][0] = -Math.sin(alpha);
    		ergebnis[2][2] = Math.cos(alpha);
    		
    		return ergebnis;
    		
    	}
    	else return A;
    }
    
    /**
     * Rotiere um die X-Achse um alpha Grad gegen den Uhrzeigersinn.
     * Funktionalität geprüft.
     * @param alpha diesen parameter darf der User einstellen
     * @return die rotationsmatrix A
     */
    
    public double[][] createRotationMatrix_X(double alpha) {
    	//Vermeidung von Fehlern
    	
    	double[][] A = {{1,0,0},{0,0,0},{0,0,0}};
    	
    	if (A.length == A[0].length) {
    		
    		double[][] ergebnis = A.clone();
    		
    		ergebnis[1][1] = Math.cos(alpha);
    		ergebnis[1][2] = -Math.sin(alpha);
    		ergebnis[2][1] = Math.sin(alpha);
    		ergebnis[2][2] = Math.cos(alpha);
    		
    		return ergebnis;
    		
    	}
    	else return A;
    }
    
    /**
     * Quelle: http://www.javawithus.com/programs/matrix-addition-and-subtraction
     * @param a
     * @param b
     * @return gibt die unveränderte Matrix A zurück, wenn rechnugn nicht Möglich
     */
    public double[][] matrixAddition(double[][] a, double[][] b) {
        int rows = a.length;
        int columns = a[0].length;
        
        //vermeidung von Exceptions
        if ((a.length == b.length) && (a[0].length == b[0].length)) {
	        double[][] result = new double[rows][columns];
	        for (int i = 0; i < rows; i++) {
	            for (int j = 0; j < columns; j++) {
	                result[i][j] = a[i][j] + b[i][j];
	            }
	        }
	        return result;
        }
        else return a;
    }
    
    /**
     * Jeder Eintrag der Matrix wird gerundet.
     * @param A
     * @return
     */
    public double[][] matrixRunden(double[][] A) {
    	double[][] rundeMatrix = A.clone();
    	for(int i = 0; i<A.length; i++) {
    		for(int j = 0; j<A[0].length; j++) {
    			rundeMatrix[i][j] = Math.round(A[i][j]*10000)/10000.0;
    		}
    	}
    	return rundeMatrix;
    }
    
    /**
     * Schreibt an die entsprechenden Stellen eine 1, damit die Regeln der Scherung nicht verletzt werden.
     * @param a
     * @return
     */
    public double[][] matrixPrepareScherung(double[][] a) {
    	double[][] m_prepared = a.clone();
    	
    	m_prepared[0][0] = 1.0; m_prepared[1][1] = 1.0; m_prepared[2][2] = 1.0;
    	
    	return m_prepared;
    }
    
    
    /**
     * Schreibt an die entsprechenden Stellen eine 1, damit die Regeln der Skalierung nicht verletzt werden.
     * @param a
     * @return
     */
    public double[][] matrixPrepareSkalierung(double[][] a) {
    	double[][] m_prepared = a.clone();
    	
    	m_prepared[0][1] = 1.0; m_prepared[0][2] = 1.0;
    	m_prepared[1][0] = 1.0; m_prepared[1][2] = 1.0;
    	m_prepared[2][0] = 1.0; m_prepared[2][1] = 1.0;
    	
    	
    	return m_prepared;
    }
    
    public double[][] matrix_intToDouble(int[][] schrott) {
    	double[][] output = new double[schrott.length][schrott[0].length];
    	for(int i=0 ; i< schrott.length ; i++){
    		for(int j=0 ; j< schrott[0].length ; j++){
    			output[i][j] = (double)schrott[i][j];
    		}
    	}
    	
    	return output;
    }
    
    /**
     * keine ahnung, ob sich sowas als parameter aus animal heraus übergeben lässt.
     * wenn nicht, einfach durch strings ersetzen.
     * selbe für enum "Rotationsachse"
     * @author ZeRg
     *
     */
    public enum ModusAuswahl {
    	Translation {
    		public String toString() {
  	          return "Modus: Translation";
  	      }
    	},
    	Skalierung {
    		public String toString() {
    	          return "Modus: Skalierung";
    	      }
    	},
    	Scherung {
    		public String toString() {
  	          return "Modus: Scherung";
  	      }
    	},
    	Rotation {
    		public String toString() {
  	          return "Modus: Rotation";
  	      }
    	}
    }
    
    public enum Rotationsachse {
    	X {
    		public String toString() {
  	          return "Rotation um die x-Achse";
  	      }
    	},
    	Y {
    		public String toString() {
    	          return "Rotation um die y-Achse";
    	      }
    	},
    	Z {
    		public String toString() {
  	          return "Rotation um die z-Achse";
  	      }
    	},
    }
    
    public static void main(String[] args) {
    	
    	// Create a new animation
    	// name, author, screen width, screen height
    	Language l = new AnimalScript("Affine Abbildungen - Translation", "Team 11", 10, 10);
		Translation_Inhalt s = new Translation_Inhalt(l);
		
		//Das sind die Eingabeparameter
		//Transformationsmatrix
		int[][] a = {{-4,9,3},{18,6,1},{-2,3,1}};
		//Verschiebungsvektor
		int[][] b = {{5},{9},{2}};
		//Urbildvektor
		int [][] x = {{1},{2},{3}};
		
		s.simulateAllgemein();
		s.translation(x, b);
		
		
		System.out.println(l);
    		
    		

    		
    	}
}
