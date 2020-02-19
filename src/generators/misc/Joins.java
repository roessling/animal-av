package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;

/*
 * Joins.java
 * Adrian Wagener, Ivelin Ivanov, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

public class Joins implements ValidatingGenerator {
    private Language lang;
    private Translator translator;
    private Text title;
    private TextProperties titleProperties;
    private RectProperties smallTitleRectangleProperties;
    private RectProperties bigTitleRectangleProperties;
    private Polyline hLine;
    private PolylineProperties titleLineProperties;
    private TextProperties descProperties;
    private SourceCode sc;
    private SourceCodeProperties sourceCodeProperties;
    private Text[] out = new Text[5];
    private TextProperties outroProperties;
    private Rect[] outRect = new Rect[1];
    private boolean askQuestion;
    private Random rand = new Random();
    private int lastAskedQuestion;
    private int r;
    private String[][] tableA;
    private String[][] tableB;
    private StringMatrix firstTable;
    private StringMatrix secondTable;
    private StringMatrix createdTable;
    private int joinType = 1;
    private MatrixProperties matProp;
    private Text codeComment;
    private Rect[] commentRect = new Rect[4];
    
    public Joins(String resource, Locale locale) {
        // Translator
        translator = new Translator(resource, locale);
        // Language
        lang = new AnimalScript(translator.translateMessage("algorithmName"), "Adrian Wagener, Ivelin Ivanov", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    /************************************************************************************
     **************************** Generator Framework ***********************************
     ************************************************************************************/
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        titleLineProperties = (PolylineProperties) props.getPropertiesByName("titleLine");
        bigTitleRectangleProperties = (RectProperties) props.getPropertiesByName("bigTitleRectangle");
        sourceCodeProperties = (SourceCodeProperties) props.getPropertiesByName("sourcecode");
        titleProperties = (TextProperties) props.getPropertiesByName("title");
        descProperties = (TextProperties) props.getPropertiesByName("description");
        smallTitleRectangleProperties = (RectProperties) props.getPropertiesByName("smallTitleRectangle");
        joinType = (Integer)primitives.get("joinType");
        outroProperties = (TextProperties) props.getPropertiesByName("outro");
        askQuestion = (Boolean) primitives.get("askQuestion");
        tableA = (String[][])primitives.get("tableA");
        tableB = (String[][])primitives.get("tableB");
        start();
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getAlgorithmName() {
        return translator.translateMessage("algorithmName");
    }

    public String getName() {
        return translator.translateMessage("generatorName");
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return translator.getCurrentLocale();
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    public String getAnimationAuthor() {
        return "Adrian Wagener, Ivelin Ivanov";
    }

    public String getDescription() {
        return translator.translateMessage("description[0]") + "\n" + translator.translateMessage("description[1]") + "\n"
        		+ translator.translateMessage("description[2]") + "\n" + translator.translateMessage("description[3]") + "\n"
        		+ translator.translateMessage("description[4]") + "\n" + translator.translateMessage("description[5]") + "\n"
                + translator.translateMessage("description[6]") + "\n" + translator.translateMessage("description[7]") + "\n"
                + translator.translateMessage("description[8]") + "\n" + translator.translateMessage("description[9]") + "\n"
                + translator.translateMessage("description[10]");
    }

    public String getCodeExample() {
        return "    public String[][] crossJoin(String[][] arrayA, String[][] arrayB) {"
        		+ "\n"
        		+ "        String result[][] = new String[arrayA.length * arrayB.length][arrayA[0].length + arrayB[0].length];"
        		+ "\n"
        		+ "        System.arraycopy(arrayA[0], 0, result[0], 0, arrayA.length);"
        		+ "\n"
        		+ "        System.arraycopy(arrayB[0], 0, result[0], arrayA[0].length, arrayA.length);"
        		+ "\n"
        		+ "        int resultRowIndex = 1;"
                + "\n"
                + "        for (int i = 1; i < arrayA.length; i++) {"
                + "\n"
                + "            for (int j = 1; j < arrayB.length; j++) {"
                + "\n"
                + "            System.arraycopy(arrayA[i], 0, result[resultRowIndex], 0, arrayA[i].length);"
                + "\n"
                + "            System.arraycopy(arrayB[j], 0, result[resultRowIndex], arrayA[i].length, arrayB[j].length);"
                + "\n"
                + "            resultRowIndex++;"
                + "\n"
                + "            }"
                + "\n"
                + "        }"
                + "\n"
                + "    return result;"
                + "}";
    }

    public void init() {
        // Language setup
        lang = new AnimalScript(translator.translateMessage("algorithmName"), "Adrian Wagener, Ivelin Ivanov", 1366, 768);
        lang.setStepMode(true);
        lang.setInteractionType(1024);
    }

    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        String errormsg = "";
        boolean valid = true;
        if (joinType < 1 || joinType > 3) {
        	valid = false;
        	errormsg = errormsg + translator.translateMessage("parseError[0]");
        } else if (tableA != null) {
        	for (int i = 0; i < tableA[0].length; i++) {
        		if (tableA[0][i] == null) {
        			valid = false;
        			errormsg = errormsg + translator.translateMessage("parseError[1]");
        		}
        	}
        } else if (tableB != null) {
        	for (int i = 0; i < tableB[0].length; i++) {
        		if (tableB[0][i] == null) {
        			valid = false;
        			errormsg = errormsg + translator.translateMessage("parseError[1]");
        		}
        	}
        }
        if (!valid)
            showErrorWindow(errormsg);
        return valid;
    }

    /************************************************************************************
     *************************** Starting the Animation *********************************
     ************************************************************************************/
    public void start() {
        // titleProperties
        Font fontTitle = (Font) titleProperties.get("font");
        fontTitle = fontTitle.deriveFont(1, 36);
        titleProperties.set("font", fontTitle);
        switch (joinType) {
        	case 1:
        		title = lang.newText(new Coordinates(50, 30), "Cross-Join", "title", null, titleProperties);
        		break;
        	case 2:
        		title = lang.newText(new Coordinates(50, 30), "Union-Join", "title", null, titleProperties);
        		break;
        	case 3:
        		title = lang.newText(new Coordinates(50, 30), "Natural-Join", "title", null, titleProperties);
        		break;
        }
        // horizontal line under the title
        Node[] node = new Node[]{new Offset(0, 10, "title", "SW"), new Offset(0, 10, "title", "SE")};
        hLine = lang.newPolyline(node, "title", null, titleLineProperties);
        hLine.hide();
        matProp = new MatrixProperties();
        matProp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        firstTable = lang.newStringMatrix(new Offset(0, 40, "title", "SW"), tableA, "firstTable", null, matProp);
        secondTable = lang.newStringMatrix(new Offset(120, 0, "firstTable", "NE"), tableB, "secondTable", null, matProp);
        firstTable.hide();
        secondTable.hide();
       
        // rectangle for the title
        lang.newRect(new Offset(-5, 10, "title", "SW"), new Offset(5, -10, "title", "NE"), "titleRect", null, smallTitleRectangleProperties);
        // description
        Font fontDesc = (Font) descProperties.get("font");
        fontDesc = fontDesc.deriveFont(0, 16);
        descProperties.set("font", fontDesc);
        lang.newText(new Offset(0, 20, "title", "SW"), translator.translateMessage("description[0]"), "desc[0]", null, descProperties);
        lang.newText(new Offset(0, 10, "desc[0]", "SW"), translator.translateMessage("description[1]"), "desc[1]", null, descProperties);
        lang.newText(new Offset(0, 10, "desc[1]", "SW"), translator.translateMessage("description[2]"), "desc[2]", null, descProperties);
        lang.newText(new Offset(0, 10, "desc[2]", "SW"), translator.translateMessage("description[3]"), "desc[3]", null, descProperties);
        lang.newText(new Offset(0, 10, "desc[3]", "SW"), translator.translateMessage("description[4]"), "desc[4]", null, descProperties);
        lang.newText(new Offset(0, 10, "desc[4]", "SW"), translator.translateMessage("description[5]"), "desc[5]", null, descProperties);
        lang.newText(new Offset(0, 5, "desc[5]", "SW"), translator.translateMessage("description[6]"), "desc[6]", null, descProperties);
        lang.newText(new Offset(0, 5, "desc[6]", "SW"), translator.translateMessage("description[7]"), "desc[7]", null, descProperties);
        lang.newText(new Offset(0, 5, "desc[7]", "SW"), translator.translateMessage("description[8]"), "desc[8]", null, descProperties);
        // rectangle for the description
        lang.newRect(new Offset(-5, -10, "title", "NW"), new Offset(10, 120, "desc[4]", "SE"), "descRect", null, bigTitleRectangleProperties);
        // Sourcecode
        Font fontSourcecode = (Font) sourceCodeProperties.get("font");
        fontSourcecode = fontSourcecode.deriveFont(0, 16);
        sourceCodeProperties.set("font", fontSourcecode);
        sc = lang.newSourceCode(new Offset(0, 100, "firstTable", "SW"), "sourceCode", null, sourceCodeProperties);
        switch (joinType) {
        	//Cross-Join
        	case 1:        
        		sc.addCodeLine("public String[][] crossJoin(String[][] arrayA, String[][] arrayB) {", null, 0, null);	//0
        		sc.addCodeLine("String result[][] = new String[((arrayA.length - 1) * (arrayB.length - 1)) + 1][arrayA[0].length + arrayB[0].length];", null, 1, null);                                            //1
        		sc.addCodeLine("System.arraycopy(arrayA[0], 0, result[0], 0, arrayA.length);", null, 1, null);			//2
        		sc.addCodeLine("System.arraycopy(arrayB[0], 0, result[0], arrayA[0].length, arrayA.length);", null, 1, null);                                      //3
        		sc.addCodeLine("int resultRowIndex = 1;", null, 1, null);												//4
        		sc.addCodeLine("for (int i = 1; i < arrayA.length; i++) {", null, 1, null);								//5
        		sc.addCodeLine("for (int j = 1; j < arrayB.length; j++) {", null, 2, null);								//6
        		sc.addCodeLine("System.arraycopy(arrayA[i], 0, result[resultRowIndex], 0, arrayA[i].length);", null, 3, null);	//7
        		sc.addCodeLine("System.arraycopy(arrayB[j], 0, result[resultRowIndex], arrayA[i].length, arrayB[j].length);", null, 3, null);//8
        		sc.addCodeLine("resultRowIndex++;", null, 3, null);														//9
        		sc.addCodeLine("}", null, 2, null);																		//10
        		sc.addCodeLine("}", null, 1, null);																		//11
        		sc.addCodeLine("return result;", null, 1, null);														//12
        		sc.addCodeLine("}", null, 0, null);																		//13
        		break;
        	case 2:
        		sc.addCodeLine("public String[][] unionJoin(String[][] arrayA, String[][] arrayB) {", null, 0, null);	//0
        		sc.addCodeLine("String result[][] = new String[((arrayA.length - 1) + (arrayB.length - 1)) + 1][arrayA[0].length + arrayB[0].length];", null, 1, null);                                            //1
        		sc.addCodeLine("System.arraycopy(arrayA[0], 0, result[0], 0, arrayA.length);", null, 1, null);			//2
        		sc.addCodeLine("System.arraycopy(arrayB[0], 0, result[0], arrayA[0].length, arrayA.length);", null, 1, null);                                      //3
        		sc.addCodeLine("int resultRowIndex = 1;", null, 1, null);												//4
        		sc.addCodeLine("for (int i = 1; i < arrayA.length; i++) {", null, 1, null);								//5
        		sc.addCodeLine("System.arraycopy(arrayA[i], 0, result[resultRowIndex], 0, arrayA[i].length);", null, 2, null);								//6
        		sc.addCodeLine("resultRowIndex++;", null, 2, null);														//7
        		sc.addCodeLine("}", null, 1, null);																		//8
        		sc.addCodeLine("for (int j = 1; j < arrayB.length; j++) {", null, 1, null);														//9
        		sc.addCodeLine("System.arraycopy(arrayB[j], 0, result[resultRowIndex], arrayA[0].length, arrayB[j].length);", null, 2, null);																		//10
        		sc.addCodeLine("resultRowIndex++;", null, 2, null);														//11
        		sc.addCodeLine("}", null, 1, null);																		//12
        		sc.addCodeLine("return result;", null, 1, null);														//13
        		sc.addCodeLine("}", null, 0, null);																		//14
        		break;
        	case 3:
        		sc.addCodeLine("public String[][] naturalJoin(String[][] arrayA, String[][] arrayB) {", null, 0, null);	//0
        		sc.addCodeLine("ArrayList<Integer> commonColumnsA = new ArrayList<Integer>();", null, 1, null);			//1
        		sc.addCodeLine("ArrayList<Integer> commonColumnsB = new ArrayList<Integer>();", null, 1, null);			//2
        		sc.addCodeLine("ArrayList<String[]> tempTable = new ArrayList<String[]>();", null, 1, null);			//3
        		sc.addCodeLine("for (int i = 0; i < arrayA[0].length; i++) {", null, 1, null);							//4
        		sc.addCodeLine("for (int j = 0; j < arrayB[0].length; j++) {", null, 2, null);							//5
        		sc.addCodeLine("if (arrayA[0][i].equals(arrayB[0][j])) {", null, 3, null);								//6
        		sc.addCodeLine("commonColumnsA.add(i);", null, 4, null);												//7
        		sc.addCodeLine("commonColumnsB.add(j);}}}", null, 4, null);												//8
        		sc.addCodeLine("int numberOfColumns = arrayA[0].length + arrayB[0].length - commonColumnsA.size();", null, 1, null); //9
        		sc.addCodeLine("int index = arrayA[0].length;", null, 1, null);											//10
        		sc.addCodeLine("String[] currentRow = new String[numberOfColumns];", null, 1, null);					//11
        		sc.addCodeLine("System.arraycopy(arrayA[0], 0, currentRow, 0, arrayA[0].length);", null, 1, null);		//12
        		sc.addCodeLine("for (int i = 0; i < arrayB[0].length; i++) {", null, 1, null);							//13
        		sc.addCodeLine("if (!commonColumnsB.contains(i)) {", null, 2, null);									//14
        		sc.addCodeLine("currentRow[index] = arrayB[0][i];", null, 3, null);										//15
        		sc.addCodeLine("index++;}}", null, 3, null);															//16
        		sc.addCodeLine("tempTable.add(currentRow);", null, 1, null);											//17
        		sc.addCodeLine("for (int i = 1; i < arrayA.length; i++) {", null, 1, null);								//18
        		sc.addCodeLine("for (int j = 1; j < arrayB.length; j++) {", null, 2, null);								//19
        		sc.addCodeLine("boolean equalCommonAttributes = true;", null, 3, null);									//20
        		sc.addCodeLine("for (int k = 0; k < commonColumnsA.size(); k++) {", null, 3, null);						//21
        		sc.addCodeLine("if (!arrayA[i][commonColumnsA.get(k)].equals(arrayB[j][commonColumnsB.get(k)])) {", null, 4, null); //22
        		sc.addCodeLine("equalCommonAttributes = false;}}", null, 5, null);										//23
        		sc.addCodeLine("if (equalCommonAttributes) {", null, 3, null);											//24
        		sc.addCodeLine("currentRow = new String[numberOfColumns];", null, 4, null);								//25
        		sc.addCodeLine("System.arraycopy(arrayA[i], 0, currentRow, 0, arrayA[i].length);", null, 4, null);		//26
        		sc.addCodeLine("index = arrayA[i].length;", null, 4, null);												//27
        		sc.addCodeLine("for (int l = 0; l < arrayB[j].length; l++) {", null, 4, null);							//28
        		sc.addCodeLine("if (!commonColumnsB.contains(l)) {", null, 5, null);									//29
        		sc.addCodeLine("currentRow[index] = arrayB[j][l];", null, 6, null);										//30
        		sc.addCodeLine("index++;}}", null, 6, null);															//31
        		sc.addCodeLine("tempTable.add(currentRow);}}}", null, 4, null);											//32
        		sc.addCodeLine("String[][] result = new String[tempTable.size()][];", null, 1, null);					//33
        		sc.addCodeLine("for (int i = 0; i < result.length; i++) {", null, 1, null);								//34
        		sc.addCodeLine("result[i] = tempTable.get(i);}", null, 2, null);										//35
        		sc.addCodeLine("return result;}", null, 1, null);														//36
        }
        sc.hide();
        lang.nextStep();
        lang.hideAllPrimitives();
        title.show();
        hLine.show();
        lang.nextStep();
        firstTable.show();
        secondTable.show();
        lang.nextStep(translator.translateMessage("sourcecode"));
        sc.show();
        lang.nextStep(translator.translateMessage("animationstart"));
        // highlight the whole source code
        for (int i = 0; i < sc.length(); i++)
            sc.highlight(i);
        lang.nextStep();
        // unhighlight whole code (except 0st and last line) and whole graph except root
        for (int i = 1; i < sc.length() - 1; i++)
            sc.unhighlight(i);
        lang.nextStep();
        // ask question
        r = rand.nextInt(3);
        lastAskedQuestion = r;
        ask(r);
        // Call the algorithm
        switch (joinType) {
        	case 1:
        		crossJoin(tableA, tableB);
        		break;
        	case 2:
        		unionJoin(tableA, tableB);
        		break;
        	case 3:
        		naturalJoin(tableA, tableB);
        		break;
        }
        // Make sure to not ask the same question as in the beginning
        while (lastAskedQuestion == r) {
            r = rand.nextInt(3);
        }
        ask(r);
        lang.nextStep(translator.translateMessage("summary"));
        //lang.hideAllPrimitives();
        sc.hide();
        title.show();
        hLine.show();
        // Outro
        Font fontOutro = (Font) outroProperties.get("font");
        fontOutro = fontOutro.deriveFont(0, 16);
        outroProperties.set("font", fontOutro);
        lang.nextStep();
        // First Outro
        out[0] = lang.newText(new Offset(0, 100, "firstTable", "SW"), translator.translateMessage("outro[0]"), "out[0]", null, outroProperties);
        out[1] = lang.newText(new Offset(0, 140, "firstTable", "SW"), translator.translateMessage("outro[1]"), "out[1]", null, outroProperties);
        out[2] = lang.newText(new Offset(0, 180, "firstTable", "SW"), translator.translateMessage("outro[2]"), "out[2]", null, outroProperties);
        out[3] = lang.newText(new Offset(0, 220, "firstTable", "SW"), translator.translateMessage("outro[3]"), "out[3]", null, outroProperties);
        out[4] = lang.newText(new Offset(0, 260, "firstTable", "SW"), translator.translateMessage("outro[4]"), "out[4]", null, outroProperties);
        // outro rectangle
        RectProperties rectOutProps = new RectProperties();
        rectOutProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        outRect[0] = lang.newRect(new Offset(-10, -10, "out[0]", "NW"), new Offset(10, 10, "out[4]", "SE"), "outRect", null, rectOutProps);
        lang.nextStep();
    }

    /************************************************************************************
     ***************************** Cross Join *******************************************
     ************************************************************************************/
    public String[][] crossJoin(String[][] arrayA, String[][] arrayB) {
    	sc.unhighlight(0);
    	sc.unhighlight(sc.length() - 1);
    	sc.highlight(1);
       	String result[][] = new String[((arrayA.length - 1) * (arrayB.length - 1)) + 1][arrayA[0].length + arrayB[0].length];
       	int resLength = result[0].length;
    	createdTable = lang.newStringMatrix(new Offset(240, -100, "secondTable", "NE"), result, "createdTable", null, matProp);
        createdTable.show();
        lang.nextStep();
        //Copy first row of both tables into the resulting one
        //The first row in all tables consists of the column labels
        sc.toggleHighlight(1, 2);
        sc.highlight(3);
        System.arraycopy(arrayA[0], 0, result[0], 0, arrayA[0].length);
        System.arraycopy(arrayB[0], 0, result[0], arrayA[0].length, arrayB[0].length);
        for (int i = 0; i < result[0].length; i++) {
        	createdTable.put(0, i, result[0][i], null, null);
        }
        createdTable.highlightElemColumnRange(0, 0, resLength - 1, null, null);
        lang.nextStep();
		int resultRowIndex = 1;
        sc.unhighlight(3);
		sc.toggleHighlight(2, 4);
		createdTable.unhighlightElemColumnRange(0, 0, resLength - 1, null, null);
		createdTable.highlightElemColumnRange(1, 0, resLength - 1, null, null);
		lang.nextStep();
		sc.toggleHighlight(4, 5);
		sc.highlight(6);
		sc.highlight(7);
		sc.highlight(8);
		sc.highlight(9);
		sc.highlight(10);
		sc.highlight(11);
		for (int i = 1; i < arrayA.length; i++) {
			firstTable.highlightElemColumnRange(i, 0, arrayA[i].length - 1, null, null);
			for (int j = 1; j < arrayB.length; j++) {
				secondTable.highlightElemColumnRange(j, 0, arrayB[j].length - 1, null, null);
				System.arraycopy(arrayA[i], 0, result[resultRowIndex], 0, arrayA[i].length);
				System.arraycopy(arrayB[j], 0, result[resultRowIndex], arrayA[i].length, arrayB[j].length);
				for (int k = 0; k < result[i].length; k++) {
		        	createdTable.put(resultRowIndex, k, result[resultRowIndex][k], null, null);
		        }
				createdTable.highlightElemColumnRange(resultRowIndex, 0, resLength - 1, null, null);
				lang.nextStep();
				resultRowIndex++;
				createdTable.unhighlightElemColumnRange(resultRowIndex - 1, 0, resLength - 1, null, null);
				secondTable.unhighlightElemColumnRange(j, 0, arrayB[j].length - 1, null, null);
			}
			firstTable.unhighlightElemColumnRange(i, 0, arrayA[i].length - 1, null, null);
		}
		sc.unhighlight(5);
		sc.unhighlight(6);
		sc.unhighlight(7);
		sc.unhighlight(8);
		sc.unhighlight(9);
		sc.unhighlight(10);
		sc.unhighlight(11);
		sc.highlight(12);
		return result;
	}
    
    /************************************************************************************
     ***************************** Union Join *******************************************
     ************************************************************************************/
    public String[][] unionJoin(String[][] arrayA, String[][] arrayB) {
    	RectProperties rectCodeComment = new RectProperties();
        rectCodeComment.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	sc.unhighlight(0);
    	sc.unhighlight(sc.length() - 1);
    	sc.highlight(1);
       	String result[][] = new String[((arrayA.length - 1) + (arrayB.length - 1)) + 1][arrayA[0].length + arrayB[0].length];
       	int resLength = result[0].length;
    	createdTable = lang.newStringMatrix(new Offset(240, 0, "secondTable", "NE"), result, "createdTable", null, matProp);
        createdTable.show();
        lang.nextStep();
        //Copy first row of both tables into the resulting one
        //The first row in all tables consists of the column labels
        sc.toggleHighlight(1, 2);
        sc.highlight(3);
        codeComment = lang.newText(new Offset(50, 50, "sourceCode", "NE"), translator.translateMessage("codeCommentU[0]"), "codeComment", null, outroProperties);
        commentRect[0] = lang.newRect(new Offset(-10, -10, "codeComment", "NW"), new Offset(60, 10, "codeComment", "SE"), "commentRect[0]", null, rectCodeComment);
        System.arraycopy(arrayA[0], 0, result[0], 0, arrayA[0].length);
        System.arraycopy(arrayB[0], 0, result[0], arrayA[0].length, arrayB[0].length);
        for (int i = 0; i < result[0].length; i++) {
        	createdTable.put(0, i, result[0][i], null, null);
        }
        createdTable.highlightElemColumnRange(0, 0, resLength - 1, null, null);
        lang.nextStep();
		int resultRowIndex = 1;
        sc.unhighlight(3);
		sc.toggleHighlight(2, 4);
		createdTable.unhighlightElemColumnRange(0, 0, resLength - 1, null, null);
		createdTable.highlightElemColumnRange(1, 0, resLength - 1, null, null);
		lang.nextStep();
		sc.toggleHighlight(4, 5);
		sc.highlight(6);
		sc.highlight(7);
		sc.highlight(8);
		commentRect[0].hide();
		codeComment.setText(translator.translateMessage("codeCommentU[1]"), null, null);
        commentRect[1] = lang.newRect(new Offset(-10, -10, "codeComment", "NW"), new Offset(60, 10, "codeComment", "SE"), "commentRect[1]", null, rectCodeComment);
		for (int i = 1; i < arrayA.length; i++) {
			firstTable.highlightElemColumnRange(i, 0, arrayA[i].length - 1, null, null);
			System.arraycopy(arrayA[i], 0, result[resultRowIndex], 0, arrayA[i].length);
			for (int k = 0; k < result[0].length; k++) {
	        	createdTable.put(resultRowIndex, k, result[resultRowIndex][k], null, null);
	        }
			createdTable.highlightElemColumnRange(resultRowIndex, 0, resLength - 1, null, null);
			lang.nextStep();
			resultRowIndex++;
			createdTable.unhighlightElemColumnRange(resultRowIndex - 1, 0, resLength - 1, null, null);
			firstTable.unhighlightElemColumnRange(i, 0, arrayA[i].length - 1, null, null);
		}
		sc.unhighlight(5);
		sc.unhighlight(6);
		sc.unhighlight(7);
		sc.unhighlight(8);
		sc.highlight(9);
		sc.highlight(10);
		sc.highlight(11);
		sc.highlight(12);
		commentRect[1].hide();
		codeComment.setText(translator.translateMessage("codeCommentU[2]"), null, null);
        commentRect[2] = lang.newRect(new Offset(-10, -10, "codeComment", "NW"), new Offset(60, 10, "codeComment", "SE"), "commentRect[1]", null, rectCodeComment);

		for (int j = 1; j < arrayB.length; j++) {
			secondTable.highlightElemColumnRange(j, 0, arrayB[j].length - 1, null, null);
			System.arraycopy(arrayB[j], 0, result[resultRowIndex], arrayA[0].length, arrayB[j].length);
			for (int k = 0; k < result[0].length; k++) {
		        createdTable.put(resultRowIndex, k, result[resultRowIndex][k], null, null);
		    }
			createdTable.highlightElemColumnRange(resultRowIndex, 0, resLength - 1, null, null);
			lang.nextStep();
			resultRowIndex++;
			createdTable.unhighlightElemColumnRange(resultRowIndex - 1, 0, resLength - 1, null, null);
			secondTable.unhighlightElemColumnRange(j, 0, arrayB[j].length - 1, null, null);
		}		
		sc.unhighlight(9);
		sc.unhighlight(10);
		sc.unhighlight(11);
		sc.unhighlight(12);
		sc.highlight(13);
		commentRect[2].hide();
		codeComment.hide();
		return result;
	}
	    
    /************************************************************************************
     ***************************** Natural Join *****************************************
     ************************************************************************************/
	public String[][] naturalJoin(String[][] arrayA, String[][] arrayB) {
        RectProperties rectCodeComment = new RectProperties();
        rectCodeComment.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		sc.unhighlight(0);
    	sc.unhighlight(sc.length() - 1);
    	sc.highlight(1);
    	sc.highlight(2);
    	sc.highlight(3);
    	sc.highlight(4);
    	sc.highlight(5);
    	sc.highlight(6);
    	sc.highlight(7);
    	sc.highlight(8);
		//List of indices of all common columns in the first table
		ArrayList<Integer> commonColumnsA = new ArrayList<Integer>();
		//List of indices of all common columns in the second table
		ArrayList<Integer> commonColumnsB = new ArrayList<Integer>();
		//Temporary result table, because the end size is not known yet and thus a variable array is needed
		ArrayList<String[]> tempTable = new ArrayList<String[]>();
		//Populate the lists of indices
		for (int i = 0; i < arrayA[0].length; i++) {
			for (int j = 0; j < arrayB[0].length; j++) {
				if (arrayA[0][i].equals(arrayB[0][j])) {
					commonColumnsA.add(i);
					commonColumnsB.add(j);
				}
			}
		}
		codeComment = lang.newText(new Offset(50, 50, "sourceCode", "NE"), translator.translateMessage("codeComment[0]"), "codeComment", null, outroProperties);
        commentRect[0] = lang.newRect(new Offset(-10, -10, "codeComment", "NW"), new Offset(60, 10, "codeComment", "SE"), "commentRect[0]", null, rectCodeComment);
		lang.nextStep();
		sc.unhighlight(1);
		sc.unhighlight(2);
		sc.unhighlight(3);
		sc.unhighlight(4);
		sc.unhighlight(5);
		sc.unhighlight(6);
		sc.unhighlight(7);
		sc.unhighlight(8);
		
		
		sc.highlight(9);
    	sc.highlight(10);
    	sc.highlight(11);
    	sc.highlight(12);
    	sc.highlight(13);
    	sc.highlight(14);
    	sc.highlight(15);
    	sc.highlight(16);
    	sc.highlight(17);
		
		//numberOfColumns is the number of columns in the resulting table
		int numberOfColumns = arrayA[0].length + arrayB[0].length - commonColumnsA.size();

		/* *****************************************************************************
		 * "EMPTY"/DUPLICATE CODE, USED ONLY TO DETERMINE THE SIZE OF THE RESULTING TABLE
		 * CHANGES HERE WILL NOT AFFECT THE END RESULT, EXCEPT THE SIZE OF THE TABLE
		 * *****************************************************************************/
		int resultHeight = 1;
		for (int i = 1; i < arrayA.length; i++) {
			for (int j = 1; j < arrayB.length; j++) {
				//Check if all identical columns contain the same element for the current row
				boolean equalCommonAttributes = true;
				for (int k = 0; k < commonColumnsA.size(); k++) {
					if (!arrayA[i][commonColumnsA.get(k)].equals(arrayB[j][commonColumnsB.get(k)])) {
						equalCommonAttributes = false;
					}
				}
				//If the common attributes are equal, copy the current rows
				if (equalCommonAttributes) {
					resultHeight++;
				}
			}
		}
		/* **********************
		 * END OF DUPLICATE CODE
		 * **********************/
		createdTable = lang.newStringMatrix(new Offset(240, 0, "secondTable", "NE"), new String[resultHeight][numberOfColumns], "createdTable", null, matProp);
		for (int i = 0; i < arrayA[0].length; i++) {
			createdTable.put(0, i, arrayA[0][i], null, null);
		}
		createdTable.show();

		int index = arrayA[0].length;
		String[] currentRow = new String[numberOfColumns];
		//Copy the column names to the new table
		System.arraycopy(arrayA[0], 0, currentRow, 0, arrayA[0].length);
		for (int i = 0; i < arrayB[0].length; i++) {
			if (!commonColumnsB.contains(i)) {
				currentRow[index] = arrayB[0][i];
				createdTable.put(0, index, arrayB[0][i], null, null);
				index++;
			}
		}
		tempTable.add(currentRow);
		commentRect[0].hide();
		codeComment.setText(translator.translateMessage("codeComment[1]"), null, null);
        commentRect[1] = lang.newRect(new Offset(-10, -10, "codeComment", "NW"), new Offset(60, 10, "codeComment", "SE"), "commentRect[1]", null, rectCodeComment);
        createdTable.highlightElemColumnRange(0, 0, numberOfColumns - 1, null, null);
        lang.nextStep();
		
		sc.unhighlight(9);
		sc.unhighlight(10);
		sc.unhighlight(11);
		sc.unhighlight(12);
		sc.unhighlight(13);
		sc.unhighlight(14);
		sc.unhighlight(15);
		sc.unhighlight(16);
		sc.unhighlight(17);
		
		
		sc.highlight(18);
    	sc.highlight(19);
    	sc.highlight(20);
    	sc.highlight(21);
    	sc.highlight(22);
    	sc.highlight(23);
    	sc.highlight(24);
    	sc.highlight(25);
    	sc.highlight(26);
    	sc.highlight(27);
    	sc.highlight(28);
    	sc.highlight(29);
    	sc.highlight(30);
    	sc.highlight(31);
    	sc.highlight(32);
    	commentRect[1].hide();
    	codeComment.setText(translator.translateMessage("codeComment[2]"), null, null);
        commentRect[2] = lang.newRect(new Offset(-10, -10, "codeComment", "NW"), new Offset(60, 10, "codeComment", "SE"), "commentRect[2]", null, rectCodeComment);
    	createdTable.unhighlightElemColumnRange(0, 0, numberOfColumns - 1, null, null);
		//Copy all rows with identical common column attributes to the new table
		int resultRowIndex = 1;
		for (int i = 1; i < arrayA.length; i++) {
			for (int j = 1; j < arrayB.length; j++) {
				//Check if all identical columns contain the same element for the current row
				boolean equalCommonAttributes = true;
				for (int k = 0; k < commonColumnsA.size(); k++) {
					if (!arrayA[i][commonColumnsA.get(k)].equals(arrayB[j][commonColumnsB.get(k)])) {
						equalCommonAttributes = false;
					}
				}
				//If the common attributes are equal, copy the current rows
				if (equalCommonAttributes) {
					currentRow = new String[numberOfColumns];
					//Copy complete row of the first table
					System.arraycopy(arrayA[i], 0, currentRow, 0, arrayA[i].length);
					index = arrayA[i].length;
					//Copy only the "unique" columns of the second table
					for (int l = 0; l < arrayB[j].length; l++) {
						if (!commonColumnsB.contains(l)) {
							currentRow[index] = arrayB[j][l];
							index++;
						}
					}
					firstTable.highlightElemColumnRange(i, 0, arrayA[i].length - 1, null, null);
					
					secondTable.highlightElemColumnRange(j, 0, arrayB[j].length - 1, null, null);				
					createdTable.highlightElemColumnRange(resultRowIndex, 0, numberOfColumns - 1, null, null);
					for (int z = 0; z < currentRow.length; z++) {
						createdTable.put(resultRowIndex, z, currentRow[z], null, null);
					}
					tempTable.add(currentRow);
					lang.nextStep();
					secondTable.unhighlightElemColumnRange(j, 0, arrayB[j].length - 1, null, null);
					firstTable.unhighlightElemColumnRange(i, 0, arrayA[i].length - 1, null, null);
					createdTable.unhighlightElemColumnRange(resultRowIndex, 0, numberOfColumns - 1, null, null);
					resultRowIndex++;
				}
			}
		}
		sc.unhighlight(18);
    	sc.unhighlight(19);
    	sc.unhighlight(20);
    	sc.unhighlight(21);
    	sc.unhighlight(22);
    	sc.unhighlight(23);
    	sc.unhighlight(24);
    	sc.unhighlight(25);
    	sc.unhighlight(26);
    	sc.unhighlight(27);
    	sc.unhighlight(28);
    	sc.unhighlight(29);
    	sc.unhighlight(30);
    	sc.unhighlight(31);
    	sc.unhighlight(32);
		
    	sc.highlight(33);
    	sc.highlight(34);
    	sc.highlight(35);
    	
    	commentRect[2].hide();
    	codeComment.setText(translator.translateMessage("codeComment[3]"), null, null);
        commentRect[3] = lang.newRect(new Offset(-10, -10, "codeComment", "NW"), new Offset(60, 10, "codeComment", "SE"), "commentRect[3]", null, rectCodeComment);
		String[][] result = new String[tempTable.size()][];
		for (int i = 0; i < result.length; i++) {
			result[i] = tempTable.get(i);
		}
		lang.nextStep();
		codeComment.hide();
		commentRect[3].hide();
		return result;
	}
	
    private void ask(int random) {
    	if (!askQuestion) return;
        MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel(Integer.toString(rand.nextInt(150) + lastAskedQuestion * rand.nextInt(100)));
        switch (random) {
            case 0:
                question.setPrompt(translator.translateMessage("question[0]"));
                question.addAnswer(translator.translateMessage("answerYes"), 0, translator.translateMessage("questionFeedback[0.1]"));
                question.addAnswer(translator.translateMessage("answerNo"), 1, translator.translateMessage("questionFeedbackT[0.0]"));
                break;
            case 1:
                question.setPrompt(translator.translateMessage("question[1]"));
                question.addAnswer(translator.translateMessage("answerYes"), 0, translator.translateMessage("questionFeedback[1.1]"));
                question.addAnswer(translator.translateMessage("answerNo"), 1, translator.translateMessage("questionFeedbackT[1.0]"));
                break;
            case 2:
                question.setPrompt(translator.translateMessage("question[2]"));
                question.addAnswer(translator.translateMessage("answerYes"), 0, translator.translateMessage("questionFeedback[2.1]"));
                question.addAnswer(translator.translateMessage("answerNo"), 1, translator.translateMessage("questionFeedbackT[2.0]"));
                break;
        }
        lang.addMCQuestion(question);
    }

    /************************************************************************************
     ******************************** Error Window **************************************
     ************************************************************************************/
    private void showErrorWindow(String error) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), error, "Error", 0);
    }
}