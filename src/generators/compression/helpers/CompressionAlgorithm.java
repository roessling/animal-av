package generators.compression.helpers;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

/**
 * This abstract class encapsulates a set of features common to compression
 * animations using AnimalScript. *
 * 
 * @author Florian Lindner
 */
public abstract class CompressionAlgorithm {

  protected Language                     lang;

  protected static MatrixProperties      mp;

  protected static ArrayProperties       ap;

  protected static ArrayMarkerProperties amp;

  protected static TextProperties        tpsteps;

  protected static TextProperties        tptopic;

  protected static TextProperties        tpwords;

  protected static SourceCodeProperties  scp;

  protected static GraphProperties       gp;

  protected static RectProperties        rctp;

  protected GeneratorType                myType = new GeneratorType(
                                                    GeneratorType.GENERATOR_TYPE_COMPRESSION);

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public CompressionAlgorithm() {
    // matrix
    mp = new MatrixProperties();
    mp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    mp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    mp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);

    // array
    ap = new ArrayProperties();
    ap.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    ap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

    // array marker
    amp = new ArrayMarkerProperties();
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // text topic
    tptopic = new TextProperties();
    tptopic.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    tptopic.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 32));

    // text steps
    tpsteps = new TextProperties();
    tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    tpsteps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));

    // text "algo in words"
    tpwords = new TextProperties();
    tpwords.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.ITALIC, 22));
    tpwords.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // source code
    scp = new SourceCodeProperties();
    scp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.RED);
    scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // graphs
    gp = new GraphProperties();
    gp.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
    gp.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
    gp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    gp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.YELLOW);
    gp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    gp.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
    gp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);

    // rectangle
    rctp = new RectProperties();
    rctp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  /**
   * getContentLocale returns the target locale of the generated output Use e.g.
   * Locale.US for English content, Locale.GERMANY for German, etc.
   * 
   * @return a Locale instance that describes the content type of the output
   */
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public abstract String getName();

  public String getAnimationAuthor() {
    return "Florian Lindner";
  }

  public void init() {
    // nothing to be done here
  }

}
