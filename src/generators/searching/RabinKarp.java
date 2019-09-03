package generators.searching;

import generators.searching.helpers.AbstractStringSearchGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.Text;
import algoanim.util.Offset;

public class RabinKarp extends AbstractStringSearchGenerator {
  private long patternHash;

  public RabinKarp(String resourceName, Locale locale) {
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
        + "private double patternHash\n"
        + "\n"
        + "public List&#60Integer&#62 search(String inputText, String inputPattern) {\n"
        + "  if (inputIsBad(inputText, inputPattern)) {\n"
        + "    return new ArrayList&#60Integer&#62();\n"
        + "  }\n"
        + "  setText(inputText);\n"
        + "  setPattern(inputPattern);\n"
        + "  setPatternHash();\n"
        + "  return rabinKarpSearch();\n"
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
        + "private void setPatternHash() {\n"
        + "  patternHash = 0;\n"
        + "  for (int i = 0; i &#60 patternLength; i++) {\n"
        + "    patternHash = (patternHash &#60&#60 1) + pattern[i];\n"
        + "  }\n"
        + "}\n"
        + "\n"
        + "private List&#60Integer&#62 rabinKarpSearch() {\n"
        + "  List&#60Integer&#62 occurrences = new ArrayList&#60Integer&#62();\n"
        + "  long textHash = 0;\n"
        + "  int i = 0;\n"
        + "  int factor = 1 &#60&#60 (patternLength - 1);\n"
        + "  for (int h = 0; h &#60 patternLength; h++) {\n"
        + "    textHash = (textHash &#60&#60 1) + text[h];\n"
        + "  }\n"
        + "  while (i &#60 textLength - patternLength) {\n"
        + "    if (patternHash == textHash) {\n"
        + "      int j = 0;\n"
        + "      while (j &#60 patternLength && text[i + j] == pattern[j]) {\n"
        + "        j++;\n"
        + "      }\n"
        + "      if (j == patternLength) {\n"
        + "        occurrences.add(i);\n"
        + "      }\n"
        + "    }\n"
        + "    textHash = ((textHash - text[i] * factor) &#60&#60 1) + text[i + patternLength];\n"
        + "    i++;\n" + "  }\n" + "  if (patternHash == textHash) {\n"
        + "    int j = 0;\n"
        + "    while (j &#60 patternLength && text[i + j] == pattern[j]) {\n"
        + "      j++;\n" + "    }\n" + "    if (j == patternLength) {\n"
        + "      occurrences.add(i);\n" + "    }\n" + "  }\n"
        + "  return occurrences;\n" + "}\n";
  }

  @Override
  public String getDescription() {
    return translator.translateMessage("descriptionLine1")
        + translator.translateMessage("descriptionLine2")
        + translator.translateMessage("descriptionLine3")
        + translator.translateMessage("descriptionLine4")
        + translator.translateMessage("descriptionLine5")
        + translator.translateMessage("descriptionLine6")
        + translator.translateMessage("descriptionLine7");
  }

  @Override
  protected int getCodeHeigth() {
    return 32;
  }

  @Override
  protected int getCodeWidth() {
    return 80;
  }

  @Override
  protected List<String> getMainCode() {
    List<String> codeLines = new ArrayList<String>();
    codeLines.add("private char[] pattern, text;");
    codeLines.add("private int patternLength, textLength;");
    codeLines.add("private Map<Character, Integer> skipValues;");
    codeLines.add("");
    codeLines
        .add("public List<Integer> search(String inputText, String inputPattern) {");
    codeLines.add("  if (inputIsBad(inputText, inputPattern)) {");
    codeLines.add("    return new ArrayList<Integer>();");
    codeLines.add("  }");
    codeLines.add("  setText(inputText);");
    codeLines.add("  setPattern(inputPattern);");
    codeLines.add("  setPatternHash();");
    codeLines.add("  return rabinKarpSearch();");
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
    setPatternHash(); // code line 10
    mainCode.unhighlight(10);

    mainCode.highlight(11);
    List<Integer> hits = rabinKarpSearch(); // code line 11
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

  private void setPatternHash() { // code line 0
    // show code
    phaseCode = lang.newSourceCode(phaseCodeCoordinates, "setPatternHash",
        null, sourceCodeProperties);
    phaseCode.addCodeLine("private void setPatternHash() {",
        "setPatternHash_0", 0, null);
    phaseCode.addCodeLine("patternHash = 0;", "setPatternHash_1", 1, null);
    phaseCode.addCodeLine("for (int i = 0; i < patternLength; i++) {",
        "setPatternHash_2", 1, null);
    phaseCode.addCodeLine("patternHash = (patternHash << 1) + pattern[i];",
        "setPatternHash_3", 2, null);
    phaseCode.addCodeLine("}", "setPatternHash_4", 1, null);
    phaseCode.addCodeLine("}", "setPatternHash_5", 0, null);

    patternHash = 0; // code line 1
    phaseCode.highlight(1);
    phaseCode.highlight(2);
    phaseCode.highlight(3);
    phaseCode.highlight(4);
    setExplanation(translator.translateMessage("explainPatternHash"));
    lang.nextStep(); // step
    phaseCode.unhighlight(1);
    phaseCode.unhighlight(2);
    phaseCode.unhighlight(4);

    Text animationPatternHash = lang.newText(new Offset(0, 20, "label_Pattern",
        "SW"), translator.translateMessage("patternHash",
        String.valueOf(patternHash)), "patternHash", null);

    for (int i = 0; i < patternLength; i++) { // code line 2
      animationPattern.highlightCell(i, null, null);
      patternHash = (patternHash << 1) + pattern[i]; // code line 3
      setExplanation(translator.translateMessage("characterCode",
          String.valueOf(pattern[i]), String.valueOf((int) pattern[i])));
      lang.nextStep(); // step
      setExplanation(translator.translateMessage("buildHash",
          String.valueOf(pattern[i])));
      animationPatternHash.setText(
          translator.translateMessage("patternHash",
              String.valueOf(patternHash)), null, null);
      lang.nextStep(); // step
      animationPattern.unhighlightCell(i, null, null);
    } // code line 4
    phaseCode.unhighlight(3);

    setExplanation(translator.translateMessage("finalPatternHash",
        String.valueOf(patternHash)));
    lang.nextStep(); // step

    phaseCode.hide();
  } // code line 5

  private List<Integer> rabinKarpSearch() { // code line 0
    // show code
    phaseCode = lang.newSourceCode(phaseCodeCoordinates, "rabinKarpSearch",
        null, sourceCodeProperties);

    phaseCode.addCodeLine("private List<Integer> rabinKarpSearch() {",
        "rabinKarpSearch_0", 0, null);
    phaseCode.addCodeLine(
        "List<Integer> occurrences = new ArrayList<Integer>();",
        "rabinKarpSearch_1", 1, null);
    phaseCode.addCodeLine("long textHash = 0;", "rabinKarpSearch_2", 1, null);
    phaseCode.addCodeLine("int i = 0;", "rabinKarpSearch_3", 1, null);
    phaseCode.addCodeLine("int factor = 1 << (patternLength - 1)",
        "rabinKarpSearch_4", 1, null);
    phaseCode.addCodeLine("for (int h = 0; h < patternLength; h++) {",
        "rabinKarpSearch_5", 1, null);
    phaseCode.addCodeLine("textHash = (textHash << 1) + text[h];",
        "rabinKarpSearch_6", 2, null);
    phaseCode.addCodeLine("}", "rabinKarpSearch_7", 1, null);
    phaseCode.addCodeLine("while (i < textLength - patternLength) {",
        "rabinKarpSearch_8", 1, null);
    phaseCode.addCodeLine("if (patternHash == textHash) {",
        "rabinKarpSearch_9", 2, null);
    phaseCode.addCodeLine("int j = 0;", "rabinKarpSearch_10", 3, null);
    phaseCode.addCodeLine(
        "while (j < patternLength && text[i + j] == pattern[j]) {",
        "rabinKarpSearch_11", 3, null);
    phaseCode.addCodeLine("j++;", "rabinKarpSearch_12", 4, null);
    phaseCode.addCodeLine("}", "rabinKarpSearch_13", 3, null);
    phaseCode.addCodeLine("if (j == patternLength) {", "rabinKarpSearch_14", 3,
        null);
    phaseCode.addCodeLine("occurrences.add(i);", "rabinKarpSearch_15", 4, null);
    phaseCode.addCodeLine("}", "rabinKarpSearch_16", 3, null);
    phaseCode.addCodeLine("}", "rabinKarpSearch_17", 2, null);
    phaseCode
        .addCodeLine(
            "textHash = ((textHash - text[i] * factor) << 1) + text[i + patternLength];",
            "rabinKarpSearch_18", 2, null);
    phaseCode.addCodeLine("i++;", "rabinKarpSearch_19", 2, null);
    phaseCode.addCodeLine("}", "rabinKarpSearch_20", 1, null);
    phaseCode.addCodeLine("if (patternHash == textHash) {",
        "rabinKarpSearch_21", 1, null);
    phaseCode.addCodeLine("int j = 0;", "rabinKarpSearch_22", 2, null);
    phaseCode.addCodeLine(
        "while (j < patternLength && text[i + j] == pattern[j]) {",
        "rabinKarpSearch_23", 2, null);
    phaseCode.addCodeLine("j++;", "rabinKarpSearch_24", 3, null);
    phaseCode.addCodeLine("}", "rabinKarpSearch_25", 2, null);
    phaseCode.addCodeLine("if (j == patternLength) {", "rabinKarpSearch_26", 2,
        null);
    phaseCode.addCodeLine("occurrences.add(i);", "rabinKarpSearch_27", 3, null);
    phaseCode.addCodeLine("}", "rabinKarpSearch_28", 2, null);
    phaseCode.addCodeLine("}", "rabinKarpSearch_29", 1, null);
    phaseCode.addCodeLine("return occurrences;", "rabinKarpSearch_30", 1, null);
    phaseCode.addCodeLine("}", "rabinKarpSearch_31", 0, null);

    List<Integer> occurrences = new ArrayList<Integer>(); // code line 1
    phaseCode.highlight(1);
    setExplanation(translator.translateMessage("initOccurrences"));
    lang.nextStep(); // step
    phaseCode.unhighlight(1);

    long textHash = 0; // code line 2
    phaseCode.highlight(2);
    setExplanation(translator.translateMessage("windowHash"));
    lang.nextStep(); // step
    phaseCode.unhighlight(2);

    int i = 0; // code line 3
    phaseCode.highlight(3);
    setExplanation(translator.translateMessage("explainI"));
    lang.nextStep(); // step
    phaseCode.unhighlight(3);

    int factor = 1 << (patternLength - 1); // code line 4
    phaseCode.highlight(4);
    setExplanation(translator.translateMessage("hashDepends"));
    lang.nextStep(); // step
    phaseCode.unhighlight(4);

    phaseCode.highlight(5);
    phaseCode.highlight(6);
    phaseCode.highlight(7);
    setExplanation(translator.translateMessage("explainTextHash"));
    lang.nextStep(); // step
    phaseCode.unhighlight(5);
    phaseCode.unhighlight(7);

    Text animationTextHash = lang.newText(
        new Offset(0, 20, "patternHash", "SW"),
        translator.translateMessage("textHash", String.valueOf(textHash)),
        "textHash", null);

    for (int h = 0; h < patternLength; h++) { // code line 5
      animationText.highlightCell(h, null, null);
      textHash = (textHash << 1) + text[h]; // code line 6
      setExplanation(translator.translateMessage("characterCode",
          String.valueOf(text[h]), String.valueOf((int) text[h])));
      lang.nextStep(); // step
      setExplanation(translator.translateMessage("buildHash",
          String.valueOf((int) text[h])));
      animationTextHash.setText(
          translator.translateMessage("textHash", String.valueOf(textHash)),
          null, null);
      lang.nextStep(); // step
      animationText.unhighlightCell(h, null, null);
    } // code line 7
    phaseCode.unhighlight(6);

    phaseCode.highlight(8);
    setExplanation(translator.translateMessage("searchTillEnd"));
    lang.nextStep(); // step
    phaseCode.unhighlight(8);

    patternMarker = lang.newArrayMarker(animationPattern, 0, "patternMarker",
        null);
    patternMarker.hide();
    textMarker = lang.newArrayMarker(animationText, 0, "textMarker", null);
    textMarker.hide();

    animationText.highlightElem(0, patternLength - 1, null, null);
    while (i < textLength - patternLength) { // code line 8

      if (patternHash == textHash) { // code line 9
        phaseCode.highlight(9);
        setExplanation(translator.translateMessage("hashesMatch"));
        lang.nextStep(); // step
        phaseCode.unhighlight(9);

        int j = 0; // code line 10
        phaseCode.highlight(10);
        setExplanation(translator.translateMessage("explainJ"));
        lang.nextStep(); // step
        phaseCode.unhighlight(10);

        phaseCode.highlight(11);
        setExplanation(translator.translateMessage("compareUntil"));
        lang.nextStep(); // step
        phaseCode.unhighlight(11);

        textMarker.move(i, null, null);
        patternMarker.move(0, null, null);
        textMarker.show();
        patternMarker.show();

        increaseComparisonCount();
        while (j < patternLength && text[i + j] == pattern[j]) { // code line 11

          j++; // code line 12
          textMarker.increment(null, null);
          patternMarker.increment(null, null);
          lang.nextStep(); // step
          if (j < patternLength) {
            increaseComparisonCount();
          }

        } // code line 13
        phaseCode.highlight(14);
        if (j == patternLength) { // code line 14
          occurrences.add(i); // code line 15
          phaseCode.highlight(15);
          phaseCode.highlight(16);
          setExplanation(translator.translateMessage("foundPattern"));
          lang.nextStep(); // step
          phaseCode.unhighlight(15);
          phaseCode.unhighlight(16);
        } // code line 16
        else {
          setExplanation(translator.translateMessage("mismatch"));
          lang.nextStep(); // step
        }
        phaseCode.unhighlight(14);
        textMarker.hide();
        patternMarker.hide();
      } // code line 17

      else {
        phaseCode.highlight(9);
        setExplanation(translator.translateMessage("hashesDoNotMatch"));
        lang.nextStep(); // step
        phaseCode.unhighlight(9);
      }

      textHash = ((textHash - text[i] * factor) << 1) + text[i + patternLength]; // code
                                                                                 // line
                                                                                 // 18
      phaseCode.highlight(18);
      phaseCode.highlight(19);
      setExplanation(translator.translateMessage("nextWindowHash"));
      animationTextHash.setText(
          translator.translateMessage("textHash", String.valueOf(textHash)),
          null, null);
      animationText.unhighlightElem(i, null, null);
      animationText.highlightElem(i + patternLength, null, null);
      lang.nextStep(); // step
      phaseCode.unhighlight(18);
      phaseCode.unhighlight(19);
      i++; // code line 19

    } // code line 20

    phaseCode.highlight(21);
    setExplanation(translator.translateMessage("lastPosition"));
    lang.nextStep(); // step
    phaseCode.unhighlight(21);
    if (patternHash == textHash) { // code line 21
      phaseCode.highlight(21);
      setExplanation(translator.translateMessage("hashesMatch"));
      lang.nextStep(); // step
      phaseCode.unhighlight(21);

      int j = 0; // code line 22
      phaseCode.highlight(22);
      setExplanation(translator.translateMessage("explainJ"));
      lang.nextStep(); // step
      phaseCode.unhighlight(22);

      phaseCode.highlight(23);
      setExplanation(translator.translateMessage("compareUntil"));
      lang.nextStep(); // step
      phaseCode.unhighlight(23);

      textMarker.move(i, null, null);
      patternMarker.move(0, null, null);
      textMarker.show();
      patternMarker.show();

      increaseComparisonCount();
      while (j < patternLength && text[i + j] == pattern[j]) { // code line 23
        j++; // code line 24
        textMarker.increment(null, null);
        patternMarker.increment(null, null);
        lang.nextStep(); // step
        if (j < patternLength) {
          increaseComparisonCount();
        }

      } // code line 25
      phaseCode.highlight(26);
      if (j == patternLength) { // code line 26
        occurrences.add(i); // code line 27
        phaseCode.highlight(27);
        phaseCode.highlight(28);
        setExplanation(translator.translateMessage("foundPattern"));
        lang.nextStep(); // step
        phaseCode.unhighlight(27);
        phaseCode.unhighlight(28);
      } // code line 28
      else {
        setExplanation(translator.translateMessage("mismatch"));
        lang.nextStep(); // step
      }
    } // code line 29
    else {
      phaseCode.highlight(21);
      setExplanation(translator.translateMessage("hashesDoNotMatch"));
      lang.nextStep(); // step
      phaseCode.unhighlight(21);
    }
    animationText.unhighlightElem(i, textLength - 1, null, null);
    phaseCode.unhighlight(26);
    return occurrences; // code line 30
  } // code line 31
}
