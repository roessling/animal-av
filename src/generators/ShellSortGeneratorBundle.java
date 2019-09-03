package generators;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.sorting.GenericAnnotatedShellSort;

import java.util.Locale;
import java.util.Vector;

public class ShellSortGeneratorBundle implements GeneratorBundle {
  public Vector<Generator> getGenerators() {
    Vector<Generator> result = new Vector<Generator>(120);

    // ShellSort Type I, English, Java
    result.add(new GenericAnnotatedShellSort("resources/JavaShellSort", Locale.US));
    
    // ShellSort Type I, English, Pseudo-Code
    result.add(new GenericAnnotatedShellSort("resources/PseudoShellSort", Locale.US));

    // ShellSort Type I, German, Java
    result.add(new GenericAnnotatedShellSort("resources/JavaShellSort", Locale.GERMANY));
    
    // ShellSort Type I, German, Pseudo-Code
    result.add(new GenericAnnotatedShellSort("resources/PseudoShellSort", Locale.GERMANY));
    
    return result;
  }

}
