package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorBundle;

import java.util.Locale;
import java.util.Vector;

public class DummyGenerator implements GeneratorBundle {

	@Override
	public Vector<Generator> getGenerators() {
		Vector<Generator> generators = new Vector<Generator>(35, 15);
		generators.add(new DoubleHashing());
    generators.add(new Hashing());
    generators.add(new Hashing2());
    generators.add(new HashingAnnotated());
    generators.add(new LinearHashing7());
    generators.add(new OpenAddressingHashing());
    
    // TODO "under probation"
 // generators.add(new DoubleHashing());
 // generators.add(new Hashin());
 // generators.add(new HashingLinear());
 // generators.add(new HashingLinearAlt());
 // generators.add(new HashingLinearProbing());
 // generators.add(new HashingWT());
 // generators.add(new QuadraticProbingWithAnnotations());
    

    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new Adler32());
    generators.add(new FNV1a(Locale.GERMANY));
    generators.add(new FNV1a(Locale.US));
    generators.add(new Luhn());
    generators.add(new MD4());
    
    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new CRC());
    generators.add(new Fletcher());
    generators.add(new GeohashDecryption());
    generators.add(new GeohashEncryption());

    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new Parity());
    generators.add(new HammingCode());
    generators.add(new HashfunktionAusKompressionsfunktion());
    generators.add(new MD5());
    generators.add(new MD4());
    generators.add(new SHA1());  // Exzellent.
    generators.add(new SHA256());  // v.s.
    
    // Generators from the AlgoAnim course in summer semester 2018.
    generators.add(new SHA3(Locale.GERMAN));
    generators.add(new SHA3(Locale.ENGLISH));
    
    // Generators from the AlgoAnim course in summer semester 2018.
    generators.add(new CuckooHashing(Locale.GERMANY));
    generators.add(new CuckooHashing(Locale.US));

    return generators;
	}
}