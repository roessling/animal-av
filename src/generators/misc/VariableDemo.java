package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class VariableDemo implements Generator {
  private Language lang;

  public void init() {
    lang = new AnimalScript("Variable Demo", "Guido Roessling", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    Variables v = lang.newVariables();
    SourceCodeProperties scp = new SourceCodeProperties();
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    SourceCode sc = lang
        .newSourceCode(new Coordinates(20, 50), "sc", null, scp);
    sc.addCodeLine("public int f() {", "2", 0, null);
    sc.addCodeLine("int x = 0;", "3", 1, null);
    sc.addCodeLine("int y = 7;", "4", 1, null);
    sc.addCodeLine("int z = 6;", "5", 1, null);
    sc.addCodeLine("x = y *z;", "6", 1, null);
    sc.addCodeLine("return x;", "6", 1, null);
    sc.addCodeLine("}", "7", 0, null);
    sc.addCodeLine("int x = 10;", "1", 0, null);
    sc.addCodeLine("f(); // x remains 10 here", "9", 0, null);
    lang.nextStep();
    sc.highlight("1");
    v.declare("int", "x", "10");
    lang.nextStep();
    sc.unhighlight("1");
    sc.highlight("2");
    v.openContext();
    lang.nextStep();
    sc.unhighlight("2");
    sc.highlight("3");
    v.declare("int", "x", "0");
    lang.nextStep();
    sc.unhighlight("3");
    sc.highlight("4");
    v.declare("int", "y", "7");
    lang.nextStep();
    sc.unhighlight("4");
    sc.highlight("5");
    v.declare("int", "z", "6");
    lang.nextStep();
    sc.unhighlight("5");
    sc.highlight("6");
    v.set("x", String.valueOf(7 * 6));
    lang.nextStep();
    sc.unhighlight("6");
    sc.highlight("7");
    lang.nextStep();
    v.closeContext();
    sc.highlight("9");
    return lang.toString();
  }

  public String getName() {
    return "Variable Demo";
  }

  public String getAlgorithmName() {
    return "Variable Demo";
  }

  public String getAnimationAuthor() {
    return "Guido Rößling";
  }

  public String getDescription() {
    return "Demo on using the Variable window";
  }

  public String getCodeExample() {
    return "Demo on using the Variable window";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}