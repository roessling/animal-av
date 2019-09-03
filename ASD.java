//package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class ASD implements Generator {
    private Language lang;

    public void init(){
        lang = new AnimalScript("ASD", "ASDASD", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        int[] ArrayData = (int[])primitives.get("Array Data");
        ArrayData[0] = ArrayData[0];
        return lang.toString();
    }

    public String getName() {
        return "ASD";
    }

    public String getAlgorithmName() {
        return "ASASD";
    }

    public String getAnimationAuthor() {
        return "ASDASD";
    }

    public String getDescription(){
        return "asd";
    }

    public String getCodeExample(){
        return "asd";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}