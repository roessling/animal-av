/*
 * CORDIC.java
 * Annemarie Mattmann, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/*
 * Generator for CORDIC circular rotation mode
 */
public class CORDIC implements ValidatingGenerator {
    private Language lang;
    private RectProperties legendFrameProperties;
    private SourceCodeProperties sourceCodeProperties;
    private PolylineProperties functionGraphProperties;
    private PolylineProperties angleVectorProperties;
    private PolylineProperties trueAngleVectorProperties;
    private int numberIterations;
    private RectProperties codeFrameProperties;
    private double angle;

    public void init(){
        lang = new AnimalScript("CORDIC", "Annemarie Mattmann", 640, 480);
        // Initialize StepMode
     	lang.setStepMode(true);
    }

    /*
     * Initialize animation, show explanations and call algorithm
     */
    public void rotate() {
    	// Parameters
    	double z0 = angle;
    	int n = numberIterations;
    	
    	// Get color of target and current vector for explanation texts
    	Color vectorColor = (Color) angleVectorProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY);
    	Color goalColor = (Color) trueAngleVectorProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY);
    	
    	// Animation title + frame (rectangle around title)
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
			    Font.BOLD, 24));
		
		Text title = lang.newText(new Coordinates(20, 30), "CORDIC", "title", null, titleProps);
		Rect titleFrame = lang.newRect(new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW), new Offset(5, 5, "title", AnimalScript.DIRECTION_SE), "titleFrame", null);

		// Animation background information
		InfoBox description = new InfoBox(lang, new Offset(0, 20, "title", AnimalScript.DIRECTION_SW), 20, "Background Information"); 
		List<String> preText = Arrays.asList(
				"CORDIC (COordinate Rotation DIgital Computer) is a numerical algorithm which efficiently calculates sine and cosine of",
				"a given angle in the circular rotation mode, which is shown here (other modes allow for different computations).",
				"It was designed to be written in hardware using only addition and shifts and has been used for example in calculators.",
				"However, today it is seldom used since memory is no longer an issue and one can store thousands of sine and cosine",
				"values and interpolate if the exact value is not available.",
				"In CORDIC sine and cosine of a given angle are calculated by reading their values from the vector (1, 0) that is rotated",
				"by the given angle. However, since only addition and shifts are available for the computation the rotation must be",
				"approximated through additions or subtractions of smaller, given angles that were calculated such that their tangent",
				"equals a power of 2 (and can thus be applied by shifting).",
				"Also, for convergence the given angle must be in the interval of [-1.7433, 1.7433] respectively [-99.88°, 99.88°]. Sine",
				"and cosine of values beyond this interval may be calculated using their symmetry properties.");
		description.setText(preText);
		
		lang.nextStep("Introduction");

		// More detailed animation background information	
		description.hide();
		InfoBox formula = new InfoBox(lang, new Offset(0, 20, "title", AnimalScript.DIRECTION_SW), 20, "Iteration Formula"); 
		List<String> formulaText = Arrays.asList(
				"  x[i+1] = x[i] - sigma[i]*2^(-i)*y[i]",
				"  y[i+1] = sigma[i]*2^(-i)*x[i] + y[i]",
				"  z[i+1] = z[i] - sigma[i]*alpha[i]",
				"with x[0] = 1*K, y[0] = 0 and z[0] = target angle, where",
				"- x and y are the cosine and sine values of the angle that the vector describes at the n-th step",
				"- z is the target angle minus all rotations of (i-1) steps (i.e. the difference of the current angle to the target angle)",
				"- sigma[i] is the direction of rotation (either -1 for clockwise or 1 for counterclockwise); it is derived from z[i] and",
				"  multiplied with the current angle to adjust the rotation direction (which is a simple sign change in hardware)",
				"- alpha[i] is the current angle of rotation looked up from a table of precomputed angles with a tangent equal to a power",
				"  of 2",
				"The tangent is used because instead of applying the common rotation matrix the pseudo rotation matrix",
				"       1            -tan(angle)",
				"  tan(angle)           1",
				"is used. Multiplying the pseudo rotation matrix by a correction factor cos(angle) yields the  common rotation matrix",
				"  cos(angle)       -sin(angle)",
				"  sin(angle)        cos(angle",
				"Since multiplication in hardware is costly and one is only interested in the sine and cosine values of x and y at step n",
				"of the iteration this correction factor is precomputed, looked up in a table and used for x[0], so the vector will have",
				"size 1 at the n-th step of the iteration. Thus",
				"- K = Product_i=0^n-1*cos(alpha[i])");
		formula.setText(formulaText);
		
		lang.nextStep();
		
		formula.hide();
		
		// Function graph
		int offsetFromLeft = 20;
		int lengthOfXY = 150;
		Polyline y = lang.newPolyline(new Offset[]{(new Offset(offsetFromLeft+lengthOfXY, 55+lengthOfXY*2, "title", AnimalScript.DIRECTION_SW)), (new Offset(offsetFromLeft+lengthOfXY, 55, "title", AnimalScript.DIRECTION_SW))}, "yArrow", null, functionGraphProperties);
		Polyline x = lang.newPolyline(new Offset[]{(new Offset(-lengthOfXY, -lengthOfXY, "yArrow", AnimalScript.DIRECTION_SW)), (new Offset(lengthOfXY, -lengthOfXY, "yArrow", AnimalScript.DIRECTION_SW))}, "xArrow", null, functionGraphProperties);
		
		// Create legend (angles of vectors) with rectangle frame (hidden until vectors are created)
		TextProperties targetProperties = new TextProperties();
		targetProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, goalColor);
		targetProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 12));
		TextProperties vectorLegendProperties = new TextProperties();
		vectorLegendProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, vectorColor);
		vectorLegendProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 12));
		
		Text legendVector = lang.newText(new Offset(50, -lengthOfXY, "xArrow", AnimalScript.DIRECTION_NE), "Current Angle", "legendVector", null, vectorLegendProperties);
		Text legendTarget = lang.newText(new Offset(0, 10, "legendVector", AnimalScript.DIRECTION_SW), "Target Angle = " + Math.toDegrees(z0), "legendTarget", null, targetProperties);
		
		Rect legendFrame = lang.newRect(new Offset(-15, -15, "legendVector", AnimalScript.DIRECTION_NW), new Offset(15, 15, "legendTarget", AnimalScript.DIRECTION_SE), "legendFrame", null, legendFrameProperties);

		legendVector.hide();
		legendTarget.hide();
		legendFrame.hide();
		
		// Source code and frame placed to the right of the legend
		SourceCode code = lang.newSourceCode(new Offset(50, 100, "legendFrame", AnimalScript.DIRECTION_NE), "code", null, sourceCodeProperties);
		code.addCodeLine("define cordic(angle, numberIterations) //rotation mode", "definition", 0, null);
		code.addCodeLine("x[0] = K // get K from table", "xdef", 2, null);
		code.addCodeLine("y[0] = 0", "ydef", 2, null);
		code.addCodeLine("for i=0 to numberIterations-1 do", "for", 2, null);
		code.addCodeLine("if z[i] > 0", "if", 4, null);
		code.addCodeLine("sigma_i = 1", "posSigma", 6, null);
		code.addCodeLine("else", "else", 4, null);
		code.addCodeLine("sigma_i = -1", "negSigma", 6, null);
		code.addCodeLine("x[i+1] = x[i] - sigma_i*2^(-i)*y[i]", "xi", 4, null);
		code.addCodeLine("y[i+1] = sigma_i*2^(-i)*x[i] + y[i]", "yi", 4, null);
		code.addCodeLine("z[i+1] = z[i] - sigma_i*alpha_i // get alpha_i from table", "zi", 4, null);
		code.addCodeLine("return (x[numberIterations], y[numberIterations])", "return", 2, null);
		
		Rect codeFrame = lang.newRect(new Offset(-5, -5, "code", AnimalScript.DIRECTION_NW), new Offset(5, 5, "code", AnimalScript.DIRECTION_SE), "codeFrame", null, codeFrameProperties);
		
		lang.nextStep();
		
		code.highlight(0);
		
		// Target vector
		Polyline goal = lang.newPolyline(new Offset[]{(new Offset(-lengthOfXY, 0, "xArrow", AnimalScript.DIRECTION_NE)), (new Offset(0, 0, "xArrow", AnimalScript.DIRECTION_NE))}, "goalVector", null, trueAngleVectorProperties);
		goal.rotate(new Offset(-lengthOfXY, 0, "xArrow", AnimalScript.DIRECTION_NE), (int)Math.toDegrees(z0), null, null);
		
		// Show legend
		legendVector.show();
		legendTarget.show();
		legendFrame.show();
		
		// Create number counter of iterations
		Text nText = lang.newText(new Offset(0, 10, "legendFrame", AnimalScript.DIRECTION_SW), "Iteration = 0", "nText", null);
		nText.hide();
		// Create display text for z_i
		Text ziText = lang.newText(new Offset(0, 8, "nText", AnimalScript.DIRECTION_SW), "z_0 = " + z0, "ziText", null);
		ziText.hide();
		
		lang.nextStep("Initialization");
		
		code.unhighlight(0);
		
		// Call algorithm for CORDIC circular rotation and get the result for x[n] and y[n]
		double result[] = algorithmRotation(z0, n, lengthOfXY, code, legendVector, nText, ziText);
		
		code.highlight(11);

		// Show marker for final sine and cosine values
		int xOffset = (int)(result[0]*lengthOfXY+lengthOfXY);
		int yOffset = (int)(result[1]*lengthOfXY+lengthOfXY);
		Polyline sin = lang.newPolyline(new Offset[]{(new Offset(6, -yOffset, "yArrow", AnimalScript.DIRECTION_SW)), (new Offset(-6, -yOffset, "yArrow", AnimalScript.DIRECTION_SW))}, "sine", null);
		Polyline cos = lang.newPolyline(new Offset[]{(new Offset(xOffset, 6, "xArrow", AnimalScript.DIRECTION_NW)), (new Offset(xOffset, -6, "xArrow", AnimalScript.DIRECTION_NW))}, "cosine", null);
		
		// Show final values of CORDIC and values of target angle
		Text sinText = lang.newText(new Offset(-50, -35, "yArrow", AnimalScript.DIRECTION_N), "sine = " + result[1], "sineText", null, vectorLegendProperties);
		Text cosText = lang.newText(new Offset(0, 0, "xArrow", AnimalScript.DIRECTION_SE), "cosine = " + result[0], "cosineText", null, vectorLegendProperties);
		
		Text trueSinText = lang.newText(new Offset(0, 5, "sineText", AnimalScript.DIRECTION_SW), "sine = " + Math.sin(z0), "sineText", null, targetProperties);
		Text trueCosText = lang.newText(new Offset(0, 5, "cosineText", AnimalScript.DIRECTION_SW), "cosine = " + Math.cos(z0), "cosineText", null, targetProperties);
		
		nText.hide();
		ziText.hide();
		
		lang.nextStep("Leave Algorithm");
		
		// HideAllPrimitivesExcept does not seem to work with
		// lang.hideAllPrimitivesExcept(Arrays.asList(title, titleFrame));
		lang.hideAllPrimitives();
		
		title.show();
		titleFrame.show();
		
		// Final remark
		InfoBox outlook = new InfoBox(lang, new Offset(0, 20, "title", AnimalScript.DIRECTION_SW), 20, "Final Remark"); 
		List<String> postText = Arrays.asList(
				"This animation displayed the so called circular rotation mode of CORDIC for calculating sine and cosine of a given angle.",
				"The circular mode of CORDIC includes another so called vector mode which is the reverse of the rotation mode and provides",
				"the absolute of a vector and its angle. The changes necessary to apply this mode is to set z[0] = 0 and let the user",
				"define x[0] and y[0] (though the absolute value x[n] must be divided by K to obtain the real absolute value). Also, sigma[i]",
				"is evaluated using y[i-1] instead of z[i-1] (the basic idea is to try to rotate until y is zero instead of until the",
				"difference between the target and current angle is zero).",
				"Beyond that, even more modes exist which calculate for example the hyperbolical functions. These modes require different",
				"values for the variables than the circular mode and an additional one to generalize the algorithm.");
		outlook.setText(postText);
		
		lang.nextStep("Final Remark");
	}
	
    /*
     * CORDIC circular rotation mode
     */
	private double[] algorithmRotation(double z0, int n, int lengthOfXY, SourceCode code, Text legendVector, Text nText, Text ziText) {
		
		// Set TextHightlightColor to CodeHightlightColor
		Color textHighlightColor = (Color)code.getProperties().get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
		
		// Normally looked up in a table but calculated here
		double K = 1;
		for(int i = 0; i < n; i++) {
			K *= Math.cos(Math.atan(Math.pow(2, -i)));
		}
		
		// Initialize variables
		double[] x = new double[n+1];
		double[] y = new double[n+1];
		double[] z = new double[n+1];
		x[0] = K;
		y[0] = 0;
		z[0] = z0;
		
		code.highlight(1);
		code.highlight(2);
		
		String legendVectorText = legendVector.getText();		
		
		// Create array for all vectors (past ones get gray and lighter in color the older they are)
		Polyline[] vec = new Polyline[n+1];
		// Create first vector
		vec[0] = lang.newPolyline(new Offset[]{(new Offset(-lengthOfXY, 0, "xArrow", AnimalScript.DIRECTION_NE)), (new Offset((int)(x[0]*lengthOfXY-lengthOfXY), (int)(y[0]*lengthOfXY), "xArrow", AnimalScript.DIRECTION_NE))}, "vector", null, angleVectorProperties);
		
		// Show current angle
		legendVector.setText(legendVectorText + " = " + Math.toDegrees(Math.atan2(y[0], x[0])), null, null);
		
		// Declare/initialize variables for the variable counter
		Variables vars = lang.newVariables();
		vars.declare("double", "K");
		vars.set("K", Double.toString(K));
		vars.declare("int", "n");
		vars.set("n", Integer.toString(n));
		vars.declare("double", "angle");
		vars.set("angle", Double.toString(Math.toDegrees(z0)));
		vars.declare("double", "x");
		vars.set("x", Double.toString(x[0]));
		vars.declare("double", "y");
		vars.set("y", Double.toString(y[0]));
		vars.declare("double", "z");
		vars.set("z", Double.toString(z[0]));
		vars.declare("int", "i");
		vars.declare("int", "sigma");
		
		lang.nextStep("Enter Algorithm");
		nText.show();
		ziText.show();
		
		code.unhighlight(1);
		code.unhighlight(2);
		
		int sigma_i;
		// Set initial shadow color (gray) and brighting factor
		int shadowVectorsColor = 128; // because RGB(128,128,128) equals Color.GRAY
		int brightingFactor = shadowVectorsColor/(n+2);
		// Do n iterations
		for (int i = 0; i < n; i++) {
			// Update i
			vars.set("i", Integer.toString(i));
			
			code.highlight(3);
			// Set iteration and highlight change
			nText.setText("Iteration = " + i, null, null);
			nText.changeColor(null, textHighlightColor, null, null);
			
			lang.nextStep(i +"th Iteration");
			
			code.unhighlight(3);
			
			// Highlight z_i which is used to determine sigma_i
			nText.changeColor(null, Color.BLACK, null, null);
			ziText.changeColor(null, textHighlightColor, null, null);
			
			// Check and highlight sigma_i
			if (z[i] > 0) {
				sigma_i = 1;
				code.highlight(4);
				code.highlight(5);
			}
			else {
				sigma_i = -1;
				code.highlight(6);
				code.highlight(7);
			}
			
			// Update sigma_i
			vars.set("sigma", Integer.toString(sigma_i));
			
			lang.nextStep();
			
			ziText.changeColor(null, Color.BLACK, null, null);
			
			code.unhighlight(4);
			code.unhighlight(5);
			code.unhighlight(6);
			code.unhighlight(7);
			
			code.highlight(8);
			code.highlight(9);
			
			// Calculate next x and y
			x[i+1] = x[i] - sigma_i*Math.pow(2, -i)*y[i];
			y[i+1] = sigma_i*Math.pow(2, -i)*x[i] + y[i];
			// Normally looked up in a table but calculated here
			double alpha_i = Math.atan(Math.pow(2, -i));
			
			// Create new current vector and shade out the older one(s); older ones get brighter
			vec[i+1] = lang.newPolyline(new Offset[]{(new Offset(-lengthOfXY, 0, "xArrow", AnimalScript.DIRECTION_NE)), (new Offset((int)(x[i+1]*lengthOfXY-lengthOfXY), -(int)(y[i+1]*lengthOfXY), "xArrow", AnimalScript.DIRECTION_NE))}, "vector", null, angleVectorProperties);
			for (int j = 0; j <= i; j++) {
				int newColor = shadowVectorsColor + (i-j)*brightingFactor;
				vec[j].changeColor(null, new Color(newColor, newColor, newColor), null, null);
			}
			
			// Update angle
			legendVector.setText(legendVectorText + " = " + Math.toDegrees(Math.atan2(y[i+1], x[i+1])), null, null);
			
			// Update variables
			vars.set("x", Double.toString(x[i+1]));
			vars.set("y", Double.toString(y[i+1]));
			
			lang.nextStep();
			
			code.unhighlight(8);
			code.unhighlight(9);
			
			code.highlight(10);
			
			// Calculate next z
			z[i+1] = z[i] - sigma_i*alpha_i;
			
			// Change z value and highlight text
			ziText.setText("z_" + (i+1) + " = " + z[i+1], null, null);
			ziText.changeColor(null, textHighlightColor, null, null);
			
			// Update variable
			vars.set("z", Double.toString(z[i+1]));
			
			lang.nextStep();
			
			ziText.changeColor(null, Color.BLACK, null, null);
			code.unhighlight(10);
		}
		
		// Return result
		return new double[]{x[n], y[n]};
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        legendFrameProperties = (RectProperties)props.getPropertiesByName("legendFrameProperties");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        functionGraphProperties = (PolylineProperties)props.getPropertiesByName("functionGraphProperties");
        angleVectorProperties = (PolylineProperties)props.getPropertiesByName("angleVectorProperties");
        trueAngleVectorProperties = (PolylineProperties)props.getPropertiesByName("trueAngleVectorProperties");
        numberIterations = (Integer)primitives.get("numberIterations");
        codeFrameProperties = (RectProperties)props.getPropertiesByName("codeFrameProperties");
        angle = (double)primitives.get("angle");
        
        rotate();
        
        return lang.toString();
    }

    public String getName() {
        return "CORDIC";
    }

    public String getAlgorithmName() {
        return "CORDIC";
    }

    public String getAnimationAuthor() {
        return "Annemarie Mattmann";
    }

    public String getDescription(){
        return "CORDIC (COordinate Rotation DIgital Computer) is a numerical algorithm which efficiently calculates sine and cosine of a given angle in the circular rotation mode, which is shown here (other modes allow for different computations)."
 +"\n"
 +"<br>"
 +"\n"
 +"It was designed to be written in hardware using only addition and shifts and has been used for example in calculators. However, today it is seldom used since memory is no longer an issue and one can store thousands of sine and cosine values and interpolate if the exact value is not available."
 +"\n"
 +"<br>"
 +"\n"
 +"In CORDIC sine and cosine of a given angle are calculated by reading their values from the vector (1, 0) that is rotated by the given angle. However, since only addition and shifts are available for the computation the rotation must be approximated through additions or subtractions of smaller, given angles that were calculated such that their tangent equals a power of 2 (and can thus be applied by shifting)."
 +"\n"
 +"<br>"
 +"\n"
 +"Also, for convergence the given angle must be in the interval of <b>[-1.7433, 1.7433]</b> respectively [-99.88°, 99.88°]. Sine and cosine of values beyond this interval may be calculated using their symmetry properties."
 +"\n";
    }

    public String getCodeExample(){
        return "define cordic(angle, numberIterations) //rotation mode"
 +"\n"
 +"  x[0] = K // get K from table"
 +"\n"
 +"  y[0] = 0"
 +"\n"
 +"  for i=0 to numberIterations-1 do"
 +"\n"
 +"    if z[i] > 0"
 +"\n"
 +"      sigma_i = 1"
 +"\n"
 +"    else"
 +"\n"
 +"      sigma_i = -1"
 +"\n"
 +"    x[i+1] = x[i] - sigma_i*2^(-i)*y[i]"
 +"\n"
 +"    y[i+1] = sigma_i*2^(-i)*x[i] + y[i]"
 +"\n"
 +"    z[i+1] = z[i] - sigma_i*alpha_i // get alpha_i from table"
 +"\n"
 +"  return (x[numberIterations], y[numberIterations])";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		angle = (double)arg1.get("angle");
		// Throw an error when the angle is outside of the radius of convergence
		if (angle < -1.7433|| angle > 1.7433) {
			JOptionPane.showMessageDialog(null, "The angle must be in the interval of [-1.7433, 1.7433]!");
			return false;
		}
		return true;
	}

}