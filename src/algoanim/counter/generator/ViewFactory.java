package algoanim.counter.generator;

import algoanim.counter.model.FourValueCounter;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.FourValueView;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.generators.Language;
import algoanim.properties.CounterProperties;
import algoanim.util.Node;

/**
 * Factory class to create Counters.
 * 
 * @author Axel Heimann
 */

public class ViewFactory {

	/**
	 * Creates and returns a view for an <code>AccessAndAssignmentCounter</code>
	 * or <code>FourValueCounter</code>
	 * 
	 * 
	 * @param lang
	 *            the <code>Language</code> used to create Counter
	 * @param coord
   *          the Node representing the top left corner of the view
	 * @param counter
	 *            the <code>AbstractCounter</code> whose values are visualized
	 * @param CounterProperties
	 *            the <code>CounterProperties</code> which define colors etc. of
	 *            the visualization
	 * @param bar
	 *            true -> Values visualized as bar false -> Values not
	 *            visualized as bar
	 * @param number
	 *            true -> Values visualized as number false -> Values not
	 *            visualized as number
	 * @param valueNames
	 *            String Array to customize the names of the counted values
	 *            (e.g. "Zuweisungen" instead of "assignments"). The Array must
	 *            have 2 or 4 entries dependent on the number of visualized
	 *            values.
	 * @return TwoValueView the <code>FourValueCounter</code> which visualizes
	 *         the counted values.
	 */
	public TwoValueView createView(Language lang, Node coord,
			TwoValueCounter counter, CounterProperties CounterProperties,
			boolean bar, boolean number, String[] valueNames) {
		TwoValueView tvw = new TwoValueView(lang, coord, number, bar,
				CounterProperties, valueNames);
		counter.addObserver(tvw);
		return tvw;
	}

	/**
	 * Creates and returns a view for an <code>FourValueCounterCounter</code> or
	 * <code>FourValueCounter</code>
	 * 
	 * 
	 * @param lang
	 *            the <code>Language</code> used to create Counter
	 * @param coord
   *          the Node representing the top left corner of the view
	 * @param counter
	 *            the <code>AbstractCounter</code> whose values are visualized
	 * @param CounterProperties
	 *            the <code>CounterProperties</code> which define colors etc. of
	 *            the visualization
	 * @param bar
	 *            true -> Values visualized as bar false -> Values not
	 *            visualized as bar
	 * @param number
	 *            true -> Values visualized as number false -> Values not
	 *            visualized as number
	 * @param valueNames
	 *            String Array to customize the names of the counted values
	 *            (e.g. "Zuweisungen" instead of "assignments"). The Array must
	 *            have 2 or 4 entries dependent on the number of visualized
	 *            values.
	 * @return TwoValueView the <code>FourValueCounter</code> which visualizes
	 *         the counted values.
	 */
	public FourValueView createView(Language lang, Node coord,
			FourValueCounter counter, CounterProperties CounterProperties,
			boolean bar, boolean number, String[] valueNames) {
		FourValueView fvw = new FourValueView(lang, coord, number, bar,
				CounterProperties, valueNames);
		counter.addObserver(fvw);
		return fvw;
	}

}
