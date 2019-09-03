package generators.searching.boyermoore;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.searching.helpers.AbstractStringSearchGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringMatrix;
import algoanim.properties.MatrixProperties;
import algoanim.util.Offset;

public class BoyerMoore extends AbstractStringSearchGenerator {

	protected MatrixProperties matrixProperties;
	private List<Character> skipKeyList;
	private List<Integer> skipValueList;
	private StringMatrix animationSkipMap;
	private StringMatrix animationSkipMap2;

	private Map<Character, Integer> badCharacterSkip;
	private int[] goodSuffixSkip;

  public BoyerMoore(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

	@Override
	public String getAnimationAuthor() {
		return "Torsten Kohlhaas";
	}

	@Override
	public String getCodeExample() {
		return "private char[] pattern, text;\n" + "private int patternLength, textLength;\n"
				+ "private Map&#60Character, Integer&#62 badCharacterSkip;\n"
				+ "private int[] goodSuffixSkip;\n" + "\n"
				+ "public List&#60Integer&#62 search(String inputText, String inputPattern) {\n"
				+ "  if (inputIsBad(inputText, inputPattern)) {\n"
				+ "    return new ArrayList&#60Integer&#62();\n" + "  }\n"
				+ "  setText(inputText);\n" + "  setPattern(inputPattern);\n"
				+ "  initializeBadCharacter();\n" + "  initializeGoodSuffix();\n"
				+ "  return boyerMooreSearch();\n" + "}\n" + "\n"
				+ "private boolean inputIsBad(String inputText, String inputPattern) {\n"
				+ "  return (inputText == null || inputText.isEmpty()\n"
				+ "    || inputPattern == null || inputPattern.isEmpty() || inputPattern\n"
				+ "    .length() &#62 inputText.length());\n" + "}\n" + "\n"
				+ "private void setText(String inputText) {\n"
				+ "  textLength = inputText.length();\n" + "  text = inputText.toCharArray();\n"
				+ "}\n" + "\n" + "private void setPattern(String inputPattern) {\n"
				+ "  patternLength = inputPattern.length();\n"
				+ "  pattern = inputPattern.toCharArray();\n" + "}\n" + "\n"
				+ "private void initializeBadCharacter() {\n"
				+ "  skipValues = new HashMap&#60Character, Integer&#62(pattern.length);\n"
				+ "  for (int i = 0; i &#60 pattern.length; i++) {\n"
				+ "    skipValues.put(pattern[i], i);\n" + "  }\n" + "}\n" + "\n"
				+ "private void initializeGoodSuffix() {\n"
				+ "  goodSuffixSkip = new int[patternLength + 1];\n"
				+ "  int[] temporaryTable = new int[patternLength + 1];\n"
				+ "  int i = patternLength, j = patternLength + 1;\n"
				+ "  temporaryTable[i] = j;\n" + "  while (i &#62 0) {\n"
				+ "    while (j &#60= patternLength && pattern[i - 1] != pattern[j - 1]) {\n"
				+ "      if (goodSuffixSkip[j] == 0) {\n" + "        goodSuffixSkip[j] = j - i;\n"
				+ "      }\n" + "      j = temporaryTable[j];\n" + "    }\n" + "    i--;\n"
				+ "    j--;\n" + "    temporaryTable[i] = j;\n" + "  }\n" + "  int k, l;\n"
				+ "  l = temporaryTable[0];\n" + "  for (k = 0; k &#60= patternLength; k++) {\n"
				+ "    if (goodSuffixSkip[k] == 0) {\n" + "      goodSuffixSkip[k] = l;\n"
				+ "    }\n" + "    if (k == l) {\n" + "      j = temporaryTable[l];\n" + "    }\n"
				+ "  }\n" + "}\n" + "\n" + "private List&#60Integer&#62 boyerMooreSearch() {\n"
				+ "  List&#60Integer&#62 occurrences = new ArrayList&#60Integer&#62();\n"
				+ "  int i = 0, j;\n" + "  while (i &#60= textLength - patternLength) {\n"
				+ "    j = patternLength - 1;\n"
				+ "    while (j &#62= 0 && pattern[j] == text[i + j]) {\n" + "      j--;\n"
				+ "    }\n" + "    if (j &#60 0) {\n" + "      occurrences.add(i);\n"
				+ "      i += goodSuffixSkip[0];\n" + "    } else {\n"
				+ "      Integer bcSkip = badCharacterSkip.get(text[i + j]);\n"
				+ "      if (bcSkip == null) {\n" + "        bcSkip = -1;\n" + "      }\n"
				+ "      i += Math.max(goodSuffixSkip[j + 1], j - bcSkip);\n" + "    }\n" + "  }\n"
				+ "  return occurrences;\n" + "}\n";
	}

	@Override
	public String getDescription() {
		return translator.translateMessage("descriptionLine1")
				+ translator.translateMessage("descriptionLine2")
				+ translator.translateMessage("descriptionLine3")
				+ translator.translateMessage("descriptionLine4")
				+ translator.translateMessage("descriptionLine5")
				+ translator.translateMessage("descriptionLine6");
	}

	protected void readProperties(AnimationPropertiesContainer props) {
		super.readProperties(props);
		matrixProperties = (MatrixProperties) props.getPropertiesByName("MatrixProperties");
	}

	@Override
	protected int getCodeHeigth() {
		return 27;
	}

	@Override
	protected int getCodeWidth() {
		return 80;
	}

	protected List<String> getMainCode() {
		List<String> codeLines = new ArrayList<String>();
		codeLines.add("private char[] pattern, text;");
		codeLines.add("private int patternLength, textLength;");
		codeLines.add("private Map<Character, Integer> badCharacterSkip;");
		codeLines.add("private int[] goodSuffixSkip;");
		codeLines.add("");
		codeLines.add("public List<Integer> search(String inputText, String inputPattern) {");
		codeLines.add("  if (inputIsBad(inputText, inputPattern)) {");
		codeLines.add("    return new ArrayList<Integer>();");
		codeLines.add("  }");
		codeLines.add("  setText(inputText);");
		codeLines.add("  setPattern(inputPattern);");
		codeLines.add("  initializeBadCharacter();");
		codeLines.add("  initializeGoodSuffix();");
		codeLines.add("  return boyerMooreSearch();");
		codeLines.add("}");
		return codeLines;
	}

	@Override
	protected List<Integer> search(String inputText, String inputPattern) { // code line 5
		// check input
		mainCode.highlight(6);
		if (inputIsBad(inputText, inputPattern)) { // code line 6
			mainCode.unhighlight(6);

			mainCode.highlight(7);
			setExplanation(translator.translateMessage("abortSearch"));
			lang.nextStep(); // step
			mainCode.unhighlight(7);
			return new ArrayList<Integer>(); // code line 7
		} // code line 8
		mainCode.unhighlight(6);

		mainCode.highlight(9);
		setText(inputText); // code line 9
		mainCode.unhighlight(9);

		mainCode.highlight(10);
		setPattern(inputPattern); // code line 10
		mainCode.unhighlight(10);

		mainCode.highlight(11);
		initializeBadCharacter(); // code line 11
		mainCode.unhighlight(11);

		mainCode.highlight(12);
		initializeGoodSuffix(); // code line 12
		mainCode.unhighlight(12);

		mainCode.highlight(13);
		List<Integer> hits = boyerMooreSearch(); // code line 13
		mainCode.unhighlight(13);

		// show results
		if (hits.isEmpty()) {
			setExplanation(translator.translateMessage("patternNotFound"));
		} else {
			setExplanation(translator.translateMessage("hits", String.valueOf(hits.size())));
			for (Integer hit : hits) {
				animationText.highlightCell(hit, hit + patternLength - 1, null, null);
			}
		}
		lang.nextStep(); // step
		animationText.unhighlightCell(0, animationText.getLength(), null, null);
		explanation.hide();

		return hits;
	} // code line 14

	private void initializeBadCharacter() { // code line 0
		// show code
		phaseCode = lang.newSourceCode(phaseCodeCoordinates, "initializeBadCharacter", null,
				sourceCodeProperties);
		phaseCode.addCodeLine("private void initializeBadCharacter() {",
				"initializeBadCharacter_0", 0, null);
		phaseCode.addCodeLine("skipValues = new HashMap<Character, Integer>(pattern.length);",
				"initializeBadCharacter_1", 1, null);
		phaseCode.addCodeLine("for (int i = 0; i < pattern.length; i++) {",
				"initializeBadCharacter_2", 1, null);
		phaseCode
				.addCodeLine("skipValues.put(pattern[i], i);", "initializeBadCharacter_3", 2, null);
		phaseCode.addCodeLine("}", "initializeBadCharacter_4", 1, null);
		phaseCode.addCodeLine("}", "initializeBadCharacter_5", 0, null);

		badCharacterSkip = new HashMap<Character, Integer>(patternLength); // code line 1
		phaseCode.highlight(1);
		setExplanation(translator.translateMessage("createTable1"));
		String[][] skipMatrix = new String[2][patternLength];
		for (int i = 0; i < patternLength; i++) {
			skipMatrix[0][i] = "";
			skipMatrix[1][i] = "";
		}
		animationSkipMap = lang.newStringMatrix(new Offset(0, 20, "pattern", "SW"), skipMatrix,
				"skipMap", null, matrixProperties);
		lang.newText(new Offset(-100, 0, "skipMap", "NW"),
				translator.translateMessage("labelBadCharacter"), "label_badCharacter", null);
		lang.nextStep(); // step
		phaseCode.unhighlight(1);

		phaseCode.highlight(2);
		phaseCode.highlight(3);
		phaseCode.highlight(4);
		setExplanation(translator.translateMessage("explainTable1"));
		skipKeyList = new ArrayList<Character>(patternLength);
		skipValueList = new ArrayList<Integer>(patternLength);
		ArrayMarker patternMarker = lang
				.newArrayMarker(animationPattern, -1, "patternMarker", null);
		for (int i = 0; i < patternLength; i++) { // code line 2
			patternMarker.increment(null, null);
			char key = pattern[i];
			int value = i;
			badCharacterSkip.put(key, value);
			int position = skipKeyList.indexOf(key);
			if (position == -1) {
				skipKeyList.add(key);
				skipValueList.add(value);
				position = skipKeyList.indexOf(key);
			} else {
				skipValueList.set(position, value);
			}

			animationSkipMap.highlightCell(0, position, null, null);
			animationSkipMap.put(0, position, String.valueOf(key), null, null);
			animationSkipMap.highlightCell(1, position, null, null);
			animationSkipMap.put(1, position, String.valueOf(value), null, null);
			lang.nextStep(); // step
			animationSkipMap.unhighlightCell(0, position, null, null);
			animationSkipMap.unhighlightCell(1, position, null, null);
		} // code line 4
		patternMarker.hide();
		phaseCode.unhighlight(2);
		phaseCode.unhighlight(3);
		phaseCode.unhighlight(4);
		phaseCode.hide();
	} // code line 5

	private void initializeGoodSuffix() {
		// show code
		phaseCode = lang.newSourceCode(phaseCodeCoordinates, "initializeGoodSuffix", null,
				sourceCodeProperties);
		phaseCode.addCodeLine("private void initializeGoodSuffix() {", "initializeGoodSuffix_0", 0,
				null);
		phaseCode.addCodeLine("goodSuffixSkip = new int[patternLength + 1];",
				"initializeGoodSuffix_1", 1, null);
		phaseCode.addCodeLine("int[] temporaryTable = new int[patternLength + 1];",
				"initializeGoodSuffix_2", 1, null);
		phaseCode.addCodeLine("int i = patternLength, j = patternLength + 1;",
				"initializeGoodSuffix_3", 1, null);
		phaseCode.addCodeLine("temporaryTable[i] = j;", "initializeGoodSuffix_4", 1, null);
		phaseCode.addCodeLine("while (i > 0) {", "initializeGoodSuffix_5", 1, null);
		phaseCode.addCodeLine("while (j <= patternLength && pattern[i - 1] != pattern[j - 1]) {",
				"initializeGoodSuffix_6", 2, null);
		phaseCode.addCodeLine("if (goodSuffixSkip[j] == 0) {", "initializeGoodSuffix_7", 3, null);
		phaseCode.addCodeLine("goodSuffixSkip[j] = j - i;", "initializeGoodSuffix_8", 4, null);
		phaseCode.addCodeLine("}", "initializeGoodSuffix_9", 3, null);
		phaseCode.addCodeLine("j = temporaryTable[j];", "initializeGoodSuffix_10", 3, null);
		phaseCode.addCodeLine("}", "initializeGoodSuffix_11", 2, null);
		phaseCode.addCodeLine("i--;", "initializeGoodSuffix_12", 2, null);
		phaseCode.addCodeLine("j--;", "initializeGoodSuffix_13", 2, null);
		phaseCode.addCodeLine("temporaryTable[i] = j;", "initializeGoodSuffix_14", 2, null);
		phaseCode.addCodeLine("}", "initializeGoodSuffix_15", 1, null);
		phaseCode.addCodeLine("int k, l;", "initializeGoodSuffix_16", 1, null);
		phaseCode.addCodeLine("l = temporaryTable[0];", "initializeGoodSuffix_17", 1, null);
		phaseCode.addCodeLine("for (k = 0; k <= patternLength; k++) {", "initializeGoodSuffix_18",
				1, null);
		phaseCode.addCodeLine("if (goodSuffixSkip[k] == 0) {", "initializeGoodSuffix_19", 2, null);
		phaseCode.addCodeLine("goodSuffixSkip[k] = l;", "initializeGoodSuffix_20", 3, null);
		phaseCode.addCodeLine("}", "initializeGoodSuffix_21", 2, null);
		phaseCode.addCodeLine("if (k == l) {", "initializeGoodSuffix_22", 2, null);
		phaseCode.addCodeLine("j = temporaryTable[l];", "initializeGoodSuffix_23", 3, null);
		phaseCode.addCodeLine("}", "initializeGoodSuffix_24", 2, null);
		phaseCode.addCodeLine("}", "initializeGoodSuffix_25", 1, null);
		phaseCode.addCodeLine("}", "initializeGoodSuffix_26", 0, null);

		goodSuffixSkip = new int[patternLength + 1]; // code line 1
		phaseCode.highlight(1);
		setExplanation(translator.translateMessage("createTable2"));
		String[][] skipMatrix = new String[3][patternLength + 1];
		for (int i = 0; i <= patternLength; i++) {
			skipMatrix[0][i] = String.valueOf(i);
			skipMatrix[1][i] = "0";
			skipMatrix[2][i] = "";
		}
		animationSkipMap2 = lang.newStringMatrix(new Offset(0, 20, "skipMap", "SW"), skipMatrix,
				"skipMap2", null, matrixProperties);
		lang.newText(new Offset(-100, 0, "skipMap2", "NW"),
				translator.translateMessage("labelGoodSuffix"), "label_goodSuffix", null);
		lang.nextStep(); // step
		phaseCode.unhighlight(1);

		int[] temporaryTable = new int[patternLength + 1]; // code line 2
		phaseCode.highlight(2);
		setExplanation(translator.translateMessage("createTable3"));
		for (int i = 0; i <= patternLength; i++) {
			animationSkipMap2.put(2, i, "0", null, null);
		}
		lang.nextStep(); // step
		phaseCode.unhighlight(2);

		int i = patternLength, j = patternLength + 1; // code line 3
		phaseCode.highlight(3);
		setExplanation(translator.translateMessage("explainIJ"));
		lang.nextStep(); // step
		phaseCode.unhighlight(3);

		temporaryTable[i] = j; // code line 4
		phaseCode.highlight(4);
		setExplanation(translator.translateMessage("emptyStringEntry"));
		animationSkipMap2.put(2, i, String.valueOf(j), null, null);
		animationSkipMap2.highlightCell(2, i, null, null);
		lang.nextStep(); // step
		animationSkipMap2.unhighlightCell(2, i, null, null);
		phaseCode.unhighlight(4);

		phaseCode.highlight(5);
		setExplanation(translator.translateMessage("analyzePattern"));
		lang.nextStep(); // step
		phaseCode.unhighlight(5);
		while (i > 0) { // code line 5

			if (i != patternLength) {
				phaseCode.highlight(5);
				setExplanation(translator.translateMessage("analyzeUntil"));
				lang.nextStep(); // step
				phaseCode.unhighlight(5);
			}

			while (j <= patternLength && pattern[i - 1] != pattern[j - 1]) { // code line 6
				increaseComparisonCount();
				phaseCode.highlight(6);
				setExplanation(translator.translateMessage("borderNotExtendable"));
				lang.nextStep(); // step
				phaseCode.unhighlight(6);

				phaseCode.highlight(7);
				animationSkipMap2.highlightCell(1, j, null, null);
				if (goodSuffixSkip[j] == 0) { // code line 7
					goodSuffixSkip[j] = j - i; // code line 8
					animationSkipMap2.put(1, j, String.valueOf(j - i), null, null);
					phaseCode.highlight(8);
					setExplanation(translator.translateMessage("setSkipValue"));
					lang.nextStep(); // step
					phaseCode.unhighlight(8);
				} // code line 9
				else {
					setExplanation(translator.translateMessage("doNotSetSkipValue"));
					lang.nextStep(); // step
				}
				animationSkipMap2.unhighlightCell(1, j, null, null);
				phaseCode.unhighlight(7);

				phaseCode.highlight(10);
				setExplanation(translator.translateMessage("setJ"));
				animationSkipMap2.highlightCell(2, j, null, null);
				lang.nextStep(); // step
				phaseCode.unhighlight(10);
				animationSkipMap2.unhighlightCell(2, j, null, null);
				j = temporaryTable[j]; // code line 10

			} // code line 11
			if (j > patternLength) {
				increaseComparisonCount();
			}

			i--; // code line 12
			j--; // code line 13
			temporaryTable[i] = j; // code line 14
			animationPattern.highlightCell(i, null, null);
			phaseCode.highlight(12);
			phaseCode.highlight(13);
			phaseCode.highlight(14);
			setExplanation(translator.translateMessage("nextEntry"));
			animationSkipMap2.put(2, i, String.valueOf(j), null, null);
			animationSkipMap2.highlightCell(2, i, null, null);
			lang.nextStep(); // step
			phaseCode.unhighlight(12);
			phaseCode.unhighlight(13);
			phaseCode.unhighlight(14);
			animationSkipMap2.unhighlightCell(2, i, null, null);
		} // code line 15
		for (int x = 0; x < patternLength; x++) {
			animationPattern.unhighlightCell(x, null, null);
		}

		setExplanation(translator.translateMessage("remainingEntries"));
		lang.nextStep(); // step

		int k, l; // code line 16
		phaseCode.highlight(16);
		setExplanation(translator.translateMessage("explainKL"));
		lang.nextStep(); // step
		phaseCode.unhighlight(16);

		l = temporaryTable[0]; // code line 17
		phaseCode.highlight(17);
		setExplanation(translator.translateMessage("explainSkipValue"));
		lang.nextStep(); // step
		phaseCode.unhighlight(17);

		phaseCode.highlight(18);
		setExplanation(translator.translateMessage("fillSkipMap"));
		lang.nextStep(); // step
		phaseCode.unhighlight(18);
		for (k = 0; k <= patternLength; k++) { // code line 18

			phaseCode.highlight(19);
			if (goodSuffixSkip[k] == 0) { // code line 19
				goodSuffixSkip[k] = l; // code line 20
				phaseCode.highlight(20);
				setExplanation(translator.translateMessage("fillSkipValue", String.valueOf(l)));
				animationSkipMap2.highlightCell(1, k, null, null);
				animationSkipMap2.put(1, k, String.valueOf(l), null, null);
				lang.nextStep(); // step
				phaseCode.unhighlight(20);
				animationSkipMap2.unhighlightCell(1, k, null, null);

			} // code line 21
			else {
				setExplanation(translator.translateMessage("doNotChangeSkipValue"));
				animationSkipMap2.highlightCell(1, l, null, null);
				lang.nextStep(); // step
				animationSkipMap2.unhighlightCell(1, l, null, null);
			}
			phaseCode.unhighlight(19);

			if (k == l) { // code line 22
				j = temporaryTable[l]; // code line 23
			} // code line 24
			phaseCode.highlight(22);
			phaseCode.highlight(23);
			phaseCode.highlight(24);
			setExplanation(translator.translateMessage("shortSuffix"));
			lang.nextStep(); // step
			phaseCode.unhighlight(22);
			phaseCode.unhighlight(23);
			phaseCode.unhighlight(24);

		} // code line 25
		for (int f = 0; f <= patternLength; f++) {
			animationSkipMap2.put(2, f, " ", null, null);
		}
		phaseCode.hide();
	} // code line 26

	private List<Integer> boyerMooreSearch() {
		// show code
		phaseCode = lang.newSourceCode(phaseCodeCoordinates, "boyerMooreSearch", null,
				sourceCodeProperties);
		phaseCode.addCodeLine("private List<Integer> boyerMooreSearch() {", "boyerMooreSearch_0",
				0, null);
		phaseCode.addCodeLine("List<Integer> occurrences = new ArrayList<Integer>();",
				"boyerMooreSearch_1", 1, null);
		phaseCode.addCodeLine("int i = 0, j;", "boyerMooreSearch_2", 1, null);
		phaseCode.addCodeLine("while (i <= textLength - patternLength) {", "boyerMooreSearch_3", 1,
				null);
		phaseCode.addCodeLine("j = patternLength - 1;", "boyerMooreSearch_4", 2, null);
		phaseCode.addCodeLine("while (j >= 0 && pattern[j] == text[i + j]) {",
				"boyerMooreSearch_5", 2, null);
		phaseCode.addCodeLine("j--;", "boyerMooreSearch_6", 3, null);
		phaseCode.addCodeLine("}", "boyerMooreSearch_7", 2, null);
		phaseCode.addCodeLine("if (j < 0) {", "boyerMooreSearch_8", 2, null);
		phaseCode.addCodeLine("occurrences.add(i);", "boyerMooreSearch_9", 3, null);
		phaseCode.addCodeLine("i += goodSuffixSkip[0];", "boyerMooreSearch_10", 3, null);
		phaseCode.addCodeLine("} else {", "boyerMooreSearch_11", 2, null);
		phaseCode.addCodeLine("Integer bcSkip = badCharacterSkip.get(text[i + j]);",
				"boyerMooreSearch_12", 3, null);
		phaseCode.addCodeLine("if (bcSkip == null) {", "boyerMooreSearch_13", 3, null);
		phaseCode.addCodeLine("bcSkip = -1;", "boyerMooreSearch_14", 4, null);
		phaseCode.addCodeLine("}", "boyerMooreSearch_15", 3, null);
		phaseCode.addCodeLine("i += Math.max(goodSuffixSkip[j + 1], j - bcSkip);",
				"boyerMooreSearch_16", 3, null);
		phaseCode.addCodeLine("}", "boyerMooreSearch_17", 2, null);
		phaseCode.addCodeLine("}", "boyerMooreSearch_18", 1, null);
		phaseCode.addCodeLine("return occurrences;", "boyerMooreSearch_19", 1, null);
		phaseCode.addCodeLine("}", "boyerMooreSearch_20", 0, null);

		List<Integer> occurrences = new ArrayList<Integer>(); // code line 1
		phaseCode.highlight(1);
		setExplanation(translator.translateMessage("initOccurrences"));
		lang.nextStep(); // step
		phaseCode.unhighlight(1);

		int i = 0, j; // code line 2
		phaseCode.highlight(2);
		setExplanation(translator.translateMessage("explainIJ2"));
		lang.nextStep(); // step
		phaseCode.unhighlight(2);

		patternMarker = lang.newArrayMarker(animationPattern, patternLength - 1, "patternMarker",
				null);
		patternMarker.hide();
		textMarker = lang.newArrayMarker(animationText, patternLength - 1, "textMarker", null);
		textMarker.hide();
		while (i <= textLength - patternLength) { // code line 3
			phaseCode.highlight(3);
			setExplanation(translator.translateMessage("searchTillEnd"));
			lang.nextStep(); // step
			phaseCode.unhighlight(3);

			j = patternLength - 1; // code line 4
			animationText.highlightElem(i, i + patternLength - 1, null, null);
			phaseCode.highlight(4);
			setExplanation(translator.translateMessage("comparisonStart"));
			textMarker.move(i + j, null, null);
			textMarker.show();
			patternMarker.move(j, null, null);
			patternMarker.show();
			lang.nextStep(); // step
			phaseCode.unhighlight(4);

			phaseCode.highlight(5);
			setExplanation(translator.translateMessage("compareUntil"));
			lang.nextStep(); // step

			phaseCode.highlight(6);
			while (j >= 0 && pattern[j] == text[i + j]) { // code line 5
				increaseComparisonCount();

				j--; // code line 6
				textMarker.decrement(null, null);
				patternMarker.decrement(null, null);
				lang.nextStep(); // step

			} // code line 7
			if (j < 0) { // code line 8
				textMarker.hide();
				patternMarker.hide();
				phaseCode.unhighlight(5);
				phaseCode.unhighlight(6);

				occurrences.add(i); // code line 9
				phaseCode.highlight(9);
				setExplanation(translator.translateMessage("foundPattern"));
				lang.nextStep(); // step
				phaseCode.unhighlight(9);
				for (int x = 0; x < patternLength; x++) {
					animationText.unhighlightElem(i + x, null, null);
				}

				i += goodSuffixSkip[0]; // code line 10
				phaseCode.highlight(10);
				setExplanation(translator.translateMessage("moveGoodSuffix"));
				animationSkipMap2.highlightCell(1, 0, null, null);
				lang.nextStep(); // step
				phaseCode.unhighlight(10);
				animationSkipMap2.unhighlightCell(1, 0, null, null);
			} else { // code line 11

				setExplanation(translator.translateMessage("mismatch"));
				lang.nextStep(); // step
				textMarker.hide();
				patternMarker.hide();
				phaseCode.unhighlight(5);
				phaseCode.unhighlight(6);

				phaseCode.highlight(12);
				animationText.highlightCell(i + patternLength - 1, null, null);
				Integer bcSkip = badCharacterSkip.get(text[i + j]); // code line 12
				if (bcSkip == null) { // code line 13
					bcSkip = -1; // code line 14
					phaseCode.highlight(13);
					phaseCode.highlight(14);
					phaseCode.highlight(15);
					setExplanation(translator.translateMessage("notInBadCharacterTable",
							String.valueOf(text[i + j])));
					lang.nextStep(); // step
					phaseCode.unhighlight(13);
					phaseCode.unhighlight(14);
					phaseCode.unhighlight(15);
				} // code line 15

				int position = skipKeyList.indexOf(text[i + j]);
				if (position >= 0) {
					animationSkipMap.highlightCell(0, position, null, null);
					animationSkipMap.highlightCell(1, position, null, null);
				}
				if (bcSkip == -1) {
					if (j - bcSkip == 1) {
						setExplanation(translator.translateMessage("-1BadCharacterShift1"));
					} else {
						setExplanation(translator.translateMessage("-1BadCharacterShiftVariable",
								String.valueOf(j - bcSkip)));
					}
				} else {
					if (j - bcSkip == 1) {
						setExplanation(translator.translateMessage("badCharacterShift1",
								String.valueOf(bcSkip)));
					} else {
						setExplanation(translator.translateMessage("badCharacterShiftVariable",
								String.valueOf(bcSkip), String.valueOf(j - bcSkip)));
					}
				}
				lang.nextStep(); // step
				if (position >= 0) {
					animationSkipMap.unhighlightCell(0, position, null, null);
					animationSkipMap.unhighlightCell(1, position, null, null);
				}
				phaseCode.unhighlight(12);

				String skipMessage;
				if (goodSuffixSkip[j + 1] > j - bcSkip) {
					skipMessage = translator.translateMessage("moreThanBadCharacterShift");
				} else if (goodSuffixSkip[j + 1] == j - bcSkip) {
					skipMessage = translator.translateMessage("sameAsBadCharacterShift");
				} else {
					skipMessage = translator.translateMessage("lessThanBadCharacterShift");
				}

				phaseCode.highlight(16);
				if (goodSuffixSkip[j + 1] == 1) {
					setExplanation(translator.translateMessage("goodSuffixShift1") + " "
							+ skipMessage);
				} else {
					setExplanation(translator.translateMessage("goodSuffixShiftVariable",
							String.valueOf(goodSuffixSkip[j + 1]))
							+ " " + skipMessage);
				}
				animationSkipMap2.highlightCell(0, j + 1, null, null);
				animationSkipMap2.highlightCell(1, j + 1, null, null);
				lang.nextStep(); // step	
				animationText.unhighlightCell(i + patternLength - 1, null, null);
				animationSkipMap2.unhighlightCell(0, j + 1, null, null);
				animationSkipMap2.unhighlightCell(1, j + 1, null, null);

				animationText.unhighlightElem(i, i + patternLength, null, null);

				i += Math.max(goodSuffixSkip[j + 1], j - bcSkip); // code line 16
				int skipValue = Math.max(goodSuffixSkip[j + 1], j - bcSkip);
				if (i <= textLength - patternLength) {
					setExplanation(translator.translateMessage("continue",
							String.valueOf(skipValue)));
					animationText.unhighlightElem(i - skipValue, i - 1, null, null);
					animationText.highlightElem(i, i + patternLength - 1, null, null);
				} else {
					setExplanation(translator.translateMessage("endSearch"));
					animationText.unhighlightElem(i - skipValue, textLength - 1, null, null);
				}
				lang.nextStep(); // step
				phaseCode.unhighlight(16);
			} // code line 17
		} // code line 18
		phaseCode.hide();
		return occurrences; // code line 19
	} // code line 20
}
