package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.graph.DeterminierungNDFA.DeterminierungNDFADemo;
import generators.graph.MinimierungDFA.MinimierungDFADemo;
import generators.graph.bellmanford.BellmanFord;
import generators.graph.betweenness.Betweenness;
import generators.graph.betweenness.Betweenness.BETWEENNESS_TYPE;
import generators.graph.bipartite.BipartitionGenerator;
import generators.graph.boruvka.Boruvka;
import generators.graph.breadthfirstsearch.BreadthFirstSearch;
import generators.graph.bronkerbosch.BronKerboschVertexOrdering;
import generators.graph.bronkerbosch.BronKerboschWithPivoting;
import generators.graph.bronkerbosch.BronKerboschWithoutPivoting;
import generators.graph.bully.BullyGenerator;
import generators.graph.debruijn.DeBruijn;
import generators.graph.depthfirstsearch.DFStraverse;
import generators.graph.dijkstra.Dijkstra3;
import generators.graph.dijkstra.DijkstraDE;
import generators.graph.euleriancyclecode.EulerianCycleCodeGenerator;
import generators.graph.kahn.KahnsAlgo;
import generators.graph.kruskal.Kruskal;
import generators.graph.kruskal.Kruskal2;
import generators.graph.kruskal.KruskalAlgoAPIGenerator;
import generators.graph.lindenmayer.LindenmayerAPIGenerator;
import generators.graph.longestdagpath.LongestDagPathGenerator;
import generators.graph.nearestneighbor.NearestNeighborAlgorithm;
import generators.graph.prim.MyPrim;
import generators.graph.prim.PrimAPIGenerator;
import generators.graph.sna.GirvanNewman;
import generators.graph.tsp.TSP;
import generators.graph.warshall.Warshall;
import generators.graph.whispers.Whispers;

import java.util.Locale;
import java.util.Vector;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new AnnotatedRingGenerator());
    generators.add(new BellmanFord());
    generators.add(new Kruskal());
    generators.add(new MyPrim());

    // TODO "under probation"
    // generators.add(new GeneratorKruskalAlgo());
    // generators.add(new GV());
    // generators.add(new ReverseDelete());
    // generators.add(new BellmanFordGHHC());
    // generators.add(new BipartiteGraph()); // throws an exception
    // generators.add(new BFS());
    // generators.add(new DepthFirstSearch());
    // generators.add(new DFS());
    // generators.add(new DFSDSDM());
    // generators.add(new Dijkstra());
    // generators.add(new Dijkstra2());
    // generators.add(new Floyd());
    // generators.add(new KruskalGHHC());
    // generators.add(new Prim());
    // generators.add(new PrimAlgorithm());
    // generators.add(new PrimAPIGeneratorA());
    // generators.add(new Warshall2());
    // generators.add(new WarshallMTBE());

    // Generators from the AlgoAnim course in summer semester 2011.
    generators.add(new FinalFLP());
    generators.add(new FordFulkerson2());
    generators.add(new MinMax());
    generators.add(new PathSearch());
    generators.add(new PrimAPIGenerator());

    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new Boruvka());
    generators.add(new BreadthFirstSearch());
    generators.add(new DFStraverse());
    // generators.add(new Dijkstra());
    generators.add(new Dijkstra3());
    generators.add(new EdmondsAlgorithm());
    // generators.add(new GV());
    generators.add(new Kantenlistenalgorithmus());
    generators.add(new Kosaraju());
    generators.add(new KruskalAlgoAPIGenerator());
    generators.add(new Kruskal2());
    generators.add(new NearestNeighborAlgorithm());
    generators.add(new Tarjan());
    generators.add(new Whispers());

    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new Betweenness(BETWEENNESS_TYPE.LINK_BETWEENNESS));
    generators.add(new Betweenness(BETWEENNESS_TYPE.NODE_BETWEENNESS));
    // generators.add(new BipartiteGraph()); // Exception.
    generators.add(new BipartitionGenerator());
    generators.add(new BullyGenerator());
    generators.add(new DijkstraDE());
    generators.add(new EdgeColoringGenerator());
    generators.add(new GirvanNewman());
    generators.add(new LongestDagPathGenerator());
    generators.add(new MCL()); // Gut.
    // generators.add(new ReverseDelete2());
    generators.add(new TSP()); // Exzellent.
    generators.add(new Warshall());
    generators.add(new WelshPowell()); // Gut.

    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new BronKerboschVertexOrdering());  // v.s.
    generators.add(new BronKerboschWithoutPivoting());  // Exzellent.
    generators.add(new BronKerboschWithPivoting());  // v.s.
    generators.add(new DeBruijn());  // -3. Format der Beschreibung.
    generators.add(new DeterminierungNDFADemo());
    generators.add(new DiamondSquare());  // -9,, Defaults, Validierung, Pseudocode.
    generators.add(new DSR());
    generators.add(new LindenmayerAPIGenerator());  // -3, Gut, dennoch starkes Potential übrig.
    generators.add(new MinimierungDFADemo());
    generators.add(new ReverseDelete());

    // Generators from the AlgoAnim course in summer term 2016
    generators.add(new ClassicalBipartiteMatching());
    generators.add(new EulerianCycleCodeGenerator());
    generators.add(new Perzeptron());
    
    //GR
    generators.add(new GozintoGenerator());
    generators.add(new Fleury());
    generators.add(new PushRelabelGenerator());
    generators.add(new PageRank());

    // Generators from the AlgoAnim course in summer term 2017
    generators.add(new HotPotatoRouting());
    generators.add(new MazeKruskal());

    // Generators from the AlgoAnim course in summer term 2018
    generators.add(new KargersAlgorithm(Locale.US));
    generators.add(new KargersAlgorithm(Locale.GERMAN));
    generators.add(new EdmondsKarp_generator(Locale.GERMANY));
    generators.add(new EdmondsKarp_generator(Locale.US));
    generators.add(new FarthestInsertion());
    generators.add(new NearestInsertion());
    generators.add(new RoutingBackwardLearning());
    generators.add(new ACO());
    generators.add(new AhujaOrlin());
    generators.add(new Dinic());
    generators.add(new ForceDirectedGraphDrawingAPI());

    
    // Generators from the AlgoAnim course in summer term 2019
    generators.add(new BackpropGenerator("resources/BackpropGenerator", Locale.GERMANY));
    generators.add(new BackpropGenerator("resources/BackpropGenerator", Locale.US));
    generators.add(new Beamsearch(Locale.GERMANY));
    generators.add(new Beamsearch(Locale.US));
    generators.add(new DivisiveClustering());
    generators.add(new DirectedGraphicalModel());
    generators.add(new ForwardpropGenerator("resources/ForwardpropGenerator", Locale.GERMANY));
    generators.add(new ForwardpropGenerator("resources/ForwardpropGenerator", Locale.US));
    generators.add(new KahnsAlgo());
    generators.add(new StableMarriageProblem(Locale.GERMANY));
    generators.add(new StableMarriageProblem(Locale.US));
    generators.add(new SuccessiveShortestPaths());
    generators.add(new UndirectedGraphicalModel());
    
    return generators;
  }
}
