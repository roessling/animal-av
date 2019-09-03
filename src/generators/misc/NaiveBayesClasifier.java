/*
 * NaiveBayesClasifier.java
 * J. H. Mieseler, T. G. Petry , 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Offset;
import animal.main.Animal;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;

public class NaiveBayesClasifier implements Generator {
    private static Language l;
    private TextProperties PseudocodeHighlightingColor2;
    private String[] Testdaten;
    private ArrayProperties TestdataHighlightingColor;
    private TextProperties PseudocodeHighlightingColor1;
    private static String[][] Trainingsdaten;
    private MatrixProperties TrainingsdataHighlightingColor;
    private static Object Ergebnis;
    private static String[][] berechnungs_data;
    static int titel = 15; //größe der ueberschriften in den Tabellen
	static int ueberschrift = 20; // größe der ueberschriften gesamt
	static int textSize = 15; // größe der Texte gesamt

    public void init(){
        l = new AnimalScript("Naive Bayes Classifier [DE]", "J. H. Mieseler, T. G. Petry ", 800, 600);
    }

    public static void main(String[] args) {
    	Generator generator = new NaiveBayesClasifier();
    	Animal.startGeneratorWindow(generator);
    }    
    
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        PseudocodeHighlightingColor2 = (TextProperties)props.getPropertiesByName("PseudocodeHighlightingColor2");
        Testdaten = (String[])primitives.get("Testdaten");
        TestdataHighlightingColor = (ArrayProperties)props.getPropertiesByName("TestdataHighlightingColor");
        PseudocodeHighlightingColor1 = (TextProperties)props.getPropertiesByName("PseudocodeHighlightingColor1");
        Trainingsdaten = (String[][])primitives.get("Trainingsdaten");
        TrainingsdataHighlightingColor = (MatrixProperties)props.getPropertiesByName("TrainingsdataHighlightingColor");
        
        naiveBayes(Testdaten, Trainingsdaten, TestdataHighlightingColor, TrainingsdataHighlightingColor, PseudocodeHighlightingColor1, PseudocodeHighlightingColor2);
        return l.toString();
    }

    @SuppressWarnings("unused")
    public static void naiveBayes(String[] Testdaten, String[][] Trainingsdaten, ArrayProperties ap, MatrixProperties mp, TextProperties textmark, TextProperties textmark2) {
	    l.setStepMode(true);
	    l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	    String[] classvektor = new String[Trainingsdaten.length];
	    int lenX = Trainingsdaten.length;		// number of training samples
		int lenY = Trainingsdaten[0].length;	// number of attributes per training sample
		
		
		for(int i=0; i < classvektor.length; i++) {
			classvektor[i]= "0";
		}
		
		//Zähler fuer die Anzahl der verschiedenen Klassen
		int Klassenzähler = 0;
		// Aufzählen der verschiedenen Klassen und speichern in classvektor
		for(int i=0; i<= (lenX -1); i++) {
			for(int j=0; j<= (classvektor.length - 1); j++) {
				if(Trainingsdaten[i][lenY - 1].equals(classvektor[j])) {
					break;
				} else if(classvektor[j].equals("0")) {
					classvektor[j] = Trainingsdaten[i][lenY - 1];
					Klassenzähler = Klassenzähler + 1;
					break;	
				}
			}
		}
		
		//Anlegen der Berechnungen
		berechnungs_data = new String[Klassenzähler +1][lenY +1];
		
		// Berechnungsdaten vom Benutzer abhängig einlesen
		for(int i = 0; i < (Klassenzähler +1); i++) {
			for(int j = 0; j < (lenY +1); j++) {
				if(i== 0) {
					berechnungs_data[i][0] = "";
					berechnungs_data[i][1] = "Klasse";
					if(j > 1) {
						berechnungs_data[i][j]= Testdaten[j-2];
					}
				}
				else if(i > 0 && j == 0) {
					berechnungs_data[i][j] = classvektor[i-1]; 			
				}
				else {
					berechnungs_data[i][j] = ""; 
				}
			}
			
		}
	
		
		//anlegen verschiedener Textarten
		Font font = new Font("SansSerif", Font.BOLD, titel);
		Font schriftart = new Font("SansSerif", Font.BOLD, ueberschrift);
		Font introFont = new Font("SansSerif", 1, textSize);
		
		TextProperties ueberschrift = new TextProperties();
		ueberschrift.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		ueberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY, schriftart);
		
		TextProperties titel = new TextProperties();
		titel.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.getHSBColor(0F, 1.0F, 0.65F));
		titel.set(AnimationPropertiesKeys.FONT_PROPERTY, schriftart);
		
		TextProperties intro = new TextProperties();
		intro.set(AnimationPropertiesKeys.FONT_PROPERTY, introFont);
		
		TextProperties comment = new TextProperties();
		comment.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		
		
		//Text auf der Startseite
		Text naiveb = l.newText(new Coordinates (10,10), "Naive Bayes Classifier" , "titel", null, titel);
		//Lege das gelbe Rechteck unter den Titel
	    RectProperties var3 = new RectProperties();
	    var3.set("fillColor", Color.getHSBColor(0.115F, 0.15F, 1.0F));
	    var3.set("filled", true);
	    var3.set("depth", 2);
	    Rect yellowbox = l.newRect(new Offset(-5, -5, naiveb, "NW"), new Offset(5, 5, naiveb, "SE"), "hrect", (DisplayOptions)null, var3);
		l.nextStep();
		// Introduction text
		Text introt1 = l.newText(new Coordinates(30, 75), "Einleitende Erklaerungen", "intro", null, ueberschrift);
		Text introta = l.newText(new Coordinates(30, 100), "Der Naive Bayes Classifier (zu deutsch: naiver Bayes Klassifizierer) ist ein Algorithmus aus dem Bereich", "intro", null, intro);
		Text introtb = l.newText(new Coordinates(30, 115), "des maschinellen Lernens. Er nutzt den Satz von Bayes und ist auf sog. Bayesschen Wahrscheinlichkeiten aufgebaut.", "intro", null, intro);
		Text introtc = l.newText(new Coordinates(30, 130), "Bayesschen Wahrscheinlichkeiten werden nicht als Frequenz fuer das Eintreten eines bestimmten Ereignisses angesehen, ", "intro", null, intro);
		Text introtd = l.newText(new Coordinates(30, 145), "sondern als Gard der Glaubhauftigkeit eines Ergebnisses. Der Algorithmus wird naive genannt, da stochastische", "intro", null, intro);
		Text introte = l.newText(new Coordinates(30, 160), "Unabhaengigkeit der einzelnen Attribute der Trainingsdaten angenommen wird. Dies ist in der Realitaet oftmals", "intro", null, intro);
		Text introtf = l.newText(new Coordinates(30, 175), "nicht der Fall, dennoch hat sich in der Praxis gezeigt, dass sich trotz dieser starken Praemisse in den", "intro", null, intro);
		Text introtg = l.newText(new Coordinates(30, 190), "meisten Faellen konstant gute Ergebnisse erzielen lassen.", "intro", null, intro);
		Text introth = l.newText(new Coordinates(30, 205), "Außerdem ist der Naive Bayes Classifier ein supervised learning algorithm (ueberwachter Lern-Algorithmus).", "intro", null, intro);
		Text introti = l.newText(new Coordinates(30, 220), "Das bedeutet, dass die Klassen-Labels fuer Trainings- und Testdaten gegeben sein muessen.", "intro", null, intro);
		Text introtj = l.newText(new Coordinates(30, 235), "Das Ziel des Naive Bayes Classifier ist die Wahrscheinlichkeit einer falschen Klassifizierung zu minimieren.", "intro", null, intro);
		l.nextStep();
		Text introt2 = l.newText(new Coordinates(30, 260), "Class-Conditional Probability oder Likelihood", "intro", null, ueberschrift);
		Text introtk = l.newText(new Coordinates(30, 285), "Hierzu werden zuerst aus den Trainingsdaten die Klassen-bedingte Wahrscheinlichkeit des aktuell zu klassifizierenden", "intro", null, intro);
		Text introtl = l.newText(new Coordinates(30, 300), "Test-Beispiels fuer jede Klasse berechnet. Also die Vorkommenswahrscheinlichkeit der Attribute des Test-Beispiels unter", "intro", null, intro);
		Text introtm = l.newText(new Coordinates(30, 315), "jeder Klasse. Diese Wahrscheinlichkeit wird auch class-conditional probability oder likelihood genannt und", "intro", null, intro);
		Text introtn = l.newText(new Coordinates(30, 330), "mit p(x|C_k) bezeichnet, wobei x fuer den Vector der Attribute des Beispiels steht und C_k fuer Klasse k", "intro", null, intro);
		l.nextStep();
		Text introt3 = l.newText(new Coordinates(30, 355), "Class Prior", "intro", null, ueberschrift);
		Text introto = l.newText(new Coordinates(30, 380), "Dann wird die A-prioir-Wahrscheinlichkeit jeder Klasse bestimmt. Also die Vorkommenswahrscheinlichkeit jeder Klasse.", "intro", null, intro);
		Text introtp = l.newText(new Coordinates(30, 395), "Diese Wahrscheinlichkeit wird auch class prior genannt und mit p(C_k) bezeichnet.", "intro", null, intro);
		l.nextStep();
		Text introt4 = l.newText(new Coordinates(30, 420), "Normalisierungsterm", "intro", null, ueberschrift);
		Text introtq = l.newText(new Coordinates(30, 445), "Weiterhin muss um den Satz von Bayes anzuwenden noch ein Normalisierungsterm berechnet werden, damit die Ergebnisse", "intro", null, intro);
		Text introtr = l.newText(new Coordinates(30, 460), "auf einer Skala von Wahrscheinlichkeiten, also zwischen 0 und 1 dargestellt werden koennen.", "intro", null, intro);
		Text introts = l.newText(new Coordinates(30, 475), "Der Normalisierungsterm ist Marginalverteilung des Attributvektors des Beispiels, also die Summe der likelihood", "intro", null, intro);
		Text intrott = l.newText(new Coordinates(30, 490), "fuer jede Klasse multipliziert mit dem zugehoerigen class prioir: p(x) = SUM_over_k( p(x|C_k) * p(C_k) ) ", "intro", null, intro);
		l.nextStep();
		Text introt5 = l.newText(new Coordinates(30, 515), "Class Posterior und Entscheidungsfindung", "intro", null, ueberschrift);
		Text introtu = l.newText(new Coordinates(30, 540), "Zuletzt wird die A-posteriori-Wahrscheinlichkeit fuer jede Klasse mittels dem Satz von Bayes berechnet:", "intro", null, intro);
		Text introtv = l.newText(new Coordinates(30, 555), "p(C_K|x) = p(x|C_k) * p(C_k) / p(x)", "intro", null, intro);
		Text introtw = l.newText(new Coordinates(30, 570), "Die A-posteriori-Wahrscheinlichkeit wird auch class posterior genannt.", "intro", null, intro);
		Text introtx = l.newText(new Coordinates(30, 585), "Es wird nun diejenige Klasse fuer das zu klassifizierende Beispiel vorhergesagt, welche den groessten class posterior besitzt.", "intro", null, intro);
		Text introty = l.newText(new Coordinates(30, 600), "Denn das Maximieren des class posteriors kommt dem Minimieren einer falschen Klassifizierung gleich.", "intro", null, intro);
		
		l.nextStep();
		
		//verstecke Startseitentext
		List<Primitive> keepThose = new LinkedList<Primitive>();
		keepThose.add(naiveb);
		keepThose.add(yellowbox);
		l.hideAllPrimitivesExcept(keepThose);
		
		//Fragen: 
		
		MultipleChoiceQuestionModel einstellung = new MultipleChoiceQuestionModel("einstellung");
		einstellung.setPrompt("Haben Ihre Testdaten gleiche viele Attribute wie die Trainingsdaten, nur ohne das Klassenattribute und sind diese in der selben Reihenfolge?");
		einstellung.addAnswer("Ja.", 1, "Hervorragend. Fahren Sie nun mit den Visualisierungen fort.");
		einstellung.addAnswer("Nein.", 2, "Brechen Sie bitte die Visualisierung ab, und ändern Sie Ihre Primitiven dementsprechend.");
		l.addMCQuestion(einstellung);
		l.nextStep();
				
				
		//anlegen des Peseudo Codes
		Text pseudoCode1 = l.newText(new Coordinates(50, 60),  "PseudoCode", "pcode", null, ueberschrift);
		Text pseudoCode2 = l.newText(new Offset(0, 0, pseudoCode1, "SW"),  "START", "pcode", null);
		Text pseudoCode3 = l.newText(new Offset(0, 0, pseudoCode2, "SW"), "// Erstellung einer Wertetabelle um speatere Berchnungen zu vereinfachen", "pcode", null, comment);
		Text pseudoCode4 = l.newText(new Offset(0, 0, pseudoCode3, "SW"), "for each class c:", "test", null);
		Text pseudoCode5 = l.newText(new Offset(0, 0, pseudoCode4, "SW"), "   for each sample s in trainings_data:", "pcode", null);
		Text pseudoCode6 = l.newText(new Offset(0, 0, pseudoCode5, "SW"), "      if (class_attribute of s == c):", "pcode", null);
		Text pseudoCode7 = l.newText(new Offset(0, 0, pseudoCode6, "SW"), "         class_counter++", "test", null);
		Text pseudoCode8 = l.newText(new Offset(0, 0, pseudoCode7, "SW"), "for each class c:", "pcode", null);
		Text pseudoCode9 = l.newText(new Offset(0, 0, pseudoCode8, "SW"), "   for each attribut a from test_data:", "pcode", null);
		Text pseudoCode10 = l.newText(new Offset(0, 0, pseudoCode9, "SW"), "      if (class_attribute of sample in trainings_data == c &&", "pcode", null);
		Text pseudoCode11 = l.newText(new Offset(0, 0, pseudoCode10, "SW"), "          value of attribute a of sample in trainings_data == value of attribute a in test_data): ", "pcode", null);
		Text pseudoCode12 = l.newText(new Offset(0, 0, pseudoCode11, "SW"), "         counter_attribute_a_for_class_c++", "pcode", null);
		Text pseudoCode13 = l.newText(new Offset(0, 0, pseudoCode12, "SW"), "// Berechnung der Likelihood jeder Klasse", "pcode", null, comment);
		Text pseudoCode14 = l.newText(new Offset(0, 0, pseudoCode13, "SW"), "for each class c:", "pcode", null);
		Text pseudoCode15 = l.newText(new Offset(0, 0, pseudoCode14, "SW"), "   multiply all counter_attribute_a_for_class_c", "pcode", null);
		Text pseudoCode16 = l.newText(new Offset(0, 0, pseudoCode15, "SW"), "// Brechnung des Class Priors fuer jede Klasse", "pcode", null, comment);
		Text pseudoCode17 = l.newText(new Offset(0, 0, pseudoCode16, "SW"), "for each class c:", "pcode", null);
		Text pseudoCode18 = l.newText(new Offset(0, 0, pseudoCode17, "SW"), "   class_counter / #of_trainings_samples", "pcode", null);
		Text pseudoCode19 = l.newText(new Offset(0, 0, pseudoCode18, "SW"), "// Berechnung des Normalisierungsterms", "pcode", null, comment);
		Text pseudoCode20 = l.newText(new Offset(0, 0, pseudoCode19, "SW"), "sum over each class c:", "pcode", null);
		Text pseudoCode21 = l.newText(new Offset(0, 0, pseudoCode20, "SW"), "   likelihood_of_c * prior_of_c", "pcode", null);
		Text pseudoCode22 = l.newText(new Offset(0, 0, pseudoCode21, "SW"), "// Brechnung des Class Posteriors fuer jede Klasse", "pcode", null, comment);
		Text pseudoCode23 = l.newText(new Offset(0, 0, pseudoCode22, "SW"), "for each class c:", "pcode", null);
		Text pseudoCode24 = l.newText(new Offset(0, 0, pseudoCode23, "SW"), "   likelihood_of_c * prior_of_c / normalisation_term", "pcode", null);
		Text pseudoCode25 = l.newText(new Offset(0, 0, pseudoCode24, "SW"), "return class c with highest posterior", "pcode", null);
		Text pseudoCode26 = l.newText(new Offset(0, 0, pseudoCode25, "SW"), "END", "pcode", null);
		
		Text erklaerungen = l.newText(new Offset(0, 15, pseudoCode26, "SW"), "Erklaerungen", "test", null, ueberschrift);
		
		
		//anlegen der Trainingsdaten
	
		mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
	
		Text trainingsdaten = l.newText(new Offset(400, 0, pseudoCode1, "NE"), "Trainingsdaten", "Trainingsdaten", null, ueberschrift);
		StringMatrix stringMatrix = l.newStringMatrix(new Offset(0, 10, trainingsdaten, "SW"), Trainingsdaten, "trainingsdaten", null, mp);
		
				
		//anlegen der Testdaten
	
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		Text testdaten = l.newText(new Offset(50, -36, stringMatrix, "NE"), "Aktuelle Testdaten", "testdaten", null, ueberschrift);
		StringArray stringArray = l.newStringArray(new Offset(0, 10, testdaten, "SW"), Testdaten, "Das hier sind unsere Testdaten", null, ap);
		stringArray.showIndices(false, null, null);
		
		
		//Rechnungen verschönern
		Text rechnung = l.newText(new Offset(0, 30, stringArray, "SW"), "Zwischenergebnisse", "rechnung", null, ueberschrift);
		StringMatrix Berechnungen = l.newStringMatrix(new Offset(0, 10, rechnung, "SW"), berechnungs_data, "Berechnungen", null, mp);
		for (int i = 0; i <= lenY; i++) {
			Berechnungen.setGridFont(0, i, font, null, null);
		}
	
		
		//anlegen der Erklärungen:
		Text Erklärung1 = l.newText(new Offset(0, 0, erklaerungen, "SW"), "Wir zaehlen, wie oft jede Klasse in den Trainingsdaten vorkommt.", "Erklärungen", null);
		Text Erklärung2 = l.newText(new Offset(0, 0, erklaerungen, "SW"), "Wir zaehlen, wie oft jeder Attribut-Wert des Test-Beispiels in jeder Klasse vorkommt.", "Erklärungen", null);
		Text Erklärung3 = l.newText(new Offset(0, 0, erklaerungen, "SW"), "Nun wird die likelihood berechnet: ", "Erklärungen", null);
		Text Erklärung3_1 = l.newText(new Offset(0, 15, erklaerungen, "SW"), "Hinweis: Sollte ein Attributwert in einer Klasse nicht vorkommen,", "Erklärungen", null);
		Text Erklärung3_2 = l.newText(new Offset(0, 30, erklaerungen, "SW"), "wird fuer diesen eine Vorkommenswahrscheinlichkeit von 1% angenommen.", "Erklärungen", null);
		Text Erklärung4 = l.newText(new Offset(0, 0, erklaerungen, "SW"), "Den Prior kann man aus der Tabelle ablesen.", "Erklärungen", null);
//		Text Erklärung5 = l.newText(new Offset(0, 0, erklaerungen, "SW"), "Die Werte werden nun normalisiert: ", "Erklärungen", null);
		Text Erklärung5 = l.newText(new Offset(0, 0, erklaerungen, "SW"), "Um zu normalisieren wird Prior * Likelihood einer Klasse, durch die Summe ueber alle Klassen dieses Produkts geteilt.", "Erklärungen", null); 
		Text Erklärung6 = l.newText(new Offset(0, 0, erklaerungen, "SW"), "Wir sagen die Klasse mit dem hoechsten Posterior fuer das Test-Beispiel voraus.", "Erklärungen", null);
		
		Erklärung1.hide();
		Erklärung2.hide();
		Erklärung3.hide();
		Erklärung3_1.hide();
		Erklärung3_2.hide();
		Erklärung4.hide();
		Erklärung5.hide();
		Erklärung6.hide();
			
		l.nextStep();
		
		pseudoCode2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		//lärung1.show();	//Erklärung zum ersten Schritt
		l.nextStep();

		pseudoCode2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
		
		//markiere jede Zeile die die selbe Klasse enthält.
		int[] Zähler;
		int[][] AttributeZähler;
		Zähler = new int[lenX];
		AttributeZähler = new int[lenY][Klassenzähler];
	
		//fuer jede Klasse
		Erklärung1.show();
		for(int x=0; x < Klassenzähler; x++) {
			pseudoCode4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			Berechnungen.highlightCell(x+1, 0, null, null);
			l.nextStep();
			//Schaue welche Zeile zu welcher Klasse gehört und schreibe es direkt in Berechnungen rein.
			for(int i=0; i <= (lenX -1); i++ ) {
				pseudoCode4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				pseudoCode5.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				stringMatrix.highlightElem(i, lenY-1, null, null);
				l.nextStep();
				pseudoCode5.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				pseudoCode6.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);

				l.nextStep();
				if(Trainingsdaten[i][lenY - 1].equals(classvektor[x])) {
					Zähler[x]++;
					for(int j=0; j <= (lenY -1); j++) {
						stringMatrix.highlightCell(i,j,null,null);	
						}
					pseudoCode6.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					pseudoCode7.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					l.nextStep(); 
					}
				Berechnungen.put(x+1,1, Zähler[x] + "/" + lenX, null, null); 
				pseudoCode6.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				pseudoCode7.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			}	
			pseudoCode5.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
			
			// Lösche alle Markierungen aus der Matrix nach jeder Klasse
			for(int a=0; a<=(lenX -1); a++ ) {
				for( int b=0; b <=(lenY-1); b++) {
					stringMatrix.unhighlightCell(a,b, null, null);	
					stringMatrix.unhighlightElem(a, b, null, null);
				}
			}
			Berechnungen.unhighlightCell(x+1, 0, null, null);
		}
		pseudoCode4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);

		Erklärung1.hide();
		
		// =============================================================================================================================================================================================================================
		//Zweiter Schritt: Attribut-Wert-Häufigkeiten fuer jede Klasse zählen
		
		MultipleChoiceQuestionModel AttributeWertHaeufigkeit = new MultipleChoiceQuestionModel("AttributeWertHaeufigkeit");
		AttributeWertHaeufigkeit.setPrompt("Welche Werte benötigen wir als nächstes?");
		AttributeWertHaeufigkeit.addAnswer("Die Anzahl der Attribut-Wert-Häufigkeiten für jede Klasse.", 1, "Korrekt.");
		AttributeWertHaeufigkeit.addAnswer("Die Anzahl der Zeichen pro Attribute.", 2, "Leider falsch");
		AttributeWertHaeufigkeit.addAnswer("Die Wahrscheinlichkeit einer Doppelung eines Eingabedatensatzes.", 3, "Leider falsch");
		l.addMCQuestion(AttributeWertHaeufigkeit);
		l.nextStep();
		
		Erklärung2.show();	//Erklärung zum zweiten Schritt
		l.nextStep();
	
		//ueberpruefe Attribute der Testdaten nach Klassenverteilung. Markiere Sie und trage in Berechnungen ein
		int Berechnung;
		for(int x=0; x < Klassenzähler; x++) {
			Berechnung = 2;
			pseudoCode8.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			Berechnungen.highlightCell(x+1, 0, null, null);
			l.nextStep();
			
			for(int j=0; j < (lenY -1); j++) {
				pseudoCode8.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				pseudoCode9.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);

				Berechnungen.highlightCell(0, j+2, null, null);
				stringArray.highlightCell(j, null, null); 
				l.nextStep();
				pseudoCode9.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				pseudoCode10.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				pseudoCode11.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				for(int i=0; i <= (lenX -1); i++ ) { 	
					if(Trainingsdaten[i][j].equals(Testdaten[j]) && Trainingsdaten[i][lenY - 1].equals(classvektor[x])) {
						AttributeZähler[j+1][x]++;		
						stringMatrix.highlightCell(i,j,null,null);
					}
				}
				l.nextStep();
				
				Berechnungen.put(x + 1, Berechnung, AttributeZähler[Berechnung - 1][x] + "/" + Zähler[x], null, null);  
				if(AttributeZähler[Berechnung - 1][x] > 0) {
					pseudoCode10.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					pseudoCode11.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					pseudoCode12.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				} else {
					pseudoCode10.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					pseudoCode11.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				
				}
				Berechnung++;
				l.nextStep();
				pseudoCode10.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
				pseudoCode11.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
				pseudoCode12.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			}
			
			// Lösche alle Markierungen aus der Matrix nach jeder Klasse
			for(int a=0; a<=(lenX -1); a++ ) {
				for( int b=0; b <=(lenY-1); b++) {
					stringMatrix.unhighlightCell(a,b, null, null);	
					stringArray.unhighlightCell(b, null, null);
				}
			}
			
			//lösche alle Markierungen aus Berechnungen
			for (int i = 0; i <=lenY; i++) {
				for (int j = 0; j <= Klassenzähler; j++) {
					Berechnungen.unhighlightCell(j, i, null, null);
				}
			}
			pseudoCode9.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
			pseudoCode10.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
			pseudoCode11.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode12.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);

		}
			
		
		// Lösche alle Markierungen aus der Matrix
		for(int a=0; a<=(lenX -1); a++ ) {
			for( int b=0; b <=(lenY-1); b++) {
				stringMatrix.unhighlightCell(a,b, null, null);	
				stringMatrix.unhighlightElem(a,b, null, null);
				stringArray.unhighlightElem(b, null, null);
			}
		}
		pseudoCode8.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);	
		Erklärung2.hide();
		
		// =============================================================================================================================================================================================================================
		// Dritter Schritt: Berechnung der Likelihood
		
		MultipleChoiceQuestionModel likelihood = new MultipleChoiceQuestionModel("likelihood");
		likelihood.setPrompt("Was ist die Likelihood?");
		likelihood.addAnswer("Die Likelihood (genauer: class-conditional probability) ist die Wahrscheinlichkeit, dass ein Attributwertvektor in einer bestimmten Klasse vorkommt.", 1, "Korrekt. ");
		likelihood.addAnswer("Die Likelihood (genauer: class prior) ist die Vorkommenswahrscheinlichkeit einer Klasse.", 2, " Leider falsch.");
		likelihood.addAnswer("Eine nette Wohngegend.", 3, "Leider falsch.");
		l.addMCQuestion(likelihood);
		l.nextStep();
		
		Erklärung3.show();	//Erklärung zum dritten Schritt
		Erklärung3_1.show();
		Erklärung3_2.show();
		l.nextStep();
		
		//Berechne Likelihood fuer jede Klasse und schreibe die Rechnung auf
		for(int x=0; x < Klassenzähler; x++) {
			pseudoCode14.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				

			Berechnungen.highlightCell(x+1, 0, null, null);
			Text t = l.newText(new Offset(0, 10 + 15*x, Berechnungen, "SW"), "Likelihood Klasse " + Berechnungen.getElement(x+1, 0) + ": ", "Likeli", null); 
			l.nextStep();
			pseudoCode14.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				
			pseudoCode15.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				

			for(int i=2; i<=(lenY); i++){
				Berechnungen.highlightCell(x+1,i,null,null);	
				Offset o = new Offset(30*(i - 2), 0, t, AnimalScript.DIRECTION_BASELINE_END); 
				if(i< lenY) {
					Text t1 = l.newText(o, Berechnungen.getElement(x + 1, i) + " * ", "test", null);
					l.nextStep();
			 	}
				if(i == lenY) {
					Text t2 = l.newText(o, Berechnungen.getElement(x + 1, i) + "  =  " + classify(Testdaten, x, 4),"JonasCode", null);
					l.nextStep();
				}
			}
			pseudoCode15.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);				

			//Lösche Markierungen 
			for(int a=0; a<=(Klassenzähler); a++ ) {
				for( int b=0; b <=(lenY); b++) {
					Berechnungen.unhighlightCell(a,b, null, null);	
				}
			}
		}
		pseudoCode14.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);				
		
		Erklärung3.hide();	
		Erklärung3_1.hide();
		Erklärung3_2.hide();
		
		// =============================================================================================================================================================================================================================
		// Vierter Schritt: Berechnung des Priors
		
		MultipleChoiceQuestionModel naiveBayes = new MultipleChoiceQuestionModel("naiveBayes");
		naiveBayes.setPrompt("Wissen Sie noch, warum der Classifizierer 'Bayes' im Namen trägt?");
		naiveBayes.addAnswer("Da alle Wahrscheinlichkeiten hier nicht als Frequenz fuer das Eintreten eines bestimmten Ereignisses angesehen werden, sondern als Gard der Glaubhauftigkeit des Ergebnisses.", 1, "Korrekt.");
		naiveBayes.addAnswer("Da Thomas Bayes nicht nur den beruehmten Satz in der Mathematik, sondern auch diesen Algorithmus entwickelt.", 2, "Leider falsch.");
		l.addMCQuestion(naiveBayes);
		l.nextStep();
		
		Erklärung4.show();
		l.nextStep();
		
		//Schreibe Prior auf
		for(int x=0; x < Klassenzähler; x++) {
			pseudoCode17.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				

			Berechnungen.highlightCell(x+1,1,null,null);
			l.nextStep();
			pseudoCode17.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				
			pseudoCode18.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				

			Text Prior = l.newText(new Offset(0, 20 + 15*(x + Klassenzähler), Berechnungen, "SW"), "Prior Klasse " + Berechnungen.getElement(x+1, 0) + ": " + Berechnungen.getElement(x+1, 1), "test", null);
			l.nextStep();
			pseudoCode18.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);				

			Berechnungen.unhighlightCell(x+1,1,null,null);
		}
		
		pseudoCode17.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);				
		Erklärung4.hide();
		
		// =============================================================================================================================================================================================================================
		// Fünfter Schritt: Normalisieren
		
		MultipleChoiceQuestionModel normalisieren = new MultipleChoiceQuestionModel("normalisieren");
		normalisieren.setPrompt("Ist Normalisieren notwendig?");
		normalisieren.addAnswer("Ja, um festzustellen, fuer welche Klasse wir uns entscheiden sollen.", 1, "Leider falsch.");
		normalisieren.addAnswer("Ja, da wir sonst zwar wuessten fuer welche Klasse wir uns entscheiden sollen, allerdings nicht wie sicher diese Entscheidung ist.", 2, "Leider falsch.");
		normalisieren.addAnswer("Nein, es hilft lediglich beim Vergleich Klassen A-Posteriori's, da diese somit auf Bayesche Wahrscheinlichkeiten abgebildet werden. ", 3, "Korrekt.");
		l.addMCQuestion(normalisieren);
		l.nextStep();
		
		Erklärung5.show();	
		l.nextStep();
		pseudoCode20.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				
		pseudoCode21.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				

		l.nextStep();
		
		for(int x=0; x < Klassenzähler; x++) {
			Text likeliPrior = l.newText(new Offset(0, 30 + 15*(x + Klassenzähler * 2), Berechnungen, "SW"),
					"Likelihood * Prior fuer Klasse " + Berechnungen.getElement(x + 1, 0) + ": " +  classify(Testdaten, x, 1), 
					"likeliPrior", null); 
			l.nextStep();
		}
		
		
		double counter = 0;
		for(int x=0; x < Klassenzähler; x++) {
			counter = counter + classify(Testdaten, x, 1);
		}
		Text normTerm = l.newText(new Offset(0, 40 + 15*(Klassenzähler * 3), Berechnungen, "SW"), "Normalisierungsterm: " + counter, "normTerm", null);
		
		l.nextStep();
		
		Erklärung5.hide();
		pseudoCode20.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);				
		pseudoCode21.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);				

		
		
		// =============================================================================================================================================================================================================================
		// Sechster Schritt: Ergebnis ausgeben
		Erklärung6.show();
		Berechnungen.hide();
		l.nextStep();
		
		Text Normalisierung3 = l.newText(new Offset(0, 0, Berechnungen, "NW"), "", "", null);
		for(int x=0; x < Klassenzähler; x++) {
			pseudoCode23.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				

			l.nextStep();
			pseudoCode23.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark2.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				
			pseudoCode24.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				
			Normalisierung3 = l.newText(new Offset(0, 0+((1+x)*15), Berechnungen, "NW"), Berechnungen.getElement(x+1, 0) + ": " + classify(Testdaten, x, 1) + " / " + counter +" = "+ classify(Testdaten, x, 2)  , "norm anzeigen", null);
			l.nextStep();
			pseudoCode24.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);				

		}
		pseudoCode23.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);				
		
		classify(Testdaten, 1, 3);
		//Gebe markietes Ergebnis aus
		
		pseudoCode25.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				
		Text Ergebnis1 = l.newText(new Offset(0, 10, Normalisierung3, "SW"), "Das Ergebnis ist somit: " + Ergebnis , "Ergebnis", null);
	    l.newRect(new Offset(-5, -5, Ergebnis1, "NW"), new Offset(5, 5, Ergebnis1, "SE"), "hrect", (DisplayOptions)null, var3);
		l.nextStep();
		pseudoCode25.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);				
		pseudoCode26.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) textmark.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				

	
		l.finalizeGeneration();
		//System.out.println(l.toString());
    }
       
    
    public static double classify(String[] Testdaten, int x, int aufruf) {
		int lenX = Trainingsdaten.length;		// number of training samples
		int lenY = Trainingsdaten[0].length;	// number of attributes per training sample
		//System.out.println("lenX: " + lenX);
		//System.out.println("lenY: " + lenY);
		
		// map all training-classes to the number of their occurrences
		LinkedHashMap<String, Double> classes = new LinkedHashMap<String, Double>(); 	
		for(int i = 0; i < lenX; i++) {
			if(classes.containsKey(Trainingsdaten[i][lenY - 1])) {
				classes.put(Trainingsdaten[i][lenY - 1], classes.get(Trainingsdaten[i][lenY - 1]) + 1);
			} else {
				classes.put(Trainingsdaten[i][lenY - 1], 1.0);
			}
		}
		//System.out.println("Klassen und ihre Anzahl: " + classes.toString());
		
		// map all test-features to the number of their occurrences
		LinkedHashMap<String, Double> features = new LinkedHashMap<String, Double>(); 
		for(int i = 0; i < Testdaten.length; i++) {
			features.put(Testdaten[i], 0.0);
			for(int j = 0; j < lenX; j++) {
				if(features.containsKey(Trainingsdaten[j][i])) {
					features.put(Trainingsdaten[j][i], features.get(Trainingsdaten[j][i]) + 1);
				}
			}
		}
		//System.out.println("Test-Features und ihre Anzahl: " + features.toString());
		
		// Calculate likelihood of test feature sample for each class
		Object[][] likelihood = new Object[2][classes.size()];
		likelihood[1] = classes.keySet().toArray();
		int cnt = 0;
		@SuppressWarnings("unused")
    double tmp;
		double pXCi = 1;
		for(int i = 0; i < classes.size(); i++) { 	// For each class
			for(int j = 0; j < lenY - 1; j++) {		// For each feature
				for(int k = 0; k < lenX; k++) {		// For every test-data sample
					if(Trainingsdaten[k][j].equals(Testdaten[j]) && Trainingsdaten[k][lenY - 1].equals(likelihood[1][i])) {	// Count occurrences of feature j in class i
						cnt++;
					}
				}
				//System.out.println("Anzahl von Feature " + Testdaten[j] + " unter Klasse " + likelihood[1][i] + ": " + cnt);
				// Multiply pXCi with pXjCi. Should pXjCi == 0 use 0.01 instead
				if(cnt / classes.get(likelihood[1][i]) == 0) {
					pXCi = pXCi * 0.01;
				} else {
					pXCi = pXCi * cnt / classes.get(likelihood[1][i]);
				}
				cnt = 0;	// Reset cnt 
			}
			//System.out.println("Kummulierte Likelihood ueber alle Features von Klasse " + likelihood[1][i] + ": " + pXCi);
			likelihood[0][i] = (double) pXCi;	// Save pXCi in the likelihood array to corresponding class
			pXCi = 1;	// Rest pXCi
		}
				
		if(aufruf ==4 ){
			return (double) likelihood[0][x];    //gebe die Kummulierte Likelihood ueber Klasse x in Animal aus
			}
		
		// Calculate the probability of the occurrence of class i
		Object[][] pCi = new Object[2][classes.size()];
		pCi[1] = classes.keySet().toArray();
		for(int i = 0; i < classes.size(); i++) {
			pCi[0][i] = (double)(classes.get(pCi[1][i]) / lenX);
		}
		for(int i = 0; i < classes.size(); i++) {
			//System.out.println("Vorkommenswahrscheinlichkeit von Klasse " + pCi[1][i] + " : " + pCi[0][i]);
		}
		
		// Calculate the probability of the occurrence of feature-vector over the hole training set
		double pX = 0;
		for(int j = 0; j < classes.size(); j++) {
			pX = pX + (double) likelihood[0][j] * (double) pCi[0][j];
		}
		//System.out.println("Vorkommenswahrscheinlichkeit des Attribut-Vektors des Testdatenpunktes unter den gegebenen Trainingsdaten: " + pX);
		
		// Calculate posterior distribution for each class i: p(C_i | X) = p(X | C_i)[likelihood] * p(C_i)[class prior] / p(X)[normalisation factor]
		Object[][] pCiX = new Object[2][classes.size()];
		pCiX[1] = classes.keySet().toArray();
		for(int i = 0; i < classes.size(); i++) { 
			if(aufruf == 1) {
				pCiX[0][i] = (((double) likelihood[0][i]) * ((double) pCi[0][i]));   //   / pX;
			}else {
				pCiX[0][i] = (((double) likelihood[0][i]) * ((double) pCi[0][i])) / pX;
			}
		}
		
		// Find index of class with highest posterior distribution
		double max = 0;
		int ind = 0;
		for(int i = 0; i < classes.size(); i++) {
			if((double) pCiX[0][i] > max) {
				max = (double) pCiX[0][i];
				ind = i;
			}
		}
		
		for(int i = 0; i < classes.size(); i++) {
			//System.out.println(pCiX[1][i] + " : " + pCiX[0][i]);
		}
		
		
		//System.out.println("Winner: " + pCiX[1][ind]);
		if(aufruf == 3) {
			Ergebnis = pCiX[1][ind];
			return 0.0;
		}else {
			return (double) pCiX[0][x];	
		}
	} 
    
    
    public String getName() {
        return "Naive Bayes Classifier [DE]";
    }

    public String getAlgorithmName() {
        return "Naive Bayes Classifier (for nominal attribute values) ";
    }

    public String getAnimationAuthor() {
        return "J. H. Mieseler, T. G. Petry ";
    }

    public String getDescription(){
        return "Im Bereich des maschinellen Lernens sind \"naive Bayes classifiers\" eine Familie von simplen Klassifizierern,"
 +"\n"
 +"welche nicht nur diejenige Klasse fuer eine gegebene Beobachtung zurueckgeben, die am wahrscheinlichsten ist,"
 +"\n"
 +"sonder eine Wahrscheinlichkeitsverteilung ueber die Menge aller moeglichen Klassen, bzw. fuer wie wahrscheinlich"
 +"\n"
 +"der Klassifizierer jede Klasse fuer die gegebene Beobachtung haelt."
 +"\n"
 +"Diese Familie von Klassifizierern bassiert auf der Anwendung des Satzes von Bayes unter der starken (naiven) Annahme"
 +"\n"
 +"der stochastischen Unabhaengigkeit der einzelnen Attribute der Trainingsdaten."
 +"\n"
 +"Der hier implementierte Algorithmus soll ein Grundlegendes Verstaendis fuer die Familie der \"naive Bayes classifiers\""
 +"\n"
 +"liefern und nutzt daher nominelle Attribute, sprich \"1\" und \"2\" sind genau so unterschiedlich wie \"1\" und \"100\""
 +"\n"
 +"oder \"blau\" und \"rot\".";
    }

    public String getCodeExample(){
        return "public static double classify(String[] Testdaten, int x, int aufruf) {"
 +"\n"
 +"		int lenX = Trainingsdaten.length;		// number of training samples"
 +"\n"
 +"		int lenY = Trainingsdaten[0].length;	// number of attributes per training sample"
 +"\n"
 +"		System.out.println(\"lenX: \" + lenX);"
 +"\n"
 +"		System.out.println(\"lenY: \" + lenY);"
 +"\n"
 +"		"
 +"\n"
 +"		// map all training-classes to the number of their occurrences"
 +"\n"
 +"		HashMap<String, Double> classes = new HashMap<String, Double>(); 	"
 +"\n"
 +"		for(int i = 0; i < lenX; i++) {"
 +"\n"
 +"			if(classes.containsKey(Trainingsdaten[i][lenY - 1])) {"
 +"\n"
 +"				classes.put(Trainingsdaten[i][lenY - 1], classes.get(Trainingsdaten[i][lenY - 1]) + 1);"
 +"\n"
 +"			} else {"
 +"\n"
 +"				classes.put(Trainingsdaten[i][lenY - 1], 1.0);"
 +"\n"
 +"			}"
 +"\n"
 +"		}"
 +"\n"
 +"		System.out.println(\"Klassen und ihre Anzahl: \" + classes.toString());"
 +"\n"
 +"		"
 +"\n"
 +"		// map all test-features to the number of their occurrences"
 +"\n"
 +"		HashMap<String, Double> features = new HashMap<String, Double>(); "
 +"\n"
 +"		for(int i = 0; i < Testdaten.length; i++) {"
 +"\n"
 +"			features.put(Testdaten[i], 0.0);"
 +"\n"
 +"			for(int j = 0; j < lenX; j++) {"
 +"\n"
 +"				if(features.containsKey(Trainingsdaten[j][i])) {"
 +"\n"
 +"					features.put(Trainingsdaten[j][i], features.get(Trainingsdaten[j][i]) + 1);"
 +"\n"
 +"				}"
 +"\n"
 +"			}"
 +"\n"
 +"		}"
 +"\n"
 +"		System.out.println(\"Test-Features und ihre Anzahl: \" + features.toString());"
 +"\n"
 +"		"
 +"\n"
 +"		// Calculate likelihood of test feature sample for each class"
 +"\n"
 +"		Object[][] likelihood = new Object[2][classes.size()];"
 +"\n"
 +"		likelihood[1] = classes.keySet().toArray();"
 +"\n"
 +"		int cnt = 0;"
 +"\n"
 +"		double tmp;"
 +"\n"
 +"		double pXCi = 1;"
 +"\n"
 +"		for(int i = 0; i < classes.size(); i++) { 	// For each class"
 +"\n"
 +"			for(int j = 0; j < lenY - 1; j++) {		// For each feature"
 +"\n"
 +"				for(int k = 0; k < lenX; k++) {		// For every test-data sample"
 +"\n"
 +"					if(Trainingsdaten[k][j].equals(Testdaten[j]) && Trainingsdaten[k][lenY - 1].equals(likelihood[1][i])) {	// Count occurrences of feature j in class i"
 +"\n"
 +"						cnt++;"
 +"\n"
 +"					}"
 +"\n"
 +"				}"
 +"\n"
 +"				System.out.println(\"Anzahl von Feature \" + Testdaten[j] + \" unter Klasse \" + likelihood[1][i] + \": \" + cnt);"
 +"\n"
 +"				// Multiply pXCi with pXjCi. Should pXjCi == 0 use 0.01 instead"
 +"\n"
 +"				if(cnt / classes.get(likelihood[1][i]) == 0) {"
 +"\n"
 +"					pXCi = pXCi * 0.01;"
 +"\n"
 +"				} else {"
 +"\n"
 +"					pXCi = pXCi * cnt / classes.get(likelihood[1][i]);"
 +"\n"
 +"				}"
 +"\n"
 +"				cnt = 0;	// Reset cnt "
 +"\n"
 +"			}"
 +"\n"
 +"			System.out.println(\"Kummulierte Likelihood ueber alle Features von Klasse \" + likelihood[1][i] + \": \" + pXCi);"
 +"\n"
 +"			likelihood[0][i] = (double) pXCi;	// Save pXCi in the likelihood array to corresponding class"
 +"\n"
 +"			pXCi = 1;	// Rest pXCi"
 +"\n"
 +"		}"
 +"\n"
 +"		"
 +"\n"
 +"		if(aufruf ==4 ){"
 +"\n"
 +"			return (double) likelihood[0][x];    //gebe die Kummulierte Likelihood ueber Klasse x in Animal aus"
 +"\n"
 +"			}"
 +"\n"
 +"		"
 +"\n"
 +"		// Calculate the probability of the occurrence of class i"
 +"\n"
 +"		Object[][] pCi = new Object[2][classes.size()];"
 +"\n"
 +"		pCi[1] = classes.keySet().toArray();"
 +"\n"
 +"		for(int i = 0; i < classes.size(); i++) {"
 +"\n"
 +"			pCi[0][i] = (double)(classes.get(pCi[1][i]) / lenX);"
 +"\n"
 +"		}"
 +"\n"
 +"		for(int i = 0; i < classes.size(); i++) {"
 +"\n"
 +"			System.out.println(\"Vorkommenswahrscheinlichkeit von Klasse \" + pCi[1][i] + \" : \" + pCi[0][i]);"
 +"\n"
 +"		}"
 +"\n"
 +"		"
 +"\n"
 +"		// Calculate the probability of the occurrence of feature-vector over the hole training set"
 +"\n"
 +"		double pX = 0;"
 +"\n"
 +"		for(int j = 0; j < classes.size(); j++) {"
 +"\n"
 +"			pX = pX + (double) likelihood[0][j] * (double) pCi[0][j];"
 +"\n"
 +"		}"
 +"\n"
 +"		System.out.println(\"Vorkommenswahrscheinlichkeit des Attribut-Vektors des Testdatenpunktes unter den gegebenen Trainingsdaten: \" + pX);"
 +"\n"
 +"		"
 +"\n"
 +"		// Calculate posterior distribution for each class i: p(C_i | X) = p(X | C_i)[likelihood] * p(C_i)[class prior] / p(X)[normalisation factor]"
 +"\n"
 +"		Object[][] pCiX = new Object[2][classes.size()];"
 +"\n"
 +"		pCiX[1] = classes.keySet().toArray();"
 +"\n"
 +"		for(int i = 0; i < classes.size(); i++) { "
 +"\n"
 +"			if(aufruf == 1) {"
 +"\n"
 +"				pCiX[0][i] = (((double) likelihood[0][i]) * ((double) pCi[0][i]));   //   / pX;"
 +"\n"
 +"			}else {"
 +"\n"
 +"				pCiX[0][i] = (((double) likelihood[0][i]) * ((double) pCi[0][i])) / pX;"
 +"\n"
 +"			}"
 +"\n"
 +"		}"
 +"\n"
 +"		"
 +"\n"
 +"		// Find index of class with highest posterior distribution"
 +"\n"
 +"		double max = 0;"
 +"\n"
 +"		int ind = 0;"
 +"\n"
 +"		for(int i = 0; i < classes.size(); i++) {"
 +"\n"
 +"			if((double) pCiX[0][i] > max) {"
 +"\n"
 +"				max = (double) pCiX[0][i];"
 +"\n"
 +"				ind = i;"
 +"\n"
 +"			}"
 +"\n"
 +"		}"
 +"\n"
 +"		"
 +"\n"
 +"		for(int i = 0; i < classes.size(); i++) {"
 +"\n"
 +"			System.out.println(pCiX[1][i] + \" : \" + pCiX[0][i]);"
 +"\n"
 +"		}"
 +"\n"
 +"		"
 +"\n"
 +"		"
 +"\n"
 +"		System.out.println(\"Winner: \" + pCiX[1][ind]);"
 +"\n"
 +"		if(aufruf == 3) {"
 +"\n"
 +"			Ergebnis = pCiX[1][ind];"
 +"\n"
 +"			return 0.0;"
 +"\n"
 +"		}else {"
 +"\n"
 +"			return (double) pCiX[0][x];	"
 +"\n"
 +"		}"
 +"\n"
 +"	}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}

