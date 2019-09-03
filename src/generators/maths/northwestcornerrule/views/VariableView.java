package generators.maths.northwestcornerrule.views;

import generators.maths.northwestcornerrule.AnimProps;
import generators.maths.northwestcornerrule.DrawingUtils;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class VariableView {

	private Language animationScript;
	private DrawingUtils myDrawingUtils;
	
	private Text variable_i;
	private Text variable_j;
	private Text variable_x;
	private Text variable_angebot_i;
	private Text variable_nachfrage_j;
	
	private static String TXT_VIEW_TITLE = "Variablen";
	private static int CO_X_VARIABLES = 20;
	private static int CO_Y_VARIABLES = 400;
	private static int CO_Y_VARIABLES_DELTA = 25;
	
	public VariableView(Language animationScript, DrawingUtils myDrawingUtils ){
		this.animationScript = animationScript;
		this.myDrawingUtils = myDrawingUtils;
	}
	
	public void setupView(){
		
		myDrawingUtils.drawHeader(new Coordinates(5,375), TXT_VIEW_TITLE);
		setupFields();
	}
	
	private void setupFields(){
		
		variable_i = animationScript.newText(new Coordinates(CO_X_VARIABLES, CO_Y_VARIABLES), "int i = 0" , "variable_i", null, AnimProps.TXT_VAR_PROPS);
		variable_j = animationScript.newText(new Coordinates(CO_X_VARIABLES, CO_Y_VARIABLES + CO_Y_VARIABLES_DELTA), "int j = 0" , "variable_j", null, AnimProps.TXT_VAR_PROPS);
		variable_x = animationScript.newText(new Coordinates(CO_X_VARIABLES, CO_Y_VARIABLES + CO_Y_VARIABLES_DELTA*2), "int x = null" , "variable_x", null, AnimProps.TXT_VAR_PROPS);
		variable_angebot_i = animationScript.newText(new Coordinates(CO_X_VARIABLES, CO_Y_VARIABLES + CO_Y_VARIABLES_DELTA*3), "angebot[i] = " , "variable_angebot_i", null, AnimProps.TXT_VAR_PROPS);
		variable_nachfrage_j = animationScript.newText(new Coordinates(CO_X_VARIABLES, CO_Y_VARIABLES + CO_Y_VARIABLES_DELTA*4), "nachfrage[j] = " , "variable_angebot_i", null, AnimProps.TXT_VAR_PROPS);

	}
	
	public void alter_variable_i(int value){
		variable_i.setText("int i = " +value, new TicksTiming(30), new TicksTiming(30));
	}
	
	public void alter_variable_j(int value){
		variable_j.setText("int j = " +value, new TicksTiming(30), new TicksTiming(30));
	}
	
	public void alter_variable_x(int angebot, int nachfrage, int min){
		variable_x.setText("int x = min(" +angebot +"," + nachfrage +")" , new TicksTiming(30), new TicksTiming(30));
		variable_x.setText("int x = " + min , new TicksTiming(160), new TicksTiming(80));	
	}
	
	public void alter_variable_angebot_i(int newValue, int delta, int i){
		variable_angebot_i.setText("angebot["+i+"] = " + (newValue + delta) +" - " + delta , new TicksTiming(30), new TicksTiming(30));
		variable_angebot_i.setText("angebot["+i+"] = " + newValue , new TicksTiming(160), new TicksTiming(80));
	}
	
	public void alter_variable_nachfrage_j(int newValue, int delta, int j){
		variable_nachfrage_j.setText("nachfrage["+j+"] = " + (newValue + delta) +" - " + delta , new TicksTiming(30), new TicksTiming(30));
		variable_nachfrage_j.setText("nachfrage["+j+"] = " + newValue , new TicksTiming(160), new TicksTiming(80));
	}
}
