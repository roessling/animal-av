package generators.maths;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;

public class QR implements Generator{

	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;

	/**
	 * the source code shown in the animation
	 */
	private SourceCode           src;
	
	/**
	  * Globally defined source code properties
	  */
	 private SourceCodeProperties sourceCodeProps;
	 
	 public QR(){
		init();
	 }
	 
	  public String getName() {
	    return "QR [EN]";
	  }

	  public String getAlgorithmName() {
	    return "QR [EN]";
	  }

	  public String getAnimationAuthor() {
	    return "Dan Le";
	  }

	  public String getDescription() {
	    return stringAppend(desc);
	  }

	  public String getCodeExample() {
	    return stringAppend(pseudoCode);
	  }

	  public String getFileExtension() {
	    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	  }

	  public Locale getContentLocale() {
	    return Locale.US;
	  }

	  public GeneratorType getGeneratorType() {
	    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	  }

	  public String getOutputLanguage() {
	    return Generator.PSEUDO_CODE_OUTPUT;
	  }
	  
	  private String stringAppend(String[] in){
		  StringBuffer sb = new StringBuffer();
		  for(String s :in){
			  sb.append(s+"\n");
		  }
		  return sb.toString();
	  }
	 
	/**
	 * Default constructor
	 * 
	 * @param l
	 *            the conrete language object used for creating output
	 */
	public QR(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
	}

	private static final String desc[] = {"In numerical linear algebra," ,
			"the QR algorithm is an eigenvalue algorithm: ",
			"that is, a procedure to calculate the eigenvalues and eigenvectors of a matrix. " ,
			"The QR transformation was developed in the late 1950s by John G.F. Francis (England) " ,
			"and by Vera N. Kublanovskaya (USSR), working independently." ,
			"The basic idea is to perform a QR decomposition, writing the matrix as a product of an orthogonal matrix " ,
			"and an upper triangular matrix, multiply the factors in the other order, and iterate.",
			"A(k) = Q(k) * R(k)",
			"A(k+1)= R(k) * Q(k)"};
	
	
	public void printDescription(){
	  	Text []descArray = new Text[desc.length];
	  	for (int i = 0; i < desc.length; i++) {
	  		descArray[i] = lang.newText(new Coordinates(10, 30 * (i + 3)), desc[i], "desc" + i, null);
	  		if (i < desc.length - 1) 
	  			lang.nextStep();
			else 
				lang.nextStep("Init");
	  	}
	  	for (int i = 0; i < desc.length; i++) 
	  		descArray[i].hide();
	  }

	private static final String pseudoCode[] = {
			"public void QR(double[][] A, int n)" // 0
			, "{" // 1
			, "  for(int i=0;i<n;i++){"//2
			, "   %% find Q, R : A = Q * R"//3
			, "    Q = gramSmith(A);"//4
			, "    R = Q * A;"//5
			, "    A = Q * R;"//6
			, "  }"//7
			, "}"//8
			, ""
			, ""
			, "private double[][] gramSmith(double[][] A)"//11
			, "{" // 12
			, "  double[][] u = transpose(A);"//13
			, "	 double[][] e = new double[A.length][A[0].length];"//14
			, "	 for (int j = 0; j < A[0].length; j++) {"//15
			, "	    for (int k = 0; k < j; k++) {"//16
			, "	        e[j] = sub(e[j], proj(u[j], e[k]));"//17
			, "	    }"//18
			, "     e[j] = add(e[j],u[j]);"//19
			, "     e[j] = normalize(e[j]);"//20
			, "	 }"//21
			, "}"//22
			, "return transpose(e);"}//23
			;
	
	void printSourceCode() {
	  	src = lang.newSourceCode(new Coordinates(10, 120), "SourceCode", null, sourceCodeProps);
	  	for (int i = 0; i < pseudoCode.length; i++) {
	  		src.addCodeLine(pseudoCode[i], null, 0, null);
	  	}
	  }

	private static final double[][] A = { { 1, 3, 0, 0 }, { 3, 2, 1, 0 },
			{ 0, 1, 3, 4 }, { 0, 0, 4, 1 } };
	//private static final double[][] B = { { 12, -51, 4 }, { 6, 167, -68 },
			//{ -4, 24, -41 } };


	private TextProperties       textProps;
	/**
	 */
	
	public String generate(AnimationPropertiesContainer props,
		      Hashtable<String, Object> primitives) {
		
		MatrixProperties mp = (MatrixProperties) props.getPropertiesByName("matrixProp");
		int n = (Integer)primitives.get("n");
		//DoubleMatrix A = (DoubleMatrix) primitives.get("Matrix");
		//double[][] mA = A.getData();
		
		int[][] m_A = (int[][]) primitives.get("Matrix");

		if(m_A.length != m_A[0].length){
			return lang.toString();
		}
		
		double[][] mA = new double[m_A.length][m_A[0].length];
		for(int i=0;i<m_A.length;i++){
			for(int j=0;j<m_A[0].length;j++){
				mA[i][j] = m_A[i][j];
			}
		}
		
		DisplayOptions display = null;
		
		setProperties();
		
		start(mA,n, display,mp);
		
		return lang.toString();
	}
	
	private DoubleMatrix matrix_Q,matrix_E;
	private Text index_j, index_k;
	public void start(double[][] A,int n, DisplayOptions display,MatrixProperties mp) {
		src = lang.newSourceCode(new Coordinates(390, 50), "sourceCode",
		        null, sourceCodeProps);
		
	    textProps = new TextProperties();
	    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.PLAIN, 16));
		double[][] C = A.clone();
		double[][] Q = C.clone(), R = C.clone();
	    Text index_i = lang.newText(new Coordinates(1200, 70),"", "___I", null, textProps);

	    index_j = lang.newText(new Coordinates(1200, 120),"", "___J", null, textProps);

	    index_k = lang.newText(new Coordinates(1200, 170),"", "___K", null, textProps);
	    
	    Text index_C = lang.newText(new Coordinates(370, 70),"A", "___A", null, textProps);
	    Text index_Q = lang.newText(new Coordinates(370, 270),"Q", "___Q", null, textProps);
	    Text index_R = lang.newText(new Coordinates(370, 470),"R", "___R", null, textProps);
	    Text index_E = lang.newText(new Coordinates(1170, 270),"E", "___E", null, textProps);
	    
	    double[][] E = new double[A.length][A[0].length];
	    matrix_Q = lang.newDoubleMatrix(new Coordinates(400, 270), Q, "Q", display,mp);
	    matrix_E = lang.newDoubleMatrix(new Coordinates(1200, 270), E, "E", display,mp);
	    DoubleMatrix matrix_R = lang.newDoubleMatrix(new Coordinates(400, 470), R, "R", display,mp);
	    matrix_E.hide();
	    matrix_Q.hide();
	    matrix_R.hide();
		index_i.show();
		index_j.hide();
		index_k.hide();
		
		index_C.show();
		index_Q.show();
		index_R.show();
		index_E.show();
		
		printSourceCode();
	    lang.nextStep();
	    lang.hideAllPrimitives();
		printDescription();

	    lang.hideAllPrimitives();
	    lang.nextStep();
	    DoubleMatrix matrix_C = lang.newDoubleMatrix(new Coordinates(400, 70), C, "C", display,mp);
	    matrix_C.show();
		printSourceCode();
		
		src.highlight(2);
		
		for (int k = 0; k < n; k++) {
			index_i.setText("i="+String.valueOf(k), null, null);
			src.highlight(4);
			Q = gramSmith(C);
			updateMatrix(matrix_Q,Q);
			matrix_Q.show();
			//matrix_Q.show();
			lang.nextStep();
			src.unhighlight(4);
			// debug(Q);
			// System.out.println();
			R = mul(transpose(Q), C);
			
			//matrix_R.show();
			for (int i = 0; i < R.length; i++) {
				for (int j = 0; j < i; j++)
					R[i][j] = 0;
			}

			updateMatrix(matrix_R,R);
			matrix_R.show();
			
			src.highlight(5);
			lang.nextStep();
			// debug(R);
			// System.out.println();
			C = mul(R, Q);
			updateMatrix(matrix_C, C);
			src.unhighlight(5);
			src.highlight(6);
			lang.nextStep();
			src.unhighlight(6);
		}
		src.unhighlight(2);
		//debug(C);
		for (int i = 0; i < R.length; i++) {
			matrix_C.highlightCell(i, i, null, null);
			//matrix_C.highlightElem(i, i, null, null);
			lang.nextStep();
		}
	}
	
	private void updateMatrix(DoubleMatrix C, double[][] newMatrix){
	  if (C == null || C.getNrRows() == 0)
	    return;
//	  double[][] A = C.getData();
		for (int j = 0; j < C.getNrCols(); j++) {
			for (int i = 0; i < C.getNrRows(); i++) {
				C.put(i, j, newMatrix[i][j], null, null);
			}
		}
	}

	@SuppressWarnings("unused")
	private static void debug(double[][] A) {
		for (int j = 0; j < A[0].length; j++) {
			for (int i = 0; i < A.length; i++) {
				System.out.print(A[j][i] + " ");
			}
			System.out.println();
		}
	}

	@SuppressWarnings("unused")
	private static double[][] normalize(double[][] A) {
		double[][] B = A.clone();
		for (int j = 0; j < A[0].length; j++) {
			double sum = sum(A, j);
			for (int i = 0; i < A.length; i++) {
				B[i][j] /= sum;
			}
		}
		return B;
	}

	private static double[] normalize(double[] A) {
		double[] B = A.clone();
		for (int j = 0; j < A.length; j++) {
			double sum = sum(A);
			B[j] /= sum;
		}
		return B;
	}

	private static double[][] transpose(double[][] A) {
		double[][] B = new double[A[0].length][A.length];
		for (int j = 0; j < A[0].length; j++) {
			for (int i = 0; i < A.length; i++) {
				B[i][j] = A[j][i];
			}
		}
		return B;
	}

	private static double sum(double[] A) {
		double sum = 0;
		for (int i = 0; i < A.length; i++) {
			sum += A[i] * A[i];
		}
		return (double) Math.sqrt(sum);
	}

	private static double sum(double[][] A, int column) {
		if (column > A[0].length)
			return -1;
		else {
			double sum = 0;
			for (int i = 0; i < A.length; i++) {
				sum += A[i][column] * A[i][column];
			}
			return (double) Math.sqrt(sum);
		}
	}

	private static double[][] mul(double[][] A, double[][] B) {
		double[][] res = new double[A.length][B[0].length];
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B[0].length; j++) {
				for (int k = 0; k < A[0].length; k++) {
					res[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		return res;

	}

	// Tested
	//
	/**
	 * 
	 * {12, -51, 4}, {6, 167,-68}, {-4, 24,-41} };
	 * 
	 * 
	 * result: 12.0 -69.0 -11.6 6.0 158.0 1.199997 -4.0 30.0 -33.0
	 */
	private double[][] gramSmith(double[][] A) {
		double[][] u = transpose(A);
		double[][] e = new double[A.length][A[0].length];
		matrix_E.show();
		src.highlight(13);
		src.highlight(14);
		lang.nextStep();
		src.unhighlight(13);
		src.unhighlight(14);
		
		index_j.show();
		index_k.show();
		
		for (int j = 0; j < A[0].length; j++) {
			src.highlight(15);
			index_j.setText("j="+j, null, null);
			for (int k = 0; k < j; k++) {
				index_k.setText("k="+k, null, null);
				src.highlight(16);
				src.highlight(17);
				e[j] = sub(e[j], proj(u[j], e[k]));
				updateMatrix(matrix_E, e);
				lang.nextStep();
			}
			src.unhighlight(16);
			src.unhighlight(17);
			src.highlight(19);
			e[j] = add(e[j],u[j]);
			updateMatrix(matrix_E, e);
			lang.nextStep();
			src.unhighlight(19);
			src.highlight(20);
			e[j] = normalize(e[j]);
			updateMatrix(matrix_E, e);
			lang.nextStep();
			src.unhighlight(20);
		}
		index_j.hide();
		index_k.hide();
		src.unhighlight(15);
		return transpose(e);
	}

	private static double[] sub(double[] a, double[] b) {
		double[] res = a.clone();
		for (int i = 0; i < a.length; i++)
			res[i] -= b[i];
		return res;
	}
	
	private static double[] add(double[] a, double[] b) {
		double[] res = a.clone();
		for (int i = 0; i < a.length; i++)
			res[i] += b[i];
		return res;
	}

	private static double[] proj(double[] a, double[] e) {
		double[] res = a.clone();
		double c = mul(a, e) / mul(e, e);
		res = mul(e, c);
		return res;
	}

	private static double[] mul(double[] a, double c) {
		double[] res = a.clone();
		for (int i = 0; i < a.length; i++)
			res[i] *= c;
		return res;
	}

	static double mul(double[] a, double[] b) {
		double res = 0;
		for (int i = 0; i < a.length; i++)
			res += a[i] * b[i];
		return res;
	}

	@SuppressWarnings("unused")
	private static double[] column(double[][] A, int column) {
		double[] col = new double[A.length];
		for (int i = 0; i < A.length; i++) {
			col[i] = A[i][column];
		}
		return col;
	}
	
	private void setProperties() {
		sourceCodeProps = new SourceCodeProperties();
		//for Source Code
		sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.MONOSPACED, Font.PLAIN, 12));
		sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}
	
	private static double[][] getDefaultMatrix(){
		return A;
	}
	
	
	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height
		
		Language l = new AnimalScript("QR", "Dan Le", 1920, 1080);
		QR gN = new QR(l);
		gN.setProperties();
		gN.printSourceCode();
		int n= 4;
		gN.start(getDefaultMatrix(),n,null,null);
		System.out.println(l);
	}

	@Override
	public void init() {
		lang = new AnimalScript("QR [EN]", "Dan Le",
		        1366, 768);
		lang.setStepMode(true);
	}
}
