package generators;

import generators.framework.Generator;

import java.util.HashMap;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.annotations.Annotation;
import algoanim.annotations.ExecutorManager;
import algoanim.annotations.LineParser;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;

/**
 * This is the base class for annotated algorithms, a special
 * type of AV content generators. Annotated algorithms allow the
 * content author to specify annotations (similar to Java annotations)
 * in the source code to cause "appropriate" actions.
 * 
 * @author Sebastian Proksch <sproksch@rbg.informatik.tu-darmstadt.de>,
 * Guido Roessling <roessling@acm.org>
 * @version 1.0 2009-02-20
 */
public abstract class AnnotatedAlgorithm implements Generator {
  
  /**
   * The content generation language instance
   */
  protected Language                            lang;
  
  /**
   * The source code block to be shown in the animation - and that
   * contains the annotations
   */
  protected SourceCode                          sourceCode;
  
  /**
   * The variables used in the animation
   */
  protected Variables                           vars;
  
  /**
   * The HashMap of annotations, associating a line as the key
   * with the vector of annotations
   */
  protected HashMap<String, Vector<Annotation>> annotations;
  
  /**
   * The manager for executing annotations
   */
  protected ExecutorManager                     annoMan;

  /**
   * Subclasses must implement this method to provide access
   * to the source code that is visible in the animation, and
   * that contains the annotations to be executed as we go along
   * 
   * @return the String that contains the annotated source. Each
   * line in the String typically represents one code line.
   */
  public abstract String getAnnotatedSrc();

  protected String lastLabelUsed = null;
  
  /**
   * init method
   */
  public void init() {
    // Generate a new Language instance for content creation
    // Parameter: Animation title, author, width, height
    lang = new AnimalScript(getName(), getAnimationAuthor(), 640, 480);

    // Activate step control
    lang.setStepMode(true);

    // initiate source code primitive
    sourceCode = lang.newSourceCode(new Coordinates(20, 20), "code", null);

    // initiate variables support
    vars = lang.newVariables();

    annotations = new HashMap<String, Vector<Annotation>>();

    // last used label
    lastLabelUsed = null;
  }

  /**
   * returns the file extension for the animation; typically will be
   * "asu" for AnimalScript (uncompressed)
   */
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /**
   * Method to parse the annotated source code. Note that the
   * <em>sourceCode</em> attribute <em>must</em> exist before this method
   * is called!
   */
  public void parse() {
    // initialize AnnotationManager
    annoMan = new ExecutorManager(vars, sourceCode);

    LineParser parser;
    String[] lines = getAnnotatedSrc().split("\n");
    for (int i = 0; i < lines.length; i++) {

      // retrieve annotation symbol
      parser = new LineParser(lines[i], "@");

      // store annotations for this label
      annotations.put(parser.getLabel(), parser.getProperties());

      // add the line to the SourceCode object that has been created before
      if (parser.isContinue())
        sourceCode.addCodeElement(parser.getCode(), parser.getLabel(), parser
            .getIndent(), null);
      else
        sourceCode.addCodeLine(parser.getCode(), parser.getLabel(), parser
            .getIndent(), null);
    }
  }

  /**
   * uses the <code>LineParser</code> to automatically generate a code-example
   * based on the annotated source
   * 
   * @returns code example
   */
  public String getCodeExample() {
    String codeExample = "";
    String preLine = "";

    LineParser parser;
    String[] lines = getAnnotatedSrc().split("\n");
    for (int i = 0; i < lines.length; i++) {
      // TODO annotation symbol aus der datei holen
      parser = new LineParser(lines[i], "@");

      String indent = "";
      for (int j = 0; j < parser.getIndent(); j++)
        indent += "  ";

      String code = parser.getCode().replace("<", "&lt;").replace(">", "&gt;");
      if (parser.isContinue())
        codeExample += " " + code;
      else {
        codeExample += preLine + indent + code;
        preLine = "\n";
      }
    }
    return codeExample;
  }

  /**
   * Convenience method for executing a code line.
   * Calling exec on a label will highlight the associated
   * code line in the animation and then execute all annotations
   * for the current line.
   * @param label
   */
  public void exec(String label) {
    Vector<Annotation> props = annotations.get(label);
    if (props == null) {
      System.err.print("No entry found for label '"+label +"'");
      if (lastLabelUsed != null)
          System.err.println(", last valid label was " +lastLabelUsed);
      else
        System.err.println("; this was the first exec you requested!");
    }
    else {
      sourceCode.toggleHighlight(label);
      annoMan.exec(props);
    }
  }
}
