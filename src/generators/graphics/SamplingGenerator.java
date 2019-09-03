package generators.graphics;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.sampling.AnimalGrid;
import generators.graphics.sampling.AnimalValue;
import generators.graphics.sampling.Utils;
import static generators.graphics.sampling.Utils.APK;
import static generators.graphics.sampling.Utils.buildOffset;
import java.awt.Font;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author lobo
 */
public class SamplingGenerator implements ValidatingGenerator {
    
    protected Language lang;
    protected String algoName;
    protected String algoAuthors;
    protected String algoDescription;
    protected String algoConclusion;
    protected String SOURCE_CODE;
    protected SourceCode actualSourceCode;
    
    protected TextProperties topicProp;
    protected TextProperties textProp;
    protected SourceCodeProperties sourceCodeProp;
    
    protected void setupProperties(AnimationPropertiesContainer props, Map<String, Object> primitives){
        textProp = (TextProperties) props.getPropertiesByName("text");
        // Derive topic font size from text font size and make it bold
        topicProp = Utils.clone(textProp);
        Font font = (Font) topicProp.get(APK.FONT_PROPERTY);
        font = new Font(font.getName(), font.getStyle() + Font.BOLD, font.getSize() + 4);
        topicProp.set(APK.FONT_PROPERTY, font);
        
        sourceCodeProp = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
        
        AnimalGrid.setColor(
                (SquareProperties) props.getPropertiesByName("fillColor"), 
                (SquareProperties) props.getPropertiesByName("markColor")
        );
        
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer apc, Hashtable<String, Object> hshtbl) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String generate(AnimationPropertiesContainer apc, Hashtable<String, Object> hshtbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAlgorithmName() {
        return algoName;
    }

    @Override
    public String getAnimationAuthor() {
        return algoAuthors;
    }

    @Override
    public String getCodeExample() {
        String code = SOURCE_CODE.replaceFirst("%d", "System.currentTimeMillis()");
        code = code.replaceAll("<", "&lt;");
        code = code.replaceAll(">", "&gt;");
        return code;
    }

    @Override
    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    @Override
    public String getDescription() {
        return algoDescription;
    }

    @Override
    public String getFileExtension() {
        return "asu";
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    @Override
    public String getName() {
        return algoName;
    }

    @Override
    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    @Override
    public void init() {
        lang = new AnimalScript(algoName, algoAuthors, 1600, 1000);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }
    
    protected void toggleHighlight(SourceCode sc, int oldline, int newline, String label) {
        sc.toggleHighlight(oldline, newline);
        nextStep(label);
    }
    
    protected void toggleHighlight(SourceCode sc, int oldline, int newline) {
        toggleHighlight(sc, oldline, newline, null);
    }
    
    protected void toggleHighlight(int oldline, int newline) {
        toggleHighlight(actualSourceCode, oldline, newline, null);
    }
    
    protected void toggleHighlight(int oldline, int[] newlines) {
        actualSourceCode.unhighlight(oldline);
        for (int l : newlines) {
            actualSourceCode.highlight(l);
        }
        lang.nextStep();
    }
    
    protected void toggleHighlight(int[] oldlines, int[] newlines) {
        for (int l : oldlines) {
            actualSourceCode.unhighlight(l);
        }
        for (int l : newlines) {
            actualSourceCode.highlight(l);
        }
        lang.nextStep();
    }
    
    protected void toggleHighlightWithoutStep(int[] oldlines, int[] newlines) {
        for (int l : oldlines) {
            actualSourceCode.unhighlight(l);
        }
        for (int l : newlines) {
            actualSourceCode.highlight(l);
        }
    }
    
    protected void toggleHighlight(int[] oldlines, int[] newlines, String label) {
        for (int l : oldlines) {
            actualSourceCode.unhighlight(l);
        }
        for (int l : newlines) {
            actualSourceCode.highlight(l);
        }
        lang.nextStep(label);
    }

    protected void highlight(SourceCode sc, int newline, String label) {
        sc.highlight(newline);
        nextStep(label);
    }
    
    protected void highlight(SourceCode sc, int newline) {
        highlight(sc, newline, null);
    }
    
    protected void highlight(int newline) {
        highlight(actualSourceCode, newline, null);
    }
    
    protected void highlight(int newline, String label) {
        highlight(actualSourceCode, newline, label);
    }
    
    protected void unhighlight(int oldline) {
        unhighlight(actualSourceCode, oldline);
    }
    
    protected void activateSourceCode(SourceCode sc, int newline) {
        sc.highlight(newline);
        nextStep();
    }

    protected void unhighlight(SourceCode sc, int newline) {
        sc.unhighlight(newline);
        nextStep();
    }
    
    protected SourceCode generateSourceCode(Node baseNode, String sourceCode, String name) {
        SourceCode sc = lang.newSourceCode(baseNode, name, null, sourceCodeProp);

        String[] lines = sourceCode.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int indention = line.lastIndexOf('\t') + 1;
            sc.addCodeLine(line, null, indention, null);
        }
        return sc;
    }
    
    protected SourceCode generateSourceCode(Node baseNode, String sourceCode) {
        return generateSourceCode(baseNode, sourceCode, "SourceCode0");
    }
    
    protected Node placeTopic(String topic) {
        lang.newText(new Coordinates(25, 25), topic, "topicText", null, topicProp);
        RectProperties rectProp = new RectProperties();
        rectProp.set(APK.COLOR_PROPERTY, topicProp.get(APK.COLOR_PROPERTY));
        lang.newRect(new Offset(-2, -2, "topicText", "NW"), new Offset(2, 2, "topicText", "SE"), "topicRect", null, rectProp);
        return new Offset(0, 2, "topicRect", "SW");
    }
    
    protected AnimalValue[] createValues(String[] values, Node baseNode) {
        int count = values.length;
        AnimalValue[] labels = new AnimalValue[count];
        int lineSpacing = 15;
        for (int i = 0; i < count; i++) {
            labels[i] = new AnimalValue(lang, textProp);
            Offset offset = buildOffset(lang, 0, i * lineSpacing, baseNode, "SW");
            labels[i].createValue(offset, values[i]);
        }
        return labels;
    }
    
    protected void nextStep(String label){
        if(label != null && label.length() > 0){
            lang.nextStep(label);
        } else {
            lang.nextStep();
        }
    }
    
    protected void nextStep(){
        nextStep(null);
    }
    
    protected double calcMinDistance(java.awt.Point from, List<java.awt.Point> points){
        double minDistance = Double.MAX_VALUE;
        for(java.awt.Point to : points){
            if(to != from){
                double distance = Math.sqrt(Math.pow(from.x-to.x, 2) + Math.pow(from.y-to.y, 2));
                minDistance = minDistance > distance ? distance : minDistance;
            }
        }
        return minDistance;
    }
    
    protected double calcMinDistance(List<java.awt.Point> points){
        double minDistance = Double.MAX_VALUE;
        for(java.awt.Point point : points){
            double distance = calcMinDistance(point, points);
            minDistance = minDistance > distance ? distance : minDistance;
        }
        return minDistance;
    }
    
    protected double calcAvgMinDistance(List<java.awt.Point> points){
        double avgMinDistance = 0;
        for(java.awt.Point point : points){
            avgMinDistance += calcMinDistance(point, points);
        }
        return avgMinDistance / points.size();
    }
    
}