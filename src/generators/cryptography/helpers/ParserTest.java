package generators.cryptography.helpers;


public class ParserTest {
  public static void main(String[] args) throws Exception {
    // Einige Tests für den Algorithmus
    String[] tests = new String[] { "0.1+1", "2+4*5", "(2+4)*5", "(((5)))",
        "1 +-+-+ 25", "5*6", "(1+1)*2*(2)", "(2+3)*3 + 4 mod 7" };

    Parser parser = new Parser();

    for (String test : tests) {
      System.out.printf("\"%s\" wird zu \"%f\"\n", test, parser.parse(test));
      System.out.println(test + " ist " + parser.containsInvalidNumber(test));
    }
  }
}
