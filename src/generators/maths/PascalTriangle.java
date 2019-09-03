package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.parser.InteractionFactory;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Jan Stolzenburg <jan.stolzenburg@arcor.de>
 */
public class PascalTriangle implements generators.framework.Generator {

  private Language language;

  private int[][] pascalTriangle;

  private int numberOfRows;

  private int numberOfStepwiseCalculatedRows;

  private SourceCode pascalCode;

  private TextProperties triangleProperties;

  private TextProperties triangleZeroProperties;

  private Text[][] triangleText;

  private Text[][] triangleZeros;

  private int windowSizeX = 1000;

  private int trianglePosX = 400;

  private int trianglePosY = 300;

  private int trianglePosNextColumnOffsetX = 60;

  private int trianglePosNextRowOffsetY = 50;

  private int arrowOffsetStartXLeft = 11;

  private int arrowOffsetStartXRight = 0;

  private int arrowOffsetStartY = 20;

  private int arrowOffsetEndXLeft = 4;

  private int arrowOffsetEndXRight = 8;

  private int arrowOffsetEndY = 3;

  private boolean useArrowhead;

  private boolean useCodeMarker;

  private String codeMarker = "-->";

  private Color zerosColor;

  private PolylineProperties arrowProperties;

  private Polyline[][] arrows;

  private Text[] highlightArrows;

  private int currentHighlight;
  
  private String answer;
  private InteractionFactory f;
  private FillInBlanksQuestionModel fib;

  private static final String[] pseudoCode = new String[] {
      "1. Das einzige Element der ersten Zeile ist die 1.",
      "2. Für die Konstruktion der zweiten Zeile denkt man sich jeweils eine 0 links und rechts neben die 1.",
      "3. Nun addiert man jeweils zwei benachbarte Zahlen und fügt das Ergebnis darunter in die Mitte ein.",
      "4. Man entfernt die gedachten Nullen wieder.",
      "5. Für die Konstruktion der nächsten Zeile denkt man sich jeweils eine 0 links und rechts außen neben die 1en.",
      "6. Nun addiert man jeweils zwei benachbarte Zahlen und fügt das Ergebnis darunter in die Mitte ein.",
      "7. Man entfernt die gedachten Nullen wieder.",
      "Die Anweisungen 5, 6 und 7 wiederholt man so lange, bis das Dreieck die gewünschte Größe hat.", };

  public PascalTriangle() {
    this(7, 4, false, true, Color.GRAY);
  }

  public PascalTriangle(int numberOfRows) {
    this(numberOfRows, 4, false, true, Color.GRAY);
  }

  public PascalTriangle(int numberOfRows, int numberOfStepwiseCalculatedRows) {
    this(numberOfRows, numberOfStepwiseCalculatedRows, false, true, Color.GRAY);
  }

  public PascalTriangle(int numberOfRows, int numberOfStepwiseCalculatedRows,
      boolean useArrowhead, boolean useCodeMarker, Color zerosColor) {
    int nrRows = numberOfRows, nrStepCR = numberOfStepwiseCalculatedRows;
    if (nrRows < 4) {
      nrRows = 4;
      System.err.println("The triangle must have at least four rows!");
    }
    if (nrRows > 14) {
      nrRows = 14;
      System.err.println("The triangle must have at most fourteen rows!");
    }
    if (nrStepCR < 3) {
      nrStepCR = 3;
      System.err
          .println("At least the first three rows must be calculated stepwise!");
    }
    if (nrStepCR > numberOfRows) {
      nrStepCR = numberOfRows;
      System.err
          .println("It is not possible to calculate more rows stepwise than we have rows at all!");
    }
    this.numberOfRows = nrRows;
    this.numberOfStepwiseCalculatedRows = nrStepCR;
    this.useArrowhead = useArrowhead;
    this.useCodeMarker = useCodeMarker;
    this.zerosColor = zerosColor;
  }

  public String generate() {
    this.language = new AnimalScript("Das Pascalsche Dreieck",
        "Jan Stolzenburg <jan.stolzenburg@arcor.de>", this.windowSizeX,
        trianglePosY + (this.trianglePosNextRowOffsetY * this.numberOfRows));
    this.language.setStepMode(true);
    
    this.language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    f = new InteractionFactory(this.language, "InteractionPatterns.xml");
	this.language.addQuestionGroup(new QuestionGroupModel("triangle", 2));
	
    this.makeAnimation();
    return this.language.toString();
  }

  private void makeAnimation() {  
    this.makeHeader();
    this.language.nextStep();
    
    // Frage nach Namensgebung
    MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel("Namensgebung");
    msq.setPrompt("Unter welchen Namen ist das Pascalsche Dreieck bekannt?");
    msq.addAnswer("Das pascalsche Dreieck", 1, "Diese Bezeichnung geht auf Blaise Pascal zur&uuml;ck");
    msq.addAnswer("Yang-Hui-Dreieck", 1, "Diese Bezeichnung geht auf Yang Hui (China) zur&uuml;ck");
    msq.addAnswer("Tartaglia-Dreieck", 1, "Diese Bezeichnung geht auf Nicolo Fontana Tartaglia (Italien) zur&uuml;ck");
    msq.addAnswer("Chayyam-Dreieck", 1, "Diese Bezeichnung geht auf Omar Chayyam (Iran) zur&uuml;ck");
    msq.addAnswer("Binominal-Dreieck", -1, "Das Dreieck ist zwar eine geometrische Darstellung des Binomialkoeffizienten jedoch gibt es die Bezeichnung des Binomaldreiecks nicht");
    this.language.addMSQuestion(msq);
    this.language.nextStep();
    
    this.makeCode();
    this.language.nextStep();
    this.makeTriangle();
    
    HtmlDocumentationModel link = new HtmlDocumentationModel("link");
	link.setLinkAddress("http://de.wikipedia.org/wiki/Pascalsches_Dreieck");
	this.language.addDocumentationLink(link);
	this.language.nextStep();
    
    this.language.finalizeGeneration();
  }

  private void makeHeader() {
    TextProperties titleProperties = new TextProperties();
    titleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    titleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    Text title = this.language
        .newText(new Coordinates(20, 30),
            "Das Pascalsche Dreieck - Konstruktion", "title", null,
            titleProperties);

    RectProperties titleBoxProperties = new RectProperties();
    titleBoxProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    titleBoxProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    titleBoxProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    titleBoxProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    this.language.newRect(new Offset(-5, -5, title, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, title, AnimalScript.DIRECTION_SE), "titleBox", null,
        titleBoxProperties);
  }

  private void makeCode() {
    SourceCodeProperties pascalCodeProperties = new SourceCodeProperties();
    pascalCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);
    pascalCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    pascalCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    this.pascalCode = this.language.newSourceCode(new Coordinates(30, 80),
        "Pascal Pseude Code", null, pascalCodeProperties);
    this.highlightArrows = new Text[pseudoCode.length];
    if (!this.useCodeMarker)
      this.codeMarker = "";
    for (int i = 0; i < pseudoCode.length; i++) {
      this.pascalCode.addCodeLine(pseudoCode[i], "line" + String.valueOf(i), 0,
          null);
      this.highlightArrows[i] = this.language.newText(new Coordinates(5,
          94 + (20 * i)), this.codeMarker, "arrowCode" + String.valueOf(i),
          new Hidden());
    }
  }

  private void makeTriangle() {
    this.pascalTriangle = new int[this.numberOfRows][this.numberOfRows];
    this.calcPascalTriangle();
    this.prepareTriangle();
    this.makeFirstRow();
    this.language.nextStep();
    this.makeSecondRow();
    this.language.nextStep();
    this.makeThirdRow();
    this.language.nextStep();
    int currentRow = 4;
    
    while (currentRow <= this.numberOfRows) {
      this.makeNextRow(currentRow);
      this.language.nextStep();
      currentRow += 1;
    }
    this.finalizeTriangle();
  }

  private void prepareTriangle() {
    this.triangleProperties = new TextProperties();
    this.triangleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);
    this.triangleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.BOLD, 18));
    this.triangleZeroProperties = new TextProperties();
    this.triangleZeroProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        this.zerosColor);
    this.triangleZeroProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 18));
    this.triangleText = new Text[this.numberOfRows][this.numberOfRows];
    this.triangleZeros = new Text[this.numberOfRows - 1][2];
    this.arrowProperties = new PolylineProperties();
    this.arrowProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY,
        this.useArrowhead);
    this.arrows = new Polyline[this.numberOfRows - 1][this.numberOfRows * 2];
  }

  private void makeFirstRow() {
    this.highlight(0);
    this.language.newText(new Coordinates(0, 0), "FailureWorkaround",
        "FailureWorkaround", new Hidden(), this.triangleProperties); // A
                                                                      // workaround
                                                                      // for a
                                                                      // failure:
                                                                      // the
                                                                      // first
                                                                      // text
                                                                      // after a
                                                                      // chanche
                                                                      // of
                                                                      // textsize
                                                                      // has a
                                                                      // different
                                                                      // baseline
                                                                      // than
                                                                      // following
                                                                      // texts
                                                                      // of the
                                                                      // same
                                                                      // size.
    this.triangleText[0][0] = this.language.newText(new Coordinates(
        this.trianglePosX, this.trianglePosY), "1", "Row0Column1", null,
        this.triangleProperties);
  }

  private void makeSecondRow() {
    this.highlight(1);
    this.triangleZeros[0][0] = this.language.newText(new Coordinates(
        this.trianglePosX - this.trianglePosNextColumnOffsetX,
        this.trianglePosY), "0", "Row0ColumnFirst", null,
        this.triangleZeroProperties);
    this.triangleZeros[0][1] = this.language.newText(new Coordinates(
        this.trianglePosX + this.trianglePosNextColumnOffsetX,
        this.trianglePosY), "0", "Row0ColumnLast", null,
        this.triangleZeroProperties);
    this.language.nextStep();
    this.highlight(2);
    this.makeArrows(1, 0);
    this.language.nextStep();
    this.triangleText[1][0] = this.language.newText(new Coordinates(
        this.trianglePosX - (this.trianglePosNextColumnOffsetX / 2),
        this.trianglePosY + this.trianglePosNextRowOffsetY), "1",
        "Row1Column1", null, this.triangleProperties);
    this.language.nextStep();
    this.makeArrows(1, 1);
    this.language.nextStep();
    this.triangleText[1][1] = this.language.newText(new Coordinates(
        this.trianglePosX + (this.trianglePosNextColumnOffsetX / 2),
        this.trianglePosY + this.trianglePosNextRowOffsetY), "1",
        "Row1Column2", null, this.triangleProperties);
    this.language.nextStep();
    this.hideArrows(1, 0);
    this.hideArrows(1, 1);
    this.language.nextStep();
    this.highlight(3);
    this.hideZeros(0);
  }

  private void makeThirdRow() {
    this.highlight(4);
    this.triangleZeros[1][0] = this.language.newText(new Coordinates(
        this.trianglePosX - ((this.trianglePosNextColumnOffsetX / 2) * 3),
        this.trianglePosY + this.trianglePosNextRowOffsetY), "0",
        "Row1ColumnFirst", null, this.triangleZeroProperties);
    this.triangleZeros[1][1] = this.language.newText(new Coordinates(
        this.trianglePosX + ((this.trianglePosNextColumnOffsetX / 2) * 3),
        this.trianglePosY + this.trianglePosNextRowOffsetY), "0",
        "Row1ColumnLast", null, this.triangleZeroProperties);
    this.language.nextStep();
    this.highlight(5);
    this.makeArrows(2, 0);
    this.language.nextStep();
    this.triangleText[2][0] = this.language.newText(new Coordinates(
        this.trianglePosX + ((this.trianglePosNextColumnOffsetX / 2) * -2),
        this.trianglePosY + (this.trianglePosNextRowOffsetY * 2)), "1",
        "Row2Column1", null, this.triangleProperties);
    this.language.nextStep();
    this.makeArrows(2, 1);
    this.language.nextStep();
    this.triangleText[2][1] = this.language.newText(new Coordinates(
        this.trianglePosX + ((this.trianglePosNextColumnOffsetX / 2) * 0),
        this.trianglePosY + (this.trianglePosNextRowOffsetY * 2)), "2",
        "Row2Column2", null, this.triangleProperties);
    this.language.nextStep();
    this.makeArrows(2, 2);
    this.language.nextStep();
    this.triangleText[2][2] = this.language.newText(new Coordinates(
        this.trianglePosX + ((this.trianglePosNextColumnOffsetX / 2) * 2),
        this.trianglePosY + (this.trianglePosNextRowOffsetY * 2)), "1",
        "Row2Column3", null, this.triangleProperties);
    this.language.nextStep();
    this.hideArrows(2, 0);
    this.hideArrows(2, 1);
    this.hideArrows(2, 2);
    this.language.nextStep();
    this.highlight(6);
    this.hideZeros(1);
  }

  /**
   * This method works for currentRow > 1
   */
  private void makeNextRow(int currentRow) {
    int rowIndex = currentRow - 1;
    this.makeTriangleZeros(rowIndex - 1);
    this.language.nextStep();
    this.highlight(5);
    if (this.numberOfStepwiseCalculatedRows >= currentRow) {
      fib = f.generateFIBQuestion("nextRowPascalTriangle", "nextRow" + currentRow);
        
      answer = "";
      for (int i = 0; i < currentRow; i++) {
        this.makeArrows(rowIndex, i);
        this.language.nextStep();
        this.triangleText[rowIndex][i] = this.language.newText(new Coordinates(
            this.calcPositionX(rowIndex, i), this.calcPositionY(rowIndex, i)),
            String.valueOf(this.pascalTriangle[rowIndex][i]), "Row"
                + String.valueOf(rowIndex) + "Column" + String.valueOf(i + 1),
            null, this.triangleProperties);
        
        if(i+1 == currentRow) {
        	answer += String.valueOf(this.pascalTriangle[rowIndex][i]);
        } else {
        	answer += String.valueOf(this.pascalTriangle[rowIndex][i]) + ", ";
        }
        this.language.nextStep();
      }
      
      fib.addAnswer(answer, 2, "Die richtige Antwort lautet: " + answer);
      
    } else {
      fib = f.generateFIBQuestion("nextRowPascalTriangle", "nextRow" + currentRow);
        
      for (int i = 0; i < currentRow; i++)
        this.makeArrows(rowIndex, i);
      this.language.nextStep();
      
      answer = "";
      for (int i = 0; i < currentRow; i++) {
        this.triangleText[rowIndex][i] = this.language.newText(new Coordinates(
            this.calcPositionX(rowIndex, i), this.calcPositionY(rowIndex, i)),
            String.valueOf(this.pascalTriangle[rowIndex][i]), "Row"
                + String.valueOf(rowIndex) + "Column" + String.valueOf(i + 1),
            null, this.triangleProperties);
        if(i+1 == currentRow) {
        	answer += String.valueOf(this.pascalTriangle[rowIndex][i]);
        } else {
        	answer += String.valueOf(this.pascalTriangle[rowIndex][i]) + ", ";
        }
        this.language.nextStep();
      }
      fib.addAnswer(answer, 2, "Die richtige Antwort lautet: " + answer);
    }
    for (int i = 0; i < currentRow; i++)
      this.hideArrows(rowIndex, i);
    this.language.nextStep();
    this.highlight(6);
    this.hideZeros(rowIndex - 1);
  }

  private void makeTriangleZeros(int rowIndex) {
    this.highlight(4);
    this.triangleZeros[rowIndex][0] = this.language.newText(new Coordinates(
        this.calcPositionX(rowIndex, -1), this.calcPositionY(rowIndex, -1)),
        "0", "Row" + String.valueOf(rowIndex) + "ColumnFirst", null,
        this.triangleZeroProperties);
    this.triangleZeros[rowIndex][1] = this.language.newText(new Coordinates(
        this.calcPositionX(rowIndex, rowIndex + 1), this.calcPositionY(
            rowIndex, rowIndex + 1)), "0", "Row" + String.valueOf(rowIndex)
        + "ColumnLast", null, this.triangleZeroProperties);
  }

  
//  // Perhaps someone will need this code some day. It is a idea of how to place
//  // the text with offsets and not absolute coordinates.
//  private void makeTriangleText(int rowIndex) {
    // int medium = rowIndex / 2;
    // Text middle;
    // Text leftmost;
    // Text rightmost;
    // if ((rowIndex % 2) == 0) { //A row with an odd number of elements.
    // //Write the element in the middle of the row.
    // middle = this.triangleText[rowIndex][medium] = this.language.newText(new
    // Offset(0, this.trianglePosNextRowOffsetY, this.positioningHelp[medium],
    // AnimalScript.DIRECTION_S),
    // String.valueOf(this.pascalTriangle[rowIndex][medium + 1]), "Row" +
    // String.valueOf(rowIndex) + "Column" + String.valueOf(medium), new
    // Hidden(), this.triangleProperties);
    // }
    // else { //A row with an even number of elements.
    // //Write a zero in the middle of the row. This helps us to keep some
    // distance between the elements.
    // middle = this.positioningHelp[medium] = this.language.newText(new
    // Offset(0, this.trianglePosNextRowOffsetY,
    // this.triangleText[rowIndex][medium], AnimalScript.DIRECTION_S), "0",
    // "positioningHelpRow" + String.valueOf(rowIndex), new Hidden(),
    // this.triangleProperties);
    // }
    // leftmost = middle;
    // rightmost = middle;
    // for(int i = medium - 1; i >= 0; i--) {
    // leftmost = this.triangleText[rowIndex][i] = this.language.newText(new
    // Offset(-this.trianglePosNextColumnOffsetX, 0, leftmost,
    // AnimalScript.DIRECTION_NW),
    // String.valueOf(this.pascalTriangle[rowIndex][i]), "Row" +
    // String.valueOf(rowIndex) + "Column" + String.valueOf(i + 1), new
    // Hidden(), this.triangleProperties);
    // }
    // for(int i = medium + 1; i <= rowIndex; i++) {
    // rightmost = this.triangleText[rowIndex][i] = this.language.newText(new
    // Offset(+this.trianglePosNextColumnOffsetX, 0, rightmost,
    // AnimalScript.DIRECTION_NE),
    // String.valueOf(this.pascalTriangle[rowIndex][i]), "Row" +
    // String.valueOf(rowIndex) + "Column" + String.valueOf(i + 1), new
    // Hidden(), this.triangleProperties);
    // }
//  }

  private int calcPositionX(int rowIndex, int entryIndex) {
    return (this.trianglePosX + ((this.trianglePosNextColumnOffsetX / 2) * ((entryIndex * 2) - rowIndex)));
  }

  private int calcPositionY(int rowIndex, int entryIndex) {
    return (this.trianglePosY + (this.trianglePosNextRowOffsetY * rowIndex));
  }

  /**
   * @param rowIndex
   *          the row of the number that is calculated. Starts with zero.
   *          Smallest useful value is one. (Means: Arrows to the second line)
   * @param entryIndex
   *          the index of the number that is calculated. Starts with zero.
   */
  private void makeArrows(int rowIndex, int entryIndex) {
    Node startLeft, endLeft, startRight, endRight;
    startLeft = new Coordinates(this.arrowOffsetStartXLeft
        + this.calcPositionX(rowIndex - 1, entryIndex - 1),
        this.arrowOffsetStartY
            + this.calcPositionY(rowIndex - 1, entryIndex - 1));
    startRight = new Coordinates(this.arrowOffsetStartXRight
        + this.calcPositionX(rowIndex - 1, entryIndex), this.arrowOffsetStartY
        + this.calcPositionY(rowIndex - 1, entryIndex));
    endLeft = new Coordinates(this.arrowOffsetEndXLeft
        + this.calcPositionX(rowIndex, entryIndex), this.arrowOffsetEndY
        + this.calcPositionY(rowIndex, entryIndex));
    endRight = new Coordinates(this.arrowOffsetEndXRight
        + this.calcPositionX(rowIndex, entryIndex), this.arrowOffsetEndY
        + this.calcPositionY(rowIndex, entryIndex));

    Node[] arrowPointsLeft = new Node[] { startLeft, endLeft };
    Node[] arrowPointsRight = new Node[] { startRight, endRight };
    this.arrows[rowIndex - 1][entryIndex * 2] = this.language.newPolyline(
        arrowPointsLeft, "arrowLeftForRow" + String.valueOf(rowIndex)
            + "Column" + String.valueOf(entryIndex + 1), null,
        this.arrowProperties);
    this.arrows[rowIndex - 1][(entryIndex * 2) + 1] = this.language
        .newPolyline(arrowPointsRight, "arrowRightForRow"
            + String.valueOf(rowIndex) + "Column"
            + String.valueOf(entryIndex + 1), null, this.arrowProperties);
  }

  private void hideArrows(int rowIndex, int entryIndex) {
    this.arrows[rowIndex - 1][entryIndex * 2].hide();
    this.arrows[rowIndex - 1][(entryIndex * 2) + 1].hide();
  }

  private void hideZeros(int rowIndex) {
    this.triangleZeros[rowIndex][0].hide();
    this.triangleZeros[rowIndex][1].hide();
  }

  private void highlight(int rowIndex) {
    this.pascalCode.unhighlight(this.currentHighlight);
    this.pascalCode.highlight(rowIndex);
    this.highlightArrows[this.currentHighlight].hide();
    this.highlightArrows[rowIndex].show();
    this.currentHighlight = rowIndex;
    this.language.nextStep();
  }

  
//  // Just for debugging purpose.
//  private void makeWindowBorder() {
//    RectProperties titleBoxProperties = new RectProperties();
//    titleBoxProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
//    titleBoxProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
//    this.language.newRect(new Coordinates(0, 0), new Coordinates(
//        this.windowSizeX, trianglePosY
//            + (this.trianglePosNextRowOffsetY * this.numberOfRows)),
//        "titleBox", null, titleBoxProperties);
//  }

  private void finalizeTriangle() {
    // Nothing yet
  }

  private void calcPascalTriangle() {
    int[][] calcTriangle = new int[this.numberOfRows][this.numberOfRows + 1]; // We
                                                                              // use
                                                                              // the
                                                                              // zeros
                                                                              // from
                                                                              // the
                                                                              // initialisation
                                                                              // for
                                                                              // the
                                                                              // calculation.
                                                                              // In
                                                                              // the
                                                                              // last
                                                                              // row,
                                                                              // we
                                                                              // need
                                                                              // one
                                                                              // more
                                                                              // column
                                                                              // than
                                                                              // we
                                                                              // have
                                                                              // rows
                                                                              // because
                                                                              // of
                                                                              // these
                                                                              // zeros.
    calcTriangle[0][1] = 1;
    for (int i = 1; i < this.numberOfRows; i++)
      for (int j = 1; j <= (i + 1); j++)
        // The first column consists of zeros that we use for the calculation.
        calcTriangle[i][j] = calcTriangle[i - 1][j - 1]
            + calcTriangle[i - 1][j];
    for (int i = 0; i < this.numberOfRows; i++)
      for (int j = 0; j <= i; j++)
        this.pascalTriangle[i][j] = calcTriangle[i][j + 1];
  }

  public String generate(AnimationPropertiesContainer properties,
      Hashtable<String, Object> primitives) {
    String nameNumberOfRows = "Anzahl der Zeilen (mindestens 4)";
    String nameNumberOfStepwiseCalculatedRows = "Anzahl der Zeilen, deren Werte einzeln berechnet werden (mindestens 3)";
    String nameUseArrowhead = "Pfeilspitzen anzeigen?";
    String nameUseCodeMarker = "Die aktuelle Codezeile mit einem Pfeil markieren?";
    String nameZerosColor = "Farbe der Hilfsnullen?";
    if (primitives.containsKey(nameNumberOfRows)
        && (primitives.get(nameNumberOfRows) != null)) {
      this.numberOfRows = (Integer) primitives.get(nameNumberOfRows);
      if (this.numberOfRows < 4) {
        this.numberOfRows = 4;
        System.err.println("The triangle must have at least four rows!");
      }
      if (this.numberOfRows > 14) {
        this.numberOfRows = 14;
        System.err.println("The triangle must have at most fourteen rows!");
      }
    }
    if (primitives.containsKey(nameNumberOfStepwiseCalculatedRows)
        && (primitives.get(nameNumberOfStepwiseCalculatedRows) != null)) {
      this.numberOfStepwiseCalculatedRows = (Integer) primitives
          .get(nameNumberOfStepwiseCalculatedRows);
      if (this.numberOfStepwiseCalculatedRows < 3) {
        this.numberOfStepwiseCalculatedRows = 3;
        System.err
            .println("At least the first three rows must be calculated stepwise!");
      }
      if (this.numberOfStepwiseCalculatedRows > this.numberOfRows) {
        this.numberOfStepwiseCalculatedRows = this.numberOfRows;
        System.err
            .println("It is not possible to calculate more rows stepwise than we have rows at all!");
      }
    }
    if (primitives.containsKey(nameUseArrowhead)
        && (primitives.get(nameUseArrowhead) != null)) {
      this.useArrowhead = (Boolean) primitives.get(nameUseArrowhead);
    }
    if (primitives.containsKey(nameUseCodeMarker)
        && (primitives.get(nameUseCodeMarker) != null)) {
      this.useCodeMarker = (Boolean) primitives.get(nameUseCodeMarker);
    }
    if (primitives.containsKey(nameZerosColor)
        && (primitives.get(nameZerosColor) != null)) {
      this.zerosColor = (Color) primitives.get(nameZerosColor);
    }
    return this.generate();
  }

  public String getCodeExample() {
    String result = "";
    for (String lineOfCode : pseudoCode)
      result += (lineOfCode + "\n");
    return result;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Zeigt schrittweise die Konstruktion des Pascalschen Dreiecks.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getName() {
    return "Die Konstruktion des Pascalschen Dreiecks";
  }

  public String getAlgorithmName() {
    return "Pascalsches Dreieck";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public String getAnimationAuthor() {
    return "Jan Stolzenburg";
  }
  public void init() {
    // nothing to be done here
  }

}
