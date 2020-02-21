package generators.hashing;

import java.awt.Color;
import java.awt.Font;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

import javax.swing.*;

/**
 * @author Dorian Arnouts, Alexander Peerdeman
 */
public class CuckooHashing implements ValidatingGenerator {
	private static Language lang;
	private Locale locale;
	private Translator translator;

	private SourceCodeProperties sourceCode;
	private int initialTableSize;
	private int recursionDepthToDisplay;
	private int[] elementsToInsert;
	private double desiredFillLevel;
	private int[] existingElements;
	private int factorIncreasingTableSize;
	private TextProperties text;
	private MatrixProperties hashTables;

	public CuckooHashing(Locale l) {
		locale = l;
		translator = new Translator("resources/CuckooHashing", locale);
	}

	public void init() {
		lang = new AnimalScript("Cuckoo Hashing", "Alexander Peerdeman, Dorian Arnouts", 800, 600);
	}

	public static void main(String[] args) {
		Generator generator = new CuckooHashing(Locale.GERMANY);
//		Generator generator = new CuckooHashing(Locale.US);
		Animal.startGeneratorWindow(generator);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// Get Primitives and Properties
		sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		initialTableSize = (Integer) primitives.get("initialTableSize");
		recursionDepthToDisplay = (Integer) primitives.get("recursionDepthToDisplay");
		elementsToInsert = (int[]) primitives.get("elementsToInsert");
		desiredFillLevel = (double) primitives.get("desiredFillLevel");
		existingElements = (int[]) primitives.get("existingElements");
		factorIncreasingTableSize = (Integer) primitives.get("factorIncreasingTableSize");
		text = (TextProperties) props.getPropertiesByName("text");
		hashTables = (MatrixProperties) props.getPropertiesByName("hashTables");

		Integer[] elementsToInsert2 = Arrays.stream(elementsToInsert).boxed().toArray(Integer[]::new);
		Integer[] existingElements2 = Arrays.stream(existingElements).boxed().toArray(Integer[]::new);

		CuckooHashingAnimation.generate(lang, translator, sourceCode, initialTableSize, recursionDepthToDisplay,
				elementsToInsert2, desiredFillLevel, existingElements2, factorIncreasingTableSize, text, hashTables);
		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return translator.translateMessage("algoName");
	}

	@Override
	public String getAnimationAuthor() {
		return "Alexander Peerdeman, Dorian Arnouts";
	}

	@Override
	public String getCodeExample() {
		return "insert(table1, table2, h1, h2, value)" + "\n" + "    increaseSizeIfNecessary(table1, table2)" + "\n"
				+ "    if table1[h1(value)] = null or table1[h1(value)] = value then" + "\n"
				+ "        table1[h1(value)] <- value; return" + "\n"
				+ "    if table2[h2(value)] = null or table2[h2(value)] = value then" + "\n"
				+ "        table2[h2(value)] <- value; return	" + "\n" + "    oldTable1 = table1; oldTable2 = table2"
				+ "\n" + "    while true do" + "\n" + "        x <-> table1[h1(value)]" + "\n"
				+ "        if x = null then" + "\n" + "            return" + "\n" + "        x <-> table2[h2(value)]"
				+ "\n" + "        if x = null then	" + "\n" + "            return" + "\n"
				+ "        if oldTable1 = table1 and oldTable2 = table2 then" + "\n" + "            break" + "\n"
				+ "    rehash(table1, table2, h1, h2)" + "\n" + "    insert(table1, table2, h1, h2, value)" + "\n"
				+ "\n" + "rehash(table1, table2, h1, h2)" + "\n"
				+ "    bufferedElements := getAllElements(table1, table2)" + "\n"
				+ "    h1 = new Hashfunction; h2 = new Hashfunction" + "\n" + "    clear(table1, table2)" + "\n"
				+ "    for all x in bufferedElements do" + "\n" + "        insert(table1, table2, h1, h2, x)" + "\n";
	}

	@Override
	public Locale getContentLocale() {
		return locale;
	}

	@Override
	public String getDescription() {
		return translator.translateMessage("algoDescription").replace("quot;", "'");
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}

	@Override
	public String getName() {
		return translator.translateMessage("name");
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer,
			Hashtable<String, Object> hashtable) throws IllegalArgumentException {
	    int initializeTableSize = (Integer) hashtable.get("initialTableSize");
	    if(initializeTableSize <= 0) {
            JOptionPane.showMessageDialog(null, translator.translateMessage("sizeAllowedValues"),
                    translator.translateMessage("invalidInput"), JOptionPane.OK_OPTION);
            return false;
	    }
        int recursionDepth = (Integer) hashtable.get("recursionDepthToDisplay");
        if(recursionDepth < 0) {
            JOptionPane.showMessageDialog(null, translator.translateMessage("recursionDepth"),
                    translator.translateMessage("invalidInput"), JOptionPane.OK_OPTION);
            return false;
        }
        double fillLevel = (double) hashtable.get("desiredFillLevel");
        if(fillLevel <= 0 || fillLevel > 1) {
            JOptionPane.showMessageDialog(null, translator.translateMessage("fillLevel"),
                    translator.translateMessage("invalidInput"), JOptionPane.OK_OPTION);
            return false;
        }
        int factorIncreasingTableSize = (Integer) hashtable.get("factorIncreasingTableSize");
        if(factorIncreasingTableSize <= 1) {
            JOptionPane.showMessageDialog(null, translator.translateMessage("factorIncreasing"),
                    translator.translateMessage("invalidInput"), JOptionPane.OK_OPTION);
            return false;
        }
		return true;
	}
}

/**
 * @author Dorian Arnouts, Alexander Peerdeman
 */
class CuckooHashingAnimation {
	private Language lang;
	private Translator t;

	// Style objects
	private TextProperties headerTextProps;
	private TextProperties textProps;
	private MatrixProperties tableProps;
	private SourceCodeProperties sourceCodeProps;
	private ArrayProperties arrayProps;

	private Color borderColor;

	private Text header;
	private Text message;
	private Rect headerRect;

	private Integer table1[];
	private Integer table2[];
	private int tableSize;

	// test if random hashing works
	private Random rng = new Random(0);
	private int random1 = 0;
	private int random2 = 1;

	private StringMatrix tableList1;
	private StringMatrix tableList2;
	private Integer maxWidth;
	private StringArray intArray;
	private SourceCode code;
	private Stack<Integer> codeHighlight;

	// determines how full the tables are allowed to be
	private static double DESIRED_FILL_LEVEL;
	private static int INITIAL_TABLE_SIZE;
	private static int FACTOR_SIZE_INCREASE;
	private static int RECURSION_LEVEL_TO_DISPLAY;

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "CuckooHashing",
				"Dorian Arnouts, Alexander Peerdeman", 800, 600);
		Translator t = new Translator("resources/CuckooHashing", Locale.GERMANY);

		CuckooHashingAnimation cha = new CuckooHashingAnimation(l, t, new SourceCodeProperties(), 6, 20, 0.85, 2,
				new TextProperties(), new MatrixProperties());

		Random rng = new Random(0);

		ArrayList<Integer> randomElements = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			randomElements.add(rng.nextInt(1000));
		}
		Integer[] elementsAlreadyExistant = randomElements.toArray(new Integer[] {});

		randomElements = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			randomElements.add(rng.nextInt(1000));
		}
		Integer[] elementsToBeInserted = randomElements.toArray(new Integer[] {});

		cha.insertAll(elementsAlreadyExistant, elementsToBeInserted);

		String result = l.toString();

		try (PrintWriter out = new PrintWriter("anim.asu")) {
			out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final long endTime = System.nanoTime();
		final long elapsed = endTime - startTime;
		final long ms = TimeUnit.NANOSECONDS.toMillis(elapsed);
		final int lines = result.split("\r\n|\r\n").length;

		System.out.println("Done! Took " + ms + "ms to generate " + lines + " lines.");

	}

	public CuckooHashingAnimation(Language l, Translator t, SourceCodeProperties sourceCode, Integer initialTableSize,
			Integer recursionDepthToDisplay, double desiredFillLevel, Integer factorIncreasingTableSize,
			TextProperties text, MatrixProperties hashTables) {
		this.lang = l;
		this.t = t;

		DESIRED_FILL_LEVEL = desiredFillLevel;
		INITIAL_TABLE_SIZE = initialTableSize;
		FACTOR_SIZE_INCREASE = factorIncreasingTableSize;
		RECURSION_LEVEL_TO_DISPLAY = recursionDepthToDisplay;

		// initial table height
		this.tableSize = INITIAL_TABLE_SIZE;
		this.table1 = new Integer[tableSize];
		this.table2 = new Integer[tableSize];

		this.lang.setStepMode(true);

		initializeStyles(sourceCode, text, hashTables);
	}

	public static void generate(Language l, Translator t, SourceCodeProperties sourceCode, Integer initialTableSize,
			Integer recursionDepthToDisplay, Integer[] elementsToInsert, double desiredFillLevel,
			Integer[] existingElements, Integer factorIncreasingTableSize, TextProperties text,
			MatrixProperties hashTables) {
		CuckooHashingAnimation cha = new CuckooHashingAnimation(l, t, sourceCode, initialTableSize,
				recursionDepthToDisplay, desiredFillLevel, factorIncreasingTableSize, text, hashTables);
		cha.insertAll(existingElements, elementsToInsert);
	}

	/**
	 * Initializes style objects for later use
	 */
	private void initializeStyles(SourceCodeProperties sourceCode, TextProperties text, MatrixProperties hashTables) {

		tableProps = hashTables;
		sourceCodeProps = sourceCode;
		textProps = text;
		arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		borderColor = (Color) hashTables.getItem(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY).get();
	}

	/**
	 * Entry point for generating animation. First build tables with existing
	 * elements, then insert remaining elements and visualize these operations.
	 *
	 * @param elementsAlreadyExistant elements already inside hash tables before
	 *                                animation starts
	 * @param elementsToBeInserted    elements to be inserted within the animation
	 */
	void insertAll(Integer[] elementsAlreadyExistant, Integer[] elementsToBeInserted) {
		// create the header
		List<Primitive> titlePrimitives = createHeader();

		// first slide is an introduction
		createIntroSlide();

		// set up and place message placeholder
		createMessage();

		// determine longest input
		analyzeInput(elementsAlreadyExistant, elementsToBeInserted);

		// show empty tables
		createTables(0);

		// set up source code
		createSourceCode();

		nextStep(null);
		System.out.println("Ex  " + Arrays.toString(elementsAlreadyExistant));
		for (int i : elementsAlreadyExistant) {
			insert(i, null);
		}

		createTables(0);

		System.out.println("Ins " + Arrays.toString(elementsToBeInserted));
		for (int i : elementsToBeInserted) {
			insert(i, 0);
		}

		// hide Primitives except Header
		lang.hideAllPrimitivesExcept(titlePrimitives);

		tableList1.hide();
		tableList2.hide();
		intArray.hide();

		// last slide is a conclusion
		createConclusionSlide();
	}

	private void analyzeInput(Integer[] elementsAlreadyExistant, Integer[] elementsToBeInserted) {
		List<Integer> all = new ArrayList<>();
		all.addAll(Arrays.stream(elementsAlreadyExistant).collect(Collectors.toList()));
		all.addAll(Arrays.stream(elementsToBeInserted).collect(Collectors.toList()));
		maxWidth = all.stream().map(x -> Integer.toString(x)).map(x -> x.length()).reduce(Integer::max).orElse(0);
	}

	private void createTables(Integer depth) {
		if (depth == null || depth > RECURSION_LEVEL_TO_DISPLAY)
			return;

		// hide old tables
		if (tableList1 != null) {
			tableList1.hide();
		}

		if (tableList2 != null) {
			tableList2.hide();
		}

		// create new ones
		Offset left = new Offset(0, 10, headerRect, AnimalScript.DIRECTION_SW);
		tableList1 = createdStringMatrix(table1, left);

		Offset right = new Offset(400, 0, tableList1, AnimalScript.DIRECTION_NE);
		tableList2 = createdStringMatrix(table2, right);
	}

	private StringMatrix createdStringMatrix(Integer[] values, Offset offset) {
		ArrayList<String[]> matrix = new ArrayList<>();

		// always show beginning of table
		if (values.length > 0) {
			matrix.add(representation(values, 0));
		}

		/*
		 * Aggregate empty cells and show filled cells. After an iteration i points
		 * first filled cell and j points to first empty cell. Start at index 1 and stop
		 * before length - 1 because head and tail of tables should always be displayed.
		 */
		int base = 1, i = 1, j = 1;
		while (i < values.length - 1 && j < values.length - 1) {

			// find bounds of empty cell group
			while (i < values.length - 1 && values[i] == null) {
				i++;
			}
			// from there find bound of a group of filled cells
			j = i;
			while (j < values.length - 1 && values[j] != null) {
				j++;
			}
			// check if there were empty cells
			if (i > base) {
				// check it were at least three cells
				if (i - base >= 3) {
					// aggregate them
					matrix.add(new String[] { "...", "..." });
				} else {
					// show them individually
					for (int k = base; k < i; k++) {
						matrix.add(representation(values, k));
					}
				}
			}
			// check if there were filled cells
			if (j > i) {
				// show them individually
				for (int k = i; k < j; k++) {
					matrix.add(representation(values, k));
				}
			}
			// done with this group next group starts at j
			base = j;
			i = j;
		}
		// if the table has at least two values show its tail
		if (values.length >= 2) {
			matrix.add(representation(values, values.length - 1));
		}

		// assemble stringMatrix

		String[][] str = new String[matrix.size()][2];
		for (int index = 0; index < matrix.size(); index++) {
			str[index][0] = matrix.get(index)[0];
			str[index][1] = matrix.get(index)[1];
		}

		StringMatrix strMatr = lang.newStringMatrix(offset, str, "stringMatrix", null, tableProps);

		return strMatr;
	}

	private String[] representation(Integer[] values, int i) {
		int valueIndexLen = Integer.toString(values.length - 1).length();
		int widthIndex = valueIndexLen > 3 ? valueIndexLen : 3;
		int widthValues = maxWidth > 3 ? maxWidth : 3;
		String[] result = new String[2];
		result[0] = String.format("%" + widthIndex + "d", i);
		String value = values[i] == null ? "" : Integer.toString(values[i]);
		result[1] = String.format("%" + widthValues + "s", value);

		return result;
	}

	/**
	 * Insert a new element into the hash tables.
	 *
	 * @param key the key to insert
	 */
	private boolean insert(Integer key, Integer depth) {
		message(t.translateMessage("elementToInsert", Integer.toString(key)), depth);
		unhighlightAll();
		highlight(0);
		nextStep(t.translateMessage("insert", Integer.toString(key)), depth);

		message(t.translateMessage("assureFillLevel"), depth);
		highlight(1);
		nextStep(depth);

		// check if tables exceed fill level
		if (exceedsFillLevel()) {
			// temporarily buffer elements from tables
			Collection<Integer> bufferedElements = getAllElements();

			// create larger tables
			increaseTableSize(depth);
			message(t.translateMessage("fillLevelExceededIncrease", Integer.toString(tableSize)), depth);
			nextStep(t.translateMessage("increaseTableSizeTo", Integer.toString(tableSize)), depth);

			for (Integer i : bufferedElements) {
				insert(i, depth == null ? null : depth + 1);
			}
			createTables(depth);
			message(t.translateMessage("reinsertedAll"), depth);
			nextStep(t.translateMessage("reinsertionDone"), depth);
		}
		Integer[] oldTable1 = table1.clone();
		Integer[] oldTable2 = table2.clone();
		message(t.translateMessage("insert", Integer.toString(key)), depth);
		showInsertElement(key);
		unhighlight(1);
		nextStep(depth);

		int h1 = hash1(key);
		message(t.translateMessage("calculated", "h1", Integer.toString(key), Integer.toString(h1)), depth);
		highlightTableList(tableList1, h1, depth);
		highlight(2);
		nextStep(depth);

		if (table1[h1] == null || table1[h1].compareTo(key) == 0) {
			if (table1[h1] == null) {
				message(t.translateMessage("tableEmpty", "table1", Integer.toString(h1), Integer.toString(key)), depth);
			} else {
				message(t.translateMessage("tableContains", "table1", Integer.toString(h1), Integer.toString(key)),
						depth);
			}
			highlight(3);
			nextStep(depth);

			table1[h1] = key;
			showInsertElement(null);
			createTables(depth);
			message(t.translateMessage("insertionComplete"), depth);
			unhighlightAll();
			nextStep(depth);

			return true;
		}

		message(t.translateMessage("tryTable2"), depth);
		unhighlight(2);
		unhighlightTableList(tableList1, h1, depth);
		nextStep(depth);

		int h2 = hash2(key);
		message(t.translateMessage("calculated", "h2", Integer.toString(key), Integer.toString(h2)), depth);
		highlightTableList(tableList2, h2, depth);
		highlight(4);
		nextStep(depth);

		if (table2[h2] == null || table2[h2].compareTo(key) == 0) {
			if (table2[h2] == null) {
				message(t.translateMessage("tableEmpty", "table2", Integer.toString(h2), Integer.toString(key)), depth);
			} else {
				message(t.translateMessage("tableContains", "table2", Integer.toString(h2), Integer.toString(key)),
						depth);
			}
			highlight(5);
			nextStep(depth);

			table2[h2] = key;
			showInsertElement(null);
			createTables(depth);
			message(t.translateMessage("insertionComplete"), depth);
			unhighlightAll();
			nextStep(depth);

			return true;
		}

		// could not insert key so start loop trying to insert key.
		Integer current = key;
		message(t.translateMessage("neitherTableHasSpace"), depth);
		unhighlight(4);
		unhighlightTableList(tableList2, h2, depth);
		highlight(6);
		nextStep(depth);
		unhighlight(6);

		while (true) {
			highlight(7);
			current = handleSwap(current, hash1(current), table1, "left", tableList1, depth);

			if (current == null) {
				message(t.translateMessage("spaceInFirst"), depth);
				createTables(depth);
				highlight(9, 10);
				nextStep(depth);

				unhighlightAll();
				message(t.translateMessage("insertionComplete"), depth);
				nextStep(depth);

				return true;
			}
			message(t.translateMessage("noSpaceInFirst"), depth);
			unhighlight(8);
			nextStep(depth);

			current = handleSwap(current, hash2(current), table2, "right", tableList2, depth);
			if (current == null) {
				message(t.translateMessage("spaceInSecond"), depth);
				createTables(depth);
				highlight(12, 13);
				nextStep(depth);

				unhighlightAll();
				message(t.translateMessage("insertionComplete"), depth);
				nextStep(depth);

				return true;
			}
			message(t.translateMessage("noSpaceInSecond"), depth);
			unhighlight(11);
			nextStep(depth);

			message(t.translateMessage("checkIfChanged"), depth);
			highlight(14);
			nextStep(depth);

			if (tablesChanged(oldTable1, oldTable2)) {
				message(t.translateMessage("changed"), depth);

				nextStep(depth);

				unhighlight(14);
				continue;
			} else {
				message(t.translateMessage("notChanged"), depth);
				highlight(15);
				nextStep(depth);

				unhighlight(14, 15);
				break;
			}
		}

		message(t.translateMessage("needRehash"), depth);
		unhighlight(7);
		highlight(16);
		nextStep(t.translateMessage("rehashing"), depth);

		rehash(depth);
		message(t.translateMessage("rehashingDone"), depth);
		nextStep(t.translateMessage("rehashingDone"), depth);

		message(t.translateMessage("nowInsertRemaining", Integer.toString(current)), depth);

		unhighlight(16);
		highlight(17);
		nextStep(depth);

		insert(current, depth); // next step already called in insert()

		message(t.translateMessage("rehashingAndInsertionDone"), depth);
		unhighlightAll();
		nextStep(depth);
		return true;

	}

	private void unhighlightTableList(StringMatrix table, int h, Integer depth) {
		if (depth == null || depth > RECURSION_LEVEL_TO_DISPLAY)
			return;

		int pos = getRightPosition(table, h);
		table.unhighlightCell(pos, 0, null, null);
		table.unhighlightCell(pos, 1, null, null);
	}

	private void unhighlight(int... positions) {
		for (int i : positions) {
			code.unhighlight(i);
			for (int index = 0; index < codeHighlight.size(); index++) {
				if (index == i) {
					codeHighlight.remove(index);
				}
			}
		}
	}

	private void highlight(int... positions) {
		for (int i : positions) {
			code.highlight(i);
			codeHighlight.push(i);
		}
	}

	private void unhighlightAll() {
		while (codeHighlight.size() > 0) {
			code.unhighlight(codeHighlight.pop());
		}
	}

	private Integer handleSwap(Integer current, int hash, Integer[] table, String side, StringMatrix matrix,
			Integer depth) {
		int s = side == "left" ? 1 : 2;
		message(t.translateMessage("calculated", "h" + s, Integer.toString(current), Integer.toString(hash)), depth);
		highlightTableList(matrix, hash, depth);
		highlight(8 + (s - 1) * 3);
		nextStep(depth);

		String value = table[hash] == null ? "null" : Integer.toString(table[hash]);
		message(t.translateMessage("swap", Integer.toString(current), value), depth);
		highlightTableList(matrix, hash, depth);
		nextStep(depth);

		Integer tmp = current;
		current = table[hash];
		table[hash] = tmp;
		showInsertElement(current);
		createTables(depth);
		nextStep(depth);

		return current;
	}

	private void showInsertElement(Integer key) {
		int widthValues = maxWidth > 3 ? maxWidth : 3;
		String elem = key == null ? String.format("%" + widthValues + "s", "") : Integer.toString(key);

		if (intArray != null)
			intArray.hide();

		Offset offset = new Offset(200, 0, tableList1, AnimalScript.DIRECTION_N);

		intArray = lang.newStringArray(offset, new String[] { elem }, "InsertElement", null, arrayProps);
		intArray.showIndices(false, null, null);
		intArray.setBorderColor(0, borderColor, null, null);
	}

	private void highlightTableList(StringMatrix tableList, int h, Integer depth) {
		if (depth == null || depth > RECURSION_LEVEL_TO_DISPLAY)
			return;

		int pos = getRightPosition(tableList, h);
		tableList.highlightCellColumnRange(pos, 0, 1, null, null);

	}

	private int getRightPosition(StringMatrix tableList, int pos) {

		for (int i = 0; i < tableList.getNrRows(); i++) {
			String index = tableList.getElement(i, 0).trim();

			if (!index.equals("...") && Integer.parseInt(index) >= pos) {
				return Integer.parseInt(index) == pos ? i : i - 1;
			}
		}
		return tableList.getNrRows() - 1;
	}

	private boolean exceedsFillLevel() {
		double fillLevel1 = (double) countEntries(table1) / table1.length;
		double fillLevel2 = (double) countEntries(table2) / table2.length;
		return (fillLevel1 >= DESIRED_FILL_LEVEL || fillLevel2 >= DESIRED_FILL_LEVEL);
	}

	private long countEntries(Integer[] table) {
		return Arrays.stream(table).filter(x -> x != null).count();
	}

	private void increaseTableSize(Integer depth) {
		int oldSize = table1.length;
		// make sure that both tables have the same length
		if (oldSize != table2.length)
			throw new ArrayIndexOutOfBoundsException("Tables did not have equal length.");

		tableSize = calculateNewSize(oldSize);
		System.out.println("[i] Increase size to " + tableSize);
		table1 = new Integer[tableSize];
		table2 = new Integer[tableSize];

		createTables(depth);

	}

	private int calculateNewSize(int oldSize) {
		return (int) Math.floor((double) oldSize * FACTOR_SIZE_INCREASE);
	}

	private Collection<Integer> getAllElements() {
		Collection<Integer> all = new ArrayList<>();
		// remove null from tables and add to same collection
		all.addAll(Arrays.stream(table1).filter(x -> x != null).collect(Collectors.toList()));
		all.addAll(Arrays.stream(table2).filter(x -> x != null).collect(Collectors.toList()));
		return all;
	}

	private void rehash(int depth) {
		System.out.println("[i] Rehash");
		// temporarily save all elements
		Collection<Integer> bufferedElements = getAllElements();
		message(t.translateMessage("storeAll"), depth);
		highlight(19, 20);
		nextStep(depth);

		// shuffle hash functions to ensure different hashes
		selectNewRandomParametersForUniversalHashFunctions();
		message(t.translateMessage("defineNewHashfunctions"), depth);
		// create new empty tables
		table1 = new Integer[tableSize];
		table2 = new Integer[tableSize];
		createTables(depth);
		unhighlight(20);
		highlight(21, 22);
		nextStep(depth);

		message(t.translateMessage("insertStored"), depth);
		unhighlight(21, 22);
		highlight(23, 24);
		for (Integer i : bufferedElements) {
			insert(i, depth + 1);
		}
		createTables(depth);
		nextStep(depth);

		unhighlight(23, 24);
	}

	private void selectNewRandomParametersForUniversalHashFunctions() {
		// select new hash functions
		random1 = rng.nextInt();
		random2 = rng.nextInt();
	}

	private boolean tablesChanged(Integer[] oldTable1, Integer[] oldTable2) {
		for (int i = 0; i < tableSize; i++) {
			if (oldTable1[i] != table1[i] || oldTable2[i] != table2[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates source code object which contains the pseudo-code
	 */
	private void createSourceCode() {
		// put it on the right side of Cuckoo Hashing visualization
		Offset offset = new Offset(100, -10, tableList2, AnimalScript.DIRECTION_NE);

		code = lang.newSourceCode(offset, "sourceCode", null, sourceCodeProps);

		String[] lines = { "insert(table1, table2, h1, h2, value)", "	increaseSizeIfNecessary(table1, table2)",
				"	if table1[h1(value)] = null or table1[h1(value)] = value then",
				"		table1[h1(value)] <- value; return",
				"	if table2[h2(value)] = null or table2[h2(value)] = value then",
				"		table2[h2(value)] <- value; return", "	oldTable1 = table1; oldTable2 = table2",
				"	while true do", "		x <-> table1[h1(value)]", "		if x = null then", "			return",
				"		x <-> table2[h2(value)]", "		if x = null then", "			return",
				"		if oldTable1 = table1 and oldTable2 = table2 then", "			break",
				"	rehash(table1, table2, h1, h2)", "	insert(table1, table2, h1, h2, value)", "", "rehash()",
				"	bufferedElements := getAllElements(table1, table2)",
				"	h1 = new Hashfunction; h2 = new Hashfunction", "	clear(table1, table2)",
				"	for all x in bufferedElements do", "		insert(table1, table2, h1, h2, x)" };

		// add lines to a source code object
		for (String line : lines) {
			// as it does not display tabs initially, count them and use "indentation"
			int tabs = 0;
			for (char c : line.toCharArray()) {
				if ("\t".equals(c + "")) {
					tabs++;
				}
			}
			code.addCodeLine(line, null, tabs, null);
		}
		codeHighlight = new Stack<Integer>();
	}

	/**
	 * Creates a header within a rectangle as headline for the animation
	 */
	private List<Primitive> createHeader() {
		headerTextProps = new TextProperties();
		headerTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));

		RectProperties headerRectProps = new RectProperties();
		headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		header = lang.newText(new Coordinates(22, 20), t.translateMessage("headerText"), "header", null,
				headerTextProps);

		headerRect = lang.newRect(new Offset(-10, -5, header.getName(), AnimalScript.DIRECTION_NW),
				new Offset(10, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null, headerRectProps);

		List<Primitive> headerPrim = new LinkedList<>();
		headerPrim.add(header);
		headerPrim.add(headerRect);

		return headerPrim;
	}

	/**
	 * Creates and places a message placeholder at the top of the viewport. Consider
	 * elements to be inserted to avoid overlapping.
	 */
	private void createMessage() {
		message = lang.newText(new Offset(50, -7, header, AnimalScript.DIRECTION_E), "", "messageText", null,
				textProps);
	}

	/**
	 * Creates introduction slide with algorithm description
	 */
	private void createIntroSlide() {
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc = lang.newSourceCode(new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "intro", null, scp);

		// split the description at \n to put it into separate lines
		for (String introLine : t.translateMessage("algoDescription").split("\n")) {
			sc.addCodeLine(introLine.replace("quot;", "'"), "intro", 0, null);
		}

		lang.nextStep(t.translateMessage("introduction"));
		sc.hide();
	}

	/**
	 * Creates conclusion slide
	 */
	private void createConclusionSlide() {
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc = lang.newSourceCode(new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "conclusion", null,
				scp);

		// split the conclusion at \n to put it into separate lines
		for (String ConclusionLine : t.translateMessage("algoConclusion").split("\n")) {
			sc.addCodeLine(ConclusionLine.replace("quot;", "'"), "conclusion", 0, null);
		}

		lang.nextStep(t.translateMessage("conclusion"));
		sc.hide();
	}

	/**
	 * Start the next step if performInBackground is false
	 */
	private void nextStep(String message, Integer depth) {
		if (depth != null && depth <= RECURSION_LEVEL_TO_DISPLAY) {
			String padding = "";
			for (int i = 0; i < depth * 6; i++) {
				padding += " ";
			}
			lang.nextStep(padding + message);
		}
	}

	private void nextStep(Integer depth) {
		if (depth != null && depth <= RECURSION_LEVEL_TO_DISPLAY) {
			lang.nextStep();
		}
	}

	private int hash1(int value) {
		return universalHash(random1, value);
	}

	private int hash2(int value) {
		return universalHash(random2, value);
	}

	private int h1(int value) {
		return value % tableSize;
	}

	private int h2(int value) {
		return (int) Math.floor((double) value / tableSize) % tableSize;
	}

	private int universalHash(int rdm, int value) {
		int result = (h1(value) + rdm * h2(value)) % tableSize;
		return (result + tableSize) % tableSize;

	}

	/**
	 * Sets the message to display
	 *
	 * @param messageText the message to display
	 */
	private void message(String messageText, Integer depth) {
		if (depth != null && depth <= RECURSION_LEVEL_TO_DISPLAY)
			message.setText(messageText, null, null);
	}

}
