package animal.handler;

import animal.graphics.meta.PTMatrix;

public class StringMatrixHandler extends MatrixHandler {

	@Override
	protected void setDataAt(int row, int column, String value, PTMatrix matrix) {
		matrix.setElementAt(row, column, value);
	}
}
