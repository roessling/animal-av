package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpers.DTWCode;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;

public class DTW implements Generator {
  private Language             lang;
  private SourceCodeProperties SourceCodeProps;
  private int[]                Signal;
  private ArrayProperties      ArrayProps;
  private int[]                Reference;
  private MatrixProperties     MatrixProps;
  private boolean              ShorterLoops;

  public void init() {
    lang = new AnimalScript("Dynamic Time Warp",
        "Daniel Tanneberg, Nadine Trüschler", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    SourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("SourceCodeProps");
    Signal = (int[]) primitives.get("Signal");
    ArrayProps = (ArrayProperties) props.getPropertiesByName("ArrayProps");
    Reference = (int[]) primitives.get("Reference");
    MatrixProps = (MatrixProperties) props.getPropertiesByName("MatrixProps");
    ShorterLoops = (Boolean) primitives.get("ShorterLoops");

    if (Signal.length > 0 && Reference.length > 0) {
      DTWCode dtw = new DTWCode(lang, SourceCodeProps, ArrayProps, MatrixProps,
          ShorterLoops);

      dtw.dtw(Reference, Signal);

      lang.nextStep("End");
    } else
      System.out.println("Wrong Inputs!");

    System.out.println(lang);

    return lang.toString();
  }

  public String getName() {
    return "Dynamic Time Warp";
  }

  public String getAlgorithmName() {
    return "Dynamic Time Warping";
  }

  public String getAnimationAuthor() {
    return "Daniel Tanneberg, Nadine Trüschler";
  }

  public String getDescription() {
    return "Dynamic time warping (DTW) is an algorithm for measuring similarity between "
        + "\n"
        + "two sequences which may vary in time or speed. "
        + "\n"
        + "In general, DTW is a method that allows a computer to find an optimal match "
        + "\n"
        + "between two given sequences (e.g. time series) with certain restrictions. "
        + "\n"
        + "The sequences are ''warped' non-linearly in the time dimension to determine "
        + "\n"
        + "a measure of their similarity independent of certain non-linear variations "
        + "\n"
        + "in the time dimension. "
        + "\n"
        + "DTW has been applied to video, audio, and graphics - indeed, "
        + "\n"
        + "any data which can be turned into a linear representation can be analyzed "
        + "\n"
        + "with DTW. A well known application has been automatic speech recognition, "
        + "\n"
        + "to cope with different speaking speeds. "
        + "\n"
        + "DTW is an algorithm particularly suited to matching sequences with missing "
        + "\n"
        + "information, provided there are long enough segments for matching to occur."
        + "\n"
        + "The extension of the problem for two-dimensional 'series' like images "
        + "\n"
        + "(planar warping) is NP-complete, while the problem for one-dimensional signals "
        + "\n" + "like time series can be solved in polynomial time.";
  }

  public String getCodeExample() {
    return "public int dtw(int reference[], int signal[]) {"
        + "\n"
        + "    matrix = new int[signal.length][reference.length];"
        + "\n"
        + "\n"
        + "    for(int i = 0; i < matrix.length; i++) "
        + "\n"
        + "        for(int j = 0; j < matrix[i].length; j++) "
        + "\n"
        + "            matrix[i][j] = Math.abs(signal[i]-reference[j]);"
        + "\n"
        + "\n"
        + "    for(int i = 0; i < matrix.length; i++) {"
        + "\n"
        + "        for(int j = 0; j < matrix[i].length; j++) {"
        + "\n"
        + "            if(i == 0 && j == 0) "
        + "\n"
        + "                matrix[i][j] = Math.abs(signal[i]-reference[j]);"
        + "\n"
        + "            else if(i == 0) "
        + "\n"
        + "                matrix[i][j] = Math.abs(signal[i]-reference[j]) + matrix[i][j-1];"
        + "\n"
        + "            else if(j == 0) "
        + "\n"
        + "                matrix[i][j] = Math.abs(signal[i]-reference[j]) + matrix[i-1][j];"
        + "\n"
        + "            else "
        + "\n"
        + "                matrix[i][j] = Math.abs(signal[i]-reference[j]) + Math.min(Math.min(matrix[i-1][j],"
        + "\n"
        + "                                                                                                                         matrix[i][j-1]),"
        + "\n"
        + "                                                                                                                         matrix[i-1][j-1]);"
        + "\n" + "        }" + "\n" + "    }" + "\n"
        + "    return matrix[signal.length-1][reference.length-1];" + "\n"
        + "}";
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
    return Generator.JAVA_OUTPUT;
  }

}