package generators.backtracking;

import generators.backtracking.helpers.CYK;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public class CYKGenerator implements Generator {
    private Language lang;
    private TextProperties Wort;
    private SourceCodeProperties Description;
    private SourceCodeProperties Grammatik;
    private TextProperties Headertext;
    private String wort;
    private String benutzerCNF;
    private TextProperties show_i;
    private TextProperties show_j;
    private TextProperties show_k;
    private TextProperties Text;



    public void init(){
        lang = new AnimalScript("Cocke-Younger-Kasami-Algorithmus", "Ahmet Erguen, Niklas Bunzel", 1280, 1024);
    }


    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
       
       Text = (TextProperties)props.getPropertiesByName("Text");
	   show_i = (TextProperties)props.getPropertiesByName("show_i");
       show_j = (TextProperties)props.getPropertiesByName("show_j");
       show_k = (TextProperties)props.getPropertiesByName("show_k");
       Wort = (TextProperties)props.getPropertiesByName("Wort");
        Description = (SourceCodeProperties)props.getPropertiesByName("Description");
        Grammatik = (SourceCodeProperties)props.getPropertiesByName("Grammatik");
        Headertext = (TextProperties)props.getPropertiesByName("Headertext");
        wort = (String)primitives.get("wort");
        benutzerCNF = (String)primitives.get("benutzerCNF");
        
		  
		  CYK cyk = new CYK(lang);
		  cyk.findWord(Headertext, Description, Grammatik, Wort, benutzerCNF, wort,show_i,show_j,show_k,Text);
		  
        return lang.toString();
    }

    public String getName() {
        return "Cocke-Younger-Kasami-Algorithmus";
    }

    public String getAlgorithmName() {
        return "Cocke-Younger-Kasami-Algorithmus";
    }

    public String getAnimationAuthor() {
        return "Ahmet Erguen, Niklas Bunzel";
    }

    public String getDescription(){
        return 
 "\n"
 +"\n"
 +" the Cocke-Younger-Kasami (CYK) algorithm (alternatively called CKY) is a parsing algorithm "
 +"\n"
 +"for context-free grammars.<br>"
 +"\n"
 +"The standard version of CYK operates only on context-free grammars given in Chomsky normal form (CNF).<br>"
 +"\n"
 +"However any context-free grammar may be transformed to a CNF grammar expressing the same language.<br>"
 +"\n"
 +"The importance of the CYK algorithm stems from its high efficiency in certain situations. <br>"
 +"\n"
 +"Using Landau symbols, the worst case running time of CYK is  Theta(n^3  * |G|), <br>"
 +"\n"
 +"where n is the length of the parsed string and |G| is the size of the CNF grammar G.<br>"
 +"\n"
 +"This makes it one of the most efficient parsing algorithms in terms of worst-case asymptotic complexity."
 +"\n"
 +"\n"

 +"\n";
    }

    public String getCodeExample(){
        return 
 "\n"
 
 +"let the input be a string S consisting of n characters: a1 ... an.<br>"
 +"let the grammar contain r nonterminal symbols R1 ... Rr.<br>"
 +"This grammar contains the subset Rs which is the set of start symbols.<br>"
 +"let T[n,n] be an matrix of nonterminals. Initialize all elements of T to the empty string.<br>"
 +"for each i = 0 to n<br>"
 +"    for each unit production Rj -> ai<br>"
 +"        set T[i,1] = Rj<br>"
 +"for each j = 1 to n -- Length of span<br>"
 +"    for each i = 0 to n-j -- Start of span<br>"
 +"        for each k = 0 to j-1 -- Partition of span<br>"
 +"            set T[i,j] = T[i,j] union T[i,k] and T[i+k,j-k]<br>"
 +"if any of T[1,n] equals any nonterminals of Rs then<br>"
 +"    S is member of language<br>"
 +"else<br>"
 +"    S is not member of language<br>";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.US;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_BACKTRACKING);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}