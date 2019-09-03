package generators.hashing;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class HashingLinear extends AnnotatedAlgorithm {

	protected TextProperties headerProps;
	protected TextProperties remarkProps;
	protected int m;
	protected MatrixProperties matrixProps;
	protected String[][] matrix;
	protected int[] insertions;
	protected TextProperties insertionsProps;
	protected Color highlightColor;
	protected SourceCodeProperties sourceCodeProps;
	protected TextProperties variablesProps;
	protected Text[] textEntries;
	protected Text[] textMovingEntries;
	protected StringMatrix animMatrix;
	protected int cellWidth;
	protected int cellHeight;
	protected Text textEntry;
	protected Text textI;
	protected Text textIndex;
	protected Timing timing1 = new TicksTiming(15);
	protected Timing timing2 = new TicksTiming(50);
	protected Timing timing3 = new TicksTiming(65);
	protected String ALGORITHM_NAME = "Hashing with linear probing";
	protected String NAME = "Hashing with linear probing";

	protected final String ANIMATION_AUTHOR =
			"Max Bank <max_bank@rbg.informatik.tu-darmstadt.de>";

	protected final String ANNOTATED_SOURCE_CODE =
		  "public void insert(int entry) {		@label(\"header\") @openContext\n"
		+ "   int i = 0;						@label(\"decI\") @declare(\"int\", \"i\", \"0\")\n"
		+ "   int index;						@label(\"decIndex\") @declare(\"int\", \"index\", \"0\")\n"
		+ "   do {								@label(\"do\")\n"
		+ "      index = (entry + i) % m;		@label(\"calcIndex\")\n"
		+ "      i++;							@label(\"incI\") @inc(\"i\")\n"
		+ "   } while (table[index] != null);	@label(\"while\")\n"
		+ "   table[index] = entry;				@label(\"insert\")\n"
		+ "}									@label(\"end\") @closeContext\n";

	@Override
	public String getAnnotatedSrc() {
		return ANNOTATED_SOURCE_CODE;
	}

	protected final String DESCRIPTION = "A hash table is a data structure that "
			+ "under optimal conditions provides insertion, finding and "
			+ "deletion of entries at constant costs. Therefore a so called "
			+ "hash function is used to calculate an array index from the "
			+ "entry to be inserted. Then the entry is inserted into an array "
			+ "with length m at this position. The entry can now be accessed "
			+ "or deleted by again using the hash function to get the array "
			+ "index.\n"
			+ "Ideally the hash function should always distribute different "
			+ "entries to different array positions, but as the array length is"
			+ " usually less then the number of possible different entries, so "
			+ "called collisions will occur, i.e. different entries will be "
			+ "mapped to the same array position. In this case a mechanism is "
			+ "needed to determine an alternative array position.\n"
			+ "In this visualization, the very simple modulo function is used "
			+ "as the hash function. Collisions are resolved by linear probing "
			+ "with step size 1, i.e. if the original slot is already used, the"
			+ " index is increased by steps of 1 until a free position is "
			+ "found.";

	protected final String REMARK = "Note: Management of the hashtable's size is "
			+ "not part of this visualization!";

	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		headerProps = (TextProperties) arg0.getPropertiesByName("header style");
		float headerSize = (Integer) arg1.get("header size");
		Font headerFont = (Font) headerProps
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont
				.deriveFont(Font.BOLD, headerSize));
		remarkProps = (TextProperties) arg0.getPropertiesByName("remark style");
		float remarkSize = (Integer) arg1.get("remark size");
		Font remarkFont = (Font) remarkProps
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		remarkProps.set(AnimationPropertiesKeys.FONT_PROPERTY, remarkFont
				.deriveFont(remarkSize));
		m = (Integer) arg1.get("hash table size");
		matrixProps = (MatrixProperties) arg0
				.getPropertiesByName("hash table style");
		insertions = (int[]) arg1.get("insertions");
		insertionsProps = (TextProperties) arg0
				.getPropertiesByName("insertions style");
		float insertionsSize = (Integer) arg1.get("insertions size");
		Font insertionsFont = (Font) insertionsProps
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		insertionsProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				insertionsFont.deriveFont(insertionsSize));
		highlightColor = (Color) arg1.get("highlight color");
		sourceCodeProps = (SourceCodeProperties) arg0
				.getPropertiesByName("source code style");
		variablesProps = (TextProperties) arg0
				.getPropertiesByName("variables style");
		float variablesSize = (Integer) arg1.get("variables size");
		Font variablesFont = (Font) variablesProps
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		variablesProps.set(AnimationPropertiesKeys.FONT_PROPERTY, variablesFont
				.deriveFont(Font.BOLD, variablesSize));

		myInit();

		for (int i = 0; i < insertions.length; i++) {
			insert(i);
		}

		return lang.toString();
	}

	@Override
	    public void init() {}
	public void myInit() {
		super.init();
		
		lang.newText(new Coordinates(20, 30), getAlgorithmName(),
				"header", null, headerProps);

		lang.newText(new Offset(0, 10, "header", AnimalScript.DIRECTION_SW),
						REMARK, "remark", null, remarkProps);

		matrix = new String[2][m];
		for (int i = 0; i < m; i++) {
			matrix[0][i] = "";
			matrix[1][i] = String.format("%2d", i);
		}

		animMatrix = lang.newStringMatrix(new Offset(0, 20, "remark",
						AnimalScript.DIRECTION_SW), matrix, "matrix", null,
						matrixProps);

		cellWidth = 28;
		cellHeight = 30;
		int vertSpace = 15;
		Node[] endpoints = new Node[2];
		endpoints[0] = new Offset(-2, vertSpace, "remark",
				AnimalScript.DIRECTION_SW);
		endpoints[1] = new Offset(m * cellWidth - 2, vertSpace, "remark",
				AnimalScript.DIRECTION_SW);
		lang.newPolyline(endpoints, "tableBorderTop", null);
		endpoints[0] = new Offset(-2, vertSpace + cellHeight, "remark",
				AnimalScript.DIRECTION_SW);
		endpoints[1] = new Offset(m * cellWidth - 2, vertSpace + cellHeight,
				"remark", AnimalScript.DIRECTION_SW);
		lang.newPolyline(endpoints, "tableBorderBottom", null);
		endpoints[0] = new Offset(-2, vertSpace, "remark",
				AnimalScript.DIRECTION_SW);
		endpoints[1] = new Offset(-2, vertSpace + cellHeight, "remark",
				AnimalScript.DIRECTION_SW);
		lang.newPolyline(endpoints, "tableBorderVert0", null);
		for (int i = 1; i <= m; i++) {
			endpoints[0] = new Offset(i * cellWidth - 2, vertSpace, "remark",
					AnimalScript.DIRECTION_SW);
			endpoints[1] = new Offset(i * cellWidth - 2,
					vertSpace + cellHeight, "remark",
					AnimalScript.DIRECTION_SW);
			lang.newPolyline(endpoints, String.format("tableBorderVert%d", i),
					null);
		}

		lang.newText(new Offset(0, 40, "matrix", AnimalScript.DIRECTION_SW),
				"Insert:", "insert", null, insertionsProps);
		textEntries = new Text[insertions.length];
		textMovingEntries = new Text[insertions.length];
		textEntries[0] = lang.newText(new Offset(20, 0, "insert",
				AnimalScript.DIRECTION_NE), String.valueOf(insertions[0]),
				"entry0", null, insertionsProps);
		textMovingEntries[0] = lang.newText(new Offset(20, 0, "insert",
				AnimalScript.DIRECTION_NE), String.valueOf(insertions[0]),
				"movingEntry0", null, insertionsProps);
		textMovingEntries[0].hide();
		textMovingEntries[0].changeColor(
				AnimationPropertiesKeys.COLOR_PROPERTY,
				highlightColor, null, null);
		for (int i = 1; i < insertions.length; i++) {
			textEntries[i] = lang.newText(new Offset(10, 0, String.format(
					"entry%d", i - 1), AnimalScript.DIRECTION_NE), String
					.valueOf(insertions[i]), String.format("entry%d", i), null,
					insertionsProps);
			textMovingEntries[i] = lang.newText(new Offset(10, 0, String
					.format("entry%d", i - 1), AnimalScript.DIRECTION_NE),
					String.valueOf(insertions[i]), String.format("entry%d", i),
					null, insertionsProps);
			textMovingEntries[i].hide();
			textMovingEntries[i].changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY,
					highlightColor, null, null);
		}
		sourceCode = lang.newSourceCode(new Offset(0, 30, "insert",
				AnimalScript.DIRECTION_SW), "sourceCode", null,
				sourceCodeProps);

		prepareVariablesDisplay();

		lang.nextStep();
		
		parse();
	}

	protected void prepareVariablesDisplay() {
		vars.declare("int", "m", String.valueOf(m));
		vars.setGlobal("m");
		Text textM = lang.newText(new Offset(230, 70, "insert",
				AnimalScript.DIRECTION_NE),
				String.format("m = %d", m), "textM", null, variablesProps);
		textM.show();
		textEntry = lang.newText(new Offset(0, 25, "textM",
				AnimalScript.DIRECTION_NW), "entry =", "textEntry", null,
				variablesProps);
		textI = lang.newText(new Offset(0, 50, "textM",
				AnimalScript.DIRECTION_NW), "i =", "textI", null,
				variablesProps);
		textIndex = lang.newText(new Offset(0, 75, "textM",
				AnimalScript.DIRECTION_NW), "index =", "textAddress", null,
				variablesProps);
	}

	protected void insert(int entryIndex) {
		int entry = insertions[entryIndex];
		exec("header");
		vars.declare("int", "entry", String.valueOf(entry));
		TextUpdater entryUp = new TextUpdater(textEntry);
		entryUp.addToken("entry = ");
		entryUp.addToken(vars.getVariable("entry"));
		entryUp.update();
		textEntry.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				highlightColor, null, null);
		textEntry.show(timing2);
		textEntries[entryIndex].changeColor(
				AnimationPropertiesKeys.COLOR_PROPERTY, highlightColor, null,
				null);
		lang.nextStep();
		textEntry.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK, null, null);

		exec("decI");
		int i = 0;
		TextUpdater iUp = new TextUpdater(textI);
		iUp.addToken("i = ");
		iUp.addToken(vars.getVariable("i"));
		iUp.update();
		textI.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				highlightColor, null, null);
		textI.show(timing2);
		lang.nextStep();
		textI.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK,
				null, null);

		exec("decIndex");
		int index;
		TextUpdater indexUp = new TextUpdater(textIndex);
		indexUp.addToken("index = ");
		indexUp.addToken(vars.getVariable("index"));
		indexUp.update();
		textIndex.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				highlightColor, null, null);
		textIndex.show(timing2);
		lang.nextStep();
		textIndex.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK, null, null);

		do {
			exec("calcIndex");
			index = calcIndex(entry, i);
			vars.set("index", String.valueOf(index));
			textIndex.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					highlightColor, timing2, null);
			lang.nextStep();
			textIndex.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color.BLACK, null, null);

			exec("incI");
			i++;
			textI.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					highlightColor, timing2, null);
			lang.nextStep();
			textI.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color.BLACK, null, null);

			exec("while");
			animMatrix.highlightCell(1, index, timing2, null);
			lang.nextStep();
			animMatrix.unhighlightCellRowRange(0, 1, index, null, null);
		} while (!matrix[0][index].isEmpty());

		exec("insert");
		textMovingEntries[entryIndex].show();
		try {
			textMovingEntries[entryIndex].moveTo(null, "translate", new Offset(
					index * cellWidth + 5, 340, animMatrix,
					AnimalScript.DIRECTION_NW), timing2, timing1);
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		textMovingEntries[entryIndex].hide(timing3);
		animMatrix.put(0, index, String.format("%2d", entry), timing3, null);
		animMatrix.highlightCell(0, index, timing2, null);
		animMatrix.highlightCell(1, index, null, null);
		lang.nextStep();
		animMatrix.unhighlightCellRowRange(0, 1, index, null, null);
		textEntries[entryIndex]
				.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
						Color.BLACK, null, null);
		textI.hide();
		textIndex.hide();
	}
	
	protected int calcIndex(int entry, int i) {
		return (entry + i) % m;
	}

	@Override
	public String getAlgorithmName() {
		return ALGORITHM_NAME;
	}

	@Override
	public String getAnimationAuthor() {
		return ANIMATION_AUTHOR;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
//				| GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

}
