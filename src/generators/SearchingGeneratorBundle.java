package generators;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.searching.binarysearching.GenericIterativeBinaryIntSearching;
import generators.searching.binarysearching.GenericIterativeBinaryStringSearching;
import generators.searching.binarysearching.GenericRecursiveBinaryIntSearching;
import generators.searching.binarysearching.GenericRecursiveBinaryStringSearching;
import generators.searching.bruteforce.BruteForceStringSearching;
import generators.searching.interpolatedsearching.GenericIterativeInterpolatedIntSearching;
import generators.searching.interpolatedsearching.GenericRecursiveInterpolatedIntSearching;
import generators.searching.straightselection.GenericIterativeStraightIntSelection;
import generators.searching.straightselection.GenericIterativeStraightStringSelection;
import generators.searching.straightselection.GenericRecursiveStraightIntSelection;
import generators.searching.straightselection.GenericRecursiveStraightStringSelection;

import java.util.Locale;
import java.util.Vector;

public class SearchingGeneratorBundle implements GeneratorBundle {
  public Vector<Generator> getGenerators() {
    Vector<Generator> result = new Vector<Generator>(120);
    // BruteForceStringSearching, German
    result.add(new BruteForceStringSearching(
        "resources/JavaBruteForceStringSearching", Locale.GERMANY));
    
    // binary searching -- Java code
    
    // binary searching, Java, iterative, type int, English
    result.add(new GenericIterativeBinaryIntSearching(
        "resources/JavaIterativeBinaryIntSearching", Locale.US));
    // binary searching, Java, iterative, type int, German
    result.add(new GenericIterativeBinaryIntSearching(
        "resources/JavaIterativeBinaryIntSearching", Locale.GERMANY));

    // binary searching, Java, iterative, type String, English
    result.add(new GenericIterativeBinaryStringSearching(
        "resources/JavaIterativeBinaryStringSearching", Locale.US));
    // binary searching, Java, iterative, type String, German
    result.add(new GenericIterativeBinaryStringSearching(
        "resources/JavaIterativeBinaryStringSearching", Locale.GERMANY));

    // binary searching, Java, recursive, type int, English
    result.add(new GenericRecursiveBinaryIntSearching(
        "resources/JavaRecursiveBinaryIntSearching", Locale.US));
    // binary searching, Java, recursive, type int, German
    result.add(new GenericRecursiveBinaryIntSearching(
        "resources/JavaRecursiveBinaryIntSearching", Locale.GERMANY));

    // binary searching, Java, recursive, type String, English
    result.add(new GenericRecursiveBinaryStringSearching(
        "resources/JavaRecursiveBinaryStringSearching", Locale.US));
    // binary searching, Java, recursive, type String, German
    result.add(new GenericRecursiveBinaryStringSearching(
        "resources/JavaRecursiveBinaryStringSearching", Locale.GERMANY));

    // binary searching - Pseudo code
    
    // binary searching, Pseudo Code, iterative, type int, English
    result.add(new GenericIterativeBinaryIntSearching(
        "resources/PseudoIterativeBinaryIntSearching", Locale.US));
    // binary searching, Pseudo Code, iterative, type int, German
    result.add(new GenericIterativeBinaryIntSearching(
        "resources/PseudoIterativeBinaryIntSearching", Locale.GERMANY));

    // binary searching, Pseudo Code, iterative, type String, English
    result.add(new GenericIterativeBinaryStringSearching(
        "resources/PseudoIterativeBinaryStringSearching", Locale.US));
    // binary searching, Pseudo Code, iterative, type String, German
    result.add(new GenericIterativeBinaryStringSearching(
        "resources/PseudoIterativeBinaryStringSearching", Locale.GERMANY));

    // binary searching, Pseudo Code, recursive, type int, English
    result.add(new GenericRecursiveBinaryIntSearching(
        "resources/PseudoRecursiveBinaryIntSearching", Locale.US));
    // binary searching, Pseudo Code, recursive, type int, German
    result.add(new GenericRecursiveBinaryIntSearching(
        "resources/PseudoRecursiveBinaryIntSearching", Locale.GERMANY));

    // binary searching, Pseudo Code, recursive, type String, English
    result.add(new GenericRecursiveBinaryStringSearching(
        "resources/PseudoRecursiveBinaryStringSearching", Locale.US));
    // binary searching, Pseudo Code, recursive, type String, German
    result.add(new GenericRecursiveBinaryStringSearching(
        "resources/PseudoRecursiveBinaryStringSearching", Locale.GERMANY));
    
    // linear searching -- Java code
    
    // linear searching, Java, iterative, type int, English
    result.add(new GenericIterativeStraightIntSelection(
        "resources/JavaIterativeStraightIntSelection", Locale.US));
    // linear searching, Java, iterative, type int, German
    result.add(new GenericIterativeStraightIntSelection(
        "resources/JavaIterativeStraightIntSelection", Locale.GERMANY));

    // linear searching, Java, iterative, type String, English
    result.add(new GenericIterativeStraightStringSelection(
        "resources/JavaIterativeStraightStringSelection", Locale.US));
    // linear searching, Java, iterative, type String, German
    result.add(new GenericIterativeStraightStringSelection(
        "resources/JavaIterativeStraightStringSelection", Locale.GERMANY));

    // linear searching, Java, recursive, type int, English
    result.add(new GenericRecursiveStraightIntSelection(
        "resources/JavaRecursiveStraightIntSelection", Locale.US));
    // linear searching, Java, recursive, type int, German
    result.add(new GenericRecursiveStraightIntSelection(
        "resources/JavaRecursiveStraightIntSelection", Locale.GERMANY));

    // linear searching, Java, recursive, type String, English
    result.add(new GenericRecursiveStraightStringSelection(
        "resources/JavaRecursiveStraightStringSelection", Locale.US));
    // linear searching, Java, recursive, type String, German
    result.add(new GenericRecursiveStraightStringSelection(
        "resources/JavaRecursiveStraightStringSelection", Locale.GERMANY));

    // linear searching - Pseudo code
    
    // linear searching, Pseudo Code, iterative, type int, English
    result.add(new GenericIterativeStraightIntSelection(
        "resources/PseudoIterativeStraightIntSelection", Locale.US));
    // linear searching, Pseudo Code, iterative, type int, German
    result.add(new GenericIterativeStraightIntSelection(
        "resources/PseudoIterativeStraightIntSelection", Locale.GERMANY));

    // linear searching, Pseudo Code, iterative, type String, English
    result.add(new GenericIterativeStraightStringSelection(
        "resources/PseudoIterativeStraightStringSelection", Locale.US));
    // linear searching, Pseudo Code, iterative, type String, German
    result.add(new GenericIterativeStraightStringSelection(
        "resources/PseudoIterativeStraightStringSelection", Locale.GERMANY));

    // linear searching, Pseudo Code, recursive, type int, English
    result.add(new GenericRecursiveStraightIntSelection(
        "resources/PseudoRecursiveStraightIntSelection", Locale.US));
    // linear searching, Pseudo Code, recursive, type int, German
    result.add(new GenericRecursiveStraightIntSelection(
        "resources/PseudoRecursiveStraightIntSelection", Locale.GERMANY));

    // linear searching, Pseudo Code, recursive, type String, English
    result.add(new GenericRecursiveStraightStringSelection(
        "resources/PseudoRecursiveStraightStringSelection", Locale.US));
    // linear searching, Pseudo Code, recursive, type String, German
    result.add(new GenericRecursiveStraightStringSelection(
        "resources/PseudoRecursiveStraightStringSelection", Locale.GERMANY));
   
    // interpolated search -- Java code
    
    // interpolated searching, Java, iterative, type int, English
    result.add(new GenericIterativeInterpolatedIntSearching(
        "resources/JavaIterativeInterpolatedIntSearching", Locale.US));
    // interpolated searching, Java, iterative, type int, German
    result.add(new GenericIterativeInterpolatedIntSearching(
        "resources/JavaIterativeInterpolatedIntSearching", Locale.GERMANY));

    // interpolated searching, Java, recursive, type int, English
    result.add(new GenericRecursiveInterpolatedIntSearching(
        "resources/JavaRecursiveInterpolatedIntSearching", Locale.US));
    // interpolated searching, Java, recursive, type int, German
    result.add(new GenericRecursiveInterpolatedIntSearching(
        "resources/JavaRecursiveInterpolatedIntSearching", Locale.GERMANY));

    // linear searching - Pseudo code
    
    // interpolated searching, Pseudo Code, iterative, type int, English
    result.add(new GenericIterativeInterpolatedIntSearching(
        "resources/PseudoIterativeInterpolatedIntSearching", Locale.US));
    // interpolated searching, Pseudo Code, iterative, type int, German
    result.add(new GenericIterativeInterpolatedIntSearching(
        "resources/PseudoIterativeInterpolatedIntSearching", Locale.GERMANY));

    // interpolated searching, Pseudo Code, recursive, type int, English
    result.add(new GenericRecursiveInterpolatedIntSearching(
        "resources/PseudoRecursiveInterpolatedIntSearching", Locale.US));
    // interpolated searching, Pseudo Code, recursive, type int, German
    result.add(new GenericRecursiveInterpolatedIntSearching(
        "resources/PseudoRecursiveInterpolatedIntSearching", Locale.GERMANY));
 
    return result;
  }
}
