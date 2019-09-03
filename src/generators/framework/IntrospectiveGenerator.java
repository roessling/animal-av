package generators.framework;

public interface IntrospectiveGenerator extends Generator {
  public static final int DYNAMIC_ANIMATION = 1;
  
  public static final int INTRODUCTION = 2;
  
  public static final int ASSOCIATED_CODE_WITHOUT_HIGHLIGHTING = 4;
  
  public static final int HIGHLIGHTED_ASSOCIATED_CODE = 8;
  
  public static final int USER_PROVIDED_INPUT = 16;
  
  public static final int USER_ADAPTABLE_LAYOUT = 32;
  
  public static final int ASSIGNMENT_COUNTERS = 64;
  
  public static final int SUMMARY_AT_END = 128;
  
  public static final int COMPLEXITY_INFORMATION = 256;
  
  public static final int INTEGRATED_QUESTIONS = 512;
  
  /**
   * returns the operations supported by this concrete generator
   */
  public int getSupportedOperations();
}
