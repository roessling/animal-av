/*
 * SecretSharingGenerator.java
 * Simon Schmidt, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class SecretSharingGenerator implements Generator {
    private Language lang;
    private int degree;
    private int secret;
    private IntArray coefficients;
	private Coordinates base;
	//used to downscale the coordinate system
	private final double scale=50.0;
	private int numberOfShares;
	private int seed;
	private TextProperties textProps;
	private Text header;
	

    
    public void init(){
        lang = new AnimalScript("Shamir's Secret Sharing", "Simon Schmidt", 800, 600);
        lang.setStepMode(true);
    	lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        TextProperties headerProps=new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));
        header=lang.newText(new Coordinates(20,10), "Shamir's Secret Sharing", "header", null, headerProps);
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.PLAIN, 16));
        lang.newText(new Coordinates(10, 100),
            "Mit Shamir's Secret Sharing kann ein Geheimnis an eine beliebige Anzahl an Personen",
            "description1", null, textProps);
        lang.newText(new Offset(0, 25, "description1",
            AnimalScript.DIRECTION_NW),
            "verteilt werden, ohne dass eine davon das Geheimnis herausfinden kann.",
            "description2", null, textProps);
        lang.newText(new Offset(0, 25, "description2",
            AnimalScript.DIRECTION_NW),
            "Um das Geheimnis zu rekonstruieren muss eine bestimmte Anzahl an Geheimnisträgern",
            "description3", null, textProps);
        lang.newText(new Offset(0, 25, "description3",
            AnimalScript.DIRECTION_NW),
            "zusammenarbeiten.",
            "description4", null, textProps);
        
        lang.newText(new Offset(0, 25, "description4",
                AnimalScript.DIRECTION_NW),
                "Hierzu wird, neben dem Geheimnis, noch die Anzahl an Geheimnisteilen (oder Shares)  n",
                "description5", null, textProps);
        lang.newText(new Offset(0, 25, "description5",
                AnimalScript.DIRECTION_NW),
                "und die Anzahl der zur Rekonstruktion benötigten Shares t angegeben.",
                "description6", null, textProps);
        lang.newText(new Offset(0, 25, "description6",
                AnimalScript.DIRECTION_NW),
                "Anschließend werden die Koeffizienten eines Polynoms vom Grad t-1 zufällig bestimmt,",
                "description7", null, textProps);
        lang.newText(new Offset(0, 25, "description7",
                AnimalScript.DIRECTION_NW),
                "wobei der konstante Teil des Polynoms das Geheimnis ist.",
                "description8", null, textProps);
        lang.newText(new Offset(0, 25, "description8",
                AnimalScript.DIRECTION_NW),
                "Zuletzt wird die Funktion an n verschiedenen Stellen ausgewertet und die resultierenden ",
                "description9", null, textProps);
        lang.newText(new Offset(0, 25, "description9",
                AnimalScript.DIRECTION_NW),
                "Wertepaare an die Geheimnisträger verteilt.",
                "description10", null, textProps);
        
        
        
        lang.nextStep();
        lang.hideAllPrimitives();
        header.show();
        //create the coordinate system
        base= new Coordinates(20,440);
        for(int i=50; i<=400; i+=50){
        	Node[] yLabelsLower= new Node[2];
        	Node[] yLabelsUpper= new Node[2];
        	Node[] xLabels= new Node[2];
        	yLabelsLower[0]=new Offset(-5, i, base, " ");
        	yLabelsLower[1]=new Offset(5,i,base," ");
        	
        	yLabelsUpper[0]=new Offset(-5, -i, base, " ");
        	yLabelsUpper[1]=new Offset(5,-i,base," ");
        	
        	xLabels[0]=new Offset(i, -5, base, " ");
        	xLabels[1]=new Offset(i,5,base," ");
        	lang.newPolyline(yLabelsLower, null, null);
        	lang.newPolyline(yLabelsUpper, null, null);
        	lang.newPolyline(xLabels, null, null);
        	lang.newText(new Offset(-10,i,base,null), String.valueOf(i/50), null, null);
        	lang.newText(new Offset(-10,-i,base,null), String.valueOf(i/50), null, null);
        	lang.newText(new Offset(i,10,base,null), String.valueOf(i/50), null, null);


        }
        Node[] coordinateSystem = { new Coordinates(20,800),new Coordinates(20, 40), base, new Coordinates(420, 440) };
		lang.newPolyline(coordinateSystem, "coordinateSystem", null);
        lang.nextStep();
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        ArrayProperties arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        degree = (Integer)primitives.get("requiredShares");
        numberOfShares=(Integer)primitives.get("numberOfShares");
        secret = (Integer)primitives.get("secret");
        seed= (Integer)primitives.get("seed");
		lang.newText(new Coordinates(450, 40), "1. Erzeuge die Koeffizienten des Polynoms", "text1", null);
        coefficients=lang.newIntArray(new Coordinates(450, 70), new int[degree], "doubleArray",
				null, arrayProps);
        lang.nextStep();
        createPolynomial();
        lang.hideAllPrimitivesExcept(header);
        //Ending screen
        lang.newText(new Coordinates(10, 100),
                "Die Rekonstruktion erfolgt mittels Polynominterpolation, weshalb das Ergebnis eindeutig ist,",
                "description1", null, textProps);
            lang.newText(new Offset(0, 25, "description1",
                AnimalScript.DIRECTION_NW),
                "solange die Anzahl der Stützstellen (Shares) größer ist als der Grad des Polynoms.",
                "description2", null, textProps);
            lang.newText(new Offset(0, 25, "description2",
                    AnimalScript.DIRECTION_NW),
                    "Da reelle Zahlen digital nicht dargestellt werden können, findet die Berechnung normalerweise",
                    "description3", null, textProps);
            lang.newText(new Offset(0, 25, "description3",
                    AnimalScript.DIRECTION_NW),
                    "in endlichen Körpern (also modulo einer Primzahl) statt.",
                    "description4", null, textProps);
            lang.newText(new Offset(0, 25, "description4",
                    AnimalScript.DIRECTION_NW),
                    "Darauf wurde hier verzichtet, da dies die Animation unübersichtlich machen würde.",
                    "description5", null, textProps);
        lang.finalizeGeneration();
        return lang.toString();
    }
    /**
     * creates the coefficients for the polynomial
     */
    private void createPolynomial() {
		coefficients.put(0, (secret*50), null, null);
		lang.nextStep();
		Random rand=new Random(seed);
		for (int i = 1; i < coefficients.getLength(); i++) {
			int coeff=rand.nextInt(20)-10;
			//make sure the last coefficient is not zero
			while(i==(coefficients.getLength()-1) && coeff==0){
				coeff=rand.nextInt(20)-10;
			}
			coefficients.put(i, coeff, null, null);
			lang.nextStep();
		}
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("MC1");
        q1.setPrompt("Dürfen die Koeffizienten den Wert 0 haben?");
   
        q1.addAnswer("Ja, alle", 0, "Falsch!");
        q1.addAnswer("Nein, keiner", 0, "Falsch!");
        q1.addAnswer("Alle bis auf den zur höchsten Potenz zugehörigen", 1, "Richtig!");
   
        q1.setNumberOfTries(1);
   
        lang.addMCQuestion(q1);
        lang.nextStep();
		plot();
	}
    /**
     * plots the polynomial
     */
    public void plot(){
		Node[] verts=new Node[400];
		verts[0]=new Offset(0, secret, base, null );
		double val=0;
		int last=410;
		for(int i=0;i<400; i++){
			val=evaluatePolynomial(i);
			
			verts[i]=new Offset(i,(int)(-val), base ,null);
			if(Math.abs(val)>400){
				last=i;
				break;
			}
			
			
		}
		lang.newPolyline(verts, null, null);
		lang.nextStep();
		getPoints(last);
		
	}
    /**
     *  Determines the shares
     * @param last the last visible point of the function
     */
    private void getPoints(int last){
    	 MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("MC2");
         q2.setPrompt("Spielt der Abstand zwischen den Stützstellen eine Rolle?");
    
         q2.addAnswer("Ja", 0, "Falsch!");
         q2.addAnswer("Nein", 1, "Richtig!");
    
         q2.setNumberOfTries(1);
    
         lang.addMCQuestion(q2);
         lang.nextStep();
        //set distance between points
    	int pointDistance=last/numberOfShares;
    	double[][] points=new double[numberOfShares][2];
    	DoubleMatrix values=lang.newDoubleMatrix(new Coordinates(440, 170 ), points, "values", null);
    	int col=0;
    	int i=10;
    	lang.newText(new Coordinates(450,120), "2. Bestimme die Punktepaare", "text2", null);
    	lang.newText(new Coordinates(460,145), "X     Y", "text3", null);
    	while(col<numberOfShares){
    		double value=evaluatePolynomial(i);
    		Node[] verts={new Offset(i, 0, base ,null),new Offset(i, (int) Math.ceil(-value), base ,null)};
    		lang.newPolyline(verts, " ", null);
    		values.put(col, 0,  (i/scale), null, null);
    		values.put(col, 1,  (value/scale), null, null);
    		col++;
    		i+=pointDistance;
    		lang.nextStep();
    	}
    	lang.newText(new Coordinates(450,270), "3. Verteile die Paare", "text2", null);
    }
    
    /**
     * Evaluates the Polynomial at a given point
     * @param xValue the point at which the value is calculated
     * @return the value of the polynomial
     */
    private double evaluatePolynomial(int xValue){
    	double value=0;
		for(int j=0; j<coefficients.getLength();j++){
			value=value+coefficients.getData(j)*Math.pow((xValue/scale),j);
			
		}
		return value;
    }
    public String getName() {
        return "Shamir's Secret Sharing";
    }

    public String getAlgorithmName() {
        return "Shamir's Secret Sharing";
    }

    public String getAnimationAuthor() {
        return "Simon Schmidt";
    }

    public String getDescription(){
        return "Shamir's Secret Sharing wird zur Aufteilung eines Geheimnisses an n Geheimnistr\u00e4ger verwendet."
 +"\n"
 +"Um das Geheimnis zu rekonstruieren m\u00fcssen t<=n dieser Geheimnistr\u00e4ger ihre jeweiligen Teile "
 +"\n"
 +"zusammensetzen. Die Geheimnisteile sind Wertepaare und das Geheimnis der konstante Teil eines"
 +"\n"
 +" Polynoms vom Grad t-1. Die Rekonstruktion erfolgt \u00fcber eine Polynominterpolation mit den"
 +"\n"
 +"t Wertepaaren, wodurch das gesuchte Polynom eindeutig bestimmt wird.";
    }

    public String getCodeExample(){
        return "test" ;
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}