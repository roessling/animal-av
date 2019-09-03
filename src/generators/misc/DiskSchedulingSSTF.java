package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
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
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;



/**
 * @author Johanna Heinz <johanna.heinz@stud.tu-darmstadt.de>
 * @version 1.1 20180508
 */
@SuppressWarnings("unused")
public class DiskSchedulingSSTF implements ValidatingGenerator {

	  /**
	   * The concrete language object used for creating output
	   */
	  private Language lang;
	  private Translator translator;
	  private Locale l;
	  private int anzahlZylinder;
	  private int[] Queue;
	  private int aktuellePositionDesKopfes;
	  private SourceCodeProperties HighlightfarbeDesPseudoCode;
	  private CircleProperties FarbeDerKreise;
	  private PolylineProperties FarbeDerPfeile;

	  public void init(){
		  lang = new AnimalScript("Disk Scheduling: SSTF", "Johanna Heinz", 800, 600);
		  lang.setStepMode(true);
	  }
	  
	  // Initialize via new DiskSchedulingSSTF("resources/DiskSchedulingSSTF", Locale.GERMANY)
	  // or via new DiskSchedulingSSTF("resources/DiskSchedulingSSTF", Locale.US) for english text
	  public DiskSchedulingSSTF(String path, Locale locale) {
		  l = locale;
		  translator = new Translator(path, l);
	  }

	  public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
	      anzahlZylinder = (Integer)primitives.get("Anzahl Zylinder");
	      Queue = (int[])primitives.get("Queue");
	      aktuellePositionDesKopfes = (Integer)primitives.get("Aktuelle Position des Kopfes");
	      
	      HighlightfarbeDesPseudoCode = (SourceCodeProperties)props.getPropertiesByName("Highlightfarbe des Pseudo Code");
	      FarbeDerKreise = (CircleProperties)props.getPropertiesByName("Farbe der Kreise");
	      FarbeDerPfeile = (PolylineProperties)props.getPropertiesByName("Farbe der Pfeile");
	      
	      Color hColor = (Color) HighlightfarbeDesPseudoCode.get("highlightColor");
	      Color cColor = (Color) FarbeDerKreise.get("fillColor");
	      Color aColor = (Color) FarbeDerPfeile.get("color");
	      
	      if(validateInput(props, primitives)) {
	    	  schedule(Queue, anzahlZylinder, aktuellePositionDesKopfes, hColor, cColor, aColor);
	      }
	      
	      return lang.toString();
	  }
	  
	  @Override
		public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
				throws IllegalArgumentException {
	    	
	    	anzahlZylinder = (Integer)primitives.get("Anzahl Zylinder");
	        Queue = (int[])primitives.get("Queue");
	        aktuellePositionDesKopfes = (Integer)primitives.get("Aktuelle Position des Kopfes");
	        
	        String exception = translator.translateMessage("exception");
	        
	        for(int i = 0; i < Queue.length; i++) {
	        	
	        	if(Queue[i] >= anzahlZylinder) {
	        		throw new IllegalArgumentException(exception  + Integer.toString(anzahlZylinder - 1));
	        	}
	        }
	        
	        if(aktuellePositionDesKopfes >= anzahlZylinder) {
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
	      return "Disk Scheduling: Shortest Seek Time First";
	  }

	  public String getAnimationAuthor() {
	      return "Johanna Heinz";
	  }

	  public String getDescription(){
		  String tlDescr1 = translator.translateMessage("intro1");
		  String tlDescr2 = translator.translateMessage("intro2");
		  String tlDescr3 = translator.translateMessage("descr");
		  
	      return tlDescr1
	    		  +"\n"
	    		  +tlDescr2
	    		  +"\n"
	    		  +"\n"
	    		  + tlDescr3 + ".";
	  }

	  public String getCodeExample(){
		  String tlCodeEg1 = translator.translateMessage("codeEg1");
		  String tlCodeEg2 = translator.translateMessage("codeEg2");
		  
	      return tlCodeEg1
	    		  +"\n"
	    		  + tlCodeEg2 + ".";
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
	   * Schedules the disk requests of the input array
	   * 
	   * @param array queue the disk requests
	   * @param int cylinder the number of cylinders
	   * @param int currentHeadPosition the current position of the disk head  
	   * @param Color highlight the highlight color for the pseudo code
	   * @param Color color the color for the circles
	   * @param Color colorA the color for the arrows
	   */
    public void schedule(int[] queue, int cylinder, int currentHeadPosition, Color highlight, Color color, Color colorA) {
		  
		  ArrayList<Integer> q = new ArrayList<Integer>();
		  
		  for(int i = 0; i < queue.length; i++) {
			  q.add(queue[i]);
		  }
		  // Save headline and rectangle around it to exclude from hiding later on
		  ArrayList<Primitive> hl = new ArrayList<Primitive>();
		  
		  // Headline
		  TextProperties hlProps = new TextProperties();
		  hlProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		  
		  Text headline = lang.newText(new Coordinates(40, 40), "Disk Scheduling: Shortest Seek Time First", "headline", null, hlProps);
		  
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
		  
		  SourceCode intro = lang.newSourceCode(new Offset(0, 130, headline, AnimalScript.DIRECTION_NW), "intro", null, textProps);
		  intro.addCodeLine(tlIntro1, null, 0, null);
		  intro.addCodeLine(tlIntro2, null, 0, null);
		  intro.addCodeLine(" ", null, 0, null);
		  intro.addCodeLine(tlIntro3, null, 0, null);
		  intro.addCodeLine(tlIntro4, null, 0, null);
		  
		  lang.nextStep(tlIntroHeader);
		  
		  // Hide introduction
		  introHeader.hide();
		  intro.hide();
		  
		  String tlAlgoHeader = translator.translateMessage("algoHeader");
		  
		  Text algoHeader = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlAlgoHeader, "algo", null, headerProps);
		  
		  String tlAlgo1 = translator.translateMessage("algo1");
		  String tlAlgo2 = translator.translateMessage("algo2");
		  String tlAlgo3 = translator.translateMessage("algo3");
		  
		  SourceCode algo = lang.newSourceCode(new Offset(0, 130, headline, AnimalScript.DIRECTION_NW), "intro", null, textProps);
		  algo.addCodeLine(tlAlgo1, null, 0, null);
		  algo.addCodeLine(tlAlgo2, null, 0, null);
		  algo.addCodeLine(tlAlgo3, null, 0, null);
		  
		  lang.nextStep(tlAlgoHeader);
		  
		  // Hide algo
		  algoHeader.hide();
		  algo.hide();
		  
		  String tlExampleHeader = translator.translateMessage("exampleHeader");
		  Text exampleHeader = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlExampleHeader, "example", null, headerProps);
		  
		  int maxCylinder = cylinder - 1;
		  
		  // Strings from input needed for several texts
		  String c = Integer.toString(cylinder);
		  String maxC = Integer.toString(maxCylinder);
		  String strQueue = q.stream().map(Object::toString).collect(Collectors.joining(", "));
		  String cHP = Integer.toString(currentHeadPosition);
		  
		  // Example text
		  String tlEg1 = translator.translateMessage("eg1");
		  String tlEg2 = translator.translateMessage("eg2");
		  String tlEg3 = translator.translateMessage("eg3");
		  String tlEg4 = translator.translateMessage("eg4");
		  
		  TextProperties resultProps = new TextProperties();
		  resultProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
		  
		  Text eg1 = lang.newText(new Offset(900, 130, headline, AnimalScript.DIRECTION_NW), tlEg1 + " " + c + " " + tlEg2 + " (0 - " + maxC + ")", "eg1", null, resultProps);
		  Text eg2 = lang.newText(new Offset(900, 155, headline, AnimalScript.DIRECTION_NW), tlEg3 + ": {" + strQueue + "}", "eg2", null, resultProps);
		  Text eg3 = lang.newText(new Offset(900, 180, headline, AnimalScript.DIRECTION_NW), tlEg4 + ": " + cHP, "eg3", null, resultProps);
		  
		  SourceCodeProperties algoProps = new SourceCodeProperties();
		  algoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 18));
		  algoProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlight);
		 
		  SourceCode algo2 = lang.newSourceCode(new Offset(900, 230, headline, AnimalScript.DIRECTION_NW), "intro", null, algoProps);
		  algo2.addCodeLine(tlAlgo1, null, 0, null);
		  algo2.addCodeLine(tlAlgo2, null, 0, null);
		  algo2.addCodeLine(tlAlgo3, null, 0, null);
		  
		  lang.nextStep(tlExampleHeader);
		  
		  // Disk rectangle
		  RectProperties diskProps = new RectProperties();
		  diskProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		  diskProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
		  diskProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		  diskProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		  
		  Rect disk = lang.newRect(new Offset(0, 285, headline, AnimalScript.DIRECTION_NW), new Offset(800, 685, headline, AnimalScript.DIRECTION_NW), "disk", null, diskProps);
		  
		  // Basic scale lines
		  Offset[] scale1Nodes = {new Offset(0, 285, headline, AnimalScript.DIRECTION_NW), new Offset(800, 285, headline, AnimalScript.DIRECTION_NW)};
		  Polyline scale1 = lang.newPolyline(scale1Nodes, "scale1", null);
		  Offset[] scale2Nodes = {new Offset(0, 286, headline, AnimalScript.DIRECTION_NW), new Offset(800, 286, headline, AnimalScript.DIRECTION_NW)};
		  Polyline scale2 = lang.newPolyline(scale2Nodes, "scale2", null);
		  Offset[] leftEdgeNodes = {new Offset(0, 265, headline, AnimalScript.DIRECTION_NW), new Offset(0, 305, headline, AnimalScript.DIRECTION_NW)};
		  Polyline leftEdge = lang.newPolyline(leftEdgeNodes, "leftEdge", null);
		  Offset[] rightEdgeNodes = {new Offset(800, 265, headline, AnimalScript.DIRECTION_NW), new Offset(800, 305, headline, AnimalScript.DIRECTION_NW)};
		  Polyline rightEdge = lang.newPolyline(rightEdgeNodes, "rightEdge", null);
		  
		  // Basic scale text labels
		  TextProperties scaleProps = new TextProperties();
		  scaleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		  
		  Text t0 = lang.newText(new Offset(-5, 230, headline, AnimalScript.DIRECTION_NW), "0", "t0", null, scaleProps);
		  Text tRightEdge = lang.newText(new Offset(780, 230, headline, AnimalScript.DIRECTION_NW), c, "t" + c, null, scaleProps);
		  
		  int x = cylinder / 10;
		  
		  // Scale lines and scale text label for every queue element
		  for(int i = 1; i < 10; i++) {
			   
			  int y = i * x;
			  
			  // Name of the element for naming in AnimalScript and scale text label
			  String elementName = Integer.toString(y);
			  
			  Offset[] elementNodes = {new Offset(xOffset(y, cylinder), 265, headline, AnimalScript.DIRECTION_NW), new Offset(xOffset(y, cylinder), 285, headline, AnimalScript.DIRECTION_NW)};
			  Polyline element = lang.newPolyline(elementNodes, "l" + elementName, null);
			  
			  // Text label
			  Text tElement = lang.newText(new Offset(xOffset(y, cylinder) - 10, 230, headline, AnimalScript.DIRECTION_NW), elementName, "t" + elementName, null, scaleProps);
		  }
		  
		  lang.nextStep();
		  
		  
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
		  
		  // List to keep track of schedule and print it
		  ArrayList<Integer> schedule = new ArrayList<Integer>();
		  
		  schedule.add(currentHeadPosition);
		  
		  lang.nextStep();
		  
		  // Calculation of next served request and distance after initial currentHeadPosition
		  int[] nextAndDist = nextRequest(q, currentHeadPosition); 
		  int next = nextAndDist[0];
		  int dist = nextAndDist[1];
		  
		  schedule.add(next);
		  
		  // Highlight right line of pseudo code
		  algo2.highlight(0);
		  
		  // Strings needed for display of schedule and distance
		  String nextCircleName = Integer.toString(next);
		  String d = Integer.toString(dist);
		  
		  int yOffset = 350 / q.size();
		  Circle nC = lang.newCircle(new Offset(xOffset(next, cylinder), 310 + yOffset, headline, AnimalScript.DIRECTION_NW), 5, "c" + nextCircleName, null, circleProps);
		  
		  String strSchedule = schedule.stream().map(Object::toString).collect(Collectors.joining(", "));
		  
		  // Result text
		  result1.setText("Schedule: " + strSchedule, null, null);
		  
		  lang.nextStep();
		  
		  // Highlight right line of pseudo code
		  algo2.unhighlight(0);
		  algo2.highlight(1);
		  
		  // Properties of arrows between circles
		  PolylineProperties arrowProps = new PolylineProperties();
		  arrowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorA);
		  arrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		  
		  Offset[] arrowNodes = {new Offset(xOffset(currentHeadPosition, cylinder), 310, headline, AnimalScript.DIRECTION_NW), new Offset(xOffset(next, cylinder), 310 + yOffset, headline, AnimalScript.DIRECTION_NW)};
		  Polyline arrow = lang.newPolyline(arrowNodes, "a" + nextCircleName, null, arrowProps);
		  
		  
		  result2.setText(tlResult2 + ": " + d, null, null);
		  eg3.setText(tlEg4 + ": " + Integer.toString(next), null, null);
		  
		  lang.nextStep();		  
		  
		  // Initial start point for loop
		  Integer start = next;
		  // Initial value the yOffset will be multiplied with 
		  int n = 2;
		  
		  
		  while(q.size() != 1) {
			  
			  q.remove(start);
			  
			  String strStart = Integer.toString(start);
			  
			  // Highlight right line of pseudo code
			  algo2.unhighlight(1);
			  algo2.highlight(2);
			  
			  strQueue = q.stream().map(Object::toString).collect(Collectors.joining(", "));
			  
			  eg2.setText(tlEg3 + ": {" + strQueue + "}", null, null);
			  
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo2.unhighlight(2);
			  algo2.highlight(0);
			  
			  // Get next nearest request and update distance			  
			  int[] nAndD = nextRequest(q, start); 
			  int nextR = nAndD[0];
			  
			  schedule.add(nextR);
			  dist += nAndD[1];
				  
			  String nextName = Integer.toString(nextR);
			  String distance = Integer.toString(dist);
			  String stringSchedule = schedule.stream().map(Object::toString).collect(Collectors.joining(", "));
				  
			  Circle newCircle = lang.newCircle(new Offset(xOffset(nextR, cylinder), 310 + n * yOffset, headline, AnimalScript.DIRECTION_NW), 5, "c" + nextName, null, circleProps);
			  
			  // Result text
			  result1.setText("Schedule: " + stringSchedule, null, null);
			  
			  lang.nextStep();
			  
			  // Highlight right line of pseudo code
			  algo2.unhighlight(0);
			  algo2.highlight(1);
			  
			  eg3.setText(tlEg4 + ": " + Integer.toString(nextR), null, null);
			  
			  Offset[] aNodes = {new Offset(xOffset(start, cylinder), 310 + (n-1) * yOffset, headline, AnimalScript.DIRECTION_NW), new Offset(xOffset(nextR, cylinder), 310 + n * yOffset, headline, AnimalScript.DIRECTION_NW)};
			  Polyline a = lang.newPolyline(aNodes, "a" + nextName, null, arrowProps);
				  
			  result2.setText(tlResult2 + ": " + distance, null, null);
				  
			  start = nextR;
			  n++;
			  lang.nextStep();
			  
		  }
		  
		  // Highlight right line of pseudo code
		  algo2.unhighlight(1);
		  algo2.highlight(2);
		  
		  eg2.setText(tlEg3 + ": { }", null, null);
		  
		  lang.nextStep();
		  
		  lang.hideAllPrimitivesExcept(hl);
		  
		  // Outro header
		  String tlOutroHeader = translator.translateMessage("outroHeader");
		  Text outroHeader = lang.newText(new Offset(0, 80, headline, AnimalScript.DIRECTION_NW), tlOutroHeader, "conclusion", null, headerProps);
		  
		  // Outro text
		  String tlOutro1 = translator.translateMessage("outro1");
		  String tlOutro2 = translator.translateMessage("outro2");
		  String tlOutro3 = translator.translateMessage("outro3");
		  String tlOutro4 = translator.translateMessage("outro4");
		  String tlOutro5 = translator.translateMessage("outro5");
		  String tlOutro6 = translator.translateMessage("outro6");
		  String tlOutro7 = translator.translateMessage("outro7");
		  String tlOutro8 = translator.translateMessage("outro8");
		  SourceCode outro = lang.newSourceCode(new Offset(0, 130, headline, AnimalScript.DIRECTION_NW), "outro", null, textProps);
		  outro.addCodeLine(tlOutro1, null, 0, null);
		  outro.addCodeLine(tlOutro2, null, 0, null);
		  outro.addCodeLine(tlOutro3, null, 0, null);
		  outro.addCodeLine(tlOutro4, null, 0, null);
		  outro.addCodeLine(tlOutro5, null, 0, null);
		  outro.addCodeLine(tlOutro6, null, 0, null);
		  outro.addCodeLine(tlOutro7, null, 0, null);
		  outro.addCodeLine(tlOutro8, null, 0, null);
		  
		  lang.nextStep(tlOutroHeader);
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
	   * Determines the next served request and distance
	   *
	   * @param ArrayList<Integer> q the disk requests
	   * @param int currentRequest the current request
	   * @return array an array containing the next served request and distance
	   */
	  public int[] nextRequest (ArrayList<Integer> q, int currentRequest) {
		  int min = Integer.MAX_VALUE;
		  int next = 0;
		  
		  for(Integer e : q) {
			  
	  		  int dist = Math.abs(currentRequest - e);
		  
	  		  if(dist < min) {
	  			  min = dist;
	  			  next = e;			  
	  		  }
		  }
		  
		  int[] result = {next, min};
		  return result;
	  }
}