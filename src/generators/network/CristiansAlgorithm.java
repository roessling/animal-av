/*
 * CristiansAlgorithm.java
 * Martin Müller, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class CristiansAlgorithm implements ValidatingGenerator {
    private Language lang;
    
	private String t0Time;
	private int m0Time, m1Time;
	private int processingTime;
	private String tTime;
	private int tPos = 1;
	
	private Rect right;
	private SourceCode guide;
	private SourceCodeProperties scText = new SourceCodeProperties();
	private TextProperties tProp = new TextProperties(), deviceTitle = new TextProperties(), labels = new TextProperties();
	private RectProperties recProp = new RectProperties();
	private PolylineProperties pProp = new PolylineProperties();

    public void init(){
        lang = new AnimalScript("Cristian's Algorithm", "Martin Müller", 800, 600);
        lang.setStepMode(true);
    }
    
    @Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
    	String t0Check = (String)primitives.get("t0");
    	int m0Check = (Integer)primitives.get("requestDuration");
    	int m1Check = (Integer)primitives.get("responseDuration");
    	int pTCheck = (Integer)primitives.get("processingTime");
		
    	try
    	{
    		calcTime(t0Check, 0);
    	}
    	catch(ParseException pe)
    	{
    		JOptionPane.showMessageDialog(null, "The time format for t0 has to be HH:mm:ss.SSS!", "Error!", JOptionPane.ERROR_MESSAGE);
    		return false;
    	}
    	
    	if(m0Check < 0 || m1Check < 0 || pTCheck < 0)
		{
			JOptionPane.showMessageDialog(null, "All timings have to be equals to or biggen than zero!", "Error!", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	t0Time = (String)primitives.get("t0");
    	m0Time = (Integer)primitives.get("requestDuration");
    	m1Time = (Integer)primitives.get("responseDuration");
    	processingTime = (Integer)primitives.get("processingTime");
    	
    	scText = (SourceCodeProperties)props.getPropertiesByName("TextProperty");
        tProp = (TextProperties)props.getPropertiesByName("TitleProperty");
        deviceTitle = (TextProperties)props.getPropertiesByName("DeviceTitleProperty");
        labels = (TextProperties)props.getPropertiesByName("LabelProperty");
        recProp = (RectProperties)props.getPropertiesByName("RectangleProperty");
        pProp = (PolylineProperties)props.getPropertiesByName("ArrowProperty");
        
        tProp.set(AnimationPropertiesKeys.FONT_PROPERTY, arrangeFont((Font)tProp.get(AnimationPropertiesKeys.FONT_PROPERTY), Font.BOLD, 20));
        
    	try {
			proceed();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
        return lang.toString();
    }
    
    private Font arrangeFont(Font font, int style, int size) {
		return new Font(font.getFamily(), style == -1 ? font.getStyle() : style, size);
	}
    
	public void proceed() throws ParseException {		
		Text header = lang.newText(new Coordinates(250, 50), "Cristian's Algorithm", "header", null, tProp);

		SourceCode intro = lang.newSourceCode(new Offset(0, 50, header, "SW"), "intro", null, scText);
		intro.addMultilineCode(
				"This animation will teach the principle of Cristian's Algorithm and"
				+ "\nthe clock synchronization behind."
				+ "\nPush the 'Play'-Button to start the animation", null, null);
		
		lang.nextStep("Start Screen");
		
		intro.hide();

		Text p = lang.newText(new Offset(-50, 20, header, "SW"), "P", "p", null, deviceTitle);
		Text s = lang.newText(new Offset(50, 20, header, "SW"), "S", "s", null, deviceTitle);

		Rect left = lang.newRect(new Offset(-1, 0, p, "S"), new Offset(1, 140, p, "S"), "left", null, recProp);
		right = lang.newRect(new Offset(-1, 0, s, "S"), new Offset(1, 140, s, "S"), "right", null, recProp);
		
		guide = lang.newSourceCode(new Offset(120, 10, right, "NE"), "guide", null, scText);

		guide.addMultilineCode(
				"At first we have a device P like a computer which wants to"
				+ "\nsynchronize it's time with a time server S", null, null);
		
		lang.nextStep();
		
		lang.newText(new Offset(-15, 10, left, "NW"), "t0", "t0", null, labels);
		lang.newText(new Offset(-15, 20, left, "SW"), "t0 : " + t0Time, "t0", null, labels);
		
		newGuide();
		guide.addMultilineCode(
				"At the time t0 P sends a synchronize"
				+ "\nrequest to the server S", null, null);
		
		lang.nextStep();

		Polyline l1 = lang.newPolyline(new Offset[]{new Offset(1, 20, left, "N"), new Offset(1, 40, right, "N") }, "l1", null, pProp);
		lang.newText(new Offset(-10, -20, l1, "N"), m0Time + "ms", "m0", null, labels);
		
		lang.nextStep("Time Request");
		
		Polyline reqArr = 	lang.newPolyline(new Offset[]{new Offset(-5, 40, right, "N"), new Offset(5, 40, right, "N") }, "reqArr", null);
		Polyline tl 	=	lang.newPolyline(new Offset[]{new Offset(-5, 60, right, "N"), new Offset(5, 60, right, "N") }, "tl", null);
		Polyline sendAns=	lang.newPolyline(new Offset[]{new Offset(-5, 80, right, "N"), new Offset(5, 80, right, "N") }, "sendAns", null);
		
		Polyline pl;
		switch(tPos)
		{
			case 0: 
				tTime = calcTime(t0Time, m0Time);
				pl = reqArr; break;
			case 2: 
				tTime = calcTime(t0Time, m0Time + processingTime);
				pl = sendAns; break;
			default:
				tTime = calcTime(t0Time, m0Time + processingTime / 2);
				pl = tl; break;
		}
		lang.newText(new Offset(8, -8, pl, "E"), "t", "t", null, labels);
		
		Polyline bracketR = lang.newPolyline(new Offset[]{
								new Offset(15, 0, reqArr, "E"),
								new Offset(30, 0, reqArr, "E"),
								new Offset(30, 0, sendAns, "E"),
								new Offset(15, 0, sendAns, "E")
								}, "tl", null, pProp);
		
		lang.newText(new Offset(8, -8, bracketR, "E"), "I : " + processingTime + "ms", "I", null, labels);
		lang.newText(new Offset(-15, 35, left, "SW"), "t   : " + tTime, "tTime", null, labels);
		
		newGuide();
		guide.addMultilineCode(
				"The Server processes the Request in time I"
				+ "\nand prepares an answer with time t", null, null);
		
		lang.nextStep("Processing");
		
		newGuide();
		guide.addMultilineCode(
				"The Server sends a the Response with his answer"
				+ "\nwhich arrives P at time t1", null, null);
		
		Polyline l2 = lang.newPolyline(new Offset[]{new Offset(1, 80, right, "N"), new Offset(1, 95, left, "N") }, "l2", null, pProp);
		lang.newText(new Offset(-10, 10, l2, "S"), m1Time + "ms", "m1", null, labels);

		String t1Time = calcTime(t0Time, m0Time + processingTime + m1Time);
		
		lang.newText(new Offset(-15, 90, left, "N"), "t1", "t1", null, labels);
		lang.newText(new Offset(-15, 50, left, "SW"), "t1 : " + t1Time, "t0", null, labels);
		
		lang.nextStep("Time Response");
		
		Polyline bracket = lang.newPolyline(new Offset[]{
								new Offset(-20, 20, left, "N"),
								new Offset(-30, 20, left, "N"),
								new Offset(-30, 100, left, "N"),
								new Offset(-15, 100, left, "N")
								}, "tl", null, pProp);
		
		lang.newText(new Offset(-100, -20, bracket, "W"), "Roundtrip-time", "rtt", null, labels);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = sdf.parse(t0Time);
        Date date2 = sdf.parse(t1Time);
             
        String rttTime = String.valueOf(date2.getTime() - date.getTime());

		lang.newText(new Offset(-15, 65, left, "SW"), "RTT : " + rttTime + "ms", "rtt", null, labels);
		lang.newText(new Offset(-15, 80, left, "SW"), "RTT / 2 : " + Integer.parseInt(rttTime) / 2 + "ms", "rtt", null, labels);
		
		lang.nextStep("RTT");
		
		newGuide();
		guide.addMultilineCode(
				"P now takes t from the Server and adds"
				+ "\nRTT/2 to it for it's new time", null, null);
		
		String newTimeTime = calcTime(t1Time, -Integer.parseInt(rttTime) / 2);
		
		lang.newText(new Offset(-40, 105, left, "SW"), "Time set to : " + newTimeTime + "ms", "newTime", null, labels);
		
		date = sdf.parse(newTimeTime);
		date2 = sdf.parse(tTime);
		
		if(date.compareTo(date2) < 0)
			lang.newText(new Offset(-40, 135, left, "SW"), "So the sync wasn't correct, since P is to early!", "analysis", null, labels);
		else if(date.compareTo(date2) == 0)
			lang.newText(new Offset(-40, 135, left, "SW"), "Time synchronization successful!", "analysis", null, labels);
		else
			lang.newText(new Offset(-40, 135, left, "SW"), "So the sync wasn't correct, since P is to late!", "analysis", null, labels);

		lang.nextStep("Set Time");

		lang.hideAllPrimitives();
		header.show();

		guide = lang.newSourceCode(new Offset(0, 50, header, "SW"), "outtro", null, scText);
		guide.addMultilineCode(
				"The animation is over now. We have learned how a theoretical"
				+ "\ntime synchronization via Cristian's Algorithm works"
				+ "\n "
				+ "\n "
				+ "\nSome further notes:"
				+ "\n\t- The algorithm is reasonably accurate:"
				+ "\n\t\t-> Let 'min' be the minimum time to transmit a message one-way."
				+ "\n\t\t-> The earliest time that S can have replied is t0 + min."
				+ "\n\t\t-> The latest time that S can have replied is t0 + RTT - min"
				+ "\n\t\t-> So the time range for an answer is RTT - 2 * min"
				+ "\n\t\t-> Therefor the accurancy is +- (RTT / 2 - min)"
				+ "\n "
				+ "\n\t- Problems:"
				+ "\n\t\tCristian's Algorithm is only suitable for deterministic LAN evironments or Intranet"
				+ "\n\t\tWhat if S fails?"
				+ "\n\t\t-> You need redundancy through group of servers and multicast requests"
				+ "\n\t\t-> How to decide what time is correct if the replies vary? (see byzantine agreement problems)"
				+ "\n "
				+ "\n\t\tIt is required that there is a reliable cummunication between the processes"
						, null, null);
		
		lang.nextStep("End Screen");
	}
	
	private void newGuide() {
		guide.hide();
		guide = null;
		guide = lang.newSourceCode(new Offset(120, 10, right, "NE"), "guide", null, scText);
	}
	
	private String calcTime(String time, int addMS) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
		Date d = df.parse(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MILLISECOND, addMS);
		return df.format(cal.getTime());
	}

    public String getName() {
        return "Cristian's Algorithm";
    }

    public String getAlgorithmName() {
        return "Cristian's Algorithm";
    }

    public String getAnimationAuthor() {
        return "Martin Müller";
    }

    public String getDescription(){
        return "Cristian's Algorithm is a method for clock synchronisation and is primarily used in low-latency intranets."
 +"\n"
 +"The algorithm only achieves synchronisation if the round-trip time (RTT) of the request is short"
 +"\n"
 +"compared to required accuracy.";
    }

    public String getCodeExample(){
        return "Structure:"
 +"\n"
 +"	We have a process P and a time server S (UTC)"
 +"\n"
 +"Principle:"
 +"\n"
 +"	1.) P sends a time request to S at t0"
 +"\n"
 +"	2.) S receives the request and needs I to respond the time T"
 +"\n"
 +"	3.) P receives the response at t1"
 +"\n"
 +"	4.) P sets his time to T + RTT / 2, where RTT/2 is the return time of the initial time request"
 +"\n"
 +"\n"
 +"	";
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