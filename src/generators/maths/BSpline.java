/*
 * BSpline.java
 * Falko Wittrin, Thilo Billerbeck, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.*;
import algoanim.util.Offset;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class BSpline implements Generator {
    private Language lang;
    private SourceCodeProperties sourceCodeProperties;
    private PolylineProperties polylineProperties;
    private MatrixProperties tableProperties;
    private int m;
    private int[][] points;

    public void init(){
        lang = new AnimalScript("B-Spline Berechnung", "Falko Wittrin, Thilo Billerbeck", 1080, 1920);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        polylineProperties = (PolylineProperties)props.getPropertiesByName("polylineProperties");
        tableProperties = (MatrixProperties)props.getPropertiesByName("tableProperties");
        m = (Integer)primitives.get("m");
        points = (int[][])primitives.get("points");


        Coordinates[] coords = new Coordinates[points.length];
        for(int i = 0; i < points.length; i++){
            coords[i] = new Coordinates(points[0][i],points[1][i]);
        }
        calculateBSpline(coords,m);
		lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "B-Spline Berechnung";
    }

    public String getAlgorithmName() {
        return "B-Spline Berechnung";
    }

    public String getAnimationAuthor() {
        return "Falko Wittrin, Thilo Billerbeck";
    }

    public String getDescription(){
        return "Berechnet ein B-Spline anhand der eingebenen Punkte und anhand des Grade m ";
    }

    public String getCodeExample(){
        return "n = punkte.length - 1"
 +"\n"
 +"Initialisiere t[Anzahl der Punkte(n) + Grad des Polynoms(m) + 2] mit 0"
 +"\n"
 +"for j = 0 to n+m+1"
 +"\n"
 +"  if  j <= m t[j] = 0"
 +"\n"
 +"  if j >= m+1 and j <= n t[j] = j-m"
 +"\n"
 +"  if j > n t[j] = n - m + 1"
 +"\n"
 +"endfor"
 +"\n"
 +"Initialisiere B[n-m+2][n+1] mit 0"
 +"\n"
 +"for t = 0 to n-m+1"
 +"\n"
 +"  for i = 0 to n"
 +"\n"
 +"    if m == 0 and t >= t[i] and t < t[i+1] Bi,0(t) = 1"
 +"\n"
 +"    else if m == 0 and t < t[i] or t >= t[i+1] Bi,0(t) = 0"
 +"\n"
 +"      else Bi,m(t) = (t - t[i])/(t[i+m] - t[i]) * Bi,m-1 + (t[i+k+1]-t)/(t[i+k+1]-t[i+1]) * Bi+1,m-1"
 +"\n"
 +"  endfor"
 +"\n"
 +"  berechne f(t) = p0 * B0,m(t) + ... + pn * Bn,m(t) für 0 <= t <= n-m + 1"
 +"\n"
 +"endfor";
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

    public void calculateBSpline(Coordinates[] points, int m){
        int n = points.length-1;

        TextProperties textOptionsNormal2 = new TextProperties();
        textOptionsNormal2.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.BOLD, 16));

        TextProperties textOptionsLarge = new TextProperties();
        textOptionsLarge.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.BOLD, 32));

        TextProperties textOptionsNormal = new TextProperties();
        textOptionsNormal.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.PLAIN, 16));


        //Einleitung
        //Text text = lang.newText(new Coordinates(20,20),"B-Spline Interpolation","textHead",null,textOptionsLarge);
		Text text = lang.newText(new Offset(20,20, new Coordinates(0,0),null),"B-Spline Interpolation","textHead",null,textOptionsLarge);
        Text text1001 = lang.newText(new Coordinates(20,80),"Die Animation zeigt wie anhand von n+1 Punkten und dem Grad m der Verbindung ein Spline mit Hilfe der B-Spline Approximation berechnet wird.","text1001",null,textOptionsNormal);
        Text text1002 = lang.newText(new Coordinates(20,100),"Dabei werde mit mit Hilfe der B-Spline Basisfunktion die Punkte der Funktion berechnet.","text1002",null,textOptionsNormal);
        Text text1003 = lang.newText(new Coordinates(20,120),"Diese können nach Abschluss der Berechnung verbunden werden und ergeben dann das gewuenschte Polynom vom Grad m.","text1003",null,textOptionsNormal);
        lang.nextStep("Einleitung");

        //Initialisierung
        text1001.hide();
        text1002.hide();
        text1003.hide();
		
		//Welchen Wert hat n? 
		FillInBlanksQuestionModel q1 = new FillInBlanksQuestionModel("q1");
		q1.setPrompt("Welchen wert hat n");
		q1.addAnswer(new AnswerModel("1",Integer.toString(n),1,"korrekt"));
		lang.addFIBQuestion(q1);


        Text text11 = lang.newText(new Coordinates(20,80),"Eingabewerte: Anzahl der Punkte n+1 = " + Integer.toString(points.length) + " Grad des Splines m = " + Integer.toString(m) ,"text3",null,textOptionsNormal2);
        StringMatrix grid = lang.newStringMatrix(new Coordinates(20,105),createTableData(points),"grid",null,tableProperties);

        Text text4 = lang.newText(new Coordinates(20,250),"Pseudocode","text4",null,textOptionsNormal2);
        SourceCode pseudoCode = lang.newSourceCode(new Coordinates(20,260),"pseudoCode", null, sourceCodeProperties);
        pseudoCode.addCodeLine("n = punkte.length - 1",null,0,null);
        pseudoCode.addCodeLine("Initialisiere t[Anzahl der Punkte(n) + Grad des Polynoms(m) + 2] mit 0 ",null,0,null);
        pseudoCode.addCodeLine("for j = 0 to n+m+1",null,1,null);
        pseudoCode.addCodeLine("if  j <= m t[j] = 0",null,2,null);
        pseudoCode.addCodeLine("if j >= m+1 and j <= n t[j] = j-m",null,2,null);
        pseudoCode.addCodeLine("if j > n t[j] = n - m + 1",null,2,null);
        pseudoCode.addCodeLine("endfor",null,1,null);
        pseudoCode.addCodeLine("Initialisiere B[n-m+2][n+1] mit 0",null,0,null); //7
        pseudoCode.addCodeLine("for t = 0 to n-m+1",null,0,null);
        pseudoCode.addCodeLine("for i = 0 to n",null,1,null);
        pseudoCode.addCodeLine("if m == 0 and t >= t[i] and t < t[i+1] Bi,0(t) = 1",null,2,null);
        pseudoCode.addCodeLine("else if m == 0 and t < t[i] or t >= t[i+1] Bi,0(t) = 0",null,2,null);
        pseudoCode.addCodeLine("else Bi,m(t) = (t - t[i])/(t[i+m] - t[i]) * Bi,m-1 + (t[i+k+1]-t)/(t[i+k+1]-t[i+1]) * Bi+1,m-1",null,3,null);
        pseudoCode.addCodeLine("endfor",null,1,null);
        pseudoCode.addCodeLine("berechne f(t) = p0 * B0,m(t) + ... + pn * Bn,m(t) für 0 <= t <= n-m + 1",null,1,null);
        pseudoCode.addCodeLine("endfor",null,0,null);
        lang.nextStep("Berechne tj");

        //Tj berechnen
        int tj[] = calculateKnots(m,n,pseudoCode,tableProperties,textOptionsNormal2);

        //Function berechnen
        calculateFunction(m,n,tj,pseudoCode,tableProperties,textOptionsNormal2,points);


    }

    private String[][] createTableData(Coordinates[] p){
        java.util.List<String> firstLine = new LinkedList<String>();
        firstLine.add("name");
        java.util.List<String> secondLine = new LinkedList<String>();
        secondLine.add("x");
        java.util.List<String> thirdLine = new LinkedList<>();
        thirdLine.add("y");
        for(int i = 0; i < p.length; i++){
            firstLine.add("p"+i);
            secondLine.add(Integer.toString(p[i].getX()));
            thirdLine.add(Integer.toString(p[i].getY()));
        }

        String [] first = new String[firstLine.toArray().length];
        String[] second = new String[secondLine.toArray().length];;
        String[] third = new String[thirdLine.toArray().length];;

        for(int i = 0; i < firstLine.toArray().length; i++){
            first[i] = firstLine.get(i);
            second[i] = secondLine.get(i);
            third[i] = thirdLine.get(i);
        }

        return new String[][]{first,second,third};
    }

    private String[][] createKnotTableData(int length){
        java.util.List<String> firstLine = new LinkedList<String>();
        firstLine.add("name");
        java.util.List<String> secondLine = new LinkedList<String>();
        secondLine.add("x");
        for(int i = 0; i < length; i++){
            firstLine.add("t"+i);
            secondLine.add("");
        }

        String [] first = new String[firstLine.toArray().length];
        String[] second = new String[secondLine.toArray().length];

        for(int i = 0; i < firstLine.toArray().length; i++){
            first[i] = firstLine.get(i);
            second[i] = secondLine.get(i);
        }
        return new String[][]{first,second};
    }

    private String[][] createResultTableData(double[] x, double[] y){
        java.util.List<String> firstLine = new LinkedList<String>();
        firstLine.add("t");
        java.util.List<String> secondLine = new LinkedList<String>();
        secondLine.add("x(t)");
        List<String> thirdLine = new LinkedList<String>();
        thirdLine.add("y(t)");
        for(int i = 0; i < x.length; i++){
            firstLine.add(Integer.toString(i));
            secondLine.add("");
            thirdLine.add("");
        }

        String [] first = new String[firstLine.toArray().length];
        String[] second = new String[secondLine.toArray().length];
        String[] third = new String[thirdLine.toArray().length];

        for(int i = 0; i < firstLine.toArray().length; i++){
            first[i] = firstLine.get(i);
            second[i] = secondLine.get(i);
            third[i] = thirdLine.get(i);
        }
        return new String[][]{first,second,third};
    }

    private String[][] createBTableData(double[][] bs, int m){
        String[][] data = new String[bs.length+1][bs[0].length+1];
        data[0][0] = "t\\B";

        for(int i = 0; i < 1; i++){
            for(int t = 1; t < data.length; t++){
                data[t][i] = Integer.toString(t);
            }
        }

        for(int t = 0; t < 1; t++){
            for(int i = 1; i < data[0].length; i++){
                data[t][i] = "(" + (i-1) + "," + m +")";
            }
        }

        for(int t = 0; t < bs.length; t++){
            for(int i = 0; i < bs[t].length; i++){
                data[t+1][i+1] = Double.toString(bs[t][i]);
            }
        }
        return data;
    }

    private int[] calculateKnots(int m, int n, SourceCode pseudoCode, MatrixProperties matrixOptions, TextProperties textOptions){

        int[] t = new int[n+m+2];
		
		
		//Wie viele tj muessen berechnet werden? 
		FillInBlanksQuestionModel q2 = new FillInBlanksQuestionModel("q2");
		q2.setPrompt("Wie viele tj muessen berechnet werden");
		q2.addAnswer(new AnswerModel("1",Integer.toString(t.length),1,"korrekt"));
		lang.addFIBQuestion(q2);
		
		
		
        Text text4 = lang.newText(new Coordinates(650,80),"Tj","text4",null,textOptions);
        StringMatrix grid = lang.newStringMatrix(new Coordinates(650,100),createKnotTableData(t.length),"grid",null,matrixOptions);
        pseudoCode.highlight(0);
        pseudoCode.highlight(1);
        lang.nextStep("Berechne tj 1.Iteration");
        pseudoCode.unhighlight(0);
        pseudoCode.unhighlight(1);
        pseudoCode.highlight(2);
        lang.nextStep();
        pseudoCode.unhighlight(2);

        for(int j = 0; j < t.length; j++){
            if(j <= m){
                pseudoCode.unhighlight(5);
                pseudoCode.unhighlight(4);
                pseudoCode.highlight(3);
                t[j] = 0;
                grid.put(1,j+1, Integer.toString(t[j]),null,null);
                lang.nextStep();
            }
            else{
                if(j >= m+1 && j <= n){
                    pseudoCode.unhighlight(3);
                    pseudoCode.unhighlight(5);
                    pseudoCode.highlight(4);
                    t[j] = j-m;
                    grid.put(1,j+1, Integer.toString(t[j]),null,null);
                    lang.nextStep();
                }
                else{
                    if(j>n){
                        pseudoCode.unhighlight(3);
                        pseudoCode.unhighlight(4);
                        pseudoCode.highlight(5);
                        t[j] = n-m+1;
                        grid.put(1,j+1, Integer.toString(t[j]),null,null);
                        lang.nextStep();
                    }
                }
            }
        }

        pseudoCode.unhighlight(3);
        pseudoCode.unhighlight(4);
        pseudoCode.unhighlight(5);
        pseudoCode.highlight(6);
        lang.nextStep("Berechnung tj abgeschlossen");
        return t;
    }

    private void calculateFunction(int m, int n, int[] tj,SourceCode pseudoCode, MatrixProperties matrixOptions, TextProperties textNormal2, Coordinates[] p){
        //Ergebnis berechnen
        pseudoCode.unhighlight(6);
        pseudoCode.highlight(7);
        lang.nextStep("Berechne Bi,k");

        pseudoCode.unhighlight(7);
        pseudoCode.highlight(8);
		
		//Wie viele Ergebnispunkte werden berechnet? 
		FillInBlanksQuestionModel q3 = new FillInBlanksQuestionModel("q3");
		q3.setPrompt("Wie viele Ergebnispunkte werden berechnet?");
		q3.addAnswer(new AnswerModel("1",Integer.toString(n-m+2),1,"korrekt"));
		lang.addFIBQuestion(q3);
		

        double[] resultX = new double[n-m+2];
        double[] resultY = new double[n-m+2];
        double[][] bs = new double[n-m+2][n+1];
        int[] pointsX = new int[p.length];
        int[] pointsY = new int[p.length];

        for(int i = 0; i < p.length; i++){
            pointsX[i] = p[i].getX();
            pointsY[i] = p[i].getY();
        }

        Text text5 = lang.newText(new Coordinates(650,185),"Punkte der Kurve","text5",null,textNormal2);
        StringMatrix grid = lang.newStringMatrix(new Coordinates(650,205),createResultTableData(resultX, resultY),"grid",null,matrixOptions);

        Text text6 = lang.newText(new Coordinates(650,320),"Koeffizienten","text6",null,textNormal2);
        StringMatrix grid2 = lang.newStringMatrix(new Coordinates(650,340),createBTableData(bs,m),"grid2",null,matrixOptions);
        lang.nextStep();

        for(int t = 0; t <= n-m+1; t++){
            pseudoCode.unhighlight(8);
            pseudoCode.highlight(9);

            //Bi,k berechnen
            double[] values = getB(n+1,m,t,tj);
            for(int i = 0; i < values.length; i++){
                pseudoCode.highlight(10);
                pseudoCode.highlight(11);
                pseudoCode.highlight(12);

                bs[t][i] = values[i];
                grid2.put(t+1,i+1,Double.toString(values[i]),null,null);
                lang.nextStep();
            }
            pseudoCode.unhighlight(9);
            pseudoCode.unhighlight(10);
            pseudoCode.unhighlight(11);
            pseudoCode.unhighlight(12);
            pseudoCode.highlight(13);
            lang.nextStep();

            //x(t),y(t) berechnen
            resultX[t] = calcResult(t,n,pointsX,bs);
            resultY[t] = calcResult(t,n,pointsY,bs);
            pseudoCode.unhighlight(13);
            pseudoCode.highlight(14);
            grid.put(1,t+1,Double.toString(resultX[t]),null,null);
            grid.put(2,t+1,Double.toString(resultY[t]),null,null);
            lang.nextStep();
            pseudoCode.unhighlight(14);
        }

        //Draw Graph
        pseudoCode.highlight(15);
        lang.nextStep("Zusammenfassung (Graph)");

        float[] greatesValue = findGreatestValue(p);
        float[] lowestValue = findLowestValue(p);
        PolylineProperties polylineOptions = new PolylineProperties();
        polylineOptions.set(AnimationPropertiesKeys.BWARROW_PROPERTY,true);
        PolylineProperties polylineOptions2 = new PolylineProperties();
        polylineOptions2.set(AnimationPropertiesKeys.FWARROW_PROPERTY,true);

        TextProperties textOptionsSmall = new TextProperties();
        textOptionsSmall.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("SansSerif", Font.PLAIN, 12));

        Polyline xAxis = lang.newPolyline(new Node[]{new Coordinates(940,400),new Coordinates(1350,400)},"xAxis",null,polylineOptions2);
        Polyline yAxis = lang.newPolyline(new Node[]{new Coordinates(940,100),new Coordinates(940,410)},"yAxis",null,polylineOptions);
        Text text7 = lang.newText(new Coordinates(940,415),Double.toString(lowestValue[0]),"x_axis_value_0",null,textOptionsSmall);
        Text text10 = lang.newText(new Coordinates(940,405),Double.toString(lowestValue[1]),"y_axis_value_0",null,textOptionsSmall);
        Text text8 = lang.newText(new Coordinates(1350,405),Double.toString(greatesValue[0]),"x_axis_value_2",null,textOptionsSmall);
        Text text9 = lang.newText(new Coordinates(925,105),Double.toString(greatesValue[1]),"y_axis_value_1",null,textOptionsSmall);

        float fx = 400/(greatesValue[0]-lowestValue[0]);
        float fy = 310/(greatesValue[1]-lowestValue[1]);

        EllipseProperties elipseOptions = new EllipseProperties();
        elipseOptions.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.BLACK);
        elipseOptions.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
        elipseOptions.set(AnimationPropertiesKeys.DEPTH_PROPERTY,0);

        Ellipse e;
        for(int i = 0; i < resultX.length; i++){
            e = lang.newEllipse(calcCoordinates(fx,fy,resultX[i],resultY[i],940,940),new Coordinates(5,5),"e0",null,elipseOptions);
        }

        Polyline graph = lang.newPolyline(toCoordinates(resultX,resultY,fx,fy),"graph",null,polylineProperties);

    }

    private Coordinates[] toCoordinates(double[] x, double[] y, float fx, float fy){
        Coordinates[] coords = new Coordinates[x.length];
        for(int i = 0; i < x.length; i++){
            coords[i] = calcCoordinates(fx,fy,(int)x[i],(int)y[i],940,940);
        }
        return coords;
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

    private Coordinates calcCoordinates(float fx, float fy,double px, double py,int xStart,int yStart){
        int x = (int)(fx * px)+xStart;
        int y = 400 - (int)(fy*py);
        return new Coordinates(x,y);
    }

    private double calcResult(int t, int n, int[] p, double[][] bs){
        double result = 0;
        for(int i = 0; i < n+1; i++){
            result += p[i] * bs[t][i];
        }
        return  result;
    }

    private double[] getB(int length, int m, int t, int[] tj){
        double[] b = new double[length];
        for(int i = 0; i < length; i++){
            b[i] = calculateB(i,m,t,tj);
        }
        return b;
    }

    private double calculateB(int i, int k, int t, int[] tj){
        if(k == 0){
            if(t >= tj[i] && t <= tj[i+1]){
                return 1;
            }else{
                return 0;
            }
        }else{
            double zaehlerA = t - tj[i];
            double nennerA = tj[i+k] - tj[i];
            double coeffA  = 0;
            if(nennerA != 0) coeffA = zaehlerA/nennerA;

            double zaehlerB = tj[i+k+1]-t;
            double nennerB = tj[i+k+1]-tj[i+1];
            double coeffB  = 0;
            if(nennerB != 0) coeffB = zaehlerB/nennerB;

            return coeffA * calculateB(i,k-1,t,tj) + coeffB * calculateB(i+1,k-1,t,tj);
        }
    }

}