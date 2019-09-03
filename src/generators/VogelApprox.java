package generators;


import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

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

public class VogelApprox implements Generator {
	
	
	static int[] angebot, nachfrage; // nimmt Angebots- bzw. Nachfragemengen auf
	static boolean[] markierteZeilen; // nimmt auf, welche Zeilen markiert sind
	static boolean[] markierteSpalten; // nimmt auf, welche Spalten markiert sind
	static int maxDsIndex_global, maxDzIndex_global,indexMinZeile, indexMinSpalte, transportmenge;
	

	//public static String getCode(AnimationPropertiesContainer properties,	Hashtable<String, Object> primitives) {
	public static String getCode() { // zum testen
		int []dz_array;
		Vector<Integer> lsgIndexVector = new Vector<Integer>(2);
		
		
		Language lang = new AnimalScript("Vogelsche Approximationsmethode", "Petra Dörsam, Kevin Tappe", 640, 480);

		
		// mehrere Schritte auf einmal moeglich
		lang.setStepMode(true);
		
		// Daten auslesen
		
		/*int [] a = (int[]) primitives.get("anbieterArray");
		int [] b = (int[]) primitives.get("nachfragerArray");
		 */
		// zum manuellen Testen die Werte von der Folie
		int[] a= {10,8,7};
		int[] b = {6,5,8,6};
	
		int[][] c  = {{7,2,4,7},{9,5,3,3},{7,7,6,4}};
		
		// Eigenschaften Text
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.BLACK);

		
		// Eigenschaften der Arrays (Anbieter und Nachfrager)
		ArrayProperties arrayProps = new ArrayProperties();
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    
	    // Nachfrage- und Angebots-Arrays fuer die Anzeige 
		// Array muss zur Darstellung in Matrix umgeschrieben werden
	    
	    // nachfrage umschreiben fuer Darstellung
	    int [][] b_j_daten=new int [1][b.length] ;
		for (int i = 0; i < b.length; i++) {
				b_j_daten[0][i] = b[i];
		}
		// angebot umschreiben fuer die Darstellung
		int [][] a_j_daten=new int [a.length][1] ;
			for (int j = 0; j < a.length; j++) {
				a_j_daten[j][0] = a[j];
		}
		// durchnummerierung der anbieter und nachfrager
		int [][] b_j_anzahl=new int [1][b.length] ;
		for (int i = 0; i < b.length; i++) {
				b_j_anzahl[0][i] = i+1;				
		}
		int [][] a_i_anzahl=new int [a.length][1] ;
		for (int i = 0; i < a.length; i++) {
			a_i_anzahl[i][0] = i+1;
		}
		// Eigenschaften der Kostenmatrix (aehnlich css)
		MatrixProperties kostenmatrixProps = new MatrixProperties();
		
		kostenmatrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		kostenmatrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		kostenmatrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.FALSE);   
		kostenmatrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		kostenmatrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);

// TODO BORDER_PROPERTY scheint eine interessante Eigenschaft zu sein. Herausfinden, wie man sie nutzt und was sie tut.
// TODO Das gleiche gilt fuer DIVIDINGLINE_PROPERTY 
//		kostenmatrixProps.set(AnimationPropertiesKeys.BORDER_PROPERTY, XXX);
						
		MatrixProperties matrixProps = new MatrixProperties();
		matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE); 
		matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GRAY);

		
		// Initialisierung der Matrix, die die Loesungen beinhalten soll
		int[][] lsgMatrixWerte = new int[a.length][b.length];
		
		
		// Festlegung der Eigenschaften (Eventuell durch den Generator variieren lassen)
		SourceCodeProperties scProps = new SourceCodeProperties();		 		 
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE); 					
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12)); 	// Schriftart & Größe
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   					// Farbe wenn hervorgehoben
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);		
		
		
		// Einleitungstext
		SourceCode einleitung1 = lang.newSourceCode(new Coordinates(30, 120), "einleitung1", null, scProps);
		einleitung1.addCodeLine("Bei der Vogelschen Approximationsmethode handelt es sich um eine Heuristik zum "
								+ "Loesen des klassischen Transportproblems.", null, 0, null);
		einleitung1.addCodeLine("", null, 0, null);
		einleitung1.addCodeLine("Das klassische Transportproblem (TPP) ist ein lineares Optimierungsproblem:", null, 0, null);
		einleitung1.addCodeLine("Zwischem m Anbietern A_i (i = 1...m) und n Nachfragern B_j (j = 1...n) mit Angebotsmengen a = (a_1, ..., a_m) und", null, 0, null);
		einleitung1.addCodeLine("Nachfragemengen b = (b_1, ..., b_m) eines Gutes und einer Kostenmatrix C, die die Transportkosten pro Mengeneinheit fuer", null, 0, null);
		einleitung1.addCodeLine("jeden moeglichen Weg zwischen Anbietern und Nachfragern wiedergibt, soll ein kostenminimaler Transportplan gefunden werden.", null, 0, null);
		einleitung1.addCodeLine("Als Nebenbedingung gilt: die Summe der Angebotsmengen muss gleich der Summe der Nachfragemengen sein.", null, 0, null);
		
	// ------------------------ nextStep()----------------------------------------	
		lang.nextStep();
		
		einleitung1.hide();
		
		SourceCode einleitung2 = lang.newSourceCode(new Coordinates(30, 120), "einleitung2",null, scProps);
		einleitung2.addCodeLine("Die Vogelsche Approximationsmethode besteht aus zwei Teilen:", null, 0, null); 
		einleitung2.addCodeLine("", null, 0, null); 
		einleitung2.addCodeLine("* Im ersten Teil des Algorithmus werden Transportmengen solange ", null, 1, null);
		einleitung2.addCodeLine("  nach dem Prinzip des kleinsten Regrets verteilt, bis entweder ", null, 1, null);
		einleitung2.addCodeLine("  m-1 Zeilen oder n-1 Spalten der Transportmatrix belegt", null, 1, null);
		einleitung2.addCodeLine("  (= gestrichen) sind." , null, 1, null);
		einleitung2.addCodeLine("", null, 1, null); // 2
		einleitung2.addCodeLine("* Im zweiten Teil des Algorithmus werden die noch verbleibenden ", null, 1, null);
		einleitung2.addCodeLine("  Transportmengen verteilt.", null, 1, null);
		einleitung2.addCodeLine("", null, 0, null);
		einleitung2.addCodeLine("(mit m = Anzahl Zeilen, n = Anzahl Spalten)", null, 0, null);
		einleitung2.addCodeLine("", null, 0, null);

	// ------------------------ nextStep()----------------------------------------	
		lang.nextStep();
		
		einleitung2.addCodeLine("Oben finden sich die Angaben zur den Angebots- und Nachfragemengen,", null, 0, null);
		einleitung2.addCodeLine("sowie die Transportkosten. Diese Angaben finden sich auch in den", null, 0, null);
		einleitung2.addCodeLine("unten erscheinenden Matrizen, in welchen der Algorithmus visualisiert wird, wieder.", null, 0, null);
		
		
		// Initale Ausgabe der Daten
		//TODO Alles in einem Schritt ausgeben -> 
		// Angebots- & Nachfragemengenanzeige (oben links)
		lang.newText(new Coordinates(20, 50), "Angebotsmenge a = ", "angebotsText", null, tp);
		lang.newIntArray(new Coordinates(145,45),a, "angebot", null, arrayProps);	
		lang.newText(new Coordinates(20, 85), "Nachfragemente b = ", "nachfrageText", null, tp);
		lang.newIntArray(new Coordinates(145, 80), b, "nachfrage", null, arrayProps);
		Text textKosten = lang.newText(new Coordinates(400,65), "Kostenmatrix C = ", "kostenText", null, tp);
		IntMatrix initC= lang.newIntMatrix(new Offset(0,-40, textKosten, AnimalScript.DIRECTION_NE), c, "kosten_anzeige", null, kostenmatrixProps);

		
	// ------------------------ nextStep()----------------------------------------
		lang.nextStep();
		einleitung2.hide();
		
	    // TODO Geht das nicht einfacher? Evtl. mit Matrix-Eigenschaft BORDER oder DIVIDINGLINE? (Keine Dokumentation zu
		// den beiden...) 
		// linien fuer die Matrizen definieren
	    String waagLinie[][] = new String [1][b.length+2];
	    
	    for(int count=0; count<b.length+2;count++){
	    	waagLinie[0][count] ="_";
	    }
	    
	    String senkLinie[][] = new String [a.length+2][1];
	    
	    for(int count=0; count<a.length+2;count++){
	    	senkLinie[count][0] ="|";
	    }

		// Loesungsmatrix mit "Rahmen" (unten links)
		IntMatrix a_i_anz = 
		lang.newIntMatrix(new Coordinates(120, 460), a_i_anzahl, "a_i_anz",null, matrixProps);
	
		IntMatrix lsgMatrix =
		lang.newIntMatrix(new Offset(0, 0, a_i_anz, AnimalScript.DIRECTION_NE), lsgMatrixWerte, "lsgMatrix",null, kostenmatrixProps);
		
		IntMatrix b_j_anz =
		lang.newIntMatrix(new Offset(-55, -25, lsgMatrix, AnimalScript.DIRECTION_N), b_j_anzahl, "b_j_anz",null, matrixProps);
	
		IntMatrix b_j =
		lang.newIntMatrix(new Offset(0, -10, lsgMatrix, AnimalScript.DIRECTION_SW), b_j_daten, "b_j",null, kostenmatrixProps);
		
		IntMatrix a_i =
		lang.newIntMatrix(new Offset(0, 0, lsgMatrix, AnimalScript.DIRECTION_NE), a_j_daten, "a_i",null, kostenmatrixProps);
		
		// Kostenmatrix mit Regretwerten (unten rechts)
		IntMatrix a_i_anz2 = lang.newIntMatrix(new Offset(200, 0, lsgMatrix, AnimalScript.DIRECTION_NE), a_i_anzahl, "a_i_anz2",null, matrixProps);
		
		
		// Als String[][], damit Elemente gestrichen und durch x ersetzt werden koennen
		
	    String c_String[][] = new String [c.length][c[0].length];
	    for(int count=0; count<c.length;count++){
	    	for(int x=0; x<c[0].length;x++){
	    		c_String[count][x] =(new Integer(c[count][x])).toString();;
	    	}
	    }
		
		StringMatrix kostenmatrix = lang.newStringMatrix(new Offset(0, 0, a_i_anz2, AnimalScript.DIRECTION_NE), c_String, "kostenmatrix",null, kostenmatrixProps);
		
		// Trennlinien fuer die Matrix rechts
		lang.newStringMatrix((new Offset(-10, -28, kostenmatrix, AnimalScript.DIRECTION_NE)), senkLinie, "senkLinieKMatrix",null, kostenmatrixProps);
		lang.newStringMatrix((new Offset(-10, -28, kostenmatrix, AnimalScript.DIRECTION_NW)), senkLinie, "senkLinieKMatrix2",null, kostenmatrixProps);
		lang.newStringMatrix((new Offset(-28,-20, kostenmatrix, AnimalScript.DIRECTION_NW)), waagLinie, "waagLinieKMatrix",null, kostenmatrixProps);
		lang.newStringMatrix((new Offset(-28,-30, kostenmatrix, AnimalScript.DIRECTION_SW)), waagLinie, "waagLinieKMatrix2",null, kostenmatrixProps);
		
		
		IntMatrix b_j_anz2= lang.newIntMatrix(new Offset(-55, -25, kostenmatrix, AnimalScript.DIRECTION_N), b_j_anzahl, "b_j_anz2",null, matrixProps);
		// Beschriftung rechte Matrix
		lang.newText((new Offset(-70,0, a_i_anz2, AnimalScript.DIRECTION_W)), "Anbieter A_i", "aText", null, tp);
		lang.newText((new Offset(-30, -5, b_j_anz2, AnimalScript.DIRECTION_N)), "Nachfrager B_j", "bText", null, tp);
		lang.newText((new Offset(0, -10, b_j_anz2, AnimalScript.DIRECTION_SE)), "dz", "dzText", null, tp);
		lang.newText((new Offset(-23, 10, a_i_anz2, AnimalScript.DIRECTION_SE)), "ds", "dsText", null, tp);
		
		// Beschriftung linken Matrix
		lang.newText((new Offset(-70,0, a_i_anz, AnimalScript.DIRECTION_W)), "Anbieter A_i", "aText_Lsg", null, tp);
		lang.newText((new Offset(-30, -5, b_j_anz, AnimalScript.DIRECTION_N)), "Nachfrager B_j", "bText_Lsg", null, tp);
		
		lang.newText((new Offset(0, -20, b_j_anz, AnimalScript.DIRECTION_SE)), "Rest", "dzText", null, tp);
		lang.newText((new Offset(-30, 10, a_i_anz, AnimalScript.DIRECTION_SE)), "Rest", "dsText", null, tp);
		
		// Trennlinien fuer die Matrix links
		lang.newStringMatrix((new Offset(-10, -28, lsgMatrix, AnimalScript.DIRECTION_NE)), senkLinie, "senkLinieLsgMatrix",null, kostenmatrixProps);
		lang.newStringMatrix((new Offset(-10, -28, lsgMatrix, AnimalScript.DIRECTION_NW)), senkLinie,"senkLinieLsgMatrix2",null, kostenmatrixProps);
		lang.newStringMatrix((new Offset(-28,-20, lsgMatrix, AnimalScript.DIRECTION_NW)), waagLinie, "waagLinieLsgMatrix",null, kostenmatrixProps);
		lang.newStringMatrix((new Offset(-28,-30, lsgMatrix, AnimalScript.DIRECTION_SW)), waagLinie, "waagLinieLsgMatrix2",null, kostenmatrixProps);
	
		// Ueberschriften zur Transportmengen- und Transportkostenmatrix
		lang.newText((new Offset(-90, -55, kostenmatrix, AnimalScript.DIRECTION_N)), "Transportkosten / Regretwerte:", "ueberschrift_kostenmatrix", null, tp); 
		lang.newText((new Offset(-125, -55, lsgMatrix, AnimalScript.DIRECTION_N)), "realisierte und verbleibende Transportmengen:", "ueberschrift_transportmengen", null, tp);
		
		// Hinweistext zur Markierung (Streichung) von Eintraegen/Zeilen/Spalten
		lang.newText((new Offset(-60, 40, kostenmatrix, AnimalScript.DIRECTION_SW)), "X markiert einen gestrichenen Eintrag", "hinweis_gestrichen", null, tp);
		
		// umschreiben von ds/dz zur darstellung
		String [][] dz_matrix=new String [a.length][1] ;
		for (int x = 0; x < a.length; x++) {
			dz_matrix[x][0] = " ";
		}
		String [][] ds_matrix=new String [1][b.length] ;
		for (int x = 0; x < b.length; x++) {
			ds_matrix[0][x] = " ";
		}
		
		StringMatrix dz_i = lang.newStringMatrix(new Offset(0, 0, kostenmatrix, AnimalScript.DIRECTION_NE), dz_matrix, "dz",null, kostenmatrixProps);
		StringMatrix ds_j = lang.newStringMatrix(new Offset(0, -10, kostenmatrix, AnimalScript.DIRECTION_SW), ds_matrix, "ds",null, kostenmatrixProps);

	
	// ------------------------ nextStep()----------------------------------------	
		lang.nextStep();
		
		// Source Code des 1. Teil des Algorithmus
		SourceCode sc = lang.newSourceCode(new Coordinates(30, 120), "sourceCodeTeil1",null, scProps);
		sc.addCodeLine("Erster Teil des Algorithmus (Verteilen nach kleinstem Regret):", null, 0, null);  // 0
				sc.addCodeLine("", null, 0, null); // 1
				sc.addCodeLine("Schleife: Solange (noch nicht m-1 Zeilen gestrichen) ODER (noch nicht n-1 Spalten gestrichen)) {", null, 0, null); // 2
				sc.addCodeLine("Fuer jede ungestrichen Zeile i in der Transportkostenmatrix:", null, 1, null); // 3
				sc.addCodeLine("* Bestimme die zwei kleinsten Werte und berechne deren Differenz (= Regretwert dz)", null, 2, null);  // 4
				sc.addCodeLine("Fuer jede ungestrichene Spalte j in der Transportkostenmatrix:", null, 1, null);  // 5
				sc.addCodeLine("* Bestimme die zwei kleinsten Werte und berechne deren Differenz (= Regretwert ds);", null, 2, null);  // 6
				sc.addCodeLine("Waehle die Zeile oder Spalte mit dem groessten Regretwert, die nicht gestrichen ist", null, 1, null);  // 7
				sc.addCodeLine("Bestimme die guenstigste Verbindung in dieser Zeile/Spalte, d.h. die minimalen Transportkosten", null, 1, null); // 8
				sc.addCodeLine("Realisiere die Transportmenge fuer diese minimalen Transportkosten:", null, 1, null); // 9
				sc.addCodeLine("* Transportmenge := Minimum(Angebot[i], Nachfrage[j])", null, 2, null); // 10
				sc.addCodeLine("* Angebot[i] := Angebot[i] - Tranportmenge", null, 2, null); // 11
				sc.addCodeLine("* Nachfrage[j] := Nachfrage[j] - Tranportmenge", null, 2, null); // 12
				sc.addCodeLine("Falls Angebot[i] = 0 => streiche i-te Zeile", null, 1, null); // 13
				sc.addCodeLine("Falls Nachfrage[j] = 0 => streiche j-te Spalte", null, 1, null); // 14
				sc.addCodeLine("} // Abbruchbedingung (m-1 Zeilen ODER n-1 Spalten gestrichen) erfuellt -> Ende der Schleife", null, 0, null); // 15
		
				sc.highlight(0); // Vorstellung des Algorithmus (Hervorheben der Ueberschrift)

		//--> Eigentlicher Animation beginnt hier
				
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
				angebot=a;
				nachfrage=b;

	// ------------------------ nextStep()----------------------------------------
				lang.nextStep();				
				
				// WHILE-Schleife aus Algorithmus
				while ((zaehleMarkierungen(markierteZeilen) < i - 1) 
					&& (zaehleMarkierungen(markierteSpalten) < j - 1)) {
					sc.toggleHighlight(0, 2); // Schleifen-Beginn
					
					// Berechne alle Regretwerte
					Vector<int[]> regretVektoren;
					regretVektoren = new Vector<int[]>(2);
							
					regretVektoren = berechneRegretwerte(c);
					
					sc.unhighlight(4); // unschoen, aber so wird das letzte element wieder entmarkiert
					sc.unhighlight(8); 
						
					// erste inintialierung
					int[] indexKleinstesEle={0,0};
					int prev_z = 0;

	// ------------------------ nextStep()----------------------------------------
					lang.nextStep();					
					sc.unhighlight(2);
					sc.highlight(3); // Durchgehen der Zeilen, Berechung Regret
					sc.highlight(4);

	// ------------------------ nextStep()----------------------------------------
					lang.nextStep();						
					
					// ZEILEN
					for (int z = 0; z<i; z++) {
						if (!markierteZeilen[z]) { // nur unmarkierte

						// unmarkieren
						kostenmatrix.unhighlightElem(prev_z, indexKleinstesEle[0], null, null);
						kostenmatrix.unhighlightElem(prev_z, indexKleinstesEle[1], null, null);	
						
						// finde kleinste Ele
						indexKleinstesEle = findeIndexKleinsteEle(c[z],markierteSpalten);
						// Markiere kleinste Ele
						kostenmatrix.highlightElem(z, indexKleinstesEle[0], null, null);
						kostenmatrix.highlightElem(z, indexKleinstesEle[1], null, null);
						prev_z = z;
					
	// ------------------------ nextStep()----------------------------------------					
						lang.nextStep();
						
						dz_array = regretVektoren.get(0);
						String [] dz_String = new String[dz_array.length]; 
						for (int k=0;k<dz_array.length;k++){
							dz_String[k] = (new Integer(dz_array[k])).toString();
						}
							//Regretwerte ersetzten
							dz_i.put(z, 0, dz_String[z], null, null);
							dz_i.highlightElem(z, 0, null, null);

	// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
						
							dz_i.unhighlightElem(z, 0, null, null);	
						}
					}
					sc.unhighlight(3);
					sc.unhighlight(4);
					kostenmatrix.unhighlightElem(prev_z, indexKleinstesEle[0], null, null);
					kostenmatrix.unhighlightElem(prev_z, indexKleinstesEle[1], null, null);	
					
					// SPALTEN
					int []indexKleinstesEleSp={0,0};
					int prev_s=0;
					int []spalten_array = new int [a.length];
									
					sc.highlight(5); // Durchgehen der Spalten, Berechung Regretwerte
					sc.highlight(6);

	// ------------------------ nextStep()----------------------------------------
					lang.nextStep();	
					
					for (int s = 0; s<j; s++) {
						if (!markierteSpalten[s]) {
						
							// unmarkieren
							kostenmatrix.unhighlightElem(indexKleinstesEleSp[0],prev_s,  null, null);
							kostenmatrix.unhighlightElem(indexKleinstesEleSp[1],prev_s,  null, null);	
							
							// finde kleinste Ele
							// Spalte in Array Schreiben (super unschoen)
						
							for (int k=0; k<a.length; k++){
								spalten_array[k]=c[k][s];
							}
		
							indexKleinstesEleSp = findeIndexKleinsteEle(spalten_array,markierteZeilen);
							
							kostenmatrix.highlightElem(indexKleinstesEleSp[0],s,  null, null);
							kostenmatrix.highlightElem(indexKleinstesEleSp[1],s,  null, null);
							prev_s=s;
	
		// ------------------------ nextStep()----------------------------------------								
							lang.nextStep();
							
							// umschreiben als String
							int[]ds_array = regretVektoren.get(1);
							String [] ds_String = new String[ds_array.length]; 
							for (int k=0;k<ds_array.length;k++){
								ds_String[k] = (new Integer(ds_array[k])).toString();
							}
							
							// Regretwerte ersetzten und hervorrufen
							ds_j.put(0, s, ds_String[s], null, null);
							ds_j.highlightElem(0, s, null, null);
							
		// ------------------------ nextStep()----------------------------------------					
							lang.nextStep();
						
							ds_j.unhighlightElem(0, s, null, null);
						}
					}
					sc.unhighlight(5);
					sc.unhighlight(6);
					kostenmatrix.unhighlightElem(indexKleinstesEleSp[0],prev_s,null, null);
					kostenmatrix.unhighlightElem(indexKleinstesEleSp[1],prev_s,  null, null);	
					
			
					int[] maxRegret;
					maxRegret = berechneMaxRegret(regretVektoren);
					
					// int-Array: Position 0: maximaler Regretwert, Position 1: Angabe ob max. Regretwert in Zeilenvektor (0) oder Spaltenvektor (1)
				
					realisiere(c, maxRegret);
					int sMaxReg, zMaxReg;
					
					// ist groesster Regretwert in Spalte oder Zeile? -> unterschiedliche markierungen
					
					if (maxRegret[1] == 0) {
						// markierung des groessten Regretwertes in reg-spalte
						zMaxReg = maxDzIndex_global;
						sMaxReg = indexMinZeile;
					
						sc.toggleHighlight(6, 7); // Markierung des groessten Regretwerts in der dz-Zeile

	// ------------------------ nextStep()----------------------------------------						
						lang.nextStep();
						dz_i.highlightElem(zMaxReg, 0, null, null);
						
					} // in Spalte
					else {
						sMaxReg = maxDsIndex_global;
						zMaxReg = indexMinSpalte;
						
						sc.toggleHighlight(6, 7); // Markierung des groessten Regretwerts in der ds Spalte

	// ------------------------ nextStep()----------------------------------------	
						lang.nextStep();
						ds_j.highlightElem(0, sMaxReg, null, null);
					}

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					sc.toggleHighlight(7, 8); 
					kostenmatrix.highlightElem(zMaxReg, sMaxReg, null, null); // Markierung der geringsten Transportkosten in Zeile
					
	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();					
				
					// EINTRAGUNG IN DIE TRANSPORTMATRIX
					lsgMatrix.highlightElem(zMaxReg, sMaxReg, null, null);
					lsgIndexVector.add(0, zMaxReg);
					lsgIndexVector.add(1, sMaxReg);
					
	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();

					ds_j.unhighlightElem(0, sMaxReg, null, null);
					dz_i.unhighlightElem(zMaxReg, 0, null, null);
					
					sc.unhighlight(8); // Minimale Transportmenge N/A
					sc.highlight(9);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					sc.highlight(10);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					
					b_j.highlightElem(0, sMaxReg, null, null);
					a_i.highlightElem(zMaxReg, 0, null, null);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					
					//  Transportmenge in Matrix realisieren
					lsgMatrix.put(zMaxReg, sMaxReg, transportmenge, null, null);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					sc.toggleHighlight(10, 11); // Angebotmenge um transportierte Menge reduzieren

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					// lsgMatrix.unhighlightElem(zMaxReg,sMaxReg, null, null);
					b_j.unhighlightElem(0, sMaxReg, null, null);
					a_i.put(zMaxReg, 0, angebot[zMaxReg], null, null);

	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					sc.toggleHighlight(11, 12);  // Nachfrage um transportierte Menge reduzieren

	// ------------------------ nextStep()----------------------------------------					
					a_i.unhighlightElem(zMaxReg, 0, null, null);
					b_j.highlightElem(0, sMaxReg, null, null);
					lang.nextStep();				
				
					b_j.put(0, sMaxReg, nachfrage[sMaxReg], null, null);
					
	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					b_j.unhighlightElem(0, sMaxReg, null, null);
					
					sc.unhighlight(9);
					sc.unhighlight(12);
					sc.highlight(13);

					// MARKIERUNG (Streichen) der Anbieter und Nachfrager
					// Streichen der Zeilen und Spalten die Null sind
					if (markierteZeilen[zMaxReg]) { // Markieren der Zeilen	
	// ------------------------ nextStep()----------------------------------------					
						lang.nextStep();
						
						a_i_anz2.highlightElem(zMaxReg,0, null, null);	
						a_i_anz.highlightElem(zMaxReg, 0,null, null);	
						dz_i.put(zMaxReg, 0, "X", null, null);
						
						kostenmatrix.highlightElemColumnRange(zMaxReg, kostenmatrix.getNrCols()-1, 0, null, null);
						for (int q = 0; q < kostenmatrix.getNrCols(); q++) {
							kostenmatrix.put(zMaxReg, q, "X", null, null);
						}
					}
	// ------------------------ nextStep()----------------------------------------					
					lang.nextStep();
					a_i_anz2.unhighlightElem(zMaxReg,0, null, null);	
					a_i_anz.unhighlightElem(zMaxReg, 0,null, null);	
					kostenmatrix.unhighlightElemColumnRange(zMaxReg, kostenmatrix.getNrCols()-1, 0, null, null);
					sc.toggleHighlight(13, 14);

					if (markierteSpalten[sMaxReg]) { // Markieren der Spalten
	// ------------------------ nextStep()----------------------------------------					
						lang.nextStep();
						b_j_anz2.highlightElem(0, sMaxReg, null, null);	
						b_j_anz.highlightElem(0, sMaxReg, null, null);
						ds_j.put(0, sMaxReg, "X", null, null);
						
						kostenmatrix.highlightElemRowRange(0, kostenmatrix.getNrRows()-1, sMaxReg, null, null);
						for (int q = 0; q < kostenmatrix.getNrRows(); q++) {
							kostenmatrix.put(q, sMaxReg, "X", null, null);
						}
					}
	// ------------------------ nextStep()----------------------------------------
					lang.nextStep();
					b_j_anz2.unhighlightElem(0, sMaxReg, null, null);	
					b_j_anz.unhighlightElem(0, sMaxReg, null, null);
					kostenmatrix.unhighlightElemRowRange(0, kostenmatrix.getNrRows()-1, sMaxReg, null, null);
					sc.toggleHighlight(14, 2);
					b_j.unhighlightElem(0, sMaxReg, null, null);
					a_i.unhighlightElem(zMaxReg, 0, null, null);
					kostenmatrix.unhighlightElem(zMaxReg, sMaxReg, null, null);
					lsgMatrix.unhighlightElem(zMaxReg, sMaxReg, null, null);					
				} // Grosse while-Schleife (= Abbruchbedingung fuer 1. Teil des Algorithmus)

	// ------------------------ nextStep()----------------------------------------					
				lang.nextStep();
				sc.unhighlight(2);
				sc.highlight(15);
				

// **************************** Verteilung der Restmengen:**********************************

	// ------------------------ nextStep()----------------------------------------
				lang.nextStep();			
				
				sc.hide();
				SourceCode scRest = lang.newSourceCode(new Coordinates(30, 120), "sourceCodeTeil2",null, scProps);
				scRest.addCodeLine("Zweiter Teil des Algorithmus (Verteilen der Restmengen):", null, 0, null);  // 0
				scRest.addCodeLine("", null, 0, null);  // 1
				scRest.addCodeLine("Fuer alle nun noch ungestrichenen Elemente [i,j] der Transportkostenmatrix:", null, 0, null); // 2
				scRest.addCodeLine("* Realisiere die moegliche Transportmenge:", null, 1, null); // 3
				scRest.addCodeLine("* Transportmenge := Minimum(Angebot[i], Nachfrage[j])", null, 2, null);  // 4
				scRest.addCodeLine("* Angebot[i] := Angebot[i] - Tranportmenge", null, 2, null);  // 5
				scRest.addCodeLine("* Nachfrage[j] := Nachfrage[j] - Tranportmenge", null, 2, null);  // 6
				scRest.addCodeLine("* Falls Angebot[i] = 0 => streiche i-te Zeile, falls Nachfrage[j] = 0 => streiche j-te Spalte", null, 2, null);  // 7
				scRest.addCodeLine("Alle Elemente gestrichen, d.h. alle Transportmengen verteilt -> Ende", null, 0, null);  // 8
		
				
				scRest.highlight(0); // Ueberschrift zum 2. Teil des Algorithmus hervorheben

	// ------------------------ nextStep()----------------------------------------				
				lang.nextStep();
				scRest.unhighlight(0);
				for (int z = 0; z < a.length; z++) {
					for (int s = 0; s < b.length; s++) {	

						if (!markierteZeilen[z] && !markierteSpalten[s]) {
							scRest.highlight(2);
							
	// ------------------------ nextStep()----------------------------------------				
							lang.nextStep();
							lsgMatrix.highlightCell(z, s, null, null);
							kostenmatrix.highlightCell(z, s, null, null);

	// ------------------------ nextStep()----------------------------------------				
							lang.nextStep();						
							scRest.toggleHighlight(2, 3);
	
	// ------------------------ nextStep()----------------------------------------		
							lang.nextStep();
							scRest.highlight(4);
							
	// ------------------------ nextStep()----------------------------------------		
							lang.nextStep();						
							a_i.highlightCell(z, 0, null, null);
							b_j.highlightCell(0, s, null, null);
							
							int transportmenge = 0;
							int angebot = a_i.getElement(z, 0);
							int nachfrage = b_j.getElement(0, s);
							
							// bestimmung der transportkosten
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
							lsgMatrix.highlightElem(z, s, null, null);
							lsgMatrix.put(z, s, transportmenge, null, null); // Eintragung der Transportmenge
							// Fuellen der IndexVektoren
							lsgIndexVector.add(0, z);
							lsgIndexVector.add(1, s);
							
		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
							scRest.toggleHighlight(4, 5);
							
		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
							b_j.unhighlightElem(0, s, null, null);
							a_i.put(z, 0, angebot, null, null); // Angebot um transportierte Menge reduzieren
					
		// ------------------------ nextStep()----------------------------------------				
							lang.nextStep();
							scRest.toggleHighlight(5, 6);
							
		// ------------------------ nextStep()----------------------------------------				
							lang.nextStep();				
							b_j.highlightElem(0, s, null, null);
							a_i.unhighlightElem(z, 0,  null, null);
							b_j.put(0, s, nachfrage, null, null); // Nachfrage um transportierte Menge reduzieren

		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
							scRest.toggleHighlight(6, 7);
			
		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
							
							// Markiere Spalte bzw. Zeile bei der der Wert 0 wird
							b_j.unhighlightElem(0, s, null, null);
							
							if (angebot == 0) {
								a_i_anz2.highlightElem(z, 0, null, null);	
								a_i_anz.highlightElem(z, 0, null, null);
								dz_i.put(z, 0, "X", null, null);
								kostenmatrix.put(z, 0, "X", null, null);
								kostenmatrix.highlightElemColumnRange(z, kostenmatrix.getNrCols()-1, 0, null, null);
								for (int q = 0; q < kostenmatrix.getNrCols(); q++) {
									kostenmatrix.put(z, q, "X", null, null);
								}
							} else if (nachfrage == 0) {
								b_j_anz2.highlightElem(0, s, null, null);	
								b_j_anz.highlightElem(0, s, null, null);
								ds_j.put(0, s, "X", null, null);
								kostenmatrix.highlightElemRowRange(0, kostenmatrix.getNrRows()-1, s, null, null);
								for (int q = 0; q < kostenmatrix.getNrRows(); q++) {
									kostenmatrix.put(q, s, "X", null, null);
								}
							}
		// ------------------------ nextStep()----------------------------------------							
							lang.nextStep();
							a_i_anz2.unhighlightElem(z, 0, null, null);	
							a_i_anz.unhighlightElem(z, 0, null, null);
							b_j_anz2.unhighlightElem(0, s, null, null);	
							b_j_anz.unhighlightElem(0, s, null, null);
							kostenmatrix.unhighlightElemColumnRange(z, kostenmatrix.getNrCols()-1, 0, null, null);
							kostenmatrix.unhighlightElemRowRange(0, kostenmatrix.getNrRows()-1, s, null, null);
							scRest.unhighlight(7);
							scRest.unhighlight(3);
							lsgMatrix.unhighlightElem(z, 0,  null, null);
							kostenmatrix.unhighlightCell(z, s, null, null);

						} // Grosse While-Schleife						
					}
					
				} // Alle Zeilen und Spalten durchlaufen -> keine Restmengen mehr zu verteilen -> Ende
				
				scRest.highlight(2);
	// ------------------------ nextStep()----------------------------------------
				lang.nextStep();
				scRest.unhighlight(2);
				scRest.highlight(8);
				
				// Unmarkiere die gesamten Matrizen und Streichung ALLER dz/ds
			
			    for(int count=0; count<c.length;count++){
			    	dz_i.put(count, 0, "X", null, null);
			    	for(int x=0; x<c[0].length;x++){
			    		kostenmatrix.unhighlightElem(count, x, null, null);
			    		lsgMatrix.unhighlightElem(count, x, null, null);
			    		ds_j.put(0, x, "X", null, null);
			    	}
			    }
				

	// **************************** Ausgabe des Fkt-Wertes und dessen Zusammensetzung**********************************

		// ------------------------ nextStep()----------------------------------------
				lang.nextStep();
				scRest.hide();
				SourceCode scFktWert = lang.newSourceCode(new Coordinates(30, 120), "Funktionswert", null, scProps);
				scFktWert.addCodeLine("Nachdem die Transportmengen verteilt wurden, koennen die Transportkosten einfach", null, 0, null);
				scFktWert.addCodeLine("berechnet werden:", null, 0, null);
				scFktWert.addCodeLine("", null, 0, null);
				scFktWert.addCodeLine("Transportkosten = Transportkosten c_ij * Transportmenge zwischen A_i und B_j", null, 0, null);
				scFktWert.addCodeLine("[fuer alle i = 1...m, j = 1...n]", null, 0, null); 
				
				
				TextProperties fktProp = new TextProperties();
				fktProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.RED);
				fktProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 16)); 
				
				String FktRechnung =" ";
				Integer erg_int = 0;
				Text FktWert_text = lang.newText(new Offset(-200, 150, scFktWert, AnimalScript.DIRECTION_N), "Gesamte Transportkosten = ", "funktionswert", null, tp);
				Text FktRechnung_anzeige = lang.newText(new Offset(35, 0, FktWert_text, AnimalScript.DIRECTION_NE), FktRechnung, "rechnung", null, tp);
				Text FktWert_ergebnis = lang.newText(new Offset(0, 0, FktWert_text, AnimalScript.DIRECTION_NE), FktRechnung, "ergebnis", null, fktProp);
				
				Enumeration<Integer> e = lsgIndexVector.elements ();
				boolean first = true;
				while (e.hasMoreElements ()) {
				
					Integer z = (Integer) e.nextElement();
					Integer s = (Integer) e.nextElement();
					
					
				
					lsgMatrix.highlightElem(z, s, null, null);
					
					Integer kosten = c[z][s];
					Integer menge = lsgMatrix.getElement(z, s);
				
					kostenmatrix.put(z, s, kosten.toString(), null, null);	
					kostenmatrix.highlightElem(z, s, null, null);
					initC.highlightElem(z, s, null, null);
					
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
					
					kostenmatrix.unhighlightElem(z, s, null, null);
					initC.unhighlightElem(z, s, null, null);
					lsgMatrix.unhighlightElem(z, s, null, null);
					
			}
		// ------------------------ nextStep()----------------------------------------					
				lang.nextStep();
				
				SourceCode scHinweis = lang.newSourceCode(new Coordinates(30, 320), "Hinweis", null, scProps);
				scHinweis.addCodeLine("Hinweis: Bei den errechneten Transportkosten handelt es sich nicht unbedingt um die minimal moeglichen", null, 0, null);
				scHinweis.addCodeLine("Transportkosten fuer diese Instanz des Problems, da zur Loesung eine Heuristik eingesetzt wurde!", null, 0, null);
				
	    return lang.toString();
	} // getCode

	
	// Zum Testen
	public static void main(String args[]){
		
		
		System.out.print(getCode());
		
	}
	// TODO Generatorschnittstelle
	
//	private final static int[][] standardmatrix = {{7,2,4,7},{9,5,3,3},{7,7,6,4}};
/*	
	private static int[][] parseMatrix(String eingabe){
		String [] zeilen = eingabe.split(";");
		int n = zeilen.length;
		zeilen[0] = zeilen[0].trim();
		zeilen[n-1] = zeilen[n-1].trim();
		if (zeilen[0].charAt(0) == '['){ // Eckige Klammern eliminieren
			if (zeilen[n-1].charAt(zeilen[n-1].length()-1) == ']'){ // Korrekte Klammerung pruefen 
				zeilen[0] = zeilen[0].substring(1);
				zeilen[n-1] = zeilen[n-1].substring(0,zeilen[n-1].length()-1);
			} else {
				System.err.println("Syntax-Fehler: schließende Klammer erwartet.");
				return standardmatrix;
			}
		}
		int [][] result = new int[n][n];
		int i = 0;
		for (String zeile : zeilen) {
			zeile = zeile.trim();
			String[] elemente = zeile.split(",");
			if (elemente.length != n){
				System.err.println("Syntax-Fehler: Input-Matrix muss symmetrisch sein.");
				// TODO muss unsere wohl nicht, oder?
				return standardmatrix;
			}
			
			for (int j = 0; j < n; j++) {
				try {
					result[i][j] = Integer.parseInt(elemente[j].trim());
					if (result[i][j] != 0 && result[i][j] != 1){ // muss 0 oder 1 sein
						System.err.println("Syntax error: Values for input matrix must be 0 or 1.");
						// TODO bei uns auch nicht
						return standardmatrix;
					}
				} catch (NumberFormatException nfe){
					System.err.println("Syntax error: Number expected for input matrix.");
					return standardmatrix;
				}
			}
			i++;
		}
		
		return result;
	}	
	*/
	
// ---------------------------- Logik der Vogelschen Approximation ----------------------------------	
	
	/**
	 * Findet in einem int-Array die zwei kleinsten Elemente
	 * @param liste - int-Array in welchem die zwei kleinsten Werte gesucht werden sollen
	 * @param markListe - boolean-Array welches angibt, welche Werte schon markiert, d. h. irrelevant sind
	 * @return - int-Array mit Position 0: Index des kleinsten Elements, Position 1: zweitkleinsten Elements
	 */
	
	public static int[] findeIndexKleinsteEle(int[] liste, boolean[] markListe) {
		
		
		// Damit das Bestimmen des Indices des kleinsten und zweitkleinsten moeglichst einfach wird,
		// werden Wert und zugehoerige Markierung in einem Verbund (Record, "Objekt") zusammengefasst
		// und so gemeinsam sortiert.
		VerbundWertMarkierung[] kopieListe;
		kopieListe = new VerbundWertMarkierung[liste.length];
		
		for (int i = 0; i < liste.length; i++) {
			kopieListe[i] = new VerbundWertMarkierung(liste[i], i, markListe[i]);
		}
		
		Arrays.sort(kopieListe); // Durch Sortierung laesst sich leicht das kleinste und zweikleinste Element bestimmen
		
		int indexMin = -1; // Index des kleinsten unmarkierten Elements
		int indexMin2 = -1; // Indes des zweikleinsten unmarkierten Elements
		int indexMinSort = -1; // benoetigt zur Bestimmung des zweitkleinsten Elements
		
		// Finde kleinstes unmarkiertes Element
		for (int i = 0; i < kopieListe.length; i++) {
			if (!kopieListe[i].markiert) {
				indexMin = kopieListe[i].index;
				indexMinSort = i;
				break; // kl. Element gefunden, Schleife kann abgebrochen werden
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
		
		// Hier wird gezaubert! (Berechnung Regretwerte der Zeilenvektoren)
		for (int i = 0; i < feld.length; i++) {
			for (int j = 0; j < feld[0].length; j++) {
				zeilenvektor[j] = feld[i][j]; // aktuelle Zeile
			}
			
			indexKleinsteEle = findeIndexKleinsteEle(zeilenvektor,markierteSpalten);
			minWert = indexKleinsteEle[0];
			minWert2 = indexKleinsteEle[1];
			
			dz[i] = feld[i][minWert2] - feld[i][minWert];
			
		}
		
		// Hier wird auch gezaubert! (Berechnung Regretwerte der Spaltenvektoren)
		for (int j = 0; j < feld[0].length; j++) {
			for (int i = 0; i < feld.length; i++) {
				spaltenvektor[i] = feld[i][j]; // aktuelle Spalte
			}
			
			indexKleinsteEle = findeIndexKleinsteEle(spaltenvektor,markierteZeilen);
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
	
		// Zuerst Zeilen, spaeter Spalten
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
		// Debug
		// Falls diese Exception geworfen wird, ist die Abbruchbedingung schon erreicht, jedoch wurde dies
		// nicht erkannt.
		if (maxDzIndex < 0 || maxDsIndex < 0) {
			throw new IllegalArgumentException ("Mehr als #Zeilen - 1 oder #Spalten - 1 markiert!. maxDz = " +maxDzIndex +", maxDs="+maxDsIndex);
		} 
	
		//Bestimme "Gesamt"-Maximum aus Maximum(max. Regretwert Zeilen, max. Regretwert Spalten)
		if (dz[maxDzIndex] >= ds[maxDsIndex]) {
			maxWert = maxDzIndex;
			maxWertInZoderS = 0;

			maxDzIndex_global=maxDzIndex;

		} else {
			maxWert = maxDsIndex;
			maxWertInZoderS = 1;
			maxDsIndex_global=maxDsIndex;
	
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
			indexMinZeile=indexMin;
		
		
		} else {
			// maximaler Regretwert liegt in Spaltenvektor
			
			// finde minimale Kosten in gegebener Spalte
			for (int i = 0; i < feld.length; i++ ) {
				if (feld[i][pos] <= min && !markierteZeilen[i]) {
					min = feld[i][pos];
					indexMin = i;
				}
			}
			indexMinSpalte=indexMin;
			
		}
				
		int zeile;
		int spalte;
		
		// Ordnung schaffen, d. h. pos und min Namen auf Grund ihrer akutellen Bedeutung verpassen
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
	 * Intitialisiert ein Markierungsarry (=keine Markierungen), d.h. setzt alle Werte auf false.
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
		
		
		// TODO Hier den Aufruf des Algorithmus hinein 

	//	getCode( properties,primitives);
		return null;
	}
	
	public String getAlgorithmName() {

		return "Vogelsche Approximation";
	}
	
	public String getCodeExample() {
//TODO Pseudo-Code einfuegen		
	
		
	return	"Bei der Vogelschen Approximationsmethode handelt es sich um eine Heuristik zum Loesen des klassischen Transportproblems."
		+ "Der Algorithmus besteht aus zwei Teilen:"
		+ "* Im ersten Teil des Algorithmus werden Transportmengen solange nach dem Prinzip des kleinsten Regrets verteilt, bis entweder "
		+ "  m-1 Zeilen oder n-1 Spalten der Transportmatrix belegt (= gestrichen) sind."
		+ "* Im zweiten Teil des Algorithmus werden die noch verbleibenden Transportmengen verteilt. (mit m = Anzahl Zeilen, n = Anzahl Spalten)"
		+ "Oben finden sich die Angaben zur den Angebots- und Nachfragemengen,"
		+ "sowie die Transportkosten. Diese Angaben finden sich auch in den"
		+ "unten erscheinenden Matrizen, in welchen der Algorithmus visualisiert wird, wieder.";
	
	}
	
	public Locale getContentLocale() {
		
		return Locale.GERMANY;
	}
	
	public String getDescription() {

		return "Loesung des Transportproblems durch die Vogelsche Approximationsmethode";
	}
	
	public String getFileExtension() {

		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION; // Folien zur API-Beschreibung nennt ".asu", Uebungsblaett nennt "asu"
	}
	
	public GeneratorType getGeneratorType() {
		
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}
	
	public String getName() {
		
		return "Die Vogelsche Approximationsmethode ist eine Heuristik aus dem Bereich des Operations Research zur Lösung des klassischen Transportproblems. " +
			"Bei gegebener Angebotes- und Nachfragemenge sowie den Kosten für jeden Transport, werden die möglichst optimalen Verbindungen zwischen Anbietern und Nachfragern gesucht." +
			"Optimal bedeutet in diesem Zusammenhang, dass der Nachfrager alle gewünschten Güter erhöt,der Anbieter alle angebotenen Waren absetzt und die Transportkosten insgesamt kostenminimal sind";

	}
	
	public String getOutputLanguage() {
		
		return Generator.PSEUDO_CODE_OUTPUT;
	}


	@Override
	public String getAnimationAuthor() {
		return "Petra Doersam und Kevin Tappe";
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	
}
