/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generators.hashing.helpers;

import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.util.TicksTiming;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author Matthias Mettel
 */
public class FNV1aUtils {
	public static boolean IsHexString(String str) {
		try {
			new BigInteger(str, 16);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * Utility Method to check inputs
	 *
	 * @param val boolean value to check
	 */
	public static void Assert(boolean val) {
		if (!val) {
			throw new AssertionError();
		}
	}

	/**
	 * creates a string array with a specific content
	 *
	 * @param length array length
	 * @param fillin content to put into the array
	 * @return return string array
	 */
	public static String[] createFilledStringArray(int length, String fillin) {
		String[] str = new String[length];

		Arrays.fill(str, fillin);

		return str;
	}

	/**
	 * highlight a array element
	 *
	 * @param array
	 * @param idx
	 */
	public static void highlight(StringArray array, int idx) {
		array.highlightCell(idx, null, null);
		array.highlightElem(idx, null, null);
	}

	/**
	 * unhighlight a array element
	 *
	 * @param array
	 * @param idx
	 */
	public static void unhighlight(StringArray array, int idx) {
		array.unhighlightCell(idx, null, null);
		array.unhighlightElem(idx, null, null);
	}

	/**
	 * highlight elements in range of the array
	 * @param lang
	 * @param array
	 * @param start
	 * @param end
	 */
	public static void highlight(Language lang, StringArray array, int start, int end) {
		lang.addLine("highlightArrayCell on \"" + array.getName() + "\" from " + start + " to " + end);
		lang.addLine("highlightArrayElem on \"" + array.getName() + "\" from " + start + " to " + end);
	}

	/**
	 * unhighlight elements in range of the array
   * @param lang
   * @param array
   * @param start
   * @param end
	 */
	public static void unhighlight(Language lang, StringArray array, int start, int end) {
		lang.addLine("unhighlightArrayCell on \"" + array.getName() + "\" from " + start + " to " + end);
		lang.addLine("unhighlightArrayElem on \"" + array.getName() + "\" from " + start + " to " + end);
	}

	public static void highlightAll(Language lang, StringArray array) {
		highlight(lang, array, 0, array.getLength() - 1);
	}

	public static void unhighlightAll(Language lang, StringArray array) {
		unhighlight(lang, array, 0, array.getLength() - 1);
	}

	public static String[] split1Char(String str) {
		return Arrays.copyOfRange(str.split(""), 0, str.length());
	}

	public static String join(String[] r, String d, int start, int len) {
		StringBuilder sb = new StringBuilder();
		int i;
		for (i = start; i < start + len - 1; i++) {
			sb.append(r[i]).append(d);
		}
		sb.append(r[i]);
		return sb.toString();
	}

	public static String join(String[] r, int start, int len) {
		return join(r, "", start, len);
	}

	public static String join(String[] r, String d) {
		return join(r, d, 0, r.length);
	}

	public static String join(String[] r) {
		return join(r, "");
	}

	public static String[] IntToStrArray(BigInteger i, int len) {
		return split1Char(IntToStr(i, len));
	}

	public static String IntToStr(BigInteger i, int len) {
		String str = String.format("%0" + len + "X", i);
		return str.substring(str.length() - len);
	}

	public static TicksTiming GetDelayed(int index, TicksTiming timing) {
		return GetDelayed(index, timing, null);
	}

	public static TicksTiming GetDelayed(int index, TicksTiming timing, TicksTiming delay) {
		int d = delay == null ? 0 : delay.getDelay();
		return timing == null ? null : new TicksTiming(d + index * timing.getDelay());
	}

	public static void ArraySet(StringArray array, String[] data) {
		ArraySet(array, data, null, null, false);
	}

	public static void ArraySet(StringArray array, String[] data, TicksTiming timing) {
		ArraySet(array, data, timing, null, false);
	}

//	public static void ArraySet(StringArray array, String[] data, TicksTiming timing, TicksTiming delay) {
//		ArraySet(array, data, timing, delay, false);
//	}
	public static void ArraySet(StringArray array, String[] data, TicksTiming timing, boolean highlight) {
		ArraySet(array, data, timing, null, highlight);
	}

	public static void ArraySet(StringArray array, String[] data, TicksTiming timing, TicksTiming delay, boolean highlight) {
		Assert(array.getLength() == data.length);

		for (int i = 0; i < data.length; ++i) {
			array.put(i, data[i], GetDelayed(i, timing, delay), null);
			if (highlight) {
				array.highlightElem(i, GetDelayed(i, timing, delay), null);
			}
		}
	}

	public static void ArrayClear(StringArray array) {
		ArraySet(array, createFilledStringArray(array.getLength(), " "));
	}
	
}
