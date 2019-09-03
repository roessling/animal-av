/*
 * Gerschgorin.java
 * Jannis Weil, Hendrik Wuerz, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

/**
 * Connects the gerschgorin implementation with the animal framework.
 * @author Jannis Weil, Hendrik Wuerz
 */
public class Gerschgorin implements ValidatingGenerator {
    private Language lang;
    private int[][] matrix;

    public void init() {
        lang = new AnimalScript("Gerschgorin Kreise", "Jannis Weil, Hendrik Wuerz", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        matrix = (int[][]) primitives.get("matrix");
        new generators.maths.gerschgorin.Gerschgorin(lang, matrix);
        return lang.toString();
    }

    public String getName() {
        return "Gerschgorin Kreise";
    }

    public String getAlgorithmName() {
        return "Gerschgorin Kreise zur Eingrenzung von Eigenwerten quadratischer Matrizen";
    }

    public String getAnimationAuthor() {
        return "Jannis Weil, Hendrik Wuerz";
    }

    public String getDescription() {
        return "Mit Hilfe von Gerschgorin Kreisen können die Eigenwerte von quadratischen Matrizen eingegrenzt werden. "
                + "\n"
                + "Als Ergebnis liegen Intervalle vor, in denen sich die jeweiligen Eigenwerte befinden müssen, eine Berechnung der exakten Werte ist über dieses Verfahren jedoch nicht möglich."
                + "\n"
                + "Dennoch eignet es sich häufig um eine Abschätzung treffen zu können, oder um die Plausibilität von Werten zu überprüfen.";
    }

    public String getCodeExample() {
        return "verarbeiteMatrix(matix) {"
                + "\n"
                + "  foreach(row in matrix) {"
                + "\n"
                + "    Zeichne den Kreis-Mittelpunk an der Stelle des Diagonalelements"
                + "\n"
                + "    foreach(column in row) {"
                + "\n"
                + "        Erhöhe Kreisradius um den Elementwert"
                + "\n"
                + "    }"
                + "\n"
                + "  }"
                + "\n"
                + "}"
                + "\n"
                + "verarbeiteMatrix(originalMatrix);"
                + "\n"
                + "verarbeiteMatrix(transponierteMatrix);"
                + "\n"
                + "foreach(row in matrix) {"
                + "\n"
                + "    finalerRadius = Min(KreisAusOriginalMatrix, KreisAusTransponierterMatrix)"
                + "\n"
                + "}"
                + "\n"
                + "lokalisiereEigenwerte()";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        int[][] matrix = (int[][]) primitives.get("matrix");
        if(matrix.length == 0) throw new IllegalArgumentException("The matrix must not be empty");
        if(matrix.length != matrix[0].length) throw new IllegalArgumentException("The matrix must be quadratic");
        return true;
    }
}