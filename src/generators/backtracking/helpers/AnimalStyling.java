package generators.backtracking.helpers;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.HashMap;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;

import algoanim.properties.AnimationProperties;
import algoanim.properties.TextProperties;

import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;

public class AnimalStyling {
	
	private HashMap<String, AnimalObjectStyle> objects = new HashMap<String, AnimalObjectStyle>();
	
	private boolean initalized = false;
	private HashMap<Class<?>, AnimalObjectStyle> defaults = new HashMap<Class<?>, AnimalObjectStyle>();
	public static AnimationPropertiesContainer props = null;
	public AnimalStyling(){
		if(!initalized && props != null){
			createDefaults();
			
			AnimalObjectStyle header = new AnimalObjectStyle();
			//header.position = new Offset(5, 5, "header", "NW");
			header.position = new Coordinates(10, 10);
			header.element = AnimalStyling.getPropsByName("headline");
			header.element.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) header.element.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName(), Font.BOLD, 24));
			this.objects.put("header", header);
			
			AnimalObjectStyle headerBox = new AnimalObjectStyle();
			headerBox.position = new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW);
			headerBox.position2 = new Offset(5, 5, "header", AnimalScript.DIRECTION_SE);
			headerBox.element = AnimalStyling.getPropsByName("headerBox");
			this.objects.put("headerBox", headerBox);
			
			AnimalObjectStyle descriptionHeader = new AnimalObjectStyle();
			descriptionHeader.position = new Offset(20, 0, "header", "NE");
			descriptionHeader.element = AnimalStyling.getPropsByName("subheadline");
			descriptionHeader.element.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) descriptionHeader.element.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName(), Font.BOLD, 20));
			this.objects.put("descriptionHeader", descriptionHeader);
			this.objects.put("alogrythmHeader", descriptionHeader);
			
			AnimalObjectStyle description1 = new AnimalObjectStyle();
			AnimalObjectStyle description2 = new AnimalObjectStyle();
			AnimalObjectStyle description3 = new AnimalObjectStyle();
			AnimalObjectStyle description4 = new AnimalObjectStyle();
			AnimalObjectStyle description5 = new AnimalObjectStyle();
			AnimalObjectStyle description6 = new AnimalObjectStyle();
			AnimalObjectStyle description7 = new AnimalObjectStyle();
			AnimalObjectStyle description8 = new AnimalObjectStyle();
			AnimalObjectStyle description9 = new AnimalObjectStyle();
			AnimalObjectStyle description10 = new AnimalObjectStyle();
            AnimalObjectStyle description11 = new AnimalObjectStyle();
			description1.position = new Offset(0, 0, "descriptionHeader", "SW");
			description2.position = new Offset(0, 0, "description1", "SW");
			description3.position = new Offset(0, 0, "description2", "SW");
			description4.position = new Offset(0, 0, "description3", "SW");
			description5.position = new Offset(0, 0, "description4", "SW");
			description6.position = new Offset(0, 0, "description5", "SW");
			description7.position = new Offset(0, 0, "description6", "SW");
			description8.position = new Offset(0, 0, "description7", "SW");
			description9.position = new Offset(0, 0, "description8", "SW");
			description10.position = new Offset(0, 0, "description9", "SW");
			description11.position = new Offset(0, 0, "description10", "SW");
			
			this.objects.put("description1", description1);
			this.objects.put("description2", description2);
			this.objects.put("description3", description3);
			this.objects.put("description4", description4);
			this.objects.put("description5", description5);
			this.objects.put("description6", description6);
			this.objects.put("description7", description7);
			this.objects.put("description8", description8);
			this.objects.put("description9", description9);
			this.objects.put("description10", description10);
			this.objects.put("description11", description11);
                        
			AnimalObjectStyle conclusion1 = new AnimalObjectStyle();
			AnimalObjectStyle conclusion2 = new AnimalObjectStyle();
			AnimalObjectStyle conclusion3 = new AnimalObjectStyle();
			AnimalObjectStyle conclusion4 = new AnimalObjectStyle();
			AnimalObjectStyle conclusion5 = new AnimalObjectStyle();
			AnimalObjectStyle conclusion6 = new AnimalObjectStyle();
			conclusion1.position = new Offset(0, 30, "headerBox", "SW");
			conclusion2.position = new Offset(0, 0, "conclusion1", "SW");
			conclusion3.position = new Offset(0, 0, "conclusion2", "SW");
			conclusion4.position = new Offset(0, 0, "conclusion3", "SW");
			conclusion5.position = new Offset(0, 0, "conclusion4", "SW");
			conclusion6.position = new Offset(0, 0, "conclusion5", "SW");
			
			
			this.objects.put("conclusion1", conclusion1);
			this.objects.put("conclusion2", conclusion2);
			this.objects.put("conclusion3", conclusion3);
			this.objects.put("conclusion4", conclusion4);
			this.objects.put("conclusion5", conclusion5);
			this.objects.put("conclusion6", conclusion6);
			
			AnimalObjectStyle solution1 = new AnimalObjectStyle();
			AnimalObjectStyle solution2 = new AnimalObjectStyle();
			AnimalObjectStyle solution3 = new AnimalObjectStyle();
			AnimalObjectStyle solution4 = new AnimalObjectStyle();
			AnimalObjectStyle solution5 = new AnimalObjectStyle();
			AnimalObjectStyle solution6 = new AnimalObjectStyle();
            AnimalObjectStyle solution7 = new AnimalObjectStyle();
            AnimalObjectStyle solution8 = new AnimalObjectStyle();
            AnimalObjectStyle solution9 = new AnimalObjectStyle();
            AnimalObjectStyle solution10 = new AnimalObjectStyle();
            AnimalObjectStyle solution11 = new AnimalObjectStyle();
            AnimalObjectStyle solution12 = new AnimalObjectStyle();
            AnimalObjectStyle solution13 = new AnimalObjectStyle();
            //AnimalObjectStyle solution14 = new AnimalObjectStyle();
                        
			solution1.position = new Offset(0, 30, "headerBox", "SW");
			solution2.position = new Offset(0, 0, "solution1", "SW");
			solution3.position = new Offset(0, 0, "solution2", "SW");
			solution4.position = new Offset(0, 0, "solution3", "SW");
			solution5.position = new Offset(0, 0, "solution4", "SW");
			solution6.position = new Offset(0, 0, "solution5", "SW");
			solution7.position = new Offset(0, 0, "solution6", "SW");
            solution8.position = new Offset(0, 0, "solution7", "SW");
            solution9.position = new Offset(0, 0, "solution8", "SW");
            solution10.position = new Offset(0, 0, "solution9", "SW");
            solution11.position = new Offset(0, 0, "solution10", "SW");
            solution12.position = new Offset(0, 0, "solution11", "SW");
            solution13.position = new Offset(0, 0, "solution12", "SW");
            //solution14.position = new Offset(0, 0, "solution13", "SW");
                        
			this.objects.put("solution1", solution1);
			this.objects.put("solution2", solution2);
			this.objects.put("solution3", solution3);
			this.objects.put("solution4", solution4);
			this.objects.put("solution5", solution5);
			this.objects.put("solution6", solution6);
			this.objects.put("solution7", solution7);
	        this.objects.put("solution8", solution8);
	        this.objects.put("solution9", solution9);
	        this.objects.put("solution10", solution10);
	        this.objects.put("solution11", solution11);
	        this.objects.put("solution12", solution12);
	        this.objects.put("solution13", solution13);
	        //this.objects.put("solution14", solution14);
                        
			AnimalObjectStyle fieldsUpdatedDesc = new AnimalObjectStyle();
			AnimalObjectStyle fieldsUpdatedDesc1 = new AnimalObjectStyle();
			AnimalObjectStyle fieldsUpdatedDesc2 = new AnimalObjectStyle();
                        
			Font defaultfont =  ((Font) defaults.get(Text.class).element.get(AnimationPropertiesKeys.FONT_PROPERTY));
			Font newfont = defaultfont.deriveFont(Math.round(defaultfont.getSize()*0.8));
			
			fieldsUpdatedDesc1.position = new Offset(0, 14, "dequeueText", "SW");
			fieldsUpdatedDesc2.position = new Offset(0, 0, "fieldsUpdatedDesc1", "SW");
			
			fieldsUpdatedDesc.display = defaults.get(Text.class).display;
			fieldsUpdatedDesc.element = new TextProperties();
			fieldsUpdatedDesc.element.set(AnimationPropertiesKeys.FONT_PROPERTY, newfont); 
					
			fieldsUpdatedDesc.element.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
			fieldsUpdatedDesc1.element=fieldsUpdatedDesc.element;
			fieldsUpdatedDesc1.display = fieldsUpdatedDesc.display;
			
			fieldsUpdatedDesc2.element=fieldsUpdatedDesc.element;
			fieldsUpdatedDesc2.display = fieldsUpdatedDesc.display;
			this.objects.put("fieldsUpdatedDesc1", fieldsUpdatedDesc1);
			this.objects.put("fieldsUpdatedDesc2", fieldsUpdatedDesc2);
			
		}
	}
	
	private void createDefaults() {
		AnimalObjectStyle text = new AnimalObjectStyle();
		text.display = null;
		if(props != null)
		text.element = props.getPropertiesByName("text");
		this.defaults.put(Text.class, text);
	}

	public AnimalObjectStyle getDefault(Class<?> className) {
		AnimalObjectStyle obj =  this.defaults.get(className);
		Font defaultFont = (Font) obj.element.get(AnimationPropertiesKeys.FONT_PROPERTY);
		obj.position = new OffsetFromLastPosition(0, defaultFont.getSize());
		return obj;
	}

	public AnimalObjectStyle get(String name){
		AnimalObjectStyle style = this.objects.get(name);
		return style;
	}

	public static void setProps(AnimationPropertiesContainer props) {
		AnimalStyling.props=props;
	}

	public static AnimationProperties getPropsByName(String prop) {
		return AnimalStyling.props.getPropertiesByName(prop);
	}

}
