/*
 * RB_Baume_DELETE.java
 * Maciej Aleksander Mokwinski, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

import java.util.Hashtable;

import translator.Translator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class RB_TREES_DELETE implements Generator {
    private static Language                lang;
    private int[]                          insertVertexValues;
    private int[]                          deleteVertexValues;
    static RB_TREES_DELETE_SRC             t;

    private static int                     graphCounter       = 0;
    /**
     * title / header
     */
    public static Graph                    graph;

    public static ArrayList<Vertex_DELETE> vertices           = new ArrayList<>();

    // -- SourceCode -- //
    private SourceCode                     srcDELETE_FIX;
    private SourceCode                     srcRightRotate;
    private SourceCode                     srcLeftRotate;
    private SourceCode                     srcDELETE;

    private SourceCodeProperties           sourceCodePropsDELETE_FIX;
    private SourceCodeProperties           sourceCodePropsRightRotate;
    private SourceCodeProperties           sourceCodePropsLeftRotate;
    private SourceCodeProperties           sourceCodePropsDELETE;
    // -- SourceCode -- //

    private Rect                           hRect;
    private Text                           header;

    private static TextProperties          infoTextProperties = new TextProperties();

    // private Color GRAPH_RED_COLOR = Color.RED;
    private int                            black_value        = 84;
    private Color                          GRAPH_BLACK_COLOR  = new Color(black_value,
                                                                      black_value,
                                                                      black_value);
    private Color                          notActiveColor;

    private Text[]                         valuesText;
    private Circle[]                       valuesCircles;

    private static int[]                   offsetsGraph       = { 150, 75, 50, 25, 15 };

    Random                                 randomGenerator    = new Random();
    private int                            questionProbability;

    private Variables                      vars;

    public static Graph[]                  graphenArray;

    private static int                     border_px          = 20;

    private Locale                         alocale;
    private Translator                     translator;
    private AnimationPropertiesContainer   props;

    public RB_TREES_DELETE(String resourceName, Locale locale) {

        this.alocale = locale;
        translator = new Translator(resourceName, alocale);
        init();

    }

    @Override
    public void init() {
        lang = new AnimalScript(translator.translateMessage("algorithmName"),
                "Maciej Aleksander Mokwinski + Julian Harbarth", 600, 400);

        lang.setStepMode(true);

        lang.setInteractionType(
                Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        this.props = props;

        // Resetting the list from possible former executions
        vertices = new ArrayList<Vertex_DELETE>();

        insertVertexValues = (int[]) primitives.get("VertexValues");
        deleteVertexValues = (int[]) primitives.get("deleteVertexValues");
        questionProbability = (int) primitives.get("questionProbability");
        Vertex_DELETE v;

        SourceCodeProperties notActiveColorProps = (SourceCodeProperties) props
                .getPropertiesByName("notActiveColor");

        this.notActiveColor = (Color) notActiveColorProps.get("color");

        vars = lang.newVariables();

        
        createTitle();
        createInfoText();
        
        drawRules();
        drawPseudoCode();

        int random = randomGenerator.nextInt(100);

        System.out.println("prob: " + questionProbability);
        System.out.println("rnd: " + random);

        if (random <= questionProbability) {
            MultipleSelectionQuestionModel msqm = new MultipleSelectionQuestionModel("color");

            msqm.setPrompt(translator.translateMessage("questionWhichColor"));

            msqm.addAnswer(translator.translateMessage("questionAnswer1"), 1,
                    translator.translateMessage("questionCorrectAnswerColor") + "\n\n");
            msqm.addAnswer(translator.translateMessage("questionAnswer2"), 1,
                    translator.translateMessage("questionCorrectAnswerColor") + "\n\n");

            msqm.addAnswer(translator.translateMessage("questionAnswer3"),
                    0,
                    translator.translateMessage("questionWrongAnswer",
                            translator.translateMessage("questionAnswer1"))
                            + " "
                            + translator.translateMessage("questionand") + " "
                            + translator.translateMessage("questionAnswer2") + "!\n\n");
            msqm.addAnswer(
                    translator.translateMessage("questionAnswer4"),
                    0,
                    translator.translateMessage("questionWrongAnswer",
                            translator.translateMessage("questionAnswer1"))
                            + " "
                            + translator.translateMessage("questionand") + " "
                            + translator.translateMessage("questionAnswer2") + "!\n\n");
            msqm.addAnswer(
                    translator.translateMessage("questionAnswer5"),
                    0,
                    translator.translateMessage("questionWrongAnswer",
                            translator.translateMessage("questionAnswer1"))
                            + " "
                            + translator.translateMessage("questionand") + " "
                            + translator.translateMessage("questionAnswer2") + "!\n\n");

            lang.addMSQuestion(msqm);
        }
        
        

        t = new RB_TREES_DELETE_SRC(lang, translator, border_px, infoTextProperties,
                srcDELETE, srcDELETE_FIX, srcLeftRotate, srcRightRotate, notActiveColor, vars,
                questionProbability);

        // Insert the Vertices
        for (int i = 0; i < insertVertexValues.length; i++) {

            v = new Vertex_DELETE(insertVertexValues[i]);

            vertices.add(v);

            v.setVertexIndex(vertices.size() - 1);

            t.insert(v);
        }

        // Draw not deleted vertices
        valuesText = new Text[deleteVertexValues.length];
        valuesCircles = new Circle[deleteVertexValues.length];
        drawNotInsertedVertices();
        
        

        // Draw NULL node
        drawNullNode();

        // draw the graph with the inserted vertices
        updateGraph();

        // after all vertices has been inserted
        // the deletion can be started:

        // DELETION:
        // pick a value form the input-array
        // the picked value, is the value of the vertex, that should be deleted
        for (int i = 0; i < deleteVertexValues.length; i++) {
            Vertex_DELETE insertedVertex;

            // having the vlaue of the vertex, find the vertex itself
            // if the vertex with the given value, does not exists
            // then is just not going to be found (this leads to no errors)
            for (int j = 0; j < vertices.size(); j++) {

                insertedVertex = vertices.get(j);

                if (insertedVertex.getValue() == deleteVertexValues[i]) {

                    lang.nextStep(translator.translateMessage("tableOfContentsDelete",
                            Integer.toString(i + 1)));

                    System.out.println("DELETING: " + insertedVertex.getValue());
                    t.delete(insertedVertex);

                    updateGraph();

                    // Color the inserted vertex green
                    valuesCircles[i].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
                            Color.GREEN, null, null);

                }
            }
        }

        random = randomGenerator.nextInt(100);

        if (random <= questionProbability) {

            MultipleChoiceQuestionModel msqm = new MultipleChoiceQuestionModel("runtime");

            msqm.setPrompt(translator.translateMessage("questionRuntime"));

            msqm.addAnswer("O(n²)", 0, translator.translateMessage("questionWrongAnswerRuntime"));
            msqm.addAnswer("O(n)", 0, translator.translateMessage("questionWrongAnswerRuntime"));
            msqm.addAnswer("O(n log n)", 0,
                    translator.translateMessage("questionWrongAnswerRuntime"));
            msqm.addAnswer("O(log n)", 1,
                    translator.translateMessage("questionCorrectAnswerRuntime"));

            lang.addMCQuestion(msqm);
        }

        lang.nextStep(translator.translateMessage("tableOfContentsFinished"));
        // FAZIT
        t.hide_all();
        lang.hideAllPrimitives();
        header.show();
        hRect.show();

        showOutro();

        lang.finalizeGeneration();
        return lang.toString();
    }

    private void showOutro() {
        TextProperties textProps = new TextProperties();

        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));

        // -- Outro 0 -- //
        lang.newText(new Coordinates(border_px, 100),
                translator.translateMessage("outro00"),
                "outro00", null, textProps);

        // -- Outro 1 -- //
        lang.newText(new Offset(0, 50, "outro00", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro10"),
                "outro10", null, textProps);

        // -- Outro 2 -- //
        lang.newText(new Offset(0, 50, "outro10", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro20"),
                "outro20", null, textProps);

        lang.newText(new Offset(0, 25, "outro20", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro21"),
                "outro21", null, textProps);

        lang.newText(new Offset(0, 25, "outro21", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro22"),
                "outro22", null, textProps);

        // -- Outro 3 -- //
        lang.newText(new Offset(0, 50, "outro22", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro30"),
                "outro30", null, textProps);

        lang.newText(new Offset(0, 25, "outro30", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro31"),
                "outro31", null, textProps);

        lang.newText(new Offset(0, 25, "outro31", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro32"),
                "outro32", null, textProps);

        lang.newText(new Offset(0, 25, "outro32", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro33"),
                "outro33", null, textProps);

        lang.newText(new Offset(0, 25, "outro33", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro34"),
                "outro34", null, textProps);

        // -- Outro 4 -- //
        lang.newText(new Offset(0, 50, "outro34", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro40"),
                "outro40", null, textProps);

        lang.newText(new Offset(0, 25, "outro40", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro41"),
                "outro41", null, textProps);

        lang.newText(new Offset(0, 25, "outro41", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro42"),
                "outro42", null, textProps);

        lang.newText(new Offset(0, 25, "outro42", AnimalScript.DIRECTION_NW),
                translator.translateMessage("outro43"),
                "outro43", null, textProps);

        lang.nextStep(translator.translateMessage("tableOfContentsFinal"));
    }

    private void drawRules() {
        int x, y;
        String name;
        SourceCodeProperties codeProperties = new SourceCodeProperties();

        codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));

        // Coordinates:
        x = border_px;
        y = 620;

        Coordinates ruleCoordinates = new Coordinates(x, y);
        // prepare name
        name = "rules";

        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        String ruleString =
                translator.translateMessage("propertyHead")
                        + "\n \n"
                        + translator.translateMessage("property1")
                        + "\n"
                        + translator.translateMessage("property2")
                        + "\n"
                        + translator.translateMessage("property3")
                        + "\n"
                        + translator.translateMessage("property4")
                        + "\n"
                        + translator.translateMessage("property5")
                        + "\n"
                        + translator.translateMessage("property51");

        // draw x:

        SourceCode sc = lang.newSourceCode(ruleCoordinates, name, null, codeProperties);
        sc.addMultilineCode(ruleString, "_0", null);

        lang.newRect(new Offset(-5, -5, "rules", "NW"),
                new Offset(5, 5, "rules", "SE"),
                "rRect",
                null, rectProps);
    }

    private void drawNullNode() {

        TextProperties null_text_properties = new TextProperties();
        null_text_properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 14));

        lang.newText(new Coordinates(border_px * 2, border_px * 5),
                "T.null", "null_text", null, null_text_properties);

        CircleProperties null_circle_properties = new CircleProperties();
        null_circle_properties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        null_circle_properties.set(AnimationPropertiesKeys.FILL_PROPERTY, GRAPH_BLACK_COLOR);
        null_circle_properties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        lang.newCircle(new Offset(18, 10, "null_text", "NW"), 25,
                "null_circle", null, null_circle_properties);
    }

    private void drawNotInsertedVertices() {

        CircleProperties circleProps = new CircleProperties();
        circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        TextProperties vertexValueProps = new TextProperties();
        vertexValueProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 14));

        int value_x;

        int size = 20;

        String off = "hRect";

        for (int i = 0; i < deleteVertexValues.length; i++) {

            valuesCircles[i] = lang.newCircle(
                    new Offset(30, size, off, "NE"),
                    size,
                    "vertexCircle" + i,
                    null,
                    circleProps);

            String value = String.valueOf(deleteVertexValues[i]);
            int length = value.length();

            value_x = size - (length * 4);

            valuesText[i] = lang.newText(new Offset(value_x, size - 10, "vertexCircle" + i, "NW"),
                    value, value, null, vertexValueProps);

            off = "vertexCircle" + i;
        }
    }

    private void createInfoText() {
        // InfoText properties

        infoTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 14));

        // Introduction:
        TextProperties textProps = new TextProperties();
        TextProperties textPropsHeader = new TextProperties();

        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));
        
        textPropsHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 16));

        // -- INTRO 0 -- //
        lang.newText(new Coordinates(border_px, 100),
                translator.translateMessage("intro01"),
                "intro01", null, textPropsHeader);

        lang.newText(new Offset(0, 25, "intro01", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro02"),
                "intro02", null, textProps);

        lang.newText(new Offset(0, 25, "intro02", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro03"),
                "intro03", null, textProps);

        lang.newText(new Offset(0, 25, "intro03", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro04"),
                "intro04", null, textProps);

        lang.nextStep(translator.translateMessage("tableOfContentsStart")); 

        // -- INTRO 1 -- //
        lang.newText(new Offset(0, 50, "intro04", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro11"),
                        "intro11", null, textPropsHeader);

        lang.newText(new Offset(0, 25, "intro11", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro12"),
                        "intro12", null, textProps);

        lang.newText(new Offset(0, 25, "intro12", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro13"),
                        "intro13", null, textProps);

        lang.newText(new Offset(0, 25, "intro13", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro14"),
                        "intro14", null, textProps);

        lang.newText(new Offset(0, 25, "intro14", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro15"),
                        "intro15", null, textProps);

        lang.newText(new Offset(0, 25, "intro15", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro16"),
                        "intro16", null, textProps);

        lang.newText(new Offset(0, 25, "intro16", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro17"),
                        "intro17", null, textProps);

        lang.nextStep(translator.translateMessage("tableOfContentsStart")); 
        
        // -- INTRO 2 -- //
        lang.newText(new Offset(0, 50, "intro17", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro21"),
                        "intro21", null, textPropsHeader);

        lang.newText(new Offset(0, 25, "intro21", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro22"),
                        "intro22", null, textProps);

        lang.newText(new Offset(0, 25, "intro22", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro23"),
                        "intro23", null, textProps);

        lang.newText(new Offset(0, 25, "intro23", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro24"),
                        "intro24", null, textProps);

        lang.newText(new Offset(0, 25, "intro24", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro25"),
                        "intro25", null, textProps);

        lang.newText(new Offset(0, 25, "intro25", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro26"),
                        "intro26", null, textProps);

        lang.newText(new Offset(0, 25, "intro26", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro27"),
                        "intro27", null, textProps);

        lang.newText(new Offset(0, 25, "intro27", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro28"),
                        "intro28", null, textProps);

        lang.newText(new Offset(0, 25, "intro28", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro29"),
                        "intro29", null, textProps);

        lang.newText(new Offset(0, 25, "intro29", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro210"),
                        "intro210", null, textProps);
        
        lang.nextStep();
        
        lang.hideAllPrimitives();
        header.show();
        hRect.show();
        
        
        // -- INTRO 3 -- //
//        lang.newText(new Offset(0, 40, "intro210", AnimalScript.DIRECTION_NW),
                lang.newText(new Coordinates(border_px, 100),
                        translator.translateMessage("intro31"),
                        "intro31", null, textPropsHeader);

        lang.newText(new Offset(0, 25, "intro31", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro32"),
                        "intro32", null, textProps);

        lang.newText(new Offset(0, 25, "intro32", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro33"),
                        "intro33", null, textProps);

        lang.newText(new Offset(0, 25, "intro33", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro34"),
                        "intro34", null, textProps);

        lang.newText(new Offset(0, 25, "intro34", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro35"),
                        "intro35", null, textProps);

        lang.newText(new Offset(0, 25, "intro35", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro36"),
                        "intro36", null, textProps);
        
        lang.nextStep();
        
        // -- INTRO 4 -- //
        lang.newText(new Offset(0, 50, "intro36", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro41"),
                        "intro41", null, textPropsHeader);

        lang.newText(new Offset(0, 25, "intro41", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro42"),
                        "intro42", null, textProps);

        lang.newText(new Offset(0, 25, "intro42", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro43"),
                        "intro43", null, textProps);

        lang.newText(new Offset(0, 25, "intro43", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro44"),
                        "intro44", null, textProps);
        
        lang.nextStep();
        
        // -- INTRO 5 -- //
        lang.newText(new Offset(0, 50, "intro44", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro51"),
                        "intro51", null, textPropsHeader);

        lang.newText(new Offset(0, 25, "intro51", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro52"),
                        "intro52", null, textProps);

        lang.newText(new Offset(0, 25, "intro52", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro53"),
                        "intro53", null, textProps);

        lang.newText(new Offset(0, 25, "intro53", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro54"),
                        "intro54", null, textProps);

        lang.newText(new Offset(0, 25, "intro54", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro55"),
                        "intro55", null, textProps);

        lang.newText(new Offset(0, 25, "intro55", AnimalScript.DIRECTION_NW),
                        translator.translateMessage("intro56"),
                        "intro56", null, textProps);                

        lang.nextStep();
        
        lang.newText(new Offset(0, 50, "intro56", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro61"),
                "intro61", null, textPropsHeader);

        lang.newText(new Offset(0, 25, "intro61", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro62"),
                "intro62", null, textProps);

        lang.newText(new Offset(0, 25, "intro62", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro63"),
                "intro63", null, textProps);

        lang.newText(new Offset(0, 25, "intro63", AnimalScript.DIRECTION_NW),
                translator.translateMessage("intro64"),
                "intro64", null, textProps);
        
        lang.nextStep();
        lang.hideAllPrimitives();
        header.show();
        hRect.show();
    }

    private void drawPseudoCode() {

        boolean hidden = false;

        SourceCodeProperties sourceCode = (SourceCodeProperties) props
                .getPropertiesByName("sourceCode");

        hidden = (boolean) sourceCode.get("hidden");

        Color color = (Color) sourceCode.get("color");
        Color contextColor = (Color) sourceCode.get("contextColor");
        Color highlightColor = (Color) sourceCode.get("highlightColor");

        String name = ((Font) sourceCode.get("font")).getFamily();
        int style = Font.PLAIN;
        int size = 12;

        if ((boolean) sourceCode.get("bold")) {
            style = style ^ Font.BOLD;
        }

        if ((boolean) sourceCode.get("italic")) {
            style = style ^ Font.ITALIC;
        }

        size = (int) sourceCode.get("size");

        Font font = new Font(name, style, size);

        sourceCodePropsDELETE = new SourceCodeProperties();

        sourceCodePropsDELETE.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        sourceCodePropsDELETE.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        sourceCodePropsDELETE.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contextColor);
        sourceCodePropsDELETE.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightColor);

        srcDELETE = lang.newSourceCode(new Offset(-120, -220, "rRect", "NE"), "srcDELETE",
                null, sourceCodePropsDELETE);

        srcDELETE.addCodeLine("RB-DELETE(T, z)",
                "srcDELETE_0", 10, null);
        srcDELETE.addCodeLine("  if (z.left == T.null",
                "srcDELETE_1", 10, null);
        srcDELETE.addCodeLine("      || z.right == T.null)",
                "srcDELETE_2", 10, null);
        srcDELETE.addCodeLine("      y = z",
                "srcDELETE_3", 10, null);
        srcDELETE.addCodeLine("  else",
                "srcDELETE_4", 10, null);
        srcDELETE.addCodeLine("      y = TREE-SUCCESSOR(z)",
                "srcDELETE_5", 10, null);
        srcDELETE.addCodeLine("  y-original-color = y.color",
                "srcDELETE_51", 10, null);
        srcDELETE.addCodeLine("  if (y.left != T.null)",
                "srcDELETE_6", 10, null);
        srcDELETE.addCodeLine("      x = y.left",
                "srcDELETE_7", 10, null);
        srcDELETE.addCodeLine("  else",
                "srcDELETE_8", 10, null);
        srcDELETE.addCodeLine("      x = y.right",
                "srcDELETE_9", 10, null);
        srcDELETE.addCodeLine("  x.p = y.p",
                "srcDELETE_10", 10, null);
        srcDELETE.addCodeLine("  if (y.p == T.null)",
                "srcDELETE_11", 10, null);
        srcDELETE.addCodeLine("      T.root = x",
                "srcDELETE_12", 10, null);
        srcDELETE.addCodeLine("  else",
                "srcDELETE_13", 10, null);
        srcDELETE.addCodeLine("      if (y == y.p.left)",
                "srcDELETE_14", 10, null);
        srcDELETE.addCodeLine("          y.p.left = x",
                "srcDELETE_15", 10, null);
        srcDELETE.addCodeLine("      else",
                "srcDELETE_16", 10, null);
        srcDELETE.addCodeLine("          y.p.right = x",
                "srcDELETE_17", 10, null);
        srcDELETE.addCodeLine("  if (y != z)",
                "srcDELETE_18", 10, null);
        srcDELETE.addCodeLine("      z.value = y.value",
                "srcDELETE_19", 10, null);
        // srcDELETE.addCodeLine("      copy y satellite data",
        // "srcDELETE_20", 10, null);
        srcDELETE.addCodeLine("  if (y-original-color == BLACK)",
                "srcDELETE_21", 10, null);
        srcDELETE.addCodeLine("      RB-DELETE-FIXUP(T, x)",
                "srcDELETE_22", 10, null);
        // srcDELETE.addCodeLine("  return y",
        // "srcDELETE_23", 10, null);

        srcDELETE.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);

        // INSERT FIX
        // SourceCode Props init:
        sourceCodePropsDELETE_FIX = new SourceCodeProperties();

        sourceCodePropsDELETE_FIX.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        sourceCodePropsDELETE_FIX.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        sourceCodePropsDELETE_FIX.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contextColor);
        sourceCodePropsDELETE_FIX.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
                highlightColor);

        // SourceCode itself init
        srcDELETE_FIX = lang.newSourceCode(new Offset(-120, -320, "srcDELETE", "NE"),
                "srcDELETE_FIX",
                null, sourceCodePropsDELETE_FIX);

        srcDELETE_FIX.addCodeLine("RB-DELETE-FIXUP(T, x)",
                "SRC_DELETE_FIX_0", 10, null);
        srcDELETE_FIX.addCodeLine(" while (x != T.root",
                "SRC_DELETE_FIX_1", 10, null);
        srcDELETE_FIX.addCodeLine("        && x.color == BLACK)",
                "SRC_DELETE_FIX_2", 10, null);
        srcDELETE_FIX.addCodeLine("     if (x == x.p.left)",
                "SRC_DELETE_FIX_3", 10, null);
        srcDELETE_FIX.addCodeLine("         w = x.p.right",
                "SRC_DELETE_FIX_4", 10, null);
        srcDELETE_FIX.addCodeLine("         if (w.color == RED)",
                "SRC_DELETE_FIX_5", 10, null);
        srcDELETE_FIX.addCodeLine("             w.color = BLACK",
                "SRC_DELETE_FIX_6", 10, null);
        srcDELETE_FIX.addCodeLine("             x.p.color = RED",
                "SRC_DELETE_FIX_7", 10, null);
        srcDELETE_FIX.addCodeLine("             LEFT-ROTATE(T, x.p)",
                "SRC_DELETE_FIX_8", 10, null);
        srcDELETE_FIX.addCodeLine("             w = x.p.right",
                "SRC_DELETE_FIX_9", 10, null);
        srcDELETE_FIX.addCodeLine("         if (w.left.color == BLACK",
                "SRC_DELETE_FIX_10", 10, null);
        srcDELETE_FIX.addCodeLine("             && w.right.color == BLACK)",
                "SRC_DELETE_FIX_11", 10, null);
        srcDELETE_FIX.addCodeLine("             w.color = RED",
                "SRC_DELETE_FIX_12", 10, null);
        srcDELETE_FIX.addCodeLine("             x = x.p",
                "SRC_DELETE_FIX_13", 10, null);
        srcDELETE_FIX.addCodeLine("         else",
                "SRC_DELETE_FIX_14", 10, null);
        srcDELETE_FIX.addCodeLine("             if (w.right.color == BLACK)",
                "SRC_DELETE_FIX_15", 10, null);
        srcDELETE_FIX.addCodeLine("                 w.left.color = BLACK",
                "SRC_DELETE_FIX_16", 10, null);
        srcDELETE_FIX.addCodeLine("                 w.color = RED",
                "SRC_DELETE_FIX_17", 10, null);
        srcDELETE_FIX.addCodeLine("                 RIGHT-ROTATE(T, w)",
                "SRC_DELETE_FIX_18", 10, null);
        srcDELETE_FIX.addCodeLine("                 w = x.p.right",
                "SRC_DELETE_FIX_19", 10, null);
        srcDELETE_FIX.addCodeLine("             w.color = x.p.color",
                "SRC_DELETE_FIX_20", 10, null);
        srcDELETE_FIX.addCodeLine("             x.p.color = BLACK",
                "SRC_DELETE_FIX_21", 10, null);
        srcDELETE_FIX.addCodeLine("             w.right.color = BLACK",
                "SRC_DELETE_FIX_22", 10, null);
        srcDELETE_FIX.addCodeLine("             LEFT-ROTATE(T, x.p)",
                "SRC_DELETE_FIX_23", 10, null);
        srcDELETE_FIX.addCodeLine("             x = T.root",
                "SRC_DELETE_FIX_24", 10, null);
        srcDELETE_FIX.addCodeLine("     else",
                "SRC_DELETE_FIX_25", 10, null);
        srcDELETE_FIX.addCodeLine("         w = x.p.left",
                "SRC_DELETE_FIX_26", 10, null);
        srcDELETE_FIX.addCodeLine("         if (w.color == RED)",
                "SRC_DELETE_FIX_27", 10, null);
        srcDELETE_FIX.addCodeLine("             w.color = BLACK",
                "SRC_DELETE_FIX_28", 10, null);
        srcDELETE_FIX.addCodeLine("             x.p.color = RED",
                "SRC_DELETE_FIX_29", 10, null);
        srcDELETE_FIX.addCodeLine("             RIGHT-ROTATE(T, x.p)",
                "SRC_DELETE_FIX_30", 10, null);
        srcDELETE_FIX.addCodeLine("             w = x.p.left",
                "SRC_DELETE_FIX_31", 10, null);
        srcDELETE_FIX.addCodeLine("         if (w.right.color == BLACK",
                "SRC_DELETE_FIX_32", 10, null);
        srcDELETE_FIX.addCodeLine("             && w.left.color == BLACK)",
                "SRC_DELETE_FIX_33", 10, null);
        srcDELETE_FIX.addCodeLine("             w.color = RED",
                "SRC_DELETE_FIX_34", 10, null);
        srcDELETE_FIX.addCodeLine("             x = x.p",
                "SRC_DELETE_FIX_35", 10, null);
        srcDELETE_FIX.addCodeLine("         else",
                "SRC_DELETE_FIX_36", 10, null);
        srcDELETE_FIX.addCodeLine("             if (w.left.color == BLACK)",
                "SRC_DELETE_FIX_37", 10, null);
        srcDELETE_FIX.addCodeLine("                 w.right.color = BLACK",
                "SRC_DELETE_FIX_38", 10, null);
        srcDELETE_FIX.addCodeLine("                 w.color = RED",
                "SRC_DELETE_FIX_39", 10, null);
        srcDELETE_FIX.addCodeLine("                 LEFT-ROTATE(T, w)",
                "SRC_DELETE_FIX_40", 10, null);
        srcDELETE_FIX.addCodeLine("                 w = x.p.left",
                "SRC_DELETE_FIX_41", 10, null);
        srcDELETE_FIX.addCodeLine("             w.color = x.p.color",
                "SRC_DELETE_FIX_42", 10, null);
        srcDELETE_FIX.addCodeLine("             x.p.color = BLACK",
                "SRC_DELETE_FIX_43", 10, null);
        srcDELETE_FIX.addCodeLine("             w.left.color = BLACK",
                "SRC_DELETE_FIX_44", 10, null);
        srcDELETE_FIX.addCodeLine("             RIGHT-ROTATE(T, x.p)",
                "SRC_DELETE_FIX_45", 10, null);
        srcDELETE_FIX.addCodeLine("             x = T.root",
                "SRC_DELETE_FIX_46", 10, null);
        srcDELETE_FIX.addCodeLine(" x.color = BLACK",
                "SRC_DELETE_FIX_47", 10, null);

        srcDELETE_FIX.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

        // RIGHT ROTATE:
        // SourceCode Props init:
        sourceCodePropsRightRotate = new SourceCodeProperties();

        sourceCodePropsRightRotate.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        sourceCodePropsRightRotate.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        sourceCodePropsRightRotate.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contextColor);
        sourceCodePropsRightRotate.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
                highlightColor);

        // SourceCode itself init
        srcRightRotate = lang.newSourceCode(new Offset(-120, 0, "srcDELETE_FIX", "NE"),
                "srcRightRotate",
                null, sourceCodePropsRightRotate);

        srcRightRotate.addCodeLine("RIGHT-ROTATE(T, x)", // 0
                "srcRightRotate_0", 10, null);
        srcRightRotate.addCodeLine("    y = x.left", // 1
                "srcRightRotate_1", 10, null);
        srcRightRotate.addCodeLine("    x.left = y.right", // 2
                "srcRightRotate_2", 10, null);
        srcRightRotate.addCodeLine("    if (y.right != T.null)", // 3
                "srcRightRotate_3", 10, null);
        srcRightRotate.addCodeLine("        y.right.p = x", // 4
                "srcRightRotate_4", 10, null);
        srcRightRotate.addCodeLine("    y.p = x.p", // 5
                "srcRightRotate_5", 10, null);
        srcRightRotate.addCodeLine("    if (x.p == T.null)", // 6
                "srcRightRotate_6", 10, null);
        srcRightRotate.addCodeLine("        T.root = y", // 7
                "srcRightRotate_7", 10, null);
        srcRightRotate.addCodeLine("    else", // 8
                "srcRightRotate_8", 10, null); // if(y.p.left = y)
        srcRightRotate.addCodeLine("        if (x == x.p.right)", // 9
                "srcRightRotate_9", 10, null);
        srcRightRotate.addCodeLine("            x.p.right = y", // 10
                "srcRightRotate_10", 10, null);
        srcRightRotate.addCodeLine("        else", // 11
                "srcRightRotate_11", 10, null);
        srcRightRotate.addCodeLine("            x.p.left = y", // 12
                "srcRightRotate_12", 10, null);
        srcRightRotate.addCodeLine("    y.right = x", // 13
                "srcRightRotate_13", 10, null);
        srcRightRotate.addCodeLine("    x.p = y", // 14
                "srcRightRotate_14", 10, null);

        srcRightRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

        // LEFT ROTATE
        // SourceCode Props init:
        sourceCodePropsLeftRotate = new SourceCodeProperties();

        sourceCodePropsLeftRotate.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        sourceCodePropsLeftRotate.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        sourceCodePropsLeftRotate.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contextColor);
        sourceCodePropsLeftRotate.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
                highlightColor);

        // SourceCode itself init
        srcLeftRotate = lang.newSourceCode(new Offset(-120, 370, "srcDELETE_FIX", "NE"),
                "srcLeftRotate",
                null, sourceCodePropsLeftRotate);

        srcLeftRotate.addCodeLine("LEFT-ROTATE(T, x)", // 0
                "srcLeftRotate_0", 10, null);
        srcLeftRotate.addCodeLine("    y = x.right", // 1
                "srcLeftRotate_1", 10, null);
        srcLeftRotate.addCodeLine("    x.right = y.left", // 2
                "srcLeftRotate_2", 10, null);
        srcLeftRotate.addCodeLine("    if (y.left != T.null)", // 3
                "srcLeftRotate_3", 10, null);
        srcLeftRotate.addCodeLine("    y.left.p = x", // 4
                "srcLeftRotate_4", 10, null);
        srcLeftRotate.addCodeLine("    y.p = x.p", // 5
                "srcLeftRotate_5", 10, null);
        srcLeftRotate.addCodeLine("        if (x.p == T.null)", // 6
                "srcLeftRotate_6", 10, null);
        srcLeftRotate.addCodeLine("    T.root = y", // 7
                "srcLeftRotate_7", 10, null);
        srcLeftRotate.addCodeLine("        else", // 8
                "srcLeftRotate_8", 10, null);
        srcLeftRotate.addCodeLine("            if (x == x.p.left)", // 9
                "srcLeftRotate_9", 10, null);
        srcLeftRotate.addCodeLine("        x.p.left = y", // 10
                "srcLeftRotate_10", 10, null);
        srcLeftRotate.addCodeLine("            else", // 11
                "srcLeftRotate_11", 10, null);
        srcLeftRotate.addCodeLine("    x.p.right = y", // 12
                "srcLeftRotate_12", 10, null);
        srcLeftRotate.addCodeLine("    y.left = x", // 13
                "srcLeftRotate_13", 10, null);
        srcLeftRotate.addCodeLine("    x.p = y", // 14
                "srcLeftRotate_14", 10, null);

        srcLeftRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

        if (hidden) {
            srcDELETE.hide();
            srcDELETE_FIX.hide();
            srcRightRotate.hide();
            srcLeftRotate.hide();
        }

    }

    private void createTitle() {
        // Title: text
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));
        header = lang.newText(new Coordinates(border_px, border_px * 2),
                translator.translateMessage("algorithmName"),
                "header", null, headerProps);

        // Title: rectangle
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        hRect = lang.newRect(new Offset(-5, -5, "header",
                AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, rectProps);
    }

    public String getName() {
        return translator.translateMessage("algorithmName");
    }

    public String getAlgorithmName() {
        return translator.translateMessage("algorithmName");
    }

    public String getAnimationAuthor() {
        return "Maciej Aleksander Mokwinski + Julian Harbarth";
    }

    public String getDescription() {
        return translator.translateMessage("descriptionLine1")
                + "\n"
                + translator.translateMessage("descriptionLine2")
                + "\n\n"
                + translator.translateMessage("descriptionLine3")
                + "\n\n"
                + translator.translateMessage("descriptionLine4")
                + "\n\n"
                + translator.translateMessage("propertyHead")
                + "\n"
                + translator.translateMessage("property1")
                + "\n"
                + translator.translateMessage("property2")
                + "\n"
                + translator.translateMessage("property3")
                + "\n"
                + translator.translateMessage("property4")
                + "\n"
                + translator.translateMessage("property5")
                + translator.translateMessage("property51");
    }

    public String getCodeExample() {
        return "RB-DELETE(T, z)"
                + "\n"
                + "  if (z.left = T.null || z.right == T.null)"
                + "\n"
                + "      y = z"
                + "\n"
                + "  else"
                + "\n"
                + "      y = TREE-SUCCESSOR(z)"
                + "\n"
                + "  if (y.left != T.null)"
                + "\n"
                + "      x = y.left"
                + "\n"
                + "  else"
                + "\n"
                + "      x = y.right"
                + "\n"
                + "  x.p = y.p"
                + "\n"
                + "  if (y.p == T.null)"
                + "\n"
                + "      T.root = x"
                + "\n"
                + "  else"
                + "\n"
                + "      if (y == y.p.left)"
                + "\n"
                + "          y.p.left = x"
                + "\n"
                + "      else"
                + "\n"
                + "          y.p.right = x"
                + "\n"
                + "  if (y != z)"
                + "\n"
                + "      z.value = y.value"
                + "\n"
                + "      copy y satellite data"
                + "\n"
                + "  if (y.color == BLACK)"
                + "\n"
                + "      RB-DELETE-FIXUP(T, x)"
                + "\n"
                + "   return y";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return alocale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    /**
     * updates (or draws if it's null) the graph this method bases on the global
     * defined ArrayList vertices which contains all the inserted vertices
     */
    public static void updateGraph() {
        Vertex_DELETE v;
        int i = 0;

        if (graph != null) {
            graph.hide();

            for (i = 0; i < vertices.size(); i++) {
                v = vertices.get(i);
                if (v.isDeleted())
                    vertices.remove(i);
            }

            i = 0;

            // update vertices' Index
            for (Vertex_DELETE vv : vertices) {
                vv.setIndexVertex(i);
                i++;
            }

        }

        amoundOfInsertedVertices = vertices.size();

        if (amoundOfInsertedVertices > 0) {
            // Adjacency list update
            int[][] adj = updateAdjacencyMatrix();

            // Koordinates update
            Node[] n = updateCoordinates();

            // prepare labels
            String[] labels = updateLabels();

            // create default Graph
            // draw the Graph
            makeGraph(adj, n, labels);

            // Highlight black vertices
            highlightBlackVertices();

        }
    }

    public static int amoundOfInsertedVertices = 0;

    /**
     * puts the children of the inserted vertices on its adjacency list, doing
     * this the conection (vertex) line will be drawn in the animal Animation
     */
    public static int[][] updateAdjacencyMatrix() {
        int i = 0;
        int leftChildVertexIndex = -1;
        int rightChildVertexIndex = -1;
        int[] list;
        int[][] adjMatrix = new int[amoundOfInsertedVertices][amoundOfInsertedVertices];

        for (Vertex_DELETE n : vertices) {
            if (n != null) {
                // create new adj List, that has
                n.initAdjList(amoundOfInsertedVertices);
                leftChildVertexIndex = n.getLeftChild().getVertexIndex();
                rightChildVertexIndex = n.getRightChild().getVertexIndex();

                if (leftChildVertexIndex != -1)
                    n.setChildInAdjacencyList(leftChildVertexIndex);

                if (rightChildVertexIndex != -1)
                    n.setChildInAdjacencyList(rightChildVertexIndex);

                list = n.getAdjacencyList();

                for (int j = 0; j < list.length; j++) {
                    adjMatrix[i][j] = list[j];
                }

            }

            i++;
        }

        return adjMatrix;
    }

    public static Node[] updateCoordinates() {
        int x = 385, y = 100;

        int depth = 0;
        Vertex_DELETE n;
        Vertex_DELETE parent;
        int stage = 0;
        Node[] coordinates = new Node[amoundOfInsertedVertices];
        t.root.setVertexCoordinates(x, y);

        for (int k = 0; k < 7; k++) {

            for (int i = 0; i < amoundOfInsertedVertices; i++) {
                depth = 0;
                n = vertices.get(i);
                if (t.root == n) {
                    coordinates[i] = n.getVertexCoordinates();
                    continue;
                }
                // figure out the depth of the vertex n
                depth = t.getDepth(t.root, n.getValue(), depth);

                if (depth == 1 + stage) {
                    parent = n.getParent();

                    y = parent.getY() + 50;
                    if (n == parent.getLeftChild()) { // n is left child
                        x = parent.getX() - offsetsGraph[depth - 1];
                        n.setVertexCoordinates(x, y);
                    } else { // else n is the right child
                        x = parent.getX() + offsetsGraph[depth - 1];
                        n.setVertexCoordinates(x, y);
                    }
                    coordinates[i] = n.getVertexCoordinates();

                } else
                    continue;
            }
            stage++;
        }

        return coordinates;
    }

    public static String[] updateLabels() {
        String[] labels = new String[amoundOfInsertedVertices];

        for (int i = 0; i < amoundOfInsertedVertices; i++) {

            labels[i] = vertices.get(i).getVertexLabel();

        }

        return labels;
    }

    public static void highlightBlackVertices() {
        Node[] vertex = new Node[1];

        if (graph == null)
            return;

        for (Vertex_DELETE n : vertices) {

            if (n.getColor() == 'b') {
                vertex[0] = graph.getNode(n.getVertexIndex());

                graph.highlightNode(vertex[0], null, null);
            } else {
                vertex[0] = graph.getNode(
                        n.getVertexIndex());

                graph.unhighlightNode(vertex[0],
                        null, null);

            }
        }

    }

    /**
     * init a graph-primitive and return it
     * 
     * @param n
     * @param adj
     * @return Graph - initialised graph primitive
     */
    public static void makeGraph(int[][] adj, Node[] n, String[] labels) {
        // GRAPH

        GraphProperties graphProps = new GraphProperties();
        graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
        graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.DARK_GRAY);

        getDefaultGraph(graphProps, adj, n, labels);
    }

    public static int updateCounter = 0;

    public static void getDefaultGraph(GraphProperties graphProps, int[][] adj, Node[] n,
            String[] givenLabels) {

        // define the edges of the graph
        int[][] graphAdjacencyMatrix = new int[amoundOfInsertedVertices][amoundOfInsertedVertices];
        graphAdjacencyMatrix = adj;

        // define the vertices and their positions
        Node[] graphVertices = new Node[amoundOfInsertedVertices];

        graphVertices = n;

        // define the text of the vertices
        String[] labels = new String[amoundOfInsertedVertices];

        labels = givenLabels;

        StringBuffer sb = new StringBuffer();
        sb.delete(0, sb.length());
        sb.append("graph").append(graphCounter);
        graphCounter++;

        graph = lang.newGraph(sb.toString(), graphAdjacencyMatrix, graphVertices, labels, null,
                graphProps);

    }

}