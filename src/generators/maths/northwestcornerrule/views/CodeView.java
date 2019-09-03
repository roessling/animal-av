package generators.maths.northwestcornerrule.views;

import generators.maths.northwestcornerrule.AnimProps;
import generators.maths.northwestcornerrule.DrawingUtils;
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
		sc.addCodeLine("public int nordwestEckenRegel", null, 0, null);  // 0
		sc.addCodeLine(" (int[] angebot, int[] nachfrage){", null, 0, null); 
		sc.addCodeLine("int i = 0;", null, 1, null); 
		sc.addCodeLine("int j = 0;", null, 1, null);  // 3
		sc.addCodeLine("while (i<= angebot.length ", null, 1, null);  // 4
		sc.addCodeLine("&& j <=nachfrage.length) {", null, 1, null);  // 5
		sc.addCodeLine("x = Math.min(angebot[i],nachfrage[i]);", null, 2, null);  // 6
		sc.addCodeLine("saveToBasis(i, j, x);", null, 2, null);  // 7
		sc.addCodeLine("angebot[i] -= x;", null, 2, null); // 8
		sc.addCodeLine("nachfrage[j] -= x;", null, 2, null); // 9
		sc.addCodeLine("if(angebot[i] == 0)", null, 2, null); // 10
		sc.addCodeLine("i++;", null, 3, null); // 11
		sc.addCodeLine("else", null, 2, null); // 12
		sc.addCodeLine("j++;", null, 3, null); // 13
		sc.addCodeLine("}", null, 1, null); // 14
		sc.addCodeLine("}", null, 0, null); // 15
		    
	}
	
	public void highlight(int line){
		unhighlightAll();
		sc.highlight(line);
	}
	
	public void highlight(int line, int line2){
		highlight(line);
		sc.highlight(line2);
	}
	
	public void unhighlightAll(){
		for(int i=0; i<=15; i++){
			sc.unhighlight(i);
		}
	}

}
