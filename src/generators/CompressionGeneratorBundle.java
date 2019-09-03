package generators;

import generators.compression.BurrowsWheelerReTransformation;
import generators.compression.BurrowsWheelerTransform;
import generators.compression.Huffman;
import generators.compression.MTF;
import generators.compression.MTFDecoding;
import generators.compression.Sequitur;
import generators.compression.ShannonFanoEncoding;
import generators.compression.arithmetic.ArithmeticDecoding;
import generators.compression.arithmetic.ArithmeticEncoding;
import generators.compression.lempelziv.LZ77;
import generators.compression.lempelziv.LZ77Decoding;
import generators.compression.lempelziv.LZ77Encoding;
import generators.compression.lempelziv.LZ78;
import generators.compression.lempelziv.LZ78Decoding;
import generators.compression.lempelziv.LZW;
import generators.compression.lempelziv.LZWDecoding;
import generators.compression.runlength.RLE;
import generators.compression.runlength.RLEEn;
import generators.framework.Generator;
import generators.framework.GeneratorBundle;

import java.util.Vector;

public class CompressionGeneratorBundle implements GeneratorBundle {
  public Vector<Generator> getGenerators() {
    Vector<Generator> result = new Vector<Generator>(120);

    // arithmetic encoding
    result.add(new ArithmeticDecoding());
    // arithmetic encoding
    result.add(new ArithmeticEncoding());
    // Burrows-Wheeler-Transformation
    result.add(new BurrowsWheelerTransform());
    // Burrows-Wheeler-Retransformation
    result.add(new BurrowsWheelerReTransformation());
    // Huffman encoding
    result.add(new Huffman());
    // LZ77 encoding
    result.add(new LZ77());
    // LZ77 decoding
    result.add(new LZ77Decoding());
    // LZ77 encoding (English)
    result.add(new LZ77Encoding());
    // LZ78 encoding
    result.add(new LZ78());
    // LZ78 decoding
    result.add(new LZ78Decoding());
    // LZW encoding
    result.add(new LZW());
    // LZW decoding
    result.add(new LZWDecoding());
    // Move-to-Front encoding
    result.add(new MTF());
    // Move-to-Front decoding
    result.add(new MTFDecoding());
    // RLE encoding
    result.add(new RLE());
    // RLE encoding (English)
    result.add(new RLEEn());
    // Sequitur encoding
    result.add(new Sequitur());
    // Shannon-Fano encoding
    result.add(new ShannonFanoEncoding());
    return result;
  }

}
