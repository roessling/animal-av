/*
 * WagnerWithinGenerator.java
 * Najim Azizi, Timm Lampa, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import translator.Translator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class WagnerWithinGenerator implements Generator {
    private Language lang;
    private MatrixProperties ResultMatrixprops;
    private int[][] input;
    private SourceCodeProperties bigsourceCodeprops;
    private MatrixProperties InputMatrixprops;
    private MatrixProperties InputLabelLeftprops;
    private MatrixProperties OutputLabelLeftprops;
    private MatrixProperties OutputMatrixprops;
    private MatrixProperties InputLabelTopprops;
    private SourceCodeProperties descriptionprops;
    private MatrixProperties OutputLabelTopprops;
    private SourceCodeProperties sourceCodeprops;
    private Translator translator;
    private boolean correctinput = false;
    private String error= "Inputfehler";
    
    private Locale language;
    	
	public WagnerWithinGenerator(Locale language){
		this.language = language;
		translator = new Translator("translatorfiles/WW" , language ) ;
	}

    public void init(){
        lang = new AnimalScript("Wagner Within Algorithmus", "Najim Azizi, Timm Lampa", 800, 600);
        

    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        ResultMatrixprops = (MatrixProperties)props.getPropertiesByName("ResultMatrixprops");
        input = (int[][])primitives.get("Input");
        bigsourceCodeprops = (SourceCodeProperties)props.getPropertiesByName("bigsourceCodeprops");
        InputMatrixprops = (MatrixProperties)props.getPropertiesByName("InputMatrixprops");
        InputLabelLeftprops = (MatrixProperties)props.getPropertiesByName("InputLabelLeftprops");
        OutputLabelLeftprops = (MatrixProperties)props.getPropertiesByName("OutputLabelLeftprops");
        OutputMatrixprops = (MatrixProperties)props.getPropertiesByName("OutputMatrixprops");
        InputLabelTopprops = (MatrixProperties)props.getPropertiesByName("InputLabelTopprops");
        descriptionprops = (SourceCodeProperties)props.getPropertiesByName("descriptionprops");
        OutputLabelTopprops = (MatrixProperties)props.getPropertiesByName("OutputLabelTopprops");
        sourceCodeprops = (SourceCodeProperties)props.getPropertiesByName("sourceCodeprops");
        lang.setStepMode(true);
        checkInput(input);
        //falls Input korrekt, dann Algorithmus ausführen
        if(correctinput){
        WagnerWithin WW = new WagnerWithin(lang);
        WW.wagner_within(input, ResultMatrixprops, InputMatrixprops, InputLabelLeftprops, InputLabelTopprops, OutputMatrixprops, OutputLabelTopprops, OutputLabelLeftprops, sourceCodeprops, bigsourceCodeprops, descriptionprops,translator);
        }
        //sonst Fehlermeldung erstellen
        else{
        	create_error_slide();
        }
        return lang.toString();
    }
    
    //Überprüft den Input auf Richtigkeit
    private void checkInput(int[][] input){
    	//Überprüfung, ob 4 Zeilen Input
    	if(input.length != 4){
    		correctinput=false;
    		error = translator.translateMessage("falscheZeilenanzahl");
    		return;
    	}
    	//Überprüfen, ob mind. eine Spalte
    	if(input[0].length<2){
    		correctinput=false;
    		error = translator.translateMessage("falscheSpaltenanzahl");
    		return;
    	}
    	//Überprüfung, ob alle Werte positiv
    	for(int i=0; i<4;i++){
    		for(int j =0; j<input[0].length;j++){
    			if(input[i][j]<0){
    				correctinput=false;
    				error = translator.translateMessage("negativeZahlen");
    				return;
    			}
    		}
    	}
    	correctinput=true;
    }
    //Erstellung einer Seite mit Fehlermeldung
    private void create_error_slide(){
		TextProperties titleprops = new TextProperties();
		titleprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
			    Font.BOLD, 24));
		Text errortext1 = lang.newText(new Coordinates(100,20), translator.translateMessage("Inputfehler"), "error1", null, titleprops);
		Text errortext2 = lang.newText(new Coordinates(100,60), error, "error2", null, titleprops);
		lang.nextStep();
    }
    public String getName() {
        return "Wagner-Within-Algorithmus";
    }

    public String getAlgorithmName() {
        return "Wagner-Within-Algorithmus";
    }

    public String getAnimationAuthor() {
        return "Najim Azizi, Timm Lampa";
    }

    public String getDescription(){
        return 
        		/*translator.translateMessage("desc1")
        		+"\n"+
        		translator.translateMessage("desc2")
        		+"\n"+*/
        		translator.translateMessage("desc3")
        		+"\n"+
        		translator.translateMessage("desc4")
        		+"\n"+
        		translator.translateMessage("desc5")
        		+"\n"+
        		translator.translateMessage("desc6")
        		+"\n"+
        		translator.translateMessage("desc7")
        		+"\n"+
        		translator.translateMessage("desc8")
        		+"\n"+
        		translator.translateMessage("desc9")
        		+"\n"+
        		translator.translateMessage("desc10")
        		+"\n"+
        		translator.translateMessage("desc11")
        		+"\n"+
        		translator.translateMessage("desc12")
        		+"\n"+
        		translator.translateMessage("desc13")
        		+"\n"+
        		translator.translateMessage("desc14")
        		+"\n"+
        		translator.translateMessage("desc15")
        		+"\n";
           }

    public String getCodeExample(){
        return translator.translateMessage("Code1")
        		+"\n  "+
        		translator.translateMessage("Code2")
        		+"\n  "+
        		translator.translateMessage("Code3")
        		+"\n"+
        		translator.translateMessage("Code4")
        		+"\n  "+
        		translator.translateMessage("Code5")
        		+"\n    "+
        		translator.translateMessage("Code6")
        		+"\n    "+
        		translator.translateMessage("Code7")
        		+"\n    "+
        		translator.translateMessage("Code8")
        		+"\n      "+
        		translator.translateMessage("Code9")
        		+"\n        "+
        		translator.translateMessage("Code10")
        		+"\n      "+
        		translator.translateMessage("Code11")
        		+"\n        "+
        		translator.translateMessage("Code12")
        		+"\n      "+
        		translator.translateMessage("Code13")
        		+"\n        "+
        		translator.translateMessage("Code14")
        		+"\n      "+
        		translator.translateMessage("Code15")
        		+"\n  "+
        		translator.translateMessage("Code16")
        		+"\n    "+
        		translator.translateMessage("Code17")
        		+"\n    "+
        		translator.translateMessage("Code18")
        		+"\n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return language;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}