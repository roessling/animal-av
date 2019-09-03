package generators.cryptography;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.cryptography.helpers.E;
import generators.cryptography.helpers.OFB;
import generators.cryptography.helpers.Parser;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;

public class OFBGenerator implements ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties SourceCode_properties;
    private ArrayMarkerProperties ArrayMarker_properties;
    private String initial_vector;
    private ArrayProperties Array_properties;
    private RectProperties TitleBackground_properties;
    private TextProperties AttentionText_properties;
    private int r;
    int[] E_as_permutation;
    private Boolean E_is_permutation;
    String E_as_function;
    private String message_m;

    public void init(){
        lang = new AnimalScript("Output Feedback Mode [EN] ", "Christian Feier, Yannick Drost", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        SourceCode_properties = (SourceCodeProperties)props.getPropertiesByName("SourceCode_properties");
        ArrayMarker_properties = (ArrayMarkerProperties)props.getPropertiesByName("ArrayMarker_properties");
        Array_properties = (ArrayProperties)props.getPropertiesByName("Array_properties");
        TitleBackground_properties = (RectProperties)props.getPropertiesByName("TitleBackground_properties");
        AttentionText_properties = (TextProperties)props.getPropertiesByName("AttentionText_properties");
        
        initial_vector = (String)primitives.get("initial_vector");
        
        if (primitives.get("r") instanceof String)
          r = Integer.valueOf((String) primitives.get("r"));
        else r = (Integer)primitives.get("r");
        
        if (primitives.get("E_as_permutation") instanceof String) {
          try {
            String E_as_permutation_String = (String) primitives.get("E_as_permutation");
            int E_length = E_as_permutation_String.length();
            E_as_permutation = new int[E_length];
            for (int i = 0; i < E_length; i++) {
              E_as_permutation[i] = Integer.valueOf(E_as_permutation_String.substring(i, i+1));
            }
          }
          catch (Exception e) {}
        }
        else E_as_permutation = (int[])primitives.get("E_as_permutation");
        
        if (primitives.get("E_is_permutation") instanceof String) {
          E_is_permutation = Boolean.valueOf((String) primitives.get("E_is_permutation"));
        }        
        else E_is_permutation = (Boolean)primitives.get("E_is_permutation");
        
        E_as_function = (String)primitives.get("E_as_function");
        message_m = (String)primitives.get("message_m");
        
        final int n = initial_vector.length();
        
        CheckpointUtils.checkpointEvent(this, "nEvent", new Variable("n", n));
        
        E e;
        // E is a permutation
        if (E_is_permutation)
        {
	        e = new E() 
	        {	
	 			@Override
	 			public Object stringRepresentation() {
	 				
	 				String[] result = new String[E_as_permutation.length];
	 				
	 				for (int i = 0; i < result.length; i++)
	 					result[i] = new Integer(E_as_permutation[i]).toString();
	 				
	 				return result;
	 			}
	 			
	 			@Override
	 			public boolean isPermutation() {
	 				return true;
	 			}
	 			
	 			@Override
	 			public String encrypt(String i_i) {
	 				
	 				StringBuffer sb = new StringBuffer();
	 				for (int i = 0; i < E_as_permutation.length; i++)
	 				{
	 					sb.append(i_i.charAt(E_as_permutation[i]-1));
	 				}
	 				
	 				return sb.toString();
	 			}
	 		};
        }
        // e is no permutation
        else
        {
        	e = new E() {
				
				@Override
				public Object stringRepresentation() {
					return E_as_function;
				}
				
				@Override
				public boolean isPermutation() {
					return false;
				}
				
				@Override
				public String encrypt(String i_i) {
					// replace x with i_i
					int in = Integer.parseInt(i_i, 2);
					String input = E_as_function.replace("x", String.valueOf(in));
					Parser p = new Parser();
					int result = (int) p.parse(input);
					
					return toBin(result, n);
				}
			};
        }
 		
 		OFB ofb = new OFB(lang, this);
 		ofb.ofb(message_m, n, r, initial_vector, e, TitleBackground_properties, SourceCode_properties, Array_properties, AttentionText_properties, ArrayMarker_properties);
        
        return lang.toString();
    }
    
    
	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) throws IllegalArgumentException {
		
        SourceCode_properties = (SourceCodeProperties)props.getPropertiesByName("SourceCode_properties");
        ArrayMarker_properties = (ArrayMarkerProperties)props.getPropertiesByName("ArrayMarker_properties");
        initial_vector = (String)primitives.get("initial_vector");
        Array_properties = (ArrayProperties)props.getPropertiesByName("Array_properties");
        TitleBackground_properties = (RectProperties)props.getPropertiesByName("TitleBackground_properties");
        AttentionText_properties = (TextProperties)props.getPropertiesByName("AttentionText_properties");
        r = (Integer)primitives.get("r");
        E_as_permutation = (int[])primitives.get("E_as_permutation");
        E_is_permutation = (Boolean)primitives.get("E_is_permutation");
        E_as_function = (String)primitives.get("E_as_function");
        message_m = (String)primitives.get("message_m");
        
        int n = initial_vector.length();
		
        // check if 1 <= r <= n
        if (!(1 <= r && r <= n))
        	return false;
        
        // Permutationschecks
        if (E_is_permutation)
        {
	        // check if permutation has length of n
	        if (!(n == E_as_permutation.length))
	        	return false;
	        
	        // check if permutation is bijective
	        boolean[] tmp = new boolean[E_as_permutation.length];
	        
	        for (int i = 0; i < E_as_permutation.length; i++)
	        	if (E_as_permutation[i] < 1 || E_as_permutation[i] > n)
	        		return false;
	        
	        for (int i = 0; i < tmp.length; i++)
	        	tmp[i] = false;
	        
	        for (int i = 0; i < E_as_permutation.length; i++)
	        {
	        	tmp[E_as_permutation[i]-1] = true;
	        }
	        
	        for (int i = 0; i < tmp.length; i++)
	        	if (!(tmp[i]))
	        		return false;
        }
        // function checks
        else
        {
        	// check if just integers in the given formula
        	Parser p = new Parser();
        	if (p.containsInvalidNumber(E_as_function))
        		return false;
        }
        
        
        // check if init vector has length of n
        if (!(n == initial_vector.length()))
        	return false;
        
        // check if init vector contains just 0 and 1
        for (int i = 0; i < initial_vector.length(); i++)
        {
        	if (!(initial_vector.charAt(i) == '0' || initial_vector.charAt(i) == '1'))
        		return false;
        }
        
        // check if message contains just 0 and 1
        for (int i = 0; i < message_m.length(); i++)
        {
        	if (message_m.charAt(i) != '0' && message_m.charAt(i) != '1')
        		return false;
        }
        
        return true;
	}
    
    
	/**
	 * converts the given number to a binary string witl n digits
	 * 
	 * @param num
	 * 				given number
	 * @param n
	 * 				number of digits
	 * 
	 * @return number as binary string
	 */
	String toBin(int num, int n)
	{
		String bin = Integer.toBinaryString(num);
		
		while (bin.length() < n)
		{
			bin = "0" + bin;
		}
		
		if (bin.length() > n)
		{
			bin = bin.substring(bin.length()-n, bin.length());
		}
		
		return bin;
	}
    
    

    public String getName() {
        return "Output Feedback Mode [EN] ";
    }

    public String getAlgorithmName() {
        return "Output Feedback Mode (OFB)";
    }

    public String getAnimationAuthor() {
        return "Christian Feier, Yannick Drost";
    }

 
    
    public String getDescription(){
        return "A Blockcipher is a ciphering mode to map blocks of a constant length to blocks of the same length. Modes of operation enable the repeated and secure use of"
 +"\n"
 +"block ciphers under a single key. One of those modes of operation is the Output Feedback Mode aka OFB Mode. The OFB makes block ciphers into a synchronous"
 +"\n"
 +"stream Cipher by generating keystream blocks which are then XORed with the plaintext blocks to get the ciphertext blocks."
 +"\n"
 +"OFB Mode is pretty useful to encode long texts because same blocks will be mapped on different blocks. So same blocks will never be encoded as the block before.<br/><br/>"
 +"\n"
 +"<strong>Requirements:</strong>"
 +"\n"
 +"<ul>"
 +"\n"
 +"     <li>the initialvector just contains 0 and 1 as elements"
 +"\n"
 +"     <li>1 &le; r &le; n with r,n &isin; &#x2115;"
 +"\n"
 +"     <li>the permutationkey has to be a bijective function with the following mapping E: {0,1}<sup>n</sup> &rarr; {0,1}<sup>n</sup> "
 +"\n"
 +"     <li>the intitialvector must have the length of n"
 +"\n"
 +"</ul>"
 +"\n"
 +"\n"
 +"\n"
 +"<strong>Parameters:</strong><br/>"
 +"\n"
 +"the parameter E_is_permutation decides whether E is a permutation or function. If E is a permutation, the parameter E_as_permutation is important for you. In this case the following "
 +"\n"
 +"requirements have to match:<br/>"
 +"\n"
 +"<strong>Requirements:</strong>"
 +"\n"
 +"<ul>"
 +"\n"
 +"     <li>the permutationkey has to be a bijective function with the following mapping E: {0,1}<sup>n</sup> &rarr; {0,1}<sup>n</sup> "
 +"\n"
 +"</ul>"
 +"\n"
 +"if E is no permutation, the parameter E_as_function is important and the following requirements have to match:<br/>"
 +"\n"
 +"<strong>Requirements:</strong>"
 +"\n"
 +"<ul>"
 +"\n"
 +"    <li>the form of the function is f(x) = y. you just need to fill in y. But the parameter in y <strong>has to be</strong> x."
 +"\n"
 +"    <li>the function should be a function with the following mapping E: &#x2115; &rarr; {0,...,2<sup>n</sup>} "
 +"\n"
 +"    <li>the following operations are supported: +, -, *, mod<br/>."
 +"\n"
 +" Examples:"
 +"\n"
 +"	<ul>"
 +"\n"
 +"	     <li> x + 2 mod 7"
 +"\n"
 +"	     <li>(2+x) *5 + 7 mod 42"
 +"\n"
 +"	     <li>whitespaces will be ignored, so (2+2) -     3 equals (2+2)-3"
 +"\n"
 +"	</ul>"
 +"\n"
 +"</ul>";
    }

    public String getCodeExample(){
        return "<span style=\"text-decoration: underline;\"><font size=\"6\">in words step by step</font></span>"
 +"\n"
 +"\n"
 +"init:"
 +"<ul>"
 +"     <li>choose a message m to encode"
 +"\n"
 +"     <li>choose an encryption function E. Usually E is a permutation key"
 +"\n"
 +"     <li>choose an initialvector IV in {0,1}<sup>n</sup>. n is a natural number."
 +"\n"
 +"     <li>choose an r with 1 &le; r &le; n"
 +"\n"
 +"     <li>split the given text into blocks of length r. So we get m = m<sub>1</sub> m<sub>2</sub> m<sub>3</sub> ... m_j.<br/>Assuming block m<sub>j</sub> has not length of r. If so, we add zeros to the end of m<sub>j</sub>"
 +"\n"
 +"     <li>until it has length of r."
 +"\n"
 +"     <li>set I<sub>0</sub> = IV"
 +"\n"
 +"</ul>"
 +"\n"
 +"step 1:"
 +"<ul>"
 +"     <li>calculate O<sub>i</sub> = E<sub>k</sub>(I<sub>i</sub>)"
 +"\n"
 +"</ul>"
 +"\n"
 +"step 2:"
 +"<ul>"
 +"     <li>calculate t<sub>i</sub>. t<sub>i</sub> are the first r bits of O<sub>i</sub>"
 +"\n"
 +"</ul>"
 +"\n"
 +"step 3:"
 +"<ul>"
 +"     <li>calculate c<sub>i</sub> = t<sub>i</sub> xor m<sub>i</sub>"
 +"\n"
 +"</ul>"
 +"\n"
 +"step 4:"
 +"<ul>"
 +"     <li>set I<sub>i+1</sub> = O<sub>i</sub> and start at step 1 again until m<sub>j</sub> is encrypted"
 +"\n"
 +"</ul>"
 +"\n"
 +"<br/><br/><span style=\"text-decoration: underline;\"><font size=\"6\">in Pseudocode</font></span>"
 +"\n"
 +"<pre><span style=\"color: #B404AE;\"><strong>def</strong></span> OFB(m, r, IV):\n"
 +"    <span style=\"color: #088A08;\"># split text into blocks of length of r</span>\n"
 +"    blocks = splitTextIntoBlocks(m, r)\n"
 +"    <span style=\"color: #088A08;\"># set init vector as I_i</span>\n"
 +"    I_i = IV\n"
 +"    c = Array(blocks.length())\n\n"
 +"    <span style=\"color: #B404AE;\"><strong>for</strong></span> block <span style=\"color: #B404AE;\"><strong>in</strong></span> blocks:\n"
 +"        O_i = E(I_i)\n"
 +"        t_i = O_i[0:r]\n"
 +"        c[i] = t_i xor m_i\n"
 +"\n"
 +"        I_i = O_i\n"
 +"\n"
 +"    <span style=\"color: #B404AE;\"><strong>return</strong></span> c</pre>";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.US;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}