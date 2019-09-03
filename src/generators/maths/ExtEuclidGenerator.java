package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;

public class ExtEuclidGenerator implements Generator {
  private Language             lang;
  private RectProperties       Header_background;
  private int                  b;
  private TextProperties       Header;
  private int                  a;
  private SourceCodeProperties Sourcecode;

  public void init() {
    lang = new AnimalScript("Extended Euclidean Algorithm [EN]",
        " Christian Feier, Yannick Drost ", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Header_background = (RectProperties) props
        .getPropertiesByName("Header_background");
    b = (Integer) primitives.get("b");
    Header = (TextProperties) props.getPropertiesByName("Header");
    a = (Integer) primitives.get("a");
    Sourcecode = (SourceCodeProperties) props.getPropertiesByName("Sourcecode");

    new ExtEuclid(lang, Header_background, Header, Sourcecode, a, b);

    return lang.toString();
  }

  public String getName() {
    return "Extended Euclidean Algorithm [EN]";
  }

  public String getAlgorithmName() {
    return "Extended Euclidean Algorithm";
  }

  public String getAnimationAuthor() {
    return "Christian Feier, Yannick Drost";
  }

  public String getDescription() {
    return "Beside calculating the greatest common devisor as the Euclidean Algorithm does, the Extended Euclidean Algorithm "
        + "\n"
        + "finds two integers x and y that conform to <b> a * x + b * y = gcd( a, b ) </b> where a and b are the input integers. "
        + "\n"
        + "For example this is pretty useful to find the private RSA keypart d. Since gcd( a, b ) = 1, x is the multiplicative "
        + "\n"
        + "inverse of a modulo b and y is the multiplicative inverse of b modulo a."
        + "\n";
  }

  public String getCodeExample() {
    return "in words:"
        + "\n"
        + "<ul>"
        + "\n"
        + "   <li>Initialize x2, x1, y2, y1"
        + "\n"
        + "   <li>Check whether b > 0"
        + "\n"
        + "   <li>calculate q and r with q = a div b and r = a mod b"
        + "\n"
        + "   <li>calculate x and y with x = x2 - q*x1 and y = y2 - q*y1"
        + "\n"
        + "   <li>set a = b and b = r"
        + "\n"
        + "   <li>set x1 = x, x2 = x1"
        + "\n"
        + "   <li>set y1 = y, y2 = y1"
        + "\n"
        + "   <li>If b = 0, x, y and the gcd conform to: a*x + b*y = gcd(a,b)"
        + "\n"
        + "</ul>"
        + "\n"
        + "\n"
        + "\n"
        + "<br/><br/>in Javacode:"
        + "\n"
        + "<pre>"
        + "\n"
        + "\n<span style=\"color: #B404AE;\"><strong>public int[]</strong></span> extEuclid(<span style=\"color: #B404AE;\"><strong>int</strong></span> a, <span style=\"color: #B404AE;\"><strong>int</strong></span> b) { <br>"
        + "\n"
        + "    <span style=\"color: #B404AE;\"><strong>int</strong></span> q, r, x, y;"
        + "\n"
        + "    <span style=\"color: #B404AE;\"><strong>int</strong></span> x2 = 1, x1 = 0; y2 = 0, y1 = 0;"
        + "\n"
        + "    <span style=\"color: #B404AE;\"><strong>while</strong></span> (b > 0) {"
        + "\n"
        + "        q = a / b; "
        + "\n"
        + "        r = a % b;"
        + "\n"
        + "  "
        + "\n"
        + "        x = x2 - q * x1;"
        + "\n"
        + "        y = y2 - q * y1;"
        + "\n"
        + "    	"
        + "\n"
        + "        a = b;"
        + "\n"
        + "        b = r;"
        + "\n"
        + "    	"
        + "\n"
        + "        x2 = x1;"
        + "\n"
        + "        x1 = x; "
        + "\n"
        + "    	"
        + "\n"
        + "        y2 = y1; "
        + "\n"
        + "        y1 = y; "
        + "\n"
        + "    }"
        + "\n"
        + "    x = x2; y = y2; gcd = a;"
        + "\n"
        + "    <span style=\"color: #B404AE;\"><strong>return new int</strong></span>[]{x,y,gcd};"
        + "\n" + "} " + "\n" + "</pre>";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}