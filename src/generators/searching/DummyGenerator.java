package generators.searching;

import java.util.Locale;
import java.util.Vector;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.searching.alphabeta.AlphaBeta;
import generators.searching.alphabeta.AlphaBetaSearch;
import generators.searching.binarysearching.GenericIterativeBinaryStringSearching;
import generators.searching.binarysearching.GenericRecursiveBinaryIntSearching;
import generators.searching.binarysearching.GenericRecursiveBinaryIntSearchingWithCounter;
import generators.searching.binarysearching.GenericRecursiveBinaryStringSearching;
import generators.searching.boyermoore.BoyerMoore;
import generators.searching.bruteforce.BruteForceStringSearching;
import generators.searching.expectimax.ExpectimaxGenerator;
import generators.searching.horspool.Horspool;
import generators.searching.horspool.HorspoolGenerator;
import generators.searching.horspool.HorspoolRaita;
import generators.searching.horspool.Sunday;
import generators.searching.interpolatedsearching.InterpolationSearchWrapper;
import generators.searching.kmp.KMP;
import generators.searching.kmp.KMPAnnotated;
import generators.searching.kmp.KMPGenerator;
import generators.searching.kmp.KMPLPeng;
import generators.searching.kmp.KnuthMorrisPratt;
import generators.searching.kmp.KnuthMorrisPrattSearch;
import generators.searching.kmp.KnuthMorrisPrattStringSearchWrapper;
import generators.searching.minmax.MinMaxGenerator;
import generators.searching.sss.SSSStar;
import generators.searching.straightselection.GenericIterativeStraightIntSelection;
import generators.searching.straightselection.GenericIterativeStraightIntSelectionWithCounter;
import generators.searching.straightselection.GenericIterativeStraightStringSelection;
import generators.searching.straightselection.GenericRecursiveStraightIntSelection;
import generators.searching.straightselection.GenericRecursiveStraightStringSelection;
import generators.searching.tabusearch.TabuSearchGenerator;
import generators.searching.topk.FaginsAlgorithm;
import generators.searching.topk.ThresholdAlgorithm;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new AStarGenerator());

    // BruteForceStringSearching, German
    generators.add(new BruteForceStringSearching(
        "resources/JavaBruteForceStringSearching", Locale.GERMANY));

    // binary searching -- Java code

    // binary searching, Java, iterative, type String, English
    generators.add(new GenericIterativeBinaryStringSearching(
        "resources/JavaIterativeBinaryStringSearching", Locale.US));
    // binary searching, Java, iterative, type String, German
    generators.add(new GenericIterativeBinaryStringSearching(
        "resources/JavaIterativeBinaryStringSearching", Locale.GERMANY));

    // binary searching, Java, recursive, type int, English
    generators.add(new GenericRecursiveBinaryIntSearching(
        "resources/JavaRecursiveBinaryIntSearching", Locale.US));
    // binary searching, Java, recursive, type int, German
    generators.add(new GenericRecursiveBinaryIntSearching(
        "resources/JavaRecursiveBinaryIntSearching", Locale.GERMANY));

    // binary searching, Java, recursive, type String, English
    generators.add(new GenericRecursiveBinaryStringSearching(
        "resources/JavaRecursiveBinaryStringSearching", Locale.US));
    // binary searching, Java, recursive, type String, German
    generators.add(new GenericRecursiveBinaryStringSearching(
        "resources/JavaRecursiveBinaryStringSearching", Locale.GERMANY));

    // binary searching - Pseudo code

    // binary searching, Pseudo Code, iterative, type String, English
    generators.add(new GenericIterativeBinaryStringSearching(
        "resources/PseudoIterativeBinaryStringSearching", Locale.US));
    // binary searching, Pseudo Code, iterative, type String, German
    generators.add(new GenericIterativeBinaryStringSearching(
        "resources/PseudoIterativeBinaryStringSearching", Locale.GERMANY));

    // binary searching, Pseudo Code, recursive, type int, English
    generators.add(new GenericRecursiveBinaryIntSearching(
        "resources/PseudoRecursiveBinaryIntSearching", Locale.US));
    // binary searching, Pseudo Code, recursive, type int, German
    generators.add(new GenericRecursiveBinaryIntSearching(
        "resources/PseudoRecursiveBinaryIntSearching", Locale.GERMANY));

    // binary searching, Pseudo Code, recursive, type String, English
    generators.add(new GenericRecursiveBinaryStringSearching(
        "resources/PseudoRecursiveBinaryStringSearching", Locale.US));
    // binary searching, Pseudo Code, recursive, type String, German
    generators.add(new GenericRecursiveBinaryStringSearching(
        "resources/PseudoRecursiveBinaryStringSearching", Locale.GERMANY));

    // linear searching -- Java code

    // linear searching, Java, iterative, type int, English
    generators.add(new GenericIterativeStraightIntSelection(
        "resources/JavaIterativeStraightIntSelection", Locale.US));
    // linear searching, Java, iterative, type int, German
    generators.add(new GenericIterativeStraightIntSelection(
        "resources/JavaIterativeStraightIntSelection", Locale.GERMANY));

    // linear searching, Java, iterative, type String, English
    generators.add(new GenericIterativeStraightStringSelection(
        "resources/JavaIterativeStraightStringSelection", Locale.US));
    // linear searching, Java, iterative, type String, German
    generators.add(new GenericIterativeStraightStringSelection(
        "resources/JavaIterativeStraightStringSelection", Locale.GERMANY));

    // linear searching, Java, recursive, type int, English
    generators.add(new GenericRecursiveStraightIntSelection(
        "resources/JavaRecursiveStraightIntSelection", Locale.US));
    // linear searching, Java, recursive, type int, German
    generators.add(new GenericRecursiveStraightIntSelection(
        "resources/JavaRecursiveStraightIntSelection", Locale.GERMANY));

    // linear searching, Java, recursive, type String, English
    generators.add(new GenericRecursiveStraightStringSelection(
        "resources/JavaRecursiveStraightStringSelection", Locale.US));
    // linear searching, Java, recursive, type String, German
    generators.add(new GenericRecursiveStraightStringSelection(
        "resources/JavaRecursiveStraightStringSelection", Locale.GERMANY));

    // linear searching - Pseudo code

    // linear searching, Pseudo Code, iterative, type int, English
    generators.add(new GenericIterativeStraightIntSelection(
        "resources/PseudoIterativeStraightIntSelection", Locale.US));
    // linear searching, Pseudo Code, iterative, type int, German
    generators.add(new GenericIterativeStraightIntSelection(
        "resources/PseudoIterativeStraightIntSelection", Locale.GERMANY));

    // linear searching, Pseudo Code, iterative, type String, English
    generators.add(new GenericIterativeStraightStringSelection(
        "resources/PseudoIterativeStraightStringSelection", Locale.US));
    // linear searching, Pseudo Code, iterative, type String, German
    generators.add(new GenericIterativeStraightStringSelection(
        "resources/PseudoIterativeStraightStringSelection", Locale.GERMANY));

    // linear searching, Pseudo Code, recursive, type int, English
    generators.add(new GenericRecursiveStraightIntSelection(
        "resources/PseudoRecursiveStraightIntSelection", Locale.US));
    // linear searching, Pseudo Code, recursive, type int, German
    generators.add(new GenericRecursiveStraightIntSelection(
        "resources/PseudoRecursiveStraightIntSelection", Locale.GERMANY));

    // linear searching, Pseudo Code, recursive, type String, English
    generators.add(new GenericRecursiveStraightStringSelection(
        "resources/PseudoRecursiveStraightStringSelection", Locale.US));
    // linear searching, Pseudo Code, recursive, type String, German
    generators.add(new GenericRecursiveStraightStringSelection(
        "resources/PseudoRecursiveStraightStringSelection", Locale.GERMANY));

    generators.add(new InterpolationSearchWrapper());
    generators.add(new KMP());
    generators.add(new KMPAnnotated());
    generators.add(new KnuthMorrisPratt());
    generators.add(new KnuthMorrisPrattStringSearchWrapper());
    generators.add(new Levenstein());
    // generators.add(new SequentielleSuche());
    generators.add(new KMPLPeng());

    // Generators from the AlgoAnim course in summer semester 2011.
    generators.add(new Levenshtein2011());
    generators.add(new KMPGenerator());
    generators.add(new AlphaBetaSearch());
    generators.add(new RabinKarpAlgorithm());
    generators.add(new Kadane());
    generators.add(new RepetitionMatcher());

    // Generators with Counter

    // linear searching, Java, iterative, type int, English
    generators.add(new GenericIterativeStraightIntSelectionWithCounter(
        "resources/JavaIterativeStraightIntSelection", Locale.US));
    // linear searching, Java, iterative, type int, German
    generators.add(new GenericIterativeStraightIntSelectionWithCounter(
        "resources/JavaIterativeStraightIntSelection", Locale.GERMANY));
    // binary searching, Java, recursive, type int, English
    generators.add(new GenericRecursiveBinaryIntSearchingWithCounter(
        "resources/JavaRecursiveBinaryIntSearching", Locale.US));
    // binary searching, Java, recursive, type int, German
    generators.add(new GenericRecursiveBinaryIntSearchingWithCounter(
        "resources/JavaRecursiveBinaryIntSearching", Locale.GERMANY));

    // TODO "under probation"
    // generators.add(new AnnotatedKMPMatching());
    // generators.add(new BoyerMooreStringSearchWrapper());
    // generators.add(new BruteForceStringSearchWrapper());
    // generators.add(new GenericIterativeBinaryIntSearching());
    // generators.add(new GenericIterativeInterpolatedIntSearching());
    // generators.add(new GenericRecursiveInterpolatedIntSearching());
    // generators.add(new Levenshteinmatrix());
    // generators.add(new SequentielleSuche());

    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new Bullyalgorithmus());
    generators.add(new ElectionOnRings());
    generators.add(new UCSGenerator());
    generators.add(new JumpSearchAlgorithm());
    generators.add(new HorspoolGenerator());
    generators.add(new StringMatchingFiniteStateMachine());

    // Generators from BSc Thesis Torsten Kohlhaas
    generators.add(new BoyerMoore("resources/JavaBoyerMoore", Locale.GERMANY));
    generators.add(new BoyerMoore("resources/JavaBoyerMoore", Locale.US));
    generators.add(new Horspool("resources/JavaHorspool", Locale.GERMANY));
    generators.add(new Horspool("resources/JavaHorspool", Locale.US));
    generators.add(new HorspoolRaita("resources/JavaHorspoolRaita",
        Locale.GERMANY));
    generators.add(new HorspoolRaita("resources/JavaHorspoolRaita", Locale.US));
    generators.add(new KnuthMorrisPrattSearch(
        "resources/JavaKnuthMorrisPrattSearch", Locale.GERMANY));
    generators.add(new KnuthMorrisPrattSearch(
        "resources/JavaKnuthMorrisPrattSearch", Locale.US));
    generators.add(new LinearStringSearch("resources/JavaLinearStringSearch",
        Locale.GERMANY));
    generators.add(new LinearStringSearch("resources/JavaLinearStringSearch",
        Locale.US));
    generators.add(new RabinKarp("resources/JavaRabinKarp", Locale.GERMANY));
    generators.add(new RabinKarp("resources/JavaRabinKarp", Locale.US));
    generators.add(new Sunday("resources/JavaSunday", Locale.GERMANY));
    generators.add(new Sunday("resources/JavaSunday", Locale.US));

    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new Logclockmutexgenerator());
    generators.add(new AlphaBeta()); // Exzellent.
    generators.add(new IterativeTiefensuche());
    generators.add(new PUSGenerator());
    generators.add(new SkipSearchGenerator()); // Exzellent.
    generators.add(new Bitap2());
    generators.add(new Negascout()); // Exzellent. Vorbildlich.
    generators.add(new IDAStar()); // Exzellent.

    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new MinMaxGenerator());
    generators.add(new TSPviaGBFGenerator());  // Bringt viele Informationen an einem einzigen Graphen an.
    generators.add(new ExpectimaxGenerator());  // Ansehnliche Bäume.
    generators.add(new FaginsAlgorithm("resources/FaginsAlgorithm", Locale.US));  // Gut.
    generators.add(new FaginsAlgorithm("resources/FaginsAlgorithm", Locale.GERMANY));  // v.s.
    generators.add(new ThresholdAlgorithm("resources/ThresholdAlgorithm", Locale.US));  // v.s.
    generators.add(new ThresholdAlgorithm("resources/ThresholdAlgorithm", Locale.GERMANY));  // v.s.
    generators.add(new SelectionSearch());  // Gut.
    
    // Generators from the AlgoAnim course in summer semester 2015.
    generators.add(new LocalSearchTSP("resources/LocalSearchTSP", Locale.US)); // sehr gut
    generators.add(new LocalSearchTSP("resources/LocalSearchTSP", Locale.GERMANY)); // sehr gut
    generators.add(new SimulatedAnnealingGenerator());

    // Generators from the AlgoAnim course in summer term 2016.
    generators.add(new Pledge());
    generators.add(new QuickselectGenerator());
    generators.add(new SimpleStringMatching());

    // Generators from the AlgoAnim course in summer term 2017.
    generators.add(new FibonacciSearch());
    generators.add(new NegaMax("resources/NegaMax", Locale.US));
    generators.add(new NegaMax("resources/NegaMax", Locale.GERMANY));

    
    // Generators from the AlgoAnim course in summer term 2019.
    generators.add(new Beweiszahlsuche());
    generators.add(new Generator_ExponentialSearch());
    generators.add(new HillClimb(Locale.GERMANY));
    generators.add(new HillClimb(Locale.US));
    generators.add(new JumpPointSearch(Locale.GERMANY));
    generators.add(new JumpPointSearch(Locale.US));
    generators.add(new QueensAnnealingGenerator(Locale.GERMANY));
    generators.add(new QueensAnnealingGenerator(Locale.US));
    generators.add(new SSSStar("resources/SSSStar", Locale.GERMANY));
    generators.add(new SSSStar("resources/SSSStar", Locale.US));
    generators.add(new SublistSearchGenerator());
    generators.add(new TabuSearchGenerator());
    generators.add(new ZAlgorithmGenerator());
    
    return generators;
  }
}
