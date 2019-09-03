/*
 * AbstractCongestionPacket.java
 * Felix Gail, Torben Carstens, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network.congestioncontrol.helper;

import algoanim.primitives.Rect;
import algoanim.primitives.generators.RectGenerator;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;

import java.awt.*;
import java.util.Date;

enum Position {
    INIITAL,
    WAITING,
    DROPPED,
    ENQUEUED,
    PROCESSED,
    PROCESS,
    AWAIT
}


public class CongestionControlPacket {
    static final int HEIGHT = 35;
    static final int WIDTH = 20;
    private static final Node DROPPED_POSITION = new Coordinates(AbstractCongestionControl.FIFO_UPPER_LEFT.getX() + 205, AbstractCongestionControl.FIFO_UPPER_LEFT.getY() + CongestionControlFIFO.HEIGHT / 3);
    private static int droppedPackets = 0;
    private static int enqueuedPackets = 0;
    static int ID = 0;
    private final CongestionControlClient client;
    private final RectGenerator generator;
    private final Coordinates upperLeft;
    private final Coordinates lowerRight;
    private final RectProperties properties;
    private Position position;
    private Date created; // UNIX timestamp, measure throughput in ms?
    private final int id;
    private Rect rect;

    private final static int WAITING_OFFSET = 5;
    private final static Coordinates DEFAULT_WAITING_POSITION =
            new Coordinates(
                    AbstractCongestionControl.FIFO_UPPER_LEFT.getX() + (CongestionControlFIFO.WIDTH - WIDTH) / 2,
                    AbstractCongestionControl.FIFO_UPPER_LEFT.getY() - 25 - HEIGHT);
    private static Coordinates waitingPosition = DEFAULT_WAITING_POSITION;

    CongestionControlPacket(RectGenerator generator, Coordinates upperLeft, Coordinates lowerRight, RectProperties properties, CongestionControlClient client) {
        this.id = ID;
        ID++;
        position = Position.INIITAL;
        this.client = client;
        this.generator = generator;
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
        this.properties = properties;
    }

    public static int getDroppedPackets() {
        return droppedPackets;
    }

    public static int getEnqueuedPackets() {
        return enqueuedPackets;
    }

    void draw() {
        rect = new Rect(generator, upperLeft, lowerRight, "packet_" + ID, new MsTiming(0), properties);
    }

    public void drop() {
        droppedPackets++;
        position = Position.DROPPED;
    }

    public void enqueue() {
        enqueuedPackets++;
        position = Position.ENQUEUED;
    }

    void process() {
        position = Position.PROCESS;
    }

    public void processed() {
        position = Position.PROCESSED;
    }

    // @throws IllegalStateException in the case, that nextStep() is called even if the packet has been already processed (last possible state)
    void update() throws IllegalStateException {
        switch (position) {
            case INIITAL:
                position = Position.WAITING;
                break;
            case WAITING:
                rect.moveTo("N", "translate", waitingPosition, null, null);
                waitingPosition = new Coordinates(waitingPosition.getX() - WAITING_OFFSET,
                        waitingPosition.getY() - WAITING_OFFSET);
                position = Position.AWAIT;
                break;
            case AWAIT:
                break;
            case DROPPED:
                rect.moveTo("N", "translate", DROPPED_POSITION, null, null);
                position = Position.AWAIT;
                break;
            case ENQUEUED:
                rect.hide();
                break;
            case PROCESS:
                rect.show();
                rect.moveTo("S", "translate", new Coordinates(
                        AbstractCongestionControl.FIFO_UPPER_LEFT.getX() + CongestionControlFIFO.WIDTH / 2 - WIDTH / 2 + 8,
                        AbstractCongestionControl.FIFO_UPPER_LEFT.getY() + (int) (CongestionControlFIFO.HEIGHT * 0.7) + HEIGHT + 24), null, null);
                position = Position.PROCESSED;
                break;
            case PROCESSED:
                rect.hide();
                break;
        }
    }

    Position getPosition() {
        return position;
    }

    Color getColor() {
        return client.getColor();
    }

    int getId() {
        return id;
    }

    void waiting() {
        position = Position.WAITING;
    }

    public static void resetWaitingPosition(int numberOfPackets) {
        waitingPosition = new Coordinates(DEFAULT_WAITING_POSITION.getX() + (numberOfPackets * WAITING_OFFSET) / 2,
                DEFAULT_WAITING_POSITION.getY());
    }

    public Rect getRect() {
        return rect;
    }

    public static void resetStatics() {
        ID = 0;
        droppedPackets = 0;
        enqueuedPackets = 0;
    }
}
