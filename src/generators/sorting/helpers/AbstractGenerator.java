package generators.sorting.helpers;

import generators.framework.Generator;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Vector;

import algoanim.annotations.Annotation;
import algoanim.annotations.ExecutorManager;
import algoanim.annotations.LineParser;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public abstract class AbstractGenerator implements Generator {

  protected static final TextProperties       HEADLINE_PROPERTIES;
  protected static final TextProperties       LABEL_PROPERTIES;
  protected static final TextProperties       DESC_PROPERTIES;
  protected static final TextProperties       VARIABLE_PROPERTIES;
  public static final TextProperties          BOLD;
  protected static final SourceCodeProperties SOURCECODE_PROPERTIES;

  protected static final int                  MARGIN  = 30;
  protected static final int                  PADDING = 20;

  /**
   * The variables used in the animation
   */
  private Variables                           vars;
  /**
   * The content generation language instance
   */
  protected Language                          lang;

  /**
   * The source code block to be shown in the animation - and that contains the
   * annotations
   */
  protected SourceCode                        sourceCode;

  /**
   * The manager for executing annotations
   */
  private ExecutorManager                     annoMan;
  private HashMap<String, Vector<Annotation>> annotations;

  static {
    HEADLINE_PROPERTIES = new TextProperties();
    HEADLINE_PROPERTIES
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    HEADLINE_PROPERTIES.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));

    SOURCECODE_PROPERTIES = new SourceCodeProperties();
    SOURCECODE_PROPERTIES.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    SOURCECODE_PROPERTIES.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    SOURCECODE_PROPERTIES.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    SOURCECODE_PROPERTIES.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        Color.BLACK);

    DESC_PROPERTIES = new TextProperties();
    DESC_PROPERTIES.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    DESC_PROPERTIES.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));

    LABEL_PROPERTIES = new TextProperties();
    LABEL_PROPERTIES.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    LABEL_PROPERTIES.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));

    VARIABLE_PROPERTIES = new TextProperties();
    VARIABLE_PROPERTIES.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    VARIABLE_PROPERTIES.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));

    BOLD = new TextProperties();
    BOLD.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    BOLD.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 18));
  }

  public String getAnimationAuthor() {
    return "Jan Kassens, Zoran Zaric, Jürgen Benjamin Ronshausen";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public void init() {
    annotations = new HashMap<String, Vector<Annotation>>();
  }

  public void parse() {
    annotations = new HashMap<String, Vector<Annotation>>();
    vars = lang.newVariables();

    // initialize AnnotationManager
    annoMan = new ExecutorManager(vars, sourceCode);

    String[] lines = getAnnotatedSrc();
    for (int i = 0; i < lines.length; i++) {

      LineParser parser = new LineParser(lines[i], "@");

      // store annotations for this label
      annotations.put(parser.getLabel(), parser.getProperties());

      // add the line to the SourceCode object that has been created before
      if (parser.isContinue())
        sourceCode.addCodeElement(parser.getCode(), parser.getLabel(),
            parser.getIndent(), null);
      else
        sourceCode.addCodeLine(parser.getCode(), parser.getLabel(),
            parser.getIndent(), null);
    }
  }

  /**
   * Hebt eine Zeile hervor und ruft nextStep des Language-Objekts auf.
   * 
   * @param label
   *          Die hervorzuhebene Zeile.
   */
  protected void execute(String label) {
    lang.nextStep();
    highlight(label);
  }

  protected abstract String[] getAnnotatedSrc();

  public void highlight(String label) {
    Vector<Annotation> props = annotations.get(label);
    sourceCode.toggleHighlight(label);
    annoMan.exec(props);
  }

}
