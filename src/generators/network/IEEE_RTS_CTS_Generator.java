/*
 * IEEE_RTS_CTS_Generator.java
 * Alexander Müller, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Random;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;

import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;

public class IEEE_RTS_CTS_Generator implements ValidatingGenerator {
    private Language lang;
    
    //variables for clients and host
    private int CWmax;
    private int CWmin;
    private int clientCount;
    private int[] clientOmega;
    private int[] clientBackoff;
    
    //variables for animalScript
    private CircleProperties hostcircle;
    private CircleProperties clientcircle;
    private TextProperties textProps;
    private TextProperties arrowTextProps;
    
    //variables for graph generation
    private Coordinates hostCoor;
    private Coordinates[] clientsCoor;
    private List<Primitive> graphPrimitives;
    
    private boolean step1visible = true;
    private boolean step2visible = false;
    private boolean step3visible = false;
    private boolean step3bvisible = false;
    private boolean step4visible = false;
    private boolean step5visible = false;
    private boolean step6visible = false;
    private int currentStep = 1;
    
    static int leftOffset = 80;
    static int topOffset = 100;
    static int nodeRadius = 30;
    static int clientDistance = 120;

    public void init(){
        lang = new AnimalScript("RTS/CTS Mechanismus", "Alexander Müller", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(
       		 Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	if(props == null && primitives == null){
        	//initialize algo with test values for console output
        	CWmax = 128;
            CWmin = 8;
            clientCount = 3;
            hostcircle = new CircleProperties();
        	hostcircle.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
        	hostcircle.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        	hostcircle.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        	clientcircle = new CircleProperties();
        	clientcircle.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
        	clientcircle.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
        	clientcircle.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
            
            //generate random backoff-times for clients
            clientOmega = new int[clientCount];
            clientBackoff = new int[clientCount];
            generateBackoffTimes();
            graphPrimitives = new ArrayList<Primitive>();
        	clientsCoor = new Coordinates[clientCount];
            
        	//generate animalScript
        	generateHeader();
        	lang.nextStep("Einleitung");
        	generateDiscription();
        	lang.nextStep("Beispiel");
        	lang.hideAllPrimitivesExcept(graphPrimitives);
        	generateGraph();
        	executeAlgo();
        	lang.nextStep("Fazit");
        	lang.hideAllPrimitives();
        	generateConclusion();
            
        	lang.finalizeGeneration();
            return lang.toString();
        }
        
        //properties for host and client
        hostcircle = (CircleProperties)props.getPropertiesByName("hostcircle");
        clientcircle = (CircleProperties)props.getPropertiesByName("clientcircle");
    	
    	CWmax = (Integer)primitives.get("CW max");
        CWmin = (Integer)primitives.get("CW min");
        clientCount = (Integer)primitives.get("Clientanzahl");
        
        //generate random backoff-times for clients
        clientOmega = new int[clientCount];
        clientBackoff = new int[clientCount];
        generateBackoffTimes();
        graphPrimitives = new ArrayList<Primitive>();
    	clientsCoor = new Coordinates[clientCount];
        
    	//set visibilities for steps
    	step1visible = true;
    	step2visible = false;
        step3visible = false;
        step3bvisible = false;
        step4visible = false;
        step5visible = false;
        step6visible = false;
        currentStep = 1;
    	
    	//generate animalScript
    	generateHeader();
    	lang.nextStep("Einleitung");
    	generateDiscription();
    	lang.nextStep("Beispiel");
    	lang.hideAllPrimitivesExcept(graphPrimitives);
    	generateGraph();
    	executeAlgo();
    	lang.nextStep("Fazit");
    	lang.hideAllPrimitives();
    	generateConclusion();
        
    	lang.finalizeGeneration();
        return lang.toString();
    }
    
    private void generateBackoffTimes(){
    	Random ranGen = new Random();
        for(int i = 0; i < clientCount; i++){
        	clientOmega[i] = CWmin;
        	clientBackoff[i] = ranGen.nextInt(clientOmega[i])+1;
        }
        //force collision on last client
        clientBackoff[clientCount - 1] = clientBackoff[ranGen.nextInt(clientCount - 2)];
    }

    private void generateHeader() {
    	// show the header with a heading surrounded by a rectangle
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 24));
        this.graphPrimitives.add(
        		lang.newText(new Coordinates(40, 30), getAlgorithmName(),
        				"header", null, headerProps));
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        this.graphPrimitives.add(
        		lang.newRect(new Offset(-5, -5, "header",
        				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
        				null, rectProps));
    }
    
    private void generateDiscription() {
    	// setup the start page with the description
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.PLAIN, 16));
        lang.newText(new Coordinates(10, 100),
        		"Der Access-Controll-Mechanismus des IEEE 802.11 WiFi Standards ",
        		"description1", null, textProps);
        lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
        		"dient dazu, den Zugriff auf den WiFi-Kanal unter den einzelen Clients ",
        		"description2", null, textProps);
        lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
        		"aufzuteilen und dabei Kollisionen zu vermeiden. Dabei wird zwischen ",
        		"description3", null, textProps);
        lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW),
        		"zwei Verfahren unterschieden.",
        		"description4", null, textProps);
        lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
        		"Im folgenden wird das RTS/CTS-Verfahren näher betrachtet. Bei ",
        		"description5", null, textProps);
        lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW),
        		"diesem Verfahren wird vor der Datenübertragung durch den Host der ",
        		"description6", null, textProps);
        lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
                "Kanal freigegeben und dadurch die Dauer von Kollisionen verkürzt.",
                "description7", null, textProps);

        // generate algorithm explanation in steps
        lang.nextStep("Beschreibung");
        lang.newText(new Offset(0, 50, "description7", AnimalScript.DIRECTION_NW),
        		"1. Will ein Client Daten senden, wählt er einen zufälligen ",
        		"algo11", null, textProps);
        lang.newText(new Offset(25, 25, "algo11", AnimalScript.DIRECTION_NW),
        		"Backoff-Wert zwischen 0 und w (zunächst w = CWmin).",
        		"algo12", null, textProps);

        lang.nextStep();
        lang.newText(new Offset(-25, 25, "algo12", AnimalScript.DIRECTION_NW),
        		"2. Der Backoff-Wert wird herunter gezählt, solange der WiFi Kanal ",
        		"algo21", null, textProps);
        lang.newText(new Offset(25, 25, "algo21", AnimalScript.DIRECTION_NW),
        		"nicht genutzt wird. Findet eine Übertragung auf dem Kanal statt, ",
        		"algo22", null, textProps);
        lang.newText(new Offset(0, 25, "algo22", AnimalScript.DIRECTION_NW),
        		"wird der Backoff-Wert eingefroren, bis der Kanal für einen DIFS ", 
        		"algo23", null, textProps);
        lang.newText(new Offset(0, 25, "algo23", AnimalScript.DIRECTION_NW),
        		"ungenutzt bleibt.", 
        		"algo24", null, textProps);

        lang.nextStep();
        lang.newText(
                new Offset(-25, 25, "algo24", AnimalScript.DIRECTION_NW),
                "3. Erreicht der Backoff-Wert Null, sendet der Client einen ",
                "algo31", null, textProps);
        lang.newText(
                new Offset(25, 25, "algo31", AnimalScript.DIRECTION_NW),
                "Request-To-Sent (RTS).",
                "algo32", null, textProps);
        
        lang.nextStep();
        lang.newText(
                new Offset(-25, 25, "algo32", AnimalScript.DIRECTION_NW),
                "3b. Kommt es beim Übertragen des RTS zu einer Kollsion, wird w ",
                "algo33", null, textProps);
        lang.newText(
                new Offset(25, 25, "algo33", AnimalScript.DIRECTION_NW),
                "verdoppelt (solange w < CWmax) und wieder bei 1. begonnen.",
                "algo34", null, textProps);
        
        lang.nextStep();
        lang.newText(
                new Offset(-25, 25, "algo34", AnimalScript.DIRECTION_NW),
                "4. Der Host antwortet auf das RTS-Signal mit einem Clear-To-Send ",
                "algo41", null, textProps);
        lang.newText(
                new Offset(25, 25, "algo41", AnimalScript.DIRECTION_NW),
                "(CTS).",
                "algo42", null, textProps);
        
        lang.nextStep();
        lang.newText(
                new Offset(-25, 25, "algo42", AnimalScript.DIRECTION_NW),
                "5. Der Client sendet das Datenpaket.",
                "algo51", null, textProps);
        
        lang.nextStep();
        lang.newText(
                new Offset(0, 25, "algo51", AnimalScript.DIRECTION_NW),
                "6. Der Host bestätigt den Empfang des Pakets durch ein ACK-Signal.",
                "algo61", null, textProps);
    }
    
    private void generateGraph() {
    	TextProperties nodeTextProps = new TextProperties();
    	nodeTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    	
    	Coordinates tempCoor;
    	//draw host node
    	hostCoor = new Coordinates(
				leftOffset + nodeRadius + (clientDistance * (clientCount-1) )/2, 
				topOffset + nodeRadius );
    	graphPrimitives.add(lang.newCircle(hostCoor, nodeRadius, "Host", null, hostcircle));
    	tempCoor = new Coordinates(hostCoor.getX(), hostCoor.getY() - 8 );
    	graphPrimitives.add(lang.newText(tempCoor, "Host", "HostText", null, nodeTextProps));
    	
    	//draw client nodes
    	for(int i = 0; i < clientCount; i++){
    		clientsCoor[i] = new Coordinates(
    				leftOffset + nodeRadius + (clientDistance * i), 
    				topOffset + nodeRadius + clientDistance );
    		graphPrimitives.add(lang.newCircle(clientsCoor[i], nodeRadius, "Client" + i, null, clientcircle));
    		tempCoor = new Coordinates(clientsCoor[i].getX(), clientsCoor[i].getY() - 8 );
    		graphPrimitives.add(lang.newText(tempCoor, 
        			"Client " + (i+1), 
        			"ClientText" + i, 
        			null, 
        			nodeTextProps));
    	}
    	
    	//draw CWmin and CWmax
    	Coordinates CWText = new Coordinates(
				clientsCoor[0].getX() + clientDistance / 2, 
    			clientsCoor[0].getY() + nodeRadius + 42);
    	graphPrimitives.add(lang.newText(CWText, 
    			"CWmin = " + CWmin, 
    			"CWmin", 
    			null, 
    			nodeTextProps));
    	CWText = new Coordinates(
				clientsCoor[1].getX() + clientDistance / 2, 
    			clientsCoor[0].getY() + nodeRadius + 42);
    	graphPrimitives.add(lang.newText(CWText, 
    			"CWmax = " + CWmax, 
    			"CWmax", 
    			null, 
    			nodeTextProps));
    	
    	generateGraphExplanation();
    }
    
    private void generateGraphExplanation(){
    	TextProperties usedProps = this.textProps;
    	
    	TextProperties highlightProps = new TextProperties();
    	highlightProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 16));
    	
    	// draw algorithm steps
    	//step1
    	if(step1visible){
    		if(currentStep == 1){
    			usedProps = highlightProps;
    		}
    		lang.newText(new Coordinates(leftOffset - 40, topOffset + nodeRadius + clientDistance* 2),
            		"1. Will ein Client Daten senden, wählt er einen zufälligen ",
            		"step11", null, usedProps);
            lang.newText(new Offset(25, 25, "step11", AnimalScript.DIRECTION_NW),
            		"Backoff-Wert zwischen 0 und w (zunächst w = CWmin).",
            		"step12", null, usedProps);
            usedProps = this.textProps;
    	}

        //step2
    	if(step2visible){
    		if(currentStep == 2){
    			usedProps = highlightProps;
    		}
    		lang.newText(new Offset(-25, 25, "step12", AnimalScript.DIRECTION_NW),
            		"2. Der Backoff-Wert wird herunter gezählt, solange der WiFi Kanal ",
            		"step21", null, usedProps);
            lang.newText(new Offset(25, 25, "step21", AnimalScript.DIRECTION_NW),
            		"nicht genutzt wird. Findet eine Übertragung auf dem Kanal statt, ",
            		"step22", null, usedProps);
            lang.newText(new Offset(0, 25, "step22", AnimalScript.DIRECTION_NW),
            		"wird der Backoff-Wert eingefroren, bis der Kanal für einen DIFS ", 
            		"step23", null, usedProps);
            lang.newText(new Offset(0, 25, "step23", AnimalScript.DIRECTION_NW),
            		"ungenutzt bleibt.", 
            		"step24", null, usedProps);
            usedProps = this.textProps;
    	}

        //step3
    	if(step3visible){
    		if(currentStep == 3){
    			usedProps = highlightProps;
    		}
    		lang.newText(
                    new Offset(-25, 25, "step24", AnimalScript.DIRECTION_NW),
                    "3. Erreicht der Backoff-Wert Null, sendet der Client einen ",
                    "step31", null, usedProps);
            lang.newText(
                    new Offset(25, 25, "step31", AnimalScript.DIRECTION_NW),
                    "Request-To-Sent (RTS).",
                    "step32", null, usedProps);
            usedProps = this.textProps;
    	}
        
        //step3b
    	if(step3bvisible){
    		if(currentStep == 31){
    			usedProps = highlightProps;
    		}
    		lang.newText(
                    new Offset(-25, 25, "step32", AnimalScript.DIRECTION_NW),
                    "3b. Kommt es beim Übertragen des RTS zu einer Kollsion, wird w ",
                    "step33", null, usedProps);
            lang.newText(
                    new Offset(25, 25, "step33", AnimalScript.DIRECTION_NW),
                    "verdoppelt (solange w < CWmax) und wieder bei 1. begonnen.",
                    "step34", null, usedProps);
            usedProps = this.textProps;
    	}
        
        //step4
    	if(step4visible){
    		if(currentStep == 4){
    			usedProps = highlightProps;
    		}
            lang.newText(
                    new Offset(-25, 75, "step32", AnimalScript.DIRECTION_NW),
                    "4. Der Host antwortet auf das RTS-Signal mit einem Clear-To-Send ",
                    "step41", null, usedProps);
            lang.newText(
                    new Offset(25, 25, "step41", AnimalScript.DIRECTION_NW),
                    "(CTS).",
                    "step42", null, usedProps);
            usedProps = this.textProps;
    	}
        
        //step5
    	if(step5visible){
    		if(currentStep == 5){
    			usedProps = highlightProps;
    		}
    		lang.newText(
                new Offset(-25, 25, "step42", AnimalScript.DIRECTION_NW),
                "5. Der Client sendet das Datenpaket.",
                "step51", null, usedProps);
            usedProps = this.textProps;
    	}
        
        
        //step6
    	if(step6visible){
    		if(currentStep == 6){
    			usedProps = highlightProps;
    		}
    		lang.newText(
                    new Offset(0, 25, "step51", AnimalScript.DIRECTION_NW),
                    "6. Der Host bestätigt den Empfang des Pakets durch ein ACK-Signal.",
                    "step61", null, usedProps);
            usedProps = this.textProps;
    	}
    	updateBackoffTexts();
    }
    
    private void updateBackoffTexts(){
    	TextProperties backoffTextProps = new TextProperties();
    	backoffTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    	TextProperties omegaTextProps = new TextProperties();
    	omegaTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    	omegaTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
    	for(int i = 0; i < clientCount; i++){
        	if(clientBackoff[i] >= 0){
        		Coordinates backoffCoor = new Coordinates(
        				clientsCoor[i].getX(), 
            			clientsCoor[i].getY() + nodeRadius);
        		Coordinates omegaCoor = new Coordinates(
        				clientsCoor[i].getX(), 
            			clientsCoor[i].getY() + nodeRadius + 12);
        		//generate backoff texts
        		lang.newText(backoffCoor, 
            			"Backoff = " + clientBackoff[i], 
            			"BackoffTime" + i, 
            			null, 
            			backoffTextProps);
        		//generate omega texts
        		lang.newText(omegaCoor, 
            			"w = " + clientOmega[i], 
            			"w" + i, 
            			null, 
            			omegaTextProps);
        	}
    	}
    }
    
    private void executeAlgo(){
		arrowTextProps = new TextProperties();
    	arrowTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    	
    	step2visible = true;
    	while(!algoFinished()){
    		lang.nextStep();
    		currentStep = 2;
    		LinkedList<Integer> readyClients = new LinkedList<Integer>();
    		
    		lang.hideAllPrimitivesExcept(graphPrimitives);
    		// find clients ready to send
    		for(int i = 0; i < clientCount; i++){
    			//update backoff time
    			clientBackoff[i]--;
    			
    			//save clients ready for RTS
    			if(clientBackoff[i] == 0){
    				readyClients.add(i);
    			}
    		}
    		
    		boolean collisionOccured = (readyClients.size() > 1);
    		
    		// draw next slide
    		generateGraphExplanation();
        	//choose color for arrow text
        	Color arrowColor = collisionOccured? Color.RED : Color.GREEN;
        	arrowTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, arrowColor);
        	
    		if(readyClients.size() > 0){
            	//draw explanation for RTS signal
            	lang.hideAllPrimitivesExcept(graphPrimitives);
            	step3visible = true;
            	currentStep = 3;
    			generateGraphExplanation();
    			//draw RTS arrows
            	for(int c : readyClients){
            		sendRTS(c);
    			}
        		lang.nextStep();
        		
        		//respond to RTS
    			if(!collisionOccured){//respond to correct transmission
        			generateRTSResponse(readyClients.getFirst());
        		} else {//respond to collision
        	    	MultipleChoiceQuestionModel question = 
        	    			new MultipleChoiceQuestionModel("collisionQuestion");
        	    	question.setPrompt("Was geschieht nach dem Senden der RTS-Signale?");
        	    	question.addAnswer("Der Host antwortet mit einem CTS-Signal an einen der Clients.", 
        	    			0, 
        	    			"Leider nicht korrekt, der Host konnte wegen der Kollision das RTS-Signal "
        	    			+ "nicht empfangen.");
        	    	question.addAnswer("Die Clients wählen neue Backoff-Werte und wiederholen die "
        	    			+ "Übertragung.", 
        	    			100, 
        	    			"Korrekt! Die Übertragung der RTS-Signale ist Fehlgeschlagen und die "
        	    			+ "Clients versuchen es erneut.");
        	    	question.addAnswer("Die Clients senden ihr jeweiliges Datenpaket.", 
        	    			0, 
        	    			"Leider nicht korrekt. Ein Client darf sein Datenpaket erst senden, wenn "
        	    			+ "er sein CTS-Signal empfangen hat.");
        	    	question.setNumberOfTries(1);
        	    	lang.addMCQuestion(question);
        	    	lang.nextStep();
        			
        			generateCollisionResponse(readyClients);
        		}
    		}
    		
    	}
    }
    
    private void generateConclusion(){
    	generateHeader();
    	lang.newText(new Coordinates(10, 100),
        		"Durch die Nutzung des RTS/CTS Mechanismus wird die Entstehung von ",
        		"conclusion1", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion1", AnimalScript.DIRECTION_NW),
        		"Kollisionen bei der Übertragung von Datenpaketen vermieden. Da ",
        		"conclusion2", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion2", AnimalScript.DIRECTION_NW),
        		"Kollisionen nur noch bei der Übertragung von RTS-Signalen auftreten, ",
        		"conclusion3", null, textProps);
        lang.newText(new Offset(0, 25, "conclusion3", AnimalScript.DIRECTION_NW),
        		"ist deren Dauer kürzer und der WiFi-Kanal wird effizient genutzt.",
        		"conclusion4", null, textProps);
    }
    
    private boolean algoFinished(){
    	for(int bo : clientBackoff){
    		if(bo > 0)
    			return false;
    	}
    	return true;
    }
    
    private void sendRTS(int clientID){
    	//draw arrow from client to host
    	PolylineProperties lineProps = new PolylineProperties();
    	lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    	lineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
    	
    	Coordinates start = algoanim.util.Node.convertToNode(
    			new Point(clientsCoor[clientID].getX(), 
    					clientsCoor[clientID].getY() - nodeRadius));
    	Coordinates end = algoanim.util.Node.convertToNode(
    			new Point(hostCoor.getX(), hostCoor.getY() + nodeRadius));
    	lang.newPolyline(new Coordinates[] {start, end}, "ReqArrow",  null, lineProps);
    	
    	//draw text
    	Coordinates textCoor = new Coordinates( 
    			( start.getX()+end.getX() ) / 2, 
    			( start.getY()+end.getY() ) / 2);
    	lang.newText(textCoor, "RTS", "RTSText" + clientID, null, arrowTextProps);
    	
    }
    
    private void generateRTSResponse(int clientID){
    	lang.hideAllPrimitivesExcept(graphPrimitives);;
    	step4visible = true;
    	currentStep= 4;
		generateGraphExplanation();
    	//draw CTS response from host
    	PolylineProperties lineProps = new PolylineProperties();
    	lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    	lineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
    	
    	Coordinates host = algoanim.util.Node.convertToNode(
    			new Point(hostCoor.getX(), 
    					hostCoor.getY() + nodeRadius));
    	Coordinates client = algoanim.util.Node.convertToNode(
    			new Point(clientsCoor[clientID].getX(), 
    					clientsCoor[clientID].getY() - nodeRadius));
    	lang.newPolyline(new Coordinates[] {host, client}, "CTSArrow",  null, lineProps);
    	//draw CTS text
    	Coordinates textCoor = new Coordinates( 
    			( host.getX()+ client.getX() ) / 2, 
    			( host.getY()+ client.getY() ) / 2);
    	lang.newText(textCoor, "CTS", "CTSText", null, arrowTextProps);

    	//draw packet transmission from client
    	lang.nextStep();
    	lang.hideAllPrimitivesExcept(graphPrimitives);;
    	step5visible = true;
    	currentStep = 5;
		generateGraphExplanation();
		lang.newPolyline(new Coordinates[] {client, host}, "packetArrow",  null, lineProps);
    	lang.newText(textCoor, "Datenpaket", "dataPacket", null, arrowTextProps);

    	//draw ACK transmission from host
    	lang.nextStep();
    	lang.hideAllPrimitivesExcept(graphPrimitives);
    	step6visible = true;
    	currentStep = 6;
		generateGraphExplanation();
		lang.newPolyline(new Coordinates[] {host, client}, "ACKArrow",  null, lineProps);
    	lang.newText(textCoor, "ACK", "ACK", null, arrowTextProps);
    }
    
    private void generateCollisionResponse(LinkedList<Integer> collidingClients){
    	Random ranGen = new Random();
    	
    	lang.hideAllPrimitivesExcept(graphPrimitives);
		step3bvisible = true;
		currentStep = 31;
    	
    	//expand the contention window
    	for(int c : collidingClients){
    		if(clientOmega[c] < CWmax)
    			clientOmega[c] *= 2;
    	}
		generateGraphExplanation();
    	lang.nextStep();
    	
    	//assign new backoff times
    	for(int c : collidingClients){
    		if(clientOmega[c] < CWmax)
    			clientBackoff[c] = ranGen.nextInt(clientOmega[c])+1;
    	}
    	
    	lang.hideAllPrimitivesExcept(graphPrimitives);
    	currentStep = 1;
    	generateGraphExplanation();
    }
    
    public String getName() {
        return "RTS/CTS Mechanismus";
    }

    public String getAlgorithmName() {
        return "IEEE 802.11 RTS/CTS Mechanismus";
    }

    public String getAnimationAuthor() {
        return "Alexander Müller";
    }

    public String getDescription(){
        return "Der Access-Controll-Mechanismus des IEEE 802.11 WiFi Standards dient dazu, den Zugriff auf den WiFi-Kanal unter den einzelen Clients aufzuteilen und dabei Kollisionen zu vermeiden. Dabei wird zwischen zwei Verfahren unterschieden. Im folgenden wird das RTS/CTS-Verfahren näher betrachtet. Bei diesem Verfahren wird vor der Datenübertragung durch den Host der Kanal freigegeben und dadurch die Dauer von Kollisionen verkürzt."
        		+"\n";
    }

    public String getCodeExample(){
        return "Vor jeder Packetübertragung wird vom Client ein zufälliger Backoff-Wert zwischen 0 und w gewählt."
        		+"\n"
        		+"Ein Client, der ein Packet übertragen will, wartet bis der Kanal frei ist und zählt anschließend seine Backoff-Zeit herunter. Bei 0 angekommen sendet der Client ein \"request to send\" (RTS). Wird dies vom Host erkannt, antworter er mit einem \"clear to send\" (CTS). Der Host darf sein Packet nur dann senden, wenn er das entsprechende CTS-Signal empfängt.";
    }
    
    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		CWmax = (Integer)arg1.get("CW max");
        CWmin = (Integer)arg1.get("CW min");
        clientCount = (Integer)arg1.get("Clientanzahl");

        if(CWmax < CWmin)
        	return false;
        if(clientCount < 3 || clientCount > 8)
        	return false;
        
        return true;
	}
	/*
	public static void main(String[] args) {
	    // Create a new language object for generating animation code
	    // this requires type, name, author, screen width, screen height
		IEEE_RTS_CTS_Generator gen = new IEEE_RTS_CTS_Generator();
		gen.init();
	    gen.generate(null, null);
	    
	    System.out.println(gen.lang);
	  }
	*/
}