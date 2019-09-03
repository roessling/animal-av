package generators.misc.helpers;


/**
 * Encapsulates a grid item of the type 'source'.
 * @author chollubetz
 *
 */
public class GridSourceItem extends GridItem {

	public GridSourceItem(Grid parent) {
		super(parent);
	}

	@Override
	public int getValue() {
		return GridItem.SOURCE;
	}

	@Override
	public void setValue(int value) throws Exception {
		throw new Exception("Can't change value of Source");
	}


}
