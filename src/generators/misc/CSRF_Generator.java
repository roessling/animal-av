/*
 * CSRF_Generator.java
 * Alexander Müller, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.CSRF_Generator;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class CSRF_Generator implements Generator {
    private Language lang;
    private PolylineProperties Pfeile;
    private CircleProperties Nutzer;
    private CircleProperties Angreifer;
    private String Domain;
    
    private int currentStep = 0;

    static int LEFT_OFFSET = 40;
    static int TOP_OFFSET = 100;
    static int HEAD_RADIUS = 25;
    static int BODY_RADIUS = 40;
    static int SITE_WIDTH = 70;
    

    private List<Primitive> graphPrimitives;
    private TextProperties textProps;
    Coordinates userCoor = new Coordinates(
    		LEFT_OFFSET + BODY_RADIUS, 
    		TOP_OFFSET + BODY_RADIUS + 140);
    Coordinates attackerCoor = new Coordinates(
    		LEFT_OFFSET + 250 + BODY_RADIUS, 
    		TOP_OFFSET + BODY_RADIUS + 285);
    Coordinates siteCoor = new Coordinates(
    		LEFT_OFFSET + 250 + BODY_RADIUS,
    		TOP_OFFSET);

    public void init(){
        lang = new AnimalScript("Cross-Site-Request-Forgery", "Alexander Müller", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(
       		 Language.INTERACTION_TYPE_AVINTERACTION);

        graphPrimitives = new ArrayList<Primitive>();
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        if(props == null || primitives == null){
        	Pfeile = new PolylineProperties();
        	Pfeile.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        	Pfeile.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
            Nutzer = new CircleProperties();
            Nutzer.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
            Nutzer.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
            Angreifer = new CircleProperties();
            Angreifer.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
            Angreifer.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
            Domain = "www.bank.com";
        } else{
        	Pfeile = (PolylineProperties)props.getPropertiesByName("Pfeile");
        	Nutzer = (CircleProperties)props.getPropertiesByName("Nutzer");
        	Angreifer = (CircleProperties)props.getPropertiesByName("Angreifer");
        	Domain = (String)primitives.get("Domain");
        }
        Nutzer.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
        		Nutzer.get(AnimationPropertiesKeys.FILL_PROPERTY));
        Angreifer.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
        		Angreifer.get(AnimationPropertiesKeys.FILL_PROPERTY));
        
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.PLAIN, 16));
        
        generateHeader();
    	lang.nextStep("Einleitung");
    	generateDiscription();
    	lang.nextStep("Beispiel");
    	lang.hideAllPrimitivesExcept(graphPrimitives);
    	generateGraph();
    	executeAlgo();
    	lang.nextStep("Gegenmaßnahmen");
    	graphPrimitives.clear();
    	generateHeader();
    	generateGraph();
    	showCountermeasures();
    	lang.nextStep("Fazit");
    	lang.hideAllPrimitives();
    	generateConclusion();
        
        lang.finalizeGeneration();
        return lang.toString();
    }
    
    private void generateHeader() {
    	// show the header with a heading surrounded by a rectangle
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 24));
        graphPrimitives.add(
        		lang.newText(new Coordinates(80, 30), getAlgorithmName(),
        				"header", null, headerProps));
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        graphPrimitives.add(
        		lang.newRect(new Offset(-5, -5, "header",
        				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
        				null, rectProps));
    }

    private void generateDiscription() {
    	// setup the start page with the description
        lang.newText(new Coordinates(30, 100),
        		"Cross-Site-Request-Forgery oder Session-Riding bezeichnet einen ",
        		"description1", null, textProps);
        lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
        		"Angriff auf den Nutzer einer Webanwendung, bei dem der Browser des ",
        		"description2", null, textProps);
        lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
        		"Nutzers ausgenutzt wird, um eine Transaktion in der Webanwendung ",
        		"description3", null, textProps);
        lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
        		"durchzuführen. Dazu muss das Opfer bei der Webanwendung angemeldet ",
        		"description4", null, textProps);
        lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
        		"sein und eine Webseite oder E-Mail mit Schadcode öffnen. Beim öffnen ",
        		"description5", null, textProps);
        lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
        		"der Webseite/E-Mail wird der Code auf dem Gerät des Opfers ausgeführt ",
        		"description6", null, textProps);
        lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
                "und dabei die Transaktion durchgeführt.",
                "description7", null, textProps);

        // generate algorithm explanation in steps
        lang.nextStep("Beschreibung");
        lang.newText(new Offset(0, 50, "description7", AnimalScript.DIRECTION_NW),
        		"1. Der Nutzer loggt sich auf einer vertrauenswürdigen Webseite ein.",
        		"algo11", null, textProps);

        lang.nextStep();
        lang.newText(new Offset(0, 25, "algo11", AnimalScript.DIRECTION_NW),
        		"2. Authentifizierung des Nutzers durch die Webseite.",
        		"algo21", null, textProps);

        lang.nextStep();
        lang.newText(
                new Offset(0, 25, "algo21", AnimalScript.DIRECTION_NW),
                "3. Der Angreifer sendet eine E-Mail oder einen Link mit ",
                "algo31", null, textProps);
        lang.newText(
                new Offset(25, 25, "algo31", AnimalScript.DIRECTION_NW),
                "Schadcode.",
                "algo32", null, textProps);
        
        lang.nextStep();
        lang.newText(
                new Offset(-25, 25, "algo32", AnimalScript.DIRECTION_NW),
                "4. Der Nutzer öffnet die Mail/den Link.",
                "algo41", null, textProps);
        
        lang.nextStep();
        lang.newText(
                new Offset(0, 25, "algo41", AnimalScript.DIRECTION_NW),
                "5. Der Browser des Nutzers führt den Code aus und startet die ",
                "algo51", null, textProps);
        lang.newText(
                new Offset(25, 25, "algo51", AnimalScript.DIRECTION_NW),
                "Transaktion mit der ursprünglichen Webseite.",
                "algo52", null, textProps);
    }
    
    private void generateGraph() {
    	TextProperties nodeTextProps = new TextProperties();
    	nodeTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));
    	nodeTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    	nodeTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    	
    	RectProperties rectProps = new RectProperties();
    	rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    	rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
    	
    	//draw user icon
    	Nutzer.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
    	graphPrimitives.add(
    			lang.newCircle(userCoor, HEAD_RADIUS, "UserHead", null, Nutzer));
    	graphPrimitives.add(
    			lang.newCircle(
    					new Coordinates(userCoor.getX(), userCoor.getY() + 2 * HEAD_RADIUS), 
    					(int)(BODY_RADIUS), "UserBody", null, Nutzer));
    	graphPrimitives.add(
    			lang.newRect(new Coordinates(userCoor.getX() - BODY_RADIUS, 
    					userCoor.getY() + 2 * HEAD_RADIUS), 
    					new Coordinates(userCoor.getX() + BODY_RADIUS, 
    	    					userCoor.getY() + 2 * HEAD_RADIUS + BODY_RADIUS), 
    					"userRect", null, rectProps));
    	graphPrimitives.add(
    			lang.newText(new Coordinates(userCoor.getX(),
    					userCoor.getY() + 2 * HEAD_RADIUS), 
    					"Nutzer", "userText", null, nodeTextProps));
    	/* Code obsolet
    	CircleSegProperties userSegProps = new CircleSegProperties();
    	userSegProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Nutzer.get(AnimationPropertiesKeys.COLOR_PROPERTY));
        userSegProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Nutzer.get(AnimationPropertiesKeys.FILLED_PROPERTY));
        userSegProps.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 90);
        userSegProps.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 180);
    	graphPrimitives.add(
    			lang.newCircleSeg(
    					new Coordinates(userCoor.getX(), userCoor.getY() + 2 * HEAD_RADIUS), 
    					(int)(BODY_RADIUS * 1.5), "UserBody", null, userSegProps));
    	*/
    	
    	//draw attacker icon
    	Angreifer.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
    	graphPrimitives.add(
    			lang.newCircle(attackerCoor, HEAD_RADIUS, "AttackerHead", null, Angreifer));
    	graphPrimitives.add(
    			lang.newCircle(
    					new Coordinates(attackerCoor.getX(), attackerCoor.getY() + 2 * HEAD_RADIUS), 
    					(int)(BODY_RADIUS), "AttackerBody", null, Angreifer));
    	graphPrimitives.add(
    			lang.newRect(new Coordinates(attackerCoor.getX() - BODY_RADIUS, 
    					attackerCoor.getY() + 2 * HEAD_RADIUS), 
    					new Coordinates(attackerCoor.getX() + BODY_RADIUS, 
    							attackerCoor.getY() + 2 * HEAD_RADIUS + BODY_RADIUS), 
    					"attackerRect", null, rectProps));
    	graphPrimitives.add(
    			lang.newText(new Coordinates(attackerCoor.getX(),
    					attackerCoor.getY() + 2 * HEAD_RADIUS), 
    					"Angreifer", "attackerText", null, nodeTextProps));
    	
    	//draw website icon
    	rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    	graphPrimitives.add(
    			lang.newRect(new Coordinates(this.siteCoor.getX() - SITE_WIDTH / 2,
    							this.siteCoor.getY()),
    					new Coordinates(this.siteCoor.getX() + SITE_WIDTH / 2,
    							this.siteCoor.getY() + (int)(SITE_WIDTH * 1.5)), 
    					"siteRect", null, rectProps));
    	graphPrimitives.add(
    			lang.newText(siteCoor, "HTML", "htmlText", null, nodeTextProps));
    	graphPrimitives.add(
    			lang.newText(new Coordinates(
    					this.siteCoor.getX(),
    					this.siteCoor.getY() + (int)(SITE_WIDTH * 1.5)), 
    					"Webseite", "siteText", null, nodeTextProps));
    }
    
    private void executeAlgo() {
    	PolylineProperties lineProps = Pfeile;
    	TextProperties arrowTextProps = new TextProperties();
    	arrowTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));
    	arrowTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    	arrowTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    	
    	//coordinates for arrows
    	Coordinates user = algoanim.util.Node.convertToNode(
    			new Point(this.userCoor.getX() + BODY_RADIUS, 
    					this.userCoor.getY() + HEAD_RADIUS));
    	Coordinates site = algoanim.util.Node.convertToNode(
    			new Point(this.siteCoor.getX() - BODY_RADIUS, this.siteCoor.getY() + SITE_WIDTH));
        Coordinates attacker = algoanim.util.Node.convertToNode(
    			new Point(this.attackerCoor.getX() - BODY_RADIUS, 
    					this.attackerCoor.getY() + HEAD_RADIUS));
    	
    	// generate algorithm steps with visualization
    	//draw step 1
        currentStep = 1;
        updateGraphSteps();
        lang.newPolyline(new Coordinates[] {user, site}, "loginArrow",  null, lineProps);
    	Coordinates textCoor = new Coordinates( 
    			( user.getX()+site.getX() ) / 2, 
    			( user.getY()+site.getY() ) / 2);
    	lang.newText(textCoor, "Login", "loginText", null, arrowTextProps);
        
    	//draw step 2
        lang.nextStep();
        currentStep = 2;
        updateGraphSteps();
        lang.newPolyline(new Coordinates[] {site, user}, "authArrow",  null, lineProps);
        lang.newText(textCoor, "Authentifizierung", "authText", null, arrowTextProps);
        
        //draw step 3
        lang.nextStep();
        currentStep = 3;
        updateGraphSteps();
    	lang.newPolyline(new Coordinates[] {attacker, user}, "loginArrow",  null, lineProps);
    	textCoor = new Coordinates( 
    			( attacker.getX()+user.getX() ) / 2, 
    			( attacker.getY()+user.getY() ) / 2);
    	lang.newText(textCoor, "E-Mail/Link", "mailText", null, arrowTextProps);
    	
    	//draw code of the E-Mail/Link
    	lang.nextStep();
    	TextProperties highlightProps = new TextProperties();
    	highlightProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 16));
    	graphPrimitives.add(lang.newText(new Coordinates(LEFT_OFFSET+550, TOP_OFFSET+150),
    			"Beispielhafter Aufbau der Mail:", 
    			"attackCode1", null, highlightProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode1", AnimalScript.DIRECTION_NW),
    			"[...]", 
    			"attackCode2", null, textProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode2", AnimalScript.DIRECTION_NW),
    			"<img src=" + Domain + "/transfer?acct=1234&amount=1000 width=1 height=1>", 
    			"attackCode3", null, textProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode3", AnimalScript.DIRECTION_NW),
    			"[...]", 
    			"attackCode4", null, textProps));
        
    	lang.nextStep();
    	graphPrimitives.add(lang.newText(new Offset(0, 50, "attackCode4", AnimalScript.DIRECTION_NW),
    			"Beispielhafter Aufbau des Links:", 
    			"attackCode5", null, highlightProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode5", AnimalScript.DIRECTION_NW),
    			"[...]", 
    			"attackCode6", null, textProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode6", AnimalScript.DIRECTION_NW),
    			"<a href=" + Domain + "/transfer?acct=1234&amount=1000>View my Video!</a>", 
    			"attackCode7", null, textProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode7", AnimalScript.DIRECTION_NW),
    			"[...]", 
    			"attackCode8", null, textProps));
    	
    	//draw step 4
    	lang.nextStep();
    	currentStep = 4;
    	updateGraphSteps();
    	
    	//draw step 5
    	lang.nextStep();
    	currentStep = 5;
    	updateGraphSteps();
    	lang.newPolyline(new Coordinates[] {user, site}, "transArrow",  null, lineProps);
    	textCoor = new Coordinates( 
    			( user.getX()+site.getX() ) / 2, 
    			( user.getY()+site.getY() ) / 2);
    	lang.newText(textCoor, "Transaktion", "transText", null, arrowTextProps);
    	
    	//ask question
    	lang.nextStep();
    	MultipleChoiceQuestionModel question = 
    			new MultipleChoiceQuestionModel("countermeasuresQuestion");
    	question.setPrompt("Welche Gegenmaßnahmen sind Sinnvoll, um einen "
    			+ "CSRF-Angriff zu verhindern?");
    	question.addAnswer("Nutzung besserer Passwörter beim login.", 
    			0, 
    			"Leider nicht korrekt, die Passwortstärke hat keinen Einfluss " 
    			+ "auf den Angriff.");
    	question.addAnswer("Evaluieren der Transaktion auf Serverseite.", 
    			100, 
    			"Korrekt! Die Transaktion sollte vom Server nicht ohne Prüfung "
    			+ "durchgeführt werden.");
    	question.addAnswer("Als Nutzer den Link überprüfen.", 
    			30, 
    			"Leider nicht ganz. Ein CSRF-Angriff kann auch erfolgen, ohne "
    			+ "dass der Nutzer direkt auf den Link klickt.");
    	question.setNumberOfTries(1);
    	lang.addMCQuestion(question);
    }
    
    private void showCountermeasures() {
    	PolylineProperties lineProps = Pfeile;
    	TextProperties arrowTextProps = new TextProperties();
    	arrowTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));
    	arrowTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    	arrowTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    	
    	//coordinates for arrows
    	Coordinates user = algoanim.util.Node.convertToNode(
    			new Point(this.userCoor.getX() + BODY_RADIUS, 
    					this.userCoor.getY() + HEAD_RADIUS));
    	Coordinates site = algoanim.util.Node.convertToNode(
    			new Point(this.siteCoor.getX() - BODY_RADIUS, this.siteCoor.getY() + SITE_WIDTH));
        Coordinates attacker = algoanim.util.Node.convertToNode(
    			new Point(this.attackerCoor.getX() - BODY_RADIUS, 
    					this.attackerCoor.getY() + HEAD_RADIUS));
    	
    	// generate algorithm steps with visualization
    	//draw step 1
        currentStep = 1;
        updateCountermeasureSteps();
        lang.newPolyline(new Coordinates[] {user, site}, "loginArrow",  null, lineProps);
    	Coordinates textCoor = new Coordinates( 
    			( user.getX()+site.getX() ) / 2, 
    			( user.getY()+site.getY() ) / 2);
    	lang.newText(textCoor, "Login", "loginText", null, arrowTextProps);
        
    	//draw step 2
        lang.nextStep();
        currentStep = 2;
        updateCountermeasureSteps();
        lang.newPolyline(new Coordinates[] {site, user}, "authArrow",  null, lineProps);
        lang.newText(textCoor, "Authentifizierung", "authText", null, arrowTextProps);
        
        //draw step 3
        lang.nextStep();
        currentStep = 3;
        updateCountermeasureSteps();
    	
        //draw step 3a
        lang.nextStep();
        currentStep = 4;
        updateCountermeasureSteps();
        lang.newPolyline(new Coordinates[] {user, site}, "visitArrow",  null, lineProps);
        lang.newText(textCoor, "Besuch der Seite", "visitText", null, arrowTextProps);
        
        //draw step 3b
        lang.nextStep();
        currentStep = 5;
        updateCountermeasureSteps();
        lang.newPolyline(new Coordinates[] {site, user}, "tokenArrow",  null, lineProps);
        lang.newText(textCoor, "Token setzen", "tokenText", null, arrowTextProps);
        
        //draw step 3c
        lang.nextStep();
        currentStep = 6;
        updateCountermeasureSteps();
        lang.newPolyline(new Coordinates[] {user, site}, "transArrow",  null, lineProps);
        lang.newText(textCoor, "Transaktion", "transText", null, arrowTextProps);
        
        
    	//draw step 4
    	lang.nextStep();
    	currentStep = 7;
    	updateCountermeasureSteps();
    	lang.newPolyline(new Coordinates[] {attacker, user}, "loginArrow",  null, lineProps);
    	textCoor = new Coordinates( 
    			( attacker.getX()+user.getX() ) / 2, 
    			( attacker.getY()+user.getY() ) / 2);
    	lang.newText(textCoor, "E-Mail/Link", "mailText", null, arrowTextProps);
    	
    	//draw code of the E-Mail/Link
    	//lang.nextStep();
    	TextProperties highlightProps = new TextProperties();
    	highlightProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 16));
    	graphPrimitives.add(lang.newText(new Coordinates(LEFT_OFFSET+550, TOP_OFFSET+150),
    			"Beispielhafter Aufbau der Mail:", 
    			"attackCode1", null, highlightProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode1", AnimalScript.DIRECTION_NW),
    			"[...]", 
    			"attackCode2", null, textProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode2", AnimalScript.DIRECTION_NW),
    			"<img src=" + Domain + "/transfer?acct=1234&amount=1000 width=1 height=1>", 
    			"attackCode3", null, textProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode3", AnimalScript.DIRECTION_NW),
    			"[...]", 
    			"attackCode4", null, textProps));
        
    	//lang.nextStep();
    	graphPrimitives.add(lang.newText(new Offset(0, 50, "attackCode4", AnimalScript.DIRECTION_NW),
    			"Beispielhafter Aufbau des Links:", 
    			"attackCode5", null, highlightProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode5", AnimalScript.DIRECTION_NW),
    			"[...]", 
    			"attackCode6", null, textProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode6", AnimalScript.DIRECTION_NW),
    			"<a href=" + Domain + "/transfer?acct=1234&amount=1000>View my Video!</a>", 
    			"attackCode7", null, textProps));
    	graphPrimitives.add(lang.newText(new Offset(0, 25, "attackCode7", AnimalScript.DIRECTION_NW),
    			"[...]", 
    			"attackCode8", null, textProps));
    	
    	//draw step 5
    	lang.nextStep();
    	currentStep = 8;
    	updateCountermeasureSteps();
    	
    	//draw step 6
    	lang.nextStep();
    	currentStep = 9;
    	updateCountermeasureSteps();
    	lang.newPolyline(new Coordinates[] {user, site}, "transArrow",  null, lineProps);
    	textCoor = new Coordinates( 
    			( user.getX()+site.getX() ) / 2, 
    			( user.getY()+site.getY() ) / 2);
    	lang.newText(textCoor, "Transaktion", "transText", null, arrowTextProps);
    	
    	//draw step 7
        lang.nextStep();
        currentStep = 10;
        updateCountermeasureSteps();
        lang.newPolyline(new Coordinates[] {site, user}, "declineArrow",  null, lineProps);
        lang.newText(textCoor, "Abgelehnt", "declineText", null, arrowTextProps);
    }
    
    private void updateGraphSteps(){
    	lang.hideAllPrimitivesExcept(this.graphPrimitives);
    	
    	TextProperties currentProps = this.textProps;
    	TextProperties highlightProps = new TextProperties();
    	highlightProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 16));
    	
    	if(currentStep >= 1){
    		if(currentStep == 1) currentProps = highlightProps;
    		lang.newText(new Coordinates(
            		this.userCoor.getX() - BODY_RADIUS, 
            		this.attackerCoor.getY() + 2 * HEAD_RADIUS + 32),
            		"1. Der Nutzer loggt sich auf einer vertrauenswürdigen Webseite ein.",
            		"algo1", null, currentProps);
    		currentProps = textProps;
    	}
    	
    	if(currentStep >= 2){
    		if(currentStep == 2) currentProps = highlightProps;
    		lang.newText(new Offset(0, 25, "algo1", AnimalScript.DIRECTION_NW),
            		"2. Authentifizierung des Nutzers durch die Webseite.",
            		"algo2", null, currentProps);
    		currentProps = textProps;
    	}
        
    	if(currentStep >= 3){
    		if(currentStep == 3) currentProps = highlightProps;
    		lang.newText(
                    new Offset(0, 25, "algo2", AnimalScript.DIRECTION_NW),
                    "3. Der Angreifer sendet eine E-Mail oder einen Link mit ",
                    "algo3a", null, currentProps);
            lang.newText(
                    new Offset(25, 25, "algo3a", AnimalScript.DIRECTION_NW),
                    "Schadcode.",
                    "algo3b", null, currentProps);
    		currentProps = textProps;
    	}
        
        if(currentStep >= 4){
    		if(currentStep == 4) currentProps = highlightProps;
        	lang.newText(
                    new Offset(-25, 25, "algo3b", AnimalScript.DIRECTION_NW),
                    "4. Der Nutzer öffnet die Mail/den Link.",
                    "algo4", null, currentProps);
    		currentProps = textProps;
        }
        
        if(currentStep >= 5){
    		if(currentStep == 5) currentProps = highlightProps;
        	lang.newText(
                    new Offset(0, 25, "algo4", AnimalScript.DIRECTION_NW),
                    "5. Der Browser des Nutzers führt den Code aus und startet die ",
                    "algo5a", null, currentProps);
            lang.newText(
                    new Offset(25, 25, "algo5a", AnimalScript.DIRECTION_NW),
                    "Transaktion mit der ursprünglichen Webseite.",
                    "algo5b", null, currentProps);
    		currentProps = textProps;
        }
    }
    
    private void updateCountermeasureSteps(){
    	lang.hideAllPrimitivesExcept(this.graphPrimitives);
    	
    	TextProperties currentProps = this.textProps;
    	TextProperties highlightProps = new TextProperties();
    	highlightProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 16));
    	
    	if(currentStep >= 1){
    		if(currentStep == 1) currentProps = highlightProps;
    		lang.newText(new Coordinates(
            		this.userCoor.getX() - BODY_RADIUS, 
            		this.attackerCoor.getY() + 2 * HEAD_RADIUS + 32),
            		"Mögliche Gegenmaßnahme von Serverseite:",
            		"CMheader", null, highlightProps);
    		lang.newText(new Offset(0, 25, "CMheader", AnimalScript.DIRECTION_NW),
            		"1. Der Nutzer loggt sich auf einer vertrauenswürdigen Webseite ein.",
            		"cm1", null, currentProps);
    		currentProps = textProps;
    	}
    	
    	if(currentStep >= 2){
    		if(currentStep == 2) currentProps = highlightProps;
    		lang.newText(new Offset(0, 25, "cm1", AnimalScript.DIRECTION_NW),
            		"2. Authentifizierung des Nutzers durch die Webseite.",
            		"cm2", null, currentProps);
    		currentProps = textProps;
    	}
    	
    	if(currentStep >= 3){
    		if(currentStep == 3) currentProps = highlightProps;
    		lang.newText(new Offset(0, 25, "cm2", AnimalScript.DIRECTION_NW),
            		"3. Normale Nutzung der Webseite:",
            		"cm3a", null, currentProps);
    		currentProps = textProps;
    	}
    	
    	if(currentStep >= 4){
    		if(currentStep == 4) currentProps = highlightProps;
    		lang.newText(new Offset(25, 25, "cm3a", AnimalScript.DIRECTION_NW),
            		"a) Besuchen der Webseite für Transaktion.",
            		"cm3b", null, currentProps);
    		currentProps = textProps;
    	}
    	
    	if(currentStep >= 5){
    		if(currentStep == 5) currentProps = highlightProps;
    		lang.newText(new Offset(0, 25, "cm3b", AnimalScript.DIRECTION_NW),
            		"b) Setzen eines One-Time-Tokens (OTT) durch die Webseite.",
            		"cm3c", null, currentProps);
    		currentProps = textProps;
    	}
    	
    	if(currentStep >= 6){
    		if(currentStep == 6) currentProps = highlightProps;
    		lang.newText(new Offset(0, 25, "cm3c", AnimalScript.DIRECTION_NW),
            		"c) Durchführen der Transaktion, validiert durch OTT.",
            		"cm3d", null, currentProps);
    		currentProps = textProps;
    	}
        
    	if(currentStep >= 7){
    		if(currentStep == 7) currentProps = highlightProps;
    		lang.newText(
                    new Offset(-25, 25, "cm3d", AnimalScript.DIRECTION_NW),
                    "4. Der Angreifer sendet eine E-Mail oder einen Link mit ",
                    "cm4a", null, currentProps);
            lang.newText(
                    new Offset(25, 25, "cm4a", AnimalScript.DIRECTION_NW),
                    "Schadcode.",
                    "cm4b", null, currentProps);
    		currentProps = textProps;
    	}
        
        if(currentStep >= 8){
    		if(currentStep == 8) currentProps = highlightProps;
        	lang.newText(
                    new Offset(-25, 25, "cm4b", AnimalScript.DIRECTION_NW),
                    "5. Der Nutzer öffnet die Mail/den Link.",
                    "cm5", null, currentProps);
    		currentProps = textProps;
        }
        
        if(currentStep >= 9){
    		if(currentStep == 9) currentProps = highlightProps;
        	lang.newText(
                    new Offset(0, 25, "cm5", AnimalScript.DIRECTION_NW),
                    "6. Der Browser des Nutzers führt den Code aus und versucht ",
                    "cm6a", null, currentProps);
            lang.newText(
                    new Offset(25, 25, "cm6a", AnimalScript.DIRECTION_NW),
                    "die Transaktion mit der ursprünglichen Webseite zu starten.",
                    "cm6b", null, currentProps);
    		currentProps = textProps;
        }
        
        if(currentStep >= 10){
    		if(currentStep == 10) currentProps = highlightProps;
        	lang.newText(
                    new Offset(-25, 25, "cm6b", AnimalScript.DIRECTION_NW),
                    "7. Wegen dem fehlendem OTT wird die Transaktion nicht ausgeführt.",
                    "cm7", null, currentProps);
    		currentProps = textProps;
        }
    }
    
    private void generateConclusion(){
    	generateHeader();
    	lang.newText(new Coordinates(30, 100),
        		"Cross-Site-Request-Forgeries gehören zu den am weitesten verbreiteten ",
        		"conclusion1", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion1", AnimalScript.DIRECTION_NW),
        		"Angriffen auf IT-Systeme. ",
        		"conclusion2", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion2", AnimalScript.DIRECTION_NW),
        		"Sie können allerdings unter anderem verhindert werden, indem vom ",
        		"conclusion3", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion3", AnimalScript.DIRECTION_NW),
        		"Server One-Time-Tokens verwendet werden. Ein solches Token wird vom ",
        		"conclusion4", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion4", AnimalScript.DIRECTION_NW),
        		"Server zufällig generiert und an den Nutzer gesendet. Der Angreifer ",
        		"conclusion5", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion5", AnimalScript.DIRECTION_NW),
        		"kann auf dieses Token nicht zugreifen, da es nur im Kontext der ",
        		"conclusion6", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion6", AnimalScript.DIRECTION_NW),
        		"vertrauenswürdigen Webseite eingesehen werden kann. Um eine ",
        		"conclusion7", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion7", AnimalScript.DIRECTION_NW),
        		"Transaktion durchzuführen, muss das Token wieder an den Server ",
        		"conclusion8", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion8", AnimalScript.DIRECTION_NW),
        		"gesendet und verifiziert werden. ",
        		"conclusion9", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion9", AnimalScript.DIRECTION_NW),
        		"Zudem kann durch das Anfordern einer PIN oder eines Passworts bei der ",
        		"conclusion10", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion10", AnimalScript.DIRECTION_NW),
        		"Transaktion ein CSRF-Angriff verhindert werden, da hierbei die ",
        		"conclusion11", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion11", AnimalScript.DIRECTION_NW),
        		"Transaktion nicht ohne Best�tigung durch den Nutzer durchgeführt ",
        		"conclusion12", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion12", AnimalScript.DIRECTION_NW),
        		"wird.",
        		"conclusion13", null, textProps);
        
    }
    
    public String getName() {
        return "Cross-Site-Request-Forgery";
    }

    public String getAlgorithmName() {
        return "Cross-Site-Request-Forgery";
    }

    public String getAnimationAuthor() {
        return "Alexander Müller";
    }

    public String getDescription(){
        return "Cross-Site-Request-Forgery oder Session-Riding bezeichnet einen Angriff auf den Nutzer einer Webanwendung, bei dem der Browser des Nutzers ausgenutzt wird, um eine Transaktion in der Webanwendung durchzuführen. Dazu muss das Opfer bei der Webanwendung angemeldet sein und eine Webseite oder E-Mail mit Schadcode öffnen. Beim öffnen der Webseite/E-Mail wird der Code auf dem Gerät des Opfers ausgeführt und dabei die Transaktion durchgeführt.";
    }

    public String getCodeExample(){
        return "Der Nutzer meldet sich normal bei der Webanwendung an und wird durch diese Authentifiziert. Anschließend wird der Nutzer dazu gebracht eine E-Mail oder Webseite mit Schadcode zu öffnen und den darin enthaltenen Code dadurch auszuführen. Der Code wird dabei vom auf der Webanwendung authentifizierten Browser ausgeführt und dabei die Transaktion durchgeführt.";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    
    public static void main(String[] args) {
	    // Create a new language object for generating animation code
	    // this requires type, name, author, screen width, screen height
		CSRF_Generator gen = new CSRF_Generator();
		gen.init();
	    gen.generate(null, null);
	    
	    System.out.println(gen.lang);
	  }
	  
}