package generators.maths;
/*
 * GAGenerator.java
 * Markus Semmler, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class GAGenerator implements Generator {
    private Language lang;


    private int max_steps, max_val;
    private double f_theta, f_a, f_b, f_c, f_d, f_e;

    private SourceCodeProperties sourceCodeProps;
    private MatrixProperties matrixProps;

    private SourceCode txtInfo;
    private TextProperties textProps;
    private SourceCode src;
    private Polyline sepline;

    private StringMatrix elements_header;
    private IntMatrix elements_x;
    private DoubleMatrix element_f;
    private Text elements_txt;

    private StringMatrix newelements_header;
    private IntMatrix newelements_x;
    private Text newelements_txt;
    private DoubleMatrix newelement_f;

    private StringMatrix binnewelements_header;
    private StringMatrix binnewelements_bin;
    private Text binnewelementstxt;

    private StringMatrix binelements_header;
    private StringMatrix binelements_bin;
    private Text binelementstxt;

    private Polyline xaxis;
    private Polyline yaxis;
    private Polyline xybound;
    private Text plottxt;
    private Circle[] circles = new Circle[6];
    private Point sol;

    public void init(){
        lang = new AnimalScript("Genetic Algorithm", "Markus Semmler", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        setPrimitives(primitives);
        setProperties(props);
        lang.nextStep("Algo");
        Text header = lang.newText(new Coordinates(20,100), "Genetic Algorithm" +
                        "on", "header",null, textProps);
        showSeperationLine();
        displayCode();
        executeAlgorithm();
        sepline.hide();
        src.hide();
        header.hide();
        Text conclusion = lang.newText(new Coordinates(20,100), "Conclusi" +
                        "on", "header",null, textProps);
        SourceCode conclusionSrc = lang.newSourceCode(new Coordinates(20,175), "conclusionSrc",
                null, sourceCodeProps);
        conclusionSrc.addCodeLine("The solution of the genetic algorithm is (" + sol.x + ", " + sol.y + "). The presented algorithm is a gradient-free one.",null,0,null);
        conclusionSrc.addCodeLine("The explored search space is kind of restricted due to the mutation, in a real example it can be a good idea to come up with a random mutation.",null,0,null);
        conclusionSrc.addCodeLine("All found inbetween solutions jump in the element space, as they are combined discretly. It is a good and fast basic optimization algorithm.",null,0,null);
        lang.finalizeGeneration();
        return lang.toString();
    }

    private void showSeperationLine() {
        Coordinates a1 = new Coordinates(580, 75);
        Coordinates b1 = new Coordinates(580, 1200);
        Node[] nodes = new Node[2];
        nodes[0] = a1;
        nodes[1] = b1;
        sepline = lang.newPolyline(nodes, "separationLine",null);
    }

    private void setProperties(AnimationPropertiesContainer props) {
        matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceProps");
        textProps = (TextProperties) props.getPropertiesByName("textProps");
    }

    private void setPrimitives(Hashtable<String, Object> primitives) {
        f_a = (double)primitives.get("a");
        f_b = (double)primitives.get("b");
        f_c = (double)primitives.get("c");
        f_d = (double)primitives.get("d");
        f_e = (double)primitives.get("e");
        f_theta = (double)primitives.get("theta");
        max_steps = (Integer)primitives.get("max_steps");
        max_val = (Integer)primitives.get("max_value");

        if (f_a < 0 || f_b < 0 || f_c < 0 || f_d < 0 || f_e < 0) {
            throw new IllegalArgumentException("a, b, c, d, e has to be positive.");
        }

        if (max_val != 50 || max_steps < 1) {
            throw new IllegalArgumentException("max_value=50 and max_steps>0.");
        }
    }

    public double obj(Point p) {
        return f_a * p.getX() + f_b * p.getY()
                - f_theta * (f_c * p.getX() * p.getX()
                + f_d * p.getY() * p.getY() + f_e * (p.getY() + p.getX()) * (p.getY() + p.getX()));
    }

    public boolean constraint(Point p) {
        return p.getX() + p.getY() <= max_val;
    }

    public String getName() {
        return "Genetic Algorithm";
    }

    public String getAlgorithmName() {
        return "Genetic Algorithm";
    }

    public String getAnimationAuthor() {
        return "Markus Semmler";
    }

    public String getDescription(){
        return "In this problem we want to solve the optimization problem "
                +"\n"
                +"x* = max{f(x) | s.t. g(x) <= 0,  x >= 0}. The function f is called "
                +"\n"
                +"the objective. There are two basic constraints, e.g. x >= 0 and"
                +"\n"
                +"g(x) <= 0. Obviously one can encode the first constraint in the "
                +"\n"
                +"second. We advise a more clever approach for this problem, as "
                +"\n"
                +"we encode the solution space of the instance of the problem as"
                +"\n"
                +"a binary string. "
                +"\n"
                +"\n"
                +"We view the problem of maximizing the objective "
                +"\n"
                +"f(x,y)=a*x+b*y-theta*(c*x^2+d*x^2+e*(x+y)^2) and the "
                +"\n"
                +"constraint g(x,y)=x+y-maxv. We set maxv=50, as this affects "
                +"\n"
                +"the size of the binary array and thus has direct influence on the "
                +"\n"
                +"correctness of the displayed elements."
                +"\n"
                +"\n"
                +"The size of the array is two times 6 bits, as ceil(log(50))=6. So "
                +"\n"
                +"we encode each possible solution as 12 bits, where the first "
                +"\n"
                +"half corresponds to x and the second half corresponds to y."
                +"\n"
                +"\n"
                +"In each iteration the 3 most fittest solutions are selected. All"
                +"\n"
                +"combinations of two pairs are recombined, e.g. they get splitted"
                +"\n"
                +"into a left, middle and right piece (cutting at position 2 and 9) "
                +"\n"
                +"and the middle pieces are exchanged. Based on the fitness of"
                +"\n"
                +"the parents, a random bit is exchanged, e.g. the bit (i+5*j) % 12"
                +"\n"
                +"is flipped. Afterwards it continues with the selection phase.";
    }

    public String getCodeExample(){
        return "ga(f,g):"
 +"\n"
 +"    initialize the set S=(s1, s2, s3)"
 +"\n"
 +"    while steps < maxs:"
 +"\n"
 +"        fitness[i] = f(S[i])"
 +"\n"
 +"        pi = select(fitness)"
 +"\n"
 +"        S  = recombinate(S[pi])"
 +"\n"
 +"        S  = mutate(pi, S)"
 +"\n"
 +"        S  = rnd_fillup(S)";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public void displayCode() {
        src = lang.newSourceCode(new Coordinates(20, 200), "sourceCode",
                null, sourceCodeProps);
        src.addCodeLine("ga(", null, 0, null);//0
        src.addCodeLine(String.format("f(x,y)=%.2f*x+%.2f*y-%.4f*(%.2f*x^2+%.2f*y^2+%.2f*(x+x)^2)", f_a, f_b, f_theta, f_c, f_d, f_e), null, 2, null);//1
        src.addCodeLine("g(x,y)=x+y-" + max_val, null, 2, null);//2
        src.addCodeLine("):", null, 0, null);//3
        src.addCodeLine("initialize optimization set S = (S[1], S[2], S[3]))", null, 1, null); // 4
        src.addCodeLine("while steps < " + max_steps, null, 1, null); // 5
        src.addCodeLine("fitness[i]=f(S[i])", null, 2, null); // 6
        src.addCodeLine("pi=select(fitness)", null, 2, null); // 7
        src.addCodeLine("S=recombination(S[pi])", null, 2, null); // 8
        src.addCodeLine("S=mutate(S[pi], pi)", null, 2, null); // 9
        src.addCodeLine("S=rnd_fillup(S)", null, 2, null); // 9

    }

    private void displayNewBinaryElements(List<String> elements, int num_bits, Coordinates base) {
        int num_elements = elements.size();
        binnewelementstxt = lang.newText(new Coordinates(base.getX(), base.getY() - 30), "3. New Binary", "header", null);
        binnewelements_bin = lang.newStringMatrix(new Coordinates(base.getX(), base.getY() + 30), new String[num_elements][num_bits],"set_bin_x",null, matrixProps);
        binnewelements_header = lang.newStringMatrix(base, new String[][] { new String[] {"bin_x"}},"str_set",null, matrixProps);

        for (int i = 0; i < num_elements; i++) {

            Color ocolor = null;
            switch (i) {
                case 0:
                    ocolor = Color.RED;
                    break;
                case 1:
                    ocolor = Color.RED;
                    break;
                case 2:
                    ocolor = Color.GREEN;
                    break;
                case 3:
                    ocolor = Color.GREEN;
                    break;
                case 4:
                    ocolor = Color.YELLOW;
                    break;
                case 5:
                    ocolor = Color.YELLOW;
                    break;
            }

            Color mcolor = null;
            switch (i) {
                case 0:
                    mcolor = Color.CYAN;
                    break;
                case 1:
                    mcolor = Color.MAGENTA;
                    break;
                case 2:
                    mcolor = Color.ORANGE;
                    break;
                case 3:
                    mcolor = Color.MAGENTA;
                    break;
                case 4:
                    mcolor = Color.ORANGE;
                    break;
                case 5:
                    mcolor = Color.CYAN;
                    break;
            }

            for (int j = 0; j < 2; j++) {
                binnewelements_bin.setGridHighlightFillColor(i, j, ocolor, null, null);
                binnewelements_bin.highlightCell(i, j, null, null);
            }
            for (int j = 9; j < 12; j++) {
                binnewelements_bin.setGridHighlightFillColor(i, j, ocolor, null, null);
                binnewelements_bin.highlightCell(i, j, null, null);
            }
            for (int j = 2; j < 9; j++) {
                binnewelements_bin.setGridHighlightFillColor(i, j, mcolor, null, null);
                binnewelements_bin.highlightCell(i, j, null, null);
            }
            for (int j = 0; j < num_bits; j++) {
                binnewelements_bin.put(i, j, String.valueOf(elements.get(i).charAt(j)), null, null);
            }
        }
    }

    private void hideNewBinaryElements() {
        binnewelements_bin.hide();
        binnewelements_header.hide();
        binnewelementstxt.hide();
        binnewelements_bin = null;
        binnewelements_header = null;
        binnewelementstxt = null;
    }

    private void displayBinaryElements(List<String> elements, int num_bits, Coordinates base) {
        int num_elements = elements.size();
        binelementstxt = lang.newText(new Coordinates(base.getX(), base.getY() - 30), "2. Selected Binary", "header", null);
        binelements_bin = lang.newStringMatrix(new Coordinates(base.getX(), base.getY() + 30), new String[num_elements][num_bits],"set_bin_x",null, matrixProps);
        binelements_header = lang.newStringMatrix(base, new String[][] { new String[] {"bin_x"}},"str_set",null, matrixProps);

        for (int i = 0; i < num_elements; i++) {

            Color mcolor = null;
            switch (i) {
                case 0:
                    mcolor = Color.ORANGE;
                    break;
                case 1:
                    mcolor = Color.CYAN;
                    break;
                case 2:
                    mcolor = Color.MAGENTA;
                    break;
            }

            Color ocolor = null;
            switch (i) {
                case 0:
                    ocolor = Color.RED;
                    break;
                case 1:
                    ocolor = Color.GREEN;
                    break;
                case 2:
                    ocolor = Color.YELLOW;
                    break;
            }

            for (int j = 0; j < 2; j++) {
                binelements_bin.setGridHighlightFillColor(i, j, ocolor, null, null);
                binelements_bin.highlightCell(i, j, null, null);
            }
            for (int j = 9; j < 12; j++) {
                binelements_bin.setGridHighlightFillColor(i, j, ocolor, null, null);
                binelements_bin.highlightCell(i, j, null, null);
            }
            for (int j = 2; j < 9; j++) {
                binelements_bin.setGridHighlightFillColor(i, j, mcolor, null, null);
                binelements_bin.highlightCell(i, j, null, null);
            }
            for (int j = 0; j < num_bits; j++) {
                binelements_bin.put(i, j, String.valueOf(elements.get(i).charAt(j)), null, null);
            }
        }
    }

    private void hideBinaryElements() {
        binelements_bin.hide();
        binelements_header.hide();
        binelementstxt.hide();
        binelements_bin = null;
        binelements_header = null;
        binelementstxt = null;
    }

    private void displayElements(List<Point> elements, List<Double> pfitness, Coordinates base, List<Integer> indices) {
        elements_txt = lang.newText(new Coordinates(base.getX(), base.getY() - 30), "1. Set", "header", null);
        elements_header = lang.newStringMatrix(base, new String[][] { new String[] {"x1", "x2", "f"}},"str_set",null, matrixProps);
        int num_elements = elements.size();
        int[][] mat = new int[num_elements][2];
        for (int i = 0; i < num_elements; i++) {
            mat[i][0] = elements.get(i).x;
            mat[i][1] = elements.get(i).y;;
        }
        elements_x = lang.newIntMatrix(new Coordinates(base.getX(), base.getY() + 30), mat, "set", null, matrixProps);
        element_f = lang.newDoubleMatrix(new Coordinates(base.getX() + 50, base.getY() + 30), new double[num_elements][1], "set_fitness", null, matrixProps);

        for (int i = 0; i < num_elements; i++) {
            element_f.put(i, 0, pfitness.get(i), null, null);
            if (indices != null && indices.contains(i))
                element_f.highlightCell(i, 0, null, null);
        }

    }

    private void hideElements() {
        elements_x.hide();
        element_f.hide();
        elements_header.hide();
        elements_txt.hide();
        elements_txt = null;
        elements_x = null;
        element_f = null;
        elements_header = null;
    }

    private void displayText(String text, int length)
    {
        StringBuilder sb = new StringBuilder(text);

        int i = 0;
        while (i + length < sb.length() && (i = sb.lastIndexOf(" ", i + length)) != -1) {
            sb.replace(i, i + 1, "\n");
        }


        txtInfo = lang.newSourceCode(new Coordinates(1200, 320), "sourceCode",
                null, sourceCodeProps);
        String[] res = sb.toString().split("\n");
        for (int k = 0; k < res.length; k++) {
            txtInfo.addCodeLine(res[k], null, 0, null);//0
        }
    }

    private void hideText()
    {
        txtInfo.hide();
    }

    private void displayPlot(List<Point> points, Coordinates base, int width, int height) {
        int max_x = max_val;
        int max_y = max_val;
        int min_x = 0;
        int min_y = 0;
        int middle_x = (int) (base.getX() + height / 2.0);
        int middle_y = (int) (base.getY() + width / 2.0);
        plottxt = lang.newText(new Coordinates(base.getX(), base.getY() - 30), "Current Points", "header", null);
        xaxis = lang.newPolyline(new Coordinates[] { new Coordinates(base.getX(), base.getY()), new Coordinates(base.getX(), base.getY() + height) }, "lin", null);
        yaxis = lang.newPolyline(new Coordinates[] { new Coordinates(base.getX(), base.getY() + height), new Coordinates(base.getX() + width, base.getY() + height) }, "lin", null);
        xybound = lang.newPolyline(new Coordinates[] { new Coordinates(max_val, 0), new Coordinates(0, max_val) }, "lin", null);

        for (int i = 0; i < points.size(); i++) {
            double tr_x = (points.get(i).x - (double) min_x) / (max_x - min_x);
            double tr_y = (points.get(i).y - (double) min_y) / (max_y - min_y);

            int tr_i_x = (int) (width * tr_x + base.getX());
            int tr_i_y = (int) (base.getY() + height - height * tr_y);
            circles[i] = lang.newCircle(new Coordinates(tr_i_x, tr_i_y), 5, "cir", null);
        }
    }

    private void hidePlot() {
        xaxis.hide();
        yaxis.hide();
        plottxt.hide();
        xybound.hide();
        xybound = null;
        plottxt = null;
        xaxis = null;
        yaxis = null;

        for (int i = 0; i < 6; i++) {
            if (circles[i] != null) circles[i].hide();
        }
    }

    public void displayQuestion() {
        MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("MC1");
        question.setPrompt("What is correct about the found optimium?");
        question.addAnswer("In every case the correct optimum can be found.",0, "Wrong. As the parts are fixed and the bits are the same for the first, not all solutions get explored.");
        question.addAnswer("A good criterion are 3 steps of optimization.", 0, "Wrong. The termination criterion should be choosen when no improvement is made.");
        question.addAnswer("The presented algorithm doesn't use gradient information and is thus a first order optimization procedure. ", 1, "Correct. The objective has not to be differentiable. ");
        lang.addMCQuestion(question);
    }

    public void displayMidQuestion() {
        MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("MC2");
        question.setPrompt("Why do we need to fill up with random data points.?");
        question.addAnswer("To avoid local optima.", 0, "Wrong. This would hinder convergence, when a change in the objective is taken as the criterion.");
        question.addAnswer("It might happen that x + y > " + max_val, 1, "Correct. As the binary representation only allows positive elements, we only need to ensure this constraint..");
        question.addAnswer("It might happen that x < 0 or that y < 0", 0, "Wrong. Due to the choose of the binary representation only positive elements are generated.");
        lang.addMCQuestion(question);
    }

    public void displayAddQuestion() {
        MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("MC3");
        question.setPrompt("How to choose the stoping criterion in the real world?");
        question.addAnswer("Use a fixed number of steps as in the example.",0, "Wrong. You can't tell how fast the algorithm converges in advance, so choosing a fixed number doesn't work in the general case.");
        question.addAnswer("Detect the change in the objective and if there is no increase stop.", 1, "Correct. This criterion ensures convergence, because the pool of combination strings stays fixed, although it might happen that random data would cause a premature convergence..");
        question.addAnswer("Detect the range of objective values and if it goes under a certain threshold stop.", 0, "Wrong. This can happen way before finding the optimum even if no random data is added.");
        lang.addMCQuestion(question);
    }

    private void displayNewElements(List<Point> elements, List<Double> pfitness, Coordinates base, List<Integer> indices) {

        newelements_txt = lang.newText(new Coordinates(base.getX(), base.getY() - 30), "4. All Candidates", "header", null);
        newelements_header = lang.newStringMatrix(base, new String[][] { new String[] {"x1", "x2", "f"}},"str_set",null, matrixProps);
        int num_elements = elements.size();
        int[][] mat = new int[num_elements][2];
        for (int i = 0; i < num_elements; i++) {
            mat[i][0] = elements.get(i).x;
            mat[i][1] = elements.get(i).y;;
        }
        newelements_x = lang.newIntMatrix(new Coordinates(base.getX(), base.getY() + 30), mat, "set", null, matrixProps);
        newelement_f = lang.newDoubleMatrix(new Coordinates(base.getX() + 50, base.getY() + 30), new double[num_elements][1], "set_fitness", null, matrixProps);

        for (int i = 0; i < num_elements; i++) {
            newelement_f.put(i, 0, pfitness.get(i), null, null);
            if (indices.contains(i))
                newelement_f.highlightCell(i, 0, null, null);
        }

    }

    private void hideNewElements() {
        newelements_x.hide();
        newelement_f.hide();
        newelements_header.hide();
        newelements_txt.hide();
        newelements_txt = null;
        newelements_x = null;
        newelement_f = null;
        newelements_header = null;
    }


    public void executeAlgorithm() {

        // ------------------------------
        // INITIALIZATION
        // ------------------------------

        lang.nextStep();
        src.highlight(1);

        // fitness function
        // 20 * x + 12 * y - 0.0005 * (2 * x^2 + y^2 + (x + y)^2)
        Function<Point, Double> fn = this::obj;

        lang.nextStep();
        src.unhighlight(1);
        src.highlight(2);
        Function<Point, Boolean> g = this::constraint;

        // conversion function
        Function<Integer, String> intToBinary = x ->
                String.format("%6s", Integer.toBinaryString(x))
                        .replace(' ', '0');

        Function<Point, String> pointToBinary = p -> intToBinary.apply(p.x).concat(intToBinary.apply(p.y));
        Function<String, Integer> binaryToInteger = s -> Integer.parseInt(s.substring(0, 6), 2);
        Function<String, Point> binaryToPoint =
                s -> new Point(
                        binaryToInteger.apply(s.substring(0, 6)),
                        binaryToInteger.apply(s.substring(6))
                );

        lang.nextStep();
        src.unhighlight(2);
        src.highlight(4);
        Random rand = new Random();

        java.util.List<Point> elements = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int mv1 = rand.nextInt(max_val);
            int mv2 = rand.nextInt(max_val - mv1);

            elements.add(new Point(mv1, mv2));
        }

        int num_bits = (int) Math.ceil(Math.log(max_val) / Math.log(2));
        num_bits = 2 * num_bits;

        lang.nextStep();
        src.unhighlight(4);
        src.highlight(5);
        int last_highlighted = 5;
        java.util.List<Double> fitness = elements.stream().map(fn).collect(Collectors.toList());


        displayAddQuestion();
        for (int step = 0; step < max_steps; step++) {

            src.unhighlight(last_highlighted);
            src.highlight(6);

            // ------------------------------
            // SELECTION
            // ------------------------------
            java.util.List<Integer> indices = IntStream.range(0, fitness.size())
                    .boxed().sorted(Comparator.comparing(fitness::get))
                    .mapToInt(ele -> ele).boxed().collect(Collectors.toList());

            // plan
            Collections.reverse(indices);
            indices = indices.subList(0, Math.min(3, indices.size()));

            // ------------------------------
            // EVALUATION
            // ------------------------------
            displayElements(elements, fitness, new Coordinates(650, 100), indices);
            displayPlot(elements, new Coordinates(1200, 100), 400, 150);
            displayText("These are all elements of the current iteration set. In the selection step the 3 best elements are filtered. In the table they are displayed in yellow.", 80);
            lang.nextStep();

            // ------------------------------
            src.unhighlight(6);
            src.highlight(7);


            // sort both arrays
            java.util.List<Point> selements = indices.stream()
                    .map(elements::get)
                    .collect(Collectors.toList());

            java.util.List<Double> sfitnesses = indices.stream()
                    .map(fitness::get)
                    .collect(Collectors.toList());

            hideElements();
            hidePlot();
            displayElements(selements, sfitnesses, new Coordinates(650, 100), null);
            displayPlot(elements, new Coordinates(1200, 100), 400, 150);
            hideText();
            displayText("Only the best 3 elements are considered. As a next step they get converted into a binary representation to make mutation and recombination possible.", 80);
            lang.nextStep();

            src.unhighlight(7);
            src.highlight(8);


            // ------------------------------

            // map all elements to string
            java.util.List<String> mappedselements = selements.stream().map(pointToBinary).collect(Collectors.toList());
            displayBinaryElements(mappedselements, num_bits, new Coordinates(850, 100));
            hideText();
            displayText("The individuals are displayed in binary representation. All over 12 bits are used. They are grouped in 3 blockes each with different colors. The middle parts" +
                    " gets exchanged in the next step.", 80);
            lang.nextStep();

            // ------------------------------
            // RECOMBINATION
            // ------------------------------
            int max_iter = mappedselements.size();
            java.util.List<String> recombined = new ArrayList<>();
            java.util.List<Integer> mut_indicices = new ArrayList<>();


            for (int i = 0; i < max_iter; i++) {
                for (int j = 0; j < max_iter; j++) {
                    if (i == j) continue;

                    // get strings
                    String el1 = mappedselements.get(i);
                    String el2 = mappedselements.get(j);

                    // split up strings
                    String sleft = el1.substring(0, 2);
                    String smiddle = el2.substring(2, 9);
                    String sright = el1.substring(9);

                    // concat strings and add
                    String res = sleft.concat(smiddle.concat(sright));

                    // mutate index
                    int mut_i = (i + 5 * j) % 12;
                    mut_indicices.add(mut_i);
                    recombined.add(res);
                }
            }

            displayNewBinaryElements(recombined, num_bits, new Coordinates(850, 300));
            hideText();
            displayText("Recombine the elements, the same color means, the part is taken from the corresponding parent. Note how each element is combined with the others such that two offsprings are producted. As a next step a bit based on the index is flipped.", 80);
            lang.nextStep();

            hideNewBinaryElements();

            src.unhighlight(8);
            src.highlight(9);
            last_highlighted = 9;

            // ------------------------------
            // MUTATION
            // ------------------------------
            for (int i = 0; i < recombined.size(); i++) {

                StringBuilder bld_res = new StringBuilder(recombined.get(i));
                bld_res.setCharAt(mut_indicices.get(i), recombined.get(i).charAt(mut_indicices.get(i)) == '0' ? '1' : '0');

                // add to list
                recombined.set(i, bld_res.toString());

            }
            // ------------------------------

            // convert to points
            elements = recombined.stream().map(binaryToPoint).collect(Collectors.toList());
            fitness = elements.stream().map(fn).collect(Collectors.toList());
            List<Integer> nindices = new ArrayList<>();
            for (int p = 0; p < elements.size(); p++) {
                if (elements.get(p).x < 0 || elements.get(p).y < 0 || !g.apply(elements.get(p))) {
                    nindices.add(p);
                }
            }

            displayNewElements(elements, fitness, new Coordinates(650, 300), nindices);
            displayNewBinaryElements(recombined, num_bits, new Coordinates(850, 300));
            hideText();
            displayText("These are the mutated values, as binary and as the pointwise display style. All invalid examples are highlighted and get removed accordingely.", 80);

            lang.nextStep();

            hideNewElements();
            hideNewBinaryElements();
            hideElements();
            hideBinaryElements();
            hidePlot();
            hideText();

            // sort both arrays
            elements = IntStream.range(0, elements.size())
                    .filter(x -> !nindices.contains(x))
                    .boxed().map(elements::get)
                    .collect(Collectors.toList());

            src.unhighlight(9);
            src.highlight(10);
            last_highlighted = 10;
            displayText("Fill up elements if less than 3 elements.", 80);

            for (int i = 0; i < 3 - elements.size(); i++) {
                int mv1 = rand.nextInt(max_val);
                int mv2 = rand.nextInt(max_val - mv1);
                elements.add(new Point(mv1, mv2));
            }

            fitness = elements.stream().map(fn).collect(Collectors.toList());

            if (step == 0) displayMidQuestion();
            lang.nextStep();
            hideText();
        }

        java.util.List<Integer> indices = IntStream.range(0, fitness.size())
                .boxed().sorted(Comparator.comparing(fitness::get))
                .mapToInt(ele -> ele).boxed().collect(Collectors.toList());

        // ascending
        Collections.reverse(indices);
        sol = elements.get(indices.get(0));
        displayQuestion();
    }
}