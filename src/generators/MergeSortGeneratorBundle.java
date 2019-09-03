package generators;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.sorting.GenericAnnotatedMergeSort;

import java.util.Locale;
import java.util.Vector;

public class MergeSortGeneratorBundle implements GeneratorBundle {
  public Vector<Generator> getGenerators() {
    Vector<Generator> result = new Vector<Generator>(120);

    // Merge Sort Type I, English, Java
    result.add(new GenericAnnotatedMergeSort("resources/JavaMergeSort", Locale.US));

    // Merge Sort Type I, English, Pseudo-Code
    result.add(new GenericAnnotatedMergeSort("resources/PseudoMergeSort", Locale.US));

    // Merge Sort Type I, German, Java
    result.add(new GenericAnnotatedMergeSort("resources/JavaMergeSort", Locale.GERMANY));

    // Merge Sort Type I, German, Pseudo-Code
    result.add(new GenericAnnotatedMergeSort("resources/PseudoMergeSort", Locale.GERMANY));

    return result;
  }

}
