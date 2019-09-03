/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL;

import generators.misc.modelcheckerCTL.ctl.CTLNode;
import generators.misc.modelcheckerCTL.ctl.CTLTreeBuilder;
import generators.misc.modelcheckerCTL.kripke.KripkeState;
import generators.misc.modelcheckerCTL.kripke.KripkeStructure;
import generators.misc.modelcheckerCTL.kripke.StructureBuilder;
import generators.misc.modelcheckerCTL.token.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ModelChecker {

    protected KripkeStructure structure;
    private HashSet<String> seenFormulas;
    private boolean signalCurrentFormula = true;

    public ModelChecker(KripkeStructure structure) {
        this.structure = structure;
        this.seenFormulas = new HashSet<>();
    }

    public List<KripkeState> runAlgo(CTLNode tree) {
        List<KripkeState> result = null;
        if (tree == null) {
            result = null;
        } else {
            List<KripkeState> currentLeft = runAlgo(tree.getLeft());
            List<KripkeState> currentRight = runAlgo(tree.getRight());
            signalCurrentFormula = !seenFormulas.contains(tree.toString());
            if (signalCurrentFormula) signalFormula(tree);
            result = evaluate(tree, currentLeft, currentRight);
            if (signalCurrentFormula) signalResult(tree, result);
            if (signalCurrentFormula) seenFormulas.add(tree.toString());
//            System.out.println(tree.toString() + " : " + Util.printSimpleStates(result));

        }
        return result;
    }

    List<KripkeState> evaluate(CTLNode node, List<KripkeState> currentLeft, List<KripkeState> currentRight) {
        List<KripkeState> result = null;
        switch (node.getToken().getType()) {
            case TERMINAL:
                result = evaluateTerminal(node.getToken());
                break;
            case TRUE:
                result = structure.getStates();
                break;
            case FALSE:
                result = new ArrayList<>();
                break;
            case NOT:
                result = evaluateNot(currentRight);
                break;
            case OR:
                result = evaluateOr(currentLeft, currentRight);
                break;
            case AND:
                result = evaluateAnd(currentLeft, currentRight);
                break;
            case IMPL:
                result = evaluateImpl(currentLeft, currentRight);
                break;
            case EX:
                result = evaluateEX(currentRight);
                break;
            case AF:
                result = evaluateAF(currentRight);
                break;
            case EU:
                result = evaluateEU(currentLeft, currentRight);
                break;
            default:
                result = new ArrayList<>();
        }
        return result;
    }

    List<KripkeState> evaluateTerminal(Token token) {
        List<KripkeState> result = new ArrayList<>();
        if (token != null) {
            for (KripkeState state : structure.getStates()) {
                if (state.getTerminals().contains(token)) {
                    result.add(state);
//                    System.out.println("found " + token.getFormula() + " in " + state.getName());
                }
            }
            if (signalCurrentFormula) signalTerminalsInStates(token, result);
        } else throw new IllegalArgumentException();
        return result;
    }

    List<KripkeState> evaluateNot(List<KripkeState> states) {
        List<KripkeState> result = new ArrayList<>();
        if (states != null) {
            for (KripkeState state : structure.getStates()) {
                if (!states.contains(state)) {
                    result.add(state);
                }
            }
            if (signalCurrentFormula) signalSetLeft(result);
        } else throw new IllegalArgumentException();
        return result;
    }

    List<KripkeState> evaluateOr(List<KripkeState> currentLeft, List<KripkeState> currentRight) {
        List<KripkeState> result = new ArrayList<>();
        if (currentLeft != null && currentRight != null) {
            for (KripkeState state : structure.getStates()) {
                if (currentLeft.contains(state) || currentRight.contains(state)) {
                    result.add(state);
                }
            }
            if (signalCurrentFormula) signalSetLeft(currentLeft);
            if (signalCurrentFormula) signalSetRight(currentRight);
        } else throw new IllegalArgumentException();
        return result;
    }

    List<KripkeState> evaluateAnd(List<KripkeState> currentLeft, List<KripkeState> currentRight) {
        List<KripkeState> result = new ArrayList<>();
        if (currentLeft != null && currentRight != null) {
            for (KripkeState state : structure.getStates()) {
                if (currentLeft.contains(state) && currentRight.contains(state)) {
                    result.add(state);
                }
            }
            if (signalCurrentFormula) signalSetLeft(currentLeft);
            if (signalCurrentFormula) signalSetRight(currentRight);
        } else throw new IllegalArgumentException();
        return result;
    }

    List<KripkeState> evaluateImpl(List<KripkeState> currentLeft, List<KripkeState> currentRight) {
        List<KripkeState> result = new ArrayList<>();
        if (currentLeft != null && currentRight != null) {
            result = evaluateNot(currentLeft);
            for (KripkeState state : structure.getStates()) {
                if (!result.contains(state) && currentRight.contains(state)) {
                    result.add(state);
                }
            }
            if (signalCurrentFormula) signalSetRight(currentRight);
        } else throw new IllegalArgumentException();
        return result;
    }

    List<KripkeState> evaluateEX(List<KripkeState> currentRight) {
        List<KripkeState> result = new ArrayList<>();
        List<KripkeState> tmp;
        List<KripkeState> listforSignaling;
        if (currentRight != null) {
            for (KripkeState state : currentRight) {
                tmp = structure.getPredecessorsOf(state);
                listforSignaling = new ArrayList<>();
                for (KripkeState state2 : tmp) {
                    if (!result.contains(state2)) {
                        result.add(state2);
                        listforSignaling.add(state2);
                    }
                }
                tmp = new ArrayList<>();
                tmp.add(state);
                if (signalCurrentFormula) signalEdges(listforSignaling, tmp);
                if (signalCurrentFormula) signalSetRight(listforSignaling);
            }
        } else throw new IllegalArgumentException();
        return result;
    }

    List<KripkeState> evaluateAF(List<KripkeState> currentRight) {
        List<KripkeState> result = new ArrayList<>();
        List<KripkeState> tmp;
        List<KripkeState> tmpListForSignaling;
        boolean foundState = true;
        boolean allSucc = false;
        if (currentRight != null) {
            result.addAll(currentRight);
            if (signalCurrentFormula) signalSetRight(currentRight);

            while (foundState) {
                foundState = false;
                for (KripkeState state : structure.getStates()) {
                    tmp = structure.getSuccessorsOf(state);
                    if (tmp.size() > 0) {
                        allSucc = true;
                        for (KripkeState state2 : tmp) {
                            allSucc = allSucc && result.contains(state2);
//                            System.out.println(state2.getName() + " ist in result: " + allSucc);
                        }
                        if (allSucc && !result.contains(state)) {
                            foundState = true;
                            result.add(state);
                            tmpListForSignaling = new ArrayList<>();
                            tmpListForSignaling.add(state);
                            if (signalCurrentFormula) signalEdges(tmpListForSignaling, tmp);
                            if (signalCurrentFormula) signalSingleRight(state);
                        }
                    }
                }
            }

        } else throw new IllegalArgumentException();
        return result;
    }

    List<KripkeState> evaluateEU(List<KripkeState> currentLeft, List<KripkeState> currentRight) {
        List<KripkeState> result = new ArrayList<>();
        List<KripkeState> tmp;
        boolean foundState = true;

        if (currentRight != null && currentLeft != null) {
            result.addAll(currentRight);
            if (signalCurrentFormula) signalSetRight(currentRight);
            while (foundState) {
                foundState = false;

                for (KripkeState state : structure.getStates()) {
                    if (result.contains(state)) {
                        tmp = structure.getPredecessorsOf(state);
                        if (tmp.size() > 0) {

                            for (KripkeState state2 : tmp) {

                                //                                    System.out.println(state2.getName() + " ist in result: " + allPre);
                                if (currentLeft.contains(state2) && !result.contains(state2)) {
                                    foundState = true;
                                    result.add(state2);
                                    if (signalCurrentFormula) {
                                        signalEdge(state2, state);
                                        signalSingleRight(state2);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else throw new IllegalArgumentException();
        return result;
    }

    void signalFormula(CTLNode tree) {
        System.out.print(tree.toString() + " : ");
    }

    void signalResult(CTLNode tree, List<KripkeState> result) {
        System.out.println(Util.printSimpleStates(result));
    }

    void signalSingleLeft(KripkeState state) {

    }

    void signalSingleRight(KripkeState state) {

    }

    void signalSetLeft(List<KripkeState> states) {

    }

    void signalSetRight(List<KripkeState> states) {

    }

    void signalTerminalsInStates(Token token, List<KripkeState> states) {

    }

    void signalEdge(KripkeState from, KripkeState to) {

    }

    void signalEdges(List<KripkeState> from, List<KripkeState> to) {

    }


    public static void main(String[] args) {
        StructureBuilder structureBuilder = new StructureBuilder();
        String stateStr = "{q0,q1,q2,q3}";
        String relStr = "{ ( q0,q1),( q0,q2),( q0,q3),( q1,q1),( q1,q2),( q2,q1),( q2,q2),( q3,q3) }";
        String termStr = "{ ( q0,{a} ),( q1,{a, b} ),( q2,{b,c} ),( q3,{b} ) }";
        KripkeStructure structure = structureBuilder.buildStructure(stateStr, "{q0}", relStr, termStr);
        CTLTreeBuilder treeBuilder = new CTLTreeBuilder();
        String str = "AG(A(a)U(b))";
//        str = "EG((EX(b))->(AG(b)))";
//        str = "AF((c)->(AG(EX(EX(c)))))";
//        str = "(EF(AX((b)->(c))))";
//        str = "(A(a)U(b))";
//        str = "AF((c)->(AG(EX(EX(c)))))";
//        str = "(AG(A(a)U(b)))";
//        str = "(EG(b))";
        CTLNode node = treeBuilder.process(str);
        treeBuilder.minimizeAll(node);

        ModelChecker modelChecker = new ModelChecker(structure);
        modelChecker.runAlgo(node);
//        System.out.println(structure.getPredecessorsOf(structure.getState(1)));

    }

}
