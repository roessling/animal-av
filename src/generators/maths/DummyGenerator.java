package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.maths.adjoint.AdjointAnimGenerator;
import generators.maths.affine.AA_Rotation;
import generators.maths.affine.AA_Scherung;
import generators.maths.affine.AA_Skalierung;
import generators.maths.affine.AA_Translation;
import generators.maths.buffon.Buffon;
import generators.maths.eratosthenes.EratosthenesGenerator;
import generators.maths.faddejewleverrier.FaddejewLeverrier;
import generators.maths.fixpointinteration.FPIGenerator;
import generators.maths.lrzerlegung.LRZerlegung;
import generators.maths.newtonpolynomial.NewtonPolynomialAnimGenerator;
import generators.maths.romannumbers.RomanNumberGenerator;

import java.util.Locale;
import java.util.Vector;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new FastBitcount());
    generators.add(new ModPow());

    // Generators from the AlgoAnim course in summer semester 2011.
    generators.add(new Cholesky());
    generators.add(new DeterminantGenerator());
    generators.add(new DiscreteLogarithmPollardRho());
    generators.add(new DivDiff());
    generators.add(new EfficientExpModNGenerator());
    generators.add(new ExtEuclidGenerator());
    generators.add(new GaussElim());
    generators.add(new PascalTriangle());

    // TODO "under probation"
    // generators.add(new AnnotatedSiebdesEratosthenes());
    // generators.add(new Cholgen());
    // generators.add(new ErweiterterEuklid2());
    // generators.add(new Fibonacci());
    // generators.add(new KreuzProd());
    // generators.add(new MatrixMult());
    // generators.add(new NewtonMethod()); // also missing .xml file?
    
    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new Brown());
//  generators.add(new Cholgen());
//    generators.add(new DecimalConversion());  // Fehler entdeckt.
    generators.add(new EratosthenesGenerator());
    generators.add(new Erwartungswert());
    generators.add(new ErweiterterEuklidGenerator());
    generators.add(new EuklidGenerator());
    generators.add(new MatrixGenerator());
    generators.add(new MillerRabinTest());
    generators.add(new MultiGenerator());
    generators.add(new NorthWestCornerRule());
    generators.add(new QR());
    generators.add(new RomanNumberGenerator());
    generators.add(new SchriftlicheMultiplikation());
    generators.add(new VogelApproximation());
    
    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new AdjointAnimGenerator());
    generators.add(new BabylonianMethod()); // Vorbildlich.
    generators.add(new Buffon()); // Exzellent. Vorbildlich.
    generators.add(new DoubleDabble());
    generators.add(new ExplicitEulerMethod());
    generators.add(new FaddejewLeverrier());
    generators.add(new FermatFact());
    generators.add(new FibIterative()); // Gut.  
    generators.add(new FiboRecursiv());
    generators.add(new Karatsuba());
    generators.add(new KaratsubaMultiplication()); //  Exzellent.
    generators.add(new LaplaceGenerator());
    generators.add(new NewtonPolynomialAnimGenerator());
    generators.add(new NewtonVerfahren());
    generators.add(new PotenzMenge());
    generators.add(new RungeKutta4Ordnung());
    generators.add(new SiebDesAtkin());
    generators.add(new Sundaram()); // Exzellent.

    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new LRZerlegung());
    generators.add(new Matrixmultiplication());
    generators.add(new AnyBaseToAnyBase());
    generators.add(new FPIGenerator());  // Automatisch Skalierung eines Funktionsgraphen.
    generators.add(new AA_Rotation());  // Gut.
    generators.add(new AA_Scherung());  // v.s.
    generators.add(new AA_Skalierung());  // v.s.
    generators.add(new AA_Translation());  // v.s.

    // Generators from the AlgoAnim course in summer term 2016.
    generators.add(new ChineseRem("resources/ChineseRem", Locale.GERMANY));
    generators.add(new ChineseRem("resources/ChineseRem", Locale.US));
    generators.add(new DFTGenerator());
    generators.add(new GaussJordan());
    generators.add(new GaussLegendre(Locale.GERMANY));
    generators.add(new GaussLegendre(Locale.US));
    generators.add(new KahanSummationAlgorithm());
    generators.add(new Lagrange());
    generators.add(new Neville(Locale.GERMANY));
    generators.add(new Neville(Locale.US));
    generators.add(new OptMatrixChain());
//    generators.add(new SieveOfJosephusFlavius());
    generators.add(new Simpsonregel());
    generators.add(new TransferEntropy());
    generators.add(new TrapezoidRule());

    // Generators from the AlgoAnim course in summer term 2017.
    generators.add(new BayesTheorem());
    generators.add(new csi_wiz());
    generators.add(new csi_wizDE());
    generators.add(new Ellipsoid());
    generators.add(new FourierMotzkin());
    generators.add(new Gerschgorin());
    generators.add(new MonteCarloSimulation());
    generators.add(new Polynomdivision());
    generators.add(new Primfaktorzerlegung());
    generators.add(new RiemannIntegralDE());
    generators.add(new RiemannIntegralEN());
    generators.add(new TotaleWahrscheinlichkeit());
    
    //GR
    generators.add(new Heun());
    generators.add(new OptMatrixChain());

    // Generators from the AlgoAnim course in summer term 2018.
    generators.add(new GaussOsterformel());
    generators.add(new SSA());
    generators.add(new ResolutionGenerator(Locale.ENGLISH));
    generators.add(new ResolutionGenerator(Locale.GERMAN));
    generators.add(new SimplexGenerator("resources/SimplexGenerator", Locale.US));
    generators.add(new SimplexGenerator("resources/SimplexGenerator", Locale.GERMANY));
    generators.add(new NewtonInterpolation());
    generators.add(new HornerScheme());
    generators.add(new HornformelMarkierung());
    generators.add(new TrialDivision(Locale.ENGLISH));
    generators.add(new TrialDivision(Locale.GERMAN));
    generators.add(new FastInverseSquareRoot(Locale.GERMANY));
    generators.add(new FastInverseSquareRoot(Locale.US));
    generators.add(new Interp2DScattered());
    generators.add(new ValueDifferenceMetric(Locale.GERMANY));
    generators.add(new ValueDifferenceMetric(Locale.US));
    generators.add(new LineareRegressionDE(Locale.GERMANY));
    generators.add(new LineareRegressionEN(Locale.US));
    
    // Generators from the AlgoAnim course in summer term 2019.
    generators.add(new BinaereExponentationWiz());
    generators.add(new BlumBlumShub("resources/BlumBlumShub", Locale.GERMANY));
    generators.add(new BlumBlumShub("resources/BlumBlumShub", Locale.US));
    generators.add(new BSpline());
    generators.add(new CatmullRomSplines());
    generators.add(new Drachenkurve(Locale.GERMANY));
    generators.add(new Drachenkurve(Locale.US));
    generators.add(new ExponentiationBySquaring("resources/ExponentiationBySquaring", Locale.GERMANY));
    generators.add(new ExponentiationBySquaring("resources/ExponentiationBySquaring", Locale.US));
    generators.add(new GAGenerator());
    generators.add(new HanoiVisualisierung());
    generators.add(new KnightsTourGenerator());
    generators.add(new LongestIncreasingSubsequenceNaive());
    generators.add(new PairwiseSummationGenerator());
    generators.add(new PQFormel("resources/pq", Locale.GERMANY));
    generators.add(new PQFormel("resources/pq", Locale.US));
    generators.add(new ProductRule("resources/translationProductRule", Locale.GERMANY));
    generators.add(new ProductRule("resources/translationProductRule", Locale.US));
    generators.add(new PsoGenerator());
    generators.add(new Pythagoras("resources/translationPythagoras", Locale.GERMANY));
    generators.add(new Pythagoras("resources/translationPythagoras", Locale.US));
    generators.add(new Skalarprodukt("resources/translationSkalarprodukt", Locale.GERMANY));
    generators.add(new Skalarprodukt("resources/translationSkalarprodukt", Locale.US));
    generators.add(new SpencersOsterformel(Locale.GERMANY));
    generators.add(new SpencersOsterformel(Locale.US));
    generators.add(new SternBrocot("resources/SternBrocot", Locale.GERMANY));
    generators.add(new SternBrocot("resources/SternBrocot", Locale.US));
    generators.add(new ZassenhausAPI());

    return generators;
  }
}
