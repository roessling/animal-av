package algoanim.primitives;

import algoanim.primitives.generators.GeneratorInterface;
import algoanim.util.DisplayOptions;


/**
 * Base class for all concrete arrays.
 * @author Jens Pfau, Stephan Mehlhase
 */
public abstract class MatrixPrimitive extends CountablePrimitive {
	protected int nrRows = 0;
	protected int nrCols = 0;
	
	/**
	 * @param g the appropriate code <code>Generator</code>.
	 * @param display [optional] the <code>DisplayOptions</code> of this
	 * <code>ArrayPrimitive</code>.
	 */
	public MatrixPrimitive(GeneratorInterface g, DisplayOptions display) {
		super(g, display);
	}
	
	/**
	 * Returns the number of rows of the internal matrix.
	 * @return the number of rows of the internal array.
	 */
	public int getNrRows() {
		return nrRows;
	}
	
	/**
	 * Returns the number of columns in row <em>row</em> of the internal matrix.
	 * @return the number of columns in row <em>row</em> of the internal array.
	 */
	public int getNrCols() {
		return nrCols;
	}
}
