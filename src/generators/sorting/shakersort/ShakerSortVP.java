package generators.sorting.shakersort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class ShakerSortVP implements generators.framework.Generator {

  protected Language lang;

  public ShakerSortVP() {
    init();
  }

  public ShakerSortVP(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  private void sort(int[] a, ArrayProperties aprops,
      SourceCodeProperties scprops, ArrayMarkerProperties amprops_i,
      ArrayMarkerProperties amprops_j) {

    IntArray ia = lang.newIntArray(new Coordinates(20, 100), a, "intArray",
        null, aprops);

    lang.nextStep();

    SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, scprops);

    sc.addCodeLine("public void sort(int[] array)", null, 0, null); // 0
    sc.addCodeLine("{", null, 0, null); // 1
    sc.addCodeLine("boolean swapped;", null, 1, null); // 2
    sc.addCodeLine("int i;", null, 1, null); // 3
    sc.addCodeLine("do", null, 1, null); // 4
    sc.addCodeLine("{", null, 1, null); // 5
    sc.addCodeLine("swapped = false;", null, 2, null); // 6
    sc.addCodeLine("for (i = 0; i <= A.length - 2; i++)", null, 2, null); // 7
    sc.addCodeLine("{", null, 2, null); // 8
    sc.addCodeLine("if (A[i] > A[i+1])", null, 3, null); // 9
    sc.addCodeLine("{", null, 3, null); // 10
    sc.addCodeLine("swap(array, i, i+1);", null, 4, null); // 11
    sc.addCodeLine("swapped = true;", null, 4, null); // 12
    sc.addCodeLine("}", null, 3, null); // 13
    sc.addCodeLine("}", null, 2, null); // 14
    sc.addCodeLine("if (swapped == false)", null, 2, null); // 15
    sc.addCodeLine("break;", null, 3, null); // 16
    sc.addCodeLine("swapped = false;", null, 2, null); // 17
    sc.addCodeLine("for (i = 0; i <= A.length - 2; i++)", null, 2, null); // 18
    sc.addCodeLine("{", null, 2, null); // 19
    sc.addCodeLine("if (A[i] > A[i+1])", null, 3, null); // 20
    sc.addCodeLine("{", null, 3, null); // 21
    sc.addCodeLine("swap(array, i, i+1);", null, 4, null); // 22
    sc.addCodeLine("swapped = true;", null, 4, null); // 23
    sc.addCodeLine("}", null, 3, null); // 24
    sc.addCodeLine("}", null, 2, null); // 25
    sc.addCodeLine("}", null, 1, null); // 26
    sc.addCodeLine("while (swapped);", null, 0, null); // 27
    sc.addCodeLine("}", null, 0, null); // 28

    lang.nextStep();
    ia.highlightCell(0, ia.getLength() - 1, null, null);
    try {
      shakeSort(ia, sc, amprops_i, amprops_j);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  private void shakeSort(IntArray array, SourceCode codeSupport,
      ArrayMarkerProperties amprops_i, ArrayMarkerProperties amprops_j) {
    codeSupport.highlight(0, 0, false);
    lang.nextStep();

    codeSupport.toggleHighlight(0, 1);
    lang.nextStep();

    codeSupport.toggleHighlight(1, 2);
    lang.nextStep();

    codeSupport.toggleHighlight(2, 3);
    lang.nextStep();

    codeSupport.toggleHighlight(3, 4);
    lang.nextStep();

    codeSupport.toggleHighlight(4, 5);
    lang.nextStep();

    codeSupport.toggleHighlight(5, 6);
    lang.nextStep();

    codeSupport.unhighlight(6);

    ArrayMarker iMarker = lang.newArrayMarker(array, 1, "i", null, amprops_i);

    ArrayMarker jMarker = lang.newArrayMarker(array, array.getLength() - 2,
        "j", null, amprops_j);
    jMarker.hide();

    boolean swapped;

    do {
      swapped = false;
      iMarker.show();
      for (int i = 0; i <= array.getLength() - 2; i++) {
        codeSupport.highlight(7);
        lang.nextStep();
        codeSupport.toggleHighlight(7, 9);
        array.highlightElem(i, null, null);
        array.highlightElem(i + 1, null, null);
        lang.nextStep();
        if (array.getData(i) > array.getData(i + 1)) {
          codeSupport.toggleHighlight(9, 11);
          codeSupport.highlight(12);
          array.swap(i, i + 1, null, new TicksTiming(15));
          swapped = true;
          lang.nextStep();
          iMarker.increment(new TicksTiming(25), new TicksTiming(20));
          codeSupport.unhighlight(11);
          codeSupport.unhighlight(12);
          array.unhighlightElem(i, null, null);
          array.unhighlightElem(i + 1, null, null);
        } else {
          codeSupport.unhighlight(9);
          array.unhighlightElem(i, null, null);
          iMarker.increment(null, new TicksTiming(20));
          array.unhighlightElem(i + 1, null, null);
        }
      }
      iMarker.move(1, null, new TicksTiming(15));

      codeSupport.highlight(15);
      lang.nextStep();
      if (!swapped) {
        codeSupport.toggleHighlight(15, 16);
        lang.nextStep();
        codeSupport.unhighlight(16);
        break;
      } else
        codeSupport.unhighlight(15);
      codeSupport.highlight(17);
      swapped = false;
      lang.nextStep();

      jMarker.show();
      for (int j = array.getLength() - 2; j >= 0; j--) {
        codeSupport.toggleHighlight(17, 18);
        lang.nextStep();
        codeSupport.toggleHighlight(18, 20);
        array.highlightElem(j, null, null);
        array.highlightElem(j + 1, null, null);
        lang.nextStep();
        if (array.getData(j) > array.getData(j + 1)) {
          codeSupport.toggleHighlight(20, 22);
          codeSupport.highlight(23);
          array.swap(j, j + 1, null, new TicksTiming(15));
          swapped = true;
          lang.nextStep();
          jMarker.decrement(new TicksTiming(25), new TicksTiming(20));
          codeSupport.unhighlight(22);
          codeSupport.unhighlight(23);
          array.unhighlightElem(j, null, null);
          array.unhighlightElem(j + 1, null, null);
        } else {
          codeSupport.unhighlight(20);
          array.unhighlightElem(j, null, null);
          jMarker.decrement(null, new TicksTiming(20));
          array.unhighlightElem(j + 1, null, null);
        }
      }
      jMarker.move(array.getLength() - 2, null, new TicksTiming(15));
    } while (swapped);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();
    int[] ia = (int[]) primitives.get("Input Data");
    ArrayProperties ap = (ArrayProperties) props
        .getPropertiesByName("arrayProps");
    SourceCodeProperties scp = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");
    ArrayMarkerProperties amp_i = (ArrayMarkerProperties) props
        .getPropertiesByName("iMarkerProps");
    ArrayMarkerProperties amp_j = (ArrayMarkerProperties) props
        .getPropertiesByName("jMarkerProps");
    System.out.println(ia);
    sort(ia, ap, scp, amp_i, amp_j);
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Shaker Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Vyacheslav Polonskyy";
  }

  @Override
  public String getCodeExample() {
    return "procedure cocktailSort( A : list of sortable items ) defined as:"
        + "\n"
        + "  do"
        + "\n"
        + "    swapped := false"
        + "\n"
        + "	 for each i in 0 to length( A ) - 2 do:"
        + "\n"
        + "  		if A[ i ] > A[ i + 1 ] then // test whether the two elements are in the wrong order"
        + "\n"
        + " 		 	swap( A[ i ], A[ i + 1 ] ) // let the two elements change places"
        + "\n"
        + "  			swapped := true"
        + "\n"
        + " 		end if"
        + "\n"
        + "	 end for"
        + "\n"
        + "  	 if swapped = false then"
        + "\n"
        + "		// we can exit the outer loop here if no swaps occurred."
        + "\n"
        + "		break do-while loop"
        + "\n"
        + "	 end if"
        + "\n"
        + "	 swapped := false"
        + "\n"
        + "	 for each i in length( A ) - 2 to 0 do:"
        + "\n"
        + "		if A[ i ] > A[ i + 1 ] then"
        + "\n"
        + "			swap( A[ i ], A[ i + 1 ] )"
        + "\n"
        + "			swapped := true"
        + "\n"
        + "		end if"
        + "\n"
        + "	 end for"
        + "\n"
        + "  while swapped // if no elements have been swapped, then the list is sorted"
        + "\n" + "end procedure";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Das zu sortierende Feld wird abwechselnd nach oben und nach unten durchlaufen. Dabei werden jeweils zwei benachbarte Elemente verglichen und gegebenenfalls vertauscht.Durch diese Bidirektionalität kommt es zu einem schnellerem Absetzen von großen bzw. kleinen Elementen. Anhand des Sortierverfahrens lässt sich auch der Name erklären, denn der Sortiervorgang erinnert an das Schütteln des Arrays oder eines Barmixers.";
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
    return "Shaker Sort";
  }

  @Override
  public String getOutputLanguage() {
    return generators.framework.Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Shakersort Animation", "Vyacheslav Polonskyy",
        640, 480);
    lang.setStepMode(true);
  }
}
