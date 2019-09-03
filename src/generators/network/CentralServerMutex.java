/*
 * CentralServerMutex.java
 * Martin Möller, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.util.Hashtable;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;

public class CentralServerMutex implements ValidatingGenerator {
    private Language lang;
    private int processAmount;
    
	private LinkedList<SourceCode> procs = new LinkedList<SourceCode>();
	private LinkedList<Circle> circs = new LinkedList<Circle>();
	private LinkedList<Text> circTex = new LinkedList<Text>();
	private LinkedList<Integer> wtu = new LinkedList<Integer>();
	private LinkedList<Integer> bt = new LinkedList<Integer>();
	
	private HashMap<String, Text> enters = new HashMap<String, Text>();
	private HashMap<Integer, SimpleEntry<Polyline, Text>> arrows = new HashMap<Integer, SimpleEntry<Polyline, Text>>();
	private TextProperties tProp = new TextProperties(), comments = new TextProperties(), deviceTitle = new TextProperties();
	private SourceCodeProperties scProp = new SourceCodeProperties(), scText = new SourceCodeProperties();
	private ArrayProperties ap = new ArrayProperties();
	private CircleProperties circProp = new CircleProperties();
	private RectProperties recProp = new RectProperties();
	private PolylineProperties enterArrowProp = new PolylineProperties(), exitArrowProp = new PolylineProperties();
	
	private int moveOneStep = 16;
	
	private SourceCode guide, server;
	private Rect rectS;
	private LinkedList<String> queue = new LinkedList<String>();
	private StringArray queueArray;
	private Text q, resText, varQueueText, waitUntil;
	private TwoValueCounter counter;

    public void init(){
        lang = new AnimalScript("Central Server Mutex", "Martin Möller", 800, 600);
        lang.setStepMode(true);
    }
    
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		int[] wtuCheck = (int[])primitives.get("wantToUse");
		int[] btCheck = (int[])primitives.get("blockTime");
		int pACheck = (Integer)primitives.get("processAmount");
		
		if(pACheck < 2)
		{
			JOptionPane.showMessageDialog(null, "You need at least 2 processes!", "Error!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if(wtuCheck.length != btCheck.length)
		{
			JOptionPane.showMessageDialog(null, "blockTime and wantToUse must have the same size!", "Error!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		for (int i = 0; i < wtuCheck.length; i++)
		{
			if(wtuCheck[i] < 1 || wtuCheck[i] > pACheck)
			{
				JOptionPane.showMessageDialog(null, "Only Processes between 1 and " + pACheck + " can use the resource!", "Error!", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			if(i < wtuCheck.length - 1)
			{
				int sum = i + btCheck[i];
				sum = sum < wtuCheck.length ? sum : wtuCheck.length - 1;
				for(int j = i + 1; j <= sum; j++)
				{
					if(wtuCheck[i] == wtuCheck[j])
					{
						JOptionPane.showMessageDialog(null, "Process " + i + "can't send a request if it already hold the resource!\nCheck your blockTime!", "Error!", JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
			}
		}
		
		for(int i = 0; i < btCheck.length; i++)
		{
			if(btCheck[i] < 1)
			{
				JOptionPane.showMessageDialog(null, "The blockTime per Process has to be between 1 and " + pACheck + "!", "Error!", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		return true;
	}

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        processAmount = (Integer)primitives.get("processAmount");

        scProp = (SourceCodeProperties)props.getPropertiesByName("SourcecodeProperty");
        scText = (SourceCodeProperties)props.getPropertiesByName("TextProperty");
        tProp = (TextProperties)props.getPropertiesByName("TitleProperty");
        deviceTitle = (TextProperties)props.getPropertiesByName("DeviceTitleProperty");
        circProp = (CircleProperties)props.getPropertiesByName("CircleProperty");
        recProp = (RectProperties)props.getPropertiesByName("RectangleProperty");
        enterArrowProp = (PolylineProperties)props.getPropertiesByName("EnterArrowProperty");
        exitArrowProp = (PolylineProperties)props.getPropertiesByName("ExitArrowProperty");
        
        comments = new TextProperties();
        comments.set(AnimationPropertiesKeys.FONT_PROPERTY, scProp.get(AnimationPropertiesKeys.FONT_PROPERTY));
        
        deviceTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, arrangeFont((Font)deviceTitle.get(AnimationPropertiesKeys.FONT_PROPERTY), -1, 20));
        tProp.set(AnimationPropertiesKeys.FONT_PROPERTY, arrangeFont((Font)tProp.get(AnimationPropertiesKeys.FONT_PROPERTY), Font.BOLD, 20));
        
        for(int i : (int[])primitives.get("wantToUse"))
        	wtu.add(i);
        
        for(int i : (int[])primitives.get("blockTime"))
        	bt.add(i);
        
        proceed();

        return lang.toString();
    }
    
    private Font arrangeFont(Font font, int style, int size) {
		return new Font(font.getFamily(), style == -1 ? font.getStyle() : style, size);
	}

	public void proceed() {
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ap.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));

		rectS = lang.newRect(new Coordinates(350, 100), new Coordinates(650 + (processAmount - 2) * 200, 250), "rectS", null, recProp);
		rectS.hide();
		
		Text s = lang.newText(new Offset(-25, 20, rectS, "N"), "Server", "S", null, deviceTitle);
		s.hide();
		
		q = lang.newText(new Offset(10, 0, rectS, "W"), "Queue:", "Q", null, comments);
		q.hide();
		
		Text header = lang.newText(new Offset(-75, -100, s, "NW"), "Central Server Mutex", "header", null, tProp);

		SourceCode intro = lang.newSourceCode(new Offset(-150, 50, header, "SW"), "intro", null, scText);
		intro.addMultilineCode(
				"Description:"
				+ "\n\tThe Central Server Mutex is a possibility to share one resource with different processes."
				+ "\n\tIt is based on a queue which is managed by the server that allows only the first person in the queue"
				+ "\n\tto access the shared resource (e.g. a file)."
				+ "\n\tAfter the work with the resource is done, the next process in the queue can access the resource."
				+ "\n "
				+ "\nPrinciple:"
				+ "\n\tEvery time a request for the resource reaches the server, the server will check whether"
				+ "\n\tthe resource is free or not. If it is free the server will immediately answer the request"
				+ "\n\tand provide access the the resource. If the resource on the other hand is NOT free the server"
				+ "\n\twill not answer the request and queue it up. Later when the request reaches the first position"
				+ "\n\tin the queue and the resource will no longer be used by the previous process it will be answered"
				+ "\n\tand the resource assigned."
				+ "\n "
				+ "\nPush the 'Play'-Button to start the animation", null, null);
		
		lang.nextStep("Start Screen");
		
		intro.hide();
		
		guide = lang.newSourceCode(new Coordinates(10, 50), "guide", null, scText);
		guide.addCodeLine("To explain the principle of the", null, 0, null);
		guide.addCodeLine("Central Server Mutex we need:", null, 0, null);
		
		lang.nextStep("Server description");
		
		guide.addCodeLine("a Server", null, 1, null);
		
		server = lang.newSourceCode(new Offset(50, -100, rectS, "NE"), "server", null, scProp);
		
		server.addMultilineCode(
				"Server S"
				+ "\n "
				+ "\nqueue := empty"
				+ "\nresource := free"
				+ "\n "
				+ "\nfunction enter(Process P)"
				+ "\n\tif (resource == free)"
				+ "\n\t\tresource := P"
				+ "\n\t\treturn true"
				+ "\n\telse"
				+ "\n\t\tadd P to queue"
				+ "\n\twaitUntil(resource == P)"
				+ "\n\treturn true"
				+ "\n "
				+ "\nfunction exit(Process P)"
				+ "\n\tif (queue is empty)"
				+ "\n\t\tresource := free"
				+ "\n\telse"
				+ "\n\t\tresource := queue[0]", null, null);
		s.show();
		rectS.show();
		
		lang.nextStep();
		
		guide.addCodeLine("- with a globally available resource", null, 2, null);

		Text r = lang.newText(new Offset(10, 40, rectS, "W"), "Resource:", "R", null, comments);
		Text resUse = lang.newText(new Offset(10, 0, r, "NE"), "free", "isFree", null, comments);
		String resUseString = "free";

		lang.nextStep();
		
		guide.addCodeLine("- with a function enter()", null, 2, null);
		guide.addCodeLine("  which let us request the resource", null, 2, null);
		
		for(int i = 5; i <= 12; i++)
			server.highlight(i);
		
		lang.nextStep();
		
		guide.addCodeLine("- with a function exit()", null, 2, null);
		guide.addCodeLine("  which let us notice the", null, 2, null);
		guide.addCodeLine("  Server that we no longer", null, 2, null);
		guide.addCodeLine("  need access to the resource", null, 2, null);
		
		for(int i = 5; i <= 12; i++)
			server.unhighlight(i);
		
		for(int i = 14; i <= 18; i++)
			server.highlight(i);
		
		lang.nextStep("Process description");
		
		guide.addCodeLine("", null, 2, null);
		guide.addCodeLine("a couple of Processes", null, 1, null);
		guide.addCodeLine("- which want to access the resource", null, 2, null);
		
		for(int i = 1; i <= processAmount; i++)
		{
			Circle c = lang.newCircle(new Offset(50 + (i-1) * 200, 100, rectS, "SW"), 20, "circP" + i, null, circProp);
			Text t = lang.newText(new Offset(10, 14, c, "NW"), "P" + i, "P" + i, null, deviceTitle);
			
			SourceCode sc = lang.newSourceCode(new Offset(-25, 25, c, "SW"), "process" + i, null, scProp);
			sc.addMultilineCode(
					"Process P" + i
					+ "\nfunction requestResource()"
					+ "\n\tS.enter(P" + i + ")"
					+ "\n\tworkWithResource()"
					+ "\n\tS.exit(P" + i + ")", null, null);
			
			procs.add(sc);
			circs.add(c);
			circTex.add(t);
		}
		
		IntArray queueCounter = lang.newIntArray(new Offset(0, 50, procs.get(0), "SW"), new int[]{0}, "counterView", null);
		queueCounter.hide();

        counter = lang.newCounter(queueCounter);
        CounterProperties cp = new CounterProperties();
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
        
        lang.newText(new Offset(0, 120, guide, "SW"), "access -> to the resource", "counterText", null);
        lang.newText(new Offset(0, 135, guide, "SW"), "assignments -> amount of enqueues", "cT", null);
        lang.newCounterView(counter, new Offset(0, 160, guide, "SW"), cp, true, true);
		
		for(int i = 14; i <= 18; i++)
			server.unhighlight(i);
		
		lang.nextStep();
		
		varQueueText = lang.newText(new Offset(10, -120, server, "E"), "", "que", null, comments);
		waitUntil = lang.newText(new Offset(10, 26, server, "E"), "", "waiters", null, comments);
		
		int time = 0;
		boolean first = false;
		queue.clear();
		for(int i = 0; i < wtu.size(); i++)
		{		
			newGuide();
			
			int current = wtu.get(i);
			boolean state = queue.isEmpty() && resUseString.equals("free");
			newProc(current, state);
			
			Text enter = enters.get("P"+current);
			SourceCode proc = procs.get(current - 1);

			if(state)
			{
				counter.accessInc(1);
				resText = lang.newText(new Offset(10, -104, server, "E"), "// resource = P"+current, "res", null, comments);
				
				move(6, 7, enter, moveOneStep);		
				move(7, 8, enter, moveOneStep);
				
				server.unhighlight(8);
				resUse.setText("P"+current, null, null);
				resUseString = "P"+current;
				
				hideArrow(current);
				enter.hide();
				proc.toggleHighlight(2, 3);
				
				guide.addMultilineCode(
						"The Server returns true, so"
						+ "\nthe resource is available and"
						+ "\nP"+current+" can use it.", null, null);
				
				lang.nextStep();

				newGuide();
				guide.addMultilineCode(
						"But what happens when another Process"
						+ "\nwants to access the resource, too?"
						+ "\n ", null, null);		
				
				lang.nextStep();
				
				guide.addMultilineCode(
						"If that happens the Server will store"
						+ "\nthe incoming request in a queue"
						+ "\nand delay the response for the process."
						+ "\n ", null, null);
				
				q.show();				
				
				queue.add("");
				queueArray = lang.newStringArray(new Offset(10, -12, q, "E"), new String[]{""}, "queue", null, ap);
				
				time = bt.removeFirst();
				
				first = true;
				
				lang.nextStep();
				
				
			}
			else if(time != 0)
			{
				move(6, 9, enter, moveOneStep * 3);				
				move(9, 10, enter, moveOneStep);
				
				enqueue("P"+current);

				varQueueText.setText("// queue = "+ queue.toString(), null, null);
				
				lang.nextStep();
				
				server.toggleHighlight(10, 11);
				enter.hide();
				
				waitUntil.setText("// Process " + queue.toString() +  (queue.size() == 1 ? " is " : " are ") + "here", null, null);
				
				changeArrowColor(current, (Color) exitArrowProp.get(AnimationPropertiesKeys.COLOR_PROPERTY));

				guide.addMultilineCode(
						"P"+current+" will now wait until the resource"
						+ "\nis available"
						+ "\n ", null, null);
				
				time--;
				
				if(time == 0)
				{
					lang.nextStep();
					
					finishProcess(first);
					first = false;
					if(!wtu.isEmpty())
					{
						dequeue();
						resUseString = "P"+wtu.getFirst();
						resUse.setText(resUseString, null, null);
						
						varQueueText.setText("// queue = " + queue.toString(), null, null);
						assignResource();
						time = bt.removeFirst();
					}
					i--;
				}
				
				lang.nextStep();
			}
		}
		
		while(!wtu.isEmpty())
		{
			finishProcess(first);
			if(!wtu.isEmpty())
			{
				dequeue();
				resUseString = "P"+wtu.getFirst();
				resUse.setText(resUseString, null, null);
				
				varQueueText.setText("// queue = " + queue.toString(), null, null);
				assignResource();
				time = bt.removeFirst();
				
				lang.nextStep();
			}
		}
		
		resUseString = "free";
		resUse.setText("free", null, null);

		newGuide();
		guide.addMultilineCode(
				"Since the Server has no other requests"
				+ "\nin the queue it will wait for new requests.", null, null);
	
		lang.nextStep();
		
		lang.hideAllPrimitives();
		header.show();

		guide = lang.newSourceCode(new Offset(-150, 50, header, "SW"), "outtro", null, scText);
		guide.addMultilineCode(
				"The animation is over now. We have learned what"
				+ "\nCentral Server Mutex means and how it is handled"
				+ "\n "
				+ "\n "
				+ "\nSome further notes:"
				+ "\n\t- The performance of this principle bases on the server"
				+ "\n\tsince this is resource handling region"
				+ "\n "
				+ "\n\t- The entering of a process is a critical section, because it needs"
				+ "\n\ttwo messages (enter() + acknowledgement) to assign a process"
				+ "\n\tif for instance the acknowledgement is lost the process does not know,"
				+ "\n\tthat he can access the resource, but the server has assigned the process"
				+ "\n "
				+ "\n\t- Failure:"
				+ "\n\t\tThe central server is a single point of failure"
				+ "\n\t\tthat means, that if he fails the whole resource will no longer be available"
				+ "\n "
				+ "\n\t\tIf a client, holding the resource, fails, the resource will never be freed"
						, null, null);
		
		String[][] bla = new String[][]{
				{"Algorithm", "Msgs per entry/exit", "Msgs before entry", "Problems"},
				{"Central Server", "3", "2", "Central server crash"},
				{"Ring-Based", "1 to Inf", "0 to n-1", "Lost token, process crash"},
				{"Logical Clock", "2 * (n-1)", "2 * (n-1)", "Process crash"}
		};
		
		MatrixProperties mp = new MatrixProperties();
		mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
		
		lang.newStringMatrix(new Offset(0, 50, guide, "SW"), bla, "table", null, mp);
		
		lang.nextStep("End Screen");
	}
	
	private void newGuide()
	{
		guide.hide();
		guide = null;
		guide = lang.newSourceCode(new Coordinates(10, 50), "guide", null, scText);
	}
	
	private void newProc(int procNumber, boolean state)
	{
		if(state)
			guide.addMultilineCode(
					"Now let us assume that P"+procNumber+" wants"
					+ "\nto access the resource."
					+ "\n ", null, null);
		
		SourceCode proc = procs.get(procNumber - 1);
		
		proc.highlight(1);
		
		lang.nextStep("Process " + procNumber + " arrives");
		
		proc.toggleHighlight(1, 2);
		
		Polyline line = lang.newPolyline(
							new Offset[]{new Offset(0, 0, circs.get(procNumber - 1), "N"),
							new Offset(50 + (procNumber - 1) * 200, 0, rectS, "SW") },
							"P"+procNumber+"toS", null, enterArrowProp);
		
		Text label = lang.newText(new Offset(15, 0, line, "E"), "enter(P"+ procNumber + ")", "arrowP" + procNumber + "Label", null, comments);
		
		arrows.put(procNumber, new AbstractMap.SimpleEntry<Polyline, Text>(line, label));
		
		if(!state)
			guide.addMultilineCode(
					"P"+procNumber+" sends a request to the Server"
					+ "\nand waits for the answer."
					+ "\n ", null, null);
		
		lang.nextStep();
		
		server.highlight(5);

		enters.put("P"+procNumber, lang.newText(new Offset(10, -72, server, "E"), "// Process " + procNumber + " is here", "enterP"+procNumber, null, comments));
		
		lang.nextStep();
		
		move(5, 6, enters.get("P"+procNumber), moveOneStep);
	}
	
	private void assignResource()
	{
		int current = wtu.getFirst();
		Text enter = enters.get("P"+current);
		SourceCode proc = procs.get(current - 1);
		
		server.highlight(12);
		enter.moveBy(null, 0, moveOneStep * 2, null, null);
		enter.show();
		waitUntil.setText("// Process " + queue.toString() +  (queue.size() == 1 ? " is " : " are ") + "here", null, null);
		
		changeArrowColor(current, (Color) enterArrowProp.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		
		lang.nextStep();
		
		proc.toggleHighlight(2, 3);
		server.unhighlight(12);
		
		hideArrow(current);
		enter.hide();
		
		newGuide();
		
		guide.addMultilineCode(
				"The server answers the request of P" + current
				+ "\nwhich can now work with the resource."
				+ "\n ", null, null);
		

		proc.toggleHighlight(2, 3);
	}
	
	private void finishProcess(boolean first)
	{
		int current = wtu.removeFirst();
		Text enter = enters.get("P"+current);
		SourceCode proc = procs.get(current - 1);
		
		proc.toggleHighlight(3, 4);
		
		arrows.get(current).getValue().setText("exit(P"+current+")", null, null);
		showArrow(current);
		
		newGuide();

		guide.addMultilineCode(
				"P"+current+" has finished the work with the resource"
				+ "\nand sends a notice to the server."
				+ "\n ", null, null);
		
		lang.nextStep("Process " + current + " finishes");
		
		server.highlight(14);
		
		enter.moveBy(null, 0, moveOneStep * (first ? 6 : 2), null, null);
		enter.show();
		
		lang.nextStep();
		
		if(queue.getFirst().equals(""))
		{
			guide.addMultilineCode(
			"The Server checks if there are other"
			+ "\nrequests in the queue. If not it will"
			+ "\nset the resource to free", null, null);
			
			move(14, 15, enter, moveOneStep);
			resText.hide();
			move(15, 16, enter, moveOneStep);

			server.unhighlight(16);
		}
		else
		{			
			guide.addMultilineCode(
					"Ther server checks if there are other"
					+ "\nrequests in the queue. If so it will"
					+ "\nproceed with the next process in the queue."
					+ "\n ", null, null);
			
			move(14, 15, enter, moveOneStep);			
			move(15, 17, enter, moveOneStep * 2);
			
			resText.setText("// resource = P"+wtu.getFirst(), null, null);
			move(17, 18, enter, moveOneStep);

			server.unhighlight(18);
		}
		proc.unhighlight(4);
		
		hideArrow(current);
		enter.hide();

		enters.remove("P"+current);
	}
	
	private void move(int oldLine, int newLine, Text t, int amount)
	{
		server.toggleHighlight(oldLine, newLine);
		t.moveBy(null, 0, amount, null, null);
		lang.nextStep();
	}
	
	private void changeArrowColor(int number, Color color)
	{
		arrows.get(number).getKey().changeColor(null, color, null, null);
		arrows.get(number).getValue().changeColor(null, color, null, null);
	}
	
	private void hideArrow(int number)
	{
		arrows.get(number).getKey().hide();
		arrows.get(number).getValue().hide();
	}
	
	private void showArrow(int number)
	{
		arrows.get(number).getKey().show();
		arrows.get(number).getValue().show();
	}
	
	private void enqueue(String item)
	{			
		if(queue.getFirst().equals(""))
			queue.set(0, item);
		else
			queue.add(item);
		counter.assignmentsInc(1);
		newQueue();
	}
	
	private void dequeue()
	{
		queue.remove();
		
		if(queue.isEmpty())
		{
			queue.addFirst("");
			server.unhighlight(11);
			waitUntil.hide();
			varQueueText.hide();
		}
		counter.accessInc(1);
		newQueue();
	}
	
	private void newQueue() {
		queueArray.hide();
		queueArray = null;
		queueArray = lang.newStringArray(new Offset(10, -12, q, "E"), queue.toArray(new String[queue.size()]), "queue", null, ap);
	}

    public String getName() {
        return "Central Server Mutex";
    }

    public String getAlgorithmName() {
        return "Mutex";
    }

    public String getAnimationAuthor() {
        return "Martin Möller";
    }

    public String getDescription(){
        return "The Central Server Mutex is a possibility to share one resource with different processes."
 +"\n"
 +"It is based on a queue which is managed by the server that allows only the first person in the queue"
 +"\n"
 +"to access the shared resource (e.g. a file).\""
 +"\n"
 +"After the work with the resource is done, the next process in the queue can access the resource.";
    }

    public String getCodeExample(){
        return "You have the following Variables to adjust the propgrams behavior:"
 +"\n"
 +"	wantToUse: all processes (in order) which want to use the resource"
 +"\n"
 +"	blockTime: the time (in order) that the current resource needs to finish his work with"
 +"\n"
 +"		the resource. The time itself represents the amount of new requests."
 +"\n"
 +"		e.g. your time is 2 then the process will hold the resource until 2 new"
 +"\n"
 +"		requests of other processes arrive or no other process wants to use"
 +"\n"
 +"		the resource (wantToUse is finished)";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
}