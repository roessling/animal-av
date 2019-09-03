package generators.cryptography.enigma;

public class EnigmaMachine {

	private static final char[] LETTERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	public static class EnigmaMachineConfiguration {
		private EnigmaRotor rotorA;
		private EnigmaRotor rotorB;
		private EnigmaRotor rotorC;
		private EnigmaReflector reflector;
		private EnigmaPlugboard plugboard;

		public EnigmaMachineConfiguration(EnigmaRotor rotorA, EnigmaRotor rotorB, EnigmaRotor rotorC,
				EnigmaReflector reflector, EnigmaPlugboard plugboard) {
			this.rotorA = rotorA;
			this.rotorB = rotorB;
			this.rotorC = rotorC;
			this.reflector = reflector;
			this.plugboard = plugboard;
		}
	}

	private EnigmaRotor rotorA;
	private EnigmaRotor rotorB;
	private EnigmaRotor rotorC;
	private EnigmaReflector reflector;
	private EnigmaPlugboard plugboard;

	public EnigmaMachine(EnigmaMachineConfiguration config) {
		this.rotorA = config.rotorA;
		this.rotorB = config.rotorB;
		this.rotorC = config.rotorC;
		this.plugboard = config.plugboard;
		this.reflector = config.reflector;
	}

	public char encrypt(final char letter) {
		moveRotors();

		int value = getOutputIndex(plugboard, 0, letter - 'A', false);
		value = getOutputIndex(rotorC, rotorC.getPosition(), value, false);
		value = getOutputIndex(rotorB, rotorB.getPosition(), value, false);
		value = getOutputIndex(rotorA, rotorA.getPosition(), value, false);
		value = getOutputIndex(reflector, 0, value, false);
		value = getOutputIndex(rotorA, rotorA.getPosition(), value, true);
		value = getOutputIndex(rotorB, rotorB.getPosition(), value, true);
		value = getOutputIndex(rotorC, rotorC.getPosition(), value, true);
		value = getOutputIndex(plugboard, 0, value, true);
		return LETTERS[value];
	}

	public int getOutputIndex(EnigmaMap map, int position, int inIndex, boolean reverse) {
		int value = (inIndex + position) % 26;
		char letter = LETTERS[value];
		letter = map.get(letter, reverse);
		value = (letter - 'A') - position;
		if (value < 0)
			value += 26;
		return value;
	}

	private void moveRotors() {
		rotorC.increasePosition();
		if (rotorC.isTurnover(rotorC.getPosition())) {
			rotorB.increasePosition();
			if (rotorB.isTurnover(rotorB.getPosition())) {
				rotorA.increasePosition();
			}
		}
	}
}
