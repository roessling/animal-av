package generators.datastructures;

import java.util.Locale;
import java.util.Vector;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new BinarySpacePartitioning());
    
    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new TheFaultAlgorithmGenerator()); // Exzellent. Vorbildlich.
    
    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new LinkedListGenerator());  // Exzellent.
    generators.add(new DoublyLinkedListGenerator());  // v.s.
    generators.add(new RingBufferGenerator());  // v.s.

    generators.add(new DoublyLinkedList2(Locale.GERMANY));
    generators.add(new DoublyLinkedList2(Locale.US));
    
    // Generators from the AlgoAnim course in summer semester 2017
    generators.add(new NewLinkedList());
    

    // Generators from the AlgoAnim course in summer semester 2018
    generators.add(new IEEE754(Locale.US));
    generators.add(new IEEE754(Locale.GERMANY));
    generators.add(new BloomFilterGenerator(Locale.US));
    generators.add(new BloomFilterGenerator(Locale.GERMANY));
    generators.add(new dial_implementation_generator());
    generators.add(new Wavelet(Locale.US));
    generators.add(new Wavelet(Locale.GERMANY));

    // Generators from the AlgoAnim course in summer semester 2019
    generators.add(new PotentialField(Locale.GERMANY));
    generators.add(new PotentialField(Locale.US));
    generators.add(new SkipListInsertion(Locale.GERMANY));
    generators.add(new SkipListInsertion(Locale.US));
    
    return generators;
  }
}
