/*
 * AnyBaseToAnyBase.java
 * Khalid, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class AnyBaseToAnyBase implements ValidatingGenerator {
    private Language lang;
    private String input;
    private int outputBase;
    private int inputBase;
    private SourceCode sc;
    private SourceCodeProperties scProps;
    private Text mainProblem, tempRes, sourceIntro, problemIntro, inputSpec, toSpec, fromSpec, calcIntro, resultInto, monitorIntro, signSymbol1, signSymbol2;
    private TextProperties headerProps, textProps, signProps;
    private StringArray firstP, secondP;
    private StringArray [] mids;
    private ArrayProperties arrayPropsCarry, arrayPropsMids, arrayPropsProds;
    private PolylineProperties polyProps;
    int index, opCount;
    char [] bases = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O'
    ,'P','Q','R','S','T','U','V','W','X','Y','Z'};
    public void init(){
        lang = new AnimalScript("Any Base to Any Base Conversion", "Faris Abraha, Khalid Ibrahim S Alzamil", 800, 600);
    }
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        input = (String)primitives.get("input");
        inputBase = (Integer)primitives.get("input base");
        outputBase = (Integer)primitives.get("output base");
        headerProps = (TextProperties)props.getPropertiesByName("header properties");
        textProps = (TextProperties)props.getPropertiesByName("text properties");
        signProps = (TextProperties)props.getPropertiesByName("symbol properties");
        scProps = (SourceCodeProperties)props.getPropertiesByName("source code properties");
        arrayPropsProds = (ArrayProperties)props.getPropertiesByName("array product properties");
        arrayPropsMids = (ArrayProperties)props.getPropertiesByName("array middle properties");
        arrayPropsCarry = (ArrayProperties)props.getPropertiesByName("array carry properties");
        polyProps = (PolylineProperties)props.getPropertiesByName("line breaker properties");
        lang.setStepMode(true);
        sourceIntro = lang.newText(new Coordinates(20,20), "Source Code:", "Source Intro", null, headerProps);
        sourceIntro.setFont(new Font("Bold", Font.PLAIN, 25), null, null);
        showSourceCode();
        problemIntro = lang.newText(new Coordinates(20,250), "Parameters:", "Problem Intro", null, headerProps);
        problemIntro.setFont(new Font("Bold", Font.PLAIN, 25), null, null);
        monitorIntro = lang.newText(new Coordinates(20,350), "Current Step:", "Monitor Intro", null, headerProps);
        monitorIntro.setFont(new Font("Bold", Font.PLAIN, 25), null, null);
        resultInto = lang.newText(new Coordinates(20,430), "Result("+outputBase+"-Base):", "Result Intro", null, headerProps);
        resultInto.setFont(new Font("Bold", Font.PLAIN, 25), null, null);
        calcIntro = lang.newText(new Coordinates(500,20), "Calculation("+outputBase+"-Base):", "Calculation Intro", null, headerProps);
        calcIntro.setFont(new Font("Bold", Font.PLAIN, 25), null, null);
        inputSpec = lang.newText(new Coordinates(20,270), "input: " + input, "Input Spec", null, textProps);
        inputSpec.setFont(new Font("Monospaced", Font.ITALIC, 12), null, null);
        fromSpec = lang.newText(new Coordinates(20,290), "from: " + inputBase, "From Spec", null, textProps);
        fromSpec.setFont(new Font("Monospaced", Font.ITALIC, 12), null, null);
        toSpec = lang.newText(new Coordinates(20,310), "to: " + outputBase, "To Spec", null, textProps);
        toSpec.setFont(new Font("Monospaced", Font.ITALIC, 12), null, null);
        mainProblem = lang.newText(new Coordinates(20,370), "", "Main Problem", null, textProps);
        mainProblem.setFont(new Font("Bold", Font.ITALIC, 12), null, null);
        tempRes = lang.newText(new Coordinates(20,450), "result = ", "Result", null, textProps);
        tempRes.setFont(new Font("Monospaced", Font.ITALIC, 12), null, null);
        lang.setStepMode(false);
    	sc.highlight(2);
    	index = 0;
    	opCount = 0;
    	lang.nextStep("Initialization");
        convert(inputBase, outputBase, input);
        sc.unhighlight(9);
        return lang.toString();
    }
    
    public void showSourceCode() {
		// now, create the source code entity
		sc = lang.newSourceCode(new Coordinates(20, 40), "sourceCode", null, scProps);
		// add a code line
		// parameters: code itself; name (can be null); indentation level; display options
		// Char array "bases"
		sc.addCodeLine("char [] bases = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C',", null, 0, null);
		sc.addCodeLine("'D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};", null, 1, null);
		//the convert method
		sc.addCodeLine("public void convert(int from, int to, String input) {", null, 0, null);
		sc.addCodeLine("String result = input.charAt(0);", null, 1, null);
		sc.addCodeLine("for (i = 0; i < input.length()-1; i++) {", null, 1, null);
		sc.addCodeLine("result = mul(result, bases[from], bases[to]);", null, 2, null);
		sc.addCodeLine("result = add(input.charAt(i+1), result,bases[to]);", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("return result;", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
	}
    
    public String convert(int fromB, int toB, String input) { //2
    	sc.toggleHighlight(2,3);
    	mainProblem.setText("To start things off, we'll accumelate the value of our first Charecter ("+ input.charAt(0) +") from the input sequence to our current result", null, null);
		String result = addB(mids, input.charAt(0) + "", "", bases[toB], false, false, false, 0, 0, 0); //3
		tempRes.setText("result = " + result, null, null);
		mainProblem.setText("", null, null);
		sc.toggleHighlight(3,4);
		lang.nextStep("Calculation #" + (++index));
		for(int i = 0; i < (input.length()-1); i++) { //4
			sc.toggleHighlight(4,5);
			mainProblem.setText("Now, we'll multiply our current result with the from Parameter " + "("+fromB+")", null, null);
			result = mullB(
                           result,
                           addB(mids, bases[fromB]+"", "", bases[toB], false, false, false, 0, 0, 0),
                           bases[toB]); //5
			tempRes.setText("result = " + result, null, null);
			mainProblem.setText("", null, null);
			sc.toggleHighlight(5,6);
			mainProblem.setText("Now, we'll accumelate the value of our next Charecter ("+input.charAt(i+1) +") from the input sequence to our current result", null, null);
			result = addB(
                          mids,
                          addB(mids, input.charAt(i+1)+"", "", bases[toB], false, false, false, 0, 0, 0),
                          result, bases[toB], false, true, false, 0, 0, 0);//6
			tempRes.setText("result = " + result, null, null);
			mainProblem.setText("", null, null);
			if(i != input.length()-2) {
				sc.toggleHighlight(6,4);
				lang.nextStep("Calculation #" + (++index));
			}
			else {
				sc.toggleHighlight(6,7);
			}
		}//7
		mainProblem.setText("We are finally done converting, and the number of operations used is " + opCount, null, null);
		sc.toggleHighlight(7,8);
		lang.nextStep("Result");
		return result;//8
	}//9
    
	public String addB(StringArray [] midsA, String a, String b, char base, boolean first, boolean simple, boolean last, int cocx, int res_Y, int ignore) {
		lang.setStepMode(false);
		String result = "";
		int aCount = a.length()-1;
		int bCount = b.length()-1;
		char carry = '0';
		List <String> carryLst = new ArrayList<String>();
		while(0 <= aCount && 0 <= bCount) {
			result = bases[((getValue(a.charAt(aCount)) + getValue(b.charAt(bCount)) + getValue(carry)) % getValue(base))] + result;
			opCount++;
			carry = bases[((getValue(a.charAt(aCount)) + getValue(b.charAt(bCount))+ getValue(carry)) / getValue(base))];
			opCount++;
			carryLst.add(carry+"");
			aCount--;
			bCount--;
		}
		if(aCount >= 0) {
			for(; aCount >= 0; aCount--) {
				result = bases[((getValue(a.charAt(aCount))+ getValue(carry)) % getValue(base))] + result;
				opCount++;
				carry = bases[((getValue(a.charAt(aCount))+ getValue(carry)) / getValue(base))];
				opCount++;
				carryLst.add(carry+"");
			}
		}
		if(bCount >= 0) {
			for(; bCount >= 0; bCount--) {
				result = bases[((getValue(b.charAt(bCount))+ getValue(carry)) % getValue(base))] + result;
				opCount++;
				carry = bases[((getValue(b.charAt(bCount))+ getValue(carry)) / getValue(base))];
				opCount++;
				carryLst.add(carry+"");
			}
		}
		while(getValue(carry) >= getValue(base)) {
			carryLst.remove(carryLst.size()-1);
			result = bases[(getValue(carry)) % getValue(base)] + result;
			opCount++;
			carry = bases[(getValue(carry)) / getValue(base)];
			opCount++;
			carryLst.add(carry+"");
		}
		if(carry != '0') {
			result = carry + result;
		}
		String [] fakeArray = new String[carryLst.size()];
		for(int i = 0; i < carryLst.size(); i++) {
			fakeArray[carryLst.size()-1-i] = carryLst.get(i);
		}
		// Visualize the result
		if(first) {
			String [] tempCarry = new String[fakeArray.length];
			for(int i = 0; i < fakeArray.length; i++) {
				tempCarry[i] = "  ";
			}
			int aVisCount = a.length();
			int bVisCount = b.length();
			int resCount = result.length()-1;
			StringArray carryB = lang.newStringArray(new Coordinates(cocx,240-130), tempCarry, "Carry", null, arrayPropsCarry);
			signSymbol2 = lang.newText(new Coordinates(cocx-13,res_Y), "+", "SignSymbols", null, signProps);
			int cCount = fakeArray.length-1;
			while(cCount >= 0) {
				boolean aSafe = ((aVisCount <= (a.length()-(ignore-1)) || ignore >= 1) && (aVisCount >= 0));
				boolean bSafe = (bVisCount <= (b.length()-(ignore)) && (bVisCount >= 0));
				if(aSafe) {
					midsA[ignore-1].highlightElem(aVisCount, null, null);
				}
				if(bSafe) {
					midsA[ignore].highlightElem(bVisCount, null, null);
				}
				midsA[ignore-1].unhighlightElem(aVisCount, null, null);
				midsA[ignore].unhighlightElem(bVisCount, null, null);
				lang.setStepMode(true);
				midsA[ignore].put(bVisCount, result.charAt(resCount)+"", null, null);
				midsA[ignore].highlightElem(bVisCount, null, null);
				lang.setStepMode(false);
				carryB.unhighlightElem(cCount, null, null);
				carryB.put(cCount, fakeArray[cCount], null, null);
				aVisCount--;
				bVisCount--;
				cCount --;
				resCount--;
			}
			lang.setStepMode(true);
			midsA[ignore-1].hide();
			carryB.hide();
			signSymbol2.hide();
			midsA[ignore].unhighlightElem(0, midsA[ignore].getLength()-1, null, null);
			lang.setStepMode(false);
			if(last) {
				lang.setStepMode(true);
				String [] finalString = new String[result.length()];
				for(int i = 0; i < result.length(); i++) {
					finalString[i] = result.charAt(i)+"";
				}
				StringArray finalRes = lang.newStringArray(new Coordinates(cocx+13,res_Y+15), finalString, "my last result", null, arrayPropsMids);
				finalRes.highlightElem(0, finalString.length-1, null, null);
				lang.setStepMode(false);
				midsA[ignore].hide();
				lang.nextStep();
				finalRes.hide();
				lang.nextStep();
			}
			
		}
		if(simple) {
			if(a.length() > b.length()) {
				String swap = a;
				a = b;
				b = swap;
			}
			String [] inputA = new String [b.length()];
			String [] inputB = new String [b.length()];
			for(int i = inputA.length-1; i >= 0; i--) {
				inputB[i] = b.charAt(i) + "";
				if(0 <= i-(b.length() - a.length())) {
					inputA[i] = a.charAt(i-(b.length() - a.length())) + "";
				}
				else {
					inputA[i] = "  ";
				}
			}
			int dark = b.length() - a.length();
			String [] tempCarry = new String[fakeArray.length];
			for(int i = 0; i < fakeArray.length; i++) {
				tempCarry[i] = "  ";
			}
			lang.setStepMode(true);
			StringArray bigAdditive = lang.newStringArray(new Coordinates(700-(200),200-130), inputA, "A", null, arrayPropsProds);
			StringArray smallAdditive = lang.newStringArray(new Coordinates(700-(200),220-130), inputB, "B", null, arrayPropsProds);
			signSymbol2 = lang.newText(new Coordinates(674-200,220-130), "+", "SignSymbols", null, signProps);
			StringArray carryAdditive = lang.newStringArray(new Coordinates(700-13-(200),180-130), tempCarry, "Carry", null, arrayPropsCarry);
			Coordinates[] ver = new Coordinates[2];
			ver[0] = new Coordinates(700-(200), 235-130);
			ver[1] = new Coordinates(700-(200)+(b.length())*13, 235-130);
			Polyline breakerF = lang.newPolyline(ver, "Breaker", null, polyProps);
			String [] finalString = new String[result.length()];
			for(int i = 0; i < result.length(); i++) {
				finalString[i] = "  ";
			}
			StringArray finalRes = lang.newStringArray(new Coordinates(700-((result.length()-b.length())*13)-(200),240-130), finalString, "my result", null, arrayPropsMids);
			lang.setStepMode(false);
			int inputACount = inputA.length-1;
			int inputBCount = inputB.length-1;
			int cCount = fakeArray.length-1;
			int resCount = result.length()-1;
			while(cCount >= 0) {
				boolean safeA = inputACount >= (inputA.length-1)-(a.length()-1);
				if(safeA) {
					bigAdditive.highlightElem(inputACount, null, null);
				}
				smallAdditive.highlightElem(inputBCount, null, null);
				bigAdditive.unhighlightElem(inputACount, null, null);
				smallAdditive.unhighlightElem(inputBCount, null, null);
				lang.setStepMode(true);
				finalRes.put(inputBCount, result.charAt(resCount)+"", null, null);
				finalRes.highlightElem(inputBCount, null, null);
				lang.setStepMode(false);
				carryAdditive.put(cCount, fakeArray[cCount], null, null);
				inputACount--;
				inputBCount--;
				cCount --;
				resCount--;
			}
			finalRes.unhighlightElem(0, finalRes.getLength()-1, null, null);
			carryAdditive.hide();
			bigAdditive.hide();
			smallAdditive.hide();
			breakerF.hide();
			signSymbol2.hide();
			finalRes.hide();
			lang.nextStep();
		}
		return result;
	}
	
	public String mullB(String a, String b, char base) {
		String result = "";
		String [] temp = new String[b.length()];
		int [] carryCO = new int[b.length()];
		int [] midResCO_X = new int[b.length()];
		int [] midResCO_Y = new int[b.length()];
		if(b.length() > a.length()) {
			return mullB(b, a, base);
		}
		String[] aArray = new String [a.length()];
		String[] bArray = new String [a.length()];
		for(int i = aArray.length-1; i >= 0; i--) {
			aArray[i] = a.charAt(i) + "";
			if(0 <= i-(a.length() - b.length())) {
				bArray[i] = b.charAt(i-(a.length() - b.length())) + "";
			}
			else {
				bArray[i] = "  ";
			}
		}
		int dark = a.length() - b.length();
		lang.setStepMode(true);
		String [] fakeCarry = new String[a.length()];
		firstP = lang.newStringArray(new Coordinates(700-(200),200-130), aArray, "A", null, arrayPropsProds);
		secondP = lang.newStringArray(new Coordinates(700-(200),220-130), bArray, "B", null, arrayPropsProds);
		signSymbol1 = lang.newText(new Coordinates(674-(200),220-130), "*", "SignSymbols", null, signProps);
		Coordinates[] ver = new Coordinates[2];
		ver[0] = new Coordinates(700-(200), 235-130);
		ver[1] = new Coordinates(700-(200)+(a.length())*13, 235-130);
		Polyline breakerF = lang.newPolyline(ver, "Breaker", null, polyProps);
		lang.setStepMode(false);
		for(int i = 0; i < temp.length; i++) {
			temp[i] = "";
		}
		char carry = '0';
		StringArray [] mids = new StringArray[b.length()];
		int highlightB = a.length()-1;
		for(int i = b.length()-1; i >= 0; i--) {
			for(int offset = 0; offset < (b.length()-1-i); offset++) {
				temp[b.length()-1-i] = temp[b.length()-1-i] + '0';
			}
			for(int j = a.length()-1; j >= 0; j--) {
				temp[b.length()-1-i] = bases[((getValue(a.charAt(j)) * getValue(b.charAt(i))) + getValue(carry))%getValue(base)]
                + temp[b.length()-1-i];
				opCount++;
				carry = bases[((getValue(a.charAt(j)) * getValue(b.charAt(i))) + getValue(carry))/getValue(base)];
				opCount++;
				fakeCarry[j] = carry+"";
				if(j == 0 && carry != '0') {
					temp[b.length()-1-i] = carry + temp[b.length()-1-i];
					carry = '0';
				}
			}
			String [] tempStr = new String[temp[b.length()-1-i].length()];
			String [] fakeStr = new String[temp[b.length()-1-i].length()+1];
			for(int k = 0; k < tempStr.length+1; k++) {//
				if(k == tempStr.length) {
					fakeStr[k] = "  ";
				}
				else {
					tempStr[k] = temp[b.length()-1-i].charAt(k)+"";
					fakeStr[k] = "  ";
				}
			}
			String [] fakeFakerCarry = new String[a.length()];
			for(int k = 0; k < fakeFakerCarry.length; k++) {//
				fakeFakerCarry[k] = "  ";
			}
			lang.setStepMode(true);
			StringArray carryA = lang.newStringArray(new Coordinates(700-13-(200),180-130), fakeFakerCarry, "Carry", null, arrayPropsCarry);
			if(b.length()==1) {
				mids[(b.length()-1)-i] = lang.newStringArray(new Coordinates(700-(200)-((b.length()-1-i+(tempStr.length-a.length()-(b.length()-1-i))+1)*13),(240-130)),
                                                             fakeStr, "mid", null, arrayPropsMids);
			}
			if(b.length()!=1) {
				mids[(b.length()-1)-i] = lang.newStringArray(new Coordinates(700-(200)-((b.length()-1-i+(tempStr.length-a.length()-(b.length()-1-i))+1)*13),(240-130)+((b.length()-i)*20)),
                                                             fakeStr, "mid", null, arrayPropsMids);
			}
			midResCO_X[(b.length()-1)-i] = (700-((b.length()-1-i+(tempStr.length-a.length()-(b.length()-1-i))+1)*13))-(200);
			midResCO_Y[(b.length()-1)-i] = (240-130)+((b.length()-i)*20);
			carryCO[(b.length()-1)-i] = 700-((b.length()+1-i+(tempStr.length-a.length()-(b.length()-i)))*13)-(200);
			lang.setStepMode(false);
			int highlightA = a.length()-1;
			int highlightC = a.length()-1;
			for(int k = temp[((b.length()-1)-i)].length()-1-(b.length()-1-i)+1; k >= 1; k--) {//
				lang.setStepMode(false);
				if(highlightA >= 0) {
					secondP.highlightElem(highlightB, null, null);
					firstP.highlightElem(highlightA, null, null);
				}
				lang.setStepMode(true);
				mids[(b.length()-1)-i].put(k, tempStr[k-1], null, null);
				mids[(b.length()-1)-i].unhighlightCell(k, null, null);
				if(highlightA >= 0) {
					secondP.unhighlightElem(highlightB, null, null);
					firstP.unhighlightElem(highlightA, null, null);
				}
				highlightA--;
				lang.setStepMode(false);
				if(highlightC>=0) {
					carryA.put(highlightC, fakeCarry[highlightC], null, null);
				}
				lang.setStepMode(true);
				highlightC--;
			}
			carryA.hide();
			lang.nextStep();
			highlightB--;
		}
		if(b.length()!=1) {
			ver[0] = new Coordinates(midResCO_X[b.length()-1], (235-130)+((b.length()+1)*20));
			ver[1] = new Coordinates(midResCO_X[b.length()-1]+(mids[b.length()-1].getLength())*13, (235-130)+((b.length()+1)*20));
			Polyline breakerS = lang.newPolyline(ver, "Breaker", null, polyProps);
			for(int i = 0; i < temp.length; i++) {
				if(i == 0) {
					result = addB(mids, result, temp[i], base, false, false, false,0, 0, 0);
				}
				else {
					if(i == temp.length-1) {
						result = addB(mids, result, temp[i], base, true, false, true, carryCO[i], midResCO_Y[i], i);
					}
					else {
						result = addB(mids, result, temp[i], base, true, false, false, carryCO[i], midResCO_Y[i], i);
					}
				}
			}
			lang.setStepMode(true);
			//loose all the current arrays
			firstP.hide();
			secondP.hide();
			signSymbol1.hide();
			breakerF.hide();
			breakerS.hide();
			for(int i = 0; i < mids.length; i++) {
				mids[i].hide();
			}
			lang.nextStep();
			return result;
		}
		lang.setStepMode(true);
		//loose all the current arrays
		firstP.hide();
		secondP.hide();
		signSymbol1.hide();
		breakerF.hide();
		mids[0].hide();
		lang.nextStep();
		return temp[0];
	}
	
	
	public String repeat(String str, int times) {
		String result = "";
		for(int i = 0; i < times; i++) {
			result += str;
		}
		return result;
	}
	
	public int getValue(char v) {
		switch (v) {
			case '0' : return 0;
			case '1' : return 1;
			case '2' : return 2;
			case '3' : return 3;
			case '4' : return 4;
			case '5' : return 5;
			case '6' : return 6;
			case '7' : return 7;
			case '8' : return 8;
			case '9' : return 9;
			case 'A' : return 10;
			case 'B' : return 11;
			case 'C' : return 12;
			case 'D' : return 13;
			case 'E' : return 14;
			case 'F' : return 15;
			case 'G' : return 16;
			case 'H' : return 17;
			case 'I' : return 18;
			case 'J' : return 19;
			case 'K' : return 20;
			case 'L' : return 21;
			case 'M' : return 22;
			case 'N' : return 23;
			case 'O' : return 24;
			case 'P' : return 25;
			case 'Q' : return 26;
			case 'R' : return 27;
			case 'S' : return 28;
			case 'T' : return 29;
			case 'U' : return 30;
			case 'V' : return 31;
			case 'W' : return 32;
			case 'X' : return 33;
			case 'Y' : return 34;
			case 'Z' : return 35;
                //
			case 'a' : return 10;
			case 'b' : return 11;
			case 'c' : return 12;
			case 'd' : return 13;
			case 'e' : return 14;
			case 'f' : return 15;
			case 'g' : return 16;
			case 'h' : return 17;
			case 'i' : return 18;
			case 'j' : return 19;
			case 'k' : return 20;
			case 'l' : return 21;
			case 'm' : return 22;
			case 'n' : return 23;
			case 'o' : return 24;
			case 'p' : return 25;
			case 'q' : return 26;
			case 'r' : return 27;
			case 's' : return 28;
			case 't' : return 29;
			case 'u' : return 30;
			case 'v' : return 31;
			case 'w' : return 32;
			case 'x' : return 33;
			case 'y' : return 34;
			case 'z' : return 35;
			default: return -1;
		}
	}
    
    public String getName() {
        return "Any Base to Any Base Conversion";
    }
    
    public String getAlgorithmName() {
        return "Any Base to Any Base Conversion";
    }
    
    public String getAnimationAuthor() {
        return "Faris Abraha, Khalid Ibrahim S Alzamil";
    }
    
    public String getDescription(){
        return "This algorithm converts any base (from 2 to 35) to any other base (from 2 to 35). "
        +"\n"
        +"For example converts 12\u2083" + " to 101\u2082" + ":"
        +"\n"
        +"\n"
        +"123"
        +"\n"
        +"\n"
        +" = ((1*3)\u2082+2\u2083)\u2082"
        +"\n"
        +" = (11\u2082+2)\u2082"
        +"\n"
        +" = 1001\u2082"
        +"\n"
        +"\n"
        +"= 101\u2082";
    }
    
    
    public String getCodeExample(){
        return "char [] bases = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O'"
        +"\n"
        +"			,'P','Q','R','S','T','U','V','W','X','Y','Z'}; "
        +"\n"
        +"	"
        +"\n"
        +"public void convert(int from, int to, String input) {"
        +"\n"
        +"	String result = input.charAt(0);"
        +"\n"
        +"	for (i = 0; i < input.length()-1; i++) {"
        +"\n"
        +"		result = mul(result, bases[from], bases[to]);"
        +"\n"
        +"		result = add(input.charAt(i+1), result,bases[to]);"
        +"\n"
        +"	}"
        +"\n"
        +"	return result;"
        +"\n"
        +"}";
    }
    
    public String getFileExtension(){
        return "asu";
    }
    
    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }
    
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }
    
    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
                                 Hashtable<String, Object> arg1) throws IllegalArgumentException {
		JOptionPane error = new JOptionPane();
		String msg = "";
		// TODO Auto-generated method stub
		if((Integer)arg1.get("input base") < 2 || (Integer)arg1.get("input base") > 35) {
			JOptionPane.showMessageDialog(error, "The input base is invalid, the input base has to be between 2 and 35");
			throw new IllegalArgumentException();
		}
		if((Integer)arg1.get("output base") < 2 || (Integer)arg1.get("output base") > 35) {
			JOptionPane.showMessageDialog(error, "The output base is invalid, the output base has to be between 2 and 35");
			throw new IllegalArgumentException();
		}
		String in = (String)arg1.get("input");
		for(int i = 0; i < in.length(); i++) {
			if(getValue(in.charAt(i)) >= (Integer)arg1.get("input base") && getValue(in.charAt(i))!=-1) {
				for(int k = 0; k < (Integer)arg1.get("input base"); k++) {
					if(k == 0) {
						msg = "" + k;
					}
					if(k == ((Integer)arg1.get("input base")-1)) {
						if(k > 9) {
							msg = msg + " and " + bases[k];
						}
						else {
							msg = msg + " and " + k;
						}
					}
					if(k > 0 && k < ((Integer)arg1.get("input base")-1)) {
						if(k > 9) {
							msg = msg + ", " + bases[k];
						}
						else {
							msg = msg + ", " + k;
						}
					}
				}
				JOptionPane.showMessageDialog(error, "The input sequence (that is to be converted) is an invalid " + ((Integer)arg1.get("input base"))+"-Base number.... the valid characters are in this case: \n"+msg);
				throw new IllegalArgumentException();
			}
		}
		return true;
	}
    
}