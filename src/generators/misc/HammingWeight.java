package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class HammingWeight implements ValidatingGenerator {
  private Language              lang;
  private ArrayProperties       word1Properties, word2Properties;
  private SourceCodeProperties  sourceCodeProperties;
  private SourceCodeProperties  variablesProperties;
  private RectProperties        titleBoxProperties, sourceCodeBoxProperties;
  private TextProperties        titleProperties;
  private SourceCodeProperties  descriptionProperties;
  private ArrayMarkerProperties arrayMarkerProperties;

  private int[]                 word1;
  private int[]                 word2;

  public void init() {
    lang = new AnimalScript("Hamming-Gewicht [DE]",
        "Marco Br&auml;uning, Nicolas Kolb", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    word1Properties = (ArrayProperties) props
        .getPropertiesByName("word1Properties");
    word2Properties = (ArrayProperties) props
        .getPropertiesByName("word2Properties");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProperties");
    variablesProperties = (SourceCodeProperties) props
        .getPropertiesByName("variablesProperties");
    titleBoxProperties = (RectProperties) props
        .getPropertiesByName("titleBoxProperties");
    sourceCodeBoxProperties = (RectProperties) props
        .getPropertiesByName("sourceCodeBoxProperties");
    titleProperties = (TextProperties) props
        .getPropertiesByName("titleProperties");
    descriptionProperties = (SourceCodeProperties) props
        .getPropertiesByName("descriptionProperties");
    arrayMarkerProperties = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarkerProperties");
    word1 = (int[]) primitives.get("word1");
    word2 = (int[]) primitives.get("word2");

    titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) titleProperties.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont(Font.BOLD, 24));

    lang.newText(new Coordinates(20, 30), "Hamming-Gewicht", "header", null,
        titleProperties);

    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, titleBoxProperties);

    lang.nextStep();

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    Text descriptionHeader = lang.newText(new Coordinates(20, 80),
        "Beschreibung des Algorithmus", "descriptionHeader", null, textProps);

    SourceCode intro = lang.newSourceCode(new Offset(0, 30,
        "descriptionHeader", AnimalScript.DIRECTION_NW), "descr", null,
        descriptionProperties);

    intro.addCodeLine(
        "Das Hamming-Gewicht ist ein Maß für die Unterschiedlichkeit", null, 0,
        null);
    intro.addCodeLine("von Zeichenketten. Es beschreibt dabei die Anzahl",
        null, 0, null);
    intro.addCodeLine(
        "der unterschiedlichen Stellen zweier gleichlanger Codewörter.", null,
        0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("Das Hamming-Gewicht wird häufig zur Fehlererkennung",
        null, 0, null);
    intro.addCodeLine(
        "und Fehlerkorrektur eingesetzt. Es werden beispielsweise beim", null,
        0, null);
    intro.addCodeLine(
        "Ausfüllen von Web-Formularen oftmals Korrekturvorschläge zum", null,
        0, null);
    intro.addCodeLine("Straßennamen mit Hilfe des Hamming-Gewichtes bestimmt.",
        null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("Die asymptotische Komplexität dieses Verfahrens", null,
        0, null);
    intro.addCodeLine(
        "beträgt O(|wort1|) bzw. O(|wort2|), da |wort1| = |wort2| gilt.", null,
        0, null);

    lang.nextStep("Einleitung");
    intro.hide();
    descriptionHeader.hide();

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    lang.newText(new Coordinates(20, 80), "", "exmplHeader", null, textProps);

    IntArray array1 = lang.newIntArray(new Offset(0, 30, "exmplHeader",
        AnimalScript.DIRECTION_SW), word1, "word1", null, word1Properties);

    IntArray array2 = lang.newIntArray(new Offset(0, 90, "exmplHeader",
        AnimalScript.DIRECTION_SW), word2, "word2", null, word2Properties);

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    lang.newText(new Coordinates(250, 80), "Pseudo-Code", "codeHeader", null,
        textProps);

    SourceCode sc = lang.newSourceCode(new Offset(0, 10, "codeHeader",
        AnimalScript.DIRECTION_SW), "code", null, sourceCodeProperties);

    sc.addCodeLine("int weight = 0;", null, 0, null);
    sc.addCodeLine("for (i=0 to word1.length) do", null, 0, null);
    sc.addCodeLine("if (word1.i != word2.i)", null, 1, null);
    sc.addCodeLine("weight++;", null, 2, null);
    sc.addCodeLine("end if", null, 1, null);
    sc.addCodeLine("end for", null, 0, null);

    lang.newRect(new Offset(-5, -5, "code", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "code", "SE"), "hRect", null, sourceCodeBoxProperties);

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
    lang.newText(new Coordinates(580, 80), "Variablenwerte", "variablesHeader",
        null, textProps);

    lang.nextStep("Start");

    int hammingWeight = 0;

    try {
      hammingWeight = hammingWeight(array1, array2, sc);
    } catch (Exception e) {
      e.printStackTrace();
    }

    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 18));

    lang.newText(new Coordinates(20, 300), "Das Hamming-Gewicht beträgt: "
        + hammingWeight, "exmplResult", null, textProps);

    lang.nextStep("Ende");

    lang.finalizeGeneration();
    return lang.toString();
  }

  private int hammingWeight(IntArray word1, IntArray word2, SourceCode code)
      throws Exception {

    int weight = 0;

    MultipleChoiceQuestionModel question1 = new MultipleChoiceQuestionModel(
        "question1");
    question1.setPrompt("Was passiert als nächstes?");
    question1
        .addAnswer(
            "Die Variable weight wird um 1 inkrementiert.",
            1,
            "Richtig! Da die beiden Werte nicht übereinstimmen, erhöht sich das Hamming-Gewicht folglich um 1.");
    question1
        .addAnswer(
            "Nichts, die verglichenen Werte stimmen überein.",
            0,
            "Falsch. In der Tat stimmen die Werte nicht überein. Sieh noch einmal genau hin!");

    MultipleChoiceQuestionModel question2 = new MultipleChoiceQuestionModel(
        "question2");
    question2.setPrompt("Was passiert als nächstes?");
    question2
        .addAnswer(
            "Die Variable weight wird um 1 inkrementiert.",
            0,
            "Falsch. Die beiden Werte stimmen überein, daher erhöht sich das Hamming-Gewicht nicht!");
    question2
        .addAnswer(
            "Nichts, die verglichenen Werte stimmen überein.",
            1,
            "Richtig! Das Hamming-Gewicht wird nicht erhöht und die For-Schleife wird fortgesetzt.");

    TwoValueCounter counter = lang.newCounter(word1);
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
    lang.newCounterView(counter, new Coordinates(580, 285), cp, true, true);

    for (int i = 0; i < word1.getLength(); i++) {
      SourceCode variables = lang.newSourceCode(new Offset(0, 10,
          "variablesHeader", AnimalScript.DIRECTION_SW), "variables", null,
          variablesProperties);
      variables.addCodeLine("", null, 0, null);
      variables.addCodeLine("i = " + i, null, 0, null);
      variables.addCodeLine("", null, 0, null);
      variables.addCodeLine("weight = " + weight, null, 0, null);

      if (i == 0)
        code.highlight(0);
      code.highlight(1);

      ArrayMarker marker1 = lang.newArrayMarker(word1, i, "marker1", null,
          arrayMarkerProperties);
      ArrayMarker marker2 = lang.newArrayMarker(word2, i, "marker2", null,
          arrayMarkerProperties);

      lang.nextStep((i + 1) + ". Iteration");
      code.unhighlight(0);
      code.unhighlight(1);
      code.highlight(2);

      lang.nextStep();
      code.unhighlight(2);

      if (word1.getData(i) != word2.getData(i)) {
        word1.highlightCell(i, null, null);
        word2.highlightCell(i, null, null);
        code.highlight(3);
        weight++;
        lang.addMCQuestion(question1);
        lang.nextStep();
        variables.hide();
        variables = lang.newSourceCode(new Offset(0, 10, "variablesHeader",
            AnimalScript.DIRECTION_SW), "variables", null, variablesProperties);
        variables.addCodeLine("", null, 0, null);
        variables.addCodeLine("i = " + i, null, 0, null);
        variables.addCodeLine("", null, 0, null);
        variables.addCodeLine("weight = " + weight, null, 0, null);
        code.unhighlight(3);
      } else if (i == 2) {
        lang.addMCQuestion(question2);
        lang.nextStep();
      }

      code.highlight(4);
      lang.nextStep();
      code.unhighlight(4);

      if (i != word1.getLength() - 1) {
        variables.hide();
      }
      marker1.hide();
      marker2.hide();

    }

    code.highlight(5);
    lang.nextStep();
    code.unhighlight(5);

    return weight;
  }

  public String getName() {
    return "Hamming-Gewicht [DE]";
  }

  public String getAlgorithmName() {
    return "Hamming-Gewicht";
  }

  public String getAnimationAuthor() {
    return "Marco Bräuning, Nicolas Kolb";
  }

  public String getDescription() {
    return "Das Hamming-Gewicht ist ein Ma&szlig; f&uuml;r die Unterschiedlichkeit"
        + "\n"
        + "von Zeichenketten. Es beschreibt dabei die Anzahl"
        + "\n"
        + "der unterschiedlichen Stellen zweier gleichlanger Codew&ouml;rter.\n"
        + "\n"
        + "Das Hamming-Gewicht wird h&auml;ufig zur Fehlererkennung"
        + "\n"
        + "und Fehlerkorrektur eingesetzt. Es werden beispielsweise beim"
        + "\n"
        + "Ausf&uuml;llen von Web-Formularen oftmals Korrekturvorschl&auml;ge zum"
        + "\n"
        + "Stra&szlig;ennamen mit Hilfe des Hamming-Gewichtes bestimmt.\n"
        + "\n"
        + "Die asymptotische Komplexit&auml;t dieses Verfahrens betr&auml;gt O(|wort1|)"
        + "\n" + "bzw. O(|wort2|), da |wort1| = |wort2| gilt.";
  }

  public String getCodeExample() {
    return "int weight = 0;" + "\n" + "for (i=0 to word1.length) do" + "\n"
        + "   if(word1.i != word2.i)" + "\n" + "      weight++;" + "\n"
        + "   end if" + "\n" + "end for";

  }

  public static void main(String[] args) {
    HammingWeight hw = new HammingWeight();
    hw.init();
    System.out.println(hw.generate(null, null));
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    word1 = (int[]) primitives.get("word1");
    word2 = (int[]) primitives.get("word2");

    if (word1.length != word2.length)
      throw new IllegalArgumentException(
          "\n\nDa der Algorithmus vorraussetzt, dass die zu "
              + "vergleichenden Wörter\ngleich lang sind, müssen die "
              + "die Größen der beiden Arrays identisch sein.\n\n"
              + "Überprüfen Sie bitte 'word1' und 'word2' "
              + "und setzen Sie\n'Anzahl der Elemente' auf den gleichen Wert.");

    return true;
  }

}