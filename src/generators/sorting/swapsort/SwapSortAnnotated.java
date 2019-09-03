package generators.sorting.swapsort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

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

public class SwapSortAnnotated extends AnnotatedAlgorithm implements Generator{
	
	private String ase = "Amount of smaller elements";
	private String as = "Amount of SWAP operations";
	
	private int[] arrayData;
	private IntArray array = null;
	private ArrayMarker Marker_Index, Marker_Small;
	private Timing defaultTiming = new TicksTiming(5);
	
	private ArrayMarkerUpdater amuI, amuJ;
	
	@Override
	public String getAnnotatedSrc() {
		return 
		"public void swapSort(int[] array) { 		@label(\"header\") \n" +
		"   int startIndex = 0, smaller; 			@label(\"startInit\") @declare(\"int\", \"startIndex\", \"0\") @declare(\"int\", \"smaller\")  \n" +
		"   while(startIndex < array.length - 1) {	@label(\"while\") \n" +
		"     smaller = 							@label(\"small\") \n" +
		"			countSmallerOnes(array , startIndex); @label(\"helpFunc\") @continue \n" +
		"     if(smaller > 0) {						@label(\"if1\") \n" +
		"       swap(startIndex , startIndex + smaller); @label(\"swap\") @inc(\""+as+"\") \n" +
		"     } 									@label(\"if1End\")\n" +
		"     else {								@label(\"else1\") \n" +
		"       startIndex++;						@label(\"startIndexInc\") @inc(\"startIndex\")\n" +
		"    }										@label(\"else1End\") \n" +
		"  }										@label(\"whileEnd\")\n" + 
		"}											@label(\"end\")\n" +
		"private int countSmallerOnes(final int[] array, final int index) { @label(\"headerHelpFunc\")\n" +
		"  int counter = 0, i;							@label(\"var_declares\") @declare(\"int\", \"i\") @set(\""+ase+"\", \"0\") \n" +
		"  for (i = index + 1; 							@label(\"forInit\")  \n" +
		"			i < array.length; 					@label(\"forComp\") @continue \n" +
		"				i++) { 							@label(\"forInc\") @continue @inc(\"i\")  \n" +
		"    if(array[index] > array[i]){				@label(\"if2\") \n" +
		"      counter++;								@label(\"counterInc\")  @inc(\""+ase+"\")\n" +
		"    }											@label(\"if2End\") \n" +
		"  }											@label(\"forEnd\")\n" +
		"  return counter;								@label(\"return\")\n" +
		"}												@label(\"endHelp\") ";
	}
	
	@Override
	public String getCodeExample(){
	return "" +
	"0. public void swapSort(int[] array) {" + "\n" +
	"1.   int startIndex = 0;" + "\n" +
	"2.   while(startIndex < array.length - 1) {" + "\n" +
	"3.     int smaller = countSmallerOnes(array , startIndex);" + "\n" +
	"4.     if(smaller > 0) {" + "\n" +
	"5.       swap(startIndex , startIndex + smaller);" + "\n" +
	"6.     }" + "\n" +
	"7.     else {" + "\n" +
	"8.       startIndex++;" + "\n" +
	"9.    }" + "\n" +
	"10. }" + "\n" + 
	"11.}" + "\n" +
	"12." + "\n" +
	"13.private int countSmallerOnes(final int[] array, final int index) {" + "\n" +
	"14.  int counter = 0;" + "\n" +
	"15.  for (int i = index + 1; i < array.length; i++) {" + "\n" +
	"16.    if(array[index] > array[i]){" + "\n" +
	"17.      counter++;" + "\n" +
	"18.    }" + "\n" +
	"19.  }" + "\n" +
	"20.  return counter;" + "\n" +
	"21.}";
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		super.init();
		TextProperties tProp = new TextProperties();
		tProp.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced",Font.PLAIN,25));
//		Text head = 
		lang.newText(new Coordinates(20,30), "Swap Sort", "title", null,tProp);
//		Text head = lang.newText(new Coordinates(20, 30), "Selection Sort", "...", null);
		
		arrayData = (int[]) primitives.get("intArray");
		
		
		sourceCode = lang.newSourceCode(new Coordinates(20,145), "sourceCode", null, (SourceCodeProperties) props.getPropertiesByName("sourceCode"));
		array = lang.newIntArray(new Coordinates(20, 120), arrayData, "intArray", null, (ArrayProperties) props.getPropertiesByName("intArray"));
		
		Marker_Index = lang.newArrayMarker(array, 0, "startIndex", null,(ArrayMarkerProperties) props.getPropertiesByName("Marker_Index"));
		Marker_Small = lang.newArrayMarker(array, 0, "i", null,(ArrayMarkerProperties) props.getPropertiesByName("Marker_Small"));
		
		amuI = new ArrayMarkerUpdater(Marker_Index, null, defaultTiming, array.getLength() - 1);
		amuJ = new ArrayMarkerUpdater(Marker_Small, null, defaultTiming, array.getLength() - 1);
		 
		vars.declare("int", ase); vars.setGlobal(ase);
		vars.declare("int", as); vars.setGlobal(as);
		
		
		Text text = lang.newText(new Coordinates(170, 120), "...", "complexity", null);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Amount of smaller elements:");
		tu.addToken(vars.getVariable(ase));
		tu.addToken(" - Amount of SWAP operations:");
		tu.addToken(vars.getVariable(as));
		tu.update();
		
		parse();
		
		sort();
		
		return lang.toString();
	}

	private void sort() {
		exec("header");
		lang.nextStep();
		
		exec("startInit");
		amuI.setVariable(vars.getVariable("startIndex"));
		lang.nextStep();
		
		exec("while");
		lang.nextStep();
		
		while(Integer.parseInt(vars.get("startIndex")) < array.getLength()-1) {
			
			exec("small");
			lang.nextStep();
			
			exec("helpFunc");
			lang.nextStep();
			
			
			countSmallerOnes();
			
			vars.set("smaller",vars.get(ase));
			
			
			exec("if1");
			lang.nextStep();
			
			if(Integer.parseInt(vars.get("smaller")) > 0) {
				exec("swap");
				lang.nextStep();
				
				array.swap(Integer.parseInt(vars.get("startIndex")), Integer.parseInt(vars.get("startIndex"))+Integer.parseInt(vars.get("smaller")), defaultTiming, null);
				
				
			}
			else {
				exec("else1");
				lang.nextStep();
				array.highlightCell(Integer.parseInt(vars.get("startIndex")), defaultTiming, null);
				exec("startIndexInc");
				lang.nextStep();
			}
			
			exec("else1End");
			lang.nextStep();
			
			exec("while");
			lang.nextStep();
			
		}
		exec("whileEnd");
		lang.nextStep();
		
		exec("end");
		lang.nextStep();
		
	}

	private void countSmallerOnes() {
		exec("headerHelpFunc");
		lang.nextStep();
		
		exec("var_declares");
		lang.nextStep();
		
		exec("forInit");
		vars.set("i", String.valueOf(Integer.parseInt(vars.get("startIndex"))+1));
		amuJ.setVariable(vars.getVariable("i"));
		lang.nextStep();
		
		exec("forComp");
		lang.nextStep();
		
		while(Integer.parseInt(vars.get("i")) < array.getLength()) {
			exec("if2");
			lang.nextStep();
				
			if(array.getData(Integer.parseInt(vars.get("startIndex"))) > array.getData(Integer.parseInt(vars.get("i")))) {
				exec("counterInc");
				lang.nextStep();
			}
			exec("if2End");
			lang.nextStep();
			
			exec("forInc");
			lang.nextStep();
						
			exec("forComp");
			lang.nextStep();
		}
		
		exec("forEnd");
		lang.nextStep();

		exec("return");
		lang.nextStep();
		
	}

	@Override
	public String getAlgorithmName() {
		return "Swap Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Enkh-Amgalan Ganbaatar, Martin Tjokrodiredjo";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Swap sort is a sorting algorithm, " +
		"specifically an in-place comparison sort. " +
		"It has O(n2) complexity, making it inefficient on large lists, " +
		"and generally performs worse than the similar insertion sort. " +
		"Swap sort is noted for its simplicity, " +
		"and also has performance advantages over more " +
		"complicated algorithms in certain situations."+
		" source: www.en.wikipedia.org";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return "Swap Sort [Annotated]";
	}

	@Override
	public String getOutputLanguage() {
		return JAVA_OUTPUT;
	}
	
}