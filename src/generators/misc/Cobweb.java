/*
 * Cobweb.java
 * J. H. Mieseler, T. G. Petry, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpersCOBWEB.CobwebCategory;
import interactionsupport.models.MultipleChoiceQuestionModel;

@SuppressWarnings("unused")
public class Cobweb implements Generator {
	//Globale Variablen
    private static Language l;
    private TextProperties SubtreeRootHighlighting;
    private static TextProperties PseudoCodeHighlighting;
    private TextProperties TreeBestChildHighlighting;
    private int[][] TrainingsData;
    private static algoanim.primitives.Graph graf;
	private static StringMatrix nodeTexts;
	private static StringMatrix cuTable;
	private static ArrayList<CobwebCategory> treeList;
	private static CobwebCategory theRoot;
	private static TextProperties ueberschrift = new TextProperties();
	private static Text pseudoCode1;
	private static Text pseudoCode2;
	private static Text pseudoCode3;
	private static Text pseudoCode3a;
	private static Text pseudoCode3b;
	private static Text pseudoCode4;
	private static Text pseudoCode5;
	private static Text pseudoCode6;
	private static Text pseudoCode7;
	private static Text pseudoCode8;
	private static Text pseudoCode9;
	private static Text pseudoCode10;
	private static Text pseudoCode11;
	private static Text pseudoCode12;
	private static Text pseudoCode13;
	private static Text pseudoCode14;
	private static Text pseudoCode15;
	private static Text pseudoCode16;
	private static Text pseudoCode17;
	private static Text pseudoCode18;
	private static Text pseudoCode19;
	private static Text pseudoCode20;
	private static Text pseudoCode21;
	private static Text pseudoCode22;
	private static Text pseudoCode23;
	private static Text pseudoCode24;
	private static Text pseudoCode25;
	private static Text pseudoCode26;
	private static Text pseudoCode27;
	private static Text pseudoCode28;
	private static Text tree;
	private static Text baumknotenlables;
	private static Text trainingsdaten;
	private static Text cu;
	
	// Colors
	private static Color bordeaux = Color.getHSBColor(0F, 1.0F, 0.65F);
	private static Color cream = Color.getHSBColor(0.115F, 0.15F, 1.0F);
	private static Color cloud = Color.getHSBColor(0.6F, 0.08F, 1.0F);
	
	// Question enCounters :P
	private static boolean qc1 = true;
	private static boolean qc2 = true;
	private static boolean qc3 = true;
	private static boolean qc4 = true;

    public void init(){
        l = new AnimalScript("COBWEB Clustering [DE]", "J. H. Mieseler, T. G. Petry", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        SubtreeRootHighlighting = (TextProperties)props.getPropertiesByName("SubtreeRootHighlighting");
        PseudoCodeHighlighting = (TextProperties)props.getPropertiesByName("PseudoCodeHighlighting");
        TreeBestChildHighlighting = (TextProperties)props.getPropertiesByName("TreeBestChildHighlighting");
        TrainingsData = (int[][])primitives.get("TrainingsData");
        l.setStepMode(true);
		l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		// Anlegen verschiedener Textarten
    Font font = new Font("SansSerif", Font.BOLD, 5);
		Font schriftart = new Font("SansSerif", Font.BOLD, 20);
		Font introFont = new Font("SansSerif", 1, 15);
		cloud = (Color) TreeBestChildHighlighting.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		cream = (Color) SubtreeRootHighlighting.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		ueberschrift.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		ueberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY, schriftart);

		TextProperties titel = new TextProperties();
		titel.set(AnimationPropertiesKeys.COLOR_PROPERTY, bordeaux);
		titel.set(AnimationPropertiesKeys.FONT_PROPERTY, schriftart);

		TextProperties intro = new TextProperties();
		intro.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		intro.set(AnimationPropertiesKeys.FONT_PROPERTY, introFont);
		
		TextProperties intromark = new TextProperties();
		intromark.set(AnimationPropertiesKeys.COLOR_PROPERTY, bordeaux);
		intromark.set(AnimationPropertiesKeys.FONT_PROPERTY, introFont);

		// Text auf der Startseite
		Text cobweb = l.newText(new Coordinates (10,10), "COBWEB Clustering" , "titel", null, titel);
		// Lege das gelbe Rechteck unter den Titel
		RectProperties var3 = new RectProperties();
		var3.set("fillColor", Color.getHSBColor(0.115F, 0.15F, 1.0F));
		var3.set("filled", true);
		var3.set("depth", 2);
		Rect yellowbox = l.newRect(new Offset(-5, -5, cobweb, "NW"), new Offset(5, 5, cobweb, "SE"), "hrect", (DisplayOptions)null, var3);
		l.nextStep();

		
		
		// Introduction text
		Text introt1 = l.newText(new Coordinates(30, 75),          "Einleitende Erklaerungen", "intro", null, ueberschrift);
		Text introta = l.newText(new Offset(0, 0, introt1, "SW"),  "COBWEB Clustering ist ein inkrementelles System fuer hierarchisch konzeptionelles Clustering aus dem Bereich", "intro", null, intro);
		Text introtb = l.newText(new Offset(0, 0, introta, "SW"),  "des maschinellen Lernens und wurde von Professor Douglas H. Fisher entwickelt. COBWEB nutzt nominelle Attributwerte.", "intro", null, intro);
		Text introtc = l.newText(new Offset(0, 0, introtb, "SW"),  "COBWEB organisiert die einzelnen Beobachtungen/Trainingsdatenpunkte schrittweise in einem Klassifikationsbaum.", "intro", null, intro);
		Text introtd = l.newText(new Offset(0, 0, introtc, "SW"),  "Jeder Knoten im Klassifikationsbaum repraesentiert ein Konzept (eine Klasse) und wird mit einem", "intro", null, intro);
		Text introte = l.newText(new Offset(0, 0, introtd, "SW"),  "Wahrscheinlichkeits-Konzept beschriftet, welches die Attributwert-Verteilung aller Objekte, die durch", "intro", null, intro);
		Text introtf = l.newText(new Offset(0, 0, introte, "SW"),  "dieses Konzept klassifiziert werden, zusammenfasst.", "intro", null, intro);
		Text introtg = l.newText(new Offset(0, 0, introtf, "SW"),  "Der so erstellte Klassifikationsbaum kann dann von weiteren Algorithmen genutzt werden, um fehlende Attributwerte", "intro", null, intro);
		Text introth = l.newText(new Offset(0, 0, introtg, "SW"),  "oder die Klasse eines neuen Objekts vorherzusagen.", "intro", null, intro);
		Text introti = l.newText(new Offset(0, 0, introth, "SW"),  "Die folgende Visualisierung wird sich allerdings ausschlie�lich mit dem COBWEB Algorithmus beschaeftigen und", "intro", null, intro);
		Text introtj = l.newText(new Offset(0, 0, introti, "SW"),  "veranschaulichen, wie dieser den Klassifikationsbaum aufbaut. Au�erdem sind zur Vereinfachung nur die", "intro", null, intro);
		Text introtj1 = l.newText(new Offset(0, 0, introtj, "SW"), "Attributwerte 0 und 1 erlaubt, was bedeutet das Objekt besitzt das Attribut oder eben nicht.", "intro", null, intro);
		l.nextStep();
		Text introt2 = l.newText(new Offset(0, 0, introtj1, "SW"), "Hierarchisch Konzeptionelles Clustering", "intro", null, ueberschrift);
		Text introtk = l.newText(new Offset(0, 0, introt2, "SW"),  "Konzeptionelles Clustering bassiert auf dem Prozess der Kategorisierung. Hierbei versucht man Objekte zu", "intro", null, intro);
		Text introtl = l.newText(new Offset(0, 0, introtk, "SW"),  "erkennen, zu unterscheide und zu verstehen, um diese dann in Kategorien zusammenzufassen.", "intro", null, intro);
		Text introtm = l.newText(new Offset(0, 0, introtl, "SW"),  "Jede Kategorie liegt einem Konzept zu Grunde, das die unter dieser Kategorie klassifizierten Objekt beschreibt.", "intro", null, intro);
		Text introtn = l.newText(new Offset(0, 0, introtm, "SW"),  "Hierarchisch Konzeptionelles Clustering ist eine Erweiterung des Konzeptionellen Clusterings bei dem die", "intro", null, intro);
		Text introtn1 = l.newText(new Offset(0, 0, introtn, "SW"), "Konzepte noch in hierarchischen Strukturen angeordnet werden.", "intro", null, intro);
		Text introto = l.newText(new Offset(0, 0, introtn1, "SW"), "Konzeptionelles Clustering ist ein Paradigma fuer Unueberwachtes Lernen und kann daher auch dann angewendet werden,", "intro", null, intro);
		Text introtp = l.newText(new Offset(0, 0, introto, "SW"),  "wenn die echten Klassen der Beobachtungen/Trainingsdatenpunkte nicht bekannt sind", "intro", null, intro);
		l.nextStep();
		Text introt3 = l.newText(new Offset(0, 0, introtp, "SW"),  "Der COBWEB Algorithmus", "intro", null, ueberschrift);
		Text introtq = l.newText(new Offset(0, 0, introt3, "SW"),  "Der COBWEB Algorithmus ist ein rekursiver Algorithmus, dem eine COBWEB Kategorie/ ein COBWEB Baumknoten und", "intro", null, intro);
		Text introtq1 = l.newText(new Offset(0, 0, introtq, "SW"), "ein einzufuegendes Objekt uebergeben werden und er besteht aus vier grundlegenden Operationen.", "intro", null, intro);
		Text introtr = l.newText(new Offset(0, 0, introtq1, "SW"), "Welche Operation im aktuellen Schritt ausgewaehlt und durchgefuehrt wird, haengt von der sog. Category Utility ab,", "intro", null, intro);
		Text introts = l.newText(new Offset(0, 0, introtr, "SW"),  "die durch die Durchfuehrung dieser Operation erzielt werden wuerde.", "intro", null, intro);
		Text intrott = l.newText(new Offset(0, 0, introts, "SW"),  "Die vier grundlegenden Operationen sind:", "intro", null, intro);
		Text intrott1 = l.newText(new Offset(0, 0, intrott, "SW"), "1) Einen neuen Knoten einfuegen", "intro", null, intromark);
		Text introtu = l.newText(new Offset(0, 0, intrott1, "SW"), "    Ein neuer Knoten wird erstellt, das aktuelle Objekt diesem hinzugefuegt und der Knoten wird dem Baum angefuegt.", "intro", null, intro);
		Text introtv = l.newText(new Offset(0, 0, introtu, "SW"),  "2) Verschmelzen zweier Knoten", "intro", null, intromark);
		Text introtw = l.newText(new Offset(0, 0, introtv, "SW"),  "    Zwei Knoten werden verschmolzen, indem ein neuer Knoten an deren Stelle erstellt wird und die zu verschmelzenden", "intro", null, intro);
		Text introtx = l.newText(new Offset(0, 0, introtw, "SW"),  "    Knoten zu dessen Kindern gemacht werden. Der neue Knoten fasst die Attributwert-Verteilungen der beiden", "intro", null, intro);
		Text introty = l.newText(new Offset(0, 0, introtx, "SW"),  "    verschmolzenen Knoten zusammen und ist Kind des Elternknoten der beiden verschmolzenen Knoten.", "intro", null, intro);
		Text introtz = l.newText(new Offset(0, 0, introty, "SW"),  "3) Spaltung eines Knotens", "intro", null, intromark);
		Text introtaa = l.newText(new Offset(0, 0, introtz, "SW"), "    Ein Knoten wird gespalten, indem er durch seine Kinder ersetzt wird.", "intro", null, intro);
		Text introtbb = l.newText(new Offset(0, 0, introtaa, "SW"),"4) Das Objekt die Hierarchie hinabreichen", "intro", null, intromark);
		Text introtcc = l.newText(new Offset(0, 0, introtbb, "SW"),"    Der COBWEB Algorithmus wird mit der Wurzel des entsprechenden Teilbaumes und dem aktuellen Objekt aufgerufen", "intro", null, intro);
		Text introtz1 = l.newText(new Offset(0, 0, introtcc, "SW"),  "Wichtig:", "intro", null, intromark);
		Text introtz2 = l.newText(new Offset(0, 0, introtz1, "SW"),  "Der Baum wird so aufgebaut, dass in den Blaettern immer nur ein Objekt oder mehrere Objekte mit den exakt selben", "intro", null, intro);
		Text introtz3 = l.newText(new Offset(0, 0, introtz2, "SW"),  "Attributwerten gespeichert werden. Hier kann man also aus der Likelihood dieses Blattes und der Anzahl der enthaltenen", "intro", null, intro);
		Text introtz4 = l.newText(new Offset(0, 0, introtz3, "SW"),  "Objekt direkt auslesen, welche Attributwerte das Objekt im Blatt hat und wie oft es dort vorkommt.", "intro", null, intro);
		Text introtz5 = l.newText(new Offset(0, 0, introtz4, "SW"),  "Alle anderen Knoten, die keine Bleatter sind, enthalten alle Objekte die ihre Kindknoten enthalten und", "intro", null, intro);
		Text introtz6 = l.newText(new Offset(0, 0, introtz5, "SW"),  "die Likelihood zeigt an wie wahrscheinlich der jeweilige Attributwert 1 ist bzw die prozentuale Anzahl der Objekte,", "intro", null, intro);
		Text introtz7 = l.newText(new Offset(0, 0, introtz6, "SW"),  "bei denen dieser Attributwert 1 ist.", "intro", null, intro);
		l.nextStep();
		Text introt4 = l.newText(new Offset(0, 0, introtz7, "SW"), "Category Utility", "intro", null, ueberschrift);
		Text introtdd = l.newText(new Offset(0, 0, introt4, "SW"), "Die Category Utility ist ein Messwert fuer die Guete der Kategorisierung einer gegebenen Menge an Kategorien.", "intro", null, intro);
		Text introtee = l.newText(new Offset(0, 0, introtdd, "SW"),"Sie versucht sowohl die Wahrscheinlichkeit, dass zwei Objekte in der selben Kategorie Attributwerte gemeinsam haben,", "intro", null, intro);
		Text introtff = l.newText(new Offset(0, 0, introtee, "SW"),"als auch die Wahrscheinlichkeit, dass zwei Objekte in unterschiedlichen Kategorien verschiedene Attributwerte", "intro", null, intro);
		Text introtgg = l.newText(new Offset(0, 0, introtff, "SW"),"besitzen, zu maximieren.", "intro", null, intro);
		Text introthh = l.newText(new Offset(0, 0, introtgg, "SW"),"Die Category Utility berechnet sich folgenderma�en:", "intro", null, intro);
		Text introtii = l.newText(new Offset(0, 0, introthh, "SW"),"CU({C_1, ..., C_n}) = 1/n[sum over k=1 to n(P(C_k)[sum over i(sum over j(P(A_j = V_ij|C_k)^2)) - sum over i(sum over j(P(A_j = V_ij)^2))]]", "intro", null, intromark);
		Text introtjj = l.newText(new Offset(0, 0, introtii, "SW"),"Hierbei ist {C_1, ..., C_n} eine Menge von Kategorien, P(C_k) die Wahrscheinlichkeit der Kategorie k,", "intro", null, intro);
		Text introtkk = l.newText(new Offset(0, 0, introtjj, "SW"),"P(A_j = V_ij|C_k) die Kategorie-bedingte Wahrscheinlichkeit des Attribut-Wert Paares A_j = V_ij der Kategorie k", "intro", null, intro);
		Text introtll = l.newText(new Offset(0, 0, introtkk, "SW"),"und P(A_j = V_ij) die marginal Wahrscheinlichkeit des Attribut-Wert Paares A_j = V_ij.", "intro", null, intro);
//		Text introtmm = l.newText(new Offset(0, 0, introtmm, "SW"),"", "intro", null, intro);
				
		l.nextStep();
		

		// verstecke Startseitentext
		List<Primitive> keepThose = new LinkedList<Primitive>();
		keepThose.add(cobweb);
		keepThose.add(yellowbox);
		l.hideAllPrimitivesExcept(keepThose);


		// PseudoCode		
		TextProperties comment = new TextProperties();
		comment.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		
		// Zum Text markieren bitte pesudeoCode.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null); verwenden
		pseudoCode1 = l.newText(new Coordinates(40, 60), "PseudoCode", "pseudocode", null, ueberschrift);
		pseudoCode2 = l.newText(new Offset(0, 0, pseudoCode1, "SW"),   "// Input: A COBWEB subtree root category, an instance of record to insert", "pseudocode", null, comment);
		pseudoCode3 = l.newText(new Offset(0, 0, pseudoCode2, "SW"),   "COBWEB(root, record):", "pseudocode", null);
		pseudoCode3a = l.newText(new Offset(0,0, pseudoCode3, "SW"),   "if root has no children then", "Beschreibung",null);
		pseudoCode3b = l.newText(new Offset(0,0, pseudoCode3a, "SW"),  "   if root has no records or already contains the record then", "Beschreibung",null);
		pseudoCode4 = l.newText(new Offset(0, 0, pseudoCode3b, "SW"),  "      root.insert(record)", "pseudocode", null);
		pseudoCode7 = l.newText(new Offset(0, 0, pseudoCode4, "SW"),   "   else", "pseudocode", null);
		pseudoCode8 = l.newText(new Offset(0, 0, pseudoCode7, "SW"),   "      root.children := {copy(root)}", "pseudocode", null);
		pseudoCode9 = l.newText(new Offset(0, 0, pseudoCode8, "SW"),   "      newcategory(record)", "pseudocode", null);
		pseudoCode10 = l.newText(new Offset(0, 0, pseudoCode9, "SW"),  "      root.insert(record)", "pseudocode", null);
		pseudoCode11 = l.newText(new Offset(0, 0, pseudoCode10, "SW"), "else", "pseudocode", null);
		pseudoCode12 = l.newText(new Offset(0, 0, pseudoCode11, "SW"), "   insert(record, root)", "pseudocode", null);
		pseudoCode5 = l.newText(new Offset(0, 0, pseudoCode12, "SW"),  "   if a child of root contains record then", "pseudocode", null);
		pseudoCode6 = l.newText(new Offset(0, 0, pseudoCode5, "SW"),   "      cobweb(child, record), return", "pseudocode", null);
		pseudoCode13 = l.newText(new Offset(0, 0, pseudoCode6, "SW"),  "   for child in root's children do", "pseudocode", null);
		pseudoCode14 = l.newText(new Offset(0, 0, pseudoCode13, "SW"), "      calculate Category Utility for child.insert(record),", "pseudocode", null);	
		pseudoCode15 = l.newText(new Offset(0, 0, pseudoCode14, "SW"), "   end for", "pseudocode", null);	
		pseudoCode16 = l.newText(new Offset(0, 0, pseudoCode15, "SW"), "   set best1, best2 children w. best CU.", "pseudocode", null);
		pseudoCode17 = l.newText(new Offset(0, 0, pseudoCode16, "SW"), "   if newcategory(record) yields best CU then", "pseudocode", null);
		pseudoCode18 = l.newText(new Offset(0, 0, pseudoCode17, "SW"), "      newcategory(record)", "pseudocode", null);
		pseudoCode19 = l.newText(new Offset(0, 0, pseudoCode18, "SW"), "   else if merge(best1, best2) yields best CU then", "pseudocode", null);
		pseudoCode20 = l.newText(new Offset(0, 0, pseudoCode19, "SW"), "      merge(best1, best2)", "pseudocode", null);
		pseudoCode21 = l.newText(new Offset(0, 0, pseudoCode20, "SW"), "      COBWEB(merged, record)", "pseudocode", null);
		pseudoCode22 = l.newText(new Offset(0, 0, pseudoCode21, "SW"), "   else if split(best1) yields best CU then", "pseudocode", null);
		pseudoCode23 = l.newText(new Offset(0, 0, pseudoCode22, "SW"), "      split(best1)", "pseudocode", null);
		pseudoCode24 = l.newText(new Offset(0, 0, pseudoCode23, "SW"), "      root.remove(record)", "pseudocode", null);
		pseudoCode25 = l.newText(new Offset(0, 0, pseudoCode24, "SW"), "      COBWEB(root, record)", "pseudocode", null);
		pseudoCode26 = l.newText(new Offset(0, 0, pseudoCode25, "SW"), "   else", "pseudocode", null);
		pseudoCode27 = l.newText(new Offset(0, 0, pseudoCode26, "SW"), "      COBWEB(best1, record)", "pseudocode", null);
		pseudoCode28 = l.newText(new Offset(0, 0, pseudoCode27, "SW"), "// END", "pseudocode", null, comment);

		// Anlegen des Trainingsdaten-Arrays
		MatrixProperties mp = new MatrixProperties();
		mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		mp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, cream);
		mp.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, bordeaux);
//		mp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, bordeaux);
		trainingsdaten = l.newText(new Offset(350, 0, pseudoCode1, "NE"), "Trainingsdaten", "Trainingsdaten", null, ueberschrift);
		IntMatrix Trainingsdata = l.newIntMatrix(new Offset(0, 15, trainingsdaten, "SW"), TrainingsData, "trainingsdaten", null, mp);
		
		// Anlegen der �berschriften
		baumknotenlables = l.newText(new Offset(75, -40, Trainingsdata, "NE"), "Baumknotenlables", "test", null, ueberschrift);			
		tree = l.newText(new Offset(100+25*Trainingsdata.getNrCols(), 0, baumknotenlables, "NE"), "COBWEB Baum", "tree", null, ueberschrift);
		cu = l.newText(new Offset(0, 10, pseudoCode28, "SW"), "Category Utility", "tree", null, ueberschrift);
		
		// Algorithmus ausf�hren
		theRoot = new CobwebCategory();
		for(int x = 0; x < TrainingsData.length; x++) {
			for(int y = 0; y < TrainingsData[0].length; y++) {
				Trainingsdata.highlightCell(x, y, null, null);
			}
			l.nextStep();
			
			cobweb(theRoot, TrainingsData[x]);
			drawtree(theRoot);			
			l.nextStep();
			
			for(int y = 0; y < TrainingsData[0].length; y++) {
				Trainingsdata.unhighlightCell(x, y, null, null);
			}
		}
		
		l.finalizeGeneration();
		return l.toString();
    }


	public static double calcCU(CobwebCategory root) {
		double n = root.getChildren().size();		// n -> number of children-categories
		double s = root.getRecords().size();	// s -> number of samples
		
		ArrayList<Double> pC = new ArrayList<Double>();	// category prior probability of all categories
		calc_pC(root, s, pC);
		
		ArrayList<double[][]> pAC = new ArrayList<double[][]>();
		calc_pAC(root, pAC);
		
		double cu = 0;
		double tmp1 = 0;
		double tmp2 = 0;
		double tmp3 = 0;
		for(int k = 0; k < pC.size(); k++) {
			tmp1 = 0;
			for(int i = 0; i < pAC.get(0)[0].length; i++) {
				for(int j = 0; j < 2; j++) {
					tmp1 += pAC.get(k)[j][i] * pAC.get(k)[j][i];
				}
			}
			tmp2 = 0;
			for(int i = 0; i < pAC.get(0)[0].length; i++) {
				for(int j = 0; j < 2; j++) {
					tmp3 = 0;
					for(int c = 0; c < pC.size(); c++) {
						tmp3 += pAC.get(c)[j][i] * pC.get(c);
					}
					tmp2 += tmp3 * tmp3;
				}
			}
			cu += pC.get(k) * (tmp1 - tmp2);
		}
		
		return cu / n;
	}
	
	/**
	 * Calculates the category prior probability for all categories, starting with the given and then for all descendants
	 * @param category	starting category
	 * @param s			number of samples in the Cobweb tree
	 * @param pC		ArrayList to save each category prior probability in
	 */
	public static void calc_pC(CobwebCategory category, double s, ArrayList<Double> pC) {
		double pC_k;
		if(!category.getChildren().isEmpty()) {
			for(CobwebCategory child : category.getChildren()) {
				pC_k = child.getRecords().size() / s;
				pC.add(pC_k);
			}
		}		
	}
	
	/**
	 * Calculates the likelihood for all categories, starting with the given and then for all descendants
	 * @param category	starting category
	 * @param pAC		ArrayList to save each categories likelihood in
	 */
	public static void calc_pAC(CobwebCategory category, ArrayList<double[][]> pAC) {
		double[][] pAC_k;
		if(!category.getChildren().isEmpty()) {
			for(CobwebCategory child : category.getChildren()) {
				pAC_k = new double[2][child.categoryLikelihood1().length];
				pAC_k[0] = child.categoryLikelihood0();
				pAC_k[1] = child.categoryLikelihood1();
				pAC.add(pAC_k);
			}
		}
	}
	
	// =============================================================================================================================================================================================================================
	/**
	 * @param root		the root category of the COBWEB (sub-) tree in which the record is to be inserted
	 * @param record	the record to be inserted into the COBWEB (sub-) tree
	 */
	public static void cobweb(CobwebCategory root, int[] record) {
		pseudoCode3.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		
		// Baum zeichnen
		try {
			try {
				drawtree(theRoot);
			} catch (IndexOutOfBoundsException e) {}
		} catch (NullPointerException e) {}
				
		// Aktuellen Knoten highlighten
		try {
			for(int i = 0; i < treeList.size(); i++) {
				if(root.equals(treeList.get(i))) {
					graf.setNodeHighlightFillColor(i, cream, null, null);
					graf.highlightNode(i, null, null);
				}
			}
		} catch (NullPointerException e) {}
		
		// CU Tabelle verstecken
		try {
			cuTable.hide();
			l.nextStep();
		} catch (NullPointerException e) {}
		// CU Tabelle neu zeichnen
		String[][] cuTableTmp = new String[6][2];
		cuTableTmp[0][0] = "Operation";
		cuTableTmp[0][1] = "Resulting CU";
		cuTableTmp[1][0] = "Insert in best1";
		cuTableTmp[1][1] = "";
		cuTableTmp[2][0] = "Insert in best2";
		cuTableTmp[2][1] = "";
		cuTableTmp[3][0] = "Insert new Node";
		cuTableTmp[3][1] = "";
		cuTableTmp[4][0] = "Merge best1 & best2";
		cuTableTmp[4][1] = "";
		cuTableTmp[5][0] = "Split best1";
		cuTableTmp[5][1] = "";
		cuTable = l.newStringMatrix(new Offset(0, 0, cu, "SW"), cuTableTmp, "trainingsdaten", null);		
		l.nextStep();
		
		// Debug Output
		System.out.print("Current record: ");
		for(int i = 0; i < record.length; i++) {
			System.out.print(record[i]);
		}
		//System.out.println("");
		
		// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		// if root has no children
		pseudoCode3.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
		pseudoCode3a.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		l.nextStep();
		if(root.getChildren().isEmpty()) {	
			// if root has no records initialize the root
			pseudoCode3a.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode3b.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			l.nextStep();
			if(root.getRecords().isEmpty()) {
				pseudoCode3b.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				pseudoCode4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				root.addRecord(record);
				l.nextStep();
				
				pseudoCode4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				return;
			}
			// if root already contains the record just add it again, no need for child creating
			for(int[] rec : root.getRecords()) {
				if(Arrays.equals(rec, record)) {
					pseudoCode3b.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					pseudoCode4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					root.addRecord(record);
					l.nextStep();					
					
					pseudoCode4.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					return;
				}
			}

			pseudoCode3b.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode7.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			l.nextStep();
			// clone root and add clone to root's children
			pseudoCode7.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode8.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			CobwebCategory clone = new CobwebCategory();
			ArrayList<int[]> cloneRecords =  new ArrayList<int[]>();
			cloneRecords.addAll(root.getRecords());
			clone.setRecords(cloneRecords);
			root.addChild(clone);
			l.nextStep();
			
			// create new category for record and add to root's children
			pseudoCode8.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode9.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			CobwebCategory newChild = new CobwebCategory();
			newChild.addRecord(record);
			root.addChild(newChild);
			l.nextStep();
			
			// add record to root
			pseudoCode9.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode10.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			root.addRecord(record);
			l.nextStep();
			
			pseudoCode10.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			return;
		
		// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		} else {
			pseudoCode3a.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode11.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			l.nextStep();
			
			// add record to root
			pseudoCode11.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode12.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			root.addRecord(record);
			l.nextStep();
			
			// check for child that already contains the record
			pseudoCode12.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode5.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			l.nextStep();
			for(CobwebCategory child : root.getChildren()) {
				for(int[] rec : child.getRecords()) {
					if(Arrays.equals(rec, record)) {
						pseudoCode5.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
						pseudoCode6.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
						l.nextStep();
						
						pseudoCode6.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
						cobweb(child, record);
						return;
					}
				}
			}
			
			// find 2 children with the best CU if record was inserted into them
			CobwebCategory first = null;
			double firstCU = - Double.MAX_VALUE;
			CobwebCategory second = null;
			double secondCU = - Double.MAX_VALUE;
			double tmpCU;
			for(CobwebCategory child : root.getChildren()) {
				pseudoCode5.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				pseudoCode13.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				l.nextStep();
				
				child.addRecord(record);
				pseudoCode13.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				pseudoCode14.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);				
				tmpCU = calcCU(root);
				if(firstCU < tmpCU) {
					secondCU = firstCU;
					second = first;
					firstCU = tmpCU;
					first = child;
				} else if(secondCU < tmpCU) {
					secondCU = tmpCU;
					second = child;
				}
				child.removeRecord(record);
				l.nextStep();
				pseudoCode14.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			}
			pseudoCode15.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			l.nextStep();
			
			//System.out.println("firstCU:  " + firstCU);
			//System.out.println("secondCU: " + secondCU);
			pseudoCode15.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode16.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			cuTableTmp[1][0] = "Insert in best1";
			cuTableTmp[1][1] = Double.toString(firstCU);
			cuTableTmp[2][0] = "Insert in best2";
			cuTableTmp[2][1] = Double.toString(secondCU);
			cuTable.hide();
			l.nextStep();
			cuTable = l.newStringMatrix(new Offset(0, 0, cu, "SW"), cuTableTmp, "trainingsdaten", null);
			// best1 Knoten highlighten
			try {
				for(int i = 0; i < treeList.size(); i++) {
					if(first.equals(treeList.get(i))) {
						graf.setNodeHighlightFillColor(i, cloud, null, null);
						graf.highlightNode(i, null, null);
					}
				}
			} catch (NullPointerException e) {}
			l.nextStep();			
			
			// check if new category for the record would result in the best CU
			pseudoCode16.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode17.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			ArrayList<CobwebCategory> oldChildren = new ArrayList<CobwebCategory>();
			oldChildren.addAll(root.getChildren());
			CobwebCategory newChild = new CobwebCategory();
			newChild.addRecord(record);
			root.addChild(newChild);
			// check CU
			double newCU = calcCU(root);
			//System.out.println("newCU:    " + newCU);
			cuTableTmp[3][1] = Double.toString(newCU);
			cuTable.hide();
			l.nextStep();
			cuTable = l.newStringMatrix(new Offset(0, 0, cu, "SW"), cuTableTmp, "trainingsdaten", null);
			l.nextStep();
			if(firstCU < newCU) {
				// first question encounter
				if(qc1) {
					MultipleChoiceQuestionModel insertQuestion = new MultipleChoiceQuestionModel("insertQuestion");
					insertQuestion.setPrompt("Wie laeuft der Algorithmus nun weiter?");
					insertQuestion.addAnswer("Es wird die Category Utility (CU) f�r das Verschmelzen der beiden besten Kinder brechnet.", 1,
							"Flasch. Die CU des Einfuegends einer neue Kategorie ist h�her als die CU des die Hierarchie Hinabzusteigens. "
							+ "Deshalb wird ersteres ausgefuehrt, weitere Berchnungen sind nicht notwenig.");
					insertQuestion.addAnswer("Eine neue Kategorie wird mit dem aktuellen Objekt gefuellt und eingefuegt.", 2, "Korrekt!");
					insertQuestion.addAnswer("Es wird die CU f�r die Spaltung des besten Kindes brechnet, "
							+ "da as Verschmelzen der beiden besten Kinder in diesem Fall nicht moeglich ist.", 3, 
							"Flasch. Die CU des Einfuegends einer neue Kategorie ist h�her als die CU des die Hierarchie Hinabzusteigens. "
							+ "Deshalb wird ersteres ausgefuehrt, weitere Berchnungen sind nicht notwenig.");
					l.addMCQuestion(insertQuestion);					
					l.nextStep();
					try {
						if(insertQuestion.getUserAnwerID().equals("2")) {
							qc1 = false;
						}
					} catch(NullPointerException e) {}					
				}
				
				pseudoCode17.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				pseudoCode18.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				l.nextStep();
				
				pseudoCode18.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
				return;
			} else {
				root.setChildren(oldChildren);
			}
			
			// check if merging the two best-fitting categories yields the best CU
			pseudoCode17.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode19.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			if(second != null) {
				// create category for merging
				CobwebCategory newMergeChild = new CobwebCategory();
				// summarize the attribute-value counts of the nodes being merged
				ArrayList<int[]> mergedRecords = new ArrayList<int[]>();
				mergedRecords.addAll(first.getRecords());
				mergedRecords.addAll(second.getRecords());
				newMergeChild.setRecords(mergedRecords);
				// add the two categories to merge as children
				ArrayList<CobwebCategory> mergedChildren = new ArrayList<CobwebCategory>();
				mergedChildren.add(first);
				mergedChildren.add(second);
				newMergeChild.setChildren(mergedChildren);
				// update root
				ArrayList<CobwebCategory> newMergeChildren = new ArrayList<CobwebCategory>();
				newMergeChildren.addAll(oldChildren);
				newMergeChildren.remove(first);
				newMergeChildren.remove(second);
				newMergeChildren.add(newMergeChild);
				root.setChildren(newMergeChildren);
				// check CU
				root.removeRecord(record);
				double mergCU = calcCU(root);
				root.addRecord(record);
				//System.out.println("mergeCU:  " + mergCU);
				cuTableTmp[4][1] = Double.toString(mergCU);
				cuTable.hide();
				l.nextStep();
				cuTable = l.newStringMatrix(new Offset(0, 0, cu, "SW"), cuTableTmp, "trainingsdaten", null);
				l.nextStep();				
				if(firstCU < mergCU) {
					// second question encounter
					if(qc2) {
						MultipleChoiceQuestionModel mergeQuestion = new MultipleChoiceQuestionModel("mergeQuestion");
						mergeQuestion.setPrompt("Wie laeuft der Algorithmus nun weiter?");
						mergeQuestion.addAnswer("Die beiden Kinder mit der besten Category Utility (CU) werden verschmolzen.", 1, "Korrekt!");
						mergeQuestion.addAnswer("Die CU fuer die Spaltung des beste Kind wird berechent", 2, 
								"Falsch. Die CU des Verschmelzens der beiden besten Kinder ist h�her als die CU des die Hierarchie Hinabzusteigens. "
								+ "Deshalb wird ersteres ausgefuehrt, weitere Berchnungen sind nicht notwenig.");
						mergeQuestion.addAnswer("Wir steigen die Hierarchie hinab, da die Berechnung der Spaltung des besten Kindes in diesem Fall nicht moeglich ist.", 3, 
								"Falsch. Die CU des Verschmelzens der beiden besten Kinder ist h�her als die CU des die Hierarchie Hinabzusteigens. "
								+ "Deshalb wird ersteres ausgefuehrt, weitere Berchnungen sind nicht notwenig.");
						l.addMCQuestion(mergeQuestion);					
						l.nextStep();
						try {
							if(mergeQuestion.getUserAnwerID().equals("1")) {
								qc2 = false;
							}
						} catch(NullPointerException e) {}					
					}
					
					pseudoCode19.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					pseudoCode20.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					l.nextStep();
					
					pseudoCode20.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					pseudoCode21.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					l.nextStep();
					
					pseudoCode21.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);					
					cuTable.hide();
					l.nextStep();
					cobweb(newMergeChild, record);
					return;
				} else {
					root.setChildren(oldChildren);
				}
			} else {
				cuTableTmp[4][1] = "not possible";
				cuTable.hide();
				l.nextStep();
				cuTable = l.newStringMatrix(new Offset(0, 0, cu, "SW"), cuTableTmp, "trainingsdaten", null);
				l.nextStep();
			}
			
			
			// check if splitting the best-fitting category yields the best CU
			pseudoCode19.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode22.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			if(!first.getChildren().isEmpty()) {				
				ArrayList<CobwebCategory> newSplitChildren = new ArrayList<CobwebCategory>();
				newSplitChildren.addAll(oldChildren);
				newSplitChildren.addAll(first.getChildren());
				newSplitChildren.remove(first);
				root.setChildren(newSplitChildren);
				// check CU
				root.removeRecord(record);
				double splitCU = calcCU(root);
				root.addRecord(record);
				//System.out.println("splitCU:  " + splitCU);
				cuTableTmp[5][1] = Double.toString(splitCU);
				cuTable.hide();
				l.nextStep();
				cuTable = l.newStringMatrix(new Offset(0, 0, cu, "SW"), cuTableTmp, "trainingsdaten", null);
				l.nextStep();
				if(firstCU < splitCU) {
					// third question encounter
					if(qc3) {
						MultipleChoiceQuestionModel splitQuestion = new MultipleChoiceQuestionModel("splitQuestion");
						splitQuestion.setPrompt("Wie laeuft der Algorithmus nun weiter?");
						splitQuestion.addAnswer("Wir steigen die Hierarchie hinab, da die Spaltung des besten Kindes unnoetig waere.", 1, 
								"Falsch. Die Spaltung des besten Kindes liefert die beste Category Utility und wird deshalb selbstverstaendlich ausgefuehrt.");
						splitQuestion.addAnswer("Das beste Kind wird gespalten.", 2, "Korrekt!");
						l.addMCQuestion(splitQuestion);					
						l.nextStep();
						try {
							if(splitQuestion.getUserAnwerID().equals("2")) {
								qc3 = false;
							}
						} catch(NullPointerException e) {}					
					}
					
					pseudoCode22.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					pseudoCode23.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					l.nextStep();
					
					pseudoCode23.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					pseudoCode24.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					root.removeRecord(record);
					l.nextStep();
					
					pseudoCode24.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					pseudoCode25.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
					l.nextStep();
					
					pseudoCode25.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
					cuTable.hide();
					l.nextStep();
					cobweb(root, record);
					return;
				} else {
					root.setChildren(oldChildren);
				}
			} else {
				// fourth question encounter
				if(qc4) {
					MultipleChoiceQuestionModel splitPossibleQuestion = new MultipleChoiceQuestionModel("splitPossibleQuestion");
					splitPossibleQuestion.setPrompt("Wie laeuft der Algorithmus nun weiter?");
					splitPossibleQuestion.addAnswer("Wir steigen die Hierarchie hinab, da die Spaltung des besten Kindes nicht moeglich ist.", 1, 
							"Korrekt! Das beste Kind hat keine Kinder, deshalb ist die Spaltung des besten Kindes nicht moeglich.");
					splitPossibleQuestion.addAnswer("Die Category Utility (CU) fuer die Spaltung des besten Kindes wird berechnet", 2, 
							"Falsch. Das beste Kind hat keine Kinder, deshalb ist die Spaltung des besten Kindes nicht moeglich.");
					l.addMCQuestion(splitPossibleQuestion);					
					l.nextStep();
					try {
						if(splitPossibleQuestion.getUserAnwerID().equals("1")) {
							qc4 = false;
						}
					} catch(NullPointerException e) {}					
				}
				
				cuTableTmp[5][1] = "not possible";
				cuTable.hide();
				l.nextStep();
				cuTable = l.newStringMatrix(new Offset(0, 0, cu, "SW"), cuTableTmp, "trainingsdaten", null);
				l.nextStep();
			}
			
			// if none of the above options yields a better CU
			pseudoCode22.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode26.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			l.nextStep();
			
			pseudoCode26.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			pseudoCode27.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) PseudoCodeHighlighting.get( AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			l.nextStep();
			
			pseudoCode27.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
			cuTable.hide();
			cobweb(first, record);	
		}
	}
	// =============================================================================================================================================================================================================================


	//Baum zeichnen
	public static void drawtree(CobwebCategory root) {
		int nodeCounter = root.countNodes();								// Anzahl der Kategorien/Graphknoten
		String[] texte = new String[nodeCounter];							// Texte f�r die Graphknoten
		int[][] adjazensmatrix = new int[nodeCounter][nodeCounter];			// Adjazensmatrix, beschreibt Kanten zwischen Knoten
		Node[] nodes = new Node[nodeCounter];								// Positionen der Knoten
		treeList = root.treeToList();				// Cobweb-Baum level-weise in einer Liste
		ArrayList<ArrayList<CobwebCategory>> layerList = root.layerList();	// Cobweb-Baum in einer Liste die f�r jedes Level eine Unter-Liste enth�lt
		
		//Verberge vorhergezeichnetes, damit Linien und Beschreibung nicht doppelt auftauchen
		try {
			graf.hide();
			nodeTexts.hide();
			l.nextStep();
		} catch (NullPointerException e) {
			//System.out.println("Erste Iteration");
		}
				
		// Knotentexte schreiben
		String[][] likelitable = new String[nodeCounter+1][3];
		likelitable[0][0] = "Knoten";
		likelitable[0][1] = "Anz. enthaltener Objekte";
		likelitable[0][2] = "Likelihood";
		for(int i = 0; i < treeList.size(); i++) {
			texte[i] = Integer.toString(i);		// Knoten nummerieren
			// likelihood runden
			double[] likeli = treeList.get(i).categoryLikelihood1();
			for(int j = 0; j < likeli.length; j++) {
				likeli[j] = Math.round(likeli[j] * 100.0) / 100.0;
			}
			// Beschreibungstabelle f�llen
			likelitable[i+1][0] = texte[i];
			likelitable[i+1][1] = Integer.toString(treeList.get(i).getRecords().size());
			likelitable[i+1][2] = Arrays.toString(likeli);
		}
		nodeTexts = l.newStringMatrix(new Offset(0, 0, baumknotenlables, "SW"), likelitable, "trainingsdaten", null);
			
		// Adjazensmatrix f�llen
		for(int i = 0; i < nodeCounter - 1; i++) {
			int j = i + 1;
			while(j < nodeCounter) {
				if(treeList.get(i).isParentOf(treeList.get(j))) {
					adjazensmatrix[i][j] = 1;
				}
				j++;
			}
		}
		
		// nodes positionieren
		int x = 0;
		for(int i = 0; i < layerList.size(); i++) {
			for(int j = 0; j < layerList.get(i).size(); j++) {
				nodes[x] = new Offset(100*j + 30, 100*i + 60, tree, "SO");
				x++;
			}
		}
		
		
		graf = l.newGraph("Der GRAF", adjazensmatrix, nodes, texte, null);
		
		// passe Stelle des pseudoCodes an: Zumindest kann man so messen, wie weit der PseudoCode zur Seite muss. Das hier ist aber nicht
		// Die L�sung. 
		//
		// pseudoCode1 = l.newText(new Coordinates(layerList.get(layerList.size() -1).size()*200, 60), "PseudoCode", "test", null, ueberschrift);		

		//for all Nodes, set NodeFillColor = White 
		for(int i = 0; i < nodeCounter; i++) {
			graf.setNodeHighlightFillColor(i, Color.WHITE, null, null);
			//g.setNodeLabel(i, ""+ intMatrix[i][0], null, null); //�berLabel k�nnten wir an die Richtige Stelle den richtigen Inhalt schreiben. M�ssen das nicht �ber das Array machen
			graf.setNodeRadius(i, 30, null, null);
			graf.setNodeHighlightTextColor(i, bordeaux, null, null);
			graf.highlightNode(i, null, null);
		}


	}
	
	
    
    
    public String getName() {
        return "COBWEB Clustering [DE]";
    }

    public String getAlgorithmName() {
        return "COBWEB";
    }

    public String getAnimationAuthor() {
        return "J. H. Mieseler, T. G. Petry";
    }

    public String getDescription(){
        return "Im Bereich des maschinellen Lernens ist der COBWEB Alogrithmus ein inkrementelles System fuer hierarchisch konzeptionelles Clustering."
 +"\n"
 +"Der COBWEB Alogrithmus wurde von Professor Douglas H. Fisher entwickelt."
 +"\n"
 +"Die folgende Visualisierung wird sich mit dem grundlegenden COBWEB Alogrithmus beschaeftigen, mehr Informationen dazu am Beginn der Visualisierung."
 +"\n"
 +"\n"
 +"F�r detaillierte Informationen zum Thema COBWEB moechten wir Ihnen die urspruengliche Arbeit von Professor Fisher ans Herz legen:"
 +"\n"
 +"\"Fisher, D. H. (1987). Knowledge acquisition via incremental conceptual clustering. Machine learning, 2(2), 139-172.\""
 +"\n"
 +"https://link.springer.com/content/pdf/10.1007/BF00114265.pdf"
 +"\n"
 +"\n"
 +"Hinweis:"
 +"\n"
 +"COBWEB nutzt nominelle Attributwerte, aber zur Vereinfachung sind in dieser Visualisierung nur die Attributwerte 0 und 1 erlaubt, was bedeutet das Objekt besitzt das Attribut oder eben nicht."
 +"\n"
 +"Beachten Sie dies beim Bearbeiten der Trainingsdaten!";
    }

    public String getCodeExample(){
        return "/**"
 +"\n"
 +" * @param root		the root category of the COBWEB (sub-) tree in which the record is to be inserted"
 +"\n"
 +" * @param record	the record to be inserted into the COBWEB (sub-) tree"
 +"\n"
 +" */" 
 +"\n"
 +"public static void cobweb(CobwebCategory root, int[] record) {	"
 +"\n"
 +"	System.out.print(\"Current record: \");"
 +"\n"
 +"	for(int i = 0; i < record.length; i++) {"
 +"\n"
 +"		System.out.print(record[i]);"
 +"\n"
 +"	}"
 +"\n"
 +"	System.out.println(\"\");"
 +"\n"
 +"	System.out.println(\"current category: \" + root);"
 +"\n"
 +"			"
 +"\n"
 +"	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
 +"\n"
 +"	// if root has no children"
 +"\n"
 +"	if(root.getChildren().isEmpty()) {	"
 +"\n"
 +"		// if root has no records initialize the root"
 +"\n"
 +"		if(root.getRecords().isEmpty()) {"
 +"\n"
 +"			root.addRecord(record);"
 +"\n"
 +"			return;"
 +"\n"
 +"		}"
 +"\n"
 +"		// if root already contains the record just add it again, no need for child creating"
 +"\n"
 +"		for(int[] rec : root.getRecords()) {"
 +"\n"
 +"			if(Arrays.equals(rec, record)) {"
 +"\n"
 +"				root.addRecord(record);"
 +"\n"
 +"				return;"
 +"\n"
 +"			}"
 +"\n"
 +"		}"
 +"\n"
 +"		// clone root and add clone to root's children"
 +"\n"
 +"		CobwebCategory clone = new CobwebCategory();"
 +"\n"
 +"		ArrayList<int[]> cloneRecords =  new ArrayList<int[]>();"
 +"\n"
 +"		cloneRecords.addAll(root.getRecords());"
 +"\n"
 +"		clone.setRecords(cloneRecords);"
 +"\n"
 +"		root.addChild(clone);"
 +"\n"
 +"		// create new category for record and add to root's children"
 +"\n"
 +"		CobwebCategory newChild = new CobwebCategory();"
 +"\n"
 +"		newChild.addRecord(record);"
 +"\n"
 +"		root.addChild(newChild);"
 +"\n"
 +"		// add record to root"
 +"\n"
 +"		root.addRecord(record);"
 +"\n"
 +"		return;"
 +"\n"
 +"	"
 +"\n"
 +"	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
 +"\n"
 +"	} else {"
 +"\n"
 +"		// add record to root"
 +"\n"
 +"		root.addRecord(record);"
 +"\n"
 +"		"
 +"\n"
 +"		// check for child that already contains the record"
 +"\n"
 +"		for(CobwebCategory child : root.getChildren()) {"
 +"\n"
 +"			for(int[] rec : child.getRecords()) {"
 +"\n"
 +"				if(Arrays.equals(rec, record)) {"
 +"\n"
 +"					cobweb(child, record);"
 +"\n"
 +"					return;"
 +"\n"
 +"				}"
 +"\n"
 +"			}"
 +"\n"
 +"		}"
 +"\n"
 +"		"
 +"\n"
 +"		// find 2 children with the best CU if record was inserted into them"
 +"\n"
 +"		CobwebCategory first = null;"
 +"\n"
 +"		double firstCU = - Double.MAX_VALUE;"
 +"\n"
 +"		CobwebCategory second = null;"
 +"\n"
 +"		double secondCU = - Double.MAX_VALUE;"
 +"\n"
 +"		double tmpCU;"
 +"\n"
 +"		for(CobwebCategory child : root.getChildren()) {"
 +"\n"
 +"			// ArrayList<int[]> oldRecords = child.getRecords();"
 +"\n"
 +"			child.addRecord(record);"
 +"\n"
 +"			tmpCU = calcCU(root);"
 +"\n"
 +"			if(firstCU < tmpCU) {"
 +"\n"
 +"				secondCU = firstCU;"
 +"\n"
 +"				second = first;"
 +"\n"
 +"				firstCU = tmpCU;"
 +"\n"
 +"				first = child;"
 +"\n"
 +"			} else if(secondCU < tmpCU) {"
 +"\n"
 +"				secondCU = tmpCU;"
 +"\n"
 +"				second = child;"
 +"\n"
 +"			}"
 +"\n"
 +"			child.removeRecord(record);"
 +"\n"
 +"		}"
 +"\n"
 +"		System.out.println(\"firstCU:  \" + firstCU);"
 +"\n"
 +"		System.out.println(\"secondCU: \" + secondCU);"
 +"\n"
 +"		"
 +"\n"
 +"		"
 +"\n"
 +"		// check if new category for the record would result in the best CU"
 +"\n"
 +"		ArrayList<CobwebCategory> oldChildren = new ArrayList<CobwebCategory>();"
 +"\n"
 +"		oldChildren.addAll(root.getChildren());"
 +"\n"
 +"		CobwebCategory newChild = new CobwebCategory();"
 +"\n"
 +"		newChild.addRecord(record);"
 +"\n"
 +"		root.addChild(newChild);"
 +"\n"
 +"		// check CU"
 +"\n"
 +"		double newCU = calcCU(root);"
 +"\n"
 +"		System.out.println(\"newCU:    \" + newCU);"
 +"\n"
 +"		if(firstCU < newCU) {"
 +"\n"
 +"			return;"
 +"\n"
 +"		} else {"
 +"\n"
 +"			root.setChildren(oldChildren);"
 +"\n"
 +"		}"
 +"\n"
 +"		"
 +"\n"
 +"		// check if merging the two best-fitting categories yields the best CU"
 +"\n"
 +"		if(second != null) {"
 +"\n"
 +"			// create category for merging"
 +"\n"
 +"			CobwebCategory newMergeChild = new CobwebCategory();"
 +"\n"
 +"			// summarize the attribute-value counts of the nodes being merged"
 +"\n"
 +"			ArrayList<int[]> mergedRecords = new ArrayList<int[]>();"
 +"\n"
 +"			mergedRecords.addAll(first.getRecords());"
 +"\n"
 +"			mergedRecords.addAll(second.getRecords());"
 +"\n"
 +"			newMergeChild.setRecords(mergedRecords);"
 +"\n"
 +"			// add the two categories to merge as children"
 +"\n"
 +"			ArrayList<CobwebCategory> mergedChildren = new ArrayList<CobwebCategory>();"
 +"\n"
 +"			mergedChildren.add(first);"
 +"\n"
 +"			mergedChildren.add(second);"
 +"\n"
 +"			newMergeChild.setChildren(mergedChildren);"
 +"\n"
 +"			// update root"
 +"\n"
 +"			ArrayList<CobwebCategory> newMergeChildren = new ArrayList<CobwebCategory>();"
 +"\n"
 +"			newMergeChildren.addAll(oldChildren);"
 +"\n"
 +"			newMergeChildren.remove(first);"
 +"\n"
 +"			newMergeChildren.remove(second);"
 +"\n"
 +"			newMergeChildren.add(newMergeChild);"
 +"\n"
 +"			root.setChildren(newMergeChildren);"
 +"\n"
 +"			// check CU"
 +"\n"
 +"			root.removeRecord(record);"
 +"\n"
 +"			double mergCU = calcCU(root);"
 +"\n"
 +"			root.addRecord(record);"
 +"\n"
 +"			System.out.println(\"mergeCU:  \" + mergCU);"
 +"\n"
 +"			if(firstCU < mergCU) {"
 +"\n"
 +"				cobweb(newMergeChild, record);"
 +"\n"
 +"				return;"
 +"\n"
 +"			} else {"
 +"\n"
 +"				root.setChildren(oldChildren);"
 +"\n"
 +"			}"
 +"\n"
 +"		}"
 +"\n"
 +"		"
 +"\n"
 +"		// check if splitting the best-fitting category yields the best CU"
 +"\n"
 +"		if(!first.getChildren().isEmpty()) {"
 +"\n"
 +"			// new children for root"
 +"\n"
 +"			ArrayList<CobwebCategory> newSplitChildren = new ArrayList<CobwebCategory>();"
 +"\n"
 +"			newSplitChildren.addAll(oldChildren);"
 +"\n"
 +"			newSplitChildren.addAll(first.getChildren());"
 +"\n"
 +"			newSplitChildren.remove(first);"
 +"\n"
 +"			// update root"
 +"\n"
 +"			root.setChildren(newSplitChildren);"
 +"\n"
 +"			// check CU"
 +"\n"
 +"			root.removeRecord(record);"
 +"\n"
 +"			double splitCU = calcCU(root);"
 +"\n"
 +"			root.addRecord(record);"
 +"\n"
 +"			System.out.println(\"splitCU:  \" + splitCU);"
 +"\n"
 +"			if(firstCU < splitCU) {"
 +"\n"
 +"				root.removeRecord(record);"
 +"\n"
 +"				cobweb(root, record);"
 +"\n"
 +"				return;"
 +"\n"
 +"			} else {"
 +"\n"
 +"				root.setChildren(oldChildren);"
 +"\n"
 +"			}"
 +"\n"
 +"		}"
 +"\n"
 +"		"
 +"\n"
 +"		// if none of the above options yields a better CU"
 +"\n"
 +"		cobweb(first, record);			"
 +"\n"
 +"	}"
 +"\n"
 +"}";
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