package generators.cryptography.enigma;

public class EnigmaPlugboard extends EnigmaMap {

	public static final EnigmaPlugboard DEFAULT_PLUGBOARD = new EnigmaPlugboard("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "Standard Steckverbindung");
	
	public EnigmaPlugboard(String mapping, String description) {
		super(mapping, description);
	}

}
