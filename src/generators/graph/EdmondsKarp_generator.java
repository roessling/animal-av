/*
 * edmondsKarp_generator.java
 * Benedikt Lins, Stefan Thaut, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import algoanim.util.Coordinates;
import generators.graph.edmondsKarpHelper.EdmondsKarp;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import translator.Translator;

public class EdmondsKarp_generator implements ValidatingGenerator {
    private Language lang;
    private int Zielknoten;
    private int[][] Adjazenzmatrix;
    private int[][] Kapazitaetsmatrix;
    private int[][] Knotenpositionen;
    private int Startknoten;
    
    private Locale locale;

    private Translator translator;

    /**
     * localizer: "resources/edmonds_karp"
     *
     * Following Locales are possible:
     * Locale.GERMANY
     * Locale.US
     */
    public EdmondsKarp_generator(Locale locale) {
      this.locale = locale;
        translator = new Translator("resources/Edmonds_karp", locale);
    }

    public void init(){
        lang = new AnimalScript("Edmonds-Karp", "Benedikt Lins, Stefan Thaut", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Zielknoten = (Integer)primitives.get("Zielknoten");
        Adjazenzmatrix = (int[][])primitives.get("Adjazenzmatrix");
        Kapazitaetsmatrix = (int[][])primitives.get("Kapazitaetsmatrix");
        Knotenpositionen = (int[][])primitives.get("Knotenpositionen");
        Startknoten = (Integer)primitives.get("Startknoten");

        Coordinates[] nodes = new Coordinates[Knotenpositionen.length];
        for(int i = 0; i < Knotenpositionen.length; i++) {
            nodes[i] = new Coordinates(Knotenpositionen[i][0], Knotenpositionen[i][1]);
        }

        new EdmondsKarp(lang, Adjazenzmatrix, Kapazitaetsmatrix, Startknoten, Zielknoten, nodes, translator);
        
        return lang.toString();
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        int Zielknoten = (Integer)hashtable.get("Zielknoten");
        int[][] Adjazenzmatrix = (int[][])hashtable.get("Adjazenzmatrix");
        int[][] Kapazitaetsmatrix = (int[][])hashtable.get("Kapazitaetsmatrix");
        int[][] Knotenpositionen = (int[][])hashtable.get("Knotenpositionen");
        int Startknoten = (Integer)hashtable.get("Startknoten");

        if(Adjazenzmatrix.length != Adjazenzmatrix[0].length) {
            throw new IllegalArgumentException(translator.translateMessage("ex1"));
            //return false;
        }

        if(Kapazitaetsmatrix.length != Kapazitaetsmatrix[0].length) {
            throw new IllegalArgumentException(translator.translateMessage("ex2"));
            //return false;
        }

        if(Adjazenzmatrix.length != Kapazitaetsmatrix.length) {
            throw new IllegalArgumentException(translator.translateMessage("ex3"));
            //return false;
        }

        for(int i = 0; i < Adjazenzmatrix.length; i++) {
            for(int j = 0; j < Adjazenzmatrix[i].length; j++)
                if(Adjazenzmatrix[i][j] != 0 && Adjazenzmatrix[i][j] != 1) {
                    throw new IllegalArgumentException(translator.translateMessage("ex4"));
                    //return false;
                }
        }

        if(Zielknoten < 0 || Zielknoten >= Adjazenzmatrix.length) {
            throw new IllegalArgumentException(translator.translateMessage("ex5") + " " + Adjazenzmatrix.length + " " + translator.translateMessage("be"));
            //return false;
        }

        if(Startknoten < 0 || Startknoten >= Adjazenzmatrix.length) {
            throw new IllegalArgumentException(translator.translateMessage("ex6") + " " + Adjazenzmatrix.length + " " + translator.translateMessage("be"));
            //return false;
        }

        if(Startknoten == Zielknoten) {
            throw new IllegalArgumentException(translator.translateMessage("ex7"));
            //return false;
        }

        if(Knotenpositionen.length != Adjazenzmatrix.length) {
            throw new IllegalArgumentException(translator.translateMessage("ex8"));
            //return false;
        }

        if(Knotenpositionen[0].length != 2) {
            throw new IllegalArgumentException(translator.translateMessage("ex9"));
            //return false;
        }

        return true;
    }

    public String getName() {
        return "Edmonds-Karp";
    }

    public String getAlgorithmName() {
        return "Edmonds-Karp";
    }

    public String getAnimationAuthor() {
        return "Benedikt Lins, Stefan Thaut";
    }

    public String getDescription(){
        return translator.translateMessage("generatorDescription");
    }

    public String getCodeExample(){
        return "edmondsKarp(G, s, t):"
 +"\n"
 +"     path = breadthFirstSearch(G, s, t)"
 +"\n"
 +"     while(path != null):"
 +"\n"
 +"          residualCapacity = INFINITY"
 +"\n"
 +"          for(edge in path):"
 +"\n"
 +"               residualCapacity = min(residualCapacity, edge.getResidualCapacity())"
 +"\n"
 +"          for(edge in path):"
 +"\n"
 +"               edge.addResidualFlow(residualCapacity)"
 +"\n"
 +"          path = breadthFirstSearch(G, s, t)";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}