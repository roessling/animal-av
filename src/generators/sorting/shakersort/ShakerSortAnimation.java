package generators.sorting.shakersort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class ShakerSortAnimation extends AnimatedIntArrayAlgorithm implements Generator{

	private int textCounter = 0;
	
	private Language language;
	
	private static ArrayProperties arrProp;
	
	private static ArrayMarkerProperties links_zeiger, rechts_zeiger, i_zeiger, j_zeiger;
	
	private static SourceCodeProperties sc_prop;
	
	private static TextProperties tp1;
	
	private static TextProperties tp2;
	
	private static TextProperties tp3;
	
	private static TextProperties tp4;
	
	private SourceCode sc;
	
	private static final String CODE_BESCHREIBUNG = 
		 "public void BubleSort (int [] liste)\n"
		+"{\n"				
		+	"int links = 0;\n"
		+	"int rechts = liste.length - 1;\n"
		+	"boolean vertauscht = true;\n"
		+	"while(vertauscht)\n"
		+	"{\n"
		+		"vertauscht = false;\n"
		+		"for(int i = links; i < rechts; i++)\n"
		+		"{\n"
		+			"if (liste[i].compareTo(liste[i+1]) > 0)\n"
		+			"{\n"
		+				"vertauscht = true;\n"
		+				"swap(liste, liste[i], liste[i+1]);\n"
		+			"}\n"		
		+		"}\n"
		+		"rechts--;\n"
		+		"for(int j = rechts; j < links; j--)\n"
		+		"{\n"
		+			"if (liste[j].compareTo(liste[j-1]) < 0)\n"
		+			"{\n"
		+				"vertauscht = true;\n"
		+				"swap(liste, liste[j], liste[j-1]);\n"		
		+			"}\n"
		+		"}\n"
		+		"links++;\n"
		+	"}\n"
		+"}";
	
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

	
	public void sort (int [] liste){
		
		//*************** Beschreibung des Algorithmus im Schrift *************************

		Text t0 = language.newText(new Coordinates(150, 20), "SHAKERSORT", "f1"+textCounter, null, tp1);
		textCounter++;
		language.nextStep();

		Text t1 = language.newText(new Coordinates(150, 60), "Beschreibung des Algorithmus", "f1"+textCounter, null, tp2);
		textCounter++;
		language.nextStep();

		Text t2 = language.newText(new Coordinates(20, 120), "1. Die zu sortierende Liste wird abwechselnd vom Beginn bzw. vom Ende her durchlaufen", "f1"+textCounter, null, tp3);
		textCounter++;
		language.nextStep();

		Text t3 = language.newText(new Coordinates(20, 160), "2. Vom Beginn her vergleicht man immer zwei benachbarte Elemente ", "f1"+textCounter, null, tp3);
		textCounter++;
		language.nextStep();

		Text t4 = language.newText(new Coordinates(20, 200), "3. Sind diese Elemente falsch sortiert, dann vertauscht man die", "f1"+textCounter, null, tp3);
		textCounter++;
		language.nextStep();

		Text t5 = language.newText(new Coordinates(20, 240), "4. Nach dem ersten Durchlauf ist das größte Element an seiner richtigen Position", "f1"+textCounter, null, tp3);
		textCounter++;
		language.nextStep();

		Text t6 = language.newText(new Coordinates(20, 280), "5. Danach geht man vom Ende her und vergleicht wiederum immer zwei benachbarte Elemente", "f1"+textCounter, null, tp3);
		textCounter++;
		language.nextStep();

		Text t7 = language.newText(new Coordinates(20, 320), "6. Sind diese falsch sortiert, dann vertauscht man die", "f1"+textCounter, null, tp3);
		textCounter++;
		language.nextStep();

		Text t8 = language.newText(new Coordinates(20, 360), "7. Nach dem zweiten Durchlauf, ist das kleinste Elemente an seiner richtigen Position", "f1"+textCounter, null, tp3);
		textCounter++;
		language.nextStep();

		Text t9 = language.newText(new Coordinates(20, 400), "8. Die Schritte 2 bis 7 wiederholt man bis alle Elemente sortiert sind.", "f1"+textCounter, null, tp3);
		textCounter++;
		language.nextStep();
		
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

		language.nextStep();
		
		// *********************************************************************************
		
		language.newText(new Coordinates(150, 20), "ShakerSort Animationsbeispiel", "f1"+textCounter, null, tp4);
		
		// IntArray-Objekt erzeugt, welches auf die Display-Eigenschaften verweist
		IntArray array = language.newIntArray(new Coordinates(20, 100), liste, "array", null, arrProp);
		
		// SourceCode Objekt erzeugen
		sc = language.newSourceCode(new Coordinates(20, 150),"code", null, sc_prop);
		
		// Zeilen im SourceCode hinzufügen
		sc.addCodeLine("public void ShakerSort (int [] liste)", null, 0, null);
		sc.addCodeLine("{", null, 0, null);												// 1
		sc.addCodeLine("int links = 0;", null, 1, null);								// 2
		sc.addCodeLine("int rechts = liste.length - 1;", null, 1, null);				// 3
		sc.addCodeLine("boolean vertauscht = true;", null, 1, null);					// 4
		sc.addCodeLine("while(vertauscht)", null, 1, null);								// 5
		sc.addCodeLine("{", null, 1, null);												// 6
		sc.addCodeLine("vertauscht = false;", null, 2, null);							// 7
		sc.addCodeLine("for(int i = links; i < rechts; i++)", null, 2, null);			// 8
		sc.addCodeLine("{", null, 2, null);												// 9
		sc.addCodeLine("if (liste[i].compareTo(liste[i+1]) > 0)", null, 3, null);		// 10
		sc.addCodeLine("{", null, 3, null);												// 11
		sc.addCodeLine("vertauscht = true;", null, 4, null);							// 12
		sc.addCodeLine("swap(liste, liste[i], liste[i+1]);", null, 4, null);				// 13
		sc.addCodeLine("}", null, 3, null);												// 14		
		sc.addCodeLine("}", null, 2, null);												// 15
		sc.addCodeLine("rechts--;", null, 2, null);										// 16
		sc.addCodeLine("for(int j = rechts; j < links; j--)", null, 2, null);			// 17
		sc.addCodeLine("{", null, 2, null);												// 18
		sc.addCodeLine("if (liste[j].compareTo(liste[j-1]) < 0)", null, 3, null);		// 19
		sc.addCodeLine("{", null, 3, null);												// 20
		sc.addCodeLine("vertauscht = true;", null, 4, null);							// 21
		sc.addCodeLine("swap(liste, liste[j], liste[j-1]);", null, 4, null);				// 22		
		sc.addCodeLine("}", null, 3, null);												// 23
		sc.addCodeLine("}", null, 2, null);												// 24
		sc.addCodeLine("links++;", null, 2, null);										// 25
		sc.addCodeLine("}", null, 1, null);												// 26
		sc.addCodeLine("}", null, 0, null);												// 27
		language.nextStep();
		
		shakerSort(array,sc);
	}
	
	public void shakerSort(IntArray array, SourceCode sourceCode){
		
		int links = 0; 
		int rechts = array.getLength()-1;
		boolean vertauscht = true;
		int i = 0, j = 0;
		// Highlight die erste line
		sourceCode.highlight(0);
		
		language.nextStep();
		
		sourceCode.toggleHighlight(0, 0, false, 2, 0);
		
		ArrayMarker links_Pointer = language.newArrayMarker(array, 0, "links",null, links_zeiger);
		
		language.nextStep();		
		sourceCode.toggleHighlight(2, 0, false, 3, 0);
		
		ArrayMarker rechts_Pointer = language.newArrayMarker(array, rechts, "rechts", null, rechts_zeiger);
		
		language.nextStep();
		sourceCode.toggleHighlight(3, 0, false, 4, 0);
		
		language.nextStep();
		sourceCode.toggleHighlight(4, 0, false, 5, 0);
		
		while (vertauscht){
			
			vertauscht = false;
			language.nextStep();
			sourceCode.toggleHighlight(5, 0, false, 7, 0);
			
			language.nextStep();
			sourceCode.toggleHighlight(7, 0, false, 8, 0);
			
			ArrayMarker i_Pointer = language.newArrayMarker(array, links, "i",null, i_zeiger);
			for(i = links; i < rechts; i++){
				
				language.nextStep();
				sourceCode.toggleHighlight(8, 0, false, 10, 0);				
				array.highlightElem(i, i+1, null, null);
				
				if(array.getData(i) > array.getData(i+1)){
					
					vertauscht = true;
					language.nextStep();
					sourceCode.toggleHighlight(10, 0, false, 12, 0);
					
					language.nextStep();
					sourceCode.toggleHighlight(12, 0, false, 13, 0);
					language.nextStep();
					array.swap(i, i+1, new TicksTiming(0), new TicksTiming(50));					
					
					language.nextStep();
					sourceCode.toggleHighlight(13, 0, false, 8, 0);				
					i_Pointer.move(i, new TicksTiming(0), new TicksTiming(50));
					
				}else{
					language.nextStep();
					sourceCode.toggleHighlight(10, 0, false, 8, 0);				
					i_Pointer.move(i+1, new TicksTiming(0), new TicksTiming(50));
				}
				array.unhighlightElem(i, i+1, null, null);
			}
			language.nextStep();			
			array.highlightCell(i, null, null);
			rechts--;
			language.nextStep();
			sourceCode.toggleHighlight(8, 0, false, 16, 0);
			rechts_Pointer.move(rechts, new TicksTiming(0), new TicksTiming(50));
			
			language.nextStep();
			sourceCode.toggleHighlight(16, 0, false, 17, 0);
			
			ArrayMarker j_Pointer = language.newArrayMarker(array, rechts, "j", null, j_zeiger);
			
			for(j = rechts; j > links; j--){
				
				language.nextStep();
				sourceCode.toggleHighlight(17, 0, false, 19, 0);				
				array.highlightElem(j-1, j, null, null);
				if(array.getData(j) < array.getData(j-1)){
					
					language.nextStep();
					sourceCode.toggleHighlight(19, 0, false, 21, 0);
					vertauscht = true;
					
					
					language.nextStep();
					sourceCode.toggleHighlight(21, 0, false, 22, 0);
					language.nextStep();
					array.swap(j-1, j, new TicksTiming(0), new TicksTiming(50));
					
					language.nextStep();
					sourceCode.toggleHighlight(22, 0, false, 17, 0);				
					j_Pointer.move(j, new TicksTiming(0), new TicksTiming(50));
				}else{
					language.nextStep();
					sourceCode.toggleHighlight(19, 0, false, 17, 0);				
					j_Pointer.move(j-1, new TicksTiming(0), new TicksTiming(50));
				}
				array.unhighlightElem(j-1, j, null, null);
			}
			language.nextStep();			
			array.highlightCell(j, null, null);
			links++;
			language.nextStep();
			sourceCode.toggleHighlight(17, 0, false, 25, 0);
			links_Pointer.move(links, new TicksTiming(0), new TicksTiming(50));
			
			language.nextStep();
			i_Pointer.hide();
			j_Pointer.hide();
			
			language.nextStep();
			sourceCode.toggleHighlight(25, 0, false, 5, 0);
		}
		language.nextStep();				
		sourceCode.unhighlight(5);
		
		language.nextStep();
		array.highlightCell(0, array.getLength()-1, null, null);
		
		language.nextStep();
		links_Pointer.hide();
		rechts_Pointer.hide();
		
		language.nextStep();
		sourceCode.hide();						
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.BOLD,40));
		language.newText(new Coordinates(150, 300), "FERTIG !", "f2", null, tp);
	}
	
	public static void main(String args[]){
		
		ShakerSortAnimation shakerSort = new ShakerSortAnimation();
		
		shakerSort.init();

		int[] myArray = new int[]{7,3,2,4,1,13,52,13,5,1};
		
		shakerSort.sort(myArray);
		
		System.out.println(shakerSort.language.toString());	
	}

	@Override
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		
		init();
		
		int [] arrayData = (int[]) primitives.get("array");
		
		arrProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,         props.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));
		arrProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, props.get("array", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
		arrProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,  props.get("array", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		arrProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, props.get("array", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
		arrProp.set(AnimationPropertiesKeys.FILL_PROPERTY,          props.get("array", AnimationPropertiesKeys.FILL_PROPERTY));
		
		i_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("i_Pointer", AnimationPropertiesKeys.COLOR_PROPERTY));
		i_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, props.get("i_Pointer", AnimationPropertiesKeys.LABEL_PROPERTY));
		
		j_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("j_Pointer", AnimationPropertiesKeys.COLOR_PROPERTY));
		j_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, props.get("j_Pointer", AnimationPropertiesKeys.LABEL_PROPERTY));
		
		links_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("links_Pointer", AnimationPropertiesKeys.COLOR_PROPERTY));
		links_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, props.get("links_Pointer", AnimationPropertiesKeys.LABEL_PROPERTY));
		
		rechts_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("rechts_Pointer", AnimationPropertiesKeys.COLOR_PROPERTY));
		rechts_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, props.get("rechts_Pointer", AnimationPropertiesKeys.LABEL_PROPERTY));				
		
		sc_prop.set(AnimationPropertiesKeys.BOLD_PROPERTY, 				props.get("sourceCode", AnimationPropertiesKeys.BOLD_PROPERTY));
		sc_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, 			props.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
		sc_prop.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, 		props.get("sourceCode", AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));
		sc_prop.set(AnimationPropertiesKeys.FONT_PROPERTY, 				props.get("sourceCode", AnimationPropertiesKeys.FONT_PROPERTY));
		sc_prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 	props.get("sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		sc_prop.set(AnimationPropertiesKeys.SIZE_PROPERTY, 				props.get("sourceCode", AnimationPropertiesKeys.SIZE_PROPERTY));
		
		sort(arrayData);
		
		return language.toString();
	}
	
	public void init(){
		
		language = new AnimalScript("ShakerSort","Paulin Nguimdoh",640,480);
		
		language.setStepMode(true);
		
		arrProp = new ArrayProperties();		
		arrProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		arrProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.gray);
		arrProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.black);
		arrProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.white);
		arrProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.green);
		
		i_zeiger = new ArrayMarkerProperties();		
		i_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		i_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);	
		
		j_zeiger = new ArrayMarkerProperties();		
		j_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
		j_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
		
		links_zeiger = new ArrayMarkerProperties();		
		links_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "links");
		links_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.pink);
		
		rechts_zeiger = new ArrayMarkerProperties();		
		rechts_zeiger.set(AnimationPropertiesKeys.LABEL_PROPERTY, "rechts");
		rechts_zeiger.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.pink);		
		
		tp1 = new TextProperties();
		tp1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
		tp1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.BOLD,35));
		
		tp2 = new TextProperties();
		tp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		tp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.BOLD,24));
		
		tp3 = new TextProperties();
		tp3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		tp3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.PLAIN,20));
		
		tp4 = new TextProperties();
		tp4.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		tp4.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.BOLD,35));
		
		sc_prop = new SourceCodeProperties();
		sc_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		sc_prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
		sc_prop.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.cyan);
		sc_prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.BOLD,14));
				
	}
	
	public String getAnimationAuthor(){
		
		return "Paulin Nguimdoh";
	}

	@Override
	public String getAlgorithmName() {
		
		return "Shaker Sort";
	}

	@Override
	public String getCodeExample() {
		
		return CODE_BESCHREIBUNG;
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
