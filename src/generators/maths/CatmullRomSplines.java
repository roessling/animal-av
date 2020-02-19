/*
 * CatmullRomSplines.java
 * Falko Wittrin, Thilo Billerbeck, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
 
 package generators.maths;

import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import java.awt.*;
import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.*;

public class CatmullRomSplines implements Generator {
    private Language lang;
    private int[] xCoordinates;
    private SourceCodeProperties soureCodeProperties;
    private PolylineProperties polylinePropertiesTangentens;
    private int[] yCoordinates;
    private MatrixProperties tableProperties;
    private PolylineProperties polylinePropertiesGraph;

    public void init(){
        lang = new AnimalScript("Catmull Rom Spline Interpolation", "Falko Wittrin, Thilo Billerbeck", 1080, 1920);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        xCoordinates = (int[])primitives.get("xCoordinates");
        soureCodeProperties = (SourceCodeProperties)props.getPropertiesByName("soureCodeProperties");
        polylinePropertiesTangentens = (PolylineProperties)props.getPropertiesByName("polylinePropertiesTangentens");
        yCoordinates = (int[])primitives.get("yCoordinates");
        tableProperties = (MatrixProperties)props.getPropertiesByName("tableProperties");
        polylinePropertiesGraph = (PolylineProperties)props.getPropertiesByName("polylinePropertiesGraph");

        Coordinates[] points = new Coordinates[]{new Coordinates(xCoordinates[0],yCoordinates[0]),new Coordinates(xCoordinates[1],yCoordinates[1]),new Coordinates(xCoordinates[2],yCoordinates[2]),new Coordinates(xCoordinates[3],yCoordinates[3])};
        calculateSpline(points);

		lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Catmull Rom Spline Interpolation";
    }

    public String getAlgorithmName() {
        return "Catmull Rom Spline Interpolation";
    }

    public String getAnimationAuthor() {
        return "Falko Wittrin, Thilo Billerbeck";
    }

    public String getDescription(){
        return "Erstellt anhand von vier Kontrollpunkten ein Spline nach der Catmull Rom Formel.";
    }

    public String getCodeExample(){
        return "q(t) = 0.5 * (2 * p1 + (p2 - p0) * t + (2*p0 - 5*p1 + 4*p2 - p3) * t^2 + (3*p1 -p0 - 3 * p2 + p3) * t^3))";
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


    public void calculateSpline(Coordinates[] points){

        TextProperties textOptionsNormal2 = new TextProperties();
        textOptionsNormal2.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.BOLD, 16));

        TextProperties textOptionsLarge = new TextProperties();
        textOptionsLarge.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.BOLD, 32));

        TextProperties textOptionsNormal = new TextProperties();
        textOptionsNormal.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.PLAIN, 16));

        TextProperties textOptionsSmall = new TextProperties();
        textOptionsSmall.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.PLAIN, 12));

		lang.nextStep("Einleitung");
        Text text = lang.newText(new Coordinates(20,20),"Catmull Rom Spline Interpolation","textHead",null,textOptionsLarge);
        Text text1 = lang.newText(new Coordinates(20,80),"Die Animation zeigt wie anhand von vier Kontrollpunkten eine Kurve","text1",null,textOptionsNormal);
        Text text2 = lang.newText(new Coordinates(20,100),"zwischen dem 2. und 3. Kontrollpunkt berechnet wird.","text2",null,textOptionsNormal);
        Text text3 = lang.newText(new Coordinates(20,120),"Dabei dienen der erste und letze Kontrollpunkt als Angabe der Richtung im Punkt P2 bzw. P3","text3",null,textOptionsNormal);
        Text text4 = lang.newText(new Coordinates(20,140),"Die Idee hinter dem Algorithmus ist Tangenten zu berechnen welche gewichtet werden und summiert eine Koordinate ergeben.","text4",null,textOptionsNormal);

        lang.nextStep("Initialisierung");

        text1.hide();
        text2.hide();
        text3.hide();
        text4.hide();

        Text text5 = lang.newText(new Coordinates(20,190),"Pseudocode","text5",null,textOptionsNormal2);
        SourceCode pseudoCode = lang.newSourceCode(new Coordinates(20,200),"pseudoCode", null, soureCodeProperties);
        pseudoCode.addCodeLine("t0 = 2 * p1",null,0,null);
        pseudoCode.addCodeLine("t1 = (p2 - p0) * t",null,0,null);
        pseudoCode.addCodeLine("t2 = (2*p0 - 5*p1 + 4*p2 - p3) * t^2",null,0,null);
        pseudoCode.addCodeLine("t3 = (3*p1 -p0 - 3 * p2 + p3) * t^3)",null,0,null);
        pseudoCode.addCodeLine("q(t) = 0.5 * (t0 + t1 + t2 + t3)",null,0,null);

        Text text6 = lang.newText(new Coordinates(900,40),"Beschreibung","text6",null,textOptionsNormal2);
        SourceCode desciption = lang.newSourceCode(new Coordinates(900,50),"description", null, soureCodeProperties);
        desciption.addCodeLine("Schritt 1: Berechnung der einzelnen Tangenten tx",null,0,null);
        desciption.addCodeLine("Schritt 2: Einsetzen von t_x in q(t)",null,0,null);
        desciption.addCodeLine("Schritt 3: Auswerten von q(t) für verschiedene t um Punkte auf dem Spline zu erhalten. ",null,0,null);

        //Grid
        String[][] data = {
                {"name","p0","p1","p2","p3"},
                {"x",Double.toString(points[0].getX()),Double.toString(points[1].getX()),Double.toString(points[2].getX()),Double.toString(points[3].getX())},
                {"y",Double.toString(points[0].getY()),Double.toString(points[1].getY()),Double.toString(points[2].getY()),Double.toString(points[3].getY())}
        };
        StringMatrix grid = lang.newStringMatrix(new Coordinates(20,85),data,"grid",null,tableProperties);

        ///Graph
        float[] greatesValue = findGreatestValue(points);
        float[] lowestValue = findLowestValue(points);
        PolylineProperties polylineOptions = new PolylineProperties();
        polylineOptions.set(AnimationPropertiesKeys.BWARROW_PROPERTY,true);
        PolylineProperties polylineOptions2 = new PolylineProperties();
        polylineOptions2.set(AnimationPropertiesKeys.FWARROW_PROPERTY,true);

        Polyline xAxis = lang.newPolyline(new Node[]{new Coordinates(390,400),new Coordinates(800,400)},"xAxis",null,polylineOptions2);
        Polyline yAxis = lang.newPolyline(new Node[]{new Coordinates(400,100),new Coordinates(400,410)},"yAxis",null,polylineOptions);
        Text text7 = lang.newText(new Coordinates(400,415),Double.toString(lowestValue[0]),"x_axis_value_0",null,textOptionsSmall);
        Text text10 = lang.newText(new Coordinates(400,405),Double.toString(lowestValue[1]),"y_axis_value_0",null,textOptionsSmall);
        Text text8 = lang.newText(new Coordinates(800,405),Double.toString(greatesValue[0]),"x_axis_value_2",null,textOptionsSmall);
        Text text9 = lang.newText(new Coordinates(385,105),Double.toString(greatesValue[1]),"y_axis_value_1",null,textOptionsSmall);

        PointProperties pointOptions = new PointProperties();
        pointOptions.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.RED);
        pointOptions.set(AnimationPropertiesKeys.DEPTH_PROPERTY,10);

        float fx = 400/(greatesValue[0]-lowestValue[0]);
        float fy = 310/(greatesValue[1]-lowestValue[1]);

        EllipseProperties elipseOptions = new EllipseProperties();
        elipseOptions.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.BLACK);
        elipseOptions.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
        elipseOptions.set(AnimationPropertiesKeys.DEPTH_PROPERTY,0);

        Ellipse e = lang.newEllipse(calcCoordinates(fx,fy,points[0].getX(),points[0].getY(),400,400),new Coordinates(5,5),"e0",null,elipseOptions);
        Ellipse e1 = lang.newEllipse(calcCoordinates(fx,fy,points[1].getX(),points[1].getY(),400,400),new Coordinates(5,5),"e1",null,elipseOptions);
        Ellipse e2 = lang.newEllipse(calcCoordinates(fx,fy,points[2].getX(),points[2].getY(),400,400),new Coordinates(5,5),"e2",null,elipseOptions);
        Ellipse e3 = lang.newEllipse(calcCoordinates(fx,fy,points[3].getX(),points[3].getY(),400,400),new Coordinates(5,5),"e3",null,elipseOptions);
        lang.nextStep("Berechnung der 1. Tangente");

        getCatmullRomSpline(pseudoCode,desciption,points,fx,fy);
    }

    private Coordinates calcCoordinates(float fx, float fy,double px, double py,int xStart,int yStart){
        int x = (int)(fx * px)+xStart;
        int y = 400 - (int)(fy*py);
        return new Coordinates(x,y);
    }

    private float[] findGreatestValue(Coordinates[] p){
        float greatestX = p[0].getX();
        float greatestY = p[0].getY();

        for(int i = 1; i < p.length; i++){
            if(p[i].getX() > greatestX) greatestX = p[i].getX();
            if(p[i].getY() > greatestY) greatestY = p[i].getY();
        }

        return new float[]{greatestX,greatestY};
    }

    private float[] findLowestValue(Coordinates[] p){
        float lowestX = p[0].getX();
        float lowestY = p[0].getY();

        for(int i = 1; i < p.length; i++){
            if(p[i].getX() < lowestX) lowestX = p[i].getX();
            if(p[i].getY() < lowestY) lowestY = p[i].getY();
        }

        return new float[]{lowestX,lowestY};
    }

    private void getCatmullRomSpline(SourceCode pseudoCode, SourceCode algoDescription, Coordinates[] points, float fx, float fy){

        double p0x = points[0].getX();
        double p1x = points[1].getX();
        double p2x = points[2].getX();
        double p3x = points[3].getX();

        double p0y = points[0].getY();
        double p1y = points[1].getY();
        double p2y = points[2].getY();
        double p3y = points[3].getY();

        pseudoCode.highlight(0);
        algoDescription.highlight(0);

        double x = 2 * p1x;
        double y = 2 * p1y;

        Polyline line = lang.newPolyline(new Coordinates[]{calcCoordinates(fx,fy,p0x,p0y,400,400),calcCoordinates(fx,fy,x+p0x,y+p0y,400,400)},"t1",null,polylinePropertiesTangentens);
        lang.nextStep("Berechnung der 2. Tangente");

        pseudoCode.highlight(1);
        pseudoCode.unhighlight(0);

        double x2 = p2x-p0x;
        double y2 = p2y-p0y;

        Polyline line2 = lang.newPolyline(new Coordinates[]{calcCoordinates(fx,fy,p1x,p1y,400,400),calcCoordinates(fx,fy,x2+p1x,y2+p1y,400,400)},"t2",null,polylinePropertiesTangentens);
        lang.nextStep("Berechnung der 3. Tangente");

        pseudoCode.highlight(2);
        pseudoCode.unhighlight(1);

        double x3 = 2*p0x-5*p1x+4*p2x-p3x;
        double y3 = 2*p0y-5*p1y+4*p2y-p3y;

        Polyline line3 = lang.newPolyline(new Coordinates[]{calcCoordinates(fx,fy,p2x,p2y,400,400),calcCoordinates(fx,fy,x3+p2x,y3+p2y,400,400)},"t3",null,polylinePropertiesTangentens);
        lang.nextStep("Berechnung der 4. Tangente");

        pseudoCode.highlight(3);
        pseudoCode.unhighlight(2);

        double x4 = 3*p1x-p0x-3*p2x+p3x;
        double y4 = 3*p1y-p0y-3*p2y+p3y;

        Polyline line4 = lang.newPolyline(new Coordinates[]{calcCoordinates(fx,fy,p3x,p3y,400,400),calcCoordinates(fx,fy,x4+p3x,y4+p3y,400,400)},"t4",null,polylinePropertiesTangentens);
        lang.nextStep("Berechnung der Ergenispunkte");

        pseudoCode.unhighlight(3);
        pseudoCode.highlight(4);
        algoDescription.unhighlight(0);
        algoDescription.highlight(2);
        lang.nextStep();

        TextProperties textOptionsNormal = new TextProperties();
        textOptionsNormal.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.BOLD, 16));

        Text text = lang.newText(new Coordinates(20,330),"q(t) Ergebnisse","ergebnisse",null,textOptionsNormal);

        String[][] data = {
                {"t","0","0.25","0.5","0.75","1"},
                {"q(t) x","","","","",""},
                {"q(t) y","","","","",""}
        };
        StringMatrix grid = lang.newStringMatrix(new Coordinates(20,350),data,"grid",null,tableProperties);

        pseudoCode.unhighlight(4);
        algoDescription.highlight(2);
        algoDescription.unhighlight(1);

        double[] q0 = CalculateQ(points,0f);

        grid.put(1,1,Double.toString(q0[0]),null,null);
        grid.put(2,1,Double.toString(q0[1]),null,null);


        EllipseProperties elipseOptions = new EllipseProperties();
        elipseOptions.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.RED);
        elipseOptions.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
        elipseOptions.set(AnimationPropertiesKeys.DEPTH_PROPERTY,0);
        Ellipse e0 = lang.newEllipse(calcCoordinates(fx,fy,q0[0],q0[1],400,400),new Coordinates(5,5),"e0",null,elipseOptions);
        lang.nextStep();

        double[] q1 = CalculateQ(points,.25f);

        grid.put(1,2,Double.toString(q1[0]),null,null);
        grid.put(2,2,Double.toString(q1[1]),null,null);

        Ellipse e1 = lang.newEllipse(calcCoordinates(fx,fy,q1[0],q1[1],400,400),new Coordinates(5,5),"e1",null,elipseOptions);
        Polyline l1 = lang.newPolyline(new Coordinates[]{calcCoordinates(fx,fy,q0[0],q0[1],400,400),calcCoordinates(fx,fy,q1[0],q1[1],400,400)},"l1",null,polylinePropertiesGraph);
        lang.nextStep();

        double[] q2 = CalculateQ(points,.5f);

        grid.put(1,3,Double.toString(q2[0]),null,null);
        grid.put(2,3,Double.toString(q2[1]),null,null);

        Ellipse e2 = lang.newEllipse(calcCoordinates(fx,fy,q2[0],q2[1],400,400),new Coordinates(5,5),"e2",null,elipseOptions);
        Polyline l2 = lang.newPolyline(new Coordinates[]{calcCoordinates(fx,fy,q1[0],q1[1],400,400),calcCoordinates(fx,fy,q2[0],q2[1],400,400)},"l2",null,polylinePropertiesGraph);
        lang.nextStep();
		
		//Frage
		FillInBlanksQuestionModel qe2 = new FillInBlanksQuestionModel("qe2");
		qe2.setPrompt("Wie lautetet die y-koordinate des Ergebnisses bei t=0.5");
		qe2.addAnswer(new AnswerModel("1",Double.toString(q2[1]),1,"korrekt"));
		lang.addFIBQuestion(qe2);

        double[] q3 = CalculateQ(points,.75f);

        grid.put(1,4,Double.toString(q3[0]),null,null);
        grid.put(2,4,Double.toString(q3[1]),null,null);

        Ellipse e3 = lang.newEllipse(calcCoordinates(fx,fy,q3[0],q3[1],400,400),new Coordinates(5,5),"e3",null,elipseOptions);
        Polyline l3 = lang.newPolyline(new Coordinates[]{calcCoordinates(fx,fy,q2[0],q2[1],400,400),calcCoordinates(fx,fy,q3[0],q3[1],400,400)},"l3",null,polylinePropertiesGraph);
        lang.nextStep();

        double[] q4 = CalculateQ(points,1f);

        grid.put(1,5,Double.toString(q4[0]),null,null);
        grid.put(2,5,Double.toString(q4[1]),null,null);

        Ellipse e4 = lang.newEllipse(calcCoordinates(fx,fy,q4[0],q4[1],400,400),new Coordinates(5,5),"e4",null,elipseOptions);
        Polyline l4 = lang.newPolyline(new Coordinates[]{calcCoordinates(fx,fy,q3[0],q3[1],400,400),calcCoordinates(fx,fy,q4[0],q4[1],400,400)},"l4",null,polylinePropertiesGraph);
    
		lang.nextStep("Zusammenfassung");
		lang.hideAllPrimitives();
		
		
		//Frage:
		FillInBlanksQuestionModel qe1 = new FillInBlanksQuestionModel("qe1");
		qe1.setPrompt("Welchen Wert hat die x Koordinate von f(x) = t1 fuer x = 1?");
		qe1.addAnswer(new AnswerModel("1",Double.toString((p2x-p0x)*1+p1x),1,"korrekt"));
		lang.addFIBQuestion(qe1);
		
		
		TextProperties textOptionsLarge = new TextProperties();
        textOptionsLarge.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.BOLD, 32));
		
		Text text21 = lang.newText(new Coordinates(20,20),"Catmull Rom Spline Interpolation","textHead",null,textOptionsLarge);
		Text text15 = lang.newText(new Coordinates(20,80),"Zusammenfassung","text6",null,textOptionsNormal);
		Text text16 = lang.newText(new Coordinates(20,120),"Es wurden folgende Tangenten berechnet","text6",null,textOptionsNormal);
		Text text17 = lang.newText(new Coordinates(20,140),"t0(x) = "+"p0 +"+"(" + Double.toString(2 * p1x) + "|"+ Double.toString(2* p1y)+")","text6",null,textOptionsNormal);
		Text text18 = lang.newText(new Coordinates(20,160),"t1(x) = "+"p1 +" + "x *" + "(" + Double.toString(p2x-p0x) + "|"+ Double.toString(p2y-p0y)+")","text6",null,textOptionsNormal);
		Text text19 = lang.newText(new Coordinates(20,180),"t2(x) = "+"p2 +" + "x*x * " + "(" + Double.toString(2*p0x-5*p1x+4*p2x-p3x) + "|"+ Double.toString(2*p0y-5*p1y+4*p2y-p3y)+")","text6",null,textOptionsNormal);
		Text text20 = lang.newText(new Coordinates(20,200),"t3(x) = "+"p3 +" + "x*x*x *" + "(" + Double.toString(3*p1x-p0x-3*p2x+p3x) + "|"+ Double.toString(3*p1y-p0y-3*p2y+p3y)+")","text6",null,textOptionsNormal);
	}

    private double[] CalculateQ(Coordinates[] points, float t){
        double p0 = points[0].getX();
        double p1 = points[1].getX();
        double p2 = points[2].getX();
        double p3 = points[3].getX();

        double x = 0.5 * ((2 * p1) + (-p0 + p2) * t + (2*p0 - 5*p1 + 4*p2 - p3) * t*t + (-p0 + 3*p1- 3*p2 + p3) * t*t*t);

        p0 = points[0].getY();
        p1 = points[1].getY();
        p2 = points[2].getY();
        p3 = points[3].getY();

        double y = 0.5 * ((2 * p1) + (-p0 + p2) * t + (2*p0 - 5*p1 + 4*p2 - p3) * t*t + (-p0 + 3*p1- 3*p2 + p3) * t*t*t);
        return new double[]{x,y};
    }
}