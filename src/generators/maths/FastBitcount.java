package generators.maths;


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


public class FastBitcount extends AnnotatedAlgorithm implements generators.framework.Generator {

	
	//required for preview-sources
	private int sourceCodeIndex_ = 1;
	
	
	@Override
	public String getName() {
		return "Fast bitcount";
	}
	
	private final String DESC_NAIVE = "Zusammenfassend: Intuitiv, einfach und langsam :( Aber es geht auch besser :)";
	
	private boolean initialized_ = false;

	private void setBitArray(int n, int q, int pp, StringArray a) {
		setBitArray(n, q, pp, a, false);
	}
	
	private void setBitArray(int n, int q, int pp, StringArray a, boolean init) {
		
		int m = n;
		
		boolean[] touched = new boolean[a.getLength()];

		String[] old = new String[a.getLength()];
		for (int i = 0; i < old.length; i++)
		    old[i] = a.getData(i);
		
		for(int i=0; i<a.getLength(); i++) {
			touched[i] = init;
		}
		
		for(int i=0; i<a.getLength(); i+=q+1) {		
			
			for(int j=0;i+j<a.getLength() && j<q;j+=pp){
				int idx=a.getLength()-(i+j+1);
				String val = Integer.toString(m & ((1<<pp)-1));
				touched[idx] = true;
				if( init || !val.equals(old[idx]) ) {
					a.put( idx, val, null, null ); 
				}
				m >>>= pp;
			}
		}		
		for(int i=0; i<a.getLength(); i++) {
			if( !touched[i] && !old[i].equals(" ")) {
				a.put( i, " ", null, null );
			}
		}
	}

	
	@Override
	public String getAnnotatedSrc() {		
		String[] src = new String[2];
		
		src[0] =
			"int count(T v) {											@label(\"head\")		\n" + 
			"	int c=0;												@label(\"var_c\") 		\n" +
			"	while( v ) { 											@label(\"oWhileHead\")	\n" +
			"		c += (v & 1);										@label(\"add\") 		\n" +
			"		v >>= 1;											@label(\"shift\")		\n" +	
			"	}														@label(\"oWhileEnd\")	\n" +
			"	return c;												@label(\"return\")		\n" +
			"}															@label(\"end\")			\n";
	
		src[1] =
			"const T M55 = (T)~(T)0/3;       			/* 0x5555...55 */ @label(\"const_M55\")\n"+
			"const T M33 = (T)~(T)0/15*3;               /* 0x3333...33 */ @label(\"const_M55\")\n"+
			"const T M0F = (T)~(T)0/255*15;             /* 0x0F0F...0F */ @label(\"const_M55\")\n"+
			"const T M01 = (T)~(T)0/255;                /* 0x0101...01 */ @label(\"const_M55\")\n\n"+
			
			"int count(T v) {											@label(\"head\")	\n" +
			"	v = v - ((v >> 1) & M55);								@label(\"bit2\")	\n" +
			"	v = (v & M33) + ((v >> 2) & M33);						@label(\"bit4\")	\n" +
			"	v = (v + (v >> 4)) & M0F;								@label(\"bit8\")	\n" +
			"	return (int) ((v * M01) >> (sizeof(v) - 1) * CHAR_BIT);	@label(\"return\")	\n" +
			"}															@label(\"end\")		\n";
			
		
		return src[sourceCodeIndex_];
	}
	
	public void parse(int sourceCodeIndex) {
		sourceCodeIndex_ = sourceCodeIndex;		
		super.parse();
	}
	
	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		int in = (Integer) primitives.get("in");
	    String[] init = new String[] {
	    		"?","?","?","?","?","?","?","?",
	    		"?","?","?","?","?","?","?","?",
	    		"?","?","?","?","?","?","?","?",
	    		"?","?","?","?","?","?","?","?"
	    	};
	    
		TextProperties title = new TextProperties();
		title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 28));
	    
		TextProperties subTitle = new TextProperties();
		subTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 22));		
		
		TextProperties description = new TextProperties();
		description.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 16));	

		Text subtitle = lang.newText(new Coordinates(10, 50), "", "sub", null, subTitle);
	    
		Text desc = lang.newText(new Coordinates(10, 240), "", "desc", null, description);
    
		ArrayProperties grid = new ArrayProperties();
		grid.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		grid.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.LIGHT_GRAY);
		grid.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    
		
	    StringArray value = lang.newStringArray(new Coordinates(30,  90), init, "value", null, grid);
		Text count = lang.newText(new Coordinates(540, 150), "Bitcount := ?", "cnt", null, new TextProperties());
	    
		
	    sourceCode = lang.newSourceCode(new Coordinates(30, 150), "source_n", null, (SourceCodeProperties)props.getPropertiesByName("sourceCode"));
    	parse(0);  	
	    
	    
	    subtitle.setText("Die intuitive Heransgehensweise", null, null);
    	value.highlightCell(init.length-1, null, null);

    	exec("head");
	    lang.nextStep();

	    exec("var_c");
    	setBitArray(in, 999, 1, value);
    	count.setText("Bitcount := 0", null, null);
    	lang.nextStep();
    	
    	int cnt=0,v=in;
    	for(;;) {
    		
    	    exec("oWhileHead");
    		lang.nextStep();
    		
    		if (v==0) break;
    		

    	    exec("add");
    	    cnt += v & 1;	    	
	    	if( (v & 1) == 1 ) {
	    		value.highlightElem(init.length-1, null, null);
	    	}	    	
	    	count.setText("Bitcount := "+cnt, null, null);
    		lang.nextStep();
    		
    		exec("shift");
	    	v>>>=1; 
	    	setBitArray(v, 999, 1, value);
	    	lang.nextStep();
    	}
    		    

    	exec("return");
	    value.unhighlightCell(init.length-1, null, null);
	    setBitArray(v, 999, 1, value);
	    lang.nextStep();

	    sourceCode.hide();
	    lang.nextStep();
	    
	    desc.setText(DESC_NAIVE, null, null);
	    
	    
	    lang.nextStep();
	    
	    count.hide();
	    value.unhighlightCell(init.length-1, null, null);
	    value.hide();
	    subtitle.setText("Der parallele (bithack) Ansatz", null, null);
	    desc.setText("", null, null);
	    lang.nextStep();
	    
		
	    sourceCode = lang.newSourceCode(new Coordinates(30, 150), "source_n", null, (SourceCodeProperties)props.getPropertiesByName("sourceCode"));
    	parse(1);
    	
    	
    	lang.nextStep();
	    
    	exec("head");
    	v=in;
    	setBitArray(v, 999, 1, value, true);
    	lang.nextStep();
    	
	    
    	exec("bit2");
    	v = v - ((v >>> 1) & 0x55555555);
    	setBitArray(v, 999, 2, value);
    	lang.nextStep();
    	

    	exec("bit4");
    	v = (v & 0x33333333) + ((v >>> 2) & 0x33333333);
    	setBitArray(v, 999, 4, value);
    	lang.nextStep();
    	

    	exec("bit8");
    	v = (v + (v >>> 4)) & 0x0F0F0F0F;
    	setBitArray(v, 999, 8, value);
    	lang.nextStep();


    	exec("return");
    	v = v * 0x01010101 >>> 24;
    	setBitArray(v, 999, 8, value);
    	lang.nextStep();
    	
    	desc.setText("Ausnutzung von Bitarithmetik lohnt sich", null, null);
    	
		
		return lang.toString().replace("\"?\" \"?\" depth 1", "\"?\" \"?\" depth 1 font Monospaced");
				
	}



	

	@Override
	public String getAlgorithmName() {
		return "Fast bitcount";
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
		return "Ein Beispiel daf&uuml;r, dass sich auch einfache Probleme " +
				"auf interessante Art l&ouml;sen lassen.<p>Mehr " +
				"Beispiele finden sich unter http://graphics.stanford.edu/~seander/bithacks.html</p>";
	}


	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}


	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}


	@Override
	public String getOutputLanguage() {
		return "C";
	}


	@Override
	public void init() 
	{		
		if (initialized_ ) {
			return;
		} else {
			initialized_ = true;
		}
		
		super.init();
		
			
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24));
				
		lang.newText(
				new Coordinates(10, 10)
			,	getAlgorithmName()
			,	"title"
			,	null
			,	tp
			);

	}


	public FastBitcount() 
	{
		init();
	}
}
