package generators.maths;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
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
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnnotatedSiebdesEratosthenes extends AnnotatedAlgorithm implements Generator{
	
	private String comp = "Compares";
	private String assi = "Assignments";
	
	private ArrayProperties arrayProps;
//	private TextProperties textProps;
	private SourceCodeProperties scProps;
	private ArrayMarkerProperties arrayMarkerProps_i;
    private ArrayMarkerProperties arrayMarkerProps_j;
    private ArrayMarkerProperties arrayMarkerProps_ij;
    
//	private int number;
	private int pointerCounteri = 0;
    private int pointerCounterj = 0;
    private int pointerCounterij = 0;
    
	private IntArray arrANI = null;
	private ArrayMarker iMarker; //,jMarker,ijMarker;
	private Timing t = new TicksTiming(50);
	private ArrayMarkerUpdater amuI, amuJ; //,amuIJ;
	
	
	@Override
	public String getAnnotatedSrc() {
		return "public void siebDesEratosthenes(int n){	@label(\"header\")\n"
				+ " for (int i = 2; 					@label(\"iForInit\") @declare(\"int\",\"i\", 2) @inc(\""+assi+"\")\n"
				+ "			 i <= Math.sqrt(n); 		@label(\"iForComp\") @continue @inc(\""+comp+"\")\n"
				+ "								i++){	@label(\"iForInc\")  @continue @inc(\"i\") @inc(\""+assi+"\")\n"
				+ "		for (int j = i; 				@label(\"jForInit\") @declare(\"int\",\"j\",0) @set(\"j\", \"i\") @inc(\""+assi+"\")\n"
				+ "				j <= ( n / i ); 		@label(\"jForComp\") @continue @inc(\""+comp+"\")\n"
				+ "								j++){	@label(\"jForInc\")  @continue @inc(\"j\") @inc(\""+assi+"\")\n"
				+ "			 i*j Element ist nicht Primzahl, streiche aus der Liste @label(\"if\")\n"
				+ "		}								@label(\"jForEnd\")\n"
				+ "	}									@label(\"iForEnd\")\n"
				+ "}									@label(\"end\")\n";
				
	}
	
	public void localInit() {
		
	    //		super.init();
		sourceCode= lang.newSourceCode(new Coordinates(40, 200), "sourceCode",null, scProps);
		
		// setup complexity
	    vars.declare("int", comp); 
	    vars.setGlobal(comp);
		vars.declare("int", assi); 
		vars.setGlobal(assi);
		
		Text text = lang.newText(new Coordinates(15, 30), "Das Sieb des Eratosthenes", "complexity", null);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Compares: ");
		tu.addToken(vars.getVariable(comp));
		tu.addToken(" - Assignments: ");
		tu.addToken(vars.getVariable(assi));
		tu.update();
		
		// parsing anwerfen
		parse();
	}
	
	public void start(int a) {
		int[] inArray = new int[a];
	    for(int i=0; i<inArray.length;i++){
				inArray[i]=i;
		}
	    
	    arrANI = lang.newIntArray(new Coordinates(30, 150), inArray, "arrANI", null, arrayProps);
				
		arrayMarkerProps_i = new ArrayMarkerProperties();
	    arrayMarkerProps_i.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");   
	    arrayMarkerProps_i.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
	    iMarker = lang.newArrayMarker(arrANI, 0, "i" + pointerCounteri, null, arrayMarkerProps_i);
	    amuI = new ArrayMarkerUpdater(iMarker, null, t, arrANI.getLength() - 1); 
	    
	    arrayMarkerProps_j = new ArrayMarkerProperties();
	    arrayMarkerProps_j.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");   
	    arrayMarkerProps_j.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
	    ArrayMarker jMarker = lang.newArrayMarker(arrANI, 0, "j" + pointerCounterj, null, arrayMarkerProps_j);
	    amuJ = new ArrayMarkerUpdater(jMarker, null, t, arrANI.getLength() - 1);   
	    
	    arrayMarkerProps_ij = new ArrayMarkerProperties();
	    arrayMarkerProps_ij.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i*j");   
	    arrayMarkerProps_ij.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.MAGENTA);
	    ArrayMarker ijMarker = lang.newArrayMarker(arrANI, 0, "ij" + pointerCounterij, null, arrayMarkerProps_ij);
	    new ArrayMarkerUpdater(ijMarker, null, t, arrANI.getLength() - 1); 
	    
	    lang.nextStep();
	    arrANI.highlightElem(0, null, t);
	    arrANI.highlightElem(1,null, t);
	    lang.nextStep();
	    
	    arrANI.highlightElem(0, t, t);
	    arrANI.highlightElem(1,t , t);
	    lang.nextStep();
	    
	    exec("header");
	    lang.nextStep();
	    
	    exec("iForInit");
	    System.err.println(vars.getVariable("i"));
	    amuI.setVariable(vars.getVariable("i"));
	    lang.nextStep();
	    
	    exec("iForComp");
	    vars.set("n", String.valueOf(arrANI.getLength()-1));
	    lang.nextStep();
	    
	    int m = Integer.parseInt(vars.get("n"));
	    while(Integer.parseInt(vars.get("i")) <= Math.sqrt( arrANI.getData(m))){
	    	iMarker.move(Integer.parseInt(vars.get("i")), null, null);
    		lang.nextStep();
	
    		exec("jForInit");
    		amuJ.setVariable(vars.getVariable("j"));
    		lang.nextStep();
   
    		exec("jForComp");
    		lang.nextStep();
    		
    		while(Integer.parseInt(vars.get("j"))<= arrANI.getData(m) / Integer.parseInt(vars.get("i"))){
    			jMarker.move(Integer.parseInt(vars.get("j")), null, null);
        		lang.nextStep();
        		
        		exec("if");
        		lang.nextStep();
        		
        		if (Integer.parseInt(vars.get("i"))*Integer.parseInt(vars.get("j"))<=arrANI.getData(m)){      			
        			ijMarker.move(Integer.parseInt(vars.get("i"))*Integer.parseInt(vars.get("j")), null, null);
	                arrANI.highlightElem(Integer.parseInt(vars.get("i"))*Integer.parseInt(vars.get("j")),null , null);                
	                lang.nextStep();
        		}
        		
        		exec("jForInc");
        		lang.nextStep();
        		exec("jForComp");
        		lang.nextStep();
    		}
    		
    		exec("iForInc");
    		lang.nextStep();
    		exec("iForComp");
    		lang.nextStep();   		
	    }
	        
	}
	
	
	
	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		Integer number = (Integer) primitives.get("int");
//		textProps = (TextProperties)props.getPropertiesByName("text");
		arrayProps = (ArrayProperties)props.getPropertiesByName("array");
		scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		localInit();
		
		start(number);
		return lang.toString();
	}

	@Override
	public String getAnimationAuthor() {
		return "Xiaofan Fan";
	}
	
	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
	
	@Override
	public String getAlgorithmName() {
		
		return "Das Sieb des Eratosthenes";
	}

	
	@Override
	public Locale getContentLocale() {
		
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		
		return "Das ist ein neues Beispiel von dem neuen Annotationsystem.";
	}

	@Override
	public GeneratorType getGeneratorType() {
		
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS) ;
	}

	@Override
	public String getName() {
		
		return "Das Sieb des Eratosthenes";
	}

	
	
}