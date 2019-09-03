package generators;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.sorting.bubblesort.GenericAnnotatedBubbleSort;

import java.util.Locale;
import java.util.Vector;

public class BubbleSortGeneratorBundle implements GeneratorBundle {
  public Vector<Generator> getGenerators() {
    Vector<Generator> result = new Vector<Generator>(120);

    // Basic Bubble Sort
    //   Java version
    //     English
    result.add(new GenericAnnotatedBubbleSort("resources/JavaBubbleSort", Locale.US));
    //     German
    result.add(new GenericAnnotatedBubbleSort("resources/JavaBubbleSort", Locale.GERMANY));

    //   Pseudo-Code
    //     English
    result.add(new GenericAnnotatedBubbleSort("resources/PseudoBubbleSort", Locale.US));
    //     German
    result.add(new GenericAnnotatedBubbleSort("resources/PseudoBubbleSort", Locale.GERMANY));
//    //     German
//    result.add(new GenericAnnotatedBubbleSort("resources/DelphiBubbleSort", Locale.GERMANY));

    // TODO: check if actually matches!
//    // GdI 2 version
//    //   English, Java
//    result.add(new GenericAnnotatedBubbleSort("resources/JavaGdI2BubbleSort", Locale.US));
//    //   German, Java
//    result.add(new GenericAnnotatedBubbleSort("resources/JavaGdI2BubbleSort", Locale.GERMANY));
//
//    // Improved GdI 2 version
//    //   English, Java
//    result.add(new GenericAnnotatedBubbleSort("resources/JavaGdI2ImprovedBubbleSort", Locale.US));
//    //   German, Java
//    result.add(new GenericAnnotatedBubbleSort("resources/JavaGdI2ImprovedBubbleSort", Locale.GERMANY));
    
    return result;
  }

}
