package generators.interactive;


import java.util.Vector;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    
    // Example Generator.
    generators.add(new DoublyLinkedListInteractiv());

    
    // Generators from the AlgoAnim course in summer term XXXX.
    
    return generators;
  }
}
