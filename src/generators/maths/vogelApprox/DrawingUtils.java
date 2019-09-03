package generators.maths.vogelApprox;

import generators.maths.vogelApprox.AnimProps;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class DrawingUtils {
	
	private static int counter = 0;
	private Language myAnimationScript;

	public DrawingUtils(Language animationScript) {
		myAnimationScript = animationScript;
	}
	
	public void drawHeader(Coordinates c, String content){
		
		counter++;
		String randomName = "header" + counter;
		myAnimationScript.newText(c, content,randomName, null, AnimProps.HEADER_PROPS);    
		myAnimationScript.newRect(new Offset(-5, -5, randomName,
		        AnimalScript.DIRECTION_NW), new Offset(5, 5, randomName, "SE"), "",
		        null, AnimProps.RECT_HEADER_PROPS);
	}
	
	public void drawTextWithBox(Coordinates c, String content){
		
		counter++;
		String randomName = "textbox" + counter;
		myAnimationScript.newText(c, content,randomName, null, AnimProps.HEADER_PROPS);    
		myAnimationScript.newRect(new Offset(-50, -50, randomName,
		        AnimalScript.DIRECTION_NW), new Offset(50, 50, randomName, "SE"), "",
		        null, AnimProps.RECT_TEXTBOX_PROPS);
	}
	
	public void drawTextWithMinorBox(Coordinates c, String content){
		
		counter++;
		String randomName = "textboxMinor" + counter;
		myAnimationScript.newText(c, content,randomName, null, AnimProps.TXT_VAR_PROPS2);    
		myAnimationScript.newRect(new Offset(-5, -5, randomName,
		        AnimalScript.DIRECTION_NW), new Offset(5, 5, randomName, "SE"), "",
		        null, AnimProps.RECT_TEXTBOX2_PROPS);
	}
	
	public void drawBoxAroundObject(String name, int offset){
		myAnimationScript.newRect(new Offset(-offset, -offset, name,
		        AnimalScript.DIRECTION_NW), new Offset(offset, offset, name, "SE"), "",
		        null, AnimProps.RECT_TEXTBOX2_PROPS);
	}
	
	public void buildText(Coordinates c, String content){
		
		counter++;
		String[] contentArray = content.split("<br>");
		String randomName = "description" + counter;
		myAnimationScript.newText(c,contentArray[0],randomName, null, AnimProps.TXT_INTRO_PROPS);
		
		for(int i=1; i<contentArray.length; i++){
			myAnimationScript.newText(new Offset(0, 25, randomName,
			        AnimalScript.DIRECTION_NW),contentArray[i],
			        randomName +"_" + i, null, AnimProps.TXT_INTRO_PROPS);
			randomName = randomName + "_" + i;
		}
	}
	
	

}
