package generators.misc.birch;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.helpers.Pair;
import generators.misc.birch.elements.*;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Birch {

    class SplitResult {
        CFTreeNode firstTN;
        CFTreeNode secondTN;
        ClusterFeature firstCF; // Key
        ClusterFeature secondCF; // Key
        StringArray firstArray, secondArray;
        Text firstText, secondText;

        SplitResult(CFTreeNode firstTN, CFTreeNode secondTN, ClusterFeature firstCF, ClusterFeature secondCF,
                    StringArray firstArray, StringArray secondArray, Text firstText, Text secondText) {
            this.firstCF = firstCF;
            this.secondCF = secondCF;
            this.firstTN = firstTN;
            this.secondTN = secondTN;
            this.firstArray = firstArray;
            this.secondArray = secondArray;
            this.firstText = firstText;
            this.secondText = secondText;
        }
    }

    private Language lang;
    private Translator translator;

    private TextProperties headlineProperties;
    private RectProperties headlineRectProperties;
    private TextProperties textProperties;
    private TextProperties smallTextProperties;
    private TextProperties subheadlineProperties;
    private SourceCodeProperties pseudocodeProperties;
    private ArrayProperties nodeProperties;
    private PolylineProperties arrowProperties;
    private MatrixProperties tableProperties;
    private ArrayProperties splitArrayProperties;

    private Color coordinateSystemHighlightColor;

    private int BL;
    private float threshold;

    private ClusterFeature[] initialClusterFeatures;
    private ClusterFeature[] insertClusterFeatures;
    private ClusterFeature currentClusterFeature, currentCFInsertedInto;

    private NumberedSourceCode insertCFPseudocode;
    private NumberedSourceCode insertCFIntoLeafPseudocode;
    private NumberedSourceCode insertCFIntoNodePseudocode;
    private NumberedSourceCode splitNodePseudocode;

    private CoordinateSystem coordinateSystem;
    private StringMatrix table;
    private LinkedList<Primitive> currentTree = new LinkedList<>();

    private int tableRowCount;

    private CFTree tree, finalTree;
    private Offset treeUpperLeft;

    private Map<String, CFGeometrics> cfGeometricsMap = new HashMap<>();

    private int nextQuestionID = 0;
    private Map<String, Integer> questionsByGroup = new HashMap<>();

    public Birch(
            Language lang,
            int BL,
            float threshold,
            ClusterFeature[] initialClusterFeatures,
            ClusterFeature[] insertClusterFeatures,
            Translator translator
    ) {
        this.lang = lang;
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        this.BL = BL;
        this.threshold = threshold;
        this.initialClusterFeatures = initialClusterFeatures;
        this.insertClusterFeatures = insertClusterFeatures;

        this.tree = new CFTree(BL, threshold);
        this.finalTree = new CFTree(BL, threshold);

        Arrays.stream(initialClusterFeatures).forEach(cf -> {
            tree.insert(cf);
            finalTree.insert(cf);
        });
        Arrays.stream(insertClusterFeatures).forEach(cf -> finalTree.insert(cf));

        this.translator = translator;
    }

    public void initializeProperties(
            MatrixProperties table,
            RectProperties rect,
            ArrayProperties tree,
            TextProperties text,
            SourceCodeProperties pseudocode,
            SourceCodeProperties coordinateSystem
    ) {
        String font = ((Font) text.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName();
        String pseudocodeFont = ((Font) pseudocode.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName();

        coordinateSystemHighlightColor = (Color) coordinateSystem.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);

        headlineProperties = new TextProperties();
        headlineProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.BOLD, 25));
        headlineProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

        headlineRectProperties = new RectProperties();
        headlineRectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        headlineRectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
                rect.get(AnimationPropertiesKeys.FILL_PROPERTY));
        headlineRectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        textProperties = new TextProperties();
        textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.PLAIN, 20));

        smallTextProperties = new TextProperties();
        smallTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.PLAIN, 15));

        subheadlineProperties = new TextProperties();
        subheadlineProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.BOLD, 22));

        pseudocodeProperties = new SourceCodeProperties();
        pseudocodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(pseudocodeFont, Font.PLAIN, 15));
        pseudocodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
                pseudocode.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));

        nodeProperties = new ArrayProperties();
        nodeProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        nodeProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
                tree.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));

        arrowProperties = new PolylineProperties();
        arrowProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

        tableProperties = new MatrixProperties();
        tableProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        tableProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.LIGHT_GRAY);
        tableProperties.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, Color.BLACK);
        tableProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
                table.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));

        splitArrayProperties = new ArrayProperties();
        splitArrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    }

    public void doAnimation() {
        Rect headlineRect = doHeadline(new Coordinates(20, 30));

        doIntroduction(new Offset(0, 20, headlineRect, AnimalScript.DIRECTION_SW));

        Text headlineCoord = doCoordinateSystem(new Offset(0, 20, headlineRect, AnimalScript.DIRECTION_SW));

        lang.nextStep();

        Text headlinePseudocode = doPseudocode(new Offset(470, 0, headlineCoord, AnimalScript.DIRECTION_NW));

        lang.nextStep();

        Text treeHeadline = doTreeHeadline(new Offset(400, 0, headlinePseudocode, AnimalScript.DIRECTION_NE));
        int width = doTree();

        lang.nextStep();

        doTable(new Offset(0, finalTree.getRoot().getHeight() * 50, treeHeadline, AnimalScript.DIRECTION_SW));
        updateTable();

        lang.nextStep(translator.translateMessage("begin_of_algorithm"));
        insertCFPseudocode.hide();
        for (int i = 0; i < insertClusterFeatures.length; i++) {
            currentClusterFeature = insertClusterFeatures[i];
            doNextInsertion(new Offset(0, 0, headlinePseudocode, AnimalScript.DIRECTION_SW));
        }

        headlinePseudocode.hide();
        lang.nextStep();

        showAllCFsColored(tree.getRoot(), 1);
        lang.nextStep(translator.translateMessage("end"));

        doEnd(new Offset(470, 0, headlineCoord, AnimalScript.DIRECTION_NW));

        lang.finalizeGeneration();
    }

    private Rect doHeadline(Node node) {
        Text headline = lang.newText(node, translator.translateMessage("title"), "headline", null, headlineProperties);

        return lang.newRect(new Offset(-5, -5, headline, AnimalScript.DIRECTION_NW),
                new Offset(5, 5, headline, AnimalScript.DIRECTION_SE), "hRect",
                null, headlineRectProperties);
    }

    private void doIntroduction(Node node) {
        MultilineText text = new MultilineText(translator.translateMessage("introduction1"), lang, textProperties, node);

        lang.nextStep(translator.translateMessage("introduction"));

        text.hide();
        text = new MultilineText(translator.translateMessage("introduction2"), lang, textProperties, node);

        lang.nextStep();

        Text pseudocodeTitle = lang.newText(OffsetHelper.offsetOf(text.getLowerLeft(), 0, 25),
                translator.translateMessage("pseudocode") + ":", "pseudocodeTitle", null, subheadlineProperties);
        NumberedSourceCode pseudocode1 = doInsertCFPseudocode(new Offset(0, 0, pseudocodeTitle, AnimalScript.DIRECTION_SW));
        NumberedSourceCode pseudocode2 = doInsertCFIntoNodePseudocode(OffsetHelper.offsetOf(pseudocode1.getUpperRight(), 30, 0));
        NumberedSourceCode pseudocode3 = doInsertCFIntoLeafPseudocode(OffsetHelper.offsetOf(pseudocode2.getUpperRight(), 10, 0));
        NumberedSourceCode pseudocode4 = doSplitNodePseudocode(OffsetHelper.offsetOf(pseudocode3.getLowerLeft(), 0, 10));

        lang.nextStep(translator.translateMessage("pseudocode"));

        Text pseudocodeExplanation = lang.newText(OffsetHelper.offsetOf(pseudocode1.getLowerLeft(), 0, 75),
                translator.translateMessage("introduction3"), "pseudocodeExplanation", null, textProperties);

        lang.nextStep();

        text.hide();
        pseudocodeTitle.hide();
        pseudocode1.hide();
        pseudocode2.hide();
        pseudocode3.hide();
        pseudocode4.hide();
        pseudocodeExplanation.hide();
    }

    private NumberedSourceCode doInsertCFPseudocode(Node node) {
        NumberedSourceCode pseudocode = new NumberedSourceCode(node, pseudocodeProperties, lang, 1);

        pseudocode.addCodeLine("insertCF(cf)", 0);
        pseudocode.addCodeLine("if root is NIL", 2);
        pseudocode.addCodeLine("root <- new Node()", 4);
        pseudocode.addCodeLine("root.add(cf)", 4);
        pseudocode.addCodeLine("return", 4);
        pseudocode.addCodeLine("endif", 2);
        pseudocode.addCodeLine("insertCFIntoNode(cf, root)", 2);
        pseudocode.addCodeLine("if root.getCount() > BL", 2);
        pseudocode.addCodeLine("[first, second] <- splitNode(root)", 4);
        pseudocode.addCodeLine("root <- new Node()", 4);
        pseudocode.addCodeLine("root.add(first, second)", 4);
        pseudocode.addCodeLine("endif", 2);

        return pseudocode;
    }

    private NumberedSourceCode doInsertCFIntoNodePseudocode(Node node) {
        NumberedSourceCode pseudocode = new NumberedSourceCode(node, pseudocodeProperties, lang, 13);

        pseudocode.addCodeLine("insertCFIntoNode(cf, node)", 0);
        pseudocode.addCodeLine("if node.isLeaf", 2);
        pseudocode.addCodeLine("insertCFIntoLeaf(cf, node)", 4);
        pseudocode.addCodeLine("else", 2);
        pseudocode.addCodeLine("nearestCF <- node.findNearestCF(cf)", 4);
        pseudocode.addCodeLine("insertCFIntoNode(cf, nearestCF.node)", 4);
        pseudocode.addCodeLine("if nearestCF.node.getCount() > BL", 4);
        pseudocode.addCodeLine("[first, second] <- splitNode(nearestCF.node)", 6);
        pseudocode.addCodeLine("node.add(first, second)", 6);
        pseudocode.addCodeLine("node.remove(nearestCF)", 6);
        pseudocode.addCodeLine("else", 4);
        pseudocode.addCodeLine("recalculate(nearestCF)", 6);
        pseudocode.addCodeLine("endif", 4);
        pseudocode.addCodeLine("endif", 2);

        return pseudocode;
    }

    private NumberedSourceCode doInsertCFIntoLeafPseudocode(Node node) {
        NumberedSourceCode pseudocode = new NumberedSourceCode(node, pseudocodeProperties, lang, 27);

        pseudocode.addCodeLine("insertCFIntoLeaf(cf, leaf)", 0);
        pseudocode.addCodeLine("nearestCF <- leaf.findNearestCF(cf)", 2);
        pseudocode.addCodeLine("newCF <- nearestCF.add(cf)", 2);
        pseudocode.addCodeLine("if treshold < newCF.getRadius()", 2);
        pseudocode.addCodeLine("nearestCF <- newCF", 4);
        pseudocode.addCodeLine("else", 2);
        pseudocode.addCodeLine("leaf.add(cf)", 4);
        pseudocode.addCodeLine("endif", 2);

        return pseudocode;
    }

    private NumberedSourceCode doSplitNodePseudocode(Node node) {
        NumberedSourceCode pseudocode = new NumberedSourceCode(node, pseudocodeProperties, lang, 35);

        pseudocode.addCodeLine("[first, second] splitNode(node)", 0);
        pseudocode.addCodeLine("[cf1, cf2] <- CFs that are most far away", 2);
        pseudocode.addCodeLine("[[cf1, ...], [cf2, ...]] <-", 2);
        pseudocode.addCodeLine("put remaining CFs in nearest node", 4);
        pseudocode.addCodeLine("first <- [cf1, ...]", 2);
        pseudocode.addCodeLine("second <- [cf2, ...]", 2);

        return pseudocode;
    }

    private Text doPseudocode(Node node) {
        Text pseudocodeHeadline = lang.newText(node, translator.translateMessage("pseudocode") + ":", "pseudocode", null, subheadlineProperties);

        Offset pos = new Offset(0, 0, pseudocodeHeadline, AnimalScript.DIRECTION_SW);

        insertCFPseudocode = doInsertCFPseudocode(pos);
        insertCFIntoNodePseudocode = doInsertCFIntoNodePseudocode(pos);
        insertCFIntoLeafPseudocode = doInsertCFIntoLeafPseudocode(pos);
        splitNodePseudocode = doSplitNodePseudocode(pos);

        insertCFIntoNodePseudocode.hide();
        insertCFIntoLeafPseudocode.hide();
        splitNodePseudocode.hide();

        return pseudocodeHeadline;
    }

    private Text doTreeHeadline(Node node) {
        Text treeHeadline = lang.newText(node, translator.translateMessage("cf_tree") + ":", "treeHeadline", null, subheadlineProperties);
        treeUpperLeft = new Offset(0, 0, treeHeadline, AnimalScript.DIRECTION_SW);
        return treeHeadline;
    }

    private int doTree() {
        CFTreeNode root = tree.getRoot();

        int maxLeafs = (int) Math.pow(BL, finalTree.getRoot().getHeight() - 1);

        currentTree.forEach(p -> p.hide());
        currentTree = new LinkedList<>();

        if (root != null) {
            visualizeNode(root, treeUpperLeft, 0, maxLeafs * 50 * BL, 5);
        }

        return maxLeafs * 50 * BL;
    }

    private Primitive visualizeNode(CFTreeNode node, Offset offset, int xStart, int xEnd, int y) {
        int boxMargin = 5;
        int charWidth = 5;

        int xMid = (xEnd + xStart) / 2;
        int width = 0;
        ClusterFeature[] keys = node.getKeys();
        for (int i = 0; i < node.getCount(); i++) {
            width += (keys[i].getName().length() * charWidth) / 2;
            width += boxMargin;
        }
        xMid -= width;

        String[] cfStrings = new String[node.getCount()];
        ClusterFeature[] cfs = node.getKeys();
        for (int i = 0; i < node.getCount(); i++) {
            cfStrings[i] = cfs[i].getName();
        }

        StringArray sa = lang.newStringArray(OffsetHelper.offsetOf(offset, xMid, y), cfStrings, "node", null, nodeProperties);
        sa.showIndices(false, null, null);
        node.setRepresentationTreeNode(sa);

        if (!node.isLeaf()) {
            // Rekursiver Aufruf
            y += 50;
            CFTreeNode[] children = node.getChildren();
            int laenge = (xEnd - xStart) / BL;
            int newXStart;
            int newXEnd;

            int xOffset = 0;

            for (int i = 0; i < node.getCount(); i++) {
                newXStart = xStart + laenge * i;
                newXEnd = xStart + laenge * (i + 1);

                Primitive childReference = visualizeNode(children[i], offset, newXStart, newXEnd, y);

                int keyLength = keys[i].getName().length() * charWidth;
                xOffset += boxMargin + keyLength / 2;

                Polyline polyline = lang.newPolyline(new Node[]{
                        new Offset(xOffset, 0, sa, AnimalScript.DIRECTION_SW),
                        new Offset(0, 0, childReference, AnimalScript.DIRECTION_N)
                }, "edge", null, arrowProperties);

                currentTree.add(polyline);

                xOffset += keyLength / 2 + 2 * boxMargin;
            }
        }

        currentTree.add(sa);

        return sa;
    }

    private Text doCoordinateSystem(Node node) {
        Text coordHeadline = lang.newText(node, translator.translateMessage("coordinate_system") + ":", "coords", null, subheadlineProperties);

        double xInitialMax = Arrays.stream(initialClusterFeatures).mapToDouble(cf -> cf.getCentroid().getX()).max().orElse(0);
        double yInitialMax = Arrays.stream(initialClusterFeatures).mapToDouble(cf -> cf.getCentroid().getY()).max().orElse(0);
        double xInsertMax = Arrays.stream(insertClusterFeatures).mapToDouble(cf -> cf.getCentroid().getX()).max().orElse(0);
        double yInsertMax = Arrays.stream(insertClusterFeatures).mapToDouble(cf -> cf.getCentroid().getY()).max().orElse(0);
        int xMax = (int) Math.max(Math.ceil(xInitialMax), Math.ceil(xInsertMax));
        int yMax = (int) Math.max(Math.ceil(yInitialMax), Math.ceil(yInsertMax));

        double xInitialMin = Arrays.stream(initialClusterFeatures).mapToDouble(cf -> cf.getCentroid().getX()).min().orElse(Double.MAX_VALUE);
        double yInitialMin = Arrays.stream(initialClusterFeatures).mapToDouble(cf -> cf.getCentroid().getY()).min().orElse(Double.MAX_VALUE);
        double xInsertMin = Arrays.stream(insertClusterFeatures).mapToDouble(cf -> cf.getCentroid().getX()).min().orElse(Double.MAX_VALUE);
        double yInsertMin = Arrays.stream(insertClusterFeatures).mapToDouble(cf -> cf.getCentroid().getY()).min().orElse(Double.MAX_VALUE);
        int xMin = (int) Math.min(Math.ceil(xInitialMin), Math.ceil(xInsertMin));
        int yMin = (int) Math.min(Math.ceil(yInitialMin), Math.ceil(yInsertMin));
        if (yMin > 0)
            yMin--;

        int maxDiff = Math.max(xMax - xMin, yMax - yMin);
        xMax = xMin + maxDiff;
        yMax = yMin + maxDiff;

        int xStep = (int) Math.ceil((xMax - xMin) / 15f);
        int yStep = (int) Math.ceil((yMax - yMin) / 15f);
        coordinateSystem = new CoordinateSystem(lang,
                new Offset(0, 0, coordHeadline, AnimalScript.DIRECTION_SW), 450, 450,
                xMax, yMax, xMin, yMin, xStep, yStep);

        CFTreeNode root = tree.getRoot();

        Map<String, ClusterFeature> cfMap = new HashMap<>();
        collectCFs(root, cfMap);
        cfMap.forEach((name, cf) -> cfGeometricsMap.put(cf.getName(), CFGeometrics.create(coordinateSystem, cf)));

        if (root != null) {
            for (ClusterFeature cf : root.getKeys()) {
                if (cf != null) {
                    cfGeometricsMap.get(cf.getName()).showBasics();
                } else {
                    break;
                }
            }
        }

        return coordHeadline;
    }

    private NamedLine drawThresholdLine(float x, float y) {
        float thresholdCoordinate = (float) (threshold * Math.sin(Math.PI / 4));
        return NamedLine.create(coordinateSystem, x, y, x + thresholdCoordinate, y - thresholdCoordinate, "t");
    }

    private void doTable(Node node) {
        Map<String, ClusterFeature> finalCFMap = new HashMap<>();
        collectCFs(finalTree.getRoot(), finalCFMap);

        Text tableHeadline = lang.newText(node, translator.translateMessage("cf_values") + ":", "tableHeadline", null, subheadlineProperties);

        tableRowCount = finalCFMap.size() + 2;

        String[][] data = new String[tableRowCount][4];
        data[0] = new String[]{translator.translateMessage("name"), "N", "LS", "SS"};
        for (int i = 1; i < finalCFMap.size() + 2; i++) {
            data[i] = new String[]{"", "", "", ""};
        }
        table = lang.newStringMatrix(new Offset(20, 20, tableHeadline, AnimalScript.DIRECTION_SW), data, "table", null, tableProperties);

        table.highlightCellColumnRange(0, 0, 3, null, null);
    }

    private void updateTable() {
        SortedMap<String, ClusterFeature> cfMap = new TreeMap<>();
        collectCFs(tree.getRoot(), cfMap);
        int i = 1;
        ClusterFeature cf;
        table.unhighlightElemColumnRange(1, 0, 1, null, null);

        if (currentClusterFeature != null) {
            table.put(1, 0, currentClusterFeature.getName(), null, null);
            table.put(1, 1, currentClusterFeature.getN() + "", null, null);
            table.put(1, 2, currentClusterFeature.getLS() + "", null, null);
            table.put(1, 3, currentClusterFeature.getSS() + "", null, null);
            i = 2;
            table.highlightElemColumnRange(1, 0, 1, null, null);
        }

        for (Map.Entry<String, ClusterFeature> entry : cfMap.entrySet()) {
            cf = entry.getValue();
            table.put(i, 0, entry.getKey(), null, null);
            table.put(i, 1, cf.getN() + "", null, null);
            table.put(i, 2, cf.getLS().toString(), null, null);
            table.put(i, 3, cf.getSS().toString(), null, null);
            i++;
        }

        for (; i < tableRowCount; i++) {
            table.put(i, 0, "", null, null);
            table.put(i, 1, "", null, null);
            table.put(i, 2, "", null, null);
            table.put(i, 3, "", null, null);
        }
    }

    private void collectCFs(CFTreeNode node, Map<String, ClusterFeature> cfMap) {
        if (node == null) {
            return;
        }

        int count = node.getCount();
        ClusterFeature[] keys = node.getKeys();
        CFTreeNode[] children = node.getChildren();
        for (int i = 0; i < count; i++) {
            cfMap.put(keys[i].getName(), keys[i]);
            collectCFs(children[i], cfMap);
        }
    }

    private void doEnd(Node node) {
        Text resultHeadline = lang.newText(node, translator.translateMessage("conclusion") + ":", "fazit", null, subheadlineProperties);

        MultilineText resultation = new MultilineText(translator.translateMessage("conclusion1"), lang, smallTextProperties, new Offset(0, 20, resultHeadline, AnimalScript.DIRECTION_SW));

        lang.nextStep();
        Text overviewHeadline = lang.newText(OffsetHelper.offsetOf(resultation.getLowerLeft(), 0, 20), "Überblick über die verwendeten Formeln:", "overwiew", null, subheadlineProperties);
        String text2 = translator.translateMessage("centroid") + ":\n"
                + "\tc = LS/N\n"
                + translator.translateMessage("radius") + ":\n"
                + "\tr^2 = SS/N - (LS/N)^2\n"
                + translator.translateMessage("distance") + ":\n"
                + "\t" + translator.translateMessage("euclidic_distance_description");
        MultilineText overview = new MultilineText(text2, lang, smallTextProperties, new Offset(0, 20, overviewHeadline, AnimalScript.DIRECTION_SW));
    }

    private void doNextInsertion(Node node) {
        NumberedSourceCode pseudocode = new NumberedSourceCode(node, pseudocodeProperties, lang, 0);

        pseudocode.addCodeLine("insertCF(" + currentClusterFeature.getName() + ")", 0);

        CFGeometrics cfGeometrics = CFGeometrics.create(coordinateSystem, currentClusterFeature);
        cfGeometricsMap.put(currentClusterFeature.getName(), cfGeometrics);
        cfGeometrics.showBasics();

        updateTable();

        lang.nextStep(translator.translateMessage("inserting_node") + " '" + currentClusterFeature.getName() + "'");

        cfGeometrics.changeColorBasics(coordinateSystemHighlightColor);
        pseudocode.highlight(0);

        lang.nextStep();

        pseudocode.hide();
        insert(currentClusterFeature);

        pseudocode.show();

        lang.nextStep();

        pseudocode.unhighlight(0);
        if (currentCFInsertedInto == null)
            cfGeometrics.changeColorBasics(Color.BLACK);
        else {
            cfGeometricsMap.get(currentCFInsertedInto.getName()).hideBasics();
            currentCFInsertedInto = null;
        }

        lang.nextStep();
        cfGeometrics.hideBasics();
        pseudocode.hide();
    }

    private void insert(ClusterFeature cf) {
        insertCFPseudocode.show();
        insertCFPseudocode.highlight(1);

        CFTreeNode root = tree.getRoot();

        lang.nextStep();
        insertCFPseudocode.highlight(2);
        insertCFPseudocode.unhighlight(1);

        lang.nextStep();
        insertCFPseudocode.unhighlight(2);

        if (root == null) {
            insertCFPseudocode.highlight(3);
            insertCFPseudocode.highlight(4);

            root = new CFTreeNode(BL, true);
            root.addKey(cf);
            tree.setRoot(root);

            currentClusterFeature = null;
            doTree();
            updateTable();
            root.highlight();

            lang.nextStep();
            insertCFPseudocode.unhighlight(3);
            insertCFPseudocode.unhighlight(4);
            insertCFPseudocode.highlight(5);

            lang.nextStep();
            insertCFPseudocode.unhighlight(5);
            insertCFPseudocode.hide();
            root.unhighlight();

            return;
        }

        insertCFPseudocode.highlight(6);

        lang.nextStep();
        insertCFPseudocode.unhighlight(6);
        insertCFPseudocode.highlight(7);

        lang.nextStep();
        insertCFPseudocode.hide();
        insertIntoNode(cf, root);
        root.highlight();
        insertCFPseudocode.show();

        lang.nextStep();
        insertCFPseudocode.unhighlight(7);
        insertCFPseudocode.highlight(8);

        lang.nextStep();
        insertCFPseudocode.unhighlight(8);

        if (root.getCount() > BL) {
            insertCFPseudocode.highlight(9);

            lang.nextStep();
            insertCFPseudocode.hide();
            SplitResult result = splitNode(root, null);

            insertCFPseudocode.show();

            lang.nextStep();
            insertCFPseudocode.highlight(10);
            insertCFPseudocode.highlight(11);
            insertCFPseudocode.unhighlight(9);

            root = new CFTreeNode(BL, false);
            tree.setRoot(root);

            root.addKey(result.firstCF, result.firstTN);
            root.addKey(result.secondCF, result.secondTN);
            doTree();
            updateTable();
            root.highlight();

            lang.nextStep();
            insertCFPseudocode.unhighlight(10);
            insertCFPseudocode.unhighlight(11);
            result.firstArray.hide();
            result.secondArray.hide();
            result.firstText.hide();
            result.secondText.hide();
        }

        insertCFPseudocode.highlight(12);

        lang.nextStep();
        insertCFPseudocode.unhighlight(12);
        insertCFPseudocode.hide();
        root.unhighlight();
    }

    /**
     * Precondition: currentNode.getCount() must be less than BL
     *
     * @param cf
     * @param currentNode
     */
    private void insertIntoNode(ClusterFeature cf, CFTreeNode currentNode) {
        insertCFIntoNodePseudocode.show();
        insertCFIntoNodePseudocode.highlight(13);
        currentNode.highlight();

        List<ClusterFeature> hideAtTheEnd = new LinkedList<>();
        Arrays.stream(currentNode.getKeys()).forEach(k -> {
            if (k != null) {
                hideAtTheEnd.add(k);
                cfGeometricsMap.get(k.getName()).showBasics();
            }
        });

        lang.nextStep();
        insertCFIntoNodePseudocode.unhighlight(13);
        insertCFIntoNodePseudocode.highlight(14);

        lang.nextStep();
        insertCFIntoNodePseudocode.unhighlight(14);
        if (currentNode.isLeaf()) {
            insertCFIntoNodePseudocode.highlight(15);

            lang.nextStep();
            insertCFIntoNodePseudocode.hide();
            currentNode.unhighlight();

            insertIntoLeaf(cf, currentNode);

            insertCFIntoNodePseudocode.show();
            currentNode.highlight();

            lang.nextStep();
            insertCFIntoNodePseudocode.unhighlight(15);
        } else {
            insertCFIntoNodePseudocode.highlight(16);

            lang.nextStep();
            insertCFIntoNodePseudocode.unhighlight(16);
            insertCFIntoNodePseudocode.highlight(17);

            ClusterFeature[] keys = currentNode.getKeys();

            int nearestIdx = currentNode.findNearest(cf);
            ClusterFeature oldNearest = keys[nearestIdx];

            CFTreeNode child = currentNode.getChildren()[nearestIdx];

            int childCountPre = child.getCount();

            NamedLine[] namedLines = visualizeDistancesFromCF(currentNode, cf);

            lang.nextStep();
            namedLines[nearestIdx].changeColor(coordinateSystemHighlightColor);

            lang.nextStep();
            insertCFIntoNodePseudocode.unhighlight(17);
            insertCFIntoNodePseudocode.highlight(18);
            Arrays.stream(namedLines).forEach(NamedLine::hide);
            hideAtTheEnd.removeIf(clusterFeature -> {
                boolean b = !clusterFeature.getName().equals(keys[nearestIdx].getName());
                if (b) cfGeometricsMap.get(clusterFeature.getName()).hideBasics();
                return b;
            });

            lang.nextStep();
            hideAtTheEnd.forEach(clusterFeature -> cfGeometricsMap.get(clusterFeature.getName()).hideBasics());
            hideAtTheEnd.clear();
            insertCFIntoNodePseudocode.unhighlight(18);
            insertCFIntoNodePseudocode.hide();
            currentNode.unhighlight();

            insertIntoNode(cf, child);

            currentNode.highlight();
            insertCFIntoNodePseudocode.show();
            insertCFIntoNodePseudocode.highlight(18);

            lang.nextStep();
            insertCFIntoNodePseudocode.unhighlight(18);
            insertCFIntoNodePseudocode.highlight(19);

            lang.nextStep();
            insertCFIntoNodePseudocode.unhighlight(19);

            if (child.getCount() > BL) {
                insertCFIntoNodePseudocode.highlight(20);

                lang.nextStep();
                insertCFIntoNodePseudocode.hide();
                SplitResult result = splitNode(child, keys[nearestIdx].getName());
                insertCFIntoNodePseudocode.show();

                lang.nextStep();
                insertCFIntoNodePseudocode.unhighlight(20);
                insertCFIntoNodePseudocode.highlight(21);
                insertCFIntoNodePseudocode.highlight(22);
                currentNode.addKey(result.firstCF, result.firstTN);
                keys[nearestIdx] = result.secondCF;
                currentNode.getChildren()[nearestIdx] = result.secondTN;
                doTree();
                updateTable();
                currentNode.highlight();

                lang.nextStep();
                doRandomShouldSplitNodeQuestion("shouldSplitNode", 2, currentNode.getCount() > BL);

                insertCFIntoNodePseudocode.unhighlight(21);
                insertCFIntoNodePseudocode.unhighlight(22);
                result.secondText.hide();
                result.secondArray.hide();
                result.firstText.hide();
                result.firstArray.hide();
            } else {
                insertCFIntoNodePseudocode.highlight(23);

                lang.nextStep();
                insertCFIntoNodePseudocode.unhighlight(23);
                insertCFIntoNodePseudocode.highlight(24);
                if (child.getCount() == 1)
                    keys[nearestIdx] = keys[nearestIdx].addClusterFeature(cf, child.getKeys()[0].getName());
                else if (child.getCount() == 2 && childCountPre == 1)
                    keys[nearestIdx] = keys[nearestIdx].addClusterFeature(cf, tree.getNameProvider().useName());
                else
                    keys[nearestIdx] = keys[nearestIdx].addClusterFeature(cf, keys[nearestIdx].getName());

                //recalculate text
                String nearestName = oldNearest.getName();
                String cfName = cf.getName();

                String text = "newCF =" + nearestName + " + " + cfName + "\n"
                        + cfName + " = " + cf.toStringWithoutName() + "\n"
                        + nearestName + " = " + oldNearest.toStringWithoutName() + "\n";

                String text2 = "newCF " + translator.translateMessage("calculation") + ": \n"
                        + "\t N = " + nearestName + ".N + " + cfName + ".N = " + oldNearest.getN() + " + " + cf.getN() + "\n"
                        + "\t\t = " + keys[nearestIdx].getN() + "\n"
                        + "\t LS = " + nearestName + ".LS + " + cfName + ".LS = " + oldNearest.getLS() + " + " + cf.getLS() + "\n"
                        + "\t\t = " + keys[nearestIdx].getLS() + "\n"
                        + "\t SS = " + nearestName + ".SS + " + cfName + ".SS = " + oldNearest.getSS() + " + " + cf.getSS() + "\n"
                        + "\t\t = " + keys[nearestIdx].getSS();

                MultilineText recalculateText = new MultilineText(text, lang, smallTextProperties, OffsetHelper.offsetOf(insertCFIntoNodePseudocode.getLowerLeft(), 0, 20));

                lang.nextStep();
                MultilineText recalculateText2 = new MultilineText(text2, lang, smallTextProperties, OffsetHelper.offsetOf(recalculateText.getLowerLeft(), 0, 20));


                lang.nextStep();
                if (currentCFInsertedInto == null || !keys[nearestIdx].getName().equals(currentCFInsertedInto.getName()))
                    cfGeometricsMap.put(keys[nearestIdx].getName(), CFGeometrics.create(coordinateSystem, keys[nearestIdx]));

                Text result = lang.newText(OffsetHelper.offsetOf(recalculateText2.getLowerLeft(), 0, 20),
                        keys[nearestIdx].getName() + " = newCF", "result", null, smallTextProperties);

                doTree();
                updateTable();
                currentNode.highlight();


                lang.nextStep();
                recalculateText.hide();
                recalculateText2.hide();
                result.hide();
                insertCFIntoNodePseudocode.unhighlight(24);
            }
            insertCFIntoNodePseudocode.highlight(25);

            lang.nextStep();
            insertCFIntoNodePseudocode.unhighlight(25);
        }
        insertCFIntoNodePseudocode.highlight(26);

        lang.nextStep();
        currentNode.unhighlight();
        insertCFIntoNodePseudocode.hide();
        insertCFIntoNodePseudocode.unhighlight(26);
        hideAtTheEnd.forEach(k -> cfGeometricsMap.get(k.getName()).hideBasics());
    }

    private void insertIntoLeaf(ClusterFeature cf, CFTreeNode currentNode) {
        insertCFIntoLeafPseudocode.show();
        insertCFIntoLeafPseudocode.highlight(27);
        currentNode.highlight();

        lang.nextStep();
        insertCFIntoLeafPseudocode.unhighlight(27);
        insertCFIntoLeafPseudocode.highlight(28);
        NamedLine[] distanceLines = visualizeDistancesFromCF(currentNode, cf);

        ClusterFeature[] keys = currentNode.getKeys();

        int nearestIdx = currentNode.findNearest(cf);
        ClusterFeature nearest = keys[nearestIdx];

        ClusterFeature newCF;
        if (nearest.getN() == 1) {
            newCF = nearest.addClusterFeature(cf, "C_" + tree.getcNameProvider().getName());
        } else {
            newCF = nearest.addClusterFeature(cf, nearest.getName());
        }

        lang.nextStep();
        distanceLines[nearestIdx].changeColor(coordinateSystemHighlightColor);

        lang.nextStep();
        insertCFIntoLeafPseudocode.unhighlight(28);
        insertCFIntoLeafPseudocode.highlight(29);
        Arrays.stream(distanceLines).forEach(dl -> dl.hide());

        ClusterFeature renamedNewCF = new ClusterFeature(newCF);
        renamedNewCF.setName("newCF");

        String text = "newCF =" + nearest.getName() + " + " + cf.getName() + "\n"
                + cf.getName() + " = " + cf.toStringWithoutName() + "\n"
                + nearest.getName() + " = " + nearest.toStringWithoutName() + "\n";

        String addition = "newCF " + translator.translateMessage("calculation") + ": \n"
                + "\t N = " + nearest.getName() + ".N + " + cf.getName() + ".N = " + nearest.getN() + " + " + cf.getN() + "\n"
                + "\t\t = " + newCF.getN() + "\n"
                + "\t LS = " + nearest.getName() + ".LS + " + cf.getName() + ".LS = " + nearest.getLS() + " + " + cf.getLS() + "\n"
                + "\t\t = " + newCF.getLS() + "\n"
                + "\t SS = " + nearest.getName() + ".SS + " + cf.getName() + ".SS = " + nearest.getSS() + " + " + cf.getSS() + "\n"
                + "\t\t = " + newCF.getSS();

        MultilineText addText = new MultilineText(text, lang, smallTextProperties, OffsetHelper.offsetOf(insertCFIntoLeafPseudocode.getLowerLeft(), 0, 20));

        lang.nextStep();
        MultilineText addText2 = new MultilineText(addition, lang, smallTextProperties, OffsetHelper.offsetOf(addText.getLowerLeft(), 0, 20));

        lang.nextStep();

        CFGeometrics newCFGeometrics = CFGeometrics.create(coordinateSystem, renamedNewCF);
        newCFGeometrics.showBasics();

        Text result = lang.newText(OffsetHelper.offsetOf(addText2.getLowerLeft(), 0, 20),
                newCF.getName() + " = newCF", "result", null, smallTextProperties);

        lang.nextStep();
        insertCFIntoLeafPseudocode.unhighlight(29);
        insertCFIntoLeafPseudocode.highlight(30);
        newCFGeometrics.showRadius();
        NamedLine thresholdLine = drawThresholdLine(newCF.getCentroid().getX(), newCF.getCentroid().getY());

        lang.nextStep();
        insertCFIntoLeafPseudocode.unhighlight(30);
        thresholdLine.hide();
        newCFGeometrics.hideRadius();

        if (newCF.getRadius() < threshold) {
            insertCFIntoLeafPseudocode.highlight(31);
            newCFGeometrics.hideBasics();

            keys[nearestIdx] = newCF;
            currentCFInsertedInto = newCF;
            tree.getcNameProvider().useName();

            CFGeometrics geo = CFGeometrics.create(coordinateSystem, newCF);
            geo.showBasics();
            cfGeometricsMap.put(newCF.getName(), geo);

            lang.nextStep();
            cfGeometricsMap.get(currentClusterFeature.getName()).changeColorBasics(Color.LIGHT_GRAY);
            cfGeometricsMap.get(nearest.getName()).changeColorBasics(Color.LIGHT_GRAY);

            currentClusterFeature = null;
            doTree();
            updateTable();
            currentNode.highlight();

            lang.nextStep();
            addText.hide();
            addText2.hide();
            result.hide();
            insertCFIntoLeafPseudocode.unhighlight(31);
        } else {
            newCFGeometrics.hideBasics();
            addText.hide();
            addText2.hide();
            result.hide();
            insertCFIntoLeafPseudocode.highlight(32);

            lang.nextStep();
            insertCFIntoLeafPseudocode.unhighlight(32);
            insertCFIntoLeafPseudocode.highlight(33);
            currentNode.addKey(cf);
            currentClusterFeature = null;
            doTree();
            updateTable();
            currentNode.highlight();

            lang.nextStep();
            doRandomShouldSplitNodeQuestion("shouldSplitLeaf", 2, currentNode.getCount() > BL);

            insertCFIntoLeafPseudocode.unhighlight(33);
        }
        insertCFIntoLeafPseudocode.highlight(34);

        lang.nextStep();
        insertCFIntoLeafPseudocode.hide();
        insertCFIntoLeafPseudocode.unhighlight(34);
        currentNode.unhighlight();
    }

    private SplitResult splitNode(CFTreeNode node, String oldName) {
        splitNodePseudocode.show();
        splitNodePseudocode.highlight(35);
        ClusterFeature[] cfs = node.getKeys();
        for (int i = 0; i < node.getCount(); i++) {
            cfGeometricsMap.get(cfs[i].getName()).showBasics();
        }

        lang.nextStep();
        splitNodePseudocode.unhighlight(35);
        splitNodePseudocode.highlight(36);

        Pair<Integer, Integer> widestAway = node.findWidestAway();

        ClusterFeature first = cfs[widestAway.getX()];
        ClusterFeature second = cfs[widestAway.getY()];

        Map<String, NamedLine> linesMap = new HashMap<>();

        for (int i = 0; i < node.getCount() - 1; i++) {
            Vector centroidI = cfs[i].getCentroid();
            for (int j = i + 1; j < node.getCount(); j++) {
                Vector centroidJ = cfs[j].getCentroid();
                NamedLine line = NamedLine.create(coordinateSystem, centroidI.getX(), centroidI.getY(), centroidJ.getX(), centroidJ.getY(),
                        String.format("%.2f", cfs[i].getDistanceTo(cfs[j])));
                linesMap.put(cfs[i].getName() + cfs[j].getName(), line);
                linesMap.put(cfs[j].getName() + cfs[i].getName(), line);
            }
        }

        CFTreeNode firstTN = new CFTreeNode(BL, node.isLeaf());
        CFTreeNode secondTN = new CFTreeNode(BL, node.isLeaf());

        firstTN.addKey(first, node.getChildren()[widestAway.getX()]);
        secondTN.addKey(second, node.getChildren()[widestAway.getY()]);

        ClusterFeature firstCF = first;
        ClusterFeature secondCF = second;

        lang.nextStep();
        linesMap.get(first.getName() + second.getName()).changeColor(coordinateSystemHighlightColor);

        lang.nextStep();

        String[] firstArrayData = new String[BL];
        firstArrayData[0] = first.getName();
        int firstArrayLength = 1;

        String[] secondArrayData = new String[BL];
        secondArrayData[0] = second.getName();
        int secondArrayLength = 1;

        for (int i = 1; i < firstArrayData.length; i++) {
            firstArrayData[i] = "NIL";
            secondArrayData[i] = "NIL";
        }
        StringArray firstArray = lang.newStringArray(
                OffsetHelper.offsetOf(insertCFIntoNodePseudocode.getLowerLeft(), 0, 20), firstArrayData,
                "firstSplitArray", null, splitArrayProperties);
        firstArray.showIndices(false, null, null);
        StringArray secondArray = lang.newStringArray(
                new Offset(0, 5, firstArray, AnimalScript.DIRECTION_SW), secondArrayData,
                "secondSplitArray", null, splitArrayProperties);
        secondArray.showIndices(false, null, null);

        lang.nextStep();
        splitNodePseudocode.unhighlight(36);
        splitNodePseudocode.highlight(37);
        splitNodePseudocode.highlight(38);
        linesMap.forEach((key, value) -> value.hide());

        lang.nextStep();

        // verbleibende CFs dem dichtesten der weit entferntesten CFs zuteilen
        for (int i = 0; i < node.getCount(); i++) {
            if (i == widestAway.getX() || i == widestAway.getY()) {
                continue;
            }

            NamedLine lineToFirst = linesMap.get(cfs[i].getName() + first.getName());
            NamedLine lineToSecond = linesMap.get(cfs[i].getName() + second.getName());
            lineToFirst.show();
            lineToSecond.show();
            lang.nextStep();

            ClusterFeature currentKey = cfs[i];
            if (currentKey.getDistanceTo(first) < currentKey.getDistanceTo(second)) {
                lineToFirst.changeColor(coordinateSystemHighlightColor);
                lang.nextStep();
                firstArray.put(firstArrayLength, currentKey.getName(), null, null);
                firstArrayLength++;

                firstTN.addKey(currentKey, node.getChildren()[i]);
                firstCF = firstCF.addClusterFeature(currentKey, null);
            } else {
                lineToSecond.changeColor(coordinateSystemHighlightColor);
                lang.nextStep();
                secondArray.put(secondArrayLength, currentKey.getName(), null, null);
                secondArrayLength++;

                secondTN.addKey(currentKey, node.getChildren()[i]);
                secondCF = secondCF.addClusterFeature(currentKey, null);
            }

            lang.nextStep();
            lineToFirst.hide();
            lineToSecond.hide();
        }

        boolean oldNameUsed = oldName == null;
        if (firstTN.getCount() > 1) {
            if (!oldNameUsed) {
                firstCF.setName(oldName);
                oldNameUsed = true;
            } else
                firstCF.setName(tree.getNameProvider().useName());
            cfGeometricsMap.put(firstCF.getName(), CFGeometrics.create(coordinateSystem, firstCF));
        }
        if (secondTN.getCount() > 1) {
            if (!oldNameUsed)
                secondCF.setName(oldName);
            else
                secondCF.setName(tree.getNameProvider().useName());
            cfGeometricsMap.put(secondCF.getName(), CFGeometrics.create(coordinateSystem, secondCF));
        }

        lang.nextStep();
        splitNodePseudocode.unhighlight(37);
        splitNodePseudocode.unhighlight(38);
        splitNodePseudocode.highlight(39);
        splitNodePseudocode.highlight(40);
        Text firstText = lang.newText(new Offset(5, 2, firstArray, AnimalScript.DIRECTION_NE), "<- first",
                "firstSplitArrayName", null, smallTextProperties);
        Text secondText = lang.newText(new Offset(5, 2, secondArray, AnimalScript.DIRECTION_NE), "<- second",
                "secondSplitArrayName", null, smallTextProperties);

        lang.nextStep();
        splitNodePseudocode.unhighlight(39);
        splitNodePseudocode.unhighlight(40);
        splitNodePseudocode.hide();
        for (int i = 0; i < node.getCount(); i++) {
            cfGeometricsMap.get(cfs[i].getName()).hideBasics();
        }

        return new SplitResult(firstTN, secondTN, firstCF, secondCF, firstArray, secondArray, firstText, secondText);
    }

    private NamedLine[] visualizeDistancesFromCF(CFTreeNode node, ClusterFeature cf) {
        NamedLine[] allLines = new NamedLine[node.getCount()];
        float cfX = cf.getCentroid().getX();
        float cfY = cf.getCentroid().getY();
        ClusterFeature[] cfs = node.getKeys();

        for (int i = 0; i < node.getCount(); i++) {
            Vector centroid = cfs[i].getCentroid();
            allLines[i] = NamedLine.create(coordinateSystem, cfX, cfY, centroid.getX(), centroid.getY(),
                    String.format("%.2f", cfs[i].getDistanceTo(cf)));
        }

        return allLines;
    }

    private void showAllCFsColored(CFTreeNode node, int heigth) {
        Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CYAN, Color.MAGENTA,
                Color.PINK};

        Color color = Color.LIGHT_GRAY;
        if (heigth <= colors.length)
            color = colors[heigth - 1];

        node.getRepresentationTreeNode().changeColor("outlineColor", color, null, null);

        ClusterFeature[] keys = node.getKeys();
        CFTreeNode[] children = node.getChildren();
        for (int i = 0; i < node.getCount(); i++) {
            CFGeometrics geo = cfGeometricsMap.get(keys[i].getName());
            geo.showBasics();
            geo.changeColorBasics(color);

            if (!node.isLeaf() && children[i].getCount() > 1)
                showAllCFsColored(children[i], heigth + 1);
            else if (!node.isLeaf())
                colorSubtree(children[i], heigth + 1, color);
        }
    }

    private void colorSubtree(CFTreeNode node, int height, Color color) {
        node.getRepresentationTreeNode().changeColor("outlineColor", color, null, null);

        CFTreeNode child = node.getChildren()[0];
        if (!node.isLeaf() && child.getCount() > 1)
            showAllCFsColored(child, height + 1);
        else if (!node.isLeaf())
            colorSubtree(child, height + 1, color);
    }

    /*
     * Questions
     */
    private boolean shouldCreateQuestion(String group, int limit) {
        Integer createdCount = questionsByGroup.getOrDefault(group, 0);
        if (createdCount < limit && new Random().nextBoolean()) {
            questionsByGroup.put(group, createdCount + 1);
            return true;
        }
        return false;
    }

    private void doRandomShouldSplitNodeQuestion(String group, int limit, boolean shouldSplitNode) {
        if (shouldCreateQuestion(group, limit)) {
            TrueFalseQuestionModel question = new TrueFalseQuestionModel(nextQuestionID + "", shouldSplitNode, 5);
            question.setPrompt(translator.translateMessage("split_node_question"));

            nextQuestionID++;
            lang.addTFQuestion(question);
            lang.nextStep();
        }
    }
}
