package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.candidateElimination.Description;
import generators.helpers.candidateElimination.Example;
import generators.helpers.candidateElimination.ExampleGenerator;
import generators.helpers.candidateElimination.Rule;
import generators.helpers.candidateElimination.SourceCode;
import generators.helpers.candidateElimination.StringSet;
import generators.helpers.candidateElimination.Table;
import generators.helpers.candidateElimination.TableMarker;
import generators.helpers.candidateElimination.Title;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class CandidateElimination implements Generator {

  private Language          lang;

  private static int        numberOfFeatures;
  private static String[][] featureValues;

  private StringSet         gStringSet;
  private StringSet         sStringSet;
  private Table             table;
  private TableMarker       tableMarker;
  private SourceCode        sourceCode;
  private Description       description;

  // private Title title;

  public CandidateElimination() {
    init();
  }

  public void candidateElimination(Example[] e) {
    boolean incrFlag = false;
    int i = 0;
    // CodeLine 1
    Set<Rule> gSet = Rule.initGSet();
    // CodeLine 2
    Set<Rule> sSet = Rule.initSSet();

    this.sourceCode.highlight(0);
    this.sourceCode.highlight(1);
    this.lang.nextStep();
    updateSets(gSet, sSet);

    this.tableMarker.show();
    this.sourceCode.unhighlight(0);
    this.sourceCode.unhighlight(1);

    // CodeLine 3

    int sPointer;
    int gPointer;
    for (Example example : e) {
      this.sourceCode.highlight(2);
      this.lang.nextStep();

      if (i > 0)
        this.tableMarker.increment();
      i++;

      // CodeLine 4
      if (example.classified) {
        this.sourceCode.toggleHighlight(2, 3);
        this.lang.nextStep();
        this.sourceCode.toggleHighlight(3, 4);
        this.lang.nextStep();
        deleteNotCoveringRules(sSet, gSet, example, true);
        // CodeLine 7
        sPointer = 0;

        this.sStringSet.showMarker("does s not cover e? then generalize");
        incrFlag = false;

        this.sourceCode.unhighlight(4);
        this.sourceCode.unhighlight(5);
        for (Rule rule : sSet) {
          this.sourceCode.highlight(6);
          this.lang.nextStep();

          if (incrFlag)
            this.sStringSet.incrementMarker();

          // CodeLine 7 also
          if (rule.classify(example) != example.classified) {

            sSet.remove(rule);

            this.sStringSet.highlight(sPointer++);
            // updateSets(gSet, sSet);

            this.sourceCode.toggleHighlight(6, 7);
            this.lang.nextStep();

            updateSets(gSet, sSet);
            this.sourceCode.toggleHighlight(7, 8);
            this.sourceCode.highlight(9);
            this.lang.nextStep();

            // CodeLine 9.1
            Rule h = rule.generalize(example);

            this.sourceCode.toggleHighlight(9, 10);
            this.lang.nextStep();
            this.sourceCode.toggleHighlight(10, 11);
            this.lang.nextStep();
            // CodeLine 9.2 and 9.3
            if ((h.classify(example) == example.classified)
                && h.isMoreSpecial(gSet)) {
              // CodeLine 9 also
              sSet.add(h);
            }

          }
          incrFlag = true;

        }

        updateSets(gSet, sSet);

        this.sourceCode.unhighlight(8);
        this.sourceCode.toggleHighlight(11, 12);
        this.sourceCode.highlight(13);
        this.lang.nextStep();

        Rule[] setArray = sSet.toArray(new Rule[sSet.size()]);
        for (int j = 0; j < setArray.length; j++) {
          if (setArray[j].isMoreSpecial(sSet))
            sSet.remove(setArray[j]);
        }

        updateSets(gSet, sSet);
        this.sourceCode.unhighlight(12);
        this.sourceCode.unhighlight(13);
        // CodeLine 10
      } else {
        this.sourceCode.toggleHighlight(2, 14);
        this.lang.nextStep();
        this.sourceCode.toggleHighlight(14, 15);
        this.lang.nextStep();
        // CodeLine 11
        deleteNotCoveringRules(sSet, gSet, example, false);

        gPointer = 0;

        this.gStringSet.showMarker("does g cover e? then specialize");
        // CodeLine 14
        incrFlag = false;

        this.sourceCode.unhighlight(15);
        this.sourceCode.unhighlight(16);
        Rule[] gSetArray = gSet.toArray(new Rule[gSet.size()]);
        for (int k = 0; k < gSetArray.length; k++) {
          Rule rule = gSetArray[k];

          this.sourceCode.highlight(17);
          this.lang.nextStep();

          if (incrFlag)
            this.gStringSet.incrementMarker();

          // CodeLine 14 also

          if (rule.classify(example) != example.classified) {

            // CodeLine 15
            gSet.remove(rule);
            this.gStringSet.highlight(gPointer++);

            this.sourceCode.toggleHighlight(17, 18);
            this.lang.nextStep();

            updateSets(gSet, sSet);
            // CodeLine 16.1
            Set<Rule> hSet = rule.specialize(example);

            this.sourceCode.toggleHighlight(18, 19);
            this.sourceCode.highlight(20);
            this.lang.nextStep();

            Rule[] setArray = hSet.toArray(new Rule[hSet.size()]);
            for (int j = 0; j < setArray.length; j++) {
              Rule h = setArray[j];

              this.sourceCode.unhighlight(22);
              this.sourceCode.toggleHighlight(20, 21);
              this.lang.nextStep();
              this.sourceCode.toggleHighlight(21, 22);
              this.lang.nextStep();

              // CodeLine 16.2 and 16.3
              if ((h.classify(example) != example.classified)
                  || !h.isMoreGenerell(sSet)) {
                hSet.remove(h);
              }
            }
            // CodeLine 16 also
            gSet.addAll(hSet);

            updateSets(gSet, sSet);
            incrFlag = true;
          }
          this.sourceCode.unhighlight(17);
          this.sourceCode.unhighlight(19);
          this.sourceCode.toggleHighlight(22, 23);
          this.sourceCode.highlight(24);
          this.lang.nextStep();

          Rule[] setArray = gSet.toArray(new Rule[gSet.size()]);
          for (int j = 0; j < setArray.length; j++) {
            if (setArray[j].isMoreGenerell(gSet))
              gSet.remove(setArray[j]);
          }
          updateSets(gSet, sSet);

          this.sourceCode.unhighlight(23);
          this.sourceCode.unhighlight(24);

        }

      }
    }
  }

  /**
   * @param gSet
   * @param sSet
   */
  private void updateSets(Set<Rule> gSet, Set<Rule> sSet) {
    this.gStringSet.update(gSet);
    this.sStringSet.update(sSet);
    this.lang.nextStep();
  }

  private void deleteNotCoveringRules(Set<Rule> sSet, Set<Rule> gSet,
      Example example, boolean isGSet) {
    Set<Rule> usedSet = (isGSet) ? gSet : sSet;
    int lineToHighlight = (isGSet) ? 4 : 15;
    Rule[] setArray = usedSet.toArray(new Rule[usedSet.size()]);

    if (isGSet)
      this.gStringSet.showMarker("does g not cover e? then remove");
    else
      this.sStringSet.showMarker("does s cover e? then remove");
    this.lang.nextStep();
    // CodeLine 5 and 12
    for (int i = 0; i < setArray.length; i++) {
      Rule rule = setArray[i];

      // CodeLine 5 also and 12 also
      if (rule.classify(example) != example.classified) {

        // CodeLine 6 and 13
        usedSet.remove(rule);
        this.sourceCode.toggleHighlight(lineToHighlight, lineToHighlight + 1);
        this.lang.nextStep();

        if (isGSet)
          this.gStringSet.highlight(i);
        else
          this.sStringSet.highlight(i);

        this.lang.nextStep();
      }
      if (isGSet) {
        if (i >= setArray.length - 1)
          this.gStringSet.hideMarker();
        else {
          this.sourceCode.toggleHighlight(lineToHighlight + 1, lineToHighlight);
          this.lang.nextStep();
          this.gStringSet.incrementMarker();
        }
      } else {
        if (i >= setArray.length - 1)
          this.sStringSet.hideMarker();
        else
          throw new IllegalStateException();
      }
    }
    updateSets(gSet, sSet);
  }

  public static int getNumberOfConditions() {
    return numberOfFeatures;
  }

  public static String[] getFeatureValues(int i) {
    return featureValues[i];
  }

  /**
   * @see generators.framework.Generator#generate(generators.framework.properties.AnimationPropertiesContainer,
   *      java.util.Hashtable)
   */
  @Override
  public String generate(AnimationPropertiesContainer animProps,
      Hashtable<String, Object> primProps) {

    String[][] instances = ExampleGenerator.generateInstances(primProps);
    String[][] featureValues = ExampleGenerator
        .generateFeatureValues(instances);
    Example[] e = ExampleGenerator.generateExamples(instances);

    CandidateElimination.numberOfFeatures = featureValues.length;
    CandidateElimination.featureValues = featureValues;

    new Title(this.lang);
    this.description = new Description(this.lang);
    this.sourceCode = new SourceCode(this.lang);
    this.sStringSet = new StringSet("S", new Coordinates(50, 450), animProps,
        this.lang);
    this.gStringSet = new StringSet("G", new Coordinates(50, 550), animProps,
        this.lang);
    this.sourceCode.hide();
    this.lang.nextStep();

    TextProperties cutlineProps = new TextProperties("cutProp");
    cutlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.ITALIC, 12));

    // notes
    this.lang.newText(new Offset(-50, 50, "sourceCode", "S"),
        "Gruen: Positive Klasse, Rot: Negative Klasse", "cutline1", null,
        cutlineProps);
    this.lang.newText(new Offset(0, 10, "cutline1", "SW"),
        "? = akzeptiert alle Werte", "cutline2", null, cutlineProps);
    this.lang.newText(new Offset(0, 10, "cutline2", "SW"),
        "{} = akzeptiert keinen Wert", "cutline3", null, cutlineProps);

    this.sourceCode.show();
    this.description.hide();
    this.table = new Table(instances, animProps, this.lang);
    this.tableMarker = new TableMarker(this.table, this.lang);
    this.tableMarker.hide();
    this.lang.nextStep();

    candidateElimination(e);

    return this.lang.toString();
  }

  /**
   * @see generators.framework.Generator#getAlgorithmName()
   */
  @Override
  public String getAlgorithmName() {
    return "Candidate Elimination";
  }

  /**
   * @see generators.framework.Generator#getAnimationAuthor()
   */
  @Override
  public String getAnimationAuthor() {
    return "Christian Brinker, Mateusz Parzonka";
  }

  /**
   * @see generators.framework.Generator#getCodeExample()
   */
  @Override
  public String getCodeExample() {
    return "G = set of maximally general hypotheses\nS = set of maximally specific "
        + "hypotheses\n\nFor each training example e\n  if e is positive\n    "
        + "For each hypothesis g in G that does not cover e\n      remove g "
        + "from G\n    For each hypothesis s in S that does not cover e\n      "
        + "remove s from S\n      S = S all hypotheses h such that\n        h "
        + "is a minimal generalization of s\n        h covers e\n        some "
        + "hypothesis in G is more general than h\n      remove from S any "
        + "hypothesis that is more general\n      than another hypothesis in "
        + "S\n  if e is negative\n    For each hypothesis s in S that does "
        + "cover e\n      remove s from S\n    For each hypothesis g in G that "
        + "does cover e\n      remove g from G\n      G = G all hypotheses h "
        + "such that\n        h is a minimal specialization of g\n        h "
        + "covers e\n        some hypothesis in S is more special than h\n    "
        + "  remove from G any hypothesis that is more special\n      than another"
        + " hypothesis in G";
  }

  /**
   * @see generators.framework.Generator#getContentLocale()
   */
  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  /**
   * @see generators.framework.Generator#getDescription()
   */
  @Override
  public String getDescription() {
    return "Der Candidate Elimination Algorithmus sucht mit Hilfe einer Reihe von "
        + "Trainingsbeispielen die Grenzen des sogenannten Version Space. "
        + "Dieser bildet den Bereich aller Regeln ab, die die Trainingsbeispiele "
        + "korrekt mit Hilfe einer einzigen Regel klassifizieren koennen.";
  }

  /**
   * @see generators.framework.Generator#getFileExtension()
   */
  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /**
   * @see generators.framework.Generator#getGeneratorType()
   */
  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  /**
   * @see generators.framework.Generator#getName()
   */
  @Override
  public String getName() {
    return "Candidate Elimination";
  }

  /**
   * @see generators.framework.Generator#getOutputLanguage()
   */
  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * @see generators.framework.Generator#init()
   */
  @Override
  public void init() {
    this.lang = new AnimalScript("Candidate Elimination",
        "Christian Brinker, Mateusz Parzonka", 640, 480);
    this.lang.setStepMode(true);
  }
}
