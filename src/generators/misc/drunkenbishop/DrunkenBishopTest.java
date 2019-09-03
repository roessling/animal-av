package generators.misc.drunkenbishop;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.Test;

@SuppressWarnings("static-method")
public class DrunkenBishopTest {
	@Test
	public void testGetMovementVectorRotatedStandardGrid() {
		movementInGrid(17, 9);
	}
	
	@Test
	public void testGetMovementVectorStandardGrid() {
		movementInGrid(9, 17);
	}

	@Test
	public void testGetMovementVector5x5Grid() {
		movementInGrid(5, 5);
	}

	@Test
	public void testGetMovementVector3x3Grid() {
		movementInGrid(3, 3);
	}

	private void movementInGrid(int rows, int cols) {
		// start position of the bishop
		int[] move = DrunkenBishop.getMovementVector(((rows - 1) / 2) * cols
				+ ((cols - 1) / 2), rows, cols);
		assertEquals(-(cols + 1), move[0]);
		assertEquals(-(cols - 1), move[1]);
		assertEquals((cols - 1), move[2]);
		assertEquals((cols + 1), move[3]);

		// top-left corner
		move = DrunkenBishop.getMovementVector(0, rows, cols);
		assertEquals(0, move[0]);
		assertEquals(1, move[1]);
		assertEquals(cols, move[2]);
		assertEquals(cols + 1, move[3]);

		// top-right corner
		move = DrunkenBishop.getMovementVector(cols - 1, rows, cols);
		assertEquals(-1, move[0]);
		assertEquals(0, move[1]);
		assertEquals(cols - 1, move[2]);
		assertEquals(cols, move[3]);

		// bottom-left corner
		move = DrunkenBishop.getMovementVector((rows - 1) * cols, rows, cols);
		assertEquals(-cols, move[0]);
		assertEquals(-(cols - 1), move[1]);
		assertEquals(0, move[2]);
		assertEquals(1, move[3]);

		// bottom-right corner
		move = DrunkenBishop.getMovementVector(rows * cols - 1, rows, cols);
		assertEquals(-(cols + 1), move[0]);
		assertEquals(-cols, move[1]);
		assertEquals(-1, move[2]);
		assertEquals(0, move[3]);

		// top row
		move = DrunkenBishop.getMovementVector((cols - 1) / 2, rows, cols);
		assertEquals(-1, move[0]);
		assertEquals(1, move[1]);
		assertEquals((cols - 1), move[2]);
		assertEquals((cols + 1), move[3]);

		// bottom row
		move = DrunkenBishop.getMovementVector(((rows * cols) - 1) - (cols - 1)
				/ 2, rows, cols);
		assertEquals(-(cols + 1), move[0]);
		assertEquals(-(cols - 1), move[1]);
		assertEquals(-1, move[2]);
		assertEquals(1, move[3]);

		// left edge
		move = DrunkenBishop.getMovementVector(((rows - 1) / 2) * cols, rows,
				cols);
		assertEquals(-cols, move[0]);
		assertEquals(-(cols - 1), move[1]);
		assertEquals(cols, move[2]);
		assertEquals(cols + 1, move[3]);

		// right edge
		move = DrunkenBishop.getMovementVector((((rows - 1) / 2) + 1) * cols
				- 1, rows, cols);
		assertEquals(-(cols + 1), move[0]);
		assertEquals(-cols, move[1]);
		assertEquals((cols - 1), move[2]);
		assertEquals(cols, move[3]);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateFingerprintTooShort() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "00::af::23::12::56");
		db.validateInput(null, map);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateFingerprintTooLong() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "ff:e4:cf:6b:b9:2b:ec:63:bd:2c:81:16:d0:40:fd:16:74");
		db.validateInput(null, map);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateFingerprintTooFewWords() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "e4acf:6b:b9:2b:ec:63:bd:2c:81:16:d0:40:fd:16:74");
		db.validateInput(null, map);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateWordTooShort() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "e:4cf:6b:b9:2b:ec:63:bd:2c:81:16:d0:40:fd:16:74");
		db.validateInput(null, map);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateWordTooLong() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "e4c:f:6b:b9:2b:ec:63:bd:2c:81:16:d0:40:fd:16:74");
		db.validateInput(null, map);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateNonHexWord() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "g4:cf:6b:b9:2b:ec:63:bd:2c:81:16:d0:40:fd:16:74");
		db.validateInput(null, map);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateNegativeHexWord() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "-4:cf:6b:b9:2b:ec:63:bd:2c:81:16:d0:40:fd:16:74");
		db.validateInput(null, map);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateCoinsTooShort() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "e4:cf:6b:b9:2b:ec:63:bd:2c:81:16:d0:40:fd:16:74");
		map.put("coins", " #");
		db.validateInput(null, map);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateBoardRowOdd() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "e4:cf:6b:b9:2b:ec:63:bd:2c:81:16:d0:40:fd:16:74");
		map.put("coins", " #SE");
		map.put("board-rows", 4);
		db.validateInput(null, map);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testValidateBoardColumnOdd() {
		DrunkenBishop db = new DrunkenBishop();
		Hashtable<String, Object> map = new Hashtable<String, Object>();
		map.put("fingerprint", "e4:cf:6b:b9:2b:ec:63:bd:2c:81:16:d0:40:fd:16:74");
		map.put("coins", " #SE");
		map.put("board-rows", 5);
		map.put("board-columns", 4);
		db.validateInput(null, map);
	}

}
