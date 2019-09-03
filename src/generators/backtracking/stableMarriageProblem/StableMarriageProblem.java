package generators.backtracking.stableMarriageProblem;

import generators.backtracking.helpers.Person;
import generators.backtracking.helpers.RankingExtractor;
import generators.backtracking.helpers.Table;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class StableMarriageProblem implements ValidatingGenerator {

  private Language              lang;

  private final static String   HEADER      = "Heiratsproblem";

  private final static String[] DESCRIPTION = {
      "Beim Heiratsproblem (stable marriage problem) sind n Männer und n Frauen gegeben. Zusätzlich hat jede Person jeder anderen",
      "Person des anderen Geschlechts eine eindeutige Rangnummer gegeben.",
      "Aufgabe ist es nun, dass jeder Mann eine Frau heiratet, sodass es keine zwei Personen gibt, die nicht miteinander verheiratet",
      "sind, sich aber gegenseitig ihrem eigenen Ehepartner vorziehen.",
      "",
      "Das Heiratsproblem kann mit dem Gale-Shapley Algorithmus gelöst werden. Zu Beginn dieses Algorithmus gelten alle Männer noch",
      "als unverlobt.",
      "In jeder Runde machen die unverlobten Männer der Frau, die sie am meisten bevorzugen einen Heiratsantrag. Erhält eine Frau",
      "einen Heiratsantrag und ist unverlobt, nimmt sie diesen an.",
      "Wenn sie schon verlobt ist, entscheidet sie anhand der Priorität, ob sie den Heiratsantrag ablehnt, oder ob sich sich von",
      "ihrem bisherigen Verlobten trennt und den Heiratsantrag annimmt." };

  private final static String[] SOURCE_CODE = {
      "initialisiere alle Männer und Frauen als unverlobt;",
      "solange es mindestens einen unverlobten Mann m gibt {",
      "   m macht der Frau w einen Antrag, die die höchte Priorität hat und die ihn noch nicht abgelehnt hat;",
      "   falls w noch unverlobt ist", "      w nimmt Antrag an;",
      "   ansonsten{", "      m' = der mit w verlobte Mann;",
      "      falls w m favorisiert",
      "         w nimmt Heiratsantrag an und trennt sich von m';",
      "      ansonsten", "         w lehnt Antrag von m ab;", "   }", "}" };

  private TextProperties        headerProps, normalTextProps, descriptionProps,
      headlineProps, tableTextProps;
  private RectProperties        headerRectProps;
  private SourceCodeProperties  sourceCodeProps;
  private PolylineProperties    lineProps;
  private Text                  header, textSC, text_m, text_m2, hints,
      explanation, text_w, c1, c2, c3, c4;
  private Table                 menTable, womenTable;
  private RankingExtractor      re;
  private SourceCode            sc;
  private Color                 highlightColor;
  private int                   number;
  private int                   counterRequests, counterReject1,
      counterReject2, counterAccept, counterIteration;
  private Color                 headerRectColor;

  public void init() {
    lang = new AnimalScript("Heiratsproblem", "Michael Sandforth", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    headerRectColor = (Color) primitives.get("headerRectColor");
    headlineProps = (TextProperties) props.getPropertiesByName("headline");
    normalTextProps = (TextProperties) props.getPropertiesByName("text");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    descriptionProps = (TextProperties) props
        .getPropertiesByName("description");
    tableTextProps = (TextProperties) props.getPropertiesByName("tableText");
    headerProps = (TextProperties) props.getPropertiesByName("header");

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    doAlgorithm((String) primitives.get("men"),
        (String) primitives.get("women"));
    lang.finalizeGeneration();

    return lang.toString();
  }

  public void doAlgorithm(String men, String women) {
    re = new RankingExtractor(men, women, lang);
    number = re.getMen().length;

    counterRequests = 0;
    counterReject1 = 0;
    counterReject2 = 0;
    counterAccept = 0;
    counterIteration = 0;

    setProperties();
    showDescription();
    showAlgorithm();
    showConclusion();
  }

  private void setProperties() {
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) headerProps
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont((float) 24));
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) headerProps
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.BOLD));

    normalTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) normalTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont((float) 16));

    descriptionProps = new TextProperties();
    descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 16));

    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) headlineProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont((float) 20));
    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) headlineProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont(Font.BOLD));

    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        ((Font) sourceCodeProps.get(AnimationPropertiesKeys.FONT_PROPERTY))
            .deriveFont((float) 16));

    tableTextProps = new TextProperties();
    tableTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 18));

    headerRectProps = new RectProperties();
    headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, headerRectColor);
    headerRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    lineProps = new PolylineProperties();
    lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    highlightColor = Color.red;
  }

  private void showDescription() {
    header = lang.newText(new Coordinates(20, 20), HEADER, "header", null,
        headerProps);
    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "headerRect",
        null, headerRectProps);

    Primitive[] des = new Primitive[DESCRIPTION.length];
    Primitive prev = (Primitive) header;
    for (int i = 0; i < DESCRIPTION.length; i++) {
      des[i] = lang.newText(new Offset(0, 8, prev, AnimalScript.DIRECTION_SW),
          DESCRIPTION[i], "description" + i, null, normalTextProps);
      prev = des[i];
    }

    lang.nextStep("Einleitung");

    for (int i = 0; i < DESCRIPTION.length; i++) {
      des[i].hide();
    }
  }

  private void showAlgorithm() {
    menTable = new Table("Männer", headlineProps, tableTextProps, lineProps,
        new Offset(0, 30, header, AnimalScript.DIRECTION_SW), lang,
        re.getMen(), re.getWomen());
    womenTable = new Table("Frauen", headlineProps, tableTextProps, lineProps,
        new Offset(3, 70, menTable.getRect(), AnimalScript.DIRECTION_SW), lang,
        re.getWomen(), re.getMen());

    textSC = lang.newText(new Offset(250, -40, menTable.getRect(),
        AnimalScript.DIRECTION_NE), "Sourcecode:", "textSC", null,
        headlineProps);
    sc = lang.newSourceCode(
        new Offset(0, 0, textSC, AnimalScript.DIRECTION_SW), "sourceCode",
        null, sourceCodeProps);
    for (int i = 0; i < SOURCE_CODE.length; i++) {
      sc.addCodeLine(SOURCE_CODE[i], null, 0, null);
    }

    lang.nextStep("Algorithmus");

    hints = lang.newText(new Offset(0, 50, sc, AnimalScript.DIRECTION_SW),
        "Hinweise:", "hints", null, headlineProps);
    Text initialisation = lang.newText(new Offset(0, 5, hints,
        AnimalScript.DIRECTION_SW),
        "Am Anfang werden alle Männer und Frauen als unverlobt initialisiert.",
        "init", null, normalTextProps);
    initialisation.changeColor(null, highlightColor, null, null);
    sc.highlight(0);

    explanation = lang.newText(new Offset(0, 5, hints,
        AnimalScript.DIRECTION_SW), "", "explanation", null, normalTextProps);
    text_m = lang.newText(new Offset(0, 5, explanation,
        AnimalScript.DIRECTION_SW), "", "m", null, normalTextProps);
    text_w = lang.newText(new Offset(0, 5, text_m, AnimalScript.DIRECTION_SW),
        "", "w", null, normalTextProps);
    text_m2 = lang.newText(new Offset(0, 5, text_w, AnimalScript.DIRECTION_SW),
        "", "m2", null, normalTextProps);

    c1 = lang.newText(new Offset(0, 5, text_m2, AnimalScript.DIRECTION_SW),
        "Heiratsanträge: " + counterRequests, "c1", null, normalTextProps);
    c2 = lang.newText(new Offset(0, 5, c1, AnimalScript.DIRECTION_SW),
        "angenommene Anträge: " + counterAccept, "c2", null, normalTextProps);
    c3 = lang.newText(new Offset(0, 5, c2, AnimalScript.DIRECTION_SW),
        "abgelehnte Anträge: " + counterReject1, "c3", null, normalTextProps);
    c4 = lang.newText(new Offset(0, 5, c3, AnimalScript.DIRECTION_SW),
        "aufgelöste Verlobungen: " + counterReject2, "c4", null,
        normalTextProps);

    lang.nextStep();

    boolean ready = false;
    sc.toggleHighlight(0, 1);
    initialisation.hide();

    int man = 0;
    int woman = 0;

    while (!ready) {
      explanation.setText(re.getMen()[man].getName()
          + " ist noch nicht verlobt.", null, null);
      explanation.changeColor(null, highlightColor, null, null);
      text_m.setText("m = " + re.getMen()[man].getName(), null, null);
      text_m.changeColor(null, highlightColor, null, null);

      lang.nextStep();

      sc.toggleHighlight(1, 2);

      int w1 = 0;
      int w2 = 0;

      menTable.setRequest(man, woman);
      for (int a = 0; a < re.getMen().length; a++)
        for (int b = 0; b < re.getMen().length; b++)
          if (re.getMen()[man].getPersonAt(woman) == re.getWomen()[a]
              && re.getWomen()[a].getPersonAt(b) == re.getMen()[man]) {
            w1 = a;
            w2 = b;
            womenTable.setRequest(a, b);
            explanation
                .setText(re.getMen()[man].getName() + " macht "
                    + re.getWomen()[a].getName() + " einen Antrag.", null, null);
            text_w.setText("w = " + re.getWomen()[a].getName(), null, null);
            text_w.changeColor(null, highlightColor, null, null);
            counterRequests++;
            c1.setText("Heiratsanträge: " + counterRequests, null, null);
          }
      text_m.changeColor(null, Color.black, null, null);

      lang.nextStep();

      text_w.changeColor(null, Color.BLACK, null, null);
      sc.toggleHighlight(2, 3);

      int best = -1;
      for (int i = number - 1; i > -1; i--) {
        if (womenTable.getRequests().getElement(i, w1) == 1
            && womenTable.getRejects().getElement(i, w1) == 0 && i != w2) {
          best = i;
        }
      }

      if (best == -1) {
        explanation.setText(re.getWomen()[w1].getName()
            + " ist noch nicht verlobt und nimmt deshalb den Antrag an.", null,
            null);
        sc.highlight(4);
        counterAccept++;
        c2.setText("angenommene Anträge: " + counterAccept, null, null);

        lang.nextStep();

        sc.unhighlight(4);
        sc.unhighlight(3);
      } else {
        Person fiance = re.getWomen()[w1].getPersonAt(best);
        explanation.setText(
            re.getWomen()[w1].getName() + " ist mit " + fiance.getName()
                + " verlobt.", null, null);

        text_m2.setText("m' = " + fiance.getName(), null, null);
        text_m2.changeColor(null, highlightColor, null, null);

        sc.highlight(6);

        lang.nextStep();

        sc.unhighlight(3);
        sc.toggleHighlight(6, 7);

        if (w2 < best) {
          explanation.setText(
              re.getWomen()[w1].getName() + " zieht "
                  + re.getMen()[man].getName() + " dem " + fiance.getName()
                  + " vor.", null, null);

          lang.nextStep();

          sc.toggleHighlight(7, 8);
          explanation.setText("Deshalb trennt sie sich von " + fiance.getName()
              + " und nimmt den Antrag von " + re.getMen()[man].getName()
              + " an.", null, null);
          womenTable.setReject(w1, best);
          for (int a = 0; a < re.getMen().length; a++)
            for (int b = 0; b < re.getMen().length; b++)
              if (re.getWomen()[w1].getPersonAt(best) == re.getMen()[a]
                  && re.getMen()[a].getPersonAt(b) == re.getWomen()[w1])
                menTable.setReject(a, b);
          counterReject2++;
          c4.setText("aufgelöste Verlobungen: " + counterReject2, null, null);
          counterAccept++;
          c2.setText("angenommene Anträge: " + counterAccept, null, null);
        } else {
          explanation.setText(
              re.getWomen()[w1].getName() + " zieht " + fiance.getName()
                  + " dem " + re.getMen()[man].getName() + " vor.", null, null);

          lang.nextStep();

          sc.toggleHighlight(7, 10);
          explanation.setText("Deshalb lehnt sie den Antrag von "
              + re.getMen()[man].getName() + " ab.", null, null);

          menTable.setReject(man, woman);
          for (int a = 0; a < re.getMen().length; a++)
            for (int b = 0; b < re.getMen().length; b++)
              if (re.getMen()[man].getPersonAt(woman) == re.getWomen()[a]
                  && re.getWomen()[a].getPersonAt(b) == re.getMen()[man])
                womenTable.setReject(a, b);
          counterReject1++;
          c3.setText("abgelehnte Anträge: " + counterReject1, null, null);
        }
        lang.nextStep();

        text_m2.hide();
        sc.unhighlight(8);
        sc.unhighlight(10);
      }

      int[] next = next();
      sc.highlight(1);
      if (counterIteration % re.getMen().length == 1) {
        if (next[0] != -1) {
          MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
              "ready" + counterIteration);
          mcqm.setPrompt("Ist der Algorithmus nun fertig?");
          mcqm.addAnswer("Ja", 0, "Falsch. " + re.getMen()[next[0]].getName()
              + " ist noch nicht verlobt.");
          mcqm.addAnswer("Nein", 1,
              "Richtig. " + re.getMen()[next[0]].getName()
                  + " ist noch nicht verlobt.");
          lang.addMCQuestion(mcqm);
        } else {
          MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel(
              "ready" + counterIteration);
          mcqm.setPrompt("Ist der Algorithmus nun fertig?");
          mcqm.addAnswer("Ja", 1, "Richtig. Alle Männer sind nun verlobt.");
          mcqm.addAnswer("Nein", 0, "Falsch. Alle Männer sind nun verlobt.");
          lang.addMCQuestion(mcqm);
        }
        explanation.setText("", null, null);
        lang.nextStep();
      }

      if (next[0] == -1) {
        ready = true;
        sc.unhighlight(1);
      } else {
        man = next[0];
        woman = next[1];
        text_w.setText("", null, null);
        explanation.setText(re.getMen()[man].getName()
            + " ist noch nicht verlobt.", null, null);
      }
      counterIteration++;
    }
    text_w.hide();
    text_m.hide();
    text_m2.hide();
    explanation.setText("Jeder hat nun einen Partner gefunden.", null, null);

    lang.nextStep();
  }

  public void showConclusion() {
    lang.hideAllPrimitives();
    header = lang.newText(new Coordinates(20, 20), HEADER, "header", null,
        headerProps);
    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "headerRect",
        null, headerRectProps);

    Text conclusion = lang.newText(new Offset(0, 10, header,
        AnimalScript.DIRECTION_SW), "Es gab " + counterRequests
        + " Heiratsanträge. Davon wurden " + counterAccept + " angenommen und "
        + counterReject1 + " direkt abgelehnt.", "conclusion", null,
        normalTextProps);
    if (counterReject2 == 1)
      lang.newText(new Offset(0, 5, conclusion, AnimalScript.DIRECTION_SW),
          "Eine Verlobung wurde wieder aufgelöst.", "conclusion2", null,
          normalTextProps);
    else
      lang.newText(new Offset(0, 5, conclusion, AnimalScript.DIRECTION_SW),
          counterReject2 + " Verlobungen wurden wieder aufgelöst.",
          "conclusion2", null, normalTextProps);

    lang.nextStep("Fazit");
  }

  private int[] next() {
    for (int x = 0; x < number; x++) {
      boolean ready = false;
      for (int i = 0; i < number && !ready; i++) {
        if (menTable.getRequests().getElement(i, x) == 1
            && menTable.getRejects().getElement(i, x) == 0)
          ready = true;
        else if (menTable.getRequests().getElement(i, x) == 0)
          return new int[] { x, i };
      }
    }
    return new int[] { -1, -1 };
  }

  public String getName() {
    return "Heiratsproblem";
  }

  public String getAlgorithmName() {
    return "Heiratsproblem";
  }

  public String getAnimationAuthor() {
    return "Michael Sandforth";
  }

  public String getDescription() {
    return "Beim Heiratsproblem (stable marriage problem) sind n M&auml;nner und n Frauen gegeben. Zus&auml;tzlich hat jede Person jeder anderen"
        + "\n"
        + "Person des anderen Geschlechts eine eindeutige Rangnummer gegeben."
        + "\n"
        + "Aufgabe ist es nun, dass jeder Mann eine Frau heiratet, sodass es keine zwei Personen gibt, die nicht miteinander verheiratet"
        + "\n"
        + "sind, sich aber gegenseitig ihrem eigenen Ehepartner vorziehen."
        + "\n"
        + "\n"
        + "Das Heiratsproblem kann mit dem Gale-Shapley Algorithmus gel&ouml;st werden. Zu Beginn dieses Algorithmus gelten alle M&auml;nner noch"
        + "\n"
        + "als unverlobt."
        + "\n"
        + "In jeder Runde machen die unverlobten M&auml;nner der Frau, die sie am meisten bevorzugen einen Heiratsantrag. Erh&auml;lt eine Frau"
        + "\n"
        + "einen Heiratsantrag und ist unverlobt, nimmt sie diesen an."
        + "\n"
        + "Wenn sie schon verlobt ist, entscheidet sie anhand der Priorit&auml;t, ob sie den Heiratsantrag ablehnt, oder ob sich sich von"
        + "\n"
        + "ihrem bisherigen Verlobten trennt und den Heiratsantrag annimmt.";
  }

  public String getCodeExample() {
    return "initialisiere alle M&auml;nner und Frauen als unverlobt;"
        + "\n"
        + "solange es mindestens einen unverlobten Mann m gibt {"
        + "\n"
        + "   m macht der Frau w einen Antrag, die die h&ouml;chte Priorit&auml;t hat und die ihn noch nicht abgelehnt hat;"
        + "\n" + "   falls w noch unverlobt ist" + "\n"
        + "      w nimmt Antrag an;" + "\n" + "   ansonsten{" + "\n"
        + "      m' = der mit w verlobte Mann;" + "\n"
        + "      falls w m favorisiert" + "\n"
        + "         w nimmt Heiratsantrag an und trennt sich von m';" + "\n"
        + "      ansonsten" + "\n" + "         w lehnt Antrag von m ab;" + "\n"
        + "   }" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_BACKTRACKING);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // RankingExtractor re =
    new RankingExtractor((String) primitives.get("men"),
        (String) primitives.get("women"), new AnimalScript("Heiratsproblem",
            "Michael Sandforth", 800, 600));
    return true;
  }

  /*
   * public static void main(String[] args){ Language l = new
   * AnimalScript("Heiratsproblem", "Michael Sandforth", 640, 480);
   * StableMarriageProblem smp = new StableMarriageProblem(l); smp.doAlgorithm(
   * "Adam: Gina, Eva, Sabine, Meike \n Bernd: Eva, Meike, Gina, Sabine \n Tom: Eva, Meike, Sabine, Gina \n Dominik: Meike, Eva, Sabine, Gina"
   * ,
   * "Eva: Adam, Dominik, Tom, Bernd \n Sabine: Adam, Bernd, Tom, Dominik \n Gina: Bernd, Dominik, Tom, Adam \n Meike: Tom, Adam, Bernd, Dominik"
   * ); System.out.println(l); }
   */
}
