/*
 * RED.java
 * Felix Gail, Torben Carstens, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import animal.variables.VariableRoles;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.congestioncontrol.helper.AbstractCongestionControl;
import generators.network.congestioncontrol.helper.CongestionControlPacket;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RED extends AbstractCongestionControl {

    private final List<AbstractMap.SimpleEntry<String, Integer>> INTRO_SLIDE = IntStream.range(1, 11).mapToObj(
            i ->
                    new AbstractMap.SimpleEntry<>(translator.translateMessage("intro" + i), 0))
            .collect(Collectors.toList());
    private double reactiveness;
    private double max;
    private double min;
    private double max_p;
    private double average;
    private Text averageRaw;
    private Text averageFilled;
    private Text p0Raw;
    private Text p0Filled;
    private Text pRaw;
    private Text pFilled;

    /*Usage in DummyGenerator:
    generators.add(new RED(Locale.GERMANY));
    generators.add(new RED(Locale.US));
     */
    public RED(Locale locale) {
        super("RED", locale);
    }

    @Override
    public void initFormulas() {
        super.initFormulas();
        updateFormulas(0);
    }

    void updateFormulas(int queuedPackets) {
        TextProperties properties = new TextProperties();
        properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 14));

        double oldAverage = average;
        String raw = "average = (1 - reactivness) * average + reactivness * fifoSize";
        double average = calculateAverage();
        String filled = String.format("%7.2f = (1 - %11.4f) * %7.2f + %11.4f * %8d", average, reactiveness, oldAverage, reactiveness, getFifo().size());
        Coordinates position = new Coordinates(AbstractCongestionControl.FIFO_UPPER_LEFT.getX() + 400, AbstractCongestionControl.FIFO_UPPER_LEFT.getY() - 65);
        averageRaw = lang.newText(position, raw, "raw_formula_text", null, properties);
        averageRaw.changeColor("color", new Color(0, 0, 0), null, null);
        position = new Coordinates(position.getX(), position.getY() + 50);
        averageFilled = lang.newText(position, filled, "filled_formula_text", null, properties);
        averageFilled.changeColor("color", new Color(255, 0, 0), null, null);

        raw = "p0 = max_p * (average - min) / (max - min)";
        double temp_result = Math.max(0, max_p * (average - min) / (max - min));
        filled = String.format("%2.2f = %5s * (%7.2f - %3.2f) / (%3.2f - %3.2f)", temp_result, max_p, average, min, max, min);
        position = new Coordinates(position.getX(), position.getY() + 50);
        p0Raw = lang.newText(position, raw, "raw_formula_text", null, properties);
        p0Raw.changeColor("color", new Color(0, 0, 0), null, null);
        position = new Coordinates(position.getX(), position.getY() + 50);
        p0Filled = lang.newText(position, filled, "filled_formula_text", null, properties);
        p0Filled.changeColor("color", new Color(255, 0, 0), null, null);

        raw = "dropProbability = p0 / (1 - queuedPackets * p0)";
        double dropProbability = getDropProbability(queuedPackets);
        filled = String.format("%15.2f = %2.2f / (1 - %13d * %2.2f)", dropProbability, temp_result, queuedPackets, temp_result);
        position = new Coordinates(position.getX(), position.getY() + 50);
        pRaw = lang.newText(position, raw, "raw_formula_text", null, properties);
        pRaw.changeColor("color", new Color(0, 0, 0), null, null);
        position = new Coordinates(position.getX(), position.getY() + 50);
        pFilled = lang.newText(position, filled, "filled_formula_text", null, properties);
        pFilled.changeColor("color", new Color(255, 0, 0), null, null);
    }

    private void clearFormulas() {
        averageRaw.setText("", null, null);
        averageFilled.setText("", null, null);
        p0Raw.setText("", null, null);
        p0Filled.setText("", null, null);
        pRaw.setText("", null, null);
        pFilled.setText("", null, null);
    }

    private double calculateAverage() {
        double avg = ((1 - reactiveness) * average + reactiveness * getFifo().size());

        this.variables.set("average", "" + average);

        return avg;
    }

    private double getDropProbability(int queuedPackets) {
        double temp = max_p * (average - min) / (max - min);
        temp = Math.max(0, temp / (1 - queuedPackets * temp));

        if (getFifo().size() <= min) {
            temp = 0;
        } else if (getFifo().size() >= max) {
            temp = 1;
        }

        return Math.max(0, temp);
    }

    @Override
    protected void congestionControl() {
        nextStep("FUNCTION_DEFINITION");
        nextStep("FIFO_DECLARATION");
        nextStep("INDEX_DECLARATION");
        int numberOfGeneratedPackets = 0;
        while (numberOfGeneratedPackets < getapproximatePacketsToGenerate()) {
            List<CongestionControlPacket> packets = receivePackets();
            nextStep("INCOMING_PACKET_DECLARATION");
            CongestionControlPacket.resetWaitingPosition(packets.size());
            updateClients();
            getFifo().update();
            numberOfGeneratedPackets += packets.size();
            for (CongestionControlPacket packet : packets) {
                average = calculateAverage();

                nextStep("FOR_PACKET_ITERATION");
                getFifo().update();
                updateClients();
                // nextStep("GET_RANDOM");
                double random = new Random().nextDouble();
                this.variables.declare("double", "randomProbability", "" + random, VariableRoles.TEMPORARY.toString());
                nextStep("CALCULATE_DROP_PROBABILITY");
                double probability = getDropProbability(getFifo().size());
                this.variables.declare("double", "dropProbability", "" + probability, VariableRoles.TEMPORARY.toString());

                clearFormulas();
                updateFormulas(packets.size());
                // nextStep("CHECK_DROP");
                if (random <= probability) {
                    packet.drop();
                    updateClients();
                    getFifo().update();
                    nextStep("DROP_PACKET");
                    packet.processed();
                } else {
                    // nextStep("CHECK_DROP_ELSE");
                    getFifo().enqueue(packet);
                    packet.enqueue();
                    this.variables.set("fifoSize", "" + getFifo().size());
                    updateClients();
                    getFifo().update();
                    clearFormulas();
                    updateFormulas(packets.size());
                    nextStep("ENQUEUE_PACKET");
                }
                this.variables.discard("randomProbability");

                updateClients();
                getFifo().update();
                // nextStep("UPDATE_FIFO_DROP_ENQ");
            }

            for (int i = 0; i < getNumberPacketsToProcess(); i++) {
                if (!getFifo().isEmpty()) {
                    dequeuePacket();
                }
            }
        }

        dequeueRemainingPackets();
        updateClients();
    }

    @Override
    public String getName() {
        return "Random Early Detection (RED)";
    }

    @Override
    public String getAlgorithmName() {
        return "Random Early Detection (RED)";
    }

    @Override
    protected List<AbstractMap.SimpleEntry<String, Integer>> getIntroSlide() {
        return INTRO_SLIDE;
    }

    @Override
    public String getDescription() {
        return translator.translateMessage("description");
    }

    @Override
    protected boolean validateSpecificInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        reactiveness = castInputParameter(primitives, "reactivness", 0.002);
        average = castInputParameter(primitives, "initialAverage", 0);
        max = castInputParameter(primitives, "max", 0.95) * getFifoCapacity();
        min = castInputParameter(primitives, "min", 0.2) * getFifoCapacity();
        max_p = castInputParameter(primitives, "max_p", 0.1);

        this.variables.declare("double", "reactivness", "" + reactiveness, "fixed value");
        this.variables.declare("double", "average", "" + average, VariableRoles.ORGANIZER.toString());
        this.variables.declare("int", "min", "" + min, "fixed value");
        this.variables.declare("int", "max", "" + max, "fixed value");
        this.variables.declare("double", "maxP", "" + max_p, "fixed value");

        return reactiveness > 0 && reactiveness <= 1
                && average >= 0 && average <= getFifoCapacity()
                && max > min && max <= getFifoCapacity()
                && min > 0
                && max_p > 0 && max_p <= 1;
    }
}
