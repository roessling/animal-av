package generators.compression;
import generators.compression.helpers.ShannonFanoAnim;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;



import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;

public class ShannonGenerator implements Generator {
    private Language lang;
    private String inputString;
    private SourceCodeProperties sourceCode;
    private ArrayMarkerProperties stringArrayMarker;
    private ArrayProperties stringArray;
//    private ShannonFanoAnim sh;

    public void init(){
        lang = new AnimalScript("Shannon-Fano", "Dieter Hofmann, Artem Vovk", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        inputString = (String)primitives.get("inputString");
        System.out.println("inputString" +inputString);
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        stringArrayMarker = (ArrayMarkerProperties)props.getPropertiesByName("stringArrayMarker");
        stringArray = (ArrayProperties)props.getPropertiesByName("stringArray");
        new ShannonFanoAnim(lang, stringArray,sourceCode,stringArrayMarker, inputString);
        return lang.toString();
    }

    public String getName() {
        return "Shannon-Fano";
    }

    public String getAlgorithmName() {
        return "Shannon-Fano Coding";
    }

    public String getAnimationAuthor() {
        return "Dieter Hofmann, Artem Vovk";
    }

    public String getDescription(){
        return "In Shannon-Fano coding, the symbols are arranged in order from most probable to least probable, and then divided into two sets whose total probabilities are as close as possible to being equal. All symbols then have the first digits of their codes assigned; symbols in the first set receive \"0\" and symbols in the second set receive \"1\". As long as any sets with more than one member remain, the same process is repeated on those sets, to determine successive digits of their codes. When a set has been reduced to one symbol, of course, this means the symbol's code is complete and will not form the prefix of any other symbol's code. The algorithm works, and it produces fairly efficient variable-length encodings; when the two smaller sets produced by a partitioning are in fact of equal probability, the one bit of information used to distinguish them is used most efficiently. Unfortunately, Shannon-Fano does not always produce optimal prefix codes; the set of probabilities {0.35, 0.17, 0.17, 0.16, 0.15} is an example of one that will be assigned non-optimal codes by Shannon-Fano coding.";
    }

    public String getCodeExample(){
        return " 1:  begin"
 +"\n"
 +" 2:     count source units"
 +"\n"
 +" 3:     sort source units to non-decreasing order"
 +"\n"
 +" 4:     SF-SplitS"
 +"\n"
 +" 5:     output(count of symbols, encoded tree, symbols)"
 +"\n"
 +" 6:     write output"
 +"\n"
 +" 7:   end"
 +"\n"
 +" 8:  "
 +"\n"
 +" 9:  procedure SF-Split(S)"
 +"\n"
 +"10:  begin"
 +"\n"
 +"11:     if (|S|>1) then"
 +"\n"
 +"12:      begin"
 +"\n"
 +"13:        divide S to S1 and S2 with about same count of units"
 +"\n"
 +"14:        add 1 to codes in S1"
 +"\n"
 +"15:        add 0 to codes in S2"
 +"\n"
 +"16:        SF-Split(S1)"
 +"\n"
 +"17:        SF-Split(S2)"
 +"\n"
 +"18:      end"
 +"\n"
 +"19:  end";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.US;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}
