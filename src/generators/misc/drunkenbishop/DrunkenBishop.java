package generators.misc.drunkenbishop;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.properties.tree.PropertiesTreeModel;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Hashtable;
import java.util.Locale;

import util.KalnischkiesGrid;
import util.KalnischkiesGrid.GridStyle;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class DrunkenBishop implements ValidatingGenerator {
	private Language lang;
	private SourceCodeProperties sourceCodeProp;
	private TextProperties plainProp;
	private TextProperties boldProp;
	private MatrixProperties fpwordProp;
	private MatrixProperties fprintProp;
	private MatrixProperties coinLegendProp;
	private MatrixProperties chessProp;

	private static final String INTRO = ""
			+ "Bishop Peter finds himself in the middle of an ambient atrium.\n"
			+ "There are walls on all four sides and apparently there is no exit.\n"
			+ "The floor is paved with square tiles, strictly alternating between black and white.\n"
			+ "His head heavily aching – probably from too much wine he had before –\n"
			+ "he starts wandering around…";

	private static final String POSITION = "Bishop Peter places his mitre (##MITRE##) down on the floor to mark the place he starts from.\n"
			+ "It is a very nice place in the middle of the board, because of the odd row and column count.\n"
			+ "(Note also that on a terminal, the space between lines \\\"distorts\\\" the image so it looks different here)\n"
			+ "\n"
			+ "While the position can be described with coordinates, the bishop decides to give each tile\n"
			+ "a number to make it easier to talk about movement later.\n"
			+ "\n" + "A tile at (x,y) has the number = x * ##COLUMNS## + y";

	private static final String COINS = ""
			+ "The idea to mark his way across the room seems to be a good idea, so he shortly ponders\n"
			+ "about how he should do it and finds a few coins in his pocket: \\\"That should do it for the moment!\\\"";

	private static final String FINGERPRINT = ""
			+ "While our bishop believes he is free to move in the room,\n"
			+ "we know that his movement is defined by a higher authority:\n"
			+ "The OpenSSH fingerprint the algorithm got as input,\n"
			+ "which is a hexadecimal string representing 16 bytes.\n" + "\n"
			+ "As a bishop, he moves only diagonal steps,\n"
			+ "so we have 4 ways of moving.\n"
			+ "Those moves can be represented with 2 bits…\n"
			+ "and the fingerprint is 64 * 2 bits!\n";

	private static final String LITTLEENDIAN = ""
			+ "Looking at the first byte (reading left to right)\n"
			+ "we have to decided in which order we read the bit-pairs:\n"
			+ "As unfamilar as it might be, the algorithm decides to\n"
			+ "read the bit-pairs right to left (also known as: little endian).";

	private static final String[] MOVEMENT = {
			"The four possible bit-pairs allow these moves:",
			"##PAIR## = ##MOVE## move (position change: ##POS##)" };

	private static final String WALLBUMP = "One last thing:\n"
			+ "As the room is enclosed by walls our bishop has to\n"
			+ "decide how to handle \\\"bumping\\\" into the wall.\n"
			+ "He will simply cancel the move into the wall,\n"
			+ "but still does the move alongside the wall.\n"
			+ "This means he changes tile-color by wall bumping.\n"
			+ "(except in \\\"edge-cases\\\" of course)\n\n"
			+ "But now: Lets get moving!";

	private static final String NOCOINS = "After 64 steps bishop Peter runs out of coins.\n"
			+ "(And we have also no bit-pairs left to move him around).\n"
			+ "Bishop Peter sits down here (##END##),"
			+ "his head still arching and a bit exhausted,\n"
			+ "he starts thinking about what to do next.\n" + "But suddently …";

	private static final String OUTRO = ""
			+ "Many (scientific) questions about this relatively new algorithm\n"
			+ "are still unanswered and so its not shown by default in SSH on connect,\n"
			+ "which in turn means most people will encounter it only on key creation.\n"
			+ "\n"
			+ "It is for example unclear how easy it is to create image-collisions,\n"
			+ "how many different shapes can be created by the algorithm (or recognised by humans)\n"
			+ "or if different rules (board size, movement rules, …) could improve the algorithm.\n"
			+ "Are you up to follow the footsteps of drunken bishop Peter?\n"
			+ "\n"
			+ "More information: http://dirk-loss.de/sshvis/drunken_bishop.pdf";

	private static final String WAKEUPPETER = "Peter wakes up in his bed. \\\"What a strange dream!\\\"";

	private int grid_rows;
	private int grid_columns;
	private int coins_in_pocket;
	private int curPos;
	private KalnischkiesGrid<String> grid;
	private KalnischkiesGrid<String> fprint;
	private KalnischkiesGrid<String> fpword;
	private Text coinCounter;
	private KalnischkiesGrid<String> coinLegend;
	private Text[] moveText;

	private String fingerprint;
	private String coins;
	private char position_start;
	private char position_end;
	private Text currentPosition;
	private Text fprinttitle;

	@Override
	public void init() {
		this.lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);

		this.lang.setStepMode(true);
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		this.fpwordProp = (MatrixProperties) props
				.getPropertiesByName("current-fingerprint-word");
		this.fprintProp = (MatrixProperties) props
				.getPropertiesByName("fingerprint");
		this.coinLegendProp = (MatrixProperties) props
				.getPropertiesByName("coin-legend");
		this.chessProp = (MatrixProperties) props
				.getPropertiesByName("board");
		// setup font for various descriptions
		this.sourceCodeProp = (SourceCodeProperties) props
				.getPropertiesByName("descriptions");
		this.plainProp = new TextProperties();
		Font plainfont = (Font) this.sourceCodeProp
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		this.plainProp.set(AnimationPropertiesKeys.FONT_PROPERTY, plainfont);
		this.boldProp = new TextProperties();
		Font boldfont = plainfont.deriveFont(Font.BOLD);
		this.boldProp.set(AnimationPropertiesKeys.FONT_PROPERTY, boldfont);
		// get primitives
		drunkenBishop((String) primitives.get("fingerprint"),
				(String) primitives.get("coins"),
				(Integer) primitives.get("board-rows"),
				(Integer) primitives.get("board-columns"));
		return this.lang.toString();
	}

	private void drunkenBishop(String fingerprint, String coins, int grid_rows,
			int grid_cols) {
		this.coins_in_pocket = 64;
		this.curPos = -1;
		this.moveText = new Text[6];
		// extract start/end marker from coin string
		this.position_start = coins.charAt(coins.length() - 2);
		this.position_end = coins.charAt(coins.length() - 1);
		this.coins = coins.substring(0, coins.length() - 2);
		// ensure that the first coin is no coin
		if (this.coins.charAt(0) != ' ')
			this.coins = " " + this.coins;
		this.fingerprint = fingerprint;
		this.grid_rows = grid_rows;
		this.grid_columns = grid_cols;

		intro();
		steps();
		noCoinsLeft();
		interpretation();
	}

	private void interpretation() {
		this.lang
				.newText(
						new Coordinates(15, 80),
						"And we are left alone with a strange image for our fingerprint",
						"dream-stop2", null, this.plainProp);
		this.lang.newText(new Coordinates(15, 95), this.fingerprint,
				"dream-stop3", null, this.plainProp);

		SourceCodeProperties monospace = new SourceCodeProperties();
		monospace.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		monospace.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode randomImage = this.lang.newSourceCode(new Coordinates(15,
				110), "randomimage", null, monospace);
		String topbottom = "+";
		for (int i = 0; i < this.grid_columns; ++i)
			topbottom += '-';
		topbottom += '+';

		randomImage.addCodeLine(topbottom, null, 0, null);
		for (int i = 0; i < this.grid_rows; ++i)
			randomImage.addCodeLine("|" + this.grid.getRow(i, " ", "") + "|",
					null, 0, null);
		randomImage.addCodeLine(topbottom, null, 0, null);

		SourceCode outroDescription = this.lang.newSourceCode(new Coordinates(
				15, 300), "outrodescription", null, this.sourceCodeProp);
		for (String line : (OUTRO).split("\n"))
			outroDescription.addCodeLine(line, null, 0, null);
	}

	private void noCoinsLeft() {
		int[] pos = this.grid.getPositionFromTile(this.curPos);
		this.grid.setGridValue(pos[0], pos[1], "" + this.position_end);
		this.grid.unhighlightCell(pos[0], pos[1]);

		SourceCode nocoinsDescription = this.lang.newSourceCode(new Offset(0,
				20, "movetext5", "NW"), "nocoinsdescription", null,
				this.sourceCodeProp);
		for (String line : NOCOINS.replace("##END##", "" + this.position_end)
				.split("\n"))
			nocoinsDescription.addCodeLine(line, null, 0, null);

		this.lang.nextStep();
		this.lang.hideAllPrimitives();
		this.fpword.hide();
		this.fprint.hide();
		this.grid.hide();
		this.coinLegend.hide();

		this.lang.newText(new Coordinates(60, 200), WAKEUPPETER, "dream-stop",
				null, this.boldProp);

		this.lang.nextStep();
		this.lang.hideAllPrimitives();
	}

	private void steps() {
		int fingerprintparts = this.fingerprint.split(":").length;
		for (int word = 0; word < fingerprintparts; ++word) {
			setFingerprintByte(word);
			for (int i = 0; i < 4; ++i) {
				// show the info needed for the next step
				this.fpword.highlightCell(0, 3 - i);
				setMovementOptions(this.curPos);

				this.lang.nextStep();
				this.fpword.unhighlightCell(0, 3 - i);

				// move and drop a coin
				String pair = this.fpword.getElement(0, 3 - i);
				int move[] = getMovementVector(this.curPos, this.grid_rows,
						this.grid_columns);
				int bitpair = Integer.parseInt(pair, 2);
				setPosition(this.curPos + move[bitpair]);
				dropCoinAndPause(this.curPos);
			}
		}
	}

	private void dropCoinAndPause(int curPos) {
		int[] pos = this.grid.getPositionFromTile(curPos);
		setCoinCounter(--this.coins_in_pocket);
		String current = this.grid.getElement(pos[0], pos[1]);
		if (current == null || current.isEmpty())
			current = " ";
		int stack = this.coinLegend.findColumn(1, current);
		if (stack != -1) {
			if ((stack + 1) < this.coinLegend.getNumberOfColumns())
				++stack;
			this.coinLegend.highlightCell(0, stack);
			this.coinLegend.highlightCell(1, stack);
			String now = this.coinLegend.getElement(1, stack);
			this.grid.setGridValue(pos[0], pos[1], now);
		}
		this.lang.nextStep();
		if (stack != -1) {
			this.coinLegend.unhighlightCell(0, stack);
			this.coinLegend.unhighlightCell(1, stack);
		}
	}

	private void intro() {
		intro_motivation();
		intro_position();
		intro_coins();
		intro_fingerprint();
		intro_littleendian();
		intro_movement();

	}

	private void intro_movement() {
		this.moveText[0] = this.lang.newText(new Offset(0, 10,
				"fingerprintgrid", "SW"), MOVEMENT[0], "movetext0", null,
				this.plainProp);
		this.moveText[1] = this.lang.newText(
				new Offset(0, 2, "movetext0", "SW"), "##PLACEHOLDER##",
				"movetext1", null, this.plainProp);
		this.moveText[2] = this.lang.newText(
				new Offset(0, 2, "movetext1", "SW"), "##PLACEHOLDER##",
				"movetext2", null, this.plainProp);
		this.moveText[3] = this.lang.newText(
				new Offset(0, 2, "movetext2", "SW"), "##PLACEHOLDER##",
				"movetext3", null, this.plainProp);
		this.moveText[4] = this.lang.newText(
				new Offset(0, 2, "movetext3", "SW"), "##PLACEHOLDER##",
				"movetext4", null, this.plainProp);
		this.moveText[5] = this.lang.newText(
				new Offset(0, 2, "movetext4", "SW"), "##PLACEHOLDER##",
				"movetext5", null, this.plainProp);

		setMovementOptions(this.curPos);

		SourceCode moveDescription = this.lang.newSourceCode(new Offset(0, 20,
				"movetext4", "NW"), "movedescription", null,
				this.sourceCodeProp);
		for (String line : (WALLBUMP).split("\n"))
			moveDescription.addCodeLine(line, null, 0, null);

		this.lang.nextStep();
		moveDescription.hide();
	}

	private void setMovementOptions(int curPos) {
		int[] moveVector = getMovementVector(curPos, this.grid_rows,
				this.grid_columns);
		for (int i = 0; i < moveVector.length; ++i) {
			String moveStr = getStringForPositionChange(moveVector[i]);

			String id = Integer.toBinaryString(i);
			while (id.length() < 2)
				id = "0" + id;

			String move = MOVEMENT[1].replace("##PAIR##", id)
					.replace("##MOVE##", moveStr)
					.replace("##POS##", "" + moveVector[i]);
			this.moveText[i + 1].setText(move, null, null);
		}
		int[] freeMove = { -(this.grid_columns + 1), -(this.grid_columns - 1),
				(this.grid_columns - 1), (this.grid_columns + 1) };
		boolean blocked = false;
		for (int i = 0; i < freeMove.length; ++i)
			if (freeMove[i] != moveVector[i])
				blocked = true;
		if (blocked == true)
			this.moveText[5].setText(
					"(Note how the moves are (partly) canceled by the wall)",
					null, null);
		else
			this.moveText[5].setText("", null, null);
	}

	private String getStringForPositionChange(int move) {
		if (move == -(this.grid_columns + 1))
			return "↖";
		else if (move == -this.grid_columns)
			return "↑";
		else if (move == -(this.grid_columns - 1))
			return "↗";
		else if (move == -1)
			return "←";
		else if (move == 0)
			return "no";
		else if (move == 1)
			return "→";
		else if (move == (this.grid_columns + 1))
			return "↘";
		else if (move == this.grid_columns)
			return "↓";
		else if (move == (this.grid_columns - 1))
			return "↙";
		else
			return "##?" + move + "?##";
	}

	static int[] getMovementVector(int curPos, int grid_rows, int grid_columns) {
		int[] moveVector = { 0, 0, 0, 0 };
		if (curPos / grid_columns >= 1) {
			moveVector[0] -= grid_columns;
			moveVector[1] -= grid_columns;
		}
		if (curPos < grid_columns * (grid_rows - 1)) {
			moveVector[2] += grid_columns;
			moveVector[3] += grid_columns;
		}
		if (curPos % grid_columns != 0) {
			moveVector[0] -= 1;
			moveVector[2] -= 1;
		}
		if (curPos % grid_columns != (grid_columns - 1)) {
			moveVector[1] += 1;
			moveVector[3] += 1;
		}
		return moveVector;
	}

	private void intro_littleendian() {
		this.fprinttitle = this.lang.newText(new Offset(20, 0, "chess", "NE"),
				"PLACEHOLDER-FINGERBYTE", "fingerprint-byte", null,
				this.plainProp);
		this.fpword = new KalnischkiesGrid<String>(this.lang, "fingerprintgrid",
				new Offset(0, 10, "fingerprint-byte", "SW"), 1, 4, 2,
				GridStyle.TABLE, this.fpwordProp);
		setFingerprintByte(0);
		this.fpword.highlightCell(0, 3);

		SourceCode endianDescription = this.lang.newSourceCode(new Offset(0,
				20, "fingerprintgrid", "NW"), "endiandescription", null,
				this.sourceCodeProp);
		for (String line : LITTLEENDIAN.split("\n"))
			endianDescription.addCodeLine(line, null, 0, null);

		this.lang.nextStep();
		endianDescription.hide();
	}

	private void intro_fingerprint() {
		SourceCode printDescription = this.lang.newSourceCode(new Offset(20, 0,
				"chess", "NE"), "printdescription", null, this.sourceCodeProp);
		for (String line : FINGERPRINT.split("\n"))
			printDescription.addCodeLine(line, null, 0, null);

		this.lang.newText(new Offset(0, 10, "title", "SW"), "Fingerprint: ",
				"fingerprint-hexdecimal", null, this.plainProp);
		String[] words = this.fingerprint.split(":");
		this.fprint = new KalnischkiesGrid<String>(this.lang, "fingerprinthexgrid",
				new Offset(5, 0, "fingerprint-hexdecimal", "NE"), 1,
				words.length, 2, GridStyle.PLAIN, this.fprintProp);
		for (int i = 0; i < words.length; ++i)
			this.fprint.setGridValue(0, i, words[i]);

		this.lang.nextStep();
		printDescription.hide();
	}

	private void setFingerprintByte(int word) {
		if (word != 0)
			this.fprint.unhighlightCell(0, word - 1);
		this.fprint.highlightCell(0, word);
		String wordStr = this.fprint.getElement(0, word);
		int dec = Integer.parseInt(wordStr, 16);
		this.fprinttitle.setText("Looking at byte: " + wordStr + " (decimal: "
				+ dec + ")", null, null);
		String binary = Integer.toBinaryString(dec);
		while (binary.length() < 8)
			binary = "0" + binary;
		for (int i = 0; i < binary.length(); i += 2)
			this.fpword.setGridValue(0, i / 2, binary.substring(i, i + 2));
	}

	private void intro_coins() {
		SourceCode coinDescription = this.lang.newSourceCode(new Offset(0, 20,
				this.currentPosition, "SW"), "coindescription", null,
				this.sourceCodeProp);
		for (String line : COINS.split("\n"))
			coinDescription.addCodeLine(line, null, 0, null);

		this.lang.nextStep();
		coinDescription.hide();

		this.coinCounter = this.lang.newText(new Offset(0, 20,
				this.currentPosition, "SW"), "PLACEHOLDER-COUNTER",
				"coincounter", null, this.plainProp);
		setCoinCounter(this.coins_in_pocket);

		this.lang
				.newText(
						new Offset(0, 20, this.coinCounter, "SW"),
						"The amount of coins on the floor tile is represented by the following characters:",
						"coinlegendtext", null, this.plainProp);
		this.coinLegend = new KalnischkiesGrid<String>(this.lang, "coinlegend",
				new Offset(0, 10, "coinlegendtext", "SW"), 2,
				this.coins.length(), 2, GridStyle.TABLE, this.coinLegendProp);
		for (int i = 0; i < this.coins.length(); ++i) {
			this.coinLegend.setGridValue(0, i, "" + i);
			this.coinLegend.setGridValue(1, i, "" + this.coins.charAt(i));
		}
		this.lang
				.newText(
						new Offset(0, 10, "coinlegend", "SW"),
						"(If more coins are on the floor than representable the highest character will remain.)",
						"coinlegendtext", null, this.plainProp);

		this.lang.nextStep();
	}

	private void intro_position() {
		String[] header_row = new String[this.grid_rows];
		for (int i = 0; i < this.grid_rows; ++i)
			header_row[i] = "" + i;
		String[] header_col = new String[this.grid_columns];
		for (int i = 0; i < this.grid_columns; ++i)
			header_col[i] = "" + i;
		this.grid = new KalnischkiesGrid<String>(this.lang, "chess", new Offset(0,
				50, "title", "NW"), header_row, header_col, 3,
				GridStyle.CHESSBLACK, this.chessProp);

		this.currentPosition = this.lang.newText(new Offset(0, 20, "chess",
				"SW"), "PLACEHOLDER-POSITION", "currentposition", null,
				this.plainProp);
		this.currentPosition.hide();
		int pos_row = (this.grid_rows - 1) / 2;
		int pos_col = (this.grid_columns - 1) / 2;
		int pos = pos_row * this.grid_columns + pos_col;
		setPosition(pos);
		this.grid.setGridValue(pos_row, pos_col, "" + this.position_start);

		SourceCode positionDescription = this.lang.newSourceCode(new Offset(0,
				20, "chess", "SW"), "firststep", null, this.sourceCodeProp);
		for (String line : (POSITION.replace("##MITRE##", ""
				+ this.position_start).replace("##COLUMNS##", ""
				+ this.grid_columns)).split("\n"))
			positionDescription.addCodeLine(line, null, 0, null);

		this.lang.nextStep();
		this.currentPosition.show();
		positionDescription.hide();
	}

	private void intro_motivation() {
		this.lang.newText(new Coordinates(10, 20), getName(), "title", null,
				this.boldProp);

		SourceCode motivation = this.lang.newSourceCode(new Offset(0, 40,
				"title", "NW"), "motivation", null, this.sourceCodeProp);
		for (String line : INTRO.split("\n"))
			motivation.addCodeLine(line, null, 0, null);

		this.lang.nextStep();
		motivation.hide();
	}

	private void setPosition(int p) {
		if (this.curPos != -1) {
			int[] oldpos = this.grid.getPositionFromTile(this.curPos);
			this.grid.unhighlightCell(oldpos[0], oldpos[1]);
		}

		int[] pos = this.grid.getPositionFromTile(p);
		this.curPos = p;
		this.currentPosition.setText("Current Position: " + p + " at ("
				+ pos[0] + "," + pos[1] + ")", null, null);
		this.grid.highlightCell(pos[0], pos[1]);
	}

	private void setCoinCounter(int coins) {
		this.coinCounter.setText("Remaining coins: " + coins, null, null);
	}

	@Override
	public String getName() {
		return "Drunken Bishop: SSH RandomArt";
	}

	@Override
	public String getAlgorithmName() {
		return "Drunken Bishop";
	}

	@Override
	public String getAnimationAuthor() {
		return "David Kalnischkies";
	}

	@Override
	public String getDescription() {
		return (INTRO
				+ "\n\n"
				+ "Well, to be exact, he only makes diagonal steps – just like a bishop on a chess board.\n"
				+ "When he hits a wall, he moves to the side, which takes him from the black tiles to the white tiles (or vice versa).\n"
				+ "And after each move, he places a coin on the floor, to remember that he has been there before.\n"
				+ "\n"
				+ "After 64 steps, just when no coins are left, Peter suddenly wakes up.\n"
				+ "What a strange dream! And what a great idea for a random art algorithm!\n"
				+ "\n"
				+ "Verifying the Fingerprint of a machine you want to connect to via ssh is quiet tiresome,\n"
				+ "and minor variations in the fingerprint might went unnoticed.\n" + "Looking at simple ascii images on the other hand might be easier.")
				.replace("\n\n", "<br /><br />");
	}

	@Override
	public String getCodeExample() {
		return "Mark start position\n"
				+ "Split fingerprint into hexdecimal number pairs\n"
				+ "Read first unseen hex-pair from left to right and convert to 8 bits\n"
				+ "\tRead first unseen bit-pair from right to left\n"
				+ "\t\tMove into current bit-pair defined direction\n"
				+ "\t\tDrop a coin on the current position\n"
				+ "\tRepeat until all bit-pairs are seen\n"
				+ "Repeat until all hex-pairs are seen\n" + "Mark end position";
	}

	@Override
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		String fingerprint = ((String) primitives.get("fingerprint")).trim();
		if (fingerprint.length() != 47)
			throw new IllegalArgumentException(
					"A valid OpenSSH fingerprint is 64 characters long. The provided one has a length of "
							+ fingerprint.length() + ".");
		String[] words = fingerprint.split(":");
		if (words.length != 16)
			throw new IllegalArgumentException(
					"A valid OpenSSH fingerprint consists of 16 hexdecimal pairs. The provided one has "
							+ words.length);
		for (int i = 0; i < words.length; ++i) {
			if (words[i].length() != 2)
				throw new IllegalArgumentException(
						"A valid hexdecimal pair consists of 2 characters. \"Pair\" "
								+ i + " consists of " + words[i].length());
			int dec = -1;
			try {
				dec = Integer.parseInt(words[i], 16);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Pair "
								+ i
								+ " ("
								+ words[i]
								+ ") in the fingerprint is not a valid hexidecimal number.");
			}
			if (dec < 0)
				throw new IllegalArgumentException(
						"Pair "
								+ i
								+ " ("
								+ words[i]
								+ ") in the fingerprint must be a positive hexidecimal number.");
			// the following should be impossible to reach
			if (Integer.toBinaryString(dec).length() > 8)
				throw new IllegalArgumentException(
						"Pair "
								+ i
								+ " ("
								+ words[i]
								+ ") in the fingerprint can't be represented with 8 bits.");
		}

		String coins = (String) primitives.get("coins");
		if (coins.length() < 3)
			throw new IllegalArgumentException(
					"The coinstring must be at least 3 characters long as the last two are taken as start and endposition!");

		int grid_rows = (Integer) primitives.get("board-rows");
		if (grid_rows % 2 == 0)
			throw new IllegalArgumentException(
					"The board needs an odd row count so that the bishop can start in the exact middle of the board!");
		int grid_cols = (Integer) primitives.get("board-columns");
		if (grid_cols % 2 == 0)
			throw new IllegalArgumentException(
					"The board needs an odd column count so that the bishop can start in the exact middle of the board!");
		if (grid_rows == 1 || grid_cols == 1)
			throw new IllegalArgumentException(
					"The board should really be two-dimensional to be interesting, don't you think?");
		return true;
	}

	public static void main(String[] args) {
		DrunkenBishop s = new DrunkenBishop();
		s.init();
		PropertiesTreeModel ptm = new PropertiesTreeModel();
		ptm.loadFromXMLFile(
				s.getClass().getName().replace('.', File.separatorChar)
						+ ".xml", true);
		AnimationPropertiesContainer container = ptm.getPropertiesContainer();
		Hashtable<String, Object> primitives = ptm.getPrimitivesContainer();
		System.out.println(s.generate(container, primitives));
	}
}