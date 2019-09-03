/*
 * QLearning.java
 * Dmitrij Kress, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.primitives.Variables;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class QLearning implements Generator {
    // Visualization attributes
    private Language lang;
    private Variables vars;
    private final String FINISH_SYMBOL = "\u2606";
    private final String TRAP_SYMBOL = "\u2620";
    
    private Color tableStateColor = Color.getHSBColor(0.90f, 0, 0.9f);
    private Color qUpdateColor = new Color(0xFFAA53);
    private Color currentStateColor = new Color(0xF23E2B);
    
    private Rect[][] qFunctionTable; 
    private Text[][] qFunctionText;
    private Rect[][] rectField;
    private Text[][] textField;
    private Text[] textFieldLabel;
    private Text[] annotation;    
    private Text aText;
    private Text qFuncLabel;
    private SourceCode code;


    // Algorithm attributes
    private double alpha;
    private double gamma;
    
    private int[] coordinatesFinish;
    private int[][] coordinatesTraps;
    private double rewardMove;
    private double rewardFinish;
    private double rewardTrap;
    
    private SourceCodeProperties sourceCodeProps;
    private TextProperties rectTextProps;
    private RectProperties rectsProps;
    private SourceCodeProperties textsProps;

    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int UP = 2;
    private final int DOWN = 3;       

    double [][] rewards;
    double [][][] qFunction = new double[4][3][4];
    
    private int detailedIterations;
    private int numberOfUpdates;
    
    private int learningSteps;
    
    private int iteration;

    
    
    public void init(){
        lang = new AnimalScript("QLearning", "Dmitrij Kress", 900, 600);
        learningSteps = 0;        
        iteration = 1;
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        lang.setStepMode(true);
        
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        rectTextProps = (TextProperties)props.getPropertiesByName("rectText");
        rectsProps = (RectProperties)props.getPropertiesByName("rects");
        textsProps = (SourceCodeProperties)props.getPropertiesByName("text");
        
        coordinatesTraps = (int[][])primitives.get("traps");        
        alpha = Double.valueOf(primitives.get("alpha").toString());
        gamma = Double.valueOf(primitives.get("gamma").toString());
        rewardTrap = (Integer)primitives.get("rewardTrap");
        rewardFinish = (Integer)primitives.get("rewardFinish");
        rewardMove = (Integer)primitives.get("rewardMove");
        coordinatesFinish = (int[])primitives.get("finish");
        numberOfUpdates = (Integer)primitives.get("numberOfUpdates");
        detailedIterations = (Integer)primitives.get("detailedIterations");
        
        rewards = new double[4][3];
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (coordinatesFinish[0] == i && coordinatesFinish[1] == j) {
                    rewards[i][j] = rewardFinish;
                } else {
                    rewards[i][j] = rewardMove;
                    for (int[] trap : coordinatesTraps) {
                        if (trap[0] == i && trap[1] == j) {
                            rewards[i][j] = rewardTrap;
                        }
                    }
                }
                for (Integer action : actionsAvailable(i, j)) {
                    setQ(i, j, action, 0);
                }
            }
        }
        
        vars = lang.newVariables();
        vars.declare("double", "alpha");
        vars.set("alpha", "" + round(alpha));
        vars.declare("double", "gamma");
        vars.set("gamma", "" + round(gamma));
        vars.declare("int", "iteration");
        vars.setGlobal("iteration");
        vars.set("iteration", "1");

        
        
        introduction1();                
        initializeField(); 
        introduction2();
        initializeQTable();
        initializeAnnotation();
        learn();
        conclusion();
        
        return lang.toString();
    }

    public String getName() {
        return "QLearning";
    }

    public String getAlgorithmName() {
        return "Q-Learning";
    }

    public String getAnimationAuthor() {
        return "Dmitrij Kress";
    }

    public String getDescription(){
        return "Q-Learning is a machine learning algorithm from the reinforcement learning"
 +"\n"
 +"area. The goal of the Q-Learning algorithm is to find a function Q which gives the"
 +"\n"
 +"utility of a pair (s, a) where s is a state and a is an action. To achieve that the"
 +"\n"
 +"algorithm performs an action and receives the immediate reward. The rewards"
 +"\n"
 +"received in different states are used to calculate the Q-Function.";
    }

    public String getCodeExample(){
        return "Initialize all Q(s, a)"
 +"\n"
 +"Loop"
 +"\n"
 +"   Select a state s"
 +"\n"
 +"   While (s != target_state)"
 +"\n"
 +"      Select an action a and execute it"
 +"\n"
 +"      Receive the immediate reward and observe the new state s'"
 +"\n"
 +"      Update the Q-Function:"
 +"\n"
 +"         Q(s, a) := Q(s, a) + \u03B1*((r(s, a) + \u03B3max Q(s', a')) - Q(s, a))"
 +"\n"
 +"      s := s'"
 +"\n";
    }


    
    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    
    
    
    
    /**
     * Show introductory texts.
     */
    public void introduction1() {
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        Text header = lang.newText(new Coordinates(150, 20), "Q-Learning", "header", null, headerProps);
        
        RectProperties headerRectProps = new RectProperties();
        headerRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);

        int border = 2;
        Rect headerRect = lang.newRect(new Offset(-border , -border, header, "NW"), 
                                       new Offset(border, border, header, "SE"), "headerRect", null, headerRectProps);
                
                
        SourceCodeProperties textProps = new SourceCodeProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        
        SourceCode intro = lang.newSourceCode(new Coordinates(15, 40), "text", null, textsProps);
        intro.addCodeLine("Q-Learning is a machine learning algorithm from the reinforcement learning", null, 0, null);
        intro.addCodeLine("area. The goal of the Q-Learning algorithm is to find a function Q which gives the", null, 0, null);
        intro.addCodeLine("utility of a pair (s, a) where s is a state and a is an action. To achieve that the", null, 0, null);
        intro.addCodeLine("algorithm performs an action and receives the immediate reward. The rewards", null, 0, null);
        intro.addCodeLine("received in different states are used to calculate the Q-Function.", null, 0, null);
         
        lang.nextStep("Introduction");
        
        intro.hide();
        
        code = lang.newSourceCode(new Coordinates(15, 40), "text", null, textsProps);
        code.addCodeLine("The outline of the algorithm looks as follows:", null, 0, null);
        code.addCodeLine("", null, 0, null);
        code.addCodeLine("Initialize all Q(s, a) with 0", null, 0, null);
        code.addCodeLine("Loop", null, 0, null);
        code.addCodeLine("   Select a state s", null, 0, null);
        code.addCodeLine("   While (s != target_state)", null, 0, null);
        code.addCodeLine("      Select an action a and execute it", null, 0, null);
        code.addCodeLine("      Receive the immediate reward and observe the new state s'", null, 0, null);
        code.addCodeLine("      Update the Q-Function:", null, 0, null);
        code.addCodeLine("         Q(s, a) := Q(s, a) + \u03B1*((r(s, a) + \u03B3max Q(s', a')) - Q(s, a))", null, 0, null);
        code.addCodeLine("      s := s'", null, 0, null);
        code.addCodeLine("", null, 0, null);
        code.addCodeLine("\u03B1 is learning rate which controls how much the new value will change the function. \u03B3 is discount factor. ", null, 0, null);
        code.addCodeLine("The purpose of discount factor is to weight immediate rewards higher than the ones further in the future", null, 0, null);
        
        
        TextProperties aTextProps = new TextProperties();
        aTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
        aTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
                (Color)sourceCodeProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
        aText = lang.newText(new Offset(259, -70, code, "SW"), "a'", "aText", null, aTextProps);
        
        lang.nextStep();

        code.hide();
    }
    
    
    /**
     * Show the text that explains the example.
     */
    public void introduction2() {
        SourceCodeProperties textProps = new SourceCodeProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        textProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, currentStateColor);
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        
        SourceCode intro = lang.newSourceCode(new Coordinates(40, 250), "text", null, textsProps);
        intro.addCodeLine("We illustrate how the algorithm works using following example. The", null, 0, null);
        intro.addCodeLine("goal is to find a shortest path to " + FINISH_SYMBOL + " from every square while", null, 0, null);
        intro.addCodeLine("avoiding " + TRAP_SYMBOL + ". Reaching " + FINISH_SYMBOL + " leads to a reward of "+ (int)rewardFinish
                            +". " , null, 0, null);
        intro.addCodeLine(TRAP_SYMBOL + " corresponds to a negative reward of " + (int)rewardTrap + ", and every step costs " + (int)rewardMove + ".", null, 0, null);
        intro.addCodeLine("The algorithm chooses an initial state randomly and performs one of", null, 0, null);
        intro.addCodeLine("the actions which currently have the highest Q-Value (if there are ", null, 0, null);
        intro.addCodeLine("several such actions, one of them is chosen randomly) and updates the", null, 0, null);
        intro.addCodeLine("Q-Function until the finish state is reached. After that", null, 0, null);
        intro.addCodeLine("another random state is chosen. ", null, 0, null); 
        intro.addCodeLine("Best actions according to the Q function are indicated with arrows.", null, 0, null); 
        
        
        
        code = lang.newSourceCode(new Coordinates(15, 40), "text", null, sourceCodeProps);
        code.addCodeLine("", null, 0, null);
        code.addCodeLine("", null, 0, null);
        code.addCodeLine("Initialize all Q(s, a) with 0", null, 0, null);
        code.addCodeLine("Loop", null, 0, null);
        code.addCodeLine("   Select a state s", null, 0, null);
        code.addCodeLine("   While (s != target_state)", null, 0, null);
        code.addCodeLine("      Select an action a and execute it", null, 0, null);
        code.addCodeLine("      Receive the immediate reward and observe the new state s'", null, 0, null);
        code.addCodeLine("      Update the Q-Function:", null, 0, null);
        code.addCodeLine("         Q(s, a) := Q(s, a) + \u03B1*((r(s, a) + \u03B3max Q(s', a')) - Q(s, a))", null, 0, null);
        code.addCodeLine("      s := s'", null, 0, null);
        code.addCodeLine("", null, 0, null);
        
        lang.nextStep("Example explained");
        
        intro.hide();
    }
    
    
    
    /**
     * Show conclusion texts.
     */
    public void conclusion() {
        for (Rect[] rects : rectField) {
            for (Rect r : rects) {
                r.hide();
            }
        }
        for (Rect[] rects : qFunctionTable) {
            for (Rect r : rects) {
                r.hide();
            }
        }
        for (Text[] txts : qFunctionText) {
            for (Text t : txts) {
                t.hide();
            }
        }
        for (Text[] txts : textField) {
            for (Text t : txts) {
                t.hide();
            }
        }
        for (Text t : annotation) {
            t.hide();
        }
        aText.hide();
        code.hide();
        qFuncLabel.hide();
        for (Text t : textFieldLabel) {
            t.hide();
        }
        
        
                
                
        SourceCodeProperties textProps = new SourceCodeProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        
        SourceCode intro = lang.newSourceCode(new Coordinates(15, 40), "text", null, textsProps);
        intro.addCodeLine("It can be shown that the Q-Function converges towards real value if each action is", null, 0, null);
        intro.addCodeLine("executed infinitely often in every state. In practice however convergence also occurs ", null, 0, null);
        intro.addCodeLine("under less strict condition.", null, 0, null);
         
        
        lang.nextStep("Conclusion");
        
        
    }
    
    
    
    
    /**
     * Draw the field.
     */
    public void initializeField() {
        int posX = 445;
        int posY = 70;
        int cellSize = 40;
        
        /*
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        */
         
        rectField = new Rect[4][3];
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                rectField[i][j] = lang.newRect(new Coordinates(posX + i*cellSize, posY + j*cellSize), 
                                               new Coordinates(posX + (i + 1)*cellSize, posY + (j + 1)*cellSize), 
                                               "Field_" + i + "_" + j, null, rectsProps);
            }
        }
        
        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, cellSize));
        rectTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, cellSize));
        textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        
        textField = new Text[4][3];
        
        // Next line is here only because without it the first element of textField isn't placed properly 
        Text uselessText = lang.newText(new Offset(0, 0, rectField[0][0], AnimalScript.DIRECTION_N), "A", "tmp", null, rectTextProps);
        uselessText.hide();
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                String text = "  ";
                if (coordinatesFinish[0] == i && coordinatesFinish[1] == j) {
                    text = FINISH_SYMBOL;
                } else {
                    for (int[] trap : coordinatesTraps) {
                        if (trap[0] == i && trap[1] == j) {
                            text = TRAP_SYMBOL;
                        }
                    }
                    
                } 
                textField[i][j] = lang.newText(new Offset(0, 0, rectField[i][j], AnimalScript.DIRECTION_N),
                        text, "fieldText_" + i + "_" + j, null, textProps);
            }
        }
       
        
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, (int) (cellSize / 2)));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);
        
        textFieldLabel = new Text[7];
        
        uselessText = lang.newText(new Offset(0, 0, rectField[0][0], AnimalScript.DIRECTION_N), "A", "tmp", null, textProps);
        uselessText.hide();
        
        for (int i = 0; i < 4; i++) {
           textFieldLabel[i] = lang.newText(new Offset(0, -27, rectField[i][0], "N"),  
                                            Integer.toString(i), "TextFieldLabel_1_" + i, null, textProps);
        } 
        for (int i = 0; i < 3; i++) {
            textFieldLabel[i + 4] = lang.newText(new Offset(-12, -14, rectField[0][i], "W"), 
                    Integer.toString(i), "TextFieldLabel_1_" + i, null, textProps);
        }
    }

    
    
    /**
     * Set the Text-object which will be used to display 
     * the comment for the steps of the example.
     */
    private void initializeAnnotation() {
        int fontSize = 12;
        int lineDistance = fontSize + 11;        
        Coordinates textPosition = new Coordinates(50, 270);
        
        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, fontSize));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
               
        annotation = new Text[8];
        annotation[0] = lang.newText(new Offset(0, 0, textPosition, AnimalScript.DIRECTION_NW), 
                "\u03B1 = " + alpha + ",  \u03B3 = " + gamma, "annotation0", null, textProps);
        annotation[1] = lang.newText(new Offset(0, lineDistance, annotation[0], AnimalScript.DIRECTION_NW),
                "", "annotation1", null, textProps);
        annotation[2] = lang.newText(new Offset(0, lineDistance, annotation[1], AnimalScript.DIRECTION_NW),
                "", "annotation2", null, textProps);
        annotation[3] = lang.newText(new Offset(0, lineDistance, annotation[2], AnimalScript.DIRECTION_NW),
                "" , "annotation3", null, textProps);
        annotation[4] = lang.newText(new Offset(0, lineDistance, annotation[3], AnimalScript.DIRECTION_NW),
                "" , "annotation4", null, textProps);
        annotation[5] = lang.newText(new Offset(0, lineDistance, annotation[4], AnimalScript.DIRECTION_NW),
                "" , "annotation5", null, textProps);
        annotation[6] = lang.newText(new Offset(0, lineDistance, annotation[5], AnimalScript.DIRECTION_NW),
                "" , "annotation5", null, textProps);
        
        annotation[7] = lang.newText(new Offset(0, -lineDistance, annotation[0], AnimalScript.DIRECTION_NW),
                "Iteration " , "annotation0", null, textProps);
    }
    
    
    
    
    /**
     * Draw the table showing current Q-Function.
     */
    public void initializeQTable() {
        int posX = 620;
        int posY = 50;
        int width = 60;
        int height = 26;
        int fontSize = 13;
        
        code.highlight(2);
        
 
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
         
        qFunctionTable = new Rect[5][13];
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 13; j++) {
                qFunctionTable[i][j] = lang.newRect(new Coordinates(posX + i*width, posY + j*height), 
                                               new Coordinates(posX + (i + 1)*width, posY + (j + 1)*height), 
                                               "Field_" + i + "_" + j, null, rectProps);            }
            qFunctionTable[i][0].changeColor("fillColor", Color.LIGHT_GRAY, null, null);
        }
        
        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, fontSize));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        
                
        Text uselessText = lang.newText(new Offset(0, 0, qFunctionTable[0][0], AnimalScript.DIRECTION_N), "A", "tmp", null, textProps);
        uselessText.hide();
        
        qFunctionText = new Text[5][13];
        qFunctionText[0][0] = lang.newText(new Offset(0, 5, qFunctionTable[0][0], AnimalScript.DIRECTION_N),
                "State", "", null, textProps);
        qFunctionText[1][0] = lang.newText(new Offset(0, 5, qFunctionTable[1][0], AnimalScript.DIRECTION_N),
                "left", "", null, textProps);
        qFunctionText[2][0] = lang.newText(new Offset(0, 5, qFunctionTable[2][0], AnimalScript.DIRECTION_N),
                "right", "", null, textProps);
        qFunctionText[3][0] = lang.newText(new Offset(0, 5, qFunctionTable[3][0], AnimalScript.DIRECTION_N),
                "up", "", null, textProps);
        qFunctionText[4][0] = lang.newText(new Offset(0, 5, qFunctionTable[4][0], AnimalScript.DIRECTION_N),
                "down", "", null, textProps);
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                qFunctionText[0][1 +j*4 + i] = lang.newText(new Offset(0, 5, qFunctionTable[0][1 +j*4 + i], AnimalScript.DIRECTION_N),
                        "(" + j + ", " + i +")", "", null, textProps);
                qFunctionTable[0][1 +j*4 + i].changeColor("fillColor", tableStateColor, null, null);
            }
        }

        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 13; j++) {
                String text;
                double q = getQ((j - 1) % 4, (j - 1)/4,  i - 1);
                if (!Double.isNaN(q)) {
                    text = "" + round(q);

                } else {
                    text = "  --";
                }
                
                qFunctionText[i][j] = lang.newText(new Offset(-8, 5, qFunctionTable[i][j], AnimalScript.DIRECTION_N),
                        "       ", "", null, textProps);
                qFunctionText[i][j].setText(text, null, null);
            }
        }
        
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, fontSize));
        qFuncLabel = lang.newText(new Offset(0, -fontSize - 4, qFunctionTable[2][0], AnimalScript.DIRECTION_N), "Q-Function", "tmp", null, textProps);
        
        lang.nextStep("Demonstration");
    }
    
    
    
  
    
    /**
     * Displays the paths that are best according to 
     * current Q-Function.
     */
    private void showArrows() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                boolean emptySquare = true;
                if (coordinatesFinish[0] == i && coordinatesFinish[1] == j) {
                    emptySquare = false;;
                }
                for (int[] trap : coordinatesTraps) {
                    if (trap[0] == i && trap[1] == j) {
                        emptySquare = false;;
                    }
                }
                if (!emptySquare) {
                    continue;
                }
                
                Double maxReward = Double.NEGATIVE_INFINITY;
                boolean equalRewards = false;
                int bestAction = -1;
                for (Integer act : actionsAvailable(i, j)) {
                    if (getQ(i, j, act) > maxReward) {
                        bestAction = act;
                        maxReward = getQ(i, j, act);
                        equalRewards = false;
                    } else if (getQ(i, j, act) == maxReward) {
                        equalRewards = true;
                    }
                }
                
                if (!equalRewards) { 
                    switch (bestAction) {
                        case LEFT: if (!textField[i][j].getText().equals("←")) {
                                        textField[i][j].setText("←", null, null);
                                   }                            
                                   break;
                        case RIGHT: if (!textField[i][j].getText().equals("→")) {
                                        textField[i][j].setText("→", null, null);
                                    }  
                                    break;
                        case UP:    if (!textField[i][j].getText().equals("↑")) {
                                        textField[i][j].setText("↑", null, null);
                                    }  
                                    break;
                        case DOWN:  if (!textField[i][j].getText().equals("↓")) {
                                        textField[i][j].setText("↓", null, null);
                                    }  
                                    break;                    
                    } 
                } else {
                    if (!textField[i][j].getText().equals(" ")) {
                        textField[i][j].setText(" ", null, null);
                    }
                }
            }
        }
    }

    
    
    
    
    /**
     * Execute and visualize Q-Learning.
     */
    public void learn() {
        
        code.unhighlight(2);
        code.highlight(4);
        while (learningSteps <= numberOfUpdates) {            
            learnCycle();  
            iteration++;
        }
        
    }
    

    /**
     * Choose one state randomly and follow the current best strategy until the goal state is reached.
     * After an action is performed the Q-Function is updated and visualization is refreshed. 
     */
    private void learnCycle() {
        // Choose a random state
        Random rand = new Random();
        int[] state = new int[2];
        state[0] = rand.nextInt(4);
        state[1] = rand.nextInt(3);
        
        Color color = (Color) rectsProps.get(AnimationPropertiesKeys.FILL_PROPERTY);
        
        vars.set("iteration", "" + iteration);
        annotation[7].setText("Iteration " + iteration, null, null);
        
        annotation[0].setText("s := (" + state[1] + ", " + state[0] + ")", null, null);
        rectField[state[0]][state[1]].changeColor("fillColor", currentStateColor, null, null);
        qFunctionTable[0][state[1]*4 + state[0] + 1].changeColor("fillColor", currentStateColor, null, null);
        lang.nextStep("Iteration " + iteration);

        if (state[0] == coordinatesFinish[0] && state[1] == coordinatesFinish[1]) {
            rectField[state[0]][state[1]].changeColor("fillColor", color, null, null);
            qFunctionTable[0][state[1]*4 + state[0] + 1].changeColor("fillColor", tableStateColor, null, null);
        }

        
        for (int i = 0; i <= 20 && learningSteps <= numberOfUpdates; i++) {
            LinkedList<Integer> bestActions = null;
            
            // Select randomly one of the actions that
            // have the highest Q-Value
            double maxFound = Double.NEGATIVE_INFINITY;
            for (Integer act : actionsAvailable(state[0], state[1])) {
                if (getQ(state[0], state[1], act) > maxFound) {
                    bestActions = new LinkedList<Integer>();
                    bestActions.add(act);
                    maxFound = getQ(state[0], state[1], act);
                } else if (getQ(state[0], state[1], act) == maxFound) {
                    bestActions.add(act);
                }
            }           
            int action = bestActions.get(rand.nextInt(bestActions.size()));
            
            
            int nextState[] = stateAfterAction(state, action);
            double maxAfter = Double.NEGATIVE_INFINITY;
            for (Integer act : actionsAvailable(nextState[0], nextState[1])) {
                if (getQ(nextState[0], nextState[1], act) > maxAfter) {
                    maxAfter = getQ(nextState[0], nextState[1], act);
                }
            }

            if(state[0] == 1 && state[1] == 0) {
                break;
            }
            
            // calculate new value for Q((state[0], state[1]), action)
            double qQurrent = getQ(state[0], state[1], action);
            double qNew = qQurrent + alpha*(rewards[nextState[0]][nextState[1]] + gamma*maxAfter - qQurrent);
            
            showArrows();
            
                      
            // update Q-function
            setQ(state[0], state[1], action, qNew);
            learningSteps++;
            

            
            String actionName = action <= 1 ? (action == 0 ? "left" : "right") : (action == 2 ? "up" : "down");   
            String updateTerm = "Q((" + state[1] + ", " + state[0] + "), " + actionName + ") := " 
                    + round(qQurrent) + " + " + alpha + " * (" + rewards[nextState[0]][nextState[1]] + " + " 
                    + gamma + " * " + round(maxAfter);
            if (qQurrent >= 0) {
                updateTerm += " - " + round(qQurrent);
            } else {
                updateTerm += " - (" + round(qQurrent)+ ")";
            }            
            updateTerm += ") = " + round(qNew);
            
            annotation[1].setText("a := " + actionName, null, null);   
            rectField[nextState[0]][nextState[1]].changeColor("fillColor", Color.PINK, null, null);
            qFunctionTable[action+1][0].changeColor("fillColor", Color.PINK, null, null);
            
            
            
            code.unhighlight(4);
            if (learningSteps <= detailedIterations) {
                
                code.unhighlight(10);                
                code.highlight(6);
                lang.nextStep();  
                code.unhighlight(6);
            } else {
                code.highlight(5);
            }
            
            
            

            annotation[2].setText("r((" + state[1] + ", " + state[0] + "), " + actionName + ") = " 
                                   + rewards[nextState[0]][nextState[1]], null, null);
            annotation[3].setText("s' = (" + nextState[1] + ", " + nextState[0] + ")", null, null);
            
            if (learningSteps <= detailedIterations) {
                code.highlight(7);
                lang.nextStep();  
                code.unhighlight(7);
                code.highlight(8);
                code.highlight(9);
                aText.changeColor("color", 
                        (Color)sourceCodeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
            } 
            
            qFunctionTable[action+1][state[1]*4+state[0] + 1].changeColor("fillColor", qUpdateColor, null, null);

            annotation[4].setText(updateTerm, null, null);
            qFunctionText[action + 1][1 + state[1]*4 + state[0]].setText(round(qNew) + "", null, null);
            
            showArrows();
            lang.nextStep();
            
            qFunctionTable[action+1][state[1]*4+state[0] + 1].changeColor("fillColor", Color.WHITE, null, null);
            
            if (learningSteps <= detailedIterations) {                         
                code.unhighlight(8);
                code.unhighlight(9);
                aText.changeColor("color", 
                        (Color)sourceCodeProps.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
                code.highlight(10);
            } 
            
            
            
            annotation[0].setText("s := (" + nextState[0] + ", " + nextState[1] + ")", null, null);
            annotation[1].setText("", null, null);
            annotation[2].setText("", null, null);
            annotation[3].setText("", null, null);
            annotation[4].setText("", null, null);
            rectField[state[0]][state[1]].changeColor("fillColor", color, null, null);
            qFunctionTable[action+1][0].changeColor("fillColor", Color.LIGHT_GRAY, null, null);
            qFunctionTable[0][state[1]*4 + state[0] + 1].changeColor("fillColor", tableStateColor, null, null);
            rectField[nextState[0]][nextState[1]].changeColor("fillColor", currentStateColor, null, null);
            qFunctionTable[0][nextState[1]*4 + nextState[0] + 1].changeColor("fillColor", currentStateColor, null, null);
            
            if (learningSteps <= detailedIterations) {
                lang.nextStep();                
            }            
            
            
            
            if ((nextState[0] == coordinatesFinish[0] && nextState[1] == coordinatesFinish[1]) || i == 20) {                
                code.unhighlight(10);                
                if (learningSteps > detailedIterations) {
                    annotation[0].setText("s := (" + nextState[0] + ", " + nextState[1] + ")", null, null);
                    annotation[1].setText("", null, null);
                    annotation[2].setText("", null, null);
                    annotation[3].setText("", null, null);
                    annotation[4].setText("", null, null);
                    rectField[nextState[0]][nextState[1]].changeColor("fillColor", currentStateColor, null, null);
                    lang.nextStep();
                    code.unhighlight(5);
                }
                code.highlight(4);
                rectField[nextState[0]][nextState[1]].changeColor("fillColor", color, null, null);
                qFunctionTable[0][nextState[1]*4+nextState[0] + 1].changeColor("fillColor", tableStateColor, null, null);
            }
            
            if (learningSteps == detailedIterations) {
                code.unhighlight(10); 
            }
                         
            state = nextState;
        }
    }
    
    
    /**
     * Returns a new state that is reached after an action is performed.
     * @param state int[2] representing a state.
     * @param action action to be performed.
     * @return int[2] that represents a new state.
     */
    private int[] stateAfterAction(int[] state, int action) {
        int[] ret = new int [2];
        ret[0] = state[0];
        ret[1] = state[1];
        if (action == LEFT) ret[0]--;
        if (action == RIGHT) ret[0]++;
        if (action == UP) ret[1]--;
        if (action == DOWN) ret[1]++;
        return ret;
    }
    
    /**
     * Current Q-Value for the point (c1, c2).
     */
    private double getQ(int c1, int c2, int action) {
        for (Integer act : this.actionsAvailable(c1, c2)) {
            if (act == action) {
               return qFunction[c1][c2][action];
            }
        }
        return Double.NaN;
    }
    
    
    private void setQ(int c1, int c2, int action, double value) {
        qFunction[c1][c2][action] = value;
    }
    
    
    
    /** 
     * Returns list with possible actions from the state (c1, c2).
     */
    private LinkedList<Integer> actionsAvailable(int c1, int c2) {
        LinkedList<Integer> retList = new LinkedList<Integer>();
        if (c1 > 0) {
            retList.add(LEFT);
        }
        if (c1 < 3) {
            retList.add(RIGHT);
        }
        if (c2 < 2) {
            retList.add(DOWN);
        }
        if (c2 > 0) {
            retList.add(UP);
        }
        return retList;
    }
   
    
    private static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        return (double) Math.round(value) / factor;
    }
    

}
