/*
 * export_optchain.java
 * Kenten Fina, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

import algoanim.primitives.*;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;
import algoanim.variables.VariableTypes;
import animal.animator.Move;
import animal.variables.VariableRoles;

public class OptMatrixChain implements ValidatingGenerator {
    protected Language lang;
    protected int[] matrix_sizes;
    protected int matrix_count;
    protected SourceCodeProperties SP_sourceCode;
    protected MatrixProperties MP_index_table;
    protected MatrixProperties MP_count_table;
    
    protected TextProperties TP_title;
    protected TextProperties TP_subtitle;
    protected TextProperties TP_std, TP_bold, TP_final;
    
    protected RectProperties RP_titlebox;
    protected SourceCodeProperties SP_phases;
    protected ArrayProperties AP_sourcedata;
    
    protected Timing localTimer_250ms;
    protected DecimalFormat localFormat;
    
	protected Text TitleText;
	protected Rect TitleBox;
	protected Text SubTitleText;
	protected Rect SubTitleBox;
	protected ArrayList<Primitive> ListTitle;
	protected SourceCode SPhaseCode;
	protected SourceCode SRCCode;
	protected IntArray MatrixSizes;
	protected Text CountInfo, IndexInfo, TableInfo, SizeInfo, Formula, FormulaInfo, VarInfo_L, VarInfo_I, VarInfo_K, VarInfo_Q;
	
	protected Variables Vars;
	
	protected IntMatrix IndexTable;
	protected IntMatrix CountTable;
	protected int[][] tableIndex;
	protected int[][] tableCount;
    
    public OptMatrixChain() {
    	init();
    }

    @Override
    public void init(){
        lang = new AnimalScript("Optimized Matrix Chain Multiplication", "Kenten Fina", 800, 600);
        ListTitle = new ArrayList<Primitive>();
        InitAnimationProps();
    }

    private void InitAnimationProps() {
        TP_title = new TextProperties("Title");
        TP_title.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 25) );
        TP_title.set( AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE );

        TP_subtitle = new TextProperties("SubTitle");
        TP_subtitle.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20) );
        TP_subtitle.set( AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE );
        
        TP_std = new TextProperties("STD");
        
        TP_bold = new TextProperties("BOLD");
        TP_bold.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        
        TP_final = new TextProperties("LARGE");
        TP_final.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
        
        RP_titlebox = new RectProperties("TitleBox");
        RP_titlebox.set( AnimationPropertiesKeys.FILLED_PROPERTY, true );
        RP_titlebox.set( AnimationPropertiesKeys.DEPTH_PROPERTY, 2 );
        RP_titlebox.set( AnimationPropertiesKeys.FILL_PROPERTY, new Color(80, 160, 255) );
        
        SP_phases = new SourceCodeProperties("PhasesCode");
        SP_phases.set( AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK );
        SP_phases.set( AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
        SP_phases.set( AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE );
        
        AP_sourcedata = new ArrayProperties("ArrayData");
        AP_sourcedata.set( AnimationPropertiesKeys.DEPTH_PROPERTY, 8);
        AP_sourcedata.set( AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        AP_sourcedata.set( AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
        AP_sourcedata.set( AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        
        localTimer_250ms = new MsTiming(250);

        localFormat = new DecimalFormat("#.####", DecimalFormatSymbols.getInstance( Locale.ENGLISH ));
    }
    
    @Override
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        matrix_sizes = (int[])primitives.get("matrix_sizes");
        SP_sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        MP_index_table = (MatrixProperties)props.getPropertiesByName("index_table");
        MP_count_table = (MatrixProperties)props.getPropertiesByName("count_table");
        
        MP_count_table.set( AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        MP_index_table.set( AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        MP_index_table.set( AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.LIGHT_GRAY);
        
        ListTitle.clear();
        
        generateOMCScript();
        
        return lang.toString();
    }

    protected void generateOMCScript() {
    	matrix_count = matrix_sizes.length-1;
    	lang.setStepMode(true);
    	FGenVars(lang);
    	
        // Step 1: Title
        FGenerateOCMHeader(lang);
        
        // Step 2: Code
        FGenerateOCMMainCode(lang);
        
        // Step 3: Phase 1
        FGenerateOCMTable(lang);
        
        // Step 4: Phase 2
        FGenerateOCMFormula(lang);

        // Step 5: Finalization
        FGenerateOCMFinal(lang);
	}
    
    private void FGenVars(Language AScript) {
    	Vars = lang.newVariables();
    }

	private void FGenerateOCMFinal(Language AScript) {
		IndexTable.hide();
		IndexInfo.hide();
		
		Formula.moveTo(null, null, new Offset(0, 50, "p_msize", "SW"), Timing.INSTANTEOUS, Timing.MEDIUM);
		FormulaInfo.moveTo(null, null, new Offset(0, 30, "p_msize", "SW"), Timing.INSTANTEOUS, Timing.MEDIUM);
		
		Text hint = AScript.newText(new Offset(0, 100, "p_msize", "SW"), "   ", "final_hint", null, TP_final);
		hint.setText("Multiplying the matrices in this order only needs " + tableCount[0][tableCount[0].length-1] + 
				" scalar operations in contrast to the naive approach with " + getNaiveOperations(matrix_sizes) + " operations.", Timing.MEDIUM, Timing.INSTANTEOUS);
	}

	private int getNaiveOperations(int[] mats) {
		if (mats.length < 3) return 0;
		if (mats.length == 3) return (mats[0] * mats[1] * mats[2]);
		
		int[] nmats = new int[mats.length-1];
		nmats[0] = mats[0];
		for (int i = 1; i < nmats.length; i++)
			nmats[i] = mats[i+1];
		int v = getNaiveOperations(nmats);
		
		return (mats[0] * mats[1] * mats[2]) + v;
	}

	private void FGenerateOCMHeader(Language AScript) {
		TitleText = AScript.newText( new Coordinates(16, 16), "Optimized Matrix Chain Multiplication", "header", null, TP_title);
        AScript.addItem( TitleText );
        
        TitleBox = AScript.newRect( new Offset(-4, -4, "header", "NW"), new Offset(4, 4, "header", "SE"), "hRect", null, RP_titlebox );
        AScript.addItem( TitleBox );
        	
        ListTitle.add(TitleText);
        ListTitle.add(TitleBox);
        
        SourceCode init_description = AScript.newSourceCode( new Offset(0, 40, "header", "SW"), "description", null, SP_sourceCode);
        init_description.addMultilineCode( getDescription(), "desc_all", Timing.INSTANTEOUS);
        AScript.nextStep("Introduction");
        
        init_description.hide();
	}

	private void FGenerateOCMMainCode(Language AScript) {
		SPhaseCode = AScript.newSourceCode( new Offset(240, 0, "hRect", "NE"), "mainSteps", null, SP_phases);
        SPhaseCode.addCodeLine("1. Build Matrix containing best cut indices.", "phase_1", 0, Timing.INSTANTEOUS);
        SPhaseCode.addCodeLine("2. Retrieve optimal order using the cut indices.", "phase_2", 0, Timing.INSTANTEOUS);

        AScript.addItem(SPhaseCode);
        AScript.nextStep();

        SPhaseCode.highlight(0);
	}
	
	private void FGenerateOCMTable(Language AScript) {
        // Subtitle:
        SubTitleText = AScript.newText( new Offset(0, 16, "header", "SW"), "1. Generate Tables", "subheader", null, TP_subtitle);
        AScript.addItem( SubTitleText );

        SubTitleBox = AScript.newRect( new Offset(-4, 0, "subheader", "NW"), new Offset(44, 0, "subheader", "SE"), "subhRect1", null, RP_titlebox );
        AScript.addItem( SubTitleBox );
        
        // Create Data Tables:
        tableCount = genEmptyCountMatrix(matrix_count);
        tableIndex = genEmptyIndexMatrix(matrix_count);
        MatrixSizes = AScript.newIntArray(new Offset(0, 50, "subheader", "SW"), matrix_sizes, "p_msize", null, AP_sourcedata);
        SizeInfo = AScript.newText(new Offset(0, -20, "p_msize", "NW"), "Matrix Sizes", "matrixinfo", null, TP_std);
        CountTable = AScript.newIntMatrix(new Offset(0, 50, "p_msize", "SW"), tableCount, "counttable", null, MP_count_table);
        CountInfo = AScript.newText(new Offset(0, -20, "counttable", "NW"), "Scalar Multiplication Table", "countinfo", null, TP_std);
        IndexTable = AScript.newIntMatrix(new Offset(0, 50, "counttable", "SW"), tableIndex, "indextable", null, MP_index_table);
        IndexInfo = AScript.newText(new Offset(0, -20, "indextable", "NW"), "Cut Index Table", "indexinfo", null, TP_std);
        
        SRCCode = AScript.newSourceCode(new Offset(0, 100, SPhaseCode, "SW"), "srccode", null, SP_sourceCode);
        SRCCode.addMultilineCode("MATRIX-CHAIN-BUILD (p) {"
 +"\n"
 +"  n := Length(p) - 1;"
 +"\n"
 +"  for i = 1 to n do initialize m[i, i] := 0; // Multiplying only one matrix does not need any scalar operations."
 +"\n"
 +"  for l = 2 to n do // Start with lengths of 2 and go up to the matrix count."
 +"\n"
 +"    for i = 1 to n - l + 1 do // Find the best order for the matrices M[i] to M[i+l-1]."
 +"\n"
 +"      j := i + l - 1; // calculate the y index of the field in the matrix."
 +"\n"
 +"      m[i, j] := +inf;"
 +"\n"
 +"      for k = i to j - 1 do // check all possible combinations"
 +"\n"
 +"        // Calculate amount of scalar operations:" + "\n"
 +"        q := m[i, k] + m[k + 1, j] + p[i-1] * p[k] * p[j] ;" 
 +"\n"
 +"        if q < m[i, j] then // If this order is better"
 +"\n"
 +"          // then use this order and set its scalar operations as best." + "\n"
 +"          m[i,j] := q;"
 +"\n"
 +"          s[i,j] := k;"
 +"\n"
 +"  return s;"
 +"\n"
 +"}", "code", null);
        
        SRCCode.highlight(2);
        
        VarInfo_I = AScript.newText(new Offset(20, 0, SubTitleBox, "SE"), "i = 0", "varI", null, TP_bold);
        VarInfo_K = AScript.newText(new Offset(50, 0, VarInfo_I, "NE"), "k = 0", "varK", null, TP_bold);
        VarInfo_L = AScript.newText(new Offset(0, 15, VarInfo_I, "SW"), "l = 0", "varL", null, TP_bold);
        VarInfo_Q = AScript.newText(new Offset(50, 0, VarInfo_L, "NE"), "q = 0", "varQ", null, TP_bold);
        
        Vars.declare("int", "i", "0", VariableRoles.STEPPER.name());
        Vars.declare("int", "j", "0", VariableRoles.FOLLOWER.name());
        Vars.declare("int", "k", "0", VariableRoles.STEPPER.name());
        Vars.declare("int", "l", "0", VariableRoles.STEPPER.name());
        Vars.declare("int", "q", "0", VariableRoles.TEMPORARY.name());
        
        
        for (int i = 0; i < matrix_count; i++) {
        	for (int j = 0; j < matrix_count; j++) {
        		IndexTable.highlightElem(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        	}
        }
        
        lang.nextStep("Phase 1: Building cut index table");
        SRCCode.unhighlight(2);
        
        for (int len = 2; len <= matrix_count; len++) {
        	// Go through all fields on the diagonal:
        	for (int i = 1; i <= matrix_count - len + 1; i++) {
        		// set j index:
        		int j = i + len - 1;
        		CountTable.highlightCell(i-1, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		CountTable.highlightElem(i-1, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		
        		IndexTable.unhighlightElem(i-1, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		IndexTable.highlightCell(i-1, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		
        		MatrixSizes.highlightCell(i-1, i+len-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		
        		tableCount[i-1][j-1] = tableCount[i-1][i-1] + tableCount[i][j-1] + matrix_sizes[i-1] * matrix_sizes[i] * matrix_sizes[j];
        		CountTable.put(i-1, j-1, tableCount[i-1][j-1], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		
        		tableIndex[i-1][j-1] = i;
        		IndexTable.put(i-1, j-1, tableIndex[i-1][j-1], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		
        		CountTable.highlightCell(i-1, i-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		CountTable.highlightCell(i, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		MatrixSizes.highlightElem(i-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		MatrixSizes.highlightElem(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		MatrixSizes.highlightElem(j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		SRCCode.highlight(9); SRCCode.highlight(10); SRCCode.highlight(11); SRCCode.highlight(12); SRCCode.highlight(13);
        		UpdateVarText(i, i, len, tableCount[i-1][j-1]);
        		UpdateVars(i, j, i, len, tableCount[i-1][j-1]);
        		AScript.nextStep();
        		SRCCode.unhighlight(9); SRCCode.unhighlight(10); SRCCode.unhighlight(11); SRCCode.unhighlight(12); SRCCode.unhighlight(13);
        		MatrixSizes.unhighlightElem(i-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		MatrixSizes.unhighlightElem(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		MatrixSizes.unhighlightElem(j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		CountTable.unhighlightCell(i-1, i-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		CountTable.unhighlightCell(i, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		// Try out all combinations:
        		for (int k = i+1; k < j; k++) {
        			int q = tableCount[i-1][k-1] + tableCount[k][j-1] + matrix_sizes[i-1] * matrix_sizes[k] * matrix_sizes[j];
        			if (q < tableCount[i-1][j-1]) {
        				tableCount[i-1][j-1] = q;
        				CountTable.put(i-1, j-1, tableCount[i-1][j-1], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        				
        				tableIndex[i-1][j-1] = k;
        				IndexTable.put(i-1, j-1, tableIndex[i-1][j-1], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        				SRCCode.highlight(11); SRCCode.highlight(12); SRCCode.highlight(13);
        			}
        			SRCCode.highlight(9); SRCCode.highlight(10);
        			CountTable.highlightCell(i-1, k-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            		CountTable.highlightCell(k, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            		MatrixSizes.highlightElem(i-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            		MatrixSizes.highlightElem(k, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            		MatrixSizes.highlightElem(j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            		UpdateVarText(i, k, len, q);
            		UpdateVars(i, j, k, len, q);
            		AScript.nextStep();
            		SRCCode.unhighlight(12); SRCCode.unhighlight(9); SRCCode.unhighlight(10); SRCCode.unhighlight(11); SRCCode.unhighlight(13);
            		MatrixSizes.unhighlightElem(i-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            		MatrixSizes.unhighlightElem(k, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            		MatrixSizes.unhighlightElem(j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            		CountTable.unhighlightCell(i-1, k-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            		CountTable.unhighlightCell(k, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		}
        		
        		CountTable.unhighlightCell(i-1, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		CountTable.unhighlightElem(i-1, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		IndexTable.unhighlightCell(i-1, j-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        		MatrixSizes.unhighlightCell(i-1, i+len-1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        	}
        }
        Vars.discard("i");
        Vars.discard("j");
        Vars.discard("k");
        Vars.discard("l");
        Vars.discard("q");
	}
	
	private void UpdateVarText(int i, int k, int l, int q) {
		VarInfo_I.setText("i = " + i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		VarInfo_K.setText("k = " + k, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		VarInfo_L.setText("l = " + l, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		VarInfo_Q.setText("q = " + q, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}
	
	private void UpdateVars(int i, int j, int k, int l, int q) {
		Vars.set("i", String.valueOf(i));
		Vars.set("j", String.valueOf(j));
		Vars.set("k", String.valueOf(k));
		Vars.set("l", String.valueOf(l));
		Vars.set("q", String.valueOf(q));
	}

	private String genMatString(int i, int j) {
		if (i == j) return "M[" + i + "]";
		return "M[" + i + ":" + j + "]";
	}
	
	private String genMatRegex(int i, int j) {
		return "M\\[" + i + ":" + j + "\\]";
	}
	
	private String MatMul (int i, int j, String s, Language AScript) {
		if (j > i) {
			s = s.replaceFirst(genMatRegex(i, j), 
				"(" + genMatString(i, tableIndex[i][j]-1) + " x " + genMatString(tableIndex[i][j], j) + ")" 
				);
			Formula.setText(s, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			IndexTable.highlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			AScript.nextStep();
			IndexTable.unhighlightCell(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			
			s = MatMul(i, tableIndex[i][j]-1, s, AScript);
			s = MatMul(tableIndex[i][j], j,   s, AScript);
			
			return s;
		} else {
			return s;
		}
	}
	
	private void FGenerateOCMFormula(Language AScript) {
		CountTable.hide();
		CountInfo.hide();
		VarInfo_I.hide();
		VarInfo_K.hide();
		VarInfo_L.hide();
		VarInfo_Q.hide();
		SRCCode.hide();
		SPhaseCode.unhighlight(0);
		SPhaseCode.highlight(1);
	
		SubTitleText.setText("2. Generate Formula", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		String s = genMatString(0, matrix_count-1);
		FormulaInfo = AScript.newText(new Offset(0, 30, "indextable", "SW"), "Formula:", "formulai", null, TP_bold);
		Formula = AScript.newText(new Offset(0, 10, "formulai", "SW"), s, "formula", null, TP_bold);
		AScript.nextStep("Phase 2: Generating formula");
		
		s = MatMul(0, matrix_count-1, s, AScript);
		// System.out.println(s);
	}
	
	private int[][] genEmptyIndexMatrix(int n) {
		int[][] result = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = -1;
			}
		}
		return result;
	}

	private int[][] genEmptyCountMatrix(int n) {
		int[][] result = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					result[i][j] = 0;
				} else {
					result[i][j] = -1;
				}
			}
		}
		return result;
	}

	public String getName() {
        return "Optimized Matrix Chain Multiplication";
    }

    public String getAlgorithmName() {
        return "Optimized Matrix Chain Multiplication";
    }

    public String getAnimationAuthor() {
        return "Kenten Fina";
    }

    public String getDescription(){
        return "When having an array of matrices, which shall be multiplied, there is an order of multiplications, which"
 +"\n"
 +"has a minimum number of scalar operations."
 +"\n"
 +"This algorithm uses the following Dynamic Programming approach to find this order:"
 +"\n"
 +"Let n be the amount of matrices."
 +"\n"
 +"For i = 1 to n"
 +"\n"
 +"  pick i neighbored matrices and get its optimal multiplication order, by finding the best combination"
 +"\n"
 +"  of the [1;i-1] neighbored matrices orders."
 +"\n"
 +"Rebuild the order from the saved indexes, where to split the matrix chain.";
    }

    public String getCodeExample(){
        return "MATRIX-CHAIN-BUILD (p) {"
 +"\n"
 +"  n := Length(p) - 1;"
 +"\n"
 +"\n"
 +"  for i = 1 to n do initialize m[i, i] := 0;"
 +"\n"
 +"  for l = 2 to n do"
 +"\n"
 +"    for i = 1 to n - l + 1 do"
 +"\n"
 +"      j := i + l - 1;"
 +"\n"
 +"      m[i, j] := +inf;"
 +"\n"
 +"      for k = i to j - 1 do"
 +"\n"
 +"        q := m[i, k] + m[k + 1, j] + p[i-1] * p[k] * p[j] ;"
 +"\n"
 +"        if q < m[i, j] then"
 +"\n"
 +"          m[i,j] := q;"
 +"\n"
 +"          s[i,j] := k;"
 +"\n"
 +"  return s;"
 +"\n"
 +"}"
 +"\n"
 +"        "
 +"\n"
 +"MATRIX-CHAIN-MULTIPLY (A, s, i, j) {"
 +"\n"
 +"  if j > i then"
 +"\n"
 +"    X := MATRIX-CHAIN-MULTIPLY (A, s, i, s[i, j]);"
 +"\n"
 +"    Y := MATRIX-CHAIN-MULTIPLY (A, s, s[i, j] + 1, j);"
 +"\n"
 +"    return MATRIX-MULTIPLY (X, Y);"
 +"\n"
 +"  else"
 +"\n"
 +"    return A[i];"
 +"\n"
 +"}"
 +"\n"
 +"\n"
 +"Call:"
 +"\n"
 +"MATRIX-CHAIN_MULTIPLY (A, MATRIX-CHAIN-BUILD(p), 1, Length(p) - 1);";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        
        return true;
    }

}