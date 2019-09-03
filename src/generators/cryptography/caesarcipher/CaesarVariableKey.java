package generators.cryptography.caesarcipher;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;




public class CaesarVariableKey extends AnnotatedAlgorithm implements Generator{

	/**
	 * The concrete language object used for creating output
	 */
	
	
	private ArrayProperties arrayProps;
	private ArrayMarker marker;
	private ArrayMarkerProperties markerProps;
	
	private String [] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	private String [] cipherAlphabet = new String[26];
	
	
	private SourceCodeProperties sourceCodeProps;
	
	private String [] text;
	private String [] ciphre;
	
	private String PlainText;
	private int key;
	
	private Text  pseudoCode, shift, toEncText, plainText, ciphreText, EncryptedText;
	
	private StringArray pText, alpha, ciphreAlpha, cText;
	
	
	private String ver = "Vergleiche";
	private String zuw = "Zuweisungen";
	
	
	
	private static final String DESCRIPTION = "Bei der Caesar Chiffre handelt es sich um ein einfaches Verschlüsselungsverfahren. \n" +
	"Jeder Buchstabe des Alphabeten wird um \n" +
	"3 Stellen nach links verschoben, somit entsteht das Geheimalphabet.\n" +
	"Jeder Buchstabe des zu verschlüssenden Textes, besitzt eine Stelle im Alphabet,\n " +
	"die jetzt im Geheimalphabet ein anderer Buchstabe besitzt. \n" +
	"Der Text wird verschlüsselt indem jeder Buchstabe des Textes durch \n" +
	"die entsprechende Buchstabe des Geihmalphabeten ersetzt wird.";
	
	private static final String SOURCE_CODE = "Für i=0 in PlainText-Array to n \n" +
	"Finde die Stelle der Buchstabe im PlainAlphabet \n" +
	"Finde den Buchstaben der an dieser Stelle im CipherAlphabet steht \n" +
	"Schreibe auf der Stelle i in CipherText-Array den gefundenen Buchstaben ";
		
		
	@Override
	public String getAnnotatedSrc() {
		return "Für i=0; 											@label(\"header\") @declare(\"int\", \"i\") @inc(\""+zuw+"\")\n" 
		+ " i < Länge von PlainText-Array;    						@label(\"Comp\") @continue @inc(\""+ver+"\")\n"
		+ "i=i+1                              				    	@label(\"ForInc\")  @continue @inc(\"i\") @inc(\""+zuw+"\")\n"
		+ "	Finde die Stelle der Buchstabe im PlainAlphabet	 		@label(\"plainAlphabet\") @inc(\""+zuw+"\")\n"
		+ "	Finde den Buchstaben der an dieser Stelle im CipherAlphabet steht @label(\"ciphreAlphabet\") @inc(\""+zuw+"\") \n"
		+ " Schreibe auf der Stelle i in CipherText-Array den gefundenen Buchstaben @label(\"ciphreText\") @inc(\""+zuw+"\")\n"
		+"     @label(\"end\")";
	}

	
	@Override
	public void init() {
		 super.init();
		 
		 init_ciphre();
		 
		 
		 pseudoCode = lang.newText(new Coordinates(20, 20), "Pseudo Code", "pseudocode", null);
		 pseudoCode.setFont(new Font ("Serif", Font.BOLD, 24), null, null);
		 sourceCode = lang.newSourceCode(new Coordinates(20, 50), "sourceCode", null, sourceCodeProps);
		
		 
		 shift = lang.newText(new Coordinates(20, 200), "Shift = "+ key, "shift", null);
	     shift.setFont(new Font ("Serif", Font.BOLD, 16), null, null);
	     
		 toEncText = lang.newText(new Coordinates(20, 250), "Plain Text", "text1", null);
		 toEncText.setFont(new Font ("Serif", Font.BOLD, 16), null, null);
		 
		 pText = lang.newStringArray(new Offset(80,0, toEncText, "west"), text, "ciphrealphabet", 
					null, arrayProps);;
		    
		 plainText = lang.newText(new Offset(-25,80, toEncText, "south"), "Plainalphabet", "text2", null);
		 plainText.setFont(new Font ("Serif", Font.BOLD, 16), null, null);
		     
		 alpha = lang.newStringArray(new Offset(120,0, plainText, "west"), alphabet, "alphabet", 
					null, arrayProps);
		    
		 ciphreText = lang.newText(new Offset(-25,160, toEncText, "south"), "Cipheralhpabet", "text3", null);
		 ciphreText.setFont(new Font ("Serif", Font.BOLD, 16), null, null);
		    
		 ciphreAlpha = lang.newStringArray(new Offset(120,0, ciphreText, "west"), cipherAlphabet, "ciphrealphabet", 
					null, arrayProps);;
		    
		 EncryptedText = lang.newText(new Offset(-25,240, toEncText, "south"), "Cipher Text", "text4", null);
	     EncryptedText.setFont(new Font ("Serif", Font.BOLD, 16), null, null);
		    
	     cText = lang.newStringArray(new Offset(100,0, EncryptedText, "west"), ciphre, "ciphrealphabet", 
							null, arrayProps);
	     
	  // setup complexity
	     vars.declare("int", ver); vars.setGlobal(ver);
		 vars.declare("int", zuw); vars.setGlobal(zuw);
		 
		 parse();
	  }
	
	private void setProps(){
		 sourceCodeProps = new SourceCodeProperties();
		 sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		 sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font (Font.MONOSPACED, Font.PLAIN ,20));
		
	}
	
	private void setKey(int k){
		
		this.key = k;
	}
	
	private void setPlain(String plain){
		
		this.PlainText = plain;
	}
	
	
	public void init_ciphre(){
		 
		 PlainText.toCharArray();
		 text = new String[PlainText.length()];
		 ciphre = new String[PlainText.length()];
		
		 for(int i=0;i<PlainText.length();i++){
			text[i] = String.valueOf(PlainText.charAt(i));
			ciphre[i] = "*";
		 }
		 
		 key = key % 26;
		 for(int i=key;i<cipherAlphabet.length;i++){
			cipherAlphabet[i-key] = alphabet[i];
	     }   	
		 for(int i=0; i<key;i++){
			cipherAlphabet[(i+26-key)] = alphabet[i];
		 }
		 
	}
	
	public void caesar(String s, int k){
		     
		     init();
	    try {
	    	
			en_caesar(s , k, alphabet, pText, alpha, ciphreAlpha, cText);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void en_caesar( String s, int key, String [] alphabet, 
			StringArray plainText, StringArray plainAlphabet, 
			StringArray cipherAlphabet, StringArray cipherText)
    {
        int diff;
     lang.nextStep(); 
     exec("header");
     
     for( int i = 0; i < s.length(); i++ )
      {
    	lang.nextStep();
    	exec("Comp"); 
    	
    	
        int c = s.charAt( i );
        plainText.highlightElem(i, null, null);
        marker = lang.newArrayMarker(plainText,i, "marker", null,markerProps);
      
        
        
        if ( (c >= 'A') && (c <= 'Z') ) {
        	 c +=32;
        }
        
        if( (c >= 'a') && (c <= 'z') ){
                diff = 97;
                c = c - diff;
                lang.nextStep();
                
                plainText.unhighlightElem(i, null, null);
                plainAlphabet.highlightElem(c, null, null);
                marker.hide();
           	    marker = lang.newArrayMarker(plainAlphabet,c, "marker", null,markerProps);
           	    exec("plainAlphabet");
           	    
           	    lang.nextStep();
           	    plainAlphabet.unhighlightElem(c, null, null);
           	   
           	    cipherAlphabet.highlightElem(c, null, null);
        	    marker.hide();
        	    marker = lang.newArrayMarker(cipherAlphabet,c, "marker", null,markerProps);
        	    exec("ciphreAlphabet");
        	    
        	    lang.nextStep();
        	    cipherAlphabet.unhighlightElem(c, null,null);
        	    
        	   
                cipherText.put(i, cipherAlphabet.getData(c), null, null);
                marker.hide();
                marker = lang.newArrayMarker(cipherText,i, "marker", null,markerProps);
                exec("ciphreText");
                
                lang.nextStep();
                
                marker.hide();
                exec("ForInc");
          }else{
        	  marker.hide();
        	  exec("end");
          }
           lang.nextStep();
           exec("end");
       }

    }
	
	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		CaesarVariableKey c = new CaesarVariableKey();
		c.setPlain((String) arg1.get("PlainText"));
		c.setKey((int)(Integer) arg1.get("key"));
		c.setProps();
		c.arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");
		c.markerProps = (ArrayMarkerProperties) arg0.getPropertiesByName("markerProps");
	    c.caesar(c.PlainText, c.key);
	    return c.lang.toString();
	}
   
	
	
	@Override
	public String getAlgorithmName() {
		return "Caesar-Verschl\u00fcsselung";
	}


	@Override
	public String getAnimationAuthor() {
		return "Jurlind Budurushi, Genc Shala";
	}


	@Override
	public String getCodeExample() {
		return SOURCE_CODE;
	}


	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}


	@Override
	public String getDescription() {
		return DESCRIPTION; 
	}


	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}


	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}


	@Override
	public String getName() {
		return "Caesar Chiffre with variable Key";
	}


	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}
