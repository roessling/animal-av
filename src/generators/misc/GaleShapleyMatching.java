package generators.misc;
import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

public class GaleShapleyMatching implements Generator {
	
	private int N, engagedCount;
	private String[][] menPref;
	private String[][] womenPref;
	private String[] men;
	private String[] women;
	private String[] womenPartner;
	private boolean[] menEngaged;
	private String[] menEng;
	private StringArray Men;
	private StringArray Women;
	private StringMatrix WomenPref;
	private StringArray MenEngaged;
	private StringMatrix MenPref;
	private StringArray WomenPartner;
	private Language lang;
	
	
	public GaleShapleyMatching(){
		init();
	}

	

	/** function to calculate all matches 
	 * @param wp 
	 * @param mp2 
	 * @param w 
	 * @param m **/
	private String calcMatches(String[] m, String[] w, String[][] mp, String[][] wp)

	{
		N = mp.length;
		engagedCount = 0;
		men = m;
		women = w;
		menPref = mp;
		womenPref = wp;
		womenPartner = new String[N];
		menEng = new String[N];
		
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		
		ArrayProperties ap1 = new ArrayProperties();
		ap1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);

		ArrayProperties ap2 = new ArrayProperties();
		ap2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		ArrayProperties ap3 = new ArrayProperties();
		ap3.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
		ap3.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		ap3.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.WHITE);

		// SourceCode Properties
		SourceCodeProperties Sc = new SourceCodeProperties();
		Sc.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
		Sc.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		// Matrix Properties
		MatrixProperties MatrixP = new MatrixProperties();
		// mp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, );
		MatrixP.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.green);

		AnimationPropertiesContainer props = null;
		// Tittle
		TextProperties headerProp = new TextProperties();
		headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 1, 24));		
		Text title = lang.newText(new Coordinates(20,30), "Gale Shapley Algorithm", "Gale-Shapley",null, headerProp);
		//Introduction
		SourceCodeProperties sProps = new SourceCodeProperties();
	    sProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 0, 16));
		String introduction = "The following visualization shows the stable marriage problem to pair men and women"
				+ " who had expressed their individual preferences about one another. "+ "\n" + "A pairing is considered stable if no spouse is motivated to select another."
				+ "\nThe Gale-Shapley procedure uses rankings to determine stable pairings. "
				+ "First, assign each man to the woman he most prefers. If two or more men ask "
				+ "\nthe same woman, she must choose whom she prefers. The other men then "
				+ "ask the next woman on their lists. This process should be continued until stable"
				+ "\npairings are obtained";
		
		String fazit = "The algorithm proved that there is a stable set of engagements for any set of preferences. A stable set of engagements for marriage is one, "
				+ "\nwhere no man prefers a woman over the one he is engaged to, where that other woman also prefers that man over the one she is engaged "
				+ "\nto. The determination of stable matching has many applications in allocation problems. For example, The algorithm has been in the USA "
				+ "\nsince 1952 under the National Resident Matching Program medicine students distributed to hospitals, using a procedure Which is based "
				+ "\non the concept of stable matching.";
		
		
		
		
		SourceCode intro1 = lang.newSourceCode(new Coordinates(20,80), "introduction", null, sProps);
		intro1.addMultilineCode(introduction, null, null);
		lang.nextStep();
		intro1.hide();
		// create the text-object to visualize der Text
		SourceCode code = lang.newSourceCode(new Coordinates(20, 120), "PseudoCode", null, Sc);

		lang.nextStep();
		Text t1 = lang.newText(new Coordinates(630, 20), "Men", "t1", null);
		Men = lang.newStringArray(new Coordinates(630, 40), men, "Men", null, ap1);
		Men.showIndices(false, null, null);
		Text t2 = lang.newText(new Coordinates(830, 20), "Women", "t2", null);
		Women = lang.newStringArray(new Coordinates(830, 40), women, "Women", null, ap2);
		Women.showIndices(false, null, null);
		lang.nextStep();
		Text t4 = lang.newText(new Coordinates(630, 100), "Preference of Men", "t3", null);
		MenPref = lang.newStringMatrix(new Coordinates(630, 120), menPref, "MenPref", null);
		lang.nextStep();
		Text t3 = lang.newText(new Coordinates(830, 100), "Preference of Women", "t3", null);
		WomenPref = lang.newStringMatrix(new Coordinates(830, 120), womenPref, "WomenPref", null);
		lang.nextStep();

		// write the PseudoCode
		code.addCodeLine("function GaleShapleyMatching", null, 0, null);
		code.addCodeLine("{", null, 1, null);
		code.addCodeLine("Initialize all men and women to free", null, 2, null);
		code.addCodeLine("while there is free man who still has a woman to propose to", null, 2, null);
		code.addCodeLine("{", null, 3, null);
		code.addCodeLine("w = m's highest ranked such woman to whom he has not yet proposed", null, 4, null);
		code.addCodeLine("if w is free", null, 4, null);
		code.addCodeLine("(m, w) become engaged", null, 5, null);
		code.addCodeLine("else some pair (m', w) already exists", null, 4, null);
		code.addCodeLine("if w prefers m to m'", null, 5, null);
		code.addCodeLine("(m, w) become engaged", null, 6, null);
		code.addCodeLine("m' becomes free", null, 6, null);
		code.addCodeLine("else", null, 5, null);
		code.addCodeLine("(m', w) remain engaged", null, 6, null);
		code.addCodeLine("}", null, 3, null);
		code.addCodeLine("}", null, 1, null);
		lang.nextStep();

		code.highlight(0);
		lang.nextStep();
		code.unhighlight(0);
		code.highlight(2);
		lang.nextStep();

		Text t6 = lang.newText(new Coordinates(630, 300), "Engaged Men", "t6", null);
		Text t5 = lang.newText(new Coordinates(630, 360), "Women's Partner", "t5", null);
		MenEngaged = lang.newStringArray(new Coordinates(630, 320), menEng, "MenEngaged", null, ap3);
		MenEngaged.showIndices(false, null, null);
		WomenPartner = lang.newStringArray(new Coordinates(630, 380), womenPartner, "WomenPartner", null, ap3);
		WomenPartner.showIndices(false, null, null);

		for (int i = 0; i < MenEngaged.getLength(); i++) {

			MenEngaged.put(i, "free", null, null);

		}
		for (int i = 0; i < WomenPartner.getLength(); i++) {

			WomenPartner.put(i, "free", null, null);

		}
		lang.nextStep();
		code.unhighlight(2);
		int current = 0;
		
		while (engagedCount < N) {
			
			code.highlight(3);
			lang.nextStep();
						
			

			int free;
			for (free = 0; free < N; free++)

				if (MenEngaged.getData(free).equals("free")){
					MenEngaged.highlightCell(free, null, null);
					break;
				}
			lang.nextStep();		
			int i;
			for (i = 0; i < N && (MenEngaged.getData(free).equals("free")); i++) {

				code.unhighlight(8);
				code.unhighlight(3);
				code.highlight(5);
				MenPref.highlightCell(free, i, null, null);

				int index = womenIndexOf(MenPref.getElement(free, i));

				lang.nextStep();
				code.unhighlight(5);
				code.highlight(6);
				lang.nextStep();
				WomenPartner.highlightCell(index, null, null);
				if (WomenPartner.getData(index) == "free")

				{
					lang.nextStep();
					code.unhighlight(6);
					code.highlight(7);
					WomenPartner.highlightCell(index, null, null);
					WomenPartner.put(index, Men.getData(free), null, null);
					MenEngaged.put(free, MenPref.getElement(free, i), null, null);
					engagedCount++;
					lang.nextStep();
					code.unhighlight(7);
					code.highlight(3);

				} else {
					code.unhighlight(6);
					code.highlight(8);
					for (int j = 0; j < Men.getLength(); j++) {
						if (MenEngaged.getData(j).equals(MenPref.getElement(free, i))) {
							
							MenEngaged.highlightElem(j, null, null);
							WomenPartner.highlightElem(index, null, null);
						}
					}
					
					lang.nextStep();
					String currentPartner = WomenPartner.getData(index);
					code.unhighlight(8);
					code.highlight(9);
					lang.nextStep();
					TrueFalseQuestionModel tfq_1 = new TrueFalseQuestionModel("tfq", true, 1);
					tfq_1.setPrompt("Is there even better preferences for the current woman");		
					tfq_1.setFeedbackForAnswer(true, "Correct, there is a better preference.");
					tfq_1.setFeedbackForAnswer(false, "Wrong answer, there is an another preference.");
					lang.addTFQuestion(tfq_1);
					lang.nextStep();

					
					if (morePreference(currentPartner, Men.getData(free), index)) {
						
						
//						MultipleChoiceQuestionModel howIsPrefOfWomenQuestion = new MultipleChoiceQuestionModel("howIsPrefOfWomenQuestion");
						
						WomenPartner.highlightCell(index, null, null);
						lang.nextStep();
						code.unhighlight(9);
						code.highlight(10);

						WomenPartner.put(index, Men.getData(free), null, null);
						WomenPartner.unhighlightElem(index, null, null);
						MenEngaged.put(free, MenPref.getElement(free, i), null, null);
						lang.nextStep();
						code.unhighlight(10);
						code.highlight(11);
						MenEngaged.put(menIndexOf(currentPartner), "free", null, null);
						MenEngaged.unhighlightCell(menIndexOf(currentPartner), null, null);
						MenEngaged.unhighlightElem(menIndexOf(currentPartner), null, null);
						lang.nextStep();
						code.unhighlight(11);
						code.highlight(3);
						
						
					}
					else{
						
						code.unhighlight(9);
						code.highlight(12);
						code.highlight(13);
						WomenPartner.unhighlightElem(index, null, null);
						MenEngaged.unhighlightElem(menIndexOf(currentPartner), null, null);
						lang.nextStep();
						code.unhighlight(12);
						code.unhighlight(13);
						code.highlight(3);
						
					}					
				}
			}
		}
		lang.nextStep();
		code.unhighlight(3);
		
		
		WomenPartner.hide();
		MenEngaged.hide();
		code.hide();
		t1.hide();
		t2.hide();
		t3.hide();
		t4.hide();
		t5.hide();
		t6.hide();
		WomenPref.hide();
		MenPref.hide();
		Women.hide();
		Men.hide();
		
		SourceCode fazitCode = lang.newSourceCode(new Coordinates(20,80), "fazit", null, sProps);
		fazitCode.addMultilineCode(fazit, null, null);
		lang.finalizeGeneration();
		return lang.toString();
	}

	/**
	 * function to check if women prefers new partner over old assigned partner
	 **/
	private boolean morePreference(String curPartner, String newPartner, int index) {
		
		for (int i = 0; i < N; i++) {
			
			if (WomenPref.getElement(index, i).equals(newPartner)) {
				/*lang.nextStep();
				MultipleChoiceQuestionModel isThereMorePreference = new MultipleChoiceQuestionModel("isThereMorePreference");
				isThereMorePreference.setPrompt("Is there even better preference for the current woman? Who is that?");
				isThereMorePreference.addAnswer("there is no other preference.", 0, "Wrong");
				isThereMorePreference.addAnswer(WomenPref.getElement(index, i), 1 ,"Correct");
				isThereMorePreference.addAnswer("M1", 1 ,"Correct");
				isThereMorePreference.setNumberOfTries(1);
				lang.addMCQuestion(isThereMorePreference);				
				lang.nextStep();*/
				
				WomenPref.highlightCell(index, i, null, null);
				return true;
			}
			if (WomenPref.getElement(index, i).equals(curPartner)){
				return false;
			}
		}
		return false;
	}

	/** get men index **/
	private int menIndexOf(String str) {
		for (int i = 0; i < N; i++)
			if (Men.getData(i).equals(str))
				// if (men[i].equals(str))
				return i;
		return -1;
	}

	/** get women index **/
	private int womenIndexOf(String str) {
		for (int i = 0; i < N; i++)
			if (Women.getData(i).equals(str))

				return i;

		return -1;
	}

	/** print couples **/
	public void printCouples() {
		System.out.println("Couples are : ");
		for (int i = 0; i < N; i++) {

			System.out.println(WomenPartner.getData(i) + Women.getData(i));
		}
	}

	
	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		men = (String[]) primitives.get("Men");
		women = (String[]) primitives.get("Women");
		menPref = (String[][]) primitives.get("PrefMen");
		womenPref = (String[][]) primitives.get("PrefWomen");
		
		
	
		GaleShapleyMatching gs = new GaleShapleyMatching();
		return this.calcMatches(men, women, menPref,womenPref);
	}

	
	@Override
	public String getAlgorithmName() {
		return "Gale-Shapley Matching Algoithm";
	}

	@Override
	public String getAnimationAuthor() {
		return "Alexander Appel, Zeinab Mohammadkia";
	}

	@Override
	public String getCodeExample() {
		
		return "function stableMatching {"
				 +"\n"
				 +"    Initialize all m ? M and w ? W to free"
				 +"\n"
				 +"    while ? free man m who still has a woman w to propose to {"
				 +"\n"
				 +"       w = m's highest ranked such woman to whom he has not yet proposed"
				 +"\n"
				 +"       if w is free"
				 +"\n"
				 +"         (m, w) become engaged"
				 +"\n"
				 +"       else some pair (m', w) already exists"
				 +"\n"
				 +"         if w prefers m to m'"
				 +"\n"
				 +"            (m, w) become engaged"
				 +"\n"
				 +"            m' becomes free"
				 +"\n"
				 +"         else"
				 +"\n"
				 +"            (m', w) remain engaged"
				 +"\n"
				 +"    }"
				 +"\n"
				 +"}";
				    };
	

	@Override
	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	@Override
	public String getDescription() {
		return "Gale Shapley Algorithm is used to solve the stable marriage problem (SMP)."
				+ " SMP is the problem of finding a stable matching between two sets of elements."
				+ " Given an equal number of men and women to be paired for marriage,"
				+ " each man ranks all the women in order of his preference and each woman ranks"
				+ " all the men in order of her preference."
				+ "A stable set of engagements for marriage is one where no man prefers a woman"
				+ " over the one he is engaged to, where that other woman also prefers that man"
				+ " over the one she is engaged to. I.e. with consulting marriages, there would"
				+ " be no reason for the engagements between the people to change.";
    
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getName() {
		return "Gale-Shapley Matching";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript("Gale Shapley Matching Algoithm",
				"Authors", 800, 600);
	}


}