package generators;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.sorting.selectionsort.GenericAnnotatedSelectionSort;

import java.util.Locale;
import java.util.Vector;

public class SelectionSortGeneratorBundle implements GeneratorBundle {
  public Vector<Generator> getGenerators() {
    Vector<Generator> result = new Vector<Generator>(120);

    // Selection Sort Type I, English, Java
    result.add(new GenericAnnotatedSelectionSort("resources/JavaSelectionSort", Locale.US));
    
    // Selection Sort Type I, English, Pseudo-Code
    result.add(new GenericAnnotatedSelectionSort("resources/PseudoSelectionSort", Locale.US));
    
    // Selection Sort Type I, German, Java
    result.add(new GenericAnnotatedSelectionSort("resources/JavaSelectionSort", Locale.GERMANY));
    
    // Selection Sort Type I, German, Pseudo-Code
    result.add(new GenericAnnotatedSelectionSort("resources/PseudoSelectionSort", Locale.GERMANY));

    return result;
  }

}
