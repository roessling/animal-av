package generators;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.sorting.insertionsort.GenericAnnotatedInsertionSort;
import generators.sorting.insertionsort.GenericAnnotatedInsertionSort2;
import generators.sorting.insertionsort.GenericAnnotatedInsertionSort3;

import java.util.Locale;
import java.util.Vector;

public class InsertionSortGeneratorBundle implements GeneratorBundle {
  public Vector<Generator> getGenerators() {
    Vector<Generator> result = new Vector<Generator>(120);

    // Insertion Sort Type I, English, Java
    result.add(new GenericAnnotatedInsertionSort("resources/JavaInsertionSort", Locale.US));
    
    // Insertion Sort Type I, English, Pseudo-Code
    result.add(new GenericAnnotatedInsertionSort("resources/PseudoInsertionSort", Locale.US));
    
    // Insertion Sort Type II, English, Java
    result.add(new GenericAnnotatedInsertionSort2("resources/JavaInsertionSort2", Locale.US));

    // Insertion Sort Type II, English, Pseudo-Code
    result.add(new GenericAnnotatedInsertionSort2("resources/PseudoInsertionSort2", Locale.US));

    // Insertion Sort Type III, English, Java
    result.add(new GenericAnnotatedInsertionSort3("resources/JavaInsertionSort3", Locale.US));

    // Insertion Sort Type III, English, Pseudo-Code
    result.add(new GenericAnnotatedInsertionSort3("resources/PseudoInsertionSort3", Locale.US));
    
    // Insertion Sort Type I, German, Java
    result.add(new GenericAnnotatedInsertionSort("resources/JavaInsertionSort", Locale.GERMANY));
    
    // Insertion Sort Type I, German, Pseudo-Code
    result.add(new GenericAnnotatedInsertionSort("resources/PseudoInsertionSort", Locale.GERMANY));
    
    // Insertion Sort Type II, German, Java
    result.add(new GenericAnnotatedInsertionSort2("resources/JavaInsertionSort2", Locale.GERMANY));

    // Insertion Sort Type II, German, Pseudo-Code
    result.add(new GenericAnnotatedInsertionSort2("resources/PseudoInsertionSort2", Locale.GERMANY));

    // Insertion Sort Type III, German, Java
    result.add(new GenericAnnotatedInsertionSort3("resources/JavaInsertionSort3", Locale.GERMANY));

    // Insertion Sort Type III, German, Pseudo-Code
    result.add(new GenericAnnotatedInsertionSort3("resources/PseudoInsertionSort3", Locale.GERMANY));
    
 // Insertion Sort Type I, Bulgarian, Java
    result.add(new GenericAnnotatedInsertionSort("resources/JavaInsertionSort", new Locale("bg", "BG")));
    
    return result;
  }

}
