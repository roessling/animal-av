package generators.misc;


import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.VogelApproxHelper;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class VogelApproximation implements Generator {
	
	
	static int[] angebot, nachfrage; // nimmt Angebots- bzw. Nachfragemengen auf
	static boolean[] markierteZeilen; // nimmt auf, welche Zeilen markiert sind
	static boolean[] markierteSpalten; // nimmt auf, welche Spalten markiert sind

	static int maxDsIndex_global, maxDzIndex_global,indexMinZeile, indexMinSpalte, transportmenge;
	

	public static String getCode(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) {
		int []dz_array;
		Vector<Integer> lsgIndexVector = new Vector<Integer>(2);
		
		
		Language lang = new AnimalScript("Vogelsche Approximationsmethode", "Petra Doersam, Kevin Tappe", 640, 480);

		
		// mehrere Schritte auf einmal moeglich
		lang.setStepMode(true);
		
		// Daten auslesen
		
		int [] a = (int[]) primitives.get("anbieter");	 // Anbieterarray
		int [] b = (int[]) primitives.get("nachfrager"); // Nachfragerarray
		 
		int [][] c = (int[][]) primitives.get("kosten"); // Kostenmatrix
	
		
		// Eigenschaften Text
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
				
		// Eigenschaften der Arrays (Anbieter und Nachfrager)
		ArrayProperties arrayProps = new ArrayProperties();
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    
	    // Nachfrage- und Angebots-Arrays fuer die Anzeige 
		// Array muss zur Darstellung in Matrix umgeschrieben werden
	    
	    // Nachfrage umschreiben fuer Darstellung
	    int [][] b_j_daten = new int [1][b.length] ;
		for (int i = 0; i < b.length; i++) {
				b_j_daten[0][i] = b[i];
		}
		// Angebot umschreiben fuer die Darstellung
		int [][] a_j_daten = new int [a.length][1] ;
			for (int j = 0; j < a.length; j++) {
				a_j_daten[j][0] = a[j];
		}
		
		// Durchnummerierung der Anbieter und Nachfrager
		int [][] b_j_anzahl = new int [1][b.length] ;
		for (int i = 0; i < b.length; i++) {
				b_j_anzahl[0][i] = i + 1;				
		}
		
		int [][] a_i_anzahl = new int [a.length][1] ;
		for (int i = 0; i < a.length; i++) {
			a_i_anzahl[i][0] = i + 1;
		}
		
		// Eigenschaften der Kostenmatrix (aehnlich css)
		MatrixProperties matrix_redhighlight_props = new MatrixProperties();
		
		matrix_redhighlight_props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		matrix_redhighlight_props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		matrix_redhighlight_props.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.FALSE);   
		matrix_redhighlight_props.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		matrix_redhighlight_props.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		
		MatrixProperties matrix_grayhighlight_props = new MatrixProperties();
		matrix_grayhighlight_props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE); 
		matrix_grayhighlight_props.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		matrix_grayhighlight_props.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GRAY);

		
		// Initialisierung der Matrix, die die Loesungen (= Transportmengenmatrix) beinhalten soll
		int[][] lsgMatrixWerte = new int[a.length][b.length];
		
		// Festlegung der Eigenschaften des Quell-Codes
		SourceCodeProperties scProps = new SourceCodeProperties();		 		 
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE); 					
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12)); // Schriftart & GrÂÂ§e
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED); // Farbe wenn hervorgehoben
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);		
		
		
		// Festlegung der Eigenschaften der Ueberschriften
		SourceCodeProperties scHeadProps = new SourceCodeProperties();		 		 
		scHeadProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16)); 	// Schriftart & Groesse
		
		// Festlegung der Eigenschaften des erklaerenden Textes
		SourceCodeProperties scNormalProps = new SourceCodeProperties();		 		 
		scNormalProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12)); 	// Schriftart & Groesse
				
		
		// Einleitungstext
		SourceCode einleitung1 = lang.newSourceCode(new Coordinates(30, 120), "einleitung1", null, scNormalProps);
		einleitung1.addCodeLine("Bei der Vogelschen Approximationsmethode handelt es sich um eine Heuristik zum "
								+ "Loesen des klassischen Transportproblems.", null, 0, null);
		einleitung1.addCodeLine("", null, 0, null);
		einleitung1.addCodeLine("Das klassische Transportproblem (TPP) ist ein lineares Optimierungsproblem:", null, 0, null);
		einleitung1.addCodeLine("Zwischem m Anbietern A_i (i = 1...m) und n Nachfragern B_j (j = 1...n) soll ein Gut transportiert werden.", null, 0, null);
		einleitung1.addCodeLine("Die Anbieter bieten die Angebotsmengen a = (a_1, ..., a_m) an und die Nachfrager moechten die Nachfragemengen b = (b_1, ..., b_m).", null, 0, null);
		einleitung1.addCodeLine("Durch Angabe einer Transportkostenmatrix C, die die Transportkosten pro Mengeneinheit des Gutes fuer jeden moeglichen Weg zwischen Anbietern und Nachfragern", null, 0, null);
		einleitung1.addCodeLine("wiedergibt, entsteht ein Optimierungsproblem: Es soll ein kostenminimaler Transportplan fuer die Konstellation aus Angebots- und Nachfragemengen gefunden werden.", null, 0, null);
		einleitung1.addCodeLine("Als Nebenbedingung gilt: Die Summe der Angebotsmengen muss gleich der Summe der Nachfragemengen sein.", null, 0, null);
		

	// ------------------------ nextStep()----------------------------------------	
		lang.nextStep();
		
		SourceCode weitereAlgo = lang.newSourceCode(new Coordinates(30, 350), "weitereAlgo", null, scNormalProps);
		weitereAlgo.addCodeLine("Die Vogelsche Approximationsmethode ist nur eine Heuristik unter vielen.", null, 0, null);
		weitereAlgo.addCodeLine("Weitere Heuristiken fuer das klassische Transportproblem sind beispielsweise", null, 0, null);
		weitereAlgo.addCodeLine("die Nordwest-Ecken-Regel und die Spaltenminimum-Methode.", null, 0, null);
		weitereAlgo.addCodeLine("Natuerlich gibt es auch Algorithmen, die stets eine optimale Loesung finden. Hier seien der", null, 0, null);
		weitereAlgo.addCodeLine("Simplex-Algorithmus (fuer alle linearen Optimierungsprobleme) sowie die MODI-Methode und die", null, 0, null);
		weitereAlgo.addCodeLine("Stepping-Stone-Methode genannt. Die beiden letztgenannten sind im Vergleich zum Simplex-Algorithmus", null, 0, null);
		weitereAlgo.addCodeLine("fuer das klassische TPP effizienter, da sie die spezielle Struktur der Problemstellung ausnutzen.", null, 0, null);
		
		
	// ------------------------ nextStep()----------------------------------------	
		lang.nextStep();
		
		einleitung1.hide();
		weitereAlgo.hide();
		
		SourceCode einleitung2 = lang.newSourceCode(new Coordinates(30, 220), "einleitung2", null, scNormalProps);
		einleitung2.addCodeLine("Oben finden sich die Angaben zur den Angebots- und Nachfragemengen,", null, 0, null);
		einleitung2.addCodeLine("sowie die Transportkosten. Diese Angaben finden sich auch in den", null, 0, null);
		einleitung2.addCodeLine("unten erscheinenden Matrizen, in welchen der Algorithmus visualisiert wird, wieder.", null, 0, null);
		
		
		// Initale Ausgabe der Daten
		// Alles in einem Schritt ausgeben -> Leider funktioniert dies nicht, da Animal schuld ist ;)
		// Angebots- & Nachfragemengenanzeige (oben links)
		lang.newText(new Coordinates(20, 50), "Angebotsmenge a = ", "angebotsText", null, tp);
		lang.newIntArray(new Coordinates(145,45),a, "angebot", null, arrayProps);	
		lang.newText(new Coordinates(20, 85), "Nachfragemente b = ", "nachfrageText", null, tp);
		lang.newIntArray(new Coordinates(145, 80), b, "nachfrage", null, arrayProps);
		Text textKosten = lang.newText(new Coordinates(400,65), "Transportkostenmatrix C = ", "kostenText", null, tp);
		IntMatrix initC= lang.newIntMatrix(new Offset(5,-20, textKosten, AnimalScript.DIRECTION_NE), c, "kosten_anzeige", null, matrix_redhighlight_props);

		
	// ------------------------ nextStep()----------------------------------------
		lang.nextStep();
		
		// linien fuer die Matrizen definieren 
	    String waagLinie[][] = new String [1][b.length+2];
	    
	    for (int count = 0; count < b.length + 2; count++){
	    	waagLinie[0][count] = "_";
	    }
	    
	    String senkLinie[][] = new String [a.length+2][1];
	    
	    for( int count=0; count < a.length + 2; count++){
	    	senkLinie[count][0] = "|";
	    }

		// Loesungsmatrix (= Transportmengenmatrix) mit "Rahmen" (unten links)
		IntMatrix a_i_anz = 
		lang.newIntMatrix(new Coordinates(120, 560), a_i_anzahl, "a_i_anz", null, matrix_grayhighlight_props);
	
		IntMatrix lsgMatrix =
		lang.newIntMatrix(new Offset(0, 3, a_i_anz, AnimalScript.DIRECTION_NE), lsgMatrixWerte, "lsgMatrix", null, matrix_redhighlight_props);
		
		IntMatrix b_j =
		lang.newIntMatrix(new Offset(0, 18, lsgMatrix, AnimalScript.DIRECTION_SW), b_j_daten, "b_j", null, matrix_redhighlight_props);
		
		IntMatrix a_i =
		lang.newIntMatrix(new Offset(0, 0, lsgMatrix, AnimalScript.DIRECTION_NE), a_j_daten, "a_i", null, matrix_redhighlight_props);
		
		// Tranportkostenmatrix mit Regretwerten (unten rechts)
		IntMatrix a_i_anz2 = lang.newIntMatrix(new Offset(200, -2, lsgMatrix, AnimalScript.DIRECTION_NE), a_i_anzahl, "a_i_anz2", null, matrix_grayhighlight_props);
		
		// Als String[][], damit gestrichene Elemente durch ein X ersetzt werden koennen
	    String c_String[][] = new String [c.length][c[0].length];
	    for(int count = 0; count < c.length; count++){
	    	for(int x = 0; x < c[0].length; x++){
	    		c_String[count][x] = (new Integer(c[count][x])).toString();;
	    	}
	    }
		
		StringMatrix kostenmatrix = lang.newStringMatrix(new Offset(0, 0, a_i_anz2, AnimalScript.DIRECTION_NE), c_String, "kostenmatrix", null, matrix_redhighlight_props);
		
		
		// Trennlinien fuer die Matrix rechts
		lang.newStringMatrix((new Offset(-10, -28, kostenmatrix, AnimalScript.DIRECTION_NE)), senkLinie, "senkLinieKMatrix", null, matrix_redhighlight_props);
		lang.newStringMatrix((new Offset(-10, -28, kostenmatrix, AnimalScript.DIRECTION_NW)), senkLinie, "senkLinieKMatrix2", null, matrix_redhighlight_props);
		StringMatrix waagrechteLinie = lang.newStringMatrix((new Offset(-28, -20, kostenmatrix, AnimalScript.DIRECTION_NW)), waagLinie, "waagLinieKMatrix", null, matrix_redhighlight_props);
		lang.newStringMatrix((new Offset(-28, -20, kostenmatrix, AnimalScript.DIRECTION_SW)), waagLinie, "waagLinieKMatrix2", null, matrix_redhighlight_props);
		
		
		IntMatrix b_j_anz2 =
			lang.newIntMatrix(new Offset(-30, -3, waagrechteLinie, AnimalScript.DIRECTION_N), b_j_anzahl, "b_j_anz", null, matrix_grayhighlight_props);
			
		
		// Beschriftung rechte Matrix
		lang.newText((new Offset(-80, -20, a_i_anz2, AnimalScript.DIRECTION_W)), "Anbieter A_i", "aText", null, tp);
		lang.newText((new Offset(-40, -20, b_j_anz2, AnimalScript.DIRECTION_N)), "Nachfrager B_j", "bText", null, tp);
		lang.newText((new Offset(-23, -15, waagrechteLinie, AnimalScript.DIRECTION_E)), "dz", "dzText", null, tp);
		lang.newText((new Offset(-30, 0, a_i_anz2, AnimalScript.DIRECTION_SE)), "ds", "dsText", null, tp);
		
		// Trennlinien linke Matrix
		lang.newStringMatrix((new Offset(-10, -28, lsgMatrix, AnimalScript.DIRECTION_NE)), senkLinie, "senkLinieLsgMatrix", null, matrix_redhighlight_props);
		lang.newStringMatrix((new Offset(-10, -28, lsgMatrix, AnimalScript.DIRECTION_NW)), senkLinie,"senkLinieLsgMatrix2", null, matrix_redhighlight_props);
		StringMatrix waagrechteLinieMlinks=lang.newStringMatrix((new Offset(-28, -20, lsgMatrix, AnimalScript.DIRECTION_NW)), waagLinie, "waagLinieLsgMatrixlinks", null, matrix_redhighlight_props);
		lang.newStringMatrix((new Offset(-28, -20, lsgMatrix, AnimalScript.DIRECTION_SW)), waagLinie, "waagLinieLsgMatrix2", null, matrix_redhighlight_props);
	
		// Beschriftung linke Matrix
		IntMatrix b_j_anz =
			lang.newIntMatrix(new Offset(-30, -3, waagrechteLinieMlinks, AnimalScript.DIRECTION_N), b_j_anzahl, "b_j_anz", null, matrix_grayhighlight_props);
		
		lang.newText((new Offset(-80, -20, a_i_anz, AnimalScript.DIRECTION_W)), "Anbieter A_i", "aText_Lsg", null, tp);
		lang.newText((new Offset(-40, -20, b_j_anz, AnimalScript.DIRECTION_N)), "Nachfrager B_j", "bText_Lsg", null, tp);
		
		lang.newText((new Offset(-28, -18, waagrechteLinieMlinks, AnimalScript.DIRECTION_E)), "Rest", "dzText", null, tp);
		lang.newText((new Offset(-35, 5, a_i_anz, AnimalScript.DIRECTION_SE)), "Rest", "dsText", null, tp);
		
		// Ueberschriften zur Transportmengen- und Transportkostenmatrix
		lang.newText((new Offset(-90, -65, kostenmatrix, AnimalScript.DIRECTION_N)), "Transportkosten / Regretwerte:", "ueberschrift_kostenmatrix", null, tp); 
		lang.newText((new Offset(-125, -65, lsgMatrix, AnimalScript.DIRECTION_N)), "realisierte und restliche Transportmengen:", "ueberschrift_transportmengen", null, tp);
		
		// Hinweistext zur Markierung (Streichung) von Eintraegen/Zeilen/Spalten
		lang.newText((new Offset(-60, 40, kostenmatrix, AnimalScript.DIRECTION_SW)), "X markiert einen gestrichenen Eintrag", "hinweis_gestrichen", null, tp);
		
		// umschreiben von ds/dz zur darstellung
		String [][] dz_matrix = new String [a.length][1] ;
		for (int x = 0; x < a.length; x++) {
			dz_matrix[x][0] = " ";
		}
		String [][] ds_matrix = new String [1][b.length] ;
		for (int x = 0; x < b.length; x++) {
			ds_matrix[0][x] = " ";
		}
		// Auch fÂr diesen Aufbau brauch er zwei Klicks
		StringMatrix dz_i = lang.newStringMatrix(new Offset(0, 0, kostenmatrix, AnimalScript.DIRECTION_NE), dz_matrix, "dz", null, matrix_redhighlight_props);
		StringMatrix ds_j = lang.newStringMatrix(new Offset(0, 0, kostenmatrix, AnimalScript.DIRECTION_SW), ds_matrix, "ds", null, matrix_redhighlight_props);

	// ------------------------ nextStep()----------------------------------------	
		lang.nextStep();
		einleitung2.hide();
		
		SourceCode einleitung3 = lang.newSourceCode(new Coordinates(30, 140), "einleitung3", null, scNormalProps);
		einleitung3.addCodeLine("Die Transportmengenmatrix unten links wird die realisierten Transportmengen schrittweise anzeigen.", null, 0, null);
		einleitung3.addCodeLine("Rechts von dieser Matrix werden die noch zu verteilenden restlichen Angebotsmengen angezeigt,", null, 0, null);
		einleitung3.addCodeLine("unterhalb die noch zu verteilenden Transportmengen.", null, 0, null);
		einleitung3.addCodeLine("", null, 0, null);
		einleitung3.addCodeLine("Die Transportkostenmatrix unten rechts enthaelt zunaechst die gleichen Werte wie die Matrix C oben rechts.", null, 0, null);
		einleitung3.addCodeLine("Waehrend der Ausfuehrung des Algorithmus werden jedoch Zeilen und Spalten dieser Matrix gestrichen. ", null, 0, null);
		einleitung3.addCodeLine("Rechts von dieser Matrix werden die Zeilenregretwerte (dz) angezeigt,", null, 0, null);
		einleitung3.addCodeLine("unterhalb die Spaltenregretwerte (ds).", null, 0, null);
		einleitung3.addCodeLine("", null, 0, null);
		einleitung3.addCodeLine("", null, 0, null);
		einleitung3.addCodeLine("Die Vogelsche Approximationsmethode baut auf dem Konzept des maximalen Regretwertes (= 'groesstes Bedauern') auf:", null, 0, null);
		einleitung3.addCodeLine("Die Transportwege werden schrittweise ausgewaehlt und mit Transportmengen bestueckt. Dies geschieht bei jeder Iteration so, dass zuerst anhand des maximalen", null, 0, null);
		einleitung3.addCodeLine("Regretwertes ein Anbieter oder Nachfrager ausgewaehlt wird. Fuer diesen Anbieter bzw. Nachfrager wird dann ein Weg mit minimalen Transportkosten bestimmt und", null, 0, null);
		einleitung3.addCodeLine("mit der maximal moeglichen Transportmenge realisiert. So werden zukuenftige hoehere Transportkosten umgangen.", null, 0, null);

		
	// ------------------------ nextStep()----------------------------------------	
		lang.nextStep();
		einleitung3.hide();
		
		SourceCode einleitung4 = lang.newSourceCode(new Coordinates(30, 140), "einleitung4", null, scNormalProps);
		einleitung4.addCodeLine("", null, 0, null);
		einleitung4.addCodeLine("Die Vogelsche Approximationsmethode besteht aus zwei Teilen:", null, 0, null); 
		einleitung4.addCodeLine("", null, 0, null); 
		einleitung4.addCodeLine("* Im ersten Teil des Algorithmus werden Transportmengen solange ", null, 1, null);
		einleitung4.addCodeLine("  nach dem Prinzip des maximalen Regrets verteilt, bis entweder ", null, 1, null);
		einleitung4.addCodeLine("  m-1 Zeilen oder n-1 Spalten der Transportmengenmatrix belegt (= gestrichen) sind.", null, 1, null);
		einleitung4.addCodeLine("", null, 1, null);
		einleitung4.addCodeLine("* Im zweiten Teil des Algorithmus werden die noch verbleibenden ", null, 1, null);
		einleitung4.addCodeLine("  Transportmengen verteilt.", null, 1, null);
		einleitung4.addCodeLine("", null, 0, null);
		einleitung4.addCodeLine("(mit m = Anzahl Zeilen = Anzahl Anbieter, n = Anzahl Spalten = Anzahl Nachfrager)", null, 0, null);

	// ------------------------ nextStep()----------------------------------------	
		lang.nextStep();
		einleitung4.hide();
		
		
		// Source Code des 1. Teil des Algorithmus
		

		// Ueberschrift ueber den ersten Teil des Algorithmus
		SourceCode scHead = lang.newSourceCode(new Coordinates(30, 140), "ueberschrift_ersterTeil", null, scHeadProps);
		scHead.addCodeLine("Erster Teil des Algorithmus (Verteilen nach maximalem Regret):", null, 0, null);
	

		
		// Quellcode des ersten Teils des Algorithmus
		SourceCode sc = lang.newSourceCode(new Coordinates(30, 190), "sourceCodeTeil1", null, scProps);
		
				sc.addCodeLine("Schleife: Solange (noch nicht m-1 Zeilen gestrichen) ODER (noch nicht n-1 Spalten gestrichen)) {", null, 0, null); // 0
				sc.addCodeLine("Fuer jede ungestrichen Zeile i in der Transportkostenmatrix:", null, 1, null); // 1
				sc.addCodeLine("* Bestimme die zwei kleinsten Werte und berechne deren Differenz (= Regretwert dz)", null, 2, null);  // 2
				sc.addCodeLine("Fuer jede ungestrichene Spalte j in der Transportkostenmatrix:", null, 1, null);  // 3
				sc.addCodeLine("* Bestimme die zwei kleinsten Werte und berechne deren Differenz (= Regretwert ds);", null, 2, null);  // 4
				sc.addCodeLine("Waehle die Zeile oder Spalte mit dem maximalen Regretwert, die nicht gestrichen ist", null, 1, null);  // 5
				sc.addCodeLine("Bestimme die guenstigste Verbindung in dieser Zeile/Spalte, d.h. die minimalen Transportkosten", null, 1, null); // 6
				sc.addCodeLine("Realisiere die Transportmenge fuer diese minimalen Transportkosten:", null, 1, null); // 7
				sc.addCodeLine("* Transportmenge := Minimum(Angebot[i], Nachfrage[j])", null, 2, null); // 8
				sc.addCodeLine("* Angebot[i] := Angebot[i] - Tranportmenge", null, 2, null); // 9
				sc.addCodeLine("* Nachfrage[j] := Nachfrage[j] - Tranportmenge", null, 2, null); // 10
				sc.addCodeLine("Falls Angebot[i] = 0 => streiche i-te Zeile", null, 1, null); // 11
				sc.addCodeLine("ansonsten gilt Nachfrage[j] = 0 => streiche j-te Spalte", null, 2, null); // 12
				sc.addCodeLine("} // Schleifenabbruchbedingung erfuellt (m-1 Zeilen ODER n-1 Spalten gestrichen) -> Ende der Schleife", null, 0, null); // 13
				
				
		//--> Eigentliche Animation beginnt hier
				
				// Definition der folgenden Variablen
				int i = a.length; //# Zeilen = # Anbieter
				int j = b.length; //# Spalten = # Nachfrager
				
				// Markierungsmatrix Initialisierung mit Zugriff auf die VA-Klasse
				// erstellung der Markierungsmatizen
				markierteZeilen = new boolean[i];
				markierteSpalten = new boolean[j];
		
				// Initialisiere Markierung
				initMarkierung(markierteZeilen);
				initMarkierung(markierteSpalten);
		
				// Angebot = a; Nachfrage= b
				angebot = new int[i];
				nachfrage = new int[j];
				angebot = a;
				nachfrage = b;

	// ------------------------ nextStep()----------------------------------------
				lang.nextStep();				
				
				sc.highlight(0);
				
				// Grosse WHILE-Schleife der Vogelschen Approximationsmethode (= Abbruchbedingung 1. Teil)
				while ((zaehleMarkierungen(markierteZeilen) < i - 1) 
					&& (zaehleMarkierungen(markierteSpalten) < j - 1)) {

					// Berechne alle Regretwerte
					Vector<int[]> regretVektoren;
					regretVektoren = new Vector<int[]>(2);
					
					regretVektoren = berechneRegretwerte(c);
						
					// erste inintialierung
					int[] indexKleinstesEle = {0,0};
					int prev_z = 0;

	// ------------------------ nextStep()----------------------------------------
					lang.nextStep();					
					
					sc.unhighlight(0);
					sc.highlight(1); // Durchgehen der Zeilen
					sc.highlight(2);

	// ------------------------ nextStep()----------------------------------------
					lang.nextStep();						
					
					// ZEILEN
					for (int z = 0; z < i; z++) {
						if (!markierteZeilen[z]) { // nur unmarkierte

						// unmarkieren
						kostenmatrix.unhighlightCell(prev_z, indexKleinstesEle[0], null, null);
						kostenmatrix.unhighlightCell(prev_z, indexKleinstesEle[1], null, null);	
						
						// finde kleinste Elemente
						indexKleinstesEle = findeIndexKleinsteEle(c[z], markierteSpalten);

						// Markiere kleinste Elemente
						kostenmatrix.highlightCell(z, indexKleinstesEle[0], null, null);
						kostenmatrix.highlightCell(z, indexKleinstesEle[1], null, null);
						prev_z = z;
					
	// ------------------------ nextStep()----------------------------------------					
						lang.nextStep();
						
						dz_array = regretVektoren.get(0);
						String [] dz_String = new String[dz_array.length]; 
						
						for (int k = 0; k < dz_array.length; k++){
							dz_String[k] = (new Integer(dz_array[k])).toString();
						}
							//Regretwerte ersetzten
							dz_i.put(z, 0, dz_String[z], null, null);
							dz_i.highlightCell(z, 0, null, null);

	// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
						
							dz_i.unhighlightCell(z, 0, null, null);	
						}
					}
					sc.unhighlight(1);
					sc.unhighlight(2);
					kostenmatrix.unhighlightCell(prev_z, indexKleinstesEle[0], null, null);
					kostenmatrix.unhighlightCell(prev_z, indexKleinstesEle[1], null, null);	
					
					// SPALTEN
					int[] indexKleinstesEleSp = {0,0};
					int prev_s = 0;
					int[] spalten_array = new int[a.length];
									
					sc.highlight(3); // Durchgehen der Spalten, Berechung Regretwerte
					sc.highlight(4);

	// ------------------------ nextStep()----------------------------------------
					lang.nextStep();	
					
					for (int s = 0; s < j; s++) {
						if (!markierteSpalten[s]) {
						
							// unmarkieren
							kostenmatrix.unhighlightCell(indexKleinstesEleSp[0], prev_s,  null, null);
							kostenmatrix.unhighlightCell(indexKleinstesEleSp[1], prev_s,  null, null);	
							
							// finde kleinste Ele
							// Spalte in Array Schreiben (super unschoen)
						
							for (int k = 0; k < a.length; k++){
								spalten_array[k] = c[k][s];
							}
		
							indexKleinstesEleSp = findeIndexKleinsteEle(spalten_array, markierteZeilen);
							
							kostenmatrix.highlightCell(indexKleinstesEleSp[0], s, null, null);
							kostenmatrix.highlightCell(indexKleinstesEleSp[1], s, null, null);
							prev_s = s;
	
		// ------------------------ nextStep()----------------------------------------								
							lang.nextStep();
							
							// umschreiben als String
							int[]ds_array = regretVektoren.get(1);
							String [] ds_String = new String[ds_array.length]; 
							
							for (int k = 0; k < ds_array.length; k++){
								ds_String[k] = (new Integer(ds_array[k])).toString();
							}
							
							// Regretwerte ersetzten und hervorrufen
							ds_j.put(0, s, ds_String[s], null, null);
							ds_j.highlightCell(0, s, null, null);
							
		// ------------------------ nextStep()----------------------------------------					
							lang.nextStep();
						
							ds_j.unhighlightCell(0, s, null, null);
						}
					}
					sc.unhighlight(3);
					sc.unhighlight(4);
					kostenmatrix.unhighlightCell(indexKleinstesEleSp[0], prev_s, null, null);
					kostenmatrix.unhighlightCell(indexKleinstesEleSp[1], prev_s, null, null);	
					
			
					int[] maxRegret;
					maxRegret = berechneMaxRegret(regretVektoren);
					
					// Berechnung der Transportmenge sowie Rest der Angebots- und Nachfragemenge. Wird in die globalen Variablen geschrieben
					realisiere(c, maxRegret);
					
					int sMaxReg, zMaxReg; // Index der Spalte/Zeile mit groessten Regretwert
					
					// ist groesster Regretwert in Spalte oder Zeile? -> unterschiedliche markierungen
					if (maxRegret[1] == 0) {
						// markierung des groessten Regretwertes in reg-spalte
						zMaxReg = maxDzIndex_global;
						sMaxReg = indexMinZeile;
					
						sc.toggleHighlight(4, 5); // Markierung des groessten Regretwerts in der dz-Zeile

	// ------------------------ nextStep()----------------------------------------						
						lang.nextStep();
						
						dz_i.highlightCell(zMaxReg, 0, null, null);
						
					} // in Spalte
					else {
						sMaxReg = maxDsIndex_global;
						zMaxReg = indexMinSpalte;
						
						sc.toggleHighlight(4, 5); // Markierung des groessten Regretwerts in der ds Spalte

	// ------------------------ nextStep()----------------------------------------	
						lang.nextStep();
						
						ds_j.highlightCell(0, sMaxReg, null, null);
					}

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					
					sc.toggleHighlight(5, 6);
					
	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();					
					
					kostenmatrix.highlightCell(zMaxReg, sMaxReg, null, null); // Markierung der geringsten Transportkosten in Zeile
									
					lsgMatrix.highlightCell(zMaxReg, sMaxReg, null, null);
					//lsgMatrix.highlightCell(zMaxReg, sMaxReg, null, null);
					
					
					lsgIndexVector.add(0, zMaxReg);
					lsgIndexVector.add(1, sMaxReg);
					
	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();

					ds_j.unhighlightCell(0, sMaxReg, null, null);
					dz_i.unhighlightCell(zMaxReg, 0, null, null);
					
					sc.unhighlight(6); // Minimale Transportmenge Nachfrager/Anbieter
					sc.highlight(7);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					
					sc.highlight(8);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					
					b_j.highlightCell(0, sMaxReg, null, null);
					a_i.highlightCell(zMaxReg, 0, null, null);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					
					//  Transportmenge in Matrix realisieren
					lsgMatrix.put(zMaxReg, sMaxReg, transportmenge, null, null);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					
					sc.toggleHighlight(8, 9); // Angebotmenge um transportierte Menge reduzieren
					b_j.unhighlightCell(0, sMaxReg, null, null);
	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();

					// lsgMatrix.unhighlightCell(zMaxReg,sMaxReg, null, null);
				
					a_i.put(zMaxReg, 0, angebot[zMaxReg], null, null);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					
					sc.toggleHighlight(9, 10);  // Nachfrage um transportierte Menge reduzieren
					
					a_i.unhighlightCell(zMaxReg, 0, null, null);
					b_j.highlightCell(0, sMaxReg, null, null);
	
	// ------------------------ nextStep()----------------------------------------
					lang.nextStep();				
				
					b_j.put(0, sMaxReg, nachfrage[sMaxReg], null, null);
					
	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();

					b_j.unhighlightCell(0, sMaxReg, null, null);
					lsgMatrix.unhighlightCell(zMaxReg, sMaxReg, null, null);
					
					sc.unhighlight(7);
					sc.unhighlight(10);
					sc.highlight(11);
					a_i.highlightCell(zMaxReg, 0, null, null);
	
	// ------------------------ nextStep()----------------------------------------						
					lang.nextStep();
					
					// MARKIERUNG (Streichen) der Anbieter und Nachfrager
					// Streichen der Zeile/Spalte, welche gleich Null ist
	
					if (markierteZeilen[zMaxReg]) { // Markieren der Zeilen	
						a_i_anz2.highlightCell(zMaxReg, 0, null, null);	
						a_i_anz.highlightCell(zMaxReg, 0, null, null);	
						dz_i.put(zMaxReg, 0, "X", null, null);
						
						kostenmatrix.highlightCellColumnRange(zMaxReg, kostenmatrix.getNrCols()-1, 0, null, null);
					
						for (int q = 0; q < kostenmatrix.getNrCols(); q++) {
							kostenmatrix.put(zMaxReg, q, "X", null, null);
						}
						
	// ------------------------ nextStep()----------------------------------------					
						lang.nextStep();
						
						a_i_anz2.unhighlightCell(zMaxReg,0, null, null);	
						a_i_anz.unhighlightCell(zMaxReg, 0, null, null);	
						kostenmatrix.unhighlightCellColumnRange(zMaxReg, kostenmatrix.getNrCols()-1, 0, null, null);
						sc.unhighlight(11);
						a_i.unhighlightCell(zMaxReg, 0, null, null);
						
					} else { 

						if (markierteSpalten[sMaxReg]) { // Markieren der Spalten
							sc.unhighlight(11);
							a_i.unhighlightCell(zMaxReg, 0, null, null);
							b_j.highlightCell(0, sMaxReg, null, null);
							sc.highlight(12);
							
							b_j_anz.highlightCell(0, sMaxReg, null, null);

	// ------------------------ nextStep()----------------------------------------					
							lang.nextStep();
							a_i.unhighlightCell(zMaxReg, 0, null, null);
							b_j_anz2.highlightCell(0, sMaxReg, null, null);	
							b_j_anz.highlightCell(0, sMaxReg, null, null);
							ds_j.put(0, sMaxReg, "X", null, null);
							
							kostenmatrix.highlightCellRowRange(0, kostenmatrix.getNrRows()-1, sMaxReg, null, null);
							for (int q = 0; q < kostenmatrix.getNrRows(); q++) {
								kostenmatrix.put(q, sMaxReg, "X", null, null);
							}
						}
					
	// ------------------------ nextStep()----------------------------------------
						lang.nextStep();
	
						a_i.unhighlightCell(zMaxReg, 0, null, null);
						b_j.unhighlightCell(0, sMaxReg, null, null);
						b_j_anz2.unhighlightCell(0, sMaxReg, null, null);	
						b_j_anz.unhighlightCell(0, sMaxReg, null, null);
						kostenmatrix.unhighlightCellRowRange(0, kostenmatrix.getNrRows()-1, sMaxReg, null, null);
						sc.unhighlight(12);
					}
					
					sc.highlight(0);
					b_j.unhighlightCell(0, sMaxReg, null, null);
					a_i.unhighlightCell(zMaxReg, 0, null, null);
					kostenmatrix.unhighlightCell(zMaxReg, sMaxReg, null, null);
					lsgMatrix.unhighlightCell(zMaxReg, sMaxReg, null, null);					
				
				} // Grosse while-Schleife (= Abbruch 1. Teil der Vogelschen Approximationsmethode)

	// ------------------------ nextStep()----------------------------------------					
				lang.nextStep();
				
				sc.unhighlight(0);
				sc.highlight(13);
				

// **************************** Verteilung der Restmengen (2. Teil der Vogelschen Approximationsmethode) *****************************

	// ------------------------ nextStep()----------------------------------------
				lang.nextStep();			
				
				scHead.hide();
				sc.hide();
			
				// Ueberschrift zweiter Teil des Algorithmus
				SourceCode scHead2 = lang.newSourceCode(new Coordinates(30, 140), "ueberschrift_zweiterTeil", null, scHeadProps);
				scHead2.addCodeLine("Zweiter Teil des Algorithmus (Verteilen der Restmengen):", null, 0, null);
			
				// Quellcode zum zweiten Teil des Algorithmus
				SourceCode scRest = lang.newSourceCode(new Coordinates(30, 190), "sourceCodeTeil2", null, scProps);
				scRest.addCodeLine("Fuer alle nun noch ungestrichenen Elemente [i,j] der Transportkostenmatrix:", null, 0, null); // 0
				scRest.addCodeLine("* Realisiere die moegliche Transportmenge:", null, 1, null); // 1
				scRest.addCodeLine("* Transportmenge := Minimum(Angebot[i], Nachfrage[j])", null, 2, null);  // 2
				scRest.addCodeLine("* Angebot[i] := Angebot[i] - Tranportmenge", null, 2, null);  // 3
				scRest.addCodeLine("* Nachfrage[j] := Nachfrage[j] - Tranportmenge", null, 2, null);  // 4
				scRest.addCodeLine("* Falls Angebot[i] = 0 => streiche i-te Zeile", null, 2, null);  // 5
				scRest.addCodeLine("  ansonsten gilt Nachfrage[j] = 0 => streiche j-te Spalte", null, 3, null);  // 6
				scRest.addCodeLine("Alle Elemente gestrichen, d.h. alle Transportmengen verteilt -> Ende", null, 0, null);  // 7

	// ------------------------ nextStep()----------------------------------------				
				lang.nextStep();
		
				for (int z = 0; z < a.length; z++) {
					for (int s = 0; s < b.length; s++) {	

						if (!markierteZeilen[z] && !markierteSpalten[s]) {
							scRest.highlight(0);
							
	// ------------------------ nextStep()----------------------------------------				
							lang.nextStep();

							lsgMatrix.highlightCell(z, s, null, null);
							kostenmatrix.highlightCell(z, s, null, null);

	// ------------------------ nextStep()----------------------------------------				
							lang.nextStep();						
							
							scRest.toggleHighlight(0, 1);
	
	// ------------------------ nextStep()----------------------------------------		
							lang.nextStep();
							
							scRest.highlight(2);
							
	// ------------------------ nextStep()----------------------------------------		
							lang.nextStep();						
							
							a_i.highlightCell(z, 0, null, null);
							b_j.highlightCell(0, s, null, null);
							
							int transportmenge = 0;
							int angebot = a_i.getElement(z, 0);
							int nachfrage = b_j.getElement(0, s);
							
							// Bestimmung der Transportmenge
							if (angebot <= nachfrage) {
								transportmenge = angebot;
								angebot = 0 ;
								nachfrage = nachfrage - transportmenge;
								markierteZeilen[z] = true; // markiere Zeile	
							} else {
								transportmenge = nachfrage;
								angebot = angebot- transportmenge;
								nachfrage = 0 ;
								markierteSpalten[s] = true; // markiere Spalte
							}
							
							lang.nextStep();
							lsgMatrix.highlightCell(z, s, null, null);
							lsgMatrix.put(z, s, transportmenge, null, null); // Eintragung der Transportmenge
							
							// Fuellen der IndexVektoren
							lsgIndexVector.add(0, z);
							lsgIndexVector.add(1, s);
							
		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
							
							scRest.toggleHighlight(2, 3);
							
		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
							
							b_j.unhighlightCell(0, s, null, null);
							a_i.put(z, 0, angebot, null, null); // Angebot um transportierte Menge reduzieren
					
		// ------------------------ nextStep()----------------------------------------				
							lang.nextStep();
							
							scRest.toggleHighlight(3, 4);
							
		// ------------------------ nextStep()----------------------------------------				
							lang.nextStep();				
							
							b_j.highlightCell(0, s, null, null);
							a_i.unhighlightCell(z, 0,  null, null);

		// ------------------------ nextStep()----------------------------------------	
							lang.nextStep();	
							
							b_j.put(0, s, nachfrage, null, null); // Nachfrage um transportierte Menge reduzieren

		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
		
							scRest.unhighlight(4);
							scRest.highlight(5);
							a_i.highlightCell(z, 0,  null, null);
							b_j.unhighlightCell(0, s, null, null);
							lsgMatrix.unhighlightCell(z, s, null, null);

		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
									
							// Markiere Spalte bzw. Zeile bei der der Wert 0 wird
							if (angebot == 0) {
								a_i_anz2.highlightCell(z, 0, null, null);	
								a_i_anz.highlightCell(z, 0, null, null);

		// ------------------------ nextStep()----------------------------------------							
								lang.nextStep();
								dz_i.put(z, 0, "X", null, null);
								kostenmatrix.highlightCellColumnRange(z, kostenmatrix.getNrCols()-1, 0, null, null);
								
								for (int q = 0; q < kostenmatrix.getNrCols(); q++) {
									kostenmatrix.put(z, q, "X", null, null);
								}
								
							} else if (nachfrage == 0) {
								a_i.unhighlightCell(z, 0, null, null);
								b_j.highlightCell(0, s, null, null);
								scRest.unhighlight(5);
								scRest.highlight(6);
								b_j_anz2.highlightCell(0, s, null, null);	
								b_j_anz.highlightCell(0, s, null, null);

		// ------------------------ nextStep()----------------------------------------							
								lang.nextStep();
					
								ds_j.put(0, s, "X", null, null);
								kostenmatrix.highlightCellRowRange(0, kostenmatrix.getNrRows()-1, s, null, null);
								
								for (int q = 0; q < kostenmatrix.getNrRows(); q++) {
									kostenmatrix.put(q, s, "X", null, null);
								}
							}
		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
							
							a_i.unhighlightCell(z, 0, null, null);
							b_j.unhighlightCell(0, s, null, null);
							scRest.unhighlight(5);
							scRest.unhighlight(6);
							a_i_anz2.unhighlightCell(z, 0, null, null);	
							a_i_anz.unhighlightCell(z, 0, null, null);
							b_j_anz2.unhighlightCell(0, s, null, null);	
							b_j_anz.unhighlightCell(0, s, null, null);
							kostenmatrix.unhighlightCellColumnRange(z, kostenmatrix.getNrCols()-1, 0, null, null);
							kostenmatrix.unhighlightCellRowRange(0, kostenmatrix.getNrRows()-1, s, null, null);
							scRest.unhighlight(5);
							scRest.unhighlight(1);
							lsgMatrix.unhighlightCell(z, 0,  null, null);
							kostenmatrix.unhighlightCell(z, s, null, null);

						} 						
					}	
				} // Alle Zeilen und Spalten durchlaufen -> keine Restmengen mehr zu verteilen -> Ende
				
				scRest.highlight(0);

		// ------------------------ nextStep()----------------------------------------
				lang.nextStep();
		
				scRest.unhighlight(0);
				scRest.highlight(7);
				
				// Unmarkiere die gesamten Matrizen und Streichung ALLER dz/ds
			    for (int count = 0; count < c.length; count++){
			    	dz_i.put(count, 0, "X", null, null);
			    	
			    	for (int x = 0; x < c[0].length; x++){
			    		kostenmatrix.unhighlightCell(count, x, null, null);
			    		lsgMatrix.unhighlightCell(count, x, null, null);
			    		ds_j.put(0, x, "X", null, null);
			    	}
			    }
				

	// **************************** Ausgabe des Fkt-Wertes und dessen Zusammensetzung**********************************

		// ------------------------ nextStep()----------------------------------------
				lang.nextStep();
			
				scHead2.hide();
				scRest.hide();
				
				SourceCode scHead3 = lang.newSourceCode(new Coordinates(30, 140), "ueberschrift_FktWert", null, scHeadProps);
				scHead3.addCodeLine("Transportkosten", null, 0, null);

				SourceCode scFktWert = lang.newSourceCode(new Coordinates(30, 190), "Funktionswert", null, scProps);
				scFktWert.addCodeLine("Nachdem die Transportmengen verteilt wurden, koennen die Transportkosten einfach", null, 0, null);
				scFktWert.addCodeLine("berechnet werden:", null, 0, null);
				scFktWert.addCodeLine("", null, 0, null);
				scFktWert.addCodeLine("Gesamte Transportkosten = Transportkosten c_ij * Transportmenge zwischen A_i und B_j", null, 0, null);
				scFktWert.addCodeLine("[fuer alle i = 1...m, j = 1...n]", null, 0, null); 
				
				
				TextProperties fktProp = new TextProperties();
				fktProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.RED);
				fktProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 16)); 
				
				String FktRechnung =" ";
				Integer erg_int = 0;
				Text FktWert_text = lang.newText(new Offset(-200, 150, scFktWert, AnimalScript.DIRECTION_N), "Gesamte Transportkosten = ", "funktionswert", null, tp);
				Text FktRechnung_anzeige = lang.newText(new Offset(35, 0, FktWert_text, AnimalScript.DIRECTION_NE), FktRechnung, "rechnung", null, tp);
				Text FktWert_ergebnis = lang.newText(new Offset(5, 0, FktWert_text, AnimalScript.DIRECTION_NE), FktRechnung, "ergebnis", null, fktProp);

				FktWert_ergebnis.setText(erg_int.toString(), null, null);

		// ------------------------ nextStep()----------------------------------------
				lang.nextStep();
				
				Enumeration<Integer> e = lsgIndexVector.elements ();
				boolean first = true;
				while (e.hasMoreElements ()) {
				
					Integer z = (Integer) e.nextElement();
					Integer s = (Integer) e.nextElement();

					lsgMatrix.highlightCell(z, s, null, null);
					
					Integer kosten = c[z][s];
					Integer menge = lsgMatrix.getElement(z, s);
				
					kostenmatrix.put(z, s, kosten.toString(), null, null);	
					kostenmatrix.highlightCell(z, s, null, null);
					initC.highlightCell(z, s, null, null);
					
					erg_int = erg_int + kosten * lsgMatrix.getElement(z, s) ;
					
					if (first) {
						FktRechnung += " = " + kosten.toString() + " x " + menge.toString();
					} else {
						FktRechnung += " + " + kosten.toString() + " x " + menge.toString();
					}
					
					FktRechnung_anzeige.setText(FktRechnung, null, null);
					
					FktWert_ergebnis.setText(erg_int.toString(), null, null);
					first = false;
					
		// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					
					kostenmatrix.unhighlightCell(z, s, null, null);
					initC.unhighlightCell(z, s, null, null);
					lsgMatrix.unhighlightCell(z, s, null, null);
			}
		// ------------------------ nextStep()----------------------------------------					
				lang.nextStep();
				
				SourceCode scHinweis = lang.newSourceCode(new Coordinates(30, 400), "Hinweis", null, scProps);
				scHinweis.addCodeLine("Hinweis: Bei den errechneten Transportkosten handelt es sich nicht unbedingt um die minimal moeglichen", null, 0, null);
				scHinweis.addCodeLine("Transportkosten fuer diese Instanz des Problems, da zur Loesung eine Heuristik herangezogen wurde!", null, 0, null);
				
	    return lang.toString();
	}
	
// ---------------------------- Logik der Vogelschen Approximation (eingesetzte Hilfsfunktionen) -------------------	
	
	/**
	 * Findet in einem int-Array die zwei kleinsten Elemente
	 * @param liste - int-Array in welchem die zwei kleinsten Werte gesucht werden sollen
	 * @param markListe - boolean-Array welches angibt, welche Werte schon markiert, d. h. irrelevant sind
	 * @return - int-Array mit Position 0: Index des kleinsten Elements, Position 1: zweitkleinsten Elements
	 */
	
	public static int[] findeIndexKleinsteEle(int[] liste, boolean[] markListe) {
		
		
		// Damit das Bestimmen des Indices des kleinsten und zweitkleinsten moeglichst einfach wird,
		// werden Wert und zugehoerige Markierung in einem Verbund (Record, "Objekt") namens VogelApprox_Helper
		// zusammengefasst und so gemeinsam sortiert.
		VogelApproxHelper[] kopieListe;
		kopieListe = new VogelApproxHelper[liste.length];
		
		for (int i = 0; i < liste.length; i++) {
			kopieListe[i] = new VogelApproxHelper(liste[i], i, markListe[i]);
		}
		
		Arrays.sort(kopieListe); // Durch Sortierung laesst sich leicht das kleinste und zweikleinste Element bestimmen
		
		int indexMin = -1; // Index des kleinsten unmarkierten Elements
		int indexMin2 = -1; // Index des zweikleinsten unmarkierten Elements
		int indexMinSort = -1; // benoetigt zur Bestimmung des zweitkleinsten Elements
		
		// Finde kleinstes unmarkiertes Element
		for (int i = 0; i < kopieListe.length; i++) {
			if (!kopieListe[i].markiert) {
				indexMin = kopieListe[i].index;
				indexMinSort = i;
				break; // kleinstes Element gefunden, Schleife kann abgebrochen werden
			}
		}
		
		// Finde zweitkleinstes unmarkiertes Element
		for (int i = indexMinSort+1; i < kopieListe.length; i++) {
			if (!kopieListe[i].markiert) {
				indexMin2 = kopieListe[i].index;
				break; // zweikl. Element gefunden, Schleife kann abgebrochen werden
			}
		}
		
		int[] indexMinFeld;
		indexMinFeld = new int[2];
		indexMinFeld[0] = indexMin;
		indexMinFeld[1] = indexMin2;
		
		return indexMinFeld;
	}
	
	/**
	 * Die Methode brechnet die maximalen Regretwerte aus den Differenzen der kleinsten Elemente
	 * einer jeden Zeile bzw. Spalte. Markierungen (d. h. gestrichene [=nicht mehr zu betrachtende])
	 * Zeilen bzw. Spalten werden dabei beruecksichtigt.
	 * 
	 * @param feld - Kostenmatrix aus der die Regretwerte berechnet werden sollen
	 * @return Vector<int[2]> - An Position 0 liegt das Int-Array mit den Regretweren der Zeilenvektoren, an Position 1 das Int-Array mit den Regretwerten der Spaltenvektoren
	 */
	public static Vector<int[]> berechneRegretwerte (int[][] feld) {
		
		int[] dz, ds; // Regretwerte Zeilen (dz) und Regretwerte Spalten (ds)
		dz = new int[feld.length];
		ds = new int[feld[0].length];
		
		int[] indexKleinsteEle; // Array nimmt die Indices des kleinsten (Index 0) und zweikleinsten (Index 1) Wertes auf
		int minWert, minWert2; // Nimmen die Werte des kleinsten (minWert) bzw. zweitkleinsten (minWert2) Elements auf.
		
		int[] zeilenvektor; // nimmt eine Zeile der Kostenmatrix auf
		zeilenvektor = new int[feld[0].length];
		
		int [] spaltenvektor; // nimmt eine Spalte der Kostenmatrix auf
		spaltenvektor = new int[feld.length];
		
		// Berechnung Regretwerte der Zeilenvektoren
		for (int i = 0; i < feld.length; i++) {
			for (int j = 0; j < feld[0].length; j++) {
				zeilenvektor[j] = feld[i][j]; // aktuelle Zeile
			}
			
			indexKleinsteEle = findeIndexKleinsteEle(zeilenvektor, markierteSpalten);
			minWert = indexKleinsteEle[0];
			minWert2 = indexKleinsteEle[1];
			
			dz[i] = feld[i][minWert2] - feld[i][minWert];
			
		}
		
		// Berechnung Regretwerte der Spaltenvektoren
		for (int j = 0; j < feld[0].length; j++) {
			for (int i = 0; i < feld.length; i++) {
				spaltenvektor[i] = feld[i][j]; // aktuelle Spalte
			}
			
			indexKleinsteEle = findeIndexKleinsteEle(spaltenvektor, markierteZeilen);
			minWert = indexKleinsteEle[0];
			minWert2 = indexKleinsteEle[1];

			ds[j] = feld[minWert2][j] - feld[minWert][j];
		}
		
		// Abgleich Regretwerte in Zeile bzw. Spalte mit Markierung
		for (int i = 0; i < dz.length; i++) {
			if (markierteZeilen[i]) {
				dz[i] = Integer.MIN_VALUE;
			}
		}
		
		for (int i = 0; i < ds.length; i++) {
			if (markierteSpalten[i]) {
				ds[i] = Integer.MIN_VALUE;
			}
		}

		Vector<int[]> erg;
		erg = new Vector<int[]>(2);
	
		// Zuerst Zeilen, spaeter Spalten (im gesamten Algorithmus und allen Hilfsfunktionen wird dies so gehalten)
		erg.add(0, dz);
		erg.add(1, ds);
	
		return erg;
	} // berechneRegretwerte
	
	/**
	 * Berechnet den maximalen Regretwert und gibt an, ob sich dieser in einem Zeilenvektor oder Spaltenvektor befindet
	 * @param regretVektoren
	 * @return int-Array: Position 0: maximaler Regretwert, Position 1: Angabe ob max. Regretwert in Zeilenvektor (0) oder Spaltenvektor (1)
	 */

	public static int[] berechneMaxRegret(Vector<int[]> regretVektoren) {
		
		int[] dz, ds; // Regretwerte Zeilen (dz) und Spalten (ds)
		
		
		dz = regretVektoren.get(0); // hole Regretwerte der Zeilenvektoren
		ds = regretVektoren.get(1); // hole Regretwerte der Spaltenvektoren
		
		int tempMaxWert = -1; // temp. Variable zur Bestimmung der Regretwerte in Zeilen und Spalten
		int maxDzIndex = -1; // nimmt den maximalen Regretwert einer Zeile auf
		int maxDsIndex = -1; // nimmt den maximalen Regretwert einer Spalte auf
		int maxWert = -1; // nimmt den maximalen Regretwert auf
		int maxWertInZoderS = -1; // gibt an, ob maximaler Regret in Zeilen (0) oder Spalten (1) liegt
		
		// Bestimme Maximum der Zeilenregretwerte, beachte dabei etwaige Markierungen
		for (int i = 0; i < dz.length; i++) {
			if (dz[i] > tempMaxWert && !markierteZeilen[i]) {
				tempMaxWert = dz[i];
				maxDzIndex = i;
			}
		}

		// Bestimme Maximum des Spaltenregretwerte, beachte dabei etwaige Markierungen
		tempMaxWert = -1;
		
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] > tempMaxWert && !markierteSpalten[i]) {
				tempMaxWert = ds[i];
				maxDsIndex = i;
			}
		}
		
		// Test: Falls diese Exception geworfen wird, ist die Abbruchbedingung schon erreicht,
		// jedoch wurde dies nicht erkannt.
		if (maxDzIndex < 0 || maxDsIndex < 0) {
			throw new IllegalArgumentException ("Mehr als #Zeilen - 1 oder #Spalten - 1 markiert!. maxDz = " +maxDzIndex +", maxDs="+maxDsIndex);
		} 
	
		//Bestimme "Gesamt"-Maximum := Maximum(max. Regretwert Zeilen, max. Regretwert Spalten)
		if (dz[maxDzIndex] >= ds[maxDsIndex]) {
			maxWert = maxDzIndex;
			maxWertInZoderS = 0;

			maxDzIndex_global = maxDzIndex;

		} else {
			maxWert = maxDsIndex;
			maxWertInZoderS = 1;
			maxDsIndex_global = maxDsIndex;
		}
		
		int[] erg;
		erg = new int[2];
		
		erg[0] = maxWert;
		erg[1] = maxWertInZoderS;
		
		return  erg;
	} // berechneMaxRegret

	
	/**
	 * Finde und realisiere die guenstigste Verbindung in ausgewaehlter Zeile bzw. Spalte
	 * @param feld - Kostenmatrix als int[][]
	 * @param maxRegret - int-Array: Position 0: maximaler Regretwert, Position 1: Angabe ob max. Regretwert in Zeilenvektor (0) oder Spaltenvektor (1)
	 */
	public static void realisiere(int[][] feld, int[] maxRegret) {

		int pos = maxRegret[0];
		int indexMin = -1; // nimmt Index der minimalen Kosten auf
		int min = Integer.MAX_VALUE; // nimmt minimale Kosten auf
		
		if (maxRegret[1] == 0) {
			// maximaler Regretwert liegt in Zeilenvektor
			
			// finde minimale Kosten in gegebener Zeile
			for (int i = 0; i < feld[0].length; i++ ) {
				if (feld[pos][i] <= min && !markierteSpalten[i]) {
					min = feld[pos][i];
					indexMin = i;
				}
			}
			indexMinZeile = indexMin;
		
		} else {
			// maximaler Regretwert liegt in Spaltenvektor
			
			// finde minimale Kosten in gegebener Spalte
			for (int i = 0; i < feld.length; i++ ) {
				if (feld[i][pos] <= min && !markierteZeilen[i]) {
					min = feld[i][pos];
					indexMin = i;
				}
			}
			indexMinSpalte = indexMin;			
		}
				
		int zeile;
		int spalte;
		
		// Ordnung schaffen, d. h. pos und min bessere Namen auf Grund ihrer akutellen Bedeutung verpassen
		if (maxRegret[1] == 0) {
		// falls in Zeilenvektor (maxRegret[1] == 0)
		//feld[min][pos];
			zeile = pos;
			spalte = indexMin;
		} else {
			// falls in Spaltenvektor (maxRegret[1] == 1)
			//feld[pos][min]
			zeile = indexMin;
			spalte = pos;
		}
		
		// Genaue Position der minimalen Kosten ist nun bekannt: feld[zeile][spalte
		
		// Bestimme Transportmenge, d. h. Minimum aus Angebotsmenge und Nachfragemenge
		// und realisiere diese, d. h. verringere Angebot und Nachfrage um die Transportmenge
		
		if (angebot[zeile] <= nachfrage[spalte]) {
			transportmenge = angebot[zeile];
			angebot[zeile] = 0;
			nachfrage[spalte] = nachfrage[spalte] - transportmenge;
			markierteZeilen[zeile] = true; // markiere Zeile
			
		} else {
			transportmenge = nachfrage[spalte];
			angebot[zeile] = angebot[zeile] - transportmenge;
			nachfrage[spalte] = 0;
			markierteSpalten[spalte] = true; // markiere Spalte
		}
	} // realisiere
	
	/**
	 * Intitialisiert ein Markierungsarry (= keine Markierungen), d.h. setzt alle Werte auf false.
	 * @param a boolean-Array, welches initialisiert werden soll.
	 * 
	 */
	public static void initMarkierung(boolean[] a) {
		for (int l = 0; l < a.length; l++) {
			a[l] = false;
		}
	}
	
	/**
	 * Zaehlt die Anzahl der Markierungen in einer Zeile bzw. Spalte.
	 * Markierung an Stelle i entspricht dem Wert true an Stelle i im Array.
	 * @param a boolean-Array, welches die Markierungen aufgenommen hat.
	 * @return int - Anzahl der Markierungen in einer Zeile bzw. Spalte.
	 */
	public static int zaehleMarkierungen(boolean[] a) {
		int zaehler = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i]) {
				zaehler++;
			}
		}
		return zaehler;
	}
	
	/**
	 * Testet, ob alle Eintraege in der Kostenmatrix nicht-negativ sind.
	 * @param matrix - Matrix, die ueberprueft wird
	 * @return true - wenn alle Eintraege >= 0, false - wenn mindestens ein Eintrag < 0
	 */
	public static boolean kostenmatrixOk (int[][] matrix) {
		boolean matrixOk = true;
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] < 0) {
					matrixOk = false;
					break;
				}
			}
		}
		return matrixOk;
	}
	

	
// ---------------------------- implements Generator ----------------------------------	
	

	public String generate(AnimationPropertiesContainer properties,
			Hashtable<String, Object> primitives) {

		return	getCode (properties, primitives);
//		return null;
	}
	
	public String getAlgorithmName() {

		return "Vogelsche Approximationsmethode";
	}
	
	public String getCodeExample() {
return	"Die Vogelsche Approximationsmethode besteht aus zwei Teilen:\n"
		+ "* Im ersten Teil des Algorithmus werden Transportmengen solange\n "
		+ "  nach dem Prinzip des maximalen Regrets verteilt, bis entweder \n"
		+ "  m-1 Zeilen oder n-1 Spalten der Transportmengenmatrix belegt (= gestrichen) sind.\n"
		+ "* Im zweiten Teil des Algorithmus werden die noch verbleibenden \n"
		+ "  Transportmengen verteilt.\n"
		+ "(mit m = Anzahl Zeilen = Anzahl Anbieter, n = Anzahl Spalten = Anzahl Nachfrager)\n";

		

	}
	
	public Locale getContentLocale() {
		
		return Locale.GERMANY;
	}
	
	public String getDescription() {

		return "Die Vogelsche Approximationsmethode ist eine Heuristik aus dem Bereich des Operations Research zur Loesung des klassischen Transportproblems. " +
		"Bei gegebener Angebots- und Nachfragemenge sowie den Kosten fuer einen jeden Transportweg werden die moeglichst optimalen Verbindungen zwischen Anbietern und Nachfragern gesucht." +
		"Optimal bedeutet in diesem Zusammenhang, dass der Nachfrager alle gewuenschten Gueter erhaelt, der Anbieter alle angebotenen Waren absetzt und die Transportkosten insgesamt kostenminimal sind";
	}
	
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}
	
	public GeneratorType getGeneratorType() {
		
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}
	
	public String getName() {
		return "Vogelsche Approximation";
		
	}
	
	public String getOutputLanguage() {
		
		return Generator.PSEUDO_CODE_OUTPUT;
	}


	
	public String getAnimationAuthor() {
		return "Petra Dörsam, Kevin Tappe";
	}


	
	public void init() {
		// TODO Auto-generated method stub
		
	}

	
}
