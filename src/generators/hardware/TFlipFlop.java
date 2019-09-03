package generators.hardware;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import util.text.FormattedText;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class TFlipFlop {

	private Language lang;

	/**
	 * Default constructor
	 * 
	 * @param l
	 *            the concrete language object used for creating output
	 */
	public TFlipFlop(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);

	}

	private static final String DESCRIPTION = 
			"Ein Flipflop ist ein getakteter Baustein in der Digitaltechnik."
			+"Er reagiert mei&szligt auf die positive Taktflanke und kann 1 Bit speichern."
			+"Ein T-Flipflop wechselt mit jedem Taktimpuls seinen Ausgangszustand."
			+ "Wobei das T nicht f&uumlr Takt, sondern f&uumlr Toggeln oder Toggle steht."
			+ "Dar&uumlber hinaus verf&uumlgt es &uumlber eine Reseteingang. Mit diesem kann der Ausgangswert"
			+ "vom T-Flipflop auf 0 zu initialisieren."
			+ "In dieser Animation wird dem T-Flipflop eine Reihe von Bits &uumlbergeben."
			+ "Die Ausgabe l&aumlsst sich dann typisch f&uumlr Hardware auf einem Wavediagramm ablesen";

	private static final String SOURCE_CODE = "module t_flipflop(" // 0
			+ "\n  input T," // 1
			+ "\n  input clk," // 2
			+ "\n  input reset," // 3
			+ "\n  output reg Q," // 4
			+ "\n  output Qn" // 5
			+ "\n);" // 6
			+ "\nalways@(posedge clk) begin" // 7
			+ "\n  if (reset) 	Q <= 0;" // 8
			+ "\n  else			Q <= Q^T;" // 9
			+ "\n  end" // 10
			+ "\n  assign Qn = Q;" // 11
			+ "\n endmodule"; // 12

	public void simulate(boolean[] a) {
		
		//Signal Properties
		PolylineProperties PolyProps = new PolylineProperties();
		PolyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		//Timeline Properties
		PolylineProperties TimelineProps = new PolylineProperties();
		TimelineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		
		//Wavediagramm Text Properties	
		TextProperties Wavetxtprops = new TextProperties();	
		Wavetxtprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		Wavetxtprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN ,16));
		
		//Text Properties	
		TextProperties txtProps = new TextProperties();	
		txtProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN ,14));
		
		//Sourcecode properties
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		//Array properties
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
			       Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
			       Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
			       Color.YELLOW);
		
		
		
		// intro text
		FormattedText intro_txt = new FormattedText("intro_txt", lang, new Coordinates(80, 200));
		intro_txt.addInNextLine("Das T-Flipflop ist ein digitales Bauteil in Form einer Transistorschaltung.");
		intro_txt.addInNextLine("Zur Darstellung solcher Bauteile in algorithmischer Form wird");
		intro_txt.addInNextLine("hier ein Modul in einer Hardwarebeschreibungssprache (Verilog) gezeigt.");
		intro_txt.addInNextLine("T_Flopflops speichern einen bin�ren Wert abh�ngig von ihrem Eingangssignal T.");
		intro_txt.addInNextLine("T steht hier f�r Toggle. Das T-Flipflop �ndert seinen Wert nur zu einer Taktflanke.");
		intro_txt.addInNextLine("Es gibt zwei Arten von Taktflanken:");
		intro_txt.addInNextLine("-Steigende Taktflanken sind der Wechsel des Eingangssignals clk von 0 auf 1.");
		intro_txt.addInNextLine("-Fallende Taktflanken sind der Wechsel des Eingangssignals clk von 1 auf 0.");
		intro_txt.addInNextLine("Im folgenden Beispiel handelt es sich um ein T-Flipflop, welches auf");
		intro_txt.addInNextLine("steigende Taktflanken reagiert.");
		intro_txt.show();

		intro_txt.hide();
		lang.nextStep();
		intro_txt.hide();
		

		FormattedText intro_txt_two = new FormattedText("introtxt_two",lang,new Coordinates(80,260));
		intro_txt_two.addInNextLine("Auf der rechten Seite ist eine m�gliche Implementierung in Verilog zu erkennen.");
		intro_txt_two.addInNextLine("Neben dem Eingang T existieren noch ein clk und reset Eingang.");
		intro_txt_two.addInNextLine("Diese sind f�r den Takt und die Initialisierung des internen Speichers zust�ndig.");
		intro_txt_two.addInNextLine("");
		intro_txt_two.show();
		
		
		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(520, 260),
				"sourceCode", null, scProps);

		sc.addCodeLine("module t_flipflop(", null, 0, null); 
		sc.addCodeLine("input T,", null, 1, null); 
		sc.addCodeLine("input clk,", null, 1, null);
		sc.addCodeLine("input reset,", null, 1, null); 
		sc.addCodeLine("output reg Q,", null, 1, null); 
		sc.addCodeLine("output Qn", null, 1, null); 
		sc.addCodeLine(");", null, 0, null); 
		sc.addCodeLine("always@(posedge clk) begin", null, 0, null); 
		sc.addCodeLine("if (reset) 	Q <= 0;", null, 1, null); 
		sc.addCodeLine("else Q <= Q^T;", null, 1, null); 
		sc.addCodeLine("end", null, 0, null); 
		sc.addCodeLine("assign Qn = Q;", null, 0, null);
		sc.addCodeLine("endmodule", null, 0, null);
		
		
		intro_txt_two.hide();
		
		lang.nextStep();		
		intro_txt_two.hide();
		
		

		
		// generating clk_signal in wavediagramm
		Text clk_txt = lang.newText(new Coordinates(40,40),"clk", "clk", null, Wavetxtprops);
		Node[] clk_nodes = new Node[5 * a.length];

		int x = 20;
		int y = 60;
		int step = 30;

		clk_nodes[0] = new Coordinates(x, y);
		x += step;
		clk_nodes[1] = new Coordinates(x, y);
		y -= step;
		clk_nodes[2] = new Coordinates(x, y);
		x += step;
		clk_nodes[3] = new Coordinates(x, y);

		for (int i = 0; i < clk_nodes.length-4; i += 4) {
			y += step;
			clk_nodes[i + 0] = new Coordinates(x, y);
			x += step;
			clk_nodes[i + 1] = new Coordinates(x, y);
			y -= step;
			clk_nodes[i + 2] = new Coordinates(x, y);
			x += step;
			clk_nodes[i + 3] = new Coordinates(x, y);
		}
		
		// Draw clk_signal from nodes 
		Polyline clk_signal = lang.newPolyline(clk_nodes, "line", null, PolyProps);
		
		
		// generating T_signal in wavediagramm		
		Text T_txt = lang.newText(new Coordinates(40,75),"T", "T", null, Wavetxtprops);
		
		x = 80;
		y = 100;
		
		ArrayList<Node> T_nodes_arraylist = new ArrayList<Node>();
		
		if (a[0]){
			y-=step;
		}
		T_nodes_arraylist.add(new Coordinates(x, y));
		x+=4*step;
		T_nodes_arraylist.add(new Coordinates(x, y));
		for(int i = 1;i<a.length;i++){
			if (a[i-1]){
				if(!a[i]){
					y+=step;
					T_nodes_arraylist.add(new Coordinates(x,y));					
				}				
			}else {
				if(a[i]){
					y-=step;
					T_nodes_arraylist.add(new Coordinates(x,y));
				}
			}
			x+=2*step;
			T_nodes_arraylist.add(new Coordinates(x,y));
		}
		Node[] T_nodes =(Node[]) T_nodes_arraylist.toArray(new Node[0]);
		// Draw clk_signal from nodes 
		Polyline T_signal = lang.newPolyline(T_nodes, "line", null, PolyProps);
		
		// generating R_signal in wavediagramm		
		Text R_txt = lang.newText(new Coordinates(40,115),"R", "R", null, Wavetxtprops);
				
		x = 80;
		y = 140-step;
		
		Node[] R_nodes = new Node[4];
		R_nodes[0]= new Coordinates(x, y);		
		R_nodes[1]= new Coordinates(x+2*step, y);
		R_nodes[2]= new Coordinates(x+2*step, y+step);
		R_nodes[3]= new Coordinates(x+2*step*(a.length+1), y+step);
		
		
		// Draw R_signal from nodes 
		Polyline R_signal = lang.newPolyline(R_nodes, "line", null, PolyProps);
		
		// generating Q_signal in wavediagramm
		Text Q_txt = lang.newText(new Coordinates(40,155),"Q", "Q", null, Wavetxtprops);
		
		x=80;
		y=180-step;
				
		Node[] Q_nodes_tristate = new Node[5];
		Q_nodes_tristate[0]= new Coordinates(x, y);
		Q_nodes_tristate[1]= new Coordinates(x+step, y);
		Q_nodes_tristate[2]= new Coordinates(x+step, y+step);
		Q_nodes_tristate[3]= new Coordinates(x, y+step);
		
		Polyline Q_signal_tristate = lang.newPolyline(Q_nodes_tristate, "line", null, PolyProps);
		
		Node[] Q_nodes_reset = new Node[2];
		Q_nodes_reset[0] = new Coordinates(x+step, y+step);
		Q_nodes_reset[1] = new Coordinates(x+2*step, y+step);

		Polyline Q_signal_reset = lang.newPolyline(Q_nodes_reset, "line", null, PolyProps);
		
		boolean Q = false;	
		ArrayList<Node> Q_nodes_arrayList = new ArrayList<Node>();
		Q_nodes_arrayList.add(new Coordinates(x+2*step, y+step));
		x+=3*step;
		y+=step;
		Q_nodes_arrayList.add(new Coordinates(x, y));
		for (int i = 0;i<a.length;i++){
			Q=Q^a[i];
			if(a[i]){
				if(Q){
					y-=step;
				}else{
					y+=step;					
				}
				Q_nodes_arrayList.add(new Coordinates(x, y));
			}
			x+=2*step;
			if(i==a.length-1)
				x-=step;
			
			Q_nodes_arrayList.add(new Coordinates(x, y));
		}
		
		Node[] Q_nodes = Q_nodes_arrayList.toArray(new Node[0]);
		
		Polyline Q_signal = lang.newPolyline(Q_nodes, "line", null, PolyProps);
		
		
		// generating Qn_signal in wavediagramm	
		Text Qn_txt = lang.newText(new Coordinates(40,200),"Qn", "Qn", null, Wavetxtprops);
		
		x=80;
		y=225-step;
				
		Node[] Qn_nodes_tristate = new Node[4];
		Qn_nodes_tristate[0]= new Coordinates(x, y);
		Qn_nodes_tristate[1]= new Coordinates(x+step, y);
		Qn_nodes_tristate[2]= new Coordinates(x+step, y+step);
		Qn_nodes_tristate[3]= new Coordinates(x, y+step);
		
		Polyline Qn_signal_tristate = lang.newPolyline(Qn_nodes_tristate, "line", null, PolyProps);
		
		Node[] Qn_nodes_reset = new Node[2];
		Qn_nodes_reset[0] = new Coordinates(x+step, y);
		Qn_nodes_reset[1] = new Coordinates(x+2*step, y);

		Polyline Qn_signal_reset = lang.newPolyline(Qn_nodes_reset, "line", null, PolyProps);	
		
		Q = false;	
		ArrayList<Node> Qn_nodes_arrayList = new ArrayList<Node>();
		Qn_nodes_arrayList.add(new Coordinates(x+2*step, y));
		x+=3*step;
		//y+=step;
		Qn_nodes_arrayList.add(new Coordinates(x, y));
		for (int i = 0;i<a.length;i++){
			Q=Q^a[i];
			if(a[i]){
				if(Q){
					y+=step;
				}else{
					y-=step;					
				}
				Qn_nodes_arrayList.add(new Coordinates(x, y));
			}
			x+=2*step;
			if(i==a.length-1)
				x-=step;
			
			Qn_nodes_arrayList.add(new Coordinates(x, y));
		}
		
		Node[] Qn_nodes = Qn_nodes_arrayList.toArray(new Node[0]);
		
		Polyline Qn_signal = lang.newPolyline(Qn_nodes, "line", null, PolyProps);		
		
		// text to explain the wavediagram
		FormattedText wave_txt = new FormattedText("wave_txt", lang, new Coordinates(80, 260));
		wave_txt.addInNextLine("Das Wavediagramm oben enth�lt sowohl die Eingabe und Ausgabesignale.");
		wave_txt.addInNextLine("Die Linien zeigen den Verlauf der 1 Bit gro�en Signale an.");
		wave_txt.addInNextLine("Auf der x-Achse ist die Zeit angegeben.");
		wave_txt.addInNextLine("Auf der y-Achse sind die Ver�nderungen der Signale bin�r zu sehen.");
		wave_txt.addInNextLine("Ist die Linie oben, so ist der Wert 1. Ist die Linie unten, ist der Wert 0.");
		wave_txt.addInNextLine("Die gr�ne Linie zeigt den Zeitpunkt an, in dem sich die Schaltung befindet.");
		wave_txt.show();
		
		
		x=80;
		y=20;
		int offset = 220;
		
		Node[] timeline = new Node[2];
		timeline[0]=new Coordinates(x, y);
		timeline[1]=new Coordinates(x, y+offset);
		
		Polyline timeline_poly = lang.newPolyline(timeline,"timeline",null, TimelineProps);
		// start a new step after the wavediagram was created
		

		wave_txt.hide();
		lang.nextStep();	
		wave_txt.hide();
		x-=step;
		
		// text for the reset time
		FormattedText reset_time = new FormattedText("reset_tim", lang, new Coordinates(80, 260));
		reset_time.addInNextLine("Ist das Resetsignal auf 1 gesetzt, so wird der Initialzustand des Registers");
		reset_time.addInNextLine("auf 0 gesetzt und die Ausgabe von Q �ndert sich zu 0.");
		reset_time.addInNextLine("Gleichzeitig �ndert sich die Ausgabe von dem negierten Ausgang Qn zu 1.");
		reset_time.addInNextLine("Das Resetsignal ist hier ein Taktsynchroner Reset.");
		reset_time.addInNextLine("Das hei�t das T-Flipflop reagiert auch bei einem Resetsignal");
		reset_time.addInNextLine("nur auf die steigende Taktflanke.");
		reset_time.hide();
		
		// text for rest of simulation time
		FormattedText simulation_time = new FormattedText("simulation_time", lang, new Coordinates(80, 260));
		simulation_time.addInNextLine("Ist das Resetsignal 0, reagiert das T-Flipflop normal");
		simulation_time.addInNextLine("zu jeder Taktflanke auf die Eingabe T.");
		simulation_time.addInNextLine("Ist das T Signal gesetzt, wird der Wert von Q von 0 zu 1");
		simulation_time.addInNextLine("beziehungsweise von 1 zu 0 ge�ndert. Diese Ausgangssignale");
		simulation_time.addInNextLine("werden bis zur n�chsten Taktflanke gehalten.");
		simulation_time.hide();
		
		FormattedText actual_time = reset_time;
		
		for(int i = 0;i<=a.length;i++){
			actual_time.hide();
			x+=2*step;
			timeline[0] = new Coordinates(x, y);
			timeline[1] = new Coordinates(x, y+offset);
			timeline_poly.hide();
			timeline_poly = lang.newPolyline(timeline,"timeline",null, TimelineProps);
			if (i==0){
				actual_time=reset_time;
				sc.highlight(7);
				lang.nextStep();
				sc.toggleHighlight(7, 8);
				actual_time.show();
				lang.nextStep();
				sc.toggleHighlight(8, 10);
				lang.nextStep();
				sc.toggleHighlight(10, 11);
				lang.nextStep();
				sc.unhighlight(11);
				lang.nextStep();
				actual_time.hide();				
			}else{
				actual_time=simulation_time;
				actual_time.show();
				sc.highlight(7);
				lang.nextStep();
				sc.toggleHighlight(7, 9);
				lang.nextStep();
				sc.toggleHighlight(9, 10);
				lang.nextStep();
				sc.toggleHighlight(10, 11);
				lang.nextStep();
				sc.unhighlight(11);
				lang.nextStep();
				
			}
		}
		// end of simulation
		actual_time.hide();
		lang.hideAllPrimitives();
		
		// end text
		FormattedText endtext = new FormattedText("endtext", lang, new Coordinates(200, 260));
		endtext.addInNextLine("Ende der Simulation");
		//endtext.addInNextLine("");
		lang.nextStep();
	}

	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	public String getName() {
		return "T_FlipFlop";
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height
		Language l = new AnimalScript("TFlipflop", "Oliver Dietz", 640, 480);
		TFlipFlop tff = new TFlipFlop(l);
		boolean[] a = { true, false, true, true, true, false, false, true, false, true };
		tff.simulate(a);
		System.out.println(l);
	}

}
