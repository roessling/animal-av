/*
 * OS_RANK.java
 * Florian Breitfelder, Patrick Jattke, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.OSRank.OSRank;
import generators.tree.rbtree_helper.Tree;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;

/**
 * 
 * Standard generator. Use this one to create
 * a language specific generator.
 * 
 * E. g. :
 * OS_RANK_DE
 * generators.add(new OS_RANK_DE("resources/osrank/language"));
 * 
 * OS_RANK_EN
 * generators.add(new OS_RANK_EN("resources/osrank/language"));
 *
 */
public class OS_RANK implements Generator {
    private Language lang;
    private int x_KeyForInorderTreeWalk;
    private int[] t_nodeKeys;
    private Translator trans;
    private Locale locale;
    
    public OS_RANK(String languageFilesPath, Locale loc) {
    	if(languageFilesPath == null || languageFilesPath.length() == 0 || loc == null) {
    		trans = new Translator("resources/osrank/language", Locale.US);
    		this.locale = Locale.US;
    	} else {
    		trans = new Translator(languageFilesPath, loc);
    		this.locale = loc;
    	}
    }

    @Override
	public void init(){
        lang = new AnimalScript("OS-RANK", "Florian Breitfelder, Patrick Jattke", 800, 600);
    }

    @Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
    	// Input paramters for OS-RANK(T,x)
        x_KeyForInorderTreeWalk = (Integer)primitives.get(trans.translateMessage("x_KeyForInorderTreeWalk"));
        t_nodeKeys = (int[])primitives.get(trans.translateMessage("t_nodeKeys"));
        
        // Create OSRank object with primitives provided by user input
        OSRank s = new OSRank(lang, trans, primitives, props);

		// Create Startscreen
		s.showStartscreen();

		// Create Graph
		Tree t = new Tree(t_nodeKeys, lang, s.getCurrentNodeColor());

		// Initialize variable window
		OSRank.variables = lang.newVariables();

		// Initialize Animation
		s.initializeAnimation();

		// Start algorithm execution
		s.osRank(t, t.getNodeByKey(x_KeyForInorderTreeWalk));
		
		// Create Endscreen
		s.showEndscreen();

		return lang.toString();
    }

    @Override
	public String getName() {
        return "OS-RANK";
    }

    @Override
	public String getAlgorithmName() {
        return "OS-RANK";
    }

    @Override
	public String getAnimationAuthor() {
        return "Florian Breitfelder, Patrick Jattke";
    }

    @Override
	public String getDescription(){
        return trans.translateMessage("description");
    }

    @Override
	public String getCodeExample(){
        return "OS-RANK(T, x) "
				 +"\n"
				 +"r = x.left.size + 1"
				 +"\n"
				 +"y = x"
				 +"\n"
				 +"while y != T.root"
				 +"\n"
				 +"	if (y == y.p.right)"
				 +"\n"
				 +"		r = r + y.p.left.size + 1"
				 +"\n"
				 +"	y = y.p"
				 +"\n"
				 +"return r"
				 +"\n";
    }

    @Override
	public String getFileExtension(){
        return "asu";
    }

    @Override
	public Locale getContentLocale() {
        return locale;
    }

    @Override
	public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    @Override
	public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}