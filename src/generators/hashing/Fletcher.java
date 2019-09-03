package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class Fletcher implements ValidatingGenerator{
	protected Language lang;
	
	private boolean no_detection = false;
	
	private MyIntVariable op_counter; // Zaehlt Operationen im Verlauf des Algorithmus
	private int[] op_counts = new int[4]; // Haelt alle Ergebnisse von op_counter fuer abschliessende Zusammenfassung
	

	private int error_position;
	private int error_value;
	
	private IntArray array1;
	private ArrayProperties array_props;
	
	private ArrayMarker i;
	private ArrayMarkerProperties ami;
	
	private MyIntVariable sum1, sum2, check1, check2;
	
	private SourceCode sc, sc_calc;
	private SourceCodeProperties sc_props, calc_props, expl_props;
	
	private Text headline;
	private TextProperties headline_props;
	
	private SourceCode expl; // SourceCode, der als Text zweckentfremdet wird, aufgrund von mehr Features
	
	Timing duration = new TicksTiming(5); 
	Timing txt_duration = new TicksTiming(305);
	
	private class MyIntVariable{
		
		private SourceCode varCode;
		private SourceCodeProperties varProps;
		private int varVal;
		private Node varCoord;
		private String varName;
		
		private boolean highlighted = false;
		
		public MyIntVariable(Node coord, int initval, String name){
			varProps = new SourceCodeProperties();
			varProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
			varProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
			
			varVal = initval;
			varName = name;
			varCoord = coord;
			
			update();
		}
		
		public void add(int xy){
			varVal = varVal + xy;
			update();
		}
		
		
		public void mod(int xy){
			varVal = varVal % xy;
			update();
		}
		
		public void set(int xy){
			varVal = xy;
			update();
		}
		
		public int get(){
			return varVal;
		}
		
		
		public void switchHighlight() {
			if(!highlighted)
				varCode.highlight(0);
			else
				varCode.unhighlight(0);
			highlighted = !highlighted;
		}
		
		public void hide() {
			varCode.hide();
		}
		
		private void update(){
			if(varCode != null)
				varCode.hide();
			varCode = lang.newSourceCode(varCoord, "", null, varProps);
			varCode.addCodeLine(varName + " = " + varVal, null, 0, null);
		}
	}
	
	@Override
	public void init(){
		lang = new AnimalScript("Fletcher's Checksum - Animation", "Erich Wittenbeck", 640, 480);
		lang.setStepMode(true);
		
		array_props = new ArrayProperties();
		array_props.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.GREEN);
		array_props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		array_props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		
		ami = new ArrayMarkerProperties();
		ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		
		sc_props = new SourceCodeProperties();
		sc_props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		sc_props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		
		calc_props = new SourceCodeProperties();
		calc_props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		calc_props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		
		expl_props = new SourceCodeProperties();
		expl_props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		headline_props = new TextProperties();
		headline_props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		headline_props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
	}
	
	/**
	 * Realisiert die Animation des Algorithmus
	 * @param msg Die zu versendende/empfangene und zu pruefende Nachricht als byte[] (intern: int[])
	 */
	private void fletcher(int[] msg){
		
		headline = lang.newText(new Coordinates(20,20), "Fletcher's Checksum - Animation", "", null, headline_props);
		headline.setFont(new Font("Monospaced", Font.PLAIN, 20), null, null);
		Rect head_rect = lang.newRect(new Offset(-2,-2,headline,AnimalScript.DIRECTION_NW),
				new Coordinates(395, 37), "", null);
		lang.nextStep();
		
		displayExp(0);
		lang.nextStep("1.: Einfuehrung");
		
		expl.hide();
		showSourceCode(true);
		op_counter = new MyIntVariable(new Offset(0, 20, sc, "SW"),0, "Anzahl Operationen");
		lang.nextStep();
		
		sc.highlight(0);
		lang.nextStep();
		
		// Array-Repraesentation
		array1 = lang.newIntArray(new Coordinates(50,100), msg, "msg", null, array_props);
		lang.nextStep();
		
		sc.toggleHighlight(0,1);
		lang.nextStep();
		
		// Zeile #1
		i = lang.newArrayMarker(array1, 0, "i", null, ami);
		sum1 = new MyIntVariable(new Coordinates(50, 150), 0, "sum1");
		sum2 = new MyIntVariable(new Coordinates(50, 180), 0, "sum2");
		lang.nextStep();
		
		// Zeilen #2 - #5
		sc.toggleHighlight(1,2);
		fletcher_loop();
		
		sc.toggleHighlight(2,5);
		op_counts[0] = op_counter.get();
		lang.nextStep("2. Vorbereiten der Nachricht");
		displayExp(1);
		
		int check1_int = 255 - ((sum1.get() + sum2.get()) % 255);
		int check2_int = 255 - ((sum1.get() + check1_int) % 255);
		
		expl.hide();
		
		sc.toggleHighlight(5,6);
		showCalc_2(sum1.get(), sum2.get());
		
		check1 = new MyIntVariable(new Coordinates(150, 150), check1_int, "check1");
		check1.switchHighlight();
		sc_calc.hide();
		op_counter.add(3);
		lang.nextStep();
		
		sc.toggleHighlight(6,7);
		showCalc_2(sum1.get(), check1_int);
		
		check2 = new MyIntVariable(new Coordinates(150, 180), check2_int, "check2");
		check1.switchHighlight(); check2.switchHighlight();
		sc_calc.hide();
		op_counter.add(3);
		lang.nextStep();
		
		check2.switchHighlight();
		op_counts[1] = op_counter.get(); op_counter.set(0);
		sc.unhighlight(7);
		displayExp(2);
		
		expl.hide();
		check1.switchHighlight();
		lang.nextStep();
		
		
		i.hide(); array1.hide(); 
		array1 = lang.newIntArray(new Coordinates(50, 100), appendToArray(msg, check1.get()), "", null, array_props);
		i = lang.newArrayMarker(array1, msg.length - 1, "i", null, ami);
		i.increment(null, duration);
		array1.highlightCell(i.getPosition(), null, duration);
		sc.highlight(8);
		lang.nextStep();
		
		check1.hide();
		check2.switchHighlight();
		sc.unhighlight(8);
		lang.nextStep();
		
		i.hide(); array1.hide();
		array1 = lang.newIntArray(new Coordinates(50, 100), appendToArray(toArray(array1), check2.get()), "", null, array_props);
		i = lang.newArrayMarker(array1, msg.length, "i", null, ami);
		i.increment(null, duration);
		array1.highlightCell(i.getPosition(), null, duration);
		sc.highlight(9);
		lang.nextStep();
		
		
		check2.hide(); array1.unhighlightCell(i.getPosition(), null, duration);
		i.hide(); 
		sc.toggleHighlight(9,10);
		lang.nextStep("3. Der erste Test");
		
		
		displayExp(3);
		
		sc.hide();
		showSourceCode(false);
		lang.nextStep();
		
		sc.highlight(1);
		lang.nextStep();
		
		expl.hide();
		i = lang.newArrayMarker(array1, 0, "", null, ami);
		sum1.set(0); sum2.set(0);
		lang.nextStep();
		
		sc.toggleHighlight(1, 2);
		fletcher_loop(); 
		
		lang.nextStep();
		op_counts[2] = op_counter.get(); op_counter.set(0);
		displayExp(4);
		
		expl.hide(); i.hide();
		sc.toggleHighlight(2,5);
		displayExp(5);
		
		lang.nextStep("4. Fehlererkennung");
		
		//Fehler-Beispiel
		
		array1.highlightCell(error_position, null, duration);
		lang.nextStep();
		
		if(array1.getData(error_position) + error_value < 255 && array1.getData(error_position) + error_value > 0){
			array1.hide(); 
			array1.put(error_position, array1.getData(error_position) +  error_value, null, duration);
			array1 = lang.newIntArray(new Coordinates(50, 100), toArray(array1), "", null, array_props);
			array1.highlightCell(error_position, null, duration);
		}else{
			if(array1.getData(error_position) == 255)
				no_detection = true;
			
			array1.put(error_position, 0, null, duration);
		}
		lang.nextStep();
		
		
		expl.hide();
		i = lang.newArrayMarker(array1, 0, "i", null, ami);
		sum1.set(0); sum2.set(0);
		array1.unhighlightCell(error_position, null, duration);
		sc.toggleHighlight(5,1);
		lang.nextStep();
		
		sc.toggleHighlight(1,2);
		fletcher_loop();
		
		sc.toggleHighlight(2,5);
		op_counts[3] = op_counter.get(); op_counter.set(0);
		
		if(!no_detection){
		displayExp(7);
		expl.hide();
		displayExp(8);
		}else
			displayExp(10);
		
		expl.hide();
		lang.hideAllPrimitives();
		headline.show(); head_rect.show();
		displayExp(9);
		lang.nextStep("5. Abschluss");
	}
	
	/**
	 * Realisiert die Hauptschleife des fletcher-Algorithmus aus, die im Laufe der Animation
	 * des oefteren ausgefuehrt werden muss
	 */
	private void fletcher_loop(){
		for(; i.getPosition() < array1.getLength(); i.increment(null, duration)){
			lang.nextStep();
			
			sc.toggleHighlight(2,3);
			showCalc_1(sum1.get(), array1.getData(i.getPosition()));
			
			sc_calc.hide();
			sum1.add(array1.getData(i.getPosition())); sum1.mod(255); sum1.switchHighlight(); op_counter.add(2);
			lang.nextStep();
			
			sc.toggleHighlight(3,4);
			sum1.switchHighlight(); 
			showCalc_1(sum2.get(), sum1.get());
			
			sc_calc.hide();
			sum2.add(sum1.get()); sum2.mod(255); sum2.switchHighlight(); op_counter.add(2);
			lang.nextStep();
			
			sum2.switchHighlight();
			sc.toggleHighlight(4,2);
		}
	}
	
	
	/**
	 * Anzeige des Quellcodes des Algorithmus
	 * @param whole soll der Ganze Quellcode (fuer Erzeugen der Prueffelder) oder nur die Schleife (fuer Fehlerpruefung) gezeigt werden.
	 */
	private void showSourceCode(boolean whole){
		sc = lang.newSourceCode(new Coordinates(50, 220), "", null, sc_props);
		
		sc.addCodeLine("fletcher(byte[] message){", null, 0, null); // #0
		sc.addCodeLine("int i = 0; int sum1 = 0; int sum2 = 0;", null, 1, null); // #1
		sc.addCodeLine("for(; i < message.length; i++){", null, 1, null); // #2
		sc.addCodeLine("sum1 = (sum1 + message[i]) % 255 // 2 Operationen", null, 2, null); // #3
		sc.addCodeLine("sum2 = (sum1 + sum2) % 255 // 2 Operationen", null, 2, null); // #4
		sc.addCodeLine("}", null, 1, null); // #5
		
		if(whole){
			sc.addCodeLine("int check1 = 255 - ((sum1 + sum2) % 255) // 3 Operationen", null, 1, null); // #6
			sc.addCodeLine("int check2 = 255 - ((sum1 + check1) % 255) // 3 Operationen", null, 1, null); // #7
			sc.addCodeLine("message = append(message,check1) // Erweitert arrays um ein angegebenes Element", null, 1, null); // #8
			sc.addCodeLine("message = append(message,check2)", null, 1, null); // #9
			sc.addCodeLine("return message", null, 1, null); // #10
		}
		
		sc.addCodeLine("}", null, 0, null); // #11
	}
	
	/**
	 * Veranschaulicht fuer den Zuschauer die Berechnung ((a + b) % 255) aus fletcher_loop
	 */
	private void showCalc_1(int a, int b){
		sc_calc = lang.newSourceCode(new Offset(10,25,array1,"NE"), "", null, calc_props);
		
		sc_calc.addCodeLine("("+a+" + "+b+") % 255 = "+(a+b)+" % 255", null, 0, null);
		lang.nextStep();
		
		sc_calc.hide();
		sc_calc = lang.newSourceCode(new Offset(10,25,array1,"NE"), "", null, calc_props);
		sc_calc.addCodeLine(""+((a+b)% 255), null, 0, null);
		sc_calc.highlight(0);
		lang.nextStep();
	}
	
	/**
	 * Veranschaulicht fuer den Zuschauer die Berechnung von (255 - ((a + b) % 255)) fuer das Erzeugen der Prueffelder
	 */
	private void showCalc_2(int a, int b){
		sc_calc = lang.newSourceCode(new Offset(10,25,array1,"NE"), "", null, calc_props);
		sc_calc.addCodeLine("255 - (("+a+" + "+b+") % 255) = 255 - ("+(a+b)+" % 255)", null, 0, null);
		lang.nextStep();
		
		sc_calc.hide();
		sc_calc = lang.newSourceCode(new Offset(10,25,array1,"NE"), "", null, calc_props);
		sc_calc.addCodeLine(""+(255 - ( (a+b)% 255) ), null, 0, null);
		sc_calc.highlight(0);
		lang.nextStep();
	}
	
	/**
	 * Ergaenzt Arrays um ein angegebenes Element
	 * @param toBeAppendedTo das Array an das man was ranhaengen will
	 * @param x das anzuhaengende Datum
	 * @return Das um x erweiterte Ursprungsarray
	 */
  private int[] appendToArray(int[] toBeAppendedTo, int x){
    int[] res = new int[toBeAppendedTo.length + 1];
    for(int i = 0; i < toBeAppendedTo.length; i++){
      res[i] = toBeAppendedTo[i];
    }
    res[toBeAppendedTo.length] = x;
    return res;
  }
  
  // Inserted to restore compability with removed Array.getData().
  static public int[] toArray(IntArray array){
    final int[] result = new int[array.getLength()];
    for (int i = 0; i < array.getLength(); i++) {
      result[i] = array.getData(i);
    }
    return result;
  }
  
	/**
	 * Zeigt diverse Erlaeuterungen im Laufe der Animation an
	 * @param disp_nr ID der gewuenschten Erklaerung
	 */
	private void displayExp(int disp_nr){
		switch (disp_nr){
			case 0 :
				expl = lang.newSourceCode(new Coordinates(200,100), "", null, expl_props);
				expl.addCodeLine("Bei 'Fletcher's Checksum' handelt es sich, wie beim Namen ersichtlich,", null, 0, null);
				expl.addCodeLine("um einen Pruefsummen-Algorithmus zur Erkennung von Datenuebertragungsfehlern.", null, 0, null);
				expl.addCodeLine("Er ist aehnlich stark wie das CRC-Verfahren,", null, 0, null);
				expl.addCodeLine("hat aber einen teils wesentlich geringeren Rechenaufwand.", null, 0, null);
				expl.addCodeLine("", null, 0, null);
				expl.addCodeLine("Wir sehen uns den Algorithmus in 2 Faellen an:", null, 0, null);
				expl.addCodeLine("- Korrekte uebertragung der Daten", null, 0, null);
				expl.addCodeLine("- Fehlerhafte uebertragung der Daten", null, 0, null);
				expl.addCodeLine("", null, 0, null);
				expl.addCodeLine("Doch zuerst muessen die Pruefsummen selbst berechnet werden.", null, 0, null);
				expl.addCodeLine("", null, 0, null);
				expl.addCodeLine("In der urspruenglichen Variante des Algorithmus wird hierfuer der Datenstream", null, 0, null);
				expl.addCodeLine("in Bytes (8-Bit Bloecke) zerlegt...", null, 0, null);
				break;
			
			case 1 :
				expl = lang.newSourceCode(new Coordinates(300,150), "", null, expl_props);
				expl.addCodeLine("Nun stehen die zwei Pruefsummen fest.",null,0,null);
				expl.addCodeLine("Damit aber die Qualitaet von CRC in Sachen Fehlererkennung erreicht wird,", null, 0, null);
				expl.addCodeLine("muessen diese noch einmal verarbeitet werden und zwar wie folgt:", null, 0, null);
				break;
			case 2 :
				expl = lang.newSourceCode(new Coordinates(300,150), "", null, expl_props);
				expl.addCodeLine("Diese 2 Werte werden nun an die zu versendende Nachricht drangehaengt", null, 0, null);
				break;
			
			case 3 :
				expl = lang.newSourceCode(new Coordinates(300,150), "", null, expl_props);
				expl.addCodeLine("Zur Fehlerpruefung muss der Empfaenger nun den Algorithmus erneut", null, 0, null);
				expl.addCodeLine("auf die empfangene Nachricht, samt Prueffeldern, andwenden", null, 0, null);
				break;
			
			case 4 :
				expl = lang.newSourceCode(new Coordinates(300,150), "", null, expl_props);
				expl.addCodeLine("Die Pruefsumme (sum1 + sum2) ist 0! Somit kann von einer", null, 0, null);
				expl.addCodeLine("korrekten uebertragung der Daten ausgegangen werden!", null, 0, null);
				break;
			
			case 5 :
				expl = lang.newSourceCode(new Coordinates(300,150), "", null, expl_props);
				expl.addCodeLine("Nun ein anderes Szenario: Bei der uebertragung ist ein Fehler ", null, 0, null);
				expl.addCodeLine("aufgetreten - hier : in Byte #" + error_position, null, 0, null);
				break;
			
			case 6 :
				expl = lang.newSourceCode(new Coordinates(300,150), "", null, expl_props);
				expl.addCodeLine("Wir pruefen erneut wie vorher...", null, 0, null);
				break;
			
			case 7 :
				expl = lang.newSourceCode(new Coordinates(300,150), "", null, expl_props);
				expl.addCodeLine("Die Pruefsumme ist NICHT 0. Somit weiss der Empfaenger,", null, 0, null);
				expl.addCodeLine("dass ein Fehler aufgetreten ist", null, 0, null);
				break;
			case 9 :
				expl = lang.newSourceCode(new Coordinates(300,150), "", null, expl_props);
				expl.addCodeLine("Es wurden",null,0,null);
				expl.addCodeLine("",null,0,null);
				expl.addCodeLine("- Beim ersten Errechnen von sum1 und sum2 "+op_counts[0]+" Operationen",null,0,null);
				expl.addCodeLine("- Beim Pruefen der Nachricht jeweils "+op_counts[2]+" Operationen",null,0,null);
				expl.addCodeLine("... durchgefuehrt",null,0,null);
				expl.addCodeLine("",null,0,null);
				expl.addCodeLine("Man erkennt, dass die Zeitkomplexitaet ( der Hauptschleife ) bei n*4 liegt",null,0,null);
				expl.addCodeLine("wobei n die Laenge der Nachricht ( die zu erweitern oder zu pruefen ist ) in Bytes ist.",null,0,null);
				expl.addCodeLine("Dies steht im Gegensatz zu - zB. - dem CRC-Verfahren, welches (im schlimmsten Fall) von der Anzahl der einzelnen Bits abhaengt.",null,0,null);
				expl.addCodeLine("",null,0,null);
				expl.addCodeLine("Fazit: Ein einfacher, aber effektiver Algorithmus zur Erkennung" +
						" von uebertragungsfehlern. Hoffentlich war diese Animation aufschlussreich!", null, 0, null);
				break;
			case 10 : 
				expl = lang.newSourceCode(new Coordinates(300,150), "", null, expl_props);
				expl.addCodeLine("Die Schwaeche dieses Verfahrens liegt darin, dass es nicht zwischen den Bytes 11111111 und 00000000,", null, 0, null);
				expl.addCodeLine("bzw. 255 und 0 ( in Dezimal ) unterscheiden kann, weswegen sich die Pruefsummen nicht geaendert und folgich der Fehler", null, 0, null);
				expl.addCodeLine("leider nicht entdeckt wurde", null, 0, null);
				break;
			default : break;
		}
		lang.nextStep();
	}

	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		validateInput(arg0, arg1);
		
		array_props = (ArrayProperties) arg0.getPropertiesByName("array_properties");
		ami = (ArrayMarkerProperties) arg0.getPropertiesByName("message_marker");
		sc_props = (SourceCodeProperties) arg0.getPropertiesByName("sourcecode_properties");
		calc_props = (SourceCodeProperties) arg0.getPropertiesByName("calculation_properties");
		expl_props = (SourceCodeProperties) arg0.getPropertiesByName("explanation_properties");
		headline_props = (TextProperties) arg0.getPropertiesByName("headline_properties");
		
		error_position = (Integer) arg1.get("error_position");
		error_value = (Integer) arg1.get("error_value");
		
		fletcher((int[])arg1.get("message"));
		
		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Fletchers Checksum";
	}

	@Override
	public String getAnimationAuthor() {
		return "Erich Wittenbeck";
	}

	@Override
	public String getCodeExample() {
		return "fletcher(byte[] message){ \n" +
				"\tint i = 0; int sum1 = 0; int sum2 = 0;\n" +
				"\tfor(; i < message.length; i++){\n" +
				"\t\tsum1 = (sum1 + message[i]) % 255\n" +
				"\t\tsum2 = (sum1 + sum2) % 255\n" +
				"\t}\n" +
				"}";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Animation des Pr&uuml;fsummen-Algorithmus von John G. Fletcher, einer Alternative zu CRC ( und co. )" +
				"<br>" +
				"Im folgenden wird die urspr&uuml;ngliche Variante des Verfahrens behandelt.<br>" +
				"Diese basiert auf Bytestreams, weswegen nur Werte aus dem Bereich 0 - 255 zul&auml;ssig sind <br>" +
				"<br>" +
				"Es wird ebenfalls im Rahmen der Animation ein Beispiel durchgerechnet, bei dem ein &Uuml;bertragungsfehler passiert.<br>" +
				"Hierf&uuml;r ist in folgendem bei 'error_position' der Index des betroffenen Bytes und bei 'error_value' der Wert einzugeben,<br>" +
				"um den sich dieses ver&auml;ndern soll.";
	}

	@Override
	public String getFileExtension() {
		return ".asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}

	@Override
	public String getName() {
		return "Fletcher's Checksum";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		for (int x : (int[])arg1.get("message")){
			if(x < 0 || x > 255)
				throw new IllegalArgumentException("Die Werte des Arrays 'message' sind keine vorzeichenlose Bytes (lies: Werte von 0 - 255 )");
		}
		
		int err_po = (Integer) arg1.get("error_position");
		
		if(err_po < 0 || err_po >= ((int[]) arg1.get("message")).length)
			throw new IllegalArgumentException("Der Index "+ err_po +" für den Übertragungsfehler ist ungültig ( negativ oder zu groß für 'message' )!");
		
		if((Integer) arg1.get("error_value") == 0)
			throw new IllegalArgumentException("Der Wert 0 ist als Fehler unzulässig!");
		
		return true;
	}
}
