/*
 * csi_wiz.java
 * Mavin Stiller, Marius Süßmilch, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class csi_wizDE implements Generator {
    private Language lang;
   

    public void init(){
        lang = new AnimalScript("Cubic Spline Interpolation", "Mavin Stiller, Marius Süßmilch", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {

        
        CubicSplineGenDE csg = new CubicSplineGenDE();
        return csg.run(props, primitives, lang);
    }

    public String getName() {
        return "Cubic Spline Interpolation[DE]";
    }

    public String getAlgorithmName() {
        return "Cubic Spline Interpolation";
    }

    public String getAnimationAuthor() {
        return "Marvin Stiller, Marius Süßmilch";
    }

    public String getDescription(){
        return "In dieser Animation wird gezeigt, wie man mit Hilfe von Polynomen dritter Ordnung verschiedene Funktionen interpolieren kann. Ihr könnt euer Wissen an den Fragen in der Animation testen.";
    }

    public String getCodeExample(){
        return "---";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}