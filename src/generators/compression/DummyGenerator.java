package generators.compression;

import generators.compression.lempelziv.LZW2;
import generators.compression.lempelziv.LZWDecoding;
import generators.compression.shannon_fano.ShannonFanoCoding;
import generators.compression.tunstall.TunstallCodingGenerator;
import generators.compression.huffman.HuffmanCoding;
import generators.compression.huffman2.HuffmanCodeGenerator;
import generators.compression.runlength.RLE;
import generators.framework.Generator;
import generators.framework.GeneratorBundle;

import java.util.Locale;
import java.util.Vector;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new BurrowsWheelerReTransformation());
    generators.add(new BurrowsWheelerTransform());
    generators.add(new LZW2("resources/LZW_Java", Locale.US));
    generators.add(new LZW2("resources/LZW_Java", Locale.GERMANY));
    generators.add(new LZWDecoding());
    generators.add(new MTF());
    generators.add(new MTFDecoding());
    generators.add(new RLE());
    generators.add(new Sequitur());

    // TODO "under probation"
    // generators.add(new Huffman());
    // generators.add(new LZWAlgorithm());
    // generators.add(new ShannonFanoEncoding());
    // generators.add(new ShannonGenerator());
    // generators.add(new ArithmeticDecoding());
    // generators.add(new ArithmeticEncoding());
    // generators.add(new LZ77());
    // generators.add(new LZ77Decoding());
    // generators.add(new LZ77Encoding());
    // generators.add(new LZ78());
    // generators.add(new LZ78Decoding());
    // generators.add(new LZW());
    // generators.add(new RLEEn());

    // Generators from the AlgoAnim course in summer semester 2011.
    generators.add(new ShannonGenerator());

    // Generators from the AlgoAnim course in summer semester 2015.
    generators.add(new LZ77("resources/LZ77", Locale.GERMANY)); // gut
    generators.add(new LZ77("resources/LZ77", Locale.US)); // gut
    generators.add(new ShannonFanoCoding());
    generators.add(new HuffmanCoding());

    // Generators from the AlgoAnim course in summer semester 2017.    
    generators.add(new HuffmanCodeGenerator());
    generators.add(new TunstallCodingGenerator());

    // Generators from the AlgoAnim course in summer semester 2018.    
    generators.add(new Dxt1Generator());
    generators.add(new Dxt4Generator());


    // Generators from the AlgoAnim course in summer semester 2019.    
    generators.add(new JPEGCompression("resources/JPEGCompression", Locale.GERMANY));
    generators.add(new JPEGCompression("resources/JPEGCompression", Locale.US));
    
    return generators;
  }
}
