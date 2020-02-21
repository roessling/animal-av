package generators.misc.birch.elements;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.misc.birch.OffsetHelper;

public class NumberedSourceCode {
    private SourceCode lineNumbers;
    private SourceCode code;
    private int nextLineNumber;
    private int firstLineNumber;

    public NumberedSourceCode(Node node, SourceCodeProperties sourceCodeProperties, Language lang, int firstLineNumber){
        this.nextLineNumber = firstLineNumber;
        this.firstLineNumber = firstLineNumber;
        lineNumbers = lang.newSourceCode(node,"lineNumbers", null, sourceCodeProperties);
        code = lang.newSourceCode(OffsetHelper.offsetOf(node,30,0), "code", null, sourceCodeProperties);
    }

    public void addCodeLine(String text, int indentation){
        if (nextLineNumber <10) {
            lineNumbers.addCodeLine(" " + nextLineNumber + ".", null, 0, null);
        } else {
            lineNumbers.addCodeLine(nextLineNumber + ".", null, 0, null);
        }
        code.addCodeLine(text, null, indentation, null);
        nextLineNumber++;
    }

    public void hide(){
        lineNumbers.hide();
        code.hide();
    }

    public void show(){
        lineNumbers.show();
        code.show();
    }

    public void highlight(int line){
        code.highlight(line-firstLineNumber);
    }

    public void unhighlight(int line) {code.unhighlight(line-firstLineNumber);}

    public Node getLowerLeft() {
        return new Offset(0, 0, lineNumbers, AnimalScript.DIRECTION_SW);
    }

    public Node getUpperRight() {
        return new Offset(0,-12 ,code,AnimalScript.DIRECTION_NE);
    }
}
