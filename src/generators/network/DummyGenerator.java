package generators.network;

import java.util.Locale;
import java.util.Vector;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.misc.FloydsTortoiseAndHare;
import generators.network.aodv.AODVRoutingGenerator;
import generators.network.dns.DNSQueryGenerator;
import generators.network.graph.BellmanFordGenerator;
import generators.network.graph.DijkstraGenerator;
import generators.network.graph.FloydWarshallGenerator;
import generators.network.graph.KruskalGenerator;
import generators.network.graph.PrimGenerator;
import generators.network.routing.DistanceVectorRouting;
import generators.network.routing.VectorRoutingGenerator;
import generators.network.routing.impl.dvr.DistanceVectorFactory;
import generators.network.routing.impl.pvr.PathVectorFactory;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 *         Provides all specific generators for 'networking'.
 * 
 */
public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(165, 45);
    /*
     * Graph Algorithms
     */
    generators.add(new BellmanFordGenerator(Locale.GERMANY));
    generators.add(new BellmanFordGenerator(Locale.US));

    generators.add(new DijkstraGenerator(Locale.GERMANY));
    generators.add(new DijkstraGenerator(Locale.US));

    generators.add(new FloydWarshallGenerator(Locale.GERMANY));
    generators.add(new FloydWarshallGenerator(Locale.US));

    generators.add(new KruskalGenerator(Locale.GERMANY));
    generators.add(new KruskalGenerator(Locale.US));

    generators.add(new PrimGenerator(Locale.GERMANY));
    generators.add(new PrimGenerator(Locale.US));

    /*
     * Basic Network Algorithms
     */
    generators.add(new DNSQueryGenerator(Locale.GERMANY));
    generators.add(new DNSQueryGenerator(Locale.US));

    /*
     * Routing Algorithms
     */
    generators.add(new VectorRoutingGenerator(Locale.GERMANY,
        new DistanceVectorFactory()));
    generators.add(new VectorRoutingGenerator(Locale.US,
        new DistanceVectorFactory()));

    generators.add(new VectorRoutingGenerator(Locale.GERMANY,
        new PathVectorFactory()));
    generators.add(new VectorRoutingGenerator(Locale.US,
        new PathVectorFactory()));

    // TODO "under probation"
    generators.add(new DistanceVectorRouting());

    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new CentralServerMutex());
    generators.add(new RingBasedMutex());
    generators.add(new CristiansAlgorithm());
    generators.add(new AODVRoutingGenerator(Locale.GERMANY));
    generators.add(new AODVRoutingGenerator(Locale.US));
    generators.add(new Petrinetze());  // Gut.
    
    // Generators from the AlgoAnim course in summer semester 2015.
    generators.add(new Berkeley());
    
    // Generators from the AlgoAnim course in summer semester 2017.
    generators.add(new LeakyBucketGeneratorEN());
    generators.add(new IEEE_RTS_CTS_Generator());
    generators.add(new TokenBucketEN());
    

    // Generators from the AlgoAnim course in summer semester 2018.
    generators.add(new Marzullo_algorithm(Locale.US));
    generators.add(new Marzullo_algorithm(Locale.GERMANY));
    generators.add(new LinkStateRouting(Locale.US));
    generators.add(new LinkStateRouting(Locale.GERMANY));
    generators.add(new SlidingWindow(Locale.US));
    generators.add(new SlidingWindow(Locale.GERMANY));
    generators.add(new RED(Locale.GERMANY));
    generators.add(new RED(Locale.US));
    generators.add(new TailDrop(Locale.GERMANY));
    generators.add(new TailDrop(Locale.US));
    
    // Generators from the AlgoAnim course in summer semester 2019.
    generators.add(new ChandyLamport("resources/ChandyLamport", Locale.US));
    generators.add(new ChandyLamport("resources/ChandyLamport", Locale.GERMANY));
    generators.add(new EasyNTP());
    generators.add(new FloydsTortoiseAndHare(Locale.GERMANY));
    generators.add(new FloydsTortoiseAndHare(Locale.US));
    generators.add(new LamportClock("resources/LamportClock", Locale.GERMANY));
    generators.add(new LamportClock("resources/LamportClock", Locale.US));
    generators.add(new TCPCongestionControl());
    generators.add(new VectorClock("resources/VectorClock", Locale.GERMANY));
    generators.add(new VectorClock("resources/VectorClock", Locale.US));

    return generators;
  }

}
