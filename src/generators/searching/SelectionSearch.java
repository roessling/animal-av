package generators.searching;

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

import java.util.Hashtable;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalPolylineGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.counter.enumeration.ControllerEnum;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;

public class SelectionSearch implements ValidatingGenerator {
	
    private Language lang;
    
    private int intBiggestNumber_i;
    private int[] intSearchList;
    
    private String strHeadline;
    
	private ArrayProperties arrayProperties;
	private SourceCodeProperties sourceCodeProperties;
	private SourceCodeProperties textProperties;
	private RectProperties headerRectProperties;
	private TextProperties headerTextProperties;
	private ArrayMarkerProperties arrayMarker_n_Properties;
	private ArrayMarkerProperties arrayMarker_m_Properties;
	private ArrayMarkerProperties arrayMarker_q_Properties;
    
	private AnimalRectGenerator rectGenerator = null;
	private AnimalTextGenerator textGenerator = null;
	private PolylineGenerator lineGenerator = null;

	

    public void init(){
        
    	this.lang = new AnimalScript("Selection Search", "Erman Akca, Eugen Mesmer", 800, 800);
        
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
    	arrayMarker_n_Properties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarker_n_Properties");
    	arrayMarker_m_Properties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarker_m_Properties");
    	arrayMarker_q_Properties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarker_q_Properties");
    	strHeadline = (String)primitives.get("strHeadline");
    	intBiggestNumber_i = (Integer)primitives.get("intBiggestNumber_i");
    	intSearchList = (int[])primitives.get("intSearchList");
	
		this.sort(intSearchList, intBiggestNumber_i);
        
        return lang.toString();
    }

    public String getName() {
        return "Selection Search [DE]";
    }

    public String getAlgorithmName() {
        return "Selection Search";
    }

    public String getAnimationAuthor() {
        return "Erman Akca, Eugen Mesmer";
    }

    public String getDescription(){
        return "Selection Search gehoert zur Gruppe der Suchalgorithmen zur Suche des i. groessten/kleinsten Elementes."
        		+ "\n"
        		+ "<p>Der Name Selection Search kommt von Selection Sort, da die zu grundelegende Idee mit diesem Algorithmus identisch ist. Im ersten Durchlauf wird das groesste bzw. kleinste Element gesucht, im zweiten Durchgang dann das zweit groesste/kleinste, solange bis das i. groesste/kleinste Element gefunden wurde.</p>"
        		+ "\n"
        		+ "<p>Der Algorithmus erkennt auch, ob es effizienter ist nach dem i. groessten oder i. kleinsten Element zu suchen.</p>";
    }

    public String getCodeExample(){
        return    "public void selectionsearch(int[] list, int i){"
+ "\n"
        		+ "   searchForSmallest = false;"
+ "\n"
        		+ "   if(list.length-i &#60 i){"
+ "\n"
        		+ "      searchForSmallest = true;"
+ "\n"
        		+ "      i = list.length-i;"
+ "\n"
        		+ "   }"
+ "\n"
        		+ "   for(n = list.length-1; n >= list.length-i; n--){"
+ "\n"
        		+ "      q = 0;"
+ "\n"
        		+ "      for(m = 1; m &#60= n; m++){"
+ "\n"
        		+ "         if(searchForSmallest){"
+ "\n"
        		+ "            if(list[m] &#60 list[q])"
+ "\n"
        		+ "               q = m"
+ "\n"
        		+ "         }"
+ "\n"
        		+ "         else{"
+ "\n"
        		+ "            if(list[m] > list[q])"
+ "\n"
        		+ "               q = m"
+ "\n"
        		+ "         }"
+ "\n"
        		+ "      }"
+ "\n"
        		+ "      swap(list[q], list[n]);"
+ "\n"
        		+ "   }"
+ "\n"
        		+ "}"
        	;
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
	private void sort(int[] unsortedList, int i){
		
		ArrayProperties arrayProps = arrayProperties;
		SourceCodeProperties scProps = sourceCodeProperties;
		SourceCodeProperties txProps = textProperties;
		
		ArrayMarkerProperties arrayMarkerProps_n = arrayMarker_n_Properties;
		ArrayMarkerProperties arrayMarkerProps_m = arrayMarker_m_Properties;
		ArrayMarkerProperties arrayMarkerProps_q = arrayMarker_q_Properties;
		
		this.printHeader();
		
		SourceCode introText = this.lang.newSourceCode(new Coordinates(30, 50), "Introduction", null, txProps);
		introText.addCodeLine("Selection Search gehoert zur Gruppe der Suchalgorithmen zur Suche des i. groessten/kleinsten Elementes.", null, 0, null);
		introText.addCodeLine("", null, 0, null);
		introText.addCodeLine("Der Name Selection Search kommt von Selection Sort, da die zu Grunde liegende Idee mit diesem Algorithmus", null, 0, null);
		introText.addCodeLine("identisch ist.", null, 0, null);
		introText.addCodeLine("", null, 0, null);
		introText.addCodeLine("Im ersten Durchlauf wird das groesste bzw. kleinste Element gesucht, im zweiten Durchgang dann das", null, 0, null);
		introText.addCodeLine("zweit groesste/kleinste, solange bis das i. groesste/kleinste Element gefunden wurde.", null, 0, null);
		introText.addCodeLine("", null, 0, null);
		introText.addCodeLine("Der Algorithmus erkennt auch, ob es effizienter ist nach dem i. groessten oder i. kleinsten Element zu", null, 0, null);
		introText.addCodeLine("suchen.", null, 0, null);
		this.lang.nextStep();
		introText.hide();
		
		IntArray unsortedArray = this.lang.newIntArray(new Coordinates(70, 150), unsortedList, "listToBeSort", null, arrayProps);
	
		SourceCode sc  = this.lang.newSourceCode(new Coordinates(370, 60), "Source Code", null, scProps);
		sc.addCodeLine("public void selectionsearch(int[] list, int i){", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("searchForSmallest=false;", null, 0, null);
		sc.addCodeLine("if(list.length-i < i){", null, 0, null);
		sc.addCodeLine("searchForSmallest=true;", null, 1, null);
		sc.addCodeLine("i=list.length-i;", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("for(n=list.length-1;n>=list.length-i;n--){", null, 0, null);
		sc.addCodeLine("q=0;", null, 1, null);
		sc.addCodeLine("for(m=1;m<=n;m++){", null, 1, null);
		sc.addCodeLine("if(searchForSmallest){", null, 2, null);
		sc.addCodeLine("if(list[m] < list[q])", null, 3, null);
		sc.addCodeLine("q=m", null, 4, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("else{", null, 2, null);
		sc.addCodeLine("if(list[m] > list[q])", null, 3, null);
		sc.addCodeLine("q=m", null, 4, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("swap(list[q], list[n]);", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		
		Coordinates[] lineCoordinates = {new Coordinates(310, 60), new Coordinates(310, 520)};
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
		TwoValueView counterView = this.lang.newCounterView(counter, new Coordinates(30, 300), counterProps);
		counterView.showNumber();
		counterView.showText();
		counterView.hideBar();
		
		counterView.highlightAssignments();
		counterView.highlightAccess();
		
		this.lang.nextStep("Der Selection Search Suchalgorithmus startet...");
		//Beginn des Such Algorithmus
		boolean searchForSmallest = false;
		int q;
		
		sc.toggleHighlight(0, 2);
		counterView.highlightAccess();
		counterView.highlightAssignments();
		assignments++;
		counterView.update(ControllerEnum.assignments, assignments);

		SourceCode searchForSmallestDisplay = this.lang.newSourceCode(new Coordinates(30, 350), "Search for smallest Display", null, txProps);
		searchForSmallestDisplay.addCodeLine("SearchForSmallest: false", null, 0, null);
		searchForSmallestDisplay.highlight(0);
		
		this.lang.nextStep("Pruefe ob es effizienter ist, nach dem i. groessten oder i. kleinsten Element zu suchen.");
		sc.unhighlight(2);
		sc.toggleHighlight(0, 3);
		accesses++;
		counterView.update(ControllerEnum.access, accesses);
		SourceCode infoBox = this.lang.newSourceCode(new Coordinates(30, 400), "InfoBox", null, txProps);
		infoBox.addCodeLine("Pruefe ob es effizienter ist, nach dem", null, 0, null);
		infoBox.addCodeLine("i. groessten oder", null, 0, null);
		infoBox.addCodeLine("i. kleinsten Element zu suchen...", null, 0, null);
		
		if(unsortedArray.getLength()-i < i){
			
			this.lang.nextStep();
			assignments++;
			counterView.update(ControllerEnum.assignments, assignments);
			
			sc.toggleHighlight(3, 4);
			searchForSmallest = true;
			searchForSmallestDisplay.hide();
			searchForSmallestDisplay = this.lang.newSourceCode(new Coordinates(30, 350), "Search for smallest Display", null, txProps);
			searchForSmallestDisplay.addCodeLine("SearchForSmallest: true", null, 0, null);
			searchForSmallestDisplay.highlight(0);
			
			this.lang.nextStep();
			accesses=+1;
			counterView.update(ControllerEnum.access, accesses);
			assignments=+1;
			counterView.update(ControllerEnum.assignments, assignments);
			sc.toggleHighlight(4, 5);
			i = unsortedList.length - i;
		}
		
		if(searchForSmallest)
			this.lang.nextStep("Es wird nach dem "+ i + ". kleinsten Element gesucht. Die Suche startet...");
		else
			this.lang.nextStep("Es wird nach dem " + i + ". groessten Element gesucht. Die Suche startet...");
		
		sc.unhighlight(5);
		searchForSmallestDisplay.unhighlight(0);
		infoBox.hide();
		SourceCode searchInfo = this.lang.newSourceCode(new Coordinates(30, 250), "InfoBox", null, txProps);
		if(searchForSmallest)
			searchInfo.addCodeLine("gesuchtes Element: " + i + ". kleinstes", null, 0, null);
		else
			searchInfo.addCodeLine("gesuchtes Element: " + i + ". groesstes", null, 0, null);
		
		ArrayDisplayOptions displayOptions = new ArrayDisplayOptions(new MsTiming(3000), new MsTiming(2000), false);
		ArrayMarker nMarker = this.lang.newArrayMarker(unsortedArray, unsortedList.length-1, "n-Marker", displayOptions, arrayMarkerProps_n);
		
		ArrayMarker mMarker = this.lang.newArrayMarker(unsortedArray, 1, "m-Marker", displayOptions, arrayMarkerProps_m);
		mMarker.hide();
		
		ArrayMarker qMarker = this.lang.newArrayMarker(unsortedArray, 0, "q-Marker", displayOptions, arrayMarkerProps_q);
		qMarker.hide();
		
		int countHelper = 0;
		for(int n=unsortedArray.getLength()-1; n >= unsortedList.length-i;n--){
			
			countHelper++;
			sc.toggleHighlight(3, 8);

			assignments=+1;
			counterView.update(ControllerEnum.assignments, assignments);
			accesses=+1;
			counterView.update(ControllerEnum.access, accesses);
			
			nMarker.move(n, new MsTiming(700), new MsTiming(200));
			
			if(searchForSmallest)
				this.lang.nextStep("Suche nach dem " + countHelper + ". kleinstem Element, in dem die Liste bis zum Ende durch iteriert und verglichen wird.");
			else
				this.lang.nextStep("Suche nach dem " + countHelper + ". groesstem Element, in dem die Liste bis zum Ende durch iteriert und verglichen wird.");
			
			//Nur im ersten Durchgang show() ausfuehren, sonst kann der Marker nicht mehr ausgeblendet werden
			if(n==unsortedArray.getLength()-1)
				qMarker.show();
			
			qMarker.move(0, new MsTiming(700), new MsTiming(200));	
			counterView.unhighlightAccess();
			q = 0;
			assignments=+1;
			counterView.update(ControllerEnum.assignments, assignments);
			sc.toggleHighlight(8, 9);

			
			this.lang.nextStep();


			for(int m=1; m<=n ;m++){
				
				//Nur im ersten Durchgang show() ausfuehren, sonst kann der Marker nicht mehr ausgeblendet werden
				if(n==unsortedArray.getLength()-1 && m==1)
					mMarker.show();
				
				counterView.highlightAccess();
				accesses=+1;
				counterView.update(ControllerEnum.access, accesses);
				
				assignments=+1;
				counterView.update(ControllerEnum.assignments, assignments);
				
				sc.toggleHighlight(9, 10);
				mMarker.move(m, new MsTiming(700), new MsTiming(200));	

				this.lang.nextStep();
				
				counterView.unhighlightAssignments();
				sc.toggleHighlight(10, 11);
				accesses=+1;
				counterView.update(ControllerEnum.access, accesses);
				
				if(searchForSmallest){
					
					this.lang.nextStep();
					unsortedArray.highlightElem(m, new MsTiming(300), new MsTiming(200));
					unsortedArray.highlightElem(q, new MsTiming(300), new MsTiming(200));
					sc.toggleHighlight(11, 12);
					//Accesses + 2
					accesses=+2;
					counterView.update(ControllerEnum.access, accesses);
					
					if(unsortedArray.getData(m) < unsortedArray.getData(q)){						
						
						this.lang.nextStep();
						unsortedArray.unhighlightElem(m, new MsTiming(500), new MsTiming(800));
						unsortedArray.unhighlightElem(q, new MsTiming(500), new MsTiming(800));
						sc.toggleHighlight(12, 13);
						accesses=+1;
						counterView.update(ControllerEnum.access, accesses);
						counterView.highlightAssignments();
						assignments=+1;
						counterView.update(ControllerEnum.assignments, assignments);
						
						q = m;
						qMarker.move(q, new MsTiming(700), new MsTiming(200));	
						
						this.lang.nextStep();
						sc.unhighlight(13);
					}
					else{
						
						this.lang.nextStep();
						unsortedArray.unhighlightElem(m, new MsTiming(500), new MsTiming(800));
						unsortedArray.unhighlightElem(q, new MsTiming(500), new MsTiming(800));
						sc.unhighlight(12);
					}
					
				}
				else{
					
					this.lang.nextStep();
					unsortedArray.highlightElem(m, new MsTiming(300), new MsTiming(200));
					unsortedArray.highlightElem(q, new MsTiming(300), new MsTiming(200));
					sc.toggleHighlight(11, 16);
					//Accesses + 2
					accesses=+2;
					counterView.update(ControllerEnum.access, accesses);
					
					if(unsortedArray.getData(m) > unsortedArray.getData(q)){
						
						this.lang.nextStep();
						unsortedArray.unhighlightElem(m, new MsTiming(500), new MsTiming(800));
						unsortedArray.unhighlightElem(q, new MsTiming(500), new MsTiming(800));
						sc.toggleHighlight(16, 17);
						accesses=+1;
						counterView.update(ControllerEnum.access, accesses);
						counterView.highlightAssignments();
						assignments=+1;
						counterView.update(ControllerEnum.assignments, assignments);
						
						q = m;
						qMarker.move(q, new MsTiming(700), new MsTiming(200));
						
						this.lang.nextStep();
						sc.unhighlight(17);
					}
					else{
						
						this.lang.nextStep();
						unsortedArray.unhighlightElem(m, new MsTiming(500), new MsTiming(800));
						unsortedArray.unhighlightElem(q, new MsTiming(500), new MsTiming(800));
						sc.unhighlight(16);
					}
					
				}

			}
			
			sc.toggleHighlight(0, 20);
			unsortedArray.swap(q, n, new MsTiming(700), new MsTiming(300));

			//Access + 3
			accesses=+3;
			counterView.update(ControllerEnum.access, accesses);
			
			//Assignments + 3
			counterView.highlightAssignments();
			assignments=+3;
			counterView.update(ControllerEnum.assignments, assignments);
			
			unsortedArray.highlightCell(n, new MsTiming(800), new MsTiming(400));
			if(searchForSmallest)
				this.lang.nextStep("Das " + countHelper + ". kleinste Element wurde gefunden und an entsprechender Stelle in der Liste einsortiert.");
			else
				this.lang.nextStep("Das " + countHelper + ". groesste Element wurde gefunden und an entsprechender Stelle in der Liste einsortiert.");
			
			sc.unhighlight(20);
			
		}
		
		qMarker.hide();
		nMarker.hide();
		mMarker.hide();

		if(searchForSmallest)
			arrayMarkerProps_n.set(AnimationPropertiesKeys.LABEL_PROPERTY, i+". kleinstes Element");
		else
			arrayMarkerProps_n.set(AnimationPropertiesKeys.LABEL_PROPERTY, i+". groesstes Element");
		
		ArrayMarker endMarker = this.lang.newArrayMarker(unsortedArray, qMarker.getPosition(), "end-Marker", displayOptions, arrayMarkerProps_n);
		
		this.lang.nextStep("Das gesuchte Element wurde hiermit gefunden: " + unsortedArray.getData(unsortedArray.getLength()-i));
		sc.hide();
		unsortedArray.hide();
		counterView.hide();
		line.hide();
		endMarker.hide();
		searchForSmallestDisplay.hide();
		searchInfo.hide();
		
		SourceCode outroText = this.lang.newSourceCode(new Coordinates(30, 100), "Outroduction", null, txProps);
		outroText.addCodeLine("Das gesuchte Element ist hiermit gefunden!", null, 0, null);
		if(searchForSmallest)
			outroText.addCodeLine(i+". kleinstes Element: " + unsortedArray.getData(unsortedArray.getLength()-i), null, 0, null);
		else
			outroText.addCodeLine(i+". groesstes Element: " + unsortedArray.getData(unsortedArray.getLength()-i), null, 0, null);
		outroText.addCodeLine("", null, 0, null);
		outroText.addCodeLine("Der Algorithmus hat eine Laufzeit von O(i*n) und braucht i*(n-i/2) Vergleiche", null, 0, null);
		outroText.addCodeLine("bis das gesuchte Element gefunden wurde.", null, 0, null);
	}
	
	private void printHeader(){
		
		RectProperties rectProps = headerRectProperties; 
		TextProperties textProps = headerTextProperties; 
		
		new Rect(this.rectGenerator, new Coordinates(10, 10), new Coordinates(720, 45), "rect", null, rectProps);
		
		new Text(textGenerator, new Coordinates(15, 20), strHeadline, "title", null, textProps);
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		
		if(arg1.get("intBiggestNumber_i") == null || arg1.get("intSearchList") == null){
			JOptionPane.showMessageDialog(null, "Die Liste und das i.-groesste Element darf nicht leer sein.", "Invalide Eingabe!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		int intBiggestNumber_i = (Integer) arg1.get("intBiggestNumber_i");
		int[] intSearchList = (int[]) arg1.get("intSearchList");
		
		if(intBiggestNumber_i < 1 && intBiggestNumber_i > intSearchList.length){
			JOptionPane.showMessageDialog(null, "Das i.-groesste Element wonach gesucht wird, muss grosser als 0 sein und kleiner wie die entsprechende Listenlaenge.", "Invalide Eingabe!", JOptionPane.WARNING_MESSAGE);
			return false;
		}
			
		return true;
	}

}