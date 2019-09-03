/*
 * OS_SELECT.java
 * Florian Breitfelder, Patrick Jattke, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.OSSelect.OSSelect;
import generators.tree.rbtree_helper.Node;
import generators.tree.rbtree_helper.Tree;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;

/**
 * 
 * Standard generator. Use this one to create a language specific generator.
 * <p>
 * E. g. : <br>
 * OS_SELECT_DE <br> 
 * generators.add(new OS_SELECT_DE("resources/osselect/language")); <br>
 * 
 * OS_SELECT_EN <br>
 * generators.add(new OS_SELECT_EN("resources/osselect/language"));
 *
 */
public class OS_SELECT implements Generator {

	private Language lang;
	private int x_PositionOfRootOfSubtree;
	private int[] KeysForTreeNodes;
	private int i_iThSmallestKeyToFind;
	private Translator trans;
	private Locale locale;
	private Tree tree;

	public OS_SELECT(String languageFilesPath, Locale loc) {
		if (languageFilesPath == null || languageFilesPath.length() == 0
				|| loc == null) {
			trans = new Translator("resources/osselect/language", Locale.US);
			this.locale = Locale.US;
		} else {
			trans = new Translator(languageFilesPath, loc);
			this.locale = loc;
		}
	}

	@Override
	public void init() {
		this.lang = new AnimalScript("OS-SELECT",
				"Florian Breitfelder, Patrick Jattke", 800, 600);
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		// Algorithm parameters
		KeysForTreeNodes = (int[]) primitives.get(trans.translateMessage("KeysForTreeNodes"));
		x_PositionOfRootOfSubtree = (Integer) primitives
				.get(trans.translateMessage("x_PositionOfRootOfSubtree"));
		i_iThSmallestKeyToFind = (Integer) primitives
				.get(trans.translateMessage("i_iThSmallestKeyToFind"));

		OSSelect oss = new OSSelect(lang, trans, primitives, props);

		// Create Startscreen
		oss.showStartscreen();

		// Create Graph
		this.tree = new Tree(KeysForTreeNodes, lang, oss.getCurrentNodeColor());

		// Initialize variable window
		OSSelect.variables = lang.newVariables();

		// Initialize Animation
		oss.initializeAnimation();

		// Start algorithm execution
		Node rootNode = tree.getNodeByKey(KeysForTreeNodes[x_PositionOfRootOfSubtree]);
		oss.osSelectStart(this.tree, rootNode, i_iThSmallestKeyToFind);

		// Create Endscreen
		oss.showEndscreen();

		return lang.toString();
	}

	public String getName() {
		return "OS-SELECT";
	}

	public String getAlgorithmName() {
		return "OS-SELECT";
	}

	public String getAnimationAuthor() {
		return "Florian Breitfelder, Patrick Jattke";
	}

	public String getDescription(){
        return trans.translateMessage("description");
    }

	public String getCodeExample() {
		return "OS-SELECT(x, i)" + "\n" + "r = x.left.size + 1" + "\n"
				+ "if (i == r)" + "\n" + "	return x" + "\n" + "else if (i < r)"
				+ "\n" + "	return OS-SELECT(x.left, i)" + "\n"
				+ "else return OS-SELECT(x.right, i-r)";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return this.locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}
