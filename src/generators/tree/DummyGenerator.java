package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.tree.binarySearchTree.BinarySearchTreeGenerator;
import generators.tree.binarySearchTree.BinarySearchTreeGeneratorEN;
import generators.tree.btree.delete.BTreeDeleteGenerator;
import generators.tree.btree.insert.BTreeInsertGenerator;
import generators.tree.knuthlayout.KnuthLayout;

import java.util.Vector;
import java.util.Locale;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new EffectiveBinaryTree());
    generators.add(new HeapTree());
    generators.add(new EffectiveAVLTree());
    generators.add(new TournamentSort());
    generators.add(new IgnallSchrage());

    // Generators from the AlgoAnim course in summer semester 2011.
    generators.add(new RTreeNodeSplit());

    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new Quadtree());

    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new HeapExtractMinimum());
    generators.add(new HeapInsert());
    generators.add(new KnuthLayout()); // Exzellent.
    generators.add(new BTreeInsertGenerator()); // Gut.
    generators.add(new BTreeDeleteGenerator()); // Gut.

    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new BinarySearchTreeGenerator());
    generators.add(new BinarySearchTreeGeneratorEN());
    generators.add(new BSTreeTraverse()); // Gut.

    // Generators from the AlgoAnim course in summer semester 2015.
    generators.add(new OS_RANK_DE("resources/osrank/language"));
    generators.add(new OS_SELECT_DE("resources/osselect/language")); // sehr gut
    generators.add(new KDTree(Locale.US)); // ausgezeichnet
    generators.add(new KDTree(Locale.GERMANY)); // ausgezeichnet
    generators.add(new RB_TREES_INSERT("resources/RB_INSERT", Locale.US));
    generators.add(new RB_TREES_INSERT("resources/RB_INSERT", Locale.GERMANY));
    generators.add(new RB_TREES_DELETE("resources/RB_DELETE", Locale.US));
    generators.add(new RB_TREES_DELETE("resources/RB_DELETE", Locale.GERMANY));

    // Generators from the AlgoAnim course in summer semester 2017
    generators.add(new TreeLabeling());

    // Generators from the AlgoAnim course in summer semester 2018
    generators.add(new TrieInsert("resources/TrieInsert", Locale.US));
    generators.add(new TrieSearch("resources/TrieSearch", Locale.US));
    generators.add(new LeonardoHeapGenerator());
    generators.add(new BTreeSearchGenerator(Locale.US));
    generators.add(new BTreeSearchGenerator(Locale.GERMANY));
    

    // Generators from the AlgoAnim course in summer semester 2019
    generators.add(new AhoCorasick("resources/AhoCorasick", Locale.GERMANY));
    generators.add(new AhoCorasick("resources/AhoCorasick", Locale.US));
    generators.add(new Fitch());
    generators.add(new RRT(Locale.GERMANY));
    generators.add(new RRT(Locale.US));
    
    return generators;
  }
}
