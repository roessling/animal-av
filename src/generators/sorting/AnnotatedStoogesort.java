package generators.sorting;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
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
 * Lösung für die Endabgabe 
 * @author Clemens Bergmann
 *
 */
public class AnnotatedStoogesort extends AnnotatedAlgorithm implements Generator {
  
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
   * Dies ist der Standard Konstruktor
   * Er erstellt einen Generator mit leerem Geheimtext und dem Schlüsselbuchstaben 'a'
   */
  public AnnotatedStoogesort() {
  }
  
  private StringArray arr;
  private TextUpdater tui,tuj,tucomp,tuk;
  private ArrayMarkerUpdater amui, amuj;

  /**
   * Diese Funktion erstellt überschift und Sourcecode in der Animation
   */
  private void stooge_prepare(String tosort, AnimationPropertiesContainer props){
    
    
    DisplayOptions header_ops = null;
    Text header = lang.newText(new Coordinates(20, 30), this.getAlgorithmName(), "header", header_ops);

    SourceCodeProperties scp = new SourceCodeProperties();

    scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);

    this.sourceCode = lang.newSourceCode(new Offset(0, 150, header, AnimalScript.DIRECTION_SW),"source", null, scp);

    ArrayList<String> al = new ArrayList<String>();
    for(char c : tosort.toCharArray()){
      al.add(""+c);
    }
    ArrayProperties ap = (ArrayProperties) props.getPropertiesByName("ap");
    this.arr = lang.newStringArray(new Offset(0, 40, header, AnimalScript.DIRECTION_SW), al.toArray(new String[0]), "tosort", null, ap);

    ArrayMarkerProperties amp = (ArrayMarkerProperties) props.getPropertiesByName("amp");
    ArrayMarker mark_i = lang.newArrayMarker(arr, 0, "mark_i", null, amp);
    this.amui = new ArrayMarkerUpdater(mark_i, null, null , this.arr.getLength() - 1);
    
    ArrayMarker mark_j = lang.newArrayMarker(arr, this.arr.getLength()-1, "mark_j", null, amp);
    this.amuj = new ArrayMarkerUpdater(mark_j, null, null , this.arr.getLength() - 1);

    vars.declare("int", "i"); vars.setGlobal("i");
    vars.declare("int", "j"); vars.setGlobal("j");
    vars.declare("int", "k"); vars.setGlobal("k");
    vars.declare("int", "comp"); vars.setGlobal("comp");
    vars.declare("string", "ai"); vars.setGlobal("ai");
    vars.declare("string", "aj"); vars.setGlobal("aj");
    this.amui.setVariable(vars.getVariable("i"));
    this.amuj.setVariable(vars.getVariable("j"));
    
    Text i_text = lang.newText(new Offset(0,3,this.arr,AnimalScript.DIRECTION_SW ), "" , "i", null);
    this.tui = new TextUpdater(i_text);
    tui.addToken("i = ");
    tui.addToken(vars.getVariable("i"));
    tui.update();
    
    Text j_text = lang.newText(new Offset(0,3,i_text,AnimalScript.DIRECTION_SW ), "" , "j", null);
    this.tuj = new TextUpdater(j_text);
    tuj.addToken("j = ");
    tuj.addToken(vars.getVariable("j"));
    tuj.update();
    
    Text comp_text = lang.newText(new Offset(0,3,j_text,AnimalScript.DIRECTION_SW ), "" , "comp", null);
    this.tucomp = new TextUpdater(comp_text);
    tucomp.addToken(vars.getVariable("ai"));
    tucomp.addToken(" < ");
    tucomp.addToken(vars.getVariable("aj"));
    tucomp.addToken(" ? => ");
    tucomp.addToken(vars.getVariable("comp"));
    tucomp.update();
    
    Text k_text = lang.newText(new Offset(0,3,comp_text,AnimalScript.DIRECTION_SW ), "" , "k", null);
    this.tuk = new TextUpdater(k_text);
    tuk.addToken("k = (j-i+1)/3 =");
    tuk.addToken(vars.getVariable("k"));
    tuk.update();
    
    parse();
  }
  
  /**
   * 
   */
  private void stooge_intro(){
    exec("var_A");
  }

  /**
   * Sort the given range of the array
   * @param i
   * @param j
   */
  private void stooge_sort(int i, int j){
    exec("var_i");
    vars.set("i", "" + i);
    lang.nextStep();
    
    exec("var_j");
    vars.set("j", "" + j);
    lang.nextStep();
    
    vars.set("ai", arr.getData(Integer.parseInt(vars.get("i"))));
    vars.set("aj", arr.getData(Integer.parseInt(vars.get("j"))));
    
    exec("compare");
    boolean test=vars.get("ai").compareTo(vars.get("aj")) > 0;
    vars.set("comp", (test) ? "true" : "false");
    lang.nextStep();
    
    if(test){
      exec("then");
      arr.swap(i, j, this.wait, this.duration);
      lang.nextStep();
    }

    if(i+1 >= j){
      exec("else");
      lang.nextStep();
      return;
    }
    
    exec("calc_k");
    int k=((j-i+1)/3);
    vars.set("k", ""+ k);
    lang.nextStep();
    
    exec("rec1");
    stooge_sort(i,j-k);
        
    exec("rec2");
    stooge_sort(i+k,j);
    
    exec("rec3");
    stooge_sort(i,j-k);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    this.init();
    
    String tosort=(String) primitives.get("tosort");
    this.wait = new TicksTiming((Integer) primitives.get("wait"));
    this.duration = new TicksTiming((Integer) primitives.get("duration"));
    
    stooge_prepare(tosort, props);
    
    stooge_intro();
    
    stooge_sort(0, tosort.length()-1);
    
    return this.lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Stooge Sort";
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
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getName() {
    return "Stoogesort übergebbarem sortierarray [Annotated]";
  }

  @Override
  public String getOutputLanguage() {
    return PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    super.init();
  }


  @Override
  public String getAnnotatedSrc() {
    return "1. Gegeben sei ein Array A. Diese enthält ein zu sortierende Zeichenfolge."
         + " @label(\"var_A\") \n"
         + "2. Gegeben sei die linke Grenze i des zu sortierenden Bereichs."
         + " @label(\"var_i\") @openContext \n"
         + "3. Gegeben sei die rechte Grenze j des zu sortierenden Bereichs."
         + " @label(\"var_j\") \n"
         + "4. Vergleiche A[i] und A[j]."
         + " @label(\"compare\") \n"
         + "5. Sollte die Reihenfolge nicht stimmen vertausche A[i] und A[j]."
         + " @label(\"then\") @highlight(\"compare\")\n"
         + "6. Sollte i+1 größer J sein, breche ab."
         + " @label(\"else\") @highlight(\"compare\")\n"
         + "7. Berechne k als Grenze der Drittel des Arrays. k = [(j-i+1)/3]"
         + " @label(\"calc_k\") \n"
         + "8. Berechne Stoogesort für die ersten beiden Drittel."
         + " @label(\"rec1\") @closeContext\n"
         + "9. Berechne Stoogesort für die letzten beiden Drittel."
         + " @label(\"rec2\") @closeContext\n"
         + "10. Berechne Stoogesort für die ersten beiden Drittel."
         + " @label(\"rec3\") @closeContext\n";
  }
}
