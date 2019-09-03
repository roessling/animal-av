package generators.cryptography.enigma;

import java.util.HashMap;
import java.util.Map;

public class EnigmaMap {

	private static final char[] LETTERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	private final Map<Character, Character> map;
	private final Map<Character, Character> reverseMap;
	private final String description;

	public EnigmaMap(String mapping, String description) throws IllegalArgumentException {
		if(mapping.length() != 26) {
			throw new IllegalArgumentException("A mapping must have exactly 26 letters.");
		}
		this.description = description;
		this.map = buildMap(mapping);
		this.reverseMap = buildReverseMap(mapping);
	}

	public char get(char input, boolean reverse) {
		if (reverse) {
			return reverseMap.get(input);
		} else {
			return map.get(input);
		}
	}

	@Override
	public String toString() {
		return this.description;
	}
	
	private Map<Character, Character> buildMap(String mapping) {
		Map<Character, Character> map = new HashMap<>();
		for (int i = 0; i < mapping.length(); i++)
			map.put(LETTERS[i], mapping.charAt(i));
		return map;
	}

	private Map<Character, Character> buildReverseMap(String mapping) {
		Map<Character, Character> map = new HashMap<>();
		for (int i = 0; i < mapping.length(); i++)
			map.put(mapping.charAt(i), LETTERS[i]);
		return map;
	}
}
