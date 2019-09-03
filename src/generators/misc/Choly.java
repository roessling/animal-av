package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class Choly implements Generator {
  private Language lang;
//  private int[][]  intMatrix;

  public void init() {
    lang = new AnimalScript("Cholesky Zerlegung", "Peter Baumann, Oren Avni",
        800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
//    intMatrix = (int[][]) primitives.get("intMatrix");

    return lang.toString();
  }

  public String getName() {
    return "Cholesky Zerlegung";
  }

  public String getAlgorithmName() {
    return "Cholesky Zerlegung";
  }

  public String getAnimationAuthor() {
    return "Peter Baumann, Oren Avni";
  }

  public String getDescription() {
    return "Das Cholesky-Verfahren bezeichnet in der numerischen Mathematik die Zerlegung einer symmetrischen "
        + "\n"
        + "positiven definiten Matrix A in das Produkt einer unteren Dreiecksmatrix L und ihrer Transponierten L^T."
        + "\n"
        + "Nach Berechnung von L bzw. L^T kann ein lineares Gleichungssystem A*x = b (wie beim Gauss-Algorithmus) "
        + "\n"
        + "mit Hilfe der folgenden gestaffelten Systeme gel&ouml;st werden:"
        + "\n"
        + "\n"
        + "1.) L*y = b"
        + "\n"
        + "2.) L^T*x = y"
        + "\n"
        + "\n"
        + "Das Verfahren wurde von dem gleichnahmigen franz&ouml;sischen Mathematiker Andre-Louis Cholesky vor "
        + "\n"
        + "1914 entwickelt, jedoch erst nach seinem Tod (31.08.1918) von seinem befreundeten Offizier"
        + "\n"
        + "Commandant Benoit in 1924 ver&ouml;ffentlicht.  "
        + "\n"
        + "  "
        + "\n"
        + "Quelle: http://www-history.mcs.st-andrews.ac.uk/Biographies/Cholesky.html  "
        + "\n" + "        http://www.mathematik.tu-darmstadt.de";
  }

  public String getCodeExample() {
    return "Bla...";
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

}