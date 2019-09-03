package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Claudius Kleemann <claudius.kleeman@stud.tu-darmstadt.de>
 */
public class Base64Decode implements ValidatingGenerator {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;

  public Base64Decode() {

  }

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */

  public Base64Decode(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  /**
   * The Lookuptabel for encode
   */
  private static final String[]        BASE64_LOOCKUP_TABLE        = { "A",
      "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
      "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c",
      "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
      "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4",
      "5", "6", "7", "8", "9", "+", "/"                           };

  /**
   * The reverse lookup table
   */
  private List<String>                 BASE64_REVERSE_LOOKUP_TABLE = Arrays
                                                                       .asList(BASE64_LOOCKUP_TABLE);

  /**
   * The SourceCode of the encoding
   */
  private SourceCode                   intro;

  /**
   * The TextPropertie of the Header
   */
  private TextProperties               headerProp;

  /**
   * The TextPropertie of the normal Texts
   */
  private TextProperties               defaultProp;

  /**
   * The ArrayProperties
   */
  private ArrayProperties              arrayProps;

  /**
   * The Prop for sourceCode
   */
  private SourceCodeProperties         scProps;

  /**
   * The bitStreamArray
   */
  private StringArray                  symbolStreamArray;

  /**
   * The codeBlockArray
   */
  private StringArray                  codeBlockArray;

  /**
   * The Array with lookup in it;
   */
  private StringArray                  codeArray;

  /**
   * The calcText
   */
  private Text                         calcText;

  /**
   * the outputText
   */
  private Text                         outputText;

  /**
   * the encoding sourceCode
   */
  private SourceCode                   encodingSource;

  /**
   * the header text
   */

  private Text                         header;

  /**
   * last used Line counter
   */

  private int                          lastLineInUse               = -1;

  /**
   * The CounterProperties
   */
  private CounterProperties            counterProp;

  /**
   * The Counter
   */
  private TwoValueCounter              counter;

  // /**
  // * The View of the Counter
  // */
  // private TwoValueView view;

  /**
   * The result of the Encoding
   */

  private String                       result                      = "";

  /**
   * The sourceCode of the algo.
   */
  private final String[]               sourceCode                  = {
      "1. Kopiere 4 Zeichen aus der Zeichenkette",
      "2. Dekodiere das erste Zeichen", "3. Dekodiere das zweite Zeichen",
      "4. Wird die letzte Gruppe bearbeitet?",
      "5. Wenn das dritte Zeichen ein = ist beende die Decodierung",
      "6. Decodiere das dritte Zeichen",
      "7. Wird die letzte Gruppe bearbeitet?",
      "8. Wenn das vierte Zeichen ein = ist beende die Decodierung",
      "9. Dekodiere das vierte Zeichen",
      "10. Für jedes = werden 2 Bit gelöscht"                     };

  /**
   * The introText show on the first slide
   */
  private final String[]               introText                   = {
      "Die Base64 Codierung wird zur effizienten Darstellung von Binärdatein in ASCII verwendet.",
      "Einsatz findet diese Verfahren zum Beispiel beim versenden von Anhängen in E-Mails.",
      "Base64 ist ein Zahlensystem zur Basis 64. Das Alphabet besteht also aus 64 Zeichen.",
      "Das Alphabet der Base64 umfasst die Zeichen A-Z, a-z, 0-9 sowie + und /. ",
      "Jedes Zeichen hat somit einen Informationsgehalt von log_2(64) = 6 Bit.",
      "Anders formuliert bedeutet dies, dass ein Zeichen in Base64 6 Bit darstellt.",
      "Dafür werden beim decodieren 4 Zeichen in 3 Byte umgerechnet.",
      "Jedes Zeichen stellt dabei 6 Bit da. Die geschieht mit hilfe einer Codiertabelle",
      "",
      "Das kleinstes gemeinsames Vielfaches von 6 und 8 ist 24.",
      "Somit muss immer ein Vielfaches von 24 Bit bzw. 3 Byte in Base64 codiert werden.",
      "Dies ist notwendig um keine Zeichen zu erzeugen die nur \\\"halb gefüllt\\\" sind.",
      "\\\"Halb gefüllt\\\" müssten sonst gesondert Dargestellt werden um sicherzustellen,",
      "das beim decodieren die ursprüngliche Eingabe wiederhergestellt wird.",
      "Sollte die Anzahl der Bytes einmal nicht durch 3 teilbar sein, werden die fehlenden Bytes mit 0 aufgefüllt.",
      "Um bei der Decodierung die aufgefüllten Bytes nicht fälchlich der Binärdatei hinzuzufügen,",
      "wird für jedes Byte, das aufgefüllt wurde, ein Zeichen am Ende gelöscht und ein = hinzugefügt.",
      "Somit kann ein Wort in Base64 auf die normalen Zeichen des Alphabetes, = oder == enden.",
      "Würde man die hinzugefügten 0er Bytes bei Decodieren nicht wieder löschen,",
      "wäre das en- und decoden nicht transparent. de(en(a)) = a würde nicht mehr allgemein gelten.",
      "",
      "Beim Decodieren wird der Zeichenstream solange decodiert bis man auf das erste = Zeichen trifft",
      "oder der alle Zeichen übersetzt sind. Jedes = Zeichen bedeute ein Byte weniger Information die",
      "in diesem Zeichenstream gespeichert ist. Da beim encodieren vorher 0 aufgefüllt wurden um ein ",
      "vielfaches von 6 zu erreichen müssen für jedes = Zeichen 2 Bit der vorherigen Übersetzung",
      "gelöscht werden."                                          };

  private AnimationPropertiesContainer props;

  /**
   * The SymbolStream to work on
   */
  private String                       symbolStream[];

  private StringArray                  outputArray0;

  private StringArray                  outputArray1;

  private StringArray                  outputArray2;

  /**
   * Encodes an BitStream to Base64
   * 
   * @param symbols
   *          the Bitstream to encode
   */
  public void decode(String symbols) {
    // empty the result at the begin of a new encoding
    this.result = "";

    this.symbolStream = new String[symbols.length()];

    int k = 0;
    for (char oneSymbol : symbols.toCharArray()) {
      this.symbolStream[k] = Character.toString(oneSymbol);
      k++;
    }

    setProperties();

    // Intro Slide
    showIntro();

    // Start the UI
    createAnimationUI();

    final int numberOfIterations = symbolStream.length / 4;

    for (int i = 0; i < numberOfIterations; i++) {
      if (i != 0) {
        clearBlockArrays();
      }
      setNextCodeLine(0);

      copy4Symbols(i);

      boolean lastRound = !(i + 1 < numberOfIterations);

      decodeAllSymbols(lastRound);

      lang.nextStep();
    }
    // endloop
    showOutro();

  }

  /**
   * Sets the a new {@link #calcText} and ads the prefix
   * 
   * @param calcString
   *          the text to set
   */
  private void setCalcText(String calcString) {
    calcText.setText("Berechnung: " + calcString, null, null);
  }

  /**
   * Set which line of {@link #encodingSource} should be highlighted.
   * 
   * @param newLineNumber
   *          number of the new line to highlight
   */
  private void setNextCodeLine(int newLineNumber) {
    if (newLineNumber < this.sourceCode.length) {
      if (this.lastLineInUse >= 0) {
        encodingSource.toggleHighlight(this.lastLineInUse, newLineNumber);
      } else {
        encodingSource.highlight(newLineNumber);
      }
      this.lastLineInUse = newLineNumber;
    }
  }

  /**
   * Unhighlights the current line of {@link #encodingSource} and highlight the
   * next line
   */
  private void highlightNextCodeLine() {
    if (this.lastLineInUse == -1) {
      encodingSource.highlight(0);
      lastLineInUse = 0;
    } else if (this.lastLineInUse < this.sourceCode.length - 1) {
      encodingSource.toggleHighlight(lastLineInUse, lastLineInUse + 1);
      lastLineInUse++;
    }
  }

  /**
   * creates the Animation UI
   * 
   * @param outputArray0
   * @param outputArray1
   * @param outputArray2
   */
  private void createAnimationUI() {
    // hide the intro
    intro.hide();
    // symbolStream
    Text symbolStream = lang.newText(textOffest(header), "Zeichenstream: ",
        "symbolStreamt", null, defaultProp);

    symbolStreamArray = lang.newStringArray(arrayOffset(symbolStream),
        this.symbolStream, "symbolStreamArray", null, arrayProps);

    // CodeBlock
    Text symbolBlockText = lang.newText(textOffest(symbolStream),
        "Zeichenblock: ", "zeichenBlockText", null, defaultProp);

    String[] str = new String[4];
    Arrays.fill(str, "  ");

    codeBlockArray = lang.newStringArray(arrayOffset(symbolBlockText), str,
        "CodeBlockArray", null, arrayProps);

    // Codiertabelle
    Text codeArrayText = lang.newText(textOffest(symbolBlockText),
        "Codiertabelle: ", "codeArrayText", null, defaultProp);

    codeArray = lang.newStringArray(arrayOffset(codeArrayText),
        BASE64_LOOCKUP_TABLE, "codeArray", null, arrayProps);

    counter = lang.newCounter(codeBlockArray);

    // Berechnung und Ausgabe
    calcText = lang.newText(textOffest(codeArrayText), "Berechnung: ",
        "calcText", null, defaultProp);

    outputText = lang.newText(textOffest(calcText), "Ausgabe : ", "outputText",
        null, defaultProp);

    String[] str8 = new String[8];
    Arrays.fill(str8, "  ");

    outputArray0 = lang.newStringArray(arrayOffset(outputText), str8, "", null,
        arrayProps);
    outputArray1 = lang.newStringArray(new Offset(5, 0, outputArray0, "NE"),
        str8, "", null, arrayProps);
    outputArray2 = lang.newStringArray(new Offset(5, 0, outputArray1, "NE"),
        str8, "", null, arrayProps);

    lang.newText(new Offset(-10, -20, outputArray0, "N"), "1. Byte", "", null);
    lang.newText(new Offset(-10, -20, outputArray1, "N"), "2. Byte", "", null);
    lang.newText(new Offset(-10, -20, outputArray2, "N"), "3. Byte", "", null);

    lang.newText(new Offset(5, 0, outputArray2, "NE"),
        "Anmerkung: Es werden nur die 3 aktuellen Bytes dargestellt.", "", null);

    /*
     * StringArray outputArray1 = lang.newStringArray(
     * arrayOffset(outputArray1), str, null, null, arrayProps);
     */
    // Quelltext
    createCodierSource();

    // Zähler

    Text counterText = lang.newText(new Offset(0, 40, encodingSource, "SW"),
        "Zugriffe auf den Codeblock:", "counterText", null, defaultProp);

    lang.newCounterView(counter, new Offset(0, 40, counterText, "SW"),
        counterProp, true, true);

    lang.nextStep("Begin der Visualisierung");
  }

  /**
   * Clears the elements in {@link #codeBlockArray}
   */
  private void clearBlockArrays() {
    counter.deactivateCounting();
    for (int i = 0; i < this.codeBlockArray.getLength(); i++) {
      this.codeBlockArray.put(i, "  ", null, null);
    }
    for (int i = 0; i <= 23; i++) {
      this.putBitToArray(i, "  ");
    }
    counter.activateCounting();
  }

  /**
   * Show the PseudoCode of Base64 encoding
   */
  private void createCodierSource() {
    encodingSource = lang.newSourceCode(textOffest(outputText), "codierSource",
        null, scProps);

    for (String oneLine : sourceCode) {
      // get the Integer value of the first char in the line

      int valueOfFirstSymbol = Integer
          .decode(String.valueOf(oneLine.charAt(0))); // checks that the second
                                                      // char of the line is
      // a dot "."
      boolean secondIsADot = Character.toString(oneLine.charAt(1)).equals(".");

      int intention = 1;
      if ((secondIsADot == true)
          && ((valueOfFirstSymbol == 5) || (valueOfFirstSymbol == 8))) {
        intention = 2;
      }
      if (secondIsADot == false) {
        intention = 0;
      }
      encodingSource.addCodeLine(oneLine, null, intention, null);
    }
  }

  /**
   * Show the Intro
   */
  private void showIntro() {
    intro = lang.newSourceCode(textOffest(header), "intro", null, scProps);

    for (String oneLine : introText) {
      intro.addCodeLine(oneLine, null, 0, null);
    }
    lang.nextStep("Einleitung");
  }

  /**
   * Show the Outro
   */
  private void showOutro() {
    lang.hideAllPrimitives();
    header.show();
    SourceCode outro = lang.newSourceCode(textOffest(header), "outro", null,
        scProps);

    String[] outroText = {
        "Für die Darstellung von 3 Byte in Base64 werden 4 Byte benötigt. Diese entspricht einer Effizienz von 75%.",
        "Würde man eine Binärzahldatei einfach als folge von 0 und 1 in ASCII Darstellen so ergebe dies eine Effizienz von 12,5%.", };

    for (String oneLine : outroText) {
      outro.addCodeLine(oneLine, null, 0, null);
    }
  }

  /**
   * Set all Properties for Text, array, source etc.
   */
  private void setProperties() {
    // this.setPropsToUserInput();

    headerProp = new TextProperties();
    // headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "SansSerif boxed", Font.BOLD, 24));
    // Work arround to get a
    // boxed header
    headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
        Font.decode("SansSerif size 24 bold boxed-24"));

    defaultProp = new TextProperties();
    defaultProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 24));

    arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(
        115, 210, 22));
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(
        252, 233, 79));
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));

    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(239,
        41, 41));
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    counterProp = new CounterProperties();
    counterProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    counterProp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(114, 159,
        207));

    this.setPropsToUserInput();

    header = lang.newText(new Coordinates(20, 30), "Base64 Decoder", "header",
        null, headerProp);

  }

  /**
   * Sets the Properties to the user input.
   */
  private void setPropsToUserInput() {
    // Wenn wir nicht interativ unterwegs sind gibt das Probleme :) also
    // testen wir zuerst
    if (props != null) {
      this.defaultProp = (TextProperties) props.getPropertiesByName("Text");
      this.headerProp = (TextProperties) props
          .getPropertiesByName("Überschrift");
      this.arrayProps = (ArrayProperties) props.getPropertiesByName("Array");
      this.scProps = (SourceCodeProperties) props
          .getPropertiesByName("Quelltext");
    }
  }

  private void decodeAllSymbols(boolean lastRound) {
    for (int i = 0; i <= 3; i++) {
      highlightNextCodeLine();

      boolean breack = false;
      if (i > 1) {
        breack = handleLastRound(lastRound, i);
      }

      if (breack == true) {
        handelePaddingSigns(i);
        break;
      }

      decodeOneSymbol(i);
    }
  }

  private boolean handleLastRound(boolean lastRound, int i) {
    if (lastRound == false) {
      setCalcText("Da noch weitere Zeichen im Zeichenstream gibt es weitere Gruppen.");
      lang.nextStep();
      highlightNextCodeLine();
      highlightNextCodeLine();
      return false;
    } else {
      setCalcText("Wir befinden uns in der letzten Runde.");
      lang.nextStep();
      highlightNextCodeLine();
      final String currentSymbol = this.codeBlockArray.getData(i);
      if (currentSymbol.equals("=") == true) {
        setCalcText("Das aktuelle Symbol ist ein =. Die Dekodierung wird beendet.");
        lang.nextStep();
        return true;
      } else {
        setCalcText("Das aktuelle Symbol ist kein = . Die Dekodierung wird fortgesetzt.");
        lang.nextStep();
        highlightNextCodeLine();
        return false;
      }

    }

  }

  private void handelePaddingSigns(int i) {
    final int numberOfPaddingSigns = 4 - i;
    final int numberOfDecodedBytesInRound = i - 1;
    final int numberOfBitsToDrop = (3 - numberOfDecodedBytesInRound) * 2;

    // highlight the last line
    setNextCodeLine(9);

    String calcString = "Es wurden %s = Zeichen gefunden. Somit hat der letzte Block nur %s Bytes";
    calcString = String.format(calcString, numberOfPaddingSigns,
        numberOfDecodedBytesInRound);
    setCalcText(calcString);

    lang.nextStep("Behandlung angehängter = Zeichen");

    calcString = "Die letzten %s Bit sind somit nicht Teil der encodierten Information.";
    calcString = String.format(calcString, numberOfBitsToDrop);
    setCalcText(calcString);

    lang.nextStep();

    calcString = "Sie werden deshalb gelöscht";
    setCalcText(calcString);

    lang.nextStep();

    int start = 8 * numberOfDecodedBytesInRound;
    int end = start + numberOfBitsToDrop;

    for (int k = start; k < end; k++) {
      this.putBitToArray(k, "  ");
    }

    this.result = result.substring(0, result.length() - numberOfBitsToDrop);

    // outputText.setText(outputText.getText() + this.result, null, null);
  }

  private void putBitToArray(int bitNo, String bitValue) {
    final int arrayNo = bitNo / 8;
    final int posNo = bitNo % 8;

    if (arrayNo == 0) {
      this.outputArray0.put(posNo, bitValue, null, null);
    }
    if (arrayNo == 1) {
      this.outputArray1.put(posNo, bitValue, null, null);
    }
    if (arrayNo == 2) {
      this.outputArray2.put(posNo, bitValue, null, null);
    }

  }

  /**
   * 
   * @param i
   * @return true if decoded false if a = is found
   */
  private boolean decodeOneSymbol(int i) {
    StringArray source = this.codeBlockArray;

    source.highlightCell(i, null, null);

    final String currentSymbol = source.getData(i);

    if ((i > 1) && currentSymbol.equals("=")) {
      setCalcText("Das aktuelle zeichen ist ein = . Somit ist die normale Decodierung beendet");
      return false;

    } else {

      final int currentSymbolAsInt = this.BASE64_REVERSE_LOOKUP_TABLE
          .indexOf(currentSymbol);
      final String currentSymbolAsBin = String.format("%6s",
          Integer.toBinaryString(currentSymbolAsInt)).replace(' ', '0');

      codeArray.highlightCell(currentSymbolAsInt, null, null);

      String calcString = "Das Symbol %s entspricht dem Wert %s und den Bits %s";
      calcString = String.format(calcString, currentSymbol, currentSymbolAsInt,
          currentSymbolAsBin);

      setCalcText(calcString);

      lang.nextStep();

      int currentBitPosition = 6 * i;

      for (char currentBit : currentSymbolAsBin.toCharArray()) {
        putBitToArray(currentBitPosition, Character.toString(currentBit));
        currentBitPosition++;
      }

      // String newOutputText = outputText.getText() + this.result
      // + currentSymbolAsBin;
      // outputText.setText(newOutputText, null, null);

      lang.nextStep();

      source.highlightElem(i, null, null);
      source.unhighlightCell(i, null, null);
      codeArray.unhighlightCell(currentSymbolAsInt, null, null);

      this.result = this.result + currentSymbolAsBin;
      return true;
    }

  }

  private void copy4Symbols(int roundNumber) {
    final int beginSymbolStream = roundNumber * 4;
    final int endSymbolStream = beginSymbolStream + 3;

    StringArray from = symbolStreamArray;
    StringArray to = codeBlockArray;

    // Highlight the Cells
    from.highlightCell(beginSymbolStream, endSymbolStream, null, null);
    to.highlightCell(0, 3, null, null);

    setCalcText("Es werden vier Zeichen kopiert");

    final String labelText = "Decodierung der %d. 4 Zeichengruppe";

    lang.nextStep(String.format(labelText, roundNumber + 1));

    for (int i = 0; i <= 3; i++) {
      to.put(i, from.getData(beginSymbolStream + i), null, null);
    }

    lang.nextStep();

    // unhighlightCell work arrount!
    unhighlightCells(from, beginSymbolStream, endSymbolStream);
    unhighlightCells(to, 0, 3);

    from.highlightElem(beginSymbolStream, endSymbolStream, null, null);

    lang.nextStep();
  }

  /**
   * {@link StringArray#unhighlightCell(int, int, algoanim.util.Timing, algoanim.util.Timing)}
   * dose not what I think it should do. I write this workaround
   * 
   * @param target
   *          the {@link StringArray} to unhihglihtCells
   * @param begin
   *          the first Cell to unhighlight
   * @param end
   *          the last Cell to unhighlight
   */
  private void unhighlightCells(StringArray target, int begin, int end) {
    // Not working
    // target.unhighlightCell(begin, end, null, null);
    // Work arount:
    for (int i = 0; i <= end; i++) {
      target.unhighlightCell(i + begin, null, null);
    }
  }

  /**
   * Default offset for a new text Object.
   * 
   * @param referenceNode
   *          the node before the current one
   * @return the offset Object to referenceNode
   */
  private Offset textOffest(Text referenceNode) {
    return new Offset(0, 40, referenceNode, "SW");
  }

  /**
   * Default offset for a array to its description text
   * 
   * @param referenceNode
   *          the description text
   * @return the offset Object to referenceNode
   */
  private Offset arrayOffset(Text referenceNode) {
    return new Offset(5, -13, referenceNode, "E");
  }

  public void init() {
    lang = new AnimalScript("Base64 Decoder", "Claudius Kleemann", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    String symbolStream = (String) primitives.get("Symbolstream");

    boolean ignoreAllStyle = (Boolean) primitives.get("IgnoreAllStyle");

    System.out.println(ignoreAllStyle);
    if (ignoreAllStyle == false) {
      this.props = props;
    } else {
      this.props = null;
    }

    decode(symbolStream);
    return lang.toString();
  }

  public String getName() {
    return "Base64 Decoder";
  }

  public String getAlgorithmName() {
    return "Base64";
  }

  public String getAnimationAuthor() {
    return "Claudius Kleemann";
  }

  public String getDescription() {
    return stringArrayToDesString(this.introText, true);
  }

  public String getCodeExample() {
    return stringArrayToDesString(this.sourceCode, false);
  }

  /**
   * Translates a String Array to a String. Between each String Element a "\n"
   * Sign is addet.
   * 
   * @param text
   *          the String Array which should transformed to the String
   * @param quitOnEmptyParagraph
   *          if true transformation stops at the first empty String element
   * @return the String of the String array with "\n" between two elements
   */
  private String stringArrayToDesString(String[] text,
      final boolean quitOnEmptyParagraph) {
    StringBuilder stringBuilder = new StringBuilder();

    for (String textLine : text) {
      // Quit loop if we found a empty Line
      if ((quitOnEmptyParagraph == true) && textLine.equals("")) {
        break;
      }
      stringBuilder.append(textLine);
      stringBuilder.append("\n");
    }
    // remove the final "\n"
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);

    return stringBuilder.toString();
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

  public static void main(String[] args) {
    // Create a new animation
    // name, author, screen width, screen height

    Language l = new AnimalScript("Base64 Decoder", "Claudius Kleemann", 640,
        480);
    Base64Decode s = new Base64Decode(l);

    s.decode("ABCDEF==");
    System.out.println(l);
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    String symbolStream = (String) arg1.get("Symbolstream");

    String ausgabe = "";
    // Wenn der Zeichenstream nicht ein vielfaches von 4
    final int symbolsMoreThenFour = symbolStream.length() % 4;
    if (symbolsMoreThenFour != 0) {
      ausgabe += "Der Base64 Decoder kann nur vielfache von 4 Zeichen verarbeiten "
          + "Bitte gib %s weitere Zeichen ein.";
      ausgabe = String.format(ausgabe, (8 - symbolsMoreThenFour));
    }
    // Wenn etwas anders als 0 oder 1 im String steht ist das auch nicht
    // gewollt.
    // Wir löschen alle 0 und 1 aus dem String. Wenn keine weiteren Zeiche
    // im String sind ist der String danach leer

    String worker = symbolStream;
    for (String currentSymbol : Base64Decode.BASE64_LOOCKUP_TABLE) {
      worker = worker.replace(currentSymbol, "");
    }
    worker = worker.replace("=", "");

    final int numberOfInvalidSymbols = worker.length();
    if (numberOfInvalidSymbols != 0) {
      ausgabe += "\nDer Base64 Encoder arbeitet nur mit [A-Z][a-z][0-9] + / =. "
          + "Momentan sind %s andere Zeichen in deiner Eingabe enthalten. Es ist %s";
      ausgabe = String.format(ausgabe, numberOfInvalidSymbols, worker);
    }
    // Wenn die Ausgabe nicht leer ist werfen wir die Exception
    if (ausgabe.equals("") == false) {
      throw new IllegalArgumentException(ausgabe);
    }

    return true;
  }
}
