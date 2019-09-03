package generators.cryptography;

import generators.cryptography.helpers.CFB;
import generators.cryptography.helpers.E;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;

public class CFBgenerator implements Generator {
  private Language lang;

  private String   initial_vector;
  // private String message_c;
  private int      r;
  int[]            E_as_permutation;
  private String   message_m;

  public void init() {
    lang = new AnimalScript("CFBgenerator", "Michelle Walther & Steffen Heger",
        800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    initial_vector = (String) primitives.get("initial_vector");
    // message_c = (String) primitives.get("message_c");
    r = (Integer) primitives.get("r");
    E_as_permutation = (int[]) primitives.get("E_as_permutation");
    message_m = (String) primitives.get("message_m");

    int n = initial_vector.length();

    E e = new E() {

      @Override
      public Object stringRepresentation() {

        int[] e_as_permutation2 = E_as_permutation;
        String[] result = new String[e_as_permutation2.length];

        for (int i = 0; i < result.length; i++)
          result[i] = new Integer(e_as_permutation2[i]).toString();

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

    CFB cfb = new CFB(lang);
    cfb.cfb(message_m, n, r, initial_vector, e);

    return lang.toString();
  }

  public String getName() {
    return "CFBgenerator";
  }

  public String getAlgorithmName() {
    return "CFB";
  }

  public String getAnimationAuthor() {
    return "Michelle Walther, Steffen Heger";
  }

  public String getDescription() {
    return "Der CFB ist ein Betriebsmodus in dem Klartexte verschluesselt"
        + "\n"
        + "(und wieder dechiffriert) werden koennen,"
        + "\n"
        + "die laenger als die Blocklaenge des Chiffrierverfahrens sind."
        + "\n"
        + "Im CFB werden Bloecke, die kuerzere Laenge als n haben koennen,"
        + "\n"
        + "durch Addition mod 2 entsprechender Schluesselbloecke verschluesselt.";
  }

  public String getCodeExample() {
    return "def CFB(m, r, IV):"
        + "\n"
        + "zerlege den Text in Bloecke der Laenge r"
        + "\n"
        + "wende die Permutation E auf den Block I_i an"
        + "\n"
        + "(erste Iteration = Anwendung von E auf IV)"
        + "\n"
        + "#setze Initialisierungsvektor als I_i"
        + "\n"
        + "I_i = IV"
        + "\n"
        + "1.) O_i = E(I_i)"
        + "\n"
        + "2.) t_i = ersten r Bits von O_i"
        + "\n"
        + "3.) m_i = zu betrachtender Teilblock"
        + "\n"
        + "4.) c[i] = t_i XOR m_i"
        + "\n"
        + "5.) I_i = entferne die ersten r Bit von I_i und haenge c_i hinten an"
        + "\n"
        + "gehe zurueck zu Schritt 1.) solang noch Bloecke vorhanden sind"
        + "\n"
        + "return c"
        + "\n"
        + "\n"
        + "\n"
        + "\n"
        + "def CFB(c, r, IV):"
        + "\n"
        + "zerlege das Chiffrat in Bloecke der Laenge r"
        + "\n"
        + "wende die Permutation E auf den Block I_i an"
        + "\n"
        + "(erste Iteration = Anwendung von E auf IV)"
        + "\n"
        + "# setze Initialisierungsvektor als I_i"
        + "\n"
        + "I_i = IV"
        + "\n"
        + "1.) O_i = E^-(I_i)"
        + "\n"
        + "2.) t_i = ersten r Bits von O_i"
        + "\n"
        + "3.) c_i = zu betrachtender Teilblock"
        + "\n"
        + "4.) m[i] = t_i XOR c_i "
        + "\n"
        + "5.) I_i = entferne die ersten r Bit von I_i und haenge c_i hinten an"
        + "\n"
        + "gehe zurueck zu Schritt 1.) solang noch Bloecke vorhanden sind"
        + "\n" + "return m";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}