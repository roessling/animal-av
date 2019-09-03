package generators.misc.lee;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpers.GridItem;
import generators.misc.helpers.LeeCreator;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.CircleProperties;
import algoanim.properties.SourceCodeProperties;

public class LeeGenerator implements ValidatingGenerator {
  private Language             lang;
  private CircleProperties     gridCircle;
  private int[][]              input;
  private Color                followBack1;
  private SourceCodeProperties sourceCode;
  private Color                followBack2;
  private Color                waveFront2;
  private Color                wall;
  private Color                waveFront1;
  private Color                gridSpot;
  private Font                 introFont;
  private Font                 outroFont;
  private Font                 labelFont;
  private Font                 consoleFont;

  public void init() {
    lang = new AnimalScript("Lee [EN]", "Christian Hollubetz", 800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    gridCircle = (CircleProperties) props.getPropertiesByName("gridCircle");
    input = (int[][]) primitives.get("input");
    followBack1 = (Color) primitives.get("followBack1");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    followBack2 = (Color) primitives.get("followBack2");
    waveFront2 = (Color) primitives.get("waveFront2");
    wall = (Color) primitives.get("wall");
    waveFront1 = (Color) primitives.get("waveFront1");
    gridSpot = (Color) primitives.get("gridSpot");
    introFont = (Font) primitives.get("introFont");
    outroFont = (Font) primitives.get("outroFont");
    labelFont = (Font) primitives.get("labelFont");
    consoleFont = (Font) primitives.get("consoleFont");

    LeeCreator lc = new LeeCreator(lang, gridCircle, input, followBack1,
        sourceCode, followBack2, waveFront2, wall, waveFront1, gridSpot,
        introFont, outroFont, labelFont, consoleFont);
    try {
      lang = lc.perform();
    } catch (Exception e) {
      e.printStackTrace();
    }

    lang.finalizeGeneration();

    return lang.toString();
  }

  public String getName() {
    return "Lee [EN]";
  }

  public String getAlgorithmName() {
    return "Lee [EN]";
  }

  public String getAnimationAuthor() {
    return "Christian Hollubetz";
  }

  public String getDescription() {
    return "The purpose of Lees algorithm is to find the shortest path from a source to a sink (train).<br>"
        + "\n"
        + "The algorithm performs on a grid. It is strongly used to solve the maze problem and with it the task to connect two parts together.<br>"
        + "\n"
        + "The main idea is a wave front, that starts at the source and flows in every possible direction.<br>"
        + "\n"
        + "When this wave front reaches the train, the shortest path is determined by following back the wave fronts path."
        + "\n"
        + "You can specify a few parameters for the input. Some are options for the visualization and one int[][] is the input grid. This grid has include one source and one train. The source is modeled by a "
        + GridItem.SOURCE
        + " and the train is modelled by a "
        + GridItem.TRAIN
        + ". There has to be exactly one source and one train. Walls can be modelled by a "
        + GridItem.WALL
        + ", but there is no need to have at least one wall. All the other values should be 0.";
  }

  public String getCodeExample() {
    return "lee(grid_point S, grid_point T) {" + "\n"
        + "  set<grid_point> wave, new_wave;" + "\n"
        + "  grid_point neighbor, elem, path_elem;" + "\n" + "  int label;"
        + "\n" + "  /* 1. Step: Wave front */" + "\n" + "  new_wave := {S};"
        + "\n" + "  label := 0;" + "\n" + "  while (!new_wave.contains(T)) {"
        + "\n" + "    ++label;" + "\n" + "    wave := new_wave;" + "\n"
        + "    new_wave := empty;" + "\n" + "    foreach (element : wave)"
        + "\n" + "      foreach (neighbor : neighbors(element))" + "\n"
        + "        if (neighbor.value == 0) {" + "\n"
        + "          neighbor.value := label;" + "\n"
        + "          new_wave := new_wave + {neighbor};" + "\n" + "        }"
        + "\n" + "  }" + "\n" + "  /* 2. Step: Follow back */" + "\n"
        + "  path_elem := T;" + "\n" + "  for (i:=label-1; i >= 1; --i) {"
        + "\n" + "    path_elem.value := -1;" + "\n"
        + "    path_elem := chooseNeighborOf_WithValue_(path_elem, i);" + "\n"
        + "  }" + "\n" + "  /* 3. Step: Cleanup */" + "\n"
        + "  foreach 'point on grid'" + "\n" + "    if (point.value > 0)"
        + "\n" + "      point.value := 0;" + "\n" + "}" + "\n" + "\n" + "\n"
        + "class grid_point : point {" + "\n" + "  int value;" + "\n" + "};";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    try {
      // no empty input
      if (arg1.get("input") == null)
        throw new IllegalArgumentException(
            "The input is not allowed to be empty. Please specify it.");

      int[][] input = (int[][]) arg1.get("input");

      // no unusable small input
      if (input.length < 1 || input[0].length < 1)
        throw new IllegalArgumentException(
            "The input is unusable small. The grid should at least have the place for two items.");

      // exactly one source and one train
      int countSource = 0;
      int countTrain = 0;
      for (int i = 0; i < input.length; i++)
        for (int j = 0; j < input[0].length; j++) {
          countSource += (input[i][j] == GridItem.SOURCE) ? 1 : 0;
          countTrain += (input[i][j] == GridItem.TRAIN) ? 1 : 0;
        }
      if (countSource != 1 || countTrain != 1)
        throw new IllegalArgumentException(
            "There has to be exactly one source (" + GridItem.SOURCE
                + ") and exactly one train (" + GridItem.TRAIN
                + ") in the input.");
    } catch (Exception e) {
      // other exception
      throw new IllegalArgumentException("An other exception occured.");
    }

    // the input fulfills all the needs and with it is a correct input
    return true;
  }

}