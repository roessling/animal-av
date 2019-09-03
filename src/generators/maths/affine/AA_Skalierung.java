package generators.maths.affine;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.util.Locale;

import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class AA_Skalierung implements ValidatingGenerator {
    private Language lang;
    private double double_a00;
    private double double_a11;
    private double double_a22;
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
        lang = new AnimalScript("Skalierung", "Brost, Rei�ig", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	double_a00 = (double)primitives.get("double_a00");
    	double_a11 = (double)primitives.get("double_a11");
    	double_a22 = (double)primitives.get("double_a22");
    	intMatrix_urbildpunkte = (int[][])primitives.get("intMatrix_urbildpunkte");
        
        //Abbildungsmatrix zusammensetzen
        double[][] abbm = {{double_a00,1,1},{1,double_a11,1},{1,1,double_a22}};
        
        
        //animation erstellen
        Skalierung_Inhalt i = new Skalierung_Inhalt(lang);
        
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
        
        
        i.setzePropertiesUser(	matrix_normal, matrix_markiert, polygon, text_header, text_normal, text_markiert, rechenzeichen_normal,
				textrechenzeichen_markiert, pfeilProps, achse_props);
        i.setzeProperties();
        
        
        
        i.simulateAllgemein();
        i.skalierung(abbm, intMatrix_urbildpunkte);
        i.skalierung_restlichePunkte_schnell(intMatrix_urbildpunkte, abbm);
        i.letzteFolie(intMatrix_urbildpunkte[0].length);
        
      //Fragen
        lang.nextStep();
        lang.setInteractionType(
        		Language.INTERACTION_TYPE_AVINTERACTION);
        makeFrage(0);
        lang.nextStep();
        makeFrage(1);
        lang.finalizeGeneration();
        lang.nextStep();
        
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
            
            simpleFrage0.addAnswer("Nein", 0, "Ihre Antwort ist nicht richtig. Die Seitenverhältnisse können durch"
    				+ "	eine affinen Skalierung beeinflusst werden.");
            simpleFrage0.addAnswer("Ja", 1, "Ihre Antwort ist richtig.");
            
            simpleFrage0.setPrompt("Ist es mögich, die Seitenverhältnisse eines Quadrates durch eine affinen Skalierung zu verändern?");
            lang.addMCQuestion(simpleFrage0);
            break;
    	case 1:
    		MultipleChoiceQuestionModel simpleFrage1 = new MultipleChoiceQuestionModel("mc1");
            
            simpleFrage1.addAnswer("Nein", 0, "Ihre Antwort ist nicht richtig. Parallele Geraden erhalten ihre"
    				+ "	Parallelität bei.");
            simpleFrage1.addAnswer("Ja", 1, "Ihre Antwort ist richtig.");
            
            simpleFrage1.setPrompt("Bleiben parallele Geraden nach einer affinen Skalierung allgemein weiterhin parallel?");
            lang.addMCQuestion(simpleFrage1);
    		break;
    	}
    }
    
    

    public String getName() {
        return "Skalierung";
    }

    public String getAlgorithmName() {
        return "Affine Abbildung";
    }

    public String getAnimationAuthor() {
        return "Brost, Reißig";
    }

    public String getDescription(){
        return "Dies ist die Animation der Skalierung aus dem Themengebiet Affine Abbildung. " +
        "Die Dimension der Punkte (Number of Rows) ist fest auf den dreidimensionalen Raum gesetzt und darf nicht verändert werden. " +
        "Als Eingabeparameter ist folgendes wählbar: " + 
        "Erstens die Anzahl der Punkte aus denen das Polygon besteht (Number of columns) und die entsprechenden x-, y- und z-Werte (von oben nach unten) der Punkte. Werte von -999 bis 999 werden akzeptiert. Die Punkte müssen im oder gegen den Uhrzeigersinn angeordnet werden. " +
        "Zweitens die Diagonale der Abbildungsmatrix, welche aus insgesamt drei Werten besteht. Double_a00 ist der Wert um welchen der x-Wert gestreckt wird. Double_a011 ist der Wert um welchen der y-Wert gestreckt wird.Double_a22 ist der Wert um welchen der z-Wert gestreckt wird.";
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
	    
	    if ((double)arg1.get("double_a00") > 999 || (double)arg1.get("double_a00") < -999 ||
	    	(double)arg1.get("double_a11") > 999 || (double)arg1.get("double_a11") < -999 ||
	    	(double)arg1.get("double_a22") > 999 || (double)arg1.get("double_a22") < -999	) {
	    	
	    	valid = false;
	    }
	      
		return valid;
	}

}