/*
 * GozintoGenerator.java
 * Timm Lampa,Najim Azizi, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;

public class GozintoGenerator implements Generator {
    private Language lang;
    private int[] needs;
    private MatrixProperties calculateTable;
    private MatrixProperties result;
    private SourceCodeProperties sourceCode;
    private String[] needNames;
    private MatrixProperties gozintoList;
    private String[] nodeNames;
    private double questionProbability;
    private Graph graph;
    
    public GozintoGenerator() {
    	
    }

    public void init(){
        lang = new AnimalScript("Gozinto-Listen-Verfahren", "Timm Lampa,Najim Azizi", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        needs = (int[])primitives.get("needs");
        calculateTable = (MatrixProperties)props.getPropertiesByName("calculateTable");
        result = (MatrixProperties)props.getPropertiesByName("result");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        needNames = (String[])primitives.get("needNames");
        gozintoList = (MatrixProperties)props.getPropertiesByName("gozintoList");
        nodeNames = (String[])primitives.get("nodeNames");
        questionProbability = Double.parseDouble(primitives.get("questionProbability").toString());
        graph = (Graph)primitives.get("graph");
        
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        
        Gozinto gozinto = new Gozinto(lang, gozintoList, calculateTable, result, sourceCode, questionProbability);
        int[][] adj = graph.getAdjacencyMatrix();
        gozinto.gozinto(graph.getAdjacencyMatrix(), nodeNames, needNames, needs);
        //gozinto.gozinto("(R1,Z1,3);(R1,Z2,4);(R1,X,2);(R2,Z2,2);(R2,X,3);(R3,Z1,5);(R3,Z2,1);(Z1,X,4);(Z2,X,6);(X,X,0);(Z1,300);(Z2,200);(X,300)");
        
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Gozinto-Listen-Verfahren";
    }

    public String getAlgorithmName() {
        return "Gozinto-Listen-Verfahren";
    }

    public String getAnimationAuthor() {
        return "Timm Lampa,Najim Azizi";
    }

    public String getDescription(){
        return "Das Gozinto-Listen-Verfahren wird genutzt, um St&uuml;cklisten aufzul&ouml;sen. Somit kann mit diesem Verfahren ausgehend"
 +"\n"
 +"vom Prim&auml;rbedarf eines Produkts der Sekund&auml;rbedarf anhand der Fertigungsstruktur errechnet werden."
 +"\n"
 +"\n"
 +"Dieses Verfahren baut auf einem sogenannten Gozintographen auf. Dieser dient dazu die Fertigungsstruktur und die"
 +"\n"
 +"quantitativen Input-Output-Beziehungen grafisch darzustellen."
 +"\n"
 +"\n"
 +"Der Gozintograph ist ein gerichteter, bewerteter Graph, dessen Knoten Endprodukte, Baugruppen und Einzelteile darstellen, w&auml;hrend"
 +"\n"
 +"dessen Pfeile als Input-Output-Beziehungen zu verstehen sind."
 +"\n"
 +"\n"
 +"Die Bewertung der Pfeile gibt an, wie viele Einheiten eines Einzelteils bzw. einer Baugruppe direkt in eine &uuml;bergeordnete Baugruppe bzw. in ein Endprodukt eingehen.";
    }

    public String getCodeExample(){
        return "1. Erstellen des Gozinto-Graphens."
 +"\n"
 +"2. Knoten im Gozinto-Graphen nummerieren."
 +"\n"
 +"3. Aufstellung einer Gozinto-Liste, in der alle Pfeile systematisch erfasst und nacheinander abgearbeitet werden."
 +"\n"
 +"      a. Beginne Liste mit dem am niedrigsten indizierten Knoten, in den Pfeile einm&uuml;nden."
 +"\n"
 +"      b. Trage in die Tabelle, die in die Eingangsknoten m&uuml;ndenden Ausgangsknoten mit den dazugehörigen Bewertungen, ein."
 +"\n"
 +"            i. Solange j noch Eingangsknoten besitzt: Gehe zum n&auml;chst h&ouml;heren Index i, welcher ein Ausgangsknoten von j ist."
 +"\n"
 +"            ii. Solange bis der letzt Knoten erreicht wurde: Gehe zum n&auml;chst h&ouml;heren Index j (j = j + 1)."
 +"\n"
 +"4. Anlegen einer Rechentabelle, mit"
 +"\n"
 +"       i = Index der Knoten,"
 +"\n"
 +"       V_i = Ausgangsvalenz (Zahl der nicht abgearbeiteten Pfeile, die aus einem Knoten hervor gehen) und"
 +"\n"
 +"       N_i = kumulierter Bedarfsmengenvektor (für N_i^0 ist N_i gleich dem Nettoprim&auml;rbedarf (Prim&auml;rbedarf - Lagerbestand))."
 +"\n"
 +"5. Gozinto-Liste von unten nach oben abarbeiten:"
 +"\n"
 +"       Sei k=1"
 +"\n"
 +"       solange bis alle Ausgangsknoten abgearbeitet sind"
 +"\n"
 +"       N_i^k = N_j^(k-1) * d_ij + N_i^(k-1)"
 +"\n"
 +"       b. V_i^k = V_i^(k-1) - 1"
 +"\n"
 +"       c. wenn neues j gesetzt wird: k = k + 1";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}