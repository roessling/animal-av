/*
 * SSSStar.java
 * Hanna Holtdirk, Robin Satter, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching.sss;

import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.Rect;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Graph;
import algoanim.primitives.StringMatrix;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

import interactionsupport.models.InteractionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import translator.Translator;

import java.util.Locale;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.lang.StringBuilder;
import java.util.StringTokenizer;
import java.awt.Color;
import java.awt.Font;

import static generators.searching.sss.Node.Type;


public class SSSStar implements ValidatingGenerator {
    private enum Status {
        LIVE, SOLVED
    }

    /**
     * Class for elements in the OPEN priority queue.
     */
    private class Descriptor {
        final Node node;
        final Status status;
        final int merit;

        public Descriptor(Node J, Status s, int h) {
            this.node = J;
            this.status = s;
            this.merit = h;
        }

        @Override
        public String toString() {
            String merit = this.merit == Integer.MAX_VALUE ? "inf" : "" + this.merit;
            return "(" + this.node.toString() + ", " + this.status + ", " + merit + ")";
        }
    }

    private Language lang;
    private Locale loc;
    private Translator trans;

    // translations
    private final String title;
    private final String algoName;

    // primitives
    private Graph graph;
    private SourceCode sourceCode;
    private StringMatrix openTable;
    private Polyline pArrow;
    private double probQuestion;
    private boolean askQuestions;

    // properties
    private RectProperties rectProps;
    private TextProperties textProps;
    private TextProperties meritProps;
    private SourceCodeProperties codeProps;
    private GraphProperties graphProps;
    private MatrixProperties openProps;

    private Node root;
    private int treeDepth;
    private int numNodes;
    private Coordinates[] coordinates;
    private ArrayList<Descriptor> openQueue;


    public SSSStar(String path, Locale l){
        loc = l;
        trans = new Translator(path, loc);
        title = trans.translateMessage("title");
        algoName = trans.translateMessage("algoName");
    }

    @Override
    public void init(){
        lang = new AnimalScript(title, "Hanna Holtdirk, Robin Satter", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    @Override
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        // get primitives and properties from settings
        String treeString = (String) primitives.get("tree");
        probQuestion = (Double) primitives.get("probQuestion");
        askQuestions = (Boolean) primitives.get("askQuestions");
        textProps = (TextProperties) props.getPropertiesByName("text");
        meritProps = (TextProperties) props.getPropertiesByName("meritColor");
        rectProps = (RectProperties) props.getPropertiesByName("headerBox");
        codeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
        graphProps = (GraphProperties) props.getPropertiesByName("graph");
        openProps = (MatrixProperties) props.getPropertiesByName("openList");

        // initialize variables
        root = parseTree(treeString);
        treeDepth = Node.getDepth(root);
        openQueue = new ArrayList<Descriptor>();

        // display all elements initially
        this.intro();

        // create question groups
        QuestionGroupModel group = new QuestionGroupModel("nextGroup", 6);
        lang.addQuestionGroup(group);

        // run SSS*
        int result = this.sss();

        // end
        this.end(result);
        
        return lang.toString();
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives){
        String treeString = (String) primitives.get("tree");
        root = parseTree(treeString);
        return root != null;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public String getAlgorithmName() {
        return algoName;
    }

    @Override
    public String getAnimationAuthor() {
        return "Hanna Holtdirk, Robin Satter";
    }

    @Override
    public String getDescription(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < 3; i++)
            sb.append(trans.translateMessage("descrU" + i) + "\n");
        return sb.toString();
    }

    @Override
    public String getCodeExample(){
        return "OPEN := { (\u03b5, LIVE, inf) } \n"
                +  "while (true) \n"
                +  "    pop p := (J, s, h) from OPEN \n"
                +  "    if J == \u03b5 and s == SOLVED \n"
                +  "        STOP and return h \n"
                +  "    else \n"
                +  "        if s == LIVE \n"
                +  "            if J is a terminal node \n"
                +  "                add (J, SOLVED, min(h,value(J))) to OPEN \n"
                +  "            else if J is a MIN node \n"
                +  "                add (J.1, LIVE, h) to OPEN \n"
                +  "            else \n"
                +  "                for j=1..number_of_children(J) \n"
                +  "                    add (J.j, LIVE, h) to OPEN \n"
                +  "        else \n"
                +  "            if J is a MIN node \n"
                +  "                add (parent(J), SOLVED, h) to OPEN \n"
                +  "                purge parent(J) \n"
                +  "            else if is_last_child(J) \n"
                +  "                add (parent(J), SOLVED, h) to OPEN \n"
                +  "            else \n"
                +  "                add (rightSibling(J), LIVE, h) to OPEN \n";
    }

    @Override
    public String getFileExtension(){
        return "asu";
    }

    @Override
    public Locale getContentLocale() {
        return loc;
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    @Override
    public String getOutputLanguage() {
        return ValidatingGenerator.PSEUDO_CODE_OUTPUT;
    }

    /**
     * Parses the tree from the given String primitive entered by the user.
     * @param s tree syntax as a string.
     * @return the root of the built tree or null if syntax of s or structure of the tree was not correct
     */
    private Node parseTree(String s) {
        String treeString = s.replaceAll("\\s+",""); // remove spaces
        StringTokenizer st = new StringTokenizer(treeString, "{},()", true);
        if (!st.hasMoreTokens()) {
            System.err.println("Error in parsing tree: Empty string.");
            return null;
        }
        String name = st.nextToken();
        if(!name.matches("[a-zA-Z0-9]+")) {
            System.err.println("Error in parsing tree: Node names should only contain letters and numbers.");
            return null;
        }
        Node root = new Node(name, Type.MAX_NODE, null);
        if (!st.hasMoreTokens() || !st.nextToken().equals("{")) {
            System.err.println("Error in parsing tree: Root node must have children.");
            return null;
        }
        boolean correct = recBuildTree(root, st, new HashSet<String>());
        if(st.hasMoreTokens()){
            System.err.println("Error in parsing tree: Tokens are left behind last closing bracket '}'.");
            return null;
        }
        return correct ? root : null;
    }

    private boolean recBuildTree(Node parent, StringTokenizer st, Set<String> set) {
        if (!st.hasMoreTokens()) {
            System.err.println("Error in parsing tree: Empty brackets.");
            return false;
        }
        String name = st.nextToken();
        Node n = null;
        while(!name.equals("}")) {
            if (!st.hasMoreTokens()) {
                System.err.println("Error in parsing tree: Opening bracket, '{' or '(', is missing.");
                return false;
            }
            if(!name.matches("[a-zA-Z0-9]+")) {
                System.err.println("Error in parsing tree: Node names should only contain letters and numbers.");
                return false;
            }
            if(!set.add(name)) {
                System.err.println("Error in parsing tree: Duplicate node name.");
                return false;
            }
            String bracket = st.nextToken();
            if(bracket.equals("(")) {
                // terminal node
                int value;
                try {
                   value = Integer.parseInt(st.nextToken());
                } catch(NumberFormatException e) {
                    System.err.println("Error in parsing tree: Leaf nodes must have an integer value and be enclosed by brackets ().");
                    return false;
                }
                if(value < 0) {
                    System.err.println("Error in parsing tree: Leaf values must be positive.");
                    return false;
                }
                if(parent.getType() != Type.MIN_NODE) {
                    System.err.println("Error in parsing tree: Leaf nodes must be a child of a min node.");
                }
                n = new Node(name, value, parent);
                if(parent != null)
                    parent.addChild(n);
                if(!st.nextToken().equals(")")) {
                    System.err.println("Error in parsing tree: Closing bracket ')' is missing.");
                    return false;
                }
            } else if(bracket.equals("{")) {
                // min or max node
                Type t;
                // root node is max node, then switch between min and max
                if(parent == null || parent.getType() == Type.MIN_NODE)
                    t = Type.MAX_NODE;
                else
                    t = Type.MIN_NODE;
                n = new Node(name, t, parent);
                if(parent != null)
                    parent.addChild(n);
                // recursive call to build children
                if(!recBuildTree(n, st, set)) return false;
            } else {
                System.err.println("Error in parsing tree: Node has to have children in {} or a value in ().");
                return false;
            }
            if (!st.hasMoreTokens()) {
                System.err.println("Error in parsing tree: Closing bracket '}' is missing.");
                return false;
            }
            // get next sibling (or closing bracket)
            name = st.nextToken();
        }
        return true;
    }

    /**
     * Initially displays the components of the animation (code, list, tree)
     * and shows a description of the algorithm and the code.
     */
    private void intro() {
        Font font = (Font) textProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
        Color color = (Color) textProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);

        // header
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font.deriveFont(Font.BOLD, 18));
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        lang.newText(new Coordinates(30, 30), this.algoName, "header", null, headerProps);
        lang.newRect(new Offset(-15, -15, "header", "NW"), new Offset(15, 15, "header", "SE"),
                "headerBox", null, rectProps);
        lang.nextStep(trans.translateMessage("intro"));

        // description
        List<String> descrList = new ArrayList<String>();
        for(int i = 0; i < 8; i++)
            descrList.add(trans.translateMessage("descr" + i));
        InfoBox descriptionBox = new InfoBox(lang, new Coordinates(10, 100), descrList.size(), trans.translateMessage("descrTitle"));
        descriptionBox.setText(descrList);

        lang.nextStep();
        descriptionBox.hide();

        // source code
        this.displayCode();
        lang.nextStep();

        // graph
        numNodes = Node.numNodes(root);
        int[][] adjacencyMatrix = new int[numNodes][numNodes];
        coordinates = new Coordinates[numNodes];
        String[] names = new String[numNodes];
        buildGraph(new Coordinates(580, 100), 60, 100, root, 0, 0, adjacencyMatrix, names);
        graph = lang.newGraph("graph", adjacencyMatrix, coordinates, names, null, graphProps);

        // min/max label
        TextProperties labelProps = new TextProperties();
        labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font.deriveFont(Font.BOLD));
        labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

        for(int i = 0; i < treeDepth-1; i++) {
            if(i%2 == 0)
                lang.newText(new Offset(0, 5 + i*100, "graph", "NW"), "MAX", "maxLabel"+i, null, labelProps);
            else
                lang.newText(new Offset(0, 5 + i*100, "graph", "NW"), "MIN", "minLabel"+i, null, labelProps);
        }
        lang.nextStep();

        // open queue
        String[][] entries = new String[numNodes][1];
        for(int i = 0; i < entries.length; i++)
            entries[i][0] = "";
        openTable = lang.newStringMatrix(new Offset(70, 20, "sourceCode", "NE"), entries, "openTable", null, openProps);
        PolylineProperties arrowProps = new PolylineProperties();
        // label
        TextProperties openProps = new TextProperties();
        openProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 12));
        lang.newText(new Offset(30, -20, "openTable", "N"), "OPEN", "openLabel", null, openProps);
        // descriptor marker
        arrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        pArrow = lang.newPolyline(new Offset[]{ new Offset(-30, 10, "openTable", "NW"), new Offset(0, 10, "openTable", "NW")},
                "pArrow", null, arrowProps);
        pArrow.hide();

        lang.nextStep(trans.translateMessage("start"));
    }

    private void displayCode() {
        SourceCodeProperties smallerCodeProps = new SourceCodeProperties();
        smallerCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 10));
        smallerCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, (Color) codeProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
        smallerCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, (Color) codeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
        sourceCode = lang.newSourceCode(new Coordinates(15, 75), "sourceCode", null, smallerCodeProps);
        // add code lines
        sourceCode.addCodeLine("OPEN := { (ε, LIVE, inf) }", null, 0, null);
        sourceCode.addCodeLine("while (true)", null, 0, null);
        sourceCode.addCodeLine("pop p := (J, s, h) from OPEN", null, 1, null);
        sourceCode.addCodeLine("if J == ε and s == SOLVED", null, 1, null);
        sourceCode.addCodeLine("STOP and return h", null, 2, null);
        sourceCode.addCodeLine("else", null, 1, null);
        sourceCode.addCodeLine("if s == LIVE", null, 2, null);
        sourceCode.addCodeLine("if J is a terminal node", null, 3, null);
        sourceCode.addCodeLine("add (J, SOLVED, min(h,value(J))) to OPEN", null, 4, null);
        sourceCode.addCodeLine("else if J is a MIN node", null, 3, null);
        sourceCode.addCodeLine("add (J.1, LIVE, h) to OPEN", null, 4, null);
        sourceCode.addCodeLine("else", null, 3, null);
        sourceCode.addCodeLine("for j=1..number_of_children(J)", null, 4, null);
        sourceCode.addCodeLine("add (J.j, LIVE, h) to OPEN", null, 5, null);
        sourceCode.addCodeLine("else", null, 2, null);
        sourceCode.addCodeLine("if J is a MIN node", null, 3, null);
        sourceCode.addCodeLine("add (parent(J), SOLVED, h) to OPEN", null, 4, null);
        sourceCode.addCodeLine("purge parent(J)", null, 4, null);
        sourceCode.addCodeLine("else if is_last_child(J)", null, 3, null);
        sourceCode.addCodeLine("add (parent(J), SOLVED, h) to OPEN", null, 4, null);
        sourceCode.addCodeLine("else", null, 3, null);
        sourceCode.addCodeLine("add (rightSibling(J), LIVE, h) to OPEN", null, 4, null);

        lang.nextStep();

        // step for step description of the code
        describeCode("descrCode1", "titleCode1", 5,-150, -165, 50);
        describeCode("descrCode2", "titleCode2", 1,-110, -120, 35);
        describeCode("descrCode3", "titleCode3", 4,-40, -75, 100);
        describeCode("descrCode4", "titleCode4", 3,80, 40, 100);
        describeCode("descrCode5", "titleCode5", 1,80, 80, 20);

    }

    private void describeCode(String descriptionName, String title, int segments, int offsetYBox, int offsetYBracket, int bracketSize) {
        List<String> description = new ArrayList<String>();
        for(int i = 1; i <= segments; i++)
            description.add(trans.translateMessage(descriptionName + "_" + i));
        InfoBox descriptionBox = new InfoBox(lang, new Offset(320, offsetYBox, "sourceCode", "W"),
                description.size(), trans.translateMessage(title));
        descriptionBox.setText(description);
        TextProperties bracketProps = new TextProperties();
        bracketProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, bracketSize));
        Text bracket = lang.newText(new Offset(280, offsetYBracket, "sourceCode", "W"), "}", "bracket", null, bracketProps);
        lang.nextStep();
        descriptionBox.hide();
        bracket.hide();
    }

    /**
     * Builds a displayable Animal Graph from the internal graph structure consisting of Nodes.
     * Builds the graph bottom-up (first leaf nodes).
     *
     * @param offsetNW point north-west of the graph wheere it should be placed
     * @param deltaX distance between leaf nodes
     * @param deltaY size of one level
     * @param node that should be added to the graph
     * @param currIndex current node index
     * @param level current depth
     * @param adjacencyMatrix represents the edges
     * @param names of the nodes
     * @return the next index
     */
    private int buildGraph(Coordinates offsetNW, int deltaX, int deltaY, Node node, int currIndex, int level,
                           int[][] adjacencyMatrix, String[] names) {
        int nextIndex = currIndex + 1;
        if (node.getType() == Type.LEAF) {
            // leaf node
            Coordinates coord = null;
            for (int i = currIndex; i >= 0; i--)
                if (coordinates[i] != null) { // node is not the first leaf -> placed next to previous sibling
                    coord = new Coordinates(coordinates[i].getX() + deltaX, offsetNW.getY() + level * deltaY);
                    break;
                }
            if (coord == null) { // node is the first leaf
                coord = new Coordinates(offsetNW.getX(), offsetNW.getY() + level * deltaY);
            }
            coordinates[currIndex] = coord;
            lang.newText(new Coordinates(coord.getX(), coord.getY() + 25), "" + node.getValue(),
                    "value" + node.toString(), null, new TextProperties());
        } else {
            // min or max node
            int sumX = 0;
            for (int i = 0; i < node.numChildren(); i++) {
                int index = nextIndex;
                // recursive call -> build children first
                nextIndex = buildGraph(offsetNW, deltaX, deltaY, node.getChild(i), index, level + 1, adjacencyMatrix, names);

                // update adjacency matrix
                adjacencyMatrix[currIndex][index] = 1;
                adjacencyMatrix[index][currIndex] = 1;

                sumX += coordinates[index].getX();
            }
            // set node over center of its children
            coordinates[currIndex] = new Coordinates(sumX / node.numChildren(), offsetNW.getY() + level * deltaY);
        }
        names[currIndex] = node.toString();
        node.setIndex(currIndex);
        return nextIndex;
    }

    /**
     * Simulates the SSS* algorithm.
     * @return result of SSS*, i.e., the overall minimax value
     */
    private int sss() {
        int qNum = 0;
        MultipleChoiceQuestionModel mcq;
        // initialize OPEN
        openQueue.add(new Descriptor(root, Status.LIVE, Integer.MAX_VALUE));
        openTable.put(0, 0, "(" + root + ", LIVE, inf)", null, null);
        this.highlightSC(0);
        Descriptor p = openQueue.get(0);

        while(true) {
            // ask question 2
            if(askQuestions && Math.random() <= probQuestion && openQueue.size() > 1) {
                mcq = new MultipleChoiceQuestionModel("question2_" + qNum++);
                mcq.setPrompt(trans.translateMessage("question2"));
                mcq.setGroupID("nextGroup");
                mcq.addAnswer(openQueue.get(0).node.toString(), 1, trans.translateMessage("posFeedback"));
                for (int i = 1; i < openQueue.size(); i++) {
                    Node n = openQueue.get(i).node;
                    mcq.addAnswer(n.toString(), 0, trans.translateMessage("negFeedback") + " "
                            + trans.translateMessage("feedback2") + " " + openQueue.get(0).node.toString() + ".");
                }
                lang.addMCQuestion(mcq);
            }

            this.highlightSC(1);
            graph.unhighlightNode(p.node.getIndex(), null, null);
            // get descriptor with biggest merit (leftmost if two nodes with same biggest merit)
            p = openQueue.remove(0);
            pArrow.show();
            graph.highlightNode(p.node.getIndex(), null, null);
            this.highlightSC(2);
            this.highlightSC(3);
            // stop condition
            if(p.node.equals(root) && p.status == Status.SOLVED) {
                this.highlightSC(4);
                return p.merit;
            } else {
                this.highlightSC(5);
                this.highlightSC(6);
                if(p.status == Status.LIVE) { // status LIVE (node has not been solved yet)
                    Type t = p.node.getType();

                    // ask question 1
                    if(askQuestions && Math.random() <= probQuestion && (t == Type.LEAF || t == Type.MIN_NODE)) {
                        mcq = new MultipleChoiceQuestionModel("question1_" + qNum++);
                        mcq.setPrompt(trans.translateMessage("question1"));
                        mcq.setGroupID("nextGroup");
                        // correct answer
                        Descriptor correct = t == Type.LEAF ?
                                new Descriptor(p.node, Status.SOLVED, Math.min(p.merit, p.node.getValue())) :
                                new Descriptor(p.node.getChild(0), Status.LIVE, p.merit);
                        mcq.addAnswer(correct.toString(), 1, trans.translateMessage("posFeedback"));
                        String feedback = trans.translateMessage("negFeedback") + " " + trans.translateMessage("feedback1")
                                + " " + correct + ".";
                        if(!(t == Type.LEAF)) {
                            if(p.node.numChildren() > 1) {
                                mcq.addAnswer((new Descriptor(p.node.getChild(1), Status.LIVE, p.merit)).toString(), 0, feedback);
                            }
                            mcq.addAnswer((new Descriptor(p.node.getChild(0), Status.SOLVED, p.merit)).toString(), 0, feedback);
                        }
                        mcq.addAnswer((new Descriptor(p.node, Status.SOLVED, Math.max(p.merit, p.node.getValue()))).toString(), 0, feedback);
                        mcq.addAnswer((new Descriptor(p.node, Status.LIVE, p.merit)).toString(), 0, feedback);
                        lang.addMCQuestion(mcq);
                    }
                    this.highlightSC(7);
                    if(t == Type.LEAF) { // J is a terminal node
                        // solve terminal node
                        int merit = Math.min(p.merit, p.node.getValue());
                        openQueue.add(new Descriptor(p.node, Status.SOLVED, merit));
                        updateOpen();
                        displayMerit(p.node, "" + merit);
                        pArrow.hide();
                        this.highlightSC(8);
                    } else {
                        this.highlightSC(9);
                        if (t == Type.MIN_NODE) { // J is a min node
                            // add descriptor for first child to open
                            openQueue.add(new Descriptor(p.node.getChild(0), Status.LIVE, p.merit));
                            updateOpen();
                            pArrow.hide();
                            this.highlightSC(10);
                        } else { // J is a max node
                            this.highlightSC(11);
                            // add a descriptor for each child to open
                            for(Node child : p.node.getChildren())
                                openQueue.add(new Descriptor(child, Status.LIVE, p.merit));
                            updateOpen();
                            pArrow.hide();
                            // highlight complete for-loop in one step
                            sourceCode.highlight(12);
                            this.highlightSC(13);
                            sourceCode.unhighlight(12);
                        }
                    }
                } else { // status SOLVED
                    this.highlightSC(14);
                    Node parent = p.node.getParent();
                    this.highlightSC(15);
                    if(p.node.getType() == Type.MIN_NODE) { // J is a min node
                        // solve parent
                        openQueue.add(new Descriptor(parent, Status.SOLVED, p.merit));
                        updateOpen();
                        displayMerit(parent, "" + p.merit);
                        pArrow.hide();
                        // save old OPEN list for the case that a question is asked
                        List<Descriptor> oldList = new ArrayList<Descriptor>(openQueue);
                        // purge parent
                        this.purge(parent, openQueue);

                        // ask question 3
                        if(askQuestions) {
                            MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel("question3_" + qNum++);
                            msq.setPrompt(trans.translateMessage("question3"));
                            for (Descriptor d : oldList) {
                                boolean correct = !openQueue.contains(d);
                                msq.addAnswer(d.toString(), correct ? 1 : -1, d.toString() + " "
                                        + trans.translateMessage(correct ? "posFeedback3" : "negFeedback3"));
                            }
                            lang.addMSQuestion(msq);
                        }

                        this.highlightSC(16);
                        updateOpen();
                        this.highlightSC(17, "Purge " + parent);
                    } else {
                        this.highlightSC(18);
                        if (p.node.isLastChild()) { // J does not have a right sibling
                            // solve parent
                            openQueue.add(new Descriptor(parent, Status.SOLVED, p.merit));
                            updateOpen();
                            // write evaluated merit to node and <= merit to its siblings
                            displayMerit(parent, "" + p.merit);
                            for(Node n: parent.getParent().getChildren())
                                if(n != parent) displayMerit(n, "≤" + p.merit);
                            pArrow.hide();
                            this.highlightSC(19);
                        } else { // J has a right sibling
                            this.highlightSC(20);
                            openQueue.add(new Descriptor(p.node.getRightSibling(), Status.LIVE, p.merit));
                            updateOpen();
                            pArrow.hide();
                            this.highlightSC(21);
                        }
                    }
                }
            }
        }
    }

    /**
     *  Recursive function that removes all entries from OPEN that are associated with one of the ancestors of n.
     *
     * @param n current node that is being purged
     * @param queue the list from which the associated descriptors are removed
     */
    private void purge(Node n, List<Descriptor> queue) {
        if(n.numChildren() == 0)
            return;
        for(Node child: n.getChildren()) {
            queue.removeIf(d -> d.node.equals(child));
            purge(child, queue);
        }
    }

    private void highlightSC(int next) {
        highlightSC(next, "");
    }

    private void highlightSC(int next, String stepMessage) {
        sourceCode.highlight(next);
        lang.nextStep(stepMessage);
        sourceCode.unhighlight(next);
    }

    private void updateOpen() {
        Comparator<Descriptor> comp = (Descriptor d1, Descriptor d2) ->
                d1.merit != d2.merit ? d2.merit - d1.merit : d1.node.getIndex() - d2.node.getIndex();
        // sort list so that descriptor with biggest merit is always first (or with the leftmost node if merits are equal)
        Collections.sort(openQueue, comp);
        for(int i = 0; i < numNodes; i++) {
            if(i < openQueue.size())
                openTable.put(i, 0, "" + openQueue.get(i), null, null);
            else
                openTable.put(i, 0, "", null, null);
        }
    }

    private void displayMerit(Node n, String merit) {
        // get font from textProps and color from meritProps
        Font font = (Font) textProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
        Color color = (Color) meritProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        TextProperties meritTextProps = new TextProperties();
        meritTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        meritTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

        Coordinates coords = coordinates[n.getIndex()];
        int nodeSize = n.getName().length()*5 + 15;
        lang.newText(new Coordinates(coords.getX()+nodeSize, coords.getY()), merit, "merit" + n.getIndex(), null, meritTextProps);
    }

    private void end(int result) {
        sourceCode.hide();
        // conclusion text
        List<String> concl = new ArrayList<String>();
        concl.add(trans.translateMessage("concl1") + result + ".");
        for(int i = 2; i < 10; i++)
            concl.add(trans.translateMessage("concl" + i));
        InfoBox conclBox = new InfoBox(lang, new Coordinates(20, 150), concl.size(), trans.translateMessage("conclTitle"));
        conclBox.setText(concl);
        lang.nextStep(trans.translateMessage("conclTitle"));

        lang.finalizeGeneration();
    }
}
