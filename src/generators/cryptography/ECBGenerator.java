package generators.cryptography;

import generators.cryptography.helpers.E;
import generators.cryptography.helpers.ECB;
import generators.cryptography.helpers.Parser;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;

public class ECBGenerator implements Generator {
  private Language             lang;
  String                       E_as_function;
  private TextProperties       Text;
  private SourceCodeProperties Description;
  // private SourceCodeProperties sourceCode;
  // private int r;
  int                          rint;
  private String               rint2;
  private TextProperties       Header;
  private TextProperties       Pseudocode;
  private boolean              E_is_permutation;
  int[]                        E_as_permutation;
  private String               message_m;
  private TextProperties       CipherBlocks;

  public void init() {
    lang = new AnimalScript("Electronic Codebook Mode (ECB)",
        "Ahmet Erguen,Niklas Bunzel", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    E_as_function = (String) primitives.get("E_as_function");
    Text = (TextProperties) props.getPropertiesByName("Text");
    Description = (SourceCodeProperties) props
        .getPropertiesByName("Description");
    // int r = (Integer)primitives.get("r").getIntValue();
    Header = (TextProperties) props.getPropertiesByName("Header");
    Pseudocode = (TextProperties) props.getPropertiesByName("Pseudocode");
    E_is_permutation = (Boolean) primitives.get("E_is_permutation");
    E_as_permutation = (int[]) primitives.get("E_as_permutation");
    message_m = (String) primitives.get("message_m");
    CipherBlocks = (TextProperties) props.getPropertiesByName("CipherBlocks");

    rint2 = (primitives.get("r").toString());
    rint = (Integer) primitives.get("r");
    E_as_permutation = (int[]) primitives.get("E_as_permutation");
    E_is_permutation = (Boolean) primitives.get("E_is_permutation");
    E_as_function = (String) primitives.get("E_as_function");
    message_m = (String) primitives.get("message_m");

    E e;
    // E is a permutation
    if (E_is_permutation) {
      e = new E() {
        @Override
        public Object stringRepresentation() {

          String[] result = new String[E_as_permutation.length];

          for (int i = 0; i < result.length; i++)
            result[i] = new Integer(E_as_permutation[i]).toString();

          return result;
        }

        @Override
        public boolean isPermutation() {
          return true;
        }

        @Override
        public String encrypt(String i_i) {

          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < E_as_permutation.length; i++) {
            sb.append(i_i.charAt(E_as_permutation[i] - 1));
          }

          return sb.toString();
        }
      };
    }
    // e is no permutation
    else {
      e = new E() {

        @Override
        public Object stringRepresentation() {
          return E_as_function;
        }

        @Override
        public boolean isPermutation() {
          return false;
        }

        @Override
        public String encrypt(String i_i) {
          // replace x with i_i
          int in = Integer.parseInt(i_i, 2);
          String input = E_as_function.replace("x", String.valueOf(in));
          Parser p = new Parser();
          int result = (int) p.parse(input);

          return toBin(result, rint);
        }
      };
    }

    ECB crypto = new ECB(lang);
    crypto.crypt(e, message_m, rint, rint2, Header, Text, Text, CipherBlocks,
        Pseudocode, Text, Text, Text, Description, Description, Description,
        Description, Description);

    return lang.toString();
  }

  public String getName() {
    return "Electronic Codebook Mode (ECB)";
  }

  public String getAlgorithmName() {
    return "Electronic Codebook Mode (ECB)";
  }

  public String getAnimationAuthor() {
    return "Ahmet Erguen, Niklas Bunzel";
  }

  public String getDescription() {
    return "Electronic Codebook Mode (ECB)"
        + "\n"
        + " Electronic Codebook Mode (ECB)(http://en.wikipedia.org/wiki/Electronic_codebook#Electronic_codebook_.28ECB.29) is a Blockcipher(http://en.wikipedia.org/wiki/Block_cipher)<br>"
        + "\n"
        + " The message is divided into plaintext blocks.<br>"
        + "\n"
        + " The blocks being encrypted to get the ciphertext blocks.<br>"
        + "\n"
        + " The ciphertext blocks are decrypted, with the same function to get the ciphertext blocks.<br>"
        + "\n"
        + " The disadvantage of this method is that identical plaintext blocks are encrypted into identical ciphertext blocks.<br>"
        + "\n"
        + " It is not recommended for use in cryptographic protocols at all.<br>"
        + "\n";
  }

  public String getCodeExample() {
    return "Encryption" + "\n" + "\n" + "def ECB(m, r):" + "\n"
        + " # split text into blocks of length of r" + "\n"
        + "     blocks = splitText(m, r)" + "\n"
        + "     c = Array(blocks.length())" + "\n"
        + "     for each block in blocks:" + "\n" + "           c[i] = E(m_i)"
        + "\n" + "  return c" + "\n" + "\n"
        + ":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" + "\n"
        + "\n" + "Decryption" + "\n" + "\n" + "def ECB(c, r):" + "\n"
        + "     # split text into blocks of length of r" + "\n"
        + "     blocks = splitText(m, r)" + "\n"
        + "     c = Array(blocks.length())" + "\n"
        + "     for each block in blocks:" + "\n" + "          c[i] = E(m_i)"
        + "\n" + "  return c";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * converts the given number to a binary string witl n digits
   * 
   * @param num
   *          given number
   * @param n
   *          number of digits
   * 
   * @return number as binary string
   */
  String toBin(int num, int n) {
    String bin = Integer.toBinaryString(num);

    while (bin.length() < n) {
      bin = "0" + bin;
    }

    if (bin.length() > n) {
      bin = bin.substring(bin.length() - n, bin.length());
    }

    return bin;
  }

}