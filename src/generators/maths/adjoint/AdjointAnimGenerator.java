package generators.maths.adjoint;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class AdjointAnimGenerator implements Generator {
  private Language             lang;
  private SourceCodeProperties minorSourceCodeProperties;
  private SourceCodeProperties mainSourceCodeProperties;
  private MatrixProperties     matrixProperties;
  private int[][]              eingabematrix;

  public void init() {
    lang = new AnimalScript("Berechnung der Adjunkten einer Matrix",
        "Christian Weinert, Simon Reuß", 1200, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    mainSourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("mainSourceCodeProperties");
    minorSourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("minorSourceCodeProperties");

    matrixProperties = (MatrixProperties) props
        .getPropertiesByName("matrixProperties");
    eingabematrix = (int[][]) primitives.get("eingabematrix");

    if (eingabematrix.length != eingabematrix[0].length) {
      lang.newText(
          new Coordinates(20, 30),
          "Dieser Algorithmus akzeptiert nur quadratische Matrizen als Eingabe, bitte geben Sie beim nächsten Versuch eine gültige Matrix an",
          "Fehlermeldung", null);
    } else {
      AdjointAnim adjointAnim = new AdjointAnim(lang, mainSourceCodeProperties,
          minorSourceCodeProperties, matrixProperties);
      adjointAnim.calc(new Matrix(eingabematrix));
    }

    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Berechnung der Adjunkten einer Matrix";
  }

  public String getAlgorithmName() {
    return "Adjunktenberechnung";
  }

  public String getAnimationAuthor() {
    return "Christian Weinert,Simon Reuß";
  }

  public String getDescription() {
    return "Als Adjunkte einer quadratischen Matrix bezeichnet man die Transponierte der Kofaktormatrix, welche aus den vorzeichengewichteten Unterdeterminanten besteht."
        + "\n"
        + "adj(A) = Cof(A)<sup>T</sup> mit A &isin; &#8477;<sup>n&times;n</sup>"
        + "\n"
        + "Sie kann bespielsweise daf&uuml;r verwendet werden, um die Inverse einer regul&auml;ren quadratischen Matrix zu berechnen.";
  }

  public String getCodeExample() {
    return "Adjunktenberechnung(eingabematrix)" + "\n" + "	for i = 1 to n"
        + "\n" + "		for j = 1 to n" + "\n"
        + "			unterdeterminante = Unterdeterminante(eingabematrix, i, j)"
        + "\n" + "			signedUD = (-1)^(i + j) * unterdeterminante" + "\n"
        + "			kofaktormatrix[i][j] = signedUD" + "\n"
        + "	adjunkte = Transponiere kofaktormatrix" + "\n" + "	return adjunkte"
        + "\n" + "	" + "\n" + "Unterdeterminante(eingabematrix, i, j)" + "\n"
        + "	m = Streiche Zeile i und Spalte j aus eingabematrix" + "\n"
        + "	return Determinante(m)";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}
