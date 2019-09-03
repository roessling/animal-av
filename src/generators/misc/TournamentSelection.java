/*
 * TournamentSelection.java
 * Lukas Weber, Idris Nematpur, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.Point;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
//import com.sun.media.sound.AiffFileReader;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.*;

public class TournamentSelection implements ValidatingGenerator {
    private Language lang;
    private String exp1str,exp2str,exp3str,exp4str,exp5str,exp6str,exp7str,exp8str,exp9str;
    private String dis1,dis2,dis3,dis4,dis5,dis6,dis7,dis8,dis9,dis10,dis11,dis12,dis13,dis14,dis15;
    private String pro1str,pro2str,pro3str,pro4str,con1str,con2str,con3str,con4str,con5str,add1str,add2str,add3str,add4str, add5str;
    private Random random = new Random(System.currentTimeMillis());
    private PointProperties pointProperties;
    private TextProperties titleTextProperties, windowTitleProperties;
    private PolylineProperties polylineProperties;
    private Point anker, headerAnchor, tsAnchor,populationAnchor, explanationAnchor, codeAnchor;
    private Rect slideRect;
    private Text slideHeader,slide1,slide2,slide3,slide4,slide5,slide6,slide7,slide8,slide9, slide10, slide11,
    slide12,slide13,slide14,slide15;
    private Text populationTitle, tsTitle, expTitle, srcTitle, proTitle, conTitle, addTitle;
    private Text pro1,pro2,pro3,pro4,con1,con2,con3,con4,con5,add1,add2,add3,add4, add5;
    private SourceCode code;
    private Font windowTitle;
    private int[] distribution = new int[18];

    //CONFIG PARAMS
    private int populationSize, depth;
    private Color textColor;
    private Color sampledColor = new Color(200, 200, 200);

    public void init() {
        lang = new AnimalScript("Tournament Selection", "Lukas Weber, Idris Nematpur", 820, 660);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        EllipseProperties ellipseProperties = (EllipseProperties)props.getPropertiesByName("ellipseProperties");
        SourceCodeProperties sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        TextProperties individualTextProperties = (TextProperties)props.getPropertiesByName("individualTextProperties");
        populationSize = (Integer)primitives.get("populationSize");
        depth = (Integer)primitives.get("tournamentDepth");
        RectProperties rectProperties = (RectProperties)props.getPropertiesByName("rectProperties");
        textColor = (Color)individualTextProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY);


        setupStrings();
        setupProperties();
        setupAnchorPoints();
        ArrayList<TournamentSelection.Individual> population = new ArrayList<>();
        ArrayList<TournamentSelection.Individual> players = new ArrayList<>();

        makeHeader(headerAnchor,rectProperties, titleTextProperties);
        lang.nextStep();
        showSlide(rectProperties);

        lang.nextStep("Vorbemerkung");
        hideSlide();
        makeAnimWindows(rectProperties);
        makeAnimWindowTitles();
        setupSourceCode(sourceCodeProperties);

        int columns = 6;
        int distance = (int)Math.ceil(390 / columns);
        double distanceRadiusRelation = 0.8;
        int radius = (int)Math.ceil(distance*distanceRadiusRelation / 2);
        int y_length = 250;
        boolean radiusTooBig = (distance*(populationSize/columns + 1) > y_length);
        while(radiusTooBig){
            columns++;
            distance = (int)Math.ceil(390 / columns);
            radius = (int)Math.ceil(distance*distanceRadiusRelation / 2);
            radiusTooBig = (distance*(populationSize/columns + 1) > y_length);
        }

        int x, y;
        for(int i = 0; i < populationSize;){
            x = (i%columns) * distance + distance/2 + 5;
            y = (i/columns) * distance + distance/2 + 40;
            int fitness =  random.nextInt(90) + 10;
            addToDistribution(fitness);
            Individual individual =  new Individual(lang, fitness, populationAnchor,
                    x, y, radius, "id_"+i, null, individualTextProperties, ellipseProperties);
            population.add(individual);
            i++;
        }
        makeHistogram(rectProperties);

        Text exp1 = lang.newText(new Offset(20, 37,explanationAnchor,"C"),exp1str,"exp1",null, individualTextProperties);
        lang.nextStep("Sampling");

        code.highlight(1);
        HashSet<Integer> samples = new HashSet<Integer>();
        while(samples.size() < Math.pow(2,depth)){
            samples.add(random.nextInt(populationSize));
        }
        int d = (int)Math.floor(390/Math.pow(2,depth));
        int r = (int)Math.min(d/2 * 0.9, 25);
        int x_pos = d/2 + 5;
        for(Integer i : samples){
            Individual player = new TournamentSelection.Individual(population.get(i),x_pos, 300-10-r, new Coordinates(r,r), tsAnchor);
            int ii = 0;
            players.add(player);
            population.get(i).ellipse.changeColor("fillColor", sampledColor, null, null);
            x_pos += d;
        }


        Text exp2 = lang.newText(new Offset(20, 62, explanationAnchor, "C"), exp2str,"exp2", null, individualTextProperties);
        Text exp3 = lang.newText(new Offset(20, 77, explanationAnchor, "C"), exp3str,"exp3", null, individualTextProperties);
        Text exp4 = lang.newText(new Offset(20, 92, explanationAnchor, "C"), exp4str,"exp3", null, individualTextProperties);
        Text exp5 = lang.newText(new Offset(20, 107, explanationAnchor, "C"), exp5str,"exp5", null, individualTextProperties);
        lang.nextStep("Tournament");

        Color wonColor = new Color(136,255, 155);
        Color lostColor = new Color(255, 139, 139);
        code.unhighlight(1);
        code.highlight(2);
        code.highlight(3);
        code.highlight(4);

        for(int round = 0; round < depth; ++round){
            for(int i = 0; i < players.size(); i += Math.pow(2,round+1)){
                Individual p1 = players.get(i);
                Individual p2 = players.get(i + (int)Math.pow(2,round));
                int height = (280-20-2*r)/(depth);
                int newPos_x = (p1.position.getX() + p2.position.getX())/2;
                int newPos_y = p1.position.getY() - height;
                if(p1.fitness < p2.fitness){
                    Individual winner = new Individual(p2, newPos_x, newPos_y, tsAnchor);
                    p1.ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, lostColor, null, null);
                    p2.ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, wonColor, null, null);
                    players.set(i, winner);
                    drawLines(p1, p2, winner);
                } else {
                    Individual winner = new Individual(p1, newPos_x, newPos_y, tsAnchor);
                    p1.ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, wonColor, null, null);
                    p2.ellipse.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, lostColor, null, null);
                    players.set(i, winner);
                    drawLines(p1, p2, winner);
                }
            }
            if(round == depth-1 ){
                Text exp6 = lang.newText(new Offset(20, 133, explanationAnchor, "C"), exp6str,"exp6", null, individualTextProperties);
                Text exp7 = lang.newText(new Offset(20, 148, explanationAnchor, "C"), exp7str,"exp7", null, individualTextProperties);
            }
            lang.nextStep();
        }

        code.unhighlight(2);
        code.unhighlight(3);
        code.unhighlight(4);
        code.highlight(5);
        Text exp8 = lang.newText(new Offset(20, 188, explanationAnchor, "C"), exp8str,"exp8", null, individualTextProperties);
        Individual final_winner = new Individual(players.get(0), 200, 250, explanationAnchor);

        lang.nextStep();
        lang.hideAllPrimitives();
        makeHeader(headerAnchor,rectProperties,titleTextProperties);
        makeFinalWindows(rectProperties);
        makeFinalWindowTitles();
        makeFinalWindowText(individualTextProperties);
        lang.nextStep("Zusatzinformationen");
        return lang.toString();
    }

    private void makeHistogram(RectProperties rectProperties) {
        System.out.println(distribution);
        RectProperties histoRectProp = rectProperties;
        histoRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 50);
        histoRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(230,230,230));
        for(int i = 0; i < 18; ++i){
            lang.newRect(new Offset(20+i*20, 290-distribution[i]*10,populationAnchor, "C"), new Offset(20+(i+1)*20, 290, populationAnchor, "C"),
                    "histo"+i, null, histoRectProp);
        }
    }

    private void drawLines(Individual p1, Individual p2, Individual winner) {
        Offset p1_loc =  new Offset(p1.position.getX(), p1.position.getY(), tsAnchor, "C");
        Offset p2_loc =  new Offset(p2.position.getX(), p2.position.getY(), tsAnchor, "C");
        Offset win_loc = new Offset(winner.position.getX(), winner.position.getY(), tsAnchor, "C");
        int middle_height = (p1.position.getY() + winner.position.getY())/2;
        Offset leftCorner = new Offset(p1_loc.getX(),  middle_height, tsAnchor, "C");
        Offset righCorner = new Offset(p2_loc.getX(),  middle_height, tsAnchor, "C");
        Offset middle = new Offset(win_loc.getX(),  middle_height, tsAnchor, "C");
        Node[] p1_vert = {p1_loc, leftCorner};
        Node[] p2_vert = {p2_loc, righCorner};
        Node[] horizontal = {leftCorner, righCorner};
        Node[] winner_vert = {middle, win_loc};
        lang.newPolyline(p1_vert, "p1v", null, polylineProperties);
        lang.newPolyline(p2_vert, "p2v", null, polylineProperties);
        lang.newPolyline(horizontal, "hor", null, polylineProperties);
        lang.newPolyline(winner_vert, "winv", null, polylineProperties);
    }

    private void addToDistribution(int fitness){
        int slot = (fitness-10)/5;
        distribution[slot] = distribution[slot] + 1;
    }

    private void makeHeader(Point ref, RectProperties rp,
                            TextProperties tp){
        makeBackgroundRectangle(ref, 805, 40, "headerBGRect", rp);
        Text title = lang.newText(new Offset(250, 16, ref, "C"), "Tournament Selection", "title",
                null, tp);
        title.setFont(new Font("SansSerif", Font.BOLD, 28),null,null);
    }

    private Primitive makeBackgroundRectangle(Point anchor, int lr_x, int lr_y, String id, RectProperties rectProperties) {
        return lang.newRect(new Offset(0, 0, anchor, "C"), new Offset(lr_x, lr_y, anchor, "C"), id, null, rectProperties);
    }

    public String getName() {
        return "Tournament Selection";
    }

    public String getAlgorithmName() {
        return "Tournament Selection";
    }

    public String getAnimationAuthor() {
        return "Lukas Weber, Idris Nematpur";
    }

    public String getDescription() {
        return "Bei der Roulette Wheel Selection handelt es sich um einen Selektionsalgorithmus"
                + "\n"
                + "aus der Klasse der Genetischen Algorithmen. Dementsprechend ist die"
                + "\n"
                + "RWS eine probabilistische Methode, um aus einer gegeben"
                + "\n"
                + "Population ein Individuum zu selektieren. "
                + "\n"
                + "Die Seletion erfolgt dabei proportional zu der Fitness der Individuen." +
                "\n" +
                "Dabei wird eine Art Roulettekessel angelegt, in welchem jedes Individuum" +
                "\n" +
                "über ein Feld verfügt. Zusätzlich ist die Größe dieses Feldes" +
                "\n" +
                "Proportional zum Quotienten aus Individuenfitness und Gesamtfitness." +
                "\n" +
                "Die Selektion erfolgt dann metaphorisch durch das Werfen einer Kugel.";
    }

    public String getCodeExample() {
        String src = "";
        src += "public Individual rwSelection(Individual[] population) {\n";
        src += "\tint sum = calculateFitnessSum();\n";
        src += "\tfor (int i = 0; i < population.length; i++) {\n";
        src += "\t\taddWheelSegment(population[i]);\n";
        src += "\t\t}\n";
        src += "\tint selector = randInt(360);\n";
        src += "\treturn selectSegmentAtAngle(selector);\n";
        src += "}";
        return src;
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    private int findSelectedIndividual(int angle, int[] angles, ArrayList<Individual> pop){
        int start = 0;
        for(int i = 0; i< pop.size(); i++ ){
            int end = angles[i];
            if(angle >= start && angle < end){
                return i;
            }
            start = end;
        }
        return pop.size()-1;
    }



    private void showSlide(RectProperties rectProperties){
        slideRect = lang.newRect(new Offset(0,0,tsAnchor,"C"), new Offset(805, 600, tsAnchor, "C"), "slideBG",
                null, rectProperties);
        slideHeader = lang.newText(new Offset(20, 20, tsAnchor,"C"),"Vorbemerkung","dishead",null,
                titleTextProperties);
        slide1 = lang.newText(new Offset(20, 60, tsAnchor,"C"),dis1,"dis1",null, titleTextProperties);
        slide2 = lang.newText(new Offset(20, 80, tsAnchor,"C"),dis2,"dis2",null, titleTextProperties);
        slide3 = lang.newText(new Offset(20, 100, tsAnchor,"C"),dis3,"dis3",null, titleTextProperties);
        slide4 = lang.newText(new Offset(20, 120, tsAnchor,"C"),dis4,"dis4",null, titleTextProperties);
        slide5 = lang.newText(new Offset(20, 140, tsAnchor,"C"),dis5,"dis5",null, titleTextProperties);
        slide6 = lang.newText(new Offset(20, 160, tsAnchor,"C"),dis6,"dis6",null, titleTextProperties);
        slide7 = lang.newText(new Offset(20, 180, tsAnchor,"C"),dis7,"dis7",null, titleTextProperties);
        slide8 = lang.newText(new Offset(20, 200, tsAnchor,"C"),dis8,"dis8",null, titleTextProperties);
        slide9 = lang.newText(new Offset(20, 220, tsAnchor,"C"),dis9,"dis9",null, titleTextProperties);
        slide10 = lang.newText(new Offset(20, 240, tsAnchor,"C"),dis10,"dis10",null, titleTextProperties);
        slide11 = lang.newText(new Offset(20, 260, tsAnchor,"C"),dis11,"dis11",null, titleTextProperties);
        slide12 = lang.newText(new Offset(20, 280, tsAnchor,"C"),dis12,"dis12",null, titleTextProperties);
        slide13 = lang.newText(new Offset(20, 300, tsAnchor,"C"),dis13,"dis13",null, titleTextProperties);
        slide14 = lang.newText(new Offset(20, 320, tsAnchor,"C"),dis14,"dis14",null, titleTextProperties);
        slide15 = lang.newText(new Offset(20, 340, tsAnchor,"C"),dis15,"dis15",null, titleTextProperties);
        Font slideFont = new Font("SansSerif", Font.BOLD, 17);
        Font slideHead = new Font("SansSerif", Font.BOLD, 25);
        slideHeader.setFont(slideHead, null, null);
        slide1.setFont(slideHead, null, null);
        slide1.setFont(slideFont, null, null);
        slide2.setFont(slideFont, null, null);
        slide3.setFont(slideFont, null, null);
        slide4.setFont(slideFont, null, null);
        slide5.setFont(slideFont, null, null);
        slide6.setFont(slideFont, null, null);
        slide7.setFont(slideFont, null, null);
        slide8.setFont(slideFont, null, null);
        slide9.setFont(slideFont, null, null);
        slide10.setFont(slideFont, null, null);
        slide11.setFont(slideFont, null, null);
        slide12.setFont(slideFont, null, null);
        slide13.setFont(slideFont, null, null);
        slide14.setFont(slideFont, null, null);
        slide15.setFont(slideFont, null, null);
    }

    private void makeFinalWindowText(TextProperties individualTextProperties){
        Font f = new Font("SansSerif", Font.PLAIN, 17);
        pro1 = lang.newText(new Offset(20,37, tsAnchor,"C"),pro1str,"pro1",null, individualTextProperties);
        pro2 = lang.newText(new Offset(20,57, tsAnchor,"C"),pro2str,"pro2",null, individualTextProperties);
        pro3 = lang.newText(new Offset(20,77, tsAnchor,"C"),pro3str,"pro3",null, individualTextProperties);
        pro4 = lang.newText(new Offset(20,97, tsAnchor,"C"),pro4str,"pro4",null, individualTextProperties);
        con1 = lang.newText(new Offset(20,37, explanationAnchor,"C"),con1str,"con1",null, individualTextProperties);
        con2 = lang.newText(new Offset(20,57, explanationAnchor,"C"),con2str,"con2",null, individualTextProperties);
        con3 = lang.newText(new Offset(20,77, explanationAnchor,"C"),con3str,"con3",null, individualTextProperties);
        con4 = lang.newText(new Offset(20,97, explanationAnchor,"C"),con4str,"con4",null, individualTextProperties);
        con5 = lang.newText(new Offset(20,117, explanationAnchor,"C"),con5str,"con5",null, individualTextProperties);
        add1 = lang.newText(new Offset(20,37, populationAnchor,"C"),add1str,"add1",null, individualTextProperties);
        add2 = lang.newText(new Offset(20,57, populationAnchor,"C"),add2str,"add2",null, individualTextProperties);
        add3 = lang.newText(new Offset(20,77, populationAnchor,"C"),add3str,"add3",null, individualTextProperties);
        add4 = lang.newText(new Offset(20,97, populationAnchor,"C"),add4str,"add4",null, individualTextProperties);
        add5 = lang.newText(new Offset(20,117, populationAnchor,"C"),add5str,"add5",null, individualTextProperties);
        pro1.setFont(f,null,null);
        pro2.setFont(f,null,null);
        pro3.setFont(f,null,null);
        pro4.setFont(f,null,null);
        con1.setFont(f,null,null);
        con2.setFont(f,null,null);
        con3.setFont(f,null,null);
        con4.setFont(f,null,null);
        con5.setFont(f,null,null);
        add1.setFont(f,null,null);
        add2.setFont(f,null,null);
        add3.setFont(f,null,null);
        add4.setFont(f,null,null);
        add5.setFont(f,null,null);
    }

    private void hideSlide(){
        slideRect.hide();
        slideHeader.hide();
        slide1.hide();
        slide2.hide();
        slide3.hide();
        slide4.hide();
        slide5.hide();
        slide6.hide();
        slide7.hide();
        slide8.hide();
        slide9.hide();
        slide10.hide();
        slide11.hide();
        slide12.hide();
        slide13.hide();
        slide14.hide();
        slide15.hide();
    }

    private void makeAnimWindows(RectProperties rectProperties){
        makeBackgroundRectangle(tsAnchor, 400, 300, "tournementBGRect", rectProperties);
        makeBackgroundRectangle(populationAnchor, 400, 300, "populationBGRect", rectProperties);
        makeBackgroundRectangle(explanationAnchor, 400, 300, "explanationBGRect", rectProperties);
        makeBackgroundRectangle(codeAnchor, 400, 300, "codeBGRect", rectProperties);
    }

    private void makeFinalWindows(RectProperties rectProperties){
        makeBackgroundRectangle(tsAnchor, 400, 300, "ProPane", rectProperties);
        makeBackgroundRectangle(explanationAnchor, 400, 300, "ConPane", rectProperties);
        makeBackgroundRectangle(populationAnchor, 805, 300, "AddPane", rectProperties);
    }

    private void makeFinalWindowTitles(){
        proTitle = lang.newText(new Offset(20,6,tsAnchor,"C"), "Vorteile", "ProTitle", null, windowTitleProperties);
        conTitle = lang.newText(new Offset(20,6,explanationAnchor,"C"), "Nachteile", "ConTitle",null,
                windowTitleProperties);
        addTitle = lang.newText(new Offset(20,6, populationAnchor, "C"), "Zusatzinformationen", "addTitle",
                null, windowTitleProperties);
        proTitle.setFont(windowTitle, null, null);
        conTitle.setFont(windowTitle, null, null);
        addTitle.setFont(windowTitle, null, null);
    }

    private void makeAnimWindowTitles(){
        populationTitle = lang.newText(new Offset(20, 6, populationAnchor, "C"), "Population",
                "popTitle", null, windowTitleProperties);
        tsTitle = lang.newText(new Offset(20, 6, tsAnchor, "C"), "Tournament - Turniertiefe: " + depth,
                "tsTitle",null, windowTitleProperties);
        expTitle = lang.newText(new Offset(20, 6,explanationAnchor, "C"), "Erklärung", "expText", null,
                windowTitleProperties);
        srcTitle = lang.newText(new Offset(20, 6, codeAnchor, "C"), "Source Code", "srcText", null,
                windowTitleProperties);
        populationTitle.setFont(new Font("SansSerif", Font.BOLD, 14),null,null);
        tsTitle.setFont(windowTitle,null,null);
        expTitle.setFont(windowTitle, null, null);
        srcTitle.setFont(windowTitle, null, null);
    }

    private void setupSourceCode(SourceCodeProperties sourceCodeProperties){
        code = lang.newSourceCode(new Offset(20,37,codeAnchor, "C"),"srcCode",null,sourceCodeProperties);
        code.addCodeLine("public Individual tSelection(Individual[] population, int t){", "sig",0, null);
        code.addCodeLine("  Individual[] players = sampleIndividuals(population, t);","line1", 1, null);
        code.addCodeLine("  for(players.length > 1){","line2", 2,null);
        code.addCodeLine("    players =  getWinners(players);","line2", 2, null);
        code.addCodeLine("  }", "line2", 2,null);
        code.addCodeLine("  return players[0];", "line3",2,null);
        code.addCodeLine("}","line4",0,null);
    }

    private void setupStrings(){
        exp1str = "Als erstes sampeln wir Individuen für das Turnierverfahren";
        exp2str = "Nun beginnt die Turnierselektion bei der in jeder Runde";
        exp3str = "jeweils zwei Individuen miteinander konkurieren und der";
        exp4str = "Fitnessstärkere die nächste Runde erreicht. Bei gleicher";
        exp5str = "Fitness ist es egal wer weiter kommt";
        exp6str = "Das Individum welches übrig bleit ist der Gewinner";
        exp7str = "der Turnierselektion, welches zurück gegeben wird.";
        exp8str = "Es wird folgendes Individuum selektiert:";

        dis1 = "Im Folgenden betrachen wir die Turnierselektion. Es handelt sich dabei um ein Verfahren";
        dis2 = "des Genetic Programming. Turnierselektion stellt ein Selektionsverfahren dar, bei";
        dis3 = "dem die Wahrscheinlichkeit, dass ein Individuum ausgewählt wird, von der Verteilung";
        dis4 = "der Fitnesswerte abhängt, und nur teilweise vom eigentlichen Fitnesswert.";
        dis5 = "Ziel der Turnierselektion ist es probabilistisch ein Individuum aus der gegebenen";
        dis6 = "Polulation zu selektieren. Im nachfolgenden Schritt wird dieses Individuum mit anderen";
        dis7 = "Individuen gekreuzt oder mutiert.";
        dis8 = "Das Verfahren wählt zunächst zufällig eine Teilmenge der Polulation aus. Die Größe der Menge";
        dis9 = "hängt von der Turniertiefe ab. Die Individuen dieser Teilmenge konkurieren dann in einem ";
        dis10 = "Turnierverfahren bei dem das Individuum mit der besten Fitness bestimmt wird.";
        dis11 = "Wichtig ist, dass bei der Selektion natürliche Prozesse modelliert werden sollten. ";
        dis12 = "Dementsprechend ist die Wahl eines Individuums für das Turnierverfahren zwar zufällig, ";
        dis13 = "jedoch ist die Verteilung der Fitnesswerte der Teilmenge abhängig von der Verteilung der ";
        dis14 = "Fitness der Gesamtpopulation, sodass Individuen mit einer höheren oder oft auftrettenden ";
        dis15 = "Fitness bevorzugt werden.";

        pro1str = "Die Selektion erfolgt proportional zur Verteilung";
        pro2str = "der Fitnesswerte. Auch schlechtere Individuen";
        pro3str = "werden selektiert, wobei die schlechtesten durch";
        pro4str = "das Turnierverfahren aussortiert werden.";

        con1str = "Bei sehr einseitiger Verteilung der Fitnesswerte";
        con2str = "dominieren die häufig auftretenden Individuen,";
        con3str = "welche nicht unbedingt die besten sein müssen";
        con4str = "Bei falscher Wahl der Turniertiefe werden nur die";
        con5str = "besten ausgewählt.";

        add1str = "Bei der Turnierselektion ist die Turniertiefe nicht vorgegeben. In der";
        add2str = "Praxis wird jedoch meistens eine Turnierttiefe von 1 gewählt, bei der lediglich";
        add3str = "2 Elemente gesampelt werden und im Turnier miteinander verglichen werden.";
        add4str = "Hierfür wurde nämlich gezeigt, dass es schon ausreichend ist um eine genügende";
        add5str = "Diversität der Individuen zu erhalten.";
    }

    private void setupProperties(){
        windowTitle = new Font("SansSerif", Font.BOLD, 14);
        pointProperties = new PointProperties("PP");
        titleTextProperties = new TextProperties("");
        titleTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor.brighter());
        windowTitleProperties = new TextProperties("");
        windowTitleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor.brighter().brighter());
        polylineProperties = new PolylineProperties("Connector");
        polylineProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
    }

    private void setupAnchorPoints(){
        anker = lang.newPoint(new Coordinates(0, 0), "Ankerpunkt", null, pointProperties);
        headerAnchor = lang.newPoint(new Offset(10, 10, anker, "C"), "HeaderAnchor", null, pointProperties);
        tsAnchor = lang.newPoint(new Offset(0, 45, headerAnchor, "C"), "TournamentAnchor", null, pointProperties);
        populationAnchor = lang.newPoint(new Offset(0, 350, headerAnchor, "C"),"PopAnchor", null, pointProperties);
        explanationAnchor = lang.newPoint(new Offset(405, 45, headerAnchor, "C"), "ExpAnchor", null, pointProperties);
        codeAnchor = lang.newPoint(new Offset(405, 350, headerAnchor, "C"), "CodeAnchor", null, pointProperties);
        anker.hide();
        headerAnchor.hide();
        tsAnchor.hide();
        populationAnchor.hide();
        explanationAnchor.hide();
        codeAnchor.hide();
    }


    public static void main(String[] args) {
        TournamentSelection ts = new TournamentSelection();
        ts.init();
        String s = ts.generate(null, null);
        //System.out.println(s);
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        boolean valid = true;
        if(depth < 1 || depth > 4) valid = false;
        if(populationSize > 200 || populationSize < Math.pow(2, depth)) valid = false;
        return true;//TODO important!!!
    }

    class Individual {
        private Language language;
        private DisplayOptions displayOptions;
        public EllipseProperties ellipseProperties;
        private TextProperties textProperties;
        private Offset position;
        private Coordinates radius;
        private Color fight = null, win = null, lose = null;

        private String id;
        private Ellipse ellipse;
        private Text text;
        private int fitness;
        private boolean hidden;

        Individual(Language lang, int fitnessValue, Primitive ref, int x, int y, int rad, String ident, DisplayOptions dp,
                   TextProperties tp, EllipseProperties ep) {
            this.hidden = false;
            this.language = lang;
            this.fitness = fitnessValue;
            this.position = new Offset(x, y, ref, "C");
            this.displayOptions = dp;
            this.ellipseProperties = ep;
            this.textProperties = tp;
            this.id = ident;
            this.radius = new Coordinates(rad, rad);
            drawInitial();
        }

        Individual(Individual other, int x, int y, Primitive ref) {
            this.hidden = other.hidden;
            this.language = other.language;
            this.displayOptions = other.displayOptions;
            this.ellipseProperties = other.ellipseProperties;
            this.textProperties = other.textProperties;
            this.position = new Offset(x, y, ref, "C");
            this.radius = other.radius;
            this.id = other.id + "clone";
            this.fitness = other.fitness;
            this.hidden = false;
            drawInitial();
        }

        Individual(Individual other, int x, int y, Coordinates radius, Primitive ref) {
            this.hidden = other.hidden;
            this.language = other.language;
            this.displayOptions = other.displayOptions;
            this.ellipseProperties = other.ellipseProperties;
            this.textProperties = other.textProperties;
            this.position = new Offset(x, y, ref, "C");
            this.radius = radius;
            this.id = other.id + "clone";
            this.fitness = other.fitness;
            this.hidden = false;
            drawInitial();
        }

        Individual(Individual other, int x, int y) {
            this.hidden = other.hidden;
            this.language = other.language;
            this.displayOptions = other.displayOptions;
            this.ellipseProperties = other.ellipseProperties;
            this.textProperties = other.textProperties;
            this.position = new Offset(x, y, other.position.getRef(), "C");
            this.radius = other.radius;
            this.id = other.id + "clone";
            this.fitness = other.fitness;
            this.hidden = false;
            drawInitial();
        }

        private void drawInitial() {
            this.ellipse = language.newEllipse(position, radius, id + "ellipse", displayOptions, ellipseProperties);
            Font font = (Font) textProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
            int fontsize = font.getSize();
            Offset offset = new Offset(-fontsize / 2 - 1, -fontsize / 2 - 1, ellipse, "C");
            this.text = language.newText(offset, "" + fitness, id + "text", displayOptions, textProperties);
        }


        public int getFitness() {
            return fitness;
        }
    }

}