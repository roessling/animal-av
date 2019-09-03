package generators.framework.types;

import java.util.Locale;

import translator.Translator;

public class AlgorithmNames {
  private static Translator trans;

  static {
    trans = new Translator("algorithms", Locale.US);
  }

  public static String getAlgorithmName(String code, Locale language) {
    String translation = trans.translateMessage(code);
    return translation;
  }
}
