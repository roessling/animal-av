package generators.framework;

import java.util.Locale;

import translator.Translator;

public abstract class InternationalizedGenerator implements Generator {
  /** 
   * the internal Translator for looking up the language resources
   */
  protected Translator internalTranslator = null;

  /**
   * the name for the source resources
   */
  protected String resourceName = null;
  
  /**
   * the concrete locale used
   */
  protected Locale targetLocale = null;
  

  /**
   * the keys to test for
   */
  private final static String[] keys = {"algorithmName", "animationAuthor", "codeExample", 
      "description", "name", "outputLanguage"};
  
  /**
   * creates the internal translator for the base file name and the target locale
   * 
   * @param baseFile the base file name for the resources
   * @param locale the target locale, e.g. Locale.US or Locale.GERMANY
   */
  public InternationalizedGenerator(String baseFile, Locale locale) {
    resourceName = baseFile;
    targetLocale = locale;
    internalTranslator = new Translator(baseFile, locale);
  }

  /**
   * returns an empty String if the resource file contains proper entries for all required
   * keys, else returns a String listing all missing entries.
   * This method is intentionally static to prevent the need to instantiate or "store"
   * a large set of concrete instances of this class for the purpose of testing multiple
   * generator instances for completeness
   * 
   * @return an empty String if the requested resource exists and contains all entries, else
   * returns a String with a list of all missing resources
   * with a print-out to System.out.println
   */
  public String checkValidity() {
    return InternationalizedGenerator.checkValidity(resourceName, targetLocale);
  }
  
  /**
   * returns an empty String if the resource file contains proper entries for all required
   * keys, else returns a String listing all missing entries.
   * This method is intentionally static to prevent the need to instantiate or "store"
   * a large set of concrete instances of this class for the purpose of testing multiple
   * generator instances for completeness
   * 
   * @return an empty String if the requested resource exists and contains all entries, else
   * returns a String with a list of all missing resources
   * with a print-out to System.out.println
   */
  public static String checkValidity(String baseFile, Locale locale) {
    String code = "[" + baseFile +'.' + locale.toString() + "] ";
    String value = null;
    StringBuilder sb = new StringBuilder(1024);
    Translator testTranslator = new Translator(baseFile, locale);
    for (String key : keys) {
      value = testTranslator.translateMessage(key);
      if (value == null || value.length() == 0)
        sb.append(code).append("key " +key +" has no value with a length of at least 1.");
    }

    return sb.toString();
  }
  
  /**
   * returns the associated value or en empty (non-null) String if there was
   * no associated value present
   * @param key the key to look up
   * @return either the value in the resource file or the empty String "".
   */
  private String returnEmptyOnError(String key) {
    String result = internalTranslator.translateMessage(key);
    return (result != null) ? result : "";
  }

  @Override
  public String getAlgorithmName() {
    return returnEmptyOnError("algorithmName");
  }

  @Override
  public String getAnimationAuthor() {
    return returnEmptyOnError("animationAuthor");
  }

  @Override
  public String getCodeExample() {
    return returnEmptyOnError("codeExample");
  }

  @Override
  public Locale getContentLocale() {
    return targetLocale;
  }

  @Override
  public String getDescription() {
    return returnEmptyOnError("description");
  }
  

  @Override
  public String getName() {
    return returnEmptyOnError("name");
  }

  @Override
  public String getOutputLanguage() {
    return returnEmptyOnError("outputLanguage");
  }
}
