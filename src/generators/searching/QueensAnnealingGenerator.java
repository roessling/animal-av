/*
 * Queens_Annealing_Generator_DE.java
 * Manuel Brack, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.searching;

import algoanim.primitives.Text;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;
import java.util.ResourceBundle;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import translator.Translator;

public class QueensAnnealingGenerator implements ValidatingGenerator {
    private Language lang;
    private RectProperties thermometerProps;
    private SourceCodeProperties sourceCodeProps;
    private double coolingRate;
    private PolygonProperties chessBoardProps;
    private int numberOfQueens;
    private int initialTemperature;

    private QueensAnnealing generator;
    private Translator translator;
    private Locale locale;

    public QueensAnnealingGenerator(Locale locale)
    {
        this.locale = locale;
        //translator = ResourceBundle.getBundle("QueensAnnealingBundle", locale);
        translator = new Translator("resources/QueensAnnealingBundle", locale);

    }
    public void init() {
        lang = new AnimalScript("8 Queens Problem with Simulated Annealing", "Manuel Brack", 900, 700);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        thermometerProps = (RectProperties) props.getPropertiesByName("Thermometer");
        sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("Source Code");
        coolingRate = (double) primitives.get("Cooling Rate");
        chessBoardProps = (PolygonProperties) props.getPropertiesByName("Chessboard");
        numberOfQueens = (Integer) primitives.get("Number of Queens");
        initialTemperature = (Integer) primitives.get("Initial Temperature");

        generator = new QueensAnnealing(lang, thermometerProps, sourceCodeProps, chessBoardProps, translator);
        Solution sol = generator.find_solution(numberOfQueens, initialTemperature, coolingRate);
        this.addConclusion(sol);
        return lang.toString();
    }


    private void addConclusion(Solution sol) {
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 50));
        Text header = lang.newText(new Coordinates(40, 15), translator.translateMessage("header.mainDescription"), "header", null, headerProps);

        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 40));
        Text subHeader = lang.newText(new Coordinates(65, 100), translator.translateMessage("header.sub.description5"), "header", null, headerProps);

        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
        generator = new QueensAnnealing(null, thermometerProps, sourceCodeProps, chessBoardProps, null);
        long iterations = 0;
        int successs = 0;
        int attemps = 5000;
        long energy = 0;

        for (int i = 0; i < attemps; i++) {
            Solution solt = generator.find_solution(numberOfQueens, initialTemperature, coolingRate);
            if (solt.energy == 0) {
                successs++;
            }
            energy += solt.energy;
            iterations += solt.iterations;
        }

        double iterationsMean = (iterations * 1.0 / attemps);
        double successRate = (double) successs / attemps;
        double energyMean = (double) energy / attemps;
        String text = "";
        if (sol.energy == 0) {
            text = translator.translateMessage("solution.line1.1");
            if (successRate > 0.8) {
                if (sol.iterations / iterationsMean > 1.2) {
                    text += translator.translateMessage("solution.line1.2");
                } else {
                    text += translator.translateMessage("solution.line1.3");
                }
            } else {
                text += translator.translateMessage("solution.line1.4");
            }

        } else {
            text = translator.translateMessage("solution.line1.5");
        }
        Text description1 = lang.newText(new Offset(0, 20, subHeader, "SW"), text, "header", null, headerProps);
        Text description2 = lang.newText(new Offset(0, 70, subHeader, "SW"), String.format(translator.translateMessage("solution.line2"), successRate * 100, iterationsMean), "header", null, headerProps);
        Text description3 = lang.newText(new Offset(0, 95, subHeader, "SW"), translator.translateMessage("solution.line3"), "header", null, headerProps);
        Text description4 = lang.newText(new Offset(0, 120, subHeader, "SW"), translator.translateMessage("solution.line4"), "header", null, headerProps);
        Text description5 = lang.newText(new Offset(0, 145, subHeader, "SW"), translator.translateMessage("solution.line5"), "header", null, headerProps);
        Text description6 = lang.newText(new Offset(0, 170, subHeader, "SW"), translator.translateMessage("solution.line6"), "header", null, headerProps);

        text = "";
        if (sol.energy > 0) {
            if (successRate > 0.7) {
                text = translator.translateMessage("solution.line7.1");
            } else {
                text = translator.translateMessage("solution.line7.2");
            }
        } else if (sol.iterations / iterationsMean > 1.2) {
            text = translator.translateMessage("solution.line7.3");
        }
        Text description7 = lang.newText(new Offset(0, 220, subHeader, "SW"), text, "header", null, headerProps);
        lang.nextStep(translator.translateMessage("stepDescription.conclusion"));
    }

    public String getName() {
        return "8 Queens Problem with Simulated Annealing";
    }

    public String getAlgorithmName() {
        return "Simulated Annealing";
    }

    public String getAnimationAuthor() {
        return "Manuel Brack | Jonathan Kolhas";
    }

    public String getDescription() {
        return translator.translateMessage("shortdescription.line1")
                + "\n"
                + translator.translateMessage("shortdescription.line2")
                + "\n"
                + translator.translateMessage("shortdescription.line3");
    }

    public String getCodeExample() {
        return "function SIMULATED-ANNEALING(NumberOfQueens, Temperature, CoolingRate)"
                + "\n"
                + "      init ChessBoard"
                + "\n"
                + "      currentState = INITIAL-STATE"
                + "\n"
                + "      for(t = 1 to inf)"
                + "\n"
                + "            if Temperature <= 0 OR VALUE(currentState) = 0 then "
                + "\n"
                + "                  return currentState"
                + "\n"
                + "			"
                + "\n"
                + "            nextState = RANDOM-STATE"
                + "\n"
                + "            ΔE = VALUE(currentState) - VALUE(nextState)"
                + "\n"
                + "            if - ΔE > 0 then"
                + "\n"
                + "                  currentState = nextState"
                + "\n"
                + "			"
                + "\n"
                + "            else if exp( -ΔE/Temperature) > RANDOM-NUMBER then"
                + "\n"
                + "                  currentState = nextState"
                + "\n"
                + "			"
                + "\n"
                + "            Temperature = COOLDOWN-TEMP(Temperature, t, CoolingRate)"
                + "\n";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return this.locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        thermometerProps = (RectProperties) props.getPropertiesByName("Thermometer");
        sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("Source Code");
        coolingRate = (double) primitives.get("Cooling Rate");
        chessBoardProps = (PolygonProperties) props.getPropertiesByName("Chessboard");
        numberOfQueens = (Integer) primitives.get("Number of Queens");
        initialTemperature = (Integer) primitives.get("Initial Temperature");

        if (initialTemperature < 1)
            return false;
        if (coolingRate < 0.1 || coolingRate > 0.99)
            return false;
        if (numberOfQueens < 2 || numberOfQueens > 10)
            return false;

        return true;
    }
}