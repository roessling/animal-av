package generators.searching.horspool;

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

public class HorspoolRaita extends AbstractStringSearchGenerator {

	protected MatrixProperties matrixProperties;
	private List<Character> skipKeyList;
	private List<Integer> skipValueList;
	private StringMatrix animationSkipMap;

	private Map<Character, Integer> skipValues;
	
  public HorspoolRaita(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

	@Override
	public String getAnimationAuthor() {
		return "Torsten Kohlhaas";
	}

	@Override
	public String getCodeExample() {
		return "private char[] pattern, text;\n"
				+ "private int patternLength, textLength;\n"
				+ "private Map&#60Character, Integer&#62 skipValues;\n"
				+ "\n"
				+ "public List&#60Integer&#62 search(String inputText, String inputPattern) {\n"
				+ "  if (inputIsBad(inputText, inputPattern)) {\n"
				+ "    return new ArrayList&#60Integer&#62();\n"
				+ "  }\n"
				+ "  setText(inputText);\n"
				+ "  setPattern(inputPattern);\n"
				+ "  setSkipMap();\n"
				+ "  return raitaSearch();\n"
				+ "}\n"
				+ "\n"
				+ "private boolean inputIsBad(String inputText, String inputPattern) {\n"
				+ "  return (inputText == null || inputText.isEmpty()\n"
				+ "          || inputPattern == null || inputPattern.isEmpty() || inputPattern\n"
				+ "          .length() &#62 inputText.length());\n"
				+ "}\n"
				+ "\n"
				+ "private void setText(String inputText) {\n"
				+ "  textLength = inputText.length();\n"
				+ "  text = inputText.toCharArray();\n"
				+ "}\n"
				+ "\n"
				+ "private void setPattern(String inputPattern) {\n"
				+ "  patternLength = inputPattern.length();\n"
				+ "  pattern = inputPattern.toCharArray();\n"
				+ "}\n"
				+ "\n"
				+ "private void setSkipMap() {\n"
				+ "  skipValues = new HashMap&#60Character, Integer&#62(pattern.length);\n"
				+ "  for (int i = 0; i &#60 pattern.length - 1; i++) {\n"
				+ "    skipValues.put(pattern[i], pattern.length - (i + 1));\n"
				+ "  }\n"
				+ "}\n"
				+ "\n"
				+ "private List&#60Integer&#62 raitaSearch() {\n"
				+ " List&#60Integer&#62 occurrences = new ArrayList&#60Integer&#62();\n"
				+ " int i = 0, j;\n"
				+ " while (i &#60= textLength - patternLength) {\n"
				+ "  if (pattern[patternLength - 1] == text[i + patternLength - 1] && pattern[0] == text[i]\n"
				+ "    && pattern[patternLength / 2] == text[i + (patternLength / 2)]) {\n"
				+ "    if (patternLength &#60= 3) {\n" + "      occurrences.add(i);\n"
				+ "    } else {\n" + "      j = 1;\n"
				+ "      while (pattern[j] == text[i + j]) {\n" + "      j++;\n"
				+ "      if (j == patternLength - 1) {\n" + "        occurrences.add(i);\n"
				+ "        break;\n" + "        }\n" + "      }\n" + "    }\n" + "  }\n"
				+ "  Integer skipValue = skipValues.get(text[i + patternLength - 1]);\n"
				+ "  if (skipValue == null) {\n" + "    skipValue = patternLength;\n" + "  }\n"
				+ "  i += skipValue;\n" + "  return occurrences;\n" + "}";
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
	protected List<String> getMainCode() {
		List<String> codeLines = new ArrayList<String>();
		codeLines.add("private char[] pattern, text;");
		codeLines.add("private int patternLength, textLength;");
		codeLines.add("private Map<Character, Integer> skipValues;");
		codeLines.add("");
		codeLines.add("public List<Integer> search(String inputText, String inputPattern) {");
		codeLines.add("  if (inputIsBad(inputText, inputPattern)) {");
		codeLines.add("    return new ArrayList<Integer>();");
		codeLines.add("  }");
		codeLines.add("  setText(inputText);");
		codeLines.add("  setPattern(inputPattern);");
		codeLines.add("  setSkipMap();");
		codeLines.add("  return raitaSearch();");
		codeLines.add("}");
		return codeLines;
	}

	@Override
	protected int getCodeHeigth() {
		return 27;
	}

	@Override
	protected int getCodeWidth() {
		return 90;
	}

	@Override
	protected List<Integer> search(String inputText, String inputPattern) { // code line 4
		// check input
		mainCode.highlight(5);
		if (inputIsBad(inputText, inputPattern)) { // code line 5
			mainCode.unhighlight(5);

			mainCode.highlight(6);
			setExplanation(translator.translateMessage("abortSearch"));
			lang.nextStep(); // step
			mainCode.unhighlight(6);
			return new ArrayList<Integer>(); // code line 6
		} // code line 7
		mainCode.unhighlight(5);

		mainCode.highlight(8);
		setText(inputText); // code line 8
		mainCode.unhighlight(8);

		mainCode.highlight(9);
		setPattern(inputPattern); // code line 9
		mainCode.unhighlight(9);

		mainCode.highlight(10);
		setSkipMap(); // code line 10
		mainCode.unhighlight(10);

		mainCode.highlight(11);
		List<Integer> hits = raitaSearch(); // code line 11
		mainCode.unhighlight(11);

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
	} // code line 12

	private void setSkipMap() { // code line 0
		// show code
		phaseCode = lang.newSourceCode(phaseCodeCoordinates, "setSkipMap", null,
				sourceCodeProperties);
		phaseCode.addCodeLine("private void setSkipMap() {", "setSkipMap_0", 0, null);
		phaseCode.addCodeLine("skipValues = new HashMap<Character, Integer>(patternLength);",
				"setSkipMap_1", 1, null);
		phaseCode.addCodeLine("for (int i = 0; i < pattern.length - 1; i++) {", "setSkipMap_2", 1,
				null);
		phaseCode.addCodeLine("skipValues.put(pattern[i], pattern.length - (i + 1));",
				"setSkipMap_3", 2, null);
		phaseCode.addCodeLine("}", "setSkipMap_4", 1, null);
		phaseCode.addCodeLine("}", "setSkipMap_5", 0, null);

		skipValues = new HashMap<Character, Integer>(patternLength); // code line 1
		phaseCode.highlight(1);
		setExplanation(translator.translateMessage("createTable"));
		String[][] skipMatrix = new String[2][patternLength];
		for (int i = 0; i < patternLength; i++) {
			skipMatrix[0][i] = "";
			skipMatrix[1][i] = "";
		}

		animationSkipMap = lang.newStringMatrix(new Offset(0, 20, "pattern", "SW"), skipMatrix,
				"skipMap", null, matrixProperties);
		lang.newText(new Offset(-100, 0, "skipMap", "NW"), translator.translateMessage("skipMap"),
				"label_skipMap", null);
		lang.nextStep(); // step
		phaseCode.unhighlight(1);

		phaseCode.highlight(2);
		setExplanation(translator.translateMessage("analyzePattern"));
		lang.nextStep(); // step
		phaseCode.unhighlight(2);

		phaseCode.highlight(3);
		setExplanation(translator.translateMessage("explainTable"));
		skipKeyList = new ArrayList<Character>(patternLength);
		skipValueList = new ArrayList<Integer>(patternLength);
		ArrayMarker patternMarker = lang
				.newArrayMarker(animationPattern, -1, "patternMarker", null);
		for (int i = 0; i < patternLength - 1; i++) { // code line 2
			patternMarker.increment(null, null);
			char key = pattern[i];
			int value = patternLength - (i + 1);
			skipValues.put(key, value);
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
		phaseCode.unhighlight(3);
		phaseCode.hide();
	} // code line 5

	private List<Integer> raitaSearch() { // code line 0
		// show code
		phaseCode = lang.newSourceCode(phaseCodeCoordinates, "raitaSearch", null,
				sourceCodeProperties);
		phaseCode.addCodeLine("private List<Integer> raitaSearch() {", "raitaSearch_0", 0, null);
		phaseCode.addCodeLine("List<Integer> occurrences = new ArrayList<Integer>();",
				"raitaSearch_1", 1, null);
		phaseCode.addCodeLine("int i = 0, j;", "raitaSearch_2", 1, null);
		phaseCode
				.addCodeLine("while (i <= textLength - patternLength) {", "raitaSearch_3", 1, null);
		phaseCode
				.addCodeLine(
						"if (pattern[patternLength - 1] == text[i + patternLength - 1] && pattern[0] == text[i]",
						"raitaSearch_4", 2, null);
		phaseCode.addCodeLine("&& pattern[patternLength / 2] == text[i + (patternLength / 2)]) {",
				"raitaSearch_5", 4, null);
		phaseCode.addCodeLine("if (patternLength <= 3) {", "raitaSearch_6", 3, null);
		phaseCode.addCodeLine("occurrences.add(i);", "raitaSearch_7", 4, null);
		phaseCode.addCodeLine("} else {", "raitaSearch_8", 3, null);
		phaseCode.addCodeLine("j = 1;", "raitaSearch_9", 4, null);
		phaseCode.addCodeLine("while (pattern[j] == text[i + j]) {", "raitaSearch_10", 5, null);
		phaseCode.addCodeLine("j++;", "raitaSearch_11", 6, null);
		phaseCode.addCodeLine("if (j == patternLength - 1) {", "raitaSearch_12", 6, null);
		phaseCode.addCodeLine("occurrences.add(i);", "raitaSearch_13", 7, null);
		phaseCode.addCodeLine("break;", "raitaSearch_14", 7, null);
		phaseCode.addCodeLine("}", "raitaSearch_15", 6, null);
		phaseCode.addCodeLine("}", "raitaSearch_16", 5, null);
		phaseCode.addCodeLine("}", "raitaSearch_17", 4, null);
		phaseCode.addCodeLine("}", "raitaSearch_18", 3, null);
		phaseCode.addCodeLine("Integer skipValue = skipValues.get(text[i + patternLength - 1]);",
				"raitaSearch_19", 3, null);
		phaseCode.addCodeLine("if (skipValue == null) {", "raitaSearch_20", 3, null);
		phaseCode.addCodeLine("skipValue = patternLength;", "raitaSearch_21", 4, null);
		phaseCode.addCodeLine("}", "raitaSearch_22", 3, null);
		phaseCode.addCodeLine("i += skipValue;", "raitaSearch_23", 3, null);
		phaseCode.addCodeLine("}", "raitaSearch_24", 2, null);
		phaseCode.addCodeLine("return occurrences;", "raitaSearch_25", 2, null);
		phaseCode.addCodeLine("}", "raitaSearch_26", 1, null);

		List<Integer> occurrences = new ArrayList<Integer>(); // code line 1
		phaseCode.highlight(1);
		setExplanation(translator.translateMessage("initOccurrences"));
		lang.nextStep(); // step
		phaseCode.unhighlight(1);

		int i = 0, j; // code line 2
		phaseCode.highlight(2);
		setExplanation(translator.translateMessage("explainIJ"));
		lang.nextStep(); // step
		phaseCode.unhighlight(2);

		phaseCode.highlight(3);
		setExplanation(translator.translateMessage("searchTillEnd"));
		lang.nextStep(); // step
		phaseCode.unhighlight(3);

		patternMarker = lang.newArrayMarker(animationPattern, 1, "patternMarker", null);
		patternMarker.hide();
		textMarker = lang.newArrayMarker(animationText, 1, "textMarker", null);
		textMarker.hide();

		while (i <= textLength - patternLength) { // code line 3
			animationText.highlightElem(i, i + patternLength - 1, null, null);
			phaseCode.highlight(4);
			phaseCode.highlight(5);
			textMarker.move(i + patternLength - 1, null, null);
			patternMarker.move(patternLength - 1, null, null);
			textMarker.show();
			patternMarker.show();
			setExplanation(translator.translateMessage("compareLast"));
			increaseComparisonCount();
			lang.nextStep(); // step

			if (pattern[patternLength - 1] != text[i + patternLength - 1]) {
				mismatch();
			} else {
				textMarker.move(i, null, null);
				patternMarker.move(0, null, null);
				setExplanation(translator.translateMessage("compareFirst"));
				increaseComparisonCount();
				lang.nextStep(); // step

				if (pattern[0] != text[i]) {
					mismatch();
				} else {
					textMarker.move(i + (patternLength / 2), null, null);
					patternMarker.move(patternLength / 2, null, null);
					setExplanation(translator.translateMessage("compareMiddle"));
					increaseComparisonCount();
					lang.nextStep(); // step

					if (pattern[patternLength / 2] != text[i + (patternLength / 2)]) {
						mismatch();
					} else {
						textMarker.hide();
						patternMarker.hide();
					}
				}
			}
			phaseCode.unhighlight(4);
			phaseCode.unhighlight(5);

			if (pattern[patternLength - 1] == text[i + patternLength - 1] && pattern[0] == text[i] // code line 4
					&& pattern[patternLength / 2] == text[i + (patternLength / 2)]) { // code line 5
				if (patternLength <= 3) { // code line 6
					occurrences.add(i); // code line 7
					phaseCode.highlight(6);
					phaseCode.highlight(7);
					setExplanation(translator.translateMessage("alreadyDone"));
					lang.nextStep(); // step
					phaseCode.unhighlight(6);
					phaseCode.unhighlight(7);

				} else { // code line 8
					phaseCode.highlight(9);
					phaseCode.highlight(10);
					phaseCode.highlight(11);
					setExplanation(translator.translateMessage("comparisonStart"));
					increaseComparisonCount();
					j = 1; // code line 9
					textMarker.move(i + j, null, null);
					textMarker.show();
					patternMarker.move(j, null, null);
					patternMarker.show();
					lang.nextStep(); // step

					while (pattern[j] == text[i + j]) { // code line 10
						j++; // code line 11

						if (j == patternLength - 1) { // code line 12
							phaseCode.unhighlight(9);
							phaseCode.unhighlight(10);
							phaseCode.unhighlight(11);
							phaseCode.highlight(12);
							phaseCode.highlight(13);
							phaseCode.highlight(14);
							setExplanation(translator.translateMessage("foundPattern"));
							lang.nextStep(); // step
							occurrences.add(i); // code line 13
							break; // code line 14
						} // code line 15
						else {
							textMarker.increment(null, null);
							patternMarker.increment(null, null);
							lang.nextStep(); // step
							increaseComparisonCount();
						}
					} // code line 16
					phaseCode.unhighlight(9);
					phaseCode.unhighlight(10);
					phaseCode.unhighlight(11);
					phaseCode.unhighlight(12);
					phaseCode.unhighlight(13);
					phaseCode.unhighlight(14);
					textMarker.hide();
					patternMarker.hide();
				} // code line 17
			} // code line 18

			Integer skipValue = skipValues.get(text[i + patternLength - 1]); // code line 19
			phaseCode.highlight(19);
			animationText.highlightCell(i + patternLength - 1, null, null);

			if (skipValue == null) { // code line 20
				phaseCode.highlight(19);
				setExplanation(translator.translateMessage("notInSkipMap",
						String.valueOf(text[i + patternLength - 1])));
				lang.nextStep(); // step
				phaseCode.unhighlight(20);
				phaseCode.unhighlight(19);

				skipValue = patternLength; // code line 21
				phaseCode.highlight(21);
				setExplanation(translator.translateMessage("moveWindowMax"));
				lang.nextStep(); // step
				phaseCode.unhighlight(21);
			} // code line 22

			else {
				int position = skipKeyList.indexOf(text[i + patternLength - 1]);
				animationSkipMap.highlightCell(0, position, null, null);
				animationSkipMap.highlightCell(1, position, null, null);
				if (skipValues.get(text[i + patternLength - 1]) == 1) {
					setExplanation(translator.translateMessage("moveWindow1"));
				} else {
					setExplanation(translator.translateMessage("moveWindowVariable",
							String.valueOf(skipValues.get(text[i + patternLength - 1]))));
				}
				lang.nextStep(); // step
				phaseCode.unhighlight(19);
				animationSkipMap.unhighlightCell(0, position, null, null);
				animationSkipMap.unhighlightCell(1, position, null, null);
			}
			animationText.unhighlightCell(i + patternLength - 1, null, null);

			i += skipValue; // code line 23
			phaseCode.highlight(23);
			if (i <= textLength - patternLength) {
				setExplanation(translator.translateMessage("continue", String.valueOf(skipValue)));
				animationText.unhighlightElem(i - skipValue, i - 1, null, null);
				animationText.highlightElem(i, i + patternLength - 1, null, null);
			} else {
				setExplanation(translator.translateMessage("endSearch"));
				animationText.unhighlightElem(i - skipValue, textLength - 1, null, null);
			}
			lang.nextStep(); // step
			phaseCode.unhighlight(23);

		} // code line 24
		phaseCode.hide();
		return occurrences; // code line 25
	} // code line 26

	private void mismatch() {
		setExplanation(translator.translateMessage("mismatch"));
		lang.nextStep(); // step
		textMarker.hide();
		patternMarker.hide();
	}
}
