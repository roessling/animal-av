package generators.maths.function;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * This class can parse and draw given functions
 * 
 * In order to draw a function call the drawFunction() method
 * It takes a function String. Syntax:
 * Sum (sigma) expression: 			\sum{from}{to}\
 * Multiplication(PI) expression:	\mult{from}{to}{condition}\
 * Index / Pow expression:			\index{base}{exponent}{index}\
 * Fraction:						\frac{numerator expression}{denominator expression}\
 * Dot:								\cdot\
 * e.g. "f(x)=\\sum{i=0}{N}\\ (5 \\cdot\\ \\index{x}{}{i}\\ - sqrt(x) )"
 * This function will also be parsed (except for Sum and Mult expressions) and saved in "parsed"
 * 
 * In order to just parse a function without drawing it you can call the method setParsed(String p):
 * Syntax:
 * sin(x), cos(x), tan(x), sqrt(x), pow(x,y), pi, e, +, -, *, /, (, )
 * e.g. "pow(x,3) + sin(x) - 2pi + 3*5 / (7+x)"
 * 
 * In order to get the result of a parsed formular, call the calculate(String[][] rep) method
 * rep is a matrix like {{"x", "y"},{"1.9", "2"}}.
 * It means that all values "x" are replaced with the number 1.9 and all values "y" with 2
 * If there is still a not replaced value in the function it will return Double.MIN_VALUE
 * 
 * @author Simon Bohlender
 *
 */
public class Function {
	
	private Language lang;
	private LinkedList<Primitive> functionElements;
	private String id;
	private String raw = "";
	
	private String parsed = "";
	private double result = 0;
	
	public Function(Language l, String name){
		lang = l;
		id = name;
		functionElements = new LinkedList<Primitive>();
	}
	
	/**
	 * Draws a given function and parses is (without sum and mult expressions)
	 * @param func Function expression
	 * @param xpos x position upper left
	 * @param ypos y position upper left
	 * @return length of generated drawing
	 */
	public int drawFunction(String func, int xpos, int ypos){
		raw = func;
		int completeLength = 0;
		int x = xpos;
		int y = ypos;
		TextProperties standardText = new TextProperties();
		standardText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 17));
		lang.newText(new Coordinates(x, y), "", "spacer", null, standardText);
		char[] funcArr = func.toCharArray();
		for(int i = 0; i<funcArr.length; i++){
			if(funcArr[i] == '\\' && i<funcArr.length-1){
				int end = 0;
				int start = i+1;
				int bracStat = 0;
				//looks for special expression blocks starting with \
				for(int k = start; k<funcArr.length; k++){
					if(funcArr[k] == '}'){
						bracStat -= 1;
					}
					else if(funcArr[k] == '{'){
						bracStat += 1;
					}
					else if(funcArr[k] ==  '\\' && bracStat == 0){
						end = k;
						break;
					}
				}
				//deals with sum expression
				String symbol = func.substring(start, end);
				if (symbol.toCharArray()[0] == 's'){
					int fromStart = start+4;
					int fromEnd = start+4;
					for(int l = fromStart+1; l<=end; l++){
						if(funcArr[l] == '}'){
							fromEnd = l;
							break;
						}
					}
					int toStart = fromEnd+2;
					int toEnd = end-1;
					String from = func.substring(fromStart, fromEnd);
					String to = func.substring(toStart, toEnd);
					drawSum(from, to, x, y);
					lang.newText(new Coordinates(x, y), "", "spacer", null, standardText);
					x = x + 25;
					i = toEnd + 1;
					completeLength += 20;
				}	
				//deals with frac expression
				if (symbol.toCharArray()[0] == 'f'){
					int upStart = start+5;
					int upEnd = start+5;
					boolean stop = false;
					for(int l = upStart+1; l<funcArr.length; l++){
						if(funcArr[l] == '\\'){
							stop = !stop;
						}
						else if(funcArr[l] == '}' && !stop){
							upEnd = l;
							break;
						}
					}
					int downStart = upEnd+2;
					int downEnd = end-1;
					String up = func.substring(upStart, upEnd);
					String down = func.substring(downStart, downEnd);
					drawFrac(up, down, x, y);
					lang.newText(new Coordinates(x, y), "", "spacer", null, standardText);
					int len = Math.max(up.length(), down.length());
					x = x+len*8;
					i = downEnd + 1;
					completeLength += len*10;
				}
				//deals with index expression
				if (symbol.toCharArray()[0] == 'i'){
					int baseStart = start+6;
					int baseEnd = start+6;
					for(int l = baseStart+1; l<=end; l++){
						if(funcArr[l] == '}'){
							baseEnd = l;
							break;
						}
					}
					int upStart = baseEnd + 2;
					int upEnd = baseEnd + 2;
					for(int m = upStart; m<=end; m++){
						if(funcArr[m] == '}'){
							upEnd = m;
							break;
						}
					}
					int downStart = upEnd+2;
					int downEnd = end-1;
					String base = func.substring(baseStart, baseEnd);
					String up = "";
					if(upStart < upEnd){
						up = func.substring(upStart, upEnd);						
					}
					String down = "";
					if(downStart < downEnd){
						down = func.substring(downStart, downEnd);
					}
					drawPow(base, up, down, x, y);
					lang.newText(new Coordinates(x, y), "", "spacer", null, standardText);
					int len = Math.max(up.length(), down.length());
					x = x+base.length()*10 + len*8;
					i = downEnd + 1;
					completeLength += base.length()*10 + len*8;
				}
				//deals with multiplication expression
				if (symbol.toCharArray()[0] == 'm'){
					int fromStart = start+5;
					int fromEnd = start+5;
					for(int l = fromStart+1; l<=end; l++){
						if(funcArr[l] == '}'){
							fromEnd = l;
							break;
						}
					}
					int condStart = fromEnd + 2;
					int condEnd = fromEnd + 2;
					for(int m = condStart; m<=end; m++){
						if(funcArr[m] == '}'){
							condEnd = m;
							break;
						}
					}
					int toStart = condEnd+2;
					int toEnd = end-1;
					String from = func.substring(fromStart, fromEnd);
					String cond = "";
					if(condStart < condEnd){
						cond = func.substring(condStart, condEnd);						
					}
					String to= "";
					if(toStart < toEnd){
						to = func.substring(toStart, toEnd);
					}
					drawMult(from, cond, to, x, y);
					lang.newText(new Coordinates(x, y), "", "spacer", null, standardText);
					int len = Math.max(from.length(), Math.max(cond.length(), to.length()));
					x = x + len*8 + 8;
					i = toEnd + 1;
					completeLength += len*8 + 8;
				}
				//deals with cdot expression
				if (symbol.toCharArray()[0] == 'c'){
					Circle dot = lang.newCircle(new Coordinates(x+8, y+12), 1, id+"_dot"+i, null);
					functionElements.add(dot);
					lang.newText(new Coordinates(x, y), "", "spacer", null, standardText);
					x = x + 15;
					i = i + 5;
					completeLength += 5;
				}		
			}
			else{
				boolean nextValid = true;
				int iNew = i;
				String addText = "";
				//checks for textblock in order not to add a new text block for each letter
				while(nextValid){
					if(funcArr[iNew] != ' '){
						if(funcArr[iNew] == '#'){
							addText += " ";						
						}
						else{			
							addText += funcArr[iNew];
							parsed += funcArr[iNew];
						}
					}
					if(iNew < funcArr.length-1){
						iNew += 1;
						if(Character.isLetter(funcArr[iNew])){
							nextValid = true;
						}
						else{
							nextValid = false;
							i = iNew-1;
						}
					}
					else {
						nextValid = false;
						i = iNew;
					}
				}
				Text t = lang.newText(new Coordinates(x, y), addText, id+"_text"+i, null, standardText);	
				functionElements.add(t);
				x += addText.length()*10;
				completeLength += addText.length()*10;
				
			}
		}
		return completeLength;
	}
	
	/**
	 * Draws a Fraction
	 * @param numerator Numerator of fraction
	 * @param denominator denominator of fraction
	 * @param xpos xpostion
	 * @param ypos yposition
	 */
	private void drawFrac(String numerator, String denominator, int xpos, int ypos){
		ypos = ypos + 14;
		int yposNumerator = ypos-25;
		int yposDenumerator = ypos-4;
		if(numerator.contains("sum")){
			yposNumerator -= 12;
		}
		if(numerator.contains("mult")){
			yposNumerator -= 25;
		}
		if(denominator.contains("sum") || denominator.contains("mult")){
			yposDenumerator += 15;
		}
		TextProperties standardText = new TextProperties();
		standardText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 17));
		parsed += "((";
		int lenUp = drawFunction(numerator, xpos, yposNumerator);
		parsed += ")/(";
		int lenDown = drawFunction(denominator, xpos, yposDenumerator);
		parsed += "))";
		Node[] fracNodes = new Node[2];
		fracNodes[0] = new Coordinates(xpos, ypos);
		fracNodes[1] = new Coordinates(xpos+Math.max(numerator.length(), Math.max(lenUp, lenDown)), ypos);
		Polyline frac = lang.newPolyline(fracNodes, id+"_frac", null);
		functionElements.add(frac);
		lang.newText(new Coordinates(xpos, ypos), "", "space", null, standardText);
	}
	
	/**
	 * Draws a pow expression
	 * @param base base numper
	 * @param exp exponent
	 * @param index index number
	 * @param xpos xposition
	 * @param ypos yposition
	 */
	private void drawPow(String base, String exp, String index, int xpos, int ypos){		
		TextProperties baseProb = new TextProperties();
		baseProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 17));
		Text b = lang.newText(new Coordinates(xpos, ypos), base, id+"_base", null, baseProb);
		functionElements.add(b);
		TextProperties expProb = new TextProperties();
		expProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 10));
		if (!exp.equals("")){
			Text e = lang.newText(new Coordinates(xpos+base.length()*10, ypos-7), exp, id+"_pow_exp", null, expProb);
			functionElements.add(e);
			lang.newText(new Coordinates(xpos, ypos), "", "space", null, baseProb);	
			parsed += "(Math.pow(" + base + index + "," + exp +"))";	
		}
		else{
			parsed += "(Math.pow(" + base + index + ",1" + "))";
		}
		if (!index.equals("")){
			Text d = lang.newText(new Coordinates(xpos+base.length()*10, ypos+3), index, id+"_pow_index", null, expProb);
			functionElements.add(d);
			lang.newText(new Coordinates(xpos, ypos), "", "space", null, baseProb);
		}
	}
	
	/**
	 * Draws a sum expression
	 * @param from Start of sum
	 * @param to End of sum
	 * @param xpos xposition
	 * @param ypos yposition
	 */
	private void drawSum(String from, String to, int xpos, int ypos){
		ypos = ypos + 3;
		xpos = xpos + 5;
		Node[] sumNodes = new Node[5];
		sumNodes[0] = new Coordinates(xpos+15, ypos);
		sumNodes[1] = new Coordinates(xpos, ypos);
		sumNodes[2] = new Coordinates(xpos+10, ypos+10);
		sumNodes[3] = new Coordinates(xpos, ypos+20);
		sumNodes[4] = new Coordinates(xpos+15, ypos+20);
		Polyline sum = lang.newPolyline(sumNodes, id+"_sum", null);	
		functionElements.add(sum);
		
		TextProperties textProb = new TextProperties();	
		textProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		Text f = lang.newText(new Coordinates(xpos-3, ypos+13), from, id+"_sum_from", null, textProb);
		Text t = lang.newText(new Coordinates(xpos+4, ypos-16), to, id+"_sum_to", null, textProb);
		functionElements.add(f);
		functionElements.add(t);
	}
	
	/**
	 * Draws a multiplication expression
	 * @param from start of multiplication
	 * @param cond condition of multiplication
	 * @param to end of multiplication
	 * @param xpos xposition
	 * @param ypos yposition
	 */
	private void drawMult(String from, String cond, String to, int xpos, int ypos){
		ypos += 3;
		xpos += 7;
		Node[] multNodes = new Node[6];		
		multNodes[0] = new Coordinates(xpos+15, ypos+20);
		multNodes[1] = new Coordinates(xpos+15, ypos+3);
		multNodes[2] = new Coordinates(xpos+17, ypos);
		multNodes[3] = new Coordinates(xpos, ypos);
		multNodes[4] = new Coordinates(xpos+3, ypos+3);
		multNodes[5] = new Coordinates(xpos+3, ypos+20);		
		Polyline mult = lang.newPolyline(multNodes, id+"mult", null);	
		functionElements.add(mult);
		
		TextProperties textProb = new TextProperties();	
		textProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		Text f = lang.newText(new Coordinates(xpos-3, ypos+13), from, id+"_mult_from", null, textProb);
		Text t = lang.newText(new Coordinates(xpos-3, ypos+29), cond, id+"_mult_cond", null, textProb);
		Text c = lang.newText(new Coordinates(xpos+4, ypos-16), to, id+"_mult_to", null, textProb);
		functionElements.add(f);
		functionElements.add(t);
		functionElements.add(c);
	}
	
	public void hide(){
		for(int i = 0; i < functionElements.size(); i++){
			functionElements.get(i).hide();
		}
	}
	
	public void show(){
		for(int i = 0; i < functionElements.size(); i++){
			functionElements.get(i).show();
		}
	}
	
	public void moveBy(String moveType, int dx, int dy, Timing delay, Timing duration){
		for(int i = 0; i < functionElements.size(); i++){
			functionElements.get(i).moveBy(moveType, dx, dy, delay, duration);
		}
	}
	
	public void moveTo(String direction, String moveType, Node target, Timing delay, Timing duration){
		for(int i = 0; i < functionElements.size(); i++){
			functionElements.get(i).moveTo(direction, moveType, target, delay, duration);
		}
	}
	
	public void moveVia(String direction, String moveType, Primitive via, Timing delay, Timing duration){
		for(int i = 0; i < functionElements.size(); i++){
			functionElements.get(i).moveVia(direction, moveType, via, delay, duration);
		}
	}
	
	/**
	 * Returns raw expression of drawn function
	 * @return
	 */
	public String getRawFunction(){
		return raw;
	}
	
	/**
	 * creates final parsing and corrects last mistakes, adds * between expressions and 
	 * corrects special expression blocks like sin cos sqrt pow
	 */
	public void parseFunction(){
		String finalParsed = "";
		char[] func = parsed.toCharArray();
		for(int i = 0; i<parsed.length()-1; i++){
			if(Character.isLetter(func[i]) && Character.isLetter(func[i+1]) ||
					Character.isDigit(func[i]) && Character.isLetter(func[i+1]) ||
					Character.isLetter(func[i]) && Character.isDigit(func[i+1]) ||
					func[i] == ')' && func[i+1] == '(' ||
					(Character.isDigit(func[i]) || Character.isLetter(func[i]) ) && func[i+1] == '('  ||
					func[i] == ')' && (Character.isDigit(func[i+1]) || Character.isLetter(func[i+1]) ) ||
					Character.isLetter(func[i]) && Character.isDigit(func[i+1])) {
				finalParsed += func[i]+"*";
			}
			else{
				finalParsed += func[i];
			}
		}
		finalParsed += func[func.length-1];
		//correct broken syntax like s*i*n* to sin

		finalParsed = finalParsed.replaceAll("--", "+");
		finalParsed = finalParsed.replaceAll("(M\\*a\\*t\\*h\\.p\\*o\\*w\\*)|(p\\*o\\*w\\*)", "Math.pow");
		finalParsed = finalParsed.replaceAll("(Math.pow\\([a-zA-Z]*)\\*([0-9],)", "$1$2");
		finalParsed = finalParsed.replaceAll("(M\\*a\\*t\\*h\\.t\\*a\\*n\\*)|(t\\*a\\*n\\*)", "Math.tan");
		finalParsed = finalParsed.replaceAll("(M\\*a\\*t\\*h\\.c\\*o\\*s\\*)|(c\\*o\\*s\\*)", "Math.cos");
		finalParsed = finalParsed.replaceAll("(M\\*a\\*t\\*h\\.s\\*i\\*n\\*)|(s\\*i\\*n\\*)", "Math.sin");
		finalParsed = finalParsed.replaceAll("(M\\*a\\*t\\*h\\.s\\*q\\*r\\*t\\*)|(s\\*q\\*r\\*t\\*)", "Math.sqrt");
		finalParsed = finalParsed.replaceAll("p\\*i|P\\*i|P\\*I|p\\*I", "Math.PI");
		finalParsed = finalParsed.replaceAll("e|E", "Math.E");
		parsed = finalParsed;
	}
	
	/**
	 * Calculates result of parsed formular
	 * @param replacements matrix dictionary where each variable is assigned to a specific numeric value 
	 * @return result of calculation
	 */
	public double calculate(String[][] replacements){
		parseFunction();
		String completeFunc = parsed;
		for(int i = 0; i<replacements[0].length; i++){
			completeFunc = completeFunc.replaceAll(replacements[0][i], Double.valueOf(replacements[1][i])+"");
		}
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		if(true){
			try {
				result = (double)engine.eval(completeFunc);
			} catch (ScriptException e) {
				result = Double.MIN_VALUE;
			}
		}
		return result;
	}
	
	public void highlight(){
		for(Primitive element: functionElements){
			 element.changeColor("", Color.RED, new TicksTiming(1), new TicksTiming(1));
		}
	}
	
	public void unHighlight(){
		for(Primitive element: functionElements){
			 element.changeColor("", Color.BLACK, new TicksTiming(1), new TicksTiming(1));
		}
	}
	
	/**
	 * Set function which will be parsed
	 * @param f function
	 */
	public void setParsedFunction(String f){
		parsed = f;
	}
	
	/**
	 * Get result of Calculation
	 * @return calculation result
	 */
	public double getResult(){
		return result;
	}
	
	/**
	 * get parsed formular
	 * @return parsed formular
	 */
	public String getParsed(){
		parseFunction();
		return parsed;
	}

	public LinkedList<Primitive> getElements() {
		return functionElements;
	}
	
}
