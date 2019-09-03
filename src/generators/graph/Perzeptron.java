/*
 * Perzeptron.java
 * Oemer M. Ayar, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;


import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Perzeptron implements Generator {
    private static Language lang;
    private static Perzeptron s;
    private static Text header;
    private static Rect hRect,iRect,iRect_1,iRect_4,iRect_5,rect_logic,rect_fehler;
    private static Text intro_1,intro_2,intro_3,intro_4,intro_5,intro_6,intro_7,intro_8,intro_9,intro_10,intro_11,info,info_1,info_2,info_3,info_4,info_5,info_6,fehlerterme;
    private static Text info_logic,output_2,output,func,info_fehler,info_outro;
    private Text legende_1, legende_2, legende_3;
    private Text activ_a,activ_b,activ_h,activ_i,activ_j,activ_x,activ_y;
    private Text out_a,out_b,out_i,out_j,out_h;
    private Text calc_1,calc_2,calc_3,calc_4,calc_5,calc_6;
    private Text zwischen_info, zwischen_info_1;
    private Text and_tab_1,and_tab_2,and_tab_3,and_tab_4,and_tab_5,and_tab_6;
    private Text or_tab_1,or_tab_2,or_tab_3,or_tab_4,or_tab_5,or_tab_6;
    private Text xor_tab_1,xor_tab_2,xor_tab_3,xor_tab_4,xor_tab_5,xor_tab_6;
    private Text and_tab_info, andor_tab_info;
    private static Circle x,y,h,i,j,a,b;
    GraphProperties graph_properties = new GraphProperties();
    TextProperties info_properties = new TextProperties();
    RectProperties info_rectProps = new RectProperties();
    TextProperties tab_properties = new TextProperties();
    private int[][] matrix = new int[7][7];
    int a_activ;
    int b_activ;
    int h_activ;
    
    private int input_x = 2;
    private int input_y = -4;;
    
    Graph graph;
    
    
    public void init(){
        lang = new AnimalScript("Perzeptron", "Oemer M. Ayar", 800, 600);
    }
    
    public Perzeptron(Language l) {
    	lang = l;
        lang.setStepMode(true);
      }
    
    public Perzeptron() {
    }
    
    
    public static void main(String[] args) {
        // Create a new language object for generating animation code
        // this requires type, name, author, screen width, screen height
    	lang = new AnimalScript("Perzeptron", "Oemer M. Ayar", 800, 600);
    	
        System.out.println(lang);
        
      }
    
    public void start_intro(){
    	
    	//first step header
    	TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 30));
        header = lang.newText(new Coordinates(20, 30), "Perzeptron",
            "header", null, headerProps);
        
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("#B3EE3A"));
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.decode("#B3EE3A"));
        
        hRect = lang.newRect(new Offset(-5, -5, "header",
            AnimalScript.DIRECTION_NW), new Offset(600, 15 , "header", "SE"), "hRect",
            null, rectProps);
        
        
        TextProperties introProps = new TextProperties();
        introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.LAYOUT_LEFT_TO_RIGHT, 15));
        
        intro_1 = lang.newText(new Coordinates(20, 300), "",
                "intro_1", null, introProps);
        
        intro_2 = lang.newText(new Coordinates(20, 80), "Das Perzeptron (nach engl. perception, „Wahrnehmung“) ist ein vereinfachtes künstliches neuronales Netz,",
                "intro_2", null, introProps);
        intro_3 = lang.newText(new Coordinates(20, 100), "das zuerst von Frank Rosenblatt 1958 vorgestellt wurde. Es besteht in der Grundversion (einfaches Perzeptron)",
                "intro_3", null, introProps);
        intro_4 = lang.newText(new Coordinates(20, 120), "aus einem einzelnen künstlichen Neuron mit anpassbaren Gewichtungen und einem Schwellenwert.",
                "intro_4", null, introProps);
        intro_5 = lang.newText(new Coordinates(20, 140), "Unter diesem Begriff werden heute verschiedene Kombinationen des ursprünglichen Modells verstanden,",
                "intro_5", null, introProps);
        intro_6 = lang.newText(new Coordinates(20, 160), "dabei wird zwischen einlagigen und mehrlagigen Perzeptrons (engl. multi-layer perceptron, MLP) unterschieden.",
                "intro_6", null, introProps);
        intro_7 = lang.newText(new Coordinates(20, 180), "Die prinzipielle Arbeitsweise besteht darin, einen Eingabevektor in einen Ausgabevektor umzuwandeln ",
                "intro_7", null, introProps);
        intro_8 = lang.newText(new Coordinates(20,200), "und damit stellt es einen einfachen Assoziativspeicher dar.",
        		"intro_8",null, introProps);
        intro_9 = lang.newText(new Coordinates(20, 220), "Sein Anwendungsgebiet findet das Perzeptron in der künstlichen Intelligenz, im Bereich 'Machine Learning'.",
                "intro_9", null, introProps);
        intro_10 = lang.newText(new Coordinates(20, 300), "Der Unterschied zwischen einlagigen und mehrlagigen Perzeptrons ist, dass bei mehrlagigen Perzeptrons",
                "intro_10", null, introProps);
        intro_11 = lang.newText(new Coordinates(20, 320), "ein Hidden-Layer zwischen Input und Output Neuronen sind. Im Verlauf der Animation wird dies nocheinmal deutlich.",
                "intro_11", null, introProps);

        
        
        
        
        
    	lang.nextStep();
    	
    	
    	intro_1.hide();
    	intro_2.hide();
    	intro_3.hide();
    	intro_4.hide();
    	intro_5.hide();
    	intro_6.hide();
    	intro_7.hide();
    	intro_8.hide();
    	intro_9.hide();
    	intro_10.hide();
    	intro_11.hide();
    }
    
    public void show_graph(){

    	
    	
    	graph_properties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
    	graph_properties.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLUE);
    	graph_properties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.CYAN);
    	graph_properties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.CYAN);
    	graph_properties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
    	graph_properties.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
    	graph_properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    	graph_properties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	graph_properties.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
    	

    	Coordinates x_c = new Coordinates(100,140);
    	Coordinates y_c = new Coordinates(250,140);
    	
    	Coordinates h_c = new Coordinates(25,300);
    	Coordinates i_c = new Coordinates(175,300);
    	Coordinates j_c = new Coordinates(325,300);
    	
    	Coordinates a_c = new Coordinates(100,450);
    	Coordinates b_c = new Coordinates(250,450);
    	
    	Coordinates[] koordinaten = new Coordinates[7];
    	koordinaten[0] = x_c;
    	koordinaten[1] = y_c;
    	koordinaten[2] = h_c;
    	koordinaten[3] = i_c;
    	koordinaten[4] = j_c;
    	koordinaten[5] = a_c;
    	koordinaten[6] = b_c;
    	
    	
    	
    	matrix[0][2] = 3;
    	matrix[0][3] = 2;
    	matrix[0][4] = 1;
    	
    	matrix[1][2] = 1;
    	matrix[1][3] = 3;
    	matrix[1][4] = 1;
    	
    	matrix[2][5] = 2;
    	matrix[2][6] = 2;


    	matrix[3][5] = 2;
    	matrix[3][6] = 1;
    	
    	matrix[4][5] = 1;
    	matrix[4][6] = 1;
    	
    	
    								  //0   1   2   3   4   5   6
    	String[] graph_labels = new String[]{"   x   ","   y   ","   h   ","   i   ","   j   ","   a   ","   b   "};
    	
    	
    	graph = lang.newGraph("Netz", matrix, koordinaten, graph_labels, null, graph_properties);
    	
    	lang.nextStep();
    	
    	
    	graph.highlightNode(0, null, null);
    	graph.highlightNode(1, null, null);
    	
    	
    	TextProperties legend_prop = new TextProperties();
    	legend_prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font( Font.SANS_SERIF, Font.PLAIN, 25));
    	legend_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    	

    	legende_1 = lang.newText(new Coordinates(400, 140), "Input-Neuronen",
                "legende_1", null, legend_prop);
    	
    	lang.nextStep();
    	
    	legende_1.hide();
    	graph.unhighlightNode(0, null, null);
    	graph.unhighlightNode(1, null, null);
    	
    	
    	
    	//graph_properties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
    	//graph = lang.newGraph("Netz", matrix, koordinaten, graph_labels, null, graph_properties);
    	
    	graph.highlightNode(2, null, null);
    	graph.highlightNode(3, null, null);
    	graph.highlightNode(4, null, null);
    	legende_2 = lang.newText(new Coordinates(410, 290), "Hidden-Neuronen",
                "legende_2", null, legend_prop);
    	
    	lang.nextStep();
    	
    	legende_2.hide();
    	graph.unhighlightNode(2, null, null);
    	graph.unhighlightNode(3, null, null);
    	graph.unhighlightNode(4, null, null);
    	
    	
    	
    	
    	//graph_properties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.CYAN);
    	//graph = lang.newGraph("Netz", matrix, koordinaten, graph_labels, null, graph_properties);
    	
    	graph.highlightNode(5, null, null);
    	graph.highlightNode(6, null, null);
    	
    	legende_3 = lang.newText(new Coordinates(400, 435), " Output-Neuronen",
                "legende_3", null, legend_prop);
    	
    	lang.nextStep();
    	legende_3.hide();
    	graph.unhighlightNode(5, null, null);
    	graph.unhighlightNode(6, null, null);
    	
    	
    	
    	//graph_properties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
    	//graph = lang.newGraph("Netz", matrix, koordinaten, graph_labels, null, graph_properties);
     	
    }

    public void show_sideText(){
    	
    	TextProperties node_prop = new TextProperties();
        node_prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 15));
        node_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    	
        info_properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.PLAIN, 12));
        info_properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
        
        
        info_rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        info_rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("#c1ffc1"));
        info_rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        info_rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.decode("#c1ffc1"));
               
        info = lang.newText(new Coordinates(420, 65), "Aktivierungsfunktion, Eingaben:",
                "info", null, info_properties);
        
        func = lang.newText(new Coordinates(420, 100), "g(x) = x + 2 | | x = " + input_x + " , y =" + input_y + ".",
                "info", null, info_properties);
         
         iRect = lang.newRect(new Offset(-5, -5, "info",
             AnimalScript.DIRECTION_NW), new Offset(185, 5, "info", "SE"), "iRect",
             null, info_rectProps);
         
         
         lang.nextStep();
         
         output = lang.newText(new Coordinates(420, 135), "Outputs für h, i und j: ",
                 "output", null, info_properties);
         
         iRect_1 = lang.newRect(new Offset(-5, -5, "output",
                 AnimalScript.DIRECTION_NW), new Offset(240, 5, "output", "SE"), "iRect_1",
                 null, info_rectProps);
         
         info_1 = lang.newText(new Coordinates(420, 165), "in_h = W_x.h * x + W_y.h * y",
                 "info_1", null, info_properties);
         
         
         
         
         activ_x = lang.newText(new Coordinates( 122, 165), "" + input_x, "input_x", null, node_prop);
         activ_y = lang.newText(new Coordinates( 272, 165), "" + input_y, "input_y", null, node_prop);
         
         
         
         
         int h_in = matrix[0][2] * input_x + matrix[1][2] * input_y;
         calc_1 = lang.newText(new Coordinates(420, 185),"        " + "= " +  matrix[0][2] + " * " + input_x + " + " + matrix[1][2] + " * " + input_y + " = "  + h_in,
                 "calc_1", null, info_properties);
         
         
         
         info_1.changeColor(null, Color.GREEN, null,null);
         calc_1.changeColor(null, Color.GREEN, null,null);
         graph.highlightEdge(0, 2, null, null);
         graph.highlightEdge(1, 2, null, null);
        
         lang.nextStep();
         
         info_1.changeColor(null, Color.BLACK, null,null);
         calc_1.changeColor(null, Color.BLACK, null,null);
         graph.unhighlightEdge(0, 2, null, null);
         graph.unhighlightEdge(1, 2, null, null);
         
         
        
         
         int i_in = matrix[0][3] * input_x + matrix[1][3] * input_y;
         info_2 = lang.newText(new Coordinates(420, 215), "in_i = W_x.i * x + W_y.i * y",
                 "info_2", null, info_properties);
         calc_2 = lang.newText(new Coordinates(420, 235),"       " +"= " +  matrix[0][3] + " * " + input_x + " + " + matrix[1][3] + " * " + input_y + " = "  + i_in,
                 "calc_2", null, info_properties);
         
         
         info_2.changeColor(null, Color.GREEN, null,null);
         calc_2.changeColor(null, Color.GREEN, null,null);
         
         graph.highlightEdge(0, 3, null, null);
         graph.highlightEdge(1, 3, null, null);
         
         
         lang.nextStep();
         
         info_2.changeColor(null, Color.BLACK, null,null);
         calc_2.changeColor(null, Color.BLACK, null,null);
         
         graph.unhighlightEdge(0, 3, null, null);
         graph.unhighlightEdge(1, 3, null, null);
         
         int j_in = matrix[0][4] * input_x + matrix[1][4] * input_y;
         info_3 = lang.newText(new Coordinates(420, 265), "in_j = W_x.j * x + W_y.j * y",
                 "info_3", null, info_properties);
         
         calc_3 = lang.newText(new Coordinates(420, 285),"       " +"= " +  matrix[0][4] + " * " + input_x + " + " + matrix[1][4] + " * " + input_y + " = "  + j_in,
                 "calc_3", null, info_properties);
         
         
         
         
         info_3.changeColor(null, Color.GREEN, null,null);
         calc_3.changeColor(null, Color.GREEN, null,null);
         graph.highlightEdge(0, 4, null, null);
         graph.highlightEdge(1, 4, null, null);
         

         
         lang.nextStep();
         
         info_3.changeColor(null, Color.BLACK, null,null);
         calc_3.changeColor(null, Color.BLACK, null,null);
         graph.unhighlightEdge(0, 4, null, null);
         graph.unhighlightEdge(1, 4, null, null);
         
         
         
         info_4 = lang.newText(new Coordinates(420, 300), "Für die Berechnung der Zwischenknoten müssen wir", "info_4", null, info_properties);
         zwischen_info = lang.newText(new Coordinates(420, 320), "unsere Werte in die Aktivierungsfunktion einsetzen.", "zwischen_info", null, info_properties);
        
         lang.nextStep();
         
         
         

         
         
         h_activ = (h_in + 2);
         int i_activ = (i_in + 2);
         int j_activ = (j_in + 2);
         
         activ_h = lang.newText(new Coordinates( 47, 315), "" + h_activ, "activ_h", null, node_prop);
         activ_i = lang.newText(new Coordinates(197, 315), "" + i_activ, "activ_i", null, node_prop);
         activ_j = lang.newText(new Coordinates(347, 315), "" + j_activ, "activ_j", null, node_prop);
         
         out_h = lang.newText(new Coordinates(420, 340), "out_h = "+ h_activ + ", " , "out_h", null, node_prop);
         out_i = lang.newText(new Coordinates(540, 340), "out_i = "+ i_activ + ", " , "out_i", null, node_prop);
         out_j = lang.newText(new Coordinates(660, 340), "out_j = "+ j_activ, "out_j", null, node_prop);
         
         
         
         lang.nextStep();
         
         output_2 = lang.newText(new Coordinates(420, 370), "Outputs für a und b: ",
                 "output_2", null, info_properties);
         
         iRect_5 = lang.newRect(new Offset(-5, -5, "output_2",
                 AnimalScript.DIRECTION_NW), new Offset(245, 5, "output_2", "SE"), "iRect_5",
                 null, info_rectProps);
    	
         
         
         int a_in = matrix[2][5] * h_activ + matrix[3][5] * i_activ + matrix[4][5] * j_activ;
         
         info_5 = lang.newText(new Coordinates(420, 400), "in_ a = W_h.a * out_h + W_i.a * out_i + W_j.a * out_ j",
                 "info_5", null, info_properties);
         
         calc_5 = lang.newText(new Coordinates(420, 420),"         " +"= " +  matrix[2][5] + " * " + h_activ + " + " + matrix[3][5] + " * " + i_activ + "+ " + matrix[4][5] + " * " + j_activ + " = " + a_in    ,
                 "calc_5", null, info_properties);
         
         
         
         
         info_5.changeColor(null, Color.GREEN, null,null);
         calc_5.changeColor(null, Color.GREEN, null,null);
         
         graph.highlightEdge(2, 5, null, null);
         graph.highlightEdge(3, 5, null, null);
         graph.highlightEdge(4, 5, null, null);
         
         
         
        
         lang.nextStep();
         
         
         
         
         info_5.changeColor(null, Color.BLACK, null,null);
         calc_5.changeColor(null, Color.BLACK, null,null);
         
         
         graph.unhighlightEdge(2, 5, null, null);
         graph.unhighlightEdge(3, 5, null, null);
         graph.unhighlightEdge(4, 5, null, null);
         
         int b_in = matrix[2][6] * h_activ + matrix[3][6] * i_activ + matrix[4][6] * j_activ;
         
         info_6 = lang.newText(new Coordinates(420, 440), "in_ a = W_h.b * out_h + W_i.b * out_i + W_j.b * out_ j",
                 "info_6", null, info_properties);
         
         
         calc_6 = lang.newText(new Coordinates(420, 460),"         " +"= " +  matrix[2][6] + " * " + h_activ + " + " + matrix[3][6] + " * " + i_activ + "+ " + matrix[4][6] + " * " + j_activ + " = " + b_in    ,
                 "calc_6", null, info_properties);
         
         
         
         info_6.changeColor(null, Color.green, null,null);
         calc_6.changeColor(null, Color.green, null,null);
         graph.highlightEdge(2, 6, null, null);
         graph.highlightEdge(3, 6, null, null);
         graph.highlightEdge(4, 6, null, null);
         
         
         lang.nextStep();
         
         info_6.changeColor(null, Color.BLACK, null,null);
         calc_6.changeColor(null, Color.BLACK, null,null);
         graph.unhighlightEdge(2, 6, null, null);
         graph.unhighlightEdge(3, 6, null, null);
         graph.unhighlightEdge(4, 6, null, null);
         
         
         
         a_activ = (a_in + 2);
         b_activ = (b_in + 2);
         
         activ_a = lang.newText(new Coordinates( 122, 470), "" + a_activ, "activ_a", null, node_prop);
         activ_b = lang.newText(new Coordinates(270, 470), "" + b_activ, "activ_b", null, node_prop);
         
         
         out_a = lang.newText(new Coordinates(420, 500), "out_a = " + a_activ + ", " , "out_a", null, node_prop);
         out_b = lang.newText(new Coordinates(560, 500), "out_b = " + b_activ, "out_b", null, node_prop);
         
         
         zwischen_info_1 = lang.newText(new Coordinates(420, 480), "Und wieder in die Aktivierungsfunktion und wir sind fertig.", "zwischen_info_1", null, info_properties);
         
    }
    
    
    public void showFehlerTerme(){
    	
    	info_fehler = lang.newText(new Coordinates(20, 80),  "Perzeptron-Lernregel: ",
                "info_fehler", null, info_properties);
        
    	rect_fehler = lang.newRect(new Offset(-5, -5, "info_fehler",
                AnimalScript.DIRECTION_NW), new Offset(635, 10, "info_fehler", "SE"), "rect_fehler",
                null, info_rectProps);
    	    	
    	
    	Text info_learn = lang.newText(new Coordinates(420, 110),  "Wir nehmen an, dass das Netzwerk für obigen Input (x,y) = ("  + input_x + "," + input_y + ")" ,
                "info_learn", null, info_properties);
    	Text info_learn2 = lang.newText(new Coordinates(420, 130),  "die Ausgabe (a,b) = (-0,2,0.9) liefern soll. Die Lernrate sei alpha = 0.5",
                "info_learn2", null, info_properties);
    	
    	lang.nextStep();
    	
    	Text fehler1 = lang.newText(new Coordinates(420, 170),  "Berechnung der Fehlerterme delta_a und delta_b:",
                "fehler1", null, info_properties);
    	
    	Rect fehlerRect = lang.newRect(new Offset(-5, -5, "fehler1",
                 AnimalScript.DIRECTION_NW), new Offset(87, 5, "fehler1", "SE"), "fehlerRect",
                 null, info_rectProps);
             
    	
    	double fehler_a = -0.2;
    	double fehler_b = 0.9;
    	double alpha = 0.5;
    	double delta_a = (fehler_a - a_activ) * 1;
    	double delta_b = (fehler_b - b_activ) * 1;
    	
    	Text ableitung = lang.newText(new Coordinates(420, 200),  "g'(x) = 1",
                "ableitung", null, info_properties);
    	
    	Text calc_deltaA = lang.newText(new Coordinates(420, 220),  "delta_a = "+ "Err_a * g'(in_a) " + " = " + "(" + fehler_a + " - " + a_activ + " )" +  " *  1" + " = " + delta_a,
                "calc_deltaA", null, info_properties);
    	Text calc_deltaB = lang.newText(new Coordinates(420, 240),  "delta_b = "+ "Err_b * g'(in_b) " + " = " +"(" + fehler_b + " - " + b_activ + " )" +  " *  1" + " = " + delta_b,
                "calc_deltaB", null, info_properties);
    	
    	
    	lang.nextStep();
    	
    	Text fehler2 = lang.newText(new Coordinates(420, 270),  "Berechnung der Fehlerrate delta_h:",
                "fehler2", null, info_properties);
    	
    	Rect fehlerRect2 = lang.newRect(new Offset(-5, -5, "fehler2",
                 AnimalScript.DIRECTION_NW), new Offset(168, 5, "fehler2", "SE"), "fehlerRect2",
                 null, info_rectProps);
    	
    	double delta_h = (matrix[2][5] * delta_a * 1) + (matrix[2][6] * delta_b * 1); 
    	
    	Text calc_deltaH1 = lang.newText(new Coordinates(420, 300),  "delta_h = W_h.a * delta_a * g'(in_h) + W_h.b * delta_b * g'(in_h)",
                "calc_deltaH1", null, info_properties);
    	Text calc_deltaH2 = lang.newText(new Coordinates(464, 320),  "= " + matrix[2][5] + " * " + delta_a +  " * 1" + "  +  " + matrix[2][6] + " * " + delta_b + " * " + " 1 ",
                "calc_deltaH2", null, info_properties);
    	Text calc_deltaH3 = lang.newText(new Coordinates(464, 340),  "= " + delta_h,
                "calc_deltaH3", null, info_properties);
    	
    	
    	lang.nextStep();
    	
    	Text learning = lang.newText(new Coordinates(420, 370),  "Gewichtsveränderung für W_h.a (aka learning): ",
                "learning", null, info_properties);
    	
    	Rect fehlerRect3 = lang.newRect(new Offset(-5, -5, "learning",
                 AnimalScript.DIRECTION_NW), new Offset(100, 5, "learning", "SE"), "fehlerRect3",
                 null, info_rectProps);
    	
    	
    	double wha = matrix[2][5] + (alpha * delta_a * h_activ);
    	
    	Text calc_wha1 = lang.newText(new Coordinates(420, 400),  "new_W_h.a = W_h.a + (alpha * delta_a * out_h) ",
                "calc_wha1", null, info_properties);
    	
    	Text calc_wha2 = lang.newText(new Coordinates(487, 420),  "=" + matrix[2][5] +  " + " + "( " + alpha + " * " + delta_a + " * " + h_activ + " )" + " = " + wha  ,
                "calc_wha2", null, info_properties);
    	
    	
    	lang.nextStep();
    	
    	info_learn.hide();
    	info_learn2.hide();
    	fehler1.hide();
    	fehler2.hide();
    	
    	calc_deltaA.hide();
    	calc_deltaB.hide();
    	ableitung.hide();
    	calc_deltaH2.hide();
    	calc_deltaH1.hide();
    	calc_deltaH3.hide();
    	learning.hide();
    	calc_wha1.hide();
    	calc_wha2.hide();
    	graph.hide();
    	
    	fehlerRect3.hide();
    	fehlerRect2.hide();
    	fehlerRect.hide();
    }
    
    
    private void show_logicGraphs(){
    	
    	tab_properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 15));
        tab_properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    	
    	
    	
    	info_logic = lang.newText(new Coordinates(20, 80),  "Logische Funktionen: Schwellenwertfunktion g(x) = 1 für x > 0, g(x) = 0 sonst., ",
                "info_logic", null, info_properties);
        
    	rect_logic = lang.newRect(new Offset(-5, -5, "info_logic",
                AnimalScript.DIRECTION_NW), new Offset(335, 10, "info_logic", "SE"), "rect_logic",
                null, info_rectProps);
        
        
    	Text intro_logic = lang.newText(new Coordinates(20, 120),  "Perzeptronen ermöglichen ebenfalls Modellierungen von Logischen Funktionen. Im Verlauf  ",
                "info_logic", null, info_properties);
    	Text intro_logic2 = lang.newText(new Coordinates(20, 140),  "sehen wir die Graphen und überprüfen ihre Korrektheit anhand von Wahrheitstabellen.",
                "info_logic2", null, info_properties);
    	Text intro_logic3 = lang.newText(new Coordinates(20, 160),  "Wir nutzen hierfür aus, dass Neuronen im Prinzip nichts anderes machen als zu addieren.",
                "info_logic3", null, info_properties);
    	
    	intro_logic.setFont(new Font( Font.SANS_SERIF, Font.PLAIN, 15), null, null);
    	intro_logic2.setFont(new Font( Font.SANS_SERIF, Font.PLAIN, 15), null, null);
    	intro_logic3.setFont(new Font( Font.SANS_SERIF, Font.BOLD, 15), null, null);
    	
    	lang.nextStep();
    	
    	intro_logic.hide();
    	intro_logic2.hide();
    	intro_logic3.hide();
    	
    	
        
    	Text info_and = lang.newText(new Coordinates(20, 120),  "X AND Y : ",
                "info_logic", null, info_properties);
    	
    	info_and.changeColor(null, Color.BLUE, null, null);
        
    	Coordinates x_and = new Coordinates(30,170);
    	Coordinates y_and = new Coordinates(80,170);
   	
    	Coordinates one_and = new Coordinates(130,170);
    	Coordinates a_and = new Coordinates(80,270);
       
       
    	Coordinates[] koordinaten_and = new Coordinates[4];
    	koordinaten_and[0] = x_and;
    	koordinaten_and[1] = y_and;
    	koordinaten_and[2] = one_and;
    	koordinaten_and[3] = a_and;
  
   	
    	int[][] matrix_and = new int[4][4];
   	
		matrix_and[0][3] = 1;
		matrix_and[1][3] = 1;
		matrix_and[2][3] = 1;
		
		
	    String[] and_labels = new String[]{" x "," y "," 1 ", " a "};
	    
	    
	
	    Graph and_graph = lang.newGraph("And_Netz", matrix_and, koordinaten_and, and_labels, null, graph_properties);
	    and_graph.setEdgeWeight(2, 3, -1, null, null);
	    lang.nextStep();
	    
	    
	    and_tab_info = lang.newText(new Coordinates(30, 350),  "Wir überprüfen die Korrektheit des Netwerkes anhand der Wahrheitstabelle:", "tab_info", null, tab_properties);
	    
	    
	    and_tab_1 = lang.newText(new Coordinates(30, 375),  "   x   y   in_a  out_a ", "and_tab_1", null, tab_properties);
	    and_tab_2 = lang.newText(new Coordinates(30, 377),  "________________", "and_tab_2", 		   null, tab_properties);
	    and_tab_3 = lang.newText(new Coordinates(30, 395),  "   0   0    -1        0   ", "and_tab_3", null, tab_properties);
	    and_tab_4 = lang.newText(new Coordinates(30, 410),  "   0   1     0        0   ", "and_tab_4", null, tab_properties);
	    and_tab_5 = lang.newText(new Coordinates(30, 425),  "   1   0     0        0   ", "and_tab_5", null, tab_properties);
	    and_tab_6 = lang.newText(new Coordinates(30, 440),  "   1   1     1        1   ", "and_tab_6", null, tab_properties);
	    
	    lang.nextStep();
	    
	    //////////////////////////NEUER GRAPH//////////////////////////
	   
	    Text info_or = lang.newText(new Coordinates(270, 120),  "X OR Y : ",
	    "info_logic", null, info_properties);
	    
	    info_or.changeColor(null, Color.BLUE, null, null);
	       
	    Coordinates x_or = new Coordinates(270,170);
	   	Coordinates y_or = new Coordinates(370,170);
	
	   	Coordinates a_or = new Coordinates(320,270);
		       
		       
        Coordinates[] koordinaten_or = new Coordinates[3];
        koordinaten_or[0] = x_or;
        koordinaten_or[1] = y_or;
        koordinaten_or[2] = a_or;
		  
		   	
		int[][] matrix_or = new int[3][3];
		   	
		matrix_or[0][2] = 1;
		matrix_or[1][2] = 1;
   	
   	
   	
		String[] or_labels = new String[]{" x "," y "," a "};
   	
		Graph or_graph = lang.newGraph("OR_Netz", matrix_or, koordinaten_or, or_labels, null, graph_properties);
       
		lang.nextStep();
		
		
		or_tab_1 = lang.newText(new Coordinates(270, 375),  "   x   y   in_a  out_a ", "or_tab_1", null, tab_properties);
	    or_tab_2 = lang.newText(new Coordinates(270, 377),  "________________", "or_tab_2", 		   null, tab_properties);
	    or_tab_3 = lang.newText(new Coordinates(270, 395),  "   0   0     0        0   ", "or_tab_3", null, tab_properties);
	    or_tab_4 = lang.newText(new Coordinates(270, 410),  "   0   1     1        1   ", "or_tab_4", null, tab_properties);
	    or_tab_5 = lang.newText(new Coordinates(270, 425),  "   1   0     1        1   ", "or_tab_5", null, tab_properties);
	    or_tab_6 = lang.newText(new Coordinates(270, 440),  "   1   1     2        1   ", "or_tab_6", null, tab_properties);
		
		
		
		lang.nextStep();
       
		//////////////////////////NEUER GRAPH//////////////////////////
       
       
		Text info_and_or = lang.newText(new Coordinates(500, 120),  "(X OR Y ) AND Z: ",
               "info_logic", null, info_properties);
       
		info_and_or.changeColor(null, Color.BLUE, null, null);
		
		Coordinates x_and_or = new Coordinates(500,170);
		Coordinates y_and_or = new Coordinates(550,170);
		Coordinates z_and_or = new Coordinates(600,170);
   	   	Coordinates h_and_or = new Coordinates(550,270);
   	   	Coordinates i_and_or = new Coordinates(600,270);
   	   	Coordinates one_and_or = new Coordinates(650,270);
   	   	Coordinates a_and_or = new Coordinates(600,370);
   	
       
       
   	   	Coordinates[] koordinaten_and_or = new Coordinates[7];
   	   	koordinaten_and_or[0] = x_and_or;
   	   	koordinaten_and_or[1] = y_and_or;
   	   	koordinaten_and_or[2] = z_and_or;
   	   	koordinaten_and_or[3] = h_and_or;
   	   	koordinaten_and_or[4] = i_and_or;
   	   	koordinaten_and_or[5] = one_and_or;
   	   	koordinaten_and_or[6] = a_and_or;
  
   	
   	   	int[][] matrix_and_or = new int[7][7];
   	
   	   	matrix_and_or[0][3] = 1;
   	   	matrix_and_or[1][3] = 1;
   	   	matrix_and_or[2][4] = 1;
   	   	matrix_and_or[3][6] = 1;
   	   	matrix_and_or[4][6] = 1;
   	   	matrix_and_or[5][6] = 1;
   	
   	
   	   	String[] _and_or_labels = new String[]{" x "," y "," z ", " h ", " i " , " 1 ", " a "};
   	
   	   	Graph _and_or_graph = lang.newGraph("And_Or_Netz", matrix_and_or, koordinaten_and_or, _and_or_labels, null, graph_properties);
   	    _and_or_graph.setEdgeWeight(5, 6, -1, null, null);
   	    
   	   	lang.nextStep();
		andor_tab_info = lang.newText(new Coordinates(450, 420),  "Einfache Verkettung von den vorherigen Netzwerken", "xor_tab_info", null, tab_properties);
       
   	   	lang.nextStep();
   	   	

   		and_tab_1.hide();
   		and_tab_2.hide();
   		and_tab_3.hide();
   		and_tab_4.hide();
   		and_tab_5.hide();
   		and_tab_6.hide();
   		or_tab_1.hide();
   		or_tab_2.hide();
   		or_tab_3.hide();
   		or_tab_4.hide();
   		or_tab_5.hide();
   		or_tab_6.hide();
   		and_tab_info.hide();
   		andor_tab_info.hide();
   	   	
   	   	
   	   	
   	   	
   	   	_and_or_graph.hide();
   	   	or_graph.hide();
   	   	and_graph.hide();
       
   	   	info_and_or.hide();
   	   	info_and.hide();
   	   	info_or.hide();
       
   	   	Text info_xor = lang.newText(new Coordinates(20, 300),  "Und wie funktioniert (X XOR Y) ? Am besten mal selbst ausprobieren, bevor man auf weiter klickt! ",
               "info_xor", null, info_properties);
   	   	info_xor.setFont(new Font( Font.SANS_SERIF, Font.BOLD, 14), null, null);
   	   	
   	   	Text info_xor2 = lang.newText(new Coordinates(20, 330),  "TIPP: Es sollte ein mehrlagiges Perzeptron sein, da ein XOR ohne Hidden Layer nicht visualisiert werden kann.",
             "info_xor2", null, info_properties);
 	   	info_xor2.setFont(new Font( Font.SANS_SERIF, Font.BOLD, 14), null, null);
   	   	
   	   	
   	   	
   	   	
   	   	lang.nextStep();
   	   	info_xor.hide();
   	   	info_xor2.hide();
   	   	
   	   	
   	   	Text xor_func_info = lang.newText(new Coordinates(20, 120),  "Die XOR - Funktionalität kann auch umgeschrieben werden als:  ",
                "xor_func_info", null, info_properties);
   	   	lang.nextStep();
   	   	
   	   	
   	   	
   	   	
   		Text xor_func = lang.newText(new Coordinates(400, 120),  " (x AND ~y ) OR ( ~x AND y)  ",
                "xor_func_info", null, info_properties);
   		xor_func.changeColor(null, Color.BLUE, null, null);
   		xor_func.setFont(new Font( Font.SANS_SERIF, Font.BOLD, 17), null, null);
   		lang.nextStep();

   		
   		//////////////////////////NEUER GRAPH//////////////////////////
   	   	
   		
   		Coordinates x_xor = new Coordinates(50,170);
		Coordinates y_xor = new Coordinates(250,170);
		Coordinates h_xor = new Coordinates(50,320);
   	   	Coordinates i_xor = new Coordinates(250,320);
   	   	Coordinates a_xor = new Coordinates(150,470);
   
   	   	Coordinates[] koordinaten_xor = new Coordinates[5];
   	   	koordinaten_xor[0] = x_xor;
   	   	koordinaten_xor[1] = y_xor;
   	   	koordinaten_xor[2] = h_xor;
   	   	koordinaten_xor[3] = i_xor;
   	   	koordinaten_xor[4] = a_xor;

  
   	
   	   	int[][] matrix_xor= new int[5][5];
   	
   	   	matrix_xor[0][2] = 1;
   	   	matrix_xor[1][2] = 1;
   	   	matrix_xor[0][3] = 1;
   	   	matrix_xor[1][3] = 1;
   	   	matrix_xor[2][4] = 1;
   	   	matrix_xor[3][4] = 1;
   	
   	
   	   	String[] xor_labels = new String[]{" x "," y "," h ", " i ", " a "};
   	
   	   	Graph xor_graph = lang.newGraph("XOR_Netz", matrix_xor, koordinaten_xor, xor_labels, null, graph_properties);
   	   	xor_graph.setEdgeWeight(1, 2, -1, null, null);
   	   	xor_graph.setEdgeWeight(0, 3, -1, null, null);
   	   	
   	   	lang.nextStep();
   		
   	    xor_tab_1 = lang.newText(new Coordinates(350, 170),  "   x   y   in_h  in_i   out_h    out_i   in_a    out_a ", "xor_tab_1", null, tab_properties);
	    xor_tab_2 = lang.newText(new Coordinates(360, 172),  "___________________________________", "xor_tab_2", 		   null, tab_properties);
	    xor_tab_3 = lang.newText(new Coordinates(350, 190),  "   0   0     0      0          0        0          0          0", "xor_tab_3", null, tab_properties);
	    xor_tab_4 = lang.newText(new Coordinates(350, 205),  "   0   1    -1      1          0        1          1          1", "xor_tab_4", null, tab_properties);
	    xor_tab_5 = lang.newText(new Coordinates(350, 220),  "   1   0     1     -1          1        0          1          1", "xor_tab_5", null, tab_properties);
	    xor_tab_6 = lang.newText(new Coordinates(350, 235),  "   1   1     0      1          0        0          0          0", "xor_tab_6", null, tab_properties);
   	   	
   	   	lang.nextStep();
   	   	xor_graph.hide();
   	   	
   	   	xor_tab_1.hide();
   	   	xor_tab_2.hide();
   	   	xor_tab_3.hide();
   	   	xor_tab_4.hide();
   	   	xor_tab_5.hide();
   	   	xor_tab_6.hide();
   	   	xor_func_info.hide();
   	   	xor_func.hide();
   	    info_logic.hide();
   	    rect_logic.hide();
   	   	
    }
    
    
    private void showOutro(){
    	
    	info_outro = lang.newText(new Coordinates(20, 80),  "Outro",
                "info_outro", null, info_properties);
        
    	Text end = lang.newText(new Coordinates(20, 120), "Das Perzeptron war damals eines der ersten ernstzunehmenden Lernmaschinen, ",
                 "end", null, info_properties);
    	Text end1 = lang.newText(new Coordinates(20, 140), "jedoch wird die Perzeptron Lernregel heutzutage nicht mehr viel benutzt, da keine Kovergenz bei nicht trennbaren Klassen vorhanden ist. ",
                "end1", null, info_properties);
    	Text end2 = lang.newText(new Coordinates(20, 170), "Ich hoffe die Visualisierung hat Ihnen gefallen. ",
                "end2", null, info_properties);
    	
    }
    
    
    public void learn(){
    	////Einleitung\\\\
    	start_intro();

    	
    	////Neuronales Netz\\\\
    	show_graph();
    	
    	////Informationen anzeigen\\\\
    	show_sideText();
    	
        lang.nextStep();
        
        zwischen_info.hide();
        zwischen_info_1.hide();
        
        calc_1.hide();
        calc_2.hide();
        calc_3.hide();

        calc_5.hide();
        calc_6.hide();
        
        out_a.hide();
        out_b.hide();
        out_h.hide();
        out_i.hide();
        out_j.hide();
        
        //graph.hide();
        
        
        info.hide();
        info_1.hide();
        info_2.hide();
        info_3.hide();
        info_4.hide();
        info_5.hide();
        info_6.hide();
        iRect.hide();
        iRect_1.hide();

        iRect_5.hide();
        func.hide();
        output.hide();
        output_2.hide();
        
        ////FEHLERTERME////
         showFehlerTerme();
        
         
         info_fehler.hide();
         activ_a.hide();
         activ_b.hide();
         activ_h.hide();
         activ_i.hide();
         activ_j.hide();
         activ_x.hide();
         activ_y.hide();
         ////Logische Funktionen\\\\
         show_logicGraphs();
         
         
         
         
        
         showOutro();
       
        
        
        
        
        
        
    }
    
    

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        input_y = (Integer)primitives.get("input_y");
        input_x = (Integer)primitives.get("input_x");
        
        init();
        s = new Perzeptron(lang);  
        s.learn();
        
        return lang.toString();
    }

    public String getName() {
        return "Perzeptron";
    }

    public String getAlgorithmName() {
        return "Perzeptron";
    }

    public String getAnimationAuthor() {
        return "Oemer M. Ayar";
    }

    public String getDescription(){
        return "Das Perzeptron (nach engl. perception, „Wahrnehmung“) ist ein vereinfachtes künstliches neuronales "
 +"\n"
 +"Netz, das zuerst von Frank Rosenblatt 1958 vorgestellt wurde. "
 +"\n"
 +"Es besteht in der Grundversion (einfaches Perzeptron) aus einem einzelnen künstlichen Neuron "
 +"\n"
 +"mit anpassbaren Gewichtungen und einem Schwellenwert."
 +"\n"
 +" Die prinzipielle Arbeitsweise besteht darin, einen Eingabevektor in einen Ausgabevektor "
 +"\n"
 +"umzuwandeln und damit stellt es einen einfachen Assoziativspeicher dar. (Wikipedia)";
    }
    
    public String getCodeExample(){
        return "";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}