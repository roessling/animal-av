package generators.searching.SimulatedAnnealing.Util;

import algoanim.counter.enumeration.ControllerEnum;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.generators.Language;
import algoanim.properties.CounterProperties;
import algoanim.util.Node;

/**
 * Created by Philipp Becker on 08.08.15.
 * In the original TwoValueView the INCREMENT_LENGTH is fixed to 5 (its final and initialized directly during
 * declaration) which is kind of inflexible (and really unnecessary). So I made my own Counter View
 * (without Blackjack and Hookers, but) with one time settable (in Constructor) incrementation length for both
 * bars separably!
 */
public class MyCounterView extends TwoValueView {
    /**
     * One time settable incrementation length
     */
    private final int assigenmentBarInc;
    private final int accessBarInc;


    public MyCounterView(Language lang, Node coord, boolean number, boolean bar,
                        CounterProperties counterProperties, String[] valueNames, int accessBarInc,
                        int assigenmentBarInc) {
        super(lang, coord, number, bar, counterProperties, valueNames);
        this.accessBarInc = accessBarInc;
        this.assigenmentBarInc = assigenmentBarInc;
    }

    /**
     * Adapting relevant method!
     * @param valueType
     * @param value
     */
    @Override
    public void update(ControllerEnum valueType, int value) {
        switch (valueType) {
            case assignments:
                assignments = assignments + value;
                assignmentsNumber.setText(String.valueOf(assignments), null, null);
                assignmentsBar.moveBy("translate #2", assigenmentBarInc * value, 0,
                        null, null);
                break;
            case access:
                accesses = accesses + value;
                accessesNumber.setText(String.valueOf(accesses), null, null);
                accessesBar.moveBy("translate #2", accessBarInc * value, 0, null,
                        null);
                break;
            default:
                try {
                    throw new IllegalArgumentException("ValuetypeNotVisualized");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
        }
    }

}
