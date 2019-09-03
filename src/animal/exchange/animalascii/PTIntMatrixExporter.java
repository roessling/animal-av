package animal.exchange.animalascii;

import animal.graphics.meta.PTMatrix;

public class PTIntMatrixExporter extends PTMatrixExporter {

	@Override
	String getDataAt(int r, int c,PTMatrix matrix) {
		return matrix.getElementAt(r, c);
	}

}
