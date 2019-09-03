package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class Hashing2 implements Generator {

  /**
   * The concrete language object used for creating output
   */
  private int                   n;
  private int                   pointerCounter = 0;
  private Language              lang;
  private int[]                 in;
  private int[]                 out;
  private Text                  pseudoCode;
  // private Color elementCOLOR ,HIGHLIGHTelementCOLOR,
  // HIGHLIGHTcellCOLOR,codeCOLOR, HIGHLIGHTCodeCOLOR,FONTcodeCOLOR,markerCOLOR;
  private ArrayProperties       arrayProps;
  // private ArrayMarker sMarker;
  private ArrayMarkerProperties markerProps;
  private SourceCodeProperties  sourceCodeProps;

  public Hashing2() {
    // Store the language object
    lang = new AnimalScript("Hashing", "kam", 640, 480);
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);
  }

  public void init() {
  }

  public void Hashing(IntArray Array, IntArray Array2, SourceCode codeSupport) {

    pointerCounter++;

    ArrayMarker sMarker = lang.newArrayMarker(Array2, -1, "s" + pointerCounter,
        null, markerProps);

    for (int i = 0; i < n; i++) {
      Array.highlightElem(i, null, null);

      codeSupport.highlight(0, 0, false);
      lang.nextStep();
      codeSupport.unhighlight(0, 0, false);
      codeSupport.highlight(1, 0, false);
      int spos = (Array.getData(i)) % n;
      sMarker.move(spos, null, null);
      lang.nextStep();

      while (Array2.getData((spos) % n) != 0) {
        codeSupport.unhighlight(1, 0, false);

        codeSupport.highlight(2, 0, false);
        lang.nextStep();
        codeSupport.unhighlight(2, 0, false);
        codeSupport.highlight(4, 0, false);
        lang.nextStep();
        codeSupport.unhighlight(4, 0, false);
        codeSupport.highlight(1, 0, false);
        spos++;
        spos = spos % n;
        sMarker.move(spos, null, null);
        lang.nextStep();
      }
      codeSupport.unhighlight(0, 0, false);
      codeSupport.unhighlight(1, 0, false);
      codeSupport.highlight(2, 0, false);
      lang.nextStep();
      codeSupport.unhighlight(2, 0, false);
      codeSupport.highlight(3, 0, false);
      lang.nextStep();
      Array2.put(spos, Array.getData(i), null, null);
      Array2.highlightElem(spos, null, null);
      Array.unhighlightElem(i, null, null);
      Array.highlightCell(i, null, null);
      lang.nextStep();

      codeSupport.unhighlight(3, 0, false);
      lang.nextStep();
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * generator.Generator#generate(generator.properties.AnimationPropertiesContainer
   * , java.util.Hashtable)
   */
  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    in = (int[]) arg1.get("in");
    n = in.length;
    arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");
    markerProps = (ArrayMarkerProperties) arg0
        .getPropertiesByName("markerProps");
    sourceCodeProps = (SourceCodeProperties) arg0
        .getPropertiesByName("sourceCodeProps");
    out = new int[n];

    IntArray ia = lang.newIntArray(new Coordinates(20, 100), in, "intArray",
        null, arrayProps);

    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK);
    IntArray ib = lang.newIntArray(new Coordinates(700, 100), out, "intArray2",
        null, arrayProps);

    lang.nextStep();
    pseudoCode = lang.newText(new Coordinates(200, 100),
        "Pseudo Code:Lineares Sondieren mit h(k)=k", "pseudocode", null);
    pseudoCode.setFont(new Font("Serif", Font.BOLD, 18), null, null);
    SourceCode sc = lang.newSourceCode(new Coordinates(200, 110), "sourceCode",
        null, sourceCodeProps);

    sc.addCodeLine("setze i=0", null, 0, null); // 0
    sc.addCodeLine("shritt 2 :brechne s(i,k)= (h(k)+i) mod m", null, 0, null); // 1
    sc.addCodeLine("falls s(i,k) frei ist ", null, 1, null); // 2
    sc.addCodeLine("      trage es dort", null, 1, null); // 3
    sc.addCodeLine("      andernfalls ,i++ ,shritt 2", null, 1, null); // 4

    lang.nextStep();

    try {
      // Start SelectionSort
      Hashing(ia, ib, sc);

    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }

    return lang.toString();
  }


  @Override
  public String getAlgorithmName() {
    return "Hashing mit linearer Sondierung";
  }

  @Override
  public String getCodeExample() {
    return "setze i=0" + "\n" + "shritt 2 :brechne s(i,k)= (h(k)+i) mod m"
        + "\n" + "falls s(i,k) frei ist " + "\n" + "      trage es dort" + "\n"
        + "      andernfalls ,i++ ,shritt 2";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Das Hashverfahren ist ein Algorithmus zum Suchen von Datenobjekten in großen Datenmengen. Es basiert auf der Idee,"
        + "\n"
        + " dass eine mathematische Funktion die Position eines Objektes in einer Tabelle berechnet. Dadurch erübrigt sich die "
        + "\n"
        + "Durchsuchung vieler Datenobjekte, bis das Zielobjekt gefunden wurde."
        + "in diesem beispiel ist die Hashfunktion h(k)=k";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
  }

  @Override
  public String getName() {
    return "Lineares Sondieren mit h(k)=k";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public String getAnimationAuthor() {
    return "Saffar Kamel";
  }

}
