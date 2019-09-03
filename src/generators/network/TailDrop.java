/*
 * TailDrop.java
 * Felix Gail, Torben Carstens, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.congestioncontrol.helper.AbstractCongestionControl;
import generators.network.congestioncontrol.helper.CongestionControlPacket;

import java.util.AbstractMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TailDrop extends AbstractCongestionControl {

    //Introduction slide
    private final List<AbstractMap.SimpleEntry<String, Integer>> INTRO_SLIDE = IntStream.range(1, 4).mapToObj(
            i ->
                    new AbstractMap.SimpleEntry<>(translator.translateMessage("intro" + i), 0))
            .collect(Collectors.toList());

    /*Usage in DummyGenerator:
    generators.add(new TailDrop(Locale.GERMANY));
    generators.add(new TailDrop(Locale.US));
     */
    public TailDrop(Locale locale) {
        super("TailDrop", locale);
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
                nextStep("FOR_PACKET_ITERATION");
                getFifo().update();
                updateClients();
                // nextStep("CHECK_FIFO_CAPACITY");

                if (getFifo().isFull()) {
                    packet.drop();
                    updateClients();
                    getFifo().update();
                    nextStep("DROP_PACKET");
                    packet.processed();
                } else {
                    // nextStep("CHECK_FIFO_CAPACITY_ELSE");
                    getFifo().enqueue(packet);
                    packet.enqueue();
                    this.variables.set("fifoSize", "" + getFifo().size());
                    updateClients();
                    getFifo().update();
                    nextStep("ENQUEUE_PACKET");
                    // nextStep("INCREMENT_INDEX");
                }
                updateClients();
                getFifo().update();
                // nextStep("UPDATE_FIFO_DROP_ENQ");
            }

            // TODO: Calculate by throughput
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
        return "Tail Drop";
    }

    @Override
    public String getAlgorithmName() {
        return "Tail Drop";
    }

    @Override
    protected List<AbstractMap.SimpleEntry<String, Integer>> getIntroSlide() {
        return INTRO_SLIDE;
    }

    @Override
    protected boolean validateSpecificInput(AnimationPropertiesContainer animationPropertiesContainer,
                                            Hashtable<String, Object> primitives)
            throws IllegalArgumentException {
        return true;
    }

    @Override
    public String getDescription() {
        // {TODO}
        return translator.translateMessage("description");
    }
}
