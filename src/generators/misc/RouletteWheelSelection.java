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
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

public class RouletteWheelSelection implements Generator {
    private Language lang;
    private String exp1str,exp2str,exp3str,exp4str,exp5str,exp6str,exp7str,exp8str,exp9str;
    private String dis1,dis2,dis3,dis4,dis5,dis6,dis7,dis8,dis9,dis10,dis11,dis12,dis13,dis14,dis15;
    private String pro1str,pro2str,pro3str,pro4str,con1str,con2str,con3str,con4str,add1str,add2str,add3str,add4str, add5str;
    private Random random = new Random(System.currentTimeMillis());
    private PointProperties pointProperties;
    private ArcProperties selectorProperties;
    private TextProperties titleTextProperties, windowTitleProperties;

    private Point anker, headerAnchor, rwsAnchor,populationAnchor, explanationAnchor, codeAnchor;
    private Rect slideRect;
    private Text slideHeader,slide1,slide2,slide3,slide4,slide5,slide6,slide7,slide8,slide9, slide10, slide11,
    slide12,slide13,slide14,slide15;
    private Text populationTitle, rwsTitle, expTitle, srcTitle, proTitle, conTitle, addTitle;
    private Text pro1,pro2,pro3,pro4,con1,con2,con3,con4,add1,add2,add3,add4, add5;
    private SourceCode code;
    private Font windowTitle;
    private boolean rankBasedSelect;

    //CONFIG PARAMS
    private int populationSize;
    private Color textColor;

    public void init() {
        lang = new AnimalScript("Roulette Wheel Selection", "Lukas Weber, Idris Nematpur", 820, 660);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        rankBasedSelect = false;
        populationSize = (Integer)primitives.get("populationSize");

        EllipseProperties ellipseProperties = (EllipseProperties)props.getPropertiesByName("ellipseProperties");
        TextProperties individualTextProperties = (TextProperties)props.getPropertiesByName("individualTextProperties");
        SourceCodeProperties sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        ArcProperties arcProperties = (ArcProperties)props.getPropertiesByName("arcProperties");
        RectProperties rectProperties = (RectProperties)props.getPropertiesByName("rectProperties");
        textColor = (Color)individualTextProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY);


        setupStrings();
        setupProperties(individualTextProperties);
        setupAnchorPoints();

        ArrayList<RouletteWheelSelection.Individual> population = new ArrayList<>();
        ArrayList<Color> colors = calculateColorArray(populationSize);
        ArrayList<Arc> segments = new ArrayList<>();
        Variables vars = lang.newVariables();
        vars.declare("int", "accFitness", "0", "Overall Population Fitness");
        vars.declare("int", "currAccFitness","0", "currently accumulated Fitness");
        vars.declare("int", "currIndivFitness", "0", "Fitness of the current Individual");
        vars.declare("double", "currIndivFraction", "0.0", "Fraction of the current Individual");
        vars.declare("int", "generatedAngle", "0", "Angle for Individual Selection");

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
            int fitness = rankBasedSelect ? i + 1: random.nextInt(90) + 10;
            Individual individual =  new Individual(lang, fitness, populationAnchor,
                    x, y, radius, "id_"+i, null, individualTextProperties, ellipseProperties);
            population.add(individual);
            i++;
        }

        lang.nextStep("Wheel Generierung");
        Point center = lang.newPoint(new Offset(200,175, rwsAnchor, "C"), "center", null, pointProperties);
        int rwsRadius = 120;
        int accumulatedFitness = 0;
        code.highlight(1);
        for(RouletteWheelSelection.Individual i : population){
            accumulatedFitness += i.getFitness();
        }
        vars.set("accFitness", ""+accumulatedFitness);
        Text exp1 = lang.newText(new Offset(20, 37,explanationAnchor,"C"),exp1str,"exp1",null, individualTextProperties);
        populationTitle.setText(populationTitle.getText() + " - Accumulated Fitness: " + accumulatedFitness,
                null,null);
        lang.nextStep();
        code.unhighlight(1);
        code.highlight(2);
        code.highlight(3);
        code.highlight(4);

        Text exp2 = lang.newText(new Offset(20, 62, explanationAnchor, "C"), exp2str,"exp2", null, individualTextProperties);
        Text exp3 = lang.newText(new Offset(20, 77, explanationAnchor, "C"), exp3str,"exp3", null, individualTextProperties);
        Text exp4 = lang.newText(new Offset(20, 92, explanationAnchor, "C"), exp4str,"exp3", null, individualTextProperties);
        int accu = 0;
        vars.set("currAccFitness", ""+ accu);
        int[] angles = new int[populationSize];
        for(int i = 0; i < populationSize; i++){
            RouletteWheelSelection.Individual individual = population.get(i);
            accu += individual.getFitness();
            vars.set("currAccFitness", ""+ accu);
            rwsTitle.setText("Roulette Wheel - Accumulated Fitness: " + accu, null,null);
            for(Arc a : segments)a.hide();
            segments.clear();
            int startAngle = 0;
            arcProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
            for(int j = 0; j <= i; j++){
                RouletteWheelSelection.Individual indiv = population.get(j);
                double fraction = indiv.getFitness() / (double) accu;
                int angle = (int)Math.round(360 * fraction);
                arcProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, colors.get(j));
                arcProperties.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, startAngle<360?startAngle:359);
                if(j == i && startAngle+angle < 360){
                    arcProperties.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 360-startAngle);
                }else arcProperties.set(AnimationPropertiesKeys.ANGLE_PROPERTY, angle);
                Arc arc = lang.newArc(new Offset(0,0,center,"C"),new Coordinates(rwsRadius,rwsRadius),
                        "seg"+ i+j, null, arcProperties);
                startAngle += angle;
                segments.add(arc);
                arcProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
                angles[j] = startAngle;
                if(j==i){
                    vars.set("currIndivFraction", ""+ fraction);
                    vars.set("currIndivFitness", ""+ individual.getFitness());
                }
            }
            individual.ellipse.changeColor("fillColor",colors.get(i),null,null);
            lang.nextStep();
        }
        code.unhighlight(2);
        code.unhighlight(3);
        code.unhighlight(4);
        code.highlight(5);

        Text exp5 = lang.newText(new Offset(20, 117, explanationAnchor, "C"), exp5str,"exp5", null, individualTextProperties);
        Text exp6 = lang.newText(new Offset(20, 133, explanationAnchor, "C"), exp6str,"exp6", null, individualTextProperties);
        Text exp7 = lang.newText(new Offset(20, 148, explanationAnchor, "C"), exp7str,"exp7", null, individualTextProperties);
        Text exp8 = lang.newText(new Offset(20, 163, explanationAnchor, "C"), exp8str,"exp8", null, individualTextProperties);
        int generatedAngle = random.nextInt(360) + 1;
        vars.set("generatedAngle", ""+ generatedAngle);
        rwsTitle.setText("Roulette Wheel - Generated Angle: " + generatedAngle, null,null);
        selectorProperties.set(AnimationPropertiesKeys.ANGLE_PROPERTY, generatedAngle);
        Arc select = lang.newArc(new Offset(0,0,center,"C"),new Coordinates(rwsRadius-15, rwsRadius-15),"selector",
                null, selectorProperties);
        lang.nextStep("Selektion");
        code.unhighlight(5);
        code.highlight(6);
        Text exp9 = lang.newText(new Offset(20, 188, explanationAnchor, "C"), exp9str,"exp9", null, individualTextProperties);
        int selectedNumber = findSelectedIndividual(generatedAngle, angles, population);
        Individual individual = population.get(selectedNumber);
        Individual selected = new Individual(individual, 200, 250, explanationAnchor);
        selected.ellipse.changeColor("fillColor",colors.get(selectedNumber),null,null);

        lang.nextStep();
        lang.hideAllPrimitives();
        makeHeader(headerAnchor,rectProperties,titleTextProperties);
        makeFinalWindows(rectProperties);
        makeFinalWindowTitles();
        makeFinalWindowText(individualTextProperties);
        lang.nextStep("Zusatzinformationen");
        return lang.toString();
    }

    private void makeHeader(Point ref, RectProperties rp,
                            TextProperties tp){
        makeBackgroundRectangle(ref, 805, 40, "headerBGRect", rp);
        Text title = lang.newText(new Offset(250, 16, ref, "C"), "Roulette Wheel Selection", "title",
                null, tp);
        title.setFont(new Font("SansSerif", Font.BOLD, 28),null,null);
    }

    private Primitive makeBackgroundRectangle(Point anchor, int lr_x, int lr_y, String id, RectProperties rectProperties) {
        return lang.newRect(new Offset(0, 0, anchor, "C"), new Offset(lr_x, lr_y, anchor, "C"), id, null, rectProperties);
    }

    public String getName() {
        return "Roulette Wheel Selection";
    }

    public String getAlgorithmName() {
        return "Roulette Wheel Selection";
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
                "ueber ein Feld verfuegt. Zusätzlich ist die Größe dieses Feldes" +
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

    private ArrayList<Color> calculateColorArray(int number){
        ArrayList<Color> colors = new ArrayList<>();
        float angle = 0.0f;
        float increment = 360.0f/number;
        for(int i = 0; i < number; i ++){
            float hue = angle/360;
            float sat = 0.6f;
            float bright = 1.0f;
            Color c = Color.getHSBColor(hue,sat,bright);
            colors.add(c);
            angle += increment;
        }
        return colors;
    }

    private void showSlide(RectProperties rectProperties){
        slideRect = lang.newRect(new Offset(0,0,rwsAnchor,"C"), new Offset(805, 600, rwsAnchor, "C"), "slideBG",
                null, rectProperties);
        slideHeader = lang.newText(new Offset(20, 20, rwsAnchor,"C"),"Vorbemerkung","dishead",null,
                titleTextProperties);
        slide1 = lang.newText(new Offset(20, 60, rwsAnchor,"C"),dis1,"dis1",null, titleTextProperties);
        slide2 = lang.newText(new Offset(20, 80, rwsAnchor,"C"),dis2,"dis2",null, titleTextProperties);
        slide3 = lang.newText(new Offset(20, 100, rwsAnchor,"C"),dis3,"dis3",null, titleTextProperties);
        slide4 = lang.newText(new Offset(20, 120, rwsAnchor,"C"),dis4,"dis4",null, titleTextProperties);
        slide5 = lang.newText(new Offset(20, 140, rwsAnchor,"C"),dis5,"dis5",null, titleTextProperties);
        slide6 = lang.newText(new Offset(20, 160, rwsAnchor,"C"),dis6,"dis6",null, titleTextProperties);
        slide7 = lang.newText(new Offset(20, 180, rwsAnchor,"C"),dis7,"dis7",null, titleTextProperties);
        slide8 = lang.newText(new Offset(20, 200, rwsAnchor,"C"),dis8,"dis8",null, titleTextProperties);
        slide9 = lang.newText(new Offset(20, 220, rwsAnchor,"C"),dis9,"dis9",null, titleTextProperties);
        slide10 = lang.newText(new Offset(20, 240, rwsAnchor,"C"),dis10,"dis10",null, titleTextProperties);
        slide11 = lang.newText(new Offset(20, 260, rwsAnchor,"C"),dis11,"dis11",null, titleTextProperties);
        slide12 = lang.newText(new Offset(20, 280, rwsAnchor,"C"),dis12,"dis12",null, titleTextProperties);
        slide13 = lang.newText(new Offset(20, 300, rwsAnchor,"C"),dis13,"dis13",null, titleTextProperties);
        slide14 = lang.newText(new Offset(20, 320, rwsAnchor,"C"),dis14,"dis14",null, titleTextProperties);
        slide15 = lang.newText(new Offset(20, 340, rwsAnchor,"C"),dis15,"dis15",null, titleTextProperties);
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
        pro1 = lang.newText(new Offset(20,37, rwsAnchor,"C"),pro1str,"pro1",null, individualTextProperties);
        pro2 = lang.newText(new Offset(20,57, rwsAnchor,"C"),pro2str,"pro2",null, individualTextProperties);
        pro3 = lang.newText(new Offset(20,77, rwsAnchor,"C"),pro3str,"pro3",null, individualTextProperties);
        pro4 = lang.newText(new Offset(20,97, rwsAnchor,"C"),pro4str,"pro4",null, individualTextProperties);
        con1 = lang.newText(new Offset(20,37, explanationAnchor,"C"),con1str,"con1",null, individualTextProperties);
        con2 = lang.newText(new Offset(20,57, explanationAnchor,"C"),con2str,"con2",null, individualTextProperties);
        con3 = lang.newText(new Offset(20,77, explanationAnchor,"C"),con3str,"con3",null, individualTextProperties);
        con4 = lang.newText(new Offset(20,97, explanationAnchor,"C"),con4str,"con4",null, individualTextProperties);
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
        makeBackgroundRectangle(rwsAnchor, 400, 300, "tournementBGRect", rectProperties);
        makeBackgroundRectangle(populationAnchor, 400, 300, "populationBGRect", rectProperties);
        makeBackgroundRectangle(explanationAnchor, 400, 300, "explanationBGRect", rectProperties);
        makeBackgroundRectangle(codeAnchor, 400, 300, "codeBGRect", rectProperties);
    }

    private void makeFinalWindows(RectProperties rectProperties){
        makeBackgroundRectangle(rwsAnchor, 400, 300, "ProPane", rectProperties);
        makeBackgroundRectangle(explanationAnchor, 400, 300, "ConPane", rectProperties);
        makeBackgroundRectangle(populationAnchor, 805, 300, "AddPane", rectProperties);
    }

    private void makeFinalWindowTitles(){
        proTitle = lang.newText(new Offset(20,6,rwsAnchor,"C"), "Vorteile", "ProTitle", null, windowTitleProperties);
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
        rwsTitle = lang.newText(new Offset(20, 6, rwsAnchor, "C"), "Roulette Wheel",
                "rwsTitle",null, windowTitleProperties);
        expTitle = lang.newText(new Offset(20, 6,explanationAnchor, "C"), "Erklärung", "expText", null,
                windowTitleProperties);
        srcTitle = lang.newText(new Offset(20, 6, codeAnchor, "C"), "Source Code", "srcText", null,
                windowTitleProperties);
        populationTitle.setFont(new Font("SansSerif", Font.BOLD, 14),null,null);
        rwsTitle.setFont(windowTitle,null,null);
        expTitle.setFont(windowTitle, null, null);
        srcTitle.setFont(windowTitle, null, null);
    }

    private void setupSourceCode(SourceCodeProperties sourceCodeProperties){
        code = lang.newSourceCode(new Offset(20,37,codeAnchor, "C"),"srcCode",null,sourceCodeProperties);
        code.addCodeLine("public Individual rwSelection(Individual[] population) {", "sig",0, null);
        code.addCodeLine("int sum = calculateFitnessSum();","line1", 1, null);
        code.addCodeLine("for (int i = 0; i < population.length; i++) {","line2", 1,null);
        code.addCodeLine("addWheelSegment(population[i]);","line2", 2, null);
        code.addCodeLine("}", "line2", 1,null);
        code.addCodeLine("int selector = randInt(360);", "line3",1,null);
        code.addCodeLine("return selectSegmentAtAngle(selector);", "line4",1,null);
        code.addCodeLine("}","line5",0,null);
    }

    private void setupStrings(){
        exp1str = "Als erstes berechnen wir die Summe aller Fitnesswerte.";
        exp2str = "Im zweiten Schritt erstellen wir ein Kuchendiagramm.";
        exp3str = "Auf diesem Diagramm erhält jedes Individuum einen";
        exp4str = "Teil, der proportional zu seiner Fitness ist.";
        exp5str = "Um nun ein Individuum zu selektieren, reicht es";
        exp6str = "aus, einen zufälligen Winkel zu generieren.";
        exp7str = "Durch den generierten Winkel können wir genau";
        exp8str = "ein Individuum auswählen, welches zurückgegeben wird.";
        exp9str = "Es wird folgendes Individuum selektiert:";

        dis1 = "Im Folgenden betrachten wir die Roulette Wheel Selektion. Es handelt sich dabei um ein";
        dis2 = "Verfahren des Genetic Programming. Spezifischer handelt es sich bei der Roulette Wheel";
        dis3 = "Selektion um ein Fitness-proportionales Selektionsverfahren. Das bedeutet, dass die";
        dis4 = "Wahrscheinlichkeit, dass ein spezifisches Individuum selektiert wird proportional";
        dis5 = "zu der Fitness des Individuums ist.";
        dis6 = "Ziel der Roulette Wheel Selektion ist es, probabilistisch ein Individuum aus einer";
        dis7 = "gegebenen Population auszuwählen. Im nachfolgenden Schritt wird dieses Individuum";
        dis8 = "mit anderen Individuen gekreuzt oder mutiert.";
        dis9 = "Wichtig ist, dass bei der Selektion natürliche Prozesse modelliert werden sollen.";
        dis10 = "Dementsprechend erfolgt die Selektion prinzipiell zufällig, wobei trotz allem";
        dis11 = "versucht wird, fittere Individuen wahrscheinlicher auszuwählen.";
        dis12 = "Das Verfahren sieht dabei vor, dass aus der gegebenen Population eine Art Roulette";
        dis13 = "Wheel erstellt wird, wobei jedes Individuum der Population einen Teil zugewiesen ";
        dis14 = "bekommt. Die Breite des Teils ist dabei proportional zum Anteil, den der ";
        dis15 = "Fitnesswert des Individuums an der aufsummierten Populationsfitness hat.";

        pro1str = "Die Selektion erfolgt proportional zur Fitness.";
        pro2str = "Auch schlechtere Individuuen werden selektiert,";
        pro3str = "wodurch die Diversität erhalten bleibt.";
        pro4str = "";

        con1str = "Extrem gute Individuen dominieren die Selektion.";
        con2str = "Diversitätserhaltung ist im Vergleich zu";
        con3str = "anderen Selektionsverfahren nur bedingt gut.";
        con4str = "";

        add1str = "Basierend auf der RWS gibt es 2 weitere Selektionsverfahren: Rank-based Selektion";
        add2str = "und Stochastic Universal Sampling. Beide wandeln RWS nur geringfügig ab, haben";
        add3str = "aber bestimmte Vor- und Nachteile. Dadurch ist RWS sehr vielseitg einsetzbar.";
        add4str = "RWS kann mittels stochastischer Verfahren optimiert werden und erreicht dann eine";
        add5str = "konstante Komplexität.";
    }

    private void setupProperties(TextProperties individualTextProperties){
        windowTitle = new Font("SansSerif", Font.BOLD, 14);
        pointProperties = new PointProperties("PP");
        titleTextProperties = new TextProperties("");
        titleTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor.brighter());
        individualTextProperties = new TextProperties("individual");
        windowTitleProperties = new TextProperties("");
        windowTitleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor.brighter().brighter());
        selectorProperties = new ArcProperties("Selector");
        selectorProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        selectorProperties.set(AnimationPropertiesKeys.COUNTERCLOCKWISE_PROPERTY, true);
        selectorProperties.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 0);
        selectorProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    }

    private void setupAnchorPoints(){
        anker = lang.newPoint(new Coordinates(0, 0), "Ankerpunkt", null, pointProperties);
        headerAnchor = lang.newPoint(new Offset(10, 10, anker, "C"), "HeaderAnchor", null, pointProperties);
        rwsAnchor = lang.newPoint(new Offset(0, 45, headerAnchor, "C"), "TournamentAnchor", null, pointProperties);
        populationAnchor = lang.newPoint(new Offset(0, 350, headerAnchor, "C"),"PopAnchor", null, pointProperties);
        explanationAnchor = lang.newPoint(new Offset(405, 45, headerAnchor, "C"), "ExpAnchor", null, pointProperties);
        codeAnchor = lang.newPoint(new Offset(405, 350, headerAnchor, "C"), "CodeAnchor", null, pointProperties);
        anker.hide();
        headerAnchor.hide();
        rwsAnchor.hide();
        populationAnchor.hide();
        explanationAnchor.hide();
        codeAnchor.hide();
    }


    public static void main(String[] args) {
        RouletteWheelSelection ts = new RouletteWheelSelection();
        ts.init();
        String s = ts.generate(null, null);
        //System.out.println(s);
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    class Individual {
        private Language language;
        private DisplayOptions displayOptions;
        private EllipseProperties ellipseProperties;
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
            Offset offset = new Offset(-fontsize / 2, -fontsize / 2, ellipse, "C");
            this.text = language.newText(offset, "" + fitness, id + "text", displayOptions, textProperties);
        }

        public void correctAnimError(){
            this.text.hide();
            Font font = (Font) textProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
            int fontsize = font.getSize();
            Offset offset = new Offset(-fontsize / 2, -fontsize / 2, ellipse, "C");
            this.text = language.newText(offset, "" + fitness, id + "text", displayOptions, textProperties);
        }

        public int getFitness() {
            return fitness;
        }
    }

}