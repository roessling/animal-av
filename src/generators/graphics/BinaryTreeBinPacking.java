/*
 * BinaryTreeBinPacking.java
 * Julian Fischer, Christian Seybert, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;
//package anim;
import java.awt.*;
import java.util.*;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.VariablesGenerator;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.primitives.Rect;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.main.Animal;
import animal.variables.Variable;
import animal.variables.VariableRoles;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

class Block implements Comparable<Block> {
    private Rect animRect;
    private int x;
    private int y;
    private int width;
    private int height;

    Block(Language l, int x, int y, int width, int height, Color col, String id) {
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, col);
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        animRect = l.newRect(new Coordinates(x,y),new Coordinates(x+width,y+height), id, null, rectProps);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPos(int x, int y) {
        animRect.moveTo(null, null, new Coordinates(x, y), null, null);
        this.x = x;
        this.y = y;
    }

    public void movePos(int x, int y) {
        animRect.moveBy(null, x, y, null, null);
        this.x -= x;
        this.y -= y;
    }

    public void destroy(){
        animRect.hide();
    }

    public int compareTo(Block block) {
        return block.height - height;
    }
}


class BinPackerNode {
    private Language l;
    private SourceCode sc;
    private Rect animRec;
    private Rectangle area;
    private boolean isUsed;
    private BinPackerNode rightNode;
    private BinPackerNode bottomNode;

    BinPackerNode(int x, int y, int width, int height, Language l, SourceCode sc, String id) {
        this.l = l;
        this.sc = sc;
        animRec = l.newRect(new Coordinates(x,y),new Coordinates(x+width,y+height), id, null);

        area = new Rectangle(x, y, width, height);
        isUsed = false;
        rightNode = null;
        bottomNode = null;
    }

    // Insert the block in the given Area and returns true if it fits
    boolean insertBlock(Block block) {
        RectProperties rectPropsHighlight = new RectProperties();
        rectPropsHighlight.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.getHSBColor(0.5f, 1.0f, 1.0f));
        Rect highlightRect = l.newRect(new Coordinates(area.x,area.y), new Coordinates(area.x+area.width,area.y+area.height), null, null, rectPropsHighlight);



        l.nextStep();
        highlightRect.hide();
        sc.unhighlight(2);
        sc.unhighlight(3);

        if(isUsed){
            // Find a place in the right node and if no place is found in the bottom node
            sc.highlight(2);
            if(rightNode.insertBlock(block))
                return true;

            sc.highlight(3);
            if(bottomNode.insertBlock(block))
                return true;

            return false;
        }

        if(block.getWidth() <= area.width && block.getHeight() <= area.height) {
            for(int i = 6; i <= 13; i++)
                sc.highlight(i);
            // Insert and split this node
            isUsed = true;
            rightNode = new BinPackerNode(area.x + block.getWidth(), area.y,
                    area.width - block.getWidth(), block.getHeight(), l, sc, animRec.getName()+">");
            bottomNode = new BinPackerNode(area.x, area.y + block.getHeight(),
                    area.width, area.height - block.getHeight(), l, sc, animRec.getName()+"v");
            block.setPos(area.x, area.y);
            l.nextStep();
            for(int i = 6; i <= 13; i++)
                sc.unhighlight(i);
            return true;
        }
        sc.highlight(15);
        l.nextStep();
        sc.unhighlight(15);
        return false;
    }


}

public class BinaryTreeBinPacking implements Generator {
    private Language lang;
    int areaWidth = 256;
    int areaHeight = 256;

    public void init(){
        lang = new AnimalScript("Binary Tree Bin Packing", "Julian Fischer, Christian Seybert", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        int[][] binSize = (int[][])primitives.get("Bin Size");
        areaHeight = (Integer)primitives.get("Area Height");
        boolean sort = (Boolean)primitives.get("Sort");
        areaWidth = (Integer)primitives.get("Area Width");

        lang.setStepMode(true);
        lang.setInteractionType(
                Language.INTERACTION_TYPE_AVINTERACTION);

        Text title = lang.newText(new Coordinates(20, 20), "Binary Tree Bin Packing", "title", null);
        title.setFont(new Font("SansSerif", 1, 20), null, null);


        SourceCode description = lang.newSourceCode(new Coordinates(20, 70), "description", null);
        description.addMultilineCode("Mit Hilfe des Multi Bin Tree Packing Algorithmus können Rechtecke\n" +
                "effizient in einem größeren Rechteck angeordnet werden, sodass möglichst wenig Platz\n" +
                "frei bleibt. Dies ist zum Beispiel hilfreich für sogenannte Texturenatlanten, wo viele\n" +
                "kleine Texturen auf der Grafikkarte zu einer großen zusammengepackt werden, um Zugriffe einzusparen\n" +
                "Die dreidimensionale Form dieses Algorithmus kann auch verwendet werden, um z.B bei Amazon Pakete\n" +
                "möglichst effizient in den Transporter einzuladen.\n" +
                "Der Algorithums liefert nicht immer eine optimale Lösung. Das Problem ist nämlich NP-Schwer lösbar,\n" +
                "weshalb schon ab ca. 30 Paketen ein optimaler Algorithmus ein paar Tage rechnen würde.\n" +
                "Der Algorithmus funktioniert am besten, wenn man die Pakete vorher nach Größe absteigend sortiert.\n" +
                "Dann fügt man die Pakete nacheinander ein. Es wird geschaut, ob ein Paket vorhanden\n" +
                "ist, wenn ja wiederholt er den Schritt für den Platz rechts vom Paket und dann unterhalb\n" +
                "des Paketes. Wenn kein Paket vorhanden ist und das Paket reinpasst, so sortiert er es\n" +
                "ein. Ansonsten sucht er weiter, bis kein Platz mehr gefunden werden kann.", null, null);

        lang.nextStep("Einleitung");
        description.hide();
        rand = new Random();
        this.l = l;
        blocks = new ArrayList<>();
        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
        SourceCode sc = lang.newSourceCode(new Coordinates(areaWidth+gapSize*2,
                gapSize*2+grainSize*grainSteps), "sourceCode", null, scProps);

        sc.addCodeLine("boolean insertBlock(Rectangle block) {", null, 0, null);
        sc.addCodeLine("if(isUsed)", null, 1, null);
        sc.addCodeLine("return rightNode.insertBlock(block) ||", null, 2, null);
        sc.addCodeLine("       bottomNode.insertBlock(block);", null, 2, null);
        sc.addCodeLine("", null, 1, null);
        sc.addCodeLine("if(block.width <= area.width && block.height <= area.height) {", null, 1, null);
        sc.addCodeLine("isUsed = true;", null, 2, null);
        sc.addCodeLine("rightNode = new BinPackerNode(area.x + block.width, " +
                "area.y,", null, 2, null);
        sc.addCodeLine("                              area.width - block" +
                ".width, block.height);", null, 2, null);
        sc.addCodeLine("bottomNode = new BinPackerNode(area.x, area.y + block" +
                ".height, ",null,2,null);
        sc.addCodeLine("                               area.width, area" +
                ".height - block.height);", null, 2, null);
        sc.addCodeLine("block.x = area.x;", null, 2, null);
        sc.addCodeLine("block.y = area.y;", null, 2, null);
        sc.addCodeLine("return true;", null, 2, null);
        sc.addCodeLine("}", null, 1, null);
        sc.addCodeLine("return false;", null, 1, null);
        sc.addCodeLine("}", null, 0, null);


        BinPackerNode node = new BinPackerNode(gapSize, gapSize*2+grainSize*grainSteps, areaWidth, areaHeight, lang, sc, "Area:");

        // Add all given blocks
        blockListOffsetX = 0;
        for(int count = 0; count < binSize.length; count++)
            addBlock( binSize[count][0], binSize[count][1]);
        if(sort) {
            lang.nextStep("Sortierung");
            sortBlocks();
            reArrange();

        }
        lang.nextStep("Einfügen");
        calculateBlockPositions(node);
        lang.hideAllPrimitivesExcept(Arrays.asList(title));

        description = lang.newSourceCode(new Coordinates(20, 70), "description", null);
        description.addMultilineCode("Das Ergebnis ist ein relativ dicht bepacktes Rechteck. Die Laufzeitkomplexität ist\n" +
                "im besten Fall linear und im schlimmsten Fall quadratisch. Rechtecke, die nicht hineinpassten, wurden\n" +
                "dabei verworfen. Das Ergebnis ist auch nicht zwangsweise optimal, aber\n" +
                "in den meisten Fällen gut genug.", null, null);
        lang.nextStep("Schlusswort");
        //System.out.println(l);
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Binary Tree Bin Packing";
    }

    public String getAlgorithmName() {
        return "Binary Tree Bin Packing";
    }

    public String getAnimationAuthor() {
        return "Julian Fischer, Christian Seybert";
    }

    public String getDescription(){
        return "Algorithmus, um kleinere Rechtecke in ein größeres platzsparend einzufügen.";
    }

    public String getCodeExample(){
        return "boolean insertBlock(Rectangle block) {"
                +"\n"
                +"    if(isUsed)"
                +"\n"
                +"        return rightNode.insertBlock(block) || bottomNode.insertBlock(block);"
                +"\n"
                +"\n"
                +"    if(block.width <= area.width && block.height <= area.height) {"
                +"\n"
                +"        isUsed = true;"
                +"\n"
                +"        rightNode = new BinPackerNode(area.x + block.width, area.y, "
                +"\n"
                +"                                                         area.width - block.width, block.height);\", null, 2, null);"
                +"\n"
                +"        bottomNode = new BinPackerNode(area.x, area.y + block.height, "
                +"\n"
                +"                                                             area.width, area .height - block.height);\", null, 2, null);"
                +"\n"
                +"        block.x = area.x;\", null, 2, null);"
                +"\n"
                +"        block.y = area.y;\", null, 2, null);"
                +"\n"
                +"        return true;"
                +"\n"
                +"    }"
                +"\n"
                +"    return false"
                +"\n"
                +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }


    final int gapSize = 40;
    final int grainSize = 16;
    final int grainSteps = 8;

    private Language l;
    private ArrayList<Block> blocks;
    private int blockListOffsetX = 0;
    Random rand;

    private void addBlock(int width, int height) {
        blocks.add(new Block(lang, gapSize+blockListOffsetX, gapSize + (grainSize * grainSteps - height) / 2,
                width, height, Color.getHSBColor(rand.nextFloat(),0.6f, 0.9f), "Bin:"+blocks.size()));
        blockListOffsetX += width + gapSize;
    }

    private void reArrange() {
        blockListOffsetX = 0;
        for (Block block: blocks) {
            block.setPos(gapSize+blockListOffsetX, block.getY());
            blockListOffsetX += block.getWidth() + gapSize;
        }
    }

    private void sortBlocks() {
        Collections.sort(blocks);
    }

    private void calculateBlockPositions(BinPackerNode root) {
        while(!blocks.isEmpty()) {
            Block curBlock = blocks.remove(0);
            if(!root.insertBlock(curBlock))
                curBlock.destroy();
            for (Block block: blocks) {
                block.movePos(-curBlock.getWidth()-gapSize, 0);
            }
        }
    }

    public static void main(String[] args) {
        Generator generator = new BinaryTreeBinPacking();
        Animal.startGeneratorWindow(generator);
    }
}
