/*
 * ResolutionGenerator.java
 * Fabian Bauer, Andreas Bauer, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Color;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.resolutionCalculus.Clause;
import generators.maths.resolutionCalculus.ResolutionGeneratorMain;
import generators.maths.resolutionCalculus.parser.Parser;
import generators.maths.resolutionCalculus.parser.Token;
import translator.Translator;

public class ResolutionGenerator implements ValidatingGenerator {

	private Translator translator;

	private Language lang;
	private Color clauseCheckColor;
	private Color clauseHighlightColor1;
	private Color clauseHighlightColor2;
	private List<Clause> clauseSet;
	private boolean showQuestions;

	public ResolutionGenerator(Locale locale) {
		if (locale != Locale.GERMAN && locale != Locale.ENGLISH)
			throw new IllegalArgumentException(locale.toString().concat(" unsupported"));
		translator = new Translator("resources/Resolution-Calculus", locale);
	}

	public void init() {
		lang = new AnimalScript("Resolution-Calculus", "Fabian Bauer, Andreas Bauer", 800, 600);
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		ResolutionGeneratorMain.main(translator, lang, clauseSet, clauseHighlightColor1, clauseHighlightColor2,
				clauseCheckColor, showQuestions);
		return lang.toString();
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		clauseCheckColor = (Color) primitives.get("clauseCheckColor");
		clauseHighlightColor1 = (Color) primitives.get("clauseHighlightColor1");
		clauseHighlightColor2 = (Color) primitives.get("clauseHighlightColor2");
		showQuestions = (Boolean) primitives.get("showQuestions");
		String input = (String) primitives.get("clauseSet");
		try {
			clauseSet = new Parser(Token.toTokens(input)).getClauses();
			if (clauseSet.isEmpty())
				return false;
			for (Clause c : clauseSet)
				if (c.isUnsatisfiable())
					return false;
		} catch (IllegalArgumentException e) {
			return false;
		}

		return true;
	}

	public String getName() {
		return translator.translateMessage("name");
	}

	public String getAlgorithmName() {
		return "Resolution-Calculus";
	}

	public String getAnimationAuthor() {
		return "Fabian Bauer, Andreas Bauer";
	}

	public String getDescription() {
		return translator.translateMessage("wdwExpl1").concat(translator.translateMessage("wdwExpl2"))
				.concat(translator.translateMessage("wdwExpl3")).concat(translator.translateMessage("wdwExpl4"))
				.concat(translator.translateMessage("wdwExpl5")).concat(translator.translateMessage("wdwExpl6"))
				.concat(translator.translateMessage("wdwExpl7")).concat(translator.translateMessage("wdwExpl8"))
				.concat(translator.translateMessage("wdwExpl9")).concat(translator.translateMessage("wdwExpl10"))
				.concat(translator.translateMessage("wdwExpl11"));
	}

	public String getCodeExample() {
		return "resolve(clause1, clause2) {" + "\n" + "  res = new List<Clause>" + "\n"
				+ "  foreach literal : clause1.literals {" + "\n" + "    negation = clause2.getNegatedLiteral(literal)"
				+ "\n" + "    if negation != null" + "\n"
				+ "      res.add(new Clause((clause1.literals UNION clause2.literals) EXCEPT (literal UNION negation)))"
				+ "\n" + "  }" + "\n" + "  checkForUnsatisfiability(res)" + "\n" + "  removeAlreadyExisting(res)" + "\n"
				+ "  return res" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return translator.getCurrentLocale();
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}