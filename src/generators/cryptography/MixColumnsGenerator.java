package generators.cryptography;

import generators.cryptography.helpers.MixColumns;
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

public class MixColumnsGenerator implements ValidatingGenerator {
    private Language lang;
    private MatrixProperties matrixProps;
    private int[][] stateMatrix;
    private SourceCodeProperties sourceCodeProps;

    public void init(){
        lang = new AnimalScript("MixColumns [en]", "Stefan Kaesdorf, Marco Drebing", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        matrixProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
        stateMatrix = (int[][])primitives.get("stateMatrix");
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
        MixColumns mixColumns = new MixColumns(lang, stateMatrix, matrixProps, sourceCodeProps);
        mixColumns.mixColumns();
        return lang.toString();
    }

    public String getName() {
        return "MixColumns [en]";
    }

    public String getAlgorithmName() {
        return "MixColumns";
    }

    public String getAnimationAuthor() {
        return "Stefan Kaesdorf, Marco Drebing";
    }

    public String getDescription(){

       return "The MixColumns is an operation of the Rijndael-Cipher. Each step computes " +
       "one column of the resultmatrix for the given statematrix. It takes four" +
       "bytes as input and outputs four bytes, where each input byte affects all" +
       "four output bytes. Along with the shift-rows step, it is the primary source" +
       "of diffusion in Rijndael." +
       "Each column is converted into a polynomial and is then multiplied with" +
       "the fixed MDS-Matrix. The result will be multiplied modulo the fixed" +
       "polynomial x⁸ + x⁴ + x³ + x + 1." +
       "As this animation visualizes MixColumns for AES (Advanced Encryption Standard), " +
       "it requires the matrix to contain 16 bytes. Therefore the input matrix must be " +
       "a 4x4 matrix containung int values between 0 and 255. In the animation these " +
       "values will be desplayed as hax values.";
    }

    public String getCodeExample(){

       return "mixColumns(Matrix hexMatrix){" +
       "\n for(int column = 0; column < 4; column++){" +
       "\n    Hex[] resultColumn = new Hex[4];" +
       "\n    for(int row = 0; row < 4; row++){" +
       "\n       hexValue = vectorMultplication(stateMatrix.column, mdsMatrix.row);" +
       "\n       resultColumn[row] = hexValue;" +
       "\n    }" +
       "\n    resultMatrix.addColumn(resultColumn);" +
       "\n }" +
       "\n}" +
       "\n" +
       "\n" +
       "\ncalculation(Column stateMatrix.column, Row mdsMatrix.row){" +
       "\n Hex[] result = new Hex[4];" +
       "\n for(int i = 0; i < 4; i++){" +
       "\n    statePolynom = stateMatrix.column[i].toPolynom;" +
       "\n    mdsPolynom = mdsMatrix.row[i].toPolynom;" +
       "\n    multipliedPolynom = statePolynom * mdsPolynom;" +
       "\n    resultPolynom = multiploedPolynom mod (x⁸ + x⁴ + x³ + x + 1);" +
       "\n    result[i] = resultPolynom.toHex;" +
       "\n }" +
       "\n return result[0] xor result[1] xor result[2] xor result[3];" +
       "\n}";
       
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
        return Generator.PSEUDO_CODE_OUTPUT;
    }

   @Override
   public boolean validateInput(AnimationPropertiesContainer properties,
         Hashtable<String, Object> primitives) throws IllegalArgumentException {
      boolean legal = false;
      Object obj = primitives.get("stateMatrix");
      if(obj != null && obj instanceof int[][]){
         int[][] matrix = (int[][]) obj;
         if(matrix.length == 4 && matrix[0].length == 4){
            for(int i = 0; i < 4; i++){
               for(int j = 0; j < 4; j++){
                  int k = matrix[i][j];
                  if(k >= 0 && k < 256){
                     legal = true;
                  }
                  else{
                     return false;
                  }
               }
            }
         }
      }
      return legal;
   }
}