/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL;

import generators.misc.modelcheckerCTL.ctl.CTLNode;
import generators.misc.modelcheckerCTL.kripke.KripkeState;
import generators.misc.modelcheckerCTL.kripke.KripkeStructure;

import java.util.List;

public class DummyCalcChecker extends ModelChecker {
    private int solutionCount = 0;

    public DummyCalcChecker(KripkeStructure structure) {
        super(structure);
    }

    public int getSolutionCount(CTLNode tree){
        solutionCount = 0;
        runAlgo(tree);
        return solutionCount;
    }

    @Override
    void signalFormula(CTLNode tree) {
        solutionCount++;
    }

    @Override
    void signalResult(CTLNode tree, List<KripkeState> result) {
    }
}
