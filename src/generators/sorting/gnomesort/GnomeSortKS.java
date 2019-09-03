
package generators.sorting.gnomesort;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class GnomeSortKS extends AnnotatedAlgorithm implements Generator{
	
//	private int pointerCounter = 0;
	private ArrayProperties arrayProps;
	private ArrayMarkerProperties markerProps;
	private ArrayMarkerProperties markerProps2;
	private SourceCodeProperties sourceCodeProps;
	private int []Array ;
//	private Text  pseudoCode;
	
	private IntArray ia;
	
	private String comp = "Compares";
	private String assi = "Assignments";
	private Timing defaultTiming = new TicksTiming(100);
	private ArrayMarkerUpdater amuI;
	private ArrayMarkerUpdater amuJ;
	private ArrayMarker iMarker, jMarker;
	
	/*public GnomeSortKS()
	{
		lang = new AnimalScript("GnomeSort", "kam", 640, 480);
		lang.setStepMode(true);
	}
	*/
	@Override
	    public void init() { }
	public void localInit() {
		super.init();
		
		sourceCode= lang.newSourceCode(new Coordinates(200, 180), "sumupCode",null, sourceCodeProps);
		
		ia  = lang.newIntArray(new Coordinates(200, 100), Array, "ia", null, arrayProps);
		
		
		/*pseudoCode = lang.newText(new Coordinates(200, 150), "Pseudo Code", "pseudocode", null);
		pseudoCode.setFont(new Font ("Serif", Font.BOLD, 24), null, null);
	  	*/	
		
		
		
//		pointerCounter++;
		iMarker = lang.newArrayMarker(ia, 1, "iMarker", null, markerProps);
		amuI = new ArrayMarkerUpdater(iMarker, null, defaultTiming, ia.getLength()-1 );
		
//		pointerCounter++;
		jMarker = lang.newArrayMarker(ia, 2, "jMarker" , null, markerProps2);
		amuJ = new ArrayMarkerUpdater(jMarker, null, defaultTiming, ia.getLength()-1);
		
		
		// setup complexity
		vars.declare("int", comp); vars.setGlobal(comp);
		vars.declare("int", assi); vars.setGlobal(assi);
		
		Text text = lang.newText(new Coordinates(300, 20), "...", "complexity", null);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Compares: ");
		tu.addToken(vars.getVariable(comp));
		tu.addToken(" - Assignments: ");
		tu.addToken(vars.getVariable(assi));
		tu.update();
		// parsing anwerfen
		
		
		
		
		// parsing anwerfen
		parse();	
		
		
}
	public void Sort(){
		 

		
		
		
		
		
		exec("header");
		lang.nextStep();
		
		exec("vars_marker_i");
		amuI.setVariable(vars.getVariable("i"));
		lang.nextStep();
		
		exec("vars_marker_j");
		amuJ.setVariable(vars.getVariable("j"));
		lang.nextStep();
		
		exec("while");
		lang.nextStep();
		
        
		while (Integer.parseInt(vars.get("i"))<ia.getLength()){
	    	
		exec("if");	
		lang.nextStep();
		if (ia.getData(Integer.parseInt(vars.get("i")) - 1) <= ia.getData(Integer.parseInt(vars.get("i"))))
		{exec("i_equal_j");lang.nextStep();
		 exec("j++");lang.nextStep();
		}
		else
		{exec("else");
		lang.nextStep();
		
		exec("i--");lang.nextStep();
		exec("swap");
		ia.swap(Integer.parseInt(vars.get("i")) , Integer.parseInt(vars.get("i"))+1, null, defaultTiming);
		lang.nextStep();
		
		
		
		
		exec("if2");
		lang.nextStep();
		
		if (Integer.parseInt(vars.get("i"))==0)
		{ exec("i_equal_j_1");lang.nextStep();
		 exec("j++_1");lang.nextStep();}
		}
		exec("while");
		lang.nextStep();
		
		}exec("end");
		lang.nextStep();
		
		
		
		
		
		/*sc.highlight(0);
		lang.nextStep();
		
		int i= 1;
		int j =2;
		sc.unhighlight(0);
		sc.highlight(1);
		sc.highlight(2);
		lang.nextStep();
		
		while (i < ia.getLength()){
		sc.unhighlight(1);
		sc.unhighlight(2);
		
		
		sc.highlight(3);
		lang.nextStep();
		
		if (ia.getData(i-1) <= ia.getData(i))
		{
		 sc.unhighlight(3);
		 sc.highlight(4);
		 lang.nextStep();
		 
		 sc.unhighlight(4);
		 sc.highlight(5);
		 sc.highlight(6);
		 i = j;
		 j++;
         lang.nextStep();
		 
         if (i>=ia.getLength())
      		iMarker.moveOutside(null, null);
      		else iMarker.move(i, null, null);
      		
         
         
         
         if (j>=ia.getLength())
			 jMarker.moveOutside(null, null);	
		
		else jMarker.move(j, null, null);;
     		lang.nextStep();
		 
		 
		 sc.unhighlight(5);
		 sc.unhighlight(6);
		}
		else
		{
		sc.unhighlight(3);
		sc.highlight(7);
		lang.nextStep();
		
		sc.unhighlight(7);
		sc.highlight(8);
		lang.nextStep();
		
		ia.swap(i-1,i , null, null);
		lang.nextStep();
		
		sc.unhighlight(8);
		sc.highlight(9);
		i--;
		lang.nextStep();
		
		iMarker.move(i, null, null);
		lang.nextStep();
		
		sc.unhighlight(9);
		sc.highlight(10);
		lang.nextStep();
		
		if (i==0)
		{
		sc.unhighlight(10);	
		sc.highlight(11);
		sc.highlight(12);
		i=j;
		j++;
		lang.nextStep();
		
		if (i>=ia.getLength())
      		iMarker.moveOutside(null, null);
      		else iMarker.move(i, null, null);
      		
		
		if (j>=ia.getLength())
			 jMarker.moveOutside(null, null);	
		
		else jMarker.move(j, null, null);
		lang.nextStep();
		
		sc.unhighlight(11);
		sc.unhighlight(12);
		lang.nextStep();
		}
		sc.unhighlight(10);
		
		}		
}	
		lang.nextStep();	*/	
		
		
		
		
		
	}

	
	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		
		Array = (int[]) arg1.get("Array");
		arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");
		markerProps = (ArrayMarkerProperties) arg0.getPropertiesByName("markerProps");
		markerProps2 = (ArrayMarkerProperties) arg0.getPropertiesByName("markerProps2");
		sourceCodeProps=(SourceCodeProperties) arg0.getPropertiesByName("sourceCodeProps");
		localInit(); 
		Sort();
		
		
		return lang.toString();
	}
	
	

	@Override
	public String getAlgorithmName() {
	
		return "Gnome Sort";
	}

	@Override
	public String getAnimationAuthor() {
	
		return "Saffar Kamel";
	}

/*	@Override
	public String getCodeExample() {
	
		return " " +"\n"
+"function gnomeSort(a[0..size-1]) {" +"\n"
+" i := 1"+"\n"
+" j := 2"+"\n"
+" while i < size"+"\n"
+"    if a[i-1] <= a[i] "+"\n"
+"        i := j"+"\n"
+"        j := j + 1 "+"\n"
+"    else"+"\n"
+"        swap a[i-1] and a[i]"+"\n"
+"        i := i - 1"+"\n"
+"        if i = 0"+"\n"
+"          i := j"+"\n"
+"          j := j + 1"+"\n"
+" }";

	}*/

	@Override
	public Locale getContentLocale() {
		
		return Locale.US;
	}

	@Override
	public String getDescription() {
		
		return "The simplest sort algorithm is not Bubble Sort..., it is not Insertion Sort..., it's Gnome Sort!"+"\n"
		       +"Gnome Sort is based on the technique used by the standard Dutch Garden Gnome (Du.: tuinkabouter)."+"\n"
		       +"Here is how a garden gnome sorts a line of flower pots. Basically, he looks at the flower pot next to him and the previous one;"+"\n"
		       +"if they are in the right order he steps one pot forward, otherwise he swaps them and steps one pot backwards. Boundary conditions: "+"\n"
		       +"if there is no previous pot, he steps forwards; if there is no pot next to him, he is done."+"\n"
		       +"Dick Grune";
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
		
		return "GnomeSort";
	}

	@Override
	public String getOutputLanguage() {
		
		 return Generator.PSEUDO_CODE_OUTPUT;
	}
	

	@Override
	public String getAnnotatedSrc() {
		
		  return "function gnomeSort(a[0..size-1]) {      @label(\"header\")\n"
			+" i := 1                                 @label(\"vars_marker_i\") @declare(\"int\", \"i\", \"1\") @inc(\""+assi+"\")\n"
			+" j := 2                                 @label(\"vars_marker_j\") @declare(\"int\", \"j\", \"2\") @inc(\""+assi+"\")\n"
			+" while i < size                         @label(\"while\")  @inc(\""+comp+"\")\n"
			+"    if a[i-1] <= a[i]                   @label(\"if\")@inc(\""+comp+"\")\n"
			+"        i := j                          @label(\"i_equal_j\") @inc(\""+assi+"\") @eval(\"i\", \"j\")\n"
			+"        j := j +1                       @label(\"j++\") @inc(\""+assi+"\") @inc(\"j\")\n"
			+"    else                                @label(\"else\")\n"
			+"        i := i - 1                      @label(\"i--\") @inc(\""+assi+"\") @dec(\"i\")\n"
			+"        swap a[i] and a[i+1]            @label(\"swap\") @inc(\""+assi+"\") @inc(\""+assi+"\") @inc(\""+assi+"\")\n"
			+"        if i = 0                        @label(\"if2\")@inc(\""+comp+"\")\n"
			+"          i := j                        @label(\"i_equal_j_1\") @inc(\""+assi+"\") @eval(\"i\", \"j\")\n"
			+"          j := j + 1                    @label(\"j++_1\") @inc(\""+assi+"\") @inc(\"j\")\n"
			+" }                                      @label(\"end\")";
			
  

	}}
