/*
 * ArithConverterGenerator.java
 * Jannis Weil, Hendrik Wuerz, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.arithconvert.ArithConverter;
import algoanim.animalscript.AnimalScript;

/**
 * A generator for an animation of the conversion of an infix expression to a lr-postorder expression.
 * @author Jannis Weil, Hendrik Wuerz
 */
public class ArithConverterGenerator implements ValidatingGenerator {
    private Language lang;

    public void init(){
        lang = new AnimalScript("Konvertierung von Infixnotation zu LR-Postorder Darstellung", "Jannis Weil, Hendrik Wuerz", 800, 600);
    }
 
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        String expression = (String)primitives.get("Ausdruck");
        boolean questions = (boolean)primitives.get("Fragen");
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        ArithConverter.enableQuestions = questions;
        ArithConverter.animateExpression(lang, expression);
        return lang.toString();
    }

    public String getName() {
        return "Konvertierung von Infixnotation zu LR-Postorder Darstellung";
    }

    public String getAlgorithmName() {
        return "Konvertierung von Infixnotation zu LR-Postorder Darstellung";
    }

    public String getAnimationAuthor() {
        return "Jannis Weil, Hendrik Wuerz";
    }

    public String getDescription(){
        return ArithConverter.DESCRIPTION
        	+ "\n\nDer arithmetische Ausdruck kann als Parameter dieser Animation gesetzt werden.\n"
        	+ ArithConverter.INPUT_DESCRIPTION;
    }

    public String getCodeExample(){
        return ArithConverter.SOURCE_CODE;
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		String expression = (String)primitives.get("Ausdruck");
		try{
			ArithConverter.parse(expression);
			// parsing worked, the input is valid
			return true;
		}
		catch(IllegalArgumentException e){
			// parsing did not work..
			throw e;
		}
	}

}