package generators.sorting;

import java.util.Locale;
import java.util.Vector;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.sorting.bogosort.Bogobogosort;
import generators.sorting.bogosort.Bogosort;
import generators.sorting.bubblesort.BubbleSort;
import generators.sorting.bubblesort.EnglishMatlabBubbleSort;
import generators.sorting.bubblesort.GenericAnnotatedBubbleSortICS2;
import generators.sorting.bucketsort.BucketSort;
import generators.sorting.bucketsort.BucketSortAF;
import generators.sorting.combsort.CombSort;
import generators.sorting.combsort.CombSort2;
import generators.sorting.gnomesort.GenericAnnotatedGnomeSortICS2;
import generators.sorting.gnomesort.GnomeSort2;
import generators.sorting.gnomesort.GnomeSortADS;
import generators.sorting.gnomesort.GnomeSortAW;
import generators.sorting.insertionsort.GenericAnnotatedInsertionSort;
import generators.sorting.insertionsort.GenericAnnotatedInsertionSortICS2;
import generators.sorting.insertionsort.GenericAnnotatedInsertionSortICS2WithCounter;
import generators.sorting.insertionsort.InsertionSortGdI2;
import generators.sorting.patienceSort.PatienceSort;
import generators.sorting.quicksort.DelphiAnnotatedQuickSort;
import generators.sorting.quicksort.GenericAnnotatedQuickSort;
import generators.sorting.quicksort.GenericAnnotatedQuickSortICS2;
import generators.sorting.selectionsort.GenericAnnotatedSelectionSort;
import generators.sorting.selectionsort.GenericAnnotatedSelectionSortWithCounter;
import generators.sorting.selectionsort.SelectionSortGen;
import generators.sorting.shakersort.AnnotatedShakerSort;
import generators.sorting.shakersort.AnnotatedShakerSorter;
import generators.sorting.shakersort.ShakerSort1;
import generators.sorting.shakersort.ShakerSort2;
import generators.sorting.shakersort.ShakerSort3;
import generators.sorting.shakersort.ShakerSortAPI;
import generators.sorting.shakersort.ShakerSortAnimation;
import generators.sorting.shakersort.ShakerSortDemo;
import generators.sorting.shakersort.ShakerSortFJ;
import generators.sorting.shakersort.ShakerSortGenerator;
import generators.sorting.shakersort.ShakerSortTV;
import generators.sorting.shakersort.ShakerSorter;
import generators.sorting.shakersort.Shakersorter2;
import generators.sorting.swapsort.SwapSort;
import generators.sorting.swapsort.SwapSort3;
import generators.sorting.swapsort.SwapSortBZ;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(165, 45);
    generators.add(new Adapt2Phasen2BandMischen());
    generators.add(new AnnotatedAdapt2Phasen2BandMischen());
    generators.add(new AnnotatedShakerSort());
    generators.add(new AnnotatedStoogesort());
    generators.add(new APIShakerSort());
    generators.add(new RadixSortBurlakGlaser());
    generators.add(new BinaryTreeSortJava());
    generators.add(new BucketSort());
    generators.add(new CombSort());
    generators.add(new CombSort2());
    generators.add(new CountingSort());
    generators.add(new CountingSort2());
    generators.add(new CountingSortAnimation());
    generators.add(new DelphiAnnotatedQuickSort());
    generators.add(new EnglishMatlabBubbleSort());
    // generators.add(new EvenOddSort());
    // generators.add(new GdI2BubbleSort("JavaGdI2BubbleSort", Locale.GERMANY));
    generators.add(new GnomeSortADS());
    generators.add(new GnomeSortAW());
    generators.add(new HeapSort());
    generators.add(new Heapsort2());
    // generators.add(new ImprovedGdI2BubbleSort());
    generators.add(new OddEvenSortTV());
    generators.add(new ProxmapSort());
    generators.add(new ShakerSort1());
    generators.add(new ShakerSort2());
    generators.add(new ShakerSort3());
    generators.add(new ShakerSortAnimation());
    generators.add(new ShakerSortAPI());
    generators.add(new ShakerSortDemo());
    generators.add(new ShakerSorter());
    generators.add(new ShakerSortFJ());
    generators.add(new ShakerSortGenerator());
    generators.add(new ShakerSortTV());
    generators.add(new SimpleSortJMSS());
    generators.add(new SlowSortPS());
    generators.add(new StoogeSort());
    generators.add(new StoogeSortHL());
    generators.add(new SwapSort());
    generators.add(new SwapSort3());
    generators.add(new SwapSortBZ());

    // TODO "under probation"
    // generators.add(new AnimalShakerSort());
    // generators.add(new AnnotatedBubbleSort());
    // generators.add(new AnnotatedCountingSort());
    // generators.add(new AnnotatedGnomeSort());
    // generators.add(new AnnotatedRadixSort());
    // generators.add(new AnnotatedSelectionSort());
    // generators.add(new AnnotatedSwapSorter());
    // generators.add(new BinaryTreeSortAnnotated());
    // generators.add(new BucketSort2());
    // generators.add(new BucketSortAnim());
    // generators.add(new BucketSortAnnon());
    // generators.add(new BucketSortER());
    // generators.add(new BucketSortMB());
    // generators.add(new CocktailSort());
    // generators.add(new CombSortAH());
    // generators.add(new CombSortGenerator());
    // generators.add(new DelphiAnnotatedBubbleSort());
    // generators.add(new DelphiAnnotatedQuickSort2());
    // generators.add(new DelphiAnnotatedSelectionSort());
    // generators.add(new GdI2BubbleSort());
    // generators.add(new GenericAnnotatedBubbleSort());
    // generators.add(new GenericAnnotatedInsertionSort2());
    // generators.add(new GenericAnnotatedInsertionSort3());
    // generators.add(new GenericAnnotatedSelectionSortICS2());
    // generators.add(new GenericInteractiveAnnotatedQuickSort());
    // generators.add(new GermanMatlabBubbleSort());
    // generators.add(new GnomeSort());
    // generators.add(new GnomeSortKS());
    // generators.add(new GnomeSortParsons());
    // generators.add(new GnomeSortTUS());
    // generators.add(new ImprovedGdI2BubbleSort());
    // generators.add(new KRZCountingSort());
    // generators.add(new KRZRadixSort());
    // generators.add(new OddEvenSort());
    // generators.add(new RadixSort());
    // generators.add(new RadixSortJZ());
    // generators.add(new SelectionSort());
    // generators.add(new SelectionSortDemo());
    // generators.add(new ShakerSort());
    // generators.add(new ShakerSortAnnotated());
    // generators.add(new ShakerSortDD());
    // generators.add(new ShakerSortTimB());
    // generators.add(new ShakerSortVP());
    // generators.add(new SimpleSort());
    // generators.add(new SimpleSortMAKT());
    // generators.add(new SlowSort());
    // generators.add(new StoogeSort2());
    // generators.add(new SwapSort2());
    // generators.add(new SwapSortAnnotated());
    // generators.add(new SwapSortBZ2());
    // generators.add(new SwapSorter());
    // generators.add(new HybridsortGenerator());

    // generic wrappers
    // Merge Sort Type I, English, Java
    generators.add(
        new GenericAnnotatedMergeSort("resources/JavaMergeSort", Locale.US));

    // Merge Sort Type I, English, Pseudo-Code
    generators.add(
        new GenericAnnotatedMergeSort("resources/PseudoMergeSort", Locale.US));

    // Merge Sort Type I, German, Java
    generators.add(new GenericAnnotatedMergeSort("resources/JavaMergeSort",
        Locale.GERMANY));

    // Merge Sort Type I, German, Pseudo-Code
    generators.add(new GenericAnnotatedMergeSort("resources/PseudoMergeSort",
        Locale.GERMANY));
    // QuickSort Type I, English, Java
    generators.add(
        new GenericAnnotatedQuickSort("resources/JavaQuickSort", Locale.US));

    // QuickSort Type I, English, Pseudo-Code
    generators.add(
        new GenericAnnotatedQuickSort("resources/PseudoQuickSort", Locale.US));

    // QuickSort Type I, German, Java
    generators.add(new GenericAnnotatedQuickSort("resources/JavaQuickSort",
        Locale.GERMANY));

    // QuickSort Type I, German, Pseudo-Code
    generators.add(new GenericAnnotatedQuickSort("resources/PseudoQuickSort",
        Locale.GERMANY));

    // Selection Sort Type I, English, Java
    generators.add(new GenericAnnotatedSelectionSort(
        "resources/JavaSelectionSort", Locale.US));

    // Selection Sort Type I, English, Pseudo-Code
    generators.add(new GenericAnnotatedSelectionSort(
        "resources/PseudoSelectionSort", Locale.US));

    // Selection Sort Type I, German, Java
    generators.add(new GenericAnnotatedSelectionSort(
        "resources/JavaSelectionSort", Locale.GERMANY));

    // Selection Sort Type I, German, Pseudo-Code
    generators.add(new GenericAnnotatedSelectionSortWithCounter(
        "resources/PseudoSelectionSort", Locale.GERMANY));

    // ShellSort Type I, English, Java
    generators.add(
        new GenericAnnotatedShellSort("resources/JavaShellSort", Locale.US));

    // ShellSort Type I, English, Pseudo-Code
    generators.add(
        new GenericAnnotatedShellSort("resources/PseudoShellSort", Locale.US));

    // ShellSort Type I, German, Java
    generators.add(new GenericAnnotatedShellSort("resources/JavaShellSort",
        Locale.GERMANY));

    // ShellSort Type I, German, Pseudo-Code
    generators.add(new GenericAnnotatedShellSort("resources/PseudoShellSort",
        Locale.GERMANY));

    // exercise sheets
    generators.add(new CocktailSortOptimized());
    generators.add(new BucketSortAF());
    // generators.add(new CombSortWR()); // Does not animate.
    generators.add(new EvenOddSort());

    // Generators for the ICS2 lecture
    generators.add(new GenericAnnotatedBubbleSortICS2(
        "resources/JavaBubbleSortICS2", Locale.US));
    generators.add(new GenericAnnotatedGnomeSortICS2(
        "resources/JavaGnomeSortICS2", Locale.US));
    generators.add(new GenericAnnotatedInsertionSortICS2(
        "resources/JavaInsertionSortICS2", Locale.US));
    generators.add(new GenericAnnotatedQuickSortICS2(
        "resources/JavaQuickSortICS2", Locale.US));

    // Generators from the AlgoAnim course in summer semester 2011.
    generators.add(new GnomeSort2());
    generators.add(new CountingSort3());
    generators.add(new BottomUpHierarchicalClustering());
    generators.add(new PancakeSort());
    generators.add(new StoogeSortEN());
    generators.add(new GenericAnnotatedInsertionSort(
        "resources/JavaInsertionSort", Locale.GERMANY));
    generators.add(new GenericAnnotatedInsertionSort(
        "resources/JavaInsertionSort", Locale.US));
    generators.add(new InsertionSortGdI2());
    generators.add(new AnnotatedShakerSorter());
    generators.add(new SimpleSort2());
    generators.add(new StrandSort());

    // Generators with Counter

    generators.add(new GenericAnnotatedInsertionSortICS2WithCounter(
        "resources/JavaInsertionSortICS2", Locale.US));

    // Selection Sort Type I with Counter, German, Pseudo-Code
    generators.add(new GenericAnnotatedSelectionSort(
        "resources/PseudoSelectionSort", Locale.GERMANY));

    // Selection Sort Type I with Counter, English, Java
    generators.add(new GenericAnnotatedSelectionSortWithCounter(
        "resources/JavaSelectionSort", Locale.US));

    // Selection Sort Type I with Counter, English, Pseudo-Code
    generators.add(new GenericAnnotatedSelectionSortWithCounter(
        "resources/PseudoSelectionSort", Locale.US));

    // Selection Sort Type I with Counter, German, Java
    generators.add(new GenericAnnotatedSelectionSortWithCounter(
        "resources/JavaSelectionSort", Locale.GERMANY));

    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new Shakersorter2());
    generators.add(new CocktailSortEnglish());
    generators.add(new JohnsonAlgoAPIGenerator());
    generators.add(new TournamentSort());
    generators.add(new BubbleSort());
    generators.add(new SelectionSortGen());
    generators.add(new TopSort());
    generators.add(new MergeSortGenerator());
    generators.add(new StrandSortGen());
    generators.add(new FlexCargo());

    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new DBSCAN());
    generators.add(new KMeans()); // Gut.
    generators.add(new Spaghettisortgenerator()); // Exzellent.
    generators.add(new CycleSortGenerator()); // Exzellent.

    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new Bogosort());
    generators.add(new Bogobogosort());
    generators.add(new Bubblesort()); // Tauschdiagramm.
    generators.add(new Quicksort()); // v.s.
    generators.add(new RippleSort()); // v.s.

    // Generators from the AlgoAnim course in summer semester 2015.
    generators.add(new BitonicSort());
    //GR DEBUG
//    generators.add(new TopoSort());
//    generators.add(new TopoSort(new Locale("de")));
    generators.add(new CocktailSortDutschka());
    
    // Generators from the AlgoAnim course in summer term 2016.
    generators.add(new AmericanFlagSortGenerator());
//    generators.add(new DualPivotQuicksort());
//    generators.add(new Timsort());
    
    // Generators from the AlgoAnim course in summer term 2017.
    generators.add(new FlashSortWiz());
    generators.add(new PigeonholeSort());
    generators.add(new LocalOutlierFactor("resources/LocalOutlierFactor", Locale.GERMANY));
    generators.add(new LocalOutlierFactor("resources/LocalOutlierFactor", Locale.US));
    generators.add(new PatienceSort());
    generators.add(new TimSort2());
    generators.add(new TreeSort());

    // GR
    generators.add(new HybridsortGenerator());
    
    // Generators from the AlgoAnim course in summer term 2018.
    generators.add(new PPBSGenerator("resources/PPBSGenerator", Locale.GERMANY));
    generators.add(new PPBSGenerator("resources/PPBSGenerator", Locale.US));
    generators.add(new SmoothSortGenerator());
    generators.add(new SLPGenerator(Locale.GERMANY));
    generators.add(new SLPGenerator(Locale.US));
    generators.add(new SampleSort());
    generators.add(new LibGenerator());

    
    // Generators from the AlgoAnim course in summer term 2018.
    generators.add(new Generator_ThreeWaySort());
    generators.add(new MultiKeyQuicksortGenerator());
        
    // Generators from the AlgoAnim course in summer term 2018.
    generators.add(new IntroSort("resources/IntroSort", Locale.GERMANY));
    generators.add(new IntroSort("resources/IntroSort", Locale.US));
    generators.add(new PostmanSort());
    
    return generators;
  }
}
