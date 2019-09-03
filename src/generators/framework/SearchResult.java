package generators.framework;

public class SearchResult {
  public Generator generator;
  public int score;
  
  public SearchResult(Generator gen, int searchScore) {
    generator = gen;
    score = searchScore;
  }
}
