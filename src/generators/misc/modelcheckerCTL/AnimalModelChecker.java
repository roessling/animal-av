/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL;

import algoanim.primitives.generators.Language;
import generators.misc.modelcheckerCTL.ctl.CTLNode;
import generators.misc.modelcheckerCTL.kripke.AnimalKripkeState;
import generators.misc.modelcheckerCTL.kripke.AnimalKripkeStructure;
import generators.misc.modelcheckerCTL.kripke.KripkeState;
import generators.misc.modelcheckerCTL.token.Token;
import generators.misc.modelcheckerCTL.token.Type;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.*;
import java.util.List;

public class AnimalModelChecker extends ModelChecker {
    private Color firstHighlight = Color.RED;
    private Color secondHighlight = Color.BLUE;
    GUI gui;
    Language lang;
    int questionType;
    double questionLikelihood = 0.3;

    public AnimalModelChecker(AnimalKripkeStructure structure, Language lang, GUI gui, Color firstHighlight, Color secondHighlight) {
        super(structure);
        this.lang = lang;
        this.firstHighlight = firstHighlight;
        this.secondHighlight = secondHighlight;
        this.gui = gui;

        QuestionGroupModel resultStates = new QuestionGroupModel("resultStates",2);
        lang.addQuestionGroup(resultStates);
        QuestionGroupModel singleResult = new QuestionGroupModel("singleResult",2);
        lang.addQuestionGroup(singleResult);

    }

    void showInAnimalDebugCode(String str){
        //gui.setInfoText(str);
    }

    @Override
    void signalFormula(CTLNode tree) {
        questionType = (int)(Math.random()*3);
        gui.putSolutionFormula(tree.toString());
        gui.showFormulaText(tree.getToken().getType());

        if(Math.random()<questionLikelihood && (tree.getToken().getType()!=Type.TERMINAL)){
            List<KripkeState> result = new DummyCalcChecker(this.structure).runAlgo(tree);
            if(questionType==0) {
                MultipleSelectionQuestionModel multipleSelectionQuestionModel = new MultipleSelectionQuestionModel("resultQ" + Math.random());
                multipleSelectionQuestionModel.setPrompt("Welche Zustände werden durch " + tree.toString() + " markiert?");
                String answer = Util.printSimpleStates(result);

                for (KripkeState state:
                        structure.getStates()) {
                    multipleSelectionQuestionModel.addAnswer(state.getName(), result.contains(state)?1:-1*structure.getStates().size(), state.getName());

                }
                multipleSelectionQuestionModel.addAnswer("keine", result.size()==0?1:-1*structure.getStates().size(), "keine");
                multipleSelectionQuestionModel.setGroupID("resultStates");

                lang.addMSQuestion(multipleSelectionQuestionModel);
            }else {
                int state = (int)(Math.random()*structure.getStates().size());
                TrueFalseQuestionModel trueFalseQuestionModel = new TrueFalseQuestionModel("singleResult"+Math.random(), result.contains(structure.getState(state)), 1);
                trueFalseQuestionModel.setPrompt("Für " +tree.toString()+", ist Zustand "+structure.getState(state).getName()+" in der Ergebnismenge?");
//                trueFalseQuestionModel.setCorrectAnswer(result.contains(structure.getState(state)));
                lang.addTFQuestion(trueFalseQuestionModel);
            }
//            lang.nextStep();
        }
        lang.nextStep();
    }

    @Override
    void signalResult(CTLNode tree, List<KripkeState> result) {
//        showInAnimalDebugCode("result signal pre "+Util.printSimpleStates(result));

        gui.putSolutionStates(Util.printSimpleStates(result));
        showInAnimalDebugCode("result signal inter "+Util.printSimpleStates(result));
        if (result.size() > 0) {
            for (KripkeState state : result) {
                ((AnimalKripkeStructure) this.structure).highlightState(state.getId(), Color.YELLOW);
            }
//            lang.nextStep();
            showInAnimalDebugCode("result signal gr\u00f6\u00dfer 0 " +Util.printSimpleStates(result));
        }
        lang.nextStep("  L\u00f6se " +tree.toString());
        ((AnimalKripkeStructure) this.structure).unHighlightAll();
        for (KripkeState state : this.structure.getStates()) {
            if (state instanceof AnimalKripkeState) {
                ((AnimalKripkeState) state).unMarkState();
            }
        }
//        lang.nextStep();
    }

    @Override
    void signalSingleLeft(KripkeState state) {
        if (state instanceof AnimalKripkeState) {
            ((AnimalKripkeState) state).markState(firstHighlight);
        }
        lang.nextStep();
    }

    @Override
    void signalSingleRight(KripkeState state) {
        if (state instanceof AnimalKripkeState) {
            ((AnimalKripkeState) state).markState(secondHighlight);
        }
        lang.nextStep();
    }

    @Override
    void signalSetLeft(List<KripkeState> states) {
        if (states.size() > 0) {
            for (KripkeState state : states) {
                if (state instanceof AnimalKripkeState) {
                    ((AnimalKripkeState) state).markState(firstHighlight);
                }
            }
            lang.nextStep();
        }
    }

    @Override
    void signalSetRight(List<KripkeState> states) {
        if (states.size() > 0) {
            for (KripkeState state : states) {
                if (state instanceof AnimalKripkeState) {
                    ((AnimalKripkeState) state).markState(secondHighlight);
                }
            }
            lang.nextStep();
        }
    }

    @Override
    void signalTerminalsInStates(Token token, List<KripkeState> states) {
        for (KripkeState state : states) {
            if (state instanceof AnimalKripkeState) {
                ((AnimalKripkeState) state).highlightTerminal(token);
            }
        }
        showInAnimalDebugCode("signal terminals "+token.getFormula()+" "+Util.printSimpleStates(states));
        lang.nextStep();
    }

    @Override
    void signalEdge(KripkeState from, KripkeState to) {
        if (structure instanceof AnimalKripkeStructure) {
            ((AnimalKripkeStructure) structure).highlightEdge(from.getId(), to.getId(), Color.GREEN);
        }
//        lang.nextStep();
    }

    @Override
    void signalEdges(List<KripkeState> from, List<KripkeState> to) {
        if (from.size() > 0) {
            for (KripkeState fromState : from) {
                for (KripkeState toState : to) {
                    ((AnimalKripkeStructure) structure).highlightEdge(fromState.getId(), toState.getId(), Color.GREEN);
                }
            }
//            lang.nextStep();
        }

    }

//    public static void main(String[] args) {
//        StructureBuilder structureBuilder = new StructureBuilder();
//        String stateStr = "{q0,q1,q2,q3}";
//        String relStr = "{ ( q0,q1),( q0,q2),( q0,q3),( q1,q1),( q1,q2),( q2,q1),( q2,q2),( q3,q3) }";
//        String termStr = "{ ( q0,{a} ),( q1,{a, b} ),( q2,{b,c} ),( q3,{b} ) }";
//        KripkeStructure structure = structureBuilder.buildStructure(stateStr, "{q0}", relStr, termStr);
//        Language lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "hallo", "Timm Welt", 400, 400);
//        lang.setStepMode(true);
//        AnimalKripkeStructure animalKripkeStructure = new AnimalKripkeStructure(structure, lang, CoordinatesPatternBuilder.getCircularPattern(structure.getStates().size(), 200, 200, 150), Color.RED, Color.BLUE);
//        CTLTreeBuilder treeBuilder = new CTLTreeBuilder();
//        String str = "AG(A(a)U(b))";
////        str = "EG((EX(b))->(AG(b)))";
////        str = "AF((c)->(AG(EX(EX(c)))))";
////        str = "(EF(AX((b)->(c))))";
////        str = "(A(a)U(b))";
////        str = "AF((c)->(AG(EX(EX(c)))))";
////        str = "(AG(A(a)U(b)))";
////        str = "(EG(b))";
////        str = "(a)&(b)";
//        CTLNode node = treeBuilder.process(str);
//        treeBuilder.minimizeAll(node);
//
//        AnimalModelChecker modelChecker = new AnimalModelChecker(animalKripkeStructure, lang);
//        lang.nextStep();
//        modelChecker.runAlgo(node);
//        System.out.println(lang.toString());
//        Animal.startAnimationFromAnimalScriptCode(lang.toString());
//    }
}
