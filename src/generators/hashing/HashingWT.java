package generators.hashing;


import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;




public class HashingWT implements Generator {


	public boolean staticCharWidth = false;
	Language l;
	private Text modText;
	private Text collisionText;
	private Text averageHits;
	private int insertedKeys, numOfHits;
	int pixelWidthOfChar;
	Font f = new Font("Monospaced", Font.PLAIN, 12);

	Color colorHighlight;

	
	private final String SOURCE_CODE =
		"while(keys.hasNext(){"+
		"\n  Key key = keys.next()"+
		"\n  int pos = h(key, hashTable)"+
		"\n  hashTable.setKey(key, pos)"+
		"\n}"+
		"\n\nh(int key, Hashtable table){"+
		"\n  int pos = k % table.size()"+
		"\n  if(!table.isFree(pos)){"+
		"\n    pos = h(k+1, table)"+
		"\n  }"+
		"\n  return pos"+
		"\n}";
	
	private final String DESCRIPTION =
	"Hashing ist ein Verfahren um effizient ein Element in einer großen Datenmenge suchen zu können."
	+"\nEs basiert auf einer so genannten Hash-Funktion, die jedem Element eine Position in der Hashtabelle zuordnet."
	+"\nDadurch ist es im BestCase möglich, ein Element in konstanter Zeit einzuordnen und auch wieder auffinden zu können."
	+"\nDiese Hashfunktion kann z.B. wie folgt aussehen: 'h(k) = k mod s' wobei k der einzufügende Schlüssel sein soll und s die Größe der Hashtabelle."
	+"\nFalls 2 Elemente auf dieselbe Position in der Tabelle abgebildet werden (wie bei s=11, k1=2, k2=13), kommt es zu einer Kollision"
	+"\nDiese Kollisionen werden durch das so genannte Sondierungsverfahren aufgelöst, welches versucht, dem Element eine andere Position zuzuordnen"
	+"\nDas einfachste und hier vorgestellte Verfahren heißt 'Lineare Sondierung', in dem ab der Hashposition einfach so lange linear die Tabelle durchlaufen wird, bis ein freier Platz gefunden ist"
	+"\n\nUm die Kollisionen innerhalb der Hashingtabelle und den damit verbundenen Aufwand beim linearen Sondieren"
	+"\ngering zu halten, sollten die Schlüssel möglichst durch die Hashfunktion möglichst gleichverteilt positioniert werden."
	+"\nWenn man die Struktur der Schlüssel nicht kennt (z.B. Geburtsdaten, Indizies), ist es sinnvoll, eine Primzahl als Größe"
	+"\nder Hashtabelle und damit als Teiler in der Hashfunktion zu wählen";
	
	static Timing StandardTiming = new TicksTiming(40);
	

	
	public HashingWT(){
		l = new AnimalScript("Hashing Animation", "Ioannis Tsigaridas & Manuel Wick", 800,600);
		l.setStepMode(true);
	}
	
	
	/**
	 * Function to determine the width of textelements with the given font
	 * @param f
	 * @return the FontMetrics object
	 */
	@SuppressWarnings("deprecation")
	public static FontMetrics getConcreteFontMetrics(Font f) {
//		Graphics graphics = null; //
		
		
//		Animal.get().getGraphics();

		// problem: need an Animal instance running before!
//		if (graphics != null){
//			
//			return graphics.getFontMetrics(f);
//			
//		}
//		else{
//			System.out.println("check");
			return Toolkit.getDefaultToolkit().getFontMetrics(f);
//		}
	}
	
	public static int getStringWidth(String text, Font f) {
		int width = 0;
		FontMetrics fm = getConcreteFontMetrics(f);
	
		if ((fm != null) && (text != null)) {
			width = fm.stringWidth(text);
		}
		return width;
	}
	
	
	
	/**
	 * Selbsterstelltes Element, das sich frei über den Screen bewegen lässt und
	 * in sich mit anderen Elementen z.B. in der Hashtabelle oder einer Liste ablegen lässt.
	 * @author manuel
	 *
	 */
	public class Element{
		Text text;
		int pixelWidth;
		
		public Element(int value, Node node) {
			this.pixelWidth = pixelWidthOfChar*String.valueOf(value).length();
			TextProperties textProps = new TextProperties();
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, f);
			text = l.newText(node, String.valueOf(value), String.valueOf(this.hashCode()), null, textProps);
		}
		
		public Text getElement(){
			return text;
		}
		
		public int getPixelWidth(){
			return pixelWidth;
		}
		
		public int getValue(){
			return Integer.valueOf(text.getText());
		}
		
		public void highlight(){
			this.text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, colorHighlight, null, null);
		}
		
		public void unhighlight(){
			this.text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
		}
		
		public void moveTo(Node node, Timing delay, Timing duration){
			try {
				this.text.moveTo(AnimalScript.DIRECTION_NW, null, node, delay, duration);
			} catch (IllegalDirectionException e) {
				e.printStackTrace();
			}
		}
		
		public void moveTo(Node node, Timing delay){
			moveTo(node, delay, StandardTiming);
		}
	}
	
	
	/**
	 * Liste, die zu Anfang des Hashings alle Elemente beinhaltet.
	 * Bietet die Möglichkeit, Elemente zu entnehmen und gleichzeitig alle Folgenden
	 * nachrücken zu lassen.
	 * @author manuel
	 *
	 */
	public class KeyList implements Iterator<Element>{
		
		private ArrayList<Element> keys = new ArrayList<Element>();
		
		public KeyList(Node node, int[] keys) {
			
			this.keys.add(new Element(keys[0], node));
			for(int i=1; i<keys.length; i++){	
				this.keys.add(new Element(keys[i], new Offset(5,0, this.keys.get(i-1).getElement(), AnimalScript.DIRECTION_NE)));
			}
		}

		@Override
		public boolean hasNext() {
			return !keys.isEmpty();
		}

		@Override
		public Element next() {
			return keys.get(0);
		}

		@Override
		public void remove() {
			keys.remove(0);
		}
		
		public void moveAllKeys(){
			for(Element key : keys){
				key.text.moveBy(null, -(next().pixelWidth+5), 0, null, StandardTiming);
			}
		}
		
	}
	
	
	/**
	 * Hashtabelle, in die an beliebigen Positionen Elemente eingefügt werden können.
	 * @author manuel
	 *
	 */
	public class HashTable{
		
		private ArrayList<Rect> cell = new ArrayList<Rect>();
		private ArrayList<Element> keys = new ArrayList<Element>();
		private int numberOfElements;
		
		public HashTable(int x, int y, int dx, int dy, int numberOfCells) {
			
			if(numberOfCells<=0){
				try {
					throw new Exception("Number of cells must be greater than 0");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			numberOfElements = numberOfCells;
			
			int maxDigitsWidth = String.valueOf(numberOfCells).length()*pixelWidthOfChar+5;
			
			cell.add(l.newRect(new Coordinates(x,y), new Coordinates(x+dx,y+dy), this.hashCode()+"0", null));
			l.newText(new Offset(-maxDigitsWidth,5,cell.get(0), AnimalScript.DIRECTION_NW), "0", this.hashCode()+"0index", null);
			keys.add(null);
			
			for(int i=1; i<numberOfCells; i++){
				cell.add(l.newRect(new Offset(0,0,cell.get(i-1),AnimalScript.DIRECTION_SW), new Offset(dx,dy,cell.get(i-1),AnimalScript.DIRECTION_SW) ,this.hashCode()+""+i, null));
				l.newText(new Offset(-maxDigitsWidth,5,cell.get(i), AnimalScript.DIRECTION_NW), String.valueOf(i), this.hashCode()+i+"index", null);
				keys.add(null);
			}
		}
		
		public Rect getCell(int i){
			return cell.get(i);
		}
		
		public Element getKey(int pos){
			return keys.get(pos);
		}
		
		public boolean isFree(int pos){
			return (keys.get(pos) == null);
		}
		
		public void setKey(Element key, int pos){
			keys.set(pos, key);
		}
		
		public int getSize(){
			return numberOfElements;
		}
		
	}
	

	
	private void showDescription(){
		SourceCode description = l.newSourceCode(new Coordinates(45,100), "description", null);
		description.addCodeLine("Hashing ist ein Verfahren um effizient ein Element in einer großen Datenmenge suchen zu können.", null, 0, null);
		description.addCodeLine("Es basiert auf einer so genannten Hash-Funktion, die jedem Element eine Position in der Hashtabelle zuordnet.", null, 0, null);
		description.addCodeLine("Dadurch ist es im BestCase möglich, ein Element in konstanter Zeit einzuordnen und auch wieder auffinden zu können.", null, 0, null);
		description.addCodeLine("", null, 0, null);
		description.addCodeLine("Diese Hashfunktion kann z.B. wie folgt aussehen: 'h(k) = k mod s' wobei k der einzufügende Schlüssel sein soll und s die Größe der Hashtabelle.", null, 0, null);
		description.addCodeLine("Falls 2 Elemente auf dieselbe Position in der Tabelle abgebildet werden (wie bei s=11, k1=2, k2=13), kommt es zu einer Kollision", null, 0, null);
		description.addCodeLine("Diese Kollisionen werden durch das so genannte Sondierungsverfahren aufgelöst, welches versucht, dem Element eine andere Position zuzuordnen", null, 0, null);
		description.addCodeLine("", null, 0, null);
		description.addCodeLine("Das einfachste und hier vorgestellte Verfahren heißt 'Lineare Sondierung', in dem ab der Hashposition einfach so lange", null, 0, null);
		description.addCodeLine("linear die Tabelle durchlaufen wird, bis ein freier Platz gefunden ist", null, 0, null);
		description.addCodeLine("", null, 0, null);
		description.addCodeLine("while(keys.hasNext(){", null, 0, null);
		description.addCodeLine("Key key = keys.next()", null, 1, null);
		description.addCodeLine("int pos = h(key, hashTable)", null, 1, null);
		description.addCodeLine("hashTable.setKey(key, pos)", null, 1, null);
		description.addCodeLine("}", null, 0, null);
		description.addCodeLine("", null, 0, null);
		description.addCodeLine("h(int key, Hashtable table){", null, 0, null);
		description.addCodeLine("int pos = k % table.size()", null, 1, null);
		description.addCodeLine("if(!table.isFree(pos)){", null, 1, null);
		description.addCodeLine("pos = h(k+1, table)", null, 2, null);
		description.addCodeLine("}", null, 1, null);
		description.addCodeLine("return pos", null, 1, null);
		description.addCodeLine("}", null, 0, null);
		
		l.nextStep();
		description.hide();
	}
	
	/**
	 * Ermittelt den Pixel-Längsten Key im Array
	 * @param keysData
	 * @return
	 */
	private int getLongestKeyWidth(int[] keysData){
		int maxLength=0;
		String stringKey;
		for(int key : keysData){
			stringKey = String.valueOf(key);
			maxLength = stringKey.length()>maxLength ? stringKey.length() : maxLength;
		}
		return pixelWidthOfChar*maxLength;
	}
	
	/**
	 * Generiert zufällig Zahlen der Maximallänge maxNumOfDigits.
	 * @param numOfKeys
	 * @param maxNumOfDigits
	 * @return the random numbers
	 */
	public static int[] generateRandomNumber(int numOfKeys, int maxNumOfDigits){
		int[] key = new int[numOfKeys];
		for(int i=0; i<numOfKeys; i++){
			key[i] = (int) Math.floor(Math.random()*Math.pow(10, maxNumOfDigits));
		}
		return key;
	}
	
	/**
	 * Averages Hits aktualisieren
	 */
	private void updateAverageHits(){
		String av = String.valueOf((numOfHits*1.0/insertedKeys));
		av = av.substring(0,Math.min(4,av.length()));
		averageHits.setText("durchschnittliche Hits: "+numOfHits+" Zugriff(e) / "+insertedKeys+" Schlüssel = "+av, null, null);
	}

	
	/**
	 * Das Hashing-Verfahren
	 * @param hashtableSize
	 * @param keysData
	 */
	public void hash(int hashtableSize, int[] keysData){

		Node upperLeft = new Coordinates(60,40);
		Text titleText = l.newText(upperLeft, "Lineares Hashing", "titleText", null);
		
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY , Color.lightGray);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY , true);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		
		l.newRect(new Offset(-15,-5,titleText,AnimalScript.DIRECTION_NW), new Offset(15,5,titleText,AnimalScript.DIRECTION_SE), "titleBox", null, rectProps);
		
		showDescription();
		
		
		// Dem Benutzer werden Infos über die gewählte Länge der Hashtabelle und das Resultat dessen gegeben.
		// Gegebenenfalls wird eine passende Hashtabellengröße ermittelt.
		SourceCode descriptionPS = l.newSourceCode(new Coordinates(100,100), "description", null);
		int hashtableSize2 = hashtableSize;
    if(hashtableSize2==0){
			descriptionPS.addCodeLine("Um die Anzahl der Kollisionen beim Hashing möglichst gering zu halten", null, 0, null);
			descriptionPS.addCodeLine("wird für die Hashtabellengröße und damit als Teiler in der Hashfunktion", null, 0, null);
			descriptionPS.addCodeLine("die nächstgrößere Primzahl benutzt.", null, 0, null);
		}
		else if(hashtableSize2<keysData.length){
			descriptionPS.addCodeLine("Als Größe der Hashtabelle wurde "+hashtableSize2+" gewählt.", null, 0, null);
			descriptionPS.addCodeLine("Es würden nicht alle "+keysData.length+" Schlüssel in die Tabelle passen.", null, 0, null);
			descriptionPS.addCodeLine("Die nächstgrößere Primzahl wird als neue Größe benutzt.", null, 0, null);
		}
		else{
			descriptionPS.addCodeLine("Die Größe der Hashtabelle wurde vom Benutzer festgelegt.", null, 0, null);
		}
		hashtableSize2 = hashtableSize2 < keysData.length ? nextPrim(keysData.length) : hashtableSize2;
		
		descriptionPS.addCodeLine("", null, 0, null);
		descriptionPS.addCodeLine("Schlüsselanzahl: "+keysData.length, null, 0, null);
		descriptionPS.addCodeLine("Hashtabellengröße: "+hashtableSize2, null, 0, null);
		
		l.nextStep();
		descriptionPS.hide();
		
		
		

		modText = l.newText(new Coordinates(220,180), "", "modText", null);
		
		TextProperties collisionProps = new TextProperties();
		
		insertedKeys = numOfHits = 0;
		averageHits = l.newText(new Offset(0,5,modText,AnimalScript.DIRECTION_SW), "", "averageHits", null);
		updateAverageHits();
		
		collisionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorHighlight);
		collisionText = l.newText(new Offset(0,5,averageHits,AnimalScript.DIRECTION_SW), "", "collisionText", null, collisionProps);
		
		// "22" hat die Pixelbreite 16 + 10 für beide Ränder //
		HashTable table = new HashTable(100,120,getLongestKeyWidth(keysData)+10,12+10,hashtableSize2);

		KeyList keyTable = new KeyList(new Coordinates(200,120),keysData);
		
		
		
		
		l.nextStep();
		
		while(keyTable.hasNext()){
			Element key = keyTable.next();
			l.nextStep();
			keyTable.moveAllKeys();
			int pos = h(key.getValue(), key, table);
			l.nextStep();
			key.moveTo(new Offset(5,5,table.getCell(pos),AnimalScript.DIRECTION_NE), null);
			table.setKey(key, pos);
			insertedKeys++; numOfHits++;
			updateAverageHits();
			keyTable.remove();
		}

	}
	
	
	/**
	 * Der eigentliche Hashing-Algorithmus mit linearer Sondierung
	 * Nicht sicher, falls mehr Elemente als Hashtabellengröße
	 * @param k
	 * @param hashTable
	 * @return
	 */
	private int h(int k, Element key, HashTable table){
		int pos = k % table.getSize();
		
		modText.setText(k+" mod "+table.getSize()+" = "+pos, null, null);
		
		key.moveTo(new Offset(40,5,table.getCell(pos),AnimalScript.DIRECTION_NW), null);

		if(!table.isFree(pos)){
			numOfHits++;
			updateAverageHits();
			modText.setText(k+" mod "+table.getSize()+" = "+pos, null, null);
			collisionText.setText("Kollision", null, null);
			int oldPos = pos;
			table.getKey(oldPos).highlight();
			l.nextStep();
			collisionText.setText("", null, null);
			pos = h(k+1, key, table); // Lineares Sondieren //
			table.getKey(oldPos).unhighlight();
		}
		
		return pos;
	}
	
	/**
	 * Sucht die nächste Primzahl von z aus.
	 * @param z
	 * @return the next prime
	 */
	public int nextPrim(int z){
		boolean found=false;

		int z2 = z;
    while(!found){
			z2++;
			found = true;
			int s = (int)Math.sqrt((double)z2);
			for(int i=2; i<=s; i++){
				if(z2 % i == 0){
					found = false;
				}
			}
		}
		return z2;
	}

	@Override
	public String generate(AnimationPropertiesContainer properties,
			Hashtable<String, Object> primitives) {
		
		int[] keys = (int[])primitives.get("keys");
		colorHighlight = (Color) properties.get("color", AnimationPropertiesKeys.COLOR_PROPERTY);

		int hashtableSize = (Integer)primitives.get("hashtableSize");
		
		if(staticCharWidth){
			//statisch zur Ermittlung der Länge eines Characters
			//wird im Generator durch die Methode Hashing.getStringWidth gelöst
			this.pixelWidthOfChar = 7;
		}else{
			Font font = new Font("Monospace", Font.PLAIN, 12);
			pixelWidthOfChar = HashingWT.getStringWidth("1", font);
		}
		

		this.hash(hashtableSize, keys);
		return 	l.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Hashing mit linearer Sondierung";
	}

	@Override
	public String getCodeExample() {
		return SOURCE_CODE;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
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
	public String getName() {
		return "Linear Hashing";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public String getAnimationAuthor() {
		return "Ioannis Tsigaridas, Manuel Wick";
	}

	@Override
	public void init() {
		// nothing to be done here...
		
	}
}
