package generators.misc.helpers;


/**
 * Encapsulates a regular grid item.
 * @author chollubetz
 *
 */
public class GridNormalItem extends GridItem {

	int value;
	
	public GridNormalItem(Grid parent) {
		super(parent);
		value = 0;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public void setValue(int value) throws Exception {
		this.value = value;
	}

}
