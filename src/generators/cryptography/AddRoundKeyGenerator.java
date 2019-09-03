package generators.cryptography;

import generators.cryptography.helpers.AddRoundKey;
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

public class AddRoundKeyGenerator implements ValidatingGenerator {
	
    private Language lang;
    
    //user input values
    private MatrixProperties matrixProps;
    
    private MatrixProperties roundKeyProps;
    
    private SourceCodeProperties sourceCodeProps;
    
    private int[][] roundKey;
    
    private int[][] matrix;
    
	//the displayed description
	private static final String DESCRIPTION = "AddRoundKey is a step of the Rijndael-Chiffre. For AES this step gets"
			+ "an AES state matrix (consisting of 16 byte values) and a 16 byte round"
			+ "key as input. The first column of the matrix is XORed with the first 4 bytes"
			+ "the left. Similarly, the third and fourth rows are shifted by offsets"
			+ "of the key. The resulting bytes are the new values for the first column."
			+ "The next column gets XORed with the following 4 bytes of the key and so on."
			+ "As this animation visualizes AddRoundKey for AES (Advanced Encryption Standard), "
		    + "it requires the matrix to contain 16 bytes and the roundKey likewise. Therefore the input matrix must be "
		    + "a 4x4 matrix containung int values between 0 and 255 and the roundKey a vector containing values in the same range.";

	//the displayed code example
	private static final String SOURCE_CODE = 
			"private void addRoundKey(int[][] matrix, int[] roundKey){"
			+"\n	for(int column = 0; column < 4; column++){"
			+"\n		int[] currentColumn = new int[4];"
			+"\n		currentColumn[0] = matrix[0][column];"
			+"\n		currentColumn[1] = matrix[1][column];"
			+"\n		currentColumn[2] = matrix[2][column];"
			+"\n		currentColumn[3] = matrix[3][column];"
			+"\n		int []currentKey = new int[4];"
			+"\n		currentKey[0] = roundKey[4*column];"
			+"\n		currentKey[1] = roundKey[4*column+1];"
			+"\n		currentKey[2] = roundKey[4*column+2];"
			+"\n		currentKey[3] = roundKey[4*column+3];"
			+"\n		for(int row = 0; row < 4; row++){;"
			+"\n			resultVector[row]=currentColumn[row] ^ currentKey[row];"
			+"\n		}"
			+"\n		matrix[0][column]=resultVector[0];"
			+"\n		matrix[1][column]=resultVector[1];"
			+"\n		matrix[2][column]=resultVector[2];"
			+"\n		matrix[3][column]=resultVector[3];"
			+"\n	}"
			+"\n}";


    public void init(){
        lang = new AnimalScript("AddRoundKey [en]", "Stefan Kaesdorf,Marco Drebing", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        matrixProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
        roundKeyProps = (MatrixProperties)props.getPropertiesByName("roundKeyProps");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        
        roundKey = (int[][])primitives.get("roundKey");
        matrix = (int[][])primitives.get("matrix");
        AddRoundKey addRoundKey = new AddRoundKey(lang, matrixProps, roundKeyProps, sourceCodeProps);
        addRoundKey.addRoundKey(matrix, roundKey);
        return lang.toString();
    }

    public String getName() {
        return "AddRoundKey [en]";
    }

    public String getAlgorithmName() {
        return "AddRoundKey";
    }

    public String getAnimationAuthor() {
        return "Stefan Kaesdorf, Marco Drebing";
    }

    public String getDescription(){
        return DESCRIPTION;
    }

    public String getCodeExample(){
        return SOURCE_CODE;
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.US;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer properties,
			Hashtable<String, Object> primitives) throws IllegalArgumentException {
		
		if(primitives.get("matrix")!=null&&primitives.get("matrix") instanceof int[][]){
			int[][]matrix = (int[][]) primitives.get("matrix");
			if(matrix.length==4&&matrix[0].length==4){
				for(int i = 0; i<4;i++){
					for(int j = 0; j<4;j++){
						if(!(0<=matrix[i][j]&&matrix[i][j]<=255)){
							return false;
						}
					}
				}
			}else{
				return false;
				}
		}else{
			return false;
		}
		
		if(primitives.get("roundKey")!=null&&primitives.get("roundKey") instanceof int[][]){
			int[][]matrix = (int[][]) primitives.get("matrix");
			if(matrix.length==4&&matrix[0].length==4){
				for(int i = 0; i<4;i++){
					for(int j = 0; j<4;j++){
						if(!(0<=matrix[i][j]&&matrix[i][j]<=255)){
							return false;
						}
					}
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
		return true;
	}

}