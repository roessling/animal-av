package generators.misc.helpers;
import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Offset;

public class AdaptedSourceCode {
	private algoanim.primitives.SourceCode sc;

	private SourceCodeProperties scProps;
	private Language lang;
	private int stepNumber = -1;
	private int[][] StepLines_Iteration = {
			{7,8,9,10,11},
			{12,13,14,15,16},
			{17,18},
			{19,20,21,22},
			{23,24,25,26},
			{27,28},
			{}};
	
	private int[][] StepLines_Iteration_unHighlight = {
			{},
			{8,9,10,11},
			{12,13,14,15,16},
			{17,18},
			{19,20,21,22},
			{23,24,25,26},
			{27,28}};
	
	
  // private int[] StepLines_Description = {1,2,3,4,5};
	
	public AdaptedSourceCode(int x, int y,Language lang) {
		super();
		this.lang = lang;
		//this.scProps = scProps;
		init();
		// lang.newText(new Offset(x,	y, "dropCost_Header",AnimalScript.DIRECTION_NW), "","refPointCWD", null, infoLineProp);
		 
		this.sc =  this.lang.newSourceCode(new Offset(x,y, "dropCost_Header",AnimalScript.DIRECTION_NW), "sourceCode",
		        null, this.scProps);
		
		addCode();
		
		
	}

	private void init() {

		scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}
	
	private void addCode(){
		/* 1 */		sc.addCodeLine("Erläuterung:", null, 1, null);
		/* 2 */		sc.addCodeLine("Menge aller Lagerhäuser I", null, 2, null);
		/* 3 */		sc.addCodeLine("c_i1,j := Transportkosten von Kunde j zum 'nächstgelegenen' Lager i1", null, 2, null);
		/* 4 */		sc.addCodeLine("c_i2,j := Transportkosten von Kunde j zu 'übernöchstem' Lager i2", null, 2, null);
		/* 5 */		sc.addCodeLine("Fülle Tabellen mit Transportkosten und Warenhaus Fixkosten", null, 2, null);
		/* 6 */		sc.addCodeLine("", null, 1, null);
		/* 7 */		sc.addCodeLine("Iteration:", null, 1, null);
		/* 8 */		sc.addCodeLine("a) Minimale Kosten", null, 2, null);
		/* 9 */		sc.addCodeLine("Finde minimale Kosten c_i1, c_i2 für alle j und i aus I", null, 2, null);
		/* 10 */	sc.addCodeLine("Trage geringste  Kosten in Zeilen c_i1 und ", null,3, null);
		/* 11 */	sc.addCodeLine("zweitgeringste Kosten in Zeilen c_i2 ein", null,3, null);
		/* 12 */	sc.addCodeLine("b) Kostenerhöhung", null, 2, null);
		/* 13 */	sc.addCodeLine("Berechnung der Transportkostenerhöhung w_ij (zu Kunde j) bei ", null, 2, null);
		/* 14 */	sc.addCodeLine("Schließung von Lager i aus I:", null, 2, null);
		/* 15 */	sc.addCodeLine("w_ij = c_i2 - c_i1 , falls i = i1 (Differenz der geringsten und zweitgeringsten Kosten)", null, 3, null);
		/* 16 */	sc.addCodeLine("w_ij = 0           , sonst", null,3, null);
		/* 17 */	sc.addCodeLine("Bereche Transportkostenerhöhung bei Schließung des Lagers i", null, 2, null);
		/* 18 */	sc.addCodeLine("E_i = Summe_i (w_ij) für alle i aus I", null, 3, null);
		/* 19 */	sc.addCodeLine("c) Kostenersparniss", null, 2, null);
		/* 20 */	sc.addCodeLine("Berechne Gesamtkostenersparniss d_i bei Schließung des Lagers i", null, 2, null);
		/* 21 */	sc.addCodeLine("d_i = f_i - E_i    für alle nicht geschlossen Lager", null, 3, null);
		/* 22 */	sc.addCodeLine(" Falls d_i <= 0 dann bleibt Lager i immer offen", null, 3, null);
		/* 23 */	sc.addCodeLine("d) Schließen", null, 2, null);
		/* 24 */	sc.addCodeLine("Schließung des Lagers k mit der größten Einsparung", null, 2, null);
		/* 25 */	sc.addCodeLine("k = argmax {d_i} für die gilt d_i > 0 und i aus I", null, 3, null);
		/* 26 */	sc.addCodeLine("I := I /k", null, 3, null);
		/* 27 */	sc.addCodeLine("e) Abbruch?", null, 2, null);
		/* 28 */	sc.addCodeLine("falls d_i <= 0 für alle i aus I", null, 3, null);

		
		

	}
	
	public void hightlightLine(int i){
		sc.highlight(i-1);
		}
	
	public void hightlightLines(int[] i){
		for (int j : i) {
			sc.highlight(j-1);		
		}
	}
	
		
	public void unhightlightLine(int i){
	sc.unhighlight(i-1);
		}
	
	public void unhightlightLines(int[] i){
		for (int j : i) {
			sc.unhighlight(j-1);		
		}
	}
	
	
	
	public void hightlightNextStep(){
		
		switch (stepNumber) {
		case -1: 
				stepNumber++;	
				
				break;
		case 6 :
				stepNumber = 0 ;
				unhightlightLines(StepLines_Iteration_unHighlight[stepNumber]);
				break;

		default:
				stepNumber++;				
				unhightlightLines(StepLines_Iteration_unHighlight[stepNumber]);
				break;
		}
		hightlightLines(StepLines_Iteration[stepNumber]);
		
	}
	
	public void description(){
		hightlightLine(27);
		hightlightLine(28);
	}
	 
	
	public void hide(){
		sc.hide();
	}
	
	public void show(){
		sc.show();
	}
		
		

}
