package generators.cryptography.util;

import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

public class KeyIsFunction implements KeyInterface {
	private String key;
	private int a;
	private int b;
	private int n;

	/**
	 * str ist Form : (ax+b) mod n oder ax+b mod n oder a*x+b
	 * 
	 * @param str
	 */
	public KeyIsFunction(String str) {
		this.key = str.trim();
	}

	@Override
	public void drawKey(Language lang, String baseIDRef) {
		// TODO Auto-generated method stub
		TextProperties props = new TextProperties();
		props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		lang.newText(new Offset(10, 0, baseIDRef, AnimalScript.DIRECTION_NE),
				key, "function", null, props);

	}

	@Override
	public int[] calculateWithDecryptionKey(String initial_vector) {
		// TODO Auto-generated method stub
		getElement();
		String[] arr = null;
		int x = Integer.parseInt(initial_vector, 2);
		int result = (a * x + b) % n;
		int lengOf_n = Integer.toBinaryString(n).length();

		result = (result > 0) ? result : (result + n);
		String binarized = Integer.toBinaryString(result);
		if (binarized.length() < lengOf_n) {
			StringBuffer buff = new StringBuffer();
			for (int i = 0; i < lengOf_n; i++) {
				buff.append("0");
			}
			binarized = buff.toString()
					.substring(0, lengOf_n - binarized.length())
					.concat(binarized);
		}
		int[] result_array = new int[lengOf_n];
		for (int j = 0; j < lengOf_n; j++) {
			result_array[j] = Integer.valueOf(String.valueOf(binarized
					.charAt(j)));
		}
		return result_array;
		//return new int[]{1,1,0,1,0,1};
	}

	public void getElement() {
		key=key.replaceAll(" ", "");
		String regex_1 = "^(\\+|-|)?(\\d*)(x)(mod)(\\d+)";
		String regex_2 = "^(\\+|-|)?(\\d*)(x)(\\+|-)(\\d+)(mod)(\\d+)";
		String regex_3 = "^(\\+|-|)?(\\d+)(\\*)(x)(mod)(\\d+)";
		String regex_4 = "^(\\+|-|)?(\\d+)(\\*)(x)(\\+|-)(\\d+)(mod)(\\d+)";

		String regex_5 = "^(\\()(\\+|-|)?(\\d*)(x)(\\))(mod)(\\d+)";
		String regex_6 = "^(\\()(\\+|-|)?(\\d*)(x)(\\+|-)(\\d+)(\\))(mod)(\\d+)";
		String regex_7 = "^(\\()(\\+|-|)?(\\d+)(\\*)(x)(\\))(mod)(\\d+)";
		String regex_8 = "^(\\()(\\+|-|)?(\\d+)(\\*)(x)(\\+|-)(\\d+)(\\))(mod)(\\d+)";

		Pattern p = Pattern.compile("(mod)(\\d+)");
		Matcher m = p.matcher(key);
		if (m.find()) {
			String N = m.group(2);
			n = Integer.valueOf(N);

		}

		p = Pattern.compile("(\\+|-|)?(\\d*)(\\*)?(x)");
		m = p.matcher(key);
		if (m.find()) {
			String temp_1 = m.group(1);
			String temp_2 = m.group(2);
			if (temp_2.equals("")) {
				if (temp_1.equals("") || temp_1.equals("+")) {
					a = 1;
				}
				if (temp_1.equals("-")) {
					a = -1;
				}
			} else {
				if (temp_1.equals("") || temp_1.equals("+")) {
					a = Integer.valueOf(temp_2);
				}
				if (temp_1.equals("-")) {
					a = (-1) * Integer.valueOf(temp_2);
				}
			}
		}
		p = Pattern.compile("(\\+|-)?(\\d*)(\\))?(mod)");
		m = p.matcher(key);
		if (m.find()) {
			String temp_1 = m.group(1);
			String temp_2 = m.group(2);
			if (temp_2.equals("")) {
				b = 0;

			} else {
				if (temp_1.equals("+")) {
					b = Integer.valueOf(temp_2);
				}
				if (temp_1.equals("-")) {
					b = (-1) * Integer.valueOf(temp_2);
				}
			}
		}
	}

}
