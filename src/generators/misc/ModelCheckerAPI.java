/*
 * ModelCheckerAPI.java
 * Timm Welz, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
 package generators.misc;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.modelcheckerCTL.*;
import generators.misc.modelcheckerCTL.ctl.CTLNode;
import generators.misc.modelcheckerCTL.ctl.CTLTreeBuilder;
import generators.misc.modelcheckerCTL.kripke.AnimalKripkeStructure;
import generators.misc.modelcheckerCTL.kripke.KripkeState;
import generators.misc.modelcheckerCTL.kripke.KripkeStructure;
import generators.misc.modelcheckerCTL.kripke.StructureBuilder;
import generators.misc.modelcheckerCTL.token.Type;

import java.awt.*;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class ModelCheckerAPI implements Generator {
    private Language lang;
    private String input_formula;
    private String input_terminals;
    private String input_states;
    private String input_initState;
    private String input_transitions;
    private GraphProperties graphProperties;
    private ArrayProperties terminalProperties;
    private CircleProperties outerCircleProperties;
    private CircleProperties innerCircleProperties;
    private TextDatabase textDatabase;
    private GUI gui;
    private SourceCodeProperties scTextProperties;

    public void init() {
        lang = new AnimalScript("Model Checking CTL Marking Algorithm", "Timm Welz", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        input_formula = (String) primitives.get("Input Formula");
        input_terminals = (String) primitives.get("Kripke Structure: Terminals");
        input_states = (String) primitives.get("Kripke Structure: States");
        input_initState = (String) primitives.get("Kripke Structure: Initial State");
        input_transitions = (String) primitives.get("Kripke Structure: Transitions");
        terminalProperties = (ArrayProperties) props.getPropertiesByName("TerminalProperties");
        outerCircleProperties = (CircleProperties) props.getPropertiesByName("outerCircleProperties");
        innerCircleProperties = (CircleProperties) props.getPropertiesByName("innerCircleProperties");
        graphProperties = (GraphProperties) props.getPropertiesByName("GraphProperties");
        textDatabase = new TextDatabaseDE();


         scTextProperties = new SourceCodeProperties();

//        input_formula = "AF(a)";
//        input_terminals = "{ ( q0,{a} ),( q1,{a, b} ),( q2,{a,b,c} ),( q3,{a,b} ) }";
//        input_transitions = "{ ( q0,q1),( q1,q2),( q1,q1),( q2,q2),( q2,q3),( q3,q0),( q3,q3) }";

        lang.setStepMode(true);
        CTLTreeBuilder treeBuilder = new CTLTreeBuilder();

        CTLNode node = treeBuilder.process(input_formula);
        List<String> minimizedSteps;
        minimizedSteps = treeBuilder.minimizeAll(node);

        TextProperties titleProps = new TextProperties();
        titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 1, 32));
        Text title = lang.newText(new Coordinates(8,2), "Model Checking CTL", "title", null, titleProps);

        runIntro(minimizedSteps);
        title.show();

        StructureBuilder structureBuilder = new StructureBuilder();
        KripkeStructure structure = structureBuilder.buildStructure(input_states, input_initState, input_transitions, input_terminals);
        int size = structure.getStates().size();
        gui = new GUI(lang, new DummyCalcChecker(structure).getSolutionCount(node));
        AnimalKripkeStructure animalKripkeStructure = new AnimalKripkeStructure(structure, gui.getStructureCoordinates(size), lang, props);
        gui.setAnimalKripkeStructure(animalKripkeStructure);

        AnimalModelChecker modelChecker = new AnimalModelChecker(animalKripkeStructure, lang, gui, (Color) innerCircleProperties.get("fillColor"), (Color) outerCircleProperties.get("fillColor"));
        gui.putTheFormula(node.toString());
        lang.nextStep("Algorithmus Start");
        List<KripkeState> result = modelChecker.runAlgo(node);

        gui.showResults(result, this.scTextProperties);
        lang.finalizeGeneration();
        return lang.toString();
    }

    void runIntro(List<String> minimizedSteps){
        SourceCode tmp, tmp2;
        String str;
        List<String> tmpSTR = textDatabase.getIntroPage("intro");
        tmp = lang.newSourceCode(new Coordinates(10,45), "introTXT", null, scTextProperties);
        for (int i = 0; i<tmpSTR.size(); i++) {
            str = tmpSTR.get(i);
            tmp.addCodeLine(str, "introTxt"+i, 0,null);
        }
        tmp.addCodeLine("", "introMin", 0,null);
        tmpSTR = textDatabase.getIntroPage("min");
        tmp.addCodeLine(tmpSTR.get(0), "introMin", 0,null);
        tmp.addCodeLine(tmpSTR.get(1), "introMin", 0,null);
        MatrixProperties minimizedProbs = new MatrixProperties();
        minimizedProbs.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
        String[][] matr1 = new String[5][2];
        for (int i = 2; i<7; i++) {
            matr1[i-2][0] = (i-1)+".)";
            matr1[i-2][1] = tmpSTR.get(i);
        }

        Coordinates tmpCoord = (Coordinates)tmp.getUpperLeft();
        StringMatrix minFormula = lang.newStringMatrix(new Coordinates(tmpCoord.getX()+50, tmpCoord.getY()+135), matr1, "minimizedTable", null, minimizedProbs);

        lang.nextStep("Einleitung");
        tmp2 = lang.newSourceCode(new Coordinates(tmpCoord.getX()+330, tmpCoord.getY()+96), "introTXT2", null, scTextProperties);
        tmp2.addCodeLine(tmpSTR.get(tmpSTR.size()-1), "introMin", 0,null);
        minimizedProbs = new MatrixProperties();
        minimizedProbs.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        String[][] matr2 = new String[6][2];
        for (int i = 0; i < 6; i++) {
            matr2[i][0] = "";
            matr2[i][1] = "";
        }
        tmpCoord = (Coordinates)tmp2.getUpperLeft();
        StringMatrix minMatrix = lang.newStringMatrix(new Coordinates(tmpCoord.getX()+60, tmpCoord.getY()+40), matr2, "minimizedTable", null, minimizedProbs);
        minMatrix.put(0,0,"Gegeben:",null,null);
        minMatrix.put(0,1,minimizedSteps.get(0),null,null);
        for (int i = 1; i < 6; i++) {
            lang.nextStep();
            minMatrix.put(i,0,i+".:",null,null);
            minMatrix.put(i,1,minimizedSteps.get(i),null,null);
        }
        lang.nextStep("Minimierte Formel");
//        tmp.hide();
//        tmp2.hide();
//        minMatrix.hide();
//        minFormula.hide();
        lang.hideAllPrimitives();

    }

    public String getName() {
        return "Model Checking CTL Marking Algorithm";
    }

    public String getAlgorithmName() {
        return "Model Checking CTL";
    }

    public String getAnimationAuthor() {
        return "Timm Welz";
    }

    public String getDescription() {
        return "Der Markierungsalgorithmus f\u00FCr CTL Model-Checking Probleme dient zur \u00DCberpr\u00FCfung,\n"+
                "ob eine Formel auf der gegebenen Kripke-Struktur erf\u00FCllt ist. Dazu markiert er alle Zust\u00E4nde,\n"+
                "in denen die Formel gilt, und iteriert (von innen nach au\u00DFen) \u00FCber alle Teilformeln. \n"+
                "Ist der Anfangszustand am Ende markiert, ist die Formel auf der Kripke-Struktur erf\u00FCllt.\n\n"+
               

               
                "Kurzer \u00DCberblick: Kripke Strukturen\n"+
                "sind an endliche Automaten angelehnt, kennen aber keinen Endzustand, ein Pfad in einer Struktur\n"+
                "ist somit eine UNENDLICHE Sequenz (von Zust\u00E4nden).\n"+
                "\n"+
                "Eine Kripke Struktur   M = ("+TextDatabase.stateSet+",I,R,L)   \u00FCber eine Menge P von Atomen ist definiert als:\n"+
                "-eine endliche Menge an Zust\u00E4nden \n"+TextDatabase.stateSet+"  und Anfangszust\u00E4nden I aus S\n"+
                "-einer \u00DCbergangsrelation R\n"+
                "-einer Abbildung L, welche jedem Zustand aus S eine Menge von P-Atomen zuweist \n\n"+


                
                "Kurzer \u00DCberblick: Computational Tree Logic\n"+
                "CTL erlaubt Aussagen \u00FCber das temporale Verhalten eines Systems. Dazu wird Aussagenlogik um\n"+
                "Pfad- und Zustandsquantoren erweitert. Ausgewertet wird immer bez\u00FCglich einer Kripke-Struktur und\n"+
                "einem Anfangszustand. Die Quantoren immer als Paar (Pfad-Zustand) auf.\n"+
                "\n"+
                "Pfadquantoren:\n"+
                "\tA  \"f\u00FCr jeden Pfad gilt\"\n"+
                "\tE  \"es existiert ein Pfad auf dem gilt\"\n"+
                  "\n"+
                  "Zustandsquantoren:\n"+
                  "\tX  \"im n\u00E4chsten Zustand gilt\"\n"+
                  "\tF  \"in Zukunft gilt irgendwann\"\n"+
                  "\tG  \"in Zukunft gilt immer\"\n"+
                  "\tU  \"es gilt eine Eigenschaft, bis eine andere gilt\"\n"+

              

                
                "Die genutzte minimale Quantorenmenge ist:  EX, AF, EU\n\n"+
                "Die Unformungsregeln lauten:\n"+
                Type.AG+TextDatabase.phi+"  =  "+Type.NOT+Type.EF+"("+Type.NOT+TextDatabase.phi+")\n"+
                Type.EF+TextDatabase.phi+"  =  "+Type.EU_START+"("+Type.TRUE+")"+Type.EU_END+"("+TextDatabase.phi+")\n"+
                Type.AU_START+"("+TextDatabase.phi1+")"+Type.AU_END+"("+TextDatabase.phi2+")  =  "+Type.NOT+"(("+Type.EU_START+"("+Type.NOT+TextDatabase.phi1+")"+Type.EU_END+"(("+Type.NOT+TextDatabase.phi1+")"+Type.AND+Type.NOT+"("+TextDatabase.phi2+"))"+Type.OR+Type.EG+"("+Type.NOT+TextDatabase.phi2+"))\n"+
                Type.EG+TextDatabase.phi+"  =  "+Type.NOT+Type.AF+"("+Type.NOT+TextDatabase.phi+")\n"+
                Type.AX+TextDatabase.phi+"  =  "+Type.NOT+Type.EX+"("+Type.NOT+TextDatabase.phi+")\n"+
                ""+
                "Dementsprechend wird die gegebene Formel transformiert:\n\n\n"+
                
                  
                  "Inhalt und Formeln basieren auf den Folien zu CTL\n"+
                  "aus dem Kurs \"Formale Methoden des Softwareentwurfs\"(Stand 2017) von Prof. Katzenbeisser";
    }

    public String getCodeExample() {
        String result = "";
        TextDatabase txtdb = new TextDatabaseDE();
        for (String str : txtdb.getIntroPage("codeExample")) {
            result = result+str+"\n";
        }
        return result;
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
}