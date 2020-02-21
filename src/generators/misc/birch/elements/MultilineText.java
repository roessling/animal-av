package generators.misc.birch.elements;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.misc.birch.OffsetHelper;

import java.util.LinkedList;

public class MultilineText {
    private LinkedList<Text> textLines = new LinkedList<>();
    private int lastLineIndentation = 0;

    public MultilineText(String string, Language lang, TextProperties textProperties, Node node) {
        Node lastNode = node;

        int lastIndentation = 0;
        int currentIndentation;

        String[] lines = string.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            currentIndentation = 0;
            while (line.indexOf("\t") == 0) {
                currentIndentation += 40;
                line = line.replaceFirst("\t", "");
            }

            if (line.equals("")) {
                lastNode = OffsetHelper.offsetOf(lastNode, currentIndentation - lastIndentation, 10);
            } else {
                Text text = lang.newText(OffsetHelper.offsetOf(lastNode, currentIndentation - lastIndentation, 0),
                        lines[i], "line" + i, null, textProperties);
                lastNode = new Offset(0, 0, text, AnimalScript.DIRECTION_SW);
                textLines.add(text);
            }

            lastIndentation = currentIndentation;
        }

        lastLineIndentation = lastIndentation;
    }

    public Node getLowerLeft() {
        return new Offset(-lastLineIndentation, 0, textLines.getLast(), AnimalScript.DIRECTION_SW);
    }

    public void hide() {
        textLines.forEach(Primitive::hide);
    }
}
