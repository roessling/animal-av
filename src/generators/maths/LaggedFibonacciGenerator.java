package generators.maths;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Andre Pacak
 * @author Marc Semmler
 */
public class LaggedFibonacciGenerator implements ValidatingGenerator {

    private int fZero; //first value
    private int fOne; //second value
    private int range; // sets range for the number that gets generated
    private int n; //number of steps


    private SourceCodeProperties sourceCodeHighlightColor;
    private ArrayProperties arrayProperties;
    private ArrayMarkerProperties iMarkerProperties;
    private ArrayMarkerProperties jMarkerProperties;
    private ArrayMarkerProperties kMarkerProperties;

    private SourceCode src;
    private double probOfQuestions = 50;

    private Language language;

    private void showDescription() {
        this.showHeadline();
        SourceCode description = language.newSourceCode(
                new Offset(0, 60, "headline", AnimalScript.DIRECTION_NW),
                "description",
                null,
                sourceCodeHighlightColor);
        description
                .addCodeLine(
                        "Der Lagged Fibonacci Generator (LFG) ist ein Pseudozufallszahlengenerator. Er basiert auf ",
                        null, 0, null);
        description
                .addCodeLine(
                        "der Fibonacci Sequenz, bei der die Summe der letzten beiden Werte den neuen Wert bilden. Beim LFG ",
                        null, 0, null);
        description
                .addCodeLine(
                        "werden zwei beliebige Werte, welche nicht den gleichen Index haben, aus der vorherigen Sequenz genommen und kombiniert.",
                        null, 0, null);
        description.addCodeLine("", null, 0, null);
        description
                .addCodeLine(
                        "Die Kombination kann durch Addition, Substraktion, Multiplikation oder dem exklusiven Oder (XOR)",
                        null, 0, null);
        description
                .addCodeLine(
                        "erfolgen. Bei unserer Version haben wir uns fuer die Addition entschieden. Man nennt ihn dann additiven",
                        null, 0, null);
        description
                .addCodeLine(
                        "Lagged Fibonacci Generator. Danach wird der enstandene Wert mit einem festen Wert m modulo gerechnet, um eine Zahl ",
                        null, 0, null);
        description
                .addCodeLine(
                        "im Bereich von 0 bis m zu erhalten.",
                        null, 0, null);
    }

    private void highlightLine(int line) {
        for (int i = 0; i < 9; i++)
            this.src.unhighlight(i);
        this.src.highlight(line);
    }

    private void showHeadline() {
        TextProperties headerProps = new TextProperties();
        headerProps.set(
                AnimationPropertiesKeys.FONT_PROPERTY,
                new Font(Font.SANS_SERIF, Font.BOLD, 25));
        Text header = language.newText(
                new Coordinates(20, 20),
                "Lagged Fibonacci Generator",
                "headline",
                null,
                headerProps);
        RectProperties headerBorderProps = new RectProperties();
        headerBorderProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        headerBorderProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        headerBorderProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        Rect headerBorder = language.newRect(
                new Offset(-5, -5, "headline", AnimalScript.DIRECTION_NW),
                new Offset(5, 5, "headline", AnimalScript.DIRECTION_SE),
                "headerBorder",
                null,
                headerBorderProps);
    }

    private void showAlgorithm() {
        //show source code and description field and and all of the visualization
        this.showHeadline();

        this.src = language.newSourceCode(
                new Offset(0, 50, "headline", AnimalScript.DIRECTION_NW),
                "sourceCode",
                null,
                sourceCodeHighlightColor);
        src.addCodeLine("generate(f0, f1, range, n)", null, 0, null);
        src.addCodeLine("f = array of n", null, 1, null);
        src.addCodeLine("f[0] = f0", null, 1, null);
        src.addCodeLine("f[1] = f1", null, 1, null);
        src.addCodeLine("for(i = 2; i < n; i++)", null, 1, null);
        src.addCodeLine("k = rand(0,i)", null, 2, null);
        src.addCodeLine("j = rand[0,k)", null, 2, null);
        src.addCodeLine("f[i] = (f[j] + f[k]) mod range", null, 2, null);
        src.addCodeLine("return f[n-1]", null, 1, null);

        Random rand = new Random();

        Variables vars = language.newVariables();
        vars.declare("int", "k");
        vars.declare("int", "j");
        vars.declare("int", "i");
        vars.declare("int", "range");
        vars.declare("int", "n");

        this.showVariableState();
        TextProperties textProps = new TextProperties();
        Text kValue = language.newText(
                new Offset(50, -10, "variableStateLines2", AnimalScript.DIRECTION_W),
                "xxx",
                "kValue",
                null,
                textProps);
        Text jValue = language.newText(
                new Offset(0, 20, "kValue", AnimalScript.DIRECTION_NW),
                "xxx",
                "jValue",
                null,
                textProps);
        Text iValue = language.newText(
                new Offset(0, 20, "jValue", AnimalScript.DIRECTION_NW),
                "xxx",
                "iValue",
                null,
                textProps);
        Text rangeValue = language.newText(
                new Offset(0, 20, "iValue", AnimalScript.DIRECTION_NW),
                "xxx",
                "rangeValue",
                null,
                textProps);
        Text nValue = language.newText(
                new Offset(0, 20, "rangeValue", AnimalScript.DIRECTION_NW),
                "xxx",
                "nValue",
                null,
                textProps);


        Text stepDescriptionText = language.newText(
                new Offset(0, 100, "sourceCode", AnimalScript.DIRECTION_W),
                "",
                "stepDescription",
                null,
                textProps);
        Text stepDescriptionTextCont = language.newText(
                new Offset(0, 120, "sourceCode", AnimalScript.DIRECTION_W),
                "",
                "stepDescriptionCont",
                null,
                textProps);
        Text stepDescriptionTextCont2 = language.newText(
                new Offset(0, 140, "sourceCode", AnimalScript.DIRECTION_W),
                "",
                "stepDescriptionCont2",
                null,
                textProps);
        List<Text> stepDescriptionLines =
                Arrays.asList(stepDescriptionText, stepDescriptionTextCont, stepDescriptionTextCont2);

        showStepDescription("Starte Algorithmus.", stepDescriptionLines);
        src.highlight(0);

        vars.set("range", String.valueOf(range));
        rangeValue.setText(String.valueOf(range), null, null);

        vars.set("n", String.valueOf(n));
        nValue.setText(String.valueOf(n), null, null);

        language.nextStep("Starte Algorithmus.");


        if (showQuestion()) {
            String correctAnswer = "Falsch. Die richtige Antwort waere [0, range) gewesen.";
            MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("range_question");
            question.setPrompt("In welchem Bereich werden Zahlen generiert?");
            question.addAnswer("(0, range)", 0, correctAnswer);
            question.addAnswer("[0, range)", 1, "Richtig");
            question.addAnswer("(0, range + 1)", 0, correctAnswer);
            question.addAnswer("[0, range + 1)", 0, correctAnswer);
            language.addMCQuestion(question);
        }
        //start algorithm
        int[] numbers = new int[n];
        IntArray drawnNumbers = language.newIntArray(new Coordinates(300, 140), numbers, "visualNumbers", null, this.arrayProperties);
        ArrayMarker iMarker = language.newArrayMarker(drawnNumbers, 2, "iMarker", null, this.iMarkerProperties);
        ArrayMarker kMarker = language.newArrayMarker(drawnNumbers, 0, "kMarker", null, this.kMarkerProperties);
        ArrayMarker jMarker = language.newArrayMarker(drawnNumbers, 0, "jMarker", null, this.jMarkerProperties);
        showStepDescription("Initialisiere Array f mit " + n + " Elementen.", stepDescriptionLines);
        this.highlightLine(1);

        language.nextStep("");

        numbers[0] = fZero;
        drawnNumbers.put(0, fZero, null, null);
        showStepDescription("Setze f[0] auf den Wert von f0(=" + fZero + ").", stepDescriptionLines);
        this.highlightLine(2);

        language.nextStep("");

        numbers[1] = fOne;
        drawnNumbers.put(1, fOne, null, null);
        showStepDescription("Setze f[1] auf den Wert von f1(=" + fOne + ").", stepDescriptionLines);
        this.highlightLine(3);

        language.nextStep("");

        for (int i = 2; i < n; i++) {
            drawnNumbers.unhighlightCell(i - 1, null, null);
            showStepDescription("Iteration " + (i - 1) + ".", stepDescriptionLines);
            iMarker.move(i, null, null);
            if (i == 2) {
                iMarker.show();
            }
            iValue.setText(String.valueOf(i), null, null);
            vars.set("i", String.valueOf(i));
            highlightLine(4);
            language.nextStep("Iteration " + (i - 1));

            int k;
            do {
                k = rand.nextInt(i);
            } while (k == 0);
            showStepDescription("Waehle einen Index für das Array, welcher kleiner ist als i(=" + i + ").", stepDescriptionLines);
            kMarker.move(k, null, null);
            if (i == 2) {
                kMarker.show();
            }
            kValue.setText(String.valueOf(k), null, null);
            vars.set("k", String.valueOf(k));
            this.highlightLine(5);
            language.nextStep("");

            int j = k == 1 ? 0 : rand.nextInt(k);
            showStepDescription("Waehle einen Index für das Array, welcher kleiner ist als der Index k(=" + k + ").", stepDescriptionLines);
            jMarker.move(j, null, null);
            if (i == 2) {
                jMarker.show();
            }
            jValue.setText(String.valueOf(j), null, null);
            vars.set("j", String.valueOf(j));
            this.highlightLine(6);

            //need to calc it before the step to ask question
            numbers[i] = (numbers[k] + numbers[j]) % range;
            if (showQuestion() && i == n / 2) {
                FillInBlanksQuestionModel question = new FillInBlanksQuestionModel("f_i_question");
                question.setPrompt("Auf welchen Wert wird f[" + i + "] gesetzt?");
                question.addAnswer(String.valueOf(numbers[i]), 1, "Richtig");
                language.addFIBQuestion(question);
            }
            language.nextStep("");
            drawnNumbers.put(i, numbers[i], null, null);
            drawnNumbers.highlightCell(i, null, Timing.MEDIUM);
            showStepDescription("Berechne den Wert von f[i] durch Kombination von f[k](=" + numbers[k] + ") und f[j](=" + numbers[j] + ").", stepDescriptionLines);
            this.highlightLine(7);
            language.nextStep("");
        }
        showStepDescription("Gebe die letzte generierte Zahl(=" + numbers[n - 1] + ") zurueck.", stepDescriptionLines);
        this.highlightLine(8);
    }

    public boolean showQuestion() {
        Random rnd = new Random();
        int generatedNumber = rnd.nextInt(99) + 1;
        return probOfQuestions != 0 && generatedNumber < this.probOfQuestions;
    }

    private void showStepDescription(String text, java.util.List<Text> lines) {
        String[] words = text.split(" ");
        int currentLineIndex = 0;
        String currentLineText = "";
        int maxWidth = Math.min(60, text.length());
        for (int i = 0; i < words.length; i++) {
            if ((currentLineText.length() + words[i].length()) <= maxWidth) {
                currentLineText += words[i] + " ";
            }
            if (i != words.length - 1) {
                if ((currentLineText.length() + words[i + 1].length()) > maxWidth) {
                    lines.get(currentLineIndex).setText(currentLineText, null, null);
                    currentLineText = "";
                    currentLineIndex++;
                }
            } else {
                lines.get(currentLineIndex).setText(currentLineText, null, null);
                currentLineText = "";
                currentLineIndex++;
            }
        }
        for (int i = currentLineIndex; i < lines.size(); i++) {
            lines.get(i).setText("", null, null);
        }
    }

    public void showVariableState() {
        TextProperties textProps = new TextProperties();
        language.newText(
                new Offset(0, 250, "sourceCode", AnimalScript.DIRECTION_NW),
                "Variablenstatus:",
                "variableStateLines1",
                null,
                textProps);
        RectProperties recProps = new RectProperties();
        recProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        recProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        recProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        language.newRect(
                new Offset(-5, -5, "variableStateLines1", AnimalScript.DIRECTION_NW),
                new Offset(5, 5, "variableStateLines1", AnimalScript.DIRECTION_SE),
                "varRec",
                null,
                recProps);
        language.newText(
                new Offset(0, 30, "variableStateLines1", AnimalScript.DIRECTION_NW),
                "k:",
                "variableStateLines2",
                null,
                textProps);
        language.newText(
                new Offset(0, 20, "variableStateLines2", AnimalScript.DIRECTION_NW),
                "j:",
                "variableStateLines3",
                null,
                textProps);
        language.newText(
                new Offset(0, 20, "variableStateLines3", AnimalScript.DIRECTION_NW),
                "i:",
                "variableStateLines4",
                null,
                textProps);
        language.newText(
                new Offset(0, 20, "variableStateLines4", AnimalScript.DIRECTION_NW),
                "range:",
                "variableStateLines5",
                null,
                textProps);
        language.newText(
                new Offset(0, 20, "variableStateLines5", AnimalScript.DIRECTION_NW),
                "n:",
                "variableStateLines6",
                null,
                textProps);
    }

    private void showConclusion() {
        this.showHeadline();
        SourceCode conclusion = language.newSourceCode(
                new Offset(0, 60, "headline", AnimalScript.DIRECTION_NW),
                "conclusion",
                null,
                sourceCodeHighlightColor);
        conclusion
                .addCodeLine(
                        "Der Lagged Fibonacci Generator ist ein verbesserter pseudo Zufallszahlengenerator. Er ",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "liefert bessere Ergebnisse als lineare Kongruenzgeneratoren.",
                        null, 0, null);
        conclusion.addCodeLine("", null, 0, null);
        conclusion
                .addCodeLine("Das Ergebnis ist stark abhaengig von den initialen Werten und dem Verfahren mit dem", null, 0, null);
        conclusion
                .addCodeLine("die Indizes j und k gewählt werden. In unserer Version haben wir uns", null, 0, null);
        conclusion
                .addCodeLine("dafuer entschieden die beiden Indizes aus denen das Ergebnis gebildet wird",
                        null, 0, null);
        conclusion
                .addCodeLine("zufaellig zu waehlen, wobei diese eigentlich durch andere Methoden bestimmt werden sollten.",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "Da man durch zufaelliges Waehlen der Indizes schlechtere Ergebnisse erzielt.",
                        null, 0, null);
        conclusion.addCodeLine("", null, 0, null);
        conclusion.
                addCodeLine("Zum Beispiel wenn die Range ein Potenz von 2 ist, soll man die Indizes so waehlen das sie"
                        , null, 0, null);
        conclusion.
                addCodeLine("eine Potenz eines primitiven Polynoms sind.", null, 0, null);
        conclusion.addCodeLine("", null, 0, null);
        conclusion
                .addCodeLine(
                        "Die Komplexitaet des Algorithmus variiert und ist abhaengig von dem Verfahren durch das die",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "vorherigen Indizes gewaehlt werden. Wenn wir einfach die Indizes i-1 und i-2 waehlen wuerden",
                        null, 0, null);
        conclusion
                .addCodeLine(
                        "haette der Algorithmus eine Komplexitaet von O(n), wobei n die Anzahl der vorher angegeben Iterationen ist.",
                        null, 0, null);
    }

    private void showErrorWindow(String message) {
        JOptionPane.showMessageDialog(
                JOptionPane.getRootFrame(),
                message,
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        boolean error = false;
        int fZero = (Integer) primitives.get("f0");
        int fOne = (Integer) primitives.get("f1");
        int m = (Integer) primitives.get("range");
        int n = (Integer) primitives.get("n");
        int probOfQuestion = (Integer) primitives.get("percentageOfProbQuestion");
        StringBuilder errorMessage = new StringBuilder();
        if (fZero < 0) {
            errorMessage.append("f0 darf nicht kleiner als 0 sein\n");
            error = true;
        }
        if (fOne < 0) {
            errorMessage.append("f0 darf nicht kleiner als 0 sein\n");
            error = true;
        }
        if (n <= 1) {
            errorMessage.append("n muss größer als 1 sein\n");
            error = true;
        }
        if (m < 2) {
            errorMessage.append("range darf nicht kleiner als 2 sein\n");
            error = true;
        }
        if (fOne > m || fZero > m) {
            errorMessage.append("f0 und f0 dürfen nicht größer als range sein\n");
            error = true;
        }
        if (probOfQuestion < 0 || probOfQuestion > 100) {
            errorMessage.append("Wahrscheinlichkeit der Fragen muss mindestends 0 und hoechstens 100 sein");
            error = true;
        }
        if (error) {
            showErrorWindow(errorMessage.toString());
        }
        return !error;
    }

    @Override
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        this.fZero = (Integer) primitives.get("f0");
        this.fOne = (Integer) primitives.get("f1");
        this.range = (Integer) primitives.get("range");
        this.n = (Integer) primitives.get("n");
        this.probOfQuestions = (Integer) primitives.get("percentageOfProbQuestion");

        this.arrayProperties = (ArrayProperties) props.getPropertiesByName("arrayColors");
        this.sourceCodeHighlightColor = (SourceCodeProperties) props.getPropertiesByName("sourceCodeHighlightColor");
        this.iMarkerProperties = (ArrayMarkerProperties) props.getPropertiesByName("iMarkerColor");
        this.jMarkerProperties = (ArrayMarkerProperties) props.getPropertiesByName("jMarkerColor");
        this.kMarkerProperties = (ArrayMarkerProperties) props.getPropertiesByName("kMarkerColor");
        this.iMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
        this.jMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
        this.kMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "k");

        language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        this.showDescription();
        language.nextStep("Zeige Beschreibung an");
        language.hideAllPrimitives();
        this.showAlgorithm();
        language.nextStep("Algorithmus terminiert");
        language.hideAllPrimitives();
        this.showConclusion();
        language.nextStep("Zeige Konklusion an");

        language.finalizeGeneration();
        return language.toString();
    }

    @Override
    public String getAlgorithmName() {
        return "Lagged Fibonacci Generator";
    }

    @Override
    public String getAnimationAuthor() {
        return "Andre Pacak, Marc Semmler";
    }

    @Override
    public String getCodeExample() {
        return "generate(f0, f1, range, n)\n" +
                "   f = array of n\n" +
                "   f[0] = f0\n" +
                "   f[1] = f1\n" +
                "   for(i = 2; i < n; i++)\n" +
                "       k = rand(0,i)\n" +
                "       j = rand[0,k)\n" +
                "       f[i] = (f[j] + f[k]) mod range\n" +
                "   return f[n-1]";
    }

    @Override
    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    @Override
    public String getDescription() {
        return "Der Lagged Fibonacci Generator (LFG) ist ein Pseudozufallszahlengenerator. Er basiert auf "
                + "der Fibonacci Sequenz, bei der die Summe der letzten beiden Werte den neuen Wert bilden. Beim LFG "
                + "werden zwei beliebige Werte, welche nicht den gleichen Index haben, aus der vorherigen Sequenz genommen und kombiniert. "
                + "Die Kombination kann durch Addition, Substraktion, Multiplikation oder dem exklusiven Oder (XOR) "
                + "erfolgen. Bei unserer Version haben wir uns fuer die Addition entschieden. Man nennt ihn dann additiven "
                + "Lagged Fibonacci Generator. Danach wird der enstandene Wert mit einem festen Wert m modulo gerechnet, um eine Zahl "
                + "im Bereich von 0 bis m zu erhalten.";
    }

    @Override
    public String getFileExtension() {
        return "asu";
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    @Override
    public String getName() {
        return "Lagged Fibonacci Generator [DE]";
    }

    @Override
    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    @Override
    public void init() {
        language = new AnimalScript(
                "Lagged Fibonacci Generator [DE]",
                "Andre Pacak, Marc Semmler",
                800,
                600);
        language.setStepMode(true);
    }
}