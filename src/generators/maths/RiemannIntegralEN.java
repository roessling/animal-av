/*
 * wizard.java
 * Marvin Stiller, Marius S��milch, 2017 for the Animal project at TU Darmstadt.
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

public class RiemannIntegralEN implements Generator {
    private Language lang;

    public void init(){
        lang = new AnimalScript("Riemann Integral [EN]", "Marvin Stiller, Marius Süßmilch", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        RiemannIntegralGeneratorEN rig = new RiemannIntegralGeneratorEN();
        
        return rig.run(props, primitives, lang);
    }

    public String getName() {
        return "Riemann Integral [EN]";
    }

    public String getAlgorithmName() {
        return "Riemann Integral";
    }

    public String getAnimationAuthor() {
        return "Marvin Stiller, Marius Süßmilch";
    }

    public String getDescription(){
        return "\n"
 +"The Riemann Integral is a numerical method to calculate integrals."
 +"\n"
 +"It is calculated by two types of rectangle families."
 +"\n"
 +"The upper sum and the lower sum.";
    }

    public String getCodeExample(){
        return "";
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
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}