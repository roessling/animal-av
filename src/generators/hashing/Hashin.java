package generators.hashing;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
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
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Hashin extends AnnotatedAlgorithm implements Generator {
	

	
	/**
	 * The concrete language object used for creating output
	 */
	private ArrayMarkerUpdater amuS;
	private int n;
//	private int pointerCounter = 0;
	private int []in ;
	private int []out ;
	private IntArray ia;
	private IntArray ib;
	private ArrayProperties arrayProps;
	private ArrayMarkerProperties markerProps;
	private SourceCodeProperties sourceCodeProps;
	private String comp = "Compares";
	private String assi = "Assignments";
	private Timing defaultTiming = new TicksTiming(100);
	private ArrayMarker sMarker;
	
	/**
	 * Default constructor
	 */
	public Hashin() {
		// Store the language object
		lang = new AnimalScript("Hashing", "kam", 640, 480);
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
		}
	
	public void init() {
	  // nothing to be done here
	}
	
	public void localInit(){
       super.init();
		
        sourceCode  = lang.newSourceCode(new Coordinates(200, 100), "sourceCode",
   	        null, sourceCodeProps);
		out=new int [n];
			
	     ia = lang.newIntArray(new Coordinates(20, 100), in, "intArray", 
					null, arrayProps);
		
	    
	    //arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,  Color.white);
	    
	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,  Color.WHITE);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
	    ib = lang.newIntArray(new Coordinates(600, 100), out, "intArray2", 
					null, arrayProps);
		
    // pointerCounter++;
	   sMarker = lang.newArrayMarker(ib, 0, "s", 
	   null, markerProps);
	   amuS = new ArrayMarkerUpdater(sMarker, defaultTiming, defaultTiming, ib.getLength()-1 );
	    
	    vars.declare("int", comp); vars.setGlobal(comp);
		vars.declare("int", assi); vars.setGlobal(assi);
		
		Text text = lang.newText(new Coordinates(300, 20), "...", "complexity", null);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Compares: ");
		tu.addToken(vars.getVariable(comp));
		tu.addToken(" - Assignments: ");
		tu.addToken(vars.getVariable(assi));
		tu.update();
		lang.nextStep();
		parse();
										
	}
	
	public void performHashing(){
	
		int k;
		exec("header");
		lang.nextStep();
		
		exec("i_temp");
		vars.set("Temp", Integer.toString (0) )  ;
		amuS.setVariable(vars.getVariable("Temp"));
		lang.nextStep();
		
		exec("vars_marker_j");
		ia.highlightElem (Integer.parseInt(vars.get("j")), null, null);
		lang.nextStep();
		
		exec("while");
		lang.nextStep();
		while (Integer.parseInt(vars.get("j"))<ia.getLength()){
			
	   exec("setze_i_temp");
	   boolean put =false;
	   
       lang.nextStep();
       while (!put){
       exec("brechne");
        k= in[Integer.parseInt(vars.get("j"))]+Integer.parseInt(vars.get("i"))%n;
        vars.set("Temp", Integer.toString (k%n) )  ; 
        lang.nextStep();
		
        exec("if"); 
        lang.nextStep();
        
        if(ib.getData(k%n)==0)
        {exec("eifuegen");lang.nextStep();
        ib.put(k%n, ia.getData(Integer.parseInt(vars.get("j"))), null, null);
        ib.highlightElem (k%n, null, null);
        lang.nextStep();
        exec("j++"); 
        ia.unhighlightElem (Integer.parseInt(vars.get("j"))-1, null, null);
        ia.highlightCell(Integer.parseInt(vars.get("j"))-1, null, null);
        ia.highlightElem (Integer.parseInt(vars.get("j")), null, null);
        put=true;
        lang.nextStep();
        }
        else{
        System.out.print(vars.get("i")+" "+vars.get("j")+"a");	
        exec("else");
        lang.nextStep();
        }}
        exec("while");
		lang.nextStep();	
			
		}
		exec("return"); 
        lang.nextStep();
	
		
	/*	
		for(int i=0;i<n;i++){Array.highlightElem(i, null, null);
			
	                      
	                      
	                     codeSupport.highlight(0, 0, false); 
	                     codeSupport.highlight(1,0,false);
	                     int spos=(Array.getData(i))%n;
	                     sMarker.move(spos, null, null);
	                     lang.nextStep();
	                     

	                     while (Array2.getData((spos)%n)!=0){
	                     codeSupport.unhighlight(0, 0, false); 
		                 codeSupport.unhighlight(1,0,false);
		                 
		                 codeSupport.highlight(2, 0, false); 
		                 lang.nextStep();	
		                 codeSupport.unhighlight(2, 0, false);
		                 codeSupport.highlight(4, 0, false);
		                 lang.nextStep();
		                 codeSupport.unhighlight(4, 0, false); 
	                     codeSupport.highlight(1,0,false);
	                     spos++;
	                     spos=spos%n;
	                     sMarker.move(spos, null, null);
	                     lang.nextStep();
	                     }
	                     codeSupport.unhighlight(0, 0, false); 
		                 codeSupport.unhighlight(1,0,false);
	                     codeSupport.highlight(2, 0, false); 
	                     lang.nextStep();
	                     codeSupport.unhighlight(2, 0, false);
	                     codeSupport.highlight(3, 0, false);  
	                     lang.nextStep();
	Array2.put(spos, Array.getData(i), null, null);
    Array2.highlightElem (spos, null, null);
    Array.unhighlightElem(i, null, null);
    Array.highlightCell(i, null, null);
    lang.nextStep();
     
    codeSupport.unhighlight(3,0,false);
    lang.nextStep();
	                     }*/
		                  
		
		
		
	
		
		
		
	}

	/* (non-Javadoc)
	 * @see generator.Generator#generate(generator.properties.AnimationPropertiesContainer, java.util.Hashtable)
	 */
	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
	    
		
		in = (int[]) arg1.get("in");
		n=in.length;
		arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");
		markerProps = (ArrayMarkerProperties) arg0.getPropertiesByName("markerProps");
		sourceCodeProps=(SourceCodeProperties) arg0.getPropertiesByName("sourceCodeProps");
		/*	sourceCodeProps=(SourceCodeProperties) arg0.getPropertiesByName("sourceCodeProps");
	 *  elementCOLOR =(Color) arg1.get("elementCOLOR");
		HIGHLIGHTelementCOLOR=(Color) arg1.get("HIGHLIGHTelementCOLOR");
		HIGHLIGHTcellCOLOR=(Color) arg1.get("HIGHLIGHTcellCOLOR");
		codeCOLOR=(Color) arg1.get("codeCOLOR");
		HIGHLIGHTCodeCOLOR=(Color) arg1.get("HIGHLIGHTCodeCOLOR");
		markerCOLOR=(Color) arg1.get("markerCOLOR");*/
		
		localInit(); 
		performHashing();
		return lang.toString();
	}
	
	@Override
	public String getAlgorithmName() {
    return "Hashing mit linearer Sondierung";
	}
	
	
	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}
	@Override
	public String getDescription() {
		return "Das Hashverfahren ist ein Algorithmus zum Suchen von Datenobjekten in großen Datenmengen. Es basiert auf der Idee," +"\n"
				+" dass eine mathematische Funktion die Position eines Objektes in einer Tabelle berechnet. Dadurch erübrigt sich die " +"\n"
				+"Durchsuchung vieler Datenobjekte, bis das Zielobjekt gefunden wurde.";
	}
	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}
	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}
	@Override
	public String getName() {
		return "Hashing";
	}
	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
	
	@Override
	public String getAnimationAuthor() {
		return "Saffar Kamel";
	}

	@Override
	public String getAnnotatedSrc() {
		return 
		"Hash(array1[0..size-1]){                 @label(\"header\")\n"+
		"i,temp                                   @label(\"i_temp\")@declare(\"int\", \"i\")@declare(\"int\", \"Temp\")\n"+
		"j=0                                      @label(\"vars_marker_j\") @declare(\"int\", \"j\", \"0\") @inc(\""+assi+"\")\n \n"+
		"while(j<array1.length()){                @label(\"while\")\n \n"+      
		"setze i=0,temp=0                         @label(\"setze_i_temp\")  @set(\"i\", \"0\") @set(\"Temp\", \"0\") @inc(\""+assi+"\") @inc(\""+assi+"\")\n"+
		"shritt 2 :brechne temp = (a[j]+i) mod m  @label(\"brechne\") @inc(\""+assi+"\")\n"+
		"falls array2[temp] frei ist              @label(\"if\")  @inc(\""+comp+"\")\n"+ 
		"      trage es dort,                     @label(\"eifuegen\")@inc(\""+assi+"\")\n"+
		"                     j++                 @label(\"j++\") @continue @inc(\"j\") @inc(\""+assi+"\")\n"+
		"      andernfalls ,i++ ,shritt 2 }       @label(\"else\") @inc(\""+assi+"\")  @inc(\"i\")\n" +   
	    "return array 2}                          @label(\"return\")";}
	
	
		
		
	
	}
	
	
	

