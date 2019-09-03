package generators.misc;

import java.util.Hashtable;
import java.util.Locale;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.InternationalizedGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class DemoGenerator extends InternationalizedGenerator {

  public DemoGenerator() {
    this("resources/Demo", Locale.GERMANY);
  }
  
  public DemoGenerator(String resource, Locale targetLocale) {
    super(resource, targetLocale);
  }
  
  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // TODO Auto-generated method stub
    return "";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public void init() {
    System.err.println("INIT");
  }

  public static void main(String[] args) {
    InternationalizedGenerator x = new DemoGenerator();
    System.err.println("---------");
    System.err.println(x.checkValidity());
    System.err.println("+++++++++");
    System.err.println("---------");
    System.err.println(InternationalizedGenerator.checkValidity("resources/Demo", Locale.GERMANY));
    System.err.println("+++++++++");
  }

}
