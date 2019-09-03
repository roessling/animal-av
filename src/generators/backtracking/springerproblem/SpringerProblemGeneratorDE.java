package generators.backtracking.springerproblem;

import generators.backtracking.helpers.AnimalStyling;
import generators.backtracking.helpers.Springerproblem;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;


import java.util.Hashtable;

import algoanim.primitives.generators.Language;
import generators.framework.properties.AnimationPropertiesContainer;

public class SpringerProblemGeneratorDE implements ValidatingGenerator {
    private Language lang;
    private int startCol;

    private int cols;
    private int startRow;;
    private int rows;
    
    public void init(){
    	Springerproblem.reset();
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
       
    	AnimalStyling.setProps(props);
    	startCol = (Integer)primitives.get("startCol");
        cols = (Integer)primitives.get("cols");
        rows = (Integer)primitives.get("rows");
        startRow = (Integer)primitives.get("startRow");
        
        
        lang = Springerproblem.generate(rows, cols, startRow, startCol);
        return lang.toString();
    }

    public String getName() {
        return "Springerproblem";
    }

    public String getAlgorithmName() {
        return "Springerproblem";
    }

    public String getAnimationAuthor() {
        return "Eric Brüggemann, Mohit Mahkija";
    }

    public String getDescription(){
        return "<h1><font color='red'>Warnung: Je nach Eingabeparametern ist die erzeugte ASU Datei sehr groß.</font></h1><br />" +
        		"Das Springerproblem ist das Problem für den Springer auf einem n * m großen Schachfeld einen Weg zu finden bei dem er jedes Feld genau ein mal besucht.<br /><br />" +
        		"Da das Springerproblem ein Spezialfall des Hamiltonpfadproblems ist, welches NP-vollständig ist, ist kein effizienter Lösungsalgorithmus bekannt.<br />" +
        		"In diesem Beispiel wird ein einfacher Backtracking Algorithmus verwendet, der immer das Feld als Nächstes besucht, das die wenigsten freien Nachbarn hat. (Warnsdorf Regel).";
    }

    public String getCodeExample(){
        return "private boolean solve(Board board, Field field, int turn) {"
 +"\n"
 +"	field.visit(turn);"
 +"\n"
 +"	if (turn == board.getCols() * board.getRows() - 1) {"
 +"\n"
 +"		return true;"
 +"\n"
 +"	}"
 +"\n"
 +"	List<Field> fields = field.getNeighboors();"
 +"\n"
 +"	Collections.sort(fields);"
 +"\n"
 +"	for (Field neighboor : fields) {"
 +"\n"
 +"		if (!neighboor.isVisited()) {"
 +"\n"
 +"			if (solve(board, neighboor, turn + 1)) {"
 +"\n"
 +"				return true;"
 +"\n"
 +"			}"
 +"\n"
 +"			board.unvisit(neighboor);"
 +"\n"
 +"		}"
 +"\n"
 +"	}"
 +"\n"
 +"	return false;"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_BACKTRACKING);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) throws IllegalArgumentException {
        rows = (Integer)primitives.get("rows");
        cols = (Integer)primitives.get("cols");
        startRow = (Integer)primitives.get("startRow");
        startCol = (Integer)primitives.get("startCol");
        
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Spielfeld zu klein!");
        } 
        if (startRow < 0 || startRow >= rows || startCol < 0 || startCol >= cols) {
            throw new IllegalArgumentException("Die Startposition muss innerhalb des Spielfeldes liegen!");
        }
        return true;
    }

}