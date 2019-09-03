package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.*;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class DoubleDabble implements Generator {
  private Language              lang;
  private SourceCodeProperties  Beschreibungen;
  private TextProperties        zeilenInfos, bcd, original;
  private SourceCodeProperties  sourceCode;
  private ArrayProperties       orginalarray;
  private ArrayProperties       bcdarray;
  private int                   dezimalZahl;
  private ArrayMarkerProperties arrayMarker;
  private StringArray           originalArray;
  private StringArray           array;
  private int                   msqmCnt;

  public void init() {
    lang = new AnimalScript("DoubbleDabble [DE]",
        "Patrick Lerch,Felix Hammacher", 800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    lang.setStepMode(true);
  }

  private String getFontFromPropertie(AnimationProperties prop) {
    String font = prop.get("font").toString();
    int n = font.indexOf("name=");
    int komma = 0;
    for (int i = n; i < font.length(); i++) {
      if (font.charAt(i) == ',') {
        komma = i;
        break;
      }
    }
    font = font.substring(n + 5, komma);

    return font;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    //

    Beschreibungen = (SourceCodeProperties) props
        .getPropertiesByName("Beschreibungen");
    zeilenInfos = (TextProperties) props.getPropertiesByName("zeilenInfos");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    orginalarray = (ArrayProperties) props.getPropertiesByName("orginalarray");
    bcdarray = (ArrayProperties) props.getPropertiesByName("bcdarray");
    dezimalZahl = (Integer) primitives.get("dezimalZahl");
    arrayMarker = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker");
    bcd = (TextProperties) props.getPropertiesByName("bcd");
    this.original = (TextProperties) props.getPropertiesByName("original");

    zeilenInfos.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(this.getFontFromPropertie(zeilenInfos), Font.BOLD, 15));
    bcd.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(this.getFontFromPropertie(bcd), Font.BOLD, 15));
    original.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(this.getFontFromPropertie(original), Font.BOLD, 15));
    // Setze Beschreibung auf Größe 16
    Beschreibungen.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(this.getFontFromPropertie(Beschreibungen), Font.PLAIN, 16));
    this.erzeugeCode(dezimalZahl);

    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Double Dabble";
  }

  public String getAlgorithmName() {
    return "Double Dabble";
  }

  public String getAnimationAuthor() {
    return "Patrick Lerch, Felix Hammacher";
  }

  public String getDescription() {
    return "Der Double Dabble Algorithmus konvertiert Bin&#228;rZahlen in BCD Zahlen."
        + "\n"
        + "Daf&#252;r bekommt er eine Integer Zahl die Maximal 8 Ziffern breit sein darf (max. 99999999)."
        + "\n"
        + " Diese Zahl betrachtet der Double Dabble Algorithmus dann im Bin&#228;rformat."
        + "\n"
        + "Anschlie&#223;end wird diese Bin&#228;rzahlen durch Shift und Add Operation so in ein anderen Integer speicher geschoben, dass am Ende eine Integer Zahl vorliegt,"
        + "\n"
        + " die Bin&#228;r konvertiert genau f&#252;r die BCD Darstellung der Originalzahl steht.";
  }

  public String getCodeExample() {
    return "public static int doubleDabble(int dezimalWert){" + "\n"
        + "	String original = Integer.toBinaryString(dezimalWert);" + "\n"
        + "	int bcd =0;		" + "\n" + "	while(original.length()!=0){" + "\n"
        + "		for(int i=0;i<8;i++){" + "\n"
        + "			if(((bcd&(15*(int)(Math.pow(16, 7-i))))>>(28-i*4))>=5)" + "\n"
        + "				bcd += 3<<(28-4*i);	" + "\n" + "		}" + "\n" + "		bcd<<=1;"
        + "\n" + "		String tmp= original.substring(0, 1);" + "\n"
        + "		if(tmp.equals(\"1\"))" + "\n" + "			bcd |=1;" + "\n"
        + "		original = original.substring(1, original.length());			" + "\n"
        + "	}		" + "\n" + "	return bcd;			" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  private void erzeugeCode(int dezimalZahl) {

    // TO DO: GRUPPE ERZEUGEN
    lang.addQuestionGroup(new QuestionGroupModel("ad", 2));

    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
    TextProperties headerprobs = new TextProperties();
    headerprobs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30), "DoubleDabble",
        "header", null, headerprobs);

    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "hRect", null);
    headerprobs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));
    Text descrHd = lang.newText(new Coordinates(20, 80),
        "Beschreibung des Algorithmus", "descrHd", null, headerprobs);

    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLACK);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // now, create the source code entity
    SourceCode scHeader = lang.newSourceCode(new Offset(0, 30, descrHd,
        AnimalScript.DIRECTION_SW), "descr", null, Beschreibungen);
    scHeader.addCodeLine(
        "Der Double Dabble Algorithmus konvertiert BinärZahlen in BCD Zahlen",
        null, 0, null);
    scHeader
        .addCodeLine(
            "Dafür bekommt er eine Integer Zahl die Maximal 8 Ziffern breit sein darf (max. 99999999).",
            null, 0, null);
    scHeader
        .addCodeLine(
            "Diese Zahl betrachtet der Double Dabble Algorithmus dann im Binärformat",
            null, 0, null);
    scHeader
        .addCodeLine(
            "Anschliessend wird diese Binärzahlen durch Shift und Add Operation so in ein anderen Integer speicher geschoben, dass am Ende eine Integer Zahl vorliegt, ",
            null, 0, null);
    scHeader
        .addCodeLine(
            "die Binär konvertiert genau für die BCD Darstellung der Originalzahl steht.",
            null, 0, null);
    lang.nextStep("Algorithmusstart");
    scHeader.hide();
    descrHd.hide();
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 12));

    SourceCode sc = lang.newSourceCode(new Offset(0, 50, header,
        AnimalScript.DIRECTION_SW), "sourceCode", null, sourceCode);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    sc.addCodeLine("public static int doubleDabble(int dezimalWert){", null, 0,
        null); // 0
    sc.addCodeLine("String original = Integer.toBinaryString(dezimalWert);",
        null, 1, null);
    sc.addCodeLine("int bcd =0;		", null, 1, null);
    sc.addCodeLine("while(original.length()!=0){", null, 1, null); // 3
    sc.addCodeLine("for(int i=0;i<8;i++){", null, 2, null); // 4
    sc.addCodeLine("if(((bcd&(15*(int)(Math.pow(16, 7-i))))>>(28-i*4))>=5)",
        null, 3, null); // 5
    sc.addCodeLine("bcd += 3<<(28-4*i);	", null, 4, null); // 6
    sc.addCodeLine("}", null, 2, null); // 7
    sc.addCodeLine("bcd<<=1;", null, 2, null); // 8
    sc.addCodeLine("String tmp= original.substring(0, 1);", null, 2, null); // 9
    sc.addCodeLine("if(tmp.equals('1'))", null, 2, null); // 10
    sc.addCodeLine("bcd |=1;", null, 3, null); // 11
    sc.addCodeLine("original = original.substring(1, original.length());",
        null, 2, null); // 12
    sc.addCodeLine("}", null, 1, null); // 13
    sc.addCodeLine("return bcd;	", null, 1, null); // 14
    sc.addCodeLine("}", null, 0, null); // 15
    lang.nextStep("Funktionsaufruf");

    sc.highlight(0);
    headerprobs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 17));
    Text aufrufer = lang.newText(new Offset(200, -20, sc,
        AnimalScript.DIRECTION_NW), "Wir rufen die Funktion mit dem Wert "
        + dezimalZahl + " auf", "aufruf", null, zeilenInfos);
    lang.nextStep("Code-Betrachtung");
    sc.unhighlight(0);
    sc.highlight(1);
    aufrufer.hide();
    headerprobs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 15));
    headerprobs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    Text c1 = lang.newText(new Offset(550, 50, sc, AnimalScript.DIRECTION_SW),
        "original:", "c1", null, this.original);
    lang.newText(new Offset(0, -35, aufrufer, AnimalScript.DIRECTION_SW),
        "original:", "c11", null, this.original);
    headerprobs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

    originalArray = lang.newStringArray(new Offset(0, 20, c1,
        AnimalScript.DIRECTION_NW), new String[] { Integer
        .toBinaryString(dezimalZahl) }, "originalArray", null,
        this.orginalarray);

    TwoValueCounter counterOriginalArray = lang.newCounter(originalArray);
    lang.newCounterView(counterOriginalArray, new Offset(80, -20, aufrufer,
        AnimalScript.DIRECTION_NW), cp, true, true);

    // for getting the first access
    originalArray.put(0, Integer.toBinaryString(dezimalZahl), null, null);
    lang.nextStep();
    // Beginn step 5
    sc.unhighlight(1);
    sc.highlight(2);
    Text c2 = lang.newText(new Offset(-280, 4, c1, AnimalScript.DIRECTION_NW),
        "bcd:", "c2", null, bcd);
    lang.newText(new Offset(-0, -85, aufrufer, AnimalScript.DIRECTION_SW),
        "bcd:", "c2", null, bcd);
    headerprobs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    int ergebniss = 0;
    Text c2_1 = lang.newText(
        new Offset(-180, 20, c2, AnimalScript.DIRECTION_NW), "0 =", "c2_1",
        null, headerprobs);

    array = lang.newStringArray(new Offset(70, 0, c2_1,
        AnimalScript.DIRECTION_NW), new String[] { "0000", "0000", "0000",
        "0000", "0000", "0000", "0000", "0000" }, "array", null, this.bcdarray);
    TwoValueCounter counterArray = lang.newCounter(array);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    lang.newCounterView(counterArray, new Offset(80, -70, aufrufer,
        AnimalScript.DIRECTION_NW), cp, true, true);
    // for getting the first access
    array.put(7, "0000", null, null);

    lang.nextStep("AußereSchleife");
    // i=2 ersetzen mit originalArray.getData(0).length()!=0
    // ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
    // arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
    // "Betrachte diese 4 Bits für sich und prüfe ob Wert >=5");
    // arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    ArrayMarker iMarker = lang.newArrayMarker(array, 0, "iMarker", null,
        arrayMarker);
    iMarker.hide();
    Text c3 = lang.newText(new Offset(300, 45, sc, AnimalScript.DIRECTION_NW),
        "original.length() ist nicht 0, also Schleife wird ausgeführt", "c3",
        null, zeilenInfos);
    c3.hide();
    Text i = lang.newText(new Offset(0, 17, c3, AnimalScript.DIRECTION_NW),
        "i=0", "i", null, zeilenInfos);
    i.hide();
    Text shift = lang.newText(
        new Offset(0, -130, sc, AnimalScript.DIRECTION_SE),
        "shifte bcd um 1 bit nach links", "shift", null, zeilenInfos);
    shift.hide();
    Text ctmp = lang.newText(
        new Offset(0, -115, sc, AnimalScript.DIRECTION_SE), "tmp=1", "ctmp",
        null, zeilenInfos);
    ctmp.hide();
    Text ctmp0 = lang.newText(
        new Offset(0, -115, sc, AnimalScript.DIRECTION_SE), "tmp = 0", "ctmp0",
        null, zeilenInfos);
    ctmp0.hide();
    Text cOR = lang.newText(new Offset(0, -80, sc, AnimalScript.DIRECTION_SE),
        "Verändere die geshiftete 0 auf 1", "cOR", null, zeilenInfos);
    cOR.hide();
    Text abschnitt = lang
        .newText(
            new Offset(-150, -45, sc, AnimalScript.DIRECTION_SE),
            "Schneide in original das vorderste Zeichen ab, da es in bcd geshiftet wurde",
            "abschnitt", null, zeilenInfos);
    abschnitt.hide();
    Text bedingung = lang
        .newText(
            new Offset(-150, -28, shift, AnimalScript.DIRECTION_NW),
            "In diesen 4 Bits ist der Wert >=5. Nun wird 3 auf diese 4 Bits draufaddiert",
            "bedingung", null, zeilenInfos);
    bedingung.hide();
    while (originalArray.getData(0).length() != 0) {
      sc.unhighlight(2);
      sc.highlight(3);
      c3.show();
      lang.nextStep("innere Schleife");
      c3.hide();
      sc.unhighlight(3);
      sc.highlight(4);
      i.show();

      iMarker.show();
      for (int cnt = 0; cnt < 8; cnt++) {
        i.setText("i=" + cnt, null, null);
        lang.nextStep("Überprüfung von jeweils 4Bits");
        int wertVon4bit = Integer.parseInt(array.getData(cnt), 2);
        sc.unhighlight(4);
        sc.highlight(5);
        iMarker.move(cnt, null, null);
        if (wertVon4bit >= 5) {

          MultipleSelectionQuestionModel msqm = new MultipleSelectionQuestionModel(
              "msqm" + msqmCnt);
          msqm.setPrompt("In welchen 4 Bits wird sich der Wert ändern und wie wird er aussehen?");
          int antwort = Integer.parseInt(array.getData(cnt), 2);
          antwort += 3;
          for (int zaehler = 0; zaehler <= 7; zaehler++) {
            if (zaehler == cnt) {
              msqm.addAnswer(
                  "In dem " + cnt + "ten Arrayfeld, mit folgendem Inhalt: "
                      + Integer.toBinaryString(antwort), 1, "Richtig");
            } else {
              String myString = zufall4BitString();
              StringBuffer sb = new StringBuffer();
              sb.append("In dem " + zaehler
                  + "ten Arrayfeld, mit folgendem Inhalt: ");
              sb.append(myString + ".");
              msqm.addAnswer(sb.toString(), 0, "Falsch");
            }
          }
          lang.addMSQuestion(msqm);
          msqmCnt++;
          lang.nextStep();
          sc.unhighlight(5);
          sc.highlight(6);
          wertVon4bit += 3;
          ergebniss += 3 * (int) (Math.pow(16, 7 - cnt));
          c2_1.setText(ergebniss + " =", null, null);
          bedingung.show();
          array.put(cnt, Integer.toBinaryString(wertVon4bit), null, null);
          array.highlightElem(cnt, null, null);
        }
        lang.nextStep();
        array.unhighlightElem(cnt, null, null);
        bedingung.hide();
        sc.unhighlight(6);
        sc.unhighlight(5);
        sc.highlight(4);
      }
      i.setText("i=8 und somit wird die Schleife nicht mehr ausgeführt", null,
          null);
      lang.nextStep("BitShift");
      i.hide();
      iMarker.hide();
      iMarker.move(0, null, null);
      sc.unhighlight(4);
      sc.highlight(7);
      lang.nextStep();
      sc.unhighlight(7);
      sc.highlight(8);

      // für den Access beim Shift
      array.getData(0);
      shift.show();
      ergebniss *= 2;
      c2_1.setText(ergebniss + " =", null, null);
      String zwischenshift = Integer.toBinaryString(ergebniss);
      while (!(zwischenshift.length() % 4 == 0))
        zwischenshift = "0" + zwischenshift;

      int begin = 0;
      int zwCnt = 0;
      while (zwischenshift.length() != 0) {
        begin = zwischenshift.length() - 4;
        array.put(7 - zwCnt,
            zwischenshift.substring(begin, zwischenshift.length()), null, null);
        zwischenshift = zwischenshift.substring(0, zwischenshift.length() - 4);
        zwCnt++;
      }
      array.highlightElem(0, 7, null, null);
      lang.nextStep();
      array.unhighlightElem(0, 7, null, null);
      shift.hide();
      sc.unhighlight(8);
      sc.highlight(9);
      String tmp = originalArray.getData(0).substring(0, 1);
      if (tmp.equals("1"))
        ctmp.show();
      else
        ctmp0.show();
      lang.nextStep();
      sc.unhighlight(9);
      sc.highlight(10);
      FillInBlanksQuestionModel algoYear = new FillInBlanksQuestionModel(
          "BCD-Wert" + ergebniss);// BCD-Wert"+ergebniss);
      algoYear.setPrompt("Welchen Wert wird BCD im nächsten Schritt haben?");
      algoYear.setGroupID("ad");
      if (tmp.equals("1")) {
        // TO DO: QUESTION die nur 5 mal aufgerufen werden soll*************

        algoYear.addAnswer(Integer.toString(ergebniss + 1), 1,
            "Richtig. Er wird genau um 1 erhöht");

        lang.addFIBQuestion(algoYear);
        // ****************************************************************

        lang.nextStep();
        ergebniss += 1;
        c2_1.setText(ergebniss + " =", null, null);
        String kippeBit = array.getData(7);
        kippeBit = kippeBit.substring(0, 3) + "1";
        array.put(7, kippeBit, null, null);
        cOR.show();
        array.highlightElem(7, null, null);
        sc.unhighlight(10);
        sc.highlight(11);
      } else {
        algoYear.addAnswer(Integer.toString(ergebniss), 1,
            "Richtig. Er bleibt unverändert");
        lang.addFIBQuestion(algoYear);
      }
      lang.nextStep();
      array.unhighlightElem(7, null, null);
      cOR.hide();
      sc.unhighlight(11);
      sc.unhighlight(10);
      ctmp.hide();
      ctmp0.hide();
      sc.highlight(12);
      abschnitt.show();
      originalArray.put(
          0,
          originalArray.getData(0).substring(1,
              originalArray.getData(0).length()), null, null);
      originalArray.highlightElem(0, null, null);
      lang.nextStep();
      originalArray.unhighlightElem(0, null, null);
      abschnitt.hide();
      sc.unhighlight(12);
    }

    // lang.nextStep();
    abschnitt.hide();
    sc.unhighlight(12);
    sc.highlight(3);
    Text c3Ende = lang.newText(new Offset(0, 0, c3, AnimalScript.DIRECTION_NW),
        "original.length() ist nun 0, also Schleife wird NICHT ausgeführt",
        "c3Ende", null, zeilenInfos);
    lang.nextStep("Rückgabe");
    c3Ende.hide();
    sc.unhighlight(3);
    sc.highlight(13);
    lang.nextStep();
    sc.unhighlight(13);
    sc.highlight(14);
    Text ruechgabe = lang.newText(new Offset(-150, 30, abschnitt,
        AnimalScript.DIRECTION_NW), "Der Wert " + ergebniss
        + " aus bcd wird zurückgegeben.", "rueckgabe", null, zeilenInfos);
    array.getData(0);
    lang.nextStep("Zusammenfassung");
    ruechgabe.hide();
    sc.hide();
    String endZahl = Integer.toString(dezimalZahl);
    while (endZahl.length() != 8) {
      endZahl = "0" + endZahl;
    }
    StringBuffer sb = new StringBuffer();
    for (int cnt1 = 0; cnt1 < endZahl.length(); cnt1++) {
      sb.append(endZahl.substring(cnt1, cnt1 + 1));
      sb.append("      ");
      if (cnt1 == 5)
        sb.append(" ");

    }

    lang.newText(new Offset(-122, 30, c2, AnimalScript.DIRECTION_SE),
        sb.toString(), "ergebniss", null, zeilenInfos);
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.WHITE);
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.WHITE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));
    SourceCode bericht = lang.newSourceCode(new Offset(0, 30, descrHd,
        AnimalScript.DIRECTION_SW), "bericht", null, Beschreibungen);
    bericht.addCodeLine("Betrachtet man nun die " + ergebniss
        + " im Binärformat, sieht man genau,", null, 0, null);
    bericht.addCodeLine(
        "dass jeweils 4 Bit eine Ziffer der ursprünglichen Zahl " + dezimalZahl
            + " darstellen.", null, 0, null);
  }

  private String zufall4BitString() {
    StringBuffer sb = new StringBuffer();
    sb.append("1");
    for (int i = 0; i < 3; i++) {
      sb.append((int) Math.rint(Math.random()));
    }
    return sb.toString();
  }

}