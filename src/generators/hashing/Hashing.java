package generators.hashing;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.HashTableImpl;
import generators.helpers.HashTableImpl.Block;
import generators.helpers.MultilineText;
import generators.helpers.OffsetCoords;
import generators.helpers.RelativeText;
import generators.helpers.RelativeTextImpl;
import generators.helpers.TextUtil;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.SourceCode;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;


/**
 * @author Kamil Erhard, Dirk Kröhan
 * 
 */
public class Hashing extends AnnotatedAlgorithm implements Generator, HashTableImpl.CodeObserver {

	public Hashing() {
		super();
	}

	private static final String DESCRIPTION = "Beim Hashverfahren werden die Zieldaten in einer Hashtabelle gespeichert. Eine Hashfunktion berechnet zu jedem Datenobjekt einen Hashwert, der als Index in der Tabelle verwendet wird. Zum Berechnen dieses Hashwertes wird ein Schlüssel benötigt, der dieses Objekt eindeutig identifiziert. Dieser Schlüssel wird von der Hashfunktion zum Berechnen des Hashwertes verwendet. Das Datenobjekt wird an einer durch den Hashwert festgelegten Stelle (Bucket genannt) in der Tabelle gespeichert. Hashfunktionen müssen jedoch nicht eindeutig sein, so dass sich in der Praxis mehrere Objekte einen Bucket teilen müssen. Diesen Fall nennt man Kollision, er benötigt eine spezielle Behandlung durch das Verfahren.Da Hash-Funktionen im Allgemeinen nicht injektiv sind, können zwei unterschiedliche Schlüssel zum selben Hash-Wert, also zum selben Feld in der Tabelle führen. Dieses Ereignis wird als Kollision bezeichnet. In diesem Fall muss die Hashtabelle mehrere Werte an derselben Stelle aufnehmen. Um dieses Problem zu handhaben, gibt es diverse Kollisionsauflösungsstrategien.\n"
		+ "<br><br>\n"
		+ "Bei der quadratisch alternierenden Sondierung wird bei einer Kollisison nach einem neuen freien Speicher gesucht, allerdings nicht sequenziell, wie bei der linearen Sondierung,sondern mit stetig quadratisch wachsender Schrittweite und in beide Richtungen. Verursacht h(k) eine Kollision, so werden nacheinander h(k) + 1,h(k) - 1,h(k) + 4,h(k) - 4,h(k) + 9 usw. probiert.\n"
		+ "<br>\n"
		+ "Wählt man die Anzahl der Behälter geschickt (nämlich , m ist Primzahl), so erzeugt jede Sondierungsfolge h0(x) bis hm - 1(x) eine Permutation der Zahlen 0 bis m - 1; so wird also sichergestellt, dass jeder Behälter getroffen wird.\n"
		+ "<br><br>\n"
		+ "Quadratisches Sondieren ergibt keine Verbesserung bei Primärkollisionen h0(x) = h0(y), kann aber die Wahrscheinlichkeit der Bildung von längeren Ketten bei Sekundärkollisionen h0(x) = hk(y) herabsetzen, d. h. Clusterbildung wird vermieden.\n"
		+ "<br><br>\n"
		+ "Hat die Tabelle einen gewissen Füllgrad überschritten, wird sie zwangsläufig entarten. Dann kann nur eine Vergrößerung der Tabelle mit nachfolgender Restrukturierung wieder zu akzeptablem Laufzeitverhalten führen.";

	private static final String SOURCE_CODE = "public void store(int key, String value) {\n"
		+ "\tint homeposition = hash(key);\n" + "\tint sig = -1, k = 0, pos = homeposition;\n"
		+ "\twhile (hashTable[pos] != null) {\n" + "\t\tsig *= -1;\n" + "\t\tif (sig == 1) k++;\n"
		+ "\t\tpos = hash(homeposition + sig * k²);\n" + "\t}\n" + "\thashTable[pos] = value;\n"
		+ "\tnumElements++;\n" + "\tif (mustGrow()) {\n" + "\t\tgrow();\n" + "\t}\n" + "}";

	private final String[] helpTextArray = new String[8];
	private final SourceCode[] sc = new SourceCode[5];

	private int insertedElements = -1;
	private int lastHighlightedHelpText = -1;

	private ArrayList<RelativeText> hashTableFields, dataTable, helpText;

	private RelativeText hashText, sigText, kText, currentElementText;

	private HashTableImpl sht;
	private AnimalTextGenerator atg;

	private Color helpTextHighLightColor, elementHighLightColor;

	private TextProperties dataTextProps, helpTextProps, hashFunctionTextProps;

	private Boolean isSourceCodeHidden;

	private HashTableImpl.Block lastShownSourceCodeBlock;

	private void highlightHelpText(int index, String text) {

		if (this.lastHighlightedHelpText != -1) {
			this.helpText.get(this.lastHighlightedHelpText).changeColor(AnimalScript.COLORCHANGE_COLOR, (Color) this.helpTextProps.get("color"),
				null, null);
		}

		this.helpText.get(index).changeColor(AnimalScript.COLORCHANGE_COLOR, this.helpTextHighLightColor, null, null);
		if (text != null) {
			this.helpText.get(index).setText(this.helpTextArray[index].replace("##", text), null, null);
		}

		this.lastHighlightedHelpText = index;
	}

	@Override
	public void exec(String label) {
		super.exec(label);
	}

	public void initHashing(int hashtableSize, double hashTableLoadFactor, String[] dataElements, int[] dataKeys) {
		this.vars.declare("int", "hashTableSize", String.valueOf(this.sht.hashTable.length));
		this.vars.declare("int", "hashTableLoadFactor", String.valueOf(this.sht.hashTableLoadFactor));
		this.vars.declare("int", "numElements", "0");
		this.vars.declare("int", "data.length", Integer.toString(dataElements.length));

		this.sht.hashing(dataElements, dataKeys);
	}

	private void animateTableGrowing() {
		Coordinates c = new Coordinates(20, 20);

		for (int i = 0; i < this.insertedElements; i++) {

			try {
				this.dataTable.get(i).moveTo(AnimalScript.DIRECTION_E, "translate", c, null, new MsTiming(1000));
			} catch (IllegalDirectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String element = this.dataTable.get(i).getText();
			this.dataTable.get(i).setText(element, new MsTiming(1000), new MsTiming(300));
			c = new OffsetCoords(c, 0, 25);
		}

		this.growHashTableTexts(this.dataTextProps);
		this.moveSourceCode();
	}

	private void growHashTableTexts(TextProperties t) {
		int oldSize = this.hashTableFields.size();

		for (int i=0; i<oldSize; i++) {
			this.vars.set("hashTable-"+i, "null");
		}

		this.buildHashTableTexts(t, oldSize);
	}

	private void moveSourceCode() {
		try {
			OffsetCoords offsetCoords = new OffsetCoords(this.hashTableFields.get(this.sht.getSize() - 1).getUpperLeft(), 0,
				50);
			for (int i=0; i<5; i++) {
				this.sc[i].moveTo(AnimalScript.DIRECTION_S, "translate", offsetCoords, null, null);
			}
		} catch (IllegalDirectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String buildHashFunctionText(int key, int k, int sig, int result) {
		StringBuilder sb = new StringBuilder();
		sb.append("( ").append(key).append(" + ").append("sig").append(" * ").append("k²").append(") mod ").append(
			this.sht.getSize()).append(" = ").append(result);
		return sb.toString();
	}

	private void buildDataTableTexts(TextProperties t, String[] dataElements, int[] dataKeys) {

		this.dataTable = new ArrayList<RelativeText>(dataElements.length);
		Coordinates c = new Coordinates(20, 20);
		for (int i = 0; i < dataElements.length; i++) {
			this.dataTable.add(new RelativeTextImpl(this.atg, new OffsetCoords(c, 0, i * 25), dataKeys[i] + " : "
				+ dataElements[i], "data" + i, null, t));
		}
	}

	private void buildHashTableTexts(TextProperties t, int startIndex) {
		Coordinates c = new Coordinates(500, 20);

		for (int i = startIndex; i < this.sht.getSize(); i++) {
			this.hashTableFields.add(new RelativeTextImpl(this.atg, new OffsetCoords(c, 0, i * 25),
				String.valueOf(i) + " : ", "hashTable" + i, null, t));

			this.vars.declare("String", "hashTable-"+i+"", "null");
		}
	}

	private void buildHashFunctionTexts(TextProperties t) {

		this.hashText = new RelativeTextImpl(new AnimalTextGenerator(this.lang), new Coordinates(200, 100), "(KEY mod "
			+ this.sht.getSize() + ")", "hashText", null, t);

		this.kText = new RelativeTextImpl(new AnimalTextGenerator(this.lang), new OffsetCoords(
			this.hashText.getUpperLeft(), 0, 20), "", "k", null, t);
		this.kText.hide();

		this.sigText = new RelativeTextImpl(new AnimalTextGenerator(this.lang), new OffsetCoords(this.kText.getUpperLeft(), 0,
			20), "", "sig", null, t);
		this.sigText.hide();
	}

	private void buildHelpText(TextProperties t) {

		this.helpTextArray[0] = "1. Prüfe ob Füllgrad (##%) der Hashtabelle erreicht ist.";
		this.helpTextArray[1] = "$T1.1 Falls ja, Hashtabelle wird geleert, vergrößert und Objekte$N$Tmüssen mit der neuen Hashtabellengröße neu gehasht werden.";
		this.helpTextArray[2] = "2. Zu speicherndes Objekt (##)$Nwird ausgewählt";
		this.helpTextArray[3] = "3. Berechne Hausposition des Objektschlüssels (##)$Nmit Hilfe der Hashfunktion";
		this.helpTextArray[4] = "4. Prüfe ob Hausposition (##) in der Hashtabelle$Nnoch frei ist";
		this.helpTextArray[5] = "$T4.1 Falls frei, dann speichere Objekt (##)";
		this.helpTextArray[6] = "$T4.2 Falls belegt, berechne neuen Hashwert durch$N$Tquadratisch alternernierende Sondierung der Hausposition";
		this.helpTextArray[7] = "$T$T4.2.1 Prüfe neu berechnete Position (##)";

		this.helpText = new ArrayList<RelativeText>();
		Node oc = new OffsetCoords(this.dataTable.get(this.dataTable.size() - 1).getUpperLeft(), 0, 50);

		MultilineText mc = null;

		for (int i=0; i<this.helpTextArray.length; i++) {
			mc = new MultilineText(new AnimalTextGenerator(this.lang), new OffsetCoords(oc, 0, (mc == null ? 0 : mc
					.getHeight()) + 20), this.helpTextArray[i], "helpText"+i, t, 15);
			this.helpText.add(mc);
			oc = mc.getUpperLeft();
		}
	}

	private void buildSourceCode(SourceCodeProperties scProps) {
		Node oc = new OffsetCoords(this.hashTableFields.get(this.sht.getSize() - 1).getUpperLeft(), 0, 50);

		for (int i=0; i<5; i++) {
			this.sc[i] = this.lang.newSourceCode(oc, "sc"+i, null, scProps);
		}

		for (int i=0; i<5; i++) {
			this.sourceCode = this.sc[i];
			this.lastShownSourceCodeBlock = HashTableImpl.Block.getByOrdinal(i);
			this.parse();
			this.sc[i].hide();
		}

		this.lastShownSourceCodeBlock = null;
	}

	public void showSourceCodeBlock(Block codeBlock) {

		if (this.isSourceCodeHidden) {
			return;
		}

		if (this.lastShownSourceCodeBlock != null) {
			this.exec(this.lastShownSourceCodeBlock.name()+"_"+codeBlock.name());
			this.lang.nextStep();
			this.sc[this.lastShownSourceCodeBlock.ordinal()].hide();
		}

		this.sc[codeBlock.ordinal()].show();
		this.sourceCode = this.sc[codeBlock.ordinal()];
		this.lastShownSourceCodeBlock = codeBlock;
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		this.dataTextProps = (TextProperties) props.getPropertiesByName("dataText");
		this.helpTextProps = (TextProperties) props.getPropertiesByName("helpText");
		this.hashFunctionTextProps = (TextProperties) props.getPropertiesByName("hashFunctionText");
		SourceCodeProperties sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		this.isSourceCodeHidden = (Boolean) sourceCodeProps.get("hidden");
		String[] dataElements = (String[]) primitives.get("dataElements");
		int[] dataKeys = (int[]) primitives.get("dataKeys");

		if (dataElements.length != dataKeys.length) {
			throw new IllegalArgumentException("Number of elements and keys must be equal!");
		}

		TextUtil.setFont((Font) this.dataTextProps.get("font"));
		int hashTableSize = (Integer) primitives.get("initialHashTableSize");
		double hashTableLoadFactor = (Double) primitives.get("hashTableLoadFactor");
		this.elementHighLightColor = (Color) primitives.get("elementHighLightColor");
		this.helpTextHighLightColor = (Color) primitives.get("helpTextHighLightColor");

		TextUtil.hashMap.clear();

		this.sht = new HashTableImpl(hashTableSize, hashTableLoadFactor, this);

		this.init();

		this.atg = new AnimalTextGenerator(this.lang);
		this.hashTableFields = new ArrayList<RelativeText>(hashTableSize);
		this.buildDataTableTexts(this.dataTextProps, dataElements, dataKeys);
		this.buildHelpText(this.helpTextProps);
		this.buildHashTableTexts(this.dataTextProps, 0);
		this.buildHashFunctionTexts(this.hashFunctionTextProps);
		this.buildSourceCode(sourceCodeProps);

		this.initHashing(hashTableSize, hashTableLoadFactor, dataElements, dataKeys);

		return this.lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Hashing mit alternierend quadratischer Sondierung";
	}

	@Override
	public String getAnimationAuthor() {
		return "Kamil Erhard, Dirk Kröhan";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public String getCodeExample() {
		return SOURCE_CODE;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getName() {
		return "Hashing mit alterneriend quadratischer Sondierung";
	}

	@Override
	public void init() {
		super.init();
	}

	private final static String[] codeBlock = {
		"public void hashing(String[][] data) {" + "@label(\"HASHING1\") @declare(\"int\", \"i\", \"-1\") \n" +
		" for (int i=0; i<data.length; i++) {" + "@label(\"HASHING2\") @inc(\"i\") \n" +
		"  if (mustGrow()) {" + "@label(\"HASHING3\") \n" +
		"   grow();" + "@label(\"HASHING4\") \n" +
		"   i = -1;" + "@label(\"HASHING5\") @set(\"i\", \"-1\") \n" +
		"   continue;" + "@label(\"HASHING6\") \n" +
		"  }" + "@label(\"HASHING7\") \n" +
		"  store(Integer.parseInt(data[i][0]), data[i][1]);" + "@label(\"HASHING8\") \n" +
		" }" + "@label(\"HASHING9\") \n" +
		"}" + "@label(\"HASHING10\") @discard(\"i\") \n" +
		"\n" +
		"public void store(int theKey, String theObject) {...}" + "@label(\"HASHING_STORE\") @declare(\"String\", \"theKey\") @declare(\"String\", \"theObject\") \n" +
		"public int hash(int theKey) {...}" + "@label(\"HASHING_HASH\") \n" +
		"public void mustGrow() {...}" + "@label(\"HASHING_MUSTGROW\") \n" +
		"public void grow() {...}" + "@label(\"HASHING_GROW\")",

		"public void hashing(String[][] data) {...}" + "@label(\"STORE_HASHING\") \n" +
		"\n" +
		"public void store(int theKey, String theObject) {" + "@label(\"STORE1\") \n" +
		" int homePosition = hash(theKey);" + "@label(\"STORE2\") @declare(\"int\", \"homePosition\") \n" +
		" int pos = homePosition;" + "@label(\"STORE3\") @declare(\"int\", \"pos\") \n" +
		" if (hashTable[homePosition] != null) {" + "@label(\"STORE4\") \n" +
		"  int sig = -1, k = 1;" + "@label(\"STORE5\") @declare(\"int\", \"sig\", \"-1\") @declare(\"int\", \"k\", \"1\") \n" +
		"  do {" + "@label(\"STORE6\") \n" +
		"   sig *= -1;" + "@label(\"STORE7\") \n" +
		"   k++;" + "@label(\"STORE8\") @inc(\"k\") \n" +
		"   pos = hash(homeposition + sig * (k/2)²);" + "@label(\"STORE9\") \n" +
		"  } while (hashTable[pos] != null);" + "@label(\"STORE10\") \n" +
		" }" + "@label(\"STORE11\") @discard(\"k\") @discard(\"sig\") \n" +
		" hashTable[pos] = theObject;" + "@label(\"STORE12\")\n" +
		" numElements++;" + "@label(\"STORE13\") @inc(\"numElements\") \n" +
		"}" + "@label(\"STORE14\") @discard(\"pos\") @discard(\"homePosition\") @discard(\"theKey\") @discard(\"theObject\") \n" +
		"\n" +
		"public int hash(int theKey) {...}" + "@label(\"STORE_HASH\") \n" +
		"public void mustGrow() {...}" + "@label(\"STORE_MUSTGROW\") \n" +
		"public void grow() {...}" + "@label(\"STORE_GROW\")",

		"public void hashing(String[][] data) {...}" + "@label(\"HASH_HASHING\") \n" +
		"public void store(int theKey, String theObject) {...}" + "@label(\"HASH_STORE\") \n" +
		"\n" +
		"public int hash(int theKey) {" + "@label(\"HASH1\") \n" +
		" return key - Math.floor(theKey / hashTableSize) * hashTableSize;" + "@label(\"HASH2\") \n" +
		"}" + "@label(\"HASH3\") \n" +
		"" + "@label(\"HASH4\") \n" +
		"\n" +
		"public void mustGrow() {...}" + "@label(\"HASH_MUSTGROW\") \n" +
		"public void grow() {...}" + "@label(\"HASH_GROW\")",

		"public void hashing(String[][] data) {...}" + "@label(\"MUSTGROW_HASHING\") \n" +
		"public void store(int theKey, String theObject) {...}" + "@label(\"MUSTGROW_STORE\") \n" +
		"public int hash(int theKey) {...}" + "@label(\"MUSTGROW_HASH\") \n" +
		"\n" +
		"private boolean mustGrow() {" + "@label(\"MUSTGROW1\") \n" +
		" return numElements >= hashTableSize * hashTableLoadFactor;" + "@label(\"MUSTGROW2\") \n" +
		"}" + "@label(\"MUSTGROW3\") @closeContext \n" +
		"\n" +
		"public void grow() {...}" + "@label(\"MUSTGROW_GROW\") ",

		"public void hashing(String[][] data) {...}" + "@label(\"GROW_HASHING\")  \n" +
		"public void store(int theKey, String theObject) {...}" + "@label(\"GROW_STORE\") \n" +
		"public int hash(int theKey) {...}" + "@label(\"GROW_HASH\") \n" +
		"private boolean mustGrow() {...}" + "@label(\"GROW_MUSTGROW\") \n" +
		"\n" +
		"private void grow() {" + "@label(\"GROW1\") \n" +
		" hashTableSize = getNextPrime(hashTableSize + 1);" + "@label(\"GROW2\") \n" +
		" numElements = 0;" + "@label(\"GROW3\") @set(\"numElements\", \"0\") \n" +
		" hashTable = new String[hashTableSize];" + "@label(\"GROW4\") \n" +
		"}" + "@label(\"GROW5\") \n"
	};

	@Override
	public String getAnnotatedSrc() {
		return codeBlock[this.lastShownSourceCodeBlock.ordinal()];
	}

	@Override
	public void executed(Block block, int line) {
		if ((this.lastShownSourceCodeBlock == null) || (this.lastShownSourceCodeBlock != block)) {
			this.showSourceCodeBlock(block);
		}

		this.exec(block.name()+line);

		switch (block) {
			case HASHING : this.handleHashing(line); break;
			case STORE : this.handleStore(line); break;
			case HASH : this.handleHash(line); break;
			case MUSTGROW : this.handleMustGrow(line); break;
			case GROW : this.handleGrow(line); break;
		}

		this.lang.nextStep();
	}

	private void handleGrow(int line) {
		switch (line) {
			case 1: this.insertedElements = this.sht.numElements; break;
			case 2:
				this.vars.set("hashTableSize", String.valueOf(this.sht.nextHashTableSize));
				break;
			case 4: this.animateTableGrowing(); break;
		}
	}

	private void handleMustGrow(int line) {}

	private void handleHash(int line) {}

	private void handleStore(int line) {
		switch (line) {
			case 1:
				this.vars.set("theKey", Integer.toString(this.sht.key));
				this.vars.set("theObject", this.sht.object);
				break;
			case 2:
				this.highlightHelpText(3, Integer.toString(this.sht.key));
				this.currentElementText.moveTo(this.hashText.getUpperLeft(), null, new MsTiming(300));
				this.currentElementText.hide(new MsTiming(300));

				this.hashText.setText("(" + this.sht.key + " mod " + this.sht.hashTable.length + ")", new MsTiming(300),
					new MsTiming(0));
				break;
			case 3:
				this.vars.set("homePosition", Integer.toString(this.sht.homeposition));
				this.hashText.setText("(" + this.sht.key + " mod " + this.sht.hashTable.length + ") = " + this.sht.homeposition, new MsTiming(300),
					new MsTiming(0));
				this.vars.set("pos", String.valueOf(this.sht.pos));
				this.currentElementText.setText(this.sht.object, null, null);
				this.currentElementText.show();
				this.currentElementText.moveBy("translate", 0, 100, null, new MsTiming(300));
				break;
			case 4:
				this.highlightHelpText(4, Integer.toString(this.sht.homeposition));
				this.currentElementText.moveTo(new OffsetCoords(this.hashTableFields.get(this.sht.pos).getUpperLeft(), -this.currentElementText.getWidth(), 0), null, new MsTiming(300));
				break;
			case 5:
				this.sigText.setText("sig = -1", new MsTiming(0), new MsTiming(0));
				this.sigText.show();
				this.kText.setText("k = 1", new MsTiming(0), new MsTiming(0));
				this.kText.show();

				this.hashText.setText(this.buildHashFunctionText(this.sht.homeposition, this.sht.k, this.sht.sig, this.sht.pos), new MsTiming(0),
					new MsTiming(0));
				break;
			case 6: break;
			case 7:
				this.sigText.setText("sig = " + this.sht.sig, new MsTiming(0), new MsTiming(0));
				this.vars.set("sig", Integer.toString(this.sht.sig));
				break;
			case 8:
				this.kText.setText("k = " + this.sht.k, new MsTiming(0), new MsTiming(0));
				break;
			case 9:
				this.highlightHelpText(6, null);
				break;
			case 10:
				this.vars.set("pos", String.valueOf(this.sht.pos));
				this.highlightHelpText(7, Integer.toString(this.sht.pos));

				this.hashText.setText(this.buildHashFunctionText(this.sht.homeposition, this.sht.k, this.sht.sig, this.sht.pos), new MsTiming(0),
					new MsTiming(0));

				this.currentElementText.moveTo(new OffsetCoords(this.hashTableFields.get(this.sht.pos).getUpperLeft(),
					-this.currentElementText.getWidth(), 0), new MsTiming(0), new MsTiming(300));
				break;
			case 11: break;
			case 12: this.highlightHelpText(5, this.sht.object);
			this.currentElementText.moveBy("translate", this.currentElementText.getWidth() + 25, 0, new MsTiming(0),
				new MsTiming(300));
			this.vars.set("hashTable-"+this.sht.pos, this.sht.object);
			break;
			case 13:
				this.currentElementText.changeColor(AnimalScript.COLORCHANGE_COLOR, (Color) this.dataTextProps.get("color"),
					new MsTiming(300), null);
				this.kText.hide();
				this.sigText.hide();
				break;
		}
	}

	private void handleHashing(int line) {
		switch (line) {
			case 1: break;
			case 2:
				this.hashText.setText("(KEY mod " + this.sht.hashTable.length + ")", new MsTiming(300),
					new MsTiming(0)); break;
			case 3: this.highlightHelpText(0, Double.toString(this.sht.hashTableLoadFactor * 100) + "%"); break;
			case 4: this.highlightHelpText(1, null); break;
			case 8:
				this.highlightHelpText(2, this.sht.object);
				this.currentElementText = this.dataTable.get(this.sht.numElements);
				this.currentElementText.changeColor(AnimalScript.COLORCHANGE_COLOR, this.elementHighLightColor, null, null);
				break;
		}
	}
}
