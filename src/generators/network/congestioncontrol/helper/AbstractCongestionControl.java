/*
 * AbstractCongestionControl.java
 * Felix Gail, Torben Carstens, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network.congestioncontrol.helper;

import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.RectGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.variables.VariableRoles;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import helpers.AnimalReader;
import translator.Translator;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCongestionControl implements ValidatingGenerator {

    private final Locale locale;
    protected Translator translator;
    protected Language lang;
    private SourceCodeProperties highlightColor;

    static final int FRAME_WIDTH = 590;
    static final Coordinates FRAME_UPPER_LEFT = new Coordinates(275, 70);
    protected static final Coordinates FIFO_UPPER_LEFT = new Coordinates(FRAME_UPPER_LEFT.getX() + FRAME_WIDTH / 2 - CongestionControlFIFO.WIDTH / 2, FRAME_UPPER_LEFT.getY() + 200);
    private final String filePath;

    // UI Elements
    private Rect rectangleHeader;
    private Text explanation;
    private Text chapter;
    private SourceCode introSlide;
    private SourceCode pseudoCode;

    // Algorithm variables
    private int fifoCapacity;
    private CongestionControlFIFO fifo;
    // Packets per second on the output interface
    private int numberOfClients;
    private CongestionControlClient[] clients;
    private int packetsToProcess;
    private double packetGenerateProbability;
    private int approximatePacketsToGenerate;

    // PseudoCode
    private LinkedList<String> pseudoCodeIdentifiers;
    private static List<String> PSEUDO_CODE;

    //Step explanations
    private static List<String> EXPLANATIONS;
    protected Variables variables;

    public AbstractCongestionControl(String fileName, Locale locale) {
        this.filePath = "resources/"+fileName;
        this.locale = locale;
        this.translator = new Translator(this.filePath, locale);
    }

    private LinkedList<SimpleEntry<String, String>> readPseudoCodeExplanations() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(AnimalReader.getInputStream(filePath + ".pseudo_code")))) {
            pseudoCodeIdentifiers = new LinkedList<>();
            return br.lines().map(String::trim).map(s -> s.replace("\\t", "\t")).map(this::parseLine).collect(Collectors.toCollection(LinkedList::new));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new LinkedList<>();
    }

    private SimpleEntry<String, String> parseLine(String line) {
        String[] splits = line.split("@");
        SimpleEntry<String, String> entry;
        if (splits.length >= 2) {
            pseudoCodeIdentifiers.add(splits[0]);
            entry = new SimpleEntry<>(splits[1], "");
            if (splits.length >= 3) {
                if (splits[2].equals("X")) {
                    entry.setValue(translator.translateMessage(splits[0]));
                } else {
                    entry.setValue(translator.translateMessage(splits[2]));
                }
            }
        } else {
            entry = new SimpleEntry<>("", "");
        }


        return entry;
    }

    public void init() {
        PSEUDO_CODE = new LinkedList<>();
        EXPLANATIONS = new LinkedList<>();
        for (SimpleEntry<String, String> entry : readPseudoCodeExplanations()) {
            PSEUDO_CODE.add(entry.getKey());
            EXPLANATIONS.add(entry.getValue());
        }

        lang = new AnimalScript(getName(), getAnimationAuthor(), 1024, 860);
        lang.setStepMode(true);
        this.variables = lang.newVariables();
    }

    protected <T> T castInputParameter(Hashtable<String, Object> primitives, String identifier, T defaultValue) {
        try {
            @SuppressWarnings("unchecked") T value = (T) primitives.get(identifier);
            if (value == null) {
                return defaultValue;
            }
            return value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        CongestionControlPacket.resetStatics();
        clients = new CongestionControlClient[numberOfClients];

        //Creating text elements and their properties
        defineTextElements();

        //Creating sourcecode elements and their properties
        defineSourceCodeElements();
        chapter.setText(translate("chapterIntro"), null, null);
        //Display introduction slide
        for (SimpleEntry<String, Integer> line : getIntroSlide()) {
            introSlide.addCodeLine(line.getKey(), null, line.getValue(), null);
        }

        lang.nextStep();
        chapter.setText(translate("chapterAlgo"), null, null);
        //Hide introduction slide
        introSlide.hide();

        // Display of pseudo code
        for (String line : PSEUDO_CODE) {
            int lengthWithoutTabs = line.replaceAll("\t", "").length();
            pseudoCode.addCodeLine(line, null, line.length() - lengthWithoutTabs, null);
        }

        initFIFO();
        initFrameTexts();

        initFormulas();

        generateClients();
        // Tail Drop
        congestionControl();
        lang.nextStep();
        chapter.setText(translate("chapterSummary"), null, null);
        lang.hideAllPrimitivesExcept(chapter);
        summarySlide();
        lang.nextStep();

        return lang.toString();
    }

    protected void summarySlide() {
        TextProperties textProperties = new TextProperties();
        textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 10));
        Coordinates upperLeft = new Coordinates(0, 0);
        Text header = lang.newText(new Offset(30, 10, upperLeft, "NE"), getAlgorithmName(), "header", null, textProperties);
        rectangleHeader = lang.newRect(new Offset(-10, 0, header, "NW"), new Offset(10, 0, header, "SE"), "rectangleHeader", null);

        TextProperties summaryProperties = new TextProperties();
        summaryProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 14));
        String[] split = translate("summaryText1").split("########");
        Coordinates next = new Coordinates(30, 50);
        for (String line : split) {
            lang.newText(next, line, "summaryText", null, summaryProperties);
            next = new Coordinates(next.getX(), next.getY() + 25);
        }
        next = new Coordinates(next.getX(), next.getY() + 25);
        summaryProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        lang.newText(next, translate("summaryText2", CongestionControlPacket.getDroppedPackets()), "summaryText2", null, summaryProperties);
        next = new Coordinates(next.getX(), next.getY() + 50);
        summaryProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
        lang.newText(next, translate("summaryText3", CongestionControlPacket.getEnqueuedPackets()), "summaryText3", null, summaryProperties);
    }

    protected void initFormulas() {
        // Empty by default
    }

    private void initFrameTexts() {
        Coordinates upperLeft = new Coordinates(FIFO_UPPER_LEFT.getX() + 200, FIFO_UPPER_LEFT.getY() + CongestionControlFIFO.HEIGHT / 3 - 20);
        TextProperties properties = new TextProperties();
        properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 14));
        Text dropped = lang.newText(upperLeft, "Drop", "drop_text", null, properties);
        dropped.changeColor("color", new Color(255, 0, 0), null, null);

        upperLeft = new Coordinates(FIFO_UPPER_LEFT.getX() + CongestionControlFIFO.WIDTH / 2 - 29, FIFO_UPPER_LEFT.getY() + CongestionControlFIFO.HEIGHT - 80);
        properties = new TextProperties();
        properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 14));
        Text processed = lang.newText(upperLeft, "Processed", "processed_text", null, properties);
        processed.changeColor("color", new Color(0, 255, 0), null, null);
    }

    private void initFIFO() {
        fifo = new CongestionControlFIFO(fifoCapacity, lang, FIFO_UPPER_LEFT);
    }

    private void generateClients() {
        RectGenerator generator = new AnimalRectGenerator(lang);
        Coordinates upperLeft, lowerRight;
        int size = CongestionControlClient.SIZE;
        int margin = (FRAME_WIDTH - (numberOfClients * size)) / numberOfClients;

        for (int id = 0; id < numberOfClients; id++) {

            upperLeft = new Coordinates(FRAME_UPPER_LEFT.getX() + (margin + size) * id + margin / 2, FRAME_UPPER_LEFT.getY());
            lowerRight = new Coordinates(upperLeft.getX() + size, upperLeft.getY() + size);
            RectProperties properties = new RectProperties("client_" + id);
            properties.set(
                    AnimationPropertiesKeys.FILL_PROPERTY,
                    ClientColor.values()[id].getColor()
            );
            properties.set(
                    AnimationPropertiesKeys.FILLED_PROPERTY,
                    true
            );
            properties.set(
                    AnimationPropertiesKeys.COLOR_PROPERTY,
                    Color.BLACK
            );
            clients[id] = new CongestionControlClient(id, generator, upperLeft, lowerRight, properties, FIFO_UPPER_LEFT);
        }
    }

    protected List<CongestionControlPacket> receivePackets() {
        LinkedList<CongestionControlPacket> list = new LinkedList<>();
        Random random = new Random();

        for (CongestionControlClient client : clients) {
            if (packetGenerateProbability >= random.nextFloat()) {
                list.add(client.generatePacket());
            }
        }
        Collections.reverse(list);
        for (CongestionControlPacket packet : list) {
            packet.draw();
        }
        Collections.reverse(list);
        return list;
    }

    protected void updateClients() {
        for (CongestionControlClient client : clients) {
            client.update();
        }
    }

    protected abstract void congestionControl();

    public abstract String getName();

    public abstract String getAlgorithmName();

    protected abstract List<AbstractMap.SimpleEntry<String, Integer>> getIntroSlide();

    public abstract String getDescription();

    public String getAnimationAuthor() {
        return "Felix Gail, Torben Carstens";
    }


    public String getCodeExample() {
        return String.join("\n", PSEUDO_CODE);
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    protected abstract boolean validateSpecificInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives)
            throws IllegalArgumentException;

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives)
            throws IllegalArgumentException {
        fifoCapacity = castInputParameter(primitives, "fifoCapacity", 10);
        numberOfClients = castInputParameter(primitives, "numberOfClients", 5);
        approximatePacketsToGenerate = castInputParameter(primitives, "approximatePacketsToGenerate", 15);
        packetGenerateProbability = castInputParameter(primitives, "packetGenerateProbability", 0.5);
        packetsToProcess = castInputParameter(primitives, "packetsToProcess", 1);
        highlightColor = (SourceCodeProperties) animationPropertiesContainer.getPropertiesByName("highlightColor");

        this.variables.declare("int", "fifoCapacity", "" + fifoCapacity, "fixed value");
        this.variables.declare("int", "numberOfClients", "" + fifoCapacity, "fixed value");
        this.variables.declare("int", "approximatePacketsToGenerate", "" + approximatePacketsToGenerate, "fixed value");
        this.variables.declare("double", "packetGenerateProbability", "" + packetGenerateProbability, "fixed value");
        this.variables.declare("int", "packetsToProcess", "" + packetsToProcess, "fixed value");
        this.variables.declare("int", "fifoSize", "0", VariableRoles.FOLLOWER.toString());

        return fifoCapacity > 0
                && packetGenerateProbability >= 0 && packetGenerateProbability <= 1
                && packetsToProcess > 0
                && numberOfClients > 0 && numberOfClients <= 10
                && approximatePacketsToGenerate > 0
                && validateSpecificInput(animationPropertiesContainer, primitives);
    }

    private void defineTextElements() {
        //Defining text parameters
        Coordinates upperLeft = new Coordinates(0, 0);
        TextProperties textProperties = new TextProperties();
        textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 10));

        explanation = lang.newText(new Offset(20, 450, upperLeft, "NE"), "", "explanation", null, textProperties);
        explanation.changeColor("color", new Color(255, 0, 0), null, null);

        TextProperties chapterProperties = new TextProperties();
        chapterProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
        chapter = lang.newText(new Offset(230, 10, upperLeft, "NE"), "", "ChapterHeader", null, chapterProperties);

        //Creating header rectangle
        Text header = lang.newText(new Offset(30, 10, upperLeft, "NE"), getAlgorithmName(), "header", null, textProperties);
        rectangleHeader = lang.newRect(new Offset(-10, 0, header, "NW"), new Offset(10, 0, header, "SE"), "rectangleHeader", null);
    }

    private void defineSourceCodeElements() {
        //Defining code parameters
        SourceCodeProperties codeProps = new SourceCodeProperties();
        codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 16));
        codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightColor.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
        codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        pseudoCode = lang.newSourceCode(new Offset(0, 100, rectangleHeader, "SW"), "sourceCode", null, codeProps);
        introSlide = lang.newSourceCode(new Offset(0, 50, rectangleHeader, "SW"), "introSlide", null, codeProps);
    }

    protected void dequeuePacket() {
        CongestionControlPacket dequeued = getFifo().dequeue();
        dequeued.process();
        nextStep("PROCESS_PACKET");
        // Process packet and remove it from the queue
        updateClients();
        getFifo().update();
        this.variables.set("fifoSize", "" + getFifo().size());
        // nextStep("UPDATE_FIFO_END");
        dequeued.processed();
    }

    public void dequeueRemainingPackets() {
        while (!getFifo().isEmpty()) {
            dequeuePacket();
        }
    }

    protected void nextStep(String identifier) {
        int lineNo = pseudoCodeIdentifiers.indexOf(identifier);
        if (lineNo < 0) {
            throw new NoSuchIdentifierException();
        }
        pseudoCode.highlight(lineNo);
        explanation.setText("Explanation: " + EXPLANATIONS.get(lineNo), null, null);
        lang.nextStep();
        pseudoCode.unhighlight(lineNo);
    }

    public int getFifoCapacity() {
        return fifoCapacity;
    }

    public CongestionControlFIFO getFifo() {
        return fifo;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public CongestionControlClient[] getClients() {
        return clients;
    }

    public int getNumberPacketsToProcess() {
        return packetsToProcess;
    }

    public double getPacketGenerateProbability() {
        return packetGenerateProbability;
    }

    public int getapproximatePacketsToGenerate() {
        return approximatePacketsToGenerate;
    }

    protected String translate(String key, Object... var) {
        return translator.translateMessage(key, var);
    }

    private class NoSuchIdentifierException extends IndexOutOfBoundsException {

      /**
       * 
       */
      private static final long serialVersionUID = 1L;
    }

}
