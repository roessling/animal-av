package generators.cryptography.caesarcipher;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;


public class Caesar_A8 extends AnnotatedAlgorithm implements generators.framework.Generator {
	
	private boolean initialized_ = false;
	
	private static final String[] ALPHA_VECTOR;
	static {	
		String a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		ALPHA_VECTOR = new String[a.length()];	
		
		for(int i=0;i<a.length();i++) {
			ALPHA_VECTOR[i] = a.substring(i, i+1);
		}
	}
	
	
	@Override
	public String getName() {
		return "Caesar Chiffre";
	}
	
	

	@Override
	public String getAnnotatedSrc() {		
		return 
			"String caesarEncode(String in, int rot) {					@label(\"head\")											\n" + 
			"	StringBuffer out = new StringBuffer();					@label(\"var_out\") 	 									\n" +
			"	int i=0;												@label(\"var_i\") 		@declare(\"int\", \"i\", \"0\")		\n" +
			"	while(i<in.length() ) { 								@label(\"oWhileHead\")										\n" +
			"		out.append((in.charAt(i) - 'A' + rot) % 26 + 'A');	@label(\"append\") 											\n" +
			"		i++;												@label(\"oWhileInc\")	@inc(\"i\")							\n" +	
			"	}														@label(\"oWhileEnd\")										\n" +
			"	return out.toString();									@label(\"return\")											\n" +
			"}															@label(\"end\")												\n";
	}
	
	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		String msg = ((String) primitives.get("message")).toUpperCase();
		int rot = ((Integer) primitives.get("rot")) % ALPHA_VECTOR.length;
				
	    ArrayProperties apA = (ArrayProperties) props.getPropertiesByName("lookup");
	    ArrayProperties apT = (ArrayProperties) props.getPropertiesByName("io");
	   
	    
		String[] msgInArray = new String[msg.length()];
		String[] msgOutArray = new String[msg.length()];
		for(int i=0;i<msg.length();i++) {
			msgInArray[i]  = msg.substring(i, i+1); 
			msgOutArray[i] = "   "; 
		}
		
		StringArray defA = lang.newStringArray(new Coordinates(30, 60), ALPHA_VECTOR, "def", null, apA);
		StringArray repA = lang.newStringArray(new Coordinates(60, 60), ALPHA_VECTOR, "rep", null, apA);
		Text vT = lang.newText(new Coordinates(30, 30), "Verschiebung := 0" , "", null);
		
		StringArray inA = lang.newStringArray(new Coordinates(240, 60), msgInArray, "in", null, apT);
		StringArray outA = lang.newStringArray(new Coordinates(240, 160), msgOutArray, "out", null, apT);
		

		exec("head");
		lang.nextStep();

		for(int i=1;i<=rot;i++) {
			
			for(int j=0; j<ALPHA_VECTOR.length;j++) {
				repA.put(j, ALPHA_VECTOR[(j+i)%26], null, null);
			}			
			vT.setText("Verschiebung := "+i , null, null);
			
			lang.nextStep();
		}
		
		

		exec("var_out");
		lang.nextStep();
		
		exec("var_i");
		lang.nextStep();		
		
	
		for( ;; )
		{
			int i = Integer.parseInt(vars.get("i"));

			exec("oWhileHead");
			lang.nextStep();
			
			if( !(i < msg.length()) ) break;

			int c = msg.charAt(i) - 'A';
			

			exec("append");
			defA.highlightCell(c, null, null);
			repA.highlightCell(c, null, null);		
			inA.highlightCell(i, null, null);			
			outA.put(i, repA.getData(c), null, null);
			outA.highlightElem(i, null, null);			
			lang.nextStep();			
			

			exec("oWhileInc");
			defA.unhighlightCell(c, null, null);
			repA.unhighlightCell(c, null, null);
			inA.unhighlightCell(i, null, null);
			outA.unhighlightElem(i, null, null);
			lang.nextStep();		
		}
		
		exec("return");		
		lang.nextStep();

		
		return lang.toString();
	}


	

	@Override
	public String getAlgorithmName() {
		return "Caesar-Verschl\u00fcsselung";
	}


	@Override
	public String getAnimationAuthor() {
		return "Steven Lambeth";
	}


	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}


	@Override
	public String getDescription() {
		return "Ein einfaches Beispiel für die Caesar-Chiffre";
	}


	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}


	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType( GeneratorType.GENERATOR_TYPE_CRYPT );
	}


	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
	
	
	@Override
	public void init() 
	{		
		if (initialized_) {
			return;
		} else {
			initialized_ = true;
		}
		
		super.init();
		
		
		lang.newText(new Coordinates(240, 30), "Eingabe" , "", null);	
		lang.newText(new Coordinates(240, 130), "Ausgabe" , "", null);
		
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospace", Font.BOLD, 24));
				
		lang.newText(
				new Coordinates(10, 10)
			,	getAlgorithmName()
			,	"title"
			,	null
			,	tp
			);

		SourceCodeProperties props = new SourceCodeProperties();
		props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
		
		sourceCode = lang.newSourceCode(new Coordinates(240, 230), "code", null, props);
		
		parse();
	}


	public Caesar_A8() 
	{
		init();
	}
}
