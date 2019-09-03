package generators.cryptography.util;

import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.util.Offset;

public class KeyIsPermutation implements KeyInterface {
	private int[] key = null;

	public KeyIsPermutation(int[] data) {
		this.key = data;
	}

	@Override
	public void drawKey(Language lang, String baseIDRef) {
		// TODO Auto-generated method stub
		int[] arr = new int[this.key.length];
		for (int i = 0; i < this.key.length; i++) {
			arr[i] = i + 1;
		}
		int[][] matrixValue = { arr, this.key };
		MatrixProperties props = new MatrixProperties();
		props.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
		props.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		// props.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
		props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);

		IntMatrix matrix = lang.newIntMatrix(new Offset(7, -20, baseIDRef,
				AnimalScript.DIRECTION_NE), matrixValue, "key", null, props);

	}

	@Override
	public int[] calculateWithDecryptionKey(String initial_vector) {
		// TODO Auto-generated method stub
		int[] init_array = new int[initial_vector.length()];
		for (int j = 0; j < init_array.length; j++) {
			init_array[j] = Integer.valueOf(String.valueOf(initial_vector
					.charAt(j)));
		}
		int[] vector_O = new int[this.key.length];
		for (int i = 0; i < vector_O.length; i++) {
			vector_O[i] = init_array[this.key[i]-1];
		}
		return vector_O;
	}

}
