package generators;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.sorting.quicksort.GenericAnnotatedQuickSort;

import java.util.Locale;
import java.util.Vector;

public class QuickSortGeneratorBundle implements GeneratorBundle {
  public Vector<Generator> getGenerators() {
    Vector<Generator> result = new Vector<Generator>(120);

    // QuickSort Type I, English, Java
    result.add(new GenericAnnotatedQuickSort("resources/JavaQuickSort", Locale.US));
    
    // QuickSort Type I, English, Pseudo-Code
    result.add(new GenericAnnotatedQuickSort("resources/PseudoQuickSort", Locale.US));

    // QuickSort Type I, German, Java
    result.add(new GenericAnnotatedQuickSort("resources/JavaQuickSort", Locale.GERMANY));
    
    // QuickSort Type I, German, Pseudo-Code
    result.add(new GenericAnnotatedQuickSort("resources/PseudoQuickSort", Locale.GERMANY));

    return result;
  }

}
