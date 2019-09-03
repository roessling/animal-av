package generators.tree;

import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
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

public class RB_TREES_DELETE_SRC {

    public Vertex_DELETE   root  = null;
    // Symbols of the colors:
    private char           RED   = 'r';
    private char           BLACK = 'b';
    private char WHITE = 'w';
    private Language       lang;
    
    String  violation = null;
    
    // Insert Knoten:
    private Vertex_DELETE  x_node;
    private Vertex_DELETE  y_node;
    private Vertex_DELETE  z_node;
    private Vertex_DELETE  w_node;

    // -- x y z Pointer -- //
    private Text           x_pointer;
    private Text           y_pointer;
    private Text           z_pointer;
    private Text           w_pointer;

    private TextProperties pointer_text_properties;
    // -- x y z Pointer -- //
    
    private Variables       vars;
    private Rect                           originalColorRect;
    
    private Color                          GRAPH_RED_COLOR    = Color.RED;
    private int                            black_value        = 84;
    private Color                          GRAPH_BLACK_COLOR  = new Color(black_value,
                                                                      black_value,
                                                                      black_value);

    // -- INFO BOX -- //
    private TextProperties info_text_properties;
    private TextProperties info_text_header_properties;

    private Text           info_text_general_information;
    private Text           info_text_specific_line_information;
    // private Text info_text_case_three;

    // -- INFO BOX -- //

    // -- SOURCE CODE -- //
    private SourceCode     srcDELETE;
    private SourceCode     srcDELETE_FIX;
    private SourceCode     srcLeftRotate;
    private SourceCode     srcRightRotate;

    private Color          notActiveColor;
    private Color          hightlightColor;
    private Color          contextColor;
    private Color          followColor;
    // -- SOURCE CODE -- //

    private int            deletedVertices;
    private Translator     translator;
    private int            border_px;
    private int           questionProbability;
    
    private Random  randomGenerator = new Random();

    protected RB_TREES_DELETE_SRC(Language l, Translator tr, int borpx, TextProperties infoprops,
            SourceCode srcI, SourceCode srcIF, SourceCode srcLR, SourceCode srcRR,
            Color nAC, Variables v, int qp) {
        translator = tr;
        lang = l;
        border_px = borpx;
        info_text_properties = infoprops;

        srcDELETE = srcI;
        srcDELETE_FIX = srcIF;
        srcLeftRotate = srcLR;
        srcRightRotate = srcRR;

        vars = v;
        
        questionProbability = qp;
        
        deletedVertices = 0;

        // -- Setting up Colors -- //
        notActiveColor = nAC;
        hightlightColor = (Color) srcDELETE_FIX.getProperties().get(
                AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
        contextColor = (Color) srcDELETE_FIX.getProperties().get(
                AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY);
        followColor = (Color) srcDELETE_FIX.getProperties().get(
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

        w_pointer = lang.newText(new Coordinates(0, 0), "", "w_pointer",
                null, pointer_text_properties);
        
        // -- Setting up info texts -- //

        info_text_header_properties = new TextProperties();
        info_text_header_properties.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font(Font.SANS_SERIF, Font.BOLD, 15));

        createInfoBox();
        drawOriginalColor();

        createGeneralInformation(translator.translateMessage("allgemeineInformationen"));
        createSpecificLineInformation(translator.translateMessage("spezifischeInformationen"));

    }

    /**
     * inserts the given Vertex into the RB-Tree
     * 
     * @param v
     * @return if the insertion was successful
     */
    public boolean insert(Vertex_DELETE z) {

        Vertex_DELETE y = null;
        Vertex_DELETE x = this.root;

        while (x != null
                && x.getValue() != null) {
            y = x;
            if (z.getValue() < x.getValue())
                x = x.getLeftChild();
            else
                x = x.getRightChild();
        }

        z.setParent(y);

        if (y == null)
            this.root = z;
        else if (z.getValue() < y.getValue())
            y.setLeftChild(z);
        else
            y.setRightChild(z);

        // leafs has also children, which has value null
        // children of the leafs are not null (their value is null)
        // this is for the purpouse, that also childeren of leaf have some
        // important information, you want to find out
        // this could be color or the parent
        z.setLeftChild(getNullVertex());
        z.getLeftChild().setColor(BLACK);
        z.getLeftChild().setParent(z);
        z.setRightChild(getNullVertex());
        z.getRightChild().setColor(BLACK);
        z.getRightChild().setParent(z);
        z.setColor(RED);

        insertFixup(z);

        return true;
    }

    public void insertFixup(Vertex_DELETE z) {
        Vertex_DELETE y;

        while (z != this.root &&
                z.getParent().getColor() == RED) {
            if (z.getParent() == z.getParent().getParent().getLeftChild()) {
                y = z.getParent().getParent().getRightChild();
                if (y != null && y.getColor() == RED) {
                    z.getParent().setColor(BLACK);
                    y.setColor(BLACK);
                    z.getParent().getParent().setColor(RED);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getRightChild()) {
                        z = z.getParent();
                        rotateLeftWithoutSteps(z);
                    }
                    z.getParent().setColor(BLACK);
                    z.getParent().getParent().setColor(RED);
                    rotateRightWithoutSteps(z.getParent().getParent());
                }
            } else {
                y = z.getParent().getParent().getLeftChild();
                if (y != null &&
                        y.getColor() == RED) {
                    z.getParent().setColor(BLACK);
                    y.setColor(BLACK);
                    z.getParent().getParent().setColor(RED);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getLeftChild()) {
                        z = z.getParent();
                        rotateRightWithoutSteps(z);
                    }
                    z.getParent().setColor(BLACK);
                    z.getParent().getParent().setColor(RED);
                    rotateLeftWithoutSteps(z.getParent().getParent());
                }
            }
        }
        this.root.setColor(BLACK);
    }

    /**
     * leafs has also children, which has value null children of the leafs are
     * not null (their value is null) this is for the purpouse, that also
     * childeren of leaf have some important information, you want to find out
     * this could be color or the parent
     * 
     * @return Vertex_DELETE
     */
    private Vertex_DELETE getNullVertex() {
        return new Vertex_DELETE(null);

    }

    public void rotateLeftWithoutSteps(Vertex_DELETE x) {
        Vertex_DELETE y = x.getRightChild(); // x=1, y=2
        x.setRightChild(y.getLeftChild());
        if (y.getLeftChild() != null)
            y.getLeftChild().setParent(x);

        y.setParent(x.getParent());

        if (x.getParent() == null)
            this.root = y;
        else if (x == x.getParent().getLeftChild()) {
            x.getParent().setLeftChild(y);
        } else
            x.getParent().setRightChild(y);
        y.setLeftChild(x);
        x.setParent(y);
    }

    private void rotateLeft(Vertex_DELETE x) {

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

        RB_TREES_DELETE.updateGraph();
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

    private void rotateRight(Vertex_DELETE x) {

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

        RB_TREES_DELETE.updateGraph();
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

    public void rotateRightWithoutSteps(Vertex_DELETE y) {

        Vertex_DELETE x = y.getLeftChild();

        y.setLeftChild(x.getRightChild());

        if (x.getRightChild() != null)
            x.getRightChild().setParent(y);

        x.setParent(y.getParent());

        if (y.getParent() == null)
            this.root = x;
        else if (y.getParent().getLeftChild() == y)
            y.getParent().setLeftChild(x);
        else
            y.getParent().setRightChild(x);

        x.setRightChild(y);
        y.setParent(x);
    }

    /**
     * removes the vertex with the given value (if there is one)
     * 
     * @param lang
     * @param value
     *            of the Vertex that should be removed
     * @return if the removal was successful or not
     */
    protected Vertex_DELETE delete(Vertex_DELETE z) {

        deleteWindow();
        
        z_node = z;
        
        srcDELETE.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);
        srcDELETE_FIX.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);
        srcRightRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);
        srcLeftRotate.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, notActiveColor, null,
                null);

//        Vertex_DELETE y_node = null, x_node = null;

        srcDELETE.highlight(0);
        showGeneralInformation("generalInformationDelete0");
        showSpecificInformation("specificInformationDelete0");

        langNextStepDelete();

        // -- STEP -- //
        
        if (z_node.getLeftChild().getValue() == null || z_node.getRightChild().getValue() == null) {
            showSpecificInformation("specificInformationDelete1True");
        } else {
            showSpecificInformation("specificInformationDelete1False");
        }

        srcDELETE.highlight(1);
        srcDELETE.highlight(2);

        langNextStepDelete();

        // -- STEP -- //

        if (z_node.getLeftChild().getValue() == null ||
                z_node.getRightChild().getValue() == null) {
           
            y_node = z_node;

            srcDELETE.unhighlight(1);
            srcDELETE.toggleHighlight(2, 3);
            showSpecificInformation("specificInformationDelete3");

            langNextStepDelete();

            // -- STEP -- //

            srcDELETE.toggleHighlight(3, 6);
        } else {
            y_node = treeSuccessor(z_node);
            
            srcDELETE.unhighlight(1);
            srcDELETE.toggleHighlight(2, 4);
            srcDELETE.toggleHighlight(4, 5);
            showSpecificInformation("specificInformationDelete5");

            langNextStepDelete();

            // -- STEP -- //

            srcDELETE.toggleHighlight(5, 6);
        }

        char y_original_color = y_node.getColor();
        
        fillOriginalColor(y_original_color);
        
        showSpecificInformation("specificInformationDelete6");
        langNextStepDelete();

        // -- STEP -- //
        
        srcDELETE.toggleHighlight(6, 7);
        
        if (y_node.getLeftChild().getValue() != null) {
            showSpecificInformation("specificInformationDelete7True");
        } else {
            showSpecificInformation("specificInformationDelete7False");
        }
        
        showGeneralInformation("generalInformationDelete1");
        
        langNextStepDelete();

        // -- STEP -- //
        
        // we mark y as deleted, so that the updateGraph Method can remove it
        // from the list of verticies "vertices" and
        // y will not appear in the next animation's step
        y_node.setDeleted(true);

        if (y_node.getLeftChild().getValue() != null) {
            x_node = y_node.getLeftChild();

            srcDELETE.toggleHighlight(7, 8);
            showSpecificInformation("specificInformationDelete8");

            langNextStepDelete();

            // -- STEP -- //

            srcDELETE.toggleHighlight(8, 11);
        } else {
            x_node = y_node.getRightChild();

            srcDELETE.toggleHighlight(7, 9);
            srcDELETE.toggleHighlight(9, 10);
            showSpecificInformation("specificInformationDelete10");

            langNextStepDelete();

            // -- STEP -- //

            srcDELETE.toggleHighlight(10, 11);
        }

        
        x_node.setParent(y_node.getParent());
        
        showSpecificInformation("specificInformationDelete11");
        showGeneralInformation("generalInformationDelete2");

        langNextStepDelete();

        // -- STEP -- //

        srcDELETE.toggleHighlight(11, 12);
        if (y_node.getParent() == null) {
            showSpecificInformation("specificInformationDelete12True");
        } else {
            showSpecificInformation("specificInformationDelete12False");
        }

        langNextStepDelete();

        // -- STEP -- //

        if (y_node.getParent() == null) {
            this.root = x_node;

            srcDELETE.toggleHighlight(12, 13);
            showSpecificInformation("specificInformationDelete13");

            langNextStepDelete();

            // -- STEP -- //

            srcDELETE.toggleHighlight(13, 19);
        } else {

            srcDELETE.toggleHighlight(12, 14);
            srcDELETE.toggleHighlight(14, 15);
            if (y_node == y_node.getParent().getLeftChild()) {
                showSpecificInformation("specificInformationDelete15True");
            } else {
                showSpecificInformation("specificInformationDelete15False");
            }

            langNextStepDelete();

            // -- STEP -- //

            if (y_node == y_node.getParent().getLeftChild()) {
              
                y_node.getParent().setLeftChild(x_node);

                srcDELETE.toggleHighlight(15, 16);
                showSpecificInformation("specificInformationDelete16");

                langNextStepDelete();

                // -- STEP -- //

                srcDELETE.toggleHighlight(16, 19);
            }
            else {
                y_node.getParent().setRightChild(x_node);

                srcDELETE.toggleHighlight(15, 17);
                srcDELETE.toggleHighlight(17, 18);
                showSpecificInformation("specificInformationDelete18");

                langNextStepDelete();

                // -- STEP -- //

                srcDELETE.toggleHighlight(18, 19);
            }
        }
        
        if (y_node != z_node) {
            showSpecificInformation("specificInformationDelete19True");
        } else {
            showSpecificInformation("specificInformationDelete19False");
        }
        
        violation = getViolation(x_node, y_original_color);
        addViolationQuestion(y_original_color);

        langNextStepDelete();

        // -- STEP -- //

        if (y_node != z_node) {

            z_node.setValue(y_node.getValue()); // und Daten von y nach z kopieren
            
            
            
            y_node = null;
            RB_TREES_DELETE.updateGraph();
            srcDELETE.toggleHighlight(19, 20);
            showSpecificInformation("specificInformationDelete20");
            
            if(violation.equals("1")) {
                showGeneralInformation("generalInformationDeleteViolation", translator.translateMessage("die1"));
            }
            
            if(violation.equals("2")) {
                showGeneralInformation("generalInformationDeleteViolation", translator.translateMessage("die2"));
            }

            if(violation.equals("4")) {
                showGeneralInformation("generalInformationDeleteViolation", translator.translateMessage("die4"));
            }
            
            if(violation.equals("none")) {
                showGeneralInformation("generalInformationDeleteViolation", translator.translateMessage("none"));
            }
            
            langNextStepDelete();

            // -- STEP -- //

            srcDELETE.toggleHighlight(20, 21);
        } else {
            srcDELETE.toggleHighlight(19, 21);
            z_node = null;
        }
        
        y_node = null;
       
        
        if(violation.equals("1")) {
            showGeneralInformation("generalInformationDeleteViolation", translator.translateMessage("die1"));
        }
        
        if(violation.equals("2")) {
            showGeneralInformation("generalInformationDeleteViolation", translator.translateMessage("die2"));
        }

        if(violation.equals("4")) {
            showGeneralInformation("generalInformationDeleteViolation", translator.translateMessage("die4"));
        }
        
        if(violation.equals("none")) {
            showGeneralInformation("generalInformationDeleteViolation", translator.translateMessage("none"));
        }
        
        RB_TREES_DELETE.updateGraph();
        
        if(y_original_color == BLACK) {
            showSpecificInformation("specificInformationDelete21True");
            System.out.println("VIOLATION: " + getViolation(x_node, y_original_color));
        } else {
            showSpecificInformation("specificInformationDelete21False");
        }
        
        langNextStepDelete();
        
        // -- STEP -- //
        
        if (y_original_color == BLACK) {

            srcDELETE.toggleHighlight(21, 22);
            showSpecificInformation("specificInformationDelete22");
           
            deleteFixup(x_node);
            
//            langNextStepDelete();
            
            srcDELETE.unhighlight(22);

        } else {
            srcDELETE.unhighlight(21);
        }
        
        srcDELETE.unhighlight(0);
        
        if(y_original_color == BLACK) {
            showGeneralInformation("generalInformationDeleteLast");
        } else {
            showGeneralInformation("generalInformationDelete3");
        }
        showSpecificInformation("specificInformationDeleteLast");
       
        srcDELETE.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);
        
        fillOriginalColor(WHITE);
        hide_all();

        deletedVertices++;
        return y_node;
    }

    /**
     * returns the successor of the given Vertex x
     * 
     * @param x
     * @return the succesor of the Vertex x
     */
    private Vertex_DELETE treeSuccessor(Vertex_DELETE x) {

        if (x.getRightChild().getValue() != null) {
            return treeMinimum(x.getRightChild());

        }

        Vertex_DELETE y = x.getParent();

        while (y != null && x == y.getRightChild()) {
            x = y;
            y = y.getParent();
        }

        return y;
    }

    /**
     * return the minimum in the (part-) tree with the root x
     * 
     * @param x
     *            - root
     * @return minimum
     */
    private Vertex_DELETE treeMinimum(Vertex_DELETE x) {

        while (x.getLeftChild().getValue() != null) {
            x = x.getLeftChild();
        }

        return x;
    }

    /**
     * fixs up the tree :P
     * 
     * @param x_node
     */
    private void deleteFixup(Vertex_DELETE x) {
        
        lang.nextStep();
        
        fixupWindow();

        w_node = null;
        x_node = x;
        
        // Highlight the whole RightRotate pseudo code in the context color
        srcDELETE_FIX.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);

        srcDELETE_FIX.highlight(0);
        
        
        
        if(violation.equals("1")) {
            showGeneralInformation("generalInformationDeleteFixupV1");
            showSpecificInformation("specificInformationDeleteFixupV1");
        }
        
        if(violation.equals("2")) {
            showGeneralInformation("generalInformationDeleteFixupV2");
            showSpecificInformation("specificInformationDeleteFixupV2");
        }

        if(violation.equals("4")) {
            showGeneralInformation("generalInformationDeleteFixupV4");
            showSpecificInformation("specificInformationDeleteFixupV4");
        }
        
        if(violation.equals("none")) {
            showGeneralInformation("generalInformationDeleteFixupVK");
        }
        
        langNextStepFixup();
        
        // -- STEP -- //
        
        
        srcDELETE_FIX.highlight(1);
        srcDELETE_FIX.highlight(2);
        if(x_node != this.root && x_node.getColor() == BLACK) {
            showSpecificInformation("specificInformationDeleteFixup1True");
        } else {
            showSpecificInformation("specificInformationDeleteFixup1False");
        }
       
        langNextStepFixup();
        
        // -- STEP -- //
        

        while (x_node != this.root && x_node.getColor() == BLACK) {
         
            srcDELETE_FIX.unhighlight(1);
            srcDELETE_FIX.toggleHighlight(2, 3);
            
            if(x_node == x_node.getParent().getLeftChild()) {
                showSpecificInformation("specificInformationDeleteFixup3True");
            } else {
                showSpecificInformation("specificInformationDeleteFixup3False");
            }
            
            langNextStepFixup();
            
            // -- STEP -- //
            
            if (x_node == x_node.getParent().getLeftChild()) {
               
                w_node = x_node.getParent().getRightChild();
                
                srcDELETE_FIX.toggleHighlight(3, 4);
                showSpecificInformation("specificInformationDeleteFixup4");
                
                langNextStepFixup();
                
                // -- STEP -- //
                
                srcDELETE_FIX.toggleHighlight(4, 5);
                if(w_node.getColor() == RED) {
                    showSpecificInformation("specificInformationDeleteFixup5True");
                    showGeneralInformation("generalInformationDeleteFixup10");
                } else {
                    showSpecificInformation("specificInformationDeleteFixup5False");
                }
                
                langNextStepFixup();
                
                // -- STEP -- //
                
                if (w_node.getColor() == RED) {
                    
                    w_node.setColor(BLACK);
                    
                    srcDELETE_FIX.toggleHighlight(5, 6);
                    showSpecificInformation("specificInformationDeleteFixup6");
                    showGeneralInformation("generalInformationDeleteFixup11");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    x_node.getParent().setColor(RED);
                    
                    srcDELETE_FIX.toggleHighlight(6, 7);
                    showSpecificInformation("specificInformationDeleteFixup7");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    srcDELETE_FIX.toggleHighlight(7, 8);
                    showSpecificInformation("specificInformationDeleteFixup8");
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    Vertex_DELETE x_tmp = x_node;
                    rotateLeft(x_node.getParent());
                    x_node = x_tmp;
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    w_node = x_node.getParent().getRightChild();
                    
                    srcDELETE_FIX.toggleHighlight(8, 9);
                    showSpecificInformation("specificInformationDeleteFixup9");
                    showGeneralInformation("generalInformationDeleteFixup12");
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
               
                    srcDELETE_FIX.toggleHighlight(9, 10);
                } else {
                    srcDELETE_FIX.toggleHighlight(5, 10);
                }
                
                
                srcDELETE_FIX.highlight(11);
                if(w_node.getLeftChild().getColor() == BLACK && w_node.getRightChild().getColor() == BLACK) {
                    showSpecificInformation("specificInformationDeleteFixup10True");
                    showGeneralInformation("generalInformationDeleteFixup20");
                } else {
                    showSpecificInformation("specificInformationDeleteFixup10False");
                }
                
                langNextStepFixup();
                
                // -- STEP -- //

                if (w_node.getLeftChild().getColor() == BLACK && w_node.getRightChild().getColor() == BLACK) {
                   
                    w_node.setColor(RED);
                    
                    srcDELETE_FIX.unhighlight(10);
                    srcDELETE_FIX.toggleHighlight(11, 12);
                    showSpecificInformation("specificInformationDeleteFixup12");
                    showGeneralInformation("generalInformationDeleteFixup21");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    x_node = x_node.getParent();
                    
                    srcDELETE_FIX.toggleHighlight(12, 13);
                    showSpecificInformation("specificInformationDeleteFixup13");
                    showGeneralInformation("generalInformationDeleteFixup22");
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    srcDELETE_FIX.toggleHighlight(13, 1);
                    srcDELETE_FIX.highlight(2);
                    
                } else {
                    
                    if(w_node.getRightChild().getColor() == BLACK) {
                        showSpecificInformation("specificInformationDeleteFixup15True");
                        showGeneralInformation("generalInformationDeleteFixup30");
                    } else {
                        showSpecificInformation("specificInformationDeleteFixup15False");
                    }
                    
                    srcDELETE_FIX.unhighlight(10);
                    srcDELETE_FIX.toggleHighlight(11, 15);
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    if (w_node.getRightChild().getColor() == BLACK) {
                        
                        w_node.getLeftChild().setColor(BLACK);
                        
                        srcDELETE_FIX.toggleHighlight(15, 16);
                        showSpecificInformation("specificInformationDeleteFixup16");
                        showGeneralInformation("generalInformationDeleteFixup31");
                        RB_TREES_DELETE.updateGraph();
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                        
                        w_node.setColor(RED);
                        
                        srcDELETE_FIX.toggleHighlight(16, 17);
                        showSpecificInformation("specificInformationDeleteFixup17");
                        RB_TREES_DELETE.updateGraph();
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                        
                        showSpecificInformation("specificInformationDeleteFixup18");
                        srcDELETE_FIX.toggleHighlight(17, 18);
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                       
                        Vertex_DELETE x_tmp = x_node;
                        rotateRight(w_node);
                        x_node = x_tmp;
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                       
                        w_node = x_node.getParent().getRightChild();
                        
                        srcDELETE_FIX.toggleHighlight(18, 19);
                        showSpecificInformation("specificInformationDeleteFixup19");
                        showGeneralInformation("generalInformationDeleteFixup32");
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                        
                        srcDELETE_FIX.toggleHighlight(19, 20);
                    } else {
                        srcDELETE_FIX.toggleHighlight(15, 20);
                    }

                    w_node.setColor(x_node.getParent().getColor());
                    
                    showSpecificInformation("specificInformationDeleteFixup20");
                    showGeneralInformation("generalInformationDeleteFixup40");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    x_node.getParent().setColor(BLACK);
                    
                    srcDELETE_FIX.toggleHighlight(20, 21);
                    showSpecificInformation("specificInformationDeleteFixup21");
                    showGeneralInformation("generalInformationDeleteFixup41");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    w_node.getRightChild().setColor(BLACK);
                    
                    srcDELETE_FIX.toggleHighlight(21, 22);
                    showSpecificInformation("specificInformationDeleteFixup22");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    srcDELETE_FIX.toggleHighlight(22, 24);
                    showSpecificInformation("specificInformationDeleteFixup23");
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    Vertex_DELETE x_tmp = x_node;
                    rotateLeft(x_node.getParent());
                    x_node = x_tmp;
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    x_node = this.root;
                    
                    srcDELETE_FIX.toggleHighlight(23, 24);
                    showSpecificInformation("specificInformationDeleteFixup24");
                    showGeneralInformation("generalInformationDeleteFixup42");
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    srcDELETE_FIX.toggleHighlight(24, 1);
                    srcDELETE_FIX.highlight(2);
                }
                
            } else { // ---d.h. if(x == right[p[x]])------
                
                w_node = x_node.getParent().getLeftChild();
                
//                srcDELETE_FIX.unhighlight(3);
                srcDELETE_FIX.toggleHighlight(3, 26);
                showSpecificInformation("specificInformationDeleteFixup26");
                
                langNextStepFixup();
                
                // -- STEP -- //
                
                if(w_node.getColor() == RED) {
                    showSpecificInformation("specificInformationDeleteFixup27True");
                    showGeneralInformation("generalInformationDeleteFixup50");
                } else {
                    showSpecificInformation("specificInformationDeleteFixup27False");
                }
                
                srcDELETE_FIX.toggleHighlight(26, 27);
                
                langNextStepFixup();
                
                // -- STEP -- //
                
                if (w_node.getColor() == RED) {
                    
                    w_node.setColor(BLACK);
                    
                    srcDELETE_FIX.toggleHighlight(27, 28);
                    showSpecificInformation("specificInformationDeleteFixup28");
                    showGeneralInformation("generalInformationDeleteFixup51");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    x_node.getParent().setColor(RED);
                    
                    srcDELETE_FIX.toggleHighlight(28, 29);
                    showSpecificInformation("specificInformationDeleteFixup29");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    srcDELETE_FIX.toggleHighlight(29, 30);
                    showSpecificInformation("specificInformationDeleteFixup30");
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    Vertex_DELETE x_tmp = x_node;
                    rotateRight(x_node.getParent());
                    x_node = x_tmp;
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    w_node = x_node.getParent().getLeftChild();
                    
                    srcDELETE_FIX.toggleHighlight(30, 31);
                    showSpecificInformation("specificInformationDeleteFixup31");
                    showGeneralInformation("generalInformationDeleteFixup52");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    srcDELETE_FIX.toggleHighlight(31, 32);
                } else {
                    srcDELETE_FIX.toggleHighlight(27, 32);
                }
                
                    srcDELETE_FIX.highlight(33);
                    
                    if(w_node.getLeftChild().getColor() == BLACK && w_node.getRightChild().getColor() == BLACK) {
                        showSpecificInformation("specificInformationDeleteFixup32True");
                        showGeneralInformation("generalInformationDeleteFixup60");
                    } else {
                        showSpecificInformation("specificInformationDeleteFixup32False");
                    }
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //

                    srcDELETE_FIX.unhighlight(32);
                    
                if (w_node.getLeftChild().getColor() == BLACK && w_node.getRightChild().getColor() == BLACK) {
                   
                    w_node.setColor(RED);
                    
                    srcDELETE_FIX.toggleHighlight(33, 34);
                    showSpecificInformation("specificInformationDeleteFixup34");
                    showGeneralInformation("generalInformationDeleteFixup61");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                   
                    x_node = x_node.getParent();
                    
                    srcDELETE_FIX.toggleHighlight(34, 35);
                    showSpecificInformation("specificInformationDeleteFixup35");
                    showGeneralInformation("generalInformationDeleteFixup62");
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    srcDELETE_FIX.toggleHighlight(35, 1);
                    srcDELETE_FIX.highlight(2);
                
                } else {
                    
                    
                    srcDELETE_FIX.toggleHighlight(33, 37);
                    
                    if(w_node.getLeftChild().getColor() == BLACK) {
                        showSpecificInformation("specificInformationDeleteFixup37True");
                        showGeneralInformation("generalInformationDeleteFixup70");
                    } else {
                        showSpecificInformation("specificInformationDeleteFixup37False");
                    }
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    if (w_node.getLeftChild().getColor() == BLACK) {
                       
                        w_node.getRightChild().setColor(BLACK);
                        
                        srcDELETE_FIX.toggleHighlight(37, 38);
                        showSpecificInformation("specificInformationDeleteFixup38");
                        showGeneralInformation("generalInformationDeleteFixup71");
                        RB_TREES_DELETE.updateGraph();
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                      
                        w_node.setColor(RED);
                        
                        srcDELETE_FIX.toggleHighlight(38, 39);
                        showSpecificInformation("specificInformationDeleteFixup39");
                        RB_TREES_DELETE.updateGraph();
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                        
                        srcDELETE_FIX.toggleHighlight(39, 40);
                        showSpecificInformation("specificInformationDeleteFixup40");
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                      
                        Vertex_DELETE x_tmp = x_node;
                        rotateLeft(w_node);
                        x_node = x_tmp;
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                      
                        w_node = x_node.getParent().getLeftChild();
                        
                        srcDELETE_FIX.toggleHighlight(40, 41);
                        showSpecificInformation("specificInformationDeleteFixup41");
                        showGeneralInformation("generalInformationDeleteFixup72");
                        
                        langNextStepFixup();
                        
                        // -- STEP -- //
                        
                        srcDELETE_FIX.toggleHighlight(41, 42);
                 
                    } else {
                        srcDELETE_FIX.toggleHighlight(37, 42);
                    }

                    w_node.setColor(x_node.getParent().getColor());
                    
                    showSpecificInformation("specificInformationDeleteFixup42");
                    showGeneralInformation("generalInformationDeleteFixup80");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    x_node.getParent().setColor(BLACK);
                    
                    srcDELETE_FIX.toggleHighlight(42, 43);
                    showSpecificInformation("specificInformationDeleteFixup43");
                    showGeneralInformation("generalInformationDeleteFixup81");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    w_node.getLeftChild().setColor(BLACK);
                    
                    srcDELETE_FIX.toggleHighlight(43, 44);
                    showSpecificInformation("specificInformationDeleteFixup44");
                    RB_TREES_DELETE.updateGraph();
                    
                    langNextStepFixup();
                    
                    // -- STEP -- // 
                    
                    srcDELETE_FIX.toggleHighlight(44, 45);
                    showSpecificInformation("specificInformationDeleteFixup45");
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    Vertex_DELETE x_tmp = x_node;
                    rotateRight(x_node.getParent());
                    x_node = x_tmp;
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    
                    x_node = this.root;
                    
                    srcDELETE_FIX.toggleHighlight(45, 46);
                    showSpecificInformation("specificInformationDeleteFixup46");
                    showGeneralInformation("generalInformationDeleteFixup82");
                    
                    langNextStepFixup();
                    
                    // -- STEP -- //
                    srcDELETE_FIX.toggleHighlight(46, 1);
                    srcDELETE_FIX.highlight(2);
                }
            }

            if(x_node != this.root && x_node.getColor() == BLACK) {
                showSpecificInformation("specificInformationDeleteFixup1True");
            } else {
                showSpecificInformation("specificInformationDeleteFixup1False");
            }
            
            srcDELETE_FIX.highlight(1);
            srcDELETE_FIX.highlight(2);
            
            langNextStepFixup();
            
            // -- STEP -- //
        }
        
        x_node.setColor(BLACK);
        
        srcDELETE_FIX.unhighlight(1);
        srcDELETE_FIX.unhighlight(2);
        
        srcDELETE_FIX.highlight(47);
        showSpecificInformation("specificInformationDeleteFixup47");
        showGeneralInformation("generalInformationDeleteFixup47");
        RB_TREES_DELETE.updateGraph();
        
        langNextStepFixup();
        
        // -- STEP -- //

        srcDELETE_FIX.unhighlight(47);
        srcDELETE_FIX.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, contextColor, null, null);
    }
    
    private void deleteWindow() {
        vars.declare("string", "x", "null", animal.variables.Variable.getRoleString(VariableRoles.WALKER));
        vars.declare("string", "y", "null",animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
        vars.declare("string", "z", "null",animal.variables.Variable.getRoleString(VariableRoles.WALKER));
        
        vars.discard("x.p");
        vars.discard("w");
       
        
        x_pointer.setText("x", null, null);
        y_pointer.setText("y", null, null);
        z_pointer.setText("z", null, null);
        
        hide_w();
        
        y_node = null;
        x_node = null;
        
    }
    
    private void langNextStepDelete() {
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
        
        draw_pointer_x();
        draw_pointer_y();
        draw_pointer_z();

        lang.nextStep();
    }
    
    
    private void fixupWindow() {
        vars.declare("string", "x", "null", animal.variables.Variable.getRoleString(VariableRoles.WALKER));
        vars.declare("string", "w", "null", animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
        vars.declare("string", "x.p", "null", animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
        
        vars.discard("y");
        vars.discard("z");
        
        x_pointer.setText("x", null, null);
        w_pointer.setText("w", null, null);
        
        hide_y();
        hide_z();
    }
    
    private void langNextStepFixup() {
        if (x_node != null) {
            vars.set("x", String.valueOf(x_node.getValue()));
        } else {
            vars.set("x", "null");
        }
        if (w_node != null) {
            vars.set("w", String.valueOf(w_node.getValue()));
        } else {
            vars.set("w", "null");
        }
        if (x_node.getParent() != null) {
            vars.set("x.p", String.valueOf(x_node.getParent().getValue()));
        } else {
            vars.set("x.p", "null");
        }
        
        draw_pointer_x();
        draw_pointer_w();

        lang.nextStep();
    }

    private void rotateWindow() {
        vars.declare("string", "x", "null", animal.variables.Variable.getRoleString(VariableRoles.WALKER));
        vars.declare("string", "y", "null",animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
        
        vars.discard("z");
        vars.discard("x.p");
        vars.discard("w");

        x_pointer.setText("x", null, null);
        y_pointer.setText("y", null, null);

        hide_z();
        hide_w();
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

    public static void inorderTreeWalk(Vertex_DELETE x) {
        if (x != null) {
            inorderTreeWalk(x.getLeftChild());
            System.out.print(x.getValue() + "(" + x.getColor() + "), ");
            inorderTreeWalk(x.getRightChild());
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
    public int getDepth(Vertex_DELETE x, int key, int depth) {
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

    private void hide_x() {
        x_pointer.setText("", null, null);
    }

    private void hide_y() {
        y_pointer.setText("", null, null);   
    }

    private void hide_z() {
        z_pointer.setText("", null, null);
    }
    
    private void hide_w() {
        w_pointer.setText("", null, null);
    }
    
    protected void hide_all() {
        x_pointer.setText("", null, null);
        y_pointer.setText("", null, null);   
        z_pointer.setText("", null, null);
        w_pointer.setText("", null, null);
    }

    private void draw_pointer_w() {
        int x, y;

        hide_w();

        // update Coordinates:
        if (w_node != null
                && w_node.getValue() != null) {

            x = w_node.getX() + 16;
            y = w_node.getY() + 16;

            if (String.valueOf(w_node.getValue()).length() >= 2) {
                x = x + 10;
            }

            // draw w:
            Coordinates x_InsertCoordinates = new Coordinates(x, y);
            w_pointer = lang.newText(x_InsertCoordinates, "w", "w_pointer", null,
                    pointer_text_properties);

        } else {
            x = 80;
            y = -2;

            w_pointer = lang.newText(new Offset(x, y, "null_text", "NW"), "w", "w_pointer", null,
                    pointer_text_properties);
        }
    }
    
    private void draw_pointer_x() {
        int x, y;

        hide_x();

        // update Coordinates:
        if (x_node != null
                && x_node.getValue() != null) {

            x = x_node.getX() - 16;
            y = x_node.getY() - 16;

            

            // draw x:
            Coordinates x_InsertCoordinates = new Coordinates(x, y);
            x_pointer = lang.newText(x_InsertCoordinates, "x", "x_pointer", null,
                    pointer_text_properties);

        } else {
            x = 50;
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
            y = y_node.getY() - 16;

            if (String.valueOf(y_node.getValue()).length() >= 2) {
                x = x + 10;
            }

            Coordinates y_InsertCoordinates = new Coordinates(x, y);
            y_pointer = lang.newText(y_InsertCoordinates, "y", "y_pointer", null,
                    pointer_text_properties);

        } else {
            x = 60;
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

        if (z_node != null && z_node.getValue() != null) {

            x = z_node.getX() - 16;
            y = z_node.getY() + 16;

//            if (String.valueOf(z_node.getValue()).length() >= 2) {
//                x = x;
//            }

            Coordinates y_InsertCoordinates = new Coordinates(x, y);
            z_pointer = lang.newText(y_InsertCoordinates, "z", "z_pointer", null,
                    pointer_text_properties);

        } else {
            x = 70;
            y = -2;

            z_pointer = lang.newText(new Offset(x, y, "null_text", "NW"), "z", "z_pointer", null,
                    pointer_text_properties);

        }
        
//      int x, y;
//
//      // z_pointer.show();
//      z_pointer.setText("z", null, null);
//
//      if (z_node.getParent() == null) {
//          z_pointer.moveTo("NW", "translate", new Offset(16, 38, "vertexCircle"
//                  + deletedVertices, "NW"),
//                  null, null);
//      } else {
//          if (z_node != null
//                  && z_node.getValue() != null) {
//
//              x = z_node.getX() - 15;
//              y = z_node.getY() + 15;
//
//              Coordinates z_InsertCoordinates = new Coordinates(x, y);
//
//              // Move z pointer to the right position
//              z_pointer.moveTo("NW", "translate", z_InsertCoordinates, null, null);
//          }
//      }
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
    
    private void drawOriginalColor() {

        TextProperties original_color_properties = new TextProperties();
        original_color_properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 14));

        lang.newText(new Offset(0, -50, "infoHeader", "NW"),
                "y-original-color: ", "original_color", null, original_color_properties);

        // Info-Box Rectangle:
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        originalColorRect = lang.newRect(new Offset(5, -5, "original_color", "NE"),
                new Offset(35, 25, "original_color", "NE"),
                "originalColorRect",
                null, rectProps);
    }

    
    private void fillOriginalColor(char color) {

        if (color == BLACK) {
            originalColorRect.changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
                    GRAPH_BLACK_COLOR, null, null);
        } 

        if (color == RED) {
            originalColorRect.changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
                    GRAPH_RED_COLOR, null, null);
        }
        
        if (color == WHITE) {
            originalColorRect.changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
                    Color.WHITE, null, null);
        }

    }
    
    private String getViolation(Vertex_DELETE x, char yoc) {
        
        String returnString = "none";
        
        
        if (yoc == BLACK) {
            
            returnString = "1";
            
            if(this.root != null && this.root.getColor() == RED) {
                returnString = "2";
            }
            
            if(x.getColor() == RED && x.getParent() != null &&  x.getParent().getColor() == RED) {
                returnString = "4";
            }
            
            if(x != this.root && x.getColor() == BLACK) {
               
            }
        } 
        
        return returnString;
        
    }
    
    
    private void addViolationQuestion(char yoc) {

        int random = randomGenerator.nextInt(100);
        if (random <= questionProbability) {

            MultipleChoiceQuestionModel mcqm = new MultipleChoiceQuestionModel("violation"
                    + deletedVertices);

            mcqm.setPrompt(translator.translateMessage("questionWhichViolation"));
            String answer = getViolation(x_node, yoc);

            if (answer.equals("1")) {
                mcqm.addAnswer("1.", 1, translator.translateMessage("questionCorrectAnswer", 
                                translator.translateMessage("d1")));
                mcqm.addAnswer("2.", 0, translator.translateMessage("questionWrongAnswer", "1."));
                mcqm.addAnswer("3.", 0, translator.translateMessage("questionWrongAnswer", "1."));
                mcqm.addAnswer("4.", 0, translator.translateMessage("questionWrongAnswer", "1."));
                mcqm.addAnswer("5.", 0, translator.translateMessage("questionWrongAnswer", "1."));
                mcqm.addAnswer(translator.translateMessage("none2"), 0,
                        translator.translateMessage("questionWrongAnswer", "1."));

            }

            if (answer.equals("2")) {
                mcqm.addAnswer("1.", 0, translator.translateMessage("questionWrongAnswer", "2."));
                mcqm.addAnswer("2.",1,translator.translateMessage("questionCorrectAnswer",
                                translator.translateMessage("d2")));
                mcqm.addAnswer("3.", 0, translator.translateMessage("questionWrongAnswer", "2."));
                mcqm.addAnswer("4.", 0, translator.translateMessage("questionWrongAnswer", "2."));
                mcqm.addAnswer("5.", 0, translator.translateMessage("questionWrongAnswer", "2."));
                mcqm.addAnswer(translator.translateMessage("none2"), 0,
                        translator.translateMessage("questionWrongAnswer", "2."));

            }
            
            if (answer.equals("4")) {
                mcqm.addAnswer("1.", 0, translator.translateMessage("questionWrongAnswer", "4."));
                mcqm.addAnswer("2.", 0, translator.translateMessage("questionWrongAnswer", "4."));
                mcqm.addAnswer("3.", 0, translator.translateMessage("questionWrongAnswer", "4."));
                mcqm.addAnswer(
                        "4.",
                        1,
                        translator.translateMessage("questionCorrectAnswer",
                                translator.translateMessage("d4")));
                mcqm.addAnswer("5.", 0, translator.translateMessage("questionWrongAnswer", "4."));
                mcqm.addAnswer(translator.translateMessage("none2"), 0,
                        translator.translateMessage("questionWrongAnswer", "4."));
            }
            
            if(answer.equals("none")) {
                
                mcqm.addAnswer("1.", 0, translator.translateMessage("questionWrongAnswer", translator.translateMessage("none2")));
                mcqm.addAnswer("2.", 0, translator.translateMessage("questionWrongAnswer", translator.translateMessage("none2")));
                mcqm.addAnswer("3.", 0, translator.translateMessage("questionWrongAnswer", translator.translateMessage("none2")));
                mcqm.addAnswer("4.", 0, translator.translateMessage("questionWrongAnswer", translator.translateMessage("none2")));
                mcqm.addAnswer("5.", 0, translator.translateMessage("questionWrongAnswer", translator.translateMessage("none2")));
                
                mcqm.addAnswer(translator.translateMessage("none2"), 1, translator.translateMessage("questionCorrectAnswer", translator.translateMessage("none2")));
                
                
            }
            
            lang.addMCQuestion(mcqm);
        }
    }

}
