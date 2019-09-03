package generators.searching;

import generators.searching.helpers.AbstractStringSearchGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LinearStringSearch extends AbstractStringSearchGenerator {

  public LinearStringSearch(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  protected List<String> getMainCode() {
    List<String> codeLines = new ArrayList<String>();
    codeLines.add("private char[] pattern, text;");
    codeLines.add("private int patternLength, textLength;");
    codeLines.add("");
    codeLines
        .add("public List<Integer> search(String inputText, String inputPattern) {");
    codeLines.add("  if (inputIsBad(inputText, inputPattern)) {");
    codeLines.add("    return new ArrayList<Integer>();");
    codeLines.add("  }");
    codeLines.add("  setText(inputText);");
    codeLines.add("  setPattern(inputPattern);");
    codeLines.add("  return linearSearch();");
    codeLines.add("}");
    return codeLines;
  }

  protected int getCodeHeigth() {
    return 15;
  }

  protected int getCodeWidth() {
    return 70;
  }

  @Override
  public String getAnimationAuthor() {
    return "Torsten Kohlhaas";
  }

  @Override
  public String getCodeExample() {
    return "private char[] pattern, text;\n"
        + "private int patternLength, textLength;\n"
        + "public List&#60Integer&#62 search(String inputText, String inputPattern) {\n"
        + "  if (inputIsBad(inputText, inputPattern)) {\n"
        + "    return new ArrayList&#60Integer&#62();\n"
        + "  }\n"
        + "  setText(inputText);\n"
        + "  setPattern(inputPattern);\n"
        + "  return linearSearch();\n"
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
        + "private List&#60Integer&#62 linearSearch() {\n"
        + "  List&#60Integer&#62 occurrences = new ArrayList&#60Integer&#62();\n"
        + "  int i = 0, j = 0;\n"
        + "  while (i &#60= textLength - patternLength) {\n"
        + "    while (j &#60 patternLength && text[i + j] == pattern[j]) {\n"
        + "      j++;\n" + "    }\n" + "    if (j == patternLength) {\n"
        + "      occurrences.add(i);\n" + "    }\n" + "    i++;\n"
        + "    j = 0;\n" + "  }\n" + "  return occurrences;\n" + "}\n";
  }

  @Override
  public String getDescription() {
    return translator.translateMessage("descriptionLine1")
        + translator.translateMessage("descriptionLine2");
  }

  @Override
  protected void compareToLinearSearch() {
    // Do nothing
  }

  protected List<Integer> search(String inputText, String inputPattern) { // code
                                                                          // line
                                                                          // 3
    // check input
    mainCode.highlight(4);
    if (inputIsBad(inputText, inputPattern)) { // code line 4
      mainCode.unhighlight(4);

      mainCode.highlight(5);
      setExplanation(translator.translateMessage("abortSearch"));
      lang.nextStep(); // step
      mainCode.unhighlight(5);
      return new ArrayList<Integer>(); // code line 5
    } // code line 6
    mainCode.unhighlight(4);

    mainCode.highlight(7);
    setText(inputText); // code line 7
    mainCode.unhighlight(7);

    mainCode.highlight(8);
    setPattern(inputPattern); // code line 8
    mainCode.unhighlight(8);

    mainCode.highlight(9);
    List<Integer> hits = linearSearch(); // code line 9
    mainCode.unhighlight(9);

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
  } // code line 10

  private List<Integer> linearSearch() {
    // show code
    phaseCode = lang.newSourceCode(phaseCodeCoordinates, "linearSearch", null,
        sourceCodeProperties);
    phaseCode.addCodeLine("private List<Integer> linearSearch() {",
        "linearSearch_0", 0, null);
    phaseCode.addCodeLine(
        "List<Integer> occurrences = new ArrayList<Integer>();",
        "linearSearch_1", 1, null);
    phaseCode.addCodeLine("int i = 0, j = 0;", "linearSearch_2", 1, null);
    phaseCode.addCodeLine("while (i <= textLength - patternLength) {",
        "linearSearch_3", 1, null);
    phaseCode.addCodeLine(
        "while (j < patternLength && text[i + j] == pattern[j]) {",
        "linearSearch_4", 2, null);
    phaseCode.addCodeLine("j++;", "linearSearch_5", 3, null);
    phaseCode.addCodeLine("}", "linearSearch_6", 2, null);
    phaseCode.addCodeLine("if (j == patternLength) {", "linearSearch_7", 2,
        null);
    phaseCode.addCodeLine("occurrences.add(i);", "linearSearch_8", 3, null);
    phaseCode.addCodeLine("}", "linearSearch_9", 2, null);
    phaseCode.addCodeLine("i++;", "linearSearch_10", 2, null);
    phaseCode.addCodeLine("j = 0;", "linearSearch_11", 2, null);
    phaseCode.addCodeLine("}", "linearSearch_12", 1, null);
    phaseCode.addCodeLine("return occurrences;", "linearSearch_13", 1, null);
    phaseCode.addCodeLine("}", "linearSearch_14", 0, null);

    List<Integer> occurrences = new ArrayList<Integer>(); // code line 1
    phaseCode.highlight(1);
    setExplanation(translator.translateMessage("initOccurrences"));
    lang.nextStep(); // step
    phaseCode.unhighlight(1);

    int i = 0, j = 0; // code line 2
    phaseCode.highlight(2);
    setExplanation(translator.translateMessage("explainIJ"));
    lang.nextStep(); // step
    phaseCode.unhighlight(2);

    phaseCode.highlight(3);
    setExplanation(translator.translateMessage("searchTillEnd"));
    lang.nextStep(); // step
    phaseCode.unhighlight(3);

    patternMarker = lang.newArrayMarker(animationPattern, 0, "patternMarker",
        null);
    textMarker = lang.newArrayMarker(animationText, 0, "textMarker", null);
    animationText.highlightElem(0, patternLength - 1, null, null);
    while (i <= textLength - patternLength) { // code line 3

      phaseCode.highlight(4);
      phaseCode.highlight(5);
      phaseCode.highlight(6);
      setExplanation(translator.translateMessage("compareUntil"));
      lang.nextStep(); // step

      increaseComparisonCount();
      while (j < patternLength && text[i + j] == pattern[j]) { // code line 4

        j++; // code line 5
        textMarker.increment(null, null);
        patternMarker.increment(null, null);
        lang.nextStep(); // step
        if (j < patternLength) {
          increaseComparisonCount();
        }

      } // code line 6
      phaseCode.unhighlight(4);
      phaseCode.unhighlight(5);
      phaseCode.unhighlight(6);

      phaseCode.highlight(7);
      if (j == patternLength) { // code line 7
        occurrences.add(i); // code line 8
        phaseCode.highlight(8);
        phaseCode.highlight(9);
        setExplanation(translator.translateMessage("foundPattern"));
        lang.nextStep(); // step
        phaseCode.unhighlight(8);
        phaseCode.unhighlight(9);
      } // code line 9
      else {
        setExplanation(translator.translateMessage("mismatch"));
        lang.nextStep(); // step
      }
      phaseCode.unhighlight(7);

      animationText.unhighlightElem(i, null, null);
      i++; // code line 10

      if (i <= textLength - patternLength) {
        phaseCode.highlight(10);
        animationText.highlightElem(i + patternLength - 1, null, null);
        j = 0; // code line 11
        phaseCode.highlight(11);
        setExplanation(translator.translateMessage("nextWindow"));
        textMarker.move(i, null, null);
        patternMarker.move(0, null, null);
        lang.nextStep(); // step
        phaseCode.unhighlight(10);
        phaseCode.unhighlight(11);
      } else {
        phaseCode.highlight(3);
        while (i < textLength) {
          animationText.unhighlightElem(i, null, null);
          i++;
        }
        setExplanation(translator.translateMessage("endSearch"));
        textMarker.hide();
        patternMarker.hide();
        lang.nextStep(); // step
        phaseCode.unhighlight(3);
      }
    } // code line 12
    return occurrences; // code line 13
  } // code line 14
}
