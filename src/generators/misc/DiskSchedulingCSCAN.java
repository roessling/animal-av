package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.stream.Collectors;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
//import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

/**
 * @author Johanna Heinz <johanna.heinz@stud.tu-darmstadt.de>
 * @version 1.1 20180508
 */
public class DiskSchedulingCSCAN implements ValidatingGenerator {
	
    private Language lang;
    private Translator translator;
    private Locale l;
    private int anzahlZylinder;
    private int letzteAnfrage;
    private int[] Queue;
    private int aktuellePositionDesKopfes;
    private SourceCodeProperties HighlightfarbeDesPseudoCode;
	private CircleProperties FarbeDerKreise;
	private PolylineProperties FarbeDerPfeile;

    public void init(){
    	lang = new AnimalScript("Disk Scheduling: (C-)SCAN", "Johanna Heinz", 800, 600);
    	lang.setStepMode(true);
    }
    
    // Initialize via new DiskSchedulingCSCAN("resources/DiskSchedulingCSCAN", Locale.GERMANY)
    // or via new DiskSchedulingCSCAN("resources/DiskSchedulingCSCAN", Locale.US) for english text
    public DiskSchedulingCSCAN(String path, Locale locale) {
    	this.l = locale;
    	this.translator = new Translator(path, l);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        
    	anzahlZylinder = (Integer)primitives.get("Anzahl Zylinder");
        letzteAnfrage = (Integer)primitives.get("Letzte Anfrage");
        Queue = (int[])primitives.get("Queue");
        aktuellePositionDesKopfes = (Integer)primitives.get("Aktuelle Position des Kopfes");
        
        HighlightfarbeDesPseudoCode = (SourceCodeProperties)props.getPropertiesByName("Highlightfarbe des Pseudo Code");
	    FarbeDerKreise = (CircleProperties)props.getPropertiesByName("Farbe der Kreise");
	    FarbeDerPfeile = (PolylineProperties)props.getPropertiesByName("Farbe der Pfeile");
	    
	    Color hColor = (Color) HighlightfarbeDesPseudoCode.get("highlightColor");
	    Color cColor = (Color) FarbeDerKreise.get("fillColor");
	    Color aColor = (Color) FarbeDerPfeile.get("color");
	    
	    if(validateInput(props, primitives)) {
	    	schedule(Queue, anzahlZylinder, aktuellePositionDesKopfes, letzteAnfrage, hColor, cColor, aColor);
	    }
        return lang.toString();
    }
    
    @Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
    	
    	anzahlZylinder = (Integer)primitives.get("Anzahl Zylinder");
        letzteAnfrage = (Integer)primitives.get("Letzte Anfrage");
        Queue = (int[])primitives.get("Queue");
        aktuellePositionDesKopfes = (Integer)primitives.get("Aktuelle Position des Kopfes");
        
        String exception = translator.translateMessage("exception");
        
        for(int i = 0; i < Queue.length; i++) {
        	
        	if(Queue[i] >= anzahlZylinder) {
        		throw new IllegalArgumentException(exception  + Integer.toString(anzahlZylinder - 1));
        	}
        }
        
        if(letzteAnfrage >= anzahlZylinder || aktuellePositionDesKopfes >= anzahlZylinder) {
        	throw new IllegalArgumentException(exception + Integer.toString(anzahlZylinder - 1));
        }
    	
        else {
        	return true;
        }
    	
	}

    public String getName() {
        return translator.translateMessage("name");
    }

    public String getAlgorithmName() {
        return "Disk Schedling: (C-)SCAN";
    }

    public String getAnimationAuthor() {
        return "Johanna Heinz";
    }

    public String getDescription(){
    	
    	String tlDescr1 = translator.translateMessage("intro1");
		String tlDescr2 = translator.translateMessage("intro2");
		String tlDescr3 = translator.translateMessage("descr1");
		String tlDescr4 = translator.translateMessage("descr2");
		
        return tlDescr1
        		+"\n"
        		+tlDescr2
        		+"\n"
        		+"	"
        		+"\n"
        		+ tlDescr3
        		+"\n"
        		+ tlDescr4 + ".";
    }

    public String getCodeExample(){
    	
    	String tlCodeEg1 = translator.translateMessage("codeEg1");
		String tlCodeEg2 = translator.translateMessage("intro3");
		String tlCodeEg3 = translator.translateMessage("intro4");
		String tlCodeEg4 = translator.translateMessage("intro5");
		String tlCodeEg5 = translator.translateMessage("intro6");
		String tlCodeEg6 = translator.translateMessage("intro7");
		String tlCodeEg7 = translator.translateMessage("intro8");
		String tlCodeEg8 = translator.translateMessage("intro9");
		String tlCodeEg9 = translator.translateMessage("codeEg2");
		String tlCodeEg10 = translator.translateMessage("intermediate1");
		String tlCodeEg11 = translator.translateMessage("intermediate2");
		String tlCodeEg12 = translator.translateMessage("intermediate3");
		String tlCodeEg13 = translator.translateMessage("intermediate4");
        
		return tlCodeEg1 + ":"
        		+"\n"
        		+ tlCodeEg2
        		+"\n"
        		+ tlCodeEg3
        		+"\n"
        		+ tlCodeEg4
        		+"\n"
        		+ tlCodeEg5
        		+"\n"
        		+ tlCodeEg6
        		+"\n"
        		+ tlCodeEg7
        		+"\n"
        		+ tlCodeEg8
        		+"\n"
        		+"\n"
        		+"\n"
        		+ tlCodeEg9 + ":"
        		+"\n"
        		+ tlCodeEg10
        		+"\n"
        		+ tlCodeEg11
        		+"\n"
        		+ tlCodeEg12
        		+"\n"
        		+ tlCodeEg13;
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return l;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	  
	  /**
	   * Schedules the disk requests of the input array according to the (C-)SCAN algorithm 
	   * 
	   * @param array queue the disk requests
	   * @param int cylinder the number of cylinders
	   * @param int currentHeadPosition the current position of the disk head  
	   * @param int lastRequest the last disk request
	   * @param Color highlight the highlight color for the pseudo code
	   * @param Color color the color for the circles
	   * @param Color colorA the color for the arrows
	   */
	  public void schedule(int[] queue, int cylinder, int currentHeadPosition, int lastRequest, Color highlight, Color color, Color colorA) {
		  
		  // Transform array to list
		  ArrayList<Integer> q = new ArrayList<Integer>();
		  ArrayList<Integer> cQ = new ArrayList<Integer>();
		  
		  for(int i = 0; i < queue.length; i++) {
			  q.add(queue[i]);
		  }
		  
		  for(int i = 0; i < queue.length; i++) {
			  cQ.add(queue[i]);
		  }
		  // Save headline and rectangle around it to exclude from hiding later on
		  ArrayList<Primitive> hl = new ArrayList<Primitive>();
		  
		  // Headline
		  TextProperties hlProps = new TextProperties();
		  hlProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		  
		  Text headline = lang.newText(new Coordinates(40, 40), "Disk Scheduling: (C)SCAN", "headline", null, hlProps);
		  
		  hl.add(headline);
		  
		  // Rectangle around headline
		  RectProperties rProps= new RectProperties();
		  rProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		  rProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		  rProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		  
		  Rect hRect = lang.newRect(new Offset(-5, -5, headline, AnimalScript.DIRECTION_NW), new Offset(5, 5, headline, AnimalScript.DIRECTION_SE), "hRect", null, rProps);
		  
		  hl.add(hRect);
		  
		  
		  // Intro text header
		  TextProperties headerProps = new TextProperties();
		  headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		  
		  String tlIntroHeader = translator.translateMessage("introHeader");
		  Text introHeader = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlIntroHeader, "introduction", null, headerProps);
		  
		  // Intro text
		  SourceCodeProperties textProps = new SourceCodeProperties();
		  textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
		  
		  String tlIntro1 = translator.translateMessage("intro1");
		  String tlIntro2 = translator.translateMessage("intro2");
		  String tlIntro3 = translator.translateMessage("intro3");
		  String tlIntro4 = translator.translateMessage("intro4");
		  String tlIntro5 = translator.translateMessage("intro5");
		  String tlIntro6 = translator.translateMessage("intro6");
		  String tlIntro7 = translator.translateMessage("intro7");
		  String tlIntro8 = translator.translateMessage("intro8");
		  String tlIntro9 = translator.translateMessage("intro9");
		  
		  SourceCode intro = lang.newSourceCode(new Offset(0, 130, headline, AnimalScript.DIRECTION_NW), "intro", null, textProps);
		  intro.addCodeLine(tlIntro1, null, 0, null);
		  intro.addCodeLine(tlIntro2, null, 0, null);
		  intro.addCodeLine(" ", null, 0, null);
		  intro.addCodeLine(tlIntro3, null, 0, null);
		  intro.addCodeLine(tlIntro4, null, 0, null);
		  intro.addCodeLine(tlIntro5, null, 0, null);
		  intro.addCodeLine(tlIntro6, null, 0, null);
		  intro.addCodeLine(tlIntro7, null, 0, null);
		  intro.addCodeLine(tlIntro8, null, 0, null);
		  intro.addCodeLine(tlIntro9, null, 0, null);
		  
		  lang.nextStep(tlIntroHeader);
		  
		  // Hide introduction
		  introHeader.hide();
		  intro.hide();
		  
		  String tlAlgoHeader = translator.translateMessage("algoHeader");
		  
		  Text algoHeader = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlAlgoHeader + "SCAN", "algo", null, headerProps);
		  
		  String tlAlgo1 = translator.translateMessage("algo1");
		  String tlAlgo2 = translator.translateMessage("algo2");
		  String tlAlgo3 = translator.translateMessage("algo3");
		  String tlAlgo4 = translator.translateMessage("algo4");
		  String tlAlgo5 = translator.translateMessage("algo5");
		  String tlAlgo6 = translator.translateMessage("algo6");
		  String tlAlgo7 = translator.translateMessage("algo7");
		  String tlAlgo8 = translator.translateMessage("algo8");
		  String tlAlgo9 = translator.translateMessage("algo9");
		  String tlAlgo10 = translator.translateMessage("algo10");
		  String tlAlgo11 = translator.translateMessage("algo11");
		  String tlAlgo12 = translator.translateMessage("algo12");

		  int maxCylinder = cylinder - 1;
		  String maxC = Integer.toString(maxCylinder);
		  
		  SourceCode algo = lang.newSourceCode(new Offset(0, 130, headline, AnimalScript.DIRECTION_NW), "algo1", null, textProps);
		  algo.addCodeLine(tlAlgo1, null, 0, null);
		  algo.addCodeLine(tlAlgo2, null, 0, null);
		  algo.addCodeLine(tlAlgo3, null, 0, null);
		  algo.addCodeLine(tlAlgo4, null, 0, null);
		  algo.addCodeLine(tlAlgo5, null, 0, null);
		  algo.addCodeLine(tlAlgo6, null, 0, null);
		  algo.addCodeLine(tlAlgo7, null, 0, null);
		  algo.addCodeLine(tlAlgo8, null, 0, null);
		  algo.addCodeLine(tlAlgo9, null, 0, null);
		  algo.addCodeLine(tlAlgo10 + " " + maxC + ".", null, 0, null);
		  algo.addCodeLine(tlAlgo11, null, 0, null);
		  algo.addCodeLine(tlAlgo12, null, 0, null);
		  
		  lang.nextStep(tlAlgoHeader + "SCAN");
		  
		  algoHeader.hide();
		  algo.hide();
		  
		  ArrayList<Primitive> example = new ArrayList<Primitive>();
		  
		  String tlExampleHeader = translator.translateMessage("exampleHeader");
		  Text exampleHeader = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlExampleHeader , "example", null, headerProps);
		  
		  // Strings from input needed for several texts
		  String c = Integer.toString(cylinder);
		  String strQueue = q.stream().map(Object::toString).collect(Collectors.joining(", "));
		  String cHP = Integer.toString(currentHeadPosition);
		  @SuppressWarnings("unused")
      String lR = Integer.toString(lastRequest);
		  
		  // Example text
		  String tlEg1 = translator.translateMessage("eg1");
		  String tlEg2 = translator.translateMessage("eg2");
		  String tlEg3 = translator.translateMessage("eg3");
		  String tlEg4 = translator.translateMessage("eg4");
		  String tlEg5 = translator.translateMessage("eg5");
		  String tlEg6 = translator.translateMessage("eg6");
		  String tlEg7 = translator.translateMessage("eg7");
		  String tlEg8 = translator.translateMessage("eg8");
		  String tlEg9 = translator.translateMessage("eg9");
		  String tlEg10 = translator.translateMessage("eg10");
		  
		  TextProperties resultProps = new TextProperties();
		  resultProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
		  
		  Text eg1 = lang.newText(new Offset(900, 130, headline, AnimalScript.DIRECTION_NW), tlEg1 + " " + c + " " + tlEg2 + " (0 - " + maxC + ")", "eg1", null, resultProps);
		  Text eg2 = lang.newText(new Offset(900, 155, headline, AnimalScript.DIRECTION_NW), tlEg3 + ": {" + strQueue + "}", "eg2", null, resultProps);
		  Text eg3 = lang.newText(new Offset(900, 180, headline, AnimalScript.DIRECTION_NW), tlEg9 + ": { }", "eg3", null, resultProps);
		  Text eg4 = lang.newText(new Offset(900, 205, headline, AnimalScript.DIRECTION_NW), tlEg10 + ": { }", "eg4", null, resultProps);
		  Text eg5 = lang.newText(new Offset(900, 230, headline, AnimalScript.DIRECTION_NW), tlEg4 + ": " + cHP, "eg5", null, resultProps);
		  Text eg6 = lang.newText(new Offset(900, 255, headline, AnimalScript.DIRECTION_NW), tlEg5 + ": " + cHP, "eg6", null, resultProps);
		  Text eg7 = lang.newText(new Offset(900, 280, headline, AnimalScript.DIRECTION_NW), tlEg6 + ": ", "eg7", null, resultProps);
		  
		  SourceCodeProperties algoProps = new SourceCodeProperties();
		  algoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
		  algoProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlight);
		  
		  SourceCode algo2 = lang.newSourceCode(new Offset(900, 320, headline, AnimalScript.DIRECTION_NW), "algo2", null, algoProps);
		  algo2.addCodeLine(tlAlgo1, null, 0, null);
		  algo2.addCodeLine(tlAlgo2, null, 0, null);
		  algo2.addCodeLine(tlAlgo3, null, 0, null);
		  algo2.addCodeLine(tlAlgo4, null, 0, null);
		  algo2.addCodeLine(tlAlgo5, null, 0, null);
		  algo2.addCodeLine(tlAlgo6, null, 0, null);
		  algo2.addCodeLine(tlAlgo7, null, 0, null);
		  algo2.addCodeLine(tlAlgo8, null, 0, null);
		  algo2.addCodeLine(tlAlgo9, null, 0, null);
		  algo2.addCodeLine(tlAlgo10 + maxC, null, 0, null);
		  algo2.addCodeLine(tlAlgo11, null, 0, null);
		  algo2.addCodeLine(tlAlgo12, null, 0, null);
		  
		  lang.nextStep();
		  
		  // Highlight right line of pseudo code
		  algo2.highlight(0);
		  
		  // Variable to set initial direction: true -> descending, false -> ascending
		  boolean direction;
		  
		  if(currentHeadPosition <= lastRequest) {
			  eg7.setText(tlEg6 + ": " + tlEg7, null, null);
			  direction = true;
		  }
		  
		  else {
			  eg7.setText(tlEg6 + ": " + tlEg8, null, null);
			  direction = false;
		  }		  
		  
		  lang.nextStep(tlExampleHeader + " SCAN");
		  
		  // Highlight right line of pseudo code
		  algo2.unhighlight(0);
		  algo2.highlight(1);
		  algo2.highlight(2);
		  
		  // Lists for disk request less or greater than current head position
		  ArrayList<Integer> lessOrEqual = new ArrayList<Integer>();
		  ArrayList<Integer> greater = new ArrayList<Integer>();
		  
		  for(Integer e : q) {
			  
			  if(e <= currentHeadPosition) {
				  lessOrEqual.add(e);
			  }
			  
			  else {
				  greater.add(e);
			  }
			  
		  }
		  
		  String strLess = lessOrEqual.stream().map(Object::toString).collect(Collectors.joining(", "));
		  eg3.setText(tlEg9 + ": {" + strLess + "}", null, null);
		  
		  lang.nextStep();
		  
		  // Highlight right line of pseudo code
		  algo2.unhighlight(2);
		  algo2.highlight(3);
		  
		  String strGreater = greater.stream().map(Object::toString).collect(Collectors.joining(", "));
		  eg4.setText(tlEg10 + ": {" + strGreater + "}", null, null);
		  
		  lang.nextStep();
		  
		  // Highlight right line of pseudo code
		  algo2.unhighlight(1);
		  algo2.unhighlight(3);
		  algo2.highlight(4);
		  
		  // Sort requests less than current head position in reverse order
		  Comparator<Integer> cmp = Collections.reverseOrder();
		  Collections.sort(lessOrEqual, cmp);
		  
		  String strLess2 = lessOrEqual.stream().map(Object::toString).collect(Collectors.joining(", "));
		  eg3.setText(tlEg9 + ": {" + strLess2 + "}", null, null);
		  
		  lang.nextStep();
		  
		  // Highlight right line of pseudo code
		  algo2.unhighlight(4);
		  algo2.highlight(5);
		  
		  // Sort requests greater than current head position
		  Collections.sort(greater);
		  
		  String strGreater2 = greater.stream().map(Object::toString).collect(Collectors.joining(", "));
		  eg4.setText(tlEg10 + ": {" + strGreater2 + "}", null, null);
		  
		  lang.nextStep();
		  
		  // Disk rectangle
		  RectProperties diskProps = new RectProperties();
		  diskProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		  diskProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		  diskProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		  diskProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		  
		  Rect disk = lang.newRect(new Offset(0, 285, headline, AnimalScript.DIRECTION_NW), new Offset(800, 685, headline, AnimalScript.DIRECTION_NW), "disk", null, diskProps);
		  
		  example.add(disk);
		  
		  // Highlight right line of pseudo code
		  algo2.unhighlight(5);
		  algo2.highlight(6);
		  
		  // Basic scale lines
		  Offset[] scale1Nodes = {new Offset(0, 285, headline, AnimalScript.DIRECTION_NW), new Offset(800, 285, headline, AnimalScript.DIRECTION_NW)};
		  Polyline scale1 = lang.newPolyline(scale1Nodes, "scale1", null);
		  Offset[] scale2Nodes = {new Offset(0, 286, headline, AnimalScript.DIRECTION_NW), new Offset(800, 286, headline, AnimalScript.DIRECTION_NW)};
		  Polyline scale2 = lang.newPolyline(scale2Nodes, "scale2", null);
		  Offset[] leftEdgeNodes = {new Offset(0, 265, headline, AnimalScript.DIRECTION_NW), new Offset(0, 305, headline, AnimalScript.DIRECTION_NW)};
		  Polyline leftEdge = lang.newPolyline(leftEdgeNodes, "leftEdge", null);
		  Offset[] rightEdgeNodes = {new Offset(800, 265, headline, AnimalScript.DIRECTION_NW), new Offset(800, 305, headline, AnimalScript.DIRECTION_NW)};
		  Polyline rightEdge = lang.newPolyline(rightEdgeNodes, "rightEdge", null);
		  
		  example.add(scale1);
		  example.add(scale2);
		  example.add(leftEdge);
		  example.add(rightEdge);
		  
		  // Basic scale text labels
		  TextProperties scaleProps = new TextProperties();
		  scaleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		  
		  Text t0 = lang.newText(new Offset(-5, 230, headline, AnimalScript.DIRECTION_NW), "0", "t0", null, scaleProps);
		  Text tRightEdge = lang.newText(new Offset(780, 230, headline, AnimalScript.DIRECTION_NW), c, "t" + c, null, scaleProps);
		  
		  example.add(t0);
		  example.add(tRightEdge);
		  
		  int x = cylinder / 10;
		  
		  // Scale lines and scale text label for every queue element
		  for(int i = 1; i < 10; i++) {
			   
			  int y = i * x;
			  
			  // Name of the element for naming in AnimalScript and scale text label
			  String elementName = Integer.toString(y);
			  
			  Offset[] elementNodes = {new Offset(xOffset(y, cylinder), 265, headline, AnimalScript.DIRECTION_NW), new Offset(xOffset(y, cylinder), 285, headline, AnimalScript.DIRECTION_NW)};
			  Polyline element = lang.newPolyline(elementNodes, "l" + elementName, null);
			  
			  example.add(element);
		
			  // Text label
			  Text tElement = lang.newText(new Offset(xOffset(y, cylinder) - 10, 230, headline, AnimalScript.DIRECTION_NW), elementName, "t" + elementName, null, scaleProps);
			  example.add(tElement);
		  }
		  
		  
		  // Result text
		  Text result1 = lang.newText(new Offset(0, 130, headline, AnimalScript.DIRECTION_NW), "Schedule: " + cHP, "result1", null, resultProps);
		  
		  String tlResult2 = translator.translateMessage("result2");
		  
		  Text result2 = lang.newText(new Offset(0, 155, headline, AnimalScript.DIRECTION_NW), tlResult2 + ": ", "result2", null, resultProps);
		  
		  // Properties of circles
		  CircleProperties circleProps = new CircleProperties();
		  circleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
		  circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		  circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
		  
		  Circle startCircle = lang.newCircle(new Offset(xOffset(currentHeadPosition, cylinder), 310, headline, AnimalScript.DIRECTION_NW), 5, "start", null, circleProps);
		  
		  example.add(startCircle);
		  
		  // Properties of arrows between circles
		  PolylineProperties arrowProps = new PolylineProperties();
		  arrowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorA);
		  arrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		  
		  lang.nextStep();
		  
		  // Highlight right line of pseudo code
		  algo2.unhighlight(6);
		  algo2.highlight(7);
		  
		  // Initial values for y coordinate offset of new circle
		  int yOffset = 350 / (q.size() + 1);
		  // Initial value the y offset will be multiplied with
		  int n = 1;
		  // Initial start position of head
		  int start = currentHeadPosition;
		  // Initial value of distance
		  int dist = 0;
		  // List to keep track of schedule and print it
		  ArrayList<Integer> schedule = new ArrayList<Integer>();
		  schedule.add(start);
		  
		  // Initial value for y coordinate offset of new circle of circular variant
		  int cYOffset = 350 / (q.size() + 2);
		  // Initial start position of head of circular variant
		  int cStart = currentHeadPosition;
		  // Initial value the y offset will be multiplied with
		  int cN = 1;
		  // Initial value of distance
		  int cDist = 0;
		  // List to keep track of schedule of circular variant and print it
		  ArrayList<Integer> cSchedule = new ArrayList<Integer>();
		  cSchedule.add(cStart);
		  
		  // Text for circular variant of SCAN algorithm
		  String tlVariant = translator.translateMessage("variationHeader");
		  
		  Text variant = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlVariant, "variant", null, headerProps);
		  
		  String tlIntermediate1 = translator.translateMessage("intermediate1");
		  String tlIntermediate2 = translator.translateMessage("intermediate2");
		  String tlIntermediate3 = translator.translateMessage("intermediate3");
		  String tlIntermediate4 = translator.translateMessage("intermediate4");
		  
		  SourceCode intermediate = lang.newSourceCode(new Offset(0, 130, headline, AnimalScript.DIRECTION_NW), "intermediate", null, textProps);
		  intermediate.addCodeLine(tlIntermediate1, null, 0, null);
		  intermediate.addCodeLine(tlIntermediate2, null, 0, null);
		  intermediate.addCodeLine(tlIntermediate3, null, 0, null);
		  intermediate.addCodeLine(tlIntermediate4, null, 0, null);
		  
		  // Properties of arrows between circles to emphasize change
		  PolylineProperties cArrowProps = new PolylineProperties();
		  cArrowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		  cArrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		  
		  String tlAlgo21 = translator.translateMessage("algo21");
		  String tlAlgo22 = translator.translateMessage("algo22");
 		  String tlAlgo23 = translator.translateMessage("algo23");
 		  String tlAlgo24 = translator.translateMessage("algo24");
		  String tlAlgo25 = translator.translateMessage("algo25");
		  String tlAlgo26 = translator.translateMessage("algo26");
		  
		  SourceCode algo3 = lang.newSourceCode(new Offset(0, 130, headline, AnimalScript.DIRECTION_NW), "intro", null, textProps);
		  algo3.addCodeLine(tlAlgo1, null, 0, null);
		  algo3.addCodeLine(tlAlgo2, null, 0, null);
		  algo3.addCodeLine(tlAlgo3, null, 0, null);
		  algo3.addCodeLine(tlAlgo4, null, 0, null);
		  algo3.addCodeLine(tlAlgo21, null, 0, null);
		  algo3.addCodeLine(tlAlgo22, null, 0, null);
		  algo3.addCodeLine(tlAlgo23, null, 0, null);
		  algo3.addCodeLine(tlAlgo24, null, 0, null);
		  algo3.addCodeLine(tlAlgo25 + " " + maxC + ".", null, 0, null);
		  algo3.addCodeLine(tlAlgo26, null, 0, null);
		  algo3.addCodeLine(tlAlgo11, null, 0, null);
		  algo3.addCodeLine(tlAlgo12, null, 0, null);
		  
		  SourceCode algo4 = lang.newSourceCode(new Offset(900, 320, headline, AnimalScript.DIRECTION_NW), "intro", null, algoProps);
		  algo4.addCodeLine(tlAlgo1, null, 0, null);
		  algo4.addCodeLine(tlAlgo2, null, 0, null);
		  algo4.addCodeLine(tlAlgo3, null, 0, null);
		  algo4.addCodeLine(tlAlgo4, null, 0, null);
		  algo4.addCodeLine(tlAlgo21, null, 0, null);
		  algo4.addCodeLine(tlAlgo22, null, 0, null);
		  algo4.addCodeLine(tlAlgo23, null, 0, null);
		  algo4.addCodeLine(tlAlgo24, null, 0, null);
		  algo4.addCodeLine(tlAlgo25 + " " + maxC + ".", null, 0, null);
		  algo4.addCodeLine(tlAlgo26, null, 0, null);
		  algo4.addCodeLine(tlAlgo11, null, 0, null);
		  algo4.addCodeLine(tlAlgo12, null, 0, null);
		  
		  variant.hide();
		  intermediate.hide();
		  algo3.hide();
		  algo4.hide();
		  
		  if(direction) {
			  
			  // Highlight right line of pseudo code
			  algo2.unhighlight(6);
			  algo2.highlight(7);
			  
			  ArrayList<Integer> first = new ArrayList<Integer>();
			  first.add(lessOrEqual.get(0));
			  
			  int[] newnewStartandDist = draw(first, start, schedule, cylinder, n, yOffset, dist, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, q, lessOrEqual, tlEg9);
			  
			  // Get initial parameters for next iterations
			  int newnewStart = newnewStartandDist[0];
			  int newnewDist = newnewStartandDist[1];
			  int newnewN = newnewStartandDist[2];
			  
			  algo2.unhighlight(7);
			  algo2.highlight(8);
			  algo2.highlight(9);
			  
			  // Draw schedule
			  lessOrEqual.remove(0);
			  int[] newStartandDist = draw(lessOrEqual, newnewStart, schedule, cylinder, newnewN, yOffset, newnewDist, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, q, lessOrEqual, tlEg9);
			  
			  // Get initial parameters for next iterations
			  int newStart = newStartandDist[0];
			  int newDist = newStartandDist[1];
			  int newN = newStartandDist[2];
			  
			  ArrayList<Integer> Left = new ArrayList<Integer>();
			  Left.add(0);
			  
			  int[] j = draw(Left, newStart, schedule, cylinder, newN, yOffset, newDist, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, q, Left, tlEg9);
			  
			  int k = j[0];
			  int l = j[1];
			  int m = j[2];
			  
			  // Highlight right line of pseudo code
			  algo2.unhighlight(8);
			  algo2.unhighlight(9);
			  algo2.highlight(10);
			  
			  ArrayList<Integer> first2 = new ArrayList<Integer>();
			  first2.add(greater.get(0));
			  
			  // Draw schedule
			  int[] nSaD = draw(first2, k, schedule, cylinder, m, yOffset, l, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, q, greater, tlEg10);
			  
			  // Highlight right line of code
			  algo2.unhighlight(10);
			  algo2.highlight(11);
			  
			  // Get initial parameters for next iterations
			  int nS = nSaD[0];
			  int nD = nSaD[1];
			  int nN = nSaD[2];
			  
			  greater.remove(0);
			  
			  draw(greater, nS, schedule, cylinder, nN, yOffset, nD, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, q, greater, tlEg10);
			  
			  // Hide everything except headline
			  lang.hideAllPrimitivesExcept(hl);
			  
			  exampleHeader.hide();
			  variant.show();
			  intermediate.show();
			  
			  lang.nextStep(tlVariant);
			  
			  variant.hide();
			  intermediate.hide();
			  
			  algo3.show();
			  
			  Text algoHeader2 = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlAlgoHeader + " CSCAN", "algo", null, headerProps);
			  
			  lang.nextStep(tlAlgoHeader + " CSCAN");
			  
			  algoHeader2.hide();
			  algo3.hide();
			  algo4.show();
			  exampleHeader.show();
			  
			  eg2.setText(tlEg3 + ": {" + strQueue + "}", null, null);
			  eg3.setText(tlEg9 + ": { }", null, null);
			  eg4.setText(tlEg10 + ": { }", null, null);
			  eg7.setText(tlEg6 + ": ", null, null);
			 
			  eg1.show();
			  eg2.show();
			  eg3.show();
			  eg4.show();
			  eg5.show();
			  eg6.show();
			  eg7.show();
			  
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.highlight(0);
			  
			  eg7.setText(tlEg6 + ": " + tlEg7, null, null);
			  
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(0);
			  algo4.highlight(1);
			  algo4.highlight(2);
			  
			// Lists for disk request less or greater than current head position
			  ArrayList<Integer> lessOrEqual2 = new ArrayList<Integer>();
			  ArrayList<Integer> greater2 = new ArrayList<Integer>();
			  
			  for(Integer e : cQ) {
				  
				  if(e <= currentHeadPosition) {
					  lessOrEqual2.add(e);
				  }
				  
				  else {
					  greater2.add(e);
				  }
				  
			  }
			  
			  eg3.setText(tlEg9 + ": {" + strLess + "}", null, null);
			 
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(2);
			  algo4.highlight(3);
			  
			  eg4.setText(tlEg10 + ": {" + strGreater2 + "}", null, null);
			  
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(1);
			  algo4.unhighlight(3);
			  algo4.highlight(4);
			  
			  // Sort requests greater than current head position in reverse order
			  Collections.sort(greater2, cmp);
			  
			  String strGreater3 = greater2.stream().map(Object::toString).collect(Collectors.joining(", "));
			  eg4.setText(tlEg10 + ": {" + strGreater3 + "}", null, null);
			 
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(4);
			  algo4.highlight(5);
			  
			  result1.setText("Schedule: " + cHP, null, null);
			  result2.setText(tlResult2 + ": ", null, null);
			  
			  result1.show();
			  result2.show();

			  for (Primitive e : example) {
				  e.show();
			  }
			  
			  lang.nextStep();
			  
			  algo4.unhighlight(5);
			  algo4.highlight(6);
			  
			  ArrayList<Integer> cFirst1 = new ArrayList<Integer>();
			  cFirst1.add(lessOrEqual2.get(0));
			  
			  int[] newnewnewStartandDist = draw(cFirst1, cStart, cSchedule, cylinder, cN, cYOffset, cDist, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, cQ, lessOrEqual2, tlEg9);
			  
			  // Get initial parameters for next iterations
			  int newnewnewStart = newnewnewStartandDist[0];
			  int newnewnewDist = newnewnewStartandDist[1];
			  int newnewnewN = newnewnewStartandDist[2];
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(6);
			  algo4.highlight(7);
			  algo4.highlight(8);
			  
			  // Draw schedule
			  lessOrEqual2.remove(0);
			  int[] cNewStartandDist = draw(lessOrEqual2, newnewnewStart, cSchedule, cylinder, newnewnewN, cYOffset, newnewnewDist, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, cQ, lessOrEqual2, tlEg9);
			  
			  // Get initial parameters for next iterations
			  int cNewStart = cNewStartandDist[0];
			  int cNewDist = cNewStartandDist[1];
			  int cNewN = cNewStartandDist[2];
			  
			  ArrayList<Integer> cLeft = new ArrayList<Integer>();
			  cLeft.add(0);
			  
			  int[] a = draw(cLeft, cNewStart, cSchedule, cylinder, cNewN, cYOffset, cNewDist, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, cQ, cLeft, tlEg9);
			  
			  int b = a[0];
			  int e = a[1];
			  int d = a[2];
			  
			  ArrayList<Integer> cRight = new ArrayList<Integer>();
			  cRight.add(maxCylinder);

			  // Highlight right line of pseudo code
			  algo4.unhighlight(7);
			  algo4.unhighlight(8);
			  algo4.highlight(9);
			  
			  int[] f = draw(cRight, b, cSchedule, cylinder, d, cYOffset, e, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, cQ, cRight, tlEg9);
			  
			  int g = f[0];
			  int h = f[1];
			  int i = f[2];
			  
			  
			  ArrayList<Integer> cFirst2 = new ArrayList<Integer>();
			  cFirst2.add(greater2.get(0));
			  
			  // Highlight right line of code
			  algo4.unhighlight(9);
			  algo4.highlight(10);
			  
			  // Draw schedule
			  int[] cnSaD = draw(cFirst2, g, cSchedule, cylinder, i, cYOffset, h, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, cQ, greater2, tlEg10);
			  
			  // Highlight right line of code
			  algo4.unhighlight(9);
			  algo4.highlight(10);
			  
			  // Get initial parameters for next iterations
			  int cNS = cnSaD[0];
			  int cND = cnSaD[1];
			  int cNN = cnSaD[2];
			  
			  greater2.remove(0);
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(10);
			  algo4.highlight(11);
			  
			  draw(greater2, cNS, cSchedule, cylinder, cNN, cYOffset, cND, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, cQ, greater2, tlEg10);
			  
		  }
		  
		  else {
			  
			  // Highlight right line of pseudo code
			  algo2.unhighlight(6);
			  algo2.highlight(7);
			  
			  ArrayList<Integer> first = new ArrayList<Integer>();
			  first.add(greater.get(0));
			  
			  int[] newnewStartandDist = draw(first, start, schedule, cylinder, n, yOffset, dist, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, q, greater, tlEg10);
			  
			  // Get initial parameters for next iterations
			  int newnewStart = newnewStartandDist[0];
			  int newnewDist = newnewStartandDist[1];
			  int newnewN = newnewStartandDist[2];
			  
			  algo2.unhighlight(7);
			  algo2.highlight(8);
			  algo2.highlight(9);
			  
			  // Draw schedule
			  greater.remove(0);
			  int[] newStartandDist = draw(greater, newnewStart, schedule, cylinder, newnewN, yOffset, newnewDist, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, q, greater, tlEg10);
			  
			  // Get initial parameters for next iterations
			  int newStart = newStartandDist[0];
			  int newDist = newStartandDist[1];
			  int newN = newStartandDist[2];
			  
			  ArrayList<Integer> Right = new ArrayList<Integer>();
			  Right.add(maxCylinder);
			  
			  int[] j = draw(Right, newStart, schedule, cylinder, newN, yOffset, newDist, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, q, Right, tlEg10);
			  
			  int k = j[0];
			  int l = j[1];
			  int m = j[2];
			  
			  // Highlight right line of pseudo code
			  algo2.unhighlight(8);
			  algo2.unhighlight(9);
			  algo2.highlight(10);
			  
			  ArrayList<Integer> first2 = new ArrayList<Integer>();
			  first2.add(lessOrEqual.get(0));
			  
			  // Draw schedule
			  int[] nSaD = draw(first2, k, schedule, cylinder, m, yOffset, l, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, q, lessOrEqual, tlEg9);
			  
			  // Highlight right line of code
			  algo2.unhighlight(10);
			  algo2.highlight(11);
			  
			  // Get initial parameters for next iterations
			  int nS = nSaD[0];
			  int nD = nSaD[1];
			  int nN = nSaD[2];
			  
			  lessOrEqual.remove(0);
			  
			  draw(lessOrEqual, nS, schedule, cylinder, nN, yOffset, nD, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, q, lessOrEqual, tlEg9);
			  
			  // Hide everything except headline
			  lang.hideAllPrimitivesExcept(hl);
			  
			  exampleHeader.hide();
			  variant.show();
			  intermediate.show();
			  
			  lang.nextStep(tlVariant);
			  
			  variant.hide();
			  intermediate.hide();
			  
			  algo3.show();
			  
			  Text algoHeader2 = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlAlgoHeader + " CSCAN", "algo", null, headerProps);
			  
			  lang.nextStep(tlAlgoHeader + " CSCAN");
			  
			  algoHeader2.hide();
			  algo3.hide();
			  algo4.show();
			  exampleHeader.show();
			  
			  eg2.setText(tlEg3 + ": {" + strQueue + "}", null, null);
			  eg3.setText(tlEg9 + ": { }", null, null);
			  eg4.setText(tlEg10 + ": { }", null, null);
			  eg7.setText(tlEg6 + ": ", null, null);
			 
			  eg1.show();
			  eg2.show();
			  eg3.show();
			  eg4.show();
			  eg5.show();
			  eg6.show();
			  eg7.show();
			  
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.highlight(0);
			  
			  eg7.setText(tlEg6 + ": " + tlEg8, null, null);
			  
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(0);
			  algo4.highlight(1);
			  algo4.highlight(2);
			  
			  // Lists for disk request less or greater than current head position
			  ArrayList<Integer> lessOrEqual2 = new ArrayList<Integer>();
			  ArrayList<Integer> greater2 = new ArrayList<Integer>();
			  
			  for(Integer e : cQ) {
				  
				  if(e <= currentHeadPosition) {
					  lessOrEqual2.add(e);
				  }
				  
				  else {
					  greater2.add(e);
				  }
				  
			  }
			  Collections.sort(greater2);
			  
			  eg3.setText(tlEg9 + ": {" + strLess + "}", null, null);
			 
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(2);
			  algo4.highlight(3);
			  
			  eg4.setText(tlEg10 + ": {" + strGreater + "}", null, null);
			  
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(1);
			  algo4.unhighlight(3);
			  algo4.highlight(4);
			  
			  // Sort requests less or equal than current head position
			  Collections.sort(lessOrEqual2);
			  
			  String strLess3 = lessOrEqual2.stream().map(Object::toString).collect(Collectors.joining(", "));
			  
			  eg3.setText(tlEg9 + ": {" + strLess3 + "}", null, null);
			  eg4.setText(tlEg10 + ": {" + strGreater2 + "}", null, null);
			 
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(4);
			  algo4.highlight(5);
			  
			  result1.setText("Schedule: " + cHP, null, null);
			  result2.setText(tlResult2 + ": ", null, null);
			  
			  result1.show();
			  result2.show();

			  for (Primitive e : example) {
				  e.show();
			  }
			  
			  lang.nextStep();
			  
			  algo4.unhighlight(5);
			  algo4.highlight(6);
			  
			  ArrayList<Integer> cFirst1 = new ArrayList<Integer>();
			  cFirst1.add(greater2.get(0));
			  int[] newnewnewStartandDist = draw(cFirst1, cStart, cSchedule, cylinder, cN, cYOffset, cDist, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, cQ, greater2, tlEg10);
			  
			  // Get initial parameters for next iterations
			  int newnewnewStart = newnewnewStartandDist[0];
			  int newnewnewDist = newnewnewStartandDist[1];
			  int newnewnewN = newnewnewStartandDist[2];
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(6);
			  algo4.highlight(7);
			  algo4.highlight(8);
			  
			  // Draw schedule
			  greater2.remove(0);
			  int[] cNewStartandDist = draw(greater2, newnewnewStart, cSchedule, cylinder, newnewnewN, cYOffset, newnewnewDist, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, cQ, greater2, tlEg10);
			  
			  // Get initial parameters for next iterations
			  int cNewStart = cNewStartandDist[0];
			  int cNewDist = cNewStartandDist[1];
			  int cNewN = cNewStartandDist[2];
			  
			  ArrayList<Integer> cRight = new ArrayList<Integer>();
			  cRight.add(maxCylinder);
			  
			  int[] a = draw(cRight, cNewStart, cSchedule, cylinder, cNewN, cYOffset, cNewDist, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, cQ, cRight, tlEg10);
			  
			  int b = a[0];
			  int e = a[1];
			  int d = a[2];
			  
			  ArrayList<Integer> cLeft = new ArrayList<Integer>();
			  cLeft.add(0);

			  // Highlight right line of pseudo code
			  algo4.unhighlight(7);
			  algo4.unhighlight(8);
			  algo4.highlight(9);
			  
			  int[] f = draw(cLeft, b, cSchedule, cylinder, d, cYOffset, e, headline, circleProps, arrowProps, result1, result2, eg4, eg2, tlEg3, cQ, cLeft, tlEg10);
			  
			  int g = f[0];
			  int h = f[1];
			  int i = f[2];
			  
			  
			  ArrayList<Integer> cFirst2 = new ArrayList<Integer>();
			  cFirst2.add(lessOrEqual2.get(0));
			  
			  // Highlight right line of code
			  algo4.unhighlight(9);
			  algo4.highlight(10);
			  
			  // Draw schedule
			  int[] cnSaD = draw(cFirst2, g, cSchedule, cylinder, i, cYOffset, h, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, cQ, lessOrEqual2, tlEg9);
			  
			  // Highlight right line of code
			  algo4.unhighlight(9);
			  algo4.highlight(10);
			  
			  // Get initial parameters for next iterations
			  int cNS = cnSaD[0];
			  int cND = cnSaD[1];
			  int cNN = cnSaD[2];
			  
			  lessOrEqual2.remove(0);
			  
			  // Highlight right line of pseudo code
			  algo4.unhighlight(10);
			  algo4.highlight(11);
			  
			  draw(lessOrEqual2, cNS, cSchedule, cylinder, cNN, cYOffset, cND, headline, circleProps, arrowProps, result1, result2, eg3, eg2, tlEg3, cQ, lessOrEqual2, tlEg9);
			  
		  }
		  
		lang.hideAllPrimitivesExcept(hl);  
		
		String tlConclusion = translator.translateMessage("outroHeader");
		@SuppressWarnings("unused")
    Text conclusion = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlConclusion, "conclusion", null, headerProps);
		
		String outro1 = translator.translateMessage("outro1");
		String outro2 = translator.translateMessage("outro2");
		String outro3 = translator.translateMessage("outro3");
		String outro4 = translator.translateMessage("outro4");
		String outro5 = translator.translateMessage("outro5");
		String outro6 = translator.translateMessage("outro6");
		String outro7 = translator.translateMessage("outro7");
		String outro8 = translator.translateMessage("outro8");
		
		SourceCode outro = lang.newSourceCode(new Offset(0, 130, headline, AnimalScript.DIRECTION_NW), "outro", null, textProps); 
		outro.addCodeLine(outro1, null, 0, null);
		outro.addCodeLine(outro2, null, 0, null);
		outro.addCodeLine(outro3, null, 0, null);
		outro.addCodeLine(outro4, null, 0, null);
		outro.addCodeLine(outro5, null, 0, null);
		outro.addCodeLine(outro6, null, 0, null);
		outro.addCodeLine(outro7, null, 0, null);
		outro.addCodeLine(outro8, null, 0, null);
		
		lang.nextStep(tlConclusion);
		
		  
	  }
	  
	  // Helpers
	  
	  /**
	   * Calculates the offset of the x part of the coordinates of the scale line
	   * offset for x = (element value * the width of the disk rectangle in pixels) / number of cylinders
	   *
	   * @param int value the value of the current queue element
	   * @param int cylinder the number of cylinders
	   * @return int the x offset
	   */
	  public int xOffset (int value, int cylinder) {
		  return (value * 800) / cylinder;
	  }
	  
	  /**
	   * Draws the circles and arrows between them 
	   *
	   * @param requests the list of requests less or greater than current head position 
	   * @param startRequest the initial start request
	   * @param alreadyScheduled the list to keep track of which requests have already been scheduled  
	   * @param nrCylinder the number of cylinders
	   * @param i the initial value the y offset will be multiplied with
	   * @param y the y offset
	   * @param d the initial distance value 
	   * @param hl the text primitive for offset (in this case the headline)
	   * @param cP the circle properties
	   * @param aP the arrow properties
	   * @param r1 the result text for schedule
	   * @param r2 the result text for distance
	   * @param l the list to remove elements from
	   * @param q the queue to remove elements from 
	   * @param t1 the text for the new queue 
	   * @param q2 the complete q
	   * @param complete the complete lessOrEqual or greater list
	   * @param t2 the text for the new lessOrEqual or greater
	   * @return array the current start request, distance and value the y offset will be multiplied with 
	   */
	  @SuppressWarnings("unused")
    public int[] draw (ArrayList<Integer> requests, int startRequest, ArrayList<Integer>alreadyScheduled, int nrCylinder, int i, int y, int d, Text hl, CircleProperties cP, PolylineProperties aP, Text r1, Text r2, Text l, Text q, String t1, ArrayList<Integer> q2, ArrayList<Integer> complete, String t2) {
		  ArrayList<Integer> x = new ArrayList<Integer>();
		  
		  for (Integer el: complete) {
			  x.add(el);
		  }
		  
		  for (Integer e : requests) {
			  
			  alreadyScheduled.add(e);
			  String nextCircleName = Integer.toString(e);
			  String strSchedule = alreadyScheduled.stream().map(Object::toString).collect(Collectors.joining(", "));
			  
			  // Circle for next request
			  Circle circle = lang.newCircle(new Offset(xOffset(e, nrCylinder), 310 + (i * y), hl, AnimalScript.DIRECTION_NW), 5, "c" + nextCircleName, null, cP);
			  
			  // Arrow to next request
			  Offset[] arrowNodes = {new Offset(xOffset(startRequest, nrCylinder), 310 + ((i-1) * y), hl, AnimalScript.DIRECTION_NW), new Offset(xOffset(e, nrCylinder), 310 + (i * y), hl, AnimalScript.DIRECTION_NW)};
			  Polyline arrow = lang.newPolyline(arrowNodes, "a" + nextCircleName, null, aP);
			  
			  d += Math.abs(startRequest - e);
			  
			  // Result text
			  r1.setText("Schedule: " + strSchedule, null, null);
			  
			  String tlSame = translator.translateMessage("result2");
			  
			  r2.setText(tlSame + ": " + Integer.toString(d), null, null);
			  
			  startRequest = e;
				  
			  x.remove(e);
			  String z = x.stream().map(Object::toString).collect(Collectors.joining(", "));
			  l.setText(t2 + ":" + "{" + z + " }" , null, null);
			  
			  q2.remove(e);
			  String newQ = q2.stream().map(Object::toString).collect(Collectors.joining(", "));
			  q.setText(t1 + ": {" + newQ + " }", null, null);
			  
			  i++;
			  lang.nextStep();
		  }
		  
		  int[] result = {startRequest, d, i};
		  return result;
	  }
	
}