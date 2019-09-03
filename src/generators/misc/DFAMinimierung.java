package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpers.Minimierung;
import generators.misc.helpers.Minimierung.MyException;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class DFAMinimierung implements Generator {
  private Language     lang;
  private int[]        AkzeptierendeZustaende;
  private String[][]   Uebergangsfunktion;
  SourceCodeProperties SourceCode;

  public String getCodeExample() {
    return "PseudoCode"
        + "\n"
        + "Teile die akzeptierenden und nicht akzeptierenden jeweils einer Klasse zu;"
        + "\n"
        + "Solange bis keine neuen Klassen gefunden wurden"
        + "\n"
        + "     Für jede Klasse"
        + "\n"
        + "          Für jedes Element"
        + "\n"
        + "               Wenn das Element noch nicht zugeteilt ist"
        + "\n"
        + "                    >Klasse mit dem Element erstellen"
        + "\n"
        + "                    ->Für alle anderen noch nicht zugeteilten Elemente x"
        + "\n" + "                         Ist x äquivalent zum Element" + "\n"
        + "                              ->x in die Klasse mit einfügen";
  }

  public void init() {
    lang = new AnimalScript("MinimierungDFA", "Eliyas Tamrat,Peter Glöckner",
        10000, 10000);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    AkzeptierendeZustaende = (int[]) primitives.get("AkzeptierendeZustaende");
    SourceCode = (SourceCodeProperties) props.getPropertiesByName("SourceCode");
    Uebergangsfunktion = (String[][]) primitives.get("Uebergangsfunktion");

    try {
      Minimierung min = new Minimierung(lang, AkzeptierendeZustaende,
          Uebergangsfunktion, SourceCode);
      min.run();
      lang.nextStep();
      min.showResult();
      lang.nextStep();
      min.hideAll();
      min.showEnd();
      lang.nextStep();
    } catch (MyException e) {
      lang.addError(e.getMessage());
      TextProperties y = new TextProperties();
      y.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 0, 0));
      y.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Sans Serif",
          Font.BOLD, 20));
      lang.newText(new Coordinates(100, 100), e.getMessage(), "error", null, y);
    }

    return lang.toString();
  }

  public String getName() {
    return "DFAMinimierung";
  }

  public String getAlgorithmName() {
    return "DFAMinimierung";
  }

  public String getAnimationAuthor() {
    return "Eliyas Tamrat, Peter Glöckner";
  }

  public String getDescription() {
    return "Da die Zustände des Minimalautomaten den Äquivalenzklassen der vom endlichen Automaten M <br />"
        + "\n"
        + "akzeptierten Sprache unter der Nerode-Relation entsprechen, spricht man auch vom Äquivalenzklassenautomat.<br />"
        + "\n"
        + "Die Minimierung eines deterministischen Automaten kann algorithmisch durch fortwährende Verfeinerung der <br />"
        + "\n"
        + "Äquivalenzklassen gelöst werden. Man beginnt mit den Zustandsmengen F und Q − F. Für jeden Buchstaben aus <br />"
        + "\n"
        + " dem Alphabet wird nun jede Zustandsmenge dahingehend aufgeteilt, dass die Überführungsfunktion die Zustände <br />"
        + "\n"
        + " jeder neuen Menge den Buchstaben in eine jeweils eindeutige Menge abbildet.<br />"
        + "\n"
        + " Dies wird so oft wiederholt bis sich keine Änderung mehr ergibt. (Zitat Wiki).<br />"
        + "\n"
        + "(Ein Leerzeichen wird nicht als Buchstabe sondern als keine Transition interpretiert.)<br />";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /*
   * public static void main(String[] args){
   * 
   * 
   * String[][] matrix = new String[6][6]; matrix =
   * Minimierung.initMatrix(matrix); matrix[0][0] = " "; matrix[0][1] = "a";
   * matrix[0][2] = "b"; matrix[1][0] = "a"; matrix[1][2] = "b"; matrix[2][3] =
   * "a"; matrix[2][4] = "b"; matrix[3][3] = "b"; matrix[3][5] = "a";
   * matrix[4][2] = "b"; matrix[4][5] = "a"; matrix[5][3] = "b"; matrix[5][5] =
   * "a";
   * 
   * int[] accepts = {2,4};
   * 
   * DFAMinimierung min = new DFAMinimierung(); min.init();
   * 
   * Hashtable<String, Object> primitives = new Hashtable<String, Object>();
   * primitives.put("AkzeptierendeZustaende", accepts);
   * primitives.put("Uebergangsfunktion", matrix);
   * 
   * SourceCodeProperties SourceCode = new SourceCodeProperties();
   * SourceCode.setName("SourceCode");
   * 
   * AnimationPropertiesContainer props = new AnimationPropertiesContainer();
   * props.add(SourceCode);
   * 
   * 
   * System.out.println(min.generate(props, primitives));
   * 
   * }
   */

}