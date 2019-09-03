/*
 * dial_implementation_generator.java
 * Stefan Thaut und Benedikt Lins, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.datastructures;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import generators.datastructures.dialImplementationHelpers.DialImplementation;
import translator.Translator;

public class dial_implementation_generator implements ValidatingGenerator {
    private Language lang;
    private int S;
    private boolean randomMethodSelection;
    private int numOps;
    private String[][] operations;

    private Translator translator;

    private static HashSet<String> opNames = new HashSet<>();

    static {
        dial_implementation_generator.opNames.add("insert");
        dial_implementation_generator.opNames.add("extractMinimum");
        dial_implementation_generator.opNames.add("decreaseKey");
        dial_implementation_generator.opNames.add("number");
    }

    public dial_implementation_generator() {
        this("resources/dial_implementation", Locale.GERMANY);
    }

    /**
     * localizer: "resources/dial_implementation"
     *
     * Following Locales are possible:
     * Locale.GERMANY
     * Locale.US
     */
    public dial_implementation_generator(String localizer, Locale locale) {
        translator = new Translator(localizer, locale);
    }

    public void init(){
        lang = new AnimalScript(translator.translateMessage("titleString"), "Stefan Thaut, Benedikt Lins", 800, 600);
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        if((Integer) hashtable.get("S") < 1 || (Integer) hashtable.get("S") > 15) {
            throw new IllegalArgumentException(translator.translateMessage("ex1"));
            //return false;
        }
        Integer S = (Integer) hashtable.get("S");
        if(((Boolean) hashtable.get("Random Method Selection")) && ((Integer) hashtable.get("Anzahl der zufaelligen Operationen") < 1)) {
            throw new IllegalArgumentException(translator.translateMessage("ex2"));
            //return false;
        }
        String[][] ops = (String[][]) hashtable.get("Operations");
        if(!((Boolean) hashtable.get("Random Method Selection"))) {
            if(ops.length < 1) {
                throw new IllegalArgumentException(translator.translateMessage("ex3"));
                //return false;
            }
            if(ops[0].length != 2) {
                throw new IllegalArgumentException(translator.translateMessage("ex4"));
                //return false;
            }

            int numElements = 0;
            int[] dial = new int[S];
            int minimum = 0;
            for(String[] op: ops) {
                if(!parseOps(op, S)) {
                    return false;
                }

                switch(op[0]) {
                    case "extractMinimum":
                        if(numElements < 1) {
                            throw new IllegalArgumentException(translator.translateMessage("ex5"));
                            //return false;
                        }
                        numElements--;
                        dial[minimum]--;
                        if(numElements == 0)
                            minimum = 0;
                        else {
                            if (dial[minimum] == 0) {
                                do {
                                    minimum++;
                                } while (dial[minimum] == 0);
                            }
                        }
                        break;
                    case "insert":
                        numElements++;
                        int value = Integer.valueOf(op[1]);
                        dial[value]++;
                        if(numElements == 1)
                            minimum = value;
                        else {
                            if (value < minimum)
                                minimum = value;
                        }
                        break;
                    case "decreaseKey":
                        String[] decreaseVars = op[1].split("->");
                        int k = Integer.valueOf(decreaseVars[0]);
                        int knew = Integer.valueOf(decreaseVars[1]);
                        if(dial[k] == 0) {
                            throw new IllegalArgumentException(translator.translateMessage("ex6"));
                            //return false;
                        }
                        dial[k]--;
                        dial[knew]++;
                        if(knew < minimum)
                            minimum = knew;
                }
            }
        }

        return true;
    }

    private boolean parseOps(String[] op, int S) {
        String opName = op[0];
        String vars = op[1];
        if(!dial_implementation_generator.opNames.contains(opName)) {
            throw new IllegalArgumentException(translator.translateMessage("theOperator") + " " + opName + " " + translator.translateMessage("ex7"));
            //return false;
        }
        if((opName.equals("extractMinimum") || opName.equals("number")) && !vars.equals("-")) {
            throw new IllegalArgumentException(translator.translateMessage("theOperator") + " " + opName + " " + translator.translateMessage("ex8"));
            //return false;
        }
        if(opName.equals("insert") && (!vars.matches("\\d+") || Integer.valueOf(vars) >= S)) {
            throw new IllegalArgumentException(translator.translateMessage("ex9") + " " + S + " " + translator.translateMessage("ex10"));
            //return false;
        }
        if(opName.equals("decreaseKey")) {
            if(!vars.replaceAll(" ", "").matches("\\d+->\\d+")) {
                throw new IllegalArgumentException(translator.translateMessage("ex11"));
                //return false;
            }
            String[] decreaseVars = vars.split("->");
            int k = Integer.valueOf(decreaseVars[0]);
            int knew = Integer.valueOf(decreaseVars[1]);
            if(k >= S) {
                throw new IllegalArgumentException(translator.translateMessage("ex12") + " " + S + " " + translator.translateMessage("ex13"));
                //return false;
            }
            if(knew >= k) {
                throw new IllegalArgumentException(translator.translateMessage("ex14"));
                //return false;
            }
        }

        return true;
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        S = (Integer)primitives.get("S");
        randomMethodSelection = (Boolean)primitives.get("Random Method Selection");
        numOps = (Integer)primitives.get("Anzahl der zufaelligen Operationen");
        operations = (String[][])primitives.get("Operations");

        DialImplementation dial = new DialImplementation(S, lang, translator);

        if(randomMethodSelection)
            dial.randomMethodSelection(numOps);
        else
            dial.methodSelection(Arrays.asList(operations));
        
        return lang.toString();
    }

    public String getName() {
        return translator.translateMessage("titleString");
    }

    public String getAlgorithmName() {
        return translator.translateMessage("titleString");
    }

    public String getAnimationAuthor() {
        return "Stefan Thaut, Benedikt Lins";
    }

    public String getDescription(){
        return translator.translateMessage("generatorDescription");

    }

    public String getCodeExample(){
        return "---";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}