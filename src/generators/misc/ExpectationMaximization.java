/*
 * ExpectationMaximization.java
 * Claudia Lölkes, Verena Sieburger, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.Point;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class ExpectationMaximization implements ValidatingGenerator {
    private Language lang;
    private Color ClusterColor4;
    private Color ClusterColor3;
    private Color ClusterColor2;
    private SourceCodeProperties CodeText2;
    private Color ClusterColor1;
    private TextProperties DescriptionText;
    private Color ClusterColor5;
    private int[] Priors;
    private int[] Sigma10;
    private int[] Sigma00;
    private int[] Sigma11;
    private int[] Sigma01;
    private int Iterationen;
    private int NumOfClusters;
    private int[][] My;
    private int[][] Datenpunkte;
   
	private int [][] my_before = new int[5][2];
	private Point[] sigmapoints = new Point[630];
	private Color[] clusterColors = {new Color(255, 0,0), new Color(0, 0,255),new Color(0, 255,0),new Color(255,127,36),new Color(255,255,0)}; 
	
	private static final String DESCRIPTION = "Expectation Maximization ist ein Cluster "
			+ "Algorithmus. Gegeben ist ein Array mit Datenpunkten. Der Algorithmus bestimmt "
			+ "dann iterativ Gaussverteilungen, die die Datenpunkte am besten "
			+ "repräsentiern. Die Gaussverteilungen für jedes Cluster geben dann die "
			+ "Wahrscheinlichkeit an, dass ein bestimmter Datenpunkt zu dem jeweiligen Cluster "
			+ "gehört."
			+ "Der Algotithmus wird initalisiert, indem für alle Cluster anfangs Mittelwerte und "
			+ "Kovarianzmatritzen angegeben werden. Außerdem kann man mit der Variable Pi angeben, "
			+ "wie die Prior Wahrscheinlichkeiten für jedes Cluster aussehen. ";
	
	private static final String MYDESCRIPTION = "Expectation Maximization ist ein Cluster "
			+ "Algorithmus. Gegeben ist ein Array mit Datenpunkten. Der Algorithmus bestimmt "
			+ "dann iterativ numOfClusters Gaussverteilungen, die die Datenpunkte am besten "
			+ "repräsentiern. Die Gaussverteilungen für jedes Cluster geben die "
			+ "Wahrscheinlichkeit an, dass ein bestimmter Datenpunkt zu dem jeweiligen Cluster "
			+ "gehört. Eine Gaussverteilung ist vollständig bestimmt, wenn man den Mittelwert "
			+ "und die Kovarianz kennt."
			+ "Der Algotithmus wird initalisiert, indem für alle Cluster anfangs Mittelwerte und "
			+ "Kovarianzmatritzen angegeben werden. Außerdem kann man mit der Variable Pi angeben, "
			+ "wie die Prior Wahrscheinlichkeiten für jedes Cluster aussehen. "
			+ "Für jedes Cluster wird der Expectation und der Maximization Schritt durchgeführt. "
			+ "Der erste Schritt ist der Expectation-Schritt. Für jeden Datenpunkt wird die "
			+ "Posterior-Wahrscheinlichkeit p(cluster_k|x_n) bestimmt, dass das aktuelle Cluster "
			+ "vorliegt, wenn man den Datenpunkt x_n gegeben hat. Im zweiten Schritt werden die Parameter "
			+ "der Gaussverteilung bestimmt, sodass die log-likelihood der Cluster maximiert wird. "
			+ "Die Schritte werden für eine feste Anzahl an Iterationen ausgeführt. Alternativ "
			+ "kann man auch abbrechen, wenn sich die Parameter der Verteilungen nicht mehr "
			+ "wesentlich ändern";
	
	private static final String SOURCE_CODE = "function expectationMaximization(numOfClusters, data, pi, mys, sigmas, iterations)" // 0
			+ "\n begin" // 1
			+ "\n  for i = 0 to iterations "
			+ "\n	for int c = 0 to numOfClusters"
			+ "\n	  for x = 0 to length(data)"
			+ "\n		alpha[c][x] = pi[c]*Gaussian(data[x] | my[c],sig[c]) "
			+ "				/ Sum(pi * Gaussian(data[x] | my, sig))"
			+ "\n		"
			+ "\n	   N[c] = sum(alpha[c])"
			+ "\n 	   my[c] = 1 / N[c] * sum(alpha[c]*data)"
			+ "\n 	   sigma[c] = 1 / N[c] * sum(alpha[c]*(data-my[c])^2)"
			+ "\n 	   pi[c] = N[c] / numOfDataPoints"
			+ "\n end"; 

    public void init(){
        lang = new AnimalScript("Expectation Maximization mit der Gaussverteilung", "Claudia Lölkes, Verena Sieburger", 800, 600);
        lang.setStepMode(true);
    }
    
    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives){
    	boolean validInput = true;
        Priors = (int[])primitives.get("Priors");
        int sum =0;
        for(int i = 0; i<Priors.length;i++){
        	sum +=Priors[i];
        }
        if (sum !=100)
        	validInput = false;
       
        Iterationen = (Integer)primitives.get("Iterationen");
        if (Iterationen>10 || Iterationen<1)
        	validInput = false;
        
        NumOfClusters = (Integer)primitives.get("NumOfClusters");
        if (NumOfClusters>5 || NumOfClusters<1)
        	validInput = false;
        
        My = (int[][])primitives.get("My");
        if(My.length != 2 && My[0].length != NumOfClusters)
        	validInput = false;
        
        Datenpunkte = (int[][])primitives.get("Datenpunkte");
    	if(Datenpunkte.length!=2)
    		validInput = false;
    	return validInput;
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        ClusterColor4 = (Color)primitives.get("ClusterColor4");
    	clusterColors[3] = ClusterColor4; 
        ClusterColor3 = (Color)primitives.get("ClusterColor3");
        clusterColors[2] = ClusterColor3; 
        ClusterColor2 = (Color)primitives.get("ClusterColor2");
        clusterColors[1] = ClusterColor2; 
        CodeText2 = (SourceCodeProperties)props.getPropertiesByName("CodeText2");
        ClusterColor1 = (Color)primitives.get("ClusterColor1");
        clusterColors[0] = ClusterColor1; 
        DescriptionText = (TextProperties)props.getPropertiesByName("DescriptionText");
        ClusterColor5 = (Color)primitives.get("ClusterColor5");   
        clusterColors[4] = ClusterColor5; 
    	Priors = (int[])primitives.get("Priors");
        Sigma10 = (int[])primitives.get("Sigma10");
        Sigma00 = (int[])primitives.get("Sigma00");
        Sigma11 = (int[])primitives.get("Sigma11");
        Sigma01 = (int[])primitives.get("Sigma01");
        Iterationen = (Integer)primitives.get("Iterationen");
        NumOfClusters = (Integer)primitives.get("NumOfClusters");
        My = (int[][])primitives.get("My");
        Datenpunkte = (int[][])primitives.get("Datenpunkte");
        
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		double[][] x = new double [Datenpunkte[0].length][Datenpunkte.length];
		for (int i = 0; i<Datenpunkte[0].length; i++){
			for (int j = 0; j<Datenpunkte.length; j++){
				x[i][j] = Datenpunkte[j][i]*0.1;
			}
		}
		startEM(x);
        System.out.println(lang);
        return lang.toString();
    }
    
    public void startEM(double [][] x){
		//Titel
		RectProperties titleRectProps = new RectProperties();
		titleRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 150,150)); //TODO anpassen
		titleRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 150,150));
		titleRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		Rect titleRect = lang.newRect(new Coordinates(10,20), new Coordinates(480,55), "titleRect", null,titleRectProps);
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 26));
		titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0,150));
		Text titel = lang.newText(new Coordinates(20, 20), "Expectation Maximization Algorithm", "Titel", null, titleProps);
		
		//erstelle Koordinatensystem
		PolylineProperties koordProps = new PolylineProperties(); 
		koordProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		koordProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
		Coordinates[] koordPos = {new Coordinates(20,70),new Coordinates(20,370),new Coordinates(320,370)};
		Polyline koord = lang.newPolyline(koordPos, "koord", null,koordProps);
		TextProperties xyProps = new TextProperties();
		xyProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 16));
		Text xText = lang.newText(new Coordinates(300, 375), "x", "x", null, xyProps);
		Text yText = lang.newText(new Coordinates(5, 85), "y", "y", null, xyProps);
		
		// plotte Punkte im Kooridinatensystem
		RectProperties datapointProps = new RectProperties();	
		for(int i = 0; i<x.length;i++){
			Rect p = lang.newRect(new Coordinates(20+(int)(x[i][0]*30),370-(int)(x[i][1]*30)), new Coordinates(22+(int)(x[i][0]*30),372-(int)(x[i][1]*30)), "point"+i, null);
		}
	   
	    //Erstelle Quellcode
	    SourceCodeProperties scProps = CodeText2;

	    SourceCode sc = lang.newSourceCode(new Coordinates(60, 400), "sourceCode", null, scProps);

	    // Zeilen hinzufügen
	    sc.addCodeLine("function expectationMaximization(numOfClusters, data, pi, mys, sigmas, iterations)", null, 0, null);
	    sc.addCodeLine("begin", null, 0, null); // 1
	    sc.addCodeLine("for i = 0 to iterations", null, 1, null);
	    sc.addCodeLine("for int c = 0 to numOfClusters ", null, 2, null);
	    sc.addCodeLine("for x = 0 to length(data) ", null, 3, null);
	    sc.addCodeLine("alpha[c][x] = pi[c]*Gaussian(data[x] | my[c],sig[c])", null, 4, null);
	    sc.addCodeLine("/ Sum(pi * Gaussian(data[x] | my, sig))", null, 6, null);
	    sc.addCodeLine("N[c] = sum(alpha[c])", null, 3, null);
	    sc.addCodeLine("my[c] = 1 / N[c] * sum(alpha[c]*data)", null, 3, null);
	    sc.addCodeLine("sigma[c] = 1 / N[c] * sum(alpha[c]*(data-my[c])^2)", null, 3, null);
	    sc.addCodeLine("pi[c] = N[c] / numOfDataPoints", null, 3, null);
	    sc.addCodeLine("end", null, 0, null);
	    
	    //TODO Einleitung
	    RectProperties textFrame  = new RectProperties();
	    textFrame.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(200, 200,200));
	    textFrame.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    RectProperties textFrame2  = new RectProperties();
	    textFrame2.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(250, 250,250));
	    textFrame2.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		Rect textFrameRect = lang.newRect(new Coordinates(400,100), new Coordinates(900,340), "textRect", null,textFrame);
		Rect textFrameRect2 = lang.newRect(new Coordinates(405,105), new Coordinates(895,335), "textRect2", null,textFrame2);
			//IntoText
		TextProperties descrProps = DescriptionText;
		descrProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		//descrProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0, 0,150));
		Text descr1 = lang.newText(new Coordinates(420, 120), getMyDescription().substring(0,76), "Descr", null, descrProps);
		Text descr2 = lang.newText(new Coordinates(420, 140), getMyDescription().substring(76,147), "Descr2", null, descrProps);
		Text descr3 = lang.newText(new Coordinates(420, 160), getMyDescription().substring(147,215), "Descr3", null, descrProps);
		Text descr4 = lang.newText(new Coordinates(420, 180), getMyDescription().substring(215,289), "Descr4", null, descrProps);
		Text descr5 = lang.newText(new Coordinates(420, 200), getMyDescription().substring(289,359)+"-", "Descr5", null, descrProps);
		Text descr6 = lang.newText(new Coordinates(420, 220), getMyDescription().substring(359,428), "Descr6", null, descrProps);
		Text descr7 = lang.newText(new Coordinates(420, 240), getMyDescription().substring(428,444), "Descr7", null, descrProps);
		lang.nextStep();
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("MC");
        q1.setPrompt("Zu welcher Kategorie zählt der EM-Algorithmus ?");
        q1.addAnswer("supervised learning", 0, "Falsch. Beim supervised learning gibt es Trainingsdaten mit korrekten Labeln ");
        q1.addAnswer("unsupervised learning", 1, "Richtig. Der EM-Algorithmus ist ein Clusteralgorithmus, bei dem ohne gelablede Trainingsdaten Cluster auf den Daten gefunden werden. ");
        q1.addAnswer("reinforcement learning", 0, "Falsch. Beim reinforcement learning wird eine Belohnungsfunktion maximiert.");
        q1.setNumberOfTries(1);
        lang.addMCQuestion(q1);
	    //Initialisierung text
	    sc.highlight(0, 0, false);
	    descr1.setText(getMyDescription().substring(444,522), null, Timing.MEDIUM);
	    descr2.setText(getMyDescription().substring(522,588), null, Timing.MEDIUM);
	    descr3.setText(getMyDescription().substring(589,662), null, Timing.MEDIUM);
	    descr4.setText(getMyDescription().substring(663,672), null, Timing.MEDIUM);
	    descr5.hide();
	    descr6.hide();
	    descr7.hide();
	    Text[] descr = {descr1, descr2, descr3, descr4, descr5, descr6, descr7};
		double[] pi0 = new double [NumOfClusters];
		double[][][] sig0 = new double [NumOfClusters][2][2];
		double[][] my0 = new double [NumOfClusters][2];
		for (int i = 0; i<NumOfClusters; i++){
			pi0[i]= Priors[i]*0.01;
			my0[i][0] = My[0][i]*0.1;
			my0[i][1] = My[1][i]*0.1;
			sig0[i][0][0] = Sigma00[i]*0.1;
			sig0[i][0][1] = Sigma01[i]*0.1;
			sig0[i][1][0] = Sigma10[i]*0.1;
			sig0[i][1][1] = Sigma11[i]*0.1;
			System.out.println("my = ("+my0[i][0]+","+my0[i][1]+")");
			System.out.println("pi = ("+pi0[i]+","+pi0[i]+")");
		}
		//plot initial Gaussians
		Rect [] myPrim = new Rect [NumOfClusters];
		for (int c=0; c<NumOfClusters; c++){
			System.out.println(c);
			System.out.println("length: " + my_before.length);
			my_before[c][0] = 20+(int)(my0[c][0]*30);
			my_before[c][1] = 370-(int)(my0[c][1]*30);
			RectProperties myProps = new RectProperties();
			myProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, clusterColors[c]); 
			myPrim[c] = lang.newRect(new Coordinates(my_before[c][0],my_before[c][1]), new Coordinates(my_before[c][0]+5,my_before[c][1]+5), "my0", null,myProps);
		}
		executeEM(NumOfClusters, x, pi0,my0, sig0,myPrim, sc, descr);
		sc.hide();
		MultipleChoiceQuestionModel q4 = new MultipleChoiceQuestionModel("MC");
        q4.setPrompt("Expectation-Maximazation ist ein _______ Verfahren");
        q4.addAnswer("hierarchisches", 0, "Falsch. Es wird keine Hierarchie von Clustern aufgebaut. ");
        q4.addAnswer("partitionierendes", 1, "Richtig. Die Anzahl der Cluster muss bestimmt werden, bevor der Algorithmus angewendet werden kann ");
        q4.addAnswer("dichtebasiertes", 0, "Falsch. Das Hauptkriterium ist nicht, dass dichte Gebiete durch nicht so dichte Gebiete voneinander getrennt werden");
        q4.setNumberOfTries(1);
        lang.addMCQuestion(q4);
	    descr1.show();
	    descr2.show();
	    descr3.show();
	    descr4.show();
	    descr5.show();
	    descr6.show();
	    descr7.show();
	    descr1.setText("Wir haben gesehen, dass der EM-Algorithmus Cluster in Datenpunkten ", null, Timing.MEDIUM);
	    descr2.setText("finden kann. In dem hier gezeigten Beispiel wurden die Cluster durch ", null, Timing.MEDIUM);
	    descr3.setText("Gaußverteilungen repräsentiert. Theoretisch können auch andere ", null, Timing.MEDIUM);
	    descr4.setText("Verteilungen verwendet werden. Die Gaußvereilung hat allerdings den ", null, Timing.MEDIUM);
	    descr5.setText("Vorteil, dass die Berechnungen in geschlossener Form möglich sind.", null, Timing.MEDIUM);
	    descr6.setText("Alternative Algorithmen für das Clustering wären zum Beispiel k-Means", null, Timing.MEDIUM);
	    descr7.setText("oder Maximum Margin Clustering.", null, Timing.MEDIUM);
		lang.nextStep();
		lang.finalizeGeneration();
	}
    
    public double twoDimGaussian(double[] x, double[] my,
			double[][] sigma) {
		double det_sig = sigma[0][0] * sigma[1][1] - sigma[0][1] * sigma[1][0];
		double fac = 1 / (sigma[0][0] * sigma[1][1] - sigma[0][1] * sigma[1][0]);
		double diff1 = x[0] - my[0];
		double diff2 = x[1] - my[1];
		double prod = fac
				* ((diff1 * sigma[1][1] - diff2 * sigma[1][0]) * diff1 + (diff2
						* sigma[0][0] - diff1 * sigma[0][1])
						* diff2);
		return 1 / Math.sqrt(Math.pow(2 * Math.PI, 2) * det_sig)
				* Math.exp(-0.5 * (prod));
	}
	
	private int convertToAnimalKoords(int dim, double val){
		if(dim == 0)
			return 20+(int)(val*30);
		else return 370-(int)(val*30);
					
	}
	
	public void plotMy(int ClusterNr, Rect myPrim,double[] my){		
		//plot mean
		myPrim.moveBy("translate",convertToAnimalKoords(0,my[0])-my_before[ClusterNr][0], convertToAnimalKoords(1,my[1])-my_before[ClusterNr][1], Timing.MEDIUM, Timing.MEDIUM);
		my_before[ClusterNr][0] = convertToAnimalKoords(0,my[0]);
		my_before[ClusterNr][1] = convertToAnimalKoords(1,my[1]);
	}
	
	public void plotSigma(int ClusterNr, Rect myPrim,double[] my, double [][] sigma){		
		//plot covariance ellipse
		double[] eigenval = eigenvalues(sigma);
		double alpha;
		double chisquare_val = 2.4477;
		double a=chisquare_val*Math.sqrt(Math.abs(eigenval[0]));
		double b=chisquare_val*Math.sqrt(Math.abs(eigenval[1]));
		double ellipse_x_r;
		double ellipse_y_r;
		PointProperties ellipseProps = new PointProperties();
		ellipseProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, myPrim.getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY));
		if (eigenval[0]>eigenval[1])
			alpha = alpha(0,sigma,eigenval[0]);
		else 
			alpha = alpha(1,sigma,eigenval[1]);
		int num = 0;
		for (int sigmap = ClusterNr*126; sigmap < ClusterNr*126+126; sigmap++){
			if (sigmapoints[sigmap] != null)
					sigmapoints[sigmap].hide();
		}
		for (double theta = 0; theta <2*Math.PI; theta=theta+0.05){
			// the ellipse in x and y coordinates 
			ellipse_x_r  = a*Math.cos( theta );
			ellipse_y_r  = b*Math.sin( theta );
			//rotate ellipse by alpha and shift by my
			ellipse_x_r = ellipse_x_r*Math.cos(alpha)-ellipse_y_r*Math.sin(alpha) + my[0];
			ellipse_y_r = ellipse_y_r*Math.sin(alpha)+ellipse_y_r*Math.cos(alpha) + my[1];
			//convert to animal koordinates
			ellipse_x_r = convertToAnimalKoords(0,ellipse_x_r);
			ellipse_y_r = convertToAnimalKoords(1,ellipse_y_r);
			//draw point
			sigmapoints[num+ClusterNr*126]  = lang.newPoint(new Coordinates((int)ellipse_x_r,(int)ellipse_y_r), "point", null,ellipseProps);
			num++;
		}		
	}
	
	private double[] eigenvalues(double [][] sigma){
		double t = sigma[0][0]+sigma[1][1];
		double d = sigma[0][0]*sigma[1][1]-sigma[0][1]*sigma[1][0];
		double [] eigenvals = {t*0.5+ Math.sqrt(Math.pow(t, 2)/(4.0-d)),t*0.5- Math.sqrt(Math.pow(t, 2)/(4.0-d))};
		return eigenvals;
	}
	
	private double alpha(int nr, double[][] sigma, double eigenval){
		double angle;
		double v1 = 1.0/(Math.sqrt(1+Math.pow(eigenval-sigma[0][0],2)/Math.pow(sigma[0][1],2)));
		double v2 = (eigenval-sigma[0][0])/(sigma[0][1]*Math.sqrt(1+Math.pow(eigenval-sigma[0][0],2)/Math.pow(sigma[0][1],2)));
		angle = Math.atan2(v2, v1);
		if(angle < 0){
		    angle = angle + 2*Math.PI;
		}
		return angle;
	}
	
	private void hideText(Text[] t){
		for (int i = 0; i < t.length; i++){
			t[i].hide();
		}
	}
	
	public void executeEM(int numOfClusters, double[][] x, double[] pi,
			double my[][], double sigma[][][], Rect[] myPrim, SourceCode codeSupport, Text[] descr) {
		int numOfDataPoints = x.length;
		double normalization;
		double weightedMyDist11;
		double weightedMyDist12;
		double weightedMyDist21;
		double weightedMyDist22;
		double weightedDataPoints_x;
		double weightedDataPoints_y;
		double N_k;
		double[][] alpha = new double[numOfDataPoints][numOfClusters];
		TextProperties icProps = new TextProperties();
		icProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 16));
		Text iText = lang.newText(new Coordinates(420, 80), "", "i", null, icProps);
		Text cText = lang.newText(new Coordinates(480, 80), "", "c", null, icProps);
		Text posteriorText = lang.newText(new Coordinates(480, 260), "Posterior = Prior*Likelihood / Normalization", "posterior", null, icProps);
		posteriorText.hide();
		Text NText = lang.newText(new Coordinates(470, 260), "N[c] = ", "N", null, icProps);
		NText.hide();
		Text N2Text = lang.newText(new Coordinates(470, 280), "N[c] = ", "N2", null, icProps);
		N2Text.hide();
		TextProperties EProps = new TextProperties();
		EProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 18));
		EProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0,0,200));
		Text eText = lang.newText(new Coordinates(420, 110), "Expectation Step", "E", null, EProps);
		eText.hide();
		TextProperties MProps = new TextProperties();
		MProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 18));
		MProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(200,0,0));
		Text mText = lang.newText(new Coordinates(420, 110), "Maximization Step", "M", null, MProps);
		mText.hide();
		lang.nextStep();
		codeSupport.unhighlight(0,0,false);
		for (int j = 0; j < Iterationen; j++) {
			if (j == 0){
				MultipleSelectionQuestionModel q2 = new MultipleSelectionQuestionModel("MS");
		        q2.setPrompt("Welche Möglichkeiten gibt es den Algorithmus zu Terminieren");
		        q2.addAnswer("Feste Iterationszahl", 1, "Eine feste Iterationszahl ist zum Beispiel sinnvoll, wenn der Algorithmus mit verschiedenen Eingaben verglichen werden soll");
		        q2.addAnswer("Wenn my einen bestimmten Wert erreicht hat", 0, " Ein fester Wert von my macht keinen Sinn, da der Wert von den Eingabedaten abhängt ");
		        q2.addAnswer("Wenn sich my weniger als ein Schwellwert Ändert", 1, "Ein Schwellwert für die Änderung von my ist sinnvoll, wenn der Algorithmus möglichst schnell mit einer festen Genuaigkeit terminieren soll. ");
		        q2.addAnswer("Wenn sich sigma weniger als ein Schwellwert ändert", 0, " ");
		        lang.addMSQuestion(q2);
			}
			codeSupport.highlight(2,0,false);
			iText.setText("i = " + j, null, Timing.MEDIUM);
			hideText(descr);
			lang.nextStep();		
			for (int cluster = 0; cluster < numOfClusters; cluster++) {
				cText.setText("c = " + cluster, null, Timing.MEDIUM);
				codeSupport.toggleHighlight(2,0,false,3,0);
				descr[0].show();
				descr[0].setText(getMyDescription().substring(672,740), null, Timing.MEDIUM);
				descr[1].show();
				descr[1].setText(getMyDescription().substring(740,755), null, Timing.MEDIUM);
				lang.nextStep();
				codeSupport.toggleHighlight(3,0,false,5,0);
				codeSupport.highlight(6, 0,false);
				codeSupport.highlight(4, 0,false);
				descr[2].show();
				descr[3].show();
				descr[0].hide();
				eText.show();
				descr[1].setText(getMyDescription().substring(755,832), null, Timing.MEDIUM);
				descr[2].setText(getMyDescription().substring(832,906), null, Timing.MEDIUM);
				descr[3].setText(getMyDescription().substring(906,964), null, Timing.MEDIUM);
				posteriorText.show();
				// E-step: compute weights
				for (int dataPoint = 0; dataPoint < numOfDataPoints; dataPoint++) {
					normalization = 0;
					for (int i = 0; i < numOfClusters; i++) {
						normalization += pi[i]
								* twoDimGaussian(x[dataPoint], my[i], sigma[i]);
						System.out.println("pi: " + pi[i]);
						System.out.println("my: " + my[i][0] + "," + my[i][1]);
						System.out.println("sig: " + sigma[i][0][0] + "," + sigma[i][0][1] + "," + sigma[i][1][0]+ "," +sigma[i][1][1]);
						System.out.println("x: " + x[dataPoint][0] + "," +x[dataPoint][1]);
						System.out.println("gaussian: " + twoDimGaussian(x[dataPoint], my[i], sigma[i]));
					}
					
					alpha[dataPoint][cluster] = (pi[cluster] * twoDimGaussian(
							x[dataPoint], my[cluster], sigma[cluster]))
							/ normalization;
				}
				lang.nextStep();
				mText.show();
				posteriorText.hide();
				eText.hide();
				descr[1].setText(getMyDescription().substring(965,1035), null, Timing.MEDIUM);
				descr[2].setText(getMyDescription().substring(1035,1089), null, Timing.MEDIUM);
				descr[3].hide();
				descr[4].show();
				descr[4].setText("N[c] ist die Wahrscheinlichkeit für das Cluster, aufsummiert für alle Punkte.", null, Timing.MEDIUM);
				NText.show();
				codeSupport.toggleHighlight(5,0,false,7,0);
				codeSupport.unhighlight(6,0,false);
				codeSupport.unhighlight(4,0,false);
				// M-step
				if (j == 0){
					FillInBlanksQuestionModel q3 = new FillInBlanksQuestionModel("FI");
			        q3.setPrompt("Beim M-step wird die _________ maximiert");
			        q3.addAnswer("Log-Likelihood", 1, "Richtig");
			        q3.addAnswer("log-likelihood", 1, "Richtig");
			        q3.addAnswer("log likelihood", 1, "Richtig");
			        q3.addAnswer("Log Likelihood", 1, "Richtig");
			        lang.addFIBQuestion(q3);
				}
				weightedDataPoints_x = 0;
				weightedDataPoints_y = 0;
				N_k = 0;
				for (int dataPoint = 0; dataPoint < numOfDataPoints; dataPoint++) {
					N_k = N_k + alpha[dataPoint][cluster];
					weightedDataPoints_x = weightedDataPoints_x
							+ alpha[dataPoint][cluster] * x[dataPoint][0];
					weightedDataPoints_y = weightedDataPoints_y
							+ alpha[dataPoint][cluster] * x[dataPoint][1];
				}
				NText.setText("N[c] = " + N_k, null, Timing.MEDIUM);
				lang.nextStep();
				descr[4].setText("Der neue Mittelwert des Clusters kann bestimmt werden", null, Timing.MEDIUM);
				codeSupport.toggleHighlight(7,0,false,8,0);
				my[cluster][0] = 1 / N_k * weightedDataPoints_x;
				my[cluster][1] = 1 / N_k * weightedDataPoints_y;
				NText.setText("my = (" +my[cluster][0]+","+my[cluster][1]+")" , null, Timing.MEDIUM);
				plotMy(cluster,myPrim[cluster], my[cluster]);
				lang.nextStep();
				descr[4].setText("Die Kovarianz für der Gaußverteilung des Clusters kann bestimmt werden.", null, Timing.MEDIUM);
				codeSupport.toggleHighlight(8,0,false,9,0);
				weightedMyDist11 = 0;
				weightedMyDist12 = 0;
				weightedMyDist21 = 0;
				weightedMyDist22 = 0;
				for (int dataPoint = 0; dataPoint < numOfDataPoints; dataPoint++) {
					weightedMyDist11 += alpha[dataPoint][cluster]
							* (x[dataPoint][0] - my[cluster][0])
							* (x[dataPoint][0] - my[cluster][0]);
					weightedMyDist12 += alpha[dataPoint][cluster]
							* (x[dataPoint][0] - my[cluster][0])
							* (x[dataPoint][1] - my[cluster][1]);
					weightedMyDist21 += alpha[dataPoint][cluster]
							* (x[dataPoint][1] - my[cluster][1])
							* (x[dataPoint][0] - my[cluster][0]);
					weightedMyDist22 += alpha[dataPoint][cluster]
							* (x[dataPoint][1] - my[cluster][1])
							* (x[dataPoint][1] - my[cluster][1]);
				}
				sigma[cluster][0][0] = 1 / N_k * weightedMyDist11;
				sigma[cluster][0][1] = 1 / N_k * weightedMyDist12;
				sigma[cluster][1][0] = 1 / N_k * weightedMyDist21;
				sigma[cluster][1][1] = 1 / N_k * weightedMyDist22;
				NText.setText("sig = |" +sigma[cluster][0][0]+"  "+sigma[cluster][0][1]+"|" , null, Timing.MEDIUM);
				N2Text.show();
				N2Text.setText("          |" +sigma[cluster][1][0]+"  "+sigma[cluster][1][1]+"|" , null, Timing.MEDIUM);
				plotSigma(cluster,myPrim[cluster], my[cluster],sigma[cluster]);
				lang.nextStep();
				N2Text.hide();
				descr[4].setText("Der Prior für das Cluster wird aktualisiert.", null, Timing.MEDIUM);
				codeSupport.toggleHighlight(9,0,false,10,0);
				pi[cluster] = N_k / numOfDataPoints;
				NText.setText("pi[c] = "+pi[cluster] , null, Timing.MEDIUM);
				lang.nextStep();
				mText.hide();
				NText.hide();
				descr[4].hide();
				descr[1].hide();
				descr[2].hide();
				codeSupport.unhighlight(10,0,false);
			}
		}
		iText.hide();
		cText.hide();
	}

    public String getName() {
        return "Expectation Maximization mit der Gaussverteilung";
    }

    public String getAlgorithmName() {
        return "Expectation Maximization";
    }

    public String getAnimationAuthor() {
        return "Claudia Lölkes, Verena Sieburger";
    }

    public String getDescription(){
        return DESCRIPTION;
    }
    
    public String getMyDescription(){
        return MYDESCRIPTION;
    }

    public String getCodeExample(){
        return  SOURCE_CODE;
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
    

}