package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import org.apache.commons.math3.distribution.UniformRealDistribution;

/*
 * PsoGenerator.java
 * Thomas Lautenschläger, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */


import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.Circle;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;


public class PsoGenerator implements Generator {
    Language lang;
    private Color _colorHighlight;
    private int[] a;
    private int numParticles, numIterations;
    private CircleProperties circleColor;
    private SourceCodeProperties sourceCode;
    private double omega, phiParticle, phiGlobal;
    private SourceCode introSrc;
    private SourceCode src;

    private Polyline pl;


    private SourceCodeProperties sourceCodeProps;
    private MatrixProperties matrixHighlight;
    private ArrayProperties arrayProps;
    private MatrixProperties matrixProps;
    private MatrixProperties matrixPropsPointer;
    private RectProperties rectProps;
    private TextProperties textProps;
    private TextProperties textParticleIndex;
    private TextProperties textIterations;
    private PolylineProperties coordProbs;
    private CircleProperties circleProbs;
    private SourceCodeProperties introSrcProps;
    private Text particleIndex;
    private Text iterationDisp, initDisp;
    private Polyline coordinateSystemX;
    private Polyline coordinateSystemY;
    private StringMatrix x_ax, y_ax;
    private  Circle[] circles;
    private ArrayMarkerProperties arrayMarkerNextFreeProperties;


    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> vars) throws IllegalArgumentException {
        int numParticles = (int) vars.get("numParticles");
        double phiGlobal = (double) vars.get("phiGlobal");
        double phiParticle = (double) vars.get("phiParticle");
        double omega = (double) vars.get("omega");


        if (numParticles < 1) {
            throw new IllegalArgumentException("Number of particles must be greater than 0");
        }
        if (phiGlobal < 0 && phiGlobal != 0) {
            throw new IllegalArgumentException("phiGlobal must be greater than 0");
        }
        if (phiParticle < 0 && phiParticle != 0) {
            throw new IllegalArgumentException("phiParticle must be greater than 0");
        }
        if (omega < 0 && omega != 0) {
            throw new IllegalArgumentException("omega must be greater than 0");
        }

        return true;
    }


    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        setParameters(primitives);
        circleColor = (CircleProperties)props.getPropertiesByName("circleColor");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        matrixHighlight = (MatrixProperties)props.getPropertiesByName("matrixProps");
        setProperties(circleColor, sourceCode, matrixHighlight);

        Text header = lang.newText(new Coordinates(20, 30), "Particle Swarm Optimization", "header", null, textProps);
        displayIntroSrc();
        Rect  hRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, rectProps);
        lang.nextStep("Intro");

        introSrc.hide();
        displayPSOCode();
        displayParticleIndex(0);
        displayInitialization();
        Coordinates a1 = new Coordinates(510, 75);
        Coordinates b1 = new Coordinates(510, 1200);
        Node[] nodes = new Node[2];
        nodes[0] = a1;
        nodes[1] = b1;
        circles = new Circle[numParticles];
        pl = lang.newPolyline(nodes, "separationLine",null);
        try {
            pso(numParticles, omega, phiParticle, phiGlobal, -5.0, 5.0, numIterations);
        } catch (LineNotExistsException e) {
            e.printStackTrace();
        }
        pl.hide();
        src.hide();

        particleIndex.hide();
        iterationDisp.hide();


        Text conclusion = lang.newText(new Coordinates(20,100), "Conclusion",
                "header",null, textProps);
        SourceCode conclusionSrc = lang.newSourceCode(new Coordinates(20,175), "conclusionSrc",
                null, sourceCodeProps);
        conclusionSrc.addCodeLine("Particle Swarm Optimization (PSO) shows the ability to find an function optimum without using gradient information.",null,0,null);
        conclusionSrc.addCodeLine("This is done by increasing the number of function evaluations which increases computation time.",null,0,null);
                conclusionSrc.addCodeLine("PSO is a good fit for highly non-linear optimization problems where gradient information is difficult to obtain.",null,0,null);
        lang.finalizeGeneration();
        return lang.toString();
    }


    public String getAlgorithmName() {
        return "Particle Swarm Optimization";
    }


    public String getAnimationAuthor() {
        return "Thomas Lautenschläger";
    }


    public String getCodeExample() {
        return
                "pso(int numParticles, double omega, double phiP, double phiG)\n" +
                "    for each particle p do\n" +
                "        initialize particle positions uniformly distributed pCurrentPos\n" +
                "        initialize particle best with current particle positions: pBestPos <-- pCurrentPos\n" +
                "        if f(pBestPos) < f(bestGlobalPos) then\n" +
                "            update the swarm's best global position: bestGlobalPos <-- pPos\n" +
                "        initialize particle velocity: vP ~ U(-|ub - lb|, |ub - lb|)\n" +
                "    for i in range iterations do\n" +
                "        for each particle p do\n" +
                "            rG, rP ~ U(0,1)\n" +
                "            update the particle's velocity: \n" +
                "            vP <-- omega * vPX + phiP * rP * (pBestPos - pCurrentPos) + phiG * rG * (g - pCurrentPos)\n" +
                "        update particle position:\n" +
                "        pCurrentPos <-- pCurrentPos + vP\n" +
                "        if f(pCurrentPos) < f(pBestPos) then\n" +
                "            update the particle's best known position: pBestPos <-- pCurrentPos\n" +
                "            if f(pBestPos) < f(bestGlobalPos) then\n" +
                "                update the swarm's best known position: bestGlobalPos <-- pCurrentPos\n";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public String getDescription() {
        return "Particle Swarm Optimization is an optimization algorithm that does not depend on gradient information\n" +
                "Each particle position represents multidimensional function inputs. The goal is to find the best optimum using all particles\n" +
                "The particles communicate with each other by broadcasting their personal best optimum position\n" +
                "Using this information a velocity vector gets computed for each particle to get closer to the global optimum";
    }


    public String getFileExtension() {
        return "asu";
    }


    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }


    public String getName() {
        return "Particle Swarm Optimization";
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public void init() {
        lang = new AnimalScript("Particle Swarm Optimization", "Thomas Lautenschläger", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
    }

    public void setParameters(Hashtable<String, Object> primitives) {
        numParticles = (int) primitives.get("numParticles");
        numIterations = (int) primitives.get("numIterations");
        phiGlobal = (double) primitives.get("phiGlobal");
        phiParticle = (double) primitives.get("phiParticle");
        omega = (double) primitives.get("omega");
        _colorHighlight = (Color) primitives.get("colorHighlight");
    }

    public void setProperties(CircleProperties circleColor, SourceCodeProperties sourceCode, MatrixProperties matrixHighlight){

        arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
                Color.YELLOW);


        matrixProps = new MatrixProperties();
        matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,matrixHighlight.get("elemHighlight"));
        matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,matrixHighlight.get("cellHighlight"));

        matrixPropsPointer = new MatrixProperties();
        matrixPropsPointer.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        matrixPropsPointer.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        matrixPropsPointer.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        matrixPropsPointer.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, matrixHighlight.get("elemHighlight"));
        matrixPropsPointer.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,Color.RED);
        matrixPropsPointer.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,Color.YELLOW);

        arrayMarkerNextFreeProperties = new ArrayMarkerProperties();
        arrayMarkerNextFreeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.MAGENTA);

        rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));

        textParticleIndex = new TextProperties();
        textParticleIndex.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 20));

        textIterations = new TextProperties();
        textIterations.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 20));

        coordProbs = new PolylineProperties();

        circleProbs = new CircleProperties();
//        circleProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
//        circleProbs.setName("circleColor");

        circleProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, circleColor.get("color"));
        circleProbs.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
//        coordProbs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
//                Font.SANS_SERIF, Font.PLAIN, 16));


        introSrcProps = new SourceCodeProperties();
        introSrcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));
        introSrcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        introSrcProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        introSrcProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced", Font.PLAIN, 12));


        sourceCodeProps = new SourceCodeProperties();
        Font font = (Font) sourceCode.get("font");
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sourceCode.get("highlightColor"));
        sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font((String) font.getName(), Font.PLAIN, (int) sourceCode.get("size")));

    }

    public void displayIntroSrc(){
        introSrc = lang.newSourceCode(new Coordinates(20, 75), "Introduction",
                null, introSrcProps);
        introSrc.addCodeLine("Given: A two dimensional function to optimize",null, 0,
                null);
        introSrc.addCodeLine("1. Initialize particle positions from a uniform distribution", null, 0,
                null);
        introSrc.addCodeLine("2. Evaluate function with particle positions to find best global position",null, 0,
                null);
        introSrc.addCodeLine("3. Initialize particle velocities from a uniform distribution",null, 0,
                null);
        introSrc.addCodeLine("4. while termination criterion is not met do ",null, 0,
                null);
        introSrc.addCodeLine("4.1 update particle velocities",null, 1,
                null);
        introSrc.addCodeLine("4.2 update particle positions using the updated velocities",null, 1,
                null);
        introSrc.addCodeLine("4.3 check if new position is closer to the optimum",null, 1,
                null);
        introSrc.addCodeLine("4.4 check if new position is closer to the optimum than the current global optimum",null, 1,
                null);
    }

    public void displayPSOCode() {
        src = lang.newSourceCode(new Coordinates(20, 100), "sourceCode",
                null, sourceCodeProps);
        src.addCodeLine("pso(int numParticles, double omega, double phiP, double phiG)", null, 0, null);//0
        src.addCodeLine("for each particle p do", null, 1, null); // 1
        src.addCodeLine("initialize particle p position uniformly distributed:", null, 2, null); // 2
        src.addCodeLine("pCurrentPos ~ U(lowerBound, upperBound)",null,2,null); //3
        src.addCodeLine("initialize partle p best known position:", null, 2, null);//4
        src.addCodeLine("pBestPos <-- pCurrentPos", null, 2, null);//5
        src.addCodeLine("if f(pBestPos) < f(globalBestPos) then",null,2,null);//6
        src.addCodeLine("update swarm's best known position:",null,3,null);//7
        src.addCodeLine("globalBest <-- pBestPos",null,3,null);//8
        src.addCodeLine("initialize the particle's velocity:",null,2,null);//9
        src.addCodeLine("vP ~ U(-|upperBound - lowerBound|, |upperBound - lowerBound|)",null,2,null);//10
        src.addCodeLine("for i in iterations do ",null,1,null);//11
        src.addCodeLine("for each particle p do",null,2,null);//12
        src.addCodeLine("generate randomly:",null,3,null);//13
        src.addCodeLine("rP, rG ~ U(0,1)",null,3,null);//14
        src.addCodeLine("update the particle's velocity:",null,3, null);//15
        src.addCodeLine("vP <-- omega * vP + phiP * rP * (pBestPos - pCurrentPos) + / ",null,3, null);//16
        src.addCodeLine("phiG * rG * (bestGlobalPos - pCurrentPos)",null,4, null);//17
        src.addCodeLine("update position of particle p:",null,3, null);//18
        src.addCodeLine("pPosCurrent <-- pCurrentPos + vP",null,3, null);//19
        src.addCodeLine("if f(pCurrentPos) < f(pBestPos) then",null,3,null);//20
        src.addCodeLine("update best position of particle p:",null,4,null);//21
        src.addCodeLine("pBestPos <-- pCurrentPos",null,4,null);//22
        src.addCodeLine("if f(pCurrentPos) < f(bestGlobalPos)",null,4,null);//23
        src.addCodeLine("update the swarm's best known position:",null,5,null);//24
        src.addCodeLine("bestGlobalPos <-- pBestPos",null,5,null);//25
    }

    public String[] updateCurrentPosPointer(int currentPos, String[] pos) {
        for (int i = 0; i < pos.length; i++) {
            pos[i] = "";
        }
        pos[currentPos] = "-->";

        return pos;
    }

    public Polyline displayPlot(int x, int y, double[][] positions, double lb, double ub) {
        int size = 300;
        Coordinates[] coordsX = new Coordinates[size];
        Coordinates[] coordsY = new Coordinates[size];
        for (int i = 0; i < size; i++) {
            coordsX[i] = new Coordinates(x + i, y+(size/2));
            }
        for (int i = 0; i < size; i++) {
            coordsY[i] = new Coordinates(x+(size/2), y + i);
        }

        String[][] x1 = new String[1][1];
        String[][] x2 = new String[1][1];
        x1[0][0] = "x1";
        x2[0][0] = "x2";


        try {
            coordinateSystemX.hide();
            coordinateSystemY.hide();
            x_ax.hide();
            y_ax.hide();
        } catch (NullPointerException e) {

        }

        x_ax =  lang.newStringMatrix(new Coordinates(x+size/2, y), x1, "x1", null, matrixProps);
        y_ax =  lang.newStringMatrix(new Coordinates(x+size, y+size/2), x2, "x2", null, matrixProps);
        coordinateSystemX = lang.newPolyline(coordsX,"coordination",null, coordProbs);
        coordinateSystemY = lang.newPolyline(coordsY,"coordination",null, coordProbs);


        displayCircles(x,y,size,positions,lb,ub);


        return null;
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public Circle displayCircles(int x, int y, int size, double[][] positions, double lb, double ub) {
        Coordinates[] coords = new Coordinates[positions.length];
        double interval = Math.abs(lb) + Math.abs(ub);
        double scale = (size / 2) + interval;



        int[][] pos = new int[positions.length][2];
        double tmp1,tmp2;
        for (int i = 0; i < positions.length; i++) {
            tmp1 = ((positions[i][0] * interval*2 + size/2) + x);
//            tmp1 = clamp(tmp1,lb * interval + size/4 + x,ub * interval + size/4 + x);
            tmp1 = clamp(tmp1,x,x+size);
            tmp2 = ((-1*positions[i][1] * interval*2 + size/2) + y);
            tmp2 = clamp(tmp2,y,y+size);
//            tmp2 = clamp(tmp2,(lb * interval + size/4 + y),(ub * interval + size/4 + y));
            pos[i][0] = (int) tmp1;
            pos[i][1] = (int) tmp2;
        }


        for (int i = 0; i < positions.length; i++) {
            coords[i] = new Coordinates(pos[i][0],pos[i][1]);
            circles[i] = lang.newCircle(coords[i], 5, "particles", null, circleProbs);
        }

        return null;
    }



    public Text displayParticleIndex(int i) {
        particleIndex = lang.newText(new Coordinates(600, 40), "Particle: "+(i+1)+"/"+numParticles, "particleIndex",null,textParticleIndex);
        return particleIndex;
    }

    public Text displayIteration(int i) {
        iterationDisp = lang.newText(new Coordinates(770, 40), "Optimization iteration: "+(i+1)+"/"+(numIterations), "iteration",null,textIterations);
        return  iterationDisp;
    }

    public Text displayInitialization() {
        initDisp = lang.newText(new Coordinates(770, 40), "Initialization","initialization",null,textIterations);
        return initDisp;
    }

    public double himmelblau(double x, double y) {
        double result = (x*x + y - 11) * (x*x + y - 11) + (x + y*y - 7) * (x + y*y - 7);
        return result;
    }

    public void pso(int numParticles, double omega, double phiP, double phiG, double lb, double ub, int numIterations) {
        // initialize bounds
        double velUb, velLb, tmp, velocityX, velocityY, rp, rg, currentValue, bestValue;
        double posX, posY;
        double[][] positions_ = new double[numParticles][2];

        int optIndex = 0;
        double[] values = new double[numParticles];
        int iteration = 0;



//        displayQuestion();
        lang.nextStep();
        displayQuestion1();
        src.highlight(0);
        lang.nextStep();
        src.unhighlight(0);


        tmp = ub - lb;
        velLb = -Math.abs(tmp);
        velUb = Math.abs(tmp);
        UniformRealDistribution dist = new UniformRealDistribution(lb, ub);
        UniformRealDistribution distVel = new UniformRealDistribution(velLb, velUb);
        UniformRealDistribution distStd = new UniformRealDistribution(0, 1);

        Vector<Double> particleCurrentPosX = new Vector();
        Vector<Double> particleCurrentPosY = new Vector();
        Vector<Double> particleBestPosX = new Vector();
        Vector<Double> particleBestPosY = new Vector();
        Double particleBestGlobalPosX = 0.0;
        Double particleBestGlobalPosY = 0.0;

        Vector<Double> particleValue = new Vector();
        Vector<Double> particleVelocityX = new Vector();
        Vector<Double> particleVelocityY = new Vector();
        // initialize current minimum with infinity
        double globalMin = Double.MAX_VALUE;
        double[][] result = new double[numParticles][1];
        double[][] velocities = new double[numParticles][2];

        // initialize particle positions

        double[][] mat = new double[numParticles][2];

        for (int i = 0; i < numParticles; i++) {
            mat[i][0] = 0.0;
            mat[i][1] = 0.0;
        }
        for (int i = 0; i < numParticles; i++) {
            velocities[i][0] = 0.0;
            velocities[i][1] = 0.0;
        }


        String[][] s = new String[numParticles][1];
        String[][] openBrackets = new String[numParticles][1];
        String[][] closeBrackets = new String[numParticles][1];
        String[][] equals = new String[numParticles][1];
        String[][] currentRow = new String[numParticles][1];

        for (int i = 0; i < numParticles; i++) {
            openBrackets[i][0] = "(";
            closeBrackets[i][0] = ")";
            s[i][0] = "f";
            equals[i][0] = "=";
            currentRow[i][0] = "";
        }

        String[][] positionL = new String[1][1];
        positionL[0][0] = "current positions";

        String[][] positionBestL = new String[1][1];
        positionBestL[0][0] = "best positions";

        String[][] positions = new String[1][2];
        positions[0][0] = "x1";
        positions[0][1] = "x2";


        String[][] currentVLabel = new String[1][1];
        currentVLabel[0][0] = "current result";

        String[][] bestVLabel = new String[1][1];
        bestVLabel[0][0] = "best result";

        String[][] velocityLabel = new String[1][1];
        velocityLabel[0][0] = "velocities";

        String[][] velocityAxis = new String[1][2];
        velocityAxis[0][0] = "v1";
        velocityAxis[0][1] = "v2";


        String[][] himmelblauFunction = new String[1][1];
        himmelblauFunction[0][0] = "f (x1,x2) = (x1^2 + x2 - 11)^2 + (x1 + x2^2 -2)^2";
        String[][] functionLabel = new String[1][1];
        functionLabel[0][0] = "function";


        int cornerXSub = 105;
        int cornerYSub = 10;


        StringMatrix curRow = lang.newStringMatrix(new Coordinates(615 - cornerXSub, 150 - cornerYSub), currentRow, "currentRow", null, matrixPropsPointer);
        StringMatrix funcLabel = lang.newStringMatrix(new Coordinates(1250, 95),functionLabel, "fLabel", null, matrixProps);
        StringMatrix plotFun = lang.newStringMatrix(new Coordinates(1150, 115),himmelblauFunction, "himmelblau", null, matrixProps);
        StringMatrix vLabel = lang.newStringMatrix(new Coordinates(900 - cornerXSub, 105 - cornerYSub), velocityLabel, "velo", null, matrixProps);
        StringMatrix vAxLabel = lang.newStringMatrix(new Coordinates(905 - cornerXSub, 125 - cornerYSub), velocityAxis, "vel", null, matrixProps);
        DoubleMatrix veloc = lang.newDoubleMatrix(new Coordinates(885 - cornerXSub, 150 - cornerYSub), velocities, "positions", null, matrixProps);

        StringMatrix pLabel = lang.newStringMatrix(new Coordinates(660 - cornerXSub, 105 - cornerYSub), positionL, "posLa", null, matrixProps);
        StringMatrix positionCoordsLabel = lang.newStringMatrix(new Coordinates(690 - cornerXSub, 125 - cornerYSub), positions, "positions", null, matrixProps);
        StringMatrix sBrackets = lang.newStringMatrix(new Coordinates(660 - cornerXSub, 150 - cornerYSub), openBrackets, "brackets", null, matrixProps);
        StringMatrix sBrackets2 = lang.newStringMatrix(new Coordinates(770 - cornerXSub, 150 - cornerYSub), closeBrackets, "brackets", null, matrixProps);
        StringMatrix f = lang.newStringMatrix(new Coordinates(645 - cornerXSub, 150 - cornerYSub), s, "f",null,matrixProps);
        StringMatrix fbest = lang.newStringMatrix(new Coordinates(995 - cornerXSub, 150 - cornerYSub), s, "fbest",null,matrixProps);
        StringMatrix sBracketsBest = lang.newStringMatrix(new Coordinates(1010 - cornerXSub, 150 - cornerYSub), openBrackets, "brackets", null, matrixProps);
        StringMatrix sBracketsBest2 = lang.newStringMatrix(new Coordinates(1120 - cornerXSub, 150 - cornerYSub), closeBrackets, "brackets", null, matrixProps);
        StringMatrix pBestLabel = lang.newStringMatrix(new Coordinates(1010 - cornerXSub, 105 - cornerYSub), positionBestL, "posBLa", null, matrixProps);
        StringMatrix positionAxisBestLabel = lang.newStringMatrix(new Coordinates(1040 - cornerXSub, 125 - cornerYSub), positions, "positionsBest", null, matrixProps);
        StringMatrix eq = lang.newStringMatrix(new Coordinates(780 - cornerXSub, 150 - cornerYSub), equals, "eq",null,matrixProps);
        StringMatrix eqBest = lang.newStringMatrix(new Coordinates(1130 - cornerXSub, 150 - cornerYSub), equals, "eqBest",null,matrixProps);

        DoubleMatrix position = lang.newDoubleMatrix(new Coordinates(670 - cornerXSub, 150 - cornerYSub), mat,"matrix",null, matrixProps);
        DoubleMatrix positionBestAxisValues = lang.newDoubleMatrix(new Coordinates(1020 - cornerXSub, 150 - cornerYSub), mat,"matrix",null, matrixProps);

        StringMatrix cv = lang.newStringMatrix(new Coordinates(795 - cornerXSub, 125 - cornerYSub), currentVLabel, "cv",null,matrixProps);
        DoubleMatrix currentResult = lang.newDoubleMatrix(new Coordinates(795 - cornerXSub, 150 - cornerYSub), result,"result",null, matrixProps);

        StringMatrix bv = lang.newStringMatrix(new Coordinates(1145 - cornerXSub, 125 - cornerYSub), bestVLabel, "bv",null,matrixProps);
        DoubleMatrix bestResult = lang.newDoubleMatrix(new Coordinates(1145 - cornerXSub, 150 - cornerYSub), result,"bestResult",null, matrixProps);
        bestResult.highlightCell(optIndex,0,null,null);
        positionBestAxisValues.highlightCell(optIndex,0,null,null);
        positionBestAxisValues.highlightCell(optIndex,1,null,null);


        src.highlight(1);
        lang.nextStep();
        for (int j = 0; j < numParticles; j++) {
                positions_[j][0] = 0.0;
                positions_[j][1] = 0.0;
            }

        for (int i = 0; i < numParticles; i++) {
//            lang.nextStep();
            curRow.put(i,0, "-->", null, null);
            bestResult.unhighlightElem(optIndex,0,null,null);
            positionBestAxisValues.unhighlightElem(optIndex,0,null,null);
            positionBestAxisValues.unhighlightElem(optIndex,1,null,null);
            src.unhighlight(9);
            src.unhighlight(10);
            src.unhighlight(1);
            src.highlight(2);
            src.highlight(3);
            particleIndex.hide();
            displayParticleIndex(i);
            particleCurrentPosX.add(i, dist.sample());
            particleCurrentPosY.add(i, dist.sample());

            posX = (Math.round((particleCurrentPosX.get(i)*100.0))/100.0);
            posY = (Math.round((particleCurrentPosY.get(i)*100.0))/100.0);
            position.put(i,0, posX, null, null);
            position.put(i,1, posY, null, null);

            position.highlightElem(i,0,null,null);
            position.highlightElem(i,1,null,null);
            currentResult.highlightElem(i,0,null,null);

            positions_[i][0] = posX;
            positions_[i][1] = posY;

            displayPlot(1120, 220, positions_,lb, ub);
            for (int j = i+1; j < numParticles; j++) {
                circles[j].hide();
            }

            lang.nextStep();
            src.unhighlight(2);
            src.unhighlight(3);
            src.highlight(4);
            src.highlight(5);
            position.unhighlightElem(i,0,null,null);
            position.unhighlightElem(i,1,null,null);
            currentResult.unhighlightElem(i,0,null,null);
            particleBestPosX.add(i, particleCurrentPosX.get(i));
            particleBestPosY.add(i, particleCurrentPosY.get(i));
            positionBestAxisValues.put(i,0, Math.round(particleBestPosX.get(i)*100.0)/100.0,null,null);
            positionBestAxisValues.put(i,1, Math.round(particleBestPosY.get(i)*100.0)/100.0,null,null);

            tmp = himmelblau(particleCurrentPosX.get(i), particleCurrentPosY.get(i));
            particleValue.add(i, tmp);


            values[i] = (Math.round((tmp*100.0)))/100.0;
            currentResult.put(i,0, values[i],null,null);
            bestResult.put(i,0,values[i],null,null);

            currentResult.highlightElem(i,0,null,null);
            bestResult.highlightElem(i,0,null,null);
            positionBestAxisValues.highlightElem(i,0,null,null);
            positionBestAxisValues.highlightElem(i,1,null,null);
            result[i][0] = values[i];

            lang.nextStep();
            currentResult.unhighlightElem(i,0,null,null);
            bestResult.unhighlightElem(i,0,null,null);
            positionBestAxisValues.unhighlightElem(i,0,null,null);
            positionBestAxisValues.unhighlightElem(i,1,null,null);
            src.unhighlight(4);
            src.unhighlight(5);
            src.highlight(6);
            // evaluate positions

            // find current optimum
            if (tmp < globalMin) {
                lang.nextStep();
                src.unhighlight(6);
                src.highlight(7);
                src.highlight(8);
                globalMin = tmp;
                particleBestGlobalPosX = particleCurrentPosX.get(i);
                particleBestGlobalPosY = particleCurrentPosY.get(i);
                bestResult.unhighlightCell(optIndex,0,null,null);
                positionBestAxisValues.unhighlightElem(optIndex,0,null,null);
                positionBestAxisValues.unhighlightElem(optIndex,1,null,null);
                positionBestAxisValues.unhighlightCell(optIndex,0,null,null);
                positionBestAxisValues.unhighlightCell(optIndex,1,null,null);
                bestResult.highlightCell(i,0,null,null);
                bestResult.highlightElem(i,0,null,null);
                positionBestAxisValues.highlightElem(i,0,null,null);
                positionBestAxisValues.highlightElem(i,1,null,null);
                positionBestAxisValues.highlightCell(i,0,null,null);
                positionBestAxisValues.highlightCell(i,1,null,null);
                optIndex = i;

            }
            lang.nextStep();
            src.unhighlight(6);
            src.unhighlight(7);
            src.unhighlight(8);
            src.highlight(9);
            src.highlight(10);
            particleVelocityX.add(i, distVel.sample());
            particleVelocityY.add(i, distVel.sample());

            velocityX = particleVelocityX.get(i);
            velocityY = particleVelocityY.get(i);

            veloc.put(i,0,Math.round(velocityX*100.0)/100.0,null,null);
            veloc.put(i,1,Math.round(velocityY*100.0)/100.0,null,null);

            veloc.highlightElem(i,0,null,null);
            veloc.highlightElem(i,1,null,null);

            lang.nextStep();
            src.unhighlight(9);
            src.unhighlight(10);
            veloc.unhighlightElem(i,0,null,null);
            veloc.unhighlightElem(i,1,null,null);

            curRow.put(i,0, "", null, null);

            for (int j = 0; j < circles.length; j++) {
                circles[j].hide();
            }
        }

        for (int j = 0; j < circles.length; j++) {
            circles[j].hide();
        }

        for (int i = 0; i < numParticles; i++) {
            positions_[i][0] = particleCurrentPosX.get(i);
            positions_[i][1] = particleCurrentPosY.get(i);
        }

        displayPlot(1120, 220, positions_,lb, ub);

        for (int i = 0; i < numParticles; i++) {
                mat[i][0] = (Math.round((particleCurrentPosX.get(i)*100.0)))/100.0;
                mat[i][1] = (Math.round((particleCurrentPosY.get(i)*100.0)))/100.0;
        }
        for (int i = 0; i < numParticles; i++) {
            velocities[i][0] = (Math.round((particleVelocityX.get(i)*100.0)))/100.0;
            velocities[i][1] = (Math.round((particleVelocityY.get(i)*100.0)))/100.0;
        }


//        DoubleArray ia = lang.newDoubleArray(new Coordinates(600, 150), values, "doubleArray",
//                null, arrayProps);



//        ia.highlightCell(optIndex,null,null);


//        lang.nextStep();
        src.unhighlight(9);
        src.unhighlight(10);
        src.highlight(11);
        initDisp.hide();
        displayIteration(iteration);
        for (int k = 0; k < numIterations; k++) {

            src.highlight(11);
            lang.nextStep();
            src.unhighlight(11);
            src.unhighlight(19);
            src.unhighlight(22);
            src.highlight(12);
            for (int i = 0; i < numParticles; i++) {
                // iterate through function dimensions
                curRow.put(i,0, "-->", null, null);
                bestResult.unhighlightElem(optIndex,0,null,null);
                positionBestAxisValues.unhighlightElem(optIndex,0,null,null);
                positionBestAxisValues.unhighlightElem(optIndex,1,null,null);
                particleIndex.hide();
                displayParticleIndex(i);
                src.highlight(12);
                src.unhighlight(19);
                src.unhighlight(20);
                src.unhighlight(22);
                lang.nextStep();
                src.unhighlight(12);
                src.highlight(13);
                src.highlight(14);
                velocityX = particleVelocityX.get(i);
                velocityY = particleVelocityY.get(i);
                rp = distStd.sample();
                rg = distStd.sample();
                lang.nextStep();
                src.unhighlight(13);
                src.unhighlight(14);
                src.highlight(15);
                src.highlight(16);
                src.highlight(17);
                // update velocities
                velocityX = velocityX * omega + phiP * rp * (particleBestPosX.get(i) - particleCurrentPosX.get(i)) + phiG *
                        rg * (particleBestGlobalPosX - particleBestPosX.get(i));
                velocityY = velocityY * omega + phiP * rp * (particleBestPosY.get(i) - particleCurrentPosY.get(i)) + phiG *
                        rg * (particleBestGlobalPosY - particleBestPosY.get(i));

                veloc.put(i,0,Math.round(velocityX*100.0)/100.0,null,null);
                veloc.put(i,1,Math.round(velocityY*100.0)/100.0,null,null);

                veloc.highlightElem(i,0,null,null);
                veloc.highlightElem(i,1,null,null);

                particleVelocityX.set(i, velocityX);
                particleVelocityY.set(i, velocityY);

                lang.nextStep();
                veloc.unhighlightElem(i,0,null,null);
                veloc.unhighlightElem(i,1,null,null);
                src.unhighlight(15);
                src.unhighlight(16);
                src.unhighlight(17);
                src.highlight(18);
                src.highlight(19);
                // update particle positions
                particleCurrentPosX.set(i, particleCurrentPosX.get(i) + velocityX);
                particleCurrentPosY.set(i, particleCurrentPosY.get(i) + velocityY);


                positions_[i][0] = particleCurrentPosX.get(i);
                positions_[i][1] = particleCurrentPosY.get(i);

                for (int j = 0; j < circles.length; j++) {
                    circles[j].hide();
                }


                displayPlot(1120, 220, positions_,lb, ub);


                // update matrix position entries
                position.put(i,0,(Math.round((particleCurrentPosX.get(i)*100.0))/100.0), null, null);
                position.put(i,1,(Math.round((particleCurrentPosY.get(i)*100.0))/100.0), null, null);

                position.highlightElem(i,0,null,null);
                position.highlightElem(i,1,null,null);
                currentResult.highlightElem(i,0,null,null);


                currentValue = himmelblau(particleCurrentPosX.get(i), particleCurrentPosY.get(i));
                bestValue = himmelblau(particleBestPosX.get(i), particleBestPosY.get(i));

                // update result vector
                currentResult.put(i,0, (Math.round((currentValue*100.0)))/100.0,null,null);




                // update particle positions if conditions are met
                lang.nextStep();
                position.unhighlightElem(i,0,null,null);
                position.unhighlightElem(i,1,null,null);
                currentResult.unhighlightElem(i,0,null,null);

                src.unhighlight(18);
                src.unhighlight(19);
                src.highlight(20);
                if (currentValue < bestValue) {
                    lang.nextStep();
                    src.unhighlight(20);
                    src.highlight(21);
                    src.highlight(22);
                    // update best result for particle i
                    bestResult.put(i,0, (Math.round((currentValue*100.0)))/100.0,null,null);

                    bestResult.highlightElem(i,0,null,null);
                    positionBestAxisValues.highlightElem(i,0,null,null);
                    positionBestAxisValues.highlightElem(i,1,null,null);

                    positionBestAxisValues.put(i,0, Math.round(particleCurrentPosX.get(i)*100.0)/100.0,null,null);
                    positionBestAxisValues.put(i,1, Math.round(particleCurrentPosY.get(i)*100.0)/100.0,null,null);

                    particleBestPosX.set(i, particleCurrentPosX.get(i));
                    particleBestPosY.set(i, particleCurrentPosY.get(i));
                    particleValue.set(i, currentValue);

                    lang.nextStep();
                    bestResult.unhighlightElem(i,0,null,null);
                    positionBestAxisValues.unhighlightElem(i,0,null,null);
                    positionBestAxisValues.unhighlightElem(i,1,null,null);
                    src.unhighlight(21);
                    src.unhighlight(22);
                    src.highlight(23);
                    if (currentValue < globalMin) {
                        lang.nextStep();
                        src.unhighlight(23);
                        src.highlight(24);
                        src.highlight(25);
                        particleBestGlobalPosX = particleBestPosX.get(i);
                        particleBestGlobalPosY = particleBestPosY.get(i);
                        globalMin = currentValue;


//                        bestResult.highlightCell(i,0,null,null);

                        // update best result vector
                        bestResult.unhighlightCell(optIndex,0,null,null);
                        bestResult.highlightCell(i,0,null,null);
                        bestResult.highlightElem(i,0,null,null);
                        bestResult.put(i,0,(Math.round((currentValue*100.0)))/100.0,null, null);
//                        ia.highlightCell(i,null,null);
                        positionBestAxisValues.unhighlightCell(optIndex,0,null,null);
                        positionBestAxisValues.unhighlightCell(optIndex,1,null,null);
                        positionBestAxisValues.highlightCell(i,0,null,null);
                        positionBestAxisValues.highlightCell(i,1,null,null);
                        positionBestAxisValues.highlightElem(i,0,null,null);
                        positionBestAxisValues.highlightElem(i,1,null,null);
                        optIndex = i;
//                        System.out.println(globalMin);
//                        System.out.println("Best X: " + particleBestGlobalPosX + " Best Y: " + particleBestGlobalPosY);
                    }
                }
                lang.nextStep();
                src.unhighlight(20);
                src.unhighlight(23);
                src.unhighlight(24);
                src.unhighlight(25);
                curRow.put(i,0, "", null, null);
            }

            iteration += 1;
            iterationDisp.hide();
            displayIteration(iteration);
        }
        displayQuestion2();
        lang.nextStep();

        lang.nextStep();
        displayQuestion3();
        lang.nextStep();

        curRow.hide();
        funcLabel.hide();
        plotFun.hide();
        vLabel.hide();
        vAxLabel.hide();
        veloc.hide();
        pLabel.hide();
        positionCoordsLabel.hide();
        sBrackets.hide();
        sBrackets2.hide();
        sBracketsBest.hide();
        sBracketsBest2.hide();
        pBestLabel.hide();
        positionAxisBestLabel.hide();
        eq.hide();
        eqBest.hide();
        position.hide();
        positionBestAxisValues.hide();
        cv.hide();
        currentResult.hide();
        bv.hide();
        bestResult.hide();
        f.hide();
        for (int i = 0; i < circles.length; i++) {
            circles[i].hide();
        }
        coordinateSystemX.hide();
        coordinateSystemY.hide();
        x_ax.hide();
        y_ax.hide();
        fbest.hide();

    }

    public void displayQuestion1() {
        MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("MC1");
        question.setPrompt("How many minimums does the Himmelblau's function f(x) have?");
        question.addAnswer("0",0, "The himmelblau's function has 4 minimums");
        question.addAnswer("1", 0, "The himmelblau's function has 4 minimums");
        question.addAnswer("8", 0, "The himmelblau's function has 4 minimums");
        question.addAnswer("4", 1, "Correct");
        lang.addMCQuestion(question);
    }

    public void displayQuestion2() {
        MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("MC2");
        question.setPrompt("Does PSO always find a global optimum?");
        question.addAnswer("Yes",0, "No and the solutions may differ for each PSO run.");
        question.addAnswer("No", 1, "Correct");
        lang.addMCQuestion(question);
    }

    public void displayQuestion3() {
        MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("MC3");
        question.setPrompt("PSO does not use gradient information during the optimization. Does that make it " +
                "in principal more or less efficient?");
        question.addAnswer("More efficient that gradient based methods.",0, "Wrong, " +
                "Gradient based methods are more efficient because less function calls are exectued. However, PSO can " +
                "be run without expert knowledge of the problem domain.");
        question.addAnswer("Less efficient that gradient based methods.", 1, "Correct");
        lang.addMCQuestion(question);
    }

}