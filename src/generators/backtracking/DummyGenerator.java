package generators.backtracking;

import generators.backtracking.springerproblem.SpringerProblemGeneratorDE;
import generators.backtracking.stableMarriageProblem.StableMarriageProblem;
import generators.framework.Generator;
import generators.framework.GeneratorBundle;

import java.util.Vector;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);

    // Generators from the AlgoAnim course in summer semester 2011.
    generators.add(new CSP());
    
    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new CYKGenerator());
    
    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new DavisPutnam());
    generators.add(new QueenPuzzleGenerator());
    generators.add(new SpringerProblemGeneratorDE()); // Gut.
//    generators.add(new StableMarriageProblem()); // Excellent. Exception.

    // Generators from the AlgoAnim course in summer semester 2017
    generators.add(new RecursiveBacktrackingMazeGeneration());
    
    return generators;
  }
}
