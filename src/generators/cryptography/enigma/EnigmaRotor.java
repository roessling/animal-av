package generators.cryptography.enigma;

public class EnigmaRotor extends EnigmaMap {

	private final int turnover;
	private int position;
	
	public EnigmaRotor(String mapping, String description, char turnoverLetter, char startPosition) {
		super(mapping, description);
		this.turnover = (turnoverLetter - 'A') + 1;
		this.position = (startPosition - 'A');
	}

	public boolean isTurnover(int position) {
		return (turnover == position);
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public void increasePosition() {
		this.position = (this.position + 1) % 26;
	}
}
