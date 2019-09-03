/*
 * SimulatedAnnealingGenerator.java
 * Philipp Becker, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.PolylineProperties;
import generators.searching.SimulatedAnnealing.Algorithm.TemperatureFunction;
import generators.searching.SimulatedAnnealing.Animation;
import generators.searching.SimulatedAnnealing.Util.Txt;

public class SimulatedAnnealingGenerator implements Generator {
    private Language lang;

    public void init(){
        lang = new AnimalScript("Simulated Annealing", "Philipp Becker", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        //Read parameters and properties
        int function = (Integer)primitives.get("Function");
        int maxIterations = (Integer)primitives.get("Max Iterations");
        int tZero = (Integer)primitives.get("T_0");
        int dataPoints = (Integer)primitives.get("Data Points");
        SourceCodeProperties textColor = (SourceCodeProperties)props.getPropertiesByName("Text Color");
        PolylineProperties dataColor = (PolylineProperties)props.getPropertiesByName("Data Color");
        PolylineProperties markerColor = (PolylineProperties)props.getPropertiesByName("Marker Color");
        Animation anim = new Animation(lang);
        //Choose Temperature Function
        TemperatureFunction tempFunc;
        switch (function){
            case 1: tempFunc = TemperatureFunction.exponential;
                break;
            case 3: tempFunc = TemperatureFunction.boltzmann;
                break;
            default:
                tempFunc = TemperatureFunction.fast;
        }
        //Set Inputs
        anim.initPrimitives(tZero, dataPoints, maxIterations, tempFunc);
        anim.initProperties(textColor,markerColor,dataColor);
        //create animation and return
        anim.animate();
        return anim.toString();
    }

    public String getName() {
        return "Simulated Annealing";
    }

    public String getAlgorithmName() {
        return "Simulated Annealing";
    }

    public String getAnimationAuthor() {
        return "Philipp Becker";
    }

    public String getDescription(){
        return Txt.DESCRIPTION + " \n \n" + Txt.ADD_FOR_GEN;
    }

    public String getCodeExample(){
        return Txt.SOURCECODE;
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}