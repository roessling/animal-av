package generators.sorting.gnomesort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;
import interactionsupport.parser.InteractionFactory;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Text;
import algoanim.properties.AnimationProperties;

public class GenericAnnotatedGnomeSortICS2 extends AnimatedIntArrayAlgorithm implements Generator {
  protected Text swapLabel, swapPerf;
  protected InteractionFactory factory;
  protected Locale contentLocale = null;

  public GenericAnnotatedGnomeSortICS2(String aResourceName, Locale aLocale) {
    resourceName = aResourceName;
    locale = aLocale;
    init();
  }

  public void init() {
    translator = new Translator(resourceName, locale);
    primitiveProps = new Hashtable<String, AnimationProperties>(59);
    localType = new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    contentLocale = locale;
  }

  /**
   * hides the array, code, and number of steps taken from the display
   */
  protected void hideNrStepsArrayCode() {
    super.hideNrStepsArrayCode();
    if (array != null)
      array.hide();
  }

  /**
   * Bubble Sort swaps neighbours if they are not sorted. It iterates up to n
   * times over the array, regarding only the elements at indices [0, n-i] in
   * iteration i. Run-time complexity in worst case: O(n*n)
   */
  public void sort() {
    // Highlight function header
    code.highlight("header");
    lang.nextStep();
    code.unhighlight("header");

    int gnome=1;
    code.highlight("initialize");
    incrementNrAssignments();    // gnome initialization
    // Create gnome marker
    ArrayMarker gnomeMarker = null;
    gnomeMarker = installArrayMarker("gnomeMarker", array, 1);
    lang.nextStep();
    code.unhighlight("initialize");
    
    
    // While loop
    while(gnome<=array.getLength()-1) {
      // Highlight "while"
      code.highlight("while");
      incrementNrComparisons();    // while
      lang.nextStep();
      code.unhighlight("while");
      
      // Highlight if
      code.highlight("if1");
      incrementNrComparisons();     // if
      lang.nextStep();
      code.unhighlight("if1");
      if(array.getData(gnome-1) <= array.getData(gnome)) {
        // Move gnome forward
        gnome++;
        if(gnome<array.getLength())
          gnomeMarker.move(gnome, null, DEFAULT_TIMING);
        else
          gnomeMarker.moveOutside(null, DEFAULT_TIMING);
        
        // Highlight "gnome++"
        code.highlight("gnome++");
        incrementNrAssignments();   //gnome++
        lang.nextStep();
        code.unhighlight("gnome++");
      }
      else {
        // Highlight Else
        code.highlight("else1");
        lang.nextStep();
        code.unhighlight("else1");
         
        // Highlight swapping
        code.highlight("swap1");
        incrementNrAssignments();   // swap
        lang.nextStep();
        code.unhighlight("swap1");

        code.highlight("swap2");
        incrementNrAssignments();   // swap
        lang.nextStep();
        code.unhighlight("swap2");
        
        code.highlight("swap3");
        incrementNrAssignments();   // swap
        lang.nextStep();
        code.unhighlight("swap3");
        array.swap(gnome - 1, gnome, null, DEFAULT_TIMING);
        
        // Move gnome backwards
        gnome--;
        gnomeMarker.move(gnome, null, DEFAULT_TIMING);
        
        // Highlight gnome--
        code.highlight("gnome--");
        incrementNrAssignments();    // gnome--
        lang.nextStep();
        code.unhighlight("gnome--");
        
        // Highlight if
        code.highlight("if2");
        incrementNrComparisons();    // if
        lang.nextStep();
        code.unhighlight("if2");
        if(gnome==0) {
          // In case gnome == 0 move gnome to 1
          gnome = 1;
          gnomeMarker.move(gnome, null, DEFAULT_TIMING);
          
          // Highlight "gnome=1";
          code.highlight("gnome=1");
          incrementNrAssignments();
          lang.nextStep();
          code.unhighlight("gnome=1");
        }
      }  
    }
  }

  /**
   * getContentLocale returns the target Locale of the generated output Use e.g.
   * Locale.US for English content, Locale.GERMANY for German, etc.
   * 
   * @return a Locale instance that describes the content type of the output
   */
  public Locale getContentLocale() {
    return contentLocale;
  }

  public String getAlgorithmName() {
    return "Gnome Sort";
  }

  public String getAnimationAuthor() {
    return "Georgi Hadshiyski";
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
    setUpDefaultElements(props, prims, "array", "code", "code", 0, 20);
    // new Offset(0, 20, array, AnimalScript.DIRECTION_SW));
    sort();
    if (swapPerf != null)
      swapPerf.hide();
    if (swapLabel != null)
      swapLabel.hide();
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }
}
