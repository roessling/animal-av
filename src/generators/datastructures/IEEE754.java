/*
 * IEEE775.java
 * Lutz Langhorst, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.datastructures;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
//import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

public class IEEE754 implements ValidatingGenerator {

	private Translator t;
	private Locale genLocale;
	
	private Language l;
	private Variables vars;

	private int sub_step_limit;

	private int IEEE_754_bias;
	private int IEEE_754_size;
	private int IEEE_754_characteristic;
	private int IEEE_754_mantissa;

	private SourceCodeProperties codeProp;
	private ArrayProperties arProp;
	private ArrayMarkerProperties arMarcProp;
	private MatrixProperties matProp;
	private TextProperties titleProp;
	private TextProperties headlineProp;
	private TextProperties textProp;
	private TextProperties numberProp;
	private RectProperties rectProp;

	//Subcalculations
	
	private String intToBit(int number, int length, Vector<String[]> textOut) {
		String temp = "";

		do {
			String bit;
			if (number % 2 == 1) {
				bit = "1";
			} else {
				bit = "0";
			}
			temp = bit + temp;

			if(textOut != null) {
				String[] tempStr = new String[5];
				tempStr[0] = "" + number;
				tempStr[1] = " / 2 =";
				tempStr[2] = "" + (number/2);
				tempStr[3] = "R: ";
				tempStr[4] = "" + bit;
				textOut.add(tempStr);
			}

			number /= 2;
		} while (number > 0);

		if (length != 0) {
			temp = String.format("%" + length + "s", temp).replaceAll(" ", "0");
		}
		
		return temp;
	}

	private String fractionToBit(double number, int limit, Vector<String[]> textOut) {
		String temp = "";

		do {
			String bit;
			double visNum = number;

			number *= 2;
			if (number >= 1) {
				bit = "1";
				number -= 1;
			} else {
				bit = "0";
			}
			temp += bit;

			if(textOut != null) {
				String[] tempStr = new String[6];
				tempStr[0] = "(";
				tempStr[1] = "" + visNum;
				tempStr[2] = "* 2 ) >= 1 ) =";
				tempStr[3] = "" + bit;
				tempStr[4] = " ; R = ";
				tempStr[5] = "" + number;
				textOut.add(tempStr);
			}

		} while (number > 0 && temp.length() < limit);

		return temp;
	}

	private String shift(String bit, int distance, Vector<String[]> textOut) {
		if(textOut != null) {
			textOut.add(new String[]{"0" , bit});
		}
		
		int sign = (distance < 0) ? -1 : 1;

		for (int i = 0; i < Math.abs(distance); i++) {
			int point = bit.indexOf('.');
			char[] temp = bit.toCharArray();

			temp[point] = temp[point + sign];
			temp[point + sign] = '.';
			bit = new String(temp);

			bit = bit.replaceAll("^0+", "");
			if (bit.charAt(0) == '.')
				bit = "0" + bit;
			if (bit.charAt(bit.length() - 1) == '.')
				bit = bit + "0";

			if(textOut != null) {
				String[] tempStr = new String[2];
				tempStr[0] = "" + (sign * (i + 1));
				tempStr[1] = "" + bit;
				textOut.add(tempStr);
			}
		}
		
		return bit;
	}

	private double bitStringToNumber(String bit, Vector<String[]> textOut) {
		double temp = 0;
		String tBit = bit;
		int point = bit.indexOf('.');

		if (point == -1) {
			point = tBit.length() - 1;
		} else {
			tBit = bit.substring(0, point) + bit.substring(point + 1, bit.length() - 1);
			point -= 1;
		}

		for (int i = 0; i < tBit.length(); i++) {
			double val = Math.pow(2, point - i);
			temp += val * Character.getNumericValue(tBit.charAt(i));
			
			if(textOut != null) {
				String[] tempStr = new String[6];
				tempStr[0] = "+";
				tempStr[1] = ("" + val).replaceAll("\\.0$", "");
				tempStr[2] = "*";
				tempStr[3] = "" + Character.getNumericValue(tBit.charAt(i));
				tempStr[4] = "=";
				tempStr[5] = ("" + temp).replaceAll("\\.0$", "");
				textOut.add(tempStr);
			}
			if(tBit.substring(i).matches("00+")) {
				textOut.add(new String[] {"..." , "" , "" , "" , "" , ""+temp});
				break;
			}
		}

		return temp;
	}
	
	//sub calc out
	
	private void subCalcOut(Vector<String[]> sV, Offset offset, String id, int highliteID) {
		String[][] data = new String[sV.size()][sV.get(0).length];
		sV.toArray(data);
		StringMatrix m = genEmptyMatrix(data.length, data[0].length, offset, id);
		for(int i = 0 ; i < data.length ; i++) {
			putLine(data[i], i, m);
			if(highliteID >= 0)
				m.highlightCell(i, highliteID, null, null);
			if(i < sub_step_limit || sub_step_limit < 0)
				l.nextStep();
		}
		m.hide();
	}
	
	private void bitToDecOut(Vector<String[]> sV, StringArray AIEEE , Offset offset, String id, int highliteID, int arID, int marcVisOf) {
		String[][] data = new String[sV.size()][sV.get(0).length];
		sV.toArray(data);
		StringMatrix m = genEmptyMatrix(data.length, data[0].length, offset, id);
		ArrayMarker marc = l.newArrayMarker(AIEEE, arID, "marker", null, arMarcProp);
		if(marcVisOf < 0)
			marc.hide();
		
		for(int i = 0 ; i < data.length ; i++) {
			putLine(data[i], i, m);
			if(data[i][0].equals("..."))
				break;
				
			m.highlightCell(i, highliteID, null, null);
			marc.move(arID+i, null, null);
			if(marcVisOf < 0 && marcVisOf + i == 0)
				marc.show();
			if(i < sub_step_limit || sub_step_limit < 0)
				l.nextStep();
			m.unhighlightCell(i, highliteID, null, null);
		}
		m.highlightCell(data.length-1, data[0].length-1, null, null);
		l.nextStep();
		marc.hide();
		m.hide();
	}
	
	//help functions
	
	private StringMatrix genEmptyMatrix(int y, int x, Offset offset, String id) {
		String[][] mat = new String[y][x];
		for(int i = 0 ; i < y ; i++) {
			for(int j = 0 ; j < x ; j++) {
				mat[i][j] = "";
			}
		}
		return l.newStringMatrix(offset, mat, id, null, matProp);
	}
	
	private void putLine(String[] l, int y, StringMatrix M) {
		for(int i = 0 ; i < l.length ; i++) {
			M.put(y, i, l[i], null, null);
		}
	}

	private String animalStringArrayToString(StringArray A) {
		String temp = "";
		for (int i = 0; i < A.getLength(); i++) {
			temp = temp + A.getData(i);
		}
		return temp;
	}

	private void stringArraySet(String[] A, String x, int a, int b) {
		for (int i = a; i <= b; i++) {
			A[i] = x;
		}
	}

	private void animalStringArraySet(StringArray A, String x, int a, int b) {
		for (int i = a; i <= b; i++) {
			A.put(i, x, null, null);
		}
	}

	private void bitStringToAnimalArray(StringArray A, String x, int a, int b) {
		for (int i = 0; i <= b - a; i++) {
			A.put(a + i, "" + x.charAt(i), null, null);
		}
	}
	
	private void loadText(Vector<String> v, String id) {
		int codeSize = Integer.parseInt(t.translateMessage(id +"Size"));
		for (int i = 0; i < codeSize; i++) {
			v.add(t.translateMessage("" + id + i));
		}
	}
	
	//main scripts
	
	private String NumberToIEEE754(double Number, Offset mainOffset) {
		boolean underflow = false;
		int NumberInt;
		double NumberFraction;
		String StaticPoint1 = "";
		String StaticPoint2 = "";
		String StaticPoint;
		int exp = 0;
		String mantissa = "";
		StringArray solIEEE;
		Offset calcOffset;

		/*
		 * Framewoek
		 */
		
		// PseudoCode
		Text codeHeader = l.newText(mainOffset, t.translateMessage("NumberToIEEE754"), "NumberToIEEE754", null,
				headlineProp);
		SourceCode NtIcode = l.newSourceCode(new Offset(0, 10, codeHeader, "SW"), "NumberToIEEE754Code", null,
				codeProp);
		NtIcode.addCodeLine(t.translateMessage("NtI0"), null, 0, null);
		NtIcode.addCodeLine(t.translateMessage("NtI1"), null, 1, null);
		NtIcode.addCodeLine(t.translateMessage("NtI2"), null, 0, null);
		NtIcode.addCodeLine(t.translateMessage("NtI3"), null, 1, null);
		NtIcode.addCodeLine(t.translateMessage("NtI4"), null, 1, null);
		NtIcode.addCodeLine(t.translateMessage("NtI5"), null, 0, null);
		NtIcode.addCodeLine(t.translateMessage("NtI6"), null, 0, null);
		NtIcode.addCodeLine(t.translateMessage("NtI7"), null, 0, null);

		// constants
		InfoBox constants = new InfoBox(l, new Offset(0, 10, NtIcode, "SW"), 4, t.translateMessage("constants"));
		Vector<String> constText = new Vector<String>();
		constText.add(t.translateMessage("bias") + IEEE_754_bias);
		constText.add(t.translateMessage("size") + IEEE_754_size);
		constText.add(t.translateMessage("lchar") + IEEE_754_characteristic);
		constText.add(t.translateMessage("lmant") + IEEE_754_mantissa);
		constants.setText(constText);

		//danger : absolute positioning
		@SuppressWarnings("unused")
		Text calcHeader = l.newText(new Offset(30, -42, NtIcode, "NE"), t.translateMessage("calcHeader"), "calcHeader",
				null, headlineProp);

		Text solNumber = l.newText(new Offset(40, 0, NtIcode, "NE"), "" + Number + " -> ", "number", null, numberProp);
		
		String[] IEEEArray = new String[IEEE_754_size];
		stringArraySet(IEEEArray, "X", 0, IEEEArray.length - 1);
		solIEEE = l.newStringArray(new Offset(0, 0, solNumber, "NE"), IEEEArray, "solIEEE", null, arProp);

		calcOffset = new Offset(0, 40, solNumber, "SW");

		/*
		 * special cases
		 */
		
		NtIcode.highlight(0);
		l.nextStep(t.translateMessage("NtI0"));
		
		Vector<Text> cases = new Vector<Text>();
		Offset tempOffset = calcOffset;

		//danger: NaN maybe wrong
		if (Double.isNaN(Number)) {
			animalStringArraySet(solIEEE, "1", 0, IEEE_754_size - 1);
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("NaN"), "cases", null, textProp));
			l.nextStep();
			return animalStringArrayToString(solIEEE);
		} else {
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("nNaN"), "cases", null, textProp));
		}
		tempOffset = new Offset(0, 0, cases.lastElement(), "SW");
		l.nextStep();

		if (Number == 0 || Math.abs(Number) < Math.pow(2, -IEEE_754_bias + 1)) {
			animalStringArraySet(solIEEE, "0", 0, solIEEE.getLength() - 1);
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("Zero"), "cases", null, textProp));
			l.nextStep();
			return animalStringArrayToString(solIEEE);
		} else if (Number > 0) {
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("Pos"), "cases", null, textProp));
			solIEEE.put(0, "0", null, null);
		} else {
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("Neg"), "cases", null, textProp));
			solIEEE.put(0, "1", null, null);
		}
		vars.set("Sign", solIEEE.getData(0));
		tempOffset = new Offset(0, 0, cases.lastElement(), "SW");
		solIEEE.highlightCell(0, null, null);
		NtIcode.highlight(1);
		l.nextStep();
		solIEEE.unhighlightCell(0, null, null);
		NtIcode.unhighlight(1);

		if (Number == Double.POSITIVE_INFINITY || Number == Double.NEGATIVE_INFINITY) {
			animalStringArraySet(solIEEE, "1", 1, IEEE_754_characteristic + 1);
			animalStringArraySet(solIEEE, "0", IEEE_754_characteristic + 2, solIEEE.getLength() - 1);
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("Inf"), "cases", null, textProp));
			l.nextStep();
			return animalStringArrayToString(solIEEE);
		} else {
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("nInf"), "cases", null, textProp));
		}
		tempOffset = new Offset(0, 0, cases.lastElement(), "SW");
		l.nextStep();

		if (Math.abs(Number) < Math.pow(2, -IEEE_754_bias + 2)) {
			underflow = true;
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("underflow"), "cases", null, textProp));
		} else {
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("normal"), "cases", null, textProp));
		}
		tempOffset = new Offset(0, 0, cases.lastElement(), "SW");
		l.nextStep();

		for (Text e : cases) {
			e.hide();
		}
		l.nextStep();

		/*
		 * to static point
		 */
		
		Vector<String[]> tosp1 = new  Vector<String[]>();
		Vector<String[]> tosp2 = new  Vector<String[]>();

		NumberInt = (int) Math.abs(Number);
		NumberFraction = Math.abs(Number) - NumberInt;
		StaticPoint1 = intToBit(NumberInt, 0, tosp1);
		StaticPoint2 = fractionToBit(NumberFraction, IEEE_754_bias + 2, tosp2);
		StaticPoint = StaticPoint1 + "." + StaticPoint2;
		vars.set("StaticPoint", StaticPoint);
		
		//text out static point
		Text SPvis = l.newText(calcOffset, t.translateMessage("toBinaryHead", new String[]{""+Number}), "staticPoint", null, textProp);
		tempOffset = calcOffset;
		calcOffset = new Offset(0, 0, SPvis, "SW");
		NtIcode.toggleHighlight(0, 2);
		
		NtIcode.highlight(3);
		l.nextStep(t.translateMessage("NtI3"));
		subCalcOut(tosp1, calcOffset, "tosp1", 4);
		SPvis.setText(SPvis.getText() + StaticPoint1 + ".", null, null);
		
		NtIcode.toggleHighlight(3, 4);
		l.nextStep(t.translateMessage("NtI4"));
		subCalcOut(tosp2, calcOffset, "tosp2", 3);
		SPvis.setText(SPvis.getText() + StaticPoint2, null, null);
		NtIcode.unhighlight(4);
		
		/*
		 * exponent+
		 * mantissa
		 */

		// exponent
		if (StaticPoint1.equals("0")) {
			exp = -1 - StaticPoint2.indexOf('1');
		} else {
			exp = StaticPoint1.length() - 1;
		}

		// shift
		Vector<String[]> schiftVis = new  Vector<String[]>();
		mantissa = shift(StaticPoint, -exp, schiftVis);
		
		NtIcode.toggleHighlight(2, 5);
		l.nextStep(t.translateMessage("NtI5"));
		subCalcOut(schiftVis, calcOffset, "schiftVis", 0);
		vars.set("Exponent", ""+ exp);
			
		SPvis.setText(t.translateMessage("floatingPoint") + mantissa, null, null);
		Text ExpVis = l.newText(calcOffset, t.translateMessage("exponent") + exp, "exponent", null, textProp);
		calcOffset = new Offset(0, 0, ExpVis, "SW");
		l.nextStep();

		// exponent vis
		NtIcode.toggleHighlight(5, 6);
		String chara;
		if (underflow) {
			exp = 0;
			ExpVis.setText(t.translateMessage("characteristic") + "0", null, null);
		} else {
			ExpVis.setText(t.translateMessage("characteristic") + exp + " + " + IEEE_754_bias, null, null);
			exp = exp + IEEE_754_bias;
			l.nextStep();
			ExpVis.setText(t.translateMessage("characteristic") + exp, null, null);
		}
		l.nextStep(t.translateMessage("NtI6"));

		Vector<String[]> charaVis = new  Vector<String[]>();
		chara = intToBit(exp, IEEE_754_characteristic, charaVis);
		
		//characteristic vis
		Text charVis = l.newText(calcOffset, t.translateMessage("toBinaryHead", new String[]{""+exp}), "staticPoint", null, textProp);
		subCalcOut(charaVis, new Offset(0, 0, charVis, "SW"), "charaVis", 4);
		charVis.setText(charVis.getText() + chara, null, null);
		bitStringToAnimalArray(solIEEE, chara, 1, IEEE_754_characteristic);
		vars.set("Characteristic", chara);
		
		solIEEE.highlightCell(1, IEEE_754_characteristic, null, null);
		l.nextStep();
		charVis.hide();
		solIEEE.unhighlightCell(1, IEEE_754_characteristic, null, null);

		// StaticPoint to Mantissa
		String prefix= "";
		NtIcode.toggleHighlight(6, 7);
		if (underflow) {
			mantissa = mantissa.replaceAll("1\\.", "1");
		}
		else {
			mantissa = mantissa.replaceAll("1\\.", "");
			prefix = t.translateMessage("pref");
		}
		mantissa = String.format("%-" + IEEE_754_mantissa + "s", mantissa).replaceAll(" ", "0");

		//mantissa vis
		String[] arg = {prefix , ""+(IEEE_754_characteristic + 1)};
		Text mantVis = l.newText(calcOffset, t.translateMessage("mantissa", arg), "mantissa", null, textProp);
		
		bitStringToAnimalArray(solIEEE, mantissa, 1 + IEEE_754_characteristic, IEEE_754_size - 1);
		vars.set("Mantissa", mantissa);
		solIEEE.highlightCell(1 + IEEE_754_characteristic, IEEE_754_size - 1, null, null);
		l.nextStep(t.translateMessage("NtI7"));
		mantVis.hide();
		solIEEE.unhighlightCell(1 + IEEE_754_characteristic, IEEE_754_size - 1, null, null);

		// IEEE 754
		return solIEEE.getData(0) + chara + mantissa;
	}

	private double IEE754toNumber(String IEEE, Offset mainOffset) {
		/*
		 * Vis Setup
		 */	
		Offset calcOffset;
		
		// PseudoCode
		Text codeHeader = l.newText(mainOffset, t.translateMessage("IEEE754ToNumber"), "IEEE754ToNumber", null,
				headlineProp);
		SourceCode ItoNcode = l.newSourceCode(new Offset(0, 10, codeHeader, "SW"), "IEEE754ToNumberCode", null,
				codeProp);
		ItoNcode.addCodeLine(t.translateMessage("ItoN0"), null, 0, null);
		ItoNcode.addCodeLine(t.translateMessage("ItoN1"), null, 0, null);
		ItoNcode.addCodeLine(t.translateMessage("ItoN2"), null, 0, null);
		ItoNcode.addCodeLine(t.translateMessage("ItoN3"), null, 0, null);
		ItoNcode.addCodeLine(t.translateMessage("ItoN4"), null, 0, null);

		// constants
		InfoBox constants = new InfoBox(l, new Offset(0, 10, ItoNcode, "SW"), 4, t.translateMessage("constants"));
		Vector<String> constText = new Vector<String>();
		constText.add(t.translateMessage("bias") + IEEE_754_bias);
		constText.add(t.translateMessage("size") + IEEE_754_size);
		constText.add(t.translateMessage("lchar") + IEEE_754_characteristic);
		constText.add(t.translateMessage("lmant") + IEEE_754_mantissa);
		constants.setText(constText);

		//danger : absolute positioning
		@SuppressWarnings("unused")
		Text calcHeader = l.newText(new Offset(30, -42, ItoNcode, "NE"), t.translateMessage("calcHeader"), "calcHeader",
				null, headlineProp);

		StringArray AIEEE;
		String[] IEEEArray = new String[IEEE_754_size];
		Text arHeader = l.newText(new Offset(40, 0, ItoNcode, "NE"), t.translateMessage("IEEEInput") , "input", null, numberProp);
		AIEEE = l.newStringArray(new Offset(2, 0, arHeader, "NE"), IEEEArray, "solIEEE", null, arProp);
		bitStringToAnimalArray(AIEEE, IEEE, 0, IEEE_754_size - 1);

		calcOffset = new Offset(0, 50, arHeader, "SW");

		// Partition
		String sign = IEEE.substring(0, 1);
		String chararacteristic = IEEE.substring(1, IEEE_754_characteristic + 1);
		String mantissa = IEEE.substring(IEEE_754_characteristic + 1, IEEE_754_size);
		double dSign;
		int dExp;
		boolean underflow = false;
		String shiftedMantissa;
		
		vars.set("Sign", sign);
		vars.set("Characteristic", chararacteristic);
		vars.set("Mantissa", mantissa);

		/*
		 * Special Cases
		 */
		ItoNcode.highlight(0);
		Vector<Text> cases = new Vector<Text>();
		Offset tempOffset = calcOffset;
		l.nextStep(t.translateMessage("ItoN0"));
		if (chararacteristic.matches("1{" + IEEE_754_characteristic + "}")) {
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("max"), "cases", null, textProp));
			tempOffset = new Offset(0, 0, cases.lastElement(), "SW");
			l.nextStep();
			AIEEE.unhighlightCell(1, IEEE_754_characteristic, null, null);
			AIEEE.highlightCell(IEEE_754_characteristic + 1, IEEE_754_size - 1, null, null);

			if (mantissa.matches("0{" + IEEE_754_mantissa + "}")) {
				cases.add(l.newText(tempOffset, "-" + t.translateMessage("Inf"), "cases", null, textProp));
				l.nextStep();

				if (sign.equals("0")) {
					return Double.POSITIVE_INFINITY;
				} else {
					return Double.NEGATIVE_INFINITY;
				}
			} else {
				cases.add(l.newText(tempOffset, "-" + t.translateMessage("NaN"), "cases", null, textProp));
				l.nextStep();

				return Double.NaN;
			}
		} else if (chararacteristic.matches("0{" + IEEE_754_characteristic + "}")) {
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("min"), "cases", null, textProp));
			tempOffset = new Offset(0, 0, cases.lastElement(), "SW");
			l.nextStep();
			AIEEE.unhighlightCell(1, IEEE_754_characteristic, null, null);
			AIEEE.highlightCell(IEEE_754_characteristic + 1, IEEE_754_size - 1, null, null);

			if (mantissa.matches("0{" + IEEE_754_mantissa + "}")) {
				cases.add(l.newText(tempOffset, "-" + t.translateMessage("Zero"), "cases", null, textProp));
				l.nextStep();

				return 0;
			} else {
				cases.add(l.newText(tempOffset, "-" + t.translateMessage("underflow"), "cases", null, textProp));
				l.nextStep();
				AIEEE.unhighlightCell(IEEE_754_characteristic + 1, IEEE_754_size - 1, null, null);

				underflow = true;
			}
		} else {
			cases.add(l.newText(tempOffset, "-" + t.translateMessage("normal"), "cases", null, textProp));
			l.nextStep();
			AIEEE.unhighlightCell(1, IEEE_754_characteristic, null, null);
		}
		for (Text e : cases) {
			e.hide();
		}

		/*
		 * exponent
		 */
		ItoNcode.toggleHighlight(0, 1);
		AIEEE.highlightCell(1, IEEE_754_characteristic, null, null);
		l.nextStep(t.translateMessage("ItoN1"));
		String expVis;
		if (underflow) {
			dExp = 1 - IEEE_754_bias;
			expVis = "1 - " + IEEE_754_bias + " = " + dExp;
		} else {
			Vector<String[]> BtNVis = new  Vector<String[]>();
			int temp = (int) bitStringToNumber(chararacteristic, BtNVis);
			dExp = temp - IEEE_754_bias;
			
			bitToDecOut(BtNVis, AIEEE , calcOffset, "BtNVisC", 3, 1, 0);
			expVis = ""+ temp + " - " + IEEE_754_bias + " = " + dExp;
			
			mantissa = "1." + mantissa;
		}
		Text solNumber = l.newText(calcOffset, t.translateMessage("exponent") + expVis, "tempMem", null, textProp);
		calcOffset = new Offset(0, 0, solNumber, "SW");
		l.nextStep();
		solNumber.setText(t.translateMessage("exponent") + dExp, null, null);
		vars.set("Exponent", ""+dExp);
		l.nextStep();
		AIEEE.unhighlightCell(1, IEEE_754_characteristic, null, null);

		/*
		 * Static Point
		 */
		Vector<String[]> shiftVis = new  Vector<String[]>();
		shiftedMantissa = shift(mantissa, dExp, shiftVis);
		
		ItoNcode.toggleHighlight(1, 2);
		AIEEE.highlightCell(IEEE_754_characteristic + 1, IEEE_754_size - 1, null, null);
		l.nextStep(t.translateMessage("ItoN2"));
		subCalcOut(shiftVis, calcOffset, "shift", 0);
		solNumber.setText(t.translateMessage("staticPoint") + shiftedMantissa.replaceAll("(0*)([01]+.[01]+)(0*)", "$2"), null, null);
		vars.set("StaticPoint", shiftedMantissa.replaceAll("(0*)([01]+.[01]+)(0*)", "$2"));
		l.nextStep();

		/*
		 * to Number
		 */
		Vector<String[]> BtNVis = new  Vector<String[]>();
		double retNumb = bitStringToNumber(shiftedMantissa, BtNVis);
		
		ItoNcode.toggleHighlight(2, 3);
		l.nextStep(t.translateMessage("ItoN3"));
		bitToDecOut(BtNVis, AIEEE, calcOffset, "BtNVisM", 3, IEEE_754_characteristic, -1);
		solNumber.setText(t.translateMessage("number") + retNumb, null, null);
		l.nextStep();
		AIEEE.unhighlightCell(IEEE_754_characteristic + 1, IEEE_754_size - 1, null, null);

		/*
		 * sign
		 */
		ItoNcode.toggleHighlight(3, 4);
		l.nextStep(t.translateMessage("ItoN4"));
		AIEEE.highlightCell(0, null, null);
		dSign = Math.pow(-1, Integer.parseInt(sign));
		retNumb = dSign * retNumb;
		solNumber.setText(t.translateMessage("number") + retNumb, null, null);
		l.nextStep();
		AIEEE.unhighlightCell(0, null, null);

		return retNumb;
	}

	private void IEEE_754_intro(String Input) {
		double Number = Double.NaN;
		String IEEE = null;

		if (Input.matches("^IEEE[01]*")) {
			IEEE = String.format("%-" + IEEE_754_size + "s", Input.substring(4)).replaceAll(" ", "0");
			vars.set("IEEE754Number", ""+IEEE);
		} else if(Input.matches("[0-9]+\\.?[0-9]*")){
			Number = Double.parseDouble(Input);
			vars.set("DecimalNumber", ""+Number);
		}else {
			Number = Double.NaN;
			vars.set("DecimalNumber", ""+Number);
		}

		Text title = l.newText(new Coordinates(30, 10), t.translateMessage("title"), "title", null, titleProp);
		Offset mainOffset = new Offset(10, 10, title, "SW");
		Rect titleRec = l.newRect(new Offset(-10, 0, title, "NW"), new Offset(10, 0, title, "SE"), "title", null, rectProp);

		// intro
		Vector<String> introText = new Vector<String>();
		loadText(introText, "intro");
		InfoBox intro = new InfoBox(l, mainOffset, introText.size(), t.translateMessage("introHeader"));
		intro.setText(introText);

		l.nextStep(t.translateMessage("introHeader"));
		intro.hide();

		// run
		String outroID;
		Vector<String> outroText = new Vector<String>();
		if (IEEE == null) {
			IEEE = NumberToIEEE754(Number, mainOffset);
			outroID = "outIeee";
			vars.set("IEEE754Number", ""+IEEE);
			
		} else {
			Number = IEE754toNumber(IEEE, mainOffset);	
			outroID = "outDouble";
			vars.set("DecimalNumber", ""+Number);
		}
		
		//outro
		l.hideAllPrimitivesExcept(title);
		titleRec.show();
		
		String[] IEEEArray = new String[IEEE_754_size];
		StringArray AIEEE = l.newStringArray(new Offset(10, 35, title, "SW"), IEEEArray, "solIEEE", null, arProp);
		bitStringToAnimalArray(AIEEE, IEEE, 0, IEEE_754_size - 1);
		
		loadText(outroText, outroID);
		InfoBox outro = new InfoBox(l, new Offset(0, 45, AIEEE, "SW"), outroText.size(), t.translateMessage("outroHeader"), IEEE, ""+Number, vars.get("StaticPoint"), ""+IEEE_754_bias, ""+IEEE_754_characteristic, ""+IEEE_754_mantissa);
		outro.setText(outroText);
		
		l.nextStep(t.translateMessage("outroHeader"));
	}
	
	//untility

    /*
     * Locale.GERMANY
     * Locale.US
     */
	public IEEE754(Locale loc) {
		genLocale = loc;
		t = new Translator("resources/IEEE754", genLocale);
	}

	public void init() {
		l = new AnimalScript("IEEE 754 Converter", "Lutz Langhorst", 800, 600);
		l.setStepMode(true);
		vars = l.newVariables();
	}

	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		
		int IEEE_754_bias = (Integer) primitives.get("IEEE754_bias");
		int IEEE_754_size = (Integer) primitives.get("IEEE754_size");
		int IEEE_754_characteristic = (Integer) primitives.get("IEEE754_characteristic_size");
		
		String err = t.translateMessage("assert");
		boolean test = true;

		if(IEEE_754_bias < 1) {
			err += "IEEE_754_bias > 0 ; ";
			test = false;
		}
		if(IEEE_754_size < 1) {
			err += "IEEE_754_size > 0 ; ";
			test = false;
		}
		if(IEEE_754_characteristic < 1) {
			err += "IEEE_754_characteristic > 0 ; ";
			test = false;
		}
		if(IEEE_754_size <= IEEE_754_characteristic) {
			err += "IEEE_754_size > IEEE_754_characteristic ; ";
			test = false;
		}
		
		if(!test)
			throw new IllegalArgumentException(err);
		return test;
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		
		/*
		 * Properties
		 */
		Font temp;
		String Input = (String) primitives.get("Input");

		sub_step_limit = (Integer) primitives.get("sub_step_limit");
		IEEE_754_size = (Integer) primitives.get("IEEE754_size");
		IEEE_754_characteristic = (Integer) primitives.get("IEEE754_characteristic_size");
		IEEE_754_mantissa = IEEE_754_size - IEEE_754_characteristic - 1;
		IEEE_754_bias = (Integer) primitives.get("IEEE754_bias");

		arProp = (ArrayProperties) props.getPropertiesByName("array");
		codeProp = (SourceCodeProperties) props.getPropertiesByName("code");
		textProp = (TextProperties) props.getPropertiesByName("text");
		titleProp = (TextProperties) props.getPropertiesByName("title");
		headlineProp = (TextProperties) props.getPropertiesByName("headline");
		rectProp = (RectProperties) props.getPropertiesByName("rectangle");
		numberProp = new TextProperties();
		
		temp = ((Font) textProp.getItem(AnimationPropertiesKeys.FONT_PROPERTY).get());
		temp = temp.deriveFont(Font.BOLD, 18);
		numberProp.set(AnimationPropertiesKeys.FONT_PROPERTY, temp);
		numberProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, textProp.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get());

		temp = ((Font) headlineProp.getItem(AnimationPropertiesKeys.FONT_PROPERTY).get());
		temp = temp.deriveFont(Font.BOLD, 16);
		headlineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, temp);

		temp = ((Font) titleProp.getItem(AnimationPropertiesKeys.FONT_PROPERTY).get());
		temp = temp.deriveFont(Font.BOLD, 20);
		titleProp.set(AnimationPropertiesKeys.FONT_PROPERTY, temp);

		
		temp = ((Font) textProp.getItem(AnimationPropertiesKeys.FONT_PROPERTY).get());
		matProp = new MatrixProperties();
		matProp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "right");
		matProp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");
		matProp.set(AnimationPropertiesKeys.FONT_PROPERTY, temp);
		matProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, ((Color) arProp.getItem(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY).get()));
		
		Color textCol = ((Color) textProp.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get());
		arMarcProp =  new ArrayMarkerProperties();
		arMarcProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, textCol);

		/*
		 * Variables
		 */
		vars.declare("double", "DecimalNumber");
		vars.declare("String", "StaticPoint");
		vars.declare("String", "IEEE754Number");
		vars.declare("int", "IEEE754Size", ""+IEEE_754_size);
		vars.declare("int", "IEEE754CharacteristicSize", ""+IEEE_754_characteristic);
		vars.declare("int", "IEEE754MantissaSize", ""+IEEE_754_mantissa);
		vars.declare("int", "Bias", ""+ IEEE_754_bias);
		vars.declare("int", "Exponent");
		vars.declare("String", "Sign");
		vars.declare("String", "Characteristic");
		vars.declare("String", "Mantissa");
		
		IEEE_754_intro(Input);
		return l.toString();
	}

	public String getName() {
		return "IEEE 754 Converter";
	}

	public String getAlgorithmName() {
		return "IEEE 754";
	}

	public String getAnimationAuthor() {
		return "Lutz Langhorst";
	}

	//HTML !!!
	public String getDescription() {
		String temp = "";
		Vector<String> text = new Vector<String>();
		loadText(text, "xintro");
		for(String e : text) {
			temp = temp + e + "\n";
		}
		return temp;
	}

	public String getCodeExample() {
		return "Sign.Characteristic.Mantissa\nNumber = Sign * Mantissa * 2 ^ (Characteristic - Bias)";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return genLocale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

//	//test
//	public static void main(String[] args) {
//		// Animal.startAnimationFromAnimalScriptCode(l.toString());
//		Generator generator = new IEEE754(Locale.GERMANY); // GERMANY , US
//		Animal.startGeneratorWindow(generator);
//	}
}