/*
 * BIRCHGenerator.java
 * Kyra Wittorf, Robin Körkemeier, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.birch.Birch;
import generators.misc.birch.ClusterFeature;
import translator.Translator;

import java.util.Hashtable;
import java.util.Locale;

public class BIRCHGenerator implements ValidatingGenerator {
    private Language lang;

    private Translator translator;
    private Locale locale;

    private int cfIndex = 0;

    public static void main(String[] args) {
        Generator generator = new BIRCHGenerator("resources/BIRCHGenerator", Locale.GERMANY);
        Animal.startGeneratorWindow(generator);
    }

    public BIRCHGenerator(String resource, Locale languageLocale) {
        translator = new Translator(resource, languageLocale);
        locale = languageLocale;
    }

    @Override
    public void init() {
        lang = new AnimalScript(translator.translateMessage("title"), "Kyra Wittorf, Robin Körkemeier", 800, 600);
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        String[][] initialClusterFeatures = (String[][]) primitives.get("Initiale Cluster-Features");
        String[][] insertClusterFeatures = (String[][]) primitives.get("Einzufügende Cluster-Features");

        if (insertClusterFeatures.length <= 1)
            return false;

        try {
            ClusterFeature[] initial = parseStringCFs(initialClusterFeatures);
            ClusterFeature[] insert = parseStringCFs(insertClusterFeatures);

            for (ClusterFeature cf : initial) {
                if (cf.getN() < 1 || cf.getLS().getX() < 0 || cf.getLS().getY() < 0 || cf.getSS().getX() < 0 || cf.getSS().getY() < 0)
                    return false;
            }
            for (ClusterFeature cf : insert) {
                if (cf.getN() < 1 || cf.getLS().getX() < 0 || cf.getLS().getY() < 0 || cf.getSS().getX() < 0 || cf.getSS().getY() < 0)
                    return false;
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return false;
        }

        int BL = (Integer) primitives.get("BL");
        float threshold = ((Double) primitives.get("Threshold")).floatValue();

        if (BL < 2)
            return false;
        if (threshold <= 0)
            return false;

        return true;
    }

    @Override
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        cfIndex = 0;

        String[][] initialClusterFeatures = (String[][]) primitives.get("Initiale Cluster-Features");
        String[][] insertClusterFeatures = (String[][]) primitives.get("Einzufügende Cluster-Features");

        int BL = (Integer) primitives.get("BL");
        float threshold = ((Double) primitives.get("Threshold")).floatValue();

        MatrixProperties Tabelle = (MatrixProperties) props.getPropertiesByName("Tabelle");
        RectProperties Rechteck = (RectProperties) props.getPropertiesByName("Rechteck");
        ArrayProperties Baum = (ArrayProperties) props.getPropertiesByName("Baum");
        TextProperties Text = (TextProperties) props.getPropertiesByName("Text");
        SourceCodeProperties Pseudocode = (SourceCodeProperties) props.getPropertiesByName("Pseudocode");
        SourceCodeProperties Koordinatensystem = (SourceCodeProperties) props.getPropertiesByName("Koordinatensystem");

        Birch birch = new Birch(lang, BL, threshold,
                parseStringCFs(initialClusterFeatures), parseStringCFs(insertClusterFeatures), translator);
        birch.initializeProperties(Tabelle, Rechteck, Baum, Text, Pseudocode, Koordinatensystem);
        birch.doAnimation();

        return lang.toString();
    }

    private ClusterFeature[] parseStringCFs(String[][] data) {
        ClusterFeature[] parsed = new ClusterFeature[data.length - 1];

        for (int i = 1; i < data.length; i++) {
            String[] d = data[i];
            parsed[i - 1] = new ClusterFeature(
                    Integer.parseInt(d[0]),
                    Float.parseFloat(d[1]),
                    Float.parseFloat(d[2]),
                    Float.parseFloat(d[3]),
                    Float.parseFloat(d[4]),
                    "C_" + cfIndex
            );
            cfIndex++;
        }

        return parsed;
    }

    @Override
    public String getName() {
        return translator.translateMessage("title");
    }

    @Override
    public String getAlgorithmName() {
        return translator.translateMessage("title_long");
    }

    @Override
    public String getAnimationAuthor() {
        return "Kyra Wittorf, Robin Körkemeier";
    }

    @Override
    public String getDescription() {
        return translator.translateMessage("short_description");
    }

    @Override
    public String getCodeExample() {
        return "insertCF(cf)"
                + "\n"
                + "	if root is NIL"
                + "\n"
                + "		root <- new Node()"
                + "\n"
                + "		root.add(cf)"
                + "\n"
                + "		return"
                + "\n"
                + "	endif"
                + "\n"
                + "	insertCFIntoNode(cf, root)"
                + "\n"
                + "	if root.getCount() > BL"
                + "\n"
                + "		[first, second] <- splitNode(root)"
                + "\n"
                + "		root <- new Node()"
                + "\n"
                + "		root.add(first, second)"
                + "\n"
                + "	endif"
                + "\n"
                + "\n"
                + "insertCFIntoNode(cf, node)"
                + "\n"
                + "	if node.isLeaf"
                + "\n"
                + "		insertCFIntoLeaf(cf, node)"
                + "\n"
                + "	else"
                + "\n"
                + "		nearestCF <- node.findNearestCF(cf)"
                + "\n"
                + "		insertCFIntoNode(cf, nearestCF.node)"
                + "\n"
                + "		if nearestCF.node.getCount() > BL"
                + "\n"
                + "			[first, second] <- splitNode(nearestCF.node)"
                + "\n"
                + "			node.add(first, second)"
                + "\n"
                + "			node.remove(nearestCF)"
                + "\n"
                + "		else"
                + "\n"
                + "			recalculate(nearestCF)"
                + "\n"
                + "		endif"
                + "\n"
                + "	endif"
                + "\n"
                + "\n"
                + "insertCFIntoLeaf(cf, leaf)"
                + "\n"
                + "	nearestCF <- leaf.findNearestCF(cf)"
                + "\n"
                + "	newCF <- nearestCF.add(cf)"
                + "\n"
                + "	if treshold < newCF.getRadius()"
                + "\n"
                + "		nearestCF <- newCF"
                + "\n"
                + "	else"
                + "\n"
                + "		leaf.add(cf)"
                + "\n"
                + "	endif"
                + "\n"
                + "\n"
                + "[first, second] splitNode(node)"
                + "\n"
                + "	[cf1, cf2] <- CFs in node that are most far away"
                + "\n"
                + "	[[cf1, ...], [cf2, ...]] <- put all remaining CFs in the nearest node"
                + "\n"
                + "	first <- [cf1, ...]"
                + "\n"
                + "	second <- [cf2, ...]";
    }

    @Override
    public String getFileExtension() {
        return "asu";
    }

    @Override
    public Locale getContentLocale() {
        return locale;
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    @Override
    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}