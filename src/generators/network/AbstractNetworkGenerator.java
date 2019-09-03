package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.CodeView;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * This class provides basic functionality for all network generators and  
 * to create a consistent style for all animations.
 *
 */
public abstract class AbstractNetworkGenerator implements Generator {
	/**
	 *  The Author of all network animations
	 */
//	protected static final String AUTHOR = "Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>";
	
	/**
	 * Text translations. This should be set dynamically by the constructor of the concrete implementation. 
	 */
	protected String textResource;
	protected Locale locale;
	/**
	 * The translator used for all text translations. The object should be initialized with the concrete textResource.
	 */
	protected Translator translator;

	/**
	 * Animation styles derived from generators.network.anim.bbcode.Style
	 */
	protected Style s;

	// animation size
	protected static final int ANIM_WIDTH = 1024;
	protected static final int ANIM_HEIGHT = 768;

	/**
	 * The animal language object used.
	 */
	protected Language l;
	
	/**
	 * Add a header consisting of the algorithm's class (ALGOCLASS) and the name (ALGONAME) to the animation.
	 * The variables are read from the text resource file provided.
	 */
	protected void getHeader() {
		// create header element
		l.newText(new Coordinates(20, 30), translator.translateMessage("ALGOCLASS"), "header1", null, (TextProperties)s.getProperties("h1"));
		l.newText(new Offset(0, 5, "header1", AnimalScript.DIRECTION_SW), translator.translateMessage("ALGONAME"), "header2", null, (TextProperties)s.getProperties("h2"));
	}
	
	/**
	 * Create a title slide with a short introduction on the topic and the animation itself.
	 * By convention the slide's content is read from ${textResource}_TITLESLIDE unless a 
	 * constant named TITLESLIDE is present in the text resource file read by the translator.
	 * 
	 *  Translator variables can be used and must be enclosed in {} brackets.
	 * 
	 * @param vars The values any variables are replaced with. 
	 */
	protected void getTitleSlide(Object... vars) {
		Slide titleSlide = new Slide(l, getResource("TITLESLIDE"), "header2", s, vars);
		titleSlide.hide();
	}
	
	/**
	 * Get a resource by its ID
	 * 
	 * @param id The resource ID
	 * @return The resource
	 */
	protected String getResource(String id) {
		return getResource(id, true);
	}
	
	/**
	 * Get a resource by its ID. 
	 * 
	 * @param id The resource ID
	 * @param languageDependant If the resource is language depended i.e. there needs 
	 * to be a translation for the current language set to true. Otherwise set to false.
	 * @return The resource
	 */
	protected String getResource(String id, boolean languageDependant) {
		String trans = translator.getResourceBundle().getMessage(id, false);
		String res = new String();
		if (trans == null || trans == "") {
			res = textResource + "_" + id;
			if (languageDependant) {
				res = res.concat("." + locale);
			}
		} else {
			res = translator.translateMessage(id);
		}
		return res;
	}

	
	@Override
	public String getAlgorithmName() {
		return translator.translateMessage("ALGOCLASS");
	}

	@Override
	public String getDescription() {		
		return Slide.getTeaser(getResource("TITLESLIDE"));
	}
	
	@Override
	public String getAnimationAuthor() {
		return "Marc Werner";
	}
	
	@Override
	public Locale getContentLocale() {
		return locale;
	}

	@Override
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
	}

	@Override
	public String getName() {
		return translator.translateMessage("ALGONAME");
	}
	
	@Override
	public String getOutputLanguage() {
		// default language is pseudo code
		return Generator.PSEUDO_CODE_OUTPUT;
	}
	
	@Override
	public String getCodeExample() {
		return CodeView.exampleFromFile(getResource("SOURCECODE", false));
	}
	
	@Override
	public void init() {
		// name, author, screen width, screen height
		l = new AnimalScript(translator.translateMessage("ALGONAME"), "Marc Werner", ANIM_WIDTH, ANIM_HEIGHT);

		// set quiz mode type
		l.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}
}
