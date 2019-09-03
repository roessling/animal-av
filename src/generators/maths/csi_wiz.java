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

public class csi_wiz implements Generator {
    private Language lang;
   

    public void init(){
        lang = new AnimalScript("Cubic Spline Interpolation", "Mavin Stiller, Marius Süßmilch", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {

        
        CubicSplineGen csg = new CubicSplineGen();
        
        return csg.run(props, primitives, lang);
    }

    public String getName() {
        return "Cubic Spline Interpolation[EN]";
    }

    public String getAlgorithmName() {
        return "Cubic Spline Interpolation";
    }

    public String getAnimationAuthor() {
        return "Mavin Stiller, Marius Süßmilch";
    }

    public String getDescription(){
        return "In this Animation you will learn how to interpolate different kinds of functions by third order polynomials using the method of Cubic Spline Interpolation. You can test your knowledge with the questions provided in the Animation.";
    }

    public String getCodeExample(){
        return "---";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}