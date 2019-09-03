package generators.sorting.shakersort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnnotatedShakerSort extends AnnotatedAlgorithm implements Generator{

	private int textCounter = 0;
	
	private int [] arrayData;
	
	private IntArray array = null;
	
	private ArrayMarker iMarker, jMarker, lMarker, rMarker;
	
	private ArrayMarkerUpdater amuI, amuJ, amuL, amuR;
	
	private Timing defaultTiming = new TicksTiming(100);
	
	private static ArrayProperties arrProp;
	
	private static ArrayMarkerProperties links_zeiger, rechts_zeiger, i_zeiger, j_zeiger;
	
	private static SourceCodeProperties sc_prop;
	
	private static TextProperties tp1;
	
	private static TextProperties tp2;
	
	private static TextProperties tp3;
	
	private static TextProperties tp4;
	
	private String vergleich = "VERGLEICHE";
	private String zuweisung = "ZUWEISUNGEN"; 
	
	
	public static final String ALGO_BESCHREIBUNG =
		"SHAKERSORT\n"	
		+"Beschreibung des Algorithmus\n"
		+"1. Die zu sortierende Liste wird abwechselnd vom Beginn bzw. vom Ende her durchlaufen\n"
		+"2. Vom Beginn her vergleicht man immer zwei benachbarte Elemente\n"
		+"3. Sind diese Elemente falsch sortiert, dann vertauscht man sie\n"
		+"4. Nach dem ersten Durchlauf ist das größte Element an seiner richtigen Position\n"
		+"5. Danach geht man vom Ende her und vergleicht wiederum immer zwei benachbarte Elemente\n"
		+"6. Sind diese falsch sortiert, dann vertauscht man sie\n"
		+"7. Nach dem zweiten Durchlauf ist das kleinste Elemente an seiner richtigen Position\n"
		+"8. Die Schritte 2 bis 7 wiederholt man bis alle Elemente sortiert sind.";

	
	public void algoDescription (){
		
		//*************** Beschreibung des Algorithmus im Schrift *************************

		Text t0 = lang.newText(new Coordinates(150, 20), "SHAKERSORT", "f1"+textCounter, null, tp1);
		textCounter++;
		lang.nextStep();

		Text t1 = lang.newText(new Coordinates(150, 60), "Beschreibung des Algorithmus", "f1"+textCounter, null, tp2);
		textCounter++;
		lang.nextStep();

		Text t2 = lang.newText(new Coordinates(20, 120), "1. Die zu sortierende Liste wird abwechselnd vom Beginn bzw. vom Ende her durchlaufen", "f1"+textCounter, null, tp3);
		textCounter++;
		lang.nextStep();

		Text t3 = lang.newText(new Coordinates(20, 160), "2. Vom Beginn her vergleicht man immer zwei benachbarte Elemente ", "f1"+textCounter, null, tp3);
		textCounter++;
		lang.nextStep();

		Text t4 = lang.newText(new Coordinates(20, 200), "3. Sind diese Elemente falsch sortiert, dann vertauscht man die", "f1"+textCounter, null, tp3);
		textCounter++;
		lang.nextStep();

		Text t5 = lang.newText(new Coordinates(20, 240), "4. Nach dem ersten Durchlauf ist das größte Element an seiner richtigen Position", "f1"+textCounter, null, tp3);
		textCounter++;
		lang.nextStep();

		Text t6 = lang.newText(new Coordinates(20, 280), "5. Danach geht man vom Ende her und vergleicht wiederum immer zwei benachbarte Elemente", "f1"+textCounter, null, tp3);
		textCounter++;
		lang.nextStep();

		Text t7 = lang.newText(new Coordinates(20, 320), "6. Sind diese falsch sortiert, dann vertauscht man die", "f1"+textCounter, null, tp3);
		textCounter++;
		lang.nextStep();

		Text t8 = lang.newText(new Coordinates(20, 360), "7. Nach dem zweiten Durchlauf, ist das kleinste Elemente an seiner richtigen Position", "f1"+textCounter, null, tp3);
		textCounter++;
		lang.nextStep();

		Text t9 = lang.newText(new Coordinates(20, 400), "8. Die Schritte 2 bis 7 wiederholt man bis alle Elemente sortiert sind.", "f1"+textCounter, null, tp3);
		textCounter++;
		lang.nextStep();
		
		t0.hide();
		t1.hide();
		t2.hide();
		t3.hide();
		t4.hide();
		t5.hide();
		t6.hide();
		t7.hide();
		t8.hide();
		t9.hide();		
	}
	
	public void sort(){
		exec("header");
		lang.nextStep();
		
		exec("l_marker");
		amuL.setVariable(vars.getVariable("links"));
		lang.nextStep();
		
		exec("r_marker");
		amuR.setVariable(vars.getVariable("rechts"));
		vars.set("rechts", String.valueOf(array.getLength() - 1));
		lang.nextStep();
		
		exec("i_marker");
		amuI.setVariable(vars.getVariable("i"));
		lang.nextStep();
		
		exec("j_marker");
		amuJ.setVariable(vars.getVariable("j"));
		lang.nextStep();
		
		exec("setCondition1");
		lang.nextStep();
		
		exec("whileComp");
		lang.nextStep();
		
		while (!"0".equals(vars.get("condition"))) {
			
			exec("resetCondition1");
			lang.nextStep();
			
			exec("iForInit");
			int links = Integer.parseInt(vars.get("links"));
			vars.set("i", String.valueOf(links));
			lang.nextStep();
						
			exec("iForComp");
			lang.nextStep();
			
			while(Integer.parseInt(vars.get("i")) < Integer.parseInt(vars.get("rechts"))){
				
				int i = Integer.parseInt(vars.get("i"));
				
				exec("i_if");
				array.unhighlightElem(0, array.getLength()-1, null, null);
				array.highlightElem(i, i+1, null, null);
				lang.nextStep();				

				if(array.getData(i) > array.getData(i+1)){
					
					exec("i_swap");
					array.swap(i, i+1, null, defaultTiming);
					lang.nextStep();
										
					exec("setCondition2");
					lang.nextStep();
				}
				
				exec("iForInc");
				lang.nextStep();
				
				exec("iForComp");
				lang.nextStep();
			}
			array.unhighlightElem(0, array.getLength()-1, null, null);
			array.highlightCell(Integer.parseInt(vars.get("rechts")), null, null);
			lang.nextStep();
			
			exec("rechts_dec");
			lang.nextStep();
			
			exec("jForInit");
			int rechts = Integer.parseInt(vars.get("rechts"));
			vars.set("j", String.valueOf(rechts));
			lang.nextStep();
			
			exec("jForComp");
			lang.nextStep();
			
			while(Integer.parseInt(vars.get("j")) > Integer.parseInt(vars.get("links"))){
				int j = Integer.parseInt(vars.get("j"));
				exec("j_if");
				array.unhighlightElem(0, array.getLength()-1, null, null);
				array.highlightElem(j-1, j, null, null);
				lang.nextStep();								

				if(array.getData(j) < array.getData(j - 1)){
					
					exec("j_swap");
					array.swap(j-1, j, null, defaultTiming);
					lang.nextStep();
										
					exec("setCondition3");
					lang.nextStep();
				}										
				
				exec("jForInc");
				lang.nextStep();
				
				exec("jForComp");
				lang.nextStep();
			}
			array.unhighlightElem(0, array.getLength()-1, null, null);
			array.highlightCell(Integer.parseInt(vars.get("links")), null, null);
			lang.nextStep();
			
			exec("links_inc");
			lang.nextStep();
			
			if(!"0".equals(vars.get("condition"))){
				exec("whileComp");
				lang.nextStep();
			}
		}
		exec("return");
		
		for (int i = 0; i < array.getLength(); i++)
			array.highlightCell(i, null, null);
		
		lang.nextStep();
		iMarker.hide();
		jMarker.hide();
		lMarker.hide();
		rMarker.hide();
	}
	
//	@Override
	public String getAnnotatedSrc() {
		return "int [] BubleSort (int [] liste){						@label(\"header\")\n"			
		+	" int links = 0;											@label(\"l_marker\") @declare(\"int\", \"links\", \"0\") @inc(\""+zuweisung+"\")\n"
		+	" int rechts = liste.length - 1;							@label(\"r_marker\") @declare(\"int\", \"rechts\") @inc(\""+zuweisung+"\")\n"		
		+	" int i;													@label(\"i_marker\") @declare(\"int\", \"i\")\n"
		+	" int j;													@label(\"j_marker\") @declare(\"int\", \"j\")\n"
		+	" boolean condition = true;									@label(\"setCondition1\") @declare(\"int\", \"condition\", \"1\") @inc(\""+zuweisung+"\")\n"
		+	" while(condition){											@label(\"whileComp\") @inc(\""+vergleich+"\")\n"						
		+	" 	condition = false;										@label(\"resetCondition1\") @set(\"condition\", \"0\") @inc(\""+zuweisung+"\")\n"		
		+	" 	for(i = links;											@label(\"iForInit\") @inc(\""+zuweisung+"\")\n"					 
		+	" 		i < rechts;						 					@label(\"iForComp\") @continue @inc(\""+vergleich+"\")\n" 						
		+	" 			i++){											@label(\"iForInc\") @continue @inc(\"i\") @inc(\""+zuweisung+"\")\n"
		+	" 			if (liste[i].compareTo(liste[i+1]) > 0){		@label(\"i_if\") @inc(\""+vergleich+"\")\n"	
		+	" 				swap(liste, liste[i], liste[i+1]);			@label(\"i_swap\") @inc(\""+zuweisung+"\") @inc(\""+zuweisung+"\") @inc(\""+zuweisung+"\")\n"
		+	" 				condition = true;							@label(\"setCondition2\") @set(\"condition\", \"1\") @inc(\""+zuweisung+"\")\n"
		+	" 			}												@label(\"i_ifEnd\")\n"	
		+	" 		}													@label(\"iForEnd\")\n"		
		+	"		rechts--;											@label(\"rechts_dec\") @dec(\"rechts\") @inc(\""+zuweisung+"\")\n"									
		+	"  	for(j = rechts;	 										@label(\"jForInit\") @inc(\""+zuweisung+"\")\n" 
		+	" 		j > links; 											@label(\"jForComp\") @continue @inc(\""+vergleich+"\")\n" 
		+	" 			j--){											@label(\"jForInc\") @continue @dec(\"j\") @inc(\""+zuweisung+"\")\n"
		+	" 			if (liste[j].compareTo(liste[j-1]) < 0){		@label(\"j_if\") @inc(\""+vergleich+"\")\n" 		
		+	" 				swap(liste, liste[j], liste[j-1]);			@label(\"j_swap\") @inc(\""+zuweisung+"\") @inc(\""+zuweisung+"\") @inc(\""+zuweisung+"\")\n"
		+	" 				condition = true;							@label(\"setCondition3\") @set(\"condition\", \"1\") @inc(\""+zuweisung+"\")\n"
		+	" 			}												@label(\"j_ifEnd\")\n"
		+	" 		}													@label(\"jForEnd\")\n"
		+	" 		links++;											@label(\"links_inc\") @inc(\"links\") @inc(\""+zuweisung+"\")\n"
		+	" }															@label(\"loopEnd\")\n"	
		+	" return liste;												@label(\"return\")\n"
		+	"}															@label(\"end\")\n";
	}	
	
	public static void main(String args[]){
		
		AnnotatedShakerSort shakerSort = new AnnotatedShakerSort();
		
		shakerSort.arrayData = new int[]{7,3,2,4,1,13,52,13,5,1};
		
		shakerSort.initLocal();
		
		shakerSort.sort();
		
		System.out.println(shakerSort.lang.toString());	
	}

	@Override
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
				
		arrayData = (int[]) primitives.get("array");
		
		initLocal();
		
		sort();
		
		return lang.toString();
	}
	
	public void initLocal(){
		
		super.init();
		
		lang = new AnimalScript("ShakerSort","Paulin Nguimdoh",640,480);
		
		lang.setStepMode(true);
		
		tp1 = new TextProperties();
		tp1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
		tp1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.BOLD,35));
		
		
		tp2 = new TextProperties();
		tp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		tp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.BOLD,24));
		
		tp3 = new TextProperties();
		tp3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		tp3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.PLAIN,20));
		
		algoDescription();
		
		arrProp = new ArrayProperties();		
		arrProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		arrProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
		arrProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.black);
		arrProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.white);
		arrProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.green);
		array = lang.newIntArray(new Coordinates(20, 70), arrayData, "array", null, arrProp);
		
		i_zeiger = new ArrayMarkerProperties();		
		i_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		i_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);	
		iMarker = lang.newArrayMarker(array, 0, "iMarker", null, i_zeiger);
		amuI = new ArrayMarkerUpdater(iMarker, null, defaultTiming, array.getLength() - 1);
		
		j_zeiger = new ArrayMarkerProperties();		
		j_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
		j_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
		jMarker = lang.newArrayMarker(array, 0, "jMarker", null, j_zeiger);
		amuJ = new ArrayMarkerUpdater(jMarker, null, defaultTiming, array.getLength() - 1);
		
		links_zeiger = new ArrayMarkerProperties();		
		links_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "links");
		links_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.pink);
		lMarker = lang.newArrayMarker(array, 0, "lMarker", null, links_zeiger);
		amuL = new ArrayMarkerUpdater(lMarker, null, defaultTiming, array.getLength() - 1);
		
		rechts_zeiger = new ArrayMarkerProperties();		
		rechts_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "rechts");
		rechts_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.pink);
		rMarker = lang.newArrayMarker(array, 0, "rMarker", null, rechts_zeiger);
		amuR = new ArrayMarkerUpdater(rMarker, null, defaultTiming, array.getLength() - 1);
		
		tp4 = new TextProperties();
		tp4.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		tp4.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.BOLD,24));
		
		sc_prop = new SourceCodeProperties();
		sc_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		sc_prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
		sc_prop.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.cyan);
		sc_prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.BOLD,14));
		sourceCode = lang.newSourceCode(new Coordinates(20, 100), "sumupCode",null, sc_prop);
		
		// setup complexity
		vars.declare("int", vergleich); vars.setGlobal(vergleich);
		vars.declare("int", zuweisung); vars.setGlobal(zuweisung);
		
		Text text = lang.newText(new Coordinates(300, 20), "...", "complexity", null,tp4);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Vergleiche: ");
		tu.addToken(vars.getVariable(vergleich));
		tu.addToken(" - Zuweisungen: ");
		tu.addToken(vars.getVariable(zuweisung));
		tu.update();
		
		// parsing anwerfen
		parse();
				
	}
	
	public String getAnimationAuthor(){
		
		return "Paulin Nguimdoh";
	}

	@Override
	public String getAlgorithmName() {
		
		return "Shaker Sort";
	}
	
	@Override
	public Locale getContentLocale() {
		
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		
		return ALGO_BESCHREIBUNG;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		
		return "Shaker Sort Animation";
	}

	@Override
	public String getOutputLanguage() {
	
		return Generator.JAVA_OUTPUT;
	}
}
