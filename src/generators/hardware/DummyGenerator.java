package generators.hardware;

import java.util.Locale;
import java.util.Vector;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;
import generators.hardware.flipflop.DFlipflop;
import generators.hardware.flipflop.JKFlipflop;
import generators.hardware.flipflop.RSFlipflop;
import generators.hardware.flipflop.TFlipflop;
import generators.hardware.gates.AndGatter;
import generators.hardware.gates.NAndGatter;
import generators.hardware.gates.NorGatter;
import generators.hardware.gates.NotGatter;
import generators.hardware.gates.OrGatter;
import generators.hardware.gates.XNorGatter;
import generators.hardware.gates.XOrGatter;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new AndGatter());
    generators.add(new Demux());
    generators.add(new DFlipflop());
    generators.add(new Halbaddierer());
    generators.add(new JKFlipflop());
    generators.add(new Mux());
    generators.add(new NAndGatter());
    generators.add(new NorGatter());
    generators.add(new NotGatter());
    generators.add(new OrGatter());
    generators.add(new RSFlipflop());
    generators.add(new TFlipflop());
    generators.add(new Volladdierer());
    generators.add(new XOrGatter());
    generators.add(new XNorGatter());
    
    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new RippleAddierer()); // Exzellent.

    // Generators from the AlgoAnim course in summer semester 2015.
    generators.add(new NWayCacheFIFO()); 
    generators.add(new NWayCacheLRU()); 
    generators.add(new PrefixAdderGenerator());

    // Generators from the AlgoAnim course in summer semester 2017
    generators.add(new ConvertFatToInode());

    // Generators from the AlgoAnim course in summer semester 2018
    generators.add(new MFUGenerator());
    generators.add(new NRUGenerator());

    // Generators from the AlgoAnim course in summer semester 2019
    generators.add(new Belady("resources/belady", Locale.GERMANY));
    generators.add(new Belady("resources/belady", Locale.US));
    generators.add(new CScan("resources/cscan", Locale.GERMANY));
    generators.add(new CScan("resources/cscan", Locale.US));
    
    return generators;
  }
}
