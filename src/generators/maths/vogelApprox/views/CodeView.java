package generators.maths.vogelApprox.views;


import generators.maths.vogelApprox.AnimProps;
import generators.maths.vogelApprox.DrawingUtils;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;

public class CodeView {
	
	private Language animationScript;
	private DrawingUtils myDrawingUtils;
	private SourceCode sc;
	
	
	public CodeView(Language animationScript, DrawingUtils myDrawingUtils){
		this.animationScript = animationScript;
		this.myDrawingUtils = myDrawingUtils;
	}
	
	public void setupView(){
		
		createCode();
		myDrawingUtils.drawBoxAroundObject(sc.getName(), 5);
	}
	
	public void createCode(){
		//create the source code entity
		sc = animationScript.newSourceCode(new Coordinates(10, 60), "sourceCode",
						   null, AnimProps.SC_PROPS);
		    
		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("1.  Berechne für jede (aktive) Zeile und Spalte der Kostenmatrix", null, 0, null);  // 0
		sc.addCodeLine("    die Differenz aus dem kleinsten (blau) und zweit-kleinsten (lila)", null, 0, null);
		sc.addCodeLine("    Element der entsprechenden Zeile/ Spalte.", null, 0, null);
		sc.addCodeLine("2.  Wähle die Zeile oder Spalte (grün) aus bei der sich die größte", null, 0, null); 
		sc.addCodeLine("    Differenz (blau) ergab.", null, 0, null);
		sc.addCodeLine("3.  Das kleinste Element der entsprechenden Spalte", null, 0, null); 
		sc.addCodeLine("    (bzw. Zeile) gibt nun die Stelle an, welche im", null, 0, null);
		sc.addCodeLine("    Transporttableau berechnet wird (blau).", null, 0, null);
		sc.addCodeLine("4.  Nun wird der kleinere Wert von Angebots- und", null, 0, null);  // 4
		sc.addCodeLine("    Nachfragevektor im Tableau eingetragen.", null, 0, null);
		sc.addCodeLine("5.  Anschließend wird der eingetragene Wert von den Rändern", null, 0, null);  // 5
		sc.addCodeLine("    abgezogen (mindestens einer muss 0 werden).  ", null, 0, null);
		sc.addCodeLine("6.  Ist nun der Wert im Nachfragevektor Null so markiere", null, 0, null);  // 6
		sc.addCodeLine("    die entsprechende Spalte in der Kostenmatrix. Diese", null, 0, null);
		sc.addCodeLine("    wird nun nicht mehr beachtet (rot). Ist der Wert des", null, 0, null);
		sc.addCodeLine("    Angebotsvektors Null markiere die Zeile der Kostenmatrix.", null, 0, null);
		sc.addCodeLine("7.  Der Algorithmus wird beendet, falls lediglich eine Zeile oder", null, 0, null); // 8
		sc.addCodeLine("    Spalte der Kostenmatrix unmarkiert ist (eines reicht aus).", null, 0, null);
		sc.addCodeLine("8 . Der entsprechenden Zeile bzw. Spalte im Transporttableau werden", null, 0, null); // 9		    
		sc.addCodeLine("    die restlichen Angebots- und Nachfragemengen zugeordnet.", null, 0, null);
		
	}
	
	public void highlight(int line){
		unhighlightAll();
		sc.highlight(line);
	}
	
	public void highlight(int from, int to){
		unhighlightAll();
		
		for(int i=from; i<=to; i++){
			sc.highlight(i);
		}

	}
	
	
	public void unhighlightAll(){
		for(int i=0; i<=19; i++){
			sc.unhighlight(i);
		}
	}

}
