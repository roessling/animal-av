package generators.misc.helpers;


/**
 * Encapsulates a grid item of the type 'train'.
 * @author chollubetz
 *
 */
public class GridTrainItem extends GridItem {

	public GridTrainItem(Grid parent) {
		super(parent);
	}

	@Override
	public int getValue() {
		return GridItem.TRAIN;
	}

	@Override
	public void setValue(int value) throws Exception {
		throw new Exception("Can't change value of Train");
	}

}
