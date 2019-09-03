package generators.framework;

import java.util.Locale;

import translator.Translator;

public abstract class I18NGenerator implements Generator {
  protected Locale locale;
  protected Translator translator;
  
  public I18NGenerator(String baseResourceName) {
    this(baseResourceName, Locale.US);
  }
  
  public I18NGenerator(String baseResourceName, Locale chosenLocale) {
    locale = chosenLocale;
    if (locale == null)
      locale = Locale.US;
    translator = new Translator(baseResourceName, locale);
  }

  @Override
  public String getAlgorithmName() {
    return translator.translateMessage("algoName");
  }

  @Override
  public Locale getContentLocale() {
    return locale;
  }

  @Override
  public String getDescription() {
    return translator.translateMessage("algoDesc");
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public String getName() {
    return translator.translateMessage("algoDisplayName");
  }
}
