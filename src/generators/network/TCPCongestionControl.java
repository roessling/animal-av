/*
 * TCPCongestionControl.java
 * Joël Tschesche, Kadoglou Konstantinos, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
//package generators.network;

package generators.network;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class TCPCongestionControl implements ValidatingGenerator {

    private Language lang;
    private int data = 10000;
    private int ss_tresh = 200;
    private int modus;
    private String userInput = "aaa";


    // Variable für Iteration (kann weg)
    int counter = 1;
    int rangeCounter;
    private int longestASequence;

    SourceCode sc;
    private SourceCodeProperties scProps;
    private PolylineProperties arrowProbs;
    private PolylineProperties lineProbs;
    private PolylineProperties redLineProbs;
    private RectProperties rectProbs;
    private TextProperties textProbs;

    private PolylineProperties thresholdProps = new PolylineProperties();
    private PolylineProperties graphProps = new PolylineProperties();


    public TCPCongestionControl() { //) {
//        lang = l;
    }

    public void init() {
        lang = new AnimalScript("TCPCongestionControl", "Joël Tschesche, Konstantinos Kadoglou", 1000, 1000);
        lang.setStepMode(true);
    }

    /*
        Simulates the TCPCongestionControl algorithm and creates depending on the received message a string.
     */

    public void startTransmission() {
        switch (modus) {
            case 0:
                userInput = "aaaaaaaaaaa3taaaaaaaaaaaaaaaaaaaaaaa";
                break; // harcoded example aaaaaaaaaaa3taaaaaaaaaaaaaaaaaaaaaaa
            case 1:
                userInput = this.createString();
                break; // random example
            case 2:
                break;
        }
        // ===================== Variables =============================
        Sender sender = new Sender(data, userInput);
        Coordinates origin = new Coordinates(550, 450);
        Coordinates currentPoint = origin;
        int cwnd = 1;
        for (rangeCounter = 0; rangeCounter < this.longestASequence(); rangeCounter++) {
            if (ss_tresh < cwnd) break;
            cwnd = cwnd * 2;
        }
        int subXRange = 500 / userInput.length();
        int subYRange = 400 / rangeCounter;

        double scale = 400/ this.dynamicLimit();

        // ===================== Sender/Receiver Animation Preparation =============================
        Polyline senderLine;
        Polyline receiverLine;
        Polyline requestArrow;
        Polyline responseArrow;

        Text senderHeader;
        Text receiverHeader;
        Text requestText;
        Text responseText;

        // ========= Text- und Rechteckeigenschaften des Titels =========
        rectProbs = new RectProperties();
        textProbs = new TextProperties();
        rectProbs.set("filled", true);
        textProbs.set("font", new Font("Monospaced", 0, 15));
        textProbs.set("color", Color.black);

        lineProbs = new PolylineProperties();
        lineProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        lineProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        lineProbs.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);

        redLineProbs = new PolylineProperties();
        redLineProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        redLineProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

        // ========= Text- und Rechteck in Animal anzeigen =========
        Rect cCR = this.lang.newRect(new Coordinates(40, 9), new Coordinates(330, 40), "congestioncontrol", (DisplayOptions) null, rectProbs);
        cCR.changeColor("fillColor", Color.YELLOW, (Timing) null, (Timing) null);
        Text cCT= this.lang.newText(new Coordinates(40, 10), "TCP - CongestionControl", "invertRGB", (DisplayOptions) null, textProbs);
        cCT.setFont(new Font("Monospaced", 0, 20), (Timing) null, (Timing) null);

        // Intro
        Text des1 = this.lang.newText(new Coordinates(40, 50), "Der Algorithmus  demonstriert wie der Sender sich der maximalen Bandbreite annähert.", "des1", (DisplayOptions) null, textProbs);
        Text des2 = this.lang.newText(new Coordinates(40, 65), "Dabei geht der Algorithmus auch auf potenzielle Überlastung des Systems ein, indem", "des2", (DisplayOptions) null, textProbs);
        Text des3 = this.lang.newText(new Coordinates(40, 80), "TIMEOUTs oder TRIPLE ACK mit speziellen Fällen abgearbeitet werden.", "des3", (DisplayOptions) null, textProbs);

        Text des4 = this.lang.newText(new Coordinates(40, 110), "Erhält der Sender ein ACK für ein versendetes Packet, so kann er ", "des4", (DisplayOptions) null, textProbs);
        Text des5 = this.lang.newText(new Coordinates(40, 125), "seine Verbidungsanzahl erhöhen. Befindet es sich unter dem SlowStart-Treshold.", "des5", (DisplayOptions) null, textProbs);
        Text des6 = this.lang.newText(new Coordinates(40, 140), "dann wird die Anzahl der Verbindungen verdoppelt, andernfalls nur um eins erhöht.", "des6", (DisplayOptions) null, textProbs);

        Text des7 = this.lang.newText(new Coordinates(40, 170), "Detektiert der Algorithmus ein TIMEOUT, so wird angenommen, dass das Netzwerk", "des7", (DisplayOptions) null, textProbs);
        Text des8 = this.lang.newText(new Coordinates(40, 185), "überlastet ist und die Daten kommen gar nicht erst beim Empfänger an.", "des8", (DisplayOptions) null, textProbs);
        Text des9 = this.lang.newText(new Coordinates(40, 200), "Der Sender geht dagegen vor indem er die Verbidungsanzahl für die Datenüber-", "des9", (DisplayOptions) null, textProbs);
        Text des10 = this.lang.newText(new Coordinates(40, 215), "tragung auf 1 reduzuiert, in die SlowStart-Phase übergeht und seinen ss_treshold", "des10", (DisplayOptions) null, textProbs);
        Text des11 = this.lang.newText(new Coordinates(40, 230), "auf halbiert. Somit soll das Netwerk entlastet werden.", "des11", (DisplayOptions) null, textProbs);

        Text des12 = this.lang.newText(new Coordinates(40, 260), "Wird hingegen ein TRIPLE ACK detektiert, dann kann man sich nicht sicher sein,", "des12", (DisplayOptions) null, textProbs);
        Text des13 = this.lang.newText(new Coordinates(40, 285), "ob das Netzwerk überlastet ist oder ein Packetsegment aus anderen Gründen den ", "des13", (DisplayOptions) null, textProbs);
        Text des14 = this.lang.newText(new Coordinates(40, 300), "Weg zum Empfänger nicht gefunden hat. Deswegen wird der ss_treshold halbiert und", "des14", (DisplayOptions) null, textProbs);
        Text des15 = this.lang.newText(new Coordinates(40, 315), "das Congestion Window darauf gesetzt.", "des15", (DisplayOptions) null, textProbs);

        Text des16 = this.lang.newText(new Coordinates(40, 345), "Im folgenden Beispiel kann es zu kleinen Rundungsfehlern kommen, da das Koordinaten-", "des16", (DisplayOptions) null, textProbs);
        Text des17 = this.lang.newText(new Coordinates(40, 360), "system dynamisch angepasst wird. Dennoch wird der Algorithmus korrekt abgearbeitet.", "des17", (DisplayOptions) null, textProbs);


        this.lang.nextStep("Intro");
       // ========= Erklärung verstecken =========
        des1.hide();
        des2.hide();
        des3.hide();
        des4.hide();
        des5.hide();
        des6.hide();
        des7.hide();
        des8.hide();
        des9.hide();
        des10.hide();
        des11.hide();
        des12.hide();
        des13.hide();
        des14.hide();
        des15.hide();
        des16.hide();
        des17.hide();

        // ========= Code, Koordiantensystem und SS_Tresh anzeigen =========
        this.showSourceCode();

        arrowProbs = new PolylineProperties();
        this.drawCoordinateSystem(origin, 500, 400, subXRange, subYRange, scale);

        Polyline ssLine = this.lang.newPolyline(new Node[] {new Coordinates(origin.getX(), (int) (origin.getY() -  scale * ss_tresh)),
                new Coordinates(origin.getX() + 500, (int) (origin.getY() -  scale * ss_tresh))},"ssLine", null, thresholdProps);


        // ========= Beginn: cwnd = 1, SlowStart  =========
        this.lang.nextStep();
        sc.highlight(0);
        cwnd = 1;
        this.lang.nextStep();
        this.highlightNextLine(0, 1);
        sender.phase = Sender.Phase.SlowStart;

        RectProperties rectProbs = new RectProperties();
        rectProbs.set("filled", true);
        TextProperties headerText = new TextProperties();
        headerText.set("color", Color.BLACK);

        Rect cARect = this.lang.newRect(new Coordinates(1150, 10), new Coordinates(1450, 35), "header", (DisplayOptions) null, rectProbs);
        cARect.changeColor("fillColor", Color.YELLOW, null, null);
        Text cAText = this.lang.newText(new Coordinates(1175, 12), "Phase: Congestion Avoidance", "currentPhase", (DisplayOptions) null, textProbs);

            Rect ssRect = this.lang.newRect(new Coordinates(1200, 10), new Coordinates(1400, 35), "header", (DisplayOptions) null, rectProbs);
        ssRect.changeColor("fillColor", Color.YELLOW, null, null);
        Text ssText = this.lang.newText(new Coordinates(1225, 12), "Phase: SlowStart", "currentPhase", (DisplayOptions) null, textProbs);

        cARect.hide();
        cAText.hide();
        // ========= Loop-Vorbereitung, Sender/ Receiver Animationen=========


        this.lang.nextStep();
        this.highlightNextLine(1, 2);

        for (int i = 0; i < userInput.length() || sender.notReceivedCompleteData(); i++) {

            senderLine = this.lang.newPolyline(new Node[]{new Coordinates(1100, 100), new Coordinates(1100, 300)}, "Sender", null, lineProbs);
            receiverLine = this.lang.newPolyline(new Node[]{new Coordinates(1500, 100), new Coordinates(1500, 300)}, "Receiver", null, lineProbs);

            senderHeader = this.lang.newText(new Coordinates(1075, 70), "Sender", "senderHeader", (DisplayOptions) null, textProbs);
            receiverHeader = this.lang.newText(new Coordinates(1470, 70), "Empfänger", "receiverTitle", (DisplayOptions) null, textProbs);

            this.lang.nextStep();
            this.highlightNextLine(2, 3);

            requestArrow = this.lang.newPolyline(new Node[]{new Coordinates(1100, 150), new Coordinates(1500, 190)}, "Sender", null, arrowProbs);
            requestText = this.lang.newText(new Coordinates(1230, 120), "Verbindungen: " + cwnd, "connectionAmount", (DisplayOptions) null, textProbs);

            this.lang.nextStep();
            this.highlightNextLine(3, 4);
            sender.waitForResponse(userInput.charAt(i));
            // Draws the Sender/Receiver-image
            switch (sender.received) {
                case TRIPLEACK: {
                    responseArrow = this.lang.newPolyline(new Node[]{new Coordinates(1500, 210), new Coordinates(1100, 250)}, "responseArrow", null, arrowProbs);
                    responseText = this.lang.newText(new Coordinates(1230, 250), "Antwort: 3 ACK", "responseArrow", (DisplayOptions) null, textProbs);
                    break;
                }
                case TIMEOUT: {
                    responseArrow = this.lang.newPolyline(new Node[]{new Coordinates(1500, 210), new Coordinates(1400, 250)}, "responseArrow", null, arrowProbs);
                    responseText = this.lang.newText(new Coordinates(1230, 250), "TIMEOUT", "responseArrow", (DisplayOptions) null, textProbs);
                    responseText.changeColor("color", Color.RED, null, null);
                    break;
                }
                default:
                    ACK:
                    {
                        responseArrow = this.lang.newPolyline(new Node[]{new Coordinates(1500, 210), new Coordinates(1100, 250)}, "responseArrow", null, arrowProbs);
                        responseText = this.lang.newText(new Coordinates(1230, 250), "Antwort: ACK", "responseArrow", (DisplayOptions) null, textProbs);
                        break;
                    }
            }


            this.lang.nextStep();
            this.highlightNextLine(4, 6);
            switch (sender.received) {
                case TRIPLEACK: {
                    this.lang.nextStep("3-ACK ist aufgetretten");

                    this.highlightNextLine(6, 7);
                    Text question = this.lang.newText(new Coordinates(1100, 350), "Ein 3ACK ist aufgetretten. Wie wird der Algrothmus nun vorgehen?", "a3Question", (DisplayOptions) null, textProbs);
                    this.lang.nextStep(3000);

                    this.highlightNextLine(7, 8);
                    Text answer1= this.lang.newText(new Coordinates(1100, 370), "1. ss_tresh wird auf die Hälfte des cwnd gesetzt: " + cwnd + "/2" + "=" + (int)cwnd/2, "answer1", (DisplayOptions) null, textProbs);
                    ss_tresh = cwnd / 2;

                    ssLine.hide();
                    ssLine = this.lang.newPolyline(new Node[] {new Coordinates(origin.getX(), origin.getY() - (int) (scale * ss_tresh)),
                            new Coordinates(origin.getX() + 500, origin.getY() - (int) (scale * ss_tresh))},"ssLine", null, thresholdProps);

                    this.lang.nextStep(3000);

                    this.highlightNextLine(8, 9);

                    Text answer2= this.lang.newText(new Coordinates(1100, 390), "2. cwnd wird auf ss_tresh gesetzt: cwnd = " + ss_tresh, "answer2", (DisplayOptions) null, textProbs);
                    cwnd = ss_tresh;
                    currentPoint = this.drawLine(currentPoint, origin, cwnd, subXRange, scale);

                    this.lang.nextStep(3000);

                    this.highlightNextLine(9, 10);
                    Text answer3= this.lang.newText(new Coordinates(1100, 410), "3. Der Sender befindet sich nun in der Congestion Avoidance Phase", "answer3", (DisplayOptions) null, textProbs);
                    if(sender.phase != Sender.Phase.CongestionAvoidance) {
                        sender.phase = Sender.Phase.CongestionAvoidance;
                        cARect.show();
                        cAText.show();
                    }

                    ssRect.hide();
                    ssText.hide();



                    this.lang.nextStep(3000);
                    question.hide();
                    answer1.hide();
                    answer2.hide();
                    answer3.hide();
                    break;
                }
                case TIMEOUT: {
                    this.lang.nextStep("TIMEOUT ist aufgetretten");
                    this.highlightNextLine(6, 12);

                    Text tQuestion = this.lang.newText(new Coordinates(1100, 350), "Ein TIMEOUT ist aufgetretten. Wie wird der Algrothmus nun vorgehen?", "tQuestion", (DisplayOptions) null, textProbs);
                    this.lang.nextStep(3000);
                    this.highlightNextLine(12, 13);

                    Text answer1= this.lang.newText(new Coordinates(1100, 370), "1. ss_tresh wird auf die Hälfte des cwnd gesetzt: " + cwnd + "/2" + "=" + (int)cwnd/2, "answer1", (DisplayOptions) null, textProbs);
                    ss_tresh = cwnd / 2;

                    ssLine.hide();
                    ssLine = this.lang.newPolyline(new Node[] {new Coordinates(origin.getX(), origin.getY() - (int) (scale * ss_tresh)),
                            new Coordinates(origin.getX() + 500, origin.getY() - (int) (scale * ss_tresh))},"ssLine", null, thresholdProps);

                    this.lang.nextStep(3000);
                    this.highlightNextLine(13, 14);

                    Text answer2= this.lang.newText(new Coordinates(1100, 390), "2. cwnd wird auf 1 gesetzt: cwnd = 1", "answer2", (DisplayOptions) null, textProbs);
                    cwnd = 1;
                    currentPoint = this.drawLine(currentPoint, origin, cwnd, subXRange, scale);

                    this.lang.nextStep(3000);

                    this.highlightNextLine(14, 15);// <==
                    Text answer3= this.lang.newText(new Coordinates(1100, 410), "3. Der Sender befindet sich nun in der Slow Start Phase", "answer3", (DisplayOptions) null, textProbs);

                    ssRect.show();
                    ssText.show();

                    cARect.hide();
                    cAText.hide();

                    sender.phase = Sender.Phase.SlowStart;

                    this.lang.nextStep(3000);
                    tQuestion.hide();
                    answer1.hide();
                    answer2.hide();
                    answer3.hide();

                    break;
                }
                default:
                    ACK:
                    {
                        this.lang.nextStep();
                        this.highlightNextLine(6, 17);
                        Text ackText;
                        this.lang.nextStep();
                        this.highlightNextLine(17, 18);
                        if (sender.phase == Sender.Phase.CongestionAvoidance) {

                            this.lang.nextStep();
                            this.highlightNextLine(18, 19);

                            cwnd++;
                            ackText = this.lang.newText(new Coordinates(1100, 350), "Algorithmus befindet sich in der CongestionAvoidance. cwnd wird um 1 erhöht und ist nun: "+ cwnd, "ackText",null);
                            currentPoint = this.drawLine(currentPoint, origin, cwnd, subXRange, scale);

                            this.lang.nextStep(3000);
                            ackText.hide();
                        } else {
                            this.lang.nextStep();
                            this.highlightNextLine(18, 20);

                            cwnd = cwnd * 2;
                            ackText = this.lang.newText(new Coordinates(1100, 350), "Algorithmus befindet sich im Slow Start. cwnd wird verdoppelt und ist nun: " + cwnd, "ackText",null);
                            currentPoint = this.drawLine(currentPoint, origin, cwnd, subXRange, scale);

                            this.lang.nextStep(3000);
                            ackText.hide();
                            break;
                        }
                    }
            }
            this.lang.nextStep();
            // Wird benötigt um alle Code-Bereiche zu entmakieren
            sc.unhighlight(10);
            sc.unhighlight(15);
            sc.unhighlight(19);
            sc.unhighlight(20);

            sc.highlight(22);
            if (cwnd >= ss_tresh && sender.phase != Sender.Phase.CongestionAvoidance) {
                this.lang.nextStep();

                this.highlightNextLine(22, 23);

                ssRect.hide();
                ssText.hide();

                ssRect.hide();
                ssText.hide();
                sender.phase = Sender.Phase.CongestionAvoidance;
                cARect.show();
                cAText.show();
            }

            this.lang.nextStep();
            sc.unhighlight(22);
            sc.unhighlight(23);

            senderHeader.hide();
            senderLine.hide();

            receiverHeader.hide();
            receiverLine.hide();

            requestArrow.hide();
            responseArrow.hide();

            requestText.hide();
            responseText.hide();
        }

        this.lang.nextStep();
        this.lang.hideAllPrimitives();

        des1.setText("TCP-CongestionControl ist ein Algorithmus, der das Netzwerk vor Überlastung bewahren", null, null);
        des2.setText("soll. Hierbei reguliert sich jeder Sender selbst mittels Nachrichten, die er aus dem", null, null);
        des3.setText("Netzwerk erhält bzw. die er nicht erhält im Falle eines TIMEOUTs.", null, null);
        des4.setText("Des Weiterten eignet sich der Algorithmus, da er sich der optimalen Bandbreite schnell in der SlowStart-Phase", null, null);
        des5.setText("nähert oder langsam während der CongestionAvoidance-Phase.Somit kann er sich dem Netzwerk", null, null);
        des6.setText("effizient anpassen.", null, null);

        cCR.show();
        cCT.show();

        des1.show();
        des2.show();
        des3.show();
        des4.show();
        des5.show();
        des6.show();
        this.lang.nextStep("Outro");
    }


    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        this.ss_tresh = (Integer)primitives.get("ssTresh");
        this.modus = (Integer)primitives.get("modus");
        this.userInput = (String)primitives.get("congestionOrder");

        this.thresholdProps = (PolylineProperties)props.getPropertiesByName("threshholdColour");
        //System.out.println(thresholdProps.getAllPropertyNames());
        this.thresholdProps.setName("thresholdProps");
        this.graphProps = (PolylineProperties)props.getPropertiesByName("graphColour");
        this.graphProps.setName("graphProps");

        this.startTransmission();
        return lang.toString();
    }

    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        if ((Integer)primitives.get("ssTresh") < 0) {
            JOptionPane.showMessageDialog((Component)null, "ss_tresh darf nicht negativ sein", "Invalid input", 0);
            return false;
        }else if ((this.stringBigger100((String)primitives.get("congestionOrder")))){
            JOptionPane.showMessageDialog((Component)null, "congestionOrder darf maximal 100 Zeichen lang sein", "Invalid input", 0);
            return false;
        }else if ( (Integer)primitives.get("modus") < 0 || 2 < (Integer)primitives.get("modus")) {
            JOptionPane.showMessageDialog((Component) null, "modus muss  0,1 oder 2 sein.", "Invalid input", 0);
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean stringBigger100(String input){
        return input.length() > 100;
    }

    public String getName() {
        return "TCPCongestionControl";
    }

    public String getAlgorithmName() {
        return "TCPCongestionControl";
    }

    public String getAnimationAuthor() {
        return "Joël Tschesche, Kadoglou Konstantinos";
    }

    public String getDescription(){
        return " Der TCP Congestion Control ist ein Algorithmus, der auf Seiten des Senders für TCP eingesetzt wird. Er dient zum Schutz des Netzwerkes vor Überlastungen."
                +"\n"
                +"Der Sender beginnt in der Slow Start Phase und verdoppelt pro ankommendes ACK die Anzahl der Verbindungen für die nächste Iteration."
                +"\n"
                +"Erreicht der Empänger eine gewisse Anzahl von Verbindungen, so wechselt er in die Congestion Avoidance Phase. Das Limit der Verbdinung wird ss_tresh genannt."
                +"\n"
                +"In der Congestion Avoidance Phase erhöht der Sender die Anzahl der Verbindungen pro ACK nur um 1, da er das Netwerk nicht unnötig überlassten will."
                +"\n"
                +"Des Weiteren behandelt das Beispiel auch, wie der Algorithmus mit zwei Sonderfällen umgeht ."
                +"\n"
                +"\n"
                +"Für diesen Algorithmus können Sie den ss_tresh selbst setzen, sowie die Reihenfolge der Nachrichten mittels Zeichenkette selbst bestimmen.";
    }

    public String getCodeExample(){
        return         "cwnd = 1" + "\n" +
        "sender.phase = SlowStart;" + "\n" +
        "while (sender.notReceivedCompleteData()){" + "\n" +
        "sender.request(cwnd);" + "\n" +
        "sender.receive();" + "\n" +
                "" + "\n" +

        "switch (sender.received){" + "\n" +
        "case TRIPLEACK: {" + "\n" +
        "ss_tresh = cwnd/2;" + "\n" +
        "cwnd = ss_tresh;" + "\n" +
        "sender.phase = Sender.Phase.CongestionAvoidance; break;" + "\n" +
        "}" + "\n" +

        "case TIMEOUT: {" + "\n" +
        "ss_tresh = cwnd/2;" + "\n" +
        "cwnd = 1;" + "\n" +
        "sender.phase = Sender.Phase.SlowStart; break;" + "\n" +
        "}" + "\n" +

        "default: ACK: {" + "\n" +
        "if (sender.phase == Sender.Phase.CongestionAvoidance)" + "\n" +
        "cwnd++;" + "\n" +
        "else cwnd = cwnd * 2; break;" + "\n" +
        "}" + "\n" +
                "" + "\n" +
        "if (cwnd >= ss_tresh)" + "\n" +
        "sender.phase =  Sender.Phase.CongestionAvoidance;" + "\n" +
        "}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    /*
        Shows source code in animal
     */
    public void showSourceCode(){
        scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        sc = lang.newSourceCode(new Coordinates(40,40),"sourceCode", null, scProps);
        sc.addCodeLine("cwnd = 1", null, 0, null);
        sc.addCodeLine("sender.phase = SlowStart;", null, 0, null);
        sc.addCodeLine("while (sender.notReceivedCompleteData()){", null, 0, null);
        sc.addCodeLine("sender.request(cwnd);", null, 1, null);
        sc.addCodeLine("sender.receive();", null, 1, null);

        sc.addCodeLine("", null, 1, null);
        sc.addCodeLine("switch (sender.received){", null, 1, null);

        sc.addCodeLine("case TRIPLEACK: {", null, 2, null);
        sc.addCodeLine("ss_tresh = cwnd/2;", null, 3, null);
        sc.addCodeLine("cwnd = ss_tresh;", null, 3, null);
        sc.addCodeLine("sender.phase = Sender.Phase.CongestionAvoidance; break;", null, 3, null);
        sc.addCodeLine("}", null, 2, null);

        sc.addCodeLine("case TIMEOUT: {", null, 2, null);
        sc.addCodeLine("ss_tresh = cwnd/2;", null, 3, null);
        sc.addCodeLine("cwnd = 1;", null, 3, null);
        sc.addCodeLine("sender.phase = Sender.Phase.SlowStart; break;", null, 3, null);
        sc.addCodeLine("}", null, 2, null);

        sc.addCodeLine("default: ACK: {", null, 2, null);
        sc.addCodeLine("if (sender.phase == Sender.Phase.CongestionAvoidance)", null, 3, null);
        sc.addCodeLine("cwnd++;", null, 4, null);
        sc.addCodeLine("else cwnd = cwnd * 2; break;", null, 3, null);
        sc.addCodeLine("}", null, 2, null);

        sc.addCodeLine("if (cwnd >= ss_tresh)", null, 1, null);
        sc.addCodeLine("sender.phase =  Sender.Phase.CongestionAvoidance;", null, 2, null);
        sc.addCodeLine("}", null, 0, null);
    }

    /*
        unhighlights the previous line and highlights the next one
     */
    public void highlightNextLine(int previous, int next){
        sc.unhighlight(previous);
        sc.highlight(next);
    }

    /*
        Calculates the longest continiously sequence of a's in a string.
        This method is needed for the dynamic range of the ordinate
    */
    public int longestASequence(){
        int lon = 0;
        int shor = 0;

        for(int i = 0; i < userInput.length() && Math.pow(2, i) < ss_tresh; i++){
            if (userInput.charAt(i) == 'a'){
                shor++;
                if (shor > lon) lon = shor;
            }
            else shor = 0;
        }
        return lon;
    }

    /*
        Creates a random String for the second mode.
        The string is generated via a simulation.
     */
    public String createString() {
        Sender dummy = new Sender(data);
        String s = "";
        int cwnd = 1;
        dummy.phase = Sender.Phase.SlowStart;
        counter = 0;

        while (dummy.notReceivedCompleteData()) {
            dummy.request(cwnd);
            dummy.determineCongestion();

            switch (dummy.received) {
                case TRIPLEACK: {
                    s = s + "3";
                    ss_tresh = ss_tresh / 2;
                    cwnd = ss_tresh;
                    dummy.phase = Sender.Phase.CongestionAvoidance;
                    break;
                }
                case TIMEOUT: {
                    s = s + "t";
                    ss_tresh = ss_tresh / 2;
                    cwnd = 1;
                    dummy.phase = Sender.Phase.SlowStart;
                    break;
                }
                default:
                    ACK:{
                        s = s + "a";
                        if (dummy.phase == Sender.Phase.CongestionAvoidance) cwnd++;
                        else cwnd = cwnd * 2;
                        break;
                    }
            }
            if(cwnd >= ss_tresh) dummy.phase = Sender.Phase.CongestionAvoidance;
        }
        return s;
    }

    /*
       draws the coordinate system
     */
    public void drawCoordinateSystem(Coordinates startPoint, int xRange, int yRange, int xDistance, int yDistance, double scale) {
        arrowProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrowProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
        arrowProbs.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        arrowProbs.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

        // creating abcissa and absicca value. In this context time
        Polyline xLine = this.lang.newPolyline(new Node[]{startPoint, new Coordinates(startPoint.getX() + xRange + 20, startPoint.getY())}, "abscisse", null, arrowProbs);
        Text xValueText = this.lang.newText(new Coordinates(startPoint.getX() + xRange + 25, startPoint.getY() - 10), "Zeit", "xValue", (DisplayOptions) null, textProbs);

        // creating ordinate and ordinate value. In this context bandwith
        Polyline yLine = this.lang.newPolyline(new Node[]{startPoint, new Coordinates(startPoint.getX(), startPoint.getY() - yRange - 25)}, "ordinate", null, arrowProbs);
        Text yValueText = this.lang.newText(new Coordinates(startPoint.getX() - 25, startPoint.getY() - yRange - 50), "Bandbreite", "yValue", (DisplayOptions) null, textProbs);

        // draws the subranges on the abcissa
        this.drawXSubRanges(startPoint, xDistance, xRange);
        // draws the subranges on the ordinate
        this.drawYSubRanges(startPoint, yDistance, yRange);
    }

    /*
        draws the subranges of the abscissa.
     */
    public void drawYSubRanges(Coordinates startPoint, int yDistance, int maxYRange) {

        int subRanges = maxYRange / 4;
        for (int i = 0; i * subRanges < maxYRange + 1; i++) {
            Polyline subRangeLine = this.lang.newPolyline(new Node[]{new Coordinates(startPoint.getX(), startPoint.getY() - i * subRanges),
                            new Coordinates(startPoint.getX() - 10, startPoint.getY() - i * subRanges)}, "rangeLine",
                    null, lineProbs);
            Text rangeText = this.lang.newText(new Coordinates(startPoint.getX() - 45, startPoint.getY() - i * subRanges - 10),
                    "" + i * (int)this.dynamicLimit()/4, "range", null, textProbs);
        }
    }

    /*
        draws the subranges of the abscissa.
     */
    public void drawXSubRanges(Coordinates startPoint, int xDistance, int maxXRange) {
        for (int i = 0; i * xDistance < maxXRange; i++) {
            Polyline subRangeLine = this.lang.newPolyline(new Node[]{new Coordinates(startPoint.getX() + i * xDistance, startPoint.getY()), new Coordinates(startPoint.getX() + i * xDistance, startPoint.getY() + 10)}, "rangeLine", null, lineProbs);
        }

        int a = 1;
        if ( userInput.length() > 20) a = 5;
        for (int i = 0; i * xDistance < maxXRange + 1; i = i + a){
            Text rangeText = this.lang.newText(new Coordinates(startPoint.getX() + i * xDistance - 5, startPoint.getY() + 10), "" + i, "range", null, textProbs);
        }
    }

    /*
        determinates how huge the range on the ordinate should be.
     */
    public double dynamicLimit(){
        int i = 1;
        while (i < Math.pow(2, this.longestASequence()-1))
            i = i*10;

        return i;
    }

    /*
        creates a new point and draws a new line between the previous iteration point and a the new one
     */
    public Coordinates drawLine(Coordinates currentPoint, Coordinates origin, int cwnd, int xDistance, double scale){

        PolylineProperties greenLineProps = new PolylineProperties();
        greenLineProps.set("color", Color.GREEN);
        Coordinates endPoint = new Coordinates(currentPoint.getX() + xDistance, origin.getY() - (int) (cwnd * scale));
        Polyline n = this.lang.newPolyline(new Node[]{currentPoint, endPoint}, "n", null, graphProps);

        return endPoint;
    }

    public static void main(String[] args) {
//        Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
//                "TCPCongestionControl", "Joël Tschesche, Konstantinos Kadoglou", 640, 480);
        TCPCongestionControl t = new TCPCongestionControl();
        t.startTransmission();
        Animal.startGeneratorWindow(t);
        //Animal.startAnimationFromAnimalScriptCode(l.toString());

    }
}