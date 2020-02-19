package generators.searching.tabusearch;

import algoanim.primitives.StringArray;
import algoanim.primitives.Text;

/**
 * This class contains some information about specific neighbors in the
 * neighborhood.
 * 
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 *
 */
public class Neighbor {

	public int cost;
	public String swap;
	public StringArray list;
	public Text costText;
	public Text swapText;

	/**
	 * Constructor to set neighbor attributes.
	 * 
	 * @param cost
	 *            cost of this solution
	 * @param swap
	 *            swap that was made to receive this solution
	 * @param list
	 *            list representation of roundtrip
	 * @param costText
	 *            text field for costs
	 * @param swapText
	 *            text field for swap
	 */
	public Neighbor(int cost, String swap, StringArray list, Text costText, Text swapText) {
		this.cost = cost;
		this.swap = swap;
		this.list = list;
		this.costText = costText;
		this.swapText = swapText;
	}
}
