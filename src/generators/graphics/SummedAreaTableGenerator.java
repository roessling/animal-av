package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.helpers.AnimalUtilities;
import generators.graphics.helpers.CustomIntMatrixGenerator;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Offset;

/**
 * Generator for the Summed Area Tables.
 */
public class SummedAreaTableGenerator implements Generator {

  // public static final String animationAuthor = "Roman Uhlig, Michael Rau";

  // the AnimalScript language
  private Language        language;

  // the animation parameters
  private int             Texture_Width;
  private int             Texture_Height;
  private int[][]         Texture;

  // various colors
  private Color           Highlight_Color_1           = Color.RED;
  private Color           Highlight_Color_2           = Color.BLUE;
  private Color           Highlight_Color_3           = Color.GREEN;
  private Color           Highlight_Color_4           = Color.ORANGE;
  private Color           Line_Color                  = Color.GRAY;
  private Color           Cell_Color                  = Color.WHITE;
  private Color           Text_Color                  = Color.BLACK;
  private Color           Pseudocode_Text_Color       = Color.BLACK;
  private Color           Pseudocode_Background_Color = Color.GRAY;
  private Color           Pseudocode_Highlight_Color  = Color.RED;
  private Color           Banner_Text_Color           = Color.BLACK;
  private Color           Equation_Background_Color   = Color.GRAY;
  private Color           Counter_Color               = Color.GREEN;

  // the animation elements
  private IntMatrix       textureGrid, summedTextureGrid;
  private Text            textureGridDescription, summedTextureGridDescription;
  private TwoValueCounter textureCounter, summedTextureCounter;
  private TwoValueView    textureCounterView, summedTextureCounterView;

  // the pseudo code
  private SourceCode      pseudoCode;
  private Rect            pseudoCodeBox;

  // the equation text primitives
  private Text            equationUpperSAT, equationUpperFirstSummand,
      equationUpperSecondSummand, equationUpperThirdSummand,
      equationUpperFourthSummand, equationLowerSAT, equationLowerFirstSummand,
      equationLowerSecondSummand, equationLowerThirdSummand,
      equationLowerFourthSummand, equationResult;
  private Rect            equationBox;

  // component toggle possibilities
  private boolean         Display_Counters            = true;
  private boolean         Display_Equation            = true;

  // the number of Questions as chosen by the user
  private int             numberOfQuestions           = 3;

  /**
   * Sets the AnimalScript language.
   */
  public void init() {
    language = new AnimalScript("Summed Area Tables",
        "Roman Uhlig, Michael Rau", 800, 600);
    language.setStepMode(true);
  }

  /**
   * Generates the animation.
   * 
   * @param props
   *          the AnimationProperties
   * @param primitives
   *          the primitive settings
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    if (primitives != null) {
      Texture = (int[][]) primitives.get("Texture");
      Texture_Height = Texture.length;
      Texture_Width = Texture[0].length;
      Highlight_Color_1 = (Color) primitives.get("Highlight_Color_1");
      Highlight_Color_2 = (Color) primitives.get("Highlight_Color_2");
      Highlight_Color_3 = (Color) primitives.get("Highlight_Color_3");
      Highlight_Color_4 = (Color) primitives.get("Highlight_Color_4");
      Line_Color = (Color) primitives.get("Line_Color");
      Cell_Color = (Color) primitives.get("Cell_Color");
      Text_Color = (Color) primitives.get("Text_Color");
      Pseudocode_Text_Color = (Color) primitives.get("Pseudocode_Text_Color");
      Pseudocode_Background_Color = (Color) primitives
          .get("Pseudocode_Background_Color");
      Pseudocode_Highlight_Color = (Color) primitives
          .get("Pseudocode_Highlight_Color");
      Banner_Text_Color = (Color) primitives.get("Banner_Text_Color");
      Display_Equation = (Boolean) primitives.get("Display_Equation");
      Equation_Background_Color = (Color) primitives
          .get("Equation_Background_Color");
      Display_Counters = (Boolean) primitives.get("Display_Counters");
      Counter_Color = (Color) primitives.get("Counter_Color");
      numberOfQuestions = (Integer) primitives.get("Number_of_questions");
    }

    // create the script itself
    createInitialDescription();
    createSummedAreaTable();
    showSummedAreaTableUsage();
    createFinalDescription();

    language.finalizeGeneration();

    // remove every occurrence of the "refresh" command from the script because
    // of major layouting problems with
    // refresh
    String finalStringWithoutRefresh = language.toString().replaceAll(
        "refresh", "");

    return finalStringWithoutRefresh;
  }

  public String getName() {
    return "Summed Area Tables";
  }

  public String getAlgorithmName() {
    return "Summed Area Tables";
  }

  public String getAnimationAuthor() {
    return "Roman Uhlig, Michael Rau";
  }

  public String getDescription() {
    return "Eine <i>Summed Area Table</i> (auch Integralbild genannt) ist ein Algorithmus, der schnelles und effizientes Aufsummieren von Werten in rechteckigen Ausschnitten eines Werterasters ermöglicht. Konkrete Anwendung finden Summed Area Tables in der Computergrafik, wo Texturen zum Vermeiden von Abtastfehlern, die beim Skalieren auftreten, im Voraus transformiert werden.<br>Vorgestellt wurden Summed Area Tables bereits im Jahre 1984 von Frank Crow, der erste praktische Einsatz erfolgte aber erst im Jahr 2002.<br><br>Im Vergleich zum alternativen Verfahren der Mipmap kann die Summed Area Table Darstellung einer Textur mehr Speicherplatz einsparen und mehr Auflösungen unterstützen. Das Speichern der mitunter großen Summenwerte erfordert aber, dass entsprechend breite Datentypen zur Verfügung stehen, die die Bitbreite der eigentlichen Textur übersteigen. Aus diesem Grund werden in moderner Grafikhardware zumeist Mipmaps verwendet.";
  }

  public String getCodeExample() {
    return "Prozedur ErstelleSummedAreaTable()\n     F&uuml;r jede Zeile\n         F&uuml;r jede Spalte\n             SAT(Zeile, Spalte) =  TEX(Zeile, Spalte) \n                                 + SAT(vorige Zeile, Spalte) \n                                 + SAT(Zeile, vorige Spalte) \n                                 - SAT(vorige Zeile, vorige Spalte)\n \n Prozedur SummiereRechteck(Punkt linksOben, Punkt rechtsUnten) gibt Zahl zur&uuml;ck\n     Summe =   SAT(rechtsUnten.Zeile, rechtsUnten.Spalte)\n             + SAT(linksOben.Zeile, linksOben.Spalte)\n             - SAT(linksOben.Zeile, rechtUnten.Spalte)\n             - SAT(rechtsUnten.Zeile, linksOben.Zeile)\n     Gib Summe zur&uuml;ck";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * Create the initial description part of the animation (animation header,
   * description text).
   */
  private void createInitialDescription() {
    // create the algorithm banner
    language.addLine("text \"headerAlgo\" \""
        + " Summed Area Tables\" at (30, 50) color "
        + AnimalUtilities.colorToString(Banner_Text_Color)
        + " font SansSerif size 50 bold depth 2");

    // the description code which is displayed in the beginning
    String[] initialDescriptionText = {
        "Eine Summed Area Table (auch Integralbild genannt)",
        "ist ein Algorithmus, der schnelles und effizientes",
        "Aufsummieren von Werten in rechteckigen Ausschnitten",
        "eines Werterasters ermöglicht. Konkrete Anwendung finden",
        "Summed Area Tables in der Computergrafik, wo Texturen",
        "zum Vermeiden von Abtastfehlern, die beim Skalieren auftreten,",
        "im Voraus transformiert werden.",
        "Vorgestellt wurden Summed Area Tables bereits im Jahre",
        "1984 von Frank Crow, der erste praktische Einsatz erfolgte",
        "aber erst im Jahr 2002.", "",
        "Im Vergleich zum alternativen Verfahren der Mipmap kann die",
        "Summed Area Table Darstellung einer Textur mehr",
        "Speicherplatz einsparen und mehr Auflösungen unterstützen.",
        "Das Speichern der mitunter großen Summenwerte erfordert",
        "aber, dass entsprechend breite Datentypen zur Verfügung",
        "stehen, die die Bitbreite der eigentlichen Textur",
        "übersteigen. Aus diesem Grund werden in moderner",
        "Grafikhardware zumeist Mipmaps verwendet." };

    // set up the description codeGroup
    SourceCodeProperties initialDescriptionProp = new SourceCodeProperties();
    initialDescriptionProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    SourceCode initialDescription = language.newSourceCode(new Coordinates(80,
        125), "initialDescription", null, initialDescriptionProp);
    for (int i = 0; i < initialDescriptionText.length; i++) {
      initialDescription.addCodeLine(initialDescriptionText[i], null, 0, null);
    }
    language.nextStep("Beschreibung der Summed Area Tables");

    // hide the description in the next step
    initialDescription.hide();
  }

  private void createSummedAreaTable() {
    // create the custom generator and set up the two texture grids
    CustomIntMatrixGenerator generator = new CustomIntMatrixGenerator(
        (AnimalScript) language, Line_Color, Cell_Color, Text_Color);
    textureGrid = new IntMatrix(generator, new Coordinates(50, 125),
        new int[Texture_Height][Texture_Width], "textureGrid", null,
        new MatrixProperties());
    summedTextureGrid = new IntMatrix(generator, new Offset(50, 0, textureGrid,
        AnimalScript.DIRECTION_NE), new int[Texture_Height][Texture_Width],
        "summedTextureGrid", null, new MatrixProperties());

    // fill the textureGrid with the texture values from the generator dialog
    for (int row = 0; row < Texture_Height; row++) {
      for (int column = 0; column < Texture_Width; column++) {
        textureGrid.put(row, column, Texture[row][column], null, null);
        language.addLine(AnimalUtilities.setGridColor(summedTextureGrid, row,
            column, null, AnimalUtilities.colorToString(Color.GRAY), null,
            null, null));
      }
    }

    // create a description text for each one of the grids
    TextProperties textProp = new TextProperties();
    textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    textureGridDescription = language.newText(new Offset(0, -52, textureGrid,
        AnimalScript.DIRECTION_NW), "Texture (TEX)", "textureDescription",
        null, textProp);
    summedTextureGridDescription = language.newText(new Offset(0, -24,
        summedTextureGrid, AnimalScript.DIRECTION_NW),
        "Summed Area Table (SAT)", "summedTextureDescription", null, textProp);

    // set up the counters AFTER initializing the textures to avoid wrong
    // displaying wrong texture assignments
    if (Display_Counters) {
      CounterProperties counterProp = new CounterProperties();
      counterProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      counterProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Counter_Color);
      textureCounter = language.newCounter(textureGrid);
      textureCounterView = language.newCounterView(textureCounter, new Offset(
          0, 0, textureGrid, AnimalScript.DIRECTION_SW), counterProp, true,
          true);
      summedTextureCounter = language.newCounter(summedTextureGrid);
      summedTextureCounterView = language.newCounterView(summedTextureCounter,
          new Offset(0, 5, summedTextureGrid, AnimalScript.DIRECTION_SW),
          counterProp, true, true);
    }

    // set up the summands of the upper equation and hide them directly
    TextProperties equationProp = new TextProperties();
    equationProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 16));
    equationUpperSAT = language.newText(new Offset(50, Display_Counters ? 60
        : 30, textureGrid, AnimalScript.DIRECTION_SW), "SAT[0][0] = ",
        "equationUpperSAT", new Hidden(), equationProp);
    equationUpperFirstSummand = language.newText(new Offset(0, 0,
        equationUpperSAT, AnimalScript.DIRECTION_NE), "TEX[0][0]",
        "equationUpperFirstSummand", new Hidden(), equationProp);
    equationUpperFirstSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
        Highlight_Color_1, null, null);
    equationUpperSecondSummand = language.newText(new Offset(0, 0,
        equationUpperFirstSummand, AnimalScript.DIRECTION_NE),
        " + SAT[0][0 - 1]", "equationUpperSecondSummand", new Hidden(),
        equationProp);
    equationUpperSecondSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
        Highlight_Color_2, null, null);
    equationUpperThirdSummand = language.newText(new Offset(0, 0,
        equationUpperSecondSummand, AnimalScript.DIRECTION_NE),
        " + SAT[0 - 1][0]", "equationUpperThirdSummand", new Hidden(),
        equationProp);
    equationUpperThirdSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
        Highlight_Color_3, null, null);
    equationUpperFourthSummand = language.newText(new Offset(0, 0,
        equationUpperThirdSummand, AnimalScript.DIRECTION_NE),
        " - SAT[0 - 1][0 - 1]", "equationUpperFourthSummand", new Hidden(),
        equationProp);
    equationUpperFourthSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
        Highlight_Color_4, null, null);

    // set up the summands of the lower equation and hide them directly
    equationLowerSAT = language.newText(new Offset(0, 8, equationUpperSAT,
        AnimalScript.DIRECTION_SW), "SAT[0][0] = ", "equationLowerSAT",
        new Hidden(), equationProp);
    equationLowerFirstSummand = language.newText(new Offset(35, 0,
        equationLowerSAT, AnimalScript.DIRECTION_NE), "",
        "equationLowerFirstSummand", new Hidden(), equationProp);
    equationLowerSecondSummand = language.newText(new Offset(120, 0,
        equationLowerSAT, AnimalScript.DIRECTION_NE), "",
        "equationLowerSecondSummand", new Hidden(), equationProp);
    equationLowerThirdSummand = language.newText(new Offset(225, 0,
        equationLowerSAT, AnimalScript.DIRECTION_NE), "",
        "equationLowerThirdSummand", new Hidden(), equationProp);
    equationLowerFourthSummand = language.newText(new Offset(330, 0,
        equationLowerSAT, AnimalScript.DIRECTION_NE), "",
        "equationLowerFourthSummand", new Hidden(), equationProp);

    // set up the result text and hide it directly
    equationResult = language.newText(new Offset(420, 0, equationLowerSAT,
        AnimalScript.DIRECTION_NE), "", "equationResult", new Hidden(),
        equationProp);

    RectProperties equationBoxProp = new RectProperties();
    equationBoxProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Equation_Background_Color);
    equationBoxProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    equationBoxProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    equationBox = language.newRect(new Offset(-8, -2, equationUpperSAT,
        AnimalScript.DIRECTION_NW), new Offset(56, 6, equationResult,
        AnimalScript.DIRECTION_SE), "equationBox", new Hidden(),
        equationBoxProp);

    // set up the pseudo code codeGroup
    SourceCodeProperties pseudoCodeProp = new SourceCodeProperties();
    pseudoCodeProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Pseudocode_Text_Color);
    pseudoCodeProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Pseudocode_Highlight_Color);
    pseudoCodeProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));

    Offset pseudoCodeOffset = Display_Equation ? new Offset(-36, 12,
        equationLowerSAT, AnimalScript.DIRECTION_SW) : new Offset(16, 48,
        textureGrid, AnimalScript.DIRECTION_SW);
    pseudoCode = language.newSourceCode(pseudoCodeOffset, "pseudoCode", null,
        pseudoCodeProp);

    // create and add Code lines for pseudo code
    String[] pseudoCodeLines = {
        "",
        "Prozedur ErstelleSummedAreaTable()",
        "    Für jede Zeile",
        "        Für jede Spalte",
        "            SAT(Zeile, Spalte) =  TEX(Zeile, Spalte)",
        "                                + SAT(vorige Zeile, Spalte)",
        "                                + SAT(Zeile, vorige Spalte)",
        "                                - SAT(vorige Zeile, vorige Spalte)",
        "",
        "Prozedur SummiereRechteck(Punkt linksOben, Punkt rechtsUnten) gibt Zahl zurück",
        "    Summe =   SAT(rechtsUnten.Zeile, linksOben.Spalte)",
        "            + SAT(linksOben.Zeile, linksOben.Spalte)",
        "            - SAT(linksOben.Zeile, rechtUnten.Spalte)",
        "            - SAT(rechtsUnten.Zeile, linksOben.Zeile)",
        "    Gib Summe zurück" };
    // line 0 is not supposed to be visible
    pseudoCode.addCodeLine("", null, 0, null);
    // every other line shall be numbered starting with 1
    for (int i = 1; i < pseudoCodeLines.length; i++) {
      pseudoCode.addCodeLine(
          String.format("%02d", i).concat(" ").concat(pseudoCodeLines[i]),
          null, 0, null);
    }

    RectProperties pseudoCodeBoxProp = new RectProperties();
    pseudoCodeBoxProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Pseudocode_Background_Color);
    pseudoCodeBoxProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    pseudoCodeBoxProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    pseudoCodeBox = language.newRect(new Offset(-16, 0, pseudoCode,
        AnimalScript.DIRECTION_NW), new Offset(32, 12, pseudoCode,
        AnimalScript.DIRECTION_SE), "pseudoCodeBox", null, pseudoCodeBoxProp);

    AnimalUtilities.easyHighlight(pseudoCode, 1);
    language.nextStep("Start des Algoritmus");

    // generate the actual Summed Area Table
    for (int row = 0; row < Texture_Height; row++) {
      AnimalUtilities.easyHighlight(pseudoCode, 2);
      language.nextStep();
      for (int column = 0; column < Texture_Width; column++) {
        AnimalUtilities.easyHighlight(pseudoCode, 3);
        language.nextStep();
        AnimalUtilities.easyHighlight(pseudoCode, 4);

        // display the equation summands
        if (Display_Equation) {
          equationUpperSAT.show();
          equationUpperFirstSummand.show();
          equationUpperSecondSummand.show();
          equationUpperThirdSummand.show();
          equationUpperFourthSummand.show();
          equationLowerSAT.show();
          equationLowerFirstSummand.show();
          equationLowerSecondSummand.show();
          equationLowerThirdSummand.show();
          equationLowerFourthSummand.show();
          equationResult.show();
          language.addLine("show \"equationBox\"");
        }

        // display the equation for this step
        equationUpperSAT.setText(getCreateSATSummandString(row, column, 0),
            null, null);
        equationUpperFirstSummand.setText(
            getCreateSATSummandString(row, column, 1), null, null);
        equationUpperSecondSummand.setText(
            getCreateSATSummandString(row, column, 2), null, null);
        equationUpperThirdSummand.setText(
            getCreateSATSummandString(row, column, 3), null, null);
        equationUpperFourthSummand.setText(
            getCreateSATSummandString(row, column, 4), null, null);
        equationLowerSAT.setText(getCreateSATSummandString(row, column, 0),
            null, null);

        // create a nice label for this step
        StringBuilder labelBuilder = new StringBuilder();
        labelBuilder.append("Berechnen des Wertes für [");
        labelBuilder.append(row);
        labelBuilder.append("][");
        labelBuilder.append(column);
        labelBuilder.append("]");
        language.nextStep(labelBuilder.toString());

        int firstSummand = 0, secondSummand = 0, thirdSummand = 0, fourthSummand = 0;

        // first summand
        firstSummand = textureGrid.getElement(row, column);
        equationLowerFirstSummand.setText(Integer.toString(firstSummand), null,
            null);
        equationLowerFirstSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
            Highlight_Color_1, null, null);
        language.addLine(AnimalUtilities.setSummedGridColor(textureGrid, row,
            column, false, Highlight_Color_1));
        language.nextStep();
        equationLowerFirstSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
            Text_Color, null, null);
        language.addLine(AnimalUtilities.setSummedGridColor(textureGrid, row,
            column, false, Cell_Color));

        // second summand
        if (row > 0) {
          secondSummand = summedTextureGrid.getElement(row - 1, column);
          equationLowerSecondSummand.setText(
              " + ".concat(Integer.toString(secondSummand)), null, null);
          equationLowerSecondSummand.changeColor(
              AnimalScript.COLORCHANGE_COLOR, Highlight_Color_2, null, null);
          language.addLine(AnimalUtilities.setSummedGridColor(textureGrid,
              row - 1, column, true,
              AnimalUtilities.lightenColor(Highlight_Color_2)));
          language.addLine(AnimalUtilities.setSummedGridColor(
              summedTextureGrid, row - 1, column, false, Highlight_Color_2));
          AnimalUtilities.easyHighlight(pseudoCode, 5);
          language.nextStep();
          equationLowerSecondSummand.changeColor(
              AnimalScript.COLORCHANGE_COLOR, Text_Color, null, null);
          language.addLine(AnimalUtilities.setSummedGridColor(textureGrid,
              row - 1, column, true, Cell_Color));
          language.addLine(AnimalUtilities.setSummedGridColor(
              summedTextureGrid, row - 1, column, false, Cell_Color));
        }

        // third summand
        if (column > 0) {
          thirdSummand = summedTextureGrid.getElement(row, column - 1);
          equationLowerThirdSummand.setText(
              " + ".concat(Integer.toString(thirdSummand)), null, null);
          equationLowerThirdSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
              Highlight_Color_3, null, null);
          language
              .addLine(AnimalUtilities.setSummedGridColor(textureGrid, row,
                  column - 1, true,
                  AnimalUtilities.lightenColor(Highlight_Color_3)));
          language.addLine(AnimalUtilities.setSummedGridColor(
              summedTextureGrid, row, column - 1, false, Highlight_Color_3));
          AnimalUtilities.easyHighlight(pseudoCode, 6);
          language.nextStep();
          equationLowerThirdSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
              Text_Color, null, null);
          language.addLine(AnimalUtilities.setSummedGridColor(textureGrid, row,
              column - 1, true, Cell_Color));
          language.addLine(AnimalUtilities.setSummedGridColor(
              summedTextureGrid, row, column - 1, false, Cell_Color));
        }

        // fourth summand
        if (row > 0 && column > 0) {
          fourthSummand = summedTextureGrid.getElement(row - 1, column - 1);
          equationLowerFourthSummand.setText(
              " - ".concat(Integer.toString(fourthSummand)), null, null);
          equationLowerFourthSummand.changeColor(
              AnimalScript.COLORCHANGE_COLOR, Highlight_Color_4, null, null);
          language.addLine(AnimalUtilities.setSummedGridColor(textureGrid,
              row - 1, column - 1, true,
              AnimalUtilities.lightenColor(Highlight_Color_4)));
          language
              .addLine(AnimalUtilities.setSummedGridColor(summedTextureGrid,
                  row - 1, column - 1, false, Highlight_Color_4));
          AnimalUtilities.easyHighlight(pseudoCode, 7);
          language.nextStep();
          equationLowerFourthSummand.changeColor(
              AnimalScript.COLORCHANGE_COLOR, Text_Color, null, null);
          language.addLine(AnimalUtilities.setSummedGridColor(textureGrid,
              row - 1, column - 1, true, Cell_Color));
          language.addLine(AnimalUtilities.setSummedGridColor(
              summedTextureGrid, row - 1, column - 1, false, Cell_Color));
        }

        // combine the summands
        int stepResult = firstSummand + secondSummand + thirdSummand
            - fourthSummand;
        equationResult.setText(" = ".concat(Integer.toString(stepResult)),
            null, null);
        summedTextureGrid.put(row, column, stepResult, null, null);
        language.addLine(AnimalUtilities.setGridColor(summedTextureGrid, row,
            column, null, AnimalUtilities.colorToString(Text_Color), null,
            null, null));
        AnimalUtilities.easyHighlight(pseudoCode, 4);
        language.nextStep();

        // reset the text labels
        equationLowerFirstSummand.setText("", null, null);
        equationLowerSecondSummand.setText("", null, null);
        equationLowerThirdSummand.setText("", null, null);
        equationLowerFourthSummand.setText("", null, null);
        equationResult.setText("", null, null);
      }
    }

    // reset the text labels
    equationUpperSAT.setText("", null, null);
    equationUpperFirstSummand.setText("", null, null);
    equationUpperSecondSummand.setText("", null, null);
    equationUpperThirdSummand.setText("", null, null);
    equationUpperFourthSummand.setText("", null, null);
    equationLowerSAT.setText("", null, null);
    equationLowerFirstSummand.setText("", null, null);
    equationLowerSecondSummand.setText("", null, null);
    equationLowerThirdSummand.setText("", null, null);
    equationLowerFourthSummand.setText("", null, null);
    equationResult.setText("", null, null);

    // hide the counterViews
    textureCounterView.hide();
    summedTextureCounterView.hide();

    // unhighlight the code
    AnimalUtilities.easyHighlight(pseudoCode, 0);
  }

  private void showSummedAreaTableUsage() {
    // necessary to allow creation of questions
    language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    for (int i = 1; i <= numberOfQuestions; i++) {
      // generate an area to sum up
      int[] rectangle = generateRectangleInGrid();
      // the correct result
      int actualSum = getPartSum(rectangle[1], rectangle[0], rectangle[3],
          rectangle[2]);

      LinkedList<String> possibleAnswers = new LinkedList<String>();
      LinkedList<Integer> possiblePoints = new LinkedList<Integer>();
      LinkedList<String> possibleFeedback = new LinkedList<String>();

      // add correct answer
      possibleAnswers.add(Integer.toString(actualSum));
      possiblePoints.add(2);
      possibleFeedback.add("Richtig!");

      // generate wrong answers
      Random rand = new Random();
      for (int k = 0; k < 3; k++) {
        int fakeSum = actualSum;
        // the fake answer should be unique
        while (possibleAnswers.contains(Integer.toString(fakeSum))) {
          fakeSum = rand.nextInt(actualSum * 2 + 10);
        }
        // add wrong answer
        possibleAnswers.add(Integer.toString(fakeSum));
        possiblePoints.add(-1);
        possibleFeedback
            .add("Leider Falsch. Die Lösung macht es vielleicht klarer.");
      }

      // generate the question model
      MultipleChoiceQuestionModel multiChoice = new MultipleChoiceQuestionModel(
          "Frage " + i);
      multiChoice.setPrompt("Bitte summiere den markierten Bereich.");
      // add answers in random order
      for (int k = 0; k < 4; k++) {
        int index = rand.nextInt(possibleAnswers.size());
        multiChoice.addAnswer(possibleAnswers.remove(index),
            possiblePoints.remove(index), possibleFeedback.remove(index));
      }

      // color the area that is about to be summed
      for (int x = rectangle[1]; x <= rectangle[3]; x++) {
        for (int y = rectangle[0]; y <= rectangle[2]; y++) {
          language.addLine(AnimalUtilities.setGridColor(textureGrid, x, y,
              null, null, AnimalUtilities.colorToString(Highlight_Color_1),
              null, null));
        }
      }

      language.addMCQuestion(multiChoice);
      animateSolution(i, rectangle);

      // restore color of marked area
      for (int x = rectangle[1]; x <= rectangle[3]; x++) {
        for (int y = rectangle[0]; y <= rectangle[2]; y++) {
          language
              .addLine(AnimalUtilities.setGridColor(textureGrid, x, y, null,
                  null, AnimalUtilities.colorToString(Cell_Color), null, null));
        }
      }
    }
  }

  private void createFinalDescription() {
    // hide the grids, their descriptions and the counters
    textureGrid.hide();
    summedTextureGrid.hide();
    textureGridDescription.hide();
    summedTextureGridDescription.hide();
    textureCounterView.hide();
    summedTextureCounterView.hide();
    pseudoCode.hide();
    pseudoCodeBox.hide();

    // hide yo wife hide yo texts
    equationUpperSAT.setText("", null, null);
    equationUpperFirstSummand.setText("", null, null);
    equationUpperSecondSummand.setText("", null, null);
    equationUpperThirdSummand.setText("", null, null);
    equationUpperFourthSummand.setText("", null, null);
    equationLowerSAT.setText("", null, null);
    equationLowerFirstSummand.setText("", null, null);
    equationLowerSecondSummand.setText("", null, null);
    equationLowerThirdSummand.setText("", null, null);
    equationLowerFourthSummand.setText("", null, null);
    equationResult.setText("", null, null);
    equationBox.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.white, null,
        null);

    // the description code which is displayed in the end
    String[] finalDescriptionText = {
        "Animiert wurde die Erzeugung einer Summed Area Table",
        "für eine ".concat(Integer.toString(Texture_Width)).concat("x")
            .concat(Integer.toString(Texture_Height))
            .concat(" Textur. Mit nur n Zugriffen auf die Textur"),
        "(in diesem konkreten Fall ".concat(
            Integer.toString(textureCounter.getAccess())).concat(
            " Zugriffe) konnte die Summed Area"),
        "Table wesentlich effizienter erstellt werden, als es mit einem naiven",
        "Ansatz möglich gewesen wäre.",
        "",
        "Der Verwendungszweck der Summed Area Table, also das",
        "Aufsummieren eines beliebigen, rechteckigen Teils einer Textur, ist",
        "mit vier Schritten in konstanter Zeit möglich. Dies wurde",
        "außerdem durch die ".concat(Integer.toString(numberOfQuestions))
            .concat(" Fragen deutlich.") };

    // set up the description codeGroup
    SourceCodeProperties finalDescriptionProp = new SourceCodeProperties();
    finalDescriptionProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    SourceCode finalDescription = language.newSourceCode(new Coordinates(80,
        115), "finalDescription", null, finalDescriptionProp);
    for (int i = 0; i < finalDescriptionText.length; i++) {
      finalDescription.addCodeLine(finalDescriptionText[i], null, 0, null);
    }
    language.nextStep("Anmerkungen zum Schluss");
  }

  private void animateSolution(int questionNumber, int[] rectangle) {
    AnimalUtilities.easyHighlight(pseudoCode, 9);
    language.nextStep("Lösung von Frage " + questionNumber);

    int topLeftY = rectangle[1];
    int topLeftX = rectangle[0];
    int bottomRightY = rectangle[3];
    int bottomRightX = rectangle[2];

    // display the equation for this step
    equationUpperSAT.setText(getReadSATSummandString(0, 0, 0), null, null);
    equationUpperFirstSummand.setText(
        getReadSATSummandString(bottomRightY, bottomRightX, 1), null, null);
    equationUpperSecondSummand.setText(
        getReadSATSummandString(topLeftY - 1, topLeftX - 1, 2), null, null);
    equationUpperThirdSummand.setText(
        getReadSATSummandString(topLeftY - 1, bottomRightX, 3), null, null);
    equationUpperFourthSummand.setText(
        getReadSATSummandString(bottomRightY, topLeftX - 1, 4), null, null);
    equationLowerSAT.setText(getReadSATSummandString(0, 0, 0), null, null);
    AnimalUtilities.easyHighlight(pseudoCode, 10);
    language.nextStep();

    int firstSummand = 0, secondSummand = 0, thirdSummand = 0, fourthSummand = 0;

    // first summand
    firstSummand = summedTextureGrid.getElement(bottomRightY, bottomRightX);
    equationLowerFirstSummand.setText(Integer.toString(firstSummand), null,
        null);
    equationLowerFirstSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
        Highlight_Color_1, null, null);
    language.addLine(AnimalUtilities.setGridColor(summedTextureGrid,
        bottomRightY, bottomRightX, null, null,
        AnimalUtilities.colorToString(Highlight_Color_1), null, null));
    language.nextStep();
    equationLowerFirstSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
        Text_Color, null, null);
    language.addLine(AnimalUtilities.setGridColor(summedTextureGrid,
        bottomRightY, bottomRightX, null, null,
        AnimalUtilities.colorToString(Cell_Color), null, null));

    // second summand
    if (topLeftY > 0 && topLeftX > 0) {
      secondSummand = summedTextureGrid.getElement(topLeftY - 1, topLeftX - 1);
      equationLowerSecondSummand.setText(
          " + ".concat(Integer.toString(secondSummand)), null, null);
      equationLowerSecondSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
          Highlight_Color_2, null, null);
      language.addLine(AnimalUtilities.setGridColor(summedTextureGrid,
          topLeftY - 1, topLeftX - 1, null, null,
          AnimalUtilities.colorToString(Highlight_Color_2), null, null));
      AnimalUtilities.easyHighlight(pseudoCode, 11);
      language.nextStep();
      equationLowerSecondSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
          Text_Color, null, null);
      language.addLine(AnimalUtilities.setGridColor(summedTextureGrid,
          topLeftY - 1, topLeftX - 1, null, null,
          AnimalUtilities.colorToString(Cell_Color), null, null));
    }

    // third summand
    if (topLeftY > 0) {
      thirdSummand = summedTextureGrid.getElement(topLeftY - 1, bottomRightX);
      equationLowerThirdSummand.setText(
          " - ".concat(Integer.toString(thirdSummand)), null, null);
      equationLowerThirdSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
          Highlight_Color_3, null, null);
      language.addLine(AnimalUtilities.setGridColor(summedTextureGrid,
          topLeftY - 1, bottomRightX, null, null,
          AnimalUtilities.colorToString(Highlight_Color_3), null, null));
      AnimalUtilities.easyHighlight(pseudoCode, 12);
      language.nextStep();
      equationLowerThirdSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
          Text_Color, null, null);
      language.addLine(AnimalUtilities.setGridColor(summedTextureGrid,
          topLeftY - 1, bottomRightX, null, null,
          AnimalUtilities.colorToString(Cell_Color), null, null));
    }

    // fourth summand
    if (topLeftX > 0) {
      fourthSummand = summedTextureGrid.getElement(bottomRightY, topLeftX - 1);
      equationLowerFourthSummand.setText(
          " - ".concat(Integer.toString(fourthSummand)), null, null);
      equationLowerFourthSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
          Highlight_Color_4, null, null);
      language.addLine(AnimalUtilities.setGridColor(summedTextureGrid,
          bottomRightY, topLeftX - 1, null, null,
          AnimalUtilities.colorToString(Highlight_Color_4), null, null));
      AnimalUtilities.easyHighlight(pseudoCode, 13);
      language.nextStep();
      equationLowerFourthSummand.changeColor(AnimalScript.COLORCHANGE_COLOR,
          Text_Color, null, null);
      language.addLine(AnimalUtilities.setGridColor(summedTextureGrid,
          bottomRightY, topLeftX - 1, null, null,
          AnimalUtilities.colorToString(Cell_Color), null, null));
    }

    // combine the summands
    int stepResult = firstSummand + secondSummand - thirdSummand
        - fourthSummand;
    equationResult.setText(" = ".concat(Integer.toString(stepResult)), null,
        null);
    AnimalUtilities.easyHighlight(pseudoCode, 14);
    language.nextStep();

    // reset the text labels
    equationUpperSAT.setText("", null, null);
    equationUpperFirstSummand.setText("", null, null);
    equationUpperSecondSummand.setText("", null, null);
    equationUpperThirdSummand.setText("", null, null);
    equationUpperFourthSummand.setText("", null, null);
    equationLowerSAT.setText("", null, null);
    equationLowerFirstSummand.setText("", null, null);
    equationLowerSecondSummand.setText("", null, null);
    equationLowerThirdSummand.setText("", null, null);
    equationLowerFourthSummand.setText("", null, null);
    equationResult.setText("", null, null);
  }

  /**
   * Get the sum for a given rectangle.
   * 
   * @param startHeight
   * @param startWidth
   * @param endHeight
   * @param endWidth
   * @return
   */
  private int getPartSum(int startHeight, int startWidth, int endHeight,
      int endWidth) {

    int result = summedTextureGrid.getElement(endHeight, endWidth);
    if (startHeight > 0) {
      result -= summedTextureGrid.getElement(startHeight - 1, endWidth);
    }
    if (startWidth > 0) {
      result -= summedTextureGrid.getElement(endHeight, startWidth - 1);
    }
    if (startHeight > 0 && startWidth > 0) {
      result += summedTextureGrid.getElement(startHeight - 1, startWidth - 1);
    }

    return result;
  }

  /**
   * Get the String for the contents of an equation summand depending on the
   * current row and column values.
   * 
   * @param row
   *          the row
   * @param column
   *          the column
   * @param summand
   *          which summand content to create (a value of 0 creates the part in
   *          front of the =)
   * @return the String contents of the equation
   */
  private String getCreateSATSummandString(int row, int column, int summand) {
    StringBuilder builder = new StringBuilder();
    switch (summand) {
      case 0:
        builder.append("SAT[");
        builder.append(row);
        builder.append("][");
        builder.append(column);
        builder.append("] = ");
        break;
      case 1:
        builder.append("TEX[");
        builder.append(row);
        builder.append("][");
        builder.append(column);
        builder.append("]");
        break;
      case 2:
        builder.append(" + SAT[");
        builder.append(row);
        builder.append(" - 1][");
        builder.append(column);
        builder.append("]");
        break;
      case 3:
        builder.append(" + SAT[");
        builder.append(row);
        builder.append("][");
        builder.append(column);
        builder.append(" - 1]");
        break;
      case 4:
        builder.append(" - SAT[");
        builder.append(row);
        builder.append(" - 1][");
        builder.append(column);
        builder.append(" - 1]");
        break;
    }
    return builder.toString();
  }

  /**
   * Get a String for the equation when reading the SAT (after answering a
   * question).
   * 
   * @param row
   * @param column
   * @param summand
   * @return the String.
   */
  private String getReadSATSummandString(int row, int column, int summand) {
    StringBuilder builder = new StringBuilder();
    switch (summand) {
      case 0:
        builder.append("Ergebnis = ");
        break;
      case 1:
        builder.append("SAT[");
        builder.append(row);
        builder.append("][");
        builder.append(column);
        builder.append("]");
        break;
      case 2:
        builder.append(" + SAT[");
        builder.append(row);
        builder.append("][");
        builder.append(column);
        builder.append("]");
        break;
      case 3:
        builder.append(" - SAT[");
        builder.append(row);
        builder.append("][");
        builder.append(column);
        builder.append("]");
        break;
      case 4:
        builder.append(" - SAT[");
        builder.append(row);
        builder.append("][");
        builder.append(column);
        builder.append("]");
        break;
    }
    return builder.toString();
  }

  /**
   * Generates random coordinates in the form of x1,y1,x2,y2 that are within the
   * grid and form a rectangle in the form of TopLeft, BottomRight
   */
  private int[] generateRectangleInGrid() {
    Random rand = new Random();
    int width = rand.nextInt(Texture_Width - 1) + 1;
    int height = rand.nextInt(Texture_Height - 1) + 1;

    int xStartingPoint = rand.nextInt(Texture_Width - width + 1);
    int yStartingPoint = rand.nextInt(Texture_Height - height + 1);

    int xEndPoint = xStartingPoint + width - 1;
    int yEndPoint = yStartingPoint + height - 1;

    return new int[] { xStartingPoint, yStartingPoint, xEndPoint, yEndPoint };

  }
}
