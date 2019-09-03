package generators.framework;

import java.util.HashMap;
import java.util.Vector;

public class GeneratorSearcher {
  private Vector<Generator> generators;
  private final static int SCORE_MATCHING_AUTHOR = 1;
  private final static int SCORE_MATCHING_CODE = 2;
  private final static int SCORE_MATCHING_DESCRIPTION = 4;
  private final static int SCORE_MATCHING_ALGORITHM = 8;
  private final static int SCORE_MATCHING_NAME = 16;
  private static final int MAX_SCORE = SCORE_MATCHING_ALGORITHM 
    + SCORE_MATCHING_AUTHOR 
    + SCORE_MATCHING_CODE
    + SCORE_MATCHING_DESCRIPTION
    + SCORE_MATCHING_NAME;
  
  public GeneratorSearcher(Vector<Generator> generatorEntries) {
    generators = generatorEntries;  
  }
  
  protected Vector<SearchResult> assembleResults(String queryString) {
    HashMap<Integer, Vector<Generator>> scores = internalResults(queryString);
    Vector<SearchResult> results = new Vector<SearchResult>(generators.size());
    for (int score = MAX_SCORE; score > 0; score--) {
      Vector<Generator> currentGens = scores.get(Integer.valueOf(score));
      for (Generator gen : currentGens)
        results.add(new SearchResult(gen, score));
    }
    results.trimToSize();
    return results;    
  }
 
  private HashMap<Integer, Vector<Generator>> internalResults(String queryString) {
    HashMap<Integer, Vector<Generator>> scoreTable = 
      new HashMap<Integer, Vector<Generator>>(MAX_SCORE + 13);
    for (int i = 0; i <= MAX_SCORE; i++)
      scoreTable.put(Integer.valueOf(i), new Vector<Generator>(25));
    int currentScore = 0, maxScore = -1; //, nrEntries = 0;
    for (Generator gen : generators) {
      // score entry and add to HashMap if score greater than 0
      // also update max score if this is higher than before
      currentScore = scoreEntry(queryString, gen);
      if (currentScore > 0) {
        if (scoreTable.containsKey(Integer.valueOf(currentScore))) {
          scoreTable.get(Integer.valueOf(currentScore)).add(gen);
        }
        if (currentScore > maxScore)
          maxScore = currentScore;
//        nrEntries++;
      }
    }
    
    return scoreTable;
  }
  
  public int scoreEntry(String queryString, Generator gen) {
    if (gen == null || queryString == null)
      return 0;
    int score = 0;
    String query = queryString.trim().toLowerCase();
//    StringBuffer sb = new StringBuffer(250);
//    sb.append("q: ").append(queryString).append(" ");
//    sb.append(gen.getName()).append(' ').append(gen.getAlgorithmName());
//    sb.append(gen.getAnimationAuthor()).append(' ').append(gen.getDescription());
//    sb.append(gen.getCodeExample());
    // score for name
    if (gen.getName().toLowerCase().indexOf(query) != -1)
      score += SCORE_MATCHING_NAME;
    
    // score for algorithm name
    if (gen.getAlgorithmName().toLowerCase().indexOf(query) != -1)
      score += SCORE_MATCHING_ALGORITHM;

    // score for generator author
    if (gen.getAnimationAuthor().toLowerCase().indexOf(query) != -1)
      score += SCORE_MATCHING_AUTHOR;

    // score for description
    if (gen.getDescription().toLowerCase().indexOf(query) != -1)
      score += SCORE_MATCHING_DESCRIPTION;
    
    // score for code example
    if (gen.getCodeExample().toLowerCase().indexOf(query) != -1)
      score += SCORE_MATCHING_CODE;
    
//    sb.append(", score: " +score);
//    System.err.println(sb.toString());
    return score;
  }
  
  public static void main(String[] args) {
    Vector<Generator> gens = new Vector<Generator>(31);
    Generator a, b, c;
    a = new generators.sorting.AnimalShakerSort();
    b = new generators.sorting.shakersort.AnnotatedShakerSort();
    c = new generators.sorting.bubblesort.AnnotatedBubbleSort();
    gens.add(a);
    gens.add(b);
    gens.add(c);
    GeneratorSearcher searcher = new GeneratorSearcher(gens);
    System.err.println("Score for Shake: " + searcher.scoreEntry("Shake", a));
    System.err.println("Score for Shake: " + searcher.scoreEntry("Shake", b));
    System.err.println("Score for Shake: " + searcher.scoreEntry("Shake", c));
    System.err.println("Score for Sorting: " + searcher.scoreEntry("Sorting", a));
    System.err.println("Score for Sorting: " + searcher.scoreEntry("Sorting", b));
    System.err.println("Score for Sorting: " + searcher.scoreEntry("Sorting", c));
    System.err.println("Score for sorting: " + searcher.scoreEntry("sorting", a));
    System.err.println("Score for sorting: " + searcher.scoreEntry("sorting", b));
    System.err.println("Score for sorting: " + searcher.scoreEntry("sorting", c));
    System.err.println("Score for ShakerSort: " + searcher.scoreEntry("ShakerSort", a));
    System.err.println("Score for ShakerSort: " + searcher.scoreEntry("ShakerSort", b));
    System.err.println("Score for ShakerSort: " + searcher.scoreEntry("ShakerSort", c));
    System.err.println("Score for bubbLE: " + searcher.scoreEntry("bubbLE", a));
    System.err.println("Score for bubbLE: " + searcher.scoreEntry("bubbLE", b));
    System.err.println("Score for bubbLE: " + searcher.scoreEntry("bubbLE", c));
    System.err.println("Sorted by score...:");
    Vector<SearchResult> res = searcher.assembleResults("bubbLE");
    for (SearchResult result: res)
      System.err.println(result.generator.getName() + " - score " +result.score);
  }
}
