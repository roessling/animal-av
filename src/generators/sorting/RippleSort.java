package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.PolylineGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalPolylineGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.counter.enumeration.ControllerEnum;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;

public class RippleSort implements ValidatingGenerator {
    
	private Language lang = null;
	
	private ArrayProperties arrayProperties;
	private SourceCodeProperties sourceCodeProperties;
	private SourceCodeProperties textProperties;
	private RectProperties headerRectProperties;
	private TextProperties headerTextProperties;
	private ArrayMarkerProperties arrayMarker_i_Properties;
	private ArrayMarkerProperties arrayMarker_j_Properties;
	
	private String strHeadline;
	
    private int[] intUnsortedList;
    
	private AnimalRectGenerator rectGenerator = null;
	private AnimalTextGenerator textGenerator = null;
	private PolylineGenerator lineGenerator = null;

	

    public void init(){
    	
		this.lang = new AnimalScript("Ripplesort", "Erman Akca, Eugen Mesmer", 800, 800);
    	
    	this.lang.setStepMode(true);
		
		this.rectGenerator = new AnimalRectGenerator(this.lang);
		this.textGenerator = new AnimalTextGenerator(this.lang);
		this.lineGenerator = new AnimalPolylineGenerator(this.lang);
    	
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        
    	arrayProperties = (ArrayProperties)props.getPropertiesByName("arrayProperties");
    	sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
    	textProperties = (SourceCodeProperties)props.getPropertiesByName("textProperties");
    	headerRectProperties = (RectProperties)props.getPropertiesByName("headerRectProperties");
    	headerTextProperties = (TextProperties)props.getPropertiesByName("headerTextProperties");
    	arrayMarker_i_Properties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarker_i_Properties");
    	arrayMarker_j_Properties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarker_j_Properties");
    	strHeadline = (String)primitives.get("strHeadline");
    	intUnsortedList = (int[])primitives.get("intUnsortedList");
 		
		this.sort(intUnsortedList);
        
        return lang.toString();
    }

    public String getName() {
        return "Ripplesort (Anlehnung Bubblesort - Tauschdiagramm) [DE]";
    }

    public String getAlgorithmName() {
        return "Ripplesort";
    }

    public String getAnimationAuthor() {
        return "Erman Akca, Eugen Mesmer";
    }

    public String getDescription(){
        return "Ripplesort gehoert zur Gruppe der elementaren Sortieralgorithmen und ist eine spezielle Form von Bubblesort."
        		+ "\n"
        		+ "<p>Beim Ripplsesort-Algorithmus wird zunaechst die 1. Zahl nacheinander mit allen anderen verglichen. Wenn eine der anderen Zahlen kleiner als die erste Zahl ist, wird 1. Zahl mit dieser Zahl getauscht. Nach einem solchen Durchlauf ist die erste Zahl die kleinste. Nun scheidet die 1. Zahl aus dem Sortierverfahren aus. Es bleiben n-1 Zahlen zum Sortieren uebrig von den urspruenglich n zu sortierenden Zahlen.</p>"
        		+ "\n"
        		+ "<p>Bei jeden Durchlauf reduziert sich so die Anzahl der zu sortierenden Zahlen um 1. Am Ende sind nur noch 2 Zahlen uebrig, die dann gegebenfalls zu vertauschen sind. Damit ist der Sortiervorgang abgeschlossen.</p>";
    }

    public String getCodeExample(){
        return "public void rippleSort(int[] list){"
        		+"\n"
        		+"   for (i=0; i &#60 list.length-1; i++)"
        		+"\n"
        		+"      for (j=i+1; j &#60 list.length; j++)"
        		+"\n"
        		+"         if(list[i] > list[j])"
        		+"\n"
        		+"            swap(list[i], list[j]);"
        		+"\n"
        		+"}"
        		;
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
	private void printHeader(){
		
		RectProperties rectProps = headerRectProperties;
		TextProperties textProps = headerTextProperties;
		
		new Rect(this.rectGenerator, new Coordinates(10, 10), new Coordinates(620, 45), "rect", null, rectProps);
		
		new Text(this.textGenerator, new Coordinates(15, 20), strHeadline, "title", null, textProps);
	}

	private void sort(int[] unsortedList){
		
		ArrayProperties arrayProps = arrayProperties;
		SourceCodeProperties scProps = sourceCodeProperties;
		SourceCodeProperties txProps = textProperties;
		
		ArrayMarkerProperties arrayMarkerProps_i = arrayMarker_i_Properties;
		ArrayMarkerProperties arrayMarkerProps_j = arrayMarker_j_Properties;
		
		//Fuer Tauschdiagramm
		List<IntArray> tauschdiagrammListen = new ArrayList<IntArray>();
		List<Polyline> lineListe = new ArrayList<Polyline>();
		int last_i = -1;
		int last_j = -1;
		boolean swapped = false;
		
		this.printHeader();
		
		SourceCode introText = this.lang.newSourceCode(new Coordinates(30, 50), "Introduction", null, txProps);
		introText.addCodeLine("Ripplesort gehoert zur Gruppe der elementaren Sortieralgorithmen und ist eine spezielle Form", null, 0, null);
		introText.addCodeLine("von Bubblesort.", null, 0, null);
		introText.addCodeLine("", null, 0, null);
		introText.addCodeLine("Beim Ripplesort-Algorithmus wird zunaechst die 1. Zahl nacheinander mit allen anderen", null, 0, null);
		introText.addCodeLine("verglichen. Wenn eine der anderen Zahlen kleiner als die erste Zahl ist, wird 1. Zahl", null, 0, null);
		introText.addCodeLine("mit dieser Zahl getauscht. Nach einem solchen Durchlauf ist die erste Zahl die kleinste.", null, 0, null);
		introText.addCodeLine("Nun scheidet die 1. Zahl aus dem Sortierverfahren aus. Es bleiben n-1 Zahlen zum", null, 0, null);
		introText.addCodeLine("Sortieren uebrig von den urspruenglich n zu sortierenden Zahlen.", null, 0, null);
		introText.addCodeLine("", null, 0, null);
		introText.addCodeLine("Bei jeden Durchlauf reduziert sich so die Anzahl der zu sortierenden Zahlen um 1.", null, 0, null);
		introText.addCodeLine("Am Ende sind nur noch 2 Zahlen uebrig, die dann gegebenfalls zu vertauschen sind.", null, 0, null);
		introText.addCodeLine("Damit ist der Sortiervorgang abgeschlossen.", null, 0, null);
		
		this.lang.nextStep();
		introText.hide();
		
		IntArray unsortedArray = this.lang.newIntArray(new Coordinates(70, 150), unsortedList, "listToBeSort", null, arrayProps);
	
		SourceCode sc  = this.lang.newSourceCode(new Coordinates(30, 300), "Source Code", null, scProps);
		sc.addCodeLine("public void rippleSort(int[] list){", null, 0, null);
		sc.addCodeLine("for (i=0;i<list.length-1;i++)", null, 1, null);
		sc.addCodeLine("for (j=i+1;j<list.length;j++)", null, 2, null);
		sc.addCodeLine("if(list[i] > list[j])", null, 3, null);
		sc.addCodeLine("swap(list[i], list[j]);", null, 4, null);
		sc.addCodeLine("}", null, 0, null);
		
		Coordinates[] lineCoordinates = {new Coordinates(310, 60), new Coordinates(310, 400)};
		Polyline line = new Polyline(this.lineGenerator, lineCoordinates, "Line", null, new PolylineProperties());
		this.lang.nextStep();
		
		sc.highlight(0, 0, false);
		unsortedArray.highlightElem(0, unsortedArray.getLength() - 1, null, null);
		unsortedArray.unhighlightElem(0, unsortedArray.getLength() - 1, null, new MsTiming(1000));
		
		//Assignment & Access Counter
		int assignments = 0;
		int accesses = 0;
		TwoValueCounter counter = new TwoValueCounter();
		counter.activateCounting();
		CounterProperties counterProps = new CounterProperties();
		counterProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		counterProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray);
		counterProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.black);
		counterProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		counterProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.gray);
		TwoValueView counterView = this.lang.newCounterView(counter, new Coordinates(30, 250), counterProps);
		counterView.showNumber();
		counterView.showText();
		counterView.hideBar();
		
		this.lang.nextStep("Der Sortielgorithmus beginnt...");
		
		ArrayDisplayOptions displayOptions = new ArrayDisplayOptions(new MsTiming(3000), new MsTiming(2000), false);
		ArrayMarker iMarker = this.lang.newArrayMarker(unsortedArray, 0, "i-Marker", displayOptions, arrayMarkerProps_i);
		
		ArrayMarker jMarker = this.lang.newArrayMarker(unsortedArray, 1, "j-Marker", displayOptions, arrayMarkerProps_j);
		jMarker.hide();
		
		counterView.highlightAssignments();
		counterView.highlightAccess();
		
		
		for(int i=0; i<unsortedArray.getLength()-1;i++){
			
			
			sc.toggleHighlight(0, 1);
			
			assignments++;
			counterView.update(ControllerEnum.assignments, assignments);
			
			accesses++;
			counterView.update(ControllerEnum.access, accesses);
			
			iMarker.move(i, new MsTiming(700), new MsTiming(200));
			
			this.lang.nextStep("Das " + (i+1) + ". kleinste Element wird gesucht und an entsprechender Stelle einsortiert.");

			sc.unhighlight(1);
			
			for(int j=i+1; j<unsortedArray.getLength();j++){
				
				//Nur im ersten Durchgang show() ausfuehren, sonst kann der Marker nicht mehr ausgeblendet werden
				if(i==0 && j==1)
					jMarker.show();
				
				accesses=+1;
				counterView.update(ControllerEnum.access, accesses);
				
				assignments=+1;
				counterView.update(ControllerEnum.assignments, assignments);
				
				sc.toggleHighlight(0, 2);
				jMarker.move(j, new MsTiming(700), new MsTiming(200));	

				this.lang.nextStep();
				
				counterView.unhighlightAssignments();
				sc.unhighlight(2);

				sc.toggleHighlight(0, 3);
				unsortedArray.highlightElem(i, new MsTiming(300), new MsTiming(200));
				unsortedArray.highlightElem(j, new MsTiming(300), new MsTiming(200));
				
				//Accesses + 2
				accesses=+2;
				counterView.update(ControllerEnum.access, accesses);
				
				//Tauschdiagramm zeichen
				
				for(IntArray temp: tauschdiagrammListen)	
					temp.moveBy(null, 0, 40, null, null);
				
				for(Polyline temp : lineListe)
					temp.moveBy(null, 0, 40, null, null);
				
				//nur die letzten 5 schritte anzeigen
				if(tauschdiagrammListen.size() > 4){
					for(int n=0; n<tauschdiagrammListen.size()-4; n++)
						tauschdiagrammListen.get(n).hide();
				
					for(int m=0; m<lineListe.size()-6; m++)
						lineListe.get(m).hide();
				}
					
				IntArray tempList = this.lang.newIntArray(new Coordinates(375, 150), this.getListFromIntArray(unsortedArray), "tauschdiagrammliste", null, arrayProps);
				tempList.highlightElem(i, null, null);
				tempList.highlightElem(j, null, null);
				tauschdiagrammListen.add(tempList);
				
				//Linien im Tauschdiagramm zeichnen
				if(last_i != -1){
					
					if(swapped){
						
						Coordinates[] tempCoordinates = {new Coordinates(375+this.calcCellWidth(tempList, last_i-1), 171), new Coordinates(375+this.calcCellWidth(tauschdiagrammListen.get(tauschdiagrammListen.size()-2), last_j-1), 189)};
						Coordinates[] tempCoordinates2 = {new Coordinates(375+this.calcCellWidth(tempList, last_j-1), 171), new Coordinates(375+this.calcCellWidth(tauschdiagrammListen.get(tauschdiagrammListen.size()-2), last_i-1), 189)};
						
						Polyline tempLine = new Polyline(this.lineGenerator, tempCoordinates, "Line"+i+j, null, new PolylineProperties());
						Polyline tempLine2 = new Polyline(this.lineGenerator, tempCoordinates2, "Line"+i+j, null, new PolylineProperties());
						lineListe.add(tempLine);
						lineListe.add(tempLine2);
					
					}
					
					else{
						
						Coordinates[] tempCoordinates = {new Coordinates(375+this.calcCellWidth(tempList, last_i-1), 171), new Coordinates(375+this.calcCellWidth(tauschdiagrammListen.get(tauschdiagrammListen.size()-2), last_i-1), 189)};
						Coordinates[] tempCoordinates2 = {new Coordinates(375+this.calcCellWidth(tempList, last_j-1), 171), new Coordinates(375+this.calcCellWidth(tauschdiagrammListen.get(tauschdiagrammListen.size()-2), last_j-1), 189)};
						Polyline tempLine = new Polyline(this.lineGenerator, tempCoordinates, "Line"+i+j, null, new PolylineProperties());
						Polyline tempLine2 = new Polyline(this.lineGenerator, tempCoordinates2, "Line"+i+j, null, new PolylineProperties());
						lineListe.add(tempLine);
						lineListe.add(tempLine2);
					}
					
					
				}
				
				if(unsortedArray.getData(i) > unsortedArray.getData(j)){
					
					swapped = true;
					this.lang.nextStep("Elemente der Liste werden getauscht. (" + unsortedArray.getData(i) + ">" + unsortedArray.getData(j) + ")");
					
					sc.toggleHighlight(3, 4);
					unsortedArray.swap(i, j, new MsTiming(700), new MsTiming(300));
					unsortedArray.unhighlightElem(i, new MsTiming(500), new MsTiming(800));
					unsortedArray.unhighlightElem(j, new MsTiming(500), new MsTiming(800));
					//Access + 3
					accesses=+3;
					counterView.update(ControllerEnum.access, accesses);
					
					//Assignments + 3
					counterView.highlightAssignments();
					assignments=+3;
					counterView.update(ControllerEnum.assignments, assignments);
					
					this.lang.nextStep();
				
					sc.unhighlight(4);
				}
				else{
					
					swapped = false;
					this.lang.nextStep("Elemente werden nicht getauscht (NOT " + unsortedArray.getData(i) + ">" + unsortedArray.getData(j) + ")");
					
					unsortedArray.unhighlightElem(i, new MsTiming(500), new MsTiming(800));
					unsortedArray.unhighlightElem(j, new MsTiming(500), new MsTiming(800));
					sc.unhighlight(3);
				}
				
				last_i=i+1;
				last_j=j+1;
			}
			
			unsortedArray.highlightCell(i, new MsTiming(500), new MsTiming(200));
		}
		
		
		counterView.highlight();

		jMarker.hide(new MsTiming(100));
		iMarker.hide(new MsTiming(100));
		unsortedArray.highlightCell(0, unsortedArray.getLength()-1, new MsTiming(500), new MsTiming(200));
		
		StringBuffer sb = new StringBuffer("[");
		for(int z=0; z<unsortedArray.getLength(); z++){
			sb.append(unsortedArray.getData(z));
			
			if(z<unsortedArray.getLength()-1)
				sb.append(" ,");
		}
		sb.append("]");
		this.lang.nextStep("Die Liste ist nun sortiert! " + sb.toString());
		
		sc.hide();
		unsortedArray.hide();
		counterView.hide();
		line.hide();
		
		//Am Ende ganzes Tauschdiagramm anzeigen
		for(IntArray liste : tauschdiagrammListen){
			
			liste.moveBy(null, 0, 100, null, null);
			liste.show();
		}
		
		for(Polyline polyLine : lineListe){
			polyLine.moveBy(null, 0, 100, null, null);
			polyLine.show();
		}
		
		SourceCode outroText = this.lang.newSourceCode(new Coordinates(30, 50), "Outroduction", null, txProps);
		outroText.addCodeLine("Die Liste ist hiermit sortiert!", null, 0, null);
		outroText.addCodeLine("", null, 0, null);
		outroText.addCodeLine("Ripplesort besitzt zwar eine etwas schlechtere Laufzeit als Bubblesort, kann aber bei", null, 0, null);
		outroText.addCodeLine("Dauersortierungen (Daten aendern sich staendig geringfuegig, Sortierung muss staendig", null, 0, null);
		outroText.addCodeLine("korregiert werden) optimaler eingesetzt werden.", null, 0, null);
		outroText.addCodeLine("", null, 0, null);
		outroText.addCodeLine("Die Anzahl an Zuweisungen und Zugriffe laesst die Aussage treffen, dass eine Sortierung", null, 0, null);
		outroText.addCodeLine("von stark unsortierten Listen sehr aufwaendig ist und lieber mit effizienteren, bekannten", null, 0, null);
		outroText.addCodeLine("Algorithmen wie Quicksort durchgefuehrt werden sollen.", null, 0, null);
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		
		if(arg1.get("intUnsortedList") == null){
			JOptionPane.showMessageDialog(null, "Die Liste darf nicht leer sein.", "Invalide Eingabe!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	private int[] getListFromIntArray(IntArray intArray){
		
		int[] list = new int[intArray.getLength()];
		
		for(int i=0 ; i<intArray.getLength() ; i++)	
			list[i] = intArray.getData(i);
		
		return list;
		
	}
	
	private int calcCellWidth(IntArray intArray, int n){
		
		double width = 6 + 3.5*(String.valueOf(intArray.getData(0)).length()-1);
		String tempN = null;
		String tempN1 = null;
		
		for(int i = 1; i<=n ; i++){
			
			tempN = String.valueOf(intArray.getData(i));
			width = width + 12 + 3.5*(tempN.length()-1);
			
			if(i>0){
				tempN1 = String.valueOf(intArray.getData(i-1));
				if(tempN1.length() > tempN.length())
					width = width + (tempN1.length()-tempN.length())*3.5;
				
				if(tempN1.length() > 1 && tempN.length() > 1){
					
					if(tempN1.length() == tempN.length())
						width = width + (tempN.length()-1)*3.5;
				}
			}
			
		}
		return (int) Math.round(width*100)/100;
		
	}
	
}