/*
 * Bin�reExponentationWiz.java
 * Daniel Friesen,Jens Abels, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;

public class BinaereExponentationWiz implements ValidatingGenerator {
    private Language lang;
    private int x;
    private int y;
    private SourceCodeProperties scProps;

    public void init(){
        lang = new AnimalScript("Bin�re Exponentation auf Basis der russischen Bauernmultiplikation", "Daniel Friesen,Jens Abels", 800, 600);
        
    }
    
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        x = (Integer)primitives.get("x");
        y = (Integer)primitives.get("y");
        scProps = (SourceCodeProperties)props.getPropertiesByName("scProps");
        
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        
        lang.setStepMode(true);
        
        start(x,y);
        
        lang.finalizeGeneration();
        
        return lang.toString();
    }
    
    
   public void start(int x, int y){
	   
	   
	   
	   
	 //Anzahl der Knoten die f�r die gegebenen Zahlen ben�tigt werden
	    int groesse=Anzahl_div(y)*2+3;
	    
	    lang.nextStep("Einleitung");
	    
	    
	   //Guppe von Fragen
	   QuestionGroupModel Q= new QuestionGroupModel("Null",2);
	   
	   
	   
	//1. Frage
	   MultipleChoiceQuestionModel q_sqr= new MultipleChoiceQuestionModel("Square?");
	   q_sqr.setGroupID(Q.getID());
	   q_sqr.setPrompt("Welcher der beiden Pfade wird quadriert?");
	   q_sqr.addAnswer("x (Die Basis)",1,"Richtig");
	   q_sqr.addAnswer("y (Der Exponent)",0,"Leider Falsch.");
	   
	 //2. Frage
	   MultipleChoiceQuestionModel q_mod= new MultipleChoiceQuestionModel("Mod?");
	   q_mod.setGroupID(Q.getID());
	   q_mod.setPrompt("Welcher der beiden Zahlen wird auf die Restklasse 2 (mod 2) gepr�ft?");
	   q_mod.addAnswer("x (Die Basis)",0,"Leider Falsch.");
	   q_mod.addAnswer("y (Der Exponent)",1,"Richtig!");
	   
	   //Z�hler
	   
	   int [] cntA= new int [1];
	   cntA[0]=2;
	   
	  ArrayProperties aProps=new ArrayProperties();
	  
	  
	  
	  IntArray cnt= lang.newIntArray(new Coordinates(500,300), cntA, "", null);
	  cnt.hide();
	  
	  TwoValueCounter counter= lang.newCounter(cnt);
	  CounterProperties cp = new CounterProperties();
	  cp.set(AnimationPropertiesKeys.FILLED_PROPERTY,false);
	  cp.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.WHITE);
	  
	  
	  
	  

	 
	  
	  
	  
	  
	   
	   
	// ################################## Header #############################################################################    
	    SourceCodeProperties hProps = new SourceCodeProperties();
	    hProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
	            new Font("SansSerif", Font.BOLD, 22));
	    SourceCodeProperties hProps2 = new SourceCodeProperties();
	    hProps2.set(AnimationPropertiesKeys.FONT_PROPERTY,
	            new Font("SansSerif", Font.BOLD, 12));
	    SourceCode header = lang.newSourceCode(new Coordinates(50,15), "header",null,hProps);
	    header.addCodeLine("Binaere Exponentation", null, 0, null);
	    SourceCode header2 = lang.newSourceCode(new Coordinates(50,50), "header2",null,hProps2);
	    header2.addCodeLine("(Russische Bauernmultiplikation)", null, 0, null);
	    
	    // ################################## Rect_Header ########################################################################
	    RectProperties recProps = new RectProperties();
	    Rect r1 = lang.newRect(new Offset(-10,-5,"header",
	    		AnimalScript.DIRECTION_NW),new Offset(10,15,"header", "SE"), "r1", null, recProps);  
	    
	// ################################## Rect + X / Y / Mod ########################################################################    
	    
	    SourceCode X_txt = lang.newSourceCode(new Coordinates(436,40), "X_txt",null,hProps2);
	    X_txt.addCodeLine("X", null, 0, null);
	    
	    Rect rX = lang.newRect(new Offset(-10,-4,"X_txt",AnimalScript.DIRECTION_NW),new Offset(10,4,"X_txt", "SE"), "rX", null, recProps);
	    
	    SourceCode Y_txt= lang.newSourceCode(new Coordinates(644,40), "Y_txt",null,hProps2); 
	    Y_txt.addCodeLine("Y", null, 0, null);
	    
	    Rect rY = lang.newRect(new Offset(-10,-4,"Y_txt",AnimalScript.DIRECTION_NW),new Offset(10,4,"Y_txt", "SE"), "rY", null, recProps);
	    
	    SourceCode mod_txt= lang.newSourceCode(new Coordinates(774,40), "mod_txt",null,hProps2); 
	    mod_txt.addCodeLine("Y mod 2", null, 0, null);
	    
	    Rect rMod = lang.newRect(new Offset(-10,-4,"mod_txt",AnimalScript.DIRECTION_NW),new Offset(10,4,"mod_txt", "SE"), "rMod", null, recProps);
	    
	    
	 // ############################ Hide all ####################################################################################### 
	    X_txt.hide();
	    Y_txt.hide();
	    mod_txt.hide();
	    rMod.hide();
	    rX.hide();
	    rY.hide();
	    
	    //Properties f�r den Ergebniskasten
	    RectProperties recRes = new RectProperties();
	    recRes.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);
	  //Result-Kette (Noch unsichtbar)
	  //Formatierung zur "Result-Anzeige"
	  	SourceCodeProperties resProps= new SourceCodeProperties();
	  	resProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.green);
	  	resProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
	  			new Font("Monospaced", Font.PLAIN, 16));
	    SourceCode res= lang.newSourceCode(new Coordinates(20,440), "res", null, resProps);

	    res.addCodeLine("Result=1", null, 0, null);
	    res.hide();
	    
	    TwoValueView view= lang.newCounterView(counter, new Offset(0,60,"res",AnimalScript.DIRECTION_NW),cp,true,true);
	    TextProperties text=new TextProperties();
	    
	    //Texte f�r Counter
	    Text Q_T= lang.newText(new Coordinates(120,516), "Quadrate/Teilungen", "Q_T", null, text);
	    Text Fakt=lang.newText(new Coordinates(120,536), "Anzahl Faktoren", "Fakt", null, text);
	    
		  view.hideText();
		  view.hideBar();
		  view.hideNumber();
		  Q_T.hide();
		  Fakt.hide();
		 
	    
	    //Props f�r Graphen
	    GraphProperties graphProps= new GraphProperties();

	    graphProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
	    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.green);
	    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.WHITE);
	    graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
	    
	  
	    
	    
		//Formatierungen f�r den Einleitungstext
		SourceCodeProperties txtProps = new SourceCodeProperties();
		txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font("SansSerif", Font.PLAIN, 14));
		
		
		
	   
	    
		String [] mod_Labels= new String[groesse];
		int [][] mod_adj= new int[groesse][groesse];
		Node[] mod_nodes= new Node[groesse];
		
		mod_adj[0][0]= 0;
		
		int w=1;
		int pos2=0;
		
		//Bestimmen der Koordinaten
		set_mod_Coord(w,pos2,mod_nodes,groesse);
		
		
		
		//Labels des Mod-Trees
		set_mod_label(y,mod_Labels);
		
		//Graph erstellen
		Graph mod= lang.newGraph("mod",mod_adj,mod_nodes,mod_Labels, null,graphProps);
			
		//Alle Knoten vom Mod-Tree verstecken
		for(int i=0; i<groesse;i++){
			mod.hideNode(i, null, null);
		}
		

		//Positioning etc.
		SourceCode txt= lang.newSourceCode(new Coordinates(20,80), "Einleitung", null, txtProps);
		
		//Beginn Einleitungstext
		txt.addCodeLine("Das Verfahren der binaeren Exponentation auf Basis der russischen Bauernmultiplikation",null, 0,null);
		txt.addCodeLine("bedient sich der einfachen Halbierung, Quadrierung und Multiplikation von Zahlen.",null, 0,null);
		txt.addCodeLine("Dabei werden folgende Schritte ausgefuehrt: ", null, 0,null);
		txt.addCodeLine("", null, 0, null);

		//Nacheinander Aufblenden mit einem Delay von 2 Sekunden jeweils.
		lang.nextStep(2000);
		
		
		
		txt.addCodeLine("1. Exponent und Basis werden getrennt und unabhaengig von einander behandelt", null, 0, null);
		txt.addCodeLine("", null, 0, null);
		
		lang.nextStep(2000);
		
		txt.addCodeLine("2. Nun wird bei jeder Iteration der Exponent halbiert, waehrend die Basis immer quadriert wird", null, 0,null);
		txt.addCodeLine("bis zu dem Zeitpunkt, an dem der Exponent nicht mehr groesser Null ist.", null, 0,null);
		txt.addCodeLine("Sollte die Halbierung des Exponenten in einer Iteration eine ungerade Zahl ergeben,", null, 0,null);
		txt.addCodeLine("wird die Basis des jeweiligen Iterationsschrittes auf das Ergebnis aufmultipliziert.", null, 0, null);
		txt.addCodeLine("", null, 0, null);

		lang.nextStep(2000);
		
		txt.addCodeLine("3. Das daraus resultierende Ergebnis der Multiplikation der Multiplikanten ist das Ergebnis der Exponentation.", 
						null, 0, null);
		//Ende des Einleitungstextes
		lang.nextStep(2000);
		
		
		//Beginn des Quellcodes
		
		txt.hide();



			
	SourceCode sc = lang.newSourceCode(new Coordinates(20,80), "Code",null,scProps);

	//Angezeigter Quellcode
	sc.addCodeLine("public int BiExpo (int x, int y)", null,0,null);
	sc.addCodeLine("{", null,0,null);
	sc.addCodeLine("int res=1;", null,2,null);
	sc.addCodeLine("while(y>0){", null,2,null);
	sc.addCodeLine("if(y % 2==1){", null,3,null);
	sc.addCodeLine("res=res*x;", null,4,null);
	sc.addCodeLine("}", null,3,null);
	sc.addCodeLine("x=x^2;", null,2,null);
	sc.addCodeLine("y=y/2;", null,2,null);
	sc.addCodeLine("}", null,2,null);
	sc.addCodeLine("return res;", null,2,null);
	sc.addCodeLine("}", null,0,null);
	sc.addCodeLine("", null,0,null);
	sc.addCodeLine("", null,0,null);
	//Ende Quellcode

	//Kurze Beschreibung/Erkl�rung
	sc.addCodeLine("Wir initialisieren x mit "+x+" und", null,0,null);
	sc.addCodeLine("y mit "+y+". Also berechnen wir "+x+"^"+y+".", null,0,null);
	sc.addCodeLine("", null,0,null);
	sc.addCodeLine("", null,0,null);
	//Ende Erkl�rung






	lang.nextStep(2000);

	//##########Nur Wurzelknoten anzeigen################



	//groesse der Matrix, Anzahl der Knoten und ihre Labels
	int [][] adj= new int [groesse][groesse];
	Node[] Nodes= new Node [groesse];
	String[] labels= new String[groesse];

	//Koordinaten der ersten 3 Knoten (Rechnung und Splitten)
	Nodes[0]= new Coordinates(528,25);
	Nodes[1]= new Coordinates (436,90);
	Nodes[2]= new Coordinates(644,90);

	//Kanten zum Splitten
	adj[0][1]=1;
	adj[0][2]=1;


	//F�rs Vielfache von 90
	int v=1;
	//Position (Y-Achse) der Knoten
	int pos=0;

	//Positionierung aller anderen Knoten

	set_Coord(pos, groesse, v, Nodes);


	//Matrix erstellen

	set_Matrix(groesse,adj);



	//Labels der ersten 3 Knoten
	labels[0]=x+"^"+y;
	labels[1]=x+"";
	labels[2]=y+"";

	//Labels der restlichen Knoten setzen
	set_Knoten(x,y,labels);

	//Graph erstellen
	Graph g= lang.newGraph("g",adj,Nodes,labels,null,graphProps);

	//Alle Konten au�er der Wurzel verstecken
	for (int i=1;i<groesse;i++){
		g.hideNode(i,null,null);
	}
	lang.nextStep("Aufteilung der Knoten.");
	lang.nextStep(2000);

	g.showNode(1, null, null);
	g.showNode(2, null, null);
	X_txt.show();
	Y_txt.show();

	rX.show();
	rY.show();

	g.highlightEdge(0, 1, null, null);
	g.highlightEdge(0, 2, null, null);
	sc.highlight(0);

	//1.Frage ploppt auf
	lang.addMCQuestion(q_sqr);

	//L�ngerer Delay wegen der Frage
	lang.nextStep(8000);

	g.unhighlightEdge(0, 1, null, null);
	g.unhighlightEdge(0, 2, null, null);
	sc.unhighlight(0);

	//Ergebnis-Kette Anzeigen (Auch im Quellcode markieren)
	res.show();
	res.highlight(0);
	sc.highlight(2);
	view.showNumber();
	Q_T.show();
	Fakt.show();
	//Assignments=Quadrierungen/Teilungen/Baumtiefe     Access=Anzahl Faktoren
	counter.accessInc(1);
	counter.assignmentsInc(0);
	
	lang.nextStep(2000);

	res.unhighlight(0);
	sc.unhighlight(2);
	sc.highlight(3);


	//Speichern von x und y
	int ytmp=y;
	int xtmp=x;

	//Z�hler f�r den mod Tree
	int mod_i=0;

	//Z�hler f�r den Normalen Tree
	int g_i=3;

	//Ergebnis
	int result=1;

	//Counter f�r "Resulthighlighting"
	int cres=1;

	//2. Frage
	lang.addMCQuestion(q_mod);
	
	lang.nextStep(8000);
	lang.nextStep("Berechnung: Start");
	//Parametisieren aller anderern Schritte
	while (ytmp>0){
		
		g.highlightNode(g_i-1, null, null);
		
		lang.nextStep(2000);
		
		sc.unhighlight(3);
		g.unhighlightNode(g_i-1, null, null);
		sc.highlight(4);
		
		
		
		
		
		mod.showNode(mod_i, null, null);
		mod.highlightNode(mod_i, null, null);
		mod_txt.show();
		rMod.show();
		

		
		lang.nextStep(2000);
		
		
		
		sc.unhighlight(4);
		mod.unhighlightNode(mod_i, null, null);
		
		
		if(ytmp%2==1){
			
			result= result*xtmp;
			
			sc.highlight(5);
			g.highlightNode(g_i-2, null, null);
			
			counter.accessInc(1);
			
			
			res.addCodeElement("*"+xtmp, "", true, 0, null);
			
			//Highlighting
			set_h_res(cres, res);
			cres=cres+1;
			
			lang.nextStep(2000);
			
			g.unhighlightNode(g_i-2, null, null);
			sc.unhighlight(5);
			set_unh_res(cres,res);
		}
		
		ytmp=ytmp/2;
		xtmp=xtmp*xtmp;
		
		g.showNode(g_i, null, null);
		g.showNode(g_i+1, null, null);
		g.highlightEdge(g_i-2, g_i, null, null);
		g.highlightEdge(g_i-1, g_i+1, null, null);
		
		
		sc.highlight(7);
		sc.highlight(8);
		
		counter.assignmentsInc(1);
		
		lang.nextStep(2000);
		
		sc.unhighlight(7);
		sc.unhighlight(8);
		
		g.unhighlightEdge(g_i-2, g_i, null, null);
		g.unhighlightEdge(g_i-1, g_i+1, null, null);
		
		
		//Neue Indizes der B�ume
		mod_i=mod_i+1;
		g_i=g_i+2;
		sc.highlight(3);
		
		}

	g.highlightNode(g_i-1, null, null);

	lang.nextStep(2000);
	lang.nextStep("Anzeige des Ergebnisses");

	g.unhighlightNode(g_i-1, null, null);
	sc.unhighlight(3);

	//return Result
	sc.highlight(10);


	res.addCodeElement("="+result, "", true, 0, null);
	Rect rRes= lang.newRect(new Offset(-10,-4,"res",AnimalScript.DIRECTION_NW),new Offset(10,4,"res", "SE"), "rRes", null, recRes);

	set_h_res(cres, res);
   }
   
 //Berechnung, wie oft y dividiert werden kann
 	public int Anzahl_div (int y){
 		int counter=0;
 		while(y!=0){
 			counter++;
 			y=y/2;
 		}
 		return counter;
 	}
 	
 	//Vergabe der Labels der Knoten
 	public void set_Knoten(int x, int y, String[] labels ){
 		//Tempor�re Variablen, sodass x und y nicht ver�ndert werden!
 		int xtmp=x;
 		int ytmp=y;
 		//F�r die Indizes der Knoten
 		int counter=3;
 		while(ytmp>=0){
 			xtmp=xtmp*xtmp;
 			ytmp=ytmp/2;
 			labels[counter]=xtmp+"";
 			labels[counter+1]=ytmp+"";
 			counter=counter+2;
 			//F�r den letzten Schitt (Schleifenanker)
 			if(ytmp==0){
 				break;
 			}
 		}
 	}
 	
 	//Platzierung der Koordinaten der Mod-Knoten
 	public void set_mod_Coord(int w, int pos2, Node[] mod_nodes, int groesse){
 		for (int i=0; i<groesse; i++){
 			pos2=w*90;
 			mod_nodes[i]=new Coordinates(794,pos2);
 			w=w+1;
 		}
 	}
 	
 	//Vergabe der Labels der Knoten im mod Tree
 	public void set_mod_label(int y, String[] labels){
 		int ytmp=y;
 		int counter = 0;
 		while (ytmp>0){
 			if(ytmp%2==1){
 				labels[counter]="1";
 			}
 			else{
 				labels[counter]="0";
 			}
 			counter=counter+1;
 			ytmp=ytmp/2;
 		}
 	}
 	
 	//Highlighting von Result
 	public void set_h_res(int cres, SourceCode res){
 		for(int i=0;i<=cres;i++){
 			res.highlight(0, i, false);
 		}
 	}
 	
 	public void set_unh_res(int cres, SourceCode res) {
 		for(int i=0;i<=cres;i++){
 			res.unhighlight(0, i, false);
 		}
 	}
 	
 	//Positionierung der Knoten
 	public void set_Coord(int pos, int groesse, int v, Node[] Nodes){
 		for (int i=3;i<=groesse-1;i=i+2){
 			v=v+1;
 			pos=90*v;
 			Nodes[i]=new Coordinates(436,pos);
 			Nodes[i+1]=new Coordinates(644,pos);
 		}
 	}	
 	
 	//Erzeugen einer Adj.Matrix
 	public void set_Matrix(int groesse, int[][]adj){
 		for(int i=1; i<=groesse-3;i++){
 			adj[i][i+2]=1;
 		}
 	}
    
//############################################################################################################################
    public String getName() {
        return "Bin�re Exponentation auf Basis der russischen Bauernmultiplikation";
    }

    public String getAlgorithmName() {
        return "Bin�re Exponentation (Russische Bauernmultiplikation)";
    }

    public String getAnimationAuthor() {
        return "Daniel Friesen,Jens Abels";
    }

    public String getDescription(){
        return "Das Verfahren der binaeren Exponentation auf Basis der russischen Bauernmultiplikation"
 +"\n"
 +"bedient sich der einfachen Halbierung, Quadrierung und Multiplikation von Zahlen."
 +"\n"
 +"Dabei werden folgende Schritte ausgefuehrt:"
 +"\n"
 +"\n"
 +"1. Exponent und Basis werden getrennt und unabhaengig von einander behandelt"
 +"\n"
 +"\n"
 +"2. Nun wird bei jeder Iteration der Exponent halbiert, waehrend die Basis immer quadriert wird"
 +"\n"
 +"bis zu dem Zeitpunkt, an dem der Exponent nicht mehr groesser Null ist."
 +"\n"
 +"Sollte die Halbierung des Exponenten in einer Iteration eine ungerade Zahl ergeben,"
 +"\n"
 +"wird die Basis des jeweiligen Iterationsschrittes auf das Ergebnis aufmultipliziert."
 +"\n"
 +"\n"
 +"3. Das daraus resultierende Ergebnis der Multiplikation der Multiplikanten ist das Ergebnis der Exponentation.";
    }

    public String getCodeExample(){
        return "public int BiExpo (int x, int y)"
 +"\n"
 +"{"
 +"\n"
 +"     int res=1;"
 +"\n"
 +"     while (y>0){"
 +"\n"
 +"          if(y%2==0){"
 +"\n"
 +"               res=res*x;"
 +"\n"
 +"          }"
 +"\n"
 +"     x=x^2;"
 +"\n"
 +"     y=y/2;"
 +"\n"
 +"     }"
 +"\n"
 +"return res;"
 +"\n"
 +"}"
 +"\n"
 +"     ";
    }

    public String getFileExtension(){
        return "asu";
    }
    
    //Erlaubt nur positive Werte f�r x und y
    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives){
    	x = (Integer)primitives.get("x");
        y = (Integer)primitives.get("y");
        if(x>=0&&y>=0){
        	return true;
        }
        else{
        	return false;
        }
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    
    
}