package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.IntArray;
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
import algoanim.util.TicksTiming;

/**
 * @author Claudius Kleemann <claudius.kleeman@stud.tu-darmstadt.de>
 */
public class Base64Encode implements ValidatingGenerator {

  /**
   * The concrete language object used for creating output
   */
  private Language lang;

  public Base64Encode() {

  }

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */

  public Base64Encode(Language l) {
    // Store the language object
    lang = l;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  /**
   * The Lookuptabel for encode
   */
  private static final String[]        BASE64_LOOCKUP_TABLE = { "A", "B", "C",
      "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
      "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e",
      "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
      "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6",
      "7", "8", "9", "+", "/"                              };

  private String[]                     lookupTable;

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
   * The TextPropertie of the result Texts
   */
  private TextProperties               outputProp;

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
  private IntArray                     bitStreamArray;

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
   * the padding text
   */

  private Text                         paddingText;

  /**
   * the number of byte which has to be padded
   */
  private int                          numberOfPaddedBytes;

  /**
   * the Bitstream
   */
  private int[]                        bitStream;

  /**
   * last used Line counter
   */

  private int                          lastLineInUse        = -1;

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

  private String                       result               = "";

  /**
   * The sourceCode of the algo.
   */
  private final String[]               sourceCode           = {
      "1. Versuche 3 Byte aus dem Bitstream in den Codeblock zu kopieren",
      "2. Fülle die fehlenden Byte mit 0 auf",
      "3. Kodiere die erste 6 Bit Gruppe zu einem Zeichen",
      "4. Kodiere die zweite 6 Bit Gruppe zu einem Zeichen",
      "5. Kodiere die dritte 6 Bit Gruppe zu einem Zeichen",
      "6. Kodiere die vierte 6 Bit Gruppe zu einem Zeichen",
      "7. Lösche für jedes Byte das mit 0 aufgefüllt wurde ein Zeichen am Ende",
      "8. Füge soviele = an bis die länge der Ausgabe durch 4 teilbar ist" };

  /**
   * The introText show on the first slide
   */
  private final String[]               introText            = {
      "Die Base64 Encodierung wird zur effizienten Darstellung von Binärdatein in ASCII verwendet.",
      "Einsatz findet diese Verfahren zum Beispiel beim versenden von Anhängen in E-Mails.",
      "Base64 ist ein Zahlensystem zur Basis 64. Das Alphabet besteht also aus 64 Zeichen.",
      "Das Alphabet der Base64 umfasst die Zeichen A-Z, a-z, 0-9 sowie + und /. ",
      "Jedes Zeichen hat somit einen Informationsgehalt von log_2(64) = 6 Bit.",
      "Anders formuliert bedeutet dies, das ein Zeichen in Base64 6 Bit darstellt.",
      "Dafür werden beim Encodieren aus 3 Byte 4 Gruppen zu je 6 Bit gebildet.",
      "Jede 6 Bit Gruppe wird dann in ein Zeichen des Base 64 Alphabets übersetzt.",
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
      "wäre das en- und decoden nicht transparent. de(en(a)) = a würde nicht mehr allgemein gelten." };

  private AnimationPropertiesContainer props;

  /**
   * Encodes an BitStream to Base64
   * 
   * @param bitStream
   *          the Bitstream to encode
   */
  public void encode(int[] bitStream) {
    this.bitStream = bitStream;

    if (this.lookupTable == null) {
      this.lookupTable = BASE64_LOOCKUP_TABLE;
    }

    // empty the result at the begin of a new encoding
    this.result = "";

    setProperties();

    // Intro Slide
    showIntro();

    // Start the UI
    createAnimationUI();

    final int numberOfBytes = bitStream.length / 8;
    final int numberOfIterations = (numberOfBytes + 2) / 3;

    for (int i = 0; i < numberOfIterations; i++) {
      if (i != 0) {
        clearCodeBlockArray();
      }
      setNextCodeLine(0);

      copy3ByteGroup(i);

      highlightNextCodeLine();

      fillCodeBlockWithPadding();

      // Encoding laufen lassen

      encodeAllGroups();

      lang.nextStep();
    }
    // endloop
    highlightNextCodeLine();

    deletePaddingCreatetSymbols();

    highlightNextCodeLine();

    addEndSigns();
    lang.nextStep();

    showOutro();

  }

  /**
   * Add the Markes ("=") for the number of paddet Bytes
   */
  private void addEndSigns() {
    int resultLength = result.length();
    // yes i am cheating ;)
    int paddedBytes = this.numberOfPaddedBytes;

    String suffixText = "";

    switch (paddedBytes) {
      case 0:
        suffixText = "Es muss kein = hinzugefügt werden.";
        break;
      case 1:
        suffixText = "Es muss ein = hinzugefügt werden.";
        break;
      case 2:
        suffixText = "Es müssen zwei = hinzugefügt werden.";
        break;

      default:
        break;
    }

    String calcString = "Die Ausgabe ist %s Zeichen lang. %s";
    calcString = String.format(calcString, resultLength, suffixText);

    String prefix = "";
    for (int i = 1; i <= this.numberOfPaddedBytes; i++) {
      prefix = prefix + "=";
    }

    setCalcText(calcString);
    lang.nextStep("Anhängen der = Zeichen");
    String outputString = "Ausgabe: " + result + prefix;

    outputText.setText(outputString, null, null);
  }

  /**
   * Deletes symbols that were created during encoding by the padding
   */
  private void deletePaddingCreatetSymbols() {
    String calcString = "";
    switch (this.numberOfPaddedBytes) {
      case 0:
        calcString = "Es wurden kein Byte aufgefüllt. Darum wird kein Zeichen gelöscht.";
        break;
      case 1:
        calcString = "Es wurde ein Byte aufgefüllt. Darum wird ein Zeichen gelöscht.";
        break;
      case 2:
        calcString = "Es wurden zwei Bytes aufgefüllt. Darum werden zwei Zeichen gelöscht.";

      default:
        break;
    }

    setCalcText(calcString);

    paddingText.setText("#Aufgefüllt: 0", null, null);
    paddingText.show();

    int cut = result.length() - this.numberOfPaddedBytes;

    String newResult = result.substring(0, cut);
    lang.nextStep("Löschen angehängter Bytes");

    if (this.numberOfPaddedBytes > 0) {

      String outputText2 = "Ausgabe: " + newResult;

      outputText.setText(outputText2, null, null);

      lang.nextStep();
    }
    this.result = newResult;
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
   * Fills missing Bytes of {@link #codeBlockArray} with 0.
   */
  private void fillCodeBlockWithPadding() {
    int end = 23;
    int start = end - (numberOfPaddedBytes * 8) + 1;

    String calcString = "";
    String myPaddText = "#Aufgefüllt: " + numberOfPaddedBytes;

    switch (numberOfPaddedBytes) {
      case 0:
        calcString = "Es fehlt kein Byte.";
        break;
      case 1:
        calcString = "Es fehlt ein Byte. Dieses wird mit 0 aufüllt.";
        break;
      case 2:
        calcString = "Es fehlen zwei Bytes. Diese wird mit 0 aufüllt.";

      default:
        break;
    }

    setCalcText(calcString);

    if (numberOfPaddedBytes == 1 || numberOfPaddedBytes == 2) {

      codeBlockArray.highlightCell(start, end, null, null);

      lang.nextStep();

      for (int i = start; i <= end; i++) {
        codeBlockArray.put(i, "0", null, null);
      }
    }

    paddingText.setText(myPaddText, null, null);
    paddingText.show(new TicksTiming(50));

    lang.nextStep();

    unhighlightCells(codeBlockArray, start, end);

  }

  /**
   * Set which line of {@link #encodingSource} should be highlighted.
   * 
   * @param newLineNumber
   *          number of the new line to highlight
   */
  private void setNextCodeLine(int newLineNumber) {
    if (this.lastLineInUse >= 0) {
      encodingSource.toggleHighlight(this.lastLineInUse, newLineNumber);
    } else {
      encodingSource.highlight(newLineNumber);
    }
    this.lastLineInUse = newLineNumber;
  }

  /**
   * Unhighlights the current line of {@link #encodingSource} and highlight the
   * next line
   */
  private void highlightNextCodeLine() {
    if (this.lastLineInUse == -1) {
      encodingSource.highlight(0);
      lastLineInUse = 0;
    } else if (this.lastLineInUse < 7) {
      encodingSource.toggleHighlight(lastLineInUse, lastLineInUse + 1);
      lastLineInUse++;
    }
  }

  /**
   * creates the Animation UI
   */
  private void createAnimationUI() {
    // hide the intro
    intro.hide();
    // Bitstream
    Text bitStreamText = lang.newText(textOffest(header), "Bitstream: ",
        "bitStreamText", null, defaultProp);

    bitStreamArray = lang.newIntArray(arrayOffset(bitStreamText),
        this.bitStream, "bitStreamArray", null, arrayProps);

    // CodeBlock
    Text codeBlockText = lang.newText(textOffest(bitStreamText), "Codeblock: ",
        "codeBlockText", null, defaultProp);

    String[] str = new String[24];
    Arrays.fill(str, "  ");

    codeBlockArray = lang.newStringArray(arrayOffset(codeBlockText), str,
        "CodeBlockArray", null, arrayProps);

    paddingText = lang.newText(new Offset(20, 0, codeBlockArray, "E"), "",
        "paddingText", null, defaultProp);
    paddingText.hide();

    // Codiertabelle
    Text codeArrayText = lang.newText(textOffest(codeBlockText),
        "Codiertabelle: ", "codeArrayText", null, defaultProp);

    codeArray = lang.newStringArray(arrayOffset(codeArrayText),
        this.lookupTable, "codeArray", null, arrayProps);

    counter = lang.newCounter(codeBlockArray);

    // Berechnung und Ausgabe
    calcText = lang.newText(textOffest(codeArrayText), "Berechnung: ",
        "calcText", null, defaultProp);

    outputText = lang.newText(textOffest(calcText), "Ausgabe: ", "outputText",
        null, outputProp);

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
  private void clearCodeBlockArray() {
    counter.deactivateCounting();
    for (int i = 0; i < this.codeBlockArray.getLength(); i++) {
      this.codeBlockArray.put(i, "  ", null, null);
    }
    counter.activateCounting();
    paddingText.hide();
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
          .decode(String.valueOf(oneLine.charAt(0)));
      // checks that the second char of the line is a dot "."
      boolean secondIsADot = Character.toString(oneLine.charAt(1)).equals(".");
      int intention = ((valueOfFirstSymbol < 7) && (secondIsADot == true)) ? 1
          : 0;
      // at the line to the listening
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
    paddingText.hide();
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

    Font defaultFont = new Font("SansSerif", Font.PLAIN, 18);

    defaultProp = new TextProperties();
    defaultProp.set(AnimationPropertiesKeys.FONT_PROPERTY, defaultFont);

    outputProp = new TextProperties();
    outputProp.set(AnimationPropertiesKeys.FONT_PROPERTY,
        defaultFont.deriveFont(22F));

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

    header = lang.newText(new Coordinates(20, 30), "Base64 Encoder", "header",
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

      Font newFont = new Font("SansSerif", Font.PLAIN, 18);
      Set<String> propertyKeys = defaultProp.getAllPropertyNames();
      if (propertyKeys.contains("font") == true) {
        if (defaultProp.get("font") instanceof Font) {
          Font oldFont = (Font) defaultProp.get("font");
          newFont = oldFont.deriveFont(oldFont.getSize() + 4F);
        }
      }

      outputProp.set(AnimationPropertiesKeys.FONT_PROPERTY, newFont);

      this.headerProp = (TextProperties) props
          .getPropertiesByName("Überschrift");
      this.arrayProps = (ArrayProperties) props.getPropertiesByName("Array");
      this.scProps = (SourceCodeProperties) props
          .getPropertiesByName("Quelltext");
    }
  }

  /**
   * Encodes all 6Bit Groups of {@link #codeBlockArray} to Base64
   */
  private void encodeAllGroups() {
    for (int i = 0; i < 4; i++) {
      highlightNextCodeLine();
      encodeOneGroup(i);
    }
  }

  /**
   * Encodes one 6Bit Group of {@link #codeBlockArray} to Base64
   * 
   * @param groupNo
   *          the Group to encode. Must be between 0 and 3
   */
  private void encodeOneGroup(int groupNo) {

    StringArray source = this.codeBlockArray;

    int firstElementToEncode = 6 * groupNo;
    int lastElementToEncode = firstElementToEncode + 5;

    source.highlightCell(firstElementToEncode, lastElementToEncode, null, null);

    int valueOfGroup = 0;
    // Encode the int group to one int
    for (int i = firstElementToEncode; i <= lastElementToEncode; i++) {
      if (source.getData(i).equals("1")) {
        valueOfGroup |= 1 << (lastElementToEncode - i);
      }
    }

    codeArray.highlightCell(valueOfGroup, null, null);

    // Setzen der Berechnungszeile
    final String groupAsBin = String.format("%6s",
        Integer.toBinaryString(valueOfGroup)).replace(' ', '0');
    final String groupAsInt = Integer.toString(valueOfGroup);
    final String groupAsBase64 = codeArray.getData(valueOfGroup);

    String calcString = "%s entspricht %s und somit %s in der Codiertabelle.";
    calcString = String.format(calcString, groupAsBin, groupAsInt,
        groupAsBase64);

    setCalcText(calcString);

    lang.nextStep();

    String newOutputText = outputText.getText() + result + groupAsBase64;
    outputText.setText(newOutputText, null, null);

    lang.nextStep();

    source.highlightElem(firstElementToEncode, lastElementToEncode, null, null);
    unhighlightCells(source, firstElementToEncode, lastElementToEncode);
    codeArray.unhighlightCell(valueOfGroup, null, null);

    this.result = this.result + groupAsBase64;
  }

  /**
   * Tries to copy up to three Bytes from {@link #bitStream} to
   * {@link #codeBlockArray}
   * 
   * @param roundNumber
   *          the encoding round. e.G. 1 means a Offset of 24 Bit in
   *          {@link #bitStream}
   */

  private void copy3ByteGroup(int roundNumber) {
    IntArray from = this.bitStreamArray;
    StringArray to = this.codeBlockArray;
    // the first bit to copy out of the Stream
    int beginBitSource = roundNumber * 8 * 3;

    // the last bit to copy out of the Stream
    // copies at maximum 24 Bits from the beginning. But not more then the
    // stream has elements left
    int endBitSource = (from.getLength() > beginBitSource + 23) ? beginBitSource + 23
        : from.getLength() - 1;

    final int highestDestinationIndexToChange = endBitSource - beginBitSource;
    int numberOfBytesToCopie = (((highestDestinationIndexToChange + 1) / 8) % 4);
    // How much Bytes are missing to be x % 3 = 0
    this.numberOfPaddedBytes = 3 - numberOfBytesToCopie;

    // Highlight the Cells
    from.highlightCell(beginBitSource, endBitSource, null, null);
    to.highlightCell(0, highestDestinationIndexToChange, null, null);

    String calcString = "";

    if (numberOfBytesToCopie == 3) {
      calcString = "Im Bitstream sind noch ausreichend Bytes. Es werden 3 Bytes kopiert.";
    } else if (numberOfBytesToCopie == 2) {
      calcString = "Im Bitstream sind nur noch %s Bytes. Diese werden kopiert.";
      calcString = String.format(calcString, numberOfBytesToCopie);
    } else {
      calcString = "Im Bitstream ist nur noch %s Byte. Dieses wird kopiert.";
      calcString = String.format(calcString, numberOfBytesToCopie);
    }
    setCalcText(calcString);

    final String labelText = "Encodierung des %s. 3er Blocks";

    lang.nextStep(String.format(labelText, roundNumber + 1));

    // Copy the Data to the Destination.
    for (int i = 0; i <= highestDestinationIndexToChange; i++) {
      to.put(i, Integer.toString(from.getData(beginBitSource + i)), null, null);
    }

    lang.nextStep();

    // unhighlightCell work arrount!
    unhighlightCells(from, beginBitSource, endBitSource);
    unhighlightCells(to, 0, endBitSource);
    // from.unhighlightCell(offset, offset + end, null, null);
    // to.unhighlightCell(0, end, null, null);

    from.highlightElem(beginBitSource, endBitSource, null, null);

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
   * {@link IntArray#unhighlightCell(int, int, algoanim.util.Timing, algoanim.util.Timing)}
   * dose not what I think it should do. I write this workaround
   * 
   * @param target
   *          the {@link IntArray} to unhihglihtCells
   * @param begin
   *          the first Cell to unhighlight
   * @param end
   *          the last Cell to unhighlight
   */
  private void unhighlightCells(IntArray target, int begin, int end) {
    // Not working:
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
    lang = new AnimalScript("Base64 Encoder", "Claudius Kleemann", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    String Bitstream = (String) primitives.get("Bitstream");
    String LookupTabelString = (String) primitives.get("Codiertabelle");

    boolean ignoreAllStyle = (Boolean) primitives.get("IgnoreAllStyle");
    if (ignoreAllStyle == false) {
      this.props = props;
    }

    this.lookupTable = new String[LookupTabelString.length()];

    for (int i = 0; i < LookupTabelString.length(); i++) {
      this.lookupTable[i] = Character.toString(LookupTabelString.charAt(i));
    }

    int[] BitStreamArray = new int[Bitstream.length()];

    // translate the String in to a int array of 0 and 1s
    for (int i = 0; i < Bitstream.length(); i++) {
      if (Bitstream.charAt(i) == '0') {
        BitStreamArray[i] = 0;
      } else if (Bitstream.charAt(i) == '1') {
        BitStreamArray[i] = 1;
      }
    }

    encode(BitStreamArray);

    /*
     * codetabelle = (ArrayProperties) props
     * .getPropertiesByName("codetabelle");
     */

    return lang.toString();
  }

  public String getName() {
    return "Base64 Encoder";
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

    Language l = new AnimalScript("Base64 Encoder", "Claudius Kleemann", 640,
        480);
    Base64Encode s = new Base64Encode(l);

    int[] bitStream = { 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0 };

    /*
     * int[] bitStream = { 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1,
     * 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0 };
     */
    s.encode(bitStream);

    System.out.println(l);
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    String Bitstream = (String) arg1.get("Bitstream");
    String lookupTableString = (String) arg1.get("Codiertabelle");

    String ausgabe = "";
    // Wenn der Bistream nicht ein vielfaches von 8 ist sind nicht nur Bytes
    // enthalten
    final int bitsMoreThenFitInByte = Bitstream.length() % 8;
    if (bitsMoreThenFitInByte != 0) {
      ausgabe += "Der Base64 Encoder kann nur ganze Bytes encoden. "
          + "Bitte gib %s weitere Bits ein damit das Byte voll wird.%n";
      ausgabe = String.format(ausgabe, (8 - bitsMoreThenFitInByte));
    }
    // Wenn etwas anders als 0 oder 1 im String steht ist das auch nicht
    // gewollt.
    // Wir löschen alle 0 und 1 aus dem String. Wenn keine weiteren Zeiche
    // im String sind ist der String danach leer
    String worker = Bitstream.replace("0", "");
    worker = worker.replace("1", "");

    final int numberOfInvalidSymbols = worker.length();
    if (numberOfInvalidSymbols != 0) {
      ausgabe += "Der Base64 Encoder arbeitet nur 0 und 1. "
          + "Momentan sind %s andere Zeichen in deiner Eingabe enthalten.%n";
      ausgabe = String.format(ausgabe, numberOfInvalidSymbols);
    }

    final int lookupSize = lookupTableString.length();

    final int nedetSize = 64;
    if (lookupSize != nedetSize) {

      // Anzahl der Zeichen zu viel oder zu wenig
      int numberOfNedetChanges = Math.abs(lookupSize - nedetSize);

      // die lookupSize kann hier nur kleiner oder größer sein!
      final String actionToDo = (lookupSize < nedetSize) ? "hinzufügen"
          : "entfernen";

      ausgabe += "Der Base64 Encoder braucht exakt 64 Zeichen in der Codiertabelle. Bitte %d Zeichen %s.%n";
      ausgabe = String.format(ausgabe, numberOfNedetChanges, actionToDo);
    }

    // Wenn die Ausgabe nicht leer ist werfen wir die Exception
    if (ausgabe.equals("") == false) {
      throw new IllegalArgumentException(ausgabe);
    }

    return true;
  }
}
