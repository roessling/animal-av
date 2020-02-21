package generators.misc;

import java.util.Locale;
import java.util.Vector;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.misc.continousdoubleauction.ContinousDoubleAuction;
import generators.misc.devs.DEVS;
import generators.misc.doubleauction.DoubleAuction;
import generators.misc.drunkenbishop.DrunkenBishop;
import generators.misc.gameoflife.GameOfLifeParallel;
import generators.misc.hirschberg.Hirschberg;
import generators.misc.kNN.KNNAllOrNothing;
import generators.misc.kNN.KNNMajorityVote;
import generators.misc.knapsackProblem.main.KnapsackProblem;
import generators.misc.lee.LeeGenerator;
import generators.misc.machineLearning.AdaBoost;
import generators.misc.machineLearning.AprioriML;
import generators.misc.machineLearning.FillingGaps;
import generators.misc.machineLearning.FindGSymbolic;
import generators.misc.machineLearning.FindS1D;
import generators.misc.machineLearning.FindS2D;
import generators.misc.machineLearning.FindSSymbolic;
import generators.misc.machineLearning.KNNMachineLearning;
import generators.misc.machineLearning.RISE;
import generators.misc.machineLearning.RelieF;
import generators.misc.machineLearning.SeparateAndConquer;
import generators.misc.nonuniformTM.SpaceComplexity;
import generators.misc.schulzemethod.SchulzeMethod;
import generators.misc.sealedbid.SealedBidGenerator;
import generators.misc.spaltenminimumMethode.SpaltenminimumMethode;
import generators.misc.sweepandpruneaabb2d.SweepAndPruneAABB2D;
import generators.tree.ID3de;
import generators.tree.ID3en;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new CandidateElimination());
    generators.add(new ClockPageReplacement());
    generators.add(new OPTICS());
    generators.add(new TowerOfHanoi());
    generators.add(new VogelApproximation());
    generators.add(new Levenshtein());
    generators.add(new MoodleConnectPresentation());
    generators.add(new VariableDemo());
    
    // "under probation"
    // generators.add(new Choly());
    // generators.add(new GameOfLive());
    // generators.add(new GaussianFilter());
    // generators.add(new Greedy());
    // generators.add(new Histogramm());
    // generators.add(new KdTree());
    // generators.add(new RoundRobin());
    // generators.add(new Simplex());
    // generators.add(new Simplex2());
    
    // Generators from the AlgoAnim course in summer semester 2011.
    generators.add(new JaroWinkler());
    generators.add(new RamerDouglasPeucker());
    generators.add(new KVDiagramm());
    generators.add(new MaekawaAlgorithm());
    generators.add(new CYK());
    generators.add(new DFAMinimierung());
    generators.add(new DTW());
    generators.add(new FloydCycle());
    generators.add(new Josephus());
    generators.add(new LongestCommonSubsequence());
    generators.add(new NeedlemanWunsch());
    
    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new HPFgenerator());
    generators.add(new SRTFgenerator());
    generators.add(new LoopParallelizationReduction());
    generators.add(new GameOfLive());
    generators.add(new FisherYatesShuffle());
    generators.add(new Base64());
    generators.add(new Casteljau());
    generators.add(new DropHeuristik());
    generators.add(new BrentsAlgorithm());
    generators.add(new ValueIterationGenerator());
    generators.add(new DTWAnimation());
    generators.add(new PolicyIterationGenerator());
    generators.add(new NearestCentroidClassifier());
//    generators.add(new Simplex2());
    generators.add(new DeBoorAlgorithmus());
    generators.add(new DeCasteljauAlgorithmus());

    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new ForwardAlgorithmGenerator());
    generators.add(new DEVS());
    generators.add(new ModiMethod());
    generators.add(new DecentralStandardization());
    generators.add(new PalindromeGenerator());
    generators.add(new Greedy());
//    generators.add(new GameOfLife2()); // Improved upon by original authors in 2014.
    generators.add(new HammingWeight());
    generators.add(new Knapsack()); // Exception.
    generators.add(new ShuntingYard()); // Exzellent.
    generators.add(new FindS());
    generators.add(new FindG());
    generators.add(new DreiBlockHeuristik()); // Gut.
    generators.add(new SchulzeMethod()); // Gut.
    generators.add(new DrunkenBishop()); // Exzellent.
    generators.add(new KMedianPPGenerator2()); // Exzellent.
    generators.add(new Base64Encode());
    generators.add(new LeeGenerator()); // Exzellent.
//    generators.add(new GameOfLifeParallel()); // Improved upon by original authors in 2014.
    generators.add(new Base64Decode());
    generators.add(new LongestCommonSubstringGenerator()); // Exzellent.
    generators.add(new Wochentagsberechnung()); // Exzellent.

    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new EvoAlgoGenerator());
    generators.add(new KNNAllOrNothing());  // kreativer Einsatz der JOptionPane bei der Validierung.
    generators.add(new KNNMajorityVote());  // v.s.
    generators.add(new EvoWordsGenerator());
    generators.add(new GameOfLife2());
    generators.add(new GameOfLifeParallel());
    generators.add(new DoubleAuction());  // Graphisch hochwertig.
    generators.add(new ContinousDoubleAuction());  // v.s.
    generators.add(new CORDIC());
    generators.add(new EloZahl("EN"));  // Gut.
    generators.add(new EloZahl("DE"));  // v.s.
    generators.add(new DFA());
    generators.add(new KuhnMunkres());  // Gut.
    generators.add(new HopcroftKarp());

    // Generators from the AlgoAnim course in summer term 2016.
    generators.add(new EDF_preemptive_Scheduling());
    generators.add(new EDF_nonpreemptive_Scheduling());
    generators.add(new FindGSetGenerator());
//    generators.add(new FindSSetGenerator());
//    generators.add(new NaiveBayesGen());
    generators.add(new OAuth2());
    generators.add(new RouletteWheelSelection());
    generators.add(new Sorensen(Locale.GERMANY));
    generators.add(new Sorensen(Locale.US));
    generators.add(new SweepAndPruneAABB2D("resources/sap", Locale.GERMANY));
    generators.add(new SweepAndPruneAABB2D("resources/sap", Locale.US));
    generators.add(new TournamentSelection());

    // Generators from the AlgoAnim course in summer term 2017.
    generators.add(new MultilevelQueue("resources/MultilevelQueue", Locale.GERMANY));
    generators.add(new MultilevelQueue("resources/MultilevelQueue", Locale.US));
    generators.add(new BankersAlgorithm(Locale.GERMANY));
    generators.add(new BankersAlgorithm(Locale.US));
    generators.add(new Batch_FindG());
    generators.add(new ChiSquareGen());

    // GR
    generators.add(new SpaceComplexity());
    generators.add(new WagnerWithinGenerator(Locale.GERMANY));
    generators.add(new WagnerWithinGenerator(Locale.US));
    
    // Generators from the AlgoAnim course in summer term 2017.
    generators.add(new ArithConverterGenerator());
    generators.add(new C45());
    generators.add(new ChomskyNormalform());
    generators.add(new CSRF_Generator());
    generators.add(new DecompositionGenerator("resources/decomposition", Locale.GERMANY));
    generators.add(new DecompositionGenerator("resources/decomposition", Locale.US));
    generators.add(new ExpectationMaximization());
    generators.add(new GaleShapleyMatching());
    generators.add(new GrayCodeGenerator());
    generators.add(new ID3en(true));
    generators.add(new ID3en(false));
    generators.add(new ID3de(true));
    generators.add(new ID3de(false));
    generators.add(new Jitter());
    generators.add(new KnapsackProblem());
    generators.add(new KNearestNeighbor());
    generators.add(new LRUPageReplacement());
    generators.add(new MatrixSequenzKlammerung());
    generators.add(new RoundRobinEn(true));
    generators.add(new RoundRobinEn(false));
    generators.add(new RoundRobinDe(true));
    generators.add(new RoundRobinDe(false));
    generators.add(new SaintLagueAlgo());
    generators.add(new SpaltenminimumMethode());
    
    // machine learning 
    generators.add(new FindS1D("resources/PseudoFindS1D",Locale.US));
    generators.add(new FindS1D("resources/PseudoFindS1D",Locale.GERMANY));   
    generators.add(new FindS2D("resources/PseudoFindS2D",Locale.US));
    generators.add(new FindS2D("resources/PseudoFindS2D",Locale.GERMANY));  
    generators.add(new FindSSymbolic("resources/PseudoFindSSymbolic", Locale.US));
    generators.add(new FindSSymbolic("resources/PseudoFindSSymbolic",Locale.GERMANY));  
    generators.add(new FindGSymbolic("resources/PseudoFindGSymbolic", Locale.US));
    generators.add(new FindGSymbolic("resources/PseudoFindGSymbolic",Locale.GERMANY));  
    generators.add(new SeparateAndConquer("resources/PseudoSeparateAndConquer", Locale.US));
    generators.add(new SeparateAndConquer("resources/PseudoSeparateAndConquer",Locale.GERMANY));  
    generators.add(new KNNMachineLearning("resources/PseudoKNNMachineLearning", Locale.US));
    generators.add(new KNNMachineLearning("resources/PseudoKNNMachineLearning",Locale.GERMANY));  
    generators.add(new FillingGaps("resources/PseudoFillingGaps", Locale.US));
    generators.add(new FillingGaps("resources/PseudoFillingGaps",Locale.GERMANY)); 
    generators.add(new AdaBoost("resources/PseudoAdaBoost", Locale.US));
    generators.add(new AdaBoost("resources/PseudoAdaBoost",Locale.GERMANY));
    generators.add(new AprioriML("resources/PseudoApriori", Locale.US));
    generators.add(new AprioriML("resources/PseudoApriori",Locale.GERMANY));

    
    // Generators from the AlgoAnim course in summer term 2018.
    generators.add(new Bagging_Generator());
    generators.add(new BoothProp());
    generators.add(new Cobweb());
    generators.add(new DiskSchedulingCSCAN("resources/DiskSchedulingCSCAN",Locale.GERMANY));
    generators.add(new DiskSchedulingCSCAN("resources/DiskSchedulingCSCAN",Locale.US));
    generators.add(new DiskschedulingFCFS("resources/DiskschedulingFCFS", Locale.GERMANY));
    generators.add(new DiskschedulingFCFS("resources/DiskschedulingFCFS",Locale.US));
    generators.add(new DiskschedulingLOOK("resources/DiskschedulingLOOK", Locale.GERMANY));
    generators.add(new DiskschedulingLOOK("resources/DiskschedulingLOOK",Locale.US));
    generators.add(new DiskSchedulingSSTF("resources/DiskSchedulingSSTF",Locale.GERMANY));
    generators.add(new DiskSchedulingSSTF("resources/DiskSchedulingSSTF",Locale.US));
    generators.add(new Glicko2Generator(Locale.US));
    generators.add(new Glicko2Generator(Locale.GERMANY));
    generators.add(new ID3ChiSquared(Locale.US));
    generators.add(new ID3ChiSquared(Locale.GERMANY));
    generators.add(new Hirschberg());
    generators.add(new HoshenKopelman());
    generators.add(new Joins("resources/Joins", Locale.US));
    generators.add(new Joins("resources/Joins", Locale.GERMANY));
    generators.add(new LamportBakeryAlgo(Locale.US));
    generators.add(new LamportBakeryAlgo(Locale.GERMANY));
    generators.add(new ModelCheckerAPI());
    generators.add(new NaiveBayesClasifier());
    generators.add(new Normalization());
    generators.add(new PetersonAlgo(Locale.US));
    generators.add(new PetersonAlgo(Locale.GERMANY));
    generators.add(new RelieF("generators/misc/machineLearning/helperRelief/Relief", Locale.US));
    generators.add(new RelieF("generators/misc/machineLearning/helperRelief/Relief", Locale.GERMANY));
    generators.add(new RISE("generators/misc/machineLearning/helperRISE/PseudoRISE", Locale.US));
    generators.add(new RISE("generators/misc/machineLearning/helperRISE/PseudoRISE", Locale.GERMANY));
    generators.add(new SATGenerator());
    generators.add(new SimpleElevator(Locale.ENGLISH));
    generators.add(new SimpleElevator(Locale.GERMANY));
    generators.add(new Smith_waterman());
    generators.add(new Windowing_Generator());
    generators.add(new ZellersKongruenz("resources/ZellersKongruenz",Locale.GERMANY));
    generators.add(new ZellersKongruenz("resources/ZellersKongruenz",Locale.US));
 
    // Generators from the AlgoAnim course in summer term 2019
    generators.add(new BIRCHGenerator("resources/BIRCHGenerator", Locale.GERMANY));
    generators.add(new BIRCHGenerator("resources/BIRCHGenerator", Locale.US));
    generators.add(new ChiMergeGenerator());
    generators.add(new CompleteLinkage());
    generators.add(new DivideAndConquer());
    generators.add(new ErshovNumbersGenerator("resources/ErshovNumbers", Locale.GERMANY));
    generators.add(new ErshovNumbersGenerator("resources/ErshovNumbers", Locale.US));
    generators.add(new FloydsCycleDetection());
    generators.add(new FriendsOfFriends());
    generators.add(new GaussFormel("resources/GaussFormel", Locale.GERMANY));
    generators.add(new GaussFormel("resources/GaussFormel", Locale.US));
    generators.add(new HusAlgorithm());
    generators.add(new KochKurve());
    generators.add(new MapReduce("resources/MapReduce", Locale.GERMANY));
    generators.add(new MapReduce("resources/MapReduce", Locale.US));
    generators.add(new MersenneTwisterGenerator());
    generators.add(new MontyHallProblem());
    generators.add(new MooresAlgorithm());
    generators.add(new PumpingLemma());
    generators.add(new RGBToyCbCr("resources/rgbToyCbCr",Locale.GERMANY));
    generators.add(new RGBToyCbCr("resources/rgbToyCbCr",Locale.US));
    generators.add(new SealedBidGenerator());
    generators.add(new SecretaryProblem(Locale.GERMANY));
    generators.add(new SecretaryProblem(Locale.US));
    generators.add(new ShortestRemainingTimeFirst("resources/ShortestRemainingTimeFirst", Locale.GERMANY));
    generators.add(new ShortestRemainingTimeFirst("resources/ShortestRemainingTimeFirst", Locale.US));
    generators.add(new SJF());
    generators.add(new Synthesis("resources/Synthesis", Locale.GERMANY));
    generators.add(new Synthesis("resources/Synthesis", Locale.US));
    generators.add(new Szymanski("resources/Szymanski", Locale.GERMANY));
    generators.add(new Szymanski("resources/Szymanski", Locale.US));
    
    return generators;
  }
}
