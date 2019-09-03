package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class HashfunktionAusKompressionsfunktion implements ValidatingGenerator {
    
	private PolylineProperties binaryArrowProps;
    private SourceCodeProperties sourceCode;
    private int b;
    private int[] Nachricht_m;
    private SourceCodeProperties infoTexte;
    private ArrayProperties messageArray;
    private RectProperties rect;
    private TextProperties headerProps;
    private TextProperties textProps;
    private Color highlightColorText;
    private Color NormalColorText;
    private int stepLength;
	private Language lang;
	
	public Language getLang() {
		return lang;
	}
	
	public void init(){
        lang = new AnimalScript("Hashfunktion aus Kompressionsfunktion", "Maurice Wendt, Dominik Gopp", 800, 600);
        this.lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        binaryArrowProps = (PolylineProperties)props.getPropertiesByName("binaryArrow");
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        infoTexte = (SourceCodeProperties)props.getPropertiesByName("infoTexte");
        messageArray = (ArrayProperties)props.getPropertiesByName("messageArray");
        NormalColorText = (Color)sourceCode.get("color");
        highlightColorText = (Color)sourceCode.get("highlightColor");
        rect = (RectProperties)props.getPropertiesByName("rect");
        headerProps = (TextProperties)props.getPropertiesByName("header");
        
        textProps = (TextProperties)props.getPropertiesByName("textProp");
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, NormalColorText);
        
        rect.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        infoTexte.set(AnimationPropertiesKeys.DEPTH_PROPERTY,0);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
        sourceCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        String[] m = intArrayToStringArray(Nachricht_m);
        start(m,b,"xor");
        
        return lang.toString();
    }

	private String[] intArrayToStringArray(int[] nachricht_m2) {
		String[] m = new String[nachricht_m2.length];
		for(int i = 0; i < nachricht_m2.length; i++)
			m[i] = nachricht_m2[i]>0 ? "1" : "0";
		return m;	
	}

	private void start(String[] m, int b, String type) {
		int a = 2*b;
		
		List<StringArray> list = new ArrayList<StringArray>();
		List<StringArray> listSaveAfterMove = new ArrayList<StringArray>();

		int r = a - b;
		
	    Text header = lang.newText(new Coordinates(5, 15), "Hashfunktion aus Kompressionsfunktion", "header", null, headerProps);
		
	    SourceCode introText = lang.newSourceCode(new Offset(200, 120, "header", AnimalScript.DIRECTION_NW), "introText", null, infoTexte);
	    introText.addCodeLine("Wir wollen nun für eine gegebene Nachricht m den Hashwert h(m) berechnen. Die ", null, 0, null);
	    introText.addCodeLine("Hashfunktion h bleibt dabei unbekannt.", null, 0, null);
	    introText.addCodeLine("Zunächst benötigt man eine Kompressionsfunktion g, welche sich dadurch kennzeichnet, dass", null, 0, null);
	    introText.addCodeLine("sie einen Binärstring der Länge a bekommt und einen Binärstring der Länge b zurückliefert. ", null, 0, null);
	    introText.addCodeLine("Sie heißt deshalb Kompressionsfunktion, weil b zwingend kleiner als a sein muss und der", null, 0, null);
	    introText.addCodeLine("String damit komprimiert wird.", null, 0, null);
	    introText.addCodeLine("", null, 0, null);
	    introText.addCodeLine("Aus der Differenz zwischen a und b, wird nun ein Parameter r= a-b berechnet. Dieser ", null, 0, null);
	    introText.addCodeLine("Parameter gibt weiter die Blocklänge vor, in die die Nachricht m aufgeteilt wird. Gegebenenfalls", null, 0, null);
	    introText.addCodeLine("muss der letzte Block noch durch Nullen aufgefüllt werden. Anschließend wird die Länge der", null, 0, null);
	    introText.addCodeLine("Nachricht ebenfalls in die Blockformatierung umgewandelt und an die bisherige Blockfolge ", null, 0, null);
	    introText.addCodeLine("angehängt.", null, 0, null);
	    introText.addCodeLine("", null, 0, null);
	    introText.addCodeLine("Nach dieser Formatierung wird nun beginnend mit einem Nullblock H_0 der Länge r die", null, 0, null);
	    introText.addCodeLine("Konkatenation des Ergebnisses H_i der i-ten Berechnung und dem i-ten Block der ", null, 0, null);
	    introText.addCodeLine("Formatierung gebildet und darauf die Kompressionsfunktion angewendet. Das Ergebnis der ", null, 0, null);
	    introText.addCodeLine("letzten Berechnung beschreibt nun den Hashwert H(m).", null, 0, null);

	    Rect infoRect = lang.newRect(new Offset(-10,-10, "introText", AnimalScript.DIRECTION_NW), new Offset(10,10,"introText",AnimalScript.DIRECTION_SE), "infoRect",null, rect);
	    lang.nextStep("Intro");
	    
	    infoRect.hide();
	    introText.hide();
	    
		TextProperties exponent = new TextProperties();
		exponent.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 9));
		exponent.set(AnimationPropertiesKeys.COLOR_PROPERTY, NormalColorText);

		TextProperties exponentFunction = new TextProperties();
		exponentFunction.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		
		TextProperties indexH = new TextProperties();
		exponentFunction.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		exponentFunction.set(AnimationPropertiesKeys.COLOR_PROPERTY, NormalColorText);

		TextProperties indizes = new TextProperties();
		indizes.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 7));
		indizes.set(AnimationPropertiesKeys.COLOR_PROPERTY,NormalColorText);
		
		TextProperties text_x = new TextProperties();
		text_x.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		text_x.set(AnimationPropertiesKeys.COLOR_PROPERTY, NormalColorText);

		SourceCode sc = lang.newSourceCode(new Offset(7, 120, "header", AnimalScript.DIRECTION_NW), "code", null, sourceCode);
		sc.addCodeLine(" 1. Gegeben eine Nachricht m und eine Kompressionsfunktion g:{0,1} → {0,1} ", null, 0, null);
		sc.addCodeLine(" 2. Sei nun r = a-b die Länge der Nachrichtenblöcke", null, 0, null);
		sc.addCodeLine(" 3. Teile m von rechts in Blöcke der Länge r ein", null, 0, null);
		sc.addCodeLine(" 4. hänge links soviele Nullen an, dass der String durch r teilbar ist", null, 0, null);
		sc.addCodeLine(" 5. hänge rechts r-Nullen an", null, 0, null);
		sc.addCodeLine(" 6. bestimme die Länge l der Nachricht m", null, 0, null);
		sc.addCodeLine(" 7. teile l in Blöcke der Länge r-1 auf", null, 0, null);
		sc.addCodeLine(" 8. fülle die Blöcke ggf. von links mit Nullen auf, damit sie die Länge r-1 haben", null, 0, null);
		sc.addCodeLine(" 9. hänge an jeden Block eine führende 1 an", null, 0, null);
		sc.addCodeLine("10. hänge diese Blöcke von rechts an den Ursprungsstring an", null, 0, null);
		sc.addCodeLine("11. Sei x = x ... x der erzeugte String, wobei x e {0,1}, 1≤i≤t und t = size(x)/r", null, 0, null);
		sc.addCodeLine("12. Sei H = 0", null, 0, null);
		sc.addCodeLine("13. Berechne H = g(H  ∘ x ) für alle i mit 1 ≤ i ≤ t", null, 0, null);
		sc.addCodeLine("14. Schließlich setzt man h(x) = H . Dies ist der Hashwert von m", null, 0, null);

		Text code1_up_a = lang.newText(new Offset(461, -7,"code",AnimalScript.DIRECTION_NW), "a", "code1_up_a", null, exponent);
		Text code1_up_b = lang.newText(new Offset(55, 0, "code1_up_a", AnimalScript.DIRECTION_NE), "b", "code1_up_b", null, exponent);	

		Text code11_down_1 = lang.newText(new Offset(90, 168, "code", AnimalScript.DIRECTION_NW), "1", "code11_down_1", null, indizes);
		Text code11_down_t = lang.newText(new Offset(43, -1, "code11_down_1", AnimalScript.DIRECTION_NW), "t", "code11_down_t", null, indizes);
		Text code11_down_i = lang.newText(new Offset(247, 0, "code11_down_1", AnimalScript.DIRECTION_NW), "i", "code11_down_i", null, indizes);
		Text code11_up_r = lang.newText(new Offset(304, -8, "code11_down_1", AnimalScript.DIRECTION_NW), "r", "code11_up_r", null, exponent);			

		Text code12_down_0 = lang.newText(new Offset(64, 182, "code", AnimalScript.DIRECTION_NW), "0", "code12_down_0", null, indizes);
		Text code12_up_b = lang.newText(new Offset(27, -7, "code12_down_0", AnimalScript.DIRECTION_NW), "b", "code12_up_r", null, exponent);	

		Text code13_down_i = lang.newText(new Offset(99, 199, "code", AnimalScript.DIRECTION_NW), "i", "code13_down_i", null, indizes);
		Text code13_down_i_1 = lang.newText(new Offset(42, 0, "code13_down_i", AnimalScript.DIRECTION_NW), "i-1", "code13_down_i_1", null, indizes);
		Text code13_down_i_2= lang.newText(new Offset(78, 0, "code13_down_i", AnimalScript.DIRECTION_NW), "i", "code13_down_i2", null, indizes);			

		Text code14_down_t = lang.newText(new Offset(239, 217, "code", AnimalScript.DIRECTION_NW), "t", "code14_down_t", null, indizes);
		
		lang.nextStep();
		
		sc.highlight(0);
		code1_up_a.changeColor(null, highlightColorText, null, null);
		code1_up_b.changeColor(null, highlightColorText, null, null);
		lang.newText(new Offset(650, 30, "header", AnimalScript.DIRECTION_SW), "m = ", "message", null, textProps);
		StringArray arr_m = lang.newStringArray(new Offset(0, 0, "message", AnimalScript.DIRECTION_NE), m, "arr_m", null, messageArray);
		lang.newText(new Offset(0, 20, "message", AnimalScript.DIRECTION_SW), "g: {0,1} → {0,1} , g(k||x) = k "+type+ " x", "function", null, textProps);
		Text function_up_a = lang.newText(new Offset(65, -11, "function", AnimalScript.DIRECTION_NW), String.valueOf(a), "function_up_a", null, exponentFunction);
		Text function_up_b = lang.newText(new Offset(73, 0, "function_up_a", AnimalScript.DIRECTION_NW), String.valueOf(b), "function_up_b", null, exponentFunction);
		
		lang.nextStep();
		
		sc.unhighlight(0);
		function_up_a.changeColor(null, highlightColorText, null, null);
		function_up_b.changeColor(null, highlightColorText, null, null);
		code1_up_a.changeColor(null, NormalColorText, null, null);
		code1_up_b.changeColor(null, NormalColorText, null, null);
		sc.highlight(1);
		Text parameter = lang.newText(new Offset(0, 20, "function", AnimalScript.DIRECTION_SW), "r = "+a+" - "+b+" = "+r, "parameter", null, textProps);
		parameter.changeColor(null, highlightColorText, null, null);
		
		lang.nextStep();
		
		function_up_a.changeColor(null, NormalColorText, null, null);
		function_up_b.changeColor(null, NormalColorText, null, null);
		parameter.changeColor(null, NormalColorText, null, null);
		sc.unhighlight(1);
		sc.highlight(2);
		int arrayCount = 0;
		int subArrayCount; 
		if(m.length % r > 0)
			subArrayCount = m.length/r +1;
		else
			subArrayCount = m.length/r;
		StringArray arrs[] = new StringArray[subArrayCount];
		for(int i = m.length-1; i>=0; i = i-r){
			int count = i;
			int CellData = 0;
			arrayCount++;
			String arr[] = new String[r];
			for(int j = r-1; j >= 0; j--){
				if(count >= 0){
					arr[j] = m[count];
					CellData++;
				}
				else
					arr[j] = "  ";
				
				count--;
			}
				
			if(arrayCount == 1){
				arrs[arrayCount-1] = lang.newStringArray(new Offset(0, 150, "arr_m", AnimalScript.DIRECTION_NE), arr, "arr1", null, messageArray);
				arr_m.highlightCell(arr_m.getLength()-r, arr_m.getLength()-1, null, null);
				arrs[arrayCount-1].highlightCell(r-CellData, r-1, null, null);
							
				lang.nextStep();
			}		
			else {
				for(int pos = arr_m.getLength()-(r*(arrayCount-1)); pos < arr_m.getLength()-(r*(arrayCount-2)); pos++)
					arr_m.unhighlightCell(pos, null, null);
				for(int pos = 0; pos < arrs[arrayCount-2].getLength(); pos++)
					arrs[arrayCount-2].unhighlightCell(pos, null, null);
				arrs[arrayCount-1]= lang.newStringArray(new Offset(-13*r-10, 0, "arr"+(arrayCount-1), AnimalScript.DIRECTION_NW), arr, "arr"+arrayCount, null, messageArray);
				if(arr_m.getLength()-r*arrayCount >= 0)
					arr_m.highlightCell(arr_m.getLength()-r*arrayCount, arr_m.getLength()-1-(r*(arrayCount-1)), null, null);
				else
					arr_m.highlightCell(0, arr_m.getLength()-1-(r*(arrayCount-1)), null, null);

				arrs[arrayCount-1].highlightCell(r-CellData, r-1, null, null);
				
				lang.nextStep();				
			}
		}
		
		sc.unhighlight(2);
		sc.highlight(3);
		for(int pos = 0; pos < r; pos++){
			arr_m.unhighlightCell(pos, null, null);
			arrs[arrayCount-1].unhighlightCell(pos, null, null);
		}
		int leftblankCount = 0;
		for(int pos = 0; pos < r; pos++){
			
			if(arrs[arrayCount-1].getData(pos).equals("  "))
				leftblankCount++;
		}
		for(int pos = 0; pos < leftblankCount; pos++){
			arrs[arrayCount-1].put(pos, "0", null, null);
			arrs[arrayCount-1].highlightCell(pos, null, null);		
		}
		
		lang.nextStep();
		
		sc.unhighlight(3);
		sc.highlight(4);

		for(int i = 0; i < leftblankCount; i++)
			arrs[arrayCount-1].unhighlightCell(i, null, null);
		
		String[] r_zeros_data = new String[r];
		for(int i = 0; i < r; i++)
			r_zeros_data[i] = "0";
		StringArray r_zeros = lang.newStringArray(new Offset(12, 0, "arr1", AnimalScript.DIRECTION_NE), r_zeros_data, "r_zeros", null, messageArray);
		r_zeros.highlightCell(0, r-1, null, null);
		
		lang.nextStep();
		
		sc.unhighlight(4);
		sc.highlight(5);
		for(int pos = 0; pos < r; pos++)
			r_zeros.unhighlightCell(pos, null, null);
		
		Offset[] set = new Offset[] {new Offset(0, -11, "arr_m", AnimalScript.DIRECTION_NW), new Offset(0, -11, "arr_m", AnimalScript.DIRECTION_NE)};
		Polyline sizeOf = lang.newPolyline(set, "sizeOf", null, binaryArrowProps);	
		Text sizeOfText = lang.newText(new Offset(15, -5, "arr_m",AnimalScript.DIRECTION_E), "size(m) = "+arr_m.getLength(), "sizeOfText", null, textProps);
		sizeOfText.changeColor(null, highlightColorText, null, null);
		
		lang.nextStep();
		
		sizeOf.hide();
		
		set = new Offset[] {new Offset(10, 15, "sizeOfText", AnimalScript.DIRECTION_NE), new Offset(90, 15, "sizeOfText", AnimalScript.DIRECTION_NE)};
		sizeOf.getProperties().set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
		Polyline binaryArrow = lang.newPolyline(set, "binaryArrow", null, binaryArrowProps);
		Text binaryArrowText = lang.newText(new Offset(-27, -23, "binaryArrow", AnimalScript.DIRECTION_N), "binär", "binaryArrowText", null, textProps);
		binaryArrowText.changeColor(null, NormalColorText, null, null);

		String size_m_to_binary = Integer.toBinaryString(arr_m.getLength());
		String[] stringArray = new String[size_m_to_binary.length()];
		for(int i = 0; i < size_m_to_binary.length(); i++)
			stringArray[i] = String.valueOf(size_m_to_binary.charAt(i));		
		StringArray binary = lang.newStringArray(new Offset(10, -11, "binaryArrow", AnimalScript.DIRECTION_NE), stringArray, "binary0", null, messageArray);
		
		lang.nextStep();
		
		sizeOfText.hide();
		binaryArrowText.hide();
		binaryArrow.hide();
		binary.hide();
		sc.unhighlight(5);
		sc.highlight(6);		
		int binaryLength = binary.getLength();
		int numOfArrays = 1;
		while(binaryLength>=r-1) {
			String[] data = new String[r-1];
			for (int i = r-2; i >= 0; i--) {
				data[i]= binary.getData(binaryLength-1);
				binaryLength--;
			}
			if(numOfArrays==1)
				list.add(lang.newStringArray(new Offset(-12, 0, "binary"+(numOfArrays-1), AnimalScript.DIRECTION_NE), data, "binary"+numOfArrays, null, messageArray));
			else
				list.add(lang.newStringArray(new Offset(-13*r-10, 0, "binary"+(numOfArrays-1), AnimalScript.DIRECTION_NW), data, "binary"+numOfArrays, null, messageArray));			
			
			numOfArrays++;
		}
		boolean hasRest = binaryLength > 0;
		if(hasRest){
			String[] rest = new String[binaryLength];
			for (int i = binaryLength-1; i >= 0; i--) {
				rest[i]=binary.getData(i);
				binaryLength--;
			}
			list.add(lang.newStringArray(new Offset(-(13*(rest.length+1))-10, 0, "binary"+(numOfArrays-1), AnimalScript.DIRECTION_NW), rest, "binary"+numOfArrays, null, messageArray));			
			
			lang.nextStep();
			
			list.get(list.size()-1).hide();
			String[] data = new String[r-1];
			int array_length = data.length-1;
			for(int j = data.length; j >= 0; j--){
				try{
					data[array_length] = list.get(list.size()-1).getData(j);
					array_length--;
				} catch(IndexOutOfBoundsException e){}		
			}
			for(int j = 0; j < data.length-1; j++){
				if(data[j] == null)
					data[j] = "  ";
			}
			list.remove(list.size()-1);
			list.add(lang.newStringArray(new Offset(-13*r-10, 0, "binary"+(numOfArrays-1), AnimalScript.DIRECTION_NW), data, "binary"+String.valueOf(list.size()+1), null,messageArray));
			
		}
		
		lang.nextStep();
		
		sc.unhighlight(6);
		sc.highlight(7);
		for(int j = 0; j < list.get(list.size()-1).getLength(); j++){
			if(list.get(list.size()-1).getData(j).equals("  ")){
				list.get(list.size()-1).put(j, "0", null, null);
				list.get(list.size()-1).highlightCell(j, null, null);
			}
		}
		
		lang.nextStep();
		
		sc.unhighlight(7);
		sc.highlight(8);
		for(int j = 0; j < list.size(); j++){
			list.get(j).hide();
			String[] data_block = new String[r];
			data_block[0] = "1";
			for(int z = list.get(j).getLength()-1; z >= 0; z--){
				data_block[z+1] = list.get(j).getData(z);
			}			
			list.set(j, lang.newStringArray(new Offset(-14 , 0, list.get(j).getName(), AnimalScript.DIRECTION_NW), data_block, list.get(j).getName(), null, messageArray));
			list.get(j).highlightCell(0, null, null);
		}
		
		lang.nextStep();
		
		sc.unhighlight(8);
		sc.highlight(9);
		for(int j = list.size()-1; j >=0; j--) {
			list.get(j).unhighlightCell(0, null, null);
			StringArray current = list.get(j);
			Offset x;
			if(j==list.size()-1)
				x = new Offset(10, 0, r_zeros.getName(),AnimalScript.DIRECTION_NE);		
			else
				x = new Offset(10, 0, list.get(j+1).getName(),AnimalScript.DIRECTION_NE);
			
			list.set(j, lang.newStringArray(x, convertStringArray(list.get(j)), current.getName(), null,  messageArray));
			list.get(j).hide();
			current.moveTo(null, "translate", x, null, new Timing(50) {
				@Override
				public String getUnit() {
					return "ticks";
				}
			});
			listSaveAfterMove.add(current);			
		}
		lang.nextStep();
		list.add(r_zeros);
		for(int i = 0; i < arrs.length; i++)
			list.add(arrs[i]);	
		sc.unhighlight(9);
		sc.highlight(10);
		code11_down_1.changeColor(null, highlightColorText, null, null);
		code11_down_t.changeColor(null, highlightColorText, null, null);
		code11_down_i.changeColor(null, highlightColorText, null, null);
		code11_up_r.changeColor(null, highlightColorText, null, null);
		Text[] textX = new Text[list.size()];
		Text[] textX_index = new Text[list.size()];
		int index_count = list.size();
		for(int i = 0; i < list.size(); i++) {
			if(i>0)
				textX[i] = lang.newText(new Offset(-5, 5, list.get(i).getName(), AnimalScript.DIRECTION_S), "x", "x"+String.valueOf(i+1), null, text_x);
			else
				textX[i] = lang.newText(new Offset(-5, -1, list.get(i).getName(), AnimalScript.DIRECTION_S), "x", "x"+String.valueOf(i+1), null, text_x);
			textX[i].changeColor(null, highlightColorText, null, null);
			textX_index[i] = lang.newText(new Offset(0, -13, "x"+String.valueOf(i+1), AnimalScript.DIRECTION_SE), String.valueOf(index_count), "x"+String.valueOf(i+1)+"_index", null, indizes);
			textX_index[i].changeColor(null, highlightColorText, null, null);
			index_count--;
		}
		
		lang.nextStep();
		
		for(int i = 0; i < textX.length; i++){
			textX[i].changeColor(null, NormalColorText, null, null);
			textX_index[i].changeColor(null, NormalColorText, null, null);
		}
		sc.unhighlight(10);
		sc.highlight(11);
		code12_down_0.changeColor(null, highlightColorText, null, null);
		code12_up_b.changeColor(null, highlightColorText, null, null);
		code11_down_1.changeColor(null, NormalColorText, null, null);
		code11_down_i.changeColor(null, NormalColorText, null, null);
		code11_down_t.changeColor(null, NormalColorText, null, null);
		code11_up_r.changeColor(null, NormalColorText, null, null);
		set = new Offset[]{new Offset(0, 260, "message", AnimalScript.DIRECTION_NW), new Offset(120, 260, "message", AnimalScript.DIRECTION_NW)};
		binaryArrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
		lang.newPolyline(set, "table_hori", null, binaryArrowProps);
		lang.newText(new Offset(12, -20, "table_hori", AnimalScript.DIRECTION_NW), "i         H", "tableHeading", null, textProps);
		lang.newText(new Offset(2, -20, "tableHeading", AnimalScript.DIRECTION_SE), "i", "tableheading_down_i", null, indexH);
		for(int i = 0; i < list.size()+1; i++){
			if(i == 0)
				lang.newText(new Offset(7, 20, "table_hori", AnimalScript.DIRECTION_NW), String.valueOf(i), "table_entry"+i, null, textProps);
			else{
				int decCount = String.valueOf(i).length()-1;
				lang.newText(new Offset((-10)*decCount-11, 40, "table_entry"+String.valueOf(i-1), AnimalScript.DIRECTION_NE), String.valueOf(i), "table_entry"+i, null, textProps);
			}
		}
		set = new Offset[]{new Offset(38, -35, "table_hori", AnimalScript.DIRECTION_NW), new Offset(31, 30, "table_entry"+String.valueOf(list.size()), AnimalScript.DIRECTION_NW)};
		lang.newPolyline(set, "table_verti", null, binaryArrowProps);
		String[] h_0_data = new String[b];
		for(int i = 0; i < h_0_data.length; i++)
			h_0_data[i] = "0";
		StringArray h_i = lang.newStringArray(new Offset(40, 0, "table_entry0", AnimalScript.DIRECTION_NE), h_0_data, "H_0", null, messageArray);
		h_i.highlightCell(0, h_i.getLength()-1, null, null);
		
		lang.nextStep("Berechnung der H_i");
		
		code12_down_0.changeColor(null, NormalColorText, null, null);
		code12_up_b.changeColor(null, NormalColorText, null, null);
		sc.unhighlight(11);
		sc.highlight(12);
		code13_down_i.changeColor(null, highlightColorText, null, null);
		code13_down_i_1.changeColor(null, highlightColorText, null, null);
		code13_down_i_2.changeColor(null, highlightColorText, null, null);
		for(int i = 0; i < h_i.getLength(); i++)
			h_i.unhighlightCell(i, null, null);
		StringArray save = null;
		int list_size = list.size();
		for(int i = 0; i < list_size; i++){	
			String[] newH_i = new String[r];
			
			for(int j = 0; j < newH_i.length; j++)
				newH_i[j] = "  ";
			
			save = lang.newStringArray(new Offset(40, 0, "table_entry"+String.valueOf(i+1), AnimalScript.DIRECTION_NE), newH_i, "H_"+String.valueOf(i+1), null, messageArray);
			
			if(i > stepLength){
				if(list.size() != listSaveAfterMove.size())
					list.get(list.size()-1).highlightCell(0, list.get(list.size()-1).getLength()-1, null, null);
				else
					listSaveAfterMove.get(0).highlightCell(0, listSaveAfterMove.get(0).getLength()-1, null, null);					
				h_i.highlightCell(0, h_i.getLength()-1, null, null);
				lang.nextStep();				
			}
			
			for(int j = r-1; j >= 0; j--) {
				save.put(j, String.valueOf(Integer.valueOf(h_i.getData(j)) ^ Integer.valueOf(list.get(list.size()-1).getData(j))), null, null);
				if(i <= stepLength) {
					h_i.highlightCell(j, null, null);
					if(list.size()!= listSaveAfterMove.size()){
						list.get(list.size()-1).highlightCell(j, null, null);
						lang.nextStep();
						list.get(list.size()-1).unhighlightCell(j, null, null);
					}
					else{
						listSaveAfterMove.get(0).highlightCell(j, null, null);
						lang.nextStep();
						listSaveAfterMove.get(0).unhighlightCell(j, null, null);
					}
					h_i.unhighlightCell(j, null, null);
					save.highlightCell(j, null, null);
					
					lang.nextStep();
					
					save.unhighlightCell(j, null, null);
				}
				
				else {
					save.highlightCell(0, save.getLength()-1, null, null);
					for(int l = 0; l < h_i.getLength(); l++){
						if(list.size() != listSaveAfterMove.size())
							list.get(list.size()-1).unhighlightCell(l, null, null);
						else
							listSaveAfterMove.get(0).unhighlightCell(l, null, null);
						h_i.unhighlightCell(l, null, null);
					}
				}
			}
			if(list.size() <= listSaveAfterMove.size())
				listSaveAfterMove.remove(0);
			
			if(i > stepLength) {				
				lang.nextStep();

				for(int j = 0; j < h_i.getLength(); j++){
					if(list.size() != listSaveAfterMove.size())
						list.get(list.size()-1).unhighlightCell(j, null, null);
					else{
						listSaveAfterMove.get(0).unhighlightCell(j, null, null);
					}											
					h_i.unhighlightCell(j, null, null);
				}
				if(list.size() == listSaveAfterMove.size())
					listSaveAfterMove.remove(0);
					
			}
			list.remove(list.size()-1);
			h_i = save;
		}
				
		sc.unhighlight(12);
		sc.highlight(13);
		code13_down_i.changeColor(null, NormalColorText, null, null);
		code13_down_i_1.changeColor(null, NormalColorText, null, null);
		code13_down_i_2.changeColor(null, NormalColorText, null, null);
		code14_down_t.changeColor(null, highlightColorText, null, null);
		Text result = lang.newText(new Offset(70, 30, "tableHeading", AnimalScript.DIRECTION_NE), "h(m) = ", "result", null, textProps);
		result.changeColor(null, highlightColorText, null, null);
		lang.newStringArray(h_i.getUpperLeft(), convertStringArray(h_i), h_i.getName()+"_duplicat",null, messageArray);
		h_i.highlightCell(0, h_i.getLength()-1, null, null);
		h_i.moveTo(null, "translate", new Offset(5, 5, "result", AnimalScript.DIRECTION_NE), null, new Timing(75) {			
			@Override
			public String getUnit() {
				return "ticks";
			}
		});
		
		lang.nextStep();
		
		lang.hideAllPrimitives();
		header.show();
		infoRect.show();
		SourceCode outroText = lang.newSourceCode(new Offset(200, 120, "header", AnimalScript.DIRECTION_NW), "outroText", null, infoTexte);
		outroText.addCodeLine("", null, 0, null);
		outroText.addCodeLine("", null, 0, null);
		outroText.addCodeLine("Wie anfangs beschrieben ist das letzte Ergebnis der Berechnung der Hashwert H(m) unserer", null, 0, null);
		outroText.addCodeLine("Nachricht. Es ist uns somit gelungen, ohne Verwendung einer konkreten Funktionsvorschrift", null, 0, null);
		outroText.addCodeLine("für die Hashfunktion aus der Nachricht m den Hashwert H(m) zu berechnen. Da die", null, 0, null);
		outroText.addCodeLine("Konstruktion einer kollisionsresistenten Hashfunktion sehr aufwendig ist, ist dies ein", null, 0, null);
		outroText.addCodeLine("wertvolles Ergebnis. Da der Begriff der Kollisionsresistenz nicht formal definiert ist, ", null, 0, null);
		outroText.addCodeLine("kann kein mathematischer Satz daraus gefolgert werden, dass auch die Hashfunktion ", null, 0, null);
		outroText.addCodeLine("kollisionsresistent ist. Nimmt man jedoch an, dass die Kompressionsfunktion g, insofern ", null, 0, null);
		outroText.addCodeLine("kollisionsresistent ist, dass es uns nicht in adäquater Zeit gelingt, eine entsprechende ", null, 0, null);
		outroText.addCodeLine("Kollision zu finden, so kann man zeigen, dass dies auch nicht für den Hashwert möglich ist.", null, 0, null);
		outroText.addCodeLine("", null, 0, null);
		outroText.addCodeLine("Eine Beweisskizze dazu findet sich in ¨Einführung in die Kryptographie¨ von Professor ", null, 0, null);
		outroText.addCodeLine("Johannes A. Buchmann.", null, 0, null);	
		lang.nextStep("Outro");
	}
	
	private String[] convertStringArray(StringArray s) {
		String[] array = new String[s.getLength()];
		for (int i = 0; i < s.getLength(); i++) {
			array[i] = s.getData(i);
		}
		return array;
	}
	
	public String getName() {
        return "Hashfunktion aus Kompressionsfunktion";
    }

    public String getAlgorithmName() {
        return "Hashfunktion aus Kompressionsfunktion";
    }

    public String getAnimationAuthor() {
        return "Maurice Wendt, Dominik Gopp";
    }

    public String getDescription(){
        return "In der Kryptographie ist es allgemein schwierig kollisionsresistente Hash-Funktionen zu finden."
        		+"\n"
        		+"Es wird also nach einem Verfahren gesucht, solche Funktionen zu konstruieren. Ralph Merkle"
        		+"\n"
        		+"hat dazu ein Verfahren entwickelt, wie wir aus einer gegebenen kollisionsresistenten"
        		+"\n"
        		+"Kompressionsfunktion eine kollisionsresistente Hashfunktion konstruieren k&ouml;nnen. Dieses"
        		+"\n"
        		+"Verfahren wird im Folgenden anhand einer einfachen Kompressionsfunktion beschrieben.";
    }

    public String getCodeExample(){
        return "1. Gegeben eine Nachricht m und eine Kompressionsfunktion g:{0,1}^a -> {0,1}^b"
				 +"\n"
				 +"2. Sei nun r = a-b die L&auml;nge der Nachrichtenbl&ouml;cke"
				 +"\n"
				 +"3. Teile m von rechts in Bl&ouml;cke der L&auml;nge r ein"
				 +"\n"
				 +"4. h&auml;nge links soviele Nullen an, dass der String durch r teilbar ist"
				 +"\n"
				 +"5. h&auml;nge rechts r Nullen an"
				 +"\n"
				 +"6. bestimme die L&auml;nge l der Nachricht m"
				 +"\n"
				 +"7. teile l in Bl&ouml;cke der L&auml;nge r-1 auf"
				 +"\n"
				 +"8. f&uuml;lle die Bl&ouml;cke ggf. von links mit Nullen auf, damit sie die L&auml;nge r-1 haben"
				 +"\n"
				 +"9. h&auml;nge an jeden Block eine f&uuml;hrende 1 an"
				 +"\n"
				 +"10. h&auml;nge diese Bl&ouml;cke von rechts an den Ursprungsstring an"
				 +"\n"
				 +"11. Sei x = x_1 ... x_t der erzeugte String, wobei x e {0,1}^r, t >= i >= 1 und t = size(x)/r"
				 +"\n"
				 +"12. Sei H_0 = 0^r"
				 +"\n"
				 +"13. Berechne H_i = g(H_i-1  o x_i ) f&uuml;r alle i mit t >= i >= 1"
				 +"\n"
				 +"14. Schlie&szlig;lich setzt man h(x) = H_t. Dies ist der Hashwert von m.";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) throws IllegalArgumentException {
		b = (Integer)arg1.get("Kompressionswert_b");
		stepLength = ((Integer)arg1.get("Schrittweite")) - 1;
        Nachricht_m = (int[])arg1.get("Nachricht_m");
        for(int i = 0; i < Nachricht_m.length; i++)
        	if(Nachricht_m[i]>1 || Nachricht_m[i]<0) {
        		JOptionPane.showMessageDialog(null, "Die Nachricht muss ein Binärstring sein." , "Fehlerhafte Eingabe",JOptionPane.WARNING_MESSAGE);
        		return false;
        	}
        if(b < 2) {
    		JOptionPane.showMessageDialog(null, "Die Funktion g muss eine Kompressionsfunktion sein. Die eingegebene Kompressionrate ist jedoch zu klein. Bitte wählen Sie b größer als 1" , "Fehlerhafte Eingabe",JOptionPane.WARNING_MESSAGE);
        	return false;
        }
        else if(stepLength < -1) {
    		JOptionPane.showMessageDialog(null, "Die Schrittlänge darf nicht negativ sein. Bitte eine positive Zahl eingeben." , "Fehlerhafte Eingabe",JOptionPane.WARNING_MESSAGE);
    		return false;
        }
        else 
        	return true;
	}
}
