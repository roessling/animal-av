package generators.hashing;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.util.Offset;

public class HashingLinearAlt extends HashingLinear {

	protected String NAME = "Hashing with alternating linear probing";

	protected final String ANNOTATED_SOURCE_CODE =
			"public void insert(int entry)									@label(\"header\") @openContext\n"
				+ "   int i = 0;											@label(\"decI\") @declare(\"int\", \"i\", \"0\")\n"
				+ "   int index;											@label(\"decIndex\") @declare(\"int\", \"index\", \"0\")\n"
				+ "   do {													@label(\"do\")\n"
				+ "      index = (entry + i * (int) Math.pow(-1, i) % m;	@label(\"calcIndex\")\n"
				+ "      i++;												@label(\"incI\") @inc(\"i\")\n"
				+ "   } while (table[index] != null);						@label(\"while\")\n"
				+ "   table[index] = entry;									@label(\"insert\")\n"
				+ "}														@label(\"end\") @closeContext\n";
	
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
			+ "as the hash function. Collisions are resolved by alternating "
			+ "linear probing with step size 1, i.e. if the original slot is "
			+ "already used, the index is decreased by 1 and this slot is "
			+ "inspected. If it is already used too, the original slot is "
			+ "increased by 2, then again decreased by 3 and so on, until a "
			+ "free position is found.";

	protected void prepareVariablesDisplay() {
		vars.declare("int", "m", String.valueOf(m));
		vars.setGlobal("m");
		Text textM = lang.newText(new Offset(390, 70, "insert",
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
	
	@Override
	protected int calcIndex(int entry, int i) {
		return (entry + i * (int) Math.pow(-1, i)) % m;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

}
