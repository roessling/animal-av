package generators.misc.helpers;


/**
 * Encapsulates a grid item of the type 'wall'.
 * @author chollubetz
 *
 */
public class GridWallItem extends GridItem {

	public GridWallItem(Grid parent) {
		super(parent);
	}

	@Override
	public int getValue() {
		return GridItem.WALL;
	}

	@Override
	public void setValue(int value) throws Exception {
		throw new Exception("Can't change value of Wall");
	}

}
