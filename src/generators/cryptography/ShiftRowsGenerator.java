package generators.cryptography;

import generators.cryptography.helpers.ShiftRows;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;

public class ShiftRowsGenerator implements ValidatingGenerator {
	
	//user input values
	private MatrixProperties matrixProps;
	
    private SourceCodeProperties sourceCodeProps;
    
    private int[][] Matrix;
	
	//variables representing the respective animal objects
	private Language lang;

	//variables for the main computation
	private ShiftRows shiftRows;

	//the displayed description
	private static final String DESCRIPTION ="The ShiftRows algorithm in general is part of the Rijndael Cipher. In"
			+"\nthis visualisation we want to show you how ShiftRows works as part of"
			+"\nthe AES (Advanced Encryption Standard) which is a special case of the"
			+"\nRijndael Cipher."
			+"\nThe ShiftRows step operates on the rows of the  AES statematrix; it"
			+"\nThe ShiftRows step operates on the rows of the matrix; it cyclically"
			+"\ncyclically shifts the bytes in each row by a certain offset. The first"
			+"\nrow is left unchanged. Each byte of the second row is shifted one to"
			+"\nthe left. Similarly, the third and fourth rows are shifted by offsets"
			+"\nof two and three respectively.";

	//the displayed code example
	private static final String SOURCE_CODE = 
			"private void shiftMatrix(int[][] matrix){"
			+ "\n	for(int i = 0; i < matrix.length; i++){"
			+ "\n		for(int j = i; j >= 0; j++){"
			+ "\n			matrix[i] = shiftLeft(matrix[i]);"
			+ "\n		}"
			+ "\n	}"
			+ "\n}"
			+"\n"
			+"\nprivate int[][] shiftLeft(int[][] matrix, int row) {"
			+"\n	for (int i = 0; i < row; i++) {"
			+"\n		int tmp = matrix[row][0];"
			+"\n		matrix[row][0] = matrix[row][1];"
			+"\n		matrix[row][1] = matrix[row][2];"
			+"\n		matrix[row][2] = matrix[row][3];"
			+"\n		matrix[row][3] = tmp;"
			+"\n	}"
			+"\n	return matrix;"
			+"\n}";
	


	@Override
	public void init(){
        lang = new AnimalScript("ShiftRows [en]", "Stefan Kaesdorf, Marco Drebing", 800, 600);
        lang.setStepMode(true);
    }

	@Override
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        matrixProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        Matrix = (int[][])primitives.get("Matrix");
        shiftRows = new ShiftRows(lang, matrixProps, sourceCodeProps);
        shiftRows.shiftRows(Matrix);
        return lang.toString();
    }

	@Override
    public String getName() {
        return "ShiftRows [en]";
    }

	@Override
    public String getAlgorithmName() {
        return "ShiftRows";
    }

	@Override
    public String getAnimationAuthor() {
        return "Stefan Kaesdorf, Marco Drebing";
    }

	@Override
    public String getDescription(){
        return DESCRIPTION;
    }

	@Override
    public String getCodeExample(){
        return SOURCE_CODE;
    }

	@Override
    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

	@Override
    public Locale getContentLocale() {
        return Locale.US;
    }

	@Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

	@Override
    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer properties,
			Hashtable<String, Object> primitives) throws IllegalArgumentException {
		if(primitives.get("Matrix")!=null&&primitives.get("Matrix") instanceof int[][]){
			int[][]matrix = (int[][]) primitives.get("Matrix");
			if(matrix.length==4&&matrix[0].length==4){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
