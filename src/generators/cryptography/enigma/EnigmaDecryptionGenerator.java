/*
 * EnigmaDecryptionGenerator.java
 * Marius Gassen, Markus Danz, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.cryptography.enigma;

import generators.cryptography.enigma.EnigmaMachine.EnigmaMachineConfiguration;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.Language;
import algoanim.primitives.Rect;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;

import translator.Translator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class EnigmaDecryptionGenerator implements ValidatingGenerator {

	private static final char[] LETTERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	private static final String[] DEFAULT_MAPPING = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
			"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	private static final String[] EXAMPLE_MAPPING = { "E", "K", "M", "F", "L", "G", "D", "Q", "V", "Z", "N", "T", "O",
			"W", "Y", "H", "X", "U", "S", "P", "A", "I", "B", "R", "C", "J" };

	private static final String[] EXAMPLE_MAPPING_II = { "A", "J", "D", "K", "S", "I", "R", "U", "X", "B", "L", "H",
			"W", "T", "M", "C", "Q", "G", "Z", "N", "P", "Y", "F", "V", "O", "E" };

	private static final String DESCRIPTION_GERMAN = "Die Enigma ist eine Maschine zur Verschl&uuml;sselung und Entschl&uuml;sselung von Text. "
			+ "\n" + "Dazu besteht die Maschine aus verschiedenen Bauteilen:" + "\n" + " - Einem Steckerbrett" + "\n"
			+ " - Einem Walzensatz mit 3 austauschbaren Walzen" + "\n" + " - Einer austauschbaren Umkehrwalze" + "\n"
			+ "Das Steckerbrett erlaubt das feste Abbilden von jedem Buchstaben auf einen anderen. Es lassen sich 3 aus 5 Walzen verwenden, wobei jede Walze eine feste Zuordnung von einem Buchstaben auf einen anderen hat. Zudem besitzt eine Walze eine &Uuml;bertragskerbe, sodass nach 26 Umdrehungen die n&auml;chste Walze um eine Position rotiert."
			+ "\n"
			+ "Die Umkehrwalze l&auml;sst sich aus 3 Walzen ausw&auml;hlen und hat wie die anderen Walzen eine feste Zuordnung der Buchstaben, rotiert aber nicht. Sie dient dazu das Signal zur&uuml;ck zu leiten."
			+ "\n"
			+ "Die Sicherheit basiert auf der Geheimhaltung von Steckverbindungen, genutzten Walzen und deren Startposition."
			+ "\n" + "Ein Durchlauf f&uuml;r einen Buchstaben sieht wie folgt aus:" + "\n" + " - Steckerbrett" + "\n"
			+ " - Walze C" + "\n" + " - Walze B" + "\n" + " - Walze A" + "\n" + " - Umkehrwalze" + "\n" + " - Walze A"
			+ "\n" + " - Walze B" + "\n" + " - Walze C" + "\n" + " - Steckerbrett";

	private static final String DESCRIPTION_ENGLISH = "The Enigma is a machine for encryption and decryption of text."
			+ "\n" + "It is built out of the following components:" + "\n" + " - a plugboard" + "\n"
			+ " - a rotor set with 3 interchangeable rotors" + "\n" + " - an interchangeable reflector" + "\n\n"
			+ "The plugboard allows to map each character to another. Out of 5 rotors - each with a fixed mapping from one character to another -, 3 can be chosen."
			+ "\n"
			+ "Additionally, each rotor has a carry notch, resulting in a one-step rotation of the following rotor after every 26 turns."
			+ "\n"
			+ "The reflector can be chosen out of 3 rotors and also has a fixed mapping from one character to another. The reflector does not rotate and is responsible for returning the signal."
			+ "\n\n"
			+ "Enigma's security is based on keeping the plugboard and used rotors with their start position secret."
			+ "\n" + "The signal of each character passes through Enigma as follows:" + "\n" + " - plugboard" + "\n"
			+ " - rotor C" + "\n" + " - rotor B" + "\n" + " - rotor A" + "\n" + " - reflector" + "\n" + " - rotor A"
			+ "\n" + " - rotor B" + "\n" + " - rotor C" + "\n" + " - plugboard";

	/*
	 * private static final String SOURCE_CODE = "public class EnigmaMachine {"
	 * + "\n" +
	 * "  private static final char[] LETTERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',"
	 * + "\n" + "      'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };"
	 * + "\n" + "\n" + "  public static class EnigmaMachineConfiguration {" +
	 * "\n" + "    private EnigmaRotor rotorA;" + "\n" +
	 * "    private EnigmaRotor rotorB;" + "\n" +
	 * "    private EnigmaRotor rotorC;" + "\n" +
	 * "    private EnigmaReflector reflector;" + "\n" +
	 * "    private EnigmaPlugboard plugboard;" + "\n" +
	 * "    private char positionA;" + "\n" + "    private char positionB;" +
	 * "\n" + "    private char positionC;" + "\n" + "\n" +
	 * "    public EnigmaMachineConfiguration(EnigmaRotor rotorA, EnigmaRotor rotorB,"
	 * + "\n" +
	 * "       EnigmaRotor rotorC, EnigmaReflector reflector, EnigmaPlugboard plugboard,"
	 * + "\n" + "       char positionA, char positionB, char positionC) {" +
	 * "\n" + "      this.positionA = positionA;" + "\n" +
	 * "      this.positionB = positionB;" + "\n" +
	 * "      this.positionC = positionC;" + "\n" +
	 * "      this.rotorA = rotorA;" + "\n" + "      this.rotorB = rotorB;" +
	 * "\n" + "      this.rotorC = rotorC;" + "\n" +
	 * "      this.reflector = reflector;" + "\n" +
	 * "      this.plugboard = plugboard;" + "\n" + "    }" + "\n" + "  }" +
	 * "\n" + "\n" + "  private EnigmaRotor rotorA;" + "\n" +
	 * "  private EnigmaRotor rotorB;" + "\n" + "  private EnigmaRotor rotorC;"
	 * + "\n" + "  private EnigmaReflector reflector;" + "\n" +
	 * "  private EnigmaPlugboard plugboard;" + "\n" +
	 * "  private int positionA;" + "\n" + "  private int positionB;" + "\n" +
	 * "  private int positionC;" + "\n" + "  private int startPositionA;" +
	 * "\n" + "  private int startPositionB;" + "\n" +
	 * "  private int startPositionC;" + "\n" + "\n" +
	 * "  public EnigmaMachine(EnigmaMachineConfiguration config) {" + "\n" +
	 * "    this.rotorA = config.rotorA;" + "\n" +
	 * "    this.rotorB = config.rotorB;" + "\n" +
	 * "    this.rotorC = config.rotorC;" + "\n" +
	 * "    this.plugboard = config.plugboard;" + "\n" +
	 * "    this.reflector = config.reflector;" + "\n" +
	 * "    this.positionA = config.positionA - 'A';" + "\n" +
	 * "    this.positionB = config.positionB - 'A';" + "\n" +
	 * "    this.positionC = config.positionC - 'A';" + "\n" + "    " + "\n" +
	 * "    this.startPositionA = this.positionA;" + "\n" +
	 * "    this.startPositionB = this.positionB;" + "\n" +
	 * "    this.startPositionC = this.positionC;" + "\n" + "  }" + "\n" + "\n"
	 * + "  public char encrypt(final char letter) {" + "\n" +
	 * "    moveRotors();" + "\n" +
	 * "    int value = getOutputIndex(plugboard, 0, letter - 'A', false);" +
	 * "\n" + "    value = getOutputIndex(rotorC, positionC, value, false);" +
	 * "\n" + "    value = getOutputIndex(rotorB, positionB, value, false);" +
	 * "\n" + "    value = getOutputIndex(rotorA, positionA, value, false);" +
	 * "\n" + "    value = getOutputIndex(reflector, 0, value, false);" + "\n" +
	 * "    value = getOutputIndex(rotorA, positionA, value, true);" + "\n" +
	 * "    value = getOutputIndex(rotorB, positionB, value, true);" + "\n" +
	 * "    value = getOutputIndex(rotorC, positionC, value, true);" + "\n" +
	 * "    value = getOutputIndex(plugboard, 0, value, true);" + "\n" +
	 * "    return LETTERS[value];" + "\n" + "  }" + "\n" + "  " + "\n" +
	 * "  public void reset() {" + "\n" + "    this.positionA = startPositionA;"
	 * + "\n" + "    this.positionB = startPositionB;" + "\n" +
	 * "    this.positionC = startPositionC;" + "\n" + "  }" + "\n" + "\n" +
	 * "  private int getOutputIndex(EnigmaMap map, int mapOffset, int inIndex, boolean reverse) {"
	 * + "\n" + "    int value = (inIndex + mapOffset) % 26;" + "\n" +
	 * "    char letter = LETTERS[value];" + "\n" +
	 * "    letter = map.get(letter, reverse);" + "\n" +
	 * "    value = (letter - 'A') - mapOffset;" + "\n" + "    if (value < 0)" +
	 * "\n" + "      value += 26;" + "\n" + "    return value;" + "\n" + "  }" +
	 * "\n" + "\n" + "  private void moveRotors() {" + "\n" +
	 * "    positionC = (positionC + 1) % 26;" + "\n" +
	 * "    if (rotorC.isTurnover(positionC)) {" + "\n" +
	 * "      positionB = (positionB + 1) % 26;" + "\n" +
	 * "      if (rotorB.isTurnover(positionB)) {" + "\n" +
	 * "        positionA = (positionA + 1) % 26;" + "\n" + "      }" + "\n" +
	 * "    }" + "\n" + "  }" + "\n" + "}" + "\n" + "\n" +
	 * "public class EnigmaMap {" + "\n" +
	 * "  private static final char[] LETTERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',"
	 * + "\n" + "      'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };"
	 * + "\n" + "  private final Map map;" + "\n" +
	 * "  private final Map reverseMap;" + "\n" +
	 * "  private final String description;" + "\n" + "\n" +
	 * "  public EnigmaMap(String mapping, String description) {" + "\n" +
	 * "    if(mapping.length() != 26) {" + "\n" +
	 * "      throw new IllegalArgumentException();" + "\n" + "    }" + "\n" +
	 * "    this.description = description;" + "\n" +
	 * "    this.map = buildMap(mapping);" + "\n" +
	 * "    this.reverseMap = buildReverseMap(mapping);" + "\n" + "  }" + "\n" +
	 * "\n" + "  public char get(char input, boolean reverse) {" + "\n" +
	 * "    if (reverse) {" + "\n" + "      return reverseMap.get(input);" +
	 * "\n" + "    } else {" + "\n" + "      return map.get(input);" + "\n" +
	 * "    }" + "\n" + "  }" + "\n" + "\n" +
	 * "  @Override  public String toString() {" + "\n" +
	 * "    return this.description;" + "\n" + "  }" + "\n" + "\n" + "  " + "\n"
	 * + "  private Map buildMap(String mapping) {" + "\n" +
	 * "    Map map = new HashMap();" + "\n" +
	 * "    for (int i = 0; i  buildReverseMap(String mapping) {" + "\n" +
	 * "    Map map = new HashMap();" + "\n" +
	 * "    for (int i = 0; i  mapping.length(); i++)" + "\n" +
	 * "      map.put(mapping.charAt(i), LETTERS[i]);" + "\n" +
	 * "    return map;" + "\n" + "  }" + "\n" + "}" + "\n" + "\n" +
	 * "public class EnigmaRotor extends EnigmaMap {" + "\n" +
	 * "  public static final EnigmaRotor ROTOR_I = new EnigmaRotor(\"EKMFLGDQVZNTOWYHXUSPAIBRCJ\", \"Walze I\", 'Q');"
	 * + "\n" +
	 * "  public static final EnigmaRotor ROTOR_II = new EnigmaRotor(\"AJDKSIRUXBLHWTMCQGZNPYFVOE\", \"Walze II\", 'E');"
	 * + "\n" +
	 * "  public static final EnigmaRotor ROTOR_III = new EnigmaRotor(\"BDFHJLCPRTXVZNYEIWGAKMUSQO\", \"Walze III\", 'V');"
	 * + "\n" +
	 * "  public static final EnigmaRotor ROTOR_IV = new EnigmaRotor(\"ESOVPZJAYQUIRHXLNFTGKDCMWB\", \"Walze IV\", 'J');"
	 * + "\n" +
	 * "  public static final EnigmaRotor ROTOR_V = new EnigmaRotor(\"VZBRGITYUPSDNHLXAWMJQOFECK\", \"Walze V\", 'Z');"
	 * + "\n" + "  private final int turnover;" + "\n" + "\n" +
	 * "  private EnigmaRotor(String mapping, String description, char turnoverLetter) {"
	 * + "\n" + "    super(mapping, description);" + "\n" +
	 * "    this.turnover = (turnoverLetter - 'A') + 1;" + "\n" + "  }" + "\n" +
	 * "\n" + "  public boolean isTurnover(int position) {" + "\n" +
	 * "    return (turnover == position);" + "\n" + "  }" + "\n" + "}" + "\n" +
	 * "\n" + "public class EnigmaReflector extends EnigmaMap {" + "\n" +
	 * "  public static final EnigmaReflector REFLECTOR_A = new EnigmaReflector(\"EJMZALYXVBWFCRQUONTSPIKHGD\", \"Umkehrwalze A\");"
	 * + "\n" +
	 * "  public static final EnigmaReflector REFLECTOR_B = new EnigmaReflector(\"YRUHQSLDPXNGOKMIEBFZCWVJAT\", \"Umkehrwalze B\");"
	 * + "\n" +
	 * "  public static final EnigmaReflector REFLECTOR_C = new EnigmaReflector(\"FVPJIAOYEDRZXWGCTKUQSBNMHL\", \"Umkehrwalze C\");"
	 * + "\n" +
	 * "  private EnigmaReflector(String mapping, String description) {" + "\n"
	 * + "    super(mapping, description);" + "\n" + "  }" + "\n" + "}" + "\n" +
	 * "\n" + "public class EnigmaPlugboard extends EnigmaMap {" + "\n" +
	 * "  public static final EnigmaPlugboard DEFAULT_PLUGBOARD = new EnigmaPlugboard(\"ABCDEFGHIJKLMNOPQRSTUVWXYZ\", \"Standard Steckverbindung\");"
	 * + "\n" + "\n" +
	 * "    public EnigmaPlugboard(String mapping, String description) {" + "\n"
	 * + "    super(mapping, description);" + "\n" + "  }" + "\n" + "}" + "\n";
	 */

	private static final String SOURCE_CODE_GERMAN = "Component.getMappingPosition(letter) gibt für die gegebene Komponente\n"
			+ "den Index der Abbildung im Alphabet für den Eingabebuchstaben zurück.\n"
			+ "Component.getReverseMappingPosition(letter) gibt für die gegebene Komponente\n"
			+ "den Index der Rückabbildung im Alphabet für den Eingabebuchstaben zurück.\n"
			+ "position<A,B,C> beschreibt die Anzahl der Schritte, die die entsprechende Walze bereits gedreht wurde. \n\n"
			+ "result := empty;\n" + "foreach(letter in message)\n" + "    moveRotorC();\n"
			+ "    if(notchC == startPosition)\n" + "        moveRotorB();\n" + "        if(notchB == startPosition)\n"
			+ "            moveRotorA();\n" + "    intermediate := plugboard.getMappingPosition(letter);\n"
			+ "    intermediate = rotorC.getMappingPosition(intermediate) - positionC;\n"
			+ "    intermediate = rotorB.getMappingPosition(intermediate) - positionB;\n"
			+ "    intermediate = rotorA.getMappingPosition(intermediate) - positionA;\n"
			+ "    intermediate = reflector.getMappingPosition(intermediate);\n"
			+ "    intermediate = rotorA.getReverseMappingPosition(intermediate) - positionA;\n"
			+ "    intermediate = rotorB.getReverseMappingPosition(intermediate) - positionB;\n"
			+ "    intermediate = rotorC.getReverseMappingPosition(intermediate) - positionC;\n"
			+ "    intermediate = plugboard.getReverseMappingPosition(intermediate);\n" + "    result += intermediate;\n"
			+ "return result;\n";

	private static final String SOURCE_CODE_ENGLISH = "Component.getMappingPosition(letter) returns the index in the alphabet \n"
			+ "of the input's mapping of the corresponding component.\n"
			+ "Component.getReverseMappingPosition(letter) returns the index in the alphabet \n"
			+ "of the input's reversed mapping of the corresponding component. \n"
			+ "position<A,B,C> describes the amount of steps the respective rotor has made. \n\n"
			+ "result := empty;\n" + "foreach(letter in message)\n" + "    moveRotorC(); \n"
			+ "    if(notchC == startPosition)\n" + "        moveRotorB();\n" + "        if(notchB == startPosition)\n"
			+ "            moveRotorA(); \n" + "    intermediate := plugboard.getMappingPosition(letter);\n"
			+ "    intermediate = rotorC.getMappingPosition(intermediate) - positionC;\n"
			+ "    intermediate = rotorB.getMappingPosition(intermediate) - positionB;\n"
			+ "    intermediate = rotorA.getMappingPosition(intermediate) - positionA;\n"
			+ "    intermediate = reflector.getMappingPosition(intermediate);\n"
			+ "    intermediate = rotorA.getReverseMappingPosition(intermediate) - positionA;\n"
			+ "    intermediate = rotorB.getReverseMappingPosition(intermediate) - positionB;\n"
			+ "    intermediate = rotorC.getReverseMappingPosition(intermediate) - positionC;\n"
			+ "    intermediate = plugboard.getReverseMappingPosition(intermediate);\n" + "    result += intermediate;\n"
			+ "return result;\n";

	private static final int COLUMN_ONE_X = 20;
	private static final int COLUMN_TWO_X = 120;
	private static final int COLUMN_THREE_X = 650;
	private static final int COLUMN_FOUR_X = 680;

	private static final int OFFSET_HEIGHT = 5;
	private static final int ARRAY_HEIGHT = 25;
	private static final int LABEL_OFFSET = 3;
	private static final int STACKED_ARRAY_HEIGHT = 2 * ARRAY_HEIGHT;
	private static final int STACKED_ARRAY_LABEL_OFFSET = 15;
	private static final int MESSAGE_Y = 60;
	private static final int CYPHER_Y = MESSAGE_Y + ARRAY_HEIGHT + OFFSET_HEIGHT;
	private static final int PLUGBOARD_Y = CYPHER_Y + ARRAY_HEIGHT + OFFSET_HEIGHT;
	private static final int ROTOR_C_Y = PLUGBOARD_Y + STACKED_ARRAY_HEIGHT + OFFSET_HEIGHT;
	private static final int ROTOR_B_Y = ROTOR_C_Y + STACKED_ARRAY_HEIGHT + OFFSET_HEIGHT;
	private static final int ROTOR_A_Y = ROTOR_B_Y + STACKED_ARRAY_HEIGHT + OFFSET_HEIGHT;
	private static final int REFLECTOR_Y = ROTOR_A_Y + STACKED_ARRAY_HEIGHT + OFFSET_HEIGHT;
	private static final int MATRIX_LABEL_Y = REFLECTOR_Y + STACKED_ARRAY_HEIGHT + 2 * OFFSET_HEIGHT;
	private static final int MATRIX_Y = MATRIX_LABEL_Y + ARRAY_HEIGHT;

	private Language lang;
	private Locale loc;
	private Translator translator;

	public EnigmaDecryptionGenerator(Locale loc) {
		this.loc = loc;
		this.translator = new Translator("EnigmaDecryptionGenerator", loc);
	}

	public void init() {
		lang = new AnimalScript("Enigma Decryption", "Marius Gassen, Markus Danz", 1024, 768);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		ArrayProperties cypherProps = (ArrayProperties) props.getPropertiesByName("Cypher Array");
		ArrayProperties plainProps = (ArrayProperties) props.getPropertiesByName("Plaintext Array");
		ArrayProperties rotorProps = (ArrayProperties) props.getPropertiesByName("Rotor Arrays");
		ArrayProperties rotorPropsReversed = (ArrayProperties) props.getPropertiesByName("Rotor Arrays Reverse");
		Color cypherHighlight = (Color) cypherProps.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
		Color plainHighlight = (Color) plainProps.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
		Color notchHighlight = (Color) rotorProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY);
		Color stepHighlight = (Color) rotorProps.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
		Color stepHighlightReverse = (Color) rotorPropsReversed.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);

		String cypher = ((String) primitives.get("Chiffre")).toUpperCase();
		String[] rotorA = (String[]) primitives.get("Walze A");
		String[] rotorB = (String[]) primitives.get("Walze B");
		String[] rotorC = (String[]) primitives.get("Walze C");
		String[] reflector = (String[]) primitives.get("Umkehrwalze");
		String[] plugboard = (String[]) primitives.get("Steckerbrett");
		String startPositionA = (String) primitives.get("Startposition Walze A");
		String startPositionB = (String) primitives.get("Startposition Walze B");
		String startPositionC = (String) primitives.get("Startposition Walze C");
		String notchA = (String) primitives.get("Uebertragskerbe Walze A");
		String notchB = (String) primitives.get("Uebertragskerbe Walze B");
		String notchC = (String) primitives.get("Uebertragskerbe Walze C");

		String rotorAMapping = "";
		for (String letter : rotorA) {
			rotorAMapping += letter;
		}

		String rotorBMapping = "";
		for (String letter : rotorB) {
			rotorBMapping += letter;
		}

		String rotorCMapping = "";
		for (String letter : rotorC) {
			rotorCMapping += letter;
		}

		String reflectorMapping = "";
		for (String letter : reflector) {
			reflectorMapping += letter;
		}

		String plugboardMapping = "";
		for (String letter : plugboard) {
			plugboardMapping += letter;
		}

		int notchAPosition = notchA.charAt(0) - 'A';
		int notchBPosition = notchB.charAt(0) - 'A';
		int notchCPosition = notchC.charAt(0) - 'A';

		EnigmaRotor rotorAConfig = new EnigmaRotor(rotorAMapping, translator.translateMessage("rotorA"),
				notchA.charAt(0), startPositionA.charAt(0));
		EnigmaRotor rotorBConfig = new EnigmaRotor(rotorBMapping, translator.translateMessage("rotorB"),
				notchB.charAt(0), startPositionB.charAt(0));
		EnigmaRotor rotorCConfig = new EnigmaRotor(rotorCMapping, translator.translateMessage("rotorC"),
				notchC.charAt(0), startPositionC.charAt(0));
		EnigmaReflector reflectorConfig = new EnigmaReflector(reflectorMapping,
				translator.translateMessage("reflector"));
		EnigmaPlugboard plugboardConfig = new EnigmaPlugboard(plugboardMapping,
				translator.translateMessage("plugboard"));

		EnigmaMachineConfiguration config = new EnigmaMachineConfiguration(rotorAConfig, rotorBConfig, rotorCConfig,
				reflectorConfig, plugboardConfig);

		EnigmaMachine machine = new EnigmaMachine(config);

		// TITLE
		Font titleFont = new Font("SansSerif", Font.BOLD, 20);
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, titleFont);
		Text title = lang.newText(new Coordinates(COLUMN_ONE_X, 35), translator.translateMessage("title"), "title",
				null, tp);

		// FIRST SLIDE
		lang.newText(new Coordinates(20, 60), translator.translateMessage("descriptionTitle"), "descriptionTitle",
				null);
		lang.newText(new Coordinates(20, 100), translator.translateMessage("descriptionText1"), "desctiptionText1",
				null);
		lang.newText(new Coordinates(20, 120), translator.translateMessage("descriptionText2"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 140), translator.translateMessage("descriptionText3"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 160), translator.translateMessage("descriptionText4"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 180), translator.translateMessage("descriptionText5"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 220), translator.translateMessage("descriptionText6"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 240), translator.translateMessage("descriptionText7"), "decsriptionText",
				null);
		lang.newText(new Coordinates(20, 260), translator.translateMessage("descriptionText8"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 280), translator.translateMessage("descriptionText9"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 320), translator.translateMessage("descriptionText10"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 340), translator.translateMessage("descriptionText11"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 360), translator.translateMessage("descriptionText12"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 380), translator.translateMessage("descriptionText13"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 400), translator.translateMessage("descriptionText14"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 420), translator.translateMessage("descriptionText15"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 440), translator.translateMessage("descriptionText16"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 460), translator.translateMessage("descriptionText17"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 480), translator.translateMessage("descriptionText18"), "descriptionText",
				null);
		lang.newText(new Coordinates(20, 500), translator.translateMessage("descriptionText19"), "descriptionText",
				null);

		lang.nextStep(translator.translateMessage("contents1"));
		lang.hideAllPrimitives();

		// EXAMPLE ANIMATION
		title.show();

		Text explanation = lang.newText(new Coordinates(COLUMN_ONE_X, 60), translator.translateMessage("example1"),
				"explanation", null);
		// EXAMPLE ROTOR I
		Text labelRotorXIn = lang.newText(new Coordinates(COLUMN_ONE_X, 102), "Rotor X In", "rotorXIn", null);
		Text labelRotorXMap = lang.newText(new Coordinates(COLUMN_ONE_X, 127), "Rotor X Mapping", "rotorXMap", null);
		Text labelRotorXOut = lang.newText(new Coordinates(COLUMN_ONE_X, 152), "Rotor X Out", "rotorXOut", null);

		MatrixProperties emp = new MatrixProperties();
		emp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
		emp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		String[][] exampleMatrix = new String[3][26];
		for (int y = 0; y < 26; y++) {
			exampleMatrix[0][y] = DEFAULT_MAPPING[y];
			exampleMatrix[1][y] = EXAMPLE_MAPPING[y];
			exampleMatrix[2][y] = DEFAULT_MAPPING[y];
		}
		StringMatrix rotorX = lang.newStringMatrix(new Coordinates(COLUMN_TWO_X, 100), exampleMatrix, "exampleMatrix",
				null, emp);

		rotorX.setGridColor(0, 1, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(1, 1, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(2, 1, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// EXAMPLE ROTOR II
		Text labelRotorYIn = lang.newText(new Coordinates(COLUMN_ONE_X, 202), "Rotor Y In", "rotorYIn", null);
		Text labelRotorYMap = lang.newText(new Coordinates(COLUMN_ONE_X, 227), "Rotor Y Mapping", "rotorYMap", null);
		Text labelRotorYOut = lang.newText(new Coordinates(COLUMN_ONE_X, 252), "Rotor Y Out", "rotorYOut", null);

		String[][] exampleMatrixTwo = new String[3][26];
		for (int y = 0; y < 26; y++) {
			exampleMatrixTwo[0][y] = DEFAULT_MAPPING[y];
			exampleMatrixTwo[1][y] = EXAMPLE_MAPPING_II[y];
			exampleMatrixTwo[2][y] = DEFAULT_MAPPING[y];
		}
		StringMatrix rotorY = lang.newStringMatrix(new Coordinates(COLUMN_TWO_X, 200), exampleMatrixTwo,
				"exampleMatrixTwo", null, emp);

		lang.nextStep(translator.translateMessage("contents2"));

		explanation.setText(translator.translateMessage("example2"), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lang.nextStep();
		explanation.setText(translator.translateMessage("example3"), Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		for (int y = 0; y < 26; y++) {
			rotorX.put(0, y, DEFAULT_MAPPING[(y + 1) % 26], Timing.MEDIUM, Timing.INSTANTEOUS);
			rotorX.put(1, y, EXAMPLE_MAPPING[(y + 1) % 26], Timing.MEDIUM, Timing.INSTANTEOUS);
			rotorX.put(2, y, DEFAULT_MAPPING[(y + 1) % 26], Timing.MEDIUM, Timing.INSTANTEOUS);
		}
		rotorX.setGridColor(0, 1, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(1, 1, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(2, 1, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(0, 0, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(1, 0, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(2, 0, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		lang.nextStep();

		explanation.setText(translator.translateMessage("example4"), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.highlightCell(0, 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		lang.nextStep();

		explanation.setText(translator.translateMessage("example5"), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.highlightCell(1, 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		lang.nextStep();

		explanation.setText(translator.translateMessage("example6"), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.highlightCell(2, 11, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		lang.nextStep();

		explanation.setText(translator.translateMessage("example7"), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorY.highlightCell(0, 11, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorY.highlightCell(1, 11, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorY.highlightCell(2, 7, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		lang.nextStep();

		explanation.setText(translator.translateMessage("example8"), Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		lang.nextStep();

		explanation.setText(translator.translateMessage("example9"), Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		rotorX.unhighlightCell(0, 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.unhighlightCell(1, 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.unhighlightCell(2, 11, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorY.unhighlightCell(0, 11, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorY.unhighlightCell(1, 11, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorY.unhighlightCell(2, 7, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		for (int y = 0; y < 26; y++) {
			rotorX.put(0, y, DEFAULT_MAPPING[(y + 2) % 26], Timing.MEDIUM, Timing.INSTANTEOUS);
			rotorX.put(1, y, EXAMPLE_MAPPING[(y + 2) % 26], Timing.MEDIUM, Timing.INSTANTEOUS);
			rotorX.put(2, y, DEFAULT_MAPPING[(y + 2) % 26], Timing.MEDIUM, Timing.INSTANTEOUS);
			rotorY.put(0, y, DEFAULT_MAPPING[(y + 1) % 26], Timing.MEDIUM, Timing.INSTANTEOUS);
			rotorY.put(1, y, EXAMPLE_MAPPING[(y + 1) % 26], Timing.MEDIUM, Timing.INSTANTEOUS);
			rotorY.put(2, y, DEFAULT_MAPPING[(y + 1) % 26], Timing.MEDIUM, Timing.INSTANTEOUS);
		}

		rotorX.setGridColor(0, 0, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(1, 0, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(2, 0, Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(0, 25, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(1, 25, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorX.setGridColor(2, 25, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		lang.nextStep();

		explanation.setText(translator.translateMessage("example10"), Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		lang.nextStep();

		lang.hideAllPrimitives();
		rotorX.hide();
		rotorY.hide();
		title.show();

		// ARRAY PROPERTIES
		ArrayProperties ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, notchHighlight);

		// PLAINTEXT
		Text cypherTextLabel = lang.newText(new Coordinates(COLUMN_ONE_X, MESSAGE_Y + LABEL_OFFSET),
				translator.translateMessage("inputText"), "cypherLabel", null);
		StringArray cypherArray = lang.newStringArray(new Coordinates(COLUMN_TWO_X, MESSAGE_Y), cypher.split(""),
				"cypherText", null, ap);
		cypherArray.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// CYPHERTEXT
		Text plainTextLabel = lang.newText(new Coordinates(COLUMN_ONE_X, CYPHER_Y + LABEL_OFFSET),
				translator.translateMessage("outputText"), "plainTextLabel", null);
		String[] plaintext = new String[cypher.length()];
		Arrays.fill(plaintext, " ");
		StringArray plaintextArray = lang.newStringArray(new Coordinates(COLUMN_TWO_X, CYPHER_Y), plaintext,
				"plainTextArray", null, ap);
		plaintextArray.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// PLUGBOARD
		StringArray plugboardArrayIn = lang.newStringArray(new Coordinates(COLUMN_TWO_X, PLUGBOARD_Y), DEFAULT_MAPPING,
				"plugboard", null, ap);
		StringArray plugboardArrayOut = lang.newStringArray(new Coordinates(COLUMN_TWO_X, PLUGBOARD_Y + ARRAY_HEIGHT),
				DEFAULT_MAPPING, "plugboard", null, ap);
		Text plugboardLabel = lang.newText(new Coordinates(COLUMN_ONE_X, PLUGBOARD_Y + STACKED_ARRAY_LABEL_OFFSET),
				plugboardConfig.toString(), translator.translateMessage("plugboard"), null);
		plugboardArrayIn.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		plugboardArrayOut.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// Rotor C
		StringArray rotorCArrayIn = lang.newStringArray(new Coordinates(COLUMN_TWO_X, ROTOR_C_Y), DEFAULT_MAPPING,
				"rotorC", null, ap);
		StringArray rotorCArrayOut = lang.newStringArray(new Coordinates(COLUMN_TWO_X, ROTOR_C_Y + ARRAY_HEIGHT),
				DEFAULT_MAPPING, "rotorC", null, ap);
		Text rotorCLabel = lang.newText(new Coordinates(COLUMN_ONE_X, ROTOR_C_Y + STACKED_ARRAY_LABEL_OFFSET),
				rotorCConfig.toString(), translator.translateMessage("rotorC"), null);
		rotorCArrayIn.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorCArrayOut.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorCArrayIn.highlightElem(notchCPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorCArrayOut.highlightElem(notchCPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// Rotor B
		StringArray rotorBArrayIn = lang.newStringArray(new Coordinates(COLUMN_TWO_X, ROTOR_B_Y), DEFAULT_MAPPING,
				"rotorB", null, ap);
		StringArray rotorBArrayOut = lang.newStringArray(new Coordinates(COLUMN_TWO_X, ROTOR_B_Y + ARRAY_HEIGHT),
				DEFAULT_MAPPING, "rotorB", null, ap);
		Text rotorBLabel = lang.newText(new Coordinates(COLUMN_ONE_X, ROTOR_B_Y + STACKED_ARRAY_LABEL_OFFSET),
				rotorBConfig.toString(), translator.translateMessage("rotorB"), null);
		rotorBArrayIn.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorBArrayOut.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorBArrayIn.highlightElem(notchBPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorBArrayOut.highlightElem(notchBPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// Rotor A
		StringArray rotorAArrayIn = lang.newStringArray(new Coordinates(COLUMN_TWO_X, ROTOR_A_Y), DEFAULT_MAPPING,
				"rotorA", null, ap);
		StringArray rotorAArrayOut = lang.newStringArray(new Coordinates(COLUMN_TWO_X, ROTOR_A_Y + ARRAY_HEIGHT),
				DEFAULT_MAPPING, "rotorA", null, ap);
		Text rotorALabel = lang.newText(new Coordinates(COLUMN_ONE_X, ROTOR_A_Y + STACKED_ARRAY_LABEL_OFFSET),
				rotorAConfig.toString(), translator.translateMessage("rotorA"), null);
		rotorAArrayIn.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorAArrayOut.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorAArrayIn.highlightElem(notchAPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		rotorAArrayOut.highlightElem(notchAPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// Reflector
		StringArray reflectorArrayIn = lang.newStringArray(new Coordinates(COLUMN_TWO_X, REFLECTOR_Y), DEFAULT_MAPPING,
				"reflector", null, ap);
		StringArray reflectorArrayOut = lang.newStringArray(new Coordinates(COLUMN_TWO_X, REFLECTOR_Y + ARRAY_HEIGHT),
				DEFAULT_MAPPING, "reflector", null, ap);
		Text reflectorLabel = lang.newText(new Coordinates(COLUMN_ONE_X, REFLECTOR_Y + STACKED_ARRAY_LABEL_OFFSET),
				reflectorConfig.toString(), translator.translateMessage("reflector"), null);
		reflectorArrayIn.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		reflectorArrayOut.showIndices(false, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// CARRY LABELS
		Text carryCBLabel = lang.newText(new Coordinates(COLUMN_ONE_X, ROTOR_C_Y + STACKED_ARRAY_HEIGHT - LABEL_OFFSET),
				translator.translateMessage("carryCB"), "carryCB", null);
		Text carryBALabel = lang.newText(new Coordinates(COLUMN_ONE_X, ROTOR_B_Y + STACKED_ARRAY_HEIGHT - LABEL_OFFSET),
				translator.translateMessage("carryBA"), "carryBA", null);
		carryCBLabel.hide();
		carryBALabel.hide();

		// LEGEND
		// message
		RectProperties rmp = new RectProperties();
		rmp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rmp.set(AnimationPropertiesKeys.FILL_PROPERTY, plainHighlight);
		Rect legendRectMessage = lang.newRect(new Coordinates(COLUMN_THREE_X, PLUGBOARD_Y),
				new Coordinates(COLUMN_THREE_X + 20, PLUGBOARD_Y + 20), "legendRectMessage", null, rmp);
		Text legendTextMessage = lang.newText(new Coordinates(COLUMN_FOUR_X, PLUGBOARD_Y + LABEL_OFFSET),
				translator.translateMessage("legendTextMessage"), "legendTextMessage", null);

		// cypher
		RectProperties rcp = new RectProperties();
		rcp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rcp.set(AnimationPropertiesKeys.FILL_PROPERTY, cypherHighlight);
		Rect legendRectCypherReverse = lang.newRect(new Coordinates(COLUMN_THREE_X, ROTOR_C_Y),
				new Coordinates(COLUMN_THREE_X + 20, ROTOR_C_Y + 20), "legendRectCypher", null, rcp);
		Text legendTextCypher = lang.newText(new Coordinates(COLUMN_FOUR_X, ROTOR_C_Y + LABEL_OFFSET),
				translator.translateMessage("legendTextCypher"), "legendTextCypher", null);

		// notch
		RectProperties rnp = new RectProperties();
		rnp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rnp.set(AnimationPropertiesKeys.FILL_PROPERTY, notchHighlight);
		Rect legendRectNotch = lang.newRect(new Coordinates(COLUMN_THREE_X, ROTOR_B_Y),
				new Coordinates(COLUMN_THREE_X + 20, ROTOR_B_Y + 20), "legendRectNotch", null, rnp);
		Text legendTextNotch = lang.newText(new Coordinates(COLUMN_FOUR_X, ROTOR_B_Y + LABEL_OFFSET),
				translator.translateMessage("legendTextNotch"), "legendTextNotch", null);

		// step
		RectProperties rsp = new RectProperties();
		rsp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rsp.set(AnimationPropertiesKeys.FILL_PROPERTY, stepHighlight);
		Rect legendRectStep = lang.newRect(new Coordinates(COLUMN_THREE_X, ROTOR_A_Y),
				new Coordinates(COLUMN_THREE_X + 20, ROTOR_A_Y + 20), "legendRectStep", null, rsp);
		Text legendTextStep = lang.newText(new Coordinates(COLUMN_FOUR_X, ROTOR_A_Y + LABEL_OFFSET),
				translator.translateMessage("legendTextStep"), "legendTextStep", null);

		// reverse step
		RectProperties rsrp = new RectProperties();
		rsrp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rsrp.set(AnimationPropertiesKeys.FILL_PROPERTY, stepHighlightReverse);
		Rect legendRectStepReverse = lang.newRect(new Coordinates(COLUMN_THREE_X, REFLECTOR_Y),
				new Coordinates(COLUMN_THREE_X + 20, REFLECTOR_Y + 20), "legendRectStepReverse", null, rsrp);
		Text legendTextStepReverse = lang.newText(new Coordinates(COLUMN_FOUR_X, REFLECTOR_Y + LABEL_OFFSET),
				translator.translateMessage("legendTextStepReverse"), "legendTextStepReverse", null);

		cypherArray.setHighlightFillColor(0, cypher.length(), plainHighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		plaintextArray.setHighlightFillColor(0, cypher.length(), cypherHighlight, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);

		// Timings
		Timing delayPlugboard = new TicksTiming(30);
		Timing delayPlugboardEnc = new TicksTiming(60);
		Timing delayRotorC = new TicksTiming(90);
		Timing delayRotorCEnc = new TicksTiming(120);
		Timing delayRotorB = new TicksTiming(150);
		Timing delayRotorBEnc = new TicksTiming(180);
		Timing delayRotorA = new TicksTiming(210);
		Timing delayRotorAEnc = new TicksTiming(240);
		Timing delayReflector = new TicksTiming(270);
		Timing delayReflectorEnc = new TicksTiming(300);
		Timing delayRotorAEncRev = new TicksTiming(330);
		Timing delayRotorARev = new TicksTiming(360);
		Timing delayRotorBEncRev = new TicksTiming(390);
		Timing delayRotorBRev = new TicksTiming(420);
		Timing delayRotorCEncRev = new TicksTiming(450);
		Timing delayRotorCRev = new TicksTiming(480);
		Timing delayPlugboardEncRev = new TicksTiming(510);
		Timing delayPlugboardRev = new TicksTiming(540);
		Timing delayCypher = new TicksTiming(570);

		String[][] mappingMatrix = new String[6][27];
		mappingMatrix[0][0] = "";
		mappingMatrix[1][0] = translator.translateMessage("plugboard");
		mappingMatrix[2][0] = translator.translateMessage("rotorC");
		mappingMatrix[3][0] = translator.translateMessage("rotorB");
		mappingMatrix[4][0] = translator.translateMessage("rotorA");
		mappingMatrix[5][0] = translator.translateMessage("reflector");

		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 26; y++) {
				switch (x) {
				case 0:
					mappingMatrix[x][y + 1] = DEFAULT_MAPPING[y];
					break;
				case 1:
					mappingMatrix[x][y + 1] = plugboard[y];
					break;
				case 2:
					mappingMatrix[x][y + 1] = rotorC[y];
					break;
				case 3:
					mappingMatrix[x][y + 1] = rotorB[y];
					break;
				case 4:
					mappingMatrix[x][y + 1] = rotorA[y];
					break;
				case 5:
					mappingMatrix[x][y + 1] = reflector[y];
					break;
				default:
					break;
				}
			}
		}

		MatrixProperties mp = new MatrixProperties();
		mp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		StringMatrix matrix = lang.newStringMatrix(new Coordinates(COLUMN_TWO_X, MATRIX_Y), mappingMatrix,
				"mappingMatrix", null, mp);
		matrix.setGridBorderColor(0, 0, Color.WHITE, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		Font matrixLabelFont = new Font("SansSerif", Font.BOLD, 14);
		TextProperties mtp = new TextProperties();
		mtp.set(AnimationPropertiesKeys.FONT_PROPERTY, matrixLabelFont);
		Text matrixLabelTop = lang.newText(new Coordinates(COLUMN_TWO_X + 400, MATRIX_LABEL_Y + LABEL_OFFSET),
				translator.translateMessage("input"), "input", null, mtp);
		Text matrixLabelLeft = lang.newText(new Coordinates(COLUMN_ONE_X, MATRIX_Y + 2 * ARRAY_HEIGHT + LABEL_OFFSET),
				translator.translateMessage("output"), "output", null, mtp);

		String result = "";

		// Step through the plaintext characters
		for (int i = 0; i < cypher.length(); i++) {
			char c = cypher.charAt(i);

			if (i != 0) {
				cypherArray.unhighlightCell(i - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			}
			cypherArray.highlightCell(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			char currentPlaintext = machine.encrypt(c);
			result += currentPlaintext;

			int positionA = rotorAConfig.getPosition();
			int positionB = rotorBConfig.getPosition();
			int positionC = rotorCConfig.getPosition();

			for (int j = 0; j < 26; j++) {
				rotorCArrayIn.put(j, DEFAULT_MAPPING[(j + positionC) % 26], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				rotorCArrayOut.put(j, DEFAULT_MAPPING[(j + positionC) % 26], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				rotorBArrayIn.put(j, DEFAULT_MAPPING[(j + positionB) % 26], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				rotorBArrayOut.put(j, DEFAULT_MAPPING[(j + positionB) % 26], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				rotorAArrayIn.put(j, DEFAULT_MAPPING[(j + positionA) % 26], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				rotorAArrayOut.put(j, DEFAULT_MAPPING[(j + positionA) % 26], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			}

			rotorAArrayIn.unhighlightElem(notchAPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorAArrayOut.unhighlightElem(notchAPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorBArrayIn.unhighlightElem(notchBPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorBArrayOut.unhighlightElem(notchBPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorCArrayIn.unhighlightElem(notchCPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorCArrayOut.unhighlightElem(notchCPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			notchAPosition = notchA.charAt(0) - 'A' - positionA;
			if (notchAPosition < 0)
				notchAPosition += 26;
			notchBPosition = notchB.charAt(0) - 'A' - positionB;
			if (notchBPosition < 0)
				notchBPosition += 26;
			notchCPosition = notchC.charAt(0) - 'A' - positionC;
			if (notchCPosition < 0)
				notchCPosition += 26;

			rotorAArrayIn.highlightElem(notchAPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorAArrayOut.highlightElem(notchAPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorBArrayIn.highlightElem(notchBPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorBArrayOut.highlightElem(notchBPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorCArrayIn.highlightElem(notchCPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorCArrayOut.highlightElem(notchCPosition, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			// Show Carry if position equals notch
			if (notchCPosition == 0) {
				carryCBLabel.show();
			} else {
				carryCBLabel.hide();
			}

			// Show Carry if position equals notch
			if (notchBPosition == 0) {
				carryBALabel.show();
			} else {
				carryBALabel.hide();
			}

			// PLUGBOARD
			int indexPlugboardIn = c - 'A';
			int indexPlugboardOut = machine.getOutputIndex(plugboardConfig, 0, indexPlugboardIn, false);

			plugboardArrayIn.setHighlightFillColor(indexPlugboardIn, stepHighlight, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			plugboardArrayOut.setHighlightFillColor(indexPlugboardOut, stepHighlight, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);

			plugboardArrayIn.highlightCell(indexPlugboardIn, delayPlugboard, Timing.INSTANTEOUS);
			plugboardArrayOut.highlightCell(indexPlugboardOut, delayPlugboardEnc, Timing.INSTANTEOUS);

			// ROTOR C
			int indexCIn = indexPlugboardOut;
			int indexCOut = machine.getOutputIndex(rotorCConfig, positionC, indexPlugboardOut, false);

			rotorCArrayIn.setHighlightFillColor(indexCIn, stepHighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorCArrayOut.setHighlightFillColor(indexCOut, stepHighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			rotorCArrayIn.highlightCell(indexCIn, delayRotorC, Timing.INSTANTEOUS);
			rotorCArrayOut.highlightCell(indexCOut, delayRotorCEnc, Timing.INSTANTEOUS);

			// ROTOR B
			int indexBIn = indexCOut;
			int indexBOut = machine.getOutputIndex(rotorBConfig, positionB, indexCOut, false);

			rotorBArrayIn.setHighlightFillColor(indexBIn, stepHighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorBArrayOut.setHighlightFillColor(indexBOut, stepHighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			rotorBArrayIn.highlightCell(indexBIn, delayRotorB, Timing.INSTANTEOUS);
			rotorBArrayOut.highlightCell(indexBOut, delayRotorBEnc, Timing.INSTANTEOUS);

			// ROTOR A
			int indexAIn = indexBOut;
			int indexAOut = machine.getOutputIndex(rotorAConfig, positionA, indexBOut, false);

			rotorAArrayIn.setHighlightFillColor(indexAIn, stepHighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorAArrayOut.setHighlightFillColor(indexAOut, stepHighlight, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			rotorAArrayIn.highlightCell(indexAIn, delayRotorA, Timing.INSTANTEOUS);
			rotorAArrayOut.highlightCell(indexAOut, delayRotorAEnc, Timing.INSTANTEOUS);

			// REFLECTOR
			int indexReflectorIn = indexAOut - positionA;
			int indexReflectorOut = machine.getOutputIndex(reflectorConfig, 0, indexAOut, false);

			reflectorArrayIn.setHighlightFillColor(indexReflectorIn, stepHighlight, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			reflectorArrayOut.setHighlightFillColor(indexReflectorOut, stepHighlightReverse, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);

			reflectorArrayIn.highlightCell(indexReflectorIn, delayReflector, Timing.INSTANTEOUS);
			reflectorArrayOut.highlightCell(indexReflectorOut, delayReflectorEnc, Timing.INSTANTEOUS);

			// ROTOR A REVERSE
			int indexARef = machine.getOutputIndex(rotorAConfig, positionA, indexReflectorOut, true);
			rotorAArrayIn.setHighlightFillColor(indexARef, stepHighlightReverse, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			rotorAArrayOut.setHighlightFillColor(indexReflectorOut, stepHighlightReverse, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);

			rotorAArrayIn.highlightCell(indexARef, delayRotorARev, Timing.INSTANTEOUS);
			rotorAArrayOut.highlightCell(indexReflectorOut, delayRotorAEncRev, Timing.INSTANTEOUS);

			// ROTOR B REVERSE
			int indexBRef = machine.getOutputIndex(rotorBConfig, positionB, indexARef, true);

			rotorBArrayIn.setHighlightFillColor(indexBRef, stepHighlightReverse, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			rotorBArrayOut.setHighlightFillColor(indexARef, stepHighlightReverse, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);

			rotorBArrayIn.highlightCell(indexBRef, delayRotorBRev, Timing.INSTANTEOUS);
			rotorBArrayOut.highlightCell(indexARef, delayRotorBEncRev, Timing.INSTANTEOUS);

			// ROTOR C REVERSE
			int indexCRef = machine.getOutputIndex(rotorCConfig, positionC, indexBRef, true);

			rotorCArrayIn.setHighlightFillColor(indexCRef, stepHighlightReverse, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			rotorCArrayOut.setHighlightFillColor(indexBRef, stepHighlightReverse, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);

			rotorCArrayIn.highlightCell(indexCRef, delayRotorCRev, Timing.INSTANTEOUS);
			rotorCArrayOut.highlightCell(indexBRef, delayRotorCEncRev, Timing.INSTANTEOUS);

			// PLUGBOARD REVERSE
			int indexPlugboardRef = machine.getOutputIndex(plugboardConfig, 0, indexCRef, true);

			plugboardArrayIn.setHighlightFillColor(indexPlugboardRef, stepHighlightReverse, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
			plugboardArrayOut.setHighlightFillColor(indexCRef, stepHighlightReverse, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);

			plugboardArrayIn.highlightCell(indexPlugboardRef, delayPlugboardRev, Timing.INSTANTEOUS);
			plugboardArrayOut.highlightCell(indexCRef, delayPlugboardEncRev, Timing.INSTANTEOUS);

			// CYPHER
			if (i != 0) {
				plaintextArray.unhighlightCell(i - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			}
			plaintextArray.highlightCell(i, delayCypher, Timing.INSTANTEOUS);
			plaintextArray.put(i, Character.toString(currentPlaintext), delayCypher, Timing.INSTANTEOUS);

			lang.nextStep(translator.translateMessage("contents3"));

			// UNHIGHLIGHT LAST STEP
			plugboardArrayIn.unhighlightCell(indexPlugboardIn, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			plugboardArrayOut.unhighlightCell(indexPlugboardOut, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			rotorCArrayIn.unhighlightCell(indexCIn, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorCArrayOut.unhighlightCell(indexCOut, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			rotorBArrayIn.unhighlightCell(indexBIn, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorBArrayOut.unhighlightCell(indexBOut, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			rotorAArrayIn.unhighlightCell(indexAIn, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorAArrayOut.unhighlightCell(indexAOut, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			reflectorArrayIn.unhighlightCell(indexReflectorIn, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			reflectorArrayOut.unhighlightCell(indexReflectorOut, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			rotorAArrayIn.unhighlightCell(indexARef, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorAArrayOut.unhighlightCell(indexReflectorOut, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			rotorBArrayIn.unhighlightCell(indexBRef, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorBArrayOut.unhighlightCell(indexARef, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			rotorCArrayIn.unhighlightCell(indexCRef, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			rotorCArrayOut.unhighlightCell(indexBRef, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

			plugboardArrayIn.unhighlightCell(indexPlugboardRef, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			plugboardArrayOut.unhighlightCell(indexCRef, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}

		// EVENTUALLY UNHIGHLIGHT FINAL STEP
		cypherArray.unhighlightCell(cypher.length() - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		plaintextArray.unhighlightCell(cypher.length() - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		// FINAL SLIDE
		lang.nextStep();
		lang.hideAllPrimitives();
		cypherArray.hide();
		plaintextArray.hide();
		plugboardArrayIn.hide();
		plugboardArrayOut.hide();
		rotorAArrayIn.hide();
		rotorAArrayOut.hide();
		rotorBArrayIn.hide();
		rotorBArrayOut.hide();
		rotorCArrayIn.hide();
		rotorCArrayOut.hide();
		reflectorArrayIn.hide();
		reflectorArrayOut.hide();
		matrix.hide();

		title.show();

		lang.newText(new Coordinates(20, 60), translator.translateMessage("finalSlide1"), "finalSlide", null);
		lang.newText(new Coordinates(20, 80), translator.translateMessage("finalSlide2"), "finalSlide", null);
		lang.newText(new Coordinates(20, 120), translator.translateMessage("finalSlide3"), "finalSlide", null);
		lang.newText(new Coordinates(20, 140), translator.translateMessage("finalSlide4"), "finalSlide", null);
		lang.newText(new Coordinates(120, 140), cypher, "finalSlide", null);
		lang.newText(new Coordinates(20, 160), translator.translateMessage("finalSlide5"), "finalSlide", null);
		lang.newText(new Coordinates(120, 160), result, "finalSlide", null);

		lang.nextStep(translator.translateMessage("contents4"));

		return lang.toString();

	}

	public String getName() {
		if (getContentLocale() == Locale.GERMANY)
			return "Enigma Entschlüsselung";
		else
			return "Enigma Decryption";
	}

	public String getAlgorithmName() {
		if (getContentLocale() == Locale.GERMANY)
			return "Enigma Entschlüsselung";
		else
			return "Enigma Decryption";
	}

	public String getAnimationAuthor() {
		return "Marius Gassen, Markus Danz";
	}

	public String getDescription() {
		if (getContentLocale() == Locale.GERMANY)
			return DESCRIPTION_GERMAN;
		return DESCRIPTION_ENGLISH;
	}

	public String getCodeExample() {
		if (loc == Locale.GERMANY)
			return SOURCE_CODE_GERMAN;
		return SOURCE_CODE_ENGLISH;
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return loc;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		String cypher = ((String) primitives.get("Chiffre")).toUpperCase(Locale.GERMANY).trim().replace("Ü", "UE")
				.replace("Ö", "OE").replaceAll("Ä", "AE");
		String[] rotorA = (String[]) primitives.get("Walze A");
		String[] rotorB = (String[]) primitives.get("Walze B");
		String[] rotorC = (String[]) primitives.get("Walze C");
		String[] reflector = (String[]) primitives.get("Umkehrwalze");
		String[] plugboard = (String[]) primitives.get("Steckerbrett");
		String startPositionA = (String) primitives.get("Startposition Walze A");
		String startPositionB = (String) primitives.get("Startposition Walze B");
		String startPositionC = (String) primitives.get("Startposition Walze C");
		String notchA = (String) primitives.get("Uebertragskerbe Walze A");
		String notchB = (String) primitives.get("Uebertragskerbe Walze B");
		String notchC = (String) primitives.get("Uebertragskerbe Walze C");

		if (!isValidAlphabet(rotorA))
			throw new IllegalArgumentException(
					"Walze A hat ein unvollständiges Alphabet. Stellen Sie sicher, dass jeder Buchstabe genau einmal vorkommt und das Alphabet 26 Zeichen hat.");
		if (!isValidAlphabet(rotorB))
			throw new IllegalArgumentException(
					"Walze B hat ein unvollständiges Alphabet. Stellen Sie sicher, dass jeder Buchstabe genau einmal vorkommt und das Alphabet 26 Zeichen hat.");
		if (!isValidAlphabet(rotorC))
			throw new IllegalArgumentException(
					"Walze C hat ein unvollständiges Alphabet. Stellen Sie sicher, dass jeder Buchstabe genau einmal vorkommt und das Alphabet 26 Zeichen hat.");
		if (!isValidAlphabet(reflector))
			throw new IllegalArgumentException(
					"Die Umkehrwalze hat ein unvollständiges Alphabet. Stellen Sie sicher, dass jeder Buchstabe genau einmal vorkommt und das Alphabet 26 Zeichen hat.");
		if (!isValidAlphabet(plugboard))
			throw new IllegalArgumentException(
					"Das Steckerbrett hat ein unvollständiges Alphabet. Stellen Sie sicher, dass jeder Buchstabe genau einmal vorkommt und das Alphabet 26 Zeichen hat.");
		if (!isValidPosition(startPositionA))
			throw new IllegalArgumentException(
					"Die Startposition von Walze A ist ungültig. Stellen Sie sicher, dass diese genau einem Buchstaben zwischen A und Z entspricht.");
		if (!isValidPosition(startPositionB))
			throw new IllegalArgumentException(
					"Die Startposition von Walze B ist ungültig. Stellen Sie sicher, dass diese genau einem Buchstaben zwischen A und Z entspricht.");
		if (!isValidPosition(startPositionC))
			throw new IllegalArgumentException(
					"Die Startposition von Walze C ist ungültig. Stellen Sie sicher, dass diese genau einem Buchstaben zwischen A und Z entspricht.");
		if (!isValidPosition(notchA))
			throw new IllegalArgumentException(
					"Die Übertragskerbe von Walze A ist ungültig. Stellen Sie sicher, dass diese genau einem Buchstaben zwischen A und Z entspricht.");
		if (!isValidPosition(notchB))
			throw new IllegalArgumentException(
					"Die Übertragskerbe von Walze B ist ungültig. Stellen Sie sicher, dass diese genau einem Buchstaben zwischen A und Z entspricht.");
		if (!isValidPosition(notchC))
			throw new IllegalArgumentException(
					"Die Übertragskerbe von Walze C ist ungültig. Stellen Sie sicher, dass diese genau einem Buchstaben zwischen A und Z entspricht.");
		if (!isValidMessage(cypher))
			throw new IllegalArgumentException(
					"Die Nachricht ist ungültig. Stellen Sie sicher, dass die Nachricht mindestens ein Zeichen enthält.");
		return true;
	}

	private boolean isValidAlphabet(String[] alphabet) {
		if (alphabet.length != 26)
			return false;
		for (Character c : LETTERS) {
			boolean found = false;
			for (int i = 0; i < alphabet.length; i++) {
				if (alphabet[i].length() != 1)
					return false;
				if (alphabet[i].equals(c.toString()))
					found = true;
			}
			if (!found)
				return false;
		}
		return true;
	}

	private boolean isValidPosition(String position) {
		if (position.length() != 1)
			return false;
		for (Character letter : LETTERS) {
			if (letter.toString().equals(position))
				return true;
		}
		return false;
	}

	private boolean isValidMessage(String message) {
		return message.length() > 0;
	}
}