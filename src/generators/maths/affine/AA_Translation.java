package generators.maths.affine;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;

public class AA_Translation implements ValidatingGenerator {
    private Language lang;
    private int[][] intMatrix_transvektor;
    private int[][] intMatrix_urbildpunkte;
   
    private MatrixProperties matrix_normal;
    private MatrixProperties matrix_markiert;
    private PolygonProperties polygon;
    private TextProperties text_header;
    private TextProperties text_normal;
    private TextProperties text_markiert;
    private TextProperties rechenzeichen_normal;
    private TextProperties textrechenzeichen_markiert;
    private PolylineProperties pfeilProps;
    private PolylineProperties achse_props;

    public void init(){
        lang = new AnimalScript("Translation", "Brost, Reissig", 50, 5);
    }
    
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	intMatrix_transvektor = (int[][])primitives.get("intMatrix_transvektor");
    	intMatrix_urbildpunkte = (int[][])primitives.get("intMatrix_urbildpunkte");
       
        
        matrix_markiert = (MatrixProperties)props.getPropertiesByName("matrix_markiert");
        matrix_normal = (MatrixProperties)props.getPropertiesByName("matrix_normal");
        rechenzeichen_normal = (TextProperties)props.getPropertiesByName("rechenzeichen_normal");
        text_markiert = (TextProperties)props.getPropertiesByName("text_markiert");
        polygon = (PolygonProperties)props.getPropertiesByName("polygon");
        text_header = (TextProperties)props.getPropertiesByName("text_header");
        text_normal = (TextProperties)props.getPropertiesByName("text_normal");
        intMatrix_urbildpunkte = (int[][])primitives.get("intMatrix_urbildpunkte");
        textrechenzeichen_markiert = (TextProperties)props.getPropertiesByName("textrechenzeichen_markiert");
        pfeilProps = (PolylineProperties)props.getPropertiesByName("pfeilProps");
        achse_props = (PolylineProperties)props.getPropertiesByName("achse_props");
        
        Translation_Inhalt i = new Translation_Inhalt(lang);
        
        i.setzePropertiesUser(	matrix_normal, matrix_markiert, polygon, text_header, text_normal, text_markiert, rechenzeichen_normal,
				textrechenzeichen_markiert, pfeilProps, achse_props);

        i.setzeProperties();
        
        i.simulateAllgemein();
        i.translation(intMatrix_urbildpunkte, intMatrix_transvektor);
        i.translation_restlichePunkte_schnell(intMatrix_urbildpunkte, intMatrix_transvektor);
        i.letzteFolie(intMatrix_urbildpunkte[0].length);
        
        lang.setInteractionType(
        		Language.INTERACTION_TYPE_AVINTERACTION);
        		
//        FillInBlanksQuestionModel algoYear = 
//			new FillInBlanksQuestionModel("Antwort");
//			algoYear.setPrompt("Ist diese Animation brilliant oder brilliant?");
//			algoYear.addAnswer("brilliant", 1, "David A. Huffman im Jahre 1952");
//			lang.addFIBQuestion(algoYear);
        
        lang.nextStep();
        
        makeFrage(0);
        lang.nextStep();
        makeFrage(1);
        lang.nextStep();
	    	
		lang.finalizeGeneration();
        return lang.toString();
    }
    
    
    /**
     * Lagert den code aus generare heraus.
     * @param nr die gewünschte frage, die gestellt werden soll
     */
    public void makeFrage(int nr) {
    	switch (nr) {
    	case 0:
    		MultipleChoiceQuestionModel simpleFrage0 = new MultipleChoiceQuestionModel("mc0");
            
            simpleFrage0.addAnswer("Ja", 0, "Ihre Antwort ist nicht richtig. Die Seitenverhältnisse bleiben nach einer"
    				+ "	Translation erhalten.");
            simpleFrage0.addAnswer("Nein", 1, "Ihre Antwort ist richtig.");
            
            simpleFrage0.setPrompt("Ist es mögich, die Seitenverhältnisse eines Quadrates durch eine affine Transformation zu verändern?");
            lang.addMCQuestion(simpleFrage0);
            break;
    	case 1:
    		MultipleChoiceQuestionModel simpleFrage1 = new MultipleChoiceQuestionModel("mc1");
            
            simpleFrage1.addAnswer("Nein", 0, "Ihre Antwort ist nicht richtig. Parallele Geraden erhalten ihre"
    				+ "	Parallelität bei.");
            simpleFrage1.addAnswer("Ja", 1, "Ihre Antwort ist richtig.");
            
            simpleFrage1.setPrompt("Bleiben parallele Geraden nach einer affinen Translation allgemein weiterhin parallel?");
            lang.addMCQuestion(simpleFrage1);
    		break;
    	}
    }
    

    public String getName() {
        return "Translation";
    }

    public String getAlgorithmName() {
        return "Affine Abbildung";
    }

    public String getAnimationAuthor() {
        return "Brost, Reissig";
    }

    public String getDescription(){
        return "Dies ist die Animation der Translation aus dem Themengebiet Affine Abbildung. " +
           "Die Dimension der Punkte (Number of Rows) ist fest auf den dreidimensionalen Raum gesetzt und darf nicht verändert werden. " +
           "Als Eingabeparameter ist folgendes wählbar: " + 
           "Erstens die Anzahl der Punkte aus denen das Polygon besteht (Number of columns) und die entsprechenden x-, y- und z-Werte (von oben nach unten) der Punkte. Werte von -999 bis 999 werden akzeptiert. Die Punkte müssen im oder gegen den Uhrzeigersinn angeordnet werden. " +
           "Zweitens den Verschiebungsvektor, welcher aus insgesamt drei Werten besteht. Er gibt an um welchen Wert das Polygon in die x-, y- und z-Achse (von oben nach unten) verschoben wird.";
    }

    public String getCodeExample(){
        return "Nicht erforderlich.";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    @Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
    	
    	boolean valid = true;
    	int maxgroesse = 1000;
    	
	    int[][] test =	 (int[][]) arg1.get("intMatrix_urbildpunkte");
	    
	    if (test.length <= 2) {
			   valid = false;
		}
	    
	    for (int i=0 ; i< test.length ; i++) {
	    	for (int j=0 ; j< test[0].length ; j++) {
	    		if ((test[i][j] >= maxgroesse) || (test[i][j] <= (-maxgroesse))) {
	    			valid = false;
	    		}
	    	}
	    }
	    
	    int[][] test2 =	 (int[][]) arg1.get("intMatrix_transvektor");
	    
	    if (test2.length <= 2) {
			   valid = false;
		}
	    
	    for (int i=0 ; i< test2.length ; i++) {
	    	for (int j=0 ; j< test2[0].length ; j++) {
	    		if ((test2[i][j] >= maxgroesse) || (test2[i][j] <= (-maxgroesse))) {
	    			valid = false;
	    		}
	    	}
	    }
    	
    	
    	
		return valid;
	}


	

}