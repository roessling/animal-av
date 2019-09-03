package generators.searching.kmp;

import generators.searching.helpers.AbstractStringSearchGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.IntArray;
import algoanim.util.Offset;

public class KnuthMorrisPrattSearch extends AbstractStringSearchGenerator {

  private int[]    skipValues;

  private IntArray animationSkipMap;

  public KnuthMorrisPrattSearch(String resourceName, Locale locale) {
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
        + "private int[] skipValues;\n"
        + "\n"
        + "public List&#60Integer&#62 search(String inputText, String inputPattern) {\n"
        + "  if (inputIsBad(inputText, inputPattern)) {\n"
        + "    return new ArrayList&#60Integer&#62();\n"
        + "  }\n"
        + "  setText(inputText);\n"
        + "  setPattern(inputPattern);\n"
        + "  setSkipMap();\n"
        + "  return kmpSearch();\n"
        + "}\n"
        + "\n"
        + "private boolean inputIsBad(String inputText, String inputPattern) {\n"
        + "  return (inputText == null || inputText.isEmpty()\n"
        + "    || inputPattern == null || inputPattern.isEmpty() || inputPattern\n"
        + "    .length() &#62 inputText.length());\n"
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
        + "  skipValues = new int[patternLength + 1];\n"
        + "  int i = 0, j = -1;\n"
        + "  skipValues[i] = j;\n"
        + "  while (i &#60 patternLength) {\n"
        + "    while (j &#62= 0 && pattern[i] != pattern[j]) {\n"
        + "      j = skipValues[j];\n"
        + "    }\n"
        + "    i++;\n"
        + "    j++;\n"
        + "    skipValues[i] = j;\n"
        + "  }\n"
        + "}\n"
        + "\n"
        + "private List&#60Integer&#62 kmpSearch() {\n"
        + "  List&#60Integer&#62 occurrences = new ArrayList&#60Integer&#62();\n"
        + "  int i = 0, j = 0;\n" + "  while (i &#60 textLength) {\n"
        + "    while (j &#62= 0 && text[i] != pattern[j]) {\n"
        + "      j = skipValues[j];\n" + "    }\n" + "    i++;\n"
        + "    j++;\n" + "    if (j == patternLength) {\n"
        + "      occurrences.add(i - j);\n" + "      j = skipValues[j];\n"
        + "    }\n" + "  }\n" + "  return occurrences;\n" + "}";
  }

  @Override
  public String getDescription() {
    return translator.translateMessage("descriptionLine1")
        + translator.translateMessage("descriptionLine2")
        + translator.translateMessage("descriptionLine3")
        + translator.translateMessage("descriptionLine4");
  }

  @Override
  protected int getCodeHeigth() {
    return 16;
  }

  @Override
  protected int getCodeWidth() {
    return 70;
  }

  @Override
  protected List<String> getMainCode() {
    List<String> codeLines = new ArrayList<String>();
    codeLines.add("private char[] pattern, text;");
    codeLines.add("private int patternLength, textLength;");
    codeLines.add("private int[] skipValues;");
    codeLines.add("");
    codeLines
        .add("public List<Integer> search(String inputText, String inputPattern) {");
    codeLines.add("  if (inputIsBad(inputText, inputPattern)) {");
    codeLines.add("    return new ArrayList<Integer>();");
    codeLines.add("  }");
    codeLines.add("  setText(inputText);");
    codeLines.add("  setPattern(inputPattern);");
    codeLines.add("  setSkipMap();");
    codeLines.add("  return kmpSearch();");
    codeLines.add("}");
    return codeLines;
  }

  @Override
  protected List<Integer> search(String inputText, String inputPattern) { // code
                                                                          // line
                                                                          // 4
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
    List<Integer> hits = kmpSearch(); // code line 11
    mainCode.unhighlight(11);

    // show results
    if (hits.isEmpty()) {
      setExplanation(translator.translateMessage("patternNotFound"));
    } else {
      setExplanation(translator.translateMessage("hits",
          String.valueOf(hits.size())));
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
    phaseCode.addCodeLine("private void setSkipMap() {", "setSkipMap_0", 0,
        null);
    phaseCode.addCodeLine("skipValues = new int[patternLength + 1];",
        "setSkipMap_1", 1, null);
    phaseCode.addCodeLine("int i = 0, j = -1;", "setSkipMap_2", 1, null);
    phaseCode.addCodeLine("skipValues[i] = j;", "setSkipMap_3", 1, null);
    phaseCode.addCodeLine("while (i < patternLength) {", "setSkipMap_4", 1,
        null);
    phaseCode.addCodeLine("while (j >= 0 && pattern[i] != pattern[j]) {",
        "setSkipMap_5", 2, null);
    phaseCode.addCodeLine("j = skipValues[j];", "setSkipMap_6", 3, null);
    phaseCode.addCodeLine("}", "setSkipMap_7", 2, null);
    phaseCode.addCodeLine("i++;", "setSkipMap_8", 2, null);
    phaseCode.addCodeLine("j++;", "setSkipMap_9", 2, null);
    phaseCode.addCodeLine("skipValues[i] = j;", "setSkipMap_10", 2, null);
    phaseCode.addCodeLine("}", "setSkipMap_11", 1, null);
    phaseCode.addCodeLine("}", "setSkipMap_12", 0, null);

    skipValues = new int[patternLength + 1]; // code line 1
    phaseCode.highlight(1);
    setExplanation(translator.translateMessage("explainTable"));
    animationSkipMap = lang.newIntArray(new Offset(0, 20, "pattern", "SW"),
        new int[patternLength + 1], "skipMap", null, textArrayProperties);
    lang.newText(new Offset(-100, 0, "skipMap", "NW"),
        translator.translateMessage("skipMap"), "label_skipMap", null);
    lang.nextStep(); // step

    setExplanation(translator.translateMessage("explainBorder"));
    lang.nextStep(); // step
    phaseCode.unhighlight(1);

    int i = 0, j = -1; // code line 2
    phaseCode.highlight(2);
    setExplanation(translator.translateMessage("explainIJ"));
    lang.nextStep(); // step
    phaseCode.unhighlight(2);

    skipValues[i] = j; // code line 3
    phaseCode.highlight(3);
    animationSkipMap.highlightCell(i, null, null);
    animationSkipMap.put(i, j, null, null);
    setExplanation(translator.translateMessage("emptyStringBorder"));
    lang.nextStep(); // step
    phaseCode.unhighlight(3);
    animationSkipMap.unhighlightCell(i, null, null);

    phaseCode.highlight(4);
    setExplanation(translator.translateMessage("analyzePattern"));
    lang.nextStep(); // step
    phaseCode.unhighlight(4);
    while (i < patternLength) { // code line 4

      phaseCode.highlight(5);
      animationPattern.highlightCell(i, null, null);
      if (j >= 0) {
        increaseComparisonCount();
        if (pattern[i] == pattern[j]) {
          setExplanation(translator.translateMessage("extendBorder"));
          lang.nextStep(); // step
        }
      }
      while (j >= 0 && pattern[i] != pattern[j]) { // code line 5

        setExplanation(translator.translateMessage("noBorderOfThisLength",
            String.valueOf(i + 1), String.valueOf(j + 1)));
        lang.nextStep(); // step

        j = skipValues[j]; // code line 6
        phaseCode.highlight(6);
        if (j >= 0) {
          setExplanation(translator.translateMessage("testShorterBorder"));
          increaseComparisonCount();
        } else {
          setExplanation(translator.translateMessage("noBorder"));
        }
        lang.nextStep(); // step
        phaseCode.unhighlight(6);

      } // code line 7
      phaseCode.unhighlight(5);

      i++; // code line 8
      j++; // code line 9
      skipValues[i] = j; // code line 10
      phaseCode.highlight(8);
      phaseCode.highlight(9);
      phaseCode.highlight(10);
      animationSkipMap.highlightCell(i, null, null);
      animationSkipMap.put(i, j, null, null);
      setExplanation(translator.translateMessage("longestBorder",
          String.valueOf(i), String.valueOf(j)));
      lang.nextStep(); // step
      phaseCode.unhighlight(8);
      phaseCode.unhighlight(9);
      phaseCode.unhighlight(10);
      animationSkipMap.unhighlightCell(i, null, null);
    } // code line 11

    for (int k = 0; k < patternLength; k++) {
      animationPattern.unhighlightCell(k, null, null);
    }
    phaseCode.hide();
  } // code line 12

  private List<Integer> kmpSearch() { // code line 0
    // show code
    phaseCode = lang.newSourceCode(phaseCodeCoordinates, "kmpSearch", null,
        sourceCodeProperties);
    phaseCode.addCodeLine("private List<Integer> kmpSearch() {", "kmpSearch_0",
        0, null);
    phaseCode.addCodeLine(
        "List<Integer> occurrences = new ArrayList<Integer>();", "kmpSearch_1",
        1, null);
    phaseCode.addCodeLine("int i = 0, j = 0;", "kmpSearch_2", 1, null);
    phaseCode.addCodeLine("while (i < textLength) {", "kmpSearch_3", 1, null);
    phaseCode.addCodeLine("while (j >= 0 && text[i] != pattern[j]) {",
        "kmpSearch_4", 2, null);
    phaseCode.addCodeLine("j = skipValues[j];", "kmpSearch_5", 3, null);
    phaseCode.addCodeLine("}", "kmpSearch_6", 2, null);
    phaseCode.addCodeLine("i++;", "kmpSearch_7", 2, null);
    phaseCode.addCodeLine("j++;", "kmpSearch_8", 2, null);
    phaseCode.addCodeLine("if (j == patternLength) {", "kmpSearch_9", 2, null);
    phaseCode.addCodeLine("occurrences.add(i - j);", "kmpSearch_10", 3, null);
    phaseCode.addCodeLine("j = skipValues[j];", "kmpSearch_11", 3, null);
    phaseCode.addCodeLine("}", "kmpSearch_12", 2, null);
    phaseCode.addCodeLine("}", "kmpSearch_13", 1, null);
    phaseCode.addCodeLine("return occurrences;", "kmpSearch_14", 1, null);
    phaseCode.addCodeLine("}", "kmpSearch_15", 0, null);

    List<Integer> occurrences = new ArrayList<Integer>(); // code line 1
    phaseCode.highlight(1);
    setExplanation(translator.translateMessage("initOccurrences"));
    lang.nextStep(); // step
    phaseCode.unhighlight(1);

    int i = 0, j = 0; // code line 2
    phaseCode.highlight(2);
    setExplanation(translator.translateMessage("explainIJ2"));
    lang.nextStep(); // step
    phaseCode.unhighlight(2);

    phaseCode.highlight(3);
    setExplanation(translator.translateMessage("analyzeText"));
    lang.nextStep(); // step
    phaseCode.unhighlight(3);

    patternMarker = lang.newArrayMarker(animationPattern, 0, "patternMarker",
        null);
    textMarker = lang.newArrayMarker(animationText, 0, "textMarker", null);
    while (i < textLength) { // code line 3

      phaseCode.highlight(4);
      increaseComparisonCount();
      if (text[i] != pattern[j]) {
        setExplanation(translator.translateMessage("mismatch"));
        lang.nextStep(); // step
      } else {
        if (j == 0) {
          setExplanation(translator.translateMessage("match1"));
        } else if (j == patternLength - 1) {
          phaseCode.unhighlight(4);
          phaseCode.highlight(9);
          setExplanation(translator.translateMessage("matchAll"));
        } else {
          setExplanation(translator.translateMessage("matchVariable",
              String.valueOf(j + 1)));
        }
        lang.nextStep(); // step
        if (j == patternLength - 1) {
          phaseCode.unhighlight(9);
        }
      }

      while (j >= 0 && text[i] != pattern[j]) { // code line 4
        phaseCode.highlight(5);
        StringBuilder explanationBuilder = new StringBuilder();
        for (int x = j; x >= 0; x--) {
          explanationBuilder.append(text[i - x]);
        }
        explanationBuilder.append(translator.translateMessage("isNoBorder"));
        setExplanation(explanationBuilder.toString());
        lang.nextStep(); // step

        int position = j;
        animationSkipMap.highlightCell(position, null, null);
        j = skipValues[j]; // code line 5
        if (j == -1) {
          setExplanation(translator.translateMessage("noBorderFound"));
        } else {
          setExplanation(translator.translateMessage("nextPossibleBorder",
              String.valueOf(j)));
          patternMarker.move(j, null, null);
        }
        lang.nextStep(); // step
        animationSkipMap.unhighlightCell(position, null, null);
        if (j >= 0) {
          if (text[i] == pattern[j]) {
            explanationBuilder = new StringBuilder();
            for (int x = j; x >= 0; x--) {
              explanationBuilder.append(text[i - x]);
            }
            explanationBuilder.append(translator.translateMessage("isABorder"));
            setExplanation(explanationBuilder.toString());
            lang.nextStep(); // step
          }
          increaseComparisonCount();
        }
        phaseCode.unhighlight(5);
      } // code line 6

      i++; // code line 7
      j++; // code line 8

      phaseCode.unhighlight(4);

      if (j == patternLength) { // code line 9
        occurrences.add(i - j); // code line 10
        animationText.highlightCell(i - j, i - 1, null, null);
        phaseCode.highlight(10);
        setExplanation(translator.translateMessage("foundPattern"));
        lang.nextStep(); // step
        for (int x = i - j; x < i; x++) {
          animationText.unhighlightCell(x, null, null);
        }
        phaseCode.unhighlight(10);

        j = skipValues[j]; // code line 11
        phaseCode.highlight(11);
        animationSkipMap.highlightCell(patternLength, null, null);
        setExplanation(translator.translateMessage("avoidRedundancy",
            String.valueOf(j)));
        patternMarker.move(j - 1, null, null);
        lang.nextStep(); // step
        phaseCode.unhighlight(11);
        animationSkipMap.unhighlightCell(patternLength, null, null);
      } // code line 12

      if (i == textLength) {
        setExplanation(translator.translateMessage("endSearch"));
        textMarker.hide();
        patternMarker.hide();
        lang.nextStep(); // step
      } else {
        phaseCode.highlight(7);
        phaseCode.highlight(8);
        setExplanation(translator.translateMessage("continue"));
        textMarker.move(i, null, null);
        patternMarker.move(j, null, null);
        lang.nextStep(); // step
        phaseCode.unhighlight(7);
        phaseCode.unhighlight(8);
      }

    } // code line 13
    phaseCode.hide();
    return occurrences; // code line 14
  } // code line 15
}
