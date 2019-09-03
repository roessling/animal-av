package generators.sorting.quicksort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedIntArrayAlgorithm;
import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.parser.InteractionFactory;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Text;
import algoanim.properties.AnimationProperties;

public class GenericAnnotatedQuickSortICS2 extends AnimatedIntArrayAlgorithm implements Generator {
  protected Text swapLabel, swapPerf;
  protected InteractionFactory factory;
  protected Locale contentLocale = null;
  
  // Markers
  private ArrayMarker leftMarker;
  private ArrayMarker rightMarker;
  private ArrayMarker iMarker;
  private ArrayMarker pivotMarker = null;
  
  public GenericAnnotatedQuickSortICS2(String aResourceName, Locale aLocale) {
    resourceName = aResourceName;
    locale = aLocale;
    init();
  }

  public void init() {
    translator = new Translator(resourceName, locale);
    primitiveProps = new Hashtable<String, AnimationProperties>(59);
    localType = new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    contentLocale = locale;
    pivotMarker = null;
  }

  /**
   * hides the array, code, and number of steps taken from the display
   */
  protected void hideNrStepsArrayCode() {
    super.hideNrStepsArrayCode();
    if (array != null)
      array.hide();
  }


  
  //Returns -1 if all the keys are equal; otherwise the index of the bigger
  //key from the two first distinct keys
  int findPivot(int left, int right) {
    // Higlight function header
    code.highlight("findpivotheader");
    lang.nextStep();
    code.unhighlight("findpivotheader");
    
    // Highlight variables initialization
    code.highlight("variables2");
    incrementNrAssignments();
    lang.nextStep();
    code.unhighlight("variables2");
    int k = array.getData(left);

    iMarker.show();
    for (int i = left+1; i<= right; i++) {
        // Highlight for
        code.highlight("for");
        iMarker.move(i, null, null);
        incrementNrAssignments();
        incrementNrComparisons();
        lang.nextStep();
        code.unhighlight("for");
      
        // Highlight if
        code.highlight("findpivotif");
        incrementNrComparisons();
        lang.nextStep();
        code.unhighlight("findpivotif");
        // return index as soon as it is clear which key is larger
        if (array.getData(i) > k) {
          
          // Highlight return
          code.highlight("findpivotreturn1");
          lang.nextStep();
          code.unhighlight("findpivotreturn1");
          iMarker.hide();
          return i;
        }
        else if(array.getData(i) < k) {
          // Highlight return
          code.highlight("findpivotelse");
          lang.nextStep();
          code.unhighlight("findpivotelse");
          iMarker.hide();
          return left;
        }
    }
    iMarker.hide();
    // Highlight return
    code.highlight("findpivotreturn2");
    lang.nextStep();
    code.unhighlight("findpivotreturn2");
    // all keys equal
    return -1;
  }
  
 
  //Divide the interval [left,right] in such way, all keys smaller than the
  //pivot are left, and all the keys larger (or equal) are right.
  //Return the start position of the right subinterval
  private int divide(int left, int right, int pivot) {
    // Higlight function header
    code.highlight("divideheader");
    lang.nextStep();
    code.unhighlight("divideheader");
    
    // Highlight variables initialization
    code.highlight("variables3");
    incrementNrAssignments();
    incrementNrAssignments();
    incrementNrAssignments();

    array.highlightCell(left, null, null);
    array.highlightCell(right, null, null);
    lang.nextStep();
    code.unhighlight("variables3");
    int i = left, j = right, pivotElement = array.getData(pivot);

    do {
        // Highlight do
        code.highlight("dividedo");
        lang.nextStep();
        code.unhighlight("dividedo");
        
        // search for next swappable elements
        while(array.getData(i) < pivotElement) {
          // Highlight while
          code.highlight("dividewone");
          incrementNrAssignments();
          incrementNrComparisons();
          i++;
          if(i-1!=j)
            array.unhighlightCell(i-1, null, null);
          if(j!=i)
            array.highlightCell(i, null, null);
          lang.nextStep();
          code.unhighlight("dividewone");   
        }
        while(array.getData(j) >= pivotElement) {
          // Highlight while
          code.highlight("dividewtwo");
          incrementNrAssignments();
          incrementNrComparisons();
          j--;
          if(j+1!=i)
            array.unhighlightCell(j+1, null, null);
          if(j!=i)
            array.highlightCell(j, null, null);
          lang.nextStep();
          code.unhighlight("dividewtwo");
        }
        
        // Highlight if
        code.highlight("divideif");
        incrementNrComparisons();
        lang.nextStep();
        code.unhighlight("divideif");
        
        // swap elements if applicable
        if (i < j)  {
          // Highlight swap
          code.highlight("swap");
          array.highlightElem(i, null, null);
          array.highlightElem(j, null, null);
          lang.nextStep();
          incrementNrAssignments();
          incrementNrAssignments();
          incrementNrAssignments();
          array.swap(i, j, null, null);
          lang.nextStep();
          array.unhighlightElem(i, null, null);
          array.unhighlightElem(j, null, null);
          code.unhighlight("swap");
        }
        
     // Highlight while
        code.highlight("dividewhile3");
        incrementNrComparisons();
        lang.nextStep();
        code.unhighlight("dividewhile3");
    } while (i < j);
    array.unhighlightCell(i, null, null);
    if(i!=j)
      array.unhighlightCell(j, null, null);
    // Highlight return
    code.highlight("dividereturn");
    lang.nextStep();
    code.unhighlight("dividereturn");
    return i;
  }
  
  
//This function sorts the array recursively
  void quicksort(int left, int right) {
    // Higlight function header
    code.highlight("quicksortheader");
    leftMarker.move(left, null, null);
    rightMarker.move(right, null, null);
    lang.nextStep();
    code.unhighlight("quicksortheader");
    
    // Highlight variables initialization
    code.highlight("variables1");
    if(pivotMarker==null)
      pivotMarker = installArrayMarker("pivotMarker", array, 0);
    pivotMarker.moveOutside(null, null);
    lang.nextStep();
    code.unhighlight("variables1");
    int i, pivot;
    
    // Highlight findPivot execution
    code.highlight("callpivot");
    lang.nextStep();
    code.unhighlight("callpivot");
    pivot = findPivot(left, right);
    code.highlight("callpivot");
    if(pivot!=-1)
      pivotMarker.move(pivot, null, null);
    else
      pivotMarker.moveOutside(null, null);
    incrementNrAssignments();
    lang.nextStep();
    code.unhighlight("callpivot");
    
    // Highlight if statement
    code.highlight("quicksortif");
    incrementNrComparisons();
    lang.nextStep();
    code.unhighlight("quicksortif");
    if(pivot != -1) {
      // Highlight divide execution
      code.highlight("calldivide");
      lang.nextStep();
      code.unhighlight("calldivide");
      i = divide(left, right, pivot);
      code.highlight("calldivide");
      incrementNrAssignments();
      lang.nextStep();
      code.unhighlight("calldivide");
      
      // Highlight recursive 1
      code.highlight("recursive1");
      lang.nextStep();
      code.unhighlight("recursive1");
      quicksort(left, i-1);
      code.highlight("recursive1");
      lang.nextStep();
      code.unhighlight("recursive1");
      
      // Highlight recursive 2
      code.highlight("recursive2");
      lang.nextStep();
      code.unhighlight("recursive2");
      quicksort(i, right);
      code.highlight("recursive1");
      lang.nextStep();
      code.unhighlight("recursive1");
    }
  }
  
  public void sort() {
    // Initialize markers
    leftMarker = installArrayMarker("leftMarker", array, 0);
    rightMarker = installArrayMarker("rightMarker", array, array.getLength() - 1);
    iMarker = installArrayMarker("iMarker", array, 0);
    iMarker.hide();
    
    // Start Quick Sort
    quicksort(0, array.getLength()-1);
    
    HtmlDocumentationModel link = new HtmlDocumentationModel("link");
    link.setLinkAddress("http://de.wikipedia.org/wiki/Quicksort");
    lang.addDocumentationLink(link);
    HtmlDocumentationModel link2 = new HtmlDocumentationModel("link2");
    link2.setLinkAddress("http://java.net/index.html");
    lang.addDocumentationLink(link2);
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
    return "Quicksort";
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