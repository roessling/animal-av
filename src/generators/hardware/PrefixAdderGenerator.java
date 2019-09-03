/*
 * PrefixAdderGenerator.java
 * Philipp Becker, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hardware;

import algoanim.properties.AnimationPropertiesKeys;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.properties.SquareProperties;
import generators.hardware.prefixAdderAnimation.PrefixAdderAnimation;
import generators.hardware.prefixAdderAnimation.util.MyText;

public class PrefixAdderGenerator implements Generator {
    private Language lang;

    public void init(){
        System.out.println("Init Called");
        lang = new AnimalScript("Prefix Adder", "Philipp Becker", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        TextProperties highlightColor = (TextProperties)props.getPropertiesByName("highlightColor");
        SquareProperties squareColor = (SquareProperties)props.getPropertiesByName("squareColor");
        int inputB = (Integer)primitives.get("inputB");
        boolean subtract = (Boolean)primitives.get("subtract");
        boolean signed = (Boolean)primitives.get("signed");
        int inputA = (Integer)primitives.get("inputA");
        TextProperties textColor = (TextProperties)props.getPropertiesByName("textColor");
        TextProperties inputHighlightColor1 = (TextProperties)props.getPropertiesByName("inputHighlightColor1");
        TextProperties inputHighlightColor2 = (TextProperties)props.getPropertiesByName("inputHighlightColor2");
        TextProperties inputHighlightColor3 = (TextProperties)props.getPropertiesByName("inputHighlightColor3");
        Color inputHighlight1 = (Color) inputHighlightColor1.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        Color inputHighlight2 = (Color) inputHighlightColor2.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        Color inputHighlight3 = (Color) inputHighlightColor3.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        PrefixAdderAnimation animation = new PrefixAdderAnimation(lang);
        animation.setInputs(inputA,inputB,signed,subtract);
        animation.setProperties(textColor,squareColor, (Color) highlightColor.get(AnimationPropertiesKeys.COLOR_PROPERTY),
                inputHighlight1, inputHighlight2, inputHighlight3);
        animation.animateAdder();
        return lang.toString();
    }

    public String getName() {
        return "Prefix Adder";
    }

    public String getAlgorithmName() {
        return "Prefix Adder";
    }

    public String getAnimationAuthor() {
        return "Philipp Becker";
    }

    public String getDescription(){
        return MyText.INTRODUCTION + MyText.ADDITIONAL_INTRO_FOR_GEN;
    }

    public String getCodeExample(){
        return " ";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}