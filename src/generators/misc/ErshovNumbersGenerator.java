/*
 * ErshovNumbersGenerator.java
 * Robin Koerkemeier, Kyra Wittorf, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.ershov.ErshovNumbers;
import generators.misc.ershov.parser.Parser;
import generators.misc.ershov.parser.exception.ParserException;
import generators.misc.ershov.parser.exception.TokenizerException;
import translator.Translator;

import java.util.Hashtable;
import java.util.Locale;

public class ErshovNumbersGenerator implements ValidatingGenerator {
    private Language lang;

    private Translator translator;
    private Locale locale;

    public static void main(String[] args) {
        Generator generator = new ErshovNumbersGenerator("resources/ErshovNumbers", Locale.US);
        Animal.startGeneratorWindow(generator);
    }

    public ErshovNumbersGenerator(String resource, Locale languageLocale){
        translator = new Translator(resource, languageLocale);
        locale = languageLocale;
    }

    @Override
    public void init() {
        lang = new AnimalScript(translator.translateMessage("title"), "Robin Koerkemeier, Kyra Wittorf", 800, 600);
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        String expression = (String) primitives.get("Ausdruck");

        try {
            new Parser().parse(expression);
            return true;
        } catch (TokenizerException | ParserException e) {
            return false;
        }
    }

    @Override
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        SourceCodeProperties graphProperties = (SourceCodeProperties) props.getPropertiesByName("Graph");
        RectProperties rectProperties = (RectProperties) props.getPropertiesByName("Rechteck");
        SourceCodeProperties sourceProperties =
                (SourceCodeProperties) props.getPropertiesByName("Pseudocode und Aufgabe");
        TextProperties textProperties = (TextProperties) props.getPropertiesByName("Text");

        String expression = (String) primitives.get("Ausdruck");
        ErshovNumbers ershov = new ErshovNumbers(lang, expression, translator);

        ershov.determineProperties(graphProperties, rectProperties, sourceProperties, textProperties);
        ershov.doAnimation();

        return lang.toString();
    }

    @Override
    public String getName() {
        return translator.translateMessage("title");
    }

    @Override
    public String getAlgorithmName() {
        return translator.translateMessage("header");
    }

    @Override
    public String getAnimationAuthor() {
        return "Robin Koerkemeier, Kyra Wittorf";
    }

    @Override
    public String getDescription() {
        return translator.translateMessage("fullDescription");
    }

    @Override
    public String getCodeExample() {
        return "int erschov(v)"
                + "\n"
                + "    if v.left is NIL and v.right is NIL"
                + "\n"
                + "        return 1"
                + "\n"
                + "    else if v.left is NIL"
                + "\n"
                + "        return ershov(v.right)"
                + "\n"
                + "    else if v.right is NIL"
                + "\n"
                + "        return ershov(v.left)"
                + "\n"
                + "    else"
                + "\n"
                + "        eLeft <- ershov(v.left)"
                + "\n"
                + "        eRight <- ershov(v.right)"
                + "\n"
                + "        if eLeft == eRight"
                + "\n"
                + "            return eLeft + 1"
                + "\n"
                + "        else"
                + "\n"
                + "            return max(eLeft, eRight)"
                + "\n"
                + "        endif"
                + "\n"
                + "    endif";
    }

    @Override
    public String getFileExtension() {
        return "asu";
    }

    @Override
    public Locale getContentLocale() {
        return locale;
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    @Override
    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
}