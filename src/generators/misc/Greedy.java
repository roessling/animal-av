package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Greedy implements ValidatingGenerator {

  private int[]                 nutzen;
  private int[]                 gewicht;
  private double                gewichtKap;
  private String[]              namen;
  double[]                      relNutzen;
  int[]                         auswahl;
  int                           gesamtNutzen  = 0;
  int                           gesamtGewicht = 0;

  private Language              lang;
  private ArrayProperties       NamenArrayProperties;
  private ArrayProperties       NutzenArrayProperties;
  private ArrayProperties       AuswahlArrayProperties;
  private ArrayMarkerProperties ArrayMarker;
  private ArrayProperties       RelNutzenProperties;
  private ArrayProperties       GewichtsArrayProperties;
  private SourceCodeProperties  SourceCodeProperties;

  public void init() {
    lang = new AnimalScript("Der Greedy-Algorithmus fuer das Rucksackproblem",
        "Marlene Utz,Martin Hameister,Lea Rausch", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    gewicht = (int[]) primitives.get("Gewicht");
    NamenArrayProperties = (ArrayProperties) props
        .getPropertiesByName("NamenArrayProperties");
    namen = (String[]) primitives.get("Namen");
    NutzenArrayProperties = (ArrayProperties) props
        .getPropertiesByName("NutzenArrayProperties");
    AuswahlArrayProperties = (ArrayProperties) props
        .getPropertiesByName("AuswahlArrayProperties");
    ArrayMarker = (ArrayMarkerProperties) props
        .getPropertiesByName("ArrayMarker");
    RelNutzenProperties = (ArrayProperties) props
        .getPropertiesByName("RelNutzenProperties");
    GewichtsArrayProperties = (ArrayProperties) props
        .getPropertiesByName("GewichtsArrayProperties");
    nutzen = (int[]) primitives.get("Nutzen");
    SourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("SourceCodeProperties");
    gewichtKap = (Integer) primitives.get("Rucksackkapazitaet");

    lang.setStepMode(true);

    gesamtNutzen = 0;
    gesamtGewicht = 0;

    relNutzen = new double[namen.length];
    auswahl = new int[namen.length];
    greedyAnimieren();

    lang.finalizeGeneration();
    return lang.toString();
  }

  // calcRelNutzen berechnet den relativen Nutzen der einzelnen Packstücke
  public void calcRelNutzen() {
    if (gewicht.length == nutzen.length) {
      for (int i = 0; i < gewicht.length; i++) {

        double tempNutzen = (double) nutzen[i] / (double) gewicht[i];
        double tempDouble = tempNutzen * 100;
        int tempInt = (int) tempDouble;
        relNutzen[i] = ((double) tempInt) / 100;
      }
    } else
      System.out
          .println("Längen des Gewichts- und Nutzen-Arrays stimmen nicht überein");
  }

  // sort sortiert die Packstücke nach absteigendem relativen Nutzen
  public void sort() {
    boolean swapped = true;
    int n = relNutzen.length;
    while (swapped == true) {
      swapped = false;
      for (int i = 0; i < n - 1; i++) {
        if (relNutzen[i] < relNutzen[i + 1]) {
          swap(i, i + 1);
          swapped = true;
        }
      }
      n--;

    }
  }

  public void swap(int i, int j) {
    double tempRelNutzenI = relNutzen[i];
    double tempRelNutzenJ = relNutzen[j];
    relNutzen[i] = tempRelNutzenJ;
    relNutzen[j] = tempRelNutzenI;

    int tempGewI = gewicht[i];
    int tempGewJ = gewicht[j];
    gewicht[i] = tempGewJ;
    gewicht[j] = tempGewI;

    int tempNutzenI = nutzen[i];
    int tempNutzenJ = nutzen[j];
    nutzen[i] = tempNutzenJ;
    nutzen[j] = tempNutzenI;

    String tempNameI = namen[i];
    String tempNameJ = namen[j];
    namen[i] = tempNameJ;
    namen[j] = tempNameI;

  }

  public void greedy(SourceCode sc, StringArray namenArray,
      IntArray nutzenArray, IntArray gewichtArray, IntArray auswahlArray) {

    TextProperties textProp = new TextProperties();
    TextProperties textPropTrue = new TextProperties();
    textPropTrue.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    TextProperties textPropFalse = new TextProperties();
    textPropFalse.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    lang.nextStep();
    sc.unhighlight(2);

    // Array Marker
    ArrayMarker namenMarker = lang.newArrayMarker(namenArray, (-1), "i", null,
        ArrayMarker);

    Text GesGew = lang.newText(new Offset(10, 0, auswahlArray,
        AnimalScript.DIRECTION_NE), "Gesamtgewicht:  " + gesamtGewicht,
        "GesGew", null, textProp);
    Text GesNutz = lang.newText(new Offset(10, 0, GesGew,
        AnimalScript.DIRECTION_NE), "Gesamtnutzen:  " + gesamtNutzen,
        "GesNutz", null, textProp);

    boolean first = true;
    for (int i = 0; i < relNutzen.length; i++) {
      sc.highlight(3);
      namenMarker.move(i, null, null);
      String label = i + "-te Iteration";
      lang.nextStep(label);
      sc.unhighlight(3);
      sc.highlight(4);

      if (gewichtKap >= (gesamtGewicht + gewicht[i])) {

        lang.nextStep();
        if (first) {

          TrueFalseQuestionModel einpackq = new TrueFalseQuestionModel("3",
              true, 0);
          einpackq
              .setPrompt("Kann das aktuell betrachtete Packstück eingepackt werden?");
          einpackq.setFeedbackForAnswer(true,
              "Das Packstueck kann eingepackt werden");
          einpackq.setFeedbackForAnswer(false,
              "Das Packstueck kann nicht eingepackt werden");
          lang.addTFQuestion(einpackq);
          first = false;
        }
        namenArray.highlightCell(i, null, null);

        lang.nextStep();
        Text einpacken = lang.newText(new Offset(0, 50, sc,
            AnimalScript.DIRECTION_SW),
            "Das Packstueck kann eingepackt werden.", "einpacken" + i, null,
            textPropTrue);

        sc.unhighlight(4);
        sc.highlight(5);
        auswahlArray.highlightCell(i, null, null);
        auswahl[i] = 1;
        lang.nextStep();
        auswahlArray.put(i, 1, null, null);

        lang.nextStep();
        sc.unhighlight(5);
        sc.highlight(6);
        gewichtArray.highlightCell(i, null, null);
        gesamtGewicht = gesamtGewicht + gewicht[i];

        GesGew.hide();
        Text GesGewGruen = lang.newText(new Offset(0, 0, GesGew,
            AnimalScript.DIRECTION_NW), "Gesamtgewicht:  " + gesamtGewicht,
            ("GesGewGruen" + i), null, textPropTrue);

        lang.nextStep();
        sc.unhighlight(6);
        gewichtArray.unhighlightCell(i, null, null);
        GesGewGruen.hide();
        GesGew = lang.newText(new Offset(0, 0, GesGew,
            AnimalScript.DIRECTION_NW), "Gesamtgewicht:  " + gesamtGewicht,
            ("GesGew" + i), null, textProp);

        sc.highlight(7);
        nutzenArray.highlightCell(i, null, null);
        gesamtNutzen = gesamtNutzen + nutzen[i];

        GesNutz.hide();
        Text GesNutzGruen = lang.newText(new Offset(0, 0, GesNutz,
            AnimalScript.DIRECTION_NW), "Gesamtnutzen:  " + gesamtNutzen,
            ("GesNutzGruen" + i), null, textPropTrue);

        lang.nextStep();
        sc.unhighlight(7);
        einpacken.hide();
        namenArray.unhighlightCell(i, null, null);
        nutzenArray.unhighlightCell(i, null, null);
        GesNutzGruen.hide();
        GesNutz = lang.newText(new Offset(0, 0, GesNutz,
            AnimalScript.DIRECTION_NW), "Gesamtnutzen:  " + gesamtNutzen,
            ("GesNutz" + i), null, textProp);

      } else {

        lang.nextStep();
        Text nichtEinpacken = lang.newText(new Offset(0, 50, sc,
            AnimalScript.DIRECTION_SW),
            "Das Packstueck kann nicht eingepackt werden.", "Neinpacken" + i,
            null, textPropFalse);

        lang.nextStep();
        sc.unhighlight(4);
        sc.highlight(9);
        auswahlArray.highlightCell(i, null, null);
        auswahl[i] = 0;
        auswahlArray.put(i, 0, null, null);

        lang.nextStep();
        sc.unhighlight(9);
        nichtEinpacken.hide();
      }
    }
    namenMarker.hide();
  }

  public void greedyAnimieren() {

    // Header
    TextProperties headerVisProp = new TextProperties();
    Font headerFont = new Font("SansSerif", 1, 24);
    headerVisProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    headerVisProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);

    Text header = lang.newText(new Coordinates(20, 30),
        "Der Greedy-Algorithmus fuer das Rucksackproblem", "header", null,
        headerVisProp);

    RectProperties headerRecProp = new RectProperties();
    headerRecProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    headerRecProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    headerRecProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "hRect", null,
        headerRecProp);

    // Algorithmus-Beschreibung
    TextProperties textPropBeschreibung = new TextProperties();
    Font beschrebungFont = new Font("SansSerif", 1, 18);
    textPropBeschreibung.set(AnimationPropertiesKeys.FONT_PROPERTY,
        beschrebungFont);

    String beschreibungString = getDescription();

    String[] beschreibungSplit = beschreibungString.split("\n");
    Text[] beschreibung = new Text[beschreibungSplit.length];

    lang.nextStep("Einleitung");
    for (int i = 0; i < beschreibungSplit.length; i++) {
      beschreibung[i] = lang.newText(new Offset(20, (80 + i * 25), header,
          AnimalScript.DIRECTION_SW), beschreibungSplit[i],
          ("Beschreibung" + i), null, textPropBeschreibung);
    }

    lang.nextStep();
    for (int i = 0; i < beschreibungSplit.length; i++) {
      beschreibung[i].hide();
    }

    // Arrays
    TextProperties textProp = new TextProperties();
    Text kapa = lang.newText(new Offset(15, 40, header,
        AnimalScript.DIRECTION_SW), "Rucksack-Kapazitaet:      " + gewichtKap,
        "kapa", null, textProp);
    Text nameText = lang.newText(new Offset(0, 40, kapa,
        AnimalScript.DIRECTION_SW), "Namen: ", "nameText", null, textProp);
    Text nutzenText = lang.newText(new Offset(0, 15, nameText,
        AnimalScript.DIRECTION_SW), "Nutzen: ", "nutzenText", null, textProp);
    Text gewichtText = lang.newText(new Offset(0, 15, nutzenText,
        AnimalScript.DIRECTION_SW), "Gewicht: ", "gewichtText", null, textProp);
    Text relNutzenText = lang.newText(new Offset(0, 15, gewichtText,
        AnimalScript.DIRECTION_SW), "rel. Nutzen: ", "relNutzenText", null,
        textProp);
    lang.newText(new Offset(0, 15, relNutzenText, AnimalScript.DIRECTION_SW),
        "Auswahl: ", "auswahlText", null, textProp);

    StringArray nameArray = lang.newStringArray(new Offset(30, 0, nameText,
        AnimalScript.DIRECTION_NE), namen, "name", null, NamenArrayProperties);
    IntArray nutzenArray = lang.newIntArray(new Offset(0, 10, nameArray,
        AnimalScript.DIRECTION_SW), nutzen, "nutzen", null,
        NutzenArrayProperties);
    IntArray gewichtArray = lang.newIntArray(new Offset(0, 10, nutzenArray,
        AnimalScript.DIRECTION_SW), gewicht, "gewicht", null,
        GewichtsArrayProperties);
    DoubleArray relNutzenArray = lang.newDoubleArray(new Offset(0, 10,
        gewichtArray, AnimalScript.DIRECTION_SW), relNutzen, "relNutzen", null,
        RelNutzenProperties);
    IntArray auswahlArray = lang.newIntArray(new Offset(0, 10, relNutzenArray,
        AnimalScript.DIRECTION_SW), auswahl, "auswahl", null,
        AuswahlArrayProperties);

    // Quellcode
    lang.nextStep();

    // SourceCodeProperties scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
    // Font.PLAIN, 12));
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode sc = lang.newSourceCode(new Offset(100, 40, header,
        AnimalScript.DIRECTION_SE), "sourceCode", null, SourceCodeProperties);

    sc.addCodeLine("public void greedy() {", null, 0, null);
    sc.addCodeLine("calcRelNutzen();", null, 1, null);
    sc.addCodeLine("sort();", null, 1, null);
    sc.addCodeLine("for (int i = 0; i < relNutzen.size(); i++) {", null, 1,
        null);
    sc.addCodeLine("if (gewichtKap >= (gesamtGewicht + gewicht.get(i))) {",
        null, 2, null);
    sc.addCodeLine("auswahl.set(i, 1);", null, 3, null);
    sc.addCodeLine("gesamtGewicht = gesamtGewnicht + gewicht.get(i);", null, 3,
        null);
    sc.addCodeLine("gesamtNutzen = gesamtNutzen + nutzen.get(i);", null, 3,
        null);
    sc.addCodeLine("} else {", null, 2, null);
    sc.addCodeLine("auswahl.set(i, 0);", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    // calcRelNutzen
    lang.nextStep("Berechnung des realtiven Nutzens");
    sc.highlight(0);
    sc.unhighlight(0);
    sc.highlight(1);
    calcRelNutzen();
    Text calcRel = lang
        .newText(
            new Offset(0, 50, sc, AnimalScript.DIRECTION_SW),
            "Der relative Nutzen eines Packstückes entspricht dem Quotienten seines Nutzen und Gewichts.",
            "calcRel", null);

    // Frage zun rel Nutzen
    String answer = String.valueOf(relNutzen[0]);
    FillInBlanksQuestionModel q = new FillInBlanksQuestionModel("1");
    q.setPrompt("Wie lautet der relative Nutzen des ersten Packstücks? ");
    // Text test=lang.newText(new Offset(0, 100, sc,
    // AnimalScript.DIRECTION_SW),answer, "test", null);
    System.out.println(answer);
    q.addAnswer(
        answer,
        1,
        "Der relative Nutzen eines Packstückes entspricht dem Quotienten seines Nutzen und Gewichts.");
    lang.addFIBQuestion(q);

    lang.nextStep();
    relNutzenArray.hide();
    DoubleArray relNutzenArray2 = lang.newDoubleArray(new Offset(0, 10,
        gewichtArray, AnimalScript.DIRECTION_SW), relNutzen, "relNutzen2",
        null, RelNutzenProperties);

    // sort
    lang.nextStep("Sortieren");
    sc.unhighlight(1);
    calcRel.hide();
    sc.highlight(2);
    sort();

    // Frage zur Sortierung
    MultipleChoiceQuestionModel multiq = new MultipleChoiceQuestionModel("2");
    multiq.setPrompt("Wie sieht die sortierte Liste aus?");
    String[] temp = new String[namen.length];
    for (int i = 0; i < namen.length; i++) {
      temp[namen.length - 1 - i] = namen[i];
    }
    String answer1 = "[" + namen[0];
    for (int i = 1; i < namen.length; i++) {
      answer1 = answer1 + ", " + namen[i];
    }
    answer1 = answer1 + "]";

    String answer2 = "[" + temp[0];
    for (int i = 1; i < temp.length; i++) {
      answer2 = answer2 + ", " + temp[i];
    }
    answer2 = answer2 + "]";

    multiq
        .addAnswer(answer1, 0,
            "Richtig! Die Packstücke werden nach absteigendem relativen Nutzen sortiert.");
    multiq
        .addAnswer(
            answer2,
            1,
            "Falsch! Die Packstücke werden nicht nach aufsteigendem sondern nach absteigendem relativen Nutzen sortiert.");
    lang.addMCQuestion(multiq);

    // Sortierte Arrays anzeigen
    lang.nextStep();
    relNutzenArray2.hide();
    nameArray.hide();
    gewichtArray.hide();
    nutzenArray.hide();
    StringArray nameArray2 = lang.newStringArray(new Offset(30, 0, nameText,
        AnimalScript.DIRECTION_NE), namen, "name2", null, NamenArrayProperties);
    IntArray nutzenArray2 = lang.newIntArray(new Offset(0, 10, nameArray,
        AnimalScript.DIRECTION_SW), nutzen, "nutzen2", null,
        NutzenArrayProperties);
    IntArray gewichtArray2 = lang.newIntArray(new Offset(0, 10, nutzenArray,
        AnimalScript.DIRECTION_SW), gewicht, "gewicht2", null,
        GewichtsArrayProperties);
    lang.newDoubleArray(new Offset(0, 10, gewichtArray,
        AnimalScript.DIRECTION_SW), relNutzen, "relNutzen3", null,
        RelNutzenProperties);

    // Greedy
    greedy(sc, nameArray2, nutzenArray2, gewichtArray2, auswahlArray);

    // Abschluss:
    lang.nextStep("Fazit");
    TextProperties abschlussProp = new TextProperties();
    Font abschlussFont = new Font("SansSerif", 1, 16);
    abschlussProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    abschlussProp.set(AnimationPropertiesKeys.FONT_PROPERTY, abschlussFont);

    int anzahlEingepackt = 0;
    for (int i = 0; i < auswahl.length; i++) {
      if (auswahl[i] == 1) {
        anzahlEingepackt++;
      }
    }
    String loesung = "";
    if (anzahlEingepackt > 1) {
      loesung = "die Packstücke ";
    } else {
      if (anzahlEingepackt == 0) {
        loesung = "kein Packstück ";
      } else {
        loesung = "das Packstück ";
      }
    }
    int j = 0;
    for (int i = 0; i < auswahl.length; i++) {
      if (auswahl[i] == 1) {
        loesung = loesung + namen[i];
        j++;
        if (j == anzahlEingepackt - 1) {
          loesung = loesung + " und ";
        }
        if (j == anzahlEingepackt) {
          loesung = loesung + " ";
        }
        if (j < anzahlEingepackt - 1) {
          loesung = loesung + ", ";
        }
      }
    }
    Text abschluss1 = lang.newText(new Offset(-100, 25, sc,
        AnimalScript.DIRECTION_SW),
        "Der Rucksack ist fertig gepackt und enthält " + loesung, "abschluss1",
        null, abschlussProp);
    Text abschluss2 = lang.newText(new Offset(0, 10, abschluss1,
        AnimalScript.DIRECTION_SW), "bei einem Gesamtgewicht von "
        + gesamtGewicht + " und Gesamtnutzen von " + gesamtNutzen + ".",
        "abschluss2", null, abschlussProp);
    Text abschluss3 = lang
        .newText(
            new Offset(0, 10, abschluss2, AnimalScript.DIRECTION_SW),
            "Die Laufzeit der Greedy-Heuristik liegt in O(n^2). Damit liefert sie die Möglichkeit",
            "abschluss3", null, abschlussProp);
    Text abschluss4 = lang
        .newText(
            new Offset(0, 10, abschluss3, AnimalScript.DIRECTION_SW),
            "in polynomieller Zeit eine zulässige (aber nicht zwangsläufig optimale) Lösung",
            "abschluss4", null, abschlussProp);
    lang.newText(new Offset(0, 10, abschluss4, AnimalScript.DIRECTION_SW),
        "des NP-schwerern Rucksack-Problems zu finden.", "abschluss5", null,
        abschlussProp);
  }

  public String getName() {
    return "Der Greedy-Algorithmus fuer das Rucksackproblem";
  }

  public String getAlgorithmName() {
    return "Der Greedy-Algorithmus fuer das Rucksackproblem";
  }

  public String getAnimationAuthor() {
    return "Marlene Utz, Martin Hameister, Lea Rausch";
  }

  public String getDescription() {
    return "\n"
        + "Das Rucksackproblem ist ein Problem aus der Kombinatorischen Optimierung. Gegeben ist "
        + "\n"
        + "ein Rucksack mit beschraenkter Gewichtskapazitaet und einer Menge aus Packstuecken"
        + "\n"
        + "mit eigenem Gewicht und Nutzen. Gesucht wird eine Auswahl an Packstuecken, die den  "
        + "\n"
        + "Gesamtnutzen maximiert und die die Gewichtskapazitaet nicht ueberschreitet. Da das  "
        + "\n"
        + "Rucksackproblem NP-schwer ist, sind oft schon zulaessige (aber noch nicht optimale) "
        + "\n"
        + "Loesungen von Interesse. Eine Meta-Heuristik zum Finden einer solchen zulaessigen  "
        + "\n"
        + "Loesung ist der Greedy-Algorithmus. Dieser versucht Packstuecke mit moeglichst grossem   "
        + "\n"
        + "relativen Nutzen auszuwaehlen, kann bei bestimmten Packstueckzusammensetzungen  "
        + "\n"
        + "jedoch beliebig schlecht werden. Dafuer werden die Packstuecke nach absteigendem "
        + "\n"
        + "relativen Nutzen sortiert und in entsprechender Reihenfolge in den Rucksack   "
        + "\n"
        + "eingepackt, wenn ihr Einpacken die Gesamtkapazitaet nicht ueberschreitet. ";
  }

  public String getCodeExample() {
    return "public void greedy(){" + "\n" + "\n" + "     calcRelNutzen();"
        + "\n" + "     sort();" + "\n"
        + "     for (int i = 0; i < relNutzen.size(); i++)" + "\n" + "     {"
        + "\n" + "         if (gewichtKap >= (gesamtGewicht + gewicht.get(i)))"
        + "\n" + "             {" + "\n" + " 			     auswahl.set(i, 1);" + "\n"
        + "                 gesamtGewicht = gesamtGewicht + gewicht.get(i);"
        + "\n"
        + "                 gesamtNutzen = gesamtNutzen + nutzen.get(i);"
        + "\n" + "             } else { " + "\n"
        + "                 auswahl.set(i, 0);" + "\n" + "             }"
        + "\n" + "     }" + "\n" + " }";
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
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    if (((String[]) arg1.get("Namen")).length != ((int[]) arg1.get("Nutzen")).length
        || ((int[]) arg1.get("Gewicht")).length != ((int[]) arg1.get("Nutzen")).length) {
      throw new IllegalArgumentException(
          "Die Arraylängen stimmen nicht überein!!!!!!!!!!!!");
    }
    return true;
  }

}