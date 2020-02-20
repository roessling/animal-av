/*
 * MontyHallProblem.java
 * Christian Staab, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;

public class MontyHallProblem implements Generator {
    private Language lang;
    private SourceCodeProperties textProps;
    private int fastIter;
    private int detailedIter;

    public void init(){
        lang = new AnimalScript("Monty Hall Problem", "Christian Staab", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        textProps = (SourceCodeProperties)props.getPropertiesByName("textProps");
        fastIter = (Integer)primitives.get("fastIter");
        detailedIter = (Integer)primitives.get("detailedIter");

        lang.setStepMode(true);
        montyHallStart(detailedIter, fastIter, textProps);
        
        return lang.toString();
    }

    public String getName() {
        return "Monty Hall Problem";
    }

    public String getAlgorithmName() {
        return "Monty Hall Problem";
    }

    public String getAnimationAuthor() {
        return "Christian Staab";
    }

    public String getDescription(){
        return "Angenommen sie sind in einer Spielshow und haben die Wahl"
 +"\n"
 +"sich zwischen 3 Tuüen zu entscheiden. Hinter einer Tür"
 +"\n"
 +"befindet sich ein Auto, hinter den anderen beiden Ziegen."
 +"\n"
 +"Nachdem sie sich für eine Tür, z.B. Nummer 3 entschieden haben"
 +"\n"
 +"öffnet der Showmaster, der weiß hinter welchen Türen Ziegen"
 +"\n"
 +"sind, eine Tür, z.B. Nummer 1, hinter der eine Ziege ist."
 +"\n"
 +"Dann gibt er ihnen die Option, sich statt für Tür Nummer 3"
 +"\n"
 +"für Tür Nummer 2 zu entscheiden."
 +"\n"
 +"\n"
 +"Ist es für Sie von Vorteil, die Tür zu wechseln?";
    }

    public String getCodeExample(){
        return "1. Zwei Ziegen und ein Auto werden zufällig hinter den Türen platziert. "
 +"\n"
 +"2. Der Spieler entscheidet sich ohne Kenntnis was hinter welcher                                                                                                    "
 +"\n"
 +"Tür ist für eine der Türen."
 +"\n"
 +"3. Der Showmaster öffnet eine vom Spieler nicht gewählte Tür hinter "
 +"\n"
 +"der sich eine Ziege befindet."
 +"\n"
 +"4a. Fall A: Der Spieler bleibt bei seiner Entscheidung."
 +"\n"
 +"4b. Fall B: Der Spieler wechselt die gewählte Tür.";
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

    

    /**
     * @author Christian Staab <christian.staab@stud.tu-darmstadt.de>
     * @version 1.0 2019.06.21
     * 
     * 
     */

      
      //create counters for total number of games and won without or with switching
      int numberGames;
      int numberWonWithoutSwitch;
      int numberWonWithSwitch;
      
                                                                                                       

      /**
       * default duration for move processes
       */
      public final static Timing  defaultDuration = new TicksTiming(30);

      /**
       * main method that prepares and displays the detailed and fast iterations
       * for the monty hall problem
       * 
       * @param detailedIterations
       *          number of detailed iterations
       *          
       *  @param fastIterations
       *          number of fast iterations
       */
      public void montyHallStart(int detailedIterations, int fastIterations, SourceCodeProperties textProps) {
    	
    	  numberGames = 0;
          numberWonWithoutSwitch = 0;
          numberWonWithSwitch = 0;
            
    	// create string array for the three doors
    	  String[] a = {"Tür 1", "Tür 2", "Tür 3"};
    	
    	// Create header
    	
    	TextProperties headerProps = new TextProperties();
    	Font headerFont = (Font) headerProps.get("font");
    	headerFont = headerFont.deriveFont(1,36);
    	headerProps.set("font", headerFont);
        
    	String headerText = "Monty Hall problem";
        Text header = lang.newText(new Coordinates(0, 0), headerText, "header", null, headerProps);
        
        
    	// create initial description  
    	  //SourceCodeProperties descrProps = new SourceCodeProperties();
    	  SourceCode descr = lang.newSourceCode(new Offset(20,20, header, AnimalScript.DIRECTION_SW), "descr",
    		        null, textProps);
    	  descr.addCodeLine("Angenommen sie sind in einer Spielshow und haben die Wahl", null, 0,null);
    	  descr.addCodeLine("sich zwischen 3 Türen zu entscheiden. Hinter einer Tür", null, 0,null);
    	  descr.addCodeLine("befindet sich ein Auto, hinter den anderen beiden Ziegen.", null, 0,null);
    	  descr.addCodeLine("Nachdem sie sich für eine Tür, z.B. Nummer 3 entschieden haben", null, 0,null);
    	  descr.addCodeLine("öffnet der Showmaster, der weiss hinter welchen Türen Ziegen", null, 0,null);
    	  descr.addCodeLine("sind, eine Tür, z.B. Nummer 1, hinter der eine Ziege ist.", null, 0,null);
    	  descr.addCodeLine("Dann gibt er ihnen die Option sich statt für Tür Nummer 3", null, 0,null);
    	  descr.addCodeLine("für Tür Nummer 2 zu entscheiden.", null, 0,null);
    	  descr.addCodeLine("", null, 0,null);
    	  descr.addCodeLine("Ist es für sie von Vorteil die Tür zu wechseln?", null, 0,null);
    	  
    	  
        // create array
        ArrayProperties arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED);
        arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
            Color.BLACK);
        StringArray ia = lang.newStringArray(new Offset(20, 100, header, AnimalScript.DIRECTION_SW), 
        	    		a, "ia", null, arrayProps);
        ia.hide();
        
        
        lang.nextStep("Einleitung");
        
        descr.hide();
        ia.show();
        

        // create pseudo code
       /* SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
            new Font("Monospaced", Font.PLAIN, 12));

        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			*/
        SourceCode sc = lang.newSourceCode(new Offset(0,40, ia, AnimalScript.DIRECTION_SW), "sourceCode",
            null, textProps);
        
        sc.addCodeLine("1. Zwei Ziegen(rot) und ein Auto(grün) werden zufällig hinter den Türen platziert.", null, 0,
            null); // 0
        sc.addCodeLine("2. Der Spieler entscheidet sich ohne Kenntnis was hinter welcher", null, 1, null); //1
        sc.addCodeLine("   Tür ist für eine der Türen.", null, 1, null); // 2
        sc.addCodeLine("3. Der Showmaster öffnet eine vom Spieler nicht gewählte Tür hinter der sich eine Ziege befindet.", null, 2, null); // 3
        sc.addCodeLine("4a. Fall A: Der Spieler bleibt bei seiner Entscheidung.", null, 3, null); // 4
        sc.addCodeLine("4b. Fall B: Der Spieler wechselt die gewählte Tür.", null, 3, null); // 5
        
        
        
        
        // create empty Text displays for total number of games and won games
        Text gamesCount = lang.newText(new Offset(140,40, header, AnimalScript.DIRECTION_NE), "", "gamesCount", null);
        Text withoutSwitchCount = lang.newText(new Offset(0,10, gamesCount, AnimalScript.DIRECTION_SW), "", "withoutSwitchCount", null);
        Text withSwitchCount = lang.newText(new Offset(0,10, withoutSwitchCount, AnimalScript.DIRECTION_SW), "", "withSwitchCount", null);
        
        
        // Create array markers for choosen doors
     	ArrayMarkerProperties firstProps = new ArrayMarkerProperties();
         firstProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "A");
         firstProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
         firstProps.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, true);
         ArrayMarker firstMarker = lang.newArrayMarker(ia, 0, "firstMarker",
             null, firstProps);
     	firstMarker.hide();
         
         ArrayMarkerProperties secondProps = new ArrayMarkerProperties();
         secondProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "B");
         secondProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
         ArrayMarker secondMarker = lang.newArrayMarker(ia, 0, "secondMarker",
             null, secondProps);
        secondMarker.hide();
        // Update the counter once to display it
        display_status(gamesCount, withoutSwitchCount, withSwitchCount);
        
        lang.nextStep("Detailierte Iterationen");
        

        //**************************************************************************************
        
        // Perform step by step iterations
        for(int i=0;i<detailedIterations;i++) {
        	multi_step_monty_hall(ia, firstMarker, secondMarker, sc, gamesCount, withoutSwitchCount, withSwitchCount);
        }
        
        
        // Highlight complete pseudo code for fast iterations
        for(int i=0; i<6;i++) {
        	sc.highlight(i, 0, false);
        }
        lang.nextStep("Schnelle Iterationen");
        
        // Perform everything in one animation step
        for(int i=0;i<fastIterations;i++) {
        	single_step_monty_hall(ia,firstMarker, secondMarker,  sc, gamesCount, withoutSwitchCount, withSwitchCount);
        }
        
        
        //**************************************************************************************
        // Conclusion
        ia.hide();
        sc.hide();
        

    	// create conclusion description  
    	  //SourceCodeProperties endProps = new SourceCodeProperties();
    	  SourceCode endDescr = lang.newSourceCode(new Offset(20,20, header, AnimalScript.DIRECTION_SW), "endDescr",
    		        null, textProps);
    	  endDescr.addCodeLine("Für das Standard Monty Hall Problem besteht beim Wechsel", null, 0,null);
    	  endDescr.addCodeLine("eine zweidrittel Chance auf einen Gewinn.", null, 0,null);
    	  endDescr.addCodeLine("Das sollte spätestens durch die schnellen Iterationen", null, 0,null);
    	  endDescr.addCodeLine("deutlich geworden sein.", null, 0,null);
    	  endDescr.addCodeLine("", null, 0,null);
    	  endDescr.addCodeLine("Dies ist bei weitem nicht direkt ersichtlich. Wichtig zu beachten ist, dass", null, 0,null);
    	  endDescr.addCodeLine("die vom Showmaster geöffnete Tür nicht willkürlich ist. Er muss eine", null, 0,null);
    	  endDescr.addCodeLine("Tür öffnen die nicht vom Spieler gewählt wurde und hinter der kein Gewinn ist.", null, 0,null);
    	  endDescr.addCodeLine("Um das Problem inuitiver zu machen stelle man sich vor es gäbe eine Million Türen", null, 0,null);
    	  endDescr.addCodeLine("der Spieler wählt eine davon und der Showmaster öffnet alle verbleibenden", null, 0,null);
    	  endDescr.addCodeLine("Türen bis auf eine. Würden sie unter diesen Umständen wechseln oder", null, 0,null);
    	  endDescr.addCodeLine("bei ihrer ersten Wahl bleiben?", null, 0,null);
    	  

        
        lang.nextStep("Fazit");
        
      }
      
      /**
       * displays the detailed iterations
       * for the monty hall problem
       * 
       * @param ia
       *        the initial array representing the closed doors
       * 
       * @param firstMarker
       *          the Array marker for the first choice
       *          
       * @param secondMarker
       *          the Array marker for the second choice
       *          
       * @param sc
       *        the pseudo code to be highlighted  
       *          
       * @param gamesCount
       *          the text counter for the total number of games
       *          
       * @param withoutSwitchCount 
       *        the text counter for the total number of games won without switching
       *            
       * @param withSwitchCount
       * 		the text counter for the total number of games won with switching
       *          
       */
      private void multi_step_monty_hall(StringArray ia, ArrayMarker firstMarker, ArrayMarker secondMarker, SourceCode sc, Text gamesCount, Text withoutSwitchCount, Text withSwitchCount) {
    	// setup variables for winning, choosen and opened doors
    	int winningDoor = (int)(3 * Math.random());  
    	int firstChoice = (int)(3 * Math.random());
    	int doorToBeOpened = getDoorToBeOpened(winningDoor, firstChoice);
    	int secondChoice = getSecondChoice(firstChoice, doorToBeOpened);
    	
    	// Mark winning door
    	ia.highlightElem(winningDoor, null, null);
    	sc.highlight(0, 0, false);
    	
    	lang.nextStep();
    	
    	// Mark initial choice
    	firstMarker.show();
    	secondMarker.show();
    	
    	firstMarker.move(firstChoice, null, null);
    	secondMarker.move(firstChoice, null, null);
        
    	sc.unhighlight(0, 0, false);
    	sc.highlight(1, 0, false);
    	sc.highlight(2, 0, false);
    	
    	lang.nextStep();
    	
    	// Mark opened door
    	ia.highlightCell(doorToBeOpened, null, null);
    	
    	sc.unhighlight(1, 0, false);
    	sc.unhighlight(2, 0, false);
    	sc.highlight(3, 0, false);
    	
    	lang.nextStep();
    	
    	// Mark change in doors for second choice
    	secondMarker.move(secondChoice, defaultDuration, defaultDuration);
    	
    	sc.unhighlight(3, 0, false);
    	sc.highlight(4, 0, false);
    	sc.highlight(5, 0, false);
    	
    	lang.nextStep();
    	
    	// Remove Markers
    	ia.unhighlightCell(doorToBeOpened, null, null);
    	ia.unhighlightElem(winningDoor, null, null);
    	firstMarker.hide();
    	secondMarker.hide();

    	sc.unhighlight(4, 0, false);
    	sc.unhighlight(5, 0, false);
    	
    	// Update counters
    	numberGames += 1;
    	if(winningDoor == firstChoice) {numberWonWithoutSwitch += 1;}
    	if(winningDoor == secondChoice) {numberWonWithSwitch += 1;}
    	// Update the counters
    	display_status(gamesCount, withoutSwitchCount, withSwitchCount);
    	
    	
    	lang.nextStep();
    	      
    	  
      }
      
      /**
       * displays the fast iterations
       * for the monty hall problem
       * 
       * @param ia
       *        the initial array representing the closed doors
       * 
       * @param firstMarker
       *          the Array marker for the first choice
       *          
       * @param secondMarker
       *          the Array marker for the second choice
       *          
       * @param sc
       *        the pseudo code to be highlighted  
       *          
       * @param gamesCount
       *          the text counter for the total number of games
       *          
       * @param withoutSwitchCount 
       *        the text counter for the total number of games won without switching
       *            
       * @param withSwitchCount
       * 		the text counter for the total number of games won with switching
       *          
       */
      private void single_step_monty_hall(StringArray ia, ArrayMarker firstMarker, ArrayMarker secondMarker, SourceCode sc, Text gamesCount, Text withoutSwitchCount, Text withSwitchCount) {
    	int winningDoor = (int)(3 * Math.random());  
    	int firstChoice = (int)(3 * Math.random());
    	int doorToBeOpened = getDoorToBeOpened(winningDoor, firstChoice);
    	int secondChoice = getSecondChoice(firstChoice, doorToBeOpened);
    	
    	
    	// Mark winning door
    	ia.highlightElem(winningDoor, null, null);
    	
    	// Mark choices
    	firstMarker.show();
    	secondMarker.show();
    	
    	firstMarker.move(firstChoice, null, null);
    	secondMarker.move(secondChoice, null, null);
        
    	// Mark opened door
    	ia.highlightCell(doorToBeOpened, null, null);
    	
    	// Update counters
    	numberGames += 1;
    	if(winningDoor == firstChoice) {numberWonWithoutSwitch += 1;}
    	if(winningDoor == secondChoice) {numberWonWithSwitch += 1;}
    	// Update the counters
    	display_status(gamesCount, withoutSwitchCount, withSwitchCount);
    	
    	
    	lang.nextStep();
    	
    	// Remove Markers
    	ia.unhighlightCell(doorToBeOpened, null, null);
    	ia.unhighlightElem(winningDoor, null, null);
    	firstMarker.hide();
    	secondMarker.hide();
    	
      }
      
      /**
       * determines which door should be opened by the show master
       * 
       * @param winningDoor
       *        the array index number of the winning door
       * 
       * @param firstChoice
       *        the array index number of the door initially selected by the player
       *          
       */
      private int getDoorToBeOpened(int winningDoor, int firstChoice) {
    	int door = 0;
    	int random = (int)(Math.random()*2);
    	if(firstChoice == 0) {
    		if(winningDoor == 0) {
    			if(random < 1) {
    				door = 1;
    			}else {
    				door = 2;
    			}
    			
    		}else if(winningDoor == 1) {
    			door = 2;
    		}else if(winningDoor == 2) {
    			door = 1;
    		}
    	}
    	
    	if(firstChoice == 1) {
    		if(winningDoor == 1) {
    			if(random < 1) {
    				door = 0;
    			}else {
    				door = 2;
    			}
    			
    		}else if(winningDoor == 0) {
    			door = 2;
    		}else if(winningDoor == 2) {
    			door = 0;
    		}
    	}
    	
    	if(firstChoice == 2) {
    		if(winningDoor == 2) {
    			if(random < 1) {
    				door = 0;
    			}else {
    				door = 1;
    			}
    			
    		}else if(winningDoor == 0) {
    			door = 1;
    		}else if(winningDoor == 1) {
    			door = 0;
    		}
    	}
    	
    	return door;
      }
      
      
      /**
       * determines which door should be switched to as the second choice of the player
       * 
       * 
       * @param firstChoice
       *        the array index number of the door initially selected by the player
       * 
       * @param doorToBeOpened
       *        the array index number of the door opened by the show master
       * 
       */
      private int getSecondChoice(int firstChoice, int doorToBeOpened) {
    	  for(int i = 0; i<3; i++) {
    		  if(i != firstChoice && i != doorToBeOpened) {return i;}
    	  }
    	  return 0;
      }
      
      

      /**
       * displays the total number of games played as well as the number won with and without switching
       * 
       * 
       * @param totalText
       * 		number of total games played
       * 
       * @param withoutSwitchCount
       * 		number of games won without switching
       * 
       * @param withSwitchCount
       * 		number of games won with switching
       * 
       */
      private void display_status(Text totalText, Text withoutSwitchCount, Text withSwitchCount) {
    	  String gamesText = "Versuche: " + Integer.toString(numberGames);
    	  totalText.setText(gamesText, null, null);
    	  String withoutText = "Gewinn ohne Wechsel: " + Integer.toString(numberWonWithoutSwitch);
    	  withoutSwitchCount.setText(withoutText, null, null);
    	  String withText = "Gewinn mit Wechsel: " + Integer.toString(numberWonWithSwitch);
    	  withSwitchCount.setText(withText, null, null);
      }
      
      
      /**
       * main method
       * @param args
       * 		args
       */
      public static void main(String[] args) {  
    	  MontyHallProblem s = new MontyHallProblem();
    	  Animal.startGeneratorWindow(s);
      }

    
}