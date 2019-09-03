/*
 * RingBasedMutex.java
 * Martin Möller, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;

public class RingBasedMutex implements ValidatingGenerator {
    private Language lang;
    private int processAmount;
    private int startProcess;

	private Graph ring;
	private Circle token;
	private Text t;
	private int tokenPosition;
	private int radius, guideX, guideY, scX, scY;
	private String scD;
	private SourceCode guide, sc;
	private LinkedList<Integer> wtu = new LinkedList<Integer>();
	private LinkedList<Coordinates> positions = new LinkedList<Coordinates>();
	
	private TextProperties tProp = new TextProperties(), deviceTitle = new TextProperties();
	private SourceCodeProperties scProp = new SourceCodeProperties(), scText = new SourceCodeProperties();
	private CircleProperties circProp = new CircleProperties();
	private GraphProperties gProp;
	private TwoValueCounter counter;

    public void init(){
        lang = new AnimalScript("Mutex", "Martin Möller", 800, 600);
		lang.setStepMode(true);
    }
    
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		int pACheck = (Integer)primitives.get("processAmount");
		int startCheck = (Integer)primitives.get("startProcess");
		int[] wtuCheck = (int[])primitives.get("wantToUse");
		
		if(pACheck < 2)
		{
			JOptionPane.showMessageDialog(null, "You need at least 2 processes!", "Error!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		for (int i = 0; i < wtuCheck.length; i++)
		{
			if(wtuCheck[i] < 1 || wtuCheck[i] > pACheck)
			{
				JOptionPane.showMessageDialog(null, "Only Processes between 1 and " + pACheck + " can use the resource!", "Error!", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		if(startCheck < 1 || startCheck > pACheck)
		{
			JOptionPane.showMessageDialog(null, "Please select a startprocess between 1 and " + pACheck + "!", "Error!", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        processAmount = (Integer)primitives.get("processAmount");
        startProcess = (Integer)primitives.get("startProcess");
        
        scProp = (SourceCodeProperties)props.getPropertiesByName("SourcecodeProperty");
        scText = (SourceCodeProperties)props.getPropertiesByName("TextProperty");
        tProp = (TextProperties)props.getPropertiesByName("TitleProperty");
        gProp = (GraphProperties)props.getPropertiesByName("GraphProperty");
        deviceTitle = (TextProperties)props.getPropertiesByName("T-Property");
        circProp = (CircleProperties)props.getPropertiesByName("TokenProperty");
        
        deviceTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, arrangeFont((Font)deviceTitle.get(AnimationPropertiesKeys.FONT_PROPERTY), -1, 20));
        tProp.set(AnimationPropertiesKeys.FONT_PROPERTY, arrangeFont((Font)tProp.get(AnimationPropertiesKeys.FONT_PROPERTY), Font.BOLD, 20));
        
		for(int i : (int[])primitives.get("wantToUse"))
			wtu.add(i);		
		
		proceed();
        
        return lang.toString();
    }
    
    private Font arrangeFont(Font font, int style, int size) {
		return new Font(font.getFamily(), style == -1 ? font.getStyle() : style, size);
	}
	
	 public void proceed() {
		int[][] edges = new int[processAmount][processAmount];
		Coordinates[] nodes = new Coordinates[processAmount];
		String[] labels = new String[processAmount];
		radius = 15 * processAmount;
		
		positions.clear();
		for(int i = 0; i < processAmount; i++)
		{
			for(int j = 0; j < processAmount; j++)
				edges[i][j] = Math.abs(i - j) == 1 ? 1 : 0;
			
			edges[0][processAmount - 1] = 1;
			edges[processAmount - 1][0] = 1;
			
			int x = (int)(350 + radius * (1 + Math.cos(-Math.PI / 2 + i * (2 * Math.PI) / processAmount)));
			int y = (int)(100 + radius * (1 + Math.sin(-Math.PI / 2 + i * (2 * Math.PI) / processAmount)));
			
			nodes[i] = new Coordinates(x, y);
			labels[i] = "P" + (i + 1);
			positions.add(calcTokenCoords(i));
		}
		
		ring = lang.newGraph("ring", edges, nodes, labels, null, gProp);
		ring.hide();
		
		Text header = lang.newText(new Offset(-88, -70, ring, "N"), "Ring-Based Mutex", "header", null, tProp);
		
		IntArray tokCount = lang.newIntArray(new Offset(0, 50, ring, "SW"), new int[]{0}, "counterView", null);
		tokCount.hide();
		
		SourceCode intro = lang.newSourceCode(new Offset(-150, 50, header, "SW"), "intro", null, scText);
		intro.addMultilineCode(
				"Description:"
				+ "\n\tThe Ring-Based Mutex is a possibility to share one resource with different processes."
				+ "\n\tIt is based on processes which are arranged in a unidirectional ring. Whichever process hold the token"
				+ "\n\thas access to access the shared resource (e.g. a file)."
				+ "\n\tAfter the work with the resource is done, the process will pass the token to the next one."
				+ "\n "
				+ "\nPrinciple:"
				+ "\n\tSince the processes are arranged in a ring, every process P_i has a connection to process"
				+ "\n\tP_i+1 mod N where N is the amount of processes. This connection is a logical connection and not"
				+ "\n\tnecessarily a physical link."
				+ "\n\tOne process, let's assume it's P1, holds the token and has access to the resource. After P1 has"
				+ "\n\tfinished his work it will pass the token to P2. P2 does not need access to the resource so it"
				+ "\n\twill pass it to P3. P3 accesses the resource and works with it until he is done and passes the token to P4 and so on."
				+ "\n "
				+ "\nPush the 'Play'-Button to start the animation", null, null);
		
		lang.nextStep("Start Screen");
		
		intro.hide();
		
		ring.show();

		guideX = radius - 65;
		guideY = -30;
		scX = 100;
		scY = -90;
		scD = "E";
		
		if(processAmount < 8) {
			guideX = -250;
		}			
		else if(processAmount > 12) {
			guideY = -100;
			scX = guideX;
			scY = -10;
			scD = "W";
		}
			
		guide = lang.newSourceCode(new Offset(guideX, guideY, ring, "W"), "guide", null, scText);

		guide.addMultilineCode(
				"Here we have the basic"
				+ "\nstructure of Ring-Based Mutex", null, null);
		
		lang.nextStep("Basic Structure");

		createTokenAt(startProcess - 1);
		
		newGuide(guideX, guideY);
		
		guide.addMultilineCode(
				"At the beginning"
				+ "\nProcess " + startProcess + " has the Token T", null, null);
		
		scFor(startProcess);
		
		sc.highlight(8);
		
		lang.nextStep("Process " + startProcess + "holds the token");
		
        counter = lang.newCounter(tokCount);
        CounterProperties cp = new CounterProperties();
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
        
        lang.newText(new Coordinates(10, 10), "access -> to the resource", "counterText", null);
        lang.newText(new Coordinates(10, 25), "assignments -> amount of enqueues", "cT", null);
        lang.newCounterView(counter, new Coordinates(10, 50), cp, true, true);
		
		moveToUser();
		
		lang.nextStep();

		lang.hideAllPrimitives();
		header.show();

		guide = lang.newSourceCode(new Offset(-150, 40, header, "SW"), "outtro", null, scText);
		guide.addMultilineCode(
				"The animation is over now. We have learned what"
				+ "\nRing-Based Mutex means and how it is handled"
				+ "\n "
				+ "\n "
				+ "\nSome further notes:"
				+ "\n\t- The efficiency is high if there is high usage of the resource."
				+ "\n\t  On the other side it has high overhead at low usage."
				+ "\n "
				+ "\n\t- Failure:"
				+ "\n\t\tIf one of the processes fails the ring gets lost! So the token can no longer passed."
				+ "\n "
				+ "\n\t\tIt is required that there is a reliable cummunication between the processes"
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
    
    private void moveToUser()
    {
    	if(wtu.isEmpty())
    		return;
    	
    	boolean needPass = true;
    	
    	do
		{
    		if(needPass)
    		{
	    		newGuide(guideX, guideY);
	    		
	    		guide.addMultilineCode(
	    				"Now Process " + (tokenPosition + 1) + " passes"
	    				+ "\nthe Token to the next Process (P" + (tokenPosition + 2) + ")", null, null);
	    		
	    		sc.toggleHighlight(8,  9);
	    		
	    		lang.nextStep();
    		}
    		
    		scFor(tokenPosition + 2);
    		
    		sc.highlight(6);
    		
    		lang.nextStep();
			moveToken();
			newGuide(guideX, guideY - 15);
			
			counter.assignmentsInc(1);
			guide.addMultilineCode("Process " + (tokenPosition + 1) + " gets the token.\n", null, null);
			sc.toggleHighlight(6, 7);
			
			lang.nextStep();
			
			if(!wtu.contains(tokenPosition + 1))
			{
				guide.addMultilineCode(
						"Since process " + (tokenPosition + 1) + " does not need"
						+ "\naccess to the resource it sends"
						+ "\nthe token to the next process P" + (tokenPosition + 2) + ".", null, null);
				sc.toggleHighlight(7, 9);
				
				needPass = false;
			}	
			else
			{
				counter.accessInc(1);
				guide.addMultilineCode(
						"The process wants to use the"
						+ "\nresource, so it will keep the"
						+ "\ntoken until his work ist done.", null, null);
				sc.toggleHighlight(7, 8);
				wtu.remove(Integer.valueOf(tokenPosition + 1));
				
				if(wtu.isEmpty())
					return;
				
				needPass = true;
			}
			lang.nextStep("Process " + (tokenPosition + 1) + " holds the token");
		} while(true);
    }
	
	private void createTokenAt(int nodeNumber)
	{
		token = lang.newCircle(calcTokenCoords(nodeNumber), 10, "token", null, circProp);
		t = lang.newText(new Offset(4, 5, token, "NW"), "T", "T", null, deviceTitle);
		tokenPosition = nodeNumber;
	}
	
	private void moveToken()
	{
		Coordinates oldCoor = positions.get(tokenPosition);
		tokenPosition++;
		
		if(tokenPosition == positions.size())
			tokenPosition = 0;

		Coordinates newCoor = positions.get(tokenPosition);
		
		Timing time = new Timing(200) {
			
			@Override
			public String getUnit() {
				return "ticks";
			}
		};
		
		token.moveBy(null, newCoor.getX() - oldCoor.getX(), newCoor.getY() - oldCoor.getY(), null, time);
		t.moveBy(null, newCoor.getX() - oldCoor.getX(), newCoor.getY() - oldCoor.getY(), null, time);
	}
	
	private Coordinates calcTokenCoords(int nodeNumber)
	{
		int x = (int)(358 + radius + ((radius + 40) * Math.cos(-Math.PI / 2 + nodeNumber * (2 * Math.PI) / processAmount)));
		int y = (int)(108 + radius + ((radius + 40) * Math.sin(-Math.PI / 2 + nodeNumber * (2 * Math.PI) / processAmount)));
		
		return new Coordinates(x, y);
	}
	
	private void newGuide(int x, int y) {
		guide.hide();
		guide = null;
		guide = lang.newSourceCode(new Offset(x, y, ring, "W"), "guide", null, scText);
	}
	
	private void scFor(int number) {
		if(sc != null) {
			sc.hide();
			sc = null;
		}

		sc = lang.newSourceCode(new Offset(scX, scY, ring, scD), "process", null, scProp);
		sc.addMultilineCode(
				"Process P" + number
				+ "\n "
				+ "\nwantsAccess := " + wtu.contains(number)
				+ "\n "
				+ "\nfunction work()"
				+ "\n\twhile (true)"
				+ "\n\t\twaitForTokenIfHasNot()"
				+ "\n\t\tif (wantsAccess)"
				+ "\n\t\t\tworkWithResource()"
				+ "\n\t\tpassToken()", null, null);
	}

    public String getName() {
        return "Ring-Based Mutex";
    }

    public String getAlgorithmName() {
        return "Mutex";
    }

    public String getAnimationAuthor() {
        return "Martin Möller";
    }

    public String getDescription(){
        return "The Ring-Based Mutex is a possibility to share one resource with different processes."
 +"\n"
 +"It is based on a Token that allows the holder to acces the shared resource (e.g. a file)."
 +"\n"
 +"After the work with the resource is done, the token is passed to the next process.";
    }

    public String getCodeExample(){
        return "The processes are arranged in a \"ring\", so that every process P_i has a connection to process"
 +"\n"
 +"P_i+1 mod N where N is the amount of processes. One process, let's assume it's P1, holds the token"
 +"\n"
 +"and has access to the resource. After P1 has finished his work it will pass the token to P2. P2 does"
 +"\n"
 +"not need access to the resource so it will pass it to P3. P3 accesses the resource and works with it,"
 +"\n"
 +"until he is done and passes the token to P4 and so on.";
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