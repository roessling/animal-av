package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class PotenzMenge implements Generator {
    private Language lang;
    private ArrayMarkerProperties markerProps;
    private String[] Menge;
    private ArrayProperties arrayProps;
    private TextProperties textProps;
    private SourceCodeProperties scProps;
    
    
    private SourceCode sc;
//    private Text header;
//    private Rect hRect;
    
    
//    private Text ausgabe;
//    private Text ausgabe2;
    private Text text1;
    private Text text2;
    private Text text3;
    private Text text4;
    private Text text5;
    private Text text6;
    private Text text7;
    private ArrayMarker i;
//    private Text actualSet;
    private StringArray actualset;
    private Text info;
    private Text info2;
    private Text aktuelleRekursion;
    private int rekCnt = -1;

    public PotenzMenge() {
    	// nothing here
    }
    
    public void init() {
		lang = new AnimalScript("Potenzmenge bestimmen", "Admir Agia", 640, 480);
		lang.setStepMode(true);
		
//		arrayProps = new ArrayProperties();
//		
//		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED); // color red
//		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
//		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY); // fill color gray
//		
//		markerProps = new ArrayMarkerProperties();
//		markerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
//		markerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "first");
		
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        markerProps = (ArrayMarkerProperties)props.getPropertiesByName("markerProps");
        Menge = (String[])primitives.get("Menge");
        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        textProps = (TextProperties)props.getPropertiesByName("textProps");
        scProps = (SourceCodeProperties)props.getPropertiesByName("scProps");
        /*lang.newText(new Coordinates(20, 80), "Potenzmenge", "title", null);*/
        getPowerSet();
        return lang.toString();
    }
    
    
public void createSourceCode() {
		
//		SourceCodeProperties scProps = new SourceCodeProperties();
//		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
//		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
//				"Monospaced", Font.PLAIN, 12));
//		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
//		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc = lang.newSourceCode(new Coordinates(20, 100),
				"sourceCode", null, scProps);
		
		sc.addCodeLine("public void getPowerSet(Vector<String[]> PowerSet) {", null, 0, null);
		sc.addCodeLine("if (Set.length == 0) {", null, 1, null);
		sc.addCodeLine("PowerSet.add(new String[] {} ;", null, 2, null);
		sc.addCodeLine("} else {",null, 1, null);
		sc.addCodeLine("String first = Set[0];", null, 2, null);
		sc.addCodeLine("removeFirst();", null, 2, null);
		sc.addCodeLine("int OldSize = PowerSet.size();", null, 2, null);
		sc.addCodeLine("getPowerSet(PowerSet);", null, 2, null);
		sc.addCodeLine("int NewSize = PowerSet.size();", null, 2, null);
		sc.addCodeLine("for (int i=Oldsize;i<Newsize;i++){", null, 2, null);
		sc.addCodeLine("String[] StringWithFirst = new String[Potenzmenge.get(i).length+1];", null, 3, null);
		sc.addCodeLine("StringWithFirst[0] = first;", null, 3, null);
		sc.addCodeLine("for (int k=0;k<PowerSet.get(i).length;k++){", null, 3, null);
		sc.addCodeLine("StringWithFirst[k+1] = PowerSet.get(i)[k];", null, 4, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("PowerSet.add(StringWithFirst);", null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);

		setSc(sc);
	}

	public SourceCode getSc() {
		return sc;
	}

	public void setSc(SourceCode sc) {
		this.sc = sc;
	}

	public void removeFirst() {
		actualset.hide();
	
		String[] NewMenge = new String[Menge.length - 1];
		for (int i = 0; i < Menge.length - 1; i++) {
			NewMenge[i] = Menge[i + 1];
		}
		Menge = NewMenge;
		 actualset = lang.newStringArray(new Coordinates(800, 100),
					Menge, "arrayMenge", null, arrayProps);
		
	}

	public void getPotenzmenge(Vector<String[]> Potenzmenge, int pos) {
		rekCnt++;
		aktuelleRekursion.hide();
		aktuelleRekursion = lang.newText(new Coordinates(20, 500),
				"Aktuelle Rekursion: " + rekCnt,"aktuelleRekursion",  null, textProps);
		int pos2 = pos;
		pos2--;
		sc.highlight(0);
		lang.nextStep();
		sc.toggleHighlight(0,1);
		lang.nextStep();
		if (Menge.length == 0) {
			Potenzmenge.add(new String[] { " " });
			sc.toggleHighlight(1,2);
			
			info2 =lang.newText(new Offset(100, 0, "info",
			        AnimalScript.DIRECTION_NW),
			        "Da die aktuelle Menge leer ist, erhalten wir als Potenzmenge nur die leere Menge.",
			        "info2", null, textProps);
			
			lang.newText(new Offset(0, 25, "potenzmenge",
			        AnimalScript.DIRECTION_NW),
			        "{}",
			        "first", null, textProps);
			lang.nextStep();
			info2.hide();
			sc.unhighlight(2);
		} else {
			sc.toggleHighlight(1,3);
			lang.nextStep();
			sc.toggleHighlight(3,4);
			lang.nextStep();
			
			sc.toggleHighlight(4,5);
			String first = Menge[0];
			removeFirst();
			lang.nextStep();
			sc.toggleHighlight(5,6);
			lang.nextStep();
			sc.toggleHighlight(6,7);
			lang.nextStep();
			
			int Oldsize = Potenzmenge.size();
			sc.unhighlight(7);
			i.increment(null, null);
      getPotenzmenge(Potenzmenge, pos2);
			rekCnt--;
			aktuelleRekursion.hide();
			aktuelleRekursion = lang.newText(new Coordinates(20, 500),
					"Aktuelle Rekursion: " + rekCnt,"aktuelleRekursion",  null, textProps);
			
			i.decrement(null, null);
			 actualset = lang.newStringArray(new Coordinates(800, 100),
						Menge, "arrayMenge", null, arrayProps);
			int Newsize = Potenzmenge.size();
			int maxSize = 0;
			sc.highlight(8);
			lang.nextStep(first + " wird zu allen bisherigen Elementen hinzugefügt.");
			sc.unhighlight(8);
			info2.hide();
			info2 =lang.newText(new Offset(100, 0, "info",
			        AnimalScript.DIRECTION_NW),
			        "Jetzt wird " + first + " zu allen Elementen der Potenzmenge hinzugefügt.",
			        "info2", null, textProps);
			for (int i=Oldsize;i<Newsize;i++){
				
				sc.highlight(9);
				lang.nextStep();
				sc.toggleHighlight(9,10);
				lang.nextStep();
				sc.toggleHighlight(10, 11);
				lang.nextStep();
				sc.unhighlight(11);
				String[] StringWithFirst = new String[Potenzmenge.get(i).length+1];
				StringWithFirst[0] = first;
				for (int k=0;k<Potenzmenge.get(i).length;k++){
					
					
					sc.highlight(12);
					lang.nextStep();
					sc.toggleHighlight(12, 13);
					lang.nextStep();
					sc.toggleHighlight(13, 14);
					lang.nextStep();
					sc.unhighlight(14);
					StringWithFirst[k+1] = Potenzmenge.get(i)[k];
				}
				
				Potenzmenge.add(StringWithFirst);
				sc.highlight(15);
				lang.newText(new Offset(0+ maxSize, 25*pos2, "first",
				        AnimalScript.DIRECTION_NW),
				        RowToString(StringWithFirst),
				        "next", null, textProps);
				maxSize = maxSize+ 10* StringWithFirst.length+15;
				lang.nextStep();
				sc.toggleHighlight(15,16);
				lang.nextStep();
				sc.unhighlight(16);
			}
			lang.nextStep();
			info2.hide();
			sc.highlight(17);
		}
		lang.nextStep();
		sc.toggleHighlight(17,18);
		lang.nextStep();
		sc.unhighlight(18);
	}
	
	public String RowToString(String[] row){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		
		for(int i=0;i<row.length-2;i++){
			sb.append(row[i]+",");
	
		}
		if(row.length>1){
		sb.append(row[row.length-2]);
		}
		sb.append("}");
		return sb.toString();
	}
	
	public Vector<String[]> getPowerSet(){
		
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.MONOSPACED, Font.BOLD, 24));
		lang.newText(new Coordinates(20, 30), "Potenzmenge bestimmen",
				"header", null, headerProps);
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		 lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, rectProps);
		lang.nextStep("Einleitung");
//		textProps = new TextProperties();
//		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
//				Font.SANS_SERIF, Font.PLAIN, 16));
		 text1 = lang.newText(new Coordinates(10, 100),
			        "•	    Als Potenzmenge bezeichnet man in der Mengenlehre",
			        "description1", null, textProps);
			    text2 =lang.newText(new Offset(25, 25, "description1",
			        AnimalScript.DIRECTION_NW),
			        "die Menge aller Teilmengen einer gegebenen Grundmenge.",
			        "description2", null, textProps);
			    
			    lang.nextStep();
			    text3 = lang.newText(new Offset(-25, 25, "description2",
			        AnimalScript.DIRECTION_NW),
			        "•	     Man notiert die Potenzmenge einer Menge X meist als P(X).",
			        "description3", null, textProps);

			   lang.nextStep();
			   
			    text4 = lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
			        "•	    Wir bestimmen die Potenzmenge rekursiv, indem wir ein Element aus",
			        "description4", null, textProps);
			    text5 = lang.newText(new Offset(25, 25, "description4", AnimalScript.DIRECTION_NW),
			        "der Menge entfernen und die Potenzmenge der restlichen Menge bestimmen. ",
			        "description5", null, textProps);
			    text6 = lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
			        "Dann wird jedes Element der Potenzmenge verdoppelt und das entfernte",
			        "description6", null, textProps);
			    text7 = lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
			        "Element wieder hinzugef&uuml;gt.",
			        "description7", null, textProps);
			    
			    
			    lang.nextStep();
			    
			
			    text1.hide();
			    text2.hide();
			    text3.hide();
			    text4.hide();
			    text5.hide();
			    text6.hide();
			    text7.hide();
		
		createSourceCode();
		lang.nextStep();
		 lang.newText(new Coordinates(550, 100),
		        "Menge: ",
		        "menge", null, textProps);
		 lang.newText(new Coordinates(700, 100),
		        "Aktuelle Menge: ",
		        "actual", null, textProps);
		actualset = lang.newStringArray(new Coordinates(800, 100),
				Menge, "arrayMenge", null, arrayProps);
		
		 info = lang.newText(new Coordinates(20, 450),
					"Bemerkung: ","info",  null, textProps);
		 
		 aktuelleRekursion = lang.newText(new Coordinates(20, 500),
					"Aktuelle Rekursion: " + rekCnt,"aktuelleRekursion",  null, textProps);
		
		StringArray set = lang.newStringArray(new Coordinates(600, 100),
				Menge, "arrayMenge", null, arrayProps);
		
		lang.newText(new Offset(0, 50, "menge",
		        AnimalScript.DIRECTION_NW),
		        "Potenzmenge: ",
		        "potenzmenge", null, textProps);
		i = lang.newArrayMarker(set, 0, "i", null, markerProps);
		
		
		Vector<String[]> collection = new Vector<String[]>();
		getPotenzmenge(collection, Menge.length+1);
		info.hide();
		aktuelleRekursion.hide();
		lang.nextStep("Potenzmenge bestimmt");
		
		return collection;
	}

    
    
    

    public String getName() {
        return "Potenzmenge";
    }

    public String getAlgorithmName() {
        return "Potenzmenge";
    }

    public String getAnimationAuthor() {
        return "Admir Agia";
    }

    public String getDescription(){
        return "Als Potenzmenge bezeichnet man in der Mengenlehre "
 +"\n"
 +"die Menge aller Teilmengen einer gegebenen Grundmenge."
 +"\n"
 +"Man notiert die Potenzmenge einer Menge X meist als P(X)."
 +"\n"
 +"Wir bestimmen die Potenzmenge rekursiv, indem wir ein Element aus"
 +"\n"
 +"der Menge entfernen und die Potenzmenge der restlichen Menge bestimmen. "
 +"\n"
 +"Dann wird jedes Element der Potenzmenge verdoppelt und das entfernte"
 +"\n"
 +"Element wieder hinzugef&uuml;gt.";
    }

    public String getCodeExample(){
        return "public void getPowerSet(Vector<String[]> PowerSet) {"
 +"\n"
 +"    if (Set.length == 0) {"
 +"\n"
 +"      PowerSet.add(new String[] {} ;"
 +"\n"
 +"    } else {"
 +"\n"
 +"         String first = Set[0];"
 +"\n"
 +"         removeFirst();"
 +"\n"
 +"         int OldSize = PowerSet.size();"
 +"\n"
 +"         getPowerSet(PowerSet);"
 +"\n"
 +"         int NewSize = PowerSet.size();"
 +"\n"
 +"         for (int i=Oldsize;i&lt;Newsize;i++){"
 +"\n"
 +"                   String[] StringWithFirst = new String[Potenzmenge.get(i).length+1];"
 +"\n"
 +"                   StringWithFirst[0] = first;"
 +"\n"
 +"                   for (int k=0;k&lt;PowerSet.get(i).length;k++){"
 +"\n"
 +"                         StringWithFirst[k+1] = PowerSet.get(i)[k];"
 +"\n"
 +"                   }"
 +"\n"
 +"                   PowerSet.add(StringWithFirst);"
 +"\n"
 +"          }"
 +"\n"
 +"     }"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}