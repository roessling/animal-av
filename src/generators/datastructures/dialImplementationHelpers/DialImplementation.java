package generators.datastructures.dialImplementationHelpers;

import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;
import translator.Translator;

import java.awt.*;
import java.util.*;
import java.util.List;

@SuppressWarnings("unused")
public class DialImplementation {

    private int S;

    private DialImplementationIntegerList[] A;

    private int P;

    private int N;

    /*private static final String DESCRIPTION =   "Die Dial-Implementierung ist eine Implementierung der beschränkten monotonen Prioritätswarteschlange.\n"
                                                + "Der Vorteil dieser Implementierung ist die konstante Zugriffszeit auf das Minimum der Warteschlange\n"
                                                + "und die konstante Komplexität für das Einfügen eines neuen Elements. Monoton ist diese Warteschlange,\n"
                                                + "da die Elemente in einer totalen Ordnung gespeichert werden und zu jeder Zeit mit konstanter Komplexität\n"
                                                + "auf das aktuelle Minimum zugegriffen werden kann.\n"
                                                + "Die Schlüsselwerte, die in der Warteschlange gespeichert werden, müssen numerischer Natur sein und die Modulo-\n"
                                                + "Operation unterstützen.\n"
                                                +"\n"
                                                + "Die Datenstruktur wird durch drei Objektvariablen definiert:\n"
                                                + "1. S: Die Schlüsselwerte, die gespeichert werden, befinden sich in der Menge {0, ..., S - 1}\n"
                                                + "2. A: In dem Array A der Größe S werden die Schlüsselwerte gespeichert\n"
                                                + "3. P: P enthält den Index des Arrays A, an der das jeweils aktuelle Minimum der Schlüsselwerte zu finden ist\n"
                                                + "\n"
                                                + "An einer Position des Arrays können beliebig viele Elemente des gleichen Schlüsselwerts gespeichert werden.\n"
                                                + "Eine praktische Anwendung dieser Datenstruktur ist beispielsweise der Dijsktra-Algorithmus, bei dem die\n"
                                                + "jeweils nächsten kürzesten Kanten gespeichert werden müssen. Nun kann es sein, dass es zwei Kanten a1 und a2\n"
                                                + " derselben Länge l geben kann. Der Schlüsselwert dieser beiden Kanten ist l und an A[l] sind dann a1 und a2 zu finden.";*/

    private final String DESCRIPTION;

    private static final String INSERT_CODE = "insert(K):"
            + "\n\tpos = (K - A[P] + P) mod S"
            + "\n\tA[pos].insert(K)"
            + "\n\tif K < A[P]:"
            + "\n\t\tP = pos";

    private final String SUMMARY;/* =       "In der Ausführung der aufeinanderfolgenden Operationen haben wir gesehen, dass alle Berechnungen in den Methoden\n"
                                                + "number, insert und decreaseKey in konstanter Zeit durchgeführt werden. Nur das Finden des neuen Minimums in der\n"
                                                + "Methode extractMinimum benötigt im worst case S - 1 Operationen. Ein neues Minimum muss allerdings nur gefunden\n"
                                                + "werden, falls die Menge der Werte an der entsprechenden Position nach der Extraktion leer ist. Und auch der\n"
                                                + "worst case tritt nur ein, falls die gespeicherten Schlüsselwerte maximal weit voneinander entfernt sind.\n"
                                                + "Und selbst im worst case hat die Datenstruktur noch eine lineare Komplexität, was sie für entsprechende Anwendungen\n"
                                                + "deutlich effizienter macht als übliche Warteschlangen.\n"
                                                + "\n"
                                                + "Dem aufmerksamen Betrachter wird aufgefallen sein, dass zu jeder Zeit zwischen den Operationen die folgende\n"
                                                + "Invariante erfüllt ist:\n"
                                                + "Für eine Position i in der Menge {0, ..., S - 1} ist der Schlüsselwert an dieser Position größer als der\n"
                                                + "Schlüsselwert an der Position P und zwar um genau (S + i - P) mod S.";*/

    /* Animal Properties */

    private Language lang;

    private DialImplementationBag[] bags;

    private Coordinates center;

    private Text title;

    private Circle circle;

    private Circle circleOut;

    private StringArray viewArray;

    private ArrayMarker pMarker;

    private Text nText;

    private Text pText;

    private Text nextMethodLabel;

    private Text nextMethod;

    private static final TextProperties nextMethodProperties = new TextProperties();

    private Offset nextMethodOffset;

    private SourceCodeProperties codeProperties;

    private SourceCode numberSourceCode;

    private SourceCode insertSourceCode;

    private SourceCode extractMinimumSourceCode;

    private SourceCode decreaseKeySourceCode;

    private Translator translator;

    static {
        DialImplementation.nextMethodProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 18));
    }

    public DialImplementation(int S, Language l, Translator translator) {
        if(S <= 0)
            throw new IllegalArgumentException("The span must be greater than zero, but was " + S);

        this.S = S;
        this.N = 0;
        this.A = new DialImplementationIntegerList[this.S];
        for(int i = 0; i < this.S; i++)
            this.A[i] = new DialImplementationIntegerList();
        this.P = new Random().nextInt(this.S);

        this.bags = new DialImplementationBag[this.S];

        this.translator = translator;

        DESCRIPTION = translator.translateMessage("descriptionGlobal");

        SUMMARY = translator.translateMessage("summaryGlobal");

        this.initAnimal(l);
    }

    private void initAnimal(Language l) {
        this.lang = l;
        this.lang.setStepMode(true);

        float deltaAngle = 360 / this.S;
        int r = this.getRadius();
        this.center = new Coordinates(150 + r, 150 + r);

        Offset sourceCodeOffset = new Offset(300, -50, this.center, null);

        this.codeProperties = new SourceCodeProperties();
        this.codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
        this.codeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
        this.numberSourceCode = this.lang.newSourceCode(sourceCodeOffset, "NumberSourceCode", null, codeProperties);
        this.numberSourceCode.addCodeLine("number():", null, 0, null);
        this.numberSourceCode.addCodeLine("return N", null, 1, null);
        this.numberSourceCode.hide();

        this.insertSourceCode = this.lang.newSourceCode(sourceCodeOffset, "InsertSourceCode", null, codeProperties);
        this.insertSourceCode.addCodeLine("insert(K):", null, 0, null);
        this.insertSourceCode.addCodeLine("if A[P].isEmpty():", null, 1, null);
        this.insertSourceCode.addCodeLine("A[P].insert(K)", null, 2, null);
        this.insertSourceCode.addCodeLine("else:", null, 1, null);
        this.insertSourceCode.addCodeLine("pos = (K - A[P] + P) mod S", null, 2, null);
        this.insertSourceCode.addCodeLine("A[pos].insert(K)", null, 2, null);
        this.insertSourceCode.addCodeLine("if K < A[P]:", null, 2, null);
        this.insertSourceCode.addCodeLine("P = pos", null, 3, null);
        this.insertSourceCode.addCodeLine("N = N + 1", null, 1, null);
        this.insertSourceCode.hide();

        this.extractMinimumSourceCode = this.lang.newSourceCode(sourceCodeOffset, "ExtractMinimumSourceCode",null,  codeProperties);
        this.extractMinimumSourceCode.addCodeLine("extractMinimum():", null, 0, null);
        this.extractMinimumSourceCode.addCodeLine("K = A[P].remove()", null, 1, null);
        this.extractMinimumSourceCode.addCodeLine("if N != 0 && A[P].isEmpty():", null, 1, null);
        this.extractMinimumSourceCode.addCodeLine("do:", null, 2, null);
        this.extractMinimumSourceCode.addCodeLine("P = P + 1 mod S",null, 3, null);
        this.extractMinimumSourceCode.addCodeLine("while A[P].isEmpty()", null, 2, null);
        this.extractMinimumSourceCode.addCodeLine("N = N - 1", null, 1, null);
        this.extractMinimumSourceCode.addCodeLine("return K", null, 1, null);
        this.extractMinimumSourceCode.hide();

        this.decreaseKeySourceCode = this.lang.newSourceCode(sourceCodeOffset, "DecreaseKeySourceCode", null, codeProperties);
        this.decreaseKeySourceCode.addCodeLine("decreaseKey(K, Knew):", null, 0, null);
        this.decreaseKeySourceCode.addCodeLine("pos1 = (K - A[P] + P) mod S", null, 1, null);
        this.decreaseKeySourceCode.addCodeLine("pos2 = (Knew - A[P] + P) mod S", null, 1, null);
        this.decreaseKeySourceCode.addCodeLine("A[pos1].remove()", null, 1, null);
        this.decreaseKeySourceCode.addCodeLine("A[pos2].insert(Knew)", null, 1, null);
        this.decreaseKeySourceCode.addCodeLine("if pos1 == P:", null, 1, null);
        this.decreaseKeySourceCode.addCodeLine("P = pos2", null, 2, null);
        this.decreaseKeySourceCode.addCodeLine("else:", null, 1, null);
        this.decreaseKeySourceCode.addCodeLine("if Knew < A[P]:", null, 2, null);
        this.decreaseKeySourceCode.addCodeLine("P = pos2", null, 3, null);
        this.decreaseKeySourceCode.hide();

        TextProperties titleProperties = new TextProperties();
        titleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));

        this.title = this.lang.newText(new Offset(-125, -r - 130, this.center, null), translator.translateMessage("titleString"), "Title", null, titleProperties);

        RectProperties titleBoxProperties = new RectProperties();
        titleBoxProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        titleBoxProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        titleBoxProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);

        Rect titleBox = lang.newRect(new Offset(-10, -10, title, null), new Offset(256, 30, title, null), "TitleBox", null, titleBoxProperties);

        SourceCode description = this.lang.newSourceCode(new Offset(-(150 + r - 125) / 2, 100, title, null), "Description", null, new SourceCodeProperties());
        Arrays.asList(DESCRIPTION.split("\n")).forEach(s -> description.addCodeLine(s, null, 0, null));

        this.lang.nextStep(translator.translateMessage("introduction"));

        description.hide();
        TextProperties initializationProperties = new TextProperties();
        initializationProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
        Text initialization = this.lang.newText(new Offset(0, 0, this.center, null), translator.translateMessage("initialization"), "InitializationText", null, initializationProperties);
        ArcProperties initializationMoveArcProperties = new ArcProperties();
        initializationMoveArcProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
        initializationMoveArcProperties.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 180);
        initializationMoveArcProperties.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 90);
        Arc initializationMoveArc = this.lang.newArc(new Offset(0, -75, this.center, null), new Coordinates(300, 75), "InitializationMoveArc", null, initializationMoveArcProperties);
        initialization.moveVia(null, null, initializationMoveArc, new MsTiming(2000), new MsTiming(1000));

        SourceCodeProperties initializationListProperties = new SourceCodeProperties();
        initializationListProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
        initializationListProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
        SourceCode initializationList = this.lang.newSourceCode(new Offset(300, -50, this.center, null), "InitializationList", null, initializationListProperties);
        initializationList.hide();
        initializationList.addCodeLine(translator.translateMessage("initCodeLine0") + this.S, null, 0, null);
        initializationList.addCodeLine(translator.translateMessage("initCodeLine1") + this.P + translator.translateMessage("initCodeLine2"), null, 0, null);
        initializationList.show(new MsTiming(3000));

        this.lang.nextStep();

        initializationList.highlight(0, 0, false);

        ArrayProperties dialProperties = new ArrayProperties();
        dialProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        dialProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        dialProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        dialProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        dialProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, hex2Rgb("#32CD32"));
        dialProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, hex2Rgb("#6A5ACD"));

        this.circle = this.lang.newCircle(new Offset(0, 0, this.center, null), r - 40, "InnerCircle", null, new CircleProperties());
        CircleProperties circleOutProperties = new CircleProperties();
        circleOutProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        circleOutProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        circleOutProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        this.circleOut = this.lang.newCircle(new Offset(0, 0, this.center, null), r + 40, "OuterCircle", null, circleOutProperties);
        for(int i = 0; i < this.S; i++) {
            Offset coords = new Offset((int) (Math.cos(Math.toRadians(deltaAngle * i)) * r), (int) (Math.sin(Math.toRadians(deltaAngle * i)) * r), center, null);
            Circle dialCircle = this.lang.newCircle(coords, 30, "DialCircle" + i, null, new CircleProperties());
            Offset textOffset = new Offset(15, 23, dialCircle, null);
            Text arrayMultiplier = this.lang.newText(textOffset, this.A[i].length() + " * ", "ArrayMultiplier" + i, null, new TextProperties());
            Offset arrayOffset = new Offset(20, -5, arrayMultiplier, null);
            StringArray array = this.lang.newStringArray(arrayOffset, new String[]{this.A[i].getFirst() == null ? " " : this.A[i].getFirst().toString()}, "Array" + i, null, dialProperties);
            array.showIndices(false, null, null);
            this.bags[i] = new DialImplementationBag(dialCircle, arrayMultiplier, array);
        }

        this.viewArray = this.lang.newStringArray(new Offset(300, -170, this.center, null), this.getStringArray(), "ViewArray", null, dialProperties);
        TextProperties nTextProperties = new TextProperties();
        nTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 15));
        this.nText = this.lang.newText(new Offset(0, 45, this.viewArray, null), "N = 0", "nText", null, nTextProperties);

        this.lang.nextStep();

        initializationList.unhighlight(0, 0, false);
        initializationList.highlight(1, 0, false);

        this.bags[this.P].getOuterCircle().changeColor(null, Color.RED, null, null);

        ArrayMarkerProperties pMarkerProperties = new ArrayMarkerProperties();
        pMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "P");
        pMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        this.pMarker = this.lang.newArrayMarker(this.viewArray, this.P, "P" + this.P, null, pMarkerProperties);

        this.pText = this.lang.newText(new Offset(75, 0, this.nText, null), "P = " + this.P, "pText", null, nTextProperties);

        this.lang.nextStep();

        initialization.hide();
        initializationList.hide();
        this.nextMethodLabel = this.lang.newText(new Offset(300, -75, this.center, null), translator.translateMessage("nextMethodLabel"), "nextMethodLabel", null, initializationProperties);
        this.nextMethodOffset = new Offset(200, 0, this.nextMethodLabel, null);
        this.nextMethod = this.lang.newText(this.nextMethodOffset, "", "nextMethod", null, DialImplementation.nextMethodProperties);
        //this.randomMethodSelection(20);
        //System.out.println(this.bags[1].getCenter().getX() + ", " + this.bags[1].getCenter().getY());
        //this.turnUp(1);

        //this.lang.nextStep();
        //this.bags[1].getOuterCircle().changeColor(null, Color.BLACK, null, null);
        //this.bags[2].getOuterCircle().changeColor(null, Color.RED, null, null);
        //System.out.println(this.bags[1].getCenter().getX() + ", " + this.bags[1].getCenter().getY());
        //this.turnDown(2);
        //this.summary();
    }

    public void randomMethodSelection(int numMethods) {
        boolean lastMethodNumber = false;

        int numElements = 0;
        int[] dial = new int[S];
        int minimum = 0;

        List<String[]> ops = new LinkedList<>();
        for(int i = 0; i < numMethods; i++) {
            if(lastMethodNumber) {
                lastMethodNumber = false;
                if(numElements > 0) {
                    if(minimum == 0 && dial[minimum] == numElements) {
                        int method = new Random().nextInt(2);
                        switch(method) {
                            case 0:
                                int k = new Random().nextInt(S);
                                //System.out.println("insert(" + k + ")");
                                ops.add(new String[]{"insert", Integer.toString(k)});
                                numElements++;
                                dial[k]++;
                                if(numElements == 1)
                                    minimum = k;
                                else {
                                    if (k < minimum)
                                        minimum = k;
                                }
                                //this.insert(k);
                                break;
                            case 1:
                                //System.out.println("extractMinimum()");
                                ops.add(new String[]{"extractMinimum", "-"});
                                numElements--;
                                dial[minimum]--;
                                if(numElements > 0) {
                                    if (dial[minimum] == 0) {
                                        do {
                                            minimum++;
                                        } while (dial[minimum] == 0);
                                    }
                                }
                                //this.extractMinimum();
                                break;
                        }
                    } else {
                        int method = new Random().nextInt(3);
                        int k;
                        switch (method) {
                            case 0:
                                int pos;
                                do {
                                    pos = new Random().nextInt(S);
                                } while (dial[pos] == 0 || pos == 0);
                                k = pos;
                                int knew = new Random().nextInt(pos);
                                //System.out.println("decreaseKey(" + k + ", " + knew + ")");
                                ops.add(new String[]{"decreaseKey", Integer.toString(k) + "->" + Integer.toString(knew)});
                                dial[k]--;
                                dial[knew]++;
                                if(knew < minimum)
                                    minimum = knew;
                                //this.decreaseKey(k, knew);
                                break;
                            case 1:
                                k = new Random().nextInt(S);
                                //System.out.println("insert(" + k + ")");
                                ops.add(new String[]{"insert", Integer.toString(k)});
                                numElements++;
                                dial[k]++;
                                if(numElements == 1)
                                    minimum = k;
                                else {
                                    if (k < minimum)
                                        minimum = k;
                                }
                                //this.insert(k);
                                break;
                            case 2:
                                //System.out.println("extractMinimum()");
                                ops.add(new String[]{"extractMinimum", "-"});
                                numElements--;
                                dial[minimum]--;
                                if(numElements > 0) {
                                    if (dial[minimum] == 0) {
                                        do {
                                            minimum++;
                                        } while (dial[minimum] == 0);
                                    }
                                }
                                //this.extractMinimum();
                                break;
                        }
                    }
                } else {
                    int k = new Random().nextInt(S);
                    //System.out.println("insert(" + k + ")");
                    ops.add(new String[]{"insert", Integer.toString(k)});
                    numElements++;
                    dial[k]++;
                    if(numElements == 1)
                        minimum = k;
                    else {
                        if (k < minimum)
                            minimum = k;
                    }
                    //this.insert(k);
                }
            } else {
                if(numElements > 0) {
                    if(minimum == 0 && dial[minimum] == numElements) {
                        int method = new Random().nextInt(3);
                        switch(method) {
                            case 0:
                                int k = new Random().nextInt(S);
                                //System.out.println("insert(" + k + ")");
                                ops.add(new String[]{"insert", Integer.toString(k)});
                                numElements++;
                                dial[k]++;
                                if(numElements == 1)
                                    minimum = k;
                                else {
                                    if (k < minimum)
                                        minimum = k;
                                }
                                //this.insert(k);
                                break;
                            case 1:
                                //System.out.println("extractMinimum()");
                                ops.add(new String[]{"extractMinimum", "-"});
                                numElements--;
                                dial[minimum]--;
                                if(numElements > 0) {
                                    if (dial[minimum] == 0) {
                                        do {
                                            minimum++;
                                        } while (dial[minimum] == 0);
                                    }
                                }
                                //this.extractMinimum();
                                break;
                            case 2:
                                lastMethodNumber = true;
                                //System.out.println("number()");
                                ops.add(new String[]{"number", "-"});
                                //this.number();
                                break;
                        }
                    } else {
                        int method = new Random().nextInt(4);
                        int k;
                        switch (method) {
                            case 0:
                                int pos;
                                do {
                                    pos = new Random().nextInt(S);
                                } while (dial[pos] == 0 || pos == 0);
                                k = pos;
                                int knew = new Random().nextInt(pos);
                                //System.out.println("decreaseKey(" + k + ", " + knew + ")");
                                ops.add(new String[]{"decreaseKey", Integer.toString(k) + "->" + Integer.toString(knew)});
                                dial[k]--;
                                dial[knew]++;
                                if(knew < minimum)
                                    minimum = knew;
                                //this.decreaseKey(k, knew);
                                break;
                            case 1:
                                k = new Random().nextInt(S);
                                //System.out.println("insert(" + k + ")");
                                ops.add(new String[]{"insert", Integer.toString(k)});
                                numElements++;
                                dial[k]++;
                                if(numElements == 1)
                                    minimum = k;
                                else {
                                    if (k < minimum)
                                        minimum = k;
                                }
                                //this.insert(k);
                                break;
                            case 2:
                                //System.out.println("extractMinimum()");
                                ops.add(new String[]{"extractMinimum", "-"});
                                numElements--;
                                dial[minimum]--;
                                if(numElements > 0) {
                                    if (dial[minimum] == 0) {
                                        do {
                                            minimum++;
                                        } while (dial[minimum] == 0);
                                    }
                                }
                                //this.extractMinimum();
                                break;
                            case 3:
                                lastMethodNumber = true;
                                //System.out.println("number()");
                                ops.add(new String[]{"number", "-"});
                                //this.number();
                                break;
                        }
                    }
                } else {
                    int method = new Random().nextInt(2);
                    switch(method) {
                        case 0:
                            int k = new Random().nextInt(S);
                            //System.out.println("insert(" + k + ")");
                            ops.add(new String[]{"insert", Integer.toString(k)});
                            numElements++;
                            dial[k]++;
                            if(numElements == 1)
                                minimum = k;
                            else {
                                if (k < minimum)
                                    minimum = k;
                            }
                            //this.insert(k);
                            break;
                        case 1:
                            lastMethodNumber = true;
                            //System.out.println("number()");
                            ops.add(new String[]{"number", "-"});
                            //this.number();
                            break;
                    }
                }
            }
            //System.out.println(this);
        }

        //ops.forEach(op -> System.out.print(op[0]));
        methodSelection(ops);
        //this.summary();
    }

    public void methodSelection(List<String[]> ops) {
        for(int i = 0; i < ops.size(); i++) {
            if(ops.size() - i - 1 > 2)
                nextMethod.setText(DialImplementation.getPrettyOperator(ops.get(i + 1)) + ", " +
                                        DialImplementation.getPrettyOperator(ops.get(i + 2)) + ", " +
                                        DialImplementation.getPrettyOperator(ops.get(i + 3)) + ((ops.size() - i - 1 > 3)? ", ..." : ""), null, null);
            else if(ops.size() - i - 1 == 2)
                nextMethod.setText(DialImplementation.getPrettyOperator(ops.get(i + 1)) + ", " +
                        DialImplementation.getPrettyOperator(ops.get(i + 2)), null, null);
            else if(ops.size() - i - 1 == 1)
                nextMethod.setText(DialImplementation.getPrettyOperator(ops.get(i + 1)), null, null);
            else
                nextMethod.setText("", null, null);
            parseOp(ops.get(i));
        }
        //ops.forEach(this::parseOp);

        this.summary();
    }

    private static String getPrettyOperator(String[] op) {
        String opName = op[0];
        switch(opName) {
            case "number":
                opName += "()";
                break;
            case "extractMinimum":
                opName += "()";
                break;
            case "insert":
                int k = Integer.valueOf(op[1]);
                opName += "(" + k + ")";
                break;
            case "decreaseKey":
                String[] keys = op[1].split("->");
                int kOld = Integer.valueOf(keys[0]);
                int kNew = Integer.valueOf(keys[1]);
                opName += "(" + kOld + ", " + kNew + ")";
                break;
        }
        return opName;
    }

    private void parseOp(String[] op) {
        String opName = op[0];
        switch(opName) {
            case "number":
                this.number();
                break;
            case "extractMinimum":
                this.extractMinimum();
                break;
            case "insert":
                int k = Integer.valueOf(op[1]);
                this.insert(k);
                break;
            case "decreaseKey":
                String[] keys = op[1].split("->");
                int kOld = Integer.valueOf(keys[0]);
                int kNew = Integer.valueOf(keys[1]);
                this.decreaseKey(kOld, kNew);
                break;
        }
    }

    private void summary() {
        this.circle.hide();
        this.circleOut.hide();
        Arrays.asList(this.bags).forEach(DialImplementationBag::hide);
        this.viewArray.hide();
        this.nextMethod.hide();
        this.nextMethodLabel.hide();
        this.pText.hide();
        this.nText.hide();
        this.pMarker.hide();
        TextProperties summaryLabelProperties = new TextProperties();
        summaryLabelProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
        Text summaryLabel = this.lang.newText(new Offset(0, 100, this.title, null), translator.translateMessage("summaryTitle"), "summaryLabel", null, summaryLabelProperties);
        SourceCode summary = this.lang.newSourceCode(new Offset(0, 25, summaryLabel, null), "Summary", null, new SourceCodeProperties());
        Arrays.asList(SUMMARY.split("\n")).forEach(s -> summary.addCodeLine(s, null, 0, null));

        lang.nextStep(translator.translateMessage("summary"));
    }

    private int getRadius() {
        return 50 + this.S * 8;
    }

    private Arc getArc(Coordinates center, DialImplementationBag bag, int r, float angle) {
        Offset start = bag.getCenter();
        int ax = r;
        int ay = 0;
        int bx = start.getX();
        int by = start.getY();
        int ab = ax * bx + ay * by;
        double la = Math.sqrt(ax * ax + ay * ay);
        double lb = Math.sqrt(bx * bx + by * by);
        double a = Math.toDegrees(Math.acos(ab / (la * lb)));
        if(start.getY() > 0)
            a = 360 - a;
        int startAngle = (int) Math.round(a);
        ArcProperties arcProperties = new ArcProperties();
        arcProperties.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, startAngle);
        arcProperties.set(AnimationPropertiesKeys.ANGLE_PROPERTY, (int) angle);
        arcProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
        return this.lang.newArc(center, new Coordinates(r, r), "Arc", null, arcProperties);
    }

    private int turn(int angle) {
        int duration = 0;
        for(DialImplementationBag bag : this.bags)
            duration = bag.moveVia(this.getArc(this.center, bag, this.getRadius(), angle));

        return duration;
    }

    private int turn(int angle, int timeOffset) {
        int duration = 0;
        for(DialImplementationBag bag : this.bags)
            duration = bag.moveVia(this.getArc(this.center, bag, this.getRadius(), angle), timeOffset);

        return duration;
    }

    private int turnUp(int start) {
        //this.lang.nextStep();
        Offset startCoords = this.bags[start].getCenter();
        int ax = 0;
        int ay = -this.getRadius();
        int bx = startCoords.getX();
        int by = startCoords.getY();
        int ab = ax * bx + ay * by;
        double la = Math.sqrt(ax * ax + ay * ay);
        double lb = Math.sqrt(bx * bx + by * by);
        double a = Math.toDegrees(Math.acos(ab / (la * lb)));
        if(startCoords.getX() > 0)
            a = 360 - a;
        int startAngle = (int) Math.round(a);
        return this.turn(startAngle);
    }

    private int turnUp(int start, int timeOffset) {
        //this.lang.nextStep();
        Offset startCoords = this.bags[start].getCenter();
        int ax = 0;
        int ay = -this.getRadius();
        int bx = startCoords.getX();
        int by = startCoords.getY();
        int ab = ax * bx + ay * by;
        double la = Math.sqrt(ax * ax + ay * ay);
        double lb = Math.sqrt(bx * bx + by * by);
        double a = Math.toDegrees(Math.acos(ab / (la * lb)));
        if(startCoords.getX() > 0)
            a = 360 - a;
        int startAngle = (int) Math.round(a);
        return this.turn(startAngle, timeOffset);
    }

    private int turnDown(int start) {
        //this.lang.nextStep();
        Offset startCoords = this.bags[start].getCenter();
        int ax = 0;
        int ay = this.getRadius();
        int bx = startCoords.getX();
        int by = startCoords.getY();
        int ab = ax * bx + ay * by;
        double la = Math.sqrt(ax * ax + ay * ay);
        double lb = Math.sqrt(bx * bx + by * by);
        double a = Math.toDegrees(Math.acos(ab / (la * lb)));
        if(startCoords.getX() < 0)
            a = 360 - a;
        int startAngle = (int) Math.round(a);
        return this.turn(startAngle);
    }

    private int turnDown(int start, int timeOffset) {
        //this.lang.nextStep();
        Offset startCoords = this.bags[start].getCenter();
        int ax = 0;
        int ay = this.getRadius();
        int bx = startCoords.getX();
        int by = startCoords.getY();
        int ab = ax * bx + ay * by;
        double la = Math.sqrt(ax * ax + ay * ay);
        double lb = Math.sqrt(bx * bx + by * by);
        double a = Math.toDegrees(Math.acos(ab / (la * lb)));
        if(startCoords.getX() < 0)
            a = 360 - a;
        int startAngle = (int) Math.round(a);
        return this.turn(startAngle, timeOffset);
    }

    private static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }

    private String[] getStringArray() {
        String[] output = new String[this.A.length];
        for(int i = 0; i < this.A.length; i++)
            output[i] = this.getStringForElement(i);

        return output;
    }

    private String getStringForElement(int pos) {
        return this.A[pos].isEmpty() ? "0 x " + Integer.toString(this.S - 1).replaceAll(".", " ") : this.A[pos].length() + " x " + this.A[pos].getFirst() + DialImplementation.getSpace(Integer.toString(this.S).length() - Integer.toString(this.A[pos].getFirst()).length());
    }

    private static String getSpace(int times) {
        String output = "";
        for(int i = 0; i < times; i++)
            output += " ";

        return output;
    }

    private int insertInBag(int K, int duration) {
        CircleProperties insertCircleProperties = new CircleProperties();
        insertCircleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        Circle insertCircle = this.lang.newCircle(new Offset(0, -this.getRadius() - 70, this.center, null), 20, "InsertCircle", null, insertCircleProperties);
        TextProperties insertTextProperties = new TextProperties();
        insertTextProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        Text insertText = this.lang.newText(new Offset(16, 12, insertCircle, null), Integer.toString(K), "InsertText", null, insertTextProperties);
        insertText.moveTo(null, null, new Offset(0, 60, insertText, null), new MsTiming(duration), Timing.VERY_SLOW);
        insertCircle.moveTo(null, null, new Offset(0, 60, insertCircle, null), new MsTiming(duration), Timing.VERY_SLOW);
        insertCircle.hide(new MsTiming(duration + 1000));
        insertText.hide(new MsTiming(duration + 1000));
        return duration + 1000;
    }

    private int removeFromBag(int K, int duration, boolean hide) {
        CircleProperties removeCircleProperties = new CircleProperties();
        removeCircleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        Circle removeCircle = this.lang.newCircle(new Offset(0, this.getRadius() + 20, this.center, null), 20, "RemoveCircle", null, removeCircleProperties);
        TextProperties removeTextPropertoes = new TextProperties();
        removeTextPropertoes.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        Text removeText = this.lang.newText(new Offset(16, 12, removeCircle, null), Integer.toString(K), "RemoveText", null, removeTextPropertoes);
        removeText.moveTo(null, null, new Offset(0, 60, removeText, null), new MsTiming(duration), Timing.VERY_SLOW);
        removeCircle.moveTo(null, null, new Offset(0, 60, removeCircle, null), new MsTiming(duration), Timing.VERY_SLOW);
        if(hide) {
            removeCircle.hide(new MsTiming(duration + 1500));
            removeText.hide(new MsTiming(duration + 1500));
        }
        return duration + 1000;
    }

    private int removeFromBagAndChange(int K, int duration, int KNew, int pos1, int pos2) {
        CircleProperties removeCircleProperties = new CircleProperties();
        removeCircleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        Circle removeCircle = this.lang.newCircle(new Offset(0, this.getRadius() + 20, this.center, null), 20, "RemoveCircle", null, removeCircleProperties);
        TextProperties removeTextPropertoes = new TextProperties();
        removeTextPropertoes.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        Text removeText = this.lang.newText(new Offset(16, 12, removeCircle, null), Integer.toString(K), "RemoveText", null, removeTextPropertoes);
        removeText.moveTo(null, null, new Offset(0, 60, removeText, null), new MsTiming(duration), Timing.VERY_SLOW);
        removeCircle.moveTo(null, null, new Offset(0, 60, removeCircle, null), new MsTiming(duration), Timing.VERY_SLOW);
        removeText.setText(Integer.toString(KNew), new MsTiming(duration + 1000), null);

        if(!this.A[pos1].isEmpty())
            this.bags[pos1].changeText(this.A[pos1].length(), this.A[pos1].getFirst(), duration);
        else
            this.bags[pos1].changeText(0, -1, duration + 1000);
        this.viewArray.put(pos1, this.getStringForElement(pos1), new MsTiming(duration + 1000), null);

        duration = this.turnUp(pos2, duration + 1000);

        ArcProperties moveArcProperties = new ArcProperties();
        moveArcProperties.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 270);
        moveArcProperties.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 180);
        moveArcProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

        Arc moveArc = this.lang.newArc(center, new Coordinates(this.getRadius() + 80, this.getRadius() + 80), "Arc", null, moveArcProperties);

        removeCircle.moveVia(null, null, moveArc, new MsTiming(duration), new MsTiming(2000));
        removeText.moveVia(null, null, moveArc, new MsTiming(duration), new MsTiming(2000));

        removeCircle.moveTo(null, null, new Offset(0, 60, removeCircle, null), new MsTiming(duration + 2000), Timing.VERY_SLOW);
        removeText.moveTo(null, null, new Offset(0, 60, removeText, null), new MsTiming(duration + 2000), Timing.VERY_SLOW);

        this.bags[pos2].changeText(A[pos2].length() + 1, KNew, duration + 3000);
        this.viewArray.put(pos2, (this.A[pos2].length() + 1) + " x " + KNew + DialImplementation.getSpace(Integer.toString(this.S).length() - Integer.toString(KNew).length()), new MsTiming(duration + 3000), null);

        removeCircle.hide(new MsTiming(duration + 3000));
        removeText.hide(new MsTiming(duration + 3000));

        return duration + 3000;
    }

    public void insert(int K) {
        if(K < 0 || K >= S)
            throw new IllegalArgumentException("The key must be in the interval of Zero to " + this.S + ", but was " + K);

        //this.nextMethod.setText("insert(" + K + ")", null, null);
        this.insertSourceCode.show();

        SourceCode insertAnnotation = this.lang.newSourceCode(new Offset(600, -50, this.center, null), "InsertAnnotation", null, this.codeProperties);
        insertAnnotation.addCodeLine("K = " + K, null, 0, null);

        this.lang.nextStep("insert(" + K + ")");

        //insertAnnotation.addCodeLine("pos = (" + K + " - " + (this.A[P].isEmpty() ? K : this.A[P].getFirst()) + " + " + P + ") mod " + S + " = " + (this.A[P].isEmpty() ? P : Math.floorMod(K - this.A[P].getFirst() + P, this.S)), null, 0, null);
        this.insertSourceCode.highlight(1);
        if(this.A[P].isEmpty()) {
            insertAnnotation.addCodeLine("true", null, 0, null);

            this.lang.nextStep();

            this.insertSourceCode.unhighlight(1);
            this.insertSourceCode.highlight(2);

            int duration = this.turnUp(P);
            this.insertInBag(K, duration);
            this.A[P].add(K);
            this.bags[P].changeText(this.A[P].length(), this.A[P].getFirst(), duration);
            this.viewArray.put(P, this.getStringForElement(P), new MsTiming(duration), null);
            insertAnnotation.addCodeLine("", null, 0, null);
            insertAnnotation.addCodeLine("", null, 0, null);
            insertAnnotation.addCodeLine("", null, 0, null);
            insertAnnotation.addCodeLine("", null, 0, null);
            insertAnnotation.addCodeLine("", null, 0, null);
            insertAnnotation.addCodeLine("", null, 0, null);

            this.lang.nextStep();
            this.insertSourceCode.unhighlight(2);
        } else {
            insertAnnotation.addCodeLine("false", null, 0, null);

            int pos = Math.floorMod(K - this.A[P].getFirst() + P, this.S);

            this.lang.nextStep();

            this.insertSourceCode.unhighlight(1);
            this.insertSourceCode.highlight(4);
            insertAnnotation.addCodeLine("", null, 0, null);
            insertAnnotation.addCodeLine("", null, 0, null);
            insertAnnotation.addCodeLine("pos = (" + K + " - " + this.A[P].getFirst() + " + " + P + ") mod " + this.S + " = " + pos, null, 0, null);

            ArrayMarkerProperties arrayMarkerProperties = new ArrayMarkerProperties();
            arrayMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "pos");
            arrayMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
            ArrayMarker posMarker = this.lang.newArrayMarker(this.viewArray, pos, "pos" + pos, null, arrayMarkerProperties);

            this.bags[pos].getOuterCircle().changeColor(null, Color.BLUE, null, null);

            this.lang.nextStep();

            this.insertSourceCode.unhighlight(4);
            this.insertSourceCode.highlight(5);
            insertAnnotation.addCodeLine("", null, 0, null);

            int duration = this.turnUp(pos);
            this.insertInBag(K, duration);
            this.A[pos].add(K);

            this.bags[pos].changeText(this.A[pos].length(), this.A[pos].getFirst(), duration);
            this.viewArray.put(pos, this.getStringForElement(pos), new MsTiming(duration), null);

            this.lang.nextStep();

            this.insertSourceCode.unhighlight(5);
            this.insertSourceCode.highlight(6);

            if(K < this.A[P].getFirst()) {
                insertAnnotation.addCodeLine("true", null, 0, null);

                this.lang.nextStep();

                this.insertSourceCode.unhighlight(6);
                this.insertSourceCode.highlight(7);
                this.bags[P].getOuterCircle().changeColor(null, Color.BLACK, null, null);
                P = Math.floorMod(K - this.A[P].getFirst() + P, this.S);
                insertAnnotation.addCodeLine("P = " + P, null, 0, null);
                this.pText.changeColor(null, Color.BLUE, null, null);
                this.pText.setText("P = " + this.P, null, null);
                this.pMarker.move(pos, null, null);
                this.bags[P].getOuterCircle().changeColor(null, Color.RED, null, null);
                posMarker.hide();

                this.lang.nextStep();

                this.insertSourceCode.unhighlight(7);
                this.pText.changeColor(null, Color.BLACK, null, null);
            } else {
                this.bags[pos].getOuterCircle().changeColor(null, Color.BLACK, null, null);
                insertAnnotation.addCodeLine("false", null, 0, null);
                insertAnnotation.addCodeLine("", null, 0, null);
                posMarker.hide();

                this.lang.nextStep();

                this.insertSourceCode.unhighlight(6);
            }
        }
        this.insertSourceCode.highlight(8);
        this.N++;
        insertAnnotation.addCodeLine("N = " + (this.N - 1) + " + 1 = " + this.N, null, 0, null);
        this.nText.changeColor(null, Color.BLUE, null, null);
        this.nText.setText("N = " + this.N, new MsTiming(1500), null);

        this.lang.nextStep();
        this.nText.changeColor(null, Color.BLACK, null, null);
        this.insertSourceCode.unhighlight(8);
        this.insertSourceCode.hide();
        insertAnnotation.hide();
    }

    public Integer extractMinimum() {
        if(this.A[this.P].isEmpty())
            throw new NoSuchElementException("There is no element stored in the array");

        //this.nextMethod.setText("extractMinimum()", null, null);
        this.extractMinimumSourceCode.show();

        this.lang.nextStep("extractMinimum()");

        Integer output = this.A[this.P].removeFirst();

        this.extractMinimumSourceCode.highlight(1);
        int duration = this.turnDown(P);
        this.removeFromBag(output, duration, true);

        SourceCode extractMinimumAnnotation = this.lang.newSourceCode(new Offset(535, -50, this.center, null), "ExtractMinimumAnnotation", null, codeProperties);
        extractMinimumAnnotation.addCodeLine("", null, 0, null);

        if(!this.A[P].isEmpty())
            this.bags[P].changeText(this.A[P].length(), this.A[P].getFirst(), duration);
        else
            this.bags[P].changeText(0, -1, duration);

        this.viewArray.put(P, this.getStringForElement(P), new MsTiming(duration), null);
        extractMinimumAnnotation.addCodeLine("K = " + output, null, 0, null);

        this.lang.nextStep();

        this.extractMinimumSourceCode.unhighlight(1);
        this.extractMinimumSourceCode.highlight(2);

        TextProperties doStepProperties = new TextProperties();
        doStepProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 15));
        Text doStep = this.lang.newText(new Offset(0, 76, extractMinimumAnnotation, null), "", "DoStep", null, doStepProperties);
        Text whileText = this.lang.newText(new Offset(0, 95, extractMinimumAnnotation, null), "", "WhileText", null, doStepProperties);

        if(N > 1 && this.A[this.P].isEmpty()) {
            extractMinimumAnnotation.addCodeLine("true", null, 0, null);

            this.lang.nextStep();

            this.extractMinimumSourceCode.unhighlight(2);

            extractMinimumAnnotation.addCodeLine("", null, 0, null);
            extractMinimumAnnotation.addCodeLine("", null, 0, null);
            extractMinimumAnnotation.addCodeLine("", null, 0, null);

            do {
                this.extractMinimumSourceCode.highlight(4);

                doStep.setText("P = " + P + " + 1 mod " + S + " = " + (P + 1) % S, null, null);
                this.bags[P].getOuterCircle().changeColor(null, Color.BLACK, new MsTiming(1500), null);
                this.P = (this.P + 1) % this.S;

                this.pMarker.move(P, new MsTiming(1500), null);
                this.pText.changeColor(null, Color.BLUE, null, null);
                this.pText.setText("P = " + P, new MsTiming(1500), null);
                this.bags[P].getOuterCircle().changeColor(null, Color.RED, new MsTiming(1500), null);

                this.lang.nextStep();

                this.pText.changeColor(null, Color.BLACK, null, null);

                this.extractMinimumSourceCode.unhighlight(4);
                this.extractMinimumSourceCode.highlight(5);

                whileText.setText(Boolean.toString(A[P].isEmpty()), null, null);

                this.lang.nextStep();

                whileText.hide();

                this.extractMinimumSourceCode.unhighlight(5);
            } while (this.A[this.P].isEmpty());
        } else {
            extractMinimumAnnotation.addCodeLine("false", null, 0, null);
            extractMinimumAnnotation.addCodeLine("", null, 0, null);
            extractMinimumAnnotation.addCodeLine("", null, 0, null);
            extractMinimumAnnotation.addCodeLine("", null, 0, null);
            this.lang.nextStep();

            this.extractMinimumSourceCode.unhighlight(2);
        }

        this.extractMinimumSourceCode.highlight(6);

        extractMinimumAnnotation.addCodeLine("N = " + this.N + " - 1 = " + (this.N - 1), null, 0, null);
        this.nText.changeColor(null, Color.BLUE, null, null);
        this.N--;
        this.nText.setText("N = " + N, new MsTiming(1500), null);

        this.lang.nextStep();

        this.nText.changeColor(null, Color.BLACK, null, null);
        this.extractMinimumSourceCode.unhighlight(6);
        this.extractMinimumSourceCode.highlight(7);

        extractMinimumAnnotation.addCodeLine("return " + output, null, 0, null);

        this.lang.nextStep();

        doStep.hide();
        whileText.hide();
        this.extractMinimumSourceCode.unhighlight(7);
        this.extractMinimumSourceCode.hide();
        extractMinimumAnnotation.hide();

        return output;
    }

    public void decreaseKey(Integer K, Integer KNew) {
        if(KNew >= K)
            throw new IllegalArgumentException("The new key must be less than the old key (" + K + "), but the new key was " + KNew);
        if(this.A[Math.floorMod(K - this.A[P].getFirst() + P, this.S)].isEmpty())
            throw new IllegalArgumentException("The key does not exists in the array");

        //this.nextMethod.setText("decreaseKey(" + K + ", " + KNew + ")", null, null);
        this.decreaseKeySourceCode.show();

        SourceCode decreaseKeyAnnotation = this.lang.newSourceCode(new Offset(575, -50, this.center, null), "DecreaseKeyAnnotation", null, codeProperties);
        decreaseKeyAnnotation.addCodeLine("K = " + K + ", KNew = " + KNew, null, 0, null);

        this.lang.nextStep("decreaseKey(" + K + ", " + KNew + ")");

        this.decreaseKeySourceCode.highlight(1);

        int pos1 = Math.floorMod(K - this.A[P].getFirst() + P, this.S);

        decreaseKeyAnnotation.addCodeLine("pos1 = (" + K + " - " + A[P].getFirst() + " + " + P + ") mod " + S + " = " + pos1, null, 0, null);
        ArrayMarkerProperties pos1MarkerProperties = new ArrayMarkerProperties();
        pos1MarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "pos1");
        pos1MarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        ArrayMarker pos1Marker = this.lang.newArrayMarker(this.viewArray, pos1, "Pos1Marker", null, pos1MarkerProperties);
        this.bags[pos1].getOuterCircle().changeColor(null, Color.BLUE, null, null);

        this.lang.nextStep();

        this.decreaseKeySourceCode.unhighlight(1);
        this.decreaseKeySourceCode.highlight(2);

        int pos2 = Math.floorMod(KNew - this.A[P].getFirst() + P, this.S);

        decreaseKeyAnnotation.addCodeLine("pos2 = (" + KNew + " - " + A[P].getFirst() + " + " + P + ") mod " + S + " = " + pos2, null, 0, null);
        ArrayMarkerProperties pos2MarkerProperties = new ArrayMarkerProperties();
        pos2MarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "pos2");
        pos2MarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        ArrayMarker pos2Marker = this.lang.newArrayMarker(this.viewArray, pos2, "Pos2Marker", null, pos2MarkerProperties);
        this.bags[pos2].getOuterCircle().changeColor(null, Color.BLUE, null, null);

        this.lang.nextStep();

        this.A[pos1].removeFirst();

        this.decreaseKeySourceCode.unhighlight(2);
        this.decreaseKeySourceCode.highlight(3);
        this.decreaseKeySourceCode.highlight(4);

        int duration = this.turnDown(pos1);
        duration = this.removeFromBagAndChange(K, duration, KNew, pos1, pos2);

        A[pos2].add(KNew);

        this.lang.nextStep();

        decreaseKeyAnnotation.addCodeLine("", null, 0, null);
        decreaseKeyAnnotation.addCodeLine("", null, 0, null);

        this.decreaseKeySourceCode.unhighlight(3);
        this.decreaseKeySourceCode.unhighlight(4);
        this.decreaseKeySourceCode.highlight(5);

        this.bags[pos1].getOuterCircle().changeColor(null, Color.BLACK, null, null);
        this.bags[pos2].getOuterCircle().changeColor(null, Color.BLACK, null, null);
        this.bags[P].getOuterCircle().changeColor(null, Color.RED, null, null);

        if(pos1 == P) {
            decreaseKeyAnnotation.addCodeLine("true", null, 0, null);

            this.lang.nextStep();

            this.bags[P].getOuterCircle().changeColor(null, Color.BLACK, new MsTiming(1500), null);

            P = pos2;

            this.decreaseKeySourceCode.unhighlight(5);
            this.decreaseKeySourceCode.highlight(6);

            decreaseKeyAnnotation.addCodeLine("P = " + P, null, 0, null);
            this.pText.changeColor(null, Color.BLUE, null, null);
            this.bags[P].getOuterCircle().changeColor(null, Color.RED, new MsTiming(1500), null);
            this.pMarker.move(pos2, new MsTiming(1500), null);
            this.pText.setText("P = " + P, new MsTiming(1500), null);

            this.lang.nextStep();

            this.pText.changeColor(null, Color.BLACK, null, null);

            this.decreaseKeySourceCode.unhighlight(6);
        } else {
            decreaseKeyAnnotation.addCodeLine("false", null, 0, null);
            decreaseKeyAnnotation.addCodeLine("", null, 0, null);
            decreaseKeyAnnotation.addCodeLine("", null, 0, null);

            this.lang.nextStep();

            this.decreaseKeySourceCode.unhighlight(5);
            this.decreaseKeySourceCode.highlight(8);

            if(KNew < A[P].getFirst()) {
                decreaseKeyAnnotation.addCodeLine("true", null, 0, null);

                this.lang.nextStep();

                this.decreaseKeySourceCode.unhighlight(8);
                this.decreaseKeySourceCode.highlight(9);

                this.bags[P].getOuterCircle().changeColor(null, Color.BLACK, new MsTiming(1500), null);

                P = pos2;

                decreaseKeyAnnotation.addCodeLine("P = " + P, null, 0, null);
                this.pText.changeColor(null, Color.BLUE, null, null);
                this.bags[P].getOuterCircle().changeColor(null, Color.RED, new MsTiming(1500), null);
                this.pMarker.move(pos2, new MsTiming(1500), null);
                this.pText.setText("P = " + P, new MsTiming(1500), null);

                this.lang.nextStep();

                this.pText.changeColor(null, Color.BLACK, null, null);

                this.decreaseKeySourceCode.unhighlight(9);
            } else {
                decreaseKeyAnnotation.addCodeLine("false", null, 0, null);

                this.lang.nextStep();

                this.decreaseKeySourceCode.unhighlight(6);
            }
        }

        pos1Marker.hide();
        pos2Marker.hide();
        this.decreaseKeySourceCode.hide();
        decreaseKeyAnnotation.hide();
    }

    public void insertPure(int K) {
        if(K < 0 || K >= S)
            throw new IllegalArgumentException("The key must be in the interval of Zero to " + this.S + ", but was " + K);

        if(this.A[P].isEmpty())
            this.A[P].add(K);
        else {
            int pos = Math.floorMod(K - this.A[P].getFirst() + P, this.S);
            this.A[pos].add(K);
            if(K < this.A[P].getFirst())
                P = Math.floorMod(K - this.A[P].getFirst() + P, this.S);
        }
        this.N++;
    }

    public Integer findMinimum() {
        if(this.A[this.P].isEmpty())
            throw new NoSuchElementException("There is no element stored in the array");

        return this.A[this.P].getFirst();
    }

    public int number() {
        //this.nextMethod.setText("number()", null, null);
        this.numberSourceCode.show();

        this.lang.nextStep("number()");

        SourceCode numberAnnotation = this.lang.newSourceCode(new Offset(450, -50, this.center, null), "NumberAnnotation", null, codeProperties);
        numberAnnotation.addCodeLine(" ", null, 0, null);
        numberAnnotation.addCodeLine("return " + this.N, null, 0, null);

        this.nText.changeColor(null, Color.BLUE, null, null);
        this.numberSourceCode.highlight(1);

        this.lang.nextStep();
        this.nText.changeColor(null, Color.BLACK, null, null);
        this.numberSourceCode.unhighlight(1);
        this.numberSourceCode.hide();
        numberAnnotation.hide();

        return this.N;
    }

    public DialImplementationIntegerList[] getA() {
        return this.A;
    }

    public String toString() {
        return "DialImplementation(0 - " + (this.S - 1) + "): Minimum at position " + this.P + "; Array = " + Arrays.deepToString(this.A);
    }

    /*public static void main(String[] args) {
        Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Dial-Implementation", "Benedikt Lins, Stefan Thaut", 640, 480);
        DialImplementation dial = new DialImplementation(15, l);
        dial.randomMethodSelection(500);
        //dial.insert(3);
        System.out.println(l);
    }*/
}
