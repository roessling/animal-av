package generators.tree;

import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.variables.VariableRoles;

/**
 * Klasse representiert die Datenstruktur "RB-Baum", welche wiederum eine
 * Sturkturierte Menge der Knoten (Vertices) ist
 * 
 * @author matze
 *
 */
public class RB_TREES_INSERT_SRC {

    Language                lang;
    private Translator      translator;
    private int             border_px;

    protected Vertex_INSERT root                   = null;
    private final char      RED                    = 'r';
    private final char      BLACK                  = 'b';
    private int             insertedVertices       = 0;
    private int             formerInsertedVertices = 0;

    // Insert Knoten:
    private Vertex_INSERT   x_node;
    private Vertex_INSERT   y_node;
    private Vertex_INSERT   z_node;

    // -- x y z Pointer -- //
    private Text            x_pointer;
    private Text            y_pointer;
    private Text            z_pointer;

    private TextProperties  pointer_text_properties;
    // -- x y z Pointer -- //

    // -- INFO BOX -- //
    private TextProperties  info_text_properties;
    private TextProperties  info_text_header_properties;

    private Text            info_text_general_information;
    private Text            info_text_specific_line_information;
    // private Text info_text_case_three;

    // -- INFO BOX -- //

    private int             questionProbability;
    Random                  randomGenerator        = new Random();

    private Variables       vars;

    private SourceCode      srcINSERT;
    private SourceCode      srcINSERT_FIX;
    private SourceCode      srcLeftRotate;
    private SourceCode      srcRightRotate;

    private Color           notActiveColor;
    private Color           hightlightColor;
    private Color           contextColor;
    private Color           followColor;

    protected RB_TREES_INSERT_SRC(Language l, Translator tr, int borpx, TextProperties infoprops,
            SourceCode srcI, SourceCode srcIF, SourceCode srcLR, SourceCode srcRR,
            Color nAC, Variables v, int qb) {
        translator = tr;
        lang = l;
        border_px = borpx;
        info_text_properties = infoprops;

        srcINSERT = srcI;
        srcINSERT_FIX = srcIF;
        srcLeftRotate = srcLR;
        srcRightRotate = srcRR;
        this.questionProbability = qb;

        this.vars = v;

        // -- Setting up Colors -- //
        notActiveColor = nAC;
        hightlightColor = (Color) srcINSERT_FIX.getProperties().get(
                AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
        contextColor = (Color) srcINSERT_FIX.getProperties().get(
                AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY);
        followColor = (Color) srcINSERT_FIX.getProperties().get(
                AnimationPropertiesKeys.COLOR_PROPERTY);
        // -- Setting up Colors -- //

        // -- Setting up pointers -- //
        pointer_text_properties = new TextProperties();
        pointer_text_properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 14));

        x_pointer = lang.newText(new Coordinates(0, 0), "", "x_pointer",
                null, pointer_text_properties);

        y_pointer = lang.newText(new Coordinates(0, 0), "", "y_pointer",
                null, pointer_text_properties);

        z_pointer = lang.newText(new Coordinates(0, 0), "", "z_pointer",
                null, pointer_text_properties);

        hide_x();
        hide_y();
        hide_z();
        // -- Setting up info texts -- //

        info_text_header_properties = new TextProperties();
        info_text_header_properties.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font(Font.SANS_SERIF, Font.BOLD, 15));

        createInfoBox();

        createGeneralInformation(translator.translateMessage("allgemeineInformationen"));
        createSpecificLineInformation(translator.translateMessage("spezifischeInformationen"));

    }

    /**
     * inserts the given Vertex into the RB-Tree
     * 
     * @param srcINSERT
     * @param lang
     * @param v
     * @return if the insertion was successful
     */
    protected boolean insert(Vertex_INSERT z) {

        insertWindow();

        srcINSERT.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);
        srcINSERT_FIX.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);
        srcRightRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);
        srcLeftRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

        z_node = z;

        srcINSERT.highlight(0);
        showGeneralInformation("generalInformationInsert");
        showSpecificInformation("specificInformationInsert0");

        langNextStepInsert();

        // -- STEP -- //

        y_node = null;

        srcINSERT.highlight(1);
        showSpecificInformation("specificInformationInsert1");

        langNextStepInsert();

        // -- STEP -- //

        x_node = this.root;

        srcINSERT.toggleHighlight(1, 2);
        showSpecificInformation("specificInformationInsert2");

        langNextStepInsert();

        // -- STEP -- //
        srcINSERT.toggleHighlight(2, 3);
        if (x_node == null || x_node.getValue() == null) {
            showSpecificInformation("specificInformationInsert3False");
        } else {
            showSpecificInformation("specificInformationInsert3True");
        }

        langNextStepInsert();

        // -- STEP -- //

        while (x_node != null
                && x_node.getValue() != null) {

            y_node = x_node;

            srcINSERT.toggleHighlight(3, 4);
            showSpecificInformation("specificInformationInsert4");

            langNextStepInsert();

            // -- STEP -- //

            srcINSERT.toggleHighlight(4, 5);
            if (z_node.getValue() < x_node.getValue()) {
                showSpecificInformation("specificInformationInsert5True");
            } else {
                showSpecificInformation("specificInformationInsert5False");
            }

            langNextStepInsert();

            // -- STEP -- //

            if (z_node.getValue() < x_node.getValue()) {

                x_node = x_node.getLeftChild();

                srcINSERT.toggleHighlight(5, 6);

                langNextStepInsert();
                srcINSERT.toggleHighlight(6, 3);

            } else {

                srcINSERT.toggleHighlight(5, 7);
                // langNextStep();

                x_node = x_node.getRightChild();

                srcINSERT.toggleHighlight(7, 8);

                langNextStepInsert();
                srcINSERT.toggleHighlight(8, 3);
            }

            if (x_node == null || x_node.getValue() == null) {
                showSpecificInformation("specificInformationInsert3False");
            } else {
                showSpecificInformation("specificInformationInsert3True");
            }
            langNextStepInsert();

        }

        z_node.setParent(y_node);
        srcINSERT.toggleHighlight(3, 9);
        showSpecificInformation("specificInformationInsert9");

        langNextStepInsert();

        // -- STEP -- //

        srcINSERT.toggleHighlight(9, 10);

        if (y_node == null) {
            showSpecificInformation("specificInformationInsert10True");
        } else {
            showSpecificInformation("specificInformationInsert10False");
        }

        langNextStepInsert();

        if (y_node == null) {
            this.root = z_node;
            srcINSERT.toggleHighlight(10, 11);
            insertNode();
            z_node.setColor(RED);

            insertedVertices++;
            addViolationQuestion();

            langNextStepInsert();
            srcINSERT.toggleHighlight(11, 17);
        } else {

            srcINSERT.toggleHighlight(10, 12);
            // langNextStep();
            srcINSERT.toggleHighlight(12, 13);
            if (z_node.getValue() < y_node.getValue()) {
                showSpecificInformation("specificInformationInsert13True");
            } else {
                showSpecificInformation("specificInformationInsert13False");
            }

            langNextStepInsert();

            // -- STEP -- //

            if (z_node.getValue() < y_node.getValue()) {

                y_node.setLeftChild(z_node);
                insertNode();
                z_node.setColor(RED);

                insertedVertices++;
                addViolationQuestion();

                srcINSERT.toggleHighlight(13, 14);
                showSpecificInformation("specificInformationInsert14");

                langNextStepInsert();

                // -- STEP -- //

                srcINSERT.toggleHighlight(14, 17);
            } else {
                srcINSERT.toggleHighlight(13, 15);
                // langNextStep();

                y_node.setRightChild(z_node);
                insertNode();
                z_node.setColor(RED);

                insertedVertices++;
                addViolationQuestion();

                srcINSERT.toggleHighlight(15, 16);
                showSpecificInformation("specificInformationInsert16");

                langNextStepInsert();

                // -- STEP -- //

                srcINSERT.toggleHighlight(16, 17);
            }
        }

        z_node.setColor(RED);

        RB_TREES_INSERT.updateGraph();

        insertFixup(z_node);

        showGeneralInformation("generalInformationReturnToInsert");
        showSpecificInformation("specificInformation20");

        langNextStepInsert();

        showGeneralInformation("-");
        showSpecificInformation("-");

        srcINSERT.unhighlight(0);
        srcINSERT.unhighlight(17);

        srcINSERT.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);
        hide_pointers();

        formerInsertedVertices++;

        return true;
    }

    private void insertWindow() {
        vars.declare("string", "x", "null", animal.variables.Variable.getRoleString(VariableRoles.WALKER));
        vars.declare("string", "y", "null",animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
        vars.declare("string", "z", "null",animal.variables.Variable.getRoleString(VariableRoles.WALKER));
        vars.declare("string", "z.p","null", animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
        
        
        vars.discard("z.p.p");

        x_pointer.setText("x", null, null);
        y_pointer.setText("y", null, null);
        z_pointer.setText("z", null, null);
        
        y_node = null;
        x_node = null;
    }

    private void langNextStepInsert() {

        if (x_node != null) {
            vars.set("x", String.valueOf(x_node.getValue()));
        } else {
            vars.set("x", "null");
        }

        if (y_node != null) {
            vars.set("y", String.valueOf(y_node.getValue()));
        } else {
            vars.set("y", "null");
        }

        if (z_node != null) {
            vars.set("z", String.valueOf(z_node.getValue()));
        } else {
            vars.set("z", "null");
        }

        if (z_node.getParent() != null) {
            vars.set("z.p", String.valueOf(z_node.getParent().getValue()));
        } else {
            vars.set("z.p", "null");
        }

        draw_pointer_x();
        draw_pointer_y();
        draw_pointer_z();

        lang.nextStep();
    }

    private void fixupWindow() {
        vars.declare("string", "x", "null", animal.variables.Variable.getRoleString(VariableRoles.WALKER));
        vars.declare("string", "y", "null",animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
        vars.declare("string", "z", "null",animal.variables.Variable.getRoleString(VariableRoles.WALKER));
        vars.declare("string", "z.p","null", animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
        vars.declare("string", "z.p.p","null", animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));

        x_pointer.setText("x", null, null);
        y_pointer.setText("y", null, null);
        z_pointer.setText("z", null, null);
    }

    private void langNextStepFixup() {
        if (x_node != null) {
            vars.set("x", String.valueOf(x_node.getValue()));
        } else {
            vars.set("x", "null");
        }

        if (y_node != null) {
            vars.set("y", String.valueOf(y_node.getValue()));
        } else {
            vars.set("y", "null");
        }

        if (z_node != null) {
            vars.set("z", String.valueOf(z_node.getValue()));
        } else {
            vars.set("z", "null");
        }

        if (z_node.getParent() != null) {
            vars.set("z.p", String.valueOf(z_node.getParent().getValue()));
        } else {
            vars.set("z.p", "null");
        }

        if (z_node != null && z_node.getParent() != null &&
                z_node.getParent().getParent() != null && z_node.getParent().getParent() != null) {
            vars.set("z.p.p", String.valueOf(z_node.getParent().getParent().getValue()));
        } else {
            vars.set("z.p.p", "null");
        }

        draw_pointer_x();
        draw_pointer_y();
        draw_pointer_z();

        lang.nextStep();
    }

    private void rotateWindow() {
        vars.declare("string", "x", "null", animal.variables.Variable.getRoleString(VariableRoles.WALKER));
        vars.declare("string", "y", "null",animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
        
        vars.discard("z");
        vars.discard("z.p");
        vars.discard("z.p.p");

        x_pointer.setText("x", null, null);
        y_pointer.setText("y", null, null);

        hide_z();
    }

    private void langNextStepRotate() {
        if (x_node != null) {
            vars.set("x", String.valueOf(x_node.getValue()));
        } else {
            vars.set("x", "null");
        }

        if (y_node != null) {
            vars.set("y", String.valueOf(y_node.getValue()));
        } else {
            vars.set("y", "null");
        }

        draw_pointer_x();
        draw_pointer_y();

        lang.nextStep();
    }

    private void insertFixup(Vertex_INSERT z) {

        fixupWindow();

        z_node = z;

        // Highlight the whole RightRotate pseudo code in the context color
        srcINSERT_FIX.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);

        srcINSERT_FIX.highlight(0);

        showGeneralInformation("generalInformationFixupCall", getViolation());
        showSpecificInformation("specificInformation200");

        langNextStepFixup();

        // -- STEP -- //

        srcINSERT_FIX.highlight(1);
        if (z_node != this.root && z_node.getParent().getColor() == RED) {
            showSpecificInformation("specificInformationFixup1True");
        } else {
            showSpecificInformation("specificInformationFixup1False");
        }

        langNextStepFixup();

        // -- STEP -- //

        while (z_node != this.root &&
                z_node.getParent().getColor() == RED) {

            srcINSERT_FIX.toggleHighlight(1, 2);
            if (z_node.getParent() == z_node.getParent().getParent().getLeftChild()) {
                showSpecificInformation("specificInformationFixup2True");
            } else {
                showSpecificInformation("specificInformationFixup2False");
            }

            langNextStepFixup();

            // -- STEP -- //

            if (z_node.getParent() == z_node.getParent().getParent().getLeftChild()) {

                y_node = z_node.getParent().getParent().getRightChild();

                srcINSERT_FIX.toggleHighlight(2, 3);
                showSpecificInformation("specificInformationFixup3");

                langNextStepFixup();

                // -- STEP -- //

                srcINSERT_FIX.toggleHighlight(3, 4);
                if (y_node != null && y_node.getColor() == RED) {
                    showSpecificInformation("specificInformationFixup4True");
                    showGeneralInformation("generalInformationFixupCase1");
                } else {
                    showSpecificInformation("specificInformationFixup4False");
                }
                
                langNextStepFixup();

                // -- STEP -- //

                // case 1
                if (y_node != null && y_node.getColor() == RED) {

                    z_node.getParent().setColor(BLACK);

                    srcINSERT_FIX.toggleHighlight(4, 5);
                    showSpecificInformation("specificInformationFixup5");
                    showGeneralInformation("generalInformationFixupCase11");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    y_node.setColor(BLACK);

                    srcINSERT_FIX.toggleHighlight(5, 6);
                    showSpecificInformation("specificInformationFixup6");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    z_node.getParent().getParent().setColor(RED);

                    srcINSERT_FIX.toggleHighlight(6, 7);
                    showSpecificInformation("specificInformationFixup7");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    z_node = z_node.getParent().getParent();

                    srcINSERT_FIX.toggleHighlight(7, 8);
                    showSpecificInformation("specificInformationFixup8");
                    showGeneralInformation("generalInformationFixupCase12");

                    langNextStepFixup();

                    // -- STEP -- //

                    srcINSERT_FIX.toggleHighlight(8, 1);
                    if (z_node != this.root && z_node.getParent().getColor() == RED) {
                        showSpecificInformation("specificInformationFixup1True");
                    } else {
                        showSpecificInformation("specificInformationFixup1False");
                    }

                } else {
                    srcINSERT_FIX.toggleHighlight(4, 9);
                    // langNextStepFixup();

                    srcINSERT_FIX.toggleHighlight(9, 10);
                    if (z_node == z_node.getParent().getRightChild()) {
                        showSpecificInformation("specificInformationFixup10True");
                        showGeneralInformation("generalInformationFixupCase2");
                    } else {
                        showSpecificInformation("specificInformationFixup10False");
                        showGeneralInformation("generalInformationFixupCase3");
                    }

                    langNextStepFixup();

                    // -- STEP -- //

                    // case 2
                    if (z_node == z_node.getParent().getRightChild()) {

                        z_node = z_node.getParent();

                        srcINSERT_FIX.toggleHighlight(10, 11);
                        showSpecificInformation("specificInformationFixup11");
                        showGeneralInformation("generalInformationFixupCase21");

                        langNextStepFixup();

                        // -- STEP -- //

                        srcINSERT_FIX.toggleHighlight(11, 12);
                        showSpecificInformation("specificInformationFixup12");
                        showGeneralInformation("generalInformationFixupCase22");

                        langNextStepFixup();

                        // -- STEP -- //

                        rotateLeft(z_node);

                        langNextStepFixup();

                        // -- STEP -- //

                        srcINSERT_FIX.toggleHighlight(12, 13);

                    } else {
                        srcINSERT_FIX.toggleHighlight(10, 13);
                    }

                    z_node.getParent().setColor(BLACK);

                    // case 3

                    showSpecificInformation("specificInformationFixup13");
                    showGeneralInformation("generalInformationFixupCase31");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    z_node.getParent().getParent().setColor(RED);

                    srcINSERT_FIX.toggleHighlight(13, 14);
                    showSpecificInformation("specificInformationFixup14");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    srcINSERT_FIX.toggleHighlight(14, 15);
                    showSpecificInformation("specificInformationFixup15");
                    showGeneralInformation("generalInformationFixupCase32");

                    langNextStepFixup();

                    // -- STEP -- //

                    rotateRight(z_node.getParent().getParent());

                    srcINSERT_FIX.toggleHighlight(15, 1);
                    if (z_node != this.root && z_node.getParent().getColor() == RED) {
                        showSpecificInformation("specificInformationFixup1True");
                    } else {
                        showSpecificInformation("specificInformationFixup1False");
                    }

                    langNextStepFixup();

                    // -- STEP -- //

                }
            } else {
                srcINSERT_FIX.toggleHighlight(2, 16);

                y_node = z_node.getParent().getParent().getLeftChild();

                srcINSERT_FIX.toggleHighlight(16, 17);
                showSpecificInformation("specificInformationFixup17");

                langNextStepFixup();

                // -- STEP -- //

                srcINSERT_FIX.toggleHighlight(17, 18);

                if (y_node != null && y_node.getColor() == RED) {
                    showSpecificInformation("specificInformationFixup18True");
                    showGeneralInformation("generalInformationFixupCase4");
                } else {
                    showSpecificInformation("specificInformationFixup18False");
                }

                langNextStepFixup();

                // -- STEP -- //

                // case 4
                if (y_node != null &&
                        y_node.getColor() == RED) {

                    z_node.getParent().setColor(BLACK);

                    srcINSERT_FIX.toggleHighlight(18, 19);
                    showSpecificInformation("specificInformationFixup19");
                    showGeneralInformation("generalInformationFixupCase41");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    y_node.setColor(BLACK);

                    srcINSERT_FIX.toggleHighlight(19, 20);
                    showSpecificInformation("specificInformationFixup20");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    z_node.getParent().getParent().setColor(RED);

                    srcINSERT_FIX.toggleHighlight(20, 21);
                    showSpecificInformation("specificInformationFixup21");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    z_node = z_node.getParent().getParent();

                    srcINSERT_FIX.toggleHighlight(21, 22);
                    showSpecificInformation("specificInformationFixup22");
                    showGeneralInformation("generalInformationFixupCase42");

                    langNextStepFixup();

                    // -- STEP -- //

                    srcINSERT_FIX.toggleHighlight(22, 1);
                    if (z_node != this.root && z_node.getParent().getColor() == RED) {
                        showSpecificInformation("specificInformationFixup1True");
                       
                    } else {
                        showSpecificInformation("specificInformationFixup1False");
                        
                    }

                    langNextStepFixup();

                    // -- STEP -- //

                } else {
                    srcINSERT_FIX.toggleHighlight(18, 23);

                    srcINSERT_FIX.toggleHighlight(23, 24);
                    if (z_node == z_node.getParent().getLeftChild()) {
                        showSpecificInformation("specificInformationFixup24True");
                        showGeneralInformation("generalInformationFixupCase5");
                    } else {
                        showSpecificInformation("specificInformationFixup24False");
                        showGeneralInformation("generalInformationFixupCase6");
                    }

                    langNextStepFixup();

                    // -- STEP -- //

                    // case 5
                    if (z_node == z_node.getParent().getLeftChild()) {

                        z_node = z_node.getParent();

                        srcINSERT_FIX.toggleHighlight(24, 25);
                        showSpecificInformation("specificInformationFixup25");
                        showGeneralInformation("generalInformationFixupCase51");

                        langNextStepFixup();

                        // -- STEP -- //

                        srcINSERT_FIX.toggleHighlight(25, 26);
                        showSpecificInformation("specificInformationFixup26");
                        showGeneralInformation("generalInformationFixupCase52");

                        langNextStepFixup();

                        // -- STEP -- //

                        rotateRight(z_node);

                        langNextStepFixup();

                        // -- STEP -- //

                        srcINSERT_FIX.toggleHighlight(26, 27);
                    } else {
                        srcINSERT_FIX.toggleHighlight(24, 27);
                    }

                    // case 6

                    z_node.getParent().setColor(BLACK);

                    showSpecificInformation("specificInformationFixup27");
                    showGeneralInformation("generalInformationFixupCase61");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    z_node.getParent().getParent().setColor(RED);

                    srcINSERT_FIX.toggleHighlight(27, 28);
                    showSpecificInformation("specificInformationFixup28");
                    RB_TREES_INSERT.updateGraph();

                    langNextStepFixup();

                    // -- STEP -- //

                    showSpecificInformation("specificInformationFixup29");
                    showGeneralInformation("generalInformationFixupCase62");
                    srcINSERT_FIX.toggleHighlight(28, 29);

                    langNextStepFixup();

                    // -- STEP -- //

                    rotateLeft(z_node.getParent().getParent());

                    srcINSERT_FIX.toggleHighlight(29, 1);
                    if (z_node != this.root && z_node.getParent().getColor() == RED) {
                        showSpecificInformation("specificInformationFixup1True");
                    } else {
                        showSpecificInformation("specificInformationFixup1False");
                    }

                    langNextStepFixup();

                    // -- STEP -- //

                }
            }
        }

        this.root.setColor(BLACK);

        srcINSERT_FIX.toggleHighlight(1, 30);
        RB_TREES_INSERT.updateGraph();
        showSpecificInformation("specificInformationFixup30");

        langNextStepFixup();

        srcINSERT_FIX.unhighlight(0);
        srcINSERT_FIX.unhighlight(30);

        // Unhighlight the whole pseudo code back to the normal color
        srcINSERT_FIX.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

    }

    private Vertex_INSERT getNullVertex() {
        return new Vertex_INSERT(null);

    }

    private void rotateLeft(Vertex_INSERT x) {

        rotateWindow();

        // Highlight the whole RightRotate pseudo code in the context color
        srcLeftRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);

        x_node = x;

        srcLeftRotate.highlight(0);
        showGeneralInformation("generalInformationLeftRotate0");
        showSpecificInformation("specificInformationLeftRotate0");

        langNextStepRotate();

        // -- STEP -- //

        y_node = x_node.getRightChild(); // x=1, y=2

        srcLeftRotate.highlight(1);
        showSpecificInformation("specificInformationLeftRotate1");

        langNextStepRotate();

        // -- STEP -- //

        x_node.setRightChild(y_node.getLeftChild());

        srcLeftRotate.toggleHighlight(1, 2);
        showSpecificInformation("specificInformationLeftRotate2");
        langNextStepRotate();

        // -- STEP -- //

        srcLeftRotate.toggleHighlight(2, 3);
        if (y_node.getLeftChild() != null && y_node.getRightChild().getValue() != null) {
            showSpecificInformation("specificInformationLeftRotate3True");
        } else {
            showSpecificInformation("specificInformationLeftRotate3False");
        }

        langNextStepRotate();

        if (y_node.getLeftChild() != null && y_node.getRightChild().getValue() != null) {

            y_node.getLeftChild().setParent(x_node);

            srcLeftRotate.toggleHighlight(3, 4);
            showSpecificInformation("specificInformationLeftRotate4");

            langNextStepRotate();

            // -- STEP -- //

            srcLeftRotate.toggleHighlight(4, 5);
        } else {
            srcLeftRotate.toggleHighlight(3, 5);
        }

        y_node.setParent(x_node.getParent());

        showSpecificInformation("specificInformationLeftRotate5");

        langNextStepRotate();

        // -- STEP -- //

        srcLeftRotate.toggleHighlight(5, 6);
        if (x_node.getParent() == null || x_node.getValue() == null) {
            showSpecificInformation("specificInformationLeftRotate6True");
        } else {
            showSpecificInformation("specificInformationLeftRotate6False");
        }

        langNextStepRotate();

        // -- STEP -- //

        if (x_node.getParent() == null || x_node.getValue() == null) {
            this.root = y_node;

            srcLeftRotate.toggleHighlight(6, 7);
            showSpecificInformation("specificInformationLeftRotate7");

            langNextStepRotate();

            // -- STEP -- //

            srcLeftRotate.toggleHighlight(7, 13);
        } else {
            srcLeftRotate.toggleHighlight(6, 8);
            srcLeftRotate.toggleHighlight(8, 9);
            if (x_node == x_node.getParent().getLeftChild()) {
                showSpecificInformation("specificInformationLeftRotate9True");
            } else {
                showSpecificInformation("specificInformationLeftRotate9False");
            }

            langNextStepRotate();

            // -- STEP -- //

            if (x_node == x_node.getParent().getLeftChild()) {

                x_node.getParent().setLeftChild(y_node);

                srcLeftRotate.toggleHighlight(9, 10);
                showSpecificInformation("specificInformationLeftRotate10");

                langNextStepRotate();

                // -- STEP -- //

                srcLeftRotate.toggleHighlight(10, 13);
            } else {
                x_node.getParent().setRightChild(y_node);

                srcLeftRotate.toggleHighlight(9, 11);
                srcLeftRotate.toggleHighlight(11, 12);
                showSpecificInformation("specificInformationLeftRotate12");
                langNextStepRotate();

                // -- STEP -- //

                srcLeftRotate.toggleHighlight(12, 13);

            }
        }

        y_node.setLeftChild(x_node);

        showSpecificInformation("specificInformationLeftRotate13");

        langNextStepRotate();

        // -- STEP -- //

        x_node.setParent(y_node);

        RB_TREES_INSERT.updateGraph();
        srcLeftRotate.toggleHighlight(13, 14);
        showSpecificInformation("specificInformationLeftRotate14");

        langNextStepRotate();

        // -- STEP -- //

        srcLeftRotate.unhighlight(14);

        showSpecificInformation("specificInformationLeftRotateLast");
        showGeneralInformation("generalInformationLeftRotateLast");

        langNextStepRotate();

        srcLeftRotate.unhighlight(0);

        // Unhighlight the whole pseudo code back to the normal color
        srcLeftRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

    }

    private void rotateRight(Vertex_INSERT x) {

        rotateWindow();

        // Highlight the whole RightRotate pseudo code in the context color
        srcRightRotate
                .changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);

        x_node = x;

        srcRightRotate.highlight(0);
        showGeneralInformation("generalInformationRightRotate0");
        showSpecificInformation("specificInformationRightRotate0");

        langNextStepRotate();

        // -- STEP -- //

        y_node = x_node.getLeftChild();

        srcRightRotate.highlight(1);
        showSpecificInformation("specificInformationRightRotate1");

        langNextStepRotate();

        // -- STEP -- //

        x_node.setLeftChild(y_node.getRightChild());

        srcRightRotate.toggleHighlight(1, 2);
        showSpecificInformation("specificInformationRightRotate2");
        langNextStepRotate();

        // -- STEP -- //

        srcRightRotate.toggleHighlight(2, 3);
        if (y_node.getRightChild() != null && y_node.getRightChild().getValue() != null) {
            showSpecificInformation("specificInformationRightRotate3True");
        } else {
            showSpecificInformation("specificInformationRightRotate3False");
        }

        langNextStepRotate();

        if (y_node.getRightChild() != null && y_node.getRightChild().getValue() != null) {

            y_node.getRightChild().setParent(x_node);

            srcRightRotate.toggleHighlight(3, 4);
            showSpecificInformation("specificInformationRightRotate4");

            langNextStepRotate();

            // -- STEP -- //

            srcRightRotate.toggleHighlight(4, 5);
        } else {
            srcRightRotate.toggleHighlight(3, 5);
        }

        y_node.setParent(x_node.getParent());

        showSpecificInformation("specificInformationRightRotate5");

        langNextStepRotate();

        // -- STEP -- //

        srcRightRotate.toggleHighlight(5, 6);
        if (x_node.getParent() == null || x_node.getValue() == null) {
            showSpecificInformation("specificInformationRightRotate6True");
        } else {
            showSpecificInformation("specificInformationRightRotate6False");
        }

        langNextStepRotate();

        // -- STEP -- //

        if (x_node.getParent() == null || x_node.getValue() == null) {
            this.root = y_node;

            srcRightRotate.toggleHighlight(6, 7);
            showSpecificInformation("specificInformationRightRotate7");

            langNextStepRotate();

            // -- STEP -- //

            srcRightRotate.toggleHighlight(7, 13);
        } else {
            srcRightRotate.toggleHighlight(6, 8);
            srcRightRotate.toggleHighlight(8, 9);
            if (x_node == x_node.getParent().getRightChild()) {
                showSpecificInformation("specificInformationRightRotate9True");
            } else {
                showSpecificInformation("specificInformationRightRotate9False");
            }

            langNextStepRotate();

            // -- STEP -- //

            if (x_node == x_node.getParent().getRightChild()) {

                x_node.getParent().setRightChild(y_node);

                srcRightRotate.toggleHighlight(9, 10);
                showSpecificInformation("specificInformationRightRotate10");

                langNextStepRotate();

                // -- STEP -- //

                srcRightRotate.toggleHighlight(10, 13);
            } else {
                y_node.getParent().setLeftChild(y_node);

                srcRightRotate.toggleHighlight(9, 11);
                srcRightRotate.toggleHighlight(11, 12);
                showSpecificInformation("specificInformationRightRotate12");

                langNextStepRotate();

                // -- STEP -- //

                srcRightRotate.toggleHighlight(12, 13);

            }
        }

        y_node.setRightChild(x_node);

        showSpecificInformation("specificInformationRightRotate13");

        langNextStepRotate();

        // -- STEP -- //

        x_node.setParent(y_node);

        RB_TREES_INSERT.updateGraph();
        srcRightRotate.toggleHighlight(13, 14);
        showSpecificInformation("specificInformationRightRotate14");

        langNextStepRotate();

        // -- STEP -- //

        srcRightRotate.unhighlight(14);

        showSpecificInformation("specificInformationRightRotateLast");
        showGeneralInformation("generalInformationRightRotateLast");

        langNextStepRotate();

        srcRightRotate.unhighlight(0);

        // Unhighlight the whole pseudo code back to the normal color
        srcRightRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);
    }

    private String getViolation() {
        if (root.getColor() != 'b') {
            return translator.translateMessage("die2");
        }

        for (Vertex_INSERT n : RB_TREES_INSERT.vertices) {
            if (n.getColor() == 'r') {
                if (n.getLeftChild().getColor() != 'b' || n.getRightChild().getColor() != 'b') {
                    return translator.translateMessage("die4");
                }
            }
        }
        return translator.translateMessage("keine");
    }

    private void addViolationQuestion() {

        int random = randomGenerator.nextInt(100);
        if (random <= questionProbability) {

            MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel("violation"
                    + insertedVertices);

            mcqm.setPrompt(translator.translateMessage("questionWhichViolation"));
            String answer = getViolation();

            if (answer.equals(translator.translateMessage("keine"))) {
                mcqm.addAnswer(
                        "1.",
                        0,
                        translator.translateMessage("questionWrongAnswer",
                                translator.translateMessage("none")));
                mcqm.addAnswer(
                        "2.",
                        0,
                        translator.translateMessage("questionWrongAnswer",
                                translator.translateMessage("none")));
                mcqm.addAnswer(
                        "3.",
                        0,
                        translator.translateMessage("questionWrongAnswer",
                                translator.translateMessage("none")));
                mcqm.addAnswer(
                        "4.",
                        0,
                        translator.translateMessage("questionWrongAnswer",
                                translator.translateMessage("none")));
                mcqm.addAnswer(
                        "5",
                        0,
                        translator.translateMessage("questionWrongAnswer",
                                translator.translateMessage("none")));
                mcqm.addAnswer(
                        translator.translateMessage("none"),
                        1,
                        translator.translateMessage("questionCorrectAnswer",
                                translator.translateMessage("none")));

            }

            if (answer.equals(translator.translateMessage("die2"))) {
                mcqm.addAnswer("1.", 0, translator.translateMessage("questionWrongAnswer", "2."));
                mcqm.addAnswer(
                        "2.",
                        1,
                        translator.translateMessage("questionCorrectAnswer",
                                translator.translateMessage("d2")));
                mcqm.addAnswer("3.", 0, translator.translateMessage("questionWrongAnswer", "2."));
                mcqm.addAnswer("4.", 0, translator.translateMessage("questionWrongAnswer", "2."));
                mcqm.addAnswer("5.", 0, translator.translateMessage("questionWrongAnswer", "2."));
                mcqm.addAnswer(translator.translateMessage("none"), 0,
                        translator.translateMessage("questionWrongAnswer", "2."));

            }

            if (answer.equals(translator.translateMessage("die4"))) {
                mcqm.addAnswer("1.", 0, translator.translateMessage("questionWrongAnswer", "4."));
                mcqm.addAnswer("2.", 0, translator.translateMessage("questionWrongAnswer", "4."));
                mcqm.addAnswer("3.", 0, translator.translateMessage("questionWrongAnswer", "4."));
                mcqm.addAnswer(
                        "4.",
                        1,
                        translator.translateMessage("questionCorrectAnswer",
                                translator.translateMessage("d4")));
                mcqm.addAnswer("5.", 0, translator.translateMessage("questionWrongAnswer", "4."));
                mcqm.addAnswer(translator.translateMessage("none"), 0,
                        translator.translateMessage("questionWrongAnswer", "4."));
            }

            lang.addMCQuestion(mcqm);
        }
    }

    /**
     * calculates the depth of the given node method functions similar to
     * seach-method
     * 
     * @param v
     *            - node, which's depth we want to find out
     * @return depth of the given node (-1 if not found)
     */
    protected int getDepth(Vertex_INSERT x, int key, int depth) {
        if (x == null)
            return -1;
        else if (x.getValue() == null)
            return -1;
        else if (key == x.getValue())
            return depth;

        if (key < x.getValue())
            return getDepth(x.getLeftChild(), key, depth + 1);
        else
            return getDepth(x.getRightChild(), key, depth + 1);

    }

    private void createGeneralInformation(String testString) {

        lang.newText(new Offset(5, 5, "infoRect2", "NW"),
                translator.translateMessage("generalInformation"),
                "general_information_header", null, info_text_header_properties);

        info_text_general_information = lang.newText(
                new Offset(0, 0, "general_information_header", "SW"),
                testString,
                "general_information", null, info_text_properties);
    }

    private void createSpecificLineInformation(String testString) {

        lang.newText(new Offset(0, 10, "general_information", "SW"),
                translator.translateMessage("specificInformation"),
                "specific_information_header", null, info_text_header_properties);

        info_text_specific_line_information = lang.newText(
                new Offset(0, 0, "specific_information_header", "SW"),
                testString,
                "specific_line_information", null, info_text_properties);
    }

    private void showGeneralInformation(String message) {
        info_text_general_information.setText(translator.translateMessage(message),
                null, null);
    }

    private void showGeneralInformation(String message, String param) {
        info_text_general_information.setText(translator.translateMessage(message, param),
                null, null);
    }

    private void showSpecificInformation(String message) {
        info_text_specific_line_information.setText(translator.translateMessage(message),
                null, null);
    }

    private void createInfoBox() {

        // Info Texts:
        // Title "Info-Box:"
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 16));
        lang.newText(new Coordinates(border_px, 500), "Info-Box:",
                "infoHeader", null, headerProps);

        // Info-Box Rectangle:
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        lang.newRect(new Offset(-5, -5, "infoHeader",
                AnimalScript.DIRECTION_NW),
                new Offset(5, 5, "infoHeader", "SE"), 
                "infoRect",
                null, rectProps);

        RectProperties rectProps2 = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        lang.newRect(new Offset(0, 0, "infoRect", "SW"),
                new Offset(0, 0, "rRect", "NE"),
                "infoRect2",
                null, rectProps2);

    }

    private void hide_x() {
        x_pointer.setText("", null, null);
    }

    private void hide_y() {
        y_pointer.setText("", null, null);
    }

    private void hide_z() {
        z_pointer.setText("", null, null);
    }

    private void draw_pointer_x() {
        int x, y;

        hide_x();

        // update Coordinates:
        if (x_node != null
                && x_node.getValue() != null) {

            x = x_node.getX() - 15;
            y = x_node.getY() - 15;

            Coordinates x_InsertCoordinates = new Coordinates(x, y);

            // draw x:
            x_pointer = lang.newText(x_InsertCoordinates, "x", "x_pointer", null,
                    pointer_text_properties);

        } else {
            x = 60;
            y = -2;

            x_pointer = lang.newText(new Offset(x, y, "null_text", "NW"), "x", "x_pointer", null,
                    pointer_text_properties);
        }
    }

    /**
     * draw y, near the vertex that is actually y
     */
    private void draw_pointer_y() {
        int x, y;

        hide_y();

        if (y_node != null && y_node.getValue() != null) {

            x = y_node.getX() + 16;
            y = y_node.getY() - 15;

            if (String.valueOf(y_node.getValue()).length() >= 2) {
                x = x + 10;
            }

            Coordinates y_InsertCoordinates = new Coordinates(x, y);
            y_pointer = lang.newText(y_InsertCoordinates, "y", "y_pointer", null,
                    pointer_text_properties);

        } else {
            x = 50;
            y = -2;

            y_pointer = lang.newText(new Offset(x, y, "null_text", "NW"), "y", "y_pointer", null,
                    pointer_text_properties);

        }
    }

    /**
     * draw z, near the vertex that is actually z
     */
    private void draw_pointer_z() {
        int x, y;

        hide_z();

        if (insertedVertices != formerInsertedVertices) {
            x = z_node.getX() - 15;
            y = z_node.getY() + 15;

            Coordinates z_InsertCoordinates = new Coordinates(x, y);

            z_pointer = lang.newText(z_InsertCoordinates, "z", "z_pointer", null,
                    pointer_text_properties);

        } else {
            z_pointer = lang.newText(new Offset(16, 38, "vertexCircle"
                    + insertedVertices, "NW"), "z", "z_pointer", null, pointer_text_properties);

        }
    }

    private void insertNode() {

        z_node.setLeftChild(getNullVertex());
        z_node.getLeftChild().setColor(BLACK);
        z_node.getLeftChild().setParent(z_node);

        z_node.setRightChild(getNullVertex());
        z_node.getRightChild().setColor(BLACK);
        z_node.getRightChild().setParent(z_node);

        RB_TREES_INSERT.updateGraph();
    }

    protected void hide_pointers() {
        y_pointer.setText("", null, null);
        x_pointer.setText("", null, null);
        z_pointer.setText("", null, null);
        
    }

}
