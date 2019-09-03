package generators.cryptography.caesarcipher;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * Lösung für die 5. Aufgabe 
 * @author Clemens Bergmann
 *
 */
public class AnnotatedCaesarWithKey extends AnnotatedAlgorithm implements Generator {
  
  /**
   * Dieses Interface beschreibt eine Klasse mit einer Funktion die von einem Zeichen auf einen Verschiebewert abbildet.
   * @author Clemens Bergmann
   *
   */
  private interface keyfunction{
    /**
     * Diese Funktion bekommt ein Zeichen und liefert den zugehörigen Schiebewert.
     * @param c Das Schlüsselzeichen
     * @return der Verschiebewert
     */
    int getshiftnum(char c);		
  }

  /*
   * Alle Generatoren dieser Klasse haben das selbe Timing
   */
  /**
   * Wie lange soll vor einer Aktion gewartet werden
   */
  private Timing wait = null;
  /**
   * Wie lange soll eine Aktion dauern
   */
  private Timing duration = new TicksTiming(20);
  
  /**
   * Die generellen eigenschaften von allen Arrays eines generators diesen Types
   */
  private ArrayProperties ap;
  
  /**
   * Die generellen Eigenschaften aller ArrayMarker eines Generators diesen Types
   */
  private ArrayMarkerProperties amp;
  
  /**
   * Das Alphabet auf dem alle generatoren diesen typs arbeiten.
   */
  char[] alphabet;
  
  /**
   * Der Schlüßelbuchstabe eines Generators
   */
  private char k;
  
  /**
   * Der Geheimtext eines Generators
   */
  private String secret;

  /**
   * Dies ist der Standard Konstruktor
   * Er erstellt einen Generator mit leerem Geheimtext und dem Schlüsselbuchstaben 'a'
   */
  public AnnotatedCaesarWithKey() {
    this('a',"");
  }

  /**
   * Der normalerweise verwendeten Konstruktor
   * @param k Der Schlüsselbuchstabe
   * @param secret Das Geheimniss das verschlüsselt werden soll
   */
  public AnnotatedCaesarWithKey(char k,String secret){
    this.k=k;
    this.secret=secret;
    this.lang = new AnimalScript(this.getAlgorithmName(), this.getAnimationAuthor(), 640, 480);
  }

  /**
   * Diese Funktion erstellt überschift und Sourcecode in der Animation
   */
  private void caesar_prepare(){
    DisplayOptions header_ops = null;
    Text header = lang.newText(new Coordinates(20, 30), this.getAlgorithmName(), "header", header_ops);

    SourceCodeProperties scp = new SourceCodeProperties();

    scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);

    this.sourceCode = lang.newSourceCode(new Offset(0, 100, header, AnimalScript.DIRECTION_SW),"source", null, scp);

    parse();
  }

  /**
   * Diese Funktion erstellt die Animation für die Verschlüsselung eines Textes.
   * @param klartext Der zu verschlüsselnde Klartext
   * @param schluessel Der Schlüsselbuchstabe
   * @param f die Mappingfunktion von schluessel auf einen Verschiebewert
   * @return Der verschlüsselte Text
   */
  private String caesar_crypt(String klartext, char schluessel, keyfunction f){    
    
    exec("enc");

    lang.nextStep();
    
    exec("enc_var_t");
    ArrayList<String> al = new ArrayList<String>();
    for(char c : klartext.toCharArray()){
      al.add(""+c);
    }
    
    StringArray arr = lang.newStringArray(new Offset(0, 40, this.sourceCode, AnimalScript.DIRECTION_SW), al.toArray(new String[0]), "klartext", null, ap);
    
    
    lang.nextStep();
    
    exec("enc_var_f");
    
    lang.nextStep();
    
    exec("enc_var_k");
    
    Text k_text = lang.newText(new Offset(0,20,arr,AnimalScript.DIRECTION_SW ), "k = " + schluessel , "k", null);
    
    lang.nextStep();
    
    exec("enc_var_n");
    
    int shiftnum = f.getshiftnum(schluessel);
    
    k_text.setText("n = f(k) = " + shiftnum, null, null);
    
    lang.nextStep();
    
    k_text.hide();
    
    exec("enc_shift");

    ArrayMarker marker = lang.newArrayMarker(arr, 0, "pointer", null, amp);
    
    for(int i=0; i < arr.getLength(); i++){
      marker.move(i, null, null);
      for(int j=0; j<shiftnum; j ++){
        arr.put(i, "" + shift(arr.getData(i).charAt(0),1), wait, duration);
      }
      
      lang.nextStep();
    }
    
    exec("enc_send");
    
    arr.hide(duration);
    
    lang.nextStep();
    
    String result = "";
    for(int i=0; i< arr.getLength(); i++){
      result += arr.getData(i);
    }
    
    return result;
  }

  /**
   * Diese Funktion entschlüsselt einen Text der vorher mit {@link #caesar_crypt(String, char, keyfunction)} verschlüsselt wurde
   * @param cypher Der verschlüsselte Text
   * @param schluessel Der Schlüsselbuchstabe
   * @param f Die gleiche mappingfunktion wie bei {@link #caesar_crypt(String, char, keyfunction)}.
   * @return Der entschlüsselte Text
   */
  private String caesar_uncrypt(String cypher, char schluessel, keyfunction f){
    exec("dec");

    lang.nextStep();
    
    exec("dec_var_c");
    ArrayList<String> al = new ArrayList<String>();
    for(char c : cypher.toCharArray()){
      al.add(""+c);
    }
    StringArray arr = lang.newStringArray(new Offset(0, 40, this.sourceCode, AnimalScript.DIRECTION_SW), al.toArray(new String[0]), "klartext", null, ap);
    
    lang.nextStep();
    
    exec("dec_var_f");
    
    lang.nextStep();
    
    exec("dec_var_K");
    
    Text k_text = lang.newText(new Offset(0,20,arr,AnimalScript.DIRECTION_SW ), "k = " + schluessel , "k", null);
    
    lang.nextStep();
    
    exec("dec_var_n");
    
    int shiftnum = f.getshiftnum(schluessel);
    
    k_text.setText("n = f(k) = " + shiftnum, null, null);
    
    lang.nextStep();
    
    k_text.hide();
    
    exec("dec_shift");
    
    ArrayMarker marker = lang.newArrayMarker(arr, 0, "pointer", null, amp);
    
    for(int i=0; i < arr.getLength(); i++){
      marker.move(i, null, null);
      for(int j=0; j<shiftnum; j ++){
        arr.put(i, "" + shift(arr.getData(i).charAt(0),-1), wait, duration);
      }
      
      lang.nextStep();
    }
    
    exec("dec_var_t");
    
    lang.nextStep();
    
    String result = "";
    for(int i=0; i< arr.getLength(); i++){
      result += arr.getData(i);
    }
    
    return result;
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    
    this.init();

    this.secret=(String) primitives.get("secret");
    this.k = ((String) primitives.get("key")).charAt(0);
    alphabet = ((String) primitives.get("alphabet")).toCharArray();
    Arrays.sort(alphabet);
    wait = new TicksTiming((Integer) primitives.get("wait"));
    duration = new TicksTiming((Integer) primitives.get("duration"));
    amp = (ArrayMarkerProperties) props.getPropertiesByName("amp");
    ap = (ArrayProperties) props.getPropertiesByName("ap");
    
    keyfunction f = new keyfunction(){
      @Override
      public int getshiftnum(char c) {
        int result = 0;
        char c2 = c;
        c2 = tolower(c2);
        result = Arrays.binarySearch(alphabet, c2) + 1;
        result = Math.max(result, 0);
        return result;
      }

    };
    this.caesar_prepare();
    String c = this.caesar_crypt(this.secret, this.k, f);    
    this.caesar_uncrypt(c, this.k, f);
    
    return this.lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Caesar-Verschl\u00fcsselung";
  }

  @Override
  public String getAnimationAuthor() {
    return "Clemens Bergmann"; // <cbergmann@schuhklassert.de>";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "This Generator generates a Animation for the Caesar-Chiffre.";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  @Override
  public String getName() {
    return "Caesar Chiffre mit übergebbarem Verschiebewert [Annotated]";
  }
  
  @Override
  public String getOutputLanguage() {
    return PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    super.init();
  }

  /**
   * Diese Funktion verschiebt einen Buchstaben um den gegebenen Wert.
   * @param in Der Buchstabe
   * @param shift Die Verschiebezahl
   * @return Der verschobene Buchstabe
   */
  private char shift(char in, int shift){
    char in2 = in;
    in2 = tolower(in2);
    int index=Arrays.binarySearch(alphabet, in2);

    char result;
    if(index>=0){
      int new_index=((index+shift)%alphabet.length);
      if(new_index<0){
        new_index+=alphabet.length;
      }
      result=alphabet[new_index];
    }else{
      result=in2; 
    }

    return result;
  }
  
  /**
   * Diese Funktion wandelt einen Buchstaben in seine kleinschreibweise
   * @param in Der Buchstabe
   * @return die kleine Variante des Buchstabens
   */
  static char tolower(char in){
    return ("" + in).toLowerCase().charAt(0);
  }

  @Override
  public String getAnnotatedSrc() {
    return "A: Verschlüsselung"
     + " @label(\"enc\") \n"
     + "1. Gegeben sei der Klartext t. Dieser enthält schützenswerte Informationen."
     + " @label(\"enc_var_t\") \n"
     + "2. Gegeben sei eine Funktion f die einem Schlüsselbuchstaben eine Zahl zuweist (z.B. seine Position im Alphabet)"
     + " @label(\"enc_var_f\") \n"
     + "3. Gegeben sei ein Schlüsselbuchstabe k."
     + " @label(\"enc_var_k\") \n"
     + "4. Wende f auf k an. Das Ergebniss sei n."
     + " @label(\"enc_var_n\") \n"
     + "5. Verschiebe jeden Buchstaben im Schlüsseltext zyklisch um k. Das Ergebniss ist der Cyphertext c."
     + " @label(\"enc_shift\") \n"
     + "6. Versende eine Nachricht bestehend aus k und c an den Empfänger. Dieser muss f kennen."
     + " @label(\"enc_send\") \n"
     + "B: Entschlüsselung"
     + " @label(\"dec\") \n"
     + "1. Empfangen wird der Cyphertext c."
     + " @label(\"dec_var_c\") \n"
     + "2. Gegeben sei die Funktion f."
     + " @label(\"dec_var_f\") \n"
     + "3. Empfangen wird der Schlüsselbuchstabe K."
     + " @label(\"dec_var_K\") \n"
     + "4. Wende f auf an. das Ergeniss sei n."
     + " @label(\"dec_var_n\") \n"
     + "5. Verschiebe die Buchstaben in c zyklisch um k. Das Ergeniss ist der Klartext t."
     + " @label(\"dec_shift\") \n"
     + "6. Das Ergebniss ist der Klartext t."
     + " @label(\"dec_var_t\") \n";
  }
}
